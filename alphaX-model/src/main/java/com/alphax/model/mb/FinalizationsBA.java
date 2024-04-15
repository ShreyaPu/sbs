package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about Finalisierungen BA 50 and 67 (Finalizations BA)")
public class FinalizationsBA {

	@DBTable(columnName ="BENEN", required = true)
	private String inventoryDescription;
	
	@DBTable(columnName ="AKTBES", required = true)
	private BigDecimal bookingAmount;
	
	@DBTable(columnName = "BESAUS", required = true)
	private BigDecimal pendingOrders;
	

	public String getInventoryDescription() {
		return inventoryDescription;
	}

	public void setInventoryDescription(String inventoryDescription) {
		this.inventoryDescription = inventoryDescription;
	}

	public BigDecimal getBookingAmount() {
		return bookingAmount;
	}

	public void setBookingAmount(BigDecimal bookingAmount) {
		this.bookingAmount = bookingAmount;
	}

	/**
	 * @return the pendingOrders
	 */
	public BigDecimal getPendingOrders() {
		return pendingOrders;
	}

	/**
	 * @param pendingOrders the pendingOrders to set
	 */
	public void setPendingOrders(BigDecimal pendingOrders) {
		this.pendingOrders = pendingOrders;
	}	
	
	
	
}
