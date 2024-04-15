package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about AlphaX Admin setting for agency")
public class AdminAgency {

	@DBTable(columnName ="AGENCY_ID", required = true)
	private BigDecimal agencyId;
	
	@DBTable(columnName ="AP_AGENCY_ID", required = true)
	private String alphaPlusAgencyId;
	
	@DBTable(columnName ="NAME", required = true)
	private String agencyName;
	
	@DBTable(columnName ="CITY", required = true)
	private String city;
	
	@DBTable(columnName ="POST_CODE", required = true)
	private String postalCode;
	
	@DBTable(columnName ="STREETANDNUM", required = true)
	private String streetNumber;
	
	@DBTable(columnName ="ISACTIVE", required = true)
	private BigDecimal active;
	
	@DBTable(columnName ="COMPANY_ID", required = true)
	private BigDecimal companyId;
	
	@DBTable(columnName ="ISDELETED ", required = true)
	private BigDecimal deletedFlag;
	
	@DBTable(columnName ="VALUE", required = true)
	private String functionFlag;
	
	
	
	

	public String getFunctionFlag() {
		return functionFlag;
	}

	public void setFunctionFlag(String functionFlag) {
		this.functionFlag = functionFlag;
	}

	public BigDecimal getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(BigDecimal deletedFlag) {
		this.deletedFlag = deletedFlag;
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
	 * @return the active
	 */
	public BigDecimal getActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(BigDecimal active) {
		this.active = active;
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
	 * @return the companyId
	 */
	public String getAgencyName() {
		return agencyName;
	}

	/**
	 * @param companyName the companyName to set
	 */
	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the postalCode
	 */
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * @param postalCode the postalCode to set
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	/**
	 * @return the streetNumber
	 */
	public String getStreetNumber() {
		return streetNumber;
	}

	/**
	 * @param streetNumber the streetNumber to set
	 */
	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}
	
	
}
