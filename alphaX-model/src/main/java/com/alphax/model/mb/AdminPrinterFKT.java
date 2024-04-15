package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description="All fields about admin Printer functionalities")
public class AdminPrinterFKT {
	
	@DBTable(columnName ="ID", required = true)
	private String printerFktId;
	
	@DBTable(columnName ="PRINTER_ID", required = true)
	private String printerId;
	
	@DBTable(columnName ="FKT_ID", required = true)
	private String fktId;
	
	@DBTable(columnName ="HAS_FKT", required = true)
	private BigDecimal hasActiveFKT;
	
	@DBTable(columnName ="FKT", required = true)
	private String fktValue;

	public String getFktValue() {
		return fktValue;
	}

	public void setFktValue(String fktValue) {
		this.fktValue = fktValue;
	}

	public BigDecimal getHasActiveFKT() {
		return hasActiveFKT;
	}

	public void setHasActiveFKT(BigDecimal hasActiveFKT) {
		this.hasActiveFKT = hasActiveFKT;
	}

	public String getPrinterFktId() {
		return printerFktId;
	}

	public void setPrinterFktId(String printerFktId) {
		this.printerFktId = printerFktId;
	}

	public String getPrinterId() {
		return printerId;
	}

	public void setPrinterId(String printerId) {
		this.printerId = printerId;
	}

	public String getFktId() {
		return fktId;
	}

	public void setFktId(String fktId) {
		this.fktId = fktId;
	}

	
	

}
