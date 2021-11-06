package com.wensw.ext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.stereotype.Component;

/**
 * @author: wensw
 * @date: 2021/7/12
 * @description:
 */
@Component
public class MyBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        int beanDefinitionCount = registry.getBeanDefinitionCount();
        System.out.println("postProcessBeanDefinitionRegistry----beanDefinitionCount-----" + beanDefinitionCount);

        String[] beanDefinitionNames = registry.getBeanDefinitionNames();
        for (int i = 0; i < beanDefinitionNames.length; i++) {
            System.out.println("postProcessBeanDefinitionRegistry----beanDefinitionName======" + beanDefinitionNames[i]);
        }
        System.out.println("MyBeanDefinitionRegistryPostProcessor - postProcessBeanDefinitionRegistry" + registry);
       /* RootBeanDefinition rootBeanDefinition = new RootBeanDefinition();
        registry.registerBeanDefinition("注册bean", rootBeanDefinition);*/

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
        for (int i = 0; i < beanDefinitionNames.length; i++) {
            System.out.println("postProcessBeanFactory----beanDefinitionName======" + beanDefinitionNames[i]);
        }
    }
}
