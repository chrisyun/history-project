package com.talkweb.ncfw.entity;
/**
 * <p>文件名称: User.java</p>
 * <p>文件描述: 用户实体</p>
 * <p>版权所有: 版权所有(C)2010</p>
 * <p>公　　司: 拓维信息系统股份有限公司</p>
 * <p>内容摘要: 简要描述本文件的内容，包括主要模块、函数及能的说明</p>
 * <p>其他说明: 其它内容的说明</p>
 * <p>完成日期: 2011-4-30</p>
 * <p>修改记录1:</p>
 * <pre>
 *    修改日期:
 *    修 改 人:
 *    修改内容:
 * </pre>
 * <p>修改记录2：…</p>
 * @author  Wuqingming
 */
public class User implements java.io.Serializable {

	private static final long serialVersionUID = -580489897943049566L;
	
	private String 			userid;						//用户ID
	private String			roleid;						//角色ID
	private String 			surname;					//姓
	private String 			givenname;					//名
	private String 			username;					//用户姓名
	private String 			displayname;				//显示名称
	private String 			gender;						//性别
	private Integer 		age;						//年龄
	private String			birthday;					//生日
	private String 			photo;						//照片
	private String 			mail;						//邮箱
	private String 			mobile;						//手机号码
	private String 			telephonenumber;			//电话号码
	private String 			officetel;					//办公电话
	private String 			faxnumber;					//传真号码
	private String 			status;						//状态
	private String 			usertype;					//用户类型
	private String 			provincenumber;				//省份编码(全国23个省份编码)
	private String 			citynumber;					//城市编码(上级组织编码)
	private String 			cityname;					//城市名称(上级组织名称)
	private String 			streetnumber;				//街道编码
	private String 			identitynumber;				//身份证号码
	private String 			qqnumber;					//qq号码
	private String 			msnnumber;					//msn号码
	private Integer 		ordernumber;				//排序号
	private Integer 		postalcode;					//邮政编码
	private String 			userpwd;					//登录密码
	
	private String			createtime;					//创建时间
	private String			lastactivetime;				//上次活动时间
	
	private String 			remark;						//备注
	
	private String 			bakcolumn1;					//备份字段1, 已用    黑名单加入类型   0: 单个 1:批量
	private String 			bakcolumn2;					//备份字段2
	private String 			bakcolumn3;					//备份字段3

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getGivenname() {
		return givenname;
	}

	public void setGivenname(String givenname) {
		this.givenname = givenname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDisplayname() {
		return displayname;
	}

	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTelephonenumber() {
		return telephonenumber;
	}

	public void setTelephonenumber(String telephonenumber) {
		this.telephonenumber = telephonenumber;
	}

	public String getOfficetel() {
		return officetel;
	}

	public void setOfficetel(String officetel) {
		this.officetel = officetel;
	}

	public String getFaxnumber() {
		return faxnumber;
	}

	public void setFaxnumber(String faxnumber) {
		this.faxnumber = faxnumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUsertype() {
		return usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}

	public String getProvincenumber() {
		return provincenumber;
	}

	public void setProvincenumber(String provincenumber) {
		this.provincenumber = provincenumber;
	}

	public String getCitynumber() {
		return citynumber;
	}

	public void setCitynumber(String citynumber) {
		this.citynumber = citynumber;
	}

	public String getCityname() {
		return cityname;
	}

	public void setCityname(String cityname) {
		this.cityname = cityname;
	}

	public String getStreetnumber() {
		return streetnumber;
	}

	public void setStreetnumber(String streetnumber) {
		this.streetnumber = streetnumber;
	}

	public String getIdentitynumber() {
		return identitynumber;
	}

	public void setIdentitynumber(String identitynumber) {
		this.identitynumber = identitynumber;
	}

	public String getQqnumber() {
		return qqnumber;
	}

	public void setQqnumber(String qqnumber) {
		this.qqnumber = qqnumber;
	}

	public String getMsnnumber() {
		return msnnumber;
	}

	public void setMsnnumber(String msnnumber) {
		this.msnnumber = msnnumber;
	}

	public Integer getOrdernumber() {
		return ordernumber;
	}

	public void setOrdernumber(Integer ordernumber) {
		this.ordernumber = ordernumber;
	}

	public Integer getPostalcode() {
		return postalcode;
	}

	public void setPostalcode(Integer postalcode) {
		this.postalcode = postalcode;
	}

	public String getUserpwd() {
		return userpwd;
	}

	public void setUserpwd(String userpwd) {
		this.userpwd = userpwd;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getLastactivetime() {
		return lastactivetime;
	}

	public void setLastactivetime(String lastactivetime) {
		this.lastactivetime = lastactivetime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getBakcolumn1() {
		return bakcolumn1;
	}

	public void setBakcolumn1(String bakcolumn1) {
		this.bakcolumn1 = bakcolumn1;
	}

	public String getBakcolumn2() {
		return bakcolumn2;
	}

	public void setBakcolumn2(String bakcolumn2) {
		this.bakcolumn2 = bakcolumn2;
	}

	public String getBakcolumn3() {
		return bakcolumn3;
	}

	public void setBakcolumn3(String bakcolumn3) {
		this.bakcolumn3 = bakcolumn3;
	}

}

