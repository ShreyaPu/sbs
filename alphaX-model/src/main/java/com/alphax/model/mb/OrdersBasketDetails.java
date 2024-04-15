package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about Orders Basket Details")
public class OrdersBasketDetails {

	@DBTable(columnName ="BEST_BEND", required = true)
	private String supplierNo;
	
	@DBTable(columnName ="BEST_DESC", required = true)
	private String supplierName;
	
	@DBTable(columnName ="MARKE", required = true)
	private String part_Brand;
	
	@DBTable(columnName ="ET_ETNR", required = true)
	private String partNumber;
	
	@DBTable(columnName ="ET_BEN", required = true)
	private String partDescription;
	
	@DBTable(columnName ="ET_LNR", required = true)
	private String warehouseNumber;	
	
	@DBTable(columnName ="SERV_MENGE_B", required = true)
	private String totalPartsNumber;
	
	@DBTable(columnName ="CNT_TNR", required = true)
	private Integer count_Relocate;
	
	@DBTable(columnName ="PSRV_POSNR", required = true)
	private BigDecimal orderPosition_PSRV;
	
	@DBTable(columnName ="ASRV_POSNR", required = true)
	private BigDecimal orderPosition_ASRV;
	
	@DBTable(columnName ="ET_HERST", required = true)
	private String etManufacturer;
	
	@DBTable(columnName ="ROWNUMER", required = true)
	private Integer totalCount;
	
	@DBTable(columnName ="ET_ETUP", required = true)
	private BigDecimal et_ETUP;
	
	@DBTable(columnName ="ASERV_FA", required = true)
	private BigDecimal aserv_FA;
	
	@DBTable(columnName ="ASERV_FIL", required = true)
	private BigDecimal aserv_FIL;
	
	@DBTable(columnName ="ASERV_AUFNR", required = true)
	private BigDecimal aserv_AUFNR;
	
	@DBTable(columnName ="ASERV_SAART", required = true)
	private BigDecimal aserv_SAART;
	
	@DBTable(columnName ="ASERV_ERGNR", required = true)
	private BigDecimal aserv_ERGNR ;
	
	@DBTable(columnName ="KFIRMA", required = true)
	private BigDecimal kfirma;
	
	@DBTable(columnName ="KSERV_KDNUM", required = true)
	private String kserv_KDNUM;
	
	@DBTable(columnName ="KSERV_SA", required = true)
	private BigDecimal kserv_SA;
	
	@DBTable(columnName ="PSERV_FA", required = true)
	private BigDecimal pserv_FA;
	
	@DBTable(columnName ="PSERV_FIL", required = true)
	private BigDecimal pserv_FIL;
	
	@DBTable(columnName ="PSERV_AUFNR", required = true)
	private BigDecimal pserv_AUFNR;
	
	@DBTable(columnName ="PSERV_SAART", required = true)
	private BigDecimal pserv_SAART;
	
	@DBTable(columnName ="PSERV_ERGNR", required = true)
	private BigDecimal pserv_ERGNR;
	
	@DBTable(columnName ="BHERST", required = true)
	private String bManufacturer;
	
	@DBTable(columnName ="BESTKORB", required = true)
	private String bestkorb;
	
	@DBTable(columnName ="BDAT", required = true)
	private String orderDate;	
	
	@DBTable(columnName ="BZEIT", required = true)
	private String orderTime;
	
	
	

	public OrdersBasketDetails() {

	}


	/**
	 * @return the supplierNo
	 */
	public String getSupplierNo() {
		return supplierNo;
	}


	/**
	 * @param supplierNo the supplierNo to set
	 */
	public void setSupplierNo(String supplierNo) {
		this.supplierNo = supplierNo;
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
	 * @return the part_Brand
	 */
	public String getPart_Brand() {
		return part_Brand;
	}


	/**
	 * @param part_Brand the part_Brand to set
	 */
	public void setPart_Brand(String part_Brand) {
		this.part_Brand = part_Brand;
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
	 * @return the partDescription
	 */
	public String getPartDescription() {
		return partDescription;
	}


	/**
	 * @param partDescription the partDescription to set
	 */
	public void setPartDescription(String partDescription) {
		this.partDescription = partDescription;
	}


	/**
	 * @return the warehouseNumber
	 */
	public String getWarehouseNumber() {
		return warehouseNumber;
	}


	/**
	 * @param warehouseNumber the warehouseNumber to set
	 */
	public void setWarehouseNumber(String warehouseNumber) {
		this.warehouseNumber = warehouseNumber;
	}


	/**
	 * @return the totalPartsNumber
	 */
	public String getTotalPartsNumber() {
		return totalPartsNumber;
	}


	/**
	 * @param totalPartsNumber the totalPartsNumber to set
	 */
	public void setTotalPartsNumber(String totalPartsNumber) {
		this.totalPartsNumber = totalPartsNumber;
	}


	/**
	 * @return the count_Relocate
	 */
	public Integer getCount_Relocate() {
		return count_Relocate;
	}


	/**
	 * @param count_Relocate the count_Relocate to set
	 */
	public void setCount_Relocate(Integer count_Relocate) {
		this.count_Relocate = count_Relocate;
	}


	/**
	 * @return the orderPosition_PSRV
	 */
	public BigDecimal getOrderPosition_PSRV() {
		return orderPosition_PSRV;
	}


	/**
	 * @param orderPosition_PSRV the orderPosition_PSRV to set
	 */
	public void setOrderPosition_PSRV(BigDecimal orderPosition_PSRV) {
		this.orderPosition_PSRV = orderPosition_PSRV;
	}


	/**
	 * @return the orderPosition_ASRV
	 */
	public BigDecimal getOrderPosition_ASRV() {
		return orderPosition_ASRV;
	}


	/**
	 * @param orderPosition_ASRV the orderPosition_ASRV to set
	 */
	public void setOrderPosition_ASRV(BigDecimal orderPosition_ASRV) {
		this.orderPosition_ASRV = orderPosition_ASRV;
	}


	/**
	 * @return the etManufacturer
	 */
	public String getEtManufacturer() {
		return etManufacturer;
	}


	/**
	 * @param etManufacturer the etManufacturer to set
	 */
	public void setEtManufacturer(String etManufacturer) {
		this.etManufacturer = etManufacturer;
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


	public BigDecimal getEt_ETUP() {
		return et_ETUP;
	}


	public void setEt_ETUP(BigDecimal et_ETUP) {
		this.et_ETUP = et_ETUP;
	}


	public BigDecimal getAserv_FA() {
		return aserv_FA;
	}


	public void setAserv_FA(BigDecimal aserv_FA) {
		this.aserv_FA = aserv_FA;
	}


	public BigDecimal getAserv_FIL() {
		return aserv_FIL;
	}


	public void setAserv_FIL(BigDecimal aserv_FIL) {
		this.aserv_FIL = aserv_FIL;
	}


	public BigDecimal getAserv_AUFNR() {
		return aserv_AUFNR;
	}


	public void setAserv_AUFNR(BigDecimal aserv_AUFNR) {
		this.aserv_AUFNR = aserv_AUFNR;
	}


	public BigDecimal getAserv_ERGNR() {
		return aserv_ERGNR;
	}


	public void setAserv_ERGNR(BigDecimal aserv_ERGNR) {
		this.aserv_ERGNR = aserv_ERGNR;
	}


	public BigDecimal getKfirma() {
		return kfirma;
	}


	public void setKfirma(BigDecimal kfirma) {
		this.kfirma = kfirma;
	}


	public String getKserv_KDNUM() {
		return kserv_KDNUM;
	}


	public void setKserv_KDNUM(String kserv_KDNUM) {
		this.kserv_KDNUM = kserv_KDNUM;
	}


	public BigDecimal getKserv_SA() {
		return kserv_SA;
	}


	public void setKserv_SA(BigDecimal kserv_SA) {
		this.kserv_SA = kserv_SA;
	}


	public BigDecimal getPserv_FA() {
		return pserv_FA;
	}


	public void setPserv_FA(BigDecimal pserv_FA) {
		this.pserv_FA = pserv_FA;
	}


	public BigDecimal getPserv_FIL() {
		return pserv_FIL;
	}


	public void setPserv_FIL(BigDecimal pserv_FIL) {
		this.pserv_FIL = pserv_FIL;
	}


	public BigDecimal getPserv_AUFNR() {
		return pserv_AUFNR;
	}


	public void setPserv_AUFNR(BigDecimal pserv_AUFNR) {
		this.pserv_AUFNR = pserv_AUFNR;
	}


	public BigDecimal getPserv_SAART() {
		return pserv_SAART;
	}


	public void setPserv_SAART(BigDecimal pserv_SAART) {
		this.pserv_SAART = pserv_SAART;
	}


	public BigDecimal getPserv_ERGNR() {
		return pserv_ERGNR;
	}


	public void setPserv_ERGNR(BigDecimal pserv_ERGNR) {
		this.pserv_ERGNR = pserv_ERGNR;
	}


	public BigDecimal getAserv_SAART() {
		return aserv_SAART;
	}


	public void setAserv_SAART(BigDecimal aserv_SAART) {
		this.aserv_SAART = aserv_SAART;
	}


	public String getbManufacturer() {
		return bManufacturer;
	}


	public void setbManufacturer(String bManufacturer) {
		this.bManufacturer = bManufacturer;
	}


	public String getBestkorb() {
		return bestkorb;
	}


	public void setBestkorb(String bestkorb) {
		this.bestkorb = bestkorb;
	}


	public String getOrderDate() {
		return orderDate;
	}


	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}


	public String getOrderTime() {
		return orderTime;
	}


	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}


	
	


}