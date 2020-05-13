package com.itzjx.controller.verifacation;

import com.itzjx.common.Const;
import com.itzjx.common.ResponseCode;
import com.itzjx.common.ServerResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 用于拦截controller下大部分方法，用于登录认证
 */
@Aspect
@Component
@Order(1)
public class LoginVerificationAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoginVerificationAspect.class);

    //拦截条件
    @Pointcut("execution(* com.itzjx.controller..*.*(..)) && !execution(* com.itzjx.controller..User*.*(..))" )
    public void pt1() {}
    @Around("pt1()")
    public Object signVerification(ProceedingJoinPoint pjp) throws Throwable{
        //登录验证
        if (Verification()){
            //已经登录
            return pjp.proceed();//继续执行被拦截的方法
        } else {
            //未登录
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
    }

    /**
     * 验证是否登录
     * @return
     */
    public boolean Verification() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession();

        //该方法也可以获得传入的参数
//    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//    User user = (User) requestAttributes.getAttribute(Const.CURRENT_USER,RequestAttributes.SCOPE_SESSION);
        logger.info("拦截成功,session.attribute: " + session.getAttribute(Const.CURRENT_USER));
        if (session.getAttribute(Const.CURRENT_USER) == null) {
            return false;
        }
        return true;
    }

}
