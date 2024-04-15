package com.alphax.vo.mb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Search Result for selections on Selektion aus Bewegung")
public class MovementSearchDTO {
	
	private String searchResult;

	/**
	 * @return the searchResult
	 */
	@ApiModelProperty(value = "Search Result ( %% - DB E_CPSDAT/E_HISTO - BELNR or KDNR or BSN_LC or KB||ETNR||ES1||ES2 or MCODE or MARKEN).")
	public String getSearchResult() {
		return searchResult;
	}

	/**
	 * @param searchResult the searchResult to set
	 */
	public void setSearchResult(String searchResult) {
		this.searchResult = searchResult;
	}
	
	

}
