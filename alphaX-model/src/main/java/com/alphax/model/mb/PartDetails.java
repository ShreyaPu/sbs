package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

public class PartDetails {

	@DBTable(columnName = "HERST", required = true)
	private String oem;

	@DBTable(columnName = "TNR", required = true)
	private String partNumber;

	@DBTable(columnName = "LNR", required = true)
	private BigDecimal warehouse;

	@DBTable(columnName = "SA", required = true)
	private BigDecimal storageIndikator;

	@DBTable(columnName = "TA", required = true)
	private BigDecimal partsIndikator;

	@DBTable(columnName = "LEIART", required = true)
	private BigDecimal activityType;

	@DBTable(columnName = "BENEN", required = true)
	private String name;

	@DBTable(columnName = "LOPA", required = true)
	private String storageLocation;

	@DBTable(columnName = "LIWERK", required = true)
	private BigDecimal deliverIndicator;

	@DBTable(columnName = "EPR", required = true)
	private BigDecimal purchasePrice;

	@DBTable(columnName = "DAK", required = true)
	private BigDecimal averageNetPrice;

	@DBTable(columnName = "EKNPR", required = true)
	private BigDecimal netPrice;

	@DBTable(columnName = "PFAND", required = true)
	private BigDecimal deposit;

	@DBTable(columnName = "TRUWER", required = true)
	private BigDecimal returnValue;

	@DBTable(columnName = "LEKPR", required = true)
	private BigDecimal lastPurchasePrice;

	@DBTable(columnName = "ENTSOW", required = true)
	private BigDecimal disposalCostEuro;

	@DBTable(columnName = "ZPREIS", required = true)
	private BigDecimal futurePurchasePrice;

	@DBTable(columnName = "APREIS", required = true)
	private BigDecimal oldPurchasePrice;

	@DBTable(columnName = "NPREIS", required = true)
	private BigDecimal previousPurchasePrice;

	@DBTable(columnName = "ENTSOP", required = true)
	private BigDecimal disposalCost;

	@DBTable(columnName = "RG", required = true)
	private String discountGroup;

	@DBTable(columnName = "TMARKE", required = true)
	private String oemBrand;

	@DBTable(columnName = "MWST", required = true)
	private BigDecimal valueAddedTax;

	@DBTable(columnName = "SKL", required = true)
	private String commonPartWithUnimog;

	@DBTable(columnName = "MINBES", required = true)
	private BigDecimal minimumStock;

	@DBTable(columnName = "MAXBES", required = true)
	private BigDecimal maximumStock;

	@DBTable(columnName = "SIBES", required = true)
	private BigDecimal saftyStock;

	@DBTable(columnName = "IVZME", required = true)
	private BigDecimal currentInventoryCountedStock;

	@DBTable(columnName = "AKTBES", required = true)
	private BigDecimal currentStock;

	@DBTable(columnName = "LIVBES", required = true)
	private BigDecimal previousInventoryCountedStock;

	@DBTable(columnName = "VKLFMS", required = true)
	private BigDecimal salesStockCurrentMonth;

	@DBTable(columnName ="VKLJ1S", required = true)
	private BigDecimal currentYearMonthlySalesStock01;
	
	@DBTable(columnName ="VKLJ2S", required = true)
	private BigDecimal currentYearMonthlySalesStock02;
	
	@DBTable(columnName ="VKLJ3S", required = true)
	private BigDecimal currentYearMonthlySalesStock03;
	
	@DBTable(columnName ="VKLJ4S", required = true)
	private BigDecimal currentYearMonthlySalesStock04;
	
	@DBTable(columnName ="VKLJ5S", required = true)
	private BigDecimal currentYearMonthlySalesStock05;
	
	@DBTable(columnName ="VKLJ6S", required = true)
	private BigDecimal currentYearMonthlySalesStock06;
	
	@DBTable(columnName ="VKLJ7S", required = true)
	private BigDecimal currentYearMonthlySalesStock07;
	
	@DBTable(columnName ="VKLJ8S", required = true)
	private BigDecimal currentYearMonthlySalesStock08;
	
	@DBTable(columnName ="VKLJ9S", required = true)
	private BigDecimal currentYearMonthlySalesStock09;
	
	@DBTable(columnName ="VKLJAS", required = true)
	private BigDecimal currentYearMonthlySalesStock10;
	
	@DBTable(columnName ="VKLJBS", required = true)
	private BigDecimal currentYearMonthlySalesStock11;
	
	@DBTable(columnName ="VKLJCS", required = true)
	private BigDecimal currentYearMonthlySalesStock12;
	

	@DBTable(columnName ="VKAVGS", required = true)
	private BigDecimal salesStockAverageCurrentYear ;

	@DBTable(columnName = "VKVJ1S", required = true)
	private BigDecimal lastYearMonthlySalesStock01;
	
	@DBTable(columnName = "VKVJ2S", required = true)
	private BigDecimal lastYearMonthlySalesStock02;
	
	@DBTable(columnName = "VKVJ3S", required = true)
	private BigDecimal lastYearMonthlySalesStock03;
	
	@DBTable(columnName = "VKVJ4S", required = true)
	private BigDecimal lastYearMonthlySalesStock04;
	
	@DBTable(columnName = "VKVJ5S", required = true)
	private BigDecimal lastYearMonthlySalesStock05;
	
	@DBTable(columnName = "VKVJ6S", required = true)
	private BigDecimal lastYearMonthlySalesStock06;
	
	@DBTable(columnName = "VKVJ7S", required = true)
	private BigDecimal lastYearMonthlySalesStock07;
	
	@DBTable(columnName = "VKVJ8S", required = true)
	private BigDecimal lastYearMonthlySalesStock08;
	
	@DBTable(columnName = "VKVJ9S", required = true)
	private BigDecimal lastYearMonthlySalesStock09;
	
	@DBTable(columnName = "VKVJAS", required = true)
	private BigDecimal lastYearMonthlySalesStock10;
	
	@DBTable(columnName = "VKVJBS", required = true)
	private BigDecimal lastYearMonthlySalesStock11;
	
	@DBTable(columnName = "VKVJCS", required = true)
	private BigDecimal lastYearMonthlySalesStock12;
	
	@DBTable(columnName = "VKSVJS", required = true)
	private BigDecimal salesStockLastYear;

	@DBTable(columnName = "VKLFJS", required = true)
	private BigDecimal currentYearSalesStock;

	@DBTable(columnName = "ZULFMS", required = true)
	private BigDecimal currentMonthReceiptStock;

	@DBTable(columnName = "ZULM1S", required = true)
	private BigDecimal monthlyReceiptsStock01;
	
	@DBTable(columnName = "ZULM2S", required = true)
	private BigDecimal monthlyReceiptsStock02;
	
	@DBTable(columnName = "ZULM3S", required = true)
	private BigDecimal monthlyReceiptsStock03;
	
	@DBTable(columnName = "ZULM4S", required = true)
	private BigDecimal monthlyReceiptsStock04;
	
	@DBTable(columnName = "ZULM5S", required = true)
	private BigDecimal monthlyReceiptsStock05;
	
	@DBTable(columnName = "ZULM6S", required = true)
	private BigDecimal monthlyReceiptsStock06;
	
	@DBTable(columnName = "ZULM7S", required = true)
	private BigDecimal monthlyReceiptsStock07;
	
	@DBTable(columnName = "ZULM8S", required = true)
	private BigDecimal monthlyReceiptsStock08;
	
	@DBTable(columnName = "ZULM9S", required = true)
	private BigDecimal monthlyReceiptsStock09;
	
	@DBTable(columnName = "ZULMAS", required = true)
	private BigDecimal monthlyReceiptsStock10;
	
	@DBTable(columnName = "ZULMBS", required = true)
	private BigDecimal monthlyReceiptsStock11;
	
	@DBTable(columnName = "ZULMCS", required = true)
	private BigDecimal monthlyReceiptsStock12;

	@DBTable(columnName = "ZUAVGS", required = true)
	private BigDecimal currentYearAverageReceiptStock;

	@DBTable(columnName = "ZUSVJS", required = true)
	private BigDecimal lastYearReceiptStock;

	@DBTable(columnName = "ZULFJS", required = true)
	private BigDecimal currentYearReceiptStock;

	@DBTable(columnName = "VKLFMW", required = true)
	private BigDecimal currentMonthSales;

	@DBTable(columnName = "VKLJ1W", required = true)
	private BigDecimal currentYearMonthlySales01;
	
	@DBTable(columnName = "VKLJ2W", required = true)
	private BigDecimal currentYearMonthlySales02;
	
	@DBTable(columnName = "VKLJ3W", required = true)
	private BigDecimal currentYearMonthlySales03;
	
	@DBTable(columnName = "VKLJ4W", required = true)
	private BigDecimal currentYearMonthlySales04;
	
	@DBTable(columnName = "VKLJ5W", required = true)
	private BigDecimal currentYearMonthlySales05;
	
	@DBTable(columnName = "VKLJ6W", required = true)
	private BigDecimal currentYearMonthlySales06;
	
	@DBTable(columnName = "VKLJ7W", required = true)
	private BigDecimal currentYearMonthlySales07;
	
	@DBTable(columnName = "VKLJ8W", required = true)
	private BigDecimal currentYearMonthlySales08;
	
	@DBTable(columnName = "VKLJ9W", required = true)
	private BigDecimal currentYearMonthlySales09;
	
	@DBTable(columnName = "VKLJAW", required = true)
	private BigDecimal currentYearMonthlySales10;
	
	@DBTable(columnName = "VKLJBW", required = true)
	private BigDecimal currentYearMonthlySales11;
	
	@DBTable(columnName = "VKLJCW", required = true)
	private BigDecimal currentYearMonthlySales12;
	
	@DBTable(columnName = "VKAVGW", required = true)
	private BigDecimal averageSales;

	@DBTable(columnName = "VKVJ1W", required = true)
	private BigDecimal lastYearMonthlySales01;
	
	@DBTable(columnName = "VKVJ2W", required = true)
	private BigDecimal lastYearMonthlySales02;
	
	@DBTable(columnName = "VKVJ3W", required = true)
	private BigDecimal lastYearMonthlySales03;
	
	@DBTable(columnName = "VKVJ4W", required = true)
	private BigDecimal lastYearMonthlySales04;
	
	@DBTable(columnName = "VKVJ5W", required = true)
	private BigDecimal lastYearMonthlySales05;
	
	@DBTable(columnName = "VKVJ6W", required = true)
	private BigDecimal lastYearMonthlySales06;
	
	@DBTable(columnName = "VKVJ7W", required = true)
	private BigDecimal lastYearMonthlySales07;
	
	@DBTable(columnName = "VKVJ8W", required = true)
	private BigDecimal lastYearMonthlySales08;
	
	@DBTable(columnName = "VKVJ9W", required = true)
	private BigDecimal lastYearMonthlySales09;
	
	@DBTable(columnName = "VKVJAW", required = true)
	private BigDecimal lastYearMonthlySales10;
	
	@DBTable(columnName = "VKVJBW", required = true)
	private BigDecimal lastYearMonthlySales11;
	
	@DBTable(columnName = "VKVJCW", required = true)
	private BigDecimal lastYearMonthlySales12;

	@DBTable(columnName = "VKSVJW", required = true)
	private BigDecimal lastYearSales;

	@DBTable(columnName = "VKLFJW", required = true)
	private BigDecimal currentYearSales;

	@DBTable(columnName = "ZULFMW", required = true)
	private BigDecimal currentMonthReceiptInEuro;

	@DBTable(columnName = "ZULM1W", required = true)
	private BigDecimal monthlyReceiptsInEuro01;
	
	@DBTable(columnName = "ZULM2W", required = true)
	private BigDecimal monthlyReceiptsInEuro02;
	
	@DBTable(columnName = "ZULM3W", required = true)
	private BigDecimal monthlyReceiptsInEuro03;
	
	@DBTable(columnName = "ZULM4W", required = true)
	private BigDecimal monthlyReceiptsInEuro04;
	
	@DBTable(columnName = "ZULM5W", required = true)
	private BigDecimal monthlyReceiptsInEuro05;
	
	@DBTable(columnName = "ZULM6W", required = true)
	private BigDecimal monthlyReceiptsInEuro06;
	
	@DBTable(columnName = "ZULM7W", required = true)
	private BigDecimal monthlyReceiptsInEuro07;
	
	@DBTable(columnName = "ZULM8W", required = true)
	private BigDecimal monthlyReceiptsInEuro08;
	
	@DBTable(columnName = "ZULM9W", required = true)
	private BigDecimal monthlyReceiptsInEuro09;
	
	@DBTable(columnName = "ZULMAW", required = true)
	private BigDecimal monthlyReceiptsInEuro10;
	
	@DBTable(columnName = "ZULMBW", required = true)
	private BigDecimal monthlyReceiptsInEuro11;
	
	@DBTable(columnName = "ZULMCW", required = true)
	private BigDecimal monthlyReceiptsInEuro12;

	@DBTable(columnName = "ZUAVGW", required = true)
	private BigDecimal averageReceiptInEuro;

	@DBTable(columnName = "ZUSVJW", required = true)
	private BigDecimal lastYearReceiptInEuro;

	@DBTable(columnName = "ZULFJW", required = true)
	private BigDecimal currentYearReceiptInEuro;

	@DBTable(columnName = "BESAUS", required = true)
	private BigDecimal pendingOrders;

	@DBTable(columnName = "MIBESM", required = true)
	private BigDecimal minimumOrderQuantity;

	@DBTable(columnName = "KDRUE", required = true)
	private BigDecimal customerBacklog;

	@DBTable(columnName = "LIFRUE", required = true)
	private BigDecimal deliveryBacklog;

	@DBTable(columnName = "ANZVK", required = true)
	private BigDecimal salesAmount;

	@DBTable(columnName = "NFMGE", required = true)
	private BigDecimal requestedQuantity;

	@DBTable(columnName = "ZUMGE", required = true)
	private BigDecimal bookingAmount;

	@DBTable(columnName = "ANZNFS", required = true)
	private BigDecimal requestedAmount;

	@DBTable(columnName = "NFLJ01", required = true)
	private BigDecimal currentYearMonthlyRequestedAmounts01;
	
	@DBTable(columnName = "NFLJ02", required = true)
	private BigDecimal currentYearMonthlyRequestedAmounts02;
	
	@DBTable(columnName = "NFLJ03", required = true)
	private BigDecimal currentYearMonthlyRequestedAmounts03;
	
	@DBTable(columnName = "NFLJ04", required = true)
	private BigDecimal currentYearMonthlyRequestedAmounts04;
	
	@DBTable(columnName = "NFLJ05", required = true)
	private BigDecimal currentYearMonthlyRequestedAmounts05;
	
	@DBTable(columnName = "NFLJ06", required = true)
	private BigDecimal currentYearMonthlyRequestedAmounts06;
	
	@DBTable(columnName = "NFLJ07", required = true)
	private BigDecimal currentYearMonthlyRequestedAmounts07;
	
	@DBTable(columnName = "NFLJ08", required = true)
	private BigDecimal currentYearMonthlyRequestedAmounts08;
	
	@DBTable(columnName = "NFLJ09", required = true)
	private BigDecimal currentYearMonthlyRequestedAmounts09;
	
	@DBTable(columnName = "NFLJ10", required = true)
	private BigDecimal currentYearMonthlyRequestedAmounts10;
	
	@DBTable(columnName = "NFLJ11", required = true)
	private BigDecimal currentYearMonthlyRequestedAmounts11;
	
	@DBTable(columnName = "NFLJ12", required = true)
	private BigDecimal currentYearMonthlyRequestedAmounts12;
	

	@DBTable(columnName = "NFVJ01", required = true)
	private BigDecimal lastYearMonthlyRequestedAmounts01;
	
	@DBTable(columnName = "NFVJ02", required = true)
	private BigDecimal lastYearMonthlyRequestedAmounts02;
	
	@DBTable(columnName = "NFVJ03", required = true)
	private BigDecimal lastYearMonthlyRequestedAmounts03;
	
	@DBTable(columnName = "NFVJ04", required = true)
	private BigDecimal lastYearMonthlyRequestedAmounts04;
	
	@DBTable(columnName = "NFVJ05", required = true)
	private BigDecimal lastYearMonthlyRequestedAmounts05;
	
	@DBTable(columnName = "NFVJ06", required = true)
	private BigDecimal lastYearMonthlyRequestedAmounts06;
	
	@DBTable(columnName = "NFVJ07", required = true)
	private BigDecimal lastYearMonthlyRequestedAmounts07;
	
	@DBTable(columnName = "NFVJ08", required = true)
	private BigDecimal lastYearMonthlyRequestedAmounts08;
	
	@DBTable(columnName = "NFVJ09", required = true)
	private BigDecimal lastYearMonthlyRequestedAmounts09;
	
	@DBTable(columnName = "NFVJ10", required = true)
	private BigDecimal lastYearMonthlyRequestedAmounts10;
	
	@DBTable(columnName = "NFVJ11", required = true)
	private BigDecimal lastYearMonthlyRequestedAmounts11;
	
	@DBTable(columnName = "NFVJ12", required = true)
	private BigDecimal lastYearMonthlyRequestedAmounts12;

	@DBTable(columnName = "TVORMG", required = true)
	private BigDecimal serviceProposal;

	@DBTable(columnName = "VORHS", required = true)
	private BigDecimal inventoryAmount;

	@DBTable(columnName = "BESVER", required = true)
	private String orderAnnotation;

	@DBTable(columnName = "KSPKZ", required = true)
	private String calculationLock;

	@DBTable(columnName = "NC", required = true)
	private String compartsMark;

	@DBTable(columnName = "PRKZ", required = true)
	private String priceMark;

	@DBTable(columnName = "LKZ", required = true)
	private String deletionMark;

	@DBTable(columnName = "DISPO", required = true)
	private String dispoMark;

	@DBTable(columnName = "IVKZ", required = true)
	private String inventorygMark;

	@DBTable(columnName = "BARCDE", required = true)
	private BigDecimal barcodeNumber;

	@DBTable(columnName = "ABSCH", required = true)
	private BigDecimal surchargeDeduction;

	@DBTable(columnName = "KDABS", required = true)
	private BigDecimal surchargeDeductionMark;

	@DBTable(columnName = "WINTER", required = true)
	private String campaignMark;

	@DBTable(columnName = "MODUS", required = true)
	private String mode;

	@DBTable(columnName = "DTLABG", required = true)
	private BigDecimal lastDisposalDate;

	@DBTable(columnName = "DTLZUG", required = true)
	private BigDecimal lastReceiptDate;

	@DBTable(columnName = "DTANLA", required = true)
	private BigDecimal creationDate;

	@DBTable(columnName = "DTLBES", required = true)
	private BigDecimal lastOrderDate;

	@DBTable(columnName = "DTVERF", required = true)
	private BigDecimal expirationDate;

	@DBTable(columnName = "GPRDAT", required = true)
	private BigDecimal futurePriceFromDate;

	@DBTable(columnName = "DTLBEW", required = true)
	private BigDecimal lastMovementDate;

	@DBTable(columnName = "DTINV", required = true)
	private BigDecimal inventoryDate;

	@DBTable(columnName = "DATLDI", required = true)
	private BigDecimal lastDispositionDate;

	@DBTable(columnName = "MC", required = true)
	private String marketingCode;

	@DBTable(columnName = "TNRS", required = true)
	private String sortingFormatPartNumber;

	@DBTable(columnName = "TNRD", required = true)
	private String printingFormatPartNumber;

	@DBTable(columnName = "TNRV", required = true)
	private String predecessorPartNumber;

	@DBTable(columnName = "TNRN", required = true)
	private String successorPartNumber;

	@DBTable(columnName = "TNRM", required = true)
	private String partNumberMarketplace;

	@DBTable(columnName = "TNRMLN", required = true)
	private String marketplaceStockNumber;

	@DBTable(columnName = "TNRH", required = true)
	private String oemPartNumber;

	@DBTable(columnName = "TCODE", required = true)
	private String cCodeHint;

	@DBTable(columnName = "VERP1", required = true)
	private BigDecimal packagingUnit1;

	@DBTable(columnName = "VERP2", required = true)
	private BigDecimal packagingUnit2;

	@DBTable(columnName = "LKZ2", required = true)
	private String deletionMark2;

	@DBTable(columnName = "TIDENT", required = true)
	private String partNumberIdentificationMark;

	@DBTable(columnName = "DRTKZ", required = true)
	private String theftMark;

	@DBTable(columnName = "GFKZ", required = true)
	private String hazardousMaterialMark;

	@DBTable(columnName = "TAUKZ", required = true)
	private String exchangeMark;

	@DBTable(columnName = "GEWICH", required = true)
	private BigDecimal weight;

	@DBTable(columnName = "TVOL", required = true) //change
	private String oldVolume;

	@DBTable(columnName = "TRSPER", required = true)
	private String discountLockMark;

	@DBTable(columnName = "KZEINS", required = true)
	private String submissionMark;

	@DBTable(columnName = "THWSNR", required = true)
	private String tradeGoodsNumber;

	@DBTable(columnName = "ME", required = true)
	private String quantityUnit;

	@DBTable(columnName = "ZAEGRU", required = true)
	private String inventoryGroup;

	@DBTable(columnName = "KZBEVO", required = true)
	private String stockageMark;

	@DBTable(columnName = "KZRUCK", required = true)
	private String returnPermittedMark;
	
	@DBTable(columnName = "PRAFKZ", required = true)
	private String preferenceMark;

	@DBTable(columnName = "FIRMA", required = true)
	private BigDecimal company;

	@DBTable(columnName = "FILIAL", required = true)
	private BigDecimal branch;

	@DBTable(columnName = "SPKZV", required = true)
	private String salesLockMark;

	@DBTable(columnName = "LIEFNR", required = true)
	private String mainSupplier;

	@DBTable(columnName = "TVOLN", required = true)//change
	private String lengthWidthHeigth;

	/*@DBTable(columnName = "", required = true)
	private BigDecimal width;

	@DBTable(columnName = "", required = true)
	private BigDecimal heigth;*/

	@DBTable(columnName = "INTVKZ", required = true)
	private String internalAllocationIndicator;

	@DBTable(columnName = "IVBGJS", required = true)
	private BigDecimal inventoryAtYearStart;
	
	@DBTable(columnName = "LABEL", required = true)
	private String partLabel;
	
	@DBTable(columnName = "BUVORG", required = true)
	private String discountGroupValue;
	
	@DBTable(columnName = "SATZ", required = true)
	private BigDecimal discountGroupPercentageValue;
	
	@DBTable(columnName = "MWST_SATZ", required = true)
	private String discountValuePercentageMWST;
	
	@DBTable(columnName = "TLE_LAZ", required = true)
	private String storageValue;
	
	@DBTable(columnName = "TLE_IMCO", required = true)
	private String imcoNumber;
	
	@DBTable(columnName = "LagerName", required = true)
	private String lagerName;
	
	@DBTable(columnName = "orderNo", required = true)
	private String orderNumber;
	
	@DBTable(columnName = "EPREIS", required = true)
	private String oldPrice;
	
	@DBTable(columnName = "NEPREIS", required = true)
	private String netPurchasePrice;
	
	@DBTable(columnName = "TAX", required = true)
	private String taxValue;
	
	@DBTable(columnName = "GRUPPE", required = true)
	private String partDiscountGroup;
	
	@DBTable(columnName = "price_limit", required = true)
	private String priceLimit;
	
	@DBTable(columnName = "surcharge_percentage", required = true)
	private String surchargePercentage;
	
	@DBTable(columnName ="ROWNUMER", required = true)
	private Integer totalCount;
	
	@DBTable(columnName ="Leiferwerk", required = true)
	private String bodywork;
	
	@DBTable(columnName ="Teilart", required = true)
	private String partType;
	
	@DBTable(columnName = "AP_LNR", required = true)
	private String alphaPlusWarehouseId;
	
	@DBTable(columnName = "containerSizeFrom", required = true)
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

	public BigDecimal getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(BigDecimal warehouse) {
		this.warehouse = warehouse;
	}

	public BigDecimal getStorageIndikator() {
		return storageIndikator;
	}

	public void setStorageIndikator(BigDecimal storageIndikator) {
		this.storageIndikator = storageIndikator;
	}

	public BigDecimal getPartsIndikator() {
		return partsIndikator;
	}

	public void setPartsIndikator(BigDecimal partsIndikator) {
		this.partsIndikator = partsIndikator;
	}

	public BigDecimal getActivityType() {
		return activityType;
	}

	public void setActivityType(BigDecimal activityType) {
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

	public BigDecimal getDeliverIndicator() {
		return deliverIndicator;
	}

	public void setDeliverIndicator(BigDecimal deliverIndicator) {
		this.deliverIndicator = deliverIndicator;
	}

	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public BigDecimal getAverageNetPrice() {
		return averageNetPrice;
	}

	public void setAverageNetPrice(BigDecimal averageNetPrice) {
		this.averageNetPrice = averageNetPrice;
	}

	public BigDecimal getNetPrice() {
		return netPrice;
	}

	public void setNetPrice(BigDecimal netPrice) {
		this.netPrice = netPrice;
	}

	public BigDecimal getDeposit() {
		return deposit;
	}

	public void setDeposit(BigDecimal deposit) {
		this.deposit = deposit;
	}

	public BigDecimal getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(BigDecimal returnValue) {
		this.returnValue = returnValue;
	}

	public BigDecimal getLastPurchasePrice() {
		return lastPurchasePrice;
	}

	public void setLastPurchasePrice(BigDecimal lastPurchasePrice) {
		this.lastPurchasePrice = lastPurchasePrice;
	}

	public BigDecimal getDisposalCostEuro() {
		return disposalCostEuro;
	}

	public void setDisposalCostEuro(BigDecimal disposalCostEuro) {
		this.disposalCostEuro = disposalCostEuro;
	}

	public BigDecimal getFuturePurchasePrice() {
		return futurePurchasePrice;
	}

	public void setFuturePurchasePrice(BigDecimal futurePurchasePrice) {
		this.futurePurchasePrice = futurePurchasePrice;
	}

	public BigDecimal getOldPurchasePrice() {
		return oldPurchasePrice;
	}

	public void setOldPurchasePrice(BigDecimal oldPurchasePrice) {
		this.oldPurchasePrice = oldPurchasePrice;
	}

	public BigDecimal getPreviousPurchasePrice() {
		return previousPurchasePrice;
	}

	public void setPreviousPurchasePrice(BigDecimal previousPurchasePrice) {
		this.previousPurchasePrice = previousPurchasePrice;
	}

	public BigDecimal getDisposalCost() {
		return disposalCost;
	}

	public void setDisposalCost(BigDecimal disposalCost) {
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

	public BigDecimal getValueAddedTax() {
		return valueAddedTax;
	}

	public void setValueAddedTax(BigDecimal valueAddedTax) {
		this.valueAddedTax = valueAddedTax;
	}

	public String getCommonPartWithUnimog() {
		return commonPartWithUnimog;
	}

	public void setCommonPartWithUnimog(String commonPartWithUnimog) {
		this.commonPartWithUnimog = commonPartWithUnimog;
	}

	public BigDecimal getMinimumStock() {
		return minimumStock;
	}

	public void setMinimumStock(BigDecimal minimumStock) {
		this.minimumStock = minimumStock;
	}

	public BigDecimal getMaximumStock() {
		return maximumStock;
	}

	public void setMaximumStock(BigDecimal maximumStock) {
		this.maximumStock = maximumStock;
	}

	public BigDecimal getSaftyStock() {
		return saftyStock;
	}

	public void setSaftyStock(BigDecimal saftyStock) {
		this.saftyStock = saftyStock;
	}

	public BigDecimal getCurrentInventoryCountedStock() {
		return currentInventoryCountedStock;
	}

	public void setCurrentInventoryCountedStock(BigDecimal currentInventoryCountedStock) {
		this.currentInventoryCountedStock = currentInventoryCountedStock;
	}

	public BigDecimal getCurrentStock() {
		return currentStock;
	}

	public void setCurrentStock(BigDecimal currentStock) {
		this.currentStock = currentStock;
	}

	public BigDecimal getPreviousInventoryCountedStock() {
		return previousInventoryCountedStock;
	}

	public void setPreviousInventoryCountedStock(BigDecimal previousInventoryCountedStock) {
		this.previousInventoryCountedStock = previousInventoryCountedStock;
	}

	public BigDecimal getSalesStockCurrentMonth() {
		return salesStockCurrentMonth;
	}

	public void setSalesStockCurrentMonth(BigDecimal salesStockCurrentMonth) {
		this.salesStockCurrentMonth = salesStockCurrentMonth;
	}

	public BigDecimal getCurrentYearMonthlySalesStock01() {
		return currentYearMonthlySalesStock01;
	}

	public void setCurrentYearMonthlySalesStock01(BigDecimal currentYearMonthlySalesStock01) {
		this.currentYearMonthlySalesStock01 = currentYearMonthlySalesStock01;
	}

	public BigDecimal getCurrentYearMonthlySalesStock02() {
		return currentYearMonthlySalesStock02;
	}

	public void setCurrentYearMonthlySalesStock02(BigDecimal currentYearMonthlySalesStock02) {
		this.currentYearMonthlySalesStock02 = currentYearMonthlySalesStock02;
	}

	public BigDecimal getCurrentYearMonthlySalesStock03() {
		return currentYearMonthlySalesStock03;
	}

	public void setCurrentYearMonthlySalesStock03(BigDecimal currentYearMonthlySalesStock03) {
		this.currentYearMonthlySalesStock03 = currentYearMonthlySalesStock03;
	}

	public BigDecimal getCurrentYearMonthlySalesStock04() {
		return currentYearMonthlySalesStock04;
	}

	public void setCurrentYearMonthlySalesStock04(BigDecimal currentYearMonthlySalesStock04) {
		this.currentYearMonthlySalesStock04 = currentYearMonthlySalesStock04;
	}

	public BigDecimal getCurrentYearMonthlySalesStock05() {
		return currentYearMonthlySalesStock05;
	}

	public void setCurrentYearMonthlySalesStock05(BigDecimal currentYearMonthlySalesStock05) {
		this.currentYearMonthlySalesStock05 = currentYearMonthlySalesStock05;
	}

	public BigDecimal getCurrentYearMonthlySalesStock06() {
		return currentYearMonthlySalesStock06;
	}

	public void setCurrentYearMonthlySalesStock06(BigDecimal currentYearMonthlySalesStock06) {
		this.currentYearMonthlySalesStock06 = currentYearMonthlySalesStock06;
	}

	public BigDecimal getCurrentYearMonthlySalesStock07() {
		return currentYearMonthlySalesStock07;
	}

	public void setCurrentYearMonthlySalesStock07(BigDecimal currentYearMonthlySalesStock07) {
		this.currentYearMonthlySalesStock07 = currentYearMonthlySalesStock07;
	}

	public BigDecimal getCurrentYearMonthlySalesStock08() {
		return currentYearMonthlySalesStock08;
	}

	public void setCurrentYearMonthlySalesStock08(BigDecimal currentYearMonthlySalesStock08) {
		this.currentYearMonthlySalesStock08 = currentYearMonthlySalesStock08;
	}

	public BigDecimal getCurrentYearMonthlySalesStock09() {
		return currentYearMonthlySalesStock09;
	}

	public void setCurrentYearMonthlySalesStock09(BigDecimal currentYearMonthlySalesStock09) {
		this.currentYearMonthlySalesStock09 = currentYearMonthlySalesStock09;
	}

	public BigDecimal getCurrentYearMonthlySalesStock10() {
		return currentYearMonthlySalesStock10;
	}

	public void setCurrentYearMonthlySalesStock10(BigDecimal currentYearMonthlySalesStock10) {
		this.currentYearMonthlySalesStock10 = currentYearMonthlySalesStock10;
	}

	public BigDecimal getCurrentYearMonthlySalesStock11() {
		return currentYearMonthlySalesStock11;
	}

	public void setCurrentYearMonthlySalesStock11(BigDecimal currentYearMonthlySalesStock11) {
		this.currentYearMonthlySalesStock11 = currentYearMonthlySalesStock11;
	}

	public BigDecimal getCurrentYearMonthlySalesStock12() {
		return currentYearMonthlySalesStock12;
	}

	public void setCurrentYearMonthlySalesStock12(BigDecimal currentYearMonthlySalesStock12) {
		this.currentYearMonthlySalesStock12 = currentYearMonthlySalesStock12;
	}

	public BigDecimal getSalesStockAverageCurrentYear() {
		return salesStockAverageCurrentYear;
	}

	public void setSalesStockAverageCurrentYear(BigDecimal salesStockAverageCurrentYear) {
		this.salesStockAverageCurrentYear = salesStockAverageCurrentYear;
	}

	public BigDecimal getLastYearMonthlySalesStock01() {
		return lastYearMonthlySalesStock01;
	}

	public void setLastYearMonthlySalesStock01(BigDecimal lastYearMonthlySalesStock01) {
		this.lastYearMonthlySalesStock01 = lastYearMonthlySalesStock01;
	}

	public BigDecimal getLastYearMonthlySalesStock02() {
		return lastYearMonthlySalesStock02;
	}

	public void setLastYearMonthlySalesStock02(BigDecimal lastYearMonthlySalesStock02) {
		this.lastYearMonthlySalesStock02 = lastYearMonthlySalesStock02;
	}

	public BigDecimal getLastYearMonthlySalesStock03() {
		return lastYearMonthlySalesStock03;
	}

	public void setLastYearMonthlySalesStock03(BigDecimal lastYearMonthlySalesStock03) {
		this.lastYearMonthlySalesStock03 = lastYearMonthlySalesStock03;
	}

	public BigDecimal getLastYearMonthlySalesStock04() {
		return lastYearMonthlySalesStock04;
	}

	public void setLastYearMonthlySalesStock04(BigDecimal lastYearMonthlySalesStock04) {
		this.lastYearMonthlySalesStock04 = lastYearMonthlySalesStock04;
	}

	public BigDecimal getLastYearMonthlySalesStock05() {
		return lastYearMonthlySalesStock05;
	}

	public void setLastYearMonthlySalesStock05(BigDecimal lastYearMonthlySalesStock05) {
		this.lastYearMonthlySalesStock05 = lastYearMonthlySalesStock05;
	}

	public BigDecimal getLastYearMonthlySalesStock06() {
		return lastYearMonthlySalesStock06;
	}

	public void setLastYearMonthlySalesStock06(BigDecimal lastYearMonthlySalesStock06) {
		this.lastYearMonthlySalesStock06 = lastYearMonthlySalesStock06;
	}

	public BigDecimal getLastYearMonthlySalesStock07() {
		return lastYearMonthlySalesStock07;
	}

	public void setLastYearMonthlySalesStock07(BigDecimal lastYearMonthlySalesStock07) {
		this.lastYearMonthlySalesStock07 = lastYearMonthlySalesStock07;
	}

	public BigDecimal getLastYearMonthlySalesStock08() {
		return lastYearMonthlySalesStock08;
	}

	public void setLastYearMonthlySalesStock08(BigDecimal lastYearMonthlySalesStock08) {
		this.lastYearMonthlySalesStock08 = lastYearMonthlySalesStock08;
	}

	public BigDecimal getLastYearMonthlySalesStock09() {
		return lastYearMonthlySalesStock09;
	}

	public void setLastYearMonthlySalesStock09(BigDecimal lastYearMonthlySalesStock09) {
		this.lastYearMonthlySalesStock09 = lastYearMonthlySalesStock09;
	}

	public BigDecimal getLastYearMonthlySalesStock10() {
		return lastYearMonthlySalesStock10;
	}

	public void setLastYearMonthlySalesStock10(BigDecimal lastYearMonthlySalesStock10) {
		this.lastYearMonthlySalesStock10 = lastYearMonthlySalesStock10;
	}

	public BigDecimal getLastYearMonthlySalesStock11() {
		return lastYearMonthlySalesStock11;
	}

	public void setLastYearMonthlySalesStock11(BigDecimal lastYearMonthlySalesStock11) {
		this.lastYearMonthlySalesStock11 = lastYearMonthlySalesStock11;
	}

	public BigDecimal getLastYearMonthlySalesStock12() {
		return lastYearMonthlySalesStock12;
	}

	public void setLastYearMonthlySalesStock12(BigDecimal lastYearMonthlySalesStock12) {
		this.lastYearMonthlySalesStock12 = lastYearMonthlySalesStock12;
	}

	public BigDecimal getSalesStockLastYear() {
		return salesStockLastYear;
	}

	public void setSalesStockLastYear(BigDecimal salesStockLastYear) {
		this.salesStockLastYear = salesStockLastYear;
	}

	public BigDecimal getCurrentYearSalesStock() {
		return currentYearSalesStock;
	}

	public void setCurrentYearSalesStock(BigDecimal currentYearSalesStock) {
		this.currentYearSalesStock = currentYearSalesStock;
	}

	public BigDecimal getCurrentMonthReceiptStock() {
		return currentMonthReceiptStock;
	}

	public void setCurrentMonthReceiptStock(BigDecimal currentMonthReceiptStock) {
		this.currentMonthReceiptStock = currentMonthReceiptStock;
	}

	public BigDecimal getMonthlyReceiptsStock01() {
		return monthlyReceiptsStock01;
	}

	public void setMonthlyReceiptsStock01(BigDecimal monthlyReceiptsStock01) {
		this.monthlyReceiptsStock01 = monthlyReceiptsStock01;
	}

	public BigDecimal getMonthlyReceiptsStock02() {
		return monthlyReceiptsStock02;
	}

	public void setMonthlyReceiptsStock02(BigDecimal monthlyReceiptsStock02) {
		this.monthlyReceiptsStock02 = monthlyReceiptsStock02;
	}

	public BigDecimal getMonthlyReceiptsStock03() {
		return monthlyReceiptsStock03;
	}

	public void setMonthlyReceiptsStock03(BigDecimal monthlyReceiptsStock03) {
		this.monthlyReceiptsStock03 = monthlyReceiptsStock03;
	}

	public BigDecimal getMonthlyReceiptsStock04() {
		return monthlyReceiptsStock04;
	}

	public void setMonthlyReceiptsStock04(BigDecimal monthlyReceiptsStock04) {
		this.monthlyReceiptsStock04 = monthlyReceiptsStock04;
	}

	public BigDecimal getMonthlyReceiptsStock05() {
		return monthlyReceiptsStock05;
	}

	public void setMonthlyReceiptsStock05(BigDecimal monthlyReceiptsStock05) {
		this.monthlyReceiptsStock05 = monthlyReceiptsStock05;
	}

	public BigDecimal getMonthlyReceiptsStock06() {
		return monthlyReceiptsStock06;
	}

	public void setMonthlyReceiptsStock06(BigDecimal monthlyReceiptsStock06) {
		this.monthlyReceiptsStock06 = monthlyReceiptsStock06;
	}

	public BigDecimal getMonthlyReceiptsStock07() {
		return monthlyReceiptsStock07;
	}

	public void setMonthlyReceiptsStock07(BigDecimal monthlyReceiptsStock07) {
		this.monthlyReceiptsStock07 = monthlyReceiptsStock07;
	}

	public BigDecimal getMonthlyReceiptsStock08() {
		return monthlyReceiptsStock08;
	}

	public void setMonthlyReceiptsStock08(BigDecimal monthlyReceiptsStock08) {
		this.monthlyReceiptsStock08 = monthlyReceiptsStock08;
	}

	public BigDecimal getMonthlyReceiptsStock09() {
		return monthlyReceiptsStock09;
	}

	public void setMonthlyReceiptsStock09(BigDecimal monthlyReceiptsStock09) {
		this.monthlyReceiptsStock09 = monthlyReceiptsStock09;
	}

	public BigDecimal getMonthlyReceiptsStock10() {
		return monthlyReceiptsStock10;
	}

	public void setMonthlyReceiptsStock10(BigDecimal monthlyReceiptsStock10) {
		this.monthlyReceiptsStock10 = monthlyReceiptsStock10;
	}

	public BigDecimal getMonthlyReceiptsStock11() {
		return monthlyReceiptsStock11;
	}

	public void setMonthlyReceiptsStock11(BigDecimal monthlyReceiptsStock11) {
		this.monthlyReceiptsStock11 = monthlyReceiptsStock11;
	}

	public BigDecimal getMonthlyReceiptsStock12() {
		return monthlyReceiptsStock12;
	}

	public void setMonthlyReceiptsStock12(BigDecimal monthlyReceiptsStock12) {
		this.monthlyReceiptsStock12 = monthlyReceiptsStock12;
	}

	public BigDecimal getCurrentYearAverageReceiptStock() {
		return currentYearAverageReceiptStock;
	}

	public void setCurrentYearAverageReceiptStock(BigDecimal currentYearAverageReceiptStock) {
		this.currentYearAverageReceiptStock = currentYearAverageReceiptStock;
	}

	public BigDecimal getLastYearReceiptStock() {
		return lastYearReceiptStock;
	}

	public void setLastYearReceiptStock(BigDecimal lastYearReceiptStock) {
		this.lastYearReceiptStock = lastYearReceiptStock;
	}

	public BigDecimal getCurrentYearReceiptStock() {
		return currentYearReceiptStock;
	}

	public void setCurrentYearReceiptStock(BigDecimal currentYearReceiptStock) {
		this.currentYearReceiptStock = currentYearReceiptStock;
	}

	public BigDecimal getCurrentMonthSales() {
		return currentMonthSales;
	}

	public void setCurrentMonthSales(BigDecimal currentMonthSales) {
		this.currentMonthSales = currentMonthSales;
	}

	public BigDecimal getCurrentYearMonthlySales01() {
		return currentYearMonthlySales01;
	}

	public void setCurrentYearMonthlySales01(BigDecimal currentYearMonthlySales01) {
		this.currentYearMonthlySales01 = currentYearMonthlySales01;
	}

	public BigDecimal getCurrentYearMonthlySales02() {
		return currentYearMonthlySales02;
	}

	public void setCurrentYearMonthlySales02(BigDecimal currentYearMonthlySales02) {
		this.currentYearMonthlySales02 = currentYearMonthlySales02;
	}

	public BigDecimal getCurrentYearMonthlySales03() {
		return currentYearMonthlySales03;
	}

	public void setCurrentYearMonthlySales03(BigDecimal currentYearMonthlySales03) {
		this.currentYearMonthlySales03 = currentYearMonthlySales03;
	}

	public BigDecimal getCurrentYearMonthlySales04() {
		return currentYearMonthlySales04;
	}

	public void setCurrentYearMonthlySales04(BigDecimal currentYearMonthlySales04) {
		this.currentYearMonthlySales04 = currentYearMonthlySales04;
	}

	public BigDecimal getCurrentYearMonthlySales05() {
		return currentYearMonthlySales05;
	}

	public void setCurrentYearMonthlySales05(BigDecimal currentYearMonthlySales05) {
		this.currentYearMonthlySales05 = currentYearMonthlySales05;
	}

	public BigDecimal getCurrentYearMonthlySales06() {
		return currentYearMonthlySales06;
	}

	public void setCurrentYearMonthlySales06(BigDecimal currentYearMonthlySales06) {
		this.currentYearMonthlySales06 = currentYearMonthlySales06;
	}

	public BigDecimal getCurrentYearMonthlySales07() {
		return currentYearMonthlySales07;
	}

	public void setCurrentYearMonthlySales07(BigDecimal currentYearMonthlySales07) {
		this.currentYearMonthlySales07 = currentYearMonthlySales07;
	}

	public BigDecimal getCurrentYearMonthlySales08() {
		return currentYearMonthlySales08;
	}

	public void setCurrentYearMonthlySales08(BigDecimal currentYearMonthlySales08) {
		this.currentYearMonthlySales08 = currentYearMonthlySales08;
	}

	public BigDecimal getCurrentYearMonthlySales09() {
		return currentYearMonthlySales09;
	}

	public void setCurrentYearMonthlySales09(BigDecimal currentYearMonthlySales09) {
		this.currentYearMonthlySales09 = currentYearMonthlySales09;
	}

	public BigDecimal getCurrentYearMonthlySales10() {
		return currentYearMonthlySales10;
	}

	public void setCurrentYearMonthlySales10(BigDecimal currentYearMonthlySales10) {
		this.currentYearMonthlySales10 = currentYearMonthlySales10;
	}

	public BigDecimal getCurrentYearMonthlySales11() {
		return currentYearMonthlySales11;
	}

	public void setCurrentYearMonthlySales11(BigDecimal currentYearMonthlySales11) {
		this.currentYearMonthlySales11 = currentYearMonthlySales11;
	}

	public BigDecimal getCurrentYearMonthlySales12() {
		return currentYearMonthlySales12;
	}

	public void setCurrentYearMonthlySales12(BigDecimal currentYearMonthlySales12) {
		this.currentYearMonthlySales12 = currentYearMonthlySales12;
	}

	public BigDecimal getAverageSales() {
		return averageSales;
	}

	public void setAverageSales(BigDecimal averageSales) {
		this.averageSales = averageSales;
	}

	public BigDecimal getLastYearMonthlySales01() {
		return lastYearMonthlySales01;
	}

	public void setLastYearMonthlySales01(BigDecimal lastYearMonthlySales01) {
		this.lastYearMonthlySales01 = lastYearMonthlySales01;
	}

	public BigDecimal getLastYearMonthlySales02() {
		return lastYearMonthlySales02;
	}

	public void setLastYearMonthlySales02(BigDecimal lastYearMonthlySales02) {
		this.lastYearMonthlySales02 = lastYearMonthlySales02;
	}

	public BigDecimal getLastYearMonthlySales03() {
		return lastYearMonthlySales03;
	}

	public void setLastYearMonthlySales03(BigDecimal lastYearMonthlySales03) {
		this.lastYearMonthlySales03 = lastYearMonthlySales03;
	}

	public BigDecimal getLastYearMonthlySales04() {
		return lastYearMonthlySales04;
	}

	public void setLastYearMonthlySales04(BigDecimal lastYearMonthlySales04) {
		this.lastYearMonthlySales04 = lastYearMonthlySales04;
	}

	public BigDecimal getLastYearMonthlySales05() {
		return lastYearMonthlySales05;
	}

	public void setLastYearMonthlySales05(BigDecimal lastYearMonthlySales05) {
		this.lastYearMonthlySales05 = lastYearMonthlySales05;
	}

	public BigDecimal getLastYearMonthlySales06() {
		return lastYearMonthlySales06;
	}

	public void setLastYearMonthlySales06(BigDecimal lastYearMonthlySales06) {
		this.lastYearMonthlySales06 = lastYearMonthlySales06;
	}

	public BigDecimal getLastYearMonthlySales07() {
		return lastYearMonthlySales07;
	}

	public void setLastYearMonthlySales07(BigDecimal lastYearMonthlySales07) {
		this.lastYearMonthlySales07 = lastYearMonthlySales07;
	}

	public BigDecimal getLastYearMonthlySales08() {
		return lastYearMonthlySales08;
	}

	public void setLastYearMonthlySales08(BigDecimal lastYearMonthlySales08) {
		this.lastYearMonthlySales08 = lastYearMonthlySales08;
	}

	public BigDecimal getLastYearMonthlySales09() {
		return lastYearMonthlySales09;
	}

	public void setLastYearMonthlySales09(BigDecimal lastYearMonthlySales09) {
		this.lastYearMonthlySales09 = lastYearMonthlySales09;
	}

	public BigDecimal getLastYearMonthlySales10() {
		return lastYearMonthlySales10;
	}

	public void setLastYearMonthlySales10(BigDecimal lastYearMonthlySales10) {
		this.lastYearMonthlySales10 = lastYearMonthlySales10;
	}

	public BigDecimal getLastYearMonthlySales11() {
		return lastYearMonthlySales11;
	}

	public void setLastYearMonthlySales11(BigDecimal lastYearMonthlySales11) {
		this.lastYearMonthlySales11 = lastYearMonthlySales11;
	}

	public BigDecimal getLastYearMonthlySales12() {
		return lastYearMonthlySales12;
	}

	public void setLastYearMonthlySales12(BigDecimal lastYearMonthlySales12) {
		this.lastYearMonthlySales12 = lastYearMonthlySales12;
	}

	public BigDecimal getLastYearSales() {
		return lastYearSales;
	}

	public void setLastYearSales(BigDecimal lastYearSales) {
		this.lastYearSales = lastYearSales;
	}

	public BigDecimal getCurrentYearSales() {
		return currentYearSales;
	}

	public void setCurrentYearSales(BigDecimal currentYearSales) {
		this.currentYearSales = currentYearSales;
	}

	public BigDecimal getCurrentMonthReceiptInEuro() {
		return currentMonthReceiptInEuro;
	}

	public void setCurrentMonthReceiptInEuro(BigDecimal currentMonthReceiptInEuro) {
		this.currentMonthReceiptInEuro = currentMonthReceiptInEuro;
	}

	public BigDecimal getMonthlyReceiptsInEuro01() {
		return monthlyReceiptsInEuro01;
	}

	public void setMonthlyReceiptsInEuro01(BigDecimal monthlyReceiptsInEuro01) {
		this.monthlyReceiptsInEuro01 = monthlyReceiptsInEuro01;
	}

	public BigDecimal getMonthlyReceiptsInEuro02() {
		return monthlyReceiptsInEuro02;
	}

	public void setMonthlyReceiptsInEuro02(BigDecimal monthlyReceiptsInEuro02) {
		this.monthlyReceiptsInEuro02 = monthlyReceiptsInEuro02;
	}

	public BigDecimal getMonthlyReceiptsInEuro03() {
		return monthlyReceiptsInEuro03;
	}

	public void setMonthlyReceiptsInEuro03(BigDecimal monthlyReceiptsInEuro03) {
		this.monthlyReceiptsInEuro03 = monthlyReceiptsInEuro03;
	}

	public BigDecimal getMonthlyReceiptsInEuro04() {
		return monthlyReceiptsInEuro04;
	}

	public void setMonthlyReceiptsInEuro04(BigDecimal monthlyReceiptsInEuro04) {
		this.monthlyReceiptsInEuro04 = monthlyReceiptsInEuro04;
	}

	public BigDecimal getMonthlyReceiptsInEuro05() {
		return monthlyReceiptsInEuro05;
	}

	public void setMonthlyReceiptsInEuro05(BigDecimal monthlyReceiptsInEuro05) {
		this.monthlyReceiptsInEuro05 = monthlyReceiptsInEuro05;
	}

	public BigDecimal getMonthlyReceiptsInEuro06() {
		return monthlyReceiptsInEuro06;
	}

	public void setMonthlyReceiptsInEuro06(BigDecimal monthlyReceiptsInEuro06) {
		this.monthlyReceiptsInEuro06 = monthlyReceiptsInEuro06;
	}

	public BigDecimal getMonthlyReceiptsInEuro07() {
		return monthlyReceiptsInEuro07;
	}

	public void setMonthlyReceiptsInEuro07(BigDecimal monthlyReceiptsInEuro07) {
		this.monthlyReceiptsInEuro07 = monthlyReceiptsInEuro07;
	}

	public BigDecimal getMonthlyReceiptsInEuro08() {
		return monthlyReceiptsInEuro08;
	}

	public void setMonthlyReceiptsInEuro08(BigDecimal monthlyReceiptsInEuro08) {
		this.monthlyReceiptsInEuro08 = monthlyReceiptsInEuro08;
	}

	public BigDecimal getMonthlyReceiptsInEuro09() {
		return monthlyReceiptsInEuro09;
	}

	public void setMonthlyReceiptsInEuro09(BigDecimal monthlyReceiptsInEuro09) {
		this.monthlyReceiptsInEuro09 = monthlyReceiptsInEuro09;
	}

	public BigDecimal getMonthlyReceiptsInEuro10() {
		return monthlyReceiptsInEuro10;
	}

	public void setMonthlyReceiptsInEuro10(BigDecimal monthlyReceiptsInEuro10) {
		this.monthlyReceiptsInEuro10 = monthlyReceiptsInEuro10;
	}

	public BigDecimal getMonthlyReceiptsInEuro11() {
		return monthlyReceiptsInEuro11;
	}

	public void setMonthlyReceiptsInEuro11(BigDecimal monthlyReceiptsInEuro11) {
		this.monthlyReceiptsInEuro11 = monthlyReceiptsInEuro11;
	}

	public BigDecimal getMonthlyReceiptsInEuro12() {
		return monthlyReceiptsInEuro12;
	}

	public void setMonthlyReceiptsInEuro12(BigDecimal monthlyReceiptsInEuro12) {
		this.monthlyReceiptsInEuro12 = monthlyReceiptsInEuro12;
	}

	public BigDecimal getAverageReceiptInEuro() {
		return averageReceiptInEuro;
	}

	public void setAverageReceiptInEuro(BigDecimal averageReceiptInEuro) {
		this.averageReceiptInEuro = averageReceiptInEuro;
	}

	public BigDecimal getLastYearReceiptInEuro() {
		return lastYearReceiptInEuro;
	}

	public void setLastYearReceiptInEuro(BigDecimal lastYearReceiptInEuro) {
		this.lastYearReceiptInEuro = lastYearReceiptInEuro;
	}

	public BigDecimal getCurrentYearReceiptInEuro() {
		return currentYearReceiptInEuro;
	}

	public void setCurrentYearReceiptInEuro(BigDecimal currentYearReceiptInEuro) {
		this.currentYearReceiptInEuro = currentYearReceiptInEuro;
	}

	public BigDecimal getPendingOrders() {
		return pendingOrders;
	}

	public void setPendingOrders(BigDecimal pendingOrders) {
		this.pendingOrders = pendingOrders;
	}

	public BigDecimal getMinimumOrderQuantity() {
		return minimumOrderQuantity;
	}

	public void setMinimumOrderQuantity(BigDecimal minimumOrderQuantity) {
		this.minimumOrderQuantity = minimumOrderQuantity;
	}

	public BigDecimal getCustomerBacklog() {
		return customerBacklog;
	}

	public void setCustomerBacklog(BigDecimal customerBacklog) {
		this.customerBacklog = customerBacklog;
	}

	public BigDecimal getDeliveryBacklog() {
		return deliveryBacklog;
	}

	public void setDeliveryBacklog(BigDecimal deliveryBacklog) {
		this.deliveryBacklog = deliveryBacklog;
	}

	public BigDecimal getSalesAmount() {
		return salesAmount;
	}

	public void setSalesAmount(BigDecimal salesAmount) {
		this.salesAmount = salesAmount;
	}

	public BigDecimal getRequestedQuantity() {
		return requestedQuantity;
	}

	public void setRequestedQuantity(BigDecimal requestedQuantity) {
		this.requestedQuantity = requestedQuantity;
	}

	public BigDecimal getBookingAmount() {
		return bookingAmount;
	}

	public void setBookingAmount(BigDecimal bookingAmount) {
		this.bookingAmount = bookingAmount;
	}

	public BigDecimal getRequestedAmount() {
		return requestedAmount;
	}

	public void setRequestedAmount(BigDecimal requestedAmount) {
		this.requestedAmount = requestedAmount;
	}

	public BigDecimal getCurrentYearMonthlyRequestedAmounts01() {
		return currentYearMonthlyRequestedAmounts01;
	}

	public void setCurrentYearMonthlyRequestedAmounts01(BigDecimal currentYearMonthlyRequestedAmounts01) {
		this.currentYearMonthlyRequestedAmounts01 = currentYearMonthlyRequestedAmounts01;
	}

	public BigDecimal getCurrentYearMonthlyRequestedAmounts02() {
		return currentYearMonthlyRequestedAmounts02;
	}

	public void setCurrentYearMonthlyRequestedAmounts02(BigDecimal currentYearMonthlyRequestedAmounts02) {
		this.currentYearMonthlyRequestedAmounts02 = currentYearMonthlyRequestedAmounts02;
	}

	public BigDecimal getCurrentYearMonthlyRequestedAmounts03() {
		return currentYearMonthlyRequestedAmounts03;
	}

	public void setCurrentYearMonthlyRequestedAmounts03(BigDecimal currentYearMonthlyRequestedAmounts03) {
		this.currentYearMonthlyRequestedAmounts03 = currentYearMonthlyRequestedAmounts03;
	}

	public BigDecimal getCurrentYearMonthlyRequestedAmounts04() {
		return currentYearMonthlyRequestedAmounts04;
	}

	public void setCurrentYearMonthlyRequestedAmounts04(BigDecimal currentYearMonthlyRequestedAmounts04) {
		this.currentYearMonthlyRequestedAmounts04 = currentYearMonthlyRequestedAmounts04;
	}

	public BigDecimal getCurrentYearMonthlyRequestedAmounts05() {
		return currentYearMonthlyRequestedAmounts05;
	}

	public void setCurrentYearMonthlyRequestedAmounts05(BigDecimal currentYearMonthlyRequestedAmounts05) {
		this.currentYearMonthlyRequestedAmounts05 = currentYearMonthlyRequestedAmounts05;
	}

	public BigDecimal getCurrentYearMonthlyRequestedAmounts06() {
		return currentYearMonthlyRequestedAmounts06;
	}

	public void setCurrentYearMonthlyRequestedAmounts06(BigDecimal currentYearMonthlyRequestedAmounts06) {
		this.currentYearMonthlyRequestedAmounts06 = currentYearMonthlyRequestedAmounts06;
	}

	public BigDecimal getCurrentYearMonthlyRequestedAmounts07() {
		return currentYearMonthlyRequestedAmounts07;
	}

	public void setCurrentYearMonthlyRequestedAmounts07(BigDecimal currentYearMonthlyRequestedAmounts07) {
		this.currentYearMonthlyRequestedAmounts07 = currentYearMonthlyRequestedAmounts07;
	}

	public BigDecimal getCurrentYearMonthlyRequestedAmounts08() {
		return currentYearMonthlyRequestedAmounts08;
	}

	public void setCurrentYearMonthlyRequestedAmounts08(BigDecimal currentYearMonthlyRequestedAmounts08) {
		this.currentYearMonthlyRequestedAmounts08 = currentYearMonthlyRequestedAmounts08;
	}

	public BigDecimal getCurrentYearMonthlyRequestedAmounts09() {
		return currentYearMonthlyRequestedAmounts09;
	}

	public void setCurrentYearMonthlyRequestedAmounts09(BigDecimal currentYearMonthlyRequestedAmounts09) {
		this.currentYearMonthlyRequestedAmounts09 = currentYearMonthlyRequestedAmounts09;
	}

	public BigDecimal getCurrentYearMonthlyRequestedAmounts10() {
		return currentYearMonthlyRequestedAmounts10;
	}

	public void setCurrentYearMonthlyRequestedAmounts10(BigDecimal currentYearMonthlyRequestedAmounts10) {
		this.currentYearMonthlyRequestedAmounts10 = currentYearMonthlyRequestedAmounts10;
	}

	public BigDecimal getCurrentYearMonthlyRequestedAmounts11() {
		return currentYearMonthlyRequestedAmounts11;
	}

	public void setCurrentYearMonthlyRequestedAmounts11(BigDecimal currentYearMonthlyRequestedAmounts11) {
		this.currentYearMonthlyRequestedAmounts11 = currentYearMonthlyRequestedAmounts11;
	}

	public BigDecimal getCurrentYearMonthlyRequestedAmounts12() {
		return currentYearMonthlyRequestedAmounts12;
	}

	public void setCurrentYearMonthlyRequestedAmounts12(BigDecimal currentYearMonthlyRequestedAmounts12) {
		this.currentYearMonthlyRequestedAmounts12 = currentYearMonthlyRequestedAmounts12;
	}

	public BigDecimal getLastYearMonthlyRequestedAmounts01() {
		return lastYearMonthlyRequestedAmounts01;
	}

	public void setLastYearMonthlyRequestedAmounts01(BigDecimal lastYearMonthlyRequestedAmounts01) {
		this.lastYearMonthlyRequestedAmounts01 = lastYearMonthlyRequestedAmounts01;
	}

	public BigDecimal getLastYearMonthlyRequestedAmounts02() {
		return lastYearMonthlyRequestedAmounts02;
	}

	public void setLastYearMonthlyRequestedAmounts02(BigDecimal lastYearMonthlyRequestedAmounts02) {
		this.lastYearMonthlyRequestedAmounts02 = lastYearMonthlyRequestedAmounts02;
	}

	public BigDecimal getLastYearMonthlyRequestedAmounts03() {
		return lastYearMonthlyRequestedAmounts03;
	}

	public void setLastYearMonthlyRequestedAmounts03(BigDecimal lastYearMonthlyRequestedAmounts03) {
		this.lastYearMonthlyRequestedAmounts03 = lastYearMonthlyRequestedAmounts03;
	}

	public BigDecimal getLastYearMonthlyRequestedAmounts04() {
		return lastYearMonthlyRequestedAmounts04;
	}

	public void setLastYearMonthlyRequestedAmounts04(BigDecimal lastYearMonthlyRequestedAmounts04) {
		this.lastYearMonthlyRequestedAmounts04 = lastYearMonthlyRequestedAmounts04;
	}

	public BigDecimal getLastYearMonthlyRequestedAmounts05() {
		return lastYearMonthlyRequestedAmounts05;
	}

	public void setLastYearMonthlyRequestedAmounts05(BigDecimal lastYearMonthlyRequestedAmounts05) {
		this.lastYearMonthlyRequestedAmounts05 = lastYearMonthlyRequestedAmounts05;
	}

	public BigDecimal getLastYearMonthlyRequestedAmounts06() {
		return lastYearMonthlyRequestedAmounts06;
	}

	public void setLastYearMonthlyRequestedAmounts06(BigDecimal lastYearMonthlyRequestedAmounts06) {
		this.lastYearMonthlyRequestedAmounts06 = lastYearMonthlyRequestedAmounts06;
	}

	public BigDecimal getLastYearMonthlyRequestedAmounts07() {
		return lastYearMonthlyRequestedAmounts07;
	}

	public void setLastYearMonthlyRequestedAmounts07(BigDecimal lastYearMonthlyRequestedAmounts07) {
		this.lastYearMonthlyRequestedAmounts07 = lastYearMonthlyRequestedAmounts07;
	}

	public BigDecimal getLastYearMonthlyRequestedAmounts08() {
		return lastYearMonthlyRequestedAmounts08;
	}

	public void setLastYearMonthlyRequestedAmounts08(BigDecimal lastYearMonthlyRequestedAmounts08) {
		this.lastYearMonthlyRequestedAmounts08 = lastYearMonthlyRequestedAmounts08;
	}

	public BigDecimal getLastYearMonthlyRequestedAmounts09() {
		return lastYearMonthlyRequestedAmounts09;
	}

	public void setLastYearMonthlyRequestedAmounts09(BigDecimal lastYearMonthlyRequestedAmounts09) {
		this.lastYearMonthlyRequestedAmounts09 = lastYearMonthlyRequestedAmounts09;
	}

	public BigDecimal getLastYearMonthlyRequestedAmounts10() {
		return lastYearMonthlyRequestedAmounts10;
	}

	public void setLastYearMonthlyRequestedAmounts10(BigDecimal lastYearMonthlyRequestedAmounts10) {
		this.lastYearMonthlyRequestedAmounts10 = lastYearMonthlyRequestedAmounts10;
	}

	public BigDecimal getLastYearMonthlyRequestedAmounts11() {
		return lastYearMonthlyRequestedAmounts11;
	}

	public void setLastYearMonthlyRequestedAmounts11(BigDecimal lastYearMonthlyRequestedAmounts11) {
		this.lastYearMonthlyRequestedAmounts11 = lastYearMonthlyRequestedAmounts11;
	}

	public BigDecimal getLastYearMonthlyRequestedAmounts12() {
		return lastYearMonthlyRequestedAmounts12;
	}

	public void setLastYearMonthlyRequestedAmounts12(BigDecimal lastYearMonthlyRequestedAmounts12) {
		this.lastYearMonthlyRequestedAmounts12 = lastYearMonthlyRequestedAmounts12;
	}

	public BigDecimal getServiceProposal() {
		return serviceProposal;
	}

	public void setServiceProposal(BigDecimal serviceProposal) {
		this.serviceProposal = serviceProposal;
	}

	public BigDecimal getInventoryAmount() {
		return inventoryAmount;
	}

	public void setInventoryAmount(BigDecimal inventoryAmount) {
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

	public BigDecimal getBarcodeNumber() {
		return barcodeNumber;
	}

	public void setBarcodeNumber(BigDecimal barcodeNumber) {
		this.barcodeNumber = barcodeNumber;
	}

	public BigDecimal getSurchargeDeduction() {
		return surchargeDeduction;
	}

	public void setSurchargeDeduction(BigDecimal surchargeDeduction) {
		this.surchargeDeduction = surchargeDeduction;
	}

	public BigDecimal getSurchargeDeductionMark() {
		return surchargeDeductionMark;
	}

	public void setSurchargeDeductionMark(BigDecimal surchargeDeductionMark) {
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

	public BigDecimal getLastDisposalDate() {
		return lastDisposalDate;
	}

	public void setLastDisposalDate(BigDecimal lastDisposalDate) {
		this.lastDisposalDate = lastDisposalDate;
	}

	public BigDecimal getLastReceiptDate() {
		return lastReceiptDate;
	}

	public void setLastReceiptDate(BigDecimal lastReceiptDate) {
		this.lastReceiptDate = lastReceiptDate;
	}

	public BigDecimal getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(BigDecimal creationDate) {
		this.creationDate = creationDate;
	}

	public BigDecimal getLastOrderDate() {
		return lastOrderDate;
	}

	public void setLastOrderDate(BigDecimal lastOrderDate) {
		this.lastOrderDate = lastOrderDate;
	}

	public BigDecimal getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(BigDecimal expirationDate) {
		this.expirationDate = expirationDate;
	}

	public BigDecimal getFuturePriceFromDate() {
		return futurePriceFromDate;
	}

	public void setFuturePriceFromDate(BigDecimal futurePriceFromDate) {
		this.futurePriceFromDate = futurePriceFromDate;
	}

	public BigDecimal getLastMovementDate() {
		return lastMovementDate;
	}

	public void setLastMovementDate(BigDecimal lastMovementDate) {
		this.lastMovementDate = lastMovementDate;
	}

	public BigDecimal getInventoryDate() {
		return inventoryDate;
	}

	public void setInventoryDate(BigDecimal inventoryDate) {
		this.inventoryDate = inventoryDate;
	}

	public BigDecimal getLastDispositionDate() {
		return lastDispositionDate;
	}

	public void setLastDispositionDate(BigDecimal lastDispositionDate) {
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

	public BigDecimal getPackagingUnit1() {
		return packagingUnit1;
	}

	public void setPackagingUnit1(BigDecimal packagingUnit1) {
		this.packagingUnit1 = packagingUnit1;
	}

	public BigDecimal getPackagingUnit2() {
		return packagingUnit2;
	}

	public void setPackagingUnit2(BigDecimal packagingUnit2) {
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

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
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

	public BigDecimal getCompany() {
		return company;
	}

	public void setCompany(BigDecimal company) {
		this.company = company;
	}

	public BigDecimal getBranch() {
		return branch;
	}

	public void setBranch(BigDecimal branch) {
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

	public String getLengthWidthHeigth() {
		return lengthWidthHeigth;
	}

	public void setLengthWidthHeigth(String lengthWidthHeigth) {
		this.lengthWidthHeigth = lengthWidthHeigth;
	}

	public String getInternalAllocationIndicator() {
		return internalAllocationIndicator;
	}

	public void setInternalAllocationIndicator(String internalAllocationIndicator) {
		this.internalAllocationIndicator = internalAllocationIndicator;
	}

	public BigDecimal getInventoryAtYearStart() {
		return inventoryAtYearStart;
	}

	public void setInventoryAtYearStart(BigDecimal inventoryAtYearStart) {
		this.inventoryAtYearStart = inventoryAtYearStart;
	}

	public String getPartLabel() {
		return partLabel;
	}

	public void setPartLabel(String partLabel) {
		this.partLabel = partLabel;
	}

	public String getDiscountGroupValue() {
		return discountGroupValue;
	}

	public void setDiscountGroupValue(String discountGroupValue) {
		this.discountGroupValue = discountGroupValue;
	}

	public BigDecimal getDiscountGroupPercentageValue() {
		return discountGroupPercentageValue;
	}

	public void setDiscountGroupPercentageValue(BigDecimal discountGroupPercentageValue) {
		this.discountGroupPercentageValue = discountGroupPercentageValue;
	}

	public String getDiscountValuePercentageMWST() {
		return discountValuePercentageMWST;
	}

	public void setDiscountValuePercentageMWST(String discountValuePercentageMWST) {
		this.discountValuePercentageMWST = discountValuePercentageMWST;
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

	public String getLagerName() {
		return lagerName;
	}

	public void setLagerName(String lagerName) {
		this.lagerName = lagerName;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getOldPrice() {
		return oldPrice;
	}

	public void setOldPrice(String oldPrice) {
		this.oldPrice = oldPrice;
	}

	public String getNetPurchasePrice() {
		return netPurchasePrice;
	}

	public void setNetPurchasePrice(String netPurchasePrice) {
		this.netPurchasePrice = netPurchasePrice;
	}

	public String getTaxValue() {
		return taxValue;
	}

	public void setTaxValue(String taxValue) {
		this.taxValue = taxValue;
	}

	public String getPartDiscountGroup() {
		return partDiscountGroup;
	}

	public void setPartDiscountGroup(String partDiscountGroup) {
		this.partDiscountGroup = partDiscountGroup;
	}

	/**
	 * @return the priceLimit
	 */
	public String getPriceLimit() {
		return priceLimit;
	}

	/**
	 * @param priceLimit the priceLimit to set
	 */
	public void setPriceLimit(String priceLimit) {
		this.priceLimit = priceLimit;
	}

	/**
	 * @return the surchargePercentage
	 */
	public String getSurchargePercentage() {
		return surchargePercentage;
	}

	/**
	 * @param surchargePercentage the surchargePercentage to set
	 */
	public void setSurchargePercentage(String surchargePercentage) {
		this.surchargePercentage = surchargePercentage;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * @return the bodywork
	 */
	public String getBodywork() {
		return bodywork;
	}

	/**
	 * @param bodywork the bodywork to set
	 */
	public void setBodywork(String bodywork) {
		this.bodywork = bodywork;
	}

	public String getPartType() {
		return partType;
	}

	public void setPartType(String partType) {
		this.partType = partType;
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
