package com.spade.jdoc.utils;

import lombok.Data;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Data
public class ReturnMessage implements Serializable {
    private String message = "";
    private Integer code = 0;//0 ok,
    private final Map<String, Object> data = new HashMap<>();

    public static ReturnMessage success() {
        ReturnMessage returnMessage = new ReturnMessage();
        return returnMessage;
    }


    public static ReturnMessage error(String message) {
        ReturnMessage returnMessage = new ReturnMessage();
        returnMessage.setMessage(message);
        returnMessage.setCode(1);
        return returnMessage;
    }

    public static ReturnMessage error(int code, String message) {
        ReturnMessage returnMessage = new ReturnMessage();
        returnMessage.setCode(code);
        returnMessage.setMessage(message);
        return returnMessage;
    }

    public ReturnMessage setData(final Map<String, Object> data) {
        this.data.putAll(data);
        return this;
    }

    public ReturnMessage setData(final String key, final Object object) {
        data.put(key, object);
        return this;
    }

    public ReturnMessage setData(final String key, final Collection<Object> collection) {
        data.put(key, collection);
        return this;
    }


}
