package com.wensw;

import com.wensw.aop.DoWorkingBean;
import com.wensw.bean.User;
import com.wensw.bean.autowired.AutowiredBean;
import com.wensw.bean.autowired.AwaredBean;
import com.wensw.bean.profile.DatasourceProfile;
import com.wensw.bean.properties.BeanProperties;
import com.wensw.config.*;
import com.wensw.ext.MyEvent;
import com.wensw.service.impl.UserServiceImpl;
import com.wensw.transaction.GuestService;
import lombok.extern.log4j.Log4j;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Map;

@Log4j
public class App {

    @Test
    public void test01() {
        //加载配置类bean
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        System.out.println("容器创建完成----");
        Object init = applicationContext.getBean("init");
        Object init2 = applicationContext.getBean("init");
        System.out.println(init == init2);
    }

    @Test
    public void test02() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (int i = 0; i < beanDefinitionNames.length; i++) {
            System.out.println("bean-name--->   " + beanDefinitionNames[i]);
        }
    }

    @Test
    public void test03() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        Map<String, User> beansOfType = context.getBeansOfType(User.class);
        for (Map.Entry<String, User> map : beansOfType.entrySet()) {
            System.out.println("key----->" + map.getKey());
            System.out.println("value----->" + map.getValue());
        }

    }

    /**
     * 注册一个bean
     *
     * @throws Exception
     */
    @Test
    public void test04() throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ImportConfig.class);
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        Object bean = context.getBean("factoryBean");
        Object beanSelf = context.getBean("&factoryBean");
        System.out.println("工厂bean 创建的对象--->" + bean);
        System.out.println("工厂bean本身 -->" + beanSelf);
        for (String name : beanDefinitionNames) {
            System.out.println("当前所有注册的bean -- name =" + name);
        }
    }

    /**
     * bean 的生命周期： 创建并完成赋值 - > 初始化 - > 销毁
     */
    @Test
    public void test05_bean_life() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BeanLifeConfig.class);
        System.out.println("创建ioc容器，自动注册单实例bean进容器。。。");
        System.out.println("关闭容器，自动注销所有单实例bean");
        context.close();
    }

    /**
     * bean创建并完成属性赋值后进行初始化工作前后后置处理
     * 1.populateProperties
     * 2.init -> {
     * // bean初始化前的处理
     * postProcessBeforeInitialization(Object result,String beanName);
     * // 执行bean初始化方法
     * initMethod()
     * // bean初始化后的处理
     * postProcessAfterInitialization(Object result,String beanName);
     * }
     */
    @Test
    public void test06_bean_life_postprocessor() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BeanLifeConfig.class);
        System.out.println("创建ioc容器，自动注册单实例bean进容器。。。");
        Object manageLifeBeanPrototype = context.getBean("manageLifeBeanPrototype");
        System.out.println("获取多实例bean: " + manageLifeBeanPrototype);
        context.close();
        System.out.println("关闭容器，自动注销所有单实例bean");
    }

    /**
     * bean在创建完成后的属性赋值
     */
    @Test
    public void test07_bean_properties_init() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BeanPropertiesInitConfig.class);
        ConfigurableEnvironment environment = context.getEnvironment();
        BeanProperties bean = context.getBean(BeanProperties.class);
        System.out.println("BeanProperties -> " + bean);
        System.out.println("beanProperties.name -> " + environment.getProperty("beanProperties.name"));
        System.out.println("beanProperties.init -> " + environment.getProperty("beanProperties.init"));
    }

    /**
     * 组件之间的依赖关系 - spring 依赖注入 DI
     */
    @Test
    public void test08_bean_autowired_di() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BeanAutowiredConfig.class);
        AutowiredBean bean = context.getBean(AutowiredBean.class);
        System.out.println("AutowiredBean @Autowired  userServiceImpl->" + bean);
        UserServiceImpl bean1 = context.getBean(UserServiceImpl.class);
        System.out.println("context get bean userServiceImpl-> " + bean1);
        System.out.println(bean.equals(bean1));
    }

    @Test
    public void test09_bean_aware() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AwareBeanConfig.class);
        AwaredBean bean = context.getBean(AwaredBean.class);
        System.out.println(bean);
    }

    @Test
    public void test10_bean_profile() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(ProfileConfig.class);
        context.getEnvironment().setActiveProfiles("test");
        context.refresh();
        Map<String, DatasourceProfile> bean = context.getBeansOfType(DatasourceProfile.class);
        for (Map.Entry<String, DatasourceProfile> map : bean.entrySet()) {
            System.out.println("beanName---->" + map.getKey());
            System.out.println("bean->>>>>>" + map.getValue());
        }
    }

    @Test
    public void test11_aop_proxy() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AopConfig.class);
        DoWorkingBean bean = context.getBean(DoWorkingBean.class);
        bean.doWork(15, 0);
    }

    @Test
    public void test12_transaction() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TransactionConfig.class);
        GuestService bean = context.getBean(GuestService.class);
        String[] fields = new String[]{"id", "name"};
        String[] values = new String[]{"'125'", "'john'"};
        bean.insert("trans", fields, values);
    }

    @Test
    public void testBeanFactoryPostProcessor() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ExtendConfig.class);
        context.close();
    }

    @Test
    public void testApplicationEventListenerConfig() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationEventListenerConfig.class);
        MyEvent event = new MyEvent(new String("testEventPublish"));
        event.setName("test");
        event.setConfig("publish");
        context.publishEvent(event);
        context.close();
    }

}
