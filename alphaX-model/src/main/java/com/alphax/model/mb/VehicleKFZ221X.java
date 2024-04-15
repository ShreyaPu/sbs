package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

public class VehicleKFZ221X {

	
	@DBTable(columnName ="HMARKE", required = true )
	private String marke;

	@DBTable(columnName ="HERCOD", required = true )
	private String herstellerCode;

	@DBTable(columnName ="TYPCOD", required = true )
	private String typCode;

	@DBTable(columnName ="FGNR", required = true )
	private String getriebeNummer;

	@DBTable(columnName ="FANR", required = true )
	private String achsNummer;

	@DBTable(columnName ="ATMKM", required = true )
	private BigDecimal laufleistAustauschmotor;

	@DBTable(columnName ="FFUHRP", required = true )
	private String fuhrparkNr;

	@DBTable(columnName ="BAUJAHR", required = true )
	private BigDecimal baujahr;

	@DBTable(columnName ="VKL", required = true )
	private BigDecimal vergleichsklasse;

	@DBTable(columnName ="VKSPA", required = true )
	private String verkaufssparte;

	@DBTable(columnName ="FAUFZU", required = true )
	private String auftragsZusatzText;

	@DBTable(columnName ="FBRE_MJ", required = true )
	private BigDecimal sicherheitsprufung1;
	//@DBTable(columnName ="KDNR1", required = true )
	//private String sicherheitsprufung2;
	
	@DBTable(columnName ="UVV_MJ", required = true )
	private BigDecimal unfallverhutungsvorschrift1;
	//@DBTable(columnName ="KDNR1", required = true )
	//private String unfallverhutungsvorschrift2;
	
	@DBTable(columnName ="FTAO", required = true )
	private BigDecimal tachoUntersuchung;
	
	@DBTable(columnName ="FAKL_MJ", required = true )
	private BigDecimal kuhlmittelwechsel1;
	//@DBTable(columnName ="KDNR1", required = true )
	//private String kuhlmittelwechsel2;
	
	@DBTable(columnName ="FABF_MJ", required = true )
	private BigDecimal bremsflussigkeitswechsel1;
	//@DBTable(columnName ="KDNR1", required = true )
	//private String bremsflussigkeitswechsel2;
	
	@DBTable(columnName ="ASSYST", required = true )
	private String assyst;
	
	@DBTable(columnName ="ARTLWD", required = true )
	private String art_l_su;
	
	@DBTable(columnName ="WDABKM", required = true )
	private BigDecimal su_Ba_km;
	
	@DBTable(columnName ="WDAB", required = true )
	private BigDecimal su_b_ab;

	@DBTable(columnName ="SERKDNR", required = true )
	private String kundenNumberVega;
	//@DBTable(columnName ="KDNR1", required = true )
	//private String vegaName;
	
	@DBTable(columnName ="SERKDNR2", required = true )
	private String kundenNumberBelegdruck;
	//@DBTable(columnName ="KDNR1", required = true )
	//private String belegdruck_Name;
	
	@DBTable(columnName ="SERVNR", required = true )
	private String servicevertragVertragsNumber;
	
	@DBTable(columnName ="SERDTB", required = true )
	private BigDecimal laufzeit1;
	
	@DBTable(columnName ="SERDTE", required = true )
	private BigDecimal laufzeit2;
	
	@DBTable(columnName ="SERKM", required = true )
	private BigDecimal servicevertrag_kmBegrenzung;

	@DBTable(columnName ="VSKOKDNR", required = true )
	private String versichererko_KundenNumber;
	//@DBTable(columnName ="KDNR1", required = true )
	//private String versichererko_Name;
	
	@DBTable(columnName ="VSKOVNR", required = true )
	private String versichererko_VertragsNumber;
	
	@DBTable(columnName ="VSKODTBEG", required = true )
	private BigDecimal versichererko_laufzeit1;
	
	@DBTable(columnName ="VSKODTEND", required = true )
	private BigDecimal versichererko_laufzeit2;
	
	@DBTable(columnName ="VSKOKM", required = true )
	private BigDecimal versichererko_kmBegrenzung;

	@DBTable(columnName ="WTRVKDNR", required = true )
	private String weitererVertrag_KundenNumber;
	//@DBTable(columnName ="KDNR1", required = true )
	//private String weitererVertrag_Name;
	
	@DBTable(columnName ="WTRVVNR", required = true )
	private String weitererVertrag_VertragsNumber;
	
	@DBTable(columnName ="WTRVDTBEG", required = true )
	private BigDecimal weitererVertrag_laufzeit1;
	
	@DBTable(columnName ="WTRVDTEND", required = true )
	private BigDecimal weitererVertrag_laufzeit2;
	
	@DBTable(columnName ="WTRVKM", required = true )
	private BigDecimal weitererVertrag_kmBegrenzung;

	@DBTable(columnName ="FINKDNR", required = true )
	private String finanzierunq_KundenNumber;
	//@DBTable(columnName ="KDNR1", required = true )
	//private String finanzierunq_Name;

	@DBTable(columnName ="FINVNR", required = true )
	private String finanzierunq_VertragsNumber;
	
	@DBTable(columnName ="FINDTBEG", required = true )
	private BigDecimal finanzierunq_laufzeit1;
	
	@DBTable(columnName ="FINDTEND", required = true )
	private BigDecimal finanzierunq_laufzeit2;
	
	@DBTable(columnName ="FINKM", required = true )
	private BigDecimal finanzierunq_kmBegrenzung;
	
	@DBTable(columnName ="FINART", required = true )
	private String finanzierungsart1;
	//private String finanzierungsart2;
	
	@DBTable(columnName ="FINGES", required = true )
	private String finanzGesellschaft1;
	//private String finanzGesellschaft2;

	@DBTable(columnName ="VORAAKTION", required = true )
	private String aktion1;
	//private String aktion2;
	
	@DBTable(columnName ="VORAVNR", required = true )
	private String kartenNumber;
	
	@DBTable(columnName ="VORADTBEG", required = true )
	private BigDecimal vorteilsaktion_laufzeit1;
	
	@DBTable(columnName ="VORADTEND", required = true )
	private BigDecimal vorteilsaktion_laufzeit2;
	
	@DBTable(columnName ="VORAKM", required = true )
	private BigDecimal vorteilsaktion_kmBegrenzung;
	
	@DBTable(columnName ="FZWI_TT", required = true )
	private BigDecimal lieferUbernahmedatum_dd;
	
	@DBTable(columnName ="FZWI_MJ", required = true )
	private BigDecimal lieferUbernahmedatum_mmyy;

	public VehicleKFZ221X() {
		
	}

	public String getMarke() {
		return marke;
	}

	public void setMarke(String marke) {
		this.marke = marke;
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

	public BigDecimal getLaufleistAustauschmotor() {
		return laufleistAustauschmotor;
	}

	public void setLaufleistAustauschmotor(BigDecimal laufleistAustauschmotor) {
		this.laufleistAustauschmotor = laufleistAustauschmotor;
	}

	public String getFuhrparkNr() {
		return fuhrparkNr;
	}

	public void setFuhrparkNr(String fuhrparkNr) {
		this.fuhrparkNr = fuhrparkNr;
	}

	public BigDecimal getBaujahr() {
		return baujahr;
	}

	public void setBaujahr(BigDecimal baujahr) {
		this.baujahr = baujahr;
	}

	public BigDecimal getVergleichsklasse() {
		return vergleichsklasse;
	}

	public void setVergleichsklasse(BigDecimal vergleichsklasse) {
		this.vergleichsklasse = vergleichsklasse;
	}

	public String getVerkaufssparte() {
		return verkaufssparte;
	}

	public void setVerkaufssparte(String verkaufssparte) {
		this.verkaufssparte = verkaufssparte;
	}

	public String getAuftragsZusatzText() {
		return auftragsZusatzText;
	}

	public void setAuftragsZusatzText(String auftragsZusatzText) {
		this.auftragsZusatzText = auftragsZusatzText;
	}

	public BigDecimal getSicherheitsprufung1() {
		return sicherheitsprufung1;
	}

	public void setSicherheitsprufung1(BigDecimal sicherheitsprufung1) {
		this.sicherheitsprufung1 = sicherheitsprufung1;
	}

	public BigDecimal getUnfallverhutungsvorschrift1() {
		return unfallverhutungsvorschrift1;
	}

	public void setUnfallverhutungsvorschrift1(BigDecimal unfallverhutungsvorschrift1) {
		this.unfallverhutungsvorschrift1 = unfallverhutungsvorschrift1;
	}

	public BigDecimal getTachoUntersuchung() {
		return tachoUntersuchung;
	}

	public void setTachoUntersuchung(BigDecimal tachoUntersuchung) {
		this.tachoUntersuchung = tachoUntersuchung;
	}

	public BigDecimal getKuhlmittelwechsel1() {
		return kuhlmittelwechsel1;
	}

	public void setKuhlmittelwechsel1(BigDecimal kuhlmittelwechsel1) {
		this.kuhlmittelwechsel1 = kuhlmittelwechsel1;
	}

	public BigDecimal getBremsflussigkeitswechsel1() {
		return bremsflussigkeitswechsel1;
	}

	public void setBremsflussigkeitswechsel1(BigDecimal bremsflussigkeitswechsel1) {
		this.bremsflussigkeitswechsel1 = bremsflussigkeitswechsel1;
	}

	public String getAssyst() {
		return assyst;
	}

	public void setAssyst(String assyst) {
		this.assyst = assyst;
	}

	public String getArt_l_su() {
		return art_l_su;
	}

	public void setArt_l_su(String art_l_su) {
		this.art_l_su = art_l_su;
	}

	public BigDecimal getSu_Ba_km() {
		return su_Ba_km;
	}

	public void setSu_Ba_km(BigDecimal su_Ba_km) {
		this.su_Ba_km = su_Ba_km;
	}

	public BigDecimal getSu_b_ab() {
		return su_b_ab;
	}

	public void setSu_b_ab(BigDecimal su_b_ab) {
		this.su_b_ab = su_b_ab;
	}

	public String getKundenNumberVega() {
		return kundenNumberVega;
	}

	public void setKundenNumberVega(String kundenNumberVega) {
		this.kundenNumberVega = kundenNumberVega;
	}

	public String getKundenNumberBelegdruck() {
		return kundenNumberBelegdruck;
	}

	public void setKundenNumberBelegdruck(String kundenNumberBelegdruck) {
		this.kundenNumberBelegdruck = kundenNumberBelegdruck;
	}

	public String getServicevertragVertragsNumber() {
		return servicevertragVertragsNumber;
	}

	public void setServicevertragVertragsNumber(String servicevertragVertragsNumber) {
		this.servicevertragVertragsNumber = servicevertragVertragsNumber;
	}

	public BigDecimal getLaufzeit1() {
		return laufzeit1;
	}

	public void setLaufzeit1(BigDecimal laufzeit1) {
		this.laufzeit1 = laufzeit1;
	}

	public BigDecimal getLaufzeit2() {
		return laufzeit2;
	}

	public void setLaufzeit2(BigDecimal laufzeit2) {
		this.laufzeit2 = laufzeit2;
	}

	public BigDecimal getServicevertrag_kmBegrenzung() {
		return servicevertrag_kmBegrenzung;
	}

	public void setServicevertrag_kmBegrenzung(BigDecimal servicevertrag_kmBegrenzung) {
		this.servicevertrag_kmBegrenzung = servicevertrag_kmBegrenzung;
	}

	public String getVersichererko_KundenNumber() {
		return versichererko_KundenNumber;
	}

	public void setVersichererko_KundenNumber(String versichererko_KundenNumber) {
		this.versichererko_KundenNumber = versichererko_KundenNumber;
	}

	public String getVersichererko_VertragsNumber() {
		return versichererko_VertragsNumber;
	}

	public void setVersichererko_VertragsNumber(String versichererko_VertragsNumber) {
		this.versichererko_VertragsNumber = versichererko_VertragsNumber;
	}

	public BigDecimal getVersichererko_laufzeit1() {
		return versichererko_laufzeit1;
	}

	public void setVersichererko_laufzeit1(BigDecimal versichererko_laufzeit1) {
		this.versichererko_laufzeit1 = versichererko_laufzeit1;
	}

	public BigDecimal getVersichererko_laufzeit2() {
		return versichererko_laufzeit2;
	}

	public void setVersichererko_laufzeit2(BigDecimal versichererko_laufzeit2) {
		this.versichererko_laufzeit2 = versichererko_laufzeit2;
	}

	public BigDecimal getVersichererko_kmBegrenzung() {
		return versichererko_kmBegrenzung;
	}

	public void setVersichererko_kmBegrenzung(BigDecimal versichererko_kmBegrenzung) {
		this.versichererko_kmBegrenzung = versichererko_kmBegrenzung;
	}

	public String getWeitererVertrag_KundenNumber() {
		return weitererVertrag_KundenNumber;
	}

	public void setWeitererVertrag_KundenNumber(String weitererVertrag_KundenNumber) {
		this.weitererVertrag_KundenNumber = weitererVertrag_KundenNumber;
	}

	public String getWeitererVertrag_VertragsNumber() {
		return weitererVertrag_VertragsNumber;
	}

	public void setWeitererVertrag_VertragsNumber(String weitererVertrag_VertragsNumber) {
		this.weitererVertrag_VertragsNumber = weitererVertrag_VertragsNumber;
	}

	public BigDecimal getWeitererVertrag_laufzeit1() {
		return weitererVertrag_laufzeit1;
	}

	public void setWeitererVertrag_laufzeit1(BigDecimal weitererVertrag_laufzeit1) {
		this.weitererVertrag_laufzeit1 = weitererVertrag_laufzeit1;
	}

	public BigDecimal getWeitererVertrag_laufzeit2() {
		return weitererVertrag_laufzeit2;
	}

	public void setWeitererVertrag_laufzeit2(BigDecimal weitererVertrag_laufzeit2) {
		this.weitererVertrag_laufzeit2 = weitererVertrag_laufzeit2;
	}

	public BigDecimal getWeitererVertrag_kmBegrenzung() {
		return weitererVertrag_kmBegrenzung;
	}

	public void setWeitererVertrag_kmBegrenzung(BigDecimal weitererVertrag_kmBegrenzung) {
		this.weitererVertrag_kmBegrenzung = weitererVertrag_kmBegrenzung;
	}

	public String getFinanzierunq_KundenNumber() {
		return finanzierunq_KundenNumber;
	}

	public void setFinanzierunq_KundenNumber(String finanzierunq_KundenNumber) {
		this.finanzierunq_KundenNumber = finanzierunq_KundenNumber;
	}

	public String getFinanzierunq_VertragsNumber() {
		return finanzierunq_VertragsNumber;
	}

	public void setFinanzierunq_VertragsNumber(String finanzierunq_VertragsNumber) {
		this.finanzierunq_VertragsNumber = finanzierunq_VertragsNumber;
	}

	public BigDecimal getFinanzierunq_laufzeit1() {
		return finanzierunq_laufzeit1;
	}

	public void setFinanzierunq_laufzeit1(BigDecimal finanzierunq_laufzeit1) {
		this.finanzierunq_laufzeit1 = finanzierunq_laufzeit1;
	}

	public BigDecimal getFinanzierunq_laufzeit2() {
		return finanzierunq_laufzeit2;
	}

	public void setFinanzierunq_laufzeit2(BigDecimal finanzierunq_laufzeit2) {
		this.finanzierunq_laufzeit2 = finanzierunq_laufzeit2;
	}

	public BigDecimal getFinanzierunq_kmBegrenzung() {
		return finanzierunq_kmBegrenzung;
	}

	public void setFinanzierunq_kmBegrenzung(BigDecimal finanzierunq_kmBegrenzung) {
		this.finanzierunq_kmBegrenzung = finanzierunq_kmBegrenzung;
	}

	public String getFinanzierungsart1() {
		return finanzierungsart1;
	}

	public void setFinanzierungsart1(String finanzierungsart1) {
		this.finanzierungsart1 = finanzierungsart1;
	}

	public String getFinanzGesellschaft1() {
		return finanzGesellschaft1;
	}

	public void setFinanzGesellschaft1(String finanzGesellschaft1) {
		this.finanzGesellschaft1 = finanzGesellschaft1;
	}

	public String getAktion1() {
		return aktion1;
	}

	public void setAktion1(String aktion1) {
		this.aktion1 = aktion1;
	}

	public String getKartenNumber() {
		return kartenNumber;
	}

	public void setKartenNumber(String kartenNumber) {
		this.kartenNumber = kartenNumber;
	}

	public BigDecimal getVorteilsaktion_laufzeit1() {
		return vorteilsaktion_laufzeit1;
	}

	public void setVorteilsaktion_laufzeit1(BigDecimal vorteilsaktion_laufzeit1) {
		this.vorteilsaktion_laufzeit1 = vorteilsaktion_laufzeit1;
	}

	public BigDecimal getVorteilsaktion_laufzeit2() {
		return vorteilsaktion_laufzeit2;
	}

	public void setVorteilsaktion_laufzeit2(BigDecimal vorteilsaktion_laufzeit2) {
		this.vorteilsaktion_laufzeit2 = vorteilsaktion_laufzeit2;
	}

	public BigDecimal getVorteilsaktion_kmBegrenzung() {
		return vorteilsaktion_kmBegrenzung;
	}

	public void setVorteilsaktion_kmBegrenzung(BigDecimal vorteilsaktion_kmBegrenzung) {
		this.vorteilsaktion_kmBegrenzung = vorteilsaktion_kmBegrenzung;
	}

	public BigDecimal getLieferUbernahmedatum_dd() {
		return lieferUbernahmedatum_dd;
	}

	public void setLieferUbernahmedatum_dd(BigDecimal lieferUbernahmedatum_dd) {
		this.lieferUbernahmedatum_dd = lieferUbernahmedatum_dd;
	}

	public BigDecimal getLieferUbernahmedatum_mmyy() {
		return lieferUbernahmedatum_mmyy;
	}

	public void setLieferUbernahmedatum_mmyy(BigDecimal lieferUbernahmedatum_mmyy) {
		this.lieferUbernahmedatum_mmyy = lieferUbernahmedatum_mmyy;
	}

	
	
	
}
