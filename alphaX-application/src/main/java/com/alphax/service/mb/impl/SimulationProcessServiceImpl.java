package com.alphax.service.mb.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.alphax.common.rest.message.service.MessageService;
import com.alphax.common.exception.AlphaXException;
import com.alphax.common.exception.ExceptionMessages;
import com.alphax.repository.CobolServiceRepository;
import com.alphax.service.mb.SimulationProcessService;
import com.alphax.common.constants.Program_Commands_Constants;
import com.ibm.as400.access.ProgramParameter;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SimulationProcessServiceImpl extends BaseService implements SimulationProcessService {

	@Autowired
	private MessageService messageService;

	@Autowired
	CobolServiceRepository cobolServiceRepository;


	/**
	 * This method is used to create the random list of delivery Notes using COBOL Program.
	 */
	@Override
	public Map<String, String> generateDeliveryNotes( String dataLibrary, String companyId, String agencyId ) {
		log.info("Inside generateDeliveryNotes method of SimulationProcessServiceImpl");

		Map<String, String> programOutput = new HashMap<String, String>();
		ProgramParameter[] parmList = new ProgramParameter[4];
		try{
			
			// Create the input parameters 
			String returnCode = StringUtils.rightPad("", 5, " ");
			parmList[0] = new ProgramParameter(returnCode.getBytes(Program_Commands_Constants.IBM_273), 5);

			String returnMsg  = StringUtils.rightPad("", 100, " ");
			parmList[1] = new ProgramParameter(returnMsg.getBytes(Program_Commands_Constants.IBM_273), 100);

			String inParam1 = StringUtils.rightPad("BSNCRT", 6, " ");
			parmList[2] = new ProgramParameter(inParam1.getBytes(Program_Commands_Constants.IBM_273), 2);

			String randomNo = String.valueOf(getRandomNumber(15,20));
			String inDeliveryNoteCount = StringUtils.leftPad(randomNo, 2, "0");
			parmList[3] = new ProgramParameter(inDeliveryNoteCount.getBytes(Program_Commands_Constants.IBM_273), 2);
			log.info("Input delivery Note count: {}", inDeliveryNoteCount);
			
			//execute the COBOL program - OLS200CL.PGM
			List<String> outputList = cobolServiceRepository.executeProgram(parmList, 0, Program_Commands_Constants.SIMULATE_DELIVERYNOTES_PROGRAM );

			if(outputList != null && !outputList.isEmpty()) {
				if(!outputList.get(0).contains("Error")) {
					programOutput.put("returnCode", outputList.get(0).trim());
					programOutput.put("returnMsg", outputList.get(1).trim());
					programOutput.put("insertCount", outputList.get(3).trim());
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.SIMULATION_PROCESS_FAILED_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.SIMULATION_PROCESS_FAILED_MSG_KEY), exception);
			throw exception;
		}

		return programOutput;
	}
	
	
	/**
	 * This method is used generate random numbers in a given range
	 * @param min
	 * @param max
	 * @return
	 */
	public int getRandomNumber(int min, int max) {
	    return (int) ((Math.random() * (max - min)) + min);
	}
	
	
	/**
	 * This method is used to Trigger a event to automatically process the delivery notes to BSN and ET using COBOL Program.
	 */
	@Override
	public Map<String, String> autoProcessDiveryNotes( String dataLibrary, String companyId, String agencyId ) {
		log.info("Inside autoProcessDiveryNotes method of SimulationProcessServiceImpl");

		Map<String, String> programOutput = new HashMap<String, String>();
		ProgramParameter[] parmList = new ProgramParameter[4];

		try{
			//validate the company Id 
			validateCompany(companyId);
			
			//validate the Agency Id 
			validateAgency(agencyId);

			// Create the input parameters 
			String returnCode = StringUtils.rightPad("00000", 5, " ");
			parmList[0] = new ProgramParameter(returnCode.getBytes(Program_Commands_Constants.IBM_273), 5);

			String returnMsg  = StringUtils.rightPad("", 100, " ");
			parmList[1] = new ProgramParameter(returnMsg.getBytes(Program_Commands_Constants.IBM_273), 100);

			String inFirma = StringUtils.leftPad(companyId, 2, "0");
			parmList[2] = new ProgramParameter(inFirma.getBytes(Program_Commands_Constants.IBM_273), 2);

			String inFilale = StringUtils.leftPad(agencyId, 2, "0");
			parmList[3] = new ProgramParameter(inFilale.getBytes(Program_Commands_Constants.IBM_273), 2);

			//execute the COBOL program - OMVELFSCL.PGM
			List<String> outputList = cobolServiceRepository.executeProgram(parmList, 0, Program_Commands_Constants.AUTO_PROCESS_DELIVERYNOTES_PROGRAM );

			if(outputList != null && !outputList.isEmpty()) {
				if(!outputList.get(0).contains("Error")) {
					programOutput.put(outputList.get(0).trim(), outputList.get(1).trim());
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.AUTO_PROCESS_FAILED_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.AUTO_PROCESS_FAILED_MSG_KEY), exception);
			throw exception;
		}

		return programOutput;
	}
	
	
	/**
	 * This method is used to create the random list of Conflict delivery Notes using COBOL Program.
	 */
	@Override
	public Map<String, String> generateConflictDeliveryNotes( String dataLibrary, String companyId, String agencyId ) {
		log.info("Inside generateConflictDeliveryNotes method of SimulationProcessServiceImpl");

		Map<String, String> programOutput = new HashMap<String, String>();
		ProgramParameter[] parmList = new ProgramParameter[4];
		try{
			
			// Create the input parameters 
			String returnCode = StringUtils.rightPad("", 5, " ");
			parmList[0] = new ProgramParameter(returnCode.getBytes(Program_Commands_Constants.IBM_273), 5);

			String returnMsg  = StringUtils.rightPad("", 100, " ");
			parmList[1] = new ProgramParameter(returnMsg.getBytes(Program_Commands_Constants.IBM_273), 100);

			String inParam1 = StringUtils.rightPad("BSNCRT", 6, " ");
			parmList[2] = new ProgramParameter(inParam1.getBytes(Program_Commands_Constants.IBM_273), 2);

			String randomNo = String.valueOf(getRandomNumber(5,10));
			String inDeliveryNoteCount = StringUtils.leftPad(randomNo, 2, "0");
			parmList[3] = new ProgramParameter(inDeliveryNoteCount.getBytes(Program_Commands_Constants.IBM_273), 2);
			log.info("Input Conflict delivery Note count: {}", inDeliveryNoteCount);
			
			//execute the COBOL program - OLS300CL.PGM
			List<String> outputList = cobolServiceRepository.executeProgram(parmList, 0, Program_Commands_Constants.SIMULATE_CONFLICT_DELIVERYNOTES_PROGRAM );

			if(outputList != null && !outputList.isEmpty()) {
				if(!outputList.get(0).contains("Error")) {
					programOutput.put("returnCode", outputList.get(0).trim());
					programOutput.put("returnMsg", outputList.get(1).trim());
					programOutput.put("insertCount", outputList.get(3).trim());
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.SIMULATION_CONFLICT_PROCESS_FAILED_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.SIMULATION_CONFLICT_PROCESS_FAILED_MSG_KEY), exception);
			throw exception;
		}

		return programOutput;
	}
	

}