package com.piesat.portal.rpc.dto;

import com.piesat.util.BaseDto;
import lombok.Data;

/**
 * 文件管理
 */
@Data
public class FileManageDto extends BaseDto {
    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 文件分类
     */
    private String fileCategory;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 文件后缀
     */
    private String fileSuffix;

    /**
     * 文件描述
     */
    private String fileDesc;

    /**
     * 下载次数
     */
    private Integer downloadtimes;

    /**
     * 序号
     */
    private String serialNumber;


    private String typeName;


    private String categoryName;
}
