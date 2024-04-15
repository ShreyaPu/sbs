package com.alphax.model.mb;

import java.math.BigDecimal;

import com.alphax.common.util.DBTable;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about inventory Part [O_INVPART] table ")
public class InventoryParts {

	@DBTable(columnName ="OEM", required = true)
	private String manufacturer;
	
	@DBTable(columnName ="TNR", required = true)
	private String partNumber;
	
	@DBTable(columnName ="PARTNAME", required = true)
	private String partNAME;
	
	@DBTable(columnName ="PLACE", required = true)
	private String place;
	
	@DBTable(columnName ="TMA_TMARKE", required = true)
	private String partBrand;
	
	@DBTable(columnName ="ROWNUMER", required = true)
	private Integer totalCount;	
	
	@DBTable(columnName ="invpart_id", required = true)
	private Long inventoryPartId;
	
	@DBTable(columnName ="CNT", required = true)
	private Long count;
	
	@DBTable(columnName ="OLD_STOCK", required = true)
	private BigDecimal oldStock;
	
	@DBTable(columnName ="NEW_STOCK", required = true)
	private BigDecimal newStock;
	
	@DBTable(columnName ="SUM_NEW_STOCK", required = true)
	private BigDecimal sum_newStock;
	
	@DBTable(columnName ="DAK", required = true)
	private BigDecimal dak;
	
	@DBTable(columnName ="DELTA_PIECES", required = true)
	private BigDecimal deltaPieces;
	
	@DBTable(columnName ="DELTA_CUR", required = true)
	private BigDecimal deltaCur;
	
	@DBTable(columnName ="INVPARTSTS_ID", required = true)
	private BigDecimal partStatusId;
	
	@DBTable(columnName ="USER1", required = true)
	private String user1;
	
	@DBTable(columnName ="USER2", required = true)
	private String user2;
	
	@DBTable(columnName ="Check_it", required = true)
	private String checkIt;
	
	@DBTable(columnName ="COUNT_OF_PLACES", required = true)
	private Integer countOfPlaces;
	
	@DBTable(columnName ="COUNTING_DATE", required = true)
	private String countingDate;
	
	@DBTable(columnName ="COMPARE_WRH", required = true)
	private String compareWRH;
	
	@DBTable(columnName ="COMPARE_DATE", required = true)
	private String compareDate;
	
	@DBTable(columnName ="SA", required = true)
	private BigDecimal storageIndikator;
	
	@DBTable(columnName ="INVLIST_ID", required = true)
	private Long inventoryListId;
	
	@DBTable(columnName ="ACTIVATION_DATE", required = true)
	private String activationDate;
	
	@DBTable(columnName ="TA", required = true)
	private Integer partsIndikator;
	
	@DBTable(columnName ="TMA_VORBEL", required = true)
	private String vorbelValue;

	@DBTable(columnName ="TNRD", required = true)
	private String printingFormatPartNumber;
	
	@DBTable(columnName ="TNRS", required = true)
	private String sortingFormatPartNumber;


	/**
	 * @return the manufacturer
	 */
	public String getManufacturer() {
		return manufacturer;
	}

	/**
	 * @param manufacturer the manufacturer to set
	 */
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	/**
	 * @return the partNumber
	 */
	public String getPartNumber() {
		return partNumber;
	}

	/**
	 * @param partNumber the partNumber to set
	 */
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	/**
	 * @return the partNAME
	 */
	public String getPartNAME() {
		return partNAME;
	}

	/**
	 * @param partNAME the partNAME to set
	 */
	public void setPartNAME(String partNAME) {
		this.partNAME = partNAME;
	}

	/**
	 * @return the place
	 */
	public String getPlace() {
		return place;
	}

	/**
	 * @param place the place to set
	 */
	public void setPlace(String place) {
		this.place = place;
	}

	/**
	 * @return the partBrand
	 */
	public String getPartBrand() {
		return partBrand;
	}

	/**
	 * @param partBrand the partBrand to set
	 */
	public void setPartBrand(String partBrand) {
		this.partBrand = partBrand;
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
	 * @return the inventoryPartId
	 */
	public Long getInventoryPartId() {
		return inventoryPartId;
	}

	/**
	 * @param inventoryPartId the inventoryPartId to set
	 */
	public void setInventoryPartId(Long inventoryPartId) {
		this.inventoryPartId = inventoryPartId;
	}

	/**
	 * @return the count
	 */
	public Long getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(Long count) {
		this.count = count;
	}

	/**
	 * @return the oldStock
	 */
	public BigDecimal getOldStock() {
		return oldStock;
	}

	/**
	 * @param oldStock the oldStock to set
	 */
	public void setOldStock(BigDecimal oldStock) {
		this.oldStock = oldStock;
	}

	/**
	 * @return the newStock
	 */
	public BigDecimal getNewStock() {
		return newStock;
	}

	/**
	 * @param newStock the newStock to set
	 */
	public void setNewStock(BigDecimal newStock) {
		this.newStock = newStock;
	}

	/**
	 * @return the sum_newStock
	 */
	public BigDecimal getSum_newStock() {
		return sum_newStock;
	}

	/**
	 * @param sum_newStock the sum_newStock to set
	 */
	public void setSum_newStock(BigDecimal sum_newStock) {
		this.sum_newStock = sum_newStock;
	}

	/**
	 * @return the dak
	 */
	public BigDecimal getDak() {
		return dak;
	}

	/**
	 * @param dak the dak to set
	 */
	public void setDak(BigDecimal dak) {
		this.dak = dak;
	}

	/**
	 * @return the deltaPieces
	 */
	public BigDecimal getDeltaPieces() {
		return deltaPieces;
	}

	/**
	 * @param deltaPieces the deltaPieces to set
	 */
	public void setDeltaPieces(BigDecimal deltaPieces) {
		this.deltaPieces = deltaPieces;
	}

	/**
	 * @return the deltaCur
	 */
	public BigDecimal getDeltaCur() {
		return deltaCur;
	}

	/**
	 * @param deltaCur the deltaCur to set
	 */
	public void setDeltaCur(BigDecimal deltaCur) {
		this.deltaCur = deltaCur;
	}

	/**
	 * @return the partStatusId
	 */
	public BigDecimal getPartStatusId() {
		return partStatusId;
	}

	/**
	 * @param partStatusId the partStatusId to set
	 */
	public void setPartStatusId(BigDecimal partStatusId) {
		this.partStatusId = partStatusId;
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
	 * @return the checkIt
	 */
	public String getCheckIt() {
		return checkIt;
	}

	/**
	 * @param checkIt the checkIt to set
	 */
	public void setCheckIt(String checkIt) {
		this.checkIt = checkIt;
	}

	/**
	 * @return the countOfPlaces
	 */
	public Integer getCountOfPlaces() {
		return countOfPlaces;
	}

	/**
	 * @param countOfPlaces the countOfPlaces to set
	 */
	public void setCountOfPlaces(Integer countOfPlaces) {
		this.countOfPlaces = countOfPlaces;
	}

	/**
	 * @return the countingDate
	 */
	public String getCountingDate() {
		return countingDate;
	}

	/**
	 * @param countingDate the countingDate to set
	 */
	public void setCountingDate(String countingDate) {
		this.countingDate = countingDate;
	}

	/**
	 * @return the compareWRH
	 */
	public String getCompareWRH() {
		return compareWRH;
	}

	/**
	 * @param compareWRH the compareWRH to set
	 */
	public void setCompareWRH(String compareWRH) {
		this.compareWRH = compareWRH;
	}

	/**
	 * @return the compareDate
	 */
	public String getCompareDate() {
		return compareDate;
	}

	/**
	 * @param compareDate the compareDate to set
	 */
	public void setCompareDate(String compareDate) {
		this.compareDate = compareDate;
	}

	/**
	 * @return the storageIndikator
	 */
	public BigDecimal getStorageIndikator() {
		return storageIndikator;
	}

	/**
	 * @param storageIndikator the storageIndikator to set
	 */
	public void setStorageIndikator(BigDecimal storageIndikator) {
		this.storageIndikator = storageIndikator;
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
	 * @return the partsIndikator
	 */
	public Integer getPartsIndikator() {
		return partsIndikator;
	}

	/**
	 * @param partsIndikator the partsIndikator to set
	 */
	public void setPartsIndikator(Integer partsIndikator) {
		this.partsIndikator = partsIndikator;
	}

	/**
	 * @return the vorbelValue
	 */
	public String getVorbelValue() {
		return vorbelValue;
	}

	/**
	 * @param vorbelValue the vorbelValue to set
	 */
	public void setVorbelValue(String vorbelValue) {
		this.vorbelValue = vorbelValue;
	}

	/**
	 * @return the printingFormatPartNumber
	 */
	public String getPrintingFormatPartNumber() {
		return printingFormatPartNumber;
	}

	/**
	 * @param printingFormatPartNumber the printingFormatPartNumber to set
	 */
	public void setPrintingFormatPartNumber(String printingFormatPartNumber) {
		this.printingFormatPartNumber = printingFormatPartNumber;
	}

	/**
	 * @return the sortingFormatPartNumber
	 */
	public String getSortingFormatPartNumber() {
		return sortingFormatPartNumber;
	}

	/**
	 * @param sortingFormatPartNumber the sortingFormatPartNumber to set
	 */
	public void setSortingFormatPartNumber(String sortingFormatPartNumber) {
		this.sortingFormatPartNumber = sortingFormatPartNumber;
	}	
	
	
}
