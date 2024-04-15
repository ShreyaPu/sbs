package com.alphax.model.mb;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about BSN Delivery Notes")
public class BSNDeliveryNotes {


	@DBTable(columnName ="BEST_BENDL", required = true)
	private String supplierNumber;

	@DBTable(columnName ="LIEFNR_GESAMT", required = true)
	private String deliveryNoteNo;

	@DBTable(columnName ="BDAT", required = true)
	private String deliveryNoteDate;
	
	@DBTable(columnName ="HERST", required = true)
	private String manufacturer;
	
	@DBTable(columnName ="LNR", required = true)
	private String warehouseNumber;
	
	@DBTable(columnName ="PIV", required = true)
	private String piv;

	@DBTable(columnName ="ROWNUMER", required = true)
	private Integer totalCount;
	
	@DBTable(columnName ="anzahll", required = true)
	private Integer numberofPositions;


	public BSNDeliveryNotes() {

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


	public Integer getNumberofPositions() {
		return numberofPositions;
	}


	public void setNumberofPositions(Integer numberofPositions) {
		this.numberofPositions = numberofPositions;
	}
	

}