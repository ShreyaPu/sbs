package com.alphax.vo.mb;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about corrections in booking of delivery notes related to invoices data (Komigieren).")
public class CorrectionsInBooking_DTO {

	private String warehouseNumber;
	
	private String businessCases;
	
	private String partMovementDate;
	
	private String userName;
	
	private String manufacturer;
	
	private String partNumber;
	
	private String partName;
	
	private String bookingAmount;
	
	private String netShoppingPrice;
	
	private String supplierNumber;
	
	private String deliveryNoteNo;
	
	private String salesOrderPosition;
	
	
	@ApiModelProperty(value = "Lager (warehouseNumber) ")
	@NotBlank
	@Size(max=2)
	public String getWarehouseNumber() {
		return warehouseNumber;
	}

	public void setWarehouseNumber(String warehouseNumber) {
		this.warehouseNumber = warehouseNumber;
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
	@NotBlank
	public String getPartMovementDate() {
		return partMovementDate;
	}

	public void setPartMovementDate(String partMovementDate) {
		this.partMovementDate = partMovementDate;
	}

	@ApiModelProperty(value = "Name des Buchers (User) ")
	@NotBlank
	@Size(max=10)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@ApiModelProperty(value = "Hersteller OEM (Manufacturer) ")
	@NotBlank
	@Size(max=4)
	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	@ApiModelProperty(value = "Teilenummer (Part Number)")
	@NotBlank
	@Size(max=30)
	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}
	
	
	public String getPartName() {
		return partName;
	}


	public void setPartName(String partName) {
		this.partName = partName;
	}

	@ApiModelProperty(value = "Gebuchte Menge (Booking Amount)")
	@NotBlank
	public String getBookingAmount() {
		return bookingAmount;
	}

	public void setBookingAmount(String bookingAmount) {
		this.bookingAmount = bookingAmount;
	}


	@ApiModelProperty(value = "Einkaufsnettopreis (Net Shopping Price)")
	@NotBlank
	public String getNetShoppingPrice() {
		return netShoppingPrice;
	}

	public void setNetShoppingPrice(String netShoppingPrice) {
		this.netShoppingPrice = netShoppingPrice;
	}

	@ApiModelProperty(value = "Lieferantennummer (supplier Number)")
	@NotBlank
	@Size(max=5)
	public String getSupplierNumber() {
		return supplierNumber;
	}


	public void setSupplierNumber(String supplierNumber) {
		this.supplierNumber = supplierNumber;
	}


	@ApiModelProperty(value = "Lieferschein (sales Order Number)")
	@NotBlank
	@Size(max=10)
	public String getDeliveryNoteNo() {
		return deliveryNoteNo;
	}


	public void setDeliveryNoteNo(String deliveryNoteNo) {
		this.deliveryNoteNo = deliveryNoteNo;
	}


	@ApiModelProperty(value = "Position (sales Order Position)")
	@NotBlank
	@Size(max=3)
	public String getSalesOrderPosition() {
		return salesOrderPosition;
	}


	public void setSalesOrderPosition(String salesOrderPosition) {
		this.salesOrderPosition = salesOrderPosition;
	}
}
