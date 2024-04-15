package com.alphax.api.mb;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.alphax.common.response.AlphaXResponse;
import com.alphax.util.RestURIConstants;
import com.alphax.vo.mb.AdminCompanysDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

/**
 * @author A98157120
 *
 */
@Api(value = "ALPHAX",  tags = {"AlphaXAdminSetup"} )
@SwaggerDefinition(tags = {
		@Tag(name = "AlphaXAdminSetup", description = "REST controller that Setup the Initial Admin User for AlphaX")
})
public interface AlphaXSetupApi {
	
	@ApiOperation(value = "Setup AlphaX Admin User", notes = "Setup the Initial Admin User for AlphaX", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Setup the Initial Admin User for AlphaX", response = AdminCompanysDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Setup AlphaX Admin User due to server Error")
	})
	@PostMapping( value = RestURIConstants.ALPHAX_ADMIN_SETUP, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> alphaXAdminUserSetup(
			@ApiParam(value = "AlphaX Admin User", required = true) @PathVariable(value = "adminUser", required = true) String adminUser );


}
