package com.alphax.vo.mb;



import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about DeliveryNotes Dropdown")
public class DeliveryNotes_BA_DTO {

	private String manufacturer;
	
	private String warehouseNumber;
	
	private String supplierNumber;
	
	private String deliveryNoteNo;
	
	private String programName;
	
	private String businessCase;
	
	private String rekow;
	
	private String laufnr;
	
	private String deliveryNoteDate;
	
	private String info;
	
	private String status;
	
	private String deliveryNoteAudit;
	
	private String invoiceAudit;
	
	
	

	/**
	 * @return the manufacturer
	 */
	@ApiModelProperty(value = "Manufacturer (OEM ) ( %% - DB E_BSNLFS - HERST).")
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
	@ApiModelProperty(value = "Warehouse Number ( %% - DB E_BSNLFS - LNR).")
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
	 * @return the supplierNumber
	 */
	@ApiModelProperty(value = "Supplier Number ( %% - DB E_BSNLFS - BENDL).")
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
	 * @return the programName
	 */
	@ApiModelProperty(value = "Program Name ( %% - DB E_BSNLFS - ART).")
	public String getProgramName() {
		return programName;
	}

	/**
	 * @param programName the programName to set
	 */
	public void setProgramName(String programName) {
		this.programName = programName;
	}

	/**
	 * @return the businessCase
	 */
	@ApiModelProperty(value = "Business Case ( %% - DB E_BSNLFS - BA).")
	public String getBusinessCase() {
		return businessCase;
	}

	/**
	 * @param businessCase the businessCase to set
	 */
	public void setBusinessCase(String businessCase) {
		this.businessCase = businessCase;
	}

	/**
	 * @return the rekow
	 */
	@ApiModelProperty(value = "rekow ( %% - DB E_BSNLFS - REKOW).")
	public String getRekow() {
		return rekow;
	}

	/**
	 * @param rekow the rekow to set
	 */
	public void setRekow(String rekow) {
		this.rekow = rekow;
	}

	/**
	 * @return the laufnr
	 */
	@ApiModelProperty(value = "laufnr ( %% - DB E_BSNLFS - LAUFNR).")
	public String getLaufnr() {
		return laufnr;
	}

	/**
	 * @param laufnr the laufnr to set
	 */
	public void setLaufnr(String laufnr) {
		this.laufnr = laufnr;
	}

	@ApiModelProperty(value = "Delivery Note Number (Lieferscheinnummer ) ( %% - DB E_BSNLFS - NRX).")
	public String getDeliveryNoteNo() {
		return deliveryNoteNo;
	}

	public void setDeliveryNoteNo(String deliveryNoteNo) {
		this.deliveryNoteNo = deliveryNoteNo;
	}

	@ApiModelProperty(value = "Delivery Note Date (Datum) ( %% - DB E_BSNLFS - LFSDAT_JMT).")
	public String getDeliveryNoteDate() {
		return deliveryNoteDate;
	}

	public void setDeliveryNoteDate(String deliveryNoteDate) {
		this.deliveryNoteDate = deliveryNoteDate;
	}

	@ApiModelProperty(value = "Delivery Note information")
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	/**
	 * @return the status
	 */
	@ApiModelProperty(value = "Delivery Note Status")
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the deliveryNoteAudit
	 */
	@ApiModelProperty(value = "Delivery Note Audit  ( %% - DB E_BSNLFS - PRUEFUNG).")
	public String getDeliveryNoteAudit() {
		return deliveryNoteAudit;
	}

	/**
	 * @param deliveryNoteAudit the deliveryNoteAudit to set
	 */
	public void setDeliveryNoteAudit(String deliveryNoteAudit) {
		this.deliveryNoteAudit = deliveryNoteAudit;
	}

	/**
	 * @return the invoiceAudit
	 */
	@ApiModelProperty(value = "Invoice Audit  ( %% - DB E_BSNLFS - ERLEDIGT).")
	public String getInvoiceAudit() {
		return invoiceAudit;
	}

	/**
	 * @param invoiceAudit the invoiceAudit to set
	 */
	public void setInvoiceAudit(String invoiceAudit) {
		this.invoiceAudit = invoiceAudit;
	}
	
	
	
	
}
