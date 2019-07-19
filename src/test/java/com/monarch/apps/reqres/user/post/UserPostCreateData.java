package com.monarch.apps.reqres.user.post;

import com.monarch.tools.FieldGenerators;
import com.monarch.tools.FieldGenerators.Mode;

public class UserPostCreateData {

	public static String name = FieldGenerators.generateRandomString(13, Mode.ALPHANUMERIC);
	public static String job = FieldGenerators.generateRandomString(3, Mode.NUMERIC);

	/**
	 * Generate new sets of data each time the class is called
	 * 
	 * @return {@link RequestCreateUserModel}
	 */
	public static RequestCreateUserModel generateCreateUserData() {
		RequestCreateUserModel result = new RequestCreateUserModel();
		result.name = FieldGenerators.generateRandomString(8, Mode.ALPHA);
		result.job = FieldGenerators.generateRandomString(12, Mode.ALPHANUMERIC);
		return result;
	}
}
