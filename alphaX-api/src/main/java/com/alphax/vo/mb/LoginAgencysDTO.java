package com.alphax.vo.mb;


import javax.validation.constraints.Size;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about AlphaX login agency for user")
public class LoginAgencysDTO {

	private String userRoleId;
	
	private String userId;
	
	private String roleId;
	
	private String agencyId;
	
	private String agencyName;
	
	private String alphaPlusAgencyId;
	
	private String companyId;
	
	private String alphaPlusCompanyId;
	
	private String companyName;
	
	private Boolean defaultAgency;
	
	private String roleName;
	
	private Boolean changed;
	
	

	/**
	 * @return the userRoleId
	 */
	@ApiModelProperty(value = "User Role Id ( %% - DB O_USERRL - USERROLE_ID).")
	@Size(max=10)
	public String getUserRoleId() {
		return userRoleId;
	}

	/**
	 * @param userRoleId the userRoleId to set
	 */
	public void setUserRoleId(String userRoleId) {
		this.userRoleId = userRoleId;
	}

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
	 * @return the roleId
	 */
	@ApiModelProperty(value = "Role Id ( %% - DB O_USERRL - ROLE_ID).")
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
	 * @return the agencyId
	 */
	@ApiModelProperty(value = "Agency Id ( %% - DB O_USERRL - AGENCY_ID).")
	@Size(max=3)
	public String getAgencyId() {
		return agencyId;
	}

	/**
	 * @param agencyId the agencyId to set
	 */
	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}

	/**
	 * @return the agencyName
	 */
	@ApiModelProperty(value = "Agency Name ( %% - DB O_AGENCY - NAME).")
	@Size(max=50)
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
	@ApiModelProperty(value = "Alpha Plus Agency Id ( %% - DB O_AGENCY - AP_AGENCY_ID).")
	@Size(max=3)
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
	@ApiModelProperty(value = "Company Id ( %% - DB O_AGENCY - COMPANY_ID).")
	@Size(max=3)
	public String getCompanyId() {
		return companyId;
	}

	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	/**
	 * @return the alphaPlusCompanyId
	 */
	@ApiModelProperty(value = "Alpha Plus Company Id ( %% - DB O_COMPANY - AP_COMPANY_ID).")
	@Size(max=3)
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
	@ApiModelProperty(value = "Company Name ( %% - DB O_COMPANY - NAME).")
	@Size(max=50)
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
	@ApiModelProperty(value = "Default Agency ( %% - DB O_USERRL - ISDEFAULT).")
	public boolean isDefaultAgency() {
		return defaultAgency;
	}

	/**
	 * @param defaultAgency the defaultAgency to set
	 */
	public void setDefaultAgency(boolean defaultAgency) {
		this.defaultAgency = defaultAgency;
	}

	/**
	 * @return the changed
	 */
	@ApiModelProperty(value = "This flag is use for default agency")
	public Boolean isChanged() {
		return changed;
	}

	/**
	 * @param changed the changed to set
	 */
	public void setChanged(Boolean changed) {
		this.changed = changed;
	}
	
	
	
	
	
}
