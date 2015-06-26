package com.betext.usermanagement.services.authentication;

import com.betext.transportation.Exception.TokenAuthenticationException;
import com.betext.transportation.constant.ResponseCode;
import com.betext.transportation.object.TokenObject;
import com.betext.transportation.service.MemcachedClientService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public abstract class AbstractTokenAuthenticationService {

    protected static final String SUCCESS = "SUCCESS";
    protected static final String TOKEN_WAS_EXPIRED = "Token was expired.";
    protected static final String INVALID_TOKEN = "Invalid Token.";
    protected static final int ONE_MINUTE_IN_MILISEC = 60000;
    public static final String SIGN_ON_FAILED = "Sign on failed.";
    //protected Map<String, TokenObject> tokenMap = new HashMap<String, TokenObject>();
    private String errorMessage = null;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Autowired
    private MemcachedClientService memcachedClientService;


    /**
     * retrieve token if not existed or expired then CustomTokenObject should be null
     * and Description is not SUCCESS
     * @param token
     * @return
     */
    public TokenObject isValidToken(String token) {
        TokenObject result = new TokenObject();
        if (memcachedClientService.get(token) != null) {
            result = (TokenObject) memcachedClientService.get(token);
            long millisNow = System.currentTimeMillis();
            if (millisNow > result.getExpireTime()) {
                //Token Expired
                //tokenMap.remove(token);
                result.setCustomTokenObject(null);
                result.setDescription(TOKEN_WAS_EXPIRED);
                result.setToken(String.valueOf(ResponseCode.ERROR_EXPIRED_TOKEN));
            } else {
                result.setDescription(SUCCESS);
                result.setExpireTime(millisNow + (getExpireInMinute() * ONE_MINUTE_IN_MILISEC));
            }

        } else {
            //
            result.setCustomTokenObject(null);
            result.setDescription(INVALID_TOKEN);
            result.setToken(String.valueOf(ResponseCode.ERROR_INVALID_TOKEN));
        }
        return result;
    }


    private String generateToken() {
       return UUID.randomUUID().toString();
    }

    /**
     * This method should be use by scheduler or timer to clear all expired tokens.
     */
    public synchronized void clearAllTokenOnceADay() {
       memcachedClientService.clearAll();
    }


    private TokenObject setCustomToken(Object customToken) {
        TokenObject tokenObject = new TokenObject();
        tokenObject.setDescription(SUCCESS);
        long millisNow = System.currentTimeMillis();
        tokenObject.setExpireTime(millisNow + (getExpireInMinute() * ONE_MINUTE_IN_MILISEC));
        tokenObject.setCustomTokenObject(customToken);
        String token = generateToken();
        tokenObject.setToken(token);
        memcachedClientService.set(token, tokenObject);
        return tokenObject;
    }

    public synchronized TokenObject signOn(String userName, String password) {
        Object object = doSignOn(userName, password);
        if (object == null) {
            String errorMessage = getErrorMessage();
            if (errorMessage == null) {
                errorMessage = SIGN_ON_FAILED;
            }
            setErrorMessage(null);
            throw new TokenAuthenticationException(errorMessage);
        }
        TokenObject tokenObject = setCustomToken(object);
        return tokenObject;
    }

    /**
     * Implement to return customer object return null if sing on fail
     * @param userName
     * @param password
     * @return
     */
    protected abstract Object doSignOn(String userName, String password);

    protected abstract int getExpireInMinute();

}
