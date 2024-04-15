package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

public class PartGlobalSearch {
	
		// Original Equipment Manufacturer (Hersteller - OEM).
		@DBTable(columnName ="OEM", required = true)
		private String oem;
		
		// The part number (Teilenummer).
		@DBTable(columnName ="TNR", required = true)
		private String partNumber;
		
		// Warehouse Information (Lagernummer / Lagerbezeichnung).
		@DBTable(columnName ="LNR", required = true)
		private String warehouse;
		
		// Description of the part (Bezeichnung).
		@DBTable(columnName ="NAME", required = true)
		private String description;
		
		// Stock information for part (Bestand).
		@DBTable(columnName ="STOCK", required = true)
		private String stock;
		
		// Brand information for part (Teilemarke).
		@DBTable(columnName ="BRAND", required = true)
		private String oemBrand;
		
		@DBTable(columnName ="PRICE", required = true)
		private String price;
		
		@DBTable(columnName = "RGROUP", required = true)
		private String discountGroup;
		
		@DBTable(columnName ="MC", required = true)
		private String marketingCode;
		
		@DBTable(columnName ="PRKZ", required = true)
		private String priceIndicator;
		
		@DBTable(columnName ="TAX", required = true)
		private String tax;
		
		@DBTable(columnName = "surcharge_price_limit", required = true)
		private String surchargePriceLimit;
		
		@DBTable(columnName ="GEWICH", required = true)
		private String weight;
		
		@DBTable(columnName ="LABEL", required = true)
		private String partLabel;
		
		@DBTable(columnName ="LAENGE", required = true)
		private String length;
		
		@DBTable(columnName ="BREITE", required = true)
		private String width;
		
		@DBTable(columnName ="HOEHE", required = true)
		private String height;
		
		@DBTable(columnName ="VERP1", required = true)
		private String packagingUnit1;
		
		@DBTable(columnName ="VERP2", required = true)
		private String packagingUnit2;
		
		// Agency information (Filialnummer / Filialbezeichnung).
		@DBTable(columnName ="filial", required = true)
		private String agency;
		
		
		@DBTable(columnName ="ROWNUMER", required = true)
		private Integer totalCount;	
		
		// WarehouseName Information (Lagername).
		@DBTable(columnName ="LagerName", required = true)
		private String alphaXWarehouseName;
				
		@DBTable(columnName ="VKAVGW", required = true)
		private BigDecimal averageSales;
			
		@DBTable(columnName ="DTLABG", required = true)
		private BigDecimal lastDisposalDate;
		

		@DBTable(columnName ="LOPA", required = true)
		private String storageLocation;

		@DBTable(columnName = "SA", required = true)
		private String storageIndikator;
		
		@DBTable(columnName ="BLP", required = true)
		private BigDecimal listPrice;
		
		@DBTable(columnName ="RG", required = true)
		private String shoppingDiscountGroup;
		
		@DBTable(columnName ="TAXCDE", required = true)
		private String vatRegistrationNumber;
		
		@DBTable(columnName ="SORTKL", required = true)
		private String assortmentClass;
		
		@DBTable(columnName ="RAB_TBL", required = true)
		private String rabTbl;

		/**
		 * @return the oem
		 */
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
		 * @return the description
		 */
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
		 * @return the priceIndicator
		 */
		public String getPriceIndicator() {
			return priceIndicator;
		}

		/**
		 * @param priceIndicator the priceIndicator to set
		 */
		public void setPriceIndicator(String priceIndicator) {
			this.priceIndicator = priceIndicator;
		}

		/**
		 * @return the tax
		 */
		public String getTax() {
			return tax;
		}

		/**
		 * @param tax the tax to set
		 */
		public void setTax(String tax) {
			this.tax = tax;
		}

		/**
		 * @return the surchargePriceLimit
		 */
		public String getSurchargePriceLimit() {
			return surchargePriceLimit;
		}

		/**
		 * @param surchargePriceLimit the surchargePriceLimit to set
		 */
		public void setSurchargePriceLimit(String surchargePriceLimit) {
			this.surchargePriceLimit = surchargePriceLimit;
		}

		/**
		 * @return the weight
		 */
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
		 * @return the partLabel
		 */
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
		 * @return the length
		 */
		public String getLength() {
			return length;
		}

		/**
		 * @param length the length to set
		 */
		public void setLength(String length) {
			this.length = length;
		}

		/**
		 * @return the width
		 */
		public String getWidth() {
			return width;
		}

		/**
		 * @param width the width to set
		 */
		public void setWidth(String width) {
			this.width = width;
		}

		/**
		 * @return the height
		 */
		public String getHeight() {
			return height;
		}

		/**
		 * @param height the height to set
		 */
		public void setHeight(String height) {
			this.height = height;
		}

		/**
		 * @return the packagingUnit1
		 */
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
		 * @return the averageSales
		 */
		public BigDecimal getAverageSales() {
			return averageSales;
		}

		/**
		 * @param averageSales the averageSales to set
		 */
		public void setAverageSales(BigDecimal averageSales) {
			this.averageSales = averageSales;
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
		 * @return the listPrice
		 */
		public BigDecimal getListPrice() {
			return listPrice;
		}

		/**
		 * @param listPrice the listPrice to set
		 */
		public void setListPrice(BigDecimal listPrice) {
			this.listPrice = listPrice;
		}

		/**
		 * @return the shoppingDiscountGroup
		 */
		public String getShoppingDiscountGroup() {
			return shoppingDiscountGroup;
		}

		/**
		 * @param shoppingDiscountGroup the shoppingDiscountGroup to set
		 */
		public void setShoppingDiscountGroup(String shoppingDiscountGroup) {
			this.shoppingDiscountGroup = shoppingDiscountGroup;
		}

		/**
		 * @return the vatRegistrationNumber
		 */
		public String getVatRegistrationNumber() {
			return vatRegistrationNumber;
		}

		/**
		 * @param vatRegistrationNumber the vatRegistrationNumber to set
		 */
		public void setVatRegistrationNumber(String vatRegistrationNumber) {
			this.vatRegistrationNumber = vatRegistrationNumber;
		}

		/**
		 * @return the assortmentClass
		 */
		public String getAssortmentClass() {
			return assortmentClass;
		}

		/**
		 * @param assortmentClass the assortmentClass to set
		 */
		public void setAssortmentClass(String assortmentClass) {
			this.assortmentClass = assortmentClass;
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
		
		
		
		
		
		
}
