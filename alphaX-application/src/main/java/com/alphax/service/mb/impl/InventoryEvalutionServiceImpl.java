package com.alphax.service.mb.impl;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.alphax.common.rest.message.Message;
import com.alphax.common.rest.message.service.MessageService;
import com.alphax.model.mb.InventoryArchivedCounting;
import com.alphax.model.mb.InventoryArchivedDifferences;
import com.alphax.model.mb.InventoryEvalution;
import com.alphax.common.exception.AlphaXException;
import com.alphax.common.exception.ExceptionMessages;
import com.alphax.repository.CobolServiceRepository;
import com.alphax.repository.DBServiceRepository;
import com.alphax.service.mb.InventoryEvalutionService;
import com.alphax.vo.mb.GlobalSearch;
import com.alphax.vo.mb.InventoryArchivedCountingDTO;
import com.alphax.vo.mb.InventoryArchivedDifferencesDTO;
import com.alphax.vo.mb.InventoryEvalutionDTO;
import com.alphax.common.constants.AlphaXCommonUtils;
import com.alphax.common.constants.RestInputConstants;

import lombok.extern.slf4j.Slf4j;

/**
 * @author A106744104
 *
 */

@Service
@Slf4j
public class InventoryEvalutionServiceImpl extends BaseService implements InventoryEvalutionService {

	@Autowired
	DBServiceRepository dbServiceRepository;

	@Autowired
	private MessageService messageService;
	
	@Autowired
	CobolServiceRepository cobolServiceRepository;
	
	@Autowired
	AlphaXCommonUtils commonUtils;
	
	DecimalFormat decimalformat_twodigit = new DecimalFormat("#0.00", new DecimalFormatSymbols(Locale.ENGLISH));	

	/**
	 * This method is used to get the Inventory Conspicuous Items (Bestandsauffällige Positionen) from Savlib.
	 */
	@Override
	public GlobalSearch getInventoryRemarkableItems(String dataLibrary, String savLibrary, String warehouseNo, String fromDate, String toDate, String pageSize, String pageNumber) {

		log.info("Inside getInventoryRemarkableItems method of InventoryEvalutionServiceImpl");

		List<InventoryEvalutionDTO> inventoryItemsDTOList = new ArrayList<InventoryEvalutionDTO>();
		GlobalSearch globalSearchList = new GlobalSearch();		

		try {			
			String formatted_FromDate = commonUtils.getDateInYYYYMMDD(fromDate);
			String formatted_ToDate = commonUtils.getDateInYYYYMMDD(toDate);

			if(pageSize==null || pageNumber==null || pageSize.isEmpty() || pageNumber.isEmpty()){
				pageSize = RestInputConstants.DEFAULT_PAGE_SIZE;
				pageNumber = RestInputConstants.DEFAULT_PAGE_NO;
			}

			int totalRecords = Integer.parseInt(pageSize);
			int nextRows = totalRecords * (Integer.parseInt(pageNumber) - 1);

			//validate the page size
			validatePageSize(totalRecords);
			log.info("PageSize  : {}  OFFSET Size (Skip Rows):  {} Page Number  : {} ", pageSize, nextRows, pageNumber);

			warehouseNo = StringUtils.leftPad(warehouseNo, 2, "0");

			//String year = checkDateWithCurrentYear(formatted_FromDate);
			String year = formatted_FromDate.substring(2,4);

			if(null != year && !year.isEmpty()) {
				String tableName = "E_I000" + warehouseNo + year;

				boolean isTableExist = dbServiceRepository.isTableExist(savLibrary, tableName);
				
				if(isTableExist){
				StringBuilder query = new StringBuilder("Select distinct i_etnr as Teilenummer, i_herst as Hersteller, ");
				query.append("(select BENEN from ").append(dataLibrary).append(".E_ETSTAMM where TNR=tmp.I_ETNR AND LNR=tmp.I_VFNR AND HERST = tmp.I_HERST ) as Bezeichnung, i_vfnr as Lager, ");
				query.append("(select DAK from ").append(dataLibrary).append(".E_ETSTAMM  where TNR=tmp.I_ETNR AND LNR=tmp.I_VFNR AND HERST = tmp.I_HERST ) as aktueller_DAK, ");
				query.append("(select AKTBES from ").append(dataLibrary).append(".E_ETSTAMM  where TNR=tmp.I_ETNR AND LNR=tmp.I_VFNR AND HERST = tmp.I_HERST ) as aktueller_Bestand, ");
				query.append("((select sum(cast(I_erfbest as real))/10 from ").append(savLibrary).append("."+tableName);
				query.append(" where  UCASE(I_ERFBEST)=LCASE(I_ERFBEST) and I_ERFBEST != '' AND I_ERFBEST !=  I_ALTBEST  AND I_ERFASST = 'E' and i_etnr=tmp.i_etnr ) - ");
				query.append(" (select sum(I_altbest) from ").append(savLibrary).append("."+tableName);
				query.append(" WHERE  UCASE(I_ERFBEST)=LCASE(I_ERFBEST) and I_ERFBEST != '' AND I_ERFBEST !=  I_ALTBEST  AND I_ERFASST = 'E' and i_etnr=tmp.i_etnr )) as Differenz_Stuck, ");
				query.append("((select sum(cast(I_erfbest as real))/10 from ").append(savLibrary).append("."+tableName);
				query.append("  WHERE  UCASE(I_ERFBEST)=LCASE(I_ERFBEST) and I_ERFBEST != '' AND I_ERFBEST !=  I_ALTBEST  AND I_ERFASST = 'E' and i_etnr=tmp.i_etnr ) - ");
				query.append(" (select sum(I_altbest) from ").append(savLibrary).append("."+tableName).append(" WHERE  UCASE(I_ERFBEST)=LCASE(I_ERFBEST) and I_ERFBEST != '' AND I_ERFBEST !=  I_ALTBEST  AND I_ERFASST = 'E' and i_etnr=tmp.i_etnr )) * ");
				query.append(" (select DAK from ").append(dataLibrary).append(".E_ETSTAMM where TNR=tmp.I_ETNR AND LNR=tmp.I_VFNR AND HERST = tmp.I_HERST ) as Differenz_EUR, ");
				query.append("(select count(*) from ");
				query.append("(select  distinct i_etnr, i_herst, i_vfnr from ").append(savLibrary).append("."+tableName);
				query.append(" WHERE  UCASE(I_ERFBEST)=LCASE(I_ERFBEST) and I_ERFBEST != '' AND I_ERFBEST !=  I_ALTBEST  AND I_ERFASST = 'E' and (i_INVDAT between ").append(formatted_FromDate).append(" and ");
				query.append(formatted_ToDate).append(") )) AS ROWNUMER from ");
				query.append(savLibrary).append("."+tableName).append(" tmp  where  UCASE(I_ERFBEST)=LCASE(I_ERFBEST) and I_ERFBEST != '' AND I_ERFBEST !=  I_ALTBEST  AND I_ERFASST = 'E' and (i_INVDAT between ");
				query.append(formatted_FromDate).append(" and ").append(formatted_ToDate).append(") ");
				query.append(" order by ((select sum(cast(I_erfbest as real))/10 from ").append(savLibrary).append("."+tableName);
				query.append("  WHERE  UCASE(I_ERFBEST)=LCASE(I_ERFBEST) and I_ERFBEST != '' AND I_ERFBEST !=  I_ALTBEST  AND I_ERFASST = 'E' and i_etnr=tmp.i_etnr ) - ");
				query.append(" (select sum(I_altbest) from ").append(savLibrary).append("."+tableName).append(" WHERE  UCASE(I_ERFBEST)=LCASE(I_ERFBEST) and I_ERFBEST != '' AND I_ERFBEST !=  I_ALTBEST  AND I_ERFASST = 'E' and i_etnr=tmp.i_etnr )) * ");
				query.append(" (select DAK from ").append(dataLibrary).append(".E_ETSTAMM where TNR=tmp.I_ETNR AND LNR=tmp.I_VFNR AND HERST = tmp.I_HERST) OFFSET ");
				query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");

				List<InventoryEvalution> inventoryEvalutionList = dbServiceRepository.getResultUsingQuery(InventoryEvalution.class, query.toString(), true);

				if (inventoryEvalutionList != null && !inventoryEvalutionList.isEmpty()) {

					inventoryItemsDTOList = convertInventoryItemsEntityToDTO(inventoryEvalutionList, inventoryItemsDTOList);
					globalSearchList.setSearchDetailsList(inventoryItemsDTOList);
					globalSearchList.setTotalPages(Integer.toString(inventoryEvalutionList.get(0).getTotalCount()));
					globalSearchList.setTotalRecordCnt(Integer.toString(inventoryEvalutionList.get(0).getTotalCount()));

				} else {
					globalSearchList.setSearchDetailsList(inventoryItemsDTOList);
					globalSearchList.setTotalPages(Integer.toString(0));
					globalSearchList.setTotalRecordCnt(Integer.toString(0));
				}
			}else {
				globalSearchList.setSearchDetailsList(inventoryItemsDTOList);
				globalSearchList.setTotalPages(Integer.toString(0));
				globalSearchList.setTotalRecordCnt(Integer.toString(0));
			}
		}
			

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Inventory Evalution(Bestandsauffällige Positionen)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Inventory Evalution(Bestandsauffällige Positionen)"), exception);
			throw exception;
		}

		return globalSearchList;
	}
	
	
	/*private String checkDateWithCurrentYear(String fromDate) {
		//get current year
		Integer year = Calendar.getInstance().get(Calendar.YEAR);		
		String yearSuffix = "";
		String fromDateYear = fromDate.substring(0, 4);
		
		if(fromDateYear.equals(year.toString())) {
			yearSuffix = fromDateYear.substring(2, 4);
		}
		else {
			log.debug("Current year is not matched with passed date");
			Message message = messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.INVALID_MSG_KEY, "Daten");
			AlphaXException exception = new AlphaXException(message);
			log.error(messageService.getReadableMessage(ExceptionMessages.INVALID_MSG_KEY, "Daten"), exception);
			throw exception;
		}
		
		return yearSuffix;
	}*/
	
	
	/**
	 * This method is used to convert InventoryEvalution Entity to InventoryEvalutionDTO DTO
	 * @param inventoryItemList
	 * @param inventoryItemDTOList
	 * @return
	 */
	private List<InventoryEvalutionDTO> convertInventoryItemsEntityToDTO(List<InventoryEvalution> inventoryItemList,
			List<InventoryEvalutionDTO> inventoryItemDTOList) {

		for(InventoryEvalution inventoryEvlObj : inventoryItemList){

			InventoryEvalutionDTO evalutionDTO = new InventoryEvalutionDTO();
			
			evalutionDTO.setWarehouseNo(inventoryEvlObj.getWarehouseNo());
			evalutionDTO.setPartNumber(inventoryEvlObj.getPartNumber());
			evalutionDTO.setPartName(inventoryEvlObj.getPartName());
			evalutionDTO.setManufacturer(inventoryEvlObj.getManufacturer().equalsIgnoreCase(RestInputConstants.DCAG_STRING)? RestInputConstants.DAG_STRING 
					: inventoryEvlObj.getManufacturer());
			
			evalutionDTO.setAkt_Dak(String.valueOf(decimalformat_twodigit.format(inventoryEvlObj.getAkt_Dak()!=null?inventoryEvlObj.getAkt_Dak():0.0)));	
			evalutionDTO.setCountingAmount(String.valueOf(decimalformat_twodigit.format(inventoryEvlObj.getCountingAmount()!=null?inventoryEvlObj.getCountingAmount():0.0)));
			evalutionDTO.setDiffernceStuck(String.valueOf(decimalformat_twodigit.format(inventoryEvlObj.getDiffernceStuck()!=null?inventoryEvlObj.getDiffernceStuck():0.0)));
			evalutionDTO.setDiffernceEUR(String.valueOf(decimalformat_twodigit.format(inventoryEvlObj.getDiffernceEUR()!=null?inventoryEvlObj.getDiffernceEUR():0.0)));

			inventoryItemDTOList.add(evalutionDTO);
		}

		return inventoryItemDTOList;
	}
	
	
	/**
	 * This method is used to get the Inventory Conspicuous Items Details (Bestandsauffällige Positionen Details) using Partnumber.
	 */
	@Override
	public List<InventoryEvalutionDTO> getInventoryRemarkableItemsDetails(String dataLibrary, String savLibrary, String partNo, String warehouseNo) {

		log.info("Inside getInventoryRemarkableItemsDetails method of InventoryEvalutionServiceImpl");

		List<InventoryEvalutionDTO> inventoryItemsDTOList = new ArrayList<InventoryEvalutionDTO>();

		try {			
			warehouseNo = StringUtils.leftPad(warehouseNo, 2, "0");

			Integer year = Calendar.getInstance().get(Calendar.YEAR) % 100;
			String tableName = "E_I000" + warehouseNo + year;
			
			boolean isTableExist = dbServiceRepository.isTableExist(savLibrary, tableName);
			
			if(isTableExist){
			StringBuilder query = new StringBuilder("Select i_INVDAT ||' ' || I_TIME as Inventur_Name, sum(I_altbest) as Sollbestand, ");
			query.append(" sum(cast(I_erfbest as real))/10 as gezahlt_LOPA, ");
			query.append(" sum(cast(I_erfbest as real))/10 - sum(I_altbest) as Differenz_Stuck, ");
			query.append(" i_dak*(sum(cast(I_erfbest as real))/10 - sum(I_altbest)) as Differenz_EUR, i_dak as DAK  from  ");
			query.append(savLibrary).append("."+tableName).append(" WHERE I_ERFBEST != I_ALTBEST AND I_ERFASST = 'E' and i_etnr= '");
			query.append(partNo).append("' group by  i_INVDAT||' '||I_TIME, i_dak  Order by i_INVDAT||' '||I_TIME ASC ");

			//execute the query and get the resultset
			List<InventoryEvalution> inventoryEvalutionDetList = dbServiceRepository.getResultUsingQuery(InventoryEvalution.class, query.toString(), true);

			if (inventoryEvalutionDetList != null && !inventoryEvalutionDetList.isEmpty()) {

				for(InventoryEvalution inventoryDetailObj : inventoryEvalutionDetList){

					InventoryEvalutionDTO evalutionDTO = new InventoryEvalutionDTO();

					evalutionDTO.setWarehouseNo(warehouseNo);
					evalutionDTO.setPartNumber(partNo);					
					evalutionDTO.setStorageCount(String.valueOf(decimalformat_twodigit.format(inventoryDetailObj.getStorageCount()!= null?inventoryDetailObj.getStorageCount():0.0)));
					evalutionDTO.setTargetStock(String.valueOf(decimalformat_twodigit.format(inventoryDetailObj.getTargetStock()!= null?inventoryDetailObj.getTargetStock():0.0)));

					evalutionDTO.setInventoryName(inventoryDetailObj.getInventoryName());
					evalutionDTO.setDak(String.valueOf(decimalformat_twodigit.format(inventoryDetailObj.getDak()!= null?inventoryDetailObj.getDak():0.0)));
					evalutionDTO.setDiffernceStuck(String.valueOf(decimalformat_twodigit.format(inventoryDetailObj.getDiffernceStuck()!= null?inventoryDetailObj.getDiffernceStuck():0.0)));
					evalutionDTO.setDiffernceEUR(String.valueOf(decimalformat_twodigit.format(inventoryDetailObj.getDiffernceEUR()!= null?inventoryDetailObj.getDiffernceEUR():0.0)));

					inventoryItemsDTOList.add(evalutionDTO);
				}
			}
		}
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Inventory Details(Bestandsauffällige Positionen Details)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Inventory Details(Bestandsauffällige Positionen Details)"), exception);
			throw exception;
		}

		return inventoryItemsDTOList;
	}
	
	
	/**
	 * This method is used to get the Inventory Print - Archived lists of differences (Archivierte Differenzenlisten) from Savlib.
	 */
	@Override
	public List<InventoryArchivedDifferencesDTO> getInventoryArchivedDifferencesList(String dataLibrary, String savLibrary, String warehouseNo, String fromDate, String toDate) {

		log.info("Inside getInventoryArchivedDifferencesList method of InventoryEvalutionServiceImpl");
		boolean isAliasExist = false;
		List<InventoryArchivedDifferencesDTO> inventoryItemsDTOList = new ArrayList<InventoryArchivedDifferencesDTO>();

		try {			
			String formatted_FromDate = commonUtils.getDateInYYMMDD(fromDate);
			String formatted_ToDate = commonUtils.getDateInYYMMDD(toDate);

			warehouseNo = StringUtils.leftPad(warehouseNo, 2, "0");

			//String year = checkDateWithCurrentYear(commonUtils.getDateInYYYYMMDD(fromDate));
			String year = commonUtils.getDateInYYYYMMDD(fromDate).substring(2, 4);

			if(null != year && !year.isEmpty()) {
				String tableName = "AE4000" + warehouseNo + year;
				String aliasName = "MBR" + warehouseNo + year;
				
				//Table check - table exist or not
				StringBuilder fileCheckCommand = new StringBuilder("CHKOBJ OBJ(").append(savLibrary).append("/").append(tableName).append(") OBJTYPE(*FILE)");
				boolean checkFile = cobolServiceRepository.executeCommandForFile(fileCheckCommand.toString());
				
				if(!checkFile){
					
					log.debug("This file is not available :"+tableName);
					Message message = messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.INVENTORY_ARCHIVED_FILE_CHECK_FAILED_MSG_KEY);
					AlphaXException exception = new AlphaXException(message);
					log.error(messageService.getReadableMessage(ExceptionMessages.INVENTORY_ARCHIVED_FILE_CHECK_FAILED_MSG_KEY), exception);
					throw exception;
				}
				
				//Alias name check
				
				isAliasExist = dbServiceRepository.isTableExist(savLibrary, aliasName);

				if (!isAliasExist) {
					StringBuilder creatAlias = new StringBuilder("CREATE ALIAS ").append(savLibrary).append(".").append(aliasName);
					creatAlias.append(" For ").append(savLibrary).append(".").append(tableName).append("(").append(tableName).append(")");
					dbServiceRepository.createTableUsingQuery(creatAlias.toString());
				}
				
				StringBuilder query = new StringBuilder("WITH auszug AS (select distinct cast(").append(tableName);
				query.append(" aS VARCHAR(256) CCSID 273) as Daten, ROW_NUMBER() OVER () AS Zeile FROM ").append(savLibrary).append(".").append(aliasName);
				query.append(" where cast(").append(tableName).append(" aS VARCHAR(256) CCSID 273) like '%D I F F E R E N Z E N L I S T E  (%'");
				query.append("or cast(").append(tableName).append(" aS VARCHAR(256) CCSID 273) like '%Zähl Datum%' or cast(").append(tableName).append(" aS VARCHAR(256) CCSID 273) like '%Inventur%') ");
				query.append("select distinct substr(daten, 46, 39) as Differenzenliste, ");
				query.append("substr(daten, 80, 4) as List_ID, substr(daten, 129, 8) as Datum, ");
				query.append("(select substr(daten, 15, 13) from auszug where zeile=tmp.zeile+1) as Inventurkennung,");
				query.append("(select substr(daten, 28, 10) from auszug where zeile=tmp.zeile+2) as Zahldatum,");
				query.append("(select trim(substr(daten, 68, 7)) from auszug where zeile=tmp.zeile+2) as Lager ");
				query.append("from auszug tmp where daten like '%D I F F E R E N Z E N L I S T E  (%' AND substr(daten, 135 , 2)||substr(daten, 132 , 2)||substr(daten, 129 , 2) between ");
				query.append(formatted_FromDate).append(" AND ").append(formatted_ToDate).append(" order by substr(daten, 46, 39)");
				
				List<InventoryArchivedDifferences> inventoryArchivedDifferencesList = dbServiceRepository.getResultUsingQuery(InventoryArchivedDifferences.class, query.toString(), true);

				
				if (inventoryArchivedDifferencesList != null && !inventoryArchivedDifferencesList.isEmpty()) {
					
					for(InventoryArchivedDifferences archivedDiffList:inventoryArchivedDifferencesList){
						InventoryArchivedDifferencesDTO archivedDifferencesDTO = new InventoryArchivedDifferencesDTO();
						
						archivedDifferencesDTO.setDifferenceList(archivedDiffList.getDifferenceList());
						archivedDifferencesDTO.setListId(archivedDiffList.getListId());
						archivedDifferencesDTO.setDate(commonUtils.convertDateInDDMMYYYY(archivedDiffList.getDate()));
						archivedDifferencesDTO.setInventoryId(archivedDiffList.getInventoryId());
						archivedDifferencesDTO.setCountDate(archivedDiffList.getCountDate());
						archivedDifferencesDTO.setWarehouseNo(archivedDiffList.getWarehouseNo());
						
						inventoryItemsDTOList.add(archivedDifferencesDTO);
					}
				} else {
					log.debug("There is no data for this period.");
					Message message = messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.INVENTORY_ARCHIVED_DATA_CHECK_FAILED_MSG_KEY);
					AlphaXException exception = new AlphaXException(message);
					log.error(messageService.getReadableMessage(ExceptionMessages.INVENTORY_ARCHIVED_DATA_CHECK_FAILED_MSG_KEY), exception);
					throw exception;
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Inventory archived list of Differences(Archivierte Differenzenlisten)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Inventory archived list of Differences(Archivierte Differenzenlisten)"), exception);
			throw exception;
		}

		return inventoryItemsDTOList;
	}
	
	/**
	 * This method is used to get the Inventory Print - Archived lists of differences in details (Archivierte Differenzenlisten) from Savlib.
	 */
	@Override
	public List<InventoryArchivedDifferencesDTO> getInventoryArchivedDifferencesInDetails(String dataLibrary, String savLibrary, String warehouseNo, 
			String date, String listId, String detailsType) {

		log.info("Inside getInventoryArchivedDifferencesInDetails method of InventoryEvalutionServiceImpl");
		boolean isAliasExist = false;
		List<InventoryArchivedDifferencesDTO> inventoryItemsDTOList = new ArrayList<InventoryArchivedDifferencesDTO>();

		try {			
			String formatted_date = commonUtils.getDateInYYMMDD(date);

			warehouseNo = StringUtils.leftPad(warehouseNo, 2, "0");

			//String year = checkDateWithCurrentYear(commonUtils.getDateInYYYYMMDD(date));
			String year = commonUtils.getDateInYYYYMMDD(date).substring(2, 4);

			if(null != year && !year.isEmpty()) {
				String tableName = "AE4000" + warehouseNo + year;
				String aliasName = "MBR" + warehouseNo + year;
				
				//Table check - table exist or not
				StringBuilder fileCheckCommand = new StringBuilder("CHKOBJ OBJ(").append(savLibrary).append("/").append(tableName).append(") OBJTYPE(*FILE)");
				boolean checkFile = cobolServiceRepository.executeCommandForFile(fileCheckCommand.toString());
				
				if(!checkFile){
					
					log.debug("This file is not available :"+tableName);
					Message message = messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.INVENTORY_ARCHIVED_FILE_CHECK_FAILED_MSG_KEY);
					AlphaXException exception = new AlphaXException(message);
					log.error(messageService.getReadableMessage(ExceptionMessages.INVENTORY_ARCHIVED_FILE_CHECK_FAILED_MSG_KEY), exception);
					throw exception;
				}
				
				//Alias name check
				
				isAliasExist = dbServiceRepository.isTableExist(savLibrary, aliasName);

				if (!isAliasExist) {
					StringBuilder creatAlias = new StringBuilder("CREATE ALIAS ").append(savLibrary).append(".").append(aliasName);
					creatAlias.append(" For ").append(savLibrary).append(".").append(tableName).append("(").append(tableName).append(")");
					dbServiceRepository.createTableUsingQuery(creatAlias.toString());
				}
				String incrementedListId = StringUtils.leftPad(String.valueOf(Integer.parseInt(listId)+1),4,"0");
				StringBuilder query = new StringBuilder("WITH auszug AS ");
				if(detailsType.equalsIgnoreCase("htmlPreview")){
					query.append(" (select cast(").append(tableName);
					query.append(" aS VARCHAR(256) CCSID 273) as Daten, ROW_NUMBER() OVER () AS Zeile FROM ").append(savLibrary).append(".").append(aliasName);
					query.append(") SELECT 	case substr(daten, 1, 2) when '00' THEN  '</pre><hr><pre>' || substr (daten, 5, length(daten)-4) ");
					query.append("else substr (daten, 5, length(daten)-4)  END  as RAW_CONTENT ");
					query.append("FROM auszug where zeile between  (select min(zeile) from auszug where daten like '%D I F F E R E N Z E N L I S T E  (").append(listId).append(")%') AND ");
					query.append(" ( select  case when min(zeile)-1 > 0 then min(zeile)-1 else (select count(*) from auszug)  end   ");
					query.append("from auszug where daten like '%D I F F E R E N Z E N L I S T E  (").append(incrementedListId).append(")%')");
				}else{
					query.append("  (select cast(").append(tableName);
					query.append(" aS VARCHAR(256) CCSID 273) as Daten, ROW_NUMBER() OVER () AS Zeile FROM ").append(savLibrary).append(".").append(aliasName);
					query.append(") SELECT substr (daten, 5, length(daten)-4) as RAW_CONTENT,");
					query.append("case substr(daten, 1, 2) when '00' THEN 'Y' else 'N' END as NewPage FROM auszug where zeile between ");
					query.append("(select min(zeile) from auszug where daten like '%D I F F E R E N Z E N L I S T E  (").append(listId).append(")%') ");
					query.append(" AND ( select  case when min(zeile)-1 > 0 then min(zeile)-1 else (select count(*) from auszug)  end  ");
					query.append("from auszug where daten like '%D I F F E R E N Z E N L I S T E  (").append(incrementedListId).append(")%')");
				}
				
				List<InventoryArchivedDifferences> inventoryArchivedDifferencesList = dbServiceRepository.getResultUsingQuery(InventoryArchivedDifferences.class, query.toString(), true);

				
				if (inventoryArchivedDifferencesList != null && !inventoryArchivedDifferencesList.isEmpty()) {
					
					for(InventoryArchivedDifferences archivedDiffList:inventoryArchivedDifferencesList){
						InventoryArchivedDifferencesDTO archivedDifferencesDTO = new InventoryArchivedDifferencesDTO();
						
						archivedDifferencesDTO.setRawContent(archivedDiffList.getRawContent());
						archivedDifferencesDTO.setPageType(archivedDiffList.getPageType());
						
						inventoryItemsDTOList.add(archivedDifferencesDTO);
					}
				} else {
					log.debug("There is no data for this period.");
					Message message = messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.INVENTORY_ARCHIVED_DATA_CHECK_FAILED_MSG_KEY);
					AlphaXException exception = new AlphaXException(message);
					log.error(messageService.getReadableMessage(ExceptionMessages.INVENTORY_ARCHIVED_DATA_CHECK_FAILED_MSG_KEY), exception);
					throw exception;
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Inventory archived list of Differences in detail(Archivierte Differenzenlisten)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Inventory archived list of Differences in detail(Archivierte Differenzenlisten)"), exception);
			throw exception;
		}

		return inventoryItemsDTOList;
	}
	
	
	
	/**
	 * This method is used to get the Inventory Archived Counting List (Archivierte Zähllisten) from Savlib.
	 */
	@Override
	public List<InventoryArchivedCountingDTO> getArchivedCountingList(String dataLibrary, String savLibrary, String warehouseNo, String fromDate, String toDate) {

		log.info("Inside getArchivedCountingList method of InventoryEvalutionServiceImpl");

		List<InventoryArchivedCountingDTO> inventoryItemsDTOList = new ArrayList<InventoryArchivedCountingDTO>();
		boolean isTableExist = false;

		try {			
			String formatted_FromDate = commonUtils.getDateInYYYYMMDD(fromDate);
			String formatted_ToDate = commonUtils.getDateInYYYYMMDD(toDate);

			warehouseNo = StringUtils.leftPad(warehouseNo, 2, "0");

			//String year = checkDateWithCurrentYear(formatted_FromDate);
			String year = formatted_FromDate.substring(2, 4);

			if(null != year && !year.isEmpty()) {
				String tableName = "E_I000" + warehouseNo + year;

				isTableExist = dbServiceRepository.isTableExist(savLibrary, tableName);
				
				if(isTableExist){
				StringBuilder query = new StringBuilder("select DISTINCT (I_INVDAT), I_TIME, ");
				query.append("substr(I_INVDAT,7,2) CONCAT '.' CONCAT substr(I_INVDAT,5,2) CONCAT '.' CONCAT substr(I_INVDAT,1,4) as Datum, ");
				query.append("CASE substr(I_INVDAT,5,2) ");
				query.append("WHEN  '01' THEN substr(I_INVDAT,1,4) CONCAT 'JAN' CONCAT substr(I_INVDAT,7,2) CONCAT I_TIME ");
				query.append("WHEN  '02' THEN substr(I_INVDAT,1,4) CONCAT 'FEB' CONCAT substr(I_INVDAT,7,2) CONCAT I_TIME ");
				query.append("WHEN  '03' THEN substr(I_INVDAT,1,4) CONCAT 'MAR' CONCAT substr(I_INVDAT,7,2) CONCAT I_TIME ");
				query.append("WHEN  '04' THEN substr(I_INVDAT,1,4) CONCAT 'APR' CONCAT substr(I_INVDAT,7,2) CONCAT I_TIME ");
				query.append("WHEN  '05' THEN substr(I_INVDAT,1,4) CONCAT 'MAI' CONCAT substr(I_INVDAT,7,2) CONCAT I_TIME ");
				query.append("WHEN  '06' THEN substr(I_INVDAT,1,4) CONCAT 'JUN' CONCAT substr(I_INVDAT,7,2) CONCAT I_TIME "); 
				query.append("WHEN  '07' THEN substr(I_INVDAT,1,4) CONCAT 'JUL' CONCAT substr(I_INVDAT,7,2) CONCAT I_TIME "); 
				query.append("WHEN  '08' THEN substr(I_INVDAT,1,4) CONCAT 'AUG' CONCAT substr(I_INVDAT,7,2) CONCAT I_TIME "); 
				query.append("WHEN  '09' THEN substr(I_INVDAT,1,4) CONCAT 'SEP' CONCAT substr(I_INVDAT,7,2) CONCAT I_TIME "); 
				query.append("WHEN  '10' THEN substr(I_INVDAT,1,4) CONCAT 'OKT' CONCAT substr(I_INVDAT,7,2) CONCAT I_TIME "); 
				query.append("WHEN  '11' THEN substr(I_INVDAT,1,4) CONCAT 'NOV' CONCAT substr(I_INVDAT,7,2) CONCAT I_TIME "); 
				query.append("WHEN  '12' THEN substr(I_INVDAT,1,4) CONCAT 'DEZ' CONCAT substr(I_INVDAT,7,2) CONCAT I_TIME "); 
				query.append("END as Inventurkennung, ");
				query.append("(select count(*) from ").append(savLibrary).append(".").append(tableName).append(" where i_invdat=tmp.I_INVDAT AND I_time=tmp.i_time) as  Anzahl_Paginiernummern from ");
				query.append(savLibrary).append(".").append(tableName).append(" tmp where tmp.I_INVDAT between ").append(formatted_FromDate).append(" AND ").append(formatted_ToDate);
						
				List<InventoryArchivedCounting> inventoryCountingList = dbServiceRepository.getResultUsingQuery(InventoryArchivedCounting.class, query.toString(), true);

					if (inventoryCountingList != null && !inventoryCountingList.isEmpty()) {

						for (InventoryArchivedCounting counting_obj : inventoryCountingList) {

							InventoryArchivedCountingDTO countingDTO = new InventoryArchivedCountingDTO();

							countingDTO.setDate(counting_obj.getDate());
							countingDTO.setInventoryId(counting_obj.getInventoryId());
							countingDTO.setNumberOfPagination(counting_obj.getNumberOfPagination());
							countingDTO.setInventoryDate(counting_obj.getInventoryDate());
							countingDTO.setInventoryTime(counting_obj.getInventoryTime());
							
							inventoryItemsDTOList.add(countingDTO);
						}
					}
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Archived Counting List (Archivierte Zähllisten)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Archived Counting List (Archivierte Zähllisten)"), exception);
			throw exception;
		}

		return inventoryItemsDTOList;
	}
	
	/**
	 * This method is used to get the Inventory Archived Counting detail List (Archivierte Zähllisten) from Savlib.
	 */
	@Override
	public List<InventoryArchivedCountingDTO> getArchivedCountingDetailList(String dataLibrary, String savLibrary, String warehouseNo, 
			String inventoryDate, String inventoryTime, String detailsType) {

		log.info("Inside getArchivedCountingDetailList method of InventoryEvalutionServiceImpl");

		List<InventoryArchivedCountingDTO> inventoryItemsDTOList = new ArrayList<InventoryArchivedCountingDTO>();
		boolean isTableExist = false;

		try {			

			warehouseNo = StringUtils.leftPad(warehouseNo, 2, "0");

			//String year = checkDateWithCurrentYear(inventoryDate);
			String year = inventoryDate.substring(2, 4);

			if(null != year && !year.isEmpty()) {
				String tableName = "E_I000" + warehouseNo + year;

				isTableExist = dbServiceRepository.isTableExist(savLibrary, tableName);
				
				if(isTableExist){
				StringBuilder query = new StringBuilder("Select ");
				query.append("CASE  WHEN MOD (I_LFDNR, 35) = 1 THEN (select ");
				query.append("'Zählliste Wiederholungsdruck                                    Datum: ' CONCAT substr(I_INVDAT, 7, 2)CONCAT'.'CONCAT  ");
				query.append("substr(I_INVDAT, 5, 2)CONCAT'.'CONCAT substr(I_INVDAT, 1, 4) CONCAT CHR(10) CONCAT CHR(13) CONCAT 'Inventur: ' CONCAT ");
				query.append("CASE substr(I_INVDAT,5,2) ");
				query.append("WHEN  '01' THEN substr(I_INVDAT,1,4) CONCAT 'JAN' CONCAT substr(I_INVDAT,7,2) CONCAT I_TIME ");
				query.append("WHEN  '02' THEN substr(I_INVDAT,1,4) CONCAT 'FEB' CONCAT substr(I_INVDAT,7,2) CONCAT I_TIME ");
				query.append("WHEN  '03' THEN substr(I_INVDAT,1,4) CONCAT 'MAR' CONCAT substr(I_INVDAT,7,2) CONCAT I_TIME ");
				query.append("WHEN  '04' THEN substr(I_INVDAT,1,4) CONCAT 'APR' CONCAT substr(I_INVDAT,7,2) CONCAT I_TIME ");
				query.append("WHEN  '05' THEN substr(I_INVDAT,1,4) CONCAT 'MAI' CONCAT substr(I_INVDAT,7,2) CONCAT I_TIME ");
				query.append("WHEN  '06' THEN substr(I_INVDAT,1,4) CONCAT 'JUN' CONCAT substr(I_INVDAT,7,2) CONCAT I_TIME "); 
				query.append("WHEN  '07' THEN substr(I_INVDAT,1,4) CONCAT 'JUL' CONCAT substr(I_INVDAT,7,2) CONCAT I_TIME "); 
				query.append("WHEN  '08' THEN substr(I_INVDAT,1,4) CONCAT 'AUG' CONCAT substr(I_INVDAT,7,2) CONCAT I_TIME "); 
				query.append("WHEN  '09' THEN substr(I_INVDAT,1,4) CONCAT 'SEP' CONCAT substr(I_INVDAT,7,2) CONCAT I_TIME "); 
				query.append("WHEN  '10' THEN substr(I_INVDAT,1,4) CONCAT 'OKT' CONCAT substr(I_INVDAT,7,2) CONCAT I_TIME "); 
				query.append("WHEN  '11' THEN substr(I_INVDAT,1,4) CONCAT 'NOV' CONCAT substr(I_INVDAT,7,2) CONCAT I_TIME "); 
				query.append("WHEN  '12' THEN substr(I_INVDAT,1,4) CONCAT 'DEZ' CONCAT substr(I_INVDAT,7,2) CONCAT I_TIME "); 
				query.append("END  CONCAT '                                         Seite: 'CONCAT lpad(cast ( (I_LFDNR / 35) as integer) + 1, 10, ' ' ) ");
				query.append("CONCAT CHR(10) CONCAT CHR(13) CONCAT 'Lager   : ' CONCAT lpad(I_LNR, 2, ' ' ) CONCAT ");
				query.append("'                                                    Datum: ' CONCAT TO_CHAR(now(), 'DD.MM.YYYY') CONCAT ");
				query.append("CHR(10) CONCAT CHR(13) CONCAT 'Nr    Lagerort OEM  Teilenummer	        Teilename              DAK    Soll    Ist' ");
				query.append("CONCAT CHR(10) CONCAT CHR(13) CONCAT ");
				query.append("lpad(I_LFDNR,5,' ') CONCAT ' ' CONCAT ");
				query.append("lpad(I_LOPA,8,' ') CONCAT ' ' CONCAT lpad(I_HERST,4,' ') CONCAT ' ' CONCAT CAST(I_ETNR as char(19)) CONCAT ' ' CONCAT ");
				query.append("CAST(I_BEN as char(15))CONCAT ' ' CONCAT  lpad(I_DAK, 10,' ' )CONCAT ' ' CONCAT CASE I_ALTBEST WHEN '0' THEN '    0.0' ");
				query.append("ELSE lpad(I_ALTBEST, 7,' ' )  END CONCAT ' ' CONCAT CASE I_ERFBEST WHEN '' THEN '    --'  ELSE lpad(cast (I_ERFBEST as int), 6, ' ' ) END ");
				query.append("CONCAT CHR(10) CONCAT CHR(13) from ").append(savLibrary).append(".").append(tableName).append(" where I_INVDAT= '").append(inventoryDate);
				query.append("' AND I_TIME= '").append(inventoryTime).append("' AND I_LFDNR=x.I_LFDNR) WHEN MOD (I_LFDNR, 35) = 0 THEN (select  lpad(I_LFDNR,5,' ') CONCAT ' ' CONCAT ");
				query.append("lpad(I_LOPA,8,' ') CONCAT ' ' CONCAT lpad(I_HERST,4,' ') CONCAT ' ' CONCAT  CAST(I_ETNR as char(19)) CONCAT ' ' CONCAT  CAST(I_BEN as char(15))CONCAT ' ' CONCAT ");
				query.append("lpad(I_DAK, 10,' ' )CONCAT ' ' CONCAT CASE I_ALTBEST WHEN '0' THEN '    0.0'  ELSE lpad(I_ALTBEST, 7, ' ' )  END CONCAT ' ' CONCAT ");
				if(detailsType.equalsIgnoreCase("htmlPreview")){
				query.append("CASE I_ERFBEST WHEN '' THEN '    --' ELSE lpad(cast (I_ERFBEST as int), 6, ' ' )  END  CONCAT CHR(10) CONCAT CHR(13) CONCAT '</pre><hr><pre>'  from ");
				}else{
					query.append("CASE I_ERFBEST WHEN '' THEN '    --' ELSE lpad(cast (I_ERFBEST as int), 6, ' ' )  END  CONCAT CHR(10) CONCAT CHR(13) from ");	
				}
				query.append(savLibrary).append(".").append(tableName).append(" where I_INVDAT= '").append(inventoryDate).append("' AND I_TIME= '").append(inventoryTime);
				query.append("' AND I_LFDNR=x.I_LFDNR)  ELSE (select  lpad(I_LFDNR, 5, ' ') CONCAT ' ' CONCAT  lpad(I_LOPA, 8, ' ') CONCAT ' ' CONCAT  lpad(I_HERST, 4, ' ') CONCAT ' ' CONCAT ");
				query.append(" CAST(I_ETNR as char(19)) CONCAT ' ' CONCAT  CAST(I_BEN as char(15))CONCAT ' ' CONCAT  lpad(I_DAK, 10, ' ' )CONCAT ' ' CONCAT CASE I_ALTBEST ");
				query.append("WHEN '0' THEN '    0.0' ELSE lpad(I_ALTBEST, 7, ' ' )  END CONCAT ' ' CONCAT  CASE I_ERFBEST  WHEN '' THEN '    --'  ");
				query.append("ELSE lpad(cast (I_ERFBEST as int), 6, ' ' ) END CONCAT CHR(10) CONCAT CHR(13) from ").append(savLibrary).append(".").append(tableName);
				query.append(" where I_INVDAT= '").append(inventoryDate).append("' AND I_TIME= '").append(inventoryTime).append("' AND I_LFDNR=x.I_LFDNR) END as xLINE from ");
				query.append(savLibrary).append(".").append(tableName).append(" as x where I_INVDAT= '").append(inventoryDate).append("' AND I_TIME= '").append(inventoryTime).append("'");
				
				List<InventoryArchivedCounting> inventoryCountingList = dbServiceRepository.getResultUsingQuery(InventoryArchivedCounting.class, query.toString(), true);

					if (inventoryCountingList != null && !inventoryCountingList.isEmpty()) {

						for (InventoryArchivedCounting counting_obj : inventoryCountingList) {

							InventoryArchivedCountingDTO countingDTO = new InventoryArchivedCountingDTO();

							countingDTO.setxLine(counting_obj.getxLine());
							
							inventoryItemsDTOList.add(countingDTO);
						}
					}
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Archived Counting Detail List (Archivierte Zähllisten)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Archived Counting Detail List (Archivierte Zähllisten)"), exception);
			throw exception;
		}

		return inventoryItemsDTOList;
	}
	
	
}