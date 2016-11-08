package org.stormhub.bostadskollen.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.db4o.config.annotations.Indexed;

public class Apartment {
	@Indexed
	private final String id;
	@Indexed
	private long revision;
	private final Date publicationDate;
	private final Date expirationDate;
	private final String roomCount;
	private final String muncipality;
	private final String district;
	private final String rent;
	private final String size;
	private final String address;
	private final List<String> categories;
	
	public Apartment(final String id) {
		this(null, null, null, null, null, null, null, null, null, id, 0);
	}
	
	public Apartment(final String address, final List<String> categories, final String muncipality,
			final String district, final Date expirationDate, final Date publicationDate, final String rent, 
			final String roomCount, final String size, final String id, final long revision) {
		this.address = address;
		this.categories = categories == null ? null : new ArrayList<String>(categories);
		this.muncipality = muncipality;
		this.district = district;
		this.expirationDate = expirationDate;
		this.publicationDate = publicationDate;
		this.rent = rent;
		this.roomCount = roomCount;
		this.size = size;
		this.id = id;
		this.revision = revision;
	}

	public Date getPublicationDate() {
		return publicationDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public String getRoomCount() {
		return roomCount;
	}

	public String getMuncipality() {
		return muncipality;
	}

	public String getDistrict() {
		return district;
	}

	public String getRent() {
		return rent;
	}
	
	public String getAddress() {
		return address;
	}

	public String getSize() {
		return size;
	}

	public List<String> getCategories() {
		return categories == null ? null : new ArrayList<String>(categories);
	}

	public String getUrl() {
		return String.format("https://bostad.stockholm.se/Lista/details/?aid=%s", id);
	}
	
	public String getId() {
		return id;
	}
	
	public long getRevision() {
		return revision;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object == null || !(object instanceof Apartment)) {
			return false;
		}
		final Apartment apartment = (Apartment) object;
		return id.equals(apartment.getId());
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public String toString() {
		if (address == null || address.isEmpty())
			return id;
		return String.format("%s (%s)", address, id);
	}
}
