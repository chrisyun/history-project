package org.thorn.web.util;

import java.util.List;

import org.thorn.core.context.SpringContext;
import org.thorn.core.util.LocalStringUtils;
import org.thorn.dao.exception.DBAccessException;
import org.thorn.dd.entity.Dict;
import org.thorn.dd.service.IDataDictService;

/**
 * @ClassName: DDUtils
 * @Description: 数据字典的util类，提供静态的方法去调用数据字典的service方法
 * @author chenyun
 * @date 2012-5-8 下午04:48:50
 */
public class DDUtils {

	/**
	 * 
	 * @Description：根据字典类型ID获取对应的数据字典集合
	 * @author：chenyun
	 * @date：2012-5-25 上午10:25:12
	 * @param typeId
	 *            字典类型ID
	 * @return
	 * @throws DBAccessException
	 */
	public static List<Dict> queryDd(String typeId) throws DBAccessException {
		IDataDictService service = SpringContext.getBean("ddService");
		List<Dict> list = service.queryDdList(typeId);

		return list;
	}
	
	/**
	 * 
	 * @Description：
	 * @author：chenyun 	        
	 * @date：2012-5-29 下午11:19:41
	 * @param typeId
	 * @param dName
	 * @return
	 * @throws DBAccessException
	 */
	public static String queryDdById(String typeId, String dName)
			throws DBAccessException {

		List<Dict> ld = queryDd(typeId);

		for (Dict dd : ld) {
			if (LocalStringUtils.equals(dName, dd.getDname())) {
				return dd.getDvalue();
			}
		}

		return dName;
	}

}
