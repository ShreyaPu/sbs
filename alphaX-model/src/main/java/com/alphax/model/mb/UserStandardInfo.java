package com.alphax.model.mb;

import com.alphax.common.util.DBTable;

public class UserStandardInfo {
	
	@DBTable(columnName ="NRO_ROLLE", required = true)
	String userRole;
	
	@DBTable(columnName ="BNU_VNAME", required = true)
	String userFirstName;
	
	@DBTable(columnName ="BNU_NNAME", required = true)
	String userLastName;
	
	@DBTable(columnName ="NTE_TEAM", required = true)
	String userTeam;
	
	@DBTable(columnName ="BNU_FIRMA", required = true)
	String companyId;
	
	@DBTable(columnName ="BNU_FILIA", required = true)
	String agencyId;

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
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

	public String getUserTeam() {
		return userTeam;
	}

	public void setUserTeam(String userTeam) {
		this.userTeam = userTeam;
	}

	/**
	 * @return the companyId
	 */
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
	 * @return the agencyId
	 */
	public String getAgencyId() {
		return agencyId;
	}

	/**
	 * @param agencyId the agencyId to set
	 */
	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}
	
	

	
}
