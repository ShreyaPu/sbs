package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about AlphaX Admin setting for companys")
public class AdminCompanys {
	
	@DBTable(columnName ="COMPANY_ID", required = true)
	private BigDecimal companyId;
	
	@DBTable(columnName ="NAME", required = true)
	private String companyName;
	
	@DBTable(columnName ="CITY", required = true)
	private String city;
	
	@DBTable(columnName ="POST_CODE", required = true)
	private String postalCode;
	
	@DBTable(columnName ="STREETANDNUM", required = true)
	private String streetNumber;
	
	@DBTable(columnName ="COMPANY_NUMBER", required = true)
	private BigDecimal companyNumber;
	
	@DBTable(columnName ="AP_COMPANY_ID", required = true)
	private String alphaPlusCompanyId;
	
	@DBTable(columnName ="ISDELETED ", required = true)
	private BigDecimal deletedFlag;

	
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

	public BigDecimal getCompanyNumber() {
		return companyNumber;
	}

	public void setCompanyNumber(BigDecimal companyNumber) {
		this.companyNumber = companyNumber;
	}
	
	/**
	 * @return the alphaPlusAgencyId
	 */
	public String getAlphaPlusCompanyId() {
		return alphaPlusCompanyId;
	}

	/**
	 * @param alphaPlusAgencyId the alphaPlusAgencyId to set
	 */
	public void setAlphaPlusCompanyId(String alphaPlusCompanyId) {
		this.alphaPlusCompanyId = alphaPlusCompanyId;
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
