package com.example.library.domain.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 实体基类
 *
 * @author 冯名豪
 * @date 2022-09-29
 * @since 1.0
 */
@Data
public class BaseEntity implements Serializable {
    protected static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    protected Long id;

    /**
     * 创建时间
     */
    protected Date createTime;

    /**
     * 更新时间
     */
    protected Date updateTime;

    /**
     * 是否删除(0: 未删除 1:已删除)
     */
    protected Boolean deleted;

}
