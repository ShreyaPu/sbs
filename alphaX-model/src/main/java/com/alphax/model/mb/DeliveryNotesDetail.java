package com.alphax.model.mb;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about DeliveryNotes")
public class DeliveryNotesDetail {

	@DBTable(columnName ="AUFNR_ALT", required = true)
	private String orderNumber;
	
	@DBTable(columnName ="POS_BEST", required = true)
	private String orderPosition;
	
	@DBTable(columnName ="TNR_BEST", required = true)
	private String orderedPartNo;
	
	@DBTable(columnName ="BESTETEIL", required = true)
	private String orderedPartDescription;
	
	@DBTable(columnName ="MENGE_BEST", required = true)
	private String orderedQuantity;
	
	@DBTable(columnName ="POS_LFS", required = true)
	private String deliveryNotePosition;
	
	@DBTable(columnName ="TNR_GEL", required = true)
	private String suppliedPartNo;
	
	@DBTable(columnName ="GELTEIL", required = true)
	private String suppliedPartDescription;
	
	@DBTable(columnName ="MENGE_GEL", required = true)
	private String deliveredQuantity;
	
	@DBTable(columnName ="BESTHINW", required = true)
	private String note;
	
	@DBTable(columnName ="CODE_ERS", required = true)
	private String replacementCode;
	
	@DBTable(columnName ="BESTECODE", required = true)
	private String replacementCodeDesc;
	
	@DBTable(columnName ="LFSNR", required = true)
	private String deliveryNoteNo;
	
	@DBTable(columnName ="SA", required = true)
	private String sa;
	
	@DBTable(columnName ="BEN", required = true)
	private String partName;
	
	@DBTable(columnName ="ETNR", required = true)
	private String partNumber;
	
	@DBTable(columnName ="ID", required = true)
	private String purchaseOrderId;
	
	@DBTable(columnName ="Lieferant", required = true)
	private String supplierNumber;
	
	@DBTable(columnName ="HERST", required = true)
	private String manufacturer;
	
	@DBTable(columnName ="LNR", required = true)
	private String warehouseNumber;
	
	@DBTable(columnName ="LFSMARKE", required = true)
	private String lfsMarketingCode;
	
	
	public DeliveryNotesDetail() {

	}


	/**
	 * @return the orderNumber
	 */
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
	 * @return the orderPosition
	 */
	public String getOrderPosition() {
		return orderPosition;
	}


	/**
	 * @param orderPosition the orderPosition to set
	 */
	public void setOrderPosition(String orderPosition) {
		this.orderPosition = orderPosition;
	}


	/**
	 * @return the orderedPartNo
	 */
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
	 * @return the orderedPartDescription
	 */
	public String getOrderedPartDescription() {
		return orderedPartDescription;
	}


	/**
	 * @param orderedPartDescription the orderedPartDescription to set
	 */
	public void setOrderedPartDescription(String orderedPartDescription) {
		this.orderedPartDescription = orderedPartDescription;
	}


	/**
	 * @return the orderedQuantity
	 */
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
	 * @return the suppliedPartNo
	 */
	public String getSuppliedPartNo() {
		return suppliedPartNo;
	}


	/**
	 * @param suppliedPartNo the suppliedPartNo to set
	 */
	public void setSuppliedPartNo(String suppliedPartNo) {
		this.suppliedPartNo = suppliedPartNo;
	}


	/**
	 * @return the suppliedPartDescription
	 */
	public String getSuppliedPartDescription() {
		return suppliedPartDescription;
	}


	/**
	 * @param suppliedPartDescription the suppliedPartDescription to set
	 */
	public void setSuppliedPartDescription(String suppliedPartDescription) {
		this.suppliedPartDescription = suppliedPartDescription;
	}


	/**
	 * @return the deliveredQuantity
	 */
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
	 * @return the replacementCode
	 */
	public String getReplacementCode() {
		return replacementCode;
	}


	/**
	 * @param replacementCode the replacementCode to set
	 */
	public void setReplacementCode(String replacementCode) {
		this.replacementCode = replacementCode;
	}


	/**
	 * @return the replacementCodeDesc
	 */
	public String getReplacementCodeDesc() {
		return replacementCodeDesc;
	}


	/**
	 * @param replacementCodeDesc the replacementCodeDesc to set
	 */
	public void setReplacementCodeDesc(String replacementCodeDesc) {
		this.replacementCodeDesc = replacementCodeDesc;
	}


	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}


	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}


	public String getDeliveryNoteNo() {
		return deliveryNoteNo;
	}


	public void setDeliveryNoteNo(String deliveryNoteNo) {
		this.deliveryNoteNo = deliveryNoteNo;
	}


	public String getSa() {
		return sa;
	}


	public void setSa(String sa) {
		this.sa = sa;
	}


	public String getPartName() {
		return partName;
	}


	public void setPartName(String partName) {
		this.partName = partName;
	}


	public String getPartNumber() {
		return partNumber;
	}


	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}


	public String getPurchaseOrderId() {
		return purchaseOrderId;
	}


	public void setPurchaseOrderId(String purchaseOrderId) {
		this.purchaseOrderId = purchaseOrderId;
	}


	public String getSupplierNumber() {
		return supplierNumber;
	}


	public void setSupplierNumber(String supplierNumber) {
		this.supplierNumber = supplierNumber;
	}


	public String getManufacturer() {
		return manufacturer;
	}


	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}


	public String getWarehouseNumber() {
		return warehouseNumber;
	}


	public void setWarehouseNumber(String warehouseNumber) {
		this.warehouseNumber = warehouseNumber;
	}


	public String getLfsMarketingCode() {
		return lfsMarketingCode;
	}


	public void setLfsMarketingCode(String lfsMarketingCode) {
		this.lfsMarketingCode = lfsMarketingCode;
	}
	
	

}