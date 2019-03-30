package io.github.lierabbit.config.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 类工具
 *
 * @author xyy
 * @since 2019-03-28 20:33
 */
public class ClassUtils {
    /**
     * 获取范型参数的类型
     *
     * @param clazz 范型类
     * @param index 第index个范型
     * @return
     * @throws IndexOutOfBoundsException
     */
    public static Class getSuperClassGenericType(Class clazz, int index)
            throws IndexOutOfBoundsException {
        Type type = clazz.getGenericSuperclass();
        return getSuperClassGenericType(type, index);
    }

    public static Class getSuperClassGenericType(Type type, int index) {
        if (type instanceof ParameterizedType)
            return getSuperClassGenericType((ParameterizedType) type, index);
        return null;
    }

    public static Class getSuperClassGenericType(ParameterizedType type, int index)
            throws IndexOutOfBoundsException {
        Type[] params = type.getActualTypeArguments();
        if (index >= params.length || index < 0) {
            return null;
        }
        if (!(params[index] instanceof Class)) {
            return null;
        }
        return (Class) params[index];
    }
}
