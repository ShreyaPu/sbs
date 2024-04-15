package com.alphax.vo.mb;

import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about Rebooking bundles in pieces BA (Umbuchen Gebinde in Stück BA)")
public class RebookingBundlesBA_DTO {

	private String manufacturer;
	
	private String warehouseNumber;
	
	private String deliveryNoteNo;
	
	private String containerPartNo;
	
	private String containerBestand;
	
	private String containerDAK;
	
	private String numberOfContainers;
	
	private String contentsOfContainer;
	
	//Piece goods
	
	private String piecesPartNo;
	
	private String piecesBestand;
	
	private String piecesDAK;
	
	private String piecesNetPrice;
	
	private String piecesTransferAmount;
	
	// Price correction
	private String CreditValue;
	private String netshoppingPrice;
	
	@ApiModelProperty(value = "Hersteller OEM (Manufacturer) ")
	@Size(max=4)
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	
	@ApiModelProperty(value = "Lager (warehouseNumber) ")
	@Size(max=2)
	public String getWarehouseNumber() {
		return warehouseNumber;
	}
	public void setWarehouseNumber(String warehouseNumber) {
		this.warehouseNumber = warehouseNumber;
	}
	
	@ApiModelProperty(value = "Delivery Note Number (Lieferscheinnummer )")
	@Size(max=10)
	public String getDeliveryNoteNo() {
		return deliveryNoteNo;
	}
	public void setDeliveryNoteNo(String deliveryNoteNo) {
		this.deliveryNoteNo = deliveryNoteNo;
	}
	
	@ApiModelProperty(value = "Teilenummer (Part Number for Container) ")
	@Size(max=19)
	public String getContainerPartNo() {
		return containerPartNo;
	}
	public void setContainerPartNo(String containerPartNo) {
		this.containerPartNo = containerPartNo;
	}
	
	@ApiModelProperty(value = "Bestand for Container")
	@Size(max=9)
	public String getContainerBestand() {
		return containerBestand;
	}
	public void setContainerBestand(String containerBestand) {
		this.containerBestand = containerBestand;
	}
	
	@ApiModelProperty(value = "DAK for Container ")
	@Size(max=11)
	public String getContainerDAK() {
		return containerDAK;
	}
	public void setContainerDAK(String containerDAK) {
		this.containerDAK = containerDAK;
	}
	
	@ApiModelProperty(value = "Number Of Containers (Anzahl Gebinde) ")
	@Size(max=5)
	public String getNumberOfContainers() {
		return numberOfContainers;
	}
	public void setNumberOfContainers(String numberOfContainers) {
		this.numberOfContainers = numberOfContainers;
	}
	
	@ApiModelProperty(value = " ContentsOfContainer (Inhalt Gebinde) ")
	@Size(max=5)
	public String getContentsOfContainer() {
		return contentsOfContainer;
	}
	public void setContentsOfContainer(String contentsOfContainer) {
		this.contentsOfContainer = contentsOfContainer;
	}
	
	@ApiModelProperty(value = "Teilenummer (Part Number for Stückware ) ")
	@Size(max=19)
	public String getPiecesPartNo() {
		return piecesPartNo;
	}
	public void setPiecesPartNo(String piecesPartNo) {
		this.piecesPartNo = piecesPartNo;
	}
	
	@ApiModelProperty(value = "Bestand for Stückware")
	@Size(max=8)
	public String getPiecesBestand() {
		return piecesBestand;
	}
	public void setPiecesBestand(String piecesBestand) {
		this.piecesBestand = piecesBestand;
	}
	
	@ApiModelProperty(value = "DAK for Stückware ")
	@Size(max=11)
	public String getPiecesDAK() {
		return piecesDAK;
	}
	public void setPiecesDAK(String piecesDAK) {
		this.piecesDAK = piecesDAK;
	}
	
	@ApiModelProperty(value = " Shopping net price (Einkaufsnettopreis) ")
	@Size(max=10)
	public String getPiecesNetPrice() {
		return piecesNetPrice;
	}
	public void setPiecesNetPrice(String piecesNetPrice) {
		this.piecesNetPrice = piecesNetPrice;
	}
	
	@ApiModelProperty(value = " Transfer amount (Umbuchungsmenge) ")
	@Size(max=8)
	public String getPiecesTransferAmount() {
		return piecesTransferAmount;
	}
	public void setPiecesTransferAmount(String piecesTransferAmount) {
		this.piecesTransferAmount = piecesTransferAmount;
	}
	
	@ApiModelProperty(value = " Credit (Gutschrift) ")
	@Size(max=5)
	public String getCreditValue() {
		return CreditValue;
	}
	public void setCreditValue(String creditValue) {
		CreditValue = creditValue;
	}
	
	@ApiModelProperty(value = " Net shopping Price (Einkaufsnettopreis) ")
	@Size(max=10)
	public String getNetshoppingPrice() {
		return netshoppingPrice;
	}
	public void setNetshoppingPrice(String netshoppingPrice) {
		this.netshoppingPrice = netshoppingPrice;
	}
	
	
	
}
