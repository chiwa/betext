package com.betext.configuration.service;


import com.betext.transportation.object.ConfigurationResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class ConfigurationClient {

    private static DefaultHttpClient client = new DefaultHttpClient();
    private static String centralizeConfigurationURL = "http://localhost:7777/betext-centralize-cofiguration/";

    public static String getCentralizeConfigurationURL() {
        return centralizeConfigurationURL;
    }

    public static void setCentralizeConfigurationURL(String centralizeConfigurationURL) {
        ConfigurationClient.centralizeConfigurationURL = centralizeConfigurationURL;
    }

    /**
     * get configuration by key
     * @param key
     * @return
     */
    public static ConfigurationResponse getConfigurationByKey(String key) throws IOException {
        HttpGet getMethod = new HttpGet(centralizeConfigurationURL + "/configuration/" + key);
        HttpResponse response = client.execute(getMethod);
        // Get the response
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String line = "";
        StringBuffer responseMessage = new StringBuffer();
        while ((line = rd.readLine()) != null) {
            responseMessage.append(line);
        }

        ObjectMapper mapper = new ObjectMapper();
        ConfigurationResponse configurationResponse = mapper.readValue(responseMessage.toString(), ConfigurationResponse.class);
        return configurationResponse;
    }

    /**
     * get all configurations
     * @return
     */
    public static List<ConfigurationResponse> getAllConfigiruations() throws IOException {
        HttpGet getMethod = new HttpGet(centralizeConfigurationURL + "/configurations");
        HttpResponse response = client.execute(getMethod);
        // Get the response
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String line = "";
        StringBuffer responseMessage = new StringBuffer();
        while ((line = rd.readLine()) != null) {
            responseMessage.append(line);
        }
        ObjectMapper mapper = new ObjectMapper();
        List<ConfigurationResponse> list = mapper.readValue(responseMessage.toString(), TypeFactory.collectionType(List.class, ConfigurationResponse.class));
        return list;
    }

    public static void main(String[] args) throws IOException {
       System.out.println("getAllConfigiruations" + getAllConfigiruations());
       System.out.println("getConfigurationByKey : " + getConfigurationByKey("name"));
    }
}
