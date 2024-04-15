package com.alphax.vo.mb;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about Audit invoice DTO")
public class AuditInvoiceDTO {
	private String invoiceNumber;
    
	private String supplier;

	private String invoiceDate;

	private String warehouseNumber;

	private String id;

	private String guID;

	private String companycode;

	private String state;

	private String partCost;

	private String additinalCost;

	private String packing;
	
	private String freightPostExpr;
	
	private String fobCharges; 
	
	private String seaAirfright;
	
	private String insurance;
	
	private String confees;
	
	private String shipCharges;
	
	private String contCharges;
	
	private String otherCharges;
	
	private String invoiceNetValue;
	
	private String taxBaseValue;
	
	private String oem;
	
	private String description;
	
	private String invoiceDqty;
	
	private String totalNetValueLineItem;
	
	private String packingText;
	
	private String packingAccount;
	
	private String freightPostExprText;
	
	private String freightPostExprAccount;
	
	private String fobChargesText;
	
	private String fobChargesAccount;
	
	private String seaAirfrightText;
	
	private String seaAirfrightAccount;
	
	private String insuranceText;
	
	private String insuranceAccount;
	
	private String confeesText;
	
	private String confeesAccount;
	
	private String shipChargesText;
	
	private String shipChargesAccount;
	
	private String contChargesText;
	
	private String contChargesAccount;
	
	private String otherChargesText;
	
	private String otherChargesAccount;
	
	private String totalInvoiceAmount;
	
	private String partsCostsWith7PerVAT;
	
	private String partsCostsWith19PerVAT;
	

	public String getTotalNetValueLineItem() {
		return totalNetValueLineItem;
	}

	public void setTotalNetValueLineItem(String totalNetValueLineItem) {
		this.totalNetValueLineItem = totalNetValueLineItem;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getWarehouseNumber() {
		return warehouseNumber;
	}

	public void setWarehouseNumber(String warehouseNumber) {
		this.warehouseNumber = warehouseNumber;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGuID() {
		return guID;
	}

	public void setGuID(String guID) {
		this.guID = guID;
	}

	public String getCompanycode() {
		return companycode;
	}

	public void setCompanycode(String companycode) {
		this.companycode = companycode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPartCost() {
		return partCost;
	}

	public void setPartCost(String partCost) {
		this.partCost = partCost;
	}

	public String getAdditinalCost() {
		return additinalCost;
	}

	public void setAdditinalCost(String additinalCost) {
		this.additinalCost = additinalCost;
	}

	public String getPacking() {
		return packing;
	}

	public void setPacking(String packing) {
		this.packing = packing;
	}

	public String getFreightPostExpr() {
		return freightPostExpr;
	}

	public void setFreightPostExpr(String freightPostExpr) {
		this.freightPostExpr = freightPostExpr;
	}

	public String getFobCharges() {
		return fobCharges;
	}

	public void setFobCharges(String fobCharges) {
		this.fobCharges = fobCharges;
	}

	public String getSeaAirfright() {
		return seaAirfright;
	}

	public void setSeaAirfright(String seaAirfright) {
		this.seaAirfright = seaAirfright;
	}

	public String getInsurance() {
		return insurance;
	}

	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}

	public String getConfees() {
		return confees;
	}

	public void setConfees(String confees) {
		this.confees = confees;
	}

	public String getShipCharges() {
		return shipCharges;
	}

	public void setShipCharges(String shipCharges) {
		this.shipCharges = shipCharges;
	}

	public String getContCharges() {
		return contCharges;
	}

	public void setContCharges(String contCharges) {
		this.contCharges = contCharges;
	}

	public String getOtherCharges() {
		return otherCharges;
	}

	public void setOtherCharges(String otherCharges) {
		this.otherCharges = otherCharges;
	}

	public String getInvoiceNetValue() {
		return invoiceNetValue;
	}

	public void setInvoiceNetValue(String invoiceNetValue) {
		this.invoiceNetValue = invoiceNetValue;
	}

	public String getTaxBaseValue() {
		return taxBaseValue;
	}

	public void setTaxBaseValue(String taxBaseValue) {
		this.taxBaseValue = taxBaseValue;
	}

	public String getOem() {
		return oem;
	}

	public void setOem(String oem) {
		this.oem = oem;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getInvoiceDqty() {
		return invoiceDqty;
	}

	public void setInvoiceDqty(String invoiceDqty) {
		this.invoiceDqty = invoiceDqty;
	}

	/**
	 * @return the packingText
	 */
	public String getPackingText() {
		return packingText;
	}

	/**
	 * @param packingText the packingText to set
	 */
	public void setPackingText(String packingText) {
		this.packingText = packingText;
	}

	/**
	 * @return the packingAccount
	 */
	public String getPackingAccount() {
		return packingAccount;
	}

	/**
	 * @param packingAccount the packingAccount to set
	 */
	public void setPackingAccount(String packingAccount) {
		this.packingAccount = packingAccount;
	}

	

	/**
	 * @return the freightPostExprText
	 */
	public String getFreightPostExprText() {
		return freightPostExprText;
	}

	/**
	 * @param freightPostExprText the freightPostExprText to set
	 */
	public void setFreightPostExprText(String freightPostExprText) {
		this.freightPostExprText = freightPostExprText;
	}

	/**
	 * @return the freightPostExprAccount
	 */
	public String getFreightPostExprAccount() {
		return freightPostExprAccount;
	}

	/**
	 * @param freightPostExprAccount the freightPostExprAccount to set
	 */
	public void setFreightPostExprAccount(String freightPostExprAccount) {
		this.freightPostExprAccount = freightPostExprAccount;
	}

	/**
	 * @return the fobChargesText
	 */
	public String getFobChargesText() {
		return fobChargesText;
	}

	/**
	 * @param fobChargesText the fobChargesText to set
	 */
	public void setFobChargesText(String fobChargesText) {
		this.fobChargesText = fobChargesText;
	}

	/**
	 * @return the fobChargesAccount
	 */
	public String getFobChargesAccount() {
		return fobChargesAccount;
	}

	/**
	 * @param fobChargesAccount the fobChargesAccount to set
	 */
	public void setFobChargesAccount(String fobChargesAccount) {
		this.fobChargesAccount = fobChargesAccount;
	}

	/**
	 * @return the seaAirfrightText
	 */
	public String getSeaAirfrightText() {
		return seaAirfrightText;
	}

	/**
	 * @param seaAirfrightText the seaAirfrightText to set
	 */
	public void setSeaAirfrightText(String seaAirfrightText) {
		this.seaAirfrightText = seaAirfrightText;
	}

	/**
	 * @return the seaAirfrightAccount
	 */
	public String getSeaAirfrightAccount() {
		return seaAirfrightAccount;
	}

	/**
	 * @param seaAirfrightAccount the seaAirfrightAccount to set
	 */
	public void setSeaAirfrightAccount(String seaAirfrightAccount) {
		this.seaAirfrightAccount = seaAirfrightAccount;
	}

	/**
	 * @return the insuranceText
	 */
	public String getInsuranceText() {
		return insuranceText;
	}

	/**
	 * @param insuranceText the insuranceText to set
	 */
	public void setInsuranceText(String insuranceText) {
		this.insuranceText = insuranceText;
	}

	/**
	 * @return the insuranceAccount
	 */
	public String getInsuranceAccount() {
		return insuranceAccount;
	}

	/**
	 * @param insuranceAccount the insuranceAccount to set
	 */
	public void setInsuranceAccount(String insuranceAccount) {
		this.insuranceAccount = insuranceAccount;
	}

	/**
	 * @return the confeesText
	 */
	public String getConfeesText() {
		return confeesText;
	}

	/**
	 * @param confeesText the confeesText to set
	 */
	public void setConfeesText(String confeesText) {
		this.confeesText = confeesText;
	}

	/**
	 * @return the confeesAccount
	 */
	public String getConfeesAccount() {
		return confeesAccount;
	}

	/**
	 * @param confeesAccount the confeesAccount to set
	 */
	public void setConfeesAccount(String confeesAccount) {
		this.confeesAccount = confeesAccount;
	}

	/**
	 * @return the shipChargesText
	 */
	public String getShipChargesText() {
		return shipChargesText;
	}

	/**
	 * @param shipChargesText the shipChargesText to set
	 */
	public void setShipChargesText(String shipChargesText) {
		this.shipChargesText = shipChargesText;
	}

	/**
	 * @return the shipChargesAccount
	 */
	public String getShipChargesAccount() {
		return shipChargesAccount;
	}

	/**
	 * @param shipChargesAccount the shipChargesAccount to set
	 */
	public void setShipChargesAccount(String shipChargesAccount) {
		this.shipChargesAccount = shipChargesAccount;
	}

	/**
	 * @return the contChargesText
	 */
	public String getContChargesText() {
		return contChargesText;
	}

	/**
	 * @param contChargesText the contChargesText to set
	 */
	public void setContChargesText(String contChargesText) {
		this.contChargesText = contChargesText;
	}

	/**
	 * @return the contChargesAccount
	 */
	public String getContChargesAccount() {
		return contChargesAccount;
	}

	/**
	 * @param contChargesAccount the contChargesAccount to set
	 */
	public void setContChargesAccount(String contChargesAccount) {
		this.contChargesAccount = contChargesAccount;
	}

	/**
	 * @return the otherChargesText
	 */
	public String getOtherChargesText() {
		return otherChargesText;
	}

	/**
	 * @param otherChargesText the otherChargesText to set
	 */
	public void setOtherChargesText(String otherChargesText) {
		this.otherChargesText = otherChargesText;
	}

	/**
	 * @return the otherChargesAccount
	 */
	public String getOtherChargesAccount() {
		return otherChargesAccount;
	}

	/**
	 * @param otherChargesAccount the otherChargesAccount to set
	 */
	public void setOtherChargesAccount(String otherChargesAccount) {
		this.otherChargesAccount = otherChargesAccount;
	}

	/**
	 * @return the totalInvoiceAmount
	 */
	public String getTotalInvoiceAmount() {
		return totalInvoiceAmount;
	}

	/**
	 * @param totalInvoiceAmount the totalInvoiceAmount to set
	 */
	public void setTotalInvoiceAmount(String totalInvoiceAmount) {
		this.totalInvoiceAmount = totalInvoiceAmount;
	}

	/**
	 * @return the partsCostsWith7PerVAT
	 */
	public String getPartsCostsWith7PerVAT() {
		return partsCostsWith7PerVAT;
	}

	/**
	 * @param partsCostsWith7PerVAT the partsCostsWith7PerVAT to set
	 */
	public void setPartsCostsWith7PerVAT(String partsCostsWith7PerVAT) {
		this.partsCostsWith7PerVAT = partsCostsWith7PerVAT;
	}

	/**
	 * @return the partsCostsWith19PerVAT
	 */
	public String getPartsCostsWith19PerVAT() {
		return partsCostsWith19PerVAT;
	}

	/**
	 * @param partsCostsWith19PerVAT the partsCostsWith19PerVAT to set
	 */
	public void setPartsCostsWith19PerVAT(String partsCostsWith19PerVAT) {
		this.partsCostsWith19PerVAT = partsCostsWith19PerVAT;
	}

	
	

	
	
}
