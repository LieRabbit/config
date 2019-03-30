package io.github.lierabbit.config.domain;

/**
 * 配置里的单个元素
 *
 * @author xyy
 * @since 2019-03-27 16:27
 */
public class ResourceElement {
    private String key;
    private String value;

    public ResourceElement(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
