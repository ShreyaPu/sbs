package com.alphax.model.mb;

import com.alphax.common.util.DBTable;

public class PartBADetails {
	//predecessor
	//successor
	
	@DBTable(columnName = "NW1_EW", required = true)
	private String  satzart_predecessor;
	
	@DBTable(columnName = "NW2_EW", required = true)
	private String satzart_successor;
	
	@DBTable(columnName = "DAKEWV", required = true)
	private String dak_predecessor;
	
	@DBTable(columnName = "DAKEWN", required = true)
	private String dak_successor;	
	
	@DBTable(columnName = "TNRVV", required = true)
	private String predecessor_Alt;
	
	@DBTable(columnName = "TNRVN", required = true)
	private String predecessor_Neu;	
	
	@DBTable(columnName = "TNRNV", required = true)
	private String successor_Alt;
	
	@DBTable(columnName = "TNRNN", required = true)
	private String successor_Neu;
	
	@DBTable(columnName = "NPREIV", required = true)
	private String netPrice_predecessor;
	
	@DBTable(columnName = "NPREIN", required = true)
	private String netPrice_successor;
	
	@DBTable(columnName = "RABGRV", required = true)
	private String discount_pressor;

	@DBTable(columnName = "RABGRN", required = true)
	private String discount_successor;
	
	@DBTable(columnName = "MARKEV", required = true)
	private String partBrand_pressor;
	
	@DBTable(columnName = "MARKEN", required = true)
	private String partBrand_successor;
	
	@DBTable(columnName = "DRTKZV", required = true)
	private String licenseplate_Pressor;

	@DBTable(columnName = "DRTKZN", required = true)
	private String licenseplate_Successor;
	
	@DBTable(columnName = "ZPREIV", required = true)
	private String newlistprice_Pressor;
	
	@DBTable(columnName = "ZPREIN", required = true)
	private String newlistprice_Successor;
	
	@DBTable(columnName = "DTZPRV", required = true)
	private String newlistprice_PressorFrom;
	
	@DBTable(columnName = "DTZPRN", required = true)
	private String newlistprice_SuccessorFrom;
	
	@DBTable(columnName = "TRUWEV", required = true)
	private String returnvalue_Pressor;
	
	@DBTable(columnName = "TRUWEN", required = true)
	private String returnvalue_Successor;
	
	@DBTable(columnName = "MCODEV", required = true)
	private String marketingCode_predecessor;
	
	@DBTable(columnName = "MCODE", required = true)
	private String marketingCode_successor;
	
	@DBTable(columnName = "MWSTV", required = true)
	private String vat_pressor;

	@DBTable(columnName = "MWSTN", required = true)
	private String vat_successor;
	
	@DBTable(columnName = "TA_EWV", required = true)
	private String teileart_predecessor;
	
	@DBTable(columnName = "TA_EWN", required = true)
	private String teileart_successor;

	public String getSatzart_predecessor() {
		return satzart_predecessor;
	}

	public void setSatzart_predecessor(String satzart_predecessor) {
		this.satzart_predecessor = satzart_predecessor;
	}

	public String getSatzart_successor() {
		return satzart_successor;
	}

	public void setSatzart_successor(String satzart_successor) {
		this.satzart_successor = satzart_successor;
	}

	public String getDak_predecessor() {
		return dak_predecessor;
	}

	public void setDak_predecessor(String dak_predecessor) {
		this.dak_predecessor = dak_predecessor;
	}

	public String getDak_successor() {
		return dak_successor;
	}

	public void setDak_successor(String dak_successor) {
		this.dak_successor = dak_successor;
	}

	public String getPredecessor_Alt() {
		return predecessor_Alt;
	}

	public void setPredecessor_Alt(String predecessor_Alt) {
		this.predecessor_Alt = predecessor_Alt;
	}

	public String getPredecessor_Neu() {
		return predecessor_Neu;
	}

	public void setPredecessor_Neu(String predecessor_Neu) {
		this.predecessor_Neu = predecessor_Neu;
	}

	public String getSuccessor_Alt() {
		return successor_Alt;
	}

	public void setSuccessor_Alt(String successor_Alt) {
		this.successor_Alt = successor_Alt;
	}

	public String getSuccessor_Neu() {
		return successor_Neu;
	}

	public void setSuccessor_Neu(String successor_Neu) {
		this.successor_Neu = successor_Neu;
	}

	public String getNetPrice_predecessor() {
		return netPrice_predecessor;
	}

	public void setNetPrice_predecessor(String netPrice_predecessor) {
		this.netPrice_predecessor = netPrice_predecessor;
	}

	public String getNetPrice_successor() {
		return netPrice_successor;
	}

	public void setNetPrice_successor(String netPrice_successor) {
		this.netPrice_successor = netPrice_successor;
	}

	public String getDiscount_pressor() {
		return discount_pressor;
	}

	public void setDiscount_pressor(String discount_pressor) {
		this.discount_pressor = discount_pressor;
	}

	public String getDiscount_successor() {
		return discount_successor;
	}

	public void setDiscount_successor(String discount_successor) {
		this.discount_successor = discount_successor;
	}

	public String getPartBrand_pressor() {
		return partBrand_pressor;
	}

	public void setPartBrand_pressor(String partBrand_pressor) {
		this.partBrand_pressor = partBrand_pressor;
	}

	public String getPartBrand_successor() {
		return partBrand_successor;
	}

	public void setPartBrand_successor(String partBrand_successor) {
		this.partBrand_successor = partBrand_successor;
	}

	public String getLicenseplate_Pressor() {
		return licenseplate_Pressor;
	}

	public void setLicenseplate_Pressor(String licenseplate_Pressor) {
		this.licenseplate_Pressor = licenseplate_Pressor;
	}

	public String getLicenseplate_Successor() {
		return licenseplate_Successor;
	}

	public void setLicenseplate_Successor(String licenseplate_Successor) {
		this.licenseplate_Successor = licenseplate_Successor;
	}

	public String getNewlistprice_Pressor() {
		return newlistprice_Pressor;
	}

	public void setNewlistprice_Pressor(String newlistprice_Pressor) {
		this.newlistprice_Pressor = newlistprice_Pressor;
	}

	public String getNewlistprice_Successor() {
		return newlistprice_Successor;
	}

	public void setNewlistprice_Successor(String newlistprice_Successor) {
		this.newlistprice_Successor = newlistprice_Successor;
	}

	public String getNewlistprice_PressorFrom() {
		return newlistprice_PressorFrom;
	}

	public void setNewlistprice_PressorFrom(String newlistprice_PressorFrom) {
		this.newlistprice_PressorFrom = newlistprice_PressorFrom;
	}

	public String getNewlistprice_SuccessorFrom() {
		return newlistprice_SuccessorFrom;
	}

	public void setNewlistprice_SuccessorFrom(String newlistprice_SuccessorFrom) {
		this.newlistprice_SuccessorFrom = newlistprice_SuccessorFrom;
	}

	public String getReturnvalue_Pressor() {
		return returnvalue_Pressor;
	}

	public void setReturnvalue_Pressor(String returnvalue_Pressor) {
		this.returnvalue_Pressor = returnvalue_Pressor;
	}

	public String getReturnvalue_Successor() {
		return returnvalue_Successor;
	}

	public void setReturnvalue_Successor(String returnvalue_Successor) {
		this.returnvalue_Successor = returnvalue_Successor;
	}

	public String getMarketingCode_predecessor() {
		return marketingCode_predecessor;
	}

	public void setMarketingCode_predecessor(String marketingCode_predecessor) {
		this.marketingCode_predecessor = marketingCode_predecessor;
	}

	public String getMarketingCode_successor() {
		return marketingCode_successor;
	}

	public void setMarketingCode_successor(String marketingCode_successor) {
		this.marketingCode_successor = marketingCode_successor;
	}

	public String getVat_pressor() {
		return vat_pressor;
	}

	public void setVat_pressor(String vat_pressor) {
		this.vat_pressor = vat_pressor;
	}

	public String getVat_successor() {
		return vat_successor;
	}

	public void setVat_successor(String vat_successor) {
		this.vat_successor = vat_successor;
	}

	public String getTeileart_predecessor() {
		return teileart_predecessor;
	}

	public void setTeileart_predecessor(String teileart_predecessor) {
		this.teileart_predecessor = teileart_predecessor;
	}

	public String getTeileart_successor() {
		return teileart_successor;
	}

	public void setTeileart_successor(String teileart_successor) {
		this.teileart_successor = teileart_successor;
	}
	
	
	

	
	
}
