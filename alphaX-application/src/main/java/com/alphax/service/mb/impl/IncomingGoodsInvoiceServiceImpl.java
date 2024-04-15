package com.alphax.service.mb.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.alphax.common.constants.RestInputConstants;
import com.alphax.common.exception.AlphaXException;
import com.alphax.common.exception.ExceptionMessages;
import com.alphax.common.rest.message.service.MessageService;
import com.alphax.model.mb.AddCostsDetails;
import com.alphax.model.mb.AuditAssignDeliveryNotes;
import com.alphax.model.mb.BADeliveryNotes;
import com.alphax.model.mb.BookedDeliveryNoteDetails;
import com.alphax.model.mb.DeliveryNotes_BA;
import com.alphax.model.mb.InventoryList;
import com.alphax.model.mb.InvoiceDiffernces;
import com.alphax.model.mb.RecordDifferences;
import com.alphax.model.mb.SA60Model;
import com.alphax.model.mb.SA61Model;
import com.alphax.repository.DBServiceRepository;
import com.alphax.service.mb.IncomingGoodsInvoiceService;
import com.alphax.vo.mb.ActivatedCountingListDTO;
import com.alphax.vo.mb.AddCostsDetailsDTO;
import com.alphax.vo.mb.AuditAssignDeliveryNotesDTO;
import com.alphax.vo.mb.AuditInvoiceDTO;
import com.alphax.vo.mb.BookedDeliveryNoteDetailsDTO;
import com.alphax.vo.mb.BusinessCases25DTO;
import com.alphax.vo.mb.BusinessCases49DTO;
import com.alphax.vo.mb.DeliveryNotes_BA_DTO;
import com.alphax.vo.mb.DeparturesBA_DTO;
import com.alphax.vo.mb.GlobalSearch;
import com.alphax.vo.mb.InvoiceDeliveryNoteDTO;
import com.alphax.vo.mb.InvoiceDifferncesDTO;
import com.alphax.vo.mb.InvoiceOverviewDTO;
import com.alphax.vo.mb.RecordDifferences_DTO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IncomingGoodsInvoiceServiceImpl extends BaseService implements IncomingGoodsInvoiceService {

	@Autowired
	DBServiceRepository dbServiceRepository;

	@Autowired
	private MessageService messageService;

	@Autowired
	BusinessCasesImpl businessCasesServiceImpl;

	DecimalFormat decimalformat_twodigit = new DecimalFormat("#0.00", new DecimalFormatSymbols(Locale.ENGLISH));

	/**
	 * This method is used to get the List of existing Booking Texts.
	 * @return List
	 */
	@Override
	public GlobalSearch getBookingTextList(String schema, String searchString) {

		log.info("Inside getBookingTextList method of IncomingGoodsInvoiceServiceImpl");

		GlobalSearch globalSearchList = new GlobalSearch();

		try {
			StringBuilder query = new StringBuilder("Select RED_BUBEGR, RED_WESKTO from ").append(schema).append(".PEW_REDIFF Where UPPER(RED_BUBEGR) LIKE UPPER('%");
			query.append(searchString).append("%')  order by RED_BUBEGR");

			List<InvoiceDiffernces> bookingTextList = dbServiceRepository.getResultUsingQuery(InvoiceDiffernces.class, query.toString(), true);

			List<InvoiceDifferncesDTO> differencesList = new ArrayList<>();

			if (bookingTextList != null && !bookingTextList.isEmpty()) {

				for(InvoiceDiffernces invoiceDiff : bookingTextList){
					InvoiceDifferncesDTO differncesDTO = new InvoiceDifferncesDTO();

					differncesDTO.setAccountNumber(invoiceDiff.getAccountNumber());
					differncesDTO.setBookingTerm(invoiceDiff.getBookingTerm());

					differencesList.add(differncesDTO);
				}

				globalSearchList.setSearchDetailsList(differencesList);
				globalSearchList.setTotalPages(Integer.toString(0));
				globalSearchList.setTotalRecordCnt(Integer.toString(bookingTextList.size()));
			}
			else {
				globalSearchList.setSearchDetailsList(differencesList);
				globalSearchList.setTotalPages(Integer.toString(0));
				globalSearchList.setTotalRecordCnt(Integer.toString(0));
			}


		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Booking Texts list"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Booking Texts list"), exception);
			throw exception;
		}

		return globalSearchList;
	}


	/**
	 * This method is used to get the List of invoices Number.
	 * @return List
	 */
	@Override
	public List<InvoiceDifferncesDTO> getInvoiceNumberList(String dataLibrary,String schema,
			String warehouseNo, String manufacturer, String supplierNo, String searchString) {

		log.info("Inside getInvoiceNumberList method of IncomingGoodsInvoiceServiceImpl");

		List<InvoiceDifferncesDTO> invoiceNumberList = new ArrayList<>();

		try {
			manufacturer = manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING) ? RestInputConstants.DCAG_STRING: manufacturer;


			StringBuilder query = new StringBuilder("Select RENUM from ").append(dataLibrary).append(".E_BSNRCH WHERE ");
			query.append("LNR = ").append(warehouseNo).append(" AND HERST = '").append(manufacturer).append("' AND BENDL =").append(supplierNo);
			query.append(" AND RENUM LIKE '%").append(searchString).append("%'");

			List<InvoiceDiffernces> differncesList = dbServiceRepository.getResultUsingQuery(InvoiceDiffernces.class, query.toString(), true);

			if (differncesList != null && !differncesList.isEmpty()) {

				for(InvoiceDiffernces invoiceDiff : differncesList){
					InvoiceDifferncesDTO differncesDTO = new InvoiceDifferncesDTO();

					differncesDTO.setInvoicesNumber(invoiceDiff.getInvoicesNumber());

					invoiceNumberList.add(differncesDTO);
				}
			}else{
				log.info("Invoice Number List is empty");
			} 

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Invoice Number list"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Invoice Number list"), exception);
			throw exception;
		}

		return invoiceNumberList;
	}


	/**
	 * This method is used to get the List of record differences (Differenzen erfassen).
	 * @return List
	 */
	@Override
	public List<RecordDifferences_DTO> getRecordDifferencesList(String dataLibrary,String schema, String deliveryNoteNo,
			String manufacturer, String warehouseNo, String supplierNo) {

		log.info("Inside getRecordDifferencesList method of IncomingGoodsInvoiceServiceImpl");

		List<RecordDifferences_DTO> recordDiff_obj = new ArrayList<>();

		try {

			manufacturer = manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING) ? RestInputConstants.DCAG_STRING: manufacturer;

			StringBuilder query = new StringBuilder("Select BUKONTOTXT, BUKONTO, NWERT_SEPSIGN from ");
			query.append(dataLibrary).append(".E_BSNLFS where NRX = '").append(deliveryNoteNo).append("' ");
			query.append(" AND HERST = '").append(manufacturer).append("' ");
			query.append(" AND LNR = ").append(warehouseNo).append(" ");
			query.append(" AND BENDL = ").append(supplierNo).append(" ");


			List<RecordDifferences> differncesList = dbServiceRepository.getResultUsingQuery(RecordDifferences.class, query.toString(), true);

			if (differncesList != null && !differncesList.isEmpty()) {

				for(RecordDifferences recordDiff : differncesList){
					RecordDifferences_DTO differncesDTO = new RecordDifferences_DTO();

					differncesDTO.setBookingText(recordDiff.getBookingText());
					differncesDTO.setAccount(recordDiff.getAccount());

					StringBuilder sb = new StringBuilder();
					if(recordDiff.getAmount().contains("+")) {
						String nwert = StringUtils.remove(recordDiff.getAmount(),"+");
						nwert = StringUtils.stripStart(nwert, "0");
						if(nwert.length() >= 2) {
							differncesDTO.setAmount(sb.append(nwert).insert(nwert.length()-2, ".").toString());	
						}else {
							differncesDTO.setAmount("0.00");	
						}
					}else if(recordDiff.getAmount().contains("-")) {
						String nwert = StringUtils.remove(recordDiff.getAmount(),"-");
						nwert = StringUtils.stripStart(nwert, "0");
						if(nwert.length() >= 2) {
							differncesDTO.setAmount(sb.append(nwert).insert(nwert.length()-2, ".").insert(0, "-").toString());	
						}else {
							differncesDTO.setAmount("0.00");	
						}
					}else {
						differncesDTO.setAmount(recordDiff.getAmount());
					}

					differncesDTO.setExistingEntryFlag(true);
					recordDiff_obj.add(differncesDTO);
				}
			}else{
				log.info("record differences (Differenzen erfassen) is empty");
			} 

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "record differences (Differenzen erfassen) list"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"record differences (Differenzen erfassen) list"), exception);
			throw exception;
		}

		return recordDiff_obj;
	}


	/**
	 * This method is used to Capture record differences (Differenzen erfassen) list in DB.
	 * @return List
	 */
	@Override
	public List<RecordDifferences_DTO> saveRecordDifferences(List<RecordDifferences_DTO> recordDiffList, String dataLibrary,String schema) {

		log.info("Inside saveRecordDifferences method of IncomingGoodsInvoiceServiceImpl");

		try(Connection con = dbServiceRepository.getConnectionObject();
				Statement stmt = con.createStatement();){

			//con.setAutoCommit(false);

			if (recordDiffList != null && recordDiffList.size() > 0) {

				String	manufacturer = recordDiffList.get(0).getManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING) ? RestInputConstants.DCAG_STRING: recordDiffList.get(0).getManufacturer();

				// get max value of APOS from E_LFSL
				StringBuilder query_lfsl_count = new StringBuilder("select APOS FROM ").append(dataLibrary).append(".E_LFSL WHERE  Herst= '").append(manufacturer).append("' AND LNR=");
				query_lfsl_count.append(recordDiffList.get(0).getWarehouseNumber()).append(" AND NRX =  '").append(recordDiffList.get(0).getDeliveryNoteNo()).append("' AND BENDL=").append(recordDiffList.get(0).getSupplierNumber());
				query_lfsl_count.append(" order by APOS desc fetch first 1 rows only ");

				int apos = 0;
				List<BADeliveryNotes> BADeliveryNotesList = dbServiceRepository.getResultUsingQuery(BADeliveryNotes.class, query_lfsl_count.toString(), true);

				if(BADeliveryNotesList!= null && !BADeliveryNotesList.isEmpty()){
					apos = BADeliveryNotesList.get(0).getApos().intValue()+ 1;
				}

				StringBuilder query_bsnfsl_count = new StringBuilder("select LAUFNR  FROM ").append(dataLibrary).append(".E_BSNLFS WHERE  Herst= '").append(manufacturer).append("' AND LNR=");
				query_bsnfsl_count.append(recordDiffList.get(0).getWarehouseNumber()).append(" AND NRX =  '").append(recordDiffList.get(0).getDeliveryNoteNo()).append("' AND BENDL=").append(recordDiffList.get(0).getSupplierNumber());
				query_bsnfsl_count.append(" order by LAUFNR desc fetch first 1 rows only ");

				int laufnrCount = 0;
				List<DeliveryNotes_BA> deliveryNoteList = dbServiceRepository.getResultUsingQuery(DeliveryNotes_BA.class, query_bsnfsl_count.toString(), true);

				if(deliveryNoteList!=null && !deliveryNoteList.isEmpty()){
					laufnrCount = Integer.parseInt(deliveryNoteList.get(0).getLaufnr()) + 1;
				}

				int lfsl_Count = apos;
				int bsnlfs_Count = laufnrCount;

				for (RecordDifferences_DTO recordDiff_obj : recordDiffList) {

					//String	manufacturer = recordDiff_obj.getManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING) ? RestInputConstants.DCAG_STRING: recordDiff_obj.getManufacturer();

					StringBuilder query_lfsl = new StringBuilder("INSERT INTO ").append(dataLibrary);
					query_lfsl.append(".E_LFSL (NRX, NEPREIS, WERTN, TNR, BUKONTOTXT, BUKONTO, HERST, LNR, BENDL, ");
					query_lfsl.append(" PROGRAMM, APOS) VALUES ( '");
					query_lfsl.append(recordDiff_obj.getDeliveryNoteNo()).append("' , ");
					query_lfsl.append(recordDiff_obj.getAmount().replace("+", "")).append(", ");
					query_lfsl.append(recordDiff_obj.getAmount().replace("+", "")).append(", '");
					query_lfsl.append(recordDiff_obj.getBookingText()).append("', '");
					query_lfsl.append(recordDiff_obj.getBookingText()).append("', '");
					query_lfsl.append(recordDiff_obj.getAccount()).append("', '");
					query_lfsl.append(manufacturer).append("', ");
					query_lfsl.append(recordDiff_obj.getWarehouseNumber()).append(", ");
					query_lfsl.append(recordDiff_obj.getSupplierNumber()).append(", ");
					query_lfsl.append(" 'BSV470', ");
					query_lfsl.append(lfsl_Count).append(" ) ");

					StringBuilder query_bsnlfs = new StringBuilder("INSERT INTO ").append(dataLibrary);
					query_bsnlfs.append(".E_BSNLFS  (NRX , NWERT_SEPSIGN , BUKONTOTXT, BUKONTO, HERST, LNR, BENDL, ");  //NWERT_SEPSIGN
					query_bsnlfs.append(" PROGRAMM, ART, LAUFNR ) VALUES ( '");
					query_bsnlfs.append(recordDiff_obj.getDeliveryNoteNo()).append("' , '");

					if(Double.parseDouble(recordDiff_obj.getAmount()) >= 0) {
						query_bsnlfs.append(StringUtils.leftPad(recordDiff_obj.getAmount().replace(".", ""),9,"0")+"+").append("', '");
					}else {
						String amount = StringUtils.remove(recordDiff_obj.getAmount(), ".");
						amount = StringUtils.remove(amount, "-");
						query_bsnlfs.append(StringUtils.leftPad(amount,9,"0")+"-").append("', '");	
					}

					query_bsnlfs.append(recordDiff_obj.getBookingText()).append("', '");
					query_bsnlfs.append(recordDiff_obj.getAccount()).append("', '");
					query_bsnlfs.append(manufacturer).append("', ");
					query_bsnlfs.append(recordDiff_obj.getWarehouseNumber()).append(", ");
					query_bsnlfs.append(recordDiff_obj.getSupplierNumber()).append(", ");
					query_bsnlfs.append(" 'BSV470', ");
					query_bsnlfs.append(" 'BSV470', '");
					query_bsnlfs.append(StringUtils.leftPad(String.valueOf(bsnlfs_Count), 2, "0")).append("' ) ");

					lfsl_Count++;
					bsnlfs_Count++;

					stmt.addBatch(query_lfsl.toString());
					stmt.addBatch(query_bsnlfs.toString());
					log.info("Query for E_LFSL  :" + query_lfsl.toString());
					log.info("Query for E_BSNLFS  :" + query_bsnlfs.toString());

				}
			}

			int[] records = dbServiceRepository.insertResultUsingBatchQuery(stmt);
			if (records != null) {
				log.info("In E_LFSL  and E_BSNLFS  - Total rows Inserted  :" + records.length);
			} else {
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.CREATE_FAILED_MSG_KEY,"record differences (Differenzen erfassen) list"));
				log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY,
						"record differences (Differenzen erfassen) list"), exception);
				throw exception;
			}

			con.commit();
			//con.setAutoCommit(true);

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.CREATE_FAILED_MSG_KEY, "record differences (Differenzen erfassen) list"));
			log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY,
					"record differences (Differenzen erfassen) list"), exception);
			throw exception;
		}

		return recordDiffList;
	}

	/**
	 * This method is used to do corrections in booking of delivery notes related to invoices data (Komigieren).
	 * @return List
	 */
	@Override
	public Map<String, String> correctionsInBookingForDeliveryNotes(BookedDeliveryNoteDetailsDTO detailsDTO, String dataLibrary,
			String schema, String companyId, String agencyId, String loginUserName) {

		log.info("Inside correctionsInBookingForDeliveryNotes method of IncomingGoodsInvoiceServiceImpl");

		Map<String, String> finalOutput = new HashMap<String, String>();
		try {
			// call BA 25 - to reduce the stock amount with the old values
			BusinessCases25DTO ba25_obj = new BusinessCases25DTO();

			String manufacturer = detailsDTO.getManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING: detailsDTO.getManufacturer();

			DeparturesBA_DTO departuresBA25 = businessCasesServiceImpl.getDeparturesBA25Details(dataLibrary, companyId,
					agencyId, manufacturer, detailsDTO.getWarehouseNumber(), detailsDTO.getPartNumber(),
					detailsDTO.getSupplierNumber(), detailsDTO.getDeliveryNoteNo(), "6", "25");

			setDeliveryAndInvoiceAudit(detailsDTO, dataLibrary, schema, loginUserName, ba25_obj,manufacturer);

			ba25_obj.setWarehouseNumber(detailsDTO.getWarehouseNumber());
			ba25_obj.setBusinessCases("25");
			ba25_obj.setPartMovementDate(detailsDTO.getPartMovementDate());
			ba25_obj.setUserName(detailsDTO.getUserName());
			ba25_obj.setManufacturer(manufacturer);
			ba25_obj.setPartNumber(detailsDTO.getPartNumber());
			ba25_obj.setDeliveryNoteNo(detailsDTO.getDeliveryNoteNo());
			ba25_obj.setPartName(detailsDTO.getDesignation());
			ba25_obj.setBookingAmount(detailsDTO.getPostedAmount());
			ba25_obj.setNetShoppingPrice(detailsDTO.getBookedPrice());
			ba25_obj.setSupplierNumber(detailsDTO.getSupplierNumber());
			ba25_obj.setSalesOrderPosition(detailsDTO.getDeliveryNotePosition());
			ba25_obj.setCustomerGroup("6");
			ba25_obj.setListPrice(StringUtils.stripStart(departuresBA25.getListPrice(),"-"));
			ba25_obj.setInputListPrice(StringUtils.stripStart(departuresBA25.getListPrice(),"-"));
			ba25_obj.setCurrentStock(departuresBA25.getCurrentStock());
			ba25_obj.setAverageNetPrice(departuresBA25.getAverageNetPrice());
			ba25_obj.setPurchaseOrderNumber(departuresBA25.getPurchaseOrderNumber());
			ba25_obj.setPendingOrders(departuresBA25.getPendingOrders());
			ba25_obj.setDiscountGroup(departuresBA25.getDiscountGroup());
			ba25_obj.setOemBrand(departuresBA25.getOemBrand());
			ba25_obj.setMarketingCode(departuresBA25.getMarketingCode());
			ba25_obj.setLastPurchasePrice(departuresBA25.getLastPurchasePrice());
			ba25_obj.setDeliverIndicator(departuresBA25.getDeliverIndicator());
			ba25_obj.setPartsIndikator(departuresBA25.getPartsIndikator());
			ba25_obj.setNetPrice(departuresBA25.getNetPrice());
			ba25_obj.setPreviousPurchasePrice(departuresBA25.getPreviousPurchasePrice());
			ba25_obj.setSortingFormatPartNumber(departuresBA25.getSortingFormatPartNumber());
			ba25_obj.setPrintingFormatPartNumber(departuresBA25.getPrintingFormatPartNumber());
			ba25_obj.setCompany(departuresBA25.getCompany());
			ba25_obj.setAgency(departuresBA25.getAgency());
			ba25_obj.setSpecialDiscount("00.00");
			ba25_obj.setActivityType(departuresBA25.getActivityType());
			ba25_obj.setMovementDate(departuresBA25.getMovementDate());

			//We are using BSN475 logic in both BA 25 and 06 so we are calling only once 
			Map<String, String> output_bsn475 = businessCasesServiceImpl.newJavaImplementation_BSN475(ba25_obj.getOemBrand(), dataLibrary, schema, manufacturer,
					ba25_obj.getMarketingCode(),ba25_obj.getDiscountGroup(), ba25_obj.getPartNumber());

			Map<String, Boolean> ba25Output = businessCasesServiceImpl.newJavaImplementation_BA25(ba25_obj, dataLibrary, schema,
					companyId, agencyId, loginUserName, output_bsn475);

			if (ba25Output != null && !ba25Output.isEmpty()) {

				if (ba25Output.get("isUpdated")) {
					// Call BA 49 - need Inputs - PartNo, AverageNetPrice-DAK ,
					// StorageIndikator-SA, OEM, LEGER
					// DAK needs to be corrected using a BA49
					BusinessCases49DTO obj_ba49 = new BusinessCases49DTO();
					obj_ba49.setPartNumber(ba25_obj.getPartNumber());
					obj_ba49.setWarehouseNo(ba25_obj.getWarehouseNumber());
					obj_ba49.setManufacturer(manufacturer);
					obj_ba49.setAverageNetPrice(ba25_obj.getAverageNetPrice());
					obj_ba49.setStorageIndikator(departuresBA25.getStorageIndikator());
					obj_ba49.setBusinessCases("49");
					obj_ba49.setPartDescription(ba25_obj.getPartName());
					obj_ba49.setCurrentStock(ba25_obj.getCurrentStock());
					obj_ba49.setBookingAmount(ba25_obj.getBookingAmount());
					obj_ba49.setPendingOrders(ba25_obj.getPendingOrders());

					Map<String, String> ba49Output = businessCasesServiceImpl.newJavaImplementation_BA49(obj_ba49,
							dataLibrary, schema, companyId, agencyId, ba25_obj.getUserName());

					if (ba49Output != null && !ba49Output.isEmpty()) {

						if (ba49Output.get("00000").trim().equalsIgnoreCase("BA49 success")) {

							// Call BA 06 - the stock amount needs to be
							// corrected (increased) to the final correct value
							// via BA06
							ba25_obj.setBookingAmount(detailsDTO.getCorrectAmount());
							ba25_obj.setNetShoppingPrice(detailsDTO.getCorrectPrice());
							ba25_obj.setBookingAmount(detailsDTO.getPostedAmount());
							ba25_obj.setBusinessCases("06");

							Map<String, String> ba06Output = businessCasesServiceImpl.newJavaImplementation_BA06(ba25_obj, dataLibrary, schema, companyId, 
									agencyId, loginUserName,output_bsn475);

							if (ba06Output != null && !ba06Output.isEmpty()) {

								if (ba06Output.get("00000").equalsIgnoreCase("BA06 success")) {

									finalOutput.put("returnCode", "00000");
									finalOutput.put("returnMsg", "Corrections completed successfully. ((Komigieren)");
								}else{
									finalOutput.put("returnCode", "BA06FAILED");
									finalOutput.put("returnMsg", "BA 06 Failed");
								}
							}
						}else{
							finalOutput.put("returnCode", "BA49FAILED");
							finalOutput.put("returnMsg", "BA 49 Failed");
						}
					}

				}else{
					finalOutput.put("returnCode", "BA25FAILED");
					finalOutput.put("returnMsg", "BA 25 Failed");
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,
					messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.UPDATE_FAILED_MSG_KEY, "corrections in booking (Komigieren)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY,
					"corrections in booking (Komigieren)"), exception);
			throw exception;
		}

		return finalOutput;
	}


	private void setDeliveryAndInvoiceAudit(BookedDeliveryNoteDetailsDTO detailsDTO, String dataLibrary, String schema,
			String loginUserName, BusinessCases25DTO ba25_obj, String manufacturer) {

		StringBuilder query = new StringBuilder(" SELECT PRUEFUNG, ERLEDIGT FROM ").append(dataLibrary).append(".E_BSNLFS WHERE ");
		query.append("  BA='' ");
		query.append(" AND HERST = '").append(manufacturer).append("' ");
		query.append(" AND LNR = ").append(detailsDTO.getWarehouseNumber());
		query.append(" AND BENDL = ").append(detailsDTO.getSupplierNumber());
		query.append(" AND UPPER(NRX) = UPPER('").append(detailsDTO.getDeliveryNoteNo()).append("') ");

		List<DeliveryNotes_BA> deliveryNoteList = dbServiceRepository.getResultUsingQuery(DeliveryNotes_BA.class, query.toString(), true);

		if(deliveryNoteList != null && !deliveryNoteList.isEmpty()){

			for(DeliveryNotes_BA deliveryNote : deliveryNoteList){

				ba25_obj.setDeliveryNoteAudit(deliveryNote.getDeliveryNoteAudit());
				ba25_obj.setInvoiceAudit(deliveryNote.getInvoiceAudit());
			}
		}	
	}


	/**
	 * This method is used to calculate the difference if at least the invoice number available .
	 * @return List
	 */
	@Override
	public String getCalculatedDifferanceValue(String dataLibrary,String schema, String invoiceNumber) {

		log.info("Inside getCalculatedDifferanceValue method of IncomingGoodsInvoiceServiceImpl");

		String differanceValue = "0";
		double sumOfAllBookedNetValue=0.00;
		double sumOfAllInvoiceValue = 0.00;

		try {

			StringBuilder query_1 = new StringBuilder("SELECT NWERT as sum_of_all_booked_net_values FROM ");
			query_1.append(dataLibrary).append(".e_bsnlfs where RENUM = '").append(invoiceNumber).append("'");

			List<RecordDifferences> bookedNetValueList = dbServiceRepository.getResultUsingQuery(RecordDifferences.class, query_1.toString(), true);

			if(bookedNetValueList!=null && !bookedNetValueList.isEmpty()) {
				for(RecordDifferences bookedValue: bookedNetValueList ) {
					StringBuilder sb = new StringBuilder();

					if(bookedValue.getSumOfAllBookedNetValue().contains("+")){

						String nwert = StringUtils.remove(bookedValue.getSumOfAllBookedNetValue(),"+");
						nwert = StringUtils.stripStart(nwert, "0");
						if(nwert.length() > 2) {
							nwert = sb.append(nwert).insert(nwert.length()-2, ".").toString();	
						}else {
							nwert = "0.00";	
						}

						sumOfAllBookedNetValue = sumOfAllBookedNetValue + Double.parseDouble(nwert);

					}else if(bookedValue.getSumOfAllBookedNetValue().contains("-")) {

						String nwert = StringUtils.remove(bookedValue.getSumOfAllBookedNetValue(),"-");
						nwert = StringUtils.stripStart(nwert, "0");
						if(nwert.length() > 2) {
							nwert = sb.append(nwert).insert(nwert.length()-2, ".").insert(0, "-").toString();	
						}else {
							nwert = "0.00";	
						}
						sumOfAllBookedNetValue = sumOfAllBookedNetValue + Double.parseDouble(nwert);

					}else {
						sumOfAllBookedNetValue = sumOfAllBookedNetValue + Double.parseDouble(bookedValue.getSumOfAllBookedNetValue());
					}
				}
			}

			StringBuilder query_2 = new StringBuilder("SELECT LCWERTN as sum_of_all_invoice_values  from ");
			query_2.append(dataLibrary).append(".E_BSNRCH where RENUM  = '").append(invoiceNumber).append("'");

			List<RecordDifferences> invoiceValueList = dbServiceRepository.getResultUsingQuery(RecordDifferences.class, query_2.toString(), true);

			if(invoiceValueList!=null && !invoiceValueList.isEmpty()) {
				if(invoiceValueList.get(0).getSumOfAllInvoiceValue()!=null) {
					sumOfAllInvoiceValue = Double.parseDouble(invoiceValueList.get(0).getSumOfAllInvoiceValue());
				}
			}

			differanceValue =  String.valueOf(decimalformat_twodigit.format(sumOfAllBookedNetValue - sumOfAllInvoiceValue));

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Differences value (Differenzen)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Differences value (Differenzen)"), exception);
			throw exception;
		}

		return differanceValue;
	}


	/**
	 * This method is used to get the List of Additional cost for invoices Number.
	 * @return List
	 */
	@Override
	public List<AddCostsDetailsDTO> getAdditionalCostList(String schema, String invoiceNumber) {

		log.info("Inside getAdditionalCostList method of IncomingGoodsInvoiceServiceImpl");

		List<AddCostsDetailsDTO> addCostDTOList = new ArrayList<>();

		try {
			StringBuilder query = new StringBuilder("Select ID, INVOICENR, COSTSTYPE, COSTSVALUE, TEXT, ACCOUNT  from ").append(schema).append(".O_ADDCOSTS ");
			query.append(" WHERE INVOICENR = ").append(invoiceNumber);

			List<AddCostsDetails> addCostList = dbServiceRepository.getResultUsingQuery(AddCostsDetails.class, query.toString(), true);

			if (addCostList != null && !addCostList.isEmpty()) {

				for(AddCostsDetails addCost : addCostList) {
					AddCostsDetailsDTO addCostDTO = new AddCostsDetailsDTO();

					addCostDTO.setAddCostsId(String.valueOf(addCost.getAddCostsId()));
					addCostDTO.setInvoiceNo(addCost.getInvoiceNo());
					addCostDTO.setCostsType(addCost.getCostsType());
					addCostDTO.setCostsValue(addCost.getCostsValue() != null ? String.valueOf(addCost.getCostsValue()) : "0");
					addCostDTO.setText(addCost.getText());
					addCostDTO.setAccount(addCost.getAccount());

					addCostDTOList.add(addCostDTO);
				}
			}else{
				log.info("Additional Costs list is empty");
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Additional Costs list"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Additional Costs list"), exception);
			throw exception;
		}

		return addCostDTOList;
	}


	/**
	 * This method is used to used for Save / Update the Additional cost details in DB.
	 * @return Object
	 */
	@Override
	public Map<String, Boolean> saveAdditionalCostDetails(List<AddCostsDetailsDTO> additionalCostDtoList, String invoiceNumber, String schema, String loginUserName) {

		log.info("Inside saveAdditionalCostDetails method of IncomingGoodsInvoiceServiceImpl");

		Map<String, Boolean> programOutput = new HashMap<String, Boolean>();

		try {
			if (additionalCostDtoList != null && !additionalCostDtoList.isEmpty()) {

				for(AddCostsDetailsDTO addCostObj : additionalCostDtoList) {

					StringBuilder query = new StringBuilder("MERGE INTO ").append(schema).append(".O_ADDCOSTS AS ADC ");
					query.append("USING (VALUES ('").append(invoiceNumber).append("', '").append(addCostObj.getCostsType()).append("' )) AS A_TMP ");
					query.append(" (INVOICENR, COSTSTYPE) ");
					query.append("ON ADC.INVOICENR = A_TMP.INVOICENR AND ADC.COSTSTYPE=A_TMP.COSTSTYPE ");							
					query.append("WHEN MATCHED THEN ");
					query.append("UPDATE SET COSTSVALUE = ").append(addCostObj.getCostsValue()).append(", TEXT ='").append(addCostObj.getText());
					query.append("', ACCOUNT ='").append(addCostObj.getAccount()).append("' ");							
					query.append(" WHEN NOT MATCHED THEN ");
					query.append("INSERT (INVOICENR, COSTSTYPE, COSTSVALUE, TEXT, ACCOUNT) ");
					query.append("VALUES ( ").append(invoiceNumber).append(", '").append(addCostObj.getCostsType()).append("', ");
					query.append(addCostObj.getCostsValue()).append(", '").append(addCostObj.getText()).append("', '").append(addCostObj.getAccount()).append("') ");
					query.append(" ELSE IGNORE");

					dbServiceRepository.insertResultUsingQuery(query.toString());

				}
				programOutput.put("isUpdated", true);
			}
			else {
				log.info("Additional costs details List is empty.");

				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "Additional costs List"));
				log.error(messageService.getReadableMessage(ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "Additional costs List"));
				throw exception;
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "additional costs details"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY,
					"additional costs details"), exception);
			throw exception;
		}

		return programOutput;
	}

	/**
	 *  This is method is used to create Invoice Manually.
	 */
	@Override
	public Map<String, Boolean> createInvoiceManually(AuditInvoiceDTO auditInvoiceObj, String dataLibrary,
			String schema, String logedInUser) {
		log.info("Inside createInvoiceManually method of IncomingGoodsInvoiceServiceImpl");
		HashMap<String, Boolean> outputFlag = new HashMap<String, Boolean>();
		outputFlag.put("isInvoiceCreatedSuccessfully", false);
		
		try(Connection con = dbServiceRepository.getConnectionObject();
				Statement stmt = con.createStatement();){
			
			con.setAutoCommit(false);
			
			StringBuilder queryForBA60 = new StringBuilder("select count(*) AS COUNT from ");
			queryForBA60.append(schema).append(".O_SA60 where ");
			queryForBA60.append(" INVOICENR= ").append(auditInvoiceObj.getInvoiceNumber());
			queryForBA60.append(" AND VENDOR = ").append(auditInvoiceObj.getSupplier());
			queryForBA60.append(" AND INVOICEDATE = '").append(auditInvoiceObj.getInvoiceDate()).append("'");
			queryForBA60.append("AND OEM = '").append(auditInvoiceObj.getOem()).append("' ");
			queryForBA60.append(" AND AP_WRH = ").append(auditInvoiceObj.getWarehouseNumber());

			String count = dbServiceRepository.getCountUsingQuery(queryForBA60.toString());
			if(Integer.parseInt(count) > 0){
				log.info("Duplicate Entry of Invoice Number");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.UNIQUE_CONSTRAINT_FAILED_MSG_KEY, "Invoice Number"));
				log.error(messageService.getReadableMessage(ExceptionMessages.UNIQUE_CONSTRAINT_FAILED_MSG_KEY, "Invoice Number"));
				throw exception;
			}
			StringBuilder insert_SA60 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SA60");
			insert_SA60.append("(INVOICENR,INVOICEDATE,OEM, VENDOR,AP_WRH, INVOICENETVALUE, TAXBASEVALUE,PACKING, FREIGHTPOSTEXPR, FOBCHARGES, ");
			insert_SA60.append(" SEAAIRFREIGHT, INSURANCE ,CONSFEES ,SHIPPCHARGES, CONTCHARGES, OTHERCHARGES, STATE , CREATED_BY, CREATED_TS, ORIGIN, GUID, COMPANYCODE ) ");
			insert_SA60.append(" VALUES (");
			insert_SA60.append(auditInvoiceObj.getInvoiceNumber()).append(","); //INVOICENR
			insert_SA60.append("'").append(auditInvoiceObj.getInvoiceDate()).append("' ,"); //INVOICEDATE
			insert_SA60.append("'").append(auditInvoiceObj.getOem()).append("' ,"); //OEM
			insert_SA60.append(auditInvoiceObj.getSupplier()).append(","); //VENDOR
			insert_SA60.append(auditInvoiceObj.getWarehouseNumber()).append(","); //AP_WRH
			
			String partsCostsWith7PerVAT = "0.00";
			String partsCostsWith19PerVAT = "0.00";
			
			if(NumberUtils.isParsable(auditInvoiceObj.getPartsCostsWith7PerVAT())) {
				partsCostsWith7PerVAT = auditInvoiceObj.getPartsCostsWith7PerVAT();
			}
			if(NumberUtils.isParsable(auditInvoiceObj.getPartsCostsWith19PerVAT())) {
				partsCostsWith19PerVAT = auditInvoiceObj.getPartsCostsWith19PerVAT();
			}
			String invoiceNetValue = String.valueOf(Double.parseDouble(partsCostsWith7PerVAT) + Double.parseDouble(partsCostsWith19PerVAT));
			insert_SA60.append(invoiceNetValue).append(",");  //INVOICENETVALUE
			insert_SA60.append(auditInvoiceObj.getTotalInvoiceAmount()).append(",");  //TAXBASEVALUE
			
			String nullValue = null;
			if(NumberUtils.isParsable(auditInvoiceObj.getPacking())) {
			insert_SA60.append(auditInvoiceObj.getPacking()).append(","); //PACKING
			}else {
				insert_SA60.append(nullValue).append(","); //PACKING	
			}
			
			if(NumberUtils.isParsable(auditInvoiceObj.getFreightPostExpr())) {
			insert_SA60.append(auditInvoiceObj.getFreightPostExpr()).append(","); //FREIGHTPOSTEXPR
			}else {
				insert_SA60.append(nullValue).append(","); //FREIGHTPOSTEXPR	
			}
			
			if(NumberUtils.isParsable(auditInvoiceObj.getFobCharges())) {
				insert_SA60.append(auditInvoiceObj.getFobCharges()).append(","); //FOBCHARGES
			}else {
					insert_SA60.append(nullValue).append(","); //FOBCHARGES	
			}
			
			if(NumberUtils.isParsable(auditInvoiceObj.getSeaAirfright())) {
				insert_SA60.append(auditInvoiceObj.getSeaAirfright()).append(","); //SEAAIRFREIGHT
			}else {
					insert_SA60.append(nullValue).append(","); //SEAAIRFREIGHT	
			}
			
			if(NumberUtils.isParsable(auditInvoiceObj.getInsurance())) {
				insert_SA60.append(auditInvoiceObj.getInsurance()).append(","); //INSURANCE
			}else {
					insert_SA60.append(nullValue).append(","); //INSURANCE	
			}
			
			if(NumberUtils.isParsable(auditInvoiceObj.getConfees())) {
				insert_SA60.append(auditInvoiceObj.getConfees()).append(","); //CONSFEES
			}else {
					insert_SA60.append(nullValue).append(","); //CONSFEES	
			}
			
			if(NumberUtils.isParsable(auditInvoiceObj.getShipCharges())) {
				insert_SA60.append(auditInvoiceObj.getShipCharges()).append(","); //SHIPPCHARGES
			}else {
					insert_SA60.append(nullValue).append(","); //SHIPPCHARGES	
			}
			
			if(NumberUtils.isParsable(auditInvoiceObj.getContCharges())) {
				insert_SA60.append(auditInvoiceObj.getContCharges()).append(","); //CONTCHARGES
			}else {
					insert_SA60.append(nullValue).append(","); //CONTCHARGES	
			}
			
			if(NumberUtils.isParsable(auditInvoiceObj.getOtherCharges())) {
				insert_SA60.append(auditInvoiceObj.getOtherCharges()).append(","); //OTHERCHARGES
			}else {
					insert_SA60.append(nullValue).append(","); //OTHERCHARGES	
			}
			
			insert_SA60.append("0 ,");  // STATE
			insert_SA60.append("'").append(logedInUser).append("', CURRENT TIMESTAMP, 1, '', 000000 ) ");
			
			int SA60Id = dbServiceRepository.insertResultUsingQuery(insert_SA60.toString(), con);

			if (SA60Id == 0) {
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.CREATE_FAILED_MSG_KEY, "Manually invoice"));
				log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY, "Manually invoice"), exception);
				throw exception;
			}
			
			insertValueIn_ADDCOSTS(stmt, auditInvoiceObj, schema );
			int[] records = dbServiceRepository.insertResultUsingBatchQuery(stmt);
			
			if (records != null) {
				log.info("In O_ADDCOSTS - Total rows Inserted  :" + records.length);
			}
			stmt.clearBatch();

			StringBuilder insert_SA61_1 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SA61");
			insert_SA61_1.append(" (INVOICENR , DESCRIPTION, INVOICEDQTY,  TOTALNETVALLINEITEM, STATE, CREATED_BY, CREATED_TS )");
			insert_SA61_1.append(" VALUES (");
			insert_SA61_1.append(auditInvoiceObj.getInvoiceNumber()).append(",");
			insert_SA61_1.append(" 'Teilekosten mit 7% MwSt.' , 1 ,");
			insert_SA61_1.append(auditInvoiceObj.getPartsCostsWith7PerVAT());
			insert_SA61_1.append(",0 ,  ");
			insert_SA61_1.append("'").append(logedInUser).append("', CURRENT TIMESTAMP ) ");
			
			StringBuilder insert_SA61_2 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SA61");
			insert_SA61_2.append(" (INVOICENR , DESCRIPTION, INVOICEDQTY,  TOTALNETVALLINEITEM, STATE , CREATED_BY, CREATED_TS )");
			insert_SA61_2.append(" VALUES (");
			insert_SA61_2.append(auditInvoiceObj.getInvoiceNumber()).append(",");
			insert_SA61_2.append(" 'Teilekosten mit 19% MwSt.' , 1 ,");
			insert_SA61_2.append(auditInvoiceObj.getPartsCostsWith19PerVAT());
			insert_SA61_2.append(", 0 , ");
			insert_SA61_2.append("'").append(logedInUser).append("', CURRENT TIMESTAMP )");
			
			log.info("Query:"+insert_SA61_1.toString());
			log.info("Query:"+insert_SA61_2.toString());
			
			stmt.addBatch(insert_SA61_1.toString());
			stmt.addBatch(insert_SA61_2.toString());
			
			int[] sa61_records = dbServiceRepository.insertResultUsingBatchQuery(stmt);
			
			if (sa61_records != null) {
				log.info("In O_ADDCOSTS - Total rows Inserted  :" + sa61_records.length);
			}
			
			con.setAutoCommit(true);
			con.commit();
			outputFlag.put("isInvoiceCreatedSuccessfully", true);

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.CREATE_FAILED_MSG_KEY, "Manually invoice"));
			log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY,
					"Manually invoice"), exception);
			throw exception;
		}

		return outputFlag;
	}

	private void insertValueIn_ADDCOSTS(Statement stmt, AuditInvoiceDTO auditInvoiceObj, String schema) throws SQLException {
		
		if(NumberUtils.isParsable(auditInvoiceObj.getPacking())) {
			StringBuilder insert_addCosts = new StringBuilder(" INSERT INTO ").append(schema).append(".O_ADDCOSTS ");
			insert_addCosts.append(" (INVOICENR, TEXT , ACCOUNT ,COSTSTYPE , COSTSVALUE )");
			insert_addCosts.append(" VALUES (");
			insert_addCosts.append("'").append(auditInvoiceObj.getInvoiceNumber()).append("',");
			insert_addCosts.append("'").append(auditInvoiceObj.getPackingText()!=null?auditInvoiceObj.getPackingText():"").append("', ");
			insert_addCosts.append("'").append(auditInvoiceObj.getPackingAccount()!=null?auditInvoiceObj.getPackingAccount():"").append("', ");
			insert_addCosts.append(" 'Packing' , ");
			insert_addCosts.append("'").append(auditInvoiceObj.getPacking()).append("' ");
			insert_addCosts.append(" ) ");
			log.info("Query:"+insert_addCosts.toString());
			stmt.addBatch(insert_addCosts.toString());
		}
		
		if(NumberUtils.isParsable(auditInvoiceObj.getFreightPostExpr())) {
			StringBuilder insert_addCosts = new StringBuilder(" INSERT INTO ").append(schema).append(".O_ADDCOSTS ");
			insert_addCosts.append(" (INVOICENR, TEXT , ACCOUNT ,COSTSTYPE , COSTSVALUE  )");
			insert_addCosts.append(" VALUES (");
			insert_addCosts.append("'").append(auditInvoiceObj.getInvoiceNumber()).append("',");
			insert_addCosts.append("'").append(auditInvoiceObj.getFreightPostExprText()!=null?auditInvoiceObj.getFreightPostExprText():"").append("', ");
			insert_addCosts.append("'").append(auditInvoiceObj.getFreightPostExprAccount()!=null?auditInvoiceObj.getFreightPostExprAccount():"").append("', ");
			insert_addCosts.append(" 'FreightPostExpr' , ");
			insert_addCosts.append("'").append(auditInvoiceObj.getFreightPostExpr()).append("' ");
			insert_addCosts.append(" ) ");
			log.info("Query:"+insert_addCosts.toString());
			stmt.addBatch(insert_addCosts.toString());
			}
		if(NumberUtils.isParsable(auditInvoiceObj.getFobCharges())) {
			StringBuilder insert_addCosts = new StringBuilder(" INSERT INTO ").append(schema).append(".O_ADDCOSTS ");
			insert_addCosts.append(" (INVOICENR, TEXT , ACCOUNT ,COSTSTYPE , COSTSVALUE )");
			insert_addCosts.append(" VALUES (");
			insert_addCosts.append("'").append(auditInvoiceObj.getInvoiceNumber()).append("',");
			insert_addCosts.append("'").append(auditInvoiceObj.getFobChargesText()!=null?auditInvoiceObj.getFobChargesText():"").append("', ");
			insert_addCosts.append("'").append(auditInvoiceObj.getFobChargesAccount()!=null?auditInvoiceObj.getFobChargesAccount():"").append("', ");
			insert_addCosts.append("'FobCharges' , ");
			insert_addCosts.append("'").append(auditInvoiceObj.getFobCharges()).append("' ");
			insert_addCosts.append(" ) ");
			log.info("Query:"+insert_addCosts.toString());
			stmt.addBatch(insert_addCosts.toString());
			}
		if(NumberUtils.isParsable(auditInvoiceObj.getSeaAirfright())) {
			StringBuilder insert_addCosts = new StringBuilder(" INSERT INTO ").append(schema).append(".O_ADDCOSTS ");
			insert_addCosts.append(" (INVOICENR, TEXT , ACCOUNT ,COSTSTYPE , COSTSVALUE )");
			insert_addCosts.append(" VALUES (");
			insert_addCosts.append("'").append(auditInvoiceObj.getInvoiceNumber()).append("',");
			insert_addCosts.append("'").append(auditInvoiceObj.getSeaAirfrightText()!=null?auditInvoiceObj.getSeaAirfrightText():"").append("', ");
			insert_addCosts.append("'").append(auditInvoiceObj.getSeaAirfrightAccount()!=null?auditInvoiceObj.getSeaAirfrightAccount():"").append("', ");
			insert_addCosts.append("'SeaAirfright' , ");
			insert_addCosts.append("'").append(auditInvoiceObj.getSeaAirfright()).append("' ");
			insert_addCosts.append(" ) ");
			log.info("Query:"+insert_addCosts.toString());
			stmt.addBatch(insert_addCosts.toString());
			}
		if(NumberUtils.isParsable(auditInvoiceObj.getInsurance())) {
			StringBuilder insert_addCosts = new StringBuilder(" INSERT INTO ").append(schema).append(".O_ADDCOSTS ");
			insert_addCosts.append(" (INVOICENR, TEXT , ACCOUNT ,COSTSTYPE , COSTSVALUE )");
			insert_addCosts.append(" VALUES (");
			insert_addCosts.append("'").append(auditInvoiceObj.getInvoiceNumber()).append("',");
			insert_addCosts.append("'").append(auditInvoiceObj.getInsuranceText()!=null?auditInvoiceObj.getInsuranceText():"").append("', ");
			insert_addCosts.append("'").append(auditInvoiceObj.getInsuranceAccount()!=null?auditInvoiceObj.getInsuranceAccount():"").append("', ");
			insert_addCosts.append("'Insurance' , ");
			insert_addCosts.append("'").append(auditInvoiceObj.getInsurance()).append("' ");
			insert_addCosts.append(" ) ");
			log.info("Query:"+insert_addCosts.toString());
			stmt.addBatch(insert_addCosts.toString());
			}
		if(NumberUtils.isParsable(auditInvoiceObj.getConfees())) {
			StringBuilder insert_addCosts = new StringBuilder(" INSERT INTO ").append(schema).append(".O_ADDCOSTS ");
			insert_addCosts.append(" (INVOICENR, TEXT , ACCOUNT ,COSTSTYPE , COSTSVALUE )");
			insert_addCosts.append(" VALUES (");
			insert_addCosts.append("'").append(auditInvoiceObj.getInvoiceNumber()).append("',");
			insert_addCosts.append("'").append(auditInvoiceObj.getConfeesText()!=null?auditInvoiceObj.getConfeesText():"").append("', ");
			insert_addCosts.append("'").append(auditInvoiceObj.getConfeesAccount()!=null?auditInvoiceObj.getConfeesAccount():"").append("', ");
			insert_addCosts.append("'Confees' , ");
			insert_addCosts.append("'").append(auditInvoiceObj.getConfees()).append("' ");
			insert_addCosts.append(" ) ");
			log.info("Query:"+insert_addCosts.toString());
			stmt.addBatch(insert_addCosts.toString());
			}
		if(NumberUtils.isParsable(auditInvoiceObj.getShipCharges())) {
			StringBuilder insert_addCosts = new StringBuilder(" INSERT INTO ").append(schema).append(".O_ADDCOSTS ");
			insert_addCosts.append(" (INVOICENR, TEXT , ACCOUNT ,COSTSTYPE , COSTSVALUE )");
			insert_addCosts.append(" VALUES (");
			insert_addCosts.append("'").append(auditInvoiceObj.getInvoiceNumber()).append("',");
			insert_addCosts.append("'").append(auditInvoiceObj.getShipChargesText()!=null?auditInvoiceObj.getShipChargesText():"").append("', ");
			insert_addCosts.append("'").append(auditInvoiceObj.getShipChargesAccount()!=null?auditInvoiceObj.getShipChargesAccount():"").append("', ");
			insert_addCosts.append("'ShipCharges' , ");
			insert_addCosts.append("'").append(auditInvoiceObj.getShipCharges()).append("' ");
			insert_addCosts.append(" ) ");
			log.info("Query:"+insert_addCosts.toString());
			stmt.addBatch(insert_addCosts.toString());
			}
		if(NumberUtils.isParsable(auditInvoiceObj.getContCharges())) {
			StringBuilder insert_addCosts = new StringBuilder(" INSERT INTO ").append(schema).append(".O_ADDCOSTS ");
			insert_addCosts.append(" (INVOICENR, TEXT , ACCOUNT ,COSTSTYPE , COSTSVALUE )");
			insert_addCosts.append(" VALUES (");
			insert_addCosts.append("'").append(auditInvoiceObj.getInvoiceNumber()).append("',");
			insert_addCosts.append("'").append(auditInvoiceObj.getContChargesText()!=null?auditInvoiceObj.getContChargesText():"").append("', ");
			insert_addCosts.append("'").append(auditInvoiceObj.getContChargesAccount()!=null?auditInvoiceObj.getContChargesAccount():"").append("', ");
			insert_addCosts.append("'ContCharges' , ");
			insert_addCosts.append("'").append(auditInvoiceObj.getContCharges()).append("' ");
			insert_addCosts.append(" ) ");
			log.info("Query:"+insert_addCosts.toString());
			stmt.addBatch(insert_addCosts.toString());
			}
		if(NumberUtils.isParsable(auditInvoiceObj.getOtherCharges())) {
			StringBuilder insert_addCosts = new StringBuilder(" INSERT INTO ").append(schema).append(".O_ADDCOSTS ");
			insert_addCosts.append(" (INVOICENR, TEXT , ACCOUNT ,COSTSTYPE , COSTSVALUE )");
			insert_addCosts.append(" VALUES (");
			insert_addCosts.append("'").append(auditInvoiceObj.getInvoiceNumber()).append("',");
			insert_addCosts.append("'").append(auditInvoiceObj.getOtherChargesText()!=null?auditInvoiceObj.getOtherChargesText():"").append("', ");
			insert_addCosts.append("'").append(auditInvoiceObj.getOtherChargesAccount()!=null?auditInvoiceObj.getOtherChargesAccount():"").append("', ");
			insert_addCosts.append("'OtherCharges' , ");
			insert_addCosts.append("'").append(auditInvoiceObj.getOtherCharges()).append("' ");
			insert_addCosts.append(" ) ");
			log.info("Query:"+insert_addCosts.toString());
			stmt.addBatch(insert_addCosts.toString());
			}
	}


	/**
	 * This method is used to import invoice data from O_ARPDC from DB
	 * @return Object
	 */
	@Override
	public Map<String, Boolean> importInvoiceDataFromO_ARPDC(String schema, String dataLibrary, String loginUserName ) {
		log.info("Inside importInvoiceDataFromO_ARPDC method of IncomingGoodsInvoiceServiceImpl");

		Map<String, Boolean> programOutput = new HashMap<String, Boolean>();
		programOutput.put("isSA53Updated", false);
		programOutput.put("isSA60Updated", false);
		programOutput.put("isSA61Updated", false);
		try(Connection con = dbServiceRepository.getConnectionObject();){
			con.setAutoCommit(false);

			String sa53SQL = importDataFromO_ARPDCToSA53(schema, dataLibrary, loginUserName, "DCAG");	
			String sa60SQL = importDataFromO_ARPDCToSA60(schema, dataLibrary, loginUserName, "DCAG");
			String sa61SQL = importDataFromO_ARPDCToSA61(schema, dataLibrary, loginUserName, "DCAG");


			boolean executionFlag53 = dbServiceRepository.excuteProcedure(sa53SQL.toString(), con);
			boolean executionFlag60 = dbServiceRepository.excuteProcedure(sa60SQL.toString(), con);
			boolean executionFlag61 = dbServiceRepository.excuteProcedure(sa61SQL.toString(), con);


			if(executionFlag53 && executionFlag60 && executionFlag61) {
				programOutput.put("isSA53Updated", true);
				programOutput.put("isSA60Updated", true);
				programOutput.put("isSA61Updated", true);
				con.setAutoCommit(true);
				con.commit();
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Import invoice data from O_ARPDC"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Import invoice data from O_ARPDC"), exception);
			throw exception;
		}
		return programOutput;
	}


	private String importDataFromO_ARPDCToSA53(String schema, String dataLibrary, String loginUserName,
			String manufacturer) {
		StringBuilder query = new StringBuilder("BEGIN DECLARE divisor INT; SET divisor=1;  ");
		query.append("FOR v CURSOR FOR ");
		query.append("select recordtype as mrecordtype, guid as mguid, companyCode53 as mcompanyCode, deliveryNoteNr53 as mdeliveryNoteNr, ");
		query.append("deliveryNoteLineItemNr53 as mdeliveryNoteLineItemNr, orderType53 as morderType, despatchMode53 as mdespatchMode, ");
		query.append("branchIndicator53 as mbranchIndicator, orderedPartNr53 as morderedPartNr, orderedQuantity53 as morderedQuantity, ");
		query.append("measureUnit53 as mmeasureUnit, customerOrderLineNr53 as mcustomerOrderLineNr, bhw53 as mbhw, ");
		query.append("deliveryNoteDate53 as mdeliveryNoteDate,customerOrderNr53 as mcustomerOrderNr,currencyCode53 as mcurrencyCode, ");
		query.append("promoId53 as mpromoId,informationIndicator53 as minformationIndicator,deliveredPartNr53 as mdeliveredPartNr, ");
		query.append("deliveredQuantity53 as mdeliveredQuantity,replacementCodes53 as mreplacementCodes, otherCodes53 as motherCodes, ");
		query.append("validListPrice53 as mvalidListPrice, netPriceUnit53 as mnetPriceUnit,tpopReferenceNetPriceLineItem53 as mtpopReferenceNetPriceLineItem, ");
		query.append("description53 as mdescription,keyCutNrMarkCode53 as mkeyCutNrMarkCode, salesPackage153 as msalesPackage1, salesPackage253 as msalesPackage2, ");
		query.append("recordVersion53 as mrecordVersion, unitWeight53 as munitWeight, volume53 as mvolume, svhcIndicator53 as msvhcIndicator, ");
		query.append("supplierLoc53 as msupplierLoc, dangerGoodsProfile53 as mdangerGoodsProfile, customsTariffNr53 as mcustomsTariffNr, ");
		query.append("countryNr53 as mcountryNr, discountGroupIndicator53 as mdiscountGroupIndicator, currencyDecIndicator53 as mcurrencyDecIndicator,");
		query.append("preferenceKey53 as mpreferenceKey ");
		query.append("from ");
		query.append("(SELECT  recordtype , guid, jt.* from ").append(schema).append(".o_arpdc, JSON_TABLE(payload, 'lax $'  ");
		query.append("	COLUMNS(	");
		query.append("companyCode53 INT path 'lax $.companyCode', deliveryNoteNr53 varchar(30) path 'lax $.deliveryNoteNr',");
		query.append("deliveryNoteLineItemNr53 INT path 'lax $.deliveryNoteLineItemNr', orderType53 INT path 'lax $.orderType', ");
		query.append("despatchMode53 INT path 'lax $.despatchMode', branchIndicator53  INT path 'lax $.branchIndicator', ");
		query.append("orderedPartNr53 VARCHAR(30) path 'lax $.orderedPartNr', orderedQuantity53 BIGINT path 'lax $.orderedQuantity', ");
		query.append("measureUnit53 BIGINT path 'lax $.measureUnit', customerOrderLineNr53 INT path 'lax $.customerOrderLineNr', ");
		query.append("bhw53 varchar(30) path 'lax $.bhw', deliveryNoteDate53 BIGINT path 'lax $.deliveryNoteDate', ");
		query.append("customerOrderNr53 INT path 'lax $.customerOrderNr', currencyCode53 VARCHAR(4) path 'lax $.currencyCode', ");
		query.append("promoId53 VARCHAR(1) path 'lax $.promoId', informationIndicator53 VARCHAR(4) path 'lax $.informationIndicator', ");
		query.append("deliveredPartNr53 varchar(30) path 'lax $.deliveredPartNr', deliveredQuantity53 BIGINT path 'lax $.deliveredQuantity', ");
		query.append("replacementCodes53 INT path 'lax $.replacementCodes', otherCodes53 INT path 'lax $.otherCodes', ");
		query.append("validListPrice53 BIGINT path 'lax $.validListPrice', netPriceUnit53 BIGINT path 'lax $.netPriceUnit', ");
		query.append("tpopReferenceNetPriceLineItem53 BIGINT path 'lax $.tpopReferenceNetPriceLineItem', description53 varchar(30) path 'lax $.description', ");
		query.append("keyCutNrMarkCode53 varchar(30) path 'lax $.keyCutNrMarkCode', salesPackage153 INT path 'lax $.salesPackage1', ");
		query.append("salesPackage253 INT path 'lax $.salesPackage2', recordVersion53 varchar(5) path 'lax $.recordVersion', ");
		query.append("unitWeight53 BIGINT path 'lax $.unitWeight', volume53 BIGINT path 'lax $.volume', ");
		query.append("svhcIndicator53 varchar(5) path 'lax $.svhcIndicator', supplierLoc53 INT path 'lax $.supplierLoc', ");
		query.append("dangerGoodsProfile53 varchar(10) path 'lax $.dangerGoodsProfile', customsTariffNr53 varchar(16) path 'lax $.customsTariffNr', ");
		query.append("countryNr53 INT path 'lax $.countryNr', discountGroupIndicator53 INT path 'lax $.discountGroupIndicator', ");
		query.append("currencyDecIndicator53 INT path 'lax $.currencyDecIndicator', preferenceKey53 varchar(5) path 'lax $.preferenceKey'	");
		query.append (") )  AS jt where recordtype='+53' AND STATE = 9 and guid not in (SELECT guid from   ").append(schema).append(".O_SA53)	");	
		query.append(" ) DO ");
		query.append("IF mcurrencyDecIndicator = 0 then SET divisor=100; END IF; ");
		query.append("INSERT INTO ").append(schema).append(".o_SA53  ");
		query.append( "( ");
		query.append(" guid , companyCode, deliveryNoteNr, deliveryNoteLineItemNr, orderType, despatchMode, branchIndicator, ");
		query.append("orderedPartNr, orderedQuantity, measureUnit, customerOrderLineNr, bhw, deliveryNoteDate, customerOrderNr, ");
		query.append(" currencyCode, promoId, informationIndicator, deliveredPartNr, deliveredQuantity, replacementCodes, ");
		query.append("otherCodes, validListPrice, netPriceUnit, tpopReferenceNetPriceLineItem, description, keyCutNrMarkCode, ");
		query.append("salesPackage1, salesPackage2, recordVersion, unitWeight, volume, svhcIndicator, supplierLoc, dangerGoodsProfile, ");
		query.append(" customsTariffNr, countryNr, discountGroupIndicator, currencyDecIndicator, preferenceKey, CREATED_BY, CREATED_TS, ORIGIN, OEM, AP_WRH ");
		query.append( ") ");
		query.append("VALUES ");
		query.append("( ");
		query.append(" mguid, mcompanyCode, replace (mdeliveryNoteNr,'+', ''), mdeliveryNoteLineItemNr, morderType, mdespatchMode, ");
		query.append("mbranchIndicator, morderedPartNr, cast ( cast( morderedQuantity as real) / 100 as real), mmeasureUnit,  mcustomerOrderLineNr, mbhw, ");
		query.append("cast( '20' || substr(mdeliveryNoteDate, 1, 2)||'-'|| substr(mdeliveryNoteDate, 3, 2)|| '-' ||  substr(mdeliveryNoteDate, 5, 2 ) as date ), ");
		query.append("mcustomerOrderNr, mcurrencyCode, mpromoId, 	minformationIndicator, mdeliveredPartNr, ");
		query.append("cast ( cast( mdeliveredQuantity as real) / 100 as real), mreplacementCodes, motherCodes, ");
		query.append("cast (cast ( mvalidListPrice as real) / divisor as real), cast (cast ( mnetPriceUnit as real) / divisor as real), ");
		query.append("cast (cast ( mtpopReferenceNetPriceLineItem as real) / divisor as real), mdescription, trim(mkeyCutNrMarkCode), ");
		query.append("cast ( cast( msalesPackage1 as real) / 100 as real), cast ( cast( msalesPackage2 as real) / 100 as real), ");
		query.append(" mrecordVersion, cast ( cast( munitWeight as real) / 10000 as real), cast ( cast( mvolume as real) / 1000 as real), ");
		query.append("msvhcIndicator, msupplierLoc, mdangerGoodsProfile, mcustomsTariffNr, mcountryNr, mdiscountGroupIndicator, mcurrencyDecIndicator, mpreferenceKey, '");
		query.append(loginUserName).append("', ");
		query.append(" Current timestamp , 0 , '").append(manufacturer).append("', ");
		query.append("(select LKD_LNR from ").append(schema).append(".PMH_LKDNR WHERE LKD_LKDNR=right(mcompanyCode, 6)) ");
		query.append(");	");
		query.append("	END FOR;	");			
		query.append("	END ");

		return query.toString();
	}


	private String importDataFromO_ARPDCToSA60(String schema, String dataLibrary, String loginUserName, String manufacturer) {

		StringBuilder query = new StringBuilder("BEGIN DECLARE divisor INT; SET divisor=1;  ");
		query.append("FOR v CURSOR FOR ");
		query.append("select recordtype as mrecordtype, guid as mguid, companyCode60 as mcompanyCode, deliveryNoteNr60 as mdeliveryNoteNr, supplierLoc60 as msupplierLoc, ");
		query.append("orderType60 as morderType, despatchMode60 as mdespatchMode, branchIndicator60 as mbranchIndicator, deliveryTerms60 as mdeliveryTerms, ");
		query.append("paymentTerms60 as mpaymentTerms, custVatId60 as mcustVatId, dcagVatId60 as mdcagVatId, shipToCustomerNr60 as mshipToCustomerNr, ");
		query.append("currencyCode60 as mcurrencyCode, snpRetension60 as msnpRetension, taxBaseValue60 as mtaxBaseValue, taxValue60 as mtaxValue, vatCode60 as mvatCode, ");
		query.append("invoiceDate60 as minvoiceDate, packing60 as mpacking,	freightPostExpr60 as mfreightPostExpr,	fobCharges60 as mfobCharges,  seaAirFreight60 as mseaAirFreight,  ");
		query.append("insurance60 as minsurance, consFees60 as mconsFees,  shippCharges60 as mshippCharges, offDocNoCnc60 as moffDocNoCnc, contCharges60 as mcontCharges, ");
		query.append("otherCharges60 as motherCharges, invoiceGrossValue60 as minvoiceGrossValue, invoiceNetValue60 as minvoiceNetValue, invoiceTotalAmount60 as minvoiceTotalAmount,  ");
		query.append("invoiceNr60 as minvoiceNr, currencyDecIndicator60 as mcurrencyDecIndicator, invoiceType60 as minvoiceType, invoiceCopyFlag60 as minvoiceCopyFlag	");
		query.append("from ");
		query.append("(SELECT  recordtype , guid, jt.* from ").append(schema).append(".o_arpdc, JSON_TABLE(payload, 'lax $' ");
		query.append("	COLUMNS(	");
		query.append("	companyCode60 INT path 'lax $.sa60.companyCode', ");
		query.append("	deliveryNoteNr60 varchar(30) path 'lax $.sa60.deliveryNoteNr', ");
		query.append("	supplierLoc60 INT path 'lax $.sa60.supplierLoc', ");
		query.append(" orderType60 INT path 'lax $.sa60.orderType',  ");
		query.append("	despatchMode60 INT path 'lax $.sa60.despatchMode', ");
		query.append("	branchIndicator60 INT path 'lax $.sa60.branchIndicator', ");
		query.append("	deliveryTerms60 INT path 'lax $.sa60.deliveryTerms', ");
		query.append("	paymentTerms60 INT path 'lax $.sa60.paymentTerms', ");
		query.append("	custVatId60 varchar(16) path 'lax $.sa60.custVatId', dcagVatId60 varchar(16) path 'lax $.sa60.dcagVatId',");
		query.append("	shipToCustomerNr60 INT path 'lax $.sa60.shipToCustomerNr', currencyCode60 varchar(4) path 'lax $.sa60.currencyCode', ");
		query.append("	snpRetension60 varchar(10) path 'lax $.sa60.snpRetension', taxBaseValue60 BIGINT path 'lax $.sa60.taxBaseValue',	");
		query.append(" taxValue60 BIGINT path 'lax $.sa60.taxValue', vatCode60 INT path 'lax $.sa60.vatCode', invoiceDate60 INT path 'lax $.sa60.invoiceDate', ");
		query.append(" packing60 BIGINT path 'lax $.sa60.packing',	freightPostExpr60 BIGINT path 'lax $.sa60.freightPostExpr',	fobCharges60 BIGINT path 'lax $.sa60.fobCharges', ");
		query.append(" seaAirFreight60 BIGINT path 'lax $.sa60.seaAirFreight', insurance60 BIGINT path 'lax $.sa60.insurance', consFees60 BIGINT path 'lax $.sa60.consFees',  ");
		query.append(" shippCharges60 BIGINT path 'lax $.sa60.shippCharges', offDocNoCnc60 BIGINT path 'lax $.sa60.offDocNoCnc', contCharges60 BIGINT path 'lax $.sa60.contCharges',  ");
		query.append(" otherCharges60 BIGINT path 'lax $.sa60.otherCharges', invoiceGrossValue60 BIGINT path 'lax $.sa60.invoiceGrossValue', ");
		query.append(" invoiceNetValue60 BIGINT path 'lax $.sa60.invoiceNetValue',  invoiceTotalAmount60 BIGINT path 'lax $.sa60.invoiceTotalAmount', ");
		query.append(" invoiceNr60 varchar(30) path 'lax $.sa60.invoiceNr', currencyDecIndicator60 INT path 'lax $.sa60.currencyDecIndicator',  ");
		query.append(" invoiceType60 varchar(2) path 'lax $.sa60.invoiceType', invoiceCopyFlag60 varchar(5) path 'lax $.sa60.invoiceCopyFlag'	");
		query.append (") ");
		query.append (")  AS jt  where recordtype='+60' AND STATE = 9 and guid not in (SELECT guid from ").append(schema).append(".O_SA60)	");	
		query.append("	)"); 
		query.append("	DO ");

		query.append(" IF mcurrencyDecIndicator = 0 then  SET divisor=100;  END	IF; ");
		query.append("INSERT INTO ").append(schema).append(".o_SA60  ");
		query.append( "( ");
		query.append("guid, companyCode, deliveryNoteNr, supplierLoc, orderType, despatchMode, branchIndicator, deliveryTerms, ");
		query.append("paymentTerms, custVatId, dcagVatId, shipToCustomerNr, currencyCode, snpRetension, taxBaseValue, taxValue,");
		query.append("vatCode, invoiceDate, packing, freightPostExpr, fobCharges,	seaAirFreight, insurance, consFees, shippCharges,");
		query.append("offDocNoCnc, contCharges, otherCharges, invoiceGrossValue, invoiceNetValue, invoiceTotalAmount, invoiceNr, ");
		query.append("currencyDecIndicator, invoiceType, invoiceCopyFlag, CREATED_BY, CREATED_TS, ORIGIN, OEM, AP_WRH ");
		query.append( ") ");
		query.append("VALUES ");
		query.append("( ");
		query.append(" mguid, mcompanyCode, replace (mdeliveryNoteNr, '+', '' ), msupplierLoc, morderType, ");
		query.append("mdespatchMode,	mbranchIndicator, mdeliveryTerms, mpaymentTerms, mcustVatId, mdcagVatId, ");
		query.append("mshipToCustomerNr, mcurrencyCode, msnpRetension, ");
		query.append("cast(cast (mtaxBaseValue as real) / divisor as real), cast (cast (mtaxValue as real) / divisor as real), ");
		query.append("cast (cast (mvatCode as real) / 10  as real),	  ");
		query.append("cast( '20' || substr(minvoiceDate, 1, 2)||'-'|| substr(minvoiceDate, 3, 2)|| '-' || substr(minvoiceDate, 5, 2 ) as date ),  ");
		query.append("cast (cast (mpacking as real) / divisor as real), cast (cast (mfreightPostExpr as real) / divisor as real), ");
		query.append("cast (cast (mfobCharges as real) / divisor as real), cast (cast (mseaAirFreight as real) / divisor as real), ");
		query.append("cast (cast (minsurance as real) / divisor as real), cast (cast (mconsFees as real) / divisor as real), ");
		query.append("cast (cast (mshippCharges as real) / divisor as real), moffDocNoCnc, ");
		query.append("cast (cast (mcontCharges as real) / divisor as real), cast (cast (motherCharges as real) / divisor as real), ");
		query.append("cast (cast (minvoiceGrossValue as real) / divisor as real), cast (cast (minvoiceNetValue as real) / divisor as real), ");
		query.append("cast (cast (minvoiceTotalAmount as real) / divisor as real),  replace (minvoiceNr, '+', '' ), ");
		query.append("mcurrencyDecIndicator, minvoiceType, minvoiceCopyFlag, '");
		query.append(loginUserName).append("', ");
		query.append(" Current timestamp , 0 , '").append(manufacturer).append("', ");
		query.append("(select LKD_LNR from ").append(schema).append(".PMH_LKDNR WHERE LKD_LKDNR=right(mcompanyCode, 6)) ");
		query.append(");	");
		query.append("	END FOR;	");			
		query.append("	END ");

		return query.toString();

	}


	private String importDataFromO_ARPDCToSA61(String schema, String dataLibrary, String loginUserName,
			String manufacturer) {
		StringBuilder query = new StringBuilder("BEGIN DECLARE divisor INT; SET divisor=1;  ");
		query.append("FOR v CURSOR FOR ");
		query.append("select recordtype as mrecordtype, guid as mguid, nested_ordinality as mnested_ordinality, companyCode61 as mcompanyCode,  ");
		query.append("deliveryNoteNr61 as mdeliveryNoteNr, deliveryNoteLineNr61 as mdeliveryNoteLineNr, orderType61 as morderType,  ");
		query.append("despatchMode61 as mdespatchMode, branchIndicator61 as mbranchIndicator, deliveredPartNr61 as mdeliveredPartNr, ");
		query.append("invoicedQty61 as minvoicedQty, measureUnit61 as mmeasureUnit, customerOrderLineNr61 as mcustomerOrderLineNr,  ");
		query.append("bhw61 as mbhw, cmopIndicator61 as mcmopIndicator, deliveryNoteDate61 as mdeliveryNoteDate,   ");
		query.append("customerOrderNr61 as mcustomerOrderNr,	currencyCode61 as mcurrencyCode, orderedPartNr61 as morderedPartNr,  ");
		query.append("customsTariffNr61 as mcustomsTariffNr, vatCode61 as mvatCode, unitPrice61 as munitPrice,  ");
		query.append("validListPriceUnit61 as mvalidListPriceUnit, invoiceDate61 as minvoiceDate,	unitWeightLineItem61 as munitWeightLineItem,	");
		query.append("description61 as mdescription, veDocUserId61 as mveDocUserId, otherCharges61 as motherCharges, 	");
		query.append(" recordVersion61 as mrecordVersion, totalValueLineItem61 as mtotalValueLineItem,	totalNetValLineItem61 as mtotalNetValLineItem, ");
		query.append("countryNr61 as mcountryNr,	invoiceNr61 as minvoiceNr, discountGroupIndicator61 as mdiscountGroupIndicator,	 ");
		query.append("currencyDecIndicator61 as mcurrencyDecIndicator, preferenceKey61 as mpreferenceKey ");
		query.append("from ");
		query.append("(SELECT  recordtype , guid, jt.* from  ").append(schema).append(".o_arpdc, JSON_TABLE(payload, 'lax $' ");
		query.append("	COLUMNS(	");
		query.append("NESTED PATH 'lax $.sa61[*]' ");
		query.append("	COLUMNS(	");
		query.append("nested_ordinality FOR ORDINALITY, ");
		query.append("companyCode61 INT path 'lax $.companyCode',  ");
		query.append("deliveryNoteNr61 varchar(30) path 'lax $.deliveryNoteNr', ");
		query.append("deliveryNoteLineNr61 INT path 'lax $.deliveryNoteLineNr', orderType61 INT path 'lax $.orderType',  ");
		query.append("despatchMode61 INT path 'lax $.despatchMode',  branchIndicator61 BIGINT path 'lax $.branchIndicator', ");
		query.append("deliveredPartNr61 varchar(30) path 'lax $.deliveredPartNr',  invoicedQty61 BIGINT path 'lax $.invoicedQty', ");
		query.append("measureUnit61 INT path 'lax $.measureUnit',	 customerOrderLineNr61 INT path 'lax $.customerOrderLineNr', ");
		query.append("bhw61 varchar(30) path 'lax $.bhw', cmopIndicator61 varchar(5) path 'lax $.cmopIndicator',  ");
		query.append("deliveryNoteDate61 BIGINT path 'lax $.deliveryNoteDate',  customerOrderNr61 INT path 'lax $.customerOrderNr',	");
		query.append("currencyCode61 varchar(4) path 'lax $.currencyCode', orderedPartNr61 varchar(30) path 'lax $.orderedPartNr', ");
		query.append("customsTariffNr61 varchar(12) path 'lax $.customsTariffNr', vatCode61 BIGINT path 'lax $.vatCode', ");
		query.append("unitPrice61 BIGINT path 'lax $.unitPrice', validListPriceUnit61 INT path 'lax $.validListPriceUnit',");
		query.append("invoiceDate61 BIGINT path 'lax $.invoiceDate', unitWeightLineItem61 BIGINT path 'lax $.unitWeightLineItem',	");
		query.append("description61 varchar(30) path 'lax $.description', veDocUserId61 varchar(30) path 'lax $.veDocUserId',");
		query.append("otherCharges61 BIGINT path 'lax $.otherCharges', recordVersion61 INT path 'lax $.recordVersion',	");
		query.append("totalValueLineItem61 BIGINT path 'lax $.totalValueLineItem', totalNetValLineItem61 BIGINT path 'lax $.totalNetValLineItem', ");
		query.append("countryNr61 INT path 'lax $.countryNr', invoiceNr61 varchar(30) path 'lax $.invoiceNr',");
		query.append("discountGroupIndicator61 INT path 'lax $.discountGroupIndicator', currencyDecIndicator61 INT path 'lax $.currencyDecIndicator',");
		query.append("preferenceKey61 varchar(5) path 'lax $.preferenceKey'  ) ) )");
		query.append (" AS jt  where recordtype='+60' AND STATE = 9 and guid not in (SELECT guid from ").append(schema).append(".O_SA61) ");	
		query.append("	)"); 
		query.append("	DO ");
		query.append(" IF mcurrencyDecIndicator = 0 then SET divisor=100; END IF; ");
		query.append("INSERT INTO ").append(schema).append(".o_SA61  ");
		query.append( "( ");
		query.append("guid, nested_ordinality, companyCode, deliveryNoteNr, deliveryNoteLineNr, orderType, despatchMode, branchIndicator, ");
		query.append("deliveredPartNr, invoicedQty, measureUnit, customerOrderLineNr, bhw, cmopIndicator, deliveryNoteDate, customerOrderNr,");
		query.append("currencyCode, orderedPartNr, customsTariffNr, vatCode, unitPrice, validListPriceUnit, invoiceDate, unitWeightLineItem,");
		query.append("description, veDocUserId, otherCharges, recordVersion, totalValueLineItem, totalNetValLineItem, countryNr, invoiceNr, ");
		query.append("discountGroupIndicator,	currencyDecIndicator, preferenceKey, CREATED_BY, CREATED_TS, ORIGIN, OEM, AP_WRH ");
		query.append( ") ");
		query.append("VALUES ");
		query.append("( ");
		query.append("mguid, mnested_ordinality, mcompanyCode, replace (mdeliveryNoteNr, '+', ''),");
		query.append("mdeliveryNoteLineNr, morderType, mdespatchMode, mbranchIndicator, mdeliveredPartNr,  ");
		query.append("cast( cast( minvoicedQty as real )/ 100 as real), mmeasureUnit,	mcustomerOrderLineNr, mbhw,mcmopIndicator,  ");
		query.append("cast( '20' || substr(mdeliveryNoteDate, 1, 2)||'-'|| substr(mdeliveryNoteDate, 3, 2)|| '-' ||  substr(mdeliveryNoteDate, 5, 2 ) as date ), ");
		query.append("mcustomerOrderNr,mcurrencyCode,morderedPartNr, mcustomsTariffNr, ");
		query.append("cast ( cast( mvatCode as real) / 10 as real), cast ( cast (munitPrice as real) / divisor as real), mvalidListPriceUnit, ");
		query.append("cast( '20' || substr(minvoiceDate, 1, 2)||'-'|| substr(minvoiceDate, 3, 2)|| '-' || substr(minvoiceDate, 5, 2 ) as date ), ");
		query.append("cast( cast(munitWeightLineItem as real) / 1000 as real), mdescription,mveDocUserId, ");
		query.append("cast( cast(motherCharges as real) / divisor as real), mrecordVersion,	 ");
		query.append("cast( cast(mtotalValueLineItem as real) / divisor as real), cast( cast(mtotalNetValLineItem as real) / divisor as real), mcountryNr,	");
		query.append("replace (minvoiceNr, '+', '' ),  mdiscountGroupIndicator,mcurrencyDecIndicator, mpreferenceKey, '");
		query.append(loginUserName).append("', ");
		query.append(" Current timestamp , 0 , '").append(manufacturer).append("', ");
		query.append("(select LKD_LNR from ").append(schema).append(".PMH_LKDNR WHERE LKD_LKDNR=right(mcompanyCode, 6)) ");
		query.append(");	");
		query.append("	END FOR;	");			
		query.append("	END ");

		return query.toString();
	}


	/**
	 * This method is used to get the overview of Invoices data List from O_SA60 table.
	 */
	@Override
	public List<InvoiceOverviewDTO> getOverviewOfInvoiceList(String dataLibrary, String schema, String warehouseNo) {

		log.info("Inside getOverviewOfInvoiceList method of IncomingGoodsInvoiceServiceImpl");
		List<InvoiceOverviewDTO> invoiceOverviewList = new ArrayList<InvoiceOverviewDTO>();

		try {
			StringBuilder invoiceListQuery = new StringBuilder("Select ID, AP_WRH, INVOICENR, VARCHAR_FORMAT(INVOICEDATE, 'DD-MM-YYYY') AS INVOICEDATE, OEM, TAXBASEVALUE, INVOICENETVALUE, ");
			invoiceListQuery.append("(PACKING + FREIGHTPOSTEXPR + FOBCHARGES + SEAAIRFREIGHT + INSURANCE + CONSFEES + SHIPPCHARGES + CONTCHARGES + OTHERCHARGES) AS EXTRACOST, ");
			invoiceListQuery.append("(Select SUM((cast(substr (T1, 3, 6) as real) / 10) * (cast(substr (T1, 35, 7) as real) / 100)) from ");
			invoiceListQuery.append(dataLibrary).append(".E_CPSDAT cps Where cps.BELNR IN (Select distinct DELIVERYNOTENR from ");
			invoiceListQuery.append(schema).append(".O_SA61 osa61 Where osa61.INVOICENR = osa60.INVOICENR) AND (cps.BART=1 or cps.BART=2)) AS BOOKED_AMT, ");
			invoiceListQuery.append("(Select SUM(COSTSVALUE) from ").append(schema).append(".O_ADDCOSTS WHERE INVOICENR = osa60.INVOICENR) AS ADDCOSTTOTAL from ");
			invoiceListQuery.append(schema).append(".O_SA60 osa60 Where AP_WRH = ").append(warehouseNo).append(" order by INVOICENR ");

			List<SA60Model> invoiceList = dbServiceRepository.getResultUsingQuery(SA60Model.class, invoiceListQuery.toString(), true);

			if (invoiceList != null && !invoiceList.isEmpty()) {

				for(SA60Model invoiceValue : invoiceList) {

					InvoiceOverviewDTO invoiceOverview = new InvoiceOverviewDTO();

					invoiceOverview.setInvoiceId(String.valueOf(invoiceValue.getSa60Id()));
					invoiceOverview.setInvoiceNo(invoiceValue.getInvoiceNo());
					invoiceOverview.setMaufacturer(invoiceValue.getMaufacturer().equalsIgnoreCase(RestInputConstants.DCAG_STRING) ? 
							RestInputConstants.DAG_STRING : invoiceValue.getMaufacturer());
					invoiceOverview.setAp_WarehouseNo(StringUtils.leftPad(invoiceValue.getAp_WarehouseNo(), 2, "0"));
					invoiceOverview.setInvoiceDate(invoiceValue.getInvoiceDate() !=null ? invoiceValue.getInvoiceDate() : "");
					invoiceOverview.setTaxBaseValue(invoiceValue.getTaxBaseValue() != null ? decimalformat_twodigit.format(invoiceValue.getTaxBaseValue().doubleValue()) : "0.00");
					invoiceOverview.setInvoiceNetValue(invoiceValue.getInvoiceNetValue() != null ? decimalformat_twodigit.format(invoiceValue.getInvoiceNetValue()) : "0.00");
					invoiceOverview.setExtraCost(invoiceValue.getExtraCost()!=null ? decimalformat_twodigit.format(invoiceValue.getExtraCost()) : "0.00");
					invoiceOverview.setBookedAmount(invoiceValue.getBookedAmount()!=null ? decimalformat_twodigit.format(invoiceValue.getBookedAmount()) : "0.00");
					invoiceOverview.setAddCostTotal(invoiceValue.getAddCostTotal()!=null ? decimalformat_twodigit.format(invoiceValue.getAddCostTotal()) : "0.00");

					//status is set based on some conditions
					invoiceOverview.setInvoiceStatus(calculateStatus(invoiceValue));

					invoiceOverviewList.add(invoiceOverview);
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(
					LocaleContextHolder.getLocale(), ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Invoice list"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Invoice list"),exception);
			throw exception;
		}

		return invoiceOverviewList;
	}


	/*
	 * Green -- Everything OK
		  // taxBaseValue = extraCost + bookedAmount; extraCost = addCostTotal; 	bookedAmount = invoiceNetVal; 

	   Yellow -- Not (fully) booked yet
		  // bookedAmount != invoiceNetVal;  extraCost = addCostTotal;		

	   Orange -- allocate additional costs
		  //  taxBaseValue = extraCost + bookedAmount;	extraCost != addCostTotal; 	  bookedAmount = invoiceNetVal;		

	   Red -- Part price differs and/or additional costs
		 //  taxBaseValue != extraCost + bookedAmount;		extraCost != addCostTotal;
	 */
	private String calculateStatus(SA60Model invoiceValue) {
		String status = "";

		Double taxBaseValue = invoiceValue.getTaxBaseValue() != null ? invoiceValue.getTaxBaseValue().doubleValue() : 0.00;
		Double extraCost = invoiceValue.getExtraCost()!=null ? invoiceValue.getExtraCost().doubleValue() : 0.00;
		Double bookedAmount = invoiceValue.getBookedAmount()!=null ? invoiceValue.getBookedAmount().doubleValue() : 0.00;
		Double addCostTotal = invoiceValue.getAddCostTotal()!=null ? invoiceValue.getAddCostTotal().doubleValue() : 0.00;
		Double invoiceNetVal = invoiceValue.getInvoiceNetValue() != null ? invoiceValue.getInvoiceNetValue().doubleValue() : 0.00; 

		//conditions
		if((taxBaseValue.equals(extraCost + bookedAmount)) && (bookedAmount.equals(invoiceNetVal)) && (extraCost.equals(addCostTotal))) {			
			status = "Green";
		}
		else if((taxBaseValue.equals(extraCost + bookedAmount)) && (bookedAmount.equals(invoiceNetVal)) && !(extraCost.equals(addCostTotal))) {			
			status = "Orange";
		}
		else if(!(taxBaseValue.equals(extraCost + bookedAmount)) && !(extraCost.equals(addCostTotal))) {			
			status = "Red";
		}
		else if(!(bookedAmount.equals(invoiceNetVal)) && (extraCost.equals(addCostTotal))) {			
			status = "Yellow";
		}
		else {
			status = "--";
		}

		return status;
	}


	/**
	 * This is method is used to get the unassigned list of delivery Notes from DB .
	 */
	@Override
	public List<AuditAssignDeliveryNotesDTO> getAudit_NotAssignedDeliveryNotes(String schema, String dataLibrary,
			String manufacturer, String warehouse) {
		// TODO Auto-generated method stub
		log.info("Inside getAudit_NotAssignedDeliveryNotes method of IncomingGoodsInvoiceServiceImpl");
		List<AuditAssignDeliveryNotesDTO> deliveryNoteDTO = new ArrayList<>();
		try {
			manufacturer = manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING: manufacturer;
			StringBuilder query = new StringBuilder(" WITH total_price AS ( SELECT belnr AS deliverynotenr , SUM(CAST(SUBSTR(t1, 3, 6) AS REAL) / 10 * CAST(SUBSTR(t1, 9, 7) AS REAL) / 100) AS PREIS FROM ");
			query.append(dataLibrary).append(".e_cpsdat WHERE VFNR = ").append(warehouse);
			query.append(" AND (bart = 1 OR bart = 2)  AND HERST=  '").append(manufacturer).append("'");
			query.append(" and belnr not in (select DELIVERYNOTENR from ").append(schema).append(".O_SA61 ) GROUP BY belnr)SELECT deliverynotenr,PREIS FROM total_price  ");

			List<AuditAssignDeliveryNotes> deliveryNote = dbServiceRepository.getResultUsingQuery(AuditAssignDeliveryNotes.class, query.toString(), true);
			if(deliveryNote != null && !deliveryNote.isEmpty()) {
				for(AuditAssignDeliveryNotes notMovementData : deliveryNote ) {
					AuditAssignDeliveryNotesDTO deliveryDTO = new AuditAssignDeliveryNotesDTO();

					deliveryDTO.setDeliveryNote(notMovementData.getDeliveryNote());	
					deliveryDTO.setDeliveryNotePrice(notMovementData.getDeliveryNoteprice());

					deliveryNoteDTO.add(deliveryDTO);
				}
			}//list of delivery Notes that are not assigned to invoice
			else{
				log.info("list of delivery Notes is not available ");
			} 

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "list of delivery Notes is not available"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"list of delivery Notes is not available"), exception);
			throw exception;
		}

		return deliveryNoteDTO;
	}
	/**
	 * This is method is used to get the assigned list of delivery Notes from DB .
	 */

	@Override
	public List<AuditAssignDeliveryNotesDTO> getAudit_AssignedDeliveryNotes(String schema, String dataLibrary,
			String manufacturer, String invoiceNumber) {
		// TODO Auto-generated method stub
		log.info("Inside getAudit_AssignedDeliveryNotes method of IncomingGoodsInvoiceServiceImpl");
		List<AuditAssignDeliveryNotesDTO> assignDeliveryNoteDTO = new ArrayList<>();

		try {
			manufacturer = manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING: manufacturer;
			StringBuilder query = new StringBuilder(
					"WITH extracted_data AS (SELECT deliverynotenr,sum((totalnetvallineitem /invoicedqty)* invoicedqty) AS PREIS FROM ")
					.append(schema).append(".o_sa61");
			query.append(" WHERE  deliverynotenr in (select distinct deliverynotenr from  ").append(schema)
			.append(".o_sa61 ").append(" where invoicenr ='").append(invoiceNumber).append("'");
			query.append(" ) AND OEM= '").append(manufacturer).append("'");
			query.append(" GROUP BY deliverynotenr)");
			query.append(" SELECT deliverynotenr,PREIS FROM extracted_data ORDER BY deliverynotenr");

			List<AuditAssignDeliveryNotes> AssigndeliveryNote = dbServiceRepository.getResultUsingQuery(AuditAssignDeliveryNotes.class, query.toString(), true);
			if (AssigndeliveryNote != null && !AssigndeliveryNote.isEmpty()) {
				for (AuditAssignDeliveryNotes AssignMovementData : AssigndeliveryNote) {
					AuditAssignDeliveryNotesDTO AssigndeliveryDTO = new AuditAssignDeliveryNotesDTO();
					AssigndeliveryDTO.setDeliveryNote(AssignMovementData.getDeliveryNote());
					AssigndeliveryDTO.setDeliveryNotePrice(AssignMovementData.getDeliveryNoteprice());

					assignDeliveryNoteDTO.add(AssigndeliveryDTO);
				}
			} else {
				log.info("list of delivery Notes is not available ");
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,
					messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "list of delivery Notes is not available"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"list of delivery Notes is not available"), exception);
			throw exception;
		}

		return assignDeliveryNoteDTO;
	}


	/**
	 * This method is used to get the overview of Invoices data List from O_SA60 table.
	 */
	@Override
	public List<InvoiceOverviewDTO> getInvoiceDetailList(String dataLibrary, String schema, String invoiceNo, String warehouseNo, String manufacturer) {

		log.info("Inside getInvoiceDetailList method of IncomingGoodsInvoiceServiceImpl");
		List<InvoiceOverviewDTO> invoiceOverviewList = new ArrayList<InvoiceOverviewDTO>();

		try {
			manufacturer = manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING) ? RestInputConstants.DCAG_STRING : manufacturer;

			StringBuilder invoiceDetListQuery = new StringBuilder("SELECT DELIVERYNOTENR, ");
			invoiceDetListQuery.append("cast (sum(TOTALNETVALLINEITEM) as NUMERIC (10,2)) AS  TOTALNETVALLINEITEM, ");
			invoiceDetListQuery.append("(Select SUM((cast(substr (T1, 3, 6) as real) / 10) * (cast(substr (T1, 35, 7) as real) / 100)) from ");
			invoiceDetListQuery.append(dataLibrary).append(".E_CPSDAT cps Where cps.BELNR = sa.DELIVERYNOTENR AND (cps.BART=1 or cps.BART=2) ");
			invoiceDetListQuery.append(" AND cps.HERST='").append(manufacturer).append("' AND cps.VFNR=").append(warehouseNo).append(") AS LIEFERSCHEINSUMME ");
			invoiceDetListQuery.append(" from ");
			invoiceDetListQuery.append(schema).append(".O_SA61 sa Where sa.INVOICENR = ").append(invoiceNo).append(" group by sa.DELIVERYNOTENR ");

			List<SA61Model> invoiceDetailList = dbServiceRepository.getResultUsingQuery(SA61Model.class, invoiceDetListQuery.toString(), true);

			if (invoiceDetailList != null && !invoiceDetailList.isEmpty()) {

				for(SA61Model invoiceValue : invoiceDetailList) {

					InvoiceOverviewDTO invoiceOverview = new InvoiceOverviewDTO();

					invoiceOverview.setInvoiceNo(invoiceNo);
					invoiceOverview.setMaufacturer(manufacturer.equalsIgnoreCase(RestInputConstants.DCAG_STRING) ? 
							RestInputConstants.DAG_STRING : manufacturer);
					invoiceOverview.setAp_WarehouseNo(StringUtils.leftPad(warehouseNo, 2, "0"));
					invoiceOverview.setDeliveryNoteNo(invoiceValue.getDeliveryNoteNo() != null ? invoiceValue.getDeliveryNoteNo() : "");
					invoiceOverview.setTotalNetValLineItem(invoiceValue.getTotalNetValLineItem() != null ? decimalformat_twodigit.format(invoiceValue.getTotalNetValLineItem()) : "0.00");
					invoiceOverview.setDeliveryNoteTotal(invoiceValue.getDeliveryNoteTotal() != null ? decimalformat_twodigit.format(invoiceValue.getDeliveryNoteTotal()) : "0.00");

					invoiceOverviewList.add(invoiceOverview);
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(
					LocaleContextHolder.getLocale(), ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Invoice Detail List"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Invoice Detail List"),exception);
			throw exception;
		}

		return invoiceOverviewList;
	}


	/**
	 * This method is used to get invoiced details and delivery note details for items of a delivery note.
	 * @return List
	 */
	@Override
	public List<BookedDeliveryNoteDetailsDTO> getBookedDeliveryNoteDetails(String dataLibrary,String schema, String deliveryNoteNo,
			String warehouseNo) {
	
		log.info("Inside getBookedDeliveryNoteDetails method of IncomingGoodsInvoiceServiceImpl");
		List<BookedDeliveryNoteDetailsDTO> bookedDeliveryNoteList = new  ArrayList<BookedDeliveryNoteDetailsDTO>();
		
		try {
			StringBuilder query = new StringBuilder("select deliverynotenr AS Lieferschein, deliverynotelinenr as POS, deliveredpartnr as TNR, ");
			query.append("description as Bezeichnung, invoicedqty as Menge_lt_Rechnung, vatcode as MwSt,");
			query.append(" cast ((totalnetvallineitem /invoicedqty) as numeric (10,2)) AS Stuckpreis_lt_Rechnung, ");
			query.append(" case STATE when 3 THEN OLDAMOUNT else  ");
			query.append(" (select (cast(substr (t1, 3, 6) as real) / 10) from ").append(dataLibrary).append(" .e_cpsdat where belnr=f.deliverynotenr ");
			query.append(" and etnr_s=f.deliveredpartnr and int(posnr) = int(f.deliverynotelinenr) and (bart=1 or bart=2) AND VFNR=").append(warehouseNo);
			query.append(" )  end AS  Gebuchte_Menge,  case STATE when 3 THEN OLDPRICE else  ");
			query.append(" (select (cast(substr (t1, 35, 7) as real) / 100) from ").append(dataLibrary).append(".e_cpsdat where belnr=f.deliverynotenr ");
			query.append(" and etnr_s=f.deliveredpartnr and int(posnr)= int(f.deliverynotelinenr)and (bart=1 or bart=2) AND VFNR=").append(warehouseNo);
			query.append(")  end as  Gebuchter_Preis, ORIGIN From ").append(schema).append(".o_sa61 f ");
			query.append(" where deliverynotenr = '").append(deliveryNoteNo).append("'");
	
			List<BookedDeliveryNoteDetails> deliveryNoteList = dbServiceRepository.getResultUsingQuery(BookedDeliveryNoteDetails.class, query.toString(), true);
	
			if (deliveryNoteList != null && !deliveryNoteList.isEmpty()) {
				
				for(BookedDeliveryNoteDetails deliveryNote : deliveryNoteList){
					BookedDeliveryNoteDetailsDTO deliveryNoteDTO = new BookedDeliveryNoteDetailsDTO();
					
					deliveryNoteDTO.setDeliveryNoteNo(deliveryNote.getDeliveryNoteNo());
					deliveryNoteDTO.setDeliveryNotePosition(deliveryNote.getDeliveryNotePosition());
					deliveryNoteDTO.setPartNumber(deliveryNote.getPartNumber());
					deliveryNoteDTO.setDesignation(deliveryNote.getDesignation());
					deliveryNoteDTO.setQuantityAccordingToInvoice(deliveryNote.getQuantityAccordingToInvoice());
					deliveryNoteDTO.setVat(deliveryNote.getVat());
					deliveryNoteDTO.setUnitPriceAccordingToInvoice(deliveryNote.getUnitPriceAccordingToInvoice());
					deliveryNoteDTO.setPostedAmount(deliveryNote.getPostedAmount());
					deliveryNoteDTO.setBookedPrice(deliveryNote.getBookedPrice());
					deliveryNoteDTO.setOrigin(deliveryNote.getOrigin());
					
					bookedDeliveryNoteList.add(deliveryNoteDTO);
				}
			}else{
				log.info("Invoiced details and delivery note details is empty");
			} 
			
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Booked delivery note details"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Booked delivery note details"), exception);
			throw exception;
		}
		
		return bookedDeliveryNoteList;
	}


	/**
	 * This method is used to update the delivery note as Approved.
	 * @return List
	 */
	@Override
	public Map<String, Boolean> updateInvoiceDeliveryNoteAsApproved(DeliveryNotes_BA_DTO ba_dto,
			String dataLibrary, String loginUserName, String schema) {
		Map<String, Boolean> updateFlag = new HashMap<String, Boolean>();
		//updateFlag.put("isUpdated", false);
		updateFlag.put("isBSNLFSUpdated", false);
		updateFlag.put("isSA61Updated", false);
		updateFlag.put("isSA60Updated", false);
		try {
			if (ba_dto != null ) {


				StringBuilder queryForBSNLFS = new StringBuilder("UPDATE ").append(dataLibrary).append(".E_BSNLFS SET ");
				queryForBSNLFS.append(" ERLEDIGT  = 'J' WHERE HERST = '").append(ba_dto.getManufacturer()).append("' "); 
				queryForBSNLFS.append("AND LNR=").append(ba_dto.getWarehouseNumber());
				queryForBSNLFS.append(" AND BENDL=").append(ba_dto.getSupplierNumber());
				queryForBSNLFS.append(" AND NRX= '").append(ba_dto.getDeliveryNoteNo()).append("' ");
				queryForBSNLFS.append("AND ART= '").append(ba_dto.getProgramName()).append("' ");
				queryForBSNLFS.append("AND LAUFNR= '").append(ba_dto.getLaufnr()).append("' ");
				queryForBSNLFS.append("AND BA= '").append(ba_dto.getBusinessCase()).append("' ");
				queryForBSNLFS.append("AND REKOW= '").append(ba_dto.getRekow()).append("' ");



				boolean isBSNLFSUpdated = dbServiceRepository.updateResultUsingQuery(queryForBSNLFS.toString());
				if(isBSNLFSUpdated){
					updateFlag.put("isBSNLFSUpdated", true);
				}

				StringBuilder queryFoeSA61 = new StringBuilder("UPDATE ").append(schema).append(".O_SA61 SET ");
				queryFoeSA61.append(" APPROVED_BY  = ").append("'"+loginUserName+"'");
				queryFoeSA61.append("  , APPROVED_TS = CURRENT TIMESTAMP");
				queryFoeSA61.append(" where DELIVERYNOTENR ='").append(ba_dto.getDeliveryNoteNo()).append("' ");

				boolean isSA61Updated = dbServiceRepository.updateResultUsingQuery(queryFoeSA61.toString());
				if(isSA61Updated){
					updateFlag.put("isSA61Updated", true);
				}


				StringBuilder queryFoeSA60 = new StringBuilder("UPDATE ").append(schema).append(".O_SA60 SET ");
				queryFoeSA60.append(" APPROVED_BY  = ").append("'"+loginUserName+"'");
				queryFoeSA60.append(" , APPROVED_TS = CURRENT TIMESTAMP ");
				queryFoeSA60.append(" where DELIVERYNOTENR ='").append(ba_dto.getDeliveryNoteNo()).append("' ");

				boolean isSA60Updated = dbServiceRepository.updateResultUsingQuery(queryFoeSA60.toString());
				if(isSA60Updated){
					updateFlag.put("isSA60Updated", true);
				}
			}else {
				log.info("Delivery Note  is empty");

			}
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY,"Update delivery note as Approved"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY,"Update delivery note as Approved"), exception);
			throw exception;
		}

		return updateFlag;
	}


	/**
	 * This method is used to return the count of not Imported Invoice Item(O_sa60 & O_ARPDC) from DB.
	 * @return Object
	 */
	@Override
	public Map<String, String> getCountOfNotImportedInvoiceItems(String dataLibrary, String schema) {
		// TODO Auto-generated method stub
		log.info("Inside getCountOfNotImportedInvoiceItems method of IncomingGoodsInvoiceServiceImpl");
		String count = "0";
		Map<String,String> resultCounts = new HashMap<String, String>();
		//select count(*) as count from elsdb093.o_arpdc where guid not in (select guid from elsdb093.o_sa60) and recordtype='+60';
		try {

			StringBuilder query_InvoiceItemCount = new StringBuilder(" SELECT COUNT (*) AS COUNT FROM ").append(schema).append(".O_ARPDC ");
			query_InvoiceItemCount.append(" where GUID NOT IN (SELECT GUID from ").append(schema).append(" .o_sa60)");
			query_InvoiceItemCount.append(" AND RECORDTYPE = '+60'");

			count = dbServiceRepository.getCountUsingQuery(query_InvoiceItemCount.toString());
			resultCounts.put(RestInputConstants.QUERY_INVOICE_ITEM_COUNT, count);
		} 
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, " count of not Imported Invoice Item(O_sa60 & O_ARPDC) "));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "count of not Imported Invoice Item(O_sa60 & O_ARPDC)"), exception);
			throw exception;
		}

		return resultCounts;
	}


	

		
	/**
	 * This method is used to return the set/remove assigned delivery notes for an invoice from DB.
	 * @return Object
	 */
	@Override
	public Map<String, Boolean> setAssignDeliveryNoteForInvoice(List<InvoiceDeliveryNoteDTO> mappingObj,String schema, String dataLibrary,
			String loginUserName) {
		// code changes about task ALPHAX-4289 
		log.info("Inside setAssignDeliveryNoteForInvoice method of IncomingGoodsInvoiceServiceImpl");
		Map<String, Boolean> programOutput = new HashMap<String, Boolean>();
		programOutput.put("isProcessCompleted", false);
		try(Connection con = dbServiceRepository.getConnectionObject();
				Statement stmt = con.createStatement();){
			
			if (mappingObj != null && !mappingObj.isEmpty()) {
				con.setAutoCommit(false);
				

				for (InvoiceDeliveryNoteDTO MappingDTO : mappingObj) {
					if(MappingDTO.isDeletedFlag()) {

						StringBuilder deleteQuery = new StringBuilder("DELETE FROM ").append(schema).append(".O_SA61 WHERE DELIVERYNOTENR=").append("'"+MappingDTO.getDeliveryNoteNo()+"'");
						deleteQuery.append(" AND ORIGIN=2 ");

						 dbServiceRepository.deleteResultUsingQuery(deleteQuery.toString(),con);
					}else {
						StringBuilder query = new StringBuilder("BEGIN DECLARE divisor INT; FOR v CURSOR FOR   select  ");
						query.append("trim(belnr)||'-'||JJMMTT AS mGUID, trim(belnr) AS mDELIVERYNOTENR, int(posnr) AS mDELIVERYNOTELINENR, int(posnr) AS mNESTED_ORDINALITY ,ETNR_S AS mDELIVEREDPARTNR, ");
						query.append(" (select benen from ");
						query.append(dataLibrary).append(".e_etstamm where tnr=KB||ETNR||ES1||ES2 and LNR=").append(""+MappingDTO.getWarehouseNumber()+"").append(")").append(" AS mDESCRIPTION, ");
						query.append("  cast(substr(t1, 3,6) as real)/10 AS mINVOICEDQTY, cast(substr(t1, 35,7) as real)/100 AS mUNITPRICE,  cast(substr(t1, 3,6) as real)/10* cast(substr(t1, 35,7) as real)/100 AS mTOTALVALUELINEITEM,cast(substr(t1, 3,6) as real)/10* cast(substr(t1, 35,7) as real)/100  AS mTOTALNETVALLINEITEM, ");
						query.append("  cast( '20' || substr(JJMMTT, 1, 2)||'-'|| substr(JJMMTT, 3, 2)|| '-' || substr(JJMMTT, 5, 2 ) as date ) as mDELIVERYNOTEDATE ,")	;
						query.append("  (SELECT SUBSTR(DATAFLD, 1, 2) AS VALUE FROM ");
						query.append(dataLibrary).append(".REFERENZ  WHERE SUBSTR(KEYFLD, 7, 4) = '9907' and SUBSTR(KEYFLD, 12, 1)=(select mwst from ");
						query.append(dataLibrary).append(".e_etstamm where tnr=KB||ETNR||ES1||ES2 and LNR=").append(""+MappingDTO.getWarehouseNumber()+"").append("))").append(" as mVATCODE, ");
						query.append("  (select LKD_LKDNR from ");
						query.append(schema).append(".PMH_LKDNR WHERE LKD_LNR= ").append(MappingDTO.getWarehouseNumber());
						query.append(" and LKD_LIEF= ").append("'"+MappingDTO.getInvoiceNumber()+"'");
						query.append(" AND trim(LKD_TMARKE) = trim(substr(t1,24,2)) LIMIT 1) AS mCOMPANYCODE," );
						query.append(" substr(t3, 7, 11) as mBHW from  ");
						query.append(dataLibrary).append(" .e_cpsdat where (bart=1 or bart=2) and  ");
						query.append("BELNR =").append("'"+MappingDTO.getDeliveryNoteNo()+"'");
						query.append(" DO ");
						query.append("  insert into ").append(schema).append(".O_SA61 (");
						query.append(" GUID,OEM,DELIVERYNOTENR,DELIVERYNOTELINENR,NESTED_ORDINALITY,DELIVEREDPARTNR,DESCRIPTION,INVOICEDQTY,UNITPRICE,TOTALVALUELINEITEM,");
						query.append("TOTALNETVALLINEITEM, CURRENCYCODE,DELIVERYNOTEDATE,INVOICENR,INVOICEDATE, VATCODE,ORIGIN, CURRENCYDECINDICATOR, CREATED_BY, CREATED_TS,STATE,");
						query.append("AP_WRH,COMPANYCODE,BHW,OTHERCHARGES ");
						query.append(" ) ");
						query.append(" values ( ");
						query.append(" mGUID, ");
						String oem = MappingDTO.getOem().equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING: MappingDTO.getOem();
						query.append("'"+oem+"',");
						query.append("mDELIVERYNOTENR,mDELIVERYNOTELINENR,mNESTED_ORDINALITY,mDELIVEREDPARTNR, mDESCRIPTION,mINVOICEDQTY, mUNITPRICE,mTOTALVALUELINEITEM, ");
						query.append("mTOTALNETVALLINEITEM,'EUR',  ");
						query.append(" mDELIVERYNOTEDATE , ");
						query.append("'"+MappingDTO.getInvoiceNumber()+"',");
						query.append("'"+MappingDTO.getInvoiceDate()+"',");
						query.append(" mVATCODE , ");
						query.append("2,  ");
						query.append("0,  ");
						query.append("'"+loginUserName+"', ");
						query.append("CURRENT TIMESTAMP ,");
						query.append("0,  ");
						query.append(""+MappingDTO.getWarehouseNumber()+",");
						query.append(" mCOMPANYCODE , ");
						query.append(" mBHW , ");
						query.append("0  ");
						query.append(" ); ");
						query.append("   end for;");
						query.append("end ");

						log.info("Query for O_SA61  :" + query.toString());
						stmt.addBatch(query.toString());
					}}
				int[] records = dbServiceRepository.insertResultUsingBatchQuery(stmt);
				
				if (records != null) {
					log.info("In O_SA61 - Total rows Inserted  :" + records.length);

				}

				programOutput.put("isProcessCompleted", true);
				con.setAutoCommit(true);
				con.commit();
			}

		} catch (AlphaXException ex) {

			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "set/remove assigned delivery notes for an invoice "));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "set/remove assigned delivery notes for an invoice"), exception);

			throw exception;
		}
		return programOutput;
	}





	/**
	 * This method is used to Update invoice Amount for Delivery Note for an invoice from DB.
	 * @return Object
	 */
	@Override
	public Map<String, Boolean> updateInvoiceAmountForDeliveryNote(
			BookedDeliveryNoteDetailsDTO bookedDeliveryNoteDetailsDTO, String dataLibrary, String schema) {
		// TODO Auto-generated method stub
		// Task is About update "Menge lt. Rechnung" and "Preis lt. Rechnung" column.
		Map<String, Boolean> updateFlag = new HashMap<String, Boolean>();
		updateFlag.put("isSA61Updated", false);
		try {
			if (isNullOrEmpty(bookedDeliveryNoteDetailsDTO.getQuantityAccordingToInvoice()) && isNullOrEmpty(bookedDeliveryNoteDetailsDTO.getUnitPriceAccordingToInvoice()) 
					&& isNullOrEmpty(bookedDeliveryNoteDetailsDTO.getPartNumber()) && isNullOrEmpty(bookedDeliveryNoteDetailsDTO.getDeliveryNoteNo())) {

				StringBuilder queryForSA61 = new StringBuilder("UPDATE ").append(schema).append(".o_SA61 SET ");
				queryForSA61.append(" INVOICEDQTY = ").append("'").append(bookedDeliveryNoteDetailsDTO.getQuantityAccordingToInvoice()).append("'");
				queryForSA61.append(" ,TOTALVALUELINEITEM = ").append("'").append(bookedDeliveryNoteDetailsDTO.getUnitPriceAccordingToInvoice()).append("'");
				queryForSA61.append(" where DELIVEREDPARTNR= ").append("'").append(bookedDeliveryNoteDetailsDTO.getPartNumber()).append("'");
				queryForSA61.append(" AND DELIVERYNOTENR = ").append("'").append(bookedDeliveryNoteDetailsDTO.getDeliveryNoteNo()).append("'");

				boolean isSA61Updated = dbServiceRepository.updateResultUsingQuery(queryForSA61.toString());
				if(isSA61Updated){
					updateFlag.put("isSA61Updated", true);
				}
			}else {
				log.info("Delivery Note Object is empty");

			}
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY,"Update invoice Amount for Delivery Note"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY,"Update invoice Amount for Delivery Note"), exception);
			throw exception;
		}
		return updateFlag;
	}


	boolean isNullOrEmpty(String value) {

		boolean flag = false;
		if (value != null && !value.trim().isEmpty()) {
			flag = true;
		}
		return flag;
	}

	/**
	 * This method is used to return warehouse list for Invoice from DB.
	 * @return Object
	 */
	@Override
	public List<ActivatedCountingListDTO> getWarehouseListForInvoice(String dataLibrary, String schema,
			String allowedApWarehouses) {
		// TODO Auto-generated method stub
		log.info("Inside getWarehouseListForInvoice method of IncomingGoodsInvoiceServiceImpl");

		List<ActivatedCountingListDTO> warehouseListDTOs = new ArrayList<ActivatedCountingListDTO>();
		try {
			validateWarehouses(allowedApWarehouses);
			
			StringBuilder query = new StringBuilder("Select distinct p2.AP_WAREHOUS_ID  ,p2.name as WAREHOUSE_NAME  from ").append(schema).append(".o_sa60");
			query.append(" p1 inner join ").append(schema).append(".o_wrh").append(" p2 ON p1.ap_wrh = p2.AP_WAREHOUS_ID ");
			query.append(" where ap_wrh IN(").append(allowedApWarehouses).append(") order by AP_WAREHOUS_ID");
			
			List<InventoryList> warehouseList = dbServiceRepository.getResultUsingQuery(InventoryList.class, query.toString(), true);
			
			if (warehouseList != null && !warehouseList.isEmpty()) {
				for(InventoryList warehouselists : warehouseList){
					
					ActivatedCountingListDTO warehouseListDTO = new ActivatedCountingListDTO();
					
					warehouseListDTO.setWarehouseId(warehouselists.getAlphaPlusWarehouseId());
					warehouseListDTO.setWarehouseName(warehouselists.getWarehouseName());
					warehouseListDTOs.add(warehouseListDTO);
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(
					LocaleContextHolder.getLocale(), ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "warehouse list "));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "warehouse list "),exception);
			throw exception;
		}

		return warehouseListDTOs;
	}
}



		
