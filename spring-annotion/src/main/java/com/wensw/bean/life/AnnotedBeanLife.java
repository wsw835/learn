package com.wensw.bean.life;

import org.springframework.aop.framework.AbstractAdvisingBeanPostProcessor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 通过jsr250 提供的注解： @PostConstruct标注对象属性设置完成后的初始化方法
 *
 * @PreDestroy 在bean对象即将销毁前发出通知
 */
@Component
public class AnnotedBeanLife implements ApplicationContextAware {

    private ApplicationContext context;

    public AnnotedBeanLife() {
        System.out.println("对象构造完成，属性设值结束 ... AnnotedBeanLife");
    }

    /**
     * 对象属性设置完成之后
     */
    @PostConstruct
    public void init() {
        System.out.println("属性设置完成后进行初始化动作.... init ");
    }

    /**
     * 容器移除对象之前
     */
    @PreDestroy
    public void beforeDestroyNotify() {
        System.out.println("单实例bean即将被容器销毁前发出通知...  beforeDestroyNotify");
    }


    /**
     *
     *  AbstractAdvisingBeanPostProcessor
     *
     *  @See
     *    public Object postProcessBeforeInitialization(Object bean, String beanName) {
     *         return bean;
     *     }
     *
     *     public Object postProcessAfterInitialization(Object bean, String beanName) {
     *         if (this.advisor != null && !(bean instanceof AopInfrastructureBean)) {
     *             if (bean instanceof Advised) {
     *                 Advised advised = (Advised)bean;
     *                 if (!advised.isFrozen() && this.isEligible(AopUtils.getTargetClass(bean))) {
     *                     if (this.beforeExistingAdvisors) {
     *                         advised.addAdvisor(0, this.advisor);
     *                     } else {
     *                         advised.addAdvisor(this.advisor);
     *                     }
     *
     *                     return bean;
     *                 }
     *             }
     *
     *             if (this.isEligible(bean, beanName)) {
     *                 ProxyFactory proxyFactory = this.prepareProxyFactory(bean, beanName);
     *                 if (!proxyFactory.isProxyTargetClass()) {
     *                     this.evaluateProxyInterfaces(bean.getClass(), proxyFactory);
     *                 }
     *
     *                 proxyFactory.addAdvisor(this.advisor);
     *                 this.customizeProxyFactory(proxyFactory);
     *                 return proxyFactory.getProxy(this.getProxyClassLoader());
     *             } else {
     *                 return bean;
     *             }
     *         } else {
     *             return bean;
     *         }
     *     }
     *
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //设置持有的容器对象， 在容器创建完成后 调用  postProcessBeforeInitialization 进行对象初始化之前的处理工作
        this.context = applicationContext;
    }
}
