package io.github.lierabbit.config.processor;

import io.github.lierabbit.config.annotation.Config;
import io.github.lierabbit.config.annotation.Id;
import io.github.lierabbit.config.domain.*;
import io.github.lierabbit.config.exception.NoSuchResourceException;
import io.github.lierabbit.config.utils.ClassUtils;
import io.github.lierabbit.config.utils.ReflectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import java.beans.Introspector;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置注入
 *
 * @author xyy
 * @since 2019-03-28 16:56
 */
@Component
public class ConfigBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ResourceManager resourceManager;
    private ConversionService conversionService;


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        org.springframework.util.ReflectionUtils.doWithFields(bean.getClass(), field -> {
            Config config = field.getAnnotation(Config.class);
            if (config == null)
                return;

            String name = config.name();
            if (field.getType().equals(ResourceContext.class)) {
                injectContext(bean, field, name);
            } else {
                // 单条配置需要制定在哪个配置里
                if (name.equals(Config.DEFAULT))
                    throw new IllegalArgumentException(field.getClass().getName() + "单条配置名不能为默认");
                String key = config.key();
                injectSingleConfig(bean, field, name, key);
            }
        });
        return bean;
    }

    /**
     * 注入配置上下文
     *
     * @param bean
     * @param field
     * @param configName
     */
    @SuppressWarnings("unchecked")
    public void injectContext(Object bean, Field field, String configName) {
        Class classK = ClassUtils.getSuperClassGenericType(field.getGenericType(), 0);
        if (classK == null)
            throw new IllegalArgumentException("类型声明不正确");


        Class classV = ClassUtils.getSuperClassGenericType(field.getGenericType(), 1);
        if (classV == null)
            throw new IllegalArgumentException("类型声明不正确");

        Field idField = ReflectionUtils.findField(classV, vField -> {
            Id id = vField.getAnnotation(Id.class);
            return id != null;
        });

        if (idField == null)
            throw new IllegalArgumentException(classV.getName() + "找不到id字段！！！请确认是否有@Id");

        if (configName.equals(Config.DEFAULT))
            configName = Introspector.decapitalize(classV.getSimpleName());

        ResourceContext<?, ?> resourceContext = resourceManager.getContext(configName);

        if (resourceContext == null) {
            Resource resource = resourceManager.getResource(configName);

            if (resource == null)
                throw new NoSuchResourceException("找不到" + configName + "配置");

            Map<Object, Object> data = new HashMap<>();

            for (ResourceItem item : resource.getItemList()) {
                Object vObject = ReflectionUtils.newInstance(classV);
                for (ResourceElement element : item.getResourceElementList()) {
                    // 值为null不注入
                    if (element.getValue() != null) {
                        Field vField = ReflectionUtils.findField(classV, element.getKey());
                        vField.setAccessible(true);
                        Object value = conversionService.convert(element.getValue(), vField.getType());
                        ReflectionUtils.setField(vField, vObject, value);
                        if (idField.equals(vField))
                            data.put(value, vObject);
                    }
                }
            }

            resourceContext = new ResourceContext(data, resource);
            resourceContext.setClassK(classK);
            resourceContext.setClassV(classV);
            resourceManager.cacheContext(configName, resourceContext);
        }


        ReflectionUtils.setField(field, bean, resourceContext);
    }

    /**
     * 注入单个元素
     *
     * @param bean
     * @param field
     * @param configName
     * @param key
     */
    public void injectSingleConfig(Object bean, Field field, String configName, String key) {
        Resource resource = resourceManager.getResource(configName);
        if (!resource.isSingleResource())
            throw new IllegalStateException(resource.getName() + "不是单条配置!!!");

        ResourceItem item = resource.getSingleResourceItem();

        // key默认为字段名
        if (key.equals(Config.DEFAULT))
            key = field.getName();
        ResourceElement element = item.getElement(key);
        if (element == null)
            throw new IllegalStateException(resource.getName() + "里不存在" + key);

        if (element.getValue() != null) {
            Object value = conversionService.convert(element.getValue(), field.getType());
            ReflectionUtils.setField(field, bean, value);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (applicationContext instanceof ConfigurableApplicationContext)
            conversionService = ((ConfigurableApplicationContext) applicationContext).getBeanFactory().getConversionService();
        else
            conversionService = ApplicationConversionService.getSharedInstance();
    }
}
