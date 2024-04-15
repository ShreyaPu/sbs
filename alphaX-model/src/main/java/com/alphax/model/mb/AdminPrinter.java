package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description="All fields about admin Printer")
public class AdminPrinter {
	
	@DBTable(columnName ="ID", required = true)
	private String printerId;
	
	@DBTable(columnName ="NAME", required = true)
	private String printerName;
	
	@DBTable(columnName ="AP_ID", required = true)
	private String alphaPlusPrinterId;
	
	@DBTable(columnName ="DESCRIPTION", required = true)
	private String printerDescription;
	
	@DBTable(columnName ="CREATED_BY", required = true)
	private String createdBy;
	
	@DBTable(columnName ="CREATED_TS", required = true)
	private String createdTime ;
	
	@DBTable(columnName ="UPDATED_BY", required = true)
	private String updatedBy;	
		
	@DBTable(columnName ="UPDATED_TS", required = true)
	private String updatedTime;
	
	@DBTable(columnName ="ISACTIVE", required = true)
	private BigDecimal active;


	public String getPrinterId() {
		return printerId;
	}


	public void setPrinterId(String printerId) {
		this.printerId = printerId;
	}


	public String getPrinterName() {
		return printerName;
	}


	public void setPrinterName(String printerName) {
		this.printerName = printerName;
	}


	public String getAlphaPlusPrinterId() {
		return alphaPlusPrinterId;
	}


	public void setAlphaPlusPrinterId(String alphaPlusPrinterId) {
		this.alphaPlusPrinterId = alphaPlusPrinterId;
	}


	public String getPrinterDescription() {
		return printerDescription;
	}


	public void setPrinterDescription(String printerDescription) {
		this.printerDescription = printerDescription;
	}


	public String getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


	public String getCreatedTime() {
		return createdTime;
	}


	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}


	public String getUpdatedBy() {
		return updatedBy;
	}


	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}


	public String getUpdatedTime() {
		return updatedTime;
	}


	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
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
