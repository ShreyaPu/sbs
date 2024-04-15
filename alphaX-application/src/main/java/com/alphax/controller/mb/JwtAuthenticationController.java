package com.alphax.controller.mb;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alphax.config.JwtTokenUtil;
import com.alphax.service.mb.JwtAuthenticationService;
import com.alphax.common.exception.ExceptionMessages;
import com.alphax.common.response.AlphaXResponse;
import com.alphax.api.mb.JwtAuthenticationApi;
import com.alphax.common.constants.AlphaXCommonUtils;
import com.alphax.util.DecryptTokenUtils;
import com.alphax.vo.mb.JwtRequest;
import com.alphax.vo.mb.PasswordChangedDTO;
import com.alphax.vo.mb.UserCompanyInfoDTO;
import com.alphax.vo.mb.UserPermissionsVO;
import com.alphax.vo.mb.UserStandardInfoDTO;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
@Validated
public class JwtAuthenticationController implements JwtAuthenticationApi{

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService jwtInMemoryUserDetailsService;

	@Autowired
	private JwtAuthenticationService jwtAuthenticationService; 

	@Autowired
	AlphaXCommonUtils commonUtils;

	@Autowired
	private DecryptTokenUtils decryptUtil;


	/**
	 * This method is used to login to alphaX application.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> createAuthenticationToken( @RequestBody JwtRequest authenticationRequest, HttpServletRequest request ) {

		log.info("Inside JwtAuthenticationController");
		AlphaXResponse<Object> response = new AlphaXResponse<>();
		String computerName = jwtTokenUtil.getClintIPAddress(request);
		
		log.info("Client IP Address : {}  AND User Name :{}", computerName, authenticationRequest.getUsername());
		log.info("First login time : {}", new Timestamp(System.currentTimeMillis()));

		List<String> userDetailsList = jwtAuthenticationService.authenticate(authenticationRequest.getUsername(),
				authenticationRequest.getPassword(), computerName);

		final UserDetails userDetails = jwtInMemoryUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails, userDetailsList);
		int passwordExpireDays = Integer.parseInt(userDetailsList.get(14));
		
		if(passwordExpireDays>= 1 && passwordExpireDays <= 7) {	
			
			response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), 
					LocaleContextHolder.getLocale(), ExceptionMessages.LOGIN_WARNING_MSG_KEY, passwordExpireDays));
		}
		else {
			response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), 
					LocaleContextHolder.getLocale(), ExceptionMessages.LOGIN_SUCCESS_MSG_KEY));
		}
		response.setResultSet(token);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	/**
	 * This method is used to get the user permissions for logged-in user.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getUserPermissions() {

		log.info("CustomerController : Start getCustomerOne");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		UserPermissionsVO userPermissions = jwtAuthenticationService.getUserPermissions(decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), 
				decryptUtil.getApAgencyId());

		//encrypt the userPermission with jwt encryption.
		final String permissionToken = jwtTokenUtil.generatePermissions(userPermissions);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(permissionToken);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used to get the user Standard information.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getUserStandardInfo() {

		log.info("CustomerController : Start getUserStandardInfo");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		UserStandardInfoDTO userStandardInfo = jwtAuthenticationService.getUserStandardInfo(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), 
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), decryptUtil.getLogedInUser());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(userStandardInfo);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used to logout from alphaX application.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> logoutApplication(HttpServletRequest request ) {

		log.info("Inside JwtAuthenticationController : Start logoutApplication");
		AlphaXResponse<Object> response = new AlphaXResponse<>();
		String computerName = jwtTokenUtil.getClintIPAddress(request);

		boolean isLogoutSuccess = jwtAuthenticationService.logoutApplication(computerName);

		if(isLogoutSuccess) {
			response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), 
					LocaleContextHolder.getLocale(), ExceptionMessages.LOGOUT_SUCCESS_MSG_KEY));
		}
		else {
			response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), 
					LocaleContextHolder.getLocale(), ExceptionMessages.LOGOUT_FAILED_MSG_KEY));
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	/**
	 * This method is used to get the Company Agency Details.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getUserCompanyDetails() {

		log.info("CustomerController : Start getUserCompanyDetails");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		UserCompanyInfoDTO userCompanyInfo = jwtAuthenticationService.getUserCompanyDetails(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), 
				decryptUtil.getLogedInUser());


		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(userCompanyInfo);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used to setup login details.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> alphaXSetupLogin( @RequestBody JwtRequest authenticationRequest, HttpServletRequest request ) {

		log.info("Inside JwtAuthenticationController : alphaXSetupLogin");
		AlphaXResponse<Object> response = new AlphaXResponse<>();
		String computerName = jwtTokenUtil.getClintIPAddress(request);
		
		log.info("Client IP Address : {}  AND User Name :{}", computerName, authenticationRequest.getUsername());
		log.info("First login time : {}", new Timestamp(System.currentTimeMillis()));

		List<String> userDetailsList = jwtAuthenticationService.alphaXSetupLogin(authenticationRequest.getUsername(),
				authenticationRequest.getPassword(), computerName);

		final UserDetails userDetails = jwtInMemoryUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails, userDetailsList);

		response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.LOGIN_SUCCESS_MSG_KEY));

		response.setResultSet(token);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used to changed the password.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> changePassword(@RequestBody PasswordChangedDTO passwordChangedDTO, HttpServletRequest request) {

		log.info("JwtAuthenticationController : Start changePassword");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		String computerName = jwtTokenUtil.getClintIPAddress(request);
		
		log.info("Client IP Address : {}  AND User Name :{}", computerName, passwordChangedDTO.getLoginUserId());
		log.info("Password changed time : {}", new Timestamp(System.currentTimeMillis()));
		
		Map<String, Boolean> passwordFlag = jwtAuthenticationService.changePassword(passwordChangedDTO, computerName);


		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(passwordFlag);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used to terminate current java session
	 */
	@Override
	public ResponseEntity<AlphaXResponse> killMe(
			@NotBlank @RequestParam(value = "userName", required = true) String userName,
			@NotBlank @RequestParam(value = "password", required = true) String password) {

		log.info("Inside JwtAuthenticationController : Start killMe");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		boolean isLogoutSuccess = jwtAuthenticationService.killMe(userName, password);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(isLogoutSuccess);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}

}