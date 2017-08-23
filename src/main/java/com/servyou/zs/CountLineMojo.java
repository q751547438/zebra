package com.servyou.zs;

import com.servyou.zs.dispatcher.LineKindDispatcher;
import com.servyou.zs.dto.SingleFileCount;
import com.servyou.zs.dto.TotalCount;
import com.servyou.zs.util.ValidateUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhangshuo
 * @goal count
 * @phase process-sources
 * @since 1.0.0, 2017/08/21
 */
public class CountLineMojo extends AbstractMojo {

    /**
     * 统计文件路径
     *
     * @parameter
     * @readOnly
     */
    private String source;

    /**
     * 是否展示细节
     *
     * @parameter
     */
    private boolean detail;

    private Log log = getLog();

    public void execute() throws MojoExecutionException {

        log.debug("统计文件路径为：" + source);
        ValidateUtils.assertNotEmpty(source, "扫描路径为空");
        File file = new File(source);
        ValidateUtils.assertDictionary(file, "扫描路径不存在");
        Collection<File> fileCollection = FileUtils.listFiles(file, new String[]{"java"}, true);
        ValidateUtils.assertCollectionNotEmpty(fileCollection, "该路径下不存在java文件");

        Map<String, List<SingleFileCount>> everyAuthorCountMap = fileCollection.stream()
                .map(this::countFileLines)
                .filter(singleFileCount -> StringUtils.isNotEmpty(singleFileCount.getAuthor()))
                .collect(Collectors.groupingBy(SingleFileCount::getAuthor));

        log.info("================统计结果=================");
        everyAuthorCountMap.entrySet().forEach(
                entry -> {
                    List<SingleFileCount> fileCountList = entry.getValue();
                    TotalCount totalCount = new TotalCount();
                    totalCount.setAuthor(entry.getKey());
                    fileCountList.forEach(singleFileCount -> calculateTotalCount(singleFileCount, totalCount));
                    showResult(fileCountList, totalCount);
                }
        );
    }

    /**
     * 展示统计结果
     *
     * @param singleFileCountList 每个文件的详情
     * @param totalCount          总行数
     */
    private void showResult(List<SingleFileCount> singleFileCountList, TotalCount totalCount) {

        log.info("/n"+totalCount.toString());
        if (detail) {
            log.info("=====详细数据=====");
            singleFileCountList.forEach(singleFileCount -> log.info(singleFileCount.toString()));
        }
    }

    /**
     * 计算合计值
     *
     * @param singleFileCount 每一个文件的数据
     * @param totalCount      总的数据
     */
    private void calculateTotalCount(SingleFileCount singleFileCount, TotalCount totalCount) {

        long blankCount = totalCount.getBlankCount();
        long noteCount = totalCount.getNoteCount();
        long total = totalCount.getTotalCount();
        int fileNumber = totalCount.getFileNumber();
        blankCount += singleFileCount.getBlankCount();
        noteCount += singleFileCount.getNoteCount();
        total += singleFileCount.getTotalCount();
        fileNumber++;
        totalCount.setNoteCount(noteCount);
        totalCount.setTotalCount(total);
        totalCount.setBlankCount(blankCount);
        totalCount.setFileNumber(fileNumber);
    }

    /**
     * 计算单个文件的行数
     *
     * @param file 文件
     */
    private SingleFileCount countFileLines(File file) {
        if (null == file) {
            return null;
        }
        List<String> lineList;
        try {
            lineList = FileUtils.readLines(file, "utf-8");
        } catch (Exception e) {
            log.error(e);
            return null;
        }

        SingleFileCount singleFileCount = new SingleFileCount();
        LineKindDispatcher lineKindDispatcher = new LineKindDispatcher();
        lineList.forEach(line -> {
            lineKindDispatcher.setLine(line);
            long total = singleFileCount.getTotalCount();
            singleFileCount.setTotalCount(++total);

            if (lineKindDispatcher.isBlankLine()) {
                long blankCount = singleFileCount.getBlankCount();
                singleFileCount.setBlankCount(++blankCount);
            }
            if (lineKindDispatcher.isNoteLine()) {
                long noteCount = singleFileCount.getNoteCount();
                singleFileCount.setNoteCount(++noteCount);
                String author = lineKindDispatcher.getAuthor();
                if (StringUtils.isNotEmpty(author)) {
                    singleFileCount.setAuthor(author);
                }
            }
        });
        singleFileCount.setFileName(file.getName());
        lineList.forEach(this::freeMemory);
        return singleFileCount;
    }

    /**
     * 释放内存，把对象赋值成null
     *
     * @param obj 需释放的对象
     */
    private void freeMemory(Object obj) {
        obj = null;
    }
}
