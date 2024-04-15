package com.alphax.vo.mb;

import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about AlphaX Admin setting for roles")
public class AdminRolesDTO {
	
	private String roleId;
	
	private String roleName;
	
	private String roleDescription;
	
	private Boolean active;
	
	
	/**
	 * @return the roleId
	 */
	@ApiModelProperty(value = "Role Id ( %% - DB O_ROLE - ROLE_ID).")
	@Size(max=6)
	public String getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	/**
	 * @return the roleName
	 */
	@ApiModelProperty(value = "Role Name ( %% - DB O_ROLE - NAME).")
	@Size(max=30)
	public String getRoleName() {
		return roleName;
	}

	/**
	 * @param roleName the roleName to set
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * @return the roleDescription
	 */
	@ApiModelProperty(value = "Role Description ( %% - DB O_ROLE - DESCRIPTION).")
	@Size(max=250)
	public String getRoleDescription() {
		return roleDescription;
	}

	/**
	 * @param roleDescription the roleDescription to set
	 */
	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}

	/**
	 * @return the active
	 */
	@ApiModelProperty(value = "Active ( %% - DB O_ROLE - ISACTIVE ).")
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
	
	

}
