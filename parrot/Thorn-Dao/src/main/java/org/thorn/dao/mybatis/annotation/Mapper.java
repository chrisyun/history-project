package org.thorn.dao.mybatis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
 * @ClassName: Mapper 
 * @Description:
 * @author chenyun
 * @date 2012-4-26 下午02:11:26 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Mapper {
	String nameSpace();
	
	MapperNode[] node();
}

