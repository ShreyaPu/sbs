package com.alphax.model.mb;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;
@ApiModel(description = "All fields about Audit invoice")
public class AuditInvoice {

	@DBTable(columnName ="INVOICENR", required = true)
	private String invoiceNumber;
	
	@DBTable(columnName ="SUPPLIERLOC", required = true)
	private String supplier;

	@DBTable(columnName ="INVOICEDATE", required = true)
	private String invoiceDate;
	
	@DBTable(columnName ="ID", required = true)
	private String id;

	@DBTable(columnName ="GUID", required = true)
	private String guID;

	@DBTable(columnName ="COMPANYCODE", required = true)
	private String companycode;
	
	@DBTable(columnName ="STATE", required = true)
	private String state;
	
	@DBTable(columnName ="UNITPRICE", required = true)
	private String partCost;									
		
	@DBTable(columnName ="OTHERCHARGES", required = true)
	private String additinalCost;

	@DBTable(columnName ="PACKING ", required = true)
	private String packing;
	
	@DBTable(columnName ="FREIGHTPOSTEXPR ", required = true)
	private String frightPostExpr;
	
	@DBTable(columnName ="FOBCHARGES", required = true)
	private String fobCharges;
	
	@DBTable(columnName ="SEAAIRFREIGHT", required = true)
	private String seaAirfright;
	
	@DBTable(columnName ="INSURANCE", required = true)
	private String insurance;
	
	@DBTable(columnName ="CONSFEES", required = true)
	private String confees;
	
	@DBTable(columnName ="SHIPPCHARGES", required = true)
	private String shipCharges;
	
	@DBTable(columnName ="CONTCHARGES ", required = true)
	private String contCharges;
	
	@DBTable(columnName ="OTHERCHARGES ", required = true)
	private String otherCharges;
	
	@DBTable(columnName ="INVOICENETVALUE ", required = true)
	private String invoiceNetValue;
	
	@DBTable(columnName ="TAXBASEVALUE ", required = true)
	private String taxBaseValue;
	
	@DBTable(columnName ="OEM", required = true)
	private String oem;
	
	@DBTable(columnName ="DESCRIPTION", required = true)
	private String description;
	
	@DBTable(columnName ="INVOICEDQTY", required = true)
	private String invoiceDqty;
	
	@DBTable(columnName ="TOTALNETVALLINEITEM", required = true)
	private String totalNetValueLineItem;

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

	public String getFrightPostExpr() {
		return frightPostExpr;
	}

	public void setFrightPostExpr(String frightPostExpr) {
		this.frightPostExpr = frightPostExpr;
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
	
	
}
