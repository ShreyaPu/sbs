/**
 * 
 */
package com.alphax.api.mb;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.alphax.common.response.AlphaXResponse;
import com.alphax.util.RestURIConstants;
import com.alphax.vo.mb.JwtRequest;
import com.alphax.vo.mb.PasswordChangedDTO;

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
@Api(value = "ALPHAX",  tags = {"JwtAuthentication"} )
@SwaggerDefinition(tags = {
		@Tag(name = "JwtAuthentication", description = "REST controller that provides information regarding the Authentication.")
})
public interface JwtAuthenticationApi {
	
	
	@ApiOperation(value = "login fuctionality for application", notes = "login fuctionality for application", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "login successfull with jwt token details", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request" ),
			@ApiResponse(code = 401, message = "Unauthorized" ),
			@ApiResponse(code = 500, message = "unable to login/authenticate due to server Error") 
	})
	@PostMapping(value = "/authenticate")
	public ResponseEntity<AlphaXResponse> createAuthenticationToken(
			@ApiParam(value = "username and password details for login to application", required = true) @RequestBody JwtRequest authenticationRequest, 
			@ApiParam(value = "Http Request Object") HttpServletRequest request);
	
	
	@ApiOperation(value = "User permissions is use for the given Firma", notes = "it offers all the required permission for different opeations ", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get user permissions", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get user permissions due to server Error")
	})
	@GetMapping( value = RestURIConstants.USER_PERMISSIONS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getUserPermissions();
	

	@ApiOperation(value = "Extended user parameters and informations for the given Loginname", notes = "it offers all the extended user parameters and informations ", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get user permissions", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get user parameters and informations due to server Error")
	})
	@GetMapping( value = RestURIConstants.USER_INFORMATION, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getUserStandardInfo();
	
	
	@ApiOperation(value = "logout fuctionality for application.", notes = "logout fuctionality for application", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "logout successfully", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request" ),
			@ApiResponse(code = 401, message = "Unauthorized" ),
			@ApiResponse(code = 500, message = "unable to logout due to server Error") 
	})
	@GetMapping(value = RestURIConstants.LOGOUT)
	public ResponseEntity<AlphaXResponse> logoutApplication(@ApiParam(value = "Http Request Object") HttpServletRequest request);
	
	
	@ApiOperation(value = "Return user company informations for the given Loginname", notes = "it offers user company informations ", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get user company informations", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get user company due to server Error")
	})
	@GetMapping( value = RestURIConstants.USER_COMPANY_INFORMATION, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getUserCompanyDetails();
	
	
	@ApiOperation(value = "login fuctionality for AlphaX Setup", notes = "login fuctionality for AlphaX Setup", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "login for AlphaX Setup successfull with jwt token details", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request" ),
			@ApiResponse(code = 401, message = "Unauthorized" ),
			@ApiResponse(code = 500, message = "unable to login for AlphaX Setup due to server Error") 
	})
	@PostMapping(value = RestURIConstants.ALPHAX_SETUP_LOGIN)
	public ResponseEntity<AlphaXResponse> alphaXSetupLogin(
			@ApiParam(value = "username and password details for login to application", required = true) @RequestBody JwtRequest authenticationRequest, 
			@ApiParam(value = "Http Request Object") HttpServletRequest request);
	
	
	
	@ApiOperation(value = "Returns password change conformation.", notes = "Returns password change conformation", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Password changed successfully", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to change password due to server Error")
	})
	@PostMapping( value = RestURIConstants.PASSWORD_CHANGED, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> changePassword(
			@ApiParam(value = "username and password details for changed password", required = true) @RequestBody PasswordChangedDTO passwordChangedDTO,
			@ApiParam(value = "Http Request Object") HttpServletRequest request);
	
	@ApiOperation(value = "Terminate java session", notes = "Terminate java session", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Terminate java session successfully", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request" ),
			@ApiResponse(code = 401, message = "Unauthorized" ),
			@ApiResponse(code = 500, message = "unable to Terminate java session due to server Error") 
	})
	@GetMapping(value = RestURIConstants.KILL_ME)
	public ResponseEntity<AlphaXResponse> killMe(
			@ApiParam(value = "User Name", required = true) @NotBlank @RequestParam(value = "userName", required = true) String userName,
			@ApiParam(value = "Password", required = true) @NotBlank @RequestParam(value = "password", required = true) String password );
}
