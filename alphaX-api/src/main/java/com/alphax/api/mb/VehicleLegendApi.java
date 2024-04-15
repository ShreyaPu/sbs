package com.alphax.api.mb;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.alphax.common.response.AlphaXResponse;
import com.alphax.util.RestURIConstants;
import com.alphax.vo.mb.VehicleLegendDTO;

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
@Api(value = "ALPHAX",  tags = {"VehicleLegend"} )
@SwaggerDefinition(tags = {
		@Tag(name = "VehicleLegend", description = "REST controller that provides information regarding the Vehicle Lengends.")
})
public interface VehicleLegendApi {

	@ApiOperation(value = "Returns the legend of a vehicle.", notes = "Returns the legend of a vehicle.", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get vehicle Legend List", response = VehicleLegendDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get vehicle Legend due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_VEHICLE_LEGENDS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getVehicleLegend( @ApiParam(value = "Vehicle Identification Number", required = true) @PathVariable(value = "id", required = true) String vehicleId,
			@ApiParam(value = "Number of records in each page", required = false)  @RequestParam(value = "pageSize", required = false) String pageSize, 
			@ApiParam(value = "Page Number to show", required = false)  @RequestParam(value = "pageNumber", required = false) String pageNumber);
	
	
	@ApiOperation(value = "Returns a list of Reparaturkennzeichen (Repair Codes List)", notes = "Returns a list of Reparaturkennzeichen (Repair Code List)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Reparaturkennzeichen (Repair Codes) List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Reparaturkennzeichen list due to server Error")
	})
	@GetMapping( value = RestURIConstants.VEHICLE_LEGEND_REPAIRCODE, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getRepairCodeList();
	
}