package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

public class MovementData {

	@DBTable(columnName = "HERST", required = true)
	private String manufacturer;
	
	@DBTable(columnName = "VFNR", required = true)
	private BigDecimal warehouse;
	
	@DBTable(columnName = "BART", required = true)
	private BigDecimal businesscase;
	
	@DBTable(columnName = "BELEGNUMMER", required = true)
	private String documentNumber;
	
	@DBTable(columnName = "AUFTRAGSNUMMER", required = true)
	private String orderNumber;
	
	@DBTable(columnName = "BSN_LC", required = true)
	private String supplier;
	
	@DBTable(columnName = "JJMMTT", required = true)
	private BigDecimal date;
	
	@DBTable(columnName = "HHMMSS", required = true)
	private BigDecimal time;	
	
	@DBTable(columnName = "BHW", required = true)
	private String bhw;
	
	@DBTable(columnName = "MENGE", required = true)
	private Double quantity;
	
	@DBTable(columnName = "KUNDENNUMMER", required = true)
	private String customerNumber;
	
	@DBTable(columnName = "KUNDE", required = true)
	private String customerName;
	
	@DBTable(columnName = "LIEFERANTENNAME", required = true)
	private String supplierName;	
	
	@DBTable(columnName = "MA_CHANGED", required = true)
	private Integer ma_Changed;
	
	@DBTable(columnName = "MA_OLD", required = true)
	private String ma_Old;
	
	@DBTable(columnName = "MA_NEW", required = true)
	private String ma_New;
	
	@DBTable(columnName = "RG_CHANGED", required = true)
	private Integer rg_Changed;
	
	@DBTable(columnName = "RG_OLD", required = true)
	private String rg_Old;
	
	@DBTable(columnName = "RG_NEW", required = true)
	private String rg_New;
	
	@DBTable(columnName = "TA_CHANGED", required = true)
	private Integer ta_Changed;
	
	@DBTable(columnName = "TA_OLD", required = true)
	private String ta_Old;
	
	@DBTable(columnName = "TA_NEW", required = true)
	private String ta_New;
	
	
	@DBTable(columnName = "LA_CHANGED", required = true)
	private Integer la_Changed;
	
	@DBTable(columnName = "LA_OLD", required = true)
	private BigDecimal la_Old;
	
	@DBTable(columnName = "LA_NEW", required = true)
	private BigDecimal la_New;
	
	@DBTable(columnName = "MC_CHANGED", required = true)
	private Integer mc_Changed;
	
	@DBTable(columnName = "MC_OLD", required = true)
	private String mc_Old;
	
	@DBTable(columnName = "MC_NEW", required = true)
	private String mc_New;
	
	@DBTable(columnName = "KTO_CHANGED", required = true)
	private Integer kto_Changed;
	
	@DBTable(columnName = "KTO_OLD", required = true)
	private BigDecimal kto_Old;
	
	@DBTable(columnName = "KTO_NEW", required = true)
	private BigDecimal kto_New;
	
	@DBTable(columnName = "EPR_CHANGED", required = true)
	private Integer epr_Changed;
	
	@DBTable(columnName = "EPR_OLD", required = true)
	private BigDecimal epr_Old;
	
	@DBTable(columnName = "EPR_NEW", required = true)
	private BigDecimal epr_New;
	
	@DBTable(columnName = "NPR_CHANGED", required = true)
	private Integer npr_Changed;
	
	@DBTable(columnName = "NPR_OLD", required = true)
	private String npr_Old;
	
	@DBTable(columnName = "NPR_NEW", required = true)
	private String npr_New;
	
	@DBTable(columnName = "EKNPR_CHANGED", required = true)
	private Integer eknpr_Changed;
		
	@DBTable(columnName = "EKNPR_OLD", required = true)
	private BigDecimal eknpr_Old;
		
	@DBTable(columnName = "EKNPR_NEW", required = true)
	private BigDecimal eknpr_New;
	
	@DBTable(columnName = "DAK_CHANGED", required = true)
	private Integer dak_Changed;
		
	@DBTable(columnName = "DAK_OLD", required = true)
	private String dak_Old;
		
	@DBTable(columnName = "DAK_NEW", required = true)
	private String dak_New;
	
	@DBTable(columnName ="ROWNUMER", required = true)
	private Integer totalCount;
	

	public MovementData() {
	
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
	 * @return the warehouse
	 */
	public BigDecimal getWarehouse() {
		return warehouse;
	}



	/**
	 * @param warehouse the warehouse to set
	 */
	public void setWarehouse(BigDecimal warehouse) {
		this.warehouse = warehouse;
	}



	/**
	 * @return the businesscase
	 */
	public BigDecimal getBusinesscase() {
		return businesscase;
	}



	/**
	 * @param businesscase the businesscase to set
	 */
	public void setBusinesscase(BigDecimal businesscase) {
		this.businesscase = businesscase;
	}


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
	 * @return the supplier
	 */
	public String getSupplier() {
		return supplier;
	}



	/**
	 * @param supplier the supplier to set
	 */
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}



	/**
	 * @return the date
	 */
	public BigDecimal getDate() {
		return date;
	}



	/**
	 * @param date the date to set
	 */
	public void setDate(BigDecimal date) {
		this.date = date;
	}



	/**
	 * @return the time
	 */
	public BigDecimal getTime() {
		return time;
	}



	/**
	 * @param time the time to set
	 */
	public void setTime(BigDecimal time) {
		this.time = time;
	}



	/**
	 * @return the orderNumber
	 */
	public String getOrderNumber() {
		return orderNumber;
	}



	/**
	 * @param orderNumber the orderNumber to set
	 */
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}



	/**
	 * @return the bhw
	 */
	public String getBhw() {
		return bhw;
	}



	/**
	 * @param bhw the bhw to set
	 */
	public void setBhw(String bhw) {
		this.bhw = bhw;
	}



	/**
	 * @return the quantity
	 */
	public Double getQuantity() {
		return quantity;
	}



	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}



	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}



	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}



	/**
	 * @return the supplierName
	 */
	public String getSupplierName() {
		return supplierName;
	}



	/**
	 * @param supplierName the supplierName to set
	 */
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
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
	 * @return the ma_Changed
	 */
	public Integer getMa_Changed() {
		return ma_Changed;
	}



	/**
	 * @param ma_Changed the ma_Changed to set
	 */
	public void setMa_Changed(Integer ma_Changed) {
		this.ma_Changed = ma_Changed;
	}



	/**
	 * @return the ma_Old
	 */
	public String getMa_Old() {
		return ma_Old;
	}



	/**
	 * @param ma_Old the ma_Old to set
	 */
	public void setMa_Old(String ma_Old) {
		this.ma_Old = ma_Old;
	}



	/**
	 * @return the ma_New
	 */
	public String getMa_New() {
		return ma_New;
	}



	/**
	 * @param ma_New the ma_New to set
	 */
	public void setMa_New(String ma_New) {
		this.ma_New = ma_New;
	}



	/**
	 * @return the rg_Changed
	 */
	public Integer getRg_Changed() {
		return rg_Changed;
	}



	/**
	 * @param rg_Changed the rg_Changed to set
	 */
	public void setRg_Changed(Integer rg_Changed) {
		this.rg_Changed = rg_Changed;
	}



	/**
	 * @return the rg_Old
	 */
	public String getRg_Old() {
		return rg_Old;
	}



	/**
	 * @param rg_Old the rg_Old to set
	 */
	public void setRg_Old(String rg_Old) {
		this.rg_Old = rg_Old;
	}



	/**
	 * @return the rg_New
	 */
	public String getRg_New() {
		return rg_New;
	}



	/**
	 * @param rg_New the rg_New to set
	 */
	public void setRg_New(String rg_New) {
		this.rg_New = rg_New;
	}



	/**
	 * @return the ta_Changed
	 */
	public Integer getTa_Changed() {
		return ta_Changed;
	}



	/**
	 * @param ta_Changed the ta_Changed to set
	 */
	public void setTa_Changed(Integer ta_Changed) {
		this.ta_Changed = ta_Changed;
	}



	/**
	 * @return the ta_Old
	 */
	public String getTa_Old() {
		return ta_Old;
	}



	/**
	 * @param ta_Old the ta_Old to set
	 */
	public void setTa_Old(String ta_Old) {
		this.ta_Old = ta_Old;
	}



	/**
	 * @return the ta_New
	 */
	public String getTa_New() {
		return ta_New;
	}



	/**
	 * @param ta_New the ta_New to set
	 */
	public void setTa_New(String ta_New) {
		this.ta_New = ta_New;
	}



	/**
	 * @return the la_Changed
	 */
	public Integer getLa_Changed() {
		return la_Changed;
	}



	/**
	 * @param la_Changed the la_Changed to set
	 */
	public void setLa_Changed(Integer la_Changed) {
		this.la_Changed = la_Changed;
	}



	/**
	 * @return the la_Old
	 */
	public BigDecimal getLa_Old() {
		return la_Old;
	}



	/**
	 * @param la_Old the la_Old to set
	 */
	public void setLa_Old(BigDecimal la_Old) {
		this.la_Old = la_Old;
	}



	/**
	 * @return the la_New
	 */
	public BigDecimal getLa_New() {
		return la_New;
	}



	/**
	 * @param la_New the la_New to set
	 */
	public void setLa_New(BigDecimal la_New) {
		this.la_New = la_New;
	}



	/**
	 * @return the mc_Changed
	 */
	public Integer getMc_Changed() {
		return mc_Changed;
	}



	/**
	 * @param mc_Changed the mc_Changed to set
	 */
	public void setMc_Changed(Integer mc_Changed) {
		this.mc_Changed = mc_Changed;
	}



	/**
	 * @return the mc_Old
	 */
	public String getMc_Old() {
		return mc_Old;
	}



	/**
	 * @param mc_Old the mc_Old to set
	 */
	public void setMc_Old(String mc_Old) {
		this.mc_Old = mc_Old;
	}



	/**
	 * @return the mc_New
	 */
	public String getMc_New() {
		return mc_New;
	}



	/**
	 * @param mc_New the mc_New to set
	 */
	public void setMc_New(String mc_New) {
		this.mc_New = mc_New;
	}



	/**
	 * @return the kto_Changed
	 */
	public Integer getKto_Changed() {
		return kto_Changed;
	}



	/**
	 * @param kto_Changed the kto_Changed to set
	 */
	public void setKto_Changed(Integer kto_Changed) {
		this.kto_Changed = kto_Changed;
	}



	/**
	 * @return the kto_Old
	 */
	public BigDecimal getKto_Old() {
		return kto_Old;
	}



	/**
	 * @param kto_Old the kto_Old to set
	 */
	public void setKto_Old(BigDecimal kto_Old) {
		this.kto_Old = kto_Old;
	}



	/**
	 * @return the kto_New
	 */
	public BigDecimal getKto_New() {
		return kto_New;
	}



	/**
	 * @param kto_New the kto_New to set
	 */
	public void setKto_New(BigDecimal kto_New) {
		this.kto_New = kto_New;
	}



	/**
	 * @return the epr_Changed
	 */
	public Integer getEpr_Changed() {
		return epr_Changed;
	}



	/**
	 * @param epr_Changed the epr_Changed to set
	 */
	public void setEpr_Changed(Integer epr_Changed) {
		this.epr_Changed = epr_Changed;
	}



	/**
	 * @return the epr_Old
	 */
	public BigDecimal getEpr_Old() {
		return epr_Old;
	}



	/**
	 * @param epr_Old the epr_Old to set
	 */
	public void setEpr_Old(BigDecimal epr_Old) {
		this.epr_Old = epr_Old;
	}



	/**
	 * @return the epr_New
	 */
	public BigDecimal getEpr_New() {
		return epr_New;
	}



	/**
	 * @param epr_New the epr_New to set
	 */
	public void setEpr_New(BigDecimal epr_New) {
		this.epr_New = epr_New;
	}



	/**
	 * @return the npr_Changed
	 */
	public Integer getNpr_Changed() {
		return npr_Changed;
	}



	/**
	 * @param npr_Changed the npr_Changed to set
	 */
	public void setNpr_Changed(Integer npr_Changed) {
		this.npr_Changed = npr_Changed;
	}



	/**
	 * @return the npr_Old
	 */
	public String getNpr_Old() {
		return npr_Old;
	}



	/**
	 * @param npr_Old the npr_Old to set
	 */
	public void setNpr_Old(String npr_Old) {
		this.npr_Old = npr_Old;
	}



	/**
	 * @return the npr_New
	 */
	public String getNpr_New() {
		return npr_New;
	}



	/**
	 * @param npr_New the npr_New to set
	 */
	public void setNpr_New(String npr_New) {
		this.npr_New = npr_New;
	}



	/**
	 * @return the eknpr_Changed
	 */
	public Integer getEknpr_Changed() {
		return eknpr_Changed;
	}



	/**
	 * @param eknpr_Changed the eknpr_Changed to set
	 */
	public void setEknpr_Changed(Integer eknpr_Changed) {
		this.eknpr_Changed = eknpr_Changed;
	}



	/**
	 * @return the eknpr_Old
	 */
	public BigDecimal getEknpr_Old() {
		return eknpr_Old;
	}



	/**
	 * @param eknpr_Old the eknpr_Old to set
	 */
	public void setEknpr_Old(BigDecimal eknpr_Old) {
		this.eknpr_Old = eknpr_Old;
	}



	/**
	 * @return the eknpr_New
	 */
	public BigDecimal getEknpr_New() {
		return eknpr_New;
	}



	/**
	 * @param eknpr_New the eknpr_New to set
	 */
	public void setEknpr_New(BigDecimal eknpr_New) {
		this.eknpr_New = eknpr_New;
	}



	/**
	 * @return the dak_Changed
	 */
	public Integer getDak_Changed() {
		return dak_Changed;
	}



	/**
	 * @param dak_Changed the dak_Changed to set
	 */
	public void setDak_Changed(Integer dak_Changed) {
		this.dak_Changed = dak_Changed;
	}



	/**
	 * @return the dak_Old
	 */
	public String getDak_Old() {
		return dak_Old;
	}



	/**
	 * @param dak_Old the dak_Old to set
	 */
	public void setDak_Old(String dak_Old) {
		this.dak_Old = dak_Old;
	}



	/**
	 * @return the dak_New
	 */
	public String getDak_New() {
		return dak_New;
	}



	/**
	 * @param dak_New the dak_New to set
	 */
	public void setDak_New(String dak_New) {
		this.dak_New = dak_New;
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
	
	
}