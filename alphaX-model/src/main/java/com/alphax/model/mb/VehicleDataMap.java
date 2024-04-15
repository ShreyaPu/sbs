package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

public class VehicleDataMap {

	/* Daten übernommen am - Date when vehicle was taken over (%% DB MFZDK - 35 - UPDDAT).") */
	@DBTable(columnName ="UPDDAT", required = true )
	private BigDecimal takenOverDate;
	
	/*  Fz-Typ - vehicle type (%% DB MFZDK - 11 - TYPTEXT).")  */
	@DBTable(columnName ="TYPTEXT", required = true )
	private String vehicleType;
	
	/*  Fz-Klasse - vehicle class (%% DB MFZDK - 20 - FZKLASS).")  */
	@DBTable(columnName ="FZKLASS", required = true )
	private String vehicleClass;
	
	/*  Verk.Datum - Date when vehicle was sold (%% DB MFZDK - 34 - VERKDAT).")  */
	@DBTable(columnName ="VERKDAT", required = true )
	private BigDecimal dateOfSale;
	
	/*  Abn.Datum - Date when vehicle was approved (%% DB MFZDK - 22 - ABNDATUM).")  */
	@DBTable(columnName ="ABNDATUM", required = true )
	private BigDecimal dateOfApproval;
	
	/*  FDOK-Auft - FDOK order information (%% DB MFZDK - 23 - FDOKAUFT).")  */
	@DBTable(columnName ="FDOKAUFT", required = true )
	private String fdokOrder;
	
	/* Bruttopreis - gross price information (%% DB MFZDK - 31 - BLPR).")  */
	@DBTable(columnName ="BLPR", required = true )
	private BigDecimal grossPrice;
	
	/* Nettopreis - net price information (%% DB MFZDK - 32 - NETPR).")  */
	@DBTable(columnName ="NETPR", required = true )
	private BigDecimal netPrice;
	
	/* MwSt - value-adde tax information (%% DB MFZDK - 33 - MWST).")  */
	@DBTable(columnName ="MWST", required = true )
	private BigDecimal valueAddedTax;
	
	/* Fz-Brief - Identifier for vehicle registration document (%% DB MFZDK - 4 - FZBRIEF).")  */
	@DBTable(columnName ="FZBRIEF", required = true )
	private String vehicleRegistrationDocument;
	
	/* Sparte - division information (%% DB MFZDK - 21 - SPARTE).")  */
	@DBTable(columnName ="SPARTE", required = true )
	private String division;
	
	/* Herst - Identifier of vehicle manufacturer (%% DB MFZDK - 5 - HERCOD).")  */
	@DBTable(columnName ="HERCOD", required = true )
	private String manufacturerCode;
	
	/* Typ-Code - Identifier of vehcile type (%% DB MFZDK - 6 - TYPCOD).")  */
	@DBTable(columnName ="TYPCOD", required = true )
	private String typeCode;
	
	/* Variante - Information of vehicle variant and insurance key (%% DB MFZDK - 7 - VARVER).")  */
	@DBTable(columnName ="VARVER", required = true )
	private String variant;
	
	/* Motor-Nr - Identifier of vehicle engine (%% DB MFZDK - 2 - MOTORNR).")  */
	@DBTable(columnName ="MOTORNR", required = true )
	private String engineNumber;
	
	/* art - Information of engine kind used in vehicle (%% DB MFZDK - 16 - MOTART).")  */
	@DBTable(columnName ="MOTART", required = true )
	private String engineKind;
	
	/* kw - Information of engine power in kilowatts (%% DB MFZDK - 3 - KW).")  */
	@DBTable(columnName ="KW", required = true )
	private BigDecimal enginePower;
	
	/* Hubraum - Information of engins cubic capacity (%% DB MFZDK - 19 - HUB).")   */
	@DBTable(columnName ="HUB", required = true )
	private BigDecimal displacement;
	
	/* -- - Textual information of engine (%% DB MFZDK - 17 - MOTTEXT).")  */
	@DBTable(columnName ="MOTTEXT", required = true )
	private String engineDescription;
	
	/* Getriebe Code - Identifier of gearbox (%% DB MFZDK - 8 - GETRCOD).")  */
	@DBTable(columnName ="GETRCOD", required = true )
	private String gearCode;
	
	/* -- - Textual information of gearbox part 1 (%% DB MFZDK - 9 - GETRTXT1).")  */
	@DBTable(columnName ="GETRTXT1", required = true )
	private String gearDescription1;
	
	/* -- - Textual information of gearbox part 2 (%% DB MFZDK - 10 - GETRTXT2).")  */
	@DBTable(columnName ="GETRTXT2", required = true )
	private String gearDescription2;
	
	/* Structure Code - Identifier of vehcile structure (%% DB MFZDK - 12 - AUFBCOD).")  */
	@DBTable(columnName ="AUFBCOD", required = true )
	private String vehicleStructureCode;
	
	/* -- - Textual information of vehicle structure (%% DB MFZDK - 13 - AUFBTXT).")  */
	@DBTable(columnName ="AUFBTXT", required = true )
	private String vehicleStructureDescription;
	
	/* Radstand - Information of vehicle wheelbase (%% DB MFZDK - 14 - RADST).")  */
	@DBTable(columnName ="RADST", required = true )
	private BigDecimal wheelbase;
	
	/* Max.Gew. - Information of maximum weight (%% DB MFZDK - 15 - GEWMAX).")  */
	@DBTable(columnName ="GEWMAX", required = true )
	private BigDecimal maximumWeight;
	
	/* Türen - Information of doors in vehicle (%% DB MFZDK - 18 - TUERANZ).")  */
	@DBTable(columnName ="TUERANZ", required = true )
	private BigDecimal doors;
	
	/* Verbrauch innerorts - Information of fuel consumption in urban areas (%% DB MFZDK - 24 - KSTIN).")  */
	@DBTable(columnName ="KSTIN", required = true )
	private String fuelConsumptionInTown;
	
	/* Verbrauch außerorts - Information of fuel consumption out of urban areas (%% DB MFZDK - 25 - KSOUT).")  */
	@DBTable(columnName ="KSOUT", required = true )
	private String fuelConsumptionOutOfTown;
	
	/* Verbrauch kombin. - Information of combination for fuel consumption in urban areas and out of urban areas (%% DB MFZDK - 26 - KSCOM).")  */
	@DBTable(columnName ="KSCOM", required = true )
	private String fuelConsumptionCombination;
	
	/* Emission innerorts - Information of vehicle emission in urban areas (%% DB MFZDK - 28 - CO2EIN).")  */
	@DBTable(columnName ="CO2EIN", required = true )
	private String emissionInTown;
	
	/* Emission außerorts - Information of vehicle emission out of urban areas (%% DB MFZDK - 29 - CO2EOUT).")  */
	@DBTable(columnName ="CO2EOUT", required = true )
	private String emissionOutOfTown;
	
	/* Emission kombin. - Information of combination for vehicle emission in urban areas and out of urban areas (%% DB MFZDK - 30 - CO2ECOM).")  */
	@DBTable(columnName ="CO2ECOM", required = true )
	private String emissionCombination;
	
	/* Emission Richtlinie - Information of emission directive (%% DB MFZDK - 27 - CO2ERL).")  */
	@DBTable(columnName ="CO2ERL", required = true )
	private String emissionDirective;


	public VehicleDataMap() {

	}


	/**
	 * @return the takenOverDate
	 */
	public BigDecimal getTakenOverDate() {
		return takenOverDate;
	}


	/**
	 * @param takenOverDate the takenOverDate to set
	 */
	public void setTakenOverDate(BigDecimal takenOverDate) {
		this.takenOverDate = takenOverDate;
	}


	/**
	 * @return the vehicleType
	 */
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
	public BigDecimal getDateOfSale() {
		return dateOfSale;
	}


	/**
	 * @param dateOfSale the dateOfSale to set
	 */
	public void setDateOfSale(BigDecimal dateOfSale) {
		this.dateOfSale = dateOfSale;
	}


	/**
	 * @return the dateOfApproval
	 */
	public BigDecimal getDateOfApproval() {
		return dateOfApproval;
	}


	/**
	 * @param dateOfApproval the dateOfApproval to set
	 */
	public void setDateOfApproval(BigDecimal dateOfApproval) {
		this.dateOfApproval = dateOfApproval;
	}


	/**
	 * @return the fdokOrder
	 */
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
	public BigDecimal getGrossPrice() {
		return grossPrice;
	}


	/**
	 * @param grossPrice the grossPrice to set
	 */
	public void setGrossPrice(BigDecimal grossPrice) {
		this.grossPrice = grossPrice;
	}


	/**
	 * @return the netPrice
	 */
	public BigDecimal getNetPrice() {
		return netPrice;
	}


	/**
	 * @param netPrice the netPrice to set
	 */
	public void setNetPrice(BigDecimal netPrice) {
		this.netPrice = netPrice;
	}


	/**
	 * @return the valueAddedTax
	 */
	public BigDecimal getValueAddedTax() {
		return valueAddedTax;
	}


	/**
	 * @param valueAddedTax the valueAddedTax to set
	 */
	public void setValueAddedTax(BigDecimal valueAddedTax) {
		this.valueAddedTax = valueAddedTax;
	}


	/**
	 * @return the vehicleRegistrationDocument
	 */
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
	public BigDecimal getEnginePower() {
		return enginePower;
	}


	/**
	 * @param enginePower the enginePower to set
	 */
	public void setEnginePower(BigDecimal enginePower) {
		this.enginePower = enginePower;
	}


	/**
	 * @return the displacement
	 */
	public BigDecimal getDisplacement() {
		return displacement;
	}


	/**
	 * @param displacement the displacement to set
	 */
	public void setDisplacement(BigDecimal displacement) {
		this.displacement = displacement;
	}


	/**
	 * @return the engineDescription
	 */
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
	public BigDecimal getWheelbase() {
		return wheelbase;
	}


	/**
	 * @param wheelbase the wheelbase to set
	 */
	public void setWheelbase(BigDecimal wheelbase) {
		this.wheelbase = wheelbase;
	}


	/**
	 * @return the maximumWeight
	 */
	public BigDecimal getMaximumWeight() {
		return maximumWeight;
	}


	/**
	 * @param maximumWeight the maximumWeight to set
	 */
	public void setMaximumWeight(BigDecimal maximumWeight) {
		this.maximumWeight = maximumWeight;
	}


	/**
	 * @return the doors
	 */
	public BigDecimal getDoors() {
		return doors;
	}


	/**
	 * @param doors the doors to set
	 */
	public void setDoors(BigDecimal doors) {
		this.doors = doors;
	}


	/**
	 * @return the fuelConsumptionInTown
	 */
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