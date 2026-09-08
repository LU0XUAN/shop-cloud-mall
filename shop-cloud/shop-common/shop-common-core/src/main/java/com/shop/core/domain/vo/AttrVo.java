package com.shop.core.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author: shop-cloud
 * @Date: 2024/7/13 12:48
 * @Version: 1.0
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttrVo {
    private String name;
    private Integer num;
    private BigDecimal price;
}
