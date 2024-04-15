package com.alphax.api.mb;

import javax.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.alphax.common.response.AlphaXResponse;
import com.alphax.util.RestURIConstants;
import com.alphax.vo.mb.Customer;
import com.alphax.vo.mb.CustomerDefaultDataVO;
import com.alphax.vo.mb.SearchCustomerDTO;
import com.alphax.vo.mb.SearchVehicleDTO;

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
@Api(value = "ALPHAX",  tags = {"Customer"} )
@SwaggerDefinition(tags = {
		@Tag(name = "Customer", description = "REST controller that provides information regarding the customer.")
})
public interface CustomersApi {

	
	@ApiOperation(value = "Creates a new customer", notes = "Creates a new customer", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Gets Customer Object with customer identifier", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request" ),
			@ApiResponse(code = 401, message = "Unauthorized" ),
			@ApiResponse(code = 500, message = "unable to create customer due to server Error") 
	})
	@PostMapping(value = RestURIConstants.CUSTOMER)
	public ResponseEntity<AlphaXResponse> createCustomer(@ApiParam(value = "Customer Object", required = true) @RequestBody Customer customerDtl);
	
	
	@ApiOperation(value = "Update of a customer", notes = "Update of a customer", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Gets Customer Object with updated values", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request" ),
			@ApiResponse(code = 401, message = "Unauthorized" ),
			@ApiResponse(code = 500, message = "unable to update customer due to server Error") 
	})
	@PutMapping(value = RestURIConstants.CUSTOMER)
	public ResponseEntity<AlphaXResponse> updateCustomer(@ApiParam(value = "Customer Object", required = true) @RequestBody Customer customerDtl);
	
	
	@ApiOperation(value = "Returns the details information for a particular customer shown in the alphaX UI.", notes = "Returns specific customer details based on customer id", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Customer details", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get customer due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_CUSTOMER_DETAILS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getCustomerDetailsByID(
			@ApiParam(value = "Customer Identifier", required = true) @PathVariable(value = "id", required = true) int id );
	
	
	@ApiOperation(value = "Returns a list of customers", notes = "Returns a list of customers based on search criteria", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Customer List", response = SearchCustomerDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get customer due to server Error")
	})
	@GetMapping( value = RestURIConstants.CUSTOMER, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getCustomerSearchList(
			@ApiParam(value = "the search string that should be used for searching appropriate customer entries", required = true)  @RequestParam(value = "searchText", required = true) String searchText,
			@ApiParam(value = "Number of records in each page", required = false)  @RequestParam(value = "pageSize", required = false) String pageSize, 
			@ApiParam(value = "Page Number to show", required = false)  @RequestParam(value = "pageNumber", required = false) String pageNumber);
	
	
	@ApiOperation(value = "Returns a list of akquisitionskz", notes = "Returns a list of akquisitionskz for customer", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get akquisitionskz List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get akquisitionskz list due to server Error")
	})
	@GetMapping( value = RestURIConstants.CUSTOMER_AKQUISITIONSKZ, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getAkquisitionskz();
	
	
	@ApiOperation(value = "Returns a list of branchenschlussel", notes = "Returns a list of branchenschlussel for customer", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get branchenschlussel List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get branchenschlussel list due to server Error")
	})
	@GetMapping( value = RestURIConstants.CUSTOMER_BRANCHENSCHLUSSEL, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getBranchenSchlussel();
	
	
	@ApiOperation(value = "Returns a list of anredeschlussel", notes = "Returns a list of anredeschlussel for customer", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get anredeschlussel List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get anredeschlussel list due to server Error")
	})
	@GetMapping( value = RestURIConstants.CUSTOMER_ANREDESCHLUSSEL, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getAnredeSchlussel();
	
	
	@ApiOperation(value = "CustomerOne is in use for the given Firma", notes = "it offers the needed information if the tool CustomerOne is in use or not", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get customerOne information", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get customerOne info due to server Error")
	})
	@GetMapping( value = RestURIConstants.CUSTOMER_ONE_CHECK, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getCustomerOne();
	
	
	@ApiOperation(value = "Update customer/Vehicle flag for database operation ", notes = "Return true/false based on transaction ", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Update customer/Vehicle edit flag", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "Unable to Update flag due to server Error")
	})
	@PutMapping( value = RestURIConstants.AUTHORITY_FLAG, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> updateEditFlag(
			@ApiParam(value = "Affects (vehicle, customer) that should be used to let the API know which use case is addressed ", required = true) @NotBlank @RequestParam(value = "affects", required = true) String affects,
			@ApiParam(value = "flag that should be used for updating the value (deleted, in_use, disabled, enabled,  clear, moved)", required = true) @NotBlank @RequestParam(value = "flag", required = true) String flag,
			@ApiParam(value = "Customer or Vehicle Identifier", required = true) @NotBlank @RequestParam(value = "id", required = true) String id );
	
	
	@ApiOperation(value = "Flag value is use to check that database opertaions are alloweded or Not for customer/Vehicle update", notes = "Return true/false based on the Flag (deleted, in_use, disabled, enabled,  clear, moved)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "get customer/Vehicle edit flag value", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "Unable to get flag value due to server Error")
	})
	@GetMapping( value = RestURIConstants.AUTHORITY_FLAG, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getEditFlagValueById(
			@ApiParam(value = "affects (vehicle, customer) that should be used to let the API know which use case is addressed ", required = true) @NotBlank @RequestParam(value = "affects", required = true) String affects,
			@ApiParam(value = "Customer or Vehicle Identifier", required = true)  @NotBlank @RequestParam(value = "id", required = true) String id );
	
	
	@ApiOperation(value = "Returns customers default data", notes = "Returns customers default data based on Company and customer group", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get customers default data", response = CustomerDefaultDataVO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get customers default data due to server Error")
	})
	@GetMapping( value = RestURIConstants.CUSTOMER_DEFAULT_DATA, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getCustomerDefaultData(
			@ApiParam(value = "Customer group value", required = false)  @RequestParam(value = "customerGroup", required = false) String customerGroup); 
	
	
	@ApiOperation(value = "Returns a list of Mehrwertsteuer-Kennziffer(VAT code)", notes = "This list is master data, which will be return from Stub, not required DB or COBOL call", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Mehrwertsteuer List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Mehrwertsteuer list due to server Error")
	})
	@GetMapping( value = RestURIConstants.CUSTOMER_MEHRWERTSTEUER, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getMehrwertsteuerList();
	
	
	@ApiOperation(value = "Returns a list of Skonto-Theke (Cash discount counter)", notes = "This list is master data, which will be return from Stub, not required DB or COBOL call", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Skonto List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Skonto list due to server Error")
	})
	@GetMapping( value = RestURIConstants.CUSTOMER_SKONTO, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getSkontoList();
	
	
	@ApiOperation(value = "Returns a list of Zu-Abschlag (Surcharge / discount)", notes = "This list is master data, which will be return from Stub, not required DB or COBOL call", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get ZuAbschlag List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get ZuAbschlag list due to server Error")
	})
	@GetMapping( value = RestURIConstants.CUSTOMER_ZUABSCHLAG, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getZuAbschlagList();
	
	
	@ApiOperation(value = "Returns a list of Rabattkennzeichen (Discount indicator)", notes = "This list is master data, which will be return from Stub, not required DB or COBOL call", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Rabattkz List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Rabattkz list due to server Error")
	})
	@GetMapping( value = RestURIConstants.CUSTOMER_RABATTKZ, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getRabattkzList();
	
	
	@ApiOperation(value = "Returns a list of Elektronische Rechnung  (Electronic invoice)", notes = "This list is master data, which will be return from Stub, not required DB or COBOL call", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get ElektRechnung List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get ElektRechnung list due to server Error")
	})
	@GetMapping( value = RestURIConstants.CUSTOMER_ELEKRECHNUNG, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getElektRechnungList();
	
	
	@ApiOperation(value = "Returns a list of XML-Daten  (XML DATA)", notes = "This list is master data, which will be return from Stub, not required DB or COBOL call", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get XMLDaten List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get XMLDaten list due to server Error")
	})
	@GetMapping( value = RestURIConstants.CUSTOMER_XMLDATEN, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getXMLDatenList();
	
	
	@ApiOperation(value = "Returns a list of Herkunft  (Origin)", notes = "This list is master data, which will be return from Stub, not required DB or COBOL call", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Herkunft List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Herkunft list due to server Error")
	})
	@GetMapping( value = RestURIConstants.CUSTOMER_HERKUNFT, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getHerkunftList();
	
	
	@ApiOperation(value = "Returns a list of Land  (Country List)", notes = "Returns a list of Land (Countries)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Land List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Land list due to server Error")
	})
	@GetMapping( value = RestURIConstants.CUSTOMER_LAND, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getLandList();
	
	
	@ApiOperation(value = "Returns a list of Bonitäte (Credit ratings List)", notes = "Returns a list of Bonitäte (Credit ratings)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Bonitäte List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Bonitäte list due to server Error")
	})
	@GetMapping( value = RestURIConstants.CUSTOMER_BONITATE, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getBonitateList();
	
	
	@ApiOperation(value = "Returns a list of DruckerIds (Printer List)", notes = "Returns a list of DruckerIds (Printer List)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Drucker List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Drucker list due to server Error")
	})
	@GetMapping( value = RestURIConstants.CUSTOMER_DRUCKER, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getDruckerList();
			
	
	@ApiOperation(value = "Returns a list of Anrede  (Salutation DATA)", notes = "This list is master data, which will be return from Stub, not required DB or COBOL call", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Anrede List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Anrede list due to server Error")
	})
	@GetMapping( value = RestURIConstants.CUSTOMER_ANREDE, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getAnredeCodeList();
	
	
	@ApiOperation(value = "Returns a list of vehicles that are assigned to a customer.", notes = "Returns a list of vehicles based on customer number", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get customer's vehicles List", response = SearchVehicleDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get customer's vehicles due to server Error")
	})
	@GetMapping( value = RestURIConstants.CUSTOMER_VEHICLE, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getCustomerVehicles(
			@ApiParam(value = "the customer number is using for getting vehicles that are assigned to a customer", required = true) @PathVariable(value = "id", required = true) String id);
	
}
