package com.wensw.config;

import com.wensw.condition.LCondition;
import com.wensw.condition.ZCondition;
import com.wensw.bean.User;
import com.wensw.filter.MyBeanFilter;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScans(value = {
        @ComponentScan(value = "com.wensw", excludeFilters = {
               /* @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Controller.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,classes = User.class),
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = {
                        "Service"
                })*/
                @ComponentScan.Filter(type = FilterType.CUSTOM, classes = MyBeanFilter.class)
        }, useDefaultFilters = false)
})
public class ApplicationConfig {

    /**
     * 默认不指定bean的名称时，通过@Bean注解注册的bean名称为对应方法的名称 -> init
     */
    @Bean
    /**
     * {
     *      singleton: 单实例 ，在容器启动时自动调用方法创建对象放到容器中，之后的每一次获取都是直接从容器中获取，类似Map.get()
     *      prototype: 多实例，不会在容器启动时调用方法创建对象放入容器，在获取时才调用方法创建对象放入容器
     *      request: 同一次请求创建一个实例
     *      session: 同一个session创建一个实例
     * }
     */
    @Scope("singleton")
    /**
     * 懒加载 ： 只有第一次获取的时候才会创建对象、 只针对单实例singleton
     */
    @Lazy
    public User init() {
        System.out.println("容器创建时，自动调用相关方法注册组件");
        return new User("王五", 18);
    }

    @Conditional(value = {ZCondition.class})
    @Bean("张三")
    public User userZ() {
        //创建张三
        return new User("张三", 18);
    }

    @Conditional(value = {LCondition.class})
    @Bean("李四")
    public User userL() {
        //创建张三
        return new User("李四", 32);
    }

}
