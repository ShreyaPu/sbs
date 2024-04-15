package com.alphax.vo.mb;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about Orders Basket Details DTO")
public class OrdersBasketDetailsDTO {
	
	private Boolean isAserv;
	
	private String warehouseNumber;
	
	private String manufacturer;
	
	private String orderNumber;
	
	private String orderPosition;
	
	private String orderDate;	
	
	private String orderTime;
	
	private String supplierNo;
	
	private String supplierName;
	
	private String part_Brand;
	
	private String partNumber;
	
	private String partDescription;
	
	private String totalPartsNumber;
	
	private Integer count_Relocate;
	
	private String orderPositionSerialNo;
	
	private String partOEM;
	
	private String etManufacturer;
	
	private String customerName;
	
	private String newOrderNo;
	
	private String orderNote;
	
	private String orderStatus;
	
	private String deliveryNoteNo;
	
	private String totalPartsDelivered;
	
	private String partPosition;
	
	private String partPositionUP;
	
	private List<OrdersBasketDetailsDTO> orderBasketDetList;
	
	private String et_ETUP;
	
	private String aserv_FA;
	
	private String aserv_FIL;
	
	private String asrv_AUFNR;
	
	private String aserv_SAART;
	
	private String asrv_ERGNR ;
	
	private String kfirma;
	
	private String ksrv_KDNUM;
	
	private String kserv_SA;
	
	private String pserv_FA;
	
	private String pserv_FIL;
	
	private String psrv_AUFNR;
	
	private String psrv_SAART;
	
	private String psrv_ERGNR;
	
	private String bManufacturer;
	
	private String orderPosition_ASRV;
	
	private String orderPosition_PSRV;
	
	private String remainingAmount;
	
	private String partStatus;
	
	private String partStatusType;
	
	private String deliveredQuantity;
	
	private String warehouseList;
	
	private String customerNameWithAsterisk ;
	

	public OrdersBasketDetailsDTO() {

	}

	/**
	 * @return the isAserv
	 */
	@ApiModelProperty(value = "Boolean value to display ASERV or PSERV.")
	public Boolean getIsAserv() {
		return isAserv;
	}

	/**
	 * @param isAserv the isAserv to set
	 */
	public void setIsAserv(Boolean isAserv) {
		this.isAserv = isAserv;
	}
	
	
	/**
	 * @return the warehouseNumber
	 */
	@ApiModelProperty(value = "Warehouse Number (WarehouseNummer) ( %% - DB E_ESKDAT - ET_LNR).")
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
	 * @return the manufacturer
	 */
	@ApiModelProperty(value = "Manufacturer (Hersteller) ( %% - DB E_ESKDAT - BHERST).")
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
	 * @return the orderNumber
	 */
	@ApiModelProperty(value = "Order Number (Auftrag) ( %% - DB E_ESKDAT - PSERV_AUFNR / ASERV_AUFNR).")
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
	 * @return the orderPosition
	 */
	@ApiModelProperty(value = "Order Positionen (Positionen) ( %% - DB E_ESKDAT - count of PSERV_AUFNR / ASERV_AUFNR).")
	public String getOrderPosition() {
		return orderPosition;
	}

	/**
	 * @param orderPosition the orderPosition to set
	 */
	public void setOrderPosition(String orderPosition) {
		this.orderPosition = orderPosition;
	}


	/**
	 * @return the orderDate
	 */
	@ApiModelProperty(value = "Order Date (Datum) ( %% - DB E_ESKDAT - BDAT).")
	public String getOrderDate() {
		return orderDate;
	}

	/**
	 * @param orderDate the orderDate to set
	 */
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}


	/**
	 * @return the orderTime
	 */
	@ApiModelProperty(value = "Order Time (Time) ( %% - DB E_ESKDAT - BZEIT).")
	public String getOrderTime() {
		return orderTime;
	}

	/**
	 * @param orderTime the orderTime to set
	 */
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	
	
	/**
	 * @return the supplierNo
	 */
	@ApiModelProperty(value = "supplier Number (Lieferantennummer) ( %% - DB E_ESKDAT - BEST_BEND).")
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
	@ApiModelProperty(value = "supplier Name (Lieferant) ( %% - DB E_ETSTAMK6 - NAME1).")
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
	@ApiModelProperty(value = "Part Brand (Teilemarke) ( %% - DB E_ESKDAT - MARKE).")
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
	@ApiModelProperty(value = "Part Number (Teilenummer) ( %% - DB E_ESKDAT - ET_ETNR).")
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
	@ApiModelProperty(value = "Part Description (Teilebezeichnung) ( %% - DB E_ESKDAT - ET_BEN).")
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
	 * @return the totalPartsNumber
	 */
	@ApiModelProperty(value = "Total Parts Number (Anzahl) ( %% - DB E_ESKDAT - SERV_MENGE_B).")
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
	@ApiModelProperty(value = "Relocate Count (Verlagern) ( %% - DB E_ETSTAMM - Count).")
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
	 * @return the orderPositionSerialNo
	 */
	@ApiModelProperty(value = "Order Position Serial No (ASRV_POSNR / PSRV_POSNR) ( %% - DB E_ESKDAT - ASRV_POSNR / PSRV_POSNR).")
	public String getOrderPositionSerialNo() {
		return orderPositionSerialNo;
	}


	/**
	 * @param orderPositionSerialNo the orderPositionSerialNo to set
	 */
	public void setOrderPositionSerialNo(String orderPositionSerialNo) {
		this.orderPositionSerialNo = orderPositionSerialNo;
	}

	/**
	 * @return the partOEM
	 */
	@ApiModelProperty(value = "Manufacturer (OEM) ( %% - DB E_ESKDAT - BHERST).")
	public String getPartOEM() {
		return partOEM;
	}

	/**
	 * @param partOEM the partOEM to set
	 */
	public void setPartOEM(String partOEM) {
		this.partOEM = partOEM;
	}

	/**
	 * @return the etManufacturer
	 */
	@ApiModelProperty(value = "ET_Manufacturer ( %% - DB E_ESKDAT - ET_HERST).")
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
	 * @return the customerName
	 */
	@ApiModelProperty(value = "Customer Name (Kunde) ( %% - DB F_AUFKO2 / F_PATKO2 - KUNDEN).")
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
	 * @return the newOrderNo
	 */
	@ApiModelProperty(value = "New Order Number (Bestellnummer) ( %% - DB E_BSNDAT - BEST_ART|| BEST_AP).")
	public String getNewOrderNo() {
		return newOrderNo;
	}

	/**
	 * @param newOrderNo the newOrderNo to set
	 */
	public void setNewOrderNo(String newOrderNo) {
		this.newOrderNo = newOrderNo;
	}

	/**
	 * @return the orderNote
	 */
	@ApiModelProperty(value = "Order Note (Bestellhinweis) ( %% - DB E_BSNDAT - BVER ).")
	public String getOrderNote() {
		return orderNote;
	}

	/**
	 * @param orderNote the orderNote to set
	 */
	public void setOrderNote(String orderNote) {
		this.orderNote = orderNote;
	}

	/**
	 * @return the orderStatus
	 */
	public String getOrderStatus() {
		return orderStatus;
	}

	/**
	 * @param orderStatus the orderStatus to set
	 */
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	/**
	 * @return the deliveryNoteNo
	 */
	@ApiModelProperty(value = "DelieryNote Number (Lieferscheinnummer)")
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
	 * @return the totalPartsDelivered
	 */
	@ApiModelProperty(value = "Total parts delivered (Anzahl gelifert)")
	public String getTotalPartsDelivered() {
		return totalPartsDelivered;
	}

	/**
	 * @param totalPartsDelivered the totalPartsDelivered to set
	 */
	public void setTotalPartsDelivered(String totalPartsDelivered) {
		this.totalPartsDelivered = totalPartsDelivered;
	}

	/**
	 * @return the partPosition
	 */
	public String getPartPosition() {
		return partPosition;
	}

	/**
	 * @param partPosition the partPosition to set
	 */
	public void setPartPosition(String partPosition) {
		this.partPosition = partPosition;
	}

	/**
	 * @return the orderBasketDetList
	 */
	public List<OrdersBasketDetailsDTO> getOrderBasketDetList() {
		return orderBasketDetList;
	}

	/**
	 * @param orderBasketDetList the orderBasketDetList to set
	 */
	public void setOrderBasketDetList(List<OrdersBasketDetailsDTO> orderBasketDetList) {
		this.orderBasketDetList = orderBasketDetList;
	}

	@ApiModelProperty(value = "For part relocation",hidden = true)
	public String getEt_ETUP() {
		return et_ETUP;
	}

	public void setEt_ETUP(String et_ETUP) {
		this.et_ETUP = et_ETUP;
	}
	
	@ApiModelProperty(value = "For part relocation",hidden = true)
	public String getAserv_FA() {
		return aserv_FA;
	}

	public void setAserv_FA(String aserv_FA) {
		this.aserv_FA = aserv_FA;
	}
	
	@ApiModelProperty(value = "For part relocation",hidden = true)
	public String getAserv_FIL() {
		return aserv_FIL;
	}

	public void setAserv_FIL(String aserv_FIL) {
		this.aserv_FIL = aserv_FIL;
	}

	@ApiModelProperty(value = "For part relocation",hidden = true)
	public String getAsrv_AUFNR() {
		return asrv_AUFNR;
	}

	public void setAsrv_AUFNR(String asrv_AUFNR) {
		this.asrv_AUFNR = asrv_AUFNR;
	}

	@ApiModelProperty(value = "For part relocation",hidden = true)
	public String getAsrv_ERGNR() {
		return asrv_ERGNR;
	}

	public void setAsrv_ERGNR(String asrv_ERGNR) {
		this.asrv_ERGNR = asrv_ERGNR;
	}
	
	@ApiModelProperty(value = "For part relocation",hidden = true)
	public String getKfirma() {
		return kfirma;
	}

	public void setKfirma(String kfirma) {
		this.kfirma = kfirma;
	}

	@ApiModelProperty(value = "For part relocation",hidden = true)
	public String getKsrv_KDNUM() {
		return ksrv_KDNUM;
	}

	public void setKsrv_KDNUM(String ksrv_KDNUM) {
		this.ksrv_KDNUM = ksrv_KDNUM;
	}

	@ApiModelProperty(value = "For part relocation",hidden = true)
	public String getKserv_SA() {
		return kserv_SA;
	}

	public void setKserv_SA(String kserv_SA) {
		this.kserv_SA = kserv_SA;
	}

	@ApiModelProperty(value = "For part relocation",hidden = true)
	public String getPserv_FA() {
		return pserv_FA;
	}

	public void setPserv_FA(String pserv_FA) {
		this.pserv_FA = pserv_FA;
	}

	@ApiModelProperty(value = "For part relocation",hidden = true)
	public String getPserv_FIL() {
		return pserv_FIL;
	}

	public void setPserv_FIL(String pserv_FIL) {
		this.pserv_FIL = pserv_FIL;
	}

	@ApiModelProperty(value = "For part relocation",hidden = true)
	public String getPsrv_AUFNR() {
		return psrv_AUFNR;
	}

	public void setPsrv_AUFNR(String psrv_AUFNR) {
		this.psrv_AUFNR = psrv_AUFNR;
	}

	@ApiModelProperty(value = "For part relocation",hidden = true)
	public String getPsrv_SAART() {
		return psrv_SAART;
	}

	public void setPsrv_SAART(String psrv_SAART) {
		this.psrv_SAART = psrv_SAART;
	}

	@ApiModelProperty(value = "For part relocation",hidden = true)
	public String getPsrv_ERGNR() {
		return psrv_ERGNR;
	}

	public void setPsrv_ERGNR(String psrv_ERGNR) {
		this.psrv_ERGNR = psrv_ERGNR;
	}

	@ApiModelProperty(value = "For part relocation",hidden = true)
	public String getAserv_SAART() {
		return aserv_SAART;
	}

	public void setAserv_SAART(String aserv_SAART) {
		this.aserv_SAART = aserv_SAART;
	}

	@ApiModelProperty(value = "For part relocation",hidden = true)
	public String getbManufacturer() {
		return bManufacturer;
	}

	public void setbManufacturer(String bManufacturer) {
		this.bManufacturer = bManufacturer;
	}

	@ApiModelProperty(value = "For part relocation",hidden = true)
	public String getOrderPosition_ASRV() {
		return orderPosition_ASRV;
	}

	public void setOrderPosition_ASRV(String orderPosition_ASRV) {
		this.orderPosition_ASRV = orderPosition_ASRV;
	}

	@ApiModelProperty(value = "For part relocation",hidden = true)
	public String getOrderPosition_PSRV() {
		return orderPosition_PSRV;
	}

	public void setOrderPosition_PSRV(String orderPosition_PSRV) {
		this.orderPosition_PSRV = orderPosition_PSRV;
	}

	@ApiModelProperty(value = "(remainingAmount) ( %% - DB E_BSNDAT - OMENG).")
	public String getRemainingAmount() {
		return remainingAmount;
	}

	public void setRemainingAmount(String remainingAmount) {
		this.remainingAmount = remainingAmount;
	}

	/**
	 * @return the partStatus
	 */
	public String getPartStatus() {
		return partStatus;
	}

	/**
	 * @param partStatus the partStatus to set
	 */
	public void setPartStatus(String partStatus) {
		this.partStatus = partStatus;
	}

	/**
	 * @return the partStatusType
	 */
	public String getPartStatusType() {
		return partStatusType;
	}

	/**
	 * @param partStatusType the partStatusType to set
	 */
	public void setPartStatusType(String partStatusType) {
		this.partStatusType = partStatusType;
	}

	/**
	 * @return the deliveredQuantity
	 */
	@ApiModelProperty(value = "(Delivered Quantity) ( %% - DB E_BSNDAT - ZUMENG).")
	public String getDeliveredQuantity() {
		return deliveredQuantity;
	}

	/**
	 * @param deliveredQuantity the deliveredQuantity to set
	 */
	public void setDeliveredQuantity(String deliveredQuantity) {
		this.deliveredQuantity = deliveredQuantity;
	}

	/**
	 * @return the warehouseList
	 */
	public String getWarehouseList() {
		return warehouseList;
	}

	/**
	 * @param warehouseList the warehouseList to set
	 */
	public void setWarehouseList(String warehouseList) {
		this.warehouseList = warehouseList;
	}
	
	/**
	 * @return the partPositionUP
	 */
	public String getPartPositionUP() {
		return partPositionUP;
	}

	/**
	 * @param partPositionUP the partPositionUP to set
	 */
	public void setPartPositionUP(String partPositionUP) {
		this.partPositionUP = partPositionUP;
	}

	/**
	 * @return the customerNameWithAsterisk
	 */
	public String getCustomerNameWithAsterisk() {
		return customerNameWithAsterisk;
	}

	/**
	 * @param customerNameWithAsterisk the customerNameWithAsterisk to set
	 */
	public void setCustomerNameWithAsterisk(String customerNameWithAsterisk) {
		this.customerNameWithAsterisk = customerNameWithAsterisk;
	}

	
}