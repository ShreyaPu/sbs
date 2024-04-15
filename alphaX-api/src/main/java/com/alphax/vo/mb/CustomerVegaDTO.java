package com.alphax.vo.mb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about SearchCustomer_CB")
public class CustomerVegaDTO {

	//Customer Number
	@ApiModelProperty(value = "The ID of the customer for further processing.")
	private String kundenNummer;

	//Customer Name
	@ApiModelProperty(value = "The Name of the customer.")
	private String name;

	//CW/SW
	@ApiModelProperty(value = "The cw/sv details.")
	private String cw_sv;

	//Date validity From
	@ApiModelProperty(value = "The Validity From Date in dd.MM.YYYY")
	private String validityFrom;

	//Date validity To
	@ApiModelProperty(value = "The Validity To Date in dd.MM.YYYY")
	private String validityTo;

	
	public CustomerVegaDTO() {

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
	 * @return the cw_sv
	 */
	public String getCw_sv() {
		return cw_sv;
	}

	/**
	 * @param cw_sv the cw_sv to set
	 */
	public void setCw_sv(String cw_sv) {
		this.cw_sv = cw_sv;
	}

	/**
	 * @return the validityFrom
	 */
	public String getValidityFrom() {
		return validityFrom;
	}

	/**
	 * @param validityFrom the validityFrom to set
	 */
	public void setValidityFrom(String validityFrom) {
		this.validityFrom = validityFrom;
	}

	/**
	 * @return the validityTo
	 */
	public String getValidityTo() {
		return validityTo;
	}

	/**
	 * @param validityTo the validityTo to set
	 */
	public void setValidityTo(String validityTo) {
		this.validityTo = validityTo;
	}

}