package com.spade.jdoc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "com_user")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String username;
    @Column(nullable = false, length = 100, name = "password_hash")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String passwordHash;
    @Column(nullable = false, length = 20)
    private String phone;
    @Column(nullable = false, length = 100)
    private String email;
    @Column(nullable = false, length = 20, name = "nick_name")
    private String nickName;
    @Column(nullable = false, name = "role_id")
    private Integer roleId;// 1 normal ,2 super
    @Column(nullable = false)
    private Integer status;//0正常1禁用

    @Column(nullable = false, name = "create_at")
    @JsonIgnore
    private Long createAt;
    @Column(nullable = false, name = "update_at")
    @JsonIgnore
    private Long updateAt;

    @JsonIgnore
    @Column(nullable = false)
    private Integer deleted = 0;

    public boolean checkSuper() {
        if (getRoleId().equals(2)) {
            return true;
        }
        return false;
    }

    public User() {

    }

    public User(Integer id, String username, String nickName, String phone) {
        this.id = id;
        this.username = username;
        this.nickName = nickName;
        this.phone = phone;
    }
}
