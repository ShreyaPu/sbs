package com.alphax.vo.mb;

import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about StorageLocationDTO")
public class StorageLocationDTO {
	
	private String storageLocation;
	
	private String priority;
	

	public StorageLocationDTO() {
		
	}

	@ApiModelProperty(value = "Storage LOcation - DB E_LAGO - LOPA")
	@Size(max=8)
	public String getStorageLocation() {
		return storageLocation;
	}


	public void setStorageLocation(String storageLocation) {
		this.storageLocation = storageLocation;
	}


	@ApiModelProperty(value = "Priority - DB E_LAGO - PRIO")
	public String getPriority() {
		return priority;
	}


	public void setPriority(String priority) {
		this.priority = priority;
	}
	

}