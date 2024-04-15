package com.alphax.service.mb.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.alphax.common.rest.message.Message;
import com.alphax.common.rest.message.service.MessageService;
import com.alphax.config.JwtTokenUtil;
import com.alphax.model.mb.AdminModuleSection;
import com.alphax.model.mb.AdminUsers;
import com.alphax.model.mb.AdminWarehouse;
import com.alphax.model.mb.LoginAgencys;
import com.alphax.common.exception.AlphaXException;
import com.alphax.common.exception.ExceptionMessages;
import com.alphax.repository.DBServiceRepository;
import com.alphax.repository.StubServiceRepository;
import com.alphax.service.mb.CompanyService;
import com.alphax.vo.mb.AdminModuleAccessDTO;
import com.alphax.vo.mb.Companys;
import com.alphax.vo.mb.LoginAgencysDTO;
import com.alphax.common.constants.AlphaXTokenConstants;

import lombok.extern.slf4j.Slf4j;

/**
 * @author A106744104
 *
 */
@Service
@Slf4j
public class CompanyServiceImpl extends BaseService implements CompanyService {

	@Autowired
	DBServiceRepository dbServiceRepository;

	@Autowired
	StubServiceRepository stubServiceRepository;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Value("${alphax.datacenter.lib}")
	private String dataCenterLibValue;
	

	@Override
	public List<Companys> getAlphaPlusAgencyList(String datalibrary, String companyId,String agencyId) {
		log.info("Start : getAlphaPlusAgencyList");
		List<Companys> alphaPlusAgencyList = new ArrayList<>();
		
		try {

			validateCompany(companyId);
			companyId = StringUtils.leftPad(companyId, 2, "0");
			
			log.info("companyId : {}", companyId);
			
			StringBuilder query = new StringBuilder("SELECT KEYFLD, DATAFLD FROM ");
			query.append(datalibrary).append(".REFERENZ WHERE KEYFLD LIKE '0000019903").append(companyId).append("%'");
			
			Map<String, String> companyListMap = dbServiceRepository.getResultUsingCobolQuery(query.toString());
			
			if(companyListMap != null && !companyListMap.isEmpty()) {

				for(Map.Entry<String, String> companyValues : companyListMap.entrySet()) {

					Companys companys = new Companys();

					if (companyValues.getKey().length() > 4) {
						String keyFild = companyValues.getKey().trim();
						companys.setCompanyAndAgencyId(keyFild.substring(keyFild.length() - 4));
					}
					companys.setVfnr(companyValues.getValue().substring(0, 6).trim());
					companys.setDealer(companyValues.getValue().substring(6, 36).trim());
					companys.setBerufsbezeichnung(companyValues.getValue().substring(36, 55).trim());
					companys.setStreetNumber(companyValues.getValue().substring(55, 85).trim());
					companys.setPoBox(companyValues.getValue().substring(85, 93).trim());
					companys.setCountry(companyValues.getValue().substring(93, 95).trim());
					companys.setPostalCode(companyValues.getValue().substring(95, 100).trim());
					companys.setCity(companyValues.getValue().substring(101, 131).trim());
					companys.setPartner(companyValues.getValue().substring(131, 134).trim());
					companys.setKennzeichen(companyValues.getValue().substring(134, 135).trim());
					
					if(companys.getCompanyAndAgencyId().length()==4){
						companys.setCompanyId(companys.getCompanyAndAgencyId().substring(0, 2));
						companys.setAgencyId(companys.getCompanyAndAgencyId().substring(2, 4));
					}

					alphaPlusAgencyList.add(companys);
				}
			}
			else {
				log.debug("Alpha Plus Agency List is empty");
				Message message = messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "AlphaPlus Agency");
				AlphaXException exception = new AlphaXException(message);
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "AlphaPlus Agency"), exception);
				throw exception;
			}
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.debug("Error Message:" + e.getMessage());
			Message message = messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "AlphaPlus Agency");
			AlphaXException exception = new AlphaXException(e, message);
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "AlphaPlus Agency"), exception);
			throw exception;
		}
		return alphaPlusAgencyList;
	}
	
	
	@Override
	public List<LoginAgencysDTO> getAlphaXAgencyList(String datalibrary, String schema,String userLogin) {
		log.info("Start : getAlphaXAgencyList");
		List<LoginAgencysDTO> alphaXAgencyList = new ArrayList<>();
		
		try {
			
			StringBuilder query = new StringBuilder("SELECT UR.USERROLE_ID, UR.USER_ID, UR.ROLE_ID, R.NAME AS ROLENAME, UR.AGENCY_ID,UR.ISDEFAULT, UA.NAME as agencyName,");
			query.append(" UA.AP_AGENCY_ID, UA.COMPANY_ID , UC.AP_COMPANY_ID ,  UC.NAME as companyName  FROM ");
			query.append(schema).append(".O_USER U ,").append(schema).append(".O_USERRL UR ,").append(schema).append(".O_AGENCY UA ,").append(schema).append(".O_ROLE R ,");
			query.append(schema).append(".O_COMPANY  UC  where UR.USER_ID = U.USER_ID AND UR.ROLE_ID = R.ROLE_ID AND UA.AGENCY_ID = UR.AGENCY_Id AND ");
			query.append("UC.COMPANY_ID = UA.COMPANY_ID AND   U.LOGIN =").append("'"+userLogin+"'").append(" AND U.isactive = 1 AND U.ISACTIVE = UR.ISACTIVE AND U.ISACTIVE = UA.ISACTIVE AND  U.ISACTIVE = R.ISACTIVE AND  U.ISACTIVE = UC.ISACTIVE ");
			
			List<LoginAgencys> loginAgencyList = dbServiceRepository.getResultUsingQuery(LoginAgencys.class, query.toString(), true);
			
			if(loginAgencyList != null && !loginAgencyList.isEmpty()) {
				
				for(LoginAgencys loginAgencys :loginAgencyList ){
					LoginAgencysDTO loginAgencysDTO = new LoginAgencysDTO();
					
					loginAgencysDTO.setUserRoleId(String.valueOf(loginAgencys.getUserRoleId()));
					loginAgencysDTO.setUserId(String.valueOf(loginAgencys.getUserId()));
					loginAgencysDTO.setRoleId(String.valueOf(loginAgencys.getRoleId()));
					loginAgencysDTO.setRoleName(loginAgencys.getRoleName());
					loginAgencysDTO.setDefaultAgency(loginAgencys.getDefaultAgency().compareTo(new BigDecimal("1"))==0?true:false);
					loginAgencysDTO.setAgencyId(String.valueOf(loginAgencys.getAgencyId()));
					loginAgencysDTO.setAgencyName(loginAgencys.getAgencyName());
					loginAgencysDTO.setAlphaPlusAgencyId(StringUtils.leftPad(loginAgencys.getAlphaPlusAgencyId(),2,"0"));
					loginAgencysDTO.setCompanyId(String.valueOf(loginAgencys.getCompanyId()));
					loginAgencysDTO.setCompanyName(loginAgencys.getCompanyName());
					loginAgencysDTO.setAlphaPlusCompanyId(StringUtils.leftPad(loginAgencys.getAlphaPlusCompanyId(),2,"0"));
					loginAgencysDTO.setChanged(false);
					
					alphaXAgencyList.add(loginAgencysDTO);
					
				}
			}else {
				log.debug("Login agency List is empty");
				Message message = messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Login agency");
				AlphaXException exception = new AlphaXException(message);
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Login agency"), exception);
				throw exception;
			}
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.debug("Error Message:" + e.getMessage());
			Message message = messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Login agency");
			AlphaXException exception = new AlphaXException(e, message);
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Login agency"), exception);
			throw exception;
		}
		return alphaXAgencyList;
	}
	
	
	@Override
	public String createAuthorizationToken(LoginAgencysDTO agency_obj, String dataLibrary, String schema, String userId, String logedInUser, String name, String wsId,
			String savLib, String podLib, String noSavLib, String wsprt, String sysprt, String prtkurz, String mandent) {

		log.info("Inside createAuthorizationToken method of CompanyServiceImpl");
		Connection con = null;
		String token = "";
		Map<String,Object > tokenKeyValueMap = new HashMap<>();
		try {
			if (agency_obj != null && agency_obj.isChanged() != null) {

				if (agency_obj.isChanged()) {

					con = dbServiceRepository.getConnectionObject();
					con.setAutoCommit(false);

					StringBuilder updateAllRecords = new StringBuilder(" UPDATE ").append(schema).append(".O_USERRL ");
					updateAllRecords.append(" SET ISDEFAULT = 0  WHERE USER_ID = ").append(agency_obj.getUserId());

					dbServiceRepository.updateResultUsingQuery(updateAllRecords.toString(), con);

					StringBuilder updateDefaultAgency = new StringBuilder(" UPDATE ").append(schema).append(".O_USERRL ");
					updateDefaultAgency.append(" SET ISDEFAULT = 1  WHERE USER_ID = ").append(agency_obj.getUserId());
					updateDefaultAgency.append(" AND USERROLE_ID = ").append(agency_obj.getUserRoleId());

					dbServiceRepository.updateResultUsingQuery(updateDefaultAgency.toString(), con);

					con.commit();
					con.setAutoCommit(true);
				}
				
				String allowed_AP_warehouse = getAllowedAPWarehouse(schema , agency_obj.getAgencyId());
				if(!allowed_AP_warehouse.isEmpty()){
					allowed_AP_warehouse = StringUtils.chop(allowed_AP_warehouse);
				}
				
				String et_central_function = getCentralFunctionValue(schema , agency_obj.getUserId());
				
				String company_All_AP_warehouse = getCompanyAPWarehouse(schema , agency_obj.getCompanyId());
				if(!company_All_AP_warehouse.isEmpty()){
					company_All_AP_warehouse = StringUtils.chop(company_All_AP_warehouse);
				}
				
				List<AdminModuleAccessDTO> moduleAccessJson = getModuleAccessJson(schema , agency_obj.getUserId(), agency_obj.getRoleId());
				
				boolean licenseCheckFlag = checkLicenseForPartMovement(dataLibrary);
				
				tokenKeyValueMap.put(AlphaXTokenConstants.USERID, userId);	
				tokenKeyValueMap.put(AlphaXTokenConstants.LOGIN, logedInUser);	
				tokenKeyValueMap.put(AlphaXTokenConstants.USERNAME, name);
				tokenKeyValueMap.put(AlphaXTokenConstants.SCHEMA, schema);
				tokenKeyValueMap.put(AlphaXTokenConstants.DATALIB, dataLibrary);
				tokenKeyValueMap.put(AlphaXTokenConstants.WSID, wsId);
				tokenKeyValueMap.put(AlphaXTokenConstants.SAVLIB, savLib);
				tokenKeyValueMap.put(AlphaXTokenConstants.PODLIB, podLib);
				tokenKeyValueMap.put(AlphaXTokenConstants.NOSAVLIB, noSavLib);
				tokenKeyValueMap.put(AlphaXTokenConstants.WSPRT, wsprt);
				tokenKeyValueMap.put(AlphaXTokenConstants.SYSPRT, sysprt);
				tokenKeyValueMap.put(AlphaXTokenConstants.PRTKURZ, prtkurz);
				tokenKeyValueMap.put(AlphaXTokenConstants.AX_COMPANY_NAME, agency_obj.getCompanyName());
				tokenKeyValueMap.put(AlphaXTokenConstants.AX_COMPANYID, agency_obj.getCompanyId());
				tokenKeyValueMap.put(AlphaXTokenConstants.AP_COMPANY, agency_obj.getAlphaPlusCompanyId());
				tokenKeyValueMap.put(AlphaXTokenConstants.AX_AGENCY_NAME, agency_obj.getAgencyName());
				tokenKeyValueMap.put(AlphaXTokenConstants.AX_AGENCYID, agency_obj.getAgencyId());
				tokenKeyValueMap.put(AlphaXTokenConstants.AP_AGENCY, agency_obj.getAlphaPlusAgencyId());
				tokenKeyValueMap.put(AlphaXTokenConstants.ROLEID, agency_obj.getRoleId());
				tokenKeyValueMap.put(AlphaXTokenConstants.ROLENAME, agency_obj.getRoleName());
				tokenKeyValueMap.put(AlphaXTokenConstants.ALLOWED_AGENCY, agency_obj.getAlphaPlusAgencyId());
				tokenKeyValueMap.put(AlphaXTokenConstants.ALLOWED_WAREHOUSE, allowed_AP_warehouse);
				tokenKeyValueMap.put(AlphaXTokenConstants.MODULE_ACCESS, moduleAccessJson);
				tokenKeyValueMap.put(AlphaXTokenConstants.ET_CENTRAL_FUNCTION, et_central_function);
				tokenKeyValueMap.put(AlphaXTokenConstants.COMPANY_ALL_AP_WAREHOUSE, company_All_AP_warehouse);
				tokenKeyValueMap.put(AlphaXTokenConstants.MANDANT, mandent);
				tokenKeyValueMap.put(AlphaXTokenConstants.AX_DATACENTER_LIB, dataCenterLibValue);
				tokenKeyValueMap.put(AlphaXTokenConstants.LICENSE_CHECK_FOR_PART_MOVEMENT, licenseCheckFlag);
				
				token = jwtTokenUtil.generateAuthorizationToken(tokenKeyValueMap);
				
			}
			
		} catch (AlphaXException ex) {
			try {
				if (con != null) {
					con.rollback();
				}
			} catch (SQLException e1) {
				throw ex;
			}
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(
					LocaleContextHolder.getLocale(), ExceptionMessages.CREATE_FAILED_MSG_KEY, "Authorization Token"));
			log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY, "Authorization Token"),
					exception);
			try {
				if (con != null) {
					con.rollback();
				}
			} catch (SQLException e1) {
				throw exception;
			}
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		
		return token;
	}
	

	private String getCompanyAPWarehouse(String schema, String axCompanyId) {
		
		StringBuilder warehouseList = new StringBuilder(" SELECT DISTINCT AW.WAREHOUS_ID , W.AP_WAREHOUS_ID FROM ").append(schema).append(".o_agnwrh AW , ");
		warehouseList.append(schema).append(".o_wrh W, ").append(schema).append(".o_agency A WHERE  AW.WAREHOUS_ID = W.WAREHOUS_ID  AND W.ISACTIVE = 1 AND W.ISACTIVE = AW.ISACTIVE AND ");
		warehouseList.append(" AW.AGENCY_ID = A.AGENCY_ID AND A.COMPANY_ID = ").append(axCompanyId);
		
		List<AdminWarehouse> adminWarehouseList = dbServiceRepository.getResultUsingQuery(AdminWarehouse.class, warehouseList.toString(), true);
		String allowed_AP_warehouse = "";
		
		if (adminWarehouseList != null && !adminWarehouseList.isEmpty()) {

			for(AdminWarehouse adminWarehouse : adminWarehouseList){
				allowed_AP_warehouse = allowed_AP_warehouse + StringUtils.leftPad(adminWarehouse.getAlphaPlusWarehouseId(),2,"0") + ",";
			}
		}
		
		return allowed_AP_warehouse;
	}
	

	private String getCentralFunctionValue(String schema, String userId) {
		
		StringBuilder query = new StringBuilder("SELECT VALUE FROM ").append(schema).append(".O_SETUP ");
		query.append(" WHERE COMPANY_ID=0 AND AGENCY_ID=0 AND WAREHOUS_ID=0 AND ROLE_ID=0 AND PRINTER_ID=0 ");
		query.append(" AND KEY = 'ET_CENTRAL_FUNCTION' AND USER_ID = ").append(userId).append(" fetch first row only ");
		
		List<AdminUsers> adminUserList = dbServiceRepository.getResultUsingQuery(AdminUsers.class, query.toString(), true);
		String tokenValue = "0";
		if (adminUserList != null && !adminUserList.isEmpty()) {
			for (AdminUsers adminUsers : adminUserList) {
				if (adminUsers.getCentralFunctionFlag() != null && !adminUsers.getCentralFunctionFlag().isEmpty()) {
					tokenValue = adminUsers.getCentralFunctionFlag();
				}
			}
		}
		return tokenValue;
	}
	

	private String getAllowedAPWarehouse(String schema, String agencyId) {
		
		StringBuilder warehouseList = new StringBuilder(" SELECT DISTINCT AW.WAREHOUS_ID , W.AP_WAREHOUS_ID FROM ").append(schema).append(".o_agnwrh AW , ");
		warehouseList.append(schema).append(".o_wrh W WHERE  AW.WAREHOUS_ID = W.WAREHOUS_ID  AND W.ISACTIVE = 1 AND W.ISACTIVE =   AW.ISACTIVE AND ");
		warehouseList.append(" AW.AGENCY_ID = ").append(agencyId);
		
		List<AdminWarehouse> adminWarehouseList = dbServiceRepository.getResultUsingQuery(AdminWarehouse.class, warehouseList.toString(), true);
		String allowed_AP_warehouse = "";
		
		if (adminWarehouseList != null && !adminWarehouseList.isEmpty()) {

			for(AdminWarehouse adminWarehouse : adminWarehouseList){
				allowed_AP_warehouse = allowed_AP_warehouse + StringUtils.leftPad(adminWarehouse.getAlphaPlusWarehouseId(),2,"0") + ",";
			}
		}
		
		return allowed_AP_warehouse;
	}
	
	
	private List<AdminModuleAccessDTO> getModuleAccessJson(String schema, String userId, String roleId) throws Exception {

		StringBuilder moduleSectionQuery = new StringBuilder("SELECT MOD.MODULE_ID, MOD.MODULE_NAME, MSEC.MODSEC_ID, MSEC.SECTION_NAME, MSEC.KEY, ");
		moduleSectionQuery.append(" (SELECT OSET.VALUE FROM ").append(schema).append(".O_SETUP OSET WHERE MSEC.KEY = OSET.KEY AND OSET.ROLE_ID = ").append(roleId);
		moduleSectionQuery.append(" ) AS VALUE , MOD.ISACTIVE FROM ").append(schema).append(".O_MODULE MOD, ").append(schema).append(".O_MODSEC MSEC ,");
		moduleSectionQuery.append(schema).append(".O_USERRL UR   WHERE  MOD.MODULE_ID = MSEC.MODULE_ID AND MOD.ISACTIVE = 1 AND ");
		moduleSectionQuery.append(" UR.USER_ID = ").append(userId).append(" AND UR.ROLE_ID = ").append(roleId);
		
		List<AdminModuleSection> moduleAccessList = dbServiceRepository.getResultUsingQuery(AdminModuleSection.class, moduleSectionQuery.toString(), true);
		List<AdminModuleAccessDTO> moduleAccessDTOs = new ArrayList<>();
		Map<String,String> moduleSection;
		
		if (moduleAccessList != null && !moduleAccessList.isEmpty()) {
			
			//get all active module list
			 StringBuilder moduleListQuery = new StringBuilder("SELECT  DISTINCT MODULE_NAME FROM ").append(schema).append(".O_MODULE WHERE ISACTIVE = 1 ");
			 List<AdminModuleSection> activeModuleList = dbServiceRepository.getResultUsingQuery(AdminModuleSection.class, moduleListQuery.toString(), true);

			for (AdminModuleSection moduleList : activeModuleList) {

				AdminModuleAccessDTO settingsDTO = new AdminModuleAccessDTO();
				moduleSection = new HashMap<>();
				boolean isModuleAvailable = false;
				boolean checkAllValueIsNull = true;

				for (AdminModuleSection mod_Sections : moduleAccessList) {
					if (moduleList.getModuleName().equalsIgnoreCase(mod_Sections.getModuleName())) {
						isModuleAvailable = true;
						settingsDTO.setModuleName(mod_Sections.getModuleName());
						settingsDTO.setIsActive(String.valueOf(mod_Sections.getActive()));
						moduleSection.put(mod_Sections.getKey(), mod_Sections.getValue()==null?"0":mod_Sections.getValue());

						if (mod_Sections.getValue() != null) {
							checkAllValueIsNull = false;
						}
					}
				}
				if (isModuleAvailable) {
					if(!checkAllValueIsNull){
						settingsDTO.setModuleSection(moduleSection);
					}
					moduleAccessDTOs.add(settingsDTO);
				}
			}
		}		
		return moduleAccessDTOs;
	}
	
	
	private boolean checkLicenseForPartMovement(String dataLibrary) {

		boolean checkValue = false;
		StringBuilder query = new StringBuilder("SELECT KEYFLD, DATAFLD FROM ");
		query.append(dataLibrary).append(".REFERENZ WHERE TRIM(KEYFLD) LIKE '%2229").append("' ");

		Map<String, String> referenzListMap = dbServiceRepository.getResultUsingCobolQuery(query.toString());

		if (referenzListMap != null && !referenzListMap.isEmpty()) {

			for (Map.Entry<String, String> values : referenzListMap.entrySet()) {

				if (values.getValue() != null && values.getValue().length() >= 11) {
					if(values.getValue().substring(10,11).equalsIgnoreCase("1")) {
						checkValue = true;
					}
				}
			}
		}
		return checkValue;
	}
}


