package com.alphax.vo.mb;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "Inventory List [O_INVLIST] table DTO ")
public class ActivatedCountingListDTO {

	private String inventoryListId;
	
	private String inventoryListName;
	
	private String inventoryList_Status_Id;
	
	private String inventoryList_KindId;
	
	private String warehouseId;
	
	private String warehouseName;
	
	private String creationDate;
	
	private String inventoryCount;
	
	private String statusDescription;
	
	private String inventoryPartsCount;
	
	private String sortBy;
	
	
	public ActivatedCountingListDTO() {
	
	}


	/**
	 * @return the inventoryListId
	 */
	public String getInventoryListId() {
		return inventoryListId;
	}


	/**
	 * @param inventoryListId the inventoryListId to set
	 */
	public void setInventoryListId(String inventoryListId) {
		this.inventoryListId = inventoryListId;
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
	 * @return the inventoryList_Status_Id
	 */
	public String getInventoryList_Status_Id() {
		return inventoryList_Status_Id;
	}


	/**
	 * @param inventoryList_Status_Id the inventoryList_Status_Id to set
	 */
	public void setInventoryList_Status_Id(String inventoryList_Status_Id) {
		this.inventoryList_Status_Id = inventoryList_Status_Id;
	}


	/**
	 * @return the inventoryList_KindId
	 */
	public String getInventoryList_KindId() {
		return inventoryList_KindId;
	}


	/**
	 * @param inventoryList_KindId the inventoryList_KindId to set
	 */
	public void setInventoryList_KindId(String inventoryList_KindId) {
		this.inventoryList_KindId = inventoryList_KindId;
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
	 * @return the creationDate
	 */
	public String getCreationDate() {
		return creationDate;
	}


	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}


	/**
	 * @return the inventoryCount
	 */
	public String getInventoryCount() {
		return inventoryCount;
	}


	/**
	 * @param inventoryCount the inventoryCount to set
	 */
	public void setInventoryCount(String inventoryCount) {
		this.inventoryCount = inventoryCount;
	}


	/**
	 * @return the statusDescription
	 */
	public String getStatusDescription() {
		return statusDescription;
	}


	/**
	 * @param statusDescription the statusDescription to set
	 */
	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}


	/**
	 * @return the inventoryPartsCount
	 */
	public String getInventoryPartsCount() {
		return inventoryPartsCount;
	}


	/**
	 * @param inventoryPartsCount the inventoryPartsCount to set
	 */
	public void setInventoryPartsCount(String inventoryPartsCount) {
		this.inventoryPartsCount = inventoryPartsCount;
	}


	/**
	 * @return the sortBy
	 */
	public String getSortBy() {
		return sortBy;
	}


	/**
	 * @param sortBy the sortBy to set
	 */
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
	
}