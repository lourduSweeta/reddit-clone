package com.example.redditclone.service;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.redditclone.exception.SpringRedditException;
import com.example.redditclone.model.NotificationEmail;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {
	
	private final JavaMailSender mailSender;
	private final MailContentBuilder mailContentBuilder;
	
	@Async
	public void sendMail(NotificationEmail notificationEmail)
	{
		MimeMessagePreparator messagePreparator = mimeMessage -> {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
			mimeMessageHelper.setFrom("redditClone@email.com");
			mimeMessageHelper.setTo(notificationEmail.getRecipient());
			mimeMessageHelper.setSubject(notificationEmail.getSubject());
			mimeMessageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()));
		};
		
		try {
			mailSender.send(messagePreparator);
			log.info("Activation mail sent !");
		}
		catch (MailException exe) {
			throw new SpringRedditException ("Exception occured when sending mail to "+notificationEmail.getRecipient());
		}
		
	}

}
