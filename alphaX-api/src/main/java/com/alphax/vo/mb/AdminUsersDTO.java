package com.alphax.vo.mb;

import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about AlphaX Admin setting for users")
public class AdminUsersDTO {

	private String userId;
	
	private String userFirstName;
	
	private String userLastName;
	
	private String userLoginName;
	
	private String userEmail;
	
	private Boolean active;
	
	private String defaultAgency;
	
	private String defaultCompany;
	
	private String defaultWarehouse;
	
	private boolean  roleAssigned;
	
	private boolean  centralFunctionValue;
	
	
	/**
	 * @return the userId
	 */
	@ApiModelProperty(value = "User Id ( %% - DB O_USER - USER_ID).")
	@Size(max=8)
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
	 * @return the userName
	 */
	@ApiModelProperty(value = "User First Name ( %% - DB O_USER - FIRSTNAME).")
	@Size(max=50)
	public String getUserFirstName() {
		return userFirstName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}
	
	/**
	 * @return the userName
	 */
	@ApiModelProperty(value = "User Last Name ( %% - DB O_USER - LASTNAME).")
	@Size(max=50)
	public String getUserLastName() {
		return userLastName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}
	

	/**
	 * @return the userLoginName
	 */
	@ApiModelProperty(value = "User LoginName ( %% - DB O_USER - LOGIN ).")
	@Size(max=10)
	public String getUserLoginName() {
		return userLoginName;
	}

	/**
	 * @param userLoginName the userLoginName to set
	 */
	public void setUserLoginName(String userLoginName) {
		this.userLoginName = userLoginName;
	}

	/**
	 * @return the userEmail
	 */
	@ApiModelProperty(value = "User EmailId ( %% - DB O_USER - EMAIL ).")
	@Size(max=50)
	public String getUserEmail() {
		return userEmail;
	}

	/**
	 * @param userEmail the userEmail to set
	 */
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	/**
	 * @return the isActive
	 */
	@ApiModelProperty(value = "Active ( %% - DB O_USER - ISACTIVE maxLength 1 ).")
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

	/**
	 * @return the roleAssigned
	 */
	public boolean isRoleAssigned() {
		return roleAssigned;
	}

	/**
	 * @param roleAssigned the roleAssigned to set
	 */
	public void setRoleAssigned(boolean roleAssigned) {
		this.roleAssigned = roleAssigned;
	}

	/**
	 * @return the centralFunctionValue
	 */
	@ApiModelProperty(value = "Central Function ( %% - DB O_SETUP - VALUE ).")
	public boolean isCentralFunctionValue() {
		return centralFunctionValue;
	}

	/**
	 * @param centralFunctionValue the centralFunctionValue to set
	 */
	public void setCentralFunctionValue(boolean centralFunctionValue) {
		this.centralFunctionValue = centralFunctionValue;
	}

		
	
}