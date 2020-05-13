package com.itzjx.mmall_test;

import com.itzjx.controller.backend.CategoryManagerController;
import com.itzjx.controller.portal.CartController;
import com.itzjx.controller.portal.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Enumeration;
import java.util.PriorityQueue;
import java.util.concurrent.*;

@SpringBootTest
class MmallTestApplicationTests {
    public static void main(String[] args) {
        new ThreadPoolExecutor(5,
                5,0L,TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

        try {
            new ConcurrentHashMap<>().getClass().getConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

}
