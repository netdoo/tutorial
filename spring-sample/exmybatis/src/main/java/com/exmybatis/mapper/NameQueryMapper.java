package com.exmybatis.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public interface NameQueryMapper {
    List<String> getNames(@Param("names") List names);
    List<String> getUserNames(HashMap params);
    int updateUser(HashMap params);
}
