package com.alphax.vo.mb;

import io.swagger.annotations.ApiModel;

@ApiModel(description="All fields about OneApi Users")
public class AdminOneapiDTO {

	private String oneapiId;
	
	private String api;
	
	private String gemsUser;
	
	private String gemsPassword;
	
	private String customerId;
	
	private String url;
	
	private boolean active;
	
	private String updatedTime;
	
	private String updatedBy;
	
	private String auth;
	
	private String cobolUser;
	
	private String cobolPassword;
	
	private String description;
	
	private String rsaPrivateKey;
	
	
	/**
	 * @return the oneapiId
	 */
	public String getOneapiId() {
		return oneapiId;
	}

	/**
	 * @param oneapiId the oneapiId to set
	 */
	public void setOneapiId(String oneapiId) {
		this.oneapiId = oneapiId;
	}

	/**
	 * @return the api
	 */
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
	 * @return the gemsUser
	 */
	public String getGemsUser() {
		return gemsUser;
	}

	/**
	 * @param gemsUser the gemsUser to set
	 */
	public void setGemsUser(String gemsUser) {
		this.gemsUser = gemsUser;
	}

	/**
	 * @return the gemsPassword
	 */
	public String getGemsPassword() {
		return gemsPassword;
	}

	/**
	 * @param gemsPassword the gemsPassword to set
	 */
	public void setGemsPassword(String gemsPassword) {
		this.gemsPassword = gemsPassword;
	}

	/**
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	/**
	 * @return the url
	 */
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

	/**
	 * @return the updatedBy
	 */
	public String getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * @param updatedBy the updatedBy to set
	 */
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	/**
	 * @return the auth
	 */
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
	 * @return the cobolUser
	 */
	public String getCobolUser() {
		return cobolUser;
	}

	/**
	 * @param cobolUser the cobolUser to set
	 */
	public void setCobolUser(String cobolUser) {
		this.cobolUser = cobolUser;
	}

	/**
	 * @return the cobolPassword
	 */
	public String getCobolPassword() {
		return cobolPassword;
	}

	/**
	 * @param cobolPassword the cobolPassword to set
	 */
	public void setCobolPassword(String cobolPassword) {
		this.cobolPassword = cobolPassword;
	}

	/**
	 * @return the description
	 */
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
	 * @return the rsaPrivateKey
	 */
	public String getRsaPrivateKey() {
		return rsaPrivateKey;
	}

	/**
	 * @param rsaPrivateKey the rsaPrivateKey to set
	 */
	public void setRsaPrivateKey(String rsaPrivateKey) {
		this.rsaPrivateKey = rsaPrivateKey;
	}

	
	
	
}
