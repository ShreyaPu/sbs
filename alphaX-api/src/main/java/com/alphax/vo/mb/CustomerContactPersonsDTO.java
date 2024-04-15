package com.alphax.vo.mb;

import io.swagger.annotations.ApiModelProperty;

public class CustomerContactPersonsDTO {

	//Third Screen
	//Birth Date
	//M_ANSPR - GEBTAG, HPTELNR, TELNR1, MOBTEL, FAXNUM, WEBSID, EMAILA
	@ApiModelProperty(value = "geburtstag( %% - DB M_ANSPR - Lieferant).")
	private String geburtstag;
	
	@ApiModelProperty(value = "haupttelefonNummer ( %% - DB M_ANSPR - GEBTAG).")
	private String haupttelefonNummer;
	
	// landline phone number 
	@ApiModelProperty(value = "festnetzTelefonnummer ( %% - DB M_ANSPR - TELNR1).")
	private String festnetzTelefonnummer;
	
	//Phone number landline
	@ApiModelProperty(value = "mobilfunknummer ( %% - DB M_ANSPR - MOBTEL).")
	private String mobilfunknummer;
	
	@ApiModelProperty(value = "faxNummer ( %% - DB M_ANSPR - FAXNUM).")
	private String faxNummer;
	
	@ApiModelProperty(value = "webseite ( %% - DB M_ANSPR - WEBSID).")
	private String webseite;
	
	@ApiModelProperty(value = "emailAddress ( %% - DB M_ANSPR - EMAILA).")
	private String emailAddress;
	
	@ApiModelProperty(value = "sa ( %% - DB M_ANSPR - SA).")
	private String sa;
	
	//DAG data transfer
	@ApiModelProperty(value = "dagDatentransfer ( %% - DB M_ANSPR - KZTRANSAG).")
	private String dagDatentransfer;
	
	
	public CustomerContactPersonsDTO() {
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

	public String getSa() {
		return sa;
	}

	public void setSa(String sa) {
		this.sa = sa;
	} 
	
	public String getDagDatentransfer() {
		return dagDatentransfer;
	}

	public void setDagDatentransfer(String dagDatentransfer) {
		this.dagDatentransfer = dagDatentransfer;
	}
	
	
}
