package io.github.lierabbit.config.utils;

import java.io.File;

/**
 * 文件工具
 *
 * @author xyy
 * @since 2019-03-27 14:49
 */
public class FileUtils {

    /**
     * 获取文件后缀
     *
     * @param file 文件
     * @return 后缀
     */
    public static String getSuffix(File file) {
        String fileName = file.getName();
        return getSuffix(fileName);
    }

    /**
     * 获取文件后缀
     *
     * @param fileName 文件名
     * @return 后缀
     */
    public static String getSuffix(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (index == -1)
            return "";
        return fileName.substring(index + 1);
    }

    /**
     * 获取去掉后缀的文件名
     * @param file
     * @return
     */
    public static String getFileNameWithoutSuffix(File file) {
        String fileName = file.getName();
        return getFileNameWithoutSuffix(fileName);
    }


    /**
     * 获取去掉后缀的文件名
     * @param fileName
     * @return
     */
    public static String getFileNameWithoutSuffix(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (index == -1)
            return "";

        return fileName.substring(0, index);
    }
}
