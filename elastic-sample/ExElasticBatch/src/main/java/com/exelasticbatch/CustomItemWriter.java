package com.exelasticbatch;

import java.util.List;

import com.exelasticbatch.model.Alphabet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

public class CustomItemWriter implements ItemWriter<List<Alphabet>> {

    static final Logger logger = LoggerFactory.getLogger(CustomItemWriter.class);

    @Override
    public void write(List<? extends List<Alphabet>> alphabets) throws Exception {

        // alphabets 배열은 /batch/job.xml에 설정된 commit-interval 숫자만큼 전달됨.
        // 예) commit-interval : 1인 경우, alphabets 의 size 는 1
        // 예) commit-interval : 2인 경우, alphabets 의 size 는 2

        alphabets.forEach(chunkedAlphabets -> {
            chunkedAlphabets.forEach(alphabet -> {
                logger.info("{}", alphabet);
            });
        });
    }
}

