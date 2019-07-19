package com.monarch.tools.connectors.mail.data;

public class OutlookConnectorSession {

	// user details
	private String username;
	private String password;

	// server details
	private String host;
	private String port;
	private String storeType;

	// mail configurations
	private String readFolder;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getStoreType() {
		return storeType;
	}

	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}

	public String getReadFolder() {
		return readFolder;
	}

	public void setReadFolder(String readFolder) {
		this.readFolder = readFolder;
	}

	public String toString() {
		return "--- --- OutlookConnectorSession --- ---\nServer: " + host + "\nPort: " + port + "\nStoreType: " + storeType + "\nReadFolder: "
				+ readFolder + "\nUsername: " + username + "\nPassword_isEmpty: " + password.isEmpty() + "\n--- --- ---";
	}
}
