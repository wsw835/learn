package com.wensw.config;

import com.wensw.bean.autowired.AutowiredBean;
import com.wensw.bean.autowired.AwaredBean;
import com.wensw.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(value = {"com.wensw.bean.autowired", "com.wensw.service"})
@PropertySource(value = "classpath:beanProperties.properties")
public class AwareBeanConfig {

    @Bean
    public AutowiredBean autowiredBean(UserServiceImpl userService) {
        return new AutowiredBean(userService);
    }

    @Bean
    public AwaredBean awaredBean() {
        return new AwaredBean();
    }
}
