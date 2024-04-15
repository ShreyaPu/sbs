package com.alphax.api.mb;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.alphax.common.response.AlphaXResponse;
import com.alphax.util.RestURIConstants;
import com.alphax.vo.mb.LoginAgencysDTO;

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
@Api(value = "ALPHAX",  tags = {"Company"} )
@SwaggerDefinition(tags = {
		@Tag(name = "Company", description = "REST controller that provides information regarding the Company.")
})
public interface CompanyApi {
	
	
	@ApiOperation(value = "Returns a list of alphaPlus agency", notes = "Returns a list of alphaPlus agency based on datalibrary", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get alphaPlus agency List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get alphaPlus agency list due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_COMPANY, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getAlphaPlusAgencyList(
			@ApiParam(value = "AlphaPlus Company Id", required = true) @PathVariable(value = "id", required = true) String id );

	
	@ApiOperation(value = "Returns a list of login agency", notes = "Returns a list of login agency based on schema", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get login agency List", response = LoginAgencysDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get login agency due to server Error")
	})
	@GetMapping( value = RestURIConstants.LOGIN_AGENCY, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getAlphaXAgencyList();
	
	
	@ApiOperation(value = "Authorization fuctionality for application", notes = "Authorization fuctionality for application", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Authorization successfull with jwt token details", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request" ),
			@ApiResponse(code = 401, message = "Unauthorized" ),
			@ApiResponse(code = 500, message = "unable to create authorization token due to server Error") 
	})
	@PostMapping(value = RestURIConstants.LOGIN_AGENCY)
	public ResponseEntity<AlphaXResponse> createAuthenticationToken(
			@ApiParam(value = "Object of selected agency details for admin setting", required = true) @Valid @RequestBody LoginAgencysDTO loginAgency_obj);
			

}
