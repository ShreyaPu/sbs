package com.alphax.vo.mb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about created inventory Evalution information")
public class InventoryEvalutionDTO {

	@ApiModelProperty(value = "Lager - E_I000[warehouseNo][year] - i_vfnr")
	private String warehouseNo;
	
	@ApiModelProperty(value = "Hersteller - E_I000[warehouseNo][year] - i_herst")
	private String manufacturer;
	
	@ApiModelProperty(value = "Teilenummer - E_I000[warehouseNo][year] - i_etnr")
	private String partNumber;
	
	@ApiModelProperty(value = "Bezeichnung - E_ETSTAMM - BENEN")
	private String partName;
	
	@ApiModelProperty(value = "aktueller DAK - E_ETSTAMM - DAK")
	private String akt_Dak;
	
	@ApiModelProperty(value = "aktueller Bestand - E_ETSTAMM - AKTBES")
	private String countingAmount;
	
	@ApiModelProperty(value = "Differenz (Stück) ")
	private String differnceStuck;
	
	@ApiModelProperty(value = "Differenz (EUR) ")
	private String differnceEUR;
	
	@ApiModelProperty(value = "DAK - E_I000[warehouseNo][year] - i_dak")
	private String dak;
	
	@ApiModelProperty(value = "Inventur Name - E_I000[warehouseNo][year] - i_INVDAT ||' ' || I_TIME ")
	private String inventoryName;
	
	@ApiModelProperty(value = "Sollbestand - E_I000[warehouseNo][year] - I_altbest")
	private String targetStock;
	
	@ApiModelProperty(value = "gezählt alle LOPA - E_I000[warehouseNo][year] - I_erfbest")
	private String storageCount;
	

	public InventoryEvalutionDTO() {
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
	 * @return the partNumber
	 */
	public String getPartNumber() {
		return partNumber;
	}


	/**
	 * @param partNumber the partNumber to set
	 */
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}


	/**
	 * @return the partName
	 */
	public String getPartName() {
		return partName;
	}


	/**
	 * @param partName the partName to set
	 */
	public void setPartName(String partName) {
		this.partName = partName;
	}


	/**
	 * @return the akt_Dak
	 */
	public String getAkt_Dak() {
		return akt_Dak;
	}


	/**
	 * @param akt_Dak the akt_Dak to set
	 */
	public void setAkt_Dak(String akt_Dak) {
		this.akt_Dak = akt_Dak;
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
	 * @return the differnceStuck
	 */
	public String getDiffernceStuck() {
		return differnceStuck;
	}


	/**
	 * @param differnceStuck the differnceStuck to set
	 */
	public void setDiffernceStuck(String differnceStuck) {
		this.differnceStuck = differnceStuck;
	}


	/**
	 * @return the differnceEUR
	 */
	public String getDiffernceEUR() {
		return differnceEUR;
	}


	/**
	 * @param differnceEUR the differnceEUR to set
	 */
	public void setDiffernceEUR(String differnceEUR) {
		this.differnceEUR = differnceEUR;
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
	 * @return the inventoryName
	 */
	public String getInventoryName() {
		return inventoryName;
	}


	/**
	 * @param inventoryName the inventoryName to set
	 */
	public void setInventoryName(String inventoryName) {
		this.inventoryName = inventoryName;
	}


	/**
	 * @return the targetStock
	 */
	public String getTargetStock() {
		return targetStock;
	}


	/**
	 * @param targetStock the targetStock to set
	 */
	public void setTargetStock(String targetStock) {
		this.targetStock = targetStock;
	}


	/**
	 * @return the storageCount
	 */
	public String getStorageCount() {
		return storageCount;
	}


	/**
	 * @param storageCount the storageCount to set
	 */
	public void setStorageCount(String storageCount) {
		this.storageCount = storageCount;
	}
	
	
}