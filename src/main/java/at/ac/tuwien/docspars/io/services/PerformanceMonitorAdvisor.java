package at.ac.tuwien.docspars.io.services;

import at.ac.tuwien.docspars.util.ProcessMetrics;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class PerformanceMonitorAdvisor implements MethodBeforeAdvice, AfterReturningAdvice {

  private static final Logger logger = LogManager.getLogger(PerformanceMonitorAdvisor.class);
  private ProcessMetrics metrics;
  private static final Map<Class<?>, Long> startTimes = new HashMap<>();

  public void setMetrics(ProcessMetrics metrics) {
    this.metrics = metrics;
  }

  @Override
  public void before(Method method, Object[] args, Object target) throws Throwable {
    startTimes.put(target.getClass(), System.currentTimeMillis());
  }

  @Override
  public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
    Duration totalDuration = Duration.ofMillis(System.currentTimeMillis() - startTimes.get(target.getClass()));
    String executedMethodId = target.getClass().getSimpleName() + "::" + method.getName();
    metrics.addOperationTime(executedMethodId, totalDuration);
    logger.info("Executed {} in {} !", executedMethodId, ProcessMetrics.formatDurationHHMMSSsss(totalDuration));
  }
}