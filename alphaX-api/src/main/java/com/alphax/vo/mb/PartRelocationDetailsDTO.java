package com.alphax.vo.mb;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about Part Relocation Details")
public class PartRelocationDetailsDTO {

	private String partNumber;
	
	private String partName;
	
	private String supplierNumber;
	
	private String supplierName;
	
	private String warehouseNumber;
	
	private String manufacturer;
	
	private String requiredQuantity;
	
	private String movementType;
	
	private String fromPrinter;
	
	private String asrv_AUFNR;
	
	private String psrv_AUFNR;
	
	private String orderPositionSerialNo;
	
	private String etManufacturer;
	
	private Boolean isAserv;
	
	private String publisher;
	
	private List<PartRelocationDTO> partRelocationList;
	
	
	public PartRelocationDetailsDTO() {
	}


	/**
	 * @return the partNumber
	 */
	@ApiModelProperty(value = "part Number (Teilenummer) ")
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
	 * @return the partName
	 */
	@ApiModelProperty(value = "part Name (Teilename) ")
	public String getPartName() {
		return partName;
	}


	/**
	 * @param partName the partName to set
	 */
	public void setPartName(String partName) {
		this.partName = partName;
	}


	/**
	 * @return the supplierNumber
	 */
	@ApiModelProperty(value = "supplier Number (Lieferant Number) ")
	public String getSupplierNumber() {
		return supplierNumber;
	}


	/**
	 * @param supplierNumber the supplierNumber to set
	 */
	public void setSupplierNumber(String supplierNumber) {
		this.supplierNumber = supplierNumber;
	}


	/**
	 * @return the supplierName
	 */
	@ApiModelProperty(value = "supplier Name (Lieferant Name) ")
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
	 * @return the warehouseNumber
	 */
	@ApiModelProperty(value = "Warehouse Number (Lager) ")
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
	@ApiModelProperty(value = "Manufacturer (Hersteller) ")
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
	 * @return the requiredQuantity
	 */
	@ApiModelProperty(value = "required Quantity (Benötigte Anzahl) ")
	public String getRequiredQuantity() {
		return requiredQuantity;
	}


	/**
	 * @param requiredQuantity the requiredQuantity to set
	 */
	public void setRequiredQuantity(String requiredQuantity) {
		this.requiredQuantity = requiredQuantity;
	}


	/**
	 * @return the movementType
	 */
	@ApiModelProperty(value = "Movement Type (Bewegungsart BA) ")
	public String getMovementType() {
		return movementType;
	}


	/**
	 * @param movementType the movementType to set
	 */
	public void setMovementType(String movementType) {
		this.movementType = movementType;
	}


	@ApiModelProperty(value = "Printer (Drucker) ")
	public String getFromPrinter() {
		return fromPrinter;
	}


	public void setFromPrinter(String fromPrinter) {
		this.fromPrinter = fromPrinter;
	}


	public String getAsrv_AUFNR() {
		return asrv_AUFNR;
	}


	public void setAsrv_AUFNR(String asrv_AUFNR) {
		this.asrv_AUFNR = asrv_AUFNR;
	}


	public String getPsrv_AUFNR() {
		return psrv_AUFNR;
	}


	public void setPsrv_AUFNR(String psrv_AUFNR) {
		this.psrv_AUFNR = psrv_AUFNR;
	}


	public String getOrderPositionSerialNo() {
		return orderPositionSerialNo;
	}


	public void setOrderPositionSerialNo(String orderPositionSerialNo) {
		this.orderPositionSerialNo = orderPositionSerialNo;
	}


	public String getEtManufacturer() {
		return etManufacturer;
	}


	public void setEtManufacturer(String etManufacturer) {
		this.etManufacturer = etManufacturer;
	}


	public Boolean getIsAserv() {
		return isAserv;
	}


	public void setIsAserv(Boolean isAserv) {
		this.isAserv = isAserv;
	}


	/**
	 * @return the partRelocationList
	 */
	@ApiModelProperty(value = "partRelocation List ")
	public List<PartRelocationDTO> getPartRelocationList() {
		return partRelocationList;
	}


	/**
	 * @param partRelocationList the partRelocationList to set
	 */
	public void setPartRelocationList(List<PartRelocationDTO> partRelocationList) {
		this.partRelocationList = partRelocationList;
	}


	/**
	 * @return the publisher
	 */
	public String getPublisher() {
		return publisher;
	}


	/**
	 * @param publisher the publisher to set
	 */
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	
	
	
}