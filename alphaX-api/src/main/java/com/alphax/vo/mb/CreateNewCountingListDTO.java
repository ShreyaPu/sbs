package com.alphax.vo.mb;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Create a new counting list [O_INVLIST and O_INVPART] table DTO ")
public class CreateNewCountingListDTO {

	//For O_INVLIST
	
	@ApiModelProperty(value = "Lager - O_INVLIST-[Warehouse_ID-WAREHOUSE_NAME]")
	private String warehouse;
	
	@ApiModelProperty(value = "Zähllistenname - O_INVLIST-[NAME]")
	@NotBlank
	private String listName;
	
	@ApiModelProperty(value = "Sortierung und Druckauswahl -  O_INVLIST-[SORT_BY]")
	@NotBlank
	private String sortBy;
	
	@ApiModelProperty(value = "Hersteller - OEM - O_INVPART")
	@NotBlank
	private String manufacturer;
	
	@ApiModelProperty(value = "Teilenummer - [DB E_ETSTAMM]")
	private String partNumber;
	
	
	// For Default selection
	@ApiModelProperty(value = "Von Teilenummer - [DB E_ETSTAMM]")
	private String fromPartNumber;
	
	@ApiModelProperty(value = "Bis Teilenummer - [DB E_ETSTAMM]")
	private String toPartNumber;
	
	@ApiModelProperty(value = "Von Lagerort - [DB E_LAGO]")
	private String fromStorageLocation;
	
	@ApiModelProperty(value = "Bis Lagerort - [DB E_LAGO]")
	private String toStorageLocation;
	
	//For Special selection
	
	@ApiModelProperty(value = "In diesem Jahr bewegt")
	private boolean partMovedThisYear;
	
	@ApiModelProperty(value = "Altteile")
	private boolean usedParts;
	
	@ApiModelProperty(value = "Gefahrstoff Teile")
	private boolean materialParts;
	
	@ApiModelProperty(value = "Diebstahlrelevante Teile")
	private boolean theftRelevantParts;
	
	@ApiModelProperty(value = "Teile mit Verfallsdatum")
	private boolean  expirationDateParts;
	
	// Special selection End
	
	@ApiModelProperty(value ="Marketingcode - [DB E_ETSTAMM]." )
	private String marketingCode;
	
	@ApiModelProperty(value ="Maximale Zählpositionen." )
	private String maxCountingPosition;
	
	@ApiModelProperty(value ="Zählgruppe. [DB E_ETSTAMM and PMH_HERST]" )
	private String countingGroup;
	
	@ApiModelProperty(value ="Teilemarke. [DB E_ETSTAMM]" )
	private String partBrand;
	
	@ApiModelProperty(value ="Preisspanne. [DB E_ETSTAMM]" )
	private boolean priceRange;
	
	@ApiModelProperty(value ="Von Preisspanne. [DB E_ETSTAMM]" )
	private String fromPriceRange;
	
	@ApiModelProperty(value ="Bis Preisspanne. [DB E_ETSTAMM]" )
	private String toPriceRange;
	
	@ApiModelProperty(value ="Auch bereits gezählte Positionen selektieren. [DB E_ETSTAMM]" )
	private boolean alreadyCountedItems;
	
	@ApiModelProperty(value ="Satzart. [DB E_ETSTAMM]" )
	private String storageIndikator;
	
	@ApiModelProperty(value = "Lagerorte - [DB E_LAGO]")
	private String storageLocation;
	
	@ApiModelProperty(value = "Bestandswert geringer als - [DB E_ETSTAMM]")
	private String inventoryValueRange;
	
	@ApiModelProperty(value = "Eingeschränkter Teilenummernbereich")
	private boolean partNumberRangeFlag;
	
	@ApiModelProperty(value = "alle Teile auswählen")
	private boolean allParts;
	
	@ApiModelProperty(value = "alle Lagerotbereiche auswählen")
	private boolean allStorageLocations;

	/**
	 * @return the warehouse
	 */
	public String getWarehouse() {
		return warehouse;
	}

	/**
	 * @param warehouse the warehouse to set
	 */
	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	/**
	 * @return the listName
	 */
	public String getListName() {
		return listName;
	}

	/**
	 * @param listName the listName to set
	 */
	public void setListName(String listName) {
		this.listName = listName;
	}

	/**
	 * @return the sortBy
	 */
	public String getSortBy() {
		return sortBy;
	}

	/**
	 * @param sortBy the sortBy to set
	 */
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

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
	 * @return the fromPartNumber
	 */
	public String getFromPartNumber() {
		return fromPartNumber;
	}

	/**
	 * @param fromPartNumber the fromPartNumber to set
	 */
	public void setFromPartNumber(String fromPartNumber) {
		this.fromPartNumber = fromPartNumber;
	}

	/**
	 * @return the toPartNumber
	 */
	public String getToPartNumber() {
		return toPartNumber;
	}

	/**
	 * @param toPartNumber the toPartNumber to set
	 */
	public void setToPartNumber(String toPartNumber) {
		this.toPartNumber = toPartNumber;
	}

	/**
	 * @return the fromStorageLocation
	 */
	public String getFromStorageLocation() {
		return fromStorageLocation;
	}

	/**
	 * @param fromStorageLocation the fromStorageLocation to set
	 */
	public void setFromStorageLocation(String fromStorageLocation) {
		this.fromStorageLocation = fromStorageLocation;
	}

	/**
	 * @return the toStorageLocation
	 */
	public String getToStorageLocation() {
		return toStorageLocation;
	}

	/**
	 * @param toStorageLocation the toStorageLocation to set
	 */
	public void setToStorageLocation(String toStorageLocation) {
		this.toStorageLocation = toStorageLocation;
	}

	/**
	 * @return the partMovedThisYear
	 */
	public boolean isPartMovedThisYear() {
		return partMovedThisYear;
	}

	/**
	 * @param partMovedThisYear the partMovedThisYear to set
	 */
	public void setPartMovedThisYear(boolean partMovedThisYear) {
		this.partMovedThisYear = partMovedThisYear;
	}

	/**
	 * @return the usedParts
	 */
	public boolean isUsedParts() {
		return usedParts;
	}

	/**
	 * @param usedParts the usedParts to set
	 */
	public void setUsedParts(boolean usedParts) {
		this.usedParts = usedParts;
	}

	/**
	 * @return the materialParts
	 */
	public boolean isMaterialParts() {
		return materialParts;
	}

	/**
	 * @param materialParts the materialParts to set
	 */
	public void setMaterialParts(boolean materialParts) {
		this.materialParts = materialParts;
	}

	/**
	 * @return the theftRelevantParts
	 */
	public boolean isTheftRelevantParts() {
		return theftRelevantParts;
	}

	/**
	 * @param theftRelevantParts the theftRelevantParts to set
	 */
	public void setTheftRelevantParts(boolean theftRelevantParts) {
		this.theftRelevantParts = theftRelevantParts;
	}

	/**
	 * @return the expirationDateParts
	 */
	public boolean isExpirationDateParts() {
		return expirationDateParts;
	}

	/**
	 * @param expirationDateParts the expirationDateParts to set
	 */
	public void setExpirationDateParts(boolean expirationDateParts) {
		this.expirationDateParts = expirationDateParts;
	}

	/**
	 * @return the marketingCode
	 */
	public String getMarketingCode() {
		return marketingCode;
	}

	/**
	 * @param marketingCode the marketingCode to set
	 */
	public void setMarketingCode(String marketingCode) {
		this.marketingCode = marketingCode;
	}

	/**
	 * @return the maxCountingPosition
	 */
	public String getMaxCountingPosition() {
		return maxCountingPosition;
	}

	/**
	 * @param maxCountingPosition the maxCountingPosition to set
	 */
	public void setMaxCountingPosition(String maxCountingPosition) {
		this.maxCountingPosition = maxCountingPosition;
	}

	/**
	 * @return the countingGroup
	 */
	public String getCountingGroup() {
		return countingGroup;
	}

	/**
	 * @param countingGroup the countingGroup to set
	 */
	public void setCountingGroup(String countingGroup) {
		this.countingGroup = countingGroup;
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
	 * @return the priceRange
	 */
	public boolean isPriceRange() {
		return priceRange;
	}

	/**
	 * @param priceRange the priceRange to set
	 */
	public void setPriceRange(boolean priceRange) {
		this.priceRange = priceRange;
	}

	/**
	 * @return the fromPriceRange
	 */
	public String getFromPriceRange() {
		return fromPriceRange;
	}

	/**
	 * @param fromPriceRange the fromPriceRange to set
	 */
	public void setFromPriceRange(String fromPriceRange) {
		this.fromPriceRange = fromPriceRange;
	}

	/**
	 * @return the toPriceRange
	 */
	public String getToPriceRange() {
		return toPriceRange;
	}

	/**
	 * @param toPriceRange the toPriceRange to set
	 */
	public void setToPriceRange(String toPriceRange) {
		this.toPriceRange = toPriceRange;
	}

	/**
	 * @return the alreadyCountedItems
	 */
	public boolean isAlreadyCountedItems() {
		return alreadyCountedItems;
	}

	/**
	 * @param alreadyCountedItems the alreadyCountedItems to set
	 */
	public void setAlreadyCountedItems(boolean alreadyCountedItems) {
		this.alreadyCountedItems = alreadyCountedItems;
	}

	/**
	 * @return the storageIndikator
	 */
	public String getStorageIndikator() {
		return storageIndikator;
	}

	/**
	 * @param storageIndikator the storageIndikator to set
	 */
	public void setStorageIndikator(String storageIndikator) {
		this.storageIndikator = storageIndikator;
	}

	/**
	 * @return the storageLocation
	 */
	public String getStorageLocation() {
		return storageLocation;
	}

	/**
	 * @param storageLocation the storageLocation to set
	 */
	public void setStorageLocation(String storageLocation) {
		this.storageLocation = storageLocation;
	}

	/**
	 * @return the inventoryValueRange
	 */
	public String getInventoryValueRange() {
		return inventoryValueRange;
	}

	/**
	 * @param inventoryValueRange the inventoryValueRange to set
	 */
	public void setInventoryValueRange(String inventoryValueRange) {
		this.inventoryValueRange = inventoryValueRange;
	}

	/**
	 * @return the partNumberRangeFlag
	 */
	public boolean isPartNumberRangeFlag() {
		return partNumberRangeFlag;
	}

	/**
	 * @param partNumberRangeFlag the partNumberRangeFlag to set
	 */
	public void setPartNumberRangeFlag(boolean partNumberRangeFlag) {
		this.partNumberRangeFlag = partNumberRangeFlag;
	}

	/**
	 * @return the allParts
	 */
	public boolean isAllParts() {
		return allParts;
	}

	/**
	 * @param allParts the allParts to set
	 */
	public void setAllParts(boolean allParts) {
		this.allParts = allParts;
	}

	/**
	 * @return the allStorageLocations
	 */
	public boolean isAllStorageLocations() {
		return allStorageLocations;
	}

	/**
	 * @param allStorageLocations the allStorageLocations to set
	 */
	public void setAllStorageLocations(boolean allStorageLocations) {
		this.allStorageLocations = allStorageLocations;
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
	
	
	
}
