package com.alphax.vo.mb;

import java.util.Map;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about Parts Tree View Details")
public class PartsTreeViewDTO {

	private String partNumber;
	
	private String partName;
	
	private String currentStock;
	
	private Map<String, String> keyValueMap;
	
	private String parentId;

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
	 * @return the keyValueMap
	 */
	public Map<String, String> getKeyValueMap() {
		return keyValueMap;
	}

	/**
	 * @param keyValueMap the keyValueMap to set
	 */
	public void setKeyValueMap(Map<String, String> keyValueMap) {
		this.keyValueMap = keyValueMap;
	}

	/**
	 * @return the parentId
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	
}
