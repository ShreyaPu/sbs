package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description="All fields about Additional Costs of Invoice")
public class AddCostsDetails {
	
	@DBTable(columnName ="ID", required = true)
	private Long addCostsId;
	
	@DBTable(columnName ="INVOICENR", required = true)
	private String invoiceNo;
	
	@DBTable(columnName ="COSTSTYPE", required = true)
	private String costsType;
	
	@DBTable(columnName ="COSTSVALUE", required = true)
	private BigDecimal costsValue;
	
	@DBTable(columnName ="TEXT", required = true)
	private String text;
	
	@DBTable(columnName ="ACCOUNT", required = true)
	private String account;
	
	
	public AddCostsDetails() {
	}


	/**
	 * @return the addCostsId
	 */
	public Long getAddCostsId() {
		return addCostsId;
	}


	/**
	 * @param addCostsId the addCostsId to set
	 */
	public void setAddCostsId(Long addCostsId) {
		this.addCostsId = addCostsId;
	}


	/**
	 * @return the invoiceNo
	 */
	public String getInvoiceNo() {
		return invoiceNo;
	}


	/**
	 * @param invoiceNo the invoiceNo to set
	 */
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}


	/**
	 * @return the costsType
	 */
	public String getCostsType() {
		return costsType;
	}


	/**
	 * @param costsType the costsType to set
	 */
	public void setCostsType(String costsType) {
		this.costsType = costsType;
	}


	/**
	 * @return the costsValue
	 */
	public BigDecimal getCostsValue() {
		return costsValue;
	}


	/**
	 * @param costsValue the costsValue to set
	 */
	public void setCostsValue(BigDecimal costsValue) {
		this.costsValue = costsValue;
	}


	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}


	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}


	/**
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}


	/**
	 * @param account the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
	}
	
	
	
}