package com.alphax.vo.mb;

import java.util.List;

import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about Zugänge BA(Accesses BA)")
public class AccessesBA_DTO {

	private String location;
	
	private String businessCases;
	
	private String movementDate;
	
	private String userName;
	
	private String manufacturer;
	
	private String supplierNumber;
	
	private String deliveryNoteNumber;
	
	private String purchaseOrderNumber;
	
	private String purchaseOrderItem;
	
	private String orderArt;
	
	private String partNumber;
	
	private String partName;
	
	private String bookingAmount;
	
	private String listPriceWithoutVAT;
	
	private String shoppingDiscountGroup;
	
	private String marketingCode;
	
	private String netShoppingPrice;
	
	private String selfCalculatedNetPrice;
	
	private String storageLocation;
	
	private String priceIndicator;
	
	private String vatRegistrationNumber;
	
	private String bookingProcess;
	
	private String customerGroup;
	
	private String previousBookingPrice;
	
	private String specialDiscount;
	
	private String assortmentClass;
	
	private String partBrand;
	
	private String deliverIndicator;
	
	private String partsIndikator;
	
	private String activityType;
	
	private String storageIndikator;
	
	private boolean isMarkeAvailable;
	
	private String discountGroupValue;
	
	private String discountGroupPercentageValue;
	
	private String currentStock;
	
	private String pendingOrders;
	
	private String averageNetPrice;
	
	private String discountGroup;
	
	private String oemBrand;
	
	private String lastPurchasePrice;
	
	private String netPrice;
	
	private String previousPurchasePrice;
	
	private String sortingFormatPartNumber;
	
	private String printingFormatPartNumber;
	
	private String company;
	
	private String agency;
	
	private String warehouseListForPartCreation;
	
	private List<LagerDetailsDTO> finalWarehouseListDetails;
	
	private List<BADeliveryNotesDTO> baDeliveryNotesList;	
	
	private String warehouseNo;
	
	private String deletionMark;
	
	private String calculationLock;
	
	
	@ApiModelProperty(value = "Firma/Filiale/Lager (Location) ")
	@Size(max=10)
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@ApiModelProperty(value = "BA Business Cases ")
	@Size(max=2)
	public String getBusinessCases() {
		return businessCases;
	}

	public void setBusinessCases(String businessCases) {
		this.businessCases = businessCases;
	}

	@ApiModelProperty(value = "Bewegungsdatum (Movement Date) ")
	public String getMovementDate() {
		return movementDate;
	}

	public void setMovementDate(String movementDate) {
		this.movementDate = movementDate;
	}

	@ApiModelProperty(value = "Name des Zählers (User) ")
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

	@ApiModelProperty(value = "Lieferantennummer (Supplier Number) ")
	@Size(max=5)
	public String getSupplierNumber() {
		return supplierNumber;
	}

	public void setSupplierNumber(String supplierNumber) {
		this.supplierNumber = supplierNumber;
	}

	@ApiModelProperty(value = "Lieferscheinnummer (Delivery Note Number) ")
	@Size(max=10)
	public String getDeliveryNoteNumber() {
		return deliveryNoteNumber;
	}

	public void setDeliveryNoteNumber(String deliveryNoteNumber) {
		this.deliveryNoteNumber = deliveryNoteNumber;
	}

	@ApiModelProperty(value = "Bestellauftrag Nummer (Purchase Order Number) ")
	@Size(max=4)
	public String getPurchaseOrderNumber() {
		return purchaseOrderNumber;
	}

	public void setPurchaseOrderNumber(String purchaseOrderNumber) {
		this.purchaseOrderNumber = purchaseOrderNumber;
	}

	@ApiModelProperty(value = "Bestellauftrag Position (Purchase Order Item) ")
	@Size(max=3)
	public String getPurchaseOrderItem() {
		return purchaseOrderItem;
	}

	public void setPurchaseOrderItem(String purchaseOrderItem) {
		this.purchaseOrderItem = purchaseOrderItem;
	}

	@ApiModelProperty(value = "Bestellauftrag Art (orderArt) ")
	@Size(max=2)
	public String getOrderArt() {
		return orderArt;
	}

	public void setOrderArt(String orderArt) {
		this.orderArt = orderArt;
	}

	@ApiModelProperty(value = "Teilenummer (Part Number) ")
	@Size(max=30)
	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	@ApiModelProperty(value = "Teilebenennung (Part Name) ")
	@Size(max=50)
	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	@ApiModelProperty(value = "Buchungsmenge (Booking Amount) ")
	@Size(max=7)
	public String getBookingAmount() {
		return bookingAmount;
	}

	public void setBookingAmount(String bookingAmount) {
		this.bookingAmount = bookingAmount;
	}

	@ApiModelProperty(value = "Listenpreis pro Stück (List Price) ")
	@Size(max=8)
	public String getListPriceWithoutVAT() {
		return listPriceWithoutVAT;
	}

	public void setListPriceWithoutVAT(String listPriceWithoutVAT) {
		this.listPriceWithoutVAT = listPriceWithoutVAT;
	}

	@ApiModelProperty(value = "Einkaufsrabattgruppe (Shopping Discount Group) ")
	@Size(max=5)
	public String getShoppingDiscountGroup() {
		return shoppingDiscountGroup;
	}

	public void setShoppingDiscountGroup(String shoppingDiscountGroup) {
		this.shoppingDiscountGroup = shoppingDiscountGroup;
	}

	@ApiModelProperty(value = "Marketingcode  (Marketing Code) ")
	@Size(max=6)
	public String getMarketingCode() {
		return marketingCode;
	}

	public void setMarketingCode(String marketingCode) {
		this.marketingCode = marketingCode;
	}

	@ApiModelProperty(value = "Einkaufsnettopreis  (Net Shopping Price) ")
	@Size(max=8)
	public String getNetShoppingPrice() {
		return netShoppingPrice;
	}

	public void setNetShoppingPrice(String netShoppingPrice) {
		this.netShoppingPrice = netShoppingPrice;
	}

	@ApiModelProperty(value = "Selbst kalkulierter Nettopreis  (Self Calculated Net Price) ")
	@Size(max=8)
	public String getSelfCalculatedNetPrice() {
		return selfCalculatedNetPrice;
	}

	public void setSelfCalculatedNetPrice(String selfCalculatedNetPrice) {
		this.selfCalculatedNetPrice = selfCalculatedNetPrice;
	}

	@ApiModelProperty(value = "Lagerort  (Storage Location) ")
	@Size(max=8)
	public String getStorageLocation() {
		return storageLocation;
	}

	public void setStorageLocation(String storageLocation) {
		this.storageLocation = storageLocation;
	}

	@ApiModelProperty(value = "Preiskennzeichen  (Price Indicator) ")
	@Size(max=1)
	public String getPriceIndicator() {
		return priceIndicator;
	}

	public void setPriceIndicator(String priceIndicator) {
		this.priceIndicator = priceIndicator;
	}

	@ApiModelProperty(value = "MwSt Kennzeichen alphaplus  (VAT Registration Number) ")
	@Size(max=2)
	public String getVatRegistrationNumber() {
		return vatRegistrationNumber;
	}

	public void setVatRegistrationNumber(String vatRegistrationNumber) {
		this.vatRegistrationNumber = vatRegistrationNumber;
	}

	@ApiModelProperty(value = "Buchungsvorgang  (Booking Process) ")
	@Size(max=1)
	public String getBookingProcess() {
		return bookingProcess;
	}

	public void setBookingProcess(String bookingProcess) {
		this.bookingProcess = bookingProcess;
	}

	@ApiModelProperty(value = "Abnehmergruppe  (Customer Group) ")
	@Size(max=1)
	public String getCustomerGroup() {
		return customerGroup;
	}

	public void setCustomerGroup(String customerGroup) {
		this.customerGroup = customerGroup;
	}

	@ApiModelProperty(value = "Listenpreis der ehemaligen Zubuchung  (Previous Booking Price) ")
	@Size(max=8)
	public String getPreviousBookingPrice() {
		return previousBookingPrice;
	}

	public void setPreviousBookingPrice(String previousBookingPrice) {
		this.previousBookingPrice = previousBookingPrice;
	}

	@ApiModelProperty(value = "Sonderrabatt  (Special Discount) ")
	@Size(max=6)
	public String getSpecialDiscount() {
		return specialDiscount;
	}

	public void setSpecialDiscount(String specialDiscount) {
		this.specialDiscount = specialDiscount;
	}

	@ApiModelProperty(value = "Sortimentsklasse  (Assortment Class) ")
	@Size(max=1)
	public String getAssortmentClass() {
		return assortmentClass;
	}

	public void setAssortmentClass(String assortmentClass) {
		this.assortmentClass = assortmentClass;
	}

	@ApiModelProperty(value ="Teilemarke - (DB E_ETSTAMM- 22-TMARKE)." )
	public String getPartBrand() {
		return partBrand;
	}

	public void setPartBrand(String partBrand) {
		this.partBrand = partBrand;
	}

	@ApiModelProperty(value ="Lieferwerk  - (DB E_ETSTAMM- 9-LIWERK)." )
	public String getDeliverIndicator() {
		return deliverIndicator;
	}

	public void setDeliverIndicator(String deliverIndicator) {
		this.deliverIndicator = deliverIndicator;
	}

	@ApiModelProperty(value ="Teileart  - (DB E_ETSTAMM- 5-TA)." )
	public String getPartsIndikator() {
		return partsIndikator;
	}

	public void setPartsIndikator(String partsIndikator) {
		this.partsIndikator = partsIndikator;
	}

	@ApiModelProperty(value ="Leistungsart  - (DB E_ETSTAMM- 6-LEIART)." )
	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	@ApiModelProperty(value ="Satzart  - (DB E_ETSTAMM- 4-SA)." )
	public String getStorageIndikator() {
		return storageIndikator;
	}

	public void setStorageIndikator(String storageIndikator) {
		this.storageIndikator = storageIndikator;
	}

	@ApiModelProperty(value ="Part Brand - (DB PMH_TMARKE - TMA_TMARKE)" )
	public boolean isMarkeAvailable() {
		return isMarkeAvailable;
	}

	public void setMarkeAvailable(boolean isMarkeAvailable) {
		this.isMarkeAvailable = isMarkeAvailable;
	}

	@ApiModelProperty(value ="Discount Group Value - (DB E_RAB - BUVORG)" )
	public String getDiscountGroupValue() {
		return discountGroupValue;
	}

	public void setDiscountGroupValue(String discountGroupValue) {
		this.discountGroupValue = discountGroupValue;
	}

	@ApiModelProperty(value ="Discount Group Percentage Value - (DB E_RAB - SATZ)" )
	public String getDiscountGroupPercentageValue() {
		return discountGroupPercentageValue;
	}

	public void setDiscountGroupPercentageValue(String discountGroupPercentageValue) {
		this.discountGroupPercentageValue = discountGroupPercentageValue;
	}

	@ApiModelProperty(value ="Aktueller Bestand - (DB E_ETSTAMM- 29-AKTBES)." )
	public String getCurrentStock() {
		return currentStock;
	}

	public void setCurrentStock(String currentStock) {
		this.currentStock = currentStock;
	}
	
	/**
	 * @return the pendingOrders
	 */
	public String getPendingOrders() {
		return pendingOrders;
	}

	/**
	 * @param pendingOrders the pendingOrders to set
	 */
	public void setPendingOrders(String pendingOrders) {
		this.pendingOrders = pendingOrders;
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
	 * @return the lastPurchasePrice
	 */
	public String getLastPurchasePrice() {
		return lastPurchasePrice;
	}

	/**
	 * @param lastPurchasePrice the lastPurchasePrice to set
	 */
	public void setLastPurchasePrice(String lastPurchasePrice) {
		this.lastPurchasePrice = lastPurchasePrice;
	}

	/**
	 * @return the netPrice
	 */
	public String getNetPrice() {
		return netPrice;
	}

	/**
	 * @param netPrice the netPrice to set
	 */
	public void setNetPrice(String netPrice) {
		this.netPrice = netPrice;
	}

	/**
	 * @return the previousPurchasePrice
	 */
	public String getPreviousPurchasePrice() {
		return previousPurchasePrice;
	}

	/**
	 * @param previousPurchasePrice the previousPurchasePrice to set
	 */
	public void setPreviousPurchasePrice(String previousPurchasePrice) {
		this.previousPurchasePrice = previousPurchasePrice;
	}

	/**
	 * @return the sortingFormatPartNumber
	 */
	public String getSortingFormatPartNumber() {
		return sortingFormatPartNumber;
	}

	/**
	 * @param sortingFormatPartNumber the sortingFormatPartNumber to set
	 */
	public void setSortingFormatPartNumber(String sortingFormatPartNumber) {
		this.sortingFormatPartNumber = sortingFormatPartNumber;
	}

	/**
	 * @return the printingFormatPartNumber
	 */
	public String getPrintingFormatPartNumber() {
		return printingFormatPartNumber;
	}

	/**
	 * @param printingFormatPartNumber the printingFormatPartNumber to set
	 */
	public void setPrintingFormatPartNumber(String printingFormatPartNumber) {
		this.printingFormatPartNumber = printingFormatPartNumber;
	}

	/**
	 * @return the company
	 */
	public String getCompany() {
		return company;
	}

	/**
	 * @param company the company to set
	 */
	public void setCompany(String company) {
		this.company = company;
	}

	/**
	 * @return the agency
	 */
	public String getAgency() {
		return agency;
	}

	/**
	 * @param agency the agency to set
	 */
	public void setAgency(String agency) {
		this.agency = agency;
	}

	public List<BADeliveryNotesDTO> getBaDeliveryNotesList() {
		return baDeliveryNotesList;
	}


	public void setBaDeliveryNotesList(List<BADeliveryNotesDTO> baDeliveryNotesList) {
		this.baDeliveryNotesList = baDeliveryNotesList;
	}

	/**
	 * @return the warehouseListForPartCreation
	 */
	public String getWarehouseListForPartCreation() {
		return warehouseListForPartCreation;
	}

	/**
	 * @param warehouseListForPartCreation the warehouseListForPartCreation to set
	 */
	public void setWarehouseListForPartCreation(String warehouseListForPartCreation) {
		this.warehouseListForPartCreation = warehouseListForPartCreation;
	}

	/**
	 * @return the finalWarehouseListDetails
	 */
	public List<LagerDetailsDTO> getFinalWarehouseListDetails() {
		return finalWarehouseListDetails;
	}

	/**
	 * @param finalWarehouseListDetails the finalWarehouseListDetails to set
	 */
	public void setFinalWarehouseListDetails(List<LagerDetailsDTO> finalWarehouseListDetails) {
		this.finalWarehouseListDetails = finalWarehouseListDetails;
	}

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
	 * @return the deletionMark
	 */
	public String getDeletionMark() {
		return deletionMark;
	}

	/**
	 * @param deletionMark the deletionMark to set
	 */
	public void setDeletionMark(String deletionMark) {
		this.deletionMark = deletionMark;
	}

	/**
	 * @return the calculationLock
	 */
	public String getCalculationLock() {
		return calculationLock;
	}

	/**
	 * @param calculationLock the calculationLock to set
	 */
	public void setCalculationLock(String calculationLock) {
		this.calculationLock = calculationLock;
	}
	
	
		
}
