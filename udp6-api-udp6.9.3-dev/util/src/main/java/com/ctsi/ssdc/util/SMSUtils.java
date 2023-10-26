package com.ctsi.ssdc.util;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;

/**
 * 发送短信工具类
 * 
 * @author ctsi
 *
 */
public class SMSUtils {
	private final static String OPERATION_NAME = "sendMessage";

	private final static String MESSAGE_URL = "http://vip.ctinm.com:55001/VIPNMS/services/sendMessageService?wsdl&tdsourcetag=s_pctim_aiomsg";

	/**
	 * 可选接口发短信
	 * 
	 * @param sendMessageInfoModels
	 *            短信信息实体类
	 * @param operationName
	 *            操作名称
	 * @param messageUrl
	 *            发短信接口
	 * @throws ServiceException
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @return void
	 * @exception @createTime：2018年9月28日
	 * @author: fuxiang
	 */
	

	public static void sendSMSMessage(List<SendMessageInfoDefine> sendMessageInfoModels, String operationName,
			String messageUrl) throws ServiceException, MalformedURLException, RemoteException {
		Service service = new Service();
		Call call = (Call) service.createCall();
		call.setOperationName(operationName);
		call.setTargetEndpointAddress(new URL(messageUrl));
		StringBuilder temp = new StringBuilder();
		temp.append("<data>\n" + "	<messageInfos>\n");
		for (SendMessageInfoDefine sendInfo : sendMessageInfoModels) {
			temp.append("		<info>\n" + "			<sendName>" + sendInfo.getSendName() + "</sendName>\n"
					+ "			<sendPhone>" + sendInfo.getSendPhone() + "</sendPhone>\n" + "			<sendMessage>"
					+ sendInfo.getSendMessage() + "</sendMessage>\n" + "		</info>\n");
		}
		temp.append("	</messageInfos>\n" + "</data>");
		call.invoke(new Object[] { temp.toString() }); // 设置调用方法中的参数

	}

	/**
	 * 使用默认接口发短信
	 * http://vip.ctinm.com:55001/VIPNMS/services/sendMessageService?wsdl&tdsourcetag=s_pctim_aiomsg
	 * 
	 * @param sendMessageInfoModels
	 *            短信信息实体类
	 * @throws ServiceException
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @return void
	 * @exception @createTime：2018年9月28日
	 * @author: fuxiang
	 */
	

	public static void sendSMSMessage(List<SendMessageInfoDefine> sendMessageInfoModels)
			throws ServiceException, MalformedURLException, RemoteException {
		sendSMSMessage(sendMessageInfoModels, OPERATION_NAME, MESSAGE_URL);
	}


}
