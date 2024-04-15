package com.alphax.vo.mb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about PartsBrandDTO")
public class PartsBrandDTO {

	@ApiModelProperty(value ="Part Brand - (DB PMH_TMARKE - TMA_TMARKE)" )
	private String partBrand;

	/**
	 * @return the partBrand
	 */
	public String getPartBrand() {
		return partBrand;
	}

	/**
	 * @param partBrand the partBrand to set
	 */
	public void setPartBrand(String partBrand) {
		this.partBrand = partBrand;
	}
	
	
}
