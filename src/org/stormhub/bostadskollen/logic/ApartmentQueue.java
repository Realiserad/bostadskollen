package org.stormhub.bostadskollen.logic;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.stormhub.bostadskollen.data.Apartment;
import org.stormhub.bostadskollen.driver.Bostadskollen;

public class ApartmentQueue {
	private final Connection connection;
	
	public ApartmentQueue(Connection connection) {
		this.connection = connection;
	}
	
	private List<String> getCategories(final Element ad) {
		final List<String> categories = new ArrayList<String>();
		if (ad.attr("data-lagenhetstyp-vanlig").equals("True")) categories.add("Vanlig");
		if (ad.attr("data-lagenhetstyp-bostadssnabben").equals("True")) categories.add("Bostadssnabben");
		if (ad.attr("data-lagenhetstyp-korttid").equals("True")) categories.add("Korttid");
		if (ad.attr("data-lagenhetstyp-ungdom").equals("True")) categories.add("Ungdom");
		if (ad.attr("data-lagenhetstyp-student").equals("True")) categories.add("Student");
		if (ad.attr("data-lagenhetstyp-senior").equals("True")) categories.add("Senior");
		if (ad.attr("data-lagenhetstyp-nyproduktion").equals("True")) categories.add("Nyproduktion");
		return categories;
	}
	
	private Date parseTimestamp(final String date) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
				parse(date.replace('T', ' ').substring(0, 19));
		} catch (ParseException e) {
			return null;
		}
	}
	
	public Set<Apartment> fetchApartments(final long revision) throws IOException {
		System.out.println(String.format("Fetching apartments from feed %s", Bostadskollen.feed));
		final Document doc = connection.get();
		System.out.println(String.format("Loaded source document with %s bytes.", doc.toString().length()));
		final Set<Apartment> apartments = new HashSet<Apartment>();
		final Element ads = doc.select("#annons-lista").get(0);
		final Elements entries = ads.select(".m-apartment-card");
		for (Element ad : entries) {
			final String address = ad.select("div[class=m-apartment-card__address]").
					get(0).text().replace(" – ", "");
			final List<String> categories = getCategories(ad);
			final String city = ad.attr("data-kommun");
			final String district = ad.attr("data-stadsdel");
			final Date expirationDate = parseTimestamp(ad.attr("data-annonserad-till"));
			final Date publicationDate = parseTimestamp(ad.attr("data-annonserad-fran"));
			final String rent = ad.attr("data-hyra");
			final String roomCount = ad.attr("data-antal-rum");
			final String size = ad.attr("data-yta");
			final String id = ad.attr("data-id");
			apartments.add(new Apartment(address, categories, city, district, expirationDate, publicationDate, 
					rent, roomCount, size, id, revision));
		}
		
		return apartments;
	}
}
