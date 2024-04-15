package com.alphax.vo.mb;


import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about AlphaX Admin setting for companys")
public class AdminCompanysDTO {

	private String companyId;
	
	private String companyName;
	
	private String city;
	
	private String postalCode;
	
	private String streetNumber;

	private String alphaPlusCompanyId;
	

	/**
	 * @return the companyId
	 */
	@ApiModelProperty(value = "Company Id ( %% - DB O_COMPANY - COMPANY_ID).")
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
	@ApiModelProperty(value = "Company Name ( %% - DB O_COMPANY - Name).")
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
	 * @return the city
	 */
	@ApiModelProperty(value = "Company City ( %% - DB O_COMPANY - CITY).")
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
	@ApiModelProperty(value = "Company postal code ( %% - DB O_COMPANY - POST_CODE  ).")
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
	@ApiModelProperty(value = "Company Street number ( %% - DB O_COMPANY - STREETANDNUM ).")
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

	/**
	 * @return the alphaPlusAgencyId
	 */
	@ApiModelProperty(value = "AlphaPlus Agency Id ( %% - DB O_COMPANY - AP_COMPANY_ID).")
	@Size(max=2)
	public String getAlphaPlusCompanyId() {
		return alphaPlusCompanyId;
	}

	/**
	 * @param alphaPlusAgencyId the alphaPlusAgencyId to set
	 */
	public void setAlphaPlusCompanyId(String alphaPlusCompanyId) {
		this.alphaPlusCompanyId = alphaPlusCompanyId;
	}
	
	
	
}
