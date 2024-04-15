package com.alphax.vo.mb;

import javax.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@Validated
@ApiModel(description = "This Object about Vehicle details")
public class VehicleDetailsVO {

	//license number
	@ApiModelProperty(value = "Textfield Amtliches Kennzeichen - DB M1_KFZF1XX - FKENNZ7")
	@Size(max=11)
	private String amtlichesKennzeichen;

	//Customer number
	@ApiModelProperty(value = "Textfield Kunden-Nummer - DB M1_KFZF1XX - KDNR1")
	@Size(max=8, message = "The kunden Nummer should not be greater than 8")
	private String kundenNummer;

	//Second Screen

	//Brand
	@ApiModelProperty(value = "Dropdown Marke - DB M1_KFZ221X - HMARKE")
	@Size(max=3)
	private String marke;

	//Vehicle ID Number
	@ApiModelProperty(value = "Textfield Fz.Identnr - DB M1_KFZF1XX - WHC")
	@Size(max=3)
	private String fzIdentnr1;

	@ApiModelProperty(value = "Textfield Fz.Identnr - DB M1_KFZF1XX - BAUM")
	@Size(max=6)
	private String fzIdentnr2;
	
	@ApiModelProperty(value = "Textfield Fz.Identnr - DB M1_KFZF1XX - BAUM")
	@Size(max=2)
	private String fzIdentnr3;

	@ApiModelProperty(value = "Textfield Fz.Identnr - DB M1_KFZF1XX - ENDN")
	@Size(max=6)
	private String fzIdentnr4;

	@ApiModelProperty(value = "Textfield Fz.Identnr - DB M1_KFZF1XX - FGP")
	@Size(max=1)
	private String fzIdentnr5;

	//Engine Number
	@ApiModelProperty(value = "Textfield Motor-Nummer - DB M1_KFZF1XX - MOTN")
	@Size(max=14)
	private String motorNummer;

	//Family Group
	@ApiModelProperty(value = "Textfield Familiengruppe - DB M1_KFZF1XX - BUCH1")
	@Size(max=2)
	private String familienGruppe;

	//Type code
	@ApiModelProperty(value = "Textfield Typenkennzahl - DB M1_KFZF1XX - SPAL1")
	@Size(max=2)
	private String typenkennzahl;

	@ApiModelProperty(value = "Textfield Typ-Nummer - DB M1_KFZF1XX - TYPNR")
	@Size(max=3)
	private String typNummer;

	@ApiModelProperty(value = "Textfield Typ-Variante - DB M1_KFZF1XX - TYPVA")
	@Size(max=2)
	private String typVariante;

	//Motor power (KW)
	@ApiModelProperty(value = "Textfield  Motor-Stärke (KW) - DB M1_KFZF1XX - KW")
	@Size(max=3)
	private String  motorstarkeKW;

	//Manufacturer code
	@ApiModelProperty(value = "Textfield  Hersteller-Code - DB M1_KFZ221X - HERCOD")
	@Size(max=4)
	private String herstellerCode;

	@ApiModelProperty(value = "Textfield  Typ-Code - DB M1_KFZ221X - TYPCOD")
	@Size(max=8)
	private String typCode;

	//Cubic capacity / Permitted total weight
	@ApiModelProperty(value = "Textfield  Hubraum / Zulässiges Gesamtgewicht - DB M1_KFZF1XX - HUB")
	@Size(max=5)
	private String hubraumZul_ges_gew;

	//Vehicle type
	@ApiModelProperty(value = "Textfield  Fahrzeug-Typ - DB M1_KFZF1XX - TYP")
	@Size(max=18)
	private String fahrzeugTyp;

	//Vehicle type Group
	@ApiModelProperty(value = "Textfield  Fahrzeug-Typgruppe - DB M1_KFZF1XX - TYPGR")
	@Size(max=2)
	private String fahrzeugTypgruppe;

	//Vehicle Art
	@ApiModelProperty(value = "Textfield  Fahrzeug-Art - DB M1_KFZF1XX - FART")
	@Size(max=2)
	private String fahrzeugArt;

	//Last admission
	@ApiModelProperty(value = "DatePicker Letzte Zulassung - DB M1_KFZF1XX - DTLIP - DB maxLength 6")
	@Size(max=10)
	private String letzteZulassung;

	//private String letzteZulassung_mm;
	//private String letzteZulassung_yyyy;

	//First registration
	@ApiModelProperty(value = "DatePicker  Erste Zulassung - DB M1_KFZF1XX - DTEZU - DB maxLength 7")
	@Size(max=10)
	private String ersteZulassung;
	//private String ersteZulassung_mm;
	//private String ersteZulassung_yyyy;

	//Registration type
	@ApiModelProperty(value = "Dropdown  Zulassungsart - DB M1_KFZF1XX - ZUART")
	@Size(max=1)
	private String zulassungsart;

	//Own sales
	@ApiModelProperty(value = "Checkbox  Zulassungsart - DB M1_KFZF1XX - KZEV - DB maxLength 1")
	private boolean eigenverkauf;

	//Payload / No. of seats
	@ApiModelProperty(value = "Textfield   Nutzlast/Anz.Plätze - DB M1_KFZF1XX - PLATZ")
	@Size(max=5)
	private String nutzlastAnz_platze ;

	
	//Drive Type
	@ApiModelProperty(value = "Dropdown  Antriebsart  - DB M1_KFZF1XX - ANTA")
	@Size(max=1)
	private String antriebsart;

	// Number of previous owner
	@ApiModelProperty(value = "Textfield Anzahl Vorbesitzer - DB M1_KFZF1XX - ANZVO")
	@Size(max=2)
	private String anzahlVorbesitzer;

	//Mileage indicator
	@ApiModelProperty(value = "Dropdown   Laufleistung-Kennzeichen - DB M1_KFZF1XX - GKM")
	@Size(max=1)
	private String laufleistungKennzeichen;

	// Mileage
	@ApiModelProperty(value = "Textfield Laufleistung - DB M1_KFZF1XX - KZLF")
	@Size(max=7)
	private String laufleistung;

	// Gear number
	@ApiModelProperty(value = "Textfield Getriebe-Nummer - DB M1_KFZ221X - FGNR")
	@Size(max=20)
	private String getriebeNummer;

	//Axis number
	@ApiModelProperty(value = "Textfield Achs-Nummer - DB M1_KFZ221X - FANR")
	@Size(max=20)
	private String achsNummer;

	//Mileage Exchange engine
	@ApiModelProperty(value = "Textfield Laufleistung Austauschmotor - DB M1_KFZ221X - ATMKM")
	@Size(max=7)
	private String laufleistAustauschmotor;

	//Third Screen

	//Fleet number
	@ApiModelProperty(value = "Textbox Fuhrpark-Nummer - DB M1_KFZ221X - FFUHRP")
	@Size(max=10)
	private String fuhrparkNr;

	//Seller
	@ApiModelProperty(value = "Textfield Verkäufer - DB M1_KFZF1XX - VERK")
	@Size(max=6)
	private String verkaufer;

	//Order number
	@ApiModelProperty(value = "Textfield Aufrags-Nummer - DB M1_KFZF1XX - AUFN")
	@Size(max=10)
	private String aufragsNummer;

	//Maintenance group
	@ApiModelProperty(value = "Textfield Wartungs-Gruppe - DB M1_KFZF1XX - WAGR")
	@Size(max=1)
	private String wartungsGruppe;

	//Maintenance contract
	@ApiModelProperty(value = "Textfield Wartungs-Vertrag - DB M1_KFZF1XX - WAVT_TBL DB maxLength 6")
	@Size(max=1)
	private String wartungsVertrag1;
	@Size(max=1)
	private String wartungsVertrag2;
	@Size(max=1)
	private String wartungsVertrag3;
	@Size(max=1)
	private String wartungsVertrag4;
	@Size(max=1)
	private String wartungsVertrag5;
	@Size(max=1)
	private String wartungsVertrag6;

	//Order date
	@ApiModelProperty(value = "DatePicker Bestell-Datum - DB M1_KFZF1XX - DTBES - DB maxLength 6")
	@Size(max=10)
	private String bestellDatum;

	//private String bestellDatum_mm;
	//private String bestellDatum_yy;

	//Delivery / takeover date
	@ApiModelProperty(value = "DatePicker Liefer-/Übernahmedatum - DB Mx_KFZ221X - (FZWI_TT+FZWI_MJ) - DB maxLength 6")
	@Size(max=10)
	private String lieferUbernahmedatum;
	//private String lieferUbernahmedatum_mm;
	//private String lieferUbernahmedatum_yy;

	//Customer service consultant
	@ApiModelProperty(value = "Textfield Kundendienst-Berater - DB M1_KFZF1XX - KDBE")
	@Size(max=2)
	private String kundendienstBerater;

	//Construction year
	@ApiModelProperty(value = "DatePicker Baujahr - DB Mx_KFZ221X - BAUJAHR - - DB maxLength 8")
	@Size(max=10)
	private String baujahr;

	//private String Baujahr_mm;
	//private String Baujahr_yyyy;

	//accident
	@ApiModelProperty(value = "Dropdown Unfall - DB M1_KFZF1XX - UNFAL - DB maxLength 1")
	private boolean unfall;

	//Comparison class
	@ApiModelProperty(value = "Textfield Vergleichsklasse - DB Mx_KFZ221X - VKL")
	@Size(max=2)
	private String vergleichsklasse;

	//Supporting workshop
	@ApiModelProperty(value = " Textfield Betreuende Werkstatt - DB M1_KFZF1XX - WSTN")
	@Size(max=8)
	private String betreuendeWerkstatt;

	//Sales division
	@ApiModelProperty(value = "Textfield Verkaufssparte - DB Mx_KFZ221X - VKSPA")
	@Size(max=2)
	private String verkaufssparte;

	//Acquisition lock
	@ApiModelProperty(value = "Textfield Akquisitionsperre - DB M1_KFZF1XX - KZAQ")
	@Size(max=2)
	private String akquisitionsperre;

	//Additional order text
	@ApiModelProperty(value = "Textfield Auftrags-Zusatz-Text - DB Mx_KFZ221X - FAUFZU")
	@Size(max=30)
	private String auftragsZusatzText;

	@ApiModelProperty(value = " Textfield Info-Text - DB M1_KFZF1XX - TEXT")
	@Size(max=20)
	private String infoText;

	//Fourth Screen

	//Main investigation
	@ApiModelProperty(value = " Datepicker Haupt-Untersuchung - DB M1_KFZF1XX - DTTUV DB maxLength 4")
	@Size(max=10)
	private String hauptUntersuchung1;
	@ApiModelProperty(hidden=true)
	private String hauptUntersuchung2;
	//HU license plate
	@ApiModelProperty(value = " Textfield HU-Kennzeichen- DB M1_KFZF1XX - KZTUV")
	@Size(max=2)
	private String huKennzeichen;
	//safety test
	@ApiModelProperty(value = " DatePicker Sicherheitsprüfung - DB Mx_KFZ221X - FBRE_MJ DB maxLength 4")
	@Size(max=10)
	private String sicherheitsprufung1;
	@Size(max=2)
	private String sicherheitsprufung2;
	//accident prevention regulation
	@ApiModelProperty(value = " DatePicker Unfallverhütungsvorschrift - DB Mx_KFZ221X - UVV_MJ DB maxLength 6")
	@Size(max=10)
	private String unfallverhutungsvorschrift1;
	@ApiModelProperty(hidden=true)
	private String unfallverhutungsvorschrift2;
	//Speedometer inspection
	@ApiModelProperty(value = " DatePicker Tacho-Untersuchung - DB Mx_KFZ221X - FTAO DB maxLength 6")
	@Size(max=10)
	private String tachoUntersuchung;
	//private String tachoUntersuchung_mm;
	//private String tachoUntersuchung_yy;
	//Number of visits to the workshop
	@ApiModelProperty(value = " Textfield Anzahl Werkstattbesuche- DB M1_KFZF1XX - ANZWE")
	@Size(max=3)
	private String anzahlWerkstattbesuche;
	//Last visit to the workshop
	@ApiModelProperty(value = " DatePicker Letzter Werkstattbesuch - DB M1_KFZF1XX - DTLWE DB maxLength 6")
	@Size(max=10)
	private String letzterWerkstattbesuch;
	//private String letzterWerkstattbesuch_mm;
	//private String letzterWerkstattbesuch_yy;
	//Mileage
	@ApiModelProperty(value = " Textfield Laufleistun - DB M1_KFZF1XX - KMLWE")
	@Size(max=7)
	private String laufleistun;
	//Last maintenance
	@ApiModelProperty(value = " DatePicker Letzte Wartung - DB M1_KFZF1XX - DTLWA DB maxLength 6")
	@Size(max=10)
	private String letzteWartung;
	//private String letzteWartung_mm;
	//private String letzteWartung_yy;
	//mileage
	@ApiModelProperty(value = " Textfield Laufleistung - DB M1_KFZF1XX - KMLWA")
	@Size(max=7)
	private String laufleistung4thScreen;
	//Coolant change
	@ApiModelProperty(value = " DatePicker Kühlmittelwechsel - DB Mx_KFZ221X - FAKL_MJ DB maxLength 4")
	@Size(max=10)
	private String kuhlmittelwechsel1;
	@ApiModelProperty(hidden=true)
	private String kuhlmittelwechsel2;
	//Brake-fluid change
	@ApiModelProperty(value = " DatePicker Bremsflüssigkeitswechsel - DB Mx_KFZ221X - FABF_MJ DB maxLength 4")
	@Size(max=10)
	private String bremsflussigkeitswechsel1;
	@ApiModelProperty(hidden=true)
	private String bremsflussigkeitswechsel2;
	@ApiModelProperty(value = " Checkbox ASSYST - DB Mx_KFZ221X - ASSYST")
	//@Size(max=1)
	private boolean assyst;
	@ApiModelProperty(value = " Dropdown Art-L-SU - DB Mx_KFZ221X - ARTLWD")
	@Size(max=2)
	private String art_l_su;
	@ApiModelProperty(value = " Textfield SU-B a.KM - DB Mx_KFZ221X - WDABKM")
	@Size(max=7)
	private String su_Ba_km;
	@ApiModelProperty(value = " DatePicker SU-B ab - DB M1_KFZ221X - WDAB DB maxLength 8")
	@Size(max=10)
	private String su_b_ab;
	//private String su_b_ab_mm;
	//private String su_b_ab_yyyy;

	//Fifth Screen
	@ApiModelProperty(value = " Dropdown Kunden-Nr. VEGA - DB M1_KFZ221X - SERKDNR")
	@Size(max=8)
	private String kundenNumberVega;
	//Service contract
	@ApiModelProperty(value = " Textfield VEGA Name - DB M1_KDDATK1 - NAME")
	@Size(max=25)
	private String vegaName;
	//Customer number. Receipt printing
	@ApiModelProperty(value = " Dropdown Kunden-Nr. Belegdruck - DB M1_KFZ221X - SERKDNR2")
	@Size(max=8)
	private String kundenNumberBelegdruck;
	//Service contract
	@ApiModelProperty(value = " Textfield Belegdruck Name - DB M1_KDDATK1 - NAME")
	@Size(max=25)
	private String belegdruck_Name;
	//Contract no.
	@ApiModelProperty(value = " Textfield Vertrags-Nr.- DB M1_KFZ221X - SERVNR")
	@Size(max=20)
	private String servicevertragVertragsNumber;
	//running time
	@ApiModelProperty(value = " DatePicker Laufzeit- DB M1_KFZ221X - SERDTB DB maxLength 8")
	@Size(max=10)
	private String laufzeit1;
	//private String laufzeit1_mm;
	//private String laufzeit1_yyyy;
	@ApiModelProperty(value = " DatePicker Laufzeit- DB M1_KFZ221X - SERDTE DB maxLength 8")
	@Size(max=10)
	private String laufzeit2;
	//private String laufzeit2_mm;
	//private String laufzeit2_yyyy;
	// Km Limit
	@ApiModelProperty(value = " Textfield km-Begrenzung - DB M1_KFZ221X - SERKM")
	@Size(max=7)
	private String servicevertrag_kmBegrenzung;

	//Insurance cooperation
	@ApiModelProperty(value = " Dropdown Kunden-Nr.- DB M1_KFZ221X - VSKOKDNR")
	@Size(max=8)
	private String versichererko_KundenNumber;
	@ApiModelProperty(value = " Textfield Name - DB M1_KDDATK1 - NAME")
	@Size(max=25)
	private String versichererko_Name;
	@ApiModelProperty(value = " Textfield Vertrags-Nr.- DB M1_KFZ221X - VSKOVNR")
	@Size(max=20)
	private String versichererko_VertragsNumber;
	@ApiModelProperty(value = " DatePicker Laufzeit- DB M1_KFZ221X - VSKODTBEG DB maxLength 8")
	@Size(max=10)
	private String versichererko_laufzeit1;
	//private String versichererko_laufzeit1_mm;
	//private String versichererko_laufzeit1_yyyy;
	@ApiModelProperty(value = " DatePicker Laufzeit- DB M1_KFZ221X - VSKODTEND DB maxLength 8")
	@Size(max=10)
	private String versichererko_laufzeit2;
	//private String versichererko_laufzeit2_mm;
	//private String versichererko_laufzeit2_yyyy;
	@ApiModelProperty(value = " Textfield km-Begrenzung - DB M1_KFZ221X  - VSKOKM")
	@Size(max=7)
	private String versichererko_kmBegrenzung;

	//Another contract
	@ApiModelProperty(value = " Dropdown Kunden-Nr.- DB M1_KFZ221X - WTRVKDNR")
	@Size(max=8)
	private String weitererVertrag_KundenNumber;
	@ApiModelProperty(value = " Textfield Name - DB M1_KDDATK1 - NAME")
	@Size(max=25)
	private String weitererVertrag_Name;
	
	@ApiModelProperty(value = " Textfield Vertrags-Nr.- DB M1_KFZ221X - WTRVVNR")
	@Size(max=20)
	private String weitererVertrag_VertragsNumber;
	@ApiModelProperty(value = " DatePicker Laufzeit- DB M1_KFZ221X - WTRVDTBEG DB maxLength 8")
	@Size(max=10)
	private String weitererVertrag_laufzeit1;
	//private String weitererVertrag_laufzeit1_mm;
	//private String weitererVertrag_laufzeit1_yyyy;
	@ApiModelProperty(value = " DatePicker Laufzeit- DB M1_KFZ221X - WTRVDTEND DB maxLength 8")
	@Size(max=10)
	private String weitererVertrag_laufzeit2;
	//private String weitererVertrag_laufzeit2_mm;
	//private String weitererVertrag_laufzeit2_yyyy;
	@ApiModelProperty(value = " Textfield km-Begrenzung - DB M1_KFZ221X  - WTRVKM")
	@Size(max=7)
	private String weitererVertrag_kmBegrenzung;

	//Sixth Screen

	//Another contract
	@ApiModelProperty(value = " Dropdown Kunden-Nr.- DB M1_KFZ221X - FINKDNR")
	@Size(max=8)
	private String finanzierunq_KundenNumber;
	@ApiModelProperty(value = " Textfield Name - DB M1_KDDATK1 - NAME")
	@Size(max=25)
	private String finanzierunq_Name;
	@ApiModelProperty(value = " Textfield Vertrags-Nr.- DB M1_KFZ221X - FINVNR")
	@Size(max=20)
	private String finanzierunq_VertragsNumber;
	@ApiModelProperty(value = " DatePicker Laufzeit- DB M1_KFZ221X - FINDTBEG DB maxLength 8")
	@Size(max=10)
	private String finanzierunq_laufzeit1;
	//private String finanzierunq_laufzeit1_mm;
	//private String finanzierunq_laufzeit1_yyyy;
	@ApiModelProperty(value = " DatePicker Laufzeit- DB M1_KFZ221X - FINDTEND DB maxLength 8")
	@Size(max=10)
	private String finanzierunq_laufzeit2;
	//private String finanzierunq_laufzeit2_mm;
	//private String finanzierunq_laufzeit2_yyyy;
	@ApiModelProperty(value = " Textfield km-Begrenzung - DB M1_KFZ221X  - FINKM")
	@Size(max=7)
	private String finanzierunq_kmBegrenzung;
	//Type of financing
	@ApiModelProperty(value = " Textfield Finanzierungsart - DB M1_KFZ221X  - FINART")
	@Size(max=1)
	private String finanzierungsart1;
	@ApiModelProperty(hidden=true)
	private String finanzierungsart2;
	//Finance company
	@ApiModelProperty(value = " Textfield Finanz.gesellschaft - DB M1_KFZ221X  - FINGES")
	@Size(max=2)
	private String finanzGesellschaft1;
	@ApiModelProperty(hidden=true)
	private String finanzGesellschaft2;

	//Special offer
	//action
	@ApiModelProperty(value = " Dropdown Aktion - DB M1_KFZ221X  - VORAAKTION")
	@Size(max=4)
	private String aktion1;
	@ApiModelProperty(hidden=true)
	private String aktion2;
	//Contract / card number
	@ApiModelProperty(value = " Dropdown Vertrags-/Karten-Nr. - DB M1_KFZ221X - VORAVNR")
	@Size(max=20)
	private String kartenNumber;
	@ApiModelProperty(value = " DatePicker Laufzeit- DB M1_KFZ221X - VORADTBEG DB maxLength 8")
	@Size(max=10)
	private String vorteilsaktion_laufzeit1;
	//private String vorteilsaktion_laufzeit1_mm;
	//private String vorteilsaktion_laufzeit1_yyyy;
	@ApiModelProperty(value = " DatePicker Laufzeit- DB M1_KFZ221X - VORADTEND DB maxLength 8")
	@Size(max=10)
	private String vorteilsaktion_laufzeit2;
	//private String vorteilsaktion_laufzeit2_mm;
	//private String vorteilsaktion_laufzeit2_yyyy;
	@ApiModelProperty(value = " Textfield km-Begrenzung - DB M1_KFZ221X  - VORAKM")
	@Size(max=7)
	private String vorteilsaktion_kmBegrenzung;
	
	//Seventh Screen
	@ApiModelProperty(value = " Textfield Briefkastentexte - DB M1_KFZ222X  - FTEXT_T")
	@Size(max=38)
	private String briefkastentext;
	
	@ApiModelProperty(value = " Textfield FZ-Briefkastentext 1 - DB M1_KFZ222X  - FTEXT_1")
	@Size(max=38)
	private String briefkastentext1;
	
	@ApiModelProperty(value = " Textfield FZ-Briefkastentext 2 - DB M1_KFZ222X  - FTEXT_2")
	@Size(max=38)
	private String briefkastentext2;
	
	@ApiModelProperty(value = " Textfield FZ-Briefkastentext 3 - DB M1_KFZ222X  - FTEXT_3")
	@Size(max=38)
	private String briefkastentext3;
	
	@ApiModelProperty(value = " Textfield FZ-Briefkastentext 4 - DB M1_KFZ222X  - FTEXT_4")
	@Size(max=38)
	private String briefkastentext4;
	
	@ApiModelProperty(value = " Textfield FZ-Briefkastentext 5 - DB M1_KFZ222X  - FTEXT_5")
	@Size(max=38)
	private String briefkastentext5;
	
	@ApiModelProperty(value = " Textfield FZ-Briefkastentext 6 - DB M1_KFZ222X  - FTEXT_6")
	@Size(max=38)
	private String briefkastentext6;
	
	@ApiModelProperty(value = " Checkbox Druckkennzeichen - DB M1_KFZ222X  - DRUCKBK  DB maxLength 7")
	private boolean druckkennzeichenChb;
	
	@ApiModelProperty(value = " Checkbox Druckkennzeichen - DB M1_KFZ222X  - DRUCKBK DB maxLength 7")
	private boolean druckkennzeichenChb1;
	
	@ApiModelProperty(value = " Checkbox Druckkennzeichen - DB M1_KFZ222X  - DRUCKBK DB maxLength 7")
	private boolean druckkennzeichenChb2;
	
	@ApiModelProperty(value = " Checkbox Druckkennzeichen - DB M1_KFZ222X  - DRUCKBK DB maxLength 7")
	private boolean druckkennzeichenChb3;
	
	@ApiModelProperty(value = " Checkbox Druckkennzeichen - DB M1_KFZ222X  - DRUCKBK DB maxLength 7")
	private boolean druckkennzeichenChb4;
	
	@ApiModelProperty(value = " Checkbox Druckkennzeichen - DB M1_KFZ222X  - DRUCKBK DB maxLength 7")
	private boolean druckkennzeichenChb5;
	
	@ApiModelProperty(value = " Checkbox Druckkennzeichen - DB M1_KFZ222X  - DRUCKBK DB maxLength 7")
	private boolean druckkennzeichenChb6;

	//@ApiModelProperty(value = "Error List",hidden=true)
	//private List<String> errorList = new ArrayList<String>();
	@ApiModelProperty(value = "Error List",hidden=true)
	private String returnCode;
	@ApiModelProperty(value = "Error List",hidden=true)
	private String returnMsg;
	@ApiModelProperty(value = "PopUp Message",hidden=true)
	private String popUpMsg;


	public String getAmtlichesKennzeichen() {
		return amtlichesKennzeichen;
	}
	public void setAmtlichesKennzeichen(String amtlichesKennzeichen) {
		this.amtlichesKennzeichen = amtlichesKennzeichen;
	}
	public String getKundenNummer() {
		return kundenNummer;
	}
	public void setKundenNummer(String kundenNummer) {
		this.kundenNummer = kundenNummer;
	}
	public String getMarke() {
		return marke;
	}
	public void setMarke(String marke) {
		this.marke = marke;
	}
	public String getFzIdentnr1() {
		return fzIdentnr1;
	}
	public void setFzIdentnr1(String fzIdentnr1) {
		this.fzIdentnr1 = fzIdentnr1;
	}
	public String getFzIdentnr2() {
		return fzIdentnr2;
	}
	public void setFzIdentnr2(String fzIdentnr2) {
		this.fzIdentnr2 = fzIdentnr2;
	}
	public String getFzIdentnr3() {
		return fzIdentnr3;
	}
	public void setFzIdentnr3(String fzIdentnr3) {
		this.fzIdentnr3 = fzIdentnr3;
	}
	public String getFzIdentnr4() {
		return fzIdentnr4;
	}
	public void setFzIdentnr4(String fzIdentnr4) {
		this.fzIdentnr4 = fzIdentnr4;
	}
	public String getFzIdentnr5() {
		return fzIdentnr5;
	}
	public void setFzIdentnr5(String fzIdentnr5) {
		this.fzIdentnr5 = fzIdentnr5;
	}
	public String getMotorNummer() {
		return motorNummer;
	}
	public void setMotorNummer(String motorNummer) {
		this.motorNummer = motorNummer;
	}
	public String getFamilienGruppe() {
		return familienGruppe;
	}
	public void setFamilienGruppe(String familienGruppe) {
		this.familienGruppe = familienGruppe;
	}
	public String getTypenkennzahl() {
		return typenkennzahl;
	}
	public void setTypenkennzahl(String typenkennzahl) {
		this.typenkennzahl = typenkennzahl;
	}
	public String getTypNummer() {
		return typNummer;
	}
	public void setTypNummer(String typNummer) {
		this.typNummer = typNummer;
	}
	public String getTypVariante() {
		return typVariante;
	}
	public void setTypVariante(String typVariante) {
		this.typVariante = typVariante;
	}
	public String getMotorstarkeKW() {
		return motorstarkeKW;
	}
	public void setMotorstarkeKW(String motorstarkeKW) {
		this.motorstarkeKW = motorstarkeKW;
	}
	public String getHerstellerCode() {
		return herstellerCode;
	}
	public void setHerstellerCode(String herstellerCode) {
		this.herstellerCode = herstellerCode;
	}
	public String getTypCode() {
		return typCode;
	}
	public void setTypCode(String typCode) {
		this.typCode = typCode;
	}
	public String getHubraumZul_ges_gew() {
		return hubraumZul_ges_gew;
	}
	public void setHubraumZul_ges_gew(String hubraumZul_ges_gew) {
		this.hubraumZul_ges_gew = hubraumZul_ges_gew;
	}
	public String getFahrzeugTyp() {
		return fahrzeugTyp;
	}
	public void setFahrzeugTyp(String fahrzeugTyp) {
		this.fahrzeugTyp = fahrzeugTyp;
	}
	public String getFahrzeugTypgruppe() {
		return fahrzeugTypgruppe;
	}
	public void setFahrzeugTypgruppe(String fahrzeugTypgruppe) {
		this.fahrzeugTypgruppe = fahrzeugTypgruppe;
	}
	public String getFahrzeugArt() {
		return fahrzeugArt;
	}
	public void setFahrzeugArt(String fahrzeugArt) {
		this.fahrzeugArt = fahrzeugArt;
	}
	public String getLetzteZulassung() {
		return letzteZulassung;
	}
	public void setLetzteZulassung(String letzteZulassung) {
		this.letzteZulassung = letzteZulassung;
	}
	public String getErsteZulassung() {
		return ersteZulassung;
	}
	public void setErsteZulassung(String ersteZulassung) {
		this.ersteZulassung = ersteZulassung;
	}
	public String getZulassungsart() {
		return zulassungsart;
	}
	public void setZulassungsart(String zulassungsart) {
		this.zulassungsart = zulassungsart;
	}
	public boolean isEigenverkauf() {
		return eigenverkauf;
	}
	public void setEigenverkauf(boolean eigenverkauf) {
		this.eigenverkauf = eigenverkauf;
	}
	public String getNutzlastAnz_platze() {
		return nutzlastAnz_platze;
	}
	public void setNutzlastAnz_platze(String nutzlastAnz_platze) {
		this.nutzlastAnz_platze = nutzlastAnz_platze;
	}
	public String getAntriebsart() {
		return antriebsart;
	}
	public void setAntriebsart(String antriebsart) {
		this.antriebsart = antriebsart;
	}
	public String getAnzahlVorbesitzer() {
		return anzahlVorbesitzer;
	}
	public void setAnzahlVorbesitzer(String anzahlVorbesitzer) {
		this.anzahlVorbesitzer = anzahlVorbesitzer;
	}
	public String getLaufleistungKennzeichen() {
		return laufleistungKennzeichen;
	}
	public void setLaufleistungKennzeichen(String laufleistungKennzeichen) {
		this.laufleistungKennzeichen = laufleistungKennzeichen;
	}
	public String getLaufleistung() {
		return laufleistung;
	}
	public void setLaufleistung(String laufleistung) {
		this.laufleistung = laufleistung;
	}
	public String getGetriebeNummer() {
		return getriebeNummer;
	}
	public void setGetriebeNummer(String getriebeNummer) {
		this.getriebeNummer = getriebeNummer;
	}
	public String getAchsNummer() {
		return achsNummer;
	}
	public void setAchsNummer(String achsNummer) {
		this.achsNummer = achsNummer;
	}
	public String getLaufleistAustauschmotor() {
		return laufleistAustauschmotor;
	}
	public void setLaufleistAustauschmotor(String laufleistAustauschmotor) {
		this.laufleistAustauschmotor = laufleistAustauschmotor;
	}
	public String getFuhrparkNr() {
		return fuhrparkNr;
	}
	public void setFuhrparkNr(String fuhrparkNr) {
		this.fuhrparkNr = fuhrparkNr;
	}
	public String getVerkaufer() {
		return verkaufer;
	}
	public void setVerkaufer(String verkaufer) {
		this.verkaufer = verkaufer;
	}
	public String getAufragsNummer() {
		return aufragsNummer;
	}
	public void setAufragsNummer(String aufragsNummer) {
		this.aufragsNummer = aufragsNummer;
	}
	public String getWartungsGruppe() {
		return wartungsGruppe;
	}
	public void setWartungsGruppe(String wartungsGruppe) {
		this.wartungsGruppe = wartungsGruppe;
	}
	public String getWartungsVertrag1() {
		return wartungsVertrag1;
	}
	public void setWartungsVertrag1(String wartungsVertrag1) {
		this.wartungsVertrag1 = wartungsVertrag1;
	}
	public String getWartungsVertrag2() {
		return wartungsVertrag2;
	}
	public void setWartungsVertrag2(String wartungsVertrag2) {
		this.wartungsVertrag2 = wartungsVertrag2;
	}
	public String getWartungsVertrag3() {
		return wartungsVertrag3;
	}
	public void setWartungsVertrag3(String wartungsVertrag3) {
		this.wartungsVertrag3 = wartungsVertrag3;
	}
	public String getWartungsVertrag4() {
		return wartungsVertrag4;
	}
	public void setWartungsVertrag4(String wartungsVertrag4) {
		this.wartungsVertrag4 = wartungsVertrag4;
	}
	public String getWartungsVertrag5() {
		return wartungsVertrag5;
	}
	public void setWartungsVertrag5(String wartungsVertrag5) {
		this.wartungsVertrag5 = wartungsVertrag5;
	}
	public String getWartungsVertrag6() {
		return wartungsVertrag6;
	}
	public void setWartungsVertrag6(String wartungsVertrag6) {
		this.wartungsVertrag6 = wartungsVertrag6;
	}
	public String getBestellDatum() {
		return bestellDatum;
	}
	public void setBestellDatum(String bestellDatum) {
		this.bestellDatum = bestellDatum;
	}
	public String getLieferUbernahmedatum() {
		return lieferUbernahmedatum;
	}
	public void setLieferUbernahmedatum(String lieferUbernahmedatum) {
		this.lieferUbernahmedatum = lieferUbernahmedatum;
	}
	public String getKundendienstBerater() {
		return kundendienstBerater;
	}
	public void setKundendienstBerater(String kundendienstBerater) {
		this.kundendienstBerater = kundendienstBerater;
	}
	public String getBaujahr() {
		return baujahr;
	}
	public void setBaujahr(String baujahr) {
		this.baujahr = baujahr;
	}
	public boolean isUnfall() {
		return unfall;
	}
	public void setUnfall(boolean unfall) {
		this.unfall = unfall;
	}
	public String getVergleichsklasse() {
		return vergleichsklasse;
	}
	public void setVergleichsklasse(String vergleichsklasse) {
		this.vergleichsklasse = vergleichsklasse;
	}
	public String getBetreuendeWerkstatt() {
		return betreuendeWerkstatt;
	}
	public void setBetreuendeWerkstatt(String betreuendeWerkstatt) {
		this.betreuendeWerkstatt = betreuendeWerkstatt;
	}
	public String getVerkaufssparte() {
		return verkaufssparte;
	}
	public void setVerkaufssparte(String verkaufssparte) {
		this.verkaufssparte = verkaufssparte;
	}
	public String getAkquisitionsperre() {
		return akquisitionsperre;
	}
	public void setAkquisitionsperre(String akquisitionsperre) {
		this.akquisitionsperre = akquisitionsperre;
	}
	public String getAuftragsZusatzText() {
		return auftragsZusatzText;
	}
	public void setAuftragsZusatzText(String auftragsZusatzText) {
		this.auftragsZusatzText = auftragsZusatzText;
	}
	public String getInfoText() {
		return infoText;
	}
	public void setInfoText(String infoText) {
		this.infoText = infoText;
	}
	public String getHauptUntersuchung1() {
		return hauptUntersuchung1;
	}
	public void setHauptUntersuchung1(String hauptUntersuchung1) {
		this.hauptUntersuchung1 = hauptUntersuchung1;
	}
	public String getHauptUntersuchung2() {
		return hauptUntersuchung2;
	}
	public void setHauptUntersuchung2(String hauptUntersuchung2) {
		this.hauptUntersuchung2 = hauptUntersuchung2;
	}
	public String getHuKennzeichen() {
		return huKennzeichen;
	}
	public void setHuKennzeichen(String huKennzeichen) {
		this.huKennzeichen = huKennzeichen;
	}
	public String getSicherheitsprufung1() {
		return sicherheitsprufung1;
	}
	public void setSicherheitsprufung1(String sicherheitsprufung1) {
		this.sicherheitsprufung1 = sicherheitsprufung1;
	}
	public String getSicherheitsprufung2() {
		return sicherheitsprufung2;
	}
	public void setSicherheitsprufung2(String sicherheitsprufung2) {
		this.sicherheitsprufung2 = sicherheitsprufung2;
	}
	public String getUnfallverhutungsvorschrift1() {
		return unfallverhutungsvorschrift1;
	}
	public void setUnfallverhutungsvorschrift1(String unfallverhutungsvorschrift1) {
		this.unfallverhutungsvorschrift1 = unfallverhutungsvorschrift1;
	}
	public String getUnfallverhutungsvorschrift2() {
		return unfallverhutungsvorschrift2;
	}
	public void setUnfallverhutungsvorschrift2(String unfallverhutungsvorschrift2) {
		this.unfallverhutungsvorschrift2 = unfallverhutungsvorschrift2;
	}
	public String getTachoUntersuchung() {
		return tachoUntersuchung;
	}
	public void setTachoUntersuchung(String tachoUntersuchung) {
		this.tachoUntersuchung = tachoUntersuchung;
	}
	public String getAnzahlWerkstattbesuche() {
		return anzahlWerkstattbesuche;
	}
	public void setAnzahlWerkstattbesuche(String anzahlWerkstattbesuche) {
		this.anzahlWerkstattbesuche = anzahlWerkstattbesuche;
	}
	public String getLetzterWerkstattbesuch() {
		return letzterWerkstattbesuch;
	}
	public void setLetzterWerkstattbesuch(String letzterWerkstattbesuch) {
		this.letzterWerkstattbesuch = letzterWerkstattbesuch;
	}
	public String getLaufleistun() {
		return laufleistun;
	}
	public void setLaufleistun(String laufleistun) {
		this.laufleistun = laufleistun;
	}
	public String getLetzteWartung() {
		return letzteWartung;
	}
	public void setLetzteWartung(String letzteWartung) {
		this.letzteWartung = letzteWartung;
	}
	public String getLaufleistung4thScreen() {
		return laufleistung4thScreen;
	}
	public void setLaufleistung4thScreen(String laufleistung4thScreen) {
		this.laufleistung4thScreen = laufleistung4thScreen;
	}
	public String getKuhlmittelwechsel1() {
		return kuhlmittelwechsel1;
	}
	public void setKuhlmittelwechsel1(String kuhlmittelwechsel1) {
		this.kuhlmittelwechsel1 = kuhlmittelwechsel1;
	}
	public String getKuhlmittelwechsel2() {
		return kuhlmittelwechsel2;
	}
	public void setKuhlmittelwechsel2(String kuhlmittelwechsel2) {
		this.kuhlmittelwechsel2 = kuhlmittelwechsel2;
	}
	public String getBremsflussigkeitswechsel1() {
		return bremsflussigkeitswechsel1;
	}
	public void setBremsflussigkeitswechsel1(String bremsflussigkeitswechsel1) {
		this.bremsflussigkeitswechsel1 = bremsflussigkeitswechsel1;
	}
	public String getBremsflussigkeitswechsel2() {
		return bremsflussigkeitswechsel2;
	}
	public void setBremsflussigkeitswechsel2(String bremsflussigkeitswechsel2) {
		this.bremsflussigkeitswechsel2 = bremsflussigkeitswechsel2;
	}
	public boolean isAssyst() {
		return assyst;
	}
	public void setAssyst(boolean assyst) {
		this.assyst = assyst;
	}
	public String getArt_l_su() {
		return art_l_su;
	}
	public void setArt_l_su(String art_l_su) {
		this.art_l_su = art_l_su;
	}
	public String getSu_Ba_km() {
		return su_Ba_km;
	}
	public void setSu_Ba_km(String su_Ba_km) {
		this.su_Ba_km = su_Ba_km;
	}
	public String getSu_b_ab() {
		return su_b_ab;
	}
	public void setSu_b_ab(String su_b_ab) {
		this.su_b_ab = su_b_ab;
	}
	public String getKundenNumberVega() {
		return kundenNumberVega;
	}
	public void setKundenNumberVega(String kundenNumberVega) {
		this.kundenNumberVega = kundenNumberVega;
	}
	public String getVegaName() {
		return vegaName;
	}
	public void setVegaName(String vegaName) {
		this.vegaName = vegaName;
	}
	public String getKundenNumberBelegdruck() {
		return kundenNumberBelegdruck;
	}
	public void setKundenNumberBelegdruck(String kundenNumberBelegdruck) {
		this.kundenNumberBelegdruck = kundenNumberBelegdruck;
	}
	public String getBelegdruck_Name() {
		return belegdruck_Name;
	}
	public void setBelegdruck_Name(String belegdruck_Name) {
		this.belegdruck_Name = belegdruck_Name;
	}
	public String getServicevertragVertragsNumber() {
		return servicevertragVertragsNumber;
	}
	public void setServicevertragVertragsNumber(String servicevertragVertragsNumber) {
		this.servicevertragVertragsNumber = servicevertragVertragsNumber;
	}
	public String getLaufzeit1() {
		return laufzeit1;
	}
	public void setLaufzeit1(String laufzeit1) {
		this.laufzeit1 = laufzeit1;
	}
	public String getLaufzeit2() {
		return laufzeit2;
	}
	public void setLaufzeit2(String laufzeit2) {
		this.laufzeit2 = laufzeit2;
	}
	public String getServicevertrag_kmBegrenzung() {
		return servicevertrag_kmBegrenzung;
	}
	public void setServicevertrag_kmBegrenzung(String servicevertrag_kmBegrenzung) {
		this.servicevertrag_kmBegrenzung = servicevertrag_kmBegrenzung;
	}
	public String getVersichererko_KundenNumber() {
		return versichererko_KundenNumber;
	}
	public void setVersichererko_KundenNumber(String versichererko_KundenNumber) {
		this.versichererko_KundenNumber = versichererko_KundenNumber;
	}
	public String getVersichererko_Name() {
		return versichererko_Name;
	}
	public void setVersichererko_Name(String versichererko_Name) {
		this.versichererko_Name = versichererko_Name;
	}
	public String getVersichererko_VertragsNumber() {
		return versichererko_VertragsNumber;
	}
	public void setVersichererko_VertragsNumber(String versichererko_VertragsNumber) {
		this.versichererko_VertragsNumber = versichererko_VertragsNumber;
	}
	public String getVersichererko_laufzeit1() {
		return versichererko_laufzeit1;
	}
	public void setVersichererko_laufzeit1(String versichererko_laufzeit1) {
		this.versichererko_laufzeit1 = versichererko_laufzeit1;
	}
	public String getVersichererko_laufzeit2() {
		return versichererko_laufzeit2;
	}
	public void setVersichererko_laufzeit2(String versichererko_laufzeit2) {
		this.versichererko_laufzeit2 = versichererko_laufzeit2;
	}
	public String getVersichererko_kmBegrenzung() {
		return versichererko_kmBegrenzung;
	}
	public void setVersichererko_kmBegrenzung(String versichererko_kmBegrenzung) {
		this.versichererko_kmBegrenzung = versichererko_kmBegrenzung;
	}
	public String getWeitererVertrag_KundenNumber() {
		return weitererVertrag_KundenNumber;
	}
	public void setWeitererVertrag_KundenNumber(String weitererVertrag_KundenNumber) {
		this.weitererVertrag_KundenNumber = weitererVertrag_KundenNumber;
	}
	public String getWeitererVertrag_Name() {
		return weitererVertrag_Name;
	}
	public void setWeitererVertrag_Name(String weitererVertrag_Name) {
		this.weitererVertrag_Name = weitererVertrag_Name;
	}
	public String getWeitererVertrag_VertragsNumber() {
		return weitererVertrag_VertragsNumber;
	}
	public void setWeitererVertrag_VertragsNumber(String weitererVertrag_VertragsNumber) {
		this.weitererVertrag_VertragsNumber = weitererVertrag_VertragsNumber;
	}
	public String getWeitererVertrag_laufzeit1() {
		return weitererVertrag_laufzeit1;
	}
	public void setWeitererVertrag_laufzeit1(String weitererVertrag_laufzeit1) {
		this.weitererVertrag_laufzeit1 = weitererVertrag_laufzeit1;
	}
	public String getWeitererVertrag_laufzeit2() {
		return weitererVertrag_laufzeit2;
	}
	public void setWeitererVertrag_laufzeit2(String weitererVertrag_laufzeit2) {
		this.weitererVertrag_laufzeit2 = weitererVertrag_laufzeit2;
	}
	public String getWeitererVertrag_kmBegrenzung() {
		return weitererVertrag_kmBegrenzung;
	}
	public void setWeitererVertrag_kmBegrenzung(String weitererVertrag_kmBegrenzung) {
		this.weitererVertrag_kmBegrenzung = weitererVertrag_kmBegrenzung;
	}
	public String getFinanzierunq_KundenNumber() {
		return finanzierunq_KundenNumber;
	}
	public void setFinanzierunq_KundenNumber(String finanzierunq_KundenNumber) {
		this.finanzierunq_KundenNumber = finanzierunq_KundenNumber;
	}
	public String getFinanzierunq_Name() {
		return finanzierunq_Name;
	}
	public void setFinanzierunq_Name(String finanzierunq_Name) {
		this.finanzierunq_Name = finanzierunq_Name;
	}
	public String getFinanzierunq_VertragsNumber() {
		return finanzierunq_VertragsNumber;
	}
	public void setFinanzierunq_VertragsNumber(String finanzierunq_VertragsNumber) {
		this.finanzierunq_VertragsNumber = finanzierunq_VertragsNumber;
	}
	public String getFinanzierunq_laufzeit1() {
		return finanzierunq_laufzeit1;
	}
	public void setFinanzierunq_laufzeit1(String finanzierunq_laufzeit1) {
		this.finanzierunq_laufzeit1 = finanzierunq_laufzeit1;
	}
	public String getFinanzierunq_laufzeit2() {
		return finanzierunq_laufzeit2;
	}
	public void setFinanzierunq_laufzeit2(String finanzierunq_laufzeit2) {
		this.finanzierunq_laufzeit2 = finanzierunq_laufzeit2;
	}
	public String getFinanzierunq_kmBegrenzung() {
		return finanzierunq_kmBegrenzung;
	}
	public void setFinanzierunq_kmBegrenzung(String finanzierunq_kmBegrenzung) {
		this.finanzierunq_kmBegrenzung = finanzierunq_kmBegrenzung;
	}
	public String getFinanzierungsart1() {
		return finanzierungsart1;
	}
	public void setFinanzierungsart1(String finanzierungsart1) {
		this.finanzierungsart1 = finanzierungsart1;
	}
	public String getFinanzierungsart2() {
		return finanzierungsart2;
	}
	public void setFinanzierungsart2(String finanzierungsart2) {
		this.finanzierungsart2 = finanzierungsart2;
	}
	public String getFinanzGesellschaft1() {
		return finanzGesellschaft1;
	}
	public void setFinanzGesellschaft1(String finanzGesellschaft1) {
		this.finanzGesellschaft1 = finanzGesellschaft1;
	}
	public String getFinanzGesellschaft2() {
		return finanzGesellschaft2;
	}
	public void setFinanzGesellschaft2(String finanzGesellschaft2) {
		this.finanzGesellschaft2 = finanzGesellschaft2;
	}
	public String getAktion1() {
		return aktion1;
	}
	public void setAktion1(String aktion1) {
		this.aktion1 = aktion1;
	}
	public String getAktion2() {
		return aktion2;
	}
	public void setAktion2(String aktion2) {
		this.aktion2 = aktion2;
	}
	public String getKartenNumber() {
		return kartenNumber;
	}
	public void setKartenNumber(String kartenNumber) {
		this.kartenNumber = kartenNumber;
	}
	public String getVorteilsaktion_laufzeit1() {
		return vorteilsaktion_laufzeit1;
	}
	public void setVorteilsaktion_laufzeit1(String vorteilsaktion_laufzeit1) {
		this.vorteilsaktion_laufzeit1 = vorteilsaktion_laufzeit1;
	}
	public String getVorteilsaktion_laufzeit2() {
		return vorteilsaktion_laufzeit2;
	}
	public void setVorteilsaktion_laufzeit2(String vorteilsaktion_laufzeit2) {
		this.vorteilsaktion_laufzeit2 = vorteilsaktion_laufzeit2;
	}
	public String getVorteilsaktion_kmBegrenzung() {
		return vorteilsaktion_kmBegrenzung;
	}
	public void setVorteilsaktion_kmBegrenzung(String vorteilsaktion_kmBegrenzung) {
		this.vorteilsaktion_kmBegrenzung = vorteilsaktion_kmBegrenzung;
	}
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getReturnMsg() {
		return returnMsg;
	}
	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}
	public String getPopUpMsg() {
		return popUpMsg;
	}
	public void setPopUpMsg(String popUpMsg) {
		this.popUpMsg = popUpMsg;
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
	public boolean isDruckkennzeichenChb() {
		return druckkennzeichenChb;
	}
	public void setDruckkennzeichenChb(boolean druckkennzeichenChb) {
		this.druckkennzeichenChb = druckkennzeichenChb;
	}
	public boolean isDruckkennzeichenChb1() {
		return druckkennzeichenChb1;
	}
	public void setDruckkennzeichenChb1(boolean druckkennzeichenChb1) {
		this.druckkennzeichenChb1 = druckkennzeichenChb1;
	}
	public boolean isDruckkennzeichenChb2() {
		return druckkennzeichenChb2;
	}
	public void setDruckkennzeichenChb2(boolean druckkennzeichenChb2) {
		this.druckkennzeichenChb2 = druckkennzeichenChb2;
	}
	public boolean isDruckkennzeichenChb3() {
		return druckkennzeichenChb3;
	}
	public void setDruckkennzeichenChb3(boolean druckkennzeichenChb3) {
		this.druckkennzeichenChb3 = druckkennzeichenChb3;
	}
	public boolean isDruckkennzeichenChb4() {
		return druckkennzeichenChb4;
	}
	public void setDruckkennzeichenChb4(boolean druckkennzeichenChb4) {
		this.druckkennzeichenChb4 = druckkennzeichenChb4;
	}
	public boolean isDruckkennzeichenChb5() {
		return druckkennzeichenChb5;
	}
	public void setDruckkennzeichenChb5(boolean druckkennzeichenChb5) {
		this.druckkennzeichenChb5 = druckkennzeichenChb5;
	}
	public boolean isDruckkennzeichenChb6() {
		return druckkennzeichenChb6;
	}
	public void setDruckkennzeichenChb6(boolean druckkennzeichenChb6) {
		this.druckkennzeichenChb6 = druckkennzeichenChb6;
	}
	
    

}
