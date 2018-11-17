package de.x3lq.Notifier;


import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;

public class Notifier {

	public static void notifyAllEmail(final EmailConfig config) {

		try {
			Email email = new SimpleEmail();
			email.setHostName(config.getHostName());
			email.setSmtpPort(config.getPortNumber());

			if(config.isAuth()) {
				email.setAuthenticator(new DefaultAuthenticator(config.getUserName(), config.getUserPassword()));
			}

			email.setSSLOnConnect(config.isSslEnabled());
			email.setFrom(config.getUserName());
			email.setSubject("Automated Notification Services");
			email.setMsg(config.getText());

			for(String recipient : config.getSendingTo()) {
				email.addTo(recipient);
			}

			email.send();

		}catch (Exception e) {
			System.out.println("Something went wrong sending the email");

			e.printStackTrace();
		}

	}
}
