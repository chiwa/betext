package com.betext.usermanagement.services.authentication;

import com.betext.transportation.Exception.TokenAuthenticationException;
import com.betext.transportation.object.TokenObject;

/**
 * Created by chiwa on 23/6/2558.
 */
public class TestTokenApp {

    private static ImpTokenAuthenticationService testToken = new ImpTokenAuthenticationService();

    public static void main(String[] args) {
       try {
           TokenObject object =  testToken.signOn("chiwa", "xxx");
           System.out.println("==>>>  " + object);
           System.out.println("==>>" + testToken.isValidToken(object.getToken()));
           System.out.println("==>" + testToken.isValidToken("494e9fce-01f7-4f20-b12d-87abe491ab87"));
       } catch (TokenAuthenticationException ex) {
           System.out.println("Error : " + ex.getMessage());
       }

    }
}
