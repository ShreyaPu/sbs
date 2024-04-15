package com.alphax.vo.mb;

import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Validated
@ApiModel(description = "This Object about Report Selections Details")
public class ReportSelectionsDTO {

	@ApiModelProperty(value = "Hersteller (OEM)  - (DB E_ETSTAMM- 1-HERST).")
	@Size(max =4)
	private String manufacturer;

	@ApiModelProperty(value ="Teilenummer - (DB E_ETSTAMM- 2-TNR)." )
	@Size(max =30)
	private String partNumber;

	@ApiModelProperty(value ="AP-Lager  - (DB E_ETSTAMM- 3-LNR)." )
	@Size(max=2)
	private String alphaPlusWarehouseId;
	
	@ApiModelProperty(value ="AX-Lager  - (DB O_WRH - NAME)." )
	@Size(max=50)
	private String alphaxWarehouseName;

	@ApiModelProperty(value ="Satzart  - (DB E_ETSTAMM- 4-SA)." )
	@Size(max=1)
	private String storageIndikator;

	@ApiModelProperty(value ="Bezeichnung  - (DB E_ETSTAMM- 7-BENEN)." )
	@Size(max=50)
	private String name;
	
	
	//For Report
	
	@ApiModelProperty(value ="Teileart  - (DB E_ETSTAMM- 5-TA)." )
	@Size(max=2)
	private String partsIndikator;

	@ApiModelProperty(value ="Leistungsart  - (DB E_ETSTAMM- 6-LEIART)." )
	@Size(max=2)
	private String activityType;

	@ApiModelProperty(value ="Lagerort  - (DB E_ETSTAMM- 8-LOPA)." )
	@Size(max=8)
	private String storageLocation;

	@ApiModelProperty(value ="Bruttopreis  - (DB E_ETSTAMM- 10-EPR)." )
	@Size(max=11)
	private String purchasePrice;

	@ApiModelProperty(value ="Durchschnittlicher Nettopreis DAK - (DB E_ETSTAMM- 11-DAK)." )
	@Size(max=11)
	private String averageNetPrice;

	@ApiModelProperty(value ="Rückgabewert  - (DB E_ETSTAMM- 14-TRUWER)." )
	private String returnValue;

	@ApiModelProperty(value ="Letzter Einkaufspreis  - (DB E_ETSTAMM- 15-LEKPR)." )
	@Size(max=11)
	private String lastPurchasePrice;

	@ApiModelProperty(value ="Zukünftiger neuer Preis  - (DB E_ETSTAMM- 17-ZPREIS)." )
	@Size(max=11)
	private String futurePurchasePrice;

	@ApiModelProperty(value ="kalkulierter Nettopr. WE - (DB E_ETSTAMM- 19-NPREIS)." )
	@Size(max=11)
	private String previousPurchasePrice;

	@ApiModelProperty(value ="Rabattgruppe - (DB E_ETSTAMM- 21-RG)." )
	private String discountGroup;

	@ApiModelProperty(value ="Teilemarke - (DB E_ETSTAMM- 22-TMARKE)." )
	private String partBrand;

	@ApiModelProperty(value ="Mehrwertsteuer - (DB REFERENZ- KEYFLD )." )
	private String valueAddedTax;

	@ApiModelProperty(value ="Inventur-Bestand Zählung - (DB E_ETSTAMM- 28-IVZME)." )
	private String currentInventoryCount;

	@ApiModelProperty(value ="Aktueller Bestand - (DB E_ETSTAMM- 29-AKTBES)." )
	private String currentStock;

	@ApiModelProperty(value ="Umsatz ME  Vorjahr - (DB E_ETSTAMM- 58-VKSVJS)." )
	private String salesStockLastYear;

	@ApiModelProperty(value ="Umsatz ME  lfd. Jahr - (DB E_ETSTAMM- 59-VKLFJS)." )
	private String currentYearSalesStock;

	@ApiModelProperty(value ="Bestellausstand bzw. Anzahl offener Bestellungen (2 Dezimalstellen) - (DB E_ETSTAMM- 120-BESAUS)." )
	private String pendingOrders;

	@ApiModelProperty(value ="Preiskennzeichen - (DB E_ETSTAMM- 157-PRKZ)." )
	private String priceMark;

	@ApiModelProperty(value ="Datum letzter Abgang - (DB E_ETSTAMM- 167-DTLABG)." )
	private String lastDisposalDate;

	@ApiModelProperty(value ="Datum letzter Zugang - (DB E_ETSTAMM- 168-DTLZUG)." )
	private String lastReceiptDate;

	@ApiModelProperty(value ="Datum für Gültigkeit des neuen Preises (gültig ab zukünft. Preis) - (DB E_ETSTAMM- 172-GPRDAT)." )
	private String futurePriceFromDate;

	@ApiModelProperty(value ="Datum der letzten Bewegung - (DB E_ETSTAMM- 173-DTLBEW)." )
	private String lastMovementDate;

	@ApiModelProperty(value ="Marketingcode - (DB E_ETSTAMM- 177-MC)." )
	private String marketingCode;

	@ApiModelProperty(value ="Teilenummer Vorgänger - (DB E_ETSTAMM- 180-TNRV)." )
	private String predecessorPartNumber;

	@ApiModelProperty(value ="Teilenummer Nachfolger - (DB E_ETSTAMM- 181-TNRN)." )
	private String successorPartNumber;

	@ApiModelProperty(value ="Diebstahlkennzeichen - (DB E_ETSTAMM- 190-DRTKZ)." )
	private String theftRelevantLicensePlate;

	@ApiModelProperty(value ="Gefahrgutkennzeichen - (DB E_ETSTAMM- 191-GFKZ)." )
	private String reachLabel;

	@ApiModelProperty(value ="Inventur Zählgruppe - (DB E_ETSTAMM- 201-ZAEGRU)." )
	private String inventoryGroup;

	@ApiModelProperty(value ="AP-Firma - (DB E_ETSTAMM- 208-FIRMA)." )
	private String alphaPlusCompanyId;
	
	@ApiModelProperty(value ="AX-Firma - (DB O_COMPANY- NAME)." )
	private String alphaxCompanyName;

	@ApiModelProperty(value ="AP-Filiale - (DB E_ETSTAMM- 209-FILIAL)." )
	private String alphaPlusAgencyId;
	
	@ApiModelProperty(value ="AX-Filiale - (DB O_AGENCY - NAME)." )
	private String alphaxAgencyName;
	
	@ApiModelProperty(value ="Durchschnitt - (DB E_ETSTAMM - ?)." )
	private String average;
	
	@ApiModelProperty(value ="PartExpDate flag - (DB etk_tlekat - PARTEXPDATE)." )
	private Boolean partExpDate;

	public Boolean getPartExpDate() {
		return partExpDate;
	}

	public void setPartExpDate(Boolean partExpDate) {
		this.partExpDate = partExpDate;
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
	 * @return the partsIndikator
	 */
	public String getPartsIndikator() {
		return partsIndikator;
	}

	/**
	 * @param partsIndikator the partsIndikator to set
	 */
	public void setPartsIndikator(String partsIndikator) {
		this.partsIndikator = partsIndikator;
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
	 * @return the purchasePrice
	 */
	public String getPurchasePrice() {
		return purchasePrice;
	}

	/**
	 * @param purchasePrice the purchasePrice to set
	 */
	public void setPurchasePrice(String purchasePrice) {
		this.purchasePrice = purchasePrice;
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
	 * @return the returnValue
	 */
	public String getReturnValue() {
		return returnValue;
	}

	/**
	 * @param returnValue the returnValue to set
	 */
	public void setReturnValue(String returnValue) {
		this.returnValue = returnValue;
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
	 * @return the futurePurchasePrice
	 */
	public String getFuturePurchasePrice() {
		return futurePurchasePrice;
	}

	/**
	 * @param futurePurchasePrice the futurePurchasePrice to set
	 */
	public void setFuturePurchasePrice(String futurePurchasePrice) {
		this.futurePurchasePrice = futurePurchasePrice;
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
	 * @return the currentInventoryCount
	 */
	public String getCurrentInventoryCount() {
		return currentInventoryCount;
	}

	/**
	 * @param currentInventoryCount the currentInventoryCount to set
	 */
	public void setCurrentInventoryCount(String currentInventoryCount) {
		this.currentInventoryCount = currentInventoryCount;
	}

	/**
	 * @return the currentStock
	 */
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
	 * @return the salesStockLastYear
	 */
	public String getSalesStockLastYear() {
		return salesStockLastYear;
	}

	/**
	 * @param salesStockLastYear the salesStockLastYear to set
	 */
	public void setSalesStockLastYear(String salesStockLastYear) {
		this.salesStockLastYear = salesStockLastYear;
	}

	/**
	 * @return the currentYearSalesStock
	 */
	public String getCurrentYearSalesStock() {
		return currentYearSalesStock;
	}

	/**
	 * @param currentYearSalesStock the currentYearSalesStock to set
	 */
	public void setCurrentYearSalesStock(String currentYearSalesStock) {
		this.currentYearSalesStock = currentYearSalesStock;
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
	 * @return the lastReceiptDate
	 */
	public String getLastReceiptDate() {
		return lastReceiptDate;
	}

	/**
	 * @param lastReceiptDate the lastReceiptDate to set
	 */
	public void setLastReceiptDate(String lastReceiptDate) {
		this.lastReceiptDate = lastReceiptDate;
	}

	/**
	 * @return the futurePriceFromDate
	 */
	public String getFuturePriceFromDate() {
		return futurePriceFromDate;
	}

	/**
	 * @param futurePriceFromDate the futurePriceFromDate to set
	 */
	public void setFuturePriceFromDate(String futurePriceFromDate) {
		this.futurePriceFromDate = futurePriceFromDate;
	}

	/**
	 * @return the lastMovementDate
	 */
	public String getLastMovementDate() {
		return lastMovementDate;
	}

	/**
	 * @param lastMovementDate the lastMovementDate to set
	 */
	public void setLastMovementDate(String lastMovementDate) {
		this.lastMovementDate = lastMovementDate;
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
	 * @return the alphaPlusCompanyId
	 */
	public String getAlphaPlusCompanyId() {
		return alphaPlusCompanyId;
	}

	/**
	 * @param alphaPlusCompanyId the alphaPlusCompanyId to set
	 */
	public void setAlphaPlusCompanyId(String alphaPlusCompanyId) {
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
	public String getAlphaPlusAgencyId() {
		return alphaPlusAgencyId;
	}

	/**
	 * @param alphaPlusAgencyId the alphaPlusAgencyId to set
	 */
	public void setAlphaPlusAgencyId(String alphaPlusAgencyId) {
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
	 * @return the alphaPlusWarehouseId
	 */
	public String getAlphaPlusWarehouseId() {
		return alphaPlusWarehouseId;
	}

	/**
	 * @param alphaPlusWarehouseId the alphaPlusWarehouseId to set
	 */
	public void setAlphaPlusWarehouseId(String alphaPlusWarehouseId) {
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

	
}