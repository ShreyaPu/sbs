package com.alphax.downloadpdf.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.alphax.common.rest.message.service.MessageService;
import com.alphax.model.mb.BookingAccount;
import com.alphax.model.mb.InventoryList;
import com.alphax.model.mb.InventoryParts;
import com.alphax.common.exception.AlphaXException;
import com.alphax.common.exception.ExceptionMessages;
import com.alphax.repository.DBServiceRepository;
import com.alphax.vo.mb.ReportHeaderDTO;
import com.alphax.common.constants.RestInputConstants;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import lombok.extern.slf4j.Slf4j;

/**
 * @author A106744104
 *
 */

@Service
@Slf4j
public class InventoryPDFService {

	@Autowired
	DBServiceRepository dbServiceRepository;

	@Autowired
	private MessageService messageService;

	private static Font font = FontFactory.getFont(FontFactory.COURIER, 8);
	private static Font font2 = FontFactory.getFont(FontFactory.COURIER, 8, Font.BOLD);
	private static Font font3 = FontFactory.getFont(FontFactory.COURIER, 10);
	
	

	List<InventoryList> inventoryCommonList = new ArrayList<InventoryList>();
	
	DecimalFormat decimalformat_fixtwodigit = new DecimalFormat("#0.00", new DecimalFormatSymbols(Locale.GERMAN));
	DecimalFormat decimalformat_fixFourdigit = new DecimalFormat("#0.0000", new DecimalFormatSymbols(Locale.GERMAN));

	
	/**
	 * This method is used set the cell layout.
	 * @param text
	 * @param alignment
	 * @return
	 */
	public PdfPCell getCell(String text, int alignment) {
		PdfPCell cell = new PdfPCell(new Phrase(text, font));
		cell.setPadding(5);
		cell.setHorizontalAlignment(alignment);
		cell.setBorder(PdfPCell.NO_BORDER);
		return cell;
	}


	/**
	 * This method is used to generate PDF for Activated inventory List
	 * 
	 * @param document
	 * @param noOfcolumns
	 * @param schema
	 * @param inventoryListId
	 * @throws DocumentException
	 */
	public void createTableData(Document document, int noOfcolumns, String schema, String inventoryListId) throws DocumentException, Exception {

		log.info("Inside createTableData method of InventoryPDFService");

		PdfPTable table = new PdfPTable(noOfcolumns);
		table.getDefaultCell().setBorder(0);

		//this create Table header
		createTableHeaderRow(document, table);

		//this is to create PDF header, footer and table data 
		getTableContentFromDB(document, table, schema, inventoryListId);

	}
	

	/**
	 * This method is used to create Table Headers.
	 * */
	private void createTableHeaderRow(Document document, PdfPTable table) {

		List<String> columnNames = Arrays.asList("Nr", "OEM", "Lagerort", "Teilenummer/Teilename", "Zählmenge");

		for(int i=0; i < 5; i++) {
			PdfPCell cell = new PdfPCell(new Phrase(columnNames.get(i), font3));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			if(i==4) {
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			}
			cell.setBorder(0);
			table.addCell(cell);
		}
		table.setHeaderRows(1);
	}


	/**
	 * This method is used to create PDF header, footer and table data.
	 * @param document
	 * @param table
	 * @param schema
	 * @param inventoryListId
	 * @throws DocumentException
	 */
	private void getTableContentFromDB(Document document, PdfPTable table, String schema, String inventoryListId) throws DocumentException, Exception {

		log.info("Inside getTableContentFromDB method of InventoryPDFService");

		int[] columnWidth = {7, 12, 18, 55, 12};

		try {

			StringBuilder selectQuery = new StringBuilder("select CNT, PLACE, OEM, TNR, TNRD, PARTNAME from ").append(schema).append(".O_INVPART Where INVLIST_ID =");
			selectQuery.append(inventoryListId).append(" Order by CNT");

			List<InventoryParts> inventoryPartList = dbServiceRepository.getResultUsingQuery(InventoryParts.class, selectQuery.toString(), true);

			if(inventoryPartList!=null && !inventoryPartList.isEmpty()) {
				for(InventoryParts invPartList : inventoryPartList) {

					table.setWidthPercentage(100);
					table.setWidths(columnWidth);
					table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.getDefaultCell().setPaddingBottom(4);

					table.addCell(new Phrase(String.valueOf(invPartList.getCount()), font3));
					table.addCell(new Phrase(invPartList.getManufacturer().equalsIgnoreCase(RestInputConstants.DCAG_STRING) ? 
							RestInputConstants.DAG_STRING : invPartList.getManufacturer(), font3) );
					table.addCell(new Phrase(invPartList.getPlace(), font3));					
//					table.addCell(new Phrase(invPartList.getPartNumber(), font));
					
					table.getDefaultCell().setExtraParagraphSpace(2.0f);
					table.addCell(new Phrase(invPartList.getPrintingFormatPartNumber() + "\n" + invPartList.getPartNAME(), font3));

//					table.addCell(new Phrase(invPartList.getPartNAME(), font));					
					table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
					table.addCell(new Phrase("_________", font3));

				}
				document.add(table);
			}
		}
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "pdf for Activated Counting List"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"pdf for Activated Counting List"), exception);
			throw exception;
		}
	}


	/**
	 * Add footer in PDF
	 * @param document
	 * @throws DocumentException
	 */
	public void addFooter(Document document) throws DocumentException {
		Paragraph p2 = new Paragraph();		
		leaveEmptyLine(p2, 2);
		p2.setAlignment(Element.ALIGN_LEFT);
		p2.add(new Paragraph(
				"Unterschrift 1  ____________________        Unterschrift 2  ____________________", font));

		document.add(p2);
	}


	/**
	 * This method is used to create empty lines in PDF
	 * 
	 * @param paragraph
	 * @param number
	 */
	private static void leaveEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}


	/*          **********************************************************************************          */


	/**
	 * This method is used to generate PDF for Activated inventory List
	 * 
	 * @param document
	 * @param noOfcolumns
	 * @param schema
	 * @param inventoryListId
	 * @param inventoryListId
	 * @throws DocumentException
	 */
	public void createTableDataWithPageBreak(Document document, int noOfcolumns, String schema, String inventoryListId, String pageBreakDDLValue) throws DocumentException, Exception {

		log.info("Inside createTableDataWithPageBreak method of InventoryPDFService");

		PdfPTable table = new PdfPTable(noOfcolumns);
		table.getDefaultCell().setBorder(0);
		
		//this create Table header
		createTableHeaderRow(document, table);

		//this is to create PDF header, footer and table data 
		getTableContentFromDB_PB(document, table, schema, inventoryListId, pageBreakDDLValue);
	}


	/**
	 * This method is used to create PDF header, footer and table data.
	 * @param document
	 * @param table
	 * @param schema
	 * @param inventoryListId
	 * @throws DocumentException
	 */
	private void getTableContentFromDB_PB(Document document, PdfPTable table, String schema, String inventoryListId, String pageBreakDDLValue) 
			throws DocumentException, Exception {

		log.info("Inside getTableContentFromDB_PB method of InventoryPDFService");

		int[] columnWidth = {7, 12, 18, 55, 12};
		String actLocation = "";

		try {
			StringBuilder selectQuery = new StringBuilder("select CNT, PLACE, OEM, TNR, TNRD, PARTNAME from ").append(schema).append(".O_INVPART Where INVLIST_ID =");
			selectQuery.append(inventoryListId).append(" Order by CNT");

			List<InventoryParts> inventoryPartList = dbServiceRepository.getResultUsingQuery(InventoryParts.class, selectQuery.toString(), true);

			if(inventoryPartList!=null && !inventoryPartList.isEmpty()) {

				String lastLocation = StringUtils.rightPad(inventoryPartList.get(0).getPlace(), 7, " ");

				for(InventoryParts invPartList : inventoryPartList) {
					
					actLocation = StringUtils.rightPad(invPartList.getPlace(), 7, " ");
					
					switch(pageBreakDDLValue) {

					case "1":
						if(!actLocation.substring(0, 1).equals(lastLocation.substring(0, 1))) {
							document.add(table);
//							addFooter(document);
							document.newPage();
//							pageCount ++;
							table.flushContent();
//							isPageBreak = true;
						}
						break;
					case "2":
						if(!actLocation.substring(0, 2).equals(lastLocation.substring(0, 2))) {
							document.add(table);
//							addFooter(document);
							document.newPage();
//							pageCount ++;
							table.flushContent();
//							isPageBreak = true;
						}
						break;
					case "3":
						if(!actLocation.substring(0, 4).equals(lastLocation.substring(0, 4))) {
							document.add(table);
//							addFooter(document);
							document.newPage();
//							pageCount ++;
							table.flushContent();
//							isPageBreak = true;
						}
						break;					
					case "4":
						if(!actLocation.substring(0, 5).equals(lastLocation.substring(0, 5))) {
							document.add(table);
//							addFooter(document);
							document.newPage();
//							pageCount ++;
							table.flushContent();
//							isPageBreak = true;
						}
						break;
					case "5":
						if(!actLocation.substring(0, 7).equals(lastLocation.substring(0, 7))) {
							document.add(table);
//							addFooter(document);
							document.newPage();
//							pageCount ++;
							table.flushContent();
//							isPageBreak = true;
						}
						break;
					default:
						log.info("Pagebreak seletced value is wrong.");
						break;
					}

					table.setWidthPercentage(100);
					table.setWidths(columnWidth);
					table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.getDefaultCell().setPaddingBottom(4);

					table.addCell(new Phrase(String.valueOf(invPartList.getCount()), font3));
					table.addCell(new Phrase(invPartList.getManufacturer().equalsIgnoreCase(RestInputConstants.DCAG_STRING) ? 
							RestInputConstants.DAG_STRING : invPartList.getManufacturer(), font3) );
					table.addCell(new Phrase(invPartList.getPlace(), font3));
					
//					table.addCell(new Phrase(invPartList.getPartNumber(), font));
					table.getDefaultCell().setExtraParagraphSpace(2.0f);
					table.addCell(new Phrase(invPartList.getPrintingFormatPartNumber() + "\n" + invPartList.getPartNAME(), font3));
					
//					table.addCell(new Phrase(invPartList.getPartNAME(), font3));
					table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(new Phrase("_________", font3));

					lastLocation = actLocation;

				}
				document.add(table);
			}
		}
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "pdf for Activated Counting List"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"pdf for Activated Counting List"), exception);
			throw exception;
		}
	}


	/**
	 * This method is used to generate PDF for Closed inventory List
	 * 
	 * @param document
	 * @param noOfcolumns
	 * @param schema
	 * @param inventoryListId
	 * @throws DocumentException
	 */
	public void createClosedInventoryData(Document document, int noOfcolumns, String schema, String inventoryListId) throws DocumentException, Exception {

		log.info("Inside createClosedInventoryData method of InventoryPDFService");

		PdfPTable table = new PdfPTable(noOfcolumns);
		table.getDefaultCell().setBorder(0);

		//this create Table header
		createTableHeaderRow_ClosedInv(document, table);

		//this is to create PDF header, footer and table data 
		getClosedInvContentFromDB(document, table, schema, inventoryListId);

	}


	/**
	 * This method is used to create Table Headers for closed Inventory List.
	 * */
	private void createTableHeaderRow_ClosedInv(Document document, PdfPTable table) {

		List<String> columnNames = Arrays.asList("Herst", "Teilenummer", "Bezeichnung", "Pg-Nr", "Soll", "Ist", "DAK(€)", "Delta\n" + 
				"(Stück)", "Delta(€)");

		for(int i=0; i < 9; i++) {
			PdfPCell cell = new PdfPCell(new Phrase(columnNames.get(i), font));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			if(i>3) {
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			}
			cell.setBorder(0);
			table.addCell(cell);
		}
		table.setHeaderRows(1);

	}


	/**
	 * This method is used to create PDF header, footer and table data for Closed counting list
	 * @param document
	 * @param table
	 * @param schema
	 * @param inventoryListId
	 * @throws DocumentException
	 */
	private void getClosedInvContentFromDB(Document document, PdfPTable table, String schema, String inventoryListId) throws DocumentException, Exception {

		log.info("Inside getClosedInvContentFromDB method of InventoryPDFService");

		int[] columnWidth = {8, 30, 28, 8, 9, 11, 11, 11, 11};

		try {

			StringBuilder selectQuery = new StringBuilder("select CNT, OEM, TNR, TNRD, PARTNAME, OLD_STOCK, NEW_STOCK, DAK, DELTA_PIECES, DELTA_CUR  from ");
			selectQuery.append(schema).append(".O_INVPART Where INVLIST_ID =").append(inventoryListId).append(" Order by CNT asc");

			List<InventoryParts> inventoryPartList = dbServiceRepository.getResultUsingQuery(InventoryParts.class, selectQuery.toString(), true);

			if(inventoryPartList!=null && !inventoryPartList.isEmpty()) {
				for(InventoryParts invPartList : inventoryPartList) {

					table.setWidthPercentage(100);
					table.setWidths(columnWidth);
					table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

					table.addCell(new Phrase(invPartList.getManufacturer().equalsIgnoreCase(RestInputConstants.DCAG_STRING) ? 
							RestInputConstants.DAG_STRING : invPartList.getManufacturer(), font) );
//					table.addCell(new Phrase(invPartList.getPartNumber(), font));
					table.addCell(new Phrase(invPartList.getPrintingFormatPartNumber(), font));
					table.addCell(new Phrase(invPartList.getPartNAME(), font));
					table.addCell(new Phrase(String.valueOf(invPartList.getCount()), font));
					table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(new Phrase(String.valueOf(invPartList.getOldStock()!=null ? decimalformat_fixtwodigit.format(invPartList.getOldStock()): 
						decimalformat_fixtwodigit.format(0.00)), font));
					table.addCell(new Phrase(String.valueOf(invPartList.getNewStock()!=null ? decimalformat_fixtwodigit.format(invPartList.getNewStock()): 
						decimalformat_fixtwodigit.format(0.00)), font ));
					table.addCell(new Phrase(String.valueOf(invPartList.getDak()!=null ? decimalformat_fixtwodigit.format(invPartList.getDak()): ""), font));
					table.addCell(new Phrase(String.valueOf(invPartList.getDeltaPieces()!=null ? decimalformat_fixtwodigit.format(invPartList.getDeltaPieces()): ""), font));
					table.addCell(new Phrase(String.valueOf(invPartList.getDeltaCur()!=null ? decimalformat_fixtwodigit.format(invPartList.getDeltaCur()): ""), font));

				}
				document.add(table);
			}
		}
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "pdf for Closed Counting List"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"pdf for Closed Counting List"), exception);
			throw exception;
		}
	}


	/**
	 * This method is used to generate PDF for Activated inventory List for Status 3
	 * 
	 * @param document
	 * @param noOfcolumns
	 * @param schema
	 * @param inventoryListId
	 * @throws DocumentException
	 */
	public void createTableData_ST3(Document document, int noOfcolumns, String schema, String inventoryListId) throws DocumentException, Exception {

		log.info("Inside createTableData_ST3 method of InventoryPDFService");

		PdfPTable table = new PdfPTable(noOfcolumns);
		table.getDefaultCell().setBorder(0);

		//this create Table header
		createTableHeaderRow(document, table);

		//this is to create PDF header, footer and table data 
		getTableContentFromDB_ST3(document, table, schema, inventoryListId);

	}


	/**
	 * This method is used to create PDF header, footer and table data.
	 * @param document
	 * @param table
	 * @param schema
	 * @param inventoryListId
	 * @throws DocumentException
	 */
	private void getTableContentFromDB_ST3(Document document, PdfPTable table, String schema, String inventoryListId) throws DocumentException, Exception {

		log.info("Inside getTableContentFromDB_ST3 method of InventoryPDFService");

		int[] columnWidth = {7, 12, 18, 55, 12};

		try {
			StringBuilder selectQuery = new StringBuilder("select CNT, PLACE, OEM, TNR, TNRD, PARTNAME, NEW_STOCK  from ").append(schema);
			selectQuery.append(".O_INVPART Where INVLIST_ID =").append(inventoryListId).append(" Order by CNT");

			List<InventoryParts> inventoryPartList = dbServiceRepository.getResultUsingQuery(InventoryParts.class, selectQuery.toString(), true);

			if(inventoryPartList!=null && !inventoryPartList.isEmpty()) {
				for(InventoryParts invPartList : inventoryPartList) {

					table.setWidthPercentage(100);
					table.setWidths(columnWidth);
					table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.getDefaultCell().setPaddingBottom(4);

					table.addCell(new Phrase(String.valueOf(invPartList.getCount()), font3));
					table.addCell(new Phrase(invPartList.getManufacturer().equalsIgnoreCase(RestInputConstants.DCAG_STRING) ? 
							RestInputConstants.DAG_STRING : invPartList.getManufacturer(), font3) );
					table.addCell(new Phrase(invPartList.getPlace(), font3));					
//					table.addCell(new Phrase(invPartList.getPartNumber(), font));
					
					table.getDefaultCell().setExtraParagraphSpace(2.0f);
					table.addCell(new Phrase(invPartList.getPrintingFormatPartNumber() + "\n" + invPartList.getPartNAME(), font3));
//					table.addCell(new Phrase(invPartList.getPartNAME(), font));
					table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(new Phrase(String.valueOf(invPartList.getNewStock()!=null ? decimalformat_fixtwodigit.format(invPartList.getNewStock()) : "-----"), font3));

				}
				document.add(table);
			}
		}
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "pdf for Activated Counting List"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"pdf for Activated Counting List"), exception);
			throw exception;
		}
	}


	/**
	 * This method is used to generate PDF for Activated inventory List
	 * 
	 * @param document
	 * @param noOfcolumns
	 * @param schema
	 * @param inventoryListId
	 * @param inventoryListId
	 * @throws DocumentException
	 */
	public void createTableDataWithPageBreak_ST3(Document document, int noOfcolumns, String schema, String inventoryListId, String pageBreakDDLValue) throws DocumentException, Exception {

		log.info("Inside createTableDataWithPageBreak_ST3 method of InventoryPDFService");

		PdfPTable table = new PdfPTable(noOfcolumns);
		table.getDefaultCell().setBorder(0);
		
		//this create Table header
		createTableHeaderRow(document, table);

		//this is to create PDF header, footer and table data 
		getTableContentFromDB_PB_ST3(document, table, schema, inventoryListId, pageBreakDDLValue);

	}


	/**
	 * This method is used to create PDF header, footer and table data.
	 * @param document
	 * @param table
	 * @param schema
	 * @param inventoryListId
	 * @throws DocumentException
	 */
	private void getTableContentFromDB_PB_ST3(Document document, PdfPTable table, String schema, String inventoryListId, String pageBreakDDLValue) 
			throws DocumentException, Exception {

		log.info("Inside getTableContentFromDB_PB_ST3 method of InventoryPDFService");

		int[] columnWidth = {7, 12, 18, 55, 10};
		String actLocation = "";

		try {
			StringBuilder selectQuery = new StringBuilder("select CNT, PLACE, OEM, TNR, TNRD, PARTNAME, NEW_STOCK from ");
			selectQuery.append(schema).append(".O_INVPART Where INVLIST_ID =").append(inventoryListId).append(" Order by CNT");

			List<InventoryParts> inventoryPartList = dbServiceRepository.getResultUsingQuery(InventoryParts.class, selectQuery.toString(), true);

			if(inventoryPartList!=null && !inventoryPartList.isEmpty()) {

				String lastLocation = StringUtils.rightPad(inventoryPartList.get(0).getPlace(), 7, " ");

				for(InventoryParts invPartList : inventoryPartList) {
					
					actLocation = StringUtils.rightPad(invPartList.getPlace(), 7, " ");
					
					switch(pageBreakDDLValue) {

					case "1":
						if(!actLocation.substring(0, 1).equals(lastLocation.substring(0, 1))) {
							document.add(table);
//							addFooter(document);
							document.newPage();
//							pageCount ++;
							table.flushContent();
//							isPageBreak = true;
						}
//						else { isPageBreak = false; }
						break;
					case "2":
						if(!actLocation.substring(0, 2).equals(lastLocation.substring(0, 2))) {
							document.add(table);
//							addFooter(document);
							document.newPage();
//							pageCount ++;
							table.flushContent();
//							isPageBreak = true;
						}
//						else { isPageBreak = false; }
						break;
					case "3":
						if(!actLocation.substring(0, 4).equals(lastLocation.substring(0, 4))) {
							document.add(table);
//							addFooter(document);
							document.newPage();
//							pageCount ++;
							table.flushContent();
//							isPageBreak = true;
						}
//						else { isPageBreak = false; }
						break;					
					case "4":
						if(!actLocation.substring(0, 5).equals(lastLocation.substring(0, 5))) {
							document.add(table);
//							addFooter(document);
							document.newPage();
//							pageCount ++;
							table.flushContent();
//							isPageBreak = true;
						}
//						else { isPageBreak = false; }
						break;
					case "5":
						if(!actLocation.substring(0, 7).equals(lastLocation.substring(0, 7))) {
							document.add(table);
//							addFooter(document);
							document.newPage();
//							pageCount ++;
							table.flushContent();
//							isPageBreak = true;
						}
//						else { isPageBreak = false; }
						break;
					default:
						log.info("Pagebreak seletced value is wrong.");
						break;
					}

					table.setWidthPercentage(100);
					table.setWidths(columnWidth);
					table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.getDefaultCell().setPaddingBottom(4);

					table.addCell(new Phrase(String.valueOf(invPartList.getCount()), font3));
					table.addCell(new Phrase(invPartList.getManufacturer().equalsIgnoreCase(RestInputConstants.DCAG_STRING) ? 
							RestInputConstants.DAG_STRING : invPartList.getManufacturer(), font3) );
					table.addCell(new Phrase(invPartList.getPlace(), font3));					
//					table.addCell(new Phrase(invPartList.getPartNumber(), font));
					
					table.getDefaultCell().setExtraParagraphSpace(2.0f);
					table.addCell(new Phrase(invPartList.getPrintingFormatPartNumber() + "\n" + invPartList.getPartNAME(), font3));
//					table.addCell(new Phrase(invPartList.getPartNAME(), font3));
					table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(new Phrase(String.valueOf(invPartList.getNewStock()!=null ? decimalformat_fixtwodigit.format(invPartList.getNewStock()) : "----"), font3 ));

					lastLocation = actLocation;

				}
				document.add(table);
			}
		}
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "pdf for Activated Counting List"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"pdf for Activated Counting List"), exception);
			throw exception;
		}
	}


	/**
	 * This method is used to generate PDF to show differences of Inventory parts intermediate results.
	 * 
	 * @param document
	 * @param noOfcolumns
	 * @param schema
	 * @param inventoryListId
	 * @throws DocumentException
	 */
	public void createTableData_IntermediateDiff(Document document, int noOfcolumns, String schema, String inventoryListId) throws DocumentException, Exception {

		log.info("Inside createTableData_IntermediateDiff method of InventoryPDFService");

		PdfPTable table = new PdfPTable(noOfcolumns);
		table.getDefaultCell().setBorder(0);

		//this create Table header
		createTableHeaderRow_InterDiffPDF(document, table);

		//this is to create PDF header, footer and table data 
		getTableContentFromDB_InterDiffPDF(document, table, schema, inventoryListId, 1); // 1 for Discarded (Abgewiesene) Difference

		getTableContentFromDB_InterDiffPDF(document, table, schema, inventoryListId, 2); // 2 for Assumed (Angenommene) Difference
	}


	/**
	 * This method is used to create Table Headers.
	 * */
	private void createTableHeaderRow_InterDiffPDF(Document document, PdfPTable table) {

		List<String> columnNames = Arrays.asList("Hersteller", "Teilenummer", "Teilename", "SA", "Delta in €");

		for(int i=0; i < 5; i++) {
			PdfPCell cell = new PdfPCell(new Phrase(columnNames.get(i), font));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			if(i==4) {
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			}
			cell.setBorder(0);
			table.addCell(cell);
		}
		table.setHeaderRows(1);
	}


	/**
	 * This method is used to create PDF header, footer and table data.
	 * @param document
	 * @param table
	 * @param schema
	 * @param inventoryListId
	 * @throws DocumentException
	 */
	private void getTableContentFromDB_InterDiffPDF(Document document, PdfPTable table, String schema, String inventoryListId, Integer type) 
			throws DocumentException, Exception {

		log.info("Inside getTableContentFromDB_InterDiffPDF method of InventoryPDFService");
		List<InventoryParts> inventoryPartList = null;

		int[] columnWidth = {15, 25, 35, 8, 15};
		Double deltaTotal = 0.00;

		if(type.equals(1)) {
			inventoryPartList = getDiscardedDifferenceData(schema, inventoryListId);
		}
		else if(type.equals(2)){
			inventoryPartList = getAssumedDifferenceData(schema, inventoryListId);
		}

		if(inventoryPartList!=null && !inventoryPartList.isEmpty()) {

			for(InventoryParts invPartList : inventoryPartList) {

				table.setWidthPercentage(100);
				table.setWidths(columnWidth);
				table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

				table.addCell(new Phrase(invPartList.getManufacturer().equalsIgnoreCase(RestInputConstants.DCAG_STRING) ? 
						RestInputConstants.DAG_STRING : invPartList.getManufacturer(), font) );
//				table.addCell(new Phrase(invPartList.getPartNumber(), font));
				table.addCell(new Phrase(invPartList.getPrintingFormatPartNumber(), font));
				table.addCell(new Phrase(invPartList.getPartNAME(), font));
				table.addCell(new Phrase(String.valueOf(invPartList.getStorageIndikator() != null ? invPartList.getStorageIndikator() : "0"), font));
				table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				table.addCell(new Phrase(String.valueOf(invPartList.getDeltaCur()!=null ? decimalformat_fixtwodigit.format(invPartList.getDeltaCur()): 
					decimalformat_fixtwodigit.format(0.00)), font));

				//get the total of Delta_CUR
				deltaTotal += invPartList.getDeltaCur()!=null ? invPartList.getDeltaCur().doubleValue(): 0.00;
			}
			document.add(table);
			addFooter_InterDiffPDF(document, deltaTotal);
			table.flushContent();
			
			//this code is used to add page-break after Discarded (Abgewiesene) table added
			if(type.equals(1)) {
				document.newPage();
			}
		}
		else {
			table.flushContent();
		}
	}


	/**
	 * This method is used to Create Header for PDF
	 * @param document
	 * @param schema
	 * @param inventoryListId
	 * @param pageCount
	 * @throws DocumentException
	 */
	public void getHeaderData_InterDiffPDF(Document document, String schema, String inventoryListId, Integer pageCount, Integer type) throws DocumentException, Exception {

		PdfPTable table = new PdfPTable(3);

		if(pageCount.equals(1)) {
			List<InventoryList> inventoryList = getDBDataForHeaders_InterDiffPDF(schema, inventoryListId);
			inventoryCommonList = inventoryList;
		}

		if(inventoryCommonList!=null && !inventoryCommonList.isEmpty()) {
			for(InventoryList invList : inventoryCommonList) {

				table.setWidthPercentage(100);
				table.addCell(getCell(RestInputConstants.COUNTING_LIST_NAME +": "+ invList.getInventoryListName(), PdfPCell.ALIGN_LEFT));
				if(type.equals(1)) {
					table.addCell(getCell(RestInputConstants.REJECTED_DIFFERENCES, PdfPCell.ALIGN_RIGHT));
				}
				else if(type.equals(2)) {
					table.addCell(getCell(RestInputConstants.ASSUMED_DIFFERENCES, PdfPCell.ALIGN_RIGHT));
				}
				table.addCell(getCell(RestInputConstants.PAGE_COUNT +": "+ StringUtils.leftPad(pageCount.toString(), 3, "0"), PdfPCell.ALIGN_RIGHT));

				table.addCell(getCell(RestInputConstants.WAREHOUSE_NAME +": "+ invList.getWarehouseId().toString()+" - "+invList.getWarehouseName().toString(), PdfPCell.ALIGN_LEFT));
				table.addCell(getCell(" ", PdfPCell.ALIGN_RIGHT));
				table.addCell(getCell(RestInputConstants.INVENTORY_DATE +": "+ invList.getCreationDate(), PdfPCell.ALIGN_RIGHT));
			}			
		}
		document.add(table);
	}


	/**
	 * This method is used to get DB details for Header 
	 * */
	public List<InventoryList> getDBDataForHeaders_InterDiffPDF(String schema, String inventoryListId) throws Exception  {

		StringBuilder selectQuery = new StringBuilder("select WAREHOUSE_ID, WAREHOUSE_NAME, NAME, VARCHAR_FORMAT(now(), 'DD.MM.YYYY') AS CREATED_TS from ");
		selectQuery.append(schema).append(".O_INVLIST where INVLIST_ID = ").append(inventoryListId);

		List<InventoryList> inventoryList = dbServiceRepository.getResultUsingQuery(InventoryList.class, selectQuery.toString(), true);

		return inventoryList;
	}


	/**
	 * This method is used to get DB details for Discarded Difference Data 
	 * */
	public List<InventoryParts> getDiscardedDifferenceData(String schema, String inventoryListId) throws Exception  {

		StringBuilder selectQuery = new StringBuilder("select distinct TNR, TNRD, OEM, PARTNAME, SA, DELTA_CUR ");
		selectQuery.append(" from ").append(schema).append(".O_INVPART Where INVLIST_ID =");
		selectQuery.append(inventoryListId).append(" and  INVPARTSTS_ID = 2  and  NOT DELTA_CUR = 0  Order by TNR ");

		List<InventoryParts> inventoryPartList = dbServiceRepository.getResultUsingQuery(InventoryParts.class, selectQuery.toString(), true);

		return inventoryPartList;
	}


	/**
	 * This method is used to get DB details for Assumed Difference Data  
	 * */
	public List<InventoryParts> getAssumedDifferenceData(String schema, String inventoryListId) throws Exception  {

		StringBuilder selectQuery = new StringBuilder("select distinct TNR, TNRD, OEM, PARTNAME, SA, DELTA_CUR ");
		selectQuery.append(" from ").append(schema).append(".O_INVPART Where INVLIST_ID =");
		selectQuery.append(inventoryListId).append(" and  INVPARTSTS_ID = 3  and  NOT DELTA_CUR = 0  Order by TNR ");

		List<InventoryParts> inventoryPartList = dbServiceRepository.getResultUsingQuery(InventoryParts.class, selectQuery.toString(), true);

		return inventoryPartList;
	}


	/**
	 * Add footer in PDF Intermediate Diff 
	 * @param document
	 * @throws DocumentException
	 */
	public void addFooter_InterDiffPDF(Document document, Double deltaTotal) throws DocumentException {
		PdfPTable table = new PdfPTable(5);
		int[] columnWidth = {15, 25, 35, 8, 15};

		table.setWidthPercentage(100);
		table.setWidths(columnWidth);
		table.getDefaultCell().setBorder(0);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

		table.addCell(" ");
		table.addCell(" ");
		table.addCell(" ");
		table.addCell(" ");
		table.addCell(new Phrase("===========", font2));

		table.addCell(" ");
		table.addCell(" ");
		table.addCell(" ");
		table.addCell(" ");
		table.addCell(new Phrase(String.valueOf(deltaTotal !=null ? decimalformat_fixtwodigit.format(deltaTotal): decimalformat_fixtwodigit.format(0.00)), font2));

		document.add(table);

	}


	/**
	 * This method is used to generate PDF for Countless and Rejected Positions.
	 * 
	 * @param document
	 * @param noOfcolumns
	 * @param schema
	 * @param inventoryListId
	 * @throws DocumentException
	 */
	public void createCountlessRejectedPartsData(Document document, int noOfcolumns, String schema, String inventoryListId) throws DocumentException, Exception {

		log.info("Inside createCountlessRejectedPartsData method of InventoryPDFService");

		PdfPTable table = new PdfPTable(noOfcolumns);
		table.getDefaultCell().setBorder(0);

		//this create Table header
		createTableHeaderRow_Rejected(document, table);

		//this is to create PDF header, footer and table data 
		getRejectedContentFromDB(document, table, schema, inventoryListId);

	}


	/**
	 * This method is used to create Table Headers for Countless and Rejected Positions.
	 * */
	private void createTableHeaderRow_Rejected(Document document, PdfPTable table) {

		List<String> columnNames = Arrays.asList("Nr", "Herst.", "Teilenummer", "Teilename", "Grund");

		for(int i=0; i < 5; i++) {
			PdfPCell cell = new PdfPCell(new Phrase(columnNames.get(i), font));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setBorder(0);
			table.addCell(cell);
		}
		table.setHeaderRows(1);

	}


	/**
	 * This method is used to create PDF header, footer and table data for Countless and Rejected Positions
	 * @param document
	 * @param table
	 * @param schema
	 * @param inventoryListId
	 * @throws DocumentException
	 */
	private void getRejectedContentFromDB(Document document, PdfPTable table, String schema, String inventoryListId) throws DocumentException, Exception {

		log.info("Inside getRejectedContentFromDB method of InventoryPDFService");

		int[] columnWidth = {7, 9, 26, 30, 30};

		try {
			StringBuilder selectQuery = new StringBuilder("select CNT, OEM, TNR, TNRD, PARTNAME, INVPARTSTS_ID from ");
			selectQuery.append(schema).append(".O_INVPART Where INVLIST_ID =").append(inventoryListId).append(" and INVPARTSTS_ID IN(0, 2, 3)  Order by CNT asc");

			List<InventoryParts> inventoryPartList = dbServiceRepository.getResultUsingQuery(InventoryParts.class, selectQuery.toString(), true);

			if(inventoryPartList!=null && !inventoryPartList.isEmpty()) {
				for(InventoryParts invPartList : inventoryPartList) {

					table.setWidthPercentage(100);
					table.setWidths(columnWidth);
					table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

					table.addCell(new Phrase(String.valueOf(invPartList.getCount()), font));
					table.addCell(new Phrase(invPartList.getManufacturer().equalsIgnoreCase(RestInputConstants.DCAG_STRING) ? 
							RestInputConstants.DAG_STRING : invPartList.getManufacturer(), font) );
//					table.addCell(new Phrase(invPartList.getPartNumber(), font));
					table.addCell(new Phrase(invPartList.getPrintingFormatPartNumber(), font));
					table.addCell(new Phrase(invPartList.getPartNAME(), font));

					if(invPartList.getPartStatusId().toString().equals("0")) {
						table.addCell(new Phrase(RestInputConstants.REASON1_STATUS0, font));
					}
					else if(invPartList.getPartStatusId().toString().equals("2")){
						table.addCell(new Phrase(RestInputConstants.REASON2_STATUS2, font));
					}
					else if(invPartList.getPartStatusId().toString().equals("3")){
						table.addCell(new Phrase(RestInputConstants.REASON3_STATUS3, font));
					}
					else {
						table.addCell(new Phrase("__", font));
					}

				}
				document.add(table);
			}
		}
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "pdf for Countless and Rejected Positons"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"pdf for Countless and Rejected Positons"), exception);
			throw exception;
		}
	}


	/**
	 * This method is used to generate PDF for Counting List with Quantities 
	 * 
	 * @param document
	 * @param noOfcolumns
	 * @param schema
	 * @param inventoryListId
	 * @throws DocumentException
	 */
	public void createCntingListQuantityData(Document document, int noOfcolumns, String schema, String inventoryListId) throws DocumentException, Exception {

		log.info("Inside createCntingListQuantityData method of InventoryPDFService");

		PdfPTable table = new PdfPTable(noOfcolumns);
		table.getDefaultCell().setBorder(0);

		//this create Table header
		createTableHeaderRow_CountingListQ(document, table);

		//this is to create PDF header, footer and table data 
		getTableContentFromDB_CountingListQ(document, table, schema, inventoryListId);
	}


	/**
	 * This method is used to create Table Headers.
	 * */
	private void createTableHeaderRow_CountingListQ(Document document, PdfPTable table) {

		List<String> columnNames = Arrays.asList("Nr", "Lagerort", "Herst", "Teilenummer", "Teilename", "Zählmenge");

		for(int i=0; i < 6; i++) {
			PdfPCell cell = new PdfPCell(new Phrase(columnNames.get(i), font));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			if(i==5) {
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			}
			cell.setBorder(0);
			table.addCell(cell);
		}
		table.setHeaderRows(1);
	}


	/**
	 * This method is used to create PDF header, footer and table data for Counting List with Quantities.
	 * @param document
	 * @param table
	 * @param schema
	 * @param inventoryListId
	 * @throws DocumentException
	 */
	private void getTableContentFromDB_CountingListQ(Document document, PdfPTable table, String schema, String inventoryListId) throws DocumentException, Exception {

		log.info("Inside getTableContentFromDB_CountingListQ method of InventoryPDFService");

		int[] columnWidth = {7, 16, 12, 26, 30, 11};

		try {
			StringBuilder selectQuery = new StringBuilder("select CNT, PLACE, OEM, TNR, TNRD, PARTNAME, NEW_STOCK from ").append(schema).append(".O_INVPART Where INVLIST_ID =");
			selectQuery.append(inventoryListId).append(" Order by CNT ASC");

			List<InventoryParts> inventoryPartList = dbServiceRepository.getResultUsingQuery(InventoryParts.class, selectQuery.toString(), true);

			if(inventoryPartList!=null && !inventoryPartList.isEmpty()) {
				for(InventoryParts invPartList : inventoryPartList) {

					table.setWidthPercentage(100);
					table.setWidths(columnWidth);
					table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

					table.addCell(new Phrase(String.valueOf(invPartList.getCount()), font));
					table.addCell(new Phrase(invPartList.getPlace(), font));
					table.addCell(new Phrase(invPartList.getManufacturer().equalsIgnoreCase(RestInputConstants.DCAG_STRING) ? 
							RestInputConstants.DAG_STRING : invPartList.getManufacturer(), font) );
//					table.addCell(new Phrase(invPartList.getPartNumber(), font));
					table.addCell(new Phrase(invPartList.getPrintingFormatPartNumber(), font));
					table.addCell(new Phrase(invPartList.getPartNAME(), font));
					table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(new Phrase(String.valueOf(invPartList.getNewStock()!=null ? decimalformat_fixtwodigit.format(invPartList.getNewStock()) : "----"), font));
				}
				document.add(table);
			}
		}
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "pdf for Counting List With Quantities"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"pdf for Counting List With Quantities"), exception);
			throw exception;
		}

	}


	/**
	 * This method is used to generate PDF for Positions without Difference.
	 * 
	 * @param document
	 * @param noOfcolumns
	 * @param schema
	 * @param inventoryListId
	 * @throws DocumentException
	 */
	public void createWithoutDifferenceData(Document document, int noOfcolumns, String schema, String inventoryListId) throws DocumentException, Exception {

		log.info("Inside createWithoutDifferenceData method of InventoryPDFService");

		PdfPTable table = new PdfPTable(noOfcolumns);
		table.getDefaultCell().setBorder(0);

		//this create Table header
		createTableHeaderRow_WithoutDiff(document, table);

		//this is to create PDF header, footer and table data 
		getTableContentFromDB_WithoutDiff(document, table, schema, inventoryListId);

	}


	/**
	 * This method is used to create Table Headers.
	 * */
	private void createTableHeaderRow_WithoutDiff(Document document, PdfPTable table) {

		List<String> columnNames = Arrays.asList("Hersteller", "Teilenummer", "Teilename", "Ist");

		for(int i=0; i < 4; i++) {
			PdfPCell cell = new PdfPCell(new Phrase(columnNames.get(i), font));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			if(i==3) {
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			}
			cell.setBorder(0);
			table.addCell(cell);
		}
		table.setHeaderRows(1);
	}


	/**
	 * This method is used to create PDF header, footer and table data for Counting List with Quantities.
	 * @param document
	 * @param table
	 * @param schema
	 * @param inventoryListId
	 * @throws DocumentException
	 */
	private void getTableContentFromDB_WithoutDiff(Document document, PdfPTable table, String schema, String inventoryListId) {

		log.info("Inside getTableContentFromDB_WithoutDiff method of InventoryPDFService");

		int[] columnWidth = {15, 27, 38, 10};

		try {
			StringBuilder selectQuery = new StringBuilder("select CNT, PLACE, OEM, TNR, TNRD, PARTNAME, OLD_STOCK from ").append(schema).append(".O_INVPART Where INVLIST_ID =");
			selectQuery.append(inventoryListId).append(" AND INVPARTSTS_ID = 4 and DELTA_CUR = 0  Order by CNT ASC");

			List<InventoryParts> inventoryPartList = dbServiceRepository.getResultUsingQuery(InventoryParts.class, selectQuery.toString(), true);

			if(inventoryPartList!=null && !inventoryPartList.isEmpty()) {
				for(InventoryParts invPartList : inventoryPartList) {

					table.setWidthPercentage(100);
					table.setWidths(columnWidth);
					table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

					table.addCell(new Phrase(invPartList.getManufacturer().equalsIgnoreCase(RestInputConstants.DCAG_STRING) ? 
							RestInputConstants.DAG_STRING : invPartList.getManufacturer(), font) );
//					table.addCell(new Phrase(invPartList.getPartNumber(), font));
					table.addCell(new Phrase(invPartList.getPrintingFormatPartNumber(), font));
					table.addCell(new Phrase(invPartList.getPartNAME(), font));
					table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);

					table.addCell(new Phrase(String.valueOf(invPartList.getOldStock()!=null ? decimalformat_fixtwodigit.format(invPartList.getOldStock()): 
						decimalformat_fixtwodigit.format(0.00)), font));

				}
				document.add(table);
			}
		}
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "pdf for Positions Without Differnce"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"pdf for Positions Without Differnce"), exception);
			throw exception;
		}
	}


	/**
	 * This method is used to generate PDF for Accepted Different Positions (Akzeptierte Differente Positionen).
	 * 
	 * @param document
	 * @param noOfcolumns
	 * @param schema
	 * @param inventoryListId
	 * @throws DocumentException
	 */
	public void createAcceptedDifferentData(Document document, int noOfcolumns, String schema, String inventoryListId) throws DocumentException, Exception {

		log.info("Inside createAcceptedDifferentData method of InventoryPDFService");

		PdfPTable table = new PdfPTable(noOfcolumns);
		table.getDefaultCell().setBorder(0);

		//this create Table header
		createTableHeaderRow_AcceptedDiff(document, table);

		//this is to create PDF header, footer and table data 
		getTableContentFromDB_AcceptedDiff(document, table, schema, inventoryListId);

	}


	/**
	 * This method is used to create Table Headers.
	 * */
	private void createTableHeaderRow_AcceptedDiff(Document document, PdfPTable table) {

		List<String> columnNames = Arrays.asList("Herst", "Teilenummer", "Best. alt", "Endbest.", "Diff. Best.", "Sortierung nach DAK");

		for(int i=0; i < 6; i++) {
			PdfPCell cell = new PdfPCell(new Phrase(columnNames.get(i), font));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			if(i>1) {
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			}
			cell.setBorder(0);
			table.addCell(cell);
		}
		table.setHeaderRows(1);
	}


	/**
	 * This method is used to create PDF header, footer and table data for Accepted Different Positions.
	 * @param document
	 * @param table
	 * @param schema
	 * @param inventoryListId
	 * @throws DocumentException
	 */
	private void getTableContentFromDB_AcceptedDiff(Document document, PdfPTable table, String schema, String inventoryListId) throws DocumentException, Exception {

		log.info("Inside getTableContentFromDB_AcceptedDiff method of InventoryPDFService");

		int[] columnWidth = {10, 26, 13, 14, 15, 22};

		Double oldStock = 0.00;
		Double sumNewStock = 0.00;	
		Double differnceStock = 0.00;
		Double deltaCur = 0.00;
		Double sum_deltaCur = 0.00;
		Double positive_DeltaCur = 0.00;
		Double negative_DeltaCur = 0.00;

		try {
			StringBuilder selectQuery = new StringBuilder("select distinct OEM, TNR, TNRD, OLD_STOCK, DELTA_CUR, ");
			selectQuery.append("(select sum(NEW_STOCK) from ").append(schema).append(".O_INVPART where OEM = t.OEM  AND TNR = t.TNR AND INVLIST_ID =");
			selectQuery.append(inventoryListId).append(" ) AS SUM_NEW_STOCK  from ");
			selectQuery.append(schema).append(".O_INVPART t Where t.INVLIST_ID =");
			selectQuery.append(inventoryListId).append(" AND t.INVPARTSTS_ID = 4 AND  NOT DELTA_CUR = 0  Order by t.DELTA_CUR desc");

			List<InventoryParts> inventoryPartList = dbServiceRepository.getResultUsingQuery(InventoryParts.class, selectQuery.toString(), true);

			if(inventoryPartList!=null && !inventoryPartList.isEmpty()) {
				for(InventoryParts invPartList : inventoryPartList) {

					table.setWidthPercentage(100);
					table.setWidths(columnWidth);
					table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.setSpacingAfter(10f);

					table.addCell(new Phrase(invPartList.getManufacturer().equalsIgnoreCase(RestInputConstants.DCAG_STRING) ? 
							RestInputConstants.DAG_STRING : invPartList.getManufacturer(), font) );
//					table.addCell(new Phrase(invPartList.getPartNumber(), font));
					table.addCell(new Phrase(invPartList.getPrintingFormatPartNumber(), font));

					table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					
					oldStock = invPartList.getOldStock() != null ? invPartList.getOldStock().doubleValue() : 0.00;
					sumNewStock = invPartList.getSum_newStock() != null ? invPartList.getSum_newStock().doubleValue() : 0.00;
					differnceStock = sumNewStock - oldStock;

					table.addCell(new Phrase(String.valueOf(decimalformat_fixtwodigit.format(oldStock)), font));				
					table.addCell(new Phrase(String.valueOf(decimalformat_fixtwodigit.format(sumNewStock)), font));
					
					table.addCell(new Phrase(String.valueOf(decimalformat_fixtwodigit.format(differnceStock)), font));
					
					deltaCur = invPartList.getDeltaCur() != null ? invPartList.getDeltaCur().doubleValue() : 0.00;
					
					table.addCell(new Phrase(String.valueOf(decimalformat_fixtwodigit.format(deltaCur)), font));
					
					sum_deltaCur += deltaCur;
					
					if(deltaCur > 0) {
						positive_DeltaCur += deltaCur;
					}
					if(deltaCur < 0) {
						negative_DeltaCur += deltaCur;
					}
				}
				document.add(table);
				addFooter_AcceptedDiff(document, sum_deltaCur, positive_DeltaCur, negative_DeltaCur);

			}
		}
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "pdf for Accepted Diff Positions"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"pdf for Accepted Diff Positions"), exception);
			throw exception;
		}
	}



	/**
	 * Add footer in PDF
	 * @param document
	 * @throws DocumentException
	 */
	public void addFooter_AcceptedDiff(Document document, Double sum_deltaCur, Double positive_DeltaCur, Double negative_DeltaCur ) throws DocumentException {

		document.add(getLineSeperator());
		
		Paragraph p2 = new Paragraph();		
		leaveEmptyLine(p2, 2);
		document.add(p2);

		PdfPTable table = new PdfPTable(2);
		int[] columnWidth = {15, 20};
		table.setWidthPercentage(35);
		table.setWidths(columnWidth);
		table.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
		
		table.addCell(getCell(RestInputConstants.GESAMT +" '-': ", Element.ALIGN_LEFT));
		table.addCell(getCell(String.valueOf(decimalformat_fixFourdigit.format(negative_DeltaCur)), Element.ALIGN_RIGHT));
		
		table.addCell(getCell(RestInputConstants.GESAMT +" '+': ", Element.ALIGN_LEFT));
		table.addCell(getCell(String.valueOf(decimalformat_fixFourdigit.format(positive_DeltaCur)), Element.ALIGN_RIGHT));
		
		table.addCell(getCell(RestInputConstants.GESAMT +": ", Element.ALIGN_LEFT));
		table.addCell(getCell(String.valueOf(decimalformat_fixFourdigit.format(sum_deltaCur)), Element.ALIGN_RIGHT));
		
		document.add(table);
	}
	
	
	public Chunk getLineSeperator() {
		
		CustomDashedLineSeparator separator = new CustomDashedLineSeparator();
		separator.setDash(3);
		separator.setGap(3);
		separator.setLineWidth(0.5f);

		Chunk linebreak = new Chunk(separator);
		
		return linebreak;
	}
	
	
	/**
	 * This method is used to generate PDF for Difference sum of accepted positions (Differenzen-Summe von akzeptierten Positionen).
	 * 
	 * @param document
	 * @param noOfcolumns
	 * @param schema
	 * @param inventoryListId
	 * @throws DocumentException
	 */
	public void createDifferenceSumPosData(Document document, int noOfcolumns, String schema, 
			String inventoryListId, String warehouseNumber, String axCompanyId) throws DocumentException, Exception {

		log.info("Inside createDifferenceSumPosData method of InventoryPDFService");

		PdfPTable table = new PdfPTable(noOfcolumns);
		table.getDefaultCell().setBorder(0);

		//this create Table header
		createTableHeaderRow_DifferenceSum(document, table);

		//this is to data from DB 
		List<BookingAccount> finalAccountList = getTblContentFromDB_DifferenceSum(schema, inventoryListId, warehouseNumber);
		
		//this is to create PDF header, footer and table data 
		createTableContent_DifferenceSum(document, table, schema, finalAccountList, inventoryListId, warehouseNumber);
	}

	
	/**
	 * This method is used to create Table Headers.
	 * */
	private void createTableHeaderRow_DifferenceSum(Document document, PdfPTable table) {

		List<String> columnNames = Arrays.asList("Teileart", "Teilemarke", "Konto", "Kontoname", "negative Differenz(€)", "positive Differenz(€)", "Gesamtdifferenz(€)");

		for(int i=0; i < 7; i++) {
			PdfPCell cell = new PdfPCell(new Phrase(columnNames.get(i), font));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			if(i>3) {
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			}
			cell.setBorder(PdfPCell.NO_BORDER);
			table.addCell(cell);
		}		
		table.setHeaderRows(1);
	}
	
	
	/**
	 * This method is used to create PDF header, footer and table data for Difference sum of Accepted positions.(Differenzen-Summe von akzeptierten Positionen).
	 * @param document
	 * @param table
	 * @param schema
	 * @param inventoryListId
	 * @throws DocumentException
	 */
	private List<BookingAccount> getTblContentFromDB_DifferenceSum(String schema, String inventoryListId, String warehouseNumber) {

		log.info("Inside getTblContentFromDB_DifferenceSum method of InventoryPDFService");

		Double countPositive = 0.0;
		Double countNegative = 0.0;
		Double totalCount = 0.0;

		List<BookingAccount> updatedAccountList = new ArrayList<>();

		try {
			List<BookingAccount> kontoDetailList = getKontoDetailList(schema, inventoryListId, warehouseNumber);
			List<InventoryParts>  invPartList = getInventoryPartDetailList(schema, inventoryListId);

			if(kontoDetailList!=null && !kontoDetailList.isEmpty()) {
				if(invPartList!=null && !invPartList.isEmpty()) {

					List<BookingAccount> listOneList = kontoDetailList.stream().filter(two -> invPartList.stream()
							.anyMatch(one -> one.getPartsIndikator().equals(Integer.parseInt(two.getPartsIndikator())) && two.getBrand().equals(one.getPartBrand()))) 
							.collect(Collectors.toList());

					for(BookingAccount bAccount : listOneList) {

						countPositive = 0.0;
						countNegative = 0.0;
						totalCount = 0.0;

						for(InventoryParts invPart : invPartList) {

							if(bAccount.getPartsIndikator().equals(StringUtils.leftPad(invPart.getPartsIndikator().toString(), 2, "0")) 
									&& bAccount.getBrand().equals(invPart.getPartBrand())) {

								if(invPart.getDeltaCur() != null && invPart.getDeltaCur().compareTo(BigDecimal.ZERO) > 0 ) {
									countPositive += invPart.getDeltaCur().doubleValue();
								}
								if(invPart.getDeltaCur() != null && invPart.getDeltaCur().compareTo(BigDecimal.ZERO) < 0) {
									countNegative += invPart.getDeltaCur().doubleValue();
								}
								//if it is zero
								else {
									countPositive += 0.0;
								}

								totalCount = countPositive + countNegative;
							}				
						}

						BookingAccount ba = new BookingAccount(); 
						ba.setPartsIndikator(StringUtils.leftPad(bAccount.getPartsIndikator(), 5, "TA "));
						ba.setBrand(bAccount.getBrand());
						ba.setBookingNumber(bAccount.getBookingNumber());
						ba.setDescription(bAccount.getDescription());
						ba.setCountPositive(countPositive);
						ba.setCountNegative(countNegative);
						ba.setTotalCount(totalCount);

						updatedAccountList.add(ba);					
					}

					//for unmatched records
					List<InventoryParts> remainingInvPartList = invPartList.stream().filter(o1 -> listOneList.stream()
							.noneMatch(o2 -> Integer.valueOf(o2.getPartsIndikator()).equals(o1.getPartsIndikator()) && o2.getBrand().equals(o1.getPartBrand())))
							.collect(Collectors.toList());

					if(remainingInvPartList!=null && !remainingInvPartList.isEmpty()) {

						countPositive = 0.0;
						countNegative = 0.0;
						totalCount = 0.0;

						for(InventoryParts invParts : remainingInvPartList) {

							if(invParts.getDeltaCur() != null && invParts.getDeltaCur().compareTo(BigDecimal.ZERO) > 0 ) {
								countPositive += invParts.getDeltaCur().doubleValue();
							}
							if(invParts.getDeltaCur() != null && invParts.getDeltaCur().compareTo(BigDecimal.ZERO) < 0) {
								countNegative += invParts.getDeltaCur().doubleValue();
							}
							//if it is zero
							else {
								countPositive += 0.0;
							}

							totalCount = countPositive + countNegative;
						}

						BookingAccount ba = new BookingAccount();
						ba.setPartsIndikator("");
						ba.setBrand("");
						ba.setBookingNumber(BigDecimal.ZERO);
						ba.setDescription("Undefinierte Teilearten");
						ba.setCountPositive(countPositive);
						ba.setCountNegative(countNegative);
						ba.setTotalCount(totalCount);

						updatedAccountList.add(ba);		
					}
				}
			}
		}
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "pdf for Difference sum of Accepted positions"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"pdf for Difference sum of Accepted positions"), exception);
			throw exception;
		}

		return updatedAccountList;
	}
	
	
	/**
	 * This method is used to get DB details
	 * */
	public List<BookingAccount> getKontoDetailList(String schema, String inventoryListId, String warehouseNumber) throws Exception  {
		
		warehouseNumber = StringUtils.leftPad(warehouseNumber, 2, "0");

		StringBuilder selectQuery = new StringBuilder("Select BUK_TEIART, BUK_TMARKE, BUK_KONTO, BUK_BEZEI from ");
		selectQuery.append(schema).append(".PEI_BUKONT t where (t.BUK_LNR = '").append(warehouseNumber).append("') AND t.BUK_DTGULV = ");
		selectQuery.append("(select max(BUK_DTGULV) from ").append(schema).append(".PEI_BUKONT where VARCHAR_FORMAT(now(),'YYYY-MM-DD') > BUK_DTGULV  ");
		selectQuery.append(" AND BUK_LNR = '").append(warehouseNumber).append("' AND BUK_TMARKE = t.BUK_TMARKE AND BUK_TEIART = t.BUK_TEIART) ");
		selectQuery.append(" UNION ");
		selectQuery.append(" Select BUK_TEIART, BUK_TMARKE, BUK_KONTO, BUK_BEZEI from ");
		selectQuery.append(schema).append(".PEI_BUKONT t where (t.BUK_LNR = '**') AND  t.BUK_DTGULV = ");
		selectQuery.append("(select max(BUK_DTGULV) from ").append(schema).append(".PEI_BUKONT where VARCHAR_FORMAT(now(),'YYYY-MM-DD') > BUK_DTGULV  ");
		selectQuery.append(" AND BUK_LNR = '**' AND BUK_TMARKE = t.BUK_TMARKE AND BUK_TEIART = t.BUK_TEIART) ");
		selectQuery.append(" AND BUK_TMARKE || BUK_TEIART not in (select BUK_TMARKE || BUK_TEIART from ").append(schema).append(".PEI_BUKONT where BUK_LNR ='");
		selectQuery	.append(warehouseNumber).append("') ");

		List<BookingAccount> bookingAccountList = dbServiceRepository.getResultUsingQuery(BookingAccount.class, selectQuery.toString(), true);

		return bookingAccountList;
	}

	
	/**
	 * This method is used to get DB details
	 * */
	public List<InventoryParts> getInventoryPartDetailList(String schema, String inventoryListId) throws Exception  {

		StringBuilder selectQuery = new StringBuilder("select distinct TNR, TA, TMARKE as TMA_TMARKE, DELTA_CUR  from ");
		selectQuery.append(schema).append(".O_INVPART where INVLIST_ID = ").append(inventoryListId).append(" and INVPARTSTS_ID = 4");

		List<InventoryParts> inventoryPartList = dbServiceRepository.getResultUsingQuery(InventoryParts.class, selectQuery.toString(), true);

		return inventoryPartList;
	}
	
	
	/**
	 * This method is used to create PDF header, footer and table data for Difference sum of Accepted positions.(Differenzen-Summe von akzeptierten Positionen)..
	 * @param document
	 * @param table
	 * @param schema
	 * @param inventoryListId
	 * @throws DocumentException
	 */
	private void createTableContent_DifferenceSum(Document document, PdfPTable table, String schema, List<BookingAccount> bookingAccountList, 
			String inventoryListId, String warehouseNumber) {

		log.info("Inside createTableContent_DifferenceSum method of InventoryPDFService");

		int[] columnWidth = {10, 12, 13, 27, 15, 15, 19};

		Double totalPositive = 0.0;
		Double totalNegative = 0.0;
		Double totalOverall = 0.0;

		try {
			if(bookingAccountList!=null && !bookingAccountList.isEmpty()) {
				for(BookingAccount bookingAcc : bookingAccountList) {

					if(bookingAcc.getCountNegative() < 0 || bookingAcc.getCountPositive() > 0) {

						table.setWidthPercentage(100);
						table.setWidths(columnWidth);
						table.setSpacingAfter(10f);
						table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

						table.addCell(new Phrase(bookingAcc.getPartsIndikator(), font));
						table.addCell(new Phrase(bookingAcc.getBrand(), font));

						if(bookingAcc.getBookingNumber() != null && bookingAcc.getBookingNumber().compareTo(BigDecimal.ZERO) != 0) {
							table.addCell(new Phrase(bookingAcc.getBookingNumber().toString(), font));
						}
						else {
							table.addCell(new Phrase(" ", font));
						}

						table.addCell(new Phrase(bookingAcc.getDescription(), font));

						table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);

						table.addCell(new Phrase(String.valueOf(decimalformat_fixFourdigit.format(bookingAcc.getCountNegative())), font));				
						table.addCell(new Phrase(String.valueOf(decimalformat_fixFourdigit.format(bookingAcc.getCountPositive())), font));

						table.addCell(new Phrase(String.valueOf(decimalformat_fixFourdigit.format(bookingAcc.getTotalCount())), font));

						totalPositive += bookingAcc.getCountPositive();
						totalNegative += bookingAcc.getCountNegative();
						totalOverall += bookingAcc.getTotalCount();
					}
				}
				document.add(table);
				addFooter_DifferenceSum(document, totalOverall, totalPositive, totalNegative);
			}
		}
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "pdf for Difference sum of Accepted positions"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"pdf for Difference sum of Accepted positions"), exception);
			throw exception;
		}
	}
	
	
	/**
	 * This method is used to get DB details for Header 
	 * */
	public ReportHeaderDTO getDBDataForHeaders(String schema, String inventoryListId, String axCompanyId, String reportName, 
			String reportDateName ) throws Exception  {
		
		StringBuilder selectQuery = new StringBuilder("select WAREHOUSE_ID, WAREHOUSE_NAME, NAME, VARCHAR_FORMAT(ACTIVATION_DATE, 'DD.MM.YYYY') AS ACTIVATION_DATE, ");
		selectQuery.append(" VARCHAR_FORMAT(PRINT_DATE, 'DD.MM.YYYY') AS PRINT_DATE, VARCHAR_FORMAT(CREATED_TS, 'DD.MM.YYYY') AS CREATED_TS, ");
		selectQuery.append(" VARCHAR_FORMAT(now(), 'DD.MM.YYYY') AS CREATED_TS_NOW, ");
		selectQuery.append(" (SELECT NAME from ").append(schema).append(".O_Company where COMPANY_ID = ");
		selectQuery.append(axCompanyId).append(") AS AX_COMAPNY_NAME from ");
		selectQuery.append(schema).append(".O_INVLIST where INVLIST_ID = ").append(inventoryListId);

		List<InventoryList> inventoryList = dbServiceRepository.getResultUsingQuery(InventoryList.class, selectQuery.toString(), true);
		
		ReportHeaderDTO reportDTO = new ReportHeaderDTO();
		
		if(inventoryList!=null && !inventoryList.isEmpty()) {
			
			for(InventoryList invList: inventoryList) {
				
				reportDTO.setActivationDate(invList.getActivationDate());
				
				if(reportDateName.equalsIgnoreCase("print")) {
					reportDTO.setReportDate(invList.getPrintDate());
				}
				else if(reportDateName.equalsIgnoreCase("creation")) {
					reportDTO.setReportDate(invList.getCreationDate());
				}
				else if(reportDateName.equalsIgnoreCase("now")) {
					reportDTO.setReportDate(invList.getCreationNOWDate());
				}
				
				reportDTO.setReportName(reportName);
				reportDTO.setInventoryListName(invList.getInventoryListName());
				reportDTO.setAxCompanyName(invList.getAxCompanyName());
				reportDTO.setWarehouseId(StringUtils.leftPad(invList.getWarehouseId().toString(), 2, "0"));
				reportDTO.setWarehouseName(invList.getWarehouseName());			
			}			
		}

		return reportDTO;
	}
	
	
	/**
	 * Add footer in PDF
	 * @param document
	 * @throws DocumentException
	 */
	public void addFooter_DifferenceSum(Document document, Double sum_all, Double positive_total, Double negative_total ) throws DocumentException {
		
		int[] columnWidth = {10, 12, 13, 27, 15, 15, 19};
		
		document.add(getLineSeperator());		
		PdfPTable table = new PdfPTable(7);
		table.setWidthPercentage(100);
		table.setWidths(columnWidth);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.getDefaultCell().setBorder(0);
		
		table.addCell(new Phrase(" ", font));
		table.addCell(new Phrase(" ", font));
		table.addCell(new Phrase(" ", font));
		table.addCell(new Phrase(" ", font));
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(new Phrase(String.valueOf(decimalformat_fixFourdigit.format(negative_total)), font));
		table.addCell(new Phrase(String.valueOf(decimalformat_fixFourdigit.format(positive_total)), font));
		table.addCell(new Phrase(String.valueOf(decimalformat_fixFourdigit.format(sum_all)), font));
	
		document.add(table);
	}
	
	
	public void addEndPage(Document document) throws DocumentException {
		Paragraph p2 = new Paragraph();		
		leaveEmptyLine(p2, 2);
		p2.setAlignment(Element.ALIGN_LEFT);
		p2.add(new Paragraph("Zähllisten Ende", font));

		document.add(p2);
	}
	
	
}