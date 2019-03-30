package io.github.lierabbit.config.domain;

import java.util.LinkedList;
import java.util.List;

/**
 * 配置
 *
 * @author xyy
 * @since 2019-03-27 16:42
 */
public class Resource {
    private String name;
    private List<ResourceItem> itemList;
    public static final int SINGLE = 1;

    public Resource() {
        itemList = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ResourceItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<ResourceItem> itemList) {
        this.itemList = itemList;
    }

    public boolean addItem(ResourceItem item) {
        return itemList.add(item);
    }

    public boolean isSingleResource() {
        return itemList.size() == SINGLE;
    }

    public ResourceItem getSingleResourceItem() {
        return itemList.get(0);
    }
}
