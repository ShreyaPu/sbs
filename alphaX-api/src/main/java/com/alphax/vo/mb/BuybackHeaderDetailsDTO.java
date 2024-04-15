/**
 * 
 */
package com.alphax.vo.mb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author A87740971
 *
 */
@ApiModel(description = "All fields about AlphaX Buyback header details information")
public class BuybackHeaderDetailsDTO {

	@ApiModelProperty(value = "Part Number - O_BBOFITEM - PARTNRI")
	private String partNumber;
	
	@ApiModelProperty(value = "Part Name - E_ETSTAMM - BENEN")
	private String partName;
	
	@ApiModelProperty(value = "Customer Referance position - O_BBOFITEM - CUSTREFPOS")
	private String custResPos;
	
	@ApiModelProperty(value = "Requested Quantity - O_BBOFITEM - RequestedQuantity")
	private String requestedQuantity;
	
	@ApiModelProperty(value = "Stock - E_ETSTAMM - AKTBES")
	private String stock;
	
	@ApiModelProperty(value = "Item Status - O_BBOFITEM - BBOF_ITEM_STATE")
	private String itemStatus;
	
	@ApiModelProperty(value = "Send Quantity - O_BBOFITEM - SEND_QTY")
	private String sendQuantity;
	
	@ApiModelProperty(value = "Quantity - O_BBOFITEM - QTY")
	private String quantity;
	
	@ApiModelProperty(value = "itemId - O_BBOFITEM - BBOF_ITEM_ID")
	private String itemId;
	
	@ApiModelProperty(value = "Sorted Part Number - O_BBOFITEM - PARTNRS")
	private String sortedPartNumber;
	
	@ApiModelProperty(value = "Captured Part Number - O_BBOFITEM - PARTNRE")
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
	public String getStock() {
		return stock;
	}

	/**
	 * @param stock the stock to set
	 */
	public void setStock(String stock) {
		this.stock = stock;
	}

	/**
	 * @return the itemStatus
	 */
	public String getItemStatus() {
		return itemStatus;
	}

	/**
	 * @param itemStatus the itemStatus to set
	 */
	public void setItemStatus(String itemStatus) {
		this.itemStatus = itemStatus;
	}

	/**
	 * @return the sendQuantity
	 */
	public String getSendQuantity() {
		return sendQuantity;
	}

	/**
	 * @param sendQuantity the sendQuantity to set
	 */
	public void setSendQuantity(String sendQuantity) {
		this.sendQuantity = sendQuantity;
	}

	/**
	 * @return the quantity
	 */
	public String getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the itemId
	 */
	public String getItemId() {
		return itemId;
	}

	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
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
