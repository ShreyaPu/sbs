package com.alphax.model.mb;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about inventory archived counting list")
public class InventoryArchivedCounting {

	@DBTable(columnName ="I_INVDAT", required = true)  
	private String inventoryDate;
	
	@DBTable(columnName ="I_TIME", required = true)  
	private String inventoryTime;
	
	@DBTable(columnName ="Datum", required = true)  
	private String date;
	
	@DBTable(columnName ="Inventurkennung", required = true)  
	private String inventoryId;
	
	@DBTable(columnName ="Anzahl_Paginiernummern", required = true)  
	private String numberOfPagination;
	
	@DBTable(columnName ="xLine", required = true)  
	private String xLine;

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
