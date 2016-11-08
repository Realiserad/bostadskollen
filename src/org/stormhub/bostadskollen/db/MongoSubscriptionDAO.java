package org.stormhub.bostadskollen.db;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.stormhub.bostadskollen.data.Subscription;
import org.stormhub.bostadskollen.data.SubscriptionFilter;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;

public class MongoSubscriptionDAO implements SubscriptionDAO {
	private final MongoClient mongoClient;
	private final MongoDatabase db;
	
	public MongoSubscriptionDAO() {
		this.mongoClient = new MongoClient();
		this.db = mongoClient.getDatabase("bostadskollen");
	}

	@Override
	public Set<Subscription> getSubscriptions() {
		final MongoCollection<Document> documents = db.getCollection("subscriptions");
		final Set<Subscription> subscriptions = new HashSet<Subscription>();
		for (Document document : documents.find()) {
			final String subscriptionId = document.getString("subscriptionId");
			final String email = document.getString("email");
			final Date expirationDate = new Date((long)(document.getInteger("expirationDate")) * 1000); // convert timestamp to ms
			final List<String> muncipalities = Arrays.asList(document.getString("muncipalities").split(","));
			final List<String> categories = Arrays.asList(document.getString("categories").split(","));
			final int maxRent = parseInt(document.getString("maxRent"));
			final Subscription subscription = new Subscription(subscriptionId, email, expirationDate, 
					new SubscriptionFilter(categories, muncipalities, maxRent));
			subscriptions.add(subscription);
			System.out.println(subscription.toString());
		}
		return subscriptions;
	}
	
	private int parseInt(final String value) {
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return Integer.MAX_VALUE;
		}
	}

	@Override
	public void deleteSubscription(final Subscription subscription) {
		final DeleteResult deleteResult = db.getCollection("subscriptions").deleteOne(
				new Document("subscriptionId", subscription.getSubscriptionId()));
		if (deleteResult.getDeletedCount() == 0) {
			System.out.println(String.format("Failed to delete %s. No such record.", subscription.getSubscriptionId()));
		} else {
			System.out.println(String.format("Removed subscription %s for %s.", 
					subscription.getSubscriptionId(), subscription.getEmail()));
		}
	}
}
