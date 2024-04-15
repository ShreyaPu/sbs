package com.alphax.model.mb;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about SearchCustomer")
public class SearchCustomer {
	
		//Customer Number
		@DBTable(columnName ="KDNR1", required = true)
		private String kundenNummer;
		
		//Salutation
		@DBTable(columnName ="ANRE", required = true)
		private String anrede;
		
		//First Name
		@DBTable(columnName ="NAME2", required = true)
		private String name2;
		
		//Surname
		@DBTable(columnName ="NAME", required = true)
		private String name;
		
		//Postcode
		@DBTable(columnName ="PLZ", required = true)
		private String plz;
		
		//Place
		@DBTable(columnName ="ORT", required = true)
		private String ort;
		
		//Street
		@DBTable(columnName ="STR", required = true)
		private String strasse;
		
		@DBTable(columnName ="ROWNUMER", required = true)
		private Integer totalCount;
		
		@DBTable(columnName ="UNSEL", required = true)
		private String privatKunde;
		
		public SearchCustomer() {
			
		}
		
		
		public String getKundenNummer() {
			return kundenNummer;
		}
		public void setKundenNummer(String kundenNummer) {
			this.kundenNummer = kundenNummer;
		}
		public String getAnrede() {
			return anrede;
		}
		public void setAnrede(String anrede) {
			this.anrede = anrede;
		}
		
		public String getName2() {
			return name2;
		}
		public void setName2(String name2) {
			this.name2 = name2;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getPlz() {
			return plz;
		}
		public void setPlz(String plz) {
			this.plz = plz;
		}
		public String getOrt() {
			return ort;
		}
		public void setOrt(String ort) {
			this.ort = ort;
		}
		/**
		 * @return the strasse
		 */
		public String getStrasse() {
			return strasse;
		}
		/**
		 * @param strasse the strasse to set
		 */
		public void setStrasse(String strasse) {
			this.strasse = strasse;
		}


		public Integer getTotalCount() {
			return totalCount;
		}
		public void setTotalCount(Integer totalCount) {
			this.totalCount = totalCount;
		}


		public String getPrivatKunde() {
			return privatKunde;
		}


		public void setPrivatKunde(String privatKunde) {
			this.privatKunde = privatKunde;
		}
		
		
}