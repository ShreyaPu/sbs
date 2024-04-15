package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All details about the CustomerDetail.")
public class CustomerDetail {

		//First Screen
		//Customer Number
		@DBTable(columnName ="KDNR1", required = true )
		private String kundenNummer;
		//Title key
		@DBTable(columnName ="BANR", required = true) 
		private String anredeSchlussel;
		//Salutation
		@DBTable(columnName ="ANRE", required = true)
		private String anrede;
		//First Name
		@DBTable(columnName ="NAME2", required = true)
		private String additionalNameInfo;
		//Surname
		@DBTable(columnName ="NAME", required = true)
		private String name;
		@DBTable(columnName ="SNAM", required = true)
		private String sortName;
		//branchen Bezeichnung = industry name
		@DBTable(columnName ="BRAN", required = true)
		private String branchenBez;
		//Road
		@DBTable(columnName ="STR", required = true)
		private String strasse;
		//Sort Road
		@DBTable(columnName ="SSTR", required = true)
		private String sortStrasse;
		//Country
		@DBTable(columnName ="LAND", required = true)
		private String land;
		//Postcode
		@DBTable(columnName ="PLZ", required = true)
		private String plz;
		//Place
		@DBTable(columnName ="ORT", required = true)
		private String ort;
		//post office box 
		@DBTable(columnName ="POSTF", required = true)
		private BigDecimal postfach;
		//The District 
		
		//Second Screen
		
		@DBTable(columnName ="BRSL1", required = true)  
		private String branchenschlPart1;
		@DBTable(columnName ="BRSL2", required = true)
		private String branchenschlPart2;
		@DBTable(columnName ="BRSL3", required = true)
		private String branchenschlPart3;
		//private client
		@DBTable(columnName ="UNSEL", required = true)
		private String privatKunde;
		@DBTable(columnName ="KZAQ", required = true)
		private String akquisitionskz;
		
		/*//DAG data transfer
		@DBTable(columnName ="KZDAGFZG", required = true)
		private BigDecimal dagDatentransfer;*/
		//Delivery number. b. customer
		@DBTable(columnName ="LIEFNRKD", required = true)
		private String liefNrBKunde;
		//Number of invoice copies
		@DBTable(columnName ="ANZR", required = true)
		private BigDecimal anzahlRechnungsexemplare;
		//commercial register
		@DBTable(columnName ="STNR", required = true)
		private String handelsregister;
		//vehicle customer
		@DBTable(columnName ="FZKD", required = true)
		private String fahrzeugkunde;
		
		//Internal customer
		@DBTable(columnName ="INTKD", required = true)
		private String internerKunde;
		
		@DBTable(columnName ="KMWSL", required = true)
		private BigDecimal mwStKz;
		//Workshop no.
		@DBTable(columnName ="WSTN", required = true) 
		private String werkstattNr;
		
		
		//customer group
		@DBTable(columnName ="KGRU", required = true) 
		private BigDecimal kundengruppe;
		//credit limit
		@DBTable(columnName ="KLIM", required = true)
		private BigDecimal kreditlimit;
		//credit
		@DBTable(columnName ="BONIT", required = true)
		private String bonitat;
		//payment
		@DBTable(columnName ="ZAHLZIEL", required = true)
		private BigDecimal zahlungsziel;
		//Surcharge / discount
		@DBTable(columnName ="KZSCHL", required = true)
		private BigDecimal zuAbschlag;
		//Discount counter
		@DBTable(columnName ="ZA", required = true)
		private String skontoTheke;
		@DBTable(columnName ="KRABKZ", required = true)
		private BigDecimal rabattkz;
		
		
		//Sixth Screen
		@DBTable(columnName ="ERECH", required = true) 
		private String elektronRechnung;
		@DBTable(columnName ="EREMAIL", required = true)
		private String erEmail;
		@DBTable(columnName ="ERDATEN", required = true)
		private String erDaten;
		@DBTable(columnName ="ERSIGNATUR", required = true)
		private String erSignatur; 
		@DBTable(columnName ="ERANLAGEN", required = true)
		private String erAnlagen; 
		
		
		
		//Foreth Screen
		/* dialogue ban */
		@DBTable(columnName ="BTREUSTOP", required = true) 
		private String dialogverbot;
		@DBTable(columnName ="DSETELEF", required = true)
		private String telefon;
		@DBTable(columnName ="CSISPERRE", required = true)
		private String KeineService_CSI_Befragung;
		@DBTable(columnName ="DSEFAX", required = true)
		private String fax;
		@DBTable(columnName ="DSESMS", required = true)
		private String sms;
		@DBTable(columnName ="DSEEMAIL", required = true)
		private String email;
		@DBTable(columnName ="DSEBELEGNR", required = true)
		private String beleg;
		@DBTable(columnName ="DSEHERKUNF", required = true)
		private String herkunft2;
		@DBTable(columnName ="KZDAGFZG", required = true)
		private BigDecimal dag_Fzg;
		
		/* Submission date*/
		@DBTable(columnName ="DSEERSTDAT", required = true)
		private BigDecimal abgabedatum;
		@DBTable(columnName ="DSEPOST", required = true)
		private String angekreuzt;  //Need to check column value
		/* Expiry Date YY */
		@DBTable(columnName ="DSEABLDAT", required = true) 
		private BigDecimal ablaufdatum;
		@DBTable(columnName ="NODAGDSE", required = true)
		private String keineDaimlerDSE;
		/*@DBTable(columnName ="")
		private String druckerId;*/ //Need to check column name
		
		public CustomerDetail() {
			
		}

		public String getKundenNummer() {
			return kundenNummer;
		}

		public void setKundenNummer(String kundenNummer) {
			this.kundenNummer = kundenNummer;
		}

		public String getAnredeSchlussel() {
			return anredeSchlussel;
		}

		public void setAnredeSchlussel(String anredeSchlussel) {
			this.anredeSchlussel = anredeSchlussel;
		}

		public String getAnrede() {
			return anrede;
		}

		public void setAnrede(String anrede) {
			this.anrede = anrede;
		}

		

		public String getAdditionalNameInfo() {
			return additionalNameInfo;
		}

		public void setAdditionalNameInfo(String additionalNameInfo) {
			this.additionalNameInfo = additionalNameInfo;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getSortName() {
			return sortName;
		}

		public void setSortName(String sortName) {
			this.sortName = sortName;
		}

		public String getBranchenBez() {
			return branchenBez;
		}

		public void setBranchenBez(String branchenBez) {
			this.branchenBez = branchenBez;
		}

		public String getStrasse() {
			return strasse;
		}

		public void setStrasse(String strasse) {
			this.strasse = strasse;
		}

		public String getSortStrasse() {
			return sortStrasse;
		}

		public void setSortStrasse(String sortStrasse) {
			this.sortStrasse = sortStrasse;
		}

		public String getLand() {
			return land;
		}

		public void setLand(String land) {
			this.land = land;
		}

		public String getPlz() {
			return plz;
		}

		public void setPlz(String plz) {
			this.plz = plz;
		}

		public String getOrt() {
			return ort;
		}

		public void setOrt(String ort) {
			this.ort = ort;
		}

		public BigDecimal getPostfach() {
			return postfach;
		}

		public void setPostfach(BigDecimal postfach) {
			this.postfach = postfach;
		}

		public String getBranchenschlPart1() {
			return branchenschlPart1;
		}

		public void setBranchenschlPart1(String branchenschlPart1) {
			this.branchenschlPart1 = branchenschlPart1;
		}

		public String getBranchenschlPart2() {
			return branchenschlPart2;
		}

		public void setBranchenschlPart2(String branchenschlPart2) {
			this.branchenschlPart2 = branchenschlPart2;
		}

		public String getBranchenschlPart3() {
			return branchenschlPart3;
		}

		public void setBranchenschlPart3(String branchenschlPart3) {
			this.branchenschlPart3 = branchenschlPart3;
		}

		public String getPrivatKunde() {
			return privatKunde;
		}

		public void setPrivatKunde(String privatKunde) {
			this.privatKunde = privatKunde;
		}

		public String getAkquisitionskz() {
			return akquisitionskz;
		}

		public void setAkquisitionskz(String akquisitionskz) {
			this.akquisitionskz = akquisitionskz;
		}

		public String getLiefNrBKunde() {
			return liefNrBKunde;
		}

		public void setLiefNrBKunde(String liefNrBKunde) {
			this.liefNrBKunde = liefNrBKunde;
		}

		public BigDecimal getAnzahlRechnungsexemplare() {
			return anzahlRechnungsexemplare;
		}

		public void setAnzahlRechnungsexemplare(BigDecimal anzahlRechnungsexemplare) {
			this.anzahlRechnungsexemplare = anzahlRechnungsexemplare;
		}

		public String getHandelsregister() {
			return handelsregister;
		}

		public void setHandelsregister(String handelsregister) {
			this.handelsregister = handelsregister;
		}

		public String getFahrzeugkunde() {
			return fahrzeugkunde;
		}

		public void setFahrzeugkunde(String fahrzeugkunde) {
			this.fahrzeugkunde = fahrzeugkunde;
		}

		public String getInternerKunde() {
			return internerKunde;
		}

		public void setInternerKunde(String internerKunde) {
			this.internerKunde = internerKunde;
		}

		public BigDecimal getMwStKz() {
			return mwStKz;
		}

		public void setMwStKz(BigDecimal mwStKz) {
			this.mwStKz = mwStKz;
		}

		public String getWerkstattNr() {
			return werkstattNr;
		}

		public void setWerkstattNr(String werkstattNr) {
			this.werkstattNr = werkstattNr;
		}

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

		public String getElektronRechnung() {
			return elektronRechnung;
		}

		public void setElektronRechnung(String elektronRechnung) {
			this.elektronRechnung = elektronRechnung;
		}

		public String getErEmail() {
			return erEmail;
		}

		public void setErEmail(String erEmail) {
			this.erEmail = erEmail;
		}

		public String getErDaten() {
			return erDaten;
		}

		public void setErDaten(String erDaten) {
			this.erDaten = erDaten;
		}

		public String getErSignatur() {
			return erSignatur;
		}

		public void setErSignatur(String erSignatur) {
			this.erSignatur = erSignatur;
		}

		public String getErAnlagen() {
			return erAnlagen;
		}

		public void setErAnlagen(String erAnlagen) {
			this.erAnlagen = erAnlagen;
		}

		public String getDialogverbot() {
			return dialogverbot;
		}

		public void setDialogverbot(String dialogverbot) {
			this.dialogverbot = dialogverbot;
		}

		public String getTelefon() {
			return telefon;
		}

		public void setTelefon(String telefon) {
			this.telefon = telefon;
		}

		public String getKeineService_CSI_Befragung() {
			return KeineService_CSI_Befragung;
		}

		public void setKeineService_CSI_Befragung(String keineService_CSI_Befragung) {
			KeineService_CSI_Befragung = keineService_CSI_Befragung;
		}

		public String getFax() {
			return fax;
		}

		public void setFax(String fax) {
			this.fax = fax;
		}

		public String getSms() {
			return sms;
		}

		public void setSms(String sms) {
			this.sms = sms;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getBeleg() {
			return beleg;
		}

		public void setBeleg(String beleg) {
			this.beleg = beleg;
		}

		public String getHerkunft2() {
			return herkunft2;
		}

		public void setHerkunft2(String herkunft2) {
			this.herkunft2 = herkunft2;
		}

		public BigDecimal getDag_Fzg() {
			return dag_Fzg;
		}

		public void setDag_Fzg(BigDecimal dag_Fzg) {
			this.dag_Fzg = dag_Fzg;
		}

		public BigDecimal getAbgabedatum() {
			return abgabedatum;
		}

		public void setAbgabedatum(BigDecimal abgabedatum) {
			this.abgabedatum = abgabedatum;
		}

		public String getAngekreuzt() {
			return angekreuzt;
		}

		public void setAngekreuzt(String angekreuzt) {
			this.angekreuzt = angekreuzt;
		}

		public BigDecimal getAblaufdatum() {
			return ablaufdatum;
		}

		public void setAblaufdatum(BigDecimal ablaufdatum) {
			this.ablaufdatum = ablaufdatum;
		}

		public String getKeineDaimlerDSE() {
			return keineDaimlerDSE;
		}

		public void setKeineDaimlerDSE(String keineDaimlerDSE) {
			this.keineDaimlerDSE = keineDaimlerDSE;
		}


		
		
		
		
		
}
