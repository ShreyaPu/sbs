package com.alphax.vo.mb;

import java.math.BigDecimal;

import javax.validation.constraints.Size;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "This Object about customer default data")
public class CustomerDefaultDataVO {
	
	@ApiModelProperty(value = "Kundengruppe- DB PKF_KUNDEF - KUN_KDGRP")
	@Size(max=2)
	private BigDecimal kundengruppe;
	
	@ApiModelProperty(value = "Kreditlimit - DB PKF_KUNDEF - KUN_KLIMIT")
	@Size(max=9)
	private BigDecimal kreditlimit;

	@ApiModelProperty(value = "Bonität- DB PKF_KUNDEF - KUN_BONIT")
	@Size(max=1)
	private String bonitat;

	@ApiModelProperty(value = "Zahlungsziel- DB PKF_KUNDEF - KUN_ZAZIEL")
	@Size(max=2)
	private BigDecimal zahlungsziel;

	@ApiModelProperty(value = "Zu- / Abschlag - DB PKF_KUNDEF - KUN_ZUABSL")
	@Size(max=1)
	private BigDecimal zuAbschlag;

	@ApiModelProperty(value = "Skonto-Theke - DB PKF_KUNDEF - KUN_SKONTE")
	@Size(max=1)
	private String skontoTheke;

	@ApiModelProperty(value = "Rabattkz. - DB PKF_KUNDEF - KUN_RABFLG")
	@Size(max=1)
	private BigDecimal rabattkz;
	
	public BigDecimal getKundengruppe() {
		return kundengruppe;
	}
	public void setKundengruppe(BigDecimal kundengruppe) {
		this.kundengruppe = kundengruppe;
	}
	public BigDecimal getKreditlimit() {
		return kreditlimit;
	}
	public void setKreditlimit(BigDecimal kreditlimit) {
		this.kreditlimit = kreditlimit;
	}
	public String getBonitat() {
		return bonitat;
	}
	public void setBonitat(String bonitat) {
		this.bonitat = bonitat;
	}
	public BigDecimal getZahlungsziel() {
		return zahlungsziel;
	}
	public void setZahlungsziel(BigDecimal zahlungsziel) {
		this.zahlungsziel = zahlungsziel;
	}
	public BigDecimal getZuAbschlag() {
		return zuAbschlag;
	}
	public void setZuAbschlag(BigDecimal zuAbschlag) {
		this.zuAbschlag = zuAbschlag;
	}
	public String getSkontoTheke() {
		return skontoTheke;
	}
	public void setSkontoTheke(String skontoTheke) {
		this.skontoTheke = skontoTheke;
	}
	public BigDecimal getRabattkz() {
		return rabattkz;
	}
	public void setRabattkz(BigDecimal rabattkz) {
		this.rabattkz = rabattkz;
	}
	
	
}
