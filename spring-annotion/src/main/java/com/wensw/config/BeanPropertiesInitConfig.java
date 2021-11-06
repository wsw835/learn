package com.wensw.config;

import com.wensw.bean.properties.BeanProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = {"classpath:/beanProperties.properties"})
public class BeanPropertiesInitConfig {

    @Bean
    public BeanProperties beanProperties() {
        return new BeanProperties();
    }

}
