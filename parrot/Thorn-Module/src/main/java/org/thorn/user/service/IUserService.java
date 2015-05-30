package org.thorn.user.service;

import java.util.Collection;
import java.util.List;

import org.thorn.web.entity.Page;
import org.thorn.dao.exception.DBAccessException;
import org.thorn.user.entity.User;

/**
 * @ClassName: IUserService
 * @Description:
 * @author chenyun
 * @date 2012-5-4 下午03:02:29
 */
public interface IUserService {

	public boolean myPwdFindBack(String idOrAccount, String email)
			throws DBAccessException;

	public User queryUserByLogin(String idOrAccount) throws DBAccessException;

	public void save(User user) throws DBAccessException;

	public void modify(User user) throws DBAccessException;

	public void delete(String ids) throws DBAccessException;

	public Page<User> queryPage(String orgCode, String userName, String cumail,
			String area, String userAccount, long start, long limit,
			String sort, String dir) throws DBAccessException;

	public void disabledUser(String ids, String isDisabled)
			throws DBAccessException;

	public void changePwd(String userId, String newPwd)
			throws DBAccessException;

	public List<User> queryList(String orgCode, String userName, String cumail,
			Collection<String> areas, String userAccount,
			Collection<String> userIds, Collection<String> orgIds)
			throws DBAccessException;

	public List<User> querySysAdmin() throws DBAccessException;
}
