package com.wensw.ext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * @author: wensw
 * @date: 2021/7/12
 * @description:
 */
@Component
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("MyBeanFactoryPostProcessor  ====>" + beanFactory);
        int beanDefinitionCount = beanFactory.getBeanDefinitionCount();
        System.out.println("beanDefinitionCount=======>" + beanDefinitionCount);
        String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
        for (int i = 0; i < beanDefinitionNames.length; i++) {
            System.out.println(i + "---beanDefinitionNames---" + beanDefinitionNames[i]);
        }

    }

}
