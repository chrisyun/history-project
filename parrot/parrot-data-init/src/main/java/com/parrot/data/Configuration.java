package com.parrot.data;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;

/** 
 * @ClassName: Configuration 
 * @Description: 
 * @author chenyun
 * @date 2012-9-8 下午03:34:19 
 */
public class Configuration {
	
	public static String excel = "D:\\非遗司初始化数据新建.xls";
	
	private static Map<String, String> orgCode = new HashMap<String, String>();
	
	private static Map<String, String> projectType = new HashMap<String, String>();
	
	static {
		orgCode.put("非遗司", "000");
		orgCode.put("北京", "001");
		orgCode.put("天津", "003");
		orgCode.put("河北", "008");
		orgCode.put("山西", "019");
		orgCode.put("内蒙古", "006");
		orgCode.put("辽宁", "026");
		orgCode.put("吉林", "012");
		orgCode.put("黑龙江", "010");
		orgCode.put("上海", "027");
		orgCode.put("江苏", "013");
		orgCode.put("浙江", "029");
		orgCode.put("安徽", "007");
		orgCode.put("福建", "022");
		orgCode.put("江西", "014");
		orgCode.put("山东", "005");
		orgCode.put("河南", "009");
		orgCode.put("湖北", "002");
		orgCode.put("湖南", "011");
		orgCode.put("广东", "023");
		orgCode.put("广西", "025");
		orgCode.put("重庆", "017");
		orgCode.put("四川", "018");
		orgCode.put("贵州", "016");
		orgCode.put("云南", "030");
		orgCode.put("西藏", "031");
		orgCode.put("陕西", "004");
		orgCode.put("青海", "020");
		orgCode.put("宁夏", "021");
		orgCode.put("新疆", "032");
		orgCode.put("新疆生产建设兵团", "028");
		orgCode.put("香港", "033");
		orgCode.put("澳门", "034");
		projectType.put("传统戏剧", "001");
		projectType.put("民间文学", "002");
		projectType.put("传统音乐", "003");
		projectType.put("传统舞蹈", "004");
		projectType.put("曲艺", "005");
		projectType.put("传统体育、游艺与杂技", "006");
		projectType.put("传统美术", "007");
		projectType.put("传统技艺", "008");
		projectType.put("传统医药", "009");
		projectType.put("民俗", "010");
	}
	
	public static String getOrgCode(String orgName) {
		return orgCode.get(orgName);
	}
	
	public static String getProjectTypeCode(String type) {
		return projectType.get(type);
	}
	
	public static String[] readRow(HSSFRow row) {
		String[] rawArray = new String[row.getLastCellNum()];

		for (int i = 0; i < rawArray.length; i++) {
			HSSFCell cell = row.getCell(i);
			
			if(cell == null) {
				rawArray[i] = "";
				continue;
			}
			
			if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC
					|| cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
				rawArray[i] = String.valueOf(cell.getNumericCellValue());
			} else {
				rawArray[i] = cell.getStringCellValue();
			}

		}

		return rawArray;
	}
	
}

