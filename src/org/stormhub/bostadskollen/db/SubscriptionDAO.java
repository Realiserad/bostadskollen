package org.stormhub.bostadskollen.db;

import java.util.Set;

import org.stormhub.bostadskollen.data.Subscription;

public interface SubscriptionDAO {
	Set<Subscription> getSubscriptions();
	void deleteSubscription(Subscription subscription);
}
