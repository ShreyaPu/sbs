package com.alphax.vo.mb;

import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about AlphaX Admin default setting for users")
public class AdminUserSettingDTO {
	
	private String userId;
	
	private String defaultAgency;
	
	private String defaultCompany;
	
	private String defaultWarehouse;
	
	
	
	/**
	 * @return the userId
	 */
	@ApiModelProperty(value = "User Id ( %% - DB O_USER - USER_ID).")
	@Size(max=5)
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	
	/**
	 * @return the defaultAgency
	 */
	@ApiModelProperty(value = "Default Agency")
	public String getDefaultAgency() {
		return defaultAgency;
	}

	/**
	 * @param defaultAgency the defaultAgency to set
	 */
	public void setDefaultAgency(String defaultAgency) {
		this.defaultAgency = defaultAgency;
	}

	/**
	 * @return the defaultCompany
	 */
	@ApiModelProperty(value = "Default Company")
	public String getDefaultCompany() {
		return defaultCompany;
	}

	/**
	 * @param defaultCompany the defaultCompany to set
	 */
	public void setDefaultCompany(String defaultCompany) {
		this.defaultCompany = defaultCompany;
	}

	/**
	 * @return the defaultWarehouse
	 */
	@ApiModelProperty(value = "Default Warehouse")
	public String getDefaultWarehouse() {
		return defaultWarehouse;
	}

	/**
	 * @param defaultWarehouse the defaultWarehouse to set
	 */
	public void setDefaultWarehouse(String defaultWarehouse) {
		this.defaultWarehouse = defaultWarehouse;
	}
	
}
