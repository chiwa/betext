package com.betext.configuration.service;


import com.betext.transportation.object.ConfigurationResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadConfigurationService {
    private static  final Logger log = LogManager.getLogger(LoadConfigurationService.class);

    public static Map<String, String> result = new HashMap<String, String>();
    public static List<ConfigurationResponse> configurationResponseList = new ArrayList<ConfigurationResponse>();
    private static String configurationPath;
    private static String localConfiguration;
    private static long lastModify = 0;


    public static String getConfigurationPath() {
        return configurationPath;
    }

    public static void setConfigurationPath(String configurationPath) {
        LoadConfigurationService.configurationPath = configurationPath;
    }

    public static String getLocalConfiguration() {
        return localConfiguration;
    }

    public static void setLocalConfiguration(String localConfiguration) {
        LoadConfigurationService.localConfiguration = localConfiguration;
    }

    public synchronized static List<ConfigurationResponse> loadAllConfiguration() throws ParserConfigurationException, SAXException, IOException {
        if (result.isEmpty()) {
            initConfigurationValue();
        }
        return configurationResponseList;
    }

    public static String getConfigurationByKey(String key) throws IOException, SAXException, ParserConfigurationException {
        if (result.isEmpty()) {
            loadAllConfiguration();
        }
        if (result.containsKey(key)) {
            return result.get(key);
        }
        return "null";
    }

    public static synchronized void initConfigurationValue() throws IOException, SAXException, ParserConfigurationException {
        InputStream inputStream ;
        if (configurationPath == null || !(new File(configurationPath).exists())) {
            String configFile = ("/" + localConfiguration);
            if(configurationPath != null)
            {
                configFile = configurationPath;
                if(!(new File(configFile).exists()))
                {
                    configFile = ("/" + localConfiguration);
                }
            }
            log.info("Loading configuration for Local Environment : " + configFile);
            inputStream = LoadConfigurationService.class.getResourceAsStream(configFile);
        } else {
            log.info("Loading configuration for None Test Environment.");
            inputStream = new FileInputStream(configurationPath);
        }
        log.info("Configuration Path : " + configurationPath);

        Document doc = parseXML(inputStream);
        NodeList descNodes = doc.getElementsByTagName("property");
        result.clear();
        configurationResponseList.clear();
        for (int i = 0; i < descNodes.getLength(); i++) {
            String key = descNodes.item(i).getAttributes().getNamedItem("key").getTextContent();
            String value = descNodes.item(i).getTextContent();
            log.info(key + " : " + value);
            result.put(key, value);
            ConfigurationResponse configurationResponse = new ConfigurationResponse();
            configurationResponse.setKey(key);
            configurationResponse.setValue(value);
            configurationResponseList.add(configurationResponse);
        }
    }

    private static Document parseXML(InputStream stream) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory objDocumentBuilderFactory = null;
        DocumentBuilder objDocumentBuilder = null;
        Document doc = null;
        objDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
        objDocumentBuilder = objDocumentBuilderFactory.newDocumentBuilder();
        doc = objDocumentBuilder.parse(stream);
        return doc;
    }

    public static void checkIfNeedReloadConfig() throws ParserConfigurationException, SAXException, IOException {
          File file = new File(configurationPath);
          if (!file.exists()) {
              return;
          }
          if (lastModify == 0) {
            lastModify  = file.lastModified();
          }
          if (lastModify != file.lastModified()) {
              log.info("Configuration was modified need to reload configuration..........");
              lastModify  = file.lastModified();
              initConfigurationValue();
          }

    }
}
