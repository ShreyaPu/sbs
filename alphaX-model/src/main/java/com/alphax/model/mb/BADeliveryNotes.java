package com.alphax.model.mb;


import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about BADeliveryNotes - E_LFSL")
public class BADeliveryNotes {
	
	@DBTable(columnName = "AA", required = true)
	private BigDecimal orderType;
	
	@DBTable(columnName = "AUFTRAG", required = true)
	private BigDecimal orderNumber;
	
	@DBTable(columnName = "ZUMENG", required = true)
	private BigDecimal deliveredQuantity;
	
	@DBTable(columnName = "EPREIS", required = true)
	private BigDecimal oldPrice;
	
	@DBTable(columnName = "NEPREIS", required = true)
	private BigDecimal netPurchasePrice;
	
	@DBTable(columnName = "RECDAT", required = true)
	private BigDecimal receiptDate;
	
	@DBTable(columnName = "RECTIME", required = true)
	private BigDecimal receiptTime;
	
	@DBTable(columnName = "APOS", required = true)
	private BigDecimal apos;

	public BADeliveryNotes() {
		
	}

	
	public BigDecimal getOrderType() {
		return orderType;
	}

	public void setOrderType(BigDecimal orderType) {
		this.orderType = orderType;
	}

	public BigDecimal getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(BigDecimal orderNumber) {
		this.orderNumber = orderNumber;
	}

	public BigDecimal getDeliveredQuantity() {
		return deliveredQuantity;
	}

	public void setDeliveredQuantity(BigDecimal deliveredQuantity) {
		this.deliveredQuantity = deliveredQuantity;
	}

	public BigDecimal getOldPrice() {
		return oldPrice;
	}

	public void setOldPrice(BigDecimal oldPrice) {
		this.oldPrice = oldPrice;
	}

	public BigDecimal getNetPurchasePrice() {
		return netPurchasePrice;
	}

	public void setNetPurchasePrice(BigDecimal netPurchasePrice) {
		this.netPurchasePrice = netPurchasePrice;
	}

	public BigDecimal getReceiptDate() {
		return receiptDate;
	}

	public void setReceiptDate(BigDecimal receiptDate) {
		this.receiptDate = receiptDate;
	}

	public BigDecimal getReceiptTime() {
		return receiptTime;
	}

	public void setReceiptTime(BigDecimal receiptTime) {
		this.receiptTime = receiptTime;
	}


	public BigDecimal getApos() {
		return apos;
	}


	public void setApos(BigDecimal apos) {
		this.apos = apos;
	}

	
}