package com.rest.api.core;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestApiController {

    @GetMapping("/hello")
    public String getHello(){
        return "Hello XDXD";
    }

}
