package org.stormhub.bostadskollen.data;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionFilter {
	private final List<String> categories;
	private final List<String> muncipalities;
	private final int maxRent;
	
	public SubscriptionFilter(final List<String> categories, final List<String> muncipalities, final int maxRent) {
		this.categories = new ArrayList<String>(categories);
		this.muncipalities = new ArrayList<String>(muncipalities);
		this.maxRent = maxRent;
	}
	
	public List<String> getMuncipalities() {
		return new ArrayList<String>(muncipalities);
	}
	
	public List<String> getCategories() {
		return new ArrayList<String>(categories);
	}
	
	public int getMaxRent() {
		return maxRent;
	}
	
	private int asInteger(String value) {
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	public boolean matches(final Apartment apartment) {
		boolean categoryMatch = categories.stream().
				anyMatch(category -> apartment.getCategories().contains(category));
		boolean muncipalityMatch = muncipalities.contains(apartment.getMuncipality());
		boolean rentMatch = asInteger(apartment.getRent()) <= maxRent;
		return categoryMatch && muncipalityMatch && rentMatch;
	}
}
