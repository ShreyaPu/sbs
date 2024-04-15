package com.alphax.vo.mb;

import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author A106744104
 *
 */
@ApiModel(description = "This Object about Search Parts details")
public class SearchPartsDTO {

	private String oem;
	private String partNumber;
	private String warehouse;
	private String agency;
	private String partKind;
	private String description;
	private String stock;
	private String oemBrand;
	
	private String listPrice;
	private String shoppingDiscountGroup;
	private String marketingCode;
	private String priceIndicator;
	private String vatRegistrationNumber;
	private String assortmentClass;
	
	private String weight;
	private String deposit;
	private String partLabel;
	private String lengthWidthHeight;
	private String packagingUnit1;
	private String packagingUnit2;
	private String alphaXWarehouseName;
	
	private String price;		
	private String averageSales;	
	private String lastDisposalDate;
	private String storageLocation;
	private String storageIndikator;
	private boolean storageIndikatorFlag;
	private String partPriceWithSurcharge;
	private String rabTbl;
	private String discountGroup;
	

	public SearchPartsDTO() {
		
	}
	
	
	/**
	 * @return the oem
	 */
	@ApiModelProperty(value = "Original Equipment Manufacturer (Hersteller - OEM).")
	@Size(min=1)
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
	@ApiModelProperty(value = "The part number (Teilenummer).")
	@Size(min=1)
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
	 * @return the warehouse
	 */
	@ApiModelProperty(value = "Warehouse Information (Lagernummer / Lagerbezeichnung).")
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
	 * @return the agency
	 */
	@ApiModelProperty(value = "agency information (Filialnummer / Filialbezeichnung).")
	@Size(min=1)
	public String getAgency() {
		return agency;
	}
	
	/**
	 * @param agency the agency to set
	 */
	public void setAgency(String agency) {
		this.agency = agency;
	}
	
	/**
	 * @return the partKind
	 */
	@ApiModelProperty(value = "Kind of the part (Teileart).")
	public String getPartKind() {
		return partKind;
	}
	
	/**
	 * @param partKind the partKind to set
	 */
	public void setPartKind(String partKind) {
		this.partKind = partKind;
	}
	
	/**
	 * @return the description
	 */
	@ApiModelProperty(value = "Description of the part (Bezeichnung).")
	@Size(min=1)
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
	 * @return the stock
	 */
	@ApiModelProperty(value = "stock information for part (Bestand).")
	@Size(min=1)
	public String getStock() {
		return stock;
	}
	
	/**
	 * @param stock the stock to set
	 */
	public void setStock(String stock) {
		this.stock = stock;
	}
	
	/**
	 * @return the oemBrand
	 */
	@ApiModelProperty(value = "The brand of the part (Teilemarke).")
	@Size(min=1)
	public String getOemBrand() {
		return oemBrand;
	}
	
	/**
	 * @param oemBrand the oemBrand to set
	 */
	public void setOemBrand(String oemBrand) {
		this.oemBrand = oemBrand;
	}

	@ApiModelProperty(value = "List Price (Listenpreis pro Stück).")
	@Size(min=1)
	public String getListPrice() {
		return listPrice;
	}


	public void setListPrice(String listPrice) {
		this.listPrice = listPrice;
	}

	@ApiModelProperty(value = "Shopping Discount Group (Einkaufsrabattgruppe).")
	@Size(min=1)
	public String getShoppingDiscountGroup() {
		return shoppingDiscountGroup;
	}


	public void setShoppingDiscountGroup(String shoppingDiscountGroup) {
		this.shoppingDiscountGroup = shoppingDiscountGroup;
	}

	@ApiModelProperty(value = "Marketing Code (Marketingcode).")
	@Size(min=1)
	public String getMarketingCode() {
		return marketingCode;
	}


	public void setMarketingCode(String marketingCode) {
		this.marketingCode = marketingCode;
	}


	@ApiModelProperty(value = "Price Indicator (Preiskennzeichen).")
	public String getPriceIndicator() {
		return priceIndicator;
	}


	public void setPriceIndicator(String priceIndicator) {
		this.priceIndicator = priceIndicator;
	}

	@ApiModelProperty(value = "VAT RegistrationNumber (MwSt Kennzeichen).")
	@Size(min=1)
	public String getVatRegistrationNumber() {
		return vatRegistrationNumber;
	}


	public void setVatRegistrationNumber(String vatRegistrationNumber) {
		this.vatRegistrationNumber = vatRegistrationNumber;
	}

	@ApiModelProperty(value = "Assortment Class (Sortimentsklasse).")
	public String getAssortmentClass() {
		return assortmentClass;
	}


	public void setAssortmentClass(String assortmentClass) {
		this.assortmentClass = assortmentClass;
	}


	/**
	 * @return the weight
	 */
	@ApiModelProperty(value = "Weight (GEWICH).")
	public String getWeight() {
		return weight;
	}


	/**
	 * @param weight the weight to set
	 */
	public void setWeight(String weight) {
		this.weight = weight;
	}


	/**
	 * @return the deposit
	 */
	@ApiModelProperty(value = "Deposit (PFAND).")
	public String getDeposit() {
		return deposit;
	}


	/**
	 * @param deposit the deposit to set
	 */
	public void setDeposit(String deposit) {
		this.deposit = deposit;
	}


	/**
	 * @return the partLabel
	 */
	@ApiModelProperty(value = "part Label (LABEL).")
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
	 * @return the lengthWidthHeight
	 */
	@ApiModelProperty(value = "Length Width Height (LAENGE, BREITE, HOEHE).")
	public String getLengthWidthHeight() {
		return lengthWidthHeight;
	}


	/**
	 * @param lengthWidthHeight the lengthWidthHeight to set
	 */
	public void setLengthWidthHeight(String lengthWidthHeight) {
		this.lengthWidthHeight = lengthWidthHeight;
	}


	/**
	 * @return the packagingUnit1
	 */
	@ApiModelProperty(value = "Packaging Unit1 (VERP1).")
	public String getPackagingUnit1() {
		return packagingUnit1;
	}


	/**
	 * @param packagingUnit1 the packagingUnit1 to set
	 */
	public void setPackagingUnit1(String packagingUnit1) {
		this.packagingUnit1 = packagingUnit1;
	}


	/**
	 * @return the packagingUnit2
	 */
	@ApiModelProperty(value = "Packaging Unit2 (VERP2).")
	public String getPackagingUnit2() {
		return packagingUnit2;
	}


	/**
	 * @param packagingUnit2 the packagingUnit2 to set
	 */
	public void setPackagingUnit2(String packagingUnit2) {
		this.packagingUnit2 = packagingUnit2;
	}
	
	
	/**
	 * @return the alphaXWarehouseName
	 */
	public String getAlphaXWarehouseName() {
		return alphaXWarehouseName;
	}


	/**
	 * @param alphaXWarehouseName the alphaXWarehouseName to set
	 */
	public void setAlphaXWarehouseName(String alphaXWarehouseName) {
		this.alphaXWarehouseName = alphaXWarehouseName;
	}


	/**
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}


	/**
	 * @param price the price to set
	 */
	public void setPrice(String price) {
		this.price = price;
	}


	/**
	 * @return the averageSales
	 */
	public String getAverageSales() {
		return averageSales;
	}


	/**
	 * @param averageSales the averageSales to set
	 */
	public void setAverageSales(String averageSales) {
		this.averageSales = averageSales;
	}


	/**
	 * @return the lastDisposalDate
	 */
	public String getLastDisposalDate() {
		return lastDisposalDate;
	}


	/**
	 * @param lastDisposalDate the lastDisposalDate to set
	 */
	public void setLastDisposalDate(String lastDisposalDate) {
		this.lastDisposalDate = lastDisposalDate;
	}


	/**
	 * @return the storageLocation
	 */
	public String getStorageLocation() {
		return storageLocation;
	}
   /**
	 * @return the storageIndikator
	 */
	public String getStorageIndikator() {
		return storageIndikator;
	}


	/**
	 * @param storageLocation the storageLocation to set
	 */
	public void setStorageLocation(String storageLocation) {
		this.storageLocation = storageLocation;
	}

	/**
	 * * @param storageIndikator the storageIndikator to set
	 */
	public void setStorageIndikator(String storageIndikator) {
		this.storageIndikator = storageIndikator;
	}


	/**
	 * @return the storageIndikatorFlag
	 */
	public boolean isStorageIndikatorFlag() {
		return storageIndikatorFlag;
	}


	/**
	 * @param storageIndikatorFlag the storageIndikatorFlag to set
	 */
	public void setStorageIndikatorFlag(boolean storageIndikatorFlag) {
		this.storageIndikatorFlag = storageIndikatorFlag;
	}


	/**
	 * @return the partPriceWithSurcharge
	 */
	public String getPartPriceWithSurcharge() {
		return partPriceWithSurcharge;
	}


	/**
	 * @param partPriceWithSurcharge the partPriceWithSurcharge to set
	 */
	public void setPartPriceWithSurcharge(String partPriceWithSurcharge) {
		this.partPriceWithSurcharge = partPriceWithSurcharge;
	}

	
	/**
	 * @return the rabTbl
	 */
	public String getRabTbl() {
		return rabTbl;
	}


	/**
	 * @param rabTbl the rabTbl to set
	 */
	public void setRabTbl(String rabTbl) {
		this.rabTbl = rabTbl;
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
	
	
}