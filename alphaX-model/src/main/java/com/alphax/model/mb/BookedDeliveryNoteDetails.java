/**
 * 
 */
package com.alphax.model.mb;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;


/**
 * @author A87740971
 *
 */
@ApiModel(description = "All fields about invoiced details and delivery note details for items of a delivery note")
public class BookedDeliveryNoteDetails {

	@DBTable(columnName ="Lieferschein", required = true)
	private String deliveryNoteNo; //Lieferschein
	
	@DBTable(columnName ="POS", required = true)
	private String deliveryNotePosition; //POS
	
	@DBTable(columnName ="TNR", required = true)
	private String partNumber; //TNR
	
	@DBTable(columnName ="Bezeichnung", required = true)
	private String designation; //Bezeichnung
	
	@DBTable(columnName ="Menge_lt_Rechnung", required = true)
	private String quantityAccordingToInvoice;  //Menge lt. Rechnung
	
	@DBTable(columnName ="MWST", required = true)
	private String vat; //MWST
	
	@DBTable(columnName ="Stuckpreis_lt_Rechnung", required = true)
	private String unitPriceAccordingToInvoice; //Stückpreis lt. Rechnung
	
	@DBTable(columnName ="Gebuchte_Menge", required = true)
	private String postedAmount; //Gebuchte Menge
	
	@DBTable(columnName ="Gebuchter_Preis", required = true)
	private String bookedPrice;  //Gebuchter Preis
	
	@DBTable(columnName ="ORIGIN", required = true)
	private String origin;  //ORIGIN

	/**
	 * @return the deliveryNoteNo
	 */
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
	 * @return the deliveryNotePosition
	 */
	public String getDeliveryNotePosition() {
		return deliveryNotePosition;
	}

	/**
	 * @param deliveryNotePosition the deliveryNotePosition to set
	 */
	public void setDeliveryNotePosition(String deliveryNotePosition) {
		this.deliveryNotePosition = deliveryNotePosition;
	}

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
	 * @return the designation
	 */
	public String getDesignation() {
		return designation;
	}

	/**
	 * @param designation the designation to set
	 */
	public void setDesignation(String designation) {
		this.designation = designation;
	}

	/**
	 * @return the quantityAccordingToInvoice
	 */
	public String getQuantityAccordingToInvoice() {
		return quantityAccordingToInvoice;
	}

	/**
	 * @param quantityAccordingToInvoice the quantityAccordingToInvoice to set
	 */
	public void setQuantityAccordingToInvoice(String quantityAccordingToInvoice) {
		this.quantityAccordingToInvoice = quantityAccordingToInvoice;
	}

	/**
	 * @return the vat
	 */
	public String getVat() {
		return vat;
	}

	/**
	 * @param vat the vat to set
	 */
	public void setVat(String vat) {
		this.vat = vat;
	}

	/**
	 * @return the unitPriceAccordingToInvoice
	 */
	public String getUnitPriceAccordingToInvoice() {
		return unitPriceAccordingToInvoice;
	}

	/**
	 * @param unitPriceAccordingToInvoice the unitPriceAccordingToInvoice to set
	 */
	public void setUnitPriceAccordingToInvoice(String unitPriceAccordingToInvoice) {
		this.unitPriceAccordingToInvoice = unitPriceAccordingToInvoice;
	}

	/**
	 * @return the postedAmount
	 */
	public String getPostedAmount() {
		return postedAmount;
	}

	/**
	 * @param postedAmount the postedAmount to set
	 */
	public void setPostedAmount(String postedAmount) {
		this.postedAmount = postedAmount;
	}

	/**
	 * @return the bookedPrice
	 */
	public String getBookedPrice() {
		return bookedPrice;
	}

	/**
	 * @param bookedPrice the bookedPrice to set
	 */
	public void setBookedPrice(String bookedPrice) {
		this.bookedPrice = bookedPrice;
	}
	
	/**
	 * @return the origin
	 */
	public String getOrigin() {
		return origin;
	}

	/**
	 * @param origin the origin to set
	 */
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	
	
	
}
