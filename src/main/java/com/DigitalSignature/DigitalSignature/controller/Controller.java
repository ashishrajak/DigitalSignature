package com.DigitalSignature.DigitalSignature.controller;

import com.DigitalSignature.DigitalSignature.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Controller {
  private   TestService testService;

  public   Controller(TestService testService){
        this.testService=testService;
    }
    @GetMapping("/test")
    public List<String> testString(){
        return List.of(testService.testString(),"11","33","44");
    }

}
