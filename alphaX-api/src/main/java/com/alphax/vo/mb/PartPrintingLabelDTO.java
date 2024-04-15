package com.alphax.vo.mb;

import java.util.List;

import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Validated
@ApiModel(description = "This Object about Part Printing Label details")
public class PartPrintingLabelDTO {

	@ApiModelProperty(value = "Hersteller (OEM)  - (DB E_ETSTAMM- 1-HERST).")
	@Size(max =4)
	private String oem;

	@ApiModelProperty(value ="Teilenummer - (DB E_ETSTAMM- 2-TNR)." )
	@Size(max =30)
	private String partNumber;

	@ApiModelProperty(value ="Lagernummer  - (DB E_ETSTAMM- 3-LNR)." )
	@Size(max=2)
	private String warehouse;

	@ApiModelProperty(value ="Bezeichnung  - (DB E_ETSTAMM- 7-BENEN)." )
	@Size(max=50)
	private String name;

	@ApiModelProperty(value ="Lagerort  - (DB E_ETSTAMM- 8-LOPA)." )
	@Size(max=8)
	private String storageLocation;

	@ApiModelProperty(value ="Teilenummer Druckformat  - (DB E_ETSTAMM- 179-TNRD)." )
	private String printingFormatPartNumber;

	@ApiModelProperty(value =" part price  + surcharge percentage" )
	private String purchasePriceWithSurcharge;
	
	@ApiModelProperty(value ="LagerortList  - (DB E_LAGO- LOPA)." )
	private List<StorageLocationDTO> storageLocationList;
	

	public String getOem() {
		return oem;
	}

	public void setOem(String oem) {
		this.oem = oem;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStorageLocation() {
		return storageLocation;
	}

	public void setStorageLocation(String storageLocation) {
		this.storageLocation = storageLocation;
	}

	public String getPrintingFormatPartNumber() {
		return printingFormatPartNumber;
	}

	public void setPrintingFormatPartNumber(String printingFormatPartNumber) {
		this.printingFormatPartNumber = printingFormatPartNumber;
	}

	/**
	 * @return the purchasePriceWithSurcharge
	 */
	public String getPurchasePriceWithSurcharge() {
		return purchasePriceWithSurcharge;
	}

	/**
	 * @param purchasePriceWithSurcharge the purchasePriceWithSurcharge to set
	 */
	public void setPurchasePriceWithSurcharge(String purchasePriceWithSurcharge) {
		this.purchasePriceWithSurcharge = purchasePriceWithSurcharge;
	}
	
	/**
	 * @return the storageLocationList
	 */
	public List<StorageLocationDTO> getStorageLocationList() {
		return storageLocationList;
	}

	/**
	 * @param storageLocationList the storageLocationList to set
	 */
	public void setStorageLocationList(List<StorageLocationDTO> storageLocationList) {
		this.storageLocationList = storageLocationList;
	}

}