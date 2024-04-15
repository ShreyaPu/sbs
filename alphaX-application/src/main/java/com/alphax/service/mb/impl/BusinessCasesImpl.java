package com.alphax.service.mb.impl;

import java.sql.Connection;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.alphax.common.rest.message.Message;
import com.alphax.common.rest.message.service.MessageService;
import com.alphax.model.mb.AdminWarehouse;
import com.alphax.model.mb.AlphaXConfigurationKeysDetails;
import com.alphax.model.mb.BA25DefaultSetup;
import com.alphax.model.mb.BADeliveryNotes;
import com.alphax.model.mb.BookingAccount;
import com.alphax.model.mb.DeliveryNotes_BA;
import com.alphax.model.mb.FinalizationsBA;
import com.alphax.model.mb.InventoryParts;
import com.alphax.model.mb.MovementData;
import com.alphax.model.mb.PartCatalog;
import com.alphax.model.mb.PartDetails;
import com.alphax.model.mb.PartRelocation;
import com.alphax.model.mb.ReportSelections;
import com.alphax.model.mb.SearchParts2;
import com.alphax.model.mb.SupplierDetails;
import com.alphax.common.exception.AlphaXException;
import com.alphax.common.exception.ExceptionMessages;
import com.alphax.repository.CobolServiceRepository;
import com.alphax.repository.DBServiceRepository;
import com.alphax.repository.StubServiceRepository;
import com.alphax.service.mb.BusinessCases;
import com.alphax.vo.mb.AccessesBA_DTO;
import com.alphax.vo.mb.BADeliveryNotesDTO;
import com.alphax.vo.mb.BusinessCases25DTO;
import com.alphax.vo.mb.BusinessCases49DTO;
import com.alphax.vo.mb.BusinessCasesDTO;
import com.alphax.vo.mb.DeliveryNotes_BA_DTO;
import com.alphax.vo.mb.DeparturesBA_DTO;
import com.alphax.vo.mb.DropdownObject;
import com.alphax.vo.mb.FinalizationsBA_DTO;
import com.alphax.vo.mb.GlobalSearch;
import com.alphax.vo.mb.LagerDetailsDTO;
import com.alphax.vo.mb.MasterDataBA_DTO;
import com.alphax.vo.mb.RebookingBundlesBA_DTO;
import com.alphax.vo.mb.SupplierDetailsDTO;
import com.alphax.common.constants.AlphaXCommonUtils;
import com.alphax.common.constants.Program_Commands_Constants;
import com.alphax.common.constants.RestInputConstants;
import com.ibm.as400.access.ProgramParameter;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BusinessCasesImpl extends BaseService implements BusinessCases  {

	@Autowired
	private MessageService messageService;

	@Autowired
	StubServiceRepository stubServiceRepository;

	@Autowired
	CobolServiceRepository cobolServiceRepository;

	@Autowired
	DBServiceRepository dbServiceRepository;
	
	@Autowired
	AlphaXCommonUtils commonUtils;

	DecimalFormat decimalformat_twodigit = new DecimalFormat("##.##", new DecimalFormatSymbols(Locale.ENGLISH));
	DecimalFormat decimalformat_fixtwodigit = new DecimalFormat("#0.00", new DecimalFormatSymbols(Locale.ENGLISH));
	DecimalFormat decimalformat_fixFourdigit = new DecimalFormat("#0.0000", new DecimalFormatSymbols(Locale.ENGLISH));

	/**
	 * This method is used to update Finalizations BA (Finalisierungen BA) using COBOL Program.
	 */
	@Override
	public Map<String, String> updateFinalizationsBA(FinalizationsBA_DTO finalizationsBA, String dataLibrary, String schema, String companyId, String agencyId ) {
		log.info("Inside updateFinalizationsBA method of BusinessCasesImpl");

		ProgramParameter[] parmList = new ProgramParameter[12];
		Map<String, String> programOutput = new HashMap<String, String>();

		try{

			if(finalizationsBA.getBusinessCases().equals("67")) {
				programOutput = newJavaImplementation_BA67(finalizationsBA, dataLibrary, schema, companyId, agencyId,null);

			}else {

				String returnCode = StringUtils.rightPad("", 5, " ");
				parmList[0] = new ProgramParameter(returnCode.getBytes(Program_Commands_Constants.IBM_273), 5);

				String returnMsg = StringUtils.rightPad("", 100, " ");
				parmList[1] = new ProgramParameter(returnMsg.getBytes(Program_Commands_Constants.IBM_273), 100);

				String location = StringUtils.rightPad(finalizationsBA.getLocation(), 10, " ");
				parmList[2] = new ProgramParameter(location.getBytes(Program_Commands_Constants.IBM_273), 10);

				String businessCases = StringUtils.rightPad(finalizationsBA.getBusinessCases(), 2, " ");
				parmList[3] = new ProgramParameter(businessCases.getBytes(Program_Commands_Constants.IBM_273), 2);

				String partMovementDate = StringUtils.rightPad(getJJJJMMTTFormat(finalizationsBA.getPartMovementDate()), 8, " ");
				parmList[4] = new ProgramParameter(partMovementDate.getBytes(Program_Commands_Constants.IBM_273), 8);

				String userName = StringUtils.rightPad(finalizationsBA.getUserName(), 10, " ");
				parmList[5] = new ProgramParameter(userName.getBytes(Program_Commands_Constants.IBM_273), 10);

				String manufacturer = StringUtils.rightPad(finalizationsBA.getManufacturer(), 4, " ");
				parmList[6] = new ProgramParameter(manufacturer.getBytes(Program_Commands_Constants.IBM_273), 4);

				String partNumber = StringUtils.rightPad(finalizationsBA.getPartNumber(), 30, " ");
				parmList[7] = new ProgramParameter(partNumber.getBytes(Program_Commands_Constants.IBM_273), 30);

				String bookingAmount = StringUtils.leftPad(convertDecimalValue(finalizationsBA.getBookingAmount(),5,1), 6, "0");
				parmList[8] = new ProgramParameter(bookingAmount.getBytes(Program_Commands_Constants.IBM_273), 6);

				String inventoryDescription = StringUtils.rightPad(finalizationsBA.getInventoryDescription(), 10, " ");
				parmList[9] = new ProgramParameter(inventoryDescription.getBytes(Program_Commands_Constants.IBM_273), 10);

				String correctionOrAddition = StringUtils.rightPad(finalizationsBA.getCorrectionOrAddition(), 1, " ");
				if(businessCases.equalsIgnoreCase("67")){
					correctionOrAddition = "K";
				}
				parmList[10] = new ProgramParameter(correctionOrAddition.getBytes(Program_Commands_Constants.IBM_273), 1);

				String ignoreBackOrders = StringUtils.rightPad(finalizationsBA.getIgnoreBackOrders(), 1, " ");
				parmList[11] = new ProgramParameter(ignoreBackOrders.getBytes(Program_Commands_Constants.IBM_273), 1);

				//execute the COBOL program - OFSMFIN.PGM
				List<String> outputList = cobolServiceRepository.executeProgram(parmList, 0, Program_Commands_Constants.FINALIZATIONS_BA_PROGRAM );

				if(outputList != null && !outputList.isEmpty()) {
					if(!outputList.get(0).contains("Error")) {
						programOutput.put(outputList.get(0).trim(),outputList.get(1).trim());
					}
				}
			}
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY,"Finalizations BA"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY,"Finalizations BA"), exception);
			throw exception;
		}

		return programOutput;
	}


	/**
	 * This method is used to update Accesses BA  (Zugänge BA) using COBOL Program.
	 */
	@Override
	public Map<String, String> updateAccessesBA(AccessesBA_DTO accessesBA,String dataLibrary, String companyId, String agencyId ) {
		log.info("Inside updateAccessesBA method of BusinessCasesImpl");

		ProgramParameter[] parmList = new ProgramParameter[29];
		Map<String, String> programOutput = new HashMap<String, String>();

		try{

			String returnCode = StringUtils.rightPad("", 5, " ");
			parmList[0] = new ProgramParameter(returnCode.getBytes(Program_Commands_Constants.IBM_273), 5);

			String returnMsg = StringUtils.rightPad("", 100, " ");
			parmList[1] = new ProgramParameter(returnMsg.getBytes(Program_Commands_Constants.IBM_273), 100);

			String location = StringUtils.rightPad(accessesBA.getLocation(), 10, " ");
			parmList[2] = new ProgramParameter(location.getBytes(Program_Commands_Constants.IBM_273), 10);

			String businessCases = StringUtils.rightPad(accessesBA.getBusinessCases(), 2, " ");
			parmList[3] = new ProgramParameter(businessCases.getBytes(Program_Commands_Constants.IBM_273), 2);

			String movementDate = StringUtils.rightPad(getJJJJMMTTFormat(accessesBA.getMovementDate()), 8, " ");
			parmList[4] = new ProgramParameter(movementDate.getBytes(Program_Commands_Constants.IBM_273), 8);

			String userName = StringUtils.rightPad(accessesBA.getUserName(), 10, " ");
			parmList[5] = new ProgramParameter(userName.getBytes(Program_Commands_Constants.IBM_273), 10);

			String manufacturer = accessesBA.getManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING : accessesBA.getManufacturer();	
			manufacturer = StringUtils.rightPad(manufacturer, 4, " ");
			parmList[6] = new ProgramParameter(manufacturer.getBytes(Program_Commands_Constants.IBM_273), 4);

			String supplierNumber = StringUtils.leftPad(accessesBA.getSupplierNumber(), 5, "0");
			parmList[7] = new ProgramParameter(supplierNumber.getBytes(Program_Commands_Constants.IBM_273), 5);

			String deliveryNoteNumber = StringUtils.rightPad(accessesBA.getDeliveryNoteNumber(), 10, " ");
			parmList[8] = new ProgramParameter(deliveryNoteNumber.getBytes(Program_Commands_Constants.IBM_273), 10);

			String purchaseOrderNumber = StringUtils.leftPad(accessesBA.getPurchaseOrderNumber(), 4, "0");
			parmList[9] = new ProgramParameter(purchaseOrderNumber.getBytes(Program_Commands_Constants.IBM_273), 4);

			String purchaseOrderItem = StringUtils.leftPad(accessesBA.getPurchaseOrderItem(), 3, "0");
			parmList[10] = new ProgramParameter(purchaseOrderItem.getBytes(Program_Commands_Constants.IBM_273), 3);

			String orderArt = StringUtils.leftPad(accessesBA.getOrderArt(), 2, "0");
			parmList[11] = new ProgramParameter(validateWithLowValues(orderArt), 2);

			String partNumber = StringUtils.rightPad(accessesBA.getPartNumber(), 30, " ");
			parmList[12] = new ProgramParameter(partNumber.getBytes(Program_Commands_Constants.IBM_273), 30);

			String partName = StringUtils.rightPad(truncateName(accessesBA.getPartName(),50), 50, " ");
			parmList[13] = new ProgramParameter(partName.getBytes(Program_Commands_Constants.IBM_273), 50);

			String bookingAmount = StringUtils.leftPad(convertDecimalValue(accessesBA.getBookingAmount(),5,1), 6, "0");
			parmList[14] = new ProgramParameter(bookingAmount.getBytes(Program_Commands_Constants.IBM_273), 6);

			String listPrice = StringUtils.leftPad(convertDecimalValue(accessesBA.getListPriceWithoutVAT(),5,2), 7, "0");
			parmList[15] = new ProgramParameter(listPrice.getBytes(Program_Commands_Constants.IBM_273), 7);

			String shoppingDiscountGroup = StringUtils.rightPad(accessesBA.getShoppingDiscountGroup(), 5, " ");
			parmList[16] = new ProgramParameter(shoppingDiscountGroup.getBytes(Program_Commands_Constants.IBM_273), 5);

			String marketingCode = StringUtils.rightPad(accessesBA.getMarketingCode(), 6, " ");
			parmList[17] = new ProgramParameter(marketingCode.getBytes(Program_Commands_Constants.IBM_273), 6);

			String netShoppingPrice = StringUtils.leftPad(convertDecimalValue(accessesBA.getNetShoppingPrice(),5,2), 7, "0");
			parmList[18] = new ProgramParameter(netShoppingPrice.getBytes(Program_Commands_Constants.IBM_273), 7);

			// Below field Not longer used since API V1.7. Send statically LowValue
			//String selfCalculatedNetPrice = StringUtils.leftPad(convertDecimalValue(accessesBA.getSelfCalculatedNetPrice(),5,2), 7, "0");
			String selfCalculatedNetPrice = "0";
			parmList[19] = new ProgramParameter(validateWithLowValues(selfCalculatedNetPrice), 7);

			String storageLocation = StringUtils.rightPad(accessesBA.getStorageLocation(), 8, " ");
			parmList[20] = new ProgramParameter(validateWithLowValues(storageLocation), 8);

			String priceIndicator = StringUtils.rightPad(accessesBA.getPriceIndicator(), 1, " ");
			parmList[21] = new ProgramParameter(validateWithLowValues(priceIndicator), 1);

			String vatRegistrationNumber = StringUtils.leftPad(accessesBA.getVatRegistrationNumber(), 2, "0");
			parmList[22] = new ProgramParameter(vatRegistrationNumber.getBytes(Program_Commands_Constants.IBM_273), 2);

			// Below field Not longer used since API V1.7. Send statically LowValue
			//String bookingProcess = StringUtils.leftPad(accessesBA.getBookingProcess(), 1, "0");
			String bookingProcess = "0";
			parmList[23] = new ProgramParameter(validateWithLowValues(bookingProcess), 1);

			String customerGroup = StringUtils.leftPad(accessesBA.getCustomerGroup(), 1, "0");
			parmList[24] = new ProgramParameter(validateWithLowValues(customerGroup), 1);

			String previousBookingPrice = StringUtils.leftPad(convertDecimalValue(accessesBA.getPreviousBookingPrice(),5,2), 7, "0");
			parmList[25] = new ProgramParameter(previousBookingPrice.getBytes(Program_Commands_Constants.IBM_273), 7);

			// Below field Not longer used since API V1.7. Send statically LowValue
			//String specialDiscount = StringUtils.leftPad(convertDecimalValue(accessesBA.getSpecialDiscount(),3,2), 5, "0");
			String specialDiscount = "0";
			parmList[26] = new ProgramParameter(validateWithLowValues(specialDiscount), 5);

			String assortmentClass = StringUtils.rightPad(accessesBA.getAssortmentClass(), 1, " ");
			parmList[27] = new ProgramParameter(validateWithLowValues(assortmentClass), 1);
			
			// Mandantory since API V1.11.0 Only for BA05 relevant.For other BA´s send statically LowValue
			String customerNumber = "0";
			parmList[28] = new ProgramParameter(validateWithLowValues(customerNumber), 6);




			//execute the COBOL program - OFSMZUG.PGM
			List<String> outputList = cobolServiceRepository.executeProgram(parmList, 0, Program_Commands_Constants.ACCESSES_BA_PROGRAM );

			if(outputList != null && !outputList.isEmpty()) {
				if(!outputList.get(0).contains("Error")) {

					programOutput.put(outputList.get(0).trim(),outputList.get(1).trim());
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY,"Accesses BA (Zugänge BA) "));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY,"Accesses BA (Zugänge BA)"), exception);
			throw exception;
		}

		return programOutput;
	}


	private String truncateName(String name, int length) {
		String newValue = StringUtils.left(name, length);
		return newValue;
	}


	private String convertDecimalValue(String totalValue, int numericValue, int decimalValue) {

		if (totalValue != null && !totalValue.isEmpty() && totalValue.contains(".")) {
			List<String> listValues = Arrays.asList(StringUtils.split(totalValue, "."));

			String firstValue = "";
			String secondValue = "";

			if (listValues.size() == 2) {
				firstValue = StringUtils.leftPad(listValues.get(0), numericValue, "0");
				secondValue = StringUtils.rightPad(listValues.get(1), decimalValue, "0");
				totalValue = StringUtils.left(firstValue, numericValue)+ StringUtils.left(secondValue, decimalValue);
			} else {
				firstValue = StringUtils.leftPad(listValues.get(0), numericValue, "0");
				totalValue = StringUtils.left(firstValue, numericValue);
			}

		}
		return totalValue;
	}

	private String getJJJJMMTTFormat(String date) {

		if (date != null && !date.isEmpty() && checkDateFormat(date)) {

			String values[] = date.split("/");
			date = StringUtils.leftPad(values[2], 4, "0") + StringUtils.leftPad(values[1], 2, "0")
			+ StringUtils.leftPad(values[0], 2, "0");
		}

		return date;
	}

	boolean checkDateFormat(String date) {
		SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
		sdfrmt.setLenient(false);

		try {
			Date javaDate = sdfrmt.parse(date);
		} catch (Exception e) {
			log.info(date + " is Invalid Date format for ");
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY,"Invalid date format Bewegungsdatum"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY,"Invalid date format Bewegungsdatum"), exception);
			throw exception;
			//return false;
		}
		/* Return true if date format is valid */
		return true;

	}

	/**
	 * This method is used to Update Departures BA using COBOL Program (Business Cases:-[20/22/25/09]).
	 */
	@Override
	public Map<String, String> updateDeparturesBA(DeparturesBA_DTO  departuresBA_Obj, String dataLibrary, String companyId, String agencyId ) {
		log.info("Inside updateDeparturesBA method of BusinessCasesImpl");

		ProgramParameter[] parmList = new ProgramParameter[22];
		Map<String, String> programOutput = new HashMap<String, String>();

		//validate the company Id 
		validateCompany(companyId);

		//validate the agency Id 
		validateAgency(agencyId);

		try{

			String returnCode = StringUtils.rightPad("", 5, " ");
			parmList[0] = new ProgramParameter(returnCode.getBytes(Program_Commands_Constants.IBM_273), 5); //RC

			String returnMsg = StringUtils.rightPad("", 100, " ");
			parmList[1] = new ProgramParameter(returnMsg.getBytes(Program_Commands_Constants.IBM_273), 2); //RCMsg

			String warehouseNumber = StringUtils.leftPad(departuresBA_Obj.getWarehouseNumber(), 2, "0");
			String compID =  StringUtils.leftPad(companyId, 2, "0");
			String agencyID = StringUtils.leftPad(agencyId, 2, "0");

			String locationId = StringUtils.rightPad(compID+agencyID+warehouseNumber, 10, " ");
			parmList[2] = new ProgramParameter(locationId.getBytes(Program_Commands_Constants.IBM_273), 2); //LOCATION

			String businessCases = StringUtils.leftPad(departuresBA_Obj.getBusinessCases(), 2, "0");
			parmList[3] = new ProgramParameter(businessCases.getBytes(Program_Commands_Constants.IBM_273), 2); //BA

			String partMovementDate = StringUtils.rightPad(getJJJJMMTTFormat(departuresBA_Obj.getPartMovementDate()), 8, " ");
			parmList[4] = new ProgramParameter(partMovementDate.getBytes(Program_Commands_Constants.IBM_273), 2); //DATBEW

			String userName = StringUtils.rightPad(departuresBA_Obj.getUserName(), 10, " ");
			parmList[5] = new ProgramParameter(userName.getBytes(Program_Commands_Constants.IBM_273), 2); //USER

			String manufacturer_temp = departuresBA_Obj.getManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING : departuresBA_Obj.getManufacturer();

			String manufacturer = StringUtils.rightPad(manufacturer_temp, 4, " ");
			parmList[6] = new ProgramParameter(manufacturer.getBytes(Program_Commands_Constants.IBM_273), 2); //HERST

			String partNumber = StringUtils.rightPad(departuresBA_Obj.getPartNumber(), 30, " ");
			parmList[7] = new ProgramParameter(partNumber.getBytes(Program_Commands_Constants.IBM_273), 2); //TNR

			if(departuresBA_Obj.getListPrice() != null) {
				String listPrice = StringUtils.leftPad(convertDecimalValue(departuresBA_Obj.getListPrice(), 5, 2), 7, "0");
				parmList[8] = new ProgramParameter(listPrice.getBytes(Program_Commands_Constants.IBM_273), 2); //EPR
			}else {
				parmList[8] = validateWithLowValues();   //EPR
			}

			if(departuresBA_Obj.getBookingAmount() != null) {
				String bookingAmount = StringUtils.leftPad(convertDecimalValue(departuresBA_Obj.getBookingAmount(), 5, 1), 6, "0");
				parmList[9] = new ProgramParameter(bookingAmount.getBytes(Program_Commands_Constants.IBM_273), 2); //BUMENG
			}else {
				parmList[9] = validateWithLowValues();   //BUMENG
			}

			if(departuresBA_Obj.getNetShoppingPrice() != null) {
				String netShoppingPrice = StringUtils.leftPad(convertDecimalValue(departuresBA_Obj.getNetShoppingPrice(), 5, 2), 7, "0");
				parmList[10] = new ProgramParameter(validateWithLowValues(netShoppingPrice), 2); //EKNPR
			}else {
				parmList[10] = validateWithLowValues();   //EKNPR
			}

			if(departuresBA_Obj.getCustomerGroup() != null) {
				String customerGroup = StringUtils.rightPad(departuresBA_Obj.getCustomerGroup(), 1, " ");
				parmList[11] = new ProgramParameter(customerGroup.getBytes(Program_Commands_Constants.IBM_273), 2); //AG
			}else {
				parmList[11] = validateWithLowValues();   //AG
			}

			if(departuresBA_Obj.getSupplierNumber() != null) {
				String supplierNumber = StringUtils.leftPad(departuresBA_Obj.getSupplierNumber(), 5, "0");
				parmList[12] = new ProgramParameter(supplierNumber.getBytes(Program_Commands_Constants.IBM_273), 2);  //LIEF
			}else {
				parmList[12] = validateWithLowValues();   //LIEF
			}

			if(departuresBA_Obj.getSalesOrderNumber() != null) {
				String salesOrderNumber = StringUtils.rightPad(departuresBA_Obj.getSalesOrderNumber(), 10, " ");
				parmList[13] = new ProgramParameter(validateWithLowValues(salesOrderNumber), 2); //BELEG
			}else {
				parmList[13] = validateWithLowValues();   //BELEG
			}

			if(departuresBA_Obj.getSalesOrderPosition() != null) {
				String salesOrderPosition = StringUtils.leftPad(departuresBA_Obj.getSalesOrderPosition(), 3, "0");
				parmList[14] = new ProgramParameter(validateWithLowValues(salesOrderPosition), 2);   //BELEGPOS
			}else {
				parmList[14] = validateWithLowValues();   //BELEGPOS
			}

			if(departuresBA_Obj.getInvoicingType() != null) {
				String invoicingType = StringUtils.rightPad(departuresBA_Obj.getInvoicingType(), 2, " ");
				parmList[15] = new ProgramParameter(validateWithLowValues(invoicingType), 2);   //FAART
			}else {
				parmList[15] = validateWithLowValues();   //FAART
			}

			if(departuresBA_Obj.getIssuer() != null) {
				String issuer = StringUtils.rightPad(departuresBA_Obj.getIssuer(), 2, " ");
				parmList[16] = new ProgramParameter(validateWithLowValues(issuer), 2);   //MITARB
			}else {
				parmList[16] = validateWithLowValues();   //MITARB
			}

			if(departuresBA_Obj.getCustomerNumber() != null) {
				String customerNumber = StringUtils.leftPad(departuresBA_Obj.getCustomerNumber(), 8, "0");
				parmList[17] = new ProgramParameter(validateWithLowValues(customerNumber), 2);   //KDNR
			}else {
				parmList[17] = validateWithLowValues();   //KDNR
			}

			if(departuresBA_Obj.getCustomerGroupAlphaplus() != null) {
				String customerGroupAlphaplus = StringUtils.leftPad(departuresBA_Obj.getCustomerGroupAlphaplus(), 2, "0");
				parmList[18] = new ProgramParameter(validateWithLowValues(customerGroupAlphaplus), 2); //KDGRE
			}else {
				parmList[18] = validateWithLowValues();   //KDGRE
			}

			if(departuresBA_Obj.getVehicleRegistrationNo() != null) {
				String vehicleRegistrationNo = StringUtils.rightPad(departuresBA_Obj.getVehicleRegistrationNo(), 14, " ");
				parmList[19] = new ProgramParameter(validateWithLowValues(vehicleRegistrationNo), 2); //FZKZ
			}else {
				parmList[19] = validateWithLowValues();   //FZKZ
			}

			if(departuresBA_Obj.getModel() != null) {
				String model = StringUtils.rightPad(departuresBA_Obj.getModel(), 6, " ");
				parmList[20] = new ProgramParameter(validateWithLowValues(model), 2); //BAUM
			}else {
				parmList[20] = validateWithLowValues();   //BAUM
			}

			if(departuresBA_Obj.getOverstockwithDAK() != null) {
				String overstockwithDAK = StringUtils.rightPad(departuresBA_Obj.getOverstockwithDAK(), 1, " ");
				parmList[21] = new ProgramParameter(overstockwithDAK.getBytes(Program_Commands_Constants.IBM_273), 2); //UEBDAK
			}else {
				parmList[21] = validateWithLowValues();   //UEBDAK
			}


			//execute the COBOL program - OFSMETA.PGM
			List<String> outputList = cobolServiceRepository.executeProgram(parmList, 0, Program_Commands_Constants.DEPARTURES_BA_PROGRAM);

			if(outputList != null && !outputList.isEmpty()) {
				if(!outputList.get(0).contains("Error")) {

					programOutput.put("returnCode", outputList.get(0).trim());
					programOutput.put("returnMsg", outputList.get(1).trim());
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY,"Departures BA"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY,"Departures BA"), exception);
			throw exception;
		}

		return programOutput;
	}


	/**
	 * This method is used to Update Master Data BA using COBOL Program (Business Cases:-[40/44/45/49]).
	 */
	@Override
	public Map<String, String> updateMasterDataBA(MasterDataBA_DTO  masterDataBA_Obj, String dataLibrary, String companyId, String agencyId ) {
		log.info("Inside updateMasterDataBA method of BusinessCasesImpl");

		Map<String, String> programOutput = new HashMap<String, String>();

		//validate the company Id 
		validateCompany(companyId);

		//validate the agency Id 
		validateAgency(agencyId);

		try{
			ProgramParameter[] parmList = setParameterValues(masterDataBA_Obj, companyId, agencyId);

			//execute the COBOL program - OFSMMIS.PGM
			List<String> outputList = cobolServiceRepository.executeProgram(parmList, 0, Program_Commands_Constants.MASTER_DATA_BA_PROGRAM);

			if(outputList != null && !outputList.isEmpty()) {
				if(!outputList.get(0).contains("Error")) {

					programOutput.put("returnCode", outputList.get(0).trim());
					programOutput.put("returnMsg", outputList.get(1).trim());
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY,"Master Data BA"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY,"Master Data BA"), exception);
			throw exception;
		}

		return programOutput;
	}


	/**
	 * This method is used to Set the Program parameters for COBOL Program.
	 */
	private ProgramParameter[] setParameterValues(MasterDataBA_DTO  masterDataBA_Obj, String companyId, String agencyId) {

		ProgramParameter[] parmList = new ProgramParameter[70];

		try {

			String returnCode = StringUtils.rightPad("", 5, " ");
			parmList[0] = new ProgramParameter(returnCode.getBytes(Program_Commands_Constants.IBM_273), 5); //RC

			String returnMsg = StringUtils.rightPad("", 100, " ");
			parmList[1] = new ProgramParameter(returnMsg.getBytes(Program_Commands_Constants.IBM_273), 2); //RCMSG

			String warehouseNumber = StringUtils.leftPad(masterDataBA_Obj.getWarehouseNumber(), 2, "0");
			String compID =  StringUtils.leftPad(companyId, 2, "0");
			String agencyID = StringUtils.leftPad(agencyId, 2, "0");

			String locationId = StringUtils.rightPad(compID+agencyID+warehouseNumber, 10, " ");
			parmList[2] = new ProgramParameter(locationId.getBytes(Program_Commands_Constants.IBM_273), 2); //LOCATION

			String businessCases = StringUtils.rightPad(masterDataBA_Obj.getBusinessCases(), 2, " ");
			parmList[3] = new ProgramParameter(businessCases.getBytes(Program_Commands_Constants.IBM_273), 2); //BA

			String partMovementDate = StringUtils.rightPad(getJJJJMMTTFormat(masterDataBA_Obj.getPartMovementDate()), 8, " ");
			parmList[4] = new ProgramParameter(partMovementDate.getBytes(Program_Commands_Constants.IBM_273), 2); //DATBEW

			String userName = StringUtils.rightPad(masterDataBA_Obj.getUserName(), 10, " ");
			parmList[5] = new ProgramParameter(userName.getBytes(Program_Commands_Constants.IBM_273), 2); //USER

			String manufacturer_temp = masterDataBA_Obj.getManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING : masterDataBA_Obj.getManufacturer();

			String manufacturer = StringUtils.rightPad(manufacturer_temp, 4, " ");
			parmList[6] = new ProgramParameter(manufacturer.getBytes(Program_Commands_Constants.IBM_273), 2); //HERST

			String partNumber = StringUtils.rightPad(masterDataBA_Obj.getPartNumber(), 30, " ");
			parmList[7] = new ProgramParameter(partNumber.getBytes(Program_Commands_Constants.IBM_273), 2); //TNR


			if(masterDataBA_Obj.getBookingAmount()!=null) {

				String bookingAmount = StringUtils.leftPad(convertDecimalValue(masterDataBA_Obj.getBookingAmount(), 5, 1), 6, "0");
				parmList[8] = new ProgramParameter(bookingAmount.getBytes(Program_Commands_Constants.IBM_273), 2); //BUMENG
			}else {
				parmList[8] = validateWithLowValues(); //BUMENG
			}

			if(masterDataBA_Obj.getBookingReceipt() != null) {
				String bookingReceipt = StringUtils.rightPad(masterDataBA_Obj.getBookingReceipt(), 10, " ");
				parmList[9] = new ProgramParameter(bookingReceipt.getBytes(Program_Commands_Constants.IBM_273), 2); //BELEG
			}else {
				parmList[9] = validateWithLowValues(); //BELEG
			}

			if(masterDataBA_Obj.getCustomerDiscountForSERV() != null) {
				String customerDiscountForSERV = StringUtils.leftPad(convertDecimalValue(masterDataBA_Obj.getCustomerDiscountForSERV(),3,2), 5, "0");
				parmList[10] = new ProgramParameter(customerDiscountForSERV.getBytes(Program_Commands_Constants.IBM_273), 2); //ABSCH
			}else {
				parmList[10] = validateWithLowValues(); //ABSCH
			}

			if(masterDataBA_Obj.getDesignation() != null) {
				String designation = StringUtils.rightPad(masterDataBA_Obj.getDesignation(), 50, " ");
				parmList[11] = new ProgramParameter(designation.getBytes(Program_Commands_Constants.IBM_273), 2); //BENEN
			}else {
				parmList[11] = validateWithLowValues(); //BENEN
			}

			if(masterDataBA_Obj.getBackorder() != null) {
				String backorder = StringUtils.leftPad(convertDecimalValue(masterDataBA_Obj.getBackorder(),7,2), 9, "0");
				parmList[12] = new ProgramParameter(backorder.getBytes(Program_Commands_Constants.IBM_273), 2); //BESAUS
			}else {
				parmList[12] = validateWithLowValues(); //BESAUS
			}

			if(masterDataBA_Obj.getOrderNote() != null) {
				String orderNote = StringUtils.rightPad(masterDataBA_Obj.getOrderNote(), 17, " ");
				parmList[13] = new ProgramParameter(orderNote.getBytes(Program_Commands_Constants.IBM_273), 2); //BESVER
			}else {
				parmList[13] = validateWithLowValues(); //BESVER
			}

			if(masterDataBA_Obj.getDispositionIndicator() != null) {
				String dispositionIndicator = StringUtils.rightPad(masterDataBA_Obj.getDispositionIndicator(), 1, " ");
				parmList[14] = new ProgramParameter(dispositionIndicator.getBytes(Program_Commands_Constants.IBM_273), 2); //DISPO
			}else {
				parmList[14] = validateWithLowValues(); //DISPO
			}

			String licensePlate = StringUtils.rightPad("", 2, " ");
			if(masterDataBA_Obj.getLicensePlate() !=null && !masterDataBA_Obj.getLicensePlate().trim().isEmpty()) {

				if(masterDataBA_Obj.getLicensePlate().equalsIgnoreCase("1")) {
					licensePlate = StringUtils.rightPad("D", 2, " ");
				}
				parmList[15] = new ProgramParameter(licensePlate.getBytes(Program_Commands_Constants.IBM_273), 2); //DRTKZ
			}
			else {
				parmList[15] = validateWithLowValues(); //DRTKZ
			}


			if(masterDataBA_Obj.getLastOrderDate() != null) {
				String lastOrderDate = StringUtils.rightPad(getJJJJMMTTFormat(masterDataBA_Obj.getLastOrderDate()), 8, " ");
				parmList[16] = new ProgramParameter(lastOrderDate.getBytes(Program_Commands_Constants.IBM_273), 2); //DTLBES
			}else {
				parmList[16] = validateWithLowValues(); //DTLBES
			}

			if(masterDataBA_Obj.getLastMovementDate() != null) {
				String lastMovementDate = StringUtils.rightPad(getJJJJMMTTFormat(masterDataBA_Obj.getLastMovementDate()), 8, " ");
				parmList[17] = new ProgramParameter(lastMovementDate.getBytes(Program_Commands_Constants.IBM_273), 2); //DTLBEW
			}else {
				parmList[17] = validateWithLowValues(); //DTLBEW
			}

			if(masterDataBA_Obj.getLastAccessDate() != null) {
				String lastAccessDate = StringUtils.rightPad(getJJJJMMTTFormat(masterDataBA_Obj.getLastAccessDate()), 8, " ");
				parmList[18] = new ProgramParameter(lastAccessDate.getBytes(Program_Commands_Constants.IBM_273), 2); //DTLZUG
			}else {
				parmList[18] = validateWithLowValues(); //DTLZUG
			}

			if(masterDataBA_Obj.getExpirationDate() != null) {
				String expirationDate = StringUtils.rightPad(getJJJJMMTTFormat(masterDataBA_Obj.getExpirationDate()), 8, " ");
				parmList[19] = new ProgramParameter(expirationDate.getBytes(Program_Commands_Constants.IBM_273), 2); //DTVERF
			}else {
				parmList[19] = validateWithLowValues(); //DTVERF
			}

			if(masterDataBA_Obj.getNetShoppingPrice() != null) {
				String netPrice = StringUtils.leftPad(convertDecimalValue(masterDataBA_Obj.getNetShoppingPrice(), 5, 2), 7, "0");
				parmList[20] = new ProgramParameter(netPrice.getBytes(Program_Commands_Constants.IBM_273), 2); //EKNPR
			}else {
				parmList[20] = validateWithLowValues(); //EKNPR
			}

			if(masterDataBA_Obj.getDisposalFlatRate() != null) {
				String disposalFlatRate = StringUtils.leftPad(convertDecimalValue(masterDataBA_Obj.getDisposalFlatRate(), 5, 2), 7, "0");
				parmList[21] = new ProgramParameter(disposalFlatRate.getBytes(Program_Commands_Constants.IBM_273), 2); //ENTSOP
			}else {
				parmList[21] = validateWithLowValues(); //ENTSOP
			}

			if(masterDataBA_Obj.getDisposalCosts() != null) {
				String disposalCosts = StringUtils.leftPad(convertDecimalValue(masterDataBA_Obj.getDisposalCosts(),5,2), 7, "0");
				parmList[22] = new ProgramParameter(disposalCosts.getBytes(Program_Commands_Constants.IBM_273), 2); //ENTSOW
			}else {
				parmList[22] = validateWithLowValues(); //ENTSOW
			}

			if(masterDataBA_Obj.getPurchasePrice() != null) {
				String purchasePrice = StringUtils.leftPad(convertDecimalValue(masterDataBA_Obj.getPurchasePrice(),5,2), 7, "0");
				parmList[23] = new ProgramParameter(purchasePrice.getBytes(Program_Commands_Constants.IBM_273), 2); //EPR 
			}else {
				parmList[23] = validateWithLowValues(); //EPR 
			}

			if(masterDataBA_Obj.getWeightInGrams() != null) {
				String weightInGrams = StringUtils.leftPad(convertDecimalValue(masterDataBA_Obj.getWeightInGrams(),7,3), 10, "0");
				parmList[24] = new ProgramParameter(weightInGrams.getBytes(Program_Commands_Constants.IBM_273), 2); //GEWICH
			}else {
				parmList[24] = validateWithLowValues(); //GEWICH
			}

			if(masterDataBA_Obj.getListPriceValidFrom() != null) {
				String listPriceValidFrom = StringUtils.rightPad(getJJJJMMTTFormat(masterDataBA_Obj.getListPriceValidFrom()), 8, " ");
				parmList[25] = new ProgramParameter(listPriceValidFrom.getBytes(Program_Commands_Constants.IBM_273), 2); //GPRDAT
			}else {
				parmList[25] = validateWithLowValues(); //GPRDAT
			}

			if(masterDataBA_Obj.getInternalIndicator() != null) {
				String internalIndicator = StringUtils.rightPad(masterDataBA_Obj.getInternalIndicator(), 1, " ");
				parmList[26] = new ProgramParameter(internalIndicator.getBytes(Program_Commands_Constants.IBM_273), 2); //INTVKZ
			}else {
				parmList[26] = validateWithLowValues(); //INTVKZ
			}

			if(masterDataBA_Obj.getDiscountForSERV() != null) {
				String discountForSERV = StringUtils.leftPad(masterDataBA_Obj.getDiscountForSERV(), 1, "0");
				parmList[27] = new ProgramParameter(discountForSERV.getBytes(Program_Commands_Constants.IBM_273), 2); //KDABS
			}else {
				parmList[27] = validateWithLowValues(); //KDABS
			}

			if(masterDataBA_Obj.getBlockingIndicator() != null) {
				String calculationLock = StringUtils.rightPad(masterDataBA_Obj.getBlockingIndicator(), 1, " ");
				parmList[28] = new ProgramParameter(calculationLock.getBytes(Program_Commands_Constants.IBM_273), 2); //KSPKZ
			}else {
				parmList[28] = validateWithLowValues(); //KSPKZ
			}

			if(masterDataBA_Obj.getRegistrationRequired() != null) {
				String registrationRequired = StringUtils.rightPad(masterDataBA_Obj.getRegistrationRequired(), 1, " ");
				parmList[29] = new ProgramParameter(registrationRequired.getBytes(Program_Commands_Constants.IBM_273), 2); //KZEINS
			}else {
				parmList[29] = validateWithLowValues(); //KZEINS
			}

			if(masterDataBA_Obj.getMarkPartsReturn() != null) {
				String markPartsReturn = StringUtils.rightPad(masterDataBA_Obj.getMarkPartsReturn(), 1, " ");
				parmList[30] = new ProgramParameter(markPartsReturn.getBytes(Program_Commands_Constants.IBM_273), 2); //KZRUCK
			}else {
				parmList[30] = validateWithLowValues(); //KZRUCK
			}

			if(masterDataBA_Obj.getLabelEU() != null && masterDataBA_Obj.getLabelEU().trim().length() > 1) {
				String labelEU = StringUtils.rightPad(masterDataBA_Obj.getLabelEU(), 22, " ");
				parmList[31] = new ProgramParameter(labelEU.getBytes(Program_Commands_Constants.IBM_273), 2); //LABEL
			}else {
				parmList[31] = validateWithLowValues(); //LABEL
			}

			/*String labelEU = StringUtils.rightPad(masterDataBA_Obj.getLabelEU()!=null?masterDataBA_Obj.getLabelEU():"", 22, " ");
			parmList[31] = new ProgramParameter(labelEU.getBytes(Program_Commands_Constants.IBM_273), 2); //LABEL
			 */



			if(masterDataBA_Obj.getActivityType() != null) {
				String activityType = StringUtils.rightPad(masterDataBA_Obj.getActivityType(), 2, " ");
				parmList[32] = new ProgramParameter(activityType.getBytes(Program_Commands_Constants.IBM_273), 2); //LEIART
			}else {
				parmList[32] = validateWithLowValues(); //LEIART
			}

			if(masterDataBA_Obj.getDeliveryPlant_DAG() != null) {
				String deliverIndicator = StringUtils.leftPad(masterDataBA_Obj.getDeliveryPlant_DAG(), 2, "0");
				parmList[33] = new ProgramParameter(deliverIndicator.getBytes(Program_Commands_Constants.IBM_273), 2); //LIWERK
			}else {
				parmList[33] = validateWithLowValues(); //LIWERK
			}

			if(masterDataBA_Obj.getStorageLocation() != null) {
				String storageLocation = StringUtils.rightPad(masterDataBA_Obj.getStorageLocation(), 8, " ");
				parmList[34] = new ProgramParameter(storageLocation.getBytes(Program_Commands_Constants.IBM_273), 2); //LOPA
			}else {
				parmList[34] = validateWithLowValues(); //LOPA
			}

			if(masterDataBA_Obj.getMaximumStock() != null) {
				String maximumStock = StringUtils.leftPad(convertDecimalValue(masterDataBA_Obj.getMaximumStock(),8,2), 10, "0");
				parmList[35] = new ProgramParameter(maximumStock.getBytes(Program_Commands_Constants.IBM_273), 2); //MAXBES
			}else {
				parmList[35] = validateWithLowValues(); //MAXBES
			}

			if(masterDataBA_Obj.getMarketingCode() != null) {
				String marketingCode = StringUtils.rightPad(masterDataBA_Obj.getMarketingCode(), 6, " ");
				parmList[36] = new ProgramParameter(marketingCode.getBytes(Program_Commands_Constants.IBM_273), 2); //MC
			}else {
				parmList[36] = validateWithLowValues(); //MC
			}

			if(masterDataBA_Obj.getUnitOfMeasure() != null) {
				String unitOfMeasure = StringUtils.rightPad(masterDataBA_Obj.getUnitOfMeasure(), 1, " ");
				parmList[37] = new ProgramParameter(unitOfMeasure.getBytes(Program_Commands_Constants.IBM_273), 2); //ME
			}else {
				parmList[37] = validateWithLowValues(); //ME
			}

			if(masterDataBA_Obj.getMinimumOrderQuantity() != null) {
				String minimumOrderQuantity = StringUtils.leftPad(convertDecimalValue(masterDataBA_Obj.getMinimumOrderQuantity(),7,2), 9, "0");
				parmList[38] = new ProgramParameter(minimumOrderQuantity.getBytes(Program_Commands_Constants.IBM_273), 2); //MIBESM
			}else {
				parmList[38] = validateWithLowValues(); //MIBESM
			}

			if(masterDataBA_Obj.getMinimumStock() != null) {
				String minimumStock = StringUtils.leftPad(convertDecimalValue(masterDataBA_Obj.getMinimumStock(),7,2), 9, "0");
				parmList[39] = new ProgramParameter(minimumStock.getBytes(Program_Commands_Constants.IBM_273), 2); //MINBES
			}else {
				parmList[39] = validateWithLowValues(); //MINBES
			}

			if(masterDataBA_Obj.getDispositionMode() != null) {
				String dispositionMode = StringUtils.rightPad(masterDataBA_Obj.getDispositionMode(), 1, " ");
				parmList[40] = new ProgramParameter(dispositionMode.getBytes(Program_Commands_Constants.IBM_273), 2); //MODUS
			}else {
				parmList[40] = validateWithLowValues(); //MODUS
			}

			if(masterDataBA_Obj.getVatCode() != null) {
				String vatCode = StringUtils.leftPad(masterDataBA_Obj.getVatCode(), 2, " ");
				parmList[41] = new ProgramParameter(vatCode.getBytes(Program_Commands_Constants.IBM_273), 2); //MWST
			}else {
				parmList[41] = validateWithLowValues(); //MWST
			}

			if(masterDataBA_Obj.getPledgeValue() != null) {
				String pledgeValue = StringUtils.leftPad(convertDecimalValue(masterDataBA_Obj.getPledgeValue(),5,4), 9, "0");
				parmList[42] = new ProgramParameter(pledgeValue.getBytes(Program_Commands_Constants.IBM_273), 2); //PFAND
			}else {
				parmList[42] = validateWithLowValues(); //PFAND
			}

			String priceIndicator = StringUtils.rightPad("", 1, " ");

			if(masterDataBA_Obj.getPriceIndicator()!=null && !masterDataBA_Obj.getPriceIndicator().trim().isEmpty()) {

				if(masterDataBA_Obj.getPriceIndicator().equalsIgnoreCase("1")) {
					priceIndicator = StringUtils.rightPad("K", 1, " ");
				}
				parmList[43] = new ProgramParameter(priceIndicator.getBytes(Program_Commands_Constants.IBM_273), 2); //PRKZ
			}
			else {
				parmList[43] = validateWithLowValues(); //PRKZ
			}

			if(masterDataBA_Obj.getSecurityDistance() != null) {
				String securityDistance = StringUtils.leftPad(convertDecimalValue(masterDataBA_Obj.getSecurityDistance(),7,2), 9, "0");
				parmList[44] = new ProgramParameter(securityDistance.getBytes(Program_Commands_Constants.IBM_273), 2); //SIBES
			}else {
				parmList[44] = validateWithLowValues(); //SIBES
			}

			if(masterDataBA_Obj.getAssortmentClass() != null) {
				String assortmentClass = StringUtils.rightPad(masterDataBA_Obj.getAssortmentClass(), 2, " ");
				parmList[45] = new ProgramParameter(assortmentClass.getBytes(Program_Commands_Constants.IBM_273), 2); //SKL
			}else {
				parmList[45] = validateWithLowValues(); //SKL
			}

			if(masterDataBA_Obj.getPartType() != null) {
				String partType = StringUtils.leftPad(masterDataBA_Obj.getPartType(), 2, "0");
				parmList[46] = new ProgramParameter(partType.getBytes(Program_Commands_Constants.IBM_273), 2); //TA  
			}else {
				parmList[46] = validateWithLowValues(); //TA
			}

			if(masterDataBA_Obj.getReferenceCodeOEM() != null) {
				String referenceCodeOEM = StringUtils.rightPad(masterDataBA_Obj.getReferenceCodeOEM(), 3, " ");
				parmList[47] = new ProgramParameter(referenceCodeOEM.getBytes(Program_Commands_Constants.IBM_273), 2); //TCODE
			}else {
				parmList[47] = validateWithLowValues(); //TCODE
			}

			if(masterDataBA_Obj.getTradeNumber() != null) {
				String tradeNumber = StringUtils.rightPad(masterDataBA_Obj.getTradeNumber(), 18, " ");
				parmList[48] = new ProgramParameter(tradeNumber.getBytes(Program_Commands_Constants.IBM_273), 2); //THWSNR
			}else {
				parmList[48] = validateWithLowValues(); //THWSNR
			}

			if(masterDataBA_Obj.getPartIdNumber() != null) {
				String partIdNumber = StringUtils.rightPad(masterDataBA_Obj.getPartIdNumber(), 1, " ");
				parmList[49] = new ProgramParameter(partIdNumber.getBytes(Program_Commands_Constants.IBM_273), 2); //TIDENT
			}else {
				parmList[49] = validateWithLowValues(); //TIDENT
			}

			if(masterDataBA_Obj.getPartNumberOEM() != null) {
				String partNumberOEM = StringUtils.rightPad(masterDataBA_Obj.getPartNumberOEM(), 40, " ");
				parmList[50] = new ProgramParameter(partNumberOEM.getBytes(Program_Commands_Constants.IBM_273), 2);//TNRH
			}else {
				parmList[50] = validateWithLowValues(); //TNRH
			}

			if(masterDataBA_Obj.getReturnValue() != null) {
				String returnValue = StringUtils.leftPad(convertDecimalValue(masterDataBA_Obj.getReturnValue(), 7, 4), 11, "0");
				parmList[51] = new ProgramParameter(returnValue.getBytes(Program_Commands_Constants.IBM_273), 2); //TRUWER
			}else {
				parmList[51] = validateWithLowValues(); //TRUWER
			}

			if(masterDataBA_Obj.getPartVolumeOld() != null) {
				String partVolumeOld = StringUtils.rightPad(masterDataBA_Obj.getPartVolumeOld(), 10, " ");
				parmList[52] = new ProgramParameter(partVolumeOld.getBytes(Program_Commands_Constants.IBM_273), 2); //TVOL
			}else {
				parmList[52] = validateWithLowValues(); //TVOL
			}

			if(masterDataBA_Obj.getPartVolumeNew() != null) {
				String partVolumeNew = StringUtils.rightPad(masterDataBA_Obj.getPartVolumeNew(), 15, " ");
				parmList[53] = new ProgramParameter(partVolumeNew.getBytes(Program_Commands_Constants.IBM_273), 2); //TVOLN
			}else {
				parmList[53] = validateWithLowValues(); //TVOLN
			}

			if(masterDataBA_Obj.getPackingUnit1() != null) {
				String packingUnit1 = StringUtils.leftPad(masterDataBA_Obj.getPackingUnit1(), 5, "0");
				parmList[54] = new ProgramParameter(packingUnit1.getBytes(Program_Commands_Constants.IBM_273), 2); //VERP1
			}else {
				parmList[54] = validateWithLowValues(); //VERP1
			}

			if(masterDataBA_Obj.getPackingUnit2() != null) {
				String packingUnit2 = StringUtils.leftPad(masterDataBA_Obj.getPackingUnit2(), 5, "0");
				parmList[55] = new ProgramParameter(packingUnit2.getBytes(Program_Commands_Constants.IBM_273), 2); //VERP2
			}else {
				parmList[55] = validateWithLowValues(); //VERP2
			}

			if(masterDataBA_Obj.getCountGroupInventory() != null) {
				String inventoryGroup = StringUtils.rightPad(masterDataBA_Obj.getCountGroupInventory(), 3, " ");
				parmList[56] = new ProgramParameter(inventoryGroup.getBytes(Program_Commands_Constants.IBM_273), 2); //ZAEGRU
			}else {
				parmList[56] = validateWithLowValues(); //ZAEGRU
			}

			if(masterDataBA_Obj.getFuturePrice() != null) {
				String futurePrice = StringUtils.leftPad(convertDecimalValue(masterDataBA_Obj.getFuturePrice(),5,2), 7, "0");
				parmList[57] = new ProgramParameter(futurePrice.getBytes(Program_Commands_Constants.IBM_273), 2); //ZPREIS
			}else {
				parmList[57] = validateWithLowValues(); //ZPREIS
			}

			if(masterDataBA_Obj.getReceiptAmtCurrentMonth() != null) {
				String receiptAmtCurrentMonth = StringUtils.leftPad(convertDecimalValue(masterDataBA_Obj.getReceiptAmtCurrentMonth(),7,2), 9, "0"); 
				parmList[58] = new ProgramParameter(receiptAmtCurrentMonth.getBytes(Program_Commands_Constants.IBM_273), 2); //ZULFMS
			}else {
				parmList[58] = validateWithLowValues(); //ZULFMS
			}

			if(masterDataBA_Obj.getAccessValueCurrentMonth() != null) {
				String accessValueCurrentMonth = StringUtils.leftPad(convertDecimalValue(masterDataBA_Obj.getAccessValueCurrentMonth(),5,2), 7, "0");
				parmList[59] = new ProgramParameter(accessValueCurrentMonth.getBytes(Program_Commands_Constants.IBM_273), 2); //ZULFMW
			}else {
				parmList[59] = validateWithLowValues(); //ZULFMW
			}

			String hazardousMark =  StringUtils.rightPad("", 4, " ");
			if(masterDataBA_Obj.getHazardousMaterialMark()!=null && !masterDataBA_Obj.getHazardousMaterialMark().trim().isEmpty()) {

				if(masterDataBA_Obj.getHazardousMaterialMark().equalsIgnoreCase("1")) {
					hazardousMark = StringUtils.rightPad("REAC", 4, " ");
				}
				parmList[60] = new ProgramParameter(hazardousMark.getBytes(Program_Commands_Constants.IBM_273), 4); //GFKZ
			}
			else {
				parmList[60] = validateWithLowValues(); //GFKZ
			}

			if(masterDataBA_Obj.getSelfCalculatedNetPrice() != null) {
				String selfCalculatedNetPrice = StringUtils.leftPad(convertDecimalValue(masterDataBA_Obj.getSelfCalculatedNetPrice(),11,4), 15, "0");
				parmList[61] = new ProgramParameter(selfCalculatedNetPrice.getBytes(Program_Commands_Constants.IBM_273), 2); //NPREIS
			}else {
				parmList[61] = validateWithLowValues(); //NPREIS
			}

			if(masterDataBA_Obj.getDiscountGroup() != null) {
				String discountGroup = StringUtils.rightPad(masterDataBA_Obj.getDiscountGroup(), 5, " "); 
				parmList[62] = new ProgramParameter(discountGroup.getBytes(Program_Commands_Constants.IBM_273), 2);    //RG  
			}else {
				parmList[62] = validateWithLowValues(); //RG 
			}

			if(masterDataBA_Obj.getPartBrand() != null) {
				String partBrand = StringUtils.rightPad(masterDataBA_Obj.getPartBrand(), 2, " ");
				parmList[63] = new ProgramParameter(partBrand.getBytes(Program_Commands_Constants.IBM_273), 2); //TMARKE
			}else {
				parmList[63] = validateWithLowValues(); //TMARKE
			}

			if(masterDataBA_Obj.getSeasonalLicensePlate() != null) {
				String seasonalLicensePlate = StringUtils.rightPad(masterDataBA_Obj.getSeasonalLicensePlate(), 1, " ");
				parmList[64] = new ProgramParameter(seasonalLicensePlate.getBytes(Program_Commands_Constants.IBM_273), 2); //WINTER
			}else {
				parmList[64] = validateWithLowValues(); //WINTER
			}

			if(masterDataBA_Obj.getPredecessorPartNo() != null) {
				String predecessorPartNo = StringUtils.rightPad(masterDataBA_Obj.getPredecessorPartNo(), 30, " ");
				parmList[65] = new ProgramParameter(predecessorPartNo.getBytes(Program_Commands_Constants.IBM_273), 2); //TNRV
			}else {
				parmList[65] = validateWithLowValues(); //TNRV
			}

			if(masterDataBA_Obj.getSuccessorPartNo() != null) {
				String successorPartNo = StringUtils.rightPad(masterDataBA_Obj.getSuccessorPartNo(), 30, " ");
				parmList[66] = new ProgramParameter(successorPartNo.getBytes(Program_Commands_Constants.IBM_273), 2); //TNRN
			}else {
				parmList[66] = validateWithLowValues(); //TNRN
			}

			if(masterDataBA_Obj.getSa() != null) {
				String sa = StringUtils.rightPad(masterDataBA_Obj.getSa(), 1, " ");
				parmList[67] = new ProgramParameter(sa.getBytes(Program_Commands_Constants.IBM_273), 2); //SA  
			}else {
				parmList[67] = validateWithLowValues(); //SA
			}

			if(masterDataBA_Obj.getAvgAcquisitionCost() != null) {
				String avgAcquisitionCost = StringUtils.leftPad(convertDecimalValue(masterDataBA_Obj.getAvgAcquisitionCost(),11,4), 15, "0");
				parmList[68] = new ProgramParameter(avgAcquisitionCost.getBytes(Program_Commands_Constants.IBM_273), 2); //DAK
			}else {
				parmList[68] = validateWithLowValues(); //DAK
			}

			if(masterDataBA_Obj.getLastDepartureDate() != null) {
				String lastDepartureDate = StringUtils.rightPad(getJJJJMMTTFormat(masterDataBA_Obj.getLastDepartureDate()), 8, " ");
				parmList[69] = new ProgramParameter(lastDepartureDate.getBytes(Program_Commands_Constants.IBM_273), 2); //DTLABG
			}else {
				parmList[69] = validateWithLowValues(); //DTLABG
			}

		}
		catch (AlphaXException ex) {
			throw ex;
		} 
		catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY,"Master Data BA"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY,"Master Data BA"), exception);
			throw exception;
		}

		return parmList;
	}


	/**
	 * This method is used to get Finalizations BA (50 and 67 ) details (Finalisierungen BA) from DB.
	 */
	@Override
	public FinalizationsBA_DTO getFinalizationsBADetails(String dataLibrary, String companyId, String agencyId,
			String manufacturer,String warehouseNumber,String partNumber,String businessCase  ) {
		log.info("Inside getFinalizationsBADetails method of BusinessCasesImpl");

		FinalizationsBA_DTO finalizationsBA_Obj = new FinalizationsBA_DTO();
		try {
			manufacturer = manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING: manufacturer;			

			if (businessCase.equalsIgnoreCase("50")) {
				finalizationsBA_Obj = businessCase50Module(dataLibrary, manufacturer, warehouseNumber, partNumber);
			} else if(businessCase.equalsIgnoreCase("67")) {
				finalizationsBA_Obj = businessCase67Module(dataLibrary, manufacturer, warehouseNumber, partNumber);
			}else{
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.INVALID_MSG_KEY, " Business Case (BA) value"));
				log.error(messageService.getReadableMessage(ExceptionMessages.INVALID_MSG_KEY, " Business Case (BA) value"), exception);
				throw exception;
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, " Finalizations BA"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, " Finalizations BA"), exception);
			throw exception;
		}

		return finalizationsBA_Obj;
	}
	

	/**
	 * Business logic for BA 50
	 */
	private FinalizationsBA_DTO businessCase50Module(String dataLibrary, String manufacturer, String warehouseNumber, String partNumber) {

		log.info("Inside businessCase50Module method of BusinessCasesImpl");

		FinalizationsBA_DTO finalizationsBA_Obj = new FinalizationsBA_DTO();

		StringBuilder query = new StringBuilder(" SELECT BENEN, AKTBES, BESAUS FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE ");
		query.append("HERST = ").append("'"+manufacturer+"'");
		query.append(" AND LNR = ").append(warehouseNumber);
		query.append(" AND TNR = ").append("'"+partNumber+"'");

		List<FinalizationsBA> finalizationsBAList = dbServiceRepository.getResultUsingQuery(FinalizationsBA.class, query.toString(), true);

		//if the list is not empty
		if (finalizationsBAList != null && !finalizationsBAList.isEmpty()) {

			for(FinalizationsBA finalizationsBA : finalizationsBAList){

				finalizationsBA_Obj.setBookingAmount(String.valueOf(finalizationsBA.getBookingAmount()));
				finalizationsBA_Obj.setInventoryDescription(finalizationsBA.getInventoryDescription());
				finalizationsBA_Obj.setPendingOrders(String.valueOf(finalizationsBA.getPendingOrders()));
			}

		}else{

			log.info("Part number not yet created in ETSTAMM");
			AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.BUSINESS_CASES_50_FAILED_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.BUSINESS_CASES_50_FAILED_MSG_KEY));
			throw exception;
		}

		return finalizationsBA_Obj;
	}


	/**
	 * Business logic for BA 67
	 */
	private FinalizationsBA_DTO businessCase67Module(String dataLibrary, String manufacturer, String warehouseNumber, String partNumber) {

		log.info("Inside businessCase67Module method of BusinessCasesImpl");
		log.info("Call businessCase50Module...");

		FinalizationsBA_DTO finalizationsBA_Obj = businessCase50Module(dataLibrary, manufacturer, warehouseNumber, partNumber);
		finalizationsBA_Obj.setBookingAmount("");

		/*  
		 * try{ String tableName = "E_I000".concat(warehouseNumber);
		 * 
		 * StringBuilder checkTableInQSYS = new
		 * StringBuilder(" SELECT COUNT(*) AS COUNT FROM QSYS2.SYSTABLES WHERE TABLE_NAME = '"
		 * );
		 * checkTableInQSYS.append(tableName).append("' AND  TABLE_SCHEMA = '").append(
		 * dataLibrary).append("' ");
		 * 
		 * String tableCount =
		 * dbServiceRepository.getCountUsingQuery(checkTableInQSYS.toString());
		 * 
		 * if(Integer.parseInt(tableCount) > 0){
		 * 
		 * StringBuilder query = new
		 * StringBuilder(" SELECT COUNT(*) AS COUNT FROM ").append(dataLibrary).append(
		 * ".E_I000").append(warehouseNumber);
		 * query.append(" WHERE I_HERST = ").append("'"+manufacturer+"'");
		 * query.append(" AND I_ETNR = ").append("'"+partNumber+"'");
		 * 
		 * String count = dbServiceRepository.getCountUsingQuery(query.toString());
		 * 
		 * if(Integer.parseInt(count) > 0){
		 * 
		 * log.
		 * info("Part number available on counting list. Manual BA67 therefore not possible."
		 * ); AlphaXException exception = new
		 * AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale
		 * (), ExceptionMessages.BUSINESS_CASES_67_FAILED_MSG_KEY));
		 * log.error(messageService.getReadableMessage(ExceptionMessages.
		 * BUSINESS_CASES_67_FAILED_MSG_KEY)); throw exception; } } }catch (Exception e)
		 * { //log.info("Table not available in DB :.E_I000"+warehouseNumber ); }
		 */
		return finalizationsBA_Obj;
	}

	/**
	 * This method is used to get Accesses BA (01,02,05 and 17) details (Zugänge BA) from DB.
	 */
	@Override
	public AccessesBA_DTO getAccessesBADetails(String schema, String dataLibrary, String companyId, String agencyId,
			String manufacturer,String warehouseNumber,String partNumber,String businessCase) {
		log.info("Inside getAccessesBADetails method of BusinessCasesImpl");

		AccessesBA_DTO accessesBA_obj = new AccessesBA_DTO();
		try {
			manufacturer = manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING) ? RestInputConstants.DCAG_STRING : manufacturer;
			businessCase =   StringUtils.leftPad(businessCase, 2, "0");

			if (businessCase.equalsIgnoreCase("01") || businessCase.equalsIgnoreCase("02")) {
				accessesBA_obj = businessCase01Or02Module(schema,dataLibrary, manufacturer, warehouseNumber, partNumber,companyId, agencyId,businessCase );
			}else if(businessCase.equalsIgnoreCase("05")){
				accessesBA_obj = businessCase05Module(schema,dataLibrary, manufacturer, warehouseNumber, partNumber);
			} else if (businessCase.equalsIgnoreCase("17")){
				accessesBA_obj = businessCase17Module(schema,dataLibrary, manufacturer, warehouseNumber, partNumber,companyId, agencyId,businessCase);
			}else {
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.INVALID_MSG_KEY, " Business Case (BA) value"));
				log.error(messageService.getReadableMessage(ExceptionMessages.INVALID_MSG_KEY, " Business Case (BA) value"), exception);
				throw exception;

			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, " Accesses BA"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, " Accesses BA"), exception);
			throw exception;
		}

		return accessesBA_obj;

	}

	/**
	 * This method is used to get Accesses BA (01,02,05 and 17) details (Zugänge BA) from DB.
	 */
	@Override
	public AccessesBA_DTO getAccessesBA06Details(String schema, String dataLibrary, String companyId, String agencyId,
			String manufacturer,String warehouseNumber,String partNumber,String businessCase, String supplierNo, String deliveryNoteNo  ) {
		log.info("Inside getAccessesBA06Details method of BusinessCasesImpl");

		AccessesBA_DTO accessesBA_obj = new AccessesBA_DTO();
		try {
			manufacturer = manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING) ? RestInputConstants.DCAG_STRING : manufacturer;
			businessCase =   StringUtils.leftPad(businessCase, 2, "0");

			if (businessCase.equalsIgnoreCase("06")){
				accessesBA_obj = businessCase06Module(schema,dataLibrary, manufacturer, warehouseNumber, partNumber, supplierNo, deliveryNoteNo);
			}else {
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.INVALID_MSG_KEY, " Business Case (BA) value"));
				log.error(messageService.getReadableMessage(ExceptionMessages.INVALID_MSG_KEY, " Business Case (BA) value"), exception);
				throw exception;

			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, " Accesses BA"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, " Accesses BA"), exception);
			throw exception;
		}

		return accessesBA_obj;

	}


	private AccessesBA_DTO businessCase01Or02Module(String schema, String dataLibrary, String manufacturer, String warehouseNumber,
			String partNumber,String companyId, String agencyId, String businessCase)  {

		log.info("Inside businessCase01Module method of BusinessCasesImpl");

		AccessesBA_DTO accessBA_Obj = new AccessesBA_DTO();

		StringBuilder query_1 = new StringBuilder(" SELECT EPR, RG, TMARKE, LIWERK, TA, LEIART, PRKZ, LOPA, BENEN, SA, MC, EKNPR, NPREIS, SKL, DAK, MWST FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE ");
		query_1.append("HERST = ").append("'"+manufacturer+"'");
		query_1.append(" AND LNR = ").append(warehouseNumber);
		query_1.append(" AND TNR = ").append("'"+partNumber+"'");

		List<PartDetails> finalizationsPartBAList = dbServiceRepository.getResultUsingQuery(PartDetails.class, query_1.toString(), true);

		//if the list is not empty
		if (finalizationsPartBAList != null && !finalizationsPartBAList.isEmpty()) {

			for(PartDetails finalizationsPartBA : finalizationsPartBAList){

				accessBA_Obj.setListPriceWithoutVAT(decimalformat_twodigit.format(finalizationsPartBA.getPurchasePrice() != null ? finalizationsPartBA.getPurchasePrice() : "0.00"));
				accessBA_Obj.setShoppingDiscountGroup(finalizationsPartBA.getDiscountGroup());
				accessBA_Obj.setPartBrand(finalizationsPartBA.getOemBrand());
				accessBA_Obj.setDeliverIndicator(String.valueOf(finalizationsPartBA.getDeliverIndicator()));
				accessBA_Obj.setPartsIndikator(String.valueOf(finalizationsPartBA.getPartsIndikator()));
				accessBA_Obj.setActivityType(String.valueOf(finalizationsPartBA.getActivityType()));
				accessBA_Obj.setPriceIndicator(finalizationsPartBA.getPriceMark());
				accessBA_Obj.setStorageLocation(finalizationsPartBA.getStorageLocation());
				accessBA_Obj.setPartName(finalizationsPartBA.getName());
				accessBA_Obj.setStorageIndikator(String.valueOf(finalizationsPartBA.getStorageIndikator()));
				accessBA_Obj.setMarketingCode(finalizationsPartBA.getMarketingCode());
				//				accessBA_Obj.setNetShoppingPrice("00");
				accessBA_Obj.setPreviousBookingPrice(String.valueOf(finalizationsPartBA.getAverageNetPrice()));
				accessBA_Obj.setAssortmentClass(finalizationsPartBA.getCommonPartWithUnimog());
				//accessBA_Obj.setBookingAmount(String.valueOf(finalizationsPartBA.getAverageNetPrice()));
				accessBA_Obj.setBookingAmount("");
				accessBA_Obj.setVatRegistrationNumber(StringUtils.leftPad(String.valueOf(finalizationsPartBA.getValueAddedTax()),2,"0"));
				accessBA_Obj.setSelfCalculatedNetPrice(decimalformat_twodigit.format(finalizationsPartBA.getPreviousPurchasePrice()!=null ? finalizationsPartBA.getPreviousPurchasePrice() : "0.00"));
				accessBA_Obj.setSpecialDiscount("00");
			}

		}else{
			//accessBA_Obj  = getValuesFromOETKMOD(accessBA_Obj,companyId,agencyId,warehouseNumber,businessCase,partNumber,manufacturer);
			 accessBA_Obj = newJavaImplementation_OETKMOD(dataLibrary, schema, manufacturer, partNumber,"01Or02");
		}

		StringBuilder query_2 = new StringBuilder(" SELECT BUVORG , SATZ FROM ").append(dataLibrary).append(".E_RAB WHERE ");
		query_2.append("HERST = ").append("'"+manufacturer+"'");
		query_2.append(" AND MARKE = ").append("'"+accessBA_Obj.getPartBrand()+"'");
		query_2.append(" AND GRUPPE = ").append("'"+accessBA_Obj.getShoppingDiscountGroup()+"'");
		query_2.append(" order by GDAT desc fetch first row only");

		List<PartDetails> discountGroupList = dbServiceRepository.getResultUsingQuery(PartDetails.class, query_2.toString(), true);
		//if the list is not empty
		if (discountGroupList != null && !discountGroupList.isEmpty()) {

			for(PartDetails discountGroupValue : discountGroupList){

				accessBA_Obj.setDiscountGroupValue(discountGroupValue.getDiscountGroupValue());		
				accessBA_Obj.setDiscountGroupPercentageValue(String.valueOf(discountGroupValue.getDiscountGroupPercentageValue()));
			}
		}

		StringBuilder query_3 = new StringBuilder("SELECT COUNT(*) AS COUNT FROM ");
		query_3.append(schema).append(".PMH_TMARKE where TMA_HERST='").append(manufacturer).append("' AND TMA_TMARKE = '").append(accessBA_Obj.getPartBrand()+"'");

		String count = dbServiceRepository.getCountUsingQuery(query_3.toString());

		if(Integer.parseInt(count) > 0) {
			accessBA_Obj.setMarkeAvailable(true);
		}

		Double purchasePrice = calculatePurchasePrice(accessBA_Obj.getListPriceWithoutVAT(), accessBA_Obj.getDiscountGroupPercentageValue());
		accessBA_Obj.setNetShoppingPrice(decimalformat_twodigit.format(purchasePrice!=null ? purchasePrice : "0.00"));

		return accessBA_Obj;
	}

	/*Need to remove in next sprint
	private AccessesBA_DTO getValuesFromOETKMOD(AccessesBA_DTO accessBA_Obj, String companyId, String agencyId, String warehouseNumber, String businessCase, String partNumber, String manufacturer){
		log.info("Getting value from OETKMOD...");
		try {

			boolean isDataAvailable = false;
			ProgramParameter[] parmList = new ProgramParameter[17];

			String returnCode = StringUtils.rightPad("", 5, " ");
			parmList[0] = new ProgramParameter(returnCode.getBytes(Program_Commands_Constants.IBM_273), 5);

			String returnMsg = StringUtils.rightPad("", 100, " ");
			parmList[1] = new ProgramParameter(returnMsg.getBytes(Program_Commands_Constants.IBM_273), 100);

			String location = StringUtils.leftPad(companyId, 2, "0")+StringUtils.leftPad(agencyId, 2, "0")+StringUtils.leftPad(warehouseNumber, 2, "0");
			location = StringUtils.rightPad(location, 10, " ");
			parmList[2] = new ProgramParameter(location.getBytes(Program_Commands_Constants.IBM_273), 10);

			parmList[3] = new ProgramParameter(businessCase.getBytes(Program_Commands_Constants.IBM_273), 2);

			String pattern = "dd/MM/yyyy";
			String dateInString =new SimpleDateFormat(pattern).format(new Date());

			String movementDate = StringUtils.rightPad(getJJJJMMTTFormat(dateInString), 8, " ");
			parmList[4] = new ProgramParameter(movementDate.getBytes(Program_Commands_Constants.IBM_273), 8);

			String userName = StringUtils.rightPad("", 10, " ");
			parmList[5] = new ProgramParameter(userName.getBytes(Program_Commands_Constants.IBM_273), 10);

			manufacturer = StringUtils.rightPad(manufacturer, 4, " ");
			parmList[6] = new ProgramParameter(manufacturer.getBytes(Program_Commands_Constants.IBM_273), 4);

			partNumber = StringUtils.rightPad(partNumber, 30, " ");
			parmList[7] = new ProgramParameter(partNumber.getBytes(Program_Commands_Constants.IBM_273), 30);

			String listPrice = StringUtils.leftPad("", 8, "0");
			parmList[8] = new ProgramParameter(listPrice.getBytes(Program_Commands_Constants.IBM_273), 8);

			String shoppingDiscountGroup = StringUtils.rightPad("", 5, " ");
			parmList[9] = new ProgramParameter(shoppingDiscountGroup.getBytes(Program_Commands_Constants.IBM_273), 5);

			String partName = StringUtils.rightPad("", 50, " ");
			parmList[10] = new ProgramParameter(partName.getBytes(Program_Commands_Constants.IBM_273), 50);

			String oemBrand = StringUtils.rightPad("", 2, " ");
			parmList[11] = new ProgramParameter(oemBrand.getBytes(Program_Commands_Constants.IBM_273), 2);

			String marketingCode = StringUtils.rightPad("", 6, " ");
			parmList[12] = new ProgramParameter(marketingCode.getBytes(Program_Commands_Constants.IBM_273), 6);

			String deliverIndicator = StringUtils.rightPad("", 2, " ");
			parmList[13] = new ProgramParameter(deliverIndicator.getBytes(Program_Commands_Constants.IBM_273), 2);

			String partsIndikator = StringUtils.rightPad("", 2, " ");
			parmList[14] = new ProgramParameter(partsIndikator.getBytes(Program_Commands_Constants.IBM_273), 2);

			String activityType = StringUtils.rightPad("", 2, " ");
			parmList[15] = new ProgramParameter(activityType.getBytes(Program_Commands_Constants.IBM_273), 2);

			String vatRegistrationNumber = StringUtils.leftPad("", 2, "0");
			parmList[16] = new ProgramParameter(vatRegistrationNumber.getBytes(Program_Commands_Constants.IBM_273), 2);


			List<String> outputList = cobolServiceRepository.executeProgram(parmList, 0, Program_Commands_Constants.GET_PART_DETAILS );

			if (outputList != null && !outputList.isEmpty()) {
				if (!outputList.get(0).contains("Error")) {
					isDataAvailable = true;
					if (outputList.get(0).trim().equalsIgnoreCase("00000")) {

						accessBA_Obj.setListPriceWithoutVAT(outputList.get(8));
						accessBA_Obj.setShoppingDiscountGroup(outputList.get(9));
						accessBA_Obj.setPartBrand(outputList.get(11));
						accessBA_Obj.setDeliverIndicator(outputList.get(13));
						accessBA_Obj.setPartsIndikator(outputList.get(14));
						accessBA_Obj.setActivityType(outputList.get(15));
						//accessBA_Obj.setPriceIndicator("");
						accessBA_Obj.setStorageLocation("");
						accessBA_Obj.setPartName(outputList.get(10));
						accessBA_Obj.setStorageIndikator("");
						accessBA_Obj.setMarketingCode(outputList.get(12));
						accessBA_Obj.setNetShoppingPrice("0");
						//accessBA_Obj.setAssortmentClass("");
						accessBA_Obj.setBookingAmount("0");
						accessBA_Obj.setVatRegistrationNumber(StringUtils.leftPad(outputList.get(16),2,"0"));
						accessBA_Obj.setSelfCalculatedNetPrice("00");
						accessBA_Obj.setSpecialDiscount("00");
					} else {
						AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
								ExceptionMessages.BUSINESS_CASES_01_FAILED_MSG_KEY));
						log.error(messageService.getReadableMessage(ExceptionMessages.BUSINESS_CASES_01_FAILED_MSG_KEY), exception);
						throw exception;
					}
				}
			}

			if(!isDataAvailable){
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.PROGRAM_FAILED_MSG_KEY, " OETKMOD"));
				log.error(messageService.getReadableMessage(ExceptionMessages.PROGRAM_FAILED_MSG_KEY, " OETKMOD"), exception);
				throw exception;
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, " Accesses BA"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, " Accesses BA"), exception);
			throw exception;
		}

		return accessBA_Obj;

	} */

	private Double calculatePurchasePrice(String listPriceWithoutVAT, String discoutGroupPercentage) {

		Double purchasePrice = 0.00;
		if(listPriceWithoutVAT != null && discoutGroupPercentage != null) {

			purchasePrice = Double.valueOf(listPriceWithoutVAT) * ((100 - Double.valueOf(discoutGroupPercentage)) / 100) ;
		}

		return purchasePrice;
	}

	private AccessesBA_DTO businessCase05Module(String schema, String dataLibrary, String manufacturer,
			String warehouseNumber, String partNumber) {

		log.info("Inside businessCase05Module method of BusinessCasesImpl");

		AccessesBA_DTO accessBA_Obj = new AccessesBA_DTO();

		StringBuilder query_1 = new StringBuilder(" SELECT BENEN, AKTBES FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE ");
		query_1.append("HERST = ").append("'"+manufacturer+"'");
		query_1.append(" AND LNR = ").append(warehouseNumber);
		query_1.append(" AND TNR = ").append("'"+partNumber+"'");

		List<PartDetails> finalizationsPartBAList = dbServiceRepository.getResultUsingQuery(PartDetails.class, query_1.toString(), true);

		//if the list is not empty
		if (finalizationsPartBAList != null && !finalizationsPartBAList.isEmpty()) {

			for(PartDetails finalizationsPartBA : finalizationsPartBAList){

				accessBA_Obj.setPartName(finalizationsPartBA.getName());
				accessBA_Obj.setCurrentStock(String.valueOf(finalizationsPartBA.getCurrentStock()));
			}
		}else{
			log.info("Part number not yet created in ETSTAMM");
			AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.BUSINESS_CASES_05_FAILED_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.BUSINESS_CASES_05_FAILED_MSG_KEY));
			throw exception;
		}

		return accessBA_Obj;
	}

	
	private AccessesBA_DTO businessCase06Module(String schema, String dataLibrary, String manufacturer,
			String warehouseNumber, String partNumber, String supplierNo, String deliveryNoteNo) {

		log.info("Inside businessCase06Module method of BusinessCasesImpl");

		StringBuilder query_1 = new StringBuilder(" SELECT TNR, BENEN, BESAUS, AKTBES, EPR, DAK ,SA, TMARKE, RG, MC, LEKPR, TA, LIWERK, ");
		query_1.append("EKNPR, NPREIS, TNRS, TNRD, FIRMA, FILIAL, DTLBEW, LEIART FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE ");
		query_1.append("HERST = ").append("'"+manufacturer+"'");
		query_1.append(" AND LNR = ").append(warehouseNumber);
		query_1.append(" AND TNR = ").append("'"+partNumber+"'");

		List<PartDetails> finalizationsPartBAList = dbServiceRepository.getResultUsingQuery(PartDetails.class, query_1.toString(), true);
		
		AccessesBA_DTO accessBA_Obj = new AccessesBA_DTO();

		//if the list is not empty
		if (finalizationsPartBAList != null && !finalizationsPartBAList.isEmpty()) {

			for(PartDetails finalizationsPartBA : finalizationsPartBAList){

				accessBA_Obj.setPartNumber(finalizationsPartBA.getPartNumber());//TNR
				accessBA_Obj.setPartName(finalizationsPartBA.getName()); //BENEN -PartName
				accessBA_Obj.setCurrentStock(String.valueOf(finalizationsPartBA.getCurrentStock())); //AKTBES
				accessBA_Obj.setListPriceWithoutVAT(decimalformat_twodigit.format
						(finalizationsPartBA.getPurchasePrice()!=null ? finalizationsPartBA.getPurchasePrice() : "0.00")); // EPR mwst
				accessBA_Obj.setSelfCalculatedNetPrice("00");
				accessBA_Obj.setPurchaseOrderNumber("");
				
				accessBA_Obj.setPendingOrders(String.valueOf(finalizationsPartBA.getPendingOrders()));	//BESAUS
				accessBA_Obj.setAverageNetPrice(String.valueOf(finalizationsPartBA.getAverageNetPrice()));	//DAK
				accessBA_Obj.setStorageIndikator(String.valueOf(finalizationsPartBA.getStorageIndikator())); //SA
				accessBA_Obj.setDiscountGroup(finalizationsPartBA.getDiscountGroup()!=null ? finalizationsPartBA.getDiscountGroup() : "" ); // RG
				accessBA_Obj.setOemBrand(finalizationsPartBA.getOemBrand()!=null ? finalizationsPartBA.getOemBrand() : "" ); //TMARKE
				accessBA_Obj.setMarketingCode(finalizationsPartBA.getMarketingCode()); //MC
				accessBA_Obj.setLastPurchasePrice(String.valueOf(finalizationsPartBA.getLastPurchasePrice())); //LEKPR
				accessBA_Obj.setPartsIndikator(finalizationsPartBA.getPartsIndikator()!= null ? String.valueOf(finalizationsPartBA.getPartsIndikator()):"");//TA
				accessBA_Obj.setDeliverIndicator(finalizationsPartBA.getDeliverIndicator()!=null ? String.valueOf(finalizationsPartBA.getDeliverIndicator()) :"");//LIWERK
				accessBA_Obj.setNetPrice(String.valueOf(finalizationsPartBA.getNetPrice()));//EKNPR
				accessBA_Obj.setPreviousPurchasePrice(String.valueOf(finalizationsPartBA.getPreviousPurchasePrice())); //NPREIS
				accessBA_Obj.setSortingFormatPartNumber(finalizationsPartBA.getSortingFormatPartNumber()); //TNRS
				accessBA_Obj.setPrintingFormatPartNumber(finalizationsPartBA.getPrintingFormatPartNumber()); //TNRD
				accessBA_Obj.setCompany(String.valueOf(finalizationsPartBA.getCompany()));//FIRMA
				accessBA_Obj.setAgency(String.valueOf(finalizationsPartBA.getBranch())); //FILALE
				accessBA_Obj.setMovementDate(String.valueOf(finalizationsPartBA.getLastMovementDate())); //DTLBEW
				accessBA_Obj.setActivityType(String.valueOf(finalizationsPartBA.getActivityType())); //LEIART
				
			}

			accessBA_Obj  = getNetPricefromDBForBA06(dataLibrary, manufacturer, warehouseNumber, accessBA_Obj,  
					partNumber, supplierNo, deliveryNoteNo);
		}else{
			log.info("Part number not yet created in ETSTAMM");
			AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.BUSINESS_CASES_05_FAILED_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.BUSINESS_CASES_05_FAILED_MSG_KEY));
			throw exception;
		}

		return accessBA_Obj;
	}


	private AccessesBA_DTO businessCase17Module(String schema, String dataLibrary, String manufacturer,
			String warehouseNumber, String partNumber, String companyId, String agencyId, String businessCase) {

		log.info("Inside businessCase17Module method of BusinessCasesImpl");

		AccessesBA_DTO accessBA_Obj = new AccessesBA_DTO();


		StringBuilder query = new StringBuilder(" SELECT COUNT(*) AS COUNT FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE ");
		query.append("HERST = ").append("'"+manufacturer+"'");
		query.append(" AND LNR = ").append(warehouseNumber);
		query.append(" AND TNR = ").append("'"+partNumber+"'");

		String count = dbServiceRepository.getCountUsingQuery(query.toString());
		
		 //if the list is not empty
		if (Integer.parseInt(count) > 0) {

			log.info("Part position already exists. BA17 new installation implausible.");
			AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.BUSINESS_CASES_17_01FAILED_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.BUSINESS_CASES_17_01FAILED_MSG_KEY));
			throw exception;

		}else{
			accessBA_Obj = newJavaImplementation_OETKMOD(dataLibrary, schema, manufacturer, partNumber,"17");
		}
		return accessBA_Obj;
	}



	/**
	 * This method is used to get DeliveryNotes List from DB.
	 */
	@Override
	public GlobalSearch getDeliveryNoteList(String dataLibrary, String companyId, String agencyId,
			String manufacturer,String warehouseNumber,String supplierNumber,String searchString, String pageSize, String pageNumber) {
		log.info("Inside getDeliveryNoteList method of BusinessCasesImpl");

		List<DeliveryNotes_BA_DTO> deliveryNoteObjList = new ArrayList<DeliveryNotes_BA_DTO>();
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

			manufacturer = manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING: manufacturer;			

			StringBuilder query = new StringBuilder(" SELECT HERST, LNR, BENDL, NRX, ART, LAUFNR, BA, REKOW, LFSDAT_JMT,PRUEFUNG, ERLEDIGT,  ( SELECT COUNT(*) FROM ");
			query.append(dataLibrary).append(".E_BSNLFS WHERE ");
			query.append(" BA='' ");
			query.append(" AND HERST = ").append("'"+manufacturer+"'");
			query.append(" AND LNR = ").append(warehouseNumber);
			query.append(" AND BENDL = ").append(supplierNumber);
			query.append(" AND UPPER(NRX) LIKE UPPER('%").append(searchString).append("%')) AS ROWNUMER FROM ");
			query.append(dataLibrary).append(".E_BSNLFS WHERE ");
			query.append("  BA='' ");
			query.append(" AND HERST = ").append("'"+manufacturer+"'");
			query.append(" AND LNR = ").append(warehouseNumber);
			query.append(" AND BENDL = ").append(supplierNumber);
			query.append(" AND UPPER(NRX) LIKE UPPER('%").append(searchString).append("%') order by NRX OFFSET ");
			query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");

			List<DeliveryNotes_BA> deliveryNoteList = dbServiceRepository.getResultUsingQuery(DeliveryNotes_BA.class, query.toString(), true);

			if(deliveryNoteList != null && !deliveryNoteList.isEmpty()){

				for(DeliveryNotes_BA deliveryNote : deliveryNoteList){
					DeliveryNotes_BA_DTO deliveryNotes_BA_DTO = new DeliveryNotes_BA_DTO();
					deliveryNotes_BA_DTO.setDeliveryNoteNo(deliveryNote.getDeliveryNoteNo());
					deliveryNotes_BA_DTO.setDeliveryNoteDate(convertDateToString(deliveryNote.getDeliveryNoteDate()));
					deliveryNotes_BA_DTO.setInfo("-----");
					
					deliveryNotes_BA_DTO.setManufacturer(deliveryNote.getManufacturer());
					deliveryNotes_BA_DTO.setWarehouseNumber(String.valueOf(deliveryNote.getWarehouseNumber()));
					deliveryNotes_BA_DTO.setSupplierNumber((String.valueOf(deliveryNote.getSupplierNumber())));
					deliveryNotes_BA_DTO.setProgramName(deliveryNote.getProgramName());//ART
					deliveryNotes_BA_DTO.setBusinessCase(deliveryNote.getBusinessCase());
					deliveryNotes_BA_DTO.setLaufnr(deliveryNote.getLaufnr()); //LAUFNR
					deliveryNotes_BA_DTO.setRekow(deliveryNote.getRekow()); //REKOW
					
					
					if(deliveryNote.getDeliveryNoteAudit()!= null && deliveryNote.getInvoiceAudit() != null){
						if(!deliveryNote.getDeliveryNoteAudit().trim().equalsIgnoreCase("J") && !deliveryNote.getInvoiceAudit().trim().equalsIgnoreCase("J")){
							deliveryNotes_BA_DTO.setStatus("Offen");
						}else if(deliveryNote.getDeliveryNoteAudit().trim().equalsIgnoreCase("J") && !deliveryNote.getInvoiceAudit().trim().equalsIgnoreCase("J")){
							deliveryNotes_BA_DTO.setStatus("Lieferscheinprüfung durchgeführt");
						}else if((deliveryNote.getDeliveryNoteAudit().trim().equalsIgnoreCase("J") && deliveryNote.getInvoiceAudit().trim().equalsIgnoreCase("J")) || 
								(!deliveryNote.getDeliveryNoteAudit().trim().equalsIgnoreCase("J") && deliveryNote.getInvoiceAudit().trim().equalsIgnoreCase("J")) ){
							deliveryNotes_BA_DTO.setStatus("Rechnungsprüfung durchgeführt");
						}
					}
					
					deliveryNotes_BA_DTO.setDeliveryNoteAudit(deliveryNote.getDeliveryNoteAudit());
					deliveryNotes_BA_DTO.setInvoiceAudit(deliveryNote.getInvoiceAudit());
					
					deliveryNoteObjList.add(deliveryNotes_BA_DTO);
				}

				globalSearchList.setSearchDetailsList(deliveryNoteObjList);
				globalSearchList.setTotalPages(Integer.toString(deliveryNoteList.get(0).getTotalCount()));
				globalSearchList.setTotalRecordCnt(Integer.toString(deliveryNoteList.get(0).getTotalCount()));

			}else{
				globalSearchList.setSearchDetailsList(deliveryNoteObjList);
				globalSearchList.setTotalPages(Integer.toString(0));
				globalSearchList.setTotalRecordCnt(Integer.toString(0));
			}


		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, " Delivery Note"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, " Delivery Note"), exception);
			throw exception;
		}

		return globalSearchList;

	}



	/**
	 * This method is used to convert date string YYYYMMDD  to dd/MM/YYYY
	 * @param entityDate
	 * @return
	 */
	private String convertDateToString(String entityDate) {

		StringBuilder dateBuilder = new StringBuilder();

		if(entityDate!=null && !entityDate.isEmpty() && entityDate.length() == 8) {
			dateBuilder.append(entityDate.substring(6, 8)).append("/");
			dateBuilder.append(entityDate.substring(4, 6)).append("/").append(entityDate.substring(0, 4));
		}
		return dateBuilder.toString();
	}


	/**
	 * This method is used to get the Lager (Warehouse) detail List for DDL.
	 */
	@Override
	public List<LagerDetailsDTO> getLagerList(String dataLibrary, String companyId, String agencyId, String allowedWarehouses, String schema) {

		log.info("Inside getLagerList method of BusinessCasesImpl");

		List<LagerDetailsDTO> lagerList = new ArrayList<>();

		//validate the Warehouse Ids
		validateWarehouses(allowedWarehouses);

		try {
			/*StringBuilder query = new StringBuilder("select KEY2, PNAME, PSTRAS, PLZ, PORT, PLZ_2, VFNR  from ");
			query.append(dataLibrary).append(".E_ETSTAMK4 where KEY2 in (").append(allowedWarehouses).append(") order by KEY2");*/

			StringBuilder query = new StringBuilder("select WAREHOUS_ID, AP_WAREHOUS_ID, NAME, CITY, POST_CODE, STREETANDNUM, ISACTIVE, VF_NUMBER from ");
			query.append(schema).append(".O_WRH").append(" where ISACTIVE = 1 and AP_WAREHOUS_ID  IN ( ").append(allowedWarehouses).append(" ) order by AP_WAREHOUS_ID ");

			List<AdminWarehouse> lagerDetailsList = dbServiceRepository.getResultUsingQuery(AdminWarehouse.class, query.toString(), true);

			if(lagerDetailsList != null && !lagerDetailsList.isEmpty()) {
				for(AdminWarehouse lagerDetails : lagerDetailsList) {

					LagerDetailsDTO lagerDetailsDto = new LagerDetailsDTO();

					lagerDetailsDto.setWarehouseNo(StringUtils.leftPad(lagerDetails.getAlphaPlusWarehouseId().toString(),2,"0"));
					lagerDetailsDto.setWarehouseName(lagerDetails.getWarehouseName());
					lagerDetailsDto.setAddress(new StringBuilder(lagerDetails.getStreetNumber()).append(", ")
							.append(lagerDetails.getPostalCode()).append(" ").append(lagerDetails.getCity()).toString());
					lagerDetailsDto.setVfNumber(String.valueOf(lagerDetails.getVfNumber()));
					
					String warehouseName = lagerDetails.getWarehouseName()!=null?lagerDetails.getWarehouseName():"";
					lagerDetailsDto.setWarehouseIdWithName(lagerDetailsDto.getWarehouseNo()+" - "+warehouseName);

					lagerList.add(lagerDetailsDto);
				}

			}else{
				log.info("Lager Details List is empty");
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



	/**
	 * This method is used to get the Lieferant (Suppliers) detail List for DDL.
	 */
	@Override
	public GlobalSearch getSupplierList(String dataLibrary, String companyId, String agencyId) {

		log.info("Inside getSupplierList method of BusinessCasesImpl");

		GlobalSearch globalSearchList = new GlobalSearch();

		try {

			StringBuilder query = new StringBuilder("select BENDL, NAME1, NAME2, STRAS, ORT, ");
			query.append("( SELECT COUNT(*) from ").append(dataLibrary).append(".E_ETSTAMK6) AS ROWNUMER from ");
			query.append(dataLibrary).append(".E_ETSTAMK6 order by BENDL ");
			//			query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");

			List<SupplierDetails> supplierDetailsList = dbServiceRepository.getResultUsingQuery(SupplierDetails.class, query.toString(), true);
			List<SupplierDetailsDTO> supplierList = new ArrayList<>();

			if(supplierDetailsList != null && !supplierDetailsList.isEmpty()) {
				for(SupplierDetails supplierDetails : supplierDetailsList) {

					SupplierDetailsDTO supplierDetailsDto = new SupplierDetailsDTO();

					supplierDetailsDto.setSupplierNo(supplierDetails.getSupplierNo().toString());
					supplierDetailsDto.setSupplierName1(supplierDetails.getSupplierName1());
					supplierDetailsDto.setSupplierName2(supplierDetails.getSupplierName2());
					supplierDetailsDto.setStreet(supplierDetails.getStreet());
					supplierDetailsDto.setOrt(supplierDetails.getOrt());

					supplierList.add(supplierDetailsDto);
				}

				globalSearchList.setSearchDetailsList(supplierList);
				globalSearchList.setTotalPages(Integer.toString(supplierDetailsList.get(0).getTotalCount()));
				globalSearchList.setTotalRecordCnt(Integer.toString(supplierDetailsList.get(0).getTotalCount()));

			}else{
				globalSearchList.setSearchDetailsList(supplierList);
				globalSearchList.setTotalPages(Integer.toString(0));
				globalSearchList.setTotalRecordCnt(Integer.toString(0));
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Lieferant Details (Supplier)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Lieferant Details (Supplier)"), exception);
			throw exception;
		}

		return globalSearchList;
	}


	/**
	 * This method is used to get Departures BA (20/22/09 ) details (Abgänge BA) from DB.
	 */
	@Override
	public DeparturesBA_DTO getDeparturesBADetails(String dataLibrary, String companyId, String agencyId,
			String manufacturer, String warehouseNumber,String partNumber, String businessCase  ) {
		log.info("Inside getDeparturesBADetails method of BusinessCasesImpl");

		DeparturesBA_DTO departuresBA_obj = new DeparturesBA_DTO();
		try {
			manufacturer = manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING) ? RestInputConstants.DCAG_STRING : manufacturer;
			businessCase =   StringUtils.leftPad(businessCase, 2, "0");

			if(manufacturer.equalsIgnoreCase(RestInputConstants.DCAG_STRING) && businessCase.equalsIgnoreCase("09") ) {

				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.BA09_VALIDATION_FAILED_MSG_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.BA09_VALIDATION_FAILED_MSG_KEY), exception);
				throw exception;
			}

			if (businessCase.equalsIgnoreCase("20") || businessCase.equalsIgnoreCase("22") || businessCase.equalsIgnoreCase("09")) {
				departuresBA_obj = businessCase09Or20Or22Module( dataLibrary, manufacturer, warehouseNumber, partNumber);
			} 
			else {
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.INVALID_MSG_KEY, " Business Case (BA) value"));
				log.error(messageService.getReadableMessage(ExceptionMessages.INVALID_MSG_KEY, " Business Case (BA) value"), exception);
				throw exception;

			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, " Departures BA"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, " Departures BA"), exception);
			throw exception;
		}

		return departuresBA_obj;

	}


	private DeparturesBA_DTO businessCase09Or20Or22Module(String dataLibrary, String manufacturer,
			String warehouseNumber, String partNumber) {

		log.info("Inside businessCase09Or20Or22Module method of BusinessCasesImpl");

		DeparturesBA_DTO departuresBA_Obj = new DeparturesBA_DTO();

		StringBuilder query = new StringBuilder(" SELECT TNR, BENEN, BESAUS, AKTBES, EPR, DAK FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE ");
		query.append("HERST = ").append("'"+manufacturer+"'");
		query.append(" AND LNR = ").append(warehouseNumber);
		query.append(" AND TNR = ").append("'"+partNumber+"'");

		List<PartDetails> departuresBAList = dbServiceRepository.getResultUsingQuery(PartDetails.class, query.toString(), true);

		//if the list is not empty
		if (departuresBAList != null && !departuresBAList.isEmpty()) {

			for(PartDetails departuresPartBA : departuresBAList){

				departuresBA_Obj.setPartNumber(departuresPartBA.getPartNumber());
				departuresBA_Obj.setPartName(departuresPartBA.getName());	//BENEN
				departuresBA_Obj.setPendingOrders(String.valueOf(departuresPartBA.getPendingOrders()));	//BESAUS
				departuresBA_Obj.setCurrentStock(String.valueOf(departuresPartBA.getCurrentStock()));	//AKTBES
				departuresBA_Obj.setListPrice(decimalformat_twodigit.format(departuresPartBA.getPurchasePrice() != null ?departuresPartBA.getPurchasePrice(): "0.00"));	//EPR
				departuresBA_Obj.setAverageNetPrice(String.valueOf(departuresPartBA.getAverageNetPrice()));	//DAK

			}
		}else{
			log.info("Part number not yet created in ETSTAMM");
			AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.BUSINESS_CASES_05_FAILED_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.BUSINESS_CASES_05_FAILED_MSG_KEY));
			throw exception;
		}

		return departuresBA_Obj;
	}


	/**
	 * This method is used to get MasterData BA (40/44/45/49 ) details (Stammdaten BA) from DB.
	 */
	@Override
	public MasterDataBA_DTO getMasterDataBADetails(String dataLibrary, String schema, String companyId, String agencyId,
			String manufacturer, String warehouseNumber,String partNumber, String businessCase  ) {
		log.info("Inside getMasterDataBADetails method of BusinessCasesImpl");

		MasterDataBA_DTO masterDataBA_obj = new MasterDataBA_DTO();
		try {
			manufacturer = manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING) ? RestInputConstants.DCAG_STRING : manufacturer;
			businessCase =   StringUtils.leftPad(businessCase, 2, "0");

			if (businessCase.equalsIgnoreCase("40") ) {

				masterDataBA_obj = businessCase40Or44Module( dataLibrary, manufacturer, warehouseNumber, partNumber);
				masterDataBA_obj = getPriceIndicator(masterDataBA_obj);
				masterDataBA_obj = checkPurchasePriceTBEditable(masterDataBA_obj, schema);
				masterDataBA_obj = checknetPriceTBEditable(masterDataBA_obj, schema);
			} 
			else if (businessCase.equalsIgnoreCase("44") ) { 
				masterDataBA_obj = businessCase40Or44Module( dataLibrary, manufacturer, warehouseNumber, partNumber);
				masterDataBA_obj = checkPurchasePriceTBEditable(masterDataBA_obj, schema);
			}
			else if ( businessCase.equalsIgnoreCase("49")) {
				masterDataBA_obj = businessCase49Module( dataLibrary, manufacturer, warehouseNumber, partNumber);
			}
			else if ( businessCase.equalsIgnoreCase("45")) {

				masterDataBA_obj = businessCase45Module( dataLibrary, manufacturer, warehouseNumber, partNumber);
			}
			else {
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.INVALID_MSG_KEY, " Business Case (BA) value"));
				log.error(messageService.getReadableMessage(ExceptionMessages.INVALID_MSG_KEY, " Business Case (BA) value"), exception);
				throw exception;

			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, " MasterData BA"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, " MasterData BA"), exception);
			throw exception;
		}

		return masterDataBA_obj;

	}


	private MasterDataBA_DTO businessCase40Or44Module(String dataLibrary, String manufacturer,
			String warehouseNumber, String partNumber) {

		log.info("Inside businessCase40Or44Module method of BusinessCasesImpl");

		MasterDataBA_DTO masterDataBA_DTO = new MasterDataBA_DTO();

		StringBuilder query = new StringBuilder(" select TNR, BENEN, BESAUS, AKTBES, EPR, DAK, RG, TMARKE, TA, LEIART, ");
		query.append("LIWERK, LOPA, PRKZ, VERP1, ZAEGRU, DISPO, MODUS, MC, MWST, KSPKZ, ABSCH, KDABS, EKNPR, NPREIS, BESVER, WINTER  from ").append(dataLibrary).append(".E_ETSTAMM WHERE ");
		query.append("HERST = ").append("'"+manufacturer+"'");
		query.append(" AND LNR = ").append(warehouseNumber);
		query.append(" AND TNR = ").append("'"+partNumber+"'");

		List<PartDetails> masterDataBAList = dbServiceRepository.getResultUsingQuery(PartDetails.class, query.toString(), true);

		//if the list is not empty
		if (masterDataBAList != null && !masterDataBAList.isEmpty()) {

			for(PartDetails masterBA : masterDataBAList){

				masterDataBA_DTO.setPartNumber(masterBA.getPartNumber()); //TNR
				masterDataBA_DTO.setDesignation(masterBA.getName());	//BENEN
				masterDataBA_DTO.setPurchasePrice(decimalformat_twodigit.format(masterBA.getPurchasePrice()!=null ? masterBA.getPurchasePrice(): "0.00"));	//EPR
				masterDataBA_DTO.setDiscountGroup(masterBA.getDiscountGroup()); //RG
				masterDataBA_DTO.setPartBrand(masterBA.getOemBrand()); //TMARKE
				masterDataBA_DTO.setPartType(String.valueOf(masterBA.getPartsIndikator()));	//TA
				masterDataBA_DTO.setActivityType(String.valueOf(masterBA.getActivityType())); 	//LEIART
				masterDataBA_DTO.setDeliveryPlant_DAG(String.valueOf(masterBA.getDeliverIndicator()));	//LIWERK
				masterDataBA_DTO.setStorageLocation(masterBA.getStorageLocation()); //LOPA
				masterDataBA_DTO.setPriceIndicator(masterBA.getPriceMark());	//PRKZ
				masterDataBA_DTO.setPackingUnit1(String.valueOf(masterBA.getPackagingUnit1()));	//VERP1
				masterDataBA_DTO.setCountGroupInventory(masterBA.getInventoryGroup());	//ZAEGRU
				masterDataBA_DTO.setDispositionIndicator(masterBA.getDispoMark());	//DISPO
				masterDataBA_DTO.setDispositionMode(masterBA.getMode());		//MODUS
				masterDataBA_DTO.setMarketingCode(masterBA.getMarketingCode());	//MC
				masterDataBA_DTO.setVatCode(String.valueOf(masterBA.getValueAddedTax()));	//MWST
				masterDataBA_DTO.setBlockingIndicator(masterBA.getCalculationLock());	//KSPKZ
				masterDataBA_DTO.setCustomerDiscountForSERV(String.valueOf(masterBA.getSurchargeDeduction())); //ABSCH
				masterDataBA_DTO.setDiscountForSERV(String.valueOf(masterBA.getSurchargeDeductionMark())); 	//KDABS
				masterDataBA_DTO.setNetShoppingPrice(decimalformat_twodigit.format(masterBA.getNetPrice()!=null ? masterBA.getNetPrice() : "0.00"));  //EKNPR
				masterDataBA_DTO.setSelfCalculatedNetPrice(decimalformat_twodigit.format(masterBA.getPreviousPurchasePrice()));   //NPREIS
				masterDataBA_DTO.setOrderNote(masterBA.getOrderAnnotation());		//BESVER
				masterDataBA_DTO.setSeasonalLicensePlate(masterBA.getCampaignMark());		//WINTER
				masterDataBA_DTO.setManufacturer(manufacturer);

			}
		}else{
			log.info("Part number not yet created in ETSTAMM");
			AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.BUSINESS_CASES_05_FAILED_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.BUSINESS_CASES_05_FAILED_MSG_KEY));
			throw exception;
		}

		return masterDataBA_DTO;
	}


	private MasterDataBA_DTO businessCase49Module(String dataLibrary, String manufacturer,
			String warehouseNumber, String partNumber) {

		log.info("Inside businessCase49Module method of BusinessCasesImpl");

		MasterDataBA_DTO masterDataBA_Obj = new MasterDataBA_DTO();

		StringBuilder query = new StringBuilder(" SELECT BENEN, BESAUS, AKTBES, DAK, SA FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE ");
		query.append("HERST = ").append("'"+manufacturer+"'");
		query.append(" AND LNR = ").append(warehouseNumber);
		query.append(" AND TNR = ").append("'"+partNumber+"'");

		List<PartDetails> mastersBAList = dbServiceRepository.getResultUsingQuery(PartDetails.class, query.toString(), true);

		//if the list is not empty
		if (mastersBAList != null && !mastersBAList.isEmpty()) {

			for(PartDetails mastersBA : mastersBAList){

				masterDataBA_Obj.setPartNumber(mastersBA.getPartNumber());
				masterDataBA_Obj.setDesignation(mastersBA.getName()); //BENEN
				masterDataBA_Obj.setAvgAcquisitionCost(String.valueOf(mastersBA.getAverageNetPrice())); //DAK
				masterDataBA_Obj.setSa(String.valueOf(mastersBA.getStorageIndikator())); //SA
				masterDataBA_Obj.setBackorder(String.valueOf(mastersBA.getPendingOrders())); //BESAUS
				masterDataBA_Obj.setCurrentStock(String.valueOf(mastersBA.getCurrentStock())); //AKTBES

			}
		}else{
			log.info("Part number not yet created in ETSTAMM");
			AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.BUSINESS_CASES_05_FAILED_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.BUSINESS_CASES_05_FAILED_MSG_KEY));
			throw exception;
		}

		return masterDataBA_Obj;
	}


	private MasterDataBA_DTO businessCase45Module(String dataLibrary, String manufacturer,
			String warehouseNumber, String partNumber) {

		log.info("Inside businessCase45Module method of BusinessCasesImpl");

		MasterDataBA_DTO departuresBA_Obj = new MasterDataBA_DTO();

		StringBuilder query = new StringBuilder("Select TNR, BENEN, TNRN, TNRV ");
		query.append(" from ").append(dataLibrary).append(".E_ETSTAMM et where ");
		query.append("HERST = ").append("'"+manufacturer+"'");
		query.append(" AND LNR = ").append(warehouseNumber);
		query.append(" AND TNR = ").append("'"+partNumber+"'");

		List<PartDetails> departuresBAList = dbServiceRepository.getResultUsingQuery(PartDetails.class, query.toString(), true);


		//if the list is not empty
		if (departuresBAList != null && !departuresBAList.isEmpty()) {

			for(PartDetails departuresPartBA : departuresBAList){

				departuresBA_Obj.setPartNumber(departuresPartBA.getPartNumber());	//TNR
				departuresBA_Obj.setDesignation(departuresPartBA.getName());			//BENEN
				departuresBA_Obj.setSuccessorPartNo(departuresPartBA.getSuccessorPartNumber()); 	//TNRN
				departuresBA_Obj.setPredecessorPartNo(departuresPartBA.getPredecessorPartNumber());	//TNRV

			}
		}else{
			log.info("Part number not yet created in ETSTAMM");
			AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.BUSINESS_CASES_05_FAILED_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.BUSINESS_CASES_05_FAILED_MSG_KEY));
			throw exception;
		}

		return departuresBA_Obj;
	}


	private MasterDataBA_DTO getPriceIndicator(MasterDataBA_DTO masterDataBA_obj) {

		String inputString = StringUtils.rightPad("00", 2, "0")
				+ StringUtils.rightPad("", 28, " ")
				+ StringUtils.rightPad("01", 2, " ")
				+ StringUtils.rightPad(masterDataBA_obj.getPartBrand(), 2, " ")
				+ StringUtils.rightPad(masterDataBA_obj.getDiscountGroup(), 5, " ")
				+ StringUtils.rightPad(masterDataBA_obj.getPartNumber(), 19, " ")
				+ StringUtils.rightPad("", 4, " ")
				+ StringUtils.rightPad(masterDataBA_obj.getMarketingCode(), 6, " ")
				+ StringUtils.rightPad(masterDataBA_obj.getManufacturer(), 4, " ")
				+ StringUtils.rightPad("",32695, " ");

		log.info("Final inputString : {}", inputString);
		log.info("Final inputString Length : {}", inputString.length());

		try {
			ProgramParameter[] parmList = new ProgramParameter[1];

			// Create the input parameter  
			parmList[0] = new ProgramParameter(inputString.getBytes(Program_Commands_Constants.IBM_273), 3);

			List<String> outputList = cobolServiceRepository.executeProgram(parmList, 0, Program_Commands_Constants.BSN477CL_PROGRAM);
			String output = outputList.get(0);
			log.info("output - {} ", output);

			String priceIndicator = output.substring(60, 62);
			log.info("priceIndicator:- {}", priceIndicator);

			if(priceIndicator.equalsIgnoreCase("M ")) {
				masterDataBA_obj.setPriceMark1("M");
			}
			else {
				masterDataBA_obj.setPriceMark1(" ");
			}

		}catch(AlphaXException ex) {
			throw ex;
		}catch (Exception e) {
			log.info("Error Message:"+e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.BSN477CL_PROGRAM_FAILED_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.BSN477CL_PROGRAM_FAILED_MSG_KEY), exception);
			throw exception;
		}

		return masterDataBA_obj;
	}


	private MasterDataBA_DTO checkPurchasePriceTBEditable(MasterDataBA_DTO masterDataBA_obj, String schema) {

		StringBuilder query = new StringBuilder("SELECT OBJ_SACHGB, OBJ_UBWEIN FROM ").append(schema).append(".UBF_OBJEKT WHERE ");
		query.append(" OBJ_OBJTYP = 'FU' AND OBJ_OBJEKT = 'EPRAEND' ");

		dbServiceRepository.getResultUsingQuery(PartDetails.class, query.toString(), true);

		masterDataBA_obj.setPurchasePriceTBEditable(false);


		return masterDataBA_obj;
	}

	private MasterDataBA_DTO checknetPriceTBEditable(MasterDataBA_DTO masterDataBA_obj, String schema) {

		StringBuilder query = new StringBuilder("SELECT OBJ_SACHGB, OBJ_UBWEIN FROM ").append(schema).append(".UBF_OBJEKT WHERE ");
		query.append(" OBJ_OBJTYP = 'FU' AND OBJ_OBJEKT = 'EKNPRAEND' ");

		dbServiceRepository.getResultUsingQuery(PartDetails.class, query.toString(), true);

		masterDataBA_obj.setNetPriceTBEditable(false);

		return masterDataBA_obj;
	}


	/**
	 * This method is used to get Departures BA (25) details (Abgänge BA) from DB.
	 */
	@Override
	public DeparturesBA_DTO getDeparturesBA25Details(String dataLibrary, String companyId, String agencyId,
			String manufacturer, String warehouseNumber, String partNumber, String supplierNo, String deliveryNoteNo, String processCode, String businessCase  ) {
		log.info("Inside getDeparturesBA25Details method of BusinessCasesImpl");

		DeparturesBA_DTO departuresBA_obj = new DeparturesBA_DTO();
		try {
			manufacturer = manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING) ? RestInputConstants.DCAG_STRING : manufacturer;
			businessCase =   StringUtils.leftPad(businessCase, 2, "0");

			if (businessCase.equalsIgnoreCase("25")) {

				StringBuilder query = new StringBuilder(" SELECT TNR, BENEN, BESAUS, AKTBES, EPR, DAK ,SA, TMARKE, RG, MC, LEKPR, TA, LIWERK, ");
				query.append(" EKNPR, NPREIS, TNRS, TNRD,FIRMA, FILIAL, DTLBEW, LEIART FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE ");
				query.append("HERST = ").append("'"+manufacturer+"'");
				query.append(" AND LNR = ").append(warehouseNumber);
				query.append(" AND TNR = ").append("'"+partNumber+"'");

				List<PartDetails> departuresBAList = dbServiceRepository.getResultUsingQuery(PartDetails.class, query.toString(), true);

				//if the list is not empty
				if (departuresBAList != null && !departuresBAList.isEmpty()) {

					for(PartDetails departuresPartBA : departuresBAList){

						departuresBA_obj.setPartNumber(departuresPartBA.getPartNumber());//TNR
						departuresBA_obj.setPartName(departuresPartBA.getName());	//BENEN
						departuresBA_obj.setPendingOrders(String.valueOf(departuresPartBA.getPendingOrders()));	//BESAUS
						departuresBA_obj.setCurrentStock(String.valueOf(departuresPartBA.getCurrentStock()));	//AKTBES
						departuresBA_obj.setListPrice(decimalformat_twodigit.format(departuresPartBA.getPurchasePrice() != null ? departuresPartBA.getPurchasePrice(): "0.00"));  //EPR mwst
						departuresBA_obj.setAverageNetPrice(String.valueOf(departuresPartBA.getAverageNetPrice()));	//DAK
						departuresBA_obj.setStorageIndikator(String.valueOf(departuresPartBA.getStorageIndikator())); //SA
						departuresBA_obj.setDiscountGroup(departuresPartBA.getDiscountGroup()!=null ? departuresPartBA.getDiscountGroup() : "" ); // RG
						departuresBA_obj.setOemBrand(departuresPartBA.getOemBrand()!=null ? departuresPartBA.getOemBrand() : "" );
						departuresBA_obj.setMarketingCode(departuresPartBA.getMarketingCode());
						departuresBA_obj.setLastPurchasePrice(String.valueOf(departuresPartBA.getLastPurchasePrice())); //LEKPR
						departuresBA_obj.setPartsIndikator(departuresPartBA.getPartsIndikator()!= null ? String.valueOf(departuresPartBA.getPartsIndikator()):"");//TA
						departuresBA_obj.setDeliverIndicator(departuresPartBA.getDeliverIndicator()!=null ? String.valueOf(departuresPartBA.getDeliverIndicator()) :"");//LIWERK
						departuresBA_obj.setNetPrice(String.valueOf(departuresPartBA.getNetPrice()));//EKNPR
						departuresBA_obj.setPreviousPurchasePrice(String.valueOf(departuresPartBA.getPreviousPurchasePrice())); //NPREIS
						//departuresBA_obj.setOldPrice(departuresPartBA.getOldPrice()); // EPREIS
						departuresBA_obj.setSortingFormatPartNumber(departuresPartBA.getSortingFormatPartNumber()); //TNRS
						departuresBA_obj.setPrintingFormatPartNumber(departuresPartBA.getPrintingFormatPartNumber()); //TNRD
						departuresBA_obj.setCompany(String.valueOf(departuresPartBA.getCompany()));//FIRMA
						departuresBA_obj.setAgency(String.valueOf(departuresPartBA.getBranch())); //FILALE
						departuresBA_obj.setActivityType(String.valueOf(departuresPartBA.getActivityType()));
						departuresBA_obj.setMovementDate(String.valueOf(departuresPartBA.getLastMovementDate()));
					}

					getNetPricefromDB(dataLibrary, manufacturer, warehouseNumber, departuresBA_obj, partNumber, supplierNo, deliveryNoteNo, processCode);

				}
				else {
					log.info("Part number not yet created in ETSTAMM");
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.BUSINESS_CASES_05_FAILED_MSG_KEY));
					log.error(messageService.getReadableMessage(ExceptionMessages.BUSINESS_CASES_05_FAILED_MSG_KEY));
					throw exception;
				}

			} 
			else {
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.INVALID_MSG_KEY, " Business Case (BA) value"));
				log.error(messageService.getReadableMessage(ExceptionMessages.INVALID_MSG_KEY, " Business Case (BA) value"), exception);
				throw exception;

			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, " Departures BA"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, " Departures BA"), exception);
			throw exception;
		}

		return departuresBA_obj;
	}


	private DeparturesBA_DTO getNetPricefromDB(String dataLibrary, String manufacturer, String warehouseNumber, DeparturesBA_DTO departuresBA_obj,  
			String partNumber, String supplierNo, String deliveryNoteNo, String processCode) {

		List<BADeliveryNotesDTO> baDeliveryList = new ArrayList<>();

		if(processCode.equalsIgnoreCase("6") || processCode.equalsIgnoreCase("7")) {

			StringBuilder query = new StringBuilder(" Select AA, AUFTRAG, ZUMENG, EPREIS, NEPREIS, RECDAT, RECTIME from ").append(dataLibrary).append(".E_LFSL where ");
			query.append("HERST = ").append("'"+manufacturer+"'");
			query.append(" AND LNR = ").append(warehouseNumber);
			query.append(" AND TNR = ").append("'"+partNumber+"'");
			if(processCode.equalsIgnoreCase("6")) {
				query.append(" AND BENDL = ").append(supplierNo);
			}
			else if (processCode.equalsIgnoreCase("7")) { 
				query.append(" AND BENDL like '%%%%%' ");
			}
			query.append(" AND NRX = ").append("'"+deliveryNoteNo+"'");
			query.append(" AND CONCAT(AA,AUFTRAG) > '000000' ");

			List<BADeliveryNotes> BADeliveryNotesList = dbServiceRepository.getResultUsingQuery(BADeliveryNotes.class, query.toString(), true);

			if (BADeliveryNotesList != null && !BADeliveryNotesList.isEmpty()) {

				if(BADeliveryNotesList.size() > 1) {

					for(BADeliveryNotes baDeliveryNotes : BADeliveryNotesList) {

						BADeliveryNotesDTO baDeliveryNotesDTO = new BADeliveryNotesDTO(); 

						baDeliveryNotesDTO.setOrderType(StringUtils.leftPad(String.valueOf(baDeliveryNotes.getOrderType()), 2, "0"));      //AA
						baDeliveryNotesDTO.setOrderNumber(StringUtils.leftPad(String.valueOf(String.valueOf(baDeliveryNotes.getOrderNumber())), 4, "0"));  //AUFTRAG

						baDeliveryNotesDTO.setDeliveredQuantity(String.valueOf(baDeliveryNotes.getDeliveredQuantity()));   //ZUMENG
						baDeliveryNotesDTO.setOldPrice(String.valueOf(baDeliveryNotes.getOldPrice()));   //EPREIS
						baDeliveryNotesDTO.setNetPurchasePrice(String.valueOf(baDeliveryNotes.getNetPurchasePrice()));		 //NEPREIS	

						String recDate = convertDateToString(String.valueOf(baDeliveryNotes.getReceiptDate()));   //RECDAT
						String recTime = convertTimeToString(String.valueOf(baDeliveryNotes.getReceiptTime()));  //RECTIME

						baDeliveryNotesDTO.setReceiptDateTime(recDate +" "+recTime);

						baDeliveryList.add(baDeliveryNotesDTO);
					}

					departuresBA_obj.setBaDeliveryNotesList(baDeliveryList);

				}
				if(BADeliveryNotesList.size() == 1) {

					String OrderType = StringUtils.leftPad(String.valueOf(BADeliveryNotesList.get(0).getOrderType()), 2, "0");
					String OrderNumber = StringUtils.leftPad(String.valueOf(BADeliveryNotesList.get(0).getOrderNumber()), 4, "0");

					departuresBA_obj.setPurchaseOrderNumber(StringUtils.join(OrderType, OrderNumber));
					departuresBA_obj.setNetShoppingPrice(String.valueOf(BADeliveryNotesList.get(0).getNetPurchasePrice()));
					departuresBA_obj.setListPrice(String.valueOf(BADeliveryNotesList.get(0).getOldPrice()));
				}
			}
			else {
				departuresBA_obj.setNetShoppingPrice("00000");
				departuresBA_obj.setPurchaseOrderNumber("");
			}
		}
		else if(processCode.equalsIgnoreCase("8") || processCode.equalsIgnoreCase("9") || processCode.equalsIgnoreCase("Ü")) {

			departuresBA_obj.setNetShoppingPrice("00000");
			departuresBA_obj.setPurchaseOrderNumber("");
		}

		return departuresBA_obj;
	}


	/**
	 * This method is used to convert Time string hhmmss  to hh:mm:ss
	 * @param entityTime
	 * @return
	 */
	private String convertTimeToString(String entityTime) {

		StringBuilder timeBuilder = new StringBuilder();

		if(entityTime!=null && !entityTime.isEmpty()) {

			if(entityTime.length() == 6) {
				timeBuilder.append(entityTime.substring(0, 2)).append(":");
				timeBuilder.append(entityTime.substring(2, 4)).append(":").append(entityTime.substring(4, 6));
			}

			if(entityTime.length() == 5) {
				timeBuilder.append("0").append(entityTime.substring(0, 1)).append(":");
				timeBuilder.append(entityTime.substring(1, 3)).append(":").append(entityTime.substring(3, 5));
			}			
		}
		return timeBuilder.toString();
	}


	private AccessesBA_DTO getNetPricefromDBForBA06(String dataLibrary, String manufacturer, String warehouseNumber, AccessesBA_DTO accessBA_obj,  
			String partNumber, String supplierNo, String deliveryNoteNo) {

		List<BADeliveryNotesDTO> baDeliveryList = new ArrayList<>();


		StringBuilder query = new StringBuilder(" Select AA, AUFTRAG, ZUMENG, EPREIS, NEPREIS, RECDAT, RECTIME from ").append(dataLibrary).append(".E_LFSL where ");
		query.append("HERST = ").append("'"+manufacturer+"'");
		query.append(" AND LNR = ").append(warehouseNumber);
		query.append(" AND TNR = ").append("'"+partNumber+"'");
		query.append(" AND BENDL = ").append(supplierNo);
		query.append(" AND NRX = ").append("'"+deliveryNoteNo+"'");
		query.append(" AND CONCAT(AA,AUFTRAG) > '000000' ");

		List<BADeliveryNotes> BADeliveryNotesList = dbServiceRepository.getResultUsingQuery(BADeliveryNotes.class, query.toString(), true);

		if (BADeliveryNotesList != null && !BADeliveryNotesList.isEmpty()) {

			if(BADeliveryNotesList.size() > 1) {

				for(BADeliveryNotes baDeliveryNotes : BADeliveryNotesList) {

					BADeliveryNotesDTO baDeliveryNotesDTO = new BADeliveryNotesDTO(); 

					baDeliveryNotesDTO.setOrderType(StringUtils.leftPad(String.valueOf(baDeliveryNotes.getOrderType()), 2, "0"));      //AA
					baDeliveryNotesDTO.setOrderNumber(StringUtils.leftPad(String.valueOf(String.valueOf(baDeliveryNotes.getOrderNumber())), 4, "0"));  //AUFTRAG

					baDeliveryNotesDTO.setDeliveredQuantity(String.valueOf(baDeliveryNotes.getDeliveredQuantity()));   //ZUMENG
					baDeliveryNotesDTO.setOldPrice(String.valueOf(baDeliveryNotes.getOldPrice()));   //EPREIS
					baDeliveryNotesDTO.setNetPurchasePrice(String.valueOf(baDeliveryNotes.getNetPurchasePrice()));		 //NEPREIS	

					String recDate = convertDateToString(String.valueOf(baDeliveryNotes.getReceiptDate()));   //RECDAT
					String recTime = convertTimeToString(String.valueOf(baDeliveryNotes.getReceiptTime()));  //RECTIME

					baDeliveryNotesDTO.setReceiptDateTime(recDate +" "+recTime);

					baDeliveryList.add(baDeliveryNotesDTO);
				}

				accessBA_obj.setBaDeliveryNotesList(baDeliveryList);

			}
			if(BADeliveryNotesList.size() == 1) {

				String OrderType = StringUtils.leftPad(String.valueOf(BADeliveryNotesList.get(0).getOrderType()), 2, "0");
				String OrderNumber = StringUtils.leftPad(String.valueOf(BADeliveryNotesList.get(0).getOrderNumber()), 4, "0");

				accessBA_obj.setListPriceWithoutVAT(String.valueOf(BADeliveryNotesList.get(0).getOldPrice()));   //EPREIS
				accessBA_obj.setNetShoppingPrice(String.valueOf(BADeliveryNotesList.get(0).getNetPurchasePrice()));	
				accessBA_obj.setPurchaseOrderNumber(StringUtils.join(OrderType, OrderNumber));
			}
		}
		else {
			accessBA_obj.setNetShoppingPrice("00000");
			accessBA_obj.setPurchaseOrderNumber("");
		}


		return accessBA_obj;
	}



	/**
	 * This method is used to get VAT Registration Number List from DB
	 * @return List
	 */

	@Override
	public List<DropdownObject> getVATRegistrationNumber(String schema, String dataLibrary) {

		log.info("Inside getVATRegistrationNumber method of BusinessCasesImpl");

		List<DropdownObject> vatRegistrationNumberList = new ArrayList<>();

		try {
			StringBuilder query = new StringBuilder("SELECT SUBSTR(KEYFLD, 11, 2) AS KEY, SUBSTR(DATAFLD, 1, 2) AS VALUE FROM ").append(dataLibrary).append(".REFERENZ ");
			query.append(" WHERE SUBSTR(KEYFLD, 7, 4) = '9907' ");

			List<AlphaXConfigurationKeysDetails> taxesList = dbServiceRepository.getResultUsingQuery(AlphaXConfigurationKeysDetails.class, query.toString(), true);

			if (taxesList != null && !taxesList.isEmpty()) {

				for(AlphaXConfigurationKeysDetails taxesObject : taxesList){
					DropdownObject dropdownObject = new DropdownObject();
					dropdownObject.setKey(taxesObject.getKey());
					dropdownObject.setValue(taxesObject.getValue());

					vatRegistrationNumberList.add(dropdownObject);
				}
			}else{
				log.info("VAT Registration Number List List is empty");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "VAT Registration Number"));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "VAT Registration Number"));
				throw exception;
			} 

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "VAT Registration Number list"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"VAT Registration Number list"), exception);
			throw exception;
		}

		return vatRegistrationNumberList;
	}


	/**
	 * This method is used to get the Abnehmergruppe (Customer Group) List from Stub
	 * @return List
	 */
	@Override
	public List<DropdownObject> getCustomerGroupList(String businessCase) {

		log.info("Inside getCustomerGroupList method of BusinessCasesImpl");

		List<DropdownObject> customerGroupList = new ArrayList<>();

		if(businessCase.equalsIgnoreCase("20") || businessCase.equalsIgnoreCase("22") || businessCase.equalsIgnoreCase("05") || businessCase.equalsIgnoreCase("5")) {

			customerGroupList = stubServiceRepository.getDDLValuesFromStub(StubServiceRepository.customerGroupBA2022Map);
		}
		else if(businessCase.equalsIgnoreCase("25")) {
			customerGroupList = stubServiceRepository.getDDLValuesFromStub(StubServiceRepository.customerGroupBA25Map);
		}

		return customerGroupList;
	}


	/**
	 * This method is used to update Rebooking bundles in pieces BA (Umbuchen Gebinde in Stück BA) using COBOL Program.
	 */
	@Override
	public Map<String, String> updateRebookingBundlesBA(RebookingBundlesBA_DTO rebookingBundlesBA,String dataLibrary, String companyId, String agencyId ) {
		log.info("Inside updateRebookingBundlesBA method of BusinessCasesImpl");

		ProgramParameter[] parmList = new ProgramParameter[18];
		Map<String, String> programOutput = new HashMap<String, String>();

		try{

			String returnCode = StringUtils.rightPad("", 5, " ");
			parmList[0] = new ProgramParameter(returnCode.getBytes(Program_Commands_Constants.IBM_273), 5);

			String returnMsg = StringUtils.rightPad("", 100, " ");
			parmList[1] = new ProgramParameter(returnMsg.getBytes(Program_Commands_Constants.IBM_273), 100);

			String manufacturer = rebookingBundlesBA.getManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING : rebookingBundlesBA.getManufacturer();	
			manufacturer = StringUtils.rightPad(manufacturer, 4, " ");
			parmList[2] = new ProgramParameter(manufacturer.getBytes(Program_Commands_Constants.IBM_273), 4);

			String warehouseNumber = StringUtils.leftPad(rebookingBundlesBA.getWarehouseNumber(), 2, "0");
			parmList[3] = new ProgramParameter(warehouseNumber.getBytes(Program_Commands_Constants.IBM_273), 2);

			String deliveryNoteNo = StringUtils.rightPad(rebookingBundlesBA.getDeliveryNoteNo(), 10, " ");
			parmList[4] = new ProgramParameter(deliveryNoteNo.getBytes(Program_Commands_Constants.IBM_273), 10);

			String containerPartNo = StringUtils.rightPad(rebookingBundlesBA.getContainerPartNo(), 19, " ");
			parmList[5] = new ProgramParameter(containerPartNo.getBytes(Program_Commands_Constants.IBM_273), 19);

			String containerBestand =StringUtils.leftPad(rebookingBundlesBA.getContainerBestand(), 9, "0");
			parmList[6] = new ProgramParameter(containerBestand.getBytes(Program_Commands_Constants.IBM_273), 9);

			String containerDAK = StringUtils.leftPad(rebookingBundlesBA.getContainerDAK(), 11, "0");
			parmList[7] = new ProgramParameter(containerDAK.getBytes(Program_Commands_Constants.IBM_273), 11);

			String numberOfContainers = StringUtils.leftPad(rebookingBundlesBA.getNumberOfContainers(), 5, "0");
			parmList[8] = new ProgramParameter(numberOfContainers.getBytes(Program_Commands_Constants.IBM_273), 5);

			String contentsOfContainer = StringUtils.leftPad(rebookingBundlesBA.getContentsOfContainer(), 5, "0");
			parmList[9] = new ProgramParameter(contentsOfContainer.getBytes(Program_Commands_Constants.IBM_273), 5);

			String piecesPartNo = StringUtils.rightPad(rebookingBundlesBA.getPiecesPartNo(), 19, " ");
			parmList[10] = new ProgramParameter(piecesPartNo.getBytes(Program_Commands_Constants.IBM_273), 19);

			String piecesBestand = StringUtils.leftPad(rebookingBundlesBA.getPiecesBestand(), 8, "0");
			parmList[11] = new ProgramParameter(piecesBestand.getBytes(Program_Commands_Constants.IBM_273), 8);

			String piecesDAK = StringUtils.leftPad(rebookingBundlesBA.getPiecesDAK(), 11, "0");
			parmList[12] = new ProgramParameter(piecesDAK.getBytes(Program_Commands_Constants.IBM_273), 11);

			String piecesNetPrice = StringUtils.leftPad(rebookingBundlesBA.getPiecesNetPrice(), 10, "0");
			parmList[13] = new ProgramParameter(piecesNetPrice.getBytes(Program_Commands_Constants.IBM_273), 10);

			String piecesTransferAmount = StringUtils.leftPad(rebookingBundlesBA.getPiecesTransferAmount(), 8, "0");
			parmList[14] = new ProgramParameter(piecesTransferAmount.getBytes(Program_Commands_Constants.IBM_273), 8);

			String CreditValue = StringUtils.leftPad(rebookingBundlesBA.getCreditValue(), 5, "0");
			parmList[15] = new ProgramParameter(CreditValue.getBytes(Program_Commands_Constants.IBM_273), 5);

			String netshoppingPrice = StringUtils.leftPad(rebookingBundlesBA.getNetshoppingPrice(), 10, "0");
			parmList[16] = new ProgramParameter(netshoppingPrice.getBytes(Program_Commands_Constants.IBM_273), 10);

			String filler = StringUtils.rightPad("", 100, " ");
			parmList[17] = new ProgramParameter(filler.getBytes(Program_Commands_Constants.IBM_273), 100);

			//execute the COBOL program - OFSMZUG.PGM
			List<String> outputList = cobolServiceRepository.executeProgram(parmList, 0, Program_Commands_Constants.REBOOKIN_GBUNDLES_BA_PROGRAM );

			if(outputList != null && !outputList.isEmpty()) {
				if(!outputList.get(0).contains("Error")) {

					programOutput.put(outputList.get(0).trim(),outputList.get(1).trim());
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY,"Rebooking bundles in pieces BA (Umbuchen Gebinde in Stück BA)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY,"Rebooking bundles in pieces BA (Umbuchen Gebinde in Stück BA)"), exception);
			throw exception;
		}

		return programOutput;
	}

	/**
	 * This method is used to get Rebooking bundles in pieces BA (Umbuchen Gebinde in Stück BA) from DB.
	 */
	@Override
	public DeparturesBA_DTO getRebookingBundleDetails(String dataLibrary, String companyId, String agencyId,String manufacturer, String warehouseNumber, String partNumber) {
		log.info("Inside getRebookingBundleDetails method of BusinessCasesImpl");

		DeparturesBA_DTO departuresBA_obj = new DeparturesBA_DTO();
		try {
			manufacturer = manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING) ? RestInputConstants.DCAG_STRING : manufacturer;

			StringBuilder query = new StringBuilder(" SELECT BENEN, BESAUS, AKTBES, EPR, DAK FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE ");
			query.append("HERST = ").append("'"+manufacturer+"'");
			query.append(" AND LNR = ").append(warehouseNumber);
			query.append(" AND TNR = ").append("'"+partNumber+"'");

			List<PartDetails> departuresBAList = dbServiceRepository.getResultUsingQuery(PartDetails.class, query.toString(), true);

			//if the list is not empty
			if (departuresBAList != null && !departuresBAList.isEmpty()) {

				for(PartDetails departuresPartBA : departuresBAList){

					departuresBA_obj.setPartName(departuresPartBA.getName());	//BENEN
					departuresBA_obj.setPendingOrders(String.valueOf(departuresPartBA.getPendingOrders()));	//BESAUS
					departuresBA_obj.setCurrentStock(String.valueOf(departuresPartBA.getCurrentStock()));	//AKTBES
					departuresBA_obj.setListPrice(decimalformat_twodigit.format(departuresPartBA.getPurchasePrice() != null ? departuresPartBA.getPurchasePrice(): "0.00"));  //EPR
					departuresBA_obj.setAverageNetPrice(String.valueOf(departuresPartBA.getAverageNetPrice()));	//DAK
				}
			}
			else {
				log.info("Part number not yet created in ETSTAMM");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.BUSINESS_CASES_05_FAILED_MSG_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.BUSINESS_CASES_05_FAILED_MSG_KEY));
				throw exception;
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, " Rebooking bundles in pieces BA (Umbuchen Gebinde in Stück BA)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, " Rebooking bundles in pieces BA (Umbuchen Gebinde in Stück BA)"), exception);
			throw exception;
		}

		return departuresBA_obj;
	}


	/**
	 * This method is used to get the Lieferant (Suppliers) detail List for DDL based on searchString.
	 */
	@Override
	public GlobalSearch getSupplierListUsingSearchString(String dataLibrary, String companyId, String agencyId, String searchString) {

		log.info("Inside getSupplierList method of BusinessCasesImpl ");
		log.info("Get supplier list by searchString...");

		GlobalSearch globalSearchList = new GlobalSearch();

		try {

			StringBuilder query = new StringBuilder("select BENDL, NAME1, NAME2, STRAS, ORT, ");
			query.append("( SELECT COUNT(*) from ").append(dataLibrary).append(".E_ETSTAMK6 ");
			query.append(" WHERE  UPPER(BENDL) LIKE UPPER('%").append(searchString).append("%') ");
			query.append(" OR  UPPER(NAME1) LIKE UPPER('%").append(searchString).append("%') ");
			query.append(" OR  UPPER(NAME2) LIKE UPPER('%").append(searchString).append("%') ");
			query.append(" OR  UPPER(ORT) LIKE UPPER('%").append(searchString).append("%') ");
			query.append(" OR BENDL = ( CASE WHEN LENGTH(TRIM(TRANSLATE(REPLACE(TRIM('").append(searchString);
			query.append("'), ' ', 'x'), '           ', '0123456789')))=0 THEN CAST('").append(searchString).append("' as int) else 0 end) ");
			query.append(" ) AS ROWNUMER from ");
			query.append(dataLibrary).append(".E_ETSTAMK6 ");
			query.append(" WHERE  UPPER(BENDL) LIKE UPPER('%").append(searchString).append("%') ");
			query.append(" OR  UPPER(NAME1) LIKE UPPER('%").append(searchString).append("%') ");
			query.append(" OR  UPPER(ORT) LIKE UPPER('%").append(searchString).append("%') ");
			query.append(" OR BENDL = ( CASE WHEN LENGTH(TRIM(TRANSLATE(REPLACE(TRIM('").append(searchString);
			query.append("'), ' ', 'x'), '           ', '0123456789')))=0 THEN CAST('").append(searchString).append("' as int) else 0 end) ");
			query.append(" OR  UPPER(NAME2) LIKE UPPER('%").append(searchString).append("%') order by BENDL ");
			//			query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");

			List<SupplierDetails> supplierDetailsList = dbServiceRepository.getResultUsingQuery(SupplierDetails.class, query.toString(), true);
			List<SupplierDetailsDTO> supplierList = new ArrayList<>();

			if(supplierDetailsList != null && !supplierDetailsList.isEmpty()) {
				for(SupplierDetails supplierDetails : supplierDetailsList) {

					SupplierDetailsDTO supplierDetailsDto = new SupplierDetailsDTO();

					supplierDetailsDto.setSupplierNo(supplierDetails.getSupplierNo().toString());
					supplierDetailsDto.setSupplierName1(supplierDetails.getSupplierName1());
					supplierDetailsDto.setSupplierName2(supplierDetails.getSupplierName2());
					supplierDetailsDto.setStreet(supplierDetails.getStreet());
					supplierDetailsDto.setOrt(supplierDetails.getOrt());

					supplierList.add(supplierDetailsDto);
				}

				globalSearchList.setSearchDetailsList(supplierList);
				globalSearchList.setTotalPages(Integer.toString(supplierDetailsList.get(0).getTotalCount()));
				globalSearchList.setTotalRecordCnt(Integer.toString(supplierDetailsList.get(0).getTotalCount()));

			}else{
				globalSearchList.setSearchDetailsList(supplierList);
				globalSearchList.setTotalPages(Integer.toString(0));
				globalSearchList.setTotalRecordCnt(Integer.toString(0));
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Lieferant Details (Supplier)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Lieferant Details (Supplier)"), exception);
			throw exception;
		}

		return globalSearchList;
	}	


	/**
	 * This method is used to get list of active BAs
	 */
	@Override
	public List<BusinessCasesDTO> getActiveBA(String schema) {

		log.info("Inside getActiveBA method of BusinessCasesImpl ");

		List<BusinessCasesDTO> businessCasesList = new ArrayList<>();

		try {

			StringBuilder query = new StringBuilder("select BA, DESCRIPTION from ").append(schema).append(".O_BA ");
			query.append(" WHERE  ISACTIVE = 1 ORDER BY BA  ");

			List<ReportSelections> baList = dbServiceRepository.getResultUsingQuery(ReportSelections.class, query.toString(), true);

			if(baList != null && !baList.isEmpty()) {

				for(ReportSelections baObject:baList){
					BusinessCasesDTO businessCasedto = new BusinessCasesDTO(); 
					businessCasedto.setBusinessCaseKey(String.valueOf(baObject.getBusinessCaseKey()));
					businessCasedto.setBusinessCaseValue(baObject.getBusinessCaseValue());
					businessCasesList.add(businessCasedto);
				}
			}else{
				log.debug("BA List is empty");
				Message message = messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "BA");
				AlphaXException exception = new AlphaXException(message);
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "BA"), exception);
				throw exception;
			}
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "BA list"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "BA list"), exception);
			throw exception;
		}

		return businessCasesList;
	}	

	
	/**
	 * This method is used to get the default printer details from DB.
	 */
	@Override
	public Map<String, String> getDefaultPrinter(String dataLibrary, String warehouseNumber, Integer flag) {

		log.info("Inside getDefaultPrinter method of BusinessCasesImpl ");

		Map<String,String> defaultPrinterMap = new HashMap<>();
		StringBuilder query = new StringBuilder();
		
		try {
			//for sending warehouse
			if(flag == 0) {
				query = query.append("select substr(daten, 8,2) as printer from ").append(dataLibrary).append(".E_ETSTAMK3 ");
				query.append(" where VFNR =").append(warehouseNumber);
			}
			//for receiving warehouse
			else if(flag == 1) {
				query = query.append("select substr(daten, 12,2) as printer from ").append(dataLibrary).append(".E_ETSTAMK3 ");
				query.append(" where VFNR =").append(warehouseNumber);
			}

			List<PartRelocation> printerList = dbServiceRepository.getResultUsingQuery(PartRelocation.class, query.toString(), true);

			if(printerList != null && !printerList.isEmpty()) {

					defaultPrinterMap.put("defualtPrinter", printerList.get(0).getPrinter());
					
			}else{
				log.debug("Default Printer is not found");
				Message message = messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Default Printer");
				AlphaXException exception = new AlphaXException(message);
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Default Printer"), exception);
				throw exception;
			}
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Default Printer"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Default Printer"), exception);
			throw exception;
		}

		return defaultPrinterMap;
	}
	
	
	/**
	 * This method is used to get the company related Lager (Warehouse) detail List for DDL.
	 */
	@Override
	public List<LagerDetailsDTO> getComapnyBasedLagerList(String dataLibrary, String schema, String axCompanyId, String companyApWarehouses ) {

		log.info("Inside getComapnyBasedLagerList method of BusinessCasesImpl");

		List<LagerDetailsDTO> lagerList = new ArrayList<>();

		try {

			StringBuilder query = new StringBuilder("select WAREHOUS_ID, AP_WAREHOUS_ID, NAME, CITY, POST_CODE, STREETANDNUM, ISACTIVE, VF_NUMBER from ");
			query.append(schema).append(".O_WRH").append(" where ISACTIVE = 1 and AP_WAREHOUS_ID  IN ( ").append(companyApWarehouses).append(" ) order by AP_WAREHOUS_ID ");

			List<AdminWarehouse> lagerDetailsList = dbServiceRepository.getResultUsingQuery(AdminWarehouse.class, query.toString(), true);

			if(lagerDetailsList != null && !lagerDetailsList.isEmpty()) {
				for(AdminWarehouse lagerDetails : lagerDetailsList) {

					LagerDetailsDTO lagerDetailsDto = new LagerDetailsDTO();

					lagerDetailsDto.setWarehouseNo(StringUtils.leftPad(lagerDetails.getAlphaPlusWarehouseId().toString(),2,"0"));
					lagerDetailsDto.setWarehouseName(lagerDetails.getWarehouseName());
					lagerDetailsDto.setAddress(new StringBuilder(lagerDetails.getStreetNumber()).append(", ")
							.append(lagerDetails.getPostalCode()).append(" ").append(lagerDetails.getCity()).toString());
					lagerDetailsDto.setVfNumber(String.valueOf(lagerDetails.getVfNumber()));

					lagerList.add(lagerDetailsDto);
				}

			}else{
				log.info("Company Lager Details List is empty");
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
	
	/**
	 * This method is used to Update Delivery Note Status (Business Cases:-[01/02 and 25]).
	 */
	@Override
	public Map<String, Boolean> updateDeliveryNoteStatus(DeliveryNotes_BA_DTO  ba_dto, String dataLibrary) {
		log.info("Inside updateDeliveryNoteStatus method of BusinessCasesImpl");

		Map<String, Boolean> updateFlag = new HashMap<String, Boolean>();
		updateFlag.put("isUpdated", false);

		try{
			
			StringBuilder query = new StringBuilder("UPDATE ").append(dataLibrary).append(".E_BSNLFS SET ");
			query.append(" PRUEFUNG = 'N', ERLEDIGT = 'N' WHERE HERST = '").append(ba_dto.getManufacturer()).append("' "); 
			query.append("AND LNR=").append(ba_dto.getWarehouseNumber());
			query.append(" AND BENDL=").append(ba_dto.getSupplierNumber());
			query.append(" AND NRX= '").append(ba_dto.getDeliveryNoteNo()).append("' ");
			query.append("AND ART= '").append(ba_dto.getProgramName()).append("' ");
			query.append("AND LAUFNR= '").append(ba_dto.getLaufnr()).append("' ");
			query.append("AND BA= '").append(ba_dto.getBusinessCase()).append("' ");
			query.append("AND REKOW= '").append(ba_dto.getRekow()).append("' ");
			
			if(ba_dto!=null && ba_dto.getStatus()!= null && ba_dto.getStatus().equalsIgnoreCase("Lieferscheinprüfung durchgeführt")){
			boolean flag = dbServiceRepository.updateResultUsingQuery(query.toString());
			if(flag){
				updateFlag.put("isUpdated", true);
			}
		}
			
			
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY,"Delivery Note Status"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY,"Delivery Note Status"), exception);
			throw exception;
		}

		return updateFlag;
	}
	
	
	public Map<String, String> newJavaImplementation_BA67(FinalizationsBA_DTO finalizationsBA, String dataLibrary, String schema, String companyId, String agencyId,
			Connection differentialLisconnection){
		log.info("Inside newJavaImplementation_BA67 method of BusinessCasesImpl");
		
		Map<String, String> programOutput = new HashMap<String, String>();
		String warehouseNumber = "";
		String kb = "";
		String etnr ="";
		String es1 = "";
		String es2 = "";
		
		String activityType = "";
		String old_BookingAmt = "";
		String storageIndicator = "";
		String sortingPartNo = "";
		String purchasePrice = "";
		String netPrice = "";
		String discountGroup = "";
		String partBrand = "";
		String partIndikator = "";
		String deliverIndikator = "";
		String printingFormatPart = "";		
		String sign = "+";
		
		try(Connection con = dbServiceRepository.getConnectionObject();) {
			
//			con.setAutoCommit(false);
			
			String manufacturer = finalizationsBA.getManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING : finalizationsBA.getManufacturer();
			
			if(finalizationsBA.getLocation()!=null && finalizationsBA.getLocation().trim().length() == 6) {
				warehouseNumber = finalizationsBA.getLocation().substring(4,6);
			}
			
			if(finalizationsBA.getPartNumber() != null) {
				String partNo = StringUtils.rightPad(finalizationsBA.getPartNumber(), 19, " ");
				
				kb = partNo.substring(0,1);
				etnr = partNo.substring(1,13);
				es1 = partNo.substring(13,15);
				es2 = partNo.substring(15,19);
			}
			
			StringBuilder queryforOldETSTAMMData = new StringBuilder("Select LEIART, AKTBES, SA, TNRS, EPR, DAK, RG, TMARKE, TA, LIWERK, TNRD from ");
			queryforOldETSTAMMData.append(dataLibrary).append(".E_ETSTAMM  where HERST='").append(manufacturer);
			queryforOldETSTAMMData.append("' and LNR = ").append(warehouseNumber).append(" and TNR ='").append(finalizationsBA.getPartNumber() +"' ");
			
			List<PartDetails> partDetailList = dbServiceRepository.getResultUsingQuery(PartDetails.class, queryforOldETSTAMMData.toString(), true);
			
			if(partDetailList!=null && !partDetailList.isEmpty()) {
				
				for(PartDetails part_obj : partDetailList) {
					
					activityType = String.valueOf(part_obj.getActivityType()); //LEIART
					old_BookingAmt = String.valueOf(part_obj.getCurrentStock()); //AKTBES
					storageIndicator = String.valueOf(part_obj.getStorageIndikator()); //SA
					sortingPartNo = part_obj.getSortingFormatPartNumber(); //TNRS
					purchasePrice = String.valueOf(part_obj.getPurchasePrice()); //EPR
					netPrice = String.valueOf(part_obj.getAverageNetPrice()); //DAK
					discountGroup = part_obj.getDiscountGroup(); //RG
					partBrand = part_obj.getOemBrand(); //TMARKE
					partIndikator = String.valueOf(part_obj.getPartsIndikator()); //TA
					deliverIndikator = String.valueOf(part_obj.getDeliverIndicator()); //LIWERK
					printingFormatPart = part_obj.getPrintingFormatPartNumber(); //TNRD
				}
			}
			
			StringBuilder update_ETSTAMM = new StringBuilder("Update ").append(dataLibrary).append(".E_ETSTAMM ");
			update_ETSTAMM.append(" set AKTBES = ").append(finalizationsBA.getBookingAmount());
			update_ETSTAMM.append(" , IVKZ = 'I'").append(" where HERST = '").append(manufacturer);
			update_ETSTAMM.append("' and LNR = ").append(warehouseNumber).append(" and TNR ='").append(finalizationsBA.getPartNumber() + "' ");
			
			boolean isUpdated = dbServiceRepository.updateResultUsingQuery(update_ETSTAMM.toString(), con);
			
			if(isUpdated) {
				
				String bookingAmt = StringUtils.leftPad(convertDecimalValue(finalizationsBA.getBookingAmount(), 5, 1), 6, "0");
				
				if(old_BookingAmt.contains("-")){
					sign = "-";
					old_BookingAmt = StringUtils.remove(old_BookingAmt, '-');
				}				
				old_BookingAmt = StringUtils.leftPad(convertDecimalValue(old_BookingAmt, 5, 1), 6, "0");
				
				String satzart = getSatzart( dataLibrary, manufacturer, partBrand, discountGroup );
				String calculatedNetPrice = calculateNetPrice( satzart, purchasePrice );
				String accountNumber = getAccountNumber( schema, warehouseNumber, partBrand, partIndikator );
				
				
				StringBuilder query = new StringBuilder(" INSERT INTO ").append(dataLibrary).append(".E_CPSDAT ");
				query.append(" (HERST, BART, VFNR, BELNR, KB, ETNR, ES1, ES2, T1, T2, EBEST, EB_VZ, WS, T3, NW1_EW, NW2_EW, T4, HERK, JJMMTT, HHMMSS, ETNR_S, EPREW, ");
				query.append(" EPREWK, DAKEWV, DAKEWN, ERNPR, ERNPRK, RABGRV, RABGRN, MARKEV, MARKEN, PROZ, TA_EWV, TA_EWN, LW_EWV, LW_EWN, TNRD, FIRMA, FILIAL, ");
				query.append(" WSUSER, BUK_KONTOV, BUK_KONTON, LEIARTV, LEIARTN )  values ( ");
				query.append("'"+manufacturer+"', '67', ");
				query.append("'"+warehouseNumber+"', ");
				query.append("'"+ finalizationsBA.getInventoryDescription() +"', ");
				query.append("'"+kb+"', "+"'"+etnr+"', "+"'"+es1+"', "+"'"+es2+"', ");
				query.append("'  "+bookingAmt+"', ");
				query.append("'"+StringUtils.leftPad(activityType, 3, "0")+" =', ");
				query.append("'"+old_BookingAmt +"', '"+ sign +"', ");
				query.append(" ' ', ' ', ");  // WS and T3
				query.append("'"+storageIndicator + "', '"+ storageIndicator +"', ");
				query.append("' ', ");  //T4
				if(finalizationsBA.getHerkType()!=null && finalizationsBA.getHerkType().equalsIgnoreCase("CPS401")) {
					query.append("'CPS401', ");
				}else {
					query.append("'BSN5BA', ");
				}

				query.append("VARCHAR_FORMAT(CURRENT TIMESTAMP, 'YYMMDD'),");
				query.append("LPAD (VARCHAR_FORMAT(CURRENT TIMESTAMP, 'HH24MISS'),6,0),");
				query.append("'"+sortingPartNo+ "', ");
				query.append("'"+StringUtils.leftPad(convertDecimalValue(purchasePrice, 5, 2), 7, "0") + "', 'N', ");
				query.append("'"+StringUtils.leftPad(convertDecimalValue(netPrice, 5, 4), 9, "0")+ "', '"+ StringUtils.leftPad(convertDecimalValue(netPrice, 5, 4), 9, "0") +"', ");
				query.append("'"+StringUtils.leftPad(convertDecimalValue(calculatedNetPrice, 5, 2), 7, "0") + "', 'N', ");//---------------------------------//
				query.append("'"+discountGroup+"', '"+discountGroup+"', ");
				query.append("'"+partBrand+"', '"+partBrand+"', ");
				query.append("'"+StringUtils.leftPad(convertDecimalValue(satzart, 2, 2), 5, "0")+"', ");//---------------------------------//
				query.append("'"+StringUtils.leftPad(partIndikator, 2, "0") +"', '"+ StringUtils.leftPad(partIndikator, 2, "0") +"', ");
				query.append("'"+StringUtils.leftPad(deliverIndikator, 2, "0") +"', '"+ StringUtils.leftPad(deliverIndikator, 2, "0") +"', ");
				query.append("'"+printingFormatPart+"', ");
				query.append("'"+StringUtils.leftPad(companyId, 2, "0")+"', '"+StringUtils.leftPad(agencyId, 2, "0")+"', "); //-------------------------//
				query.append("'"+finalizationsBA.getUserName()+"' ,");
				query.append(accountNumber+", "+accountNumber+", "); //----------------------
				query.append(activityType+", "+activityType+" ) ");
				
				dbServiceRepository.insertResultUsingQuery(query.toString(), con);
				programOutput.put("00000"," BA67 success ");
			}
			
			con.commit();
			
//			con.setAutoCommit(true);
		}
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY,"Finalizations BA"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY,"Finalizations BA"), exception);
			throw exception;
		}

		return programOutput;
	}
	
	
	private String getSatzart( String dataLibrary, String manufacturer, String partBrand, String discountGroup ) throws Exception {
		
		String satzart = "0.00";
		
		StringBuilder queryforSatzart = new StringBuilder("Select SATZ from ");
		queryforSatzart.append(dataLibrary).append(".E_RAB  where HERST='").append(manufacturer);
		queryforSatzart.append("' and MARKE ='").append(partBrand).append("' and GRUPPE ='").append(discountGroup).append("' and BUVORG = 3 order by GDAT desc");
		
		List<PartDetails> e_RABList = dbServiceRepository.getResultUsingQuery(PartDetails.class, queryforSatzart.toString(), true);
		
		if(e_RABList !=null && !e_RABList.isEmpty()) {
			satzart = String.valueOf(e_RABList.get(0).getDiscountGroupPercentageValue());
		}
		
		return satzart;
		
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private String calculateNetPrice( String satzart, String purchasePrice ) throws Exception {
		
		double purchasePriceD = 00.0000d;
		
		double satzart_100divided = (satzart!=null && !satzart.trim().isEmpty()) ? Double.parseDouble(satzart)/100 : Double.parseDouble("00.00")/100;
		
		if(purchasePrice!=null && !purchasePrice.trim().isEmpty()) {
			purchasePriceD = Double.parseDouble(purchasePrice);
		}
		
		double cal_Purchase = purchasePriceD * satzart_100divided;
		
		double cal_netPrice = purchasePriceD - cal_Purchase;
		
		//String calculatedNetPrice = StringUtils.leftPad(convertDecimalValue(String.valueOf(cal_netPrice), 5, 2), 7, "0");
		
		String calculatedNetPrice = decimalformat_fixtwodigit.format(cal_netPrice);
		
		return calculatedNetPrice;
	}
	
	
	private String getAccountNumber( String schema, String warehouseNumber, String partBrand, String partIndikator )throws Exception {

		String accountNumber = "0";

		StringBuilder queryforAccountNo = new StringBuilder("select BUK_KONTO AS KTO_NEW from ");
		queryforAccountNo.append(schema).append(".PEI_BUKONT where  BUK_LNR ='").append(warehouseNumber);
		queryforAccountNo.append("' and BUK_TMARKE ='").append(partBrand).append("' and BUK_TEIART =").append(partIndikator).append(" and BUK_DTGULV = ");
		queryforAccountNo.append("(Select max(BUK_DTGULV) from ").append(schema).append(".PEI_BUKONT where  BUK_LNR ='").append(warehouseNumber);
		queryforAccountNo.append("' and BUK_TMARKE ='").append(partBrand).append("' and BUK_TEIART =").append(partIndikator).append(" and BUK_DTGULV <= current timestamp) ");
		queryforAccountNo.append("order by BUK_LNR, BUK_KONTO, BUK_TMARKE, BUK_TEIART");

		List<MovementData> movmentDataList = dbServiceRepository.getResultUsingQuery(MovementData.class, queryforAccountNo.toString(), true);

		if(movmentDataList != null && !movmentDataList.isEmpty()) {
			accountNumber = String.valueOf(movmentDataList.get(0).getKto_New());
		}
		else {

			StringBuilder newQueryforAccountNo = new StringBuilder("select BUK_KONTO AS KTO_NEW from ");
			newQueryforAccountNo.append(schema).append(".PEI_BUKONT where  BUK_LNR ='**' and  BUK_TMARKE ='");
			newQueryforAccountNo.append(partBrand).append("' and BUK_TEIART =").append(partIndikator).append(" and BUK_DTGULV = ");
			newQueryforAccountNo.append("(Select max(BUK_DTGULV) from ").append(schema).append(".PEI_BUKONT where  BUK_LNR ='**' ");
			newQueryforAccountNo.append(" and BUK_TMARKE ='").append(partBrand).append("' and BUK_TEIART =").append(partIndikator).append(" and BUK_DTGULV <= current timestamp) ");
			newQueryforAccountNo.append("order by BUK_LNR, BUK_KONTO, BUK_TMARKE, BUK_TEIART");

			List<MovementData> newMovmentDataList = dbServiceRepository.getResultUsingQuery(MovementData.class, newQueryforAccountNo.toString(), true);

			if(newMovmentDataList != null && !newMovmentDataList.isEmpty()) {
				accountNumber = String.valueOf(newMovmentDataList.get(0).getKto_New());
			}
		}

		return accountNumber;
	}	
	
	/**
	 * This method is used for BA49 
	 */
	@Override
	public Map<String, String> newJavaImplementation_BA49(BusinessCases49DTO obj_ba49, String dataLibrary, 
			String schema, String companyId, String agencyId, String loginUserName){
		log.info("Inside newJavaImplementation_BA49 method of BusinessCasesImpl");
		
		Map<String, String> programOutput = new HashMap<String, String>();
		programOutput.put("00000"," BA49 Failed ");
		String kb = "";
		String etnr ="";
		String es1 = "";
		String es2 = "";
		
		String activityType = "";
		String old_BookingAmt = "";
		String old_storageIndicator = "";
		String sortingPartNo = "";
		String purchasePrice = "";
		String old_netPrice = "";
		String discountGroup = "";
		String partBrand = "";
		String partIndikator = "";
		String deliverIndikator = "";
		String printingFormatPart = "";		
		String sign = "+";
		String new_netPrice = "0.0000";
	    String marketingCode = "";
	    String previousPurchasePrice = "";
	    //accessBA_Obj.setSelfCalculatedNetPrice(decimalformat_twodigit.format(finalizationsPartBA.getPreviousPurchasePrice()!=null ? finalizationsPartBA.getPreviousPurchasePrice() : "0.00"));
		String storageLocation = "";
		String pendingOrders = "";
		
		try(Connection con = dbServiceRepository.getConnectionObject();
				Statement stmt = con.createStatement();){
			
			String manufacturer = obj_ba49.getManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING : obj_ba49.getManufacturer();
			
			if(obj_ba49.getPartNumber() != null) {
				String partNo = StringUtils.rightPad(obj_ba49.getPartNumber(), 19, " ");
				
				kb = partNo.substring(0,1);
				etnr = partNo.substring(1,13);
				es1 = partNo.substring(13,15);
				es2 = partNo.substring(15,19);
			}
			
			StringBuilder queryforOldETSTAMMData = new StringBuilder("Select LEIART, AKTBES, SA, TNRS, EPR, DAK, RG, TMARKE, TA, LIWERK, TNRD,MC, NPREIS, LOPA, BESAUS from ");
			queryforOldETSTAMMData.append(dataLibrary).append(".E_ETSTAMM  where HERST='").append(manufacturer);
			queryforOldETSTAMMData.append("' and LNR = ").append(obj_ba49.getWarehouseNo()).append(" and TNR ='").append(obj_ba49.getPartNumber() +"' ");
			
			List<PartDetails> partDetailList = dbServiceRepository.getResultUsingQuery(PartDetails.class, queryforOldETSTAMMData.toString(), true);
			
			if(partDetailList!=null && !partDetailList.isEmpty()) {
				
				for(PartDetails part_obj : partDetailList) {
					
					activityType = String.valueOf(part_obj.getActivityType()); //LEIART
					old_BookingAmt = String.valueOf(part_obj.getCurrentStock()); //AKTBES
					old_storageIndicator = String.valueOf(part_obj.getStorageIndikator()); //SA
					sortingPartNo = part_obj.getSortingFormatPartNumber(); //TNRS
					purchasePrice = String.valueOf(part_obj.getPurchasePrice()); //EPR
					old_netPrice = String.valueOf(part_obj.getAverageNetPrice()); //DAK
					discountGroup = part_obj.getDiscountGroup(); //RG
					partBrand = part_obj.getOemBrand(); //TMARKE
					partIndikator = String.valueOf(part_obj.getPartsIndikator()); //TA
					deliverIndikator = String.valueOf(part_obj.getDeliverIndicator()); //LIWERK
					printingFormatPart = part_obj.getPrintingFormatPartNumber(); //TNRD
					marketingCode = part_obj.getMarketingCode(); //MC
					previousPurchasePrice = String.valueOf(part_obj.getPreviousPurchasePrice()); //NPREIS
					storageLocation = String.valueOf(part_obj.getStorageLocation()); //LOPA
					pendingOrders = String.valueOf(part_obj.getPendingOrders());//BESAUS
				}
			}
			
			StringBuilder update_ETSTAMM = new StringBuilder("");
			
			if(!StringUtils.isBlank(obj_ba49.getAverageNetPrice())){
				new_netPrice = obj_ba49.getAverageNetPrice();
			}
			
			if(obj_ba49.getStorageIndikator().equalsIgnoreCase("1")){
			
				update_ETSTAMM.append("Update ").append(dataLibrary).append(".E_ETSTAMM ");
				update_ETSTAMM.append(" set SA = 1,  DAK =");   //IVKZ = '', IVER = '', IVZME=0, VORHS=0 , DTINV =0 ,
				update_ETSTAMM.append(new_netPrice);
				update_ETSTAMM.append(" where HERST = '").append(manufacturer);
				update_ETSTAMM.append("' and LNR = ").append(obj_ba49.getWarehouseNo());
				update_ETSTAMM.append(" and TNR ='").append(obj_ba49.getPartNumber() + "' ");
			
			}else if(obj_ba49.getStorageIndikator().equalsIgnoreCase("2")){
				
				if (!old_storageIndicator.equalsIgnoreCase(obj_ba49.getStorageIndikator())) {
					if (Double.parseDouble(old_BookingAmt) > 0 || Double.parseDouble(pendingOrders) > 0) {

						AlphaXException exception = new AlphaXException(
								messageService.createApiMessage(LocaleContextHolder.getLocale(),
										ExceptionMessages.BUSINESS_CASE_49_CONVERSION_FAILED_MSG_KEY));
						log.error(messageService.getReadableMessage(ExceptionMessages.BUSINESS_CASE_49_CONVERSION_FAILED_MSG_KEY), exception);
						throw exception;

					}
				}
				update_ETSTAMM.append("Update ").append(dataLibrary).append(".E_ETSTAMM ");
				update_ETSTAMM.append(" set SA = 2,  ");  //IVKZ = '', IVER = '', IVZME=0, VORHS=0 , DTINV =0, LOPA='', ZAEGRU='',
				update_ETSTAMM.append(" DAK =").append(new_netPrice);
				update_ETSTAMM.append(" where HERST = '").append(manufacturer);
				update_ETSTAMM.append("' and LNR = ").append(obj_ba49.getWarehouseNo());
				update_ETSTAMM.append(" and TNR ='").append(obj_ba49.getPartNumber() + "' ");
			}else{
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.INVALID_MSG_KEY, " Wandlung Satzart (SA). Should be 1 or 2 "));
				log.error(messageService.getReadableMessage(ExceptionMessages.INVALID_MSG_KEY, " Wandlung Satzart (SA). Should be 1 or 2"), exception);
				throw exception;
			}
			
			boolean isUpdated = dbServiceRepository.updateResultUsingQuery(update_ETSTAMM.toString(), con);
			
			if(isUpdated) {
				
				//String bookingAmt = StringUtils.leftPad(convertDecimalValue(obj_ba49.getBookingAmount(), 5, 1), 6, "0");
				
				/*if(old_BookingAmt.contains("-")){
					sign = "-";
					old_BookingAmt = StringUtils.remove(old_BookingAmt, '-');
				}				
				old_BookingAmt = StringUtils.leftPad(convertDecimalValue(old_BookingAmt, 5, 1), 6, "0");*/
				
				String satzart = getSatzart( dataLibrary, manufacturer, partBrand, discountGroup );
				String calculatedNetPrice = "0.00";
				boolean isNumeric = discountGroup != null && !discountGroup.trim().isEmpty() && discountGroup.trim().chars().allMatch(Character::isDigit);
				if(isNumeric && Integer.parseInt(discountGroup.trim()) > 0 ){
					calculatedNetPrice = calculateNetPrice( satzart, purchasePrice );
				}
				
				//String accountNumber = getAccountNumber( schema, obj_ba49.getWarehouseNo(), partBrand, partIndikator );
				
				if(!old_storageIndicator.equalsIgnoreCase(obj_ba49.getStorageIndikator())){
					
				StringBuilder query = new StringBuilder(" INSERT INTO ").append(dataLibrary).append(".E_CPSDAT ");
				query.append(" (HERST,BART,VFNR,BELNR,POSNR,KB ,ETNR ,ES1 ,ES2 ,T1 ,T2 ,EBEST ,EB_VZ ,WS ,T3 ,NW1_EW ,NW2_EW ,T4 ,HERK ,LFDNR ,JJMMTT ,HHMMSS ,ETNR_S, ");
				query.append(" EPREW ,EPREWK ,DAKEWV ,DAKEWN ,ERNPR ,ERNPRK ,RABGRV ,RABGRN ,MARKEV ,MARKEN ,FEPR ,GARANT ,PROZ ,TA_EWV ,TA_EWN ,LW_EWV ,LW_EWN , ");
				query.append("NT_EWV ,NT_EWN ,XMLDAT ,XMLTIM ,BVDAUF ,BVDAUP ,BSNOME ,BSN_LC ,SERVAN ,SERVAL ,SRG_V ,ETM101 ,MCODE ,EKNPR ,TNRD ,AG ,FIRMA ,FILIAL , ");
				query.append(" KDNR ,FZGKZ ,FZNR ,FAART ,MITARB ,NPRKAL ,WSUSER )  values ( ");
				query.append("'"+manufacturer+"', '49', "); // HERST,BART
				query.append("'"+obj_ba49.getWarehouseNo()+"', "); // VFNR
				query.append("' ' , ' ' ,"); //BELNR,POSNR
				query.append("'"+kb+"', "+"'"+etnr+"', "+"'"+es1+"', "+"'"+es2+"', ");  //KB ,ETNR ,ES1 ,ES2
				query.append("'  005"+obj_ba49.getStorageIndikator()+"', "); // T1 update SA
				query.append("'"+StringUtils.leftPad(activityType, 3, "0")+"', "); //  T2 
				query.append("'000000', '"+ sign +"', "); //EBEST ,  EB_VZ  
				query.append(" ' ', '000000"+storageLocation+"', ");  // WS and T3  
				query.append("'"+old_storageIndicator+"', '"+obj_ba49.getStorageIndikator()+"', "); //NW1_EW ,NW2_EW
				
				if(obj_ba49.getStorageIndikator().equalsIgnoreCase("1")){ //if new SA 1
					query.append("'"+StringUtils.rightPad("0000", 18, " ")+"00', ");  //T4 
				}else if(obj_ba49.getStorageIndikator().equalsIgnoreCase("2")){ //if new SA 2
					query.append("'"+StringUtils.rightPad("0000", 10, " ")+StringUtils.rightPad(storageLocation, 8, " ")+"00', ");  //T4 
				}
				
				query.append("'BSN5BA', "); // HERK  
				query.append("'0', "); // LFDNR 
				query.append("VARCHAR_FORMAT(CURRENT TIMESTAMP, 'YYMMDD'),"); // JJMMTT  
				query.append("LPAD (VARCHAR_FORMAT(CURRENT TIMESTAMP, 'HH24MISS'),6,0),"); // HHMMSS  
				query.append("'"+sortingPartNo+ "', "); // ETNR_S
				query.append("'"+StringUtils.leftPad(convertDecimalValue(purchasePrice, 5, 2), 7, "0") + "', 'N', "); //  EPREW ,EPREWK
				query.append("'"+StringUtils.leftPad(convertDecimalValue(old_netPrice, 5, 4), 9, "0")+ "', "); //DAKEWV
				
				if(old_netPrice.equalsIgnoreCase(new_netPrice)){ //IF DAK change then new value else E_ETSTAMM.DAK
					query.append("'"+ StringUtils.leftPad(convertDecimalValue(old_netPrice, 5, 4), 9, "0") +"', "); //DAKEWN
				}else{
					query.append("'"+ StringUtils.leftPad(convertDecimalValue(new_netPrice, 5, 4), 9, "0") +"', "); //DAKEWN
				}
				query.append("'"+calculatedNetPrice + "', 'N', ");//ERNPR ,ERNPRK  need to change
				query.append("'"+discountGroup + "', '"+discountGroup + "', ");//RABGRV ,RABGRN  
				query.append("'"+partBrand + "', '"+partBrand + "', ");//MARKEV ,MARKEN  
				query.append("' ', ' ', "); //FEPR ,GARANT
				
				query.append("'"+StringUtils.leftPad(convertDecimalValue(satzart, 2, 2), 5, "0")+"', ");//PROZ
				query.append("'"+StringUtils.leftPad(partIndikator, 2, "0") +"', '"+ StringUtils.leftPad(partIndikator, 2, "0") +"', "); //TA_EWV ,TA_EWN 
				query.append("'"+StringUtils.leftPad(deliverIndikator, 2, "0") +"', '"+ StringUtils.leftPad(deliverIndikator, 2, "0") +"', "); //LW_EWV ,LW_EWN
				query.append("'0000000', '0000000', "); //NT_EWV ,NT_EWN 
				query.append("'000000', '000000', "); //XMLDAT ,XMLTIM 
				query.append("'00000', '00000', "); //BVDAUF ,BVDAUP
				query.append("'0000000', '00000', "); //BSNOME ,BSN_LC
				query.append("'000000', ' ', "); //SERVAN ,SERVAL
				query.append("' ', ' ', "); //SRG_V ,ETM101
				
				if(marketingCode.trim().equalsIgnoreCase("") && manufacturer.equalsIgnoreCase("DCAG")){
					query.append("'D00000' ,"); //MCODE
				}else if(marketingCode.trim().equalsIgnoreCase("") && !manufacturer.equalsIgnoreCase("DCAG")){
					query.append("'X00000' ,"); //MCODE
				}else{
					query.append("'"+marketingCode+"' ,");
				}
				query.append("'0',"); //EKNPR
				query.append("'"+printingFormatPart+"', "); //TNRD
				query.append("' ',"); //AG
				query.append("'"+StringUtils.leftPad(companyId, 2, "0")+"', '"+StringUtils.leftPad(agencyId, 2, "0")+"', "); //FIRMA ,FILIAL
				query.append("' ', ' ', '0' ,' ', ' ', "); // KDNR ,FZGKZ ,FZNR ,FAART ,MITARB
				query.append("'"+previousPurchasePrice+"',"); // NPRKAL
				query.append("'"+loginUserName+"' )"); // WSUSER
				
				
				stmt.addBatch(query.toString());
				log.info("Query  :"+query.toString());
				
			}
				if(!old_netPrice.equalsIgnoreCase(new_netPrice)){
					
					StringBuilder query = new StringBuilder(" INSERT INTO ").append(dataLibrary).append(".E_CPSDAT ");
					query.append(" (HERST,BART,VFNR,BELNR,POSNR,KB ,ETNR ,ES1 ,ES2 ,T1 ,T2 ,EBEST ,EB_VZ ,WS ,T3 ,NW1_EW ,NW2_EW ,T4 ,HERK ,LFDNR ,JJMMTT ,HHMMSS ,ETNR_S, ");
					query.append(" EPREW ,EPREWK ,DAKEWV ,DAKEWN ,ERNPR ,ERNPRK ,RABGRV ,RABGRN ,MARKEV ,MARKEN ,FEPR ,GARANT ,PROZ ,TA_EWV ,TA_EWN ,LW_EWV ,LW_EWN , ");
					query.append("NT_EWV ,NT_EWN ,XMLDAT ,XMLTIM ,BVDAUF ,BVDAUP ,BSNOME ,BSN_LC ,SERVAN ,SERVAL ,SRG_V ,ETM101 ,MCODE ,EKNPR ,TNRD ,AG ,FIRMA ,FILIAL , ");
					query.append(" KDNR ,FZGKZ ,FZNR ,FAART ,MITARB ,NPRKAL ,WSUSER )  values ( ");
					query.append("'"+manufacturer+"', '49', "); // HERST,BART
					query.append("'"+obj_ba49.getWarehouseNo()+"', "); // VFNR
					query.append("' ' , ' ' ,"); //BELNR,POSNR
					query.append("'"+kb+"', "+"'"+etnr+"', "+"'"+es1+"', "+"'"+es2+"', ");  //KB ,ETNR ,ES1 ,ES2
					query.append("'  023"+convertDecimalValue(new_netPrice,5,4)+"', "); // T1 update DAK
					query.append("'"+StringUtils.leftPad(activityType, 3, "0")+"', "); //  T2 need to change
					query.append("'000000', '"+ sign +"', "); //EBEST ,  EB_VZ  
					query.append(" ' ', '000000', ");  // WS and T3 
					query.append("' ', ' ', "); //NW1_EW ,NW2_EW
					query.append("'"+StringUtils.rightPad("0000", 18, " ")+"00', ");  //T4 
					query.append("'BSN5BA', "); // HERK  need to change
					query.append("'0', "); // LFDNR  need to change
					query.append("VARCHAR_FORMAT(CURRENT TIMESTAMP, 'YYMMDD'),"); // JJMMTT  
					query.append("LPAD (VARCHAR_FORMAT(CURRENT TIMESTAMP, 'HH24MISS'),6,0),"); // HHMMSS  
					query.append("'"+sortingPartNo+ "', "); // ETNR_S
					query.append("'"+StringUtils.leftPad(convertDecimalValue(purchasePrice, 5, 2), 7, "0") + "', 'N', "); //  EPREW ,EPREWK
					query.append("'"+StringUtils.leftPad(convertDecimalValue(old_netPrice, 5, 4), 9, "0")+ "', "); //DAKEWV
					
					if(old_netPrice.equalsIgnoreCase(new_netPrice)){ //IF DAK change then new value else E_ETSTAMM.DAK
						query.append("'"+ StringUtils.leftPad(convertDecimalValue(old_netPrice, 5, 4), 9, "0") +"', "); //DAKEWN
					}else{
						query.append("'"+ StringUtils.leftPad(convertDecimalValue(new_netPrice, 5, 4), 9, "0") +"', "); //DAKEWN
					}
					query.append("'"+calculatedNetPrice + "', 'N', ");//ERNPR ,ERNPRK  need to change
					query.append("'"+discountGroup + "', '"+discountGroup + "', ");//RABGRV ,RABGRN  
					query.append("'"+partBrand + "', '"+partBrand + "', ");//MARKEV ,MARKEN  
					query.append("' ', ' ', "); //FEPR ,GARANT
					
					query.append("'"+StringUtils.leftPad(convertDecimalValue(satzart, 2, 2), 5, "0")+"', ");//PROZ need to change
					query.append("'"+StringUtils.leftPad(partIndikator, 2, "0") +"', '"+ StringUtils.leftPad(partIndikator, 2, "0") +"', "); //TA_EWV ,TA_EWN 
					query.append("'"+StringUtils.leftPad(deliverIndikator, 2, "0") +"', '"+ StringUtils.leftPad(deliverIndikator, 2, "0") +"', "); //LW_EWV ,LW_EWN
					query.append("'0000000', '0000000', "); //NT_EWV ,NT_EWN 
					query.append("'000000', '000000', "); //XMLDAT ,XMLTIM 
					query.append("'00000', '00000', "); //BVDAUF ,BVDAUP
					query.append("'0000000', '00000', "); //BSNOME ,BSN_LC
					query.append("'000000', ' ', "); //SERVAN ,SERVAL
					query.append("' ', ' ', "); //SRG_V ,ETM101
					
					if(marketingCode.trim().equalsIgnoreCase("") && manufacturer.equalsIgnoreCase("DCAG")){
						query.append("'D00000' ,"); //MCODE
					}else if(marketingCode.trim().equalsIgnoreCase("") && !manufacturer.equalsIgnoreCase("DCAG")){
						query.append("'X00000' ,"); //MCODE
					}else{
						query.append("'"+marketingCode+"' ,");
					}
					
					query.append("'0',"); //EKNPR
					query.append("'"+printingFormatPart+"', "); //TNRD
					query.append("' ',"); //AG
					query.append("'"+StringUtils.leftPad(companyId, 2, "0")+"', '"+StringUtils.leftPad(agencyId, 2, "0")+"', "); //FIRMA ,FILIAL
					query.append("' ', ' ', '0' ,' ', ' ', "); // KDNR ,FZGKZ ,FZNR ,FAART ,MITARB
					query.append("'"+previousPurchasePrice+"',"); // NPRKAL
					query.append("'"+loginUserName+"' )"); // WSUSER
					
					stmt.addBatch(query.toString());
					log.info("Query  :"+query.toString());
				}
				int[] records = dbServiceRepository.insertResultUsingBatchQuery(stmt);
				if(records != null){
					log.info("In E_CPSDAT - Total rows Inserted  :"+records.length);
				}
				programOutput.put("00000"," BA49 success ");
			}
			
			
			con.commit();
			
		}
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY,"BA49"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY,"BA49"), exception);
			throw exception;
		}

		return programOutput;
	}
	
	
	/**
	 * This method is used for BA25
	 * 
	 * The BA25 reduce the stock as technical correction of incorrect bookings
	 */
	@Override
	public Map<String, Boolean> newJavaImplementation_BA25(BusinessCases25DTO  ba25_obj, String dataLibrary,String schema,
			String companyId, String agencyId, String loginUserName ,Map<String, String> output_bsn475){
		log.info("Inside newJavaImplementation_BA25 method of BusinessCasesImpl");

		Map<String, Boolean> programOutput = new HashMap<String, Boolean>();
		programOutput.put("isUpdated", false);

		String oemBrand = ""; // MARKE
		String bodyWork = ""; //Leiferwerk
		String partsIndikator = ""; //Teilart
		String activityType = ""; //Leistungsart

		try(Connection con = dbServiceRepository.getConnectionObject();
				Statement stmt = con.createStatement();){

			//con.setAutoCommit(false);
			String manufacturer = ba25_obj.getManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING : ba25_obj.getManufacturer();

			if(ba25_obj.getNetShoppingPrice() == null || ba25_obj.getNetShoppingPrice().trim().isEmpty()) {
				ba25_obj.setNetShoppingPrice("0.00");
			}

			if(ba25_obj.getBookingAmount() == null || ba25_obj.getBookingAmount().trim().isEmpty()) {
				ba25_obj.setBookingAmount("0.00");
			}

			//delivery note Check
			StringBuilder query = new StringBuilder("SELECT COUNT(*) as count FROM ");
			query.append(dataLibrary).append(".E_BSNLFS WHERE ");
			query.append(" BA='' ");
			query.append(" AND HERST = ").append("'"+manufacturer+"'");
			query.append(" AND LNR = ").append(ba25_obj.getWarehouseNumber());
			query.append(" AND BENDL = ").append(ba25_obj.getSupplierNumber());
			query.append(" AND UPPER(NRX) = UPPER('").append(ba25_obj.getDeliveryNoteNo()).append("') ");

			String count = dbServiceRepository.getCountUsingQuery(query.toString());

			if (Integer.parseInt(count) > 0) {

				if (ba25_obj.getInvoiceAudit() != null && ba25_obj.getInvoiceAudit().trim().equalsIgnoreCase("J")) {
					log.info("Delivery note done. Please choose a different delivery note");
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.BUSINESS_CASES_25_FAILED_MSG_KEY_1));
					log.error(messageService.getReadableMessage(ExceptionMessages.BUSINESS_CASES_25_FAILED_MSG_KEY_1));
					throw exception;
				} else if (ba25_obj.getDeliveryNoteAudit() != null && ba25_obj.getDeliveryNoteAudit().trim().equalsIgnoreCase("J")) {
					log.info("Delivery note closed");
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.BUSINESS_CASES_25_FAILED_MSG_KEY_2));
					log.error(messageService.getReadableMessage(ExceptionMessages.BUSINESS_CASES_25_FAILED_MSG_KEY_2));
					throw exception;
				}
			}else{

				if(ba25_obj.getCustomerGroup().equalsIgnoreCase("6")){
					log.info("Delivery note not available. Please select another delivery note");	
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.BUSINESS_CASES_25_FAILED_MSG_KEY_3));
					log.error(messageService.getReadableMessage(ExceptionMessages.BUSINESS_CASES_25_FAILED_MSG_KEY_3));
					throw exception;
				}
				/*
				 * else{ log.
				 * info("Delivery note not available but will create automatically for VC - 7,8 and 9"
				 * ); AlphaXException exception = new
				 * AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale
				 * (), ExceptionMessages.BUSINESS_CASES_25_FAILED_MSG_KEY_4));
				 * log.error(messageService.getReadableMessage(ExceptionMessages.
				 * BUSINESS_CASES_25_FAILED_MSG_KEY_4)); throw exception; }
				 */
			}

			//Price check
			if(!priceCorrectionCheck(dataLibrary,ba25_obj.getListPrice(), ba25_obj.getBookingAmount())){
				log.info("Price implausible!");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.BUSINESS_CASES_25_PRICE_CHECK_FAILED_MSG_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.BUSINESS_CASES_25_PRICE_CHECK_FAILED_MSG_KEY));
				throw exception;
			}

			String satzart = getSatzart( dataLibrary, manufacturer, ba25_obj.getOemBrand(), ba25_obj.getDiscountGroup() );

			String calculatedNetPrice = "0.00";
			String  netShoppingPrice = ba25_obj.getNetShoppingPrice();
			if(Double.parseDouble(netShoppingPrice) > 0) {
				calculatedNetPrice = netShoppingPrice;
			}else if(Double.parseDouble(netShoppingPrice) == 0 &&  ba25_obj.getCustomerGroup().equalsIgnoreCase("9")){
				if(ba25_obj.getAverageNetPrice()!=null && !ba25_obj.getAverageNetPrice().isEmpty() && ba25_obj.getAverageNetPrice().length() > 4 ) {
					calculatedNetPrice =  ba25_obj.getAverageNetPrice().substring(0, ba25_obj.getAverageNetPrice().length() - 2);  
				}
			}else if(Double.parseDouble(netShoppingPrice) == 0 &&  !ba25_obj.getCustomerGroup().equalsIgnoreCase("9")){
				calculatedNetPrice = calculateNetPrice( satzart, ba25_obj.getListPrice() );
			}
			
			if(output_bsn475==null) {
			 output_bsn475 = newJavaImplementation_BSN475(ba25_obj.getOemBrand(), dataLibrary, schema, manufacturer,
					ba25_obj.getMarketingCode(),ba25_obj.getDiscountGroup(), ba25_obj.getPartNumber());
			}
			
			oemBrand = output_bsn475.get("Marke");
			partsIndikator = output_bsn475.get("Teilart");
			if(null == oemBrand || oemBrand.trim().isEmpty()) {			
				oemBrand = ba25_obj.getOemBrand();
			}
			//Determine Booking account (BUCHUNGSKONTO)
			String bookingAccount = getBookingAccount(ba25_obj.getWarehouseNumber(), oemBrand, partsIndikator, dataLibrary, schema);
			
			//Special Case
			output_bsn475 =  checkSpecialCasesInBSN475(ba25_obj.getPartNumber(),  output_bsn475);
			oemBrand = output_bsn475.get("Marke"); //Marke
			bodyWork = output_bsn475.get("Leiferwerk"); //Leiferwerk
			partsIndikator = output_bsn475.get("Teilart"); //Teilart
			activityType = output_bsn475.get("Leistungsart"); //Leistungsart

			String cpsdatQuery = insertValuesInCPSDAT(dataLibrary, ba25_obj,calculatedNetPrice, satzart,
					companyId,agencyId,loginUserName,partsIndikator,bookingAccount, activityType );

			stmt.addBatch(cpsdatQuery.toString());
			log.info("CPSDAT Query  :"+cpsdatQuery.toString());

			String elfslQuery = insertValueInElfsL(ba25_obj, dataLibrary, satzart, partsIndikator, calculatedNetPrice);

			stmt.addBatch(elfslQuery.toString());
			log.info("E_LFSL Query  :"+elfslQuery.toString());

			String insertOrUpdateQuery1 = insertOrUpdateInBSNLFS_First(ba25_obj, dataLibrary);

			stmt.addBatch(insertOrUpdateQuery1.toString());
			log.info("E_BSNLFS Query - 1  :"+insertOrUpdateQuery1.toString());

			String insertOrUpdateQuery2 = insertOrUpdateInBSNLFS_Second(ba25_obj, dataLibrary, calculatedNetPrice, "25");

			stmt.addBatch(insertOrUpdateQuery2.toString());
			log.info("E_BSNLFS Query - 2  :"+insertOrUpdateQuery2.toString());

			String currentStock = String.valueOf(Double.parseDouble(ba25_obj.getCurrentStock())-Double.parseDouble(ba25_obj.getBookingAmount()));


			StringBuilder update_ETSTAMM = new StringBuilder("Update ").append(dataLibrary).append(".E_ETSTAMM ");
			update_ETSTAMM.append(" set EKNPR =").append(calculatedNetPrice);
			update_ETSTAMM.append(", DTLBEW = '").append(getCurrentDate()).append("', AKTBES = ").append(currentStock);
			update_ETSTAMM.append(" where HERST = '").append(manufacturer);
			update_ETSTAMM.append("' and LNR = ").append(ba25_obj.getWarehouseNumber());
			update_ETSTAMM.append(" and TNR ='").append(ba25_obj.getPartNumber() + "' ");

			stmt.addBatch(update_ETSTAMM.toString());
			log.info("E_ETSTAMM Update:"+update_ETSTAMM.toString());

			//boolean isUpdated = dbServiceRepository.updateResultUsingQuery(update_ETSTAMM.toString(), con);

			int[] records = dbServiceRepository.insertResultUsingBatchQuery(stmt);
			if(records != null){
				log.info("Total rows Inserted  :"+records.length);
			}

			con.commit();
			programOutput.put("isUpdated", true);
		}
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY,"BA25"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY,"BA25"), exception);
			throw exception;
		}

		return programOutput;
	}


	


	private String insertOrUpdateInBSNLFS_Second(BusinessCases25DTO ba25_obj, String dataLibrary, String calculatedNetPrice, String baValue) {
		log.info("Inside insertOrUpdateInBSNLFS_Second method of BusinessCasesImpl");
		
		String manufacturer = ba25_obj.getManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING : ba25_obj.getManufacturer();
		
		StringBuilder query = new StringBuilder("SELECT BPOS , BWERT, NWERT FROM ");
		query.append(dataLibrary).append(".E_BSNLFS WHERE ");
		query.append(" HERST = ").append("'"+manufacturer+"'");
		query.append(" AND LNR = ").append(ba25_obj.getWarehouseNumber());
		query.append(" AND BENDL = ").append(ba25_obj.getSupplierNumber());
		query.append(" AND NRX = '").append(ba25_obj.getDeliveryNoteNo()).append("' ");
		query.append(" AND ART='BSN5BA' AND LAUFNR ='00' AND REKOW='' ");
		if(baValue.equalsIgnoreCase("06")) {
			query.append(" AND BA='06' ");
		}
		else {
			query.append(" AND BA='25' ");
		}
		
		List<DeliveryNotes_BA> deliveryNoteList = dbServiceRepository.getResultUsingQuery(DeliveryNotes_BA.class, query.toString(), true);

		StringBuilder insertOrUpdate = new StringBuilder("");
		
		int bpos = 0;
		double bwertFromDb = 0.00;
		double nwertFromDB = 0.00;
		double calculatedBwert = 0.00;
		double calculatedNwert = 0.00;
		
		if(baValue.equalsIgnoreCase("06")) {
			
			if(Double.parseDouble(ba25_obj.getInputListPrice()) > 0) {
				calculatedBwert = Double.parseDouble(ba25_obj.getInputListPrice()) * Double.parseDouble(ba25_obj.getBookingAmount());
			}
			else {
				calculatedBwert = Double.parseDouble(ba25_obj.getListPrice()) * Double.parseDouble(ba25_obj.getBookingAmount());
			}
			calculatedNwert = Double.parseDouble(calculatedNetPrice) * Double.parseDouble(ba25_obj.getBookingAmount());
		}
		else {
			//BA 25
			if(Double.parseDouble(ba25_obj.getInputListPrice()) > 0) {
			calculatedBwert = Double.parseDouble(ba25_obj.getInputListPrice()) * Double.parseDouble(ba25_obj.getBookingAmount()) * -1;
			}else {
				calculatedBwert = Double.parseDouble(ba25_obj.getListPrice()) * Double.parseDouble(ba25_obj.getBookingAmount()) * -1;	
			}
			calculatedNwert = Double.parseDouble(calculatedNetPrice) * Double.parseDouble(ba25_obj.getBookingAmount())  * -1;
		}
		
		if (deliveryNoteList != null && !deliveryNoteList.isEmpty()) {
			
			bpos = deliveryNoteList.get(0).getBpos().intValue() + 1;
			StringBuilder bwert_builder = new StringBuilder();
			
			if(deliveryNoteList.get(0).getBwert_sepsign().contains("+")) {
				String bwert = StringUtils.remove(deliveryNoteList.get(0).getBwert_sepsign(),"+");
				bwert = StringUtils.stripStart(bwert, "0");
				if(bwert.length() >= 2) {
					bwertFromDb = Double.parseDouble(bwert_builder.append(bwert).insert(bwert.length()-2, ".").toString());		
				}else {
					bwertFromDb = 0.00;
				}
			}else if(deliveryNoteList.get(0).getBwert_sepsign().contains("-")) {
				String bwert = StringUtils.remove(deliveryNoteList.get(0).getBwert_sepsign(),"-");
				bwert = StringUtils.stripStart(bwert, "0");
				if(bwert.length() >= 2) {
					bwertFromDb = Double.parseDouble(bwert_builder.append(bwert).insert(bwert.length()-2, ".").insert(0, "-").toString());	
				}else {
					bwertFromDb = 0.00;
				}
			}else {
				bwertFromDb = Double.parseDouble(deliveryNoteList.get(0).getBwert_sepsign());	
			}
			
			StringBuilder nwert_builder = new StringBuilder();
			
			if(deliveryNoteList.get(0).getNwert_sepsign().contains("+")) {
				String nwert = StringUtils.remove(deliveryNoteList.get(0).getNwert_sepsign(),"+");
				nwert = StringUtils.stripStart(nwert, "0");
				if(nwert.length() >= 2) {
				nwertFromDB = Double.parseDouble(nwert_builder.append(nwert).insert(nwert.length()-2, ".").toString());	
				}else {
					nwertFromDB = 0.00;
				}

			}else if(deliveryNoteList.get(0).getNwert_sepsign().contains("-")) {
				String nwert = StringUtils.remove(deliveryNoteList.get(0).getNwert_sepsign(),"-");
				nwert = StringUtils.stripStart(nwert, "0");
				if(nwert.length() >= 2) {
				nwertFromDB = Double.parseDouble(nwert_builder.append(nwert).insert(nwert.length()-2, ".").insert(0, "-").toString());	
				}else {
					nwertFromDB = 0.00;
				}
			}else {
				nwertFromDB = Double.parseDouble(deliveryNoteList.get(0).getNwert_sepsign());	
			}
			
			String finalBwert = decimalformat_fixtwodigit.format(bwertFromDb + calculatedBwert);
			String finalNwert = decimalformat_fixtwodigit.format(nwertFromDB + calculatedNwert);
			
			insertOrUpdate.append("UPDATE ").append(dataLibrary).append(".E_BSNLFS SET PROGRAMM = 'BSN5BA', ");
			insertOrUpdate.append(" BPOS = ").append(bpos).append(", BWERT = '");
			
			if(Double.parseDouble(finalBwert) >= 0) {
				insertOrUpdate.append(StringUtils.leftPad(finalBwert.replace(".", ""),9,"0")+"+").append("', NWERT = '");
			}else {
				String amount = StringUtils.remove(finalBwert, ".");
				amount = StringUtils.remove(amount, "-");  //remove "-" from start
				insertOrUpdate.append(StringUtils.leftPad(amount,9,"0")+"-").append("', NWERT= '");	
			}
			
			if(Double.parseDouble(finalNwert) >= 0) {
				if(finalNwert.contains("-")) {
					String amount = StringUtils.remove(finalNwert, ".");
					amount = StringUtils.remove(amount, "-");  //remove "-" from start
					insertOrUpdate.append(StringUtils.leftPad(amount,9,"0")+"-").append("', ");
				}else {
				insertOrUpdate.append(StringUtils.leftPad(finalNwert.replace(".", ""),9,"0")+"+").append("',");
				}
			}else {
				String amount = StringUtils.remove(finalNwert, ".");
				amount = StringUtils.remove(amount, "-");  //remove "-" from start
				insertOrUpdate.append(StringUtils.leftPad(amount,9,"0")+"-").append("', ");	
			}
			insertOrUpdate.append("REC_WS = '' , ");
			insertOrUpdate.append("UPDDAT_JMT =").append(getCurrentDate()).append(" , ");
			insertOrUpdate.append("UPDTIM_STD = ").append(getCurrentTime().substring(0, 2)).append(" , ");
			insertOrUpdate.append("UPDTIM_MIN = ").append(getCurrentTime().substring(2, 4)).append(" , ");
			insertOrUpdate.append("UPDTIM_SEC = ").append(getCurrentTime().substring(4, 6)).append("  ");
			insertOrUpdate.append(" WHERE HERST = ").append("'"+manufacturer+"'");
			insertOrUpdate.append(" AND LNR = ").append(ba25_obj.getWarehouseNumber());
			insertOrUpdate.append(" AND BENDL = ").append(ba25_obj.getSupplierNumber());
			insertOrUpdate.append(" AND NRX = '").append(ba25_obj.getDeliveryNoteNo()).append("' ");
			insertOrUpdate.append(" AND ART='BSN5BA' AND LAUFNR ='00' AND REKOW='' ");
			if(baValue.equalsIgnoreCase("06")) {
				insertOrUpdate.append(" AND BA='06' ");
			}
			else {
				insertOrUpdate.append(" AND BA='25' ");
			}
		}else {
			
			insertOrUpdate.append("INSERT INTO ").append(dataLibrary).append(".E_BSNLFS ( HERST,LNR,BENDL,NRX,ART,LAUFNR,BA,REKOW, REC_WS , PRUEFUNG, UPD_WS, LFSDAT_JMT, RECDAT_JMT, ");
			insertOrUpdate.append(" RECTIM_STD, RECTIM_MIN, RECTIM_SEC,UPDDAT_JMT, UPDTIM_STD ,UPDTIM_MIN, UPDTIM_SEC,  BPOS, BWERT, NWERT, GPOS ,GWERT, DPOS, DWERT, HPOS, HWERT, ");
			insertOrUpdate.append(" RENUM , PROGRAMM, ERLEDIGT ) VALUES ( ");
			insertOrUpdate.append("'"+manufacturer+"' ,"); // HERST
			insertOrUpdate.append(ba25_obj.getWarehouseNumber()).append(","); //LNR
			insertOrUpdate.append(ba25_obj.getSupplierNumber()).append(","); //BENDL
			insertOrUpdate.append("'"+ba25_obj.getDeliveryNoteNo()+"' ,"); //NRX
			insertOrUpdate.append(" 'BSN5BA' , '00', "); //ART,LAUFNR
			
			if(baValue.equalsIgnoreCase("06")) {
				insertOrUpdate.append("'06', ");  //BA
			} 
			else {
				insertOrUpdate.append("'25', ");  //BA
			}
			insertOrUpdate.append(" '' , '', '', '' , ");  // REKOW, REC_WS , PRUEFUNG, UPD_WS
			
			insertOrUpdate.append(getCurrentDate()).append(","); //LFSDAT_JMT
			insertOrUpdate.append(getCurrentDate()).append(","); //RECDAT_JMT
			insertOrUpdate.append(getCurrentTime().substring(0, 2)).append(","); //RECTIM_STD
			insertOrUpdate.append(getCurrentTime().substring(2, 4)).append(","); //RECTIM_MIN
			insertOrUpdate.append(getCurrentTime().substring(4, 6)).append(","); //RECTIM_SEC
			insertOrUpdate.append(getCurrentDate()).append(","); //UPDTIM_JMT
			insertOrUpdate.append(getCurrentTime().substring(0, 2)).append(","); //UPDTIM_STD
			insertOrUpdate.append(getCurrentTime().substring(2, 4)).append(","); //UPDTIM_MIN
			insertOrUpdate.append(getCurrentTime().substring(4, 6)).append(","); //UPDTIM_SEC
			insertOrUpdate.append("1 , '"); //BPOS
			
			String finalBwert = decimalformat_fixtwodigit.format(calculatedBwert);
			String finalNwert = decimalformat_fixtwodigit.format(calculatedNwert);

			if(Double.parseDouble(finalBwert) >= 0) {
				if(finalBwert.contains("-")) {
					String amount = StringUtils.remove(finalBwert, ".");
					amount = StringUtils.remove(amount, "-");  //remove "-" from start
					insertOrUpdate.append(StringUtils.leftPad(amount,9,"0")+"-").append("', '");
				}else {
					insertOrUpdate.append(StringUtils.leftPad(finalBwert.replace(".", ""),9,"0")+"+").append("', '");	
				}
				
			}else {
				String amount = StringUtils.remove(finalBwert, ".");
				amount = StringUtils.remove(amount, "-");  //remove "-" from start
				insertOrUpdate.append(StringUtils.leftPad(amount,9,"0")+"-").append("', '");	
			}
			
			if(Double.parseDouble(finalNwert) >= 0) {
				if(finalNwert.contains("-")) {
					String amount = StringUtils.remove(finalNwert, ".");
					amount = StringUtils.remove(amount, "-");  //remove "-" from start
					insertOrUpdate.append(StringUtils.leftPad(amount,9,"0")+"-").append("', ");
				}else {
				insertOrUpdate.append(StringUtils.leftPad(finalNwert.replace(".", ""),9,"0")+"+").append("',");
				}
			}else {
				String amount = StringUtils.remove(finalNwert, ".");
				amount = StringUtils.remove(amount, "-");  //remove "-" from start
				insertOrUpdate.append(StringUtils.leftPad(amount,9,"0")+"-").append("', ");	
			}
			
			//insertOrUpdate.append(calculatedBwert).append(","); //BWERT
			//insertOrUpdate.append(calculatedNwert).append(","); //NWERT
			insertOrUpdate.append(" 0 ,  '000000000+'  ,"); // GPOS ,GWERT
			insertOrUpdate.append(" 0 ,  '000000000+'  ,"); // DPOS ,DWERT
			insertOrUpdate.append(" 0 ,  '000000000+'  ,"); // HPOS ,HWERT
			insertOrUpdate.append(" '' , 'BSN5BA' , '' )"); //RENUM, PROGRAMM, ERLEDIGT 
		}
		
		return insertOrUpdate.toString();
	}


	private String insertOrUpdateInBSNLFS_First(BusinessCases25DTO ba25_obj, String dataLibrary) {
		log.info("Inside insertOrUpdateInBSNLFS_First method of BusinessCasesImpl");

		String manufacturer = ba25_obj.getManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING : ba25_obj.getManufacturer();

		StringBuilder query = new StringBuilder("SELECT PRUEFUNG, ERLEDIGT FROM ");
		query.append(dataLibrary).append(".E_BSNLFS WHERE ");
		query.append(" HERST = ").append("'"+manufacturer+"'");
		query.append(" AND LNR = ").append(ba25_obj.getWarehouseNumber());
		query.append(" AND BENDL = ").append(ba25_obj.getSupplierNumber());
		query.append(" AND NRX = '").append(ba25_obj.getDeliveryNoteNo()).append("' ");
		query.append(" AND ART='' AND LAUFNR ='01' AND BA='' AND REKOW='' ");

		List<DeliveryNotes_BA> deliveryNoteList = dbServiceRepository.getResultUsingQuery(DeliveryNotes_BA.class, query.toString(), true);

		StringBuilder insertOrUpdate = new StringBuilder("");

		if (deliveryNoteList != null && !deliveryNoteList.isEmpty()) {
			insertOrUpdate.append("UPDATE ");
			insertOrUpdate.append(dataLibrary).append(".E_BSNLFS SET PROGRAMM = 'BSN5BA' ");

			String deliveryNoteAudit = deliveryNoteList.get(0).getDeliveryNoteAudit();
			String invoiceAudit = deliveryNoteList.get(0).getInvoiceAudit();

			if (deliveryNoteAudit != null && !deliveryNoteAudit.trim().isEmpty() 
					&& invoiceAudit != null && !invoiceAudit.equalsIgnoreCase("J")) {
				insertOrUpdate.append(", PRUEFUNG = ' ', RENUM = ' ' ");
			}
			insertOrUpdate.append(" WHERE HERST = ").append("'"+manufacturer+"'");
			insertOrUpdate.append(" AND LNR = ").append(ba25_obj.getWarehouseNumber());
			insertOrUpdate.append(" AND BENDL = ").append(ba25_obj.getSupplierNumber());
			insertOrUpdate.append(" AND NRX = '").append(ba25_obj.getDeliveryNoteNo()).append("' ");
			insertOrUpdate.append(" AND ART='' AND LAUFNR ='01' AND BA='' AND REKOW='' ");
		}else {

			insertOrUpdate.append("INSERT INTO ").append(dataLibrary).append(".E_BSNLFS ( HERST,LNR,BENDL,NRX,ART,LAUFNR,BA,REKOW, LFSDAT_JMT, RECDAT_JMT, ");
			insertOrUpdate.append(" RECTIM_STD, RECTIM_MIN, RECTIM_SEC, REC_WS, PRUEFUNG, GPOS ,GWERT, DPOS, DWERT, HPOS, HWERT, BPOS, BWERT, NWERT, PROGRAMM, ERLEDIGT ) VALUES ( ");
			insertOrUpdate.append("'"+manufacturer+"' ,"); // HERST
			insertOrUpdate.append(ba25_obj.getWarehouseNumber()).append(","); //LNR
			insertOrUpdate.append(ba25_obj.getSupplierNumber()).append(","); //BENDL
			insertOrUpdate.append("'"+ba25_obj.getDeliveryNoteNo()+"' ,"); //NRX
			insertOrUpdate.append(" '' , '01', '' , '' ,"); //RT,LAUFNR,BA,REKOW,
			insertOrUpdate.append(getCurrentDate()).append(","); //LFSDAT_JMT
			insertOrUpdate.append(getCurrentDate()).append(","); //RECDAT_JMT
			insertOrUpdate.append(getCurrentTime().substring(0, 2)).append(","); //RECTIM_STD
			insertOrUpdate.append(getCurrentTime().substring(2, 4)).append(","); //RECTIM_MIN
			insertOrUpdate.append(getCurrentTime().substring(4, 6)).append(","); //RECTIM_SEC
			insertOrUpdate.append(" '' , '' , 0 , "); //REC_WS, PRUEFUNG, GPOS
			insertOrUpdate.append(" '000000000+' , 0 , "); //GWERT, DPOS
			insertOrUpdate.append(" '000000000+' , 0 , "); //DWERT, HPOS
			insertOrUpdate.append(" '000000000+' , 0 , "); //HWERT, BPOS
			insertOrUpdate.append(" '000000000+' , '000000000+' , "); //BWERT, NWERT
			insertOrUpdate.append(" 'BSN5BA' , '' )"); //PROGRAMM, ERLEDIGT 
		}

		return insertOrUpdate.toString();
	}

	
	private String getCurrentDate(){
		LocalDate today = LocalDate.now();
		String month = StringUtils.leftPad(String.valueOf(today.getMonthValue()),2,"0");
		String year = String.valueOf(today.getYear());
		String day = StringUtils.leftPad(String.valueOf(today.getDayOfMonth()),2,"0");
		
		return year+month+day;
	}
	
	private String getCurrentDateInYYMMDD(){
		LocalDate today = LocalDate.now();
		String month = StringUtils.leftPad(String.valueOf(today.getMonthValue()),2,"0");
		String year = String.valueOf(today.getYear());
		String day = StringUtils.leftPad(String.valueOf(today.getDayOfMonth()),2,"0");
		
		return year.substring(2,4)+month+day;
	}
	
	private static String getCurrentTime(){
		LocalTime today = LocalTime.now();
		String hour = StringUtils.leftPad(String.valueOf(today.getHour()), 2,"0");
		String min = StringUtils.leftPad(String.valueOf(today.getMinute()), 2,"0");
		String sec = StringUtils.leftPad(String.valueOf(today.getSecond()), 2,"0");
		
		return hour+min+sec;
	}

	private String insertValueInElfsL(BusinessCases25DTO ba25_obj, String dataLibrary, String satzart, String partsIndikator, String calculatedNetPrice) {
		log.info("Inside insertValueInElfsL method of BusinessCasesImpl");
		
		String manufacturer = ba25_obj.getManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING : ba25_obj.getManufacturer();
		
		StringBuilder query = new StringBuilder("select APOS   FROM ").append(dataLibrary).append(".E_LFSL WHERE  Herst= '").append(manufacturer).append("' AND LNR=");
		query.append(ba25_obj.getWarehouseNumber()).append(" AND NRX =  '").append(ba25_obj.getDeliveryNoteNo()).append("' AND BENDL=").append(ba25_obj.getSupplierNumber());
		query.append(" AND AUFTRAG = 0 AND UPOS = 99 order by APOS desc fetch first 1 rows only ");
		
		int apos = 0;
		List<BADeliveryNotes> BADeliveryNotesList = dbServiceRepository.getResultUsingQuery(BADeliveryNotes.class, query.toString(), true);
		
		
		if(BADeliveryNotesList!=null && !BADeliveryNotesList.isEmpty()){
			apos = BADeliveryNotesList.get(0).getApos().intValue()+1;
		}
		
		double zubsn450 = Double.parseDouble(ba25_obj.getBookingAmount()) * -1;
		double wertB = zubsn450  * Double.parseDouble(ba25_obj.getInputListPrice()); //ip EPR
		double wertN = zubsn450  * Double.parseDouble(calculatedNetPrice);
		
		StringBuilder insertQuery = new StringBuilder("INSERT INTO ").append(dataLibrary).append(".E_LFSL( HERST,LNR,BENDL, NRX,AA, AUFTRAG, APOS, UPOS, ZUMENG, ZUBSN450, EPREIS, ");
		insertQuery.append(" NEPREIS,MARKE,RABGR,RABSATZ,WERTB,WERTN,TA,ERNPR,EKNPR,SONDER_RAB, TNR, DAK, RECDAT, RECTIME, PROGRAMM ) VALUES ( ");
		insertQuery.append("'"+manufacturer+"',"); //HERST
		insertQuery.append(ba25_obj.getWarehouseNumber()).append(",");//LNR
		insertQuery.append(ba25_obj.getSupplierNumber()).append(", '");//BENDL
		insertQuery.append(ba25_obj.getDeliveryNoteNo()).append("', ");//NRX
		insertQuery.append("0 ,");//AA
		insertQuery.append("0 ,");//AUFTRAG
		insertQuery.append(apos).append(", ");//APOS
		insertQuery.append("99 , ");//UPOS
		insertQuery.append(zubsn450).append(", ");//ZUMENG
		insertQuery.append(zubsn450).append(", ");//ZUBSN450
		insertQuery.append(ba25_obj.getInputListPrice()).append(", ");//EPREIS  ip EPR
		insertQuery.append(calculatedNetPrice).append(", '");//NEPREIS
		insertQuery.append(ba25_obj.getOemBrand()).append("', ");//MARKE
		insertQuery.append(ba25_obj.getDiscountGroup()).append(", ");//RABGR
		insertQuery.append(satzart).append(", ");//RABSATZ
		insertQuery.append(decimalformat_fixtwodigit.format(wertB)).append(", ");//WERTB
		insertQuery.append(decimalformat_fixtwodigit.format(wertN)).append(", ");//WERTN
		insertQuery.append(ba25_obj.getPartsIndikator()).append(", ");//TA
		insertQuery.append(calculatedNetPrice).append(", ");//ERNPR
		insertQuery.append(calculatedNetPrice).append(", ");//EKNPR
		insertQuery.append("0").append(" ,");//SONDER
		insertQuery.append("'"+ba25_obj.getPartNumber()+"',"); //TNR
		insertQuery.append(ba25_obj.getAverageNetPrice()).append(", ");//DAK
		insertQuery.append(getCurrentDate()).append(", ");//RECDAT
		insertQuery.append(getCurrentTime()).append(", ");//RECTIME
		insertQuery.append("'BSN525' ) "); //TNR
		
		
		return insertQuery.toString();
	}


	private String getBookingAccount(String warehouseNumber, String oemBrand,
			String partsIndikator, String dataLibrary, String schema) {
		log.info("Inside getBookingAccount method of BusinessCasesImpl");
		
		String bookingAccount = "0";
		StringBuilder booking_account_query = new StringBuilder("SELECT BUK_KONTO  FROM  ").append(schema).append(".PEI_BUKONT  ");
		booking_account_query.append(" WHERE  BUK_DTGULV   <= current date AND BUK_LNR ='").append(warehouseNumber).append("' and BUK_TMARKE ='");
		booking_account_query.append(oemBrand).append("' AND BUK_TEIART = '").append(partsIndikator).append("' ORDER BY BUK_LNR, BUK_KONTO, BUK_TMARKE, BUK_TEIART ");
		
		
		List<BookingAccount> bookingAccountList = dbServiceRepository.getResultUsingQuery(BookingAccount.class, booking_account_query.toString(), true);
		
		if(bookingAccountList!=null && !bookingAccountList.isEmpty()) {
			bookingAccount = String.valueOf(bookingAccountList.get(0).getBookingNumber());
			
		}
		return bookingAccount;
	}


	private String getVorbelValue(String schema, BusinessCases25DTO ba25_obj) {
		log.info("Inside getVorbelValue method of BusinessCasesImpl");
		
		StringBuilder pmh_tmarke_query = new StringBuilder("select TMA_VORBEL from ").append(schema).append(".PMH_TMARKE  ");
		pmh_tmarke_query.append(" WHERE  TMA_TMARKE  = '").append(ba25_obj.getOemBrand()).append("' order by TMA_TMARKE asc ");
		String vorbelValue = "M";
		
		List<InventoryParts> partsBrandList = dbServiceRepository.getResultUsingQuery(InventoryParts.class, pmh_tmarke_query.toString(), true);
		
		if (partsBrandList != null && !partsBrandList.isEmpty()) {
			
			for (InventoryParts partBrand : partsBrandList) {
				vorbelValue = partBrand.getVorbelValue();
			}
		}
		return vorbelValue;
	}


	private Map<String, String> setupDefaultValues(String dataLibrary, BusinessCases25DTO ba25_obj,
			String defaultMarketingCode) {
		
		log.info("Inside setupDefaultValues method of BusinessCasesImpl");
		
		String manufacturer = ba25_obj.getManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING : ba25_obj.getManufacturer();
		Map<String, String> defaultValuesMap = new HashMap<String, String>();
		//determines Marke
		BA25DefaultSetup bsnpam1_dtls = getDefaultValuesByDefaultMcode(dataLibrary,ba25_obj, defaultMarketingCode, "E_BSNPAM1"," MARKE as TMARKE ");  
		
		if(bsnpam1_dtls!=null && bsnpam1_dtls.getOemBrand()!=null && !bsnpam1_dtls.getOemBrand().trim().isEmpty() ){
			defaultValuesMap.put("Marke", bsnpam1_dtls.getOemBrand()); 
		}else if(ba25_obj.getOemBrand()!=null && !ba25_obj.getOemBrand().trim().isEmpty()){
			defaultValuesMap.put("Marke",ba25_obj.getOemBrand());
		}else{
			log.info("MA nicht ermitt.");
			AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.BUSINESS_CASES_25_FAILED_MSG_KEY_5));
			log.error(messageService.getReadableMessage(ExceptionMessages.BUSINESS_CASES_25_FAILED_MSG_KEY_5));
			throw exception;
		}
		
		//determines Leiferwerk
		BA25DefaultSetup bsnpam2_dtls = getDefaultValuesByDefaultMcode(dataLibrary,ba25_obj, defaultValuesMap.get("Marke"), "E_BSNPAM2"," LW as Leiferwerk ");  
		
		if(bsnpam2_dtls!=null && bsnpam2_dtls.getBodywork()!=null && !bsnpam2_dtls.getBodywork().trim().isEmpty() ){
			defaultValuesMap.put("Leiferwerk",bsnpam2_dtls.getBodywork());
		}else{
			String bodyWork = "50";
			if(!manufacturer.equalsIgnoreCase("DCAG")){
				bodyWork = "99";
			}
			
			defaultValuesMap.put("Leiferwerk",bodyWork);
		}
		
		//determines Teilart 
		
		BA25DefaultSetup bsnpam3_dtls = getDefaultValuesByDefaultMcode(dataLibrary,ba25_obj, defaultValuesMap.get("Marke"), "E_BSNPAM3", " TA, LEART as LEIART ");  
		
		if(bsnpam3_dtls!=null && bsnpam3_dtls.getPartsIndikator()!=null){
			defaultValuesMap.put("Teilart",String.valueOf(bsnpam3_dtls.getPartsIndikator()));
		}else{
			defaultValuesMap.put("Teilart","01"); 
		}
		
		//determines Leistungsart
		if(bsnpam3_dtls!=null && bsnpam3_dtls.getActivityType()!=null){
			defaultValuesMap.put("Leistungsart",String.valueOf(bsnpam3_dtls.getActivityType())); 
		}else{
			defaultValuesMap.put("Leistungsart","10");  
		}
		
		
		return defaultValuesMap;
	}


	private BA25DefaultSetup getDefaultValuesByDefaultMcode(String dataLibrary, BusinessCases25DTO ba25_obj, String marketingCode, String tableName, String columnName) {
		//log.info("Inside getDefaultValuesByMcode method of BusinessCasesImpl");
		
		String manufacturer = ba25_obj.getManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING : ba25_obj.getManufacturer();
		BA25DefaultSetup details = null;

		StringBuilder queryForDefaultValues = new StringBuilder("SELECT ").append(columnName).append("  FROM ");
		queryForDefaultValues.append(dataLibrary).append(".").append(tableName).append(" WHERE ");
		queryForDefaultValues.append(" HERST = ").append("'"+manufacturer+"' ");
		//queryForDefaultValues.append(" AND LNR = 01 "); //ALPHAX-5216
		queryForDefaultValues.append(" AND MCODE = '").append(marketingCode).append("'");

		List<BA25DefaultSetup> partDtls_obj  = dbServiceRepository.getResultUsingQuery(BA25DefaultSetup.class,queryForDefaultValues.toString(),true);
		
		if(partDtls_obj!=null && !partDtls_obj.isEmpty()){
			details = partDtls_obj.get(0);
		}
		
		return details;
	}
	
	private BA25DefaultSetup getDefaultValuesByMcode(String dataLibrary, BusinessCases25DTO ba25_obj, String marketingCode, String tableName, String columnName) {
		//log.info("Inside getDefaultValuesByMcode method of BusinessCasesImpl");
		
		String manufacturer = ba25_obj.getManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING : ba25_obj.getManufacturer();
		BA25DefaultSetup details = null;

		StringBuilder queryForDefaultValues = new StringBuilder("SELECT ").append(columnName).append(" FROM ");
		queryForDefaultValues.append(" ( SELECT MARKE, mcode, LW ,TA, LEART, ");
		queryForDefaultValues.append(" (case when ( substr(MCODE ,1 ,1) != ' ' ) THEN	32 ELSE	0 end) + ");
		queryForDefaultValues.append(" (case when ( substr(MCODE ,2 ,1) != ' ' ) THEN	16 ELSE	0 end) + ");
		queryForDefaultValues.append(" (case when ( substr(MCODE ,3 ,1) != ' ' ) THEN	8  ELSE	0 end) + ");
		queryForDefaultValues.append(" (case when ( substr(MCODE ,4 ,1) != ' ' ) THEN	4  ELSE	0 end) + ");
		queryForDefaultValues.append(" (case when ( substr(MCODE ,5 ,1) != ' ' ) THEN	2  ELSE	0 end) + ");
		queryForDefaultValues.append(" (case when ( substr(MCODE ,6 ,1) != ' ' ) THEN	1  ELSE	0 end) as faktor FROM ");
		queryForDefaultValues.append(dataLibrary).append(".").append(tableName).append(" WHERE ");
		queryForDefaultValues.append(" HERST = ").append("'"+manufacturer+"' AND ");
		//queryForDefaultValues.append(" AND LNR = 01 AND "); //ALPHAX-5216
		queryForDefaultValues.append(" ( substr('").append(marketingCode).append("' ,1 ,1) = substr(MCODE, 1, 1) OR substr(MCODE, 1, 1) = ' ') AND ");
		queryForDefaultValues.append(" ( substr('").append(marketingCode).append("' ,2 ,1) = substr(MCODE, 2, 1) OR substr(MCODE, 2, 1) = ' ') AND ");
		queryForDefaultValues.append(" ( substr('").append(marketingCode).append("' ,3 ,1) = substr(MCODE, 3, 1) OR substr(MCODE, 3, 1) = ' ') AND ");
		queryForDefaultValues.append(" ( substr('").append(marketingCode).append("' ,4 ,1) = substr(MCODE, 4, 1) OR substr(MCODE, 4, 1) = ' ') AND ");
		queryForDefaultValues.append(" ( substr('").append(marketingCode).append("' ,5 ,1) = substr(MCODE, 5, 1) OR substr(MCODE, 5, 1) = ' ') AND ");
		queryForDefaultValues.append(" ( substr('").append(marketingCode).append("' ,6 ,1) = substr(MCODE, 6, 1) OR substr(MCODE, 6, 1) = ' ')  ");
		queryForDefaultValues.append(" ORDER by faktor desc	 fetch first row only ) ");

		List<BA25DefaultSetup> partDtls_obj  = dbServiceRepository.getResultUsingQuery(BA25DefaultSetup.class,queryForDefaultValues.toString(),true);
		
		if(partDtls_obj!=null && !partDtls_obj.isEmpty()){
			details = partDtls_obj.get(0);
		}
		
		return details;
	}
	
	/*private List<BA25DefaultSetup> checkDefaultValuesByTNR(String dataLibrary, BusinessCases25DTO ba25_obj, String string) {
		//log.info("Inside checkDefaultValuesByTNR method of BusinessCasesImpl");
		
		String manufacturer = ba25_obj.getManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING : ba25_obj.getManufacturer();

		StringBuilder queryForDefaultValues = new StringBuilder("SELECT MARKE as TMARKE, LW as Leiferwerk, TA, LEART as LEIART,PKZ FROM ");
		queryForDefaultValues.append(dataLibrary).append(".E_BSNPAR  WHERE ");
		queryForDefaultValues.append("  HERST = ").append("'"+manufacturer+"'");
		queryForDefaultValues.append(" AND LNR = 01 ");
		queryForDefaultValues.append(" AND SA = 'P' ");
		queryForDefaultValues.append(" AND MARKE = '").append(ba25_obj.getOemBrand()).append("' ");
		queryForDefaultValues.append(" AND RG = '").append(ba25_obj.getDiscountGroup()).append("' ");
		queryForDefaultValues.append(" AND TNR = '").append(ba25_obj.getPartNumber()).append("' ");
		

		List<BA25DefaultSetup> partDtls_obj  = dbServiceRepository.getResultUsingQuery(BA25DefaultSetup.class,queryForDefaultValues.toString(),true);
		
		return partDtls_obj;
	}*/
	
	private List<BA25DefaultSetup> checkDefaultValuesByTNR(String dataLibrary, BusinessCases25DTO ba25_obj, String string) {
		//log.info("Inside checkDefaultValuesByTNR method of BusinessCasesImpl");
		
		String manufacturer = ba25_obj.getManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING : ba25_obj.getManufacturer();

		StringBuilder queryForDefaultValues = new StringBuilder("SELECT MARKE as TMARKE, LW as Leiferwerk, TA, LEART as LEIART,PKZ FROM  ");
		queryForDefaultValues.append("( SELECT MARKE, LW, TA, LEART, PKZ, TNR, ");
		queryForDefaultValues.append("(case when ( substr(KB ,1 ,1) != ' '  ) THEN 262144  ELSE 0 end) + ");
		queryForDefaultValues.append("(case when ( substr(TNR ,1 ,1) != ' ' ) THEN 131072  ELSE 0 end) + ");
		queryForDefaultValues.append("(case when ( substr(TNR ,2 ,1) != ' ' ) THEN 65536   ELSE 0 end) + ");
		queryForDefaultValues.append("(case when ( substr(TNR ,3 ,1) != ' ' ) THEN 32768   ELSE 0 end) + ");
		queryForDefaultValues.append("(case when ( substr(TNR ,4 ,1) != ' ' ) THEN 16384   ELSE 0 end) + ");
		queryForDefaultValues.append("(case when ( substr(TNR ,5 ,1) != ' ' ) THEN 8192    ELSE 0 end) + ");
		queryForDefaultValues.append("(case when ( substr(TNR ,6 ,1) != ' ' ) THEN 4096    ELSE 0 end) + ");
		queryForDefaultValues.append("(case when ( substr(TNR ,7 ,1) != ' ' ) THEN 2048    ELSE 0 end) + ");
		queryForDefaultValues.append("(case when ( substr(TNR ,8 ,1) != ' ' ) THEN 1024    ELSE 0 end) + ");
		queryForDefaultValues.append("(case when ( substr(TNR ,9 ,1) != ' ' ) THEN 512     ELSE 0 end) + ");
		queryForDefaultValues.append("(case when ( substr(TNR ,10 ,1) != ' ') THEN 256     ELSE 0 end) + ");
		queryForDefaultValues.append("(case when ( substr(TNR ,11 ,1) != ' ') THEN 128     ELSE 0 end) + ");
		queryForDefaultValues.append("(case when ( substr(TNR ,12 ,1) != ' ') THEN 64      ELSE 0 end) + ");
		queryForDefaultValues.append("(case when ( substr(TNR ,13 ,1) != ' ') THEN 32      ELSE 0 end) + ");
		queryForDefaultValues.append("(case when ( substr(TNR ,14 ,1) != ' ') THEN 16      ELSE 0 end) + ");
		queryForDefaultValues.append("(case when ( substr(TNR ,15 ,1) != ' ') THEN 8       ELSE 0 end) + ");
		queryForDefaultValues.append("(case when ( substr(TNR ,16 ,1) != ' ') THEN 4       ELSE 0 end) + ");
		queryForDefaultValues.append("(case when ( substr(TNR ,17 ,1) != ' ') THEN 2       ELSE 0 end) + ");
		queryForDefaultValues.append("(case when ( substr(TNR ,18 ,1) != ' ') THEN 1       ELSE 0 end) as faktor FROM ");
		queryForDefaultValues.append(dataLibrary).append(".E_BSNPAR  WHERE ");
		queryForDefaultValues.append("  HERST = ").append("'"+manufacturer+"' ");
	//	queryForDefaultValues.append(" AND LNR = 01 "); //ALPHAX-5216
		queryForDefaultValues.append(" AND SA = 'P' ");
		queryForDefaultValues.append(" AND MARKE = '").append(ba25_obj.getOemBrand()).append("' ");
		queryForDefaultValues.append(" AND RG = '").append(ba25_obj.getDiscountGroup()).append("' AND ");
		queryForDefaultValues.append("(substr('A' ,1 ,1) = substr(KB, 1, 1) OR substr(KB, 1, 1) = ' ') AND ");
		queryForDefaultValues.append("(substr('0001800009SA' ,1 ,1) = substr(TNR, 1, 1) OR substr(TNR, 1, 1) = ' ') AND ");
		queryForDefaultValues.append("(substr('0001800009SA' ,2 ,1) = substr(TNR, 2, 1) OR substr(TNR, 2, 1) = ' ') AND ");
		queryForDefaultValues.append("(substr('0001800009SA' ,3 ,1) = substr(TNR, 3, 1) OR substr(TNR, 3, 1) = ' ') AND ");
		queryForDefaultValues.append("(substr('0001800009SA' ,4 ,1) = substr(TNR, 4, 1) OR substr(TNR, 4, 1) = ' ') AND ");
		queryForDefaultValues.append("(substr('0001800009SA' ,5 ,1) = substr(TNR, 5, 1) OR substr(TNR, 5, 1) = ' ') AND ");
		queryForDefaultValues.append("(substr('0001800009SA' ,6 ,1) = substr(TNR, 6, 1) OR substr(TNR, 6, 1) = ' ') AND ");
		queryForDefaultValues.append("(substr('0001800009SA' ,7 ,1) = substr(TNR, 7, 1) OR substr(TNR, 7, 1) = ' ') AND ");
		queryForDefaultValues.append("(substr('0001800009SA' ,8 ,1) = substr(TNR, 8, 1) OR substr(TNR, 8, 1) = ' ') AND ");
		queryForDefaultValues.append("(substr('0001800009SA' ,9 ,1) = substr(TNR, 9, 1) OR substr(TNR, 9, 1) = ' ') AND ");
		queryForDefaultValues.append("(substr('0001800009SA' ,10 ,1) = substr(TNR, 10, 1) OR substr(TNR, 10, 1) = ' ') AND ");
		queryForDefaultValues.append("(substr('0001800009SA' ,11 ,1) = substr(TNR, 11, 1) OR substr(TNR, 11, 1) = ' ') AND ");
		queryForDefaultValues.append("(substr('0001800009SA' ,12 ,1) = substr(TNR, 12, 1) OR substr(TNR, 12, 1) = ' ') AND ");
		queryForDefaultValues.append("(substr('0001800009SA' ,13 ,1) = substr(TNR, 13, 1) OR substr(TNR, 13, 1) = ' ') AND ");
		queryForDefaultValues.append("(substr('0001800009SA' ,14 ,1) = substr(TNR, 14, 1) OR substr(TNR, 14, 1) = ' ') AND ");
		queryForDefaultValues.append("(substr('0001800009SA' ,15 ,1) = substr(TNR, 15, 1) OR substr(TNR, 15, 1) = ' ') AND ");
		queryForDefaultValues.append("(substr('0001800009SA' ,16 ,1) = substr(TNR, 16, 1) OR substr(TNR, 16, 1) = ' ') AND ");
		queryForDefaultValues.append("(substr('0001800009SA' ,17 ,1) = substr(TNR, 17, 1) OR substr(TNR, 17, 1) = ' ') AND ");
		queryForDefaultValues.append("(substr('0001800009SA' ,18 ,1) = substr(TNR, 18, 1) OR substr(TNR, 18, 1) = ' ') ");
		queryForDefaultValues.append("ORDER by faktor desc  fetch first row only) ");
		

		List<BA25DefaultSetup> partDtls_obj  = dbServiceRepository.getResultUsingQuery(BA25DefaultSetup.class,queryForDefaultValues.toString(),true);
		
		return partDtls_obj;
	}



	private boolean priceCorrectionCheck(String dataLibrary, String listPrice, String bookingAmount) {
		
		log.info("Inside priceCorrectionCheck method of BusinessCasesImpl");
		
		boolean isPriceChange = false;
		StringBuilder query = new StringBuilder("select  KEYFLD, DATAFLD from ");
		query.append(dataLibrary).append(".M1_KOND where KEYFLD like '3064%' ");

		Map<String, String> priceMap = dbServiceRepository.getResultUsingCobolQuery(query.toString());
		
		if(priceMap != null && !priceMap.isEmpty()) {
			for(Map.Entry<String, String> priceDetails : priceMap.entrySet()) {

				String value = priceDetails.getValue();
				
				String fromPrice = value.substring(0, 6)+"."+value.substring(6, 8);
				String toPrice = value.substring(8, 14)+"."+value.substring(14, 16);
				String amount = value.substring(16, 21)+"."+value.substring(21);
				
				if(value.length()==22){
					if(Double.parseDouble(listPrice) >= Double.parseDouble(fromPrice) &&
							Double.parseDouble(listPrice) <= Double.parseDouble(toPrice) && 
							Double.parseDouble(bookingAmount) <= Double.parseDouble(amount) ){
						isPriceChange = true;
						break;
					}
				}
			}
		}
		
		return isPriceChange;
	}
	
	
	private String insertValuesInCPSDAT(String dataLibrary, BusinessCases25DTO ba25_obj, String calculatedNetPrice,
			String satzart, String companyId, String agencyId, String loginUserName, String partsIndikator, String bookingAccount, String activityType) {
		
		log.info("Inside insertValuesInCPSDAT method of BusinessCasesImpl");
		String manufacturer = ba25_obj.getManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING : ba25_obj.getManufacturer();
		
		String kb = "";
		String etnr ="";
		String es1 = "";
		String es2 = "";
		
			if(ba25_obj.getPartNumber() != null) {
				String partNo = StringUtils.rightPad(ba25_obj.getPartNumber(), 19, " ");
				
				kb = partNo.substring(0,1);
				etnr = partNo.substring(1,13);
				es1 = partNo.substring(13,15);
				es2 = partNo.substring(15,19);
			}
		
			
			StringBuilder query = new StringBuilder(" INSERT INTO ").append(dataLibrary).append(".E_CPSDAT ");
			query.append(" (HERST,BART,VFNR,BELNR,POSNR,KB ,ETNR ,ES1 ,ES2 ,T1 ,T2, T3,T4, EBEST ,EB_VZ ,WS, HERK, ETNR_S,EPREW ,EPREWK,DAKEWV ,DAKEWN,ERNPR ,ERNPRK, RABGRV ,RABGRN,MARKEV ,MARKEN, ");
			query.append(" PROZ ,TA_EWV ,TA_EWN ,LW_EWV ,LW_EWN, NT_EWV ,NT_EWN, BSN_LC, MCODE,EKNPR, TNRD, AG,FIRMA ,FILIAL,VCODE,WSUSER, ");
			query.append(" BUK_KONTOV, BUK_KONTON, NPRKAL, DIMSCODE ,JJMMTT , HHMMSS, XMLDAT, XMLTIM, BVDAUF, BVDAUP,  BSNOME, SERVAN )  values ( ");
			query.append("'"+manufacturer+"', 25, "); // HERST,BART
			query.append(" "+ba25_obj.getWarehouseNumber()+", "); // VFNR
			query.append("'"+ba25_obj.getDeliveryNoteNo()+"' ,"); //BELNR
			query.append("'"+StringUtils.leftPad(ba25_obj.getSalesOrderPosition(), 3, "0") +"' ,"); //POSNR
			query.append("'"+kb+"', "+"'"+etnr+"', "+"'"+es1+"', "+"'"+es2+"', ");  //KB ,ETNR ,ES1 ,ES2

			String mengaValue = ba25_obj.getBookingAmount();
			if(mengaValue!=null && !mengaValue.contains(".")) {
				mengaValue = mengaValue+".0";
			}
			//Format – SPACES(2)+ Input Menge 9(5)v9 + EPREIS 9(5)v9(2) where EPREIS = ip EPR
			query.append("'  "+convertDecimalValue(mengaValue, 5, 1)+convertDecimalValue(ba25_obj.getInputListPrice(), 5, 2)+"', "); // T1  getInputListPrice = ip EPR
			query.append("'"+StringUtils.leftPad(activityType, 3, "0")+"', "); //  T2 
			query.append("'000000', "); //  T3
			query.append("'"+StringUtils.rightPad("0000", 18, " ")+"00', ");  //T4 
			
			String currentStock = String.valueOf(Double.parseDouble(ba25_obj.getCurrentStock())-Double.parseDouble(ba25_obj.getBookingAmount()));
			query.append("'"+convertDecimalValue(currentStock, 5, 1)+"', "); //EBEST  
			
			if(Double.parseDouble(ba25_obj.getBookingAmount()) >= 0) {
			query.append("'+', "); // EB_VZ  
			}else {
			query.append("'-', "); // EB_VZ  
			}
			query.append(" ' ', ");  // WS
			query.append(" 'BSN5BA', ");  // HERK
			query.append("'"+ba25_obj.getSortingFormatPartNumber()+ "', "); // ETNR_S
			query.append("'"+StringUtils.leftPad(convertDecimalValue(ba25_obj.getInputListPrice(), 5, 2), 7, "0") + "', 'N', "); //  EPREW ,EPREWK
			
			query.append("'"+StringUtils.leftPad(convertDecimalValue(ba25_obj.getAverageNetPrice(), 5, 4), 9, "0")+ "', "); //DAKEWV
			query.append("'"+StringUtils.leftPad(convertDecimalValue(ba25_obj.getAverageNetPrice(), 5, 4), 9, "0")+ "', "); //DAKEWN
			
			if(Double.parseDouble(ba25_obj.getListPrice()) >= 0) {
			query.append("'"+StringUtils.left(convertDecimalValue(calculatedNetPrice, 5, 2),7)+"', ");//ERNPR	
			}else {
				query.append("'"+StringUtils.left(convertDecimalValue(ba25_obj.getLastPurchasePrice(), 5, 2),7)+"', ");//ERNPR	
			}
			
			query.append("'N', ");//ERNPRK
			
			if(manufacturer.equalsIgnoreCase("DCAG") && ba25_obj.getDiscountGroup().length() > 1) {
			query.append("'"+ba25_obj.getDiscountGroup().substring(0, 2) + "', '"+ba25_obj.getDiscountGroup().substring(0, 2) + "', ");//RABGRV ,RABGRN  
			}else {
			query.append("'"+ba25_obj.getDiscountGroup()+ "', '"+ba25_obj.getDiscountGroup()+ "', ");//RABGRV ,RABGRN  	
			}
			query.append("'"+ba25_obj.getOemBrand() + "', '"+ba25_obj.getOemBrand() + "', ");//MARKEV ,MARKEN  
			
			query.append("'"+StringUtils.leftPad(convertDecimalValue(satzart, 2, 2), 5, "0")+"', ");//PROZ
			
			query.append("'"+StringUtils.leftPad(ba25_obj.getPartsIndikator(), 2, "0") +"', '"+ StringUtils.leftPad(ba25_obj.getPartsIndikator(), 2, "0") +"', "); //TA_EWV ,TA_EWN 
			query.append("'"+StringUtils.leftPad(ba25_obj.getDeliverIndicator(), 2, "0") +"', '"+ StringUtils.leftPad(ba25_obj.getDeliverIndicator(), 2, "0") +"', "); //LW_EWV ,LW_EWN
			
			if(Double.parseDouble(ba25_obj.getListPrice())> 0) {
				query.append("'"+StringUtils.left(convertDecimalValue(calculatedNetPrice, 5, 2),7)+ "', ");//NT_EWV 
				query.append("'0000000',  "); //NT_EWN 
			}else {
				query.append("'"+StringUtils.left(convertDecimalValue(ba25_obj.getLastPurchasePrice(), 5, 2),7)+ "', ");//NT_EWV 
				query.append("'"+StringUtils.left(convertDecimalValue(ba25_obj.getLastPurchasePrice(), 5, 2),7)+ "', ");//NT_EWV 
			}
			
			query.append("'"+ba25_obj.getSupplierNumber()+"', "); //BSN_LC
			
			if(ba25_obj.getMarketingCode().trim().equalsIgnoreCase("") && manufacturer.equalsIgnoreCase("DCAG")){
				query.append("'D00000' ,"); //MCODE
			}else if(ba25_obj.getMarketingCode().trim().equalsIgnoreCase("") && !manufacturer.equalsIgnoreCase("DCAG")){
				query.append("'X00000' ,"); //MCODE
			}else{
				query.append("'"+ba25_obj.getMarketingCode()+"' ,");
			}
			
			query.append(" "+ba25_obj.getNetShoppingPrice()+","); //EKNPR
			query.append("'"+ba25_obj.getPrintingFormatPartNumber()+"', "); // TNR Druck format
			
			query.append("'"+ba25_obj.getCustomerGroup()+"',"); //AG
			query.append("'"+StringUtils.leftPad(companyId, 2, "0")+"', '"+StringUtils.leftPad(agencyId, 2, "0")+"', "); //FIRMA ,FILIAL
			query.append("'"+ba25_obj.getCustomerGroup()+"',"); //VCODE
			query.append("'"+loginUserName+"' ,"); // WSUSER
			query.append(" "+bookingAccount+"  ,"); // BUK_KONTOV
			query.append(" "+bookingAccount+" ,"); // BUK_KONTON
			
			query.append(ba25_obj.getPreviousPurchasePrice()+", "); // NPRKAL
			
			if(ba25_obj.getCustomerGroup()!=null && ba25_obj.getCustomerGroup().equalsIgnoreCase("6")) {
			query.append("'R41R' , "); // DIMSCODE
			}else {
				query.append("' ' , "); // DIMSCODE
			}
			query.append("'"+getCurrentDateInYYMMDD()+"' ,"); // JJMMTT
			query.append("'"+getCurrentTime()+"' ,"); // HHMMSS
			
			query.append(" '000000', '000000', '00000', '00000', '0000000' , '000000' )  "); // HHMMSS
			
		return query.toString();
	}
	
	
	/**
	 * This method is used for BA06
	 * 
	 * The BA06 enhances the stock.It is meant for technical corrections.
	 */
	@Override
	public Map<String, String> newJavaImplementation_BA06(BusinessCases25DTO  ba25_obj, String dataLibrary,String schema,
			String companyId, String agencyId, String loginUserName ,Map<String, String> output_bsn475){
		log.info("Inside newJavaImplementation_BA06 method of BusinessCasesImpl");

		Map<String, String> programOutput = new HashMap<String, String>();
		programOutput.put("00000"," BA06 failed ");

		String oemBrand = ""; // MARKE
		String bodyWork = ""; //Leiferwerk
		String partsIndikator = ""; //Teilart
		String activityType = ""; //Leistungsart

		try(Connection con = dbServiceRepository.getConnectionObject();
				Statement stmt = con.createStatement();){

			//con.setAutoCommit(false);
			String manufacturer = ba25_obj.getManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING : ba25_obj.getManufacturer();

			if(ba25_obj.getNetShoppingPrice() == null || ba25_obj.getNetShoppingPrice().trim().isEmpty()) {
				ba25_obj.setNetShoppingPrice("0.00");
			}

			if(ba25_obj.getBookingAmount() == null || ba25_obj.getBookingAmount().trim().isEmpty()) {
				ba25_obj.setBookingAmount("0.00");
			}

			//delivery note Check
			StringBuilder query = new StringBuilder("SELECT COUNT(*) as count FROM ");
			query.append(dataLibrary).append(".E_BSNLFS WHERE ");
			query.append(" BA='' ");
			query.append(" AND HERST = ").append("'"+manufacturer+"'");
			query.append(" AND LNR = ").append(ba25_obj.getWarehouseNumber());
			query.append(" AND BENDL = ").append(ba25_obj.getSupplierNumber());
			query.append(" AND UPPER(NRX) = UPPER('").append(ba25_obj.getDeliveryNoteNo()).append("') ");

			String count = dbServiceRepository.getCountUsingQuery(query.toString());

			if (Integer.parseInt(count) > 0) {

				if (ba25_obj.getInvoiceAudit() != null && ba25_obj.getInvoiceAudit().trim().equalsIgnoreCase("J")) {
					log.info("Delivery note done. Please choose a different delivery note");
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.BUSINESS_CASES_25_FAILED_MSG_KEY_1));
					log.error(messageService.getReadableMessage(ExceptionMessages.BUSINESS_CASES_25_FAILED_MSG_KEY_1));
					throw exception;
				} else if (ba25_obj.getDeliveryNoteAudit() != null && ba25_obj.getDeliveryNoteAudit().trim().equalsIgnoreCase("J")) {
					log.info("Delivery note closed");
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.BUSINESS_CASES_25_FAILED_MSG_KEY_2));
					log.error(messageService.getReadableMessage(ExceptionMessages.BUSINESS_CASES_25_FAILED_MSG_KEY_2));
					throw exception;
				}
			}

			//Price check
			if(!priceCorrectionCheck(dataLibrary, ba25_obj.getInputListPrice(), ba25_obj.getBookingAmount())) {
				log.info("Price implausible!");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.BUSINESS_CASES_25_PRICE_CHECK_FAILED_MSG_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.BUSINESS_CASES_25_PRICE_CHECK_FAILED_MSG_KEY));
				throw exception;
			}


			String satzart = getSatzart( dataLibrary, manufacturer, ba25_obj.getOemBrand(), ba25_obj.getDiscountGroup() );

			Double calculatedNetPriceDouble = 0.00;
			String rabSatz ="";
			if(Double.parseDouble(ba25_obj.getSpecialDiscount()) > 0 && Double.parseDouble(ba25_obj.getListPrice()) > 0 ) {

				calculatedNetPriceDouble = (Double.parseDouble(ba25_obj.getListPrice()) - 
						(Double.parseDouble(ba25_obj.getListPrice()) * Double.parseDouble(ba25_obj.getSpecialDiscount()) / 100 ));
				rabSatz = ba25_obj.getSpecialDiscount();
				
			}
			else if(Double.parseDouble(ba25_obj.getNetShoppingPrice()) > 0) {
				calculatedNetPriceDouble = ba25_obj.getNetShoppingPrice() != null ? Double.parseDouble(ba25_obj.getNetShoppingPrice()) : Double.parseDouble("0.00");
				rabSatz = "00.00";
			}
			else if(ba25_obj.getPreviousPurchasePrice() != null && Double.parseDouble(ba25_obj.getPreviousPurchasePrice()) > 0) {
				calculatedNetPriceDouble = Double.parseDouble(ba25_obj.getPreviousPurchasePrice());
				rabSatz = "00.00";				
			}
			else if((satzart != null && !satzart.trim().isEmpty()) && Double.parseDouble(ba25_obj.getListPrice()) > 0){
				calculatedNetPriceDouble = (Double.parseDouble(ba25_obj.getListPrice()) - 
						(Double.parseDouble(ba25_obj.getListPrice()) * Double.parseDouble(satzart) / 100 ));				
				rabSatz = satzart;				
				if(satzart.equals("0.00")) {
					rabSatz = "00.00";
				}
			}

			Double calculatedDAK = (((Double.parseDouble(ba25_obj.getAverageNetPrice()) * Double.parseDouble(ba25_obj.getCurrentStock())) 
					+ (calculatedNetPriceDouble * Double.parseDouble(ba25_obj.getBookingAmount()))) 
					/ (Double.parseDouble(ba25_obj.getCurrentStock())  + Double.parseDouble(ba25_obj.getBookingAmount())));

			String calculatedNetPrice = String.valueOf(calculatedNetPriceDouble);

			if(output_bsn475==null) {
			 output_bsn475 = newJavaImplementation_BSN475(ba25_obj.getOemBrand(), dataLibrary, schema, 
					manufacturer, ba25_obj.getMarketingCode(), ba25_obj.getDiscountGroup(), ba25_obj.getPartNumber());
			}
			
			/*
			 * need to remove this code in next spring
			 * String defaultMarketingCode = "D00000";
			if(!manufacturer.equalsIgnoreCase("DCAG")){
				defaultMarketingCode = "X00000";
			}

			//setup default Marke, Leiferwerk, Teilart and Leistungsart using defaultMarketing code. 
			Map<String, String> defaultValues = setupDefaultValues(dataLibrary, ba25_obj, defaultMarketingCode );
			oemBrand = defaultValues.get("Marke");
			bodyWork = defaultValues.get("Leiferwerk");
			partsIndikator = defaultValues.get("Teilart");
			activityType = defaultValues.get("Leistungsart");

			//Call YMARKE  as per cobol
			String vorbelValue = getVorbelValue(schema, ba25_obj);

			if(vorbelValue!=null && !vorbelValue.isEmpty() && vorbelValue.equalsIgnoreCase("T")){

				//Determine values based on ETSTAMM - MCode 
				String etstamm_marketingCode = 	ba25_obj.getMarketingCode();
				//determines Marke
				BA25DefaultSetup bsnpam1_dtls = checkDefaultValuesByMcode(dataLibrary,ba25_obj, etstamm_marketingCode, "E_BSNPAM1"); 

				if(bsnpam1_dtls!=null && bsnpam1_dtls.getOemBrand()!=null && !bsnpam1_dtls.getOemBrand().trim().isEmpty() ){
					oemBrand = bsnpam1_dtls.getOemBrand();
				}

				//determines Leiferwerk
				BA25DefaultSetup bsnpam2_dtls = checkDefaultValuesByMcode(dataLibrary,ba25_obj, etstamm_marketingCode, "E_BSNPAM2"); 

				if(bsnpam2_dtls!=null && bsnpam2_dtls.getBodywork()!=null && !bsnpam1_dtls.getBodywork().trim().isEmpty() ){
					bodyWork = bsnpam2_dtls.getBodywork();
				}

				//determines Teilart 

				BA25DefaultSetup bsnpam3_dtls = checkDefaultValuesByMcode(dataLibrary,ba25_obj, etstamm_marketingCode, "E_BSNPAM3"); 

				if(bsnpam3_dtls!=null && bsnpam3_dtls.getPartsIndikator()!=null){
					partsIndikator = String.valueOf(bsnpam2_dtls.getPartsIndikator());
				}

				//determines Leistungsart
				if(bsnpam3_dtls!=null && bsnpam3_dtls.getActivityType()!=null){
					activityType = String.valueOf(bsnpam2_dtls.getActivityType());
				}
			}else{
				//Determine values based on ETSTAMM - TNR 			
				List<BA25DefaultSetup> bsnpar_list = checkDefaultValuesByTNR(dataLibrary,ba25_obj, "E_BSNPAR"); 

				if (bsnpar_list != null && !bsnpar_list.isEmpty()) {
					for (BA25DefaultSetup bsnpar_dtls : bsnpar_list) {

						if (bsnpar_dtls.getOemBrand() != null
								&& bsnpar_dtls.getOemBrand().equalsIgnoreCase(ba25_obj.getOemBrand())
								&& bsnpar_dtls.getDiscountGroup() != null
								&& bsnpar_dtls.getDiscountGroup().equalsIgnoreCase(ba25_obj.getDiscountGroup())
								&& bsnpar_dtls.getPartNumber() != null
								&& bsnpar_dtls.getPartNumber().equalsIgnoreCase(ba25_obj.getPartNumber())) {

							oemBrand = bsnpar_dtls.getOemBrand(); // Marke
							bodyWork = bsnpar_dtls.getBodywork(); //// Leiferwerk
							partsIndikator = String.valueOf(bsnpar_dtls.getPartsIndikator()); // Teilart
							activityType = String.valueOf(bsnpar_dtls.getActivityType()); // Leistungsart
							break;
						}
					}
				}

			}	*/
			
			oemBrand = output_bsn475.get("Marke");
			partsIndikator = output_bsn475.get("Teilart");
			if(null == oemBrand || oemBrand.trim().isEmpty()) {			
				oemBrand = ba25_obj.getOemBrand();
			}

			//Determine Booking account (BUCHUNGSKONTO)
			String bookingAccount = getBookingAccount(ba25_obj.getWarehouseNumber(), oemBrand, partsIndikator, dataLibrary, schema);
			
			//Special Case
			output_bsn475 =  checkSpecialCasesInBSN475(ba25_obj.getPartNumber(),  output_bsn475);
			oemBrand = output_bsn475.get("Marke"); //Marke
			bodyWork = output_bsn475.get("Leiferwerk"); //Leiferwerk
			partsIndikator = output_bsn475.get("Teilart"); //Teilart
			activityType = output_bsn475.get("Leistungsart"); //Leistungsart

			String cpsdatQuery = insertValuesInCPSDAT_BA06(dataLibrary, ba25_obj, calculatedNetPrice, satzart, companyId, agencyId, loginUserName, 
					partsIndikator, bookingAccount, activityType, calculatedDAK, rabSatz );

			stmt.addBatch(cpsdatQuery.toString());
			log.info("CPSDAT Query  :"+cpsdatQuery.toString());

			String elfslQuery = insertValueInElfsL_BA06(ba25_obj, dataLibrary, satzart, partsIndikator, calculatedNetPrice, calculatedDAK);

			stmt.addBatch(elfslQuery.toString());
			log.info("E_LFSL Query  :"+elfslQuery.toString());

			String insertOrUpdateQuery1 = insertOrUpdateInBSNLFS_First(ba25_obj, dataLibrary);

			stmt.addBatch(insertOrUpdateQuery1.toString());
			log.info("E_BSNLFS Query - 1  :"+insertOrUpdateQuery1.toString());

			String insertOrUpdateQuery2 = insertOrUpdateInBSNLFS_Second(ba25_obj, dataLibrary, calculatedNetPrice, "06");

			stmt.addBatch(insertOrUpdateQuery2.toString());
			log.info("E_BSNLFS Query - 2  :"+insertOrUpdateQuery2.toString());

			String currentStock = String.valueOf(Double.parseDouble(ba25_obj.getCurrentStock()) + Double.parseDouble(ba25_obj.getBookingAmount()));


			StringBuilder update_ETSTAMM = new StringBuilder("Update ").append(dataLibrary).append(".E_ETSTAMM ");
			update_ETSTAMM.append(" set  DAK=").append(calculatedDAK);
			update_ETSTAMM.append(", EKNPR =").append(ba25_obj.getNetShoppingPrice());
			update_ETSTAMM.append(", LEKPR = '").append(decimalformat_fixFourdigit.format(Double.parseDouble(calculatedNetPrice))).append("'");
			update_ETSTAMM.append(", DTLBEW = '").append(getCurrentDate()).append("', AKTBES = ").append(currentStock);
			update_ETSTAMM.append(" where HERST = '").append(manufacturer);
			update_ETSTAMM.append("' and LNR = ").append(ba25_obj.getWarehouseNumber());
			update_ETSTAMM.append(" and TNR ='").append(ba25_obj.getPartNumber() + "' ");

			stmt.addBatch(update_ETSTAMM.toString());
			log.info("E_ETSTAMM Update:"+update_ETSTAMM.toString());

			//boolean isUpdated = dbServiceRepository.updateResultUsingQuery(update_ETSTAMM.toString(), con);

			int[] records = dbServiceRepository.insertResultUsingBatchQuery(stmt);
			if(records != null){
				log.info("In E_CPSDAT - Total rows Inserted  :"+records.length);
			}

			con.commit();
			
			programOutput.put("00000","BA06 success");
		}
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY,"BA06"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY,"BA06"), exception);
			throw exception;
		}

		return programOutput;
	}
	
	
	private String insertValuesInCPSDAT_BA06(String dataLibrary, BusinessCases25DTO ba25_obj, String calculatedNetPrice, 
			String satzart, String companyId, String agencyId, String loginUserName, String partsIndikator, String bookingAccount, 
			String activityType, Double calculatedDAK, String rabSatz) {

		log.info("Inside insertValuesInCPSDAT_BA06 method of BusinessCasesImpl");
		String manufacturer = ba25_obj.getManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING : ba25_obj.getManufacturer();

		String kb = "";
		String etnr ="";
		String es1 = "";
		String es2 = "";

		if(ba25_obj.getPartNumber() != null) {
			String partNo = StringUtils.rightPad(ba25_obj.getPartNumber(), 19, " ");

			kb = partNo.substring(0,1);
			etnr = partNo.substring(1,13);
			es1 = partNo.substring(13,15);
			es2 = partNo.substring(15,19);
		}

		StringBuilder query = new StringBuilder(" INSERT INTO ").append(dataLibrary).append(".E_CPSDAT "); // Add NPRKAL, RABPRZ, BUVORG, TVKZ
		query.append(" (HERST,BART,VFNR,BELNR,POSNR,KB ,ETNR ,ES1 ,ES2 ,T1 ,T2, T3,T4, EBEST ,EB_VZ ,WS, HERK, ETNR_S,EPREW ,EPREWK,DAKEWV ,DAKEWN,ERNPR, RABGRV ,RABGRN, ERNPRK, MARKEV ,MARKEN, ");
		query.append(" PROZ ,TA_EWV ,TA_EWN ,LW_EWV ,LW_EWN, NT_EWV ,NT_EWN, BSN_LC, MCODE,EKNPR, TNRD, AG,FIRMA ,FILIAL,VCODE,WSUSER, ");
		query.append(" BUK_KONTOV, BUK_KONTON, NPRKAL, RABPRZ, BUVORG, TVKZ, JJMMTT, HHMMSS, DTLBWV, DTLBWN, XMLDAT, XMLTIM, BVDAUF, BVDAUP,  BSNOME, SERVAN )  values ( ");
		query.append("'"+manufacturer+"', 6, "); // HERST,BART
		query.append(" "+ba25_obj.getWarehouseNumber()+", "); // VFNR
		query.append("'"+ba25_obj.getDeliveryNoteNo()+"' ,"); //BELNR
		query.append("'   ' ,"); //POSNR
		query.append("'"+kb+"', "+"'"+etnr+"', "+"'"+es1+"', "+"'"+es2+"', ");  //KB ,ETNR ,ES1 ,ES2

		String mengaValue = ba25_obj.getBookingAmount();
		if(mengaValue!=null && !mengaValue.contains(".")) {
			mengaValue = mengaValue+".0";
		}
		//Format – SPACES(2)+ Input Menge 9(5)v9 + EPREIS 9(5)v9(2) where EPREIS = ip EPR  + calculated DAK (5,4) + calculated EKNPR (5,2)
		query.append("'  "+convertDecimalValue(mengaValue, 5, 1)+convertDecimalValue(ba25_obj.getInputListPrice(), 5, 2)+StringUtils.rightPad(" ", 10)+"");//T1
		query.append(convertDecimalValue(String.valueOf(calculatedDAK), 5, 4)+convertDecimalValue(decimalformat_fixtwodigit.format(Double.valueOf(calculatedNetPrice)), 5, 2)+"',");  //T1
		query.append("'"+StringUtils.leftPad(ba25_obj.getActivityType(), 3, "0")+"', "); //  T2 (LEIART)
		query.append("'000000', "); //  T3
		query.append("'"+StringUtils.rightPad(convertDecimalValue(rabSatz, 2, 2), 18, " ")+"00', ");  //T4 (Rabsatz)

		String currentStock = String.valueOf(Double.parseDouble(ba25_obj.getCurrentStock()) + Double.parseDouble(ba25_obj.getBookingAmount()));
		query.append("'"+convertDecimalValue(currentStock, 5, 1)+"', "); //EBEST  

		if(Double.parseDouble(ba25_obj.getBookingAmount()) >= 0) {
			query.append("'+', "); // EB_VZ  
		}else {
			query.append("'-', "); // EB_VZ  
		}
		query.append(" '--', ");  // WS
		query.append(" 'BSN5BA', ");  // HERK
		query.append("'"+ba25_obj.getSortingFormatPartNumber()+ "', "); // ETNR_S
		query.append("'"+StringUtils.leftPad(convertDecimalValue(ba25_obj.getListPrice(), 5, 2), 7, "0") + "', 'N', "); //  EPREW ,EPREWK

		query.append("'"+StringUtils.leftPad(convertDecimalValue(ba25_obj.getAverageNetPrice(), 5, 4), 9, "0")+ "', "); //DAKEWV
		query.append("'"+StringUtils.leftPad(convertDecimalValue(String.valueOf(calculatedDAK), 5, 4), 9, "0")+ "', "); //DAKEWN

		query.append("'"+StringUtils.left(convertDecimalValue(decimalformat_fixtwodigit.format(Double.valueOf(calculatedNetPrice)), 5, 2),7)+"', "); //ERNPR	
		
		query.append("'"+ba25_obj.getDiscountGroup()+"', "); //RABGRV
		
		if(ba25_obj.getSpecialDiscount().equals(rabSatz)) {
			query.append("' ', "); //RABGRN
			if(ba25_obj.getDiscountGroup() != null && ba25_obj.getDiscountGroup().substring(0, 2) != " ") {
				query.append("'J', ");//ERNPRK
			}
		}
		else {
			query.append("'"+ba25_obj.getDiscountGroup().substring(0, 2) + "', "); //RABGRN
			query.append("'N', ");//ERNPRK
		}

		query.append("'"+ba25_obj.getOemBrand() + "', '"+ba25_obj.getOemBrand() + "', ");//MARKEV ,MARKEN 

		query.append("'"+StringUtils.leftPad(convertDecimalValue(rabSatz, 2, 2), 5, "0")+"', ");//PROZ

		query.append("'"+StringUtils.leftPad(ba25_obj.getPartsIndikator(), 2, "0") +"', '"+ StringUtils.leftPad(ba25_obj.getPartsIndikator(), 2, "0") +"', "); //TA_EWV ,TA_EWN 
		query.append("'"+StringUtils.leftPad(ba25_obj.getDeliverIndicator(), 2, "0") +"', '"+ StringUtils.leftPad(ba25_obj.getDeliverIndicator(), 2, "0") +"', "); //LW_EWV ,LW_EWN

		query.append("'"+StringUtils.left(convertDecimalValue(ba25_obj.getLastPurchasePrice(), 5, 2),7)+ "', ");//NT_EWV
		query.append("'"+StringUtils.left(convertDecimalValue(calculatedNetPrice, 5, 2),7)+ "', ");//NT_EWN

		query.append("'"+ba25_obj.getSupplierNumber()+"', "); //BSN_LC

		if(ba25_obj.getMarketingCode().trim().equalsIgnoreCase("") && manufacturer.equalsIgnoreCase("DCAG")){
			query.append("'D00000' ,"); //MCODE
		}else if(ba25_obj.getMarketingCode().trim().equalsIgnoreCase("") && !manufacturer.equalsIgnoreCase("DCAG")){
			query.append("'X00000' ,"); //MCODE
		}else{
			query.append("'"+ba25_obj.getMarketingCode()+"' ,");
		}

		query.append(0.00+", "); //EKNPR
		query.append("'"+ba25_obj.getPrintingFormatPartNumber()+"', "); // TNR Druck format

		query.append("'6',"); //AG
		query.append("'"+StringUtils.leftPad(companyId, 2, "0")+"', '"+StringUtils.leftPad(agencyId, 2, "0")+"', "); //FIRMA ,FILIAL
		query.append("'6',"); //VCODE
		query.append("'"+loginUserName+"' ,"); // WSUSER
		query.append(" "+bookingAccount+"  ,"); // BUK_KONTOV
		query.append(" "+bookingAccount+" ,"); // BUK_KONTON

		query.append(ba25_obj.getPreviousPurchasePrice()+", "); // NPRKAL

		if(ba25_obj.getSpecialDiscount() != null && Double.parseDouble(ba25_obj.getSpecialDiscount()) > 0) {
			query.append(""+Double.parseDouble(ba25_obj.getSpecialDiscount())+", "); // RABPRZ
		}
		else {
			query.append("0.00, "); // RABPRZ
		}

		query.append("3, "); // BUVORG
		query.append("' ' , "); // TVKZ

		query.append("'"+getCurrentDateInYYMMDD()+"' ,"); // JJMMTT
		query.append("'"+getCurrentTime()+"' ,"); // HHMMSS
		query.append(ba25_obj.getPartMovementDate()+" ,"); // DTLBWV
		query.append(getCurrentDate()+" ,"); // DTLBWN

		query.append(" '000000', '000000', '00000', '00000', '0000000' , '000000' )  "); // HHMMSS

		return query.toString();
	}
	
	
	private String insertValueInElfsL_BA06(BusinessCases25DTO ba25_obj, String dataLibrary, String satzart, String partsIndikator, 
			String calculatedNetPrice, Double calculatedDAK) {
		log.info("Inside insertValueInElfsL method of BusinessCasesImpl");
		
		String manufacturer = ba25_obj.getManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING : ba25_obj.getManufacturer();
		
		StringBuilder query = new StringBuilder("select APOS   FROM ").append(dataLibrary).append(".E_LFSL WHERE  Herst= '").append(manufacturer).append("' AND LNR=");
		query.append(ba25_obj.getWarehouseNumber()).append(" AND NRX =  '").append(ba25_obj.getDeliveryNoteNo()).append("' AND BENDL=").append(ba25_obj.getSupplierNumber());
		query.append(" AND AUFTRAG = 0 AND UPOS = 99 order by APOS desc fetch first 1 rows only ");
		
		int apos = 0;
		List<BADeliveryNotes> BADeliveryNotesList = dbServiceRepository.getResultUsingQuery(BADeliveryNotes.class, query.toString(), true);
		
		if(BADeliveryNotesList!=null && !BADeliveryNotesList.isEmpty()){
			apos = BADeliveryNotesList.get(0).getApos().intValue() + 1;
		}
		
		double zubsn450 = Double.parseDouble(ba25_obj.getBookingAmount());
		double wertB = zubsn450  * Double.parseDouble(ba25_obj.getInputListPrice());
		double wertN_06 = 0.00;
		
		String updated_ERNPR = ba25_obj.getPreviousPurchasePrice() != null ? ba25_obj.getPreviousPurchasePrice() : "0.00";
		
		if(ba25_obj.getNetShoppingPrice() != null && Double.parseDouble(ba25_obj.getNetShoppingPrice()) > 0) {
			updated_ERNPR = ba25_obj.getNetShoppingPrice();
		}
		else if(ba25_obj.getSpecialDiscount() != null && Double.parseDouble(ba25_obj.getSpecialDiscount()) > 0) {
			updated_ERNPR =  "0.00";
		}
		
		double wertN = zubsn450  * Double.parseDouble(updated_ERNPR);
		
		StringBuilder insertQuery = new StringBuilder("INSERT INTO ").append(dataLibrary).append(".E_LFSL( HERST,LNR,BENDL, NRX,AA, AUFTRAG, APOS, UPOS, ZUMENG, ZUBSN450, EPREIS, ");
		insertQuery.append(" NEPREIS, MARKE, RABGR, RABSATZ, WERTB, WERTN, TA, ERNPR, EKNPR, SONDER_RAB, TNR, DAK, RECDAT, RECTIME, PROGRAMM, UPDDAT, UPDTIME ) VALUES ( "); 
		insertQuery.append("'"+manufacturer+"',"); //HERST
		insertQuery.append(ba25_obj.getWarehouseNumber()).append(",");//LNR
		insertQuery.append(ba25_obj.getSupplierNumber()).append(", '");//BENDL
		insertQuery.append(ba25_obj.getDeliveryNoteNo()).append("', ");//NRX
		insertQuery.append("0 ,");//AA
		insertQuery.append("0 ,");//AUFTRAG
		insertQuery.append(apos).append(", ");//APOS
		insertQuery.append("99 , ");//UPOS
		insertQuery.append(zubsn450).append(", ");//ZUMENG
		insertQuery.append(zubsn450).append(", ");//ZUBSN450
		insertQuery.append(ba25_obj.getInputListPrice()).append(", ");//EPREIS
		
		if(Double.parseDouble(updated_ERNPR) > 0) {
			insertQuery.append(Double.parseDouble(updated_ERNPR)).append(", "); //NEPREIS
		}
		else {
			Double rab = 0.00;
			if(Double.parseDouble(ba25_obj.getSpecialDiscount()) > 0) {
				rab = Double.parseDouble(ba25_obj.getInputListPrice()) * (Double.parseDouble(ba25_obj.getSpecialDiscount())/100); // Z-RAB ROUNDED = Ip EPR * ip SORAB
			}
			else {
				rab = Double.parseDouble(ba25_obj.getInputListPrice()) * (Double.parseDouble(satzart)/100); // Z-RAB ROUNDED = Ip EPR * E_RAB.SATZ
			}
			Double nepreis = Double.parseDouble(ba25_obj.getInputListPrice()) - rab;   // NEPREIS ROUNDED = ip EPR - Z-RAB
		    wertN_06 = zubsn450 * nepreis ;
			insertQuery.append(decimalformat_fixtwodigit.format(nepreis)).append(", "); //NEPREIS
		}
		
		insertQuery.append("'"+ba25_obj.getOemBrand()).append("', ");//MARKE
		
		if(manufacturer.equalsIgnoreCase(RestInputConstants.DCAG_STRING)) {
			if(ba25_obj.getDiscountGroup()!=null && !StringUtils.isNumeric(ba25_obj.getDiscountGroup())){
				insertQuery.append("'0', "); //RABGR
			}
			else {
				insertQuery.append("'"+ba25_obj.getDiscountGroup()).append("', "); //RABGR
			}
		}
		else {
			insertQuery.append("'"+ba25_obj.getDiscountGroup()).append("', "); //RABGR
		}
		
		
		insertQuery.append(satzart).append(", ");//RABSATZ
		insertQuery.append(decimalformat_fixtwodigit.format(wertB)).append(", "); //WERTB
		
		if(Double.parseDouble(updated_ERNPR) > 0) {
			insertQuery.append(wertN).append(", "); //WERTN
		}
		else {
			insertQuery.append(wertN_06).append(", "); //WERTN
		}
		
		insertQuery.append(ba25_obj.getPartsIndikator()).append(", ");//TA
		
		insertQuery.append(updated_ERNPR).append(", "); //ERNPR
		
		if(ba25_obj.getNetShoppingPrice() != null && Double.parseDouble(ba25_obj.getNetShoppingPrice()) > 0) {
			insertQuery.append(ba25_obj.getNetShoppingPrice()).append(", "); //EKNPR
		}
		else {
			insertQuery.append("0.00, ");//EKNPR
		}
		
		insertQuery.append(Double.parseDouble(ba25_obj.getSpecialDiscount())).append(" ,");//SONDER_RAB
		
		insertQuery.append("'"+ba25_obj.getPartNumber()+"',"); //TNR
		
		insertQuery.append(calculatedDAK).append(", "); //DAK
				
		insertQuery.append(getCurrentDate()).append(", ");//RECDAT
		insertQuery.append(getCurrentTime()).append(", ");//RECTIME
		insertQuery.append("'BSN506', 0, 0 ) "); //TNR , UPDDAT, UPDTIME 
		
		
		return insertQuery.toString();
	}
	
	
	/**
	 * This method is used for BA50
	 * 
	 * The BA50 is used to delete the Part.
	 */
	@Override
	public Map<String, Boolean> newJavaImplementation_BA50(FinalizationsBA_DTO  ba50_obj, String dataLibrary, String schema,
			String companyId, String agencyId, String loginUserName ){
		log.info("Inside newJavaImplementation_BA50 method of BusinessCasesImpl");

		Map<String, Boolean> programOutput = new HashMap<String, Boolean>();
		
		programOutput.put("isBA50ExecutedSuccessfully", false);

		try(Connection con = dbServiceRepository.getConnectionObject();
				Statement stmt = con.createStatement();){

			//con.setAutoCommit(false);
			String manufacturer = ba50_obj.getManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING : ba50_obj.getManufacturer();
			
			StringBuilder query_1 = new StringBuilder(" SELECT TNR, BENEN, BESAUS, AKTBES, EPR, DAK, SA, TMARKE, RG, MC, LEKPR, TA, LIWERK, ");
			query_1.append("EKNPR, NPREIS, TNRS, TNRD, FIRMA, FILIAL, DTLBEW, LOPA FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE ");
			query_1.append("HERST = ").append("'"+manufacturer+"'");
			query_1.append(" AND LNR = ").append(ba50_obj.getWarehouseNumber());
			query_1.append(" AND TNR = ").append("'"+ba50_obj.getPartNumber()+"'");

			List<PartDetails> finalizationsPartBAList = dbServiceRepository.getResultUsingQuery(PartDetails.class, query_1.toString(), true);
			
			if(finalizationsPartBAList != null && !finalizationsPartBAList.isEmpty()) {

				String satzart = getSatzart( dataLibrary, manufacturer, finalizationsPartBAList.get(0).getOemBrand(), finalizationsPartBAList.get(0).getDiscountGroup() );

				String cpsdatQuery = insertValuesInCPSDAT_BA50( dataLibrary, manufacturer, ba50_obj.getWarehouseNumber(), finalizationsPartBAList, satzart, 
						companyId, agencyId, loginUserName );

				String etstloeQuery = insertValueInETSTLOE_BA50(ba50_obj, dataLibrary, manufacturer);
				
				String mzliQuery = deleteValueFromMANZLI_BA50(ba50_obj, dataLibrary, schema, manufacturer);

				String etstammQuery = deleteValueFromETSTAMM_BA50(ba50_obj, dataLibrary, manufacturer);

				String elagoQuery = deleteValueFromELAGO_BA50(ba50_obj, dataLibrary, manufacturer);

				stmt.addBatch(cpsdatQuery.toString());
				log.info("Insert CPSDAT Query  :"+cpsdatQuery.toString());

				stmt.addBatch(etstloeQuery.toString());
				log.info("Insert ETSTLOE Query  :"+etstloeQuery.toString());
				
				stmt.addBatch(mzliQuery.toString());
				log.info("Delete EIN_MANZLI Query :"+mzliQuery.toString());

				stmt.addBatch(etstammQuery.toString());
				log.info("Delete E_ETSTAMM Query  :"+etstammQuery.toString());

				stmt.addBatch(elagoQuery.toString());
				log.info("Delete E_LAGO Query:"+elagoQuery.toString());

				dbServiceRepository.insertResultUsingBatchQuery(stmt);

				con.commit();
				programOutput.put("isBA50ExecutedSuccessfully", true);
			}
		}
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY,"BA50"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY,"BA50"), exception);
			throw exception;
		}

		return programOutput;
	}
	
	
	/**
	 * This method is used to create insert query for CPSDAT
	 * @return query
	 */
	private String insertValuesInCPSDAT_BA50(String dataLibrary, String manufacturer, String warehouseNumber, List<PartDetails> finalizationsPartBAList, 
			String satzart, String companyId, String agencyId, String loginUserName) {

		log.info("Inside insertValuesInCPSDAT_BA50 method of BusinessCasesImpl");

		String kb = "";
		String etnr ="";
		String es1 = "";
		String es2 = "";

		if(finalizationsPartBAList.get(0).getPartNumber() != null) {
			String partNo = StringUtils.rightPad(finalizationsPartBAList.get(0).getPartNumber(), 19, " ");

			kb = partNo.substring(0,1);
			etnr = partNo.substring(1,13);
			es1 = partNo.substring(13,15);
			es2 = partNo.substring(15,19);
		}

		StringBuilder query = new StringBuilder(" INSERT INTO ").append(dataLibrary).append(".E_CPSDAT ");  //KDNR,FZGKZ,FZNR,FART,MITRAB
		query.append(" (HERST, BART, VFNR, BELNR, POSNR, KB, ETNR, ES1, ES2, T1, T2, T3, T4, EBEST, EB_VZ, WS, HERK, NW1_EW, NW2_EW, ETNR_S, EPREW, EPREWK, DAKEWV, DAKEWN, ERNPR, ERNPRK, RABGRV, RABGRN, MARKEV, MARKEN, ");
		query.append(" PROZ, TA_EWV, TA_EWN, LW_EWV, LW_EWN, NT_EWV, NT_EWN, BSN_LC, EKNPR, TNRD, FIRMA, FILIAL, WSUSER, ");
		query.append(" NPRKAL, RABPRZ, JJMMTT, HHMMSS, XMLDAT, XMLTIM, BVDAUF, BVDAUP, BSNOME, SERVAN )  values ( ");
		query.append("'"+manufacturer+"', 50, "); // HERST, BART
		query.append(" "+warehouseNumber+", "); // VFNR
		query.append("'   ', '   ' , "); //BELNR, POSNR
		query.append("'"+kb+"', "+"'"+etnr+"', "+"'"+es1+"', "+"'"+es2+"', ");  //KB ,ETNR ,ES1 ,ES2

		query.append("'  ', ");   //T1
		query.append("'000', ");  //T2
		
		String storageLoc = finalizationsPartBAList.get(0).getStorageLocation() != null ? finalizationsPartBAList.get(0).getStorageLocation():" ";		
		query.append("'000000"+ storageLoc +"', "); //  T3
		query.append("'0000"+StringUtils.rightPad("", 14, " ")+"00', ");  //T4

		query.append("'000000', "); //EBEST
		query.append("'+', "); // EB_VZ  
		
		query.append(" '--', ");  // WS
		query.append(" 'BSN5BA', ");  // HERK
		query.append(" 'L', ");  // NW1_EW
		query.append("'"+ finalizationsPartBAList.get(0).getStorageIndikator()+ "', ");  // NW2_EW
		query.append("'"+finalizationsPartBAList.get(0).getSortingFormatPartNumber()+ "', "); // ETNR_S
		query.append("'"+StringUtils.leftPad(convertDecimalValue(String.valueOf(finalizationsPartBAList.get(0).getPurchasePrice()), 5, 2), 7, "0") + "', 'N', "); //  EPREW ,EPREWK

		query.append("'"+StringUtils.leftPad(convertDecimalValue(String.valueOf(finalizationsPartBAList.get(0).getAverageNetPrice()), 5, 4), 9, "0")+ "', "); //DAKEWV
		query.append("'"+StringUtils.leftPad(convertDecimalValue(String.valueOf(finalizationsPartBAList.get(0).getAverageNetPrice()), 5, 4), 9, "0")+ "', "); //DAKEWN
		
		Double purchasePrice = finalizationsPartBAList.get(0).getPurchasePrice()!=null ? finalizationsPartBAList.get(0).getPurchasePrice().doubleValue() : 0.00;
		Double satz = satzart != null ? Double.parseDouble(satzart) : 0.00;
		
		Double ernprValue = purchasePrice * (1 - (satz/100));
		
		if(ernprValue == 0) {
			query.append("'"+StringUtils.left(convertDecimalValue(decimalformat_fixtwodigit.format(purchasePrice), 5, 2), 7)+"', "); //ERNPR
		}
		else {
			query.append("'"+StringUtils.left(convertDecimalValue(decimalformat_fixtwodigit.format(ernprValue), 5, 2), 7)+"', "); //ERNPR
		}
		
		query.append("'N', "); //ERNPRK
		
		if(manufacturer.equalsIgnoreCase(RestInputConstants.DCAG_STRING)) {
			query.append("'"+StringUtils.leftPad(finalizationsPartBAList.get(0).getDiscountGroup(), 2, "0") + "', "); //RABGRV		
			query.append("'"+StringUtils.leftPad(finalizationsPartBAList.get(0).getDiscountGroup(), 2, "0") + "', "); //RABGRN
		}
		else {
			query.append("'"+StringUtils.leftPad(finalizationsPartBAList.get(0).getDiscountGroup(), 5, "0") + "', "); //RABGRV		
			query.append("'"+StringUtils.leftPad(finalizationsPartBAList.get(0).getDiscountGroup(), 5, "0") + "', "); //RABGRN
		}
			
		query.append("'"+finalizationsPartBAList.get(0).getOemBrand() + "', '"+finalizationsPartBAList.get(0).getOemBrand() + "', ");//MARKEV ,MARKEN 

		query.append("'"+StringUtils.leftPad(convertDecimalValue(satzart, 2, 2), 5, "0")+"', ");//PROZ

		query.append("'"+StringUtils.leftPad(String.valueOf(finalizationsPartBAList.get(0).getPartsIndikator()), 2, "0") +"', "); //TA_EWV
		query.append("'"+ StringUtils.leftPad(String.valueOf(finalizationsPartBAList.get(0).getPartsIndikator()), 2, "0") +"', "); //TA_EWN 
		query.append("'"+StringUtils.leftPad(String.valueOf(finalizationsPartBAList.get(0).getDeliverIndicator()), 2, "0") +"', "); //LW_EWV
		query.append("'"+ StringUtils.leftPad(String.valueOf(finalizationsPartBAList.get(0).getDeliverIndicator()), 2, "0") +"', "); //LW_EWN

		query.append("'"+StringUtils.leftPad("0", 7, "0")+ "', ");//NT_EWV
		query.append("'"+StringUtils.leftPad("0", 7, "0")+ "', ");//NT_EWN

		query.append("'"+StringUtils.leftPad("0", 5, "0")+"', "); //BSN_LC

		query.append(0.00+", "); //EKNPR
		query.append("'"+finalizationsPartBAList.get(0).getPrintingFormatPartNumber()+"', "); // TNRD

		query.append("'"+StringUtils.leftPad(companyId, 2, "0")+"', '"+StringUtils.leftPad(agencyId, 2, "0")+"', "); //FIRMA ,FILIAL
		query.append("'"+loginUserName+"' ,"); // WSUSER

		query.append(finalizationsPartBAList.get(0).getPreviousPurchasePrice()+", "); // NPRKAL

		query.append("0.00, "); // RABPRZ

		query.append("'"+getCurrentDateInYYMMDD()+"' ,"); // JJMMTT
		query.append("'"+getCurrentTime()+"' ,"); // HHMMSS
		
		query.append(" '000000', '000000', '00000', '00000', '0000000', '000000' )  "); // XMLDAT, XMLTIM, BVDAUF, BVDAUP, BSNOME, SERVAN

		return query.toString();
	}
	
	
	/**
	 * This method is used to create insert query for E_ETSTLOE
	 * @return query
	 */
	private String insertValueInETSTLOE_BA50(FinalizationsBA_DTO ba50_obj, String dataLibrary, String manufacturer) {

		log.info("Inside insertValueInETSTLOE_BA50 method of BusinessCasesImpl");

		StringBuilder query = new StringBuilder(" INSERT INTO ").append(dataLibrary).append(".E_ETSTLOE ");
		query.append("(HERST, TIMESTAMP, TNR,LNR,SA,TA,LEIART,BENEN,LOPA,LIWERK,EPR,DAK,EKNPR,PFAND,TRUWER,LEKPR,ENTSOW,ZPREIS,APREIS,NPREIS,ENTSOP,RG,TMARKE,MWST,SKL,MINBES, "); 
		query.append("MAXBES,SIBES,IVZME,AKTBES,LIVBES,RESBES,VKLFMS,VKLJ1S,VKLJ2S,VKLJ3S,VKLJ4S,VKLJ5S,VKLJ6S,VKLJ7S,VKLJ8S,VKLJ9S,VKLJAS,VKLJBS,VKLJCS,VKAVGS,"); 
		query.append("VKVJ1S,VKVJ2S,VKVJ3S,VKVJ4S,VKVJ5S,VKVJ6S,VKVJ7S,VKVJ8S,VKVJ9S,VKVJAS,VKVJBS,VKVJCS,VKSVJS,VKLFJS,ZULFMS,ZULM1S, "); 
		query.append("ZULM2S,ZULM3S,ZULM4S,ZULM5S,ZULM6S,ZULM7S,ZULM8S,ZULM9S,ZULMAS,ZULMBS,ZULMCS,ZUAVGS,ZUSVJS,ZULFJS,VKLFMW,VKLJ1W,VKLJ2W,VKLJ3W,VKLJ4W, ");
		query.append("VKLJ5W,VKLJ6W,VKLJ7W,VKLJ8W,VKLJ9W,VKLJAW,VKLJBW,VKLJCW,VKAVGW,VKVJ1W,VKVJ2W,VKVJ3W,VKVJ4W,VKVJ5W,VKVJ6W,VKVJ7W,VKVJ8W,VKVJ9W,VKVJAW, "); 
		query.append("VKVJBW,VKVJCW,VKSVJW,VKLFJW,ZULFMW,ZULM1W,ZULM2W,ZULM3W,ZULM4W,ZULM5W,ZULM6W,ZULM7W,ZULM8W,ZULM9W,ZULMAW,ZULMBW,ZULMCW,ZUAVGW,ZUSVJW, ");
		query.append("ZULFJW,BESAUS,MIBESM,KDRUE,LIFRUE,ANZVK,NFMGE,ZUMGE,ANZNFS,NFLJ01,NFLJ02,NFLJ03,NFLJ04,NFLJ05,NFLJ06,NFLJ07,NFLJ08,NFLJ09,NFLJ10, ");
		query.append("NFLJ11,NFLJ12,NFVJ01,NFVJ02,NFVJ03,NFVJ04,NFVJ05,NFVJ06,NFVJ07,NFVJ08,NFVJ09,NFVJ10,NFVJ11,NFVJ12,TVORMG,VORHS,BESVER,KSPKZ,NC,PRKZ, ");
		query.append("LKZ,DISPO,IVKZ,BARCDE,ABSCH,KDABS,WINTER,IVER,MODUS,DTLABG,DTLZUG,DTANLA,DTLBES,DTVERF,GPRDAT,DTLBEW,DTINV,DATLDI,DATFCC,MC,TNRS,TNRD,");
		query.append("TNRV,TNRN,TNRM,TNRMLN,TNRH,TCODE,VERP1,VERP2,LKZ2,TIDENT,DRTKZ,GFKZ,TAUKZ,SPCDE,GEWICH,TVOL,TRSPER,KZEINS,THWSNR,KZBESZ,ME,ZAEGRU, ");
		query.append("KZBEVO,KZRUCK,KZMFLO,MEHEK,MEHVK,PRAFKZ,FIRMA,FILIAL,KOMPT,VERART,SPKZV,LIEFNR,TVOLN, INTVKZ,IVBGJS,DAKGJS ) ");
		query.append("Select ");
		query.append("HERST, TO_CHAR(CURRENT TIMESTAMP,'YYYYMMDDHH24MISSFF'), TNR,LNR,SA,TA,LEIART,BENEN,LOPA,LIWERK,EPR,DAK,EKNPR,PFAND,TRUWER,LEKPR,ENTSOW,ZPREIS,APREIS,NPREIS,ENTSOP,RG,TMARKE,MWST,SKL,MINBES, "); 
		query.append("MAXBES,SIBES,IVZME,AKTBES,LIVBES,RESBES,VKLFMS,VKLJ1S,VKLJ2S,VKLJ3S,VKLJ4S,VKLJ5S,VKLJ6S,VKLJ7S,VKLJ8S,VKLJ9S,VKLJAS,VKLJBS,VKLJCS,VKAVGS,"); 
		query.append("VKVJ1S,VKVJ2S,VKVJ3S,VKVJ4S,VKVJ5S,VKVJ6S,VKVJ7S,VKVJ8S,VKVJ9S,VKVJAS,VKVJBS,VKVJCS,VKSVJS,VKLFJS,ZULFMS,ZULM1S, "); 
		query.append("ZULM2S,ZULM3S,ZULM4S,ZULM5S,ZULM6S,ZULM7S,ZULM8S,ZULM9S,ZULMAS,ZULMBS,ZULMCS,ZUAVGS,ZUSVJS,ZULFJS,VKLFMW,VKLJ1W,VKLJ2W,VKLJ3W,VKLJ4W, ");
		query.append("VKLJ5W,VKLJ6W,VKLJ7W,VKLJ8W,VKLJ9W,VKLJAW,VKLJBW,VKLJCW,VKAVGW,VKVJ1W,VKVJ2W,VKVJ3W,VKVJ4W,VKVJ5W,VKVJ6W,VKVJ7W,VKVJ8W,VKVJ9W,VKVJAW, "); 
		query.append("VKVJBW,VKVJCW,VKSVJW,VKLFJW,ZULFMW,ZULM1W,ZULM2W,ZULM3W,ZULM4W,ZULM5W,ZULM6W,ZULM7W,ZULM8W,ZULM9W,ZULMAW,ZULMBW,ZULMCW,ZUAVGW,ZUSVJW, ");
		query.append("ZULFJW,BESAUS,MIBESM,KDRUE,LIFRUE,ANZVK,NFMGE,ZUMGE,ANZNFS,NFLJ01,NFLJ02,NFLJ03,NFLJ04,NFLJ05,NFLJ06,NFLJ07,NFLJ08,NFLJ09,NFLJ10, ");
		query.append("NFLJ11,NFLJ12,NFVJ01,NFVJ02,NFVJ03,NFVJ04,NFVJ05,NFVJ06,NFVJ07,NFVJ08,NFVJ09,NFVJ10,NFVJ11,NFVJ12,TVORMG,VORHS,BESVER,KSPKZ,NC,PRKZ, ");
		query.append("LKZ,DISPO,IVKZ,BARCDE,ABSCH,KDABS,WINTER,IVER,MODUS,DTLABG,DTLZUG,DTANLA,DTLBES,DTVERF,GPRDAT,DTLBEW,DTINV,DATLDI,DATFCC,MC,TNRS,TNRD,");
		query.append("TNRV,TNRN,TNRM,TNRMLN,TNRH,TCODE,VERP1,VERP2,LKZ2,TIDENT,DRTKZ,GFKZ,TAUKZ,SPCDE,GEWICH,TVOL,TRSPER,KZEINS,THWSNR,KZBESZ,ME,ZAEGRU, ");
		query.append("KZBEVO,KZRUCK,KZMFLO,MEHEK,MEHVK,PRAFKZ,FIRMA,FILIAL,KOMPT,VERART,SPKZV,LIEFNR,TVOLN, INTVKZ,IVBGJS,DAKGJS ");
		query.append(" from ").append(dataLibrary).append(".E_ETSTAMM  Where LNR=").append(ba50_obj.getWarehouseNumber()).append(" and HERST='");
		query.append(manufacturer).append("' and TNR='").append(ba50_obj.getPartNumber()).append("' ");

		return query.toString();
	}
	
	
	/**
	 * This method is used to create Delete query for EIN_MANZLI
	 * @return query
	 */
	private String deleteValueFromMANZLI_BA50(FinalizationsBA_DTO ba50_obj, String dataLibrary, String schema, String manufacturer) {

		log.info("Inside deleteValueFromMANZLI_BA50 method of BusinessCasesImpl");

		StringBuilder query = new StringBuilder("DELETE from ").append(schema).append(".EIN_MANZLI  Where ");  
		query.append(" MZL_LNR = ").append(ba50_obj.getWarehouseNumber());
		query.append(" AND MZL_HERST = '").append(manufacturer).append("'");
		query.append(" AND MZL_TNR = '").append(ba50_obj.getPartNumber()).append("'");
		query.append(" AND  EXISTS (SELECT * FROM ").append(dataLibrary).append(".E_ETSTAMM ET WHERE DIGITS(ET.LNR) = MZL_LNR AND ET.HERST = MZL_HERST ");
		query.append(" AND ET.TNR = MZL_TNR AND ET.TMARKE LIKE '__') ");

		return query.toString();
	}
	
	
	/**
	 * This method is used to create Delete query for E_ETSTAMM
	 * @return query
	 */
	private String deleteValueFromETSTAMM_BA50(FinalizationsBA_DTO ba50_obj, String dataLibrary, String manufacturer) {

		log.info("Inside deleteValueFromETSTAMM_BA50 method of BusinessCasesImpl");
		
		StringBuilder query = new StringBuilder("DELETE from ").append(dataLibrary).append(".E_ETSTAMM  where ");  
		query.append("HERST = ").append("'"+manufacturer+"'");
		query.append(" AND LNR = ").append(ba50_obj.getWarehouseNumber());
		query.append(" AND TNR = ").append("'"+ba50_obj.getPartNumber()+"'");
		
		return query.toString();
	}
	
	
	/**
	 * This method is used to create Delete query for E_LAGO
	 * @return query
	 */
	private String deleteValueFromELAGO_BA50(FinalizationsBA_DTO ba50_obj, String dataLibrary, String manufacturer) {

		log.info("Inside deleteValueFromELAGO_BA50 method of BusinessCasesImpl");
		
		StringBuilder query = new StringBuilder("DELETE from ").append(dataLibrary).append(".E_LAGO  where ");  
		query.append("HERST = ").append("'"+manufacturer+"'");
		query.append(" AND LNR = ").append(ba50_obj.getWarehouseNumber());
		query.append(" AND TNR = ").append("'"+ba50_obj.getPartNumber()+"'");
		
		return query.toString();
	}
	
	
	@Override
	public Map<String, Boolean> newJavaImplementation_BA17(AccessesBA_DTO accessesBA, String dataLibrary,String schema,
			String companyId, String agencyId, String loginUserName ){
		log.info("Inside newJavaImplementation_BA17 method of BusinessCasesImpl");
	
		Map<String, Boolean> programOutput = new HashMap<String, Boolean>();
		programOutput.put("isBA17ExecutedSuccessfully", false);
		
		try(Connection con = dbServiceRepository.getConnectionObject();
				Statement stmt = con.createStatement();){
			 
			  if(accessesBA.getWarehouseListForPartCreation() == null || accessesBA.getWarehouseListForPartCreation().trim().isEmpty() ) {
			  AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.BUSINESS_CASES_17_01FAILED_MSG_KEY));
			  log.error(messageService.getReadableMessage(ExceptionMessages.BUSINESS_CASES_17_01FAILED_MSG_KEY), exception); 
			  throw exception; 
			  }
			
			 List<String> warehouseListForPartCreation = Stream.of(accessesBA.getWarehouseListForPartCreation().split(",", -1)).collect(Collectors.toList());
			
			//con.setAutoCommit(false);
			String manufacturer = accessesBA.getManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING : accessesBA.getManufacturer();
			
			if(accessesBA.getListPriceWithoutVAT() == null || accessesBA.getListPriceWithoutVAT().trim().isEmpty() || 
					 Double.parseDouble(accessesBA.getListPriceWithoutVAT()) <= 0.00) {
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.PASSWORD_EXPIRED_MSG_KEY, "Listenpresi unplausibel/ungültig" ));
				log.error(messageService.getReadableMessage(ExceptionMessages.PASSWORD_EXPIRED_MSG_KEY,"Listenpresi unplausibel/ungültig"), exception);
				throw exception;
			}
			
		 String generateTNRS = "";
		 String generateTNRD = "";
		 if(!manufacturer.equalsIgnoreCase("DCAG") || (accessesBA.getMarketingCode()!=null && accessesBA.getMarketingCode().length() >=2 
				 && accessesBA.getMarketingCode().substring(0, 2).equalsIgnoreCase("2A") && manufacturer.equalsIgnoreCase("DCAG"))) {
			 generateTNRS = accessesBA.getPartNumber();
		 }else {
			 generateTNRS = 	generateTNRSValueFromTNR(accessesBA.getPartNumber()); 
		 }
		 
		 if(!manufacturer.equalsIgnoreCase("DCAG")){
			 generateTNRD = accessesBA.getPartNumber();
		 }else {
		   generateTNRD =  generateTNRDValueFromTNR(accessesBA.getPartNumber());
		 }
		 
		 String calculatedNetPrice = calculateNetPrice(accessesBA.getDiscountGroupPercentageValue(),accessesBA.getListPriceWithoutVAT() );
		 String calculatedNetPriceWith4Decimal = calculateNetPriceWith4Decimal(accessesBA.getDiscountGroupPercentageValue(),accessesBA.getListPriceWithoutVAT());
		 
		for(String warehouseNo:warehouseListForPartCreation) { 
				accessesBA.setWarehouseNo(warehouseNo);
		//Determine Booking account (BUCHUNGSKONTO)
		String bookingAccount = getBookingAccount(accessesBA.getWarehouseNo(), accessesBA.getPartBrand(),accessesBA.getPartsIndikator(),dataLibrary,schema);
			 
		 String etstammQuery =  insertValuesInETSTAMM(accessesBA,dataLibrary, companyId, agencyId,generateTNRS, generateTNRD, calculatedNetPriceWith4Decimal);
		 
		 stmt.addBatch(etstammQuery);
		 log.info("ETSTAMM Query  :"+etstammQuery);
		 
		 String firstEntry_cpddat = insertValuesInCPSDAT_BA17_First(accessesBA, dataLibrary, calculatedNetPrice,companyId, agencyId, loginUserName,generateTNRS,generateTNRD, calculatedNetPriceWith4Decimal );
		 
		 stmt.addBatch(firstEntry_cpddat);
		 log.info("Query For First Entry in CPSDAT    :"+firstEntry_cpddat);
		 
		 String secondEntry_cpddat = insertValuesInCPSDAT_BA17_Second(accessesBA, dataLibrary, calculatedNetPrice,companyId, agencyId, loginUserName,generateTNRS,generateTNRD,bookingAccount, calculatedNetPriceWith4Decimal );
		
		 stmt.addBatch(secondEntry_cpddat);
		 log.info("Query For Second Entry in CPSDAT    :"+secondEntry_cpddat);
		}
			
			  int[] records = dbServiceRepository.insertResultUsingBatchQuery(stmt);
			  if(records != null){
			  log.info("Total rows Inserted  :"+records.length); 
			  }
			 
			programOutput.put("isBA17ExecutedSuccessfully", true);
			con.commit();
			
		}catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY,"BA17"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY,"BA17"), exception);
			throw exception;
		}

		return programOutput;

	
	}
	

	private String generateTNRSValueFromTNR(String partNumber) {
		String tnrsValue = "";
		if (partNumber != null) {
			String partNo = StringUtils.rightPad(partNumber, 20, " ");
			tnrsValue = partNo.substring(0, 1)+partNo.substring(6, 9) + partNo.substring(11, 13) + partNo.substring(3, 6)
					+ partNo.substring(1, 3) + partNo.substring(9, 11) + partNo.substring(13, 15)
					+ partNo.substring(15, 19);

		}
		return tnrsValue;
	}
	
	private String generateTNRDValueFromTNR(String partNumber) {
		String tnrdValue = "";
		if (partNumber != null) {
			String partNo = StringUtils.rightPad(partNumber, 20, " ");
			tnrdValue = partNo.substring(0, 1) + partNo.substring(1, 3) + StringUtils.rightPad(" ", 1," ")+ partNo.substring(3, 6)+StringUtils.rightPad(" ", 1," ")
					+ partNo.substring(6, 9)+StringUtils.rightPad(" ", 1," ")+partNo.substring(9, 11)+StringUtils.rightPad(" ", 1," ")
					+ partNo.substring(11, 13) + StringUtils.rightPad(" ", 1," ") + partNo.substring(13, 15) + StringUtils.rightPad(" ", 1," ")
					+ partNo.substring(15, 19);

		}
		return tnrdValue;
	}

	private String insertValuesInETSTAMM(AccessesBA_DTO accessesBA,String dataLibrary, String companyId, String agencyId,
			String tnrsValue,String tnrdValue, String calculatedNetPrice) {

		log.info("Inside insertValuesInETSTAMM method of BusinessCasesImpl");
	
		StringBuilder query = new StringBuilder(" INSERT INTO ").append(dataLibrary).append(".E_ETSTAMM  "); 
		query.append(" (HERST,TNR,LNR,SA,TA,LEIART,BENEN,LOPA,LIWERK,EPR,DAK,EKNPR, ");
		query.append(" LEKPR,RG,TMARKE,MWST,KSPKZ ,PRKZ,LKZ,DTANLA,MC,TNRS,TNRD,FIRMA,FILIAL ) VALUES ( ");
		query.append("'"+accessesBA.getManufacturer()+"' ,"); // HERST
		query.append("'"+accessesBA.getPartNumber()+"' ,"); //TNR
		query.append(accessesBA.getWarehouseNo()).append(","); // LNR
		query.append("1").append(","); // SA
		query.append(accessesBA.getPartsIndikator()).append(","); // TA
		query.append(accessesBA.getActivityType()).append(", '"); // LEIART
		query.append(accessesBA.getPartName()).append("' , '' , "); // BENEN , LOPA
		query.append(accessesBA.getDeliverIndicator()).append(","); // LIWERK
		query.append(accessesBA.getListPriceWithoutVAT()).append(","); // EPR
		query.append(calculatedNetPrice).append(","); // DAK
		query.append("0").append(","); // EKNPR
		query.append(calculatedNetPrice).append(", '"); // LEKPR
		query.append(accessesBA.getDiscountGroupValue()).append("', '"); // RG
		query.append(accessesBA.getPartBrand()).append("', '"); // TMARKE
		query.append(accessesBA.getVatRegistrationNumber()).append("', '"); // MWST
		query.append("K").append("', '"); // KSPKZ
		query.append(accessesBA.getPriceIndicator()).append("', '',  "); // PRKZ , LKZ
		query.append(getCurrentDate()).append(", '"); // DTANLA
		query.append(accessesBA.getMarketingCode()).append("', '"); // MC
		query.append(tnrsValue).append("', '"); // TNRS
		query.append(tnrdValue).append("', "); // TNRD
		query.append(companyId).append(", "); // FIRMA
		query.append(agencyId).append(" )"); // FILIAL

		return query.toString();
	}
	
	
	private String insertValuesInCPSDAT_BA17_First(AccessesBA_DTO ba17_obj,String dataLibrary, String calculatedNetPrice,
			 String companyId, String agencyId, String loginUserName,String tnrsValue,String tnrdValue, String calculatedNetPriceWith4Decimal ) {
		
		log.info("Inside insertValuesInCPSDAT_BA17 method of BusinessCasesImpl");
		String manufacturer = ba17_obj.getManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING : ba17_obj.getManufacturer();
		
		String kb = "";
		String etnr ="";
		String es1 = "";
		String es2 = "";
		
			if(ba17_obj.getPartNumber() != null) {
				String partNo = StringUtils.rightPad(ba17_obj.getPartNumber(), 19, " ");
				
				kb = partNo.substring(0,1);
				etnr = partNo.substring(1,13);
				es1 = partNo.substring(13,15);
				es2 = partNo.substring(15,19);
			}
		
			
			StringBuilder query = new StringBuilder(" INSERT INTO ").append(dataLibrary).append(".E_CPSDAT ");
			query.append(" (HERST,BART,VFNR,BELNR,POSNR,KB,ETNR,ES1,ES2,T1,T2,EB_VZ,WS,T3,NW1_EW,NW2_EW,T4, ");
			query.append(" HERK,JJMMTT,HHMMSS,ETNR_S,EPREW,EPREWK,DAKEWV,DAKEWN,ERNPR,ERNPRK,RABGRV,RABGRN, ");
			query.append(" MARKEV,MARKEN,FEPR,GARANT,PROZ,TA_EWV,TA_EWN,LW_EWV,LW_EWN,NT_EWV,NT_EWN,XMLDAT, ");
			query.append(" XMLTIM,MCODE,EKNPR,TNRD,FIRMA,FILIAL,MWSTV,MWSTN,PRKZV,PRKZN,WSUSER,MCODEV,EPRV,");
			query.append(" EPRN,EKNPRV,BUK_KONTOV,BUK_KONTON,LEIARTV,LEIARTN, BVDAUF ,BVDAUP ,BSNOME ,BSN_LC, SERVAN, EBEST )  values ( ");
			query.append("'"+manufacturer+"', 17, "); // HERST,BART
			query.append(" "+ba17_obj.getWarehouseNo()+", '', '000',  "); // VFNR,BELNR,POSNR
			query.append("'"+kb+"', "+"'"+etnr+"', "+"'"+es1+"', "+"'"+es2+"', ");  //KB ,ETNR ,ES1 ,ES2

			//Format – LW(2) + "000000"+EPR(7)+RG(2)+SPACES(2)+LW(2)+SPACES(1)+TA(1)+MARKE(2)+DAKEWV(9)+ERNPR(7)
			String t1Value = StringUtils.leftPad(ba17_obj.getDeliverIndicator(), 2,"0")+"000000"+
			convertDecimalValue(ba17_obj.getListPriceWithoutVAT(), 5, 2)+ba17_obj.getDiscountGroupValue().subSequence(0, 2)+"  "+
			StringUtils.leftPad(ba17_obj.getDeliverIndicator(), 2,"0")+" "+StringUtils.stripStart(ba17_obj.getPartsIndikator(), "0")+
			StringUtils.rightPad(ba17_obj.getPartBrand(), 2," ")+convertDecimalValue(calculatedNetPriceWith4Decimal, 5, 4)+
			convertDecimalValue(calculatedNetPrice, 5, 2);
			
			
			query.append("'"+t1Value+"', '"); // T1  
			query.append("0"+StringUtils.leftPad(ba17_obj.getActivityType(), 2, "0")+"', "); //  T2 
			query.append("'+', '' , "); //  EB_VZ , WS
			query.append(" '000000', "); //  T3
			query.append(" '0', '1', "); //  NW1_EW,NW2_EW
			query.append("'"+StringUtils.rightPad("0000", 18, " ")+"00*', ");  //T4 
			query.append(" 'BSN5BA', ");  // HERK
			query.append("'"+getCurrentDateInYYMMDD()+"' ,"); // JJMMTT
			query.append("'"+getCurrentTime()+"' ,"); // HHMMSS
			query.append("'"+tnrsValue+ "', "); // ETNR_S
			query.append("'"+StringUtils.leftPad(convertDecimalValue(ba17_obj.getListPriceWithoutVAT(), 5, 2), 7, "0") + "', '', "); //  EPREW ,EPREWK
			query.append("'"+StringUtils.leftPad(convertDecimalValue(calculatedNetPriceWith4Decimal, 5, 4), 9, "0")+ "', "); //DAKEWV
			query.append("'"+StringUtils.leftPad(convertDecimalValue(calculatedNetPriceWith4Decimal, 5, 4), 9, "0")+ "', "); //DAKEWN
			query.append("'"+StringUtils.left(convertDecimalValue(calculatedNetPrice, 5, 2),7)+"', 'N' ,");//ERNPR, ERNPRK
			query.append("'"+ba17_obj.getDiscountGroupValue() + "', '"+ba17_obj.getDiscountGroupValue() + "', ");//RABGRV ,RABGRN  
			query.append("'"+ba17_obj.getPartBrand() + "', '"+ba17_obj.getPartBrand() + "', ");//MARKEV ,MARKEN  
			query.append(" '', '', ");//FEPR ,GARANT 
			query.append("'"+StringUtils.leftPad(convertDecimalValue(ba17_obj.getDiscountGroupPercentageValue(), 2, 2), 5, "0")+"', ");//PROZ
			query.append("'"+StringUtils.leftPad(ba17_obj.getPartsIndikator(), 2, "0") +"', '"+ StringUtils.leftPad(ba17_obj.getPartsIndikator(), 2, "0") +"', "); //TA_EWV ,TA_EWN 
			query.append("'"+StringUtils.leftPad(ba17_obj.getDeliverIndicator(), 2, "0") +"', '"+ StringUtils.leftPad(ba17_obj.getDeliverIndicator(), 2, "0") +"', "); //LW_EWV ,LW_EWN
			query.append("'"+StringUtils.left(convertDecimalValue(calculatedNetPrice, 5, 2),7)+ "', ");//NT_EWV
			query.append("'"+StringUtils.left(convertDecimalValue(calculatedNetPrice, 5, 2),7)+ "', ");//NT_EWN
			query.append("'000000', '000000', "); //XMLDAT ,XMLTIM 
			query.append("'"+ba17_obj.getMarketingCode()+"' ,"); //MCODE
			query.append("0, "); //EKNPR
			query.append("'"+tnrdValue+"', "); //TNRD
			query.append("'"+StringUtils.leftPad(companyId, 2, "0")+"', '"+StringUtils.leftPad(agencyId, 2, "0")+"', "); //FIRMA ,FILIAL
			query.append("0,0, "); //MWSTV , MWSTN
			query.append(" '' , '' , "); //PRKZV , PRKZN
			query.append("'"+loginUserName+"' ,"); // WSUSER
			query.append("'"+ba17_obj.getMarketingCode()+"' ,"); //MCODEV
			query.append(" 0,"); //EPRV
			query.append(" 0,"); //EPRN
			query.append(" 0,"); //EKNPRV
			query.append(" 0,"); //BUK_KONTOV
			query.append(" 0,"); //BUK_KONTON
			query.append(" 0,"); //LEIARTV
			query.append(" 0 , "); //LEIARTN
			query.append("'00000', '00000', "); //BVDAUF ,BVDAUP
			query.append("'0000000', '00000', "); //BSNOME ,BSN_LC
			query.append("'000000', '000000' )"); //SERVAN, EBEST
			
			
		return query.toString();
	}
	
	
	private String insertValuesInCPSDAT_BA17_Second(AccessesBA_DTO ba17_obj, String dataLibrary, String calculatedNetPrice,
			String companyId, String agencyId, String loginUserName, String tnrsValue, String tnrdValue,String bookingAccount,
			String calculatedNetPriceWith4Decimal) {
		
		log.info("Inside insertValuesInCPSDAT_BA17 method of BusinessCasesImpl");
		
		String manufacturer = ba17_obj.getManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING : ba17_obj.getManufacturer();
		
		String kb = "";
		String etnr ="";
		String es1 = "";
		String es2 = "";
		
			if(ba17_obj.getPartNumber() != null) {
				String partNo = StringUtils.rightPad(ba17_obj.getPartNumber(), 19, " ");
				
				kb = partNo.substring(0,1);
				etnr = partNo.substring(1,13);
				es1 = partNo.substring(13,15);
				es2 = partNo.substring(15,19);
			}
		
			
			StringBuilder query = new StringBuilder(" INSERT INTO ").append(dataLibrary).append(".E_CPSDAT ");
			query.append(" (HERST,BART,VFNR,BELNR,POSNR,KB,ETNR,ES1,ES2,T1,T2,EB_VZ,WS,T3,NW1_EW,NW2_EW,T4, ");
			query.append(" HERK,JJMMTT,HHMMSS,ETNR_S,EPREW,EPREWK,DAKEWV,DAKEWN,ERNPR,ERNPRK,RABGRV,RABGRN, ");
			query.append(" MARKEV,MARKEN,FEPR,GARANT,PROZ,TA_EWV,TA_EWN,LW_EWV,LW_EWN,NT_EWV,NT_EWN,XMLDAT, ");
			query.append(" XMLTIM,MCODE,EKNPR,TNRD,FIRMA,FILIAL,MWSTV,MWSTN,PRKZV,PRKZN,WSUSER,MCODEV,EPRV,");
			query.append(" EPRN,EKNPRV,BUK_KONTOV,BUK_KONTON,LEIARTV,LEIARTN , BVDAUF ,BVDAUP ,BSNOME ,BSN_LC, SERVAN , EBEST)  values ( ");
			query.append("'"+manufacturer+"', 40, "); // HERST,BART
			query.append(" "+ba17_obj.getWarehouseNo()+", '', '',  "); // VFNR,BELNR,POSNR
			query.append("'"+kb+"', "+"'"+etnr+"', "+"'"+es1+"', "+"'"+es2+"', ");  //KB ,ETNR ,ES1 ,ES2

			String partName = StringUtils.rightPad(ba17_obj.getPartName(), 15, " ");
			//Format –LW(2) + SPACES(6)+EPR(7)+SPACES(3)+PKZ(1)+SPACES(3)+TA(1)+SPACES(3)+BENEN(15)
			String t1Value = StringUtils.leftPad(ba17_obj.getDeliverIndicator(), 2,"0")+"      "+
			convertDecimalValue(ba17_obj.getListPriceWithoutVAT(), 5, 2)+"   "+StringUtils.leftPad(ba17_obj.getPriceIndicator(), 1, " ")
			+"   "+StringUtils.stripStart(ba17_obj.getPartsIndikator(), "0")+"   "+partName.substring(0, 15);
			
			query.append("'"+t1Value+"', '"); // T1  
			query.append("0"+StringUtils.leftPad(ba17_obj.getActivityType(), 2, "0")+"', "); //  T2 
			query.append("'+', '' , "); //  EB_VZ , WS
			query.append(" '000000', "); //  T3
			query.append(" '', '', "); //  NW1_EW,NW2_EW
			query.append("'"+StringUtils.rightPad("0000", 18, " ")+"00', ");  //T4 
			query.append(" 'BSN5BA', ");  // HERK
			query.append("'"+getCurrentDateInYYMMDD()+"' ,"); // JJMMTT
			query.append("'"+getCurrentTime()+"' ,"); // HHMMSS
			query.append("'"+tnrsValue+ "', "); // ETNR_S
			query.append("'"+StringUtils.leftPad(convertDecimalValue(ba17_obj.getListPriceWithoutVAT(), 5, 2), 7, "0") + "', 'N', "); //  EPREW ,EPREWK
			query.append("'"+StringUtils.leftPad(convertDecimalValue(calculatedNetPriceWith4Decimal, 5, 4), 9, "0")+ "', "); //DAKEWV
			query.append("'"+StringUtils.leftPad(convertDecimalValue(calculatedNetPriceWith4Decimal, 5, 4), 9, "0")+ "', "); //DAKEWN
			query.append("'0000000', 'N' ,");//ERNPR, ERNPRK
			query.append("'"+ba17_obj.getDiscountGroupValue() + "', '"+ba17_obj.getDiscountGroupValue() + "', ");//RABGRV ,RABGRN  
			query.append("'"+ba17_obj.getPartBrand()+ "', '"+ba17_obj.getPartBrand() + "', ");//MARKEV ,MARKEN  
			query.append(" '', '', ");//FEPR ,GARANT 
			query.append("'"+StringUtils.leftPad(convertDecimalValue(ba17_obj.getDiscountGroupPercentageValue(), 2, 2), 5, "0")+"', ");//PROZ
			query.append("'"+StringUtils.leftPad(ba17_obj.getPartsIndikator(), 2, "0") +"', '"+ StringUtils.leftPad(ba17_obj.getPartsIndikator(), 2, "0") +"', "); //TA_EWV ,TA_EWN 
			query.append("'"+StringUtils.leftPad(ba17_obj.getDeliverIndicator(), 2, "0") +"', '"+ StringUtils.leftPad(ba17_obj.getDeliverIndicator(), 2, "0") +"', "); //LW_EWV ,LW_EWN
			query.append("'0000000', ");//NT_EWV
			query.append("'0000000', ");//NT_EWN
			query.append("'000000', '000000', "); //XMLDAT ,XMLTIM 
			query.append("'"+ba17_obj.getMarketingCode()+"' ,"); //MCODE
			query.append("0, "); //EKNPR
			query.append("'"+tnrdValue+"', "); //TNRD
			query.append("'"+StringUtils.leftPad(companyId, 2, "0")+"', '"+StringUtils.leftPad(agencyId, 2, "0")+"', "); //FIRMA ,FILIAL
			query.append(""+ba17_obj.getVatRegistrationNumber()+","+ba17_obj.getVatRegistrationNumber()+", "); //MWSTV , MWSTN
			query.append("'"+ba17_obj.getPriceIndicator()+"' , '"+ba17_obj.getPriceIndicator()+"' , "); //PRKZV , PRKZN
			query.append("'"+loginUserName+"' ,"); // WSUSER
			query.append("'"+ba17_obj.getMarketingCode()+"' ,"); //MCODEV
			query.append(" 0,"); //EPRV
			query.append(" 0,"); //EPRN
			query.append(" 0,"); //EKNPRV
			query.append(bookingAccount).append(", "); //BUK_KONTOV
			query.append(bookingAccount).append(", "); //BUK_KONTON
			query.append(ba17_obj.getActivityType()).append(", "); //LEIARTV
			query.append(ba17_obj.getActivityType()).append(", "); //LEIARTN
			query.append("'00000', '00000', "); //BVDAUF ,BVDAUP 
			query.append("'0000000', '00000', "); //BSNOME ,BSN_LC
			query.append("'000000', '000000' )"); //SERVAN, EBEST
			
			
		return query.toString();
	}

	private String calculateNetPriceWith4Decimal( String satzart, String purchasePrice ) throws Exception {
		
		double purchasePriceD = 00.0000d;
		
		double satzart_100divided = (satzart!=null && !satzart.trim().isEmpty()) ? Double.parseDouble(satzart)/100 : Double.parseDouble("00.00")/100;
		
		if(purchasePrice!=null && !purchasePrice.trim().isEmpty()) {
			purchasePriceD = Double.parseDouble(purchasePrice);
		}
		
		double cal_Purchase = purchasePriceD * satzart_100divided;
		
		double cal_netPrice = purchasePriceD - cal_Purchase;
		
		//String calculatedNetPrice = StringUtils.leftPad(convertDecimalValue(String.valueOf(cal_netPrice), 5, 2), 7, "0");
		
		String calculatedNetPrice = String.valueOf(cal_netPrice);
		
		return calculatedNetPrice;
	}
	
	
	/**
	 * This method is used for Clean up the Parts before specified period using BA 50.	 * 
	 */
	@Override
	public Map<String, Boolean> partsCleanupUsingBA50(String warehouseNumber, String toDate, String dataLibrary, String schema, String companyId, 
			String agencyId, String loginUserName ){

		log.info("Inside partsCleanupUsingBA50 method of BusinessCasesImpl");

		Map<String, Boolean> programOutput = new HashMap<String, Boolean>();		
		programOutput.put("isPartsCleanUpExecutedSuccessfully", false);

		try {			
			String formattedDate = commonUtils.getDateInYYYYMMDD(toDate);

			StringBuilder query_1 = new StringBuilder(" SELECT TNR, LNR, HERST, BESAUS, AKTBES from ").append(dataLibrary).append(".E_ETSTAMM WHERE ");
			query_1.append(" SA = 2 AND AKTBES = 0 AND BESAUS = 0 ");
			query_1.append(" AND LNR = ").append(warehouseNumber);
			query_1.append(" AND DTLBEW < ").append("'"+formattedDate+"'");

			List<PartDetails> PartsListforCleanUp = dbServiceRepository.getResultUsingQuery(PartDetails.class, query_1.toString(), true);

			if(PartsListforCleanUp != null && !PartsListforCleanUp.isEmpty()) {

				FinalizationsBA_DTO  ba50_obj = new FinalizationsBA_DTO();

				for(PartDetails parts : PartsListforCleanUp) {					

					ba50_obj.setPartNumber(parts.getPartNumber());
					ba50_obj.setWarehouseNumber(warehouseNumber);
					ba50_obj.setManufacturer(parts.getOem());

					//call BA 50 impl to cleanup parts
					newJavaImplementation_BA50(ba50_obj, dataLibrary, schema, companyId, agencyId, loginUserName );
				}
				programOutput.put("isPartsCleanUpExecutedSuccessfully", true);
			}
			else {
				log.info("Parts List is empty");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Parts"));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Parts"));
				throw exception;
			}
		}
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.DELETE_ADMIN_SETTING_FAILED_MSG_KEY, "Parts"));
			log.error(messageService.getReadableMessage(ExceptionMessages.DELETE_ADMIN_SETTING_FAILED_MSG_KEY, "Parts"), exception);
			throw exception;
		}

		return programOutput;
	}
	
	/**
	 * This method is used for BA, 01,02,and 17
	 * 
	 *  OETKMOD Business logic
	 */
	public AccessesBA_DTO newJavaImplementation_OETKMOD(String dataLibrary, String schema, String manufacturer, String partNumber, String businessCase){
		log.info("Inside newJavaImplementation_OETKMOD method of BusinessCasesImpl");
		
		AccessesBA_DTO accessBA_Obj = null;
		
		try{
			StringBuilder query_1 = new StringBuilder(" Select TLE_BENENA, TLE_BLP, TLE_RG, TLE_MC, TLE_PRKZ, TLE_TAXCDE, TLE_SORTKL, TLE_TMARKE FROM ").append(schema).append(".ETK_TLEKAT WHERE ");
			query_1.append(" TLE_TNR= ").append("'"+partNumber+"'");
			query_1.append(" AND TLE_HERST  = ").append("'"+manufacturer+"'");
			query_1.append(" AND TLE_DTGULV <= current date order by TLE_DTGULV desc LIMIT 1");

			List<PartCatalog> finalizationsPartBAList = dbServiceRepository.getResultUsingQuery(PartCatalog.class, query_1.toString(), true);
			String partBrand = "";
			String markingCode = "";
			String discountGroup = "";
			//if the list is not empty
			if (finalizationsPartBAList != null && !finalizationsPartBAList.isEmpty()) {
				accessBA_Obj = new AccessesBA_DTO();
				log.info("Part number from TLEKAT");
				for(PartCatalog partCatalog : finalizationsPartBAList){
					accessBA_Obj.setPartName(partCatalog.getPartName());
					accessBA_Obj.setListPriceWithoutVAT(decimalformat_fixtwodigit.format(partCatalog.getPurchasePrice() !=null ? partCatalog.getPurchasePrice() : "0.00"));
					accessBA_Obj.setShoppingDiscountGroup(partCatalog.getDiscountGroup());
					accessBA_Obj.setPriceIndicator(partCatalog.getPriceMark());
					accessBA_Obj.setMarketingCode(partCatalog.getMarketingCode());
					accessBA_Obj.setAssortmentClass("");
					accessBA_Obj.setVatRegistrationNumber(StringUtils.leftPad(String.valueOf(partCatalog.getValueAddedTax()),2,"0"));
					accessBA_Obj.setPartBrand(partCatalog.getPartBrand());
					partBrand = partCatalog.getPartBrand();
					markingCode = partCatalog.getMarketingCode();
					discountGroup = partCatalog.getDiscountGroup();
				}
					Map<String, String> output_bsn475 = newJavaImplementation_BSN475(partBrand, dataLibrary, schema, manufacturer, markingCode,discountGroup, partNumber);
					
					output_bsn475 =  checkSpecialCasesInBSN475(partNumber,  output_bsn475);
					
					accessBA_Obj.setDeliverIndicator(output_bsn475.get("Leiferwerk"));
					accessBA_Obj.setPartsIndikator(output_bsn475.get("Teilart"));
					accessBA_Obj.setActivityType(output_bsn475.get("Leistungsart"));
					accessBA_Obj.setStorageLocation("");
					accessBA_Obj.setStorageIndikator("");
					accessBA_Obj.setNetShoppingPrice("0");
					accessBA_Obj.setBookingAmount("0");
					accessBA_Obj.setVatRegistrationNumber("01");
					accessBA_Obj.setSelfCalculatedNetPrice("00");
					accessBA_Obj.setSpecialDiscount("00");
			}else{

				StringBuilder query_2 = new StringBuilder("SELECT tkf_herst AS herst, tkf_tnr AS tnr, tkf_benen AS benen, tkf_tmarke AS tmarke, TKF_BLP AS BLP, TKF_RG AS RG, ");
				query_2.append(" TKF_MC AS MC, TKF_PRKZ AS PRKZ, TKF_TAXCDE AS TAXCDE, TKF_SORTKL AS SORTKL, TKF_BLPZ FROM ").append(schema).append(".etk_tkfher WHERE ");
				query_2.append(" TKF_TNR= ").append("'"+partNumber+"'");
				query_2.append(" AND TKF_HERST  = ").append("'"+manufacturer+"'");
				query_2.append(" AND TKF_DTGULV <= current date order by TKF_DTGULV desc LIMIT 1");


				List<SearchParts2> partBAList =  dbServiceRepository.getResultUsingQuery(SearchParts2.class, query_2.toString(),true);

				//if the list is not empty
				if (partBAList != null && !partBAList.isEmpty()) {
					accessBA_Obj = new AccessesBA_DTO();
					log.info("Part number from TKFHER");
					for(SearchParts2 parts : partBAList){

						accessBA_Obj.setPartName(parts.getDescription());
						
						String priceBLPZ = decimalformat_fixtwodigit.format(parts.getOldListPrice() != null ? parts.getOldListPrice() : "0.00");
						if(Double.parseDouble(priceBLPZ) > 0 ) {
							accessBA_Obj.setListPriceWithoutVAT(priceBLPZ);	
						}else {
							accessBA_Obj.setListPriceWithoutVAT(decimalformat_fixtwodigit.format(parts.getListPrice() != null ? parts.getListPrice() : "0.00"));
						}
						accessBA_Obj.setShoppingDiscountGroup(parts.getShoppingDiscountGroup());
						accessBA_Obj.setPriceIndicator(parts.getPriceIndicator());
						accessBA_Obj.setMarketingCode(parts.getMarketingCode());
						accessBA_Obj.setAssortmentClass("");
						accessBA_Obj.setVatRegistrationNumber(StringUtils.leftPad(parts.getVatRegistrationNumber(), 2, "0"));
						accessBA_Obj.setPartBrand(parts.getOemBrand());
						partBrand = parts.getOemBrand();
						markingCode = parts.getMarketingCode();
						discountGroup = parts.getShoppingDiscountGroup();
						
					}
					Map<String, String> output_bsn475 = newJavaImplementation_BSN475(partBrand, dataLibrary, schema, manufacturer, markingCode,discountGroup,partNumber);
					
					output_bsn475 =  checkSpecialCasesInBSN475(partNumber,  output_bsn475);
					
					accessBA_Obj.setDeliverIndicator("00");
					accessBA_Obj.setPartsIndikator(output_bsn475.get("Teilart"));
					accessBA_Obj.setActivityType(output_bsn475.get("Leistungsart"));
					accessBA_Obj.setStorageLocation("");
					accessBA_Obj.setStorageIndikator("");
					accessBA_Obj.setNetShoppingPrice("0");
					accessBA_Obj.setBookingAmount("0");
					accessBA_Obj.setVatRegistrationNumber("01");
					accessBA_Obj.setSelfCalculatedNetPrice("00");
					accessBA_Obj.setSpecialDiscount("00");

				}else if(!businessCase.equalsIgnoreCase("17")){
					log.info("Part number not yet created in TKFHER and TLEKAT");
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.BUSINESS_CASES_17_02FAILED_MSG_KEY));
					log.error(messageService.getReadableMessage(ExceptionMessages.BUSINESS_CASES_17_02FAILED_MSG_KEY));
					throw exception;
				}
			}
			
			
		}catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,"Java implementation OETKMOD"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,"Java implementation OETKMOD"), exception);
			throw exception;
		}

		return accessBA_Obj;
	}

	
	/**
	 * This method is used for BA, 01,02,and 17
	 * 
	 *  B Business logic
	 * @param partNumber 
	 * @param discountGroup 
	 * @param markingCode 
	 */
	@Override
	public Map<String, String> newJavaImplementation_BSN475( String partBrand,String dataLibrary, String schema, String manufacturer, 
			String marketingCode, String discountGroup, String partNumber){
		log.info("Inside newJavaImplementation_BSN475 method of BusinessCasesImpl");

		Map<String, String> output = new HashMap<String, String>();
		
		try{
			BusinessCases25DTO ba25_obj = new BusinessCases25DTO();
			ba25_obj.setManufacturer(manufacturer);
			ba25_obj.setOemBrand(partBrand);
			
			String defaultMarketingCode = "D00000";
			if(!manufacturer.equalsIgnoreCase("DCAG")){
				defaultMarketingCode = "X00000";
			}

			//setup default Marke, Leiferwerk, Teilart and Leistungsart using defaultMarketing code. 
			Map<String, String> defaultValues = setupDefaultValues(dataLibrary, ba25_obj, defaultMarketingCode );
			
			//Determine values based on ETSTAMM - MCode 
			Map<String, String> valuesbasedOnMcode = setupValuesBasedOnMcode(dataLibrary, ba25_obj, marketingCode , defaultValues);
			
			//Determine values based on ETSTAMM - TNR 
			String markeForTNRBased = valuesbasedOnMcode.get("Marke");
			if(ba25_obj.getOemBrand()!=null && !ba25_obj.getOemBrand().isEmpty()) {
				markeForTNRBased = ba25_obj.getOemBrand();
			}
			ba25_obj.setOemBrand(markeForTNRBased);
			ba25_obj.setDiscountGroup(discountGroup);
			ba25_obj.setPartNumber(partNumber);
			Map<String, String> valuesbasedOnTNR = new HashMap<String, String>();
			if(partNumber!=null && !partNumber.trim().isEmpty()) {
			 valuesbasedOnTNR = setupValuesBasedOnTNR(dataLibrary, ba25_obj , defaultValues);
			}else {
			valuesbasedOnTNR.put("isValuesFromInputTNR","FALSE");	
			}

			//Call YMARKE  as per cobol
			String vorbelValue = getVorbelValue(schema, ba25_obj);

			if(vorbelValue!=null && !vorbelValue.isEmpty() && vorbelValue.equalsIgnoreCase("T")){ 
				
				if(valuesbasedOnTNR.get("isValuesFromInputTNR").equalsIgnoreCase("FALSE") && valuesbasedOnMcode.get("isValuesFromInputMcode").equalsIgnoreCase("TRUE") ) {
					output = valuesbasedOnMcode;
				}else if(valuesbasedOnTNR.get("isValuesFromInputTNR").equalsIgnoreCase("TRUE")) {
					output = valuesbasedOnTNR;
				}else if(valuesbasedOnTNR.get("isValuesFromInputTNR").equalsIgnoreCase("FALSE") && valuesbasedOnMcode.get("isValuesFromInputMcode").equalsIgnoreCase("FALSE")) {
					output = defaultValues;
				}
			}else{
				
				if(valuesbasedOnTNR.get("isValuesFromInputTNR").equalsIgnoreCase("TRUE")) {
					output = valuesbasedOnTNR;
				}else if(valuesbasedOnTNR.get("isValuesFromInputTNR").equalsIgnoreCase("FALSE") && valuesbasedOnMcode.get("isValuesFromInputMcode").equalsIgnoreCase("TRUE")) {
					output = valuesbasedOnMcode;
				}else if(valuesbasedOnTNR.get("isValuesFromInputTNR").equalsIgnoreCase("FALSE") && valuesbasedOnMcode.get("isValuesFromInputMcode").equalsIgnoreCase("FALSE")) {
					output = defaultValues;
				}
				
			}
			
		}catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,"Java BSN475"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,"Java BSN475"), exception);
			throw exception;
		}

		return output;
	}


	private Map<String, String> setupValuesBasedOnTNR(String dataLibrary, BusinessCases25DTO ba25_obj,
			Map<String, String> defaultValues) {
		
		log.info("Inside setupValuesBasedOnTNR method of BusinessCasesImpl");
		
		Map<String, String> valueBasedOnTNR = new HashMap<String, String>();
		
		String oemBrandByTNR = ""; // MARKE
		String bodyWorkByTNR = ""; //Leiferwerk
		String partsIndikatorByTNR = ""; //Teilart
		String activityTypeByTNR = ""; //Leistungsart
		
		valueBasedOnTNR.put("isValuesFromInputTNR","TRUE");
		
		List<BA25DefaultSetup> bsnpar_list = checkDefaultValuesByTNR(dataLibrary,ba25_obj, "E_BSNPAR"); 

		if (bsnpar_list != null && !bsnpar_list.isEmpty()) {
			for (BA25DefaultSetup bsnpar_dtls : bsnpar_list) {
					oemBrandByTNR = bsnpar_dtls.getOemBrand(); // Marke
					bodyWorkByTNR = bsnpar_dtls.getBodywork(); //// Leiferwerk
					partsIndikatorByTNR = String.valueOf(bsnpar_dtls.getPartsIndikator()); // Teilart
					activityTypeByTNR = String.valueOf(bsnpar_dtls.getActivityType()); // Leistungsart
					break;
				
			}
		}
		
		if(oemBrandByTNR!= null && oemBrandByTNR.isEmpty() ) {
			oemBrandByTNR = defaultValues.get("Marke");;
			valueBasedOnTNR.put("isValuesFromInputTNR","FALSE");
		}
		if(bodyWorkByTNR!= null && bodyWorkByTNR.isEmpty() ) {
			bodyWorkByTNR = defaultValues.get("Leiferwerk");
		}
		if(partsIndikatorByTNR!= null && partsIndikatorByTNR.isEmpty() ) {
			partsIndikatorByTNR = defaultValues.get("Teilart");
			valueBasedOnTNR.put("isValuesFromInputTNR","FALSE");
		}
		if(activityTypeByTNR!= null && activityTypeByTNR.isEmpty() ) {
			activityTypeByTNR = defaultValues.get("Leistungsart");
			valueBasedOnTNR.put("isValuesFromInputTNR","FALSE");
		}
		
		
		valueBasedOnTNR.put("Marke",oemBrandByTNR );
		valueBasedOnTNR.put("Leiferwerk", bodyWorkByTNR);
		valueBasedOnTNR.put("Teilart", partsIndikatorByTNR);
		valueBasedOnTNR.put("Leistungsart", activityTypeByTNR);
		
		return valueBasedOnTNR;
	}


	private Map<String, String> setupValuesBasedOnMcode(String dataLibrary, BusinessCases25DTO ba25_obj,
			String etstamm_marketingCode, Map<String, String> defaultValues) {
		log.info("Inside setupValuesBasedOnMcode method of BusinessCasesImpl");
		
		Map<String, String> valueBasedOnMCode = new HashMap<String, String>();
		
		String oemBrandByMcode = ""; // MARKE
		String bodyWorkByMcode = ""; //Leiferwerk
		String partsIndikatorByMcode = ""; //Teilart
		String activityTypeByMcode = ""; //Leistungsart
		
		valueBasedOnMCode.put("isValuesFromInputMcode","TRUE");
		//determines Marke
		BA25DefaultSetup bsnpam1_dtls =getDefaultValuesByMcode(dataLibrary,ba25_obj, etstamm_marketingCode, "E_BSNPAM1"," MARKE as TMARKE "); 

		if(bsnpam1_dtls!=null && bsnpam1_dtls.getOemBrand()!=null && !bsnpam1_dtls.getOemBrand().trim().isEmpty() ){
			oemBrandByMcode = bsnpam1_dtls.getOemBrand();
		}else {
			oemBrandByMcode = defaultValues.get("Marke");
			valueBasedOnMCode.put("isValuesFromInputMcode","FALSE");
		}

		//determines Leiferwerk
		BA25DefaultSetup bsnpam2_dtls = getDefaultValuesByMcode(dataLibrary,ba25_obj, etstamm_marketingCode, "E_BSNPAM2"," LW as Leiferwerk "); 

		if(bsnpam2_dtls!=null && bsnpam2_dtls.getBodywork()!=null && !bsnpam2_dtls.getBodywork().trim().isEmpty() ){
			bodyWorkByMcode = bsnpam2_dtls.getBodywork();
		}else {
			bodyWorkByMcode = defaultValues.get("Leiferwerk");
		}

		//determines Teilart 

		BA25DefaultSetup bsnpam3_dtls = getDefaultValuesByMcode(dataLibrary,ba25_obj, etstamm_marketingCode, "E_BSNPAM3", " TA, LEART as LEIART "); 

		if(bsnpam3_dtls!=null && bsnpam3_dtls.getPartsIndikator()!=null){
			partsIndikatorByMcode = String.valueOf(bsnpam3_dtls.getPartsIndikator());
		}else {
			partsIndikatorByMcode = defaultValues.get("Teilart");
			valueBasedOnMCode.put("isValuesFromInputMcode","FALSE");
		}

		//determines Leistungsart
		if(bsnpam3_dtls!=null && bsnpam3_dtls.getActivityType()!=null){
			activityTypeByMcode = String.valueOf(bsnpam3_dtls.getActivityType());
		}else {
			activityTypeByMcode = defaultValues.get("Leistungsart");
			valueBasedOnMCode.put("isValuesFromInputMcode","FALSE");
		}
		
		valueBasedOnMCode.put("Marke",oemBrandByMcode );
		valueBasedOnMCode.put("Leiferwerk", bodyWorkByMcode);
		valueBasedOnMCode.put("Teilart", partsIndikatorByMcode);
		valueBasedOnMCode.put("Leistungsart", activityTypeByMcode);
		
		return valueBasedOnMCode;
	}
	
	
	private Map<String, String> checkSpecialCasesInBSN475(String partNumber, Map<String, String> output_bsn475) {
		log.info("Inside checkSpecialCasesInBSN475 method of BusinessCasesImpl");

		if(partNumber!=null && !partNumber.isEmpty() && partNumber.substring(0, 1).equalsIgnoreCase("N")) {
			
				output_bsn475.put("Marke","D" );
				output_bsn475.put("Teilart", "01");
				output_bsn475.put("Leistungsart", "40");
			
			
		}else if(partNumber!=null && !partNumber.isEmpty() && partNumber.substring(0, 1).equalsIgnoreCase("W")) {
			output_bsn475.put("Marke","XW" );
			
			if(partNumber.length() >= 3 && !partNumber.substring(2, 3).equalsIgnoreCase("S")) {
				output_bsn475.put("Teilart", "01");
				output_bsn475.put("Leistungsart", "10");
			}
		}
		return output_bsn475;
	}

	
	/**
	 * This method is used to get BA 17 details from DB.
	 */
	@Override
	public AccessesBA_DTO getDetailsFor_BA17(String schema, String dataLibrary,String manufacturer,String partNumber, String allowedWarehouses) {
		log.info("Inside getDetailsFor_BA17 method of BusinessCasesImpl");
		AccessesBA_DTO ba17_obj = null;
		try {
			//validate the Warehouse Ids
			validateWarehouses(allowedWarehouses);
			
			manufacturer = manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING) ? RestInputConstants.DCAG_STRING : manufacturer;

			StringBuilder query_1 = new StringBuilder(" SELECT LPAD(LNR , 2, 0) as AP_LNR, EPR, RG, TMARKE, LIWERK, TA, LEIART, PRKZ, LOPA, BENEN, SA, MC,");
			query_1.append(" LEKPR ,LKZ ,TNRS ,TNRD ,FIRMA ,FILIAL ,KSPKZ,EKNPR, NPREIS, SKL, DAK, MWST FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE ");
			query_1.append("HERST = ").append("'"+manufacturer+"'");
			query_1.append(" AND TNR = ").append("'"+partNumber+"'");

			List<PartDetails> etstammLNRForPart = dbServiceRepository.getResultUsingQuery(PartDetails.class, query_1.toString(), true);

			//if the list is not empty
			if (etstammLNRForPart != null && !etstammLNRForPart.isEmpty()) {

				ba17_obj = new AccessesBA_DTO();
				for(PartDetails partFromEtstamm : etstammLNRForPart){

					ba17_obj.setListPriceWithoutVAT(decimalformat_fixtwodigit.format(partFromEtstamm.getPurchasePrice() != null ? partFromEtstamm.getPurchasePrice() : "0.00"));
					ba17_obj.setShoppingDiscountGroup(partFromEtstamm.getDiscountGroup());
					ba17_obj.setPartBrand(partFromEtstamm.getOemBrand());
					ba17_obj.setDeliverIndicator(String.valueOf(partFromEtstamm.getDeliverIndicator()));
					ba17_obj.setPartsIndikator(String.valueOf(partFromEtstamm.getPartsIndikator()));
					ba17_obj.setActivityType(String.valueOf(partFromEtstamm.getActivityType()));
					ba17_obj.setPriceIndicator(partFromEtstamm.getPriceMark());
					ba17_obj.setStorageLocation(partFromEtstamm.getStorageLocation());
					ba17_obj.setPartName(partFromEtstamm.getName());
					ba17_obj.setStorageIndikator(String.valueOf(partFromEtstamm.getStorageIndikator()));
					ba17_obj.setMarketingCode(partFromEtstamm.getMarketingCode());
					ba17_obj.setPreviousBookingPrice(String.valueOf(partFromEtstamm.getAverageNetPrice()));
					ba17_obj.setAssortmentClass(partFromEtstamm.getCommonPartWithUnimog());
					ba17_obj.setBookingAmount("");
					ba17_obj.setVatRegistrationNumber(StringUtils.leftPad(String.valueOf(partFromEtstamm.getValueAddedTax()),2,"0"));
					ba17_obj.setSelfCalculatedNetPrice(decimalformat_fixtwodigit.format(partFromEtstamm.getPreviousPurchasePrice()!=null ? partFromEtstamm.getPreviousPurchasePrice() : "0.00"));
					ba17_obj.setSpecialDiscount("00");
					ba17_obj.setNetShoppingPrice("0");
					
					ba17_obj.setLastPurchasePrice(String.valueOf(partFromEtstamm.getLastPurchasePrice()));
					ba17_obj.setSortingFormatPartNumber(partFromEtstamm.getSortingFormatPartNumber());
					ba17_obj.setPrintingFormatPartNumber(partFromEtstamm.getPrintingFormatPartNumber());
					ba17_obj.setCompany(String.valueOf(partFromEtstamm.getCompany()));
					ba17_obj.setAgency(String.valueOf(partFromEtstamm.getBranch()));
					ba17_obj.setDeletionMark(partFromEtstamm.getDeletionMark());
					ba17_obj.setCalculationLock(partFromEtstamm.getCalculationLock());
					ba17_obj.setManufacturer(manufacturer);
					ba17_obj.setPartNumber(partNumber);
					
				}

			} else {
				ba17_obj = newJavaImplementation_OETKMOD(dataLibrary, schema, manufacturer, partNumber, "17");
				if (ba17_obj == null) {
					ba17_obj = new AccessesBA_DTO();
				}
			}
			
			String finalWarehouseIds = "";
			/*StringBuilder query = new StringBuilder(" SELECT  LPAD(LNR , 2, 0) as AP_LNR  FROM ").append(dataLibrary).append(".E_ETSTAMM et WHERE ");
			query.append("HERST = ").append("'"+manufacturer+"'");
			query.append(" AND TNR = ").append("'"+partNumber+"'");

			List<PartDetails> etstammLNRForPart = dbServiceRepository.getResultUsingQuery(PartDetails.class, query.toString(), true); */
			
			if(etstammLNRForPart!= null && !etstammLNRForPart.isEmpty()) {
			List<String> etstammWarehouse =  etstammLNRForPart.stream().map(PartDetails::getAlphaPlusWarehouseId).collect(Collectors.toList());
			List<String> listFromtoken = Stream.of(allowedWarehouses.split(",", -1)).collect(Collectors.toList());
			listFromtoken.removeAll(etstammWarehouse);
			finalWarehouseIds = listFromtoken.stream().map(String::valueOf).collect(Collectors.joining(","));
			log.info("ETSTAMM Warehouses for Part: {} "+etstammWarehouse.stream().map(String::valueOf).collect(Collectors.joining(",")));
			}else {
				finalWarehouseIds = allowedWarehouses;
			}
			log.info("allowed Warehouses: {} "+allowedWarehouses);
			log.info("Final Warehouse List for part creation: {} "+finalWarehouseIds);
			
			if(finalWarehouseIds.isEmpty()) {
			
				log.info("Part position already exists in all warehouse. BA17 new installation implausible.");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.BUSINESS_CASES_17_01FAILED_MSG_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.BUSINESS_CASES_17_01FAILED_MSG_KEY));
				throw exception;
			}
			
			//Warehouse details list for drop down
			createFinalWarehouseListForDropdown(ba17_obj,finalWarehouseIds, schema);

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, " BA 17"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, " BA 17"), exception);
			throw exception;
		}

		return ba17_obj;

	}


	private void createFinalWarehouseListForDropdown(AccessesBA_DTO ba17_obj, String finalWarehouseIds, String schema) {
		
		log.info("Create final Warehouse List for part creation");
		
		StringBuilder query = new StringBuilder("select WAREHOUS_ID, AP_WAREHOUS_ID, NAME, CITY, POST_CODE, STREETANDNUM, ISACTIVE, VF_NUMBER from ");
		query.append(schema).append(".O_WRH").append(" where ISACTIVE = 1 and AP_WAREHOUS_ID  IN ( ").append(finalWarehouseIds).append(" ) order by AP_WAREHOUS_ID ");

		List<AdminWarehouse> lagerDetailsList = dbServiceRepository.getResultUsingQuery(AdminWarehouse.class, query.toString(), true);

		if(lagerDetailsList != null && !lagerDetailsList.isEmpty()) {
			List<LagerDetailsDTO> lagerList = new ArrayList<LagerDetailsDTO>();
			
			for(AdminWarehouse lagerDetails : lagerDetailsList) {
					
					LagerDetailsDTO lagerDetailsDto = new LagerDetailsDTO();
					
					lagerDetailsDto.setWarehouseNo(StringUtils.leftPad(lagerDetails.getAlphaPlusWarehouseId().toString(),2,"0"));
					lagerDetailsDto.setWarehouseName(lagerDetails.getWarehouseName());
					lagerDetailsDto.setAddress(new StringBuilder(lagerDetails.getStreetNumber()).append(", ")
							.append(lagerDetails.getPostalCode()).append(" ").append(lagerDetails.getCity()).toString());
					lagerDetailsDto.setVfNumber(String.valueOf(lagerDetails.getVfNumber()));
					
					String warehouseName = lagerDetails.getWarehouseName()!=null?lagerDetails.getWarehouseName():"";
					lagerDetailsDto.setWarehouseIdWithName(lagerDetailsDto.getWarehouseNo()+" - "+warehouseName);
					
					lagerList.add(lagerDetailsDto);
				}
			
			ba17_obj.setFinalWarehouseListDetails(lagerList);
		}
	}

	
	/**
	 * 
	 * This method is used to create part in multiple warehouse which is not available in ETSTAMM 	
	 * 
	 *  */
	@Override
	public Map<String, Boolean> createPartInMultipleWarehouses(String dataLibrary, String schema,String partNumber, 
			 String manufacturer, String warehouseNumbers,String vatRegistrationNumber, String companyId, String agencyId, String loginUserName ) {
		log.info("Inside createPartInMultipleWarehouses method of BusinessCasesImpl");
		Map<String, Boolean> programOutput = null;
		try{
			manufacturer = manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING) ? RestInputConstants.DCAG_STRING : manufacturer;
			
			AccessesBA_DTO ba17_obj  = newJavaImplementation_OETKMOD(dataLibrary, schema, manufacturer, partNumber,"17");
			ba17_obj.setPartNumber(partNumber);
			ba17_obj.setManufacturer(manufacturer);
			ba17_obj.setWarehouseListForPartCreation(warehouseNumbers);
			ba17_obj.setVatRegistrationNumber(vatRegistrationNumber);
			ba17_obj.setBusinessCases("17");
			ba17_obj.setDiscountGroupValue(ba17_obj.getShoppingDiscountGroup());
			
			programOutput = newJavaImplementation_BA17(ba17_obj, dataLibrary, schema, companyId, agencyId, loginUserName);
			

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.CREATE_FAILED_MSG_KEY,"Create part in multiple warehouses "));
			log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY,"Create Part in multiple warehouses "), exception);
			throw exception;
		}

		return programOutput;
	}
	
	/**
	 * This method is used to get Preiskenzein value
	 */
	public Map<String, String> newJavaImplementation_BSN477(String dataLibrary, String schema, String manufacturer, 
			String marketingCode, String discountGroup,String partBrand, String partNumber){
		log.info("Inside newJavaImplementation_BSN477 method of BusinessCasesImpl");

		Map<String, String> output = new HashMap<String, String>();
		
		try{
			BusinessCases25DTO ba25_obj = new BusinessCases25DTO();
			ba25_obj.setManufacturer(manufacturer);
			ba25_obj.setMarketingCode(marketingCode);
			ba25_obj.setOemBrand(partBrand!=null?partBrand:"");
			ba25_obj.setDiscountGroup(discountGroup!=null?partBrand:"");
			ba25_obj.setPartNumber(partNumber);
			
			boolean isValuesByInputTNR = false;
			boolean isValuesByMC = false;
			String priceTagByMcode = "";
			String	priceTageByTNR = "";
			
			//Determine values based on ETSTAMM - MCode 
			if(marketingCode!=null && !marketingCode.isEmpty()) {
				isValuesByMC = true;
				priceTagByMcode = getValuesByMcode(dataLibrary,manufacturer, marketingCode); 
			}

			//Determine values based on ETSTAMM - TNR 
			if(partNumber!=null && !partNumber.trim().isEmpty()) {
				isValuesByInputTNR = true;
			List<BA25DefaultSetup> bsnpar_list = checkDefaultValuesByTNR(dataLibrary,ba25_obj, "E_BSNPAR"); 
			if (bsnpar_list != null && !bsnpar_list.isEmpty()) {
				for (BA25DefaultSetup bsnpar_dtls : bsnpar_list) {
					priceTageByTNR = bsnpar_dtls.getPriceTag(); // PKZ
						break;
				   }
			   }
		    }
			//Call YMARKE  as per cobol
			String vorbelValue = getVorbelValue(schema, ba25_obj);
			if(vorbelValue!=null && !vorbelValue.isEmpty() && vorbelValue.equalsIgnoreCase("T")){ 
				if(isValuesByMC && !isValuesByInputTNR) {
					output.put("priceIndicator", priceTagByMcode);	
				}else if(isValuesByInputTNR) {
					output.put("priceIndicator", priceTageByTNR);	
				}else {
					output.put("priceIndicator", "");
				}
			}else {
				if(isValuesByInputTNR && priceTageByTNR!=null && priceTageByTNR.equalsIgnoreCase("M")) {
					output.put("priceIndicator", priceTageByTNR);
				}else if(isValuesByMC) {
					output.put("priceIndicator", priceTagByMcode);	
				}else {
					output.put("priceIndicator", "");	
				}
			}
		}catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,"Java BSN477"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,"Java BSN477"), exception);
			throw exception;
		}
		
		return output;
	}
		
		private String getValuesByMcode(String dataLibrary, String manufacturer, String marketingCode) {
			
			manufacturer = manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING : manufacturer;
			String value = "";
			StringBuilder queryForDefaultValues = new StringBuilder("SELECT PKZ FROM ");
			queryForDefaultValues.append(dataLibrary).append(".E_BSNPAM4 ").append(" WHERE ");
			queryForDefaultValues.append(" HERST = ").append("'"+manufacturer+"'");
			queryForDefaultValues.append(" AND LNR = 01 AND PKZ = 'M' ");
			queryForDefaultValues.append(" AND MCODE = '").append(marketingCode).append("'");

			List<BA25DefaultSetup> partDtls_obj  = dbServiceRepository.getResultUsingQuery(BA25DefaultSetup.class,queryForDefaultValues.toString(),true);
			
			if(partDtls_obj!=null && !partDtls_obj.isEmpty()){
				value = partDtls_obj.get(0).getPriceTag();
			}
			
			return value;
		}
}