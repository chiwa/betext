package com.betext.rest.camel;


import org.apache.camel.builder.RouteBuilder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class CamelRouter extends RouteBuilder {

    private final Logger log = LogManager.getLogger(CamelRouter.class);


    @Override
    public void configure() throws Exception {
        from("seda:test")
                .threads(20)
                .bean(HelloBean.class)
                .end();
    }
}
