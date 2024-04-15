package com.alphax.vo.mb;

import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about AlphaX Admin setting for Warehouse")
public class AdminWarehouseDTO {

	private String warehouseId;
	
	private String alphaPlusWarehouseId;
	
	private String warehouseName;
	
	private String city;
	
	private String postalCode;
	
	private String streetNumber;
	
	private boolean active;
	
	private String vfNumber;
	
	
	
	/**
	 * @return the warehouseId
	 */
	@ApiModelProperty(value = "warehouse Id ( %% - DB O_WRH - WAREHOUS_ID).")
	@Size(max=3)
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
	 * @return the alphaPlusWarehouseId
	 */
	@ApiModelProperty(value = "AlphaPlus warehouse Id ( %% - DB O_WRH - AP_WAREHOUS_ID).")
	public String getAlphaPlusWarehouseId() {
		return alphaPlusWarehouseId;
	}

	/**
	 * @param alphaPlusWarehouseId the alphaPlusWarehouseId to set
	 */
	public void setAlphaPlusWarehouseId(String alphaPlusWarehouseId) {
		this.alphaPlusWarehouseId = alphaPlusWarehouseId;
	}

	/**
	 * @return the active
	 */
	@ApiModelProperty(value = "Active ( %% - DB O_WRH - ISACTIVE).")
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}


	/**
	 * @return the warehouseName
	 */
	@ApiModelProperty(value = "Warehouse Name ( %% - DB O_WRH - NAME).")
	@Size(max=50)
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
	 * @return the city
	 */
	@ApiModelProperty(value = "Warehouse City ( %% - DB O_WRH - CITY).")
	@Size(max=50)
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the postalCode
	 */
	@ApiModelProperty(value = "Postal Code ( %% - DB O_WRH - POST_COD).")
	@Size(max=6)
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * @param postalCode the postalCode to set
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	/**
	 * @return the streetNumber
	 */
	@ApiModelProperty(value = "Street Number ( %% - DB O_WRH - STREETANDNUM ).")
	@Size(max=30)
	public String getStreetNumber() {
		return streetNumber;
	}

	/**
	 * @param streetNumber the streetNumber to set
	 */
	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	/**
	 * @return the vfNumber
	 */
	@ApiModelProperty(value = "VF Number ( %% - DB O_WRH - VF_NUMBER ).")
	@Size(max=5)
	public String getVfNumber() {
		return vfNumber;
	}

	/**
	 * @param vfNumber the vfNumber to set
	 */
	public void setVfNumber(String vfNumber) {
		this.vfNumber = vfNumber;
	}
	
	
	
	
	
	
}
