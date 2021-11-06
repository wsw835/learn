package com.wensw.config;

import com.alibaba.druid.DbType;
import com.alibaba.druid.pool.DruidDataSource;
import com.mysql.jdbc.Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;


/**
 * 声明式事务：
 * 事务支持环境配置：
 *
 *   1、导入相关依赖 ： 数据源、数据库驱动、spring-jdbc
 *   2、配置数据源
 *   3、给方法上标注@Transaction
 *   4、注册事务管理器 - 》DataSourceTransactionManager
 *   5、开启允许事务注解 @EnableTransactionManagement ，开启基于注解的事务管理功能
 *
 *
 * 原理分析：
 *
 *    1、 利用@EnableTransactionManagement导入 TransactionManagementConfigurationSelector 组件bean
 *       1、 TransactionManagementConfigurationSelector
 *
 *           -> 给容器中导入 AutoProxyRegistrar、ProxyTransactionManagementConfiguration 两个组件
 *
 *          protected String[] selectImports(AdviceMode adviceMode) {
 *                  switch(adviceMode) {
 *                  case PROXY:
 *                         return new String[]{AutoProxyRegistrar.class.getName(), ProxyTransactionManagementConfiguration.class.getName()};
 *                  case ASPECTJ:
 *                         return new String[]{this.determineTransactionAspectClass()};
 *                  default:
 *                          return null;
 *                  }
 *              }
 *
 *   2、AutoProxyRegistrar = 后置处理器，拦截bean的创建，返回代理对象
 *         给容器中注册一个 InfrastructureAdvisorAutoProxyCreator 组件
 *           利用后置处理器机制在对象创建之后，对对象进行包装，返回一个代理对象，代理对象里面包含了所有的增强器，
 *           通多代理对象执行方式时，对应拦截器链进行链式拦截执行
 *
 *           InfrastructureAdvisorAutoProxyCreator
 *
 *             extends  AbstractAdvisorAutoProxyCreator
 *
 *             extends  AbstractAutoProxyCreator
 *
 *           【AbstractAutoProxyCreator】  ->
 *               public Object postProcessAfterInitialization(@Nullable Object bean, String beanName) {
 * 		                if (bean != null) {
 * 			                    Object cacheKey = getCacheKey(bean.getClass(), beanName);
 * 			            if (this.earlyProxyReferences.remove(cacheKey) != bean) {
 * 				                return wrapIfNecessary(bean, beanName, cacheKey);
 *                        }
 *                     }
 * 		                return bean;
 *                  }
 *
 *
 *   3、ProxyTransactionManagementConfiguration   =》 @Configuration 配置类，给容器中注入以下组件
 *
 *
 *      =》  BeanFactoryTransactionAttributeSourceAdvisor
 *
 *         给容器中注册了事务增强器：
 *
 *             事务的增强器需要使用到事务注解的信息，所以又注入了相关的事务注解解析器： AnnotationTransactionAttributeSource。
 *
 *              （1）、TransactionAttributeSource =》 return new AnnotationTransactionAttributeSource();
 *
 *               AnnotationTransactionAttributeSource : 事务相关注解信息的属性解析器
 *
 *        public AnnotationTransactionAttributeSource(boolean publicMethodsOnly) {
 * 		        this.publicMethodsOnly = publicMethodsOnly;
 * 		        if (jta12Present || ejb3Present) {
 * 			            this.annotationParsers = new LinkedHashSet<>(4);
 * 			            this.annotationParsers.add(new SpringTransactionAnnotationParser());
 * 			         if (jta12Present) {
 * 				            this.annotationParsers.add(new JtaTransactionAnnotationParser());
 *                     }
 * 			         if (ejb3Present) {
 * 				        this.annotationParsers.add(new Ejb3TransactionAnnotationParser());
 *                   }
 *              }else {
 * 			        this.annotationParsers = Collections.singleton(new SpringTransactionAnnotationParser());
 * 		        }
 * 	        }
 *
 *
 *       例如： SpringTransactionAnnotationParser： 解析propagation、isolation、timeout、timeoutString、
 *       readOnly、value、label、rollbackFor、rollbackForClassName、noRollbackFor、noRollbackForClassName
 *
 *              protected TransactionAttribute parseTransactionAnnotation(AnnotationAttributes attributes) {
 * 		                RuleBasedTransactionAttribute rbta = new RuleBasedTransactionAttribute();
 * 		                Propagation propagation = attributes.getEnum("propagation");
 * 		                rbta.setPropagationBehavior(propagation.value());
 * 		                Isolation isolation = attributes.getEnum("isolation");
 * 		                rbta.setIsolationLevel(isolation.value());
 * 		                rbta.setTimeout(attributes.getNumber("timeout").intValue());
 * 		                String timeoutString = attributes.getString("timeoutString");
 * 		                Assert.isTrue(!StringUtils.hasText(timeoutString) || rbta.getTimeout() < 0,
 * 				            "Specify 'timeout' or 'timeoutString', not both");
 * 		                rbta.setTimeoutString(timeoutString);
 *
 * 		                rbta.setReadOnly(attributes.getBoolean("readOnly"));
 * 		                rbta.setQualifier(attributes.getString("value"));
 * 		                rbta.setLabels(Arrays.asList(attributes.getStringArray("label")));
 *
 *                		List<RollbackRuleAttribute> rollbackRules = new ArrayList<>();
 *              		for (Class<?> rbRule : attributes.getClassArray("rollbackFor")) {
 *              			rollbackRules.add(new RollbackRuleAttribute(rbRule));
 *                              }
 *               		for (String rbRule : attributes.getStringArray("rollbackForClassName")) {
 *                			rollbackRules.add(new RollbackRuleAttribute(rbRule));
 *                      }
 *                		for (Class<?> rbRule : attributes.getClassArray("noRollbackFor")) {
 *                			rollbackRules.add(new NoRollbackRuleAttribute(rbRule));
 *                      }
 *                		for (String rbRule : attributes.getStringArray("noRollbackForClassName")) {
 *                			rollbackRules.add(new NoRollbackRuleAttribute(rbRule));
 *                      }
 *                		rbta.setRollbackRules(rollbackRules);
 *
 *               		return rbta;
 *              	}
 *

 *               （2）、TransactionInterceptor ： 事务拦截器，保存了事物的相关事务注解属性信息和事务管理器
 *                    =》  extends TransactionAspectSupport implements MethodInterceptor
 *
 *                          1）、TransactionAttributeSource 存放事务的属性信息
 *
 *                          2）、TransactionManager 事务管理器
 *
 *                       因为TransactionInterceptor实现了MethodInterceptor接口，所以拥有了方法拦截器的特性：
 *                          在目标方法执行的时候，会先执行拦截器链，拦截器链本身只有TransactionInterceptor这一个拦截器
 *
 *                              事务拦截器：
 *                                 1、先获取事物的相关属性、注解信息等等
 *                                 2、在获取TransactionManager，如果没有指明具体的事务管理器类型及名称，容器会默认返回一个
 *                                 PlatformTransactionManager:
 *
 *                                 3、执行目标方法
 *                                     执行异常，获取到事务管理器进行回滚操作
 *                                     执行正常，通过事务管理器正常提交事务
 *
 *                                 =>   Object invokeWithinTransaction(Method method, @Nullable Class<?> targetClass,
 * 			                    final InvocationCallback invocation) throws Throwable;
 *
 * 	                           TransactionInfo txInfo = createTransactionIfNecessary(ptm, txAttr, joinpointIdentification);
 * 	                    		Object retVal;
 * 	                     			try {
 * 	                     				// This is an around advice: Invoke the next interceptor in the chain.
 * 	                    				// This will normally result in a target object being invoked.
 * 	                    			<!--执行目标方法
 * 	                    				retVal = invocation.proceedWithInvocation();
 * 	                              }
 * 	                              <!--碰到一场直接完成事务抛出异常
 * 	                   			catch (Throwable ex) {
 * 	                    				// target invocation exception
 * 	                    				completeTransactionAfterThrowing(txInfo, ex);
 * 	                     				throw ex;
 * 	                             }
 * 	                     		finally {
 * 	                    				cleanupTransactionInfo(txInfo);
 * 	                             }
 *
 * 	 	                   	   if (retVal != null && vavrPresent && VavrDelegate.isVavrTry(retVal)) {
 * 	                    				// Set rollback-only in case of Vavr failure matching our rollback rules...
 * 	                     				TransactionStatus status = txInfo.getTransactionStatus();
 * 	                    				if (status != null && txAttr != null) {
 * 	                    					retVal = VavrDelegate.evaluateTryFailure(retVal, txAttr, status);
 * 	                                  }
 * 	                             }
 * 	                           <!--正常执行完成，返回执行结果
 * 	                   			commitTransactionAfterReturning(txInfo);
 * 	                   			return retVal;
 *
 *
 *     =》判断异常是否是需要进行回滚的异常类型，从@Trasactional注解上获取rollback属性判断回滚具体哪些异常，
 *          如果是，则回滚事务，否则继续提交
 *          completeTransactionAfterThrowing(txInfo, ex);
 *
 *     protected void completeTransactionAfterThrowing(@Nullable TransactionInfo txInfo, Throwable ex) {
 * 		if (txInfo != null && txInfo.getTransactionStatus() != null) {
 * 			if (logger.isTraceEnabled()) {
 * 				logger.trace("Completing transaction for [" + txInfo.getJoinpointIdentification() +
 * 						"] after exception: " + ex);
 *                        }
 * 			if (txInfo.transactionAttribute != null && txInfo.transactionAttribute.rollbackOn(ex)) {
 * 				try {
 * 					txInfo.getTransactionManager().rollback(txInfo.getTransactionStatus());
 *                }
 * 				catch (TransactionSystemException ex2) {
 * 					logger.error("Application exception overridden by rollback exception", ex);
 * 					ex2.initApplicationException(ex);
 * 					throw ex2;
 *                }
 * 				catch (RuntimeException | Error ex2) {
 * 					logger.error("Application exception overridden by rollback exception", ex);
 * 					throw ex2;
 *                }
 *            }
 * 			else {
 * 				// We don't roll back on this exception.
 * 				// Will still roll back if TransactionStatus.isRollbackOnly() is true.
 * 				try {
 * 					txInfo.getTransactionManager().commit(txInfo.getTransactionStatus());
 *                }
 * 				catch (TransactionSystemException ex2) {
 * 					logger.error("Application exception overridden by commit exception", ex);
 * 					ex2.initApplicationException(ex);
 * 					throw ex2;
 *                }
 * 				catch (RuntimeException | Error ex2) {
 * 					logger.error("Application exception overridden by commit exception", ex);
 * 					throw ex2;
 *                }
 *            }* 		}
 * 	}
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

@Configuration
@EnableTransactionManagement
@ComponentScan("com.wensw.transaction")
public class TransactionConfig {

    @Bean
    public DataSource getDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        dataSource.setUrl("jdbc:mysql://localhost:3306/share_blog?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true");
        dataSource.setDriverClassLoader(Driver.class.getClassLoader());
        dataSource.setMaxActive(5);
        dataSource.setDbType(DbType.mysql);
        return dataSource;
    }

    @Bean
    public JdbcTemplate getJdbcTemplate(DataSource dataSource) {
        JdbcTemplate template = new JdbcTemplate(dataSource);
        return template;
    }

    @Bean
    /**
     * 注册事务管理器
     */
    public PlatformTransactionManager getTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
