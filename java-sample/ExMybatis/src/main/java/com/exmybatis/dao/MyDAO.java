package com.exmybatis.dao;

import com.exmybatis.App;
import com.exmybatis.domain.User;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MyDAO {

    private static final Logger logger = LoggerFactory.getLogger(MyDAO.class);

    @Autowired
    private SqlSession sql;

    public String getSuperAdmin(String userType) throws SQLException {
        return sql.selectOne("database.getSuperAdmin", userType);
    }

    public List<User> getAllUser() throws SQLException {
        return sql.selectList("database.getAllUser");
    }

    public void printAllUserByResultHandler() throws SQLException {
        MyResultHandler resultHandler = new MyResultHandler();
        sql.select("database.getAllUser", resultHandler);
        List<String> nameList = resultHandler.getResult();
        logger.info("result {} ", nameList);
    }

    public List<User> getPagingUserList(long offset, long noOfRecords) throws SQLException {
        Map<String, Long> param = new HashMap<String, Long>();

        param.put("offset", new Long(offset));
        param.put("noOfRecords", new Long(noOfRecords));

        return sql.selectList("database.getPagingUserList", param);
    }

    public List<String> getAdminUserNameList() throws SQLException {

        Map<String, String> param = new HashMap<String, String>();
        param.put("one", "ADMIN");
        param.put("two", "SA");
        return sql.selectList("database.getAdminUserNameList", param);
    }

    public List<String> getAllUserNameList() throws SQLException {
        return sql.selectList("database.getAllUserNameList");
    }

    public void updateAdminName() throws SQLException {

        Map<String, String> param = new HashMap<String, String>();
        param.put("findAdminName", "루트 관리자");
        param.put("newAdminName", "root 관리자");

        sql.update("database.updateAdminName", param);
    }

    public List<String> findAdminEMail() throws SQLException {
        User admin = new User();
        admin.setUserId("root");
        admin.setUserType("ADMIN");
        List<String> adminEmails = sql.selectList("database.findAdminEMail", admin);
        return adminEmails;
    }

    public void saveAdmin(String userId, String userName, String userType) throws SQLException {
        Map<String, String> param = new HashMap<String, String>();
        param.put("user_id", userId);
        param.put("user_name", userName);
        param.put("user_type", userType);
        sql.insert("database.saveAdmin", param);
    }
}
