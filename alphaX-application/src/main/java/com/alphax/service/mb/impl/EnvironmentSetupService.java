package com.alphax.service.mb.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.alphax.common.rest.message.service.MessageService;
import com.alphax.common.exception.AlphaXException;
import com.alphax.common.exception.ExceptionMessages;
import com.alphax.repository.CobolServiceRepository;
import com.alphax.service.mb.AS400UsersService;
import com.alphax.common.constants.AlphaXTokenConstants;
import com.alphax.common.constants.Program_Commands_Constants;
import com.alphax.util.RestURIConstants;

import com.ibm.as400.access.AS400;

import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ProgramParameter;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EnvironmentSetupService {

	@Autowired
	CobolServiceRepository cobolServiceRepository;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	AS400UsersService as400UsersService;
	
	@Value("${apt05.env.inuse}")
	private String apte05EnvInUse;

	/**
	 * This method is used to execute the COBOL program to set Environment for User
	 * 
	 * @param username
	 * @throws Exception
	 */
	public Map<String, String> setUserEnvironment(String username) throws Exception{

		ProgramParameter[] parmList;
		String output = null;
		Map<String, String> usersEnvDetails = new HashMap<String, String>();
		try {

			////##################  New for APTE05 / APTE08 ########################
			log.info("---------- ALXGUICL_NEU --------------------------------");
			parmList = new ProgramParameter[7];

			String alxaRc   = "     ";
			byte[] alxaRcB  = alxaRc.getBytes(Program_Commands_Constants.IBM_273);
			parmList[0] = new ProgramParameter(alxaRcB, 3);

			String alxaRcTxt  =  StringUtils.rightPad("", 120, " ");
			byte[] alxaRcTxtB   = alxaRcTxt.getBytes(Program_Commands_Constants.IBM_273);
			parmList[1] = new ProgramParameter(alxaRcTxtB, 3);

			String alxaCluser   = username;
			byte[] alxaCluserB  = alxaCluser.getBytes(Program_Commands_Constants.IBM_273);
			parmList[2]         = new ProgramParameter(alxaCluserB, 3);
			
			String alxaDataLib  = "          ";
			byte[] alxaDataLibB  = alxaDataLib.getBytes(Program_Commands_Constants.IBM_273);
			parmList[3]         = new ProgramParameter(alxaDataLibB, 3);
			
			String alxaSchema  = "          ";
			byte[] alxaSchemaB  = alxaSchema.getBytes(Program_Commands_Constants.IBM_273);
			parmList[4]         = new ProgramParameter(alxaSchemaB, 3);
			
			String alxaTestLib  = 	"          ";			
			if(apte05EnvInUse != null && apte05EnvInUse.equalsIgnoreCase("true")) {
				alxaTestLib  = "ALXADD01  ";
			}
			
			byte[] alxaTestLibB  = alxaTestLib.getBytes(Program_Commands_Constants.IBM_273);
			parmList[5]         = new ProgramParameter(alxaTestLibB, 3);
			
			String alxaLocId  = "          ";
			byte[] alxaLocIdB  = alxaLocId.getBytes(Program_Commands_Constants.IBM_273);
			parmList[6]         = new ProgramParameter(alxaLocIdB, 3);


			// CALL ALXGUICL.PGM
			List<String> outputList2 = cobolServiceRepository.executeProgram(parmList, 0, Program_Commands_Constants.SET_ENVIRONMENT_PROGRAM2);

			if(outputList2 != null && !outputList2.isEmpty()) {
				if(!outputList2.get(0).contains("Error")) {
					alxaRc = outputList2.get(0);
					log.info("ARC: {}", alxaRc.trim());

					output      =  outputList2.get(1);
					log.info("RCTXT: {}", output.trim());
					output      =  outputList2.get(2);
					log.info("CUSER: {}", output.trim());
					output = outputList2.get(0);


					log.info("Returncode : {}", outputList2.get(0).trim());
					usersEnvDetails.put("returnCode", outputList2.get(0).trim());

					log.info("Datalibrary  : {}", outputList2.get(3).trim());
					usersEnvDetails.put("dataLibrary", outputList2.get(3).trim());

					log.info("Schema    : {}", outputList2.get(4).trim());
					usersEnvDetails.put("schema", outputList2.get(4).trim());
				}
			}

			/*log.info("----------- RTVJOBPARM -------------------------------");
			parmList = new ProgramParameter[1];

			// RCTXT
			String rctxRc   = "                                        ";
			byte[] rctxRcB  = rctxRc.getBytes(Program_Commands_Constants.IBM_273);
			parmList[0] = new ProgramParameter(rctxRcB, 3);

			// CALL RTVJOBPARM.PGM
			List<String> outputList = cobolServiceRepository.executeProgram(parmList, 0, Program_Commands_Constants.SET_ENVIRONMENT_PROGRAM1);

			if(outputList != null && !outputList.isEmpty()) {
				if(!outputList.get(0).contains("Error")) {
					output = outputList.get(0);
				}
			}

			log.info("JOBPARM: {}", output);
			log.info("JOBPARM: STRDBG UPDPROD(*YES)");
			log.info("JOBPARM: DSPMODSRC");*/


			if (alxaRc.equals("00000")) {
				log.info("--- alphaX Environment [§RTVJOBACL] ----------------");

				parmList     = new ProgramParameter[16];
				parmList[0]  = new ProgramParameter(04);
				parmList[1]  = new ProgramParameter(10);
				parmList[2]  = new ProgramParameter(10);
				parmList[3]  = new ProgramParameter(10);
				parmList[4]  = new ProgramParameter(10);
				parmList[5]  = new ProgramParameter(10);
				parmList[6]  = new ProgramParameter(10);
				parmList[7]  = new ProgramParameter(02);
				parmList[8]  = new ProgramParameter(10);
				parmList[9]  = new ProgramParameter(02);
				parmList[10] = new ProgramParameter(10);
				parmList[11] = new ProgramParameter(10);
				parmList[12] = new ProgramParameter(01);
				parmList[13] = new ProgramParameter(01);
				parmList[14] = new ProgramParameter(10);
				parmList[15] = new ProgramParameter(10);


				// CALL §RTVJOBACL.PGM
				List<String> outputList3 = cobolServiceRepository.executeProgram(parmList, 0, Program_Commands_Constants.SET_ENVIRONMENT_PROGRAM3);

				if(outputList3 != null && !outputList3.isEmpty()) {
					if(!outputList3.get(0).contains("Error")) {
						output = outputList3.get(0);
						log.info("MANDANT: {}", output);

						output      =  outputList3.get(1);
						log.info("USER: {}", output.trim());

						output      =  outputList3.get(2);
						log.info("DTALIB: {}", output.trim());

						output = outputList3.get(3);
						log.info("PODLIB: {}", output.trim());

						output = outputList3.get(4);
						log.info("SCHEMA: {}", output.trim());

						output = outputList3.get(5);
						log.info("SAVLIB: {}", output.trim());
						
						output = outputList3.get(6);
						log.info("NOSAVLIB: {}", output.trim());

						output = outputList3.get(7);
						log.info("WSID: {}", output.trim());
						
						//set the WSID value in usersEnvDetails
						usersEnvDetails.put("WSID", output.trim());

						output = outputList3.get(8);
						log.info("WSPRT: {}", output.trim());

						output = outputList3.get(9);
						log.info("PRTKURZ: {}", output.trim());

						output = outputList3.get(10);
						log.info("SYSPRT: {}", output.trim());

						output = outputList3.get(11);
						log.info("JOBQ: {}", output.trim());

						output = outputList3.get(12);
						log.info("TYPE: {}", output.trim());

						output = outputList3.get(13);
						log.info("SUBTYPE: {}", output.trim());

						output = outputList3.get(14);
						log.info("OUTQ: {}", output.trim());

						output = outputList3.get(15);
						log.info("CLUSER: {}", output.trim());
						
						//set the PODLIB, SAVLIB, NOSAVLIB, WSPRT, SYSPRT, PRTKURZ value in usersEnvDetails
						usersEnvDetails.put(AlphaXTokenConstants.PODLIB, outputList3.get(3).trim());
						usersEnvDetails.put(AlphaXTokenConstants.SAVLIB, outputList3.get(5).trim());
						usersEnvDetails.put(AlphaXTokenConstants.NOSAVLIB, outputList3.get(6).trim());
						usersEnvDetails.put(AlphaXTokenConstants.WSPRT, outputList3.get(8).trim());
						usersEnvDetails.put(AlphaXTokenConstants.PRTKURZ, outputList3.get(9).trim());
						usersEnvDetails.put(AlphaXTokenConstants.SYSPRT, outputList3.get(10).trim());
						usersEnvDetails.put(AlphaXTokenConstants.MANDANT, outputList3.get(0).trim());
												
					}
				}

				/*log.info("--- alphaplus Environment $TRTVJOBACL -----------------");

				parmList     = new ProgramParameter[16];
				parmList[0]  = new ProgramParameter(04);
				parmList[1]  = new ProgramParameter(10);
				parmList[2]  = new ProgramParameter(10);
				parmList[3]  = new ProgramParameter(10);
				parmList[4]  = new ProgramParameter(10);
				parmList[5]  = new ProgramParameter(10);
				parmList[6]  = new ProgramParameter(10);
				parmList[7]  = new ProgramParameter(02);
				parmList[8]  = new ProgramParameter(10);
				parmList[9]  = new ProgramParameter(02);
				parmList[10] = new ProgramParameter(10);
				parmList[11] = new ProgramParameter(10);
				parmList[12] = new ProgramParameter(01);
				parmList[13] = new ProgramParameter(01);
				parmList[14] = new ProgramParameter(10);
				parmList[15] = new ProgramParameter(10);

				// CALL TRTVJOBACL.PGM
				List<String> outputList4 = cobolServiceRepository.executeProgram(parmList, 0, Program_Commands_Constants.SET_ENVIRONMENT_PROGRAM4);

				if(outputList4 != null && !outputList4.isEmpty()) {
					if(!outputList4.get(0).contains("Error")) {
						output = outputList4.get(0);
						log.info("MANDANT: {}", output);

						output = outputList4.get(1);
						log.info("USER: {}", output.trim());

						output = outputList4.get(2);
						log.info("DTALIB: {}", output.trim());

						output = outputList4.get(3);
						log.info("PODLIB: {}", output.trim());

						output = outputList4.get(4);
						log.info("SCHEMA: {}", output.trim());

						output = outputList4.get(5);
						log.info("SAVLIB: {}", output.trim());

						output = outputList4.get(6);
						log.info("NOSAVLIB: {}", output.trim());

						output = outputList4.get(7);
						log.info("WSID: {}", output.trim());

						output = outputList4.get(8);
						log.info("WSPRT: {}", output.trim());

						output = outputList4.get(9);
						log.info("PRTKURZ: {}", output.trim());

						output = outputList4.get(10);
						log.info("SYSPRT: {}", output.trim());

						output = outputList4.get(11);
						log.info("JOBQ: {}", output.trim());

						output = outputList4.get(12);
						log.info("TYPE: {}", output.trim());

						output = outputList4.get(13);
						log.info("SUBTYPE: {}", output.trim());

						output = outputList4.get(14);
						log.info("OUTQ: {}", output.trim());

						output = outputList4.get(15);
						log.info("CLUSER: {}", output.trim());

					}
				}*/
				if(apte05EnvInUse != null && apte05EnvInUse.equalsIgnoreCase("true")) {
					setRequiredCommands(cobolServiceRepository.getCommandCallObject());
				}
				
			}else{
				AlphaXException exception = new AlphaXException(outputList2.get(1).trim(),outputList2.get(0).trim(),RestURIConstants.MSG_TYPE_ERROR.toUpperCase(),LocaleContextHolder.getLocale().getLanguage().toUpperCase());
				log.info("Login failed - Error Code  : {}  Error Message:  {} ", outputList2.get(0).trim(),outputList2.get(1).trim());
				log.error(messageService.getReadableMessage(ExceptionMessages.LOGIN_FAILED_MSG_KEY), exception);
				AS400 as400 = as400UsersService.getAs400Object(); 
				as400.resetAllServices();
				throw exception;
			}
		}catch(AlphaXException alphaXException){
			throw alphaXException;
		}
		catch (Exception e) {
			AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.LOGIN_FAILED_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.LOGIN_FAILED_MSG_KEY), exception);
			throw exception;
		}

		return usersEnvDetails;
	} 

	
	/**
	 * This method is used to execute the COBOL program to get User Role and Company details.
	 *  
	 * @return List<String>
	 * @throws Exception
	 */
	/*public List<String>  getUserRoleAndCompany() throws Exception{
		ProgramParameter[] parmList;
		String output = null;
		List<String> userRoleAndCompanyList = null;
		try{

			parmList = new ProgramParameter[10];
			parmList[0] = new ProgramParameter(05);
			parmList[1] = new ProgramParameter(70);
			parmList[2] = new ProgramParameter(10);
			parmList[3] = new ProgramParameter(10);
			parmList[4] = new ProgramParameter(50);
			parmList[5] = new ProgramParameter(10);
			parmList[6] = new ProgramParameter(50);
			parmList[7] = new ProgramParameter(10);
			parmList[8] = new ProgramParameter(50);
			parmList[9] = new ProgramParameter(04);

			userRoleAndCompanyList = cobolServiceRepository.executeProgram(parmList, 0, Program_Commands_Constants.GET_USER_ROLE_AND_COMPANY);

			if(userRoleAndCompanyList != null && !userRoleAndCompanyList.isEmpty()) {
				if(!userRoleAndCompanyList.get(0).contains("Error")) {

					output = userRoleAndCompanyList.get(0);
					log.info("Returncode : {}", output);

					if(output.equalsIgnoreCase("00000")){

						output = userRoleAndCompanyList.get(1);
						log.info("Returnmessage : {}", output.trim());

						output = userRoleAndCompanyList.get(2);
						log.info("Username : {}", output.trim());

						output = userRoleAndCompanyList.get(3);
						log.info("Role ID : {}", output.trim());

						output = userRoleAndCompanyList.get(4);
						log.info("Role long name : {}", output.trim());

						output = userRoleAndCompanyList.get(5);
						log.info("Team ID : {}", output.trim());

						output = userRoleAndCompanyList.get(6);
						log.info("Team long name : {}", output.trim());

						output = userRoleAndCompanyList.get(7);
						log.info("Division ID : {}", output.trim());

						output = userRoleAndCompanyList.get(8);
						log.info("Division long name : {}", output.trim());

						output = userRoleAndCompanyList.get(9);
						log.info("Company  : {}",  output.trim());
						
						// need to comment out for release or APTE08 both
						//setRequiredCommands(cobolServiceRepository.getCommandCallObject());

					}else{
						String returnMsg = userRoleAndCompanyList.get(1);
						AlphaXException exception = new AlphaXException(returnMsg.trim(), AlphaXException.ENTITY_CAN_NOT_BE_ADDED,RestURIConstants.MSG_TYPE_ERROR.toUpperCase(),LocaleContextHolder.getLocale().getLanguage().toUpperCase());
						log.error(ExceptionMessages.RETURN_MESSAGE_FROM_OROLLE + returnMsg, exception);
						AS400 as400 = as400UsersService.getAs400Object(); 
						as400.resetAllServices();
						throw exception;
					}
				}
			}

		}catch(AlphaXException alphaXException){
			throw alphaXException;
		}
		catch (Exception e) {
			log.info("Error Message during getUserRoleAndCompany :"+e.getMessage());
			AlphaXException exception = new AlphaXException(e, ExceptionMessages.ENVIRONMENT_SETUP_FAILED, AlphaXException.ENTITY_CAN_NOT_BE_ADDED);
			log.error(ExceptionMessages.ENVIRONMENT_SETUP_FAILED, exception);
			throw exception;
		}
		return userRoleAndCompanyList;

	}	*/


	private void setRequiredCommands(CommandCall cmd) {

		//========== Start of APT08 commands ============================
		//This lib for ORELLE..If New ALXACL with 7 parameter  working fine then we can remove below APTE08_LIBRAY_COMMAND2.
//		cobolServiceRepository.executeCommand(cmd, Program_Commands_Constants.APTE08_LIBRAY_COMMAND1);
		//========== End of APT08 commands ============================


		//========== Start of APT05 commands ============================

		cobolServiceRepository.executeCommand(cmd, Program_Commands_Constants.CREATE_CUSTOMER_COMMAND3);
		cobolServiceRepository.executeCommand(cmd, Program_Commands_Constants.CREATE_CUSTOMER_COMMAND4);
		cobolServiceRepository.executeCommand(cmd, Program_Commands_Constants.CREATE_CUSTOMER_COMMAND5);
		//cobolServiceRepository.executeCommand(cmd, Program_Commands_Constants.CREATE_CUSTOMER_COMMAND6);
		cobolServiceRepository.executeCommand(cmd, Program_Commands_Constants.CREATE_CUSTOMER_COMMAND7);
		cobolServiceRepository.executeCommand(cmd, Program_Commands_Constants.CREATE_CUSTOMER_COMMAND8);
		cobolServiceRepository.executeCommand(cmd, Program_Commands_Constants.CREATE_CUSTOMER_COMMAND9);
		cobolServiceRepository.executeCommand(cmd, Program_Commands_Constants.CREATE_CUSTOMER_COMMAND10);
		cobolServiceRepository.executeCommand(cmd, Program_Commands_Constants.CREATE_CUSTOMER_COMMAND11);
		cobolServiceRepository.executeCommand(cmd, Program_Commands_Constants.CREATE_CUSTOMER_COMMAND12);
		cobolServiceRepository.executeCommand(cmd, Program_Commands_Constants.CREATE_CUSTOMER_COMMAND13);
		cobolServiceRepository.executeCommand(cmd, Program_Commands_Constants.CREATE_CUSTOMER_COMMAND14);
		//cobolServiceRepository.executeCommand(cmd, Program_Commands_Constants.CREATE_CUSTOMER_COMMAND18);
		cobolServiceRepository.executeCommand(cmd, Program_Commands_Constants.CREATE_CUSTOMER_COMMAND15);

		//========== End of APT05 commands ============================
	}

}