package com.alphax.model.mb;

import com.alphax.common.util.DBTable;

public class AuditAssignDeliveryNotes {

	@DBTable(columnName ="DELIVERYNOTENR", required = true)
	private String deliveryNote;
	
	@DBTable(columnName ="PREIS", required = true)
	private String deliveryNoteprice;

	public String getDeliveryNote() {
		return deliveryNote;
	}

	public void setDeliveryNote(String deliveryNote) {
		this.deliveryNote = deliveryNote;
	}

	public String getDeliveryNoteprice() {
		return deliveryNoteprice;
	}

	public void setDeliveryNoteprice(String deliveryNoteprice) {
		this.deliveryNoteprice = deliveryNoteprice;
	}
	

}
