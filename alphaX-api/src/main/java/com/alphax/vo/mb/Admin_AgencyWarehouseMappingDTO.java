package com.alphax.vo.mb;

import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about AlphaX Admin setting for Agency and Warehouse Mapping")
public class Admin_AgencyWarehouseMappingDTO {

	private String warehousId;;
	
	private String  agencyWarehousId;
	
	private boolean active;

	
	
	/**
	 * @return the warehousId
	 */
	@ApiModelProperty(value = "Warehous Id ( %% - DB O_AGNWRH - WAREHOUS_ID).")
	@Size(max=3)
	public String getWarehousId() {
		return warehousId;
	}

	/**
	 * @param warehousId the warehousId to set
	 */
	public void setWarehousId(String warehousId) {
		this.warehousId = warehousId;
	}

	/**
	 * @return the agencyWarehousId
	 */
	@ApiModelProperty(value = "Agency Warehous Id ( %% - DB O_AGNWRH - AGENWRHS_ID).")
	@Size(max=6)
	public String getAgencyWarehousId() {
		return agencyWarehousId;
	}

	/**
	 * @param agencyWarehousId the agencyWarehousId to set
	 */
	public void setAgencyWarehousId(String agencyWarehousId) {
		this.agencyWarehousId = agencyWarehousId;
	}

	public boolean isActive() {
		return active;
	}

	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	
	
}
