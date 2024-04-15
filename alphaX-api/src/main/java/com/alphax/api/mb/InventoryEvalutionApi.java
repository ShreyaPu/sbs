package com.alphax.api.mb;

import javax.validation.constraints.NotBlank;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alphax.common.response.AlphaXResponse;
import com.alphax.util.RestURIConstants;
import com.alphax.vo.mb.InventoryArchivedCountingDTO;
import com.alphax.vo.mb.InventoryArchivedDifferencesDTO;
import com.alphax.vo.mb.InventoryEvalutionDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

/**
 * @author A106744104
 *
 */
@Api(value = "ALPHAX",  tags = {"InventoryEvalution"} )
@SwaggerDefinition(tags = {
		@Tag(name = "InventoryEvalution", description = "REST controller that provides all the information about Inventory Evalution Api.")
})
public interface InventoryEvalutionApi {

	@ApiOperation(value = "Returns the list of Inventory Conspicuous Items (Bestandsauffällige Positionen) ", notes = "Returns the list of Inventory Conspicuous Items (Bestandsauffällige Positionen)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get all created inventory lists", response = InventoryEvalutionDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get all created inventory lists due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_INVENTORY_REMARKABLE_ITEMS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getInventoryRemarkableItems(
			@ApiParam(value = "Warehouse Number", required = true) @NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@ApiParam(value = "From Date", required = true) @NotBlank @RequestParam(value = "fromDate", required = true) String fromDate,
			@ApiParam(value = "Till Date", required = true) @NotBlank @RequestParam(value = "toDate", required = true) String toDate,
			@ApiParam(value = "Number of records in each page", required = false)  @RequestParam(value = "pageSize", required = false) String pageSize, 
			@ApiParam(value = "Page Number to show", required = false)  @RequestParam(value = "pageNumber", required = false) String pageNumber);
	
	
	@ApiOperation(value = "Returns the list of Inventory Conspicuous Items Details (Bestandsauffällige Positionen Details) ", notes = "Returns the list of Inventory Conspicuous Items Details (Bestandsauffällige Positionen Details)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get all created inventory details lists", response = InventoryEvalutionDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get all created inventory detail lists due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_INVENTORY_REMARKABLE_ITEMS_DETAILS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getInventoryRemarkableItemsDetails(
			@ApiParam(value = "Part Number", required = true)  @NotBlank @RequestParam(value = "partNo", required = true) String partNo,
			@ApiParam(value = "Warehouse Number", required = true) @NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo);
	
	
	@ApiOperation(value = "Returns the Archived lists of differences for inventory (Archivierte Differenzenlisten) ", notes = "Returns the Archived lists of differences for inventory (Archivierte Differenzenlisten)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get all Archived lists of differences for inventory", response = InventoryArchivedDifferencesDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Archived lists of differences for inventory due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_INVENTORY_ACCHIVED_DIFFERENCES, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getInventoryArchivedDifferencesList(
			@ApiParam(value = "Warehouse Number", required = true) @NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@ApiParam(value = "From Date", required = true) @NotBlank @RequestParam(value = "fromDate", required = true) String fromDate,
			@ApiParam(value = "Till Date", required = true) @NotBlank @RequestParam(value = "toDate", required = true) String toDate);
	
	
	@ApiOperation(value = "Returns the Archived lists of differences for inventory in details (Archivierte Differenzenlisten) ", notes = "Returns the Archived lists of differences for inventory in details (Archivierte Differenzenlisten)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get all Archived lists of differences for inventory in details", response = InventoryArchivedDifferencesDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Archived lists of differences for inventory in details due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_INVENTORY_ACCHIVED_DIFFERENCES_IN_DETAILS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getInventoryArchivedDifferencesInDetails(
			@ApiParam(value = "Warehouse Number", required = true) @NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@ApiParam(value = "Date", required = true) @NotBlank @RequestParam(value = "date", required = true) String date,
			@ApiParam(value = "List Id", required = true) @NotBlank @RequestParam(value = "listId", required = true) String listId,
			@ApiParam(value = "Details Type", required = true) @NotBlank @RequestParam(value = "detailsType", required = true) String detailsType);
	
	
	@ApiOperation(value = "Returns the Archived Counting List (Archivierte Zähllisten) ", notes = "Returns the Returns the Archived Counting List (Archivierte Zähllisten) from savLib", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Archived Counting List", response = InventoryArchivedCountingDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Archived Counting List due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_INVENTORY_ACCHIVED_COUNTING, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getArchivedCountingList(
			@ApiParam(value = "Warehouse Number", required = true) @NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@ApiParam(value = "From Date", required = true) @NotBlank @RequestParam(value = "fromDate", required = true) String fromDate,
			@ApiParam(value = "Till Date", required = true) @NotBlank @RequestParam(value = "toDate", required = true) String toDate);
	
	
	@ApiOperation(value = "Returns the Archived Counting detail List (Archivierte Zähllisten) ", notes = "Returns the Returns the Archived Counting detail List (Archivierte Zähllisten) from savLib", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Archived Counting detail List", response = InventoryArchivedCountingDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Archived Counting detail List due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_INVENTORY_ACCHIVED_COUNTING_DETAILS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getArchivedCountingDetailList(
			@ApiParam(value = "Warehouse Number", required = true) @NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@ApiParam(value = "Inventory Date", required = true) @NotBlank @RequestParam(value = "inventoryDate", required = true) String inventoryDate,
			@ApiParam(value = "Inventory Time", required = true) @NotBlank @RequestParam(value = "inventoryTime", required = true) String inventoryTime,
			@ApiParam(value = "Details Type", required = true) @NotBlank @RequestParam(value = "detailsType", required = true) String detailsType);
	
	
}