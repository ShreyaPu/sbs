package com.alphax.vo.mb;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "This Object related to Authentication details")
public class JwtRequest implements Serializable {

	private static final long serialVersionUID = 5926468583005150707L;
	
	@JsonProperty("username")
	private String username;
	
	@JsonProperty("password")
	private String password;
	
	//need default constructor for JSON Parsing
	public JwtRequest()
	{
		
	}

	public JwtRequest(String username, String password) {
		this.setUsername(username);
		this.setPassword(password);
	}

	@ApiModelProperty(value = "This is the Username of the user.")
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@ApiModelProperty(value = "This is the Password of the user.")
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}