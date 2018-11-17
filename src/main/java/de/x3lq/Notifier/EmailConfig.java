package de.x3lq.Notifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class EmailConfig {

	private boolean Auth;
	private boolean sslEnabled;
	private String hostName;
	private int portNumber;

	private String userName, userPassword;

	private String text;

	private List<String> sendingTo;

	public EmailConfig(boolean auth, boolean tlsEnabled, String hostName, int portNumber, String userName, String userPassword, String text, String[] sendingTo) {
		Auth = auth;
		this.sslEnabled = tlsEnabled;
		this.hostName = hostName;
		this.portNumber = portNumber;
		this.userName = userName;
		this.userPassword = userPassword;
		this.sendingTo = new ArrayList<String>(Arrays.asList(sendingTo));
		this.text = text;
	}

	public boolean isAuth() {
		return Auth;
	}

	public boolean isSslEnabled() {
		return sslEnabled;
	}

	public String getHostName() {
		return hostName;
	}

	public int getPortNumber() {
		return portNumber;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public List<String> getSendingTo() {
		return sendingTo;
	}

	public String getText() {
		return text;
	}
}
