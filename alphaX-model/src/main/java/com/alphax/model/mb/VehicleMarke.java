package com.alphax.model.mb;

import com.alphax.common.util.DBTable;

public class VehicleMarke {
	
	/*worldManufacturerCode = WHC*/
	@DBTable(columnName ="WFM_WHC", required = true )
	private String worldManufacturerCode;
	
	/*FIN Bereich */
	@DBTable(columnName ="WFM_FINBER", required = true )
	private String finArea;
	
	/*Fahrzeugmarke */
	@DBTable(columnName ="WFM_MARKE", required = true )
	private String marke;

	public VehicleMarke() {
	}

	public String getWorldManufacturerCode() {
		return worldManufacturerCode;
	}

	public void setWorldManufacturerCode(String worldManufacturerCode) {
		this.worldManufacturerCode = worldManufacturerCode;
	}

	public String getFinArea() {
		return finArea;
	}

	public void setFinArea(String finArea) {
		this.finArea = finArea;
	}

	public String getMarke() {
		return marke;
	}

	public void setMarke(String marke) {
		this.marke = marke;
	}

	
	

}
