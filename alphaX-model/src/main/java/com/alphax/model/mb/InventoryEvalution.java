package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about created inventory Evalution information")
public class InventoryEvalution {

	@DBTable(columnName ="Lager", required = true)  
	private String warehouseNo;
	
	@DBTable(columnName ="Hersteller", required = true)
	private String manufacturer;
	
	@DBTable(columnName ="Teilenummer", required = true)
	private String partNumber;
	
	@DBTable(columnName ="Bezeichnung", required = true)
	private String partName;
	
	@DBTable(columnName ="Aktueller_DAK", required = true)
	private BigDecimal akt_Dak;
	
	@DBTable(columnName ="Aktueller_Bestand", required = true)
	private BigDecimal countingAmount;
	
	@DBTable(columnName ="Differenz_Stuck", required = true)
	private Double differnceStuck;
	
	@DBTable(columnName ="Differenz_EUR", required = true)
	private Double differnceEUR;
	
	@DBTable(columnName ="ROWNUMER", required = true)
	private Integer totalCount;
	
	@DBTable(columnName ="DAK", required = true)
	private BigDecimal dak;
	
	@DBTable(columnName ="Inventur_Name", required = true)
	private String inventoryName;
	
	@DBTable(columnName ="Sollbestand", required = true)
	private BigDecimal targetStock;
	
	@DBTable(columnName ="gezahlt_LOPA", required = true)
	private Double storageCount;
	

	public InventoryEvalution() {
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
	public BigDecimal getAkt_Dak() {
		return akt_Dak;
	}


	/**
	 * @param akt_Dak the akt_Dak to set
	 */
	public void setAkt_Dak(BigDecimal akt_Dak) {
		this.akt_Dak = akt_Dak;
	}


	/**
	 * @return the countingAmount
	 */
	public BigDecimal getCountingAmount() {
		return countingAmount;
	}


	/**
	 * @param countingAmount the countingAmount to set
	 */
	public void setCountingAmount(BigDecimal countingAmount) {
		this.countingAmount = countingAmount;
	}


	/**
	 * @return the differnceStuck
	 */
	public Double getDiffernceStuck() {
		return differnceStuck;
	}


	/**
	 * @param differnceStuck the differnceStuck to set
	 */
	public void setDiffernceStuck(Double differnceStuck) {
		this.differnceStuck = differnceStuck;
	}


	/**
	 * @return the differnceEUR
	 */
	public Double getDiffernceEUR() {
		return differnceEUR;
	}


	/**
	 * @param differnceEUR the differnceEUR to set
	 */
	public void setDiffernceEUR(Double differnceEUR) {
		this.differnceEUR = differnceEUR;
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
	 * @return the dak
	 */
	public BigDecimal getDak() {
		return dak;
	}


	/**
	 * @param dak the dak to set
	 */
	public void setDak(BigDecimal dak) {
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
	public BigDecimal getTargetStock() {
		return targetStock;
	}


	/**
	 * @param targetStock the targetStock to set
	 */
	public void setTargetStock(BigDecimal targetStock) {
		this.targetStock = targetStock;
	}


	/**
	 * @return the storageCount
	 */
	public Double getStorageCount() {
		return storageCount;
	}


	/**
	 * @param storageCount the storageCount to set
	 */
	public void setStorageCount(Double storageCount) {
		this.storageCount = storageCount;
	}
	
}