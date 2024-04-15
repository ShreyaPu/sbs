package com.alphax.vo.mb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="All fields about OneApi Users")
public class AdminApiurlDTO {
	
	private String api;
	
	private String url;
	
	private String description;
	
	private boolean active;
	
	private String auth;
	
	private String createdTime;
	
	private String updatedTime;

		
	/**
	 * @return the api
	 */
	@ApiModelProperty(value = "API Name ( %% - DB O_APIURL - API ).")
	public String getApi() {
		return api;
	}

	/**
	 * @param api the api to set
	 */
	public void setApi(String api) {
		this.api = api;
	}

	/**
	 * @return the url
	 */
	@ApiModelProperty(value = "URL Information ( %% - DB O_APIURL - URL ).")
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the description
	 */
	@ApiModelProperty(value = "Description of URL ( %% - DB O_APIURL - DESCRIPTION ).")
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @return the auth
	 */
	@ApiModelProperty(value = " Authorization ( %% - DB O_APIURL - AUTH ).")
	public String getAuth() {
		return auth;
	}

	/**
	 * @param auth the auth to set
	 */
	public void setAuth(String auth) {
		this.auth = auth;
	}

	/**
	 * @return the createdTime
	 */
	public String getCreatedTime() {
		return createdTime;
	}

	/**
	 * @param createdTime the createdTime to set
	 */
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	/**
	 * @return the updatedTime
	 */
	public String getUpdatedTime() {
		return updatedTime;
	}

	/**
	 * @param updatedTime the updatedTime to set
	 */
	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}
	
	

}
