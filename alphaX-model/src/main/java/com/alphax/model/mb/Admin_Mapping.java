package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about AlphaX Admin setting for Users , ROle and Agency Mapping")
public class Admin_Mapping {

	@DBTable(columnName ="USER_ID", required = true)
	private BigDecimal userId;
	
	@DBTable(columnName ="USERROLE_ID", required = true)
	private BigDecimal userRoleId;
	
	@DBTable(columnName ="WAREHOUS_ID", required = true)
	private BigDecimal warehousId;
	
	@DBTable(columnName ="AGENWRHS_ID", required = true)
	private BigDecimal agencyWarehousId;
	
	@DBTable(columnName ="ISACTIVE", required = true)
	private BigDecimal active;
	
	@DBTable(columnName ="FIRSTNAME", required = true)
	private String userFirstName;
	
	@DBTable(columnName ="LASTNAME", required = true)
	private String userLastName;
	
	@DBTable(columnName ="NAME", required = true)
	private String roleName;
	
	
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
	 * @return the userId
	 */
	public BigDecimal getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(BigDecimal userId) {
		this.userId = userId;
	}

	/**
	 * @return the userRoldId
	 */
	public BigDecimal getUserRoleId() {
		return userRoleId;
	}

	/**
	 * @param userRoldId the userRoldId to set
	 */
	public void setUserRoleId(BigDecimal userRoleId) {
		this.userRoleId = userRoleId;
	}

	public BigDecimal getWarehousId() {
		return warehousId;
	}

	public void setWarehousId(BigDecimal warehousId) {
		this.warehousId = warehousId;
	}

	public BigDecimal getAgencyWarehousId() {
		return agencyWarehousId;
	}

	public void setAgencyWarehousId(BigDecimal agencyWarehousId) {
		this.agencyWarehousId = agencyWarehousId;
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

	public String getUserFirstName() {
		return userFirstName;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	public String getUserLastName() {
		return userLastName;
	}

	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}
	
	
}
