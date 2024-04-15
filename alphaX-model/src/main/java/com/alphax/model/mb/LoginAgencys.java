package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about AlphaX login agency for user")
public class LoginAgencys {
	
	@DBTable(columnName ="USERROLE_ID", required = true)
	private BigDecimal userRoleId;
	
	@DBTable(columnName ="USER_ID", required = true)
	private BigDecimal userId;
	
	@DBTable(columnName ="ROLE_ID", required = true)
	private BigDecimal roleId;
	
	@DBTable(columnName ="AGENCY_ID", required = true)
	private BigDecimal agencyId;
	
	@DBTable(columnName ="agencyName", required = true)
	private String agencyName;
	
	@DBTable(columnName ="AP_AGENCY_ID", required = true)
	private String alphaPlusAgencyId;
	
	@DBTable(columnName ="COMPANY_ID", required = true)
	private BigDecimal companyId;
	
	@DBTable(columnName ="AP_COMPANY_ID", required = true)
	private String alphaPlusCompanyId;
	
	@DBTable(columnName ="companyName", required = true)
	private String companyName;
	
	@DBTable(columnName ="ISDEFAULT", required = true)
	private BigDecimal defaultAgency;
	
	@DBTable(columnName ="ROLENAME", required = true)
	private String roleName;
	

	/**
	 * @return the userRoleId
	 */
	public BigDecimal getUserRoleId() {
		return userRoleId;
	}

	/**
	 * @param userRoleId the userRoleId to set
	 */
	public void setUserRoleId(BigDecimal userRoleId) {
		this.userRoleId = userRoleId;
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
	 * @return the agencyId
	 */
	public BigDecimal getAgencyId() {
		return agencyId;
	}

	/**
	 * @param agencyId the agencyId to set
	 */
	public void setAgencyId(BigDecimal agencyId) {
		this.agencyId = agencyId;
	}

	/**
	 * @return the agencyName
	 */
	public String getAgencyName() {
		return agencyName;
	}

	/**
	 * @param agencyName the agencyName to set
	 */
	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}

	/**
	 * @return the alphaPlusAgencyId
	 */
	public String getAlphaPlusAgencyId() {
		return alphaPlusAgencyId;
	}

	/**
	 * @param alphaPlusAgencyId the alphaPlusAgencyId to set
	 */
	public void setAlphaPlusAgencyId(String alphaPlusAgencyId) {
		this.alphaPlusAgencyId = alphaPlusAgencyId;
	}

	/**
	 * @return the companyId
	 */
	public BigDecimal getCompanyId() {
		return companyId;
	}

	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(BigDecimal companyId) {
		this.companyId = companyId;
	}

	/**
	 * @return the alphaPlusCompanyId
	 */
	public String getAlphaPlusCompanyId() {
		return alphaPlusCompanyId;
	}

	/**
	 * @param alphaPlusCompanyId the alphaPlusCompanyId to set
	 */
	public void setAlphaPlusCompanyId(String alphaPlusCompanyId) {
		this.alphaPlusCompanyId = alphaPlusCompanyId;
	}

	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * @return the defaultAgency
	 */
	public BigDecimal getDefaultAgency() {
		return defaultAgency;
	}

	/**
	 * @param defaultAgency the defaultAgency to set
	 */
	public void setDefaultAgency(BigDecimal defaultAgency) {
		this.defaultAgency = defaultAgency;
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

	
	
	
	

}
