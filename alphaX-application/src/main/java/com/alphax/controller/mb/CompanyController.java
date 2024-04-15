package com.alphax.controller.mb;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alphax.common.exception.ExceptionMessages;
import com.alphax.common.response.AlphaXResponse;
import com.alphax.service.mb.CompanyService;
import com.alphax.api.mb.CompanyApi;
import com.alphax.common.constants.AlphaXCommonUtils;
import com.alphax.util.DecryptTokenUtils;
import com.alphax.vo.mb.Companys;
import com.alphax.vo.mb.LoginAgencysDTO;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
@Validated
public class CompanyController implements CompanyApi {
	
	@Autowired
	CompanyService companyService;
	
	@Autowired
	AlphaXCommonUtils commonUtils;
	
	@Autowired
	DecryptTokenUtils decryptUtil;
	

	/**
	 * This method is used for get Company List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getAlphaPlusAgencyList(@PathVariable(value = "id", required = true) String id){

		log.info("CompanyController : Start getAlphaPlusAgencyList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<Companys> companyList = companyService.getAlphaPlusAgencyList(decryptUtil.getDataLibrary(), id, decryptUtil.getApAgencyId());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(companyList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used to get login agency List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getAlphaXAgencyList() {
		
		log.info("CompanyController : Start getAlphaXAgencyList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<LoginAgencysDTO> loginAgencyList = companyService.getAlphaXAgencyList(decryptUtil.getDataLibrary(), decryptUtil.getSchema(), decryptUtil.getLogedInUser());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(loginAgencyList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used to create authentication Token
	 */
	@Override
	public ResponseEntity<AlphaXResponse> createAuthenticationToken(@Valid @RequestBody LoginAgencysDTO loginAgency_obj) {
		
		log.info("CompanyController : Start getAlphaXAgencyList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		String loginAgencyList = companyService.createAuthorizationToken(loginAgency_obj, decryptUtil.getDataLibrary(), decryptUtil.getSchema(),
				decryptUtil.getUserId(), decryptUtil.getLogedInUser(), decryptUtil.getName(), decryptUtil.getWsId(), decryptUtil.getSavLibrary(),
				decryptUtil.getPodlib(), decryptUtil.getNosavlib(), decryptUtil.getWsprt(), decryptUtil.getSysprt(), decryptUtil.getPrtkurz(), decryptUtil.getMandent());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(loginAgencyList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
}