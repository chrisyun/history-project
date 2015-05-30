package org.thorn.dao.mybatis.plugin;

import java.sql.Statement;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.type.JdbcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thorn.core.util.LocalStringUtils;

@Intercepts({ @Signature(type = StatementHandler.class, method = "update", args = { Statement.class }) })
public class SqlLoggingInterceptor implements Interceptor {

	static Logger log = LoggerFactory.getLogger(SqlLoggingInterceptor.class);

	private List<SqlLoggingHandler> handlers;

	private String filter;

	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler) invocation
				.getTarget();

		BoundSql boundSql = statementHandler.getBoundSql();

		// 预编译sql
		String preparedSql = boundSql.getSql().replaceAll("\n|\r", "");
		log.debug("the boundSql:{}", preparedSql);

		// 根据过滤器判定是否需要执行handler
		if (LocalStringUtils.isNotEmpty(filter)) {

			Pattern pattern = Pattern.compile(filter, Pattern.CASE_INSENSITIVE);
			Matcher m = pattern.matcher(preparedSql);

			if (!m.matches()) {
				return invocation.proceed();
			}
		}

		String logSql = preparedSql;
		// 进行SQL占位符替换，只区分是否VARCHAR类型的
		for (ParameterMapping mapping : boundSql.getParameterMappings()) {
			String parameter = mapping.getProperty();
			JdbcType jdbcType = mapping.getJdbcType();

			String value = (String) boundSql.getAdditionalParameter(parameter);

			if (jdbcType == JdbcType.VARCHAR) {
				logSql = logSql.replaceFirst("\\?", "'" + value + "'");
			} else {
				logSql = logSql.replaceFirst("\\?", value);
			}
		}

		log.debug("the logsql:{}", logSql);

		for (SqlLoggingHandler handler : handlers) {
			handler.dispose(logSql);
		}

		return invocation.proceed();
	}

	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	public void setProperties(Properties properties) {
		this.filter = properties.getProperty("filter");
	}

	public List<SqlLoggingHandler> getHandlers() {
		return handlers;
	}

	public void setHandlers(List<SqlLoggingHandler> handlers) {
		this.handlers = handlers;
	}

}
