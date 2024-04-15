/**
 * 
 */
package com.alphax.service.mb.impl;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;

import com.alphax.common.rest.message.service.MessageService;
import com.alphax.common.exception.AlphaXException;
import com.alphax.common.exception.ExceptionMessages;
import com.alphax.common.constants.Program_Commands_Constants;
import com.ibm.as400.access.ProgramParameter;

import lombok.extern.slf4j.Slf4j;

/**
 * @author A106744104
 *
 */
@Slf4j
public abstract class BaseService {
	
	@Autowired
	private MessageService messageService;
	
	
	/**
	 * This method is used to validate companyId. It's value range is between 1 and 9.
	 * @param companyId
	 * @return  boolean
	 */
	public boolean validateCompany(String companyId) {

		boolean isValid = true;

		if(Integer.parseInt(companyId) <= 0 || Integer.parseInt(companyId) >= 10 ){
			isValid = false;
			AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.COMPANYID_INVALID_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.COMPANYID_INVALID_MSG_KEY));
			throw exception;
		}
		return isValid;
	}
	
	
	/**
	 * This method is used to validate AgencyId. It's value range is between 01 and 99.
	 * @param agencyId
	 * @return  boolean
	 */
	public boolean validateAgency(String agencyId) {

		boolean isValid = true;

		if(Integer.parseInt(agencyId) <= 0 || Integer.parseInt(agencyId) >= 100 ){
			isValid = false;
			AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.AGENCYID_INVALID_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.AGENCYID_INVALID_MSG_KEY));
			throw exception;
		}
		return isValid;
	}
	
	
	/**
	 * This method is used to validate Page Size.
	 * @param totalRecords
	 * @return  boolean
	 */
	public boolean validatePageSize(Integer totalRecords) {

		boolean isValid = true;

		if(totalRecords > 100){
			log.info("pageSize is not valid");
			isValid = false;
			AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.PGSIZE_INVALID_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.PGSIZE_INVALID_MSG_KEY));
			throw exception;
		}
		return isValid;
	}
	
	
	/**
	 * This method is used to validate the input for COBOL Program, if input is optional and if it blank then send NULL (0x00) to COBOL PGM.
	 * @param input
	 * @return  byte[]
	 */
	public byte[] validateWithLowValues(String input) throws UnsupportedEncodingException {
		
		byte[] byteValues = null;
		
		if(StringUtils.containsOnly(input, " ") || StringUtils.containsOnly(input, "0")) {
			//This is for Null values
			byteValues = new byte[] { 0x00 };
		}
		else {
			byteValues = input.getBytes(Program_Commands_Constants.IBM_273);
		}
		
		return byteValues;
	}

	
	/**
	 * This method is used to validate the input for COBOL Program, if input is optional and if it blank then send NULL (0x00) to COBOL PGM.
	 * @param input
	 * @return  byte[]
	 */
	public ProgramParameter validateWithLowValues() throws UnsupportedEncodingException {
		
		byte[] byteValues = new byte[] { 0x00 };
		return new ProgramParameter(byteValues, 2);
	}
	
	
	/**
	 * This method is used to validate warehouseIds (check warehouseIds are not null).
	 * @param warehouseIds
	 * @return  boolean
	 */
	public boolean validateWarehouses(String warehouseIds) {

		boolean isValid = true;

		if(null == warehouseIds || warehouseIds.trim().isEmpty()) {
			isValid = false;
			AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.WAREHOUSE_INVALID_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.WAREHOUSE_INVALID_MSG_KEY));
			throw exception;
		}
		return isValid;
	}
	
	/**
	 * This method is used to validate AgencyIds (check AgencyIds are not null).
	 * @param warehouseIds
	 * @return  boolean
	 */
	public boolean validateAgencys(String agencyIds) {

		boolean isValid = true;

		if(null == agencyIds || agencyIds.trim().isEmpty()) {
			isValid = false;
			AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.ALLOWED_AGENCYID_INVALID_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.ALLOWED_AGENCYID_INVALID_MSG_KEY));
			throw exception;
		}
		return isValid;
	}

}
