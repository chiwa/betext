package com.betext.transportation.object;


import java.io.Serializable;

public class TokenObject  implements Serializable {

    private String token = null;
    private long expireTime = 0;
    private Object customTokenObject = null;
    private String description;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public Object getCustomTokenObject() {
        return customTokenObject;
    }

    public void setCustomTokenObject(Object customTokenObject) {
        this.customTokenObject = customTokenObject;
    }

    @Override
    public String toString() {
        return "TokenObject{" +
                "token='" + token + '\'' +
                ", expireTime=" + expireTime +
                ", customTokenObject=" + customTokenObject +
                ", description='" + description + '\'' +
                '}';
    }
}
