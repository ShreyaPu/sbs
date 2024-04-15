package com.alphax.model.mb;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about Record Differences (Differenzen erfassen )")
public class RecordDifferences {

	@DBTable(columnName ="BUKONTOTXT", required = true)
	private String bookingText;
	
	@DBTable(columnName ="BUKONTO", required = true)
	private String account;
	
	@DBTable(columnName ="NWERT_SEPSIGN", required = true)
	private String amount;
	
	@DBTable(columnName ="sum_of_all_booked_net_values", required = true)
	private String sumOfAllBookedNetValue;
	
	@DBTable(columnName ="sum_of_all_invoice_values", required = true)
	private String sumOfAllInvoiceValue;

	/**
	 * @return the bookingText
	 */
	public String getBookingText() {
		return bookingText;
	}

	/**
	 * @param bookingText the bookingText to set
	 */
	public void setBookingText(String bookingText) {
		this.bookingText = bookingText;
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

	/**
	 * @return the amount
	 */
	public String getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getSumOfAllBookedNetValue() {
		return sumOfAllBookedNetValue;
	}

	public void setSumOfAllBookedNetValue(String sumOfAllBookedNetValue) {
		this.sumOfAllBookedNetValue = sumOfAllBookedNetValue;
	}

	public String getSumOfAllInvoiceValue() {
		return sumOfAllInvoiceValue;
	}

	public void setSumOfAllInvoiceValue(String sumOfAllInvoiceValue) {
		this.sumOfAllInvoiceValue = sumOfAllInvoiceValue;
	}
	
	
}
