package com.alphax.vo.mb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about DeliveryNotes history Details")
public class OrderHistoryDetailsDTO {

	
	private String orderPosition;
	
	private String orderedQuantity;
	
	private String deliveredQuantity;
	
	private String note;
	
	private String deliveryNoteNo;
	
	private String sa;
	
	private String partName;
	
	private String partNumber;
	
	
	public OrderHistoryDetailsDTO() {

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



	@ApiModelProperty(value = "Delivery Note Number (Lieferscheinnummer ) ( %% - DB E_LFSALL - LFSNR).")
	public String getDeliveryNoteNo() {
		return deliveryNoteNo;
	}




	public void setDeliveryNoteNo(String deliveryNoteNo) {
		this.deliveryNoteNo = deliveryNoteNo;
	}



	@ApiModelProperty(value = "SA information ( %% - DB E_LFSALL - SA).")
	public String getSa() {
		return sa;
	}




	public void setSa(String sa) {
		this.sa = sa;
	}




	@ApiModelProperty(value = "Part Name ( %% - DB E_LFSALL - BEN).")
	public String getPartName() {
		return partName;
	}




	public void setPartName(String partName) {
		this.partName = partName;
	}



	@ApiModelProperty(value = "Part Number ( %% - DB E_BSNDAT - ETNR).")
	public String getPartNumber() {
		return partNumber;
	}




	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}
	
	
	
}
