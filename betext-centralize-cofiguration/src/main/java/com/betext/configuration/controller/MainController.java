package com.betext.configuration.controller;

import com.betext.configuration.service.LoadConfigurationService;
import com.betext.transportation.object.ConfigurationResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

@Controller
public class MainController {


    @RequestMapping(value = "/index.html")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/configurations", method = {RequestMethod.GET}, produces = "application/json")
    public @ResponseBody
    List<ConfigurationResponse> getConfiguration() throws IOException, SAXException, ParserConfigurationException {
        return LoadConfigurationService.loadAllConfiguration();
    }

    @RequestMapping(value = "/configuration/{key}", method = {RequestMethod.GET}, produces = "application/json")
    public @ResponseBody ConfigurationResponse getConfigurationByKey(@PathVariable String key) throws ParserConfigurationException, SAXException, IOException {
        ConfigurationResponse configurationResponse = new ConfigurationResponse();
        configurationResponse.setKey(key);
        configurationResponse.setValue(LoadConfigurationService.getConfigurationByKey(key));
        return configurationResponse;
    }



}
