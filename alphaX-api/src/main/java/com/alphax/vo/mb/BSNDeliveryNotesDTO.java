package com.alphax.vo.mb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about BSN Delivery Notes DTO")
public class BSNDeliveryNotesDTO {

	
	private String supplierNumber;
	
	private String deliveryNoteNo;
	
	private String deliveryNoteDate;
	
	private String manufacturer;
	
	private String warehouseNumber;
	
	private String piv;
	
	private String numberofPositions;
	

	public BSNDeliveryNotesDTO() {

	}


	/**
	 * @return the supplierNumber
	 */
	@ApiModelProperty(value = "supplier Number (Lieferantennummer) ( %% - DB E_BSNDAT - BEST_BENDL).")
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
	@ApiModelProperty(value = "Delivery Note Number (Lieferscheinnummer) ( %% - DB E_BSNDAT - LIEFNR_GESAMT).")
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
	 * @return the deliveryNoteDate
	 */
	@ApiModelProperty(value = "Delivery Note Date (Datum) ( %% - DB E_BSNDAT - BDAT).")
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
	 * @return the manufacturer
	 */
	@ApiModelProperty(value = "Manufacturer information (Hersteller) ( %% - DB E_BSNDAT - HERST).")
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
	@ApiModelProperty(value = "Warehouse Number (Lagernummer) ( %% - DB E_BSNDAT - LNR).")
	public String getWarehouseNumber() {
		return warehouseNumber;
	}


	/**
	 * @param warehouseNumber the warehouseNumber to set
	 */
	public void setWarehouseNumber(String warehouseNumber) {
		this.warehouseNumber = warehouseNumber;
	}


	@ApiModelProperty(value = "PIV Number ( %% - DB E_PIV - PIV).")
	public String getPiv() {
		return piv;
	}


	public void setPiv(String piv) {
		this.piv = piv;
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

}