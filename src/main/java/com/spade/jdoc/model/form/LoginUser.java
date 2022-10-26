package com.spade.jdoc.model.form;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginUser implements Serializable {

    private String username;
    private String password;

}
