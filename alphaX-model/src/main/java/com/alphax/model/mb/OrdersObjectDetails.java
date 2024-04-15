package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about Orders Details")
public class OrdersObjectDetails {

	@DBTable(columnName ="Bestellnummer", required = true)
	private String newOrderNo;
	
	@DBTable(columnName ="BEST_BENDL", required = true)
	private String supplierNo;
	
	@DBTable(columnName ="Lieferant", required = true)
	private String supplierName;
	
	@DBTable(columnName ="MARKE", required = true)
	private String part_Brand;
	
	@DBTable(columnName ="ETNR", required = true)
	private String partNumber;
	
	@DBTable(columnName ="BEN", required = true)
	private String partDescription;
	
	@DBTable(columnName ="LNR", required = true)
	private String warehouseNumber;	
	
	@DBTable(columnName ="HERST", required = true)
	private String manufacturer;
	
	@DBTable(columnName ="BEMENG", required = true)
	private String totalPartsNumber;
	
	@DBTable(columnName ="PSRV_POSNR", required = true)
	private BigDecimal orderPosition_PSRV;
	
	@DBTable(columnName ="SERV_POSNRX", required = true)
	private String orderPosition_ASRV;
	
	@DBTable(columnName ="DISPOPOS", required = true)
	private String partPosition;
	
	@DBTable(columnName ="DISPOUP", required = true)
	private String partPositionUP;
	
	@DBTable(columnName ="STATUS", required = true)
	private String orderStatus;
	
	@DBTable(columnName ="LIEFNR_GESAMT", required = true)
	private String deliveryNoteNo;
	
	@DBTable(columnName ="GLMENG", required = true)
	private String totalPartsDelivered;
	
	@DBTable(columnName ="OMENG", required = true)
	private String remainingAmount;
	
	@DBTable(columnName ="EPREIS", required = true)
	private BigDecimal part_Price;
	
	@DBTable(columnName ="MCODE", required = true)
	private String part_MarketingCode;
	
	@DBTable(columnName ="RABGR", required = true)
	private String part_DiscountGroup;
	
	@DBTable(columnName ="ETLABEL", required = true)
	private String part_Label;
	
	@DBTable(columnName ="ZUMENG", required = true)
	private String deliveredQuantity;
	
	@DBTable(columnName ="LORT_40", required = true)
	private String storageLocation_1;
	
	@DBTable(columnName ="LORT_ET", required = true)
	private String storageLocation_2;
	

	public OrdersObjectDetails() {

	}


	/**
	 * @return the newOrderNo
	 */
	public String getNewOrderNo() {
		return newOrderNo;
	}


	/**
	 * @param newOrderNo the newOrderNo to set
	 */
	public void setNewOrderNo(String newOrderNo) {
		this.newOrderNo = newOrderNo;
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
	 * @return the supplierName
	 */
	public String getSupplierName() {
		return supplierName;
	}


	/**
	 * @param supplierName the supplierName to set
	 */
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}


	/**
	 * @return the part_Brand
	 */
	public String getPart_Brand() {
		return part_Brand;
	}


	/**
	 * @param part_Brand the part_Brand to set
	 */
	public void setPart_Brand(String part_Brand) {
		this.part_Brand = part_Brand;
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
	 * @return the warehouseNumber
	 */
	public String getWarehouseNumber() {
		return warehouseNumber;
	}


	/**
	 * @param warehouseNumber the warehouseNumber to set
	 */
	public void setWarehouseNumber(String warehouseNumber) {
		this.warehouseNumber = warehouseNumber;
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
	 * @return the totalPartsNumber
	 */
	public String getTotalPartsNumber() {
		return totalPartsNumber;
	}


	/**
	 * @param totalPartsNumber the totalPartsNumber to set
	 */
	public void setTotalPartsNumber(String totalPartsNumber) {
		this.totalPartsNumber = totalPartsNumber;
	}


	/**
	 * @return the orderPosition_PSRV
	 */
	public BigDecimal getOrderPosition_PSRV() {
		return orderPosition_PSRV;
	}


	/**
	 * @param orderPosition_PSRV the orderPosition_PSRV to set
	 */
	public void setOrderPosition_PSRV(BigDecimal orderPosition_PSRV) {
		this.orderPosition_PSRV = orderPosition_PSRV;
	}


	/**
	 * @return the orderPosition_ASRV
	 */
	public String getOrderPosition_ASRV() {
		return orderPosition_ASRV;
	}


	/**
	 * @param orderPosition_ASRV the orderPosition_ASRV to set
	 */
	public void setOrderPosition_ASRV(String orderPosition_ASRV) {
		this.orderPosition_ASRV = orderPosition_ASRV;
	}


	/**
	 * @return the partPosition
	 */
	public String getPartPosition() {
		return partPosition;
	}


	/**
	 * @param partPosition the partPosition to set
	 */
	public void setPartPosition(String partPosition) {
		this.partPosition = partPosition;
	}


	public String getOrderStatus() {
		return orderStatus;
	}


	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}


	public String getDeliveryNoteNo() {
		return deliveryNoteNo;
	}


	public void setDeliveryNoteNo(String deliveryNoteNo) {
		this.deliveryNoteNo = deliveryNoteNo;
	}


	public String getTotalPartsDelivered() {
		return totalPartsDelivered;
	}


	public void setTotalPartsDelivered(String totalPartsDelivered) {
		this.totalPartsDelivered = totalPartsDelivered;
	}


	public String getRemainingAmount() {
		return remainingAmount;
	}


	public void setRemainingAmount(String remainingAmount) {
		this.remainingAmount = remainingAmount;
	}


	public BigDecimal getPart_Price() {
		return part_Price;
	}


	public void setPart_Price(BigDecimal part_Price) {
		this.part_Price = part_Price;
	}


	public String getPart_MarketingCode() {
		return part_MarketingCode;
	}


	public void setPart_MarketingCode(String part_MarketingCode) {
		this.part_MarketingCode = part_MarketingCode;
	}


	public String getPart_DiscountGroup() {
		return part_DiscountGroup;
	}


	public void setPart_DiscountGroup(String part_DiscountGroup) {
		this.part_DiscountGroup = part_DiscountGroup;
	}


	public String getPart_Label() {
		return part_Label;
	}


	public void setPart_Label(String part_Label) {
		this.part_Label = part_Label;
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


	public String getStorageLocation_1() {
		return storageLocation_1;
	}


	public void setStorageLocation_1(String storageLocation_1) {
		this.storageLocation_1 = storageLocation_1;
	}


	public String getStorageLocation_2() {
		return storageLocation_2;
	}


	public void setStorageLocation_2(String storageLocation_2) {
		this.storageLocation_2 = storageLocation_2;
	}


	/**
	 * @return the partPositionUP
	 */
	public String getPartPositionUP() {
		return partPositionUP;
	}


	/**
	 * @param partPositionUP the partPositionUP to set
	 */
	public void setPartPositionUP(String partPositionUP) {
		this.partPositionUP = partPositionUP;
	}
	
	

	
}