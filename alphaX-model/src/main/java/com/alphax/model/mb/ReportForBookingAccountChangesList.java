package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about Report Selections Booking - Account Changes List Details")
public class ReportForBookingAccountChangesList {

	//Account Changes = Kontoanderungen
	
		@DBTable(columnName = "BA", required = true)
		private String businessCase;
		
		@DBTable(columnName = "HERST", required = true)
		private String manufacturer;
		
		@DBTable(columnName = "AP_Lager", required = true)
		private BigDecimal alphaPlusWarehouseId;
		
		@DBTable(columnName = "AX_Lager", required = true)
		private String alphaxWarehouseName;
		
		@DBTable(columnName = "Herkunft", required = true)
		private String origin;
		
		@DBTable(columnName = "Datum", required = true)
		private String bookingDate;
		
		@DBTable(columnName = "Belegnummer", required = true)
		private String documentNo;
	
		@DBTable(columnName = "Teilenummer", required = true)
		private String partNumber;
		
		@DBTable(columnName = "Bezeichnung", required = true)
		private String partName;
	
		@DBTable(columnName = "TeilartVorher", required = true)
		private String beforePartType;
		
		@DBTable(columnName = "MarketingCodeVorher", required = true)
		private String beforeMarketingCode;
		
		@DBTable(columnName = "MarkeVorher", required = true)
		private String beforePartBrand;
		
		@DBTable(columnName = "BuchungskontoVorher", required = true)
		private String beforePostingAccount;
		
		@DBTable(columnName = "KontobezeichnungVorher", required = true)
		private String beforeAccountDesignation;
		

		@DBTable(columnName = "TeileartNachher", required = true)
		private String afterPartType;
		
		@DBTable(columnName = "MarketingCodeNachher", required = true)
		private String afterMarketingCode;
		
		@DBTable(columnName = "MarkeNachher", required = true)
		private String afterPartBrand;
		
		@DBTable(columnName = "BuchungskontoNachher", required = true)
		private String afterPostingAccount;
		
		@DBTable(columnName = "KontobezeichnungNachher", required = true)
		private String afterAccountDesignation;
		
		@DBTable(columnName = "Bestand", required = true)
		private String bestand;
		
		@DBTable(columnName = "DAK", required = true)
		private Double averageNetPrice;
		
		@DBTable(columnName = "SummeInEUR", required = true)
		private String summeInEUR;

		/**
		 * @return the businessCase
		 */
		public String getBusinessCase() {
			return businessCase;
		}

		/**
		 * @param businessCase the businessCase to set
		 */
		public void setBusinessCase(String businessCase) {
			this.businessCase = businessCase;
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
		 * @return the origin
		 */
		public String getOrigin() {
			return origin;
		}

		/**
		 * @param origin the origin to set
		 */
		public void setOrigin(String origin) {
			this.origin = origin;
		}

		/**
		 * @return the bookingDate
		 */
		public String getBookingDate() {
			return bookingDate;
		}

		/**
		 * @param bookingDate the bookingDate to set
		 */
		public void setBookingDate(String bookingDate) {
			this.bookingDate = bookingDate;
		}

		/**
		 * @return the documentNo
		 */
		public String getDocumentNo() {
			return documentNo;
		}

		/**
		 * @param documentNo the documentNo to set
		 */
		public void setDocumentNo(String documentNo) {
			this.documentNo = documentNo;
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
		 * @return the partName
		 */
		public String getPartName() {
			return partName;
		}

		/**
		 * @param partName the partName to set
		 */
		public void setPartName(String partName) {
			this.partName = partName;
		}

		/**
		 * @return the beforePartType
		 */
		public String getBeforePartType() {
			return beforePartType;
		}

		/**
		 * @param beforePartType the beforePartType to set
		 */
		public void setBeforePartType(String beforePartType) {
			this.beforePartType = beforePartType;
		}

		/**
		 * @return the beforeMarketingCode
		 */
		public String getBeforeMarketingCode() {
			return beforeMarketingCode;
		}

		/**
		 * @param beforeMarketingCode the beforeMarketingCode to set
		 */
		public void setBeforeMarketingCode(String beforeMarketingCode) {
			this.beforeMarketingCode = beforeMarketingCode;
		}

		/**
		 * @return the beforePartBrand
		 */
		public String getBeforePartBrand() {
			return beforePartBrand;
		}

		/**
		 * @param beforePartBrand the beforePartBrand to set
		 */
		public void setBeforePartBrand(String beforePartBrand) {
			this.beforePartBrand = beforePartBrand;
		}

		/**
		 * @return the beforeAccountDesignation
		 */
		public String getBeforeAccountDesignation() {
			return beforeAccountDesignation;
		}

		/**
		 * @param beforeAccountDesignation the beforeAccountDesignation to set
		 */
		public void setBeforeAccountDesignation(String beforeAccountDesignation) {
			this.beforeAccountDesignation = beforeAccountDesignation;
		}

		/**
		 * @return the afterPartType
		 */
		public String getAfterPartType() {
			return afterPartType;
		}

		/**
		 * @param afterPartType the afterPartType to set
		 */
		public void setAfterPartType(String afterPartType) {
			this.afterPartType = afterPartType;
		}

		/**
		 * @return the afterMarketingCode
		 */
		public String getAfterMarketingCode() {
			return afterMarketingCode;
		}

		/**
		 * @param afterMarketingCode the afterMarketingCode to set
		 */
		public void setAfterMarketingCode(String afterMarketingCode) {
			this.afterMarketingCode = afterMarketingCode;
		}

		/**
		 * @return the afterPartBrand
		 */
		public String getAfterPartBrand() {
			return afterPartBrand;
		}

		/**
		 * @param afterPartBrand the afterPartBrand to set
		 */
		public void setAfterPartBrand(String afterPartBrand) {
			this.afterPartBrand = afterPartBrand;
		}

		/**
		 * @return the afterAccountDesignation
		 */
		public String getAfterAccountDesignation() {
			return afterAccountDesignation;
		}

		/**
		 * @param afterAccountDesignation the afterAccountDesignation to set
		 */
		public void setAfterAccountDesignation(String afterAccountDesignation) {
			this.afterAccountDesignation = afterAccountDesignation;
		}

		/**
		 * @return the bestand
		 */
		public String getBestand() {
			return bestand;
		}

		/**
		 * @param bestand the bestand to set
		 */
		public void setBestand(String bestand) {
			this.bestand = bestand;
		}

		/**
		 * @return the averageNetPrice
		 */
		public Double getAverageNetPrice() {
			return averageNetPrice;
		}

		/**
		 * @param averageNetPrice the averageNetPrice to set
		 */
		public void setAverageNetPrice(Double averageNetPrice) {
			this.averageNetPrice = averageNetPrice;
		}

		/**
		 * @return the summeInEUR
		 */
		public String getSummeInEUR() {
			return summeInEUR;
		}

		/**
		 * @param summeInEUR the summeInEUR to set
		 */
		public void setSummeInEUR(String summeInEUR) {
			this.summeInEUR = summeInEUR;
		}

		/**
		 * @return the beforePostingAccount
		 */
		public String getBeforePostingAccount() {
			return beforePostingAccount;
		}

		/**
		 * @param beforePostingAccount the beforePostingAccount to set
		 */
		public void setBeforePostingAccount(String beforePostingAccount) {
			this.beforePostingAccount = beforePostingAccount;
		}

		/**
		 * @return the afterPostingAccount
		 */
		public String getAfterPostingAccount() {
			return afterPostingAccount;
		}

		/**
		 * @param afterPostingAccount the afterPostingAccount to set
		 */
		public void setAfterPostingAccount(String afterPostingAccount) {
			this.afterPostingAccount = afterPostingAccount;
		}
		
		
		
		
		
		
}
