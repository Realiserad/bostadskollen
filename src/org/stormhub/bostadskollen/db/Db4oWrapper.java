package org.stormhub.bostadskollen.db;

import java.util.HashSet;
import java.util.Set;

import org.stormhub.bostadskollen.data.Apartment;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

public class Db4oWrapper {
	private final String dbPath;
	
	public Db4oWrapper(final String dbPath) {
		this.dbPath = dbPath;
	}
	
	public ObjectContainer getContainer() {
		return Db4oEmbedded.openFile(dbPath);
	}
	
	public Set<Apartment> asSet(ObjectSet<Apartment> in) {
		if (in == null || in.size() == 0) {
			return new HashSet<Apartment>();
		}
		return new HashSet<Apartment>(in);
	}
}
