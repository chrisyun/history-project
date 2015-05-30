package org.thorn.dd.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.thorn.dao.exception.DBAccessException;
import org.thorn.dd.entity.Dict;
import org.thorn.dd.entity.DictType;

/**
 * @ClassName: DataDictDaoImpl
 * @Description:
 * @author chenyun
 * @date 2012-5-7 上午10:46:30
 */
public class DataDictDaoImpl implements IDataDictDao {

	private final static String nameSpace = "DictMapper.";

	@Autowired
	@Qualifier("sqlSessionTemplate")
	private SqlSessionTemplate sqlSessionTemplate;

	public List<DictType> queryDtList(Map<String, Object> filter)
			throws DBAccessException {
		try {
			return (List<DictType>) sqlSessionTemplate.selectList(nameSpace
					+ "selectTypePage", filter);
		} catch (Exception e) {
			throw new DBAccessException("DataDictImpl", "queryDtList", e);
		}
	}

	public long queryDtPageCount(Map<String, Object> filter)
			throws DBAccessException {
		try {
			return (Long) sqlSessionTemplate.selectOne(nameSpace
					+ "selectTypePageCount", filter);
		} catch (Exception e) {
			throw new DBAccessException("DataDictImpl", "queryDtPageCount", e);
		}
	}

	public List<Dict> queryDdList(Map<String, Object> filter)
			throws DBAccessException {
		try {
			return (List<Dict>) sqlSessionTemplate.selectList(nameSpace
					+ "selectDict", filter);
		} catch (Exception e) {
			throw new DBAccessException("DataDictImpl", "queryDdList", e);
		}
	}

	public int saveDd(Dict dd) throws DBAccessException {
		try {
			return sqlSessionTemplate.insert(nameSpace + "insertDict", dd);
		} catch (Exception e) {
			throw new DBAccessException("DataDictImpl", "saveDd", e);
		}
	}

	public int saveDt(DictType dt) throws DBAccessException {
		try {
			return sqlSessionTemplate.insert(nameSpace + "insertDictType", dt);
		} catch (Exception e) {
			throw new DBAccessException("DataDictImpl", "saveDt", e);
		}
	}

	public int modifyDd(Dict dd) throws DBAccessException {
		try {
			return sqlSessionTemplate.update(nameSpace + "updateDict", dd);
		} catch (Exception e) {
			throw new DBAccessException("DataDictImpl", "modifyDd", e);
		}
	}

	public int modifyDt(DictType dt) throws DBAccessException {
		try {
			return sqlSessionTemplate.update(nameSpace + "updateDictType", dt);
		} catch (Exception e) {
			throw new DBAccessException("DataDictImpl", "modifyDt", e);
		}
	}

	public int deleteDd(Map<String, Object> filter) throws DBAccessException {
		try {
			return sqlSessionTemplate.delete(nameSpace + "deleteDict", filter);
		} catch (Exception e) {
			throw new DBAccessException("DataDictImpl", "deleteDd", e);
		}
	}

	public int deleteDt(List<String> ids) throws DBAccessException {
		try {
			return sqlSessionTemplate.delete(nameSpace + "deleteDictType", ids);
		} catch (Exception e) {
			throw new DBAccessException("DataDictImpl", "deleteDt", e);
		}
	}

	public int deleteDdByType(List<String> ids) throws DBAccessException {
		try {
			return sqlSessionTemplate
					.delete(nameSpace + "deleteByTypeIds", ids);
		} catch (Exception e) {
			throw new DBAccessException("DataDictImpl", "deleteDdByType", e);
		}
	}

}
