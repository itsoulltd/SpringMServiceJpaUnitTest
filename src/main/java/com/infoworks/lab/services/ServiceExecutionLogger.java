package com.infoworks.lab.services;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Arrays;
import java.util.List;

@Aspect
@Component
public class ServiceExecutionLogger {

    private static Logger LOG = LoggerFactory.getLogger(ServiceExecutionLogger.class);

    @Around("execution(* com.infoworks.lab.services..*(..))")
    public Object profileAllMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        //Measure method execution time
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result = proceedingJoinPoint.proceed();
        stopWatch.stop();

        //Get intercepted method details
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        /*List<String> paramsType = Arrays.stream(methodSignature.getParameterTypes())
                .map(val -> val.getSimpleName())
                .collect(Collectors.toList());*/
        List<String> paramsName = Arrays.asList(methodSignature.getParameterNames());

        //Log method execution time
        LOG.info("Execution time of {}.{}({}) :: {} {}"
                , className
                , methodName
                , String.join(",", paramsName)
                , stopWatch.getTotalTimeMillis()
                , "ms");
        return result;
    }

}
