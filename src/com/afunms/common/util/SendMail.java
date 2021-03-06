package com.afunms.common.util;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.afunms.initialize.ResourceCenter;

public class SendMail {
	/**
	 * 邮件发送测试main
	 * 
	 * @param args
	 */

	public static void sendMail(String toAddr, String subject, String body, String fromAddr, Address[] ccAddress) throws RemoteException {
		try {
			try {
				Properties props = new Properties();
				props.put("mail.smtp.host", "smtp.sohu.com"); // set host
				props.put("mail.smtp.auth", "true"); // set auth
				MyAuthenticator auth = new MyAuthenticator("donhukelei", "hukelei");
				Session sendMailSession = Session.getInstance(props, auth);
				sendMailSession.setDebug(true);

				MimeMessage message = new MimeMessage(sendMailSession);
				// 设置发信人
				message.setFrom(new InternetAddress(fromAddr));
				// 收信人
				message.setRecipient(Message.RecipientType.TO, new InternetAddress(toAddr));
				// 邮件标题
				message.setSubject(subject);
				message.setSentDate(new Date());
				MimeMultipart multi = new MimeMultipart();
				BodyPart textBodyPart = new MimeBodyPart(); // 第一个BodyPart.主要写一些一般的信件内容。
				textBodyPart.setText("详情见附件");
				multi.addBodyPart(textBodyPart);
				FileWriter fw = new FileWriter("aaa.html");
				PrintWriter pw = new PrintWriter(fw);
				pw.println(body);
				pw.close();
				fw.close();

				// tempFile.

				FileDataSource fds = new FileDataSource("aaa.html"); // 必须存在的文档，否则throw异常。
				BodyPart fileBodyPart = new MimeBodyPart(); // 第二个BodyPart
				fileBodyPart.setDataHandler(new DataHandler(fds)); // 字符流形式装入文件
				fileBodyPart.setFileName("fujian.html"); // 设置文件名，可以不是原来的文件名。
				// 不讲了，同第一个BodyPart.
				multi.addBodyPart(fileBodyPart);
				// MimeMultPart作为Content加入message
				message.setContent(multi);
				// 所有以上的工作必须保存。
				message.saveChanges();
				// 发送，利用Transport类，它是SMTP的邮件发送协议，
				Transport transport = sendMailSession.getTransport("smtp");
				transport.connect("smtp.163.com", "rhythm333", "hukelei");
				transport.sendMessage(message, message.getAllRecipients());
				// transport.send(message);
				transport.close();

			} catch (Exception exc) {
				exc.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sendMyMail(String toAddr, String subject, String body, String fromAddr, Address[] ccAddress) {
		try {
			try {
				Properties props = new Properties();
				props.put("mail.smtp.host", "smtp.sina.com.cn"); // set host
				props.put("mail.smtp.auth", "true"); // set auth
				MyAuthenticator auth = new MyAuthenticator("supergzm", "6400891gzm");
				Session sendMailSession = Session.getInstance(props, auth);
				sendMailSession.setDebug(true);

				// 创建 邮件的message，message对象包含了邮件众多有的部件，都是封装成了set方法去设置的
				MimeMessage message = new MimeMessage(sendMailSession);
				// 设置发信人
				message.setFrom(new InternetAddress(fromAddr));
				// 收信人
				message.setRecipient(Message.RecipientType.TO, new InternetAddress(toAddr));
				// 邮件标题
				message.setSubject(subject);
				message.setSentDate(new Date());
				MimeMultipart multi = new MimeMultipart();
				BodyPart textBodyPart = new MimeBodyPart(); // 第一个BodyPart.主要写一些一般的信件内容。
				textBodyPart.setText("详情见附件");
				multi.addBodyPart(textBodyPart);
				FileWriter fw = new FileWriter("aaa.html");
				PrintWriter pw = new PrintWriter(fw);
				pw.println(body);
				pw.close();
				fw.close();

				FileDataSource fds = new FileDataSource("aaa.html"); // 必须存在的文档，否则throw异常。
				BodyPart fileBodyPart = new MimeBodyPart(); // 第二个BodyPart
				fileBodyPart.setDataHandler(new DataHandler(fds)); // 字符流形式装入文件
				fileBodyPart.setFileName("fujian.html"); // 设置文件名，可以不是原来的文件名。
				multi.addBodyPart(fileBodyPart);
				message.setContent(multi);
				message.saveChanges();
				Transport transport = sendMailSession.getTransport("smtp");
				transport.connect("smtp.163.com", "rhythm333", "hukelei");
				transport.sendMessage(message, message.getAllRecipients());
				transport.close();

			} catch (Exception exc) {
				exc.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送邮件网关
	 */
	private String mailaddress;
	/**
	 * 接收邮件网关
	 */
	private String receiveAddress;
	private String sendmail;
	private String sendpasswd;
	private String toAddr;
	private String subject;

	private String body;

	private String fromAddr;

	private Address[] ccAddress;

	public SendMail() {
	}

	public SendMail(String mailaddress, String sendmail, String sendpasswd, String toAddr, String subject, String body, String fromAddr, Address[] ccAddress) {
		super();
		this.mailaddress = mailaddress;
		this.sendmail = sendmail;
		this.sendpasswd = sendpasswd;
		this.toAddr = toAddr;
		this.subject = subject;
		this.body = body;
		this.fromAddr = fromAddr;
		this.ccAddress = ccAddress;

	}

	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @return the ccAddress
	 */
	public Address[] getCcAddress() {
		return ccAddress;
	}

	/**
	 * @return the fromAddr
	 */
	public String getFromAddr() {
		return fromAddr;
	}

	/**
	 * 根据邮件网关和用户名 ， 获取发件人的邮箱地址 如 hongli@dhcc.com.cn
	 * 
	 * @return
	 */
	private synchronized String getFromEmailAddress() {
		StringBuffer fromEmailAddress = new StringBuffer();
		fromEmailAddress.append(sendmail);
		String[] gateArrays = { "mail.", "pop3.", "smtp.", "exchange.", "pop." };
		String tempMainAddress = mailaddress;
		for (String gate : gateArrays) {
			tempMainAddress = tempMainAddress.replaceAll(gate, "");
		}
		fromEmailAddress.append("@");
		fromEmailAddress.append(tempMainAddress);
		return fromEmailAddress.toString();
	}

	/**
	 * @return the mailaddress
	 */
	public String getMailaddress() {
		return mailaddress;
	}

	public String getReceiveAddress() {
		return receiveAddress;
	}

	/**
	 * @return the sendmail
	 */
	public String getSendmail() {
		return sendmail;
	}

	/**
	 * @return the sendpasswd
	 */
	public String getSendpasswd() {
		return sendpasswd;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @return the toAddr
	 */
	public String getToAddr() {
		return toAddr;
	}

	/**
	 * 
	 * @param recipients
	 *            邮件接收人，如"konglingqi@dhcc.com.cn"
	 * @param subject
	 *            邮件主题
	 * @param message
	 *            邮件内容
	 * @param from
	 *            邮件发送人地址，如"guzhiming@dhcc.com.cn"
	 * @param emailUserName
	 *            发邮件方 登录邮箱 所使用的用户名，如"guzhiming@dhcc.com.cn"
	 * @param emailPwd
	 *            发邮件方 登录邮箱 所使用的用户名 对应的密码
	 * @param smtpHost
	 *            邮箱smtp地址，如果使用dhcc邮箱发送邮件，则该参数填"mail.dhcc.com.cn"
	 * @throws MessagingException
	 */
	public void postMail(String recipients[], String subject, String message, String from, String emailUserName, String emailPwd, String smtpHost, String fileName)
			throws MessagingException {
		try {
			boolean debug = false;

			// Set the host smtp address
			Properties props = new Properties();
			props.put("mail.smtp.host", smtpHost);
			props.put("mail.smtp.auth", "true");

			Authenticator auth = new SMTPAuthenticator(emailUserName, emailPwd);
			Session session = Session.getDefaultInstance(props, auth);

			session.setDebug(debug);

			// create a message
			MimeMessage msg = new MimeMessage(session);
			msg.setSubject(subject, "GB2312");
			// set the from and to address
			InternetAddress addressFrom = new InternetAddress(from);
			msg.setFrom(addressFrom);

			InternetAddress[] addressTo = new InternetAddress[recipients.length];
			for (int i = 0; i < recipients.length; i++) {
				addressTo[i] = new InternetAddress(recipients[i]);
			}
			msg.setRecipients(Message.RecipientType.TO, addressTo);

			MimeMultipart multi = new MimeMultipart();
			MimeBodyPart textBodyPart = new MimeBodyPart(); // 第一个BodyPart.主要写一些一般的信件内容。
			textBodyPart.setContent(message, "text/plain;charset=GB2312");
			multi.addBodyPart(textBodyPart);

			if (fileName != null) {
				FileDataSource fds = new FileDataSource(fileName); // 必须存在的文档，否则throw异常。
				BodyPart fileBodyPart = new MimeBodyPart(); // 第二个BodyPart
				fileBodyPart.setDataHandler(new DataHandler(fds)); // 字符流形式装入文件
				fileBodyPart.setFileName("report.xls"); // 设置文件名，可以不是原来的文件名。
				multi.addBodyPart(fileBodyPart);
			}

			// MimeMultPart作为Content加入message
			msg.setContent(multi, "text/plain;charset=gb2312");
			msg.saveChanges();
			Transport.send(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean sendmail() {
		try {
			Properties props = new Properties();
			// 设置邮件服务器
			props.put("mail.smtp.host", mailaddress);// dhcc.com.cn
			// 设置登录凭证
			props.put("mail.smtp.auth", "true");
			MyAuthenticator auth = new MyAuthenticator(sendmail, sendpasswd);
			Session sendMailSession = Session.getInstance(props, auth);

			MimeMessage message = new MimeMessage(sendMailSession);
			// 设置发信人
			String fromEmailAddress = getFromEmailAddress();
			message.setFrom(new InternetAddress(fromEmailAddress));
			// 收信人
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(toAddr));
			// 邮件标题
			message.setSubject(subject);
			message.setSentDate(new Date());
			MimeMultipart multi = new MimeMultipart();
			BodyPart textBodyPart = new MimeBodyPart();
			textBodyPart.setText(body);
			multi.addBodyPart(textBodyPart);

			FileDataSource fds = new FileDataSource(ResourceCenter.getInstance().getSysPath() + "/ftpupload/ftpupload.txt");
			BodyPart fileBodyPart = new MimeBodyPart();
			fileBodyPart.setDataHandler(new DataHandler(fds));
			fileBodyPart.setFileName("alarm.txt");
			multi.addBodyPart(fileBodyPart);
			message.setContent(multi);

			message.saveChanges();
			Transport.send(message);
		} catch (Exception exc) {
			exc.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean sendmailNoFile() {
		try {
			this.postMail(new String[] { toAddr }, subject, body, fromAddr, sendmail, sendpasswd, mailaddress, null);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean sendmailWithFile(String fileName) {
		try {
			this.postMail(new String[] { toAddr }, subject, body, fromAddr, sendmail, sendpasswd, mailaddress, fileName);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @param body
	 *            the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @param ccAddress
	 *            the ccAddress to set
	 */
	public void setCcAddress(Address[] ccAddress) {
		this.ccAddress = ccAddress;
	}

	/**
	 * @param fromAddr
	 *            the fromAddr to set
	 */
	public void setFromAddr(String fromAddr) {
		this.fromAddr = fromAddr;
	}

	/**
	 * @param mailaddress
	 *            the mailaddress to set
	 */
	public void setMailaddress(String mailaddress) {
		this.mailaddress = mailaddress;
	}

	public void setReceiveAddress(String receiveAddress) {
		this.receiveAddress = receiveAddress;
	}

	/**
	 * @param sendmail
	 *            the sendmail to set
	 */
	public void setSendmail(String sendmail) {
		this.sendmail = sendmail;
	}

	/**
	 * @param sendpasswd
	 *            the sendpasswd to set
	 */
	public void setSendpasswd(String sendpasswd) {
		this.sendpasswd = sendpasswd;
	}

	/**
	 * @param subject
	 *            the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @param toAddr
	 *            the toAddr to set
	 */
	public void setToAddr(String toAddr) {
		this.toAddr = toAddr;
	}
}
