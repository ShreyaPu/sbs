package com.alphax.vo.mb;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "This Object about Value for a month (Int32)")
public class MonthlyIntValue {

	@ApiModelProperty(required = true, value = "Monat.") 
	private Integer month;
	
	@ApiModelProperty(value = "Wert.")
	private BigDecimal value;

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}
	
	
}
