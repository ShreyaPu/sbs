package com.alphax.api.mb;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.alphax.common.response.AlphaXResponse;
import com.alphax.util.RestURIConstants;

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
@Api(value = "ALPHAX",  tags = {"InventoryReport"} )
@SwaggerDefinition(tags = {
		@Tag(name = "InventoryReport", description = "REST controller that provides all the information about Inventory Report.")
})
public interface InventoryReportApi {


	@ApiOperation(value = "Download PDF file for inventory Parts lists", notes = "Download PDF file for inventory Parts lists", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Downloaded PDF file", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Download PDF file due to server Error")
	})
	@GetMapping( value = RestURIConstants.DOWNLOAD_PDF_INVENTORY_PARTS, produces = MediaType.APPLICATION_PDF_VALUE )
	public <T> ResponseEntity<T> downloadPDFInvPartsList(
			@ApiParam(value = "Inventory List Id", required = true) @NotBlank @PathVariable(value = "inventoryListId", required = true) String inventoryListId,
			@ApiParam(value = "Page Break checkbox value", required = true) @RequestParam(value = "pageBreakFlag", required = true) boolean pageBreakFlag,
			@ApiParam(value = "Page Break selected DDL value", required = false) @RequestParam(value = "pageBreakDDLValue", required = false) String pageBreakDDLValue);
	
	
	@ApiOperation(value = "Download PDF file for Closed Inventory Parts lists", notes = "Download PDF file for Closed Inventory Parts lists", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Downloaded PDF file", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Download PDF file due to server Error")
	})
	@GetMapping( value = RestURIConstants.DOWNLOAD_PDF_CLOSED_INVENTORY_PARTS, produces = MediaType.APPLICATION_PDF_VALUE )
	public <T> ResponseEntity<T> downloadPDFClosedInvPartsList(
			@ApiParam(value = "Inventory List Id", required = true) @NotBlank @PathVariable(value = "inventoryListId", required = true) String inventoryListId);
	
	
	@ApiOperation(value = "Download PDF file for inventory Parts lists having Status 3", notes = "Download PDF file for inventory Parts lists having Status 3", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Downloaded PDF file", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Download PDF file due to server Error")
	})
	@GetMapping( value = RestURIConstants.DOWNLOAD_PDF_STS3_INVENTORY_PARTS, produces = MediaType.APPLICATION_PDF_VALUE )
	public <T> ResponseEntity<T> downloadPDF_ST3_InvPartsList(
			@ApiParam(value = "Inventory List Id", required = true) @NotBlank @Pattern(regexp = "[0-9]+") @PathVariable(value = "inventoryListId", required = true) String inventoryListId,
			@ApiParam(value = "Page Break checkbox value", required = true) @RequestParam(value = "pageBreakFlag", required = true) boolean pageBreakFlag,
			@ApiParam(value = "Page Break selected DDL value", required = false) @RequestParam(value = "pageBreakDDLValue", required = false) String pageBreakDDLValue);
	
	
	@ApiOperation(value = "Download PDF file to show differences of inventory parts for intermediate results", notes = "Download PDF file to show differences of inventory parts for intermediate results", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Downloaded PDF file", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Download PDF file due to server Error")
	})
	@GetMapping( value = RestURIConstants.DOWNLOAD_PDF_INTERMEDIATE_DIFF, produces = MediaType.APPLICATION_PDF_VALUE )
	public <T> ResponseEntity<T> downloadPDF_IntermediateDiff(
			@ApiParam(value = "Inventory List Id", required = true) @NotBlank @Pattern(regexp = "[0-9]+") @PathVariable(value = "inventoryListId", required = true) String inventoryListId);
	
	
	@ApiOperation(value = "Download PDF file for Countless and Rejected positions of Parts list", notes = "Download PDF file for Countless and Rejected positions of Parts list", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Downloaded PDF file", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Download PDF file due to server Error")
	})
	@GetMapping( value = RestURIConstants.DOWNLOAD_PDF_COUNTLESS_REJECTED_LIST, produces = MediaType.APPLICATION_PDF_VALUE )
	public <T> ResponseEntity<T> downloadPDF_Countless_Rejected_List(
			@ApiParam(value = "Inventory List Id", required = true) @NotBlank @Pattern(regexp = "[0-9]+") @PathVariable(value = "inventoryListId", required = true) String inventoryListId);
	
	
	@ApiOperation(value = "Download PDF file for Counting list with quantities of Parts list", notes = "Download PDF file for Counting list with quantities of Parts list", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Downloaded PDF file", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Download PDF file due to server Error")
	})
	@GetMapping( value = RestURIConstants.DOWNLOAD_PDF_COUNTING_LIST_QUANTITY, produces = MediaType.APPLICATION_PDF_VALUE )
	public <T> ResponseEntity<T> downloadPDF_CountingListQuantity(
			@ApiParam(value = "Inventory List Id", required = true) @NotBlank @Pattern(regexp = "[0-9]+") @PathVariable(value = "inventoryListId", required = true) String inventoryListId);
	
	
	@ApiOperation(value = "Download PDF file for Postions without difference Parts list", notes = "Download PDF file for Postions without difference Parts list", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Downloaded PDF file", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Download PDF file due to server Error")
	})
	@GetMapping( value = RestURIConstants.DOWNLOAD_PDF_POSITIONS_WITHOUT_DIFF, produces = MediaType.APPLICATION_PDF_VALUE )
	public <T> ResponseEntity<T> downloadPDF_Without_Differnce(
			@ApiParam(value = "Inventory List Id", required = true) @NotBlank @Pattern(regexp = "[0-9]+") @PathVariable(value = "inventoryListId", required = true) String inventoryListId);
	
	
	@ApiOperation(value = "Download PDF file for Accepted different positions list", notes = "Download PDF file for Accepted different positions list", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Downloaded PDF file", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Download PDF file due to server Error")
	})
	@GetMapping( value = RestURIConstants.DOWNLOAD_PDF_ACCEPTED_DIFFERENT_POS, produces = MediaType.APPLICATION_PDF_VALUE )
	public <T> ResponseEntity<T> downloadPDF_Accepted_Diff_Pos(
			@ApiParam(value = "Inventory List Id", required = true) @NotBlank @Pattern(regexp = "[0-9]+") @PathVariable(value = "inventoryListId", required = true) String inventoryListId);
	

	@ApiOperation(value = "Download PDF file for Difference sum of accepted positions list", notes = "Download PDF file for Difference sum of accepted positions list", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Downloaded PDF file", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Download PDF file due to server Error")
	})
	@GetMapping( value = RestURIConstants.DOWNLOAD_PDF_DIFFERENCE_SUM_POS, produces = MediaType.APPLICATION_PDF_VALUE )
	public <T> ResponseEntity<T> downloadPDF_DifferenceSum_Pos(
			@ApiParam(value = "Inventory List Id", required = true) @NotBlank @Pattern(regexp = "[0-9]+") @PathVariable(value = "inventoryListId", required = true) String inventoryListId,
			@ApiParam(value = "the Warehouse Number", required = true) @NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "warehouseNumber", required = true) String warehouseNumber);
	
	
}