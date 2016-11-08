package org.stormhub.bostadskollen.logic;

import java.util.HashSet;
import java.util.Set;

import org.stormhub.bostadskollen.data.Apartment;

public class ApartmentDiffChecker {
	
	public Set<Apartment> getNewApartments(final Set<Apartment> previous, final Set<Apartment> next) {
		Set<Apartment> newApartments = new HashSet<Apartment>();
		for (Apartment apartment : next) {
			if (!previous.contains(apartment))
				newApartments.add(apartment);
		}
		return newApartments;
	}
}
