package com.alphax.model.mb;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about SearchCustomer_VEGA1")
public class SearchCustomer_VEGA2 {
	
		//Customer Number
		@DBTable(columnName ="KDNR1", required = true)
		private String kundenNummer;
		
		//name
		@DBTable(columnName ="NAME", required = true)
		private String name;
		
		//Postal code
		@DBTable(columnName ="SER_DTGULV", required = true)
		private String validity_From;
		
		//City
		@DBTable(columnName ="SER_DTGULB", required = true)
		private String validity_To;
		
	
		public SearchCustomer_VEGA2() {
			
		}
		
		
		public String getKundenNummer() {
			return kundenNummer;
		}
		
		public void setKundenNummer(String kundenNummer) {
			this.kundenNummer = kundenNummer;
		}
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}


		/**
		 * @return the validity_From
		 */
		public String getValidity_From() {
			return validity_From;
		}


		/**
		 * @param validity_From the validity_From to set
		 */
		public void setValidity_From(String validity_From) {
			this.validity_From = validity_From;
		}


		/**
		 * @return the validity_To
		 */
		public String getValidity_To() {
			return validity_To;
		}


		/**
		 * @param validity_To the validity_To to set
		 */
		public void setValidity_To(String validity_To) {
			this.validity_To = validity_To;
		}

}