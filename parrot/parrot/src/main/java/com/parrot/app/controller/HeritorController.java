package com.parrot.app.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.parrot.app.AppConfiguration;
import com.parrot.app.entity.CostSummary;
import com.parrot.app.entity.Heritor;
import com.parrot.app.entity.ProjectCost;
import com.parrot.app.service.IHeritorService;
import com.parrot.app.service.IProjectService;
import com.parrot.app.service.IReseverService;
import org.thorn.core.excel.ArrayAdapter;
import org.thorn.core.excel.ExcelStyle;
import org.thorn.core.excel.ExcelUtils;
import org.thorn.core.util.LocalStringUtils;
import org.thorn.core.util.ReflectUtils;
import org.thorn.dao.core.Configuration;
import org.thorn.dao.exception.DBAccessException;
import org.thorn.security.SecurityUserUtils;
import org.thorn.user.entity.User;
import org.thorn.web.controller.BaseController;
import org.thorn.web.entity.Page;
import org.thorn.web.entity.Status;
import org.thorn.web.util.DDUtils;
import org.thorn.web.util.ResponseHeaderUtils;

/**
 * @ClassName: ProjectController
 * @Description:
 * @author chenyun
 * @date 2012-8-10 上午11:10:06
 */
@Controller
@RequestMapping("/heritor")
public class HeritorController extends BaseController {

	static Logger log = LoggerFactory.getLogger(HeritorController.class);

	@Autowired
	@Qualifier("heritorService")
	private IHeritorService heritorService;

	@Autowired
	@Qualifier("projectService")
	private IProjectService projectService;

	@Autowired
	@Qualifier("reseverService")
	private IReseverService reseverService;

	@RequestMapping("/saveOrModifyHeritor")
	@ResponseBody
	public Status saveOrModifyHeritor(Heritor heritor, String opType) {
		Status status = new Status();

		try {

			if (LocalStringUtils.equals(opType, Configuration.OP_SAVE)) {
				heritorService.save(heritor);
				status.setMessage("新增传承人成功！");
			} else if (LocalStringUtils.equals(opType, Configuration.OP_MODIFY)) {
				heritorService.modify(heritor);
				status.setMessage("修改传承人成功！");
			}

		} catch (DBAccessException e) {
			status.setSuccess(false);
			status.setMessage("数据保存失败：" + e.getMessage());
			log.error("saveOrModifyHeritor[Heritor] - " + e.getMessage(), e);
		}

		return status;
	}

	@RequestMapping("/deleteHeritor")
	@ResponseBody
	public Status deleteHeritor(String ids) {
		Status status = new Status();

		try {
			heritorService.delete(ids);
			status.setMessage("数据删除成功！");
		} catch (DBAccessException e) {
			status.setSuccess(false);
			status.setMessage("数据删除失败：" + e.getMessage());
			log.error("deleteHeritor[Heritor] - " + e.getMessage(), e);
		}

		return status;
	}

	@RequestMapping("/getHeritorPage")
	@ResponseBody
	public Page<Heritor> getHeritorPage(long start, long limit, String sort,
			String dir, String name, String gender, String minority, String projectName,
			String isDie, String province, String batchNum) {
		Page<Heritor> page = new Page<Heritor>();

		try {
			User user = SecurityUserUtils.getCurrentUser();
			List<String> roleList = SecurityUserUtils.getRoleList();

			if (!SecurityUserUtils.isSysAdmin()
					&& !roleList.contains(AppConfiguration.ROLE_CENTRAL)) {

				province = user.getArea();
			}

			page = heritorService.queryPage(name, gender, minority, projectName, isDie,
					province, batchNum, start, limit, sort, dir);
		} catch (DBAccessException e) {
			log.error("getHeritorPage[Heritor] - " + e.getMessage(), e);
		}

		return page;
	}

	@RequestMapping("/getHeritorList")
	@ResponseBody
	public Page<Heritor> getHeritorList(String sort, String dir, String name,
			String province, String isDie, Integer projectId) {
		Page<Heritor> page = new Page<Heritor>();

		try {
			User user = SecurityUserUtils.getCurrentUser();
			List<String> roleList = SecurityUserUtils.getRoleList();

			if (!SecurityUserUtils.isSysAdmin()
					&& !roleList.contains(AppConfiguration.ROLE_CENTRAL)) {

				province = user.getArea();
			}

//			if (StringUtils.isBlank(isDie)) {
//				isDie = Configuration.DB_NO;
//			}

			page.setReslutSet(heritorService.queryList(name, province, isDie,
					projectId, sort, dir));
		} catch (DBAccessException e) {
			log.error("getHeritorList[Heritor] - " + e.getMessage(), e);
		}

		return page;
	}

	@RequestMapping("/bingingProject")
	@ResponseBody
	public Status bingingProject(Integer projectId, String ids) {
		Status status = new Status();

		try {
			heritorService.modifyProject(projectId, ids);
			status.setMessage("设置非遗项目传承人成功！");
		} catch (DBAccessException e) {
			status.setSuccess(false);
			status.setMessage("设置非遗项目传承人失败：" + e.getMessage());
			log.error("bingingProject[Heritor] - " + e.getMessage(), e);
		}

		return status;
	}

	@RequestMapping("/summaryAllCost")
	@ResponseBody
	public Page<CostSummary> summaryAllCost(Integer year, String province,
			Integer heritorMoney) {

		Page<CostSummary> page = new Page<CostSummary>();

		try {
			User user = SecurityUserUtils.getCurrentUser();
			List<String> roleList = SecurityUserUtils.getRoleList();

			if (!SecurityUserUtils.isSysAdmin()
					&& !roleList.contains(AppConfiguration.ROLE_CENTRAL)) {

				province = user.getArea();
			}

			if (year == null) {
				SimpleDateFormat df = new SimpleDateFormat("yyyy");
				String fullYear = df.format(new Date());

				year = Integer.parseInt(fullYear);
			}

			if (heritorMoney == null) {
				heritorMoney = 1;
			}

			CostSummary hMap = new CostSummary();
			long hMoney = heritorService.queryHeritorCount(province,
					Configuration.DB_NO) * heritorMoney;
			hMap.setName("国家级代表性传承人补助费");
			hMap.setMoney(String.valueOf(hMoney));

			CostSummary pMap = new CostSummary();
			Double pMoney = projectService.queryProjectCostSum(year, province);
			pMap.setName("国家级非物质文化遗产代表性项目补助费");
			pMap.setMoney(String.valueOf(pMoney));

			CostSummary rMap = new CostSummary();
			Double rMoney = reseverService.queryReseverCostSum(year, province);
			rMap.setName("国家级文化生态保护区补助费");
			rMap.setMoney(String.valueOf(rMoney));

			page.getReslutSet().add(pMap);
			page.getReslutSet().add(hMap);
			page.getReslutSet().add(rMap);
		} catch (DBAccessException e) {
			log.error("summaryAllCost[Double] - " + e.getMessage(), e);
		}

		return page;
	}

	@RequestMapping("/exportAllSummaryExcel")
	public void exportAllSummaryExcel(Integer year, String province,
			Integer heritorMoney, HttpServletResponse response) {
		User user = SecurityUserUtils.getCurrentUser();
		List<String> roleList = SecurityUserUtils.getRoleList();

		if (!SecurityUserUtils.isSysAdmin()
				&& !roleList.contains(AppConfiguration.ROLE_CENTRAL)) {

			province = user.getArea();
		}

		if (year == null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy");
			String fullYear = df.format(new Date());

			year = Integer.parseInt(fullYear);
		}

		if (heritorMoney == null) {
			heritorMoney = 1;
		}

		try {
			String provinceName = DDUtils.queryDdById("AREA", province);

			StringBuilder title = new StringBuilder(String.valueOf(year));
			title.append("年度").append(provinceName);
			title.append("国家非物质文化遗产保护专项资金申报汇总表");

			ResponseHeaderUtils.setExcelResponse(response, title.toString());

			String[] header = new String[] { "序号", "项目名称", "金额（万元）" };
			int[] columnWidth = new int[] { 100, 300, 100 };

			double money = 0;

			CostSummary hMap = new CostSummary();
			long hMoney = heritorService.queryHeritorCount(province,
					Configuration.DB_NO) * heritorMoney;
			hMap.setName("国家级代表性传承人补助费");
			hMap.setMoney(String.valueOf(hMoney));
			hMap.setNum("2");

			money += hMoney;

			CostSummary pMap = new CostSummary();
			Double pMoney = projectService.queryProjectCostSum(year, province);
			pMap.setName("国家级非物质文化遗产代表性项目补助费");
			pMap.setMoney(String.valueOf(pMoney));
			pMap.setNum("1");

			money += pMoney;

			CostSummary rMap = new CostSummary();
			Double rMoney = reseverService.queryReseverCostSum(year, province);
			rMap.setName("国家级文化生态保护区补助费");
			rMap.setMoney(String.valueOf(rMoney));
			rMap.setNum("3");

			money += rMoney;

			CostSummary total = new CostSummary();
			total.setName("-");
			total.setMoney(String.valueOf(money));
			total.setNum("合计");

			List<CostSummary> list = new ArrayList<CostSummary>();
			list.add(pMap);
			list.add(hMap);
			list.add(rMap);
			list.add(total);

			String[] orderArray = new String[] { "num", "name", "money" };
			List<Object[]> dataSource = ReflectUtils.object2Array(list);

			ExcelStyle style = new ExcelStyle();
			
			List<Object[]> titles = new ArrayList<Object[]>();
			titles.add(new Object[]{title.toString()});
			
			List<Integer[]> merCells = new ArrayList<Integer[]>();
			merCells.add(new Integer[]{0,0,0,2});
			
			ArrayAdapter adapter = new ArrayAdapter(header, orderArray,
					dataSource, titles, merCells);

			ExcelUtils.write2Excel(adapter, "汇总表", columnWidth, style,
					response.getOutputStream());
			response.getOutputStream().flush();

		} catch (DBAccessException e) {
			log.error("exportAllSummaryExcel[Double] - " + e.getMessage(), e);
		} catch (IOException e) {
			log.error("exportAllSummaryExcel[Double] - " + e.getMessage(), e);
		}
	}

	@RequestMapping("/exportHeritorCostExcel")
	public void exportHeritorCostExcel(Integer heritorMoney, String province,
			HttpServletResponse response) {
		try {
			User user = SecurityUserUtils.getCurrentUser();
			List<String> roleList = SecurityUserUtils.getRoleList();

			if (!SecurityUserUtils.isSysAdmin()
					&& !roleList.contains(AppConfiguration.ROLE_CENTRAL)) {

				province = user.getArea();
			}

			String provinceName = DDUtils.queryDdById("AREA", province);

			List<Heritor> listLive = heritorService.queryList(null, province,
					Configuration.DB_NO, null, null, null);

			List<Heritor> listDie = heritorService.queryList(null, province,
					Configuration.DB_YES, null, null, null);

			StringBuilder title = new StringBuilder(provinceName);
			title.append("国家级代表性传承人补助费申报书");

			ResponseHeaderUtils.setExcelResponse(response, title.toString());

			String[] header = new String[] { "序号", "姓名", "身份证号", "项目编号",
					"项目名称", "金额（万元）" };
			int[] columnWidth = new int[] { 100, 150, 300, 100, 300, 100 };

			int i = 1;
			for (Heritor her : listLive) {
				her.setBatchNum(String.valueOf(i));
				her.setDieDate(String.valueOf(heritorMoney));
				i++;
			}

			Heritor her = new Heritor();
			her.setBatchNum("合计");
			her.setName("-");
			her.setIdCard("-");
			her.setProjectCode("-");
			her.setProjectName("-");
			her.setDieDate(String.valueOf(heritorMoney * listLive.size()));
			listLive.add(her);

			her = new Heritor();
			her.setIdCard("已去世代表性传承人名单");
			listLive.add(her);

			her = new Heritor();
			her.setBatchNum("序号");
			her.setName("姓名");
			her.setIdCard("身份证号");
			her.setProjectCode("项目编号");
			her.setProjectName("项目名称");
			her.setDieDate("去世时间");
			listLive.add(her);

			i = 1;
			for (Heritor dher : listDie) {
				dher.setBatchNum(String.valueOf(i));
				i++;
			}
			listLive.addAll(listDie);

			String[] orderArray = new String[] { "batchNum", "name", "idCard",
					"projectCode", "projectName", "dieDate" };
			List<Object[]> dataSource = ReflectUtils.object2Array(listLive);

			ExcelStyle style = new ExcelStyle();
			
			List<Object[]> titles = new ArrayList<Object[]>();
			titles.add(new Object[]{title.toString()});
			
			List<Integer[]> merCells = new ArrayList<Integer[]>();
			merCells.add(new Integer[]{0,0,0,5});
			
			ArrayAdapter adapter = new ArrayAdapter(header, orderArray,
					dataSource, titles, merCells);

			ExcelUtils.write2Excel(adapter, "汇总表", columnWidth, style,
					response.getOutputStream());
			response.getOutputStream().flush();
		} catch (DBAccessException e) {
			log.error("exportProjectCostExcel[Heritor] - " + e.getMessage(), e);
		} catch (IOException e) {
			log.error("exportHeritorCostExcel[Heritor] - " + e.getMessage(), e);
		}
	}

}
