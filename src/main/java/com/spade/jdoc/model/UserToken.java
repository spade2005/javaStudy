package com.spade.jdoc.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "com_user_token")
public class UserToken implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, name = "user_id")
    private Integer userId;
    @Column(nullable = false)
    private String token;
    @Column(nullable = false, name = "expire_at")
    private Long expireAt;
    @Column(nullable = false)
    private String ip;

    @Column(nullable = false, name = "create_at")
    private Long createAt;
    @Column(nullable = false, name = "update_at")
    private Long updateAt;
    private Integer deleted;
}