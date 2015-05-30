package org.thorn.web.tag;

import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.thorn.core.util.LocalStringUtils;
import org.thorn.dd.entity.Dict;
import org.thorn.web.util.DDUtils;

/**
 * @ClassName: DDTag
 * @Description: 数据字典的标签解析类
 * @author chenyun
 * @date 2012-5-8 下午04:07:45
 */
public class DDTag extends TagSupport {

	/** */
	private static final long serialVersionUID = -4345583586016970344L;

	private String typeId;

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public int doStartTag() throws JspException {

		try {
			JspWriter out = this.pageContext.getOut();

			if (LocalStringUtils.isEmpty(typeId)) {
				out.print("the typeId is empty");
			} else {
				StringBuffer dictJson = new StringBuffer("[");
				List<Dict> list = DDUtils.queryDd(typeId);

				for (Dict dict : list) {
					dictJson.append("['").append(dict.getDname()).append("','")
							.append(dict.getDvalue()).append("'],");
				}

				String json = dictJson.toString();
				if (json.endsWith(",")) {
					json = json.substring(0, json.length() - 1);
				}
				json += "]";

				out.print(json);
			}

		} catch (Exception e) {
			throw new JspException("DDTag Resolving Tag Exception,typeId:"
					+ typeId, e);
		}

		return super.doStartTag();
	}

	public void release() {
		super.release();
		this.typeId = null;
	}
}
