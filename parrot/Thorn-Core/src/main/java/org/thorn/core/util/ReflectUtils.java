package org.thorn.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: ReflectUtils
 * @Description:
 * @author chenyun
 * @date 2012-5-2 上午10:56:17
 */
public class ReflectUtils {

	static Logger log = LoggerFactory.getLogger(ReflectUtils.class);

	/**
	 * 
	 * @Description：将map映射为javabean对象
	 * @author：chenyun
	 * @date：2012-5-2 下午02:30:19
	 * @param <T>
	 * @param map
	 *            映射数据源，field对应value
	 * @param bean
	 *            对象的类型
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static <T> T map2Object(Map<String, Object> map, Class<T> bean)
			throws InstantiationException, IllegalAccessException {
		T obj = bean.newInstance();

		Set<String> keys = map.keySet();

		for (String key : keys) {
			String setMethod = "set" + key.substring(0, 1).toUpperCase()
					+ key.substring(1);

			try {
				Method set = bean.getMethod(setMethod, map.get(key).getClass());
				set.invoke(obj, map.get(key));
			} catch (Exception e) {
				log.warn("ReflectUtils do map2Object exception,method name:{}",
						setMethod);
			}
		}

		return obj;
	}

	/**
	 * 
	 * @Description：将javabean对象映射为map
	 * @author：chenyun
	 * @date：2012-5-2 下午02:31:50
	 * @param obj
	 * @return
	 */
	public static Map<String, Object> object2Map(Object obj) {

		Class objCl = obj.getClass();
		Field[] fields = objCl.getDeclaredFields();
		Map<String, Object> map = new HashMap<String, Object>();

		for (Field field : fields) {
			String fieldName = field.getName();
			String getMethod = "get" + fieldName.substring(0, 1).toUpperCase()
					+ fieldName.substring(1);

			try {
				Method get = objCl.getMethod(getMethod);
				Object value = get.invoke(obj);

				map.put(fieldName, value);
			} catch (Exception e) {
				log.warn("ReflectUtils do object2Map exception,method name:{}",
						getMethod);
			}
		}

		return map;
	}

	/**
	 * 
	 * @Description：将javabean的list数组转换成list的object数组
	 * @author：chenyun
	 * @date：2012-5-2 下午02:32:29
	 * @param <T>
	 * @param listBean
	 * @return 数据源为空则为null，否则第一列为javabean的field数组
	 */
	public static <T> List<Object[]> object2Array(List<T> listBean) {

		if (listBean == null || listBean.size() == 0) {
			return null;
		}

		Class<T> cls = (Class<T>) listBean.get(0).getClass();
		List<Object[]> list = new ArrayList<Object[]>();

		// set field
		Field[] fieldArray = cls.getDeclaredFields();
		String[] header = new String[fieldArray.length];

		for (int i = 0; i < header.length; i++) {
			header[i] = fieldArray[i].getName();
		}
		list.add(header);

		// set bean value
		for (T bean : listBean) {
			Object[] objArray = new Object[header.length];

			for (int i = 0; i < header.length; i++) {
				String getMethod = "get"
						+ header[i].substring(0, 1).toUpperCase()
						+ header[i].substring(1);

				try {
					Method get = cls.getMethod(getMethod);
					objArray[i] = get.invoke(bean);
				} catch (Exception e) {
					log.warn(
							"ReflectUtils do object2Map exception,method name:{}",
							getMethod);
				}
			}

			list.add(objArray);
		}

		return list;
	}

	/**
	 * 
	 * @Description：将object数组的list集合转换为javabean的list集合
	 * @author：chenyun
	 * @date：2012-5-2 下午02:34:25
	 * @param <T>
	 * @param array
	 * @param cls
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static <T> List<T> array2Object(List<Object[]> array, Class<T> cls)
			throws InstantiationException, IllegalAccessException {

		if (array == null || array.size() == 0) {
			return null;
		}

		String[] header = (String[]) array.get(0);
		List<T> list = new ArrayList<T>();

		for (int i = 1; i < array.size(); i++) {

			Object[] objArray = array.get(i);
			T bean = cls.newInstance();

			for (int j = 0; j < header.length; j++) {
				String setMethod = "set"
						+ header[j].substring(0, 1).toUpperCase()
						+ header[j].substring(1);

				try {
					Method set = cls.getMethod(setMethod,
							objArray[j].getClass());
					set.invoke(bean, objArray[j]);
				} catch (Exception e) {
					log.warn(
							"ReflectUtils do array2Object exception,method name:{}",
							setMethod);
				}
			}
			list.add(bean);
		}

		return list;
	}

}
