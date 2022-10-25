package com.spade.jdoc.utils;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SearchList<T> {

    private List<T> list;

    private Long count;
}
