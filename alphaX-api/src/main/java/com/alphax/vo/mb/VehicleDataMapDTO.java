package com.alphax.vo.mb;

import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Vehicle Data Map to containing all activities on a vehicle Datenkarte.")
public class VehicleDataMapDTO {

	private String takenOverDate;
	
	private String vehicleType;
	
	private String vehicleClass;
	
	private String dateOfSale;
	
	private String dateOfApproval;
	
	private String fdokOrder;
	
	private String grossPrice;
	
	private String netPrice;
	
	private String valueAddedTax;
	
	private String vehicleRegistrationDocument;
	
	private String division;
	
	private String manufacturerCode;
	
	private String typeCode;
	
	private String variant;
	
	private String engineNumber;
	
	private String engineKind;
	
	private Integer enginePower;
	
	private Integer displacement;
	
	private String engineDescription;
	
	private String gearCode;
	
	private String gearDescription1;
	
	private String gearDescription2;
	
	private String vehicleStructureCode;
	
	private String vehicleStructureDescription;
	
	private Integer wheelbase;
	
	private Integer maximumWeight;
	
	private Integer doors;
	
	private String fuelConsumptionInTown;
	
	private String fuelConsumptionOutOfTown;
	
	private String fuelConsumptionCombination;
	
	private String emissionInTown;
	
	private String emissionOutOfTown;
	
	private String emissionCombination;
	
	private String emissionDirective;


	public VehicleDataMapDTO() {

	}


	/**
	 * @return the takenOverDate
	 */
	@ApiModelProperty(value = "Daten übernommen am - Date when vehicle was taken over (%% DB MFZDK - 35 - UPDDAT).")
	public String getTakenOverDate() {
		return takenOverDate;
	}


	/**
	 * @param takenOverDate the takenOverDate to set
	 */
	public void setTakenOverDate(String takenOverDate) {
		this.takenOverDate = takenOverDate;
	}


	/**
	 * @return the vehicleType
	 */
	@ApiModelProperty(value = "Fz-Typ - vehicle type (%% DB MFZDK - 11 - TYPTEXT).")
	@Size( max = 30 )
	public String getVehicleType() {
		return vehicleType;
	}


	/**
	 * @param vehicleType the vehicleType to set
	 */
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}


	/**
	 * @return the vehicleClass
	 */
	@ApiModelProperty(value = "Fz-Klasse - vehicle class (%% DB MFZDK - 20 - FZKLASS).")
	@Size( max = 3 )
	public String getVehicleClass() {
		return vehicleClass;
	}


	/**
	 * @param vehicleClass the vehicleClass to set
	 */
	public void setVehicleClass(String vehicleClass) {
		this.vehicleClass = vehicleClass;
	}


	/**
	 * @return the dateOfSale
	 */
	@ApiModelProperty(value = "Verk.Datum - Date when vehicle was sold (%% DB MFZDK - 34 - VERKDAT).")
	public String getDateOfSale() {
		return dateOfSale;
	}


	/**
	 * @param dateOfSale the dateOfSale to set
	 */
	public void setDateOfSale(String dateOfSale) {
		this.dateOfSale = dateOfSale;
	}


	/**
	 * @return the dateOfApproval
	 */
	@ApiModelProperty(value = "Abn.Datum - Date when vehicle was approved (%% DB MFZDK - 22 - ABNDATUM).")
	public String getDateOfApproval() {
		return dateOfApproval;
	}


	/**
	 * @param dateOfApproval the dateOfApproval to set
	 */
	public void setDateOfApproval(String dateOfApproval) {
		this.dateOfApproval = dateOfApproval;
	}


	/**
	 * @return the fdokOrder
	 */
	@ApiModelProperty(value = "FDOK-Auft - FDOK order information (%% DB MFZDK - 23 - FDOKAUFT).")
	@Size( max = 12 )
	public String getFdokOrder() {
		return fdokOrder;
	}


	/**
	 * @param fdokOrder the fdokOrder to set
	 */
	public void setFdokOrder(String fdokOrder) {
		this.fdokOrder = fdokOrder;
	}


	/**
	 * @return the grossPrice
	 */
	@ApiModelProperty(value = "Bruttopreis - gross price information (%% DB MFZDK - 31 - BLPR).")
	public String getGrossPrice() {
		return grossPrice;
	}


	/**
	 * @param grossPrice the grossPrice to set
	 */
	public void setGrossPrice(String grossPrice) {
		this.grossPrice = grossPrice;
	}


	/**
	 * @return the netPrice
	 */
	@ApiModelProperty(value = "Nettopreis - net price information (%% DB MFZDK - 32 - NETPR).")
	public String getNetPrice() {
		return netPrice;
	}


	/**
	 * @param netPrice the netPrice to set
	 */
	public void setNetPrice(String netPrice) {
		this.netPrice = netPrice;
	}


	/**
	 * @return the valueAddedTax
	 */
	@ApiModelProperty(value = "MwSt - value-adde tax information (%% DB MFZDK - 33 - MWST).")
	public String getValueAddedTax() {
		return valueAddedTax;
	}


	/**
	 * @param valueAddedTax the valueAddedTax to set
	 */
	public void setValueAddedTax(String valueAddedTax) {
		this.valueAddedTax = valueAddedTax;
	}


	/**
	 * @return the vehicleRegistrationDocument
	 */
	@ApiModelProperty(value = "Fz-Brief - Identifier for vehicle registration document (%% DB MFZDK - 4 - FZBRIEF).")
	@Size( max = 14 )
	public String getVehicleRegistrationDocument() {
		return vehicleRegistrationDocument;
	}


	/**
	 * @param vehicleRegistrationDocument the vehicleRegistrationDocument to set
	 */
	public void setVehicleRegistrationDocument(String vehicleRegistrationDocument) {
		this.vehicleRegistrationDocument = vehicleRegistrationDocument;
	}


	/**
	 * @return the division
	 */
	@ApiModelProperty(value = "Sparte - division information (%% DB MFZDK - 21 - SPARTE).")
	@Size( max = 3 )
	public String getDivision() {
		return division;
	}


	/**
	 * @param division the division to set
	 */
	public void setDivision(String division) {
		this.division = division;
	}


	/**
	 * @return the manufacturerCode
	 */
	@ApiModelProperty(value = "Herst - Identifier of vehicle manufacturer (%% DB MFZDK - 5 - HERCOD).")
	@Size( max = 4 )
	public String getManufacturerCode() {
		return manufacturerCode;
	}


	/**
	 * @param manufacturerCode the manufacturerCode to set
	 */
	public void setManufacturerCode(String manufacturerCode) {
		this.manufacturerCode = manufacturerCode;
	}


	/**
	 * @return the typeCode
	 */
	@ApiModelProperty(value = "Typ-Code - Identifier of vehcile type (%% DB MFZDK - 6 - TYPCOD).")
	@Size( max = 3 )
	public String getTypeCode() {
		return typeCode;
	}


	/**
	 * @param typeCode the typeCode to set
	 */
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}


	/**
	 * @return the variant
	 */
	@ApiModelProperty(value = "Variante - Information of vehicle variant and insurance key (%% DB MFZDK - 7 - VARVER).")
	@Size( max = 5 )
	public String getVariant() {
		return variant;
	}


	/**
	 * @param variant the variant to set
	 */
	public void setVariant(String variant) {
		this.variant = variant;
	}


	/**
	 * @return the engineNumber
	 */
	@ApiModelProperty(value = "Motor-Nr - Identifier of vehicle engine (%% DB MFZDK - 2 - MOTORNR).")
	@Size( max = 16 )
	public String getEngineNumber() {
		return engineNumber;
	}


	/**
	 * @param engineNumber the engineNumber to set
	 */
	public void setEngineNumber(String engineNumber) {
		this.engineNumber = engineNumber;
	}


	/**
	 * @return the engineKind
	 */
	@ApiModelProperty(value = "art - Information of engine kind used in vehicle (%% DB MFZDK - 16 - MOTART).")
	@Size( max = 1 )
	public String getEngineKind() {
		return engineKind;
	}


	/**
	 * @param engineKind the engineKind to set
	 */
	public void setEngineKind(String engineKind) {
		this.engineKind = engineKind;
	}


	/**
	 * @return the enginePower
	 */
	@ApiModelProperty(value = "kw - Information of engine power in kilowatts (%% DB MFZDK - 3 - KW).")
	public Integer getEnginePower() {
		return enginePower;
	}


	/**
	 * @param enginePower the enginePower to set
	 */
	public void setEnginePower(Integer enginePower) {
		this.enginePower = enginePower;
	}


	/**
	 * @return the displacement
	 */
	@ApiModelProperty(value = "Hubraum - Information of engins cubic capacity (%% DB MFZDK - 19 - HUB).")
	public Integer getDisplacement() {
		return displacement;
	}


	/**
	 * @param displacement the displacement to set
	 */
	public void setDisplacement(Integer displacement) {
		this.displacement = displacement;
	}


	/**
	 * @return the engineDescription
	 */
	@ApiModelProperty(value = "-- - Textual information of engine (%% DB MFZDK - 17 - MOTTEXT).")
	@Size( max = 30 )
	public String getEngineDescription() {
		return engineDescription;
	}


	/**
	 * @param engineDescription the engineDescription to set
	 */
	public void setEngineDescription(String engineDescription) {
		this.engineDescription = engineDescription;
	}


	/**
	 * @return the gearCode
	 */
	@ApiModelProperty(value = "Getriebe Code - Identifier of gearbox (%% DB MFZDK - 8 - GETRCOD).")
	@Size( max = 1 )
	public String getGearCode() {
		return gearCode;
	}


	/**
	 * @param gearCode the gearCode to set
	 */
	public void setGearCode(String gearCode) {
		this.gearCode = gearCode;
	}


	/**
	 * @return the gearDescription1
	 */
	@ApiModelProperty(value = "-- - Textual information of gearbox part 1 (%% DB MFZDK - 9 - GETRTXT1).")
	@Size( max = 30 )
	public String getGearDescription1() {
		return gearDescription1;
	}


	/**
	 * @param gearDescription1 the gearDescription1 to set
	 */
	public void setGearDescription1(String gearDescription1) {
		this.gearDescription1 = gearDescription1;
	}


	/**
	 * @return the gearDescription2
	 */
	@ApiModelProperty(value = "-- - Textual information of gearbox part 2 (%% DB MFZDK - 10 - GETRTXT2).")
	@Size( max = 30 )
	public String getGearDescription2() {
		return gearDescription2;
	}


	/**
	 * @param gearDescription2 the gearDescription2 to set
	 */
	public void setGearDescription2(String gearDescription2) {
		this.gearDescription2 = gearDescription2;
	}


	/**
	 * @return the vehicleStructureCode
	 */
	@ApiModelProperty(value = "Structure Code - Identifier of vehcile structure (%% DB MFZDK - 12 - AUFBCOD).")
	@Size( max = 1 )
	public String getVehicleStructureCode() {
		return vehicleStructureCode;
	}


	/**
	 * @param vehicleStructureCode the vehicleStructureCode to set
	 */
	public void setVehicleStructureCode(String vehicleStructureCode) {
		this.vehicleStructureCode = vehicleStructureCode;
	}


	/**
	 * @return the vehicleStructureDescription
	 */
	@ApiModelProperty(value = "-- - Textual information of vehicle structure (%% DB MFZDK - 13 - AUFBTXT).")
	@Size( max = 30 )
	public String getVehicleStructureDescription() {
		return vehicleStructureDescription;
	}


	/**
	 * @param vehicleStructureDescription the vehicleStructureDescription to set
	 */
	public void setVehicleStructureDescription(String vehicleStructureDescription) {
		this.vehicleStructureDescription = vehicleStructureDescription;
	}


	/**
	 * @return the wheelbase
	 */
	@ApiModelProperty(value = "Radstand - Information of vehicle wheelbase (%% DB MFZDK - 14 - RADST).")
	public Integer getWheelbase() {
		return wheelbase;
	}


	/**
	 * @param wheelbase the wheelbase to set
	 */
	public void setWheelbase(Integer wheelbase) {
		this.wheelbase = wheelbase;
	}


	/**
	 * @return the maximumWeight
	 */
	@ApiModelProperty(value = "Max.Gew. - Information of maximum weight (%% DB MFZDK - 15 - GEWMAX).")
	public Integer getMaximumWeight() {
		return maximumWeight;
	}


	/**
	 * @param maximumWeight the maximumWeight to set
	 */
	public void setMaximumWeight(Integer maximumWeight) {
		this.maximumWeight = maximumWeight;
	}


	/**
	 * @return the doors
	 */
	@ApiModelProperty(value = "Türen - Information of doors in vehicle (%% DB MFZDK - 18 - TUERANZ).")
	public Integer getDoors() {
		return doors;
	}


	/**
	 * @param doors the doors to set
	 */
	public void setDoors(Integer doors) {
		this.doors = doors;
	}


	/**
	 * @return the fuelConsumptionInTown
	 */
	@ApiModelProperty(value = "Verbrauch innerorts - Information of fuel consumption in urban areas (%% DB MFZDK - 24 - KSTIN).")
	@Size( max = 8 )
	public String getFuelConsumptionInTown() {
		return fuelConsumptionInTown;
	}


	/**
	 * @param fuelConsumptionInTown the fuelConsumptionInTown to set
	 */
	public void setFuelConsumptionInTown(String fuelConsumptionInTown) {
		this.fuelConsumptionInTown = fuelConsumptionInTown;
	}


	/**
	 * @return the fuelConsumptionOutOfTown
	 */
	@ApiModelProperty(value = "Verbrauch außerorts - Information of fuel consumption out of urban areas (%% DB MFZDK - 25 - KSOUT).")
	@Size( max = 8 )
	public String getFuelConsumptionOutOfTown() {
		return fuelConsumptionOutOfTown;
	}


	/**
	 * @param fuelConsumptionOutOfTown the fuelConsumptionOutOfTown to set
	 */
	public void setFuelConsumptionOutOfTown(String fuelConsumptionOutOfTown) {
		this.fuelConsumptionOutOfTown = fuelConsumptionOutOfTown;
	}


	/**
	 * @return the fuelConsumptionCombination
	 */
	@ApiModelProperty(value = "Verbrauch kombin. - Information of combination for fuel consumption in urban areas and out of urban areas (%% DB MFZDK - 26 - KSCOM).")
	@Size( max = 8 )
	public String getFuelConsumptionCombination() {
		return fuelConsumptionCombination;
	}


	/**
	 * @param fuelConsumptionCombination the fuelConsumptionCombination to set
	 */
	public void setFuelConsumptionCombination(String fuelConsumptionCombination) {
		this.fuelConsumptionCombination = fuelConsumptionCombination;
	}


	/**
	 * @return the emissionInTown
	 */
	@ApiModelProperty(value = "Emission innerorts - Information of vehicle emission in urban areas (%% DB MFZDK - 28 - CO2EIN).")
	@Size( max = 5)
	public String getEmissionInTown() {
		return emissionInTown;
	}


	/**
	 * @param emissionInTown the emissionInTown to set
	 */
	public void setEmissionInTown(String emissionInTown) {
		this.emissionInTown = emissionInTown;
	}


	/**
	 * @return the emissionOutOfTown
	 */
	@ApiModelProperty(value = "Emission außerorts - Information of vehicle emission out of urban areas (%% DB MFZDK - 29 - CO2EOUT).")
	@Size( max = 5)
	public String getEmissionOutOfTown() {
		return emissionOutOfTown;
	}


	/**
	 * @param emissionOutOfTown the emissionOutOfTown to set
	 */
	public void setEmissionOutOfTown(String emissionOutOfTown) {
		this.emissionOutOfTown = emissionOutOfTown;
	}


	/**
	 * @return the emissionCombination
	 */
	@ApiModelProperty(value = "Emission kombin. - Information of combination for vehicle emission in urban areas and out of urban areas (%% DB MFZDK - 30 - CO2ECOM).")
	@Size( max = 5)
	public String getEmissionCombination() {
		return emissionCombination;
	}


	/**
	 * @param emissionCombination the emissionCombination to set
	 */
	public void setEmissionCombination(String emissionCombination) {
		this.emissionCombination = emissionCombination;
	}


	/**
	 * @return the emissionDirective
	 */
	@ApiModelProperty(value = "Emission Richtlinie - Information of emission directive (%% DB MFZDK - 27 - CO2ERL).")
	@Size( max = 30)
	public String getEmissionDirective() {
		return emissionDirective;
	}


	/**
	 * @param emissionDirective the emissionDirective to set
	 */
	public void setEmissionDirective(String emissionDirective) {
		this.emissionDirective = emissionDirective;
	}


}