package io.github.lierabbit.config.utils;

import java.lang.reflect.Field;

/**
 * 反射工具
 *
 * @author xyy
 * @since 2019-03-28 22:08
 */
public class ReflectionUtils {

    public static Field findField(Class clz, FieldMatcher matcher) {
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields)
            if (matcher.match(field))
                return field;

        return null;
    }

    public interface FieldMatcher {
        boolean match(Field field);
    }

    public static Object newInstance(Class clz) {
        Object object;
        try {
            object = clz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException("无法创建" + clz.getName() + "实体");
        }
        return object;
    }

    public static Field findField(Class clz, String name) {
        try {
            return clz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException(clz.getName() + "找不到" + name + "字段");
        }
    }

    public static void setField(Field field, Object object, Object value) {
        try {
            field.setAccessible(true);
            field.set(object, value);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(object + "设置值失败!!!原因：" + e.getMessage());
        }
    }

    public static Object get(Field field, Object object) {
        Object value = null;
        field.setAccessible(true);

        try {

            value = field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return value;
    }
}
