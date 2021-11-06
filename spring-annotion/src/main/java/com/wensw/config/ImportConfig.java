package com.wensw.config;

import com.wensw.bean.importBean.FactoryBeanT;
import com.wensw.bean.User;
import com.wensw.importBeanDefReg.MyBeanDefinitionRegistry;
import com.wensw.importSelector.MyImportSelector;
import com.wensw.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Import 快速往容器内注册bean
 * @ImportSelector 选择导入具体想要注册的bean
 */
@Configuration
@Import({UserServiceImpl.class, MyImportSelector.class, MyBeanDefinitionRegistry.class, FactoryBeanT.class})
public class ImportConfig {

    @Bean
    public User userImport() {
        return new User("测试", 20);
    }

    @Bean
    public FactoryBeanT factoryBean(){
        return new FactoryBeanT();
    }
}
