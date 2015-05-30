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

import com.parrot.app.entity.Project;
import com.parrot.app.entity.ProjectFund;
import com.parrot.app.service.IProjectService;

public class InitProjectFund {
	
	static Logger log = LoggerFactory.getLogger(InitProjectFund.class);
	
	public void start() {
		File file = new File("d:/pFund.xls");
		
		IProjectService pService = SpringContext.getBean("projectService");
		
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));
			
			HSSFSheet sheet = workbook.getSheetAt(0);
			
			for (int i = 0; i <= sheet.getLastRowNum(); i++) {
				
				HSSFRow row = sheet.getRow(i);
				
				if(row == null || row.getCell(0) == null) {
					continue;
				}
				
				String[] context = Configuration.readRow(row);
				
				if(context == null || context.length < 3) {
					continue;
				}
				
				String projectName = context[0];
				if (StringUtils.isEmpty(projectName)) {
					continue;
				}
				
				projectName = projectName.replaceAll("★", "").replaceAll(" ", "");
				
				String area = context[1].replaceAll(" ", "");
				
				try {
					
					Project project = pService.queryProject(projectName, area);
					
					if(project == null) {
						log.error("project["+projectName+"] area[" +area+ "] is not found!");
						continue;
					}
					
					ProjectFund pFund = new ProjectFund();
					pFund.setPid(project.getId());
					pFund.setYear(Integer.parseInt(context[2].substring(0, 4)));
					pFund.setFund(Double.parseDouble(context[3]));
					
					pService.save(pFund);
				} catch (Exception e) {
					e.printStackTrace();
					log.error("insert project["+projectName+"] area[" +area+ "] error!");
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
