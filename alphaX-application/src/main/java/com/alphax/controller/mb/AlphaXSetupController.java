/**
 * 
 */
package com.alphax.controller.mb;

import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alphax.common.exception.ExceptionMessages;
import com.alphax.common.response.AlphaXResponse;
import com.alphax.service.mb.AlphaXSetupService;
import com.alphax.api.mb.AlphaXSetupApi;
import com.alphax.common.constants.AlphaXCommonUtils;
import com.alphax.util.DecryptTokenUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author A98157120
 *
 */
@RestController
@RequestMapping("/api")
@Slf4j
@Validated
public class AlphaXSetupController implements AlphaXSetupApi {
	
	@Autowired
	AlphaXCommonUtils commonUtils;
	
	@Autowired
	AlphaXSetupService alphaXSetup;
	
	@Autowired
	DecryptTokenUtils decryptUtil;
	
	
	/**
	 * This method is used set up the Initial Admin User for AlphaX
	 */		
	@Override
	public ResponseEntity<AlphaXResponse> alphaXAdminUserSetup(@PathVariable(value = "adminUser", required = true) String adminUser) {
		
		log.info("AlphaXSetupController : Start AlphaX Admin User Setup");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> adminUserFlag = alphaXSetup.alphaXAdminUserSetup(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), adminUser, decryptUtil.getLogedInUser());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(adminUserFlag);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	

}