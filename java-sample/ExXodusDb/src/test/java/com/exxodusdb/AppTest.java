package com.exxodusdb;

import jetbrains.exodus.ByteIterable;
import jetbrains.exodus.env.*;
import org.jetbrains.annotations.NotNull;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static jetbrains.exodus.bindings.StringBinding.entryToString;
import static jetbrains.exodus.bindings.StringBinding.stringToEntry;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppTest {

    final static Logger logger = LoggerFactory.getLogger(AppTest.class);

    @Test
    public void _0_테스트_준비() throws Exception {
        TestEnv.cleanUp();
    }

    @Test
    public void _1_CRUD_테스트() throws Exception {

        Environment env = Environments.newInstance(TestEnv.dbPath);
        Store store = env.computeInTransaction(txn -> env.openStore(TestEnv.storeName, StoreConfig.WITHOUT_DUPLICATES, txn));

        // 데이터베이스에 접근하기 위해서는 트랜잭션이 필요함.
        // If you have an exclusive transaction,
        // no other transaction (except read-only) can be started against the same Environment
        // unless you finish (commit or abort) the exclusive transaction.

        // 1. 쓰기 전용 트랜잭션 (exclusive transaction)
        env.executeInTransaction(new TransactionalExecutable() {
            @Override
            public void execute(@NotNull Transaction transaction) {
                store.put(transaction, stringToEntry("Hello"), stringToEntry("World!"));
            }
        });

        // 2. 읽기/쓰기 가능한 트랜잭션 (exclusive transaction
        String value = env.computeInTransaction(new TransactionalComputable<String>() {
            @Override
            public String compute(@NotNull Transaction transaction) {
                return entryToString(store.get(transaction, stringToEntry("Hello")));
            }
        });

        logger.info("value {}", value);

        // 3. 읽기전용 트랜잭션
        String notExistValue = env.computeInReadonlyTransaction(new TransactionalComputable<String>() {
            @Override
            public String compute(@NotNull Transaction transaction) {
                ByteIterable result = store.get(transaction, stringToEntry("Bye"));
                return (result == null) ? "" : entryToString(result);
            }
        });

        logger.info("notExistValue {}", notExistValue);


        // 4. 더미 데이터 입력
        env.executeInTransaction(new TransactionalExecutable() {
            @Override
            public void execute(@NotNull Transaction transaction) {
                store.add(transaction, stringToEntry("1"), stringToEntry("Red"));
                store.add(transaction, stringToEntry("2"), stringToEntry("Green"));
                store.add(transaction, stringToEntry("3"), stringToEntry("Blue"));
            }
        });

        // 5. 일부 데이터 삭제
        env.executeInTransaction(new TransactionalExecutable() {
            @Override
            public void execute(@NotNull Transaction transaction) {

                // k 값이 존재하면 삭제하고 true 반환
                boolean firstResult = store.delete(transaction, stringToEntry("1"));

                // k 값이 없으면 false 반환
                boolean secondResult = store.delete(transaction, stringToEntry("1"));
                logger.info("firstResult {} secondResult {}", firstResult, secondResult);
            }
        });

        // 6. 전체 데이터 조회
        env.executeInReadonlyTransaction(txn -> {
            try (Cursor cursor = store.openCursor(txn)) {
                while (cursor.getNext()) {
                    String key = entryToString(cursor.getKey());   // current key
                    String val = entryToString(cursor.getValue()); // current value
                    logger.info("k {} v {}", key, val);
                }
            }
        });

        // 9. DB Close
        env.close();
    }

    @Test
    public void _99_테스트_종료() throws Exception {

    }
}
