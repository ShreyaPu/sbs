package com.alphax.model.mb;

import com.alphax.common.util.DBTable;

public class CustomerUpdateAuthority {
	
	@DBTable(columnName ="LKZ", required = true)
	private String  kzLoeschung; // shortTerm decommissioning.
	@DBTable(columnName ="KZST", required = true)
	private String  kzStilllegung; // shortTerm decommissioning.deletion
	
	public CustomerUpdateAuthority() {
	}

	public String getKzLoeschung() {
		return kzLoeschung;
	}

	public void setKzLoeschung(String kzLoeschung) {
		this.kzLoeschung = kzLoeschung;
	}

	public String getKzStilllegung() {
		return kzStilllegung;
	}

	public void setKzStilllegung(String kzStilllegung) {
		this.kzStilllegung = kzStilllegung;
	}
	
	

}