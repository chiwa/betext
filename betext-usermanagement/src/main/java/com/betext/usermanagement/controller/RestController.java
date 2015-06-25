package com.betext.usermanagement.controller;


import com.betext.usermanagement.services.authentication.ImpTokenAuthenticationService;
import com.betext.transportation.Exception.TokenAuthenticationException;
import com.betext.transportation.constant.ResponseCode;
import com.betext.transportation.object.Request;
import com.betext.transportation.object.Response;
import com.betext.transportation.object.TokenObject;
import com.betext.transportation.object.UserSignOnObject;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class RestController {

    private final Logger log = LogManager.getLogger(RestController.class);

    @Autowired
    private ImpTokenAuthenticationService tokenAuthenticationService;

    /**
     * Do authentication is token valid or not
     * @param request
     * @return
     */
    public Response authentication(Request request) {
        TokenObject tokenObject = tokenAuthenticationService.isValidToken(request.getToken());
        Response response = new Response();
        response.setResponseDescription(tokenObject.getDescription());
        if (tokenObject.getCustomTokenObject() == null) {
            response.setResponseCode(Integer.parseInt(tokenObject.getToken()));
        }
        return response;
    }

    @RequestMapping(value = "/search", method = {RequestMethod.POST}, produces = "application/json")
    public @ResponseBody Response search(Model model, @RequestBody Request request) {
        //===================Add to every method============//
        Response response = authentication(request);
        if (response.getResponseCode() != ResponseCode.SUCCESS) {
            return response;
        }

        response.setResponseCode(ResponseCode.SUCCESS);
        return response;
    }

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
}
