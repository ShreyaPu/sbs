package com.alphax.vo.mb;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about created inventory information")
public class InventoryDTO {

	@ApiModelProperty(value = "Lager Number - O_INVLIST - Warehouse_ID")
	private String warehouseNo;
	
	@ApiModelProperty(value = "Lager Name - O_INVLIST - WAREHOUSE_NAME")
	private String warehouseName;

	@ApiModelProperty(value = "Current Date")
	private String currentDate;
	
	@ApiModelProperty(value = "Number of parts record type 1 ( Anzahl Teile Satzart 1 )")
	private String numberOfPartsType1;
	
	@ApiModelProperty(value = "Number of parts record type 2 ( Anzahl Teile Satzart 2 )")
	private String numberOfPartsType2;
	
	@ApiModelProperty(value = "Total counting position ( Zählpositionen gesamte Zahl ) - O_INVPART")
	private String totalCountingPosition;
	
	@ApiModelProperty(value = "already recorded ( bereits erfasst ) - O_INVPART")
	private String alreadyRecordedCounts;
	
	@ApiModelProperty(value = "not recorded ( nicht erfasste )" )
	private String notRecordedCounts;	
	
	@ApiModelProperty(value = "Rejected ( abgewiesen )" )
	private String rejectedCounts;
	
	@ApiModelProperty(value = "inventoryList Status Id - O_INVLIST-INVLISTSTS_ID " )
	private String inventoryList_Status_Id;
	
	@ApiModelProperty(value = "Sort by clouse -  O_INVLIST (SORT_BY)")
	private String sortBy;
	
	@ApiModelProperty(value = "User1 -  O_INVPART (USER1)")
	private String user1;
	
	@ApiModelProperty(value = "User2 -  O_INVPART (USER2)")
	private String user2;
	
	

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
	 * @return the currentDate
	 */
	public String getCurrentDate() {
		return currentDate;
	}

	/**
	 * @param currentDate the currentDate to set
	 */
	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	/**
	 * @return the numberOfPartsType1
	 */
	public String getNumberOfPartsType1() {
		return numberOfPartsType1;
	}

	/**
	 * @param numberOfPartsType1 the numberOfPartsType1 to set
	 */
	public void setNumberOfPartsType1(String numberOfPartsType1) {
		this.numberOfPartsType1 = numberOfPartsType1;
	}

	/**
	 * @return the numberOfPartsType2
	 */
	public String getNumberOfPartsType2() {
		return numberOfPartsType2;
	}

	/**
	 * @param numberOfPartsType2 the numberOfPartsType2 to set
	 */
	public void setNumberOfPartsType2(String numberOfPartsType2) {
		this.numberOfPartsType2 = numberOfPartsType2;
	}

	/**
	 * @return the totalCountingPosition
	 */
	public String getTotalCountingPosition() {
		return totalCountingPosition;
	}

	/**
	 * @param totalCountingPosition the totalCountingPosition to set
	 */
	public void setTotalCountingPosition(String totalCountingPosition) {
		this.totalCountingPosition = totalCountingPosition;
	}

	/**
	 * @return the alreadyRecordedCounts
	 */
	public String getAlreadyRecordedCounts() {
		return alreadyRecordedCounts;
	}

	/**
	 * @param alreadyRecordedCounts the alreadyRecordedCounts to set
	 */
	public void setAlreadyRecordedCounts(String alreadyRecordedCounts) {
		this.alreadyRecordedCounts = alreadyRecordedCounts;
	}

	/**
	 * @return the notRecordedCounts
	 */
	public String getNotRecordedCounts() {
		return notRecordedCounts;
	}

	/**
	 * @param notRecordedCounts the notRecordedCounts to set
	 */
	public void setNotRecordedCounts(String notRecordedCounts) {
		this.notRecordedCounts = notRecordedCounts;
	}

	/**
	 * @return the rejectedCounts
	 */
	public String getRejectedCounts() {
		return rejectedCounts;
	}

	/**
	 * @param rejectedCounts the rejectedCounts to set
	 */
	public void setRejectedCounts(String rejectedCounts) {
		this.rejectedCounts = rejectedCounts;
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

	/**
	 * @return the user1
	 */
	public String getUser1() {
		return user1;
	}

	/**
	 * @param user1 the user1 to set
	 */
	public void setUser1(String user1) {
		this.user1 = user1;
	}

	/**
	 * @return the user2
	 */
	public String getUser2() {
		return user2;
	}

	/**
	 * @param user2 the user2 to set
	 */
	public void setUser2(String user2) {
		this.user2 = user2;
	}
	
	
	
}