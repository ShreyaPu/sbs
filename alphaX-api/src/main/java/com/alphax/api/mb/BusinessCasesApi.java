package com.alphax.api.mb;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.alphax.common.response.AlphaXResponse;
import com.alphax.util.RestURIConstants;
import com.alphax.vo.mb.AccessesBA_DTO;
import com.alphax.vo.mb.BusinessCases25DTO;
import com.alphax.vo.mb.BusinessCases49DTO;
import com.alphax.vo.mb.DeliveryNotes_BA_DTO;
import com.alphax.vo.mb.DeparturesBA_DTO;
import com.alphax.vo.mb.DropdownObject;
import com.alphax.vo.mb.FinalizationsBA_DTO;
import com.alphax.vo.mb.MasterDataBA_DTO;
import com.alphax.vo.mb.RebookingBundlesBA_DTO;

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
@Api(value = "ALPHAX",  tags = {"BusinessCases"} )
@SwaggerDefinition(tags = {
		@Tag(name = "BusinessCases", description = "REST controller that provides all the information about Business Cases.")
})
public interface BusinessCasesApi {

	@ApiOperation(value = "Update Finalizations BA (Finalisierungen)", notes = "Update Finalizations BA (Finalisierungen) based on input", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Update Finalizations BA (Finalisierungen) ", response = FinalizationsBA_DTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Update Finalizations BA (Finalisierungen) due to server Error")
	})
	@PutMapping( value = RestURIConstants.FINALIZATIONS_BA, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> updateFinalizationsBA(
			@ApiParam(value = "Object of Finalizations BA (Finalisierungen)", required = true) @Valid @RequestBody FinalizationsBA_DTO  finalizationsBA_obj );
	
	
	@ApiOperation(value = "Update Accesses BA (Zugänge BA)", notes = "Update Accesses BA (Zugänge BA) based on input", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Update Accesses BA (Zugänge BA)", response = AccessesBA_DTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Update Accesses BA (Zugänge BA) due to server Error")
	})
	@PutMapping( value = RestURIConstants.ACCESSES_BA, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> updateAccessesBA(
			@ApiParam(value = "Object of Accesses BA (Zugänge BA)", required = true) @Valid @RequestBody AccessesBA_DTO  accessesBA_obj );
	
	
	@ApiOperation(value = "Update Departures BA", notes = "Update Departures BA based on input", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Update Departures BA", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Update Departures BA due to server Error")
	})
	@PutMapping( value = RestURIConstants.DEPARTURES_BA, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> updateDeparturesBA(
			@ApiParam(value = "Object of Departures BA", required = true) @Valid @RequestBody DeparturesBA_DTO  departuresBA_Obj );
	
	
	@ApiOperation(value = "Update Master Data BA", notes = "Update Master Data BA based on input", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Update Master Data BA", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Update Master Data BA due to server Error")
	})
	@PutMapping( value = RestURIConstants.MASTER_DATA_BA, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> updateMasterDataBA(
			@ApiParam(value = "Object of Departures BA", required = true) @Valid @RequestBody MasterDataBA_DTO  masterDataBA_Obj );
	

	@ApiOperation(value = "Returns Finalizations BA (Finalisierungen) Details", notes = "Returns Finalizations BA (Finalisierungen) Details", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Finalizations BA (Finalisierungen) Details", response = FinalizationsBA_DTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Finalizations BA (Finalisierungen) details due to server Error")
	})
	@GetMapping( value = RestURIConstants.FINALIZATIONS_BA, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getFinalizationsBADetails(
			@ApiParam(value = "Manufacturer name", required = true)  @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@ApiParam(value = "Warehouse number", required = true) @NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@ApiParam(value = "Part number", required = true) @NotBlank @RequestParam(value = "partNumber", required = true) String partNumber,
			@ApiParam(value = "BusinessCase", required = true) @NotBlank @RequestParam(value = "businessCase", required = true) String businessCase);
	
	
	@ApiOperation(value = "Returns Accesses BA (Zugänge BA) Details", notes = "Returns Accesses BA (Zugänge BA) Details", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Accesses BA (Zugänge BA) Details", response = AccessesBA_DTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Accesses BA (Zugänge BA) Details due to server Error")
	})
	@GetMapping( value = RestURIConstants.ACCESSES_BA, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getAccessesBADetails(
			@ApiParam(value = "Manufacturer name", required = true)  @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@ApiParam(value = "Warehouse number", required = true) @NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@ApiParam(value = "Part number", required = true) @NotBlank @RequestParam(value = "partNumber", required = true) String partNumber,
			@ApiParam(value = "BusinessCase", required = true) @NotBlank @RequestParam(value = "businessCase", required = true) String businessCase);
	
	
	@ApiOperation(value = "Returns list of delivery notes for BA", notes = "Returns list of delivery notes for BA", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get delivery notes list", response = DeliveryNotes_BA_DTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get delivery notes list due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_DELIVERY_NOTES, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getDeliveryNoteList(
			@ApiParam(value = "Manufacturer name", required = true)  @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@ApiParam(value = "Warehouse number", required = true) @NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@ApiParam(value = "Supplier Number", required = true) @NotBlank @RequestParam(value = "supplierNumber", required = true) String supplierNumber,
			@ApiParam(value = "Delivery Note Number", required = true) @NotBlank @RequestParam(value = "searchString", required = true) String searchString,
			@ApiParam(value = "Number of records in each page", required = false)  @RequestParam(value = "pageSize", required = false) String pageSize, 
			@ApiParam(value = "Page Number to show", required = false)  @RequestParam(value = "pageNumber", required = false) String pageNumber );
	
	
	@ApiOperation(value = "Returns a list of Lager (Warehouse List)", notes = "Returns a list of Lager (Warehouse List)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Lager (Warehouse) List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Lager list due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_LAGER_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getLagerList();
	
	
	@ApiOperation(value = "Returns a list of  Supplier (Lieferant List)", notes = "Returns a list of  Supplier (Lieferant List)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Supplier (Lieferant) List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Supplier (Lieferant) list due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_LIEFERANT_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getSupplierList();
	

	@ApiOperation(value = "Returns Accesses BA 06 (Zugänge BA) Details", notes = "Returns Accesses BA 06 (Zugänge BA) Details", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Accesses BA 06 (Zugänge BA) Details", response = AccessesBA_DTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Accesses BA 06 (Zugänge BA) Details due to server Error")
	})
	@GetMapping( value = RestURIConstants.ACCESSES_BA_06, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getAccessesBA06Details(
			@ApiParam(value = "Manufacturer name", required = true)  @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@ApiParam(value = "Warehouse number", required = true) @NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@ApiParam(value = "Part number", required = true) @NotBlank @RequestParam(value = "partNumber", required = true) String partNumber,
			@ApiParam(value = "BusinessCase", required = true) @NotBlank @RequestParam(value = "businessCase", required = true) String businessCase,
			@ApiParam(value = "supplier Number", required = true) @NotBlank @RequestParam(value = "supplierNo", required = true) String supplierNo,
			@ApiParam(value = "Delivery Note Number", required = true) @NotBlank @RequestParam(value = "deliveryNoteNo", required = true) String deliveryNoteNo);
			
	
	@ApiOperation(value = "Returns Departures BA (Abgänge BA) Details", notes = "Returns Departures BA (Abgänge BA) Details", response = DeparturesBA_DTO.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Departures BA (Abgänge BA) Details", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Departures BA (Abgänge BA) Details due to server Error")
	})
	@GetMapping( value = RestURIConstants.DEPARTURES_BA, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getDeparturesBADetails(
			@ApiParam(value = "Manufacturer name", required = true)  @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@ApiParam(value = "Warehouse number", required = true) @NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@ApiParam(value = "Part number", required = true) @NotBlank @RequestParam(value = "partNumber", required = true) String partNumber,
			@ApiParam(value = "BusinessCase", required = true) @NotBlank @RequestParam(value = "businessCase", required = true) String businessCase);
	
	
	@ApiOperation(value = "Returns Master Data BA (Stammdaten BA) Details", notes = "Returns Master Data BA (Stammdaten BA) Details", response = DeparturesBA_DTO.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Master Data BA (Stammdaten BA) Details", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Master Data BA (Stammdaten BA) Details due to server Error")
	})
	@GetMapping( value = RestURIConstants.MASTER_DATA_BA, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getMasterDataBADetails(
			@ApiParam(value = "Manufacturer name", required = true)  @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@ApiParam(value = "Warehouse number", required = true) @NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@ApiParam(value = "Part number", required = true) @NotBlank @RequestParam(value = "partNumber", required = true) String partNumber,
			@ApiParam(value = "BusinessCase", required = true) @NotBlank @RequestParam(value = "businessCase", required = true) String businessCase);
	
	
	@ApiOperation(value = "Returns Departures BA 25 (Abgänge BA 25) Details", notes = "Returns Departures BA 25 (Abgänge BA 25) Details", response = DeparturesBA_DTO.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Departures BA 25 (Abgänge BA 25) Details", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Departures BA 25 (Abgänge BA 25) Details due to server Error")
	})
	@GetMapping( value = RestURIConstants.DEPARTURES_BA_25, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getDeparturesBA25Details(
			@ApiParam(value = "Manufacturer name", required = true)  @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@ApiParam(value = "Warehouse number", required = true) @NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@ApiParam(value = "Part number", required = true) @NotBlank @RequestParam(value = "partNumber", required = true) String partNumber,
			@ApiParam(value = "supplier Number", required = true) @NotBlank @RequestParam(value = "supplierNo", required = true) String supplierNo,
			@ApiParam(value = "Delivery Note Number", required = true) @NotBlank @RequestParam(value = "deliveryNoteNo", required = true) String deliveryNoteNo,
			@ApiParam(value = "Process Code (Vorgangscode)", required = true) @NotBlank @RequestParam(value = "processCode", required = true) String processCode,
			@ApiParam(value = "BusinessCase", required = true) @NotBlank @RequestParam(value = "businessCase", required = true) String businessCase);
	
	
	@ApiOperation(value = "Returns a list of Taxes (VAT Registration Number)", notes = "Returns a list of Taxes (VAT Registration Number)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Taxes List (VAT Registration Number) ", response = DropdownObject.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Taxes List (VAT Registration Number) due to server Error")
	})
	@GetMapping( value = RestURIConstants.ACCESSES_BA_TAXES, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getVATRegistrationNumber();

	
	@ApiOperation(value = "Returns a list of Abnehmergruppe (Customer Group)", notes = "This list is master data, which will be return from Stub, not required DB or COBOL call", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Abnehmergruppe List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Abnehmergruppe list due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_BA_CUSTOMERGROUP, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getCustomerGroupList(@ApiParam(value = "BusinessCase", required = true) @NotBlank @RequestParam(value = "businessCase", required = true) String businessCase);

	
	@ApiOperation(value = "Update Rebooking bundles in pieces BA (Umbuchen Gebinde in Stück )", notes = "Update Rebooking bundles in pieces BA (Umbuchen Gebinde in Stück ) based on input", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Update Rebooking bundles in pieces BA (Umbuchen Gebinde in Stück ) ", response = FinalizationsBA_DTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Update Rebooking bundles in pieces BA (Umbuchen Gebinde in Stück ) due to server Error")
	})
	@PutMapping( value = RestURIConstants.REBOOKIN_GBUNDLES_BA, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> updateRebookingBundlesBA(
			@ApiParam(value = "Object of Rebooking bundles in pieces BA (Umbuchen Gebinde in Stück ) ", required = true) @Valid @RequestBody RebookingBundlesBA_DTO  rebookingBundlesBA_DTO );
	
	
	@ApiOperation(value = "Returns Rebooking bundles in pieces BA (Umbuchen Gebinde in Stück BA)  Details", notes = "Returns Rebooking bundles in pieces BA (Umbuchen Gebinde in Stück BA)  Details", response = DeparturesBA_DTO.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Rebooking bundles in pieces BA (Umbuchen Gebinde in Stück BA) Details", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Rebooking bundles in pieces BA (Umbuchen Gebinde in Stück BA) Details due to server Error")
	})
	@GetMapping( value = RestURIConstants.REBOOKIN_GBUNDLES_BA, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getRebookingBundleDetails(
			@ApiParam(value = "Manufacturer name", required = true)  @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@ApiParam(value = "Warehouse number", required = true) @NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@ApiParam(value = "Part number", required = true) @NotBlank @RequestParam(value = "partNumber", required = true) String partNumber);
	
	
	@ApiOperation(value = "Returns a list of Supplier (Lieferant List)", notes = "Returns a list of Supplier (Lieferant List)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Supplier (Lieferant) List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Supplier (Lieferant) list due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_LIEFERANT_LIST_BY_NUMBER_OR_NAME, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getSupplierListUsingSearchString(
			@ApiParam(value = "Supplier Number / Name", required = true) @NotBlank @RequestParam(value = "searchString", required = true) String searchString );

	
	@ApiOperation(value = "Returns a list of active BAs", notes = "Returns a list of active BAs", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get BA List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get BA list due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_ACTIVE_BA, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getActiveBA();
	
	
	@ApiOperation(value = "Returns default printer for selected warehouse", notes = "Returns default printer for selected warehouse", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get defult printer", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get defult printer due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_DEFAULT_PRINTER, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getDefaultPrinter(
			@ApiParam(value = "Warehouse number", required = true) @NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@ApiParam(value = "flag 0 for sending and 1 for recieving warehouse", required = true) @NotNull @RequestParam(value = "flag", required = true) Integer flag);
	

	@ApiOperation(value = "Returns list of Lagers assigned to a company (Warehouse List)", notes = "Returns list of Lagers assigned to a company (Warehouse List)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get company Lager (Warehouse) List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Lager list due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_COMPANY_LAGER_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getComapnyBasedLagerList();
	
	@ApiOperation(value = "Update Delivery Note Status", notes = "Update Delivery Note Status based on input object", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Update Delivery Note Status ", response = DeliveryNotes_BA_DTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Update Delivery Note Status due to server Error")
	})
	@PutMapping( value = RestURIConstants.GET_DELIVERY_NOTES, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> updateDeliveryNoteStatus(
			@ApiParam(value = "Object of delivery notes", required = true) @Valid @RequestBody DeliveryNotes_BA_DTO  deliveryNotes_BA_DTO );
	
	@ApiOperation(value = "Call BA49", notes = "Call BA49 based on input object", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Call BA49 ", response = BusinessCases49DTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to call BA49 due to server Error")
	})
	@PostMapping( value = RestURIConstants.BA_49, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> newJavaImplementation_BA49(
			@ApiParam(value = "Object of BA 49", required = true) @Valid @RequestBody BusinessCases49DTO  cases49_obj );
	
	@ApiOperation(value = "Call BA25", notes = "Call BA25 based on input object", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Call BA25 ", response = BusinessCases25DTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to call BA25 due to server Error")
	})
	@PostMapping( value = RestURIConstants.DEPARTURES_BA_25, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> newJavaImplementation_BA25(
			@ApiParam(value = "Object of BA 25", required = true) @Valid @RequestBody BusinessCases25DTO  cases25_obj );
	
	
	@ApiOperation(value = "Call BA06 to enhance stocks", notes = "Call BA06 based on input object to enhance stocks", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Call BA06", response = BusinessCases25DTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to call BA06 due to server Error")
	})
	@PostMapping( value = RestURIConstants.ACCESSES_BA_06, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> newJavaImplementation_BA06(
			@ApiParam(value = "Object of BA 06", required = true) @Valid @RequestBody BusinessCases25DTO  cases25_obj );
	
	
	@ApiOperation(value = "Call BA 50 to delete the Parts.", notes = "Call BA 50 based on input object to delete the Parts", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Call BA50", response = FinalizationsBA_DTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to call BA50 due to server Error")
	})
	@PostMapping( value = RestURIConstants.FINALIZATIONS_BA_50, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> newJavaImplementation_BA50(
			@ApiParam(value = "Object of BA 06", required = true) @Valid @RequestBody FinalizationsBA_DTO  ba50_obj );
	
	@ApiOperation(value = "Call BA17", notes = "Call BA17 based on input object", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Call BA17 ", response = BusinessCases25DTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to call BA17 due to server Error")
	})
	@PostMapping( value = RestURIConstants.ACCESSES_BA_17, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> newJavaImplementation_BA17(
			@ApiParam(value = "Object of BA 17", required = true) @Valid @RequestBody AccessesBA_DTO  cases25_obj );
	
	
	@ApiOperation(value = "Cleanup the Parts details using BA 50.", notes = "Cleanup the Parts details using BA 50 implentation.", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "cleanup the parts", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to cleanup parts due to server Error")
	})
	@PutMapping( value = RestURIConstants.CLEANUP_PARTS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> partsCleanupUsingBA50(
			@ApiParam(value = "Warehouse number", required = true) @NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@ApiParam(value = "Cleanup older than provided date", required = true) @NotBlank @RequestParam(value = "toDate", required = true) String toDate);
	
	
	@ApiOperation(value = "Returns BA 17 Details", notes = "Returns BA 17 Details", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get BA 17 Details", response = AccessesBA_DTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get BA 17 Details due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_DETAILS_BA_17, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getDetailsFor_BA17(
			@ApiParam(value = "Manufacturer name", required = true)  @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@ApiParam(value = "Part number", required = true) @NotBlank @RequestParam(value = "partNumber", required = true) String partNumber);
	
	
	@ApiOperation(value = "Create part in multiple warehouse which is not available in ETSTAMM", notes = "Create part in multiple warehouse which is not available in ETSTAMM by Call BA17", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Create part in multiple warehouse", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Create part in multiple warehouse due to server Error")
	})
	@PostMapping( value = RestURIConstants.CREATE_PART_IN_MULTIPLE_WAREHOUSE, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> createPartInMultipleWarehouses(
			@ApiParam(value = "Manufacturer", required = true)  @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@ApiParam(value = "Part number", required = true) @NotBlank @RequestParam(value = "partNumber", required = true) String partNumber,
			@ApiParam(value = "Warehouse Numbers", required = true) @NotBlank @RequestParam(value = "warehouseNumbers", required = true) String warehouseNumbers,
			@ApiParam(value = "Vat Registration Number", required = true) @NotBlank @RequestParam(value = "vatRegistrationNumber", required = true) String vatRegistrationNumber);
			
	@ApiOperation(value = "Call BSN475", notes = "Call BSN475 (Part of OMCGETCL program)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Call BSN475", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Call BSN475 due to server Error")
	})
	@GetMapping( value = RestURIConstants.BA_BSN_475, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> newJavaImplementation_BSN475(
			@ApiParam(value = "Manufacturer name", required = true) @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer,
			@ApiParam(value = "Marketing Code", required = true) @NotBlank @RequestParam(value = "marketingCode", required = true) String marketingCode,
			@ApiParam(value = "Part number", required = false) @RequestParam(value = "partNumber", required = false) String partNumber,
			@ApiParam(value = "Discount Group (Rabattgruppe)", required = false) @RequestParam(value = "discountGroup", required = false) String discountGroup);
	
}