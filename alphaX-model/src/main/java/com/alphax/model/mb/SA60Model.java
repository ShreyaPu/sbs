package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description="All fields about Invoice Details")
public class SA60Model {
	
	@DBTable(columnName ="ID", required = true)
	private Long sa60Id;
	
	@DBTable(columnName ="INVOICENR", required = true)
	private String invoiceNo;
	
	@DBTable(columnName ="SUPPLIERLOC", required = true)
	private Integer supplierLoc;
	
	@DBTable(columnName ="AP_WRH", required = true)
	private String ap_WarehouseNo;
	
	@DBTable(columnName ="INVOICEDATE", required = true)
	private String invoiceDate;
	
	@DBTable(columnName ="GUID", required = true)
	private String guId;
	
	@DBTable(columnName ="COMPANYCODE", required = true)
	private Integer companyCode;
	
	@DBTable(columnName ="STATE", required = true)
	private Integer state;
	
	@DBTable(columnName ="PACKING", required = true)
	private BigDecimal packing;
	
	@DBTable(columnName ="FREIGHTPOSTEXPR", required = true)
	private BigDecimal freightPostExpr;
	
	@DBTable(columnName ="FOBCHARGES", required = true)
	private BigDecimal fobCharges;
	
	@DBTable(columnName ="SEAAIRFREIGHT", required = true)
	private BigDecimal seaAirFreight;
	
	@DBTable(columnName ="INSURANCE", required = true)
	private BigDecimal insurance;
	
	@DBTable(columnName ="CONSFEES", required = true)
	private BigDecimal consFees;
	
	@DBTable(columnName ="SHIPPCHARGES", required = true)
	private BigDecimal shippingCharges;
	
	@DBTable(columnName ="CONTCHARGES", required = true)
	private BigDecimal contCharges;
	
	@DBTable(columnName ="OTHERCHARGES", required = true)
	private BigDecimal otherCharges;
	
	@DBTable(columnName ="INVOICENETVALUE", required = true)
	private BigDecimal invoiceNetValue;
	
	@DBTable(columnName ="TAXBASEVALUE", required = true)
	private BigDecimal taxBaseValue;
	
	@DBTable(columnName ="TAXVALUE", required = true)
	private BigDecimal taxValue;
	
	@DBTable(columnName ="VATCODE", required = true)
	private BigDecimal vatCode;
	
	@DBTable(columnName ="OEM", required = true)
	private String maufacturer;
	
	@DBTable(columnName ="ORDERTYPE", required = true)
	private Integer orderType;
	
	@DBTable(columnName ="INVOICETYPE", required = true)
	private String invoiceType;
	
	@DBTable(columnName ="EXTRACOST", required = true)
	private BigDecimal extraCost;
	
	@DBTable(columnName ="BOOKED_AMT", required = true)
	private Double bookedAmount;
	
	@DBTable(columnName ="ADDCOSTTOTAL", required = true)
	private BigDecimal addCostTotal;
	
	
	public SA60Model() {
	}


	/**
	 * @return the sa60Id
	 */
	public Long getSa60Id() {
		return sa60Id;
	}


	/**
	 * @param sa60Id the sa60Id to set
	 */
	public void setSa60Id(Long sa60Id) {
		this.sa60Id = sa60Id;
	}


	/**
	 * @return the invoiceNo
	 */
	public String getInvoiceNo() {
		return invoiceNo;
	}


	/**
	 * @param invoiceNo the invoiceNo to set
	 */
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}


	/**
	 * @return the supplierLoc
	 */
	public Integer getSupplierLoc() {
		return supplierLoc;
	}


	/**
	 * @param supplierLoc the supplierLoc to set
	 */
	public void setSupplierLoc(Integer supplierLoc) {
		this.supplierLoc = supplierLoc;
	}


	/**
	 * @return the ap_WarehouseNo
	 */
	public String getAp_WarehouseNo() {
		return ap_WarehouseNo;
	}


	/**
	 * @param ap_WarehouseNo the ap_WarehouseNo to set
	 */
	public void setAp_WarehouseNo(String ap_WarehouseNo) {
		this.ap_WarehouseNo = ap_WarehouseNo;
	}


	/**
	 * @return the invoiceDate
	 */
	public String getInvoiceDate() {
		return invoiceDate;
	}


	/**
	 * @param invoiceDate the invoiceDate to set
	 */
	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}


	/**
	 * @return the guId
	 */
	public String getGuId() {
		return guId;
	}


	/**
	 * @param guId the guId to set
	 */
	public void setGuId(String guId) {
		this.guId = guId;
	}


	/**
	 * @return the companyCode
	 */
	public Integer getCompanyCode() {
		return companyCode;
	}


	/**
	 * @param companyCode the companyCode to set
	 */
	public void setCompanyCode(Integer companyCode) {
		this.companyCode = companyCode;
	}


	/**
	 * @return the state
	 */
	public Integer getState() {
		return state;
	}


	/**
	 * @param state the state to set
	 */
	public void setState(Integer state) {
		this.state = state;
	}


	/**
	 * @return the packing
	 */
	public BigDecimal getPacking() {
		return packing;
	}


	/**
	 * @param packing the packing to set
	 */
	public void setPacking(BigDecimal packing) {
		this.packing = packing;
	}


	/**
	 * @return the freightPostExpr
	 */
	public BigDecimal getFreightPostExpr() {
		return freightPostExpr;
	}


	/**
	 * @param freightPostExpr the freightPostExpr to set
	 */
	public void setFreightPostExpr(BigDecimal freightPostExpr) {
		this.freightPostExpr = freightPostExpr;
	}


	/**
	 * @return the fobCharges
	 */
	public BigDecimal getFobCharges() {
		return fobCharges;
	}


	/**
	 * @param fobCharges the fobCharges to set
	 */
	public void setFobCharges(BigDecimal fobCharges) {
		this.fobCharges = fobCharges;
	}


	/**
	 * @return the seaAirFreight
	 */
	public BigDecimal getSeaAirFreight() {
		return seaAirFreight;
	}


	/**
	 * @param seaAirFreight the seaAirFreight to set
	 */
	public void setSeaAirFreight(BigDecimal seaAirFreight) {
		this.seaAirFreight = seaAirFreight;
	}


	/**
	 * @return the insurance
	 */
	public BigDecimal getInsurance() {
		return insurance;
	}


	/**
	 * @param insurance the insurance to set
	 */
	public void setInsurance(BigDecimal insurance) {
		this.insurance = insurance;
	}


	/**
	 * @return the consFees
	 */
	public BigDecimal getConsFees() {
		return consFees;
	}


	/**
	 * @param consFees the consFees to set
	 */
	public void setConsFees(BigDecimal consFees) {
		this.consFees = consFees;
	}


	/**
	 * @return the shippingCharges
	 */
	public BigDecimal getShippingCharges() {
		return shippingCharges;
	}



	/**
	 * @param shippingCharges the shippingCharges to set
	 */
	public void setShippingCharges(BigDecimal shippingCharges) {
		this.shippingCharges = shippingCharges;
	}


	/**
	 * @return the contCharges
	 */
	public BigDecimal getContCharges() {
		return contCharges;
	}


	/**
	 * @param contCharges the contCharges to set
	 */
	public void setContCharges(BigDecimal contCharges) {
		this.contCharges = contCharges;
	}


	/**
	 * @return the otherCharges
	 */
	public BigDecimal getOtherCharges() {
		return otherCharges;
	}


	/**
	 * @param otherCharges the otherCharges to set
	 */
	public void setOtherCharges(BigDecimal otherCharges) {
		this.otherCharges = otherCharges;
	}


	/**
	 * @return the invoiceNetValue
	 */
	public BigDecimal getInvoiceNetValue() {
		return invoiceNetValue;
	}


	/**
	 * @param invoiceNetValue the invoiceNetValue to set
	 */
	public void setInvoiceNetValue(BigDecimal invoiceNetValue) {
		this.invoiceNetValue = invoiceNetValue;
	}


	/**
	 * @return the taxBaseValue
	 */
	public BigDecimal getTaxBaseValue() {
		return taxBaseValue;
	}


	/**
	 * @param taxBaseValue the taxBaseValue to set
	 */
	public void setTaxBaseValue(BigDecimal taxBaseValue) {
		this.taxBaseValue = taxBaseValue;
	}


	/**
	 * @return the taxValue
	 */
	public BigDecimal getTaxValue() {
		return taxValue;
	}


	/**
	 * @param taxValue the taxValue to set
	 */
	public void setTaxValue(BigDecimal taxValue) {
		this.taxValue = taxValue;
	}


	/**
	 * @return the vatCode
	 */
	public BigDecimal getVatCode() {
		return vatCode;
	}


	/**
	 * @param vatCode the vatCode to set
	 */
	public void setVatCode(BigDecimal vatCode) {
		this.vatCode = vatCode;
	}


	/**
	 * @return the maufacturer
	 */
	public String getMaufacturer() {
		return maufacturer;
	}


	/**
	 * @param maufacturer the maufacturer to set
	 */
	public void setMaufacturer(String maufacturer) {
		this.maufacturer = maufacturer;
	}


	/**
	 * @return the orderType
	 */
	public Integer getOrderType() {
		return orderType;
	}


	/**
	 * @param orderType the orderType to set
	 */
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}


	/**
	 * @return the invoiceType
	 */
	public String getInvoiceType() {
		return invoiceType;
	}


	/**
	 * @param invoiceType the invoiceType to set
	 */
	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}


	/**
	 * @return the extraCost
	 */
	public BigDecimal getExtraCost() {
		return extraCost;
	}


	/**
	 * @param extraCost the extraCost to set
	 */
	public void setExtraCost(BigDecimal extraCost) {
		this.extraCost = extraCost;
	}


	/**
	 * @return the bookedAmount
	 */
	public Double getBookedAmount() {
		return bookedAmount;
	}


	/**
	 * @param bookedAmount the bookedAmount to set
	 */
	public void setBookedAmount(Double bookedAmount) {
		this.bookedAmount = bookedAmount;
	}


	/**
	 * @return the addCostTotal
	 */
	public BigDecimal getAddCostTotal() {
		return addCostTotal;
	}


	/**
	 * @param addCostTotal the addCostTotal to set
	 */
	public void setAddCostTotal(BigDecimal addCostTotal) {
		this.addCostTotal = addCostTotal;
	}
	
}