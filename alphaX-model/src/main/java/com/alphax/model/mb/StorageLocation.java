package com.alphax.model.mb;


import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about StorageLocation")
public class StorageLocation {
	
	@DBTable(columnName = "LOPA", required = true)
	private String storageLocation;
	
	@DBTable(columnName = "PRIO", required = true)
	private BigDecimal priority;
	

	public StorageLocation() {
		
	}


	public String getStorageLocation() {
		return storageLocation;
	}


	public void setStorageLocation(String storageLocation) {
		this.storageLocation = storageLocation;
	}


	public BigDecimal getPriority() {
		return priority;
	}


	public void setPriority(BigDecimal priority) {
		this.priority = priority;
	}
	
	
	

}
