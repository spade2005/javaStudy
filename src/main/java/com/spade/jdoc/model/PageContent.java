package com.spade.jdoc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "com_page_content")
public class PageContent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, name = "page_id")
    private Integer pageId;

    @Lob
    @Column(columnDefinition = "text")
    private String content;

    @Column(nullable = false, name = "hash_code")
    private String hashCode;

    private Integer type;//1正式 2草稿 3历史

    @Column(nullable = false, name = "create_at")
    @JsonIgnore
    private Long createAt;

    @JsonIgnore
    private Integer deleted;
}