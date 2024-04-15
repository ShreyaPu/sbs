package com.alphax.vo.mb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "This Object about extended user company and agency information")
public class UserCompanyInfoDTO {

	private String companyId;
	private String agencyId;
	
	
	
	/**
	 * @return the companyId
	 */
	@ApiModelProperty(value = "Firma (Company) - DB UBF_NUTZER - BNU_FIRMA")
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
	@ApiModelProperty(value = "Filia (Agency) - DB UBF_NUTZER - BNU_FILIA")
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
