package org.stormhub.bostadskollen.mail;

import java.io.IOException;

import org.stormhub.bostadskollen.data.Subscription;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

public class MailService {
	private final String API_KEY = "";
	private final String SENDER = "bulk-%s@stormhub.org";
	
	public void sendMail(final Subscription subscription, final String subject, final String html) {
		try {
			final Email from = new Email(String.format(SENDER, subscription.getSubscriptionId().substring(0, 6)));
		    final Email to = new Email(subscription.getEmail());
		    final Content content = new Content("text/html", html);
		    final Mail mail = new Mail(from, subject, to, content);
		    final SendGrid sg = new SendGrid(API_KEY);
		    final Request request = new Request();
		    request.method = Method.POST;
		    request.endpoint = "mail/send";
		    request.body = mail.build();
		    final Response response = sg.api(request);
		    System.out.println(String.format("Sent mail to %s (%d).", subscription.getEmail(), response.statusCode));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
