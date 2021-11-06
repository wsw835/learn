package com.wensw.ext;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * @author: wensw
 * @date: 2021/7/22
 * @description:
 */
@Service
public class ExtendListenerService {

    @EventListener(classes = {ApplicationEvent.class})
    public void listen(ApplicationEvent event) {
        System.out.println("ExtendListenerService监听到event" + event);
    }
}
