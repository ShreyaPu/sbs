
package com.alphax.vo.mb;

import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author A106744104
 *
 */
@ApiModel(description = "One of 12 optional repair codes that offer further "
		+ "information for the vehicle legend item (%% DB M_KFZL - 11-22 - "
		+ " REPKZ01-REPKZ12).")
public class VehicleLegendRepairCode {

	private String name;

	private String value;

	public VehicleLegendRepairCode() {

	}


	/**
	 * @return the name
	 */
	@ApiModelProperty(value = "the repair code field name. \"Reparaturkennzeichen 1\" to \"Reparaturkennzeichen 12\".")
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
	 * @return the value
	 */
	@ApiModelProperty(value = "the value of the repair code. e.g. \"SA\" ")
	@Size(max=2)
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}



}
