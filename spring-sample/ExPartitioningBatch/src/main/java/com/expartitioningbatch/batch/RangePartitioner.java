package com.expartitioningbatch.batch;

import com.expartitioningbatch.repository.AlphabetDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class RangePartitioner implements Partitioner{

    @Autowired
    AlphabetDB alphabetDB;

    static final Logger logger = LoggerFactory.getLogger(RangePartitioner.class);
    @Override
    public Map<String, ExecutionContext> partition(int threadCount) {
        Map<String, ExecutionContext> result = new HashMap<String, ExecutionContext>();

        int range = 3;
        int fromId = 0;
        int toId = range;

        for (int i = 0; i < threadCount; i++) {
            ExecutionContext value = new ExecutionContext();

            logger.info("fromId {}, toId {} ", fromId, toId);

            value.putInt("fromId", fromId);
            value.putInt("toId", toId);

            // give each thread a name, thread 1,2,3
            value.putString("name", "Thread" + i);

            result.put("partition" + i, value);

            fromId = toId + 1;
            toId += range;

            if (toId >= alphabetDB.size()) {
                toId = alphabetDB.size()-1;
            }
        }

        return result;
    }
}
