package com.saiteja.authentication.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
	
	private final JavaMailSender mailSender;
	
	public void sendOtpEmail(String to, String name, String otp) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			
			helper.setTo(to);
			helper.setSubject("Your OTP Code");
			helper.setText(
					"<h3>Hello " + name + ",</h3>" +
					"<p>Your OTP code is: <b>" + otp + "</b></p>" +
					"<p>This code is valid for 5 minutes.</p>",
					true
					);
			
			mailSender.send(message);
		} catch (Exception e) {
			throw new RuntimeException("Failed to send OTP email", e);
		}
	}

}
