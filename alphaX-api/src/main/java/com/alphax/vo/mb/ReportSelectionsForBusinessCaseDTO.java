package com.alphax.vo.mb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "This Object about Report Selections Details")
public class ReportSelectionsForBusinessCaseDTO {

	@ApiModelProperty(value = "LAGER  - (DB E_CPSDAT AND O_WRH - VFNR - NAME).")
	private String warehouse;

	@ApiModelProperty(value ="DATE - (DB E_CPSDAT- JJMMTT)." )
	private String date;
	
	@ApiModelProperty(value ="TEILENUMMER - (DB E_CPSDAT- KB||ETNR||ES1||ES2)." )
	private String partNumber;
	
	@ApiModelProperty(value ="BELEGNUMMER - (DB E_CPSDAT- BELNR)." )
	private String documentNumber;
	
	@ApiModelProperty(value ="Herkunft - (DB E_CPSDAT - HERK)." )
	private String origin;
	
	@ApiModelProperty(value ="Zähldifferenz - (DB E_CPSDAT )." )
	private String countingDifference;
	
	@ApiModelProperty(value ="Buchungsmenge - (DB E_CPSDAT )." )
	private String bookingAmount;
	
	@ApiModelProperty(value ="DAK-ALT - (DB E_CPSDAT)." )
	private String dakAlt;
	
	@ApiModelProperty(value ="DAK-NEU - (DB E_CPSDAT)." )
	private String dakNeu;
	
	@ApiModelProperty(value ="BESTAND-ALT - (DB E_CPSDAT)." )
	private String bestandAlt;
	
	@ApiModelProperty(value ="BESTAND-NEU - (DB E_CPSDAT)." )
	private String bestandNeu;
	
	@ApiModelProperty(value =" Manufacturer - (DB E_CPSDAT - HERST)." )
	private String manufacturer;
	
	@ApiModelProperty(value =" Bezeichnung - (DB E_ETSTAMM - BENEN)." )
	private String partName;
	
	@ApiModelProperty(value =" Teileart - (DB E_CPSDAT,O_PARTKIND  - TA_EWN - KINDNAME)." )
	private String partType;
	
	@ApiModelProperty(value =" Leistungsart - (DB E_CPSDAT - LEIARTN)." )
	private String activityType;
	
	@ApiModelProperty(value =" Rabattgruppe - (DB E_CPSDAT - RABGRN)." )
	private String discountGroup;
	
	@ApiModelProperty(value =" Rabattsatz - (DB E_CPSDAT - PROZ)." )
	private String discountGroupPercentageValue;
	
	@ApiModelProperty(value =" DAK - (DB E_CPSDAT - DAKEWN)." )
	private String averageNetPrice;
	
	@ApiModelProperty(value =" Buchungskonto - (DB E_CPSDAT - BUK_KONTON)." )
	private String bookingAccount;

	@ApiModelProperty(value =" Differenz_EUR - (DB E_CPSDAT)." )
	private String difference_EURO;
	
	
	/**
	 * @return the warehouse
	 */
	public String getWarehouse() {
		return warehouse;
	}

	/**
	 * @param warehouse the warehouse to set
	 */
	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
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
	 * @return the documentNumber
	 */
	public String getDocumentNumber() {
		return documentNumber;
	}

	/**
	 * @param documentNumber the documentNumber to set
	 */
	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	/**
	 * @return the origin
	 */
	public String getOrigin() {
		return origin;
	}

	/**
	 * @param origin the origin to set
	 */
	public void setOrigin(String origin) {
		this.origin = origin;
	}

	/**
	 * @return the countingDifference
	 */
	public String getCountingDifference() {
		return countingDifference;
	}

	/**
	 * @param countingDifference the countingDifference to set
	 */
	public void setCountingDifference(String countingDifference) {
		this.countingDifference = countingDifference;
	}

	/**
	 * @return the bookingAmount
	 */
	public String getBookingAmount() {
		return bookingAmount;
	}

	/**
	 * @param bookingAmount the bookingAmount to set
	 */
	public void setBookingAmount(String bookingAmount) {
		this.bookingAmount = bookingAmount;
	}

	/**
	 * @return the dakAlt
	 */
	public String getDakAlt() {
		return dakAlt;
	}

	/**
	 * @param dakAlt the dakAlt to set
	 */
	public void setDakAlt(String dakAlt) {
		this.dakAlt = dakAlt;
	}

	/**
	 * @return the dakNeu
	 */
	public String getDakNeu() {
		return dakNeu;
	}

	/**
	 * @param dakNeu the dakNeu to set
	 */
	public void setDakNeu(String dakNeu) {
		this.dakNeu = dakNeu;
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
	 * @return the discountGroupPercentageValue
	 */
	public String getDiscountGroupPercentageValue() {
		return discountGroupPercentageValue;
	}

	/**
	 * @param discountGroupPercentageValue the discountGroupPercentageValue to set
	 */
	public void setDiscountGroupPercentageValue(String discountGroupPercentageValue) {
		this.discountGroupPercentageValue = discountGroupPercentageValue;
	}

	/**
	 * @return the averageNetPrice
	 */
	public String getAverageNetPrice() {
		return averageNetPrice;
	}

	/**
	 * @param averageNetPrice the averageNetPrice to set
	 */
	public void setAverageNetPrice(String averageNetPrice) {
		this.averageNetPrice = averageNetPrice;
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
	 * @return the difference_EURO
	 */
	public String getDifference_EURO() {
		return difference_EURO;
	}

	/**
	 * @param difference_EURO the difference_EURO to set
	 */
	public void setDifference_EURO(String difference_EURO) {
		this.difference_EURO = difference_EURO;
	}
	
	
	
	
	
}
