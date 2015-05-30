package com.talkweb.ncfw.action;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.talkweb.ncframework.pub.common.PageBean;
import com.talkweb.ncframework.pub.dao.IGenericDAO;
import com.talkweb.ncframework.pub.utils.AssertUtils;
import com.talkweb.ncframework.pub.web.common.utils.HttpServletUtils;
import com.talkweb.ncframework.pub.web.struts2.BaseAction;

/**
 * <p>文件名称: NamingSqlAction.java</p>
 * <p>文件描述: 支持命名sql的Action</p>
 * <p>版权所有: 版权所有(C)2010</p>
 * <p>公　　司: 拓维信息系统股份有限公司</p>
 * <p>内容摘要: 简要描述本文件的内容，包括主要模块、函数及能的说明</p>
 * <p>其他说明: 其它内容的说明</p>
 * <p>完成日期: 2011-6-1</p>
 * <p>修改记录1:</p>
 * <pre>
 *    修改日期:
 *    修 改 人:
 *    修改内容:
 * </pre>
 * <p>修改记录2：…</p>
 * @author  Wuqingming
 */
public class NamingSqlAction extends BaseAction {
	private static Logger logger = Logger.getLogger(NamingSqlAction.class);
	private IGenericDAO genericDAO;
	
	public IGenericDAO getGenericDAO() {
		return genericDAO;
	}
	public void setGenericDAO(IGenericDAO genericDAO) {
		this.genericDAO = genericDAO;
	}
	
	/**
	 * 
	 * @author：Wuqingming 	        
	 * @date：2011-6-1
	 * @Description：支持命名sql的分页查询
	 * @return
	 */
	public String queryPageBean () {
		String countSql		= this.getParameter("countSql");
		String mainSql		= this.getParameter("mainSql");
		AssertUtils.notEmpty(countSql, "countSql can not be null.");
		AssertUtils.notEmpty(mainSql, "mainSql can not be null.");
		Map	condition		= this.getParameterMap();
		Long count 			= this.getGenericDAO().getCount(countSql, condition);
		PageBean pageBean 	= new PageBean();
		pageBean.setTotalProperty(count);
		if (count > 0) {
			List resultList	= this.getGenericDAO().queryList(mainSql, condition);
			pageBean.setResultList(resultList);
		}
		HttpServletUtils.outJson(this.getResponse(), pageBean);
		return null;
	}
	
	/**
	 * 
	 * @author：Wuqingming 	        
	 * @date：2011-6-1
	 * @Description：支持命名sql的结果集查询
	 * @return
	 */
	public String queryList () {
		String mainSql		= this.getParameter("mainSql");
		AssertUtils.notEmpty(mainSql, "mainSql can not be null.");
		Map	condition		= this.getParameterMap();
		List resultList		= this.getGenericDAO().queryList(mainSql, condition);
		HttpServletUtils.outJsonArray(this.getResponse(), resultList);
		return null;
	}
	
	/**
	 * 
	 * @author：Wuqingming 	        
	 * @date：2011-6-1
	 * @Description：加载某对象
	 * @return
	 */
	public String loadObject () {
		String mainSql		= this.getParameter("mainSql");
		AssertUtils.notEmpty(mainSql, "mainSql can not be null.");
		Map	condition		= this.getParameterMap();
		Object result		= this.getGenericDAO().load(mainSql, condition);
		HttpServletUtils.outJson(this.getResponse(), result);
		return null;
	}
}

