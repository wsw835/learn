package com.wensw.condition;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class LCondition implements Condition {

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        ConfigurableListableBeanFactory beanFactory = conditionContext.getBeanFactory();

        ClassLoader classLoader = conditionContext.getClassLoader();
        Environment environment = conditionContext.getEnvironment();
        BeanDefinitionRegistry registry = conditionContext.getRegistry();
        ResourceLoader resourceLoader = conditionContext.getResourceLoader();
        MergedAnnotations annotations = annotatedTypeMetadata.getAnnotations();

        String[] activeProfiles = environment.getActiveProfiles();
        String[] defaultProfiles = environment.getDefaultProfiles();

        for (int i = 0; i < activeProfiles.length; i++) {
            System.out.println("---->activeProfiles : " + i + "  val: " + activeProfiles[i]);
        }
        for (int i = 0; i < defaultProfiles.length; i++) {
            System.out.println("---->defaultProfiles : " + i + "  val: " + defaultProfiles[i]);
        }
        System.out.println("上下文环境-->L " + conditionContext);
        System.out.println("注释元数据信息---->L " + annotatedTypeMetadata);
        System.out.println("类加载器---->L " + classLoader);
        System.out.println("运行环境--->L " + environment);
        System.out.println("组件注册工厂---->L " + registry);
        System.out.println("资源加载器--->L " + resourceLoader);
        System.out.println("类加载器---->L " + classLoader);
        System.out.println("运行环境--->L " + environment);
        String name = environment.getProperty("USERNAME");
        if(StringUtils.equals("wensw",name)){
            return true;
        }
        return false;
    }

}
