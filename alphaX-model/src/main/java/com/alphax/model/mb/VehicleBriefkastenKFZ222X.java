package com.alphax.model.mb;


import com.alphax.common.util.DBTable;


public class VehicleBriefkastenKFZ222X {

	@DBTable(columnName ="FTEXT_T", required = true )
	private String briefkastentext;
	
	@DBTable(columnName ="FTEXT_1", required = true )
	private String briefkastentext1;
	
	@DBTable(columnName ="FTEXT_2", required = true )
	private String briefkastentext2;
	
	@DBTable(columnName ="FTEXT_3", required = true )
	private String briefkastentext3;
	
	@DBTable(columnName ="FTEXT_4", required = true )
	private String briefkastentext4;
	
	@DBTable(columnName ="FTEXT_5", required = true )
	private String briefkastentext5;
	
	@DBTable(columnName ="FTEXT_6", required = true )
	private String briefkastentext6;
	
	@DBTable(columnName ="DRUCKBK", required = true )
	private String DruckkennzeichenChb;

	public VehicleBriefkastenKFZ222X() {
	}

	public String getBriefkastentext() {
		return briefkastentext;
	}

	public void setBriefkastentext(String briefkastentext) {
		this.briefkastentext = briefkastentext;
	}

	public String getBriefkastentext1() {
		return briefkastentext1;
	}

	public void setBriefkastentext1(String briefkastentext1) {
		this.briefkastentext1 = briefkastentext1;
	}

	public String getBriefkastentext2() {
		return briefkastentext2;
	}

	public void setBriefkastentext2(String briefkastentext2) {
		this.briefkastentext2 = briefkastentext2;
	}

	public String getBriefkastentext3() {
		return briefkastentext3;
	}

	public void setBriefkastentext3(String briefkastentext3) {
		this.briefkastentext3 = briefkastentext3;
	}

	public String getBriefkastentext4() {
		return briefkastentext4;
	}

	public void setBriefkastentext4(String briefkastentext4) {
		this.briefkastentext4 = briefkastentext4;
	}

	public String getBriefkastentext5() {
		return briefkastentext5;
	}

	public void setBriefkastentext5(String briefkastentext5) {
		this.briefkastentext5 = briefkastentext5;
	}

	public String getBriefkastentext6() {
		return briefkastentext6;
	}

	public void setBriefkastentext6(String briefkastentext6) {
		this.briefkastentext6 = briefkastentext6;
	}

	public String getDruckkennzeichenChb() {
		return DruckkennzeichenChb;
	}

	public void setDruckkennzeichenChb(String druckkennzeichenChb) {
		DruckkennzeichenChb = druckkennzeichenChb;
	}
	
	
	
	
}
