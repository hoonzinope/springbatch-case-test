package com.example.testDB.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface ResultTextMapper {
    void putText(Map<String, Object> requestData);
    void putTexts(List<Map<String, Object>> requestData);
}
