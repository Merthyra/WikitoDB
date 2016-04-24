package at.ac.tuwien.docspars.io.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

public class PerformanceMonitorAdvisor implements MethodBeforeAdvice, AfterReturningAdvice {

  private static final Logger logger = LogManager.getLogger("PerformanceReport");

  /** Time in milliseconds */
  private long startTime = 0;

  /** Time in milliseconds */
  private long finishTime = 0;

  @Override
  public void before(Method method, Object[] args, Object target) throws Throwable {
    startTime = System.currentTimeMillis();
  }

  @Override
  public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
    finishTime = System.currentTimeMillis();
    double totalDuration = finishTime - startTime;
    logger.info("Executed " + method.getName() + " on object " + target.getClass().getName() + " in " + totalDuration / 1000 + " sec.");
  }
}