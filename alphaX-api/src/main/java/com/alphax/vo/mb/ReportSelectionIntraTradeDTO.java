package com.alphax.vo.mb;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about Report Selections IntraTrade Statistics Details")
public class ReportSelectionIntraTradeDTO {

	private String documentNumber;
	
	private String baNumber;
	
	private String partNumber;
	
	private String partName;
	
	private String customsNumber;

	private String es1;
	
	private String es2;
	
	private String statsDate;
	
	private String customerNumber;
	
	private String InvoicingAmount;
	
	private String numberofPositions;
	
	private String total;
	
	
	public ReportSelectionIntraTradeDTO() {}


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
	public String getBaNumber() {
		return baNumber;
	}


	/**
	 * @param baNumber the baNumber to set
	 */
	public void setBaNumber(String baNumber) {
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
	public String getNumberofPositions() {
		return numberofPositions;
	}


	/**
	 * @param numberofPositions the numberofPositions to set
	 */
	public void setNumberofPositions(String numberofPositions) {
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


	
	
}