package com.ctsi.ssdc.util;

/**
 * 邮件服务器配置类
 * 
 * @author fuxiang
 *
 */
public class EmailConfigDefine {
	/**
	 * 连接协议
	 */
	private String protocol;
	/**
	 * 主机名
	 */
	private String smtpHost;
	/**
	 * 端口号
	 */
	private String smtpPort;
	/**
	 * 是否开启验证
	 */
	private String smtpAuth;
	/**
	 * 设置是否使用ssl安全连接
	 */
	private String sslEnable;

	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getSmtpHost() {
		return smtpHost;
	}
	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}
	public String getSmtpPort() {
		return smtpPort;
	}
	public void setSmtpPort(String smtpPort) {
		this.smtpPort = smtpPort;
	}
	public String getSmtpAuth() {
		return smtpAuth;
	}
	public void setSmtpAuth(String smtpAuth) {
		this.smtpAuth = smtpAuth;
	}
	public String getSslEnable() {
		return sslEnable;
	}
	public void setSslEnable(String sslEnable) {
		this.sslEnable = sslEnable;
	}
}
