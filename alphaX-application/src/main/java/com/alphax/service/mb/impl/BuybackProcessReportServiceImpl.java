package com.alphax.service.mb.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.alphax.common.rest.message.service.MessageService;
import com.alphax.downloadpdf.service.BuybackProcessPDFService;
import com.alphax.common.exception.AlphaXException;
import com.alphax.common.exception.ExceptionMessages;
import com.alphax.repository.DBServiceRepository;
import com.alphax.service.mb.BuybackProcessReportService;
import com.alphax.service.mb.BuybackProcessService;
import com.alphax.vo.mb.BuybackHeaderDetailsDTO;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.extern.slf4j.Slf4j;

/**
 * @author A106744104
 *
 */
@Service
@Slf4j
public class BuybackProcessReportServiceImpl extends BaseService implements BuybackProcessReportService {

	@Autowired
	DBServiceRepository dbServiceRepository;

	@Autowired
	private MessageService messageService;

	@Autowired
	BuybackProcessPDFService buyBackPDFService;
	
	@Autowired
	BuybackProcessService buybackProcessService;
	

	DecimalFormat decimalformat_twodigit = new DecimalFormat("#0.00", new DecimalFormatSymbols(Locale.ENGLISH));

	
	/**
	 * This method is used to generate/ download the PDF file for Opened BuyBack Receipt.
	 */
	@Override
	public ByteArrayInputStream downloadPDFOpenBBOFReceipt( String schema, String dataLibrary, String headerId, String alphaPlusWarehouseNo, 
			List<BuybackHeaderDetailsDTO> buybackHeaderDetailsDTOList, String loginUserName ) {

		log.info("Inside downloadPDFOpenBBOFReceipt method of BuybackProcessReportServiceImpl");

		ByteArrayInputStream  byteArrayOutputStream = null;

		try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {			
			
			Map<String, Boolean> savedFlag = buybackProcessService.saveBuybackOpenItemDetails(buybackHeaderDetailsDTOList, headerId, schema, loginUserName);
			
			//save the printdate in the BBOFITEM
			if(savedFlag.entrySet().stream().findFirst().get().getValue()) {
				updatePrintDateIn_BBOFITEM(schema, headerId, loginUserName, buybackHeaderDetailsDTOList);
			}

			Document document = new Document();
			PdfWriter.getInstance(document, out);

			document.open();
			document.add(new Chunk(""));

			buyBackPDFService.createOpenBBOFData(document, 5, schema, dataLibrary, headerId, alphaPlusWarehouseNo);

			document.close();

			byteArrayOutputStream = new ByteArrayInputStream(out.toByteArray());

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "pdf for Opened BuyBack receipt"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"pdf for Opened BuyBack receipt"), exception);
			throw exception;
		}

		return byteArrayOutputStream;
	}
	
	
	/**
	 * This method is used to Update the Item details in O_BBOFITEM table.
	 */
	private boolean updatePrintDateIn_BBOFITEM(String schema, String headerId, String loginUserName, List<BuybackHeaderDetailsDTO> buybackHeaderDetailsDTOList) {

		log.info("Inside updateInTable_BBOFITEM method of BuybackProcessReportServiceImpl");

		boolean updateFlag = false;
		
		try (Connection con = dbServiceRepository.getConnectionObject();
				Statement stmt = con.createStatement()) {

			for(BuybackHeaderDetailsDTO headerDetailsDTO: buybackHeaderDetailsDTOList) {

				StringBuilder updateHeaderDetailsQuery = new StringBuilder("Update ").append(schema).append(".O_BBOFITEM ");
				updateHeaderDetailsQuery.append(" SET PRINT_DATE = now() where BBOF_HEAD_ID =");
				updateHeaderDetailsQuery.append(headerId).append(" and BBOF_ITEM_ID = ").append(headerDetailsDTO.getItemId());

				stmt.addBatch(updateHeaderDetailsQuery.toString());
			}

			int[] records =  dbServiceRepository.insertResultUsingBatchQuery(stmt);
			if(records != null) {
				updateFlag = true;
				log.info("Total rows inserted : {} ", records.length);
			}
		}
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Buy back Header item details"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY,
					"Buy back Header item details"), exception);

			throw exception;
		}

		return updateFlag;
	}

	
	
	/**
	 * This method is used to generate/ download the PDF file for Closed BuyBack Summary.
	 */
	@Override
	public ByteArrayInputStream downloadPDFClosedBBOF(String schema, String dataLibrary, String headerId, String alphaPlusWarehouseNo, String transferDate) {

		log.info("Inside downloadPDFClosedBBOF method of BuybackProcessReportServiceImpl");

		ByteArrayInputStream  byteArrayOutputStream = null;

		try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {

			Document document = new Document();
			PdfWriter.getInstance(document, out);

			document.open();
			document.add(new Chunk(""));

			buyBackPDFService.createClosedBBOFData(document, 7, schema, dataLibrary, headerId, alphaPlusWarehouseNo, transferDate);

			document.close();

			byteArrayOutputStream = new ByteArrayInputStream(out.toByteArray());

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "pdf for Closed BuyBack Summary"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"pdf for Closed BuyBack Summary"), exception);
			throw exception;
		}

		return byteArrayOutputStream;
	}


	

}