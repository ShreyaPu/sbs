package com.alphax.vo.mb;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about SearchCustomer")
public class SearchCustomerDTO {

	//Customer Number
	@ApiModelProperty(value = "The ID of the customer for further processing")
	private String kundenNummer;

	//Salutation
	@ApiModelProperty(value = "The salutation of the customer.")
	private String anrede;

	//First Name
	@ApiModelProperty(value = "The first name of the customer.")
	private String name2;

	//Last name
	@ApiModelProperty(value = "The last name of the customer.")
	private String name;

	//Postcode
	@ApiModelProperty(value = "The postal code for the city the customer is placed.")
	private String plz;

	//City
	@ApiModelProperty(value = "The name of the city the customer is placed.")
	private String ort;

	//Street
	@ApiModelProperty(value = "The street where the customer is placed.")
	private String strasse;
	
	//kzst
	@ApiModelProperty(value = "The kzst value for the customer.")
	private String kzst;

	@ApiModelProperty(value = "The privatKunde value use to check the customer is private or company.")
	private boolean privatKunde;


	public SearchCustomerDTO() {

	}


	/**
	 * @return the kundenNummer
	 */
	public String getKundenNummer() {
		return kundenNummer;
	}


	/**
	 * @param kundenNummer the kundenNummer to set
	 */
	public void setKundenNummer(String kundenNummer) {
		this.kundenNummer = kundenNummer;
	}


	/**
	 * @return the anrede
	 */
	public String getAnrede() {
		return anrede;
	}


	/**
	 * @param anrede the anrede to set
	 */
	public void setAnrede(String anrede) {
		this.anrede = anrede;
	}


	

	public String getName2() {
		return name2;
	}


	public void setName2(String name2) {
		this.name2 = name2;
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the plz
	 */
	public String getPlz() {
		return plz;
	}


	/**
	 * @param plz the plz to set
	 */
	public void setPlz(String plz) {
		this.plz = plz;
	}


	/**
	 * @return the ort
	 */
	public String getOrt() {
		return ort;
	}


	/**
	 * @param ort the ort to set
	 */
	public void setOrt(String ort) {
		this.ort = ort;
	}


	/**
	 * @return the strasse
	 */
	public String getStrasse() {
		return strasse;
	}


	/**
	 * @param strasse the strasse to set
	 */
	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}


	/**
	 * @return the kzst
	 */
	public String getKzst() {
		return kzst;
	}


	/**
	 * @param kzst the kzst to set
	 */
	public void setKzst(String kzst) {
		this.kzst = kzst;
	}


	public boolean isPrivatKunde() {
		return privatKunde;
	}


	public void setPrivatKunde(boolean privatKunde) {
		this.privatKunde = privatKunde;
	}

	
}