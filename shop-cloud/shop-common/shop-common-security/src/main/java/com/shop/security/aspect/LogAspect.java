package com.shop.security.aspect;

import com.shop.core.domain.entity.SysLogs;
import com.shop.logs.logs.ApiLogsService;
import com.shop.security.utils.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import com.shop.security.annotation.Log;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

/**
 * @Author: shop-cloud
 * @Date: 2024/7/12 22:56
 * @Version: 1.0
 * @Description:
 */
@Aspect
@Component
public class LogAspect {

    @Autowired(required = false)
    private ApiLogsService logsService;

    private Instant startTime;

    @Pointcut("@annotation(com.shop.security.annotation.Log)")
    public void logPointCut() {}

    @Before("logPointCut() && @annotation(log)")
    public void doBefore(JoinPoint joinPoint, Log log) {
        startTime = Instant.now();

    }

    @After("logPointCut() && @annotation(log)")
    public void after(JoinPoint joinPoint, Log log) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        SysLogs logEntity = new SysLogs();
        logEntity.setDescribe(log.describe());
        logEntity.setMethod(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logEntity.setParams(Arrays.toString(joinPoint.getArgs()));  // 参数
        logEntity.setIp(request.getRemoteAddr());
        logEntity.setUserId(SecurityUtils.getUserId().intValue());
        Instant endTime = Instant.now();
        Duration duration = Duration.between(startTime, endTime);
        long executionTimeMillis = duration.toMillis();
        logEntity.setTime(executionTimeMillis + "ms");
        if (logsService != null) {
            CompletableFuture.runAsync(() -> {
                try {
                    logsService.add(logEntity);
                } catch (Exception ignored) {
                }
            });
        }
    }
}
