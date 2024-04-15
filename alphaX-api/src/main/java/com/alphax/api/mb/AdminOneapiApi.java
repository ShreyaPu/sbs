package com.alphax.api.mb;

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
import com.alphax.vo.mb.AdminOneapiDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

@Api(value = "ALPHAX",  tags = {"OneApi"} )
@SwaggerDefinition(tags = {
		@Tag(name = "OneApi", description = "REST controller that provides all the information about OneApi.")
})

public interface AdminOneapiApi {
	
	@ApiOperation(value = "Returns the data of Customer", notes = "Returns data of particular customer from O_ONEAPI table.", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get customer details from OneApi", response = AdminOneapiDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get customer details from OneApi due to server Error")
	})
	@GetMapping( value = RestURIConstants.ONEAPI_CUSTOMER_DETAILS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getOneapiCustomerDetails(
			@ApiParam(value = "Customer Id", required = true) @NotBlank @Pattern(regexp = "[0-9]+")  @PathVariable(value = "customerId", required = true) String customerId,
			@ApiParam(value = "API Key", required = true) @NotBlank @RequestParam(value = "apiKey", required = true) String apiKey);
	
	
	@ApiOperation(value = "Returns the data of OneAPI List", notes = "Returns list from O_ONEAPI table.", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get customer details from OneApi", response = AdminOneapiDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get customer details from OneApi due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_ONEAPI_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getOneapiList(
			@ApiParam(value = "Number of records in each page", required = false)  @RequestParam(value = "pageSize", required = false) String pageSize, 
			@ApiParam(value = "Page Number to show", required = false)  @RequestParam(value = "pageNumber", required = false) String pageNumber,
			@ApiParam(value = "EVOBUS API Flag", required = true) @RequestParam(value = "isEVOBus", required = true) Boolean isEVOBus);
	
	
	@ApiOperation(value = "Create New Oneapi details", notes = "Create New Oneapi details in DB", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Create New Oneapi for admin setting", response = AdminOneapiDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Create New Oneapi for admin setting due to server Error")
	})
	@PostMapping( value = RestURIConstants.CREATE_ONEAPI, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> createNewOneapi(
			@ApiParam(value = "Object of New Oneapi details for admin setting", required = true) @Valid @RequestBody AdminOneapiDTO oneapiDTO_obj);

	
	@ApiOperation(value = "Returns a list of VF-Number(Customer Number)", notes = "Returns a list of VF-Number(Customer Number) based on schema", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get VF-Number(Customer Number)", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to VF-Number(Customer Number) due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_VF_NUMBER, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getVFNumber(
			@ApiParam(value = "EVOBUS API Flag", required = true) @RequestParam(value = "isEVOBus", required = true) Boolean isEVOBus);
	
	
	@ApiOperation(value = "delete Oneapi customer details", notes = "delete Oneapi customer details based on schema for admin setting", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Delete Oneapi customer details for admin setting", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to delete Oneapi customer details for admin setting due to server Error")
	})
	@DeleteMapping( value = RestURIConstants.ONEAPI_CUSTOMER_DETAILS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> deleteOneapiCustomerDetails(
			@ApiParam(value = "VF-Number (Customer Number)", required = true) @NotBlank @Pattern(regexp = "[0-9]+") @PathVariable(value = "customerId", required = true) String customerId );
	

	@ApiOperation(value = "delete Oneapi Configuration details", notes = "delete Oneapi Configuration details based on schema for admin setting", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Delete Oneapi Configuration details for admin setting", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to delete Oneapi Configuration details for admin setting due to server Error")
	})
	@DeleteMapping( value = RestURIConstants.DELETE_ONEAPI_CONFIGURATION_DETAILS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> deleteOneapiConfigurationDetails(
			@ApiParam(value = "VF-Number (Customer Number)", required = true) @NotBlank @Pattern(regexp = "[0-9]+") @PathVariable(value = "customerId", required = true) String customerId,
			@ApiParam(value = "API Key", required = true) @NotBlank @RequestParam(value = "apiKey", required = true) String apiKey);
	
	
	@ApiOperation(value = "get active apiURL list", notes = "get active apiURL list", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "get active apiURL list", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get active apiURL list")
	})
	@GetMapping( value = RestURIConstants.GET_APIURL, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getApiurl(
			@ApiParam(value = "EVOBUS API Flag", required = true) @RequestParam(value = "isEVOBus", required = true) Boolean isEVOBus);
	
	
	@ApiOperation(value = "Update OneAPI details", notes = "Update OneAPI details based on ID, API and Customer", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Updated OneAPI details", response = AdminOneapiDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to update OneAPI details due to server Error")
	})
	@PutMapping( value = RestURIConstants.CREATE_ONEAPI, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> updateOneapiDetails(
			@ApiParam(value = "Object of OneAPI details", required = true) @Valid @RequestBody AdminOneapiDTO oneapiDTO_obj);
	
	
	@ApiOperation(value = "Create EVOBUS API details", notes = "Create EVOBUS API details in DB", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Create EVOBUS API for admin setting", response = AdminOneapiDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Create EVOBUS for admin setting due to server Error")
	})
	@PostMapping( value = RestURIConstants.CREATE_EVOBUSAPI, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> createEVOBusApi(
			@ApiParam(value = "Object of Oneapi details for admin setting", required = true) @Valid @RequestBody AdminOneapiDTO oneapiDTO_obj);
	

}