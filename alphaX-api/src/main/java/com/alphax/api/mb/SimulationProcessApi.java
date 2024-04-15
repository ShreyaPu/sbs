/**
 * 
 */
package com.alphax.api.mb;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import com.alphax.common.response.AlphaXResponse;
import com.alphax.util.RestURIConstants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

/**
 * @author A106744104
 *
 */
@Api(value = "ALPHAX",  tags = {"SimulationProcess"} )
@SwaggerDefinition(tags = {
		@Tag(name = "SimulationProcess", description = "REST controller that provides information for Data Simulation Processing.")
})
public interface SimulationProcessApi {

	
	@ApiOperation(value = "Creates the random list of delivery Notes using COBOL", notes = "Creates the random list of delivery Notes using COBOL", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "generates the random delivery notes", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to generate delivery notes due to server Error")
	})
	@PostMapping( value = RestURIConstants.SIMULATE_DELIVERYNOTE, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> generateDeliveryNotes();
	
	
	@ApiOperation(value = "Trigger a event to automatically process the delivery notes to BSN and ET", notes = "Trigger a event to automatically process the delivery notes to BSN and ET", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "automatically process the delivery notes", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to process the delivery notes due to server Error")
	})
	@PostMapping( value = RestURIConstants.SIMULATE_AUTOPROCESS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> autoProcessDiveryNotes();
	
	
	@ApiOperation(value = "Creates the random list of Conflict delivery Notes using COBOL", notes = "Creates the random list of Conflict delivery Notes using COBOL", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "generates the random conflict delivery notes", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to generate conflict delivery notes due to server Error")
	})
	@PostMapping( value = RestURIConstants.SIMULATE_CONFLICT_DELIVERYNOTE, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> generateConflictDeliveryNotes();
	
}
