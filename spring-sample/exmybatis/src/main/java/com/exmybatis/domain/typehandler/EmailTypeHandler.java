package com.exmybatis.domain.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class EmailTypeHandler extends BaseTypeHandler<ArrayList<String>> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, ArrayList<String> strings, JdbcType jdbcType) throws SQLException {
        String param = String.join(",", strings);
        preparedStatement.setString(i, param);
    }

    @Override
    public ArrayList<String> getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String rs = resultSet.getString(s);
        ArrayList<String> emails = new ArrayList<>(Arrays.asList(rs.split(",")));
        return emails;
    }

    @Override
    public ArrayList<String> getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String rs = resultSet.getString(i);
        ArrayList<String> emails = new ArrayList<>(Arrays.asList(rs.split(",")));
        return emails;
    }

    @Override
    public ArrayList<String> getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String rs = callableStatement.getString(i);
        ArrayList<String> emails = new ArrayList<>(Arrays.asList(rs.split(",")));
        return emails;
    }
}
