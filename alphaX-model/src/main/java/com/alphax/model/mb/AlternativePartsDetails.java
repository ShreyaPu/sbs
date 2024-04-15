package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about Alternative Parts Details")
public class AlternativePartsDetails {

	@DBTable(columnName ="TNRN", required = true)
	private String newPartNumber;
	
	@DBTable(columnName ="TNR", required = true)
	private String partNumber;
	
	@DBTable(columnName ="partName", required = true)
	private String partName;
	
	@DBTable(columnName ="CODEF", required = true)
	private String codeEF;
	
	@DBTable(columnName ="CODEB", required = true)
	private String codeEB;
	
	@DBTable(columnName ="TEXT", required = true)
	private String codeValue;
	
	@DBTable(columnName ="CODE", required = true)
	private String code;
	
	@DBTable(columnName = "AKTBES", required = true)
	private BigDecimal currentStock;
	
	@DBTable(columnName = "LNR", required = true)
	private BigDecimal warehouseNo;
	
	@DBTable(columnName = "FIRMA", required = true)
	private BigDecimal companyId;

	@DBTable(columnName = "FILIAL", required = true)
	private BigDecimal agencyId;
	
	@DBTable(columnName = "HERST", required = true)
	private String oem;

	
	

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
	 * @return the codeEF
	 */
	public String getCodeEF() {
		return codeEF;
	}

	/**
	 * @param codeEF the codeEF to set
	 */
	public void setCodeEF(String codeEF) {
		this.codeEF = codeEF;
	}

	/**
	 * @return the codeEB
	 */
	public String getCodeEB() {
		return codeEB;
	}

	/**
	 * @param codeEB the codeEB to set
	 */
	public void setCodeEB(String codeEB) {
		this.codeEB = codeEB;
	}

	/**
	 * @return the codeValue
	 */
	public String getCodeValue() {
		return codeValue;
	}

	/**
	 * @param codeValue the codeValue to set
	 */
	public void setCodeValue(String codeValue) {
		this.codeValue = codeValue;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the newPartNumber
	 */
	public String getNewPartNumber() {
		return newPartNumber;
	}

	/**
	 * @param newPartNumber the codeEB to set
	 */
	public void setNewPartNumber(String newPartNumber) {
		this.newPartNumber = newPartNumber;
	}

	public BigDecimal getCurrentStock() {
		return currentStock;
	}

	public void setCurrentStock(BigDecimal currentStock) {
		this.currentStock = currentStock;
	}

	public BigDecimal getWarehouseNo() {
		return warehouseNo;
	}

	public void setWarehouseNo(BigDecimal warehouseNo) {
		this.warehouseNo = warehouseNo;
	}

	public BigDecimal getCompanyId() {
		return companyId;
	}

	public void setCompanyId(BigDecimal companyId) {
		this.companyId = companyId;
	}

	public BigDecimal getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(BigDecimal agencyId) {
		this.agencyId = agencyId;
	}

	public String getOem() {
		return oem;
	}

	public void setOem(String oem) {
		this.oem = oem;
	}
	
	
	
}
