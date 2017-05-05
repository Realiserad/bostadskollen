package org.stormhub.bostadskollen.test;

import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.stormhub.bostadskollen.data.Apartment;
import org.stormhub.bostadskollen.logic.ApartmentQueue;

public class ApartmentQueueTest {
	
	@Test
	public void testFetch96Apartments() throws IOException {
		final HttpURLConnection connection = Mockito.mock(HttpURLConnection.class);
		when(connection.getContent()).thenReturn(getClass().
						getClassLoader().
						getResourceAsStream("res/lista.txt"));
		final ApartmentQueue apartmentQueue = new ApartmentQueue(connection);
		final Set<Apartment> apartments = apartmentQueue.fetchApartments(1);
		Assert.assertEquals(62, apartments.size());
		final List<Apartment> samples = apartments.stream().
				filter(a -> "128700".equals(a.getId())).
				collect(Collectors.toList());
		Assert.assertEquals(1, samples.size());
		Assert.assertEquals("4289", samples.get(0).getRent());
		Assert.assertEquals("23", samples.get(0).getSize());
		Assert.assertEquals("1", samples.get(0).getRoomCount());
		Assert.assertEquals(1, samples.get(0).getCategories().size());
		Assert.assertEquals("Student", samples.get(0).getCategories().get(0));
		Assert.assertEquals("Stockholm", samples.get(0).getMuncipality());
		Assert.assertEquals("Beckomberga", samples.get(0).getDistrict());
		Assert.assertEquals("2017-01-11", new SimpleDateFormat("yyyy-MM-dd").
				format(samples.get(0).getExpirationDate()).
				toString());
		Assert.assertEquals("2017-01-10", new SimpleDateFormat("yyyy-MM-dd").
				format(samples.get(0).getPublicationDate()).
				toString());
		Assert.assertEquals("https://bostad.stockholm.se/Lista/details/?aid=128700", samples.get(0).getUrl());
		Assert.assertEquals("Follingbogatan 18", samples.get(0).getAddress());
		Assert.assertEquals(1, samples.get(0).getRevision());
	}
}
