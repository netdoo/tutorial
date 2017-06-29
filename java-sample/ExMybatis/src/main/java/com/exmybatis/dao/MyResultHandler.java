package com.exmybatis.dao;


import com.exmybatis.domain.User;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MyResultHandler implements ResultHandler {

    private static final Logger logger = LoggerFactory.getLogger(MyResultHandler.class);

    List<String> nameList = new ArrayList<>();

    @Override
    public void handleResult(ResultContext resultContext) {
        User user = (User) resultContext.getResultObject();
        logger.info("count {} data {}",  resultContext.getResultCount(), user.toString());

        /// 필요한 데이터만 추출함.
        nameList.add(user.getUserName());
    }

    public List<String> getResult() {
        return nameList;
    }
}
