package org.stormhub.bostadskollen.test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.stormhub.bostadskollen.data.Apartment;
import org.stormhub.bostadskollen.data.Subscription;
import org.stormhub.bostadskollen.mail.MailFormatter;
import org.stormhub.bostadskollen.mail.MailService;
import org.stormhub.bostadskollen.mail.Pair;

import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

/**
 * This class sends an email through SendGrid. To avoid spam, this
 * JUnit test has the @Test annotation commented out by default. 
 * Run manually to test the template and mail service.
 * @author Realiserad
 */
public class MailServiceTest {

	//@org.junit.Test
	public void testSendGridApi() throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
		final Apartment apartment = new Apartment("Exempelgatan 2", 
				new ArrayList<String>(), 
				"Stockholm", 
				"Kungsholmen",
				new Date(), 
				new Date(), 
				"3500", "1", 
				"25", 
				"id", 
				5L
		);
		final String mailBody = new MailFormatter(MailFormatter.getDefaultConfiguration().getTemplate("res/email.ftl")).
				setLink(apartment.getUrl()).
				setProperties(Arrays.asList(new Pair[] {
						new Pair("Adress", String.format("%s, %s", apartment.getAddress(), apartment.getDistrict())),
						new Pair("Yta", String.format("%s m<sup>2</sup>", apartment.getSize())),
						new Pair("Hyra", String.format("%s kronor i m&aring;naden", apartment.getRent())),
						new Pair("Antal rum", apartment.getRoomCount()),
						new Pair("Anm&auml;l intresse senast", new SimpleDateFormat("yyyy-MM-dd").
								format(apartment.getExpirationDate())),
				})).
				setText(String.format("En ny l&auml;genhet i %s har dykt upp i bostadsk&ouml;n.", apartment.getMuncipality())).
				setTitle("Ny bostad").
				setUnsubscribeLink("http://google.se").
				getString();
		final Subscription subscription = new Subscription("b80bb7740288fda1f201890375a60c8f", "realiserad@gmail.com", null, null);
		new MailService().sendMail(subscription, "Ny bostad", mailBody);
	}
}
