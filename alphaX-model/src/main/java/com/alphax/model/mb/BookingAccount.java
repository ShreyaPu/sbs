package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about AlphaX BookingAccount Buchungskonto - PEI_BUKONT")
public class BookingAccount {

	@DBTable(columnName ="BUK_LNR", required = true)
	private String warehouseId;
	
	@DBTable(columnName ="BUK_TMARKE", required = true)
	private String brand;
	
	@DBTable(columnName ="BUK_TEIART", required = true)
	private String partsIndikator;
	
	@DBTable(columnName ="BUK_BEZEI", required = true)
	private String description;
	
	@DBTable(columnName ="BUK_KONTO", required = true)
	private BigDecimal bookingNumber;
	
	@DBTable(columnName ="BUK_DTGULV", required = true)
	private String validFromDate;	
	
	@DBTable(columnName ="countPositive", required = true)
	private Double countPositive;
	
	@DBTable(columnName ="countNegative", required = true)
	private Double countNegative;
	
	@DBTable(columnName ="totalCount", required = true)
	private Double totalCount;

	public BookingAccount() {
		
	}


	/**
	 * @return the warehouseId
	 */
	public String getWarehouseId() {
		return warehouseId;
	}


	/**
	 * @param warehouseId the warehouseId to set
	 */
	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}


	/**
	 * @return the brand
	 */
	public String getBrand() {
		return brand;
	}


	/**
	 * @param brand the brand to set
	 */
	public void setBrand(String brand) {
		this.brand = brand;
	}


	/**
	 * @return the partsIndikator
	 */
	public String getPartsIndikator() {
		return partsIndikator;
	}


	/**
	 * @param partsIndikator the partsIndikator to set
	 */
	public void setPartsIndikator(String partsIndikator) {
		this.partsIndikator = partsIndikator;
	}


	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}


	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}


	/**
	 * @return the bookingNumber
	 */
	public BigDecimal getBookingNumber() {
		return bookingNumber;
	}


	/**
	 * @param bookingNumber the bookingNumber to set
	 */
	public void setBookingNumber(BigDecimal bookingNumber) {
		this.bookingNumber = bookingNumber;
	}


	/**
	 * @return the validFromDate
	 */
	public String getValidFromDate() {
		return validFromDate;
	}


	/**
	 * @param validFromDate the validFromDate to set
	 */
	public void setValidFromDate(String validFromDate) {
		this.validFromDate = validFromDate;
	}


	/**
	 * @return the countPositive
	 */
	public Double getCountPositive() {
		return countPositive;
	}


	/**
	 * @param countPositive the countPositive to set
	 */
	public void setCountPositive(Double countPositive) {
		this.countPositive = countPositive;
	}


	/**
	 * @return the countNegative
	 */
	public Double getCountNegative() {
		return countNegative;
	}


	/**
	 * @param countNegative the countNegative to set
	 */
	public void setCountNegative(Double countNegative) {
		this.countNegative = countNegative;
	}


	/**
	 * @return the totalCount
	 */
	public Double getTotalCount() {
		return totalCount;
	}


	/**
	 * @param totalCount the totalCount to set
	 */
	public void setTotalCount(Double totalCount) {
		this.totalCount = totalCount;
	}


}