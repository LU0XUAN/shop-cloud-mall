package com.shop.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysFile {
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 系统文件名称
     */
    private String sysFileName;

    /**
     * 文件地址
     */
    private String urlIp;

    /**
     * 文件地址
     */
    private String url;
}
