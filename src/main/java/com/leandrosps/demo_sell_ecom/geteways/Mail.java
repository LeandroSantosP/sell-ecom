package com.leandrosps.demo_sell_ecom.geteways;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class Mail {

	private JavaMailSender sender;

	public Mail(JavaMailSender sender) {
		this.sender = sender;
	}

	public void send( String to,String subject, String text){
		try {
			var message = this.sender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(text,true);
			this.sender.send(message);
		} catch (MessagingException e) {
			log.error("HERe: "+e.getMessage(), e);
			e.printStackTrace();
		}
	}

}
