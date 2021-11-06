package com.wensw.ext;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * @author: wensw
 * @date: 2021/7/12
 * @description:
 */
@Setter
@Getter
public class MyEvent extends ApplicationEvent {

    private String name;

    private String config;

    public MyEvent(Object source) {
        super(source);
    }

    @Override
    public String toString() {
        return "MyEvent{" +
                "name='" + name + '\'' +
                ", config='" + config + '\'' +
                '}';
    }
}
