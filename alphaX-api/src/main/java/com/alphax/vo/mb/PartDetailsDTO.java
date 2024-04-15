package com.alphax.vo.mb;

import java.util.List;

import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Validated
@ApiModel(description = "This Object about Part details")
public class PartDetailsDTO {

	@ApiModelProperty(value = "Hersteller (OEM)  - (DB E_ETSTAMM- 1-HERST).")
	@Size(max =4)
	private String oem;

	@ApiModelProperty(value ="Teilenummer - (DB E_ETSTAMM- 2-TNR)." )
	@Size(max =30)
	private String partNumber;

	@ApiModelProperty(value ="Lagernummer  - (DB E_ETSTAMM- 3-LNR)." )
	@Size(max=2)
	private String warehouse;

	@ApiModelProperty(value ="Satzart  - (DB E_ETSTAMM- 4-SA)." )
	@Size(max=1)
	private String storageIndikator;

	@ApiModelProperty(value ="Teileart  - (DB E_ETSTAMM- 5-TA)." )
	@Size(max=2)
	private String partsIndikator;

	@ApiModelProperty(value ="Leistungsart  - (DB E_ETSTAMM- 6-LEIART)." )
	@Size(max=2)
	private String activityType;

	@ApiModelProperty(value ="Bezeichnung  - (DB E_ETSTAMM- 7-BENEN)." )
	@Size(max=50)
	private String name;

	@ApiModelProperty(value ="Lagerort  - (DB E_ETSTAMM- 8-LOPA)." )
	@Size(max=8)
	private String storageLocation;

	@ApiModelProperty(value ="Lieferwerk  - (DB E_ETSTAMM- 9-LIWERK)." )
	@Size(max=2)
	private String deliverIndicator;

	@ApiModelProperty(value ="Bruttopreis  - (DB E_ETSTAMM- 10-EPR)." )
	@Size(max=11)
	private String purchasePrice;

	@ApiModelProperty(value ="Durchschnittlicher Nettopreis DAK - (DB E_ETSTAMM- 11-DAK)." )
	@Size(max=11)
	private String averageNetPrice;

	@ApiModelProperty(value ="Einkaufs Nettopreis  - (DB E_ETSTAMM- 12-EKNPR)." )
	@Size(max=11)
	private String netPrice;

	@ApiModelProperty(value ="Pfand  - (DB E_ETSTAMM- 13-PFAND)." )
	private String deposit;

	@ApiModelProperty(value ="Rückgabewert  - (DB E_ETSTAMM- 14-TRUWER)." )
	private String returnValue;

	@ApiModelProperty(value ="Letzter Einkaufspreis  - (DB E_ETSTAMM- 15-LEKPR)." )
	@Size(max=11)
	private String lastPurchasePrice;

	@ApiModelProperty(value ="Entsorgungspauschale Wert Euro - (DB E_ETSTAMM- 16-ENTSOW)." )
	@Size(max=11)
	private String disposalCostEuro;

	@ApiModelProperty(value ="Zukünftiger neuer Preis  - (DB E_ETSTAMM- 17-ZPREIS)." )
	@Size(max=11)
	private String futurePurchasePrice;

	@ApiModelProperty(value ="alter preis  - (DB E_ETSTAMM- 18-APREIS)." )
	@Size(max=11)
	private String oldPurchasePrice;

	@ApiModelProperty(value ="kalkulierter Nettopr. WE - (DB E_ETSTAMM- 19-NPREIS)." )
	@Size(max=11)
	private String previousPurchasePrice;

	@ApiModelProperty(value ="Entsorgungspauschale - (DB E_ETSTAMM- 20-ENTSOP)." )
	@Size(max=11)
	private String disposalCost;

	@ApiModelProperty(value ="Rabattgruppe - (DB E_ETSTAMM- 21-RG)." )
	private String discountGroup;

	@ApiModelProperty(value ="Teilemarke - (DB E_ETSTAMM- 22-TMARKE)." )
	private String oemBrand;

	@ApiModelProperty(value ="Mehrwertsteuer - (DB E_ETSTAMM- 23-MWST)." )
	private String valueAddedTax;

	@ApiModelProperty(value ="Gemeinteil für UNIMOG - (DB E_ETSTAMM- 24-SKL)." )
	private String commonPartWithUnimog;

	@ApiModelProperty(value ="Mindestbestand - (DB E_ETSTAMM- 25-MINBES)." )
	private String minimumStock;

	@ApiModelProperty(value ="Maximalbestand - (DB E_ETSTAMM- 26-MAXBES)." )
	private String maximumStock;

	@ApiModelProperty(value ="Sicherheits-Bestand - (DB E_ETSTAMM- 27-SIBES)." )
	private String saftyStock;

	@ApiModelProperty(value ="Inventur-Bestand Zählung - (DB E_ETSTAMM- 28-IVZME)." )
	private String currentInventoryCountedStock;

	@ApiModelProperty(value ="Aktueller Bestand - (DB E_ETSTAMM- 29-AKTBES)." )
	private String currentStock;

	@ApiModelProperty(value ="Letzter Inventur-Bestand - (DB E_ETSTAMM- 30-LIVBES)." )
	private String previousInventoryCountedStock;

	@ApiModelProperty(value ="Verkauf Mengen Einheit lfd. Monat - (DB E_ETSTAMM- 32-VKLFMS)." )
	private String salesStockCurrentMonth;

	@ApiModelProperty(value ="Zugang Einheiten Monatswerte laufendes Jahr (2 Dezimalstellen) - (DB E_ETSTAMM- 33-44 VKLJ1S-VKLJCS)." )
	private List<MonthlyDecimalValue> currentYearMonthlySalesStock;

	@ApiModelProperty(value ="durchschn. Umsatz  ME lfd.  Jahr - (DB E_ETSTAMM- 45-VKAVGS)." )
	private String salesStockAverageCurrentYear;

	@ApiModelProperty(value ="Zugang Einheiten Monatswerte vergangenes Jahr (2 Dezimalstellen) - (DB E_ETSTAMM- 46-57 VKVJ1S-VKVJCS)." )
	private List<MonthlyDecimalValue> lastYearMonthlySalesStock;

	@ApiModelProperty(value ="Umsatz ME  Vorjahr - (DB E_ETSTAMM- 58-VKSVJS)." )
	private String salesStockLastYear;

	@ApiModelProperty(value ="Umsatz ME  lfd. Jahr - (DB E_ETSTAMM- 59-VKLFJS)." )
	private String currentYearSalesStock;

	@ApiModelProperty(value ="Zugang laufender Monat (4 Dezimalstellen) - (DB E_ETSTAMM- 60-ZULFMS)." )
	private String currentMonthReceiptStock;

	@ApiModelProperty(value ="Zugang Monatswerte laufendes Jahr (4 Dezimalstellen) - (DB E_ETSTAMM- 61-72 ZULM1S-ZULMCS)." )
	private List<MonthlyDecimalValue> monthlyReceiptsStock;

	@ApiModelProperty(value ="Durchschnittlicher Zugang ME laufendes Jahr (4 Dezimalstellen) - (DB E_ETSTAMM- 73-ZUAVGS)." )
	private String currentYearAverageReceiptStock;

	@ApiModelProperty(value ="Zugang ME Vorjahr (4 Dezimalstellen) - (DB E_ETSTAMM- 74-ZUSVJS)." )
	private String lastYearReceiptStock;

	@ApiModelProperty(value ="Zugang ME laufendes Jahr (4 Dezimalstellen) - (DB E_ETSTAMM- 75-ZULFJS)." )
	private String currentYearReceiptStock;

	@ApiModelProperty(value ="Verk. EUR lfd. Monat (4 Dezimalstellen)  - (DB E_ETSTAMM- 76-VKLFMW)." )
	private String currentMonthSales;

	@ApiModelProperty(value ="Verkauf laufendes Jahr EUR Januar-Dezember (4 Dezimalstellen) - (DB E_ETSTAMM- 77-88 VKLJ1W-VKLJCW)." )
	private List<MonthlyDecimalValue> currentYearMonthlySales;

	@ApiModelProperty(value ="Durchschnittlicher Umsatz laufendes Jahr EUR (4 Dezimalstellen) - (DB E_ETSTAMM- 89-VKAVGW)." )
	private String averageSales;

	@ApiModelProperty(value ="Verkauf Vorjahr EUR Januar-Dezember (4 Dezimalstellen) - (DB E_ETSTAMM- 90-101 VKVJ1W-VKVJCW)." )
	private List<MonthlyDecimalValue> lastYearMonthlySales;

	@ApiModelProperty(value ="Umsatz EUR Vorjahr (4 Dezimalstellen) - (DB E_ETSTAMM- 102-VKSVJW)." )
	private String lastYearSales;

	@ApiModelProperty(value ="Umsatz EUR laufendes Jahr (4 Dezimalstellen) - (DB E_ETSTAMM- 103-VKLFJW)." )
	private String currentYearSales;

	@ApiModelProperty(value ="Zugang EUE lfd. Monat (4 Dezimalstellen) - (DB E_ETSTAMM- 104-ZULFMW)." )
	private String currentMonthReceiptInEuro;

	@ApiModelProperty(value ="Zugang EUR Janur-Dezember (4 Dezimalstellen) - (DB E_ETSTAMM- 105-116 ZULM1W-ZULMCW)." )
	private List<MonthlyDecimalValue> monthlyReceiptsInEuro;

	@ApiModelProperty(value ="Durchschnittlicher Zugang EUR lfd. Jahr (4 Dezimalstellen) - (DB E_ETSTAMM- 117-ZUAVGW)." )
	private String averageReceiptInEuro;

	@ApiModelProperty(value ="Zugang EUR Vorjahr (4 Dezimalstellen) - (DB E_ETSTAMM- 118-ZUSVJW)." )
	private String lastYearReceiptInEuro;

	@ApiModelProperty(value ="Zugang EUR lfd. Jahr (4 Dezimalstellen) - (DB E_ETSTAMM- 119-ZULFJW)." )
	private String currentYearReceiptInEuro;

	@ApiModelProperty(value ="Bestellausstand bzw. Anzahl offener Bestellungen (2 Dezimalstellen) - (DB E_ETSTAMM- 120-BESAUS)." )
	private String pendingOrders;

	@ApiModelProperty(value ="Mindestbestellmenge - (DB E_ETSTAMM- 121-MIBESM)." )
	private String minimumOrderQuantity;

	@ApiModelProperty(value ="Kundenrückstand - (DB E_ETSTAMM- 122-KDRUE)." )
	private String customerBacklog;

	@ApiModelProperty(value ="Lieferrückstand - (DB E_ETSTAMM- 123-LIFRUE)." )
	private String deliveryBacklog;

	@ApiModelProperty(value ="Anzahl Verkäufe - (DB E_ETSTAMM- 124-ANZVK)." )
	private String salesAmount;

	@ApiModelProperty(value ="Nachgefragte Mengen - (DB E_ETSTAMM- 125-NFMGE)." )
	private String requestedQuantity;

	@ApiModelProperty(value ="Anzahl Zubuchungen - (DB E_ETSTAMM- 126-ZUMGE)." )
	private String bookingAmount;

	@ApiModelProperty(value ="Anzahl Nachfragen - (DB E_ETSTAMM- 127-ANZNFS)." )
	private String requestedAmount;

	@ApiModelProperty(value ="Anzahl Nachfragen lfd. Jahr Janur-Dezember - (DB E_ETSTAMM- 128-139 NFLJ01-NFLJ12)." )
	private List<MonthlyIntValue> currentYearMonthlyRequestedAmounts;

	@ApiModelProperty(value ="Anzahl Nachfragen Vorjahr Janur-Dezember - (DB E_ETSTAMM- 140-151 NFVJ01-NFVJ12)." )
	private List<MonthlyIntValue> lastYearMonthlyRequestedAmounts;

	@ApiModelProperty(value ="Vorshclag Service - (DB E_ETSTAMM- 152-TVORMG??)." )
	private String serviceProposal;

	@ApiModelProperty(value ="Inventur Zählmenge - (DB E_ETSTAMM- 153-VORHS)." )
	private String inventoryAmount;

	@ApiModelProperty(value ="Bestellvermerk. - (DB E_ETSTAMM- 154-BESVER)" )
	private String orderAnnotation;

	@ApiModelProperty(value ="Kalkulationssperre - (DB E_ETSTAMM- 155-KSPKZ)." )
	private String calculationLock;

	@ApiModelProperty(value ="Kennzeichen Comparts - (DB E_ETSTAMM- 156-NC)." )
	private String compartsMark;

	@ApiModelProperty(value ="Preiskennzeichen - (DB E_ETSTAMM- 157-PRKZ)." )
	private String priceMark;

	@ApiModelProperty(value ="Löschkennzeichen - (DB E_ETSTAMM- 158-LKZ)." )
	private String deletionMark;

	@ApiModelProperty(value ="Kennzeichen Dispo - (DB E_ETSTAMM- 159-DISPO)." )
	private String dispoMark;

	@ApiModelProperty(value ="Inventurkennzeichen - (DB E_ETSTAMM- 160-IVKZ)." )
	private String inventorygMark;

	@ApiModelProperty(value ="Barcode Nummer - (DB E_ETSTAMM- 161-BARCDE)." )
	private String barcodeNumber;

	@ApiModelProperty(value ="Zu- oder Abschlag (2 Dezimalstellen) - (DB E_ETSTAMM- 162-ABSCH)." )
	private String surchargeDeduction;

	@ApiModelProperty(value ="Kennzeichen Zu- oder Abschlag - (DB E_ETSTAMM- 163-KDABS)." )
	private String surchargeDeductionMark;

	@ApiModelProperty(value ="Aktionkennzeichen - (DB E_ETSTAMM- 164-WINTER)." )
	private String campaignMark;

	@ApiModelProperty(value ="Modus - (DB E_ETSTAMM- 166-MODUS)." )
	private String mode;

	@ApiModelProperty(value ="Datum letzter Abgang - (DB E_ETSTAMM- 167-DTLABG)." )
	private String lastDisposalDate;

	@ApiModelProperty(value ="Datum letzter Zugang - (DB E_ETSTAMM- 168-DTLZUG)." )
	private String lastReceiptDate;

	@ApiModelProperty(value ="Anlagedatum  - (DB E_ETSTAMM- 169-DTANLA)." )
	private String creationDate;

	@ApiModelProperty(value ="Datum letzte Bestellung - (DB E_ETSTAMM- 170-DTLBES)." )
	private String lastOrderDate;

	@ApiModelProperty(value ="Verf. Dat (Vrafllsdatum) - (DB E_ETSTAMM- 171-DTVERF)." )
	private String expirationDate;

	@ApiModelProperty(value ="Datum für Gültigkeit des neuen Preises (gültig ab zukünft. Preis) - (DB E_ETSTAMM- 172-GPRDAT)." )
	private String futurePriceFromDate;

	@ApiModelProperty(value ="Datum der letzten Bewegung - (DB E_ETSTAMM- 173-DTLBEW)." )
	private String lastMovementDate;

	@ApiModelProperty(value ="Inventurdatum - (DB E_ETSTAMM- 174-DTINV)." )
	private String inventoryDate;

	@ApiModelProperty(value ="Datum der letzten Disposition - (DB E_ETSTAMM- 175-DATLDI)." )
	private String lastDispositionDate;

	@ApiModelProperty(value ="Marketingcode - (DB E_ETSTAMM- 177-MC)." )
	private String marketingCode;

	@ApiModelProperty(value ="Teilenummer Sort-Format  - (DB E_ETSTAMM- 178-TNRS)." )
	private String sortingFormatPartNumber;

	@ApiModelProperty(value ="Teilenummer Druckformat  - (DB E_ETSTAMM- 179-TNRD)." )
	private String printingFormatPartNumber;

	@ApiModelProperty(value ="Teilenummer Vorgänger - (DB E_ETSTAMM- 180-TNRV)." )
	private String predecessorPartNumber;

	@ApiModelProperty(value ="Teilenummer Nachfolger - (DB E_ETSTAMM- 181-TNRN)." )
	private String successorPartNumber;

	@ApiModelProperty(value ="Teilenummer Marktplatz - (DB E_ETSTAMM- 182-TNRM)." )
	private String partNumberMarketplace;

	@ApiModelProperty(value ="Lagernummer für Marktplatz - (DB E_ETSTAMM- 183-TNRMLN)." )
	private String marketplaceStockNumber;

	@ApiModelProperty(value ="Teilenummer des Herstellers - (DB E_ETSTAMM- 184-TNRH)." )
	private String oemPartNumber;

	@ApiModelProperty(value ="C-Code Hinweis - (DB E_ETSTAMM- 185-TCODE)." )
	private String cCodeHint;

	@ApiModelProperty(value ="Verpackungseinheit 1 - (DB E_ETSTAMM- 186-VERP1)." )
	private String packagingUnit1;

	@ApiModelProperty(value ="Verpackungseinheit 2 - (DB E_ETSTAMM- 187-VERP2)." )
	private String packagingUnit2;

	@ApiModelProperty(value ="löschkennzeichen 2 - (DB E_ETSTAMM- 188-LKZ2)." )
	private String deletionMark2;

	@ApiModelProperty(value ="Kennzeichen Teilenummer Identifizierung - (DB E_ETSTAMM- 189-TIDENT)." )
	private String partNumberIdentificationMark;

	@ApiModelProperty(value ="Diebstahlkennzeichen - (DB E_ETSTAMM- 190-DRTKZ)." )
	private String theftMark;

	@ApiModelProperty(value ="Gefahrgutkennzeichen - (DB E_ETSTAMM- 191-GFKZ)." )
	private String hazardousMaterialMark;

	@ApiModelProperty(value ="Tauschkennzeichen  - (DB E_ETSTAMM- 192-TAUKZ)." )
	private String exchangeMark;

	@ApiModelProperty(value ="Gewicht (3 Dezimalstellen) - (DB E_ETSTAMM- 194-GEWICH)." )
	private String weight;

	@ApiModelProperty(value ="Altes Volumen (3 Dezimalstellen) - (DB E_ETSTAMM- 195-TVOL)." )
	private String oldVolume;

	@ApiModelProperty(value ="Kennzeichen Rabatttsperre - (DB E_ETSTAMM- 196-TRSPER??)." )
	private String discountLockMark;

	@ApiModelProperty(value ="Kennzeichen Einsendepflicht - (DB E_ETSTAMM- 197-KZEINS)." )
	private String submissionMark;

	@ApiModelProperty(value ="HWS Nummer - (DB E_ETSTAMM- 198-THWSNR)." )
	private String tradeGoodsNumber;

	@ApiModelProperty(value ="Mengeneinheit  - (DB E_ETSTAMM- 200-ME)." )
	private String quantityUnit;

	@ApiModelProperty(value ="Inventur Zählgruppe - (DB E_ETSTAMM- 201-ZAEGRU)." )
	private String inventoryGroup;

	@ApiModelProperty(value ="Kennzeichen Bevorratung - (DB E_ETSTAMM- 202-KZBEVO)." )
	private String stockageMark;

	@ApiModelProperty(value ="Kennzeichen Rückgabe erlaubt - (DB E_ETSTAMM- 203-KZRUCK)." )
	private String returnPermittedMark;
	
	@ApiModelProperty(value ="Präferenz Kennzeichen - (DB E_ETSTAMM- 207-PRAFKZ)." )
	private String preferenceMark;

	@ApiModelProperty(value ="Firma - (DB E_ETSTAMM- 208-FIRMA)." )
	private String company;

	@ApiModelProperty(value ="Filiale - (DB E_ETSTAMM- 209-FILIAL)." )
	private String branch;

	@ApiModelProperty(value ="Kennzeichen Verkaufssperre - (DB E_ETSTAMM- 212-SPKZV)." )
	private String salesLockMark;

	@ApiModelProperty(value ="Hauptlieferant - (DB E_ETSTAMM- 213-LIEFNR)." )
	private String mainSupplier;

	@ApiModelProperty(value ="Länge in Zentimeter (0 Dezimalstellen)  - (DB E_ETSTAMM- part of 214-TVOLN)." )
	private String length;

	@ApiModelProperty(value ="Breite in Zentimeter (0 Dezimalstellen)  - (DB E_ETSTAMM- part of 214-TVOLN)." )
	private String width;

	@ApiModelProperty(value ="Höhe in Zentimeter (0 Dezimalstellen)  - (DB E_ETSTAMM- part of 214-TVOLN)." )
	private String height;
	
	@ApiModelProperty(value ="volumen in Zentimeter  - (DB E_ETSTAMM- multiply of - TVOLN)." )
	private String volume;

	@ApiModelProperty(value ="Internes Verrechnungskennzeichen - (DB E_ETSTAMM- 215-INTVKZ)." )
	private String internalAllocationIndicator;

	@ApiModelProperty(value ="Anzahl Bestand am Jahresanfang (2 Dezimalstellen) - (DB E_ETSTAMM- 216-IVBGJS)." )
	private String inventoryAtYearStart;
	
	@ApiModelProperty(value ="Part label information." )
	private PartLabelDTO partLableInfo;
	
	@ApiModelProperty(value ="Rabettsatz - (DB E_RAB - SATZ)." )
	private String discountGroupPercentageValue;
	
	@ApiModelProperty(value ="verkaufspreis mit MwSt - (DB REFERENZ - E_ETSTAMM.EPR + MWST_SATZ)." )
	private String salesPriceWithVAT;
	
	@ApiModelProperty(value =" (DB ETK_TLEKAT - TLE_LAZ)." )
	private String storageValue;
	
	@ApiModelProperty(value =" (DB ETK_TLEKAT - TLE_IMCO)." )
	private String imcoNumber;
	
	@ApiModelProperty(value ="LagerName  - (DB E_ETSTAMK4- PNAME)." )
	private String warehouseName;
	
	@ApiModelProperty(value ="Preisupdate durch Marketingcode gesperrt  - (DB E_ETSTAMM - MC - OMCGETCL)." )
	private String priceUpdateByMC;
	
	@ApiModelProperty(value =" part price  + surcharge percentage" )
	private String purchasePriceWithSurcharge;
	
	@ApiModelProperty(value ="Listenpreis mit MwSt - (DB REFERENZ - E_ETSTAMM.EPR + MWST)." )
	private String purchasePriceWithVAT;
	
	@ApiModelProperty(value = "Container size from - (DB O_OPARTTRANS - CONTAINER_SIZE_FROM)") 
	private String containerSizeFrom;
	

	public String getOem() {
		return oem;
	}

	public void setOem(String oem) {
		this.oem = oem;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	public String getStorageIndikator() {
		return storageIndikator;
	}

	public void setStorageIndikator(String storageIndikator) {
		this.storageIndikator = storageIndikator;
	}

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStorageLocation() {
		return storageLocation;
	}

	public void setStorageLocation(String storageLocation) {
		this.storageLocation = storageLocation;
	}

	public String getDeliverIndicator() {
		return deliverIndicator;
	}

	public void setDeliverIndicator(String deliverIndicator) {
		this.deliverIndicator = deliverIndicator;
	}

	public String getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(String purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public String getAverageNetPrice() {
		return averageNetPrice;
	}

	public void setAverageNetPrice(String averageNetPrice) {
		this.averageNetPrice = averageNetPrice;
	}

	public String getNetPrice() {
		return netPrice;
	}

	public void setNetPrice(String netPrice) {
		this.netPrice = netPrice;
	}

	public String getDeposit() {
		return deposit;
	}

	public void setDeposit(String deposit) {
		this.deposit = deposit;
	}

	public String getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(String returnValue) {
		this.returnValue = returnValue;
	}

	public String getLastPurchasePrice() {
		return lastPurchasePrice;
	}

	public void setLastPurchasePrice(String lastPurchasePrice) {
		this.lastPurchasePrice = lastPurchasePrice;
	}

	public String getDisposalCostEuro() {
		return disposalCostEuro;
	}

	public void setDisposalCostEuro(String disposalCostEuro) {
		this.disposalCostEuro = disposalCostEuro;
	}

	public String getFuturePurchasePrice() {
		return futurePurchasePrice;
	}

	public void setFuturePurchasePrice(String futurePurchasePrice) {
		this.futurePurchasePrice = futurePurchasePrice;
	}

	public String getOldPurchasePrice() {
		return oldPurchasePrice;
	}

	public void setOldPurchasePrice(String oldPurchasePrice) {
		this.oldPurchasePrice = oldPurchasePrice;
	}

	public String getPreviousPurchasePrice() {
		return previousPurchasePrice;
	}

	public void setPreviousPurchasePrice(String previousPurchasePrice) {
		this.previousPurchasePrice = previousPurchasePrice;
	}

	public String getDisposalCost() {
		return disposalCost;
	}

	public void setDisposalCost(String disposalCost) {
		this.disposalCost = disposalCost;
	}

	public String getDiscountGroup() {
		return discountGroup;
	}

	public void setDiscountGroup(String discountGroup) {
		this.discountGroup = discountGroup;
	}

	public String getOemBrand() {
		return oemBrand;
	}

	public void setOemBrand(String oemBrand) {
		this.oemBrand = oemBrand;
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

	public String getMinimumStock() {
		return minimumStock;
	}

	public void setMinimumStock(String minimumStock) {
		this.minimumStock = minimumStock;
	}

	public String getMaximumStock() {
		return maximumStock;
	}

	public void setMaximumStock(String maximumStock) {
		this.maximumStock = maximumStock;
	}

	public String getSaftyStock() {
		return saftyStock;
	}

	public void setSaftyStock(String saftyStock) {
		this.saftyStock = saftyStock;
	}

	public String getCurrentInventoryCountedStock() {
		return currentInventoryCountedStock;
	}

	public void setCurrentInventoryCountedStock(String currentInventoryCountedStock) {
		this.currentInventoryCountedStock = currentInventoryCountedStock;
	}

	public String getCurrentStock() {
		return currentStock;
	}

	public void setCurrentStock(String currentStock) {
		this.currentStock = currentStock;
	}

	public String getPreviousInventoryCountedStock() {
		return previousInventoryCountedStock;
	}

	public void setPreviousInventoryCountedStock(String previousInventoryCountedStock) {
		this.previousInventoryCountedStock = previousInventoryCountedStock;
	}

	public String getSalesStockCurrentMonth() {
		return salesStockCurrentMonth;
	}

	public void setSalesStockCurrentMonth(String salesStockCurrentMonth) {
		this.salesStockCurrentMonth = salesStockCurrentMonth;
	}

	public List<MonthlyDecimalValue> getCurrentYearMonthlySalesStock() {
		return currentYearMonthlySalesStock;
	}

	public void setCurrentYearMonthlySalesStock(List<MonthlyDecimalValue> currentYearMonthlySalesStock) {
		this.currentYearMonthlySalesStock = currentYearMonthlySalesStock;
	}

	public String getSalesStockAverageCurrentYear() {
		return salesStockAverageCurrentYear;
	}

	public void setSalesStockAverageCurrentYear(String salesStockAverageCurrentYear) {
		this.salesStockAverageCurrentYear = salesStockAverageCurrentYear;
	}

	public List<MonthlyDecimalValue> getLastYearMonthlySalesStock() {
		return lastYearMonthlySalesStock;
	}

	public void setLastYearMonthlySalesStock(List<MonthlyDecimalValue> lastYearMonthlySalesStock) {
		this.lastYearMonthlySalesStock = lastYearMonthlySalesStock;
	}

	public String getSalesStockLastYear() {
		return salesStockLastYear;
	}

	public void setSalesStockLastYear(String salesStockLastYear) {
		this.salesStockLastYear = salesStockLastYear;
	}

	public String getCurrentYearSalesStock() {
		return currentYearSalesStock;
	}

	public void setCurrentYearSalesStock(String currentYearSalesStock) {
		this.currentYearSalesStock = currentYearSalesStock;
	}

	public String getCurrentMonthReceiptStock() {
		return currentMonthReceiptStock;
	}

	public void setCurrentMonthReceiptStock(String currentMonthReceiptStock) {
		this.currentMonthReceiptStock = currentMonthReceiptStock;
	}

	public List<MonthlyDecimalValue> getMonthlyReceiptsStock() {
		return monthlyReceiptsStock;
	}

	public void setMonthlyReceiptsStock(List<MonthlyDecimalValue> monthlyReceiptsStock) {
		this.monthlyReceiptsStock = monthlyReceiptsStock;
	}

	public String getCurrentYearAverageReceiptStock() {
		return currentYearAverageReceiptStock;
	}

	public void setCurrentYearAverageReceiptStock(String currentYearAverageReceiptStock) {
		this.currentYearAverageReceiptStock = currentYearAverageReceiptStock;
	}

	public String getLastYearReceiptStock() {
		return lastYearReceiptStock;
	}

	public void setLastYearReceiptStock(String lastYearReceiptStock) {
		this.lastYearReceiptStock = lastYearReceiptStock;
	}

	public String getCurrentYearReceiptStock() {
		return currentYearReceiptStock;
	}

	public void setCurrentYearReceiptStock(String currentYearReceiptStock) {
		this.currentYearReceiptStock = currentYearReceiptStock;
	}

	public String getCurrentMonthSales() {
		return currentMonthSales;
	}

	public void setCurrentMonthSales(String currentMonthSales) {
		this.currentMonthSales = currentMonthSales;
	}

	public List<MonthlyDecimalValue> getCurrentYearMonthlySales() {
		return currentYearMonthlySales;
	}

	public void setCurrentYearMonthlySales(List<MonthlyDecimalValue> currentYearMonthlySales) {
		this.currentYearMonthlySales = currentYearMonthlySales;
	}

	public String getAverageSales() {
		return averageSales;
	}

	public void setAverageSales(String averageSales) {
		this.averageSales = averageSales;
	}

	public List<MonthlyDecimalValue> getLastYearMonthlySales() {
		return lastYearMonthlySales;
	}

	public void setLastYearMonthlySales(List<MonthlyDecimalValue> lastYearMonthlySales) {
		this.lastYearMonthlySales = lastYearMonthlySales;
	}

	public String getLastYearSales() {
		return lastYearSales;
	}

	public void setLastYearSales(String lastYearSales) {
		this.lastYearSales = lastYearSales;
	}

	public String getCurrentYearSales() {
		return currentYearSales;
	}

	public void setCurrentYearSales(String currentYearSales) {
		this.currentYearSales = currentYearSales;
	}

	public String getCurrentMonthReceiptInEuro() {
		return currentMonthReceiptInEuro;
	}

	public void setCurrentMonthReceiptInEuro(String currentMonthReceiptInEuro) {
		this.currentMonthReceiptInEuro = currentMonthReceiptInEuro;
	}

	public List<MonthlyDecimalValue> getMonthlyReceiptsInEuro() {
		return monthlyReceiptsInEuro;
	}

	public void setMonthlyReceiptsInEuro(List<MonthlyDecimalValue> monthlyReceiptsInEuro) {
		this.monthlyReceiptsInEuro = monthlyReceiptsInEuro;
	}

	public String getAverageReceiptInEuro() {
		return averageReceiptInEuro;
	}

	public void setAverageReceiptInEuro(String averageReceiptInEuro) {
		this.averageReceiptInEuro = averageReceiptInEuro;
	}

	public String getLastYearReceiptInEuro() {
		return lastYearReceiptInEuro;
	}

	public void setLastYearReceiptInEuro(String lastYearReceiptInEuro) {
		this.lastYearReceiptInEuro = lastYearReceiptInEuro;
	}

	public String getCurrentYearReceiptInEuro() {
		return currentYearReceiptInEuro;
	}

	public void setCurrentYearReceiptInEuro(String currentYearReceiptInEuro) {
		this.currentYearReceiptInEuro = currentYearReceiptInEuro;
	}

	public String getPendingOrders() {
		return pendingOrders;
	}

	public void setPendingOrders(String pendingOrders) {
		this.pendingOrders = pendingOrders;
	}

	public String getMinimumOrderQuantity() {
		return minimumOrderQuantity;
	}

	public void setMinimumOrderQuantity(String minimumOrderQuantity) {
		this.minimumOrderQuantity = minimumOrderQuantity;
	}

	public String getCustomerBacklog() {
		return customerBacklog;
	}

	public void setCustomerBacklog(String customerBacklog) {
		this.customerBacklog = customerBacklog;
	}

	public String getDeliveryBacklog() {
		return deliveryBacklog;
	}

	public void setDeliveryBacklog(String deliveryBacklog) {
		this.deliveryBacklog = deliveryBacklog;
	}

	public String getSalesAmount() {
		return salesAmount;
	}

	public void setSalesAmount(String salesAmount) {
		this.salesAmount = salesAmount;
	}

	public String getRequestedQuantity() {
		return requestedQuantity;
	}

	public void setRequestedQuantity(String requestedQuantity) {
		this.requestedQuantity = requestedQuantity;
	}

	public String getBookingAmount() {
		return bookingAmount;
	}

	public void setBookingAmount(String bookingAmount) {
		this.bookingAmount = bookingAmount;
	}

	public String getRequestedAmount() {
		return requestedAmount;
	}

	public void setRequestedAmount(String requestedAmount) {
		this.requestedAmount = requestedAmount;
	}

	public List<MonthlyIntValue> getCurrentYearMonthlyRequestedAmounts() {
		return currentYearMonthlyRequestedAmounts;
	}

	public void setCurrentYearMonthlyRequestedAmounts(List<MonthlyIntValue> currentYearMonthlyRequestedAmounts) {
		this.currentYearMonthlyRequestedAmounts = currentYearMonthlyRequestedAmounts;
	}

	public List<MonthlyIntValue> getLastYearMonthlyRequestedAmounts() {
		return lastYearMonthlyRequestedAmounts;
	}

	public void setLastYearMonthlyRequestedAmounts(List<MonthlyIntValue> lastYearMonthlyRequestedAmounts) {
		this.lastYearMonthlyRequestedAmounts = lastYearMonthlyRequestedAmounts;
	}

	public String getServiceProposal() {
		return serviceProposal;
	}

	public void setServiceProposal(String serviceProposal) {
		this.serviceProposal = serviceProposal;
	}

	public String getInventoryAmount() {
		return inventoryAmount;
	}

	public void setInventoryAmount(String inventoryAmount) {
		this.inventoryAmount = inventoryAmount;
	}

	public String getOrderAnnotation() {
		return orderAnnotation;
	}

	public void setOrderAnnotation(String orderAnnotation) {
		this.orderAnnotation = orderAnnotation;
	}

	public String getCalculationLock() {
		return calculationLock;
	}

	public void setCalculationLock(String calculationLock) {
		this.calculationLock = calculationLock;
	}

	public String getCompartsMark() {
		return compartsMark;
	}

	public void setCompartsMark(String compartsMark) {
		this.compartsMark = compartsMark;
	}

	public String getPriceMark() {
		return priceMark;
	}

	public void setPriceMark(String priceMark) {
		this.priceMark = priceMark;
	}

	public String getDeletionMark() {
		return deletionMark;
	}

	public void setDeletionMark(String deletionMark) {
		this.deletionMark = deletionMark;
	}

	public String getDispoMark() {
		return dispoMark;
	}

	public void setDispoMark(String dispoMark) {
		this.dispoMark = dispoMark;
	}

	public String getInventorygMark() {
		return inventorygMark;
	}

	public void setInventorygMark(String inventorygMark) {
		this.inventorygMark = inventorygMark;
	}

	public String getBarcodeNumber() {
		return barcodeNumber;
	}

	public void setBarcodeNumber(String barcodeNumber) {
		this.barcodeNumber = barcodeNumber;
	}

	public String getSurchargeDeduction() {
		return surchargeDeduction;
	}

	public void setSurchargeDeduction(String surchargeDeduction) {
		this.surchargeDeduction = surchargeDeduction;
	}

	public String getSurchargeDeductionMark() {
		return surchargeDeductionMark;
	}

	public void setSurchargeDeductionMark(String surchargeDeductionMark) {
		this.surchargeDeductionMark = surchargeDeductionMark;
	}

	public String getCampaignMark() {
		return campaignMark;
	}

	public void setCampaignMark(String campaignMark) {
		this.campaignMark = campaignMark;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getLastDisposalDate() {
		return lastDisposalDate;
	}

	public void setLastDisposalDate(String lastDisposalDate) {
		this.lastDisposalDate = lastDisposalDate;
	}

	public String getLastReceiptDate() {
		return lastReceiptDate;
	}

	public void setLastReceiptDate(String lastReceiptDate) {
		this.lastReceiptDate = lastReceiptDate;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getLastOrderDate() {
		return lastOrderDate;
	}

	public void setLastOrderDate(String lastOrderDate) {
		this.lastOrderDate = lastOrderDate;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getFuturePriceFromDate() {
		return futurePriceFromDate;
	}

	public void setFuturePriceFromDate(String futurePriceFromDate) {
		this.futurePriceFromDate = futurePriceFromDate;
	}

	public String getLastMovementDate() {
		return lastMovementDate;
	}

	public void setLastMovementDate(String lastMovementDate) {
		this.lastMovementDate = lastMovementDate;
	}

	public String getInventoryDate() {
		return inventoryDate;
	}

	public void setInventoryDate(String inventoryDate) {
		this.inventoryDate = inventoryDate;
	}

	public String getLastDispositionDate() {
		return lastDispositionDate;
	}

	public void setLastDispositionDate(String lastDispositionDate) {
		this.lastDispositionDate = lastDispositionDate;
	}

	public String getMarketingCode() {
		return marketingCode;
	}

	public void setMarketingCode(String marketingCode) {
		this.marketingCode = marketingCode;
	}

	public String getSortingFormatPartNumber() {
		return sortingFormatPartNumber;
	}

	public void setSortingFormatPartNumber(String sortingFormatPartNumber) {
		this.sortingFormatPartNumber = sortingFormatPartNumber;
	}

	public String getPrintingFormatPartNumber() {
		return printingFormatPartNumber;
	}

	public void setPrintingFormatPartNumber(String printingFormatPartNumber) {
		this.printingFormatPartNumber = printingFormatPartNumber;
	}

	public String getPredecessorPartNumber() {
		return predecessorPartNumber;
	}

	public void setPredecessorPartNumber(String predecessorPartNumber) {
		this.predecessorPartNumber = predecessorPartNumber;
	}

	public String getSuccessorPartNumber() {
		return successorPartNumber;
	}

	public void setSuccessorPartNumber(String successorPartNumber) {
		this.successorPartNumber = successorPartNumber;
	}

	public String getPartNumberMarketplace() {
		return partNumberMarketplace;
	}

	public void setPartNumberMarketplace(String partNumberMarketplace) {
		this.partNumberMarketplace = partNumberMarketplace;
	}

	public String getMarketplaceStockNumber() {
		return marketplaceStockNumber;
	}

	public void setMarketplaceStockNumber(String marketplaceStockNumber) {
		this.marketplaceStockNumber = marketplaceStockNumber;
	}

	public String getOemPartNumber() {
		return oemPartNumber;
	}

	public void setOemPartNumber(String oemPartNumber) {
		this.oemPartNumber = oemPartNumber;
	}

	public String getcCodeHint() {
		return cCodeHint;
	}

	public void setcCodeHint(String cCodeHint) {
		this.cCodeHint = cCodeHint;
	}

	public String getPackagingUnit1() {
		return packagingUnit1;
	}

	public void setPackagingUnit1(String packagingUnit1) {
		this.packagingUnit1 = packagingUnit1;
	}

	public String getPackagingUnit2() {
		return packagingUnit2;
	}

	public void setPackagingUnit2(String packagingUnit2) {
		this.packagingUnit2 = packagingUnit2;
	}

	public String getDeletionMark2() {
		return deletionMark2;
	}

	public void setDeletionMark2(String deletionMark2) {
		this.deletionMark2 = deletionMark2;
	}

	public String getPartNumberIdentificationMark() {
		return partNumberIdentificationMark;
	}

	public void setPartNumberIdentificationMark(String partNumberIdentificationMark) {
		this.partNumberIdentificationMark = partNumberIdentificationMark;
	}

	public String getTheftMark() {
		return theftMark;
	}

	public void setTheftMark(String theftMark) {
		this.theftMark = theftMark;
	}

	public String getHazardousMaterialMark() {
		return hazardousMaterialMark;
	}

	public void setHazardousMaterialMark(String hazardousMaterialMark) {
		this.hazardousMaterialMark = hazardousMaterialMark;
	}

	public String getExchangeMark() {
		return exchangeMark;
	}

	public void setExchangeMark(String exchangeMark) {
		this.exchangeMark = exchangeMark;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getOldVolume() {
		return oldVolume;
	}

	public void setOldVolume(String oldVolume) {
		this.oldVolume = oldVolume;
	}

	public String getDiscountLockMark() {
		return discountLockMark;
	}

	public void setDiscountLockMark(String discountLockMark) {
		this.discountLockMark = discountLockMark;
	}

	public String getSubmissionMark() {
		return submissionMark;
	}

	public void setSubmissionMark(String submissionMark) {
		this.submissionMark = submissionMark;
	}

	public String getTradeGoodsNumber() {
		return tradeGoodsNumber;
	}

	public void setTradeGoodsNumber(String tradeGoodsNumber) {
		this.tradeGoodsNumber = tradeGoodsNumber;
	}

	public String getQuantityUnit() {
		return quantityUnit;
	}

	public void setQuantityUnit(String quantityUnit) {
		this.quantityUnit = quantityUnit;
	}

	public String getInventoryGroup() {
		return inventoryGroup;
	}

	public void setInventoryGroup(String inventoryGroup) {
		this.inventoryGroup = inventoryGroup;
	}

	public String getStockageMark() {
		return stockageMark;
	}

	public void setStockageMark(String stockageMark) {
		this.stockageMark = stockageMark;
	}

	public String getReturnPermittedMark() {
		return returnPermittedMark;
	}

	public void setReturnPermittedMark(String returnPermittedMark) {
		this.returnPermittedMark = returnPermittedMark;
	}

	public String getPreferenceMark() {
		return preferenceMark;
	}

	public void setPreferenceMark(String preferenceMark) {
		this.preferenceMark = preferenceMark;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getSalesLockMark() {
		return salesLockMark;
	}

	public void setSalesLockMark(String salesLockMark) {
		this.salesLockMark = salesLockMark;
	}

	public String getMainSupplier() {
		return mainSupplier;
	}

	public void setMainSupplier(String mainSupplier) {
		this.mainSupplier = mainSupplier;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getInternalAllocationIndicator() {
		return internalAllocationIndicator;
	}

	public void setInternalAllocationIndicator(String internalAllocationIndicator) {
		this.internalAllocationIndicator = internalAllocationIndicator;
	}

	public String getInventoryAtYearStart() {
		return inventoryAtYearStart;
	}

	public void setInventoryAtYearStart(String inventoryAtYearStart) {
		this.inventoryAtYearStart = inventoryAtYearStart;
	}
	
	public PartLabelDTO getPartLableInfo() {
		return partLableInfo;
	}

	public void setPartLableInfo(PartLabelDTO partLableInfo) {
		this.partLableInfo = partLableInfo;
	}

	public String getDiscountGroupPercentageValue() {
		return discountGroupPercentageValue;
	}

	public void setDiscountGroupPercentageValue(String discountGroupPercentageValue) {
		this.discountGroupPercentageValue = discountGroupPercentageValue;
	}

	public String getSalesPriceWithVAT() {
		return salesPriceWithVAT;
	}

	public void setSalesPriceWithVAT(String salesPriceWithVAT) {
		this.salesPriceWithVAT = salesPriceWithVAT;
	}

	public String getStorageValue() {
		return storageValue;
	}

	public void setStorageValue(String storageValue) {
		this.storageValue = storageValue;
	}

	public String getImcoNumber() {
		return imcoNumber;
	}

	public void setImcoNumber(String imcoNumber) {
		this.imcoNumber = imcoNumber;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public String getPriceUpdateByMC() {
		return priceUpdateByMC;
	}

	public void setPriceUpdateByMC(String priceUpdateByMC) {
		this.priceUpdateByMC = priceUpdateByMC;
	}

	/**
	 * @return the purchasePriceWithSurcharge
	 */
	public String getPurchasePriceWithSurcharge() {
		return purchasePriceWithSurcharge;
	}

	/**
	 * @param purchasePriceWithSurcharge the purchasePriceWithSurcharge to set
	 */
	public void setPurchasePriceWithSurcharge(String purchasePriceWithSurcharge) {
		this.purchasePriceWithSurcharge = purchasePriceWithSurcharge;
	}

	/**
	 * @return the purchasePriceWithVAT
	 */
	public String getPurchasePriceWithVAT() {
		return purchasePriceWithVAT;
	}

	/**
	 * @param purchasePriceWithVAT the purchasePriceWithVAT to set
	 */
	public void setPurchasePriceWithVAT(String purchasePriceWithVAT) {
		this.purchasePriceWithVAT = purchasePriceWithVAT;
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