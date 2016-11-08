package org.stormhub.bostadskollen.test;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.stormhub.bostadskollen.data.Apartment;
import org.stormhub.bostadskollen.data.Subscription;
import org.stormhub.bostadskollen.data.SubscriptionFilter;
import org.stormhub.bostadskollen.db.Db4oWrapper;
import org.stormhub.bostadskollen.db.SubscriptionDAO;
import org.stormhub.bostadskollen.driver.Bostadskollen;
import org.stormhub.bostadskollen.logic.ApartmentQueue;
import org.stormhub.bostadskollen.mail.MailFormatter;
import org.stormhub.bostadskollen.mail.MailService;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

import freemarker.template.TemplateException;

public class BostadskollenTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testBostadskollen() throws ParseException, IOException, TemplateException {
		final MailFormatter mailFormatter = mockMailFormatter();
		final MailService mailService = mock(MailService.class);
		
		final List<Subscription> subscriptions = getSubscriptions();
		final SubscriptionDAO subscriptionDAO = mock(SubscriptionDAO.class);
		when(subscriptionDAO.getSubscriptions()).thenReturn(new HashSet<Subscription>(subscriptions));
		
		final Db4oWrapper db4o = mock(Db4oWrapper.class);
		final ObjectContainer objectContainer = mock(ObjectContainer.class);
		when(objectContainer.queryByExample(any(Apartment.class))).thenReturn(mock(ObjectSet.class));
		when(db4o.getContainer()).thenReturn(objectContainer);
		
		final ApartmentQueue apartmentQueue = mock(ApartmentQueue.class);
		when(apartmentQueue.fetchApartments(anyLong())).thenReturn(getApartments());
		
		new Bostadskollen().run(mailFormatter, mailService, subscriptionDAO, db4o, apartmentQueue);
		
		verify(objectContainer).store(any());
		verify(mailService).sendMail(eq(subscriptions.get(1)), anyString(), anyString());
		verify(subscriptionDAO).deleteSubscription(subscriptions.get(0));
	}
	
	private MailFormatter mockMailFormatter() throws IOException, TemplateException {
		final MailFormatter mailFormatter = mock(MailFormatter.class);
		when(mailFormatter.setLink(anyString())).thenReturn(mailFormatter);
		when(mailFormatter.setProperties(any())).thenReturn(mailFormatter);
		when(mailFormatter.setText(anyString())).thenReturn(mailFormatter);
		when(mailFormatter.setTitle(anyString())).thenReturn(mailFormatter);
		when(mailFormatter.setUnsubscribeLink(anyString())).thenReturn(mailFormatter);
		when(mailFormatter.getString()).thenReturn("");
		return mailFormatter;
	}
	
	private Set<Apartment> getApartments() throws ParseException {
		final Set<Apartment> apartments = new HashSet<>();
		apartments.add(new Apartment("Studentgatan 4A", 
			getList("Student"), 
			"Sundbyberg", 
			"", 
			getDate("2016-01-04"),
			getDate("2016-01-01"),
			"3000",
			"1",
			"19",
			"id-1",
			252242
		));
		return apartments;
	}

	private List<Subscription> getSubscriptions() throws ParseException {
		final List<Subscription> subscriptions = new ArrayList<>();
		subscriptions.add(new Subscription("11111111111111111111111111111111", 
			"apartment1@example.se", 
			getDate("2000-01-01"), // expired
			new SubscriptionFilter(
					getList("Bostadssnabben", "Student", "Ungdom"), 
					getList("Stockholm", "Sundbyberg", "Solna"),
					4500
			)
		));
		subscriptions.add(new Subscription("22222222222222222222222222222222",
			"apartment2@example.se",
			getDate("2080-01-01"), // not expired
			new SubscriptionFilter(
					getList("Bostadssnabben", "Student", "Ungdom"), 
					getList("Stockholm", "Sundbyberg", "Solna"),
					4500
			)
		));
		subscriptions.add(new Subscription("33333333333333333333333333333333",
			"apartment3@example.se",
			getDate("2080-01-01"), // not expired
			new SubscriptionFilter( // but filter should not match
					getList("Bostadssnabben"), 
					getList("Stockholm", "Sundbyberg", "Solna"),
					4500
			)
		));
		return subscriptions;
	}
	
	private List<String> getList(String... args) {
		return new ArrayList<String>(Arrays.asList(args));
	}
	
	private Date getDate(final String str) throws ParseException {
		return new SimpleDateFormat("yyyy-MM-dd").parse(str);
	}
}
