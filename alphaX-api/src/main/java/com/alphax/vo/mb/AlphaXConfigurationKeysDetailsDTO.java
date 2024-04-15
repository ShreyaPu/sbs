package com.alphax.vo.mb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about AlphaX configuration Keys details")
public class AlphaXConfigurationKeysDetailsDTO {
	
	private String key;
	
	private String value;
	
	private String updateby;
	
	private String updatedTime;
	
	//private String heading;
	
	private boolean updated;
	
	//private String fieldType;
	
	private String warehouseId;
	
	
	@ApiModelProperty(value = "Key ( %% - DB O_SETUP - KEY).")
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@ApiModelProperty(value = "Value ( %% - DB O_SETUP - VALUE).")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@ApiModelProperty(value = "Name of last updatd User ( %% - DB O_SETUP - UPDATED_BY).")
	public String getUpdateby() {
		return updateby;
	}

	public void setUpdateby(String updateby) {
		this.updateby = updateby;
	}
	@ApiModelProperty(value = "Last updatd time ( %% - DB O_SETUP - TIMESTAMP).")
	public String getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}

	@ApiModelProperty(value = "update flag" ,hidden=true)
	public boolean isUpdated() {
		return updated;
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
	}

	/*@ApiModelProperty(value = "heading type ( %% - DB O_SETUP - KEY_TYPE).")
	public String getHeading() {
		return heading;
	}

	public void setHeading(String heading) {
		this.heading = heading;
	}

	@ApiModelProperty(value = "Field type ( %% - DB O_SETUP - FIELD_TYPE).")
	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
*/
	@ApiModelProperty(value = "Warehouse Number ( %% - DB O_SETUP - WAREHOUS_ID).")
	public String getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}
	
	
	
	
	
	

}
