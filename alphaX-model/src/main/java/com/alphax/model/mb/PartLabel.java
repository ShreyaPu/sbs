package com.alphax.model.mb;

import com.alphax.common.util.DBTable;

public class PartLabel {

	@DBTable(columnName = "LABEL", required = true)
	private String label;
	
	@DBTable(columnName = "KAT", required = true)
	private String kat;
	
	@DBTable(columnName = "WERT", required = true)
	private String wert;
	
	@DBTable(columnName = "TEXT", required = true)
	private String labelHeadingValue;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getKat() {
		return kat;
	}

	public void setKat(String kat) {
		this.kat = kat;
	}

	public String getWert() {
		return wert;
	}

	public void setWert(String wert) {
		this.wert = wert;
	}

	public String getLabelHeadingValue() {
		return labelHeadingValue;
	}

	public void setLabelHeadingValue(String labelHeadingValue) {
		this.labelHeadingValue = labelHeadingValue;
	}
	
	
	
}
