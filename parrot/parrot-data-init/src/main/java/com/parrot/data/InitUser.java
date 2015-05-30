package com.parrot.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thorn.core.context.SpringContext;
import org.thorn.dao.exception.DBAccessException;
import org.thorn.security.SecurityConfiguration;
import org.thorn.user.entity.User;
import org.thorn.user.service.IUserService;

/** 
 * @ClassName: InitUser 
 * @Description: 
 * @author chenyun
 * @date 2012-9-8 下午03:30:01 
 */
public class InitUser {
	
	static Logger log = LoggerFactory.getLogger(InitUser.class);
	
	public void start() {
		
		File file = new File(Configuration.excel);
		
		IUserService service = SpringContext.getBean("userService");
		
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));
			
			//获取第一个sheet
			HSSFSheet sheet = workbook.getSheetAt(0);
			
			for (int i = 0; i <= sheet.getLastRowNum(); i++) {
				
				HSSFRow row = sheet.getRow(i);
				
				if(row == null || row.getCell(0) == null) {
					continue;
				}
				
				String[] context = Configuration.readRow(row);
				String name = context[0];
				if (StringUtils.isEmpty(name)) {
					continue;
				}
				
				try {
					User dbUser = service.queryUserByLogin(name);
					if(dbUser != null && dbUser.getUserId() != null) {
						continue;
					}
				} catch (DBAccessException e) {
					log.error("find user : " + name + " error");
				}
				
				User user = new User();
				user.setDefaultRole(SecurityConfiguration.COMMON_USER_ROLE);
				user.setUserName(name);
				user.setUserPwd("111www");
				user.setIsDisabled(org.thorn.dao.core.Configuration.DB_NO);
				user.setIsShow(org.thorn.dao.core.Configuration.DB_YES);
				user.setUserAccount(name);
				user.setOrgCode(Configuration.getOrgCode(context[1]));
				user.setSortNum(0);
				
				try {
					service.save(user);
				} catch (DBAccessException e) {
					log.error("insert user : " + name + " error");
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
	
	public void updateUsrPwd() {
		
		IUserService service = SpringContext.getBean("userService");
		try {
			List<User> list = service.queryList(null, null, null, null, null, null, null);
			
			System.out.println("----------" + list.size() + "-------------------------");
			
			for(User user : list) {
				
				if(StringUtils.equals("ADMIN", user.getUserId())) {
					continue;
				}
				
				user.setUserPwd("111www");
				
				try {
					service.changePwd(user.getUserId(), "111www");
				} catch (DBAccessException e) {
					log.error("insert user : " + user.getUserId() + " error");
				}
			}
		} catch (DBAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

