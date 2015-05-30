package org.thorn.security;

/**
 * 
 * @ClassName: SecurityConfiguration 
 * @Description: 
 * @author chenyun
 * @date 2012-5-4 下午02:11:56 
 *
 */
public interface SecurityConfiguration {
	/**
	 * 验证码存放在session中的id
	 */
	public static final String AUTHCODE_SESSION_ID = "ValidateCode";
	/**
	 * 验证码在form中的id
	 */
	public static final String AUTHCODE_FROM_ID = "ValidateCode";
	/**
	 * 验证码图片的宽度
	 */
	public static final int AUTHCODE_IMAGE_WIDTH = 80;
	/**
	 * 验证码图片的长度
	 */
	public static final int AUTHCODE_IMAGE_HEIGHT = 30;
	/**
	 * 验证码的位数
	 */
	public static final int AUTHCODE_NUM_LENGTH = 4;
	/**
	 * 是否
	 */
	public static final String YES = "YES";
	
	public static final String NO = "NO";
	/**
	 * 系统管理员角色ID
	 */
	public static final String SYS_ADMIN_ROLE = "SYSADMIN";
	/**
	 * 只能查看管理员
	 */
	public static final String ADMIN_ROLE = "buweichakan";
	
	public static final String COMMON_USER_ROLE = "COMMONUSER";
	
	public static final String ANONY_MOUS_ROLE = "ROLE_ANONYMOUS";
	
	public static final String SPRING_ENCODER_BEAN = "passwordEncoder";
}

