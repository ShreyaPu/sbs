package com.alphax.vo.mb;

import io.swagger.annotations.ApiModelProperty;

public class LagerPartDetailsDTO {
	
	@ApiModelProperty(value ="Lagernummer  - (DB E_ETSTAMM - LNR)." )
	private String warehouseNo;

	@ApiModelProperty(value ="Lager Name - (DB E_ETSTAMK4 - PNAME)." )
	private String WarehouseName;

	@ApiModelProperty(value ="Datum letzter Abgang - (DB E_ETSTAMM- DTLABG)." )
	private String lastDisposalDate;
	
	@ApiModelProperty(value ="Aktueller Bestand - (DB E_ETSTAMM- AKTBES)." )
	private String currentStock;
	
	@ApiModelProperty(value ="Satzart  - (DB E_ETSTAMM - SA)." )
	private String storageIndikator;
	
	@ApiModelProperty(value ="DAK  - (DB E_ETSTAMM - DAK)." )
	private String averageNetPrice;	
	
	private boolean movementAllowed;
	
	
	public LagerPartDetailsDTO() {

	}


	public String getWarehouseNo() {
		return warehouseNo;
	}


	public void setWarehouseNo(String warehouseNo) {
		this.warehouseNo = warehouseNo;
	}


	public String getWarehouseName() {
		return WarehouseName;
	}


	public void setWarehouseName(String warehouseName) {
		WarehouseName = warehouseName;
	}


	public String getLastDisposalDate() {
		return lastDisposalDate;
	}


	public void setLastDisposalDate(String lastDisposalDate) {
		this.lastDisposalDate = lastDisposalDate;
	}


	public String getCurrentStock() {
		return currentStock;
	}


	public void setCurrentStock(String currentStock) {
		this.currentStock = currentStock;
	}


	public String getStorageIndikator() {
		return storageIndikator;
	}


	public void setStorageIndikator(String storageIndikator) {
		this.storageIndikator = storageIndikator;
	}


	/**
	 * @return the averageNetPrice
	 */
	public String getAverageNetPrice() {
		return averageNetPrice;
	}


	/**
	 * @param averageNetPrice the averageNetPrice to set
	 */
	public void setAverageNetPrice(String averageNetPrice) {
		this.averageNetPrice = averageNetPrice;
	}


	/**
	 * @return the movementAllowed
	 */
	public boolean isMovementAllowed() {
		return movementAllowed;
	}


	/**
	 * @param movementAllowed the movementAllowed to set
	 */
	public void setMovementAllowed(boolean movementAllowed) {
		this.movementAllowed = movementAllowed;
	}

}