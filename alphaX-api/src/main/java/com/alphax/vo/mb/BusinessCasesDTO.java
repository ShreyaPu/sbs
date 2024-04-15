package com.alphax.vo.mb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about BAs key and value Details")
public class BusinessCasesDTO {

	private String  businessCaseKey;
	private String  businessCaseValue;
	
	
	/**
	 * @return the businessCaseKey
	 */
	@ApiModelProperty(value = "BA Number ( %% - DB O_BA - BA).")
	public String getBusinessCaseKey() {
		return businessCaseKey;
	}
	/**
	 * @param businessCaseKey the businessCaseKey to set
	 */
	public void setBusinessCaseKey(String businessCaseKey) {
		this.businessCaseKey = businessCaseKey;
	}
	
	/**
	 * @return the businessCaseValue
	 */
	@ApiModelProperty(value = "BA Name ( %% - DB O_BA - DESCRIPTION).")
	public String getBusinessCaseValue() {
		return businessCaseValue;
	}
	/**
	 * @param businessCaseValue the businessCaseValue to set
	 */
	public void setBusinessCaseValue(String businessCaseValue) {
		this.businessCaseValue = businessCaseValue;
	}
	
	


}
