package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

public class LagerDetails {

	@DBTable(columnName = "KEY2", required = true)
	private BigDecimal warehouseNo;

	@DBTable(columnName = "PNAME", required = true)
	private String WarehouseName;

	@DBTable(columnName = "PSTRAS", required = true)
	private String street;
	
	@DBTable(columnName = "PLZ", required = true)
	private String plz;
	
	@DBTable(columnName = "PORT", required = true)
	private String port;
	
	@DBTable(columnName = "PLZ_2", required = true)
	private String plz_2;
	
	@DBTable(columnName ="VFNR", required = true)
	private BigDecimal vfNumber;

	
	public LagerDetails() {

	}


	/**
	 * @return the warehouseNo
	 */
	public BigDecimal getWarehouseNo() {
		return warehouseNo;
	}


	/**
	 * @param warehouseNo the warehouseNo to set
	 */
	public void setWarehouseNo(BigDecimal warehouseNo) {
		this.warehouseNo = warehouseNo;
	}


	/**
	 * @return the warehouseName
	 */
	public String getWarehouseName() {
		return WarehouseName;
	}


	/**
	 * @param warehouseName the warehouseName to set
	 */
	public void setWarehouseName(String warehouseName) {
		WarehouseName = warehouseName;
	}


	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}


	/**
	 * @param street the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}


	/**
	 * @return the plz
	 */
	public String getPlz() {
		return plz;
	}


	/**
	 * @param plz the plz to set
	 */
	public void setPlz(String plz) {
		this.plz = plz;
	}


	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}


	/**
	 * @param port the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}


	public String getPlz_2() {
		return plz_2;
	}


	public void setPlz_2(String plz_2) {
		this.plz_2 = plz_2;
	}


	/**
	 * @return the vfNumber
	 */
	public BigDecimal getVfNumber() {
		return vfNumber;
	}


	/**
	 * @param vfNumber the vfNumber to set
	 */
	public void setVfNumber(BigDecimal vfNumber) {
		this.vfNumber = vfNumber;
	}
	
	
	
	
}
