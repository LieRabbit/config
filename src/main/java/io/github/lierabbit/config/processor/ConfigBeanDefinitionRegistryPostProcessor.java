package io.github.lierabbit.config.processor;


import io.github.lierabbit.config.domain.Resource;
import io.github.lierabbit.config.domain.ResourceManager;
import io.github.lierabbit.config.parse.ConfigParse;
import io.github.lierabbit.config.parse.ExcelConfigParse;
import io.github.lierabbit.config.parse.PropertiesConfigParse;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * 配置读取
 *
 * @author xyy
 * @since 2019-03-27 12:09
 */
@Component
public class ConfigBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor, InitializingBean {
    private List<ConfigParse> configParseList;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        try {
            Map<String, Resource> resourceMap = new HashMap<>();
            String classPath = getClass().getClassLoader().getResource(".").getPath();
            Files.walk(Paths.get(classPath), FileVisitOption.FOLLOW_LINKS)
                    .map(Path::toFile)
                    .map(this::parse)
                    .flatMap(Collection::stream)
                    .filter(Objects::nonNull)
                    .forEach(resource -> {
                        Resource oldResource = resourceMap.put(resource.getName(), resource);
                        if (oldResource != null)
                            throw new IllegalArgumentException("存在相同name(" + oldResource.getName() + ")的配置！！！");
                    });

            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(ResourceManager.class);
            builder.addPropertyValue("resourceMap", resourceMap);
            AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
            registry.registerBeanDefinition(ResourceManager.class.getSimpleName(), beanDefinition);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void afterPropertiesSet() {
        configParseList = Arrays.asList(new ExcelConfigParse(), new PropertiesConfigParse());
    }

    @SuppressWarnings("unchecked")
    public Collection<Resource> parse(File file) {
        for (ConfigParse configParse : configParseList) {
            if (configParse.supports(file))
                return configParse.parse(file);
        }

        return Collections.EMPTY_LIST;
    }
}
