package com.ctsi.ssdc.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import static com.ctsi.ssdc.consts.ComponentConstant.UTIL;

/**
 * 发送邮件类
 * @author ctsi
 */
public class EmailUtils {
	private final static String PROTOCOL = "smtp";

	private final static String SMTP_HOST = "smtp-ent.21cn.com";

	private final static String SMTP_PORT = "25";

	private final static String SMTP_AUTH = "true";

	private final static String SSL_ENABLE = "false";

	/**
	 * 发送邮件（可自定义发送邮件服务器）
	 * 
	 * @param addressor
	 *            发件人
	 * @param smtpKey
	 *            发件人密码（有需要客户端授权码的服务器需要改为授权码）
	 * @param emailAddrs
	 *            收件人地址
	 * @param msg
	 *            邮件内容
	 * @param emailTitle
	 *            邮件标题
	 * @param fileList
	 *            邮件附件（若无传null）
	 * @param emailConfigModel
	 *            邮件服务器配置
	 * @throws AddressException
	 * @throws MessagingException
	 * @exception @createTime：2018年9月28日
	 * @author: fuxiang
	 */
	

	public static void sendEmail(String addressor, String smtpKey, List<String> emailAddrs, String msg,
			String emailTitle, List<File> fileList, EmailConfigDefine emailConfigModel)
			throws AddressException, MessagingException {
		Properties properties = new Properties();
		properties.put("mail.transport.protocol", emailConfigModel.getProtocol());// 连接协议
		properties.put("mail.smtp.host", emailConfigModel.getSmtpHost());// 主机名
		properties.put("mail.smtp.port", emailConfigModel.getSmtpPort());// 端口号
		properties.put("mail.smtp.auth", emailConfigModel.getSmtpAuth());
		properties.put("mail.smtp.ssl.enable", emailConfigModel.getSslEnable());// 设置是否使用ssl安全连接
		properties.put("mail.debug", "true");// 设置是否显示debug信息 true 会在控制台显示相关信息
		// 得到回话对象
		Session session = Session.getInstance(properties);
		// 获取邮件对象
		Message message = new MimeMessage(session);
		// 设置发件人邮箱地址
		message.setFrom(new InternetAddress(addressor));
		// 设置收件人邮箱地址
		List<InternetAddress> internetAddrs = new ArrayList<InternetAddress>();
		for (String emailAddr : emailAddrs) {
			internetAddrs.add(new InternetAddress(emailAddr));
		}
		InternetAddress[] addrs = new InternetAddress[internetAddrs.size()];
		message.setRecipients(Message.RecipientType.TO, internetAddrs.toArray(addrs));
		// 设置邮件标题
		message.setSubject(emailTitle);
		// 添加邮件的各个部分内容，包括文本内容和附件
		Multipart multipart = new MimeMultipart();

		// 添加邮件正文
		BodyPart contentPart = new MimeBodyPart();
		contentPart.setContent(msg, "text/html;charset=UTF-8");
		multipart.addBodyPart(contentPart);

		// 遍历添加附件
		if (fileList != null && fileList.size() > 0) {
			for (File file : fileList) {
				BodyPart attachmentBodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(file);
				attachmentBodyPart.setDataHandler(new DataHandler(source));
				attachmentBodyPart.setFileName(file.getName());
				multipart.addBodyPart(attachmentBodyPart);
			}
		}
		// 将多媒体对象放到message中
		message.setContent(multipart);
		// 保存邮件
		message.saveChanges();
		// 得到邮差对象
		Transport transport = session.getTransport();
		// 连接自己的邮箱账户
		transport.connect(addressor, smtpKey);// 密码为QQ邮箱开通的stmp服务后得到的客户端授权码
		// 发送邮件
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();

	}

	/**
	 * 默认公司邮箱服务器发送邮件（smtp-ent.21cn.com）
	 * 
	 * @param addressor
	 *            发件人邮箱
	 * @param smtpKey
	 *            发件人密码
	 * @param emailAddrs
	 *            收件人邮箱
	 * @param msg
	 *            邮件信息
	 * @param emailTitle
	 *            邮件标题
	 * @param fileList
	 *            邮件附件（若无传null）
	 * @throws AddressException
	 * @throws MessagingException
	 * @exception @createTime：2018年9月28日
	 * @author: fuxiang
	 */
	

	public static void sendEmail(String addressor, String smtpKey, List<String> emailAddrs, String msg,
			String emailTitle, List<File> fileList) throws AddressException, MessagingException {
		EmailConfigDefine emailConfigModel = new EmailConfigDefine();
		emailConfigModel.setProtocol(PROTOCOL);
		emailConfigModel.setSmtpAuth(SMTP_AUTH);
		emailConfigModel.setSmtpHost(SMTP_HOST);
		emailConfigModel.setSmtpPort(SMTP_PORT);
		emailConfigModel.setSslEnable(SSL_ENABLE);
		sendEmail(addressor, smtpKey, emailAddrs, msg, emailTitle, fileList, emailConfigModel);
	}

}
