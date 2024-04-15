package com.alphax.vo.mb;

import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about Record Differences (Differenzen erfassen )")
public class RecordDifferences_DTO {

	private String deliveryNoteNo;
	
	private String bookingText;
	
	private String account;
	
	private String manufacturer;
	
	private String warehouseNumber;
	
	private String supplierNumber;
	
	private String amount;
	
	private boolean existingEntryFlag;
	

	/**
	 * @return the deliveryNoteNo
	 */
	@ApiModelProperty(value = " DeliveryNoteNo ( %% - DB E_LFSL and E_BSNLFS - NRX).")
	@Size(max =10)
	public String getDeliveryNoteNo() {
		return deliveryNoteNo;
	}

	/**
	 * @param deliveryNoteNo the deliveryNoteNo to set
	 */
	public void setDeliveryNoteNo(String deliveryNoteNo) {
		this.deliveryNoteNo = deliveryNoteNo;
	}

	/**
	 * @return the manufacturer
	 */
	@ApiModelProperty(value = " Manufacturer ( %% - DB E_LFSL and E_BSNLFS - HERST).")
	@Size(max =4)
	public String getManufacturer() {
		return manufacturer;
	}

	/**
	 * @param manufacturer the manufacturer to set
	 */
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	/**
	 * @return the warehouseNumber
	 */
	@ApiModelProperty(value = " WarehouseNumber ( %% - DB E_LFSL and E_BSNLFS - LNR).")
	@Size(max =2)
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
	 * @return the supplierNumber
	 */
	@ApiModelProperty(value = " Supplier Number ( %% - DB E_LFSL and E_BSNLFS - BENDL).")
	@Size(max =5)
	public String getSupplierNumber() {
		return supplierNumber;
	}

	/**
	 * @param supplierNumber the supplierNumber to set
	 */
	public void setSupplierNumber(String supplierNumber) {
		this.supplierNumber = supplierNumber;
	}

	/**
	 * @return the bookingText
	 */
	@ApiModelProperty(value = "Booking Text (Buchungstext) ( %% - DB E_BSNLFS - BUKONTOTXT).")
	@Size(max =30)
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
	@ApiModelProperty(value = "Account (Konto) ( %% - DB E_BSNLFS - BUKONTO).")
	@Size(max =10)
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
	@ApiModelProperty(value = "Amount (Betrag) ( %% - DB E_BSNLFS - NWERT_SEPSIGN).")
	@Size(max =10)
	public String getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(String amount) {
		this.amount = amount;
	}

	public boolean isExistingEntryFlag() {
		return existingEntryFlag;
	}

	public void setExistingEntryFlag(boolean existingEntryFlag) {
		this.existingEntryFlag = existingEntryFlag;
	}
	
	
	
}
