package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

/**
 * @author A106744104
 *
 */
public class SearchParts2 {

	// Original Equipment Manufacturer (Hersteller - OEM).
	@DBTable(columnName ="herst", required = true)
	private String oem;
	
	// The part number (Teilenummer).
	@DBTable(columnName ="tnr", required = true)
	private String partNumber;
	
	// Description of the part (Bezeichnung).
	@DBTable(columnName ="benen", required = true)
	private String description;
	
	// Brand information for part (Teilemarke).
	@DBTable(columnName ="tmarke", required = true)
	private String oemBrand;
	
	@DBTable(columnName ="ROWNUMER", required = true)
	private Integer totalCount;
	
	@DBTable(columnName ="BLP", required = true)
	private BigDecimal listPrice;
	
	@DBTable(columnName ="RG", required = true)
	private String shoppingDiscountGroup;
	
	@DBTable(columnName ="MC", required = true)
	private String marketingCode;
	
	@DBTable(columnName ="PRKZ", required = true)
	private String priceIndicator;
	
	@DBTable(columnName ="TAXCDE", required = true)
	private String vatRegistrationNumber;
	
	@DBTable(columnName ="SORTKL", required = true)
	private String assortmentClass;
	
	@DBTable(columnName ="GEWICH", required = true)
	private BigDecimal weight;
	
	@DBTable(columnName ="PFAND", required = true)
	private BigDecimal deposit;
	
	@DBTable(columnName ="LABEL", required = true)
	private String partLabel;
	
	@DBTable(columnName ="LAENGE", required = true)
	private BigDecimal length;
	
	@DBTable(columnName ="BREITE", required = true)
	private BigDecimal width;
	
	@DBTable(columnName ="HOEHE", required = true)
	private BigDecimal height;
	
	@DBTable(columnName ="VERP1", required = true)
	private BigDecimal packagingUnit1;
	
	@DBTable(columnName ="VERP2", required = true)
	private BigDecimal packagingUnit2;
	
	@DBTable(columnName ="TKF_BLPZ", required = true)
	private BigDecimal oldListPrice;
	
	
	public SearchParts2() {
		
	}
	
	
	/**
	 * @return the oem
	 */
	public String getOem() {
		return oem;
	}
	
	/**
	 * @param oem the oem to set
	 */
	public void setOem(String oem) {
		this.oem = oem;
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
	 * @return the oemBrand
	 */
	public String getOemBrand() {
		return oemBrand;
	}


	/**
	 * @param oemBrand the oemBrand to set
	 */
	public void setOemBrand(String oemBrand) {
		this.oemBrand = oemBrand;
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


	public BigDecimal getListPrice() {
		return listPrice;
	}


	public void setListPrice(BigDecimal listPrice) {
		this.listPrice = listPrice;
	}


	public String getShoppingDiscountGroup() {
		return shoppingDiscountGroup;
	}


	public void setShoppingDiscountGroup(String shoppingDiscountGroup) {
		this.shoppingDiscountGroup = shoppingDiscountGroup;
	}


	public String getMarketingCode() {
		return marketingCode;
	}


	public void setMarketingCode(String marketingCode) {
		this.marketingCode = marketingCode;
	}


	public String getPriceIndicator() {
		return priceIndicator;
	}


	public void setPriceIndicator(String priceIndicator) {
		this.priceIndicator = priceIndicator;
	}


	public String getVatRegistrationNumber() {
		return vatRegistrationNumber;
	}


	public void setVatRegistrationNumber(String vatRegistrationNumber) {
		this.vatRegistrationNumber = vatRegistrationNumber;
	}


	public String getAssortmentClass() {
		return assortmentClass;
	}


	public void setAssortmentClass(String assortmentClass) {
		this.assortmentClass = assortmentClass;
	}


	/**
	 * @return the weight
	 */
	public BigDecimal getWeight() {
		return weight;
	}


	/**
	 * @param weight the weight to set
	 */
	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}


	/**
	 * @return the deposit
	 */
	public BigDecimal getDeposit() {
		return deposit;
	}


	/**
	 * @param deposit the deposit to set
	 */
	public void setDeposit(BigDecimal deposit) {
		this.deposit = deposit;
	}


	/**
	 * @return the partLabel
	 */
	public String getPartLabel() {
		return partLabel;
	}


	/**
	 * @param partLabel the partLabel to set
	 */
	public void setPartLabel(String partLabel) {
		this.partLabel = partLabel;
	}


	/**
	 * @return the length
	 */
	public BigDecimal getLength() {
		return length;
	}


	/**
	 * @param length the length to set
	 */
	public void setLength(BigDecimal length) {
		this.length = length;
	}


	/**
	 * @return the width
	 */
	public BigDecimal getWidth() {
		return width;
	}


	/**
	 * @param width the width to set
	 */
	public void setWidth(BigDecimal width) {
		this.width = width;
	}


	/**
	 * @return the height
	 */
	public BigDecimal getHeight() {
		return height;
	}


	/**
	 * @param height the height to set
	 */
	public void setHeight(BigDecimal height) {
		this.height = height;
	}


	/**
	 * @return the packagingUnit1
	 */
	public BigDecimal getPackagingUnit1() {
		return packagingUnit1;
	}


	/**
	 * @param packagingUnit1 the packagingUnit1 to set
	 */
	public void setPackagingUnit1(BigDecimal packagingUnit1) {
		this.packagingUnit1 = packagingUnit1;
	}


	/**
	 * @return the packagingUnit2
	 */
	public BigDecimal getPackagingUnit2() {
		return packagingUnit2;
	}


	/**
	 * @param packagingUnit2 the packagingUnit2 to set
	 */
	public void setPackagingUnit2(BigDecimal packagingUnit2) {
		this.packagingUnit2 = packagingUnit2;
	}


	/**
	 * @return the oldListPrice
	 */
	public BigDecimal getOldListPrice() {
		return oldListPrice;
	}


	/**
	 * @param oldListPrice the oldListPrice to set
	 */
	public void setOldListPrice(BigDecimal oldListPrice) {
		this.oldListPrice = oldListPrice;
	}
	
	
}