package com.alphax.vo.mb;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All details about the Customer.")
public class Customer extends BaseModel {

	// Start :First Screen variables
	private String function = "00";
	// About
	private String ueber = "";
	// Header1
	private String kopf1 = "";
	// Header2
	private String kopf2 = "";
	private String tabcontrol = "";
	private String modtxt = "";
	// Mode
	private String modus = "";
	// Title key
	private String anredeSchlussel;
	// Salutation
	private String anrede;
	// First Name
	private String voname;
	// Surname
	private String name;
	private String sortName = "";
	// branchen Bezeichnung = industry name
	private String branchenBez;
	// Road
	private String strasse;
	// Sort Road
	private String sortStrasse = "";
	// Country
	private String land;
	// Postcode
	private String plz;
	// Place
	private String ort;
	//
	private String plzPostf = "";
	// post office box
	private String postfach;
	// The District
	private String ortsteil = "";
	// Note
	private String hinweis = "";
	// Stroke
	private String strich = "";
	// Error
	private String fehler = "";
	// Tooltip
	private String tooltip = "";
	// Picture Act
	private String bildakt = "";
	// Image Post
	private String bildpost = "";
	// Phone Image
	private String bildtelef = "";
	// Email Image
	private String bildemail = "";
	//
	private String bildeco = "";
	// filler is just blank space
	private String filler = "";
	//
	private String errorCode = "";

	private List<String> errorList = new ArrayList<String>();
	// Customer Number
	private String kundenNummer;
	// Birth Date
	private String geburtstag = "";
	
	private String additionalNameInfo;

	// End :First Screen variables

	// Start :Second Screen variables
	// Birthday Date Month Year
	private String geburtstagDate;
	private String geburtstagMonth;
	private String geburtstagYear;
	//
	private String branchenschlPart1;
	private String branchenschlPart2;
	private String branchenschlPart3;
	// private client
	private boolean privatKunde;
	//
	private String akquisitionskz;
	// Deviating customer number
	private String abweichendeKdNr;
	// DAG data transfer
	private boolean dagDatentransfer;
	// Delivery number. b. customer
	private String liefNrBKunde;
	// Number of invoice copies
	private String anzahlRechnungsexemplare;
	// commercial register
	private String handelsregister;
	// vehicle customer
	private boolean fahrzeugkunde;
	// Tax ID number.
	private String ustIDNr;
	// Internal customer
	private boolean internerKunde;
	// Tax NR.
	private String steuerNr;
	//
	private String mwStKz;
	// Workshop no.
	private String werkstattNr;
	// Print discount group
	private boolean druckRabattgruppe;
	// Print AW charge rate
	private boolean drucAWVerrechnungssatz;
	// customer group
	private String kundengruppe;
	// credit limit
	private String kreditlimit;
	// credit
	private String bonitat;
	// payment
	private String zahlungsziel;
	// Surcharge / discount
	private String zuAbschlag;
	// Discount counter
	private String skontoTheke;
	//
	private String rabattkz;

	private String screenTwoFiller;
	private String screenTwoErrorCode;
	private String screenFourErrorCode;
	private String screenFiveErrorCode;
	private String screenSixErrorCode;

	// End :Second Screen variables

	// Start :Third Screen variables

	// main phone number
	private String haupttelefonNummer;
	// PHONE NUMBER
	private String telefonNR = "";
	// landline phone number
	private String festnetzTelefonnummer;
	// Phone number landline
	private String mobilfunknummer;
	private String faxNummer;
	private String webseite;
	private String emailAddress;

	private String screenThreeFiller;
	private String screenThreeErrorCode;

	// End :Third Screen variables

	// Start :Fourth Screen variables

	private boolean keineDaimlerDSE1;
	private String dabgDate;
	private String dabgMonth;
	private String dabgYear;
	private boolean telefonYes1;
	private boolean telefonNo1;

	/* opt_in submitted */
	private String opt_in_Abgegeben;
	private boolean faxYes1;
	private boolean faxNo1;

	/* dialogue ban */
	private boolean dialogverbot1;
	private boolean smsYes1;
	private boolean smsNo1;
	private boolean keineService_CSI_Befragung1;
	private boolean emailYes1;
	private boolean emailNo1;
	private String beleg1;
	private String herkunft1;
	private boolean dag_Fzg1;

	/* Submission date dd */
	private String abgabedatumDD1;

	/* Submission date mm */
	private String abgabedatumMM1;

	/* Submission date YY */
	private String abgabedatumYY1;

	/* checked */
	private boolean angekreuzt1;

	/* Not checked */
	private boolean nichtAngekreuzt1;

	/* Expiry Date dd */
	private String ablaufdatumDD1;

	/* Expiry Date MM */
	private String ablaufdatumMM1;

	/* Expiry Date YY */
	private String ablaufdatumYY1;

	/* dialogue ban */
	private boolean dialogverbot2;

	private boolean telefonYes2;
	private boolean telefonNo2;

	private boolean keineService_CSI_Befragung2;
	private boolean faxYes2;
	private boolean faxNo2;
	private boolean smsYes2;
	private boolean smsNo2;
	private boolean emailYes2;
	private boolean emailNo2;
	private String beleg2;
	private String herkunft2;
	private boolean dag_Fzg2;

	/* Submission date dd */
	private String abgabedatumDD2;
	/* Submission date MM */
	private String abgabedatumMM2;
	/* Submission date YY */
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

	// End :Fourth Screen variables

	// Start :Sixth Screen variables

	private String elektronRechnung;
	private String erEmail;
	private String dlCode;
	private String verCode;
	private String beZeich;
	private String erDaten;
	private boolean erSignatur;
	private boolean erAnlagen;

	// End :Sixth Screen variables

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
	// End :Fifth Screen variables

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public String getUeber() {
		return ueber;
	}

	public void setUeber(String ueber) {
		this.ueber = ueber;
	}

	public String getKopf1() {
		return kopf1;
	}

	public void setKopf1(String kopf1) {
		this.kopf1 = kopf1;
	}

	public String getKopf2() {
		return kopf2;
	}

	public void setKopf2(String kopf2) {
		this.kopf2 = kopf2;
	}

	public String getTabcontrol() {
		return tabcontrol;
	}

	public void setTabcontrol(String tabcontrol) {
		this.tabcontrol = tabcontrol;
	}

	public String getModtxt() {
		return modtxt;
	}

	public void setModtxt(String modtxt) {
		this.modtxt = modtxt;
	}

	public String getModus() {
		return modus;
	}

	public void setModus(String modus) {
		this.modus = modus;
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

	public String getHinweis() {
		return hinweis;
	}

	public void setHinweis(String hinweis) {
		this.hinweis = hinweis;
	}

	public String getStrich() {
		return strich;
	}

	public void setStrich(String strich) {
		this.strich = strich;
	}

	public String getFehler() {
		return fehler;
	}

	public void setFehler(String fehler) {
		this.fehler = fehler;
	}

	public String getTooltip() {
		return tooltip;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	public String getBildakt() {
		return bildakt;
	}

	public void setBildakt(String bildakt) {
		this.bildakt = bildakt;
	}

	public String getBildpost() {
		return bildpost;
	}

	public void setBildpost(String bildpost) {
		this.bildpost = bildpost;
	}

	public String getBildtelef() {
		return bildtelef;
	}

	public void setBildtelef(String bildtelef) {
		this.bildtelef = bildtelef;
	}

	public String getBildemail() {
		return bildemail;
	}

	public void setBildemail(String bildemail) {
		this.bildemail = bildemail;
	}

	public String getBildeco() {
		return bildeco;
	}

	public void setBildeco(String bildeco) {
		this.bildeco = bildeco;
	}

	public String getFiller() {
		return filler;
	}

	public void setFiller(String filler) {
		this.filler = filler;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public List<String> getErrorList() {
		return errorList;
	}

	public void setErrorList(List<String> errorList) {
		this.errorList = errorList;
	}

	public String getGeburtstag() {
		return geburtstag;
	}

	public void setGeburtstag(String geburtstag) {
		this.geburtstag = geburtstag;
	}

	public String getWebseite() {
		return webseite;
	}

	public void setWebseite(String webseite) {
		this.webseite = webseite;
	}

	public String getFestnetzTelefonnummer() {
		return festnetzTelefonnummer;
	}

	public void setFestnetzTelefonnummer(String festnetzTelefonnummer) {
		this.festnetzTelefonnummer = festnetzTelefonnummer;
	}

	public String getFaxNummer() {
		return faxNummer;
	}

	public void setFaxNummer(String faxNummer) {
		this.faxNummer = faxNummer;
	}

	public String getKundenNummer() {
		return kundenNummer;
	}

	public void setKundenNummer(String kundenNummer) {
		this.kundenNummer = kundenNummer;
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

	public String getScreenTwoFiller() {
		return screenTwoFiller;
	}

	public void setScreenTwoFiller(String screenTwoFiller) {
		this.screenTwoFiller = screenTwoFiller;
	}

	public String getScreenTwoErrorCode() {
		return screenTwoErrorCode;
	}

	public void setScreenTwoErrorCode(String screenTwoErrorCode) {
		this.screenTwoErrorCode = screenTwoErrorCode;
	}

	public boolean isPrivatKunde() {
		return privatKunde;
	}

	public void setPrivatKunde(boolean privatKunde) {
		this.privatKunde = privatKunde;
	}

	public boolean isDagDatentransfer() {
		return dagDatentransfer;
	}

	public void setDagDatentransfer(boolean dagDatentransfer) {
		this.dagDatentransfer = dagDatentransfer;
	}

	public boolean isFahrzeugkunde() {
		return fahrzeugkunde;
	}

	public void setFahrzeugkunde(boolean fahrzeugkunde) {
		this.fahrzeugkunde = fahrzeugkunde;
	}

	public boolean isInternerKunde() {
		return internerKunde;
	}

	public void setInternerKunde(boolean internerKunde) {
		this.internerKunde = internerKunde;
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

	public String getHaupttelefonNummer() {
		return haupttelefonNummer;
	}

	public void setHaupttelefonNummer(String haupttelefonNummer) {
		this.haupttelefonNummer = haupttelefonNummer;
	}

	public String getTelefonNR() {
		return telefonNR;
	}

	public void setTelefonNR(String telefonNR) {
		this.telefonNR = telefonNR;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getMobilfunknummer() {
		return mobilfunknummer;
	}

	public void setMobilfunknummer(String mobilfunknummer) {
		this.mobilfunknummer = mobilfunknummer;
	}

	public String getScreenThreeFiller() {
		return screenThreeFiller;
	}

	public void setScreenThreeFiller(String screenThreeFiller) {
		this.screenThreeFiller = screenThreeFiller;
	}

	public String getScreenThreeErrorCode() {
		return screenThreeErrorCode;
	}

	public void setScreenThreeErrorCode(String screenThreeErrorCode) {
		this.screenThreeErrorCode = screenThreeErrorCode;
	}

	
	/**
	 * @return the dabgDate
	 */
	public String getDabgDate() {
		return dabgDate;
	}

	/**
	 * @param dabgDate the dabgDate to set
	 */
	public void setDabgDate(String dabgDate) {
		this.dabgDate = dabgDate;
	}

	/**
	 * @return the dabgMonth
	 */
	public String getDabgMonth() {
		return dabgMonth;
	}

	/**
	 * @param dabgMonth the dabgMonth to set
	 */
	public void setDabgMonth(String dabgMonth) {
		this.dabgMonth = dabgMonth;
	}

	/**
	 * @return the dabgYear
	 */
	public String getDabgYear() {
		return dabgYear;
	}

	/**
	 * @param dabgYear the dabgYear to set
	 */
	public void setDabgYear(String dabgYear) {
		this.dabgYear = dabgYear;
	}

	/**
	 * @return the opt_in_Abgegeben
	 */
	public String getOpt_in_Abgegeben() {
		return opt_in_Abgegeben;
	}

	/**
	 * @param opt_in_Abgegeben the opt_in_Abgegeben to set
	 */
	public void setOpt_in_Abgegeben(String opt_in_Abgegeben) {
		this.opt_in_Abgegeben = opt_in_Abgegeben;
	}

	/**
	 * @return the beleg1
	 */
	public String getBeleg1() {
		return beleg1;
	}

	/**
	 * @param beleg1 the beleg1 to set
	 */
	public void setBeleg1(String beleg1) {
		this.beleg1 = beleg1;
	}

	/**
	 * @return the herkunft1
	 */
	public String getHerkunft1() {
		return herkunft1;
	}

	/**
	 * @param herkunft1 the herkunft1 to set
	 */
	public void setHerkunft1(String herkunft1) {
		this.herkunft1 = herkunft1;
	}

	/**
	 * @return the abgabedatumDD1
	 */
	public String getAbgabedatumDD1() {
		return abgabedatumDD1;
	}

	/**
	 * @param abgabedatumDD1 the abgabedatumDD1 to set
	 */
	public void setAbgabedatumDD1(String abgabedatumDD1) {
		this.abgabedatumDD1 = abgabedatumDD1;
	}

	/**
	 * @return the abgabedatumMM1
	 */
	public String getAbgabedatumMM1() {
		return abgabedatumMM1;
	}

	/**
	 * @param abgabedatumMM1 the abgabedatumMM1 to set
	 */
	public void setAbgabedatumMM1(String abgabedatumMM1) {
		this.abgabedatumMM1 = abgabedatumMM1;
	}

	/**
	 * @return the abgabedatumYY1
	 */
	public String getAbgabedatumYY1() {
		return abgabedatumYY1;
	}

	/**
	 * @param abgabedatumYY1 the abgabedatumYY1 to set
	 */
	public void setAbgabedatumYY1(String abgabedatumYY1) {
		this.abgabedatumYY1 = abgabedatumYY1;
	}

	/**
	 * @return the ablaufdatumDD1
	 */
	public String getAblaufdatumDD1() {
		return ablaufdatumDD1;
	}

	/**
	 * @param ablaufdatumDD1 the ablaufdatumDD1 to set
	 */
	public void setAblaufdatumDD1(String ablaufdatumDD1) {
		this.ablaufdatumDD1 = ablaufdatumDD1;
	}

	/**
	 * @return the ablaufdatumMM1
	 */
	public String getAblaufdatumMM1() {
		return ablaufdatumMM1;
	}

	/**
	 * @param ablaufdatumMM1 the ablaufdatumMM1 to set
	 */
	public void setAblaufdatumMM1(String ablaufdatumMM1) {
		this.ablaufdatumMM1 = ablaufdatumMM1;
	}

	/**
	 * @return the ablaufdatumYY1
	 */
	public String getAblaufdatumYY1() {
		return ablaufdatumYY1;
	}

	/**
	 * @param ablaufdatumYY1 the ablaufdatumYY1 to set
	 */
	public void setAblaufdatumYY1(String ablaufdatumYY1) {
		this.ablaufdatumYY1 = ablaufdatumYY1;
	}

	/**
	 * @return the beleg2
	 */
	public String getBeleg2() {
		return beleg2;
	}

	/**
	 * @param beleg2 the beleg2 to set
	 */
	public void setBeleg2(String beleg2) {
		this.beleg2 = beleg2;
	}

	/**
	 * @return the herkunft2
	 */
	public String getHerkunft2() {
		return herkunft2;
	}

	/**
	 * @param herkunft2 the herkunft2 to set
	 */
	public void setHerkunft2(String herkunft2) {
		this.herkunft2 = herkunft2;
	}

	/**
	 * @return the abgabedatumDD2
	 */
	public String getAbgabedatumDD2() {
		return abgabedatumDD2;
	}

	/**
	 * @param abgabedatumDD2 the abgabedatumDD2 to set
	 */
	public void setAbgabedatumDD2(String abgabedatumDD2) {
		this.abgabedatumDD2 = abgabedatumDD2;
	}

	/**
	 * @return the abgabedatumMM2
	 */
	public String getAbgabedatumMM2() {
		return abgabedatumMM2;
	}

	/**
	 * @param abgabedatumMM2 the abgabedatumMM2 to set
	 */
	public void setAbgabedatumMM2(String abgabedatumMM2) {
		this.abgabedatumMM2 = abgabedatumMM2;
	}

	/**
	 * @return the abgabedatumYY2
	 */
	public String getAbgabedatumYY2() {
		return abgabedatumYY2;
	}

	/**
	 * @param abgabedatumYY2 the abgabedatumYY2 to set
	 */
	public void setAbgabedatumYY2(String abgabedatumYY2) {
		this.abgabedatumYY2 = abgabedatumYY2;
	}

	/**
	 * @return the ablaufdatumDD2
	 */
	public String getAblaufdatumDD2() {
		return ablaufdatumDD2;
	}

	/**
	 * @param ablaufdatumDD2 the ablaufdatumDD2 to set
	 */
	public void setAblaufdatumDD2(String ablaufdatumDD2) {
		this.ablaufdatumDD2 = ablaufdatumDD2;
	}

	/**
	 * @return the ablaufdatumMM2
	 */
	public String getAblaufdatumMM2() {
		return ablaufdatumMM2;
	}

	/**
	 * @param ablaufdatumMM2 the ablaufdatumMM2 to set
	 */
	public void setAblaufdatumMM2(String ablaufdatumMM2) {
		this.ablaufdatumMM2 = ablaufdatumMM2;
	}

	/**
	 * @return the ablaufdatumYY2
	 */
	public String getAblaufdatumYY2() {
		return ablaufdatumYY2;
	}

	/**
	 * @param ablaufdatumYY2 the ablaufdatumYY2 to set
	 */
	public void setAblaufdatumYY2(String ablaufdatumYY2) {
		this.ablaufdatumYY2 = ablaufdatumYY2;
	}

	/**
	 * @return the druckerId
	 */
	public String getDruckerId() {
		return druckerId;
	}

	/**
	 * @param druckerId the druckerId to set
	 */
	public void setDruckerId(String druckerId) {
		this.druckerId = druckerId;
	}

	/**
	 * @return the screenFourErrorCode
	 */
	public String getScreenFourErrorCode() {
		return screenFourErrorCode;
	}

	/**
	 * @param screenFourErrorCode the screenFourErrorCode to set
	 */
	public void setScreenFourErrorCode(String screenFourErrorCode) {
		this.screenFourErrorCode = screenFourErrorCode;
	}

	/**
	 * @return the elektronRechnung
	 */
	public String getElektronRechnung() {
		return elektronRechnung;
	}

	/**
	 * @param elektronRechnung the elektronRechnung to set
	 */
	public void setElektronRechnung(String elektronRechnung) {
		this.elektronRechnung = elektronRechnung;
	}

	/**
	 * @return the erEmail
	 */
	public String getErEmail() {
		return erEmail;
	}

	/**
	 * @param erEmail the erEmail to set
	 */
	public void setErEmail(String erEmail) {
		this.erEmail = erEmail;
	}

	/**
	 * @return the dlCode
	 */
	public String getDlCode() {
		return dlCode;
	}

	/**
	 * @param dlCode the dlCode to set
	 */
	public void setDlCode(String dlCode) {
		this.dlCode = dlCode;
	}

	/**
	 * @return the verCode
	 */
	public String getVerCode() {
		return verCode;
	}

	/**
	 * @param verCode the verCode to set
	 */
	public void setVerCode(String verCode) {
		this.verCode = verCode;
	}

	/**
	 * @return the beZeich
	 */
	public String getBeZeich() {
		return beZeich;
	}

	/**
	 * @param beZeich the beZeich to set
	 */
	public void setBeZeich(String beZeich) {
		this.beZeich = beZeich;
	}

	/**
	 * @return the erDaten
	 */
	public String getErDaten() {
		return erDaten;
	}

	/**
	 * @param erDaten the erDaten to set
	 */
	public void setErDaten(String erDaten) {
		this.erDaten = erDaten;
	}

	/**
	 * @return the erSignatur
	 */
	public boolean getErSignatur() {
		return erSignatur;
	}

	/**
	 * @param erSignatur the erSignatur to set
	 */
	public void setErSignatur(boolean erSignatur) {
		this.erSignatur = erSignatur;
	}

	/**
	 * @return the erAnlagen
	 */
	public boolean getErAnlagen() {
		return erAnlagen;
	}

	/**
	 * @param erAnlagen the erAnlagen to set
	 */
	public void setErAnlagen(boolean erAnlagen) {
		this.erAnlagen = erAnlagen;
	}

	/**
	 * @return the screenFiveErrorCode
	 */
	public String getScreenFiveErrorCode() {
		return screenFiveErrorCode;
	}

	/**
	 * @param screenFiveErrorCode the screenFiveErrorCode to set
	 */
	public void setScreenFiveErrorCode(String screenFiveErrorCode) {
		this.screenFiveErrorCode = screenFiveErrorCode;
	}

	/**
	 * @return the screenSixErrorCode
	 */
	public String getScreenSixErrorCode() {
		return screenSixErrorCode;
	}

	/**
	 * @param screenSixErrorCode the screenSixErrorCode to set
	 */
	public void setScreenSixErrorCode(String screenSixErrorCode) {
		this.screenSixErrorCode = screenSixErrorCode;
	}

	/**
	 * @return the temporarInfozeile
	 */
	public String getTemporarInfozeile() {
		return temporarInfozeile;
	}

	/**
	 * @param temporarInfozeile the temporarInfozeile to set
	 */
	public void setTemporarInfozeile(String temporarInfozeile) {
		this.temporarInfozeile = temporarInfozeile;
	}

	/**
	 * @return the permanentInfozeile1
	 */
	public String getPermanentInfozeile1() {
		return permanentInfozeile1;
	}

	/**
	 * @param permanentInfozeile1 the permanentInfozeile1 to set
	 */
	public void setPermanentInfozeile1(String permanentInfozeile1) {
		this.permanentInfozeile1 = permanentInfozeile1;
	}

	/**
	 * @return the permanentInfozeile2
	 */
	public String getPermanentInfozeile2() {
		return permanentInfozeile2;
	}

	/**
	 * @param permanentInfozeile2 the permanentInfozeile2 to set
	 */
	public void setPermanentInfozeile2(String permanentInfozeile2) {
		this.permanentInfozeile2 = permanentInfozeile2;
	}

	/**
	 * @return the permanentInfozeile3
	 */
	public String getPermanentInfozeile3() {
		return permanentInfozeile3;
	}

	/**
	 * @param permanentInfozeile3 the permanentInfozeile3 to set
	 */
	public void setPermanentInfozeile3(String permanentInfozeile3) {
		this.permanentInfozeile3 = permanentInfozeile3;
	}

	/**
	 * @return the permanentInfozeile4
	 */
	public String getPermanentInfozeile4() {
		return permanentInfozeile4;
	}

	/**
	 * @param permanentInfozeile4 the permanentInfozeile4 to set
	 */
	public void setPermanentInfozeile4(String permanentInfozeile4) {
		this.permanentInfozeile4 = permanentInfozeile4;
	}

	/**
	 * @return the permanentInfozeile5
	 */
	public String getPermanentInfozeile5() {
		return permanentInfozeile5;
	}

	/**
	 * @param permanentInfozeile5 the permanentInfozeile5 to set
	 */
	public void setPermanentInfozeile5(String permanentInfozeile5) {
		this.permanentInfozeile5 = permanentInfozeile5;
	}

	/**
	 * @return the permanentInfozeile6
	 */
	public String getPermanentInfozeile6() {
		return permanentInfozeile6;
	}

	/**
	 * @param permanentInfozeile6 the permanentInfozeile6 to set
	 */
	public void setPermanentInfozeile6(String permanentInfozeile6) {
		this.permanentInfozeile6 = permanentInfozeile6;
	}

	/**
	 * @return the temporarInfozeileChb
	 */
	public boolean isTemporarInfozeileChb() {
		return temporarInfozeileChb;
	}

	/**
	 * @param temporarInfozeileChb the temporarInfozeileChb to set
	 */
	public void setTemporarInfozeileChb(boolean temporarInfozeileChb) {
		this.temporarInfozeileChb = temporarInfozeileChb;
	}

	/**
	 * @return the permanentInfozeileChb1
	 */
	public boolean isPermanentInfozeileChb1() {
		return permanentInfozeileChb1;
	}

	/**
	 * @param permanentInfozeileChb1 the permanentInfozeileChb1 to set
	 */
	public void setPermanentInfozeileChb1(boolean permanentInfozeileChb1) {
		this.permanentInfozeileChb1 = permanentInfozeileChb1;
	}

	/**
	 * @return the permanentInfozeileChb2
	 */
	public boolean isPermanentInfozeileChb2() {
		return permanentInfozeileChb2;
	}

	/**
	 * @param permanentInfozeileChb2 the permanentInfozeileChb2 to set
	 */
	public void setPermanentInfozeileChb2(boolean permanentInfozeileChb2) {
		this.permanentInfozeileChb2 = permanentInfozeileChb2;
	}

	/**
	 * @return the permanentInfozeileChb3
	 */
	public boolean isPermanentInfozeileChb3() {
		return permanentInfozeileChb3;
	}

	/**
	 * @param permanentInfozeileChb3 the permanentInfozeileChb3 to set
	 */
	public void setPermanentInfozeileChb3(boolean permanentInfozeileChb3) {
		this.permanentInfozeileChb3 = permanentInfozeileChb3;
	}

	/**
	 * @return the permanentInfozeileChb4
	 */
	public boolean isPermanentInfozeileChb4() {
		return permanentInfozeileChb4;
	}

	/**
	 * @param permanentInfozeileChb4 the permanentInfozeileChb4 to set
	 */
	public void setPermanentInfozeileChb4(boolean permanentInfozeileChb4) {
		this.permanentInfozeileChb4 = permanentInfozeileChb4;
	}

	/**
	 * @return the permanentInfozeileChb5
	 */
	public boolean isPermanentInfozeileChb5() {
		return permanentInfozeileChb5;
	}

	/**
	 * @param permanentInfozeileChb5 the permanentInfozeileChb5 to set
	 */
	public void setPermanentInfozeileChb5(boolean permanentInfozeileChb5) {
		this.permanentInfozeileChb5 = permanentInfozeileChb5;
	}

	/**
	 * @return the permanentInfozeileChb6
	 */
	public boolean isPermanentInfozeileChb6() {
		return permanentInfozeileChb6;
	}

	/**
	 * @param permanentInfozeileChb6 the permanentInfozeileChb6 to set
	 */
	public void setPermanentInfozeileChb6(boolean permanentInfozeileChb6) {
		this.permanentInfozeileChb6 = permanentInfozeileChb6;
	}

	/**
	 * @return the keineDaimlerDSE1
	 */
	public boolean isKeineDaimlerDSE1() {
		return keineDaimlerDSE1;
	}

	/**
	 * @param keineDaimlerDSE1 the keineDaimlerDSE1 to set
	 */
	public void setKeineDaimlerDSE1(boolean keineDaimlerDSE1) {
		this.keineDaimlerDSE1 = keineDaimlerDSE1;
	}

	/**
	 * @return the telefonYes1
	 */
	public boolean isTelefonYes1() {
		return telefonYes1;
	}

	/**
	 * @param telefonYes1 the telefonYes1 to set
	 */
	public void setTelefonYes1(boolean telefonYes1) {
		this.telefonYes1 = telefonYes1;
	}

	/**
	 * @return the telefonNo1
	 */
	public boolean isTelefonNo1() {
		return telefonNo1;
	}

	/**
	 * @param telefonNo1 the telefonNo1 to set
	 */
	public void setTelefonNo1(boolean telefonNo1) {
		this.telefonNo1 = telefonNo1;
	}

	/**
	 * @return the faxYes1
	 */
	public boolean isFaxYes1() {
		return faxYes1;
	}

	/**
	 * @param faxYes1 the faxYes1 to set
	 */
	public void setFaxYes1(boolean faxYes1) {
		this.faxYes1 = faxYes1;
	}

	/**
	 * @return the faxNo1
	 */
	public boolean isFaxNo1() {
		return faxNo1;
	}

	/**
	 * @param faxNo1 the faxNo1 to set
	 */
	public void setFaxNo1(boolean faxNo1) {
		this.faxNo1 = faxNo1;
	}

	/**
	 * @return the dialogverbot1
	 */
	public boolean isDialogverbot1() {
		return dialogverbot1;
	}

	/**
	 * @param dialogverbot1 the dialogverbot1 to set
	 */
	public void setDialogverbot1(boolean dialogverbot1) {
		this.dialogverbot1 = dialogverbot1;
	}

	/**
	 * @return the smsYes1
	 */
	public boolean isSmsYes1() {
		return smsYes1;
	}

	/**
	 * @param smsYes1 the smsYes1 to set
	 */
	public void setSmsYes1(boolean smsYes1) {
		this.smsYes1 = smsYes1;
	}

	/**
	 * @return the smsNo1
	 */
	public boolean isSmsNo1() {
		return smsNo1;
	}

	/**
	 * @param smsNo1 the smsNo1 to set
	 */
	public void setSmsNo1(boolean smsNo1) {
		this.smsNo1 = smsNo1;
	}

	/**
	 * @return the keineService_CSI_Befragung1
	 */
	public boolean isKeineService_CSI_Befragung1() {
		return keineService_CSI_Befragung1;
	}

	/**
	 * @param keineService_CSI_Befragung1 the keineService_CSI_Befragung1 to set
	 */
	public void setKeineService_CSI_Befragung1(boolean keineService_CSI_Befragung1) {
		this.keineService_CSI_Befragung1 = keineService_CSI_Befragung1;
	}

	/**
	 * @return the emailYes1
	 */
	public boolean isEmailYes1() {
		return emailYes1;
	}

	/**
	 * @param emailYes1 the emailYes1 to set
	 */
	public void setEmailYes1(boolean emailYes1) {
		this.emailYes1 = emailYes1;
	}

	/**
	 * @return the emailNo1
	 */
	public boolean isEmailNo1() {
		return emailNo1;
	}

	/**
	 * @param emailNo1 the emailNo1 to set
	 */
	public void setEmailNo1(boolean emailNo1) {
		this.emailNo1 = emailNo1;
	}

	/**
	 * @return the dag_Fzg1
	 */
	public boolean isDag_Fzg1() {
		return dag_Fzg1;
	}

	/**
	 * @param dag_Fzg1 the dag_Fzg1 to set
	 */
	public void setDag_Fzg1(boolean dag_Fzg1) {
		this.dag_Fzg1 = dag_Fzg1;
	}

	/**
	 * @return the angekreuzt1
	 */
	public boolean isAngekreuzt1() {
		return angekreuzt1;
	}

	/**
	 * @param angekreuzt1 the angekreuzt1 to set
	 */
	public void setAngekreuzt1(boolean angekreuzt1) {
		this.angekreuzt1 = angekreuzt1;
	}

	/**
	 * @return the nichtAngekreuzt1
	 */
	public boolean isNichtAngekreuzt1() {
		return nichtAngekreuzt1;
	}

	/**
	 * @param nichtAngekreuzt1 the nichtAngekreuzt1 to set
	 */
	public void setNichtAngekreuzt1(boolean nichtAngekreuzt1) {
		this.nichtAngekreuzt1 = nichtAngekreuzt1;
	}

	/**
	 * @return the dialogverbot2
	 */
	public boolean isDialogverbot2() {
		return dialogverbot2;
	}

	/**
	 * @param dialogverbot2 the dialogverbot2 to set
	 */
	public void setDialogverbot2(boolean dialogverbot2) {
		this.dialogverbot2 = dialogverbot2;
	}

	/**
	 * @return the telefonYes2
	 */
	public boolean isTelefonYes2() {
		return telefonYes2;
	}

	/**
	 * @param telefonYes2 the telefonYes2 to set
	 */
	public void setTelefonYes2(boolean telefonYes2) {
		this.telefonYes2 = telefonYes2;
	}

	/**
	 * @return the telefonNo2
	 */
	public boolean isTelefonNo2() {
		return telefonNo2;
	}

	/**
	 * @param telefonNo2 the telefonNo2 to set
	 */
	public void setTelefonNo2(boolean telefonNo2) {
		this.telefonNo2 = telefonNo2;
	}

	/**
	 * @return the keineService_CSI_Befragung2
	 */
	public boolean isKeineService_CSI_Befragung2() {
		return keineService_CSI_Befragung2;
	}

	/**
	 * @param keineService_CSI_Befragung2 the keineService_CSI_Befragung2 to set
	 */
	public void setKeineService_CSI_Befragung2(boolean keineService_CSI_Befragung2) {
		this.keineService_CSI_Befragung2 = keineService_CSI_Befragung2;
	}

	/**
	 * @return the faxYes2
	 */
	public boolean isFaxYes2() {
		return faxYes2;
	}

	/**
	 * @param faxYes2 the faxYes2 to set
	 */
	public void setFaxYes2(boolean faxYes2) {
		this.faxYes2 = faxYes2;
	}

	/**
	 * @return the faxNo2
	 */
	public boolean isFaxNo2() {
		return faxNo2;
	}

	/**
	 * @param faxNo2 the faxNo2 to set
	 */
	public void setFaxNo2(boolean faxNo2) {
		this.faxNo2 = faxNo2;
	}

	/**
	 * @return the smsYes2
	 */
	public boolean isSmsYes2() {
		return smsYes2;
	}

	/**
	 * @param smsYes2 the smsYes2 to set
	 */
	public void setSmsYes2(boolean smsYes2) {
		this.smsYes2 = smsYes2;
	}

	/**
	 * @return the smsNo2
	 */
	public boolean isSmsNo2() {
		return smsNo2;
	}

	/**
	 * @param smsNo2 the smsNo2 to set
	 */
	public void setSmsNo2(boolean smsNo2) {
		this.smsNo2 = smsNo2;
	}

	/**
	 * @return the emailYes2
	 */
	public boolean isEmailYes2() {
		return emailYes2;
	}

	/**
	 * @param emailYes2 the emailYes2 to set
	 */
	public void setEmailYes2(boolean emailYes2) {
		this.emailYes2 = emailYes2;
	}

	/**
	 * @return the emailNo2
	 */
	public boolean isEmailNo2() {
		return emailNo2;
	}

	/**
	 * @param emailNo2 the emailNo2 to set
	 */
	public void setEmailNo2(boolean emailNo2) {
		this.emailNo2 = emailNo2;
	}

	/**
	 * @return the dag_Fzg2
	 */
	public boolean isDag_Fzg2() {
		return dag_Fzg2;
	}

	/**
	 * @param dag_Fzg2 the dag_Fzg2 to set
	 */
	public void setDag_Fzg2(boolean dag_Fzg2) {
		this.dag_Fzg2 = dag_Fzg2;
	}

	/**
	 * @return the angekreuzt2
	 */
	public boolean isAngekreuzt2() {
		return angekreuzt2;
	}

	/**
	 * @param angekreuzt2 the angekreuzt2 to set
	 */
	public void setAngekreuzt2(boolean angekreuzt2) {
		this.angekreuzt2 = angekreuzt2;
	}

	/**
	 * @return the nichtAngekreuzt2
	 */
	public boolean isNichtAngekreuzt2() {
		return nichtAngekreuzt2;
	}

	/**
	 * @param nichtAngekreuzt2 the nichtAngekreuzt2 to set
	 */
	public void setNichtAngekreuzt2(boolean nichtAngekreuzt2) {
		this.nichtAngekreuzt2 = nichtAngekreuzt2;
	}

	/**
	 * @return the keineDaimlerDSE2
	 */
	public boolean isKeineDaimlerDSE2() {
		return keineDaimlerDSE2;
	}

	/**
	 * @param keineDaimlerDSE2 the keineDaimlerDSE2 to set
	 */
	public void setKeineDaimlerDSE2(boolean keineDaimlerDSE2) {
		this.keineDaimlerDSE2 = keineDaimlerDSE2;
	}

	public String getAdditionalNameInfo() {
		return additionalNameInfo;
	}

	public void setAdditionalNameInfo(String additionalNameInfo) {
		this.additionalNameInfo = additionalNameInfo;
	}
	
	

}