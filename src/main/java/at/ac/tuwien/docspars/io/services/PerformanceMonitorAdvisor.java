package at.ac.tuwien.docspars.io.services;

import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;

public class PerformanceMonitorAdvisor implements MethodBeforeAdvice, AfterReturningAdvice {

	private static final Logger logger = LogManager.getLogger(PerformanceMonitorAdvisor.class.getName());

	/** Time in milliseconds */
	long startTime = 0;

	/** Time in milliseconds */
	long finishTime = 0;

	@Override
	public void before(Method method, Object[] args, Object target) throws Throwable {
		startTime = System.currentTimeMillis();
		// logger.debug("Executing method " + method.getName() + " on object " +
		// target.getClass().getName());
	}

	@Override
	public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
		finishTime = System.currentTimeMillis();
		double totalDuration = finishTime - startTime;
		logger.info("Executed " + method.getName() + " on object " + target.getClass().getName() + " in " + totalDuration / 1000 + " sec.");
	}
}
