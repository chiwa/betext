package com.betext.usermanagement.camel;


import com.betext.usermanagement.services.authentication.ImpTokenAuthenticationService;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CamelRouter extends RouteBuilder {

    private final Logger log = LogManager.getLogger(CamelRouter.class);

    @Autowired
    private ImpTokenAuthenticationService tokenAuthenticationService;

    @Override
    public void configure() throws Exception {
        /*int intervalInMinute = (60 * 1000) * 5;
        from("timer://clearExpireToken?period=" + intervalInMinute)
                .routeId("clearExpireToken")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        log.info("Timer to clear expired token......");
                        tokenAuthenticationService.clearAllTokenOnceAday();
                    }
                })
                .end();*/

        String cron = "0 59 23 ? * *";
        from("quartz://reconcile?fireNow=false&stateful=true&cron=" + cron + "&trigger.repeatCount=-1")
                .id("clearToken")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        log.info("Scheduler to clear token......");
                        tokenAuthenticationService.clearAllTokenOnceADay();
                    }
                })
                .end();
    }
}
