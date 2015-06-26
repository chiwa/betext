package com.betext.rest.controller;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @Autowired
    private ProducerTemplate producerTemplate;

    @RequestMapping(value = "/index.html")
    public String index() {
        for (int i=1; i<=10; i++)
          producerTemplate.sendBody("seda:test" , "Hello-" + i);
        return "index";
    }


}
