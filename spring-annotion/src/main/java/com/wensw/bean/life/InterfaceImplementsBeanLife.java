package com.wensw.bean.life;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * 通过实现spring 提供的 InitializingBean、DisposableBean 自定义bean的初始化及销毁方法
 */
@Component
public class InterfaceImplementsBeanLife implements InitializingBean, DisposableBean {

    public InterfaceImplementsBeanLife() {
        System.out.println("构造对象，初始化属性 InterfaceImplementsBeanLife");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("在所有属性设置完后进行bean的初始化.... InterfaceImplementsBeanLife ");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("在容器关闭后自动销毁所有的单实例bean.... InterfaceImplementsBeanLife");
    }


}
