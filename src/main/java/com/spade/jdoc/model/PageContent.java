package com.spade.jdoc.model;

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
    private Integer content;
    private Integer type;//1正式 2草稿 3历史

    @Column(nullable = false, name = "create_at")
    private Long createAt;
    @Column(nullable = false, name = "update_at")
    private Long updateAt;
    private Integer deleted;
}