package com.alphax.vo.mb;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about AlphaX Admin Roles setting for permissions")
public class AdminRoleSettingsDTO {

	private String moduleId;
	private String moduleSectionId;
	private String sectionName;
	private String key;
	private String value;

	
	public AdminRoleSettingsDTO() {
		
	}


	/**
	 * @return the moduleId
	 */
	public String getModuleId() {
		return moduleId;
	}


	/**
	 * @param moduleId the moduleId to set
	 */
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}


	/**
	 * @return the moduleSectionId
	 */
	public String getModuleSectionId() {
		return moduleSectionId;
	}


	/**
	 * @param moduleSectionId the moduleSectionId to set
	 */
	public void setModuleSectionId(String moduleSectionId) {
		this.moduleSectionId = moduleSectionId;
	}


	/**
	 * @return the sectionName
	 */
	public String getSectionName() {
		return sectionName;
	}


	/**
	 * @param sectionName the sectionName to set
	 */
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}


	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}


	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}


	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}


	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}	
	
	
}