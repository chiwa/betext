package com.betext.transportation.service;


import com.betext.transportation.object.ConfigurationResponse;
import com.betext.transportation.object.TokenObject;
import com.betext.transportation.object.UserSignOnObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class BeTEXTClientService {

    private static DefaultHttpClient client = new DefaultHttpClient();
    private static String centralizeConfigurationURL = "http://localhost:7777/betext-centralize-cofiguration/";
    private static String userManagementURL = "http://localhost:8888/betext-usermanagement/";

    public static String getUserManagementURL() {
        return userManagementURL;
    }

    public static void setUserManagementURL(String userManagementURL) {
        BeTEXTClientService.userManagementURL = userManagementURL;
    }

    public static String getCentralizeConfigurationURL() {
        return centralizeConfigurationURL;
    }

    public static void setCentralizeConfigurationURL(String centralizeConfigurationURL) {
        BeTEXTClientService.centralizeConfigurationURL = centralizeConfigurationURL;
    }

    /**
     * get configuration by key
     * @param key
     * @return
     */
    public static ConfigurationResponse getConfigurationByKey(String key) throws IOException {
        HttpGet getMethod = new HttpGet(centralizeConfigurationURL + "/configuration/" + key);
        StringBuffer responseMessage = getStringBufferGegMethod(getMethod);
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
        StringBuffer responseMessage = getStringBufferGegMethod(getMethod);
        ObjectMapper mapper = new ObjectMapper();
        List<ConfigurationResponse> list = mapper.readValue(responseMessage.toString(), TypeFactory.collectionType(List.class, ConfigurationResponse.class));
        return list;
    }

    /**
     * check if token is valid or not
     * @param token
     * @return
     * @throws IOException
     */
    public static TokenObject isValidToken(String token) throws IOException {
        HttpGet getMethod = new HttpGet(userManagementURL + "/isvalidtoken/" + token);
        StringBuffer responseMessage = getStringBufferGegMethod(getMethod);
        ObjectMapper mapper = new ObjectMapper();
        TokenObject tokenObject = mapper.readValue(responseMessage.toString(), TokenObject.class);
        return tokenObject;
    }

    /**
     * sign on then get token
     * @param userSignOnObject
     * @return
     * @throws IOException
     */
    public static TokenObject signOn(UserSignOnObject userSignOnObject) throws IOException {
        HttpPost method = new HttpPost(userManagementURL + "/signon/");
        ObjectMapper mapper = new ObjectMapper();
        String payLoadLogin = mapper.writeValueAsString(userSignOnObject);
        method.setHeader("Content-Type", "application/json; charset=UTF-8");
        StringEntity stringEntity = new StringEntity(payLoadLogin );
        method.setEntity(stringEntity);
        HttpResponse response = client.execute(method);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String line = "";
        StringBuffer responseMessage = new StringBuffer();
        while ((line = rd.readLine()) != null) {
            responseMessage.append(line);
        }
        mapper = new ObjectMapper();
        TokenObject tokenObject = mapper.readValue(responseMessage.toString(), TokenObject.class);
        return tokenObject;
    }

    private static StringBuffer getStringBufferGegMethod(HttpGet getMethod) throws IOException {
        HttpResponse response = client.execute(getMethod);
        // Get the response
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String line = "";
        StringBuffer responseMessage = new StringBuffer();
        while ((line = rd.readLine()) != null) {
            responseMessage.append(line);
        }
        return responseMessage;
    }

    public static void main(String[] args) throws IOException {
      // System.out.println("getAllConfigiruations" + getAllConfigiruations());
      // System.out.println("getConfigurationByKey : " + getConfigurationByKey("name"));

       System.out.println("isValidToken : " + isValidToken("6d9951c5-e0c6-458a-84cd-6ab0e505f6e6"));
       UserSignOnObject userSignOnObject = new UserSignOnObject();
       userSignOnObject.setUser("chiwa");
       userSignOnObject.setPassword("password");
       System.out.println("signOn : " + signOn(userSignOnObject));
        userSignOnObject.setUser("chiwaInvalid");
        userSignOnObject.setPassword("password");
        System.out.println("signOn : " + signOn(userSignOnObject));
    }
}
