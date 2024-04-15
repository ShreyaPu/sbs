package com.alphax.vo.mb;

import java.util.List;

import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about Abgänge BA (Departures BA)")
public class DeparturesBA_DTO {

	private String warehouseNumber;
	
	private String businessCases;
	
	private String partMovementDate;
	
	private String userName;
	
	private String manufacturer;
	
	private String partNumber;
	
	private String partName;
	
	private String listPrice;
	
	private String bookingAmount;
	
	private String netShoppingPrice;
	
	private String customerGroup;
	
	private String supplierNumber;
	
	private String salesOrderNumber;
	
	private String salesOrderPosition;
	
	private String invoicingType;
	
	private String issuer;
	
	private String customerNumber;
	
	private String customerGroupAlphaplus;
	
	private String vehicleRegistrationNo;
	
	private String model;
	
	private String overstockwithDAK;
	
	private String pendingOrders;
	
	private String currentStock;
	
	private String averageNetPrice;
	
	private String purchaseOrderNumber;
	
	private Boolean isBAChange;
	
	private List<BADeliveryNotesDTO> baDeliveryNotesList;
	
	private String storageIndikator;
	
	private String discountGroup;
	
	private String oemBrand;
	
	private String marketingCode;
	
	private String lastPurchasePrice;
	
	private String deliverIndicator;
	
	private String partsIndikator;
	
	private String netPrice;
	
	private String previousPurchasePrice;
	
	private String oldPrice;
	
	private String sortingFormatPartNumber;
	
	private String printingFormatPartNumber;
	
	private String company;
	
	private String agency;
	
	private String activityType;
	
	private String movementDate;
	
	public DeparturesBA_DTO() {
		
	}

	
	@ApiModelProperty(value = "Lager (warehouseNumber) ")
	@Size(max=2)
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
	
	
	public String getPartName() {
		return partName;
	}


	public void setPartName(String partName) {
		this.partName = partName;
	}


	@ApiModelProperty(value = "Listenpreis pro Stück (List Price)")
	public String getListPrice() {
		return listPrice;
	}

	public void setListPrice(String listPrice) {
		this.listPrice = listPrice;
	}


	@ApiModelProperty(value = "Buchungsmenge (Booking Amount)")
	public String getBookingAmount() {
		return bookingAmount;
	}

	public void setBookingAmount(String bookingAmount) {
		this.bookingAmount = bookingAmount;
	}


	@ApiModelProperty(value = "Einkaufsnettopreis (Net Shopping Price)")
	public String getNetShoppingPrice() {
		return netShoppingPrice;
	}

	public void setNetShoppingPrice(String netShoppingPrice) {
		this.netShoppingPrice = netShoppingPrice;
	}


	@ApiModelProperty(value = "Abnehmergruppe (customer Group)")
	@Size(max=1)
	public String getCustomerGroup() {
		return customerGroup;
	}

	public void setCustomerGroup(String customerGroup) {
		this.customerGroup = customerGroup;
	}


	@ApiModelProperty(value = "Lieferantennummer (supplier Number)")
	@Size(max=5)
	public String getSupplierNumber() {
		return supplierNumber;
	}


	public void setSupplierNumber(String supplierNumber) {
		this.supplierNumber = supplierNumber;
	}


	@ApiModelProperty(value = "Kundenauftragsnummer (sales Order Number)")
	@Size(max=10)
	public String getSalesOrderNumber() {
		return salesOrderNumber;
	}


	public void setSalesOrderNumber(String salesOrderNumber) {
		this.salesOrderNumber = salesOrderNumber;
	}


	@ApiModelProperty(value = "Position Kundenauftrag (sales Order Position)")
	@Size(max=3)
	public String getSalesOrderPosition() {
		return salesOrderPosition;
	}


	public void setSalesOrderPosition(String salesOrderPosition) {
		this.salesOrderPosition = salesOrderPosition;
	}


	@ApiModelProperty(value = "Fakturierungsart (Invoicing Type)")
	@Size(max=2)
	public String getInvoicingType() {
		return invoicingType;
	}


	public void setInvoicingType(String invoicingType) {
		this.invoicingType = invoicingType;
	}


	@ApiModelProperty(value = "Ausgeberkürzel (Issuer)")
	@Size(max=2)
	public String getIssuer() {
		return issuer;
	}


	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}


	@ApiModelProperty(value = "Kundennummer (Customer Number)")
	@Size(max=8)
	public String getCustomerNumber() {
		return customerNumber;
	}


	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}


	@ApiModelProperty(value = "Kundengruppe alphaplus (Customer Group alphaplus)")
	@Size(max=2)
	public String getCustomerGroupAlphaplus() {
		return customerGroupAlphaplus;
	}


	public void setCustomerGroupAlphaplus(String customerGroupAlphaplus) {
		this.customerGroupAlphaplus = customerGroupAlphaplus;
	}


	@ApiModelProperty(value = "Fahrzeugkennzeichen (Vehicle Registration Number)")
	@Size(max=14)
	public String getVehicleRegistrationNo() {
		return vehicleRegistrationNo;
	}


	public void setVehicleRegistrationNo(String vehicleRegistrationNo) {
		this.vehicleRegistrationNo = vehicleRegistrationNo;
	}


	@ApiModelProperty(value = "Baumuster (Model)")
	@Size(max=6)
	public String getModel() {
		return model;
	}


	public void setModel(String model) {
		this.model = model;
	}


	@ApiModelProperty(value = "Überbestandsbuchung mit DAK (Overstock with DAK)")
	@Size(max=6)
	public String getOverstockwithDAK() {
		return overstockwithDAK;
	}

	
	public void setOverstockwithDAK(String overstockwithDAK) {
		this.overstockwithDAK = overstockwithDAK;
	}


	public String getPendingOrders() {
		return pendingOrders;
	}


	public void setPendingOrders(String pendingOrders) {
		this.pendingOrders = pendingOrders;
	}


	public String getCurrentStock() {
		return currentStock;
	}


	public void setCurrentStock(String currentStock) {
		this.currentStock = currentStock;
	}


	public String getAverageNetPrice() {
		return averageNetPrice;
	}


	public void setAverageNetPrice(String averageNetPrice) {
		this.averageNetPrice = averageNetPrice;
	}


	/**
	 * @return the purchaseOrderNumber
	 */
	public String getPurchaseOrderNumber() {
		return purchaseOrderNumber;
	}


	/**
	 * @param purchaseOrderNumber the purchaseOrderNumber to set
	 */
	public void setPurchaseOrderNumber(String purchaseOrderNumber) {
		this.purchaseOrderNumber = purchaseOrderNumber;
	}


	/**
	 * @return the isBAChange
	 */
	public Boolean getIsBAChange() {
		return isBAChange;
	}


	/**
	 * @param isBAChange the isBAChange to set
	 */
	public void setIsBAChange(Boolean isBAChange) {
		this.isBAChange = isBAChange;
	}


	public List<BADeliveryNotesDTO> getBaDeliveryNotesList() {
		return baDeliveryNotesList;
	}


	public void setBaDeliveryNotesList(List<BADeliveryNotesDTO> baDeliveryNotesList) {
		this.baDeliveryNotesList = baDeliveryNotesList;
	}


	/**
	 * @return the storageIndikator
	 */
	public String getStorageIndikator() {
		return storageIndikator;
	}


	/**
	 * @param storageIndikator the storageIndikator to set
	 */
	public void setStorageIndikator(String storageIndikator) {
		this.storageIndikator = storageIndikator;
	}

	/**
	 * @return the discountGroup
	 */
	@ApiModelProperty(value ="Rabattgruppe - (DB E_ETSTAMM- 21-RG)." )
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
	@ApiModelProperty(value ="Teilemarke - (DB E_ETSTAMM- 22-TMARKE)." )
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
	 * @return the marketingCode
	 */
	@ApiModelProperty(value ="Marketingcode - (DB E_ETSTAMM- 177-MC)." )
	public String getMarketingCode() {
		return marketingCode;
	}


	/**
	 * @param marketingCode the marketingCode to set
	 */
	public void setMarketingCode(String marketingCode) {
		this.marketingCode = marketingCode;
	}

	@ApiModelProperty(value ="Letzter Einkaufspreis  - (DB E_ETSTAMM- 15-LEKPR)." )
	public String getLastPurchasePrice() {
		return lastPurchasePrice;
	}


	public void setLastPurchasePrice(String lastPurchasePrice) {
		this.lastPurchasePrice = lastPurchasePrice;
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

	@ApiModelProperty(value ="Einkaufs Nettopreis  - (DB E_ETSTAMM- 12-EKNPR)." )
	public String getNetPrice() {
		return netPrice;
	}


	public void setNetPrice(String netPrice) {
		this.netPrice = netPrice;
	}
	
	@ApiModelProperty(value ="kalkulierter Nettopr. WE - (DB E_ETSTAMM- 19-NPREIS)." )
	public String getPreviousPurchasePrice() {
		return previousPurchasePrice;
	}

	public void setPreviousPurchasePrice(String previousPurchasePrice) {
		this.previousPurchasePrice = previousPurchasePrice;
	}

	@ApiModelProperty(value ="Old price  - (DB E_ETSTAMM- EPREIS)." )
	public String getOldPrice() {
		return oldPrice;
	}


	public void setOldPrice(String oldPrice) {
		this.oldPrice = oldPrice;
	}

	@ApiModelProperty(value ="Teilenummer Sort-Format  - (DB E_ETSTAMM- 178-TNRS)." )
	public String getSortingFormatPartNumber() {
		return sortingFormatPartNumber;
	}


	public void setSortingFormatPartNumber(String sortingFormatPartNumber) {
		this.sortingFormatPartNumber = sortingFormatPartNumber;
	}

	@ApiModelProperty(value ="Teilenummer Druckformat  - (DB E_ETSTAMM- 179-TNRD)." )
	public String getPrintingFormatPartNumber() {
		return printingFormatPartNumber;
	}


	public void setPrintingFormatPartNumber(String printingFormatPartNumber) {
		this.printingFormatPartNumber = printingFormatPartNumber;
	}

	@ApiModelProperty(value ="Firma - (DB E_ETSTAMM- 208-FIRMA)." )
	public String getCompany() {
		return company;
	}


	public void setCompany(String company) {
		this.company = company;
	}

	@ApiModelProperty(value ="Filiale - (DB E_ETSTAMM- 209-FILIAL)." )
	public String getAgency() {
		return agency;
	}


	public void setAgency(String agency) {
		this.agency = agency;
	}


	/**
	 * @return the activityType
	 */
	public String getActivityType() {
		return activityType;
	}


	/**
	 * @param activityType the activityType to set
	 */
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}


	/**
	 * @return the movementDate
	 */
	public String getMovementDate() {
		return movementDate;
	}


	/**
	 * @param movementDate the movementDate to set
	 */
	public void setMovementDate(String movementDate) {
		this.movementDate = movementDate;
	}

	
	
}