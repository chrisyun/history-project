package org.thorn.web.util;

import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.util.StringUtils;

/** 
 * @ClassName: CustomDefaultNumberEditor 
 * @Description: 
 * @author chenyun
 * @date 2012-5-15 下午02:58:01 
 */
public class CustomDefaultNumberEditor extends CustomNumberEditor {
	
	private boolean allowEmpty;
	
	public CustomDefaultNumberEditor(Class numberClass, boolean allowEmpty)
			throws IllegalArgumentException {
		super(numberClass, allowEmpty);
		this.allowEmpty = allowEmpty;
	}
	
	
	public void setAsText(String text) throws IllegalArgumentException {
		if (this.allowEmpty && !StringUtils.hasText(text)) {
			super.setAsText("999");
		} else {
			super.setAsText(text);
		}
	}
}

