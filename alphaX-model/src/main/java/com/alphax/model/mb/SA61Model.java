package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description="All fields about Invoice Details SA61")
public class SA61Model {
	
	@DBTable(columnName ="ID", required = true)
	private Long sa61Id;
	
	@DBTable(columnName ="INVOICENR", required = true)
	private String invoiceNo;
	
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
	
	@DBTable(columnName ="INVOICEDQTY", required = true)
	private BigDecimal invoicedQty;
	
	@DBTable(columnName ="TOTALVALUELINEITEM", required = true)
	private BigDecimal totalValueLineItem;
	
	@DBTable(columnName ="TOTALNETVALLINEITEM", required = true)
	private BigDecimal totalNetValLineItem;
	
	@DBTable(columnName ="UNITPRICE", required = true)
	private BigDecimal unitPrice;
	
	@DBTable(columnName ="UNITWEIGHTLINEITEM", required = true)
	private BigDecimal unitWeightLineItem;
	
	@DBTable(columnName ="VATCODE", required = true)
	private BigDecimal vatCode;
	
	@DBTable(columnName ="OEM", required = true)
	private String maufacturer;
	
	@DBTable(columnName ="ORDERTYPE", required = true)
	private Integer orderType;
	
	@DBTable(columnName ="DELIVERYNOTENR", required = true)
	private String deliveryNoteNo;
	
	@DBTable(columnName ="DELIVEREDPARTNR", required = true)
	private String deliveredPartNo;
	
	@DBTable(columnName ="ORIGIN", required = true)
	private Integer origin;
	
	@DBTable(columnName ="PREFERENCEKEY", required = true)
	private String preferenceKey;
	
	@DBTable(columnName ="LIEFERSCHEINSUMME", required = true)
	private Double deliveryNoteTotal;
	
	
	
	public SA61Model() {
	}


	/**
	 * @return the sa61Id
	 */
	public Long getSa61Id() {
		return sa61Id;
	}


	/**
	 * @param sa61Id the sa61Id to set
	 */
	public void setSa61Id(Long sa61Id) {
		this.sa61Id = sa61Id;
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
	 * @return the invoicedQty
	 */
	public BigDecimal getInvoicedQty() {
		return invoicedQty;
	}


	/**
	 * @param invoicedQty the invoicedQty to set
	 */
	public void setInvoicedQty(BigDecimal invoicedQty) {
		this.invoicedQty = invoicedQty;
	}


	/**
	 * @return the totalValueLineItem
	 */
	public BigDecimal getTotalValueLineItem() {
		return totalValueLineItem;
	}


	/**
	 * @param totalValueLineItem the totalValueLineItem to set
	 */
	public void setTotalValueLineItem(BigDecimal totalValueLineItem) {
		this.totalValueLineItem = totalValueLineItem;
	}


	/**
	 * @return the totalNetValLineItem
	 */
	public BigDecimal getTotalNetValLineItem() {
		return totalNetValLineItem;
	}


	/**
	 * @param totalNetValLineItem the totalNetValLineItem to set
	 */
	public void setTotalNetValLineItem(BigDecimal totalNetValLineItem) {
		this.totalNetValLineItem = totalNetValLineItem;
	}


	/**
	 * @return the unitPrice
	 */
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}


	/**
	 * @param unitPrice the unitPrice to set
	 */
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}


	/**
	 * @return the unitWeightLineItem
	 */
	public BigDecimal getUnitWeightLineItem() {
		return unitWeightLineItem;
	}


	/**
	 * @param unitWeightLineItem the unitWeightLineItem to set
	 */
	public void setUnitWeightLineItem(BigDecimal unitWeightLineItem) {
		this.unitWeightLineItem = unitWeightLineItem;
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
	 * @return the deliveryNoteNo
	 */
	public String getDeliveryNoteNo() {
		return deliveryNoteNo;
	}


	/**
	 * @param deliveryNoteNo the deliveryNoteNo to set
	 */
	public void setDeliveryNoteNo(String deliveryNoteNo) {
		this.deliveryNoteNo = deliveryNoteNo;
	}


	/**
	 * @return the deliveredPartNo
	 */
	public String getDeliveredPartNo() {
		return deliveredPartNo;
	}


	/**
	 * @param deliveredPartNo the deliveredPartNo to set
	 */
	public void setDeliveredPartNo(String deliveredPartNo) {
		this.deliveredPartNo = deliveredPartNo;
	}


	/**
	 * @return the origin
	 */
	public Integer getOrigin() {
		return origin;
	}


	/**
	 * @param origin the origin to set
	 */
	public void setOrigin(Integer origin) {
		this.origin = origin;
	}


	/**
	 * @return the preferenceKey
	 */
	public String getPreferenceKey() {
		return preferenceKey;
	}


	/**
	 * @param preferenceKey the preferenceKey to set
	 */
	public void setPreferenceKey(String preferenceKey) {
		this.preferenceKey = preferenceKey;
	}


	/**
	 * @return the deliveryNoteTotal
	 */
	public Double getDeliveryNoteTotal() {
		return deliveryNoteTotal;
	}


	/**
	 * @param deliveryNoteTotal the deliveryNoteTotal to set
	 */
	public void setDeliveryNoteTotal(Double deliveryNoteTotal) {
		this.deliveryNoteTotal = deliveryNoteTotal;
	}

}