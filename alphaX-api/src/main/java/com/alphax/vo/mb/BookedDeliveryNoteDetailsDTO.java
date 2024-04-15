package com.alphax.vo.mb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about invoiced details and delivery note details for items of a delivery note")
public class BookedDeliveryNoteDetailsDTO {

	@ApiModelProperty(value = "Input warehouse number")
	private String warehouseNumber;
	
	@ApiModelProperty(value = "Input Manufacturer (OEM)")
	private String manufacturer;
	
	@ApiModelProperty(value = "Input Supplier number (Lieferant) ")
	private String supplierNumber;  //Lieferant
	
	@ApiModelProperty(value = "Input current date ")
	private String partMovementDate;
	
	@ApiModelProperty(value = "login user name")
	private String userName;
	
	@ApiModelProperty(value = "Delivery Note Number (Lieferschein) ( %% - DB O_SA61 - deliverynotenr).")
	private String deliveryNoteNo; //Lieferschein
	
	@ApiModelProperty(value = "Delivery Note Position (POS) ( %% - DB O_SA61 - deliverynotelinenr).")
	private String deliveryNotePosition; //POS
	
	@ApiModelProperty(value = "Part Number (deliveredpartnr) ( %% - DB O_SA61 - deliveredpartnr).")
	private String partNumber; //TNR
	
	@ApiModelProperty(value = "Designation (Bezeichnung) ( %% - DB O_SA61 - description).")
	private String designation; //Bezeichnung
	
	@ApiModelProperty(value = "Quantity According To Invoice (Menge lt. Rechnung) ( %% - DB O_SA61 - invoicedqty).")
	private String quantityAccordingToInvoice;  //Menge lt. Rechnung
	
	@ApiModelProperty(value = "VAT (MWST) ( %% - DB O_SA61 - vatcode).")
	private String vat; //MWST
	
	@ApiModelProperty(value = "Unit Price According To Invoice (Stückpreis lt. Rechnung) ( %% - DB O_SA61).")
	private String unitPriceAccordingToInvoice; //Stückpreis lt. Rechnung
	
	@ApiModelProperty(value = "Posted Amount (Gebuchte Menge) ( %% - DB O_SA61).")
	private String postedAmount; //Gebuchte Menge
	
	@ApiModelProperty(value = "Booked Price (Gebuchter Preis) ( %% - DB O_SA61).")
	private String bookedPrice;  //Gebuchter Preis

	@ApiModelProperty(value = "Input correct amount ")
	private String correctAmount;
	
	@ApiModelProperty(value = "Input correct price ")
	private String correctPrice;
	
	@ApiModelProperty(value = "Origin Status")
	private String origin;  //ORIGIN

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
	 * @return the supplierNumber
	 */
	public String getSupplierNumber() {
		return supplierNumber;
	}

	/**
	 * @param supplierNumber the supplierNumber to set
	 */
	public void setSupplierNumber(String supplierNumber) {
		this.supplierNumber = supplierNumber;
	}

	/**
	 * @return the partMovementDate
	 */
	public String getPartMovementDate() {
		return partMovementDate;
	}

	/**
	 * @param partMovementDate the partMovementDate to set
	 */
	public void setPartMovementDate(String partMovementDate) {
		this.partMovementDate = partMovementDate;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the deliveryNoteNo
	 */
	public String getDeliveryNoteNo() {
		return deliveryNoteNo;
	}

	/**
	 * @param deliveryNoteNo the deliveryNoteNo to set
	 */
	public void setDeliveryNoteNo(String deliveryNoteNo) {
		this.deliveryNoteNo = deliveryNoteNo;
	}

	/**
	 * @return the deliveryNotePosition
	 */
	public String getDeliveryNotePosition() {
		return deliveryNotePosition;
	}

	/**
	 * @param deliveryNotePosition the deliveryNotePosition to set
	 */
	public void setDeliveryNotePosition(String deliveryNotePosition) {
		this.deliveryNotePosition = deliveryNotePosition;
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
	 * @return the designation
	 */
	public String getDesignation() {
		return designation;
	}

	/**
	 * @param designation the designation to set
	 */
	public void setDesignation(String designation) {
		this.designation = designation;
	}

	/**
	 * @return the quantityAccordingToInvoice
	 */
	public String getQuantityAccordingToInvoice() {
		return quantityAccordingToInvoice;
	}

	/**
	 * @param quantityAccordingToInvoice the quantityAccordingToInvoice to set
	 */
	public void setQuantityAccordingToInvoice(String quantityAccordingToInvoice) {
		this.quantityAccordingToInvoice = quantityAccordingToInvoice;
	}

	/**
	 * @return the vat
	 */
	public String getVat() {
		return vat;
	}

	/**
	 * @param vat the vat to set
	 */
	public void setVat(String vat) {
		this.vat = vat;
	}

	/**
	 * @return the unitPriceAccordingToInvoice
	 */
	public String getUnitPriceAccordingToInvoice() {
		return unitPriceAccordingToInvoice;
	}

	/**
	 * @param unitPriceAccordingToInvoice the unitPriceAccordingToInvoice to set
	 */
	public void setUnitPriceAccordingToInvoice(String unitPriceAccordingToInvoice) {
		this.unitPriceAccordingToInvoice = unitPriceAccordingToInvoice;
	}

	/**
	 * @return the postedAmount
	 */
	public String getPostedAmount() {
		return postedAmount;
	}

	/**
	 * @param postedAmount the postedAmount to set
	 */
	public void setPostedAmount(String postedAmount) {
		this.postedAmount = postedAmount;
	}

	/**
	 * @return the bookedPrice
	 */
	public String getBookedPrice() {
		return bookedPrice;
	}

	/**
	 * @param bookedPrice the bookedPrice to set
	 */
	public void setBookedPrice(String bookedPrice) {
		this.bookedPrice = bookedPrice;
	}

	/**
	 * @return the correctAmount
	 */
	public String getCorrectAmount() {
		return correctAmount;
	}

	/**
	 * @param correctAmount the correctAmount to set
	 */
	public void setCorrectAmount(String correctAmount) {
		this.correctAmount = correctAmount;
	}

	/**
	 * @return the correctPrice
	 */
	public String getCorrectPrice() {
		return correctPrice;
	}

	/**
	 * @param correctPrice the correctPrice to set
	 */
	public void setCorrectPrice(String correctPrice) {
		this.correctPrice = correctPrice;
	}
	
	/**
	 * @return the origin
	 */
	public String getOrigin() {
		return origin;
	}

	/**
	 * @param origin the origin to set
	 */
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	
	
}
