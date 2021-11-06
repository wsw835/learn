package com.wensw.config;

import com.wensw.bean.life.BeanLife;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author: wensw
 * @date: 2021/7/6
 * @description: 扩展原理：
 * BeanPostProcessor: bean的创建后初始化前后的后置处理器
 * BeanFactoryPostProcessor: bean工厂的后置处理器，在beanFactory标准初始化之后调用，所有的bean定义已经保存在beanFactory中，
 * 但未创建bean的实例。
 *
 * BeanFactoryPostProcessor：
 *    1、IOC容器创建
 *    2、invokeBeanFactoryPostProcessors(beanFactory);执行BeanFactoryPostProcessor
 *         找到所有的BeanFactoryPostProcessor并执行他们的方法
 *       1、直接在BeanFactory中找到所有类型是执行BeanFactoryPostProcessor的组件，并执行他们的方法postProcessBeanFactory
 *       2、在初始化创建其他组件前面执行：finishBeanFactoryInitialization(beanFactory);
 *
 *
 *      public static void invokeBeanFactoryPostProcessors(
 * 			ConfigurableListableBeanFactory beanFactory, List<BeanFactoryPostProcessor> beanFactoryPostProcessors) {
 *
 *
 * 		Set<String> processedBeans = new HashSet<>();
 *
 * 		if (beanFactory instanceof BeanDefinitionRegistry) {
 * 			BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
 * 			List<BeanFactoryPostProcessor> regularPostProcessors = new ArrayList<>();
 * 			List<BeanDefinitionRegistryPostProcessor> registryProcessors = new ArrayList<>();
 *
 * 			for (BeanFactoryPostProcessor postProcessor : beanFactoryPostProcessors) {
 * 				if (postProcessor instanceof BeanDefinitionRegistryPostProcessor) {
 * 					BeanDefinitionRegistryPostProcessor registryProcessor =
 * 							(BeanDefinitionRegistryPostProcessor) postProcessor;
 * 					registryProcessor.postProcessBeanDefinitionRegistry(registry);
 * 					registryProcessors.add(registryProcessor);
 *                                }
 * 				else {
 * 					regularPostProcessors.add(postProcessor);
 *                }            * 			}
 *
 *
 * 			List<BeanDefinitionRegistryPostProcessor> currentRegistryProcessors = new ArrayList<>();
 *
 * 			<!--First, invoke the BeanDefinitionRegistryPostProcessors that implement PriorityOrdered.
 * 			String[] postProcessorNames =
 * 					beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
 * 			for (String ppName : postProcessorNames) {
 * 				if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
 * 					currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
 * 					processedBeans.add(ppName);
 * 				}
 * 			}
 * 			sortPostProcessors(currentRegistryProcessors, beanFactory);
 * 			registryProcessors.addAll(currentRegistryProcessors);
 * 			invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry, beanFactory.getApplicationStartup());
 * 			currentRegistryProcessors.clear();
 *
 * 			<!--Next, invoke the BeanDefinitionRegistryPostProcessors that implement Ordered.
 * 			postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
 * 			for (String ppName : postProcessorNames) {
 * 				if (!processedBeans.contains(ppName) && beanFactory.isTypeMatch(ppName, Ordered.class)) {
 * 					currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
 * 					processedBeans.add(ppName);
 * 				}
 * 			}
 * 			sortPostProcessors(currentRegistryProcessors, beanFactory);
 * 			registryProcessors.addAll(currentRegistryProcessors);
 * 			invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry, beanFactory.getApplicationStartup());
 * 			currentRegistryProcessors.clear();
 *
 * 			<!-- Finally, invoke all other BeanDefinitionRegistryPostProcessors until no further ones appear.
 * 			boolean reiterate = true;
 * 			while (reiterate) {
 * 				reiterate = false;
 * 				postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
 * 				for (String ppName : postProcessorNames) {
 * 					if (!processedBeans.contains(ppName)) {
 * 						currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
 * 						processedBeans.add(ppName);
 * 						reiterate = true;                    *                }
 * 				}
 * 				sortPostProcessors(currentRegistryProcessors, beanFactory);
 * 				registryProcessors.addAll(currentRegistryProcessors);
 * 				invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry, beanFactory.getApplicationStartup());
 * 				currentRegistryProcessors.c            ();
 * 			}
 *
 * 			<!-- Now, invoke the postProcessBeanFactory callback of all processors handled so far.
 * 			invokeBeanFactoryPostProcessors(registryProcessors, beanFactory);
 * 			invokeBeanFactoryPostProcessors(regularPostProcessors, beanFactory);
 * 		}
 *
 * 		else {
 * 			<!-- Invoke factory processors registered with the context instance.
 * 			invokeBeanFactoryPostProcessors(beanFactoryPostProcessors, beanFactory);
 * 		}
 *
 * 		<!--Do not initialize FactoryBeans here: We need to leave all regular beans
 * 		<!-- uninitialized to let the bean factory post-processors apply to them!
 * 		String[] postProcessorNames =
 * 				beanFactory.getBeanNamesForType(BeanFactoryPostProcessor.class, true, false);
 *
 * 		<!-- Separate between BeanFactoryPostProcessors that implement PriorityOrdered,
 * 		<!-- Ordered, and the rest.
 * 		List<BeanFactoryPostProcessor> priorityOrderedPostProcessors = new ArrayList<>();
 * 		List<String> orderedPostProcessorNames = new ArrayList<>();
 * 		List<String> nonOrderedPostProcessorNames = new ArrayList<>();
 * 		for (String ppName : postProcessorNames) {
 * 			if (processedBeans.contains(ppName)) {
 * 				// skip - already processed in first phas            ove
 * 			}
 * 			else if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
 * 				priorityOrderedPostProcessors.add(beanFactory.getBean(ppName, BeanFactoryPostProcessor.c            ));
 * 			}
 * 			else if (beanFactory.isTypeMatch(ppName, Ordered.class)) {
 * 				orderedPostProcessorNames.add(p            e);
 * 			}
 * 			else {
 * 				nonOrderedPostProcessorNames.add(p            e);
 *        }
 * 		}
 *
 * 		// First, invoke the BeanFactoryPostProcessors that implement PriorityOrdered.
 * 		sortPostProcessors(priorityOrderedPostProcessors, beanFactory);
 * 		invokeBeanFactoryPostProcessors(priorityOrderedPostProcessors, beanFactory);
 *
 * 		// Next, invoke the BeanFactoryPostProcessors that implement Ordered.
 * 		List<BeanFactoryPostProcessor> orderedPostProcessors = new ArrayList<>(orderedPostProcessorNames.size());
 * 		for (String postProcessorName : orderedPostProcessorNames) {
 * 			orderedPostProcessors.add(beanFactory.getBean(postProcessorName, BeanFactoryPostProcessor.cl        ));
 * 		}
 * 		sortPostProcessors(orderedPostProcessors, beanFactory);
 * 		invokeBeanFactoryPostProcessors(orderedPostProcessors, beanFactory);
 *
 * 		// Finally, invoke all other BeanFactoryPostProcessors.
 * 		List<BeanFactoryPostProcessor> nonOrderedPostProcessors = new ArrayList<>(nonOrderedPostProcessorNames.size());
 * 		for (String postProcessorName : nonOrderedPostProcessorNames) {
 * 			nonOrderedPostProcessors.add(beanFactory.getBean(postProcessorName, BeanFactoryPostProcessor.cl        ));
 * 		}
 * 		invokeBeanFactoryPostProcessors(nonOrderedPostProcessors, beanFactory);
 *
 * 		// Clear cached merged bean definitions since the post-processors might have
 * 		// modified the original metadata, e.g. replacing placeholders in values...
 * 		beanFactory.clearMetadataCa    e();
 * 	}
 *
 *
 * BeanDefinitionRegistryPostProcessor extends  BeanFactoryPostProcessor:
 *    在标准初始化之后修改应用程序上下文的内部 bean 定义注册表。所有常规 bean 定义都将被加载，
 *    但尚未实例化任何 bean。这允许在下一个后处理阶段开始之前添加更多的 bean 定义。
 *    postProcessBeanDefinitionRegistry:
 *      优先于BeanFactoryPostProcessor执行，允许往容器中中注册其他组件:
 *
 * 原理：
 *   1、创建IOC容器：
 *   2、AnnotationConfigApplicationContext无参构造：refresh()=> 调用invokeBeanFactoryPostProcessors(beanFactory)
 *   3、从容器中找到所有的BeanDefinitionRegistryPostProcessor并执行他们的postProcessBeanDefinitionRegistry方法
 *      1、依次触发所有的postProcessBeanDefinitionRegistry方法
 *      2、再触发postProcessBeanFactory方法
 *   4、从容器中找到所有的BeanFactoryPostProcessor并执行他们的postProcessBeanFactory方法
 */
@Configuration
@ComponentScan("com.wensw.ext")
public class ExtendConfig {

    @Bean
    public BeanLife beanLife() {
        BeanLife beanLife = new BeanLife();
        return beanLife;
    }

}
