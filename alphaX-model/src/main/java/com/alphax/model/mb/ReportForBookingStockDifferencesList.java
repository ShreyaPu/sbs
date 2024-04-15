package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about Report Selections Booking - stock differences List Details")
public class ReportForBookingStockDifferencesList {

	//stock differences = Bestandsunterschiede
	
	@DBTable(columnName = "BA", required = true)
	private String businessCase;
	
	@DBTable(columnName = "HERST", required = true)
	private String manufacturer;
	
	@DBTable(columnName = "AP_Lager", required = true)
	private BigDecimal alphaPlusWarehouseId;
	
	@DBTable(columnName = "AX_Lager", required = true)
	private String alphaxWarehouseName;
	
	@DBTable(columnName = "Herkunft", required = true)
	private String origin;
	
	@DBTable(columnName = "Datum", required = true)
	private String bookingDate;
	
	@DBTable(columnName = "Belegnummer", required = true)
	private String documentNo;

	@DBTable(columnName = "Teilenummer", required = true)
	private String partNumber;
	
	@DBTable(columnName = "Bezeichnung", required = true)
	private String partName;
	
	@DBTable(columnName = "Teileart", required = true)
	private String partType;
	
	@DBTable(columnName = "MarketingCode", required = true)
	private String marketingCode;
	
	@DBTable(columnName = "Marke", required = true)
	private String partBrand;
	
	@DBTable(columnName = "BestandVorher", required = true)
	private String beforeStock;
	
	@DBTable(columnName = "BestandNachher", required = true)
	private String afterStock;
	
	@DBTable(columnName = "Zahldifferenz", required = true)
	private String countDifference;
	
	@DBTable(columnName = "DAK", required = true)
	private Double averageNetPrice;
	
	@DBTable(columnName = "SummeInEUR", required = true)
	private Double summeInEUR;
	
	@DBTable(columnName = "Buchungskonto", required = true)
	private String postingAccount;
	
	@DBTable(columnName = "Kontobezeichnung", required = true)
	private String accountDesignation;

	/**
	 * @return the businessCase
	 */
	public String getBusinessCase() {
		return businessCase;
	}

	/**
	 * @param businessCase the businessCase to set
	 */
	public void setBusinessCase(String businessCase) {
		this.businessCase = businessCase;
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
	 * @return the businessCaseDocumentNo
	 */
	public String getDocumentNo() {
		return documentNo;
	}

	/**
	 * @param businessCaseDocumentNo the businessCaseDocumentNo to set
	 */
	public void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
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
	 * @return the summeInEUR
	 */
	public Double getSummeInEUR() {
		return summeInEUR;
	}

	/**
	 * @param summeInEUR the summeInEUR to set
	 */
	public void setSummeInEUR(Double summeInEUR) {
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
	
	
	
	
	
	
	
}
