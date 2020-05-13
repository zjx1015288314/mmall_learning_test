package com.itzjx.controller.verifacation;


import com.itzjx.common.Const;
import com.itzjx.common.ServerResponse;
import com.itzjx.pojo.User;
import com.itzjx.service.IUserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Aspect
@Component
@Order(2) //在VerificationAspect之后
public class AdminAspect {

    private static final Logger logger = LoggerFactory.getLogger(AdminAspect.class);

    @Autowired
    private IUserService iUserService;

    @Pointcut(value = "@annotation(com.itzjx.annotation.Admin)")
    public void annotationPointCut() {
    }

    @Around("annotationPointCut()")
    public Object doAround(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        String methodName = signature.getMethod().getName();
        logger.info("方法" + methodName + "已被拦截:");
        if(!validate()){
            return ServerResponse.createByErrorMessage("需要管理员权限");
        }
        try {
            return pjp.proceed();
        } catch (Throwable throwable) {
            return null;
        }
    }

    private boolean validate(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        //校验是否是管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return true;
        }
        return false;
    }

}
