package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;
@ApiModel(description = "All details about the CustomerInboxStandardLine.")
public class CustomerInboxStandardLine {

	@DBTable(columnName ="SATZART", required = true)// Fx_KBRIK1X - SATZART, INFTEXTP, DRKZ
	private BigDecimal PermanentInfoLineNo;
	@DBTable(columnName ="INFTEXTP", required = true)
	private String PermanentInfozeile;
	@DBTable(columnName ="DRKZ", required = true)
	private String PermanentInfozeileChb;

	public CustomerInboxStandardLine() {
	}

	public BigDecimal getPermanentInfoLineNo() {
		return PermanentInfoLineNo;
	}

	public void setPermanentInfoLineNo(BigDecimal permanentInfoLineNo) {
		PermanentInfoLineNo = permanentInfoLineNo;
	}

	public String getPermanentInfozeile() {
		return PermanentInfozeile;
	}

	public void setPermanentInfozeile(String permanentInfozeile) {
		PermanentInfozeile = permanentInfozeile;
	}

	public String getPermanentInfozeileChb() {
		return PermanentInfozeileChb;
	}

	public void setPermanentInfozeileChb(String permanentInfozeileChb) {
		PermanentInfozeileChb = permanentInfozeileChb;
	}
	
	
	
}
