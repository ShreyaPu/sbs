package com.alphax.vo.mb;

import org.springframework.validation.annotation.Validated;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Validated
@ApiModel(description = "This Object about inventory overview details")
public class InventoryOverviewDTO {

	@ApiModelProperty(value = "Teilenummer (OEM)  - (DB E_ETSTAMM- HERST).")
	private String manufacturer;

	@ApiModelProperty(value ="Teilenummer - (DB E_ETSTAMM- TNR)." )
	private String partNumber;

	@ApiModelProperty(value ="Lagernummer  - (DB E_ETSTAMM- LNR)." )
	private String warehouseNo;
	
	@ApiModelProperty(value ="Bezeichnung  - (DB E_ETSTAMM- BENEN)." )
	private String partDescription;
	
	@ApiModelProperty(value ="DAK - (DB E_ETSTAMM- DAK)." )
	private String dak;
	
	@ApiModelProperty(value ="Teilemarke - (DB E_ETSTAMM- TMARKE)." )
	private String partBrand;

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
	 * @return the warehouseNo
	 */
	public String getWarehouseNo() {
		return warehouseNo;
	}

	/**
	 * @param warehouseNo the warehouseNo to set
	 */
	public void setWarehouseNo(String warehouseNo) {
		this.warehouseNo = warehouseNo;
	}

	/**
	 * @return the partDescription
	 */
	public String getPartDescription() {
		return partDescription;
	}

	/**
	 * @param partDescription the partDescription to set
	 */
	public void setPartDescription(String partDescription) {
		this.partDescription = partDescription;
	}

	/**
	 * @return the dak
	 */
	public String getDak() {
		return dak;
	}

	/**
	 * @param dak the dak to set
	 */
	public void setDak(String dak) {
		this.dak = dak;
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
	
	
	
}
