package com.alphax.vo.mb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about Solved conflict for  Delivery Notes  DTO")
public class ConflictResolutionDTO {

	
	private String orderNumber;
	
	private String orderedPartNo;
	
	private String partDescription;
	
	private String supplierNumber;
	
	private String deliveryNoteNo;
	
	private String manufacturer;
	
	private String warehouseNumber;
	
	private String conflict_Reasons;
	
	private String conflict_Code;
	
	private String dispoup;
	
	private String dispopos;
	
	private String valueFrom_PartMaster;
	
	private String valueFrom_DeliveryNote;
	
	private String selectedType;
	
	private String selectedValue;
	
	
	
	public ConflictResolutionDTO() {

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

	public String getValueFrom_PartMaster() {
		return valueFrom_PartMaster;
	}

	public void setValueFrom_PartMaster(String valueFrom_PartMaster) {
		this.valueFrom_PartMaster = valueFrom_PartMaster;
	}

	public String getValueFrom_DeliveryNote() {
		return valueFrom_DeliveryNote;
	}

	public void setValueFrom_DeliveryNote(String valueFrom_DeliveryNote) {
		this.valueFrom_DeliveryNote = valueFrom_DeliveryNote;
	}

	public String getSelectedType() {
		return selectedType;
	}

	public void setSelectedType(String selectedType) {
		this.selectedType = selectedType;
	}

	public String getSelectedValue() {
		return selectedValue;
	}

	public void setSelectedValue(String selectedValue) {
		this.selectedValue = selectedValue;
	}
	
	
	
}
