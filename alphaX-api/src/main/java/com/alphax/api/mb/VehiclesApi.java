/**
 * 
 */
package com.alphax.api.mb;

import javax.validation.Valid;
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
import com.alphax.vo.mb.CustomerVegaDTO;
import com.alphax.vo.mb.SearchCustomerDTO;
import com.alphax.vo.mb.SearchVehicleDTO;
import com.alphax.vo.mb.VehicleDetailsVO;
import com.alphax.vo.mb.VehicleStandardInfoVO;

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
@Api(value = "ALPHAX",  tags = {"Vehicles"} )
@SwaggerDefinition(tags = {
		@Tag(name = "Vehicles", description = "REST controller that provides information regarding the customer.")
})
public interface VehiclesApi {

	@ApiOperation(value = "Creates a new vehicle", notes = "Creates a new vehicle", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Gets Vehicle Object", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request" ),
			@ApiResponse(code = 401, message = "Unauthorized" ),
			@ApiResponse(code = 500, message = "unable to create vehicle due to server Error") 
	})
	@PostMapping(value = RestURIConstants.VEHICLES)
	public ResponseEntity<AlphaXResponse> createVehicle(
			@ApiParam(value = "Vehicle Object", required = true) @Valid @RequestBody VehicleDetailsVO vehicleDtl);
			
	
	@ApiOperation(value = "Returns a list of vehicles.", notes = "Returns a list of vehicles based on search criteria", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get vehicles List", response = SearchVehicleDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get vehicles due to server Error")
	})
	@GetMapping( value = RestURIConstants.VEHICLES, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getVehiclesSearchList(
			@ApiParam(value = "the search string that should be used for searching appropriate vehicle licensplate entries", required = true)  @RequestParam(value = "searchString") String searchString,
			@ApiParam(value = "Number of records in each page", required = false)  @RequestParam(value = "pageSize", required = false) String pageSize, 
			@ApiParam(value = "Page Number to show", required = false)  @RequestParam(value = "pageNumber", required = false) String pageNumber);
	
	
	@ApiOperation(value = "Returns couple of standard information of vehicle.", notes = "Returns vehicle standard information based on VIN number", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get vehicle data", response = VehicleStandardInfoVO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get vehicle standard information due to server Error")
	})
	@GetMapping( value = RestURIConstants.VEHICLES_STANDARD_INFO, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getVehiclesInfoByVin(
			@ApiParam(value = "the VIN number that should be used to get vehicle standard information.It's value length should be first 6 digits ", required = true)  @NotBlank @RequestParam(value = "vin") String vin);
			
	
	@ApiOperation(value = "Returns a list of Marke (Brand List)", notes = "Returns a list of Marke (Brand List)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Marke List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Marke list due to server Error")
	})
	@GetMapping( value = RestURIConstants.VEHICLES_MARKE, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getMarkeList();
			
	
	@ApiOperation(value = "Returns a list of Zulassungsart (Registration type List)", notes = "Returns a list of Zulassungsart (Registration type List)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Zulassungsart (Registration type) List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Zulassungsart list due to server Error")
	})
	@GetMapping( value = RestURIConstants.VEHICLES_ZULASSUNGSART, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getZulassungsartList();
	
	
	@ApiOperation(value = "Returns a list of Antriebsart (Engine type List)", notes = "Returns a list of Antriebsart (Engine type List)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Antriebsart (Engine type) List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Antriebsart list due to server Error")
	})
	@GetMapping( value = RestURIConstants.VEHICLES_ANTRIEBSART, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getAntriebsartList();
	
	
	@ApiOperation(value = "Returns a list of Laufleistung (Mileage indicator)", notes = "This list is master data, which will be return from Stub, not required DB or COBOL call", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Laufleistung (Mileage indicator) List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Laufleistung list due to server Error")
	})
	@GetMapping( value = RestURIConstants.VEHICLES_LAUFLEISTUNG, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getLaufleistungList();
	
	
	@ApiOperation(value = "Returns a list of Verkäufer (Salesman List)", notes = "Returns a list of Verkäufer (Salesman List)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Verkäufer (Salesman) List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Verkäufer list due to server Error")
	})
	@GetMapping( value = RestURIConstants.VEHICLES_VERKAUFER, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getVerkauferList();
	
	
	@ApiOperation(value = "Returns a list of Verkaufssparte (sales division List)", notes = "Returns a list of Verkaufssparte (sales division List)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Verkaufssparte (sales division) List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Verkaufssparte list due to server Error")
	})
	@GetMapping( value = RestURIConstants.VEHICLES_VERKAUFSSPARTE, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getVerkaufssparteList();
	
	
	@ApiOperation(value = "Returns a list of Akquisitionssperre (Acquisition lock List)", notes = "Returns a list of Akquisitionssperre (Acquisition lock List)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Akquisitionssperre (Acquisition lock) List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Akquisitionssperre list due to server Error")
	})
	@GetMapping( value = RestURIConstants.VEHICLES_AKQUISITIONSPERRE, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getAkquisitionspereList();
	
	
	@ApiOperation(value = "Returns a list of Wartungs-Gruppe (Maintenance group)", notes = "This list is master data, which will be return from Stub, not required DB or COBOL call", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Wartungs-Gruppe (Maintenance group) List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Wartungs-Gruppe list due to server Error")
	})
	@GetMapping( value = RestURIConstants.VEHICLES_WARTGRUPPE, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getWartGruppeList();
	
	
	@ApiOperation(value = "Returns a list of Kundendienst-Berater (Customer Service Advisor List)", notes = "Returns a list of Kundendienst-Berater (Customer Service Advisor List)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Kundendienst-Berater (Customer service advisor) List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Kundendienst-Berater list due to server Error")
	})
	@GetMapping( value = RestURIConstants.VEHICLES_KUNDENDIENST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getKundendBeraterList();
	
	
	@ApiOperation(value = "Returns a list of Betreuende Werkstatt (Supporting workshop List)", notes = "Returns a list of Betreuende Werkstatt (Supporting workshop List)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Betreuende Werkstatt (Supporting workshop) List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Betreuende Werkstatt list due to server Error")
	})
	@GetMapping( value = RestURIConstants.VEHICLES_BETREUENDE, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getBetreuendeWerkList();
	
	
	@ApiOperation(value = "Returns the details information for a particular vehicle shown in the alphaX UI.", notes = "Returns specific vehicle details based on license number", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get vehicle details", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get vehicle due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_VEHICLE_DETAILS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getVehicleDetailsByID(
			@ApiParam(value = "Vehicle license number", required = true) @PathVariable(value = "id", required = true) String id );
	
	
	@ApiOperation(value = "Returns a list of Kunden-Nr. VEGA.", notes = "Returns a list of Kunden-Nr. VEGA based on search criteria", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Kunden-Nr. VEGA List", response = CustomerVegaDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Kunden-Nr. VEGA due to server Error")
	})
	@GetMapping( value = RestURIConstants.VEHICLES_KUNDEN_VEGA, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getKundenNr_VEGAList();
	
	
	@ApiOperation(value = "Returns a list of Kunden-Nr", notes = "Returns a list of Kunden-Nr. based on search criteria", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Kunden-Nr List", response = SearchCustomerDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Kunden-Nr due to server Error")
	})
	@GetMapping( value = RestURIConstants.VEHICLES_KUNDEN_NR, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getKundenNrList(
			@ApiParam(value = "the search string that should be used for searching appropriate vehicle licensplate entries", required = false)  @RequestParam(value = "searchString", required = false) String searchString,
			@ApiParam(value = "Number of records in each page", required = false)  @RequestParam(value = "pageSize", required = false) String pageSize, 
			@ApiParam(value = "Page Number to show", required = false)  @RequestParam(value = "pageNumber", required = false) String pageNumber);
	
	
	@ApiOperation(value = "Returns a list of Aktions", notes = "Returns a list of Aktions", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Aktions List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Aktions list due to server Error")
	})
	@GetMapping( value = RestURIConstants.VEHICLES_AKTION, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getAktionList();
	
	
	@ApiOperation(value = "Update existing vehicle", notes = "Update existing vehicle", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Update Vehicle Object", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request" ),
			@ApiResponse(code = 401, message = "Unauthorized" ),
			@ApiResponse(code = 500, message = "unable to update vehicle due to server Error") 
	})
	@PutMapping(value = RestURIConstants.VEHICLES)
	public ResponseEntity<AlphaXResponse> updateVehicle(
			@ApiParam(value = "Vehicle Object", required = true) @Valid @RequestBody VehicleDetailsVO vehicleDtl);
	
			
	@ApiOperation(value = "Check duplicate Licence plate number", notes = "Check duplicate Licence plate number", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Check duplicate Licence plate number", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request" ),
			@ApiResponse(code = 401, message = "Unauthorized" ),
			@ApiResponse(code = 500, message = "unable to Check duplicate Licence plate number due to server Error") 
	})
	@GetMapping(value = RestURIConstants.VEHICLE_DUPLICATE_LICENCE, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> checkDuplicateLicencePlate(
			@ApiParam(value = "The Licence Plate number entry that need to be checked in DB table", required = true)  @NotBlank @RequestParam(value = "licencePlate") String licencePlate);
	
	
	@ApiOperation(value = "Check duplicate vehicle identification number", notes = "Check duplicate vehicle identification number", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Check duplicate vehicle identification number", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request" ),
			@ApiResponse(code = 401, message = "Unauthorized" ),
			@ApiResponse(code = 500, message = "unable to Check duplicate vehicle identification number due to server Error") 
	})
	@GetMapping(value = RestURIConstants.VEHICLE_DUPLICATE_VIN, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> checkDuplicateVIN(
			@ApiParam(value = "The vehicle identification number entry that need to be checked in DB table", required = true) @NotBlank  @RequestParam(value = "vin") String vin);
	
	
}