package com.alphax.model.mb;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about inventory archived list of differences")
public class InventoryArchivedDifferences {

	@DBTable(columnName ="Differenzenliste", required = true)  
	private String differenceList;
	
	@DBTable(columnName ="List_ID", required = true)  
	private String listId;
	
	@DBTable(columnName ="Datum", required = true)  
	private String date;
	
	@DBTable(columnName ="Inventurkennung", required = true)  
	private String inventoryId;
	
	@DBTable(columnName ="Zahldatum", required = true)  
	private String countDate;
	
	@DBTable(columnName ="Lager", required = true)  
	private String warehouseNo;
	
	//For Details
	
	@DBTable(columnName ="RAW_CONTENT", required = true)  
	private String rawContent;
	
	@DBTable(columnName ="NewPage", required = true)  
	private String pageType;

	/**
	 * @return the differenceList
	 */
	public String getDifferenceList() {
		return differenceList;
	}

	/**
	 * @param differenceList the differenceList to set
	 */
	public void setDifferenceList(String differenceList) {
		this.differenceList = differenceList;
	}

	/**
	 * @return the listId
	 */
	public String getListId() {
		return listId;
	}

	/**
	 * @param listId the listId to set
	 */
	public void setListId(String listId) {
		this.listId = listId;
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
	 * @return the countDate
	 */
	public String getCountDate() {
		return countDate;
	}

	/**
	 * @param countDate the countDate to set
	 */
	public void setCountDate(String countDate) {
		this.countDate = countDate;
	}

	/**
	 * @return the warehouseNo
	 */
	public String getWarehouseNo() {
		return warehouseNo;
	}

	/**
	 * @param warehouseNo the warehouseNo to set
	 */
	public void setWarehouseNo(String warehouseNo) {
		this.warehouseNo = warehouseNo;
	}

	/**
	 * @return the rawContent
	 */
	public String getRawContent() {
		return rawContent;
	}

	/**
	 * @param rawContent the rawContent to set
	 */
	public void setRawContent(String rawContent) {
		this.rawContent = rawContent;
	}

	/**
	 * @return the pageType
	 */
	public String getPageType() {
		return pageType;
	}

	/**
	 * @param pageType the pageType to set
	 */
	public void setPageType(String pageType) {
		this.pageType = pageType;
	}
	
	
}
