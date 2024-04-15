package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about part catalog")
public class PartCatalog {

	@DBTable(columnName = "TLE_HERST", required = true)
	private String manufacturer;
	
	@DBTable(columnName = "TLE_TNR", required = true)
	private String partNumber;
	
	@DBTable(columnName = "TLE_BENENA", required = true)
	private String partName;
	
	@DBTable(columnName = "TLE_BLP", required = true)
	private BigDecimal purchasePrice;
	
	@DBTable(columnName = "TLE_RG", required = true)
	private String discountGroup;
	
	@DBTable(columnName = "TLE_MC", required = true)
	private String marketingCode;
	
	@DBTable(columnName = "TLE_PRKZ", required = true)
	private String priceMark;
	
	@DBTable(columnName = "TLE_TAXCDE", required = true)
	private String valueAddedTax;
	
	@DBTable(columnName = "TLE_SORTKL", required = true)
	private String commonPartWithUnimog;
	
	@DBTable(columnName = "TLE_TMARKE", required = true)
	private String partBrand;

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public String getDiscountGroup() {
		return discountGroup;
	}

	public void setDiscountGroup(String discountGroup) {
		this.discountGroup = discountGroup;
	}

	public String getMarketingCode() {
		return marketingCode;
	}

	public void setMarketingCode(String marketingCode) {
		this.marketingCode = marketingCode;
	}

	public String getPriceMark() {
		return priceMark;
	}

	public void setPriceMark(String priceMark) {
		this.priceMark = priceMark;
	}

	public String getValueAddedTax() {
		return valueAddedTax;
	}

	public void setValueAddedTax(String valueAddedTax) {
		this.valueAddedTax = valueAddedTax;
	}

	public String getCommonPartWithUnimog() {
		return commonPartWithUnimog;
	}

	public void setCommonPartWithUnimog(String commonPartWithUnimog) {
		this.commonPartWithUnimog = commonPartWithUnimog;
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
	
	
	
}
