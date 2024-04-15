package com.alphax.model.mb;

import java.math.BigDecimal;
import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about Report Selections IntraTrade Statistics List Details")
public class ReportSelectionIntraTradeList {

	@DBTable(columnName = "Belegnummer", required = true)
	private String documentNumber;
	
	@DBTable(columnName = "BA_Nummer", required = true)
	private BigDecimal baNumber;
	
	@DBTable(columnName = "Teilenummer", required = true)
	private String partNumber;
	
	@DBTable(columnName = "Bezeichnung", required = true)
	private String partName;
	
	@DBTable(columnName = "Zollnummer", required = true)
	private String customsNumber;

	@DBTable(columnName = "ES1", required = true)
	private String es1;
	
	@DBTable(columnName = "ES2", required = true)
	private String es2;
	
	@DBTable(columnName = "Datum", required = true)
	private String statsDate;
	
	@DBTable(columnName = "Kunde", required = true)
	private String customerNumber;
	
	@DBTable(columnName = "Fakturierungsbetrag", required = true)
	private String InvoicingAmount;
	
	@DBTable(columnName = "Anzahl", required = true)
	private Integer numberofPositions;
	
	@DBTable(columnName = "Summe", required = true)
	private String total;
	
	@DBTable(columnName ="ROWNUMER", required = true)
	private Integer totalCount;
	
	
	public ReportSelectionIntraTradeList() {}


	/**
	 * @return the documentNumber
	 */
	public String getDocumentNumber() {
		return documentNumber;
	}


	/**
	 * @param documentNumber the documentNumber to set
	 */
	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}


	/**
	 * @return the baNumber
	 */
	public BigDecimal getBaNumber() {
		return baNumber;
	}


	/**
	 * @param baNumber the baNumber to set
	 */
	public void setBaNumber(BigDecimal baNumber) {
		this.baNumber = baNumber;
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
	 * @return the customsNumber
	 */
	public String getCustomsNumber() {
		return customsNumber;
	}


	/**
	 * @param customsNumber the customsNumber to set
	 */
	public void setCustomsNumber(String customsNumber) {
		this.customsNumber = customsNumber;
	}


	/**
	 * @return the es1
	 */
	public String getEs1() {
		return es1;
	}


	/**
	 * @param es1 the es1 to set
	 */
	public void setEs1(String es1) {
		this.es1 = es1;
	}


	/**
	 * @return the es2
	 */
	public String getEs2() {
		return es2;
	}


	/**
	 * @param es2 the es2 to set
	 */
	public void setEs2(String es2) {
		this.es2 = es2;
	}


	/**
	 * @return the statsDate
	 */
	public String getStatsDate() {
		return statsDate;
	}


	/**
	 * @param statsDate the statsDate to set
	 */
	public void setStatsDate(String statsDate) {
		this.statsDate = statsDate;
	}


	/**
	 * @return the customerNumber
	 */
	public String getCustomerNumber() {
		return customerNumber;
	}


	/**
	 * @param customerNumber the customerNumber to set
	 */
	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}


	/**
	 * @return the invoicingAmount
	 */
	public String getInvoicingAmount() {
		return InvoicingAmount;
	}


	/**
	 * @param invoicingAmount the invoicingAmount to set
	 */
	public void setInvoicingAmount(String invoicingAmount) {
		InvoicingAmount = invoicingAmount;
	}


	/**
	 * @return the numberofPositions
	 */
	public Integer getNumberofPositions() {
		return numberofPositions;
	}


	/**
	 * @param numberofPositions the numberofPositions to set
	 */
	public void setNumberofPositions(Integer numberofPositions) {
		this.numberofPositions = numberofPositions;
	}


	/**
	 * @return the total
	 */
	public String getTotal() {
		return total;
	}


	/**
	 * @param total the total to set
	 */
	public void setTotal(String total) {
		this.total = total;
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
	
	
}