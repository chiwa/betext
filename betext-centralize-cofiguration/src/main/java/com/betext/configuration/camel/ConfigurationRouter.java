package com.betext.configuration.camel;

import com.betext.configuration.service.LoadConfigurationService;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class ConfigurationRouter extends RouteBuilder {

    private final Logger log = LogManager.getLogger(ConfigurationRouter.class);

    @Override
    public void configure() throws Exception {
        int intervalInMinute = (60 * 1000) * 1;
        from("timer://checkIfNeedReloadConfig?period=" + intervalInMinute)
                .routeId("checkIfNeedReloadConfig")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        LoadConfigurationService.checkIfNeedReloadConfig();
                    }
                })
                .end();
    }
}
