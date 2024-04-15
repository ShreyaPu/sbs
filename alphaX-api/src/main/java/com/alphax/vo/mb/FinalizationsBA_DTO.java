package com.alphax.vo.mb;

import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about Finalisierungen BA 50 and 67 (Finalizations BA)")
public class FinalizationsBA_DTO {

	private String location;
	
	private String warehouseNumber;
	
	private String businessCases;
	
	private String partMovementDate;
	
	private String userName;
	
	private String manufacturer;
	
	private String partNumber;
	
	private String bookingAmount;
	
	private String inventoryDescription;
	
	private String correctionOrAddition;
	
	private String ignoreBackOrders;
	
	private String pendingOrders;

	private String herkType ;
	
	
	@ApiModelProperty(value = "Firma/Filiale/Lager (Location) ")
	@Size(max=10)
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	@ApiModelProperty(value = "BA Business Cases ")
	@Size(max=2)
	public String getBusinessCases() {
		return businessCases;
	}

	public void setBusinessCases(String businessCases) {
		this.businessCases = businessCases;
	}

	@ApiModelProperty(value = "Datum Teilebewegung (Part Movement Date) ")
	public String getPartMovementDate() {
		return partMovementDate;
	}

	public void setPartMovementDate(String partMovementDate) {
		this.partMovementDate = partMovementDate;
	}

	@ApiModelProperty(value = "Name des Zählers (User) ")
	@Size(max=10)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@ApiModelProperty(value = "Hersteller OEM (Manufacturer) ")
	@Size(max=4)
	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	@ApiModelProperty(value = "Teilenummer (Part Number)")
	@Size(max=30)
	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	@ApiModelProperty(value = "Buchungsmenge (Booking Amount)")
	public String getBookingAmount() {
		return bookingAmount;
	}

	public void setBookingAmount(String bookingAmount) {
		this.bookingAmount = bookingAmount;
	}

	@ApiModelProperty(value = "Inventurbezeichnung (Inventory Description)")
	public String getInventoryDescription() {
		return inventoryDescription;
	}

	public void setInventoryDescription(String inventoryDescription) {
		this.inventoryDescription = inventoryDescription;
	}

	@ApiModelProperty(value = "Korrektur/Hinzufügen (Correction / Addition)")
	@Size(max=1)
	public String getCorrectionOrAddition() {
		return correctionOrAddition;
	}

	public void setCorrectionOrAddition(String correctionOrAddition) {
		this.correctionOrAddition = correctionOrAddition;
	}

	@ApiModelProperty(value = "Ignorieren Bestellausstand (Ignore Back Orders)")
	@Size(max=1)
	public String getIgnoreBackOrders() {
		return ignoreBackOrders;
	}
	
	public void setIgnoreBackOrders(String ignoreBackOrders) {
		this.ignoreBackOrders = ignoreBackOrders;
	}


	@ApiModelProperty(value = "HERK value for BA67")
	public String getHerkType() {
		return herkType;
	}

	public void setHerkType(String herkType) {
		this.herkType = herkType;
	}
	
	/**
	 * @return the pendingOrders
	 */
	@ApiModelProperty(value = "BESAUS (Pending Orders)")
	public String getPendingOrders() {
		return pendingOrders;
	}
	

	/**
	 * @param pendingOrders the pendingOrders to set
	 */
	public void setPendingOrders(String pendingOrders) {
		this.pendingOrders = pendingOrders;
	}

	/**
	 * @return the warehouseNumber
	 */
	@ApiModelProperty(value = "Lager (warehouse Number)")
	public String getWarehouseNumber() {
		return warehouseNumber;
	}

	/**
	 * @param warehouseNumber the warehouseNumber to set
	 */
	public void setWarehouseNumber(String warehouseNumber) {
		this.warehouseNumber = warehouseNumber;
	}
	
		
}