package com.alphax.api.mb;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.alphax.common.response.AlphaXResponse;
import com.alphax.util.RestURIConstants;
import com.alphax.vo.mb.AlphaXConfigurationKeysDetailsDTO;
import com.alphax.vo.mb.AlphaXConfigurationSetupDTO;
import com.alphax.vo.mb.InventoryOverviewDTO;

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
@Api(value = "ALPHAX",  tags = {"Dashboard"} )
@SwaggerDefinition(tags = {
		@Tag(name = "Dashboard", description = "REST controller that provides all the information for dashboard.")
})
public interface DashboardApi {
		
	@ApiOperation(value = "Return counts of Wareneingang", notes = "Return counts of Wareneingang", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Wareneingang counts", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Wareneingang counts due to server Error")
	})
	@GetMapping( value = RestURIConstants.DASHBOARD_WARENEINGANG_COUNTS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getGoodsReceipt();
	
	
	@ApiOperation(value = "Return counts of Daily Update - Warehouse Movement", notes = "Return counts of Daily Update - Warehouse Movement", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Daily Update counts", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Daily Update counts due to server Error")
	})
	@GetMapping( value = RestURIConstants.DASHBOARD_DAILYUPDATE_COUNTS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getDailyUpdateCount();
	
		
	@ApiOperation(value = "Admin menu to setup alphaX configuration", notes = "it offers all the alphaX configuration setup details ", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get alphaX configuration", response = AlphaXConfigurationSetupDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get alphaX configuration details due to server Error")
	})
	@GetMapping( value = RestURIConstants.ADMIN_CONFIGURATION_SETUP, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getAlphaXConfigurationDetails();
	
	
	@ApiOperation(value = "Admin menu to setup alphaX configuration", notes = "it offers all the alphaX configuration setup details ", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get alphaX configuration", response = AlphaXConfigurationSetupDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get alphaX configuration details due to server Error")
	})
	@PutMapping( value = RestURIConstants.ADMIN_CONFIGURATION_SETUP, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse>  updateAlphaXConfigurationDetails(
			@ApiParam(value = "alphaX configuration setup Object", required = true) @Valid @RequestBody List<AlphaXConfigurationSetupDTO> alphaXConfigurationSetupList);

	
	@ApiOperation(value = "Create O_Setup Table for alphaX configuration", notes = "Create O_Setup Table for alphaX configuration", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Creat O_Setup Table", response = AlphaXConfigurationSetupDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Creat O_Setup Table due to server Error")
	})
	@PutMapping( value = RestURIConstants.CREATE_O_SETUP_TABLE, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> create_OSetupTable();
	

	@ApiOperation(value = "Get automatic delivery note processing configuration details", notes = "Get automatic delivery note processing configuration details", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get automatic delivery note processing configuration", response = AlphaXConfigurationKeysDetailsDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get automatic delivery note processing configuration details due to server Error")
	})
	@GetMapping( value = RestURIConstants.AUTOMATIC_DELIVERYNOTE_PROCESSING, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getAutomaticDeliveryNoteProcessingDetails(
			@ApiParam(value = "the Warehouse Number", required = true) @NotBlank  @RequestParam(value = "warehouseNumber", required = true) String warehouseNumber);
	
	
	@ApiOperation(value = "Update automatic delivery note processing configuration details", notes = "Update automatic delivery note processing configuration details", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Update automatic delivery note processing configuration", response = AlphaXConfigurationKeysDetailsDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Update automatic delivery note processing configuration details due to server Error")
	})
	@PutMapping( value = RestURIConstants.AUTOMATIC_DELIVERYNOTE_PROCESSING, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> updateAutomaticDeliveryNoteProcessingDetails(
			@ApiParam(value = "Automatic delivery note processing configuration Object", required = true) @Valid @RequestBody List<AlphaXConfigurationKeysDetailsDTO> automaticProcessingObjList);
	
	
	@ApiOperation(value = "Returns count of inventory overview ", notes = "Returns count of inventory overview based on datalibrary", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get count of inventory overview ", response = InventoryOverviewDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get count of inventory overview due to server Error")
	})
	@GetMapping( value = RestURIConstants.DASHBOARD_INVENTORY_OVERVIEW_COUNT, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getInventoryOverviewCounts();
}
