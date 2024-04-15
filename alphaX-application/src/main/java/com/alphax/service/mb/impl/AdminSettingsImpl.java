package com.alphax.service.mb.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.alphax.common.rest.message.service.MessageService;
import com.alphax.model.mb.AdminAgency;
import com.alphax.model.mb.AdminAgencyWRHSMapping;
import com.alphax.model.mb.AdminCompanys;
import com.alphax.model.mb.AdminModule;
import com.alphax.model.mb.AdminModuleSection;
import com.alphax.model.mb.AdminRoles;
import com.alphax.model.mb.AdminUsers;
import com.alphax.model.mb.AdminWarehouse;
import com.alphax.model.mb.Admin_Mapping;
import com.alphax.model.mb.AlphaXConfigurationKeysDetails;
import com.alphax.model.mb.InventoryLagerDetails;
import com.alphax.model.mb.LagerDetails;
import com.alphax.model.mb.LoginAgencys;
import com.alphax.common.exception.AlphaXException;
import com.alphax.common.exception.ExceptionMessages;
import com.alphax.repository.DBServiceRepository;
import com.alphax.repository.StubServiceRepository;
import com.alphax.service.mb.AdminSettings;
import com.alphax.vo.mb.AdminAgencyDTO;
import com.alphax.vo.mb.AdminCompanysDTO;
import com.alphax.vo.mb.AdminLoginUsersDTO;
import com.alphax.vo.mb.AdminModuleDTO;
import com.alphax.vo.mb.AdminRoleSettingsDTO;
import com.alphax.vo.mb.AdminRolesDTO;
import com.alphax.vo.mb.AdminUserSettingDTO;
import com.alphax.vo.mb.AdminUsersDTO;
import com.alphax.vo.mb.AdminWarehouseDTO;
import com.alphax.vo.mb.Admin_AgencyWarehouseMappingDTO;
import com.alphax.vo.mb.Admin_UserRoleMappingDTO;
import com.alphax.vo.mb.DropdownObject;
import com.alphax.vo.mb.EmployeeRoleDTO;
import com.alphax.vo.mb.GlobalSearch;
import com.alphax.vo.mb.LagerDetailsDTO;
import com.alphax.common.constants.AlphaXCommonUtils;
import com.alphax.common.constants.RestInputConstants;

import lombok.extern.slf4j.Slf4j;

/**
 * @author A87740971
 *
 */
@Service
@Slf4j
public class AdminSettingsImpl extends BaseService implements AdminSettings {

	@Autowired
	DBServiceRepository dbServiceRepository;

	@Autowired
	private MessageService messageService;
	
	@Autowired
	StubServiceRepository stubServiceRepository;
	
	@Autowired
	AlphaXCommonUtils commonUtils;
	
	/**
	 * This method is used to get the FIRMA  (Company) List from DB
	 * @return List
	 */

	@Override
	public List<AdminCompanysDTO> getCompanyList(String schema, String dataLibrary) {

		log.info("Inside getCompanyList method of AdminSettingsImpl");

		List<AdminCompanysDTO> companyList = new ArrayList<>();
		
		try {
			StringBuilder query = new StringBuilder("SELECT COMPANY_ID, NAME, CITY, POST_CODE, STREETANDNUM, AP_COMPANY_ID FROM ").append(schema).append(".O_COMPANY ");
			query.append(" WHERE ISACTIVE = 1 ");

			List<AdminCompanys> adminCompanysList = dbServiceRepository.getResultUsingQuery(AdminCompanys.class, query.toString(), true);

			if (adminCompanysList != null && !adminCompanysList.isEmpty()) {
				
				for(AdminCompanys adminCompanys : adminCompanysList){
					AdminCompanysDTO adminCompanysDto = new AdminCompanysDTO();
					
					adminCompanysDto.setCompanyId(StringUtils.leftPad(String.valueOf(adminCompanys.getCompanyId()),2,"0"));
					adminCompanysDto.setCompanyName(adminCompanys.getCompanyName());
					adminCompanysDto.setCity(adminCompanys.getCity());
					adminCompanysDto.setPostalCode(adminCompanys.getPostalCode());
					adminCompanysDto.setStreetNumber(adminCompanys.getStreetNumber());
					adminCompanysDto.setAlphaPlusCompanyId(StringUtils.leftPad(String.valueOf(adminCompanys.getAlphaPlusCompanyId()),2,"0"));
					
					companyList.add(adminCompanysDto);
				}
			}else{
				log.info("Copmany List is empty");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Admin Copmany"));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Admin Copmany"));
				throw exception;
			} 
			
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Admin companys list"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Admin companys list"), exception);
			throw exception;
		}
		
		return companyList;
	}
	
	/**
	 * This method is used to get the FIRMA  (Company) Details from DB
	 * @return Object
	 */

	@Override
	public AdminCompanysDTO getCompanyDetails(String schema, String dataLibrary,String companyId) {

		log.info("Inside getCompanyDetails method of AdminSettingsImpl");

		AdminCompanysDTO adminCompanysDTO = null;
		try {
			StringBuilder query = new StringBuilder("SELECT COMPANY_ID, NAME, CITY, LPAD (POST_CODE,5,0) as POST_CODE, STREETANDNUM, AP_COMPANY_ID FROM ").append(schema).append(".O_COMPANY");
			query.append(" WHERE ISACTIVE = 1 AND COMPANY_ID = ").append(companyId);

			List<AdminCompanys> adminCompanysList = dbServiceRepository.getResultUsingQuery(AdminCompanys.class, query.toString(), true);

			if (adminCompanysList != null && !adminCompanysList.isEmpty()) {
				
				for(AdminCompanys adminCompanys : adminCompanysList){
					
					adminCompanysDTO = new AdminCompanysDTO();
					
					adminCompanysDTO.setCompanyId(StringUtils.leftPad(String.valueOf(adminCompanys.getCompanyId()),2,"0"));
					adminCompanysDTO.setCompanyName(adminCompanys.getCompanyName());
					adminCompanysDTO.setCity(adminCompanys.getCity());
					adminCompanysDTO.setPostalCode(adminCompanys.getPostalCode());
					adminCompanysDTO.setStreetNumber(adminCompanys.getStreetNumber());
					adminCompanysDTO.setAlphaPlusCompanyId(StringUtils.leftPad(String.valueOf(adminCompanys.getAlphaPlusCompanyId()),2,"0"));
					
				}
			}else{
				log.info("Company details for admin setting not found in O_COMPANY table for this Id :" + companyId);
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "Companys"));
				log.error(messageService.getReadableMessage(ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "Companys"));
				throw exception;
			}
			
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Admin companys"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Admin companys"), exception);
			throw exception;
		}
		
		return adminCompanysDTO;
	}
	
	/**
	 * This method is used to update the FIRMA  (Company) Details into DB
	 * @return Object
	 */

	@Override
	public Map<String, Boolean> updateCompanyDetails(AdminCompanysDTO adminCompanys_obj, String schema, String loginName, String dataLibrary) {

		log.info("Inside updateCompanyDetails method of AdminSettingsImpl");
		Map<String, Boolean> programOutput = new HashMap<String, Boolean>();
		Connection con = null;
		try {
			if (adminCompanys_obj != null && adminCompanys_obj.getCompanyId() != null 
					    && !adminCompanys_obj.getCompanyId().isEmpty() ) {
				StringBuilder query = new StringBuilder("UPDATE ").append(schema).append(".O_COMPANY ");
				query.append(" SET NAME =  ").append("'" + adminCompanys_obj.getCompanyName() + "'");
				query.append(", CITY = ").append("'" + adminCompanys_obj.getCity() + "'");
				query.append(", AP_COMPANY_ID = ").append("'" +adminCompanys_obj.getAlphaPlusCompanyId()+"'" );
				//query.append(", POST_CODE = ").append("LPAD ('" + adminCompanys_obj.getPostalCode() + "',5,0)");
				//query.append(", POST_CODE = ").append("'" +"LPAD(' "adminCompanys_obj.getPostalCode(), 5, 0)+ "'");
				query.append(", POST_CODE = ").append("'" + adminCompanys_obj.getPostalCode() + "'");
				query.append(", STREETANDNUM = ").append("'" + adminCompanys_obj.getStreetNumber() + "'");
				query.append(", UPDATED_TS = CURRENT TIMESTAMP");
				query.append(", UPDATED_BY = ").append("'"+loginName+"'");
				query.append(" WHERE ISACTIVE = 1 AND COMPANY_ID = ").append(adminCompanys_obj.getCompanyId());

				StringBuilder checkCompanyName = new StringBuilder("SELECT COUNT(*) AS COUNT FROM ").append(schema).append(".O_COMPANY ");
				checkCompanyName.append(" WHERE ( COMPANY_ID  <> "+adminCompanys_obj.getCompanyId()+" AND UPPER(NAME) = ").append("UPPER('"+adminCompanys_obj.getCompanyName()+"') ) OR ( COMPANY_ID  <> "+adminCompanys_obj.getCompanyId()+" AND AP_COMPANY_ID = ").append("'"+adminCompanys_obj.getAlphaPlusCompanyId()+"' )");
				
				con = dbServiceRepository.getConnectionObject();
				con.setAutoCommit(false);
				
				String count = dbServiceRepository.getCountUsingQuery(checkCompanyName.toString(),con);
				if(Integer.parseInt(count) > 0){
					log.info("Duplicate Company Name");
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.UNIQUE_CONSTRAINT_FAILED_MSG_KEY, "Company and AP_Company"));
					log.error(messageService.getReadableMessage(ExceptionMessages.UNIQUE_CONSTRAINT_FAILED_MSG_KEY, "Company and AP_Company"));
					throw exception;
					
				}
				
				boolean updateFlag = dbServiceRepository.updateResultUsingQuery(query.toString(),con);

				if (!updateFlag) {
					log.info("Unable to update company details for admin setting for this Id :"
							+ adminCompanys_obj.getCompanyId());
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(
							LocaleContextHolder.getLocale(), ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "Companys"));
					log.error(messageService.getReadableMessage(ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "Company"));
					throw exception;
				}
				
				con.commit();
				con.setAutoCommit(true);
				programOutput.put("isUpdated", true);
			} else {
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
								ExceptionMessages.UPDATE_FAILED_MSG_KEY, "companys details for admin setting"));
				log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "companys details"),
						exception);
				throw exception;
			}
		} catch (AlphaXException ex) {
			transactionRollback(con, ex);
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.UPDATE_FAILED_MSG_KEY, "companys details for admin setting"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY,
					"companys details"), exception);
			transactionRollback(con, exception);
			throw exception;
		}finally{
			connectionClose(con);
		}
		
		return programOutput;
	}
	
	/**
	 * This method is used to get the User List from DB
	 * @return List
	 */

	@Override
	public List<DropdownObject> getUserList(String schema, String dataLibrary) {

		log.info("Inside getUserList method of AdminSettingsImpl");

		List<DropdownObject> userList = new ArrayList<>();
		
		try {
			StringBuilder query = new StringBuilder("SELECT USER_ID, FIRSTNAME, LASTNAME FROM ").append(schema).append(".O_USER ");
			query.append(" WHERE ISACTIVE = 1 ");
			
			List<AdminUsers> adminUserList = dbServiceRepository.getResultUsingQuery(AdminUsers.class, query.toString(), true);

			if (adminUserList != null && !adminUserList.isEmpty()) {
				
				for(AdminUsers adminUsers : adminUserList){
					DropdownObject dropdownObject = new DropdownObject();
					dropdownObject.setKey(String.valueOf(adminUsers.getUserId()));
					dropdownObject.setValue(adminUsers.getUserFirstName()+","+adminUsers.getUserLastName());
					
					userList.add(dropdownObject);
				}
			}else{
				log.info("User List is empty");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Admin User"));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Admin User"));
				throw exception;
			} 
			
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Admin user list"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Admin user list"), exception);
			throw exception;
		}
		
		return userList;
	}
	
	/**
	 * This method is used to get user Details from DB
	 * @return Object
	 */
	
	@Override
	public AdminUsersDTO getUserDetailList(String schema, String dataLibrary,String userId) {

		log.info("Inside getUserDetailList method of AdminSettingsImpl");

		AdminUsersDTO adminUsersDTO = null;
		
		try {
			StringBuilder query = new StringBuilder("SELECT USER_ID, FIRSTNAME, LASTNAME, LOGIN, EMAIL, ISACTIVE, ( SELECT VALUE FROM ").append(schema).append(".O_SETUP ");
			query.append(" WHERE USER_ID = ").append(userId).append(" AND KEY = 'ET_CENTRAL_FUNCTION' ) As VALUE FROM  ");
			query.append(schema).append(".O_USER ").append(" WHERE ISACTIVE = 1 AND USER_ID = ").append(userId);
			
			List<AdminUsers> adminUserList = dbServiceRepository.getResultUsingQuery(AdminUsers.class, query.toString(), true);

			if (adminUserList != null && !adminUserList.isEmpty()) {
				
				for(AdminUsers adminUsers : adminUserList){
					adminUsersDTO = new AdminUsersDTO();
					adminUsersDTO.setUserId(String.valueOf(adminUsers.getUserId()));
					adminUsersDTO.setUserFirstName(adminUsers.getUserFirstName());
					adminUsersDTO.setUserLastName(adminUsers.getUserLastName());
					adminUsersDTO.setUserLoginName(adminUsers.getUserLoginName());
					adminUsersDTO.setUserEmail(adminUsers.getUserEmail());
					adminUsersDTO.setActive(adminUsers.isActive().compareTo(new BigDecimal("1"))==0?true:false);
					if(adminUsers.getCentralFunctionFlag()!= null && !adminUsers.getCentralFunctionFlag().isEmpty()){
					adminUsersDTO.setCentralFunctionValue(adminUsers.getCentralFunctionFlag().equalsIgnoreCase("1")?true:false);
					}else{
					adminUsersDTO.setCentralFunctionValue(false);
					}
					
				}
			}else{
				log.info("User List is empty");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Admin User Details"));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Admin User Details"));
				throw exception;
			} 
			
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Admin user Details"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Admin user list"), exception);
			throw exception;
		}
		
		return adminUsersDTO;
	}
	
	/**
	 * This method is used to update the user Details into DB
	 * @return Object
	 */

	@Override
	public Map<String, Boolean> updateUserDetails(AdminUsersDTO adminUser_obj, String schema, String loginName, String dataLibrary) {

		log.info("Inside updateUserDetails method of AdminSettingsImpl");
		Map<String, Boolean> programOutput = new HashMap<String, Boolean>();
		Connection con = null;
		try {
			if (adminUser_obj != null && adminUser_obj.getUserId() != null 
					    && !adminUser_obj.getUserId().isEmpty() ) {
				StringBuilder query = new StringBuilder("UPDATE ").append(schema).append(".O_USER ");
				query.append(" SET FIRSTNAME =  ").append("'" + adminUser_obj.getUserFirstName() + "'");
				query.append(", LASTNAME =  ").append("'" + adminUser_obj.getUserLastName() + "'");
				query.append(", LOGIN  = ").append("'" + adminUser_obj.getUserLoginName() + "'");
				query.append(", EMAIL = ").append("'" + adminUser_obj.getUserEmail() + "'");
				//query.append(", ISACTIVE = ").append( adminUser_obj.isActive()==true?1:0);
				query.append(", UPDATED_TS = CURRENT TIMESTAMP");
				query.append(", UPDATED_BY = ").append("'"+loginName+"'");
				query.append(" WHERE ISACTIVE = 1 AND USER_ID  = ").append(adminUser_obj.getUserId());

				con = dbServiceRepository.getConnectionObject();
				con.setAutoCommit(false);
				
				StringBuilder checkLogin = new StringBuilder("SELECT COUNT(*) AS COUNT FROM ").append(schema).append(".O_USER ");
				checkLogin.append(" WHERE USER_ID <> "+adminUser_obj.getUserId()+" AND UPPER(LOGIN) = ").append("UPPER('"+adminUser_obj.getUserLoginName()+"')");
				
				String count = dbServiceRepository.getCountUsingQuery(checkLogin.toString(),con);
				if(Integer.parseInt(count) > 0){
					log.info("Duplicate login");
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.UNIQUE_CONSTRAINT_FAILED_MSG_KEY, "Login"));
					log.error(messageService.getReadableMessage(ExceptionMessages.UNIQUE_CONSTRAINT_FAILED_MSG_KEY, "Login"));
					throw exception;
					
				}
				
				boolean updateFlag = dbServiceRepository.updateResultUsingQuery(query.toString(),con);

				if (!updateFlag) {
					log.info("Unable to update user details for admin setting for this Id :"
							+ adminUser_obj.getUserId());
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(
							LocaleContextHolder.getLocale(), ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "User"));
					log.error(messageService.getReadableMessage(ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "User"));
					throw exception;
				}
				
				updateCentralFunctionForUser(schema, dataLibrary, adminUser_obj.getUserId(), adminUser_obj.isCentralFunctionValue(), loginName);
				
				con.commit();
				con.setAutoCommit(true);
				programOutput.put("isUpdated", true);
			} else {
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
								ExceptionMessages.UPDATE_FAILED_MSG_KEY, "User details for admin setting"));
				log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "User details"),
						exception);
				throw exception;
			}
		} catch (AlphaXException ex) {
			transactionRollback(con, ex);
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.UPDATE_FAILED_MSG_KEY, "User details for admin setting"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY,
					"User details"), exception);
			transactionRollback(con, exception);
			throw exception;
		}finally{
			connectionClose(con);
		}
		
		return programOutput;
	}
	
	
	/**
	 * This method is used to get the Role List from DB
	 * @return List
	 */

	@Override
	public List<DropdownObject> getRoleList(String schema, String dataLibrary) {

		log.info("Inside getRoleList method of AdminSettingsImpl");

		List<DropdownObject> roleList = new ArrayList<>();
		
		try {
			StringBuilder query = new StringBuilder("SELECT ROLE_ID, Name FROM ").append(schema).append(".O_ROLE ");
			query.append(" WHERE ISACTIVE = 1");

			List<AdminRoles> adminRoleList = dbServiceRepository.getResultUsingQuery(AdminRoles.class, query.toString(), true);

			if (adminRoleList != null && !adminRoleList.isEmpty()) {
				
				for(AdminRoles adminRoles : adminRoleList){
					DropdownObject dropdownObject = new DropdownObject();
					dropdownObject.setKey(String.valueOf(adminRoles.getRoleId()));
					dropdownObject.setValue(adminRoles.getRoleName());
					
					roleList.add(dropdownObject);
				}
			}else{
				log.info("Role List is empty");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Admin Roles"));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Admin Roles"));
				throw exception;
			} 
			
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Admin role list"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Admin role list"), exception);
			throw exception;
		}
		
		return roleList;
	}
	
	/**
	 * This method is used to get Role Details from DB
	 * @return Object
	 */
	
	@Override
	public AdminRolesDTO getRoleDetailList(String schema, String dataLibrary,String roleId) {

		log.info("Inside getRoleDetailList method of AdminSettingsImpl");

		AdminRolesDTO adminRolesDTO = null;
		
		try {
			StringBuilder query = new StringBuilder("SELECT ROLE_ID, Name, DESCRIPTION, ISACTIVE  FROM ").append(schema).append(".O_ROLE ");
			query.append(" WHERE ISACTIVE = 1 AND ROLE_ID = ").append(roleId);
			
			List<AdminRoles> adminRoleList = dbServiceRepository.getResultUsingQuery(AdminRoles.class, query.toString(), true);

			if (adminRoleList != null && !adminRoleList.isEmpty()) {
				
				for(AdminRoles adminRoles : adminRoleList){
					adminRolesDTO = new AdminRolesDTO();
					adminRolesDTO.setRoleId(String.valueOf(adminRoles.getRoleId()));
					adminRolesDTO.setRoleName(adminRoles.getRoleName());
					adminRolesDTO.setRoleDescription(adminRoles.getRoleDescription());
					adminRolesDTO.setActive(adminRoles.isActive().compareTo(new BigDecimal("1"))==0?true:false);
				}
			}else{
				log.info("Role List is empty");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Admin role details"));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Admin role details"));
				throw exception;
			} 
			
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Admin role details"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Admin role details"), exception);
			throw exception;
		}
		
		return adminRolesDTO;
	}
	
	/**
	 * This method is used to update the role Details into DB
	 * @return Object
	 */

	@Override
	public Map<String, Boolean> updateRoleDetails(AdminRolesDTO adminRole_obj, String schema, String loginName, String dataLibrary) {

		log.info("Inside updateRoleDetails method of AdminSettingsImpl");
		Map<String, Boolean> programOutput = new HashMap<String, Boolean>();
		Connection con = null;
		try {
			if (adminRole_obj != null && adminRole_obj.getRoleId() != null 
					    && !adminRole_obj.getRoleId().isEmpty() ) {
				StringBuilder query = new StringBuilder("UPDATE ").append(schema).append(".O_ROLE ");
				query.append(" SET NAME =  ").append("'" + adminRole_obj.getRoleName() + "'");
				query.append(", DESCRIPTION  = ").append("'" + adminRole_obj.getRoleDescription() + "'");
				//query.append(", ISACTIVE = ").append( adminRole_obj.isActive()==true?1:0);
				query.append(", UPDATED_TS = CURRENT TIMESTAMP");
				query.append(", UPDATED_BY = ").append("'"+loginName+"'");
				query.append(" WHERE ISACTIVE = 1 AND ROLE_ID  = ").append(adminRole_obj.getRoleId());

				StringBuilder checkRoleName = new StringBuilder("SELECT COUNT(*) AS COUNT FROM ").append(schema).append(".O_ROLE ");
				checkRoleName.append(" WHERE ROLE_ID <> "+adminRole_obj.getRoleId()+" AND UPPER(NAME) = ").append("UPPER('"+adminRole_obj.getRoleName()+"')");
				
				con = dbServiceRepository.getConnectionObject();
				con.setAutoCommit(false);
				
				String count = dbServiceRepository.getCountUsingQuery(checkRoleName.toString(),con);
				if(Integer.parseInt(count) > 0){
					log.info("Duplicate login");
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.UNIQUE_CONSTRAINT_FAILED_MSG_KEY, "Role"));
					log.error(messageService.getReadableMessage(ExceptionMessages.UNIQUE_CONSTRAINT_FAILED_MSG_KEY, "Role"));
					throw exception;
					
				}
				
				boolean updateFlag = dbServiceRepository.updateResultUsingQuery(query.toString(),con);

				if (!updateFlag) {
					log.info("Unable to update role details for admin setting for this Id :"
							+ adminRole_obj.getRoleId());
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(
							LocaleContextHolder.getLocale(), ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "Role"));
					log.error(messageService.getReadableMessage(ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "Role"));
					throw exception;
				}
				con.commit();
				con.setAutoCommit(true);
				programOutput.put("isUpdated", true);
			} else {
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
								ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Role details for admin setting"));
				log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Role details"),
						exception);
				throw exception;
			}
		} catch (AlphaXException ex) {
			transactionRollback(con, ex);
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Role details for admin setting"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY,
					"Role details"), exception);
			transactionRollback(con, exception);
			throw exception;
		}finally{
			connectionClose(con);
		}
		
		return programOutput;
	}
	
	/**
	 * This method is used to get the module List from DB
	 * @return List
	 */

	@Override
	public List<DropdownObject> getModuleList(String schema, String dataLibrary) {

		log.info("Inside getModuleList method of AdminSettingsImpl");

		List<DropdownObject> moduleList = new ArrayList<>();
		
		try {
			StringBuilder query = new StringBuilder("SELECT MODULE_ID, MODULE_NAME FROM ").append(schema).append(".O_MODULE ");

			List<AdminModule> adminModuleList = dbServiceRepository.getResultUsingQuery(AdminModule.class, query.toString(), true);

			if (adminModuleList != null && !adminModuleList.isEmpty()) {
				
				for(AdminModule adminModule : adminModuleList){
					DropdownObject dropdownObject = new DropdownObject();
					dropdownObject.setKey(String.valueOf(adminModule.getModuleId()));
					dropdownObject.setValue(adminModule.getModuleName());
					
					moduleList.add(dropdownObject);
				}
			}else{
				log.info("Module List is empty");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Admin Module"));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Admin Module"));
				throw exception;
			} 
			
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Admin module list"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Admin module list"), exception);
			throw exception;
		}
		
		return moduleList;
	}
	
	/**
	 * This method is used to get module Details from DB
	 * @return Object
	 */
	
	@Override
	public AdminModuleDTO getModuleDetailList(String schema, String dataLibrary,String moduleId) {

		log.info("Inside getModuleDetailList method of AdminSettingsImpl");

		AdminModuleDTO adminModuleDTO = null;
		
		try {
			StringBuilder query = new StringBuilder("SELECT MODULE_ID, MODULE_Name, DESCRIPTION, ISACTIVE  FROM ").append(schema).append(".O_MODULE ");
			query.append(" WHERE MODULE_ID = ").append(moduleId);
			
			List<AdminModule> adminModuleList = dbServiceRepository.getResultUsingQuery(AdminModule.class, query.toString(), true);

			if (adminModuleList != null && !adminModuleList.isEmpty()) {
				
				for(AdminModule adminModule : adminModuleList){
					adminModuleDTO = new AdminModuleDTO();
					adminModuleDTO.setModuleId(String.valueOf(adminModule.getModuleId()));
					adminModuleDTO.setModuleName(adminModule.getModuleName());
					adminModuleDTO.setModuleDescription(adminModule.getModuleDescription());
					adminModuleDTO.setActive(adminModule.getActive().compareTo(new BigDecimal("1"))==0?true:false);
				}
			}else{
				log.info("Module List is empty");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Admin module Details"));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Admin module Details"));
				throw exception;
			} 
			
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Admin module Details"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Admin module list"), exception);
			throw exception;
		}
		
		return adminModuleDTO;
	}
	
	/**
	 * This method is used to update the module Details into DB
	 * @return Object
	 */

	@Override
	public AdminModuleDTO updateModuleDetails(AdminModuleDTO adminModule_obj, String schema, String loginName, String dataLibrary) {

		log.info("Inside updateModuleDetails method of AdminSettingsImpl");

		try {
			if (adminModule_obj != null && adminModule_obj.getModuleId() != null 
					    && !adminModule_obj.getModuleId().isEmpty() ) {
				StringBuilder query = new StringBuilder("UPDATE ").append(schema).append(".O_MODULE ");
				query.append(" SET MODULE_NAME =  ").append("'" + adminModule_obj.getModuleName() + "'");
				query.append(", DESCRIPTION  = ").append("'" + adminModule_obj.getModuleDescription() + "'");
				query.append(", ISACTIVE = ").append( adminModule_obj.isActive()==true?1:0);
				query.append(", UPDATED_TC = CURRENT TIMESTAMP");
				query.append(", UPDATED_BY = ").append("'"+loginName+"'");
				query.append(" WHERE MODULE_ID  = ").append(adminModule_obj.getModuleId());

				boolean updateFlag = dbServiceRepository.updateResultUsingQuery(query.toString());

				if (!updateFlag) {
					log.info("Unable to update module details for admin setting for this Id :"
							+ adminModule_obj.getModuleId());
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(
							LocaleContextHolder.getLocale(), ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "Module"));
					log.error(messageService.getReadableMessage(ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "Module"));
					throw exception;
				}
			} else {
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
								ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Module details for admin setting"));
				log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Module details"),
						exception);
				throw exception;
			}
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Module details for admin setting"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY,
					"Module details"), exception);
			throw exception;
		}
		
		return adminModule_obj;
	}
	
	
	/**
	 * This method is used to get the agency (Filiale) List from DB
	 * @return List
	 */
	@Override
	public List<DropdownObject> getAgencyList(String schema, String dataLibrary) {

		log.info("Inside getAgencyList method of AdminSettingsImpl");

		List<DropdownObject> agencyList = new ArrayList<>();
		
		try {
			StringBuilder query = new StringBuilder("SELECT AGENCY_ID, NAME FROM ").append(schema).append(".O_AGENCY ");
			query.append(" WHERE ISACTIVE = 1 ");

			List<AdminAgency> adminAgencyList = dbServiceRepository.getResultUsingQuery(AdminAgency.class, query.toString(), true);

			if (adminAgencyList != null && !adminAgencyList.isEmpty()) {
				
				for(AdminAgency adminAgency : adminAgencyList){
					DropdownObject dropdownObject = new DropdownObject();
					dropdownObject.setKey(StringUtils.leftPad(String.valueOf(adminAgency.getAgencyId()),2,"0"));
					dropdownObject.setValue(adminAgency.getAgencyName());
					
					agencyList.add(dropdownObject);
				}
			}else{
				log.info("Agency List is empty");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Admin Agency (Filiale)"));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Admin Agency (Filiale)"));
				throw exception;
			} 
			
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Admin Agency (Filiale) list"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Admin Agency (Filiale) list"), exception);
			throw exception;
		}
		
		return agencyList;
	}
	
	
	/**
	 * This method is used to delete the FIRMA  (Company) Details from DB
	 * @return Object
	 */

	@Override
	public Map<String, Boolean> deleteCompanyDetails(String schema, String dataLibrary,String companyId, String userLoginName) {

		log.info("Inside deleteCompanyDetails method of AdminSettingsImpl");
		Map<String, Boolean> programOutput = new HashMap<String, Boolean>();
		Connection con = null;
		try {
			StringBuilder query = new StringBuilder(" DELETE FROM ").append(schema).append(".O_COMPANY");
			query.append(" WHERE COMPANY_ID = ").append(companyId);
			
			con = dbServiceRepository.getConnectionObject();
			con.setAutoCommit(false);
			boolean updateFlag = dbServiceRepository.deleteResultUsingQuery(query.toString(),con);
			
			if (!updateFlag) {
				log.info("Unable to delete this company details for admin setting for this Id :"
						+ companyId);
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(
						LocaleContextHolder.getLocale(), ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "Company"));
				log.error(messageService.getReadableMessage(ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "Company"));
				throw exception;
			}
			
			StringBuilder delete_query = new StringBuilder(" DELETE FROM ").append(schema).append(".O_SETUP");
			delete_query.append(" WHERE AGENCY_ID=0 AND WAREHOUS_ID=0 AND ROLE_ID=0 AND PRINTER_ID=0 AND USER_ID = 0 AND COMPANY_ID =  ").append(companyId);
			
			boolean deleteFlag = dbServiceRepository.deleteResultUsingQuery(delete_query.toString(),con);
			
			if (!deleteFlag) {
				log.info("Company setting not available in o_setup for this Id :"
						+ companyId);
			}
			
			con.commit();
			con.setAutoCommit(true);
			programOutput.put("isDeleted", true);
			
		} catch (AlphaXException ex) {
			transactionRollback(con, ex);
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.DELETE_ADMIN_SETTING_FAILED_MSG_KEY, "company"));
			log.error(messageService.getReadableMessage(ExceptionMessages.DELETE_ADMIN_SETTING_FAILED_MSG_KEY,
					"company"), exception);
			transactionRollback(con, exception);
			throw exception;
		}finally{
			connectionClose(con);
		}
		
		return programOutput;
	}
	
	
	/**
	 * This method is used to create new  FIRMA  (Company) Details into DB
	 * @return Object
	 */

	@Override
	public AdminCompanysDTO createNewCompany(AdminCompanysDTO adminCompanys_obj,String schema, String dataLibrary,String loginUserName) {

		log.info("Inside createNewCompany method of AdminSettingsImpl");

		//AdminCompanysDTO adminCompanysDTO = null;
		try {
			
			StringBuilder query = new StringBuilder(" INSERT INTO ").append(schema).append(".O_COMPANY");
			query.append(" (NAME, CITY, AP_COMPANY_ID, POST_CODE, STREETANDNUM, ISACTIVE, CREATED_TS , CREATED_BY, ISDELETED )  values ( ");
			query.append("'"+adminCompanys_obj.getCompanyName()+"',");
			query.append("'"+adminCompanys_obj.getCity()+"',");
			query.append("'"+adminCompanys_obj.getAlphaPlusCompanyId() +"',");
			query.append(adminCompanys_obj.getPostalCode()+",");
			query.append("'"+adminCompanys_obj.getStreetNumber()+"',");
			query.append("1 ,");
			query.append("CURRENT TIMESTAMP ,");
			query.append("'"+loginUserName+"' ,");
			query.append(" 0 )");

			StringBuilder checkCompanyName = new StringBuilder("SELECT COUNT(*) AS COUNT FROM ").append(schema).append(".O_COMPANY ");
			checkCompanyName.append(" WHERE UPPER(NAME) = ").append("UPPER('"+adminCompanys_obj.getCompanyName()+"') OR AP_COMPANY_ID = ").append("'"+adminCompanys_obj.getAlphaPlusCompanyId()+"'");
			
			String count = dbServiceRepository.getCountUsingQuery(checkCompanyName.toString());
			if(Integer.parseInt(count) > 0){
				log.info("Duplicate Company Name");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.UNIQUE_CONSTRAINT_FAILED_MSG_KEY, "Company and AP_Company"));
				log.error(messageService.getReadableMessage(ExceptionMessages.UNIQUE_CONSTRAINT_FAILED_MSG_KEY, "Company and AP_Company"));
				throw exception;
				
			}
			
			int companyId = dbServiceRepository.insertResultUsingQuery(query.toString());

			if (companyId == 0) {
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.CREATE_FAILED_MSG_KEY, "New company details"));
				log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY, "New company details"), exception);
				throw exception;
			}
			
			adminCompanys_obj.setCompanyId(String.valueOf(companyId));
			
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.CREATE_FAILED_MSG_KEY, "New company details"));
			log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY,
					"New company details"), exception);
			throw exception;
		}
		
		return adminCompanys_obj;
	}
	
	
	
	/**
	 * This method is used to create new role Details into DB
	 * @return Object
	 */


	@Override
	public AdminRolesDTO createNewRole(AdminRolesDTO adminRole_obj, String schema, String loginUserName, String dataLibrary) {

		log.info("Inside createNewRole method of AdminSettingsImpl");

		//AdminRolesDTO adminRoleDTO = null;
		try {
			
			StringBuilder query = new StringBuilder(" INSERT INTO ").append(schema).append(".O_ROLE");
			query.append(" (Name, DESCRIPTION, ISACTIVE, CREATED_TS , CREATED_BY, ISDELETED)  values ( ");
			query.append("'"+adminRole_obj.getRoleName()+"',");
			query.append("'"+adminRole_obj.getRoleDescription()+"',");
			query.append("1 ,");
			query.append("CURRENT TIMESTAMP ,");
			query.append("'"+loginUserName+"',");
			query.append(" 0 )");

			StringBuilder checkRoleName = new StringBuilder("SELECT COUNT(*) AS COUNT FROM ").append(schema).append(".O_ROLE ");
			checkRoleName.append(" WHERE UPPER(NAME) = ").append("UPPER('"+adminRole_obj.getRoleName()+"')");
			
			String count = dbServiceRepository.getCountUsingQuery(checkRoleName.toString());
			if(Integer.parseInt(count) > 0){
				log.info("Duplicate login");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.UNIQUE_CONSTRAINT_FAILED_MSG_KEY, "Role"));
				log.error(messageService.getReadableMessage(ExceptionMessages.UNIQUE_CONSTRAINT_FAILED_MSG_KEY, "Role"));
				throw exception;
				
			}
			
			int roleId = dbServiceRepository.insertResultUsingQuery(query.toString());

			if (roleId == 0) {
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.CREATE_FAILED_MSG_KEY, "New Role"));
				log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY, "New Role"), exception);
				throw exception;
			}
			
			adminRole_obj.setRoleId(String.valueOf(roleId));
			adminRole_obj.setActive(true);
			
			
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.CREATE_FAILED_MSG_KEY, "New Role"));
			log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY,
					"New Role"), exception);
			throw exception;
		}
		
		return adminRole_obj;
	}
	
	/**
	 * This method is used to create new user Details into DB
	 * @return Object
	 */


	@Override
	public AdminUsersDTO createNewUser(AdminUsersDTO adminUser_obj, String schema, String loginUserName, String dataLibrary) {

		log.info("Inside createNewUser method of AdminSettingsImpl");

		//AdminUsersDTO adminUserDTO = null;
		Connection con = null;
		try {
			
			StringBuilder query = new StringBuilder(" INSERT INTO ").append(schema).append(".O_USER");
			query.append(" (FIRSTNAME, LASTNAME, LOGIN, EMAIL, ISACTIVE, CREATED_TS , CREATED_BY, ISDELETED)  values ( ");
			query.append("'"+adminUser_obj.getUserFirstName()+"',");
			query.append("'"+adminUser_obj.getUserLastName()+"',");
			query.append("'"+adminUser_obj.getUserLoginName()+"',");
			query.append("'"+adminUser_obj.getUserEmail()+"',");
			query.append("1 ,");
			query.append("CURRENT TIMESTAMP ,");
			query.append("'"+loginUserName+"' ,");
			query.append(" 0 )");
			
			con = dbServiceRepository.getConnectionObject();
			con.setAutoCommit(false);
			
			StringBuilder checkLogin = new StringBuilder("SELECT COUNT(*) AS COUNT FROM ").append(schema).append(".O_USER ");
			checkLogin.append(" WHERE UPPER(LOGIN) = ").append("UPPER('"+adminUser_obj.getUserLoginName()+"')");
			
			String count = dbServiceRepository.getCountUsingQuery(checkLogin.toString(),con);
			if(Integer.parseInt(count) > 0){
				log.info("Duplicate login");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.UNIQUE_CONSTRAINT_FAILED_MSG_KEY, "Login"));
				log.error(messageService.getReadableMessage(ExceptionMessages.UNIQUE_CONSTRAINT_FAILED_MSG_KEY, "Login"));
				throw exception;
				
			}
			
			int userId = dbServiceRepository.insertResultUsingQuery(query.toString(),con);

			if (userId == 0) {
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.CREATE_FAILED_MSG_KEY, "New User"));
				log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY, "New User"), exception);
				throw exception;
			}else{
				updateCentralFunctionForUser(schema,dataLibrary,String.valueOf(userId),adminUser_obj.isCentralFunctionValue(),loginUserName);
				con.commit();
				con.setAutoCommit(true);
			}
			
			adminUser_obj.setUserId(String.valueOf(userId));
			adminUser_obj.setActive(true);
			
		} catch (AlphaXException ex) {
			transactionRollback(con,ex);
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.CREATE_FAILED_MSG_KEY, "New User"));
			log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY,
					"New User"), exception);
			transactionRollback(con,exception);
			throw exception;
		}finally{
			connectionClose(con);
		}
		
		return adminUser_obj;
	}
	
	/**
	 * This method is used to delete the Role Details from DB
	 * @return Object
	 */

	@Override
	public Map<String, Boolean> deleteRoleDetails(String schema, String dataLibrary,String roleId,String userLoginName) {

		log.info("Inside deleteRoleDetails method of AdminSettingsImpl");
		Map<String, Boolean> programOutput = new HashMap<String, Boolean>();
		Connection con = null;
		try {
			StringBuilder query = new StringBuilder(" DELETE FROM ").append(schema).append(".O_ROLE");
			query.append(" WHERE ROLE_ID = ").append(roleId);

			con = dbServiceRepository.getConnectionObject();
			con.setAutoCommit(false);
			boolean updateFlag = dbServiceRepository.deleteResultUsingQuery(query.toString(),con);
			
			if (!updateFlag) {
				log.info("Unable to delete this role for admin setting for this Id :"
						+ roleId);
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(
						LocaleContextHolder.getLocale(), ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "Role"));
				log.error(messageService.getReadableMessage(ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "Role"));
				throw exception;
			}
			
			StringBuilder delete_query = new StringBuilder(" DELETE FROM ").append(schema).append(".O_SETUP");
			delete_query.append(" WHERE COMPANY_ID=0 AND AGENCY_ID=0 AND WAREHOUS_ID=0 AND PRINTER_ID=0 AND USER_ID = 0 AND ROLE_ID = ").append(roleId);
			
			boolean deleteFlag = dbServiceRepository.deleteResultUsingQuery(delete_query.toString(),con);
			
			if (!deleteFlag) {
				log.info("Role setting not available in o_setup for this Id :"
						+ roleId);
			}
			
			con.commit();
			con.setAutoCommit(true);
			programOutput.put("isDeleted", true);
			
		} catch (AlphaXException ex) {
			transactionRollback(con, ex);
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.DELETE_ADMIN_SETTING_FAILED_MSG_KEY, "Role"));
			log.error(messageService.getReadableMessage(ExceptionMessages.DELETE_ADMIN_SETTING_FAILED_MSG_KEY,
					"Role"), exception);
			transactionRollback(con, exception);
			throw exception;
		}finally{
			connectionClose(con);
		}
		
		return programOutput;
	}
	
	
	/**
	 * This method is used to delete the User Details from DB
	 * @return Object
	 */

	@Override
	public Map<String, Boolean> deleteUserDetails(String schema, String dataLibrary,String userId, String userLoginName) {

		log.info("Inside deleteUserDetails method of AdminSettingsImpl");
		Map<String, Boolean> programOutput = new HashMap<String, Boolean>();
		Connection con = null;
		try {
			
			StringBuilder query = new StringBuilder(" DELETE FROM ").append(schema).append(".O_USER");
			query.append(" WHERE USER_ID = ").append(userId);

			con = dbServiceRepository.getConnectionObject();
			con.setAutoCommit(false);
			boolean updateFlag = dbServiceRepository.deleteResultUsingQuery(query.toString(),con);
			
			if (!updateFlag) {
				log.info("Unable to delete this user for admin setting for this Id :"
						+ userId);
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(
						LocaleContextHolder.getLocale(), ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "User"));
				log.error(messageService.getReadableMessage(ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "User"));
				throw exception;
			}
			
			StringBuilder delete_query = new StringBuilder(" DELETE FROM ").append(schema).append(".O_SETUP");
			delete_query.append(" WHERE COMPANY_ID=0 AND AGENCY_ID=0 AND WAREHOUS_ID=0 AND ROLE_ID=0 AND PRINTER_ID=0 AND USER_ID = ").append(userId);
			
			boolean deleteFlag = dbServiceRepository.deleteResultUsingQuery(delete_query.toString(),con);
			
			if (!deleteFlag) {
				log.info("User setting not available in o_setup for this Id :"
						+ userId);
			}
			
			con.commit();
			con.setAutoCommit(true);
			programOutput.put("isDeleted", true);
		} catch (AlphaXException ex) {
			transactionRollback(con, ex);
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.DELETE_ADMIN_SETTING_FAILED_MSG_KEY, "User"));
			log.error(messageService.getReadableMessage(ExceptionMessages.DELETE_ADMIN_SETTING_FAILED_MSG_KEY,
					"User"), exception);
			
			transactionRollback(con, exception);
			
			throw exception;
		}finally{
			connectionClose(con);
		}
		return programOutput;
	}
	
	/**
	 * This method is used to get the login user List from DB
	 * @return List
	 */

	@Override
	public GlobalSearch getLoginUserList(String schema, String dataLibrary,String searchText,String pageSize, String pageNumber) {

		log.info("Inside getLoginUserList method of AdminSettingsImpl");
		AdminLoginUsersDTO adminLoginUsersDTO = null;
		List<AdminLoginUsersDTO> loginUserList = new ArrayList<>();
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

			
			StringBuilder query = new StringBuilder("SELECT BNU_NUTZER AS LOGIN, BNU_VNAME AS FIRSTNAME, BNU_NNAME AS LASTNAME,");
			query.append("( SELECT COUNT(*) FROM ").append(schema).append(".UBF_NUTZER "); 
			query.append(" WHERE  UPPER(BNU_NUTZER) LIKE UPPER(").append("'%"+searchText+"%')");
			query.append(" OR UPPER(BNU_VNAME) LIKE UPPER(").append("'%"+searchText+"%')");
			query.append(" OR UPPER(BNU_NNAME) LIKE UPPER(").append("'%"+searchText+"%') ) AS ROWNUMER ");
			query.append(" FROM ").append(schema).append(".UBF_NUTZER ");
			query.append(" WHERE  UPPER(BNU_NUTZER) LIKE UPPER(").append("'%"+searchText+"%')");
			query.append(" OR UPPER(BNU_VNAME) LIKE UPPER(").append("'%"+searchText+"%')");
			query.append(" OR UPPER(BNU_NNAME) LIKE UPPER(").append("'%"+searchText+"%') OFFSET ");
			query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");

			
			
			List<AdminUsers> adminUserList = dbServiceRepository.getResultUsingQuery(AdminUsers.class, query.toString(), true);

			if (adminUserList != null && !adminUserList.isEmpty()) {
				
				for(AdminUsers adminUsers : adminUserList){
					adminLoginUsersDTO = new AdminLoginUsersDTO();
					adminLoginUsersDTO.setUserLoginName(adminUsers.getUserLoginName());
					adminLoginUsersDTO.setUserFirstName(adminUsers.getUserFirstName());
					adminLoginUsersDTO.setUserLastName(adminUsers.getUserLastName());
					
					loginUserList.add(adminLoginUsersDTO);
				}
				
				globalSearchList.setSearchDetailsList(loginUserList);
				globalSearchList.setTotalPages(Integer.toString(adminUserList.get(0).getTotalCount()));
				globalSearchList.setTotalRecordCnt(Integer.toString(adminUserList.get(0).getTotalCount()));
				
			}else{
				log.info("Login user List is empty");
				globalSearchList.setSearchDetailsList(loginUserList);
				globalSearchList.setTotalPages(Integer.toString(0));
				globalSearchList.setTotalRecordCnt(Integer.toString(0));
			} 
			
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Login user list"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Login user list"), exception);
			throw exception;
		}
		
		return globalSearchList;
	}
	
	
	/**
	 * This method is used to delete the Role Details from DB
	 * @return Object
	 */

	@Override
	public Map<String, Boolean> updateAgencyRoleAndUserMapping(List<Admin_UserRoleMappingDTO> mapping_DTO, String schema, String dataLibrary,String agencyId,String roleId,String userLoginName) {

		log.info("Inside updateAgencyRoleAndUserMapping method of AdminSettingsImpl");
		Map<String, Boolean> programOutput = new HashMap<String, Boolean>();
		programOutput.put("isUpdated", false);
		Connection con = null;
		try {
			if (mapping_DTO != null && !mapping_DTO.isEmpty()) {
				
				con = dbServiceRepository.getConnectionObject();
				con.setAutoCommit(false);
				
				for (Admin_UserRoleMappingDTO userRoleMappingDTO : mapping_DTO) {

					if (userRoleMappingDTO.isActive()) {
						
						StringBuilder query = new StringBuilder(" DELETE FROM ").append(schema).append(".O_USERRL");
						query.append(" WHERE USER_ID = ").append(userRoleMappingDTO.getUserId());
						query.append(" AND AGENCY_ID = ").append(agencyId);
						boolean deleteFlag = dbServiceRepository.updateResultUsingQuery(query.toString());
						if(!deleteFlag){
							log.debug("This user not have any role in current agency :"+ userRoleMappingDTO.getUserId());	
						}

						StringBuilder insert_mapping_query = new StringBuilder(" INSERT INTO ").append(schema).append(".O_USERRL");
						insert_mapping_query.append(" (USER_ID,ROLE_ID, AGENCY_ID, ISACTIVE, CREATED_TS , CREATED_BY, ISDEFAULT)  values ( ");
						insert_mapping_query.append("'" + userRoleMappingDTO.getUserId() + "',");
						insert_mapping_query.append(roleId + ",");
						insert_mapping_query.append(agencyId + ",");
						insert_mapping_query.append("1 ,");
						insert_mapping_query.append("CURRENT TIMESTAMP ,");
						insert_mapping_query.append("'" + userLoginName + "' ,");
						insert_mapping_query.append(" 0 )");

						int userRoleId = dbServiceRepository.insertResultUsingQuery(insert_mapping_query.toString());

						if (userRoleId == 0) {
							AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
											ExceptionMessages.CREATE_FAILED_MSG_KEY, "User Role Mapping"));
							log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY,"User Role Mapping"), exception);
							throw exception;
						}
					} else {
						
						boolean deleteFlag = false;
						if(userRoleMappingDTO.getUserRoleId() != null && !userRoleMappingDTO.getUserRoleId().isEmpty()){
						StringBuilder query = new StringBuilder(" DELETE FROM ").append(schema).append(".O_USERRL");
						query.append(" WHERE USERROLE_ID = ").append(userRoleMappingDTO.getUserRoleId());

						deleteFlag = dbServiceRepository.updateResultUsingQuery(query.toString());
						}

						if (!deleteFlag) {
							log.info("Unable to delete User Role Mapping details for admin setting for this Id :"+ userRoleMappingDTO.getUserRoleId());
							AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
											ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "UserRoleId"));
							log.error(messageService.getReadableMessage(ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY,
									"UserRoleId"));
							throw exception;
						}

					}

				}
				
				con.commit();
				con.setAutoCommit(true);
			}
			programOutput.put("isUpdated", true);
		} catch (AlphaXException ex) {
			transactionRollback(con, ex);
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.UPDATE_FAILED_MSG_KEY, "User role mapping"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY,
					"User role mapping"), exception);
			transactionRollback(con, exception);
			throw exception;
		}finally{
			connectionClose(con);
		}
		
		return programOutput;
	}
	
	
	
	/**
	 * This method is used to delete the Role Details from DB
	 * @return Object
	 */

	@Override
	public Map<String, Boolean> updateAgencyWarehousMapping(List<Admin_AgencyWarehouseMappingDTO> mapping_DTO, String schema, String dataLibrary,String agencyId,String userLoginName) {

		log.info("Inside updateAgencyWarehousMapping method of AdminSettingsImpl");
		Map<String, Boolean> programOutput = new HashMap<String, Boolean>();
		programOutput.put("isUpdated", false);
		try {
			if (mapping_DTO != null && !mapping_DTO.isEmpty()) {
				for (Admin_AgencyWarehouseMappingDTO agencyWarehouseMappingDTO : mapping_DTO) {

					if (agencyWarehouseMappingDTO.isActive()) {

						StringBuilder insert_mapping_query = new StringBuilder(" INSERT INTO ").append(schema).append(".O_AGNWRH");
						insert_mapping_query.append(" (AGENCY_ID,WAREHOUS_ID, ISACTIVE, CREATED_TS , CREATED_BY)  values ( ");
						insert_mapping_query.append(agencyId+ ",");
						insert_mapping_query.append(agencyWarehouseMappingDTO.getWarehousId() + ",");
						insert_mapping_query.append("1 ,");
						insert_mapping_query.append("CURRENT TIMESTAMP ,");
						insert_mapping_query.append("'" + userLoginName + "' )");

						int agencyWarehouseId = dbServiceRepository.insertResultUsingQuery(insert_mapping_query.toString());

						if (agencyWarehouseId == 0) {
							AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
											ExceptionMessages.CREATE_FAILED_MSG_KEY, "Agency Warehouse Mapping"));
							log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY,"Agency Warehouse Mapping"), exception);
							throw exception;
						}
					} else {

						StringBuilder query = new StringBuilder(" DELETE FROM ").append(schema).append(".O_AGNWRH");
						query.append(" WHERE AGENWRHS_ID = ").append(agencyWarehouseMappingDTO.getAgencyWarehousId());

						boolean deleteFlag = dbServiceRepository.updateResultUsingQuery(query.toString());

						if (!deleteFlag) {
							log.info("Unable to delete Agency Warehouse Mapping details for admin setting for this Id :"+ agencyWarehouseMappingDTO.getAgencyWarehousId());
							AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
											ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "AgencyWarehousId"));
							log.error(messageService.getReadableMessage(ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY,
									"AgencyWarehousId"));
							throw exception;
						}

					}

				}
			}
			programOutput.put("isUpdated", true);
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Agency Warehouse Mapping"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY,
					"Agency Warehouse Mapping"), exception);
			throw exception;
		}
		
		return programOutput;
	}
	
	
	/**
	 * This method is used to get the Filiale (Agency) Details from DB
	 * @return AdminAgencyDTO
	 */
	@Override
	public AdminAgencyDTO getAgencyDetailsUsingId(String schema, String dataLibrary, String agencyId) {

		log.info("Inside getAgencyDetailsUsingId method of AdminSettingsImpl");
		AdminAgencyDTO adminAgencyDTO = null;
		try {
			StringBuilder query = new StringBuilder("select AGENCY_ID, AP_AGENCY_ID, NAME, CITY, LPAD (POST_CODE,5,0) as POST_CODE, STREETANDNUM, ISACTIVE, COMPANY_ID ,( SELECT VALUE FROM ");
			query.append(schema).append(" .O_SETUP  WHERE AGENCY_ID = ").append(agencyId).append(" AND KEY = 'BRANCH_HAS_HYUNDAI_ACCESS') AS VALUE FROM ");
	query.append(schema).append(".O_AGENCY");
	query.append(" where ISACTIVE = 1 and AGENCY_ID = ").append(agencyId);

			List<AdminAgency> adminAgencyList = dbServiceRepository.getResultUsingQuery(AdminAgency.class, query.toString(), true);

			if (adminAgencyList != null && !adminAgencyList.isEmpty()) {

				for(AdminAgency adminAgency : adminAgencyList){

					adminAgencyDTO = new AdminAgencyDTO();

					adminAgencyDTO.setAgencyId(String.valueOf(adminAgency.getAgencyId()));
					adminAgencyDTO.setAlphaPlusAgencyId(String.valueOf(adminAgency.getAlphaPlusAgencyId()));
					adminAgencyDTO.setAgencyName(adminAgency.getAgencyName());
					adminAgencyDTO.setCity(adminAgency.getCity());
					adminAgencyDTO.setPostalCode(adminAgency.getPostalCode());
					adminAgencyDTO.setStreetNumber(adminAgency.getStreetNumber());
					adminAgencyDTO.setCompanyId(String.valueOf(adminAgency.getCompanyId()));
					adminAgencyDTO.setActive(adminAgency.getActive().compareTo(new BigDecimal("1"))==0?true:false);
					if(adminAgency.getFunctionFlag()!= null && !adminAgency.getFunctionFlag().isEmpty()){
						adminAgencyDTO.setFunctionValue(adminAgency.getFunctionFlag().equalsIgnoreCase("1")?true:false);
						}else{
							adminAgencyDTO.setFunctionValue(false);
						}
				}
			}else{
				log.info("Agency details for admin setting not found in O_AGENCY table for this Id : {}", agencyId);
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "Agency (Filiale)"));
				log.error(messageService.getReadableMessage(ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "Agency (Filiale)"));
				throw exception;
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Agency (Filiale)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Agency (Filiale)"), exception);
			throw exception;
		}

		return adminAgencyDTO;
	}
	
	
	/**
	 * This method is used to Create the Filiale (Agency) Details in DB
	 * @return AdminAgencyDTO
	 */
	@Override
	public AdminAgencyDTO createNewAgency(AdminAgencyDTO adminAgency_obj, String schema, String dataLibrary, String loginUserName) {

		log.info("Inside createNewAgency method of AdminSettingsImpl");
		//Connection con = null;
		try (Connection con = dbServiceRepository.getConnectionObject();){
			
			StringBuilder query = new StringBuilder(" INSERT INTO ").append(schema).append(".O_AGENCY");
			query.append(" (AP_AGENCY_ID, NAME, CITY, POST_CODE, STREETANDNUM, ISACTIVE, COMPANY_ID, CREATED_TS , CREATED_BY, ISDELETED)  values ( ");
			query.append("'"+adminAgency_obj.getAlphaPlusAgencyId()+"' ,");
			query.append("'"+adminAgency_obj.getAgencyName()+"',");
			query.append("'"+adminAgency_obj.getCity()+"',");
			query.append("'"+adminAgency_obj.getPostalCode()+"',");
			query.append("'"+adminAgency_obj.getStreetNumber()+"',");
			query.append("1 ,");
			query.append(adminAgency_obj.getCompanyId()+",");
			query.append("CURRENT TIMESTAMP ,");
			query.append("'"+loginUserName+"' ,");
			query.append(" 0 )");
			
			con.setAutoCommit(false);
			
			StringBuilder checkCompanyName = new StringBuilder("SELECT COUNT(*) AS COUNT FROM ").append(schema).append(".O_AGENCY ");
			checkCompanyName.append(" WHERE  UPPER(NAME) = ").append("UPPER('"+adminAgency_obj.getAgencyName()+"') AND COMPANY_ID = ").append(adminAgency_obj.getCompanyId());
			
			String count = dbServiceRepository.getCountUsingQuery(checkCompanyName.toString(),con);
			if(Integer.parseInt(count) > 0){
				log.info("Duplicate Agency Name");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.UNIQUE_CONSTRAINT_FAILED_MSG_KEY, "Company_id and Agency"));
				log.error(messageService.getReadableMessage(ExceptionMessages.UNIQUE_CONSTRAINT_FAILED_MSG_KEY, "Company_id and Agency"));
				throw exception;
				
			}
			

			int agencyId = dbServiceRepository.insertResultUsingQuery(query.toString(),con);

			if (agencyId == 0) {
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.CREATE_FAILED_MSG_KEY, "New Agency"));
				log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY, "New Agency"), exception);
				throw exception;
			}else{
				updateCentralFunctionForAgency(schema,dataLibrary,String.valueOf(agencyId),adminAgency_obj.isFunctionValue(),loginUserName);
				con.commit();
				con.setAutoCommit(true);
			}
			
			
			adminAgency_obj.setAgencyId(String.valueOf(agencyId));
			adminAgency_obj.setActive(true);

		} catch (AlphaXException ex) {
			
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.CREATE_FAILED_MSG_KEY, "New Agency"));
			log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY,
					"New Agency"), exception);
		
			throw exception;
		}

		return adminAgency_obj;
	}
	
	
	/**
	 * This method is used to update the Agency details into DB
	 * @return AdminAgencyDTO
	 */
	@Override
	public Map<String, Boolean> updateAgencyDetails(AdminAgencyDTO agency_obj, String schema, String dataLibrary, String loginName) {

		log.info("Inside updateAgencyDetails method of AdminSettingsImpl");
		Map<String, Boolean> programOutput = new HashMap<String, Boolean>();
		
		try (Connection con = dbServiceRepository.getConnectionObject();){
			if (agency_obj != null && agency_obj.getAgencyId() != null && !agency_obj.getAgencyId().isEmpty() ) {
				
				StringBuilder query = new StringBuilder("UPDATE ").append(schema).append(".O_AGENCY ");
				query.append(" SET AP_AGENCY_ID = ").append("'"+agency_obj.getAlphaPlusAgencyId()+"'");
				query.append(", NAME = ").append("'" + agency_obj.getAgencyName() + "'");
				query.append(", CITY = ").append("'" + agency_obj.getCity() + "'");
				query.append(", POST_CODE = ").append("'" + agency_obj.getPostalCode() + "'");
				query.append(", STREETANDNUM= ").append("'" + agency_obj.getStreetNumber() + "'");
				query.append(", COMPANY_ID = ").append(agency_obj.getCompanyId());
				query.append(", UPDATED_TS = CURRENT TIMESTAMP");
				query.append(", UPDATED_BY = ").append("'"+loginName+"'");
				query.append(" WHERE ISACTIVE = 1 AND AGENCY_ID  = ").append(agency_obj.getAgencyId());

				StringBuilder checkCompanyName = new StringBuilder("SELECT COUNT(*) AS COUNT FROM ").append(schema).append(".O_AGENCY ");
				checkCompanyName.append(" WHERE AGENCY_ID <> "+agency_obj.getAgencyId()+" AND UPPER(NAME) = ").append("UPPER('"+agency_obj.getAgencyName()+"') AND COMPANY_ID = ").append(agency_obj.getCompanyId());
				
			
				con.setAutoCommit(false);
				
				String count = dbServiceRepository.getCountUsingQuery(checkCompanyName.toString(),con);
				if(Integer.parseInt(count) > 0){
					log.info("Duplicate Agency Name");
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.UNIQUE_CONSTRAINT_FAILED_MSG_KEY, "Company_id and Agency"));
					log.error(messageService.getReadableMessage(ExceptionMessages.UNIQUE_CONSTRAINT_FAILED_MSG_KEY, "Company_id and Agency"));
					throw exception;
					
				}
				
				boolean updateFlag = dbServiceRepository.updateResultUsingQuery(query.toString(),con);

				if (!updateFlag) {
					log.info("Unable to update agency details for admin setting for this Id :"
							+ agency_obj.getAgencyId());
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(
							LocaleContextHolder.getLocale(), ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "Agency"));
					log.error(messageService.getReadableMessage(ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "Agency"));
					throw exception;
				}
				updateCentralFunctionForAgency(schema, dataLibrary, agency_obj.getAgencyId(), agency_obj.isFunctionValue(), loginName);
				
				con.commit();
				con.setAutoCommit(true);
				programOutput.put("isUpdated", true);
			} else {
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Agency details"));
				log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Agency details"),
						exception);
				throw exception;
			}
		} catch (AlphaXException ex) {

			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Agency details"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY,
					"Agency details"), exception);
	
			throw exception;
		}finally{
		
		}

		return programOutput;
	}
	
	
	/**
	 * This method is used to delete the Agency details from DB
	 * @return Map
	 */
	@Override
	public Map<String, Boolean> deleteAgencyDetails(String agencyId, String schema, String dataLibrary, String loginName) {

		log.info("Inside deleteAgencyDetails method of AdminSettingsImpl");
		Map<String, Boolean> programOutput = new HashMap<String, Boolean>();
		Connection con = null;
		try {
			StringBuilder query = new StringBuilder(" DELETE FROM ").append(schema).append(".O_AGENCY");
			query.append(" WHERE AGENCY_ID = ").append(agencyId);

			con = dbServiceRepository.getConnectionObject();
			con.setAutoCommit(false);
			boolean updateFlag = dbServiceRepository.deleteResultUsingQuery(query.toString(),con);

			if (!updateFlag) {
				log.info("Unable to delete this Agency for admin setting for this Id :"
						+ agencyId);
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(
						LocaleContextHolder.getLocale(), ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "Agency"));
				log.error(messageService.getReadableMessage(ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "Agency"));
				throw exception;
			}
			
			StringBuilder delete_query = new StringBuilder(" DELETE FROM ").append(schema).append(".O_SETUP");
			delete_query.append(" WHERE COMPANY_ID=0 AND WAREHOUS_ID=0 AND ROLE_ID=0 AND PRINTER_ID=0 AND USER_ID = 0 AND AGENCY_ID = ").append(agencyId);
			
			boolean deleteFlag = dbServiceRepository.deleteResultUsingQuery(delete_query.toString(),con);
			
			if (!deleteFlag) {
				log.info("Agency setting not available in o_setup for this Id :"
						+ agencyId);
			}
			
			con.commit();
			con.setAutoCommit(true);
			programOutput.put("isDeleted", true);

		} catch (AlphaXException ex) {
			transactionRollback(con, ex);
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.DELETE_ADMIN_SETTING_FAILED_MSG_KEY, "Agency"));
			log.error(messageService.getReadableMessage(ExceptionMessages.DELETE_ADMIN_SETTING_FAILED_MSG_KEY,
					"Agency"), exception);
			transactionRollback(con, exception);
			throw exception;
		}finally{
			connectionClose(con);
		}

		return programOutput;
	}
	
	
	/**
	 * This method is used to get the Warehouse (Lager) List from DB
	 * @return List
	 */
	@Override
	public List<DropdownObject> getWarehouseList(String schema, String dataLibrary) {

		log.info("Inside getWarehouseList method of AdminSettingsImpl");

		List<DropdownObject> warehouseList = new ArrayList<>();
		
		try {
			StringBuilder query = new StringBuilder("SELECT WAREHOUS_ID, NAME FROM ").append(schema).append(".O_WRH ");
			query.append(" WHERE ISACTIVE = 1 ");

			List<AdminWarehouse> adminWarehouseList = dbServiceRepository.getResultUsingQuery(AdminWarehouse.class, query.toString(), true);

			if (adminWarehouseList != null && !adminWarehouseList.isEmpty()) {
				
				for(AdminWarehouse adminWarehouse : adminWarehouseList){
					DropdownObject dropdownObject = new DropdownObject();
					dropdownObject.setKey(StringUtils.leftPad(String.valueOf(adminWarehouse.getWarehouseId()),2,"0"));
					dropdownObject.setValue(adminWarehouse.getWarehouseName());
					
					warehouseList.add(dropdownObject);
				}
			}else{
				log.info("Warehouse List is empty");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Warehouse (Lager)"));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Warehouse (Lager)"));
				throw exception;
			} 
			
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Admin Warehouse (Lager) list"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Admin Warehouse (Lager) list"), exception);
			throw exception;
		}
		
		return warehouseList;
	}
	
	
	/**
	 * This method is used to get the Lager (Warehouse) Details from DB
	 * @return AdminWarehouseDTO
	 */
	@Override
	public AdminWarehouseDTO getWarehouseDetailsUsingId(String schema, String dataLibrary, String warehouseId) {

		log.info("Inside getWarehouseDetailsUsingId method of AdminSettingsImpl");
		AdminWarehouseDTO adminWarehouseDTO = null;

		try {

			StringBuilder query = new StringBuilder("select WAREHOUS_ID, AP_WAREHOUS_ID, NAME, CITY, LPAD (POST_CODE,5,0) as POST_CODE, STREETANDNUM, ISACTIVE, VF_NUMBER from ").append(schema).append(".O_WRH");
			query.append(" where ISACTIVE = 1 and WAREHOUS_ID = ").append(warehouseId);

			List<AdminWarehouse> adminWarehouseList = dbServiceRepository.getResultUsingQuery(AdminWarehouse.class, query.toString(), true);

			if (adminWarehouseList != null && !adminWarehouseList.isEmpty()) {

				for(AdminWarehouse adminWarehouse : adminWarehouseList){

					adminWarehouseDTO = new AdminWarehouseDTO();

					adminWarehouseDTO.setWarehouseId(String.valueOf(adminWarehouse.getWarehouseId()));
					adminWarehouseDTO.setAlphaPlusWarehouseId(adminWarehouse.getAlphaPlusWarehouseId());
					adminWarehouseDTO.setWarehouseName(adminWarehouse.getWarehouseName());
					adminWarehouseDTO.setCity(adminWarehouse.getCity());
					adminWarehouseDTO.setPostalCode(adminWarehouse.getPostalCode());
					adminWarehouseDTO.setStreetNumber(adminWarehouse.getStreetNumber());
					adminWarehouseDTO.setActive(String.valueOf(adminWarehouse.getActive()).equals("1")?true:false);
					adminWarehouseDTO.setVfNumber(String.valueOf(adminWarehouse.getVfNumber()));

				}
			}else{
				log.info("Warehouse details for admin setting not found in O_WRH table for this Id : {}", warehouseId);
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "Warehouse (Lager)"));
				log.error(messageService.getReadableMessage(ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "Warehouse (Lager)"));
				throw exception;
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Warehouse (Lager)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Warehouse (Lager)"), exception);
			throw exception;
		}

		return adminWarehouseDTO;
	}
	
	
	/**
	 * This method is used to Create the Lager (Warehouse) Details in DB
	 * @return AdminWarehouseDTO
	 */
	@Override
	public AdminWarehouseDTO createNewWarehouse(AdminWarehouseDTO adminWarehouse_obj, String schema, String dataLibrary, String loginUserName) {

		log.info("Inside createNewWarehouse method of AdminSettingsImpl");
		Connection con = null;
		
		try {
			StringBuilder query = new StringBuilder(" INSERT INTO ").append(schema).append(".O_WRH");
			query.append(" (AP_WAREHOUS_ID, NAME, CITY, POST_CODE, STREETANDNUM, VF_NUMBER, ISACTIVE, CREATED_TS , CREATED_BY, ISDELETED)  values ( ");
			query.append("'"+adminWarehouse_obj.getAlphaPlusWarehouseId()+"' ,");
			query.append("'"+adminWarehouse_obj.getWarehouseName()+"',");
			query.append("'"+adminWarehouse_obj.getCity()+"',");
			query.append("'"+adminWarehouse_obj.getPostalCode()+"',");
			query.append("'"+adminWarehouse_obj.getStreetNumber()+"',");
			query.append(adminWarehouse_obj.getVfNumber()+", ");
			query.append("1 ,");
			query.append("CURRENT TIMESTAMP ,");
			query.append("'"+loginUserName+"' ,");
			query.append(" 0 )");

			con = dbServiceRepository.getConnectionObject();
			con.setAutoCommit(false);
			
			StringBuilder checkWarehouseName = new StringBuilder("SELECT COUNT(*) AS COUNT FROM ").append(schema).append(".O_WRH ");
			checkWarehouseName.append(" WHERE UPPER(NAME) = ").append("UPPER('"+adminWarehouse_obj.getWarehouseName()+"') OR AP_WAREHOUS_ID = ").append("'"+adminWarehouse_obj.getAlphaPlusWarehouseId()+"'");
			
			String count = dbServiceRepository.getCountUsingQuery(checkWarehouseName.toString(),con);
			if(Integer.parseInt(count) > 0){
				log.info("Duplicate Agency Name");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.UNIQUE_CONSTRAINT_FAILED_MSG_KEY, "Warehouse or AP_warehouseId"));
				log.error(messageService.getReadableMessage(ExceptionMessages.UNIQUE_CONSTRAINT_FAILED_MSG_KEY, "Warehouse or AP_warehouseId"));
				throw exception;
				
			}
			
			int warehouseId = dbServiceRepository.insertResultUsingQuery(query.toString(),con);

			if (warehouseId == 0) {
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.CREATE_FAILED_MSG_KEY, "New Warehouse (Lager)"));
				log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY, "New Warehouse (Lager)"), exception);
				throw exception;
			}else{
				
				//Create default warehouse related value
				StringBuilder query_for_o_setup = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
				query_for_o_setup.append(" (WAREHOUS_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP )  values ( ");
				query_for_o_setup.append(warehouseId +",");
				query_for_o_setup.append("'CONF_AUTO_PRICE',");
				query_for_o_setup.append("0 ,");
				query_for_o_setup.append("'"+loginUserName+"',");
				query_for_o_setup.append("CURRENT TIMESTAMP ),");
				
				query_for_o_setup.append("( "+warehouseId +",");
				query_for_o_setup.append("'CONF_AUTO_PRICE_VALUE',");
				query_for_o_setup.append("0 ,");
				query_for_o_setup.append("'"+loginUserName+"',");
				query_for_o_setup.append("CURRENT TIMESTAMP ),");
				
				query_for_o_setup.append("( "+warehouseId +",");
				query_for_o_setup.append("'CONF_AUTO_MARKETINGCODE',");
				query_for_o_setup.append("0 ,");
				query_for_o_setup.append("'"+loginUserName+"',");
				query_for_o_setup.append("CURRENT TIMESTAMP ),");
				
				query_for_o_setup.append("( "+warehouseId +",");
				query_for_o_setup.append("'CONF_AUTO_MARKETINGCODE_VALUE',");
				query_for_o_setup.append("0 ,");
				query_for_o_setup.append("'"+loginUserName+"',");
				query_for_o_setup.append("CURRENT TIMESTAMP ),");
				
				query_for_o_setup.append("( "+warehouseId +",");
				query_for_o_setup.append("'CONF_AUTO_NAME',");
				query_for_o_setup.append("0 ,");
				query_for_o_setup.append("'"+loginUserName+"',");
				query_for_o_setup.append("CURRENT TIMESTAMP ),");
				
				query_for_o_setup.append("( "+warehouseId +",");
				query_for_o_setup.append("'CONF_AUTO_NAME_VALUE',");
				query_for_o_setup.append("0 ,");
				query_for_o_setup.append("'"+loginUserName+"',");
				query_for_o_setup.append("CURRENT TIMESTAMP ),");
				
				query_for_o_setup.append("( "+warehouseId +",");
				query_for_o_setup.append("'CONF_AUTO_DISCOUNT',");
				query_for_o_setup.append("0 ,");
				query_for_o_setup.append("'"+loginUserName+"',");
				query_for_o_setup.append("CURRENT TIMESTAMP ),");
				
				query_for_o_setup.append("( "+warehouseId +",");
				query_for_o_setup.append("'CONF_AUTO_DISCOUNT_VALUE',");
				query_for_o_setup.append("0 ,");
				query_for_o_setup.append("'"+loginUserName+"',");
				query_for_o_setup.append("CURRENT TIMESTAMP ),");
				
				query_for_o_setup.append("( "+warehouseId +",");
				query_for_o_setup.append("'CONF_AUTO_LAGERORT',");
				query_for_o_setup.append("0 ,");
				query_for_o_setup.append("'"+loginUserName+"',");
				query_for_o_setup.append("CURRENT TIMESTAMP ),");
				
				query_for_o_setup.append("( "+warehouseId +",");
				query_for_o_setup.append("'CONF_AUTO_LAGERORT_VALUE',");
				query_for_o_setup.append("0 ,");
				query_for_o_setup.append("'"+loginUserName+"',");
				query_for_o_setup.append("CURRENT TIMESTAMP ),");
				
				query_for_o_setup.append("( "+warehouseId +",");
				query_for_o_setup.append("'CONF_AUTO_MARKE',");
				query_for_o_setup.append("0 ,");
				query_for_o_setup.append("'"+loginUserName+"',");
				query_for_o_setup.append("CURRENT TIMESTAMP ),");
				
				query_for_o_setup.append("( "+warehouseId +",");
				query_for_o_setup.append("'CONF_AUTO_MARKE_VALUE',");
				query_for_o_setup.append("0 ,");
				query_for_o_setup.append("'"+loginUserName+"',");
				query_for_o_setup.append("CURRENT TIMESTAMP ),");
				
				query_for_o_setup.append("( "+warehouseId +",");
				query_for_o_setup.append("'LFS_AUTO_MOVE_EDN',");
				query_for_o_setup.append("0 ,");
				query_for_o_setup.append("'"+loginUserName+"',");
				query_for_o_setup.append("CURRENT TIMESTAMP ),");
				
				query_for_o_setup.append("( "+warehouseId +",");
				query_for_o_setup.append("'LFS_AUTO_MOVE_BSN_ET',");
				query_for_o_setup.append("0 ,");
				query_for_o_setup.append("'"+loginUserName+"',");
				query_for_o_setup.append("CURRENT TIMESTAMP ),");
				
				query_for_o_setup.append("( "+warehouseId +",");
				query_for_o_setup.append("'LFS_AUTO_PRINT_EDN',");
				query_for_o_setup.append("0 ,");
				query_for_o_setup.append("'"+loginUserName+"',");
				query_for_o_setup.append("CURRENT TIMESTAMP )");
				
				dbServiceRepository.insertResultUsingQuery(query_for_o_setup.toString());
				
				con.commit();
				con.setAutoCommit(true);
				
			}
			
			adminWarehouse_obj.setWarehouseId(String.valueOf(warehouseId));

		} catch (AlphaXException ex) {
			transactionRollback(con, ex);
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.CREATE_FAILED_MSG_KEY, "New Warehouse (Lager)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY,
					"New Warehouse (Lager)"), exception);
			
			transactionRollback(con, exception);
			
			throw exception;
		}finally{
			connectionClose(con);
		}

		return adminWarehouse_obj;
	}
	
	
	/**
	 * This method is used to update the Warehouse details into DB
	 * @return Map
	 */
	@Override
	public Map<String, Boolean> updateWarehouseDetails(AdminWarehouseDTO warehouse_obj, String schema, String dataLibrary, String loginName) {

		log.info("Inside updateWarehouseDetails method of AdminSettingsImpl");
		Map<String, Boolean> programOutput = new HashMap<String, Boolean>();
		Connection con = null;
		try {
			if (warehouse_obj != null && warehouse_obj.getWarehouseId() != null && !warehouse_obj.getWarehouseId().isEmpty() ) {
				
				StringBuilder query = new StringBuilder("UPDATE ").append(schema).append(".O_WRH ");
				query.append(" SET AP_WAREHOUS_ID = ").append("'"+warehouse_obj.getAlphaPlusWarehouseId()+"'");
				query.append(", NAME =  ").append("'" + warehouse_obj.getWarehouseName() + "'");
				query.append(", CITY  = ").append("'" + warehouse_obj.getCity() + "'");
				query.append(", POST_CODE= ").append("'" + warehouse_obj.getPostalCode() + "'");
				query.append(", STREETANDNUM= ").append("'" + warehouse_obj.getStreetNumber() + "'");
				query.append(", UPDATED_TS = CURRENT TIMESTAMP");
				query.append(", UPDATED_BY = ").append("'"+loginName+"'");
				query.append(", VF_NUMBER = ").append(warehouse_obj.getVfNumber());
				query.append(" WHERE ISACTIVE = 1 AND WAREHOUS_ID = ").append(warehouse_obj.getWarehouseId());

				con = dbServiceRepository.getConnectionObject();
				con.setAutoCommit(false);
				
				StringBuilder checkWarehouseName = new StringBuilder("SELECT COUNT(*) AS COUNT FROM ").append(schema).append(".O_WRH ");
				checkWarehouseName.append(" WHERE ( WAREHOUS_ID <> "+warehouse_obj.getWarehouseId()+" AND UPPER(NAME) = ").append("UPPER('"+warehouse_obj.getWarehouseName()+"') ) OR ( WAREHOUS_ID <> "+warehouse_obj.getWarehouseId()+" AND AP_WAREHOUS_ID = ").append("'"+warehouse_obj.getAlphaPlusWarehouseId()+"' )");
				
				String count = dbServiceRepository.getCountUsingQuery(checkWarehouseName.toString(),con);
				if(Integer.parseInt(count) > 0){
					log.info("Duplicate Agency Name");
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.UNIQUE_CONSTRAINT_FAILED_MSG_KEY, "Warehouse or AP_warehouseId"));
					log.error(messageService.getReadableMessage(ExceptionMessages.UNIQUE_CONSTRAINT_FAILED_MSG_KEY, "Warehouse or AP_warehouseId"));
					throw exception;
					
				}
				
				boolean updateFlag = dbServiceRepository.updateResultUsingQuery(query.toString(),con);

				if (!updateFlag) {
					log.info("Unable to update Warehouse details for admin setting for this Id :"
							+ warehouse_obj.getWarehouseId());
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(
							LocaleContextHolder.getLocale(), ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "Warehouse (Lager)"));
					log.error(messageService.getReadableMessage(ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "Warehouse (Lager)"));
					throw exception;
				}
				con.commit();
				con.setAutoCommit(true);
				programOutput.put("isUpdated", true);
			} else {
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Warehouse (Lager) details"));
				log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Warehouse (Lager) details"),
						exception);
				throw exception;
			}
		} catch (AlphaXException ex) {
			transactionRollback(con, ex);
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Warehouse (Lager) details"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY,
					"Warehouse (Lager) details"), exception);
			transactionRollback(con, exception);
			throw exception;
		}finally{
			connectionClose(con);
		}

		return programOutput;
	}
	
	
	/**
	 * This method is used to delete the Warehouse details from DB
	 * @return Map
	 */
	@Override
	public Map<String, Boolean> deleteWarehouseDetails(String warehouseId, String schema, String dataLibrary, String loginName) {

		log.info("Inside deleteWarehouseDetails method of AdminSettingsImpl");
		Map<String, Boolean> programOutput = new HashMap<String, Boolean>();
		Connection con = null;
		try {
			StringBuilder query = new StringBuilder(" DELETE FROM ").append(schema).append(".O_WRH");
			query.append(" WHERE WAREHOUS_ID = ").append(warehouseId);
			
			con = dbServiceRepository.getConnectionObject();
			con.setAutoCommit(false);
			boolean updateFlag = dbServiceRepository.deleteResultUsingQuery(query.toString(),con);

			if (!updateFlag) {
				log.info("Unable to delete this Warehouse for admin setting for this Id :"
						+ warehouseId);
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(
						LocaleContextHolder.getLocale(), ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "Warehouse (Lager)"));
				log.error(messageService.getReadableMessage(ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "Warehouse (Lager)"));
				throw exception;
			}
			
			StringBuilder delete_query = new StringBuilder(" DELETE FROM ").append(schema).append(".O_SETUP");
			delete_query.append(" WHERE COMPANY_ID=0 AND AGENCY_ID=0 AND ROLE_ID=0 AND PRINTER_ID=0 AND USER_ID = 0 AND WAREHOUS_ID =  ").append(warehouseId);
			
			boolean deleteFlag = dbServiceRepository.deleteResultUsingQuery(delete_query.toString(),con);
			
			if (!deleteFlag) {
				log.info("Warehouse setting not available in o_setup for this Id :"
						+ warehouseId);
			}
			
			con.commit();
			con.setAutoCommit(true);
			programOutput.put("isDeleted", true);

		} catch (AlphaXException ex) {
			transactionRollback(con, ex);
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.DELETE_ADMIN_SETTING_FAILED_MSG_KEY, "Warehouse (Lager)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.DELETE_ADMIN_SETTING_FAILED_MSG_KEY,
					"Warehouse (Lager)"), exception);
			transactionRollback(con, exception);
			throw exception;
		}finally{
			connectionClose(con);
		}

		return programOutput;
	}
	
	/**
	 * This method is used to get the User List that are not assigned to any role for current agency from DB
	 * @return List
	 */

	@Override
	public List<AdminUsersDTO> getUsers_NotAssignedToAnyRoleForCurrentAgecy(String schema, String dataLibrary,
			String agencyId, String roleId) {

		log.info("Inside getUsers_NotAssignedToAnyRoleForCurrentAgecy method of AdminSettingsImpl");

		List<AdminUsersDTO> userList = new ArrayList<>();
		
		try {
			
			StringBuilder query = new StringBuilder(" SELECT  U.USER_ID, U.FIRSTNAME, U.LASTNAME, U.ISACTIVE ,(SELECT USER_ID FROM ").append(schema).append(".O_USERRL WHERE U.USER_ID = USER_ID AND AGENCY_ID = ").append(agencyId).append(" ) as USER_HAVE_ROLE ");
			query.append(" FROM ").append(schema).append(".O_USER U   WHERE ISACTIVE = 1 ");
			query.append("AND USER_ID NOT IN (SELECT USER_ID FROM ").append(schema).append(".O_USERRL ");
			query.append(" WHERE ISACTIVE = 1 AND AGENCY_ID = ").append(agencyId).append(" AND ROLE_ID = ").append(roleId + ")");
			query.append(" order by upper(U.LASTNAME), upper(U.FIRSTNAME) ");
			
			List<AdminUsers> adminUserList = dbServiceRepository.getResultUsingQuery(AdminUsers.class, query.toString(), true);

			if (adminUserList != null && !adminUserList.isEmpty()) {
				
				for(AdminUsers adminUsers : adminUserList){
					AdminUsersDTO userDTO = new AdminUsersDTO();
					userDTO.setUserId(String.valueOf(adminUsers.getUserId()));
					userDTO.setUserFirstName(adminUsers.getUserFirstName());
					userDTO.setUserLastName(adminUsers.getUserLastName());
					userDTO.setActive(adminUsers.isActive().compareTo(new BigDecimal("1"))==0?true:false);
					
					if(adminUsers.getUserHaveRole() != null && !adminUsers.getUserHaveRole().isEmpty()){
						userDTO.setRoleAssigned(true);
					}else{
						userDTO.setRoleAssigned(false);
					}
					
					userList.add(userDTO);
				}
			}else{
				log.info("User that are not assigned to any role for current agency is not available ");
			} 
			
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "User list that are not assigned to any role for current agency"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"User list that are not assigned to any role for current agency"), exception);
			throw exception;
		}
		
		return userList;
	}
	
	
	/**
	 * This method is used to get the User List that are assigned to selected role for current agency from DB
	 * @return List
	 */

	@Override
	public List<Admin_UserRoleMappingDTO> getUsers_AssignedToSelectedRoleForCurrentAgecy(String schema, String dataLibrary,String agencyId,String roleId) {

		log.info("Inside getUsers_AssignedToSelectedRoleForCurrentAgecy method of AdminSettingsImpl");

		List<Admin_UserRoleMappingDTO> userList = new ArrayList<>();
		
		try {
			StringBuilder query = new StringBuilder(" SELECT  A.FIRSTNAME, A.LASTNAME , A.USER_ID, B.USERROLE_ID, A.ISACTIVE FROM ").append(schema).append(".O_USER A , ").append(schema).append(".O_USERRL B ");;
			query.append(" WHERE A.USER_ID = B.USER_ID AND A.ISACTIVE = 1 ");
			query.append(" AND B.AGENCY_ID = ").append(agencyId);
			query.append(" AND B.ROLE_ID = ").append(roleId);
			query.append(" order by upper(A.LASTNAME), upper(A.FIRSTNAME) ");
			
			
			List<Admin_Mapping> userRoleMappingList = dbServiceRepository.getResultUsingQuery(Admin_Mapping.class, query.toString(), true);

			if (userRoleMappingList != null && !userRoleMappingList.isEmpty()) {
				
				for(Admin_Mapping usersMapping : userRoleMappingList){
					Admin_UserRoleMappingDTO userRoleMappingDTO = new Admin_UserRoleMappingDTO();
					userRoleMappingDTO.setUserId(String.valueOf(usersMapping.getUserId()));
					userRoleMappingDTO.setUserRoleId(String.valueOf(usersMapping.getUserRoleId()));
					userRoleMappingDTO.setActive(usersMapping.isActive().compareTo(new BigDecimal("1"))==0?true:false);
					userRoleMappingDTO.setUserFirstName(usersMapping.getUserFirstName());
					userRoleMappingDTO.setUserLastName(usersMapping.getUserLastName());
					
					userList.add(userRoleMappingDTO);
				}
			}else{
				log.info("User that are assigned to selected role for current agency is not available ");
			} 
			
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "User list that are assigned to selected role for current agency"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"User list that are assigned to selected role for current agency"), exception);
			throw exception;
		}
		
		return userList;
	}
	
	
	/**
	 * This method is used to get the Warehouses List that are not assigned to current agency from DB
	 * @return List
	 */
	@Override
	public List<AdminWarehouseDTO> getWarehouses_NotAssignedToCurrentAgecy(String schema, String dataLibrary,String agencyId) {

		log.info("Inside getWarehouses_NotAssignedToCurrentAgecy method of AdminSettingsImpl");

		List<AdminWarehouseDTO> warehousesListDTO = new ArrayList<>();
		
		try {
			StringBuilder query = new StringBuilder(" SELECT  WAREHOUS_ID, NAME, CITY, ISACTIVE FROM ").append(schema).append(".O_WRH ");
			query.append(" WHERE ISACTIVE = 1 ");
			query.append("AND WAREHOUS_ID NOT IN (SELECT WAREHOUS_ID FROM ").append(schema).append(".O_AGNWRH ");
			query.append(" WHERE ISACTIVE = 1 AND AGENCY_ID = ").append(agencyId + ")");
			
			List<AdminWarehouse> warehousesList = dbServiceRepository.getResultUsingQuery(AdminWarehouse.class, query.toString(), true);

			if (warehousesList != null && !warehousesList.isEmpty()) {
				
				for(AdminWarehouse adminWarehouse : warehousesList){
					AdminWarehouseDTO warehouseDTO = new AdminWarehouseDTO();
					warehouseDTO.setWarehouseId(String.valueOf(adminWarehouse.getWarehouseId()));
					warehouseDTO.setWarehouseName(adminWarehouse.getWarehouseName());
					warehouseDTO.setCity(adminWarehouse.getCity());
					warehouseDTO.setActive(adminWarehouse.getActive().compareTo(new BigDecimal("1"))==0?true:false);
					
					warehousesListDTO.add(warehouseDTO);
				}
			}else{
				log.info("Warehouses that are not assigned to current agency is not available ");
			} 
			
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Warehouses list that are not assigned current agency"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Warehouses list that are not assigned to current agency"), exception);
			throw exception;
		}
		
		return warehousesListDTO;
	}
	
	
	/**
	 * This method is used to get the Warehouses List that are assigned to the current agency from DB
	 * @return List
	 */
	@Override
	public List<Admin_AgencyWarehouseMappingDTO> getWarehouses_AssignedToCurrentAgecy(String schema, String dataLibrary,String agencyId) {

		log.info("Inside getWarehouses_AssignedToCurrentAgecy method of AdminSettingsImpl");

		List<Admin_AgencyWarehouseMappingDTO> agencyWarehouseListDTO = new ArrayList<>();
		
		try {
			StringBuilder query = new StringBuilder(" SELECT  WAREHOUS_ID, AGENWRHS_ID, ISACTIVE FROM ").append(schema).append(".O_AGNWRH ");
			query.append(" WHERE ISACTIVE = 1 ");
			query.append(" AND AGENCY_ID = ").append(agencyId);
			
			
			
			List<Admin_Mapping> agencyWarehouseMappingList = dbServiceRepository.getResultUsingQuery(Admin_Mapping.class, query.toString(), true);

			if (agencyWarehouseMappingList != null && !agencyWarehouseMappingList.isEmpty()) {
				
				for(Admin_Mapping agencyWarehouseMapping : agencyWarehouseMappingList){
					Admin_AgencyWarehouseMappingDTO agencyWarehouseDTO = new Admin_AgencyWarehouseMappingDTO();
					agencyWarehouseDTO.setWarehousId(String.valueOf(agencyWarehouseMapping.getWarehousId()));
					agencyWarehouseDTO.setAgencyWarehousId(String.valueOf(agencyWarehouseMapping.getAgencyWarehousId()));
					agencyWarehouseDTO.setActive(agencyWarehouseMapping.isActive().compareTo(new BigDecimal("1"))==0?true:false);
					
					agencyWarehouseListDTO.add(agencyWarehouseDTO);
				}
			}else{
				log.info("Warehouses that are assigned to the current agency is not available ");
			} 
			
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Warehouses list that are assigned to current agency"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Warehouses list that are assigned to current agency"), exception);
			throw exception;
		}
		
		return agencyWarehouseListDTO;
	}
	
	
	/**
	 * This method is used to get the token List from Stub
	 * @return List
	 */
	@Override
	public List<DropdownObject> getTokensList() {

		log.info("Inside getTokensList method of AdminSettingsImpl");

		List<DropdownObject> tokenList = new ArrayList<>();
		tokenList = stubServiceRepository.getDDLValuesFromStub(StubServiceRepository.tokenValuesMap);
		return tokenList;
	}
	
	/**
	 * This method is used to get the alphaPlus companys List from DB
	 * @return List
	 */
	@Override
	public List<DropdownObject> getAlphaPlusCompanys(String dataLibrary) {

		log.info("Inside getAlphaPlusCompanys method of AdminSettingsImpl");
		List<DropdownObject> alphaPlusCompanys = new ArrayList<>();

		try {
			StringBuilder query = new StringBuilder("SELECT DISTINCT SUBSTR(KEYFLD, 11, 2) AS AP_COMPANY_ID FROM ");
			query.append(dataLibrary).append(".referenz  ").append(" WHERE KEYFLD LIKE '0000019903%' ");

			List<LoginAgencys> adminCompanyList = dbServiceRepository.getResultUsingQuery(LoginAgencys.class, query.toString(), true);

			if (adminCompanyList != null && !adminCompanyList.isEmpty()) {
				
				for(LoginAgencys adminCompany : adminCompanyList){
					DropdownObject dropdownObject = new DropdownObject();
					dropdownObject.setKey(adminCompany.getAlphaPlusCompanyId());
					dropdownObject.setValue(adminCompany.getAlphaPlusCompanyId());
					
					alphaPlusCompanys.add(dropdownObject);
				}
			}else{
				log.info("Company List is empty");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Admin Company (Firma)"));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Admin Company (Firma)"));
				throw exception;
			} 
			
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Admin Agency (Filiale) list"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Admin Agency (Filiale) list"), exception);
			throw exception;
		}
		
		
		return alphaPlusCompanys;
	}
	
	
	/**
	 * This method is used to get user default setting from DB
	 * @return Object
	 */
	
	@Override
	public AdminUserSettingDTO getUserDefaultSetting(String schema, String dataLibrary,String userId) {

		log.info("Inside getUserDetailList method of AdminSettingsImpl");

		AdminUserSettingDTO adminUserSettingDTO = new AdminUserSettingDTO();;
		
		try {
			StringBuilder query = new StringBuilder("SELECT KEY, VALUE  FROM ").append(schema).append(".O_SETUP ");
			query.append(" WHERE COMPANY_ID=0 AND AGENCY_ID=0 AND WAREHOUS_ID=0 AND ROLE_ID=0 AND PRINTER_ID=0 AND USER_ID = ").append(userId);
			
			List<AlphaXConfigurationKeysDetails> UserSettings = dbServiceRepository.getResultUsingQuery(AlphaXConfigurationKeysDetails.class, query.toString(), true);

			if (UserSettings != null && !UserSettings.isEmpty()) {
				
				for(AlphaXConfigurationKeysDetails defaultSetting : UserSettings){
					
					if(defaultSetting.getKey().equalsIgnoreCase("DEFAULT_AGENCY")){
						adminUserSettingDTO.setDefaultAgency(defaultSetting.getValue());
						
					}else if(defaultSetting.getKey().equalsIgnoreCase("DEFAULT_COMPANY")){
						adminUserSettingDTO.setDefaultCompany(defaultSetting.getValue());
						
					}else if(defaultSetting.getKey().equalsIgnoreCase("DEFAULT_WAREHOUSE")){
						
						adminUserSettingDTO.setDefaultWarehouse(defaultSetting.getValue());
					}
				}
				adminUserSettingDTO.setUserId(userId);
			}else{
				log.info("User List is empty");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "User Default Setting"));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "User Default Setting"));
				throw exception;
			} 
			
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "User Default Setting"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"User Default Setting"), exception);
			throw exception;
		}
		
		return adminUserSettingDTO;
	}
	
	/**
	 * This method is used to update the user setting into DB
	 * @return Object
	 */

	@Override
	public Map<String, Boolean> updateUserDefaultSetting(AdminUserSettingDTO adminUserSetting_obj, String schema, String loginName, String dataLibrary) {

		log.info("Inside updateUserDefaultSetting method of AdminSettingsImpl");
		Map<String, Boolean> programOutput = new HashMap<String, Boolean>();
		Connection con = null;
		try {
			if (adminUserSetting_obj != null && adminUserSetting_obj.getUserId() !=null && !adminUserSetting_obj.getUserId().isEmpty()) {
				
				StringBuilder agency_query = new StringBuilder("UPDATE ").append(schema).append(".O_SETUP ");
				agency_query.append(" SET VALUE =  ").append("'" + adminUserSetting_obj.getDefaultAgency() + "'");
				agency_query.append(", UPDATED_BY  = ").append("'" + loginName + "'");
				agency_query.append(", TIMESTAMP = ").append("CURRENT TIMESTAMP");
				agency_query.append(" WHERE COMPANY_ID=0 AND AGENCY_ID=0 AND WAREHOUS_ID=0 AND ROLE_ID=0 AND PRINTER_ID=0 ");
				agency_query.append(" AND KEY = 'DEFAULT_AGENCY' AND USER_ID  = ").append(adminUserSetting_obj.getUserId());
				
				StringBuilder company_query = new StringBuilder("UPDATE ").append(schema).append(".O_SETUP ");
				company_query.append(" SET VALUE =  ").append("'" + adminUserSetting_obj.getDefaultCompany() + "'");
				company_query.append(", UPDATED_BY  = ").append("'" + loginName + "'");
				company_query.append(", TIMESTAMP = ").append("CURRENT TIMESTAMP");
				company_query.append(" WHERE COMPANY_ID=0 AND AGENCY_ID=0 AND WAREHOUS_ID=0 AND ROLE_ID=0 AND PRINTER_ID=0 ");
				company_query.append(" AND KEY = 'DEFAULT_COMPANY' AND USER_ID  = ").append(adminUserSetting_obj.getUserId());
				
				StringBuilder warehouse_query = new StringBuilder("UPDATE ").append(schema).append(".O_SETUP ");
				warehouse_query.append(" SET VALUE =  ").append("'" + adminUserSetting_obj.getDefaultWarehouse() + "'");
				warehouse_query.append(", UPDATED_BY  = ").append("'" + loginName + "'");
				warehouse_query.append(", TIMESTAMP = ").append("CURRENT TIMESTAMP");
				warehouse_query.append(" WHERE COMPANY_ID=0 AND AGENCY_ID=0 AND WAREHOUS_ID=0 AND ROLE_ID=0 AND PRINTER_ID=0 ");
				warehouse_query.append(" AND KEY = 'DEFAULT_WAREHOUSE' AND USER_ID  = ").append(adminUserSetting_obj.getUserId());

				
				con = dbServiceRepository.getConnectionObject();
				con.setAutoCommit(false);
				
				boolean updateAgencyFlag = dbServiceRepository.updateResultUsingQuery(agency_query.toString(),con);
				boolean updateCompanyFlag = dbServiceRepository.updateResultUsingQuery(company_query.toString(),con);
				boolean updateWarehouseFlag = dbServiceRepository.updateResultUsingQuery(warehouse_query.toString(),con);

				if (!updateAgencyFlag && !updateCompanyFlag && !updateWarehouseFlag) {
					log.info("Unable to update user default setting for this Id :"
							+ adminUserSetting_obj.getUserId());
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(
							LocaleContextHolder.getLocale(), ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "User setting"));
					log.error(messageService.getReadableMessage(ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "User setting"));
					throw exception;
				}
				con.commit();
				con.setAutoCommit(true);
				programOutput.put("isUpdated", true);
			} else {
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
								ExceptionMessages.UPDATE_FAILED_MSG_KEY, "User default setting"));
				log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "User default setting"),
						exception);
				throw exception;
			}
		} catch (AlphaXException ex) {
			transactionRollback(con, ex);
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.UPDATE_FAILED_MSG_KEY, "User default setting"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY,
					"User default setting"), exception);
			
			transactionRollback(con, exception);
			
			throw exception;
		}finally{
			connectionClose(con);
		}
		
		return programOutput;
	}
	
	
	/**
	 * This method is used to get the Token values that are assigned to the current agency.
	 * @return 
	 */
	@Override
	public Map<String, String> getTokenBasedonAgency(String schema, String dataLibrary, String agencyId) {

		log.info("Inside getTokenBasedonAgency method of AdminSettingsImpl");

		Map<String, String> tokenValues = new HashMap<>();
		
		try {
			StringBuilder query = new StringBuilder(" SELECT  KEY, VALUE FROM ").append(schema).append(".O_SETUP WHERE KEY='GET_TOKEN' ");
			query.append(" AND AGENCY_ID = ").append(agencyId);
			
			List<AlphaXConfigurationKeysDetails> tokenValuesDB = dbServiceRepository.getResultUsingQuery(AlphaXConfigurationKeysDetails.class, query.toString(), true);

			if (tokenValuesDB != null && !tokenValuesDB.isEmpty()) {
				
				for(AlphaXConfigurationKeysDetails tokens : tokenValuesDB){
					
					tokenValues.put("tokenKey", tokens.getKey());
					tokenValues.put("tokenValue", tokens.getValue());
				}
			}
			else{
				log.info("Token assigned to the current agency is not available ");
			} 
			
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "token"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "token"), exception);
			throw exception;
		}
		
		return tokenValues;
	}
	
	
	/**
	 * This method is used for Create new token entry in O_SETUP table.
	 */
	@Override
	public Map<String, Boolean> createUpdateTokenBasedonAgency(String schema, String dataLibrary, String agencyId, String tokenValue, String loginName) {

		log.info("Inside createUpdateTokenBasedonAgency method of AdminSettingsImpl");
		Map<String, Boolean> dbOutput = new HashMap<String, Boolean>();

		try {

			StringBuilder query = new StringBuilder("MERGE INTO ").append(schema).append(".O_SETUP AS A ");
			query.append("USING (VALUES (0,").append(agencyId).append(", 0, 0, 0, 0, 'GET_TOKEN', '").append(tokenValue+"', '").append(loginName).append("' )) AS A_TMP ");
			query.append("(company_id, agency_id, warehous_id, role_id, printer_id, user_id, key, value, UPDATED_BY) ");
			query.append("ON A.AGENCY_ID = A_TMP.agency_id AND A.KEY='GET_TOKEN' ");

			query.append("WHEN MATCHED THEN ");
			query.append("UPDATE SET VALUE = '").append(tokenValue).append("', UPDATED_BY = '").append(loginName).append("', TIMESTAMP = CURRENT TIMESTAMP ");

			query.append("WHEN NOT MATCHED THEN ");
			query.append("INSERT (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, PRINTER_ID, USER_ID, ROLE_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP) ");
			query.append("VALUES (0, ").append(agencyId).append(", 0, 0, 0, 0, 'GET_TOKEN', '").append(tokenValue+"', '").append(loginName).append("', CURRENT TIMESTAMP) ");

			query.append(" ELSE IGNORE");

			dbServiceRepository.insertResultUsingQuery(query.toString());

			dbOutput.put("isCreate_Update", true);

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "token"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "token"), exception);
			throw exception;
		}

		return dbOutput;
	}
	
	
	/**
	 * This method is used to get the Agency List that are not assigned to current Warehouse from DB
	 * @return List
	 */
	@Override
	public List<AdminAgencyDTO> getAgencies_NotAssignedToCrntWarehouse(String schema, String dataLibrary, String warehouseId) {

		log.info("Inside getAgencies_NotAssignedToCrntWarehouse method of AdminSettingsImpl");

		List<AdminAgencyDTO> agencyListDTO = new ArrayList<>();
		
		try {
			StringBuilder query = new StringBuilder(" SELECT AGENCY_ID, NAME, CITY, POST_CODE, STREETANDNUM, COMPANY_ID, ISACTIVE FROM ").append(schema).append(".O_AGENCY ");
			query.append(" WHERE ISACTIVE = 1 ");
			query.append("AND AGENCY_ID NOT IN (SELECT AGENCY_ID FROM ").append(schema).append(".O_AGNWRH ");
			query.append(" WHERE ISACTIVE = 1 AND WAREHOUS_ID = ").append(warehouseId + ") order by AGENCY_ID");
			
			List<AdminAgency> agencyList = dbServiceRepository.getResultUsingQuery(AdminAgency.class, query.toString(), true);

			if (agencyList != null && !agencyList.isEmpty()) {
				
				for(AdminAgency adminAgencys : agencyList){
					AdminAgencyDTO agencyDTO = new AdminAgencyDTO();
					agencyDTO.setAgencyId(String.valueOf(adminAgencys.getAgencyId()));
					agencyDTO.setAgencyName(adminAgencys.getAgencyName());
					agencyDTO.setCity(adminAgencys.getCity());
					agencyDTO.setPostalCode(adminAgencys.getPostalCode());
					agencyDTO.setStreetNumber(adminAgencys.getStreetNumber());
					agencyDTO.setCompanyId(String.valueOf(adminAgencys.getCompanyId()));
					agencyDTO.setActive(adminAgencys.getActive().compareTo(new BigDecimal("1"))==0?true:false);
					
					agencyListDTO.add(agencyDTO);
				}
			}else{
				log.info("Agencies that are not assigned to current Warehouse is not available ");
			} 
			
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Agency list that are not assigned to current warehouse"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Agency list that are not assigned to current warehouse"), exception);
			throw exception;
		}
		
		return agencyListDTO;
	}
	
	
	/**
	 * This method is used to get the Agency List that are assigned to the current Warehouse from DB
	 * @return List
	 */
	@Override
	public List<AdminAgencyDTO> getAgencies_AssignedToCrntWarehouse(String schema, String dataLibrary, String warehouseId) {

		log.info("Inside getAgencies_AssignedToCrntWarehouse method of AdminSettingsImpl");

		List<AdminAgencyDTO> agencyListDTO = new ArrayList<>();
		
		try {
			
			StringBuilder query = new StringBuilder(" SELECT AGENCY_ID, NAME, CITY, POST_CODE, STREETANDNUM, COMPANY_ID, ISACTIVE FROM ").append(schema).append(".O_AGENCY ");
			query.append(" WHERE ISACTIVE = 1 ");
			query.append("AND AGENCY_ID IN (SELECT AGENCY_ID FROM ").append(schema).append(".O_AGNWRH ");
			query.append(" WHERE ISACTIVE = 1 AND WAREHOUS_ID = ").append(warehouseId + ") order by AGENCY_ID");
			
			List<AdminAgency> agencyList = dbServiceRepository.getResultUsingQuery(AdminAgency.class, query.toString(), true);

			if (agencyList != null && !agencyList.isEmpty()) {
				
				for(AdminAgency adminAgencys : agencyList){
					AdminAgencyDTO agencyDTO = new AdminAgencyDTO();
					agencyDTO.setAgencyId(String.valueOf(adminAgencys.getAgencyId()));
					agencyDTO.setAgencyName(adminAgencys.getAgencyName());
					agencyDTO.setCity(adminAgencys.getCity());
					agencyDTO.setPostalCode(adminAgencys.getPostalCode());
					agencyDTO.setStreetNumber(adminAgencys.getStreetNumber());
					agencyDTO.setCompanyId(String.valueOf(adminAgencys.getCompanyId()));
					agencyDTO.setActive(adminAgencys.getActive().compareTo(new BigDecimal("1"))==0?true:false);
					
					agencyListDTO.add(agencyDTO);
				}
			}else{
				log.info("Agencies that are assigned to the current Warehouse is not available ");
			} 
			
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Agency list that are assigned to current warehouse"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Agency list that are assigned to current warehouse"), exception);
			throw exception;
		}
		
		return agencyListDTO;
	}
	
	
	/**
	 * This method is used for create / update multiple Agencies mapping with Warehouse.
	 * @return Map<String, Boolean>
	 */
	@Override
	public Map<String, Boolean> createAgenciesMappingWithWRH(List<AdminAgencyDTO> agencyDTOList, String schema, String dataLibrary, String warehouseId, String userLoginName) {

		log.info("Inside createAgenciesMappingWithWRH method of AdminSettingsImpl");
		
		Map<String, Boolean> programOutput = new HashMap<String, Boolean>();
		programOutput.put("isUpdated", false);
		List<AdminAgencyWRHSMapping> agencyWRHSTempList = new ArrayList<>();
		boolean isAvailable;

		try {
			
			StringBuilder query = new StringBuilder(" SELECT AGENWRHS_ID, AGENCY_ID, WAREHOUS_ID FROM ").append(schema).append(".O_AGNWRH ");
			query.append(" WHERE ISACTIVE = 1 AND WAREHOUS_ID = ").append(warehouseId);
			
			List<AdminAgencyWRHSMapping> agencyWRHSMappingDBList = dbServiceRepository.getResultUsingQuery(AdminAgencyWRHSMapping.class, query.toString(), true);
			
			if(agencyDTOList != null && !agencyDTOList.isEmpty()) {
				
				for(AdminAgencyDTO agencyDTO : agencyDTOList) {
					isAvailable = false;
					
					if(agencyWRHSMappingDBList != null && !agencyWRHSMappingDBList.isEmpty()) {

						for(AdminAgencyWRHSMapping agencyWRHSDB : agencyWRHSMappingDBList) {

							if(agencyDTO.getAgencyId().equals(String.valueOf(agencyWRHSDB.getAgencyId()))) {

								StringBuilder update_query = new StringBuilder(" UPDATE ").append(schema).append(".O_AGNWRH");
								update_query.append(" SET UPDATED_TS = CURRENT TIMESTAMP , UPDATED_BY =  ");
								update_query.append("'" + userLoginName + "' WHERE AGENWRHS_ID = ");
								update_query.append(agencyWRHSDB.getAgencyWarehousId());

								dbServiceRepository.updateResultUsingQuery(update_query.toString());
								agencyWRHSTempList.add(agencyWRHSDB);
								isAvailable = true;
								break;
							}
						}
					}
				
					if(!isAvailable) {
						
						StringBuilder insert_query = new StringBuilder(" INSERT INTO ").append(schema).append(".O_AGNWRH");
						insert_query.append(" (AGENCY_ID, WAREHOUS_ID, ISACTIVE, CREATED_TS , CREATED_BY)  values ( ");
						insert_query.append(agencyDTO.getAgencyId()+ ",");
						insert_query.append(warehouseId + ",");
						insert_query.append("1 ,");
						insert_query.append("CURRENT TIMESTAMP ,");
						insert_query.append("'" + userLoginName + "' )");
						
						int agencyWarehouseId = dbServiceRepository.insertResultUsingQuery(insert_query.toString());
						
						if (agencyWarehouseId == 0) {
							AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
											ExceptionMessages.CREATE_FAILED_MSG_KEY, "Agency Warehouse Mapping"));
							log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY,"Agency Warehouse Mapping"), exception);
							throw exception;
						}
					}
				}
				
				agencyWRHSMappingDBList.removeAll(agencyWRHSTempList);
				
				//delete the mapping from O_AGNWRH table
				deleteMapping(agencyWRHSMappingDBList, schema);
				
			}
			else {				
				//delete the mapping from O_AGNWRH table
				deleteMapping(agencyWRHSMappingDBList, schema);
			}
			
			programOutput.put("isUpdated", true);
			
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Agency Warehouse Mapping"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY,
					"Agency Warehouse Mapping"), exception);
			throw exception;
		}
		
		return programOutput;
	}
	
	
	/**
	 * This method is used to delete the Agency Warehouse mapping.
	 * @param schema
	 * @param mapping
	 * @return
	 */
	private boolean deleteMapping(List<AdminAgencyWRHSMapping> agencyWRHSMappingDBList, String schema) {

		boolean deleteFlag = true;
		
		if(agencyWRHSMappingDBList != null && !agencyWRHSMappingDBList.isEmpty()) {

			for(AdminAgencyWRHSMapping mapping : agencyWRHSMappingDBList) {

				StringBuilder delete_query = new StringBuilder(" DELETE FROM ").append(schema).append(".O_AGNWRH");
				delete_query.append(" WHERE AGENWRHS_ID = ").append(mapping.getAgencyWarehousId());

				deleteFlag = dbServiceRepository.updateResultUsingQuery(delete_query.toString());

				if (!deleteFlag) {
					log.info("Unable to delete Agency Warehouse Mapping details for admin setting for this Id :"+ mapping.getAgencyWarehousId());
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "AgencyWarehousId"));
					log.error(messageService.getReadableMessage(ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY,
							"AgencyWarehousId"));
					throw exception;
				}
			}
		}

		return deleteFlag;
	}
	
	
	/**
	 * This method is used to get Settings for the selected Roles from DB.
	 * @return List
	 */
	@Override
	public List<AdminRoleSettingsDTO> getRoleSettingForModules(String schema, String dataLibrary, String roleId, String moduleName) {

		log.info("Inside getRoleSettingForModules method of AdminSettingsImpl");

		List<AdminRoleSettingsDTO> settingsListDTO = new ArrayList<>();
		
		try {
			 
			StringBuilder query = new StringBuilder("SELECT mod.MODULE_ID, msec.MODSEC_ID, msec.SECTION_NAME, msec.KEY, ");
			query.append(" (SELECT oset.VALUE FROM ").append(schema).append(".O_SETUP oset  where  msec.KEY = oset.KEY and oset.ROLE_ID=").append(roleId).append(") AS VALUE FROM "); 
			query.append(schema).append(".O_MODULE mod, ").append(schema).append(".O_MODSEC msec where mod.MODULE_ID = msec.MODULE_ID and mod.MODULE_NAME='");
			query.append(moduleName).append("'");
			
			List<AdminModuleSection> settingsList = dbServiceRepository.getResultUsingQuery(AdminModuleSection.class, query.toString(), true);

			if (settingsList != null && !settingsList.isEmpty()) {
				
				for(AdminModuleSection mod_Sections : settingsList){
					AdminRoleSettingsDTO settingsDTO = new AdminRoleSettingsDTO();
					
					settingsDTO.setModuleId(String.valueOf(mod_Sections.getModuleId()));
					settingsDTO.setModuleSectionId(String.valueOf(mod_Sections.getModuleSectionId()));
					settingsDTO.setSectionName(mod_Sections.getSectionName());
					settingsDTO.setKey(mod_Sections.getKey());
					
					if(mod_Sections.getValue() == null || mod_Sections.getValue().equals("0")) {
						settingsDTO.setValue("0");
					}
					else {
						settingsDTO.setValue(mod_Sections.getValue());
					}
					
					settingsListDTO.add(settingsDTO);
				}
			}else{
				log.info("Permission Settings are not available for current roles ");
			} 
			
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Settings list for Roles"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Settings list for Roles"), exception);
			throw exception;
		}
		
		return settingsListDTO;
	}
	
	
	/**
	 * This method is used for create / update Settings for the selected Roles in O_SETUP Table.
	 * @return Map<String, Boolean>
	 */
	@Override
	public Map<String, Boolean> updateRoleSettingsForModules(List<AdminRoleSettingsDTO> settingsDTOList, String schema, String dataLibrary, String roleId, String userLoginName) {

		log.info("Inside updateRoleSettingsForModules method of AdminSettingsImpl");
		
		Map<String, Boolean> programOutput = new HashMap<String, Boolean>();
		programOutput.put("isCreate_Update", false);

		try {
			if(settingsDTOList != null && !settingsDTOList.isEmpty()) {
				
				for(AdminRoleSettingsDTO settingsDTO : settingsDTOList) {

					StringBuilder query = new StringBuilder("MERGE INTO ").append(schema).append(".O_SETUP AS A ");
					query.append("USING (VALUES (0, 0, 0, ").append(roleId).append(", 0, 0, ").append("'" + settingsDTO.getKey() + "', ").append("'"+settingsDTO.getValue()+"', '").append(userLoginName).append("' )) AS A_TMP ");
					query.append("(company_id, agency_id, warehous_id, role_id, printer_id, user_id, key, value, UPDATED_BY) ");
					query.append("ON A.ROLE_ID = A_TMP.ROLE_ID AND A.KEY=").append("'" + settingsDTO.getKey() +"' ");

					query.append(" WHEN MATCHED THEN ");
					query.append(" UPDATE SET VALUE = '").append(settingsDTO.getValue()).append("', UPDATED_BY = '").append(userLoginName).append("', TIMESTAMP = CURRENT TIMESTAMP ");

					query.append(" WHEN NOT MATCHED THEN ");
					query.append(" INSERT (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, PRINTER_ID, USER_ID, ROLE_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP) ");
					query.append(" VALUES (0, 0, 0, 0, 0, ").append(roleId).append(", ").append("'"+ settingsDTO.getKey() +"', ").append("'"+settingsDTO.getValue()+"', '").append(userLoginName).append("', CURRENT TIMESTAMP) ");

					query.append(" ELSE IGNORE");

					dbServiceRepository.insertResultUsingQuery(query.toString());
					
				}
				programOutput.put("isCreate_Update", true);
			}
			else {				
				log.info("No Updates are available for Permission Settings. ");
			}
			
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Settings mapping with Roles"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY,
					"Settings mapping with Roles"), exception);
			throw exception;
		}
		
		return programOutput;
	}
	
	
	/**
	 * This method is used to get the Alpha-plus Lager (Warehouse) details List for DDL.
	 */
	@Override
	public List<LagerDetailsDTO> getAlphaPlusWarehouseList(String schema, String dataLibrary, String companyId, String agencyId, String warehouseId) {

		log.info("Inside getAlphaPlusWarehouseList method of AdminSettingsImpl");

		List<LagerDetailsDTO> lagerList = new ArrayList<>();

		try {
			if(warehouseId == null) {
				warehouseId = "0";
			}
			
			StringBuilder query = new StringBuilder("select KEY2, PNAME, PSTRAS, PLZ, PORT, PLZ_2, VFNR  from ");
			query.append(dataLibrary).append(".E_ETSTAMK4 where KEY2 not in ");
			query.append("(SELECT AP_WAREHOUS_ID FROM ").append(schema).append(".O_WRH where ISACTIVE=1 ) ");
			query.append(" OR KEY2 in (SELECT AP_WAREHOUS_ID FROM ").append(schema).append(".O_WRH where ISACTIVE=1 and WAREHOUS_ID = ").append(warehouseId);
			query.append(") order by KEY2");
				
			List<LagerDetails> lagerDetailsList = dbServiceRepository.getResultUsingQuery(LagerDetails.class, query.toString(), true);

			if(lagerDetailsList != null && !lagerDetailsList.isEmpty()) {
				for(LagerDetails lagerDetails : lagerDetailsList) {

					LagerDetailsDTO lagerDetailsDto = new LagerDetailsDTO();
					
					String postalcode_1 = lagerDetails.getPlz()==null?"":lagerDetails.getPlz().trim();
					String postalcode_2  = lagerDetails.getPlz_2()==null?"":lagerDetails.getPlz_2().trim();

					lagerDetailsDto.setWarehouseNo(StringUtils.leftPad(lagerDetails.getWarehouseNo().toString(),2,"0"));
					lagerDetailsDto.setWarehouseName(lagerDetails.getWarehouseName());
					lagerDetailsDto.setAddress(new StringBuilder(lagerDetails.getStreet()).append(", ")
							.append(postalcode_1+postalcode_2).append(" ").append(lagerDetails.getPort()).toString());
					lagerDetailsDto.setVfNumber(String.valueOf(lagerDetails.getVfNumber()));

					lagerList.add(lagerDetailsDto);
				}

			}else{
				log.info("Alphaplus Lagers Detail List is empty");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Lager (Warehouse)"));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Lager (Warehouse)"));
				throw exception;
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Lager Details (Warehouse)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Lager Details (Warehouse)"), exception);
			throw exception;
		}

		return lagerList;
	}
	
	private void transactionRollback (Connection con, AlphaXException ex) throws AlphaXException{
		
		try {
			if(con!=null){
			con.rollback();
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			throw ex;
		}
	}
	
	private void connectionClose(Connection con) {
		try {
			if(con!=null){
			con.close();
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	

	/**
	 * This method is used to get Employee Role list for selected Agency.
	 * @return List
	 */
	@Override
	public List<EmployeeRoleDTO> getEmployeeRoleList(String schema, String agencyId ) {

		log.info("Inside getEmployeeRoleList method of AdminSettingsImpl");

		List<EmployeeRoleDTO> employeeRoleListDTO = new ArrayList<>();
		
		try {
			 
			StringBuilder query = new StringBuilder("select USR.FIRSTNAME, USR.LASTNAME, RL.NAME  ");
			query.append("FROM ").append(schema).append(".O_USER USR, ").append(schema).append(".O_USERRL USRRL, ").append(schema).append(".O_ROLE RL ");
			query.append("WHERE USRRL.AGENCY_ID = ").append(agencyId).append(" ");
			query.append("and USR.USER_ID = USRRL.USER_ID ");
			query.append("and USRRL.ROLE_ID = RL.ROLE_ID ");
			query.append("and USRRL.ISACTIVE = 1 ");
			query.append("and USR.ISACTIVE = 1 ");
			query.append("and RL.ISACTIVE = 1 ");
			query.append("order by RL.NAME, USR.FIRSTNAME, USR.LASTNAME");			
			
			List<Admin_Mapping> employeeRoleList = dbServiceRepository.getResultUsingQuery(Admin_Mapping.class, query.toString(), true);

			if (employeeRoleList != null && !employeeRoleList.isEmpty()) {
				
				for(Admin_Mapping employee_roles : employeeRoleList){
					EmployeeRoleDTO employeeRoles = new EmployeeRoleDTO();
					
					employeeRoles.setFirstName(employee_roles.getUserFirstName());
					employeeRoles.setLastName(employee_roles.getUserLastName());
					employeeRoles.setRoleName(employee_roles.getRoleName());
										
					employeeRoleListDTO.add(employeeRoles);
				}
			}else{
				log.info("No Users are assigned to any Role under the Selected Agency ");
			} 
			
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Employee Role list for Agency"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Employee Role list for Agency"), exception);
			throw exception;
		}
		
		return employeeRoleListDTO;
	}
	

	/**
	 * This method is used for get Company's fiscal year end date details from DB.
	 * @return Object
	 */
	@Override
	public Map<String, String> getCompanyFiscalDateDetails(String dataLibrary, String companyId, String allowedWarehouses) {

		log.info("Inside getCompanyFiscalDateDetails method of AdminSettingsImpl");

		Map<String, String> fiscalDateDetails = new HashMap<>();

		try {

			fiscalDateDetails.put("fiscalEndDate", new StringBuilder("31/12/").append(LocalDate.now().getYear()).toString());
			fiscalDateDetails.put("differntDatesAvailable", "false");

			StringBuilder query = new StringBuilder("SELECT DATUMB FROM ").append(dataLibrary).append(".E_INVPARIP");
			query.append(" WHERE LGNR in(").append(allowedWarehouses).append(") order by LGNR fetch first row only");

			List<InventoryLagerDetails> fiscalDateList = dbServiceRepository.getResultUsingQuery(InventoryLagerDetails.class, query.toString(), true);

			if (fiscalDateList != null && !fiscalDateList.isEmpty()) {

				if(fiscalDateList.get(0).getFiscalDate()!=null && !fiscalDateList.get(0).getFiscalDate().trim().isEmpty() 
						&& !fiscalDateList.get(0).getFiscalDate().equals("0")) {

					fiscalDateDetails.put("fiscalEndDate", commonUtils.convertDateToString(fiscalDateList.get(0).getFiscalDate()));
				}
			}

			StringBuilder query_count = new StringBuilder("SELECT distinct DATUMB FROM ").append(dataLibrary).append(".E_INVPARIP WHERE ");
			query_count.append(" LGNR in(").append(allowedWarehouses).append(") ");

			List<InventoryLagerDetails> distinctFiscalDateList = dbServiceRepository.getResultUsingQuery(InventoryLagerDetails.class, query_count.toString(), true);

			if (distinctFiscalDateList != null && !distinctFiscalDateList.isEmpty() && distinctFiscalDateList.size() > 1) {
				fiscalDateDetails.put("differntDatesAvailable", "true");
			}


		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Company's fiscal end Date"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Company's fiscal end Date"), exception);
			throw exception;
		}

		return fiscalDateDetails;
	}
	
	
	/**
	 * This method is used for add/update the fiscal year end date details in the DB.
	 * @return Object
	 */
	@Override
	public Map<String, Boolean> createUpdateFiscalDateDetails(String dataLibrary, String allowedWarehouses, String companyId, String fiscalDate) {

		log.info("Inside createUpdateFiscalDateDetails method of AdminSettingsImpl");
		Map<String, Boolean> programOutput = new HashMap<>();
		try {

			//get list from E_INVPARIP
			StringBuilder queryForInvIP = new StringBuilder("SELECT LGNR, PGNR, VFNR FROM ").append(dataLibrary).append(".E_INVPARIP  Order by LGNR");				

			//save to arraylist
			List<InventoryLagerDetails> invIPWarehouseList = dbServiceRepository.getResultUsingQuery(InventoryLagerDetails.class, queryForInvIP.toString(), true);

			List<Integer> updatedinvIPWarehouseList = invIPWarehouseList.stream()
					.map(i -> i.getWarehouseNumber()).map(i-> Integer.parseInt(i))
					.collect(Collectors.toList());

			//Company allowed warehouse list
			List<Integer> updatedAllowedWarehouseList = Stream.of(allowedWarehouses.split(","))
					.map(String::trim).map(Integer::parseInt).collect(Collectors.toList());

			//compare for two list equality
			boolean isListEqual = updatedAllowedWarehouseList.equals(updatedinvIPWarehouseList);

			if(!isListEqual) {

				List<Integer> listforInsertion = updatedAllowedWarehouseList.stream()
						.filter(e -> !updatedinvIPWarehouseList.contains(e)) 
						.collect(Collectors.toList());
				log.info("Insertion warehouse: {}", listforInsertion);					

				if(listforInsertion != null && listforInsertion.size() > 0){
					insertRecords_INVPARIP(listforInsertion, dataLibrary);
				}
			}				

			updateINVPARIP_fiscalDate(dataLibrary, allowedWarehouses, fiscalDate);
			programOutput.put("isUpdated", true);

		}catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Company's fiscal end Date"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Company's fiscal end Date"), exception);
			throw exception;
		}

		return programOutput;

	}	
	
	
	private void insertRecords_INVPARIP(List<Integer> listForInsertion, String dataLibrary) {
		
		log.info("Inside insertRecords_INVPARIP method of AdminSettingsImpl");
		try (Connection con = dbServiceRepository.getConnectionObject();
				Statement stmt = con.createStatement();) {
			
			for(Integer warehouse: listForInsertion) {

				StringBuilder query = new StringBuilder(" INSERT INTO ").append(dataLibrary).append(".E_INVPARIP");
				query.append(" (LGNR, PGNR, KB, FILL01, VFNR, DATUM, DAT, WERT, S1, S2, S3, S4, S5, DATUMB, DIFLI_NUM, ETINV_DAT, DIFLI_NUM4, FILL02)  values ( ");
				query.append(warehouse).append(", 900, '', '', ").append(warehouse).append(", 0, 0, 1, 'J', 'J', 'J', 'J', 'J', 0, 0, 0, 0, '000' )");
				
				stmt.addBatch(query.toString());
			}
			
			int[] records =  dbServiceRepository.insertResultUsingBatchQuery(stmt);
			
			if(records != null){
				log.info("Total rows inserted : {} ", records.length);
			}
			con.commit();
			con.setAutoCommit(true);
			
		}catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Company's fiscal end Date"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Company's fiscal end Date"), exception);
			throw exception;
		}
	}
	
	
	private void updateINVPARIP_fiscalDate(String dataLibrary, String allowedWarehouses, String fiscalDate) {
		
		log.info("Inside updateINVPARIP_fiscalDate method of AdminSettingsImpl");		
		String format = "dd/MM/yyyy";
		
		try {
			if(commonUtils.checkDateFormat(fiscalDate, format) ) {

				StringBuilder update_query = new StringBuilder("UPDATE ").append(dataLibrary).append(".E_INVPARIP ");
				update_query.append(" SET DATUM = ").append("'" + commonUtils.getDateInDDMMYY(fiscalDate) + "'");
				update_query.append(", DAT = ").append("'" + commonUtils.getDateInDDMMYYYY(fiscalDate) + "'");
				update_query.append(", DATUMB = ").append("'" +commonUtils.getDateInDDMMYYYY(fiscalDate)+"'" );
				update_query.append(", ETINV_DAT = ").append("'" + commonUtils.getDateInYYYYMM(fiscalDate) + "'");
				update_query.append(" WHERE LGNR in(").append(allowedWarehouses).append(")");

				dbServiceRepository.updateResultUsingQuery(update_query.toString());
			}
			
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "fiscal end Date"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY,
					"fiscal end Date"), exception);
			throw exception;
		}
	}
	

	public void updateCentralFunctionForUser(String schema, String dataLibrary, String userId, boolean value, String loginName) {

		log.info("Inside Create/update central function value for user");
		String tokenValue = "0";
		if(value){
			tokenValue="1";
		}
	
			StringBuilder query = new StringBuilder("MERGE INTO ").append(schema).append(".O_SETUP AS A ");
			query.append("USING (VALUES (0, 0, 0, 0, 0,").append(userId).append(", 'ET_CENTRAL_FUNCTION', '").append(tokenValue+"', '").append(loginName).append("' )) AS A_TMP ");
			query.append("(company_id, agency_id, warehous_id, role_id, printer_id, user_id, key, value, UPDATED_BY) ");
			query.append("ON A.USER_ID = A_TMP.USER_ID AND A.KEY='ET_CENTRAL_FUNCTION' ");

			query.append("WHEN MATCHED THEN ");
			query.append("UPDATE SET VALUE = '").append(tokenValue).append("', UPDATED_BY = '").append(loginName).append("', TIMESTAMP = CURRENT TIMESTAMP ");

			query.append("WHEN NOT MATCHED THEN ");
			query.append("INSERT (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP) ");
			query.append("VALUES (0, 0, 0, 0, 0,").append(userId).append(", 'ET_CENTRAL_FUNCTION', '").append(tokenValue+"', '").append(loginName).append("', CURRENT TIMESTAMP) ");

			query.append(" ELSE IGNORE");

			dbServiceRepository.insertResultUsingQuery(query.toString());

	}
	
	public void updateCentralFunctionForAgency(String schema, String dataLibrary, String agencyId, boolean value, String loginName) {

		log.info("Inside Create/update central function value for Agency");
		String tokenValue = "0";
		if(value){
			tokenValue="1";
		}
	
			StringBuilder query = new StringBuilder("MERGE INTO ").append(schema).append(".O_SETUP AS A ");
			query.append("USING (VALUES (0, ").append(agencyId).append(", 0, 0,0, 0,").append(" 'BRANCH_HAS_HYUNDAI_ACCESS', '").append(tokenValue+"', '").append(loginName).append("' )) AS A_TMP ");
			query.append("(company_id, agency_id, warehous_id, role_id, printer_id, user_id, key, value, UPDATED_BY) ");
			query.append("ON A.AGENCY_ID = A_TMP.AGENCY_ID AND A.KEY='BRANCH_HAS_HYUNDAI_ACCESS' ");

			query.append("WHEN MATCHED THEN ");
			query.append("UPDATE SET VALUE = '").append(tokenValue).append("', UPDATED_BY = '").append(loginName).append("', TIMESTAMP = CURRENT TIMESTAMP ");

			query.append("WHEN NOT MATCHED THEN ");
			query.append("INSERT (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP) ");
			query.append("VALUES (0, ").append(agencyId).append(", 0, 0,0, 0,").append("'BRANCH_HAS_HYUNDAI_ACCESS', '").append(tokenValue+"', '").append(loginName).append("', CURRENT TIMESTAMP) ");
			query.append(" ELSE IGNORE");

			dbServiceRepository.insertResultUsingQuery(query.toString());

	}
	
}