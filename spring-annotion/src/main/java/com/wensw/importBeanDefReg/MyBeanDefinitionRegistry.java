package com.wensw.importBeanDefReg;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 可自行创建bean自由注册进容器中
 */
public class MyBeanDefinitionRegistry implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                                        BeanDefinitionRegistry registry,
                                        BeanNameGenerator importBeanNameGenerator) {
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition();
        //设置作用域
        rootBeanDefinition.setScope("prototype");
        //设置bean的类名
        rootBeanDefinition.setBeanClassName("com.wensw.entity.User");
        //手动注册一个bean 进容器
        registry.registerBeanDefinition("myBean", rootBeanDefinition);
    }

}
