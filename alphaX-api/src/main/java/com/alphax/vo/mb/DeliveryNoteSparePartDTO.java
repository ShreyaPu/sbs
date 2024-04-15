package com.alphax.vo.mb;

import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;

public class DeliveryNoteSparePartDTO {
	
	@ApiModelProperty(value = "Error Code",hidden=true)
	private String returnCode = "";
	
	@ApiModelProperty(value = "Error Mesage",hidden=true)
	private String returnMsg = "";
	
	@ApiModelProperty(value = "Funktion fix value",hidden=true)
	private String funktion;
	
	
	@ApiModelProperty(value = "Hersteller (OEM)  - (DB E_LFSALL - 1-HERST).")
	@Size(max =4)
	private String manufacturer;

	@ApiModelProperty(value ="Lagernummer  - (DB E_LFSALL - 3-LNR)." )
	@Size(max=2)
	private String warehouseNumber;
	
	@ApiModelProperty(value ="Supplier   - (DB E_LFSALL - 4-LIEFERANT)." )
	@Size(max=5)
	private String supplierNumber ;
	
	@ApiModelProperty(value ="Delivery note number  - (DB E_LFSALL - 6-LFSNR)." )
	@Size(max=10)
	private String deliveryNoteNumber;
	
	@ApiModelProperty(value ="Order Number  - (DB E_LFSALL - 7-AUFNR_ALT)." )
	@Size(max=5)
	private String orderNumber;
	
	@ApiModelProperty(value ="Satzart  - (DB E_LFSALL - 5-SA)." )
	@Size(max=3)
	private String storageIndikator;
	
	@ApiModelProperty(value ="Position  - (DB E_LFSALL - 8 -POS_BEST)." )
	@Size(max=5)
	private String orderPosition;	

	@ApiModelProperty(value ="NEW TNR  - (DB E_LFSALL - 13 -TNR_GEL)." )
	@Size(max=19)
	private String newTnr;

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	public String getFunktion() {
		return funktion;
	}

	public void setFunktion(String funktion) {
		this.funktion = funktion;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getWarehouseNumber() {
		return warehouseNumber;
	}

	public void setWarehouseNumber(String warehouseNumber) {
		this.warehouseNumber = warehouseNumber;
	}

	public String getSupplierNumber() {
		return supplierNumber;
	}

	public void setSupplierNumber(String supplierNumber) {
		this.supplierNumber = supplierNumber;
	}

	public String getDeliveryNoteNumber() {
		return deliveryNoteNumber;
	}

	public void setDeliveryNoteNumber(String deliveryNoteNumber) {
		this.deliveryNoteNumber = deliveryNoteNumber;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getStorageIndikator() {
		return storageIndikator;
	}

	public void setStorageIndikator(String storageIndikator) {
		this.storageIndikator = storageIndikator;
	}

	public String getOrderPosition() {
		return orderPosition;
	}

	public void setOrderPosition(String orderPosition) {
		this.orderPosition = orderPosition;
	}

	public String getNewTnr() {
		return newTnr;
	}

	public void setNewTnr(String newTnr) {
		this.newTnr = newTnr;
	}

	
	
}
