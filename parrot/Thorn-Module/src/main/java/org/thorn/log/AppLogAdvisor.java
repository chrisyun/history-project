package org.thorn.log;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thorn.core.util.ExecutorUtils;
import org.thorn.dao.core.Configuration;
import org.thorn.log.entity.AppLog;
import org.thorn.log.task.LogTask;
import org.thorn.security.SecurityUserUtils;

/**
 * @ClassName: AppLogAdvisor
 * @Description: 日志AOP切面
 * @author chenyun
 * @date 2012-5-26 下午08:14:37
 */
@Aspect
public class AppLogAdvisor {

	static private Logger log = LoggerFactory.getLogger(AppLogAdvisor.class);

	/**
	 * 
	 * @Description：定义切点
	 * @author：chenyun
	 * @date：2012-5-26 下午09:56:51
	 */
	@Pointcut("@annotation(org.thorn.log.Logging)")
	private void loggingPointcut() {
	}

	@Pointcut("!within(org.thorn.log..*) "
			+ "&& !execution(public * org.thorn..*.*ServiceImpl.query*(..)) "
			+ "&& !@annotation(org.thorn.log.NoLogging)")
	private void noLoggingPointcut() {
	}

	/**
	 * 
	 * @Description：方法正确执行时拦截
	 * @author：chenyun
	 * @date：2012-5-26 下午09:57:09
	 * @param jp
	 */
	@AfterReturning("loggingPointcut() && noLoggingPointcut()")
	public void successLog(JoinPoint jp) {
		AppLog log = getAppLog(jp);
		log.setHandleResult(Configuration.DB_SUCCESS);

		sentLogger2Executor(log);
	}

	/**
	 * 
	 * @Description：方法执行跑出异常时拦截
	 * @author：chenyun
	 * @date：2012-5-26 下午09:57:49
	 * @param jp
	 * @param ex
	 */
	@AfterThrowing(value = "loggingPointcut() && noLoggingPointcut()", throwing = "ex")
	public void exceptionLog(JoinPoint jp, Exception ex) {
		AppLog log = getAppLog(jp);
		log.setHandleResult(Configuration.DB_FAILURE);
		log.setErrorMsg(ex.getMessage());

		sentLogger2Executor(log);
	}

	private void sentLogger2Executor(AppLog log) {
		LogTask task = new LogTask(log);
		ExecutorUtils.executeTask(task);
	}

	private AppLog getAppLog(JoinPoint jp) {

		Object[] args = jp.getArgs();
		String methodName = jp.getSignature().getName();

		String className = jp.getTarget().getClass().getName();
		String userId = SecurityUserUtils.getCurrentUser().getUserId();

		Class[] argsCls = new Class[args.length];
		for (int i = 0; i < argsCls.length; i++) {
			argsCls[i] = args[i].getClass();
		}

		try {
			Method mt = jp.getTarget().getClass()
					.getMethod(methodName, argsCls);

			Logging logAn = mt.getAnnotation(Logging.class);

			if (logAn != null && StringUtils.isNotBlank(logAn.value())) {
				methodName = logAn.value();
			}
		} catch (Exception e) {
			log.warn("getAppLog[getAnnotation-Method] failure", e.getMessage());
		}

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		AppLog log = new AppLog();
		log.setExecuteTime(df.format(new Date()));
		log.setMethodName(methodName);
		log.setModuleName(className);
		log.setUserId(userId);

		// 将参数转换为json
		ObjectMapper mapper = new ObjectMapper();
		try {
			String parameter = mapper.writeValueAsString(args);
			if (parameter.length() > 1000) {
				parameter = parameter.substring(0, 1000);
			}
			log.setParameters(parameter);
		} catch (Exception e) {
			log.setParameters(e.getMessage());
			e.printStackTrace();
		}

		return log;
	}
}
