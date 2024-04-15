package com.alphax.api.mb;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.alphax.common.response.AlphaXResponse;
import com.alphax.util.RestURIConstants;
import com.alphax.vo.mb.PartPrintingLabelDTO;
import com.alphax.vo.mb.ReportForBookingAccountChangesDTO;
import com.alphax.vo.mb.ReportForBookingStockDifferencesDTO;
import com.alphax.vo.mb.ReportSelectionsDTO;
import com.alphax.vo.mb.ReportSelectionsForBusinessCaseDTO;
import com.alphax.vo.mb.ReportSelectionsForInventoryDTO;
import com.alphax.vo.mb.ReportSelectionsForMovementDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

/**
 * @author A87740971
 *
 */

@Api(value = "ALPHAX",  tags = {"ReportSelections"} )
@SwaggerDefinition(tags = {
		@Tag(name = "ReportSelections", description = "REST controller that provides information for Report Selections")
})
public interface ReportSelectionsApi {
	
	@ApiOperation(value = "Returns a list of selections from stock", notes = "Returns selections from stock (Selektion aus Bestand) based on datalibrary and schema", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get selections from stock (Selektion aus Bestand) list", response = ReportSelectionsDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get selections from stock ((Selektion aus Bestand)) due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_SELECTIONS_FROM_STOCK, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getSelectionsFromStock(
			@ApiParam(value = "Manufacturer name", required = true)  @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@ApiParam(value = "List of Warehouse numbers", required = true) @NotEmpty @RequestParam(value = "warehouseNos", required = true) List<String> warehouseNos,
			@ApiParam(value = "Part with expiry date", required = false) @RequestParam(value = "partExpiryDate", required = false) boolean partExpiryDate,
			@ApiParam(value = "With holdings", required = false)  @RequestParam(value = "withHoldings", required = false) boolean withHoldings,
			@ApiParam(value = "Without inventory", required = false)  @RequestParam(value = "withoutInventory", required = false) boolean withoutInventory,
			@ApiParam(value = "Negative inventory", required = false) @RequestParam(value = "negativeInventory", required = false) boolean negativeInventory,
			@ApiParam(value = "Relevant to theft", required = true)  @RequestParam(value = "relevantToTheft", required = false) boolean relevantToTheft,
			@ApiParam(value = "Old part", required = true)  @RequestParam(value = "oldPart", required = false) boolean oldPart,
			@ApiParam(value = "Record type 1 only", required = false)  @RequestParam(value = "recordType1", required = false) boolean recordType1,
			@ApiParam(value = "Record type 2 only", required = false)  @RequestParam(value = "recordType2", required = false) boolean recordType2,
			@ApiParam(value = "Hazardous Material Parts", required = false)  @RequestParam(value = "hazardousMaterialPart", required = false) boolean hazardousMaterialPart,
			@ApiParam(value = "With Multiple Storage Locations", required = false)  @RequestParam(value = "multipleStorageLocations", required = false) boolean multipleStorageLocations,
			@ApiParam(value = "Number of records in each page", required = false)  @RequestParam(value = "pageSize", required = false) String pageSize, 
			@ApiParam(value = "Page Number to show", required = false)  @RequestParam(value = "pageNumber", required = false) String pageNumber);

	
	@ApiOperation(value = "Generate a report from stock", notes = "Generate a report from stock (Selektion aus Bestand -CSV) based on datalibrary and schema", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Generate a report from stock (Selektion aus Bestand -CSV )", response = ReportSelectionsDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Generate a report from stock (Selektion aus Bestand -CSV ) due to server Error")
	})
	@GetMapping( value = RestURIConstants.GENERATE_REPORT_FROM_STOCK, produces = {"application/JSON"} )
	public <T> ResponseEntity<T> generateReportFromStock(
			@ApiParam(value = "Manufacturer name", required = true)  @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@ApiParam(value = "List of Warehouse numbers", required = true) @NotEmpty @RequestParam(value = "warehouseNos", required = true) List<String> warehouseNos,
			@ApiParam(value = "Part with expiry date", required = false) @RequestParam(value = "partExpiryDate", required = false) boolean partExpiryDate,
			@ApiParam(value = "With holdings", required = false)  @RequestParam(value = "withHoldings", required = false) boolean withHoldings,
			@ApiParam(value = "Without inventory", required = false)  @RequestParam(value = "withoutInventory", required = false) boolean withoutInventory,
			@ApiParam(value = "Negative inventory", required = false) @RequestParam(value = "negativeInventory", required = false) boolean negativeInventory,
			@ApiParam(value = "Relevant to theft", required = true)  @RequestParam(value = "relevantToTheft", required = false) boolean relevantToTheft,
			@ApiParam(value = "Old part", required = true)  @RequestParam(value = "oldPart", required = false) boolean oldPart,
			@ApiParam(value = "Record type 1 only", required = false)  @RequestParam(value = "recordType1", required = false) boolean recordType1,
			@ApiParam(value = "Record type 2 only", required = false)  @RequestParam(value = "recordType2", required = false) boolean recordType2,
			@ApiParam(value = "Hazardous Material Parts", required = false)  @RequestParam(value = "hazardousMaterialPart", required = false) boolean hazardousMaterialPart,
			@ApiParam(value = "With Multiple Storage Locations", required = false)  @RequestParam(value = "multipleStorageLocations", required = false) boolean multipleStorageLocations);
	
	
	@ApiOperation(value = "Returns a list of selections based on marketability", notes = "Returns selections based on marketability (Selektion nach Gängigkeit)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get selections based on marketability (Selektion nach Gängigkeit) list", response = ReportSelectionsDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get selections based on marketability (Selektion nach Gängigkeit) due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_SELECTIONS_BASED_MARKETABILITY, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getSelectionsBasedMarketability(
			@ApiParam(value = "Manufacturer name", required = true)  @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@ApiParam(value = "List of Warehouse numbers", required = true) @NotEmpty @RequestParam(value = "warehouseNos", required = true) List<String> warehouseNos,
			@ApiParam(value = "no of common Parts ", required = true) @NotBlank @RequestParam(value = "noOfCommonParts", required = true) String noOfCommonParts,
			@ApiParam(value = "periods", required = true)  @NotBlank @RequestParam(value = "periods", required = true) String periods,
			@ApiParam(value = "sorting Order", required = true)  @NotBlank @RequestParam(value = "sortOrder", required = true) String sortOrder,
			@ApiParam(value = "Type of movement", required = true) @NotBlank @RequestParam(value = "moventType", required = true) String moventType,
			@ApiParam(value = "Number of records in each page", required = false)  @RequestParam(value = "pageSize", required = false) String pageSize, 
			@ApiParam(value = "Page Number to show", required = false)  @RequestParam(value = "pageNumber", required = false) String pageNumber);
	
	
	@ApiOperation(value = "Generate a report based on marketability", notes = "Generate a report based on marketability (Selektion aus Gängigkeit -CSV) based on datalibrary and schema", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Generate a report based on marketability (Selektion aus Gängigkeit -CSV )", response = ReportSelectionsDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Generate a report based on marketability (Selektion aus Gängigkeit -CSV ) due to server Error")
	})
	@GetMapping( value = RestURIConstants.GENERATE_REPORT_BASED_MARKETABILITY, produces = {"application/JSON"} )
	public <T> ResponseEntity<T> generateReportBasedOnMarketability(
			@ApiParam(value = "Manufacturer name", required = true)  @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@ApiParam(value = "List of Warehouse number", required = true) @NotEmpty @RequestParam(value = "warehouseNos", required = true) List<String> warehouseNos,
			@ApiParam(value = "no of common Parts ", required = true) @NotBlank @RequestParam(value = "noOfCommonParts", required = true) String noOfCommonParts,
			@ApiParam(value = "periods", required = true) @NotBlank @RequestParam(value = "periods", required = true) String periods,
			@ApiParam(value = "sorting Order", required = true) @NotBlank @RequestParam(value = "sortOrder", required = true) String sortOrder,
			@ApiParam(value = "Type of movement", required = true) @NotBlank @RequestParam(value = "moventType", required = true) String moventType);

	
	@ApiOperation(value = "Returns a list of selections from stock", notes = "Returns selections from stock (Bestandstruktur) based on datalibrary and schema", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get selections from stock (Bestandstruktur) list", response = ReportSelectionsForInventoryDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get selections from stock ((Selektion aus Bestand)) due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_SELECTIONS_BASED_INVENTORY, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getSelectionsForInventory(
			@ApiParam(value = "Selected Type", required = true) @NotBlank @RequestParam(value = "selectedType", required = true) String selectedType,
			@ApiParam(value = "List of Warehouse numbers", required = true) @NotEmpty @RequestParam(value = "warehouseNos", required = true) List<String> warehouseNos,
			@ApiParam(value = "Manufacturer name", required = true) @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@ApiParam(value = "Part Brand", required = true) @NotBlank @RequestParam(value = "partBrand", required = true) String partBrand);
	
	
	@ApiOperation(value = "Generate a report from Inventory Structure", notes = "Generate a report based on marketability (Bestandstruktur - CSV) based on datalibrary and schema", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Generate a report from Inventory Structure (Bestandstruktur - CSV )", response = ReportSelectionsForInventoryDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Generate a report based on marketability (Selektion aus Gängigkeit -CSV ) due to server Error")
	})
	@PostMapping( value = RestURIConstants.GENERATE_REPORT_BASED_INVENTORY, produces = {"application/JSON"} )
	public <T> ResponseEntity<T> generateReportForInventoryStructure(
			@ApiParam(value = "Report seletions for Inventory DTO Object", required = true) @Valid @RequestBody List<ReportSelectionsForInventoryDTO> selectedInventoryList,
			@ApiParam(value = "Selection Type", required = true) @NotBlank @RequestParam(value = "selectedType", required = true) String selectedType,
			@ApiParam(value = "List of Warehouse numbers", required = true) @NotEmpty @RequestParam(value = "warehouseNos", required = true) List<String> warehouseNos,
			@ApiParam(value = "Manufacturer name", required = false)  @RequestParam(value = "manufacturer", required = false) String manufacturer, 
			@ApiParam(value = "Part Brand (Marke)", required = false) @RequestParam(value = "partBrand", required = false) String partBrand);
	
	
	@ApiOperation(value = "Returns a list of selections based on Business Case Statistics for DAK", notes = "Returns selections based on Business Case Statistics for DAK", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get selections based on Business Case Statistics for DAK (Geschäftsfallstatistiken / Änderung DAK) list", response = ReportSelectionsForBusinessCaseDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get selections based on Business Case Statistics for DAK (Geschäftsfallstatistiken / Änderung DAK) due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_SELECTIONS_BASED_STATISTICS_DAK, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getBusinessCaseStatistics_DAK(
			@ApiParam(value = "From Date", required = true) @NotBlank @RequestParam(value = "fromDate", required = true) String fromDate,
			@ApiParam(value = "Till Date", required = true) @NotBlank @RequestParam(value = "toDate", required = true) String toDate);
	
	
	@ApiOperation(value = "Returns a list of selections based on Business Case Statistics for ManualCount", notes = "Returns selections based on Business Case Statistics for ManualCount", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get selections based on Business Case Statistics for ManualCount (Geschäftsfallstatistiken / Manuelle Zählung) list", response = ReportSelectionsForBusinessCaseDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get selections based on Business Case Statistics for ManualCount (Geschäftsfallstatistiken / Manuelle Zählung) due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_SELECTIONS_BASED_STATISTICS_MANUAL_COUNT, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getBusinessCaseStatistics_ManualCount(
			@ApiParam(value = "From Date", required = true) @NotBlank @RequestParam(value = "fromDate", required = true) String fromDate,
			@ApiParam(value = "Till Date", required = true) @NotBlank @RequestParam(value = "toDate", required = true) String toDate);
	
	
	@ApiOperation(value = "Returns a list of selections based on Business Case Statistics for ManualCorrection", notes = "Returns selections based on Business Case Statistics for ManualCorrection", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get selections based on Business Case Statistics for ManualCorrection (Geschäftsfallstatistiken / Manuelle Korrektur) list", response = ReportSelectionsForBusinessCaseDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get selections based on Business Case Statistics for ManualCorrection (Geschäftsfallstatistiken / Manuelle Korrektur) due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_SELECTIONS_BASED_STATISTICS_MANUAL_CORRECTION, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getBusinessCaseStatistics_ManualCorrection(
			@ApiParam(value = "From Date", required = true) @NotBlank @RequestParam(value = "fromDate", required = true) String fromDate,
			@ApiParam(value = "Till Date", required = true) @NotBlank @RequestParam(value = "toDate", required = true) String toDate);
	
	
	@ApiOperation(value = "Returns a list of selections based on Business Case Statistics for Redemptions", notes = "Returns selections based on Business Case Statistics for Redemptions", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get selections based on Business Case Statistics for Redemptions (Geschäftsfallstatistiken / Rücknahmen) list", response = ReportSelectionsForBusinessCaseDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get selections based on Business Case Statistics for Redemptions (Geschäftsfallstatistiken / Rücknahmen) due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_SELECTIONS_BASED_STATISTICS_REDEMPTION, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getBusinessCaseStatistics_Redemptions(
			@ApiParam(value = "From Date", required = true) @NotBlank @RequestParam(value = "fromDate", required = true) String fromDate,
			@ApiParam(value = "Till Date", required = true) @NotBlank @RequestParam(value = "toDate", required = true) String toDate);
	
	
	@ApiOperation(value = "Returns a list of selections based on Business Case Statistics for WithoutInventoryMovement", notes = "Returns selections based on Business Case Statistics for WithoutInventoryMovement", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get selections based on Business Case Statistics for WithoutInventoryMovement (Geschäftsfallstatistiken / Gutschrift ohne Bestandsbewegung) list", response = ReportSelectionsForBusinessCaseDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get selections based on Business Case Statistics for WithoutInventoryMovement (Geschäftsfallstatistiken / Gutschrift ohne Bestandsbewegung) due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_SELECTIONS_BASED_STATISTICS_WITHOUT_MOVEMENT, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getBusinessCaseStatistics_WithoutInventoryMovement(
			@ApiParam(value = "From Date", required = true) @NotBlank @RequestParam(value = "fromDate", required = true) String fromDate,
			@ApiParam(value = "Till Date", required = true) @NotBlank @RequestParam(value = "toDate", required = true) String toDate);
	
	
	@ApiOperation(value = "Generate a report based on BA Statistics (Geschäftsfallstatistik - CSV)", notes = "Generate a report based on BA Statistics (Geschäftsfallstatistik - CSV) using datalibrary and schema", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Generate a report based on BA Statistics (Geschäftsfallstatistik - CSV )", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Generate a report based on BA Statistics (Geschäftsfallstatistik - CSV ) due to server Error")
	})
	@GetMapping( value = RestURIConstants.GENERATE_REPORT_BASED_BA_STATISTICS, produces = {"application/JSON"} )
	public <T> ResponseEntity<T> generateReportBasedOnBAStatistics(
			@ApiParam(value = "From Date", required = true) @NotBlank @RequestParam(value = "fromDate", required = true) String fromDate,
			@ApiParam(value = "Till Date", required = true) @NotBlank @RequestParam(value = "toDate", required = true) String toDate );
	
	
	@ApiOperation(value = "Returns a list of selections from movement", notes = "Returns selections from movement (Selektion aus Bewegung) based on datalibrary and schema", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get selections from movement (Selektion aus Bewegung) list", response = ReportSelectionsForMovementDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get selections from movement (Selektion aus Bewegung) due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_SELECTIONS_FROM_MOVEMENT, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getSelectionsFromMovement(
			@ApiParam(value = "Manufacturer name", required = true)  @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@ApiParam(value = "List of Warehouse number", required = true) @NotEmpty @RequestParam(value = "warehouseNos", required = true) List<String> warehouseNos,
			@ApiParam(value = "From Date", required = true) @NotBlank @RequestParam(value = "fromDate", required = true) String fromDate,
			@ApiParam(value = "Till Date", required = true) @NotBlank @RequestParam(value = "toDate", required = true) String toDate,
			@ApiParam(value = "BusinessCase", required = false)  @RequestParam(value = "bas", required = false) String bas,
			@ApiParam(value = "inventory / customer position", required = false)  @RequestParam(value = "invtCustPos", required = false) boolean invtCustPos,
			@ApiParam(value = "customer / inventory position", required = false) @RequestParam(value = "custInvtPos", required = false) boolean custInvtPos,
			@ApiParam(value = "DAK", required = false)  @RequestParam(value = "dak", required = false) boolean dak,
			@ApiParam(value = "Changes of Marketingcode", required = false)  @RequestParam(value = "chngOfmktgCode", required = false) boolean chngOfmktgCode,
			@ApiParam(value = "Part type / supplier", required = false)  @RequestParam(value = "partTypeOrSupplier", required = false) boolean partTypeOrSupplier,
			@ApiParam(value = "Document Number", required = false)  @RequestParam(value = "documentNo", required = false) String documentNo,
			@ApiParam(value = "Customer Number", required = false)  @RequestParam(value = "customerNo", required = false) String customerNo,
			@ApiParam(value = "Supplier Number", required = false) @RequestParam(value = "supplierNo", required = false) String supplierNo,
			@ApiParam(value = "Part Number", required = false)  @RequestParam(value = "partNo", required = false) String partNo,
			@ApiParam(value = "Marketingcode", required = false)  @RequestParam(value = "furMktgCode", required = false) String furMktgCode,
			@ApiParam(value = "Brand", required = false)  @RequestParam(value = "brand", required = false) String brand,
			@ApiParam(value = "Number of records in each page", required = false)  @RequestParam(value = "pageSize", required = false) String pageSize, 
			@ApiParam(value = "Page Number to show", required = false)  @RequestParam(value = "pageNumber", required = false) String pageNumber);
	

	@ApiOperation(value = "Generate a report from movement", notes = "Generate a report from movement (Selektion aus Bewegung -CSV) based on datalibrary and schema", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Generate a report from movement (Selektion aus Bewegung -CSV )", response = ReportSelectionsDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Generate a report from movement (Selektion aus Bewegung -CSV ) due to server Error")
	})
	@GetMapping( value = RestURIConstants.GENERATE_REPORT_FROM_MOVEMENT, produces = {"application/JSON"} )
	public <T> ResponseEntity<T> generateReportFromMovement(
			@ApiParam(value = "Manufacturer name", required = true)  @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@ApiParam(value = "List of Warehouse number", required = true) @NotEmpty @RequestParam(value = "warehouseNos", required = true) List<String> warehouseNos,
			@ApiParam(value = "From Date", required = true) @NotBlank @RequestParam(value = "fromDate", required = true) String fromDate,
			@ApiParam(value = "Till Date", required = true) @NotBlank @RequestParam(value = "toDate", required = true) String toDate,
			@ApiParam(value = "BusinessCase", required = false)  @RequestParam(value = "bas", required = false) String bas,
			@ApiParam(value = "inventory / customer position", required = false)  @RequestParam(value = "invtCustPos", required = false) boolean invtCustPos,
			@ApiParam(value = "customer / inventory position", required = false) @RequestParam(value = "custInvtPos", required = false) boolean custInvtPos,
			@ApiParam(value = "DAK", required = false)  @RequestParam(value = "dak", required = false) boolean dak,
			@ApiParam(value = "Changes of Marketingcode", required = false)  @RequestParam(value = "chngOfmktgCode", required = false) boolean chngOfmktgCode,
			@ApiParam(value = "Part type / supplier", required = false)  @RequestParam(value = "partTypeOrSupplier", required = false) boolean partTypeOrSupplier,
			@ApiParam(value = "Document Number", required = false)  @RequestParam(value = "documentNo", required = false) String documentNo,
			@ApiParam(value = "Customer Number", required = false)  @RequestParam(value = "customerNo", required = false) String customerNo,
			@ApiParam(value = "Supplier Number", required = false) @RequestParam(value = "supplierNo", required = false) String supplierNo,
			@ApiParam(value = "Part Number", required = false)  @RequestParam(value = "partNo", required = false) String partNo,
			@ApiParam(value = "Marketingcode", required = false)  @RequestParam(value = "furMktgCode", required = false) String furMktgCode,
			@ApiParam(value = "Brand", required = false)  @RequestParam(value = "brand", required = false) String brand);
	
	
	@ApiOperation(value = "Returns a list of selections based on Booking related List (Buchungsrelevante Listen)", notes = "Returns a list of selections based on Booking related List (Buchungsrelevante Listen) using DB", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Returns a list of selections based on Booking related List (Buchungsrelevante Listen)", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Booking related List (Buchungsrelevante Listen) due to server Error")
	})
	@GetMapping( value = RestURIConstants.GENERATE_BOOKING_RELEVANT_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> generateBookingReletedList(
			@ApiParam(value = "List of Warehouse numbers", required = true) @NotEmpty @RequestParam(value = "warehouseNos", required = true) List<String> warehouseNos,
			@ApiParam(value = "Manufacturer name", required = true) @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@ApiParam(value = "From Date", required = true) @NotBlank @RequestParam(value = "fromDate", required = true) String fromDate,
			@ApiParam(value = "Till Date", required = true) @NotBlank @RequestParam(value = "toDate", required = true) String toDate,
			@ApiParam(value = "Include Archived Data", required = false) @RequestParam(value = "includeArchivedData", required = false) boolean includeArchivedData );
	
	
	@ApiOperation(value = "Generate a report for Booking related List (Buchungsrelevante Listen - CSV)", notes = "Generate a report for Booking related List (Buchungsrelevante Listen - CSV) using DB", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Generate a report for Booking related List (Buchungsrelevante Listen - CSV )", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Generate a report on Booking related List (Buchungsrelevante Listen - CSV ) due to server Error")
	})
	@GetMapping( value = RestURIConstants.GENERATE_REPORT_BOOKING_RELEVANT_LIST, produces = {"application/JSON"} )
	public <T> ResponseEntity<T> generateReportOnBookingReletedList(
			@ApiParam(value = "List of Warehouse number", required = true) @NotEmpty @RequestParam(value = "warehouseNos", required = true) List<String> warehouseNos,
			@ApiParam(value = "Manufacturer name", required = true) @NotBlank  @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@ApiParam(value = "From Date", required = true) @NotBlank @RequestParam(value = "fromDate", required = true) String fromDate,
			@ApiParam(value = "Till Date", required = true) @NotBlank @RequestParam(value = "toDate", required = true) String toDate,
			@ApiParam(value = "Include Archived Data", required = false) @RequestParam(value = "includeArchivedData", required = false) boolean includeArchivedData );

	
	@ApiOperation(value = "Get a List for Search columns (Selektion aus Bewegung)", notes = "Get a List for Search columns (Selektion aus Bewegung)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get a List for Search columns (Selektion aus Bewegung)", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Get a List for Search columns (Selektion aus Bewegung) due to server Error")
	})
	@GetMapping( value = RestURIConstants.SEARCH_MOVEMENT, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> searchMovementList(
			@ApiParam(value = "Search Type", required = true) @NotBlank @RequestParam(value = "searchType", required = true) String searchType,
			@ApiParam(value = "Search Value", required = true) @NotBlank  @RequestParam(value = "searchValue", required = true) String searchValue, 
			@ApiParam(value = "Include Archived Data", required = true) @RequestParam(value = "archData", required = true) boolean archData );
	
	
	@ApiOperation(value = "Returns Parts Supply related List (Teileanbietung)", notes = "Returns Parts Supply related List (Teileanbietung) using DB", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Returns a Parts Supply related List (Teileanbietung)", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Parts Supply related List (Teileanbietung) due to server Error")
	})
	@GetMapping( value = RestURIConstants.GENERATE_PARTS_SUPPLY_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> generatePartSupplyRelatedList(
			@ApiParam(value = "Number of records in each page", required = false)  @RequestParam(value = "pageSize", required = false) String pageSize, 
			@ApiParam(value = "Page Number to show", required = false)  @RequestParam(value = "pageNumber", required = false) String pageNumber);
	
	
	@ApiOperation(value = "Generate a report for Parts Supply related List (Teileanbietung - CSV)", notes = "Generate a report for Parts Supply related List (Teileanbietung - CSV) using DB", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Generate a report for Parts Supply related List (Teileanbietung - CSV )", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Generate a report for Parts Supply (Teileanbietung - CSV ) due to server Error")
	})
	@GetMapping( value = RestURIConstants.GENERATE_REPORT_PARTS_SUPPLY_LIST, produces = {"application/JSON"} )
	public <T> ResponseEntity<T> generateReportForPartSupply();
	
	
	@ApiOperation(value = "Returns Intra Trade Statistics List (Intrahandelsstatistik)", notes = "Returns Intra Trade Statistics List (Intrahandelsstatistik) using DB", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Returns a Intra Trade Statistics List (Intrahandelsstatistik)", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Intra Trade Statistics List (Intrahandelsstatistik) due to server Error")
	})
	@GetMapping( value = RestURIConstants.GENERATE_INTRA_TRADE_STATS_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> generateIntraTradeStatisticsList(
			@ApiParam(value = "From Date", required = true) @NotBlank @RequestParam(value = "fromDate", required = true) String fromDate,
			@ApiParam(value = "Till Date", required = true) @NotBlank @RequestParam(value = "toDate", required = true) String toDate,
			@ApiParam(value = "Customer Number", required = false)  @RequestParam(value = "customerNo", required = false) String customerNo,
			@ApiParam(value = "Customer Group", required = false)  @RequestParam(value = "customerGroup", required = false) String customerGroup,
			@ApiParam(value = "Number of records in each page", required = false)  @RequestParam(value = "pageSize", required = false) String pageSize, 
			@ApiParam(value = "Page Number to show", required = false)  @RequestParam(value = "pageNumber", required = false) String pageNumber);
	
	
	@ApiOperation(value = "Generate a report for Intra Trade Statistics (Intrahandelsstatistik - CSV)", notes = "Generate a report for Intra Trade Statistics (Intrahandelsstatistik - CSV) using DB", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Generate a report for Intra Trade Statistics (Intrahandelsstatistik - CSV)", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Generate report for Intra Trade Statistics (Intrahandelsstatistik - CSV) due to server Error")
	})
	@GetMapping( value = RestURIConstants.GENERATE_REPORT_INTRA_TRADE_STATS, produces = {"application/JSON"} )
	public <T> ResponseEntity<T> generateReportIntraTradeStatistics(
			@ApiParam(value = "From Date", required = true) @NotBlank @RequestParam(value = "fromDate", required = true) String fromDate,
			@ApiParam(value = "Till Date", required = true) @NotBlank @RequestParam(value = "toDate", required = true) String toDate,
			@ApiParam(value = "Customer Number", required = false)  @RequestParam(value = "customerNo", required = false) String customerNo,
			@ApiParam(value = "Customer Group", required = false)  @RequestParam(value = "customerGroup", required = false) String customerGroup);
	
	
	@ApiOperation(value = "Returns a list of selections based on Booking relevant - StockDiff List (Buchungsrelevante Listen)", notes = "Returns a list of selections based on Booking relevant - StockDiff List (Bestandsunterschiede Listen) using DB", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Returns a list of selections based on Booking relevant - StockDiff List (Bestandsunterschiede Listen)", response = ReportForBookingStockDifferencesDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Booking relevant - StockDiff List (Bestandsunterschiede Listen) due to server Error")
	})
	@GetMapping( value = RestURIConstants.GENERATE_BOOKING_RELEVANT_STOCK_DIFF_LIST, produces = {"application/JSON"} )
	public  ResponseEntity getBookingRelevantStockDiffList(
			@ApiParam(value = "List of Warehouse number", required = true) @NotEmpty @RequestParam(value = "warehouseNos", required = true) List<String> warehouseNos,
			@ApiParam(value = "Manufacturer name", required = true) @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@ApiParam(value = "From Date", required = true) @NotBlank @RequestParam(value = "fromDate", required = true) String fromDate,
			@ApiParam(value = "Till Date", required = true) @NotBlank @RequestParam(value = "toDate", required = true) String toDate);
	
	
	@ApiOperation(value = "Returns a list of selections based on Booking relevant - AccountChange List (Kontoänderungen Listen)", notes = "Returns a list of selections based on Booking relevant - AccountChange List (Kontoänderungen Listen) using DB", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Returns a list of selections based on Booking relevant - AccountChange List (Kontoänderungen Listen)", response = ReportForBookingAccountChangesDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Booking relevant - AccountChange List (Kontoänderungen Listen) due to server Error")
	})
	@GetMapping( value = RestURIConstants.GENERATE_BOOKING_RELEVANT_ACCOUNT_CHANGE_LIST, produces = {"application/JSON"} )
	public  ResponseEntity getBookingRelevantAccountChangeList(
			@ApiParam(value = "List of Warehouse number", required = true) @NotEmpty @RequestParam(value = "warehouseNos", required = true) List<String> warehouseNos,
			@ApiParam(value = "Manufacturer name", required = true) @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@ApiParam(value = "From Date", required = true) @NotBlank @RequestParam(value = "fromDate", required = true) String fromDate,
			@ApiParam(value = "Till Date", required = true) @NotBlank @RequestParam(value = "toDate", required = true) String toDate);
	
	
	@ApiOperation(value = "Generate a report for Booking relevant - StockDiff  (Buchungsrelevante Listen - CSV)", notes = "Generate a report for Booking relevant - StockDiff List (Buchungsrelevante Listen - CSV) using DB", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Generate a report for Booking relevant - StockDiff List (Buchungsrelevante Listen - CSV)", response = ReportForBookingStockDifferencesDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Generate a report on Booking relevant - StockDiff List (Buchungsrelevante Listen - CSV) due to server Error")
	})
	@GetMapping( value = RestURIConstants.GENERATE_REPORT_BOOKING_RELEVANT_STOCK_DIFF, produces = {"application/JSON"} )
	public <T> ResponseEntity<T> generateReportForBookingRelevant_StockDifferences(
			@ApiParam(value = "List of Warehouse number", required = true) @NotEmpty @RequestParam(value = "warehouseNos", required = true) List<String> warehouseNos,
			@ApiParam(value = "Manufacturer name", required = true) @NotBlank  @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@ApiParam(value = "From Date", required = true) @NotBlank @RequestParam(value = "fromDate", required = true) String fromDate,
			@ApiParam(value = "Till Date", required = true) @NotBlank @RequestParam(value = "toDate", required = true) String toDate);
	
	
	@ApiOperation(value = "Generate a report for Booking relevant - Account Change  (Kontoänderungen Listen - CSV)", notes = "Generate a report for Booking relevant - Account Change (Kontoänderungen Listen - CSV) using DB", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Generate a report for Booking relevant - Account Change (Kontoänderungen Listen - CSV)", response = ReportForBookingAccountChangesDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Generate a report on Booking relevant - Account Change (Kontoänderungen Listen - CSV) due to server Error")
	})
	@GetMapping( value = RestURIConstants.GENERATE_REPORT_BOOKING_RELEVANT_ACCOUNT_CHANGE, produces = {"application/JSON"} )
	public <T> ResponseEntity<T> generateReportForBookingRelevant_AccountChange(
			@ApiParam(value = "List of Warehouse number", required = true) @NotEmpty @RequestParam(value = "warehouseNos", required = true) List<String> warehouseNos,
			@ApiParam(value = "Manufacturer name", required = true) @NotBlank  @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@ApiParam(value = "From Date", required = true) @NotBlank @RequestParam(value = "fromDate", required = true) String fromDate,
			@ApiParam(value = "Till Date", required = true) @NotBlank @RequestParam(value = "toDate", required = true) String toDate);
	
	
	@ApiOperation(value = "Returns a list of parts Label for printing.", notes = "Returns a list of parts Label for printing.", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get parts Label List", response = PartPrintingLabelDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get parts Label list due to server Error")
	})
	@GetMapping( value = RestURIConstants.GENERATE_REPORT_LABEL_PRINTING, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getPartsLabelforPrinting(
			@ApiParam(value = "Number of records in each page", required = true) @NotBlank @RequestParam(value = "pageSize", required = true) String pageSize, 
			@ApiParam(value = "Page Number to show", required = true)  @NotBlank @RequestParam(value = "pageNumber", required = true) String pageNumber,
			@ApiParam(value = "Warehouse number", required = true) @NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@ApiParam(value = "Manufacturer name", required = true) @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer,
			@ApiParam(value = "Part Number Range Flag", required = true) @RequestParam(value = "partNumberRangeFlag", required = true) boolean partNumberRangeFlag, 
			@ApiParam(value = "Is all parts are selected", required = true) @RequestParam(value = "allParts", required = true) boolean allParts,
			@ApiParam(value = "Is all Storage Locations selected", required = true) @RequestParam(value = "allStorageLocations", required = true) boolean allStorageLocations,
			@ApiParam(value = "from Part Number", required = false)  @RequestParam(value = "fromPartNumber", required = false) String fromPartNumber,
			@ApiParam(value = "To Part Number", required = false)  @RequestParam(value = "toPartNumber", required = false) String toPartNumber,
			@ApiParam(value = "from Storage Location", required = false)  @RequestParam(value = "fromStorageLocation", required = false) String fromStorageLocation,
			@ApiParam(value = "to Storage Location", required = false)  @RequestParam(value = "toStorageLocation", required = false) String toStorageLocation);
	
	
	@ApiOperation(value = "Returns a list of parts Label for printing for CSV.", notes = "Returns a list of parts Label for printing for CSV.", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get parts Label List", response = PartPrintingLabelDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get parts Label list due to server Error")
	})
	@GetMapping( value = RestURIConstants.GENERATE_REPORT_LABEL_PRINTING_CSV, produces = {"application/JSON"} )
	public <T> ResponseEntity<T> getPartsLabelforPrintingCSV(
			@ApiParam(value = "Warehouse number", required = true) @NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@ApiParam(value = "Manufacturer name", required = true) @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer,
			@ApiParam(value = "Part Number Range Flag", required = true) @RequestParam(value = "partNumberRangeFlag", required = true) boolean partNumberRangeFlag, 
			@ApiParam(value = "Is all parts are selected", required = true) @RequestParam(value = "allParts", required = true) boolean allParts,
			@ApiParam(value = "Is all Storage Locations selected", required = true) @RequestParam(value = "allStorageLocations", required = true) boolean allStorageLocations,
			@ApiParam(value = "from Part Number", required = false)  @RequestParam(value = "fromPartNumber", required = false) String fromPartNumber,
			@ApiParam(value = "To Part Number", required = false)  @RequestParam(value = "toPartNumber", required = false) String toPartNumber,
			@ApiParam(value = "from Storage Location", required = false)  @RequestParam(value = "fromStorageLocation", required = false) String fromStorageLocation,
			@ApiParam(value = "to Storage Location", required = false)  @RequestParam(value = "toStorageLocation", required = false) String toStorageLocation);
	
	
}