package com.servyou.zs.dto;

import lombok.Data;

/**
 * @author zhangshuo
 * @since 1.0.0, 2017/08/21
 */
@Data
public class SingleFileCount {

    private long totalCount;

    private long blankCount;

    private long noteCount;

    private String fileName;

    private String author;

    @Override
    public String toString() {
        return "文件名：" + fileName +
                "，代码总行数：" + totalCount +
                "行,其中：空白行：" + blankCount +
                "行,注释行：" + noteCount +
                "行";
    }

}
