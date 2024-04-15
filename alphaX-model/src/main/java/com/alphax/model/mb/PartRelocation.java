package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about Part Relocation Details")
public class PartRelocation {
	
	@DBTable(columnName ="PNAME", required = true)
	private String warehouseName;
	
	@DBTable(columnName ="EPR", required = true)
	private String price;
	
	@DBTable(columnName ="AKTBES", required = true)
	private String bestand;
	
	@DBTable(columnName ="LNR", required = true)
	private String warehouseNumber;
	
	@DBTable(columnName ="PORT", required = true)
	private String warehouseCity;
	
	@DBTable(columnName ="VKAVGW", required = true)
	private BigDecimal averageSales;
	
	@DBTable(columnName ="DTLABG", required = true)
	private BigDecimal lastDisposalDate;
	
	@DBTable(columnName ="LagerName", required = true)
	private String alphaXWarehouseName;
	
	@DBTable(columnName ="printer", required = true)
	private String printer;
	
	@DBTable(columnName ="DATEN", required = true)
	private String daten;

	
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
	 * @return the bestand
	 */
	public String getBestand() {
		return bestand;
	}

	/**
	 * @param bestand the bestand to set
	 */
	public void setBestand(String bestand) {
		this.bestand = bestand;
	}
	

	/**
	 * @return the warehouseNumber
	 */
	public String getWarehouseNumber() {
		return warehouseNumber;
	}

	/**
	 * @param warehouseNumber the warehouseNumber to set
	 */
	public void setWarehouseNumber(String warehouseNumber) {
		this.warehouseNumber = warehouseNumber;
	}

	public String getWarehouseCity() {
		return warehouseCity;
	}

	public void setWarehouseCity(String warehouseCity) {
		this.warehouseCity = warehouseCity;
	}

	public BigDecimal getAverageSales() {
		return averageSales;
	}

	public void setAverageSales(BigDecimal averageSales) {
		this.averageSales = averageSales;
	}

	public BigDecimal getLastDisposalDate() {
		return lastDisposalDate;
	}

	public void setLastDisposalDate(BigDecimal lastDisposalDate) {
		this.lastDisposalDate = lastDisposalDate;
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
	 * @return the printer
	 */
	public String getPrinter() {
		return printer;
	}

	/**
	 * @param printer the printer to set
	 */
	public void setPrinter(String printer) {
		this.printer = printer;
	}

	/**
	 * @return the daten
	 */
	public String getDaten() {
		return daten;
	}

	/**
	 * @param daten the daten to set
	 */
	public void setDaten(String daten) {
		this.daten = daten;
	}
	
	
}