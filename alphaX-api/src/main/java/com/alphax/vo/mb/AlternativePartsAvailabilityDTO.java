package com.alphax.vo.mb;

import io.swagger.annotations.ApiModelProperty;

public class AlternativePartsAvailabilityDTO {
	
	private String partNumber;
	
	private Boolean partHistory; 

	
	/**
	 * @return the partNumber
	 */
	@ApiModelProperty(value = "Part Number (Teilenummer) ( %% - DB E_ESKDAT - ET_ETNR).")
	public String getPartNumber() {
		return partNumber;
	}


	/**
	 * @param partNumber the partNumber to set
	 */
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	/**
	 * @return the partHistoryAvailable
	 */
	@ApiModelProperty(value = "Boolean value to check part predecessor or successor is available or not")
	public Boolean isPartHistory() {
		return partHistory;
	}

	/**
	 * @param partHistoryAvailable the partHistoryAvailable to set
	 */
	public void setPartHistory(Boolean partHistory) {
		this.partHistory = partHistory;
	}
	
}
