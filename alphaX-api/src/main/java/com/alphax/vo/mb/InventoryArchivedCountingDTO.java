package com.alphax.vo.mb;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about inventory archived counting list")
public class InventoryArchivedCountingDTO {

	@ApiModelProperty(value = "Formated Date - E_I000[warehouseNo][year] - datum")
	private String date;
	
	@ApiModelProperty(value = "Inventur-kennung - E_I000[warehouseNo][year] - Inventurkennung")
	private String inventoryId;
	
	@ApiModelProperty(value = "Anzahl Paginiernummern - E_I000[warehouseNo][year] - Anzahl_Paginiernummern") 
	private String numberOfPagination;
	
	@ApiModelProperty(value = "Inventory Date - E_I000[warehouseNo][year] - I_INVDAT")
	private String inventoryDate;
	
	@ApiModelProperty(value = "Inventory Time - E_I000[warehouseNo][year] - I_TIME")
	private String inventoryTime;
	
	@ApiModelProperty(value = "Details value - E_I000[warehouseNo][year] - xLine")
	private String xLine;

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the inventoryId
	 */
	public String getInventoryId() {
		return inventoryId;
	}

	/**
	 * @param inventoryId the inventoryId to set
	 */
	public void setInventoryId(String inventoryId) {
		this.inventoryId = inventoryId;
	}

	/**
	 * @return the numberOfPagination
	 */
	public String getNumberOfPagination() {
		return numberOfPagination;
	}

	/**
	 * @param numberOfPagination the numberOfPagination to set
	 */
	public void setNumberOfPagination(String numberOfPagination) {
		this.numberOfPagination = numberOfPagination;
	}

	/**
	 * @return the inventoryDate
	 */
	public String getInventoryDate() {
		return inventoryDate;
	}

	/**
	 * @param inventoryDate the inventoryDate to set
	 */
	public void setInventoryDate(String inventoryDate) {
		this.inventoryDate = inventoryDate;
	}

	/**
	 * @return the inventoryTime
	 */
	public String getInventoryTime() {
		return inventoryTime;
	}

	/**
	 * @param inventoryTime the inventoryTime to set
	 */
	public void setInventoryTime(String inventoryTime) {
		this.inventoryTime = inventoryTime;
	}

	/**
	 * @return the xLine
	 */
	public String getxLine() {
		return xLine;
	}

	/**
	 * @param xLine the xLine to set
	 */
	public void setxLine(String xLine) {
		this.xLine = xLine;
	}
	
	
	
}
