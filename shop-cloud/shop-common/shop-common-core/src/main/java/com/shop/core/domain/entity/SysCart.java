package com.shop.core.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName sys_cart
 */
@TableName(value ="sys_cart")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysCart implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 商品id
     */
    private Integer pid;

    /**
     * 名称
     */
    private String name;

    /**
     * 单价
     */
    private String price;

    /**
     * 总价格
     */
    private String sum;

    /**
     * 封面
     */
    private String img;

    /**
     * 轮播图
     */
    private String imgs;

    /**
     * 封面
     */
    private String details;

    /**
     * 购买数量
     */
    private Integer num;

    /**
     * 属性
     */
    private String attr;

    /**
     * 商品类型
     */
    private String type;

    /**
     * 环保环保程度
     */
    private String environment;

    /**
     * 创建人的用户id
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;


    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remarks;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}