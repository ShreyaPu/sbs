package com.alphax.vo.mb;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about Invoice Differences")
public class InvoiceDifferncesDTO {
	
	@ApiModelProperty(value = "RED_BUBEGR - PEW_REDIFF Table")
	private String bookingTerm;
	
	@ApiModelProperty(value = "RED_DTANLG - PEW_REDIFF Table")
	private String entryDate;
	
	@ApiModelProperty(value = "RED_LBEARB - PEW_REDIFF Table")
	private String lastEditor;
	
	@ApiModelProperty(value = "RED_DTAEND - PEW_REDIFF Table")
	private String lastModifiedDate;
	
	@ApiModelProperty(value = "RED_NLOES - PEW_REDIFF Table")
	private BigDecimal clearFlag;
	
	@ApiModelProperty(value = "RED_WESKTO - PEW_REDIFF Table")
	private String accountNumber;
	
	@ApiModelProperty(value = "RENUM - E_BSNRCH Table")
	private String invoicesNumber;
	
	
	
	public InvoiceDifferncesDTO() {
	}


	/**
	 * @return the bookingTerm
	 */
	public String getBookingTerm() {
		return bookingTerm;
	}


	/**
	 * @param bookingTerm the bookingTerm to set
	 */
	public void setBookingTerm(String bookingTerm) {
		this.bookingTerm = bookingTerm;
	}


	/**
	 * @return the entryDate
	 */
	public String getEntryDate() {
		return entryDate;
	}


	/**
	 * @param entryDate the entryDate to set
	 */
	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}


	/**
	 * @return the lastEditor
	 */
	public String getLastEditor() {
		return lastEditor;
	}


	/**
	 * @param lastEditor the lastEditor to set
	 */
	public void setLastEditor(String lastEditor) {
		this.lastEditor = lastEditor;
	}


	/**
	 * @return the lastModifiedDate
	 */
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}


	/**
	 * @param lastModifiedDate the lastModifiedDate to set
	 */
	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}


	/**
	 * @return the clearFlag
	 */
	public BigDecimal getClearFlag() {
		return clearFlag;
	}


	/**
	 * @param clearFlag the clearFlag to set
	 */
	public void setClearFlag(BigDecimal clearFlag) {
		this.clearFlag = clearFlag;
	}


	/**
	 * @return the accountNumber
	 */
	public String getAccountNumber() {
		return accountNumber;
	}


	/**
	 * @param accountNumber the accountNumber to set
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}


	/**
	 * @return the invoicesNumber
	 */
	public String getInvoicesNumber() {
		return invoicesNumber;
	}


	/**
	 * @param invoicesNumber the invoicesNumber to set
	 */
	public void setInvoicesNumber(String invoicesNumber) {
		this.invoicesNumber = invoicesNumber;
	}
	
	
	
}