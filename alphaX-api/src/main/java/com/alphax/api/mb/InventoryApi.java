package com.alphax.api.mb;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.alphax.common.response.AlphaXResponse;
import com.alphax.util.RestURIConstants;
import com.alphax.vo.mb.ActivatedCountingListDTO;
import com.alphax.vo.mb.AddCountedPartDTO;
import com.alphax.vo.mb.CountingGroupDTO;
import com.alphax.vo.mb.CreateNewCountingListDTO;
import com.alphax.vo.mb.CreatedCountingDetailListDTO;
import com.alphax.vo.mb.CreatedCountingListDTO;
import com.alphax.vo.mb.InventoryDTO;
import com.alphax.vo.mb.InventoryDiferentialListDTO;
import com.alphax.vo.mb.InventoryOverviewDTO;
import com.alphax.vo.mb.PartsBrandDTO;
import com.alphax.vo.mb.StorageLocationDTO;

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
@Api(value = "ALPHAX",  tags = {"Inventory"} )
@SwaggerDefinition(tags = {
		@Tag(name = "Inventory", description = "REST controller that provides all the information about Inventory.")
})
public interface InventoryApi {

	@ApiOperation(value = "Returns inventory overview counts ", notes = "Returns inventory overview counts based on datalibrary", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get inventory overview counts ", response = InventoryOverviewDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get inventory overview counts due to server Error")
	})
	@GetMapping( value = RestURIConstants.INVENTORY_OVERVIEW_COUNT, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getInventoryOverviewCounts(
			@ApiParam(value = "the Warehouse Number", required = true) @NotBlank  @RequestParam(value = "warehouseNumber", required = true) String warehouseNumber,
			@ApiParam(value = "the manufacturer", required = true) @NotBlank  @RequestParam(value = "manufacturer", required = true) String manufacturer);
	

	@ApiOperation(value = "Returns a display List for started inventory", notes = "This list is master data, which will be return from Stub, not required DB or COBOL call", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get display List for started inventory", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get display List for started inventory due to server Error")
	})
	@GetMapping( value = RestURIConstants.STARTED_INVENTORY_DISPLAY_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getDisplayListForStartedInventory();
	
	
	@ApiOperation(value = "Start inventory for an existing counting list", notes = "Start inventory for an existing counting list by COBOL", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Start inventory for an existing counting list", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Start inventory for an existing counting list due to server Error")
	})
	@GetMapping( value = RestURIConstants.START_INVENTORY, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> startInventoryForExistingCountingList( 
			@ApiParam(value = "Warehouse Number", required = true) @NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo);
	
	
	@ApiOperation(value = "Return warehouse list with status", notes = "Return warehouse list with status for inventory", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Return warehouse list with status", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to return warehouse list with status due to server Error")
	})
	@GetMapping( value = RestURIConstants.WAREHOUSE_LIST_FOR_INVENTORY, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getWarehouseListWithStatus( );
	
	
	@ApiOperation(value = "Submit the Inventory Count", notes = "Submit the Inventory Count by invoking COBOL", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Submit the Inventory Count", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Submit the Inventory Count due to server Error")
	})
	@GetMapping( value = RestURIConstants.SUBMIT_INVENTORY_COUNT, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> submitInventoryCount( 
			@ApiParam(value = "Warehouse Number", required = true) @NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@ApiParam(value = "the manufacturer", required = true) @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer,
			@ApiParam(value = "Part Number", required = true) @NotBlank  @RequestParam(value = "partNo", required = true) String partNo,
			@ApiParam(value = "Page Number", required = true) @NotBlank  @RequestParam(value = "pageNo", required = true) String pageNo,
			@ApiParam(value = "Count Value", required = true) @NotBlank  @RequestParam(value = "countValue", required = true) String countValue,
			@ApiParam(value = "Buffer", required = true) @RequestParam(value = "buffer", required = true) String buffer);
	
									
	@ApiOperation(value = "Close inventory for started counting list", notes = "Close inventory for started counting list by COBOL", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Close inventory for started counting list", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Close inventory for started counting list due to server Error")
	})
	@GetMapping( value = RestURIConstants.CLOSE_INVENTORY, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> closeInventory( 
			@ApiParam(value = "Warehouse Number", required = true) @NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo);
	
	
	@ApiOperation(value = "Returns all activated counting list with warehouse details", notes = "Returns all activated counting list with warehouse details", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get activated counting lists", response = ActivatedCountingListDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get all activated counting lists due to server Error")
	})
	@GetMapping( value = RestURIConstants.ACTIVATED_COUNT_INV_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getActivatedCountingList();
	
	
	@ApiOperation(value = "Returns selected activated counting list details", notes = "Returns selected activated counting list details", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get activated counting lists details", response = ActivatedCountingListDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get activated counting lists details due to server Error")
	})
	@GetMapping( value = RestURIConstants.ACTIVATED_COUNT_INV_LIST_DETAIL, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getActivatedCountingDetailList(
			@ApiParam(value = "Warehouse Number", required = true) @NotBlank @PathVariable(value = "warehousId", required = true) String warehouseNo,
			@ApiParam(value = "Number of records in each page", required = false)  @RequestParam(value = "pageSize", required = false) String pageSize, 
			@ApiParam(value = "Page Number to show", required = false)  @RequestParam(value = "pageNumber", required = false) String pageNumber,
			@ApiParam(value = "sorting by that is use for column name", required = false)  @RequestParam(value = "sortingBy", required = false) String sortingBy, 
			@ApiParam(value = "sorting type as ASC or DESC", required = false) @RequestParam(value = "sortingType", required = false) String sortingType,
			@ApiParam(value = "List of inventory Status Ids", required = false) @RequestParam(value = "invStatusIds", required = false) List<String> invStatusIds);
	
	
	@ApiOperation(value = "Returns created counting list details", notes = "Returns created counting list details", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get created counting lists", response = CreatedCountingListDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get all created counting lists due to server Error")
	})
	@GetMapping( value = RestURIConstants.CREATED_COUNT_INV_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getCreatedCountingList(
			@ApiParam(value = "Warehouse number", required = true) @NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@ApiParam(value = "List of inventory Type Ids", required = false) @RequestParam(value = "invTypeIds", required = false) List<String> invTypeIds);
	
	
	@ApiOperation(value = "Returns created counting detail list", notes = "Returns created counting detail list", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get created counting detail lists", response = CreatedCountingDetailListDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get all created counting detail lists due to server Error")
	})
	@GetMapping( value = RestURIConstants.CREATED_COUNT_INV_LIST_DETAILS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getCreatedCountingDetailList(
			@ApiParam(value = "Inventory List Id", required = true) @NotBlank @PathVariable(value = "inventoryListId", required = true) String inventoryListId,
			@ApiParam(value = "sorting by that is use for column name", required = true) @NotBlank @RequestParam(value = "sortingBy", required = true) String sortingBy,
			@ApiParam(value = "Number of records in each page", required = false)  @RequestParam(value = "pageSize", required = false) String pageSize, 
			@ApiParam(value = "Page Number to show", required = false)  @RequestParam(value = "pageNumber", required = false) String pageNumber);
	

	@ApiOperation(value = "Returns a list of available LOPA for provided manufacturer", notes = "Returns a list of available LOPA for provided manufacturer", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get LOPA List", response = StorageLocationDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get LOPA list due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_LOPA_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getStorageLocationList(
			@ApiParam(value = "Manufacturer Name(Hersteller)", required = true) @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer,
			@ApiParam(value = "Number of records in each page", required = false)  @RequestParam(value = "pageSize", required = false) String pageSize, 
			@ApiParam(value = "Page Number to show", required = false)  @RequestParam(value = "pageNumber", required = false) String pageNumber,
			@ApiParam(value = "Storage Location", required = true) @NotBlank @RequestParam(value = "searchString", required = true) String searchString);
	
	
	@ApiOperation(value = "Returns counting group for provided warehouse and manufacturer ", notes = "Returns counting group for provided warehouse and manufacturer", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get counting group List", response = CountingGroupDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get counting group list due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_COUNTING_GROUP_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getCountingGroupList(
			@ApiParam(value = "Warehouse number", required = true) @NotBlank @RequestParam(value = "warehouseId", required = true) String warehouseId,
			@ApiParam(value = "Manufacturer Name(Hersteller)", required = true) @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer);
	
	
	@ApiOperation(value = "Returns parts brand for provided manufacturer ", notes = "Returns parts brand for provided manufacturer", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get parts brand  List", response = PartsBrandDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get parts brand  list due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_PARTS_BRAND_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getPartsBrandList(
			@ApiParam(value = "Manufacturer Name(Hersteller)", required = true) @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer);
	
	
	@ApiOperation(value = "Returns a list of Sorting and print selection (Sortierung und Druckauswahl)", notes = "This list is master data, which will be return from Stub, not required DB or COBOL call", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Sortierung und Druckauswahl List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Sortierung und Druckauswahl list due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_SORTING_AND_PRINT_SELECTION_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getSortingAndPrintSelectionList();
	
	
	@ApiOperation(value = "Activate the inventory counting lists", notes = "Activate the inventory counting lists", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Activate the inventory counting lists", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Activate the inventory counting lists due to server Error")
	})
	@PutMapping( value = RestURIConstants.ACTIVATE_COUNTING_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> activateCountingList(
			@ApiParam(value = "Inventory List Id", required = true) @NotBlank @RequestParam(value = "inventoryListId", required = true) String inventoryListId);
	
	
	@ApiOperation(value = "Update the Inventory Status in DB", notes = "Update the Inventory Status in DB", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Update the Inventory Status in DB", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Update the Inventory Status due to server Error")
	})
	@PutMapping( value = RestURIConstants.INVENTORY_STATUS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> genericUpdateInventoryStatus(
			@ApiParam(value = "Inventory List Id", required = true) @NotBlank @RequestParam(value = "inventoryListId", required = true) String inventoryListId,
			@ApiParam(value = "Inventory Status Id", required = true) @NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "newStatusId", required = true) String newStatusId);
	
	
	@ApiOperation(value = "Returns a list of Ansicht (Opinion list)", notes = "This list is master data, which will be return from Stub, not required DB or COBOL call", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Ansicht List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Ansicht list due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_ANSICHT_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getAnsichtList();
	
	
	@ApiOperation(value = "Returns Differential list for inventory", notes = "Returns Differential list for inventory", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Differential lists", response = InventoryDiferentialListDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Differential lists due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_DIFFERENTIAL_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getDifferentialList(
			@ApiParam(value = "Inventory List Id", required = true) @NotBlank @PathVariable(value = "inventoryListId", required = true) String inventoryListId,
			@ApiParam(value = "selected opinion(Ansicht) value", required = true) @NotBlank @RequestParam(value = "opinionValue", required = true) String opinionValue, 
			@ApiParam(value = "Number of records in each page", required = false)  @RequestParam(value = "pageSize", required = false) String pageSize, 
			@ApiParam(value = "Page Number to show", required = false)  @RequestParam(value = "pageNumber", required = false) String pageNumber,
			@ApiParam(value = "sorting by column name", required = true) @NotBlank @RequestParam(value = "sortingBy", required = true) String sortingBy, 
			@ApiParam(value = "sorting type that is ASC or DESC", required = true) @NotBlank @RequestParam(value = "sortingType", required = true) String sortingType);
	

	@ApiOperation(value = "Create new Counted Part Manuelly", notes = "Create new Counted Part Manuelly in DB", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Create new counted part manuelly", response = AddCountedPartDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Create new counted part manuelly due to server Error")
	})
	@PostMapping( value = RestURIConstants.ADD_COUNTED_PART_MANUELLY, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> addCountedPartManuelly(
			@ApiParam(value = "Object of New counted part for inventory", required = true) @Valid @RequestBody AddCountedPartDTO countedPart_obj);
	
	
	@ApiOperation(value = "Returns created counting list value from active counting", notes = "Returns created counting list value from active counting", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get created counting list value", response = InventoryDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get all created counting list value due to server Error")
	})
	@GetMapping( value = RestURIConstants.CREATED_COUNTING_LIST_VALUE, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getCountingListValueFromActiveCounting(
			@ApiParam(value = "Inventory List Id", required = true) @NotBlank @PathVariable(value = "inventoryListId", required = true) String inventoryListId);
	
	
	@ApiOperation(value = "Returns a Printing pagebreak Dropdownlist", notes = "This list is master data, which will be return from Stub, not required DB or COBOL call", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Printing Pagebreak dropdown List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Pagebreak dropdown List due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_PRINTING_PAGEBREAK_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getPrintingPageBreakList();
	
	
	@ApiOperation(value = "Update counting list value from active counting in DB", notes = "Update counting list value from active counting in DB", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Update the counting list value in DB", response = CreatedCountingDetailListDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Update the counting list value due to server Error")
	})
	@PutMapping( value = RestURIConstants.UPDATE_COUNTING_LIST_VALUE, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> updateCountingListValueFromActiveCounting(
			@ApiParam(value = "Object of counting list value", required = true) @Valid @RequestBody List<CreatedCountingDetailListDTO> createdCountingDetailListDTOs);
	
	
	@ApiOperation(value = "Returns counting list items", notes = "Returns counting list items", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get counting list items", response = CreatedCountingDetailListDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get counting list items due to server Error")
	})
	@GetMapping( value = RestURIConstants.CREATED_COUNTING_LIST_ITEMS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getCountingListItemsFromActiveCounting(
			@ApiParam(value = "Inventory List Id", required = true) @NotBlank @RequestParam(value = "inventoryListId", required = true) String inventoryListId,
			@ApiParam(value = "Number of records in each page", required = false)  @RequestParam(value = "pageSize", required = false) String pageSize, 
			@ApiParam(value = "Page Number to show", required = false)  @RequestParam(value = "pageNumber", required = false) String pageNumber,
			@ApiParam(value = "page Nummer for started inventory", required = false) @Pattern(regexp = "[0-9]+") @RequestParam(value = "searchCount", required = false) String searchCount,
			@ApiParam(value = "display type", required = false) @RequestParam(value = "displayType", required = false) String displayType);
	
	
	@ApiOperation(value = "Create new Counting list", notes = "Create new Counting list in DB", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Create new Counting list", response = CreateNewCountingListDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Create new Counting list due to server Error")
	})
	@PostMapping( value = RestURIConstants.CREATE_NEW_COUNTING_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> createNewCountingList(
			@ApiParam(value = "Object of New counting list", required = true) @Valid @RequestBody CreateNewCountingListDTO countingList_obj,
			@ApiParam(value = "Selection type", required = false) @RequestParam(value = "selectionType", required = false) String selectionType);
	
	
	@ApiOperation(value = "Add parts in existing Counting list", notes = "Add parts in existing Counting list in DB", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Add parts in existing Counting list", response = CreateNewCountingListDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Add parts in existing Counting list due to server Error")
	})
	@PostMapping( value = RestURIConstants.ADD_PARTS_IN_EXISTING_COUNTING_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> addPartInExistingCountingList(
			@ApiParam(value = "Object of New counting list", required = true) @Valid @RequestBody CreateNewCountingListDTO countingList_obj,
			@ApiParam(value = "Inventory List Id", required = true) @NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "invListId", required = true) String invListId,
			@ApiParam(value = "Selection type", required = false) @RequestParam(value = "selectionType", required = false) String selectionType);
	
	
	@ApiOperation(value = "Save / Add Differential list for inventory", notes = "Save / Add Differential list for inventory", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Save differential lists", response = InventoryDiferentialListDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to save Differential lists due to server Error")
	})
	@PutMapping( value = RestURIConstants.ADD_DIFFERENTIAL_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> saveDifferentialList(
			@ApiParam(value = "Object of Inventory DiferentialList DTO list", required = true) @Valid @RequestBody List<InventoryDiferentialListDTO> differntialDTOList,
			@ApiParam(value = "Inventory List Id", required = true) @NotBlank @RequestParam(value = "inventoryListId", required = true) String inventoryListId,
			@ApiParam(value = "select all records flag", required = true) @RequestParam(value = "selectAllFlag", required = true) boolean selectAllFlag,
			@ApiParam(value = "selected opinion(Ansicht) value", required = true) @NotBlank @RequestParam(value = "opinionValue", required = true) String opinionValue);
	
	
	@ApiOperation(value = "Process / mark the counting list as fully counted", notes = "Process / mark the counting list as fully counted", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "process the counting list as fully counted", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to process the counting list as fully counted due to server Error")
	})
	@PutMapping( value = RestURIConstants.PROCESS_COUNTINGLIST_AS_FULLY_COUNTED, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> processCountingListAsFullyCounted(
			@ApiParam(value = "Inventory List Id", required = true) @NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "inventoryListId", required = true) String inventoryListId);
	
	
	@ApiOperation(value = "Delete parts from counting list", notes = "Delete parts from counting list based on OEM and partNo", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Delete parts from counting list", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Delete parts from counting list due to server Error")
	})
	@DeleteMapping( value = RestURIConstants.DELETE_PART_FROM_COUNTING_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> deletePartsFromCountingList(
			@ApiParam(value = "Inventory part Id", required = true) @NotBlank @Pattern(regexp = "[0-9]+") @PathVariable(value = "invPartId", required = true) String invPartId,
			@ApiParam(value = "Manufacturer Name(Hersteller)", required = true) @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer,
			@ApiParam(value = "part Number", required = true)  @NotBlank @RequestParam(value = "partNumber", required = true) String partNumber, 
			@ApiParam(value = "Number Of Place", required = true) @NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "numberOfPlace", required = true) String numberOfPlace);
	
	
	@ApiOperation(value = "Returns all Closed/Completed counting list with warehouse details", notes = "Returns all Closed/Completed counting list with warehouse details", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get closed counting lists", response = ActivatedCountingListDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get all Closed/Completed counting lists due to server Error")
	})
	@GetMapping( value = RestURIConstants.CLOSED_COUNT_INV_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getClosedCountingList();
	
	
	@ApiOperation(value = "Returns selected Closed/Completed counting list details", notes = "Returns selected Closed/Completed counting list details", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Closed counting lists details", response = ActivatedCountingListDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Closed/Completed counting lists details due to server Error")
	})
	@GetMapping( value = RestURIConstants.CLOSED_COUNT_INV_LIST_DETAIL, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getClosedCountingDetailList(
			@ApiParam(value = "Warehouse Number", required = true) @NotBlank @PathVariable(value = "warehousId", required = true) String warehouseNo,
			@ApiParam(value = "Number of records in each page", required = false)  @RequestParam(value = "pageSize", required = false) String pageSize, 
			@ApiParam(value = "Page Number to show", required = false)  @RequestParam(value = "pageNumber", required = false) String pageNumber);
	
	
	@ApiOperation(value = "Mark the differential list As fully processed", notes = "Mark the differential list As fully processed", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Mark the differential list As fully processed", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to process the differential list As fully processed due to server Error")
	})
	@PutMapping( value = RestURIConstants.MARK_DIFFERENTIAL_LIST_AS_FULLY_PROCESSED, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> markDifferentialListAsFullyProcessed(
			@ApiParam(value = "Inventory List Id", required = true) @NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "inventoryListId", required = true) String inventoryListId,
			@ApiParam(value = "the Warehouse Number", required = true) @NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "warehouseNo", required = true) String warehouseNo);
	
	
	@ApiOperation(value = "Copy INVLIST/INVPART into alphaplus history table", notes = "Copy INVLIST/INVPART into alphaplus history table", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Copy INVLIST/INVPART into alphaplus history table", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to process Copy INVLIST/INVPART into alphaplus history table due to server Error")
	})
	@PutMapping( value = RestURIConstants.COPY_INVENTORY_DTLS_INTO_ALPHAPLUS_HISTORY, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> copyInventoryDtlsIntoAlphaplusHistory(
			@ApiParam(value = "Inventory List Id", required = true) @NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "inventoryListId", required = true) String inventoryListId,
			@ApiParam(value = "the Warehouse Number", required = true) @NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "warehouseNo", required = true) String warehouseNo);
	
	
	@ApiOperation(value = "Update the Inventory Status in DB", notes = "Update the Inventory Status in DB", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Update the Inventory Status in DB", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Update the Inventory Status due to server Error")
	})
	@PutMapping( value = RestURIConstants.INVENTORY_STATUS_RECORDING_DATA, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> updateInventoryStatus(
			@ApiParam(value = "Inventory List Id", required = true) @NotBlank  @Pattern(regexp = "[0-9]+") @RequestParam(value = "inventoryListId", required = true) String inventoryListId);
	
	
	@ApiOperation(value = "Returns Sum of Delta Stuck and Delta DAK values for Differential list", notes = "Returns Sum of Delta Stuck and Delta DAK values for Differential list", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Sum of Delta Stuck and Delta DAK values", response = InventoryDiferentialListDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Sum due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_SUM_DELTA_DIFFERENTIAL, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getSumOfDifferntialDelta(
			@ApiParam(value = "Inventory List Id", required = true) @NotBlank @Pattern(regexp = "[0-9]+") @PathVariable(value = "inventoryListId", required = true) String inventoryListId);
	
	
	@ApiOperation(value = "Add single part in existing Counting list", notes = "Add single part in existing Counting list in DB", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Add single part in existing Counting list", response = CreateNewCountingListDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Add single part in existing Counting list due to server Error")
	})
	@PostMapping( value = RestURIConstants.ADD_SINGLE_PART_IN_EXISTING_COUNTING_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> addSinglePartInExistingCountingList(
			@ApiParam(value = "Object of New counting list", required = true) @Valid @RequestBody CreateNewCountingListDTO countingList_obj,
			@ApiParam(value = "Inventory List Id", required = true) @NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "invListId", required = true) String invListId);
	
	
	@ApiOperation(value = "Return flag of parts negative stock amount", notes = "Return flag of parts negative stock amount based on datalibrary", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get parts negative stock amount flag ", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get parts negative stock amount flag due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_PART_NEGATION_STOCK_AMOUNT, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> checkPartsWithNegativeStock(
			@ApiParam(value = "the Warehouse Number", required = true) @NotBlank  @RequestParam(value = "warehouseNumber", required = true) String warehouseNumber);
	
	
	@ApiOperation(value = "Return list of inventory status", notes = "Return list of inventory status from DB", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Return list of inventory status", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to return list of inventory status due to server Error")
	})
	@GetMapping( value = RestURIConstants.INVENTORY_STATUS_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getInventoryListStatus( );
	
	@ApiOperation(value = "Return counting list name", notes = "Return counting list name from DB", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Return counting list name", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to return counting list name due to server Error")
	})
	@GetMapping( value = RestURIConstants.GENERATED_COUNTING_LIST_NAME, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> generateCountingListName( );
	
	@ApiOperation(value = "Return flag to check partly counted storage locations for a part", notes = "Return flag to check partly counted storage locations for a part from DB", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Return flag to check partly counted storage locations for a part", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to return flag to check partly counted storage locations for a part due to server Error")
	})
	@GetMapping( value = RestURIConstants.PARTLY_COUNTED_STORAGE_LOCATION, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> checkPartlyCountedStorageLocationOfPart( 
			@ApiParam(value = "Inventory List Id", required = true) @NotBlank @PathVariable(value = "inventoryListId", required = true) String inventoryListId);
	
	@ApiOperation(value = "Return inventory list Type", notes = "Return inventory list Type from DB", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Return inventory list Type", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to return inventory list Type due to server Error")
	})
	@GetMapping( value = RestURIConstants.INVENTORY_LIST_TYPE, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getInventoryListType( );
	
	@ApiOperation(value = "Returns all warehouse details for created counting list ", notes = "Returns all warehouse details for created counting list", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get all warehouse details for created counting list", response = CreatedCountingListDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get all warehouse details for created counting list due to server Error")
	})
	@GetMapping( value = RestURIConstants.WAREHOUSES_FRO_CREATED_COUNT_INV_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getWarehouseListForCreatedCountingList();
	
}