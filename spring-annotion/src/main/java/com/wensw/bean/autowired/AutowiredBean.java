package com.wensw.bean.autowired;

import com.wensw.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.inject.Inject;

@Component
public class AutowiredBean {


    /*    @Inject*/
    private UserServiceImpl userService01;

    public AutowiredBean (@Autowired UserServiceImpl userService){
        this.userService01 = userService;
    }

 /*   //该组件bean只有一个有参构造
    public AutowiredBean(UserServiceImpl userService) {
        System.out.println("若组件bean只有一个有参构造器，那么在容器启动时，对应构造参数的组件会从容器中获取，@Autowired可以省略");
        this.userService01 = userService;
    }*/



    @Override
    public String toString() {
        return "AutowiredBean{" +
                "userService=" + userService01 +
                '}';
    }
}
