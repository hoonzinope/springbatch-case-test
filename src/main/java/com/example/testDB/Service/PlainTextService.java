package com.example.testDB.Service;

import com.example.testDB.mapper.PlainTextMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PlainTextService {
    @Autowired
    PlainTextMapper plainTextMapper;

    public void displayPlainText(){
        plainTextMapper.getAll().stream().forEach(System.out::println);
    }
    public void putPlainText(String text) {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("text", text);

        plainTextMapper.putText(requestData);
    }
}
