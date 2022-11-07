package com.example.library.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 用户实体类
 *
 * @author 冯名豪
 * @date 2022-09-29
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "user")
public class User extends BaseEntity {

    /**
     *
     */
    private String openId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 性别 (0: 男  1: 女)
     */
    private Integer sex;

    /**
     * 头像
     */
    private String head_img;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 城市
     */
    private String city;

    /**
     * 角色 (0: 普通用户  1: 管理员)
     */
    private Integer role;

}
