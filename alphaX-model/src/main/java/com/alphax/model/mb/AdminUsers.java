package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about AlphaX Admin setting for Users")
public class AdminUsers {

	@DBTable(columnName ="USER_ID", required = true)
	private BigDecimal userId;
	
	@DBTable(columnName ="FIRSTNAME", required = true)
	private String userFirstName;
	
	@DBTable(columnName ="LASTNAME", required = true)
	private String userLastName;
	
	@DBTable(columnName ="LOGIN", required = true)
	private String userLoginName;
	
	@DBTable(columnName ="EMAIL", required = true)
	private String userEmail;
	
	@DBTable(columnName ="ISACTIVE", required = true)
	private BigDecimal active;
	
	@DBTable(columnName ="ROWNUMER", required = true)
	private Integer totalCount;
	
	@DBTable(columnName ="ISDELETED ", required = true)
	private BigDecimal deletedFlag;
	
	@DBTable(columnName ="CREATED_TS", required = true)
	private String createdTime;
	
	@DBTable(columnName ="CREATED_BY", required = true)
	private String createdBy;
	
	@DBTable(columnName ="USER_HAVE_ROLE", required = true)
	private String userHaveRole;
	
	@DBTable(columnName ="VALUE", required = true)
	private String centralFunctionFlag;
	
	

	
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
	 * @return the userLoginName
	 */
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

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
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

	/**
	 * @return the createdTime
	 */
	public String getCreatedTime() {
		return createdTime;
	}

	/**
	 * @param createdTime the createdTime to set
	 */
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the userHaveRole
	 */
	public String getUserHaveRole() {
		return userHaveRole;
	}

	/**
	 * @param userHaveRole the userHaveRole to set
	 */
	public void setUserHaveRole(String userHaveRole) {
		this.userHaveRole = userHaveRole;
	}

	/**
	 * @return the centralFunctionFlag
	 */
	public String getCentralFunctionFlag() {
		return centralFunctionFlag;
	}

	/**
	 * @param centralFunctionFlag the centralFunctionFlag to set
	 */
	public void setCentralFunctionFlag(String centralFunctionFlag) {
		this.centralFunctionFlag = centralFunctionFlag;
	}

	
	
	
		
}