package com.itzjx;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

public class StarterApplicationListener implements ApplicationListener {
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        System.out.println(event.toString());
        System.out.println("ApplicationListener .... " + System.currentTimeMillis());
    }
}
