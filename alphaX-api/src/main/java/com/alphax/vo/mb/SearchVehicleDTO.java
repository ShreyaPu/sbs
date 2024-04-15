package com.alphax.vo.mb;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about SearchVehicle")
public class SearchVehicleDTO {
	
		//Customer Number
		@ApiModelProperty(value = "customer Id (owner of the vehicle) ( %% - DB Mx_KFZF1XX - KDNR1).")
		private String customerNumber;
		
		//License Plate
		@ApiModelProperty(value = "The license plate for the vehicle ( %% - DB Mx_KFZF1XX - FKENNZ7)")
		private String licencePlate;
		
		//VIN No.
		@ApiModelProperty(value = "The vehicle identification number ( %% - DB Mx_KFZF1XX - FWHC+BAUM+ENDN+FGP)")
		private String vin;
		
		
		//first RegistrationDate
		@ApiModelProperty(value = "Date of first registration ( %% - DB Mx_KFZF1XX - DTEZU).")
		private String firstRegistration_Date;
		
		
		//vehicleType
		@ApiModelProperty(value = "The vehicle type (vehicle model) ( %% - DB Mx_KFZF1XX - TYP).")
		private String vehicleType;
		
		//name
		@ApiModelProperty(value = "The owner of the vehicle ( %% - DB Mx_KDDATK1 - NAME).")
		private String name;
		
		
		public SearchVehicleDTO() {
			
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


		
		
}