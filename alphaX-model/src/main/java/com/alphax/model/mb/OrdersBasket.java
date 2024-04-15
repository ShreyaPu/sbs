package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about Orders Basket")
public class OrdersBasket {

	@DBTable(columnName ="ET_LNR", required = true)
	private String warehouseNumber;
	
	@DBTable(columnName ="BHERST", required = true)
	private String manufacturer;
	
	@DBTable(columnName ="PSERV_AUFNR", required = true)
	private String pserv_Aufnr;
	
	@DBTable(columnName ="ASERV_AUFNR", required = true)
	private String aserv_Aufnr;
	
	@DBTable(columnName ="Positionen", required = true)
	private String orderPosition;
	
	@DBTable(columnName ="BDAT", required = true)
	private String orderDate;	
	
	@DBTable(columnName ="BZEIT", required = true)
	private String orderTime;
	
	@DBTable(columnName ="ROWNUMER", required = true)
	private Integer totalCount;
	
	@DBTable(columnName ="BEST_BEND", required = true)
	private String supplierNo;
	
	@DBTable(columnName ="BEST_DESC", required = true)
	private String supplierName;
	
	@DBTable(columnName ="ET_HERST", required = true)
	private String etManufacturer;
	
	@DBTable(columnName ="KUNDEN", required = true)
	private String customerName;
	
	@DBTable(columnName ="ASERV_FA", required = true)
	private BigDecimal aserv_FA;
	
	@DBTable(columnName ="ASERV_FIL", required = true)
	private BigDecimal aserv_FIL;
	
	@DBTable(columnName ="PSERV_FA", required = true)
	private BigDecimal pserv_FA;
	
	@DBTable(columnName ="PSERV_FIL", required = true)
	private BigDecimal pserv_FIL;
	

	public OrdersBasket() {

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
	 * @return the orderTime
	 */
	public String getOrderTime() {
		return orderTime;
	}


	/**
	 * @param orderTime the orderTime to set
	 */
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
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
	 * @return the etManufacturer
	 */
	public String getEtManufacturer() {
		return etManufacturer;
	}


	/**
	 * @param etManufacturer the etManufacturer to set
	 */
	public void setEtManufacturer(String etManufacturer) {
		this.etManufacturer = etManufacturer;
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


	public BigDecimal getAserv_FA() {
		return aserv_FA;
	}


	public void setAserv_FA(BigDecimal aserv_FA) {
		this.aserv_FA = aserv_FA;
	}


	public BigDecimal getAserv_FIL() {
		return aserv_FIL;
	}


	public void setAserv_FIL(BigDecimal aserv_FIL) {
		this.aserv_FIL = aserv_FIL;
	}


	public BigDecimal getPserv_FA() {
		return pserv_FA;
	}


	public void setPserv_FA(BigDecimal pserv_FA) {
		this.pserv_FA = pserv_FA;
	}


	public BigDecimal getPserv_FIL() {
		return pserv_FIL;
	}


	public void setPserv_FIL(BigDecimal pserv_FIL) {
		this.pserv_FIL = pserv_FIL;
	}
	
	

}