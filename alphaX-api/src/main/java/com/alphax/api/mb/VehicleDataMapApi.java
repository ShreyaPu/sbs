/**
 * 
 */
package com.alphax.api.mb;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.alphax.common.response.AlphaXResponse;
import com.alphax.util.RestURIConstants;
import com.alphax.vo.mb.VehicleDataMapDTO;

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
@Api(value = "ALPHAX",  tags = {"Vehicledatamap"} )
@SwaggerDefinition(tags = {
		@Tag(name = "Vehicledatamap", description = "REST controller that provides information regarding the Vehicle DataMap.")
})
public interface VehicleDataMapApi {

	@ApiOperation(value = "Returns the data map of a vehicle.", notes = "Returns the data map of a vehicle.", response = AlphaXResponse.class )
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get data map of a vehicle", response = VehicleDataMapDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get data map of a vehicle due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_VEHICLE_DATAMAP, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getVehicleDataMap( @ApiParam(value = "Vehicle Identification Number", required = true) @PathVariable(value = "id", required = true) String vehicleId);

}