package com.alphax.vo.mb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about inventory archived list of differences")
public class InventoryArchivedDifferencesDTO {

	@ApiModelProperty(value = "Differenzenliste - AE4000[warehouseNo][year] - daten")
	private String differenceList;
	
	@ApiModelProperty(value = "List_ID - AE4000[warehouseNo][year] - daten")
	private String listId;
	
	@ApiModelProperty(value = "Datum - AE4000[warehouseNo][year] - daten")
	private String date;
	
	@ApiModelProperty(value = "Inventurkennung - AE4000[warehouseNo][year] - daten")
	private String inventoryId;
	
	@ApiModelProperty(value = "Zähldatum - AE4000[warehouseNo][year] - daten")
	private String countDate;
	
	@ApiModelProperty(value = "Lager - AE4000[warehouseNo][year] - daten")
	private String warehouseNo;
	
	//For Details
	
	@ApiModelProperty(value = "RAW_CONTENT - AE4000[warehouseNo][year] - daten")
	private String rawContent;
	
	@ApiModelProperty(value = "NewPage - AE4000[warehouseNo][year] - daten")
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
