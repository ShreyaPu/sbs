package com.alphax.vo.mb;


import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Inventory Part [O_INVPART] table DTO ")
public class CreatedCountingDetailListDTO {

	@ApiModelProperty(value = "Manufacturer - OEM - O_INVPART")
	private String manufacturer;
	
	@ApiModelProperty(value = "Part Number - TNT - O_INVPART")
	private String partNumber;
	
	@ApiModelProperty(value = "Part Name - PARTNAME - O_INVPART")
	private String partName;
	
	@ApiModelProperty(value = "Place - O_INVPART")
	private String place;
	
	@ApiModelProperty(value = "INVPART_ID - O_INVPART")
	@NotBlank
	private String inventoryPartId;
	
	@ApiModelProperty(value = "Amount - NEW_STOCK - O_INVPART")
	private String amount;
	
	@ApiModelProperty(value = "USER1 - O_INVPART")
	private String user1;
	
	@ApiModelProperty(value = "USER2 - O_INVPART")
	private String user2;
	
	@ApiModelProperty(value = "Paginier-Nummer - O_INVPART- CNT")
	private String paginationNo;
	
	@ApiModelProperty(value = "Count of Places - O_INVPART- COUNT_OF_PLACES")
	private String countOfPlaces;

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
	 * @return the partName
	 */
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
	 * @return the place
	 */
	public String getPlace() {
		return place;
	}

	/**
	 * @param place the place to set
	 */
	public void setPlace(String place) {
		this.place = place;
	}

	/**
	 * @return the inventoryPartId
	 */
	public String getInventoryPartId() {
		return inventoryPartId;
	}

	/**
	 * @param inventoryPartId the inventoryPartId to set
	 */
	public void setInventoryPartId(String inventoryPartId) {
		this.inventoryPartId = inventoryPartId;
	}

	/**
	 * @return the amount
	 */
	public String getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(String amount) {
		this.amount = amount;
	}

	/**
	 * @return the user1
	 */
	public String getUser1() {
		return user1;
	}

	/**
	 * @param user1 the user1 to set
	 */
	public void setUser1(String user1) {
		this.user1 = user1;
	}

	/**
	 * @return the user2
	 */
	public String getUser2() {
		return user2;
	}

	/**
	 * @param user2 the user2 to set
	 */
	public void setUser2(String user2) {
		this.user2 = user2;
	}

	/**
	 * @return the paginationNo
	 */
	public String getPaginationNo() {
		return paginationNo;
	}

	/**
	 * @param paginationNo the paginationNo to set
	 */
	public void setPaginationNo(String paginationNo) {
		this.paginationNo = paginationNo;
	}

	/**
	 * @return the countOfPlaces
	 */
	public String getCountOfPlaces() {
		return countOfPlaces;
	}

	/**
	 * @param countOfPlaces the countOfPlaces to set
	 */
	public void setCountOfPlaces(String countOfPlaces) {
		this.countOfPlaces = countOfPlaces;
	}
	
	
	
	
	
}
