package com.betext.usermanagement.controller;


import com.betext.transportation.Exception.TokenAuthenticationException;
import com.betext.transportation.object.TokenObject;
import com.betext.transportation.object.UserSignOnObject;
import com.betext.transportation.service.MemcachedClientService;
import com.betext.usermanagement.services.authentication.ImpTokenAuthenticationService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
public class RestController {

    private final Logger log = LogManager.getLogger(RestController.class);

    @Autowired
    private ImpTokenAuthenticationService tokenAuthenticationService;

    @Autowired
    private MemcachedClientService memcachedClientService;

    /**
     * Do authentication is token valid or not
     * @param request
     * @return
     */
    /*public Response authentication(Request request) {
        TokenObject tokenObject = tokenAuthenticationService.isValidToken(request.getToken());
        Response response = new Response();
        response.setResponseDescription(tokenObject.getDescription());
        if (tokenObject.getCustomTokenObject() == null) {
            response.setResponseCode(Integer.parseInt(tokenObject.getToken()));
        }
        return response;
    }*/

  /*  @RequestMapping(value = "/search", method = {RequestMethod.POST}, produces = "application/json")
    public @ResponseBody Response search(Model model, @RequestBody Request request) {
        //===================Add to every method============//
        Response response = authentication(request);
        if (response.getResponseCode() != ResponseCode.SUCCESS) {
            return response;
        }

        response.setResponseCode(ResponseCode.SUCCESS);
        return response;
    }*/

    @RequestMapping(value = "/signon", method = RequestMethod.POST)
    public @ResponseBody TokenObject signOn(@RequestBody UserSignOnObject userSignOnObject) throws IOException {
        TokenObject tokenObject = new TokenObject();
        try {
            tokenObject = (TokenObject) tokenAuthenticationService.signOn(userSignOnObject.getUser(), userSignOnObject.getPassword());
        } catch (TokenAuthenticationException ex) {
            tokenObject.setDescription(ex.getMessage());
        }
        return tokenObject;
    }

    @RequestMapping(value = "/isvalidtoken/{token}", method = RequestMethod.GET)
    public @ResponseBody TokenObject isValidToken(@PathVariable String token) throws IOException {
        TokenObject tokenObject = new TokenObject();
        try {
            tokenObject = (TokenObject) tokenAuthenticationService.isValidToken(token);
        } catch (TokenAuthenticationException ex) {
            tokenObject.setDescription(ex.getMessage());
        }
        return tokenObject;
    }

    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public @ResponseBody String Ping() throws IOException {
        String str = (String) memcachedClientService.get("foo");
        System.out.println("===>>> " + str);
        return "pong";
    }
}
