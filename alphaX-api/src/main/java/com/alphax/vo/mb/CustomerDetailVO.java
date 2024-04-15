package com.alphax.vo.mb;

import java.util.List;

import com.alphax.vo.mb.CustomerContactPersonsDTO;
import com.alphax.vo.mb.CustomerInboxStandardLineDTO;

public class CustomerDetailVO {

		private String kundenNummer;
		private String anredeSchlussel;
		private String anrede;
		private String voname;
		private String name;
		private String sortName;
		private String branchenBez;
		private String strasse;
		private String sortStrasse;
		private String land;
		private String plz;
		private String ort;
		private String plzPostf;
		private String postfach;
		private String ortsteil;
		private String additionalNameInfo;
		
		//Second Screen
		private String geburtstagDate;
		private String geburtstagMonth;
		private String geburtstagYear;
		private String branchenschlPart1;
		private String branchenschlPart2;
		private String branchenschlPart3;
		private boolean privatKunde;
		private String akquisitionskz;
		private String abweichendeKdNr;
		private boolean dagDatentransfer;
		private String liefNrBKunde;
		private String anzahlRechnungsexemplare;
		private String handelsregister;
		private boolean fahrzeugkunde;
		private String ustIDNr;
		private boolean internerKunde;
		private String steuerNr;
		private String mwStKz;
		private String werkstattNr;
		private boolean druckRabattgruppe;
		private boolean drucAWVerrechnungssatz;
		private String kundengruppe;
		private String kreditlimit;
		private String bonitat;
		private String zahlungsziel;
		private String zuAbschlag;
		private String skontoTheke;
		private String rabattkz;
		
		//Third Screen
		private String haupttelefonNummer;
		private String festnetzTelefonnummer;
		private String mobilfunknummer;
		private String faxNummer;
		private String webseite;
		private String emailAddress;
		
		//Sixth Screen
		private String elektronRechnung;
		private String erEmail;
		private String dlCode;
		private String verCode;
		private String beZeich;
		private String erDaten;
		private boolean erSignatur; 
		private boolean erAnlagen; 
		
		//Foreth Screen
		/* dialogue ban */
		private boolean dialogverbot2;
		private boolean telefonYes2; 
		private boolean telefonNo2;
		private boolean KeineService_CSI_Befragung2; 
		private boolean faxYes2;
		private boolean faxNo2;
		private boolean smsYes2;
		private boolean smsNo2;
		private boolean emailYes2;
		private boolean emailNo2;
		private String beleg2;
		private String herkunft2;
		private boolean dag_Fzg2;
		/* Submission date  dd*/
		private String abgabedatumDD2;
		/* Submission date  MM*/
		private String abgabedatumMM2;
		/* Submission date  YY*/
		private String abgabedatumYY2;
		private boolean angekreuzt2;
		private boolean nichtAngekreuzt2;
		/* Expiry Date dd */	
		private String ablaufdatumDD2;
		/* Expiry Date MM */	
		private String ablaufdatumMM2;
		/* Expiry Date YY */	
		private String ablaufdatumYY2;
		private boolean keineDaimlerDSE2;
		private String druckerId;
		

		//Start :Fifth Screen variables
		// Start :Fifth Screen variables
		private String temporarInfozeile;
		private String permanentInfozeile1;
		private String permanentInfozeile2;
		private String permanentInfozeile3;
		private String permanentInfozeile4;
		private String permanentInfozeile5;
		private String permanentInfozeile6;

		private boolean temporarInfozeileChb;
		private boolean permanentInfozeileChb1;
		private boolean permanentInfozeileChb2;
		private boolean permanentInfozeileChb3;
		private boolean permanentInfozeileChb4;
		private boolean permanentInfozeileChb5;
		private boolean permanentInfozeileChb6; 
		
		List<CustomerContactPersonsDTO> custContactPersons;
		List<CustomerInboxStandardLineDTO> customerInboxLine;

		public List<CustomerInboxStandardLineDTO> getCustomerInboxLine() {
			return customerInboxLine;
		}

		public void setCustomerInboxLine(List<CustomerInboxStandardLineDTO> customerInboxLine) {
			this.customerInboxLine = customerInboxLine;
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

		public String getVoname() {
			return voname;
		}

		public void setVoname(String voname) {
			this.voname = voname;
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

		public String getPlzPostf() {
			return plzPostf;
		}

		public void setPlzPostf(String plzPostf) {
			this.plzPostf = plzPostf;
		}

		public String getPostfach() {
			return postfach;
		}

		public void setPostfach(String postfach) {
			this.postfach = postfach;
		}

		public String getOrtsteil() {
			return ortsteil;
		}

		public void setOrtsteil(String ortsteil) {
			this.ortsteil = ortsteil;
		}

		public String getGeburtstagDate() {
			return geburtstagDate;
		}

		public void setGeburtstagDate(String geburtstagDate) {
			this.geburtstagDate = geburtstagDate;
		}

		public String getGeburtstagMonth() {
			return geburtstagMonth;
		}

		public void setGeburtstagMonth(String geburtstagMonth) {
			this.geburtstagMonth = geburtstagMonth;
		}

		public String getGeburtstagYear() {
			return geburtstagYear;
		}

		public void setGeburtstagYear(String geburtstagYear) {
			this.geburtstagYear = geburtstagYear;
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

		public boolean isPrivatKunde() {
			return privatKunde;
		}

		public void setPrivatKunde(boolean privatKunde) {
			this.privatKunde = privatKunde;
		}

		public String getAkquisitionskz() {
			return akquisitionskz;
		}

		public void setAkquisitionskz(String akquisitionskz) {
			this.akquisitionskz = akquisitionskz;
		}

		public String getAbweichendeKdNr() {
			return abweichendeKdNr;
		}

		public void setAbweichendeKdNr(String abweichendeKdNr) {
			this.abweichendeKdNr = abweichendeKdNr;
		}

		public boolean isDagDatentransfer() {
			return dagDatentransfer;
		}

		public void setDagDatentransfer(boolean dagDatentransfer) {
			this.dagDatentransfer = dagDatentransfer;
		}

		public String getLiefNrBKunde() {
			return liefNrBKunde;
		}

		public void setLiefNrBKunde(String liefNrBKunde) {
			this.liefNrBKunde = liefNrBKunde;
		}

		public String getAnzahlRechnungsexemplare() {
			return anzahlRechnungsexemplare;
		}

		public void setAnzahlRechnungsexemplare(String anzahlRechnungsexemplare) {
			this.anzahlRechnungsexemplare = anzahlRechnungsexemplare;
		}

		public String getHandelsregister() {
			return handelsregister;
		}

		public void setHandelsregister(String handelsregister) {
			this.handelsregister = handelsregister;
		}

		public boolean isFahrzeugkunde() {
			return fahrzeugkunde;
		}

		public void setFahrzeugkunde(boolean fahrzeugkunde) {
			this.fahrzeugkunde = fahrzeugkunde;
		}

		public String getUstIDNr() {
			return ustIDNr;
		}

		public void setUstIDNr(String ustIDNr) {
			this.ustIDNr = ustIDNr;
		}

		public boolean isInternerKunde() {
			return internerKunde;
		}

		public void setInternerKunde(boolean internerKunde) {
			this.internerKunde = internerKunde;
		}

		public String getSteuerNr() {
			return steuerNr;
		}

		public void setSteuerNr(String steuerNr) {
			this.steuerNr = steuerNr;
		}

		public String getMwStKz() {
			return mwStKz;
		}

		public void setMwStKz(String mwStKz) {
			this.mwStKz = mwStKz;
		}

		public String getWerkstattNr() {
			return werkstattNr;
		}

		public void setWerkstattNr(String werkstattNr) {
			this.werkstattNr = werkstattNr;
		}

		public boolean isDruckRabattgruppe() {
			return druckRabattgruppe;
		}

		public void setDruckRabattgruppe(boolean druckRabattgruppe) {
			this.druckRabattgruppe = druckRabattgruppe;
		}

		public boolean isDrucAWVerrechnungssatz() {
			return drucAWVerrechnungssatz;
		}

		public void setDrucAWVerrechnungssatz(boolean drucAWVerrechnungssatz) {
			this.drucAWVerrechnungssatz = drucAWVerrechnungssatz;
		}

		public String getKundengruppe() {
			return kundengruppe;
		}

		public void setKundengruppe(String kundengruppe) {
			this.kundengruppe = kundengruppe;
		}

		public String getKreditlimit() {
			return kreditlimit;
		}

		public void setKreditlimit(String kreditlimit) {
			this.kreditlimit = kreditlimit;
		}

		public String getBonitat() {
			return bonitat;
		}

		public void setBonitat(String bonitat) {
			this.bonitat = bonitat;
		}

		public String getZahlungsziel() {
			return zahlungsziel;
		}

		public void setZahlungsziel(String zahlungsziel) {
			this.zahlungsziel = zahlungsziel;
		}

		public String getZuAbschlag() {
			return zuAbschlag;
		}

		public void setZuAbschlag(String zuAbschlag) {
			this.zuAbschlag = zuAbschlag;
		}

		public String getSkontoTheke() {
			return skontoTheke;
		}

		public void setSkontoTheke(String skontoTheke) {
			this.skontoTheke = skontoTheke;
		}

		public String getRabattkz() {
			return rabattkz;
		}

		public void setRabattkz(String rabattkz) {
			this.rabattkz = rabattkz;
		}

		public String getHaupttelefonNummer() {
			return haupttelefonNummer;
		}

		public void setHaupttelefonNummer(String haupttelefonNummer) {
			this.haupttelefonNummer = haupttelefonNummer;
		}

		public String getFestnetzTelefonnummer() {
			return festnetzTelefonnummer;
		}

		public void setFestnetzTelefonnummer(String festnetzTelefonnummer) {
			this.festnetzTelefonnummer = festnetzTelefonnummer;
		}

		public String getMobilfunknummer() {
			return mobilfunknummer;
		}

		public void setMobilfunknummer(String mobilfunknummer) {
			this.mobilfunknummer = mobilfunknummer;
		}

		public String getFaxNummer() {
			return faxNummer;
		}

		public void setFaxNummer(String faxNummer) {
			this.faxNummer = faxNummer;
		}

		public String getWebseite() {
			return webseite;
		}

		public void setWebseite(String webseite) {
			this.webseite = webseite;
		}

		public String getEmailAddress() {
			return emailAddress;
		}

		public void setEmailAddress(String emailAddress) {
			this.emailAddress = emailAddress;
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

		public String getDlCode() {
			return dlCode;
		}

		public void setDlCode(String dlCode) {
			this.dlCode = dlCode;
		}

		public String getVerCode() {
			return verCode;
		}

		public void setVerCode(String verCode) {
			this.verCode = verCode;
		}

		public String getBeZeich() {
			return beZeich;
		}

		public void setBeZeich(String beZeich) {
			this.beZeich = beZeich;
		}

		public String getErDaten() {
			return erDaten;
		}

		public void setErDaten(String erDaten) {
			this.erDaten = erDaten;
		}

		public boolean isErSignatur() {
			return erSignatur;
		}

		public void setErSignatur(boolean erSignatur) {
			this.erSignatur = erSignatur;
		}

		public boolean isErAnlagen() {
			return erAnlagen;
		}

		public void setErAnlagen(boolean erAnlagen) {
			this.erAnlagen = erAnlagen;
		}

		public boolean isDialogverbot2() {
			return dialogverbot2;
		}

		public void setDialogverbot2(boolean dialogverbot2) {
			this.dialogverbot2 = dialogverbot2;
		}

		public boolean isTelefonYes2() {
			return telefonYes2;
		}

		public void setTelefonYes2(boolean telefonYes2) {
			this.telefonYes2 = telefonYes2;
		}

		public boolean isTelefonNo2() {
			return telefonNo2;
		}

		public void setTelefonNo2(boolean telefonNo2) {
			this.telefonNo2 = telefonNo2;
		}

		public boolean isKeineService_CSI_Befragung2() {
			return KeineService_CSI_Befragung2;
		}

		public void setKeineService_CSI_Befragung2(boolean keineService_CSI_Befragung2) {
			KeineService_CSI_Befragung2 = keineService_CSI_Befragung2;
		}

		public boolean isFaxYes2() {
			return faxYes2;
		}

		public void setFaxYes2(boolean faxYes2) {
			this.faxYes2 = faxYes2;
		}

		public boolean isFaxNo2() {
			return faxNo2;
		}

		public void setFaxNo2(boolean faxNo2) {
			this.faxNo2 = faxNo2;
		}

		public boolean isSmsYes2() {
			return smsYes2;
		}

		public void setSmsYes2(boolean smsYes2) {
			this.smsYes2 = smsYes2;
		}

		public boolean isSmsNo2() {
			return smsNo2;
		}

		public void setSmsNo2(boolean smsNo2) {
			this.smsNo2 = smsNo2;
		}

		public boolean isEmailYes2() {
			return emailYes2;
		}

		public void setEmailYes2(boolean emailYes2) {
			this.emailYes2 = emailYes2;
		}

		public boolean isEmailNo2() {
			return emailNo2;
		}

		public void setEmailNo2(boolean emailNo2) {
			this.emailNo2 = emailNo2;
		}

		public String getBeleg2() {
			return beleg2;
		}

		public void setBeleg2(String beleg2) {
			this.beleg2 = beleg2;
		}

		public String getHerkunft2() {
			return herkunft2;
		}

		public void setHerkunft2(String herkunft2) {
			this.herkunft2 = herkunft2;
		}

		public boolean isDag_Fzg2() {
			return dag_Fzg2;
		}

		public void setDag_Fzg2(boolean dag_Fzg2) {
			this.dag_Fzg2 = dag_Fzg2;
		}

		public String getAbgabedatumDD2() {
			return abgabedatumDD2;
		}

		public void setAbgabedatumDD2(String abgabedatumDD2) {
			this.abgabedatumDD2 = abgabedatumDD2;
		}

		public String getAbgabedatumMM2() {
			return abgabedatumMM2;
		}

		public void setAbgabedatumMM2(String abgabedatumMM2) {
			this.abgabedatumMM2 = abgabedatumMM2;
		}

		public String getAbgabedatumYY2() {
			return abgabedatumYY2;
		}

		public void setAbgabedatumYY2(String abgabedatumYY2) {
			this.abgabedatumYY2 = abgabedatumYY2;
		}

		public boolean isAngekreuzt2() {
			return angekreuzt2;
		}

		public void setAngekreuzt2(boolean angekreuzt2) {
			this.angekreuzt2 = angekreuzt2;
		}

		public boolean isNichtAngekreuzt2() {
			return nichtAngekreuzt2;
		}

		public void setNichtAngekreuzt2(boolean nichtAngekreuzt2) {
			this.nichtAngekreuzt2 = nichtAngekreuzt2;
		}

		public String getAblaufdatumDD2() {
			return ablaufdatumDD2;
		}

		public void setAblaufdatumDD2(String ablaufdatumDD2) {
			this.ablaufdatumDD2 = ablaufdatumDD2;
		}

		public String getAblaufdatumMM2() {
			return ablaufdatumMM2;
		}

		public void setAblaufdatumMM2(String ablaufdatumMM2) {
			this.ablaufdatumMM2 = ablaufdatumMM2;
		}

		public String getAblaufdatumYY2() {
			return ablaufdatumYY2;
		}

		public void setAblaufdatumYY2(String ablaufdatumYY2) {
			this.ablaufdatumYY2 = ablaufdatumYY2;
		}

		public boolean isKeineDaimlerDSE2() {
			return keineDaimlerDSE2;
		}

		public void setKeineDaimlerDSE2(boolean keineDaimlerDSE2) {
			this.keineDaimlerDSE2 = keineDaimlerDSE2;
		}

		public String getDruckerId() {
			return druckerId;
		}

		public void setDruckerId(String druckerId) {
			this.druckerId = druckerId;
		}

		public String getTemporarInfozeile() {
			return temporarInfozeile;
		}

		public void setTemporarInfozeile(String temporarInfozeile) {
			this.temporarInfozeile = temporarInfozeile;
		}

		public String getPermanentInfozeile1() {
			return permanentInfozeile1;
		}

		public void setPermanentInfozeile1(String permanentInfozeile1) {
			this.permanentInfozeile1 = permanentInfozeile1;
		}

		public String getPermanentInfozeile2() {
			return permanentInfozeile2;
		}

		public void setPermanentInfozeile2(String permanentInfozeile2) {
			this.permanentInfozeile2 = permanentInfozeile2;
		}

		public String getPermanentInfozeile3() {
			return permanentInfozeile3;
		}

		public void setPermanentInfozeile3(String permanentInfozeile3) {
			this.permanentInfozeile3 = permanentInfozeile3;
		}

		public String getPermanentInfozeile4() {
			return permanentInfozeile4;
		}

		public void setPermanentInfozeile4(String permanentInfozeile4) {
			this.permanentInfozeile4 = permanentInfozeile4;
		}

		public String getPermanentInfozeile5() {
			return permanentInfozeile5;
		}

		public void setPermanentInfozeile5(String permanentInfozeile5) {
			this.permanentInfozeile5 = permanentInfozeile5;
		}

		public String getPermanentInfozeile6() {
			return permanentInfozeile6;
		}

		public void setPermanentInfozeile6(String permanentInfozeile6) {
			this.permanentInfozeile6 = permanentInfozeile6;
		}

		public boolean isTemporarInfozeileChb() {
			return temporarInfozeileChb;
		}

		public void setTemporarInfozeileChb(boolean temporarInfozeileChb) {
			this.temporarInfozeileChb = temporarInfozeileChb;
		}

		public boolean isPermanentInfozeileChb1() {
			return permanentInfozeileChb1;
		}

		public void setPermanentInfozeileChb1(boolean permanentInfozeileChb1) {
			this.permanentInfozeileChb1 = permanentInfozeileChb1;
		}

		public boolean isPermanentInfozeileChb2() {
			return permanentInfozeileChb2;
		}

		public void setPermanentInfozeileChb2(boolean permanentInfozeileChb2) {
			this.permanentInfozeileChb2 = permanentInfozeileChb2;
		}

		public boolean isPermanentInfozeileChb3() {
			return permanentInfozeileChb3;
		}

		public void setPermanentInfozeileChb3(boolean permanentInfozeileChb3) {
			this.permanentInfozeileChb3 = permanentInfozeileChb3;
		}

		public boolean isPermanentInfozeileChb4() {
			return permanentInfozeileChb4;
		}

		public void setPermanentInfozeileChb4(boolean permanentInfozeileChb4) {
			this.permanentInfozeileChb4 = permanentInfozeileChb4;
		}

		public boolean isPermanentInfozeileChb5() {
			return permanentInfozeileChb5;
		}

		public void setPermanentInfozeileChb5(boolean permanentInfozeileChb5) {
			this.permanentInfozeileChb5 = permanentInfozeileChb5;
		}

		public boolean isPermanentInfozeileChb6() {
			return permanentInfozeileChb6;
		}

		public void setPermanentInfozeileChb6(boolean permanentInfozeileChb6) {
			this.permanentInfozeileChb6 = permanentInfozeileChb6;
		}

		public List<CustomerContactPersonsDTO> getCustContactPersons() {
			return custContactPersons;
		}

		public void setCustContactPersons(List<CustomerContactPersonsDTO> custContactPersons) {
			this.custContactPersons = custContactPersons;
		}

		public String getAdditionalNameInfo() {
			return additionalNameInfo;
		}

		public void setAdditionalNameInfo(String additionalNameInfo) {
			this.additionalNameInfo = additionalNameInfo;
		}

		
		
		
		
		
		
}
