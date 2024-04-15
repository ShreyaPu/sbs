package com.alphax.vo.mb;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about BA 25.")
public class BusinessCases25DTO {

	
	private String warehouseNumber;
	
	private String businessCases;
	
	private String partMovementDate;
	
	private String userName;
	
	private String manufacturer;
	
	private String partNumber;
	
	private String partName;
	
	private String bookingAmount;
	
	private String netShoppingPrice;
	
	private String supplierNumber;
	
	private String deliveryNoteNo;
	
	private String salesOrderPosition;
	
	private String customerGroup;
	
	private String listPrice;
	
	private String currentStock;
	
	private String averageNetPrice;
	
	private String purchaseOrderNumber;
	
	private String pendingOrders;
	
	private String deliveryNoteAudit;
	
	private String invoiceAudit;
	
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
	
	private String specialDiscount;
	
	private String inputListPrice;
	
	private String activityType;
	
	private String movementDate;
	@ApiModelProperty(value = "Lager (warehouseNumber) ")
	@NotBlank
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
	@NotBlank
	public String getPartMovementDate() {
		return partMovementDate;
	}

	public void setPartMovementDate(String partMovementDate) {
		this.partMovementDate = partMovementDate;
	}

	@ApiModelProperty(value = "Name des Buchers (User) ")
	@NotBlank
	@Size(max=10)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@ApiModelProperty(value = "Hersteller OEM (Manufacturer) ")
	@NotBlank
	@Size(max=4)
	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	@ApiModelProperty(value = "Teilenummer (Part Number)")
	@NotBlank
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

	@ApiModelProperty(value = "Gebuchte Menge (Booking Amount)")
	@NotBlank
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

	@ApiModelProperty(value = "Lieferantennummer (supplier Number)")
	@NotBlank
	@Size(max=5)
	public String getSupplierNumber() {
		return supplierNumber;
	}


	public void setSupplierNumber(String supplierNumber) {
		this.supplierNumber = supplierNumber;
	}


	@ApiModelProperty(value = "Lieferschein (sales Order Number)")
	@NotBlank
	@Size(max=10)
	public String getDeliveryNoteNo() {
		return deliveryNoteNo;
	}


	public void setDeliveryNoteNo(String deliveryNoteNo) {
		this.deliveryNoteNo = deliveryNoteNo;
	}


	@ApiModelProperty(value = "Position (sales Order Position)")
	@Size(max=3)
	public String getSalesOrderPosition() {
		return salesOrderPosition;
	}


	public void setSalesOrderPosition(String salesOrderPosition) {
		this.salesOrderPosition = salesOrderPosition;
	}

	/**
	 * @return the listPrice
	 */
	@NotBlank
	@ApiModelProperty(value = "Listenpreis ohne MwSt.  ( %% - DB E_ETSTAMM - EPR).")
	public String getListPrice() {
		return listPrice;
	}

	/**
	 * @param listPrice the listPrice to set
	 */
	public void setListPrice(String listPrice) {
		this.listPrice = listPrice;
	}

	/**
	 * @return the currentStock
	 */
	@NotBlank
	@ApiModelProperty(value = "Aktueller Bestand  ( %% - DB E_ETSTAMM - AKTBES).")
	public String getCurrentStock() {
		return currentStock;
	}

	/**
	 * @param currentStock the currentStock to set
	 */
	public void setCurrentStock(String currentStock) {
		this.currentStock = currentStock;
	}

	/**
	 * @return the averageNetPrice
	 */
	@NotBlank
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
	 * @return the purchaseOrderNumber (Auftrag)
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
	 * @return the deliveryNoteAudit
	 */
	@ApiModelProperty(value = "Delivery Note Audit  ( %% - DB E_BSNLFS - PRUEFUNG).")
	public String getDeliveryNoteAudit() {
		return deliveryNoteAudit;
	}

	/**
	 * @param deliveryNoteAudit the deliveryNoteAudit to set
	 */
	public void setDeliveryNoteAudit(String deliveryNoteAudit) {
		this.deliveryNoteAudit = deliveryNoteAudit;
	}

	/**
	 * @return the invoiceAudit
	 */
	@ApiModelProperty(value = "Invoice Audit  ( %% - DB E_BSNLFS - ERLEDIGT).")
	public String getInvoiceAudit() {
		return invoiceAudit;
	}

	/**
	 * @param invoiceAudit the invoiceAudit to set
	 */
	public void setInvoiceAudit(String invoiceAudit) {
		this.invoiceAudit = invoiceAudit;
	}
	
	@ApiModelProperty(value = "Abnehmergruppe (customer Group)")
	@Size(max=1)
	public String getCustomerGroup() {
		return customerGroup;
	}

	public void setCustomerGroup(String customerGroup) {
		this.customerGroup = customerGroup;
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
	 * @return the specialDiscount
	 */
	@ApiModelProperty(value =" SonderRabatt - (DB E_ETSTAMM- RABPRZ)." )
	public String getSpecialDiscount() {
		return specialDiscount;
	}

	/**
	 * @param specialDiscount the specialDiscount to set
	 */
	public void setSpecialDiscount(String specialDiscount) {
		this.specialDiscount = specialDiscount;
	}

	
	/**
	 * @return the inputListPrice
	 */
	@NotBlank
	@ApiModelProperty(value = "Listenpreis ohne MwSt. Input from UI ( %% - DB E_ETSTAMM - input EPR).")
	public String getInputListPrice() {
		return inputListPrice;
	}
	

	/**
	 * @param inputListPrice the inputListPrice to set
	 */
	public void setInputListPrice(String inputListPrice) {
		this.inputListPrice = inputListPrice;
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