package com.tesigners.app.util;

import java.util.Properties;

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
 
public class SendMailTLS {
	private static String encodingOptions = "text/html; charset=UTF-8";
 
	public static void sendMail(String[] toList, String subject,String body) {
 
		final String username = "tesigner.lab@gmail.com";
		final String password = "qwerty@21";
 
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
 
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
 
		try {
 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(toList[0]));
			
			Multipart mpData = new MimeMultipart();
			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(body, "text/html");
			mpData.addBodyPart(htmlPart);
			
	        message.setHeader("Content-Type", encodingOptions);
			message.setSubject(subject);
			message.setContent(mpData);
			
			Transport.send(message);
 
			System.out.println("Done");
 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	static public String constructMessageBody( String name, String loginAction,String activationLink){
		String msgBody =null;
		
		 msgBody = "<div>Hi "+name+",<br><br><b>Welcome at Tesigners !</b><br><br>This is a confirmation email for your registration at Tesigners.com"
				+ "<br>You're just one click away from the exciting new adventure of creating your own designer Tees."
				+"<br><br><h3>Click on this activation link to activate your user account <a href='"+activationLink+"'>Activate my account</a></h3>"
				+"\n\nKindly ignore this email if not requested for the same.<br><br>Best Regards,<h4>The Tesigners Team, <br><a href='www.Tesigners.com'>www.Tesigners.com</a></h4><div>";
		    
		return msgBody;
	}
	
	
}