package org.thorn.attachment.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.thorn.attachment.entity.Attachment;
import org.thorn.dao.exception.DBAccessException;

/**
 * @ClassName: AttachmentDaoImpl
 * @Description:
 * @author chenyun
 * @date 2012-6-7 下午09:06:43
 */
public class AttachmentDaoImpl implements IAttachmentDao {

	private final static String nameSpace = "AttMapper.";

	@Autowired
	@Qualifier("sqlSessionTemplate")
	private SqlSessionTemplate sqlSessionTemplate;

	public int save(Attachment att) throws DBAccessException {
		try {
			return sqlSessionTemplate.insert(nameSpace + "insert", att);
		} catch (Exception e) {
			throw new DBAccessException("AttachmentDaoImpl", "save", e);
		}
	}

	public int delete(List<String> ids) throws DBAccessException {
		try {
			return sqlSessionTemplate.delete(nameSpace + "delete", ids);
		} catch (Exception e) {
			throw new DBAccessException("AttachmentDaoImpl", "delete", e);
		}
	}

	public long queryPageCount(Map<String, Object> filter)
			throws DBAccessException {
		try {
			long count = (Long) sqlSessionTemplate.selectOne(nameSpace
					+ "selectPageCount", filter);

			return count;
		} catch (Exception e) {
			throw new DBAccessException("AttachmentDaoImpl", "queryPageCount",
					e);
		}
	}

	public List<Attachment> queryList(Map<String, Object> filter)
			throws DBAccessException {
		try {
			return (List<Attachment>) sqlSessionTemplate.selectList(nameSpace
					+ "selectPage", filter);
		} catch (Exception e) {
			throw new DBAccessException("AttachmentDaoImpl", "queryList", e);
		}
	}

	public Attachment queryAtt(Integer id) throws DBAccessException {
		try {
			return (Attachment) sqlSessionTemplate.selectOne(nameSpace
					+ "selectAtt", id);
		} catch (Exception e) {
			throw new DBAccessException("AttachmentDaoImpl", "queryAtt", e);
		}
	}

}
