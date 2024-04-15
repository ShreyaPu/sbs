package com.alphax.vo.mb;

import java.util.List;

import com.alphax.vo.mb.CreatedCountingListValues;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Inventory List [O_INVLIST] table DTO ")
public class CreatedCountingListDTO {

	@ApiModelProperty(value = "WarehouseId - Warehouse_ID")
	private String warehouseId;
	
	@ApiModelProperty(value = "WarehouseName - WAREHOUSE_NAME")
	private String warehouseName;
	
	@ApiModelProperty(value = "Created Counting List Values")
	private List<CreatedCountingListValues>  createdCountingListValues;

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
	 * @return the warehouseName
	 */
	public String getWarehouseName() {
		return warehouseName;
	}

	/**
	 * @param warehouseName the warehouseName to set
	 */
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	/**
	 * @return the createdCountingListValues
	 */
	public List<CreatedCountingListValues> getCreatedCountingListValues() {
		return createdCountingListValues;
	}

	/**
	 * @param createdCountingListValues the createdCountingListValues to set
	 */
	public void setCreatedCountingListValues(List<CreatedCountingListValues> createdCountingListValues) {
		this.createdCountingListValues = createdCountingListValues;
	}
	
	
	
	
}
