package com.alphax.model.mb;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about SearchCustomer_VEGA1")
public class SearchCustomer_VEGA1 {
	
		//Customer Number
		@DBTable(columnName ="KDNR1", required = true)
		private String kundenNummer;
		
		//name
		@DBTable(columnName ="NAME", required = true)
		private String name;
		
		
		public SearchCustomer_VEGA1() {
			
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

}