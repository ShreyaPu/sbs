package com.alphax.service.mb.impl;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.alphax.common.rest.message.service.MessageService;
import com.alphax.model.mb.AdminUsers;
import com.alphax.model.mb.UserStandardInfo;
import com.alphax.common.exception.AlphaXException;
import com.alphax.common.exception.ExceptionMessages;
import com.alphax.repository.CobolServiceRepository;
import com.alphax.repository.DBServiceRepository;
import com.alphax.service.mb.AS400UsersService;
import com.alphax.service.mb.JwtAuthenticationService;
import com.alphax.vo.mb.AdminUsersDTO;
import com.alphax.vo.mb.PasswordChangedDTO;
import com.alphax.vo.mb.UserCompanyInfoDTO;
import com.alphax.vo.mb.UserPermissionsVO;
import com.alphax.vo.mb.UserStandardInfoDTO;
import com.alphax.common.constants.AlphaXTokenConstants;
import com.alphax.common.constants.Program_Commands_Constants;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ProgramParameter;
import com.ibm.as400.access.User;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtAuthenticationServiceImpl extends BaseService implements JwtAuthenticationService {

	@Autowired
	AS400UsersService as400UsersService;
	
	@Autowired
	CacheManager cacheManager;

	@Value("${alphax.host}")
	private String host;

	@Autowired
	EnvironmentSetupService environmentSetup;

	@Autowired
	CobolServiceRepository cobolServiceRepository;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	DBServiceRepository dbServiceRepository;
	
	@Autowired
	BuildProperties buildProperties;

	
	/**
	 * This method authenticates the user by executing following COBOL programs
	 * 
	 * Step 1: Check user-name & password using AS400  validateSignon() method
	 * Step 2: if successfully validated, start executing the COBOL program to set User Environment.
	 * Step 3: execute "ALXACL.PGM" Program
	 * Step 4: execute "RTVJOBPARM.PGM" Program
	 * Step 5: execute "§RTVJOBACL.PGM" Program (to get user information)
	 * Step 6: execute "TRTVJOBACL.PGM" Program (to get user information)
	 * Step 7: To get DataLibrary information  execute  "LZCHKACL.PGM" program.
	 * Step 8: To get Role and company information execute  "OROLLE.PGM" program.
	 * step 9: Add all the information in AS400 Cacheable object.
	 * 
	 */
	@Override
	public List<String> authenticate(String userLogin, String password , String computerName) {
		
		AS400 as400 = null;
		List<String> userRoleList = new ArrayList<>();
		String usernameWithIP = null;
		Map<String, String> userEnvDetails = null;
		int passwordExpireDay = 365;
		try{

			log.info("AlphaX release version : {}  ", buildProperties.getVersion());
			if (userLogin == null || userLogin.length() > 10 
					|| password == null || password.length() > 128) {

				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.CREDENTIALS_INVALID_MSG_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.CREDENTIALS_INVALID_MSG_KEY), exception);
				throw exception;
			}
			userLogin = userLogin.toUpperCase();
			usernameWithIP = userLogin+computerName;
			if(!as400UsersService.isUserAvailable(usernameWithIP)){

				log.info("##### User not available inside map ##### : {}", userLogin);
				as400 = new AS400(host, userLogin ,password);
				as400.validateSignon();
				User user = new User(as400,userLogin);
				//Date passwordExpireDate = user.getPasswordExpireDate();
				passwordExpireDay  = user.getDaysUntilPasswordExpire();
				if(passwordExpireDay >= 1 && passwordExpireDay <= 7) {
					as400.setPasswordExpirationWarningDays(-1);
				}
				as400UsersService.setAs400Object(as400);
				
			}else{
				log.info("###### User is available inside map #####");
				as400 = (AS400) as400UsersService.getAs400users(usernameWithIP);
				as400.validateSignon(password);
				User user = new User(as400,userLogin); 
				//Date passwordExpireDate = user.getPasswordExpireDate();
				 passwordExpireDay  = user.getDaysUntilPasswordExpire();
				if(passwordExpireDay >= 1 && passwordExpireDay <= 7) {
					as400.setPasswordExpirationWarningDays(-1);
				}
				as400UsersService.setAs400Object(as400);
			}	
					
			log.info("Start :Set User Environment");
			
			//method call to execute COBOL programs
			userEnvDetails = environmentSetup.setUserEnvironment(userLogin);
			
			log.info("End :Set User Environment");
			

			/*log.info("Start :Get User Role and Company");
			userRoleList = environmentSetup.getUserRoleAndCompany();
			
			if(userRoleList==null || userRoleList.isEmpty()){

				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.OROLLE_INVALID_MSG_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.OROLLE_INVALID_MSG_KEY), exception);
				throw exception;
			}
			log.info("End :Get User Role and Company");*/
			
			//Check user login name inside DB
			log.info("Start :Check User LoginName");
			AdminUsersDTO adminUsersDTO = checkUserLogin(userEnvDetails.get("schema"),userEnvDetails.get("dataLibrary"),userLogin); 
			log.info("End :Check User LoginName");
			
			// Check if an agency and role is assigned to the user
			log.info("Start :Check User Role and Agency");
			checkUserRoleAndAgency(userEnvDetails.get("schema"),userEnvDetails.get("dataLibrary"),adminUsersDTO.getUserId());
			log.info("End :Check User Role and Agency");
			
			
			/*log.info("Start :Get User Company and Agency");
			UserCompanyInfoDTO companyInfoDTO = getUserCompanyDetails(userEnvDetails.get("schema"),userEnvDetails.get("dataLibrary"),username);
			String companyId = "";
			
			if(companyInfoDTO.getCompanyId()==null || 
					companyInfoDTO.getCompanyId().isEmpty() || companyInfoDTO.getCompanyId().trim().equalsIgnoreCase("*")){
				companyId = "01";
			}else{
				companyId = companyInfoDTO.getCompanyId().trim();
			}
			
			log.info("End :Get User Company and Agency");*/
			
			
			
			if(null != userEnvDetails && !userEnvDetails.isEmpty()) {
				
				//Add return code for get data library
				userRoleList.add(userEnvDetails.get("returnCode"));

				//Add datalibrary for get data library
				userRoleList.add(userEnvDetails.get("dataLibrary"));

				//Add Schema for get data library
				userRoleList.add(userEnvDetails.get("schema"));
				
				//Add WSID in List
				userRoleList.add(userEnvDetails.get("WSID"));
				
				//Add PODLIB in List
				userRoleList.add(userEnvDetails.get(AlphaXTokenConstants.PODLIB));
				
				//Add SAVLIB in List
				userRoleList.add(userEnvDetails.get(AlphaXTokenConstants.SAVLIB));
				
				//Add NOSAVLIB in List
				userRoleList.add(userEnvDetails.get(AlphaXTokenConstants.NOSAVLIB));
				
				//Add WSPRT in List
				userRoleList.add(userEnvDetails.get(AlphaXTokenConstants.WSPRT));
				
				//Add SYSPRT in List
				userRoleList.add(userEnvDetails.get(AlphaXTokenConstants.SYSPRT));
				
				//Add PRTKURZ in List
				userRoleList.add(userEnvDetails.get(AlphaXTokenConstants.PRTKURZ));
				
				//Add login user name
				userRoleList.add(userLogin);
				
				//Add CompanyId in list
				userRoleList.add(adminUsersDTO.getUserId());
				
				//Add Agency in list
				userRoleList.add(adminUsersDTO.getUserFirstName()+" "+adminUsersDTO.getUserLastName());
				
				//Add Mandent
				userRoleList.add(userEnvDetails.get(AlphaXTokenConstants.MANDANT));
				
				//Add password Expiry Day in List
				userRoleList.add(String.valueOf(passwordExpireDay));				
			}
			
			as400UsersService.setAs400users(usernameWithIP, as400);


		} catch(AlphaXException alphaXException){
			throw alphaXException;

		} catch (AS400SecurityException e) {
			//as400.disconnectAllServices();
			log.info("Error Message during authenticate AS400SecurityException : {}", e.getMessage() );
			if(e.getMessage().contains("Password is expired.")) {
				AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.PASSWORD_EXPIRED_MSG_KEY,e.getMessage() ));
				log.error(messageService.getReadableMessage(ExceptionMessages.PASSWORD_EXPIRED_MSG_KEY,e.getMessage()), exception);
				throw exception;
			}
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.CREDENTIALS_INVALID_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.CREDENTIALS_INVALID_MSG_KEY), exception);
			throw exception;

		}catch (Exception e) {
			log.info("Error Message during authenticate : {}", e.getMessage());

			if (as400UsersService.isUserAvailable(usernameWithIP)) {
				log.info("#####  Remove user from cache ##### : {}", usernameWithIP);
				as400UsersService.getAs400Users().remove(usernameWithIP);
				cacheManager.getCache("as400Cache").evict(usernameWithIP);

			}
			if (as400 != null) {
				as400.disconnectAllServices();
			}

			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.ENV_SETUP_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.ENV_SETUP_MSG_KEY), exception);
			throw exception;
		}

		log.info("Exit forn JwtAuthenticationController authenticate() : {}", userLogin);

		return userRoleList;

	}

/*	@Override
	public UserPermissionsVO getUserPermissions(String dataLibrary, String companyId, String agencyId) {

		log.info("Inside getUserPermissions method of JwtAuthenticationServiceImpl");
		ProgramParameter[] parmList;
		UserPermissionsVO userPermission_obj = new UserPermissionsVO();

		log.info("companyId from OROLLE : {}", companyId);

		if (companyId.trim().length() == 3) {
			companyId = companyId.substring(0, 2);
		} else if (companyId.length() == 1) {
			companyId = "01";
		}


		String inputString = StringUtils.rightPad(companyId, 10, " ") 
				+ StringUtils.rightPad("", 10, " ")
				+ StringUtils.rightPad("000", 3, " ") 
				+ StringUtils.rightPad("000", 3, " ")
				+ StringUtils.rightPad("COCHECK", 7, " ") 
				+ StringUtils.rightPad("", 388, " ");

		log.info("CustomerOne Check inputString length : {}", inputString.length());
		log.info("CustomerOne Check inputString value : {}", inputString);

		try {
			parmList = new ProgramParameter[1];
			parmList[0] = new ProgramParameter(inputString.getBytes(Program_Commands_Constants.IBM_273),inputString.length());
			List<String> outputList = cobolServiceRepository.executeProgram(parmList, 0,Program_Commands_Constants.CUSTOMER_ONE_CHECK_PROGRAM);

			if (outputList != null && !outputList.isEmpty()) {
				if (!outputList.get(0).contains("Error")) {
					String returnCode = outputList.get(0).substring(346, 351);
					if (returnCode.equalsIgnoreCase("00000")) {
						userPermission_obj.setCustomerOne(false);
					} else if (returnCode.equalsIgnoreCase("38B03")) {
						userPermission_obj.setCustomerOne(true);
					}
				} else {
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.PERMISSION_FAILED_MSG_KEY));
					log.error(messageService.getReadableMessage(ExceptionMessages.PERMISSION_FAILED_MSG_KEY), exception);
					throw exception;
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message: {}", e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.PERMISSION_FAILED_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.PERMISSION_FAILED_MSG_KEY), exception);
			throw exception;
		}

		return userPermission_obj;
	}
*/
	
	@Override
	public UserPermissionsVO getUserPermissions(String dataLibrary, String companyId, String agencyId) {

		log.info("Inside getUserPermissions method of JwtAuthenticationServiceImpl");
		ProgramParameter[] parmList;
		UserPermissionsVO userPermission_obj = new UserPermissionsVO();

		log.info("companyId from REFERENZ : {}", companyId);

		/*if (companyId.trim().length() == 3) {
			companyId = companyId.substring(0, 2);
		} else if (companyId.length() == 1) {
			companyId = "01";
		}*/
		
		try {
			
			validateCompany(companyId);	
			companyId = StringUtils.leftPad(companyId, 2,"0");
			
			parmList = new ProgramParameter[4];
			parmList[0] = new ProgramParameter(05);
			parmList[1] = new ProgramParameter(100);
			parmList[2] = new ProgramParameter(10);
			parmList[3] = new ProgramParameter(01);
			
			String progRc   = "00000";
			byte[] progRcB  = progRc.getBytes(Program_Commands_Constants.IBM_273);
			parmList[0] = new ProgramParameter(progRcB, 3);

			String progRcTxt = StringUtils.rightPad("", 100, " ");
			byte[] progRcTxtB   = progRcTxt.getBytes(Program_Commands_Constants.IBM_273);
			parmList[1] = new ProgramParameter(progRcTxtB, 5);

			String progLocationId   = StringUtils.rightPad(companyId, 10, " ");
			byte[] progLocationIdB  = progLocationId.getBytes(Program_Commands_Constants.IBM_273);
			parmList[2]           = new ProgramParameter(progLocationIdB, 3);
			
			String progEditable  = "0";
			byte[] progEditableB  = progEditable.getBytes(Program_Commands_Constants.IBM_273);
			parmList[3]         = new ProgramParameter(progEditableB, 3);
			
			
			List<String> outputList = cobolServiceRepository.executeProgram(parmList, 0, Program_Commands_Constants.NEW_CUSTOMER_ONE_CHECK_PROGRAM);

			if (outputList != null && !outputList.isEmpty()) {
				if (!outputList.get(0).contains("Error")) {
					String returnCode = outputList.get(0);
					
					log.info("returnCode : {}", returnCode);
					
					if (returnCode.equalsIgnoreCase("00000")) {
						userPermission_obj.setCustomerOne(true);
					} else if (returnCode.equalsIgnoreCase("38B03")) {
						userPermission_obj.setCustomerOne(false);
					}
				} else {
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.PERMISSION_FAILED_MSG_KEY));
					log.error(messageService.getReadableMessage(ExceptionMessages.PERMISSION_FAILED_MSG_KEY), exception);
					throw exception;
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message: {}", e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.PERMISSION_FAILED_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.PERMISSION_FAILED_MSG_KEY), exception);
			throw exception;
		}

		return userPermission_obj;
	}

	@Override
	public UserStandardInfoDTO getUserStandardInfo(String schema, String dataLibrary, String companyId,
			String agencyId,String loginName) {
		log.info("Inside getUserStandardInfo method of JwtAuthenticationServiceImpl");
		
		UserStandardInfoDTO userStandardInfoDTO = new UserStandardInfoDTO();
		List<UserStandardInfo> userStandardInfoList = new ArrayList<>();
		/*
		 * company value may be like this 0101 or 01* or * 
		 * if company valye 0101 or 01* : then use only first two digit
		 * 
		 * if company valye * : then use 01 as company Id
		 * 
		 */
		try {
			
			log.info("companyId from REFERENZ : {}", companyId);

			/*if (companyId.trim().length() > 2) {
				companyId = companyId.substring(0, 2);
			} else if (companyId.trim().length() == 1) {
				companyId = "01";
			}*/
			
			
		validateCompany(companyId);	
		companyId = StringUtils.leftPad(companyId, 2,"0");

		StringBuilder userInfo_query_1 = new StringBuilder(" SELECT NRO_ROLLE FROM " ).append(schema).append(".UBF_NUTZRO WHERE ");
		userInfo_query_1.append(" UPPER(NRO_NUTZER) = ").append("UPPER('"+loginName+"')").append(" AND NRO_FIRMA = ").append("'"+companyId+"'");
		
		StringBuilder userInfo_query_2 = new StringBuilder(" SELECT BNU_VNAME,BNU_NNAME FROM " ).append(schema).append(".UBF_NUTZER WHERE ");
		userInfo_query_2.append(" UPPER(BNU_NUTZER) = ").append("UPPER('"+loginName+"')");
		
		StringBuilder userInfo_query_3 = new StringBuilder(" SELECT NTE_TEAM FROM " ).append(schema).append(".UBF_NUTZTE WHERE ");
		userInfo_query_3.append(" UPPER(NTE_NUTZER) = ").append("UPPER('"+loginName+"')").append(" AND NTE_FIRMA = ").append("'"+companyId+"'");
		
			
			userStandardInfoList = dbServiceRepository.getResultUsingQuery(UserStandardInfo.class, userInfo_query_1.toString(),true);
			if (userStandardInfoList != null && !userStandardInfoList.isEmpty()) {

				for (UserStandardInfo userInfo_obj : userStandardInfoList) {
					userStandardInfoDTO.setUserRole(userInfo_obj.getUserRole());
					userStandardInfoList = null;
				}

			} 
			
			userStandardInfoList = dbServiceRepository.getResultUsingQuery(UserStandardInfo.class, userInfo_query_2.toString(),true);
			if (userStandardInfoList != null && !userStandardInfoList.isEmpty()) {

				for (UserStandardInfo userInfo_obj : userStandardInfoList) {
					userStandardInfoDTO.setUserFirstName(userInfo_obj.getUserFirstName());
					userStandardInfoDTO.setUserLastName(userInfo_obj.getUserLastName());
					userStandardInfoList = null;
				}

			} 
			
			userStandardInfoList = dbServiceRepository.getResultUsingQuery(UserStandardInfo.class, userInfo_query_3.toString(),true);
			if (userStandardInfoList != null && !userStandardInfoList.isEmpty()) {

				for (UserStandardInfo userInfo_obj : userStandardInfoList) {
					userStandardInfoDTO.setUserTeam(userInfo_obj.getUserTeam());
					userStandardInfoList = null;
				}

			} 
			
		}catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "User standard information"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "User standard information"), exception);
			throw exception;
		}
		return userStandardInfoDTO;

	}
	
	
	/**
	 * This method is used to disconnect all COBOL service and logout from application.
	 * 
	 */
	@Override
	public boolean logoutApplication(String computerName) {
		log.info("Inside logoutApplication method of JwtAuthenticationServiceImpl");

		boolean isLogoutSuccess = true;

		try {
			AS400 as400 = as400UsersService.getAs400Object();

			if(as400 != null) {
				String usernameWithIP = as400UsersService.getAs400Object().getUserId()+computerName;
				CommandCall cmd = new CommandCall(as400);

				String jobId = cmd.getServerJob().getNumber();
				log.info("Logout JobNumber:- {}", jobId );

				as400.resetAllServices();
				cacheManager.getCache("as400Cache").evict(usernameWithIP);
				as400UsersService.getAs400Users().remove(usernameWithIP);
			}
		}
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			isLogoutSuccess = false;
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.LOGOUT_FAILED_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.LOGOUT_FAILED_MSG_KEY));
			throw exception;
		}

		return isLogoutSuccess;

	}
	
	
	@Override
	public UserCompanyInfoDTO getUserCompanyDetails(String schema, String dataLibrary, String loginName) {
		log.info("Inside getUserCompanyDetails method of JwtAuthenticationServiceImpl");
		
		UserCompanyInfoDTO companyInfoDTO = new UserCompanyInfoDTO();
		List<UserStandardInfo> userStandardInfoList = new ArrayList<>();

		StringBuilder userInfo = new StringBuilder(" SELECT BNU_FIRMA, BNU_FILIA  FROM " ).append(schema).append(".UBF_NUTZER WHERE ");
		userInfo.append(" UPPER(BNU_NUTZER) = ").append("UPPER('"+loginName+"')");
		
		try{
			
		userStandardInfoList = dbServiceRepository.getResultUsingQuery(UserStandardInfo.class, userInfo.toString(),true);
		if (userStandardInfoList != null && !userStandardInfoList.isEmpty()) {

			for (UserStandardInfo userInfo_obj : userStandardInfoList) {
				companyInfoDTO.setCompanyId(userInfo_obj.getCompanyId());
				companyInfoDTO.setAgencyId(userInfo_obj.getAgencyId());
			}

		} 
			
		}catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "User company information"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "User company information"), exception);
			throw exception;
		}
		return companyInfoDTO;

	}
	
	
	/**
	 * This method is used to check user loginName in o_user table
	 * @return Object
	 */
	
	@Override
	public AdminUsersDTO checkUserLogin(String schema, String dataLibrary,String loginName) {

		log.info("Inside checkUserLogin method of JwtAuthenticationServiceImpl");

		AdminUsersDTO adminUsersDTO = null;
		
		try {
			StringBuilder query = new StringBuilder("SELECT USER_ID, FIRSTNAME, LASTNAME, ISACTIVE FROM ").append(schema).append(".O_USER ");
			query.append(" WHERE ISACTIVE = 1 AND LOGIN = ").append("'"+loginName+"'");
			
			List<AdminUsers> adminUserList = dbServiceRepository.getResultUsingQuery(AdminUsers.class, query.toString(), true);

			if (adminUserList != null && !adminUserList.isEmpty() && adminUserList.size() == 1) {
				
				for(AdminUsers adminUsers : adminUserList){
					adminUsersDTO = new AdminUsersDTO();
					adminUsersDTO.setUserId(String.valueOf(adminUsers.getUserId()));
					adminUsersDTO.setActive(adminUsers.isActive().compareTo(new BigDecimal("1"))==0?true:false);
					adminUsersDTO.setUserFirstName(adminUsers.getUserFirstName());
					adminUsersDTO.setUserLastName(adminUsers.getUserLastName());
				}
			}else{
				//log.info("User login is not valid");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.INVALID_USER_LOGIN_MSG_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.INVALID_USER_LOGIN_MSG_KEY));
				throw exception;
			} 
			
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.INVALID_USER_LOGIN_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.INVALID_USER_LOGIN_MSG_KEY), exception);
			throw exception;
		}
		
		return adminUsersDTO;
	}
	
	/**
	 * This method is used to check the User role and agency from DB
	 * @return List
	 */

	@Override
	public void checkUserRoleAndAgency(String schema, String dataLibrary,String userId) {

		log.info("Inside checkUserRoleAndAgency method of JwtAuthenticationServiceImpl");

		try {
			StringBuilder query = new StringBuilder(" SELECT  COUNT(*) AS count FROM ").append(schema).append(".O_USERRL ");
			query.append(" WHERE USER_ID = ").append(userId);
			
			String count = dbServiceRepository.getCountUsingQuery(query.toString());

			if (Integer.parseInt(count) > 0) {
				log.info("User have agency and role");
			}else{
				log.info("Agency and role is not assigned to the user");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.INVALID_USER_ROLE_AGENCY_MSG_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.INVALID_USER_ROLE_AGENCY_MSG_KEY));
				throw exception;
			} 
			
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.INVALID_USER_ROLE_AGENCY_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.INVALID_USER_ROLE_AGENCY_MSG_KEY), exception);
			throw exception;
		}
		
	}
	
	@Override
	public List<String> alphaXSetupLogin(String userLogin, String password , String computerName) {
		
		AS400 as400 = null;
		List<String> userRoleList = new ArrayList<>();
		String usernameWithIP = null;
		Map<String, String> userEnvDetails = null;
		try{

			if (userLogin == null || userLogin.length() > 10 
					|| password == null || password.length() > 128) {

				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.CREDENTIALS_INVALID_MSG_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.CREDENTIALS_INVALID_MSG_KEY), exception);
				throw exception;
			}
			userLogin = userLogin.toUpperCase();
			usernameWithIP = userLogin+computerName;
			if(!as400UsersService.isUserAvailable(usernameWithIP)){

				log.info("##### User not available inside map ##### : {}", userLogin);
				as400 = new AS400(host, userLogin ,password);
				as400.validateSignon();
				as400UsersService.setAs400Object(as400);
				
			}else{
				log.info("###### User is available inside map #####");
				as400 = (AS400) as400UsersService.getAs400users(usernameWithIP);
				as400.validateSignon(password);
				as400UsersService.setAs400Object(as400);
			}	
			
			log.info("Start :Set User Environment");
			
			//method call to execute COBOL programs
			userEnvDetails = environmentSetup.setUserEnvironment(userLogin);
			
			log.info("End :Set User Environment");
			
			//Check Admin userId inside DB
			//log.info("Start :Check User LoginName");
			//checkAdminUserId(userEnvDetails.get("schema"),userEnvDetails.get("dataLibrary"),userLogin); 
			//log.info("End :Check User LoginName");
			
			if(null != userEnvDetails && !userEnvDetails.isEmpty()) {
				
				//Add return code for get data library
				userRoleList.add(userEnvDetails.get("returnCode"));

				//Add datalibrary for get data library
				userRoleList.add(userEnvDetails.get("dataLibrary"));

				//Add Schema for get data library
				userRoleList.add(userEnvDetails.get("schema"));
				
				//Add WSID in List
				userRoleList.add(userEnvDetails.get("WSID"));
				
				//Add login user name
				userRoleList.add(userLogin);
				
				//Add CompanyId in list
				userRoleList.add("1");
				
				//Add Agency in list
				userRoleList.add("Admin Admin");
				
			}

			as400UsersService.setAs400users(usernameWithIP, as400);


		} catch(AlphaXException alphaXException){
			throw alphaXException;

		} catch (AS400SecurityException e) {
			//as400.disconnectAllServices();
			log.info("Error Message during authenticate AS400SecurityException : {}", e.getMessage() );
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.CREDENTIALS_INVALID_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.CREDENTIALS_INVALID_MSG_KEY), exception);
			throw exception;

		}catch (Exception e) {
			log.info("Error Message during authenticate : {}", e.getMessage());

			if (as400UsersService.isUserAvailable(usernameWithIP)) {
				log.info("#####  Remove user from cache ##### : {}", usernameWithIP);
				as400UsersService.getAs400Users().remove(usernameWithIP);
				cacheManager.getCache("as400Cache").evict(usernameWithIP);

			}
			if (as400 != null) {
				as400.disconnectAllServices();
			}

			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.ENV_SETUP_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.ENV_SETUP_MSG_KEY), exception);
			throw exception;
		}

		log.info("Exit forn JwtAuthenticationController authenticate() : {}", userLogin);

		return userRoleList;

	}
	
	
	public boolean checkAdminUserId(String schema, String dataLibrary,String loginName) {

		log.info("Inside checkAdminUserId method of JwtAuthenticationServiceImpl");

		boolean isAdminUserExist = false;
		
		try {
			StringBuilder query = new StringBuilder("SELECT CREATED_BY,CREATED_TS  FROM ").append(schema).append(".O_USER ");
			query.append(" WHERE ISACTIVE = 1 AND USER_ID = 1 ");
			
			List<AdminUsers> adminUserList = dbServiceRepository.getResultUsingQuery(AdminUsers.class, query.toString(), true);

			if (adminUserList != null && !adminUserList.isEmpty() && adminUserList.size() == 1) {
				
				String createdBy = adminUserList.get(0).getCreatedBy();
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.ALPHAX_SETUP_FAILED_MSG_KEY, createdBy));
				log.error(messageService.getReadableMessage(ExceptionMessages.ALPHAX_SETUP_FAILED_MSG_KEY, createdBy));
				throw exception;
			}else{
				isAdminUserExist = false;
			} 
			
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.INVALID_USER_LOGIN_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.INVALID_USER_LOGIN_MSG_KEY), exception);
			throw exception;
		}
		
		return isAdminUserExist;
	}

	
	@Override
	public Map<String, Boolean> changePassword(PasswordChangedDTO passwordChangedDTO, String computerName) {
		
		Map<String, Boolean> changedPasswordFlag = new HashMap<String, Boolean>();
		changedPasswordFlag.put("isPasswordChanged", false);
		
		try {
		String loginUserId = passwordChangedDTO.getLoginUserId().toUpperCase();
		
		if(passwordChangedDTO.getNewPassword().contentEquals(passwordChangedDTO.getConfirmPassword())) {
		AS400 as400 = new AS400(host, loginUserId,passwordChangedDTO.getOldPassword());
        as400.changePassword(passwordChangedDTO.getOldPassword(), passwordChangedDTO.getNewPassword());
        
        if (as400UsersService.isUserAvailable(loginUserId+computerName)) {
			log.info("#####  Remove user from cache ##### : {}", loginUserId+computerName);
			as400UsersService.getAs400Users().remove(loginUserId+computerName);
			cacheManager.getCache("as400Cache").evict(loginUserId+computerName);

		}
		if (as400 != null) {
			as400.disconnectAllServices();
		}
		
        changedPasswordFlag.put("isPasswordChanged", true);
		}}catch(Exception e){
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY,e.getMessage()));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, e.getMessage()), exception);
			throw exception;
        }
		
		return changedPasswordFlag;
	}

	

	/**
	 * This method is used to terminate current java session
	 * 
	 */
	@Override
	public boolean killMe(String username, String password ) {
		log.info("Inside killMe method of JwtAuthenticationServiceImpl");

		boolean killMe = false;

		try {
			
			if(username==null || password==null || !username.equalsIgnoreCase("Installer") || !password.equalsIgnoreCase("1#2IcanDoThis") ) {
				
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.CREDENTIALS_INVALID_MSG_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.CREDENTIALS_INVALID_MSG_KEY), exception);
				throw exception;
				
			}
			RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();

	        // Get name representing the running Java virtual machine.
	        // It returns something like 35656@Krakatau. The value before
	        // the @ symbol is the PID.
	        String jvmName = bean.getName();
	        log.info("Name = " + jvmName);

	        // Extract the PID by splitting the string returned by the
	        // bean.getName() method.
	        long pid = Long.parseLong(jvmName.split("@")[0]);
	        log.info("PID  = " + pid);

	        String cmd = "kill -9 " + pid;
	       // Runtime.getRuntime().exec(cmd);
	        System.exit(0);
	        killMe = true;
		}
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.PASSWORD_EXPIRED_MSG_KEY,"to kill java process"));
			log.error(messageService.getReadableMessage(ExceptionMessages.PASSWORD_EXPIRED_MSG_KEY,"to kill java process"));
			throw exception;
		}

		return killMe;

	}
	
	
}