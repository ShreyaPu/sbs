package com.alphax.vo.mb;

/**
 * @author A106744104
 *
 */
public class ReportHeaderDTO {
	
	private String reportName;	
	private String reportDate;
	private String inventoryListName;
	private String warehouseId;
	private String warehouseName;
	private String activationDate;
	private String axCompanyName;
	
	
	public ReportHeaderDTO() {		
	}
	

	/**
	 * @return the reportName
	 */
	public String getReportName() {
		return reportName;
	}

	/**
	 * @param reportName the reportName to set
	 */
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	/**
	 * @return the reportDate
	 */
	public String getReportDate() {
		return reportDate;
	}

	/**
	 * @param reportDate the reportDate to set
	 */
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}

	/**
	 * @return the inventoryListName
	 */
	public String getInventoryListName() {
		return inventoryListName;
	}

	/**
	 * @param inventoryListName the inventoryListName to set
	 */
	public void setInventoryListName(String inventoryListName) {
		this.inventoryListName = inventoryListName;
	}

	/**
	 * @return the warehouseId
	 */
	public String getWarehouseId() {
		return warehouseId;
	}

	/**
	 * @param warehouseId the warehouseId to set
	 */
	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

	/**
	 * @return the warehouseName
	 */
	public String getWarehouseName() {
		return warehouseName;
	}

	/**
	 * @param warehouseName the warehouseName to set
	 */
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	/**
	 * @return the activationDate
	 */
	public String getActivationDate() {
		return activationDate;
	}

	/**
	 * @param activationDate the activationDate to set
	 */
	public void setActivationDate(String activationDate) {
		this.activationDate = activationDate;
	}

	/**
	 * @return the axCompanyName
	 */
	public String getAxCompanyName() {
		return axCompanyName;
	}

	/**
	 * @param axCompanyName the axCompanyName to set
	 */
	public void setAxCompanyName(String axCompanyName) {
		this.axCompanyName = axCompanyName;
	}
	
}