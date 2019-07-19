package com.monarch.apps.reqres.user.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Based on the documentation: https://reqres.in/api
 * 
 * @author voicu.turcu
 *
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "name", "job"})
public class RequestCreateUserModel {

	@JsonProperty("name")
	public String name;

	@JsonProperty("job")
	public String job;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}
}
