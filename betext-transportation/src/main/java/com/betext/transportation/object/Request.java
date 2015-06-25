package com.betext.transportation.object;

/**
 * Created by chiwa on 24/6/2558.
 */
public class Request {
    private String token;
    private String keyword;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String toString() {
        return "Request{" +
                "token='" + token + '\'' +
                ", keyword='" + keyword + '\'' +
                '}';
    }
}
