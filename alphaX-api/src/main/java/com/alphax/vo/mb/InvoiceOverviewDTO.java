package com.alphax.vo.mb;

import org.springframework.validation.annotation.Validated;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Validated
@ApiModel(description = "This Object about Incoming goods Invoice overview details")
public class InvoiceOverviewDTO {

	@ApiModelProperty(value = "Invoice No  - (DB O_SA60 - INVOICENR).")
	private String invoiceNo;

	@ApiModelProperty(value ="Invoice Date - (DB O_SA60- INVOICEDATE)." )
	private String invoiceDate;

	@ApiModelProperty(value ="Maufacturer (OEM) - (DB O_SA60 - OEM)." )
	private String maufacturer;
	
	@ApiModelProperty(value ="Tax Base Value  - (DB O_SA60 - TAXBASEVALUE)." )
	private String taxBaseValue;
	
	@ApiModelProperty(value ="invoice Net Value - (DB O_SA60- INVOICENETVALUE)." )
	private String invoiceNetValue;
	
	@ApiModelProperty(value ="extra Cost - (DB O_SA60- (PACKING+FREIGHTPOSTEXPR+FOBCHARGES+SEAAIRFREIGHT+INSURANCE+CONSFEES+SHIPPCHARGES+CONTCHARGES+OTHERCHARGES))." )
	private String extraCost;
	
	@ApiModelProperty(value ="booked Amount - (DB E_CPSDAT- substr(T1, 9, 7) & substr (T1, 35, 7) )." )
	private String bookedAmount;
	
	@ApiModelProperty(value ="ap_WarehouseNo - (DB O_SA60 - AP_WRH)" )
	private String ap_WarehouseNo;
	
	@ApiModelProperty(value ="invoiceId - (DB O_SA60 - ID)" )
	private String invoiceId;
	
	@ApiModelProperty(value ="STATUS - calculated value" )
	private String invoiceStatus;
	
	@ApiModelProperty(value ="additional Cost Total - (DB  O_ADDCOSTS- SUM(COSTSVALUE))." )
	private String addCostTotal;
	
	@ApiModelProperty(value ="Delivery Note Number - (DB  O_SA61 - DELIVERYNOTENR )." )
	private String deliveryNoteNo;
	
	@ApiModelProperty(value ="Total Net Value Line Item - (DB  O_SA61 - SUM(TOTALNETVALLINEITEM))." )
	private String totalNetValLineItem;
	
	@ApiModelProperty(value ="Delivery Note Total - (DB  E_CPSDAT- substr(T1, 9, 7) & substr (T1, 35, 7) )." )
	private String deliveryNoteTotal;

	
	
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
	 * @return the taxBaseValue
	 */
	public String getTaxBaseValue() {
		return taxBaseValue;
	}

	/**
	 * @param taxBaseValue the taxBaseValue to set
	 */
	public void setTaxBaseValue(String taxBaseValue) {
		this.taxBaseValue = taxBaseValue;
	}

	/**
	 * @return the invoiceNetValue
	 */
	public String getInvoiceNetValue() {
		return invoiceNetValue;
	}

	/**
	 * @param invoiceNetValue the invoiceNetValue to set
	 */
	public void setInvoiceNetValue(String invoiceNetValue) {
		this.invoiceNetValue = invoiceNetValue;
	}

	/**
	 * @return the extraCost
	 */
	public String getExtraCost() {
		return extraCost;
	}

	/**
	 * @param extraCost the extraCost to set
	 */
	public void setExtraCost(String extraCost) {
		this.extraCost = extraCost;
	}

	/**
	 * @return the bookedAmount
	 */
	public String getBookedAmount() {
		return bookedAmount;
	}

	/**
	 * @param bookedAmount the bookedAmount to set
	 */
	public void setBookedAmount(String bookedAmount) {
		this.bookedAmount = bookedAmount;
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
	 * @return the invoiceId
	 */
	public String getInvoiceId() {
		return invoiceId;
	}

	/**
	 * @param invoiceId the invoiceId to set
	 */
	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	/**
	 * @return the invoiceStatus
	 */
	public String getInvoiceStatus() {
		return invoiceStatus;
	}

	/**
	 * @param invoiceStatus the invoiceStatus to set
	 */
	public void setInvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	/**
	 * @return the addCostTotal
	 */
	public String getAddCostTotal() {
		return addCostTotal;
	}

	/**
	 * @param addCostTotal the addCostTotal to set
	 */
	public void setAddCostTotal(String addCostTotal) {
		this.addCostTotal = addCostTotal;
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
	 * @return the totalNetValLineItem
	 */
	public String getTotalNetValLineItem() {
		return totalNetValLineItem;
	}

	/**
	 * @param totalNetValLineItem the totalNetValLineItem to set
	 */
	public void setTotalNetValLineItem(String totalNetValLineItem) {
		this.totalNetValLineItem = totalNetValLineItem;
	}

	/**
	 * @return the deliveryNoteTotal
	 */
	public String getDeliveryNoteTotal() {
		return deliveryNoteTotal;
	}

	/**
	 * @param deliveryNoteTotal the deliveryNoteTotal to set
	 */
	public void setDeliveryNoteTotal(String deliveryNoteTotal) {
		this.deliveryNoteTotal = deliveryNoteTotal;
	}
	
}