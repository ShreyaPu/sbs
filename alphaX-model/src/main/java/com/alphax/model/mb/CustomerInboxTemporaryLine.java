package com.alphax.model.mb;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;
@ApiModel(description = "All details about the CustomerInboxTemporaryLine.")
public class CustomerInboxTemporaryLine {

		//Deviating customer number
		@DBTable(columnName ="KDNRECH", required = true) 
		private String abweichendeKdNr;
		//Tax ID number.
		@DBTable(columnName ="USTIDENT", required = true)
		private String ustIDNr;
		//Tax NR.
		@DBTable(columnName ="STEUERNR", required = true)
		private String steuerNr;
		//Print discount group
		@DBTable(columnName ="DRRG", required = true)
		private String druckRabattgruppe;
		//Print AW charge rate
		@DBTable(columnName ="DRAW", required = true) 
		private String drucAWVerrechnungssatz;
		//Start :Fifth Screen variables
		@DBTable(columnName ="INFTEXT", required = true) 
		private String temporarInfozeile; 
		@DBTable(columnName ="DRKZ", required = true)
		private String TemporarInfozeileChb;
		
		public CustomerInboxTemporaryLine() {
		}

		public String getAbweichendeKdNr() {
			return abweichendeKdNr;
		}

		public void setAbweichendeKdNr(String abweichendeKdNr) {
			this.abweichendeKdNr = abweichendeKdNr;
		}

		public String getUstIDNr() {
			return ustIDNr;
		}

		public void setUstIDNr(String ustIDNr) {
			this.ustIDNr = ustIDNr;
		}

		public String getSteuerNr() {
			return steuerNr;
		}

		public void setSteuerNr(String steuerNr) {
			this.steuerNr = steuerNr;
		}

		public String getDruckRabattgruppe() {
			return druckRabattgruppe;
		}

		public void setDruckRabattgruppe(String druckRabattgruppe) {
			this.druckRabattgruppe = druckRabattgruppe;
		}

		public String getDrucAWVerrechnungssatz() {
			return drucAWVerrechnungssatz;
		}

		public void setDrucAWVerrechnungssatz(String drucAWVerrechnungssatz) {
			this.drucAWVerrechnungssatz = drucAWVerrechnungssatz;
		}

		public String getTemporarInfozeile() {
			return temporarInfozeile;
		}

		public void setTemporarInfozeile(String temporarInfozeile) {
			this.temporarInfozeile = temporarInfozeile;
		}

		public String getTemporarInfozeileChb() {
			return TemporarInfozeileChb;
		}

		public void setTemporarInfozeileChb(String temporarInfozeileChb) {
			TemporarInfozeileChb = temporarInfozeileChb;
		} 
		
		
		
		
		
}
