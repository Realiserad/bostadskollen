package org.stormhub.bostadskollen.logic;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.stormhub.bostadskollen.data.Apartment;
import org.stormhub.bostadskollen.driver.Bostadskollen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ApartmentQueue {
	private final HttpURLConnection connection;
	
	public ApartmentQueue(final HttpURLConnection connection) {
		this.connection = connection;
	}
	
	private List<String> getCategories(final JsonObject ad) {
		final List<String> categories = new ArrayList<String>();
		if (ad.get("Vanlig").getAsBoolean()) categories.add("Vanlig");
		if (ad.get("Bostadssnabben").getAsBoolean()) categories.add("Bostadssnabben");
		if (ad.get("Korttid").getAsBoolean()) categories.add("Korttid");
		if (ad.get("Ungdom").getAsBoolean()) categories.add("Ungdom");
		if (ad.get("Student").getAsBoolean()) categories.add("Student");
		if (ad.get("Senior").getAsBoolean()) categories.add("Senior");
		if (ad.get("Nyproduktion").getAsBoolean()) categories.add("Nyproduktion");
		return categories;
	}
	
	private Date parseTimestamp(final String date) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(date);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public Set<Apartment> fetchApartments(final long revision) throws IOException {
		System.out.println(String.format("Fetching apartments from feed %s", Bostadskollen.feed));
		connection.connect();
		JsonParser jsonParser = new JsonParser();
		JsonElement root = jsonParser.parse(new InputStreamReader((InputStream) connection.getContent()));
		System.out.println(String.format("Loaded source document with %s bytes.", root.toString().length()));
		final Set<Apartment> apartments = new HashSet<Apartment>();
		for (JsonElement elem : root.getAsJsonArray()) {
			final JsonObject ad = elem.getAsJsonObject();
			final String address = ad.get("Gatuadress").getAsString();
			final List<String> categories = getCategories(ad);
			final String city = ad.get("Kommun").getAsString();
			final String district = ad.get("Stadsdel").getAsString();
			final Date expirationDate = parseTimestamp(ad.get("AnnonseradTill").getAsString());
			final Date publicationDate = parseTimestamp(ad.get("AnnonseradFran").getAsString());
			final String rent = ad.get("Hyra").isJsonNull() ? "" : ad.get("Hyra").getAsString();
			final String roomCount = String.valueOf(ad.get("AntalRum"));
			final String size = ad.get("Yta").isJsonNull() ? "" : ad.get("Yta").getAsString();
			final String id = ad.get("AnnonsId").getAsString();
			apartments.add(new Apartment(address, categories, city, district, expirationDate, publicationDate, 
					rent, roomCount, size, id, revision));
		}
		
		return apartments;
	}
}
