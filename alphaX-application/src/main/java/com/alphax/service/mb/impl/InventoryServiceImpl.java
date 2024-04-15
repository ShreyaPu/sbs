package com.alphax.service.mb.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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

import com.alphax.common.rest.message.service.MessageService;
import com.alphax.model.mb.InventoryList;
import com.alphax.model.mb.InventoryParts;
import com.alphax.model.mb.PartDetails;
import com.alphax.model.mb.StorageLocation;
import com.alphax.common.exception.AlphaXException;
import com.alphax.common.exception.ExceptionMessages;
import com.alphax.repository.CobolServiceRepository;
import com.alphax.repository.DBServiceRepository;
import com.alphax.repository.StubServiceRepository;
import com.alphax.service.mb.InventoryService;
import com.alphax.vo.mb.ActivatedCountingListDTO;
import com.alphax.vo.mb.AddCountedPartDTO;
import com.alphax.vo.mb.CountingGroupDTO;
import com.alphax.vo.mb.CreateNewCountingListDTO;
import com.alphax.vo.mb.CreatedCountingDetailListDTO;
import com.alphax.vo.mb.CreatedCountingListDTO;
import com.alphax.vo.mb.CreatedCountingListValues;
import com.alphax.vo.mb.DropdownObject;
import com.alphax.vo.mb.FinalizationsBA_DTO;
import com.alphax.vo.mb.GlobalSearch;
import com.alphax.vo.mb.InventoryDTO;
import com.alphax.vo.mb.InventoryDiferentialListDTO;
import com.alphax.vo.mb.PartsBrandDTO;
import com.alphax.vo.mb.StorageLocationDTO;
import com.alphax.common.constants.Program_Commands_Constants;
import com.alphax.common.constants.RestInputConstants;
import com.ibm.as400.access.ProgramParameter;

import lombok.extern.slf4j.Slf4j;
/**
 * @author A87740971
 *
 */
@Service
@Slf4j
public class InventoryServiceImpl extends BaseService implements InventoryService {

	@Autowired
	DBServiceRepository dbServiceRepository;

	@Autowired
	private MessageService messageService;

	@Autowired
	StubServiceRepository stubServiceRepository;

	@Autowired
	CobolServiceRepository cobolServiceRepository;

	@Autowired
	BusinessCasesImpl businessCasesServiceImpl;

	DecimalFormat decimalformat_twodigit = new DecimalFormat("#0.00", new DecimalFormatSymbols(Locale.ENGLISH));
	
	
	/**
	 * Inventur Übersicht - (Inventory Overview Counts)
	 */	
	@Override
	public Map<String, String> getInventoryOverviewCounts(String dataLibrary, String schema, 
			String warehouseNo, String allowedApWarehouse, String manufacturer) {

		log.info("Inside getInventoryOverviewCounts method of InventoryServiceImpl");

		//validate the Warehouse Ids
		validateWarehouses(allowedApWarehouse);
		String count = "0";
		Map<String,String> resultCounts = new HashMap<String, String>();
		StringBuilder warehouseClouse = new StringBuilder("");

		try {

			if(warehouseNo.equalsIgnoreCase("Alle")){
				warehouseClouse.append(" LNR IN (").append(allowedApWarehouse).append(")");

			}else{
				warehouseClouse.append(" LNR IN (").append(warehouseNo).append(")");
			}

			if(!manufacturer.equalsIgnoreCase("Alle")){
				manufacturer = (manufacturer != null && manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING))? RestInputConstants.DCAG_STRING : manufacturer;
				warehouseClouse.append(" AND HERST = '").append(manufacturer).append("'");
			}

			//part kind "Satzart 1" (SA = 1).

			//Anzahl aller Positionen - (Number of parts)
			StringBuilder SA1_query_Anzahl_Teile  = new StringBuilder(" select count(*) AS COUNT from ").append(dataLibrary);
			SA1_query_Anzahl_Teile.append(".E_ETSTAMM where SA = 1 AND ").append(warehouseClouse);

			//Anzahl gezählte Teile - (Number of parts counted)
			StringBuilder SA1_query_Anzahl_gezahlte_Teile  = new StringBuilder(" select count(*) AS COUNT from ").append(dataLibrary);
			SA1_query_Anzahl_gezahlte_Teile.append(".E_ETSTAMM where SA = 1 AND IVKZ != '' AND ").append(warehouseClouse);

			//Anzahl ungezählte Teile - (Number of uncounted parts)
			StringBuilder SA1_query_Anzahl_ungezahlte_Teile = new StringBuilder("select count(*) AS COUNT from ").append(dataLibrary);
			SA1_query_Anzahl_ungezahlte_Teile.append(".E_ETSTAMM where  SA = 1 AND IVKZ = '' AND ").append(warehouseClouse);

			//part kind "Satzart  2" (SA = 2).

			//Anzahl aller Positionen - (Number of parts)
			StringBuilder SA2_query_Anzahl_Teile  = new StringBuilder(" select count(*) AS COUNT from ").append(dataLibrary);
			SA2_query_Anzahl_Teile.append(".E_ETSTAMM where SA = 2 AND AKTBES > 0  AND ").append(warehouseClouse);

			//Anzahl gezählte Teile - (Number of parts counted)
			StringBuilder SA2_query_Anzahl_gezahlte_Teile  = new StringBuilder(" select count(*) AS COUNT from ").append(dataLibrary);
			SA2_query_Anzahl_gezahlte_Teile.append(".E_ETSTAMM where SA = 2 AND AKTBES > 0 AND IVKZ != '' AND ").append(warehouseClouse);

			//Anzahl ungezählte Teile - (Number of uncounted parts)
			StringBuilder SA2_query_Anzahl_ungezahlte_Teile = new StringBuilder("select count(*) AS COUNT from ").append(dataLibrary);
			SA2_query_Anzahl_ungezahlte_Teile.append(".E_ETSTAMM where  SA = 2 AND AKTBES > 0 AND IVKZ = '' AND ").append(warehouseClouse);


			//SA = 1
			count = dbServiceRepository.getCountUsingQuery(SA1_query_Anzahl_Teile.toString());
			resultCounts.put("SA1_Anzahl_aller_Positionen", count);

			count = dbServiceRepository.getCountUsingQuery(SA1_query_Anzahl_gezahlte_Teile.toString());
			resultCounts.put("SA1_Anzahl_gezahlte_Teile", count);

			count = dbServiceRepository.getCountUsingQuery(SA1_query_Anzahl_ungezahlte_Teile.toString());
			resultCounts.put("SA1_Anzahl_ungezahlte_Teile", count);

			// SA = 2 
			count = dbServiceRepository.getCountUsingQuery(SA2_query_Anzahl_Teile.toString());
			resultCounts.put("SA2_Anzahl_aller_Positionen", count);

			count = dbServiceRepository.getCountUsingQuery(SA2_query_Anzahl_gezahlte_Teile.toString());
			resultCounts.put("SA2_Anzahl_gezahlte_Teile", count);

			count = dbServiceRepository.getCountUsingQuery(SA2_query_Anzahl_ungezahlte_Teile.toString());
			resultCounts.put("SA2_Anzahl_ungezahlte_Teile", count);



		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Inventory overview counts"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Inventory overview counts"), exception);
			throw exception;
		}

		return resultCounts;
	}


	@Override
	public List<DropdownObject> getDisplayListForStartedInventory() {
		log.info("Inside getDisplayListForStartedInventory method of InventoryServiceImpl");

		List<DropdownObject> displayListForStartedInventory = new ArrayList<>();
		displayListForStartedInventory = stubServiceRepository.getDDLValuesFromStub(StubServiceRepository.displayForStartedInventory);
		return displayListForStartedInventory;
	}
	
	
	/**
	 * This is method used to start inventory for an existing counting list from COBOL Program.
	 */
	@Override
	public Map<String, String> startInventoryForExistingCountingList(String warehouseNo) {
		log.info("Inside startInventoryForExistingCountingList method of InventoryServiceImpl");

		Map<String, String> programOutput = new HashMap<String, String>();

		try{

			validateWarehouses(warehouseNo);
			ProgramParameter[] parmList = new ProgramParameter[3];

			// Create the input parameters 
			String returnCode = StringUtils.rightPad("", 5, " ");
			parmList[0] = new ProgramParameter(returnCode.getBytes(Program_Commands_Constants.IBM_273), 5);

			String returnMsg  = StringUtils.rightPad("", 100, " ");
			parmList[1] = new ProgramParameter(returnMsg.getBytes(Program_Commands_Constants.IBM_273), 100);

			String warehouseNumber = StringUtils.leftPad(warehouseNo, 5, "0");
			parmList[2] = new ProgramParameter(warehouseNumber.getBytes(Program_Commands_Constants.IBM_273), 5);


			//execute the COBOL program - OINVSTRCL.PGM
			List<String> outputList = cobolServiceRepository.executeProgram(parmList, 0, Program_Commands_Constants.START_INVENTORY_FOR_EXISTING_COUNTING_LIST );

			if (outputList != null && !outputList.isEmpty()) {
				if (!outputList.get(0).contains("Error")) {

					programOutput.put("returnCode", outputList.get(0).trim());
					programOutput.put("returnMsg", outputList.get(1).trim());
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.CREATE_FAILED_MSG_KEY,"Start inventory for an existing counting list "));
			log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY,"Start inventory for an existing counting list"), exception);
			throw exception;
		}
		return programOutput;
	}

	/**
	 * This is method used to get warehouse list with status.
	 */
	@Override
	public List<DropdownObject> getWarehouseListWithStatus(String dataLibrary,String schema,String allowedApWarehouse) {
		log.info("Inside startInventoryForExistingCountingList method of InventoryServiceImpl");

		List<DropdownObject> warehouseList = new ArrayList<>();

		try{

			validateWarehouses(allowedApWarehouse);

			StringBuilder queryForInvtType = new StringBuilder("SELECT CONCAT( CONCAT( SUBSTRING (VFNR,4,2 ) , '-') , (SELECT NAME FROM ");
			queryForInvtType.append(schema).append(".O_WRH WHERE AP_WAREHOUS_ID = LTRIM(VFNR,'0'))  ) AS KEYFLD, SUBSTR(DATEN, 106, 1) AS DATAFLD  FROM ");
			queryForInvtType.append(dataLibrary).append(".E_ETSTAMK3 WHERE SUBSTRING (VFNR,4,2 ) IN(").append(allowedApWarehouse).append(")");

			Map<String, String> warehouseKeyValue = dbServiceRepository.getResultUsingCobolQuery(queryForInvtType.toString());

			if(warehouseKeyValue != null && !warehouseKeyValue.isEmpty()) {
				for(Map.Entry<String, String> wrhDetails : warehouseKeyValue.entrySet()) {

					DropdownObject dropdownObject = new DropdownObject();
					dropdownObject.setKey(wrhDetails.getKey());
					dropdownObject.setValue(wrhDetails.getValue());

					warehouseList.add(dropdownObject);
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Warehouse List"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Warehouse List"), exception);
			throw exception;
		}
		return warehouseList;
	}


	/**
	 * This method is used to Submit the Inventory Count from COBOL Program.
	 */
	@Override
	public Map<String, String> submitInventoryCount(String warehouseNo,String manufacturer,String partNo,String pageNo,String countValue,String buffer) {
		log.info("Inside submitInventoryCount method of InventoryServiceImpl");

		Map<String, String> programOutput = new HashMap<String, String>();

		try{

			validateWarehouses(warehouseNo);
			ProgramParameter[] parmList = new ProgramParameter[8];

			// Create the input parameters 
			String returnCode = StringUtils.rightPad("", 5, " ");
			parmList[0] = new ProgramParameter(returnCode.getBytes(Program_Commands_Constants.IBM_273), 5);

			String returnMsg  = StringUtils.rightPad("", 100, " ");
			parmList[1] = new ProgramParameter(returnMsg.getBytes(Program_Commands_Constants.IBM_273), 100);

			String warehouseNumber = StringUtils.leftPad(warehouseNo, 2, "0");
			parmList[2] = new ProgramParameter(warehouseNumber.getBytes(Program_Commands_Constants.IBM_273), 2);

			String manufacturer1 = StringUtils.rightPad(manufacturer, 4, " ");
			parmList[3] = new ProgramParameter(manufacturer1.getBytes(Program_Commands_Constants.IBM_273), 4);

			String partNumber = StringUtils.rightPad(partNo, 16, " ");
			parmList[4] = new ProgramParameter(partNumber.getBytes(Program_Commands_Constants.IBM_273), 16);

			String pageNumber = StringUtils.leftPad(pageNo, 6, "0");
			parmList[5] = new ProgramParameter(pageNumber.getBytes(Program_Commands_Constants.IBM_273), 6);

			String countValue1 = StringUtils.leftPad(countValue, 10, "0");
			parmList[6] = new ProgramParameter(countValue1.getBytes(Program_Commands_Constants.IBM_273), 10);

			String buffer1 = StringUtils.rightPad(buffer, 10, " ");
			parmList[7] = new ProgramParameter(buffer1.getBytes(Program_Commands_Constants.IBM_273), 10);

			//execute the COBOL program - OINVCNTCL.PGM
			List<String> outputList = cobolServiceRepository.executeProgram(parmList, 0, Program_Commands_Constants.SUBMIT_INVENTORY_COUNT );

			if (outputList != null && !outputList.isEmpty()) {
				if (!outputList.get(0).contains("Error")) {

					programOutput.put("returnCode", outputList.get(0).trim());
					programOutput.put("returnMsg", outputList.get(1).trim());
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.CREATE_FAILED_MSG_KEY,"Submit inventory Count "));
			log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY,"Submit inventory Count"), exception);
			throw exception;
		}
		return programOutput;
	}


	/**
	 * This method is used to close inventory for an existing counting list from COBOL Program.
	 */
	@Override
	public Map<String, String> closeInventory(String warehouseNo) {
		log.info("Inside closeInventory method of InventoryServiceImpl");

		Map<String, String> programOutput = new HashMap<String, String>();

		try{

			validateWarehouses(warehouseNo);
			ProgramParameter[] parmList = new ProgramParameter[3];

			// Create the input parameters 
			String returnCode = StringUtils.rightPad("", 5, " ");
			parmList[0] = new ProgramParameter(returnCode.getBytes(Program_Commands_Constants.IBM_273), 5);

			String returnMsg  = StringUtils.rightPad("", 100, " ");
			parmList[1] = new ProgramParameter(returnMsg.getBytes(Program_Commands_Constants.IBM_273), 100);

			String warehouseNumber = StringUtils.leftPad(warehouseNo, 5, "0");
			parmList[2] = new ProgramParameter(warehouseNumber.getBytes(Program_Commands_Constants.IBM_273), 5);


			//execute the COBOL program - OINVENDCL.PGM
			List<String> outputList = cobolServiceRepository.executeProgram(parmList, 0, Program_Commands_Constants.CLOSE_INVENTORY );

			if (outputList != null && !outputList.isEmpty()) {
				if (!outputList.get(0).contains("Error")) {

					programOutput.put("returnCode", outputList.get(0).trim());
					programOutput.put("returnMsg", outputList.get(1).trim());
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.CREATE_FAILED_MSG_KEY,"Close inventory for an existing counting list "));
			log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY,"Close inventory for an existing counting list"), exception);
			throw exception;
		}
		return programOutput;
	}


	/**
	 * This method is used to get all activated counting inventory List from O_INVLIST table.
	 */
	@Override
	public List<ActivatedCountingListDTO> getActivatedCountingList(String dataLibrary, String schema, String allowedWarehouses) {

		log.info("Inside getActivatedCountingList method of InventoryServiceImpl");

		List<ActivatedCountingListDTO> countingListDTOs = new ArrayList<ActivatedCountingListDTO>();

		try {			
			//validate the Warehouse Ids
			validateWarehouses(allowedWarehouses);

			StringBuilder queryForInvtList = new StringBuilder("Select distinct WAREHOUSE_ID, WAREHOUSE_NAME, (Select COUNT(*) as COUNT from ");
			queryForInvtList.append(schema).append(".O_INVLIST where INVLISTSTS_ID > 0 and INVLISTSTS_ID < 6 and WAREHOUSE_ID = inv.WAREHOUSE_ID ) from ");
			queryForInvtList.append(schema).append(".O_INVLIST inv where inv.INVLISTSTS_ID > 0 and inv.INVLISTSTS_ID < 6 ");
			queryForInvtList.append(" and WAREHOUSE_ID IN(").append(allowedWarehouses).append(") order by WAREHOUSE_ID");
			
			List<InventoryList> inventoryListDetails = dbServiceRepository.getResultUsingQuery(InventoryList.class, queryForInvtList.toString(), true);

			if (inventoryListDetails != null && !inventoryListDetails.isEmpty()) {
				for(InventoryList inventoryList : inventoryListDetails){

					ActivatedCountingListDTO listDTO = new ActivatedCountingListDTO();

					listDTO.setWarehouseId(inventoryList.getWarehouseId().toString());
					listDTO.setWarehouseName(inventoryList.getWarehouseName());
					listDTO.setInventoryCount(inventoryList.getInventoryCount().toString());
					countingListDTOs.add(listDTO);
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(
					LocaleContextHolder.getLocale(), ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Activated counting list"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Activated counting list"),exception);
			throw exception;
		}

		return countingListDTOs;
	}


	/**
	 * This method is used to get selected activated counting inventory List details from O_INVLIST table.
	 */
	@Override
	public GlobalSearch getActivatedCountingDetailList(String dataLibrary, String schema, String warehouseNo, String pageSize, String pageNumber,
			String sortingBy, String sortingType, List<String> invStatusIds) {

		log.info("Inside getActivatedCountingDetailList method of InventoryServiceImpl");

		GlobalSearch globalSearchList = new GlobalSearch();
		List<ActivatedCountingListDTO> activatedCountingDTOList = new ArrayList<ActivatedCountingListDTO>();

		if(pageSize==null || pageNumber==null || pageSize.isEmpty() || pageNumber.isEmpty()){
			pageSize = RestInputConstants.DEFAULT_PAGE_SIZE;
			pageNumber = RestInputConstants.DEFAULT_PAGE_NO;
		}

		int totalRecords = Integer.parseInt(pageSize);
		int nextRows = totalRecords * (Integer.parseInt(pageNumber) - 1);

		//validate the page size
		validatePageSize(totalRecords);
		log.info("PageSize  : {}  OFFSET Size (Skip Rows):  {} Page Number  : {} ", pageSize, nextRows, pageNumber);

		try {
			String orderByClause = getOrderByClause(sortingType, sortingBy);
			String allowedInvStatusIds = null;
			if(invStatusIds != null && !invStatusIds.isEmpty()){
				invStatusIds.removeIf(s -> s == null || "".equals(s.trim()));
				allowedInvStatusIds = StringUtils.join(invStatusIds, ",");
			}

			StringBuilder queryForInvtList = new StringBuilder("Select INVLIST_ID, NAME, VARCHAR_FORMAT(CREATED_TS, 'DD.MM.YYYY HH24:MI') AS CREATED_TIME, INVLISTSTS_ID, SORT_BY, ");
			queryForInvtList.append("(Select STATUS_DESC from ").append(schema).append(".O_INVST where INVLISTSTS_ID = x.INVLISTSTS_ID) as INVLISTSTS_DESC, ");
			queryForInvtList.append("(SELECT COUNT(*) from ");
			queryForInvtList.append(schema).append(".O_INVLIST x where INVLISTSTS_ID > 0 AND INVLISTSTS_ID < 5 AND WAREHOUSE_ID = ").append(warehouseNo);
			if(allowedInvStatusIds!=null){
				queryForInvtList.append( " AND INVLISTSTS_ID IN(").append(allowedInvStatusIds).append(")");
			}
			queryForInvtList.append(" ) AS ROWNUMER  from ");
			queryForInvtList.append(schema).append(".O_INVLIST x where INVLISTSTS_ID > 0 AND INVLISTSTS_ID < 5 AND WAREHOUSE_ID = ").append(warehouseNo);
			if(allowedInvStatusIds!=null){
				queryForInvtList.append( " AND INVLISTSTS_ID IN(").append(allowedInvStatusIds).append(")");
			}
			queryForInvtList.append(orderByClause).append(" OFFSET ");
			queryForInvtList.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");

			List<InventoryList> inventoryListDetails = dbServiceRepository.getResultUsingQuery(InventoryList.class, queryForInvtList.toString(), true);

			if (inventoryListDetails != null && !inventoryListDetails.isEmpty()) {
				for(InventoryList inventoryList : inventoryListDetails){

					ActivatedCountingListDTO listDTO = new ActivatedCountingListDTO();

					listDTO.setWarehouseId(warehouseNo);
					listDTO.setInventoryListId(inventoryList.getInventoryListId().toString());
					listDTO.setInventoryListName(inventoryList.getInventoryListName());

					listDTO.setCreationDate(inventoryList.getInvCreationDate() != null ? inventoryList.getInvCreationDate() : "");

					listDTO.setInventoryList_Status_Id(inventoryList.getInventoryList_Status_Id().toString());
					listDTO.setStatusDescription(inventoryList.getStatusDescription());

					//0: sort by TNR
					//1: sort by Storage location 
					listDTO.setSortBy(inventoryList.getSortBy() != null ? inventoryList.getSortBy().toString() : "0");

					activatedCountingDTOList.add(listDTO);
				}

				globalSearchList.setSearchDetailsList(activatedCountingDTOList);
				globalSearchList.setTotalPages(Integer.toString(inventoryListDetails.get(0).getTotalCount()));
				globalSearchList.setTotalRecordCnt(Integer.toString(inventoryListDetails.get(0).getTotalCount()));
			}	
			else {
				globalSearchList.setSearchDetailsList(activatedCountingDTOList);
				globalSearchList.setTotalPages(Integer.toString(0));
				globalSearchList.setTotalRecordCnt(Integer.toString(0));
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(
					LocaleContextHolder.getLocale(), ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Activated Counting list details"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Activated Counting list details"),exception);
			throw exception;
		}

		return globalSearchList;
	}


	/**
	 * This method is used to get created counting list from O_INVLIST table.
	 */
	@Override
	public List<CreatedCountingListValues> getCreatedCountingList(String dataLibrary, String schema, String warehouseNo, List<String> invTypeIds) {

		log.info("Inside getCreatedCountingList method of InventoryServiceImpl");
		List<CreatedCountingListValues> createdCountingListValues = new ArrayList<CreatedCountingListValues>();
		
		try {
					String allowedInvTypeIds = null;
					if(invTypeIds != null && !invTypeIds.isEmpty()){
						invTypeIds.removeIf(s -> s == null || "".equals(s.trim()));
						allowedInvTypeIds = StringUtils.join(invTypeIds, ",");
					}

					StringBuilder createdCountingListQuery = new StringBuilder("Select INVLIST_ID , NAME, SORT_BY, (Select COUNT(*) as COUNT_SA1 from ");
					createdCountingListQuery.append(schema).append(".O_INVPART where SA = 1 and INVLIST_ID = inv.INVLIST_ID),  (Select COUNT(*) as COUNT_SA2 from ");
					createdCountingListQuery.append(schema).append(".O_INVPART where SA = 2 and INVLIST_ID = inv.INVLIST_ID), ");
					createdCountingListQuery.append(" VARCHAR_FORMAT(CREATED_TS, 'DD.MM.YYYY') AS CREATED_TIME, CREATED_BY  from ");
					createdCountingListQuery.append(schema).append(".O_INVLIST inv where INVLISTSTS_ID = 0 ");
					if(allowedInvTypeIds!= null) {
						createdCountingListQuery.append(" AND inv.INVLIST_ID IN (select  DISTINCT INVLIST_ID  from ").append(schema).append(".O_INVTYPE WHERE LISTTYPE_ID IN (");
						createdCountingListQuery.append(allowedInvTypeIds).append("))");
					}
					createdCountingListQuery.append(" and WAREHOUSE_ID =").append(warehouseNo).append(" order by CREATED_TS  ");

					List<InventoryList> inventoryListValues = dbServiceRepository.getResultUsingQuery(InventoryList.class, createdCountingListQuery.toString(), true);

					if (inventoryListValues != null && !inventoryListValues.isEmpty()) {
						for(InventoryList inventoryValue : inventoryListValues){

							CreatedCountingListValues listValue = new CreatedCountingListValues();

							listValue.setInventoryListId(String.valueOf(inventoryValue.getInventoryListId()));
							listValue.setInventoryListName(inventoryValue.getInventoryListName());
							listValue.setNumberOfRrecordType1(inventoryValue.getNumberOfRrecordType1());
							listValue.setNumberOfRrecordType2(inventoryValue.getNumberOfRrecordType2());
							if(String.valueOf(inventoryValue.getSortBy()).equalsIgnoreCase("0")){
								listValue.setSortBy("TNRS"); 
							}else{
								listValue.setSortBy("PLACE");  
							}

							listValue.setCreationDate(inventoryValue.getInvCreationDate());
							listValue.setCreateBy(inventoryValue.getCreatedBy());

							createdCountingListValues.add(listValue);
						}
					}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(
					LocaleContextHolder.getLocale(), ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Created Counting list"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Created Counting list"),exception);
			throw exception;
		}

		return createdCountingListValues;
	}


	/**
	 * This method is used to get created counting list details from O_INVPART table.
	 */
	@Override
	public GlobalSearch getCreatedCountingDetailList(String dataLibrary, String schema, String inventoryListId, String orderBy, String pageSize, String pageNumber) {

		log.info("Inside getCreatedCountingDetailList method of InventoryServiceImpl");
		GlobalSearch globalSearchList = new GlobalSearch();
		List<CreatedCountingDetailListDTO> partListDTOs = new ArrayList<CreatedCountingDetailListDTO>();

		try {

			//Checking status of the counting list if changed in the meantime then page will reload.
			genericInventoryStatusCheck(schema,inventoryListId,"0"); // 0 = inventory list status Id

			if(pageSize==null || pageNumber==null || pageSize.isEmpty() || pageNumber.isEmpty()){
				pageSize = RestInputConstants.DEFAULT_PAGE_SIZE;
				pageNumber = RestInputConstants.DEFAULT_PAGE_NO;
			}

			int totalRecords = Integer.parseInt(pageSize);
			int nextRows = totalRecords * (Integer.parseInt(pageNumber) - 1);

			//validate the page size
			validatePageSize(totalRecords);
			log.info("PageSize  : {}  OFFSET Size (Skip Rows):  {} Page Number  : {} ", pageSize, nextRows, pageNumber);

			StringBuilder partList = new StringBuilder("SELECT OEM, TNR, PARTNAME, PLACE, ID as invpart_id, COUNT_OF_PLACES, (Select count(*) FROM ");
			partList.append(schema).append(".O_INVPART where INVLIST_ID = ").append(inventoryListId).append(") AS ROWNUMER FROM ");
			partList.append(schema).append(".O_INVPART where INVLIST_ID = ").append(inventoryListId).append(" ORDER BY ").append(orderBy);
			partList.append(" OFFSET ").append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");

			List<InventoryParts> inventoryParts = dbServiceRepository.getResultUsingQuery(InventoryParts.class, partList.toString(), true);

			if (inventoryParts != null && !inventoryParts.isEmpty()) {
				for (InventoryParts invParts : inventoryParts) {

					CreatedCountingDetailListDTO detailListDTO = new CreatedCountingDetailListDTO();
					detailListDTO.setManufacturer(invParts.getManufacturer());
					detailListDTO.setPartNumber(invParts.getPartNumber());
					detailListDTO.setPartName(invParts.getPartNAME());
					detailListDTO.setPlace(invParts.getPlace());
					detailListDTO.setInventoryPartId(String.valueOf(invParts.getInventoryPartId()));
					detailListDTO.setCountOfPlaces(String.valueOf(invParts.getCountOfPlaces()));
					partListDTOs.add(detailListDTO);

				}

				globalSearchList.setSearchDetailsList(partListDTOs);
				globalSearchList.setTotalPages(Integer.toString(inventoryParts.get(0).getTotalCount()));
				globalSearchList.setTotalRecordCnt(Integer.toString(inventoryParts.get(0).getTotalCount()));
			}else{
				globalSearchList.setSearchDetailsList(partListDTOs);
				globalSearchList.setTotalPages(Integer.toString(0));
				globalSearchList.setTotalRecordCnt(Integer.toString(0));
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(
					LocaleContextHolder.getLocale(), ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Created Counting details list"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Created Counting details list"),exception);
			throw exception;
		}

		return globalSearchList;
	}


	
	/**
	 * This method is used to get the LOPA (Storage Location) details based on provided manufacturer.
	 */
	@Override
	public GlobalSearch getStorageLocationList(String dataLibrary, String schema, String manufacturer, String pageSize, String pageNumber,String searchString) {

		log.info("Inside getStorageLocationList method of InventoryServiceImpl");

		List<StorageLocationDTO> storageLocationDTOList = new ArrayList<>();
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

			manufacturer = manufacturer.equalsIgnoreCase(RestInputConstants.DCAG_STRING)? RestInputConstants.DAG_STRING: manufacturer;

			StringBuilder query = new StringBuilder("Select DISTINCT(LOPA) from  ");
			query.append(dataLibrary).append(".E_LAGO  inner join ").append(schema).append(".PMH_HERST on HERST = HER_HERST WHERE  HER_HBEZKU = '").append(manufacturer);
			query.append("' AND UPPER(LOPA) like UPPER('").append(searchString).append("%') UNION  ");
			query.append("Select DISTINCT(LOPA) from ");
			query.append(dataLibrary).append(".E_ETSTAMM  inner join ").append(schema).append(".PMH_HERST on HERST = HER_HERST WHERE  HER_HBEZKU = '");
			query.append(manufacturer).append("' AND UPPER(LOPA) like UPPER('").append(searchString).append("%')  ORDER BY LOPA ASC ");
			query.append(" OFFSET ").append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");

			List<StorageLocation> lopaDetailList = dbServiceRepository.getResultUsingQuery(StorageLocation.class, query.toString(), true);


			StringBuilder queryCount = new StringBuilder("Select count(*) AS count from ( Select DISTINCT(LOPA) from  ");
			queryCount.append(dataLibrary).append(".E_LAGO  inner join ").append(schema).append(".PMH_HERST on HERST = HER_HERST WHERE  HER_HBEZKU = '").append(manufacturer);
			queryCount.append("' AND UPPER(LOPA) like UPPER('").append(searchString).append("%') UNION  ");
			queryCount.append("Select DISTINCT(LOPA) from ");
			queryCount.append(dataLibrary).append(".E_ETSTAMM  inner join ").append(schema).append(".PMH_HERST on HERST = HER_HERST WHERE  HER_HBEZKU = '").append(manufacturer);
			queryCount.append("' AND UPPER(LOPA) like UPPER('").append(searchString).append("%') ) ");

			String totalCount = dbServiceRepository.getCountUsingQuery(queryCount.toString());

			//if the list is not empty
			if (lopaDetailList != null && !lopaDetailList.isEmpty()) {

				storageLocationDTOList = setValueFromLopaDetailList(lopaDetailList);

				globalSearchList.setSearchDetailsList(storageLocationDTOList);
				globalSearchList.setTotalPages(totalCount);
				globalSearchList.setTotalRecordCnt(totalCount);

			} else {
				globalSearchList.setSearchDetailsList(storageLocationDTOList);
				globalSearchList.setTotalPages(Integer.toString(0));
				globalSearchList.setTotalRecordCnt(Integer.toString(0));
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Storage Location (LOPA)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Storage Location (LOPA)"), exception);
			throw exception;
		}

		return globalSearchList;
	}


	
	private List<StorageLocationDTO> setValueFromLopaDetailList(List<StorageLocation> lopaDetailList) {

		List<StorageLocationDTO> storageLocationDTOList = new ArrayList<>();

		for (StorageLocation storageLocation : lopaDetailList) {

			StorageLocationDTO locationDTO = new StorageLocationDTO();

			locationDTO.setStorageLocation(storageLocation.getStorageLocation());
			storageLocationDTOList.add(locationDTO);
		}

		return storageLocationDTOList;
	}
	


	/**
	 * This method is used to get the Counting Group (Zählgruppe)  details based on provided manufacturer.
	 */
	@Override
	public List<CountingGroupDTO> getCountingGroupList(String dataLibrary, String schema, String manufacturer, String warehouseId) {

		log.info("Inside getCountingGroupList method of InventoryServiceImpl");

		List<CountingGroupDTO> countingGroupDTOs = new ArrayList<>();

		try {

			manufacturer = manufacturer.equalsIgnoreCase(RestInputConstants.DCAG_STRING)? RestInputConstants.DAG_STRING: manufacturer;

			StringBuilder query = new StringBuilder("Select ZAEGRU FROM ").append(dataLibrary).append(".E_ETSTAMM  inner join ");
			query.append(schema).append(".PMH_HERST on HERST = HER_HERST WHERE  ZAEGRU <>  '' "); 
			query.append(" AND  LNR = ").append(warehouseId).append(" AND  HER_HBEZKU = '").append(manufacturer);
			query.append("' group by  ZAEGRU  ");

			List<PartDetails> countingGroupList = dbServiceRepository.getResultUsingQuery(PartDetails.class, query.toString(), true);

			//if the list is not empty
			if (countingGroupList != null && !countingGroupList.isEmpty()) {

				for (PartDetails countingGroup : countingGroupList) {
					CountingGroupDTO countingGroupDTO = new CountingGroupDTO();

					countingGroupDTO.setCountingGroup(countingGroup.getInventoryGroup());

					countingGroupDTOs.add(countingGroupDTO);
				}
			}
			else {
				log.debug("Counting Group List is empty");
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Counting Group (Zählgruppe)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Counting Group (Zählgruppe)"), exception);
			throw exception;
		}

		return countingGroupDTOs;
	}


	/**
	 * This method is used to get the parts brand (Teilemarke) list based on provided manufacturer.
	 */
	@Override
	public List<PartsBrandDTO> getPartsBrandList(String dataLibrary, String schema, String manufacturer) {

		log.info("Inside getPartsBrandList method of InventoryServiceImpl");

		List<PartsBrandDTO> partsBrandDTOs = new ArrayList<>();

		try {

			manufacturer = manufacturer.equalsIgnoreCase(RestInputConstants.DCAG_STRING)? RestInputConstants.DAG_STRING: manufacturer;

			StringBuilder query = new StringBuilder("select TMA_TMARKE from ").append(schema).append(".PMH_TMARKE inner join ");
			query.append(schema).append(".PMH_HERST on TMA_HERST = HER_HERST WHERE  HER_HBEZKU  = '"); 
			query.append(manufacturer).append("' order by TMA_TMARKE asc ");

			List<InventoryParts> partsBrandList = dbServiceRepository.getResultUsingQuery(InventoryParts.class, query.toString(), true);

			//if the list is not empty
			if (partsBrandList != null && !partsBrandList.isEmpty()) {

				for (InventoryParts partBrand : partsBrandList) {
					PartsBrandDTO partsBrandDTO = new PartsBrandDTO();

					partsBrandDTO.setPartBrand(partBrand.getPartBrand());

					partsBrandDTOs.add(partsBrandDTO);
				}
			}
			else {
				log.debug("Parts brand List is empty");
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Parts brand (Teilemarke)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Parts brand (Teilemarke)"), exception);
			throw exception;
		}

		return partsBrandDTOs;
	}


	/**
	 * This method is used to get the Sorting and print selection (Sortierung und Druckauswahl) list from Stub
	 * @return List
	 */
	@Override
	public List<DropdownObject> getSortingAndPrintSelectionList() {
		log.info("Inside getSortingAndPrintSelectionList method of InventoryServiceImpl");

		List<DropdownObject> mehrwertsteuerList = new ArrayList<>();
		mehrwertsteuerList = stubServiceRepository.getDDLValuesFromStub(StubServiceRepository.sortingAndPrintSelection);
		return mehrwertsteuerList;
	}


	/**
	 * This method is used the activate the inventory counting list based on provided inventory List id.
	 */
	@Override
	public Map<String, Boolean> activateCountingList(String dataLibrary, String schema, String inventoryListId) {

		log.info("Inside activateCountingList method of InventoryServiceImpl");

		Map<String, Boolean> procedureOutput = new HashMap<String, Boolean>();
		procedureOutput.put("isUpdated", false);
		boolean statusFlag = false;

		try(Connection con = dbServiceRepository.getConnectionObject();) {

			//update the inventory Status to 1 in INVLIST
			statusFlag = updateInventoryStatus(con, schema, inventoryListId);

			if(statusFlag) {

				StringBuilder query_for_procedure  = new StringBuilder(" BEGIN DECLARE X INTEGER; ");
				query_for_procedure.append(" DECLARE XLNR INTEGER; ");
				query_for_procedure.append(" DECLARE XDAK NUMERIC (15, 4); ");
				query_for_procedure.append(" DECLARE XBES NUMERIC (9, 2); ");
				query_for_procedure.append(" DECLARE SORT_BY NUMERIC (2); ");
				query_for_procedure.append(" SET X = 1; ");
				query_for_procedure.append(" SET XLNR = ( SELECT WAREHOUSE_ID FROM ").append(schema).append(".O_INVLIST WHERE INVLIST_ID =").append(inventoryListId).append(" ); "); 
				query_for_procedure.append(" SET SORT_BY = ( SELECT SORT_BY FROM ").append(schema).append(".O_INVLIST WHERE INVLIST_ID =").append(inventoryListId).append(" ); "); 
				query_for_procedure.append(" FOR v CURSOR FOR ");
				query_for_procedure.append(" SELECT ID AS XID, OEM AS XOEM, TNR AS XTNR, PLACE AS XPLACE FROM ").append(schema).append(".O_INVPART WHERE INVLIST_ID =");
				query_for_procedure.append(inventoryListId).append(" ORDER BY CASE WHEN SORT_BY = 0 THEN UPPER(TNRS) CONCAT ',' CONCAT UPPER(PLACE) END ,");
				query_for_procedure.append(" CASE WHEN SORT_BY = 1 THEN UPPER(PLACE) CONCAT ',' CONCAT UPPER(TNRS) END ");
				query_for_procedure.append(" DO ");
				query_for_procedure.append(" SET XDAK = ( SELECT DAK FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE LNR = XLNR AND TNR = XTNR AND HERST = XOEM ); ");
				query_for_procedure.append(" SET XBES = ( SELECT AKTBES FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE LNR = XLNR AND TNR = XTNR AND HERST = XOEM ); "); 
				query_for_procedure.append(" UPDATE ").append(schema).append(".O_INVPART SET CNT = X, DAK = XDAK, OLD_STOCK = XBES WHERE ID = XID ;");
				query_for_procedure.append(" SET X = X + 1; ");
				query_for_procedure.append(" END FOR;  END ");

				boolean executionFlag = dbServiceRepository.excuteProcedure(query_for_procedure.toString(), con);

				if(executionFlag) {
					procedureOutput.put("isUpdated", true);
				}
			}
			else {
				log.info("Inventory List Status is already set to 1");
			}
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Activate Inventory counting List"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Activate Inventory counting List"), exception);
			throw exception;
		}

		return procedureOutput;
	}


	/**
	 * This method is used to set the status of Inventory list.
	 * @param con
	 * @param schema
	 * @param inventoryListId
	 * @return
	 */
	private boolean updateInventoryStatus(Connection con, String schema, String inventoryListId) {

		Integer statusId = 0;
		boolean updateStatus = false;
		try {

			
			//ALPHAX-3820 / ALPHAX-3884 - Inventur - Zähllisten Aufbauen" Check for ACTIVATION_DATE by activating a list
			StringBuilder selectQuery = new StringBuilder("select  VARCHAR_FORMAT(ACTIVATION_DATE , 'YYYY-MM-DD HH24:MI') as ACTIVATION_DATE ,");
			selectQuery.append(" VARCHAR_FORMAT(current timestamp, 'YYYY-MM-DD HH24:MI') as CREATED_TS_NOW from ").append(schema).append(".O_INVLIST ");
			selectQuery.append(" where  ACTIVATION_DATE is not  NULL  order by  ACTIVATION_DATE  desc");
			
			List<InventoryList> invActivationDateList = dbServiceRepository.getResultUsingQuery(InventoryList.class, selectQuery.toString(), true);
	        
		    // ALPHAX-3820 /ALPHAX-3884  end	
			
			StringBuilder query = new StringBuilder("select INVLISTSTS_ID from ").append(schema).append(".O_INVLIST Where INVLIST_ID=").append(inventoryListId);
			
			List<InventoryList> inventoryStatusList = dbServiceRepository.getResultUsingQuery(InventoryList.class, query.toString(), true);
			
			if(inventoryStatusList!=null && !inventoryStatusList.isEmpty()) {
				for(InventoryList invList : inventoryStatusList) {
					statusId = invList.getInventoryList_Status_Id().intValue();
				}

				if(statusId == 0) {
					
					StringBuilder updateQuery = new StringBuilder("Update ").append(schema).append(".O_INVLIST SET ");
					
					if(invActivationDateList!=null && !invActivationDateList.isEmpty()) {
						
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
							Date activationDate = sdf.parse(invActivationDateList.get(0).getActivationDate());
							Date currentDate = sdf.parse(invActivationDateList.get(0).getCreationNOWDate());
							
						if(invActivationDateList.get(0).getActivationDate().equalsIgnoreCase(invActivationDateList.get(0).getCreationNOWDate())
								|| activationDate.after(currentDate) ) {
							log.info("Activation time of counting list is in same minut so increase time by 1 minuts");
							updateQuery.append("ACTIVATION_DATE = ").append(" TIMESTAMP((select  MAX(ACTIVATION_DATE ) from ").append(schema).append(".O_INVLIST)) + 60 SECONDS , ");	
						}else {
							updateQuery.append(" ACTIVATION_DATE = now(), ");
						}
					}else {
						updateQuery.append(" ACTIVATION_DATE = now(), ");
					}
					updateQuery.append(" INVLISTSTS_ID = 1  where INVLIST_ID =").append(inventoryListId);
					
					boolean updateFlag = dbServiceRepository.updateResultUsingQuery(updateQuery.toString(), con);

					if (updateFlag) {
						updateStatus = true;
					}
					else {
						log.info("Unable to update Status Id in O_INVLIST for this Id : {}", inventoryListId);
						AlphaXException exception = new AlphaXException(messageService.createApiMessage(
								LocaleContextHolder.getLocale(), ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "InventoryList"));
						log.error(messageService.getReadableMessage(ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "InventoryList"));
						throw exception;
					}

				}else{

					log.info("The status of the counting list has been changed in the meantime: {}", inventoryListId);
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(
							LocaleContextHolder.getLocale(), ExceptionMessages.COUNTING_LIST_STATUS_FAILED_MSG_KEY));
					log.error(messageService.getReadableMessage(ExceptionMessages.COUNTING_LIST_STATUS_FAILED_MSG_KEY));
					throw exception;
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Inventory Status"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Inventory Status"), exception);
			throw exception;
		}

		return updateStatus;
	}


	/**
	 * This method is used the Update the inventory Status Id in the O_INVLIST table based on provided inventory List id.
	 */
	@Override
	public Map<String, Boolean> genericUpdateInventoryStatus(String schema, String inventoryListId, String statusId) {

		log.info("Inside genericUpdateInventoryStatus method of InventoryServiceImpl");

		Map<String, Boolean> queryOutput = new HashMap<String, Boolean>();
		queryOutput.put("isUpdated", false);

		try {

			StringBuilder updateQuery = new StringBuilder("Update ").append(schema).append(".O_INVLIST SET INVLISTSTS_ID = ").append(Integer.parseInt(statusId));
			updateQuery.append(" where INVLIST_ID =").append(inventoryListId);

			boolean updateFlag = dbServiceRepository.updateResultUsingQuery(updateQuery.toString());

			if (updateFlag) {
				queryOutput.put("isUpdated", true);
			}
			else {
				log.info("Unable to update Status Id in O_INVLIST for this Id : {}", inventoryListId);
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(
						LocaleContextHolder.getLocale(), ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "Inventory List"));
				log.error(messageService.getReadableMessage(ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "Inventory List"));
				throw exception;
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Inventory Status"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Inventory Status"), exception);
			throw exception;
		}

		return queryOutput;
	}


	/**
	 * This method is used to get the Ansicht (Opinion list) list from Stub
	 * @return List
	 */
	@Override
	public List<DropdownObject> getAnsichtList() {
		log.info("Inside getAnsichtList method of InventoryServiceImpl");

		List<DropdownObject> ansichtListList = new ArrayList<>();
		ansichtListList = stubServiceRepository.getDDLValuesFromStub(StubServiceRepository.ansichtMap);
		return ansichtListList;
	}


	/**
	 * This method is used to get differential list details from O_INVPART table.
	 */
	@Override
	public GlobalSearch getDifferentialList(String dataLibrary, String schema, String inventoryListId, String opinionValue, 
			String pageSize, String pageNumber, String sortingBy, String sortingType) {

		log.info("Inside getDifferentialList method of InventoryServiceImpl");
		GlobalSearch globalSearchList = new GlobalSearch();
		List<InventoryDiferentialListDTO> differntialDTOList = new ArrayList<InventoryDiferentialListDTO>();
		String sortType = "ASC";

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

			if(sortingType != null && sortingType.equalsIgnoreCase("Descending")) {
				sortType = "DESC";
			}

			StringBuilder differntialListQuery = new StringBuilder("Select distinct TNR, OEM, PARTNAME, OLD_STOCK, (Select sum(NEW_STOCK) from ");
			differntialListQuery.append(schema).append(".O_INVPART where INVLIST_ID = ").append(inventoryListId).append(" and (INVPARTSTS_ID = 2 OR INVPARTSTS_ID = 3) ");
			differntialListQuery.append(" and tnr = t.tnr and oem = t.oem ").append(") as SUM_NEW_STOCK, ");
			differntialListQuery.append("DAK, DELTA_PIECES, DELTA_CUR, ");

			differntialListQuery.append("case when t.INVPARTSTS_ID=3 ").append(" then  '1' ");
			differntialListQuery.append(" else ");
			differntialListQuery.append("case when t.Old_stock != ( select sum(NEW_STOCK) from ").append(schema).append(".O_INVPART where INVLIST_ID =");
			differntialListQuery.append(inventoryListId).append(" and tnr = t.tnr  and oem = t.oem ");

			differntialListQuery.append(" )  then  '0'  else  '1'  end  end as Check_it, INVPARTSTS_ID, INVLIST_ID, ");
			differntialListQuery.append("( Select count(*) from ");
			differntialListQuery.append("( Select distinct TNR, OEM, PARTNAME, OLD_STOCK, DAK, delta_pieces, delta_cur, INVLIST_ID from ");
			differntialListQuery.append(schema).append(".O_INVPART t where INVLIST_ID = ").append(inventoryListId).append(" AND INVPARTSTS_ID > 0 ");

			
			if(opinionValue.equalsIgnoreCase("1")) {
				differntialListQuery.append(" AND DELTA_PIECES = 0 ");
			}
			else if(opinionValue.equalsIgnoreCase("2")) {
				differntialListQuery.append(" AND NOT DELTA_PIECES = 0 ");
			}	

			differntialListQuery.append(" )) AS ROWNUMER from ");
			differntialListQuery.append(schema).append(".O_INVPART t where INVLIST_ID = ").append(inventoryListId).append(" AND INVPARTSTS_ID > 0 ");

			
			if(opinionValue.equalsIgnoreCase("1")) {
				differntialListQuery.append(" AND DELTA_PIECES = 0 ");
			}
			else if(opinionValue.equalsIgnoreCase("2")) {
				differntialListQuery.append(" AND NOT DELTA_PIECES = 0 ");
			}

			differntialListQuery.append(" ORDER BY DELTA_CUR ").append(sortType);
			differntialListQuery.append(" OFFSET ").append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");

			List<InventoryParts> inventoryDiffParts = dbServiceRepository.getResultUsingQuery(InventoryParts.class, differntialListQuery.toString(), true);

			if (inventoryDiffParts != null && !inventoryDiffParts.isEmpty()) {
				for (InventoryParts invDiffParts : inventoryDiffParts) {

					InventoryDiferentialListDTO diffListDTO = new InventoryDiferentialListDTO();
					diffListDTO.setManufacturer(invDiffParts.getManufacturer().equalsIgnoreCase(RestInputConstants.DCAG_STRING) ? 
							RestInputConstants.DAG_STRING : invDiffParts.getManufacturer());
					diffListDTO.setPartNumber(invDiffParts.getPartNumber());
					diffListDTO.setPartName(invDiffParts.getPartNAME());
					diffListDTO.setInventoryPartStatusId(String.valueOf(invDiffParts.getPartStatusId()));

					diffListDTO.setCount(invDiffParts.getCount() != null ? String.valueOf(invDiffParts.getCount()) : "");
					diffListDTO.setOldStock(invDiffParts.getOldStock() != null ? String.valueOf(decimalformat_twodigit.format(invDiffParts.getOldStock())) : "");
					diffListDTO.setSum_newStock(invDiffParts.getSum_newStock() != null ? String.valueOf(decimalformat_twodigit.format(invDiffParts.getSum_newStock())) : "");
					diffListDTO.setDak(invDiffParts.getDak() != null ? String.valueOf(decimalformat_twodigit.format(invDiffParts.getDak())) : "");
					diffListDTO.setDeltaPieces(invDiffParts.getDeltaPieces()!=null ? String.valueOf(decimalformat_twodigit.format(invDiffParts.getDeltaPieces())) : "");
					diffListDTO.setDeltaCur(invDiffParts.getDeltaCur() != null ? String.valueOf(decimalformat_twodigit.format(invDiffParts.getDeltaCur())) : "");
					diffListDTO.setCheckItFlag(invDiffParts.getCheckIt() != null ? invDiffParts.getCheckIt() : "");
					diffListDTO.setInventoryPartId(String.valueOf(invDiffParts.getInventoryPartId()));
					diffListDTO.setInventoryListId(invDiffParts.getInventoryListId().toString());

					differntialDTOList.add(diffListDTO);
				}

				globalSearchList.setSearchDetailsList(differntialDTOList);
				globalSearchList.setTotalPages(Integer.toString(inventoryDiffParts.get(0).getTotalCount()));
				globalSearchList.setTotalRecordCnt(Integer.toString(inventoryDiffParts.get(0).getTotalCount()));
			}else{
				globalSearchList.setSearchDetailsList(differntialDTOList);
				globalSearchList.setTotalPages(Integer.toString(0));
				globalSearchList.setTotalRecordCnt(Integer.toString(0));
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(
					LocaleContextHolder.getLocale(), ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Created Counting details list"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Created Counting details list"),exception);
			throw exception;
		}

		return globalSearchList;
	}


	/**
	 * This method is used to create new Counted Part Manuelly into DB
	 * @return Object
	 */
	@Override
	public AddCountedPartDTO addCountedPartManuelly(AddCountedPartDTO part_obj, String schema, String dataLibrary) {

		log.info("Inside addCountedPartManuelly method of InventoryServiceImpl");
		String count;
		try {

			//Checking status of the counting list if changed in the meantime then page will reload.
			genericInventoryStatusCheck(schema,part_obj.getInventoryListId(),"3"); // 3 = inventory list status Id


			String manufacturer = part_obj.getManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING: part_obj.getManufacturer();

			StringBuilder checkCondition1 = new StringBuilder("SELECT COUNT(*) AS COUNT FROM ").append(schema).append(".O_INVPART ");
			checkCondition1.append(" WHERE OEM= '").append(manufacturer).append("' AND TNR = '").append(part_obj.getPartNumber());
			checkCondition1.append("' AND INVLIST_ID IN(SELECT INVLIST_ID FROM ").append(schema).append(".O_INVLIST WHERE ");
			checkCondition1.append(" INVLISTSTS_ID BETWEEN 0 AND 5 AND WAREHOUSE_ID = ").append(part_obj.getWarehouseId()).append(")");
				
			 count = dbServiceRepository.getCountUsingQuery(checkCondition1.toString());
			if(Integer.parseInt(count) > 0){
				log.info("The part already exists on another counting list.");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.PART_ALREADY_EXISTS_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.PART_ALREADY_EXISTS_KEY));
				throw exception;

			}

			StringBuilder checkCondition2 = new StringBuilder("SELECT COUNT(*) AS COUNT FROM ").append(dataLibrary).append(".E_LAGO  ");
			checkCondition2.append(" WHERE LNR = ").append(part_obj.getWarehouseId()).append(" AND HERST= '").append(manufacturer);
			checkCondition2.append("' AND TNR = '").append(part_obj.getPartNumber()).append("'");

			count = dbServiceRepository.getCountUsingQuery(checkCondition2.toString());
			if(Integer.parseInt(count) > 1){
				log.info("Part has more than one storage location.");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.ADD_COUNTED_PART_STORAGE_LOCATION_FAILED_MSG_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.ADD_COUNTED_PART_STORAGE_LOCATION_FAILED_MSG_KEY));
				throw exception;

			}

			if(Integer.parseInt(count) != 1){
				log.info("Part is not available in master data.");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.ADD_COUNTED_PART_MASTER_DATA_FAILED_MSG_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.ADD_COUNTED_PART_MASTER_DATA_FAILED_MSG_KEY));
				throw exception;

			}

			StringBuilder checkCondition4 = new StringBuilder("SELECT COUNT(*) AS COUNT FROM ").append(dataLibrary).append(".E_LAGO  ");
			checkCondition4.append(" WHERE LNR = ").append(part_obj.getWarehouseId()).append(" AND HERST= '").append(manufacturer);
			checkCondition4.append("' AND TNR = '").append(part_obj.getPartNumber()).append("' AND LOPA = '").append(part_obj.getPlace()).append("'");

			count = dbServiceRepository.getCountUsingQuery(checkCondition4.toString());
			if(Integer.parseInt(count) != 1){
				log.info("Storage location does not match master data.");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.ADD_COUNTED_PART_STORAGE_MASTER_DATA_FAILED_MSG_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.ADD_COUNTED_PART_STORAGE_MASTER_DATA_FAILED_MSG_KEY));
				throw exception;
			}
						
			
			StringBuilder query = new StringBuilder(" INSERT INTO ").append(schema).append(".O_INVPART ");
			query.append(" (INVLIST_ID, OEM, TNR, PARTNAME, PLACE, COUNT_OF_PLACES, CNT, DAK, OLD_STOCK, NEW_STOCK, SA, COUNTING_DATE, INVPARTSTS_ID, USER1, USER2, TA, TMARKE) VALUES ( ");
			//query.append("1,"); //status
			query.append(part_obj.getInventoryListId()).append(", '");
			query.append(manufacturer).append("', '");
			query.append(part_obj.getPartNumber()).append("', ");
			query.append("(select BENEN from ").append(dataLibrary).append(".e_etstamm where LNR=").append(part_obj.getWarehouseId());
			query.append(" AND TNR = '").append(part_obj.getPartNumber()).append("' AND HERST= '").append(manufacturer).append("' ), '");
			query.append(part_obj.getPlace()).append("', "); //Place
			query.append("1,"); // COUNT_OF_PLACES
			query.append("(select max(cnt) from ").append(schema).append(".O_INVPART WHERE INVLIST_ID = ").append(part_obj.getInventoryListId()).append(") +").append(1); //CNT
			query.append(", (select DAK from ").append(dataLibrary).append(".e_etstamm where LNR = ").append(part_obj.getWarehouseId());
			query.append(" AND TNR = '").append(part_obj.getPartNumber()).append("' AND HERST= '").append(manufacturer).append("' ), "); //DAK
			query.append("(select AKTBES from ").append(dataLibrary).append(".e_etstamm where LNR = ").append(part_obj.getWarehouseId());
			query.append(" AND TNR = '").append(part_obj.getPartNumber()).append("' AND HERST = '").append(manufacturer).append("'), "); //OLD_STOCK
			query.append(part_obj.getAmount()).append(", ");
			query.append("(select SA from ").append(dataLibrary).append(".e_etstamm where LNR = ").append(part_obj.getWarehouseId());
			query.append(" AND TNR = '").append(part_obj.getPartNumber()).append("' AND HERST= '").append(manufacturer).append("' ), "); //SA
			query.append("CURRENT TIMESTAMP ,");
			query.append("1 ,'"); //INVPARTSTS
			query.append(part_obj.getUser1()).append("','");
			query.append(part_obj.getUser2()).append("', ");
			query.append("(select TA from ").append(dataLibrary).append(".e_etstamm where LNR = ").append(part_obj.getWarehouseId());
			query.append(" AND TNR = '").append(part_obj.getPartNumber()).append("' AND HERST = '").append(manufacturer).append("' ), "); //Teileart - partsIndikator
			query.append("(select TMARKE from ").append(dataLibrary).append(".e_etstamm where LNR = ").append(part_obj.getWarehouseId());
			query.append(" AND TNR = '").append(part_obj.getPartNumber()).append("' AND HERST = '").append(manufacturer).append("' )) "); //TMARKE - partsbrand - xTMARKE
						
			int inventoryPartId = dbServiceRepository.insertResultUsingQuery(query.toString());

			if (inventoryPartId == 0) {
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.CREATE_FAILED_MSG_KEY, "Add Counted Part Manuelly"));
				log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY, "Add Counted Part Manuelly"), exception);
				throw exception;
			}

			part_obj.setInventoryPartId(String.valueOf(inventoryPartId));

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.CREATE_FAILED_MSG_KEY, "Add Counted Part Manuelly"));
			log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY,
					"Add Counted Part Manuelly"), exception);
			throw exception;
		}

		return part_obj;
	}


	/*
	 * This method is used the Update the inventory Status Id in the O_INVLIST table based on provided inventory List id.
	 */
	protected boolean updateInventoryStatus(String schema, String inventoryListId, String statusId) {

		log.info("Inside updateInventoryStatus method of InventoryServiceImpl");

		boolean isUpdated = false;

		try {

			StringBuilder selectQuery = new StringBuilder("Select NAME from ").append(schema).append(".O_INVLIST  where INVLIST_ID = ").append(inventoryListId); 
			selectQuery.append(" and INVLISTSTS_ID = 1");

			List<InventoryList> checkStatusList = dbServiceRepository.getResultUsingQuery(InventoryList.class, selectQuery.toString(), true);

			if(checkStatusList !=null && !checkStatusList.isEmpty()) {

				StringBuilder updateQuery = new StringBuilder("Update ").append(schema).append(".O_INVLIST SET INVLISTSTS_ID = ").append(Integer.parseInt(statusId));
				updateQuery.append(", PRINT_DATE = now() where INVLIST_ID =").append(inventoryListId);

				isUpdated = dbServiceRepository.updateResultUsingQuery(updateQuery.toString());

			}
			else {
				log.info("Inventory Id- {} status is not Activated", inventoryListId);
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Inventory Status"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Inventory Status"), exception);
			throw exception;
		}

		return isUpdated;
	}


	/**
	 * This method is used to get counting list value from O_INVLIST table.
	 */
	@Override
	public List<InventoryDTO> getCountingListValueFromActiveCounting(String dataLibrary, String schema, String inventoryListId) {

		log.info("Inside getCountingListValueFromActiveCounting method of InventoryServiceImpl");

		List<InventoryDTO> inventoryDTOList = new ArrayList<InventoryDTO>();

		try {
			StringBuilder createdCountingList = new StringBuilder("SELECT INVLIST_ID,INVLISTSTS_ID, Warehouse_ID, WAREHOUSE_NAME,SORT_BY, (Select COUNT(*) as countingPosition from ");
			createdCountingList.append(schema).append(".O_INVPART where INVLIST_ID = ").append(inventoryListId).append(" ),  (Select COUNT(*) as alreadyRecorded from ");
			createdCountingList.append(schema).append(".O_INVPART where INVLIST_ID = ").append(inventoryListId).append(" AND INVPARTSTS_ID=1),  ");
			createdCountingList.append("( select USER1 from ").append(schema).append(".O_INVPART where invlist_id =").append(inventoryListId).append(" and counting_date is not null order by counting_date desc fetch first row only ) ,");
			createdCountingList.append("( select USER2 from ").append(schema).append(".O_INVPART where invlist_id = ").append(inventoryListId).append(" and counting_date is not null order by counting_date desc fetch first row only ) FROM ");
			createdCountingList.append(schema).append(".O_INVLIST where INVLIST_ID = ").append(inventoryListId);

			List<InventoryList> inventoryList = dbServiceRepository.getResultUsingQuery(InventoryList.class, createdCountingList.toString(), true);

			if (inventoryList != null && !inventoryList.isEmpty() && inventoryList.size()==1) {
				for(InventoryList inventory : inventoryList){
					InventoryDTO inventoryDTO = new InventoryDTO();
					inventoryDTO.setWarehouseNo(StringUtils.leftPad(String.valueOf(inventory.getWarehouseId()),2,"0"));
					inventoryDTO.setWarehouseName(inventory.getWarehouseName());
					inventoryDTO.setTotalCountingPosition(inventory.getCountingPosition());
					inventoryDTO.setAlreadyRecordedCounts(inventory.getAlreadyRecorded());
					inventoryDTO.setInventoryList_Status_Id(String.valueOf(inventory.getInventoryList_Status_Id()));
					if(String.valueOf(inventory.getSortBy()).equalsIgnoreCase("0")){
						inventoryDTO.setSortBy("TNR"); 
					}else{
						inventoryDTO.setSortBy("PLACE");  
					}
					inventoryDTO.setUser1(inventory.getUser1());
					inventoryDTO.setUser2(inventory.getUser2());

					inventoryDTOList.add(inventoryDTO);
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(
					LocaleContextHolder.getLocale(), ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Counting list value"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Counting list value"),exception);
			throw exception;
		}

		return inventoryDTOList;
	}


	/**
	 * This method is used to get the Printing PageBreak Dropdownlist from Stub
	 * @return List
	 */
	@Override
	public List<DropdownObject> getPrintingPageBreakList() {
		log.info("Inside getPrintingPageBreakList method of InventoryServiceImpl");

		List<DropdownObject> printingPBList = new ArrayList<>();
		printingPBList = stubServiceRepository.getDDLValuesFromStub(StubServiceRepository.printingPBMap);
		return printingPBList;
	}


	
	/**
	 * This method is used to update counting list value into O_INVPART table.
	 */
	@Override
	public Map<String, Boolean> updateCountingListValueFromActiveCounting(String dataLibrary, String schema, List<CreatedCountingDetailListDTO> updatedcountingList) {

		log.info("Inside updateCountingListValueFromActiveCounting method of InventoryServiceImpl");
		Map<String, Boolean> programOutput = new HashMap<String, Boolean>();

		try(Connection con = dbServiceRepository.getConnectionObject();
				Statement stmt = con.createStatement();) {

			if(updatedcountingList!=null && !updatedcountingList.isEmpty()){

				for(CreatedCountingDetailListDTO detailListDTO:updatedcountingList){

				
					if(detailListDTO != null){
					StringBuilder updateCountingList = new StringBuilder("UPDATE ").append(schema).append(".O_INVPART SET ");
					if(detailListDTO.getAmount()==null || detailListDTO.getAmount().isEmpty()) {
						updateCountingList.append("INVPARTSTS_ID=0,");
					}else {
						updateCountingList.append("INVPARTSTS_ID=1,");
					}
					updateCountingList.append(" COUNTING_DATE=current timestamp , USER1= '").append(detailListDTO.getUser1());
					updateCountingList.append("' , USER2='").append(detailListDTO.getUser2()).append("' ");
				
					if(detailListDTO.getAmount()!=null) {
					updateCountingList.append(" , NEW_STOCK= ").append(detailListDTO.getAmount());
					}else {
						updateCountingList.append(" , NEW_STOCK= null ");
					}
					
					updateCountingList.append(" WHERE ID=").append(detailListDTO.getInventoryPartId());
					log.info("Get Results Using Query :" + updateCountingList.toString());
					stmt.addBatch(updateCountingList.toString());

					}
				}
			}
			int[] records =  dbServiceRepository.insertResultUsingBatchQuery(stmt);

			if(records != null){
				log.info("Counting list value - Total rows updated :"+records.length);
			}
			con.commit();
			con.setAutoCommit(true);
			programOutput.put("isUpdated", true);

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(
					LocaleContextHolder.getLocale(), ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Counting list value"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Counting list value"),exception);
			throw exception;
		}

		return programOutput;
	}


	
	/**
	 * This method is used to get Counting List Items From ActiveCounting from O_INVPART table.
	 */
	@Override
	public GlobalSearch getCountingListItemsFromActiveCounting(String dataLibrary, String schema, String inventoryListId, 
			String pageSize, String pageNumber, String displayType, String searchCount ) {

		log.info("Inside getCountingListItemsFromActiveCounting method of InventoryServiceImpl");
		GlobalSearch globalSearchList = new GlobalSearch();
		List<CreatedCountingDetailListDTO> partListDTOs = new ArrayList<CreatedCountingDetailListDTO>();

		try {

			if(pageSize==null || pageNumber==null || pageSize.isEmpty() || pageNumber.isEmpty()){
				pageSize = RestInputConstants.DEFAULT_PAGE_SIZE;
				pageNumber = RestInputConstants.DEFAULT_PAGE_NO;
			}

			int totalRecords = Integer.parseInt(pageSize);
			int nextRows = totalRecords * (Integer.parseInt(pageNumber) - 1);

			//validate the page size
			//			validatePageSize(totalRecords);     //This checked is removed for ALPHAX-3235

			log.info("PageSize  : {}  OFFSET Size (Skip Rows):  {} Page Number  : {} ", pageSize, nextRows, pageNumber);

			StringBuilder whereClause = new StringBuilder("");
			if(displayType!= null && displayType.equalsIgnoreCase("1")){
				whereClause.append(" AND INVPARTSTS_ID = 0 ");
			}

			if(searchCount!=null && !searchCount.isEmpty()){
				//validateNumberOnlyString(searchCount);
				whereClause.append(" AND CNT >= ").append(searchCount);
			}

			StringBuilder partList = new StringBuilder("SELECT TNR, PARTNAME, PLACE, ID as invpart_id,CNT,NEW_STOCK, (Select count(*) FROM ");
			partList.append(schema).append(".O_INVPART where INVLIST_ID = ").append(inventoryListId).append(whereClause).append(") AS ROWNUMER FROM ");
			partList.append(schema).append(".O_INVPART where INVLIST_ID = ").append(inventoryListId).append(whereClause).append(" ORDER BY CNT ");
			partList.append(" OFFSET ").append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");

			List<InventoryParts> inventoryParts = dbServiceRepository.getResultUsingQuery(InventoryParts.class, partList.toString(), true);

			if (inventoryParts != null && !inventoryParts.isEmpty()) {
				for (InventoryParts invParts : inventoryParts) {

					CreatedCountingDetailListDTO detailListDTO = new CreatedCountingDetailListDTO();
					detailListDTO.setPartNumber(invParts.getPartNumber());
					detailListDTO.setPartName(invParts.getPartNAME());
					detailListDTO.setPlace(invParts.getPlace());
					detailListDTO.setInventoryPartId(String.valueOf(invParts.getInventoryPartId()));
					detailListDTO.setPaginationNo(String.valueOf(invParts.getCount()));
					if(invParts.getNewStock()!=null){
						detailListDTO.setAmount(String.valueOf(invParts.getNewStock()));
					}else{
						detailListDTO.setAmount("");	
					}
					partListDTOs.add(detailListDTO);

				}

				globalSearchList.setSearchDetailsList(partListDTOs);
				globalSearchList.setTotalPages(Integer.toString(inventoryParts.get(0).getTotalCount()));
				globalSearchList.setTotalRecordCnt(Integer.toString(inventoryParts.get(0).getTotalCount()));
			}else{
				globalSearchList.setSearchDetailsList(partListDTOs);
				globalSearchList.setTotalPages(Integer.toString(0));
				globalSearchList.setTotalRecordCnt(Integer.toString(0));
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(
					LocaleContextHolder.getLocale(), ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Created Counting details list"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Created Counting details list"),exception);
			throw exception;
		}

		return globalSearchList;
	}


	
	/**
	 * This method is used to build new counting lists -(Neue Zähllisten aufbauen)  into DB
	 * @return Object
	 */
	@Override
	public Map<String, Boolean> createNewCountingList(CreateNewCountingListDTO countingList_DTO, String schema, String dataLibrary,String loginUser,String selectionType) {

		log.info("Inside createNewCountingList method of InventoryServiceImpl");

		Map<String, Boolean> procedureOutput = new HashMap<String, Boolean>();
		procedureOutput.put("isCreated", false);

		try(Connection con = dbServiceRepository.getConnectionObject();) {

			con.setAutoCommit(false);

			valueShouldBeNumeric(countingList_DTO.getInventoryValueRange(), "Bestandswert geringer als");
			valueShouldBeNumeric(countingList_DTO.getMaxCountingPosition(), "Maximale Zählpositionen");
			valueShouldBeNumeric(countingList_DTO.getToPriceRange(), "Bis Preisspanne");
			valueShouldBeNumeric(countingList_DTO.getFromPriceRange(), "Von Preisspanne");
			
			boolean updateFlag = automaticSettingForSA2PartsWithoutStock(dataLibrary, countingList_DTO );
			if(updateFlag) {
				
			String query_for_invList = createQueryForInventoryList(countingList_DTO,schema,loginUser);
			int invListId = dbServiceRepository.insertResultUsingQuery(query_for_invList.toString(),con);

			if (invListId == 0) {
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.CREATE_FAILED_MSG_KEY, "New Counting List"));
				log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY, "New Counting List"), exception);
				throw exception;
			}else{
				
				insertValueIn_InvType(String.valueOf(invListId), "0", "1",loginUser, con, schema);
				
				//This condition only for DCAG
				String manufacturer = countingList_DTO.getManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING: countingList_DTO.getManufacturer();
				String tnrsFrom = null;
				String tnrsTo = null;
				if(manufacturer.contentEquals("DCAG") && countingList_DTO.isPartNumberRangeFlag() && !countingList_DTO.isAllParts() ) {
						
					if(countingList_DTO.getFromPartNumber()!=null && !countingList_DTO.getFromPartNumber().isEmpty()) {
						tnrsFrom = getTNRSValue(countingList_DTO, schema,  dataLibrary,  selectionType, invListId, "FROM_TNR");	
					}
					if(countingList_DTO.getToPartNumber()!=null && !countingList_DTO.getToPartNumber().isEmpty()) {
						tnrsTo = getTNRSValue(countingList_DTO, schema,  dataLibrary,  selectionType, invListId, "TO_TNR");	
					}
				}
				
				if(manufacturer.contentEquals("DCAG") && !isNullOrEmpty(tnrsFrom)  && !isNullOrEmpty(tnrsTo)  &&
						countingList_DTO.isPartNumberRangeFlag() && !countingList_DTO.isAllParts()) {
					
					log.info("Parts(TNRS) not available in the selected record type.");
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.PARTS_NOT_EXISTS_MSG_KEY));
					log.error(messageService.getReadableMessage(ExceptionMessages.PARTS_NOT_EXISTS_MSG_KEY));
					throw exception;
				}
				

				String whereClauseCondition = "";
				whereClauseCondition = removeOrderByClouse(createWhereClause(countingList_DTO, schema, dataLibrary, selectionType, invListId ,false, tnrsFrom, tnrsTo));
				
				log.info("Check Parts existing in E_ETSTAMM or Not");
				StringBuilder checkValueInEtstamm = new StringBuilder("SELECT COUNT(tnr) AS COUNT FROM ").append(dataLibrary).append(".e_etstamm t where ");
				checkValueInEtstamm.append(whereClauseCondition);
				
				String countForEtstamm = dbServiceRepository.getCountUsingQuery(checkValueInEtstamm.toString());
				if(Integer.parseInt(countForEtstamm) == 0){
					log.info("Part(s) not available in the selected record type.");
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.PARTS_NOT_EXISTS_MSG_KEY));
					log.error(messageService.getReadableMessage(ExceptionMessages.PARTS_NOT_EXISTS_MSG_KEY));
					throw exception;
				}
				
				StringBuilder whereClause = createWhereClause(countingList_DTO, schema, dataLibrary, selectionType, invListId ,true, tnrsFrom, tnrsTo);
				whereClauseCondition = removeOrderByClouse(whereClause);
				log.info("Check Parts existing in O_INVPART or Not");
				StringBuilder checkConditionForCountingList = new StringBuilder("SELECT COUNT(tnr) AS COUNT FROM ").append(dataLibrary).append(".e_etstamm t where ");
				checkConditionForCountingList.append(whereClauseCondition);
				
				String count = dbServiceRepository.getCountUsingQuery(checkConditionForCountingList.toString());
				if(Integer.parseInt(count) == 0){
					log.info("Selection unsuccessful, the parts already exist on other counting lists");
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.PARTS_LIST_ALREADY_EXISTS_MSG_KEY));
					log.error(messageService.getReadableMessage(ExceptionMessages.PARTS_LIST_ALREADY_EXISTS_MSG_KEY));
					throw exception;
				}
				
				StringBuilder query_for_procedure  = new StringBuilder("BEGIN DECLARE lid BIGINT; ");
				query_for_procedure.append(" FOR v CURSOR FOR ");
				query_for_procedure.append(" Select herst as xOEM, tnr AS xTNR, benen AS xPARTNAME, lopa AS xPLACE, lnr as xLNR, sa as xSA, ta AS xTA, TMARKE AS xTMARKE, TNRS AS xTNRS, TNRD AS xTNRD ,");
				query_for_procedure.append(" CASE WHEN (select count(*) from ").append(dataLibrary).append(".e_lago where tnr=t.tnr and lnr=t.lnr and prio > 1 ) > 0  ");
				query_for_procedure.append(" THEN (select count(*) from ").append(dataLibrary).append(".e_lago where tnr=t.tnr and lnr=t.lnr and prio > 1 )+1  ELSE '1' ");
				query_for_procedure.append(" end as xCOUNT_OF_PLACES from ").append(dataLibrary);
				query_for_procedure.append(".e_etstamm t where ").append(whereClause);
				query_for_procedure.append(" DO ");
				query_for_procedure.append(" FOR w CURSOR FOR ");
				query_for_procedure.append(" select lopa as yPLACE from ").append(dataLibrary).append(".e_ETSTAMM where tnr=xTNR and lnr=xLNR  UNION ");
				query_for_procedure.append(" select lopa as yPLACE from ").append(dataLibrary).append(".e_lago where tnr=xTNR and lnr=xLNR AND PRIO>1 ");
				query_for_procedure.append(" DO ");
				query_for_procedure.append(" INSERT INTO  ").append(schema);
				query_for_procedure.append(".O_INVPART (INVPARTSTS_ID, INVLIST_ID, OEM, TNR, PARTNAME, PLACE, COUNT_OF_PLACES, SA,TA, TMARKE ,TNRS,TNRD) ");
				query_for_procedure.append(" VALUES (0, ").append(invListId).append(", xOEM, xTNR, xPARTNAME, yPLACE, xCOUNT_OF_PLACES, xSA, xTA, xTMARKE , xTNRS, xTNRD); ");
				query_for_procedure.append("END FOR; END FOR; END ");

				boolean executionFlag = dbServiceRepository.excuteProcedure(query_for_procedure.toString(),con);

				if(executionFlag){
					// insert value in O_INVCRITERIA

					String query_for_invCriteria = createQueryForInventoryCriteria(countingList_DTO,schema,dataLibrary, loginUser, invListId);
					int invCriteriaId = dbServiceRepository.insertResultUsingQuery(query_for_invCriteria.toString(),con);

					if (invCriteriaId == 0) {
						AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
								ExceptionMessages.CREATE_FAILED_MSG_KEY, "Inventory Criteria"));
						log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY, "Inventory Criteria"), exception);
						throw exception;
					}
				}

				procedureOutput.put("isCreated", true);
				con.commit();
				con.setAutoCommit(true);
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Create New Counting List"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Create New Counting List"), exception);
			throw exception;
		}

		return procedureOutput;
	}


	private String removeOrderByClouse(StringBuilder whereClause) {
		
		String whereClauseCondition = "";
		if(whereClause.toString().contains("ORDER BY TNRS")) {
			whereClauseCondition = whereClause.toString().replace("ORDER BY TNRS", "");
		}else if(whereClause.toString().contains("ORDER BY LOPA")) {
			whereClauseCondition = whereClause.toString().replace("ORDER BY LOPA", "");
		}else {
			whereClauseCondition = whereClause.toString();
		}
		return whereClauseCondition;
	}


	private boolean automaticSettingForSA2PartsWithoutStock(String dataLibrary,CreateNewCountingListDTO countingList_DTO) {

		log.info("Inside automaticSettingForSA2PartsWithoutStock method of InventoryServiceImpl");
		boolean updateStatus = false;

		try(Connection con = dbServiceRepository.getConnectionObject();
				Statement stmt = con.createStatement();) {
			
			List<String> listValues = Arrays.asList(StringUtils.split(countingList_DTO.getWarehouse(), "-"));
			
			if(countingList_DTO.getStorageIndikator().equalsIgnoreCase("0") || countingList_DTO.getStorageIndikator().equalsIgnoreCase("2")) {
				//ALPHAX-3894 - both query should be execute if SA2 OR SA1&2
				StringBuilder updateQuery1 = new StringBuilder("Update ").append(dataLibrary).append(".E_ETSTAMM ");
				updateQuery1.append(" SET IVKZ= ''  WHERE  SA = 2 AND IVKZ = 'I' AND DTINV  != 0 AND IVZME = 0 AND AKTBES != 0 AND ");
				updateQuery1.append(" LNR = ").append(listValues.get(0));
					dbServiceRepository.updateResultUsingQuery(updateQuery1.toString(), con);
				
				StringBuilder updateQuery2 = new StringBuilder("Update ").append(dataLibrary).append(".E_ETSTAMM ");
				updateQuery2.append(" SET IVKZ= 'I', DTINV = VARCHAR_FORMAT(now(), 'YYYYMMDD') , IVZME = 0  WHERE  SA = 2  AND AKTBES = 0 ");
				updateQuery2.append(" AND LNR=").append(listValues.get(0));
				dbServiceRepository.updateResultUsingQuery(updateQuery2.toString(), con);
				
				updateStatus = true;
			
			}else {
				log.info("SA value is 1 so not required any automatic setting" );
				updateStatus = true;
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Automatic setting for SA2 parts without stock"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY,"Automatic setting for SA2 parts without stock"), exception);
			throw exception;
		}

		return updateStatus;
	}


	private void valueShouldBeNumeric(String fieldValue, String fieldName) {

		try {
			if (fieldValue!=null) {
				Double.parseDouble(fieldValue);
			}

		} catch (Exception e) {
			log.info("Invalid field value :" + fieldName);
			AlphaXException exception = new AlphaXException(messageService
					.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.INVALID_MSG_KEY, fieldName + ",Sollte numerisch sein"));
			log.error(messageService.getReadableMessage(ExceptionMessages.INVALID_MSG_KEY, fieldName + ",Sollte numerisch sein"));
			throw exception;
		}

	}
	


	private String createQueryForInventoryList(CreateNewCountingListDTO countingList_DTO, String schema, String loginUser) {

		log.info("Inside createQueryForInventoryList method of InventoryServiceImpl");

		List<String> listValues = Arrays.asList(StringUtils.split(countingList_DTO.getWarehouse(), "-"));


		StringBuilder query_for_invList = new StringBuilder("INSERT INTO ").append(schema).append(".O_INVLIST");
		query_for_invList.append("(NAME, INVLISTSTS_ID,CREATED_TS, CREATED_BY, WAREHOUSE_ID, WAREHOUSE_NAME, ORIGIN_ID, SORT_BY) ");
		query_for_invList.append("VALUEs( ");
		query_for_invList.append("'").append(countingList_DTO.getListName()!=null?countingList_DTO.getListName():"").append("',");
		query_for_invList.append("0").append(",");
		query_for_invList.append("CURRENT TIMESTAMP").append(",");
		query_for_invList.append("'").append(loginUser).append("',");
		query_for_invList.append(listValues.get(0)).append(",");
		query_for_invList.append("'").append(listValues.get(1)).append("',");
		query_for_invList.append("0").append(",");
		query_for_invList.append(countingList_DTO.getSortBy()!=null?countingList_DTO.getSortBy():"0").append(")");

		return query_for_invList.toString();
	}


	private StringBuilder createWhereClause(CreateNewCountingListDTO list_DTO,String schema, String dataLibrary, String selectionType,
			int invListId,boolean checkPartsInCountingList, String tnrsFrom, String tnrsTo) {

		List<String> listValues = Arrays.asList(StringUtils.split(list_DTO.getWarehouse(), "-"));

		String manufacturer = list_DTO.getManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING: list_DTO.getManufacturer();
		
		
		StringBuilder query = new StringBuilder(" HERST= '").append(manufacturer);
		query.append("' AND LNR= (select distinct warehouse_id from ").append(schema).append(".O_INVLIST where INVLIST_ID=").append(invListId).append(")");
		if(checkPartsInCountingList) {
		query.append(" AND NOT TNR IN (select distinct TNR from ").append(schema).append(".O_INVPART WHERE INVLIST_ID IN(SELECT INVLIST_ID FROM ");
		query.append(schema).append(".O_INVLIST WHERE INVLISTSTS_ID BETWEEN 0 AND 5 AND WAREHOUSE_ID =").append(listValues.get(0)).append("))");
		}
		
		//for special selection
		if(selectionType!= null && selectionType.equalsIgnoreCase("Special")){

			if(list_DTO.isPartMovedThisYear()){
				query.append(" AND substr(DTLBEW, 1, 4) ='2022' ");
			}
			if(list_DTO.isUsedParts()){
				query.append(" AND substr(tnr, 14, 1)='7' ");
			}
			if(list_DTO.isMaterialParts()){
				query.append(" AND NOT (select TLE_IMCO from ").append(schema).append(".ETK_TLEKAT where TLE_TNR=t.tnr)=''");
			}
			if(list_DTO.isTheftRelevantParts()){
				query.append(" AND NOT DRTKZ='' ");
			}
			if(list_DTO.isExpirationDateParts()){
				query.append(" AND NOT (select TLE_LAZ from ").append(schema).append(".ETK_TLEKAT where TLE_TNR=t.tnr)=0");
			}else{
				log.info("No any special selection - (Sonderselektionen)");
			}
		}else{
			//for default selection
			if(list_DTO.isPartNumberRangeFlag()){
				if(!list_DTO.isAllParts()){

				 if(manufacturer.contentEquals("DCAG")) {
					 if(isNullOrEmpty(list_DTO.getFromPartNumber()) && isNullOrEmpty(list_DTO.getToPartNumber()) && isNullOrEmpty(tnrsFrom) && isNullOrEmpty(tnrsTo)){
						 	query.append(" AND ( (TNRS between '").append(tnrsFrom).append("' AND '").append(tnrsTo).append("' ) OR ");
							query.append(" (TNRS between '").append(tnrsTo).append("' AND '").append(tnrsFrom).append("' ) )");
						}else if(isNullOrEmpty(list_DTO.getFromPartNumber()) && isNullOrEmpty(tnrsFrom)){
							query.append(" AND TNRS= '").append(tnrsFrom).append("' ");
						}else if(isNullOrEmpty(list_DTO.getToPartNumber()) && isNullOrEmpty(tnrsTo)){
							query.append(" AND TNRS= '").append(tnrsTo).append("' ");
						}else{
							log.info("Both part selection is empty");
						}
				 }else {	
						if(isNullOrEmpty(list_DTO.getFromPartNumber()) && isNullOrEmpty(list_DTO.getToPartNumber())){
							
							query.append(" AND (tnr between '").append(list_DTO.getFromPartNumber()).append("' AND '").append(list_DTO.getToPartNumber()).append("' ) ");
						}else if(isNullOrEmpty(list_DTO.getFromPartNumber())){
							query.append(" AND tnr= '").append(list_DTO.getFromPartNumber()).append("'");
						}else if(isNullOrEmpty(list_DTO.getToPartNumber())){
							query.append(" AND tnr= '").append(list_DTO.getToPartNumber()).append("'");
						}else{
							log.info("Both part selection is empty");
						}
			}
			}}else{
				if(!list_DTO.isAllStorageLocations()){
					if(isNullOrEmpty(list_DTO.getFromStorageLocation()) && isNullOrEmpty(list_DTO.getToStorageLocation())){

						query.append(" AND (t.tnr in( select  tnr from ").append(dataLibrary).append(".e_ETSTAMM ");
						query.append(" where ((lopa between '").append(list_DTO.getFromStorageLocation()).append("' AND '").append(list_DTO.getToStorageLocation()).append("' )");
						query.append(" OR (lopa between '").append(list_DTO.getToStorageLocation()).append("' AND '").append(list_DTO.getFromStorageLocation()).append("' ) ) ");
						query.append(" AND HERST= '").append(manufacturer).append("' AND LNR=").append(listValues.get(0));
						query.append(" UNION ");
						query.append("SELECT  tnr from ").append(dataLibrary).append(".e_lago where "); 
						query.append(" ((lopa between '").append(list_DTO.getFromStorageLocation()).append("' AND '").append(list_DTO.getToStorageLocation()).append("' )");
						query.append(" OR  (lopa between '").append(list_DTO.getToStorageLocation()).append("' AND '").append(list_DTO.getFromStorageLocation()).append("' ) )");
						query.append(" AND HERST= '").append(manufacturer).append("' AND LNR=").append(listValues.get(0)).append(" AND PRIO>1))");
					}else if(isNullOrEmpty(list_DTO.getFromStorageLocation())){

						query.append(" AND (t.tnr in( select  tnr from ").append(dataLibrary).append(".e_ETSTAMM ");
						query.append(" where lopa = '").append(list_DTO.getFromStorageLocation());;
						query.append("' AND HERST= '").append(manufacturer).append("' AND LNR=").append(listValues.get(0));
						query.append(" UNION ");
						query.append("SELECT  tnr from ").append(dataLibrary).append(".e_lago where lopa = '");
						query.append(list_DTO.getFromStorageLocation()).append("' AND HERST= '");
						query.append(manufacturer).append("' AND LNR=").append(listValues.get(0)).append(" AND PRIO>1))");
					}else if(isNullOrEmpty(list_DTO.getToStorageLocation())){

						query.append(" AND (t.tnr in( select  tnr from ").append(dataLibrary).append(".e_ETSTAMM ");
						query.append(" where lopa = '").append(list_DTO.getToStorageLocation());;
						query.append("' AND HERST= '").append(manufacturer).append("' AND LNR=").append(listValues.get(0));
						query.append(" UNION ");
						query.append("SELECT  tnr from ").append(dataLibrary).append(".e_lago where lopa = '");
						query.append(list_DTO.getToStorageLocation()).append("' AND HERST= '");
						query.append(manufacturer).append("' AND LNR=").append(listValues.get(0)).append(" AND PRIO>1))");
					}else{
						log.info("Both LOPA selection is empty");
					}
				}

			}
		}

		if(isNullOrEmpty(list_DTO.getMarketingCode())){
			if(list_DTO.getMarketingCode().contains("*")) {
				query.append(" AND UPPER(MC) LIKE UPPER('").append(list_DTO.getMarketingCode().replace("*", "_")).append("') ");	
			}else {
				query.append(" AND UPPER(MC) LIKE UPPER('").append(list_DTO.getMarketingCode()).append("') ");
			}
			
		}
		if(isNullOrEmpty(list_DTO.getCountingGroup())){
			query.append(" AND ZAEGRU='").append(list_DTO.getCountingGroup()).append("' ");
		}
		if(isNullOrEmpty(list_DTO.getPartBrand())){
			query.append(" AND TMARKE='").append(list_DTO.getPartBrand()).append("' ");
		}
		if(isNullOrEmpty(list_DTO.getInventoryValueRange())){
			query.append(" AND AKTBES < ").append(list_DTO.getInventoryValueRange()).append(" ");
		}
		if(isNullOrEmpty(list_DTO.getStorageIndikator())){
			// 0: SA1 and SA2, 1: SA1 only, 2: SA2 only	
			if(list_DTO.getStorageIndikator().equalsIgnoreCase("0")){
				query.append(" AND ( SA=1 OR (SA=2 AND AKTBES>0)) ");
			}else if(list_DTO.getStorageIndikator().equalsIgnoreCase("1")){
				query.append(" AND SA=1 ");
			}else if(list_DTO.getStorageIndikator().equalsIgnoreCase("2")){
				query.append(" AND (SA=2 AND AKTBES>0 ) ");
			}
		}
		if(isNullOrEmpty(list_DTO.getStorageLocation())){
			// 0 = All ,  1 = Teile mit einem Lgerort 2 = Teile mit Mehrfachlagerort
			if(list_DTO.getStorageLocation().equalsIgnoreCase("1")){ 
				query.append(" AND (select count(lopa) from ").append(dataLibrary).append(".e_lago where tnr=t.tnr and lnr=t.lnr) <= 1 ");
			}else if(list_DTO.getStorageLocation().equalsIgnoreCase("2")){ 
				query.append(" AND (select count(lopa) from ").append(dataLibrary).append(".e_lago where tnr=t.tnr and lnr=t.lnr) > 1 ");
			}
		}
		if(!list_DTO.isAlreadyCountedItems()){
			query.append(" AND IVKZ='' "); //0: Do not add counted prts, 1: Add counted prts	
		}
		if(list_DTO.isPriceRange()){
			if(isNullOrEmpty(list_DTO.getFromPriceRange()) && isNullOrEmpty(list_DTO.getToPriceRange()) ){
				query.append(" AND (EPR BETWEEN ").append(list_DTO.getFromPriceRange()).append(" AND ").append(list_DTO.getToPriceRange()).append(" ) ");
			}
		}
		if(isNullOrEmpty(list_DTO.getSortBy())){
			if(list_DTO.getSortBy().equalsIgnoreCase("0")){
				query.append(" ORDER BY TNRS ");
			}else{
				query.append(" ORDER BY LOPA ");
			}
		}
		if(isNullOrEmpty(list_DTO.getMaxCountingPosition())){
			query.append("LIMIT ").append(list_DTO.getMaxCountingPosition());
		}
		
		return query;
	}


	
	private String createQueryForInventoryCriteria(CreateNewCountingListDTO list_DTO, String schema,
			String dataLibrary, String loginUser, int invListId) {
		log.info("Inside createQueryForInventoryCriteria method of InventoryServiceImpl");

		String manufacturer = list_DTO.getManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING: list_DTO.getManufacturer();

		StringBuilder query_for_invList = new StringBuilder("INSERT INTO ").append(schema).append(".O_INVCRT");
		query_for_invList.append("(INVLIST_ID, OEM, TNR_OR_PLACE, TNR_FROM, TNR_TO,PLACE_FROM,PLACE_TO, MC, COUNTING_GROUP, ");
		query_for_invList.append(" SA,COUNTED_PARTS,PRICE_FROM, PRICE_TO, PLACES,PRICE, MOVED, ALTERNATIVES,DANGER, STEAL, ");
		query_for_invList.append(" USE_BY_DATE, PARTKIND,CREATED_TS, CREATED_BY,LIMIT )"); 
		query_for_invList.append("VALUES( ");
		query_for_invList.append(invListId).append(",");
		query_for_invList.append("'").append(manufacturer).append("', ");
		query_for_invList.append(list_DTO.getSortBy()).append(", ");

		if(isNullOrEmpty(list_DTO.getFromPartNumber())){
			query_for_invList.append("'").append(list_DTO.getFromPartNumber()).append("', ");
		}else{
			query_for_invList.append("''").append(", ");
		}

		if(isNullOrEmpty(list_DTO.getToPartNumber())){
			query_for_invList.append("'").append(list_DTO.getToPartNumber()).append("', ");
		}else{
			query_for_invList.append("''").append(", ");
		}

		if(isNullOrEmpty(list_DTO.getFromStorageLocation())){
			query_for_invList.append("'").append(list_DTO.getFromStorageLocation()).append("', ");
		}else{
			query_for_invList.append("''").append(", ");	
		}

		if(isNullOrEmpty(list_DTO.getToStorageLocation())){
			query_for_invList.append("'").append(list_DTO.getToStorageLocation()).append("', ");
		}else{
			query_for_invList.append("''").append(", ");	
		}

		if(isNullOrEmpty(list_DTO.getMarketingCode())){
			query_for_invList.append("'").append(list_DTO.getMarketingCode()).append("', ");
		}else{
			query_for_invList.append("''").append(", ");	
		}

		if(isNullOrEmpty(list_DTO.getCountingGroup())){
			query_for_invList.append("'").append(list_DTO.getCountingGroup()).append("', ");
		}else{
			query_for_invList.append("''").append(", ");	
		}

		if(isNullOrEmpty(list_DTO.getStorageIndikator())){
			query_for_invList.append(list_DTO.getStorageIndikator()).append(", ");
		}else{
			query_for_invList.append("'0'").append(", ");	
		}
		query_for_invList.append(list_DTO.isAlreadyCountedItems()==false?0:1).append(", ");
		query_for_invList.append(list_DTO.getFromPriceRange()).append(", ");	
		query_for_invList.append(list_DTO.getToPriceRange()).append(", ");

		if(isNullOrEmpty(list_DTO.getStorageLocation())){
			query_for_invList.append(list_DTO.getStorageLocation()).append(", ");
		}else{
			query_for_invList.append("'0'").append(", ");	
		}
		query_for_invList.append(list_DTO.getInventoryValueRange()).append(", ");
		query_for_invList.append(list_DTO.isPartMovedThisYear()==false?0:1).append(", ");
		query_for_invList.append(list_DTO.isUsedParts()==false?0:1).append(", ");
		query_for_invList.append(list_DTO.isMaterialParts()==false?0:1).append(", ");
		query_for_invList.append(list_DTO.isTheftRelevantParts()==false?0:1).append(", ");
		query_for_invList.append(list_DTO.isExpirationDateParts()==false?0:1).append(", ");

		if(isNullOrEmpty(list_DTO.getPartBrand())){
			query_for_invList.append("'").append(list_DTO.getPartBrand()).append("', ");
		}else{
			query_for_invList.append("''").append(", ");
		}

		query_for_invList.append(" CURRENT TIMESTAMP ").append(", ");
		query_for_invList.append("'").append(loginUser).append("', ");
		query_for_invList.append(list_DTO.getMaxCountingPosition()).append(") ");

		return query_for_invList.toString();
	}


	
	boolean isNullOrEmpty(String value) {

		boolean flag = false;
		if (value != null && !value.trim().isEmpty()) {
			flag = true;
		}
		return flag;
	}


	
	/**
	 * This method is used to add parts in existing counting list (Teile hinzufügen)  into DB
	 * @return Object
	 */
	@Override
	public Map<String, Boolean> addPartInExistingCountingList(CreateNewCountingListDTO countingList_DTO, String schema, String dataLibrary,String loginUser,
			String selectionType, String invListId) {

		log.info("Inside addPartInExistingCountingList method of InventoryServiceImpl");

		Map<String, Boolean> procedureOutput = new HashMap<String, Boolean>();
		procedureOutput.put("isUpdated", false);

		try(Connection con = dbServiceRepository.getConnectionObject();) {

			con.setAutoCommit(false);
			//Checking status of the counting list if changed in the meantime then page will reload.
			genericInventoryStatusCheck(schema,invListId,"0"); // 0 = inventory list status Id

			valueShouldBeNumeric(countingList_DTO.getInventoryValueRange(), "Bestandswert geringer als");
			valueShouldBeNumeric(countingList_DTO.getMaxCountingPosition(), "Maximale Zählpositionen");
			valueShouldBeNumeric(countingList_DTO.getToPriceRange(), "Bis Preisspanne");
			valueShouldBeNumeric(countingList_DTO.getFromPriceRange(), "Von Preisspanne");

			int listId = Integer.parseInt(invListId);
			
			//This condition only for DCAG
			String manufacturer = countingList_DTO.getManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING: countingList_DTO.getManufacturer();
			String tnrsFrom = null;
			String tnrsTo = null;
			if(manufacturer.contentEquals("DCAG") && countingList_DTO.isPartNumberRangeFlag() && !countingList_DTO.isAllParts()) {
				if(countingList_DTO.getFromPartNumber()!=null && !countingList_DTO.getFromPartNumber().isEmpty()) {
					tnrsFrom = getTNRSValue(countingList_DTO, schema,  dataLibrary,  selectionType, listId, "FROM_TNR");	
				}
				if(countingList_DTO.getToPartNumber()!=null && !countingList_DTO.getToPartNumber().isEmpty()) {
					tnrsTo = getTNRSValue(countingList_DTO, schema,  dataLibrary,  selectionType, listId, "TO_TNR");	
				}
			}
			
			if(manufacturer.contentEquals("DCAG") && !isNullOrEmpty(tnrsFrom)  && !isNullOrEmpty(tnrsTo) &&
					countingList_DTO.isPartNumberRangeFlag() && !countingList_DTO.isAllParts()) {
				
				log.info("Parts(TNRS) not available in the selected record type.");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.PARTS_NOT_EXISTS_MSG_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.PARTS_NOT_EXISTS_MSG_KEY));
				throw exception;
			}
			
			String whereClauseCondition = "";
			whereClauseCondition = removeOrderByClouse(createWhereClause(countingList_DTO, schema, dataLibrary, selectionType, listId ,false,tnrsFrom, tnrsTo));
			
			log.info("Check Parts existing in E_ETSTAMM or Not");
			StringBuilder checkValueInEtstamm = new StringBuilder("SELECT COUNT(tnr) AS COUNT FROM ").append(dataLibrary).append(".e_etstamm t where ");
			checkValueInEtstamm.append(whereClauseCondition);
			
			String countForEtstamm = dbServiceRepository.getCountUsingQuery(checkValueInEtstamm.toString());
			if(Integer.parseInt(countForEtstamm) == 0){
				log.info("Part(s) not available in the selected record type.");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.PARTS_NOT_EXISTS_MSG_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.PARTS_NOT_EXISTS_MSG_KEY));
				throw exception;
			}
			
			StringBuilder whereClause = createWhereClause(countingList_DTO, schema, dataLibrary, selectionType, listId ,true,tnrsFrom, tnrsTo);
			whereClauseCondition = removeOrderByClouse(whereClause);
			log.info("Check Parts existing in O_INVPART or Not");
			StringBuilder checkConditionForCountingList = new StringBuilder("SELECT COUNT(tnr) AS COUNT FROM ").append(dataLibrary).append(".e_etstamm t where ");
			checkConditionForCountingList.append(whereClauseCondition);
			
			String count = dbServiceRepository.getCountUsingQuery(checkConditionForCountingList.toString());
			if(Integer.parseInt(count) == 0){
				log.info("Selection unsuccessful, the parts already exist on other counting lists");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.PARTS_LIST_ALREADY_EXISTS_MSG_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.PARTS_LIST_ALREADY_EXISTS_MSG_KEY));
				throw exception;
			}
				
				StringBuilder query_for_procedure  = new StringBuilder("BEGIN DECLARE lid BIGINT; ");
				query_for_procedure.append(" FOR v CURSOR FOR ");
				query_for_procedure.append(" Select herst as xOEM, tnr AS xTNR, benen AS xPARTNAME, lopa AS xPLACE, lnr as xLNR, sa as xSA, ta AS xTA, TMARKE AS xTMARKE, TNRS AS xTNRS, TNRD AS xTNRD ,");
				query_for_procedure.append(" CASE WHEN (select count(*) from ").append(dataLibrary).append(".e_lago where tnr=t.tnr and lnr=t.lnr and prio > 1 ) > 0  ");
				query_for_procedure.append(" THEN (select count(*) from ").append(dataLibrary).append(".e_lago where tnr=t.tnr and lnr=t.lnr and prio > 1 )+1  ELSE '1' ");
				query_for_procedure.append(" end as xCOUNT_OF_PLACES from ").append(dataLibrary);
				query_for_procedure.append(".e_etstamm t where ").append(whereClause);
				query_for_procedure.append(" DO ");
				query_for_procedure.append(" FOR w CURSOR FOR ");
				query_for_procedure.append(" select lopa as yPLACE from ").append(dataLibrary).append(".e_ETSTAMM where tnr=xTNR and lnr=xLNR  UNION ");
				query_for_procedure.append(" select lopa as yPLACE from ").append(dataLibrary).append(".e_lago where tnr=xTNR and lnr=xLNR AND PRIO>1 ");
				query_for_procedure.append(" DO ");
				query_for_procedure.append(" INSERT INTO  ").append(schema);
				query_for_procedure.append(".O_INVPART (INVPARTSTS_ID, INVLIST_ID, OEM, TNR, PARTNAME, PLACE, COUNT_OF_PLACES, SA, TA, TMARKE,TNRS,TNRD) ");
				query_for_procedure.append(" VALUES (0, ").append(invListId).append(", xOEM, xTNR, xPARTNAME, yPLACE, xCOUNT_OF_PLACES, xSA, xTA, xTMARKE , xTNRS, xTNRD); ");
				query_for_procedure.append("END FOR; END FOR; END ");

			boolean executionFlag = dbServiceRepository.excuteProcedure(query_for_procedure.toString(),con);

			if(executionFlag){
				// insert value in O_INVCRITERIA

				String query_for_invCriteria = createQueryForInventoryCriteria(countingList_DTO,schema,dataLibrary, loginUser, listId);
				int invCriteriaId = dbServiceRepository.insertResultUsingQuery(query_for_invCriteria.toString(),con);

				if (invCriteriaId == 0) {
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.CREATE_FAILED_MSG_KEY, "Inventory Criteria"));
					log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY, "Inventory Criteria"), exception);
					throw exception;
				}
			}

			procedureOutput.put("isUpdated", true);
			con.commit();
			con.setAutoCommit(true);
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Add Parts In Existing Counting List"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Add Parts In Existing Counting List"), exception);
			throw exception;
		}

		return procedureOutput;
	}


	/**
	 * This method is used to Save / Update differential list into O_INVPART table.
	 */
	@Override
	public Map<String, Boolean> saveDifferentialList ( String schema, List<InventoryDiferentialListDTO> differntialDTOList, 
			String inventoryListId, boolean selectAllFlag, String opinionValue ) {

		log.info("Inside saveDifferentialList method of InventoryServiceImpl");

		Map<String, Boolean> programOutput = new HashMap<String, Boolean>();
		int[] records = null;
		programOutput.put("isUpdated", false);		
		String manufacturer = "";

		try(Connection con = dbServiceRepository.getConnectionObject();
				Statement stmt = con.createStatement();) {

			if(selectAllFlag) {
				differntialDTOList = getAllDifferentialList(schema, inventoryListId, opinionValue);
			}

			if(differntialDTOList!=null && !differntialDTOList.isEmpty()){

				for(InventoryDiferentialListDTO differntialListDTO : differntialDTOList) {
					if(differntialListDTO.getCheckItFlag().equalsIgnoreCase("1")) {

						manufacturer = differntialListDTO.getManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING) ? RestInputConstants.DCAG_STRING 
								: differntialListDTO.getManufacturer();

						StringBuilder updateCountingList = new StringBuilder("Update ").append(schema).append(".O_INVPART SET ");
						updateCountingList.append(" INVPARTSTS_ID = 3 where INVLIST_ID=").append(differntialListDTO.getInventoryListId());
						updateCountingList.append(" and OEM ='").append(manufacturer).append("' and TNR = '").append(differntialListDTO.getPartNumber()).append("' ");

						log.info("Get Results Using Query :" + updateCountingList.toString());

						stmt.addBatch(updateCountingList.toString());
					}
					else {

						manufacturer = differntialListDTO.getManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING) ? RestInputConstants.DCAG_STRING 
								: differntialListDTO.getManufacturer();

						StringBuilder updateCountingList = new StringBuilder("Update ").append(schema).append(".O_INVPART SET ");
						updateCountingList.append(" INVPARTSTS_ID = 2 where INVLIST_ID=").append(differntialListDTO.getInventoryListId());
						updateCountingList.append(" and OEM ='").append(manufacturer).append("' and TNR = '").append(differntialListDTO.getPartNumber()).append("' ");

						log.info("Get Results Using Query :" + updateCountingList.toString());

						stmt.addBatch(updateCountingList.toString());
					}
				}
				records =  dbServiceRepository.insertResultUsingBatchQuery(stmt);
			}
			if(records != null){
				log.info("Counting list value - Total rows updated :"+records.length);
			}
			con.commit();
			con.setAutoCommit(true);
			programOutput.put("isUpdated", true);

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(
					LocaleContextHolder.getLocale(), ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Counting list value"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Counting list value"),exception);
			throw exception;
		}

		return programOutput;
	}



	/**
	 * This method is used to get all the differential list details from O_INVPART table.
	 */
	public List<InventoryDiferentialListDTO> getAllDifferentialList(String schema, String inventoryListId, String opinionValue) {

		log.info("Inside getAllDifferentialList method of InventoryServiceImpl");
		List<InventoryDiferentialListDTO> differntialDTOList = new ArrayList<InventoryDiferentialListDTO>();

		try {			

			StringBuilder differntialListQuery = new StringBuilder("Select distinct TNR, OEM, PARTNAME, OLD_STOCK, (Select sum(NEW_STOCK) from ");
			differntialListQuery.append(schema).append(".O_INVPART where INVLIST_ID = ").append(inventoryListId).append(" and (INVPARTSTS_ID = 2 OR INVPARTSTS_ID = 3) ");
			differntialListQuery.append(" and tnr = t.tnr and oem = t.oem ").append(") as SUM_NEW_STOCK, ");
			differntialListQuery.append("DAK, DELTA_PIECES, DELTA_CUR, ");

			differntialListQuery.append(" '1' as Check_it, INVPARTSTS_ID, INVLIST_ID  from ");

			differntialListQuery.append(schema).append(".O_INVPART t where INVLIST_ID = ").append(inventoryListId).append(" AND INVPARTSTS_ID > 0 ");

			if(opinionValue.equalsIgnoreCase("1")) {
				differntialListQuery.append(" AND DELTA_PIECES = 0 ");
			}
			else if(opinionValue.equalsIgnoreCase("2")) {
				differntialListQuery.append(" AND NOT DELTA_PIECES = 0 ");
			}

			differntialListQuery.append(" ORDER BY DELTA_CUR ASC");

			List<InventoryParts> inventoryDiffParts = dbServiceRepository.getResultUsingQuery(InventoryParts.class, differntialListQuery.toString(), true);

			if (inventoryDiffParts != null && !inventoryDiffParts.isEmpty()) {
				for (InventoryParts invDiffParts : inventoryDiffParts) {

					InventoryDiferentialListDTO diffListDTO = new InventoryDiferentialListDTO();
					diffListDTO.setManufacturer(invDiffParts.getManufacturer().equalsIgnoreCase(RestInputConstants.DCAG_STRING) ? 
							RestInputConstants.DAG_STRING : invDiffParts.getManufacturer());
					diffListDTO.setPartNumber(invDiffParts.getPartNumber());
					diffListDTO.setPartName(invDiffParts.getPartNAME());
					diffListDTO.setInventoryPartStatusId(String.valueOf(invDiffParts.getPartStatusId()));

					diffListDTO.setCount(invDiffParts.getCount() != null ? String.valueOf(invDiffParts.getCount()) : "");
					diffListDTO.setOldStock(invDiffParts.getOldStock() != null ? String.valueOf(decimalformat_twodigit.format(invDiffParts.getOldStock())) : "");
					diffListDTO.setSum_newStock(invDiffParts.getSum_newStock() != null ? String.valueOf(decimalformat_twodigit.format(invDiffParts.getSum_newStock())) : "");
					diffListDTO.setDak(invDiffParts.getDak() != null ? String.valueOf(decimalformat_twodigit.format(invDiffParts.getDak())) : "");
					diffListDTO.setDeltaPieces(invDiffParts.getDeltaPieces()!=null ? String.valueOf(decimalformat_twodigit.format(invDiffParts.getDeltaPieces())) : "");
					diffListDTO.setDeltaCur(invDiffParts.getDeltaCur() != null ? String.valueOf(decimalformat_twodigit.format(invDiffParts.getDeltaCur())) : "");
					diffListDTO.setCheckItFlag(invDiffParts.getCheckIt() != null ? invDiffParts.getCheckIt() : "");
					diffListDTO.setInventoryPartId(String.valueOf(invDiffParts.getInventoryPartId()));
					diffListDTO.setInventoryListId(invDiffParts.getInventoryListId().toString());

					differntialDTOList.add(diffListDTO);
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(
					LocaleContextHolder.getLocale(), ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "differential list"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "differential list"),exception);
			throw exception;
		}

		return differntialDTOList;
	}


	/**
	 * This method is used the process the counting list after it is fully counted based on provided inventory List id.
	 */
	@Override
	public Map<String, Boolean> processCountingListAsFullyCounted(String dataLibrary, String schema, String loggedInUser, String inventoryListId) {

		log.info("Inside processCountingListAsFullyCounted method of InventoryServiceImpl");

		Map<String, Boolean> processListOutput = new HashMap<String, Boolean>();
		processListOutput.put("isUpdated", false);
		boolean statusFlag = false;

		try(Connection con = dbServiceRepository.getConnectionObject();) {

			genericInventoryStatusCheck(schema, inventoryListId, "3");

			//update the inventory Status to 4 in INVLIST
			statusFlag = updateInventoryStatusToProcessed(con, schema, inventoryListId);

			if(statusFlag) {

				//Check multiple places (are all locations counted?) using stored procedure
				verifyMultipleLocations(con, schema, inventoryListId);

				//calculate difference of part using using stored procedure
				calculateDiffernces(con, schema, inventoryListId);

				//Update Inv_Part status to 3 for parts having  DELTA_PIECES=0 
				updateInvPartStatus(con, schema, inventoryListId);

				//Create New List from uncounted parts (If Exists)
				createNewCountedList(con, schema, loggedInUser, inventoryListId);

				processListOutput.put("isUpdated", true);

			}
			else {
				log.info("Inventory List Status is already set to 4...");
			}
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Process counting List as fully Counted"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Process counting List as fully Counted"), exception);
			throw exception;
		}

		return processListOutput;
	}


	/**
	 * This method is used to set the status of Inventory list.
	 * @param con
	 * @param schema
	 * @param inventoryListId
	 * @return
	 */
	private boolean updateInventoryStatusToProcessed( Connection con, String schema, String inventoryListId ) {

		log.info("Inside updateInventoryStatusToProcessed method of InventoryServiceImpl");
		boolean updateStatus = false;

		try {
			StringBuilder selectQuery = new StringBuilder("select INVLISTSTS_ID from ").append(schema).append(".O_INVLIST Where INVLIST_ID=").append(inventoryListId);

			List<InventoryList> inventoryStatusList = dbServiceRepository.getResultUsingQuery(InventoryList.class, selectQuery.toString(), true);

			if(inventoryStatusList!=null && !inventoryStatusList.isEmpty()) {
				Integer statusId = inventoryStatusList.get(0).getInventoryList_Status_Id().intValue();

				if(statusId == 3) {

					StringBuilder updateQuery = new StringBuilder("Update ").append(schema).append(".O_INVLIST SET INVLISTSTS_ID = 4 ");
					updateQuery.append(" where INVLIST_ID =").append(inventoryListId);

					boolean updateFlag = dbServiceRepository.updateResultUsingQuery(updateQuery.toString(), con);

					if (updateFlag) {
						updateStatus = true;
					}
					else {
						log.info("Unable to update Status Id in O_INVLIST for this Id : {}", inventoryListId);
						AlphaXException exception = new AlphaXException(messageService.createApiMessage(
								LocaleContextHolder.getLocale(), ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "InventoryList"));
						log.error(messageService.getReadableMessage(ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "InventoryList"));
						throw exception;
					}					
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Inventory Status"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Inventory Status"), exception);
			throw exception;
		}

		return updateStatus;
	}


	/**
	 * This method is used to Check multiple places (are all locations counted?) using stored procedure.
	 * @param con
	 * @param schema
	 * @param inventoryListId
	 * @return
	 */
	private boolean verifyMultipleLocations( Connection con, String schema, String inventoryListId ) throws Exception{
		log.info("Inside verifyMultipleLocations method of InventoryServiceImpl");

		StringBuilder query_for_procedure  = new StringBuilder(" BEGIN  DECLARE lid BIGINT; ");
		query_for_procedure.append(" FOR v CURSOR FOR ");
		query_for_procedure.append(" Select DISTINCT TNR as xTNR, OEM as xOEM, COUNT_OF_PLACES AS xCOP from ").append(schema).append(".O_INVPART WHERE INVLIST_ID =");
		query_for_procedure.append(inventoryListId).append(" AND INVPARTSTS_ID = 1 AND COUNT_OF_PLACES > 1 ");
		query_for_procedure.append(" DO ");
		query_for_procedure.append("IF (select COUNT(*) from ").append(schema).append(".O_INVPART WHERE INVLIST_ID =");
		query_for_procedure.append(inventoryListId).append(" AND TNR = xTNR  AND OEM = xOEM  AND INVPARTSTS_ID = 1) < xCOP  then ");
		query_for_procedure.append(" UPDATE ").append(schema).append(".O_INVPART SET INVPARTSTS_ID = 0  WHERE TNR = xTNR AND OEM = xOEM AND INVLIST_ID =").append(inventoryListId).append("; ");
		query_for_procedure.append(" END IF;  END FOR;  END ");

		boolean executionFlag = dbServiceRepository.excuteProcedure(query_for_procedure.toString(), con);

		return executionFlag;
	}


	/**
	 * This method is used to calculate difference of part using using stored procedure.
	 * @param con
	 * @param schema
	 * @param inventoryListId
	 * @return
	 */
	private boolean calculateDiffernces( Connection con, String schema, String inventoryListId ) throws Exception{
		log.info("Inside calculateDiffernces method of InventoryServiceImpl");

		StringBuilder query_for_calculate  = new StringBuilder(" BEGIN  DECLARE lid BIGINT; ");
		query_for_calculate.append(" DECLARE xBES NUMERIC (9, 2); ");
		query_for_calculate.append(" FOR v CURSOR FOR ");
		query_for_calculate.append(" SELECT distinct TNR as xTNR, DAK as xDAK, OLD_STOCK as xOLD, OEM as XOEM from ").append(schema).append(".O_INVPART WHERE INVLIST_ID =");
		query_for_calculate.append(inventoryListId).append(" AND INVPARTSTS_ID = 1 ");
		query_for_calculate.append(" DO ");
		query_for_calculate.append(" SET xBES = (Select sum(NEW_STOCK) from ").append(schema).append(".O_INVPART WHERE tnr = xTNR and oem = xOEM and INVLIST_ID =");
		query_for_calculate.append(inventoryListId).append(" AND INVPARTSTS_ID = 1); ");
		query_for_calculate.append(" UPDATE ").append(schema).append(".O_INVPART SET INVPARTSTS_ID=2, DELTA_PIECES = (xBES - xOLD), DELTA_CUR = ((xBES - xOLD) * xDAK) ");
		query_for_calculate.append(" WHERE TNR = xTNR AND OEM = xOEM AND INVLIST_ID=").append(inventoryListId).append("; ");
		query_for_calculate.append(" END FOR;  END ");

		boolean executionFlag = dbServiceRepository.excuteProcedure(query_for_calculate.toString(), con);

		return executionFlag;
	}


	/**
	 * This method is used to update the status to 3 of O_INVPART if DELTA_PIECES = 0.
	 * @param con
	 * @param schema
	 * @param inventoryListId
	 * @return
	 */
	private void updateInvPartStatus( Connection con, String schema, String inventoryListId ) throws Exception{

		log.info("Inside updateInvPartStatus method of InventoryServiceImpl");

		StringBuilder updateQuery = new StringBuilder("Update ").append(schema).append(".O_INVPART set INVPARTSTS_ID = 3 ");
		updateQuery.append(" WHERE INVLIST_ID =").append(inventoryListId).append(" AND DELTA_PIECES = 0 ");

		dbServiceRepository.updateResultUsingQuery(updateQuery.toString(), con);		
	}


	/**
	 * This method is used to Create New List from uncounted parts (If Exists).
	 * @param con
	 * @param schema
	 * @param inventoryListId
	 * @return
	 */
	private void createNewCountedList( Connection con, String schema, String loggedInUser, String inventoryListId ) throws Exception{
		log.info("Inside createNewCountedList method of InventoryServiceImpl");

		StringBuilder countedQuery = new StringBuilder("Select count(*) AS COUNT from ").append(schema).append(".O_INVPART where INVPARTSTS_ID = 0  AND INVLIST_ID =").append(inventoryListId);

		String count = dbServiceRepository.getCountUsingQuery(countedQuery.toString(), con);

		//if at least one part is not counted it makes sense to execute below SP
		if(Integer.parseInt(count) > 0) {

			StringBuilder query_for_Insert  = new StringBuilder("Insert into ");
			query_for_Insert.append(schema).append(".O_INVLIST (NAME, INVLISTSTS_ID, CREATED_TS, CREATED_BY, WAREHOUSE_ID, WAREHOUSE_NAME, ORIGIN_ID, SORT_BY) ");
			query_for_Insert.append(" VALUES ( ").append("'Ungezählt' || (select name from ").append(schema).append(".O_INVLIST where INVLIST_ID =").append(inventoryListId);
			query_for_Insert.append("), 0, now(), '").append(loggedInUser).append("', (select WAREHOUSE_ID from ").append(schema).append(".O_INVLIST where INVLIST_ID =").append(inventoryListId);
			query_for_Insert.append("), (select WAREHOUSE_NAME from ").append(schema).append(".O_INVLIST where INVLIST_ID =").append(inventoryListId);
			query_for_Insert.append("), 0, (select SORT_BY from ").append(schema).append(".O_INVLIST where INVLIST_ID =").append(inventoryListId).append(")) ");

			int newInventoryListId = dbServiceRepository.insertResultUsingQuery(query_for_Insert.toString(), con);

			insertValueIn_InvType(String.valueOf(newInventoryListId), inventoryListId, "2",loggedInUser, con, schema);
			
			StringBuilder sp_for_createNew  = new StringBuilder(" BEGIN  DECLARE lid BIGINT; ");
			sp_for_createNew.append(" DECLARE olid BIGINT; ");
			sp_for_createNew.append(" FOR v CURSOR FOR ");
			sp_for_createNew.append(" Select oem as xOEM, tnr AS xTNR, partname AS xPARTNAME, place AS xPLACE, COUNT_OF_PLACES as xPLACES, SA as xSA, TA as xTA, TMARKE AS xTMARKE , TNRS AS xTNRS, TNRD AS xTNRD from ");
			sp_for_createNew.append(schema).append(".O_INVPART where INVLIST_ID =").append(inventoryListId).append(" AND INVPARTSTS_ID = 0 ");
			sp_for_createNew.append(" DO ");
			sp_for_createNew.append(" INSERT INTO ").append(schema).append(".O_INVPART (INVPARTSTS_ID, INVLIST_ID, OEM, TNR, PARTNAME, PLACE, COUNT_OF_PLACES, SA, TA, TMARKE ,TNRS,TNRD) VALUES ");
			sp_for_createNew.append(" (0,").append(newInventoryListId).append(", xOEM, xTNR, xPARTNAME, xPLACE, xPLACES, xSA, xTA, xTMARKE,xTNRS,xTNRD ); ");
			sp_for_createNew.append(" END FOR;  END ");

			dbServiceRepository.excuteProcedure(sp_for_createNew.toString(), con);			
		}		
	}


	/**
	 * This method is used Delete a part from counting list based on part no and OEM.
	 */
	@Override
	public Map<String, Boolean> deletePartsFromCountingList(String dataLibrary, String schema, String partNo, 
			String manufacturer,String numberOfPlace, String invPartId) {

		log.info("Inside deletePartsFromCountingList method of InventoryServiceImpl");

		Map<String, Boolean> deletePartsOutput = new HashMap<String, Boolean>();
		deletePartsOutput.put("isDeleted", false);
		boolean deleteFlag = false;

		try(Connection con = dbServiceRepository.getConnectionObject();) { 

			manufacturer = (manufacturer != null && manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING))? RestInputConstants.DCAG_STRING : manufacturer;
			StringBuilder query  = new StringBuilder("DELETE FROM ").append(schema).append(".O_INVPART WHERE ");
			if(Integer.parseInt(numberOfPlace) > 1) {
				query.append("OEM= '").append(manufacturer).append("' AND TNR= '").append(partNo).append("' ");
				deleteFlag = dbServiceRepository.deleteResultUsingQuery(query.toString(), con);
			}else {
				query.append("ID= ").append(invPartId);
				deleteFlag = dbServiceRepository.deleteResultUsingQuery(query.toString(), con);
			}

			if(deleteFlag){
				deletePartsOutput.put("isDeleted", true);
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.DELETE_ADMIN_SETTING_FAILED_MSG_KEY, "Part from inventory list"));
			log.error(messageService.getReadableMessage(ExceptionMessages.DELETE_ADMIN_SETTING_FAILED_MSG_KEY, "Part from inventory list"), exception);
			throw exception;
		}

		return deletePartsOutput;
	}


	/**
	 * This method is used to get all closed/completed counting inventory List from O_INVLIST table.
	 */
	@Override
	public List<ActivatedCountingListDTO> getClosedCountingList(String dataLibrary, String schema, String allowedWarehouses) {

		log.info("Inside getClosedCountingList method of InventoryServiceImpl");

		List<ActivatedCountingListDTO> countingListDTOs = new ArrayList<ActivatedCountingListDTO>();
		
		try {			
			//validate the Warehouse Ids
			validateWarehouses(allowedWarehouses);

			StringBuilder queryForInvtList = new StringBuilder("Select distinct WAREHOUSE_ID, WAREHOUSE_NAME, (Select COUNT(*) as COUNT from ");
			queryForInvtList.append(schema).append(".O_INVLIST where INVLISTSTS_ID = 6 and WAREHOUSE_ID = inv.WAREHOUSE_ID ) from ");
			queryForInvtList.append(schema).append(".O_INVLIST inv where inv.INVLISTSTS_ID = 6 ");
			queryForInvtList.append(" and WAREHOUSE_ID IN(").append(allowedWarehouses).append(") order by WAREHOUSE_ID");
			
			List<InventoryList> inventoryListDetails = dbServiceRepository.getResultUsingQuery(InventoryList.class, queryForInvtList.toString(), true);

			if (inventoryListDetails != null && !inventoryListDetails.isEmpty()) {
				for(InventoryList inventoryList : inventoryListDetails){

					ActivatedCountingListDTO listDTO = new ActivatedCountingListDTO();

					listDTO.setWarehouseId(inventoryList.getWarehouseId().toString());
					listDTO.setWarehouseName(inventoryList.getWarehouseName());
					listDTO.setInventoryCount(inventoryList.getInventoryCount().toString());
					countingListDTOs.add(listDTO);
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(
					LocaleContextHolder.getLocale(), ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Closed counting list"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Closed counting list"),exception);
			throw exception;
		}

		return countingListDTOs;
	}


	/**
	 * This method is used to get selected activated counting inventory List details from O_INVLIST table.
	 */
	@Override
	public GlobalSearch getClosedCountingDetailList(String dataLibrary, String schema, String warehouseNo, String pageSize, String pageNumber) {

		log.info("Inside getClosedCountingDetailList method of InventoryServiceImpl");

		GlobalSearch globalSearchList = new GlobalSearch();
		List<ActivatedCountingListDTO> closedCountingDTOList = new ArrayList<ActivatedCountingListDTO>();

		if(pageSize==null || pageNumber==null || pageSize.isEmpty() || pageNumber.isEmpty()){
			pageSize = RestInputConstants.DEFAULT_PAGE_SIZE;
			pageNumber = RestInputConstants.DEFAULT_PAGE_NO;
		}

		int totalRecords = Integer.parseInt(pageSize);
		int nextRows = totalRecords * (Integer.parseInt(pageNumber) - 1);

		//validate the page size
		validatePageSize(totalRecords);
		log.info("PageSize  : {}  OFFSET Size (Skip Rows):  {} Page Number  : {} ", pageSize, nextRows, pageNumber);

		try {

			StringBuilder queryForInvtList = new StringBuilder("Select INVLIST_ID, NAME, VARCHAR_FORMAT(CREATED_TS, 'DD.MM.YYYY HH24:MI') AS CREATED_TS, INVLISTSTS_ID, ");
			queryForInvtList.append(" (Select STATUS_DESC from ");
			queryForInvtList.append(schema).append(".O_INVST where INVLISTSTS_ID = x.INVLISTSTS_ID) as INVLISTSTS_DESC, ( SELECT COUNT(*) from ");
			queryForInvtList.append(schema).append(".O_INVPART where INVLIST_ID = x.INVLIST_ID ) AS countingPosition, ");
			queryForInvtList.append("( SELECT COUNT(*) from ");
			queryForInvtList.append(schema).append(".O_INVLIST where INVLISTSTS_ID = 6 AND WAREHOUSE_ID = ").append(warehouseNo);
			queryForInvtList.append(" ) AS ROWNUMER  from ");
			queryForInvtList.append(schema).append(".O_INVLIST x where INVLISTSTS_ID = 6 AND WAREHOUSE_ID = ").append(warehouseNo);
			queryForInvtList.append(" order by CREATED_TS desc ").append(" OFFSET ");
			queryForInvtList.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");

			List<InventoryList> closedInvListDetails = dbServiceRepository.getResultUsingQuery(InventoryList.class, queryForInvtList.toString(), true);

			if (closedInvListDetails != null && !closedInvListDetails.isEmpty()) {
				for(InventoryList inventoryList : closedInvListDetails){

					ActivatedCountingListDTO listDTO = new ActivatedCountingListDTO();

					listDTO.setWarehouseId(warehouseNo);
					listDTO.setInventoryListId(inventoryList.getInventoryListId().toString());
					listDTO.setInventoryListName(inventoryList.getInventoryListName());

					listDTO.setCreationDate(inventoryList.getCreationDate() != null ? inventoryList.getCreationDate() : "");

					listDTO.setInventoryList_Status_Id(inventoryList.getInventoryList_Status_Id().toString());
					listDTO.setStatusDescription(inventoryList.getStatusDescription());

					listDTO.setInventoryPartsCount(inventoryList.getCountingPosition());

					closedCountingDTOList.add(listDTO);
				}

				globalSearchList.setSearchDetailsList(closedCountingDTOList);
				globalSearchList.setTotalPages(Integer.toString(closedInvListDetails.get(0).getTotalCount()));
				globalSearchList.setTotalRecordCnt(Integer.toString(closedInvListDetails.get(0).getTotalCount()));
			}	
			else {
				globalSearchList.setSearchDetailsList(closedCountingDTOList);
				globalSearchList.setTotalPages(Integer.toString(0));
				globalSearchList.setTotalRecordCnt(Integer.toString(0));
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(
					LocaleContextHolder.getLocale(), ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Closed Counting list details"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Closed Counting list details"),exception);
			throw exception;
		}

		return globalSearchList;
	}


	/**
	 * This method is used the mark the differential list As fully processed based on provided inventory List id.
	 */
	@Override
	public Map<String, Boolean> markDifferentialListAsFullyProcessed(String dataLibrary, String schema, String loggedInUser, 
			String companyId, String agencyId, String warehouseNo,String inventoryListId, String savLibrary) {

		log.info("Inside markDifferentialListAsFullyProcessed method of InventoryServiceImpl");

		Map<String, Boolean> processListOutput = new HashMap<String, Boolean>();
		processListOutput.put("isFullyProcessed", false);
		boolean statusFlag = false;

		try(Connection con = dbServiceRepository.getConnectionObject();) {

			 con.setAutoCommit(false);
			//if status is still 4 then update the inventory Status to 5 in INVLIST table
			statusFlag = updateInventoryListStatus(con, schema, inventoryListId);

			if(statusFlag) {
				//check if count > 0 then Create list from items with differences
				StringBuilder differencesCount = new StringBuilder("Select count(*) as count from ").append(schema).append(".O_INVPART ");
				differencesCount.append(" WHERE INVPARTSTS_ID=2 AND INVLIST_ID=").append(inventoryListId);
				String count = dbServiceRepository.getCountUsingQuery(differencesCount.toString());

				if(Integer.parseInt(count) > 0){
					createListWithDifferences(con, schema, inventoryListId,loggedInUser);
				}
				//Call BA67 for checked items from list (Check CPSDAT)	

				checkedItemsFromListforBA67(con, schema,  inventoryListId,dataLibrary,companyId, agencyId, warehouseNo, loggedInUser);
				
				//Create LIST from REJECTED items
				StringBuilder rejectCount = new StringBuilder("Select count(*) as count from ").append(schema).append(".O_INVPART ");
				rejectCount.append(" WHERE INVPARTSTS_ID=3 AND INVLIST_ID=").append(inventoryListId);

				count = dbServiceRepository.getCountUsingQuery(rejectCount.toString());
				if(Integer.parseInt(count) > 0){
					createListWithRejectedItems(con, schema, inventoryListId,loggedInUser);
				}
				
				Map<String, Boolean>  copiedInventoryDtlsFlag = copyInventoryDtlsIntoAlphaplusHistory(dataLibrary, savLibrary, schema,
						inventoryListId, warehouseNo);
				
				if(copiedInventoryDtlsFlag.get("isCopiedInventoryDtls")) {
				//Mark list as done
				StringBuilder updateQuery = new StringBuilder("Update ").append(schema).append(".O_INVLIST SET INVLISTSTS_ID=6 ");
				updateQuery.append("where INVLIST_ID =").append(inventoryListId);
				boolean closedFlag = dbServiceRepository.updateResultUsingQuery(updateQuery.toString(), con);

				if(closedFlag){
					con.commit();
					con.setAutoCommit(true);
					processListOutput.put("isFullyProcessed", true);	
				}
			  }
			}
			else {
				log.info("Inventory List Status is not 4.");
				log.info("The status of the counting list has been changed in the meantime: {}", inventoryListId);
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(
						LocaleContextHolder.getLocale(), ExceptionMessages.COUNTING_LIST_STATUS_FAILED_MSG_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.COUNTING_LIST_STATUS_FAILED_MSG_KEY));
				throw exception;

			}
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Mark Differential List As Fully Processed"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Mark Differential List As Fully Processed"), exception);
			throw exception;
		}

		return processListOutput;
	}

	/**
	 * This method is used to set the status of Inventory list.
	 * @param con
	 * @param schema
	 * @param inventoryListId
	 * @return
	 */
	private boolean updateInventoryListStatus( Connection con, String schema, String inventoryListId ) {

		log.info("Inside updateInventoryListStatus method of InventoryServiceImpl");
		boolean updateStatus = false;

		try {
			StringBuilder selectQuery = new StringBuilder("select INVLISTSTS_ID from ").append(schema).append(".O_INVLIST Where INVLIST_ID=").append(inventoryListId);

			List<InventoryList> inventoryStatusList = dbServiceRepository.getResultUsingQuery(InventoryList.class, selectQuery.toString(), true);

			if(inventoryStatusList!=null && !inventoryStatusList.isEmpty()) {
				Integer statusId = inventoryStatusList.get(0).getInventoryList_Status_Id().intValue();

				if(statusId == 4) {

					StringBuilder updateQuery = new StringBuilder("Update ").append(schema).append(".O_INVLIST SET INVLISTSTS_ID = 5 ");
					updateQuery.append(" where INVLIST_ID =").append(inventoryListId);

					boolean updateFlag = dbServiceRepository.updateResultUsingQuery(updateQuery.toString(), con);

					if (updateFlag) {
						updateStatus = true;
					}
					else {
						log.info("Unable to update Status Id in O_INVLIST for this Id : {}", inventoryListId);
						AlphaXException exception = new AlphaXException(messageService.createApiMessage(
								LocaleContextHolder.getLocale(), ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "InventoryList"));
						log.error(messageService.getReadableMessage(ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "InventoryList"));
						throw exception;
					}					
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Inventory Status"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Inventory Status"), exception);
			throw exception;
		}

		return updateStatus;
	}


	

	private void createListWithDifferences(Connection con, String schema, String inventoryListId, String loggedInUser) throws Exception {
		
		log.info("Inside createListWithDifferences method of InventoryServiceImpl");

		StringBuilder query_for_invList = new StringBuilder("INSERT INTO ").append(schema).append(".O_INVLIST");
		query_for_invList.append("(NAME, INVLISTSTS_ID,CREATED_TS, CREATED_BY, WAREHOUSE_ID, WAREHOUSE_NAME, ORIGIN_ID, SORT_BY) ");
		query_for_invList.append("VALUES( ");
		query_for_invList.append(" 'Differenzen' || ").append("(select name from ").append(schema).append(".O_INVLIST where INVLIST_ID =").append(inventoryListId).append("), ");
		query_for_invList.append("0").append(",");
		query_for_invList.append("now() ").append(",");
		query_for_invList.append("'").append(loggedInUser).append("',");
		query_for_invList.append("(select WAREHOUSE_ID from ").append(schema).append(".O_INVLIST where INVLIST_ID=").append(inventoryListId).append("),");
		query_for_invList.append("(select WAREHOUSE_NAME from ").append(schema).append(".O_INVLIST where INVLIST_ID=").append(inventoryListId).append("),");
		query_for_invList.append("0").append(", (");
		query_for_invList.append("select SORT_BY from ").append(schema).append(".O_INVLIST where INVLIST_ID=").append(inventoryListId).append(" ))");

		int newInvListId = dbServiceRepository.insertResultUsingQuery(query_for_invList.toString(),con);

		if (newInvListId == 0) {
			AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.CREATE_FAILED_MSG_KEY, "Create list with difference "));
			log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY, "Create list with difference"), exception);
			throw exception;
		}else{
			
			insertValueIn_InvType(String.valueOf(newInvListId), inventoryListId, "3",loggedInUser, con, schema);
			
			StringBuilder query_for_procedure  = new StringBuilder("BEGIN DECLARE lid BIGINT; DECLARE olid BIGINT; ");
			query_for_procedure.append(" FOR v CURSOR FOR ");
			query_for_procedure.append(" Select oem as xOEM, tnr AS xTNR, partname AS xPARTNAME, place AS xPLACE, COUNT_OF_PLACES as xPLACES, SA as xSA, TA as xTA, TMARKE AS xTMARKE , TNRS AS xTNRS, TNRD AS xTNRD ");
			query_for_procedure.append(" FROM ").append(schema).append(".O_INVPART t where INVPARTSTS_ID=2 AND INVLIST_ID=").append(inventoryListId);
			query_for_procedure.append(" DO ");
			query_for_procedure.append(" INSERT INTO ").append(schema).append(".O_INVPART (INVPARTSTS_ID, INVLIST_ID, OEM, TNR, PARTNAME, PLACE, COUNT_OF_PLACES, SA, TA, TMARKE ,TNRS,TNRD )  ");
			query_for_procedure.append("VALUES (0, ").append(newInvListId).append(", xOEM, xTNR, xPARTNAME, xPLACE, xPLACES, xSA, xTA, xTMARKE ,xTNRS,xTNRD ); ");
			query_for_procedure.append(" END FOR; END ");

			dbServiceRepository.excuteProcedure(query_for_procedure.toString(),con);
		}
	}

	private void checkedItemsFromListforBA67(Connection con, String schema, String inventoryListId,
			String dataLibrary, String companyId, String agencyId, String warehouseNo, String loggedInUser) {

		log.info("Inside checkedItemsFromListforBA67 method of InventoryServiceImpl");

		StringBuilder query = new StringBuilder(
				"Select distinct TNR, OEM , (select SUM(NEW_STOCK) from  ").append(schema).append(".O_INVPART ");
		query.append("where INVLIST_ID=").append(inventoryListId).append(" AND oem=t.oem AND TNR=t.TNR) AS SUM_NEW_STOCK, (select Warehouse_ID from  ");
		query.append(schema).append(".O_INVLIST WHERE INVLIST_ID=t.INVLIST_ID) AS COMPARE_WRH, (select max(PRINT_DATE) from ");
		query.append(schema).append(".O_INVLIST where INVLIST_ID= ").append(inventoryListId).append(" AND oem=t.oem AND TNR=t.TNR) AS COMPARE_DATE, ");
		query.append(" (select VARCHAR_FORMAT(ACTIVATION_DATE, 'YYYYMMDD') from  ");
		query.append(schema).append(".O_INVLIST WHERE INVLIST_ID=t.INVLIST_ID) AS ACTIVATION_DATE From ");
		query.append(schema).append(".O_INVPART t where INVLIST_ID=").append(inventoryListId).append(" AND INVPARTsts_ID=3 ");

		List<InventoryParts> inventoryParts = dbServiceRepository.getResultUsingQuery(InventoryParts.class,
				query.toString(), true);

		if (inventoryParts != null && !inventoryParts.isEmpty()) {
			for (InventoryParts invParts : inventoryParts) {

				StringBuilder query_for_cpsdat = new StringBuilder("SELECT count(*) as count FROM ");
				query_for_cpsdat.append(dataLibrary).append(".e_cpsdat where ");
				query_for_cpsdat.append("to_date('20'|| jjmmtt || ' ' || right( '000000' || hhmmss , 6) , ");
				query_for_cpsdat.append(" 'YYYY-MM-DD HH24:mi:ss' ) > '").append(invParts.getCompareDate());
				query_for_cpsdat.append("' AND KB||ETNR||ES1||ES2 = '").append(invParts.getPartNumber());
				query_for_cpsdat.append("' AND HERST= '").append(invParts.getManufacturer());
				query_for_cpsdat.append("' AND VFNR = ").append(invParts.getCompareWRH()).append(" AND (BART=1 OR BART=2 OR BART=6 OR BART=25 OR BART=50 OR BART=67 OR BART=20 OR BART=22 OR BART=5) ");

					String cpsdat_count = dbServiceRepository.getCountUsingQuery(query_for_cpsdat.toString(), con);
					//Hotfix for task - ALPHAX-5477
					/*StringBuilder query_for_histo = new StringBuilder("SELECT count(*) as count FROM ");
					query_for_histo.append(dataLibrary).append(".e_HISTO where ");
					query_for_histo.append(" to_date('20'|| jjmmtt || ' ' || right( '000000' || hhmmss , 6) , 'YYYY-MM-DD HH24:mi:ss' ) >  '").append(invParts.getCompareDate());
					query_for_histo.append("' AND KB||ETNR||ES1||ES2 = '").append(invParts.getPartNumber());
					query_for_histo.append("' AND HERST= '").append(invParts.getManufacturer());
					query_for_histo.append("' AND VFNR = ").append(invParts.getCompareWRH());
					query_for_histo.append(" AND (BART=1 OR BART=2 OR BART=6 OR BART=25 OR BART=50 OR BART=67 OR BART=20 OR BART=22 OR BART=5) ");

					String histo_count = dbServiceRepository.getCountUsingQuery(query_for_histo.toString(), con);*/
					//&&  Integer.parseInt(histo_count) == 0

					if (Integer.parseInt(cpsdat_count) == 0 ) {
						// call BA 67
						companyId = StringUtils.leftPad(companyId, 2, "0");
						agencyId = StringUtils.leftPad(agencyId, 2, "0");
						warehouseNo = StringUtils.leftPad(invParts.getCompareWRH(), 2, "0");
						FinalizationsBA_DTO finalizationsBA = new FinalizationsBA_DTO();
						finalizationsBA.setLocation(companyId+agencyId+warehouseNo);
						finalizationsBA.setPartNumber(invParts.getPartNumber());
						finalizationsBA.setBookingAmount(String.valueOf(invParts.getSum_newStock())); 
						finalizationsBA.setInventoryDescription("alphaX Inv");
						finalizationsBA.setBusinessCases("67");
						finalizationsBA.setManufacturer(invParts.getManufacturer());
						finalizationsBA.setUserName(loggedInUser);
						finalizationsBA.setHerkType("CPS401");


						callBA67ForCheckedItems(finalizationsBA, dataLibrary, schema, companyId, agencyId,con,inventoryListId, 
								invParts.getActivationDate(),invParts.getSum_newStock()  );
					}

			}
		}

	}
	


	private void callBA67ForCheckedItems(FinalizationsBA_DTO finalizationsBA, String dataLibrary, String schema,
			String companyId, String agencyId, Connection con, String invListId, String activationDate, BigDecimal sumNewStock) {

		log.info("Inside callBA67ForCheckedItems method of InventoryServiceImpl");

		Map<String, String> programOutput = businessCasesServiceImpl.newJavaImplementation_BA67(finalizationsBA, dataLibrary,
				schema, companyId, agencyId,null);

		if (programOutput != null && !programOutput.isEmpty()) {

			Map.Entry<String, String> entry = programOutput.entrySet().iterator().next();

			if (entry.getKey().equalsIgnoreCase("00000")) {

				StringBuilder updateQuery = new StringBuilder("Update ").append(schema);
				updateQuery.append(".O_INVPART set INVPARTSTS_ID=4 ");
				updateQuery.append(", TRANSFER_DATE= now() WHERE INVLIST_ID=").append(invListId).append(" AND oem= '").append(finalizationsBA.getManufacturer()).append("' ");
				updateQuery.append(" AND TNR= '").append(finalizationsBA.getPartNumber()).append("' ");

				dbServiceRepository.updateResultUsingQuery(updateQuery.toString(), con);

				String warehouseNumber = "";
				if(finalizationsBA.getLocation()!=null && finalizationsBA.getLocation().trim().length() == 6) {
					warehouseNumber = finalizationsBA.getLocation().substring(4,6);
				}


				updateETSTAMMAfterBA67Call(dataLibrary,sumNewStock,warehouseNumber, activationDate, finalizationsBA  );
				
			}else{
				log.info("BA 67 failed with this part - {} ",finalizationsBA.getPartNumber() );
			}
		}
	}
	
	
	private void updateETSTAMMAfterBA67Call(String dataLibrary, BigDecimal sumNewStock, String warehouseNumber,
			String activationDate, FinalizationsBA_DTO finalizationsBA) {
		
		try(Connection con = dbServiceRepository.getConnectionObject();) {
		
		StringBuilder update_ETSTAMM = new  StringBuilder("Update ").append(dataLibrary).append(".E_ETSTAMM ");
		update_ETSTAMM.append(" set DTINV=").append(activationDate).append(" , IVZME = ").append(String.valueOf(sumNewStock)); 
		update_ETSTAMM.append(" where HERST = '").append(finalizationsBA.getManufacturer());
		update_ETSTAMM.append("' and LNR = ").append(warehouseNumber);
		update_ETSTAMM.append(" and TNR ='").append(finalizationsBA.getPartNumber() + "' ");
		
		dbServiceRepository.updateResultUsingQuery(update_ETSTAMM.toString(), con);
		
		}catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Mark Differential List As Fully Processed"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Mark Differential List As Fully Processed"), exception);
			throw exception;
		}
		
	}


	private void createListWithRejectedItems(Connection con, String schema, String inventoryListId,
			String loggedInUser) throws Exception {
		log.info("Inside createListWithRejectedItems method of InventoryServiceImpl");

		StringBuilder query_for_invList = new StringBuilder("INSERT INTO ").append(schema).append(".O_INVLIST");
		query_for_invList.append("(NAME, INVLISTSTS_ID,CREATED_TS, CREATED_BY, WAREHOUSE_ID, WAREHOUSE_NAME, ORIGIN_ID, SORT_BY) ");
		query_for_invList.append("VALUES( ");
		query_for_invList.append("'Abgewiesen' || ").append(" (select name from ").append(schema).append(".O_INVLIST where INVLIST_ID =").append(inventoryListId).append("), ");
		query_for_invList.append("0").append(",");
		query_for_invList.append("now() ").append(",");
		query_for_invList.append("'").append(loggedInUser).append("',");
		query_for_invList.append(" (select WAREHOUSE_ID from ").append(schema).append(".O_INVLIST where INVLIST_ID=").append(inventoryListId).append("),");
		query_for_invList.append("(select WAREHOUSE_NAME from ").append(schema).append(".O_INVLIST where INVLIST_ID=").append(inventoryListId).append("),");
		query_for_invList.append("0").append(",");
		query_for_invList.append("(select SORT_BY from ").append(schema).append(".O_INVLIST where INVLIST_ID=").append(inventoryListId).append(" ))");

		int newInvListId = dbServiceRepository.insertResultUsingQuery(query_for_invList.toString(),con);

		if (newInvListId == 0) {
			AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.CREATE_FAILED_MSG_KEY, "Create list with rejected items "));
			log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY, "Create list with rejected items"), exception);
			throw exception;
		}else{
			insertValueIn_InvType(String.valueOf(newInvListId), inventoryListId, "4",loggedInUser, con, schema);
			
			StringBuilder query_for_procedure  = new StringBuilder("BEGIN DECLARE lid BIGINT; DECLARE olid BIGINT; ");
			query_for_procedure.append(" FOR v CURSOR FOR ");
			query_for_procedure.append(" Select oem as xOEM, tnr AS xTNR, partname AS xPARTNAME, place AS xPLACE, COUNT_OF_PLACES as xPLACES, SA as xSA, TA as xTA, TMARKE AS xTMARKE  , TNRS AS xTNRS, TNRD AS xTNRD ");
			query_for_procedure.append(" FROM ").append(schema).append(".O_INVPART t where INVPARTSTS_ID=3 AND INVLIST_ID=").append(inventoryListId);
			query_for_procedure.append(" DO ");
			query_for_procedure.append(" INSERT INTO ").append(schema).append(".O_INVPART (INVPARTSTS_ID, INVLIST_ID, OEM, TNR, PARTNAME, PLACE, COUNT_OF_PLACES, SA, TA, TMARKE ,TNRS,TNRD )  ");
			query_for_procedure.append("VALUES (0, ").append(newInvListId).append(", xOEM, xTNR, xPARTNAME, xPLACE, xPLACES, xSA, xTA, xTMARKE ,xTNRS,xTNRD); ");
			query_for_procedure.append(" END FOR; END ");

			dbServiceRepository.excuteProcedure(query_for_procedure.toString(),con);
		}
	}
	
	
	/**
	 * This method is used to Copy INVLIST/INVPART into alphaplus history table based on provided inventory List id.
	 */
	@Override
	public Map<String, Boolean> copyInventoryDtlsIntoAlphaplusHistory(String dataLibrary, String savLibrary, String schema,
			String invListId, String warehouseNo) {

		log.info("Inside copyInventoryDtlsIntoAlphaplusHistory method of InventoryServiceImpl");

		Map<String, Boolean> copyInventoryDtlsOutput = new HashMap<String, Boolean>();
		copyInventoryDtlsOutput.put("isCopiedInventoryDtls", false);
		boolean isTableExist = false;

		try(Connection con = dbServiceRepository.getConnectionObject();){
			warehouseNo = StringUtils.leftPad(warehouseNo, 2, "0");
			Integer year = Calendar.getInstance().get(Calendar.YEAR);
			String yearSuffix =  String.valueOf(year).substring(2, 4);

			isTableExist = dbServiceRepository.isTableExist(savLibrary, "E_I000" + warehouseNo +yearSuffix);

			if(!isTableExist){
				createAlphaplusHistoryTable(savLibrary, "E_I000" + warehouseNo+yearSuffix );
			}

				//copyINVLIST/INVPART into alphaplus history
				
				StringBuilder query = new StringBuilder("BEGIN DECLARE xWAREHOUSE INTEGER; DECLARE xACTDATE TIMESTAMP; ");
				query.append(" set xWAREHOUSE = (select WAREHOUSE_ID from ").append(schema).append(".o_invlist where invList_Id=").append(invListId).append("); ");
				query.append(" set xACTDATE = (select ACTIVATION_DATE from ").append(schema).append(".o_invlist where invList_Id=").append(invListId).append("); ");
				query.append(" FOR v CURSOR FOR (Select id as xID, oem as xOEM, tnr AS xTNR, cnt as xCNT, partname AS xPARTNAME, ");
				query.append(" DAK as xDAK, place AS xPLACE, TA AS xTA, TMARKE AS xMARKE, COUNTING_DATE as xCNTDATE, ");
				query.append(" TNRS AS xTNRS, OLD_STOCK as xOLD ,NEW_STOCK as xNEW  from ");
				query.append(schema).append(".O_INVPART where INVPARTSTS_ID = 4 AND INVLIST_ID =  ").append(invListId).append(" ) order by TNR, COUNTING_DATE ");
				query.append(" DO ");
				query.append(" INSERT INTO  ").append(savLibrary).append(".E_I000").append(warehouseNo).append(yearSuffix);
				query.append(" (I_BA,I_INVDAT,I_VFNR,I_LFDNR,I_LNR,I_HERST,I_ETNR,I_BESTAND,I_TIME,I_ZBETRAG,I_BEN, ");
				query.append(" I_KZHINZU,I_VPR,I_DAK,I_MARKE,I_RABGR,I_LOPA,I_DATV,I_DATB,I_ERFASST,I_ALTBEST,I_TA,");
				query.append(" I_ERFBEST,I_ERFDAT,I_ERFTIME,I_MILLSEC,I_ETNRS,FILL01,FILL03,FILL06  ) VALUES ( 67, ");
				query.append(" VARCHAR_FORMAT(xACTDATE, 'YYYYMMDD'), xWAREHOUSE, xCNT, xWAREHOUSE, xOEM, xTNR, ");
				query.append(" CASE (select count(*) from ").append(savLibrary).append(".E_I000").append(warehouseNo).append(yearSuffix);
				query.append(" where I_LNR=xWAREHOUSE AND  I_INVDAT=VARCHAR_FORMAT(xACTDATE, 'YYYYMMDD') ");
				query.append(" AND  I_TIME=VARCHAR_FORMAT(xACTDATE, 'HH24MI') AND I_ETNR=xTNR)  WHEN 0 THEN	xNEW ");
				query.append(" ELSE (select sum(NEW_STOCK) from  ").append(schema).append(".O_INVPART ");
				query.append(" where OEM=xOEM AND TNR=xTNR AND INVLIST_ID= ").append(invListId).append(" AND COUNTING_DATE <= xCNTDATE) END ,");
				query.append(" VARCHAR_FORMAT(xACTDATE, 'HH24MI'),  '0000000000000',  SUBSTR(xPARTNAME, 1, 15) ,");
				query.append(" CASE (select count(*) from ").append(savLibrary).append(".E_I000").append(warehouseNo).append(yearSuffix);
				query.append(" where  I_LNR=xWAREHOUSE AND I_INVDAT=VARCHAR_FORMAT(xACTDATE, 'YYYYMMDD') AND ");
				query.append(" I_TIME=VARCHAR_FORMAT(xACTDATE, 'HH24MI') AND  I_ETNR=xTNR)  WHEN 0 THEN ''  ELSE '1' END, ");
				
				query.append(" (SELECT EPR FROM  ").append(dataLibrary).append(".e_ETSTAMM where HERST=xOEM AND TNR=xTNR AND LNR=xWAREHOUSE), ");
				query.append(" xDAK, xMARKE,  ");
				query.append(" (SELECT RG FROM   ").append(dataLibrary).append(".e_ETSTAMM where HERST=xOEM AND TNR=xTNR AND LNR=xWAREHOUSE), xPLACE, ");
				query.append(" (select VARCHAR_FORMAT(CREATED_TS, 'YYYYMMDD') from  ").append(schema).append(".o_invlist");
				query.append(" where invList_Id= ").append(invListId).append(" ), '00000000',  'E', "); 
				query.append("CASE (select count(*) from ").append(savLibrary).append(".E_I000").append(warehouseNo).append(yearSuffix);
				query.append(" where  I_LNR=xWAREHOUSE AND I_INVDAT=VARCHAR_FORMAT(xACTDATE, 'YYYYMMDD') AND ");
				query.append(" I_TIME=VARCHAR_FORMAT(xACTDATE, 'HH24MI') AND  I_ETNR=xTNR)  WHEN 0 THEN xOLD  ELSE 0  END, ");
				query.append(" xTA,  lpad( cast(xNEW * 10 as INT),6,'0'),  VARCHAR_FORMAT(xCNTDATE, 'YYMMDD'),  ");
				query.append(" VARCHAR_FORMAT(xCNTDATE, 'HH24MIss'), RPAD(EXTRACT(MICROSECONDS FROM xCNTDATE),6,'0'), SUBSTR(xTNRS, 1, 19), '  ', '  ',  ");
				query.append(" '                                                                           '); END FOR; END ");
				
				
				boolean executionFlag = dbServiceRepository.excuteProcedure(query.toString(), con);
				
				if(executionFlag) {
					copyInventoryDtlsOutput.put("isCopiedInventoryDtls", true);
				}
			

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Copy Inventory Dtls Into Alphaplus History"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Copy Inventory Dtls Into Alphaplus History"), exception);
			throw exception;
		}

		return copyInventoryDtlsOutput;
	}


	private void createAlphaplusHistoryTable(String savLibrary, String tableName) {

		log.info("Inside createAlphaplusHistoryTable method of InventoryServiceImpl");

		StringBuilder query = new StringBuilder("CREATE TABLE ").append(savLibrary).append(".").append(tableName);
		query.append("( I_BA NUMERIC(2, 0) NOT NULL DEFAULT 0 ,"); 
		query.append("I_INVDAT NUMERIC(8, 0) NOT NULL DEFAULT 0 ,"); 
		query.append("I_VFNR NUMERIC(5, 0) NOT NULL DEFAULT 0 ,"); 
		query.append("FILL01 CHAR(2) CCSID 273 NOT NULL DEFAULT '' ,"); 
		query.append("I_LFDNR NUMERIC(6, 0) NOT NULL DEFAULT 0 ,"); 
		query.append("I_LNR NUMERIC(2, 0) NOT NULL DEFAULT 0 ,"); 
		query.append("I_HERST CHAR(4) CCSID 273 NOT NULL DEFAULT '' ,"); 
		query.append("I_ETNR CHAR(19) CCSID 273 NOT NULL DEFAULT '' ,"); 
		query.append("FILL03 CHAR(2) CCSID 273 NOT NULL DEFAULT '' ,"); 
		query.append("I_BESTAND NUMERIC(6, 1) NOT NULL DEFAULT 0 ,"); 
		query.append("I_TIME CHAR(4) CCSID 273 NOT NULL DEFAULT '' ,"); 
		query.append("I_ZBETRAG CHAR(13) CCSID 273 NOT NULL DEFAULT '' ,"); 
		query.append("I_BEN CHAR(15) CCSID 273 NOT NULL DEFAULT '' ,"); 
		query.append("I_KZHINZU CHAR(1) CCSID 273 NOT NULL DEFAULT '' ,"); 
		query.append("I_VPR DECIMAL(7, 2) NOT NULL DEFAULT 0 ,"); 
		query.append("I_DAK DECIMAL(9, 4) NOT NULL DEFAULT 0 , ");
		query.append("I_MARKE CHAR(2) CCSID 273 NOT NULL DEFAULT '' ,"); 
		query.append("I_RABGR CHAR(5) CCSID 273 NOT NULL DEFAULT '' , ");
		query.append("I_LOPA CHAR(8) CCSID 273 NOT NULL DEFAULT '' , ");
		query.append("I_DATV CHAR(8) CCSID 273 NOT NULL DEFAULT '' , ");
		query.append("I_DATB CHAR(8) CCSID 273 NOT NULL DEFAULT '' , ");
		query.append("I_ERFASST CHAR(1) CCSID 273 NOT NULL DEFAULT '' , ");
		query.append("I_ALTBEST NUMERIC(6, 1) NOT NULL DEFAULT 0 , ");
		query.append("I_TA NUMERIC(2, 0) NOT NULL DEFAULT 0 , ");
		query.append("I_ERFBEST CHAR(6) CCSID 273 NOT NULL DEFAULT '' ,"); 
		query.append("I_ERFDAT CHAR(6) CCSID 273 NOT NULL DEFAULT '' , ");
		query.append("I_ERFTIME CHAR(6) CCSID 273 NOT NULL DEFAULT '' , ");
		query.append("I_MILLSEC CHAR(6) CCSID 273 NOT NULL DEFAULT '' , ");
		query.append("I_ETNRS CHAR(19) CCSID 273 NOT NULL DEFAULT '' , ");
		query.append("FILL06 CHAR(75) CCSID 273 NOT NULL DEFAULT '' )   ");
		query.append("RCDFMT RCPSINV "); 

		StringBuilder queryForLabel = new StringBuilder("LABEL ON TABLE ").append(savLibrary).append(".").append(tableName).append(" IS 'Inventur-Zählliste 5.2'");

		StringBuilder queryForPermission = new StringBuilder(" GRANT ALTER , DELETE , INDEX , INSERT , REFERENCES , SELECT , UPDATE ON ").append(savLibrary).append(".");
		queryForPermission.append(tableName).append(" TO ").append(savLibrary.substring(0,4)).append(" WITH GRANT OPTION ");

		dbServiceRepository.createTableUsingQuery(query.toString());
		dbServiceRepository.createTableUsingQuery(queryForLabel.toString());
		dbServiceRepository.createTableUsingQuery(queryForPermission.toString());
	}


	/*
	 * This method is used the Update the inventory Status Id from 2 to 3 in the O_INVLIST table based on provided inventory List id.
	 */
	public Map<String, Boolean> updateInventoryStatus(String schema, String inventoryListId) {

		log.info("Inside updateInventoryStatus method of InventoryServiceImpl");

		Map<String, Boolean> outputFlag = new HashMap<String, Boolean>();
		outputFlag.put("isUpdated", false);

		try {

			StringBuilder selectQuery = new StringBuilder("Select INVLISTSTS_ID from ").append(schema).append(".O_INVLIST  where INVLIST_ID = ").append(inventoryListId); 
			selectQuery.append(" and (INVLISTSTS_ID = 2 OR INVLISTSTS_ID = 3 ) ");

			List<InventoryList> checkStatusList = dbServiceRepository.getResultUsingQuery(InventoryList.class, selectQuery.toString(), true);

			if(checkStatusList !=null && !checkStatusList.isEmpty()) {

				if(checkStatusList.get(0).getInventoryList_Status_Id()!=  null 
						&& String.valueOf(checkStatusList.get(0).getInventoryList_Status_Id()).equalsIgnoreCase("2")){
					outputFlag =  genericUpdateInventoryStatus(schema, inventoryListId, "3");
				}else{
					outputFlag.put("isUpdated", true);
				}
			}
			else {
				log.info("The status of the counting list has been changed in the meantime: {}", inventoryListId);
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(
						LocaleContextHolder.getLocale(), ExceptionMessages.COUNTING_LIST_STATUS_FAILED_MSG_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.COUNTING_LIST_STATUS_FAILED_MSG_KEY));
				throw exception;
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Inventory Status"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Inventory Status"), exception);
			throw exception;
		}

		return outputFlag;
	}


	/**
	 * This method is used to get Sum of Delta Stuck and Delta DAK values for Differential list.
	 */
	@Override
	public InventoryDiferentialListDTO getSumOfDifferntialDelta( String dataLibrary, String schema, String inventoryListId ) {

		log.info("Inside getSumOfDifferntialDelta method of InventoryServiceImpl");
		InventoryDiferentialListDTO diffListDTO = new InventoryDiferentialListDTO();

		try {	
			StringBuilder differntialListQuery = new StringBuilder("Select distinct TNR, OEM, PARTNAME, OLD_STOCK, (Select sum(NEW_STOCK) from ");
			differntialListQuery.append(schema).append(".O_INVPART where INVLIST_ID = ").append(inventoryListId).append(" and (INVPARTSTS_ID = 2 OR INVPARTSTS_ID = 3) ");
			differntialListQuery.append(" and tnr = t.tnr and oem = t.oem ").append(") as SUM_NEW_STOCK, ");
			differntialListQuery.append("DAK, DELTA_PIECES, DELTA_CUR, ");
			
			differntialListQuery.append("case when t.INVPARTSTS_ID=3 ").append(" then  '1' ");
			differntialListQuery.append(" else ");
			differntialListQuery.append("case when t.Old_stock != ( select sum(NEW_STOCK) from ").append(schema).append(".O_INVPART where INVLIST_ID =");
			differntialListQuery.append(inventoryListId).append(" and tnr = t.tnr  and oem = t.oem ");
			differntialListQuery.append(" )  then  '0'  else  '1'  end  end as Check_it, INVPARTSTS_ID, INVLIST_ID from ");
			differntialListQuery.append(schema).append(".O_INVPART t where INVLIST_ID = ").append(inventoryListId).append(" and INVPARTSTS_ID > 0 ");

			List<InventoryParts> inventoryDiffParts = dbServiceRepository.getResultUsingQuery(InventoryParts.class, differntialListQuery.toString(), true);

			if (inventoryDiffParts != null && !inventoryDiffParts.isEmpty()) {
				
				BigDecimal deltaPiece = BigDecimal.ZERO;
				BigDecimal deltaCur = BigDecimal.ZERO;
				boolean isDeltaPiece = false;
				boolean isDeltaCur = false;
				for(InventoryParts inventoryParts:inventoryDiffParts ) {
					
					if(inventoryParts.getDeltaPieces()!=null) {
						isDeltaPiece = true;
						deltaPiece = deltaPiece.add(inventoryParts.getDeltaPieces());
					}
					if(inventoryParts.getDeltaCur()!=null) {
						isDeltaCur = true;
						deltaCur = deltaCur.add(inventoryParts.getDeltaCur());
					}
					
				}
				diffListDTO.setDeltaPieces(deltaPiece !=null && isDeltaPiece ? 
						String.valueOf(decimalformat_twodigit.format(deltaPiece)) : "");
				diffListDTO.setDeltaCur(deltaCur != null && isDeltaCur ? 
						String.valueOf(decimalformat_twodigit.format(deltaCur)) : "");
			}
			else{
				log.info(" There is no values found for Sum of Delta Stuck and Delta DAK values in Differential list");
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(
					LocaleContextHolder.getLocale(), ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Sum of Delta DAK"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Sum of Delta DAK"), exception);
			throw exception;
		}

		return diffListDTO;
	}
	
	
	/**
	 * This method is used the check inventory Status in the O_INVLIST table based on provided inventory List id.
	 */
	public boolean genericInventoryStatusCheck(String schema, String inventoryListId, String statusIds) {

		log.info("Inside genericInventoryStatusCheck method of InventoryServiceImpl");
		String statusIdInDB = "";
		boolean updateStatus = false;
		try {
			List<String> invStatusList = Stream.of(statusIds.split(","))
					.collect(Collectors.toList());

			StringBuilder selectQuery = new StringBuilder("select INVLISTSTS_ID from ").append(schema)
					.append(".O_INVLIST Where INVLIST_ID=").append(inventoryListId);

			List<InventoryList> inventoryStatusList = dbServiceRepository.getResultUsingQuery(InventoryList.class,
					selectQuery.toString(), true);

			if (inventoryStatusList != null && !inventoryStatusList.isEmpty()) {
				for (InventoryList invList : inventoryStatusList) {
					statusIdInDB = String.valueOf(invList.getInventoryList_Status_Id());
				}

				if (invStatusList.contains(statusIdInDB)) {
					updateStatus = true;
				} else {
					log.info("The status of the counting list has been changed in the meantime: {}", inventoryListId);
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(
							LocaleContextHolder.getLocale(), ExceptionMessages.COUNTING_LIST_STATUS_FAILED_MSG_KEY));
					log.error(messageService.getReadableMessage(ExceptionMessages.COUNTING_LIST_STATUS_FAILED_MSG_KEY));
					throw exception;
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(
					LocaleContextHolder.getLocale(), ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Inventory Status"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Inventory Status"),
					exception);
			throw exception;
		}

		return updateStatus;
	}


	/**
	 * This method is used to add Single part in existing counting list (Teile hinzufügen)  into DB
	 * @return Object
	 */
	@Override
	public Map<String, Boolean> addSinglePartInExistingCountingList(CreateNewCountingListDTO countingList_DTO,
			String schema, String dataLibrary,String loginUser, String invListId) {

		log.info("Inside addSinglePartInExistingCountingList method of InventoryServiceImpl");

		Map<String, Boolean> procedureOutput = new HashMap<String, Boolean>();
		procedureOutput.put("isUpdated", false);

		try(Connection con = dbServiceRepository.getConnectionObject();) {

			con.setAutoCommit(false);

			if(countingList_DTO.getPartNumber()==null || countingList_DTO.getPartNumber().trim().isEmpty()){
				log.info("PartNumber should not be null or empty");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.INVALID_MSG_KEY, "Part Number"));
				log.error(messageService.getReadableMessage(ExceptionMessages.INVALID_MSG_KEY, "Part Number"));
				throw exception;
			}
			
			List<String> listValues = Arrays.asList(StringUtils.split(countingList_DTO.getWarehouse(), "-"));
			
			//Check if the part is already existing in the Same Counting List
			StringBuilder checkQuery = new StringBuilder(" select count(*) as COUNT from ").append(schema);
			checkQuery.append(".O_INVPART where INVLIST_ID IN( SELECT INVLIST_ID FROM ").append(schema).append(".O_INVLIST WHERE ");
			checkQuery.append(" INVLISTSTS_ID BETWEEN 0 AND 5 AND WAREHOUSE_ID = ").append(listValues.get(0));
			checkQuery.append(" ) AND TNR = '").append(countingList_DTO.getPartNumber()).append("'");
			
			String count = dbServiceRepository.getCountUsingQuery(checkQuery.toString());

			if(Integer.parseInt(count)>0){
				log.info("The part already exists on another counting list : "+countingList_DTO.getListName());
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.PART_ALREADY_EXISTS_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.PART_ALREADY_EXISTS_KEY));
				throw exception;
			}

			//Checking status of the counting list if changed in the meantime then page will reload.
			genericInventoryStatusCheck(schema,invListId,"0"); // 0 = inventory list status Id

			int listId = Integer.parseInt(invListId);
			
				StringBuilder whereClause  = createWhereClause(countingList_DTO.getPartNumber(), countingList_DTO.getManufacturer(), 
						schema, dataLibrary, listId , listValues.get(0) );

				
				
				StringBuilder query_for_procedure  = new StringBuilder("BEGIN DECLARE lid BIGINT; ");
				query_for_procedure.append(" FOR v CURSOR FOR ");
				query_for_procedure.append(" Select herst as xOEM, tnr AS xTNR, benen AS xPARTNAME, lopa AS xPLACE, lnr as xLNR, sa as xSA, ta AS xTA, TMARKE AS xTMARKE, TNRS as xTNRS, TNRD as xTNRD, ");
				query_for_procedure.append(" CASE WHEN (select count(*) from ").append(dataLibrary).append(".e_lago where tnr=t.tnr and lnr=t.lnr and prio > 1 ) > 0  ");
				query_for_procedure.append(" THEN (select count(*) from ").append(dataLibrary).append(".e_lago where tnr=t.tnr and lnr=t.lnr and prio > 1 )+1  ELSE '1' ");
				query_for_procedure.append(" end as xCOUNT_OF_PLACES from ").append(dataLibrary);
				query_for_procedure.append(".e_etstamm t where ").append(whereClause);
				query_for_procedure.append(" DO ");
				query_for_procedure.append(" FOR w CURSOR FOR ");
				query_for_procedure.append(" select lopa as yPLACE from ").append(dataLibrary).append(".e_ETSTAMM where tnr=xTNR and lnr=xLNR  UNION ");
				query_for_procedure.append(" select lopa as yPLACE from ").append(dataLibrary).append(".e_lago where tnr=xTNR and lnr=xLNR AND PRIO>1 ");
				query_for_procedure.append(" DO ");
				query_for_procedure.append(" INSERT INTO  ").append(schema);
				query_for_procedure.append(".O_INVPART (INVPARTSTS_ID, INVLIST_ID, OEM, TNR, PARTNAME, PLACE, COUNT_OF_PLACES, SA, TA, TMARKE, TNRS,TNRD) ");
				query_for_procedure.append(" VALUES (0, ").append(invListId).append(", xOEM, xTNR, xPARTNAME, yPLACE, xCOUNT_OF_PLACES, xSA, xTA, xTMARKE,xTNRS, xTNRD ); ");
				query_for_procedure.append("END FOR; END FOR; END ");

			boolean executionFlag = dbServiceRepository.excuteProcedure(query_for_procedure.toString(),con);

			if(executionFlag){
				// insert value in O_INVCRITERIA

				String query_for_invCriteria = createQueryForInventoryCriteria(countingList_DTO,schema,dataLibrary, loginUser, listId);
				int invCriteriaId = dbServiceRepository.insertResultUsingQuery(query_for_invCriteria.toString(),con);

				if (invCriteriaId == 0) {
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.CREATE_FAILED_MSG_KEY, "Inventory Criteria"));
					log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY, "Inventory Criteria"), exception);
					throw exception;
				}
			}

			procedureOutput.put("isUpdated", true);
			con.commit();
			con.setAutoCommit(true);
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Add Parts In Existing Counting List"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Add Parts In Existing Counting List"), exception);
			throw exception;
		}

		return procedureOutput;
	}

	
	
	private StringBuilder createWhereClause(String partNo, String manufacturer, String schema, String dataLibrary, 
			int invListId, String warehouseId) {
			
		manufacturer = manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING: manufacturer;
		StringBuilder query = new StringBuilder(" HERST= '").append(manufacturer);
		query.append("' AND LNR= (select distinct warehouse_id from ").append(schema).append(".O_INVLIST where INVLIST_ID=").append(invListId);
		query.append(") AND NOT TNR IN (select distinct TNR from ").append(schema).append(".O_INVPART WHERE INVLIST_ID IN(SELECT INVLIST_ID FROM ");
		query.append(schema).append(".O_INVLIST WHERE INVLISTSTS_ID BETWEEN 0 AND 5 AND WAREHOUSE_ID =").append(warehouseId).append(")) AND ");
		query.append("TNR= '").append(partNo).append("'");
		
		return query;
	}


	/**
	 * This method is used to get the parts with negative stock amount are existing or not.
	 */
	@Override
	public Map<String, Boolean> checkPartsWithNegativeStock(String dataLibrary, String schema, String warehouseId) {

		log.info("Inside checkPartsWithNegativeStock method of InventoryServiceImpl");
		Map<String,Boolean> outputMsg = new HashMap<String, Boolean>();
		outputMsg.put("isNegativeStockAvailable", false);
		try {
			StringBuilder query = new StringBuilder("Select COUNT(*) as count  from ").append(dataLibrary).append(".E_ETSTAMM WHERE "); 
			query.append("LNR = ").append(warehouseId).append(" AND  aktbes < 0 ");

			String count = dbServiceRepository.getCountUsingQuery(query.toString());

			//if the list is not empty
			if (count != null && !count.isEmpty() && Integer.parseInt(count) > 0) {
				outputMsg.put("isNegativeStockAvailable", true);
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Parts with negativ stock amount"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Parts with negativ stock amount"), exception);
			throw exception;
		}

		return outputMsg;
	}
	
	
	private String getOrderByClause(String sortingType, String sortingBy) {

		StringBuilder orderByClause = new StringBuilder();

		if (sortingBy != null && sortingBy.equalsIgnoreCase("Zähllistenname")) {
			orderByClause.append(" ORDER BY NAME ");
		}else if(sortingBy != null && sortingBy.equalsIgnoreCase("Datum")){
			orderByClause.append(" ORDER BY CREATED_TS ");
		}else if(sortingBy != null && sortingBy.equalsIgnoreCase("Status")){
			orderByClause.append(" ORDER BY INVLISTSTS_ID ");
		}else{
			orderByClause.append(" ORDER BY CREATED_TS  ");
		}

		if(sortingType!= null && sortingType.equalsIgnoreCase("ASC")){
			orderByClause.append("ASC");
		}else{
			orderByClause.append("DESC");
		}
		
		return orderByClause.toString();
	}


	/**
	 * This is method used to get Inventory list status.
	 */
	@Override
	public List<DropdownObject> getInventoryListStatus(String dataLibrary,String schema) {
		log.info("Inside getInventoryListStatus method of InventoryServiceImpl");

		List<DropdownObject> invStatusList = new ArrayList<>();

		try{
			StringBuilder queryForInvStatus = new StringBuilder("SELECT INVLISTSTS_ID as KEYFLD, STATUS_DESC as DATAFLD FROM ");
			queryForInvStatus.append(schema).append(".O_INVST ");

			Map<String, String> invStatusKeyValue = dbServiceRepository.getResultUsingCobolQuery(queryForInvStatus.toString());

			if(invStatusKeyValue != null && !invStatusKeyValue.isEmpty()) {
				for(Map.Entry<String, String> wrhDetails : invStatusKeyValue.entrySet()) {

					DropdownObject dropdownObject = new DropdownObject();
					dropdownObject.setKey(wrhDetails.getKey());
					dropdownObject.setValue(wrhDetails.getValue());

					invStatusList.add(dropdownObject);
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Inventory list status"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Inventory list status"), exception);
			throw exception;
		}
		
		return invStatusList;
	}

	
	
	/**
	 * This is method used to generate counting list name
	 */
	@Override
	public String generateCountingListName(String dataLibrary,String schema) {
		log.info("Inside generateCountingListName method of InventoryServiceImpl");

		String countingListName = "";
		
		try{

			StringBuilder queryForInvStatus = new StringBuilder("SELECT CASE WHEN  MAX(invlist_id) > 0 THEN MAX(invlist_id) +1 ELSE '1' END ");
			queryForInvStatus.append(" as KEYFLD , SECOND (CURRENT TIMESTAMP) as DATAFLD FROM ");
			queryForInvStatus.append(schema).append(".O_INVLIST ");

			Map<String, String> invListKeyValue = dbServiceRepository.getResultUsingCobolQuery(queryForInvStatus.toString());

			if(invListKeyValue != null && !invListKeyValue.isEmpty()) {
				for(Map.Entry<String, String> listDetails : invListKeyValue.entrySet()) {
					countingListName = listDetails.getKey()+listDetails.getValue();
				}
			}
			
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Counting List Name"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Counting List Name"), exception);
			throw exception;
		}
		return countingListName;
	}
	
	/**
	 * This is method used to check for partly counted storage locations for a part
	 */
	@Override
	public Boolean checkPartlyCountedStorageLocationOfPart(String dataLibrary,String schema, String inventoryListId) {
		log.info("Inside checkPartlyCountedStorageLocationOfPart method of InventoryServiceImpl");

		boolean isStorageLocationPartlyCounted = false;
		
		try{

			StringBuilder query = new StringBuilder("select count(*) as count from ");
			query.append(schema).append(".O_INVPART where invlist_id = ").append(inventoryListId).append(" and count_of_places > 1 and new_stock is null ");

			String count = dbServiceRepository.getCountUsingQuery(query.toString());

			if(Integer.parseInt(count) > 0) {
				isStorageLocationPartlyCounted = true;
			}
			
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Partly Counted Storage Location for Part"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Partly Counted Storage Location for Part"), exception);
			throw exception;
		}
		return isStorageLocationPartlyCounted;
	}
	
	
	private void insertValueIn_InvType(String inventoryListId, String parentInvListId, String listType,
			String loggedInUser, Connection con, String schema) throws Exception {

		log.info("Inside insertValueIn_InvType method of InventoryServiceImpl");

		StringBuilder query_for_Insert = new StringBuilder("Insert into ");
		query_for_Insert.append(schema).append(".O_INVTYPE (INVLIST_ID, BASED_ON, LISTTYPE_ID, CREATED_BY, CREATED_TS)  VALUES ( ");
		query_for_Insert.append(inventoryListId).append(", ");
		query_for_Insert.append(parentInvListId).append(", ");
		query_for_Insert.append(listType).append(", '");
		query_for_Insert.append(loggedInUser).append("', CURRENT TIMESTAMP ) ");

		dbServiceRepository.insertResultUsingQuery(query_for_Insert.toString(), con);

	}
	
	/**
	 * This is method used to get Inventory list Type.
	 */
	@Override
	public List<DropdownObject> getInventoryListType(String dataLibrary,String schema) {
		log.info("Inside getInventoryListType method of InventoryServiceImpl");

		List<DropdownObject> invListTypeList = new ArrayList<>();
		
		try{
			StringBuilder queryForInvStatus = new StringBuilder("SELECT LISTTYPE_ID as KEYFLD, LISTTYPE as DATAFLD FROM ");
			queryForInvStatus.append(schema).append(".O_LISTTYPE ");

			Map<String, String> invListTypeKeyValue = dbServiceRepository.getResultUsingCobolQuery(queryForInvStatus.toString());

			if(invListTypeKeyValue != null && !invListTypeKeyValue.isEmpty()) {
				for(Map.Entry<String, String> listType : invListTypeKeyValue.entrySet()) {

					DropdownObject dropdownObject = new DropdownObject();
					dropdownObject.setKey(listType.getKey());
					dropdownObject.setValue(listType.getValue());

					invListTypeList.add(dropdownObject);
				}
			}
			
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Inventory list type"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Inventory list type"), exception);
			throw exception;
		}
		
		return invListTypeList;
	}
	

	/**
	 * This method is used to get warehouse list of created counting list from O_INVLIST table.
	 */
	@Override
	public List<CreatedCountingListDTO> getWarehouseListForCreatedCountingList(String dataLibrary, String schema, String allowedWarehouses) {

		log.info("Inside getWarehouseListForCreatedCountingList method of InventoryServiceImpl");

		List<CreatedCountingListDTO> createdCountingListDTOs = new ArrayList<CreatedCountingListDTO>();

		try {
			
			//validate the Warehouse Ids
			validateWarehouses(allowedWarehouses);

			StringBuilder createdCountingList = new StringBuilder("SELECT distinct Warehouse_ID, WAREHOUSE_NAME FROM ");
			createdCountingList.append(schema).append(".O_INVLIST where INVLISTSTS_ID = 0 and WAREHOUSE_ID IN(").append(allowedWarehouses).append(") order by Warehouse_ID ");

			List<InventoryList> inventoryList = dbServiceRepository.getResultUsingQuery(InventoryList.class, createdCountingList.toString(), true);

			if (inventoryList != null && !inventoryList.isEmpty()) {
				for(InventoryList inventory : inventoryList){

					CreatedCountingListDTO listDTO = new CreatedCountingListDTO();
					listDTO.setWarehouseId(inventory.getWarehouseId().toString());
					listDTO.setWarehouseName(inventory.getWarehouseName());
					
					createdCountingListDTOs.add(listDTO);
				}
			}} catch (AlphaXException ex) {
				throw ex;
			} catch (Exception e) {
				log.info("Error Message:" + e.getMessage());
				AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(
						LocaleContextHolder.getLocale(), ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Warehouse list for created counting list"));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Warehouse list for created counting list"),exception);
				throw exception;
			}

			return createdCountingListDTOs;
		}

	private String getTNRSValue(CreateNewCountingListDTO list_DTO,String schema, String dataLibrary, String selectionType,int invListId, String tnrsBy) {
		
		log.info("get TNRS value by : {}",  tnrsBy);
		String tnrsValue = null;
		try {
		String manufacturer = list_DTO.getManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING: list_DTO.getManufacturer();
		StringBuilder query = new StringBuilder(" SELECT TNRS FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE ");
		query.append(" HERST= '").append(manufacturer);
		query.append("' AND LNR= (select distinct warehouse_id from ").append(schema).append(".O_INVLIST where INVLIST_ID=").append(invListId).append(")");
		
			//for default selection
			if(list_DTO.isPartNumberRangeFlag()){
				if(!list_DTO.isAllParts()){
				if(isNullOrEmpty(list_DTO.getFromPartNumber()) && tnrsBy.equalsIgnoreCase("FROM_TNR")){
					query.append(" AND tnr= '").append(list_DTO.getFromPartNumber()).append("'");
				}else if(isNullOrEmpty(list_DTO.getToPartNumber()) && tnrsBy.equalsIgnoreCase("TO_TNR")){
					query.append(" AND tnr= '").append(list_DTO.getToPartNumber()).append("'");
				}else{
					log.info("Both part selection is empty");
				}
			}
		}
		
		if(isNullOrEmpty(list_DTO.getMarketingCode())){
			if(list_DTO.getMarketingCode().contains("*")) {
				query.append(" AND UPPER(MC) LIKE UPPER('").append(list_DTO.getMarketingCode().replace("*", "_")).append("') ");	
			}else {
				query.append(" AND UPPER(MC) LIKE UPPER('").append(list_DTO.getMarketingCode()).append("') ");
			}
			
		}
		if(isNullOrEmpty(list_DTO.getCountingGroup())){
			query.append(" AND ZAEGRU='").append(list_DTO.getCountingGroup()).append("' ");
		}
		if(isNullOrEmpty(list_DTO.getPartBrand())){
			query.append(" AND TMARKE='").append(list_DTO.getPartBrand()).append("' ");
		}
		if(isNullOrEmpty(list_DTO.getInventoryValueRange())){
			query.append(" AND AKTBES < ").append(list_DTO.getInventoryValueRange()).append(" ");
		}
		if(isNullOrEmpty(list_DTO.getStorageIndikator())){
			// 0: SA1 and SA2, 1: SA1 only, 2: SA2 only	
			if(list_DTO.getStorageIndikator().equalsIgnoreCase("0")){
				query.append(" AND ( SA=1 OR (SA=2 AND AKTBES>0)) ");
			}else if(list_DTO.getStorageIndikator().equalsIgnoreCase("1")){
				query.append(" AND SA=1 ");
			}else if(list_DTO.getStorageIndikator().equalsIgnoreCase("2")){
				query.append(" AND (SA=2 AND AKTBES>0 ) ");
			}
		}
		if(isNullOrEmpty(list_DTO.getStorageLocation())){
			// 0 = All ,  1 = Teile mit einem Lgerort 2 = Teile mit Mehrfachlagerort
			if(list_DTO.getStorageLocation().equalsIgnoreCase("1")){ 
				query.append(" AND (select count(lopa) from ").append(dataLibrary).append(".e_lago where tnr=t.tnr and lnr=t.lnr) <= 1 ");
			}else if(list_DTO.getStorageLocation().equalsIgnoreCase("2")){ 
				query.append(" AND (select count(lopa) from ").append(dataLibrary).append(".e_lago where tnr=t.tnr and lnr=t.lnr) > 1 ");
			}
		}
		if(!list_DTO.isAlreadyCountedItems()){
			query.append(" AND IVKZ='' "); //0: Do not add counted prts, 1: Add counted prts	
		}
		if(list_DTO.isPriceRange()){
			if(isNullOrEmpty(list_DTO.getFromPriceRange()) && isNullOrEmpty(list_DTO.getToPriceRange()) ){
				query.append(" AND (EPR BETWEEN ").append(list_DTO.getFromPriceRange()).append(" AND ").append(list_DTO.getToPriceRange()).append(" ) ");
			}
		}
		
		if(isNullOrEmpty(list_DTO.getMaxCountingPosition())){
			query.append("LIMIT ").append(list_DTO.getMaxCountingPosition());
		}
		
		List<InventoryParts> inventoryPartsValues = dbServiceRepository.getResultUsingQuery(InventoryParts.class, query.toString(), true);

		if (inventoryPartsValues != null && !inventoryPartsValues.isEmpty()) {
				tnrsValue = inventoryPartsValues.get(0).getSortingFormatPartNumber();
		}
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "TNRS value"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "TNRS value"), exception);
			throw exception;
		}
		
		return tnrsValue;
		
	}
	
}