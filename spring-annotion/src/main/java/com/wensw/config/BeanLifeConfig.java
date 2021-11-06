package com.wensw.config;

import com.wensw.bean.life.BeanLife;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * bean 的生命周期 ： 创建 - 》 初始化 -》 销毁
 * 1、由容器管理bean的生命周期
 * 2、通过自定义初始化和销毁的方法
 * -> 容器在bean进入当前生命周期时可通过调用自定义的创建和初始化方法实现bean的初始化、销毁自定义
 * (1)指定自定义的初始化、销毁方法 @Bean(initMethod ="",destroyMethod = "")
 * (2)通过实现spring 提供的 InitializingBean、DisposableBean 自定义bean的初始化及销毁方法
 * (3)通过JSR250提供的标准的注解@PostConstruct实现对象创建赋值完成后的初始化方法，@PreDestroy实现容器关闭前对象的销毁方法
 * (4)Spring 提供的 BeanPostProcessor 接口的使用原理：
 *        在组件对象创建并完成属性赋值后，
 *        通过 ：postProcessBeforeInitialization（Object bean,String beanName）方法完成对象初始化前的处理工作
 *               postProcessAfterInitialization（Object bean,String beanName）方法完成对象初始化方法执行后的处理工作
 *        底层应用（implements BeanPostProcessor ）：
 *        1、ApplicationContextAwareProcessor 容器创建前后 相关bean属性初始化前后的处理工作
 *        2、AsyncAnnotationBeanPostProcessor 异步注解@Async使用对应bean的初始化前后的处理工作
 *        3、BeanValidationPostProcessor      在bean创建完成属性赋值结束后，初始化前后的校验
 *        4、ImportAwareBeanPostProcessor     @Import注解完成bean的注册后，该bean初始化前后的处理工作
 *        5、InitDestroyAnnotationBeanPostProcessor  JSR250 提供的@PostConstruct
 *        6、MethodValidationPostProcessor
 *
 * <p>
 * 构造数据对象：
 * 单实例： 创建容器时自动调用方法注册单实例bean进容器
 * 多实例： 获取容器中bean时调用方法注册bean进容器
 */
@Configuration
@ComponentScan(basePackages = "com.wensw.bean")
public class BeanLifeConfig {

    /**
     * 声明bean初始化及销毁的方法
     * 初始化 ：
     * 对象创建并被赋值之后 ，调用初始化方法
     * 销毁 ：
     * 单实例： 容器关闭时自动销毁
     * 多实例： 容器不管理这个bean，不调用销毁方法
     *
     * @return
     */
    @Bean(initMethod = "init", destroyMethod = "destroy")
    public BeanLife manageLifeBean() {
        return new BeanLife("单实例bean", "创建");
    }


    @Bean(initMethod = "init", destroyMethod = "destroy")
    @Scope("prototype")
    public BeanLife manageLifeBeanPrototype() {
        return new BeanLife("多实例bean", "创建");
    }
}
