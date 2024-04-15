package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description="All fields about APIURL")
public class AdminApiurl {
	
	@DBTable(columnName ="API", required = true)
	private String api;
	
	@DBTable(columnName ="URL", required = true)
	private String url;
	
	@DBTable(columnName ="DESCRIPTION", required = true)
	private String description;
	
	@DBTable(columnName ="ISACTIVE", required = true)
	private BigDecimal isactive;
	
	@DBTable(columnName ="AUTH", required = true)
	private String auth;
	
	@DBTable(columnName ="CREATED_TS", required = true)
	private String createdTime;
	
	@DBTable(columnName ="UPDATED_TS", required = true)
	private String updatedTime;

	
	
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
	 * @return the isactive
	 */
	public BigDecimal getIsactive() {
		return isactive;
	}

	/**
	 * @param isactive the isactive to set
	 */
	public void setIsactive(BigDecimal isactive) {
		this.isactive = isactive;
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
