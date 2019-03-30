package io.github.lierabbit.config.domain;

import java.util.LinkedList;
import java.util.List;

/**
 * 单个配置
 *
 * @author xyy
 * @since 2019-03-27 16:56
 */
public class ResourceItem {
    private List<ResourceElement> resourceElementList;

    public ResourceItem() {
        resourceElementList = new LinkedList<>();
    }

    public ResourceItem(List<ResourceElement> resourceElementList) {
        this.resourceElementList = resourceElementList;
    }

    public List<ResourceElement> getResourceElementList() {
        return resourceElementList;
    }

    public void setResourceElementList(List<ResourceElement> resourceElementList) {
        this.resourceElementList = resourceElementList;
    }

    public boolean addElement(ResourceElement element) {
        return resourceElementList.add(element);
    }

    public boolean addElement(String key, String value) {
        return addElement(new ResourceElement(key, value));
    }

    public ResourceElement getElement(String key) {
        for (ResourceElement element : resourceElementList)
            if (element.getKey().equals(key))
                return element;

        return null;
    }
}
