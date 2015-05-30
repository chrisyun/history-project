package com.talkweb.security.jcaptcha;

import com.octo.captcha.service.CaptchaService;
import com.talkweb.ncframework.pub.common.ProccessResultBean;
import com.talkweb.ncframework.pub.web.common.utils.HttpServletUtils;
import com.talkweb.ncframework.pub.web.struts2.BaseAction;

/**
 * <p>文件名称: JCaptchaAction.java</p>
 * <p>文件描述: 图片验证Action</p>
 * <p>版权所有: 版权所有(C)2010</p>
 * <p>公　　司: 拓维信息系统股份有限公司</p>
 * <p>内容摘要: 简要描述本文件的内容，包括主要模块、函数及能的说明</p>
 * <p>其他说明: 其它内容的说明</p>
 * <p>完成日期: 2011-5-25</p>
 * <p>修改记录1:</p>
 * <pre>
 *    修改日期:
 *    修 改 人:
 *    修改内容:
 * </pre>
 * <p>修改记录2：…</p>
 * @author  Wuqingming
 */
public class JCaptchaAction extends BaseAction {
	private CaptchaService captchaService;
	public static final String DEFAULT_CAPTCHA_PARAMTER_NAME = "validateCode";

	public CaptchaService getCaptchaService() {
		return captchaService;
	}

	public void setCaptchaService(CaptchaService captchaService) {
		this.captchaService = captchaService;
	}


	/**
	 * 
	 * @author：Wuqingming 	        
	 * @date：2011-5-25
	 * @Description：验证图片验证码
	 * @return
	 */
	public String validImageCode () {
		ProccessResultBean resultBean = null;
		boolean flag = false;   
        try {   
            String captchaID = this.getSession().getId();   
            String captchaValue = this.getParameter(DEFAULT_CAPTCHA_PARAMTER_NAME);   
            flag = captchaService.validateResponseForID(captchaID, captchaValue);   
        } catch (Exception e) {   
            flag = false;
        }
        String msg = flag ? "验证码输入正确." : "验证码输入错误.";
        HttpServletUtils.outJson(this.getResponse(), resultBean);
		return null;
	}
	
}

