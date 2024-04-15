package com.alphax.vo.mb;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All details about Company")
public class Companys {
	
	private String companyAndAgencyId;
	private String vfnr;
	private String dealer;
	private String berufsbezeichnung;
	private String streetNumber;
	private String poBox;
	private String country;
	private String postalCode;
	private String city;
	private String partner;
	private String kennzeichen;
	
	private String companyId;
	private String agencyId;
	
	
	public String getCompanyAndAgencyId() {
		return companyAndAgencyId;
	}
	public void setCompanyAndAgencyId(String companyAndAgencyId) {
		this.companyAndAgencyId = companyAndAgencyId;
	}
	public String getVfnr() {
		return vfnr;
	}
	public void setVfnr(String vfnr) {
		this.vfnr = vfnr;
	}
	public String getDealer() {
		return dealer;
	}
	public void setDealer(String dealer) {
		this.dealer = dealer;
	}
	public String getBerufsbezeichnung() {
		return berufsbezeichnung;
	}
	public void setBerufsbezeichnung(String berufsbezeichnung) {
		this.berufsbezeichnung = berufsbezeichnung;
	}
	public String getStreetNumber() {
		return streetNumber;
	}
	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}
	public String getPoBox() {
		return poBox;
	}
	public void setPoBox(String poBox) {
		this.poBox = poBox;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
	public String getKennzeichen() {
		return kennzeichen;
	}
	public void setKennzeichen(String kennzeichen) {
		this.kennzeichen = kennzeichen;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getAgencyId() {
		return agencyId;
	}
	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}
	

}