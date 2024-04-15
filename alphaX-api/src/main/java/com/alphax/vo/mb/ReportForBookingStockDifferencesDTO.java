package com.alphax.vo.mb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "This Object about Report Selections Booking - stock differences List Details")
public class ReportForBookingStockDifferencesDTO {

	@ApiModelProperty(value = "LAGER  - (DB E_CPSDAT / E_HISTO - VFNR).")
	private String warehouseNo;
	
	@ApiModelProperty(value = "LAGER  - (DB O_WRH - NAME).")
	private String warehouseName;

	@ApiModelProperty(value ="Herkunft - (DB E_CPSDAT / E_HISTO - HERK)." )
	private String origin;

	@ApiModelProperty(value ="DATE - (DB E_CPSDAT / E_HISTO- JJMMTT)." )
	private String date;
	
	@ApiModelProperty(value ="BELEGNUMMER - (DB E_CPSDAT / E_HISTO- BELNR)." )
	private String documentNumber;
	
	@ApiModelProperty(value ="TEILENUMMER - (DB E_CPSDAT / E_HISTO- KB||ETNR||ES1||ES2)." )
	private String partNumber;
	
	@ApiModelProperty(value =" Bezeichnung - (DB E_ETSTAMM / E_HISTO - BENEN)." )
	private String partName;
	
	@ApiModelProperty(value =" Teileart - (DB E_CPSDAT / E_HISTO  - TA_EWV)." )
	private String partType;
	
	@ApiModelProperty(value =" MarketingCode - (DB E_CPSDAT / E_HISTO  - MCODE)." )
	private String marketingCode;
	
	@ApiModelProperty(value =" Marke - (DB E_CPSDAT / E_HISTO  - markeV)." )
	private String partBrand;
	
	@ApiModelProperty(value =" BestandVorher - (DB E_CPSDAT / E_HISTO  - ebest)." )
	private String beforeStock;
	
	@ApiModelProperty(value =" BestandNachher - (DB E_CPSDAT / E_HISTO  - BestandNachher)." )
	private String afterStock;
	
	@ApiModelProperty(value =" Zahldifferenz - (DB E_CPSDAT / E_HISTO  - Zahldifferenz)." )
	private String countDifference;
	
	@ApiModelProperty(value =" DAK - (DB E_CPSDAT / E_HISTO  - DAKEWN)." )
	private String averageNetPrice;
	
	@ApiModelProperty(value =" SummeInEUR - (DB E_CPSDAT / E_HISTO  - SummeInEUR)." )
	private String summeInEUR;
	
	@ApiModelProperty(value =" Buchungskonto - (DB E_CPSDAT / E_HISTO  - BUK_KONTOV)." )
	private String postingAccount;
	
	@ApiModelProperty(value =" Kontobezeichnung - (DB pei_bukont  - BUK_BEZEI)." )
	private String accountDesignation;
	
	@ApiModelProperty(value =" Manufacturer - (DB E_CPSDAT / E_HISTO - HERST)." )
	private String manufacturer;

	/**
	 * @return the warehouseNo
	 */
	public String getWarehouseNo() {
		return warehouseNo;
	}

	/**
	 * @param warehouseNo the warehouseNo to set
	 */
	public void setWarehouseNo(String warehouseNo) {
		this.warehouseNo = warehouseNo;
	}

	/**
	 * @return the warehouseName
	 */
	public String getWarehouseName() {
		return warehouseName;
	}

	/**
	 * @param warehouseName the warehouseName to set
	 */
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
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
	 * @return the beforeStock
	 */
	public String getBeforeStock() {
		return beforeStock;
	}

	/**
	 * @param beforeStock the beforeStock to set
	 */
	public void setBeforeStock(String beforeStock) {
		this.beforeStock = beforeStock;
	}

	/**
	 * @return the afterStock
	 */
	public String getAfterStock() {
		return afterStock;
	}

	/**
	 * @param afterStock the afterStock to set
	 */
	public void setAfterStock(String afterStock) {
		this.afterStock = afterStock;
	}

	/**
	 * @return the countDifference
	 */
	public String getCountDifference() {
		return countDifference;
	}

	/**
	 * @param countDifference the countDifference to set
	 */
	public void setCountDifference(String countDifference) {
		this.countDifference = countDifference;
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
	 * @return the summeInEUR
	 */
	public String getSummeInEUR() {
		return summeInEUR;
	}

	/**
	 * @param summeInEUR the summeInEUR to set
	 */
	public void setSummeInEUR(String summeInEUR) {
		this.summeInEUR = summeInEUR;
	}

	/**
	 * @return the postingAccount
	 */
	public String getPostingAccount() {
		return postingAccount;
	}

	/**
	 * @param postingAccount the postingAccount to set
	 */
	public void setPostingAccount(String postingAccount) {
		this.postingAccount = postingAccount;
	}

	/**
	 * @return the accountDesignation
	 */
	public String getAccountDesignation() {
		return accountDesignation;
	}

	/**
	 * @param accountDesignation the accountDesignation to set
	 */
	public void setAccountDesignation(String accountDesignation) {
		this.accountDesignation = accountDesignation;
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
	
	
	
}
