package org.thorn.user.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;
import org.springframework.security.core.userdetails.UserCache;
import org.thorn.core.jmail.MailEntity;
import org.thorn.core.util.ExecutorUtils;
import org.thorn.core.util.LocalStringUtils;
import org.thorn.dao.core.Configuration;
import org.thorn.web.entity.Page;
import org.thorn.dao.exception.DBAccessException;
import org.thorn.log.NoLogging;
import org.thorn.security.SecurityEncoderUtils;
import org.thorn.user.dao.IUserDao;
import org.thorn.user.entity.User;
import org.thorn.user.task.MailTask;
import org.thorn.user.task.MailTemplete;

/**
 * @ClassName: UserServiceImpl
 * @Description:
 * @author chenyun
 * @date 2012-5-6 下午12:01:15
 */
public class UserServiceImpl implements IUserService {

	@Autowired
	@Qualifier("userDao")
	private IUserDao userDao;

	@Autowired
	@Qualifier("userSecurityCache")
	private UserCache userCache;

	@Autowired
	@Qualifier("incrSequence")
	private DataFieldMaxValueIncrementer incrSequence;

	public User queryUserByLogin(String idOrAccount) throws DBAccessException {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("idOrAccount", idOrAccount);

		return userDao.queryUser(filter);
	}

	public void save(User user) throws DBAccessException {

		String numTemp = "0000";

		String seqNum = incrSequence.nextStringValue();

		seqNum = numTemp.substring(seqNum.length()) + seqNum;

		String userId = "FY" + user.getOrgCode() + seqNum;
		user.setUserId(userId);

		String pwd = user.getUserPwd();

		if (LocalStringUtils.isNotEmpty(pwd)) {
			user.setUserPwd(SecurityEncoderUtils.encodeUserPassword(pwd,
					user.getUserId()));
		}

		userDao.save(user);

		if (LocalStringUtils.isNotEmpty(pwd)
				&& LocalStringUtils.isNotEmpty(user.getCumail())) {
			MailEntity mailInfo = MailTemplete.registerUser(user.getUserName(),
					user.getCumail(), pwd, user.getUserId());
			MailTask task = new MailTask(mailInfo);
			ExecutorUtils.executeTask(task);
		}
	}

	public void modify(User user) throws DBAccessException {
		String pwd = user.getUserPwd();

		if (LocalStringUtils.isNotEmpty(pwd)) {
			user.setUserPwd(SecurityEncoderUtils.encodeUserPassword(pwd,
					user.getUserId()));
		}

		userDao.modify(user);
		userCache.removeUserFromCache(user.getUserId());

		if (LocalStringUtils.isNotEmpty(pwd)
				&& LocalStringUtils.isNotEmpty(user.getCumail())) {
			MailEntity mailInfo = MailTemplete.changePassWd(user.getUserName(),
					user.getCumail(), pwd);
			MailTask task = new MailTask(mailInfo);
			ExecutorUtils.executeTask(task);
		}

	}

	public void delete(String ids) throws DBAccessException {
		List<String> list = LocalStringUtils.splitStr2Array(ids);
		userDao.delete(list);

		for (String uid : list) {
			userCache.removeUserFromCache(uid.toUpperCase());
		}
	}

	public List<User> querySysAdmin() throws DBAccessException {
		Map<String, Object> filter = new HashMap<String, Object>();

		filter.put("defaultRole", "SYSADMIN");
		filter.put("isDisabled", Configuration.DB_NO);

		return userDao.queryList(filter);
	}

	public List<User> queryList(String orgCode, String userName, String cumail,
			Collection<String> areas, String userAccount,
			Collection<String> userIds, Collection<String> orgIds)
			throws DBAccessException {
		Map<String, Object> filter = new HashMap<String, Object>();

		filter.put("idOrAccount", userAccount);
		filter.put("cumail", cumail);
		filter.put("orgCode", orgCode);
		filter.put("userName", userName);
		filter.put("userIds", userIds);
		filter.put("orgs", orgIds);
		filter.put("areas", areas);
		filter.put("isDisabled", Configuration.DB_NO);

		return userDao.queryList(filter);
	}

	public Page<User> queryPage(String orgCode, String userName, String cumail,
			String area, String userAccount, long start, long limit,
			String sort, String dir) throws DBAccessException {
		Map<String, Object> filter = new HashMap<String, Object>();

		filter.put("idOrAccount", userAccount);
		filter.put("cumail", cumail);
		filter.put("orgCode", orgCode);
		filter.put("area", area);
		filter.put("userName", userName);

		filter.put(Configuration.PAGE_LIMIT, limit);
		filter.put(Configuration.PAGE_START, start);

		if (LocalStringUtils.isEmpty(sort)) {
			filter.put(Configuration.SROT_NAME, "SORTNUM");
			filter.put(Configuration.ORDER_NAME, Configuration.ORDER_ASC);
		} else {
			filter.put(Configuration.SROT_NAME, sort);
			filter.put(Configuration.ORDER_NAME, dir);
		}

		Page<User> page = new Page<User>();

		page.setTotal(userDao.queryPageCount(filter));
		if (page.getTotal() > 0) {
			page.setReslutSet(userDao.queryList(filter));
		}

		return page;
	}

	public void disabledUser(String ids, String isDisabled)
			throws DBAccessException {
		Map<String, Object> filter = new HashMap<String, Object>();

		List<String> list = LocalStringUtils.splitStr2Array(ids);
		filter.put("list", list);
		filter.put("isDisabled", isDisabled);

		userDao.disabled(filter);

		for (String uid : list) {
			userCache.removeUserFromCache(uid.toUpperCase());
		}
	}

	public void changePwd(String userId, String newPwd)
			throws DBAccessException {
		User user = new User();
		user.setUserId(userId.toUpperCase());
		user.setUserPwd(SecurityEncoderUtils.encodeUserPassword(newPwd,
				user.getUserId()));

		userDao.modify(user);
		userCache.removeUserFromCache(user.getUserId());
	}

	@NoLogging
	public boolean myPwdFindBack(String idOrAccount, String email)
			throws DBAccessException {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("idOrAccount", idOrAccount);
		filter.put("cumail", email);

		User user = userDao.queryUser(filter);

		if (user != null) {

			String newPwd = LocalStringUtils.randomString(10);
			user.setUserPwd(SecurityEncoderUtils.encodeUserPassword(newPwd,
					user.getUserId()));
			userDao.modify(user);
			userCache.removeUserFromCache(user.getUserId());

			MailEntity mailInfo = MailTemplete.findPassWd(user.getUserName(),
					email, newPwd);
			MailTask task = new MailTask(mailInfo);
			ExecutorUtils.executeTask(task);

			return true;
		} else {
			return false;
		}

	}
}
