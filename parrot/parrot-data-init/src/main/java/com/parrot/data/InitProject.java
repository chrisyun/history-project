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

import com.parrot.app.entity.Project;
import com.parrot.app.service.IProjectService;

/** 
 * @ClassName: InitProject 
 * @Description: 
 * @author chenyun
 * @date 2012-9-8 下午09:20:25 
 */
public class InitProject {
	
	static Logger log = LoggerFactory.getLogger(InitProject.class);
	
	public void start() {
		
		File file = new File(Configuration.excel);
		
		IUserService service = SpringContext.getBean("userService");
		IProjectService pService = SpringContext.getBean("projectService");
		
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));
			
			//获取第二个sheet
			HSSFSheet sheet = workbook.getSheetAt(1);
			
			for (int i = 0; i <= sheet.getLastRowNum(); i++) {
				
				HSSFRow row = sheet.getRow(i);
				
				if(row == null || row.getCell(0) == null) {
					continue;
				}
				
				String[] context = Configuration.readRow(row);
				
				if(context == null || context.length < 3) {
					continue;
				}
				
				String projectName = context[3];
				if (StringUtils.isEmpty(projectName)) {
					continue;
				}
				
				try {
					String isUn = org.thorn.dao.core.Configuration.DB_NO;
					if(projectName.indexOf("★") > -1) {
						isUn = org.thorn.dao.core.Configuration.DB_YES;
						projectName = projectName.replaceAll("★", "");
					}
					
					String type = Configuration.getProjectTypeCode(context[1]);
					
					//找到项目保护单位
					String user = "";
					String province = "";
					if(context.length == 8 && context[7] != null && StringUtils.isNotBlank(context[7])) {
						try {
							User dbUser = service.queryUserByLogin(context[7]);
							if(dbUser != null && dbUser.getUserId() != null) {
								user = dbUser.getUserId();
								province = dbUser.getArea();
							} else {
								user = context[7];
							}
						} catch (DBAccessException e) {
						}
					}
					
					Project project = new Project();
					project.setCode(context[2]);
					double bn = Double.parseDouble(context[0]);
					project.setBigNo((int)bn);
					project.setType(type);
					bn = Double.parseDouble(context[4]);
					project.setSmallNo((int)bn);
					project.setName(projectName);
					project.setIsUnProject(isUn);
					String batchNum = context[5];
					if(StringUtils.isNotBlank(batchNum)) {
						batchNum = batchNum.replaceAll(" ", "");
					}
					project.setBatchNum(batchNum);
					
					if(context.length > 6 && context[6] != null) {
						project.setArea(context[6]);
					}
					project.setProvince(province);
					project.setUserId(user);
					
					
					try {
						pService.save(project);
					} catch (DBAccessException e) {
//						e.printStackTrace();
						log.error("insert project : " + projectName + " error");
					}
				} catch (Exception e) {
					log.error("insert project : " + projectName + " error");
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

