package com.wensw.bean.autowired;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.Aware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.util.StringValueResolver;

/**
 * Aware - > callback-style method.
 * <p>
 * 一个标记超级接口，指示 bean 有资格通过回调样式的方法被特定框架对象的 Spring 容器通知。实际的方法签名由各个子接口确定，
 * 但通常应仅由一个接受单个参数的返回空值的方法组成。
 * <p>请注意，仅实现 {@link Aware} 不提供默认功能。相反，处理必须明确完成，
 * 例如在 {@link org.springframework.beans.factory.config.BeanPostProcessor} 中。
 * <p>
 * 即：实现Aware接口的bean,能够通过回调方法的方式获取到对应的初始化前后相关属性
 */
public class AwaredBean implements BeanNameAware, ApplicationContextAware, EmbeddedValueResolverAware {

    private ApplicationContext applicationContext;

    @Override
    public void setBeanName(String name) {
        System.out.println("当前获取到的bean的名称--->" + name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("通过回调的方式获取到容器 --->" + applicationContext);
        this.applicationContext = applicationContext;
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver stringValueResolver) {
        String springEtl = stringValueResolver.resolveStringValue("#{80-20}");
        String properties = stringValueResolver.resolveStringValue("${beanProperties.name}");
        System.out.println("springEtl---->" + springEtl);
        System.out.println("properties--->" + properties);
        System.out.println("通过回调方式拿到stringValueResolver---->" + stringValueResolver);
    }
}
