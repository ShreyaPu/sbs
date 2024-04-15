package com.alphax.vo.mb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "This Object about Delivery Notes")
public class DeliveryNotesDTO {

	
	private String supplierNumber;

	private String deliveryNoteNo;
	
	private String manufacturer;
	
	private String warehouseNumber;

	private String numberofPositions;
	
	private String deliveryNoteDate;

	private String sa;
	
	private String piv;
	
	private String orderNumber;
	
	private String note;
	
	public DeliveryNotesDTO() {

	}


	/**
	 * @return the supplierNumber
	 */
	@ApiModelProperty(value = "Supplier Number (Lieferantennummer) ( %% - DB E_LFSALL - Lieferant).")
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
	 * @return the deliveryNoteNo
	 */
	@ApiModelProperty(value = "Delivery Note Number (Lieferscheinnummer ) ( %% - DB E_LFSALL - LFSNR).")
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
	 * @return the manufacturer
	 */
	@ApiModelProperty(value = "Manufacturer information (Hersteller) ( %% - DB E_LFSALL - HERST).")
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
	 * @return the warehouseNumber
	 */
	@ApiModelProperty(value = "Warehouse Number (Lagernummer) ( %% - DB E_LFSALL - LNR).")
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
	 * @return the numberofPositions
	 */
	@ApiModelProperty(value = "Number of Positions (Anzahll Positionen).")
	public String getNumberofPositions() {
		return numberofPositions;
	}


	/**
	 * @param numberofPositions the numberofPositions to set
	 */
	public void setNumberofPositions(String numberofPositions) {
		this.numberofPositions = numberofPositions;
	}


	/**
	 * @return the deliveryNoteDate
	 */
	@ApiModelProperty(value = "Delivery Note Date (Datum) ( %% - DB E_LFSALL - DATUM_LFS).")
	public String getDeliveryNoteDate() {
		return deliveryNoteDate;
	}


	/**
	 * @param deliveryNoteDate the deliveryNoteDate to set
	 */
	public void setDeliveryNoteDate(String deliveryNoteDate) {
		this.deliveryNoteDate = deliveryNoteDate;
	}


	/**
	 * @return the sa
	 */
	@ApiModelProperty(value = "SA information ( %% - DB E_LFSALL - SA).")
	public String getSa() {
		return sa;
	}


	/**
	 * @param sa the sa to set
	 */
	public void setSa(String sa) {
		this.sa = sa;
	}


	/**
	 * @return the piv
	 */
	public String getPiv() {
		return piv;
	}


	/**
	 * @param piv the piv to set
	 */
	public void setPiv(String piv) {
		this.piv = piv;
	}
	
	/**
	 * @return the orderNumber
	 */
	@ApiModelProperty(value = "Order Number (Auftragsnummer) ( %% - DB E_LFSALL - AUFNR_ALT).")
	public String getOrderNumber() {
		return orderNumber;
	}


	/**
	 * @param orderNumber the orderNumber to set
	 */
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	/**
	 * @return the note
	 */
	@ApiModelProperty(value = "Note (Hinweis) ( %% - DB E_LFSALL - BESTHINW).")
	public String getNote() {
		return note;
	}


	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}



}