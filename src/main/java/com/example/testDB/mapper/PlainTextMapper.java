package com.example.testDB.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface PlainTextMapper {
    List<Map<String,Object>> getAll();
    void putText(Map<String, Object> requestData);
}
