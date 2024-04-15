package com.alphax.vo.mb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about AlphaX Employee Role list for Selected Agency")
public class EmployeeRoleDTO {
	
	private String firstName;
	
	private String lastName;
	
	private String roleName;

	/**
	 * @return the firstName
	 */
	@ApiModelProperty(value = "First Name ( %% - DB O_USER - FIRSTNAME).")
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	@ApiModelProperty(value = "Last Name ( %% - DB O_USER - LASTNAME).")
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the roleName
	 */
	@ApiModelProperty(value = "Role Name ( %% - DB O_ROLE - NAME).")
	public String getRoleName() {
		return roleName;
	}

	/**
	 * @param roleName the roleName to set
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	

}
