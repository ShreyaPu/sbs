package com.alphax.vo.mb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "This Object about Report Selections Details")
public class ReportSelectionsForMovementDTO {

	@ApiModelProperty(value = "Hersteller  - (DB E_CPSDAT- HERST).")
	private String manufacturer;
	
	@ApiModelProperty(value = "LAGER  - (DB E_CPSDAT AND O_WRH - VFNR - NAME).")
	private String warehouse;
	
	@ApiModelProperty(value = "Bewegungsart  - (DB E_CPSDAT AND O_BA - BART - DESCRIPTION ).")
	private String movementType;

	@ApiModelProperty(value ="DATE - (DB E_CPSDAT- JJMMTT)." )
	private String date;
	
	@ApiModelProperty(value ="TEILENUMMER - (DB E_CPSDAT- KB||ETNR||ES1||ES2)." )
	private String partNumber;
	
	@ApiModelProperty(value ="BELEGNUMMER - (DB E_CPSDAT- BELNR)." )
	private String documentNumber;
	
	@ApiModelProperty(value ="Marke - (DB E_CPSDAT - marken)." )
	private String brand;
	
	@ApiModelProperty(value ="Positionsnummer - (DB E_CPSDAT- POSNR )." )
	private String positionNo;
	
	@ApiModelProperty(value ="Uhrzeit - (DB E_CPSDAT- HHMMSS )." )
	private String time;
	
	@ApiModelProperty(value ="WS ID - (DB E_CPSDAT- WS)." )
	private String wsId;
	
	@ApiModelProperty(value ="Nutzer - (DB E_CPSDAT- WSUSER)." )
	private String users;
	
	@ApiModelProperty(value ="Kundennummer - (DB E_CPSDAT- kdnr)." )
	private String customerNo;
	
	@ApiModelProperty(value ="Marketingcode - (DB E_CPSDAT- mcode)." )
	private String marketingCode;
	
	@ApiModelProperty(value ="Lieferantennummer - (DB E_CPSDAT- BSN_LC)." )
	private String supplierNo;
	
	@ApiModelProperty(value ="T1 - (DB E_CPSDAT- T1)." )
	private String t1Value;
	
	@ApiModelProperty(value ="T2 - (DB E_CPSDAT- T2)." )
	private String t2Value;
	
	@ApiModelProperty(value ="T3 - (DB E_CPSDAT- T3)." )
	private String t3Value;
	
	@ApiModelProperty(value ="T1 - (DB E_CPSDAT- SUBSTR(T1, 2, 6) )." )
	private String menge;

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
	 * @return the movementType
	 */
	public String getMovementType() {
		return movementType;
	}

	/**
	 * @param movementType the movementType to set
	 */
	public void setMovementType(String movementType) {
		this.movementType = movementType;
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
	 * @return the positionNo
	 */
	public String getPositionNo() {
		return positionNo;
	}

	/**
	 * @param positionNo the positionNo to set
	 */
	public void setPositionNo(String positionNo) {
		this.positionNo = positionNo;
	}

	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * @return the wsId
	 */
	public String getWsId() {
		return wsId;
	}

	/**
	 * @param wsId the wsId to set
	 */
	public void setWsId(String wsId) {
		this.wsId = wsId;
	}

	/**
	 * @return the users
	 */
	public String getUsers() {
		return users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(String users) {
		this.users = users;
	}

	/**
	 * @return the customerNo
	 */
	public String getCustomerNo() {
		return customerNo;
	}

	/**
	 * @param customerNo the customerNo to set
	 */
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	/**
	 * @return the marketingCode
	 */
	public String getMarketingCode() {
		return marketingCode;
	}

	/**
	 * @param marketingCode the marketingCode to set
	 */
	public void setMarketingCode(String marketingCode) {
		this.marketingCode = marketingCode;
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
	 * @return the t1Value
	 */
	public String getT1Value() {
		return t1Value;
	}

	/**
	 * @param t1Value the t1Value to set
	 */
	public void setT1Value(String t1Value) {
		this.t1Value = t1Value;
	}

	/**
	 * @return the t2Value
	 */
	public String getT2Value() {
		return t2Value;
	}

	/**
	 * @param t2Value the t2Value to set
	 */
	public void setT2Value(String t2Value) {
		this.t2Value = t2Value;
	}

	/**
	 * @return the t3Value
	 */
	public String getT3Value() {
		return t3Value;
	}

	/**
	 * @param t3Value the t3Value to set
	 */
	public void setT3Value(String t3Value) {
		this.t3Value = t3Value;
	}

	public String getMenge() {
		return menge;
	}

	public void setMenge(String menge) {
		this.menge = menge;
	}
	
	
	
}
