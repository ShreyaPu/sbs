package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

public class CustomerContactPersons {

	//Third Screen
	//Birth Date
	@DBTable(columnName ="GEBTAG", required = true) //M_ANSPR - GEBTAG, HPTELNR, TELNR1, MOBTEL, FAXNUM, WEBSID, EMAILA
	private String geburtstag;
	@DBTable(columnName ="HPTELNR", required = true)
	private String haupttelefonNummer;
	// landline phone number 
	@DBTable(columnName ="TELNR1", required = true)
	private String festnetzTelefonnummer;
	//Phone number landline
	@DBTable(columnName ="MOBTEL", required = true)
	private String mobilfunknummer;
	@DBTable(columnName ="FAXNUM", required = true)
	private String faxNummer;
	@DBTable(columnName ="WEBSID", required = true)
	private String webseite;
	@DBTable(columnName ="EMAILA", required = true)
	private String emailAddress;
	@DBTable(columnName ="SA", required = true)
	private BigDecimal sa;
	//DAG data transfer
	@DBTable(columnName ="KZTRANSAG", required = true)
	private String dagDatentransfer;
	
	public CustomerContactPersons() {
	}

	public String getGeburtstag() {
		return geburtstag;
	}

	public void setGeburtstag(String geburtstag) {
		this.geburtstag = geburtstag;
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

	public BigDecimal getSa() {
		return sa;
	}

	public void setSa(BigDecimal sa) {
		this.sa = sa;
	} 
	
	public String getDagDatentransfer() {
		return dagDatentransfer;
	}

	public void setDagDatentransfer(String dagDatentransfer) {
		this.dagDatentransfer = dagDatentransfer;
	}
	
	
}
