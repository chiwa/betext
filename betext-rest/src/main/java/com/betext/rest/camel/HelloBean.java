package com.betext.rest.camel;


import org.apache.camel.Exchange;

public class HelloBean {

    public void handleMessage(Exchange exchange) throws InterruptedException {
        String name = exchange.getIn().getBody(String.class);
        for(int i=1; i<=10;i++) {
            Thread.sleep(20);
            System.out.println(name + "===>> " + i);
        }
    }
}
