package com.betext.usermanagement.services.authentication;

import com.betext.transportation.object.UserSignOnObject;
import org.springframework.stereotype.Service;

@Service
public class ImpTokenAuthenticationService extends AbstractTokenAuthenticationService {

    @Override
    protected Object doSignOn(String userName, String password) {
        UserSignOnObject userObject = new UserSignOnObject();
        userObject.setUser(userName);
        userObject.setPassword(password);
        if (!userName.equals("chiwa")) {
            setErrorMessage("There is no " + userName + " in this system.");
            return null;
        }
        return userObject;
    }

    @Override
    protected int getExpireInMinute() {
        return 5;
    }
}
