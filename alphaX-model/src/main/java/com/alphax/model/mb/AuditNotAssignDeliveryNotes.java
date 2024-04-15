package com.alphax.model.mb;

import com.alphax.common.util.DBTable;

public class AuditNotAssignDeliveryNotes {
	


	@DBTable(columnName = "BELNR", required = true)
	private String deliveryNote;
	
	@DBTable(columnName = "PREIS", required = true)
	private String deliveryNotePrice;

	public String getDeliveryNote() {
		return deliveryNote;
	}

	public void setDeliveryNote(String deliveryNote) {
		this.deliveryNote = deliveryNote;
	}

	public String getDeliveryNotePrice() {
		return deliveryNotePrice;
	}

	public void setDeliveryNotePrice(String deliveryNotePrice) {
		this.deliveryNotePrice = deliveryNotePrice;
	}
	
	
	}
	

