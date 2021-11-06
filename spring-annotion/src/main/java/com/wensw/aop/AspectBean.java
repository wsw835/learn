package com.wensw.aop;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

import java.util.Arrays;

@Aspect
public class AspectBean {

    @Pointcut(value = "execution(public Integer com.wensw.aop.DoWorkingBean.doWork(int,int))")
    public void pointCut() {

    }

    @Before(value = "pointCut()")
    public void beforeDoWork(JoinPoint joinpoint) {
        Object[] args = joinpoint.getArgs();

        System.out.println("开始执行业务方法->" + joinpoint.getSignature().getName() + "之前--->参数：{}" + Arrays.asList(args));
    }

    @After(value = "pointCut()")
    public void afterDoWork(JoinPoint joinpoint) {
        System.out.println("业务方法->" + joinpoint.getSignature().getName() + "执行结束.....");
    }

    /**
     * 注意：JoinPoint joinpoint必须作为方法第一个参数接受切点位置对应方法的签名信息
     *
     * @param joinpoint 切点
     * @param results   结果
     */
    @AfterReturning(value = "pointCut()", returning = "results")
    public void afterDoWorkReturn(JoinPoint joinpoint, Object results) {
        System.out.println("业务方法->" + joinpoint.getSignature().getName() + "执行结束返回结果: {} " + results);
    }

    @AfterThrowing(value = "pointCut()", throwing = "exception")
    public void doWorkExceptionCatch(JoinPoint joinPoint, Exception exception) {
        System.out.println("执行业务方法->" + joinPoint.getSignature().getName() + "遇到异常返回->" + exception);
    }

    @Around(value = "pointCut()")
    public void roundDoWork(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        System.out.println("执行业务方法->" + joinPoint.getSignature().getName() + "中，传入的业务方法参数：" + Arrays.asList(args));
    }
}
