package com.betext.transportation.object;


import java.io.Serializable;

public class UserSignOnObject  implements Serializable {
    private String user;
    private String password;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserSignOnObject{" +
                "user='" + user + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
