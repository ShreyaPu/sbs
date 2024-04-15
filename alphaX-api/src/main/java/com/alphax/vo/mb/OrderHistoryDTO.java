package com.alphax.vo.mb;

import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about Orders History Details")
public class OrderHistoryDTO {
	
	private String orderType;
	
	private String orderNumber;
	
	private String customerIdNo;
	
	private String orderDate;
	
	private String manufacturer;
	
	private String purchaseOrderNumber;
	
	private String purchaseOrderElementCount;
	
	private String warehouseNo;
	
	private String purchaseOrderHint;
	
	private String status;
	
	
	@ApiModelProperty(value = "Order Type ( %% - DB O_ORDER - ORDERTYPE).")
	@Size(max=1)
	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	@ApiModelProperty(value = "Order Number ( %% - DB O_ORDER - ORDERNUMBER).")
	@Size(max=6)
	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	@ApiModelProperty(value = "customer Id ( %% - DB O_ORDER - CUSTOMERID).")
	@Size(max=8)
	public String getCustomerIdNo() {
		return customerIdNo;
	}

	public void setCustomerIdNo(String customerIdNo) {
		this.customerIdNo = customerIdNo;
	}

	@ApiModelProperty(value = "Order Date ( %% - DB O_ORDER - TIMESTAMP).")
	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	@ApiModelProperty(value = "Manufacturer (OEM) ( %% - DB O_PRORD - OEM).")
	@Size(max=4)
	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	@ApiModelProperty(value = "Purchase Order Number (New Order) ( %% - DB O_PRORD - PURCHASEORDERNUMBER).")
	@Size(max=19)
	public String getPurchaseOrderNumber() {
		return purchaseOrderNumber;
	}

	public void setPurchaseOrderNumber(String purchaseOrderNumber) {
		this.purchaseOrderNumber = purchaseOrderNumber;
	}

	@ApiModelProperty(value = "Purchase Order Element Count ( %% - DB O_PRORDEL).")
	public String getPurchaseOrderElementCount() {
		return purchaseOrderElementCount;
	}

	public void setPurchaseOrderElementCount(String purchaseOrderElementCount) {
		this.purchaseOrderElementCount = purchaseOrderElementCount;
	}

	@ApiModelProperty(value = "Warehouse Number ( %% - DB PURCHASEORDERELEMENT - WAREHOUSE).")
	@Size(max=3)
	public String getWarehouseNo() {
		return warehouseNo;
	}

	public void setWarehouseNo(String warehouseNo) {
		this.warehouseNo = warehouseNo;
	}

	@ApiModelProperty(value = "Purchase Order Hint ( %% - DB PURCHASEORDERELEMENT - PURCHASEORDERHINT).")
	@Size(max=12)
	public String getPurchaseOrderHint() {
		return purchaseOrderHint;
	}

	public void setPurchaseOrderHint(String purchaseOrderHint) {
		this.purchaseOrderHint = purchaseOrderHint;
	}

	@ApiModelProperty(value = "still pending")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
	

}
