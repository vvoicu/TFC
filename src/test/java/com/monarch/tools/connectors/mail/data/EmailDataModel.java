package com.monarch.tools.connectors.mail.data;

import java.util.Date;
import java.util.List;

import javax.mail.Header;

public class EmailDataModel {

	public String subject;
	public Date receivedDate;
	public String messageContent;
	public List<Header> headers;

	public String getHeaders() {
		String result = "(";
		for (Header header : headers) {
			if(header.getName().contains("X-ORDER") || header.getName().contains("X-SITE")) 
			result += header.getName() + "=" + header.getValue() + ", ";
		}
		result += ")";
		return result;
	}

	public String toString() {
		return "==================================== \n Headers:" + getHeaders() + " \n Subject: " + subject
				+ "\n Received: " + receivedDate + "\n Content: " + messageContent + "====================================\n";
	}

}
