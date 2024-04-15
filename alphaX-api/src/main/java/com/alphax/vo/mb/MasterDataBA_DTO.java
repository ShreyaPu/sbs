package com.alphax.vo.mb;

import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about Stammdaten BA (MasterData BA)")
public class MasterDataBA_DTO {

	private String warehouseNumber;
	
	private String businessCases;
	
	private String partMovementDate;
	
	private String userName;
	
	private String manufacturer;
	
	private String partNumber;
	
	private String bookingAmount;
	
	private String bookingReceipt;
	
	private String customerDiscountForSERV;
	
	private String designation;
	
	private String backorder;
	
	private String orderNote;
	
	private String dispositionIndicator;
	
	private String licensePlate;
	
	private String lastOrderDate;
	
	private String lastMovementDate;
	
	private String lastAccessDate;
	
	private String expirationDate;
	
	private String netShoppingPrice;
	
	private String disposalFlatRate;
	
	private String disposalCosts;
	
	private String purchasePrice;
	
	private String weightInGrams;
	
	private String listPriceValidFrom;
	
	private String internalIndicator;
	
	private String discountForSERV;
	
	private String blockingIndicator;
	
	private String registrationRequired;
	
	private String markPartsReturn;
	
	private String labelEU;
	
	private String activityType;
	
	private String storageLocation;
	
	private String deliveryPlant_DAG;
	
	private String maximumStock;
	
	private String marketingCode;
	
	private String unitOfMeasure;
	
	private String minimumOrderQuantity;
	
	private String minimumStock;
	
	private String dispositionMode;
	
	private String vatCode;
	
	private String pledgeValue;
	
	private String priceIndicator;
	
	private String priceMark1;
	
	private String securityDistance;
	
	private String assortmentClass;
	
	private String partType;
	
	private String referenceCodeOEM;
	
	private String tradeNumber;
	
	private String partIdNumber;
	
	private String partNumberOEM;
	
	private String returnValue;
	
	private String partVolumeOld;
	
	private String partVolumeNew;
	
	private String packingUnit1;
	
	private String packingUnit2;
	
	private String countGroupInventory;
	
	private String futurePrice;
	
	private String receiptAmtCurrentMonth;
	
	private String accessValueCurrentMonth;
	
	private String hazardousMaterialMark;
	
	private String selfCalculatedNetPrice;
	
	private String discountGroup;
	
	private String partBrand;
	
	private String seasonalLicensePlate;
	
	private String predecessorPartNo;
	
	private String successorPartNo;
	
	private String sa;
	
	private String avgAcquisitionCost;
	
	private String lastDepartureDate;
	
	private String currentStock;
	
	private Boolean PurchasePriceTBEditable;
	
	private Boolean netPriceTBEditable;
	
	private Boolean isBAChange;
	
	private String labelHeadline1;
	
	private String labelHeadline2;
	
	private String labelHeadline3;
	
	private String labelHeadline4;
	
	private String labelHeadline5;
	
	private String labelHeadline6;
	
	private String labelHeadline7;

	private Boolean isLOPAChange;
	
	@ApiModelProperty(value = "Container size from - (DB O_OPARTTRANS - CONTAINER_SIZE_FROM)") 
	private String containerSizeFrom;
	
	public MasterDataBA_DTO() {
		
	}

	@ApiModelProperty(value = "Lager - WarehouseNumber")
	public String getWarehouseNumber() {
		return warehouseNumber;
	}

	public void setWarehouseNumber(String warehouseNumber) {
		this.warehouseNumber = warehouseNumber;
	}


	@ApiModelProperty(value = "BA Business Cases ")
	@Size(max=2)
	public String getBusinessCases() {
		return businessCases;
	}

	public void setBusinessCases(String businessCases) {
		this.businessCases = businessCases;
	}

	@ApiModelProperty(value = "Datum Teilebewegung (Part Movement Date) ")
	public String getPartMovementDate() {
		return partMovementDate;
	}

	public void setPartMovementDate(String partMovementDate) {
		this.partMovementDate = partMovementDate;
	}

	@ApiModelProperty(value = "Name des Buchers (User) ")
	@Size(max=10)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@ApiModelProperty(value = "Hersteller OEM (Manufacturer) ")
	@Size(max=4)
	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	@ApiModelProperty(value = "Teilenummer (Part Number)")
	@Size(max=30)
	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}
	
	
	@ApiModelProperty(value = "Buchungsmenge (Booking Amount)")
	@Size(max=5)
	public String getBookingAmount() {
		return bookingAmount;
	}

	public void setBookingAmount(String bookingAmount) {
		this.bookingAmount = bookingAmount;
	}


	@ApiModelProperty(value = "Buchungsbeleg (Booking Receipt)")
	@Size(max=10)
	public String getBookingReceipt() {
		return bookingReceipt;
	}


	public void setBookingReceipt(String bookingReceipt) {
		this.bookingReceipt = bookingReceipt;
	}

	
	@ApiModelProperty(value = "Endkunden Abschlag für SERV (Customer Discount For SERV)")
	@Size(max=5)
	public String getCustomerDiscountForSERV() {
		return customerDiscountForSERV;
	}


	public void setCustomerDiscountForSERV(String customerDiscountForSERV) {
		this.customerDiscountForSERV = customerDiscountForSERV;
	}
	

	@ApiModelProperty(value = "Benennung (Designation)")
	@Size(max=50)
	public String getDesignation() {
		return designation;
	}


	public void setDesignation(String designation) {
		this.designation = designation;
	}

	
	@ApiModelProperty(value = "Bestellausstand (Back Order)")
	@Size(max=9)
	public String getBackorder() {
		return backorder;
	}


	public void setBackorder(String backorder) {
		this.backorder = backorder;
	}


	@ApiModelProperty(value = "Bestellvermerk ( order Note)")
	@Size(max=17)
	public String getOrderNote() {
		return orderNote;
	}

	public void setOrderNote(String orderNote) {
		this.orderNote = orderNote;
	}
	

	@ApiModelProperty(value = "Dispositionskennzeichen (Disposition Indicator)")
	@Size(max=1)
	public String getDispositionIndicator() {
		return dispositionIndicator;
	}

	public void setDispositionIndicator(String dispositionIndicator) {
		this.dispositionIndicator = dispositionIndicator;
	}

	
	@ApiModelProperty(value = "Diesbstahl relevantes Kennzeichen (license Plate)")
	@Size(max=2)
	public String getLicensePlate() {
		return licensePlate;
	}


	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	
	@ApiModelProperty(value = "Datum letzte Bestellung (Last Order Date)")
	@Size(max=8)
	public String getLastOrderDate() {
		return lastOrderDate;
	}

	public void setLastOrderDate(String lastOrderDate) {
		this.lastOrderDate = lastOrderDate;
	}


	@ApiModelProperty(value = "Datum letzte Bewegung (Last Movement Date)")
	@Size(max=8)
	public String getLastMovementDate() {
		return lastMovementDate;
	}

	public void setLastMovementDate(String lastMovementDate) {
		this.lastMovementDate = lastMovementDate;
	}


	@ApiModelProperty(value = "Datum letzter Zugang (Last Access Date)")
	@Size(max=8)
	public String getLastAccessDate() {
		return lastAccessDate;
	}

	public void setLastAccessDate(String lastAccessDate) {
		this.lastAccessDate = lastAccessDate;
	}

	
	@ApiModelProperty(value = "Verfallsdatum (Expiration Date)")
	@Size(max=8)
	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
	
	
	@ApiModelProperty(value = "Einkaufsnettopreis (Net Shopping Price)")
	@Size(max=7)
	public String getNetShoppingPrice() {
		return netShoppingPrice;
	}

	public void setNetShoppingPrice(String netShoppingPrice) {
		this.netShoppingPrice = netShoppingPrice;
	}


	@ApiModelProperty(value = "Entsorgungspauschale (Disposal Flat Rate)")
	@Size(max=7)
	public String getDisposalFlatRate() {
		return disposalFlatRate;
	}

	public void setDisposalFlatRate(String disposalFlatRate) {
		this.disposalFlatRate = disposalFlatRate;
	}
	

	@ApiModelProperty(value = "Entsorgungskosten (Disposal Costs)")
	@Size(max=7)
	public String getDisposalCosts() {
		return disposalCosts;
	}

	public void setDisposalCosts(String disposalCosts) {
		this.disposalCosts = disposalCosts;
	}


	@ApiModelProperty(value = "Listenpreis OEM (Purchase Price)")
	@Size(max=7)
	public String getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(String purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	
	@ApiModelProperty(value = "Gewicht in Gramm (weight In Grams)")
	@Size(max=10)
	public String getWeightInGrams() {
		return weightInGrams;
	}

	public void setWeightInGrams(String weightInGrams) {
		this.weightInGrams = weightInGrams;
	}

	
	@ApiModelProperty(value = "Listenpreis gültig ab (List Price Valid From)")
	@Size(max=8)
	public String getListPriceValidFrom() {
		return listPriceValidFrom;
	}

	public void setListPriceValidFrom(String listPriceValidFrom) {
		this.listPriceValidFrom = listPriceValidFrom;
	}


	@ApiModelProperty(value = "Internes Verrechnungskennzeichen (Internal Indicator)")
	@Size(max=1)
	public String getInternalIndicator() {
		return internalIndicator;
	}

	public void setInternalIndicator(String internalIndicator) {
		this.internalIndicator = internalIndicator;
	}

	
	@ApiModelProperty(value = "Endkunden Zuschlag-/Abschlag für SERV (Discount For SERV)")
	@Size(max=1)
	public String getDiscountForSERV() {
		return discountForSERV;
	}

	public void setDiscountForSERV(String discountForSERV) {
		this.discountForSERV = discountForSERV;
	}

	
	@ApiModelProperty(value = "Sperrkennzeicchen (Blocking Indicator)")
	@Size(max=1)
	public String getBlockingIndicator() {
		return blockingIndicator;
	}


	public void setBlockingIndicator(String blockingIndicator) {
		this.blockingIndicator = blockingIndicator;
	}
	

	@ApiModelProperty(value = "Kennzeichen Einsendepflichtig (Registration Required)")
	@Size(max=1)
	public String getRegistrationRequired() {
		return registrationRequired;
	}

	public void setRegistrationRequired(String registrationRequired) {
		this.registrationRequired = registrationRequired;
	}


	@ApiModelProperty(value = "Keinzeichen Teilerückgabe (mark Parts Return)")
	@Size(max=1)
	public String getMarkPartsReturn() {
		return markPartsReturn;
	}

	public void setMarkPartsReturn(String markPartsReturn) {
		this.markPartsReturn = markPartsReturn;
	}
	

	@ApiModelProperty(value = "EU Labelkennzeichen vorhanden (label EU)")
	@Size(max=22)
	public String getLabelEU() {
		return labelEU;
	}

	public void setLabelEU(String labelEU) {
		this.labelEU = labelEU;
	}

	
	@ApiModelProperty(value = "Leistungsart (activity Type)")
	@Size(max=2)
	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}


	@ApiModelProperty(value = "Hauptlagerort (Storage Location)")
	@Size(max=8)
	public String getStorageLocation() {
		return storageLocation;
	}

	public void setStorageLocation(String storageLocation) {
		this.storageLocation = storageLocation;
	}


	@ApiModelProperty(value = "Lieferwerk (nur noch für DAG) (deliver Indicator)")
	@Size(max=2)
	public String getDeliveryPlant_DAG() {
		return deliveryPlant_DAG;
	}

	public void setDeliveryPlant_DAG(String deliveryPlant_DAG) {
		this.deliveryPlant_DAG = deliveryPlant_DAG;
	}

	
	@ApiModelProperty(value = "Maximaler Bestand (Maximum Stock)")
	@Size(max=10)
	public String getMaximumStock() {
		return maximumStock;
	}

	public void setMaximumStock(String maximumStock) {
		this.maximumStock = maximumStock;
	}


	@ApiModelProperty(value = "Marketingcode (Marketing Code)")
	@Size(max=6)
	public String getMarketingCode() {
		return marketingCode;
	}


	public void setMarketingCode(String marketingCode) {
		this.marketingCode = marketingCode;
	}

	
	@ApiModelProperty(value = "Mengeneinheit (Unit Of Measure)")
	@Size(max=1)
	public String getUnitOfMeasure() {
		return unitOfMeasure;
	}

	public void setUnitOfMeasure(String unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}


	@ApiModelProperty(value = "Mindestbestellmenge (Minimum Order Quantity)")
	@Size(max=9)
	public String getMinimumOrderQuantity() {
		return minimumOrderQuantity;
	}

	public void setMinimumOrderQuantity(String minimumOrderQuantity) {
		this.minimumOrderQuantity = minimumOrderQuantity;
	}
	

	@ApiModelProperty(value = "Mindestbestand (Minimum Stock)")
	@Size(max=9)
	public String getMinimumStock() {
		return minimumStock;
	}

	public void setMinimumStock(String minimumStock) {
		this.minimumStock = minimumStock;
	}


	@ApiModelProperty(value = "Dispositionsmodus (Disposition Mode)")
	@Size(max=1)
	public String getDispositionMode() {
		return dispositionMode;
	}

	public void setDispositionMode(String dispositionMode) {
		this.dispositionMode = dispositionMode;
	}


	@ApiModelProperty(value = "Mehrwertstuerkennzeichen (VAT Code)")
	@Size(max=2)
	public String getVatCode() {
		return vatCode;
	}


	public void setVatCode(String vatCode) {
		this.vatCode = vatCode;
	}


	@ApiModelProperty(value = "Pfandwert (pledge Value)")
	@Size(max=9)
	public String getPledgeValue() {
		return pledgeValue;
	}

	public void setPledgeValue(String pledgeValue) {
		this.pledgeValue = pledgeValue;
	}

	
	@ApiModelProperty(value = "Preissperrkennzeichen (Price Indicator)")
	@Size(max=1)
	public String getPriceIndicator() {
		return priceIndicator;
	}

	public void setPriceIndicator(String priceIndicator) {
		this.priceIndicator = priceIndicator;
	}
	

	@ApiModelProperty(value = "Sicherheitsbestand (Security Distance)")
	@Size(max=9)
	public String getSecurityDistance() {
		return securityDistance;
	}

	public void setSecurityDistance(String securityDistance) {
		this.securityDistance = securityDistance;
	}


	@ApiModelProperty(value = "Sortimentsklasse (Assortment Class)")
	@Size(max=2)
	public String getAssortmentClass() {
		return assortmentClass;
	}

	public void setAssortmentClass(String assortmentClass) {
		this.assortmentClass = assortmentClass;
	}

	
	@ApiModelProperty(value = "Teileart (Part Type)")
	@Size(max=2)
	public String getPartType() {
		return partType;
	}

	public void setPartType(String partType) {
		this.partType = partType;
	}

	
	@ApiModelProperty(value = "Hinweiscode OEM (Reference Code OEM)")
	@Size(max=3)
	public String getReferenceCodeOEM() {
		return referenceCodeOEM;
	}

	public void setReferenceCodeOEM(String referenceCodeOEM) {
		this.referenceCodeOEM = referenceCodeOEM;
	}


	@ApiModelProperty(value = "Handelswarennummer (Trade Number)")
	@Size(max=18)
	public String getTradeNumber() {
		return tradeNumber;
	}

	public void setTradeNumber(String tradeNumber) {
		this.tradeNumber = tradeNumber;
	}
	

	@ApiModelProperty(value = "Teileidentnummer (Part Id Number)")
	@Size(max=1)
	public String getPartIdNumber() {
		return partIdNumber;
	}

	public void setPartIdNumber(String partIdNumber) {
		this.partIdNumber = partIdNumber;
	}


	@ApiModelProperty(value = "Teilenummer OEM (Part Number OEM)")
	@Size(max=40)
	public String getPartNumberOEM() {
		return partNumberOEM;
	}

	public void setPartNumberOEM(String partNumberOEM) {
		this.partNumberOEM = partNumberOEM;
	}

	
	@ApiModelProperty(value = "Rückgabewert (Return Value)")
	@Size(max=11)
	public String getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(String returnValue) {
		this.returnValue = returnValue;
	}

	
	@ApiModelProperty(value = "Teilevolumen ccm alt (Part Volume Old)")
	@Size(max=10)
	public String getPartVolumeOld() {
		return partVolumeOld;
	}

	public void setPartVolumeOld(String partVolumeOld) {
		this.partVolumeOld = partVolumeOld;
	}


	@ApiModelProperty(value = "Teilevolumen ccm neu (Part Volume New)")
	@Size(max=15)
	public String getPartVolumeNew() {
		return partVolumeNew;
	}

	public void setPartVolumeNew(String partVolumeNew) {
		this.partVolumeNew = partVolumeNew;
	}

	
	@ApiModelProperty(value = "Verpackungseinheit 1 (Packing Unit 1)")
	@Size(max=5)
	public String getPackingUnit1() {
		return packingUnit1;
	}

	public void setPackingUnit1(String packingUnit1) {
		this.packingUnit1 = packingUnit1;
	}

	
	@ApiModelProperty(value = "Verpackungseinheit 2 (Packing Unit 2)")
	@Size(max=5)
	public String getPackingUnit2() {
		return packingUnit2;
	}

	public void setPackingUnit2(String packingUnit2) {
		this.packingUnit2 = packingUnit2;
	}

	
	@ApiModelProperty(value = "Zählgruppe für Inventur (Group Inventory)")
	@Size(max=3)
	public String getCountGroupInventory() {
		return countGroupInventory;
	}

	public void setCountGroupInventory(String countGroupInventory) {
		this.countGroupInventory = countGroupInventory;
	}


	@ApiModelProperty(value = "Zukünftiger Preis (Future Price)")
	@Size(max=7)
	public String getFuturePrice() {
		return futurePrice;
	}

	public void setFuturePrice(String futurePrice) {
		this.futurePrice = futurePrice;
	}


	@ApiModelProperty(value = "Zugangsmenge laufender Monat (Receipt Amount Current Month)")
	@Size(max=9)
	public String getReceiptAmtCurrentMonth() {
		return receiptAmtCurrentMonth;
	}

	public void setReceiptAmtCurrentMonth(String receiptAmtCurrentMonth) {
		this.receiptAmtCurrentMonth = receiptAmtCurrentMonth;
	}


	@ApiModelProperty(value = "Zugangswert laufender Monat (Access Value Current Month)")
	@Size(max=7)
	public String getAccessValueCurrentMonth() {
		return accessValueCurrentMonth;
	}

	public void setAccessValueCurrentMonth(String accessValueCurrentMonth) {
		this.accessValueCurrentMonth = accessValueCurrentMonth;
	}


	/**
	 * @return the hazardousMaterialMark
	 */
	@ApiModelProperty(value = "Gefahrenkennzeichen / REACH-Kennzeichen (Hazardous Mark)")
	public String getHazardousMaterialMark() {
		return hazardousMaterialMark;
	}

	/**
	 * @param hazardousMaterialMark the hazardousMaterialMark to set
	 */
	public void setHazardousMaterialMark(String hazardousMaterialMark) {
		this.hazardousMaterialMark = hazardousMaterialMark;
	}

	@ApiModelProperty(value = "Selbst kalkulierter Nettopreis (Self Calculated Net Price)")
	@Size(max=15)
	public String getSelfCalculatedNetPrice() {
		return selfCalculatedNetPrice;
	}

	public void setSelfCalculatedNetPrice(String selfCalculatedNetPrice) {
		this.selfCalculatedNetPrice = selfCalculatedNetPrice;
	}
	

	@ApiModelProperty(value = "Rabattgruppe ( Discount Group)")
	@Size(max=5)
	public String getDiscountGroup() {
		return discountGroup;
	}

	public void setDiscountGroup(String discountGroup) {
		this.discountGroup = discountGroup;
	}


	@ApiModelProperty(value = "Teilemarke ( Part Brand )")
	@Size(max=2)
	public String getPartBrand() {
		return partBrand;
	}

	public void setPartBrand(String partBrand) {
		this.partBrand = partBrand;
	}


	@ApiModelProperty(value = "Saisonkennzeichen ( Seasonal License Plate )")
	@Size(max=1)
	public String getSeasonalLicensePlate() {
		return seasonalLicensePlate;
	}

	public void setSeasonalLicensePlate(String seasonalLicensePlate) {
		this.seasonalLicensePlate = seasonalLicensePlate;
	}

	
	@ApiModelProperty(value = "Vorgängerteilenummer ( Predecessor Part No )")
	@Size(max=30)
	public String getPredecessorPartNo() {
		return predecessorPartNo;
	}

	public void setPredecessorPartNo(String predecessorPartNo) {
		this.predecessorPartNo = predecessorPartNo;
	}


	@ApiModelProperty(value = "Nachfolgerteilenummer ( Successor Part No )")
	@Size(max=30)
	public String getSuccessorPartNo() {
		return successorPartNo;
	}


	public void setSuccessorPartNo(String successorPartNo) {
		this.successorPartNo = successorPartNo;
	}

	
	@ApiModelProperty(value = "SA ( SA )")
	@Size(max=1)
	public String getSa() {
		return sa;
	}

	public void setSa(String sa) {
		this.sa = sa;
	}


	@ApiModelProperty(value = "Durchschnittliche Anschaffungskosten ( Avgerage Acquisition Cost )")
	@Size(max=15)
	public String getAvgAcquisitionCost() {
		return avgAcquisitionCost;
	}

	public void setAvgAcquisitionCost(String avgAcquisitionCost) {
		this.avgAcquisitionCost = avgAcquisitionCost;
	}


	@ApiModelProperty(value = "Letztes Abgangsdatum ( Last Departure Date )")
	@Size(max=8)
	public String getLastDepartureDate() {
		return lastDepartureDate;
	}

	public void setLastDepartureDate(String lastDepartureDate) {
		this.lastDepartureDate = lastDepartureDate;
	}


	public String getCurrentStock() {
		return currentStock;
	}


	public void setCurrentStock(String currentStock) {
		this.currentStock = currentStock;
	}


	public String getPriceMark1() {
		return priceMark1;
	}


	public void setPriceMark1(String priceMark1) {
		this.priceMark1 = priceMark1;
	}


	public Boolean getPurchasePriceTBEditable() {
		return PurchasePriceTBEditable;
	}


	public void setPurchasePriceTBEditable(Boolean purchasePriceTBEditable) {
		PurchasePriceTBEditable = purchasePriceTBEditable;
	}


	public Boolean getNetPriceTBEditable() {
		return netPriceTBEditable;
	}


	public void setNetPriceTBEditable(Boolean netPriceTBEditable) {
		this.netPriceTBEditable = netPriceTBEditable;
	}

	public Boolean getIsBAChange() {
		return isBAChange;
	}

	public void setIsBAChange(Boolean isBAChange) {
		this.isBAChange = isBAChange;
	}

	public String getLabelHeadline1() {
		return labelHeadline1;
	}

	public void setLabelHeadline1(String labelHeadline1) {
		this.labelHeadline1 = labelHeadline1;
	}

	public String getLabelHeadline2() {
		return labelHeadline2;
	}

	public void setLabelHeadline2(String labelHeadline2) {
		this.labelHeadline2 = labelHeadline2;
	}

	public String getLabelHeadline3() {
		return labelHeadline3;
	}

	public void setLabelHeadline3(String labelHeadline3) {
		this.labelHeadline3 = labelHeadline3;
	}

	public String getLabelHeadline4() {
		return labelHeadline4;
	}

	public void setLabelHeadline4(String labelHeadline4) {
		this.labelHeadline4 = labelHeadline4;
	}

	public String getLabelHeadline5() {
		return labelHeadline5;
	}

	public void setLabelHeadline5(String labelHeadline5) {
		this.labelHeadline5 = labelHeadline5;
	}

	/**
	 * @return the labelHeadline6
	 */
	public String getLabelHeadline6() {
		return labelHeadline6;
	}

	/**
	 * @param labelHeadline6 the labelHeadline6 to set
	 */
	public void setLabelHeadline6(String labelHeadline6) {
		this.labelHeadline6 = labelHeadline6;
	}

	/**
	 * @return the labelHeadline7
	 */
	public String getLabelHeadline7() {
		return labelHeadline7;
	}

	/**
	 * @param labelHeadline7 the labelHeadline7 to set
	 */
	public void setLabelHeadline7(String labelHeadline7) {
		this.labelHeadline7 = labelHeadline7;
	}

	/**
	 * @return the isLOPAChange
	 */
	public Boolean getIsLOPAChange() {
		return isLOPAChange;
	}

	/**
	 * @param isLOPAChange the isLOPAChange to set
	 */
	public void setIsLOPAChange(Boolean isLOPAChange) {
		this.isLOPAChange = isLOPAChange;
	}

	/**
	 * @return the containerSizeFrom
	 */
	public String getContainerSizeFrom() {
		return containerSizeFrom;
	}

	/**
	 * @param containerSizeFrom the containerSizeFrom to set
	 */
	public void setContainerSizeFrom(String containerSizeFrom) {
		this.containerSizeFrom = containerSizeFrom;
	}
	
	

}