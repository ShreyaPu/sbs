package com.alphax.vo.mb;


import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about AlphaX Admin setting for Printer details")
public class AdminPrinterDTO {

	private String printerId;
	
	private String printerName;
	
	private String alphaPlusPrinterId;
	
	private String printerDescription;
	
	private String createdBy;
	
	private String createdTime;
	
	private String updatedBy;	
		
	private String updatedTime;
	
	private boolean active;
	
	private List<AdminPrinterFKTDTO> printerFKTList;
	

	/**
	 * @return the printerId
	 */
	@ApiModelProperty(value = "Printer Id ( %% - DB O_PRINTER - ID).")
	public String getPrinterId() {
		return printerId;
	}

	/**
	 * @param printerId the printerId to set
	 */
	public void setPrinterId(String printerId) {
		this.printerId = printerId;
	}
	

	/**
	 * @return the printerName
	 */
	@ApiModelProperty(value = "Printer Name ( %% - DB O_PRINTER - Name).")
	public String getPrinterName() {
		return printerName;
	}

	/**
	 * @param printerName the printerName to set
	 */
	public void setPrinterName(String printerName) {
		this.printerName = printerName;
	}
	

	/**
	 * @return the alphaPlusPrinterId
	 */
	@ApiModelProperty(value = "AlphaPlus Printer Id ( %% - DB O_PRINTER - AP_ID).")
	public String getAlphaPlusPrinterId() {
		return alphaPlusPrinterId;
	}

	/**
	 * @param alphaPlusPrinterId the alphaPlusPrinterId to set
	 */
	public void setAlphaPlusPrinterId(String alphaPlusPrinterId) {
		this.alphaPlusPrinterId = alphaPlusPrinterId;
	}
	

	/**
	 * @return the printerDescription
	 */
	@ApiModelProperty(value = "Printer description ( %% - DB O_PRINTER - DESCRIPTION).")
	public String getPrinterDescription() {
		return printerDescription;
	}

	/**
	 * @param printerDescription the printerDescription to set
	 */
	public void setPrinterDescription(String printerDescription) {
		this.printerDescription = printerDescription;
	}
	

	/**
	 * @return the createdBy
	 */
	@ApiModelProperty(value = "printer created by ( %% - DB O_PRINTER - CREATED_BY).")
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
	 * @return the createdTime
	 */
	@ApiModelProperty(value = "Printer created Date time ( %% - DB O_PRINTER - CREATED_TS).")
	public String getCreatedTime() {
		return createdTime;
	}

	/**
	 * @param createdTime the createdTime to set
	 */
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	

	/**
	 * @return the updatedBy
	 */
	@ApiModelProperty(value = "Printer Updated by ( %% - DB O_PRINTER - UPDATED_BY).")
	public String getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * @param updatedBy the updatedBy to set
	 */
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	

	/**
	 * @return the updatedTime
	 */
	@ApiModelProperty(value = "Printer Updated date time ( %% - DB O_PRINTER - UPDATED_TS).")
	public String getUpdatedTime() {
		return updatedTime;
	}

	/**
	 * @param updatedTime the updatedTime to set
	 */
	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}


	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
	

	/**
	 * @return the printerFKTList
	 */
	public List<AdminPrinterFKTDTO> getPrinterFKTList() {
		return printerFKTList;
	}

	/**
	 * @param printerFKTList the printerFKTList to set
	 */
	public void setPrinterFKTList(List<AdminPrinterFKTDTO> printerFKTList) {
		this.printerFKTList = printerFKTList;
	}
	
	
}