package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about AlphaX Admin Agency Printer Mapping")
public class AdminAGNPRTMapping {

	@DBTable(columnName ="AGNPRT_ID", required = true)
	private Integer agencyPrinterId;
	
	@DBTable(columnName ="AGENCY_ID", required = true)
	private BigDecimal agencyId;
	
	@DBTable(columnName ="PRT_ID", required = true)
	private BigDecimal printerId;
	
	@DBTable(columnName ="ISACTIVE", required = true)
	private BigDecimal active;
	
	@DBTable(columnName ="CREATED_BY", required = true)
	private String createdBy;
	
	@DBTable(columnName ="UPDATED_BY", required = true)
	private String modifiedBy;
	
	
	public AdminAGNPRTMapping() {
	}


	/**
	 * @return the agencyPrinterId
	 */
	public Integer getAgencyPrinterId() {
		return agencyPrinterId;
	}


	/**
	 * @param agencyPrinterId the agencyPrinterId to set
	 */
	public void setAgencyPrinterId(Integer agencyPrinterId) {
		this.agencyPrinterId = agencyPrinterId;
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
	 * @return the printerId
	 */
	public BigDecimal getPrinterId() {
		return printerId;
	}


	/**
	 * @param printerId the printerId to set
	 */
	public void setPrinterId(BigDecimal printerId) {
		this.printerId = printerId;
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