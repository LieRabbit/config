package io.github.lierabbit.config.parse;


import io.github.lierabbit.config.domain.Resource;
import io.github.lierabbit.config.domain.ResourceItem;
import io.github.lierabbit.config.utils.FileUtils;

import java.beans.Introspector;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;

/**
 * properties文件解析
 *
 * @author xyy
 * @since 2019-03-27 14:48
 */
public class PropertiesConfigParse implements ConfigParse {
    private static final String PROPERTIES = "properties";
    private static final String CONFIG_NAME = "config.name";

    @Override
    public boolean supports(File file) {
        String suffix = FileUtils.getSuffix(file);
        return PROPERTIES.equals(suffix);
    }

    @Override
    public Collection<Resource> parse(File file) {
        Resource resource = new Resource();
        String name = FileUtils.getFileNameWithoutSuffix(file);
        resource.setName(Introspector.decapitalize(name));
        ResourceItem resourceItem = new ResourceItem();
        Properties properties = new Properties();
        try (InputStream inputStream = new FileInputStream(file)) {
            properties.load(inputStream);
            Enumeration<?> enumeration = properties.propertyNames();
            while (enumeration.hasMoreElements()) {
                String key = (String) enumeration.nextElement();
                String value = properties.getProperty(key);
                resourceItem.addElement(key, value);
                if (key.equals(CONFIG_NAME))
                    resource.setName(value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        resource.addItem(resourceItem);
        return Collections.singletonList(resource);
    }
}
