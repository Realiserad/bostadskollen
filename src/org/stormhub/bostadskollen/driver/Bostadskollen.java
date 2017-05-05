package org.stormhub.bostadskollen.driver;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

import org.stormhub.bostadskollen.data.Apartment;
import org.stormhub.bostadskollen.data.Subscription;
import org.stormhub.bostadskollen.db.Db4oWrapper;
import org.stormhub.bostadskollen.db.DummySubscriptionDAO;
import org.stormhub.bostadskollen.db.MongoSubscriptionDAO;
import org.stormhub.bostadskollen.db.SubscriptionDAO;
import org.stormhub.bostadskollen.logic.ApartmentDiffChecker;
import org.stormhub.bostadskollen.logic.ApartmentQueue;
import org.stormhub.bostadskollen.mail.MailFormatter;
import org.stormhub.bostadskollen.mail.MailService;
import org.stormhub.bostadskollen.mail.Pair;

import com.db4o.ObjectContainer;

import freemarker.template.TemplateException;

/**
 * This class contains the entry point of the program.
 * When started, you should pass two parameters as follows:
 * --db 	Path to db4o database file containing any apartments loaded during the previous
 * 			execution of the program. Will also be used to write new apartments found in the 
 * 			apartment queue.
 * --ftl 	Path to an ftl template used to create an html formatted email which will
 * 			be send to any subscribers with matching subscription filters.
 * @author Realiserad
 */
public class Bostadskollen {
	public final static String feed = "https://bostad.stockholm.se/Lista/AllaAnnonser";
	
	public static void main(String args[]) {
		try {
			final Arguments arguments = Arguments.parse(Arrays.asList(args));
			new Bostadskollen().
				run(new MailFormatter(MailFormatter.getDefaultConfiguration().
						getTemplate(arguments.getMailTemplatePath())), 
					new MailService(), 
					arguments.isDryRun() ? new DummySubscriptionDAO() : new MongoSubscriptionDAO(), 
					new Db4oWrapper(arguments.getDatabasePath()), 
					new ApartmentQueue((HttpURLConnection) new URL(feed).openConnection()));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
	
	private void sendMail(final Apartment apartment, final Subscription subscription, 
			final MailFormatter mailFormatter, final MailService mailService) {
		try {
			final String mailBody = mailFormatter.
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
					setUnsubscribeLink(String.format("http://bostadskollen.h4ck.me/a/unsubscribe.php?id=%s", subscription.getSubscriptionId())).
					getString();
			mailService.sendMail(subscription, "Ny bostad", mailBody);
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Set<Apartment> updateApartmentDb(final Db4oWrapper db4o, final ApartmentQueue apartmentQueue) throws IOException {
		ObjectContainer db = null;
		try {
			db = db4o.getContainer();
			final Set<Apartment> previousApartments = db4o.asSet(db.queryByExample(Apartment.class));
			System.out.println(String.format("Loaded %d apartments from database.", previousApartments.size()));
			final Set<Apartment> currentApartments = apartmentQueue.
					fetchApartments(System.currentTimeMillis());
			final Set<Apartment> newApartments = new ApartmentDiffChecker().
					getNewApartments(previousApartments, currentApartments);
			System.out.println(String.format("Detected %d new apartments out of %d available.", 
					newApartments.size(), currentApartments.size()));
			for (Apartment newApartment : newApartments) {
				db.store(newApartment);
			}
			return newApartments;
		}
		finally {
			if (db != null)
				db.close();
		}
	}
	
	private boolean hasExpired(final Date date) {
		return date.compareTo(new Date()) < 0;
	}
	
	public void run(final MailFormatter mailFormatter, final MailService mailService,
			final SubscriptionDAO subscriptionDAO, final Db4oWrapper db4o, final ApartmentQueue apartmentQueue) throws IOException {
		final Set<Apartment> newApartments = updateApartmentDb(db4o, apartmentQueue);
		final Set<Subscription> subscriptions = subscriptionDAO.getSubscriptions();
		for (Subscription subscription : subscriptions) {
			if (hasExpired(subscription.getExpirationDate())) {
				subscriptionDAO.deleteSubscription(subscription);
				continue;
			}
			for (Apartment apartment : newApartments) {
				if (subscription.getSubscriptionFilter().matches(apartment)) {
					sendMail(apartment, subscription, mailFormatter, mailService);
				}
			}
		}
	}
}