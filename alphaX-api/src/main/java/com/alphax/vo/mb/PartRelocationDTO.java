package com.alphax.vo.mb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about Part Relocation Details")
public class PartRelocationDTO {

	private String warehouseName;
	
	private String price;
	
	private String bestand;
	
	private String warehouseNumber;
	
	private String warehouseCity;
	
	private String numberofQuantity;
	
	private String movementType;
	
	private String printer;
	
	private String note;
	
	private String averageSales;
	
	private String lastDisposalDate;
	
	
	/**
	 * @return the warehouseName
	 */
	@ApiModelProperty(value = "Warehouse Name (Lagername) ( %% - DB E_ETSTAMK4 - PNAME + PORT).")
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
	@ApiModelProperty(value = "Price (Preis) ( %% - DB E_ETSTAMM - DAK ).")
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
	@ApiModelProperty(value = "Bestand (Bestand) ( %% - DB E_ETSTAMM - AKTBES).")
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
	@ApiModelProperty(value = "Warehouse Number (WarehouseNummer) ( %% - DB E_ETSTAMM - LNR).")
	public String getWarehouseNumber() {
		return warehouseNumber;
	}

	/**
	 * @param warehouseNumber the warehouseNumber to set
	 */
	public void setWarehouseNumber(String warehouseNumber) {
		this.warehouseNumber = warehouseNumber;
	}

	@ApiModelProperty(value = "Warehouse City (WarehouseCity) ( %% - DB E_ETSTAMM - PORT).")
	public String getWarehouseCity() {
		return warehouseCity;
	}

	public void setWarehouseCity(String warehouseCity) {
		this.warehouseCity = warehouseCity;
	}

	@ApiModelProperty(value = "Number of Quantity (Anzahl) ( %% - DB E_ETSTAMK4.")
	public String getNumberofQuantity() {
		return numberofQuantity;
	}

	public void setNumberofQuantity(String numberofQuantity) {
		this.numberofQuantity = numberofQuantity;
	}

	
	@ApiModelProperty(value = "Movement Type BA (Bewegungsart BA) ( %% - DB E_ETSTAMK4 ).")
	public String getMovementType() {
		return movementType;
	}

	public void setMovementType(String movementType) {
		this.movementType = movementType;
	}

	@ApiModelProperty(value = "Printer (Drucker) ( %% -  ).")
	public String getPrinter() {
		return printer;
	}

	public void setPrinter(String printer) {
		this.printer = printer;
	}

	@ApiModelProperty(value = "Note (Hinweis) ( %% - DB E_ETSTAMM - DAK ).")
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @return the averageSales
	 */
	@ApiModelProperty(value = "Average Sales (Verkauf letzte 12 Monate) ( %% - DB E_ETSTAMM - VKAVGW).")
	public String getAverageSales() {
		return averageSales;
	}

	/**
	 * @param averageSales the averageSales to set
	 */
	public void setAverageSales(String averageSales) {
		this.averageSales = averageSales;
	}

	/**
	 * @return the lastDisposalDate
	 */
	@ApiModelProperty(value = "LastDisposal Date (Letzter Abgang) ( %% - DB E_ETSTAMM - DTLABG).")
	public String getLastDisposalDate() {
		return lastDisposalDate;
	}

	/**
	 * @param lastDisposalDate the lastDisposalDate to set
	 */
	public void setLastDisposalDate(String lastDisposalDate) {
		this.lastDisposalDate = lastDisposalDate;
	}
	
	
}