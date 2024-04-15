package com.alphax.vo.mb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Inventory List [O_INVLIST] table DTO ")
public class CreatedCountingListValues {

	@ApiModelProperty(value = "Inventory List Id - O_INVLIST(INVLIST_ID)")
	private String inventoryListId;
	
	@ApiModelProperty(value = "Inventory List Name -O_INVLIST(NAME)")
	private String inventoryListName;
	
	@ApiModelProperty(value = "Number Of Rrecord Type-SA1 - O_INVPART (COUNT_SA1)")
	private String numberOfRrecordType1;
	
	@ApiModelProperty(value = "Number Of Rrecord Type-SA2 -  O_INVPART (COUNT_SA1)")
	private String numberOfRrecordType2;
	
	@ApiModelProperty(value = "Sort by clouse -  O_INVLIST (SORT_BY)")
	private String sortBy;
	
	@ApiModelProperty(value = "Create By")
	private String createBy;
	
	@ApiModelProperty(value = "Creation Date")
	private String creationDate;

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
	 * @return the inventoryListName
	 */
	public String getInventoryListName() {
		return inventoryListName;
	}

	/**
	 * @param inventoryListName the inventoryListName to set
	 */
	public void setInventoryListName(String inventoryListName) {
		this.inventoryListName = inventoryListName;
	}

	/**
	 * @return the numberOfRrecordType1
	 */
	public String getNumberOfRrecordType1() {
		return numberOfRrecordType1;
	}

	/**
	 * @param numberOfRrecordType1 the numberOfRrecordType1 to set
	 */
	public void setNumberOfRrecordType1(String numberOfRrecordType1) {
		this.numberOfRrecordType1 = numberOfRrecordType1;
	}

	/**
	 * @return the numberOfRrecordType2
	 */
	public String getNumberOfRrecordType2() {
		return numberOfRrecordType2;
	}

	/**
	 * @param numberOfRrecordType2 the numberOfRrecordType2 to set
	 */
	public void setNumberOfRrecordType2(String numberOfRrecordType2) {
		this.numberOfRrecordType2 = numberOfRrecordType2;
	}

	/**
	 * @return the sortBy
	 */
	public String getSortBy() {
		return sortBy;
	}

	/**
	 * @param sortBy the sortBy to set
	 */
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	/**
	 * @return the createBy
	 */
	public String getCreateBy() {
		return createBy;
	}

	/**
	 * @param createBy the createBy to set
	 */
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	/**
	 * @return the creationDate
	 */
	public String getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	

	
	
}
