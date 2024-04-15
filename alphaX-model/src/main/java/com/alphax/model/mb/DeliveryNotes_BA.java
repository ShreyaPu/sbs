package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about DeliveryNotes Dropdown")
public class DeliveryNotes_BA {

	@DBTable(columnName ="HERST", required = true)
	private String manufacturer;
	
	@DBTable(columnName ="LNR", required = true)
	private BigDecimal warehouseNumber;
	
	@DBTable(columnName ="BENDL", required = true)
	private BigDecimal supplierNumber;
	
	@DBTable(columnName ="NRX", required = true)
	private String deliveryNoteNo;
	
	@DBTable(columnName ="ART", required = true)
	private String programName;
	
	@DBTable(columnName ="BA", required = true)
	private String businessCase;
	
	@DBTable(columnName ="REKOW", required = true)
	private String rekow;
	
	@DBTable(columnName ="LAUFNR", required = true)
	private String laufnr;
	
	@DBTable(columnName ="LFSDAT_JMT", required = true)
	private String deliveryNoteDate;
	
	@DBTable(columnName ="ROWNUMER", required = true)
	private Integer totalCount;
	
	@DBTable(columnName ="PRUEFUNG", required = true)
	private String deliveryNoteAudit;
	
	@DBTable(columnName ="ERLEDIGT", required = true)
	private String invoiceAudit;
	
	@DBTable(columnName ="BPOS", required = true)
	private BigDecimal bpos;
	
	@DBTable(columnName ="BWERT", required = true)
	private String bwert_sepsign;
	
	@DBTable(columnName ="NWERT", required = true)
	private String nwert_sepsign;
	
	
	

	public String getDeliveryNoteNo() {
		return deliveryNoteNo;
	}

	public void setDeliveryNoteNo(String deliveryNoteNo) {
		this.deliveryNoteNo = deliveryNoteNo;
	}

	public String getDeliveryNoteDate() {
		return deliveryNoteDate;
	}

	public void setDeliveryNoteDate(String deliveryNoteDate) {
		this.deliveryNoteDate = deliveryNoteDate;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * @return the deliveryNoteAudit
	 */
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
	public String getInvoiceAudit() {
		return invoiceAudit;
	}

	/**
	 * @param invoiceAudit the invoiceAudit to set
	 */
	public void setInvoiceAudit(String invoiceAudit) {
		this.invoiceAudit = invoiceAudit;
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
	 * @return the warehouseNumber
	 */
	public BigDecimal getWarehouseNumber() {
		return warehouseNumber;
	}

	/**
	 * @param warehouseNumber the warehouseNumber to set
	 */
	public void setWarehouseNumber(BigDecimal warehouseNumber) {
		this.warehouseNumber = warehouseNumber;
	}

	/**
	 * @return the supplierNumber
	 */
	public BigDecimal getSupplierNumber() {
		return supplierNumber;
	}

	/**
	 * @param supplierNumber the supplierNumber to set
	 */
	public void setSupplierNumber(BigDecimal supplierNumber) {
		this.supplierNumber = supplierNumber;
	}

	/**
	 * @return the programName
	 */
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
	public String getLaufnr() {
		return laufnr;
	}

	/**
	 * @param laufnr the laufnr to set
	 */
	public void setLaufnr(String laufnr) {
		this.laufnr = laufnr;
	}

	public BigDecimal getBpos() {
		return bpos;
	}

	public void setBpos(BigDecimal bpos) {
		this.bpos = bpos;
	}

	public String getBwert_sepsign() {
		return bwert_sepsign;
	}

	public void setBwert_sepsign(String bwert_sepsign) {
		this.bwert_sepsign = bwert_sepsign;
	}

	public String getNwert_sepsign() {
		return nwert_sepsign;
	}

	public void setNwert_sepsign(String nwert_sepsign) {
		this.nwert_sepsign = nwert_sepsign;
	}
	
	
	
	
	
}
