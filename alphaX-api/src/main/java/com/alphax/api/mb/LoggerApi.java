package com.alphax.api.mb;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.alphax.common.response.AlphaXResponse;
import com.alphax.util.RestURIConstants;
import com.alphax.vo.mb.VehicleLegendDTO;

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
@Api(value = "ALPHAX",  tags = {"Logger"} )
@SwaggerDefinition(tags = {
		@Tag(name = "Logger", description = "REST controller that provides Logs of AlphaX.")
})
public interface LoggerApi {

	@ApiOperation(value = "Returns the logs of alphaX", notes = "Returns the log of alphaX application.", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get the logs information", response = VehicleLegendDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get logs information due to server Error")
	})
	@GetMapping( value = RestURIConstants.LOGS_INFO, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getLogsInformation();
	
	
	@ApiOperation(value = "downloads the logs of alphaX", notes = "downloads the log of alphaX application.", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "download the logs information", response = VehicleLegendDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to download log file due to server Error")
	})
	@GetMapping( value = RestURIConstants.DOWNLOAD_LOGS, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE )
	public <T> ResponseEntity<T> downloadLogFile();
	
		
}