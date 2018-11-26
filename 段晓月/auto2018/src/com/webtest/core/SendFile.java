package com.webtest.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import com.webtest.utils.*;

public class SendFile {

	public void sendmail(String FilePath) throws Exception {
		//��ȡ�������Լ��ռ�������
		String smtp = ReadProperties.getPropertyValue("smtp");
		String sendMail = ReadProperties.getPropertyValue("send_mail");
		String receiveMail = ReadProperties.getPropertyValue("receive_mail");
		String mailPassword = ReadProperties.getPropertyValue("send_mail_password");
		// ����һ��Property�ļ�����
		Properties props = new Properties();
		// �����ʼ�����������Ϣ����������smtp��������
		props.put("mail.smtp.host", smtp);
		// ������Ҫ�����֤
		props.put("mail.smtp.auth", "true");
		// �����֤ʵ��
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				// �ڶ���������������QQ����smtp����Ȩ��
				return new PasswordAuthentication(sendMail, mailPassword + "");
			}
		});
		try {
			// ����һ��MimeMessage���ʵ������
			Message message = new MimeMessage(session);

			// ���÷����������ַ
			message.setFrom(new InternetAddress(sendMail));

			// �����ռ��������ַ
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiveMail));

			// �����ʼ�����
			message.setSubject("���Է����ʼ�");

			// ����һ��MimeBodyPart�Ķ����Ա��������
			BodyPart messageBodyPart1 = new MimeBodyPart();

			// �����ʼ���������
			messageBodyPart1.setText("������ʼ������Ĳ���");

			// ��������һ��MimeBodyPart�����Ա������������
			MimeBodyPart messageBodyPart2 = new MimeBodyPart();

			// �����ʼ��и����ļ���·��
			String filename = FilePath;

			// ����һ��datasource���󣬲������ļ�
			DataSource source = new FileDataSource(filename);

			// ����handler
			messageBodyPart2.setDataHandler(new DataHandler(source));

			// �����ļ�
			messageBodyPart2.setFileName("���Ա���.zip");

			// ����һ��MimeMultipart���ʵ������
			Multipart multipart = new MimeMultipart();

			// �������1����
			multipart.addBodyPart(messageBodyPart1);

			// �������2����
			multipart.addBodyPart(messageBodyPart2);
			// ��������
			message.setContent(multipart);
			// ���շ����ʼ�
			Transport.send(message);
			System.out.println("=====�ʼ��Ѿ�����=====");
		} catch (MessagingException e) {

			throw new RuntimeException(e);

		}
	}

}
