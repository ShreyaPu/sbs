package com.alphax.model.mb;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;
@ApiModel(description = "All details about the CustomerLocation.")
public class CustomerLocation {

	@DBTable(columnName ="PLZ", required = true)
	private String plzPostf;
	@DBTable(columnName ="ORTSTEIL", required = true)
	private String ortsteil;
	
	public CustomerLocation() {
	}

	public String getPlzPostf() {
		return plzPostf;
	}

	public void setPlzPostf(String plzPostf) {
		this.plzPostf = plzPostf;
	}

	public String getOrtsteil() {
		return ortsteil;
	}

	public void setOrtsteil(String ortsteil) {
		this.ortsteil = ortsteil;
	}
	
	
	
}
