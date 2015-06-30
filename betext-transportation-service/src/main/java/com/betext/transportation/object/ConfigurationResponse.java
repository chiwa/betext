package com.betext.transportation.object;


import java.io.Serializable;

public class ConfigurationResponse  implements Serializable {
    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ConfigurationResponse{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
