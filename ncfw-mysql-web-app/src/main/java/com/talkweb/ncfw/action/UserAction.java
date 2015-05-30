package com.talkweb.ncfw.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.jfree.util.Log;
import org.springframework.util.CollectionUtils;
import org.thorn.core.jmail.MailException;

import com.talkweb.ncframework.pub.common.PageBean;
import com.talkweb.ncframework.pub.common.ProccessResultBean;
import com.talkweb.ncframework.pub.dao.IGenericDAO;
import com.talkweb.ncframework.pub.exceptions.DAOException;
import com.talkweb.ncframework.pub.exceptions.ViewException;
import com.talkweb.ncframework.pub.utils.StringUtils;
import com.talkweb.ncframework.pub.utils.encode.EncoderUtils;
import com.talkweb.ncframework.pub.web.common.utils.HttpServletUtils;
import com.talkweb.ncframework.pub.web.struts2.BaseAction;
import com.talkweb.ncfw.entity.User;
import com.talkweb.ncfw.mail.MailTemplete;
import com.talkweb.security.SecurityHelper;
import com.talkweb.security.UserSession;

public class UserAction extends BaseAction {
	private static Logger logger = Logger.getLogger(UserAction.class);
	private User user;
	private IGenericDAO genericDAO;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public IGenericDAO getGenericDAO() {
		return genericDAO;
	}

	public void setGenericDAO(IGenericDAO genericDAO) {
		this.genericDAO = genericDAO;
	}

	/**
	 * 
	 * @author：Wuqingming
	 * @date：2011-5-7
	 * @Description：列出用户信息
	 * @return
	 */
	public String listUser() {
		Map condition = this.getParameterMap();
		PageBean<User> pageBean = new PageBean();

		String usertypeOuts = this.getParameter("usertypeOuts");
		String usertypeIns = this.getParameter("usertypeIns");
		if (StringUtils.isNotEmpty(usertypeOuts)) {
			condition.put("usertypeOutArray", usertypeOuts.split(","));
		}
		if (StringUtils.isNotEmpty(usertypeIns)) {
			condition.put("usertypeInArray", usertypeIns.split(","));
		}
		Long count = this.getGenericDAO().getCount("UserMapper.getCount",
				condition);
		pageBean.setTotalProperty(count);
		if (count > 0) {
			List<User> userList = this.getGenericDAO().queryList(
					"UserMapper.querySupportPagination", condition);
			pageBean.setResultList(userList);
		}
		HttpServletUtils.outJson(this.getResponse(), pageBean);
		return null;
	}

	/**
	 * 
	 * @author：Wuqingming
	 * @date：2011-5-5
	 * @Description：修改用户密码
	 * @return
	 */
	public String updateUserPwd() throws ViewException {
		String userid = this.getParameter("userid");
		String newpwd = this.getParameter("newpwd");
		ProccessResultBean resultBean = null;
		// userSession.
		if (StringUtils.isEmpty(userid)) {
			UserSession userSession = SecurityHelper.getCurrentUser();
			userid = userSession.getUser().getUserid();
		}
		User user = new User();
		user.setUserid(userid);
		try {
			user.setUserpwd(EncoderUtils.base64Encode(newpwd));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			this.getGenericDAO().update("UserMapper.update", user);
			resultBean = new ProccessResultBean(true, "密码修改成功.");
		} catch (DAOException e) {
			String exceptMsg = "修改用户密码时发生异常";
			logger.error(exceptMsg, e);
			resultBean = new ProccessResultBean(false, "密码修改异常.");
		}
		HttpServletUtils.outJson(this.getResponse(), resultBean);
		return null;
	}

	/**
	 * 
	 * @author：Wuqingming
	 * @date：2011-7-13
	 * @Description：启用用户
	 * @return
	 * @throws ViewException
	 */
	public String enableUser() throws ViewException {
		ProccessResultBean resultBean = null;
		String userids = this.getParameter("userids");

		String isSpflag = this.getParameter("isSpflag");

		if (StringUtils.isEmpty(userids)) {
			resultBean = new ProccessResultBean(false, "用户编号不能为空.");
			HttpServletUtils.outJson(this.getResponse(), resultBean);
			return null;
		}
		String[] useridArray = userids.split(",");
		Map para = new HashMap();
		para.put("useridArray", useridArray);
		para.put("status", "enable");
		try {
			this.getGenericDAO().update("UserMapper.updateUserStatus", para);
			/*用户启用不发邮件
			if (StringUtils.equals(isSpflag, "yes")) {
				String mails = this.getParameter("mails");
				this.sendMail(mails.split(","), "spSuccess");
			}*/

			resultBean = new ProccessResultBean(true, "用户启用成功.");
		} catch (DAOException e) {
			resultBean = new ProccessResultBean(false, "用户启用失败，启用用户时发生异常.");
		}
		HttpServletUtils.outJson(this.getResponse(), resultBean);
		return null;
	}

	/**
	 * 
	 * @author：Wuqingming
	 * @date：2011-7-13
	 * @Description：禁用用户
	 * @return
	 * @throws ViewException
	 */
	public String disableUser() throws ViewException {
		ProccessResultBean resultBean = null;
		String userids = this.getParameter("userids");
		if (StringUtils.isEmpty(userids)) {
			resultBean = new ProccessResultBean(false, "用户编号不能为空.");
			HttpServletUtils.outJson(this.getResponse(), resultBean);
			return null;
		}
		String[] useridArray = userids.split(",");
		Map para = new HashMap();
		para.put("useridArray", useridArray);
		para.put("status", "disable");
		try {
			this.getGenericDAO().update("UserMapper.updateUserStatus", para);
			resultBean = new ProccessResultBean(true, "用户禁用成功.");
		} catch (DAOException e) {
			resultBean = new ProccessResultBean(false, "用户禁用失败，禁用用户时发生异常.");
		}
		HttpServletUtils.outJson(this.getResponse(), resultBean);
		return null;
	}

	/**
	 * 
	 * @author：Wuqingming
	 * @date：2011-5-19
	 * @Description：录入用户信息
	 * @return
	 * @throws ViewException
	 */
	public String inputUser() throws ViewException {
		ProccessResultBean resultBean = null;
		String optype = this.getParameter("optype");

		if (StringUtils.equals("add", optype)) { // 新增
			String temp = user.getUserid().toString().trim();
			Pattern p = Pattern.compile(" \\s*|\t|\r|\n");
			Matcher mac = p.matcher(temp);
			user.setUserid(mac.replaceAll("").toUpperCase());
		}

		/**
		 * 唯一性检测
		 */
		resultBean = this.checkUserUnique(user, optype);
		if (!resultBean.isSuccess()) {
			HttpServletUtils.outJson(this.getResponse(), resultBean);
			return null;
		}

		String newpwd = this.getParameter("newpwd");
		if (StringUtils.isNotEmpty(newpwd)) {
			try {
				user.setUserpwd(EncoderUtils.base64Encode(newpwd));
			} catch (IOException e) {
				Log.error("encode pwd error", e);
			}
		}
		if (StringUtils.equals("add", optype)) { // 新增
			try {
				this.getGenericDAO().insert("UserMapper.insert", user);
				resultBean = new ProccessResultBean(true, "用户新增成功.");
			} catch (DAOException e) {
				resultBean = new ProccessResultBean(false, "用户新增失败.");
			}
		} else { // 修改
			try {
				this.getGenericDAO().update("UserMapper.update", user);
				resultBean = new ProccessResultBean(true, "用户信息修改成功.");
			} catch (DAOException e) {
				resultBean = new ProccessResultBean(false, "用户信息修改失败.");
			}
		}
		HttpServletUtils.outJson(this.getResponse(), resultBean);
		return null;
	}

	/**
	 * 
	 * @author：chenyun
	 * @date：2011-10-22
	 * @Description：用户名唯一性检测
	 * @param user
	 * @param optype
	 * @return
	 * @throws ViewException
	 */
	protected ProccessResultBean checkUserUnique(User user, String optype)
			throws ViewException {
		if (StringUtils.isEmpty(user.getUserid())) {
			return new ProccessResultBean(false, "登录用户名不能为空.");
		}
		Map para = new HashMap();
		para.put("userid", user.getUserid());
		para.put("mail", user.getMail());
		Long count = this.getGenericDAO().getCount(
				"UserMapper.getCountForUnique", para);
		if (StringUtils.equals("add", optype) && count == 0) { // 新增
			return new ProccessResultBean(true, "检测通过.");
		} else if (StringUtils.equals("update", optype) && count <= 1) {// 修改
			return new ProccessResultBean(true, "检测通过.");
		}
		return new ProccessResultBean(false, "登录用户名或邮箱被占用, 请更换。若密码忘记，请点击首页找回密码按钮。");
	}

	/**
	 * 
	 * @author：Wuqingming
	 * @date：2011-5-19
	 * @Description：批量删除用户
	 * @return
	 * @throws ViewException
	 */
	public String deleteUserBatch() throws ViewException {
		ProccessResultBean resultBean = null;
		String userids = this.getParameter("userids");
		String isSpflag = this.getParameter("isSpflag");
		String[] useridArray = userids.split(",");
		try {
			this.getGenericDAO().delete("UserMapper.deleteBatch", useridArray);
			/*删除用户不发邮件
			if (StringUtils.equals(isSpflag, "yes")) {
				String mails = this.getParameter("mails");
				this.sendMail(mails.split(","), "spFail");
			}*/

			resultBean = new ProccessResultBean(true, "用户信息删除成功.");
		} catch (DAOException e) {
			resultBean = new ProccessResultBean(false, "用户信息删除失败.");
		}
		HttpServletUtils.outJson(this.getResponse(), resultBean);
		return null;
	}


	/**
	 * 
	 * @author：Wuqingming
	 * @date：2011-5-31
	 * @Description：session失效检测
	 * @return
	 */
	public String checkSessionExpire() {
		ProccessResultBean resultBean = null;
		UserSession userSession = SecurityHelper.getCurrentUser();
		if (userSession == null) {
			resultBean = new ProccessResultBean(true, "session失效, 请重新登录.");
		} else {
			resultBean = new ProccessResultBean(false, "");
		}
		HttpServletUtils.outJson(this.getResponse(), resultBean);
		return null;
	}

	/**
	 * 
	 * @author：Wuqingming
	 * @date：2011-5-23
	 * @Description：忘记密码
	 * @return
	 */
	public String forgetPwd() {
		ProccessResultBean resultBean = null;
		String type = this.getParameter("findtype");
		String value = this.getParameter("findkey");

		if (StringUtils.isEmpty(type) || StringUtils.isEmpty(value)) {
			resultBean = new ProccessResultBean(false, "信息为空.");
			HttpServletUtils.outJson(this.getResponse(), resultBean);
			return null;
		}

		Map condition = new HashMap();

		if (StringUtils.equals(type, "userid")) {
			value = value.toUpperCase();
		}

		condition.put(type, value);
		List<User> userList = this.getGenericDAO().queryList(
				"UserMapper.query", condition);
		if (CollectionUtils.isEmpty(userList)) {
			resultBean = new ProccessResultBean(false, "该用户不存在.");
			HttpServletUtils.outJson(this.getResponse(), resultBean);
			return null;
		}
		user = userList.get(0);
		String newPwd = String.valueOf(RandomStringUtils.randomNumeric(8));
		try {
			user.setUserpwd(EncoderUtils.base64Encode(newPwd));
		} catch (IOException e) {
			e.printStackTrace();
		}

		/** 修改用户名与密码 */
		try {
			this.getGenericDAO().update("UserMapper.update", user);
			user.setUserpwd(newPwd);
			MailTemplete.sendMail(MailTemplete.findPassWd(user.getUsername(),
					user.getMail(), user.getUserpwd()));
		} catch (DAOException e) {
			Log.error("UserAction", e);
		} catch (MailException e) {
			Log.error("UserAction", e);
		}
		resultBean = new ProccessResultBean(true, "新密码已发送至您的邮箱, 请注意查收.");
		HttpServletUtils.outJson(this.getResponse(), resultBean);
		return null;
	}

	/**
	 * 
	 * @author：Wuqingming
	 * @date：2011-5-3
	 * @Description：用户注册
	 * @return
	 * @throws ViewException
	 */
	public String register() throws ViewException {
		if (user == null) {
			return null;
		}

		/** 其他注册约束条件 */

		user.setRoleid(SecurityHelper.COMMON_USER); // 注册方式的用户默认角色
		// user.setStatus("disable");
		// 用户注册不用审批，即时生效
		user.setStatus("enable");
		return inputUser();
	}

	private ProccessResultBean checkUser(User user) {
		ProccessResultBean resultBean = null;
		if (StringUtils.isEmpty(user.getUsername())) {
			resultBean = new ProccessResultBean(false, "用户名不能为空！");
		} else if (StringUtils.isEmpty(user.getMobile())) {
			resultBean = new ProccessResultBean(false, "手机号不能为空！");
		} else if (StringUtils.isEmpty(user.getUserpwd())) {
			resultBean = new ProccessResultBean(false, "密码不能为空！");
		} else if (StringUtils.isEmpty(user.getMail())) {
			resultBean = new ProccessResultBean(false, "邮箱不能为空！");
		} else {
			resultBean = new ProccessResultBean(true);
		}
		return resultBean;
	}

	/**
	 * validate user infomation
	 * 
	 * @return
	 */
	public String validateUser() {
		ProccessResultBean resultBean = this.checkUser(user);
		if (resultBean.isSuccess()) { // 修改模板
			Long count = this.getGenericDAO().getCount(
					"UserMapper.validateUser", user);
			if (count > 0) {
				resultBean = new ProccessResultBean(false, "用户名、手机号码或者邮箱已存在。");
			} else {
				resultBean = new ProccessResultBean(true, "验证通过.");
			}
		}
		HttpServletUtils.outJson(this.getResponse(), resultBean);
		return null;
	}

}
