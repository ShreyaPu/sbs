package com.alphax.vo.mb;

import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about AlphaX Admin setting for User , Role and Agency Mapping")
public class Admin_UserRoleMappingDTO {
	
	private String userId;
	
	private String userRoleId;
	
	private boolean active;
	
	private String userFirstName;
	
	private String userLastName;
	
	
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

	

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@ApiModelProperty(value = "UserRoleId Id ( %% - DB O_USERRL - USERROLE_ID).")
	@Size(max=10)
	public String getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(String userRoleId) {
		this.userRoleId = userRoleId;
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
	
	

}
