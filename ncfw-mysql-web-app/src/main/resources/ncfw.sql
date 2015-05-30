/*
SQLyog Enterprise - MySQL GUI v6.53 RC2
MySQL - 5.0.45-community-nt : Database - ncfw
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

/*Table structure for table `ac_menu` */

DROP TABLE IF EXISTS `ac_menu`;

CREATE TABLE `ac_menu` (
  `MENUID` varchar(50) NOT NULL,
  `MENUNAME` varchar(100) default NULL,
  `MENULABELNAME` varchar(100) default NULL,
  `MENUCODE` varchar(80) default NULL,
  `MENUENTRY` varchar(200) default NULL,
  `ISLEAF` varchar(4) default NULL,
  `OPENMODE` varchar(40) default NULL,
  `DISPLAYORDER` varchar(4) default NULL,
  `ISDISPLAY` varchar(4) default NULL,
  `PARENTMENUID` varchar(50) default NULL,
  `MENUSEQ` varchar(512) default NULL,
  PRIMARY KEY  (`MENUID`)
) ENGINE=MyISAM DEFAULT CHARSET=gbk;

/*Data for the table `ac_menu` */

insert  into `ac_menu`(`MENUID`,`MENUNAME`,`MENULABELNAME`,`MENUCODE`,`MENUENTRY`,`ISLEAF`,`OPENMODE`,`DISPLAYORDER`,`ISDISPLAY`,`PARENTMENUID`,`MENUSEQ`) values ('1','系统管理','系统管理','sys-manage',NULL,'n','x-workspace','1','y','rootId','.rootId.1.'),('130525985548471574','用户管理','用户管理','user-manages',NULL,'n','x-workspace','2','y','rootId','.rootId.130525985548471574.'),('130525987975070783','用户管理','用户管理','user-manage','business/user/user.jsp','y','x-workspace','1','y','130525985548471574','.rootId.130525985548471574.130525987975070783.'),('130525989948478669','密码修改','密码修改','changepwd-manage','business/user/changepwd.jsp','y','x-workspace','2','y','130525985548471574','.rootId.130525985548471574.130525989948478669.'),('130659009426544678','黑白名单管理','黑白名单管理','blankwhitelistmng','business/user/blacklist.jsp','y','x-workspace','5','y','130525985548471574','.rootId.130525985548471574.130659009426544678.'),('130693917942193708','上下行短信统计','上下行短信统计','upAndDown','business/statistics/upanddown.jsp','y','x-workspace','','y','130686192775931734',NULL),('130693944109315776','投诉管理','投诉管理','','business/servicepackage/complaints.jsp','y','x-workspace','','y','130525929493716511',NULL),('2','数据字典管理','数据字典管理','dict-manage','ncframework/datadict/dictionary.jsp','y','x-workspace','1','y','1','.rootId.1.2.'),('4','系统菜单管理','系统菜单管理','sys-menu','ncframework/rights/sysmenu.jsp','y','x-workspace','3','y','1','.rootId.1.4.'),('5','角色管理','角色管理','sys-role','ncframework/rights/role.jsp','y','x-workspace','4','y','1','.rootId.1.5.'),('131917059719273161','操作日志','操作日志','operatelog','ncframework/log/listSysOperateLog.jsp','y','x-workspace','4','y','1','.rootId.1.131917059719273161.');

/*Table structure for table `ac_role` */

DROP TABLE IF EXISTS `ac_role`;

CREATE TABLE `ac_role` (
  `ROLEID` varchar(30) NOT NULL,
  `ROLECODE` varchar(50) default NULL,
  `ROLENAME` varchar(100) default NULL,
  `APPID` varchar(30) default NULL,
  `ROLETYPE` varchar(20) default NULL,
  `REMARK` varchar(255) default NULL,
  `TRANSFERROLESLIMIT` varchar(1000) default NULL,
  PRIMARY KEY  (`ROLEID`)
) ENGINE=MyISAM DEFAULT CHARSET=gbk;

/*Data for the table `ac_role` */

insert  into `ac_role`(`ROLEID`,`ROLECODE`,`ROLENAME`,`APPID`,`ROLETYPE`,`REMARK`,`TRANSFERROLESLIMIT`) values ('131916683390300000','131916683390300000','普通用户',NULL,NULL,'普通用户',''),('sysadmin','sysadmin','系统管理员',NULL,NULL,'系统管理员，维护系统',''),('131916680783600000','131916680783600000','帐号管理员',NULL,NULL,'管理系统帐号','');

/*Table structure for table `ac_role_menu_ref` */

DROP TABLE IF EXISTS `ac_role_menu_ref`;

CREATE TABLE `ac_role_menu_ref` (
  `ROLEID` varchar(30) default NULL,
  `MENUID` varchar(50) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=gbk;

/*Data for the table `ac_role_menu_ref` */

insert  into `ac_role_menu_ref`(`ROLEID`,`MENUID`) values ('130526103906200000','130525929493716511'),('130526103906200000','130525942075071455'),('130526103906200000','130525946457837679'),('130526103906200000','130525954775046794'),('130526103906200000','130525960279627919'),('130526105175000000','130525944410950943'),('130526105175000000','130525961909397327'),('130526106164000000','130525944410950943'),('130526106164000000','130525961909397327'),('130526103906200000','130525938342146599'),('130526103906200000','130525944410950943'),('130526103906200000','130525952523405702'),('130526103906200000','130525958132866237'),('130526103906200000','130525961909397327'),('130526105175000000','130525929493716511'),('130526105175000000','130525960279627919'),('130526106164000000','130525929493716511'),('130526106164000000','130525960279627919'),('sysadmin','130525987975070783'),('sysadmin','130525985548471574'),('sysadmin','130649838185390620'),('sysadmin','130579578735911407'),('sysadmin','130579576762541947'),('sysadmin','130579573954773267'),('sysadmin','130579571745360974'),('sysadmin','130579569701663235'),('sysadmin','130577293031263127'),('sysadmin','130525958132866237'),('sysadmin','130525929493716511'),('sysadmin','5'),('sysadmin','4'),('sysadmin','2'),('sysadmin','1'),('sysadmin','130525989948478669'),('130681201001900000','130686208452387170'),('130681201001900000','130686205185905035'),('130681201001900000','130686192775931734'),('130681222165600000','130746835738738190'),('130681222165600000','130693917942193708'),('130681222165600000','130686208452387170'),('130681222165600000','130686205185905035'),('130681228039300000','130746835738738190'),('130681228039300000','130686192775931734'),('130681228039300000','130659014453146292'),('130681228039300000','130659009426544678'),('130681222165600000','130686199135418528'),('130681222165600000','130686192775931734'),('130681222165600000','130659014453146292'),('130681222165600000','130659009426544678'),('130681222165600000','130525989948478669'),('130681222165600000','130525987975070783'),('130681222165600000','130525985548471574'),('130681201001900000','130659014453146292'),('130681201001900000','130659009426544678'),('130681201001900000','130525989948478669'),('130681201001900000','130525987975070783'),('130681201001900000','130525985548471574'),('130681201001900000','130693944109315776'),('130681201001900000','130676509329462041'),('130681201001900000','130579571745360974'),('130681222165600000','130693944109315776'),('130681222165600000','130676509329462041'),('130681222165600000','130649838185390620'),('130681222165600000','130579571745360974'),('130681201001900000','130525929493716511'),('130681201001900000','130746835738738190'),('130681222165600000','130525929493716511'),('130681222165600000','130866449978965303'),('130681222165600000','130866456975808002'),('130681228039300000','130525985548471574'),('130681228039300000','130676509329462041'),('130681228039300000','130649838185390620'),('130681228039300000','130579573954773267'),('130681228039300000','130579571745360974'),('130681228039300000','130525929493716511'),('131337751864000000','130746835738738190'),('131337751864000000','130693917942193708'),('131337751864000000','130686208452387170'),('131337751864000000','130686199135418528'),('131337751864000000','130686192775931734'),('131337751864000000','130659014453146292'),('131337751864000000','130525985548471574'),('131337751864000000','130579571745360974'),('131337751864000000','130525929493716511'),('131337751864000000','130866449978965303'),('131337751864000000','131069923331942802'),('131337751864000000','131069925896904575'),('130681228039300000','130866449978965303'),('130681228039300000','130866456975808002'),('131916680783600000','130525985548471574'),('131916680783600000','130525987975070783'),('131916680783600000','130525989948478669'),('131916680783600000','130659009426544678');

/*Table structure for table `sys_dict_entry` */

DROP TABLE IF EXISTS `sys_dict_entry`;

CREATE TABLE `sys_dict_entry` (
  `DICTID` varchar(100) NOT NULL,
  `DICTTYPEID` varchar(255) NOT NULL,
  `DICTNAME` varchar(255) default NULL,
  `STATUS` int(11) default NULL,
  `SORTNO` int(11) default NULL,
  `RANK` int(11) default NULL,
  `PARENTID` varchar(255) default NULL,
  `SEQNO` varchar(255) default NULL,
  `FILTER1` varchar(255) default NULL,
  `FILTER2` varchar(255) default NULL,
  `MEMO` varchar(1024) default NULL,
  PRIMARY KEY  (`DICTID`,`DICTTYPEID`),
  KEY `FK_SYS_DICT_REFERENCE_SYS_DICT` (`DICTTYPEID`)
) ENGINE=MyISAM DEFAULT CHARSET=gbk;

/*Data for the table `sys_dict_entry` */

insert  into `sys_dict_entry`(`DICTID`,`DICTTYPEID`,`DICTNAME`,`STATUS`,`SORTNO`,`RANK`,`PARENTID`,`SEQNO`,`FILTER1`,`FILTER2`,`MEMO`) values ('0','BUSI_TONGJI_TYPE','按天统计',1,0,NULL,NULL,NULL,NULL,NULL,''),('0','BUSI_USER_PACKAGE_STATUS','待确认',1,1,NULL,NULL,NULL,NULL,NULL,''),('1','BUSI_TONGJI_TYPE','按月统计',1,0,NULL,NULL,NULL,NULL,NULL,''),('1','BUSI_USER_PACKAGE_STATUS','已确认',1,2,NULL,NULL,NULL,NULL,NULL,'订购套餐状态，0：待确认，1：已确认'),('comsomol','SYS_EMPPOLITICAL','团员',1,0,NULL,NULL,NULL,NULL,NULL,NULL),('crowd','SYS_EMPPOLITICAL','群众',1,1,NULL,NULL,NULL,NULL,NULL,NULL),('datadict','SYS_MODULE','数据字典',1,1,NULL,NULL,NULL,NULL,NULL,''),('delete','SYS_LOG_OPERATE_TYPE','删除',1,3,NULL,NULL,NULL,NULL,NULL,''),('exception','SYS_LOG_OPERATE_RESULT','异常',1,3,NULL,NULL,NULL,NULL,NULL,''),('failure','SYS_LOG_OPERATE_RESULT','失败',1,2,NULL,NULL,NULL,NULL,NULL,''),('insert','SYS_LOG_OPERATE_TYPE','新增',1,1,NULL,NULL,NULL,NULL,NULL,''),('menu','SYS_MODULE','菜单',1,2,NULL,NULL,NULL,NULL,NULL,''),('other','SYS_LOG_OPERATE_TYPE','其他',1,5,NULL,NULL,NULL,NULL,NULL,''),('partymember','SYS_EMPPOLITICAL','党员',1,2,NULL,NULL,NULL,NULL,NULL,NULL),('query','SYS_LOG_OPERATE_TYPE','查询',1,4,NULL,NULL,NULL,NULL,NULL,''),('role','SYS_MODULE','角色',1,3,NULL,NULL,NULL,NULL,NULL,''),('disable','SYS_USER_STATUS','禁用',1,2,NULL,NULL,NULL,NULL,NULL,''),('enable','SYS_USER_STATUS','启用',1,1,NULL,NULL,NULL,NULL,NULL,''),('success','SYS_LOG_OPERATE_RESULT','成功',1,1,NULL,NULL,NULL,NULL,NULL,''),('update','SYS_LOG_OPERATE_TYPE','修改',1,2,NULL,NULL,NULL,NULL,NULL,''),('user','SYS_MODULE','用户',1,4,NULL,NULL,NULL,NULL,NULL,''),('1','BUSI_UPDOWN_SUBMIT','下行',1,0,NULL,NULL,NULL,NULL,NULL,''),('0','BUSI_UPDOWN_SUBMIT','上行',1,0,NULL,NULL,NULL,NULL,NULL,'');

/*Table structure for table `sys_dict_type` */

DROP TABLE IF EXISTS `sys_dict_type`;

CREATE TABLE `sys_dict_type` (
  `DICTTYPEID` varchar(100) NOT NULL,
  `DICTTYPENAME` varchar(255) default NULL,
  `RANK` int(11) default NULL,
  `SEQNO` varchar(255) default NULL,
  `PARENTID` varchar(255) default NULL,
  PRIMARY KEY  (`DICTTYPEID`)
) ENGINE=MyISAM DEFAULT CHARSET=gbk;

/*Data for the table `sys_dict_type` */

insert  into `sys_dict_type`(`DICTTYPEID`,`DICTTYPENAME`,`RANK`,`SEQNO`,`PARENTID`) values ('BUSI_TONGJI_TYPE','统计方式',1,'.BUSI_TONGJI_TYPE.',NULL),('BUSI_USER_PACKAGE_STATUS','用户套餐订购状态',1,'.BUSI_USER_PACKAGE_STATUS.',NULL),('SYS_EMPPOLITICAL','人员政治面貌',1,'.SYS_EMPPOLITICAL.',NULL),('SYS_LOG_OPERATE_RESULT','系统操作结果',1,'.SYS_LOG_OPERATE_RESULT.',NULL),('SYS_LOG_OPERATE_TYPE','系统操作日志类型',1,'.SYS_LOG_OPERATE_TYPE.',NULL),('SYS_MODULE','系统模块',1,'.SYS_MODULE.',NULL),('SYS_USER_STATUS','系统用户状态',1,'.SYS_USER_STATUS.',NULL),('BUSI_UPDOWN_SUBMIT','上下行类型',1,'.BUSI_UPDOWN_SUBMIT.',NULL);

/*Table structure for table `sys_operate_log` */

DROP TABLE IF EXISTS `sys_operate_log`;

CREATE TABLE `sys_operate_log` (
  `LOGID` varchar(30) NOT NULL,
  `OPERATETYPE` varchar(16) default NULL,
  `OPERATETIME` datetime default NULL,
  `OPERATERESULT` varchar(10) default NULL,
  `APP` varchar(50) default NULL,
  `MODULE` varchar(50) default NULL,
  `CLASSNAME` varchar(255) default NULL,
  `METHODNAME` varchar(255) default NULL,
  `METHODPARAMETERS` varchar(1000) default NULL,
  `METHODDESCRIPTION` varchar(1000) default NULL,
  `OPERATOR` varchar(30) default NULL,
  `REMARK` varchar(1024) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

/*Data for the table `sys_operate_log` */

insert  into `sys_operate_log`(`LOGID`,`OPERATETYPE`,`OPERATETIME`,`OPERATERESULT`,`APP`,`MODULE`,`CLASSNAME`,`METHODNAME`,`METHODPARAMETERS`,`METHODDESCRIPTION`,`OPERATOR`,`REMARK`) values ('130746835752200000','insert','2011-06-08 01:39:17','success',NULL,'menu','com.talkweb.ncframework.modules.rights.menu.service.impl.SystemMenuServiceImpl','insertMenu','{com.talkweb.ncframework.modules.rights.menu.entity.SystemMenu@1eea96c}','新增菜单',NULL,'菜单新增成功.'),('130857595318600000','insert','2011-06-20 21:19:13','success',NULL,'menu','com.talkweb.ncframework.modules.rights.menu.service.impl.SystemMenuServiceImpl','insertMenu','{com.talkweb.ncframework.modules.rights.menu.entity.SystemMenu@17b650a}','新增菜单',NULL,'菜单新增成功.'),('130866449991200000','insert','2011-06-21 21:54:59','success',NULL,'menu','com.talkweb.ncframework.modules.rights.menu.service.impl.SystemMenuServiceImpl','insertMenu','{com.talkweb.ncframework.modules.rights.menu.entity.SystemMenu@638ee4}','新增菜单',NULL,'菜单新增成功.'),('130866451660400000','update','2011-06-21 21:55:16','success',NULL,'menu','com.talkweb.ncframework.modules.rights.menu.service.impl.SystemMenuServiceImpl','updateMenu','{com.talkweb.ncframework.modules.rights.menu.entity.SystemMenu@26161}','修改菜单',NULL,'菜单修改成功.'),('130866456987000000','insert','2011-06-21 21:56:09','success',NULL,'menu','com.talkweb.ncframework.modules.rights.menu.service.impl.SystemMenuServiceImpl','insertMenu','{com.talkweb.ncframework.modules.rights.menu.entity.SystemMenu@ca1465}','新增菜单',NULL,'菜单新增成功.'),('131069923343800000','insert','2011-07-15 11:07:13','success',NULL,'menu','com.talkweb.ncframework.modules.rights.menu.service.impl.SystemMenuServiceImpl','insertMenu','{com.talkweb.ncframework.modules.rights.menu.entity.SystemMenu@192974}','新增菜单',NULL,'菜单新增成功.'),('131069925908400000','insert','2011-07-15 11:07:38','success',NULL,'menu','com.talkweb.ncframework.modules.rights.menu.service.impl.SystemMenuServiceImpl','insertMenu','{com.talkweb.ncframework.modules.rights.menu.entity.SystemMenu@16b7f83}','新增菜单',NULL,'菜单新增成功.'),('131916504366800000','delete','2011-10-21 10:44:03','success',NULL,'menu','com.talkweb.ncframework.modules.rights.menu.service.impl.SystemMenuServiceImpl','deleteMenuBatch','{{130525929493716511}}','删除菜单',NULL,'菜单删除成功.'),('131916505016100000','delete','2011-10-21 10:44:10','success',NULL,'menu','com.talkweb.ncframework.modules.rights.menu.service.impl.SystemMenuServiceImpl','deleteMenuBatch','{{130686192775931734}}','删除菜单',NULL,'菜单删除成功.'),('131916505543800000','delete','2011-10-21 10:44:15','success',NULL,'menu','com.talkweb.ncframework.modules.rights.menu.service.impl.SystemMenuServiceImpl','deleteMenuBatch','{{130866449978965303}}','删除菜单',NULL,'菜单删除成功.'),('131916510198700000','delete','2011-10-21 10:45:01','success',NULL,'menu','com.talkweb.ncframework.modules.rights.menu.service.impl.SystemMenuServiceImpl','deleteMenuBatch','{{130659014453146292}}','删除菜单',NULL,'菜单删除成功.'),('131916510808900000','delete','2011-10-21 10:45:07','success',NULL,'menu','com.talkweb.ncframework.modules.rights.menu.service.impl.SystemMenuServiceImpl','deleteMenuBatch','{{130857595305619541}}','删除菜单',NULL,'菜单删除成功.'),('131917059744300000','insert','2011-10-21 12:16:37','success',NULL,'menu','com.talkweb.ncframework.modules.rights.menu.service.impl.SystemMenuServiceImpl','insertMenu','{com.talkweb.ncframework.modules.rights.menu.entity.SystemMenu@16be8a0}','新增菜单',NULL,'菜单新增成功.');

/*Table structure for table `t_prizeuser` */

DROP TABLE IF EXISTS `t_prizeuser`;

CREATE TABLE `t_prizeuser` (
  `userid` varchar(255) NOT NULL,
  `roleid` varchar(50) default NULL,
  `surname` varchar(10) default NULL,
  `givenname` varchar(30) default NULL,
  `username` varchar(40) default NULL,
  `displayname` varchar(100) default NULL,
  `gender` char(1) default NULL,
  `age` int(11) default NULL,
  `birthday` varchar(30) default NULL,
  `photo` varchar(255) default NULL,
  `mail` varchar(50) default NULL,
  `mobile` varchar(20) default NULL,
  `officetel` varchar(30) default NULL,
  `telephonenumber` varchar(50) default NULL,
  `status` varchar(10) default NULL,
  `usertype` varchar(10) default NULL,
  `provinceNumber` varchar(20) default NULL,
  `cityNumber` varchar(20) default NULL,
  `streetNumber` varchar(20) default NULL,
  `postalcode` varchar(30) default NULL,
  `identityNumber` varchar(30) default NULL,
  `qqNumber` varchar(20) default NULL,
  `msnNumber` varchar(50) default NULL,
  `orderNumber` int(11) default NULL,
  `userPwd` varchar(100) default NULL,
  `createTime` date default NULL,
  `lastActiveTime` date default NULL,
  `remark` varchar(512) default NULL,
  `bakcolumn1` varchar(50) default NULL COMMENT '备份字段1，已被用，标识加入黑名单类型，0:单个，1:批量 ',
  `bakcolumn2` varchar(50) default NULL,
  `bakcolumn3` varchar(50) default NULL,
  PRIMARY KEY  (`userid`),
  KEY `Index_mobile` (`mobile`),
  KEY `usertype` (`usertype`),
  KEY `mobile` (`mobile`)
) ENGINE=MyISAM DEFAULT CHARSET=gbk;

/*Data for the table `t_prizeuser` */

insert  into `t_prizeuser`(`userid`,`roleid`,`surname`,`givenname`,`username`,`displayname`,`gender`,`age`,`birthday`,`photo`,`mail`,`mobile`,`officetel`,`telephonenumber`,`status`,`usertype`,`provinceNumber`,`cityNumber`,`streetNumber`,`postalcode`,`identityNumber`,`qqNumber`,`msnNumber`,`orderNumber`,`userPwd`,`createTime`,`lastActiveTime`,`remark`,`bakcolumn1`,`bakcolumn2`,`bakcolumn3`) values ('sysadmin','sysadmin',NULL,NULL,'系统管理员',NULL,NULL,NULL,NULL,NULL,NULL,'18601393036',NULL,NULL,'enable',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'MDAwMDAw',NULL,NULL,NULL,NULL,NULL,NULL),('131917068603191164','131916680783600000',NULL,NULL,'帐号管理员',NULL,NULL,NULL,NULL,NULL,NULL,'18601392030',NULL,NULL,'enable',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2011-10-21',NULL,NULL,NULL,NULL,NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
