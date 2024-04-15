package com.alphax.vo.mb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about BSN Delivery Notes Details DTO")
public class BSNDeliveryNotesDetailDTO {

	private String pservAufnr;
	
	private String aservAufnr;
	
	private String deliveryNotePosition;
	
	private String orderNumber;
	
	private String orderedPartNo;
	
	private String partDescription;
	
	private String orderedQuantity;
	
	private String orderedValue;
	
	private String deliveredQuantity;
	
	private String deliveredValue;
	
	private String quantityCount;
	
	private String orderNote;
	
	private String supplierNumber;
	
	private String deliveryNoteNo;
	
	private String manufacturer;
	
	private String warehouseNumber;
	
	private Boolean isConflict;
	
	private String conflict_Reasons;
	
	private String conflict_Code;
	
	private String dispoup;
	
	private String dispopos;
	
	private String deliveredPartNo;
	
	private String deliveredDispoup;
	
	
	public BSNDeliveryNotesDetailDTO() {

	}


	/**
	 * @return the pservAufnr
	 */
	@ApiModelProperty(value = "pserv Aufnr (ICON) ( %% - DB E_BSNDAT - PSERV_AUFNR).")
	public String getPservAufnr() {
		return pservAufnr;
	}


	/**
	 * @param pservAufnr the pservAufnr to set
	 */
	public void setPservAufnr(String pservAufnr) {
		this.pservAufnr = pservAufnr;
	}


	/**
	 * @return the deliveryNotePosition
	 */
	@ApiModelProperty(value = "Delivery Note Position (Lieferscheinposition) ( %% - DB E_BSNDAT - BEST_POS).")
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
	 * @return the orderNumber
	 */
	@ApiModelProperty(value = "order Number (Auftragsnummer) ( %% - DB E_BSNDAT - BEST_ART + BEST_AP).")
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
	 * @return the orderedPartNo
	 */
	@ApiModelProperty(value = "ordered Part Number (Bestellte Teilenummer) ( %% - DB E_BSNDAT - ETNR).")
	public String getOrderedPartNo() {
		return orderedPartNo;
	}


	/**
	 * @param orderedPartNo the orderedPartNo to set
	 */
	public void setOrderedPartNo(String orderedPartNo) {
		this.orderedPartNo = orderedPartNo;
	}


	/**
	 * @return the partDescription
	 */
	@ApiModelProperty(value = "part Description (Teilebezeichnung) ( %% - DB E_BSNDAT - BEN).")
	public String getPartDescription() {
		return partDescription;
	}


	/**
	 * @param partDescription the partDescription to set
	 */
	public void setPartDescription(String partDescription) {
		this.partDescription = partDescription;
	}


	/**
	 * @return the orderedQuantity
	 */
	@ApiModelProperty(value = "Ordered Quantity (Bestellte Menge) ( %% - DB E_BSNDAT - BEMENG).")
	public String getOrderedQuantity() {
		return orderedQuantity;
	}


	/**
	 * @param orderedQuantity the orderedQuantity to set
	 */
	public void setOrderedQuantity(String orderedQuantity) {
		this.orderedQuantity = orderedQuantity;
	}


	/**
	 * @return the orderedValue
	 */
	@ApiModelProperty(value = "Ordered Value (Bestellter Wert) ( %% - DB E_BSNDAT -  (BEMENG*EKNPR) ).")
	public String getOrderedValue() {
		return orderedValue;
	}


	/**
	 * @param orderedValue the orderedValue to set
	 */
	public void setOrderedValue(String orderedValue) {
		this.orderedValue = orderedValue;
	}


	/**
	 * @return the deliveredQuantity
	 */
	@ApiModelProperty(value = "Delivered Quantity (Gelieferte  Menge) ( %% - DB E_BSNDAT - ZUMENG).")
	public String getDeliveredQuantity() {
		return deliveredQuantity;
	}


	/**
	 * @param deliveredQuantity the deliveredQuantity to set
	 */
	public void setDeliveredQuantity(String deliveredQuantity) {
		this.deliveredQuantity = deliveredQuantity;
	}


	/**
	 * @return the deliveredValue
	 */
	@ApiModelProperty(value = "Delivered Value (Gelieferter  Wert) ( %% - DB E_BSNDAT - (ZUMENG*EKNPR) ).")
	public String getDeliveredValue() {
		return deliveredValue;
	}


	/**
	 * @param deliveredValue the deliveredValue to set
	 */
	public void setDeliveredValue(String deliveredValue) {
		this.deliveredValue = deliveredValue;
	}


	/**
	 * @return the quantityCount
	 */
	@ApiModelProperty(value = "Delivered Value (Gezählte Menge) ( %% - DB E_BSNDAT - ??).")
	public String getQuantityCount() {
		return quantityCount;
	}


	/**
	 * @param quantityCount the quantityCount to set
	 */
	public void setQuantityCount(String quantityCount) {
		this.quantityCount = quantityCount;
	}


	/**
	 * @return the aservAufnr
	 */
	public String getAservAufnr() {
		return aservAufnr;
	}


	/**
	 * @param aservAufnr the aservAufnr to set
	 */
	public void setAservAufnr(String aservAufnr) {
		this.aservAufnr = aservAufnr;
	}


	/**
	 * @return the orderNote
	 */
	@ApiModelProperty(value = "Order Note (Bestellhinweis) ( %% - DB E_BSNDAT - BVER).")
	public String getOrderNote() {
		return orderNote;
	}


	/**
	 * @param orderNote the orderNote to set
	 */
	public void setOrderNote(String orderNote) {
		this.orderNote = orderNote;
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


	/**
	 * @return the isConflict
	 */
	public Boolean getIsConflict() {
		return isConflict;
	}


	/**
	 * @param isConflict the isConflict to set
	 */
	public void setIsConflict(Boolean isConflict) {
		this.isConflict = isConflict;
	}


	/**
	 * @return the conflict_Reasons
	 */
	@ApiModelProperty(value = "Conflict reasons ( %% - DB O_BSNCONF - HINT).")
	public String getConflict_Reasons() {
		return conflict_Reasons;
	}


	/**
	 * @param conflict_Reasons the conflict_Reasons to set
	 */
	public void setConflict_Reasons(String conflict_Reasons) {
		this.conflict_Reasons = conflict_Reasons;
	}


	@ApiModelProperty(value = "Conflict error code ( %% - DB O_BSNCONF - ERROR_CODE).")
	public String getConflict_Code() {
		return conflict_Code;
	}


	public void setConflict_Code(String conflict_Code) {
		this.conflict_Code = conflict_Code;
	}

	@ApiModelProperty(value = "Dispoup ( %% - DB E_BSNDAT - DISPOUP).")
	public String getDispoup() {
		return dispoup;
	}


	public void setDispoup(String dispoup) {
		this.dispoup = dispoup;
	}

	@ApiModelProperty(value = "Dispopos ( %% - DB E_BSNDAT - DISPOPOS).")
	public String getDispopos() {
		return dispopos;
	}


	public void setDispopos(String dispopos) {
		this.dispopos = dispopos;
	}

	@ApiModelProperty(value = "ordered Part Number (Bestellte Teilenummer) ( %% - DB E_BSNDAT - ETNR).")
	public String getDeliveredPartNo() {
		return deliveredPartNo;
	}


	public void setDeliveredPartNo(String deliveredPartNo) {
		this.deliveredPartNo = deliveredPartNo;
	}


	public String getDeliveredDispoup() {
		return deliveredDispoup;
	}


	public void setDeliveredDispoup(String deliveredDispoup) {
		this.deliveredDispoup = deliveredDispoup;
	}

	
	
	
}