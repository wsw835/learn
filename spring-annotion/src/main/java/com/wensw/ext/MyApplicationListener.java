package com.wensw.ext;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.EventListener;

/**
 * @author: wensw
 * @date: 2021/7/12
 * @description:
 *
 *  ContextRefreshedEvent:
 *  ContextClosedEvent:
 *
 */
@Component
public class MyApplicationListener implements ApplicationListener<ApplicationEvent> {

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        Object source = event.getSource();
        System.out.println("event---->" + event);
    }

}
