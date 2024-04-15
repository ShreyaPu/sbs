package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about AlphaX Buyback header information")
public class BuybackHeaderInfo {

	@DBTable(columnName ="BBOF_HEAD_ID", required = true)
	private Long headerId;
	
	@DBTable(columnName ="WAREHOUS_ID", required = true)
	private BigDecimal alphaXWarehousId;
	
	@DBTable(columnName ="WAREHOUS_NAME", required = true)
	private String alphaXWarehousName;
	
	@DBTable(columnName ="ORDERNO", required = true)
	private String orderNumber;
	
	@DBTable(columnName ="DELIVERYDATE", required = true)
	private String deliveryDate;
	
	@DBTable(columnName ="ORDERTYPE", required = true)
	private String orderType;
	
	@DBTable(columnName ="BBOF_HEADER_STATE", required = true)
	private Integer headerStatus;
	
	@DBTable(columnName ="AP_LNR", required = true)
	private Integer alphaPlusWarehouseId;

	@DBTable(columnName ="TRANSFER_DATE", required = true)
	private String transferDate;	
	
	@DBTable(columnName ="FACINGLOC", required = true)
	private Integer faceLoctionId;
	
	@DBTable(columnName ="LOCATION", required = true)
	private String faceLocation;	
	

	/**
	 * @return the alphaXWarehousId
	 */
	public BigDecimal getAlphaXWarehousId() {
		return alphaXWarehousId;
	}

	/**
	 * @param alphaXWarehousId the alphaXWarehousId to set
	 */
	public void setAlphaXWarehousId(BigDecimal alphaXWarehousId) {
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
	public Integer getHeaderStatus() {
		return headerStatus;
	}

	/**
	 * @param headerStatus the headerStatus to set
	 */
	public void setHeaderStatus(Integer headerStatus) {
		this.headerStatus = headerStatus;
	}

	/**
	 * @return the alphaPlusWarehouseId
	 */
	public Integer getAlphaPlusWarehouseId() {
		return alphaPlusWarehouseId;
	}

	/**
	 * @param alphaPlusWarehouseId the alphaPlusWarehouseId to set
	 */
	public void setAlphaPlusWarehouseId(Integer alphaPlusWarehouseId) {
		this.alphaPlusWarehouseId = alphaPlusWarehouseId;
	}

	/**
	 * @return the headerId
	 */
	public Long getHeaderId() {
		return headerId;
	}

	/**
	 * @param headerId the headerId to set
	 */
	public void setHeaderId(Long headerId) {
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

	/**
	 * @return the faceLoctionId
	 */
	public Integer getFaceLoctionId() {
		return faceLoctionId;
	}

	/**
	 * @param faceLoctionId the faceLoctionId to set
	 */
	public void setFaceLoctionId(Integer faceLoctionId) {
		this.faceLoctionId = faceLoctionId;
	}

	/**
	 * @return the faceLocation
	 */
	public String getFaceLocation() {
		return faceLocation;
	}

	/**
	 * @param faceLocation the faceLocation to set
	 */
	public void setFaceLocation(String faceLocation) {
		this.faceLocation = faceLocation;
	}
	
	
}
