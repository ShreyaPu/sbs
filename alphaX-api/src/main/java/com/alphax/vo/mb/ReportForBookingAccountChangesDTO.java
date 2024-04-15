package com.alphax.vo.mb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "This Object about Report Selections Booking - Account Changes List Details")
public class ReportForBookingAccountChangesDTO {

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
	
	//before
	@ApiModelProperty(value =" TeilartVorher - (DB E_CPSDAT / E_HISTO  - TA_EWV)." )
	private String beforePartType;
	
	@ApiModelProperty(value =" MarketingCodeVorher - (DB E_CPSDAT / E_HISTO  - MCODE)." )
	private String beforeMarketingCode;
	
	@ApiModelProperty(value =" MarkeVorher - (DB E_CPSDAT / E_HISTO  - markeV)." )
	private String beforePartBrand;
	
	@ApiModelProperty(value =" BuchungskontoVorher - (DB E_CPSDAT / E_HISTO  - BUK_KONTON)." )
	private String beforePostingAccount;
	
	@ApiModelProperty(value =" KontobezeichnungVorher - (DB pei_bukont  - BUK_BEZEI)." )
	private String beforeAccountDesignation;
	
	//after
	
	@ApiModelProperty(value =" TeileartNachher - (DB E_CPSDAT / E_HISTO  - TA_EWN)." )
	private String afterPartType;
	
	@ApiModelProperty(value =" MarketingCodeNachher - (DB E_CPSDAT / E_HISTO  - MCODEV)." )
	private String afterMarketingCode;
	
	@ApiModelProperty(value =" MarkeNachher - (DB E_CPSDAT / E_HISTO  - marken)." )
	private String afterPartBrand;
	
	@ApiModelProperty(value =" BuchungskontoNachher - (DB E_CPSDAT / E_HISTO  - BUK_KONTON)." )
	private String afterPostingAccount;
	
	@ApiModelProperty(value =" KontobezeichnungNachher - (DB pei_bukont  - BUK_BEZEI)." )
	private String afterAccountDesignation;
	
	@ApiModelProperty(value =" Bestand - (DB E_CPSDA / E_HISTOT  - ebest)." )
	private String bestand;
	
	@ApiModelProperty(value =" DAK - (DB E_CPSDAT / E_HISTO  - DAKEWN)." )
	private String averageNetPrice;
	
	@ApiModelProperty(value =" SummeInEUR - (DB E_CPSDAT / E_HISTO  - SummeInEUR)." )
	private String summeInEUR;
	
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
	 * @return the beforePartType
	 */
	public String getBeforePartType() {
		return beforePartType;
	}

	/**
	 * @param beforePartType the beforePartType to set
	 */
	public void setBeforePartType(String beforePartType) {
		this.beforePartType = beforePartType;
	}

	/**
	 * @return the beforeMarketingCode
	 */
	public String getBeforeMarketingCode() {
		return beforeMarketingCode;
	}

	/**
	 * @param beforeMarketingCode the beforeMarketingCode to set
	 */
	public void setBeforeMarketingCode(String beforeMarketingCode) {
		this.beforeMarketingCode = beforeMarketingCode;
	}

	/**
	 * @return the beforePartBrand
	 */
	public String getBeforePartBrand() {
		return beforePartBrand;
	}

	/**
	 * @param beforePartBrand the beforePartBrand to set
	 */
	public void setBeforePartBrand(String beforePartBrand) {
		this.beforePartBrand = beforePartBrand;
	}

	/**
	 * @return the beforeAccountDesignation
	 */
	public String getBeforeAccountDesignation() {
		return beforeAccountDesignation;
	}

	/**
	 * @param beforeAccountDesignation the beforeAccountDesignation to set
	 */
	public void setBeforeAccountDesignation(String beforeAccountDesignation) {
		this.beforeAccountDesignation = beforeAccountDesignation;
	}

	/**
	 * @return the afterPartType
	 */
	public String getAfterPartType() {
		return afterPartType;
	}

	/**
	 * @param afterPartType the afterPartType to set
	 */
	public void setAfterPartType(String afterPartType) {
		this.afterPartType = afterPartType;
	}

	/**
	 * @return the afterMarketingCode
	 */
	public String getAfterMarketingCode() {
		return afterMarketingCode;
	}

	/**
	 * @param afterMarketingCode the afterMarketingCode to set
	 */
	public void setAfterMarketingCode(String afterMarketingCode) {
		this.afterMarketingCode = afterMarketingCode;
	}

	/**
	 * @return the afterPartBrand
	 */
	public String getAfterPartBrand() {
		return afterPartBrand;
	}

	/**
	 * @param afterPartBrand the afterPartBrand to set
	 */
	public void setAfterPartBrand(String afterPartBrand) {
		this.afterPartBrand = afterPartBrand;
	}

	/**
	 * @return the afterAccountDesignation
	 */
	public String getAfterAccountDesignation() {
		return afterAccountDesignation;
	}

	/**
	 * @param afterAccountDesignation the afterAccountDesignation to set
	 */
	public void setAfterAccountDesignation(String afterAccountDesignation) {
		this.afterAccountDesignation = afterAccountDesignation;
	}

	/**
	 * @return the bestand
	 */
	public String getBestand() {
		return bestand;
	}

	/**
	 * @param bestand the bestand to set
	 */
	public void setBestand(String bestand) {
		this.bestand = bestand;
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
	 * @return the beforePostingAccount
	 */
	public String getBeforePostingAccount() {
		return beforePostingAccount;
	}

	/**
	 * @param beforePostingAccount the beforePostingAccount to set
	 */
	public void setBeforePostingAccount(String beforePostingAccount) {
		this.beforePostingAccount = beforePostingAccount;
	}

	/**
	 * @return the afterPostingAccount
	 */
	public String getAfterPostingAccount() {
		return afterPostingAccount;
	}

	/**
	 * @param afterPostingAccount the afterPostingAccount to set
	 */
	public void setAfterPostingAccount(String afterPostingAccount) {
		this.afterPostingAccount = afterPostingAccount;
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
