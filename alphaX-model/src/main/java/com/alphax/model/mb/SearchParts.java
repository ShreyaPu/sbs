package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

/**
 * @author A106744104
 *
 */
public class SearchParts {

	// Original Equipment Manufacturer (Hersteller - OEM).
	@DBTable(columnName ="herst", required = true)
	private String oem;
	
	// The part number (Teilenummer).
	@DBTable(columnName ="tnr", required = true)
	private String partNumber;
	
	// Warehouse Information (Lagernummer / Lagerbezeichnung).
	@DBTable(columnName ="lnr", required = true)
	private String warehouse;
	
	// Agency information (Filialnummer / Filialbezeichnung).
	@DBTable(columnName ="filial", required = true)
	private String agency;
	
	// Description of the part (Bezeichnung).
	@DBTable(columnName ="benen", required = true)
	private String description;
	
	// Stock information for part (Bestand).
	@DBTable(columnName ="aktbes", required = true)
	private String stock;
	
	// Brand information for part (Teilemarke).
	@DBTable(columnName ="tmarke", required = true)
	private String oemBrand;
	
	@DBTable(columnName ="ROWNUMER", required = true)
	private Integer totalCount;	
	
	// WarehouseName Information (Lagername).
	@DBTable(columnName ="LagerName", required = true)
	private String alphaXWarehouseName;
		
	@DBTable(columnName ="EPR", required = true)
	private String price;	
		
	@DBTable(columnName ="VKAVGW", required = true)
	private BigDecimal averageSales;
		
	@DBTable(columnName ="DTLABG", required = true)
	private BigDecimal lastDisposalDate;
	

	@DBTable(columnName ="LOPA", required = true)
	private String storageLocation;

	@DBTable(columnName = "SA", required = true)
	private BigDecimal storageIndikator;
	
	@DBTable(columnName = "RG", required = true)
	private String discountGroup;
	
	@DBTable(columnName = "surcharge_price_limit", required = true)
	private String surchargePriceLimit;

	
	public SearchParts() {
		
	}
	
	
	/**
	 * @return the oem
	 */
	public String getOem() {
		return oem;
	}
	
	/**
	 * @param oem the oem to set
	 */
	public void setOem(String oem) {
		this.oem = oem;
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
	 * @return the warehouse
	 */
	public String getWarehouse() {
		return warehouse;
	}
	
	/**
	 * @param warehouse the warehouse to set
	 */
	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}
	
	/**
	 * @return the agency
	 */
	public String getAgency() {
		return agency;
	}
	
	/**
	 * @param agency the agency to set
	 */
	public void setAgency(String agency) {
		this.agency = agency;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @return the stock
	 */
	public String getStock() {
		return stock;
	}
	
	/**
	 * @param stock the stock to set
	 */
	public void setStock(String stock) {
		this.stock = stock;
	}


	/**
	 * @return the oemBrand
	 */
	public String getOemBrand() {
		return oemBrand;
	}


	/**
	 * @param oemBrand the oemBrand to set
	 */
	public void setOemBrand(String oemBrand) {
		this.oemBrand = oemBrand;
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
	 * @return the alphaXWarehouseName
	 */
	public String getAlphaXWarehouseName() {
		return alphaXWarehouseName;
	}


	/**
	 * @param alphaXWarehouseName the alphaXWarehouseName to set
	 */
	public void setAlphaXWarehouseName(String alphaXWarehouseName) {
		this.alphaXWarehouseName = alphaXWarehouseName;
	}


	/**
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}


	/**
	 * @param price the price to set
	 */
	public void setPrice(String price) {
		this.price = price;
	}


	/**
	 * @return the averageSales
	 */
	public BigDecimal getAverageSales() {
		return averageSales;
	}


	/**
	 * @param averageSales the averageSales to set
	 */
	public void setAverageSales(BigDecimal averageSales) {
		this.averageSales = averageSales;
	}


	/**
	 * @return the lastDisposalDate
	 */
	public BigDecimal getLastDisposalDate() {
		return lastDisposalDate;
	}


	/**
	 * @param lastDisposalDate the lastDisposalDate to set
	 */
	public void setLastDisposalDate(BigDecimal lastDisposalDate) {
		this.lastDisposalDate = lastDisposalDate;
	}


	/**
	 * @return the storageLocation
	 */
	public String getStorageLocation() {
		return storageLocation;
	}
	
	public BigDecimal getStorageIndikator() {
		return storageIndikator;
	}


	/**
	 * @param storageLocation the storageLocation to set
	 */
	public void setStorageLocation(String storageLocation) {
		this.storageLocation = storageLocation;
	}

	public void setStorageIndikator(BigDecimal storageIndikator) {
		this.storageIndikator = storageIndikator;
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
	 * @return the surchargePriceLimit
	 */
	public String getSurchargePriceLimit() {
		return surchargePriceLimit;
	}


	/**
	 * @param surchargePriceLimit the surchargePriceLimit to set
	 */
	public void setSurchargePriceLimit(String surchargePriceLimit) {
		this.surchargePriceLimit = surchargePriceLimit;
	}
	
	
	
}