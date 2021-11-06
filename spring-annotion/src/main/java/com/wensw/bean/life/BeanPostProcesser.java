package com.wensw.bean.life;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 后置处理器 ： 在bean的初始化前后调用方法进行bean的处理工作
 * BeanPostProcessor 工作原理剖析：
 *
 *   -> (1) AnnotationConfigApplicationContext
 *
 *      public AnnotationConfigApplicationContext(Class<?>... componentClasses) {
 *         this();
 *         //注册当前bean作为全局容器
 *         this.register(componentClasses);
 *         // 调用上层父类AbstractApplicationContext.refresh()方法，开始进行容器内所有单例bean生命周期的管理
 *         this.refresh();
 *     }
 *
 *
 *   -> (2) AbstractApplicationContext
 *        ---> refresh()
 *        ->   public void refresh() throws BeansException, IllegalStateException {
 *         synchronized(this.startupShutdownMonitor) {
 *             StartupStep contextRefresh = this.applicationStartup.start("spring.context.refresh");
 *             this.prepareRefresh();
 *             ConfigurableListableBeanFactory beanFactory = this.obtainFreshBeanFactory();
 *             this.prepareBeanFactory(beanFactory);
 *
 *             try {
 *                 this.postProcessBeanFactory(beanFactory);
 *                 StartupStep beanPostProcess = this.applicationStartup.start("spring.context.beans.post-process");
 *                 this.invokeBeanFactoryPostProcessors(beanFactory);
 *                 this.registerBeanPostProcessors(beanFactory);
 *                 beanPostProcess.end();
 *                 this.initMessageSource();
 *                 this.initApplicationEventMulticaster();
 *                 this.onRefresh();
 *                 this.registerListeners();
 *                 <!--初始化所有单实例对象-->
 *                 this.finishBeanFactoryInitialization(beanFactory);
 *                 this.finishRefresh();
 *             } catch (BeansException var10) {
 *                 if (this.logger.isWarnEnabled()) {
 *                     this.logger.warn("Exception encountered during context initialization - cancelling refresh attempt: " + var10);
 *                 }
 *
 *                 this.destroyBeans();
 *                 this.cancelRefresh(var10);
 *                 throw var10;
 *             } finally {
 *                 this.resetCommonCaches();
 *                 contextRefresh.end();
 *             }
 *
 *         }
 *     }
 *
 *      --->(3) AbstractApplicationContext
 *
 *        --- > finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory)
 *
 *         -> protected void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory) {
 *         if (beanFactory.containsBean("conversionService") && beanFactory.isTypeMatch("conversionService", ConversionService.class)) {
 *             beanFactory.setConversionService((ConversionService)beanFactory.getBean("conversionService", ConversionService.class));
 *         }
 *
 *         if (!beanFactory.hasEmbeddedValueResolver()) {
 *             beanFactory.addEmbeddedValueResolver((strVal) -> {
 *                 return this.getEnvironment().resolvePlaceholders(strVal);
 *             });
 *         }
 *
 *         String[] weaverAwareNames = beanFactory.getBeanNamesForType(LoadTimeWeaverAware.class, false, false);
 *         String[] var3 = weaverAwareNames;
 *         int var4 = weaverAwareNames.length;
 *
 *         for(int var5 = 0; var5 < var4; ++var5) {
 *             String weaverAwareName = var3[var5];
 *             this.getBean(weaverAwareName);
 *         }
 *
 *         beanFactory.setTempClassLoader((ClassLoader)null);
 *         beanFactory.freezeConfiguration();
 *         <!--初始化所有的单实例bean-->
 *         beanFactory.preInstantiateSingletons();
 *     }
 *
 *    -> (4) DefaultListableBeanFactory
 *
 *        --->  preInstantiateSingletons()
 *
 *         -> public void preInstantiateSingletons() throws BeansException {
 *         if (this.logger.isTraceEnabled()) {
 *             this.logger.trace("Pre-instantiating singletons in " + this);
 *         }
 *
 *         <!--获取当前持有的所有定义的bean的名称-->
 *         List<String> beanNames = new ArrayList(this.beanDefinitionNames);
 *         Iterator var2 = beanNames.iterator();
 *
 *         while(true) {
 *             String beanName;
 *             Object bean;
 *             do {
 *                 while(true) {
 *                     RootBeanDefinition bd;
 *                     do {
 *                         do {
 *                             do {
 *                                 if (!var2.hasNext()) {
 *                                     var2 = beanNames.iterator();
 *
 *                                     while(var2.hasNext()) {
 *                                         beanName = (String)var2.next();
 *                                         Object singletonInstance = this.getSingleton(beanName);
 *                                         if (singletonInstance instanceof SmartInitializingSingleton) {
 *                                             StartupStep smartInitialize = this.getApplicationStartup().start("spring.beans.smart-initialize").tag("beanName", beanName);
 *                                             SmartInitializingSingleton smartSingleton = (SmartInitializingSingleton)singletonInstance;
 *                                             if (System.getSecurityManager() != null) {
 *                                                 AccessController.doPrivileged(() -> {
 *                                                     smartSingleton.afterSingletonsInstantiated();
 *                                                     return null;
 *                                                 }, this.getAccessControlContext());
 *                                             } else {
 *                                                 smartSingleton.afterSingletonsInstantiated();
 *                                             }
 *
 *                                             smartInitialize.end();
 *                                         }
 *                                     }
 *
 *                                     return;
 *                                 }
 *
 *                                 beanName = (String)var2.next();
 *                                 bd = this.getMergedLocalBeanDefinition(beanName);
 *                             } while(bd.isAbstract());
 *                         } while(!bd.isSingleton());
 *                     } while(bd.isLazyInit());
 *
 *                     <!--如果当前bean本身是一个工厂bean，那么获取这个工厂bean本身-->
 *                     if (this.isFactoryBean(beanName)) {
 *                         bean = this.getBean("&" + beanName);
 *                         break;
 *                     }
 *
 *                     <!--否则根据这个bean的名称去获取当前这个单例bean-->
 *                     this.getBean(beanName);
 *                 }
 *             } while(!(bean instanceof FactoryBean));
 *
 *             FactoryBean<?> factory = (FactoryBean)bean;
 *             boolean isEagerInit;
 *             if (System.getSecurityManager() != null && factory instanceof SmartFactoryBean) {
 *                 SmartFactoryBean var10000 = (SmartFactoryBean)factory;
 *                 ((SmartFactoryBean)factory).getClass();
 *                 isEagerInit = (Boolean)AccessController.doPrivileged(var10000::isEagerInit, this.getAccessControlContext());
 *             } else {
 *                 isEagerInit = factory instanceof SmartFactoryBean && ((SmartFactoryBean)factory).isEagerInit();
 *             }
 *
 *             if (isEagerInit) {
 *                 this.getBean(beanName);
 *             }
 *         }
 *     }
 *
 *    (5)  AbstractBeanFactory
 *
 *       ---> getBean(String beanName)
 *
 *        -->   public Object getBean(String name) throws BeansException {
 *                      return this.doGetBean(name, (Class)null, (Object[])null, false);
 *              }
 *
 *    (6)  AbstractBeanFactory
 *
 *       ---> doGetBean(String beanName)
 *
 *        -->     protected <T> T doGetBean(String name, @Nullable Class<T> requiredType,
 *                     @Nullable Object[] args, boolean typeCheckOnly) throws BeansException {
 *         String beanName = this.transformedBeanName(name);
 *         Object sharedInstance = this.getSingleton(beanName);
 *         Object bean;
 *         if (sharedInstance != null && args == null) {
 *             if (this.logger.isTraceEnabled()) {
 *                 if (this.isSingletonCurrentlyInCreation(beanName)) {
 *                     this.logger.trace("Returning eagerly cached instance of singleton bean '" + beanName + "' that is not fully initialized yet - a consequence of a circular reference");
 *                 } else {
 *                     this.logger.trace("Returning cached instance of singleton bean '" + beanName + "'");
 *                 }
 *             }
 *
 *             bean = this.getObjectForBeanInstance(sharedInstance, name, beanName, (RootBeanDefinition)null);
 *         } else {
 *             if (this.isPrototypeCurrentlyInCreation(beanName)) {
 *                 throw new BeanCurrentlyInCreationException(beanName);
 *             }
 *
 *             BeanFactory parentBeanFactory = this.getParentBeanFactory();
 *             if (parentBeanFactory != null && !this.containsBeanDefinition(beanName)) {
 *                 String nameToLookup = this.originalBeanName(name);
 *                 if (parentBeanFactory instanceof AbstractBeanFactory) {
 *                     return ((AbstractBeanFactory)parentBeanFactory).doGetBean(nameToLookup, requiredType, args, typeCheckOnly);
 *                 }
 *
 *                 if (args != null) {
 *                     return parentBeanFactory.getBean(nameToLookup, args);
 *                 }
 *
 *                 if (requiredType != null) {
 *                     return parentBeanFactory.getBean(nameToLookup, requiredType);
 *                 }
 *
 *                 return parentBeanFactory.getBean(nameToLookup);
 *             }
 *
 *             if (!typeCheckOnly) {
 *                 this.markBeanAsCreated(beanName);
 *             }
 *
 *             StartupStep beanCreation = this.applicationStartup.start("spring.beans.instantiate").tag("beanName", name);
 *
 *             try {
 *                 if (requiredType != null) {
 *                     beanCreation.tag("beanType", requiredType::toString);
 *                 }
 *
 *                 RootBeanDefinition mbd = this.getMergedLocalBeanDefinition(beanName);
 *                 this.checkMergedBeanDefinition(mbd, beanName, args);
 *                 String[] dependsOn = mbd.getDependsOn();
 *                 String[] var12;
 *                 if (dependsOn != null) {
 *                     var12 = dependsOn;
 *                     int var13 = dependsOn.length;
 *
 *                     for(int var14 = 0; var14 < var13; ++var14) {
 *                         String dep = var12[var14];
 *                         if (this.isDependent(beanName, dep)) {
 *                             throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Circular depends-on relationship between '" + beanName + "' and '" + dep + "'");
 *                         }
 *
 *                         this.registerDependentBean(dep, beanName);
 *
 *                         try {
 *                             this.getBean(dep);
 *                         } catch (NoSuchBeanDefinitionException var33) {
 *                             throw new BeanCreationException(mbd.getResourceDescription(), beanName, "'" + beanName + "' depends on missing bean '" + dep + "'", var33);
 *                         }
 *                     }
 *                 }
 *
 *                 <!--如果当前获取的根节点对应bean是一个单例类型的bean-->
 *                 if (mbd.isSingleton()) {
 *                     sharedInstance = this.getSingleton(beanName, () -> {
 *                         try {
 *                             <!--创建这个bean-->
 *                             return this.createBean(beanName, mbd, args);
 *                         } catch (BeansException var5) {
 *                             this.destroySingleton(beanName);
 *                             throw var5;
 *                         }
 *                     });
 *                     bean = this.getObjectForBeanInstance(sharedInstance, name, beanName, mbd);
 *                 } else if (mbd.isPrototype()) {
 *                     var12 = null;
 *
 *                     Object prototypeInstance;
 *                     try {
 *                         this.beforePrototypeCreation(beanName);
 *                         prototypeInstance = this.createBean(beanName, mbd, args);
 *                     } finally {
 *                         this.afterPrototypeCreation(beanName);
 *                     }
 *
 *                     bean = this.getObjectForBeanInstance(prototypeInstance, name, beanName, mbd);
 *                 } else {
 *                     String scopeName = mbd.getScope();
 *                     if (!StringUtils.hasLength(scopeName)) {
 *                         throw new IllegalStateException("No scope name defined for bean ��" + beanName + "'");
 *                     }
 *
 *                     Scope scope = (Scope)this.scopes.get(scopeName);
 *                     if (scope == null) {
 *                         throw new IllegalStateException("No Scope registered for scope name '" + scopeName + "'");
 *                     }
 *
 *                     try {
 *                         Object scopedInstance = scope.get(beanName, () -> {
 *                             this.beforePrototypeCreation(beanName);
 *
 *                             Object var4;
 *                             try {
 *                                 <!--创建bean-->
 *                                 var4 = this.createBean(beanName, mbd, args);
 *                             } finally {
 *                                 this.afterPrototypeCreation(beanName);
 *                             }
 *
 *                             return var4;
 *                         });
 *                         bean = this.getObjectForBeanInstance(scopedInstance, name, beanName, mbd);
 *                     } catch (IllegalStateException var32) {
 *                         throw new ScopeNotActiveException(beanName, scopeName, var32);
 *                     }
 *                 }
 *             } catch (BeansException var35) {
 *                 beanCreation.tag("exception", var35.getClass().toString());
 *                 beanCreation.tag("message", String.valueOf(var35.getMessage()));
 *                 this.cleanupAfterBeanCreationFailure(beanName);
 *                 throw var35;
 *             } finally {
 *                 beanCreation.end();
 *             }
 *         }
 *
 *         if (requiredType != null && !requiredType.isInstance(bean)) {
 *             try {
 *                 T convertedBean = this.getTypeConverter().convertIfNecessary(bean, requiredType);
 *                 if (convertedBean == null) {
 *                     throw new BeanNotOfRequiredTypeException(name, requiredType, bean.getClass());
 *                 } else {
 *                     return convertedBean;
 *                 }
 *             } catch (TypeMismatchException var34) {
 *                 if (this.logger.isTraceEnabled()) {
 *                     this.logger.trace("Failed to convert bean '" + name + "' to required type '" + ClassUtils.getQualifiedName(requiredType) + "'", var34);
 *                 }
 *
 *                 throw new BeanNotOfRequiredTypeException(name, requiredType, bean.getClass());
 *             }
 *         } else {
 *             return bean;
 *         }
 *     }
 *
 *      (7) DefaultSingletonBeanRegistry
 *
 *          ----> getSingleton(String beanName,ObjectFactory<?> singletonFactory)
 *
 *            -->   public Object getSingleton(String beanName, ObjectFactory<?> singletonFactory) {
 *         Assert.notNull(beanName, "Bean name must not be null");
 *         synchronized(this.singletonObjects) {
 *             Object singletonObject = this.singletonObjects.get(beanName);
 *             if (singletonObject == null) {
 *                 if (this.singletonsCurrentlyInDestruction) {
 *                     throw new BeanCreationNotAllowedException(beanName, "Singleton bean creation not allowed while singletons of this factory are in destruction (Do not request a bean from a BeanFactory in a destroy method implementation!)");
 *                 }
 *
 *                 if (this.logger.isDebugEnabled()) {
 *                     this.logger.debug("Creating shared instance of singleton bean '" + beanName + "'");
 *                 }
 *
 *                 this.beforeSingletonCreation(beanName);
 *                 boolean newSingleton = false;
 *                 boolean recordSuppressedExceptions = this.suppressedExceptions == null;
 *                 if (recordSuppressedExceptions) {
 *                     this.suppressedExceptions = new LinkedHashSet();
 *                 }
 *
 *                 try {
 *
 *                     <!--获取单例对象-->
 *                     singletonObject = singletonFactory.getObject();
 *
 *                     newSingleton = true;
 *                 } catch (IllegalStateException var16) {
 *                     singletonObject = this.singletonObjects.get(beanName);
 *                     if (singletonObject == null) {
 *                         throw var16;
 *                     }
 *                 } catch (BeanCreationException var17) {
 *                     BeanCreationException ex = var17;
 *                     if (recordSuppressedExceptions) {
 *                         Iterator var8 = this.suppressedExceptions.iterator();
 *
 *                         while(var8.hasNext()) {
 *                             Exception suppressedException = (Exception)var8.next();
 *                             ex.addRelatedCause(suppressedException);
 *                         }
 *                     }
 *
 *                     throw ex;
 *                 } finally {
 *                     if (recordSuppressedExceptions) {
 *                         this.suppressedExceptions = null;
 *                     }
 *
 *                     this.afterSingletonCreation(beanName);
 *                 }
 *
 *                 if (newSingleton) {
 *                     this.addSingleton(beanName, singletonObject);
 *                 }
 *             }
 *
 *             return singletonObject;
 *         }
 *     }
 *
 *     (8) AbstractBeanFactory
 *
 *        ----> getObject()
 *
 *     (9) AbstractAutowireCapableBeanFactory
 *
 *        ----> createBean(String beanName, RootBeanDefinition mbd, @Nullable Object[] args)
 *
 *          -->    protected Object createBean(String beanName, RootBeanDefinition mbd, @Nullable Object[] args) throws BeanCreationException {
 *         if (this.logger.isTraceEnabled()) {
 *             this.logger.trace("Creating instance of bean '" + beanName + "'");
 *         }
 *
 *         RootBeanDefinition mbdToUse = mbd;
 *         Class<?> resolvedClass = this.resolveBeanClass(mbd, beanName, new Class[0]);
 *         if (resolvedClass != null && !mbd.hasBeanClass() && mbd.getBeanClassName() != null) {
 *             mbdToUse = new RootBeanDefinition(mbd);
 *             mbdToUse.setBeanClass(resolvedClass);
 *         }
 *
 *         try {
 *             mbdToUse.prepareMethodOverrides();
 *         } catch (BeanDefinitionValidationException var9) {
 *             throw new BeanDefinitionStoreException(mbdToUse.getResourceDescription(), beanName, "Validation of method overrides failed", var9);
 *         }
 *
 *         Object beanInstance;
 *         try {
 *             beanInstance = this.resolveBeforeInstantiation(beanName, mbdToUse);
 *             if (beanInstance != null) {
 *                 return beanInstance;
 *             }
 *         } catch (Throwable var10) {
 *             throw new BeanCreationException(mbdToUse.getResourceDescription(), beanName, "BeanPostProcessor before instantiation of bean failed", var10);
 *         }
 *
 *         try {
 *
 *              <!--调用doCreateBean 返回bean实例-->
 *              beanInstance = this.doCreateBean(beanName, mbdToUse, args);
 *
 *             if (this.logger.isTraceEnabled()) {
 *                 this.logger.trace("Finished creating instance of bean '" + beanName + "'");
 *             }
 *
 *             return beanInstance;
 *         } catch (ImplicitlyAppearedSingletonException | BeanCreationException var7) {
 *             throw var7;
 *         } catch (Throwable var8) {
 *             throw new BeanCreationException(mbdToUse.getResourceDescription(), beanName, "Unexpected exception during bean creation", var8);
 *         }
 *     }
 *
 *
 *
 *     (10)  AbstractAutowireCapableBeanFactory
 *
 *            ----> doCreateBean(String beanName, RootBeanDefinition mbd, @Nullable Object[] args)
 *
 *              -->    protected Object doCreateBean(String beanName, RootBeanDefinition mbd, @Nullable Object[] args) throws BeanCreationException {
 *         BeanWrapper instanceWrapper = null;
 *         if (mbd.isSingleton()) {
 *             instanceWrapper = (BeanWrapper)this.factoryBeanInstanceCache.remove(beanName);
 *         }
 *
 *         if (instanceWrapper == null) {
 *             instanceWrapper = this.createBeanInstance(beanName, mbd, args);
 *         }
 *
 *         Object bean = instanceWrapper.getWrappedInstance();
 *         Class<?> beanType = instanceWrapper.getWrappedClass();
 *         if (beanType != NullBean.class) {
 *             mbd.resolvedTargetType = beanType;
 *         }
 *
 *         synchronized(mbd.postProcessingLock) {
 *             if (!mbd.postProcessed) {
 *                 try {
 *                     this.applyMergedBeanDefinitionPostProcessors(mbd, beanType, beanName);
 *                 } catch (Throwable var17) {
 *                     throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Post-processing of merged bean definition failed", var17);
 *                 }
 *
 *                 mbd.postProcessed = true;
 *             }
 *         }
 *
 *         boolean earlySingletonExposure = mbd.isSingleton() && this.allowCircularReferences && this.isSingletonCurrentlyInCreation(beanName);
 *         if (earlySingletonExposure) {
 *             if (this.logger.isTraceEnabled()) {
 *                 this.logger.trace("Eagerly caching bean '" + beanName + "' to allow for resolving potential circular references");
 *             }
 *
 *             this.addSingletonFactory(beanName, () -> {
 *                 return this.getEarlyBeanReference(beanName, mbd, bean);
 *             });
 *         }
 *
 *         Object exposedObject = bean;
 *
 *         try {
 *
 *             <!--先给bean属性设值后在执行bean初始化 -> populateBean-->
 *             this.populateBean(beanName, mbd, instanceWrapper);
 *
 *             <!--bean初始化 -> initializeBean-->
 *             exposedObject = this.initializeBean(beanName, exposedObject, mbd);
 *
 *
 *         } catch (Throwable var18) {
 *             if (var18 instanceof BeanCreationException && beanName.equals(((BeanCreationException)var18).getBeanName())) {
 *                 throw (BeanCreationException)var18;
 *             }
 *
 *             throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Initialization of bean failed", var18);
 *         }
 *
 *         if (earlySingletonExposure) {
 *             Object earlySingletonReference = this.getSingleton(beanName, false);
 *             if (earlySingletonReference != null) {
 *                 if (exposedObject == bean) {
 *                     exposedObject = earlySingletonReference;
 *                 } else if (!this.allowRawInjectionDespiteWrapping && this.hasDependentBean(beanName)) {
 *                     String[] dependentBeans = this.getDependentBeans(beanName);
 *                     Set<String> actualDependentBeans = new LinkedHashSet(dependentBeans.length);
 *                     String[] var12 = dependentBeans;
 *                     int var13 = dependentBeans.length;
 *
 *                     for(int var14 = 0; var14 < var13; ++var14) {
 *                         String dependentBean = var12[var14];
 *                         if (!this.removeSingletonIfCreatedForTypeCheckOnly(dependentBean)) {
 *                             actualDependentBeans.add(dependentBean);
 *                         }
 *                     }
 *
 *                     if (!actualDependentBeans.isEmpty()) {
 *                         throw new BeanCurrentlyInCreationException(beanName, "Bean with name '" + beanName + "' has been injected into other beans [" + StringUtils.collectionToCommaDelimitedString(actualDependentBeans) + "] in its raw version as part of a circular reference, but has eventually been wrapped. This means that said other beans do not use the final version of the bean. This is often the result of over-eager type matching - consider using 'getBeanNamesForType' with the 'allowEagerInit' flag turned off, for example.");
 *                     }
 *                 }
 *             }
 *         }
 *
 *         try {
 *             this.registerDisposableBeanIfNecessary(beanName, bean, mbd);
 *             return exposedObject;
 *         } catch (BeanDefinitionValidationException var16) {
 *             throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Invalid destruction signature", var16);
 *         }
 *     }
 *
 *
 *     (11)  AbstractAutowireCapableBeanFactory
 *
 *           ---->  initializeBean(String beanName, Object bean, @Nullable RootBeanDefinition mbd)
 *
 *             -->   protected Object initializeBean(String beanName, Object bean, @Nullable RootBeanDefinition mbd) {
 *         if (System.getSecurityManager() != null) {
 *             AccessController.doPrivileged(() -> {
 *                 this.invokeAwareMethods(beanName, bean);
 *                 return null;
 *             }, this.getAccessControlContext());
 *         } else {
 *             this.invokeAwareMethods(beanName, bean);
 *         }
 *
 *         Object wrappedBean = bean;
 *         if (mbd == null || !mbd.isSynthetic()) {
 *
 *             <!--在初始化bean之前-->
 *             wrappedBean = this.applyBeanPostProcessorsBeforeInitialization(bean, beanName);
 *         }
 *
 *         try {
 *              <!--执行初始化方法-->
 *             this.invokeInitMethods(beanName, wrappedBean, mbd);
 *
 *         } catch (Throwable var6) {
 *             throw new BeanCreationException(mbd != null ? mbd.getResourceDescription() : null, beanName, "Invocation of init method failed", var6);
 *         }
 *
 *         if (mbd == null || !mbd.isSynthetic()) {
 *
 *             <!--在初始化bean之后-->
 *             wrappedBean = this.applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
 *         }
 *
 *         return wrappedBean;
 *     }
 *
 *
 *    <!-- this.getBeanPostProcessors().iterator();-->
 *
 *        <!--获取容器中所有的BeanPostProcessors，挨个执行postProcessBeforeInitialization(Object result,String beanName)方法-->
 *        <!-- 如果拿到返回的bean对象为null,则直接跳出循环，不会执行后面的: -->
 *        <!-- BeanPostProcessors.postProcessBeforeInitialization(result, beanName)方法-->
 *
 *      ----> applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)
 *
 *      public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
 *         Object result = existingBean;
 *
 *         Object current;
 *         for(Iterator var4 = this.getBeanPostProcessors().iterator(); var4.hasNext(); result = current) {
 *             BeanPostProcessor processor = (BeanPostProcessor)var4.next();
 *             current = processor.postProcessBeforeInitialization(result, beanName);
 *             if (current == null) {
 *                 return result;
 *             }
 *         }
 *
 *         return result;
 *     }
 *
 *     ----> applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
 *
 *     public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {
 *         Object result = existingBean;
 *
 *         Object current;
 *         for(Iterator var4 = this.getBeanPostProcessors().iterator(); var4.hasNext(); result = current) {
 *             BeanPostProcessor processor = (BeanPostProcessor)var4.next();
 *             current = processor.postProcessAfterInitialization(result, beanName);
 *             if (current == null) {
 *                 return result;
 *             }
 *         }
 *
 *         return result;
 *     }
 *
 *
 */
@Component
public class BeanPostProcesser implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("对象刚创建并属性赋值完成未调用初始化方法之前");
        System.out.println("    =>当前未进行初始化的bean的信息--->" + beanName + "===>" + bean);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("对象创建并属性赋值完成调用初始化方法之后");
        System.out.println("    ====>当初始化之后的bean的信息--->" + beanName + "===>" + bean);
        return bean;
    }
}
