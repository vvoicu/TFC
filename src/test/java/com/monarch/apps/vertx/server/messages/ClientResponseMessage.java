package com.monarch.apps.vertx.server.messages;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientResponseMessage {

	private String receivedPayload;
	private String nanoId;

	public String getReceivedPayload() {
		return receivedPayload;
	}

	public void setReceivedPayload(String receivedPayload) {
		this.receivedPayload = receivedPayload;
	}

	public String toJson() {

		String result = "{CANNOT CONVERT PAYLOAD TO JSON}";

		ObjectMapper objectMapper = new ObjectMapper();

		try {

			result = objectMapper.writeValueAsString(this);
		} catch (Exception e) {
			String errorMessage = e.getMessage();

			System.err.println("Failed to convert the object to a JSon: \n" + errorMessage);
		}
		return result;
	}

	public void setNanoId(String nanoTime) {
		this.nanoId = nanoTime;
	}

	public String getNanoId() {
		return nanoId;
	}

}
