package com.leandrosps.demo_sell_ecom.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfiguration {

	@Value("${spring.mail.username}")
	private String emailUsername;

	@Value("${spring.mail.password}")
	private String appPassword;

	@Bean
	public JavaMailSender getJavaMailSender() {
		System.out.println("email: " + this.emailUsername);
		System.out.println("pass: " + this.appPassword);
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);
		mailSender.setUsername(this.emailUsername);
		mailSender.setPassword(this.appPassword);

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");

		return mailSender;
	}
}
