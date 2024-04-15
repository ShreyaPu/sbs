package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about AlphaX Admin Agency, Warehouse Mapping")
public class AdminAgencyWRHSMapping {

	@DBTable(columnName ="AGENWRHS_ID", required = true)
	private BigDecimal agencyWarehousId;
	
	@DBTable(columnName ="WAREHOUS_ID", required = true)
	private BigDecimal warehousId;
	
	@DBTable(columnName ="AGENCY_ID", required = true)
	private BigDecimal agencyId;
	
	@DBTable(columnName ="ISACTIVE", required = true)
	private BigDecimal active;
	
	@DBTable(columnName ="CREATED_BY", required = true)
	private String createdBy;
	
	@DBTable(columnName ="UPDATED_BY", required = true)
	private String modifiedBy;
	
	
	public AdminAgencyWRHSMapping() {
	}


	/**
	 * @return the agencyWarehousId
	 */
	public BigDecimal getAgencyWarehousId() {
		return agencyWarehousId;
	}


	/**
	 * @param agencyWarehousId the agencyWarehousId to set
	 */
	public void setAgencyWarehousId(BigDecimal agencyWarehousId) {
		this.agencyWarehousId = agencyWarehousId;
	}


	/**
	 * @return the warehousId
	 */
	public BigDecimal getWarehousId() {
		return warehousId;
	}


	/**
	 * @param warehousId the warehousId to set
	 */
	public void setWarehousId(BigDecimal warehousId) {
		this.warehousId = warehousId;
	}


	/**
	 * @return the agencyId
	 */
	public BigDecimal getAgencyId() {
		return agencyId;
	}


	/**
	 * @param agencyId the agencyId to set
	 */
	public void setAgencyId(BigDecimal agencyId) {
		this.agencyId = agencyId;
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
	
}