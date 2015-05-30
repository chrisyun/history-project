package org.thorn.log.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thorn.core.excel.ArrayAdapter;
import org.thorn.core.excel.ExcelStyle;
import org.thorn.core.excel.ExcelUtils;
import org.thorn.core.util.ReflectUtils;
import org.thorn.web.entity.Page;
import org.thorn.dao.exception.DBAccessException;
import org.thorn.log.entity.AppLog;
import org.thorn.log.service.IAppLogService;
import org.thorn.web.controller.BaseController;
import org.thorn.web.util.ResponseHeaderUtils;

/**
 * @ClassName: AppLogController
 * @Description:
 * @author chenyun
 * @date 2012-5-26 下午10:00:25
 */
@Controller
@RequestMapping("/log")
public class AppLogController extends BaseController {

	static Logger log = LoggerFactory.getLogger(AppLogController.class);

	@Autowired
	@Qualifier("logService")
	private IAppLogService logService;

	@RequestMapping("/getLogPage")
	@ResponseBody
	public Page<AppLog> getLogPage(long start, long limit, String sort,
			String dir, String moduleName, String handleResult,
			String startTime, String endTime) {
		Page<AppLog> page = new Page<AppLog>();

		try {
			page = logService.queryPage(moduleName, handleResult, startTime,
					endTime, start, limit, sort, dir);
		} catch (DBAccessException e) {
			log.error("getLogPage[AppLog] - " + e.getMessage(), e);
		}

		return page;
	}

	@RequestMapping("/exportLogExcel")
	public void exportLogExcel(HttpServletResponse response, String moduleName,
			String handleResult, String startTime, String endTime) {
		List<AppLog> list = new ArrayList<AppLog>();

		try {
			list = logService.queryList(moduleName, handleResult, startTime,
					endTime);
			
			// 可对结果集进行数据字典转换
			
			ResponseHeaderUtils.setExcelResponse(response, "系统操作日志");
			
			String[] header = new String[] { "模块名称", "方法名称", "操作人", "操作时间",
					"操作结果", "错误信息" };
			int[] columnWidth = new int[]{200,250,150,150,100,500};
			
			String[] orderArray = new String[] { "moduleName", "methodName",
					"userId", "executeTime", "handleResult", "errorMsg" };
			List<Object[]> dataSource = ReflectUtils.object2Array(list);

			ExcelStyle style = new ExcelStyle();

			ArrayAdapter adapter = new ArrayAdapter(header, orderArray,
					dataSource);
			
			ExcelUtils.write2Excel(adapter, "APPLOG", columnWidth, style, response.getOutputStream());
			response.getOutputStream().flush();
		} catch (DBAccessException e) {
			log.error("exportLogExcel[AppLog] - " + e.getMessage(), e);
		} catch (IOException e) {
			log.error("exportLogExcel[AppLog] - " + e.getMessage(), e);
		}
	}

}
