package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about AlphaX Configuration Keys Details")
public class AlphaXConfigurationKeysDetails {
	
	@DBTable(columnName ="COMPANY_ID", required = true)
	private BigDecimal companyId;
	
	@DBTable(columnName ="AGENCY_ID", required = true)
	private BigDecimal agencyId;
	
	@DBTable(columnName ="KEY", required = true)
	private String key;
	
	@DBTable(columnName ="VALUE", required = true)
	private String value;
	
	@DBTable(columnName ="UPDATED_BY", required = true)
	private String updateby;
	
	@DBTable(columnName ="TIMESTAMP", required = true)
	private String updatedTime;
	
	@DBTable(columnName ="KEY_TYPE", required = true)
	private String heading;
	
	@DBTable(columnName ="FIELD_TYPE", required = true)
	private String fieldType;
	
	@DBTable(columnName ="WAREHOUS_ID", required = true)
	private String warehouseId;

	public BigDecimal getCompanyId() {
		return companyId;
	}

	public void setCompanyId(BigDecimal companyId) {
		this.companyId = companyId;
	}

	public BigDecimal getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(BigDecimal agencyId) {
		this.agencyId = agencyId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getUpdateby() {
		return updateby;
	}

	public void setUpdateby(String updateby) {
		this.updateby = updateby;
	}

	public String getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getHeading() {
		return heading;
	}

	public void setHeading(String heading) {
		this.heading = heading;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}
	
	
	
	
}
