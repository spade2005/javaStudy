package com.spade.jdoc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "com_book")
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private String mark;

    @Column(nullable = false, name = "sort_by")
    private Integer sortBy;

    @Column(nullable = false, name = "user_id")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer userId;

    private Integer type;// 1 ,2 need pass

    @Column(nullable = false, length = 50)
    private String visitPass;

    @Column(nullable = false, name = "create_at")
    @JsonIgnore
    private Long createAt;

    @Column(nullable = false, name = "update_at")
    @JsonIgnore
    private Long updateAt;

    @JsonIgnore
    private Integer deleted;

    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<PageType> pageTypes;

    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Page> pages;
}
