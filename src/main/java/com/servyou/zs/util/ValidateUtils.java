package com.servyou.zs.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.util.Collection;

/**
 * @author zhangshuo
 * @since 1.0.0, 2017/08/23
 */

public class ValidateUtils {

    public static void assertNotEmpty(String value,String message) throws MojoExecutionException {
        if(StringUtils.isEmpty(value)){
            throw new MojoExecutionException(message);
        }
    }

    public static void assertDictionary(File file, String message) throws MojoExecutionException {
        if((!file.exists()) || (!file.isDirectory())){
            throw new MojoExecutionException(message);
        }
    }

    public static void assertCollectionNotEmpty(Collection collection, String message) throws MojoExecutionException {
        if(CollectionUtils.isEmpty(collection)){
            throw new MojoExecutionException(message);
        }
    }
}
