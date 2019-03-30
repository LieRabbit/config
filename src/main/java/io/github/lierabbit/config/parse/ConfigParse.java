package io.github.lierabbit.config.parse;



import io.github.lierabbit.config.domain.Resource;

import java.io.File;
import java.util.Collection;

/**
 * 配置解析
 *
 * @author xyy
 * @since 2019-03-27 14:31
 */
public interface ConfigParse {
    /**
     * 是否支持该文件解析
     *
     * @param file 配置文件
     * @return
     */
    boolean supports(File file);

    /**
     * 解析
     *
     * @param file 配置文件
     * @return 配置文件对应的资源
     */
    Collection<Resource> parse(File file);
}
