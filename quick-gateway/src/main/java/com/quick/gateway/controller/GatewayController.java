package com.quick.gateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class GatewayController {

    @RequestMapping("/test")
    public String getGateway(){
        return "hello";
    }
}
