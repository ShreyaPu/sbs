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

@ApiModel(description = "All fields about AlphaX Buyback header information")
public class BuybackHeaderDTO {

	@ApiModelProperty(value = "Lager Number - O_WRH - Warehouse_ID")
	private String alphaXWarehousId;
	
	@ApiModelProperty(value = "Lager NAME - O_WRH - NAME")
	private String alphaXWarehousName;
	
	@ApiModelProperty(value = "Order Number - O_BBOFHEAD - ORDERNO")
	private String orderNumber;
	
	@ApiModelProperty(value = "Delivery Date- O_BBOFHEAD - DELIVERYDATE")
	private String deliveryDate;
	
	@ApiModelProperty(value = "Order Type - O_BBOFHEAD - ORDERTYPE")
	private String orderType;
	
	@ApiModelProperty(value = "Buyback header status  - O_BBOFHEAD - BBOF_HEADER_STATE")
	private String headerStatus;
	
	@ApiModelProperty(value = "Alpha Plus WarehouseId  - O_BBOFHEAD - AP_LNR")
	private String alphaPlusWarehouseId;
	
	@ApiModelProperty(value = "Buyback header Id  - O_BBOFHEAD - BBOF_HEAD_ID")
	private String headerId;
	
	@ApiModelProperty(value = "Transfer Date - O_BBOFHEAD - TRANSFER_DATE")
	private String transferDate;
	

	
	/**
	 * @return the alphaXWarehousId
	 */
	public String getAlphaXWarehousId() {
		return alphaXWarehousId;
	}

	/**
	 * @param alphaXWarehousId the alphaXWarehousId to set
	 */
	public void setAlphaXWarehousId(String alphaXWarehousId) {
		this.alphaXWarehousId = alphaXWarehousId;
	}

	/**
	 * @return the alphaXWarehousName
	 */
	public String getAlphaXWarehousName() {
		return alphaXWarehousName;
	}

	/**
	 * @param alphaXWarehousName the alphaXWarehousName to set
	 */
	public void setAlphaXWarehousName(String alphaXWarehousName) {
		this.alphaXWarehousName = alphaXWarehousName;
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
	 * @return the deliveryDate
	 */
	public String getDeliveryDate() {
		return deliveryDate;
	}

	/**
	 * @param deliveryDate the deliveryDate to set
	 */
	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	/**
	 * @return the orderType
	 */
	public String getOrderType() {
		return orderType;
	}

	/**
	 * @param orderType the orderType to set
	 */
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	/**
	 * @return the headerStatus
	 */
	public String getHeaderStatus() {
		return headerStatus;
	}

	/**
	 * @param headerStatus the headerStatus to set
	 */
	public void setHeaderStatus(String headerStatus) {
		this.headerStatus = headerStatus;
	}

	/**
	 * @return the alphaPlusWarehouseId
	 */
	public String getAlphaPlusWarehouseId() {
		return alphaPlusWarehouseId;
	}

	/**
	 * @param alphaPlusWarehouseId the alphaPlusWarehouseId to set
	 */
	public void setAlphaPlusWarehouseId(String alphaPlusWarehouseId) {
		this.alphaPlusWarehouseId = alphaPlusWarehouseId;
	}

	/**
	 * @return the headerId
	 */
	public String getHeaderId() {
		return headerId;
	}

	/**
	 * @param headerId the headerId to set
	 */
	public void setHeaderId(String headerId) {
		this.headerId = headerId;
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
	
		
}
