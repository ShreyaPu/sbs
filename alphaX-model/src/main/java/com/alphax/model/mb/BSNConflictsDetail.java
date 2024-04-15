package com.alphax.model.mb;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about BSN Conflicts Details")
public class BSNConflictsDetail {
	
	@DBTable(columnName ="HERST", required = true)
	private String manufacturer;
	
	@DBTable(columnName ="LNR", required = true)
	private String warehouseNo;
	
	@DBTable(columnName ="BEST_BENDL", required = true)
	private String supplierNo;
	
	@DBTable(columnName ="LFSNR", required = true)
	private String deliveryNoteNo;	

	@DBTable(columnName ="Bestellnummer", required = true)
	private String orderNumber;
	
	@DBTable(columnName ="DISPOPOS", required = true)
	private String dispopos;
	
	@DBTable(columnName ="DISPOUP", required = true)
	private String dispoup;
	
	@DBTable(columnName ="ERROR_CODE", required = true)
	private String conflict_Code;
	
	@DBTable(columnName ="ETNR", required = true)
	private String partNumber;
	
	@DBTable(columnName ="PARTNAME", required = true)
	private String partName;
	
	@DBTable(columnName ="HINT", required = true)
	private String hint;
	
	@DBTable(columnName ="OLDVALUE", required = true)
	private String oldValue;
	
	@DBTable(columnName ="NEWVALUE", required = true)
	private String newVlaue;
	
	@DBTable(columnName ="TIMESTAMP", required = true)
	private String conflictSolvedDate;
	
	@DBTable(columnName ="SOLVED", required = true)
	private String solved_Status;
		
	@DBTable(columnName ="ROWNUMER", required = true)
	private Integer totalCount;
			
	
	public BSNConflictsDetail() {

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
	 * @return the warehouseNo
	 */
	public String getWarehouseNo() {
		return warehouseNo;
	}


	/**
	 * @param warehouseNo the warehouseNo to set
	 */
	public void setWarehouseNo(String warehouseNo) {
		this.warehouseNo = warehouseNo;
	}


	/**
	 * @return the supplierNo
	 */
	public String getSupplierNo() {
		return supplierNo;
	}


	/**
	 * @param supplierNo the supplierNo to set
	 */
	public void setSupplierNo(String supplierNo) {
		this.supplierNo = supplierNo;
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
	 * @return the dispopos
	 */
	public String getDispopos() {
		return dispopos;
	}


	/**
	 * @param dispopos the dispopos to set
	 */
	public void setDispopos(String dispopos) {
		this.dispopos = dispopos;
	}


	/**
	 * @return the dispoup
	 */
	public String getDispoup() {
		return dispoup;
	}


	/**
	 * @param dispoup the dispoup to set
	 */
	public void setDispoup(String dispoup) {
		this.dispoup = dispoup;
	}


	/**
	 * @return the conflict_Code
	 */
	public String getConflict_Code() {
		return conflict_Code;
	}


	/**
	 * @param conflict_Code the conflict_Code to set
	 */
	public void setConflict_Code(String conflict_Code) {
		this.conflict_Code = conflict_Code;
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
	 * @return the partName
	 */
	public String getPartName() {
		return partName;
	}


	/**
	 * @param partName the partName to set
	 */
	public void setPartName(String partName) {
		this.partName = partName;
	}


	/**
	 * @return the hint
	 */
	public String getHint() {
		return hint;
	}


	/**
	 * @param hint the hint to set
	 */
	public void setHint(String hint) {
		this.hint = hint;
	}


	/**
	 * @return the oldValue
	 */
	public String getOldValue() {
		return oldValue;
	}


	/**
	 * @param oldValue the oldValue to set
	 */
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}


	/**
	 * @return the newVlaue
	 */
	public String getNewVlaue() {
		return newVlaue;
	}


	/**
	 * @param newVlaue the newVlaue to set
	 */
	public void setNewVlaue(String newVlaue) {
		this.newVlaue = newVlaue;
	}


	/**
	 * @return the conflictSolvedDate
	 */
	public String getConflictSolvedDate() {
		return conflictSolvedDate;
	}


	/**
	 * @param conflictSolvedDate the conflictSolvedDate to set
	 */
	public void setConflictSolvedDate(String conflictSolvedDate) {
		this.conflictSolvedDate = conflictSolvedDate;
	}


	/**
	 * @return the solved_Status
	 */
	public String getSolved_Status() {
		return solved_Status;
	}


	/**
	 * @param solved_Status the solved_Status to set
	 */
	public void setSolved_Status(String solved_Status) {
		this.solved_Status = solved_Status;
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

		
}