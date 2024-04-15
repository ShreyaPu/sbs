package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;
@ApiModel(description = "All details about the CustomerDefaultData.")
public class CustomerDefaultData {

			//customer group
			@DBTable(columnName ="KUN_KDGRP", required = true) 
			private String kundengruppe;
			//credit limit
			@DBTable(columnName ="KUN_KLIMIT", required = true)
			private BigDecimal kreditlimit;
			//credit
			@DBTable(columnName ="KUN_BONIT", required = true)
			private String bonitat;
			//payment
			@DBTable(columnName ="KUN_ZAZIEL", required = true)
			private BigDecimal zahlungsziel;
			//Surcharge / discount
			@DBTable(columnName ="KUN_ZUABSL", required = true)
			private BigDecimal zuAbschlag;
			//Discount counter
			@DBTable(columnName ="KUN_SKONTE", required = true)
			private BigDecimal skontoTheke;
			@DBTable(columnName ="KUN_RABFLG", required = true)
			private BigDecimal rabattkz;
			
			public CustomerDefaultData() {
				super();
			}

			public String getKundengruppe() {
				return kundengruppe;
			}

			public void setKundengruppe(String kundengruppe) {
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

			public BigDecimal getSkontoTheke() {
				return skontoTheke;
			}

			public void setSkontoTheke(BigDecimal skontoTheke) {
				this.skontoTheke = skontoTheke;
			}

			public BigDecimal getRabattkz() {
				return rabattkz;
			}

			public void setRabattkz(BigDecimal rabattkz) {
				this.rabattkz = rabattkz;
			}

			
}
