package org.stormhub.bostadskollen.test;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.stormhub.bostadskollen.mail.MailFormatter;
import org.stormhub.bostadskollen.mail.Pair;

import freemarker.template.TemplateException;

public class MailFormatterTest {

	@Test
	public void testMailTemplate() throws TemplateException, IOException {
		final String body = new MailFormatter(MailFormatter.getDefaultConfiguration().
				getTemplate("res/email.ftl")).
			setLink("https://helix.stormhub.org").
			setProperties(Arrays.asList(new Pair[] { 
				new Pair("Yta", "42 m<sup>2</sup>"), 
				new Pair("Balkong", "Ja") 
			})).
			setText("Lagenhet i centrum.").
			setTitle("Ny lagenhet!").
			setUnsubscribeLink("https://unsubscribe.mandrilla.com").
			getString();
		Assert.assertTrue(body.contains("<a href=\"https://helix.stormhub.org"));
		Assert.assertTrue(body.contains("<b>Yta:</b> 42 m"));
		Assert.assertTrue(body.contains("<b>Balkong:</b> Ja"));
		Assert.assertTrue(body.contains("Lagenhet i centrum."));
		Assert.assertTrue(body.contains("<a href=\"https://unsubscribe.mandrilla.com"));
		Assert.assertTrue(body.contains("<title>Ny lagenhet!</title>"));
	}
}
