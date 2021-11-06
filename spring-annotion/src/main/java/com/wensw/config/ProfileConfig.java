package com.wensw.config;

import com.wensw.bean.profile.DatasourceProfile;
import org.springframework.context.annotation.*;

/**
 * @Profile 按环境注册对应bean
 * 标注在类上时，类中所有bean都要在对应设置的环境下才能被注册进容器
 * 未添加该注解@Profile的bean默认都会被注册进容器
 */
@Configuration
@ComponentScan(value = "com.wensw.bean.profile")
public class ProfileConfig {

    @Bean
    @Profile(value = {"test"})
    public DatasourceProfile testDatasource() {
        DatasourceProfile test = new DatasourceProfile();
        test.setUrl("testUrl");
        test.setPassword("testPassword");
        test.setUser("testUser");
        test.setDriverClass("testDriverClass");
        test.setTimeZone("testTimeZone");
        return test;
    }

    @Bean
    @Profile(value = {"dev"})
    public DatasourceProfile devDatasource() {
        DatasourceProfile dev = new DatasourceProfile();
        dev.setUrl("devUrl");
        dev.setPassword("devPassword");
        dev.setUser("devUser");
        dev.setDriverClass("devDriverClass");
        dev.setTimeZone("devTimeZone");
        return dev;
    }

    @Bean
    @Profile(value = {"prod"})
    public DatasourceProfile prodDatasource() {
        DatasourceProfile prod = new DatasourceProfile();
        prod.setUrl("prodUrl");
        prod.setPassword("prodPassword");
        prod.setUser("prodUser");
        prod.setDriverClass("prodDriverClass");
        prod.setTimeZone("prodTimeZone");
        return prod;
    }
}
