package com.alphax.vo.mb;

import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about AlphaX Admin setting for module")
public class AdminModuleDTO {
	
	private String moduleId;
	
	private String moduleName;
	
	private String moduleDescription;
	
	private boolean active;

	/**
	 * @return the moduleId
	 */
	@ApiModelProperty(value = "Module Id ( %% - DB O_MODULE - MODULE_ID).")
	@Size(max=4)
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
	 * @return the moduleName
	 */
	@ApiModelProperty(value = "Module Name ( %% - DB O_MODULE - MODULE_NAME).")
	@Size(max=30)
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
	@ApiModelProperty(value = "Module Discription ( %% - DB O_MODULE - DESCRIPTION).")
	@Size(max=50)
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
	@ApiModelProperty(value = "Active ( %% - DB O_MODULE - ISACTIVE ).")
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
}
