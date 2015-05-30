package org.thorn.log.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.thorn.dao.exception.DBAccessException;
import org.thorn.log.entity.AppLog;

/**
 * @ClassName: AppLogDaoImpl
 * @Description:
 * @author chenyun
 * @date 2012-5-26 下午03:38:18
 */
public class AppLogDaoImpl implements IAppLogDao {

	private final static String nameSpace = "LogMapper.";

	@Autowired
	@Qualifier("sqlSessionTemplate")
	private SqlSessionTemplate sqlSessionTemplate;

	public int save(AppLog log) throws DBAccessException {
		try {
			return sqlSessionTemplate.insert(nameSpace + "insert", log);
		} catch (Exception e) {
			throw new DBAccessException("AppLogDaoImpl", "save", e);
		}
	}

	public long queryPageCount(Map<String, Object> filter)
			throws DBAccessException {
		try {
			return (Long) sqlSessionTemplate.selectOne(nameSpace
					+ "selectPageCount", filter);
		} catch (Exception e) {
			throw new DBAccessException("AppLogDaoImpl", "queryPageCount", e);
		}
	}

	public List<AppLog> queryList(Map<String, Object> filter)
			throws DBAccessException {
		try {
			return (List<AppLog>) sqlSessionTemplate.selectList(nameSpace
					+ "selectPage", filter);
		} catch (Exception e) {
			throw new DBAccessException("AppLogDaoImpl", "queryList", e);
		}
	}

}
