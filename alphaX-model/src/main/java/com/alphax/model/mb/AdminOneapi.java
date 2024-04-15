package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description="All fields about One API Users")
public class AdminOneapi {
	
	@DBTable(columnName ="ID", required = true)
	private Integer oneapiId;
	
	@DBTable(columnName ="API", required = true)
	private String api;
	
	@DBTable(columnName ="GEMS_USER", required = true)
	private String gemsUser;
	
	@DBTable(columnName ="GEMS_PASS", required = true)
	private String gemsPassword;
	
	@DBTable(columnName ="CUSTOMER", required = true)
	private BigDecimal customerId;
	
	@DBTable(columnName ="URL", required = true)
	private String url;
	
	@DBTable(columnName ="ISACTIVE", required = true)
	private BigDecimal isactive;
	
	@DBTable(columnName ="UPDATED_TS", required = true)
	private String updatedTime;
	
	@DBTable(columnName ="UPDATED_BY", required = true)
	private String updatedBy;
	
	@DBTable(columnName ="AUTH", required = true)
	private String auth;
	
	@DBTable(columnName ="COBOL_USER", required = true)
	private String cobolUser;
	
	@DBTable(columnName ="COBOL_PASS", required = true)
	private String cobolPassword;
	
	@DBTable(columnName ="VF_Nummer", required = true)
	private String vfNumber;
	
	@DBTable(columnName ="DESCRIPTION", required = true)
	private String description;
	
	@DBTable(columnName ="ROWNUMER", required = true)
	private Integer totalCount;
	
	@DBTable(columnName ="RSAPVTKEY", required = true)
	private String rsaPrivateKey;
	

	/**
	 * @return the oneapiId
	 */
	public Integer getOneapiId() {
		return oneapiId;
	}

	/**
	 * @param oneapiId the oneapiId to set
	 */
	public void setOneapiId(Integer oneapiId) {
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
	public BigDecimal getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(BigDecimal customerId) {
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
	 * @return the vfNumber
	 */
	public String getVfNumber() {
		return vfNumber;
	}

	/**
	 * @param vfNumber the vfNumber to set
	 */
	public void setVfNumber(String vfNumber) {
		this.vfNumber = vfNumber;
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
	 * @return the totalCount
	 */
	public Integer getTotalCount() {
		return totalCount;
	}

	/**
	 * @param totalCount the totalCount to set
	 */
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
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
