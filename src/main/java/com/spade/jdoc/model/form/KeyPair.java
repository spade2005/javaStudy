package com.spade.jdoc.model.form;

import lombok.Data;

import java.io.Serializable;

@Data
public class KeyPair implements Serializable {

    private String k;
    private String v;

}
