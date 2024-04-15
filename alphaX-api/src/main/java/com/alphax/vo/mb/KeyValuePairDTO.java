package com.alphax.vo.mb;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about key value pair Details")
public class KeyValuePairDTO {

	private String codeValue;
	
	private String code;

	
	/**
	 * @return the codeValue
	 */
	@ApiModelProperty(value = "Code Value ( %% - DB ETHTXTTAB - TEXT).")
	public String getCodeValue() {
		return codeValue;
	}

	public void setCodeValue(String codeValue) {
		this.codeValue = codeValue;
	}

	/**
	 * @return the code
	 */
	@ApiModelProperty(value = "Code ( %% - DB ETHTXTTAB - CODE).")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
}
