package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about AlphaX Admin setting for Roles")
public class AdminRoles {

	@DBTable(columnName ="ROLE_ID", required = true)
	private BigDecimal roleId;
	
	@DBTable(columnName ="NAME", required = true)
	private String roleName;
	
	@DBTable(columnName ="DESCRIPTION", required = true)
	private String roleDescription;
	
	@DBTable(columnName ="ISACTIVE", required = true)
	private BigDecimal active;

	@DBTable(columnName ="ISDELETED ", required = true)
	private BigDecimal deletedFlag;
	
	/**
	 * @return the roleId
	 */
	public BigDecimal getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(BigDecimal roleId) {
		this.roleId = roleId;
	}

	/**
	 * @return the roleName
	 */
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
	public BigDecimal isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(BigDecimal active) {
		this.active = active;
	}

	/**
	 * @return the deletedFlag
	 */
	public BigDecimal getDeletedFlag() {
		return deletedFlag;
	}

	/**
	 * @param deletedFlag the deletedFlag to set
	 */
	public void setDeletedFlag(BigDecimal deletedFlag) {
		this.deletedFlag = deletedFlag;
	}
	
	
	
}
