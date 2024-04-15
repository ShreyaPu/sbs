package com.alphax.vo.mb;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "vehicle legend to containing all activities on a vehicle.")
public class VehicleLegendDTO {

	private Integer companyId;

	private Integer agencyId;

	private String assignmentNumber;

	private String assignmentDate;

	private String mileage;

	private String customerConsultant;

	private String invoiceNumber;

	private String revenue;

	private List<VehicleLegendRepairCode> repairCodeList;


	public VehicleLegendDTO() {

	}


	/**
	 * @return the companyId
	 */
	@ApiModelProperty(value = "Fa - The company identifier for the vehicle legend item (%% DB M_KFZL - 24 - FA).")
	public Integer getCompanyId() {
		return companyId;
	}


	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}


	/**
	 * @return the agencyId
	 */
	@ApiModelProperty(value = "Fi - The agency identifier for the vehicle legend item (%% DB M_KFZL - 25 - FIL).")
	public Integer getAgencyId() {
		return agencyId;
	}


	/**
	 * @param agencyId the agencyId to set
	 */
	public void setAgencyId(Integer agencyId) {
		this.agencyId = agencyId;
	}


	/**
	 * @return the assignmentNumber
	 */
	@ApiModelProperty(value = "Auftr. Nummer - The number of the assignment/order for the vehicle legend item (%% DB M_KFZL - 5 - AUFNR).")
	public String getAssignmentNumber() {
		return assignmentNumber;
	}


	/**
	 * @param assignmentNumber the assignmentNumber to set
	 */
	public void setAssignmentNumber(String assignmentNumber) {
		this.assignmentNumber = assignmentNumber;
	}


	/**
	 * @return the assignmentDate
	 */
	@ApiModelProperty(value = "Auftr. Datum - The date of the assignment/order for the vehicle legend item (%% DB M_KFZL - 6 - AUFDAT).")
	public String getAssignmentDate() {
		return assignmentDate;
	}


	/**
	 * @param assignmentDate the assignmentDate to set
	 */
	public void setAssignmentDate(String assignmentDate) {
		this.assignmentDate = assignmentDate;
	}


	/**
	 * @return the mileage
	 */
	@ApiModelProperty(value = "KM Tsd - The mileage value (in thousand kilometers) when the vehicle legend item was created (%% DB M_KFZL - 7 - KM).")
	public String getMileage() {
		return mileage;
	}


	/**
	 * @param mileage the mileage to set
	 */
	public void setMileage(String mileage) {
		this.mileage = mileage;
	}


	/**
	 * @return the customerConsultant
	 */
	@ApiModelProperty(value = "KD-Ber - The customer consultant that was involved in the vehicle legend item (%% DB M_KFZL - 8 - REPAN).")
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
	@ApiModelProperty(value = "Rechn. NummerThe invoice number related to the vehicle legend item (%% DB M_KFZL - 9 - RENR).")
	public String getInvoiceNumber() {
		return invoiceNumber;
	}


	/**
	 * @param invoiceNumber the invoiceNumber to set
	 */
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}


	/**
	 * @return the revenue
	 */
	@ApiModelProperty(value = "Umsatz - The revenue (turnover) related to the vehicle legend item (%% DB M_KFZL - 10 - REBET).")
	public String getRevenue() {
		return revenue;
	}


	/**
	 * @param revenue the revenue to set
	 */
	public void setRevenue(String revenue) {
		this.revenue = revenue;
	}


	/**
	 * @return the repairCodeList
	 */
	@ApiModelProperty(value = "Reparatur-Kennzeichen Konstruktions-Gruppe - up to 12 optional the repair codes related to the vehicle legend "
			+ "item - (DB E_ETSTAMM- 61-72 ZULM1S-ZULMCS).")
	public List<VehicleLegendRepairCode> getRepairCodeList() {
		return repairCodeList;
	}


	/**
	 * @param repairCodeList the repairCodeList to set
	 */
	public void setRepairCodeList(List<VehicleLegendRepairCode> repairCodeList) {
		this.repairCodeList = repairCodeList;
	}

}