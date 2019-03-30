package io.github.lierabbit.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 配置注解
 *
 * @author xyy
 * @since 2019-03-28 16:34
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Config {
    String DEFAULT = "default";

    // 配置名
    String name() default DEFAULT;

    // 用于获取单条配置
    String key() default DEFAULT;
}
