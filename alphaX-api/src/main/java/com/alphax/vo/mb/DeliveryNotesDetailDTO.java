package com.alphax.vo.mb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about DeliveryNotes Details")
public class DeliveryNotesDetailDTO {

	
	private String orderNumber;
	
	private String orderPosition;
	
	private String orderedPartNo;
	
	private String orderedPartDescription;
	
	private String orderedQuantity;
	
	private String deliveryNotePosition;
	
	private String suppliedPartNo;
	
	private String suppliedPartDescription;
	
	private String deliveredQuantity;
	
	private String replacementCode;
	
	private String note;
	
	
	public DeliveryNotesDetailDTO() {

	}


	/**
	 * @return the orderNumber
	 */
	@ApiModelProperty(value = "Order Number (Auftragsnummer) ( %% - DB E_LFSALL - AUFNR_ALT).")
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
	@ApiModelProperty(value = "Order Position (Auftragsposition) ( %% - DB E_LFSALL - POS_BEST).")
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
	@ApiModelProperty(value = "Order Parts Number (Bestellte Teilenummer) ( %% - DB E_LFSALL - TNR_BEST).")
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
	@ApiModelProperty(value = "Order Part Description (Bezeichnung bestellte Teilenummer) ( %% - DB E_ETSTAMM - benen).")
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
	@ApiModelProperty(value = "Order Quantity (Bestellte Menge) ( %% - DB E_LFSALL - MENGE_BEST).")
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
	@ApiModelProperty(value = "Delivery Note Position (Lieferscheinposition) ( %% - DB E_LFSALL - POS_LFS).")
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
	@ApiModelProperty(value = "Supplied Part Number (Geliefertes  Teilenummer) ( %% - DB E_LFSALL - TNR_GEL).")
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
	@ApiModelProperty(value = "Supplied Part Description (Bezeichnung Geliefertes  Teilenummer) ( %% - DB E_ETSTAMM - benen).")
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
	@ApiModelProperty(value = "Delivered Quantity (Gelieferte  Menge ) ( %% - DB E_LFSALL - MENGE_GEL).")
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
	@ApiModelProperty(value = "Replacement Code (Ersetzungscode) ( %% - DB E_LFSALL And  ETHTXTTAB - CODE_ERS + TEXT).")
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
	 * @return the note
	 */
	@ApiModelProperty(value = "Note (Hinweis) ( %% - DB E_LFSALL - BESTHINW).")
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