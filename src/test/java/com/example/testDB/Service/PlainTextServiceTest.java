package com.example.testDB.Service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application_test.properties")
class PlainTextServiceTest {

    @Autowired
    PlainTextService plainTextService;

    @Test
    public void displayTest() {
        plainTextService.putPlainText("apple");
        plainTextService.displayPlainText();
    }

}