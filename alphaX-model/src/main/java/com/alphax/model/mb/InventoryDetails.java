package com.alphax.model.mb;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about created inventory information")
public class InventoryDetails {

	@DBTable(columnName ="I_LNR", required = true)  
	private String warehouseNo;
	
	@DBTable(columnName ="I_INVDAT", required = true)
	private String date;
	
	@DBTable(columnName ="I_HERST", required = true)
	private String manufacturer;
	
	@DBTable(columnName ="I_ETNR", required = true)
	private String partNo;
	
	@DBTable(columnName ="I_BEN", required = true)
	private String partDescription;
	
	@DBTable(columnName ="I_VPR", required = true)
	private String sellingPrice;
	
	@DBTable(columnName ="I_DAK", required = true)
	private String dak;
	
	@DBTable(columnName ="I_MARKE", required = true)
	private String brand;
	
	@DBTable(columnName ="I_RABGR", required = true)
	private String discountGroup;
	
	@DBTable(columnName ="I_LOPA", required = true)  
	private String storageLocation;
	
	@DBTable(columnName ="I_LFDNR", required = true)
	private String paginationNo;
	
	@DBTable(columnName ="I_BESTAND", required = true)
	private String countingAmount;
	
	@DBTable(columnName ="I_ERFDAT", required = true)
	private String acquisitionDate;
	
	@DBTable(columnName ="I_ERFTIME", required = true)
	private String acquisitionTime;
	
	@DBTable(columnName ="LagerName", required = true)
	private String warehouseName;
	
	@DBTable(columnName ="ROWNUMER", required = true)
	private Integer totalCount;
	
	@DBTable(columnName ="COUNT", required = true)
	private Integer inventoryCount;
	
	@DBTable(columnName ="countingPosition", required = true)
	private Integer countingPosition;
	
	@DBTable(columnName ="alreadyRecorded", required = true)
	private Integer alreadyRecordedCount;
	
	@DBTable(columnName ="notRecorded", required = true)
	private Integer notRecordedCount;
	
	@DBTable(columnName ="rejectedCount", required = true)
	private Integer rejectedCount;
	

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
	 * @return the manufacturer
	 */
	public String getManufacturer() {
		return manufacturer;
	}

	/**
	 * @param manufacturer the manufacturer to set
	 */
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	/**
	 * @return the partNo
	 */
	public String getPartNo() {
		return partNo;
	}

	/**
	 * @param partNo the partNo to set
	 */
	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}

	/**
	 * @return the partDescription
	 */
	public String getPartDescription() {
		return partDescription;
	}

	/**
	 * @param partDescription the partDescription to set
	 */
	public void setPartDescription(String partDescription) {
		this.partDescription = partDescription;
	}

	/**
	 * @return the sellingPrice
	 */
	public String getSellingPrice() {
		return sellingPrice;
	}

	/**
	 * @param sellingPrice the sellingPrice to set
	 */
	public void setSellingPrice(String sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	/**
	 * @return the dak
	 */
	public String getDak() {
		return dak;
	}

	/**
	 * @param dak the dak to set
	 */
	public void setDak(String dak) {
		this.dak = dak;
	}

	/**
	 * @return the brand
	 */
	public String getBrand() {
		return brand;
	}

	/**
	 * @param brand the brand to set
	 */
	public void setBrand(String brand) {
		this.brand = brand;
	}

	/**
	 * @return the discountGroup
	 */
	public String getDiscountGroup() {
		return discountGroup;
	}

	/**
	 * @param discountGroup the discountGroup to set
	 */
	public void setDiscountGroup(String discountGroup) {
		this.discountGroup = discountGroup;
	}

	/**
	 * @return the storageLocation
	 */
	public String getStorageLocation() {
		return storageLocation;
	}

	/**
	 * @param storageLocation the storageLocation to set
	 */
	public void setStorageLocation(String storageLocation) {
		this.storageLocation = storageLocation;
	}

	/**
	 * @return the paginationNo
	 */
	public String getPaginationNo() {
		return paginationNo;
	}

	/**
	 * @param paginationNo the paginationNo to set
	 */
	public void setPaginationNo(String paginationNo) {
		this.paginationNo = paginationNo;
	}

	/**
	 * @return the countingAmount
	 */
	public String getCountingAmount() {
		return countingAmount;
	}

	/**
	 * @param countingAmount the countingAmount to set
	 */
	public void setCountingAmount(String countingAmount) {
		this.countingAmount = countingAmount;
	}

	/**
	 * @return the acquisitionDate
	 */
	public String getAcquisitionDate() {
		return acquisitionDate;
	}

	/**
	 * @param acquisitionDate the acquisitionDate to set
	 */
	public void setAcquisitionDate(String acquisitionDate) {
		this.acquisitionDate = acquisitionDate;
	}

	/**
	 * @return the acquisitionTime
	 */
	public String getAcquisitionTime() {
		return acquisitionTime;
	}

	/**
	 * @param acquisitionTime the acquisitionTime to set
	 */
	public void setAcquisitionTime(String acquisitionTime) {
		this.acquisitionTime = acquisitionTime;
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
	 * @return the totalCount
	 */
	public Integer getTotalCount() {
		return totalCount;
	}

	/**
	 * @param totalCount the totalCount to set
	 */
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * @return the inventoryCount
	 */
	public Integer getInventoryCount() {
		return inventoryCount;
	}

	/**
	 * @param inventoryCount the inventoryCount to set
	 */
	public void setInventoryCount(Integer inventoryCount) {
		this.inventoryCount = inventoryCount;
	}

	/**
	 * @return the countingPosition
	 */
	public Integer getCountingPosition() {
		return countingPosition;
	}

	/**
	 * @param countingPosition the countingPosition to set
	 */
	public void setCountingPosition(Integer countingPosition) {
		this.countingPosition = countingPosition;
	}

	/**
	 * @return the alreadyRecordedCount
	 */
	public Integer getAlreadyRecordedCount() {
		return alreadyRecordedCount;
	}

	/**
	 * @param alreadyRecordedCount the alreadyRecordedCount to set
	 */
	public void setAlreadyRecordedCount(Integer alreadyRecordedCount) {
		this.alreadyRecordedCount = alreadyRecordedCount;
	}

	/**
	 * @return the notRecordedCount
	 */
	public Integer getNotRecordedCount() {
		return notRecordedCount;
	}

	/**
	 * @param notRecordedCount the notRecordedCount to set
	 */
	public void setNotRecordedCount(Integer notRecordedCount) {
		this.notRecordedCount = notRecordedCount;
	}

	/**
	 * @return the rejectedCount
	 */
	public Integer getRejectedCount() {
		return rejectedCount;
	}

	/**
	 * @param rejectedCount the rejectedCount to set
	 */
	public void setRejectedCount(Integer rejectedCount) {
		this.rejectedCount = rejectedCount;
	}
	
}