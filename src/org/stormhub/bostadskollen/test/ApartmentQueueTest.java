package org.stormhub.bostadskollen.test;

import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.junit.Test;
import org.mockito.Mockito;
import org.stormhub.bostadskollen.data.Apartment;
import org.stormhub.bostadskollen.logic.ApartmentQueue;
import org.junit.Assert;

public class ApartmentQueueTest {
	
	@Test
	public void testFetch96Apartments() throws IOException {
		final Connection connection = Mockito.mock(Connection.class);
		when(connection.get()).thenReturn(Jsoup.parse(
				IOUtils.toString(getClass().
						getClassLoader().
						getResourceAsStream("res/lista.txt"))));
		final ApartmentQueue apartmentQueue = new ApartmentQueue(connection);
		final Set<Apartment> apartments = apartmentQueue.fetchApartments(1);
		Assert.assertEquals(96, apartments.size());
		final List<Apartment> samples = apartments.stream().
				filter(a -> "125777".equals(a.getId())).
				collect(Collectors.toList());
		Assert.assertEquals(1, samples.size());
		Assert.assertEquals("3417", samples.get(0).getRent());
		Assert.assertEquals("20", samples.get(0).getSize());
		Assert.assertEquals("1", samples.get(0).getRoomCount());
		Assert.assertEquals(1, samples.get(0).getCategories().size());
		Assert.assertEquals("Student", samples.get(0).getCategories().get(0));
		Assert.assertEquals("Botkyrka", samples.get(0).getMuncipality());
		Assert.assertEquals("Tullinge", samples.get(0).getDistrict());
		Assert.assertEquals("2016-09-18", new SimpleDateFormat("yyyy-MM-dd").
				format(samples.get(0).getExpirationDate()).
				toString());
		Assert.assertEquals("2016-09-15", new SimpleDateFormat("yyyy-MM-dd").
				format(samples.get(0).getPublicationDate()).
				toString());
		Assert.assertEquals("https://bostad.stockholm.se/Lista/details/?aid=125777", samples.get(0).getUrl());
		Assert.assertEquals("Kanslivägen 13", samples.get(0).getAddress());
		Assert.assertEquals(1, samples.get(0).getRevision());
	}
}
