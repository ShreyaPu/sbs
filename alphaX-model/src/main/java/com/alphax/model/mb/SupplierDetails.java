package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

public class SupplierDetails {

	@DBTable(columnName = "BENDL", required = true)
	private BigDecimal supplierNo;

	@DBTable(columnName = "NAME1", required = true)
	private String supplierName1;

	@DBTable(columnName = "NAME2", required = true)
	private String supplierName2;
	
	@DBTable(columnName = "STRAS", required = true)
	private String street;
	
	@DBTable(columnName = "ORT", required = true)
	private String ort;
	
	@DBTable(columnName ="ROWNUMER", required = true)
	private Integer totalCount;

	
	public SupplierDetails() {

	}


	/**
	 * @return the supplierNo
	 */
	public BigDecimal getSupplierNo() {
		return supplierNo;
	}


	/**
	 * @param supplierNo the supplierNo to set
	 */
	public void setSupplierNo(BigDecimal supplierNo) {
		this.supplierNo = supplierNo;
	}


	/**
	 * @return the supplierName1
	 */
	public String getSupplierName1() {
		return supplierName1;
	}


	/**
	 * @param supplierName1 the supplierName1 to set
	 */
	public void setSupplierName1(String supplierName1) {
		this.supplierName1 = supplierName1;
	}


	/**
	 * @return the supplierName2
	 */
	public String getSupplierName2() {
		return supplierName2;
	}


	/**
	 * @param supplierName2 the supplierName2 to set
	 */
	public void setSupplierName2(String supplierName2) {
		this.supplierName2 = supplierName2;
	}


	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}


	/**
	 * @param street the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}


	/**
	 * @return the ort
	 */
	public String getOrt() {
		return ort;
	}


	/**
	 * @param ort the ort to set
	 */
	public void setOrt(String ort) {
		this.ort = ort;
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