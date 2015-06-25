package com.betext.rest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {


    @RequestMapping(value = "/index.html")
    public String index() {
        return "index";
    }


}
