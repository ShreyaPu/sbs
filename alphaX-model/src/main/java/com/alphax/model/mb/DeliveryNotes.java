package com.alphax.model.mb;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about DeliveryNotes")
public class DeliveryNotes {


	@DBTable(columnName ="Lieferant", required = true)
	private String supplierNumber;

	@DBTable(columnName ="LFSNR", required = true)
	private String deliveryNoteNo;

	@DBTable(columnName ="HERST", required = true)
	private String manufacturer;
	
	@DBTable(columnName ="LNR", required = true)
	private String warehouseNumber;

	@DBTable(columnName ="anzahll", required = true)
	private Integer numberofPositions;

	@DBTable(columnName ="DATUM_LFS", required = true)
	private String deliveryNoteDate;

	@DBTable(columnName ="SA", required = true)
	private String sa;
	
	@DBTable(columnName ="PIV", required = true)
	private String piv;

	@DBTable(columnName ="ROWNUMER", required = true)
	private Integer totalCount;
	
	@DBTable(columnName ="AUFNR_ALT", required = true)
	private String orderNumber;
	
	@DBTable(columnName ="BESTHINW", required = true)
	private String note;
	


	public DeliveryNotes() {

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
	public Integer getNumberofPositions() {
		return numberofPositions;
	}


	/**
	 * @param numberofPositions the numberofPositions to set
	 */
	public void setNumberofPositions(Integer numberofPositions) {
		this.numberofPositions = numberofPositions;
	}


	/**
	 * @return the deliveryNoteDate
	 */
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
	 * @return the totalCount
	 */
	public Integer getTotalCount() {
		return totalCount;
	}


	/**
	 * @param totalCount the totalCount to set
	 */
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
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
	
	

}