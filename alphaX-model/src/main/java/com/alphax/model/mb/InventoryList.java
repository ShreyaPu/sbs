package com.alphax.model.mb;

import java.math.BigDecimal;
import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about inventory List [O_INVLIST] table ")
public class InventoryList {

	@DBTable(columnName ="INVLIST_ID", required = true)
	private Long inventoryListId;
	
	@DBTable(columnName ="NAME", required = true)
	private String inventoryListName;
	
	@DBTable(columnName ="INVLISTSTS_ID", required = true)
	private BigDecimal inventoryList_Status_Id;
	
	@DBTable(columnName ="KIND_ID", required = true)
	private Integer inventoryList_KindId;
	
	@DBTable(columnName ="WAREHOUSE_ID", required = true)  
	private BigDecimal warehouseId;
	
	@DBTable(columnName ="WAREHOUSE_NAME", required = true)
	private String warehouseName;
	
	@DBTable(columnName ="ORIGIN_ID", required = true)
	private Integer originId;
	
	@DBTable(columnName ="SORT_BY", required = true)
	private BigDecimal sortBy;
	
	@DBTable(columnName ="CREATED_TS", required = true)
	private String creationDate;
	
	@DBTable(columnName ="CREATED_BY", required = true)
	private String createdBy;
	
	@DBTable(columnName ="PRINT_DATE", required = true)
	private String printDate;
	
	@DBTable(columnName ="ACTIVATION_DATE", required = true)
	private String activationDate;
	
	@DBTable(columnName ="PLANNED_EMPLOYEE", required = true)
	private String plannedEmployee;
	
	@DBTable(columnName ="PLANNED_DATE", required = true)
	private String plannedDate;
	
	@DBTable(columnName ="DOWNLOAD_DATE", required = true)
	private String downloadDate;
	
	@DBTable(columnName ="DOWNLOAD_BY", required = true)
	private String downloadBy;
	
	@DBTable(columnName ="UPLOAD_DATE", required = true)
	private String uploadDate;	
	
	@DBTable(columnName ="UPLOAD_BY", required = true)
	private String uploadBy;
	
	@DBTable(columnName ="COUNT", required = true)
	private Integer inventoryCount;
	
	@DBTable(columnName ="INVLISTSTS_DESC", required = true)
	private String statusDescription;
	
	@DBTable(columnName ="COUNT_SA1", required = true)
	private String numberOfRrecordType1;
	
	@DBTable(columnName ="COUNT_SA2", required = true)
	private String numberOfRrecordType2;
	
	@DBTable(columnName ="ROWNUMER", required = true)
	private Integer totalCount;
	
	@DBTable(columnName ="COUNTINGPOSITION", required = true)
	private String countingPosition;
	
	@DBTable(columnName ="ALREADYRECORDED", required = true)
	private String alreadyRecorded;
	
	@DBTable(columnName ="USER1", required = true)
	private String user1;
	
	@DBTable(columnName ="USER2", required = true)
	private String user2;
	
	@DBTable(columnName ="CREATED_TIME", required = true)
	private String invCreationDate;
	
	@DBTable(columnName ="AX_COMAPNY_NAME", required = true)
	private String axCompanyName;
	
	@DBTable(columnName ="CREATED_TS_NOW", required = true)
	private String creationNOWDate;
	
	@DBTable(columnName ="AP_WAREHOUS_ID", required = true)  
	private String alphaPlusWarehouseId;
	
	public InventoryList() {
	
	}





	public String getAlphaPlusWarehouseId() {
		return alphaPlusWarehouseId;
	}





	public void setAlphaPlusWarehouseId(String alphaPlusWarehouseId) {
		this.alphaPlusWarehouseId = alphaPlusWarehouseId;
	}





	/**
	 * @return the inventoryListId
	 */
	public Long getInventoryListId() {
		return inventoryListId;
	}


	/**
	 * @param inventoryListId the inventoryListId to set
	 */
	public void setInventoryListId(Long inventoryListId) {
		this.inventoryListId = inventoryListId;
	}


	/**
	 * @return the inventoryListName
	 */
	public String getInventoryListName() {
		return inventoryListName;
	}


	/**
	 * @param inventoryListName the inventoryListName to set
	 */
	public void setInventoryListName(String inventoryListName) {
		this.inventoryListName = inventoryListName;
	}


	/**
	 * @return the inventoryList_Status_Id
	 */
	public BigDecimal getInventoryList_Status_Id() {
		return inventoryList_Status_Id;
	}


	/**
	 * @param inventoryList_Status_Id the inventoryList_Status_Id to set
	 */
	public void setInventoryList_Status_Id(BigDecimal inventoryList_Status_Id) {
		this.inventoryList_Status_Id = inventoryList_Status_Id;
	}


	/**
	 * @return the inventoryList_KindId
	 */
	public Integer getInventoryList_KindId() {
		return inventoryList_KindId;
	}


	/**
	 * @param inventoryList_KindId the inventoryList_KindId to set
	 */
	public void setInventoryList_KindId(Integer inventoryList_KindId) {
		this.inventoryList_KindId = inventoryList_KindId;
	}


	/**
	 * @return the warehouseId
	 */
	public BigDecimal getWarehouseId() {
		return warehouseId;
	}


	/**
	 * @param warehouseId the warehouseId to set
	 */
	public void setWarehouseId(BigDecimal warehouseId) {
		this.warehouseId = warehouseId;
	}


	/**
	 * @return the warehouseName
	 */
	public String getWarehouseName() {
		return warehouseName;
	}


	/**
	 * @param warehouseName the warehouseName to set
	 */
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}


	/**
	 * @return the originId
	 */
	public Integer getOriginId() {
		return originId;
	}


	/**
	 * @param originId the originId to set
	 */
	public void setOriginId(Integer originId) {
		this.originId = originId;
	}


	/**
	 * @return the sortBy
	 */
	public BigDecimal getSortBy() {
		return sortBy;
	}


	/**
	 * @param sortBy the sortBy to set
	 */
	public void setSortBy(BigDecimal sortBy) {
		this.sortBy = sortBy;
	}


	/**
	 * @return the creationDate
	 */
	public String getCreationDate() {
		return creationDate;
	}


	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}


	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}


	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


	/**
	 * @return the printDate
	 */
	public String getPrintDate() {
		return printDate;
	}


	/**
	 * @param printDate the printDate to set
	 */
	public void setPrintDate(String printDate) {
		this.printDate = printDate;
	}


	/**
	 * @return the activationDate
	 */
	public String getActivationDate() {
		return activationDate;
	}


	/**
	 * @param activationDate the activationDate to set
	 */
	public void setActivationDate(String activationDate) {
		this.activationDate = activationDate;
	}


	/**
	 * @return the plannedEmployee
	 */
	public String getPlannedEmployee() {
		return plannedEmployee;
	}


	/**
	 * @param plannedEmployee the plannedEmployee to set
	 */
	public void setPlannedEmployee(String plannedEmployee) {
		this.plannedEmployee = plannedEmployee;
	}


	/**
	 * @return the plannedDate
	 */
	public String getPlannedDate() {
		return plannedDate;
	}


	/**
	 * @param plannedDate the plannedDate to set
	 */
	public void setPlannedDate(String plannedDate) {
		this.plannedDate = plannedDate;
	}


	/**
	 * @return the downloadDate
	 */
	public String getDownloadDate() {
		return downloadDate;
	}


	/**
	 * @param downloadDate the downloadDate to set
	 */
	public void setDownloadDate(String downloadDate) {
		this.downloadDate = downloadDate;
	}


	/**
	 * @return the downloadBy
	 */
	public String getDownloadBy() {
		return downloadBy;
	}


	/**
	 * @param downloadBy the downloadBy to set
	 */
	public void setDownloadBy(String downloadBy) {
		this.downloadBy = downloadBy;
	}


	/**
	 * @return the uploadDate
	 */
	public String getUploadDate() {
		return uploadDate;
	}


	/**
	 * @param uploadDate the uploadDate to set
	 */
	public void setUploadDate(String uploadDate) {
		this.uploadDate = uploadDate;
	}


	/**
	 * @return the uploadBy
	 */
	public String getUploadBy() {
		return uploadBy;
	}


	/**
	 * @param uploadBy the uploadBy to set
	 */
	public void setUploadBy(String uploadBy) {
		this.uploadBy = uploadBy;
	}


	/**
	 * @return the inventoryCount
	 */
	public Integer getInventoryCount() {
		return inventoryCount;
	}


	/**
	 * @param inventoryCount the inventoryCount to set
	 */
	public void setInventoryCount(Integer inventoryCount) {
		this.inventoryCount = inventoryCount;
	}


	/**
	 * @return the statusDescription
	 */
	public String getStatusDescription() {
		return statusDescription;
	}


	/**
	 * @param statusDescription the statusDescription to set
	 */
	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}


	/**
	 * @return the numberOfRrecordType1
	 */
	public String getNumberOfRrecordType1() {
		return numberOfRrecordType1;
	}


	/**
	 * @param numberOfRrecordType1 the numberOfRrecordType1 to set
	 */
	public void setNumberOfRrecordType1(String numberOfRrecordType1) {
		this.numberOfRrecordType1 = numberOfRrecordType1;
	}


	/**
	 * @return the numberOfRrecordType2
	 */
	public String getNumberOfRrecordType2() {
		return numberOfRrecordType2;
	}


	/**
	 * @param numberOfRrecordType2 the numberOfRrecordType2 to set
	 */
	public void setNumberOfRrecordType2(String numberOfRrecordType2) {
		this.numberOfRrecordType2 = numberOfRrecordType2;
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


	/**
	 * @return the countingPosition
	 */
	public String getCountingPosition() {
		return countingPosition;
	}


	/**
	 * @param countingPosition the countingPosition to set
	 */
	public void setCountingPosition(String countingPosition) {
		this.countingPosition = countingPosition;
	}


	/**
	 * @return the alreadyRecorded
	 */
	public String getAlreadyRecorded() {
		return alreadyRecorded;
	}


	/**
	 * @param alreadyRecorded the alreadyRecorded to set
	 */
	public void setAlreadyRecorded(String alreadyRecorded) {
		this.alreadyRecorded = alreadyRecorded;
	}


	/**
	 * @return the user1
	 */
	public String getUser1() {
		return user1;
	}


	/**
	 * @param user1 the user1 to set
	 */
	public void setUser1(String user1) {
		this.user1 = user1;
	}


	/**
	 * @return the user2
	 */
	public String getUser2() {
		return user2;
	}


	/**
	 * @param user2 the user2 to set
	 */
	public void setUser2(String user2) {
		this.user2 = user2;
	}


	/**
	 * @return the invCreationDate
	 */
	public String getInvCreationDate() {
		return invCreationDate;
	}


	/**
	 * @param invCreationDate the invCreationDate to set
	 */
	public void setInvCreationDate(String invCreationDate) {
		this.invCreationDate = invCreationDate;
	}


	/**
	 * @return the axCompanyName
	 */
	public String getAxCompanyName() {
		return axCompanyName;
	}


	/**
	 * @param axCompanyName the axCompanyName to set
	 */
	public void setAxCompanyName(String axCompanyName) {
		this.axCompanyName = axCompanyName;
	}


	/**
	 * @return the creationNOWDate
	 */
	public String getCreationNOWDate() {
		return creationNOWDate;
	}


	/**
	 * @param creationNOWDate the creationNOWDate to set
	 */
	public void setCreationNOWDate(String creationNOWDate) {
		this.creationNOWDate = creationNOWDate;
	}
	
	
	
}