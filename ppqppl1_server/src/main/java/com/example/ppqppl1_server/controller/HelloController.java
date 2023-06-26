package com.example.ppqppl1_server.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

//@Component
@RestController
public class HelloController {
    @RequestMapping(value="/hello",method = RequestMethod.GET)
    public String showHello(){
        return "Hello ppqppl";
    }
}
