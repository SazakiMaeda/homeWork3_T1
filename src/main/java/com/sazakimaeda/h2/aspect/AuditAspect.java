package com.sazakimaeda.h2.aspect;

import com.sazakimaeda.h2.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.Arrays;

@Aspect
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditService auditService;

    @Around("@annotation(com.sazakimaeda.h2.annotation.WeylandWatchingYou)")
    public Object auditMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.toShortString();
        String params = Arrays.toString(joinPoint.getArgs());

        auditService.send("Выполнение метода" + methodName +
                          " с параметрами " + params);

        Object result;
        try {
            result = joinPoint.proceed();
            auditService.send("Метод "+ methodName +" успешно выполнен. Результат: " + result);
            return result;
        } catch (Throwable throwable) {
            auditService.send(
                    "Ошибка при выполнении метода " + methodName + ": " + throwable.getMessage());
            throw throwable;
        }
    }
}
