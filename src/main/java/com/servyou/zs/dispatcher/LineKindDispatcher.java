package com.servyou.zs.dispatcher;

import org.apache.commons.lang3.StringUtils;

/**
 * @author zhangshuo
 * @since 1.0.0, 2017/08/22
 */

public class LineKindDispatcher {

    private static final String[] authorDictionary = new String[]{"author：","作者：","author:","作者:","author","作者"};

    private String line;

    public boolean isBlankLine(){
        return StringUtils.isEmpty(line);
    }

    public boolean isNoteLine(){
        if (StringUtils.isEmpty(line)) {
            return false;
        }
        String useLine = line.trim();
        return useLine.startsWith("/*") || useLine.startsWith("//") || useLine.startsWith("*");
    }

    public String getAuthor(){
        for(String authorDic : authorDictionary){
            if(!line.contains(authorDic)){
                continue;
            }
            String author = StringUtils.substringAfter(line, authorDic);
            return author.trim();
        }
        return StringUtils.EMPTY;
    }


    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }
}
