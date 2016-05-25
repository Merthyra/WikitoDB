package at.ac.tuwien.docspars.io.services;

import at.ac.tuwien.docspars.util.ProcessMetrics;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;
import java.time.Duration;

public class PerformanceMonitorAdvisor implements MethodBeforeAdvice, AfterReturningAdvice {

  private static final Logger logger = LogManager.getLogger(PerformanceMonitorAdvisor.class);
  private ProcessMetrics metrics;

  public void setMetrics(ProcessMetrics metrics) {
    this.metrics = metrics;
  }

  private long startTime = 0;

  @Override
  public void before(Method method, Object[] args, Object target) throws Throwable {
    startTime = System.currentTimeMillis();
  }

  @Override
  public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
    Duration totalDuration = Duration.ofMillis(System.currentTimeMillis() - startTime);
    metrics.addOperationTime(method, totalDuration);
    logger.info("Executed {} on object {} in {} seconds!", method.getName(), target.getClass().getName(), totalDuration.getSeconds());
  }

}