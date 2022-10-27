package com.spade.jdoc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "com_page")
public class Page implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, name = "sort_by")
    private Integer sortBy;
    @Column(nullable = false, name = "user_id")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer userId;
    @Column(nullable = false, name = "type_id")
    private Integer typeId;
    @Column(nullable = false, name = "book_id")
    private Integer bookId;

    @Column(nullable = false, name = "create_at")
    @JsonIgnore
    private Long createAt;

    @Column(nullable = false, name = "update_at")
    @JsonIgnore
    private Long updateAt;
    @JsonIgnore
    private Integer deleted;


    @Transient
    private String content;
}