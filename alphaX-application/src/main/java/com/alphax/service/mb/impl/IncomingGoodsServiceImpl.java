package com.alphax.service.mb.impl;

import java.sql.Connection;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.alphax.common.rest.message.service.MessageService;
import com.alphax.model.mb.BSNConflictsDetail;
import com.alphax.model.mb.BSNDeliveryNotes;
import com.alphax.model.mb.BSNDeliveryNotesDetail;
import com.alphax.model.mb.DeliveryNotes;
import com.alphax.model.mb.DeliveryNotesDetail;
import com.alphax.model.mb.OrdersObjectDetails;
import com.alphax.model.mb.PartDetails;
import com.alphax.common.exception.AlphaXException;
import com.alphax.common.exception.ExceptionMessages;
import com.alphax.repository.CobolServiceRepository;
import com.alphax.repository.DBServiceRepository;
import com.alphax.repository.StubServiceRepository;
import com.alphax.service.mb.IncomingGoodsService;
import com.alphax.vo.mb.BSNConflictsDetailDTO;
import com.alphax.vo.mb.BSNDeliveryNotesDTO;
import com.alphax.vo.mb.BSNDeliveryNotesDetailDTO;
import com.alphax.vo.mb.ConflictResolutionDTO;
import com.alphax.vo.mb.DeliveryNoteSparePartDTO;
import com.alphax.vo.mb.DeliveryNotesDTO;
import com.alphax.vo.mb.DeliveryNotesDetailDTO;
import com.alphax.vo.mb.DropdownObject;
import com.alphax.vo.mb.GlobalSearch;
import com.alphax.common.constants.Program_Commands_Constants;
import com.alphax.common.constants.RestInputConstants;
import com.ibm.as400.access.ProgramParameter;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IncomingGoodsServiceImpl extends BaseService implements IncomingGoodsService {

	@Autowired
	DBServiceRepository dbServiceRepository;

	@Autowired
	private MessageService messageService;

	@Autowired
	CobolServiceRepository cobolServiceRepository;
	
	@Autowired
	StubServiceRepository stubServiceRepository;
	
	DecimalFormat decimalformat_twodigit = new DecimalFormat("#0.00", new DecimalFormatSymbols(Locale.ENGLISH));
	
	/*@Value("${alphax.datacenter.lib}")
	private String datacenterLibValue;*/

	
	/**
	 * This is method is used to get List of Lieferscheinliste (delivery notes) from DB.
	 */
	@Override
	public GlobalSearch getDeliveryNoteList(String dataLibrary, String companyId, String agencyId, Integer flag, String allowedWarehouses, String pageSize, String pageNumber,
			String sortingBy, String sortingType, List<String> warehouseNos) {

		log.info("Inside getDeliveryNoteList method of IncomingGoodsServiceImpl");

		List<DeliveryNotes> deliveryNoteList = new ArrayList<>();
		GlobalSearch globalSearchList = new GlobalSearch();
		StringBuilder query = new StringBuilder();

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
			
			//validate the Warehouse Ids
			validateWarehouses(allowedWarehouses);
			
			String orderByClause = getOrderByClause(sortingType, sortingBy, "DeliveryNote");
			
			if(warehouseNos != null && !warehouseNos.isEmpty()){
				 warehouseNos.removeIf(s -> s == null || "".equals(s.trim()));
				allowedWarehouses = StringUtils.join(warehouseNos, ",");
			}
			

			//SA=53 Incoming Delivery Notes
			if(flag == 1 ) {

				query = query.append("select distinct lf.Lieferant, lf.LFSNR, lf.HERST, lf.LNR,lf.AUFNR_ALT,lf.BESTHINW, ");
				query.append("(Select count(LFSNR) from ").append(dataLibrary).append(".e_lfsall where LFSNR =lf.LFSNR AND AUFNR_ALT = lf.AUFNR_ALT AND BESTHINW= lf.BESTHINW and SA=53 and BSN450 <> 'VVV' and BSN450 <> 'LLL' ");
				query.append(" and LNR in (").append(allowedWarehouses).append(")) AS anzahll, ");
				query.append("lf.DATUM_LFS, SA, (select distinct PIV from ").append(dataLibrary).append(".E_PIV where LFSNR = lf.LFSNR and HERST = lf.HERST and LNR = lf.LNR) as PIV, ");
				query.append("(select count(*) from ");
				query.append("(select  distinct Lieferant, LFSNR, HERST, LNR, DATUM_LFS, AUFNR_ALT, BESTHINW  from ").append(dataLibrary).append(".e_lfsall ");
				query.append(" where SA=53 and BSN450 <> 'VVV' and BSN450 <> 'LLL' and LNR in (").append(allowedWarehouses).append(") )) AS ROWNUMER from ");
				query.append(dataLibrary).append(".e_lfsall lf where SA=53 and BSN450 <> 'VVV' and BSN450 <> 'LLL' ");
				query.append(" and lf.LNR in (").append(allowedWarehouses).append(") ").append(orderByClause).append(" OFFSET ");
				query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");
			}
			//SA=54 OR SA=56 Announcement and Cancelled Delivery Notes - Positionen mit Terminen
			else if(flag == 2 ) {
				query = query.append("select distinct lf.Lieferant, lf.LFSNR, lf.HERST, lf.LNR, ");
				query.append("(Select count(LFSNR) from ").append(dataLibrary).append(".e_lfsall where LFSNR =lf.LFSNR and SA = lf.SA and BSN450 <> 'LLL' ");
				query.append(" and LNR in (").append(allowedWarehouses).append(") ) AS anzahll, lf.DATUM_LFS, SA, ");
				query.append("(select distinct PIV from ").append(dataLibrary).append(".E_PIV where LFSNR = lf.LFSNR and HERST = lf.HERST and LNR = lf.LNR) as PIV, ");
				query.append("(select count(*) from ");
				query.append("(select  distinct Lieferant, LFSNR, HERST, LNR, DATUM_LFS, SA  from ").append(dataLibrary).append(".e_lfsall ");
				query.append(" where (SA=54 OR SA= 56) and BSN450 <> 'LLL' and LNR in (").append(allowedWarehouses).append(") )) AS ROWNUMER from ");
				query.append(dataLibrary).append(".e_lfsall lf where (SA=54 OR SA=56) and BSN450 <> 'LLL' ");
				query.append(" and lf.LNR in (").append(allowedWarehouses).append(") ").append(orderByClause).append("  OFFSET ");
				query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");
			}
			else {
				log.info("flag value is not correct.");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.INVALID_FLAG_VALUE_MSG_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.INVALID_FLAG_VALUE_MSG_KEY));
				throw exception;
			}

			List<DeliveryNotesDTO> deliveryNoteDTOList = new ArrayList<>();
			deliveryNoteList = dbServiceRepository.getResultUsingQuery(DeliveryNotes.class, query.toString(), true);

			//if the list is not empty
			if (deliveryNoteList != null && !deliveryNoteList.isEmpty()) {

				deliveryNoteDTOList = convertDeliveyNoteEntityToDTO(deliveryNoteList, deliveryNoteDTOList);

				globalSearchList.setSearchDetailsList(deliveryNoteDTOList);
				globalSearchList.setTotalPages(Integer.toString(deliveryNoteList.get(0).getTotalCount()));
				globalSearchList.setTotalRecordCnt(Integer.toString(deliveryNoteList.get(0).getTotalCount()));
			}
			else {
				globalSearchList.setSearchDetailsList(deliveryNoteDTOList);
				globalSearchList.setTotalPages(Integer.toString(0));
				globalSearchList.setTotalRecordCnt(Integer.toString(0));
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Lieferscheinliste (delivery notes)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Lieferscheinliste (delivery notes)"), exception);
			throw exception;
		}

		return globalSearchList;
	}


	/**
	 * This method is used to convert Delivery Note Entity to delivery Note DTO Format.
	 * @param deliveryNote
	 * @return DeliveryNotesDTO
	 */
	private List<DeliveryNotesDTO> convertDeliveyNoteEntityToDTO(List<DeliveryNotes> deliveryNoteList, List<DeliveryNotesDTO> deliveryNoteDTOList) {

		for (DeliveryNotes deliveryNote : deliveryNoteList) {
			DeliveryNotesDTO deliveryNoteDTO = new DeliveryNotesDTO();

			deliveryNoteDTO.setSupplierNumber(deliveryNote.getSupplierNumber());
			deliveryNoteDTO.setDeliveryNoteNo(deliveryNote.getDeliveryNoteNo());
			if(deliveryNote.getManufacturer()!= null && deliveryNote.getManufacturer().equalsIgnoreCase(RestInputConstants.DCAG_STRING)) {
				deliveryNoteDTO.setManufacturer(RestInputConstants.DAG_STRING);
			}
			else {
				deliveryNoteDTO.setManufacturer(deliveryNote.getManufacturer());
			}
			deliveryNoteDTO.setWarehouseNumber(deliveryNote.getWarehouseNumber());
			deliveryNoteDTO.setNumberofPositions(Integer.toString(deliveryNote.getNumberofPositions()));
			deliveryNoteDTO.setDeliveryNoteDate(convertDateToString(deliveryNote.getDeliveryNoteDate()));
			deliveryNoteDTO.setSa(deliveryNote.getSa());
			deliveryNoteDTO.setPiv(deliveryNote.getPiv()!=null ? deliveryNote.getPiv(): "");
			
			deliveryNoteDTO.setOrderNumber(deliveryNote.getOrderNumber());
			deliveryNoteDTO.setNote(deliveryNote.getNote());
			
			deliveryNoteDTOList.add(deliveryNoteDTO);
		}

		return deliveryNoteDTOList;
	}


	/**
	 * This method is used to check date format should be  dd/MM/YYYY
	 * @param entityDate
	 * @return
	 */
	boolean checkDateFormat(String date , String format) {
		SimpleDateFormat sdfrmt = new SimpleDateFormat(format);
		sdfrmt.setLenient(false);
		boolean flag = true;
		try {
			sdfrmt.parse(date);
		} catch (Exception e) {
			log.info("Invalid Date format :"+date);
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.INVALID_DATE_FORMAT_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.INVALID_DATE_FORMAT_MSG_KEY), exception);
			flag =  false;
			throw exception;
			
		}
		/* Return true if date format is valid */
		return flag;

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
	 * This is method is used to get List of Lieferschein details (delivery notes Details) from DB.
	 */
	@Override
	public List<DeliveryNotesDetailDTO> getDeliveryNoteDetailsList(String deliveryNoteNo, String dataLibrary, String companyId, String agencyId, Integer flag,
			String orderNumber, String orderReference) {

		log.info("Inside getDeliveryNoteDetailsList method of IncomingGoodsServiceImpl");

		List<DeliveryNotesDetail> deliveryNoteDetailList = new ArrayList<>();
		List<DeliveryNotesDetailDTO> deliveryNoteDetailDTOList = new ArrayList<>();
		StringBuilder query = new StringBuilder();
		try {
			
			String dataCenterLibrary = null;
			ProgramParameter[] parmList = new ProgramParameter[3];

			String returnCode = StringUtils.rightPad("", 5, " ");
			parmList[0] = new ProgramParameter(returnCode.getBytes(Program_Commands_Constants.IBM_273), 5);

			String returnMsg = StringUtils.rightPad("", 100, " ");
			parmList[1] = new ProgramParameter(returnMsg.getBytes(Program_Commands_Constants.IBM_273), 10);

			String location = StringUtils.rightPad("", 10, " ");
			parmList[2] = new ProgramParameter(location.getBytes(Program_Commands_Constants.IBM_273), 10);

			// execute the COBOL program - OFSMFIN.PGM
			List<String> outputList = cobolServiceRepository.executeProgram(parmList, 0,Program_Commands_Constants.GET_DATA_CENTER_LIB_FOR_PREDECESSOR_SUCCESSOR);

			if (outputList != null && !outputList.isEmpty()) {
				if (!outputList.get(0).contains("Error")) {
					dataCenterLibrary = outputList.get(2).trim();
				}
			}
			log.info("DataCenter Library :" + dataCenterLibrary);
			if (dataCenterLibrary == null || dataCenterLibrary.isEmpty()) {
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(
						LocaleContextHolder.getLocale(), ExceptionMessages.INVALID_MSG_KEY, " DataCenter Library"));
				log.error(messageService.getReadableMessage(ExceptionMessages.INVALID_MSG_KEY, " DataCenter Library"),
						exception);
				throw exception;
			}

			
			////SA=53 Incoming Delivery Notes
			if(flag == 1) {
				query = query.append("select distinct lf.AUFNR_ALT, lf.POS_BEST, lf.TNR_BEST, ");
				query.append("(select benen from ").append(dataLibrary).append(".e_etstamm where tnr = lf.TNR_BEST and lnr = lf.LNR and herst = lf.HERST) AS BESTETEIL, ");
				query.append("lf.MENGE_BEST, lf.MENGE_GEL, lf.POS_LFS, lf.TNR_GEL, ");
				query.append("(select distinct ben from ").append(dataLibrary).append(".e_lfsall where tnr_gel = lf.TNR_GEL and lnr = lf.LNR and herst = lf.HERST and LFSNR='").append(deliveryNoteNo).append("' fetch first row only ) AS GELTEIL, lf.CODE_ERS, ");
				query.append("(select TEXT from ").append(dataCenterLibrary).append(".ETHTXTTAB where CODE = lf.CODE_ERS )AS BESTECODE, BESTHINW  from ");
				query.append(dataLibrary).append(".e_lfsall lf where SA=53 and BSN450 <> 'VVV' and BSN450 <> 'LLL' and LFSNR='").append(deliveryNoteNo).append("'");
				query.append(" AND AUFNR_ALT = ").append(orderNumber).append(" AND TRIM(BESTHINW) = '").append(orderReference.trim()) .append("' ORDER by POS_BEST");
			}
			//SA=54 OR SA=56 Announcement and Cancelled Delivery Notes - Positionen mit Terminen
			else if(flag == 2) {
				query = query.append("select distinct lf.AUFNR_ALT, lf.POS_BEST, lf.TNR_BEST, ");
				query.append("(select distinct benen from ").append(dataLibrary).append(".e_etstamm where tnr like concat(lf.TNR_BEST, '%') fetch first row only) AS BESTETEIL, ");
				query.append("lf.MENGE_BEST, lf.MENGE_GEL, lf.POS_LFS, lf.TNR_GEL, ");
				query.append("(select distinct benen from ").append(dataLibrary).append(".e_etstamm where tnr like concat(lf.TNR_GEL, '%') fetch first row only) AS GELTEIL, lf.CODE_ERS, ");
				query.append("(select TEXT from ").append(dataCenterLibrary).append(".ETHTXTTAB where CODE = lf.CODE_ERS )AS BESTECODE, BESTHINW  from ");
				query.append(dataLibrary).append(".e_lfsall lf where (SA=54 OR SA=56) and BSN450 <> 'LLL' and LFSNR='").append(deliveryNoteNo).append("'").append(" ORDER by POS_BEST");
			}
			else {
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.INVALID_FLAG_VALUE_MSG_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.INVALID_FLAG_VALUE_MSG_KEY));
				throw exception;
			}

			deliveryNoteDetailList = dbServiceRepository.getResultUsingQuery(DeliveryNotesDetail.class, query.toString(), true);

			//if the list is not empty
			if (deliveryNoteDetailList != null && !deliveryNoteDetailList.isEmpty()) {

				deliveryNoteDetailDTOList = convertDeliveyDetailsEntityToDTO(deliveryNoteDetailList, deliveryNoteDetailDTOList);
			}
			else{
				log.info("delivery notes Details List is empty");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Lieferschein details (delivery notes details)"));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Lieferschein details (delivery notes details)"));
				throw exception;
			}
		}
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Lieferschein details (delivery notes details)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Lieferschein details (delivery notes details)"), exception);
			throw exception;
		}

		return deliveryNoteDetailDTOList;

	}


	/**
	 * This method is used to convert Delivery Note Details  Entity to delivery Note Details DTO Format.
	 * @param details
	 * @return DeliveryNotesDetailDTO
	 */
	private List<DeliveryNotesDetailDTO> convertDeliveyDetailsEntityToDTO(List<DeliveryNotesDetail> deliveryNoteDetailList, 
			List<DeliveryNotesDetailDTO> deliveryNoteDetailDTOList) {

		for (DeliveryNotesDetail details : deliveryNoteDetailList) {
			DeliveryNotesDetailDTO detailsDTO = new DeliveryNotesDetailDTO();

			detailsDTO.setOrderNumber(details.getOrderNumber());
			detailsDTO.setOrderPosition(details.getOrderPosition());
			detailsDTO.setOrderedPartNo(details.getOrderedPartNo());
			detailsDTO.setOrderedPartDescription(details.getOrderedPartDescription()!=null?details.getOrderedPartDescription():"");
			detailsDTO.setOrderedQuantity(details.getOrderedQuantity());
			detailsDTO.setDeliveryNotePosition(details.getDeliveryNotePosition());
			detailsDTO.setSuppliedPartNo(details.getSuppliedPartNo());
			detailsDTO.setSuppliedPartDescription(details.getSuppliedPartDescription()!=null?details.getSuppliedPartDescription():"");
			detailsDTO.setDeliveredQuantity(details.getDeliveredQuantity()!=null?details.getDeliveredQuantity():"");
			detailsDTO.setNote(details.getNote());
			
			detailsDTO.setReplacementCode("");
			if(details.getReplacementCode() != null && (!details.getReplacementCode().equalsIgnoreCase("0") && !details.getReplacementCode().equalsIgnoreCase("00")) ) {
				detailsDTO.setReplacementCode(details.getReplacementCode() +"-"+ details.getReplacementCodeDesc());
			}
			
			deliveryNoteDetailDTOList.add(detailsDTO);

		}
		return deliveryNoteDetailDTOList;
	}


	/**
	 * This is method is used to get List of BSN Lieferschein (BSN delivery notes) from DB.
	 */
	@Override
	public GlobalSearch getBSN_DeliveryNoteList(String schema,String dataLibrary, String companyId, String agencyId, String allowedWarehouses, String pageSize, String pageNumber,
			String sortingBy, String sortingType, List<String> warehouseNos) {

		log.info("Inside getBSN_DeliveryNoteList method of IncomingGoodsServiceImpl");

		List<BSNDeliveryNotes> bsnDeliveryNoteList = new ArrayList<>();
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
			
			//validate the Warehouse Ids
			validateWarehouses(allowedWarehouses);
			
			String orderByClause = getOrderByClause(sortingType, sortingBy, "BSNDeliveryNote");
			
			if(warehouseNos != null && !warehouseNos.isEmpty()){
				 warehouseNos.removeIf(s -> s == null || "".equals(s.trim()));
				allowedWarehouses = StringUtils.join(warehouseNos, ",");
			}
			
			StringBuilder query = new StringBuilder("SELECT bsn.HERST, bsn.LNR, bsn.BEST_BENDL, bsn.LIEFNR_GESAMT, bsn.BDAT, ");
			query.append("(Select count(LIEFNR_GESAMT) from ").append(dataLibrary).append(".E_BSNDAT where LIEFNR_GESAMT = bsn.LIEFNR_GESAMT and STATUS BETWEEN 54 AND 57 and LIEFNR_GESAMT <>'' and ET_FEHLER IN ( 'X' , ' ', '*') AND DISPOUP <> 1 ");
			query.append(" AND LNR in (").append(allowedWarehouses).append(") AND ZUMENG <> 0 ");
			query.append(" AND NOT EXISTS (SELECT *  FROM ").append(schema).append(".O_BSNCONF B WHERE B.HERST = HERST and B.LNR = LNR and B.BEST_BENDL = BEST_BENDL and B.LFSNR = LIEFNR_GESAMT ) ) AS anzahll, ");
			query.append(" (select distinct PIV from ").append(dataLibrary).append(".E_PIV where LFSNR = bsn.LIEFNR_GESAMT and HERST = bsn.HERST and LNR = bsn.LNR) as PIV, ");
			query.append(" (select count(*) from ");
			query.append(" (select distinct HERST, LNR, BEST_BENDL, LIEFNR_GESAMT, BDAT from ").append(dataLibrary).append(".E_BSNDAT ");
			query.append(" Where STATUS BETWEEN 54 AND 57 AND LIEFNR_GESAMT <>'' AND  ET_FEHLER IN ( 'X' , ' ', '*') ");
			query.append(" AND LNR in (").append(allowedWarehouses).append(") AND ZUMENG <> 0 ");
			query.append(" AND NOT EXISTS (SELECT *  FROM ").append(schema).append(".O_BSNCONF B WHERE B.HERST = HERST and B.LNR = LNR and B.BEST_BENDL = BEST_BENDL and B.LFSNR = LIEFNR_GESAMT) Group BY HERST, LNR, BEST_BENDL, LFSNR, BDAT)) ");
			query.append(" as ROWNUMER from ");
			query.append(dataLibrary).append(".E_BSNDAT bsn where STATUS BETWEEN 54 AND 57 and LIEFNR_GESAMT <> '' AND  ET_FEHLER IN ( 'X' , ' ', '*') ");
			query.append(" AND LNR in (").append(allowedWarehouses).append(") AND ZUMENG <> 0 ");
			query.append(" AND NOT EXISTS (SELECT *  FROM ").append(schema).append(".O_BSNCONF B WHERE B.HERST = bsn.HERST and B.LNR = bsn.LNR and B.BEST_BENDL = bsn.BEST_BENDL and B.LFSNR = bsn.LIEFNR_GESAMT) Group BY HERST, LNR, BEST_BENDL, LFSNR, BDAT ").append(orderByClause).append(" OFFSET ");
			query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");

			List<BSNDeliveryNotesDTO> bsnDeliveryNoteDTOList = new ArrayList<>();
			bsnDeliveryNoteList = dbServiceRepository.getResultUsingQuery(BSNDeliveryNotes.class, query.toString(), true);

			//if the list is not empty
			if (bsnDeliveryNoteList != null && !bsnDeliveryNoteList.isEmpty()) {

				bsnDeliveryNoteDTOList = convertBSN_DeliveyNoteEntityToDTO(bsnDeliveryNoteList, bsnDeliveryNoteDTOList);

				globalSearchList.setSearchDetailsList(bsnDeliveryNoteDTOList);
				globalSearchList.setTotalPages(Integer.toString(bsnDeliveryNoteList.get(0).getTotalCount()));
				globalSearchList.setTotalRecordCnt(Integer.toString(bsnDeliveryNoteList.get(0).getTotalCount()));
			}
			else {
				globalSearchList.setSearchDetailsList(bsnDeliveryNoteDTOList);
				globalSearchList.setTotalPages(Integer.toString(0));
				globalSearchList.setTotalRecordCnt(Integer.toString(0));
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "BSN Lieferscheinliste (BSN delivery notes)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "BSN Lieferscheinliste (BSN delivery notes)"), exception);
			throw exception;
		}

		return globalSearchList;
	}


	/**
	 * This method is used to convert BSN Delivery Note Entity to BSN delivery Note DTO Format.
	 * @param bsnDeliveryNoteList
	 * @return BSNDeliveryNotesDTO
	 */
	private List<BSNDeliveryNotesDTO> convertBSN_DeliveyNoteEntityToDTO(List<BSNDeliveryNotes> bsnDeliveryNoteList, List<BSNDeliveryNotesDTO> bsnDeliveryNoteDTOList) {

		for (BSNDeliveryNotes bsnDeliveryNote : bsnDeliveryNoteList) {
			BSNDeliveryNotesDTO bsnNoteDTO = new BSNDeliveryNotesDTO();

			bsnNoteDTO.setSupplierNumber(bsnDeliveryNote.getSupplierNumber());
			bsnNoteDTO.setDeliveryNoteNo(bsnDeliveryNote.getDeliveryNoteNo());
			bsnNoteDTO.setDeliveryNoteDate(convertDateToString(bsnDeliveryNote.getDeliveryNoteDate()));
			bsnNoteDTO.setWarehouseNumber(bsnDeliveryNote.getWarehouseNumber());
			bsnNoteDTO.setPiv(bsnDeliveryNote.getPiv()!=null ? bsnDeliveryNote.getPiv(): "");
			if(bsnDeliveryNote.getManufacturer()!= null && bsnDeliveryNote.getManufacturer().equalsIgnoreCase(RestInputConstants.DCAG_STRING)) {
				bsnNoteDTO.setManufacturer(RestInputConstants.DAG_STRING);
			}
			else {
				bsnNoteDTO.setManufacturer(bsnDeliveryNote.getManufacturer());
			}
			bsnNoteDTO.setNumberofPositions(Integer.toString(bsnDeliveryNote.getNumberofPositions()));
			bsnDeliveryNoteDTOList.add(bsnNoteDTO);
		}

		return bsnDeliveryNoteDTOList;
	}


	/**
	 * This is method is used to get List of BSN Lieferschein details (BSN delivery notes Details) from DB.
	 */
	@Override
	public List<BSNDeliveryNotesDetailDTO> getBSN_DeliveryNoteDetailsList(String deliveryNoteNo, String dataLibrary, String companyId, String agencyId, 
			String supplierNumber, String manufacturer, String warehouseNumber, String date ) {

		log.info("Inside getBSN_DeliveryNoteDetailsList method of IncomingGoodsServiceImpl");

		List<BSNDeliveryNotesDetail> bsnDeliveryNoteDetailList = new ArrayList<>();
		List<BSNDeliveryNotesDetailDTO> bsnDeliveryNoteDetailDTOList = new ArrayList<>();

		try {

			if(manufacturer != null && manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING)) {
				manufacturer = RestInputConstants.DCAG_STRING;
			}
			
			if (date != null && !date.isEmpty() && checkDateFormat(date, "dd/MM/yyyy")) {
				StringBuilder dateBuilder = new StringBuilder();
				String dateValues[] = date.split("/");
				dateBuilder.append(dateValues[2]).append(StringUtils.leftPad(dateValues[1], 2, "0"));
				dateBuilder.append(StringUtils.leftPad(dateValues[0], 2, "0"));
				date = dateBuilder.toString();
			} 

			
			StringBuilder query = new StringBuilder("Select bsn.PSERV_AUFNR, bsn.SERV_AUFNRX, bsn.BESTPOS, bsn.BEST_ART, bsn.BEST_AP, bsn.ETNR, bsn.BEN, bsn.BEMENG, bsn.ZUMENG, ");
			query.append("(bsn.ZUMENG*bsn.EKNPR) as GelieferterWert, (bsn.BEMENG*bsn.EKNPR) as BestellterWert,bsn.GLMENG, bsn.BVER, bsn.DISPOPOS, bsn.DISPOUP, ");
			query.append(" (select etnr from ");
			query.append(dataLibrary).append(".E_BSNDAT WHERE LIEFNR_GESAMT='").append(deliveryNoteNo).append("'");
			query.append(" and HERST='").append(manufacturer).append("'");
			query.append(" and LNR='").append(warehouseNumber).append("'");
			query.append(" and BEST_BENDL='").append(supplierNumber);
			query.append("' and BDAT='").append(date).append("' and ET_FEHLER IN ( 'X' , ' ', '*') and DISPOUP = 1 and dispopos = bsn.dispopos and best_art = bsn.best_art  and best_ap = bsn.best_ap ) as deliveredPartNo , ");
			
			query.append(" (select BEN from ");
			query.append(dataLibrary).append(".E_BSNDAT WHERE LIEFNR_GESAMT='").append(deliveryNoteNo).append("'");
			query.append(" and HERST='").append(manufacturer).append("'");
			query.append(" and LNR='").append(warehouseNumber).append("'");
			query.append(" and BEST_BENDL='").append(supplierNumber);
			query.append("' and BDAT='").append(date).append("' and ET_FEHLER IN ( 'X' , ' ', '*') and DISPOUP = 1 and dispopos = bsn.dispopos and best_art = bsn.best_art  and best_ap = bsn.best_ap) as deliveredPartName , ");
			
			query.append(" (select BVER from ");
			query.append(dataLibrary).append(".E_BSNDAT WHERE LIEFNR_GESAMT='").append(deliveryNoteNo).append("'");
			query.append(" and HERST='").append(manufacturer).append("'");
			query.append(" and LNR='").append(warehouseNumber).append("'");
			query.append(" and BEST_BENDL='").append(supplierNumber);
			query.append("' and BDAT='").append(date).append("' and ET_FEHLER IN ( 'X' , ' ', '*') and DISPOUP = 1 and dispopos = bsn.dispopos and best_art = bsn.best_art  and best_ap = bsn.best_ap ) as deliveredPartOrderNote , ");
			
			query.append(" (select Zumeng from ");
			query.append(dataLibrary).append(".E_BSNDAT WHERE LIEFNR_GESAMT='").append(deliveryNoteNo).append("'");
			query.append(" and HERST='").append(manufacturer).append("'");
			query.append(" and LNR='").append(warehouseNumber).append("'");
			query.append(" and BEST_BENDL='").append(supplierNumber);
			query.append("' and BDAT='").append(date).append("' and ET_FEHLER IN ( 'X' , ' ', '*') and DISPOUP = 1 and dispopos = bsn.dispopos and best_art = bsn.best_art  and best_ap = bsn.best_ap) as deliveredPartQuantity from ");
			
			query.append(dataLibrary).append(".E_BSNDAT bsn WHERE bsn.LIEFNR_GESAMT='").append(deliveryNoteNo).append("'");
			query.append(" and bsn.HERST='").append(manufacturer).append("'");
			query.append(" and bsn.LNR='").append(warehouseNumber).append("'");
			query.append(" and bsn.BEST_BENDL='").append(supplierNumber);
			query.append("' and bsn.BDAT='").append(date).append("' and bsn.ET_FEHLER IN ( 'X' , ' ', '*') and bsn.DISPOUP <> 1 GROUP BY ");
			query.append("PSERV_AUFNR, SERV_AUFNRX, BESTPOS, DISPOPOS, DISPOUP, LIEFNR_GESAMT, BEST_ART, BEST_AP, ETNR, BEN, BEMENG, ZUMENG, GLMENG, EKNPR, BVER ");
			query.append("ORDER by BESTPOS ASC");
			
			

			bsnDeliveryNoteDetailList = dbServiceRepository.getResultUsingQuery(BSNDeliveryNotesDetail.class, query.toString(), true);

			//if the list is not empty
			if (bsnDeliveryNoteDetailList != null && !bsnDeliveryNoteDetailList.isEmpty()) {

				bsnDeliveryNoteDetailDTOList = convertBSN_DeliveyDetailsEntityToDTO(bsnDeliveryNoteDetailList, bsnDeliveryNoteDetailDTOList,
						deliveryNoteNo, supplierNumber, manufacturer, warehouseNumber,"",false);
			}
			else{
				log.info("BSN delivery notes Details List is empty");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "BSN Lieferschein details (BSN delivery notes details)"));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "BSN Lieferschein details (BSN delivery notes details)"));
				throw exception;
			}
		}
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "BSN Lieferschein details (BSN delivery notes details)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "BSN Lieferschein details (BSN delivery notes details)"), exception);
			throw exception;
		}

		return bsnDeliveryNoteDetailDTOList;

	}


	/**
	 * This method is used to convert BSN Delivery Note Details  Entity to BSN delivery Note Details DTO Format.
	 * @param fromConflict 
	 * @param schema 
	 * @param details
	 * @return DeliveryNotesDetailDTO
	 */
	private List<BSNDeliveryNotesDetailDTO> convertBSN_DeliveyDetailsEntityToDTO(List<BSNDeliveryNotesDetail> bsnDeliveryNoteDetailList, 
			List<BSNDeliveryNotesDetailDTO> bsnDeliveryNoteDetailDTOList, String deliveryNoteNo, String supplierNumber, String manufacturer, String warehouseNumber, String schema, boolean fromConflict) {

		for (BSNDeliveryNotesDetail details : bsnDeliveryNoteDetailList) {
			BSNDeliveryNotesDetailDTO detailsDTO = new BSNDeliveryNotesDetailDTO();

			detailsDTO.setPservAufnr(details.getPservAufnr());
			detailsDTO.setAservAufnr(details.getAservAufnr());
			detailsDTO.setDeliveryNotePosition(details.getDeliveryNotePosition());

			detailsDTO.setOrderNumber(StringUtils.join(StringUtils.leftPad(details.getOrderNumber1(),2,"0"), StringUtils.leftPad(details.getOrderNumber2(), 4, "0")));

			detailsDTO.setOrderedPartNo(details.getOrderedPartNo());
			detailsDTO.setPartDescription(details.getDeliveredPartName()!=null?details.getDeliveredPartName():details.getPartDescription()!=null?details.getPartDescription():"");
			detailsDTO.setOrderedQuantity(details.getOrderedQuantity());
			detailsDTO.setOrderedValue(details.getOrderedValue());
			String deliveredQuantity = "";
			if(details.getDeliveredPartQuantity()!= null){
				deliveredQuantity = details.getDeliveredPartQuantity();
			}else if(details.getDeliveredQuantity()!= null){
				deliveredQuantity = details.getDeliveredQuantity();
			}
			//detailsDTO.setDeliveredQuantity(details.getDeliveredQuantity()!=null?details.getDeliveredQuantity():"");
			detailsDTO.setDeliveredQuantity(deliveredQuantity);
			detailsDTO.setDeliveredValue(details.getDeliveredGelieferterWert()!=null?details.getDeliveredGelieferterWert():details.getDeliveredValue());
			detailsDTO.setQuantityCount(details.getQuantityCount()!=null?details.getQuantityCount():"");
			detailsDTO.setOrderNote(details.getDeliveredPartOrderNote()!=null?details.getDeliveredPartOrderNote(): details.getOrderNote()!= null ? details.getOrderNote() : "");
			
			detailsDTO.setSupplierNumber(supplierNumber);
			detailsDTO.setDeliveryNoteNo(deliveryNoteNo);
			
			manufacturer = manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING) ? RestInputConstants.DCAG_STRING: manufacturer;
				
			detailsDTO.setManufacturer(manufacturer);
			detailsDTO.setWarehouseNumber(warehouseNumber);
			
			detailsDTO.setIsConflict((details.getConflict_Count()!= null && details.getConflict_Count()>0) ? true:false);	
			detailsDTO.setConflict_Reasons(details.getConflict_Reasons() !=null ? details.getConflict_Reasons(): "");	
			detailsDTO.setConflict_Code(details.getConflict_Code() !=null ? details.getConflict_Code(): "");
			detailsDTO.setDispopos(details.getDispopos());
			detailsDTO.setDispoup(details.getDeliveredDispoup()!=null?details.getDeliveredDispoup():details.getDispoup());
			
			detailsDTO.setDeliveredPartNo(details.getDeliveredPartNo()!=null?details.getDeliveredPartNo():details.getOrderedPartNo()!=null?details.getOrderedPartNo():"");
			detailsDTO.setDeliveredDispoup(details.getDeliveredDispoup()!=null?details.getDeliveredDispoup():details.getDispoup());
			
			if(fromConflict){
				List<BSNDeliveryNotesDetail> conflictDetailsList = new ArrayList<>();

				StringBuilder query = new StringBuilder("Select LISTAGG(TRIM(HINT), '/ ') AS conflict_ERR  ,  LISTAGG(TRIM(ERROR_CODE), '/ ') AS ERROR_CODE FROM ");
				query.append(schema).append(".O_BSNCONF WHERE ");
				query.append("HERST='").append(manufacturer).append("' ");
				query.append("and LNR=").append(warehouseNumber);
				query.append(" and BEST_ART =").append(details.getOrderNumber1());
				query.append(" and BEST_AP =").append(details.getOrderNumber2());
				query.append(" and BEST_BENDL=").append(supplierNumber);
				query.append(" and DISPOPOS=").append(details.getDispopos());
				query.append(" and DISPOUP=").append(details.getDeliveredDispoup()!=null?details.getDeliveredDispoup():details.getDispoup());
				query.append(" and ETNR='").append(detailsDTO.getDeliveredPartNo()).append("'");
				query.append(" and LFSNR='").append(deliveryNoteNo).append("' and SOLVED=' '  ");
				
				
				conflictDetailsList = dbServiceRepository.getResultUsingQuery(BSNDeliveryNotesDetail.class, query.toString(), true);

				//if the list is not empty
				if (conflictDetailsList != null && !conflictDetailsList.isEmpty() 
						&& conflictDetailsList.get(0).getConflict_Reasons() != null && conflictDetailsList.get(0).getConflict_Code() != null) {
					detailsDTO.setIsConflict(true);	
					detailsDTO.setConflict_Reasons(conflictDetailsList.get(0).getConflict_Reasons() !=null ? conflictDetailsList.get(0).getConflict_Reasons(): "");	
					detailsDTO.setConflict_Code(conflictDetailsList.get(0).getConflict_Code() !=null ? conflictDetailsList.get(0).getConflict_Code(): "");
				}
				else{
					detailsDTO.setIsConflict(false);	
					detailsDTO.setConflict_Reasons("");	
					detailsDTO.setConflict_Code("");
				}
			}
			
			bsnDeliveryNoteDetailDTOList.add(detailsDTO);

		}
		return bsnDeliveryNoteDetailDTOList;
	}


	@Override
	public DeliveryNoteSparePartDTO updateDeliveryNoteSparePartNumber(DeliveryNoteSparePartDTO partDeliveryNoteDtl,
			String dataLibrary, String companyId, String agencyId) {
		log.info("Inside updateDeliveryNoteSparePartNumber method of IncomingGoodsServiceImpl");

		//validate the company Id 
		//validateCompany(companyId);

		String compID =  StringUtils.stripStart(companyId, "0");
		String agencyID = StringUtils.stripStart(agencyId, "0");
		log.info("Company ID  xx:  {}  and  Agency ID yy:  {}", compID, agencyID);
		ProgramParameter[] parmList;
		DeliveryNoteSparePartDTO partDeliveryNoteRecript =null;

		try{

			parmList = new ProgramParameter[11];

			// Create the input parameter 
			String returnCode = StringUtils.rightPad("",5," ");
			String returnMsg = StringUtils.rightPad("",100," ");
			parmList[0] = new ProgramParameter(returnCode.getBytes(Program_Commands_Constants.IBM_273),3);
			parmList[1] = new ProgramParameter(returnMsg.getBytes(Program_Commands_Constants.IBM_273),5);
			String funktion =  "ANDERN";
			parmList[2] = new ProgramParameter(funktion.getBytes(Program_Commands_Constants.IBM_273),6);

			String oem = StringUtils.rightPad(partDeliveryNoteDtl.getManufacturer(),4," ");
			parmList[3] = new ProgramParameter(oem.getBytes(Program_Commands_Constants.IBM_273),3);

			String warehouse = StringUtils.leftPad(partDeliveryNoteDtl.getWarehouseNumber(),2,"0");
			parmList[4] = new ProgramParameter(warehouse.getBytes(Program_Commands_Constants.IBM_273),2);

			String supplier = StringUtils.leftPad(partDeliveryNoteDtl.getSupplierNumber(),5,"0");
			parmList[5] = new ProgramParameter(supplier.getBytes(Program_Commands_Constants.IBM_273),3);

			String deliveryNoteNumber = StringUtils.rightPad(partDeliveryNoteDtl.getDeliveryNoteNumber(),10," ");
			parmList[6] = new ProgramParameter(deliveryNoteNumber.getBytes(Program_Commands_Constants.IBM_273),3);

			String orderNumber = StringUtils.leftPad(partDeliveryNoteDtl.getOrderNumber(),5,"0");
			parmList[7] = new ProgramParameter(orderNumber.getBytes(Program_Commands_Constants.IBM_273),3);

			String sa = StringUtils.leftPad(partDeliveryNoteDtl.getStorageIndikator(),3,"0");
			parmList[8] = new ProgramParameter(sa.getBytes(Program_Commands_Constants.IBM_273),3);

			String position = StringUtils.leftPad(partDeliveryNoteDtl.getOrderPosition(),5,"0");
			parmList[9] = new ProgramParameter(position.getBytes(Program_Commands_Constants.IBM_273),3);

			String newTNR = StringUtils.rightPad(partDeliveryNoteDtl.getNewTnr(),19," ");
			parmList[10] = new ProgramParameter(newTNR.getBytes(Program_Commands_Constants.IBM_273),3);

			List<String> outputList = cobolServiceRepository.executeProgram(parmList, 0,Program_Commands_Constants.UPDATE_DELIVERY_NOTES_PAREPART_NUMBER );

			if(outputList != null && !outputList.isEmpty()) {
				if(!outputList.get(0).contains("Error")) {
					partDeliveryNoteRecript = new DeliveryNoteSparePartDTO();
					partDeliveryNoteRecript.setReturnCode(outputList.get(0).trim());
					partDeliveryNoteRecript.setReturnMsg(outputList.get(1).trim());
					partDeliveryNoteRecript.setFunktion(outputList.get(2).trim());
					partDeliveryNoteRecript.setManufacturer(outputList.get(3).trim());
					partDeliveryNoteRecript.setWarehouseNumber(outputList.get(4).trim());
					partDeliveryNoteRecript.setSupplierNumber(outputList.get(5).trim());
					partDeliveryNoteRecript.setDeliveryNoteNumber(outputList.get(6).trim());
					partDeliveryNoteRecript.setOrderNumber(outputList.get(7).trim());
					partDeliveryNoteRecript.setStorageIndikator(outputList.get(8).trim());
					partDeliveryNoteRecript.setOrderPosition(outputList.get(9).trim());
					partDeliveryNoteRecript.setNewTnr(outputList.get(10).trim());

				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "TNR"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "TNR"), exception);
			throw exception;
		}
		return partDeliveryNoteRecript;
	}


	@Override
	public Map<String, String> takeOverdeliveryNotetoBSN(List<DeliveryNotesDTO> deliveryNoteList,
			String dataLibrary, boolean isBSNAll, String companyId, String agencyId, String filterText, String filterBy, List<String> warehouseNos) {
		log.info("Inside takeOverdeliveryNotetoBSN method of IncomingGoodsServiceImpl");

		ProgramParameter[] parmList;
		Map<String, String> programOutput = new HashMap<String, String>();
		
		try{

			// Create the input parameter 
			String returnCode = StringUtils.rightPad("00000", 5, " ");
			String returnMsg  = StringUtils.rightPad("", 100, " ");
			String lnr_herst = "";
			String parameter4 = StringUtils.rightPad("BSNSEL", 6, " ");
			String finalWarehouse = "";
			
			companyId = StringUtils.leftPad(companyId, 2, "0");
			agencyId = StringUtils.leftPad(agencyId, 2, "0");
			String companyAndAgency = StringUtils.rightPad(companyId+agencyId, 4, " ");
			
			if(warehouseNos != null && !warehouseNos.isEmpty()){
				 finalWarehouse = StringUtils.join(warehouseNos, ",");
			}

			if(isBSNAll) {
				parmList = new ProgramParameter[8];
				StringBuilder query = new StringBuilder();
				
				if(filterBy != null && filterText != null && filterBy.equalsIgnoreCase("Lieferscheinnummer")) {
					
					query = query.append("select distinct lf.Lieferant, lf.LFSNR, lf.HERST, lf.LNR, ");
					query.append("(Select count(LFSNR) from ").append(dataLibrary).append(".e_lfsall where LFSNR =lf.LFSNR and SA=53 and BSN450 <> 'VVV' and BSN450 <> 'LLL' ");
					query.append(" and LNR in (").append(finalWarehouse).append(") ");
					query.append(" and UPPER(LFSNR) LIKE ").append("UPPER('%"+filterText+"%') ");
					query.append(") AS anzahll, ");
					query.append("lf.DATUM_LFS, SA, (select distinct PIV from ").append(dataLibrary).append(".E_PIV where LFSNR = lf.LFSNR and HERST = lf.HERST and LNR = lf.LNR) as PIV ");
					query.append("from ");
					query.append(dataLibrary).append(".e_lfsall lf where SA=53 and BSN450 <> 'VVV' and BSN450 <> 'LLL' ");
					query.append(" and lf.LNR in (").append(finalWarehouse).append(") ");
					query.append(" and UPPER(LFSNR) LIKE ").append("UPPER('%"+filterText+"%')");
					query.append(" order by LFSNR ");
				}
				else if(filterBy != null && filterText != null && filterBy.equalsIgnoreCase("PIV")) {
					
					query = query.append("select distinct lf.Lieferant, lf.LFSNR, lf.HERST, lf.LNR, piv.PIV, ");
					query.append("(Select count(LFSNR) from ").append(dataLibrary).append(".e_lfsall where LFSNR =lf.LFSNR and SA=53 and BSN450 <> 'VVV' and BSN450 <> 'LLL' ");
					query.append(" and LNR in (").append(finalWarehouse).append(") ");
					query.append(") AS anzahll, ");
					query.append("lf.DATUM_LFS, SA ");
					query.append(" from ");
					query.append(dataLibrary).append(".e_lfsall lf,").append(dataLibrary).append(".E_PIV piv where lf.SA=53 and lf.BSN450 <> 'VVV' and lf.BSN450 <> 'LLL' ");
					query.append(" and lf.LNR in (").append(finalWarehouse).append(") ");
					query.append(" and piv.LFSNR = lf.LFSNR and piv.HERST = lf.HERST and piv.LNR = lf.LNR and piv.PIV LIKE ").append("UPPER('%"+filterText+"%')");
					query.append(" order by LFSNR ");			
				}
				else {
					query = query.append("select distinct lf.Lieferant, lf.LFSNR, lf.HERST, lf.LNR, ");
					query.append("(Select count(LFSNR) from ").append(dataLibrary).append(".e_lfsall where LFSNR =lf.LFSNR and SA=53 and BSN450 <> 'VVV' and BSN450 <> 'LLL' ");
					query.append(" and LNR in (").append(finalWarehouse).append(") ");
					query.append(") AS anzahll, ");
					query.append("lf.DATUM_LFS, SA, (select distinct PIV from ").append(dataLibrary).append(".E_PIV where LFSNR = lf.LFSNR and piv.HERST = lf.HERST and piv.LNR = lf.LNR) as PIV ");
					query.append("from ");
					query.append(dataLibrary).append(".e_lfsall lf where SA=53 and BSN450 <> 'VVV' and BSN450 <> 'LLL' ");
					query.append(" and lf.LNR in (").append(finalWarehouse).append(") ");
					query.append(" order by LFSNR ");
				}
				
				List<DeliveryNotes> deliveryNoteListDB = dbServiceRepository.getResultUsingQuery(DeliveryNotes.class, query.toString(), true);

				//if the list is not empty
				if (deliveryNoteListDB != null && !deliveryNoteListDB.isEmpty()) {

					List<DeliveryNotesDTO> deliveryNoteDTODBList = new ArrayList<>();

					deliveryNoteDTODBList = convertDeliveyNoteEntityToDTO(deliveryNoteListDB, deliveryNoteDTODBList);

					//calculate the warehouse and manufacturer value
					lnr_herst = getLnrHerstvalue(deliveryNoteDTODBList);
				}
				parameter4 = StringUtils.rightPad("BSNALL", 6, " ");
				
				if(filterBy != null && filterText != null && filterBy.equalsIgnoreCase("Lieferscheinnummer")) {
					parameter4 = StringUtils.rightPad("BSNFIL", 6, " ");
					parmList = setCommonParameters(parmList, returnCode, returnMsg, lnr_herst, parameter4, companyAndAgency, filterText, "", finalWarehouse);
				}
				else if(filterText != null && !filterText.trim().isEmpty() && filterBy.equalsIgnoreCase("PIV")){
					parameter4 = StringUtils.rightPad("BSNFIL", 6, " ");
					parmList = setCommonParameters(parmList, returnCode, returnMsg, lnr_herst, parameter4, companyAndAgency, "", filterText, finalWarehouse);
				}
				else {
					parmList = setCommonParameters(parmList, returnCode, returnMsg, lnr_herst, parameter4, companyAndAgency, "", "", finalWarehouse);
				}	

			}
			else {

				if(deliveryNoteList!=null && !deliveryNoteList.isEmpty()) {

					parmList = new ProgramParameter[(deliveryNoteList.size()*7)+8];
					int paramCount = 8;

					//calculate the warehouse and manufacturer value
					lnr_herst  = getLnrHerstvalue(deliveryNoteList);

					log.debug("lnr_herst:- {}", lnr_herst);

					parmList = setCommonParameters(parmList, returnCode, returnMsg, lnr_herst, parameter4, companyAndAgency, "", "", finalWarehouse);

					parmList = prepareParamList(deliveryNoteList, parmList, paramCount);
				}
				else {
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "deliveryNote"));
					log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "deliveryNote"), exception);
					throw exception;
				}
			}

			List<String> outputList = cobolServiceRepository.executeProgram(parmList, 0, Program_Commands_Constants.DELIVERY_NOTES_TO_BSN_PROGRAM );

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
					ExceptionMessages.DELIVERYNOTE_TO_BSN_FAILED_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.DELIVERYNOTE_TO_BSN_FAILED_MSG_KEY), exception);
			throw exception;
		}
		return programOutput;
	}

	/**
	 * This method is used to get unique lnr_Herst combination values from deliveryNoteList
	 * 
	 * @param deliveryNoteList
	 * @return lnr_Herst value
	 */
	private String getLnrHerstvalue( List<DeliveryNotesDTO> deliveryNoteList ) {

		String lnr_Herst = "";
		Set<String> lnrHerstSet = new LinkedHashSet<>();

		//Compare by warehouse number and then Manufacturer
		Comparator<DeliveryNotesDTO> compareBylnr_herst = Comparator.comparing(DeliveryNotesDTO::getManufacturer)
				.thenComparing(DeliveryNotesDTO::getWarehouseNumber);

		List<DeliveryNotesDTO> sortedDeliveryNoteList = deliveryNoteList.stream()
				.sorted(compareBylnr_herst).collect(Collectors.toList());

		for(DeliveryNotesDTO deliveryNotes : sortedDeliveryNoteList) {

			lnrHerstSet.add(StringUtils.leftPad(deliveryNotes.getWarehouseNumber(), 2, "0") 
					+ StringUtils.rightPad(deliveryNotes.getManufacturer(), 4, " "));
		}

		//now collect Set unique values in one lnr_Herst String.
		lnr_Herst = lnrHerstSet.stream().collect(Collectors.joining());

		lnr_Herst = StringUtils.rightPad(lnr_Herst, 120, " ");

		//return the calculated value
		return lnr_Herst;
	}

	
	/**
	 * This method is used to set common parameters for Cobol program.
	 * @param parmList
	 * @param returnCode
	 * @param returnMsg
	 * @param lnr_herst
	 * @param parameter4
	 * @param companyAndAgency 
	 * @return parmList
	 * @throws Exception
	 */
	private ProgramParameter[] setCommonParameters(ProgramParameter[] parmList, String returnCode, String returnMsg, String lnr_herst, String parameter4, 
			String companyAndAgency, String filterTextLFS, String filterTextPIV, String warehouses) throws Exception {

		parmList[0] = new ProgramParameter(returnCode.getBytes(Program_Commands_Constants.IBM_273),3);
		parmList[1] = new ProgramParameter(returnMsg.getBytes(Program_Commands_Constants.IBM_273),5);
		parmList[2] = new ProgramParameter(lnr_herst.getBytes(Program_Commands_Constants.IBM_273),5);
		parmList[3] = new ProgramParameter(parameter4.getBytes(Program_Commands_Constants.IBM_273),6);
		parmList[4] = new ProgramParameter(companyAndAgency.getBytes(Program_Commands_Constants.IBM_273),4);
		
		filterTextLFS = "%"+filterTextLFS;
		filterTextLFS = StringUtils.rightPad(filterTextLFS, 12, "%");
		parmList[5] = new ProgramParameter(filterTextLFS.getBytes(Program_Commands_Constants.IBM_273), 4);
		
		filterTextPIV = "%"+filterTextPIV;
		filterTextPIV = StringUtils.rightPad(filterTextPIV, 12, "%");
		parmList[6] = new ProgramParameter(filterTextPIV.getBytes(Program_Commands_Constants.IBM_273), 4);
	
		warehouses = StringUtils.rightPad(StringUtils.remove(warehouses, ","), 300, "0");
		parmList[7] = new ProgramParameter(warehouses.getBytes(Program_Commands_Constants.IBM_273), 4);

		return parmList;
	}
	
	
	/**
	 * This method is used to create parameter list for program based on delivery List
	 * @param deliveryNoteList
	 * @param parmList
	 * @param paramCount
	 * @return
	 */
	private ProgramParameter[] prepareParamList(List<DeliveryNotesDTO> deliveryNoteList, ProgramParameter[] parmList, int paramCount) throws Exception {

		String manufacturer = "";
		String warehouseNumber = "";
		String sa = "";
		String supplierNumber = "";
		String deliveryNoteNo = "";
		String orderNumber = "";
		String OrderNote = "";

		//iterate loop to create parameters
		for(int i = 0; i< deliveryNoteList.size(); i++) {

			manufacturer = StringUtils.rightPad(deliveryNoteList.get(i).getManufacturer(), 4, " ");
			parmList[paramCount] = new ProgramParameter(manufacturer.getBytes(Program_Commands_Constants.IBM_273),4);
			paramCount++;

			warehouseNumber = StringUtils.leftPad(deliveryNoteList.get(i).getWarehouseNumber(), 2, "0");
			parmList[paramCount] = new ProgramParameter(warehouseNumber.getBytes(Program_Commands_Constants.IBM_273),2);
			paramCount++;
			
			sa = StringUtils.leftPad(deliveryNoteList.get(i).getSa(), 3, "0");
			parmList[paramCount] = new ProgramParameter(sa.getBytes(Program_Commands_Constants.IBM_273),2);
			paramCount++;

			supplierNumber = StringUtils.leftPad(deliveryNoteList.get(i).getSupplierNumber(), 5, "0");
			parmList[paramCount] = new ProgramParameter(supplierNumber.getBytes(Program_Commands_Constants.IBM_273),5);
			paramCount++;

			deliveryNoteNo = StringUtils.rightPad(deliveryNoteList.get(i).getDeliveryNoteNo(), 10, " ");
			parmList[paramCount] = new ProgramParameter(deliveryNoteNo.getBytes(Program_Commands_Constants.IBM_273),10);
			paramCount++;
			
			orderNumber = deliveryNoteList.get(i).getOrderNumber();
			orderNumber = StringUtils.leftPad(orderNumber!=null ? orderNumber:"", 5, "0");
			parmList[paramCount] = new ProgramParameter(orderNumber.getBytes(Program_Commands_Constants.IBM_273),5);
			paramCount++;
			
			OrderNote = deliveryNoteList.get(i).getNote();
			OrderNote = StringUtils.rightPad(OrderNote!=null ? OrderNote :"", 12, " ");
			parmList[paramCount] = new ProgramParameter(OrderNote.getBytes(Program_Commands_Constants.IBM_273),12);
			paramCount++;
			
		}
		//return the parameter List
		return parmList;
	}


	@Override
	public Map<String, String> takeOverdeliveryNoteFromBSNToETStamm(List<BSNDeliveryNotesDTO> bsnDeliveryNoteList,
			boolean isEtAll, String dataLibrary, String companyId, String agencyId, String filterText, String filterBy, List<String> warehouseNos) {
		log.info("Inside takeOverdeliveryNoteFromBSNToETStamm method of IncomingGoodsServiceImpl");

		ProgramParameter[] parmList;
		Map<String, String> programOutput = new HashMap<String, String>();
		if(isEtAll){
			parmList = new ProgramParameter[8];
		}
		else{
			parmList = new ProgramParameter[(bsnDeliveryNoteList.size()*4)+12];
		}

		String manufacturer = "";
		String warehouseNumber = "";
		String supplierNumber = "";
		String deliveryNoteNo = "";
		String funktion = "";
		String finalWarehouse = "";
		int paramCount = 12;

		// Create the input parameter 
		String returnCode = StringUtils.rightPad("", 5, " ");
		String returnMsg  = StringUtils.rightPad("", 100, " ");
		

		try{
			
			companyId = StringUtils.leftPad(companyId, 2, "0");
			agencyId = StringUtils.leftPad(agencyId, 2, "0");
			String companyAndAgency = StringUtils.rightPad(companyId+agencyId, 4, " ");
			
			if(bsnDeliveryNoteList!=null && !bsnDeliveryNoteList.isEmpty() && !isEtAll) {

				funktion =  "ZUGANG";
				String pivNumber;
				if(bsnDeliveryNoteList.get(0).getPiv() != null && !bsnDeliveryNoteList.get(0).getPiv().isEmpty()){
					pivNumber  = StringUtils.leftPad(bsnDeliveryNoteList.get(0).getPiv(), 10, "0");
				}else{
					pivNumber  = StringUtils.leftPad("", 10, " ");	
				}
				parmList[0] = new ProgramParameter(returnCode.getBytes(Program_Commands_Constants.IBM_273), 3);
				parmList[1] = new ProgramParameter(returnMsg.getBytes(Program_Commands_Constants.IBM_273), 5);
				parmList[2] = new ProgramParameter(funktion.getBytes(Program_Commands_Constants.IBM_273), 5);
				parmList[3] = new ProgramParameter(companyAndAgency.getBytes(Program_Commands_Constants.IBM_273), 4);
				
				/* ---- This change is for filter and warehouse selection on Incoming goods Screen ---*/
				String inLFS =  StringUtils.rightPad("", 12, "%");
				String inPIV =  StringUtils.rightPad("", 12, "%");
				String inLIF =  StringUtils.rightPad("", 7, "%");
				String inWarehouses = StringUtils.rightPad("", 300, "0");
				
				parmList[4] = new ProgramParameter(inLFS.getBytes(Program_Commands_Constants.IBM_273), 4);
				parmList[5] = new ProgramParameter(inPIV.getBytes(Program_Commands_Constants.IBM_273), 4);
				parmList[6] = new ProgramParameter(inLIF.getBytes(Program_Commands_Constants.IBM_273), 4);
				parmList[7] = new ProgramParameter(inWarehouses.getBytes(Program_Commands_Constants.IBM_273), 4);
				/* -------------- */
				
				parmList[8] = new ProgramParameter(pivNumber.getBytes(Program_Commands_Constants.IBM_273),5);
				
				String menge = StringUtils.rightPad("", 9, " ");
				parmList[9] = new ProgramParameter(menge.getBytes(Program_Commands_Constants.IBM_273), 2);
				
				String partNumber = StringUtils.rightPad("", 19, " ");
				parmList[10] = new ProgramParameter(partNumber.getBytes(Program_Commands_Constants.IBM_273), 2);
				
				String partPosition = StringUtils.rightPad("", 4, " ");
				parmList[11] = new ProgramParameter(partPosition.getBytes(Program_Commands_Constants.IBM_273), 2);

				//iterate loop to create parameters
				for(int i = 0; i< bsnDeliveryNoteList.size(); i++) {

					manufacturer = StringUtils.rightPad(bsnDeliveryNoteList.get(i).getManufacturer(), 4, " ");
					parmList[paramCount] = new ProgramParameter(manufacturer.getBytes(Program_Commands_Constants.IBM_273),4);
					paramCount++;

					warehouseNumber = StringUtils.leftPad(bsnDeliveryNoteList.get(i).getWarehouseNumber(), 2, "0");
					parmList[paramCount] = new ProgramParameter(warehouseNumber.getBytes(Program_Commands_Constants.IBM_273),2);
					paramCount++;

					supplierNumber = StringUtils.leftPad(bsnDeliveryNoteList.get(i).getSupplierNumber(), 5, "0");
					parmList[paramCount] = new ProgramParameter(supplierNumber.getBytes(Program_Commands_Constants.IBM_273),5);
					paramCount++;

					deliveryNoteNo = StringUtils.rightPad(bsnDeliveryNoteList.get(i).getDeliveryNoteNo(), 10, " ");
					parmList[paramCount] = new ProgramParameter(deliveryNoteNo.getBytes(Program_Commands_Constants.IBM_273),10);
					paramCount++;
				}
			}else if(isEtAll){
				log.info("ET All processed");
				funktion =  "ZUGALL";
				parmList[0] = new ProgramParameter(returnCode.getBytes(Program_Commands_Constants.IBM_273),3);
				parmList[1] = new ProgramParameter(returnMsg.getBytes(Program_Commands_Constants.IBM_273),5);
				
				if(filterBy != null && filterText != null && (filterBy.equalsIgnoreCase("Lieferscheinnummer") || filterBy.equalsIgnoreCase("PIV")
						|| filterBy.equalsIgnoreCase("Lieferant"))) {
					funktion = "ZUGFIL";
				}
				parmList[2] = new ProgramParameter(funktion.getBytes(Program_Commands_Constants.IBM_273), 5);
				parmList[3] = new ProgramParameter(companyAndAgency.getBytes(Program_Commands_Constants.IBM_273), 4);
				
				/* ---- This change is for filter and warehouse selection on Incoming goods Screen ---*/
				String filterTextLFS = StringUtils.rightPad("%", 12, "%");
				String filterTextPIV = StringUtils.rightPad("%", 12, "%");
				String filterTextLIF = StringUtils.rightPad("%", 7, "%");
				
				if(filterText != null && !filterText.trim().isEmpty() && filterBy.equalsIgnoreCase("Lieferscheinnummer")) {
					filterText = "%"+filterText;
					filterTextLFS = StringUtils.rightPad(filterText, 12, "%");
				} 
				else if(filterText != null && !filterText.trim().isEmpty() && filterBy.equalsIgnoreCase("PIV")){
					filterText = "%"+filterText;
					filterTextPIV = StringUtils.rightPad(filterText, 12, "%");
				}
				else if(filterText != null && !filterText.trim().isEmpty() && filterBy.equalsIgnoreCase("Lieferant")) {
					filterText = "%"+filterText;
					filterTextLIF = StringUtils.rightPad(filterText, 7, "%");
				}
				
				parmList[4] = new ProgramParameter(filterTextLFS.getBytes(Program_Commands_Constants.IBM_273), 4);
				parmList[5] = new ProgramParameter(filterTextPIV.getBytes(Program_Commands_Constants.IBM_273), 4);
				parmList[6] = new ProgramParameter(filterTextLIF.getBytes(Program_Commands_Constants.IBM_273), 4);
				
				if(warehouseNos != null && !warehouseNos.isEmpty()){
					 finalWarehouse = StringUtils.join(warehouseNos, ",");
				}
				
				finalWarehouse = StringUtils.rightPad(StringUtils.remove(finalWarehouse, ","), 300, "0");
				parmList[7] = new ProgramParameter(finalWarehouse.getBytes(Program_Commands_Constants.IBM_273), 4);
				/* -------------- */
				
			} else {
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "BSN TO ETSTAMM"));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "BSN TO ETSTAMM"), exception);
				throw exception;
			}

			List<String> outputList = cobolServiceRepository.executeProgram(parmList, 0, Program_Commands_Constants.DELIVERY_NOTES_FROM_BSN_TO_ETSTAMM_PROGRAM );

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
					ExceptionMessages.DELIVERYNOTE_FROM_BSN_TO_ETSTAMM_FAILED_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.DELIVERYNOTE_FROM_BSN_TO_ETSTAMM_FAILED_MSG_KEY), exception);
			throw exception;
		}
		return programOutput;
	}


	/**
	 * This is method is used to get List of delivery notes based on filter from DB.
	 */
	@Override
	public GlobalSearch getDeliveryNoteListBasedOnFilter(String dataLibrary, String companyId, String agencyId, 
			String allowedWarehouses, String pageSize, String pageNumber, String searchText, String searchBy,
			String sortingBy, String sortingType, List<String> warehouseNos) {

		log.info("Inside getDeliveryNoteListBasedOnFilter method of IncomingGoodsServiceImpl");

		List<DeliveryNotes> deliveryNoteList = new ArrayList<>();
		GlobalSearch globalSearch_for_delivery_notes = new GlobalSearch();

		StringBuilder query = new StringBuilder();

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
			
			//validate the Warehouse Ids
			validateWarehouses(allowedWarehouses);
			
			String orderByClause = getOrderByClause(sortingType, sortingBy, "DeliveryNote");
			
			if(warehouseNos != null && !warehouseNos.isEmpty()){
				 warehouseNos.removeIf(s -> s == null || "".equals(s.trim()));
				allowedWarehouses = StringUtils.join(warehouseNos, ",");
			}
			

			if(searchBy.equalsIgnoreCase("PIV")){
				//SA=53 Incoming Delivery Notes
				query = query.append("select distinct lf.Lieferant, lf.LFSNR, lf.HERST, lf.LNR,piv.PIV,lf.AUFNR_ALT,lf.BESTHINW, ");
				query.append("(Select count(LFSNR) from ").append(dataLibrary).append(".e_lfsall where LFSNR =lf.LFSNR AND AUFNR_ALT = lf.AUFNR_ALT AND BESTHINW= lf.BESTHINW and SA=53 and BSN450 <> 'VVV' and BSN450 <> 'LLL' ");
				query.append(" and LNR in (").append(allowedWarehouses).append(")) AS anzahll, lf.DATUM_LFS, SA, ");
				query.append("(select count(*) from ");
				query.append("(select  distinct lf.Lieferant, lf.LFSNR, lf.HERST, lf.LNR, lf.DATUM_LFS, lf.AUFNR_ALT, lf.BESTHINW  from ").append(dataLibrary).append(".e_lfsall lf ,").append(dataLibrary).append(".E_PIV piv ");
				query.append(" where lf.SA=53 and lf.BSN450 <> 'VVV' and lf.BSN450 <> 'LLL' ");
				query.append(" and lf.LNR in (").append(allowedWarehouses).append(") ");
				query.append(" and piv.LFSNR = lf.LFSNR and piv.HERST = lf.HERST and piv.LNR = lf.LNR and piv.PIV LIKE ").append("UPPER('%"+searchText+"%')").append(" )) AS ROWNUMER from ");
				query.append(dataLibrary).append(".e_lfsall lf ,").append(dataLibrary).append(".E_PIV piv ").append(" where lf.SA=53 and lf.BSN450 <> 'VVV' and lf.BSN450 <> 'LLL' ");
				query.append(" and lf.LNR in (").append(allowedWarehouses).append(") ");
				query.append(" and piv.LFSNR = lf.LFSNR and piv.HERST = lf.HERST and piv.LNR = lf.LNR and piv.PIV LIKE ").append("UPPER('%"+searchText+"%') ").append(orderByClause).append(" OFFSET ");
				query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");

			}else if(searchBy.equalsIgnoreCase("Lieferscheinnummer")){

				//SA=53 Incoming Delivery Notes
				query = query.append("select distinct lf.Lieferant, lf.LFSNR, lf.HERST, lf.LNR,lf.AUFNR_ALT,lf.BESTHINW, ");
				query.append("(Select count(LFSNR) from ").append(dataLibrary).append(".e_lfsall where LFSNR = lf.LFSNR ").append("AND AUFNR_ALT = lf.AUFNR_ALT AND BESTHINW= lf.BESTHINW and SA=53 and BSN450 <> 'VVV' and BSN450 <> 'LLL' ");
				query.append(" and LNR in (").append(allowedWarehouses).append(")) AS anzahll, ");
				query.append("lf.DATUM_LFS, SA, (select distinct PIV from ").append(dataLibrary).append(".E_PIV where LFSNR = lf.LFSNR and HERST = lf.HERST and LNR = lf.LNR").append(" ) as PIV, ");
				query.append("(select count(*) from ");
				query.append("(select  distinct Lieferant, LFSNR, HERST, LNR, DATUM_LFS, AUFNR_ALT, BESTHINW  from ").append(dataLibrary).append(".e_lfsall ");
				query.append(" where SA=53 and BSN450 <> 'VVV' and BSN450 <> 'LLL' ");
				query.append(" and LNR in (").append(allowedWarehouses).append(") ");
				query.append(" and UPPER(LFSNR) LIKE ").append("UPPER('%"+searchText+"%')").append(")) AS ROWNUMER from ");
				query.append(dataLibrary).append(".e_lfsall lf where SA=53 and BSN450 <> 'VVV' and BSN450 <> 'LLL' ");
				query.append(" and LNR in (").append(allowedWarehouses).append(") ");
				query.append(" and  UPPER(LFSNR) LIKE ").append("UPPER('%"+searchText+"%') ").append(orderByClause).append(" OFFSET ");
				query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");

			}else{

				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.SEARCH_BY_FILTER_INVALID_MSG_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.SEARCH_BY_FILTER_INVALID_MSG_KEY));
				throw exception;
			}
			List<DeliveryNotesDTO> deliveryNoteDTOList = new ArrayList<>();
			deliveryNoteList = dbServiceRepository.getResultUsingQuery(DeliveryNotes.class, query.toString(), true);

			//if the list is not empty
			if (deliveryNoteList != null && !deliveryNoteList.isEmpty()) {

				deliveryNoteDTOList = convertDeliveyNoteEntityToDTO(deliveryNoteList, deliveryNoteDTOList);

				globalSearch_for_delivery_notes.setSearchDetailsList(deliveryNoteDTOList);
				globalSearch_for_delivery_notes.setTotalPages(Integer.toString(deliveryNoteList.get(0).getTotalCount()));
				globalSearch_for_delivery_notes.setTotalRecordCnt(Integer.toString(deliveryNoteList.get(0).getTotalCount()));
			}
			else {
				globalSearch_for_delivery_notes.setSearchDetailsList(deliveryNoteDTOList);
				globalSearch_for_delivery_notes.setTotalPages(Integer.toString(0));
				globalSearch_for_delivery_notes.setTotalRecordCnt(Integer.toString(0));
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "All delivery notes based on filter"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "All delivery notes based on filter"), exception);
			throw exception;
		}

		return globalSearch_for_delivery_notes;
	}


	/**
	 * This is method is used to get List of all BSN delivery notes based on filter from DB.
	 */
	@Override
	public GlobalSearch getBSN_DeliveryNoteListBasedOnFilter(String schema,String dataLibrary, String companyId, String agencyId, 
			String allowedWarehouses, String pageSize, String pageNumber, String searchText, String searchBy,
			String sortingBy, String sortingType, List<String> warehouseNos) {

		log.info("Inside getBSN_DeliveryNoteListBasedOnFilter method of IncomingGoodsServiceImpl");


		List<BSNDeliveryNotes> bsnDeliveryNoteList = new ArrayList<>();
		GlobalSearch globalSearch_for_bsn_notes = new GlobalSearch();

		StringBuilder query = new StringBuilder();

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
			
			//validate the Warehouse Ids
			validateWarehouses(allowedWarehouses);
			
			
			String orderByClause = getOrderByClause(sortingType, sortingBy, "BSNDeliveryNote" );
			
			if(warehouseNos != null && !warehouseNos.isEmpty()){
				 warehouseNos.removeIf(s -> s == null || "".equals(s.trim()));
				allowedWarehouses = StringUtils.join(warehouseNos, ",");
			}
			

			if(searchBy.equalsIgnoreCase("PIV")){

				query = query.append("SELECT bsn.HERST, bsn.LNR, bsn.BEST_BENDL, bsn.LIEFNR_GESAMT, bsn.BDAT,piv.PIV, ");
				query.append("(Select count(LIEFNR_GESAMT) from ").append(dataLibrary).append(".E_BSNDAT where LIEFNR_GESAMT = bsn.LIEFNR_GESAMT and STATUS BETWEEN 54 AND 57 and LIEFNR_GESAMT <>'' and ET_FEHLER IN ( 'X' , ' ', '*') ");
				query.append(" AND LNR in (").append(allowedWarehouses).append(") AND ZUMENG <> 0 ");
				query.append(" AND NOT EXISTS (SELECT *  FROM ").append(schema).append(".O_BSNCONF B WHERE B.HERST = HERST and B.LNR = LNR and B.BEST_BENDL = BEST_BENDL and B.LFSNR = LIEFNR_GESAMT ) ) AS anzahll, ");
				query.append(" (select count(*) from ");
				query.append(" (select distinct bsn.HERST, bsn.LNR, bsn.BEST_BENDL, bsn.LIEFNR_GESAMT, bsn.BDAT from ").append(dataLibrary).append(".E_BSNDAT bsn ,").append(dataLibrary).append(".E_PIV piv ");
				query.append(" Where bsn.STATUS BETWEEN 54 AND 57 AND bsn.LIEFNR_GESAMT <>'' and  ET_FEHLER IN ( 'X' , ' ', '*') ");
				query.append(" AND bsn.LNR in (").append(allowedWarehouses).append(") AND bsn.ZUMENG <> 0 ");
				query.append(" AND NOT EXISTS (SELECT *  FROM ").append(schema).append(".O_BSNCONF B WHERE B.HERST = HERST and B.LNR = LNR and B.BEST_BENDL = BEST_BENDL and B.LFSNR = LIEFNR_GESAMT )");
				query.append(" AND piv.LFSNR = bsn.LFSNR and piv.HERST = bsn.HERST and piv.LNR = bsn.LNR AND piv.PIV LIKE ").append("UPPER('%"+searchText+"%')").append(" Group BY bsn.HERST, bsn.LNR, bsn.BEST_BENDL, bsn.LFSNR, bsn.BDAT,piv.PIV))");
				query.append(" as ROWNUMER from ");
				query.append(dataLibrary).append(".E_BSNDAT bsn ,").append(dataLibrary).append(".E_PIV piv ").append(" where bsn.STATUS BETWEEN 54 AND 57 and bsn.LIEFNR_GESAMT <>'' and  ET_FEHLER IN ( 'X' , ' ', '*') ");
				query.append(" AND bsn.LNR in (").append(allowedWarehouses).append(") AND bsn.ZUMENG <> 0");
				query.append(" AND NOT EXISTS (SELECT *  FROM ").append(schema).append(".O_BSNCONF B WHERE B.HERST = HERST and B.LNR = LNR and B.BEST_BENDL = BEST_BENDL and B.LFSNR = LIEFNR_GESAMT ) ");
				query.append(" AND piv.LFSNR = bsn.LFSNR and piv.HERST = bsn.HERST and piv.LNR = bsn.LNR AND piv.PIV LIKE ").append("UPPER('%"+searchText+"%')").append(" Group BY bsn.HERST, bsn.LNR, bsn.BEST_BENDL, bsn.LFSNR, bsn.BDAT,piv.PIV  ").append(orderByClause).append(" OFFSET ");
				query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");

			}else if(searchBy.equalsIgnoreCase("Lieferscheinnummer")){

				query = query.append("SELECT bsn.HERST, bsn.LNR, bsn.BEST_BENDL, bsn.LIEFNR_GESAMT, bsn.BDAT, ");
				query.append("(Select count(LIEFNR_GESAMT) from ").append(dataLibrary).append(".E_BSNDAT where LIEFNR_GESAMT = bsn.LIEFNR_GESAMT and STATUS BETWEEN 54 AND 57 and LIEFNR_GESAMT <>'' and ET_FEHLER IN ( 'X' , ' ', '*') ");
				query.append(" AND LNR in (").append(allowedWarehouses).append(") AND ZUMENG <> 0 ");
				query.append(" AND NOT EXISTS (SELECT *  FROM ").append(schema).append(".O_BSNCONF B WHERE B.HERST = HERST and B.LNR = LNR and B.BEST_BENDL = BEST_BENDL and B.LFSNR = LIEFNR_GESAMT ) ) AS anzahll, ");
				query.append(" (select distinct PIV from ").append(dataLibrary).append(".E_PIV where LFSNR = bsn.LFSNR and HERST = bsn.HERST and LNR = bsn.LNR ").append(" ) as PIV, ");
				query.append(" (select count(*) from ");
				query.append(" (select distinct HERST, LNR, BEST_BENDL, LIEFNR_GESAMT, BDAT from ").append(dataLibrary).append(".E_BSNDAT ");
				query.append(" Where STATUS BETWEEN 54 AND 57 AND LIEFNR_GESAMT <>'' and ET_FEHLER IN ( 'X' , ' ', '*') ");
				query.append(" AND LNR in (").append(allowedWarehouses).append(") AND ZUMENG <> 0 ");
				query.append(" AND NOT EXISTS (SELECT *  FROM ").append(schema).append(".O_BSNCONF B WHERE B.HERST = HERST and B.LNR = LNR and B.BEST_BENDL = BEST_BENDL and B.LFSNR = LIEFNR_GESAMT ) ");
				query.append(" AND UPPER(LIEFNR_GESAMT) LIKE ").append("UPPER('%"+searchText+"%')").append(" Group BY HERST, LNR, BEST_BENDL, LFSNR, BDAT))");
				query.append(" as ROWNUMER from ");
				query.append(dataLibrary).append(".E_BSNDAT bsn where STATUS BETWEEN 54 AND 57 and LIEFNR_GESAMT <>'' and ET_FEHLER IN ( 'X' , ' ', '*') ");
				query.append(" AND LNR in (").append(allowedWarehouses).append(") AND ZUMENG <> 0 ");
				query.append(" AND NOT EXISTS (SELECT *  FROM ").append(schema).append(".O_BSNCONF B WHERE B.HERST = HERST and B.LNR = LNR and B.BEST_BENDL = BEST_BENDL and B.LFSNR = LIEFNR_GESAMT ) ");
				query.append(" AND UPPER(LIEFNR_GESAMT) LIKE ").append("UPPER('%"+searchText+"%')").append(" Group BY HERST, LNR, BEST_BENDL, LFSNR, BDAT ").append(orderByClause).append(" OFFSET ");
				query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");
				
			}else if(searchBy.equalsIgnoreCase("Lieferant")){

				query = query.append("SELECT bsn.HERST, bsn.LNR, bsn.BEST_BENDL, bsn.LIEFNR_GESAMT, bsn.BDAT, ");
				query.append("(Select count(LIEFNR_GESAMT) from ").append(dataLibrary).append(".E_BSNDAT where LIEFNR_GESAMT = bsn.LIEFNR_GESAMT and STATUS BETWEEN 54 AND 57 and LIEFNR_GESAMT <>' ' and ET_FEHLER IN ( 'X' , ' ', '*') ");
				query.append(" AND LNR in (").append(allowedWarehouses).append(") ");
				query.append(" AND NOT EXISTS (SELECT *  FROM ").append(schema).append(".O_BSNCONF B WHERE B.HERST = HERST and B.LNR = LNR and B.BEST_BENDL = BEST_BENDL and B.LFSNR = LIEFNR_GESAMT ) ) AS anzahll, ");
				query.append(" (select distinct PIV from ").append(dataLibrary).append(".E_PIV where LFSNR = bsn.LFSNR and HERST = bsn.HERST and LNR = bsn.LNR ").append(" ) as PIV, ");
				query.append(" (select count(*) from ");
				query.append(" (select distinct HERST, LNR, BEST_BENDL, LIEFNR_GESAMT, BDAT from ").append(dataLibrary).append(".E_BSNDAT ");
				query.append(" Where STATUS  BETWEEN 54 AND 57 AND LIEFNR_GESAMT <>' ' and ET_FEHLER IN ( 'X' , ' ', '*') ");
				query.append(" AND LNR in (").append(allowedWarehouses).append(") ");
				query.append(" AND NOT EXISTS (SELECT *  FROM ").append(schema).append(".O_BSNCONF B WHERE B.HERST = HERST and B.LNR = LNR and B.BEST_BENDL = BEST_BENDL and B.LFSNR = LIEFNR_GESAMT ) ");
				query.append(" AND UPPER(BEST_BENDL) LIKE ").append("UPPER('%"+searchText+"%')").append(" Group BY HERST, LNR, BEST_BENDL, LFSNR, BDAT))");
				query.append(" as ROWNUMER from ");
				query.append(dataLibrary).append(".E_BSNDAT bsn where STATUS BETWEEN 54 AND 57 and LIEFNR_GESAMT <>' ' and ET_FEHLER IN ( 'X' , ' ', '*') ");
				query.append(" AND LNR in (").append(allowedWarehouses).append(") ");
				query.append(" AND NOT EXISTS (SELECT *  FROM ").append(schema).append(".O_BSNCONF B WHERE B.HERST = HERST and B.LNR = LNR and B.BEST_BENDL = BEST_BENDL and B.LFSNR = LIEFNR_GESAMT ) ");
				query.append(" AND UPPER(BEST_BENDL) LIKE ").append("UPPER('%"+searchText+"%')").append(" Group BY HERST, LNR, BEST_BENDL, LFSNR, BDAT ").append(orderByClause).append(" OFFSET ");
				query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");
			}
			else{

				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.SEARCH_BY_FILTER_INVALID_MSG_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.SEARCH_BY_FILTER_INVALID_MSG_KEY));
				throw exception;
			}

			List<BSNDeliveryNotesDTO> bsnDeliveryNoteDTOList = new ArrayList<>();
			bsnDeliveryNoteList = dbServiceRepository.getResultUsingQuery(BSNDeliveryNotes.class, query.toString(), true);

			//if the list is not empty
			if (bsnDeliveryNoteList != null && !bsnDeliveryNoteList.isEmpty()) {

				bsnDeliveryNoteDTOList = convertBSN_DeliveyNoteEntityToDTO(bsnDeliveryNoteList, bsnDeliveryNoteDTOList);

				globalSearch_for_bsn_notes.setSearchDetailsList(bsnDeliveryNoteDTOList);
				globalSearch_for_bsn_notes.setTotalPages(Integer.toString(bsnDeliveryNoteList.get(0).getTotalCount()));
				globalSearch_for_bsn_notes.setTotalRecordCnt(Integer.toString(bsnDeliveryNoteList.get(0).getTotalCount()));
			}
			else {
				globalSearch_for_bsn_notes.setSearchDetailsList(bsnDeliveryNoteDTOList);
				globalSearch_for_bsn_notes.setTotalPages(Integer.toString(0));
				globalSearch_for_bsn_notes.setTotalRecordCnt(Integer.toString(0));
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "All delivery notes based on filter"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "All delivery notes based on filter"), exception);
			throw exception;
		}

		return globalSearch_for_bsn_notes;
	}


	/**
	 * This method is used to delete the Delivery Notes using COBOL program.
	 */
	@Override
	public Map<String, String> deleteDeliveryNotes(List<DeliveryNotesDTO> deliveryNoteList,
			String dataLibrary, String companyId, String agencyId) {
		log.info("Inside deleteDeliveryNotes method of IncomingGoodsServiceImpl");

		ProgramParameter[] parmList;
		Map<String, String> programOutput = new HashMap<String, String>();
		try{

			// Create the input parameter 
			String returnCode = StringUtils.rightPad("00000", 5, " ");
			String returnMsg  = StringUtils.rightPad("", 100, " ");
			String lnr_herst = StringUtils.rightPad("", 120, " ");
			String parameter4 = StringUtils.rightPad("BSNDEL", 6, " ");
			
			companyId = StringUtils.leftPad(companyId, 2, "0");
			agencyId = StringUtils.leftPad(agencyId, 2, "0");
			String companyAndAgency = StringUtils.rightPad(companyId+agencyId, 4, " ");

			if(deliveryNoteList!=null && !deliveryNoteList.isEmpty()) {

				parmList = new ProgramParameter[(deliveryNoteList.size()*7)+8];
				int paramCount = 8;

				parmList = setCommonParameters(parmList, returnCode, returnMsg, lnr_herst, parameter4,companyAndAgency, "", "" , "");

				parmList = prepareParamList(deliveryNoteList, parmList, paramCount);
			}
			else {
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "deliveryNote"));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "deliveryNote"), exception);
				throw exception;
			}

			List<String> outputList = cobolServiceRepository.executeProgram(parmList, 0, Program_Commands_Constants.DELIVERY_NOTES_TO_BSN_PROGRAM );

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
					ExceptionMessages.DELETE_DELIVERYNOTE_FAILED_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.DELETE_DELIVERYNOTE_FAILED_MSG_KEY), exception);
			throw exception;
		}
		return programOutput;
	}
	
	
	/**
	 * This method is used to delete the Announced and Cancelled Delivery Notes using COBOL program.
	 */
	@Override
	public Map<String, String> deleteAnnouncedAndCancelledNotes(List<DeliveryNotesDTO> deliveryNoteList,
			String dataLibrary, String companyId, String agencyId) {
		log.info("Inside deleteAnnouncedAndCancelledNotes method of IncomingGoodsServiceImpl");

		ProgramParameter[] parmList;
		Map<String, String> programOutput = new HashMap<String, String>();
		try{

			// Create the input parameter 
			String returnCode = StringUtils.rightPad("00000", 5, " ");
			String returnMsg  = StringUtils.rightPad("", 100, " ");
			String lnr_herst = StringUtils.rightPad("", 120, " ");
			String parameter4 = StringUtils.rightPad("BSNDEL", 6, " ");
			
			companyId = StringUtils.leftPad(companyId, 2, "0");
			agencyId = StringUtils.leftPad(agencyId, 2, "0");
			String companyAndAgency = StringUtils.rightPad(companyId+agencyId, 4, " ");

			if(deliveryNoteList!=null && !deliveryNoteList.isEmpty()) {

				parmList = new ProgramParameter[(deliveryNoteList.size()*7)+8];
				int paramCount = 8;

				parmList = setCommonParameters(parmList, returnCode, returnMsg, lnr_herst, parameter4, companyAndAgency, "", "", "");

				parmList = prepareParamList(deliveryNoteList, parmList, paramCount);
			}
			else {
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "deliveryNote"));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "deliveryNote"), exception);
				throw exception;
			}

			List<String> outputList = cobolServiceRepository.executeProgram(parmList, 0, Program_Commands_Constants.DELIVERY_NOTES_TO_BSN_PROGRAM );

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
					ExceptionMessages.DELETE_ANN_CANCELED_DLIVRYNTE_FAILED_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.DELETE_ANN_CANCELED_DLIVRYNTE_FAILED_MSG_KEY), exception);
			throw exception;
		}
		return programOutput;
	}
	
	
	/**
	 * This method is used to get the filter value for Delivery Note from Stub
	 * @return List
	 */

	@Override
	public List<DropdownObject> getFilterValuesForDeliveryNote() {

		log.info("Inside getFilterValuesForDeliveryNote method of IncomingGoodsServiceImpl");

		List<DropdownObject> filterValuesForDeliveryNote = new ArrayList<>();
		filterValuesForDeliveryNote = stubServiceRepository.getDDLValuesFromStub(StubServiceRepository.filterValuesForDeliveryNoteMap);
		return filterValuesForDeliveryNote;
	}
	
	
	/**
	 * This is method is used to get List of Positionen mit offenen Konflikten (Conflict delivery notes) from DB.
	 */
	@Override
	public GlobalSearch getConflict_DeliveryNoteList(String schema,String dataLibrary, String companyId, String agencyId, String allowedWarehouses, 
			String pageSize, String pageNumber, String sortingBy, String sortingType, List<String> warehouseNos) {

		log.info("Inside getConflict_DeliveryNoteList method of IncomingGoodsServiceImpl");

		List<BSNDeliveryNotes> conflictDeliveryNoteList = new ArrayList<>();
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
			
			//validate the Warehouse Ids
			validateWarehouses(allowedWarehouses);
			
			String orderByClause = getOrderByClause(sortingType, sortingBy, "ConflictDeliveryNote");
			
			if(warehouseNos != null && !warehouseNos.isEmpty()){
				 warehouseNos.removeIf(s -> s == null || "".equals(s.trim()));
				allowedWarehouses = StringUtils.join(warehouseNos, ",");
			}

			/*StringBuilder query = new StringBuilder("SELECT bsn.HERST, bsn.LNR, bsn.BEST_BENDL, bsn.LIEFNR_GESAMT, bsn.BDAT, ");
			query.append("(Select count(LIEFNR_GESAMT) from ").append(dataLibrary).append(".E_BSNDAT where LIEFNR_GESAMT = bsn.LIEFNR_GESAMT and STATUS > 54 AND STATUS < 90 and LIEFNR_GESAMT <>' ' and ET_FEHLER = 'X' AND DISPOUP <> 1 ) AS anzahll, ");
			query.append(" (select distinct PIV from ").append(dataLibrary).append(".E_PIV where LFSNR = bsn.LIEFNR_GESAMT) as PIV, ");
			query.append(" (select count(*) from ");
			query.append(" (select distinct HERST, LNR, BEST_BENDL, LIEFNR_GESAMT, BDAT from ").append(dataLibrary).append(".E_BSNDAT ");
			query.append(" Where STATUS > 54 AND   STATUS < 90 AND LIEFNR_GESAMT <>' ' AND  ET_FEHLER = 'X' Group BY HERST, LNR, BEST_BENDL, LFSNR, BDAT)) ");
			query.append(" as ROWNUMER from ");
			query.append(dataLibrary).append(".E_BSNDAT bsn where STATUS > 54 AND STATUS < 90 and LIEFNR_GESAMT <>' ' AND  ET_FEHLER = 'X' Group BY HERST, LNR, BEST_BENDL, LFSNR, BDAT ORDER BY bsn.LIEFNR_GESAMT OFFSET ");
			query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");*/
			
			StringBuilder query = new StringBuilder("SELECT bsn.HERST, bsn.LNR, bsn.BEST_BENDL, bsn.LIEFNR_GESAMT, bsn.BDAT, ");
			query.append("(Select count(LIEFNR_GESAMT) from ").append(dataLibrary).append(".E_BSNDAT where LIEFNR_GESAMT = bsn.LIEFNR_GESAMT and STATUS > 54 AND STATUS < 90 and LIEFNR_GESAMT <>' ' and ET_FEHLER = 'X' AND DISPOUP <> 1 ");
			query.append(" AND LNR in (").append(allowedWarehouses).append(") ");
			query.append(" AND EXISTS (SELECT *  FROM ").append(schema).append(".O_BSNCONF B WHERE B.HERST = HERST and B.LNR = LNR and B.BEST_BENDL = BEST_BENDL and B.LFSNR = LIEFNR_GESAMT) ) AS anzahll, ");
			query.append(" (select distinct PIV from ").append(dataLibrary).append(".E_PIV where LFSNR = bsn.LIEFNR_GESAMT) as PIV, ");
			query.append(" (select count(*) from ");
			query.append(" (select distinct HERST, LNR, BEST_BENDL, LIEFNR_GESAMT, BDAT from ").append(dataLibrary).append(".E_BSNDAT ");
			query.append(" Where STATUS > 54 AND   STATUS < 90 AND LIEFNR_GESAMT <>' ' AND  ET_FEHLER = 'X' ");
			query.append(" AND LNR in (").append(allowedWarehouses).append(") ");
			query.append(" AND EXISTS (SELECT *  FROM ").append(schema).append(".O_BSNCONF B WHERE B.HERST = HERST and B.LNR = LNR and B.BEST_BENDL = BEST_BENDL and B.LFSNR = LIEFNR_GESAMT) Group BY HERST, LNR, BEST_BENDL, LFSNR, BDAT)) ");
			query.append(" as ROWNUMER from ");
			query.append(dataLibrary).append(".E_BSNDAT bsn where STATUS > 54 AND STATUS < 90 and LIEFNR_GESAMT <>' ' AND  ET_FEHLER = 'X' ");
			query.append(" AND bsn.LNR in (").append(allowedWarehouses).append(") ");
			query.append(" AND EXISTS (SELECT *  FROM ").append(schema).append(".O_BSNCONF B WHERE B.HERST = bsn.HERST and B.LNR = bsn.LNR and B.BEST_BENDL = bsn.BEST_BENDL and B.LFSNR = bsn.LIEFNR_GESAMT) Group BY HERST, LNR, BEST_BENDL, LFSNR, BDAT ");
			query.append(orderByClause).append(" OFFSET ");
			query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");

			List<BSNDeliveryNotesDTO> conflictDeliveryNoteDTOList = new ArrayList<>();
			conflictDeliveryNoteList = dbServiceRepository.getResultUsingQuery(BSNDeliveryNotes.class, query.toString(), true);

			//if the list is not empty
			if (conflictDeliveryNoteList != null && !conflictDeliveryNoteList.isEmpty()) {

				conflictDeliveryNoteDTOList = convertBSN_DeliveyNoteEntityToDTO(conflictDeliveryNoteList, conflictDeliveryNoteDTOList);

				globalSearchList.setSearchDetailsList(conflictDeliveryNoteDTOList);
				globalSearchList.setTotalPages(Integer.toString(conflictDeliveryNoteList.get(0).getTotalCount()));
				globalSearchList.setTotalRecordCnt(Integer.toString(conflictDeliveryNoteList.get(0).getTotalCount()));
			}
			else {
				globalSearchList.setSearchDetailsList(conflictDeliveryNoteDTOList);
				globalSearchList.setTotalPages(Integer.toString(0));
				globalSearchList.setTotalRecordCnt(Integer.toString(0));
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Lieferschein mit Konflikten (Conflict delivery notes)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Lieferschein mit Konflikten (Conflict delivery notes)"), exception);
			throw exception;
		}

		return globalSearchList;
	}


	/**
	 * This is method is used to get List of Lieferschein mit Konflikten details (Conflict delivery notes Details) from DB.
	 */
	@Override
	public List<BSNDeliveryNotesDetailDTO> getConflict_DeliveryNoteDetailsList(String deliveryNoteNo, String dataLibrary, String schema, String companyId, String agencyId, 
			String supplierNumber, String manufacturer, String warehouseNumber, String date ) {

		log.info("Inside getConflict_DeliveryNoteDetailsList method of IncomingGoodsServiceImpl");

		List<BSNDeliveryNotesDetail> conflictDeliveryNoteDetailList = new ArrayList<>();
		List<BSNDeliveryNotesDetailDTO> conflcitDeliveryNoteDetailDTOList = new ArrayList<>();

		try {

			if(manufacturer != null && manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING)) {
				manufacturer = RestInputConstants.DCAG_STRING;
			}
			
			if (date != null && !date.isEmpty() && checkDateFormat(date, "dd/MM/yyyy")) {
				StringBuilder dateBuilder = new StringBuilder();
				String dateValues[] = date.split("/");
				dateBuilder.append(dateValues[2]).append(StringUtils.leftPad(dateValues[1], 2, "0"));
				dateBuilder.append(StringUtils.leftPad(dateValues[0], 2, "0"));
				date = dateBuilder.toString();
			} 

			/*StringBuilder query = new StringBuilder("Select bsn.PSERV_AUFNR, bsn.SERV_AUFNRX, bsn.BESTPOS, bsn.BEST_ART, bsn.BEST_AP, bsn.ETNR, bsn.BEN, bsn.BEMENG, bsn.ZUMENG, ");
			query.append("(ZUMENG*EKNPR) as GelieferterWert, (BEMENG*EKNPR) as BestellterWert, bsn.GLMENG, bsn.BVER, bsn.DISPOPOS, bsn.DISPOUP, ");
			query.append("(select count(*) from ").append(schema).append(".O_BSNCONF WHERE ETNR = bsn.ETNR and BEST_ART = bsn.BEST_ART and BEST_AP = bsn.BEST_AP ");
			query.append(" and HERST='").append(manufacturer).append("'");
			query.append(" and LNR='").append(warehouseNumber).append("' and SOLVED=' ' and LFSNR='").append(deliveryNoteNo).append("') as conflict, ");
			query.append("(select LISTAGG(TRIM(HINT), '/ ') from ").append(schema).append(".O_BSNCONF WHERE ETNR = bsn.ETNR and BEST_ART = bsn.BEST_ART and BEST_AP = bsn.BEST_AP ");
			query.append(" and HERST='").append(manufacturer).append("'");
			query.append(" and LNR='").append(warehouseNumber).append("' and SOLVED=' ' and LFSNR='").append(deliveryNoteNo).append("') as conflict_ERR,");
			query.append("(select LISTAGG(TRIM(ERROR_CODE), '/ ') from ").append(schema).append(".O_BSNCONF WHERE ETNR = bsn.ETNR and BEST_ART = bsn.BEST_ART and BEST_AP = bsn.BEST_AP ");
			query.append(" and HERST='").append(manufacturer).append("'");
			query.append(" and LNR='").append(warehouseNumber).append("' and SOLVED=' ' and LFSNR='").append(deliveryNoteNo).append("') as ERROR_CODE  from ");
			query.append(dataLibrary).append(".E_BSNDAT bsn WHERE bsn.LIEFNR_GESAMT='").append(deliveryNoteNo).append("'");
			query.append(" and bsn.HERST='").append(manufacturer).append("'");
			query.append(" and bsn.LNR='").append(warehouseNumber).append("'");
			query.append(" and bsn.BEST_BENDL='").append(supplierNumber);
			query.append("' and bsn.BDAT='").append(date).append("' and bsn.ET_FEHLER = 'X' GROUP BY ");
			query.append("bsn.PSERV_AUFNR, bsn.SERV_AUFNRX, bsn.BESTPOS, bsn.DISPOPOS, bsn.DISPOUP, bsn.LIEFNR_GESAMT, bsn.BEST_ART, bsn.BEST_AP, bsn.ETNR, bsn.BEN, bsn.BEMENG, bsn.ZUMENG, bsn.GLMENG, bsn.EKNPR, bsn.BVER ");
			query.append("ORDER by bsn.BESTPOS ASC");*/
			
			
			StringBuilder query = new StringBuilder("Select bsn.PSERV_AUFNR, bsn.SERV_AUFNRX, bsn.BESTPOS, bsn.BEST_ART, bsn.BEST_AP, bsn.ETNR, bsn.BEN, bsn.BEMENG, bsn.ZUMENG, ");
			query.append("(bsn.ZUMENG*bsn.EKNPR) as GelieferterWert, (bsn.BEMENG*bsn.EKNPR) as BestellterWert,bsn.GLMENG, bsn.BVER, bsn.DISPOPOS, bsn.DISPOUP, ");
			query.append(" (select etnr from ");
			query.append(dataLibrary).append(".E_BSNDAT WHERE LIEFNR_GESAMT='").append(deliveryNoteNo).append("'");
			query.append(" and HERST='").append(manufacturer).append("'");
			query.append(" and LNR='").append(warehouseNumber).append("'");
			query.append(" and BEST_BENDL='").append(supplierNumber);
			query.append("' and BDAT='").append(date).append("' and ET_FEHLER = 'X' and DISPOUP = 1 and dispopos = bsn.dispopos and best_art = bsn.best_art  and best_ap = bsn.best_ap ) as deliveredPartNo , ");
			
			query.append(" (select DISPOUP from ");
			query.append(dataLibrary).append(".E_BSNDAT WHERE LIEFNR_GESAMT='").append(deliveryNoteNo).append("'");
			query.append(" and HERST='").append(manufacturer).append("'");
			query.append(" and LNR='").append(warehouseNumber).append("'");
			query.append(" and BEST_BENDL='").append(supplierNumber);
			query.append("' and BDAT='").append(date).append("' and ET_FEHLER = 'X' and DISPOUP = 1 and dispopos = bsn.dispopos and best_art = bsn.best_art  and best_ap = bsn.best_ap ) as deliveredDispoup , ");
			
			query.append(" (select BEN from ");
			query.append(dataLibrary).append(".E_BSNDAT WHERE LIEFNR_GESAMT='").append(deliveryNoteNo).append("'");
			query.append(" and HERST='").append(manufacturer).append("'");
			query.append(" and LNR='").append(warehouseNumber).append("'");
			query.append(" and BEST_BENDL='").append(supplierNumber);
			query.append("' and BDAT='").append(date).append("' and ET_FEHLER = 'X' and DISPOUP = 1 and dispopos = bsn.dispopos and best_art = bsn.best_art  and best_ap = bsn.best_ap ) as deliveredPartName , ");
			
			query.append(" (select ZUMENG*EKNPR from ");
			query.append(dataLibrary).append(".E_BSNDAT WHERE LIEFNR_GESAMT='").append(deliveryNoteNo).append("'");
			query.append(" and HERST='").append(manufacturer).append("'");
			query.append(" and LNR='").append(warehouseNumber).append("'");
			query.append(" and BEST_BENDL='").append(supplierNumber);
			query.append("' and BDAT='").append(date).append("' and ET_FEHLER = 'X' and DISPOUP = 1 and dispopos = bsn.dispopos  and best_art = bsn.best_art  and best_ap = bsn.best_ap ) as deliveredGelieferterWert , ");
			
			query.append(" (select Zumeng from ");
			query.append(dataLibrary).append(".E_BSNDAT WHERE LIEFNR_GESAMT='").append(deliveryNoteNo).append("'");
			query.append(" and HERST='").append(manufacturer).append("'");
			query.append(" and LNR='").append(warehouseNumber).append("'");
			query.append(" and BEST_BENDL='").append(supplierNumber);
			query.append("' and BDAT='").append(date).append("' and ET_FEHLER = 'X' and DISPOUP = 1 and dispopos = bsn.dispopos and best_art = bsn.best_art  and best_ap = bsn.best_ap ) as deliveredPartQuantity from ");
			
			query.append(dataLibrary).append(".E_BSNDAT bsn WHERE bsn.LIEFNR_GESAMT='").append(deliveryNoteNo).append("'");
			query.append(" and bsn.HERST='").append(manufacturer).append("'");
			query.append(" and bsn.LNR='").append(warehouseNumber).append("'");
			query.append(" and bsn.BEST_BENDL='").append(supplierNumber);
			query.append("' and bsn.BDAT='").append(date).append("' and bsn.ET_FEHLER = 'X' and bsn.DISPOUP <> 1 GROUP BY ");
			query.append("PSERV_AUFNR, SERV_AUFNRX, BESTPOS, DISPOPOS, DISPOUP, LIEFNR_GESAMT, BEST_ART, BEST_AP, ETNR, BEN, BEMENG, ZUMENG, GLMENG, EKNPR, BVER ");
			query.append("ORDER by BESTPOS ASC");
			
			

			conflictDeliveryNoteDetailList = dbServiceRepository.getResultUsingQuery(BSNDeliveryNotesDetail.class, query.toString(), true);

			//if the list is not empty
			if (conflictDeliveryNoteDetailList != null && !conflictDeliveryNoteDetailList.isEmpty()) {

				conflcitDeliveryNoteDetailDTOList = convertBSN_DeliveyDetailsEntityToDTO(conflictDeliveryNoteDetailList, conflcitDeliveryNoteDetailDTOList, 
						deliveryNoteNo, supplierNumber, manufacturer, warehouseNumber,schema,true);
			}
			else{
				log.info("Conflict delivery notes Details List is empty");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Lieferschein mit Konflikten details (Conflict delivery notes details)"));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Lieferschein mit Konflikten details (Conflict delivery notes details)"));
				throw exception;
			}
		}
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Lieferschein mit Konflikten details (Conflict delivery notes details)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Lieferschein mit Konflikten details (Conflict delivery notes details)"), exception);
			throw exception;
		}

		return conflcitDeliveryNoteDetailDTOList;
	}
	
	/**
	 * This is method is used to create history for delivery note 
	 */
	@Override
	public Map<String, Boolean> createDeliveryNoteHistory(String dataLibrary, String schema,String userName, String wsid) {

		log.info("Inside createDeliveryNoteHistory method of IncomingGoodsServiceImpl");
		
		boolean deliveryNoteHistoryCreated = false;
		Map<String, Boolean> outputMap = new HashMap<>();
		outputMap.put("isHistoryCreated", deliveryNoteHistoryCreated);
		List<DeliveryNotesDetail> deliveryNoteDetailList = new ArrayList<>();
		//List<DeliveryNotesDetailDTO> deliveryNoteDetailDTOList = new ArrayList<>();
		
		try {
			
			StringBuilder query = new StringBuilder("SELECT PO.ID, PO.PURCHASEORDERNUMBER, LFALL.BESTHINW, LFALL.LFSNR, LFALL.SA, LFALL.BEN, LFALL.MENGE_BEST, LFALL.MENGE_GEL, LFALL.POS_BEST, LFALL.LNR , BSN.ETNR, LFALL.HERST, LFALL.LFSMARKE,LFALL.Lieferant, LFALL.AUFNR_ALT  FROM ");
			query.append(dataLibrary).append(".E_LFSALL LFALL ,").append(schema).append(".O_PRORD PO ,").append(dataLibrary).append(".E_BSNDAT BSN  WHERE ");
			query.append("LFALL.AUFNR_ALT = PO.PURCHASEORDERNUMBER AND BSN.B_AUFN_ALT = PO.PURCHASEORDERNUMBER AND  LFALL.BSN450 = 'RRR' AND LFALL.BSN450WSID = ").append("'"+wsid+"'");

			deliveryNoteDetailList = dbServiceRepository.getResultUsingQuery(DeliveryNotesDetail.class, query.toString(), true);
			
			//if the list is not empty
			if (deliveryNoteDetailList != null && !deliveryNoteDetailList.isEmpty()) {

				for(DeliveryNotesDetail deliveryNotesDetail : deliveryNoteDetailList){
					
					StringBuilder insert_delivery = new StringBuilder(" INSERT INTO ").append(schema).append(".O_DNEL");
					insert_delivery.append("( PurchaseOrderId, PurchaseOrderHint, DeliveryNoteNumber, DeliveryNoteType, PartNumber, PartName, OrderedAmount, DeliveredAmount,PurchasePrice, SellingPrice, DeliveryNotePosition,PartCode, LOGIN, Timestamp ) VALUES (");
					insert_delivery.append(deliveryNotesDetail.getPurchaseOrderId()+",");
					insert_delivery.append("'"+deliveryNotesDetail.getNote()+"',");
					insert_delivery.append("'"+deliveryNotesDetail.getDeliveryNoteNo()+"',");
					insert_delivery.append(deliveryNotesDetail.getSa()+",");
					insert_delivery.append("'"+deliveryNotesDetail.getPartNumber()+"',");
					insert_delivery.append("'"+deliveryNotesDetail.getPartName()+"',");
					insert_delivery.append(deliveryNotesDetail.getOrderedQuantity()+",");
					insert_delivery.append(deliveryNotesDetail.getDeliveredQuantity()+",");
					insert_delivery.append(deliveryNotesDetail.getOrderPosition()+",");
					insert_delivery.append("0,");
					insert_delivery.append("0,");
					insert_delivery.append("'',");
					insert_delivery.append("'"+userName+"',").append(" CURRENT TIMESTAMP )");;
					
					//insert in O_Order
					int deliveryNoteId = dbServiceRepository.insertResultUsingQuery(insert_delivery.toString());
					log.info("Insert Successfully");
					
					StringBuilder update_delivery = new StringBuilder(" UPDATE ").append(dataLibrary).append(".E_LFSALL SET BSN450 = 'VVV' WHERE ");
					update_delivery.append("HERST = ").append("'"+deliveryNotesDetail.getManufacturer()+"'");
					update_delivery.append("AND LFSMARKE = ").append("'"+deliveryNotesDetail.getLfsMarketingCode()+"'");
					update_delivery.append("AND LNR = ").append("'"+deliveryNotesDetail.getWarehouseNumber()+"'");
					update_delivery.append("AND Lieferant = ").append("'"+deliveryNotesDetail.getSupplierNumber()+"'");
					update_delivery.append("AND SA = ").append("'"+deliveryNotesDetail.getSa()+"'");
					update_delivery.append("AND LFSNR = ").append("'"+deliveryNotesDetail.getDeliveryNoteNo()+"'");
					update_delivery.append("AND AUFNR_ALT = ").append("'"+deliveryNotesDetail.getOrderNumber()+"'");
					
					boolean updateFlag = dbServiceRepository.updateResultUsingQuery(update_delivery.toString());
					
					if(updateFlag){
						log.info("Update Successfully");
					}
					
				}
				deliveryNoteHistoryCreated = true;
			}
			
			outputMap.put("isHistoryCreated", deliveryNoteHistoryCreated);

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.CREATE_FAILED_MSG_KEY, "Create Delivery Note history"));
			log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY, "Create Delivery Note history"), exception);
			throw exception;
		}

		return outputMap;
	}
	
	/**
	 * This is method is used to get konfliktbehebung details (Conflict Resolution Details) from DB.
	 */
	@Override
	public List<ConflictResolutionDTO> getConflictResolution(String dataLibrary, String schema, String companyId, String agencyId, 
			String supplierNumber, String manufacturer, String warehouseNumber, String deliveryNoteNo, String partNo, 
			String orderNumber, String dispopos, String dispoup, String conflict_Codes) {

		log.info("Inside getConflictResolution method of IncomingGoodsServiceImpl");

		List<ConflictResolutionDTO> conflictResolutionDTOList = new ArrayList<>();
		
		String partPrice = "";
		String partBrand = "";
		String partMarketingCode = "";
		String partDiscountGroup = "";
		String partLabel = "";
		String partName = "";
		String partStorageLocation = "";
		
		String deliveryNotePrice = "";
		String deliveryNoteBrand = "";
		String deliveryNoteMarketingCode = "";
		String deliveryNoteDiscountGroup = "";
		String deliveryNotePartLabel = "";
		String deliveryNoteName = "";
		String deliveryNoteStorageLocation = "";
		

		try {

			if(isValidOrderNo(orderNumber)){
				log.info("Order number is not valid");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.INVALID_MSG_KEY, "Order Number (Bestellnummer) "));
				log.error(messageService.getReadableMessage(ExceptionMessages.INVALID_MSG_KEY, "Order Number (Bestellnummer) "));
				throw exception;
			}

			String bestArt = orderNumber.substring(0,2);
			String bestAp = orderNumber.substring(2);
			log.info("bestArt :  {} and bestAp :  {}", bestArt, bestAp);
			
			if(manufacturer != null && manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING)) {
				manufacturer = RestInputConstants.DCAG_STRING;
			}
			//get details from part master
			StringBuilder query_partMaster = new StringBuilder("Select EPR, TMARKE, MC, RG, LABEL, BENEN , LOPA FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE ");
			query_partMaster.append("HERST = ").append("'"+manufacturer+"'");
			query_partMaster.append(" AND LNR = ").append(warehouseNumber);
			query_partMaster.append(" AND TNR = ").append("'"+partNo+"'");
			
			List<PartDetails> partDetails = dbServiceRepository.getResultUsingQuery(PartDetails.class, query_partMaster.toString(), true);
			
			if (partDetails != null && !partDetails.isEmpty()) {
				
				partPrice = String.valueOf(decimalformat_twodigit.format(partDetails.get(0).getPurchasePrice()));
				partBrand = partDetails.get(0).getOemBrand();
				partMarketingCode = partDetails.get(0).getMarketingCode();
				partDiscountGroup = partDetails.get(0).getDiscountGroup();
				partLabel = partDetails.get(0).getPartLabel();
				partName = partDetails.get(0).getName();
				partStorageLocation = partDetails.get(0).getStorageLocation();		
			}
			
			//get details from  (E_BSNDAT)
			
			StringBuilder query_deliveryNote = new StringBuilder("Select EPREIS, MARKE, MCODE, RABGR, ETLABEL, BEN, LORT_40,LORT_ET FROM ").append(dataLibrary).append(".E_BSNDAT WHERE ");
			query_deliveryNote.append("LIEFNR_GESAMT='").append(deliveryNoteNo).append("'");
			query_deliveryNote.append(" AND HERST='").append(manufacturer).append("'");
			query_deliveryNote.append(" AND LNR=").append(warehouseNumber);
			query_deliveryNote.append(" AND BEST_BENDL=").append(supplierNumber);
			query_deliveryNote.append(" AND ETNR='").append(partNo).append("'");
			query_deliveryNote.append(" AND BEST_ART=").append(bestArt);
			query_deliveryNote.append(" AND BEST_AP=").append(bestAp);
			query_deliveryNote.append(" AND DISPOPOS=").append(dispopos);
			query_deliveryNote.append(" AND DISPOUP=").append(dispoup);
			
			List<OrdersObjectDetails> deliveryNoteDetails = dbServiceRepository.getResultUsingQuery(OrdersObjectDetails.class, query_deliveryNote.toString(), true);
			
			if (deliveryNoteDetails != null && !deliveryNoteDetails.isEmpty()) {
				
				deliveryNotePrice = String.valueOf(deliveryNoteDetails.get(0).getPart_Price());
				deliveryNoteBrand = deliveryNoteDetails.get(0).getPart_Brand();
				deliveryNoteMarketingCode = deliveryNoteDetails.get(0).getPart_MarketingCode();
				deliveryNoteDiscountGroup = deliveryNoteDetails.get(0).getPart_DiscountGroup();
				deliveryNotePartLabel = deliveryNoteDetails.get(0).getPart_Label();
				deliveryNoteName = deliveryNoteDetails.get(0).getPartDescription();
				deliveryNoteStorageLocation = deliveryNoteDetails.get(0).getStorageLocation_1()+deliveryNoteDetails.get(0).getStorageLocation_2();
			}
			
			List<String> errorCodes = Arrays.asList(conflict_Codes.split("/"));

			if (errorCodes != null && !errorCodes.isEmpty()) {
				for (String codes : errorCodes) {

					if (codes.trim().equalsIgnoreCase("P0001")) {
						ConflictResolutionDTO conflict_DeliveryNotesDTO = convertEntityToConflictResolutionDTO(
								supplierNumber, manufacturer, warehouseNumber, deliveryNoteNo, partNo, orderNumber,
								dispopos, dispoup, partPrice, deliveryNotePrice, "P0001");

						conflictResolutionDTOList.add(conflict_DeliveryNotesDTO);
					}else if (codes.trim().equalsIgnoreCase("M0001")) {
						ConflictResolutionDTO conflict_DeliveryNotesDTO = convertEntityToConflictResolutionDTO(
								supplierNumber, manufacturer, warehouseNumber, deliveryNoteNo, partNo, orderNumber,
								dispopos, dispoup, partBrand, deliveryNoteBrand, "M0001");

						conflictResolutionDTOList.add(conflict_DeliveryNotesDTO);
					}else if (codes.trim().equalsIgnoreCase("MC001")) {
						ConflictResolutionDTO conflict_DeliveryNotesDTO = convertEntityToConflictResolutionDTO(
								supplierNumber, manufacturer, warehouseNumber, deliveryNoteNo, partNo, orderNumber,
								dispopos, dispoup, partMarketingCode, deliveryNoteMarketingCode, "MC001");

						conflictResolutionDTOList.add(conflict_DeliveryNotesDTO);
					}else if (codes.trim().equalsIgnoreCase("R0001")) {
						ConflictResolutionDTO conflict_DeliveryNotesDTO = convertEntityToConflictResolutionDTO(
								supplierNumber, manufacturer, warehouseNumber, deliveryNoteNo, partNo, orderNumber,
								dispopos, dispoup, partDiscountGroup, deliveryNoteDiscountGroup, "R0001");

						conflictResolutionDTOList.add(conflict_DeliveryNotesDTO);
					}else if (codes.trim().equalsIgnoreCase("L0001")) {
						ConflictResolutionDTO conflict_DeliveryNotesDTO = convertEntityToConflictResolutionDTO(
								supplierNumber, manufacturer, warehouseNumber, deliveryNoteNo, partNo, orderNumber,
								dispopos, dispoup, partLabel, deliveryNotePartLabel, "L0001");

						conflictResolutionDTOList.add(conflict_DeliveryNotesDTO);
					}else if (codes.trim().equalsIgnoreCase("LAOR1")) {
						ConflictResolutionDTO conflict_DeliveryNotesDTO = convertEntityToConflictResolutionDTO(
								supplierNumber, manufacturer, warehouseNumber, deliveryNoteNo, partNo, orderNumber,
								dispopos, dispoup, partStorageLocation, deliveryNoteStorageLocation, "LAOR1");

						conflictResolutionDTOList.add(conflict_DeliveryNotesDTO);
					}else if (codes.trim().equalsIgnoreCase("B0001")) {
						ConflictResolutionDTO conflict_DeliveryNotesDTO = convertEntityToConflictResolutionDTO(
								supplierNumber, manufacturer, warehouseNumber, deliveryNoteNo, partNo, orderNumber,
								dispopos, dispoup, partName, deliveryNoteName, "B0001");

						conflictResolutionDTOList.add(conflict_DeliveryNotesDTO);
					}else{
							AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
									ExceptionMessages.INVALID_MSG_KEY, " Conflict Code"));
							log.error(messageService.getReadableMessage(ExceptionMessages.INVALID_MSG_KEY, " Conflict Code"), exception);
							throw exception;
						
					}
				}
			}
			
		}catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Lieferschein mit Konflikten details (Conflict delivery notes details)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Lieferschein mit Konflikten details (Conflict delivery notes details)"), exception);
			throw exception;
		}

		return conflictResolutionDTOList;
	}
	
	private ConflictResolutionDTO convertEntityToConflictResolutionDTO(String supplierNumber,
			String manufacturer, String warehouseNumber, String deliveryNoteNo, String partNo, String orderNumber,
			String dispopos, String dispoup, String partPrice, String deliveryNotePrice, String conflict_Code) {
		
		ConflictResolutionDTO conflict_DeliveryNotesDTO = new ConflictResolutionDTO();
		
		conflict_DeliveryNotesDTO.setConflict_Code(conflict_Code);
		conflict_DeliveryNotesDTO.setValueFrom_PartMaster(partPrice);
		conflict_DeliveryNotesDTO.setValueFrom_DeliveryNote(deliveryNotePrice);
		conflict_DeliveryNotesDTO.setDeliveryNoteNo(deliveryNoteNo);
		conflict_DeliveryNotesDTO.setDispopos(dispopos);
		conflict_DeliveryNotesDTO.setDispoup(dispoup);
		conflict_DeliveryNotesDTO.setManufacturer(manufacturer);
		conflict_DeliveryNotesDTO.setOrderedPartNo(partNo);
		conflict_DeliveryNotesDTO.setOrderNumber(orderNumber);
		conflict_DeliveryNotesDTO.setSupplierNumber(supplierNumber);
		conflict_DeliveryNotesDTO.setWarehouseNumber(warehouseNumber);
		
		return conflict_DeliveryNotesDTO;
	}


	private boolean isValidOrderNo(String orderNo) {

		boolean flag = true;
		if (orderNo != null && orderNo.trim().length() == 6 && !orderNo.substring(0, 2).equalsIgnoreCase("00")
				&& !orderNo.substring(2).equalsIgnoreCase("0000")) {
			try {
				Integer.parseInt(orderNo);
				flag = false;

			} catch (Exception e) {
				flag = true;
			}

		}
		return flag;
	}


	/**
	 * This is method is used to update Conflict Resolution in deliveryNote / partMaster (Teilestamm ).
	 */
	@Override
	public Map<String, String> updateConflictResolution(List<ConflictResolutionDTO> conflictResolutionList, String dataLibrary, String schema, String companyId, 
			String agencyId, String userName, String wsid) {

		log.info("Inside updateConflictResolution method of IncomingGoodsServiceImpl");

		Map<String, String> programOutput = new HashMap<String, String>();
		ProgramParameter[] parmList;
		
		//validate the company Id 
		validateCompany(companyId);
		
		//validate the agency Id 
		validateAgency(agencyId);

		String conflictErrorCode = "";
		String selectedType = "";
		String selectedValue = "";
		String funktion =  "ETCONF";
		int paramCount = 14;

		// Create the input parameter 
		String returnCode = StringUtils.rightPad("", 5, " ");
		String returnMsg  = StringUtils.rightPad("", 100, " ");
		String userId  = StringUtils.rightPad(userName, 10, " ");
		wsid  = StringUtils.rightPad(wsid, 2, " ");
		try{
			
			if(conflictResolutionList!=null && !conflictResolutionList.isEmpty()) {

				parmList = new ProgramParameter[(conflictResolutionList.size()*3)+14];
				
				parmList[0] = new ProgramParameter(returnCode.getBytes(Program_Commands_Constants.IBM_273),5);
				parmList[1] = new ProgramParameter(returnMsg.getBytes(Program_Commands_Constants.IBM_273),100);
				parmList[2] = new ProgramParameter(funktion.getBytes(Program_Commands_Constants.IBM_273),5);
				parmList[3] = new ProgramParameter(userId.getBytes(Program_Commands_Constants.IBM_273),10);
				parmList[4] = new ProgramParameter(wsid.getBytes(Program_Commands_Constants.IBM_273),2);
				
				String manufacturer  = conflictResolutionList.get(0).getManufacturer();
				if(manufacturer != null && manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING)) {
					manufacturer = RestInputConstants.DCAG_STRING;
				}
				manufacturer = StringUtils.rightPad(manufacturer, 4, " ");
				parmList[5] = new ProgramParameter(manufacturer.getBytes(Program_Commands_Constants.IBM_273), 4);
				
				String warehouseNumber = StringUtils.leftPad(conflictResolutionList.get(0).getWarehouseNumber(), 2, "0");
				String compID =  StringUtils.leftPad(companyId, 2, "0");
				String agencyID = StringUtils.leftPad(agencyId, 2, "0");
				
				String locationId = StringUtils.rightPad(compID+agencyID+warehouseNumber, 10, " ");
				parmList[6] = new ProgramParameter(locationId.getBytes(Program_Commands_Constants.IBM_273), 2);
				
				if(isValidOrderNo(conflictResolutionList.get(0).getOrderNumber())){
					log.info("Order number is not valid");
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.INVALID_MSG_KEY, "Order Number (Bestellnummer) "));
					log.error(messageService.getReadableMessage(ExceptionMessages.INVALID_MSG_KEY, "Order Number (Bestellnummer) "));
					throw exception;
				}

				String bestArt = conflictResolutionList.get(0).getOrderNumber().substring(0,2);
				String bestAp = conflictResolutionList.get(0).getOrderNumber().substring(2);
				log.info("bestArt :  {} and bestAp :  {}", bestArt, bestAp);
				
				bestArt = StringUtils.leftPad(bestArt, 2, "0");
				parmList[7] = new ProgramParameter(bestArt.getBytes(Program_Commands_Constants.IBM_273), 2);
				
				bestAp = StringUtils.leftPad(bestAp, 4, "0");
				parmList[8] = new ProgramParameter(bestAp.getBytes(Program_Commands_Constants.IBM_273), 4);
				
				String supplierNo = StringUtils.leftPad(conflictResolutionList.get(0).getSupplierNumber(), 5, "0");
				parmList[9] = new ProgramParameter(supplierNo.getBytes(Program_Commands_Constants.IBM_273), 5);
				
				String disposition = StringUtils.leftPad(conflictResolutionList.get(0).getDispopos(), 4, "0");
				parmList[10] = new ProgramParameter(disposition.getBytes(Program_Commands_Constants.IBM_273), 4);
				
				String repalcementInd  = StringUtils.leftPad(conflictResolutionList.get(0).getDispoup(), 2, "0");
				parmList[11] = new ProgramParameter(repalcementInd.getBytes(Program_Commands_Constants.IBM_273), 2);
				
				String partNumber  = StringUtils.rightPad(conflictResolutionList.get(0).getOrderedPartNo(), 19, " ");
				parmList[12] = new ProgramParameter(partNumber.getBytes(Program_Commands_Constants.IBM_273), 19);
				
				String deliveryNoteNo  = StringUtils.rightPad(conflictResolutionList.get(0).getDeliveryNoteNo(), 10, " ");
				parmList[13] = new ProgramParameter(deliveryNoteNo.getBytes(Program_Commands_Constants.IBM_273), 10);

				//iterate loop to create parameters
				for(int i = 0; i< conflictResolutionList.size(); i++) {

					conflictErrorCode = StringUtils.rightPad(conflictResolutionList.get(i).getConflict_Code(), 5, " ");
					parmList[paramCount] = new ProgramParameter(conflictErrorCode.getBytes(Program_Commands_Constants.IBM_273),5);
					paramCount++;

					selectedType = StringUtils.rightPad(conflictResolutionList.get(i).getSelectedType(), 3, " ");
					parmList[paramCount] = new ProgramParameter(selectedType.getBytes(Program_Commands_Constants.IBM_273),3);
					paramCount++;

					selectedValue = StringUtils.rightPad(conflictResolutionList.get(i).getSelectedValue(), 25, " ");
					parmList[paramCount] = new ProgramParameter(selectedValue.getBytes(Program_Commands_Constants.IBM_273),5);
					paramCount++;

					
				}
			}else {
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Conflict Resolution "));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Conflict Resolution"), exception);
				throw exception;
			}

			List<String> outputList = cobolServiceRepository.executeProgram(parmList, 0, Program_Commands_Constants.CONFLICT_RESOLUTION );

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
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Conflict Resolution"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Conflict Resolution"), exception);
			throw exception;
		}
	
		return programOutput;
		
	}
	
	
	/**
	 * This is method is used to get List of Positionen mit behobenen Konflikten (Solved Conflict delivery notes) from DB.
	 */
	@Override
	public GlobalSearch getSolvedConflicts_DeliveryNoteList(String dataLibrary, String schema, String companyId, String agencyId, String allowedWarehouses, 
			String pageSize, String pageNumber,String sortingBy, String sortingType, List<String> warehouseNos) {

		log.info("Inside getSolvedConflicts_DeliveryNoteList method of IncomingGoodsServiceImpl");

		List<BSNConflictsDetail> solvedconflictDeliveryNoteList = new ArrayList<>();
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
			
			//validate the Warehouse Ids
			validateWarehouses(allowedWarehouses);
			
			String orderByClause = getOrderByClause(sortingType, sortingBy, "SolvedConflictsDeliveryNote");
			
			if(warehouseNos != null && !warehouseNos.isEmpty()){
				 warehouseNos.removeIf(s -> s == null || "".equals(s.trim()));
				allowedWarehouses = StringUtils.join(warehouseNos, ",");
			}

			StringBuilder query = new StringBuilder("Select HERST, LNR, (BEST_ART|| BEST_AP) AS Bestellnummer, BEST_BENDL, LFSNR, ");
			query.append("DISPOPOS, DISPOUP, ERROR_CODE, ETNR, PARTNAME, HINT, OLDVALUE, NEWVALUE, TIMESTAMP, SOLVED, ");
			query.append("( Select COUNT(*) from ");
			query.append(schema).append(".O_BSNCONF where SOLVED <> '' and LFSNR NOT IN (Select LIEFNR_GESAMT from ");
			query.append(dataLibrary).append(".E_BSNDAT) and LNR in (").append(allowedWarehouses).append(") ) AS ROWNUMER  from ");
			query.append(schema).append(".O_BSNCONF where SOLVED <> '' and LFSNR NOT IN (Select LIEFNR_GESAMT from ");
			query.append(dataLibrary).append(".E_BSNDAT) and LNR in (").append(allowedWarehouses).append(") ");
			query.append(orderByClause).append(" OFFSET ");
			query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");
			
										
			List<BSNConflictsDetailDTO> solvedconflictDeliveryNoteDTOList = new ArrayList<>();
			solvedconflictDeliveryNoteList = dbServiceRepository.getResultUsingQuery(BSNConflictsDetail.class, query.toString(), true);

			//if the list is not empty
			if (solvedconflictDeliveryNoteList != null && !solvedconflictDeliveryNoteList.isEmpty()) {

				solvedconflictDeliveryNoteDTOList = convertBSN_SolvedConflictToDTO(solvedconflictDeliveryNoteList, solvedconflictDeliveryNoteDTOList);

				globalSearchList.setSearchDetailsList(solvedconflictDeliveryNoteDTOList);
				globalSearchList.setTotalPages(Integer.toString(solvedconflictDeliveryNoteList.get(0).getTotalCount()));
				globalSearchList.setTotalRecordCnt(Integer.toString(solvedconflictDeliveryNoteList.get(0).getTotalCount()));
			}
			else {
				globalSearchList.setSearchDetailsList(solvedconflictDeliveryNoteDTOList);
				globalSearchList.setTotalPages(Integer.toString(0));
				globalSearchList.setTotalRecordCnt(Integer.toString(0));
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Lieferschein behobene Konflikten (solved Conflict delivery notes)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Lieferschein behobene Konflikten (solved Conflict delivery notes)"), exception);
			throw exception;
		}

		return globalSearchList;
	}
	
	
	/**
	 * This method is used to convert BSN Solved Conflict Delivery Note Entity to BSN Solved Conflict delivery Note DTO Format.
	 * @param bsnSolvedConlictDetailsList
	 * @return BSNConflictsDetailDTO
	 */
	private List<BSNConflictsDetailDTO> convertBSN_SolvedConflictToDTO(List<BSNConflictsDetail> bsnSolvedConlictDetailsList, 
			List<BSNConflictsDetailDTO> bsnSolvedConflictDTOList) {

		for (BSNConflictsDetail bsnSolvedConflict : bsnSolvedConlictDetailsList) {
			BSNConflictsDetailDTO bsnConflictDTO = new BSNConflictsDetailDTO();
			
			if(bsnSolvedConflict.getManufacturer()!= null && bsnSolvedConflict.getManufacturer().equalsIgnoreCase(RestInputConstants.DCAG_STRING)) {
				bsnConflictDTO.setManufacturer(RestInputConstants.DAG_STRING);
			} else {
				bsnConflictDTO.setManufacturer(bsnSolvedConflict.getManufacturer());
			}

			bsnConflictDTO.setWarehouseNo(bsnSolvedConflict.getWarehouseNo());
			bsnConflictDTO.setSupplierNo(bsnSolvedConflict.getSupplierNo());
			bsnConflictDTO.setDeliveryNoteNo(bsnSolvedConflict.getDeliveryNoteNo());
			bsnConflictDTO.setOrderNumber(StringUtils.leftPad(bsnSolvedConflict.getOrderNumber(), 6, "0"));
			
			bsnConflictDTO.setDispopos(bsnSolvedConflict.getDispopos());
			bsnConflictDTO.setDispoup(bsnSolvedConflict.getDispoup());
			
			bsnConflictDTO.setConflict_Code(bsnSolvedConflict.getConflict_Code());
			bsnConflictDTO.setPartNumber(bsnSolvedConflict.getPartNumber());
			bsnConflictDTO.setPartName(bsnSolvedConflict.getPartName());
			bsnConflictDTO.setHint(bsnSolvedConflict.getHint());
			
			bsnConflictDTO.setOldValue(bsnSolvedConflict.getOldValue());
			bsnConflictDTO.setNewVlaue(bsnSolvedConflict.getNewVlaue());
			bsnConflictDTO.setSolved_Status(bsnSolvedConflict.getSolved_Status());
			
			String conflictSolvedTime = bsnSolvedConflict.getConflictSolvedDate();
			
			if(conflictSolvedTime != null && conflictSolvedTime.length() > 20){
				String yyyymmdd = conflictSolvedTime.substring(0,10);
				bsnConflictDTO.setConflictSolvedDate(convertDateToString(StringUtils.remove(yyyymmdd, "-")));
			}else{
				bsnConflictDTO.setConflictSolvedDate("");
			}

			bsnSolvedConflictDTOList.add(bsnConflictDTO);
		}

		return bsnSolvedConflictDTOList;
	}
	
	
	private String getOrderByClause(String sortingType, String sortingBy, String methodType) {
		
		
		StringBuilder orderByClause = new StringBuilder();
		
		if (methodType.equalsIgnoreCase("DeliveryNote")) {

			if (sortingBy != null && sortingBy.equalsIgnoreCase("Lieferschein")) {
				orderByClause.append("ORDER BY LFSNR ");
			}
			else if (sortingBy != null && sortingBy.equalsIgnoreCase("Lieferant")) {
				orderByClause.append("ORDER BY Lieferant ");
			}			
			else {
				orderByClause.append("ORDER BY DATUM_LFS ");
			}

		} else if (methodType.equalsIgnoreCase("BSNDeliveryNote")) {

			if(sortingBy != null && sortingBy.equalsIgnoreCase("Lieferschein")){
				orderByClause.append("ORDER BY LIEFNR_GESAMT ");
			}
			else if (sortingBy != null && sortingBy.equalsIgnoreCase("Lieferant")) {
				orderByClause.append("ORDER BY BEST_BENDL ");
			}
			else{
				orderByClause.append("ORDER BY BDAT ");
			}
			
		} else if (methodType.equalsIgnoreCase("ConflictDeliveryNote")) {

			if(sortingBy != null && sortingBy.equalsIgnoreCase("Lieferschein")){
				orderByClause.append("ORDER BY bsn.LIEFNR_GESAMT ");
			}else{
				orderByClause.append("ORDER BY bsn.BDAT ");
			}
			
		} else if (methodType.equalsIgnoreCase("SolvedConflictsDeliveryNote")) {

			if (sortingBy != null && sortingBy.equalsIgnoreCase("Lieferschein")) {
				orderByClause.append("ORDER BY LFSNR ");
			} else {
				orderByClause.append("ORDER BY TIMESTAMP ");
			}
		}
		else{
			log.info("Method type is not valid");  
		}
		
		if(sortingType!= null && sortingType.equalsIgnoreCase("Ascending")){
			orderByClause.append("ASC");
		}else{
			orderByClause.append("DESC");
		}
		
		
		return orderByClause.toString();
	}
	
	
	
	/**
	 * This method is used to delete the selected BSN Delivery Notes from DB.
	 */
	@Override
	public Map<String , Boolean> deleteBSNDeliveryNotes( String dataLibrary, List<BSNDeliveryNotesDTO> bsnDeliveryNoteList) {

		log.info("Inside deleteBSNDeliveryNotes method of IncomingGoodsServiceImpl");

		Map<String, Boolean> deleteBSNdnOutput = new HashMap<String, Boolean>();
		deleteBSNdnOutput.put("isDeleted", false);

		try(Connection con = dbServiceRepository.getConnectionObject();
				Statement stmt = con.createStatement();){ 

			con.setAutoCommit(false);

			if(bsnDeliveryNoteList!=null && !bsnDeliveryNoteList.isEmpty()) {

				for(BSNDeliveryNotesDTO bsndeliveryNote : bsnDeliveryNoteList) {

					String manufacturer = bsndeliveryNote.getManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING)? 
							RestInputConstants.DCAG_STRING: bsndeliveryNote.getManufacturer();			

					StringBuilder query = new StringBuilder("DELETE FROM ");
					query.append(dataLibrary).append(".E_BSNDAT  where LNR =").append(bsndeliveryNote.getWarehouseNumber());
					query.append(" and HERST='").append(manufacturer);
					query.append("' and  LIEFNR_GESAMT = '").append(bsndeliveryNote.getDeliveryNoteNo());
					query.append("' and BEST_BENDL =" ).append(bsndeliveryNote.getSupplierNumber());

					log.info("Query : {} ", query.toString() );
					stmt.addBatch(query.toString());
				}
				
				int[] records =  dbServiceRepository.deleteResultUsingBatchQuery(stmt);

				if(records != null){
					log.info("Delete BSN delivery Notes - Total rows deleted : {} ", records.length);
				}
				con.commit();
				con.setAutoCommit(true);
				
				deleteBSNdnOutput.put("isDeleted", true);
			}
			else {
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "BSN deliveryNote"));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "BSN deliveryNote"), exception);
				throw exception;
			}	
		} 
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.DELETE_ADMIN_SETTING_FAILED_MSG_KEY, "bsn deliveryNotes"));
			log.error(messageService.getReadableMessage(ExceptionMessages.DELETE_ADMIN_SETTING_FAILED_MSG_KEY, "bsn deliveryNotes"), exception);
			throw exception;
		}

		return deleteBSNdnOutput;
	}
	
	/**
	 * This is method is used to cancel delivered parts against their order (Rückstandsauflösung) By COBOL call.
	 */
	@Override
	public Map<String, String> backlogResolution(String dataLibrary, String backlogType, String agencyId, String companyId) {

		log.info("Inside backlogResolution method of IncomingGoodsServiceImpl");

		Map<String, String> programOutput = new HashMap<String, String>();
		try {
			
			ProgramParameter[] parmList = new ProgramParameter[5];

			String returnCode = StringUtils.rightPad("", 5, " ");
			parmList[0] = new ProgramParameter(returnCode.getBytes(Program_Commands_Constants.IBM_273), 5);

			String returnMsg = StringUtils.rightPad("", 100, " ");
			parmList[1] = new ProgramParameter(returnMsg.getBytes(Program_Commands_Constants.IBM_273), 100);

			parmList[2] = new ProgramParameter(backlogType.getBytes(Program_Commands_Constants.IBM_273), 1);
			
			agencyId = StringUtils.leftPad(agencyId, 2, "0");
			parmList[3] = new ProgramParameter(agencyId.getBytes(Program_Commands_Constants.IBM_273), 2);
			
			companyId = StringUtils.leftPad(companyId, 2, "0");
			parmList[4] = new ProgramParameter(companyId.getBytes(Program_Commands_Constants.IBM_273), 2);

			// execute the COBOL program - OFKT772CL.PGM
			List<String> outputList = cobolServiceRepository.executeProgram(parmList, 0,Program_Commands_Constants.BACKLOG_RESOLUTION);

			if (outputList != null && !outputList.isEmpty()) {
				if (!outputList.get(0).contains("Error")) {
					programOutput.put(outputList.get(0).trim(), outputList.get(1).trim());
				}
			}
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY,"Backlog Resolution (Rückstandsauflösung)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Backlog Resolution (Rückstandsauflösung)"), exception);
			throw exception;
		}
		return programOutput;
	}
	
	
}


	

	
