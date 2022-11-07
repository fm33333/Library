package com.example.library.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.OffsetDateTime;

import java.lang.Integer;

/**
 * 图书实体类
 *
 * @author 冯名豪
 * @date 2022-10-10
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "book")
public class Book extends BaseEntity {

    /**
     * 图书id
     */
    private Long bookId;

    /**
     * 图书名称
     */
    private String bookName;

    /**
     * 图书名称缩写
     */
    private String subName;

    /**
     * 作者
     */
    private String author;

    /**
     * 译者
     */
    private String translator;

    /**
     * 出版社
     */
    private String publishing;

    /**
     * 出版时间
     */
    private String published;

    /**
     * 图书数量
     */
    private Integer num;

    /**
     * 剩余可借阅数量
     */
    private Integer remainNum;

    /**
     * 图书版型（平装、电子书...）
     */
    private String designed;

    /**
     * 图书编码
     */
    private String code;

    /**
     * 豆瓣编号
     */
    private Long douban;

    /**
     * 豆瓣评分
     */
    private Integer doubanScore;

    /**
     * 参与豆瓣评分的人数
     */
    private Integer numScore;

    /**
     * 总页数
     */
    private Integer pages;

    /**
     * 图书封面url
     */
    private String photoUrl;

    /**
     * 价格
     */
    private Double price;

    /**
     * 来源
     */
    private String froms;

    /**
     * 作者简介
     */
    private String authorIntro;

    /**
     * 图书简介
     */
    private String description;

    /**
     *
     */
    private OffsetDateTime bookCreateTime;

    /**
     *
     */
    private OffsetDateTime bookUpTime;

}
