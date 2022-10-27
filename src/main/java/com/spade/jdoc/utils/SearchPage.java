package com.spade.jdoc.utils;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class SearchPage extends SearchMessage {

    private int bookId;
}
