package com.spade.jdoc.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spade.jdoc.model.User;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class SearchMessage {

    private int start = 0;//起始
    private int length = 10;//长度
    @JsonIgnore
    private User user;

//    private Map<String, String> data = Map.of("keyword", "");
    private Map<String, String> data = new HashMap<>();

//    private Map<String, String> sortData = Map.of("id", "DESC");
    private Map<String, String> sortData = new HashMap<>();
}
