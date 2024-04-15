package com.alphax.vo.mb;

import org.springframework.validation.annotation.Validated;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Validated
@ApiModel(description = "This Object about Selections for Inventory structure Details")
public class ReportSelectionsForInventoryDTO {
	
	
	@ApiModelProperty(value = "Leading column value for Preisstruktur / Satzarten / Teilearten / Marken / Rabattgruppen / Inventurkennzeichen")
	private String leadingColumnValue ;
	
	@ApiModelProperty(value = "Bestandswert in €")
	private String InventoryValueInEur;
	
	@ApiModelProperty(value = "Bestandswert in %")
	private String InventoryValueInPct;
	
	@ApiModelProperty(value = "Anzahl Positionen")
	private String numberOfPositions;
	
	@ApiModelProperty(value = "Positionen in %")
	private String positionsInPct;
	
	@ApiModelProperty(value = "fromValue (DB O_CURRANGE - FROM_CUR)")
	private String fromCurValue;
	
	/**
	 * @return the inventoryValueInEur
	 */
	public String getInventoryValueInEur() {
		return InventoryValueInEur;
	}

	/**
	 * @param inventoryValueInEur the inventoryValueInEur to set
	 */
	public void setInventoryValueInEur(String inventoryValueInEur) {
		InventoryValueInEur = inventoryValueInEur;
	}

	/**
	 * @return the inventoryValueInPct
	 */
	public String getInventoryValueInPct() {
		return InventoryValueInPct;
	}

	/**
	 * @param inventoryValueInPct the inventoryValueInPct to set
	 */
	public void setInventoryValueInPct(String inventoryValueInPct) {
		InventoryValueInPct = inventoryValueInPct;
	}

	/**
	 * @return the numberOfPositions
	 */
	public String getNumberOfPositions() {
		return numberOfPositions;
	}

	/**
	 * @param numberOfPositions the numberOfPositions to set
	 */
	public void setNumberOfPositions(String numberOfPositions) {
		this.numberOfPositions = numberOfPositions;
	}

	/**
	 * @return the positionsInPct
	 */
	public String getPositionsInPct() {
		return positionsInPct;
	}

	/**
	 * @param positionsInPct the positionsInPct to set
	 */
	public void setPositionsInPct(String positionsInPct) {
		this.positionsInPct = positionsInPct;
	}

	/**
	 * @return the leadingColumnValue
	 */
	public String getLeadingColumnValue() {
		return leadingColumnValue;
	}

	/**
	 * @param leadingColumnValue the leadingColumnValue to set
	 */
	public void setLeadingColumnValue(String leadingColumnValue) {
		this.leadingColumnValue = leadingColumnValue;
	}

	/**
	 * @return the fromCurValue
	 */
	public String getFromCurValue() {
		return fromCurValue;
	}

	/**
	 * @param fromCurValue the fromCurValue to set
	 */
	public void setFromCurValue(String fromCurValue) {
		this.fromCurValue = fromCurValue;
	}
	
}
