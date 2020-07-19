package com.monarch.tools;

import com.monarch.tools.utils.ConfigUtils;

public class FieldGenerators {

	public static enum Mode {
		ALPHA_CAPS, ALPHA, ALPHANUMERIC, NUMERIC, ALPHANUMERICSCHAR,
	}

	public static String generateRandomString(int length, Mode mode) {
		StringBuffer buffer = new StringBuffer();
		String characters = "";

		switch (mode) {
		case ALPHA_CAPS:
			characters = ConfigUtils.getDictionary("ALPHA_CAPS") != null  ? ConfigUtils.getDictionary("ALPHA_CAPS")
					: "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			break;
		case ALPHA:
			characters = ConfigUtils.getDictionary("ALPHA") != null  ? ConfigUtils.getDictionary("ALPHA")
					: "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
			break;
		case ALPHANUMERIC:
			characters = ConfigUtils.getDictionary("ALPHANUMERIC") != null ? ConfigUtils.getDictionary("ALPHANUMERIC")
					: "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
			break;
		case NUMERIC:
			characters = "1234567890";
			break;
		case ALPHANUMERICSCHAR:
			characters = "abc\'hurt{}!@£$%^&*()[]/?><;:";
			break;
		}

		int charactersLength = characters.length();
		for (int i = 0; i < length; i++) {
			double index = Math.random() * charactersLength;
			buffer.append(characters.charAt((int) index));
		}
		return buffer.toString();
	}

}
