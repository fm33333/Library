package com.example.library.domain.bo;

import com.example.library.domain.entity.Book;
import com.example.library.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sun.jdi.event.StepEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import java.time.OffsetDateTime;

/**
 * 图书控制器
 *
 * @author 冯名豪
 * @date 2022-10-24
 * @since 1.0
 */
@Data
public class BookBO {

    private Integer ret;

    private String msg;

    private Data data;

    @lombok.Data
    public static class Data {
        /**
         * 图书id
         */
        @JsonProperty(value = "id")
        private Long bookId;

        /**
         * 图书名称
         */
        @JsonProperty(value = "name")
        private String bookName;

        /**
         * 图书名称缩写
         */
        @JsonProperty(value = "subname")
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
        @JsonProperty(value = "doubanScore")
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
        @JsonProperty(value = "photoUrl")
        private String photoUrl;

        /**
         * 价格
         */
        private String price;

        /**
         * 来源
         */
        private String froms;

        /**
         * 图书数量
         */
        private Integer num;

        /**
         * 作者简介
         */
        @JsonProperty(value = "authorIntro")
        private String authorIntro;

        /**
         * 图书简介
         */
        private String description;

        @JsonProperty(value = "createTime")
        private String bookCreateTime;

        @JsonProperty(value = "uptime")
        private String bookUpTime;
    }

    /**
     * 转换为Book实体类
     *
     * @return Book对象
     */
    public Book toBook() {
        Book book = new Book();
        BeanUtils.copyProperties(this.data, book);

        // "2021-04-23T16:37:27" String转为OffsetDateTime
        book.setBookCreateTime(DateUtils.strToDateTime(this.data.getBookCreateTime(), DateUtils.DATE_TIME_T_FORMATTER));
        book.setBookUpTime(DateUtils.strToDateTime(this.data.getBookUpTime(), DateUtils.DATE_TIME_T_FORMATTER));

        // "1993-11"
        // TODO：转换成日期类型？
        book.setPublished(this.data.getPublished());
        // 剔除价格中的“元”字
        String price = this.data.getPrice()
                .replace("元", "");
        book.setPrice(Double.valueOf(price));

        book.setRemainNum(this.data.getNum());
        book.setNumScore(this.data.getNumScore());

        return book;
    }

}
