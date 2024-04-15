package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about AlphaX Admin setting for Warehouse")
public class AdminWarehouse {

	@DBTable(columnName ="WAREHOUS_ID", required = true)
	private BigDecimal warehouseId;
	
	@DBTable(columnName ="AP_WAREHOUS_ID", required = true)
	private String alphaPlusWarehouseId;
	
	@DBTable(columnName ="NAME", required = true)
	private String warehouseName;
	
	@DBTable(columnName ="CITY", required = true)
	private String city;
	
	@DBTable(columnName ="POST_CODE", required = true)
	private String postalCode;
	
	@DBTable(columnName ="STREETANDNUM", required = true)
	private String streetNumber;
	
	@DBTable(columnName ="ISACTIVE", required = true)
	private BigDecimal active;
	
	@DBTable(columnName ="VF_NUMBER", required = true)
	private BigDecimal vfNumber;
	
	@DBTable(columnName ="ISDELETED ", required = true)
	private BigDecimal deletedFlag;
	
	
	public AdminWarehouse() {
		
	}


	/**
	 * @return the warehouseId
	 */
	public BigDecimal getWarehouseId() {
		return warehouseId;
	}


	/**
	 * @param warehouseId the warehouseId to set
	 */
	public void setWarehouseId(BigDecimal warehouseId) {
		this.warehouseId = warehouseId;
	}


	/**
	 * @return the alphaPlusWarehouseId
	 */
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
	 * @return the city
	 */
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
	 * @return the active
	 */
	public BigDecimal getActive() {
		return active;
	}


	/**
	 * @param active the active to set
	 */
	public void setActive(BigDecimal active) {
		this.active = active;
	}


	public BigDecimal getVfNumber() {
		return vfNumber;
	}


	public void setVfNumber(BigDecimal vfNumber) {
		this.vfNumber = vfNumber;
	}


	/**
	 * @return the deletedFlag
	 */
	public BigDecimal getDeletedFlag() {
		return deletedFlag;
	}


	/**
	 * @param deletedFlag the deletedFlag to set
	 */
	public void setDeletedFlag(BigDecimal deletedFlag) {
		this.deletedFlag = deletedFlag;
	}
	
	
	
}