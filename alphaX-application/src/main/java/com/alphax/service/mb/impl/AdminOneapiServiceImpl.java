package com.alphax.service.mb.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.alphax.common.rest.message.service.MessageService;
import com.alphax.model.mb.AdminApiurl;
import com.alphax.model.mb.AdminOneapi;
import com.alphax.common.exception.AlphaXException;
import com.alphax.common.exception.ExceptionMessages;
import com.alphax.repository.DBServiceRepository;
import com.alphax.service.mb.AdminOneapiService;
import com.alphax.vo.mb.AdminApiurlDTO;
import com.alphax.vo.mb.AdminOneapiDTO;
import com.alphax.vo.mb.DropdownObject;
import com.alphax.vo.mb.GlobalSearch;
import com.alphax.common.constants.RestInputConstants;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AdminOneapiServiceImpl extends BaseService implements AdminOneapiService  {

	@Autowired
	DBServiceRepository dbServiceRepository;

	@Autowired
	private MessageService messageService;


	/**
	 * This method is used to get user Details from DB
	 * @return Object
	 */	
	@Override
	public AdminOneapiDTO getOneapiCustomerDetails(String schema, String dataLibrary, String customerId, String apiKey) {

		log.info("Inside getOneapiCustomerDetails method of OneApiServiceImpl");

		AdminOneapiDTO adminOneapiDTO = new AdminOneapiDTO();

		try {

			StringBuilder query = new StringBuilder("select Id, GEMS_USER, DECRYPT_CHAR(GEMS_PASS,'ONEAPI') as GEMS_PASS, COBOL_USER, COBOL_PASS, ISACTIVE ");

			if(apiKey.equalsIgnoreCase("EVOBUSGET")) {
				query.append(", DECRYPT_CHAR(RSAPVTKEY, 'ONEAPI') as RSAPVTKEY  ");
			}
			query.append("from ").append(schema).append(".O_ONEAPI ");
			query.append(" WHERE CUSTOMER = ").append(customerId).append(" AND API= '").append(apiKey).append("' ");

			List<AdminOneapi> adminOneapiList = dbServiceRepository.getResultUsingQuery(AdminOneapi.class, query.toString(), true);

			if (adminOneapiList != null && !adminOneapiList.isEmpty()) {

				for(AdminOneapi adminOneapi : adminOneapiList){

					adminOneapiDTO.setOneapiId(String.valueOf(adminOneapi.getOneapiId()));
					adminOneapiDTO.setGemsUser(adminOneapi.getGemsUser());
					adminOneapiDTO.setGemsPassword(adminOneapi.getGemsPassword());
					adminOneapiDTO.setActive(adminOneapi.getIsactive().compareTo(new BigDecimal("1")) == 0 ? true : false);
					adminOneapiDTO.setRsaPrivateKey(adminOneapi.getRsaPrivateKey() != null ? adminOneapi.getRsaPrivateKey() : "");
				}
			}else{
				log.info("OneAPI customer details is empty for customet: {} , apiKey: {}",customerId, apiKey  );

				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "Customer Details"));
				log.error(messageService.getReadableMessage(ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "Customer Details"));
				throw exception;
			} 

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "OneAPI Customer Details"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"OneAPI Customer Details"), exception);
			throw exception;
		}

		return adminOneapiDTO;
	}


	/**
	 * This method is used to get user Details from DB
	 * @return Object
	 */	
	@Override
	public GlobalSearch getOneapiList(String schema, String dataLibrary, String pageSize, String pageNumber, Boolean isEVOBus ) {

		log.info("Inside getOneapiCustomerDetails method of OneApiServiceImpl");

		List<AdminOneapiDTO> adminOneapiListDTO = new ArrayList<>();
		GlobalSearch globalSearchList = new GlobalSearch();

		try {

			if(pageSize==null || pageNumber==null || pageSize.isEmpty() || pageNumber.isEmpty()){
				pageSize = RestInputConstants.DEFAULT_PAGE_SIZE;
				pageNumber = RestInputConstants.DEFAULT_PAGE_NO;
			}

			int totalRecords = Integer.parseInt(pageSize);
			int nextRows = totalRecords * (Integer.parseInt(pageNumber) - 1);

			//validate the page size
			validatePageSize(totalRecords);

			log.info("PageSize  : {}  OFFSET Size (Skip Rows):  {} Page Number  : {} ", pageSize, nextRows, pageNumber);
			
			String queryclause = "API <> 'EVOBUSGET'";		
			if(isEVOBus) {
				queryclause = "API = 'EVOBUSGET'";
			}

			StringBuilder query = new StringBuilder("SELECT OA.API , OA.CUSTOMER , OA.ISACTIVE, (SELECT DESCRIPTION FROM  ");
			query.append(schema).append(".O_APIURL WHERE  API = OA.API AND URL = OA.URL )  AS DESCRIPTION , (SELECT COUNT(*) FROM ");
			query.append(schema).append(".O_ONEAPI where ").append(queryclause);	
			query.append(" )  AS ROWNUMER  FROM ");
			query.append(schema).append(".O_ONEAPI OA  where ").append(queryclause);
			query.append(" OFFSET ").append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");

			List<AdminOneapi> adminOneapiList = dbServiceRepository.getResultUsingQuery(AdminOneapi.class, query.toString(), true);

			if (adminOneapiList != null && !adminOneapiList.isEmpty()) {

				for(AdminOneapi adminOneapi : adminOneapiList){
					AdminOneapiDTO adminOneapiDTO = new AdminOneapiDTO();
					adminOneapiDTO.setCustomerId(String.valueOf(adminOneapi.getCustomerId()));
					adminOneapiDTO.setApi(adminOneapi.getApi());
					adminOneapiDTO.setActive(adminOneapi.getIsactive().compareTo(new BigDecimal("1")) ==0 ? true : false);
					adminOneapiDTO.setDescription(adminOneapi.getDescription());

					adminOneapiListDTO.add(adminOneapiDTO);
				}

				globalSearchList.setSearchDetailsList(adminOneapiListDTO);
				globalSearchList.setTotalPages(Integer.toString(adminOneapiList.get(0).getTotalCount()));
				globalSearchList.setTotalRecordCnt(Integer.toString(adminOneapiList.get(0).getTotalCount()));

			}else{
				log.info("OneAPI list is empty");

				globalSearchList.setSearchDetailsList(adminOneapiListDTO);
				globalSearchList.setTotalPages(Integer.toString(0));
				globalSearchList.setTotalRecordCnt(Integer.toString(0));
			} 

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "OneAPI List"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"OneAPI List"), exception);
			throw exception;
		}

		return globalSearchList;
	}

	/**
	 * This method is used to create new OneAPI detail in DB - O_ONEAPI
	 */
	@Override
	public AdminOneapiDTO createOneapi(AdminOneapiDTO adminOneapi_obj, String schema, String dataLibrary, String loginUserName, String mandent) {

		log.info("Inside createOneapi method of OneApiServiceImpl");

		try {

			StringBuilder checkOneApiEntry = new StringBuilder("SELECT COUNT(*) AS COUNT FROM ").append(schema).append(".O_ONEAPI ");
			checkOneApiEntry.append(" WHERE UPPER(API) = ").append("UPPER('"+adminOneapi_obj.getApi()+"') AND CUSTOMER = ").append(adminOneapi_obj.getCustomerId());

			String count = dbServiceRepository.getCountUsingQuery(checkOneApiEntry.toString());
			if(Integer.parseInt(count) > 0){
				log.info("Duplicate APIKey and VF Number");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.UNIQUE_CONSTRAINT_FAILED_MSG_KEY, "APIKey and VF Number"));
				log.error(messageService.getReadableMessage(ExceptionMessages.UNIQUE_CONSTRAINT_FAILED_MSG_KEY, "APIKey and VF Number"));
				throw exception;

			}
			
			String username = "APLU_OAPI"; 
			String password = "OAPIAPLU";

			if (mandent != null && !mandent.isEmpty()) {
				username = mandent + "_OAPI";			
			}
			
			String updatedURL = Stream.of(adminOneapi_obj.getUrl()).map(str -> str.replace("?????", StringUtils.leftPad(adminOneapi_obj.getCustomerId(), 5, "0")))
					.findFirst().get();	

			StringBuilder query = new StringBuilder(" INSERT INTO ").append(schema).append(".O_ONEAPI");
			query.append(" (API, GEMS_USER, GEMS_PASS, CUSTOMER, URL, ISACTIVE, UPDATED_BY, AUTH, COBOL_USER, COBOL_PASS )  values ( '");
			query.append(adminOneapi_obj.getApi()).append("' , '");
			query.append(adminOneapi_obj.getGemsUser()).append("', ");
			query.append("encrypt('").append(adminOneapi_obj.getGemsPassword()).append("' ,'ONEAPI') , ");
			query.append(adminOneapi_obj.getCustomerId()).append(", '");
			query.append(updatedURL).append("' ,");
			query.append(adminOneapi_obj.isActive() ? 1 : 0).append(", '");
			query.append(loginUserName).append("' , '");
			query.append(adminOneapi_obj.getAuth()).append("' , '");
			query.append(username).append("' ,");
			query.append(" encrypt('" + password + "','ONEAPI'))");

			int oneapiId = dbServiceRepository.insertResultUsingQuery(query.toString());

			if (oneapiId == 0) {
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.CREATE_FAILED_MSG_KEY, "Oneapi"));
				log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY, "Oneapi"), exception);
				throw exception;
			}

			adminOneapi_obj.setOneapiId(String.valueOf(oneapiId));

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.CREATE_FAILED_MSG_KEY, "Oneapi"));
			log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY,
					"Oneapi"), exception);
			throw exception;
		}

		return adminOneapi_obj;
	}


	/**
	 * This method is used to get the alphaPlus VF-Number (Customer Number) List from DB
	 * @return List
	 */
	@Override
	public List<DropdownObject> getVFNumber(String schema, String dataLibrary, Boolean isEVOBus) {

		log.info("Inside getVFNumber method of OneApiServiceImpl");
		List<DropdownObject> alphaPlusVFNumber = new ArrayList<>();

		try {

			StringBuilder query = new StringBuilder("select distinct  customer  as  VF_Nummer from ").append(schema).append(".O_ONEAPI where ");
			if(isEVOBus) {
				query.append(" API='EVOBUSGET'");
			}
			else {
				query.append(" API <> 'EVOBUSGET'");
			}
			query.append(" ORDER BY customer ");

			List<AdminOneapi> vfNumberList = dbServiceRepository.getResultUsingQuery(AdminOneapi.class, query.toString(), true);

			if (vfNumberList != null && !vfNumberList.isEmpty()) {

				for(AdminOneapi vfNumber : vfNumberList){
					DropdownObject dropdownObject = new DropdownObject();
					dropdownObject.setKey(vfNumber.getVfNumber());
					dropdownObject.setValue(vfNumber.getVfNumber());

					alphaPlusVFNumber.add(dropdownObject);
				}
			}else{
				log.info("VF Number List is empty");
			} 

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "VF-Number (Customer Number)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"VF-Number (Customer Number)"), exception);
			throw exception;
		}

		return alphaPlusVFNumber;
	}


	/**
	 * This method is used to delete the Customer (VF-Number) from DB
	 * @return Object
	 */
	@Override
	public Map<String, Boolean> deleteOneapiCustomerDetails(String schema, String dataLibrary, String customerNo) {

		log.info("Inside deleteCustomerDetails method of OneApiServiceImpl");
		Map<String, Boolean> programOutput = new HashMap<String, Boolean>();

		try(Connection con = dbServiceRepository.getConnectionObject()){

			con.setAutoCommit(false);

			StringBuilder query = new StringBuilder(" DELETE FROM ").append(schema).append(".O_ONEAPI");
			query.append(" WHERE CUSTOMER = ").append(customerNo);

			boolean updateFlag = dbServiceRepository.deleteResultUsingQuery(query.toString(),con);

			if (!updateFlag) {
				log.info("Unable to delete this customer details for this VF-Number (Customer No) : {} ", customerNo);
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(
						LocaleContextHolder.getLocale(), ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "VF-Number"));
				log.error(messageService.getReadableMessage(ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "VF-Number"));
				throw exception;
			}

			con.commit();
			con.setAutoCommit(true);
			programOutput.put("isDeleted", true);

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.DELETE_ADMIN_SETTING_FAILED_MSG_KEY, "VF-Number"));
			log.error(messageService.getReadableMessage(ExceptionMessages.DELETE_ADMIN_SETTING_FAILED_MSG_KEY,
					"VF-Number"), exception);
			throw exception;
		}

		return programOutput;
	}


	/**
	 * This method is used to delete the Configuration details by VF-Number and API key from DB
	 * @return Object
	 */
	@Override
	public Map<String, Boolean> deleteOneapiConfigurationDetails(String schema, String dataLibrary, String customerNo, String apiKey) {

		log.info("Inside deleteOneapiConfigurationDetails method of OneApiServiceImpl");
		Map<String, Boolean> programOutput = new HashMap<String, Boolean>();

		try(Connection con = dbServiceRepository.getConnectionObject()){

			con.setAutoCommit(false);

			StringBuilder query = new StringBuilder(" DELETE FROM ").append(schema).append(".O_ONEAPI");
			query.append(" WHERE CUSTOMER = ").append(customerNo).append(" AND API= '").append(apiKey).append("' ");

			boolean updateFlag = dbServiceRepository.deleteResultUsingQuery(query.toString(),con);

			if (!updateFlag) {
				log.info("Unable to delete this Oneapi Configuration details for this VF-Number : {} , API key: {} ", customerNo , apiKey);
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(
						LocaleContextHolder.getLocale(), ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "Oneapi Configuration "));
				log.error(messageService.getReadableMessage(ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "Oneapi Configuration"));
				throw exception;
			}

			con.commit();
			con.setAutoCommit(true);
			programOutput.put("isDeleted", true);

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.DELETE_ADMIN_SETTING_FAILED_MSG_KEY, "Oneapi Configuration"));
			log.error(messageService.getReadableMessage(ExceptionMessages.DELETE_ADMIN_SETTING_FAILED_MSG_KEY,
					"Oneapi Configuration"), exception);
			throw exception;
		}

		return programOutput;
	}


	/**
	 * This method is used to get ApiURL List
	 * @return Object
	 */	
	@Override
	public List<AdminApiurlDTO> getApiurlList(String schema, String dataLibrary, Boolean isEVOBus ) {

		log.info("Inside getApiurlList method of OneApiServiceImpl");

		List<AdminApiurlDTO> adminApiurlListDTO = new ArrayList<>();

		String queryclause = "API <> 'EVOBUSGET'";	
		if(isEVOBus) {
			queryclause = "API = 'EVOBUSGET'";
		}

		try {
			StringBuilder query = new StringBuilder("select API , URL, DESCRIPTION, ISACTIVE, AUTH  from ").append(schema).append(".O_APIURL ");
			query.append(" WHERE ISACTIVE = 1 and ").append(queryclause);

			List<AdminApiurl> adminApiurlList = dbServiceRepository.getResultUsingQuery(AdminApiurl.class, query.toString(), true);

			if (adminApiurlList != null && !adminApiurlList.isEmpty()) {

				for(AdminApiurl adminApiurl : adminApiurlList){
					AdminApiurlDTO adminApiurlDTO = new AdminApiurlDTO();
					adminApiurlDTO.setApi(String.valueOf(adminApiurl.getApi()));
					adminApiurlDTO.setUrl(adminApiurl.getUrl());
					adminApiurlDTO.setDescription(adminApiurl.getDescription());
					adminApiurlDTO.setActive(adminApiurl.getIsactive().compareTo(new BigDecimal("1"))==0?true:false);
					adminApiurlDTO.setAuth(adminApiurl.getAuth());

					adminApiurlListDTO.add(adminApiurlDTO);
				}
			}else{
				log.info("ApiURL list is empty");
			} 

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "ApiURL List"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"ApiURL List"), exception);
			throw exception;
		}

		return adminApiurlListDTO;
	}


	/**
	 * This method is used to update OneAPI Details
	 * @return Object
	 */	
	@Override
	public AdminOneapiDTO updateOneapi(AdminOneapiDTO adminOneapi_obj, String schema, String dataLibrary, String loginUserName) {

		log.info("Inside updateOneapi method of OneApiServiceImpl");

		try {

			StringBuilder query = new StringBuilder("UPDATE ").append(schema).append(".O_ONEAPI ");
			query.append(" SET GEMS_USER =  ").append("'" + adminOneapi_obj.getGemsUser() + "'");

			if(!adminOneapi_obj.getApi().equalsIgnoreCase("EVOBUSGET")) {
				query.append(", GEMS_PASS = ").append(" encrypt('").append(adminOneapi_obj.getGemsPassword()).append("','ONEAPI') ");
			}
			else {				
				query.append(", RSAPVTKEY = ").append(" encrypt('").append(adminOneapi_obj.getRsaPrivateKey()).append("','ONEAPI') ");
			}
			query.append(", UPDATED_BY = ").append("'" + loginUserName + "'");
			query.append(", ISACTIVE = ").append(adminOneapi_obj.isActive() ? 1 : 0);
			query.append(" WHERE ID = ").append(adminOneapi_obj.getOneapiId());
			query.append(" AND API = ").append("'" + adminOneapi_obj.getApi() + "'");
			query.append(" AND CUSTOMER = ").append("'" + adminOneapi_obj.getCustomerId() + "'");

			boolean updateFlag = dbServiceRepository.updateResultUsingQuery(query.toString());

			if (!updateFlag) {
				log.info("Unable to update OneAPI details for Id :"
						+ adminOneapi_obj.getOneapiId());
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Oneapi"));
				log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Oneapi"), exception);
				throw exception;
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Oneapi"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY,
					"Oneapi"), exception);
			throw exception;
		}

		return adminOneapi_obj;
	}


	/**
	 * This method is used to create new EVOBUS detail in DB - O_ONEAPI
	 */
	@Override
	public AdminOneapiDTO createEVOBusApi(AdminOneapiDTO adminOneapi_obj, String schema, String dataLibrary, String loginUserName, String mandent) {

		log.info("Inside createEVOBusApi method of OneApiServiceImpl");

		try {

			StringBuilder checkEVOBusEntry = new StringBuilder("SELECT COUNT(*) AS COUNT FROM ").append(schema).append(".O_ONEAPI ");
			checkEVOBusEntry.append(" WHERE UPPER(API) = ").append("UPPER('"+adminOneapi_obj.getApi()+"') AND CUSTOMER = ").append(adminOneapi_obj.getCustomerId());

			String count = dbServiceRepository.getCountUsingQuery(checkEVOBusEntry.toString());

			if(Integer.parseInt(count) > 0){
				log.info("Duplicate API Key and VF Number");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.UNIQUE_CONSTRAINT_FAILED_MSG_KEY, "APIKey and VF Number"));
				log.error(messageService.getReadableMessage(ExceptionMessages.UNIQUE_CONSTRAINT_FAILED_MSG_KEY, "APIKey and VF Number"));
				throw exception;				
			}

			String cbl_Username = "APLU_OAPI";
			String cbl_Password = "OAPIAPLU";

			if (mandent != null && !mandent.isEmpty()) {
				cbl_Username = mandent + "_OAPI";
			}

			StringBuilder query = new StringBuilder(" INSERT INTO ").append(schema).append(".O_ONEAPI");
			query.append(" (API, GEMS_USER, GEMS_PASS, CUSTOMER, URL, ISACTIVE, UPDATED_BY, AUTH, COBOL_USER, COBOL_PASS, RSAPVTKEY )  values ( '");
			query.append(adminOneapi_obj.getApi()).append("' , '");
			query.append(adminOneapi_obj.getGemsUser()).append("', ");  //GEMS_User name
			query.append("encrypt('-' ,'ONEAPI') , ");   //GEMS_PASS
			query.append(adminOneapi_obj.getCustomerId()).append(", '");   //CUSTOMER
			query.append("164.44.71.11").append("' ,");     //URL
			query.append(adminOneapi_obj.isActive() ? 1 : 0).append(", '");
			query.append(loginUserName).append("' , '");
			query.append("RSA_PRIVATE_STRING' , '");  //AUTH
			query.append(cbl_Username).append("' ,");  //COBOL_USER
			query.append(" encrypt('" + cbl_Password + "','ONEAPI'), "); //COBOL_PASS
			query.append(" encrypt('" + adminOneapi_obj.getRsaPrivateKey() + "', 'ONEAPI'))"); // RSAPVTKEY

			int oneapiId = dbServiceRepository.insertResultUsingQuery(query.toString());

			if (oneapiId == 0) {
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.CREATE_FAILED_MSG_KEY, "EVOBUSAPI"));
				log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY, "EVOBUSAPI"), exception);
				throw exception;
			}

			adminOneapi_obj.setOneapiId(String.valueOf(oneapiId));

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.CREATE_FAILED_MSG_KEY, "EVOBUSAPI"));
			log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY,
					"EVOBUSAPI"), exception);
			throw exception;
		}

		return adminOneapi_obj;
	}


}