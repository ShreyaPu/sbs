package com.alphax.vo.mb;

import org.springframework.validation.annotation.Validated;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Validated
@ApiModel(description = "This Object about inventory differential list details")
public class InventoryDiferentialListDTO {

	@ApiModelProperty(value = "OEM - O_INVPART")
	private String manufacturer;
	
	@ApiModelProperty(value = "Teilenummer - TNR - O_INVPART")
	private String partNumber;
	
	@ApiModelProperty(value = "Part Name - PartName - O_INVPART")
	private String partName;
	
	@ApiModelProperty(value = "Count - CNT - O_INVPART")
	private String count;
	
	@ApiModelProperty(value = "Old Stock - OLD_STOCK - O_INVPART")
	private String oldStock;
	
	@ApiModelProperty(value = "New Stock - NEW_STOCK - O_INVPART")
	private String newStock;
	
	@ApiModelProperty(value = "Sum of new Stock - SUM_NEW_STOCK ")
	private String sum_newStock;
	
	@ApiModelProperty(value = "Dak - DAK - O_INVPART")
	private String dak;
	
	@ApiModelProperty(value = "Delta pieces - DELTA_PIECES - O_INVPART")
	private String deltaPieces;
	
	@ApiModelProperty(value = "Delta cur - DELTA_CUR - O_INVPART")
	private String deltaCur;
	
	@ApiModelProperty(value = "Checkbox value - Check_it ")
	private String checkItFlag;
	
	@ApiModelProperty(value = "Inventory Part Status ID - INVPARTSTS_ID - O_INVPART")
	private String inventoryPartStatusId;
	
	@ApiModelProperty(value = "Inventory Part ID - ID - O_INVPART")
	private String inventoryPartId;
	
	@ApiModelProperty(value = "Inventory List ID - INVLIST_ID - O_INVPART")
	private String inventoryListId;
	
	
	public InventoryDiferentialListDTO() {
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
	 * @return the count
	 */
	public String getCount() {
		return count;
	}


	/**
	 * @param count the count to set
	 */
	public void setCount(String count) {
		this.count = count;
	}


	/**
	 * @return the oldStock
	 */
	public String getOldStock() {
		return oldStock;
	}


	/**
	 * @param oldStock the oldStock to set
	 */
	public void setOldStock(String oldStock) {
		this.oldStock = oldStock;
	}


	/**
	 * @return the newStock
	 */
	public String getNewStock() {
		return newStock;
	}


	/**
	 * @param newStock the newStock to set
	 */
	public void setNewStock(String newStock) {
		this.newStock = newStock;
	}


	/**
	 * @return the sum_newStock
	 */
	public String getSum_newStock() {
		return sum_newStock;
	}


	/**
	 * @param sum_newStock the sum_newStock to set
	 */
	public void setSum_newStock(String sum_newStock) {
		this.sum_newStock = sum_newStock;
	}


	/**
	 * @return the dak
	 */
	public String getDak() {
		return dak;
	}


	/**
	 * @param dak the dak to set
	 */
	public void setDak(String dak) {
		this.dak = dak;
	}


	/**
	 * @return the deltaPieces
	 */
	public String getDeltaPieces() {
		return deltaPieces;
	}


	/**
	 * @param deltaPieces the deltaPieces to set
	 */
	public void setDeltaPieces(String deltaPieces) {
		this.deltaPieces = deltaPieces;
	}


	/**
	 * @return the deltaCur
	 */
	public String getDeltaCur() {
		return deltaCur;
	}


	/**
	 * @param deltaCur the deltaCur to set
	 */
	public void setDeltaCur(String deltaCur) {
		this.deltaCur = deltaCur;
	}


	/**
	 * @return the checkItFlag
	 */
	public String getCheckItFlag() {
		return checkItFlag;
	}


	/**
	 * @param checkItFlag the checkItFlag to set
	 */
	public void setCheckItFlag(String checkItFlag) {
		this.checkItFlag = checkItFlag;
	}


	/**
	 * @return the inventoryPartStatusId
	 */
	public String getInventoryPartStatusId() {
		return inventoryPartStatusId;
	}


	/**
	 * @param inventoryPartStatusId the inventoryPartStatusId to set
	 */
	public void setInventoryPartStatusId(String inventoryPartStatusId) {
		this.inventoryPartStatusId = inventoryPartStatusId;
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
	
}