package com.wensw.bean.importBean;

import com.wensw.bean.User;
import org.springframework.beans.factory.FactoryBean;

/**
 * 创建一个spring创建的工厂bean,默认获取到的bean为通过getObject方法返回的对象
 *
 */
public class FactoryBeanT implements FactoryBean<User> {

    @Override
    public User getObject() throws Exception {
        System.out.println("注册一个对象放入工厂bean");
        return new User("工厂bean", 23);
    }

    @Override
    public Class<?> getObjectType() {
        return User.class;
    }

    @Override
    public boolean isSingleton() {
        //返回true表示这是一个单例对象，既在容器创建时就会自动调用方法将这个bean注册进容器，之后直接从容器中取
        return true;
    }
}
