package com.alphax.controller.mb;

import java.util.List;
import java.util.Locale;

import javax.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alphax.common.exception.AlphaXException;
import com.alphax.common.exception.ExceptionMessages;
import com.alphax.common.response.AlphaXResponse;
import com.alphax.service.mb.CustomerService;
import com.alphax.api.mb.CustomersApi;
import com.alphax.common.constants.AlphaXCommonUtils;
import com.alphax.util.DecryptTokenUtils;
import com.alphax.util.RestURIConstants;
import com.alphax.vo.mb.AuthorityCheck;
import com.alphax.vo.mb.Customer;
import com.alphax.vo.mb.CustomerDefaultDataVO;
import com.alphax.vo.mb.CustomerDetailVO;
import com.alphax.vo.mb.CustomerOneCheck;
import com.alphax.vo.mb.DropdownObject;
import com.alphax.vo.mb.GlobalSearch;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
@Validated
public class CustomerController implements CustomersApi {

	@Autowired
	CustomerService customerService;

	@Autowired
	AlphaXCommonUtils commonUtils;

	@Autowired
	DecryptTokenUtils decryptUtil;


	/**
	 * This method is used for Create Customer
	 */
	@Override
	public ResponseEntity<AlphaXResponse> createCustomer( @RequestBody Customer customerDtl ) {

		log.info("CustomerController : Start createCustomer.");
		AlphaXResponse<Object> response = new AlphaXResponse<>();
		Customer customeroutput = null;
		boolean isCustomerCreated  = false;
		Locale locale = LocaleContextHolder.getLocale();

		customeroutput = customerService.createOrUpdateCustomer(customerDtl);

		List<String> errorList = customeroutput.getErrorList();
		if (errorList != null && !errorList.isEmpty()) {
			for (String errorsString : errorList) {
				if (errorsString != null) {
					if (errorsString.equalsIgnoreCase("") && !isCustomerCreated) {
						isCustomerCreated = true;
						response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, 
								ExceptionMessages.CUSTOMER_CREATED_MSG_KEY));

					} else if (customeroutput.getErrorCode().startsWith("P")
							|| customeroutput.getScreenTwoErrorCode().startsWith("P")
							|| customeroutput.getScreenThreeErrorCode().startsWith("P")
							|| customeroutput.getScreenFourErrorCode().startsWith("P")
							|| customeroutput.getScreenFiveErrorCode().startsWith("P")
							|| customeroutput.getScreenSixErrorCode().startsWith("P")) {
						response.setResponseMessages(commonUtils.buildAlphaXResponseEntity(
								response.getResponseMessages(), errorsString.substring(0).trim(),
								AlphaXException.POPUP_ERROR_CODE, RestURIConstants.MSG_TYPE_ERROR, locale.getLanguage().toUpperCase()));

					} else if(!errorsString.equalsIgnoreCase("")) {
						response.setResponseMessages(commonUtils.buildAlphaXResponseEntity(
								response.getResponseMessages(), errorsString.substring(4).trim(),
								errorsString.substring(0, 4), RestURIConstants.MSG_TYPE_ERROR, locale.getLanguage().toUpperCase()));
					}
				} 
			}
		}else {
			response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, ExceptionMessages.CUSTOMER_CREATE_FAIL_MSG_KEY));
		}
		response.setResultSet(customeroutput);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	/**
	 * This method is used for Update Customer
	 */
	@Override
	public ResponseEntity<AlphaXResponse> updateCustomer( @RequestBody Customer customerDtl ) {

		log.info("CustomerController : Start updateCustomer.");
		AlphaXResponse<Object> response = new AlphaXResponse<>();
		Customer customeroutput = null;
		boolean isCustomerCreated  = false;
		Locale locale = LocaleContextHolder.getLocale();
		
		customeroutput = customerService.createOrUpdateCustomer(customerDtl);
		
		List<String> errorList = customeroutput.getErrorList();
		if (errorList != null && !errorList.isEmpty()) {
			for (String errorsString : errorList) {
				if (errorsString != null) {
					if (errorsString.equalsIgnoreCase("") && !isCustomerCreated) {
						isCustomerCreated = true;
						response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, 
								ExceptionMessages.CUSTOMER_UPDATE_MSG_KEY));

					} else if (customeroutput.getErrorCode().startsWith("P")
							|| customeroutput.getScreenTwoErrorCode().startsWith("P")
							|| customeroutput.getScreenThreeErrorCode().startsWith("P")
							|| customeroutput.getScreenFourErrorCode().startsWith("P")
							|| customeroutput.getScreenFiveErrorCode().startsWith("P")
							|| customeroutput.getScreenSixErrorCode().startsWith("P")) {
						response.setResponseMessages(commonUtils.buildAlphaXResponseEntity(
								response.getResponseMessages(), errorsString.substring(0).trim(),
								AlphaXException.POPUP_ERROR_CODE, RestURIConstants.MSG_TYPE_ERROR, locale.getLanguage().toUpperCase()));

					} else if(!errorsString.equalsIgnoreCase("")) {
						response.setResponseMessages(commonUtils.buildAlphaXResponseEntity(
								response.getResponseMessages(), errorsString.substring(4).trim(),
								errorsString.substring(0, 4), RestURIConstants.MSG_TYPE_ERROR, locale.getLanguage().toUpperCase()));
					}
				} 
			}
		}else {
			response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, ExceptionMessages.CUSTOMER_UPDATE_FAIL_MSG_KEY));
		}
		response.setResultSet(customeroutput);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	/**
	 * This method is used for get Customer Details usin Id
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getCustomerDetailsByID(
			@PathVariable(value = "id", required = true) int id ) {

		log.info("CustomerController : Start getCustomerDetailsByID");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		CustomerDetailVO customerDtls= customerService.getCustomerDetailsByID(decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), id);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(customerDtls);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used for Search Customer.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getCustomerSearchList( @RequestParam(value = "searchText", required = true) String searchText,
			@RequestParam(value = "pageSize", required = false) String pageSize, 
			@RequestParam(value = "pageNumber", required = false) String pageNumber ) {

		log.info("CustomerController : Start getCustomerSearchList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		GlobalSearch globalSearch = customerService.getCustomerSearchList(decryptUtil.getDataLibrary(),
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), searchText, pageSize, pageNumber);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(globalSearch);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used for get Akquisitionskz DDL values
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getAkquisitionskz() {

		log.info("CustomerController : Start getAkquisitionskz");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<DropdownObject> acquisitionCodeList = customerService.getAkquisitionskz(decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(acquisitionCodeList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used for get BranchenSchlussel DDL values
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getBranchenSchlussel() {

		log.info("CustomerController : Start getBranchenSchlüssel");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<DropdownObject> industrykeyList = customerService.getBranchenSchlussel(decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(industrykeyList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}

	
	/**
	 * This method is used for get AnredeSchlussel DDL values
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getAnredeSchlussel() {

		log.info("CustomerController : Start getAnredeSchlüssel");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<DropdownObject> salutationKey = customerService.getAnredeSchlussel(decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(salutationKey);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}

	
	/**
	 * This method is used for get Customerone Details
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getCustomerOne() {

		log.info("CustomerController : Start getCustomerOne");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		CustomerOneCheck customerOne = customerService.getCustomerOne(decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(customerOne);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used to update edit flag.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> updateEditFlag( 
			@NotBlank @RequestParam(value = "affects", required = true) String affects,
			@NotBlank @RequestParam(value = "flag", required = true) String flag,
			@NotBlank @RequestParam(value = "id", required = true) String id ){

		log.info("CustomerController : Start updateEditFlag");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		log.info("Affects : "+ affects +" Flag : "+flag + " Id :" +id);
		
		AuthorityCheck authorityUpdate = customerService.updateEditFlag(decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), 
				affects, flag, id);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(authorityUpdate);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	

	/**
	 * This method is used for get edit flag by Id
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getEditFlagValueById(
			@NotBlank @RequestParam(value = "affects", required = true) String affects,
			@NotBlank @RequestParam(value = "id", required = true) String id){

		log.info("CustomerController : Start updateEditFlag");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		log.info(" Identifier :" +id);
		
		AuthorityCheck authorityCheck = customerService.getEditFlagValueById(decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), 
				affects, id);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(authorityCheck);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used for get Customer Default data
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getCustomerDefaultData(  
			@RequestParam(value = "customerGroup", required = false) String customerGroup ) {

		log.info("CustomerController : Start getCustomerDefaultData");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		CustomerDefaultDataVO customerDefaultData= customerService.getCustomerDefaultData(decryptUtil.getSchema(), decryptUtil.getApCompanyId(), 
				decryptUtil.getApAgencyId(), customerGroup);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(customerDefaultData);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}

	
	/**
	 * This method is used for get Mehrwertsteuer List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getMehrwertsteuerList() {

		log.info("CustomerController : Start getMehrwertsteuerList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<DropdownObject> mehrwertsteuerList = customerService.getMehrwertsteuerList();

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(mehrwertsteuerList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used for get Skonto List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getSkontoList() {

		log.info("CustomerController : Start getSkontoList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<DropdownObject> skontoList = customerService.getSkontoList();

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(skontoList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used for get ZuAbschlag List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getZuAbschlagList() {

		log.info("CustomerController : Start getZuAbschlagList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<DropdownObject> zuAbschlagList = customerService.getZuAbschlagList();

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(zuAbschlagList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	

	/**
	 * This method is used for get Rabattkz List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getRabattkzList() {

		log.info("CustomerController : Start getRabattkzList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<DropdownObject> rabattkzList = customerService.getRabattkzList();

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(rabattkzList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used for get ElektRechnung List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getElektRechnungList() {

		log.info("CustomerController : Start getElektRechnungList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<DropdownObject> elektRechnungList = customerService.getElektRechnungList();

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(elektRechnungList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used for get XMLDaten List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getXMLDatenList() {

		log.info("CustomerController : Start getXMLDatenList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<DropdownObject> xmlDatenList = customerService.getXMLDatenList();

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(xmlDatenList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	

	/**
	 * This method is used for get Herkunft List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getHerkunftList() {

		log.info("CustomerController : Start getHerkunftList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<DropdownObject> herkunftList = customerService.getHerkunftList();

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(herkunftList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used for get Land List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getLandList() {

		log.info("CustomerController : Start getLandList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<DropdownObject> landList = customerService.getLandList(decryptUtil.getSchema(), decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(landList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used for get Bonitate List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getBonitateList() {

		log.info("CustomerController : Start getBonitateList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<DropdownObject> BonitateList = customerService.getBonitateList(decryptUtil.getSchema(), decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(BonitateList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used for get Drucker List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getDruckerList() {

		log.info("CustomerController : Start getDruckerList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<DropdownObject> druckerList = customerService.getDruckerList(decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(druckerList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used for get AnredeCode List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getAnredeCodeList() {

		log.info("CustomerController : Start getAnredeCodeList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<DropdownObject> anredeList = customerService.getAnredeCodeList();

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(anredeList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}

	
	/**
	 * This method is used for get CustomerVehicles List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getCustomerVehicles( 
			@PathVariable(value = "id", required = true) String id ) {

		log.info("CustomerController : Start getCustomerVehicles");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		GlobalSearch globalSearch = customerService.getCustomerVehicles(decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), id);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(globalSearch);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}

}