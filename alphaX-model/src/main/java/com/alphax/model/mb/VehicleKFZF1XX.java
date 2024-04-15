package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

public class VehicleKFZF1XX {

	@DBTable(columnName ="FKENNZ7", required = true )
	private String amtlichesKennzeichen;

	@DBTable(columnName ="KDNR1", required = true )
	private String kundenNummer;

	@DBTable(columnName ="WHC", required = true )
	private String fzIdentnr1;

	@DBTable(columnName ="BAUM", required = true )
	private String fzIdentnr2;
	//@DBTable(columnName ="BAUM", required = true )
	//private String fzIdentnr3;

	@DBTable(columnName ="ENDN", required = true )
	private String fzIdentnr4;

	@DBTable(columnName ="FGP", required = true )
	private String fzIdentnr5;

	@DBTable(columnName ="MOTN", required = true )
	private String motorNummer;

	@DBTable(columnName ="BUCH1", required = true )
	private String familienGruppe;

	@DBTable(columnName ="SPAL1", required = true )
	private String typenkennzahl;

	@DBTable(columnName ="TYPNR", required = true )
	private String typNummer;

	@DBTable(columnName ="TYPVA", required = true )
	private String typVariante;

	@DBTable(columnName ="KW", required = true )
	private BigDecimal  motorstarkeKW;

	@DBTable(columnName ="HUB", required = true )
	private BigDecimal hubraumZul_ges_gew;

	@DBTable(columnName ="TYP", required = true )
	private String fahrzeugTyp;

	@DBTable(columnName ="TYPGR", required = true )
	private String fahrzeugTypgruppe;

	@DBTable(columnName ="FART", required = true )
	private String fahrzeugArt;

	@DBTable(columnName ="DTLIP", required = true )
	private BigDecimal letzteZulassung;

	@DBTable(columnName ="DTEZU", required = true )
	private BigDecimal ersteZulassung;

	@DBTable(columnName ="ZUART", required = true )
	private String zulassungsart;

	@DBTable(columnName ="KZEV", required = true )
	private String eigenverkauf;

	@DBTable(columnName ="PLATZ", required = true )
	private BigDecimal nutzlastAnz_platze ;
	
	@DBTable(columnName ="ANTA", required = true )
	private String antriebsart;

	@DBTable(columnName ="ANZVO", required = true )
	private BigDecimal anzahlVorbesitzer;

	@DBTable(columnName ="GKM", required = true )
	private BigDecimal laufleistungKennzeichen;

	@DBTable(columnName ="KZLF", required = true )
	private String laufleistung;

	@DBTable(columnName ="VERK", required = true )
	private String verkaufer;

	@DBTable(columnName ="AUFN", required = true )
	private String aufragsNummer;

	@DBTable(columnName ="WAGR", required = true )
	private String wartungsGruppe;

	@DBTable(columnName ="WAVT_TBL", required = true )
	private String wartungsVertrag;

	@DBTable(columnName ="DTBES", required = true )
	private BigDecimal bestellDatum;

	//@DBTable(columnName ="DTLIP", required = true )
	//private BigDecimal lieferUbernahmedatum;

	@DBTable(columnName ="KDBE", required = true )
	private String kundendienstBerater;

	@DBTable(columnName ="UNFAL", required = true )
	private String unfall;

	@DBTable(columnName ="WSTN", required = true )
	private String betreuendeWerkstatt;

	@DBTable(columnName ="TEXT", required = true )
	private String infoText;

	@DBTable(columnName ="DTTUV", required = true )
	private BigDecimal hauptUntersuchung1;
	//@DBTable(columnName ="DTTUV", required = true )
	//private String hauptUntersuchung2;
	
	@DBTable(columnName ="KZTUV", required = true )
	private String huKennzeichen;

	@DBTable(columnName ="ANZWE", required = true )
	private BigDecimal anzahlWerkstattbesuche;
	
	@DBTable(columnName ="DTLWE", required = true )
	private BigDecimal letzterWerkstattbesuch;
	
	@DBTable(columnName ="KMLWE", required = true )
	private BigDecimal laufleistun;
	
	@DBTable(columnName ="DTLWA", required = true )
	private BigDecimal letzteWartung;
	
	@DBTable(columnName ="KMLWA", required = true )
	private BigDecimal laufleistung4thScreen;
	
	@DBTable(columnName ="KZAQ", required = true )
	private String akquisitionsperre;

	public VehicleKFZF1XX() {
		
	}

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

	public BigDecimal getMotorstarkeKW() {
		return motorstarkeKW;
	}

	public void setMotorstarkeKW(BigDecimal motorstarkeKW) {
		this.motorstarkeKW = motorstarkeKW;
	}

	public BigDecimal getHubraumZul_ges_gew() {
		return hubraumZul_ges_gew;
	}

	public void setHubraumZul_ges_gew(BigDecimal hubraumZul_ges_gew) {
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

	public BigDecimal getLetzteZulassung() {
		return letzteZulassung;
	}

	public void setLetzteZulassung(BigDecimal letzteZulassung) {
		this.letzteZulassung = letzteZulassung;
	}

	public BigDecimal getErsteZulassung() {
		return ersteZulassung;
	}

	public void setErsteZulassung(BigDecimal ersteZulassung) {
		this.ersteZulassung = ersteZulassung;
	}

	public String getZulassungsart() {
		return zulassungsart;
	}

	public void setZulassungsart(String zulassungsart) {
		this.zulassungsart = zulassungsart;
	}

	public String getEigenverkauf() {
		return eigenverkauf;
	}

	public void setEigenverkauf(String eigenverkauf) {
		this.eigenverkauf = eigenverkauf;
	}

	public BigDecimal getNutzlastAnz_platze() {
		return nutzlastAnz_platze;
	}

	public void setNutzlastAnz_platze(BigDecimal nutzlastAnz_platze) {
		this.nutzlastAnz_platze = nutzlastAnz_platze;
	}

	public String getAntriebsart() {
		return antriebsart;
	}

	public void setAntriebsart(String antriebsart) {
		this.antriebsart = antriebsart;
	}

	public BigDecimal getAnzahlVorbesitzer() {
		return anzahlVorbesitzer;
	}

	public void setAnzahlVorbesitzer(BigDecimal anzahlVorbesitzer) {
		this.anzahlVorbesitzer = anzahlVorbesitzer;
	}

	public BigDecimal getLaufleistungKennzeichen() {
		return laufleistungKennzeichen;
	}

	public void setLaufleistungKennzeichen(BigDecimal laufleistungKennzeichen) {
		this.laufleistungKennzeichen = laufleistungKennzeichen;
	}

	public String getLaufleistung() {
		return laufleistung;
	}

	public void setLaufleistung(String laufleistung) {
		this.laufleistung = laufleistung;
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

	public String getWartungsVertrag() {
		return wartungsVertrag;
	}

	public void setWartungsVertrag(String wartungsVertrag) {
		this.wartungsVertrag = wartungsVertrag;
	}

	public BigDecimal getBestellDatum() {
		return bestellDatum;
	}

	public void setBestellDatum(BigDecimal bestellDatum) {
		this.bestellDatum = bestellDatum;
	}

	/*public BigDecimal getLieferUbernahmedatum() {
		return lieferUbernahmedatum;
	}

	public void setLieferUbernahmedatum(BigDecimal lieferUbernahmedatum) {
		this.lieferUbernahmedatum = lieferUbernahmedatum;
	}*/

	public String getKundendienstBerater() {
		return kundendienstBerater;
	}

	public void setKundendienstBerater(String kundendienstBerater) {
		this.kundendienstBerater = kundendienstBerater;
	}

	public String getUnfall() {
		return unfall;
	}

	public void setUnfall(String unfall) {
		this.unfall = unfall;
	}

	public String getBetreuendeWerkstatt() {
		return betreuendeWerkstatt;
	}

	public void setBetreuendeWerkstatt(String betreuendeWerkstatt) {
		this.betreuendeWerkstatt = betreuendeWerkstatt;
	}

	public String getInfoText() {
		return infoText;
	}

	public void setInfoText(String infoText) {
		this.infoText = infoText;
	}

	public BigDecimal getHauptUntersuchung1() {
		return hauptUntersuchung1;
	}

	public void setHauptUntersuchung1(BigDecimal hauptUntersuchung1) {
		this.hauptUntersuchung1 = hauptUntersuchung1;
	}

	public String getHuKennzeichen() {
		return huKennzeichen;
	}

	public void setHuKennzeichen(String huKennzeichen) {
		this.huKennzeichen = huKennzeichen;
	}

	public BigDecimal getAnzahlWerkstattbesuche() {
		return anzahlWerkstattbesuche;
	}

	public void setAnzahlWerkstattbesuche(BigDecimal anzahlWerkstattbesuche) {
		this.anzahlWerkstattbesuche = anzahlWerkstattbesuche;
	}

	public BigDecimal getLetzterWerkstattbesuch() {
		return letzterWerkstattbesuch;
	}

	public void setLetzterWerkstattbesuch(BigDecimal letzterWerkstattbesuch) {
		this.letzterWerkstattbesuch = letzterWerkstattbesuch;
	}

	public BigDecimal getLaufleistun() {
		return laufleistun;
	}

	public void setLaufleistun(BigDecimal laufleistun) {
		this.laufleistun = laufleistun;
	}

	public BigDecimal getLetzteWartung() {
		return letzteWartung;
	}

	public void setLetzteWartung(BigDecimal letzteWartung) {
		this.letzteWartung = letzteWartung;
	}

	public BigDecimal getLaufleistung4thScreen() {
		return laufleistung4thScreen;
	}

	public void setLaufleistung4thScreen(BigDecimal laufleistung4thScreen) {
		this.laufleistung4thScreen = laufleistung4thScreen;
	}
	public String getAkquisitionsperre() {
		return akquisitionsperre;
	}

	public void setAkquisitionsperre(String akquisitionsperre) {
		this.akquisitionsperre = akquisitionsperre;
	}
	
}
