package org.stormhub.bostadskollen.data;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Subscription {
	private final String subscriptionId;
	private final String email;
	private final Date expirationDate;
	private final SubscriptionFilter subscriptionFilter;
	
	public Subscription(final String subscriptionId, final String email, final Date expirationDate, 
			final SubscriptionFilter subscriptionFilter) {
		this.subscriptionId = subscriptionId;
		this.email = email;
		this.expirationDate = expirationDate;
		this.subscriptionFilter = subscriptionFilter;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getSubscriptionId() {
		return subscriptionId;
	}
	
	public Date getExpirationDate() {
		return expirationDate;
	}
	
	public SubscriptionFilter getSubscriptionFilter() {
		return subscriptionFilter;
	}
	
	@Override
	public String toString() {
		return String.format(
				"id => %s, mail => %s, mun => %s, cat => %s, rent => %d, exp => %s",
				subscriptionId, 
				email, 
				subscriptionFilter.getMuncipalities(), 
				subscriptionFilter.getCategories(), 
				subscriptionFilter.getMaxRent(),
				new SimpleDateFormat("yyyy-MM-dd").format(expirationDate)
		);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Subscription)) {
			return false;
		}
		return subscriptionId.equals(((Subscription) o).getSubscriptionId());
	}
	
	@Override
	public int hashCode() {
		return subscriptionId.hashCode();
	}
}
