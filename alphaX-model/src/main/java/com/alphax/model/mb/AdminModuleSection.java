package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about AlphaX Admin setting for module Section")
public class AdminModuleSection {
	
	@DBTable(columnName ="MODSEC_ID", required = true)
	private BigDecimal moduleSectionId;
	
	@DBTable(columnName ="SECTION_NAME", required = true)
	private String sectionName;
	
	@DBTable(columnName ="MODULE_ID", required = true)
	private BigDecimal moduleId;
	
	@DBTable(columnName ="ISACTIVE", required = true)
	private BigDecimal active;
	
	@DBTable(columnName ="KEY", required = true)
	private String key;
	
	@DBTable(columnName ="VALUE", required = true)
	private String value;
	
	@DBTable(columnName ="CREATED_BY", required = true)
	private String createdBy;
	
	@DBTable(columnName ="MODIFIED_BY", required = true)
	private String modifiedBy;
	
	@DBTable(columnName ="MODULE_NAME", required = true)
	private String moduleName;
	
	
	public AdminModuleSection() {
		
	}
	

	/**
	 * @return the moduleSectionId
	 */
	public BigDecimal getModuleSectionId() {
		return moduleSectionId;
	}

	/**
	 * @param moduleSectionId the moduleSectionId to set
	 */
	public void setModuleSectionId(BigDecimal moduleSectionId) {
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
	 * @return the moduleId
	 */
	public BigDecimal getModuleId() {
		return moduleId;
	}

	/**
	 * @param moduleId the moduleId to set
	 */
	public void setModuleId(BigDecimal moduleId) {
		this.moduleId = moduleId;
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

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the modifiedBy
	 */
	public String getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}


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
	
	
	
}