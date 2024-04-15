package com.alphax.model.mb;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about SearchVehicle")
public class SearchVehicle {
	
		//Customer Number
		@DBTable(columnName ="KDNR1", required = true)
		private String customerNumber;
		
		@DBTable(columnName ="FKENNZ7", required = true)
		private String licencePlate;
		
		//Vehicle identification number
		@DBTable(columnName ="vin", required = false)
		private String vin;
		
		@DBTable(columnName ="WHC", required = true)
		private String worldManufacturerCode;
		
		@DBTable(columnName ="FWHC", required = true)
		private String manufacturerCode;
		
		@DBTable(columnName ="BAUM", required = true)
		private String modelInformation;
		
		
		@DBTable(columnName ="ENDN", required = true)
		private String productionIndicator;
		
		
		@DBTable(columnName ="FGP", required = true)
		private String chasisNumberDigit;
		
		@DBTable(columnName ="DTEZU", required = true)
		private String firstRegistration_Date;
		
		@DBTable(columnName ="TYP", required = true)
		private String vehicleType;
		
		@DBTable(columnName ="NAME", required = true)
		private String name;
		
		@DBTable(columnName ="FZNUMMER", required = true)
		private String fzObjectNumber;
		
		@DBTable(columnName ="ROWNUMER", required = true)
		private Integer totalCount;
		
		
		public SearchVehicle() {
			
		}


		/**
		 * @return the customerNumber
		 */
		public String getCustomerNumber() {
			return customerNumber;
		}


		/**
		 * @param customerNumber the customerNumber to set
		 */
		public void setCustomerNumber(String customerNumber) {
			this.customerNumber = customerNumber;
		}


		/**
		 * @return the licencePlate
		 */
		public String getLicencePlate() {
			return licencePlate;
		}


		/**
		 * @param licencePlate the licencePlate to set
		 */
		public void setLicencePlate(String licencePlate) {
			this.licencePlate = licencePlate;
		}


		/**
		 * @return the vin
		 */
		public String getVin() {
			return vin;
		}


		/**
		 * @param vin the vin to set
		 */
		public void setVin(String vin) {
			this.vin = vin;
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
		 * @return the modelInformation
		 */
		public String getModelInformation() {
			return modelInformation;
		}


		/**
		 * @param modelInformation the modelInformation to set
		 */
		public void setModelInformation(String modelInformation) {
			this.modelInformation = modelInformation;
		}


		/**
		 * @return the productionIndicator
		 */
		public String getProductionIndicator() {
			return productionIndicator;
		}


		/**
		 * @param productionIndicator the productionIndicator to set
		 */
		public void setProductionIndicator(String productionIndicator) {
			this.productionIndicator = productionIndicator;
		}


		/**
		 * @return the chasisNumberDigit
		 */
		public String getChasisNumberDigit() {
			return chasisNumberDigit;
		}


		/**
		 * @param chasisNumberDigit the chasisNumberDigit to set
		 */
		public void setChasisNumberDigit(String chasisNumberDigit) {
			this.chasisNumberDigit = chasisNumberDigit;
		}


		/**
		 * @return the firstRegistration_Date
		 */
		public String getFirstRegistration_Date() {
			return firstRegistration_Date;
		}


		/**
		 * @param firstRegistration_Date the firstRegistration_Date to set
		 */
		public void setFirstRegistration_Date(String firstRegistration_Date) {
			this.firstRegistration_Date = firstRegistration_Date;
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
		 * @return the name
		 */
		public String getName() {
			return name;
		}


		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}


		/**
		 * @return the totalCount
		 */
		public Integer getTotalCount() {
			return totalCount;
		}


		/**
		 * @param totalCount the totalCount to set
		 */
		public void setTotalCount(Integer totalCount) {
			this.totalCount = totalCount;
		}


		public String getWorldManufacturerCode() {
			return worldManufacturerCode;
		}


		public void setWorldManufacturerCode(String worldManufacturerCode) {
			this.worldManufacturerCode = worldManufacturerCode;
		}


		public String getFzObjectNumber() {
			return fzObjectNumber;
		}


		public void setFzObjectNumber(String fzObjectNumber) {
			this.fzObjectNumber = fzObjectNumber;
		}
		
		
		
}