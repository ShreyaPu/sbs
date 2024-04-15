package com.alphax.vo.mb;

import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about AlphaX Admin setting for agency")
public class AdminAgencyDTO {

	private String agencyId;
	
	private String alphaPlusAgencyId;
	
	private String agencyName;
	
	private String city;
	
	private String postalCode;
	
	private String streetNumber;
	
	private boolean active;
	
	private String companyId;	
	
	private boolean  functionValue;
	
	

	public boolean isFunctionValue() {
		return functionValue;
	}

	public void setFunctionValue(boolean functionValue) {
		this.functionValue = functionValue;
	}

	/**
	 * @return the agencyId
	 */
	@ApiModelProperty(value = "Agency Id ( %% - DB O_AGENCY - AGENCY_ID).")
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
	 * @return the alphaPlusAgencyId
	 */
	@ApiModelProperty(value = "AlphaPlus Agency Id ( %% - DB O_AGENCY - AP_AGENCY_ID).")
	@Size(max=2)
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
	@ApiModelProperty(value = "Active ( %% - DB O_AGENCY - ISACTIVE).")
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
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
	 * @return the companyId
	 */
	@ApiModelProperty(value = "Agency Name ( %% - DB O_AGENCY - NAME).")
	@Size(max=50)
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
	@ApiModelProperty(value = "Agency City ( %% - DB O_AGENCY - CITY).")
	@Size(max=50)
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
	@ApiModelProperty(value = "Postal Code ( %% - DB O_AGENCY - POST_COD).")
	@Size(max=6)
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
	@ApiModelProperty(value = "Street Number ( %% - DB O_AGENCY - STREETANDNUM ).")
	@Size(max=30)
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
