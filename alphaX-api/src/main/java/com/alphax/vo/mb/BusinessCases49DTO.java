package com.alphax.vo.mb;

import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about BA 49")
public class BusinessCases49DTO {

	@ApiModelProperty(value = "Hersteller (OEM)  - (DB E_ETSTAMM- 1-HERST).")
	@Size(max =4)
	private String manufacturer;

	@ApiModelProperty(value ="Teilenummer - (DB E_ETSTAMM- 2-TNR)." )
	@Size(max =30)
	private String partNumber;

	@ApiModelProperty(value ="Lagernummer  - (DB E_ETSTAMM- 3-LNR)." )
	@Size(max=2)
	private String warehouseNo;
	
	@ApiModelProperty(value = "BA Business Cases ")
	@Size(max=2)
	private String businessCases;

	@ApiModelProperty(value ="Satzart  - (DB E_ETSTAMM- 4-SA)." )
	@Size(max=1)
	private String storageIndikator;
	
	@ApiModelProperty(value ="Durchschnittlicher Nettopreis DAK - (DB E_ETSTAMM- 11-DAK)." )
	private String averageNetPrice;
	
	@ApiModelProperty(value ="Bestellausstand bzw. Anzahl offener Bestellungen (2 Dezimalstellen) - (DB E_ETSTAMM- 120-BESAUS)." )
	private String pendingOrders;
	
	@ApiModelProperty(value ="Aktueller Bestand - (DB E_ETSTAMM- 29-AKTBES)." )
	private String currentStock;
	
	@ApiModelProperty(value ="Bezeichnung  - (DB E_ETSTAMM- 7-BENEN)." )
	@Size(max=50)
	private String partDescription;
	
	@ApiModelProperty(value ="Anzahl Zubuchungen - (DB E_ETSTAMM- 126-ZUMGE)." )
	private String bookingAmount;
	

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
	 * @return the warehouseNo
	 */
	public String getWarehouseNo() {
		return warehouseNo;
	}

	/**
	 * @param warehouseNo the warehouseNo to set
	 */
	public void setWarehouseNo(String warehouseNo) {
		this.warehouseNo = warehouseNo;
	}

	/**
	 * @return the storageIndikator
	 */
	public String getStorageIndikator() {
		return storageIndikator;
	}

	/**
	 * @param storageIndikator the storageIndikator to set
	 */
	public void setStorageIndikator(String storageIndikator) {
		this.storageIndikator = storageIndikator;
	}

	/**
	 * @return the averageNetPrice
	 */
	public String getAverageNetPrice() {
		return averageNetPrice;
	}

	/**
	 * @param averageNetPrice the averageNetPrice to set
	 */
	public void setAverageNetPrice(String averageNetPrice) {
		this.averageNetPrice = averageNetPrice;
	}

	/**
	 * @return the pendingOrders
	 */
	public String getPendingOrders() {
		return pendingOrders;
	}

	/**
	 * @param pendingOrders the pendingOrders to set
	 */
	public void setPendingOrders(String pendingOrders) {
		this.pendingOrders = pendingOrders;
	}

	/**
	 * @return the currentStock
	 */
	public String getCurrentStock() {
		return currentStock;
	}

	/**
	 * @param currentStock the currentStock to set
	 */
	public void setCurrentStock(String currentStock) {
		this.currentStock = currentStock;
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
	 * @return the bookingAmount
	 */
	public String getBookingAmount() {
		return bookingAmount;
	}

	/**
	 * @param bookingAmount the bookingAmount to set
	 */
	public void setBookingAmount(String bookingAmount) {
		this.bookingAmount = bookingAmount;
	}

	/**
	 * @return the businessCases
	 */
	public String getBusinessCases() {
		return businessCases;
	}

	/**
	 * @param businessCases the businessCases to set
	 */
	public void setBusinessCases(String businessCases) {
		this.businessCases = businessCases;
	}
	
	
	
	
}
