package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about Orders History Details")
public class OrderHistory {

	@DBTable(columnName ="KDNRFZ", required = true)
	private String customerId;
	
	@DBTable(columnName ="FZNUMMER", required = true)
	private String vehicleId;
	
	@DBTable(columnName ="EKNPR", required = true)
	private String purchasePrice;
	
	@DBTable(columnName ="EPR", required = true)
	private String sellingPrice;
	
	@DBTable(columnName ="MC ", required = true)
	private String partCode;
	
	@DBTable(columnName ="ORDERTYPE", required = true)
	private String orderType;
	
	@DBTable(columnName ="ORDERNUMBER", required = true)
	private BigDecimal orderNumber;
	
	@DBTable(columnName ="CUSTOMERID", required = true)
	private BigDecimal customerIdNo;
	
	@DBTable(columnName ="TIMESTAMP", required = true)
	private String orderDate;
	
	@DBTable(columnName ="OEM", required = true)
	private String manufacturer;
	
	@DBTable(columnName ="PURCHASEORDERNUMBER", required = true)
	private String purchaseOrderNumber;
	
	@DBTable(columnName ="POE_COUNT", required = true)
	private String purchaseOrderElementCount;
	
	@DBTable(columnName ="WAREHOUSE", required = true)
	private BigDecimal warehouseNo;
	
	@DBTable(columnName ="PURCHASEORDERHINT", required = true)
	private String purchaseOrderHint;
	
	@DBTable(columnName ="ROWNUMER", required = true)
	private Integer totalCount;
	
	

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(String purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public String getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(String sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public String getPartCode() {
		return partCode;
	}

	public void setPartCode(String partCode) {
		this.partCode = partCode;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public BigDecimal getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(BigDecimal orderNumber) {
		this.orderNumber = orderNumber;
	}

	public BigDecimal getCustomerIdNo() {
		return customerIdNo;
	}

	public void setCustomerIdNo(BigDecimal customerIdNo) {
		this.customerIdNo = customerIdNo;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getPurchaseOrderNumber() {
		return purchaseOrderNumber;
	}

	public void setPurchaseOrderNumber(String purchaseOrderNumber) {
		this.purchaseOrderNumber = purchaseOrderNumber;
	}

	public String getPurchaseOrderElementCount() {
		return purchaseOrderElementCount;
	}

	public void setPurchaseOrderElementCount(String purchaseOrderElementCount) {
		this.purchaseOrderElementCount = purchaseOrderElementCount;
	}

	public BigDecimal getWarehouseNo() {
		return warehouseNo;
	}

	public void setWarehouseNo(BigDecimal warehouseNo) {
		this.warehouseNo = warehouseNo;
	}

	public String getPurchaseOrderHint() {
		return purchaseOrderHint;
	}

	public void setPurchaseOrderHint(String purchaseOrderHint) {
		this.purchaseOrderHint = purchaseOrderHint;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	
	

}
