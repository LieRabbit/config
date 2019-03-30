package io.github.lierabbit.config.domain;

import io.github.lierabbit.config.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 配置上下文
 *
 * @author xyy
 * @since 2019-03-28 17:46
 */
public class ResourceContext<K, V> {
    // 配置实例化数据
    private Map<K, V> data;
    // 配置资源
    private Resource resource;
    // 配置实例化索引数据
    private Map<String, Map<Object, List<V>>> indexData;
    private Class<K> classK;
    private Class<V> classV;

    public ResourceContext(Map<K, V> data, Resource resource) {
        this.data = data;
        this.resource = resource;
        this.indexData = new HashMap<>();
    }

    public Map<K, V> getData() {
        return data;
    }

    public void setData(Map<K, V> data) {
        this.data = data;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public V get(K key) {
        return data.get(key);
    }

    /**
     * 通过索引获取配置资源
     *
     * @param index 索引名
     * @param value 索引值
     * @return
     */
    public List<V> getByIndex(String index, Object value) {
        Map<Object, List<V>> indexMap = indexData.get(index);

        if (indexMap == null) {
            synchronized (this) {
                indexMap = indexData.get(index);
                if (indexMap == null)
                    indexMap = initIndex(index);
            }
        }

        return indexMap.get(value);
    }

    /**
     * 初始化索引
     *
     * @param index 索引名
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<Object, List<V>> initIndex(String index) {
        Field indexField = ReflectionUtils.findField(classV, field -> field.getName().equals(index));
        if (indexField == null) {
            return Collections.EMPTY_MAP;
        }

        Map<Object, List<V>> res = new HashMap<>();

        data.forEach((k, v) -> {
            Object value = ReflectionUtils.get(indexField, v);
            List<V> list = res.computeIfAbsent(value, key -> new LinkedList<>());
            list.add(v);
        });

        indexData.put(index, res);

        return res;
    }

    /**
     * 获取K的class
     *
     * @return
     */
    public Class<K> getClassK() {
        return classK;
    }

    public void setClassK(Class<K> classK) {
        this.classK = classK;
    }

    /**
     * 获取V的类型
     *
     * @return
     */
    public Class<V> getClassV() {
        return classV;
    }

    public void setClassV(Class<V> classV) {
        this.classV = classV;
    }

    /**
     * 获取所有资源
     *
     * @return
     */
    public Collection<V> getAll() {
        return data.values();
    }
}
