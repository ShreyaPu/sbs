package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about AlphaX Admin setting for module")
public class AdminModule {
	
	@DBTable(columnName ="MODULE_ID", required = true)
	private BigDecimal moduleId;
	
	@DBTable(columnName ="MODULE_NAME", required = true)
	private String moduleName;
	
	@DBTable(columnName ="DESCRIPTION", required = true)
	private String moduleDescription;
	
	@DBTable(columnName ="ISACTIVE", required = true)
	private BigDecimal active;
	
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
	 * @return the moduleDescription
	 */
	public String getModuleDescription() {
		return moduleDescription;
	}

	/**
	 * @param moduleDescription the moduleDescription to set
	 */
	public void setModuleDescription(String moduleDescription) {
		this.moduleDescription = moduleDescription;
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

}
