package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about Report Selections Details")
public class ReportSelections {

	@DBTable(columnName = "HERST", required = true)
	private String manufacturer;

	@DBTable(columnName = "TNR", required = true)
	private String partNumber;

	@DBTable(columnName = "AP_Lager", required = true)
	private BigDecimal alphaPlusWarehouseId;
	
	@DBTable(columnName = "AX_Lager", required = true)
	private String alphaxWarehouseName;

	@DBTable(columnName = "SA", required = true)
	private BigDecimal storageIndikator;

	@DBTable(columnName = "DRTKZ", required = true)
	private String theftRelevantLicensePlate;
	
	@DBTable(columnName = "GFKZ", required = true)
	private String reachLabel;
	
	@DBTable(columnName = "BENEN", required = true)
	private String name;

	@DBTable(columnName = "AKTBES", required = true)
	private BigDecimal currentStock;
	
	@DBTable(columnName ="ROWNUMER", required = true)
	private Integer totalCount;
	
	@DBTable(columnName ="PARTEXPDATE", required = true)
	private String partExpDate;
	
	//Extra fields for reports
	
	public String getPartExpDate() {
		return partExpDate;
	}

	public void setPartExpDate(String partExpDate) {
		this.partExpDate = partExpDate;
	}

	@DBTable(columnName = "AP_Firma", required = true)
	private BigDecimal alphaPlusCompanyId;
	
	@DBTable(columnName = "AX_Firma", required = true)
	private String alphaxCompanyName;
	
	@DBTable(columnName = "AP_Filiale", required = true)
	private BigDecimal alphaPlusAgencyId;
	
	@DBTable(columnName = "AX_Filiale", required = true)
	private String alphaxAgencyName;
	
	@DBTable(columnName = "TMARKE", required = true)
	private String partBrand;
	
	@DBTable(columnName = "BESAUS", required = true)
	private BigDecimal pendingOrders;
	
	@DBTable(columnName = "LOPA", required = true)
	private String storageLocation;
	
	@DBTable(columnName = "MC", required = true)
	private String marketingCode;
	
	@DBTable(columnName = "TA", required = true)
	private BigDecimal partsIndikator;
	
	@DBTable(columnName = "LEIART", required = true)
	private BigDecimal activityType;
	
	@DBTable(columnName = "RG", required = true)
	private String discountGroup;
	
	@DBTable(columnName = "EPR", required = true)
	private BigDecimal purchasePrice;
	
	@DBTable(columnName = "MEHRWERTSTEUER", required = true)
	private String valueAddedTax;
	
	@DBTable(columnName = "DAK", required = true)
	private BigDecimal averageNetPrice;
	
	@DBTable(columnName = "LEKPR", required = true)
	private BigDecimal lastPurchasePrice;
	
	@DBTable(columnName = "NPREIS", required = true)
	private BigDecimal previousPurchasePrice;
	
	@DBTable(columnName = "TRUWER", required = true)
	private BigDecimal returnValue;
	
	@DBTable(columnName = "ZPREIS", required = true)
	private BigDecimal futurePurchasePrice;
	
	@DBTable(columnName = "GPRDAT", required = true)
	private BigDecimal futurePriceFromDate;
	
	@DBTable(columnName = "DTLABG", required = true)
	private BigDecimal lastDisposalDate;
	
	@DBTable(columnName = "DTLZUG", required = true)
	private BigDecimal lastReceiptDate;
	
	@DBTable(columnName = "DTLBEW", required = true)
	private BigDecimal lastMovementDate;
	
	@DBTable(columnName = "IVZME", required = true)
	private BigDecimal currentInventoryCount;
	
	@DBTable(columnName = "VKSVJS", required = true)
	private BigDecimal salesStockLastYear;
	
	@DBTable(columnName = "VKLFJS", required = true)
	private BigDecimal currentYearSalesStock;
	
	@DBTable(columnName = "PRKZ", required = true)
	private String priceMark;
	
	@DBTable(columnName = "TNRV", required = true)
	private String predecessorPartNumber;
	
	@DBTable(columnName = "TNRN", required = true)
	private String successorPartNumber;
	
	@DBTable(columnName = "ZAEGRU", required = true)
	private String inventoryGroup;
	
	@DBTable(columnName = "Durchschnitt", required = true)
	private String average;
	
	@DBTable(columnName = "PARTKIND_ID", required = true)
	private BigDecimal partKindId;
	
	@DBTable(columnName = "KINDNAME", required = true)
	private String partType;
	
	@DBTable(columnName = "FROM_CUR", required = true)
	private BigDecimal fromCurrency;
	
	@DBTable(columnName = "TO_CUR", required = true)
	private BigDecimal toCurrency;
	
	@DBTable(columnName = "LAGER", required = true)
	private String businessCaseWarehouse;
	
	@DBTable(columnName = "DATE", required = true)
	private String businessCaseDate;
	
	@DBTable(columnName = "TEILENUMMER", required = true)
	private String businessCasePartNo;
	
	@DBTable(columnName = "BELEGNUMMER", required = true)
	private String businessCaseDocumentNo;
	
	@DBTable(columnName = "DAK_ALT", required = true)
	private String dakAlt;
	
	@DBTable(columnName = "DAK_NEU", required = true)
	private String dakNeu;
	
	@DBTable(columnName = "BESTAND_ALT", required = true)
	private String bestandAlt;
	
	@DBTable(columnName = "BESTAND_NEU", required = true)
	private String bestandNeu;
	
	@DBTable(columnName = "HERKUNFT", required = true)
	private String businessCaseOrigin;
	
	@DBTable(columnName = "Zahldifferenz", required = true)
	private String countingDifference;
	
	@DBTable(columnName = "BUCHUNGSMENGE", required = true)
	private String bookingAmount;
	
	//Varibles for Movement
	
	@DBTable(columnName = "BART", required = true)
	private String movementType;
	
	@DBTable(columnName = "VORGANGSART_TEXT", required = true)
	private String movementTypeName;
	
	@DBTable(columnName = "MARKEN", required = true)
	private String brand;
	
	@DBTable(columnName = "POSNR", required = true)
	private String positionNo;
	
	@DBTable(columnName = "HHMMSS", required = true)
	private String time;
	
	@DBTable(columnName = "WS_ID", required = true)
	private String WS;
	
	@DBTable(columnName = "WSUSER", required = true)
	private String users;
	
	@DBTable(columnName = "KDNR", required = true)
	private String customerNo;

	@DBTable(columnName = "MCODE", required = true)
	private String mktgCodeForMovement;

	@DBTable(columnName = "BSN_LC", required = true)
	private String supplierNo;
	
	@DBTable(columnName = "T1", required = true)
	private String t1Value;
	
	@DBTable(columnName = "T2", required = true)
	private String t2Value;
	
	@DBTable(columnName = "T3", required = true)
	private String t3Value;
	
	@DBTable(columnName = "BA", required = true)
	private BigDecimal businessCaseKey;
	
	@DBTable(columnName = "DESCRIPTION", required = true)
	private String businessCaseValue;
	
	@DBTable(columnName = "RESULT", required = true)
	private String searchResult;
	
	@DBTable(columnName = "MARKEV", required = true)
	private String brandAlt;
	
	@DBTable(columnName = "TA_EWV", required = true)
	private String ta_Alt;
	
	@DBTable(columnName = "TA_EWN", required = true)
	private String ta_Neu;	
	
	@DBTable(columnName = "LEIARTV", required = true)
	private BigDecimal la_Alt;	
	
	@DBTable(columnName = "LEIARTN", required = true)
	private BigDecimal la_Neu;	
	
	@DBTable(columnName = "MCODEV", required = true)
	private String marketingCode_Alt;	
	
	@DBTable(columnName = "BUK_KONTOV", required = true)
	private BigDecimal konto_Alt;	
	
	@DBTable(columnName = "BUK_KONTON", required = true)
	private BigDecimal konto_Neu;	
	
	@DBTable(columnName = "EPRV", required = true)
	private BigDecimal listPrice_Alt;	
	
	@DBTable(columnName = "EPRN", required = true)
	private BigDecimal listPrice_Neu;	
	
	@DBTable(columnName = "NPREIV", required = true)
	private BigDecimal netPrice_Alt;	
	
	@DBTable(columnName = "NPREIN", required = true)
	private BigDecimal netPrice_Neu;	
	
	@DBTable(columnName = "DAKEWN", required = true)
	private String dak_Alt;	
	
	@DBTable(columnName = "DAKEWV", required = true)
	private String dak_Neu;	
	
	@DBTable(columnName = "NT_EWV", required = true)
	private String fester_Alt;	
	
	@DBTable(columnName = "NT_EWN", required = true)
	private String fester_Neu;	
	
	@DBTable(columnName = "VCODE", required = true)
	private String processCode;
		
	@DBTable(columnName = "TNRVV", required = true)
	private String predecessor_Alt;
		
	@DBTable(columnName = "TNRVN", required = true)
	private String predecessor_Neu;	
	
	@DBTable(columnName = "TNRNV", required = true)
	private String successor_Alt;	
	
	@DBTable(columnName = "TNRNN", required = true)
	private String successor_Neu;	
	
	@DBTable(columnName = "LIENRV", required = true)
	private String liferant_Alt;	
	
	@DBTable(columnName = "LIENRN", required = true)
	private String liferant_Neu;	
	
	@DBTable(columnName = "LABELV", required = true)
	private String label_Alt;	
	
	@DBTable(columnName = "LABELN", required = true)
	private String label_Neu;	
	
	@DBTable(columnName = "NW1_EW", required = true)
	private String sa_Alt;	
	
	@DBTable(columnName = "NW2_EW", required = true)
	private String sa_Neu;
	
	@DBTable(columnName = "MengeVorBA67", required = true)
	private Double mengeVorBA67;
	
	@DBTable(columnName = "MengeNachBA_1_2_6_25", required = true)
	private Double mengeNachBA;
	
	@DBTable(columnName = "Listenpreis_Alt", required = true)
	private Double listenpreisAlt;
	
	@DBTable(columnName = "Listenpreis_Neu", required = true)
	private Double listenpreisNeu;
	
	@DBTable(columnName = "RABGRV", required = true)
	private String rb_Alt;
	
	@DBTable(columnName = "RABGRN", required = true)
	private String rb_New;
	
	
	public String getSearchResult() {
		return searchResult;
	}

	public void setSearchResult(String searchResult) {
		this.searchResult = searchResult;
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
	 * @return the storageIndikator
	 */
	public BigDecimal getStorageIndikator() {
		return storageIndikator;
	}

	/**
	 * @param storageIndikator the storageIndikator to set
	 */
	public void setStorageIndikator(BigDecimal storageIndikator) {
		this.storageIndikator = storageIndikator;
	}

	/**
	 * @return the theftRelevantLicensePlate
	 */
	public String getTheftRelevantLicensePlate() {
		return theftRelevantLicensePlate;
	}

	/**
	 * @param theftRelevantLicensePlate the theftRelevantLicensePlate to set
	 */
	public void setTheftRelevantLicensePlate(String theftRelevantLicensePlate) {
		this.theftRelevantLicensePlate = theftRelevantLicensePlate;
	}

	/**
	 * @return the reachLabel
	 */
	public String getReachLabel() {
		return reachLabel;
	}

	/**
	 * @param reachLabel the reachLabel to set
	 */
	public void setReachLabel(String reachLabel) {
		this.reachLabel = reachLabel;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the currentStock
	 */
	public BigDecimal getCurrentStock() {
		return currentStock;
	}

	/**
	 * @param currentStock the currentStock to set
	 */
	public void setCurrentStock(BigDecimal currentStock) {
		this.currentStock = currentStock;
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

	/**
	 * @return the alphaPlusCompanyId
	 */
	public BigDecimal getAlphaPlusCompanyId() {
		return alphaPlusCompanyId;
	}

	/**
	 * @param alphaPlusCompanyId the alphaPlusCompanyId to set
	 */
	public void setAlphaPlusCompanyId(BigDecimal alphaPlusCompanyId) {
		this.alphaPlusCompanyId = alphaPlusCompanyId;
	}

	/**
	 * @return the alphaxCompanyName
	 */
	public String getAlphaxCompanyName() {
		return alphaxCompanyName;
	}

	/**
	 * @param alphaxCompanyName the alphaxCompanyName to set
	 */
	public void setAlphaxCompanyName(String alphaxCompanyName) {
		this.alphaxCompanyName = alphaxCompanyName;
	}

	/**
	 * @return the alphaPlusAgencyId
	 */
	public BigDecimal getAlphaPlusAgencyId() {
		return alphaPlusAgencyId;
	}

	/**
	 * @param alphaPlusAgencyId the alphaPlusAgencyId to set
	 */
	public void setAlphaPlusAgencyId(BigDecimal alphaPlusAgencyId) {
		this.alphaPlusAgencyId = alphaPlusAgencyId;
	}

	/**
	 * @return the alphaxAgencyName
	 */
	public String getAlphaxAgencyName() {
		return alphaxAgencyName;
	}

	/**
	 * @param alphaxAgencyName the alphaxAgencyName to set
	 */
	public void setAlphaxAgencyName(String alphaxAgencyName) {
		this.alphaxAgencyName = alphaxAgencyName;
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
	 * @return the pendingOrders
	 */
	public BigDecimal getPendingOrders() {
		return pendingOrders;
	}

	/**
	 * @param pendingOrders the pendingOrders to set
	 */
	public void setPendingOrders(BigDecimal pendingOrders) {
		this.pendingOrders = pendingOrders;
	}

	/**
	 * @return the storageLocation
	 */
	public String getStorageLocation() {
		return storageLocation;
	}

	/**
	 * @param storageLocation the storageLocation to set
	 */
	public void setStorageLocation(String storageLocation) {
		this.storageLocation = storageLocation;
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
	 * @return the partsIndikator
	 */
	public BigDecimal getPartsIndikator() {
		return partsIndikator;
	}

	/**
	 * @param partsIndikator the partsIndikator to set
	 */
	public void setPartsIndikator(BigDecimal partsIndikator) {
		this.partsIndikator = partsIndikator;
	}

	/**
	 * @return the activityType
	 */
	public BigDecimal getActivityType() {
		return activityType;
	}

	/**
	 * @param activityType the activityType to set
	 */
	public void setActivityType(BigDecimal activityType) {
		this.activityType = activityType;
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
	 * @return the purchasePrice
	 */
	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}

	/**
	 * @param purchasePrice the purchasePrice to set
	 */
	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	/**
	 * @return the valueAddedTax
	 */
	public String getValueAddedTax() {
		return valueAddedTax;
	}

	/**
	 * @param valueAddedTax the valueAddedTax to set
	 */
	public void setValueAddedTax(String valueAddedTax) {
		this.valueAddedTax = valueAddedTax;
	}

	/**
	 * @return the averageNetPrice
	 */
	public BigDecimal getAverageNetPrice() {
		return averageNetPrice;
	}

	/**
	 * @param averageNetPrice the averageNetPrice to set
	 */
	public void setAverageNetPrice(BigDecimal averageNetPrice) {
		this.averageNetPrice = averageNetPrice;
	}

	/**
	 * @return the lastPurchasePrice
	 */
	public BigDecimal getLastPurchasePrice() {
		return lastPurchasePrice;
	}

	/**
	 * @param lastPurchasePrice the lastPurchasePrice to set
	 */
	public void setLastPurchasePrice(BigDecimal lastPurchasePrice) {
		this.lastPurchasePrice = lastPurchasePrice;
	}

	/**
	 * @return the previousPurchasePrice
	 */
	public BigDecimal getPreviousPurchasePrice() {
		return previousPurchasePrice;
	}

	/**
	 * @param previousPurchasePrice the previousPurchasePrice to set
	 */
	public void setPreviousPurchasePrice(BigDecimal previousPurchasePrice) {
		this.previousPurchasePrice = previousPurchasePrice;
	}

	/**
	 * @return the returnValue
	 */
	public BigDecimal getReturnValue() {
		return returnValue;
	}

	/**
	 * @param returnValue the returnValue to set
	 */
	public void setReturnValue(BigDecimal returnValue) {
		this.returnValue = returnValue;
	}

	/**
	 * @return the futurePurchasePrice
	 */
	public BigDecimal getFuturePurchasePrice() {
		return futurePurchasePrice;
	}

	/**
	 * @param futurePurchasePrice the futurePurchasePrice to set
	 */
	public void setFuturePurchasePrice(BigDecimal futurePurchasePrice) {
		this.futurePurchasePrice = futurePurchasePrice;
	}

	/**
	 * @return the futurePriceFromDate
	 */
	public BigDecimal getFuturePriceFromDate() {
		return futurePriceFromDate;
	}

	/**
	 * @param futurePriceFromDate the futurePriceFromDate to set
	 */
	public void setFuturePriceFromDate(BigDecimal futurePriceFromDate) {
		this.futurePriceFromDate = futurePriceFromDate;
	}

	/**
	 * @return the lastDisposalDate
	 */
	public BigDecimal getLastDisposalDate() {
		return lastDisposalDate;
	}

	/**
	 * @param lastDisposalDate the lastDisposalDate to set
	 */
	public void setLastDisposalDate(BigDecimal lastDisposalDate) {
		this.lastDisposalDate = lastDisposalDate;
	}

	/**
	 * @return the lastReceiptDate
	 */
	public BigDecimal getLastReceiptDate() {
		return lastReceiptDate;
	}

	/**
	 * @param lastReceiptDate the lastReceiptDate to set
	 */
	public void setLastReceiptDate(BigDecimal lastReceiptDate) {
		this.lastReceiptDate = lastReceiptDate;
	}

	/**
	 * @return the lastMovementDate
	 */
	public BigDecimal getLastMovementDate() {
		return lastMovementDate;
	}

	/**
	 * @param lastMovementDate the lastMovementDate to set
	 */
	public void setLastMovementDate(BigDecimal lastMovementDate) {
		this.lastMovementDate = lastMovementDate;
	}

	/**
	 * @return the currentInventoryCount
	 */
	public BigDecimal getCurrentInventoryCount() {
		return currentInventoryCount;
	}

	/**
	 * @param currentInventoryCount the currentInventoryCount to set
	 */
	public void setCurrentInventoryCount(BigDecimal currentInventoryCount) {
		this.currentInventoryCount = currentInventoryCount;
	}

	/**
	 * @return the salesStockLastYear
	 */
	public BigDecimal getSalesStockLastYear() {
		return salesStockLastYear;
	}

	/**
	 * @param salesStockLastYear the salesStockLastYear to set
	 */
	public void setSalesStockLastYear(BigDecimal salesStockLastYear) {
		this.salesStockLastYear = salesStockLastYear;
	}

	/**
	 * @return the currentYearSalesStock
	 */
	public BigDecimal getCurrentYearSalesStock() {
		return currentYearSalesStock;
	}

	/**
	 * @param currentYearSalesStock the currentYearSalesStock to set
	 */
	public void setCurrentYearSalesStock(BigDecimal currentYearSalesStock) {
		this.currentYearSalesStock = currentYearSalesStock;
	}

	/**
	 * @return the priceMark
	 */
	public String getPriceMark() {
		return priceMark;
	}

	/**
	 * @param priceMark the priceMark to set
	 */
	public void setPriceMark(String priceMark) {
		this.priceMark = priceMark;
	}

	/**
	 * @return the predecessorPartNumber
	 */
	public String getPredecessorPartNumber() {
		return predecessorPartNumber;
	}

	/**
	 * @param predecessorPartNumber the predecessorPartNumber to set
	 */
	public void setPredecessorPartNumber(String predecessorPartNumber) {
		this.predecessorPartNumber = predecessorPartNumber;
	}

	/**
	 * @return the successorPartNumber
	 */
	public String getSuccessorPartNumber() {
		return successorPartNumber;
	}

	/**
	 * @param successorPartNumber the successorPartNumber to set
	 */
	public void setSuccessorPartNumber(String successorPartNumber) {
		this.successorPartNumber = successorPartNumber;
	}

	/**
	 * @return the inventoryGroup
	 */
	public String getInventoryGroup() {
		return inventoryGroup;
	}

	/**
	 * @param inventoryGroup the inventoryGroup to set
	 */
	public void setInventoryGroup(String inventoryGroup) {
		this.inventoryGroup = inventoryGroup;
	}

	/**
	 * @return the average
	 */
	public String getAverage() {
		return average;
	}

	/**
	 * @param average the average to set
	 */
	public void setAverage(String average) {
		this.average = average;
	}

	/**
	 * @return the partKindId
	 */
	public BigDecimal getPartKindId() {
		return partKindId;
	}

	/**
	 * @param partKindId the partKindId to set
	 */
	public void setPartKindId(BigDecimal partKindId) {
		this.partKindId = partKindId;
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
	 * @return the fromCurrency
	 */
	public BigDecimal getFromCurrency() {
		return fromCurrency;
	}

	/**
	 * @param fromCurrency the fromCurrency to set
	 */
	public void setFromCurrency(BigDecimal fromCurrency) {
		this.fromCurrency = fromCurrency;
	}

	/**
	 * @return the toCurrency
	 */
	public BigDecimal getToCurrency() {
		return toCurrency;
	}

	/**
	 * @param toCurrency the toCurrency to set
	 */
	public void setToCurrency(BigDecimal toCurrency) {
		this.toCurrency = toCurrency;
	}

	/**
	 * @return the businessCaseWarehouse
	 */
	public String getBusinessCaseWarehouse() {
		return businessCaseWarehouse;
	}

	/**
	 * @param businessCaseWarehouse the businessCaseWarehouse to set
	 */
	public void setBusinessCaseWarehouse(String businessCaseWarehouse) {
		this.businessCaseWarehouse = businessCaseWarehouse;
	}

	/**
	 * @return the businessCaseDate
	 */
	public String getBusinessCaseDate() {
		return businessCaseDate;
	}

	/**
	 * @param businessCaseDate the businessCaseDate to set
	 */
	public void setBusinessCaseDate(String businessCaseDate) {
		this.businessCaseDate = businessCaseDate;
	}

	/**
	 * @return the businessCasePartNo
	 */
	public String getBusinessCasePartNo() {
		return businessCasePartNo;
	}

	/**
	 * @param businessCasePartNo the businessCasePartNo to set
	 */
	public void setBusinessCasePartNo(String businessCasePartNo) {
		this.businessCasePartNo = businessCasePartNo;
	}

	/**
	 * @return the businessCaseDocumentNo
	 */
	public String getBusinessCaseDocumentNo() {
		return businessCaseDocumentNo;
	}

	/**
	 * @param businessCaseDocumentNo the businessCaseDocumentNo to set
	 */
	public void setBusinessCaseDocumentNo(String businessCaseDocumentNo) {
		this.businessCaseDocumentNo = businessCaseDocumentNo;
	}

	/**
	 * @return the countingDifference
	 */
	public String getCountingDifference() {
		return countingDifference;
	}

	/**
	 * @param countingDifference the countingDifference to set
	 */
	public void setCountingDifference(String countingDifference) {
		this.countingDifference = countingDifference;
	}

	/**
	 * @return the bookingAmount
	 */
	public String getBookingAmount() {
		return bookingAmount;
	}

	/**
	 * @param bookingAmount the bookingAmount to set
	 */
	public void setBookingAmount(String bookingAmount) {
		this.bookingAmount = bookingAmount;
	}

	/**
	 * @return the businessCaseOrigin
	 */
	public String getBusinessCaseOrigin() {
		return businessCaseOrigin;
	}

	/**
	 * @param businessCaseOrigin the businessCaseOrigin to set
	 */
	public void setBusinessCaseOrigin(String businessCaseOrigin) {
		this.businessCaseOrigin = businessCaseOrigin;
	}

	/**
	 * @return the dakAlt
	 */
	public String getDakAlt() {
		return dakAlt;
	}

	/**
	 * @param dakAlt the dakAlt to set
	 */
	public void setDakAlt(String dakAlt) {
		this.dakAlt = dakAlt;
	}

	/**
	 * @return the dakNeu
	 */
	public String getDakNeu() {
		return dakNeu;
	}

	/**
	 * @param dakNeu the dakNeu to set
	 */
	public void setDakNeu(String dakNeu) {
		this.dakNeu = dakNeu;
	}

	/**
	 * @return the bestandAlt
	 */
	public String getBestandAlt() {
		return bestandAlt;
	}

	/**
	 * @param bestandAlt the bestandAlt to set
	 */
	public void setBestandAlt(String bestandAlt) {
		this.bestandAlt = bestandAlt;
	}

	/**
	 * @return the bestandNeu
	 */
	public String getBestandNeu() {
		return bestandNeu;
	}

	/**
	 * @param bestandNeu the bestandNeu to set
	 */
	public void setBestandNeu(String bestandNeu) {
		this.bestandNeu = bestandNeu;
	}

	/**
	 * @return the movementType
	 */
	public String getMovementType() {
		return movementType;
	}

	/**
	 * @param movementType the movementType to set
	 */
	public void setMovementType(String movementType) {
		this.movementType = movementType;
	}

	/**
	 * @return the movementTypeName
	 */
	public String getMovementTypeName() {
		return movementTypeName;
	}

	/**
	 * @param movementTypeName the movementTypeName to set
	 */
	public void setMovementTypeName(String movementTypeName) {
		this.movementTypeName = movementTypeName;
	}

	/**
	 * @return the brand
	 */
	public String getBrand() {
		return brand;
	}

	/**
	 * @param brand the brand to set
	 */
	public void setBrand(String brand) {
		this.brand = brand;
	}

	/**
	 * @return the positionNo
	 */
	public String getPositionNo() {
		return positionNo;
	}

	/**
	 * @param positionNo the positionNo to set
	 */
	public void setPositionNo(String positionNo) {
		this.positionNo = positionNo;
	}

	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * @return the wS
	 */
	public String getWS() {
		return WS;
	}

	/**
	 * @param wS the wS to set
	 */
	public void setWS(String wS) {
		WS = wS;
	}

	/**
	 * @return the users
	 */
	public String getUsers() {
		return users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(String users) {
		this.users = users;
	}

	/**
	 * @return the customerNo
	 */
	public String getCustomerNo() {
		return customerNo;
	}

	/**
	 * @param customerNo the customerNo to set
	 */
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	/**
	 * @return the mktgCodeForMovement
	 */
	public String getMktgCodeForMovement() {
		return mktgCodeForMovement;
	}

	/**
	 * @param mktgCodeForMovement the mktgCodeForMovement to set
	 */
	public void setMktgCodeForMovement(String mktgCodeForMovement) {
		this.mktgCodeForMovement = mktgCodeForMovement;
	}

	/**
	 * @return the supplierNo
	 */
	public String getSupplierNo() {
		return supplierNo;
	}

	/**
	 * @param supplierNo the supplierNo to set
	 */
	public void setSupplierNo(String supplierNo) {
		this.supplierNo = supplierNo;
	}

	/**
	 * @return the t1Value
	 */
	public String getT1Value() {
		return t1Value;
	}

	/**
	 * @param t1Value the t1Value to set
	 */
	public void setT1Value(String t1Value) {
		this.t1Value = t1Value;
	}

	/**
	 * @return the t2Value
	 */
	public String getT2Value() {
		return t2Value;
	}

	/**
	 * @param t2Value the t2Value to set
	 */
	public void setT2Value(String t2Value) {
		this.t2Value = t2Value;
	}

	/**
	 * @return the t3Value
	 */
	public String getT3Value() {
		return t3Value;
	}

	/**
	 * @param t3Value the t3Value to set
	 */
	public void setT3Value(String t3Value) {
		this.t3Value = t3Value;
	}

	/**
	 * @return the businessCaseKey
	 */
	public BigDecimal getBusinessCaseKey() {
		return businessCaseKey;
	}

	/**
	 * @param businessCaseKey the businessCaseKey to set
	 */
	public void setBusinessCaseKey(BigDecimal businessCaseKey) {
		this.businessCaseKey = businessCaseKey;
	}

	/**
	 * @return the businessCaseValue
	 */
	public String getBusinessCaseValue() {
		return businessCaseValue;
	}

	/**
	 * @param businessCaseValue the businessCaseValue to set
	 */
	public void setBusinessCaseValue(String businessCaseValue) {
		this.businessCaseValue = businessCaseValue;
	}

	/**
	 * @return the brandAlt
	 */
	public String getBrandAlt() {
		return brandAlt;
	}

	/**
	 * @param brandAlt the brandAlt to set
	 */
	public void setBrandAlt(String brandAlt) {
		this.brandAlt = brandAlt;
	}

	/**
	 * @return the ta_Alt
	 */
	public String getTa_Alt() {
		return ta_Alt;
	}

	/**
	 * @param ta_Alt the ta_Alt to set
	 */
	public void setTa_Alt(String ta_Alt) {
		this.ta_Alt = ta_Alt;
	}

	/**
	 * @return the ta_Neu
	 */
	public String getTa_Neu() {
		return ta_Neu;
	}

	/**
	 * @param ta_Neu the ta_Neu to set
	 */
	public void setTa_Neu(String ta_Neu) {
		this.ta_Neu = ta_Neu;
	}

	/**
	 * @return the la_Alt
	 */
	public BigDecimal getLa_Alt() {
		return la_Alt;
	}

	/**
	 * @param la_Alt the la_Alt to set
	 */
	public void setLa_Alt(BigDecimal la_Alt) {
		this.la_Alt = la_Alt;
	}

	/**
	 * @return the la_Neu
	 */
	public BigDecimal getLa_Neu() {
		return la_Neu;
	}

	/**
	 * @param la_Neu the la_Neu to set
	 */
	public void setLa_Neu(BigDecimal la_Neu) {
		this.la_Neu = la_Neu;
	}

	/**
	 * @return the marketingCode_Alt
	 */
	public String getMarketingCode_Alt() {
		return marketingCode_Alt;
	}

	/**
	 * @param marketingCode_Alt the marketingCode_Alt to set
	 */
	public void setMarketingCode_Alt(String marketingCode_Alt) {
		this.marketingCode_Alt = marketingCode_Alt;
	}

	/**
	 * @return the konto_Alt
	 */
	public BigDecimal getKonto_Alt() {
		return konto_Alt;
	}

	/**
	 * @param konto_Alt the konto_Alt to set
	 */
	public void setKonto_Alt(BigDecimal konto_Alt) {
		this.konto_Alt = konto_Alt;
	}

	/**
	 * @return the konto_Neu
	 */
	public BigDecimal getKonto_Neu() {
		return konto_Neu;
	}

	/**
	 * @param konto_Neu the konto_Neu to set
	 */
	public void setKonto_Neu(BigDecimal konto_Neu) {
		this.konto_Neu = konto_Neu;
	}

	/**
	 * @return the listPrice_Alt
	 */
	public BigDecimal getListPrice_Alt() {
		return listPrice_Alt;
	}

	/**
	 * @param listPrice_Alt the listPrice_Alt to set
	 */
	public void setListPrice_Alt(BigDecimal listPrice_Alt) {
		this.listPrice_Alt = listPrice_Alt;
	}

	/**
	 * @return the listPrice_Neu
	 */
	public BigDecimal getListPrice_Neu() {
		return listPrice_Neu;
	}

	/**
	 * @param listPrice_Neu the listPrice_Neu to set
	 */
	public void setListPrice_Neu(BigDecimal listPrice_Neu) {
		this.listPrice_Neu = listPrice_Neu;
	}

	/**
	 * @return the netPrice_Alt
	 */
	public BigDecimal getNetPrice_Alt() {
		return netPrice_Alt;
	}

	/**
	 * @param netPrice_Alt the netPrice_Alt to set
	 */
	public void setNetPrice_Alt(BigDecimal netPrice_Alt) {
		this.netPrice_Alt = netPrice_Alt;
	}

	/**
	 * @return the netPrice_Neu
	 */
	public BigDecimal getNetPrice_Neu() {
		return netPrice_Neu;
	}

	/**
	 * @param netPrice_Neu the netPrice_Neu to set
	 */
	public void setNetPrice_Neu(BigDecimal netPrice_Neu) {
		this.netPrice_Neu = netPrice_Neu;
	}

	/**
	 * @return the dak_Alt
	 */
	public String getDak_Alt() {
		return dak_Alt;
	}

	/**
	 * @param dak_Alt the dak_Alt to set
	 */
	public void setDak_Alt(String dak_Alt) {
		this.dak_Alt = dak_Alt;
	}

	/**
	 * @return the dak_Neu
	 */
	public String getDak_Neu() {
		return dak_Neu;
	}

	/**
	 * @param dak_Neu the dak_Neu to set
	 */
	public void setDak_Neu(String dak_Neu) {
		this.dak_Neu = dak_Neu;
	}

	/**
	 * @return the fester_Alt
	 */
	public String getFester_Alt() {
		return fester_Alt;
	}

	/**
	 * @param fester_Alt the fester_Alt to set
	 */
	public void setFester_Alt(String fester_Alt) {
		this.fester_Alt = fester_Alt;
	}

	/**
	 * @return the fester_Neu
	 */
	public String getFester_Neu() {
		return fester_Neu;
	}

	/**
	 * @param fester_Neu the fester_Neu to set
	 */
	public void setFester_Neu(String fester_Neu) {
		this.fester_Neu = fester_Neu;
	}

	/**
	 * @return the processCode
	 */
	public String getProcessCode() {
		return processCode;
	}

	/**
	 * @param processCode the processCode to set
	 */
	public void setProcessCode(String processCode) {
		this.processCode = processCode;
	}

	/**
	 * @return the predecessor_Alt
	 */
	public String getPredecessor_Alt() {
		return predecessor_Alt;
	}

	/**
	 * @param predecessor_Alt the predecessor_Alt to set
	 */
	public void setPredecessor_Alt(String predecessor_Alt) {
		this.predecessor_Alt = predecessor_Alt;
	}

	/**
	 * @return the predecessor_Neu
	 */
	public String getPredecessor_Neu() {
		return predecessor_Neu;
	}

	/**
	 * @param predecessor_neu the predecessor_Neu to set
	 */
	public void setPredecessor_Neu(String predecessor_Neu) {
		this.predecessor_Neu = predecessor_Neu;
	}

	/**
	 * @return the successor_Alt
	 */
	public String getSuccessor_Alt() {
		return successor_Alt;
	}

	/**
	 * @param successor_Alt the successor_Alt to set
	 */
	public void setSuccessor_Alt(String successor_Alt) {
		this.successor_Alt = successor_Alt;
	}

	/**
	 * @return the successor_Neu
	 */
	public String getSuccessor_Neu() {
		return successor_Neu;
	}

	/**
	 * @param successor_Neu the successor_Neu to set
	 */
	public void setSuccessor_Neu(String successor_Neu) {
		this.successor_Neu = successor_Neu;
	}

	/**
	 * @return the liferant_Alt
	 */
	public String getLiferant_Alt() {
		return liferant_Alt;
	}

	/**
	 * @param liferant_Alt the liferant_Alt to set
	 */
	public void setLiferant_Alt(String liferant_Alt) {
		this.liferant_Alt = liferant_Alt;
	}

	/**
	 * @return the liferant_Neu
	 */
	public String getLiferant_Neu() {
		return liferant_Neu;
	}

	/**
	 * @param liferant_Neu the liferant_Neu to set
	 */
	public void setLiferant_Neu(String liferant_Neu) {
		this.liferant_Neu = liferant_Neu;
	}

	/**
	 * @return the label_Alt
	 */
	public String getLabel_Alt() {
		return label_Alt;
	}

	/**
	 * @param label_Alt the label_Alt to set
	 */
	public void setLabel_Alt(String label_Alt) {
		this.label_Alt = label_Alt;
	}

	/**
	 * @return the label_Neu
	 */
	public String getLabel_Neu() {
		return label_Neu;
	}

	/**
	 * @param label_Neu the label_Neu to set
	 */
	public void setLabel_Neu(String label_Neu) {
		this.label_Neu = label_Neu;
	}

	/**
	 * @return the sa_Alt
	 */
	public String getSa_Alt() {
		return sa_Alt;
	}

	/**
	 * @param sa_Alt the sa_Alt to set
	 */
	public void setSa_Alt(String sa_Alt) {
		this.sa_Alt = sa_Alt;
	}

	/**
	 * @return the sa_Neu
	 */
	public String getSa_Neu() {
		return sa_Neu;
	}

	/**
	 * @param sa_Neu the sa_Neu to set
	 */
	public void setSa_Neu(String sa_Neu) {
		this.sa_Neu = sa_Neu;
	}

	/**
	 * @return the mengeVorBA67
	 */
	public Double getMengeVorBA67() {
		return mengeVorBA67;
	}

	/**
	 * @param mengeVorBA67 the mengeVorBA67 to set
	 */
	public void setMengeVorBA67(Double mengeVorBA67) {
		this.mengeVorBA67 = mengeVorBA67;
	}

	/**
	 * @return the mengeNachBA
	 */
	public Double getMengeNachBA() {
		return mengeNachBA;
	}

	/**
	 * @param mengeNachBA the mengeNachBA to set
	 */
	public void setMengeNachBA(Double mengeNachBA) {
		this.mengeNachBA = mengeNachBA;
	}

	/**
	 * @return the listenpreisAlt
	 */
	public Double getListenpreisAlt() {
		return listenpreisAlt;
	}

	/**
	 * @param listenpreisAlt the listenpreisAlt to set
	 */
	public void setListenpreisAlt(Double listenpreisAlt) {
		this.listenpreisAlt = listenpreisAlt;
	}

	/**
	 * @return the listenpreisNeu
	 */
	public Double getListenpreisNeu() {
		return listenpreisNeu;
	}

	/**
	 * @param listenpreisNeu the listenpreisNeu to set
	 */
	public void setListenpreisNeu(Double listenpreisNeu) {
		this.listenpreisNeu = listenpreisNeu;
	}

	/**
	 * @return the rb_Alt
	 */
	public String getRb_Alt() {
		return rb_Alt;
	}

	/**
	 * @param rb_Alt the rb_Alt to set
	 */
	public void setRb_Alt(String rb_Alt) {
		this.rb_Alt = rb_Alt;
	}

	/**
	 * @return the rb_New
	 */
	public String getRb_New() {
		return rb_New;
	}

	/**
	 * @param rb_New the rb_New to set
	 */
	public void setRb_New(String rb_New) {
		this.rb_New = rb_New;
	}
		
}