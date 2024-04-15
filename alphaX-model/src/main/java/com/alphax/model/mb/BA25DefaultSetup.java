package com.alphax.model.mb;


import com.alphax.common.util.DBTable;

public class BA25DefaultSetup {

	@DBTable(columnName = "TA", required = true)
	private String partsIndikator;
	
	@DBTable(columnName = "LEIART", required = true)
	private String activityType;
	
	@DBTable(columnName = "TMARKE", required = true)
	private String oemBrand;
	
	@DBTable(columnName ="Leiferwerk", required = true)
	private String bodywork;
	
	@DBTable(columnName = "RG", required = true)
	private String discountGroup;
	
	@DBTable(columnName = "TNR", required = true)
	private String partNumber;
	
	@DBTable(columnName = "PKZ", required = true)
	private String priceTag;
	

	public String getPartsIndikator() {
		return partsIndikator;
	}

	public void setPartsIndikator(String partsIndikator) {
		this.partsIndikator = partsIndikator;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public String getOemBrand() {
		return oemBrand;
	}

	public void setOemBrand(String oemBrand) {
		this.oemBrand = oemBrand;
	}

	public String getBodywork() {
		return bodywork;
	}

	public void setBodywork(String bodywork) {
		this.bodywork = bodywork;
	}

	public String getDiscountGroup() {
		return discountGroup;
	}

	public void setDiscountGroup(String discountGroup) {
		this.discountGroup = discountGroup;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	/**
	 * @return the priceTag
	 */
	public String getPriceTag() {
		return priceTag;
	}

	/**
	 * @param priceTag the priceTag to set
	 */
	public void setPriceTag(String priceTag) {
		this.priceTag = priceTag;
	}
	
	
}
