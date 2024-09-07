package com.DigitalSignature.DigitalSignature.controller;

import com.DigitalSignature.DigitalSignature.service.TestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ControllerTest {
    @Mock
    TestService testService;
    @Autowired
    MockMvc mockMvc;

    Controller controller;
    @BeforeEach
    void setUp(){
        this.controller=new Controller(this.testService);
        Mockito.when(testService.testString()).thenReturn("value1");


    }
    @Test
    void test(){


    }

}