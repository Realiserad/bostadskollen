package org.stormhub.bostadskollen.test;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;
import org.stormhub.bostadskollen.data.Apartment;
import org.stormhub.bostadskollen.logic.ApartmentDiffChecker;

public class ApartmentDiffCheckerTest {

	@Test
	public void test2NewApartmentsFound() throws IOException {
		final HashSet<Apartment> apartmentsBefore = new HashSet<Apartment>();
		final HashSet<Apartment> apartmentsAfter = new HashSet<Apartment>();
		apartmentsBefore.add(new Apartment("1"));
		apartmentsBefore.add(new Apartment("2"));
		apartmentsAfter.add(new Apartment("2"));
		apartmentsAfter.add(new Apartment("3"));
		apartmentsAfter.add(new Apartment("4"));
		final List<Apartment> newApartments = new ApartmentDiffChecker().
				getNewApartments(apartmentsBefore, apartmentsAfter).
				stream().
				sorted((a, b) -> a.getId().compareTo(b.getId())).
				collect(Collectors.toList());
		Assert.assertEquals(2, newApartments.size());
		Assert.assertEquals("3", newApartments.get(0).getId());
		Assert.assertEquals("4", newApartments.get(1).getId());
	}
}
