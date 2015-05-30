package com.parrot.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thorn.core.context.SpringContext;
import org.thorn.dao.exception.DBAccessException;
import org.thorn.user.entity.User;
import org.thorn.user.service.IUserService;

import com.parrot.app.entity.Heritor;
import com.parrot.app.entity.Project;
import com.parrot.app.service.IHeritorService;
import com.parrot.app.service.IProjectService;

/** 
 * @ClassName: InitHeritor 
 * @Description: 
 * @author chenyun
 * @date 2012-9-8 下午10:16:41 
 */
public class InitHeritor {
	
	static Logger log = LoggerFactory.getLogger(InitHeritor.class);
	
	public void start() {
		
		File file = new File(Configuration.excel);
		
		IHeritorService hService = SpringContext.getBean("heritorService");
		IProjectService pService = SpringContext.getBean("projectService");
		
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));
			
			//获取第三个sheet
			HSSFSheet sheet = workbook.getSheetAt(2);
			
			for (int i = 0; i <= sheet.getLastRowNum(); i++) {
				
				HSSFRow row = sheet.getRow(i);
				
				if(row == null || row.getCell(0) == null) {
					continue;
				}
				
				String[] context = Configuration.readRow(row);
				
				if(context == null || context.length < 2) {
					continue;
				}
				
				String hName = context[0];
				if (StringUtils.isEmpty(hName)) {
					continue;
				}
				
				try {
					
					hName = hName.replaceAll(" ", "");
					
					String sex = "";
					if(StringUtils.isNotBlank(context[1])) {
						sex = context[1].replaceAll(" ", "");
						if(StringUtils.equals("男", sex)) {
							sex = "male";
						} else if(StringUtils.equals("女", sex)) {
							sex = "femme";
						}
					}
					
					String remark = StringUtils.defaultString(context[4]);
					
					//找到项目保护单位
					Integer projectId = null;
					String province = "";
					if(context.length == 6 && StringUtils.isNotBlank(context[5])) {
						try {
							Project project = pService.queryProject(context[5], null);
							if(project != null && project.getId() != null) {
								projectId = project.getId();
								province = project.getProvince();
							} else {
								remark += " - " + context[5];
							}
						} catch (DBAccessException e) {
						}
					}
					double bi = Double.parseDouble(context[2]);
					
					Heritor heritor = new Heritor();
					heritor.setBatchNum(String.valueOf((int)bi));
					heritor.setName(hName);
					heritor.setGender(sex);
					heritor.setIsDie(org.thorn.dao.core.Configuration.DB_NO);
					heritor.setRemark(remark);
					heritor.setProjectId(projectId);
					heritor.setProvince(province);
					heritor.setIdCard(context[3]);
					
					try {
						hService.save(heritor);
					} catch (DBAccessException e) {
//						e.printStackTrace();
						log.error("insert heritor : " + hName + " error");
					}
				} catch (Exception e) {
					log.error("insert heritor : " + hName + " error");
				}
				
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

