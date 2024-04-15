package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about Report Selections Booking List Details")
public class ReportSelectionBookingList {

	@DBTable(columnName = "HERST", required = true)
	private String manufacturer;
	
	@DBTable(columnName = "AP_Lager", required = true)
	private BigDecimal alphaPlusWarehouseId;
	
	@DBTable(columnName = "AX_Lager", required = true)
	private String alphaxWarehouseName;

	@DBTable(columnName = "Teilenummer", required = true)
	private String partNumber;
	
	@DBTable(columnName = "Bezeichnung", required = true)
	private String partName;
	
	@DBTable(columnName = "Belegnummer", required = true)
	private String businessCaseDocumentNo;
	
	@DBTable(columnName = "Datum", required = true)
	private String bookingDate;
	
	@DBTable(columnName = "Uhrzeit", required = true)
	private String bookingTime;
	
	@DBTable(columnName = "Bestand_alt", required = true)
	private String bestandAlt;
	
	@DBTable(columnName = "Bestand_neu", required = true)
	private String bestandNeu;
	
	@DBTable(columnName = "Differenz", required = true)
	private Double difference;
	
	@DBTable(columnName = "Teileart", required = true)
	private String partType;
	
	@DBTable(columnName = "Marke", required = true)
	private String partBrand;
	
	@DBTable(columnName = "Leistungsart", required = true)
	private String activityType;
	
	@DBTable(columnName = "Rabattgruppe", required = true)
	private String discountGroup;
	
	@DBTable(columnName = "Rabattsatz", required = true)
	private Double discountGroupPercentageValue;
	
	@DBTable(columnName = "DAK", required = true)
	private Double averageNetPrice;
	
	@DBTable(columnName = "Differenz_EUR", required = true)
	private Double difference_EURO;
	
	@DBTable(columnName = "Buchungskonto", required = true)
	private String bookingAccount;
	
	@DBTable(columnName = "Workstation_ID", required = true)
	private String workstationID;	
	
	@DBTable(columnName = "alphaX_User", required = true)
	private String alphaXUser;
	
	
	public ReportSelectionBookingList() {}
	

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
	 * @return the alphaPlusWarehouseId
	 */
	public BigDecimal getAlphaPlusWarehouseId() {
		return alphaPlusWarehouseId;
	}

	/**
	 * @param alphaPlusWarehouseId the alphaPlusWarehouseId to set
	 */
	public void setAlphaPlusWarehouseId(BigDecimal alphaPlusWarehouseId) {
		this.alphaPlusWarehouseId = alphaPlusWarehouseId;
	}

	/**
	 * @return the alphaxWarehouseName
	 */
	public String getAlphaxWarehouseName() {
		return alphaxWarehouseName;
	}

	/**
	 * @param alphaxWarehouseName the alphaxWarehouseName to set
	 */
	public void setAlphaxWarehouseName(String alphaxWarehouseName) {
		this.alphaxWarehouseName = alphaxWarehouseName;
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
	 * @return the businessCaseDocumentNo
	 */
	public String getBusinessCaseDocumentNo() {
		return businessCaseDocumentNo;
	}

	/**
	 * @param businessCaseDocumentNo the businessCaseDocumentNo to set
	 */
	public void setBusinessCaseDocumentNo(String businessCaseDocumentNo) {
		this.businessCaseDocumentNo = businessCaseDocumentNo;
	}

	/**
	 * @return the bookingDate
	 */
	public String getBookingDate() {
		return bookingDate;
	}

	/**
	 * @param bookingDate the bookingDate to set
	 */
	public void setBookingDate(String bookingDate) {
		this.bookingDate = bookingDate;
	}

	/**
	 * @return the bookingTime
	 */
	public String getBookingTime() {
		return bookingTime;
	}

	/**
	 * @param bookingTime the bookingTime to set
	 */
	public void setBookingTime(String bookingTime) {
		this.bookingTime = bookingTime;
	}

	/**
	 * @return the bestandAlt
	 */
	public String getBestandAlt() {
		return bestandAlt;
	}

	/**
	 * @param bestandAlt the bestandAlt to set
	 */
	public void setBestandAlt(String bestandAlt) {
		this.bestandAlt = bestandAlt;
	}

	/**
	 * @return the bestandNeu
	 */
	public String getBestandNeu() {
		return bestandNeu;
	}

	/**
	 * @param bestandNeu the bestandNeu to set
	 */
	public void setBestandNeu(String bestandNeu) {
		this.bestandNeu = bestandNeu;
	}

	/**
	 * @return the difference
	 */
	public Double getDifference() {
		return difference;
	}

	/**
	 * @param difference the difference to set
	 */
	public void setDifference(Double difference) {
		this.difference = difference;
	}

	/**
	 * @return the partType
	 */
	public String getPartType() {
		return partType;
	}

	/**
	 * @param partType the partType to set
	 */
	public void setPartType(String partType) {
		this.partType = partType;
	}

	/**
	 * @return the partBrand
	 */
	public String getPartBrand() {
		return partBrand;
	}

	/**
	 * @param partBrand the partBrand to set
	 */
	public void setPartBrand(String partBrand) {
		this.partBrand = partBrand;
	}

	/**
	 * @return the activityType
	 */
	public String getActivityType() {
		return activityType;
	}

	/**
	 * @param activityType the activityType to set
	 */
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	/**
	 * @return the discountGroup
	 */
	public String getDiscountGroup() {
		return discountGroup;
	}

	/**
	 * @param discountGroup the discountGroup to set
	 */
	public void setDiscountGroup(String discountGroup) {
		this.discountGroup = discountGroup;
	}

	/**
	 * @return the averageNetPrice
	 */
	public Double getAverageNetPrice() {
		return averageNetPrice;
	}

	/**
	 * @param averageNetPrice the averageNetPrice to set
	 */
	public void setAverageNetPrice(Double averageNetPrice) {
		this.averageNetPrice = averageNetPrice;
	}

	/**
	 * @return the difference_EURO
	 */
	public Double getDifference_EURO() {
		return difference_EURO;
	}

	/**
	 * @param difference_EURO the difference_EURO to set
	 */
	public void setDifference_EURO(Double difference_EURO) {
		this.difference_EURO = difference_EURO;
	}

	/**
	 * @return the bookingAccount
	 */
	public String getBookingAccount() {
		return bookingAccount;
	}

	/**
	 * @param bookingAccount the bookingAccount to set
	 */
	public void setBookingAccount(String bookingAccount) {
		this.bookingAccount = bookingAccount;
	}

	/**
	 * @return the workstationID
	 */
	public String getWorkstationID() {
		return workstationID;
	}

	/**
	 * @param workstationID the workstationID to set
	 */
	public void setWorkstationID(String workstationID) {
		this.workstationID = workstationID;
	}

	/**
	 * @return the alphaXUser
	 */
	public String getAlphaXUser() {
		return alphaXUser;
	}

	/**
	 * @param alphaXUser the alphaXUser to set
	 */
	public void setAlphaXUser(String alphaXUser) {
		this.alphaXUser = alphaXUser;
	}


	/**
	 * @return the discountGroupPercentageValue
	 */
	public Double getDiscountGroupPercentageValue() {
		return discountGroupPercentageValue;
	}


	/**
	 * @param discountGroupPercentageValue the discountGroupPercentageValue to set
	 */
	public void setDiscountGroupPercentageValue(Double discountGroupPercentageValue) {
		this.discountGroupPercentageValue = discountGroupPercentageValue;
	}
	
	
}