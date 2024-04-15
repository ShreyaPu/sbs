package com.alphax.vo.mb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about created inventory information")
public class InventoryDetailsDTO {
	
	@ApiModelProperty(value = "Lager - E_I000[warehouseNo] - I_LNR")
	private String warehouseNo;
	
	@ApiModelProperty(value = "Datum - E_I000[warehouseNo] - I_INVDAT")
	private String date;
	
	@ApiModelProperty(value = "Hersteller - E_I000[warehouseNo] - I_HERST")
	private String manufacturer;
	
	@ApiModelProperty(value = "Teilenummer - E_I000[warehouseNo] - I_ETNR")
	private String partNo;
	
	@ApiModelProperty(value = "Bezeichnung - E_I000[warehouseNo] - I_BEN")
	private String partDescription;
	
	@ApiModelProperty(value = "Verkaufspreis - E_I000[warehouseNo] - I_VPR")
	private String sellingPrice;
	
	@ApiModelProperty(value = "DAK - E_I000[warehouseNo] - I_DAK")
	private String dak;
	
	@ApiModelProperty(value = "Marke - E_I000[warehouseNo] - I_MARKE")
	private String brand;
	
	@ApiModelProperty(value = "Rabattgruppe - E_I000[warehouseNo] - I_RABGR")
	private String discountGroup;
	
	@ApiModelProperty(value = "Lagerort - E_I000[warehouseNo] - I_LOPA")
	private String storageLocation;
	
	@ApiModelProperty(value = "Paginier-Nummer - E_I000[warehouseNo] - I_LFDNR")
	private String paginationNo;
	
	@ApiModelProperty(value = "Zählmenge - E_I000[warehouseNo] - I_BESTAND")
	private String countingAmount;
	
	@ApiModelProperty(value = "Erfass-Datum - E_I000[warehouseNo] - I_ERFDAT")
	private String acquisitionDate;
	
	@ApiModelProperty(value = "Erfass-Uhrzeit - E_I000[warehouseNo] - I_ERFTIME")
	private String acquisitionTime;

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


	
}
