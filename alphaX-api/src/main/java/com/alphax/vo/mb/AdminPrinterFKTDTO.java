package com.alphax.vo.mb;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about AlphaX Admin setting for Printer FKT details")
public class AdminPrinterFKTDTO {

	private String fktId;
	
	private String fktValue;
	
	private boolean hasActiveFKT;
	
	
	public AdminPrinterFKTDTO() {	
	}
	

	/**
	 * @return the fktId
	 */
	@ApiModelProperty(value = "Printer FKT Id ( %% - DB O_PRTFKT - FKT_ID ).")
	public String getFktId() {
		return fktId;
	}

	/**
	 * @param fktId the fktId to set
	 */
	public void setFktId(String fktId) {
		this.fktId = fktId;
	}

	
	/**
	 * @return the fktValue
	 */
	@ApiModelProperty(value = "Printer FKT description ( %% - DB O_PRTFKT - FKT ).")
	public String getFktValue() {
		return fktValue;
	}

	/**
	 * @param fktValue the fktValue to set
	 */
	public void setFktValue(String fktValue) {
		this.fktValue = fktValue;
	}
	

	/**
	 * @return the hasActiveFKT
	 */
	@ApiModelProperty(value = "Printer FKT description ( %% - DB O_PRTHASFKT - HAS_FKT ).")
	public boolean isHasActiveFKT() {
		return hasActiveFKT;
	}

	/**
	 * @param hasActiveFKT the hasActiveFKT to set
	 */
	public void setHasActiveFKT(boolean hasActiveFKT) {
		this.hasActiveFKT = hasActiveFKT;
	}

	
}