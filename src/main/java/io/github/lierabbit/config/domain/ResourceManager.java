package io.github.lierabbit.config.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置管理器
 *
 * @author xyy
 * @since 2019-03-27 15:02
 */
public class ResourceManager {
    private Map<String, Resource> resourceMap;
    private Map<String, ResourceContext<?, ?>> resourceContextMap;

    public ResourceManager() {
        resourceContextMap = new HashMap<>();
    }

    public void setResourceMap(Map<String, Resource> resourceMap) {
        this.resourceMap = resourceMap;
    }

    public Resource getResource(String name) {
        return resourceMap.get(name);
    }

    public void cacheContext(String name, ResourceContext<?, ?> resourceContext) {
        resourceContextMap.put(name, resourceContext);
    }

    public ResourceContext<?, ?> getContext(String name) {
        return resourceContextMap.get(name);
    }

    public Map<String, Resource> getResourceMap() {
        return resourceMap;
    }
}
