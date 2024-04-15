package com.alphax.vo.mb;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about AlphaX configuration setup DTO")
public class AlphaXConfigurationSetupDTO {
	
	private String companyId;

	private String agencyId;
	
	private String agencyName;
	
	private List<AlphaXConfigurationKeysDetailsDTO> setupKeysList = null;
	
	@ApiModelProperty(value = "Firma ( %% - DB O_SETUP - COMPANY_ID).")
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	@ApiModelProperty(value = " Filliale ( %% - DB O_SETUP - AGENCY_ID).")
	public String getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}

	@ApiModelProperty(value = " Agency Name ( %% - DB REFERENZ - DATAFLD ).")
	public String getAgencyName() {
		return agencyName;
	}

	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}

	@ApiModelProperty(value = "AlphaX configuration keys detailList")
	public List<AlphaXConfigurationKeysDetailsDTO> getSetupKeysList() {
		return setupKeysList;
	}

	public void setSetupKeysList(List<AlphaXConfigurationKeysDetailsDTO> setupKeysList) {
		this.setupKeysList = setupKeysList;
	}
	
	

}
