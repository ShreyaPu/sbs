package com.alphax.service.mb.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.alphax.common.rest.message.service.MessageService;
import com.alphax.model.mb.AdminAGNPRTMapping;
import com.alphax.model.mb.AdminPrinter;
import com.alphax.model.mb.AdminPrinterFKT;
import com.alphax.common.exception.AlphaXException;
import com.alphax.common.exception.ExceptionMessages;
import com.alphax.repository.DBServiceRepository;
import com.alphax.service.mb.AdminPrinterSettingService;
import com.alphax.vo.mb.AdminPrinterDTO;
import com.alphax.vo.mb.AdminPrinterFKTDTO;
import com.alphax.vo.mb.DropdownObject;
import com.alphax.common.constants.AlphaXCommonUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author A106744104
 *
 */

@Service
@Slf4j
public class AdminPrinterSettingServiceImpl extends BaseService implements AdminPrinterSettingService {

	@Autowired
	DBServiceRepository dbServiceRepository;

	@Autowired
	private MessageService messageService;

	@Autowired
	AlphaXCommonUtils commonUtils;


	/**
	 * This method is used to create new Printer Details in the DB
	 * @return Object
	 */
	@Override
	public AdminPrinterDTO createNewPrinter(AdminPrinterDTO adminPrinter_obj, String schema, String loginUserName) {

		log.info("Inside createNewPrinter method of AdminPrinterSettingServiceImpl");

		Connection con = null;
		try {

			StringBuilder printerQuery = new StringBuilder(" INSERT INTO ").append(schema).append(".O_PRINTER");
			printerQuery.append(" ( NAME, AP_ID, DESCRIPTION, CREATED_TS, CREATED_BY, ISACTIVE )  values ( ");

			printerQuery.append("'"+adminPrinter_obj.getPrinterName()+"', ");
			printerQuery.append("'"+adminPrinter_obj.getAlphaPlusPrinterId()+"', ");
			printerQuery.append("'"+adminPrinter_obj.getPrinterDescription() +"', ");
			printerQuery.append("CURRENT TIMESTAMP, ");
			printerQuery.append("'"+loginUserName+"', ");
			printerQuery.append("1 )");

			StringBuilder checkPrinterNameQuery = new StringBuilder("SELECT COUNT(*) AS COUNT FROM ").append(schema).append(".O_PRINTER ");
			checkPrinterNameQuery.append(" WHERE UPPER(NAME) = ").append("UPPER('"+adminPrinter_obj.getPrinterName()+"') OR UPPER(AP_ID) = ");
			checkPrinterNameQuery.append("UPPER('"+adminPrinter_obj.getAlphaPlusPrinterId()).append("')");

			con = dbServiceRepository.getConnectionObject();
			con.setAutoCommit(false);

			String count = dbServiceRepository.getCountUsingQuery(checkPrinterNameQuery.toString(), con);

			if(Integer.parseInt(count) > 0) {
				log.info("Duplicate Printer Name");

				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.UNIQUE_CONSTRAINT_FAILED_MSG_KEY, "Printer and Alpahplus Printer"));
				log.error(messageService.getReadableMessage(ExceptionMessages.UNIQUE_CONSTRAINT_FAILED_MSG_KEY, "Printer and Alpahplus Printer"));
				throw exception;				
			}

			int printerId = dbServiceRepository.insertResultUsingQuery(printerQuery.toString(), con);

			if (printerId == 0) {
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.CREATE_FAILED_MSG_KEY, "New Printer details"));
				log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY, "New Printer details"), exception);
				throw exception;
			}
			//if printer created successfully
			else {
				createEntryInTable_PRTHASFKT (schema, String.valueOf(printerId), adminPrinter_obj.getPrinterFKTList(), con);
				con.commit();
				con.setAutoCommit(true);
			}

			adminPrinter_obj.setPrinterId(String.valueOf(printerId));				

		} catch (AlphaXException ex) {
			transactionRollback(con, ex);
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.CREATE_FAILED_MSG_KEY, "New Printer details"));
			log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY,
					"New Printer details"), exception);

			transactionRollback(con, exception);
			throw exception;
		}
		finally {
			connectionClose(con);
		}

		return adminPrinter_obj;
	}


	/**
	 * This method is used to add new PrinterFKT Details in the DB.
	 */
	private boolean createEntryInTable_PRTHASFKT(String schema, String printerId, List<AdminPrinterFKTDTO> printerFKTList, Connection con) {

		log.info("Inside createEntryInTable_PRTHASFKT function for printer");

		boolean insertFlag = false;

		try (Statement stmt = con.createStatement()) {

			for(AdminPrinterFKTDTO printerFKT: printerFKTList) {

				StringBuilder query = new StringBuilder(" INSERT INTO ").append(schema).append(".O_PRTHSFKT");
				query.append(" (PRINTER_ID, FKT_ID, HAS_FKT)  values (");
				query.append(printerId +", ");
				query.append(printerFKT.getFktId() +", ");
				query.append(printerFKT.isHasActiveFKT()? 1 : 0).append(")");

				stmt.addBatch(query.toString());
			}

			int[] records =  dbServiceRepository.insertResultUsingBatchQuery(stmt);

			if(records != null) {
				insertFlag = true;
				log.info("Total rows inserted : {} ", records.length);
			}
		}
		catch (AlphaXException ex) {
			transactionRollback(con, ex);
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.CREATE_FAILED_MSG_KEY, "PrinterFKT details"));
			log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY,
					"PrinterFKT details"), exception);

			transactionRollback(con, exception);
			throw exception;
		}

		return insertFlag;
	}


	/**
	 * This method is used to return the list Printer List in the DB .
	 * @return Object
	 */
	@Override
	public List<DropdownObject> getPrinterList(String schema, String dataLibrary) {

		log.info("Inside getPrinterList method of AdminPrinterSettingServiceImpl");

		List<DropdownObject> printerList = new ArrayList<>();

		try {
			StringBuilder query = new StringBuilder("SELECT ID, NAME FROM ").append(schema).append(".O_PRINTER WHERE ISACTIVE = 1 Order by ID");
			List<AdminPrinter> adminPrinterList = dbServiceRepository.getResultUsingQuery(AdminPrinter.class, query.toString(), true);

			if (adminPrinterList != null && !adminPrinterList.isEmpty()) {

				for(AdminPrinter adminPrinter : adminPrinterList) {
					DropdownObject dropdownObject = new DropdownObject();
					dropdownObject.setKey(String.valueOf(adminPrinter.getPrinterId()));
					dropdownObject.setValue(String.valueOf(adminPrinter.getPrinterName()));

					printerList.add(dropdownObject);
				}
			}
			else {
				log.info("Printer List is empty");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Admin Printer"));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Admin Printer"));
				throw exception;
			}
		}
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Admin Printer list"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Admin Printer list"), exception);
			throw exception;
		}
		return printerList;		
	}


	/**
	 * This method is used to delete the User Details from DB
	 * @return Object
	 */
	@Override
	public Map<String, Boolean> deletePrinterDetails(String schema, String printerId, String loginUserName) {

		log.info("Inside deletePrinterDetails method of AdminPrinterSettingServiceImpl");
		Map<String, Boolean> programOutput = new HashMap<String, Boolean>();
		programOutput.put("isDeleted", false);
		Connection con = null;
		try {

			// This will delete from O_AGNPRT
			StringBuilder printerDelQuery1 = new StringBuilder(" DELETE FROM ").append(schema).append(".O_AGNPRT");
			printerDelQuery1.append(" WHERE PRT_ID = ").append(printerId);

			con = dbServiceRepository.getConnectionObject();
			con.setAutoCommit(false);

			boolean deleteFlag1 = dbServiceRepository.deleteResultUsingQuery(printerDelQuery1.toString(), con);	

			if (!deleteFlag1) {
				log.info("Printer setting not available in O_AGNPRT for this Id : {}", printerId);				
			}

			// This will delete from O_PRTHASFKT
			StringBuilder delete_query = new StringBuilder(" DELETE FROM ").append(schema).append(".O_PRTHSFKT");
			delete_query.append(" WHERE PRINTER_ID = ").append(printerId);

			boolean deleteFlag2 = dbServiceRepository.deleteResultUsingQuery(delete_query.toString(), con);

			if (!deleteFlag2) {
				log.info("Printer setting not available in O_PRTHASFKT for this Id : {}", printerId);
			}

			// This will delete from O_PRINTER
			StringBuilder printerDelQuery = new StringBuilder(" DELETE FROM ").append(schema).append(".O_PRINTER");
			printerDelQuery.append(" WHERE ID = ").append(printerId);

			boolean deleteFlag3 = dbServiceRepository.deleteResultUsingQuery(printerDelQuery.toString(), con);

			if (!deleteFlag3) {
				log.info("Unable to delete this printer for admin setting for this Id : {}", printerId);
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(
						LocaleContextHolder.getLocale(), ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "Printer"));
				log.error(messageService.getReadableMessage(ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "Printer"));
				throw exception;
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
					ExceptionMessages.DELETE_ADMIN_SETTING_FAILED_MSG_KEY, "Printer"));
			log.error(messageService.getReadableMessage(ExceptionMessages.DELETE_ADMIN_SETTING_FAILED_MSG_KEY, "Printer"), exception);

			transactionRollback(con, exception);
			throw exception;
		}
		finally{
			connectionClose(con);
		}
		return programOutput;
	}


	/**
	 * This method is used to update agency and printer mapping in DB
	 * @return Object
	 */
	@Override
	public Map<String, Boolean> updateAgencyPrinterMapping(List<AdminPrinterDTO> printer_DTO, String schema, String agencyId, String loginUserName) {

		log.info("Inside updateAgencyPrinterMapping method of AdminPrinterSettingServiceImpl");

		Map<String, Boolean> programOutput = new HashMap<String, Boolean>();
		programOutput.put("isUpdated", false);
		List<AdminAGNPRTMapping> agencyPRTTempList = new ArrayList<>();
		boolean isAvailable;

		try {

			StringBuilder query = new StringBuilder(" SELECT AGNPRT_ID, AGENCY_ID, PRT_ID FROM ").append(schema).append(".O_AGNPRT ");
			query.append(" WHERE ISACTIVE = 1 AND AGENCY_ID = ").append(agencyId);

			List<AdminAGNPRTMapping> agencyPRTMappingDBList = dbServiceRepository.getResultUsingQuery(AdminAGNPRTMapping.class, query.toString(), true);

			if(printer_DTO != null && !printer_DTO.isEmpty()) {

				for(AdminPrinterDTO printerObj : printer_DTO) {
					isAvailable = false;

					if(agencyPRTMappingDBList != null && !agencyPRTMappingDBList.isEmpty()) {

						for(AdminAGNPRTMapping agencyPRTDB : agencyPRTMappingDBList) {

							if(printerObj.getPrinterId().equals(String.valueOf(agencyPRTDB.getPrinterId()))) {

								StringBuilder update_query = new StringBuilder(" UPDATE ").append(schema).append(".O_AGNPRT");
								update_query.append(" SET UPDATED_TS = CURRENT TIMESTAMP , UPDATED_BY =  ");
								update_query.append("'" + loginUserName + "' WHERE AGNPRT_ID = ");
								update_query.append(agencyPRTDB.getAgencyPrinterId());

								dbServiceRepository.updateResultUsingQuery(update_query.toString());
								agencyPRTTempList.add(agencyPRTDB);
								isAvailable = true;
								break;
							}
						}
					}

					if(!isAvailable) {

						StringBuilder insert_query = new StringBuilder(" INSERT INTO ").append(schema).append(".O_AGNPRT");
						insert_query.append(" (AGENCY_ID, PRT_ID, ISACTIVE, CREATED_TS , CREATED_BY)  values ( ");
						insert_query.append(agencyId+ ",");
						insert_query.append(printerObj.getPrinterId() + ",");
						insert_query.append("1 ,");
						insert_query.append("CURRENT TIMESTAMP ,");
						insert_query.append("'" + loginUserName + "' )");

						int agencyPrinterId = dbServiceRepository.insertResultUsingQuery(insert_query.toString());

						if (agencyPrinterId == 0) {
							AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
									ExceptionMessages.CREATE_FAILED_MSG_KEY, "Agency Printer Mapping"));
							log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY,"Agency Printer Mapping"), exception);
							throw exception;
						}
					}
				}

				agencyPRTMappingDBList.removeAll(agencyPRTTempList);

				//delete the mapping from O_AGNPRT table
				deleteAgencyPrinterMapping(agencyPRTMappingDBList, schema);

			}
			else {				
				//delete the mapping from O_AGNPRT table
				deleteAgencyPrinterMapping(agencyPRTMappingDBList, schema);
			}

			programOutput.put("isUpdated", true);

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Agency Printer Mapping"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY,
					"Agency Printer Mapping"), exception);
			throw exception;
		}

		return programOutput;
	}


	/**
	 * This method is used to delete the Agency Printer mapping from DB.
	 * @param schema
	 * @param agencyPRTMappingDBList
	 * @return
	 */
	private boolean deleteAgencyPrinterMapping(List<AdminAGNPRTMapping> agencyPRTMappingDBList, String schema) {

		boolean deleteFlag = true;

		if(agencyPRTMappingDBList != null && !agencyPRTMappingDBList.isEmpty()) {

			for(AdminAGNPRTMapping agnPRTmapping : agencyPRTMappingDBList) {

				StringBuilder delete_query = new StringBuilder(" DELETE FROM ").append(schema).append(".O_AGNPRT");
				delete_query.append(" WHERE AGNPRT_ID = ").append(agnPRTmapping.getAgencyPrinterId());

				deleteFlag = dbServiceRepository.updateResultUsingQuery(delete_query.toString());

				if (!deleteFlag) {
					log.info("Unable to delete Agency Printer Mapping details for admin setting for this Id :"+ agnPRTmapping.getAgencyPrinterId());
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "AgencyPrinterId"));
					log.error(messageService.getReadableMessage(ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY,
							"AgencyPrinterId"));
					throw exception;
				}
			}
		}

		return deleteFlag;
	}
	
	
	/**
	 * This method is used to get the Printer List that are not assigned to current Agency from DB.
	 * @return List
	 */
	@Override
	public List<AdminPrinterDTO> getPrinters_NotAssignedToCrntAgency(String schema, String agencyId) {

		log.info("Inside getPrinters_NotAssignedToCrntAgency method of AdminPrinterSettingServiceImpl");

		List<AdminPrinterDTO> printerListDTO = new ArrayList<>();
		
		try {
			StringBuilder query = new StringBuilder(" SELECT ID, NAME, AP_ID, DESCRIPTION, ISACTIVE FROM ").append(schema).append(".O_PRINTER ");
			query.append(" WHERE ISACTIVE = 1 ");
			query.append("AND ID NOT IN (SELECT PRT_ID FROM ").append(schema).append(".O_AGNPRT ");
			query.append(" WHERE ISACTIVE = 1 AND AGENCY_ID = ").append(agencyId + ") order by ID");
			
			List<AdminPrinter> printerList = dbServiceRepository.getResultUsingQuery(AdminPrinter.class, query.toString(), true);

			if (printerList != null && !printerList.isEmpty()) {
				
				for(AdminPrinter adminPrinters : printerList){
					AdminPrinterDTO printerDTO = new AdminPrinterDTO();
					
					printerDTO.setPrinterId(String.valueOf(adminPrinters.getPrinterId()));
					printerDTO.setPrinterName(adminPrinters.getPrinterName());
					printerDTO.setPrinterDescription(adminPrinters.getPrinterDescription());
					printerDTO.setAlphaPlusPrinterId(adminPrinters.getAlphaPlusPrinterId());
					printerDTO.setActive(adminPrinters.getActive().compareTo(new BigDecimal("1")) == 0 ? true : false);
					
					printerListDTO.add(printerDTO);
				}
			}
			else{
				log.info("Printers that are not assigned to current Agency is not available ");
			} 
			
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Printer list that are not assigned to current Agency"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Printer list that are not assigned to current Agency"), exception);
			throw exception;
		}
		
		return printerListDTO;
	}
	
	
	/**
	 * This method is used to get the Printer List that are assigned to the current Agency from DB.
	 * @return List
	 */
	@Override
	public List<AdminPrinterDTO> getPrinters_AssignedToCrntAgency(String schema, String agencyId) {

		log.info("Inside getPrinters_AssignedToCrntAgency method of AdminPrinterSettingServiceImpl");

		List<AdminPrinterDTO> printerListDTO = new ArrayList<>();
		
		try {
			
			StringBuilder query = new StringBuilder(" SELECT ID, NAME, AP_ID, DESCRIPTION, ISACTIVE FROM ").append(schema).append(".O_PRINTER ");
			query.append(" WHERE ISACTIVE = 1 ");
			query.append("AND ID IN (SELECT PRT_ID FROM ").append(schema).append(".O_AGNPRT ");
			query.append(" WHERE ISACTIVE = 1 AND AGENCY_ID = ").append(agencyId + ") order by ID");
			
			List<AdminPrinter> printerList = dbServiceRepository.getResultUsingQuery(AdminPrinter.class, query.toString(), true);

			if (printerList != null && !printerList.isEmpty()) {
				
				for(AdminPrinter adminPrinters : printerList){
					AdminPrinterDTO printerDTO = new AdminPrinterDTO();
					
					printerDTO.setPrinterId(String.valueOf(adminPrinters.getPrinterId()));
					printerDTO.setPrinterName(adminPrinters.getPrinterName());
					printerDTO.setPrinterDescription(adminPrinters.getPrinterDescription());
					printerDTO.setAlphaPlusPrinterId(adminPrinters.getAlphaPlusPrinterId());
					printerDTO.setActive(adminPrinters.getActive().compareTo(new BigDecimal("1")) == 0 ? true : false);
					
					printerListDTO.add(printerDTO);
				}
			}
			else {
				log.info("Printer that are assigned to the current Agency is not available ");
			} 
			
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Printer list that are assigned to current Agency"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Printer list that are assigned to current Agency"), exception);
			throw exception;
		}
		
		return printerListDTO;
	}


	private void transactionRollback (Connection con, AlphaXException ex) throws AlphaXException{

		try {
			if(con!=null){
				con.rollback();
			}
		} catch (SQLException e1) {
			throw ex;
		}
	}


	private void connectionClose(Connection con) {
		try {
			if(con!=null){
				con.close();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	

	/**
	 * This method is used to Return the  Details of printer from DB
	 * @return Object
	 */
     @Override
	public AdminPrinterDTO getPrinterDetails(String schema, String dataLibrary,  String printerId) {
		
		log.info("Inside getPrinterDetails method of AdminPrinterSettingServiceImpl");

		AdminPrinterDTO adminPrinterDetail = null;

		try {
			StringBuilder query_1 = new StringBuilder("SELECT ID, NAME, AP_ID, DESCRIPTION FROM ").append(schema).append(".O_PRINTER");
			query_1.append(" WHERE ISACTIVE = 1 and ID = ").append(printerId);

			List<AdminPrinter> adminPrinterDetails = dbServiceRepository.getResultUsingQuery(AdminPrinter.class,query_1.toString(), true);
			
			if (adminPrinterDetails != null && !adminPrinterDetails.isEmpty()) {
				
				 adminPrinterDetail = new AdminPrinterDTO();

				for (AdminPrinter values : adminPrinterDetails) {
					adminPrinterDetail.setPrinterId(values.getPrinterId());
					adminPrinterDetail.setPrinterName(values.getPrinterName());
					adminPrinterDetail.setAlphaPlusPrinterId(values.getAlphaPlusPrinterId());
					adminPrinterDetail.setPrinterDescription(values.getPrinterDescription());

					StringBuilder query_2 = new StringBuilder("SELECT FKT_ID, HAS_FKT FROM ").append(schema).append(".O_PRTHSFKT");
					query_2.append(" WHERE PRINTER_ID = ").append(printerId);

					List<AdminPrinterFKT> adminPrinterFKTDetails = dbServiceRepository.getResultUsingQuery(AdminPrinterFKT.class, query_2.toString(), true);
					List<AdminPrinterFKTDTO> adminPrinterFKTList = new ArrayList<AdminPrinterFKTDTO>();
					
					if (adminPrinterFKTDetails != null && !adminPrinterFKTDetails.isEmpty()) {

						for (AdminPrinterFKT printerFKTDetails : adminPrinterFKTDetails) {

							AdminPrinterFKTDTO AdminPrinterFKTDetails = new AdminPrinterFKTDTO();
							AdminPrinterFKTDetails.setFktId(printerFKTDetails.getFktId());
							AdminPrinterFKTDetails.setHasActiveFKT(printerFKTDetails.getHasActiveFKT().compareTo(new BigDecimal("1")) == 0 ? true : false);

							adminPrinterFKTList.add(AdminPrinterFKTDetails);
						}
					}
					
					adminPrinterDetail.setPrinterFKTList(adminPrinterFKTList);
				}

			}else{
				log.info("Printer details for admin setting not found in O_Printer table for this Id : {}", printerId);
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "Printer"));
				log.error(messageService.getReadableMessage(ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "Printer"));
				throw exception;
			}
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,
					messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Admin Printer Details"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Admin Printer Details"), exception);
			throw exception;
		}

		return adminPrinterDetail;

	}


     /**
 	 * This method is used to Update Admin Printer Details Object from DB
 	 * @return Object
 	 */
	@Override
	public Map<String, Boolean> updateAdminPrinterDetails(AdminPrinterDTO updatedAdminPrinterDetails, String schema,
			String dataLibrary, String loginUserName) {
		log.info("Inside updateAdminPrinterDetails method of AdminPrinterSettingServiceImpl");
		Map<String, Boolean> programOutput = new HashMap<String, Boolean>();
		programOutput.put("isUpdated", false);

		try (Connection con = dbServiceRepository.getConnectionObject(); Statement stmt = con.createStatement();) {
			 con.setAutoCommit(false);
			
			 if (updatedAdminPrinterDetails != null) {

				StringBuilder checkPrinterNameQuery = new StringBuilder("SELECT COUNT(*) AS COUNT FROM ").append(schema).append(".O_PRINTER ");
				checkPrinterNameQuery.append(" WHERE (ID <> ").append(updatedAdminPrinterDetails.getPrinterId()).append(" AND  UPPER(NAME) = ").append("UPPER('"+updatedAdminPrinterDetails.getPrinterName()+"') )");
				checkPrinterNameQuery.append("OR (ID <> ").append(updatedAdminPrinterDetails.getPrinterId()).append(" AND UPPER(AP_ID) = ").append("UPPER('"+updatedAdminPrinterDetails.getAlphaPlusPrinterId()).append("'))");

				String count = dbServiceRepository.getCountUsingQuery(checkPrinterNameQuery.toString(), con);

				if(Integer.parseInt(count) > 0) {
					log.info("Duplicate Printer Name OR alphaPlus Printer Id");

					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.UNIQUE_CONSTRAINT_FAILED_MSG_KEY, "Printer Name and Alpahplus Printer ID"));
					log.error(messageService.getReadableMessage(ExceptionMessages.UNIQUE_CONSTRAINT_FAILED_MSG_KEY, "Printer Name and Alpahplus Printer ID"));
					throw exception;				
				}
				

				StringBuilder query = new StringBuilder("UPDATE ").append(schema).append(".O_PRINTER SET ");
				query.append("NAME = ").append("'" + updatedAdminPrinterDetails.getPrinterName() + "'").append(", AP_ID = ").append("'" + updatedAdminPrinterDetails.getAlphaPlusPrinterId() + "'");
				query.append(", DESCRIPTION = ").append("'" + updatedAdminPrinterDetails.getPrinterDescription() + "'");
				query.append(", UPDATED_BY = ").append("'" + loginUserName + "'");
				query.append(", UPDATED_TS = CURRENT TIMESTAMP");
				query.append(" WHERE ID = ").append(updatedAdminPrinterDetails.getPrinterId());

				 dbServiceRepository.updateResultUsingQuery(query.toString());

				if (updatedAdminPrinterDetails.getPrinterFKTList() != null
						&& !updatedAdminPrinterDetails.getPrinterFKTList().isEmpty()) {

					for (AdminPrinterFKTDTO details : updatedAdminPrinterDetails.getPrinterFKTList()) {

						StringBuilder query1 = new StringBuilder("UPDATE ").append(schema).append(".O_PRTHSFKT SET ");
						query1.append("HAS_FKT = ").append(details.isHasActiveFKT() ? 1 : 0);
						query1.append(" where PRINTER_ID = ").append(updatedAdminPrinterDetails.getPrinterId());
						query1.append(" AND FKT_ID = ").append(details.getFktId());
						
						log.info("Update SQL :" + query1.toString());
						stmt.addBatch(query1.toString());
					}
					
					int[] records = dbServiceRepository.insertResultUsingBatchQuery(stmt);
					if (records != null) {
						log.info("In O_PRINTER - Total rows Inserted  :" + records.length);

					}

				}
				programOutput.put("isUpdated", true);
				con.setAutoCommit(true);
				con.commit();
			}
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,
					messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Admin printer Details"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY,
					"Admin printer Details"), exception);
			throw exception;
		}

		return programOutput;

	}

	/**
	 * This method is used to Return Admin Printer Type List from DB
	 * @return Object
	 */
    @Override
	public List<DropdownObject> getPrinterTypeList(String schema, String dataLibrary) {
		// TODO Auto-generated method stub
		log.info("Inside getPrinterList method of AdminPrinterSettingServiceImpl");

		List<DropdownObject> printerTypeList = new ArrayList<>();
		try {
			StringBuilder query = new StringBuilder("SELECT FKT_ID , FKT from ").append(schema).append(".O_PRTFKT");
			List<AdminPrinterFKT> printerAdminTypeList = dbServiceRepository.getResultUsingQuery(AdminPrinterFKT.class,
					query.toString(), true);

			if (printerAdminTypeList != null && !printerAdminTypeList.isEmpty()) {

				for (AdminPrinterFKT adminPrinterFKT : printerAdminTypeList) {
					DropdownObject dropdownObject = new DropdownObject();

					dropdownObject.setKey(String.valueOf(adminPrinterFKT.getFktId()));
					dropdownObject.setValue(String.valueOf(adminPrinterFKT.getFktValue()));
					printerTypeList.add(dropdownObject);
				}
			}

			else {
				log.info("Printer Type List is empty");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(
						LocaleContextHolder.getLocale(), ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Admin Printer"));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Admin Printer"));
				throw exception;
			}
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,
					messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Admin Printer Type list"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Admin Printer Type list"), exception);
			throw exception;
		}
		return printerTypeList;
	}
		
}
	
	
	
