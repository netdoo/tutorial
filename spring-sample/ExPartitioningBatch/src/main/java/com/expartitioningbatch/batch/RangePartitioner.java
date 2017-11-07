package com.expartitioningbatch.batch;

import com.expartitioningbatch.domain.ExecutionContextParam;
import com.expartitioningbatch.repository.AlphabetDB;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RangePartitioner implements Partitioner{

    @Autowired
    AlphabetDB alphabetDB;

    String fileName;
    String data;
    String date;

    static final Logger logger = LoggerFactory.getLogger(RangePartitioner.class);

    @Value("#{jobParameters['fileName']}")
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getFileName() {
        return this.fileName;
    }

    @Value("#{jobParameters['date']}")
    public void setDate(String date) {
        this.date = date;
    }
    public String getDate() {
        return this.date;
    }

    public void setData(String data) {
        this.data = data;
    }
    public String getData() {
        return this.data;
    }

    @Override
    public Map<String, ExecutionContext> partition(int slaveThreadCount) {
        Map<String, ExecutionContext> result = new HashMap<String, ExecutionContext>();
        int partitionSize = Math.round(this.alphabetDB.size() / slaveThreadCount) + 1;
        List<List<String>> slaveDataGroupList = Lists.partition(this.alphabetDB, partitionSize);

        for (int i = 0; i < slaveDataGroupList.size(); i++) {
            ExecutionContext executionContext = new ExecutionContext();

            ExecutionContextParam executionContextParam = new ExecutionContextParam();
            executionContextParam.setName("partition" + i);

            ArrayList<String> targetList = new ArrayList<>();
            targetList.addAll(slaveDataGroupList.get(i));
            executionContextParam.setTargetList(targetList);

            executionContext.put("name", "partition." + i);
            executionContext.put("param", executionContextParam);

            result.put("partition." + i, executionContext);
        }

        return result;
    }
}
