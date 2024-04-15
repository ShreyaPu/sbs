package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about AlphaX Buyback items information")
public class BuybackItemInfo {

	@DBTable(columnName ="PARTNRI", required = true)
	private String partNumber;
	
	@DBTable(columnName ="BENEN", required = true)
	private String partName;
	
	@DBTable(columnName ="CUSTREFPOS", required = true)
	private String custResPos;
	
	@DBTable(columnName ="RequestedQuantity", required = true)
	private String requestedQuantity;
	
	@DBTable(columnName ="AKTBES", required = true)
	private BigDecimal stock;
	
	@DBTable(columnName ="BBOF_ITEM_STATE", required = true)
	private Integer itemStatus;
	
	@DBTable(columnName ="SEND_QTY", required = true)
	private BigDecimal sendQuantity;
	
	@DBTable(columnName ="QTY", required = true)
	private BigDecimal quantity;
	
	@DBTable(columnName ="BBOF_ITEM_ID", required = true)
	private Long itemId;
	
	@DBTable(columnName ="LOPA", required = true)
	private String storageLocation;
	
	@DBTable(columnName ="NETVALUE", required = true)
	private BigDecimal netValue;
	
	@DBTable(columnName ="SEND_DAK", required = true)
	private BigDecimal sendDAK;
	
	@DBTable(columnName ="TRANSFER_DATE", required = true)
	private String transferDate;
	
	@DBTable(columnName ="PARTNRS", required = true)
	private String sortedPartNumber;
	
	@DBTable(columnName ="PARTNRE", required = true)
	private String capturePartNumber;
	

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
	 * @return the custResPos
	 */
	public String getCustResPos() {
		return custResPos;
	}

	/**
	 * @param custResPos the custResPos to set
	 */
	public void setCustResPos(String custResPos) {
		this.custResPos = custResPos;
	}

	/**
	 * @return the requestedQuantity
	 */
	public String getRequestedQuantity() {
		return requestedQuantity;
	}

	/**
	 * @param requestedQuantity the requestedQuantity to set
	 */
	public void setRequestedQuantity(String requestedQuantity) {
		this.requestedQuantity = requestedQuantity;
	}

	/**
	 * @return the stock
	 */
	public BigDecimal getStock() {
		return stock;
	}

	/**
	 * @param stock the stock to set
	 */
	public void setStock(BigDecimal stock) {
		this.stock = stock;
	}

	/**
	 * @return the itemStatus
	 */
	public Integer getItemStatus() {
		return itemStatus;
	}

	/**
	 * @param itemStatus the itemStatus to set
	 */
	public void setItemStatus(Integer itemStatus) {
		this.itemStatus = itemStatus;
	}

	/**
	 * @return the sendQuantity
	 */
	public BigDecimal getSendQuantity() {
		return sendQuantity;
	}

	/**
	 * @param sendQuantity the sendQuantity to set
	 */
	public void setSendQuantity(BigDecimal sendQuantity) {
		this.sendQuantity = sendQuantity;
	}

	/**
	 * @return the quantity
	 */
	public BigDecimal getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the itemId
	 */
	public Long getItemId() {
		return itemId;
	}

	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	
	/**
	 * @return the storageLocation
	 */
	public String getStorageLocation() {
		return storageLocation;
	}

	
	/**
	 * @param storageLocation the storageLocation to set
	 */
	public void setStorageLocation(String storageLocation) {
		this.storageLocation = storageLocation;
	}

	/**
	 * @return the netValue
	 */
	public BigDecimal getNetValue() {
		return netValue;
	}

	/**
	 * @param netValue the netValue to set
	 */
	public void setNetValue(BigDecimal netValue) {
		this.netValue = netValue;
	}

	/**
	 * @return the sendDAK
	 */
	public BigDecimal getSendDAK() {
		return sendDAK;
	}

	/**
	 * @param sendDAK the sendDAK to set
	 */
	public void setSendDAK(BigDecimal sendDAK) {
		this.sendDAK = sendDAK;
	}

	/**
	 * @return the transferDate
	 */
	public String getTransferDate() {
		return transferDate;
	}

	/**
	 * @param transferDate the transferDate to set
	 */
	public void setTransferDate(String transferDate) {
		this.transferDate = transferDate;
	}

	/**
	 * @return the sortedPartNumber
	 */
	public String getSortedPartNumber() {
		return sortedPartNumber;
	}

	/**
	 * @param sortedPartNumber the sortedPartNumber to set
	 */
	public void setSortedPartNumber(String sortedPartNumber) {
		this.sortedPartNumber = sortedPartNumber;
	}

	/**
	 * @return the capturePartNumber
	 */
	public String getCapturePartNumber() {
		return capturePartNumber;
	}

	/**
	 * @param capturePartNumber the capturePartNumber to set
	 */
	public void setCapturePartNumber(String capturePartNumber) {
		this.capturePartNumber = capturePartNumber;
	}
	
	
}