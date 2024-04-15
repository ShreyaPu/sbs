package com.alphax.vo.mb;

import io.swagger.annotations.ApiModelProperty;

public class MovementDataDTO {

	private String manufacturer;
	
	private String warehouse;
	
	private String businesscase;
	
	private String documentNumber;
	
	private String supplier;
	
	private String supplierName;
	
	private String date;
	
	private String time;
	
	private String orderNumber;
	
	private String bhw;
	
	private String quantity;
	
	private String customerName;
	
	private String customerNumber;
	
	private String ma_Changed;
	
	private String ma_Old;
	
	private String ma_New;
	
	private String rg_Changed;
	
	private String rg_Old;
	
	private String rg_New;
	
	private String ta_Changed;
	
	private String ta_Old;
	
	private String ta_New;
	
	private String la_Changed;
	
	private String la_Old;
	
	private String la_New;
	
	private String mc_Changed;
	
	private String mc_Old;
	
	private String mc_New;
	
	private String kto_Changed;
	
	private String kto_Old;
	
	private String kto_New;
	
	private String epr_Changed;
	
	private String epr_Old;
	
	private String epr_New;
	
	private String npr_Changed;
	
	private String npr_Old;
	
	private String npr_New;
	
	private String eknpr_Changed;
		
	private String eknpr_Old;
		
	private String eknpr_New;
	
	private String dak_Changed;
		
	private String dak_Old;
		
	private String dak_New;
	

	public MovementDataDTO() {
	
	}


	/**
	 * @return the manufacturer
	 */
	@ApiModelProperty(value = "HERST ( %% - DB E_CPSDAT - HERST).")
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
	@ApiModelProperty(value = "LNR ( %% - DB E_CPSDAT - VFNR).")
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
	 * @return the businesscase
	 */
	@ApiModelProperty(value = "BA ( %% - DB E_CPSDAT - BART).")
	public String getBusinesscase() {
		return businesscase;
	}



	/**
	 * @param businesscase the businesscase to set
	 */
	public void setBusinesscase(String businesscase) {
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
	@ApiModelProperty(value = "LIEFERANT ( %% - DB E_CPSDAT - BSN_LC).")
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
	 * @return the supplierName
	 */
	@ApiModelProperty(value = "LIEFERANT Name ( %% - DB E_ETSTAMK6 - NAME1).")
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
	 * @return the date
	 */
	@ApiModelProperty(value = "DATUM ( %% - DB E_CPSDAT - JJMMTT).")
	public String getDate() {
		return date;
	}


	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}


	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}


	/**
	 * @param time the time to set
	 */
	public void setTime(String time) {
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
	@ApiModelProperty(value = "BHW ( %% - DB E_CPSDAT - Substr(T3,7,12)).")
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
	@ApiModelProperty(value = "MENGE ( %% - DB E_CPSDAT - substr(T1,3,6)/10).")
	public String getQuantity() {
		return quantity;
	}



	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}



	/**
	 * @return the customerName
	 */
	@ApiModelProperty(value = "KUNDE ( %% - DB M1_KDDATK1 - NAME).")
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
	 * @return the ma_Changed
	 */
	public String getMa_Changed() {
		return ma_Changed;
	}


	/**
	 * @param ma_Changed the ma_Changed to set
	 */
	public void setMa_Changed(String ma_Changed) {
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
	public String getRg_Changed() {
		return rg_Changed;
	}


	/**
	 * @param rg_Changed the rg_Changed to set
	 */
	public void setRg_Changed(String rg_Changed) {
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
	public String getTa_Changed() {
		return ta_Changed;
	}


	/**
	 * @param ta_Changed the ta_Changed to set
	 */
	public void setTa_Changed(String ta_Changed) {
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
	public String getLa_Changed() {
		return la_Changed;
	}


	/**
	 * @param la_Changed the la_Changed to set
	 */
	public void setLa_Changed(String la_Changed) {
		this.la_Changed = la_Changed;
	}


	/**
	 * @return the la_Old
	 */
	public String getLa_Old() {
		return la_Old;
	}


	/**
	 * @param la_Old the la_Old to set
	 */
	public void setLa_Old(String la_Old) {
		this.la_Old = la_Old;
	}


	/**
	 * @return the la_New
	 */
	public String getLa_New() {
		return la_New;
	}


	/**
	 * @param la_New the la_New to set
	 */
	public void setLa_New(String la_New) {
		this.la_New = la_New;
	}


	/**
	 * @return the mc_Changed
	 */
	public String getMc_Changed() {
		return mc_Changed;
	}


	/**
	 * @param mc_Changed the mc_Changed to set
	 */
	public void setMc_Changed(String mc_Changed) {
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
	public String getKto_Changed() {
		return kto_Changed;
	}


	/**
	 * @param kto_Changed the kto_Changed to set
	 */
	public void setKto_Changed(String kto_Changed) {
		this.kto_Changed = kto_Changed;
	}


	/**
	 * @return the kto_Old
	 */
	public String getKto_Old() {
		return kto_Old;
	}


	/**
	 * @param kto_Old the kto_Old to set
	 */
	public void setKto_Old(String kto_Old) {
		this.kto_Old = kto_Old;
	}


	/**
	 * @return the kto_New
	 */
	public String getKto_New() {
		return kto_New;
	}


	/**
	 * @param kto_New the kto_New to set
	 */
	public void setKto_New(String kto_New) {
		this.kto_New = kto_New;
	}


	/**
	 * @return the epr_Changed
	 */
	public String getEpr_Changed() {
		return epr_Changed;
	}


	/**
	 * @param epr_Changed the epr_Changed to set
	 */
	public void setEpr_Changed(String epr_Changed) {
		this.epr_Changed = epr_Changed;
	}


	/**
	 * @return the epr_Old
	 */
	public String getEpr_Old() {
		return epr_Old;
	}


	/**
	 * @param epr_Old the epr_Old to set
	 */
	public void setEpr_Old(String epr_Old) {
		this.epr_Old = epr_Old;
	}


	/**
	 * @return the epr_New
	 */
	public String getEpr_New() {
		return epr_New;
	}


	/**
	 * @param epr_New the epr_New to set
	 */
	public void setEpr_New(String epr_New) {
		this.epr_New = epr_New;
	}


	/**
	 * @return the npr_Changed
	 */
	public String getNpr_Changed() {
		return npr_Changed;
	}


	/**
	 * @param npr_Changed the npr_Changed to set
	 */
	public void setNpr_Changed(String npr_Changed) {
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
	public String getEknpr_Changed() {
		return eknpr_Changed;
	}


	/**
	 * @param eknpr_Changed the eknpr_Changed to set
	 */
	public void setEknpr_Changed(String eknpr_Changed) {
		this.eknpr_Changed = eknpr_Changed;
	}


	/**
	 * @return the eknpr_Old
	 */
	public String getEknpr_Old() {
		return eknpr_Old;
	}


	/**
	 * @param eknpr_Old the eknpr_Old to set
	 */
	public void setEknpr_Old(String eknpr_Old) {
		this.eknpr_Old = eknpr_Old;
	}


	/**
	 * @return the eknpr_New
	 */
	public String getEknpr_New() {
		return eknpr_New;
	}


	/**
	 * @param eknpr_New the eknpr_New to set
	 */
	public void setEknpr_New(String eknpr_New) {
		this.eknpr_New = eknpr_New;
	}


	/**
	 * @return the dak_Changed
	 */
	public String getDak_Changed() {
		return dak_Changed;
	}


	/**
	 * @param dak_Changed the dak_Changed to set
	 */
	public void setDak_Changed(String dak_Changed) {
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
	
}