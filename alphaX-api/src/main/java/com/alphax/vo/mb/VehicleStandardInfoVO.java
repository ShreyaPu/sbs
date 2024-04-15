package com.alphax.vo.mb;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(description = "This Object about vehicle standard Information")
public class VehicleStandardInfoVO {
	
	@ApiModelProperty(value = "Fahrzeug-Typ - DB M1_KOND - DATAFLD ")
	//@Size(max=18)
	private String vehicleType;
	
	@ApiModelProperty(value = "Typgruppe - DB M1_KOND - DATAFLD ")
	//@Size(max=2)
	private String typeGroup;
	
	@ApiModelProperty(value = "Fzg-Art - DB M1_KOND - DATAFLD ")
	//@Size(max=2)
	private String typeCode;
	
	@ApiModelProperty(value = " Motor-Stärke (KW) - DB M1_KOND - DATAFLD ")
	//@Size(max=3)
	private String enginePowerKw;
	
	@ApiModelProperty(value = "Hubraum/Zul.Ges.Gew.- DB M1_KOND - DATAFLD ")
	//@Size(max=5)
	private String cubicCapacity;
	
	@ApiModelProperty(value = "Antriebsart - DB M1_KOND - DATAFLD ")
	//@Size(max=1)
	private String driveType;
	
	@ApiModelProperty(value = "Marke - DB PMH_WHCMA - WFM_MARKE ")
	//@Size(max=3)
	private String marke;
	
	
	
	public String getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
	public String getTypeGroup() {
		return typeGroup;
	}
	public void setTypeGroup(String typeGroup) {
		this.typeGroup = typeGroup;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public String getEnginePowerKw() {
		return enginePowerKw;
	}
	public void setEnginePowerKw(String enginePowerKw) {
		this.enginePowerKw = enginePowerKw;
	}
	public String getCubicCapacity() {
		return cubicCapacity;
	}
	public void setCubicCapacity(String cubicCapacity) {
		this.cubicCapacity = cubicCapacity;
	}
	public String getDriveType() {
		return driveType;
	}
	public void setDriveType(String driveType) {
		this.driveType = driveType;
	}
	public String getMarke() {
		return marke;
	}
	public void setMarke(String marke) {
		this.marke = marke;
	}
	
	

}
