package me.hikingcarrot7.privee.utils;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import lombok.extern.java.Log;

import java.util.logging.Level;

@PerformanceLog
@Interceptor
@Log
public class PerformanceLogInterceptor {

  @AroundInvoke
  public Object log(InvocationContext context) throws Exception {
    long start = System.currentTimeMillis();
    Object result = context.proceed();
    log.log(Level.INFO, () -> String.format(
            "Executed method: %s in class %s. Execution took: %d ms",
            context.getMethod().getName(),
            context.getMethod().getDeclaringClass().getName(),
            (System.currentTimeMillis() - start)
        )
    );
    return result;
  }

}
