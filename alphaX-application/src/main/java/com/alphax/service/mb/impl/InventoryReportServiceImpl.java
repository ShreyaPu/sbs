package com.alphax.service.mb.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.alphax.common.rest.message.service.MessageService;
import com.alphax.downloadpdf.service.FooterPageEvent;
import com.alphax.downloadpdf.service.HeaderPageEvent;
import com.alphax.downloadpdf.service.InventoryPDFService;
import com.alphax.common.exception.AlphaXException;
import com.alphax.common.exception.ExceptionMessages;
import com.alphax.repository.DBServiceRepository;
import com.alphax.service.mb.InventoryReportService;
import com.alphax.common.constants.RestInputConstants;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.extern.slf4j.Slf4j;

/**
 * @author A106744104
 *
 */
@Service
@Slf4j
public class InventoryReportServiceImpl extends BaseService implements InventoryReportService {

	@Autowired
	DBServiceRepository dbServiceRepository;

	@Autowired
	private MessageService messageService;

	@Autowired
	InventoryPDFService inventoryPDFService;

	@Autowired
	InventoryServiceImpl inventoryservice;

	DecimalFormat decimalformat_twodigit = new DecimalFormat("#0.00", new DecimalFormatSymbols(Locale.ENGLISH));	
	private static Font font = FontFactory.getFont(FontFactory.COURIER, 8);
	private static Font font2 = FontFactory.getFont(FontFactory.COURIER, 10);
	

	/**
	 * This method is used to generate/ download the PDF file for Inventory Parts List. 
	 */
	@Override
	public ByteArrayInputStream downloadPDFInvPartsList(String schema, String inventoryListId, boolean pageBreakFlag, String pageBreakDDLValue, String axCompanyId) {

		log.info("Inside downloadPDFInvPartsList method of InventoryServiceImpl");

		ByteArrayInputStream  byteArrayOutputStream = null;

		try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {

			inventoryservice.genericInventoryStatusCheck(schema, inventoryListId, "1,2");

			inventoryservice.updateInventoryStatus(schema, inventoryListId, "2");

			// create document
			Document document = new Document(PageSize.A4, 30, 30, 125, 70);
			PdfWriter writer = PdfWriter.getInstance(document, out);

			// add header and footer
			HeaderPageEvent headerEvent = new HeaderPageEvent();
			headerEvent.setReportHeaderList(inventoryPDFService.getDBDataForHeaders(schema, inventoryListId, axCompanyId, 
					RestInputConstants.COUNTING_LIST_NAME, "creation"), font2);
			writer.setPageEvent(headerEvent);
			
			// add footer
			FooterPageEvent footerEvent = new FooterPageEvent();
			writer.setPageEvent(footerEvent);	

			document.open();

			if(pageBreakFlag) {
				inventoryPDFService.createTableDataWithPageBreak(document, 5, schema, inventoryListId, pageBreakDDLValue); 
			}
			else {
				inventoryPDFService.createTableData(document, 5, schema, inventoryListId);
			}
			
			document.add(new Chunk(""));

			document.close();

			byteArrayOutputStream = new ByteArrayInputStream(out.toByteArray());

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "pdf for Activated Counting List"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"pdf for Activated Counting List"), exception);
			throw exception;
		}

		return byteArrayOutputStream;
	}


	/**
	 * This method is used to generate/ download the PDF file for Closed Inventory Parts List. 
	 */
	@Override
	public ByteArrayInputStream downloadPDFClosedInvPartsList( String schema, String inventoryListId, String axCompanyId ) {

		log.info("Inside downloadPDFClosedInvPartsList method of InventoryServiceImpl");

		ByteArrayInputStream  byteArrayOutputStream = null;

		try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {

			// create document
			Document document = new Document(PageSize.A4, 30, 30, 125, 70);
			PdfWriter writer = PdfWriter.getInstance(document, out);

			// add header and footer
			HeaderPageEvent headerEvent = new HeaderPageEvent();
			headerEvent.setReportHeaderList(inventoryPDFService.getDBDataForHeaders(schema, inventoryListId, axCompanyId, 
					RestInputConstants.COUNTING_LIST_NAME, "print"), font);
			writer.setPageEvent(headerEvent);
			
			document.open();

			inventoryPDFService.createClosedInventoryData(document, 9, schema, inventoryListId);
			
			document.add(new Chunk(""));

			document.close();

			byteArrayOutputStream = new ByteArrayInputStream(out.toByteArray());

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "pdf for Closed Counting List"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"pdf for Closed Counting List"), exception);
			throw exception;
		}

		return byteArrayOutputStream;
	}


	/**
	 * This method is used to generate/ download the PDF file for Inventory Parts List having Status 3. 
	 */
	@Override
	public ByteArrayInputStream downloadPDF_ST3_InvPartsList(String schema, String inventoryListId, boolean pageBreakFlag, String pageBreakDDLValue, String axCompanyId) {

		log.info("Inside downloadPDF_ST3_InvPartsList method of InventoryServiceImpl");

		ByteArrayInputStream  byteArrayOutputStream = null;

		try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {

			//boolean statusFlag = checkInventoryStatus(schema, inventoryListId, "3");
			boolean statusFlag = inventoryservice.genericInventoryStatusCheck(schema, inventoryListId, "3");

			if(statusFlag) {

				// create document
				Document document = new Document(PageSize.A4, 30, 30, 125, 70);
				PdfWriter writer = PdfWriter.getInstance(document, out);

				// add header
				HeaderPageEvent headerEvent = new HeaderPageEvent();
				headerEvent.setReportHeaderList(inventoryPDFService.getDBDataForHeaders(schema, inventoryListId, axCompanyId, 
						RestInputConstants.COUNTING_LIST_NAME, "print"), font2);
				writer.setPageEvent(headerEvent);
				
				document.open();

				if(pageBreakFlag) {
					inventoryPDFService.createTableDataWithPageBreak_ST3(document, 5, schema, inventoryListId, pageBreakDDLValue); 
				}
				else {
					inventoryPDFService.createTableData_ST3(document, 5, schema, inventoryListId);
				}

				document.add(new Chunk(""));
				
				document.close();

				byteArrayOutputStream = new ByteArrayInputStream(out.toByteArray());
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "pdf for Activated Counting List"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"pdf for Activated Counting List"), exception);
			throw exception;
		}

		return byteArrayOutputStream;
	}


	/**
	 * This method is used to generate/ download the PDF file to show differences of inventory parts intermediate results. 
	 */
	@Override
	public ByteArrayInputStream downloadPDF_IntermediateDiff( String schema, String inventoryListId, String axCompanyId ) {

		log.info("Inside downloadPDF_IntermediateDiff method of InventoryServiceImpl");

		ByteArrayInputStream  byteArrayOutputStream = null;

		try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {

			//Checking status of the counting list if changed in the meantime then page will reload.
			inventoryservice.genericInventoryStatusCheck(schema,inventoryListId,"4"); // 4 is inventory list status Id

			// create document
			Document document = new Document(PageSize.A4, 30, 30, 125, 70);
			PdfWriter writer = PdfWriter.getInstance(document, out);

			// add header and footer
			HeaderPageEvent headerEvent = new HeaderPageEvent();
			headerEvent.setReportHeaderList(inventoryPDFService.getDBDataForHeaders(schema, inventoryListId, axCompanyId, 
					RestInputConstants.COUNTING_LIST_NAME, "now"), font);
			writer.setPageEvent(headerEvent);

			document.open();
			document.add(new Chunk(""));

			inventoryPDFService.createTableData_IntermediateDiff(document, 5, schema, inventoryListId);

			document.close();

			byteArrayOutputStream = new ByteArrayInputStream(out.toByteArray());

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "pdf for Zwischenstand"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"pdf for Zwischenstand"), exception);
			throw exception;
		}

		return byteArrayOutputStream;
	}


	/**
	 * This method is used to generate/ download the PDF file for Countless and Rejected Parts List of Inventory. (Ungezählte und abgewiesene Positionen)
	 */
	@Override
	public ByteArrayInputStream downloadPDF_Countless_Rejected_List( String schema, String inventoryListId, String axCompanyId ) {

		log.info("Inside downloadPDF_Countless_Rejected_List method of InventoryServiceImpl");

		ByteArrayInputStream  byteArrayOutputStream = null;

		try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {

			// create document
			Document document = new Document(PageSize.A4, 30, 30, 125, 70);
			PdfWriter writer = PdfWriter.getInstance(document, out);

			// add header
			HeaderPageEvent headerEvent = new HeaderPageEvent();
			headerEvent.setReportHeaderList(inventoryPDFService.getDBDataForHeaders(schema, inventoryListId, axCompanyId, 
					RestInputConstants.REJECTED_POSTIONS_NAME, "print"), font);
			writer.setPageEvent(headerEvent);

			document.open();

			inventoryPDFService.createCountlessRejectedPartsData(document, 5, schema, inventoryListId);
			
			document.add(new Chunk(""));

			document.close();

			byteArrayOutputStream = new ByteArrayInputStream(out.toByteArray());

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "pdf for Countless and Rejected Positons"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"pdf for Countless and Rejected Positons"), exception);
			throw exception;
		}

		return byteArrayOutputStream;
	}


	/**
	 * This method is used to generate/ download the PDF file for Counting List With Quantities for Inventory. (Zählliste mit Zählmengen)
	 */
	@Override
	public ByteArrayInputStream downloadPDF_CountingListQuantity( String schema, String inventoryListId, String axCompanyId ) {

		log.info("Inside downloadPDF_CountingListQuantity method of InventoryServiceImpl");

		ByteArrayInputStream  byteArrayOutputStream = null;

		try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {

			// create document
			Document document = new Document(PageSize.A4, 30, 30, 125, 70);
			PdfWriter writer = PdfWriter.getInstance(document, out);

			// add header and footer
			HeaderPageEvent headerEvent = new HeaderPageEvent();
			headerEvent.setReportHeaderList(inventoryPDFService.getDBDataForHeaders(schema, inventoryListId, axCompanyId, 
					RestInputConstants.COUNTING_LIST_QUANTITY_NAME, "print"), font);
			writer.setPageEvent(headerEvent);
			
			document.open();

			inventoryPDFService.createCntingListQuantityData(document, 6, schema, inventoryListId);
			
			document.add(new Chunk(""));

			document.close();

			byteArrayOutputStream = new ByteArrayInputStream(out.toByteArray());

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "pdf for Counting List With Quantities"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"pdf for Counting List With Quantities"), exception);
			throw exception;
		}

		return byteArrayOutputStream;
	}


	/**
	 * This method is used to generate/ download the PDF file for Positions without Difference for Inventory. (Positionen ohne Differenz)
	 */
	@Override
	public ByteArrayInputStream downloadPDF_Without_Differnce( String schema, String inventoryListId, String axCompanyId ) {

		log.info("Inside downloadPDF_Without_Differnce method of InventoryServiceImpl");

		ByteArrayInputStream  byteArrayOutputStream = null;

		try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {

			// create document
			Document document = new Document(PageSize.A4, 30, 30, 125, 70);
			PdfWriter writer = PdfWriter.getInstance(document, out);

			// add header and footer
			HeaderPageEvent headerEvent = new HeaderPageEvent();
			headerEvent.setReportHeaderList(inventoryPDFService.getDBDataForHeaders(schema, inventoryListId, axCompanyId, 
					RestInputConstants.POSTIONS_WO_DIFFERNCE, "print"), font);
			writer.setPageEvent(headerEvent);
			
			document.open();

			inventoryPDFService.createWithoutDifferenceData(document, 4, schema, inventoryListId);
			
			document.add(new Chunk(""));

			document.close();

			byteArrayOutputStream = new ByteArrayInputStream(out.toByteArray());

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "pdf for Positions Without Differnce"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"pdf for Positions Without Differnce"), exception);
			throw exception;
		}

		return byteArrayOutputStream;
	}


	/**
	 * This method is used to generate/ download the PDF file for Accepted Different Positions for Inventory. (Akzeptierte Differente Positionen)
	 */
	@Override
	public ByteArrayInputStream downloadPDF_Accepted_Diff_Pos( String schema, String inventoryListId, String axCompanyId ) {

		log.info("Inside downloadPDF_Accepted_Diff_Pos method of InventoryServiceImpl");

		ByteArrayInputStream  byteArrayOutputStream = null;

		try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {

			// create document
			Document document = new Document(PageSize.A4, 30, 30, 125, 70);
			PdfWriter writer = PdfWriter.getInstance(document, out);

			// add header and footer
			HeaderPageEvent headerEvent = new HeaderPageEvent();
			headerEvent.setReportHeaderList(inventoryPDFService.getDBDataForHeaders(schema, inventoryListId, axCompanyId, 
					RestInputConstants.POSTIONS_ACCEPTED_DIFFERNCE, "print"), font);
			writer.setPageEvent(headerEvent);

			document.open();

			inventoryPDFService.createAcceptedDifferentData(document, 6, schema, inventoryListId);
			
			document.add(new Chunk(""));

			document.close();

			byteArrayOutputStream = new ByteArrayInputStream(out.toByteArray());

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "pdf for Accepted Diff Positions"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"pdf for Accepted Diff Positions"), exception);
			throw exception;
		}

		return byteArrayOutputStream;
	}


	/**
	 * This method is used to generate/ download the PDF file for Difference sum of Accepted positions. (Differenzen-Summe von akzeptierten Positionen)
	 */
	@Override
	public ByteArrayInputStream downloadPDF_DifferenceSum_Pos( String schema, String inventoryListId, String warehouseNumber, String axCompanyId ) {

		log.info("Inside downloadPDF_DifferenceSum_Pos method of InventoryServiceImpl");

		ByteArrayInputStream  byteArrayOutputStream = null;

		try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {

			// create document
			Document document = new Document(PageSize.A4, 30, 30, 125, 30);
			PdfWriter writer = PdfWriter.getInstance(document, out);

			// add header and footer
			HeaderPageEvent headerEvent = new HeaderPageEvent();
			headerEvent.setReportHeaderList(inventoryPDFService.getDBDataForHeaders(schema, inventoryListId, axCompanyId, RestInputConstants.SUMMERY_DIFFERNCE, "print"), font);	        
			writer.setPageEvent(headerEvent);

			// write to document
			document.open();			
			document.add(new Chunk(""));
			
			inventoryPDFService.createDifferenceSumPosData(document, 7, schema, inventoryListId, warehouseNumber, axCompanyId);		
		
			document.close();
	
			byteArrayOutputStream = new ByteArrayInputStream(out.toByteArray());

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "pdf for Difference sum of Accepted positions"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"pdf for Difference sum of Accepted positions"), exception);
			throw exception;
		}

		return byteArrayOutputStream;
	}

}