package com.ctsi.ssdc.util;

/**
 * 发送短信类
 * 
 * @author fuxiang
 * @date 2018年9月20日
 */
public class SendMessageInfoDefine {
	/**
	 * 发短信人名
	 */
	private String sendName;
	/**
	 * 短信内容
	 */
	private String sendMessage;
	/**
	 * 手机号
	 */
	private String sendPhone;

	public String getSendName() {
		return sendName;
	}
	public void setSendName(String sendName) {
		this.sendName = sendName;
	}
	public String getSendMessage() {
		return sendMessage;
	}
	public void setSendMessage(String sendMessage) {
		this.sendMessage = sendMessage;
	}
	public String getSendPhone() {
		return sendPhone;
	}
	public void setSendPhone(String sendPhone) {
		this.sendPhone = sendPhone;
	}

}
