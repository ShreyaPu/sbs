package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

public class VehicleLegend {
	

	/*  The company identifier for the vehicle legend item (%% DB Mx_KFZL - 24 - FA) */
	@DBTable(columnName ="FA", required = true )
	private BigDecimal companyId;

	
	/*  The agency identifier for the vehicle legend item (%% DB M_KFZL - 25 - FIL). */
	@DBTable(columnName ="FIL", required = true )
	private BigDecimal agencyId;

	
	/*  The number of the assignment/order for the vehicle legend item (%% DB M_KFZL - 5 - AUFNR). */
	@DBTable(columnName ="AUFNR", required = true )
	private BigDecimal assignmentNumber;
	
	
	/*  The date of the assignment/order for the vehicle legend item (%% DB M_KFZL - 6 - AUFDAT).  */
	@DBTable(columnName ="AUFDAT", required = true )
	private BigDecimal assignmentDate;
	
	
	/*  The mileage value (in thousand kilometers) when the vehicle legend item was created (%% DB M_KFZL - 7 - KM).  */
	@DBTable(columnName ="KM", required = true )
	private BigDecimal mileage;
	
	/*  The customer consultant that was involved in the vehicle legend item (%% DB M_KFZL - 8 - REPAN).  */
	@DBTable(columnName ="REPAN", required = true )
	private String customerConsultant;
	
	/*  The invoice number related to the vehicle legend item (%% DB M_KFZL - 9 - RENR).  */
	@DBTable(columnName ="RENR", required = true )
	private BigDecimal invoiceNumber;
	
	
	/*  The revenue (turnover) related to the vehicle legend item (%% DB M_KFZL - 10 - REBET).  */
	@DBTable(columnName ="REBET", required = true )
	private BigDecimal revenue;
	
	
	/**  
	 * 	The repair codes related to the vehicle legend item - (DB M_KFZL - 11-22 REPKZ01 - REPKZ12). 
	*/
	@DBTable(columnName ="REPKZ01", required = true )
	private String repairCode01;
	
	@DBTable(columnName ="REPKZ02", required = true )
	private String repairCode02;
	
	@DBTable(columnName ="REPKZ03", required = true )
	private String repairCode03;
	
	@DBTable(columnName ="REPKZ04", required = true )
	private String repairCode04;
	
	@DBTable(columnName ="REPKZ05", required = true )
	private String repairCode05;
	
	@DBTable(columnName ="REPKZ06", required = true )
	private String repairCode06;
	
	@DBTable(columnName ="REPKZ07", required = true )
	private String repairCode07;
	
	@DBTable(columnName ="REPKZ08", required = true )
	private String repairCode08;
	
	@DBTable(columnName ="REPKZ09", required = true )
	private String repairCode09;
	
	@DBTable(columnName ="REPKZ10", required = true )
	private String repairCode10;
	
	@DBTable(columnName ="REPKZ11", required = true )
	private String repairCode11;
	
	@DBTable(columnName ="REPKZ12", required = true )
	private String repairCode12;
	
	@DBTable(columnName ="ROWNUMER", required = true)
	private Integer totalCount;
	
	
	public VehicleLegend() {
		
	}


	/**
	 * @return the companyId
	 */
	public BigDecimal getCompanyId() {
		return companyId;
	}


	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(BigDecimal companyId) {
		this.companyId = companyId;
	}


	/**
	 * @return the agencyId
	 */
	public BigDecimal getAgencyId() {
		return agencyId;
	}


	/**
	 * @param agencyId the agencyId to set
	 */
	public void setAgencyId(BigDecimal agencyId) {
		this.agencyId = agencyId;
	}


	/**
	 * @return the assignmentNumber
	 */
	public BigDecimal getAssignmentNumber() {
		return assignmentNumber;
	}


	/**
	 * @param assignmentNumber the assignmentNumber to set
	 */
	public void setAssignmentNumber(BigDecimal assignmentNumber) {
		this.assignmentNumber = assignmentNumber;
	}


	/**
	 * @return the assignmentDate
	 */
	public BigDecimal getAssignmentDate() {
		return assignmentDate;
	}


	/**
	 * @param assignmentDate the assignmentDate to set
	 */
	public void setAssignmentDate(BigDecimal assignmentDate) {
		this.assignmentDate = assignmentDate;
	}


	/**
	 * @return the mileage
	 */
	public BigDecimal getMileage() {
		return mileage;
	}


	/**
	 * @param mileage the mileage to set
	 */
	public void setMileage(BigDecimal mileage) {
		this.mileage = mileage;
	}


	/**
	 * @return the customerConsultant
	 */
	public String getCustomerConsultant() {
		return customerConsultant;
	}


	/**
	 * @param customerConsultant the customerConsultant to set
	 */
	public void setCustomerConsultant(String customerConsultant) {
		this.customerConsultant = customerConsultant;
	}


	/**
	 * @return the invoiceNumber
	 */
	public BigDecimal getInvoiceNumber() {
		return invoiceNumber;
	}


	/**
	 * @param invoiceNumber the invoiceNumber to set
	 */
	public void setInvoiceNumber(BigDecimal invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}


	/**
	 * @return the revenue
	 */
	public BigDecimal getRevenue() {
		return revenue;
	}


	/**
	 * @param revenue the revenue to set
	 */
	public void setRevenue(BigDecimal revenue) {
		this.revenue = revenue;
	}


	/**
	 * @return the repairCode01
	 */
	public String getRepairCode01() {
		return repairCode01;
	}


	/**
	 * @param repairCode01 the repairCode01 to set
	 */
	public void setRepairCode01(String repairCode01) {
		this.repairCode01 = repairCode01;
	}


	/**
	 * @return the repairCode02
	 */
	public String getRepairCode02() {
		return repairCode02;
	}


	/**
	 * @param repairCode02 the repairCode02 to set
	 */
	public void setRepairCode02(String repairCode02) {
		this.repairCode02 = repairCode02;
	}


	/**
	 * @return the repairCode03
	 */
	public String getRepairCode03() {
		return repairCode03;
	}


	/**
	 * @param repairCode03 the repairCode03 to set
	 */
	public void setRepairCode03(String repairCode03) {
		this.repairCode03 = repairCode03;
	}


	/**
	 * @return the repairCode04
	 */
	public String getRepairCode04() {
		return repairCode04;
	}


	/**
	 * @param repairCode04 the repairCode04 to set
	 */
	public void setRepairCode04(String repairCode04) {
		this.repairCode04 = repairCode04;
	}


	/**
	 * @return the repairCode05
	 */
	public String getRepairCode05() {
		return repairCode05;
	}


	/**
	 * @param repairCode05 the repairCode05 to set
	 */
	public void setRepairCode05(String repairCode05) {
		this.repairCode05 = repairCode05;
	}


	/**
	 * @return the repairCode06
	 */
	public String getRepairCode06() {
		return repairCode06;
	}


	/**
	 * @param repairCode06 the repairCode06 to set
	 */
	public void setRepairCode06(String repairCode06) {
		this.repairCode06 = repairCode06;
	}


	/**
	 * @return the repairCode07
	 */
	public String getRepairCode07() {
		return repairCode07;
	}


	/**
	 * @param repairCode07 the repairCode07 to set
	 */
	public void setRepairCode07(String repairCode07) {
		this.repairCode07 = repairCode07;
	}


	/**
	 * @return the repairCode08
	 */
	public String getRepairCode08() {
		return repairCode08;
	}


	/**
	 * @param repairCode08 the repairCode08 to set
	 */
	public void setRepairCode08(String repairCode08) {
		this.repairCode08 = repairCode08;
	}


	/**
	 * @return the repairCode09
	 */
	public String getRepairCode09() {
		return repairCode09;
	}


	/**
	 * @param repairCode09 the repairCode09 to set
	 */
	public void setRepairCode09(String repairCode09) {
		this.repairCode09 = repairCode09;
	}


	/**
	 * @return the repairCode10
	 */
	public String getRepairCode10() {
		return repairCode10;
	}


	/**
	 * @param repairCode10 the repairCode10 to set
	 */
	public void setRepairCode10(String repairCode10) {
		this.repairCode10 = repairCode10;
	}


	/**
	 * @return the repairCode11
	 */
	public String getRepairCode11() {
		return repairCode11;
	}


	/**
	 * @param repairCode11 the repairCode11 to set
	 */
	public void setRepairCode11(String repairCode11) {
		this.repairCode11 = repairCode11;
	}


	/**
	 * @return the repairCode12
	 */
	public String getRepairCode12() {
		return repairCode12;
	}


	/**
	 * @param repairCode12 the repairCode12 to set
	 */
	public void setRepairCode12(String repairCode12) {
		this.repairCode12 = repairCode12;
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

}