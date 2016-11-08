package org.stormhub.bostadskollen.driver;

import java.util.List;

public class Arguments {
	private final String dbPath;
	private final String mailTemplatePath;
	private final boolean dryRun;
	
	public static Arguments parse(final List<String> args) {
		String dbPath = "src/res/db";
		String mailTemplatePath = "res/email.ftl";
		boolean dryRun = false;
		for (int i = 0; i < args.size(); i++) {
			final String arg = args.get(i++);
			if (arg.equals("--db")) {
				if (i >= args.size())
					throw new IllegalArgumentException("No path to database follows flag --db.");
				dbPath = args.get(i);
			} else if (arg.equals("--ftl")) {
				if (i >= args.size())
					throw new IllegalArgumentException("No path to FreeMarker template follows flag --ftl.");
				mailTemplatePath = args.get(i);
			} else if (arg.equals("--dry-run")) {
				dryRun = true;
			} else {
				throw new IllegalArgumentException(String.format("Unknown flag %s.", arg));
			}
		}
		
		return new Arguments(dbPath, mailTemplatePath, dryRun);
	}
	
	private Arguments(final String dbPath, final String mailTemplatePath, final boolean dryRun) {
		this.dbPath = dbPath;
		this.mailTemplatePath = mailTemplatePath;
		this.dryRun = dryRun;
	}
	
	public String getDatabasePath() {
		return dbPath;
	}
	
	public String getMailTemplatePath() {
		return mailTemplatePath;
	}
	
	public boolean isDryRun() {
		return dryRun;
	}
}
