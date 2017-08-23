package com.servyou.zs.dto;

import lombok.Data;

/**
 * @author zhangshuo
 * @since 1.0.0, 2017/08/22
 */
@Data
public class TotalCount {

    private long totalCount;

    private long blankCount;

    private long noteCount;

    private int fileNumber;

    private String author;

    @Override
    public String toString() {
        return "作者：" + author +
                "，代码总行数：" + totalCount +
                "行,其中：空白行：" + blankCount +
                "行,注释行" + noteCount +
                "行,总文件数：" + fileNumber;
    }
}
