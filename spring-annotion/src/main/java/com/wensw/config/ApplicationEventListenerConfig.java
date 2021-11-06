package com.wensw.config;

import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: wensw
 * @date: 2021/7/12
 * @description: 事件驱动扩展
 *
 * ApplicationListener : 监听容器中发布的事件，事件驱动模型开发
 *    public interface ApplicationListener<E extends ApplicationEvent> extends EventListener
 *     监听ApplicationEvent及下面的子事件
 *
 *     步骤：
 *      1、写一个监听器来监听某一个事件（ApplicationEvent及其子类）
 *      2、将监听器注册进容器
 *      3、只要容器中有相关的事件发布，就能够监听到这个事件
 *          ContextRefreshedEvent：容器刷新完成，所有的bean完全创建，发布这个事件
 *          ContextClosedEvent：关闭容器会发布这个事件
 *      4、发布一个事件：applicationContext.publishEvent(ApplicationEvent event)
 *
 *
 *    ContextRefreshedEvent -> finishRefresh(); -> publishEvent(new ContextRefreshedEvent(this));
 *    MyEvent  -> publishEvent(new MyEvent());
 *    ContextClosedEvent ->  publishEvent(new ContextClosedEvent(this));
 *
 *  原理：
 *     1、容器创建调用refresh()方法
 *     2、容器刷新完成时发布事件 :
 *     3、事件发布流程：publishEvent(ApplicationEvent event);
 *                -> publishEvent(ApplicationEvent event);
 * 				     -> getApplicationEventMulticaster().multicastEvent(applicationEvent, eventType);
 * 				         -> for(ApplicationListener<?> listener : getApplicationListeners(event, type));
 *           1、获取到事件的多播器（派发器）： getApplicationEventMulticaster()
 *           2、派发事件： multicastEvent(applicationEvent, eventType)
 *           3、获取所有的监听器： getApplicationListeners(event, type)
 *               1、如果当前有Executor , 可以支持异步线程发布事件
 *               2、否则同步的方式执行listener的方法：
 *                      InvokeListener(listener, event);
 *                    	    -> doInvokeListener(listener, event);
 *                    	       -> listener.onApplicationEvent(event);
 *
 *   [事件派发器（多播器）获取]:
 *     1、容器创建调用refresh()方法
 *     2、initApplicationEventMulticaster(); ->初始化事件派发器
 * 			1）、beanFactory.containsLocalBean(APPLICATION_EVENT_MULTICASTER_BEAN_NAME)
 * 		      判断容器中是否存在bean = > applicationEventMulticaster,
 * 		      （1） 如果存在，ApplicationEventMulticaster applicationEventMulticaster = applicationEventMulticaster
 * 		      （2） 不存在，则创建一个简单的单实例事件派发器：
 * 		      this.applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
 * 			  beanFactory.registerSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, this.applicationEventMulticaster);
 *
 *   [事件监听器获取]：
 *     1、容器创建调用refresh()方法
 *     2、registerListeners();  -> 注册事件监听器
 *        1） 首先获取所有已经注册的监听器，然后将其注册到事件派发器（ApplicationEventMulticaster）中：
 *             for (ApplicationListener<?> listener : getApplicationListeners()) {
 * 			        getApplicationEventMulticaster().addApplicationListener(listener);
 *                }
 *        2） 通过类型获取所有的监听器，注册到派发器中：
 *            String[] listenerBeanNames = getBeanNamesForType(ApplicationListener.class, true, false);
 * 		      for (String listenerBeanName : listenerBeanNames) {
 * 			        getApplicationEventMulticaster().addApplicationListenerBean(listenerBeanName);
 *             }
 *        3） 发布最早的事件：private Set<ApplicationEvent> earlyApplicationEvents;
 *            // Publish early application events now that we finally have a multicaster...
 * 		        Set<ApplicationEvent> earlyEventsToProcess = this.earlyApplicationEvents;
 * 		        this.earlyApplicationEvents = null;
 * 		        if (!CollectionUtils.isEmpty(earlyEventsToProcess)) {
 * 			         for (ApplicationEvent earlyEvent : earlyEventsToProcess) {
 * 				        getApplicationEventMulticaster().multicastEvent(earlyEvent);
 *                   }
 *              }
 *
 *   EventListener => [EventListenerMethodProcessor]
 *                 implements SmartInitializingSingleton, ApplicationContextAware, BeanFactoryPostProcessor
 *
 *     [SmartInitializingSingleton]: => void afterSingletonsInstantiated();
 *        1、创建IOC容器，调用refresh方法
 *        2、finishBeanFactoryInitialization(beanFactory); 初始化所有剩下的单实例bean
 *           => // Instantiate all remaining (non-lazy-init) singletons.
 * 		        beanFactory.preInstantiateSingletons();   DefaultListableBeanFactory
 * 		       => 先创建所有的单实例bean:  getBean()
 * 		       => 然后根据beanNames循环遍历所有的bean:
 * 		            循环遍历bean时，判断是否是SmartInitializingSingleton类型的bean,
 * 		              如果是，调用smartSingleton.afterSingletonsInstantiated()方法;
 *                        即调用EventListenerMethodProcessor.afterSingletonsInstantiated()方法
 *                        =>  EventListenerMethodProcessor => processBean(beanName, type);
 *                             找到有标注：EventListener注解的所有方法存入一个map集合
 *                            =>    Map<Method, EventListener> annotatedMethods = null;
 * 			                        try {
 * 				                        annotatedMethods = MethodIntrospector.selectMethods(targetType,
 * 						                (MethodIntrospector.MetadataLookup<EventListener>) method ->
 * 								        AnnotatedElementUtils.findMergedAnnotation(method, EventListener.class));
 *                                   }
 *                            => 获取到让其中所有的EventListenerFactory ，循环遍历Method,匹配对应的EventListenerFactory，
 *                               然后通过 EventListenerFactory 创建 ApplicationListener 事件监听器
 *                                factory.createApplicationListener(beanName, targetType, methodToUse);
 *                            => 最后将监听器放入容器：context.addApplicationListener(applicationListener);
 */
@Configuration
@ComponentScan("com.wensw.ext")
@Log4j2
public class ApplicationEventListenerConfig {

}
