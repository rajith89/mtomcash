package com.ontag.mcash.admin.web.util;

import java.net.Authenticator;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;


@Component
public class MailHandler
{
	@Autowired
	private MailSender mailSender;
	
	@Autowired
	private JavaMailSender javamailSender;
 
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}
 
	public void sendMail(String from, String to, String subject, String msg) {
		SimpleMailMessage message = new SimpleMailMessage();
 
		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(msg);
		mailSender.send(message);	
	}
	public void sendHtmlMail(String from, String to, String subject, String msg, String cc) throws Exception{
		
		  MimeMessage mime = this.javamailSender.createMimeMessage();
		  MimeMessageHelper helper = new MimeMessageHelper(mime, true);
		  helper.setFrom(from);
		  helper.setTo(to);
		  helper.setSubject(subject);
		  helper.setCc(cc);

		    helper.setText( msg,true);
		    this.javamailSender.send(mime);
	}
	
public void sendHtmlMailNew(String from, String to, String subject, String msg, String cc) throws Exception{
	
	
	Properties props = new Properties();
	
	props.put("mail.smtp.auth", "true");
	props.put("mail.smtp.starttls.enable", "true");
	props.put("mail.smtp.host", "smtpout.secureserver.net");
	props.put("mail.smtp.port", "25");
	
	
	final String username = "mtomcashadmin@hsworld.net";
	final String password = "ontag123";
	
//	final String username = "MtoMCashAdmin@hsworld.net";
//	final String password = "ontag123";
	
	
	Session session = Session.getInstance(props,
			  new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			  });
		
// 
//		Properties props = new Properties();
//		props.put("mail.smtp.auth", "true");
//		props.put("mail.smtp.starttls.enable", "true");
//		props.put("mail.smtp.host", "smtpout.secureserver.net");
//		props.put("mail.smtp.port", "80");
 
//		Session session = Session.getInstance(props,
//		 new javax.mail.Authenticator() {
//			protected PasswordAuthentication getPasswordAuthentication() {
//				return new PasswordAuthentication(username, password);
//			}
//		  });
        
        try{
             MimeMessage message = new MimeMessage(session);
             message.setFrom(new InternetAddress(from));
             message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
             message.setSubject(subject);
             message.setContent(msg, "text/html" );
             MimeMessage mime = this.javamailSender.createMimeMessage();
   		  	 MimeMessageHelper helper = new MimeMessageHelper(message, true);
   		  	 helper.setFrom(from);
   		  	 helper.setTo(to);
   		  	 helper.setSubject(subject);
   		  	 
   		  	 helper.setText( msg,true);
   		  	 Transport.send(message);
             System.out.println("Sent message successfully11....");
             
          }catch (MessagingException mex) {
        	  mex.printStackTrace();
        	  throw new Exception(mex.getMessage());
          }
}
}