package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about Invoice Differences")
public class InvoiceDiffernces {
	
	@DBTable(columnName ="RED_BUBEGR", required = true)
	private String bookingTerm;
	
	@DBTable(columnName ="RED_DTANLG", required = true)
	private String entryDate;
	
	@DBTable(columnName ="RED_LBEARB", required = true)
	private String lastEditor;
	
	@DBTable(columnName ="RED_DTAEND", required = true)
	private String lastModifiedDate;
	
	@DBTable(columnName ="RED_NLOES", required = true)
	private BigDecimal clearFlag;
	
	@DBTable(columnName ="RED_WESKTO", required = true)
	private String accountNumber;
	
	@DBTable(columnName ="RENUM", required = true)
	private String invoicesNumber;
	
	
	public InvoiceDiffernces() {
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
