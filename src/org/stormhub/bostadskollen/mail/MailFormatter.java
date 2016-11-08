package org.stormhub.bostadskollen.mail;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class MailFormatter {
	private String text;
	private String unsubscribe;
	private String link;
	private List<Pair> properties;
	private String title;
	private final Template template;
	
	public static Configuration getDefaultConfiguration() {
		final Configuration configuration = new Configuration(Configuration.VERSION_2_3_25);
		configuration.setClassLoaderForTemplateLoading(MailFormatter.class.getClassLoader(), "/");
		configuration.setDefaultEncoding("UTF-8");
	    configuration.setLocale(Locale.US);
	    return configuration;
	}
	
	public MailFormatter(Template template) {
		this.template = template;
	}
	
	public MailFormatter setTitle(final String title) {
		this.title = title;
		return this;
	}
	
	public MailFormatter setText(final String text) {
		this.text = text;
		return this;
	}
	
	public MailFormatter setUnsubscribeLink(final String unsubscribe) {
		this.unsubscribe = unsubscribe;
		return this;
	}
	
	public MailFormatter setLink(final String link) {
		this.link = link;
		return this;
	}
	
	public MailFormatter setProperties(final List<Pair> properties) {
		final List<Pair> nonEmptyProperties = properties.stream().
				filter(pair -> pair.getValue() != null && !"".equals(pair.getValue())).
				collect(Collectors.toList());
		this.properties = nonEmptyProperties;
		return this;
	}
	
	public String getString() throws IOException, TemplateException {
		final StringWriter out = new StringWriter();
		final Map<String, Object> input = new HashMap<String, Object>();
		input.put("link", link);
		input.put("unsubscribe", unsubscribe);
		input.put("properties", properties);
		input.put("text", text);
		input.put("title", title);
		template.process(input, out);
		return out.toString();
	}
}
