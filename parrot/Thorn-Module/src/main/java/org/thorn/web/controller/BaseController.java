package org.thorn.web.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.thorn.web.util.CustomDefaultNumberEditor;

/**
 * @ClassName: BaseController
 * @Description:controller的一个父类，对类型转换作了功能封装 1、允许数字对象类型的数据为空 2、对int类型的空值设置了默认值
 * @author chenyun
 * @date 2012-5-14 下午02:31:51
 */
public class BaseController {

	/**
	 * 初始化binder的回调函数. 允许数字类型为空，采用注解的方式需要对方法进行标注
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {

		// 必须指定该类型与pojo中的类型一致
		binder.registerCustomEditor(int.class, new CustomDefaultNumberEditor(
				Integer.class, true));

		binder.registerCustomEditor(Integer.class, new CustomNumberEditor(
				Integer.class, true));
		binder.registerCustomEditor(Long.class, new CustomNumberEditor(
				Long.class, true));
		binder.registerCustomEditor(Float.class, new CustomNumberEditor(
				Float.class, true));
		binder.registerCustomEditor(Double.class, new CustomNumberEditor(
				Double.class, true));
		binder.registerCustomEditor(BigDecimal.class, new CustomNumberEditor(
				BigDecimal.class, true));
		binder.registerCustomEditor(BigInteger.class, new CustomNumberEditor(
				BigInteger.class, true));

		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		// 设置分析过程是否严格，true表示不严格
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, false));

	}

}
