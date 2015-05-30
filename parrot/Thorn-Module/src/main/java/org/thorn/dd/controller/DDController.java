package org.thorn.dd.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thorn.core.util.LocalStringUtils;
import org.thorn.dao.core.Configuration;
import org.thorn.web.entity.Page;
import org.thorn.dao.exception.DBAccessException;
import org.thorn.dd.entity.Dict;
import org.thorn.dd.entity.DictType;
import org.thorn.dd.service.IDataDictService;
import org.thorn.web.controller.BaseController;
import org.thorn.web.entity.Status;

/**
 * @ClassName: DDController
 * @Description:
 * @author chenyun
 * @date 2012-5-7 上午11:45:31
 */
@Controller
@RequestMapping("/dd")
public class DDController extends BaseController {
	
	static Logger log = LoggerFactory.getLogger(DDController.class);
	
	@Autowired
	@Qualifier("ddService")
	private IDataDictService ddService;
	
	/**
	 * 
	 * @Description：分页获取字典类型
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午10:04:07
	 * @param start	开始页（0开始）
	 * @param limit	页数
	 * @param sort  排序字段
	 * @param dir	降序/升序
	 * @param ename 类型英文名
	 * @param cname 类型中文名
	 * @return
	 */
	@RequestMapping("/getDtPage")
	@ResponseBody
	public Page<DictType> getDtPage(long start, long limit, String sort,
			String dir, String ename, String cname) {

		Page<DictType> page = new Page<DictType>();
		try {
			page = ddService.queryDtPage(ename, cname,
					start, limit, sort, dir);
		} catch (DBAccessException e) {
			log.error("getDtPage[DD] - " + e.getMessage(), e);
		}

		return page;
	}
	
	/**
	 * 
	 * @Description：根据类型ID获取所有的字典数据项
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午10:05:53
	 * @param typeId 类型ID
	 * @return
	 */
	@RequestMapping("/getDdList")
	@ResponseBody
	public Page<Dict> getDdList(String typeId) {
		List<Dict> list = new ArrayList<Dict>();
		try {
			list = ddService.queryDdList(typeId);
		} catch (DBAccessException e) {
			log.error("getDdList[DD] - " + e.getMessage(), e);
		}
		
		Page<Dict> page = new Page<Dict>();
		page.setReslutSet(list);
		page.setTotal(list.size());
		
		return page;
	}
	
	/**
	 * 
	 * @Description：新增或修改字典数据项
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午10:06:28
	 * @param dd	   新增或修改的对象
	 * @param opType 操作类型
	 * @return
	 */
	@RequestMapping("/saveOrModifyDd")
	@ResponseBody
	public Status saveOrModifyDd(Dict dd, String opType) {
		Status status = new Status();
		
		try {
			
			if(LocalStringUtils.equals(opType, Configuration.OP_SAVE)) {
				ddService.saveDd(dd);
				status.setMessage("新增字典项成功！");
			} else if(LocalStringUtils.equals(opType, Configuration.OP_MODIFY)) {
				ddService.modifyDd(dd);
				status.setMessage("修改字典项成功！");
			}
			
		} catch (DBAccessException e) {
			status.setSuccess(false);
			status.setMessage("数据保存失败：" + e.getMessage());
			log.error("saveOrModifyDd[DD] - " + e.getMessage(), e);
		}
		
		return status;
	}
	
	/**
	 * 
	 * @Description：新增或修改字典类型
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午10:07:28
	 * @param dt	   新增或修改的对象
	 * @param opType 操作类型
	 * @return
	 */
	@RequestMapping("/saveOrModifyDt")
	@ResponseBody
	public Status saveOrModifyDt(DictType dt, String opType) {
		Status status = new Status();
		
		try {
			
			if(LocalStringUtils.equals(opType, Configuration.OP_SAVE)) {
				ddService.saveDt(dt);
				status.setMessage("新增字典类型成功！");
			} else if(LocalStringUtils.equals(opType, Configuration.OP_MODIFY)) {
				ddService.modifyDt(dt);
				status.setMessage("修改字典类型成功！");
			}
			
		} catch (DBAccessException e) {
			status.setSuccess(false);
			status.setMessage("数据保存失败：" + e.getMessage());
			log.error("saveOrModifyDt[DD] - " + e.getMessage(), e);
		}
		
		return status;
	}
	
	/**
	 * 
	 * @Description：根据主键批量删除字典数据项
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午10:08:09
	 * @param ids	主键字符串，格式id1,id2,
	 * @param typeId
	 * @return
	 */
	@RequestMapping("/deleteDd")
	@ResponseBody
	public Status deleteDd(String ids, String typeId) {
		Status status = new Status();
		
		try {
			ddService.deleteDd(ids, typeId);
			status.setMessage("数据删除成功！");
		} catch (DBAccessException e) {
			status.setSuccess(false);
			status.setMessage("数据删除失败：" + e.getMessage());
			log.error("deleteDd[DD] - " + e.getMessage(), e);
		}
		
		return status;
	}
	
	/**
	 * 
	 * @Description：根据主键批量删除字典类型项
	 * @author：chenyun 	        
	 * @date：2012-5-25 上午10:09:06
	 * @param ids	主键字符串，格式id1,id2,
	 * @return
	 */
	@RequestMapping("/deleteDt")
	@ResponseBody
	public Status deleteDt(String ids) {
		Status status = new Status();

		try {
			ddService.deleteDt(ids);
			status.setMessage("数据删除成功！");
		} catch (DBAccessException e) {
			status.setSuccess(false);
			status.setMessage("数据删除失败：" + e.getMessage());
			log.error("deleteDt[DD] - " + e.getMessage(), e);
		}
		
		return status;
	}

}
