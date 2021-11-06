package com.wensw.config;

import com.wensw.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Spring 依赖注入DI : 完成对容器中每个组件之间依赖关系的赋值
 * 1、 @Autowired :
 * 优先按类型去容器中找对应的bean，  context.getBean(Class beanClass);
 * 如果找到了多个相同类型的bean，则按照属性名称作为bean的id去容器中查找对应bean ,context.getBean(String beanName)
 * <p>
 * 结合： @Qualifier(String beanName) 可以指定自动装配的组件bean的id，
 * <p>
 * <!--nested exception is org.springframework.beans.factory.NoSuchBeanDefinitionException:
 * <!--No qualifying bean of type 'com.wensw.service.impl.UserServiceImpl' available:
 * <!--expected at least 1 bean which qualifies as autowire candidate
 * <p>
 * 注意点： 使用@Autowired注解装配的bean一定要完成属性赋值、且已经注册在容器中，否则容器无法获取到bean就会报错
 * <p>
 * 结合： @Primary 可以指定默认优先加载的bean
 * 一般实际使用存在多个同类型bean的情况下，使用@Primary注解标注一个默认注册的bean，
 * 再结合@Qualifier注解去按需装配想要的bean
 * <p>
 * 注解的属性： required(default = true) 默认@Autowired必须装配到具体的bean，若bean未找到则会报错
 * 通过设置@Autowired(required = false ) 可以避免在装配当前bean时，因未从容器中获取到bean实例对象报错
 * <p>
 * 2、@Resource: JSR250提供的标准注解，属于java规范内置的一个注解，而@Autowired是由Spring框架产出的东西
 * <p>
 * 通过@Resource标注装配的bean时，默认会按属性的名称去容器内查找bean
 * <p>
 * <!--### 注意：如果此刻从容器中未获取到指定属性名称的bean时-->
 * <p>
 * CommonAnnotationBeanPostProcessor ：  公共注解后置处理器
 * <p>
 * <!-- 获取到@Resource注解标注的属性对应的注释元素、成员变量、属性描述等信息 -->
 * <p>
 * private class ResourceElement extends CommonAnnotationBeanPostProcessor.LookupElement {
 * private final boolean lazyLookup;
 * <p>
 * public ResourceElement(Member member, AnnotatedElement ae, @Nullable PropertyDescriptor pd) {
 * super(member, pd);
 * Resource resource = (Resource)ae.getAnnotation(Resource.class);
 * String resourceName = resource.name();
 * Class<?> resourceType = resource.type();
 * this.isDefaultName = !StringUtils.hasLength(resourceName);
 * if (this.isDefaultName) {
 * resourceName = this.member.getName();
 * if (this.member instanceof Method && resourceName.startsWith("set") && resourceName.length() > 3) {
 * resourceName = Introspector.decapitalize(resourceName.substring(3));
 * }
 * } else if (CommonAnnotationBeanPostProcessor.this.embeddedValueResolver != null) {
 * resourceName = CommonAnnotationBeanPostProcessor.this.embeddedValueResolver.resolveStringValue(resourceName);
 * }
 * <p>
 * if (Object.class != resourceType) {
 * this.checkResourceType(resourceType);
 * } else {
 * resourceType = this.getResourceType();
 * }
 * <p>
 * this.name = resourceName != null ? resourceName : "";
 * this.lookupType = resourceType;
 * String lookupValue = resource.lookup();
 * this.mappedName = StringUtils.hasLength(lookupValue) ? lookupValue : resource.mappedName();
 * Lazy lazy = (Lazy)ae.getAnnotation(Lazy.class);
 * this.lazyLookup = lazy != null && lazy.value();
 * }
 * <p>
 * protected Object getResourceToInject(Object target, @Nullable String requestingBeanName) {
 * return this.lazyLookup ?
 * CommonAnnotationBeanPostProcessor.this.buildLazyResourceProxy(this, requestingBeanName)
 * <p>
 * <!--如果不是标注懒加载@lazy的单实例bean-- 调用getResource(LookupElement element,String requestingBeanName)
 * : CommonAnnotationBeanPostProcessor.this.getResource(this, requestingBeanName);
 * }
 * }
 * <p>
 * getResource : 获取bean
 * <p>
 * protected Object getResource(CommonAnnotationBeanPostProcessor.LookupElement element,
 *
 * @Nullable String requestingBeanName) throws NoSuchBeanDefinitionException {
 * if (StringUtils.hasLength(element.mappedName)) {
 * return this.jndiFactory.getBean(element.mappedName, element.lookupType);
 * } else if (this.alwaysUseJndiLookup) {
 * return this.jndiFactory.getBean(element.name, element.lookupType);
 * } else if (this.resourceFactory == null) {
 * throw new NoSuchBeanDefinitionException(element.lookupType,
 * "No resource factory configured - specify the 'resourceFactory' property");
 * } else {
 * <!--调用autowireResource(this.resourceFactory, element, requestingBeanName)装配bean-->
 * return this.autowireResource(this.resourceFactory, element, requestingBeanName);
 * }
 * }
 * <p>
 * ---> 自动装配bean
 * autowireResource(BeanFactory factory,
 * CommonAnnotationBeanPostProcessor.LookupElement element,
 * @Nullable String requestingBeanName) ：
 * <p>
 * protected Object autowireResource(BeanFactory factory,
 * CommonAnnotationBeanPostProcessor.LookupElement element,
 * @Nullable String requestingBeanName) throws NoSuchBeanDefinitionException {
 * String name = element.name;
 * Object resource;
 * Object autowiredBeanNames;
 * <!--如果不是spring提供的AutowireCapableBeanFactory 装配的bean-->
 * if (factory instanceof AutowireCapableBeanFactory) {
 * AutowireCapableBeanFactory beanFactory = (AutowireCapableBeanFactory)factory;
 * DependencyDescriptor descriptor = element.getDependencyDescriptor();
 * if (this.fallbackToDefaultTypeMatch && element.isDefaultName && !factory.containsBean(name)) {
 * autowiredBeanNames = new LinkedHashSet();
 * resource = beanFactory.resolveDependency(descriptor, requestingBeanName, (Set)autowiredBeanNames, (TypeConverter)null);
 * if (resource == null) {
 * throw new NoSuchBeanDefinitionException(element.getLookupType(), "No resolvable resource object");
 * }
 * } else {
 * resource = beanFactory.resolveBeanByName(name, descriptor);
 * autowiredBeanNames = Collections.singleton(name);
 * }
 * } else {
 * <!--直接由BeanFactory工厂去容器中查找bean,
 * <!--这里首先会从所有配置类标注的bean下查找容器内注册的同@Resource标注bean相同类型的bean
 * <!--如果按类型未找到bean，则直接抛异常 - >未找到该类型的bean
 * <!--如果按类型找到且仅有一个则自动装配该bean-->
 * <!--如果按类型找到多个则查看是否有@Primary标注的默认bean,如果没有，则抛出存在多个同类型bean的异常-->
 * <!--: No qualifying bean of type 'com.wensw.service.impl.UserServiceImpl' available:
 * <!--expected single matching bean but found 2: userService2,userService3
 * resource = factory.getBean(name, element.lookupType);
 * autowiredBeanNames = Collections.singleton(name);
 * }
 * <p>
 * if (factory instanceof ConfigurableBeanFactory) {
 * ConfigurableBeanFactory beanFactory = (ConfigurableBeanFactory)factory;
 * Iterator var11 = ((Set)autowiredBeanNames).iterator();
 * <p>
 * while(var11.hasNext()) {
 * String autowiredBeanName = (String)var11.next();
 * if (requestingBeanName != null && beanFactory.containsBean(autowiredBeanName)) {
 * beanFactory.registerDependentBean(autowiredBeanName, requestingBeanName);
 * }
 * }
 * }
 * <p>
 * return resource;
 * }
 * <p>
 * 3、@Inject: JSR330提供的一个注解，使用该注解时需要导入javax.inject依赖包
 * 其作用等同于 @Autowired ，也是先按类型去查找容器中所有该类型的bean，
 * 如果存在多个同类型bean时，按属性的名称作为bean的id获取对应bean
 * 但是@Inject是个无任何属性的注解，也就没有@Autowired（require=false）这种能够适应找不到bean时不装配bean的条件
 * <p>
 * <p>
 * <p>
 * AutowiredAnnotationBeanPostProcessor : 解析完成自动装配处理
 * 默认加在IOC容器中的组件，容器启动时会调用无参构造创建bean对象，在进行赋值初始化等操作
 * 1、@Autowired ： 可标注在构造器、方法、参数、属性上
 * 1）标注在构造器上：
 */
@Configuration
@ComponentScan(value = {"com.wensw.service", "com.wensw.bean.autowired"})
public class BeanAutowiredConfig {

    @Primary
    @Bean(value = "userService2")
    public UserServiceImpl userService02() {
        UserServiceImpl bean = new UserServiceImpl();
        bean.setName("initBean02");
        return bean;
    }

    @Bean(value = "userService3")
    public UserServiceImpl userService03() {
        UserServiceImpl bean = new UserServiceImpl();
        bean.setName("initBean03");
        return bean;
    }

}
