package com.alphax.vo.mb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about add counted part manuelly")
public class AddCountedPartDTO {

	@ApiModelProperty(value = "Manufacturer - OEM - O_INVPART")
	private String manufacturer;
	
	@ApiModelProperty(value = "Teilenummer - TNT - O_INVPART")
	private String partNumber;
	
	@ApiModelProperty(value = "place - O_INVPART")
	private String place;
	
	@ApiModelProperty(value = "Amount - NEW_STOCK - O_INVPART")
	private String amount;
	
	@ApiModelProperty(value = "USER1 - O_INVPART")
	private String user1;
	
	@ApiModelProperty(value = "USER2 - O_INVPART")
	private String user2;
	
	@ApiModelProperty(value = "inventoryListId - INVLIST_ID - O_INVPART")
	private String inventoryListId;
	
	@ApiModelProperty(value = "Warehouse Id")
	private String warehouseId;
	
	@ApiModelProperty(value = "inventoryPartId - ID - O_INVPART")
	private String inventoryPartId;


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
	 * @return the inventoryListId
	 */
	public String getInventoryListId() {
		return inventoryListId;
	}

	/**
	 * @param inventoryListId the inventoryListId to set
	 */
	public void setInventoryListId(String inventoryListId) {
		this.inventoryListId = inventoryListId;
	}

	/**
	 * @return the warehouseId
	 */
	public String getWarehouseId() {
		return warehouseId;
	}

	/**
	 * @param warehouseId the warehouseId to set
	 */
	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
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
	
	
	
}
