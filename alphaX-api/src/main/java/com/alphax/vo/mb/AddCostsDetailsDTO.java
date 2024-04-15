package com.alphax.vo.mb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="All fields about Additional Costs of Invoice")
public class AddCostsDetailsDTO {
	
	@ApiModelProperty(value = "ID - O_ADDCOSTS Table")
	private String addCostsId;
	
	@ApiModelProperty(value = "INVOICENR - O_ADDCOSTS Table")
	private String invoiceNo;
	
	@ApiModelProperty(value = "COSTSTYPE - O_ADDCOSTS Table")
	private String costsType;
	
	@ApiModelProperty(value = "COSTSVALUE - O_ADDCOSTS Table")
	private String costsValue;
	
	@ApiModelProperty(value = "TEXT - O_ADDCOSTS Table")
	private String text;
	
	@ApiModelProperty(value = "ACCOUNT - O_ADDCOSTS Table")
	private String account;
	
	
	public AddCostsDetailsDTO() {
	}


	/**
	 * @return the addCostsId
	 */
	public String getAddCostsId() {
		return addCostsId;
	}


	/**
	 * @param addCostsId the addCostsId to set
	 */
	public void setAddCostsId(String addCostsId) {
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
	public String getCostsValue() {
		return costsValue;
	}


	/**
	 * @param costsValue the costsValue to set
	 */
	public void setCostsValue(String costsValue) {
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