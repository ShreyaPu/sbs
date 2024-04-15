package com.alphax.model.mb;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about BSN Delivery Notes Details")
public class BSNDeliveryNotesDetail {

	@DBTable(columnName ="PSERV_AUFNR", required = true)
	private String pservAufnr;
	
	@DBTable(columnName ="SERV_AUFNRX", required = true)
	private String aservAufnr;
	
	@DBTable(columnName ="BESTPOS", required = true)
	private String deliveryNotePosition;
	
	@DBTable(columnName ="BEST_ART", required = true)
	private String orderNumber1;
	
	@DBTable(columnName ="BEST_AP", required = true)
	private String orderNumber2;
	
	@DBTable(columnName ="ETNR", required = true)
	private String orderedPartNo;
	
	@DBTable(columnName ="BEN", required = true)
	private String partDescription;
	
	@DBTable(columnName ="BEMENG", required = true)
	private String orderedQuantity;
	
	@DBTable(columnName ="BestellterWert", required = true)
	private String orderedValue;
	
	@DBTable(columnName ="ZUMENG", required = true)
	private String deliveredQuantity;
	
	@DBTable(columnName ="GelieferterWert", required = true)
	private String deliveredValue;
	
	@DBTable(columnName ="GLMENG", required = true)
	private String quantityCount;
	
	@DBTable(columnName ="BVER", required = true)
	private String orderNote;
	
	@DBTable(columnName ="conflict", required = true)
	private Integer conflict_Count;
	
	@DBTable(columnName ="conflict_ERR", required = true)
	private String conflict_Reasons;
	
	@DBTable(columnName ="DISPOPOS", required = true)
	private String dispopos;
	
	@DBTable(columnName ="DISPOUP", required = true)
	private String dispoup;
	
	@DBTable(columnName ="ERROR_CODE", required = true)
	private String conflict_Code;
	
	@DBTable(columnName ="DELIVEREDPARTQUANTITY", required = true)
	private String deliveredPartQuantity;
	
	@DBTable(columnName ="DELIVEREDPARTNO", required = true)
	private String deliveredPartNo;
	
	@DBTable(columnName ="deliveredDispoup", required = true)
	private String deliveredDispoup;
	
	@DBTable(columnName ="deliveredPartName", required = true)
	private String deliveredPartName;
	
	@DBTable(columnName ="deliveredPartOrderNote", required = true)
	private String deliveredPartOrderNote;
	
	@DBTable(columnName ="deliveredGelieferterWert", required = true)
	private String deliveredGelieferterWert;
	
	
	public BSNDeliveryNotesDetail() {

	}


	/**
	 * @return the pservAufnr
	 */
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
	 * @return the orderNumber1
	 */
	public String getOrderNumber1() {
		return orderNumber1;
	}


	/**
	 * @param orderNumber1 the orderNumber1 to set
	 */
	public void setOrderNumber1(String orderNumber1) {
		this.orderNumber1 = orderNumber1;
	}


	/**
	 * @return the orderNumber2
	 */
	public String getOrderNumber2() {
		return orderNumber2;
	}


	/**
	 * @param orderNumber2 the orderNumber2 to set
	 */
	public void setOrderNumber2(String orderNumber2) {
		this.orderNumber2 = orderNumber2;
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
	 * @return the partDescription
	 */
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
	 * @return the conflict_Count
	 */
	public Integer getConflict_Count() {
		return conflict_Count;
	}


	/**
	 * @param conflict_Count the conflict_Count to set
	 */
	public void setConflict_Count(Integer conflict_Count) {
		this.conflict_Count = conflict_Count;
	}


	/**
	 * @return the conflict_Reasons
	 */
	public String getConflict_Reasons() {
		return conflict_Reasons;
	}


	/**
	 * @param conflict_Reasons the conflict_Reasons to set
	 */
	public void setConflict_Reasons(String conflict_Reasons) {
		this.conflict_Reasons = conflict_Reasons;
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


	public String getConflict_Code() {
		return conflict_Code;
	}


	public void setConflict_Code(String conflict_Code) {
		this.conflict_Code = conflict_Code;
	}


	public String getDeliveredPartQuantity() {
		return deliveredPartQuantity;
	}


	public void setDeliveredPartQuantity(String deliveredPartQuantity) {
		this.deliveredPartQuantity = deliveredPartQuantity;
	}


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


	public String getDeliveredPartName() {
		return deliveredPartName;
	}


	public void setDeliveredPartName(String deliveredPartName) {
		this.deliveredPartName = deliveredPartName;
	}


	public String getDeliveredPartOrderNote() {
		return deliveredPartOrderNote;
	}


	public void setDeliveredPartOrderNote(String deliveredPartOrderNote) {
		this.deliveredPartOrderNote = deliveredPartOrderNote;
	}


	public String getDeliveredGelieferterWert() {
		return deliveredGelieferterWert;
	}


	public void setDeliveredGelieferterWert(String deliveredGelieferterWert) {
		this.deliveredGelieferterWert = deliveredGelieferterWert;
	}
	
	


	

	
	
}