package com.alphax.model.mb;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about Orders List")
public class OrdersObject {

	@DBTable(columnName ="LNR", required = true)
	private String warehouseNumber;
	
	@DBTable(columnName ="HERST", required = true)
	private String manufacturer;
	
	@DBTable(columnName ="PSERV_AUFNR", required = true)
	private String pserv_Aufnr;
	
	@DBTable(columnName ="SERV_AUFNRX", required = true)
	private String aserv_Aufnr;
	
	@DBTable(columnName ="Bestellnummer", required = true)
	private String newOrderNo;
	
	@DBTable(columnName ="Positionen", required = true)
	private String orderPosition;
	
	@DBTable(columnName ="BDAT", required = true)
	private String orderDate;	
	
	@DBTable(columnName ="BEST_BENDL", required = true)
	private String supplierNo;
	
	@DBTable(columnName ="Lieferant", required = true)
	private String supplierName;
	
	@DBTable(columnName ="Hinweis", required = true)
	private String orderNote;
	
	@DBTable(columnName ="STATUS", required = true)
	private String orderStatus;
	
	@DBTable(columnName ="KUNDEN", required = true)
	private String customerName;
	
	@DBTable(columnName ="ROWNUMER", required = true)
	private Integer totalCount;
	

	public OrdersObject() {

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
	 * @return the pserv_Aufnr
	 */
	public String getPserv_Aufnr() {
		return pserv_Aufnr;
	}


	/**
	 * @param pserv_Aufnr the pserv_Aufnr to set
	 */
	public void setPserv_Aufnr(String pserv_Aufnr) {
		this.pserv_Aufnr = pserv_Aufnr;
	}


	/**
	 * @return the aserv_Aufnr
	 */
	public String getAserv_Aufnr() {
		return aserv_Aufnr;
	}


	/**
	 * @param aserv_Aufnr the aserv_Aufnr to set
	 */
	public void setAserv_Aufnr(String aserv_Aufnr) {
		this.aserv_Aufnr = aserv_Aufnr;
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
	 * @return the orderPosition
	 */
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
	 * @return the orderDate
	 */
	public String getOrderDate() {
		return orderDate;
	}


	/**
	 * @param orderDate the orderDate to set
	 */
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
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
	 * @return the orderNote
	 */
	public String getOrderNote() {
		return orderNote;
	}


	/**
	 * @param orderNote the orderNote to set
	 */
	public void setOrderNote(String orderNote) {
		this.orderNote = orderNote;
	}


	/**
	 * @return the orderStatus
	 */
	public String getOrderStatus() {
		return orderStatus;
	}


	/**
	 * @param orderStatus the orderStatus to set
	 */
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}


	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}


	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}


	/**
	 * @return the totalCount
	 */
	public Integer getTotalCount() {
		return totalCount;
	}


	/**
	 * @param totalCount the totalCount to set
	 */
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

}