package com.DigitalSignature.DigitalSignature.service;

import com.DigitalSignature.DigitalSignature.controller.Controller;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TestServiceTest {
    @Autowired
    private Controller testService;
    @Test
    void testString(){
        List<String> list = List.of("bar", "testing", "baz");

        // Assert that the list contains exactly one "testing"
        assertThat(list).containsExactlyInAnyOrder("bar", "testing", "baz");


    }

}