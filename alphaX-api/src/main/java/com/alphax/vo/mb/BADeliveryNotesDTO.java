package com.alphax.vo.mb;


import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about BADeliveryNotesDTO - E_LFSL")
public class BADeliveryNotesDTO {
	
	private String orderType;

	private String orderNumber;

	private String deliveredQuantity;

	private String oldPrice;
	
	private String netPurchasePrice;
	
	private String receiptDateTime;
	

	public BADeliveryNotesDTO() {
		
	}


	public String getOrderType() {
		return orderType;
	}


	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}


	public String getOrderNumber() {
		return orderNumber;
	}


	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}


	public String getDeliveredQuantity() {
		return deliveredQuantity;
	}


	public void setDeliveredQuantity(String deliveredQuantity) {
		this.deliveredQuantity = deliveredQuantity;
	}


	public String getOldPrice() {
		return oldPrice;
	}


	public void setOldPrice(String oldPrice) {
		this.oldPrice = oldPrice;
	}


	public String getNetPurchasePrice() {
		return netPurchasePrice;
	}


	public void setNetPurchasePrice(String netPurchasePrice) {
		this.netPurchasePrice = netPurchasePrice;
	}


	public String getReceiptDateTime() {
		return receiptDateTime;
	}


	public void setReceiptDateTime(String receiptDateTime) {
		this.receiptDateTime = receiptDateTime;
	}


}