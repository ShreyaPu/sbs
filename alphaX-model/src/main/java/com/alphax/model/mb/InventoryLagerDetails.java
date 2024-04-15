package com.alphax.model.mb;

import com.alphax.common.util.DBTable;

public class InventoryLagerDetails {
	
	@DBTable(columnName ="LGNR", required = true)
	private String warehouseNumber;
	
	@DBTable(columnName ="PGNR", required = true)
	private String pgNumber;

	@DBTable(columnName ="VFNR", required = true)
	private String vfNumber;
	
	@DBTable(columnName ="KB", required = true)
	private String kb;
	
	@DBTable(columnName ="DATUMB", required = true)
	private String fiscalDate;

	
	public InventoryLagerDetails() {

	}


	/**
	 * @return the warehouseNumber
	 */
	public String getWarehouseNumber() {
		return warehouseNumber;
	}


	/**
	 * @param warehouseNumber the warehouseNumber to set
	 */
	public void setWarehouseNumber(String warehouseNumber) {
		this.warehouseNumber = warehouseNumber;
	}


	/**
	 * @return the pgNumber
	 */
	public String getPgNumber() {
		return pgNumber;
	}


	/**
	 * @param pgNumber the pgNumber to set
	 */
	public void setPgNumber(String pgNumber) {
		this.pgNumber = pgNumber;
	}


	/**
	 * @return the vfNumber
	 */
	public String getVfNumber() {
		return vfNumber;
	}


	/**
	 * @param vfNumber the vfNumber to set
	 */
	public void setVfNumber(String vfNumber) {
		this.vfNumber = vfNumber;
	}


	/**
	 * @return the kb
	 */
	public String getKb() {
		return kb;
	}


	/**
	 * @param kb the kb to set
	 */
	public void setKb(String kb) {
		this.kb = kb;
	}


	/**
	 * @return the fiscalDate
	 */
	public String getFiscalDate() {
		return fiscalDate;
	}


	/**
	 * @param fiscalDate the fiscalDate to set
	 */
	public void setFiscalDate(String fiscalDate) {
		this.fiscalDate = fiscalDate;
	}
	
}