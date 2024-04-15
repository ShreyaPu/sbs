package com.alphax.vo.mb;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All details about the CustomerInboxStandardLine DTO.")
public class CustomerInboxStandardLineDTO {

	// Fx_KBRIK1X - SATZART, INFTEXTP, DRKZ
	@ApiModelProperty(value = "PermanentInfoLineNo ( %% - DB Fx_KBRIK1X - SATZART).")
	private BigDecimal PermanentInfoLineNo;
	
	@ApiModelProperty(value = "PermanentInfozeile ( %% - DB Fx_KBRIK1X - INFTEXTP).")
	private String PermanentInfozeile;
	
	@ApiModelProperty(value = "PermanentInfozeileChb ( %% - DB Fx_KBRIK1X - DRKZ).")
	private String PermanentInfozeileChb;

	
	public CustomerInboxStandardLineDTO() {
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