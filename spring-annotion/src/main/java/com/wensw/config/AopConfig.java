package com.wensw.config;

import com.wensw.aop.AspectBean;
import com.wensw.aop.DoWorkingBean;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * AOP：动态代理，在程序运行期间将某段代码切入到指定方法指定位置执行的编程方式
 *     1、导入aop依赖包： Spring-aspects
 *     2、定义一个业务处理类,定义相关业务处理方法
 *     3、定义切面类，切面类制定具体方法在业务处理了相关方法执行前后，运行中、以及抛出异常后等等进行通知。
 *         通知的方法：  @Pointcut 切点
 *            前置通知： @Before(value="切点表达式")
 *            后置通知： @After(value="")
 *            环绕通知： @Around(value ="")
 *            返回通知： @AfterReturning(value ="",returning = "")
 *            异常通知： @AfterThrowing(value ="" ,throwing="")
 *     4、将业务处理类及切面类注册进容器
 *     5、开启AOP自动代理：@EnableAspectJAutoProxy
 *
 *   AOP原理：（看给容器中注册了什么组件，注册的这些组件分别有什么功能）
 *
 *   @EnableAspectJAutoProxy
 *       ->  @Import(AspectJAutoProxyRegistrar.class) -》 给容器导入AspectJAutoProxyRegistrar
 *            AspectJAutoProxyRegistrar 利用AspectJAutoProxyRegistrar => 自定义给容器中注册bean =>  BeanDefinition
 *                  AspectJAutoProxyRegistrar implements ImportBeanDefinitionRegistrar
 *        ->   ImportBeanDefinitionRegistrar.registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
 *                   BeanDefinitionRegistry registry);
 *                  =>  AopConfigUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary(registry);  //注册所需要的一些代理bean
 *                     => internalAutoProxyCreator == AnnotationAwareAspectJAutoProxyCreator
 *                      给容器中注册一个AnnotationAwareAspectJAutoProxyCreator
 *
 *
 *        -> AnnotationAwareAspectJAutoProxyCreator
 *
 *             (1) extends =>  AspectJAwareAdvisorAutoProxyCreator
 *
 *                  (2) extends =>   AbstractAdvisorAutoProxyCreator
 *
 *                       (3)extends =>  AbstractAutoProxyCreator
 *
 *                              (4)extends   => ProxyProcessorSupport
 *
 *                                     4.1)   extends  => ProxyConfig
 *
 *                                     4.2)   implements => Ordered ... (BeanClassLoaderAware, AopInfrastructureBean)
 *
 *                                        自动装配类加载器 =>  [BeanClassLoaderAware]
 *
 *                               (4)implements =>  SmartInstantiationAwareBeanPostProcessor ... ( BeanFactoryAware )
 *
 *                                       后置处理器（bean初始化前后工作）=> [SmartInstantiationAwareBeanPostProcessor]，
 *
 *                                       自动装配BeanFactory => [BeanFactoryAware]
 *
 *                                     4.1)    extends => InstantiationAwareBeanPostProcessor
 *
 *                                             4.1.1)   extends =>  BeanPostProcessor
 *
 *    AbstractAutoProxyCreator
 *
 *       => setBeanFactory(BeanFactory beanFactory)
 *
 *       => postProcessBeforeInstantiation(Object bean,String beanName) 在bean实例创建前的后置处理器，处理相关工作
 *
 *       => postProcessAfterInitialization(Object bean,String beanName) 在bean初始化完成的后置处理器，处理相关工作
 *
 *    AbstractAdvisorAutoProxyCreator
 *
 *       => setBeanFactory(BeanFactory beanFactory) =>  this.initBeanFactory((ConfigurableListableBeanFactory)beanFactory);
 *       => initBeanFactory(ConfigurableListableBeanFactory beanFactory)
 *
 *    AspectJAwareAdvisorAutoProxyCreator
 *
 *    AnnotationAwareAspectJAutoProxyCreator
 *
 *       => initBeanFactory(ConfigurableListableBeanFactory beanFactory)
 *
 *
 * 流程：
 *     1、加载主配置类，创建IOC容器
 *     2、注册配置类，调用refresh()刷新容器，使容器自动加载所有可扫描到的单例bean
 *     3、registerBeanPostProcessor() 注册所有的bean后置处理器,用来拦截bean的创建
 *                  => 1、先获取ioc容器中已经定义的需要创建对象的 BeanPostProcessor
 *                  => 2、给容器中加别的 BeanPostProcessor
 *                  => 3、优先注册实现了PriorityOrdered接口的 BeanPostProcessor
 *                  => 3、然后注册实现了Ordered接口的 BeanPostProcessor
 *                  => 5、最后注册未实现以上优先级（PriorityOrdered、Ordered）接口的普通的 BeanPostProcessor
 *                  => 6、注册BeanPostProcessor 实际就是创建BeanPostProcessor对象存放在容器中
 *                      => 创建internalAutoProxyCreator的 BeanPostProcessor =>  AnnotationAwareAspectJAutoProxyCreator
 *                            => 创建bean的实例
 *                            => populateBean 属性赋值
 *                            => initializeBean 初始化bean
 *                                => invokeAwareMethods(),处理Aware接口的方法回调
 *                                => applyBeanPostProcessorBeforeInitialization : 执行初始化前的处理工作
 *                                => invokeInitMethods(),执行初始化方法
 *                                => applyBeanPostProcessorAfterInitialization : 执行初始化后的处理工作
 *                            => 创建 BeanPostProcessor（AnnotationAwareAspectJAutoProxyCreator）完成，调用initBeanFactory -->包装增强器 ReflectiveAspectJAdvisorFactory
 *                 => 7、将BeanPostProcessor 注册到BeanFactory中 ， beanFactory.addBeanPostProcessor(BeanPostProcessor)
 *
 * =================以上是AnnotationAwareAspectJAutoProxyCreator的创建及注册过程===================
 *
 *           AnnotationAwareAspectJAutoProxyCreator = SmartInstantiationAwareBeanPostProcessor = InstantiationAwareBeanPostProcessor
 *               = > InstantiationAwareBeanPostProcessor
 *                     postProcessBeforeInstantiation(Class<?> beanClass, String beanName)
 *                     postProcessAfterInstantiation(Object bean, String beanName)
 *
 *    4、finishBeanFactoryInitialization(beanFactory); 完成beanFactory的初始化工作，创建剩下的的单实例bean => beanFactory.preInstantiateSingletons()
 *
 *            1、遍历获取容器中所有的bean,依次创建对象getBean(beanName)
 *                    getBean => doGetBean => getSingleton()
 *
 *             2、创建bean
 *
 *                   （1）、先从缓存中获取当前bean,如果能获取到，说明当前bean是之前已经被创建过的，直接使用，否则再创建
 *                       = > 只要创建后的bean都会被缓存起来
 *
 *                   （2）、createBean（） 创建bean  ===》 AnnotationAwareAspectJAutoProxyCreator会在所有bean创建之前尝试获取并返回bean的实例
 *
 *                          ps : BeanPostProcessor : bean创建完成初始化前后调用
 *                               InstantiationAwareBeanPostProcessor ： 创建bean实例前先尝试用后置处理器返回对象
 *
 *                        1)、 resolveBeforeInstantiation(beanName, mbdToUse); 解析 BeforeInstantiation
 *                              希望后置处理器在此能返回一个代理对象，如果能返回则直接使用，如果不能则调用 doCreateBean()
 *
 *                               1]、后置处理器先尝试返回对象
     *                           =>  bean = applyBeanPostProcessorsBeforeInstantiation(targetType, beanName);
 *                                    拿到所有的后置处理器，如果是InstantiationAwareBeanPostProcessor，
 *                                    就执行 postProcessBeforeInstantiation(beanClass, beanName);
     * 					             if (bean != null) {
     * 		                                bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
     *                                }
 *
 *                        2)、doCreateBean(beanName,mbd,args)真正的创建一个bean实例、同 3.6
 *
 *
 *  AnnotationAwareAspectJAutoProxyCreator(InstantiationAwareBeanPostProcessor)的作用：
 *
 *    1、每一个bean创建之前，先调用 postProcessBeforeInstantiation()
 *        1)、先判断该bean是否在advisedBeans中(保留所有增强后的bean)
 *        2)、判断该bean是否是基础类型的bean， Advice、Pointcut、Advisor、AopInfrastructureBean，或者是一个切面@Aspect
 *        3)、判断是否需要跳过
 *             1）、获取所有候选的增强器（切面里面的所有通知方法）【List<Advisor> candidateAdvisors】
 *                  （每一个封装的通知方法都是InstantiationModelAwarePointcutAdvisor）
 *                    -》判断每一个增强器是否是AspectjPointcutAdvisor类型，如果是，返回true
 *             2）、返回false
 *
 *    2、创建对象
 *         postProcessAfterInstantiation() :
 *             return wrapIfNecessary(bean, beanName, cacheKey); 包装如果需要的bean
 *
 *          wrapIfNecessary(bean, beanName, cacheKey);
 *          1)、获取当前bean所有的增强器（通知方法）
 *              1）、找到候选的增强器（找哪些通知方法是需要切入到当前方法上的）
 *              2）、获取到在当前bean使用的增强器
 *              3）、给增强器排序
 *          2）、保存当前bean在advisorBeans中
 *          3）、如果当前bean需要增强，创建当前bean的代理对象（增强后的代理对象）
 *              1）、获取所有的增强器（通知方法）
 *              2）、保存到ProxyFactory
 *              3）、创建代理对象：
 *                        public AopProxy createAopProxy(AdvisedSupport config) throws AopConfigException {
 * 		                        if (!IN_NATIVE_IMAGE &&
 * 				                    (config.isOptimize() || config.isProxyTargetClass() || hasNoUserSuppliedProxyInterfaces(config))) {
 * 			                               Class<?> targetClass = config.getTargetClass();
 * 			                    if (targetClass == null) {
 * 				                    throw new AopConfigException("TargetSource cannot determine target class: " +
 * 						                        "Either an interface or a target is required for proxy creation.");
 *                                  }
 *                                  <!--接口或者代理类，返回jdk动态代理
 * 			                    if (targetClass.isInterface() || Proxy.isProxyClass(targetClass)) {
 * 				                        return new JdkDynamicAopProxy(config);
 *                              }
 * 			                    return new ObjenesisCglibAopProxy(config);        * 		}
 * 		                    else {
 * 			                    return new JdkDynamicAopProxy(config);
 * 		                      }
 * 	                    }
 *
 * 	                  1）、JdkDynamicAopProxy
 *
 * 	                  2）、ObjenesisCglibAopProxy
 *
 *          4）、给容器返回当前组件经过代理工厂（JdkDynamicAopProxy、ObjenesisCglibAopProxy）返回的代理对象
 *          5）、以后容器中获取到的就是当前bean的代理对象，执行目标方法时，代理对象会对应执行相应的通知方法流程
 *
 *    3、目标方法执行 （ObjenesisCglibAopProxy 返回的代理对象对应目标方法执行）：
 *           容器中保存了组件bean的代理对象，（代理工厂增强后的代理对象）、对象里面包含了（增强器（素有通知方法）、目标对象）
 *         1）、拦截目标方法执行 ： CglibAopProxy.intercept()
 *         Object CglibAopProxy.DynamicAdvisedInterceptor.intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy)
 *
 *         2）、根据ProxyFactory对象获取到将要执行的目标方法的拦截器链
 *                1)、 List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);
 *
 *                2）、获取到目标方法的拦截器集合
 *
 *                    1）先尝试从缓存中获取，获取到直接返回
 *                     <!--return a List of MethodInterceptors (may also include InterceptorAndDynamicMethodMatchers)
 *                      public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method, @Nullable Class<?> targetClass) {
 * 		                    MethodCacheKey cacheKey = new MethodCacheKey(method);
 * 		                    List<Object> cached = this.methodCache.get(cacheKey);
 * 		                    if (cached == null) {
 * 			                        cached = this.advisorChainFactory.getInterceptorsAndDynamicInterceptionAdvice(
 * 					                    this, method, targetClass);
 * 			                        this.methodCache.put(cacheKey, cached);
 *                           }
 * 		                    return cached;
 * 		                }
 *
 * 		              2）、通过AdvisorChainFactory advisorChainFactory = new DefaultAdvisorChainFactory();这个增强器链工厂获取所有拦截器和动态拦截通知方法
 *                        1）、遍历所有的增强器（Advisor）将其转换为 Interceptor
 *                             MethodInterceptor[] getInterceptors(Advisor advisor)
 *                        3）、将所有增强器转为 List<MethodInterceptor>,如果是MethodInterceptor，直接加入集合，
 *                             如果不是使用AdvisorAdapter将Advisor转为MethodInterceptor。
 *                             转化完成返回 MethodInterceptor 数组。
 *
 *         3）、如果没有拦截器链，直接invoke当前method(执行当前方法)
 *                 	if (chain.isEmpty() && Modifier.isPublic(method.getModifiers())) {
 * 					        Object[] argsToUse = AopProxyUtils.adaptArgumentsIfNecessary(method, args);
 * 					            retVal = methodProxy.invoke(target, argsToUse);
 *                            }
 *         4）、如果有拦截器链，将当前获取的拦截器链、代理对象、代理方法、目标对象、目标方法等信息传入构造一个CglibMethodInvocation对象并调用proceed()方法。
 *                	retVal = new CglibMethodInvocation(proxy, target, method, args, targetClass, chain, methodProxy).proceed();
 *
 *         5）、CglibMethodInvocation调用proceed的拦截器链执行流程：
 *                   super.proceed() => ReflectiveMethodInvocation.proceed()
 *                1）、如果没有拦截器直接执行目标方法、或者当前拦截器的索引与拦截器的数组大小-1一致（执行到了最后一个方法）执行目标方法
 *                2）、链式获取每一个拦截器、拦截器执行invoke(this),每一个拦截器等待上一个拦截器执行完成返回后开始执行
 *                     拦截器链的机制：保证通知方法与目标方法的执行顺序
 *
 *                  <!--proceed() =>  currentInterceptorIndex =》初始值默认为-1
 *                   public Object proceed() throws Throwable {
 * 		                     // We start with an index of -1 and increment early.
 * 		                <!--判断当前下标 与 增强器包装后的拦截器数组大小-1 是否相同
 * 		                 <!-- this.interceptorsAndDynamicMethodMatchers.size() - 1 => 相当于当前拦截器数组的索引
 * 		                if (this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size() - 1) {
 * 		                    <!--如果相同，直接执行当前目标方法
 * 			                return invokeJoinpoint();
 *                      }
 *                      <!--获取当前对应下标为  currentInterceptorIndex+1 的拦截器
 * 		                Object interceptorOrInterceptionAdvice =
 * 				                this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);
 *
 * 				        <!--判断该拦截器是否是 InterceptorAndDynamicMethodMatcher的类型
 * 		                if (interceptorOrInterceptionAdvice instanceof InterceptorAndDynamicMethodMatcher) {
 * 			                    // Evaluate dynamic method matcher here: static part will already have
 * 			                     // been evaluated and found to match.
 *
 * 			                    <!--将当前的拦截器或拦截增强器转换为 InterceptorAndDynamicMethodMatcher
 * 			                    InterceptorAndDynamicMethodMatcher dm =
 * 					                (InterceptorAndDynamicMethodMatcher) interceptorOrInterceptionAdvice;
 *
 * 			                    Class<?> targetClass = (this.targetClass != null ? this.targetClass : this.method.getDeclaringClass());
 *
 * 			                     if (dm.methodMatcher.matches(this.method, targetClass, this.arguments)) {
 * 				                       return dm.interceptor.invoke(this);
 *                               }else {
 * 				                    // Dynamic matching failed.
 * 				                    // Skip this interceptor and invoke the next in the chain.
 * 				                    return proceed();
 *                              }
 *                     }
 * 		               else {
 * 		                   <!-- 如果不是InterceptorAndDynamicMethodMatcher的类型，则转换为MethodInterceptor并调用MethodInterceptor.invoke(this)
 *
 * 		                   <!-- MethodBeforeAdviceInterceptor 前置通知
 *
 * 		                   <!-- 执行目标方法
 *
 * 		                   <!-- AspectJAfterAdvice  后置通知
 *
 * 		                   <!-- AfterReturningAdviceInterceptor 目标方法正常执行=》返回通知
 *
 * 		                   <!-- AspectJAfterThrowingAdvice 目标方法异常执行=》异常通知
 *
 * 		                   <!-- AspectJAroundAdvice
 * 			                // It's an interceptor, so we just invoke it: The pointcut will have
 * 			                // been evaluated statically before this object was constructed.
 * 			                return ((MethodInterceptor) interceptorOrInterceptionAdvice).invoke(this);
 *                      }
 *               }
 *
 *
 *   总结：
 *       1、@EnableAspectJAutoProxy 开启AOP功能
 *       2、@EnableAspectJAutoProxy 会给容器中注册组件 AnnotationAwareAspectJAutoProxyCreator
 *       3、AnnotationAwareAspectJAutoProxyCreator是一个后置处理器，注册进容器后会拦截之后每一个bean的创建
 *       4、容器的创建流程：
 *            1）、registerBeanPostProcessor() 注册所有的bean后置处理器,用来拦截bean的创建 = > 创建 AnnotationAwareAspectJAutoProxyCreator
 *            2）、finishBeanFactoryInitialization(beanFactory);初始化剩下的单实例bean
 *                1）、创建业务逻辑bean及切面组件bean
 *                2）、AnnotationAwareAspectJAutoProxyCreator 拦截组建的创建过程
 *                3）、组件创建完之后、判断组件是否需要增强：
 *                      是：切面的通知方法、包装成增强器（Advisor）、给业务逻辑组件创建一个代理对象
 *       5、执行目标方法 -》 代理对象执行目标方法
 *           1）、代理对象执行目标方法
 *           2）、CglibAopProxy.intercept() 调用拦截方法 拦截目标方法执行
 *               1）、得到目标方法的拦截器链、增强器 Advisor 包装成 MethodInterceptor
 *               2）、利用拦截器的链式机制、依次进入每一个拦截方法执行
 *               3）、效果：
 *
 *                      正常执行： 前置通知=》执行目标方法=》后置通知=》返回通知
 *                      异常执行： 前置通知=》执行目标方法=》后置通知=》异常返回通知
 */
@Configuration
@EnableAspectJAutoProxy
public class AopConfig {

    @Bean
    public DoWorkingBean doWorkingBean() {
        return new DoWorkingBean();
    }

    @Bean
    public AspectBean aspectBean() {
        return new AspectBean();
    }

}
