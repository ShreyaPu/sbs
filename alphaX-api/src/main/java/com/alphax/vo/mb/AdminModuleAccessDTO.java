package com.alphax.vo.mb;

import java.util.Map;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about AlphaX Admin setting for Module Access in token")
public class AdminModuleAccessDTO {
	
	private String moduleName;
	private String isActive;
	private Map<String,String> moduleSection;
	/**
	 * @return the moduleName
	 */
	public String getModuleName() {
		return moduleName;
	}
	/**
	 * @param moduleName the moduleName to set
	 */
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	/**
	 * @return the isActive
	 */
	public String getIsActive() {
		return isActive;
	}
	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	/**
	 * @return the moduleSection
	 */
	public Map<String, String> getModuleSection() {
		return moduleSection;
	}
	/**
	 * @param moduleSection the moduleSection to set
	 */
	public void setModuleSection(Map<String, String> moduleSection) {
		this.moduleSection = moduleSection;
	}
	
	
	
	

}
