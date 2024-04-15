package com.alphax.vo.mb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about CountingGroupDTO")
public class CountingGroupDTO {
	
	@ApiModelProperty(value = "Counting Group (Zählgruppe) - DB E_ETSTAMM - ZAEGRU")
	private String countingGroup;

	/**
	 * @return the countingGroup
	 */
	public String getCountingGroup() {
		return countingGroup;
	}

	/**
	 * @param countingGroup the countingGroup to set
	 */
	public void setCountingGroup(String countingGroup) {
		this.countingGroup = countingGroup;
	}
	

}
