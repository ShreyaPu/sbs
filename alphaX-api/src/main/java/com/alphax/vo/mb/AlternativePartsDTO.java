package com.alphax.vo.mb;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about Alternative Parts Details")
public class AlternativePartsDTO {
	
	private String partNumber;
	
	private String partName;
	
	private String type;
	
	private String currentStock;
	
	/*private String warehouseNo;
	
	private String companyId;
	
	private String agencyId;
	
	private String oem;*/
	
	private List<KeyValuePairDTO> keyValueList;
	

	/**
	 * @return the partNumber
	 */
	@ApiModelProperty(value = "part Number ( %% - DB ETHIPS1Z1 - TNR).")
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
	 * @return the partName
	 */
	@ApiModelProperty(value = "part Name ( %% - DB E_ETSTAMM - BENEN).")
	public String getPartName() {
		return partName;
	}

	/**
	 * @param partName the partName to set
	 */
	public void setPartName(String partName) {
		this.partName = partName;
	}
	
	/**
	 * @return the keyValueInfo
	 */
	@ApiModelProperty(value = "this list use for part code and text value ( %% - DB ETHTXTTAB - CODE AND TEXT).")
	public List<KeyValuePairDTO> getKeyValueList() {
		return keyValueList;
	}

	/**
	 * @param keyValueInfo the keyValueInfo to set
	 */
	public void setKeyValueList(List<KeyValuePairDTO> keyValueList) {
		this.keyValueList = keyValueList;
	}

	/**
	 * @return the type
	 */
	@ApiModelProperty(value = "type of part like Actual, successor, predecessor")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@ApiModelProperty(value ="Aktueller Bestand - (DB E_ETSTAMM- 29-AKTBES)." )
	public String getCurrentStock() {
		return currentStock;
	}

	public void setCurrentStock(String currentStock) {
		this.currentStock = currentStock;
	}
	/*@ApiModelProperty(value ="Lagernummer  - (DB E_ETSTAMM- 3-LNR)." )
	public String getWarehouseNo() {
		return warehouseNo;
	}

	public void setWarehouseNo(String warehouseNo) {
		this.warehouseNo = warehouseNo;
	}

	@ApiModelProperty(value ="Firma - (DB E_ETSTAMM- 208-FIRMA)." )
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	@ApiModelProperty(value ="Filiale - (DB E_ETSTAMM- 209-FILIAL)." )
	public String getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}

	@ApiModelProperty(value = "Hersteller (OEM)  - (DB E_ETSTAMM- 1-HERST).")
	public String getOem() {
		return oem;
	}

	public void setOem(String oem) {
		this.oem = oem;
	}*/
	
	
	

}
