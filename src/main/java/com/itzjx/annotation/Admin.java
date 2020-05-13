package com.itzjx.annotation;

import java.lang.annotation.*;

/**
 * 自定义注解，用于controller层的角色权限控制
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Admin {
    String value() default "";
}

