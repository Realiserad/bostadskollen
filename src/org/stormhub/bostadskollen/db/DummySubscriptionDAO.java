package org.stormhub.bostadskollen.db;

import java.util.HashSet;
import java.util.Set;

import org.stormhub.bostadskollen.data.Subscription;

public class DummySubscriptionDAO implements SubscriptionDAO {

	@Override
	public Set<Subscription> getSubscriptions() {
		return new HashSet<Subscription>();
	}

	@Override
	public void deleteSubscription(Subscription subscription) {
		System.out.println(String.format("Deleting subscription for %s.", subscription.getEmail()));
	}

}
