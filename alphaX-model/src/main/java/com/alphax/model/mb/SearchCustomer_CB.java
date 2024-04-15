package com.alphax.model.mb;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about SearchCustomer_CB")
public class SearchCustomer_CB {
	
		//Customer Number
		@DBTable(columnName ="KDNR1", required = true)
		private String kundenNummer;
		
		//name
		@DBTable(columnName ="NAME", required = true)
		private String name;
		
		//Postal code
		@DBTable(columnName ="PLZ", required = true)
		private String plz;
		
		//City
		@DBTable(columnName ="ORT", required = true)
		private String ort;
		
		//Street
		@DBTable(columnName ="STR", required = true)
		private String strasse;
		
		//KZST
		@DBTable(columnName ="KZST", required = true)
		private String kzst;
		
		@DBTable(columnName ="ROWNUMER", required = true)
		private Integer totalCount;
		
		public SearchCustomer_CB() {
			
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


		/**
		 * @return the kzst
		 */
		public String getKzst() {
			return kzst;
		}


		/**
		 * @param kzst the kzst to set
		 */
		public void setKzst(String kzst) {
			this.kzst = kzst;
		}
}