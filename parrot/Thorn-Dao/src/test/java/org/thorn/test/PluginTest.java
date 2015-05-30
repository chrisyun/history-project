package org.thorn.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.thorn.core.context.SpringContext;

import junit.framework.TestCase;

/**
 * <p>文件名称: PluginTest.java</p>
 * <p>文件描述: 本类描述</p>
 * <p>版权所有: 版权所有(C)2010</p>
 * <p>内容摘要: 简要描述本文件的内容，包括主要模块、函数及能的说明</p>
 * <p>其他说明: 其它内容的说明</p>
 * <p>完成日期: 2012-5-1</p>
 * <p>修改记录1:</p>
 * <pre>
 *    修改日期:
 *    修 改 人:
 *    修改内容:
 * </pre>
 * <p>修改记录2：…</p>
 * @author  chenyun
 */
public class PluginTest extends TestCase {
	
	private SqlSessionTemplate template;
	
	public void setUp() {
		
		SpringContext context = new SpringContext();
		
		context.setApplicationContext("Thorn-Spring.xml");
		
		template = SpringContext.getBean("sqlSessionTemplate");
	}
	
	public void testSelectHandler() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("id", 21);
		map.put("name", "cody");
		
		template.selectList("TestMapper.select",map);
		
	}
	
	public void testInsertHandler() {
		
		Test test  = new Test();
		test.setName("chenyun");
		test.setCode("123456");
		
		template.insert("TestMapper.insert", test);
	}
	
	public void testDeleteHandler() {
		
		List<String> a = new ArrayList<String>();
		a.add("fff");
		
		template.delete("TestMapper.delete", a);
	}
	
}

