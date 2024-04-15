package com.alphax.downloadpdf.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import com.alphax.model.mb.BuybackHeaderInfo;
import com.alphax.model.mb.BuybackItemInfo;
import com.alphax.model.mb.InventoryParts;
import com.alphax.common.exception.AlphaXException;
import com.alphax.common.exception.ExceptionMessages;
import com.alphax.repository.DBServiceRepository;
import com.alphax.service.mb.PartsService;
import com.alphax.vo.mb.StorageLocationDTO;
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
public class BuybackProcessPDFService {

	@Autowired
	DBServiceRepository dbServiceRepository;

	@Autowired
	private MessageService messageService;
	
	@Autowired
	private PartsService partSevice;

	private static Font font = FontFactory.getFont(FontFactory.COURIER, 9);

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
	 * This method is used to generate PDF for Open BuyBack Headers receipt
	 * 
	 * @param document
	 * @param noOfcolumns
	 * @param schema
	 * @param headerId
	 * @throws DocumentException
	 */
	public void createOpenBBOFData(Document document, int noOfcolumns, String schema, String dataLibrary, String headerId, 
			String alphaPlusWarehouseNo) throws DocumentException, Exception {

		log.info("Inside createOpenBBOFData method of BuybackProcessPDFService");

		PdfPTable table = new PdfPTable(noOfcolumns);
		table.getDefaultCell().setBorder(0);

		//this create Table header
		tableHeaderRow_OpenBBOF(document, table);

		//this is to create PDF header, footer and table data 
		getOpenBBOFContentFromDB(document, table, schema, dataLibrary, headerId, alphaPlusWarehouseNo);
	}


	/**
	 * This method is used to create Table Headers for open BuyBack Parts List.
	 */
	private void tableHeaderRow_OpenBBOF(Document document, PdfPTable table) {

		List<String> columnNames = Arrays.asList("PosNo", "Teilenummer", "Benennung", "Lagerorte", "Anzahl");

		for(int i=0; i < 5; i++) {
			PdfPCell cell = new PdfPCell(new Phrase(columnNames.get(i), font));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			if(i>3) {
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			}
			cell.setBorder(0);
			table.addCell(cell);
		}
		table.setSplitLate(false);
	}
	
	
	/**
	 * This method is used to Create Header for Open Buy Back receipt PDF
	 * @param document
	 * @param schema
	 * @param headerId
	 * @param pageCount
	 * @throws DocumentException
	 */
	public void getOpenBBOFHeaderData(Document document, String schema, String headerId, String alphaPlusWarehouseNo, Integer pageCount, 
			List<BuybackHeaderInfo>buyBackHeaderCommonList) throws DocumentException, Exception {

		PdfPTable mainTable = new PdfPTable(1);
		
		PdfPTable table = new PdfPTable(2);
		int[] columnWidth = {14, 86};

		if(pageCount.equals(1)) {
			List<BuybackHeaderInfo> inventoryList = getDBDataForHeaders(schema, headerId, alphaPlusWarehouseNo);
			buyBackHeaderCommonList = inventoryList;
		}

		if(buyBackHeaderCommonList != null && !buyBackHeaderCommonList.isEmpty()) {
			for(BuybackHeaderInfo bbofheaderObj : buyBackHeaderCommonList) {				
				
				mainTable.setWidthPercentage(100);
				mainTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
				mainTable.addCell(getCell(RestInputConstants.RPT_OPEN_BUYBACK_HEADER, PdfPCell.ALIGN_LEFT));

				table.setWidthPercentage(100);
				table.setWidths(columnWidth);
			
				table.addCell(getCell(RestInputConstants.BUYBACK_ORDERNO +": ", PdfPCell.ALIGN_LEFT));
				table.addCell(getCell(bbofheaderObj.getOrderNumber(), PdfPCell.ALIGN_LEFT));

				table.addCell(getCell(RestInputConstants.BUYBACK_DATE +": ", PdfPCell.ALIGN_LEFT));
				table.addCell(getCell(getCurrentDateTime(), PdfPCell.ALIGN_LEFT));
				
				table.addCell(getCell(RestInputConstants.WAREHOUSE_NAME +": " , PdfPCell.ALIGN_LEFT));
				table.addCell(getCell(bbofheaderObj.getAlphaXWarehousId().toString()+" - "+bbofheaderObj.getAlphaXWarehousName().toString(), PdfPCell.ALIGN_LEFT));
				
				table.addCell(getCell(RestInputConstants.BUYBACK_DESTINATION +": ", PdfPCell.ALIGN_LEFT));
				table.addCell(getCell(bbofheaderObj.getFaceLoctionId().toString()+ " - " +bbofheaderObj.getFaceLocation().toString(), PdfPCell.ALIGN_LEFT));
				
				mainTable.addCell(table);
			}
		}
		document.add(mainTable);
		Paragraph paragraph = new Paragraph();
		leaveEmptyLine(paragraph, 1);
		document.add(paragraph);
	}
	
	

	/**
	 * This method is used to create PDF header, footer and table data for open Buy back Parts receipt.
	 * @param document
	 * @param table
	 * @param schema
	 * @param headerId
	 * @throws DocumentException
	 */
	private void getOpenBBOFContentFromDB(Document document, PdfPTable table, String schema, String dataLibrary, String headerId, 
			String alphaPlusWarehouseNo) throws DocumentException, Exception {

		log.info("Inside getOpenBBOFContentFromDB method of BuybackProcessPDFService");

		int[] columnWidth = {8, 25, 30, 18, 15};
		Integer pageCount = 1;
		Integer position = 1;
		
		List<BuybackHeaderInfo> buyBackHeaderCommonList = new ArrayList<BuybackHeaderInfo>();

		try {

			StringBuilder buybackHeaderDetailsQuery = new StringBuilder(" SELECT PARTNRI, (SELECT BENEN FROM ").append(dataLibrary);
			buybackHeaderDetailsQuery.append(".E_ETSTAMM WHERE TNR = item.PARTNRI  AND LNR =").append(alphaPlusWarehouseNo).append(" AND HERST='DCAG') AS BENEN, ");
			buybackHeaderDetailsQuery.append("(SELECT LOPA FROM ").append(dataLibrary);
			buybackHeaderDetailsQuery.append(".E_ETSTAMM WHERE TNR = item.PARTNRI  AND LNR =").append(alphaPlusWarehouseNo).append(" AND HERST='DCAG') AS LOPA, ");
			buybackHeaderDetailsQuery.append(" SEND_QTY, QTY, BBOF_ITEM_ID FROM ").append(schema).append(".O_BBOFITEM item ");
			buybackHeaderDetailsQuery.append(" where BBOF_HEAD_ID =").append(headerId).append(" and BBOF_ITEM_STATE IN (0,1) order by BBOF_ITEM_ID");

			List<BuybackItemInfo> buybackPartsList = dbServiceRepository.getResultUsingQuery(BuybackItemInfo.class, buybackHeaderDetailsQuery.toString(), true);

			if(buybackPartsList!=null && !buybackPartsList.isEmpty()) {
				
				for(BuybackItemInfo bbofListObj : buybackPartsList) {
					
					if(position % 35 == 1) {
						getOpenBBOFHeaderData(document, schema, headerId, alphaPlusWarehouseNo, pageCount, buyBackHeaderCommonList);

						//This check is to call header row for table.
						if(position != 1) {
							tableHeaderRow_OpenBBOF(document, table);
						}	
					}

					table.setWidthPercentage(100);
					table.setWidths(columnWidth);
					table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

					table.addCell(new Phrase(String.valueOf(position), font));
					table.addCell(new Phrase(bbofListObj.getPartNumber(), font));
					table.addCell(new Phrase(bbofListObj.getPartName(), font));
					table.addCell(new Phrase(bbofListObj.getStorageLocation(), font));
					table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(new Phrase(String.valueOf(bbofListObj.getSendQuantity()!=null ? decimalformat_fixtwodigit.format(bbofListObj.getSendQuantity()): 
						decimalformat_fixtwodigit.format(0.00)), font));

					if(position % 35 == 0) {
						document.add(table);
						document.newPage();
						pageCount ++;
						table.flushContent();
					}
					
					position++;
					
					//get the LOPA list for each parts
					List<StorageLocationDTO> storageLocationList = partSevice.getStorageLocationList(bbofListObj.getPartNumber(), dataLibrary, 
							alphaPlusWarehouseNo, RestInputConstants.DCAG_STRING);
					
					if(storageLocationList!=null && !storageLocationList.isEmpty()) {
						for(StorageLocationDTO storageDto : storageLocationList) {
							
							if(!storageDto.getStorageLocation().equalsIgnoreCase(bbofListObj.getStorageLocation())) {
								table.addCell(new Phrase(" ", font));
								table.addCell(new Phrase(" ", font));
								table.addCell(new Phrase(" ", font));
								table.addCell(new Phrase(storageDto.getStorageLocation(), font));
								table.addCell(new Phrase(" ", font));
								position++;
							}							
						}
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
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "pdf for Open BBOF Receipt"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"pdf for Open BBOF Receipt"), exception);
			throw exception;
		}

	}
	
	
	
	/* ----------------           Closed BBOF REport Methods Starts         ----------------     */
	
	/**
	 * This method is used to generate PDF for Closed BuyBack Headers Summary
	 * 
	 * @param document
	 * @param noOfcolumns
	 * @param schema
	 * @param headerId
	 * @throws DocumentException
	 */
	public void createClosedBBOFData(Document document, int noOfcolumns, String schema, String dataLibrary, String headerId, 
			String alphaPlusWarehouseNo, String transferDate) throws DocumentException, Exception {

		log.info("Inside createClosedBBOFData method of BuybackProcessPDFService");

		PdfPTable table = new PdfPTable(noOfcolumns);
		table.getDefaultCell().setBorder(0);

		//this create Table header
		tableHeaderRow_ClosedBBOF(document, table);

		//this is to create PDF header, footer and table data 
		getClosedBBOFContentFromDB(document, table, schema, dataLibrary, headerId, alphaPlusWarehouseNo, transferDate);
		
		//2nd page for closed BBOF
		createDifferenceSumPosData(document, 5, schema, dataLibrary, headerId, alphaPlusWarehouseNo, transferDate);
		
	}
	
	
	/**
	 * This method is used to create Table Headers for Closed BuyBack Parts List.
	 */
	private void tableHeaderRow_ClosedBBOF(Document document, PdfPTable table) {

		List<String> columnNames = Arrays.asList("PosNo", "Teilenummer", "Benennung", "Anzahl", "DAK", "Erstattungs-betrag", "Differenz-summe");

		for(int i=0; i < 7; i++) {
			PdfPCell cell = new PdfPCell(new Phrase(columnNames.get(i), font));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			if(i>2) {
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			}
			cell.setBorder(0);
			table.addCell(cell);
		}
		table.setSplitLate(false);
	}
	
	
	/**
	 * This method is used to Create Header for Open Buy Back receipt PDF
	 * @param document
	 * @param schema
	 * @param headerId
	 * @param pageCount
	 * @throws DocumentException
	 */
	public void getClosedBBOFHeaderData(Document document, String schema, String headerId, String alphaPlusWarehouseNo, Integer pageCount, 
			List<BuybackHeaderInfo>buyBackHeaderCommonList) throws DocumentException, Exception {

		PdfPTable mainTable = new PdfPTable(1);
		
		PdfPTable table = new PdfPTable(2);
		int[] columnWidth = {14, 86};

		if(pageCount.equals(1)) {
			List<BuybackHeaderInfo> inventoryList = getDBDataForHeaders(schema, headerId, alphaPlusWarehouseNo);
			buyBackHeaderCommonList = inventoryList;
		}

		if(buyBackHeaderCommonList != null && !buyBackHeaderCommonList.isEmpty()) {
			for(BuybackHeaderInfo bbofheaderObj : buyBackHeaderCommonList) {				
				
				mainTable.setWidthPercentage(100);
				mainTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
				mainTable.addCell(getCell(RestInputConstants.RPT_CLOSED_BUYBACK_HEADER, PdfPCell.ALIGN_LEFT));

				table.setWidthPercentage(100);
				table.setWidths(columnWidth);
			
				table.addCell(getCell(RestInputConstants.BUYBACK_ORDERNO +": ", PdfPCell.ALIGN_LEFT));
				table.addCell(getCell(bbofheaderObj.getOrderNumber(), PdfPCell.ALIGN_LEFT));

				table.addCell(getCell(RestInputConstants.BUYBACK_DATE +": ", PdfPCell.ALIGN_LEFT));
				table.addCell(getCell(getCurrentDateTime(), PdfPCell.ALIGN_LEFT));
				
				table.addCell(getCell(RestInputConstants.WAREHOUSE_NAME +": " , PdfPCell.ALIGN_LEFT));
				table.addCell(getCell(bbofheaderObj.getAlphaXWarehousId().toString()+" - "+bbofheaderObj.getAlphaXWarehousName().toString(), PdfPCell.ALIGN_LEFT));
				
				table.addCell(getCell(RestInputConstants.BUYBACK_DESTINATION +": ", PdfPCell.ALIGN_LEFT));
				table.addCell(getCell(bbofheaderObj.getFaceLoctionId().toString()+ " - " +bbofheaderObj.getFaceLocation().toString(), PdfPCell.ALIGN_LEFT));
				
				mainTable.addCell(table);
				
				mainTable.addCell(getCell(" ", PdfPCell.ALIGN_LEFT));			
				mainTable.addCell(getCell(RestInputConstants.RPT_CLOSED_BUYBACK_STATIC_HEADER, PdfPCell.ALIGN_LEFT));
			}
		}
		document.add(mainTable);
		Paragraph paragraph = new Paragraph();
		leaveEmptyLine(paragraph, 1);
		document.add(paragraph);
	}
	
	
	/**
	 * This method is used to create PDF header, footer and table data for Closed Buy back Parts Summary.
	 * @param document
	 * @param table
	 * @param schema
	 * @param headerId
	 * @throws DocumentException
	 */
	private void getClosedBBOFContentFromDB(Document document, PdfPTable table, String schema, String dataLibrary, String headerId, 
			String alphaPlusWarehouseNo, String transferDate) throws DocumentException, Exception {

		log.info("Inside getClosedBBOFContentFromDB method of BuybackProcessPDFService");

		int[] columnWidth = {8, 20, 25, 10, 10, 15, 15};
		Integer pageCount = 1;
		Integer position = 1;
		Double differenceSum = 0.00;
		Double netValue = 0.00;
		Double dakValue = 0.00;
		Double quantityValue = 0.00;
		
		List<BuybackHeaderInfo> buyBackHeaderCommonList = new ArrayList<BuybackHeaderInfo>();

		try {

			StringBuilder buybackHeaderDetailsQuery = new StringBuilder(" SELECT PARTNRI, (SELECT BENEN FROM ").append(dataLibrary);
			buybackHeaderDetailsQuery.append(".E_ETSTAMM WHERE TNR = item.PARTNRI  AND LNR =").append(alphaPlusWarehouseNo).append(" AND HERST='DCAG') AS BENEN, ");
			buybackHeaderDetailsQuery.append(" SEND_QTY, QTY, SEND_DAK, NETVALUE, BBOF_ITEM_ID FROM ").append(schema).append(".O_BBOFITEM ").append("item ");
			buybackHeaderDetailsQuery.append(" where item.BBOF_HEAD_ID =").append(headerId).append(" and item.BBOF_ITEM_STATE = 2 ");
			buybackHeaderDetailsQuery.append(" and VARCHAR_FORMAT(item.TRANSFER_DATE, 'DD-MM-YYYY')='").append(transferDate).append("' order by BBOF_ITEM_ID");
			
			List<BuybackItemInfo> buybackPartsList = dbServiceRepository.getResultUsingQuery(BuybackItemInfo.class, buybackHeaderDetailsQuery.toString(), true);

			if(buybackPartsList!=null && !buybackPartsList.isEmpty()) {
				
				for(BuybackItemInfo bbofListObj : buybackPartsList) {

					if(position % 35 == 1) {
						getClosedBBOFHeaderData(document, schema, headerId, alphaPlusWarehouseNo, pageCount, buyBackHeaderCommonList);

						//This check is to call header row for table.
						if(position != 1) {
							tableHeaderRow_ClosedBBOF(document, table);
						}	
					}

					table.setWidthPercentage(100);
					table.setWidths(columnWidth);
					table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
					
					
					netValue = bbofListObj.getNetValue() !=null ? bbofListObj.getNetValue().doubleValue() : 0.00;
					dakValue = bbofListObj.getSendDAK() !=null ? bbofListObj.getSendDAK().doubleValue() : 0.00;
					quantityValue = bbofListObj.getSendQuantity() !=null ? bbofListObj.getSendQuantity().doubleValue() : 0.00;

					table.addCell(new Phrase(String.valueOf(position), font));
					table.addCell(new Phrase(bbofListObj.getPartNumber(), font));
					table.addCell(new Phrase(bbofListObj.getPartName(), font));
					
					table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(new Phrase(decimalformat_fixtwodigit.format(quantityValue), font));
					table.addCell(new Phrase(decimalformat_fixtwodigit.format(dakValue), font));
					table.addCell(new Phrase(decimalformat_fixtwodigit.format(netValue), font));
					
					differenceSum = (netValue - dakValue) * quantityValue;					
					
					table.addCell(new Phrase(String.valueOf(differenceSum !=null ? decimalformat_fixtwodigit.format(differenceSum): 
						decimalformat_fixtwodigit.format(0.00)), font));

					if(position % 35 == 0) {
						document.add(table);
						document.newPage();
						pageCount ++;
						table.flushContent();
					}
					
					position++;
				}
				document.add(table);
			}
		}
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "pdf for Closed BBOF parts Summary"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"pdf for Closed BBOF parts Summary"), exception);
			throw exception;
		}

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
	public void createDifferenceSumPosData(Document document, int noOfcolumns, String schema, String dataLibrary, 
			String headerId, String warehouseNumber, String transferDate) throws DocumentException, Exception {
		
		log.info("Inside createDifferenceSumPosData method of BuybackProcessPDFService");

		PdfPTable table = new PdfPTable(noOfcolumns);
		table.getDefaultCell().setBorder(0);

		//this create Table header
		createTableHeaderRow_DifferenceSum(document, table);

		//this is to data from DB 
		List<BookingAccount> finalAccountList = getTblContentFromDB_DifferenceSum(schema, dataLibrary, headerId, warehouseNumber, transferDate);
		
		//this is to create PDF header, footer and table data 
		createTableContent_DifferenceSum(document, table, schema, finalAccountList, headerId, warehouseNumber);
	}

	
	/**
	 * This method is used to create Table Headers.
	 * */
	private void createTableHeaderRow_DifferenceSum(Document document, PdfPTable table) {

		List<String> columnNames = Arrays.asList("Teileart", "Teilemarke", "Konto", "Kontoname", "Gesamtdifferenz(€)");

		for(int i=0; i < 5; i++) {
			PdfPCell cell = new PdfPCell(new Phrase(columnNames.get(i), font));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			if(i>3) {
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			}
			cell.setBorder(PdfPCell.NO_BORDER);
			table.addCell(cell);
		}		
		table.setSplitLate(false);
	}
	
	
	/**
	 * This method is used to create PDF header, footer and table data for Difference sum of Accepted positions.(Differenzen-Summe von akzeptierten Positionen).
	 * @param document
	 * @param table
	 * @param schema
	 * @param inventoryListId
	 * @throws DocumentException
	 */
	private List<BookingAccount> getTblContentFromDB_DifferenceSum(String schema, String dataLibrary, String headerId, String warehouseNumber, String transferDate) {

		log.info("Inside getTblContentFromDB_DifferenceSum method of BuybackProcessPDFService");

		Double totalCount = 0.0;

		List<BookingAccount> updatedAccountList = new ArrayList<>();

		try {
			List<BookingAccount> kontoDetailList = getKontoDetailList(schema, warehouseNumber);
			List<InventoryParts>  invPartList = getBBOFPartDetailList(schema, dataLibrary, headerId, warehouseNumber, transferDate);

			if(kontoDetailList!=null && !kontoDetailList.isEmpty()) {
				if(invPartList!=null && !invPartList.isEmpty()) {			
					
					List<BookingAccount> listOneList = kontoDetailList.stream().filter(two -> invPartList.stream()
							.anyMatch(one -> (one.getPartsIndikator() != null ? one.getPartsIndikator() : Integer.valueOf(0)).equals(Integer.parseInt(two.getPartsIndikator())) 
									&& two.getBrand().equals(one.getPartBrand()!=null ? one.getPartBrand() : ""))) 
							.collect(Collectors.toList());

					for(BookingAccount bAccount : listOneList) {

						totalCount = 0.0;

						for(InventoryParts invPart : invPartList) {

							if(bAccount.getPartsIndikator().equals(StringUtils.leftPad(invPart.getPartsIndikator().toString(), 2, "0")) 
									&& bAccount.getBrand().equals(invPart.getPartBrand())) {

								if(invPart.getOldStock() != null && invPart.getNewStock() != null ) {
									totalCount += invPart.getOldStock().doubleValue() * invPart.getNewStock().doubleValue();
								}								
							}			
						}

						BookingAccount ba = new BookingAccount(); 
						ba.setPartsIndikator(StringUtils.leftPad(bAccount.getPartsIndikator(), 5, "TA "));
						ba.setBrand(bAccount.getBrand());
						ba.setBookingNumber(bAccount.getBookingNumber());
						ba.setDescription(bAccount.getDescription());
						ba.setTotalCount(totalCount);

						updatedAccountList.add(ba);					
					}

					//for unmatched records
					List<InventoryParts> remainingInvPartList = invPartList.stream().filter(o1 -> listOneList.stream()
							.noneMatch(o2 -> Integer.valueOf(o2.getPartsIndikator()).equals(o1.getPartsIndikator()!=null ? o1.getPartsIndikator() : Integer.valueOf(0)) 
									&& o2.getBrand().equals(o1.getPartBrand() !=null ? o1.getPartBrand() : "")))
							.collect(Collectors.toList());

					if(remainingInvPartList!=null && !remainingInvPartList.isEmpty()) {

						totalCount = 0.0;

						for(InventoryParts invParts : remainingInvPartList) {

							if(invParts.getOldStock() != null && invParts.getNewStock() != null ) {
								totalCount += invParts.getOldStock().doubleValue() * invParts.getNewStock().doubleValue();
							}
						}

						BookingAccount ba = new BookingAccount();
						ba.setPartsIndikator("");
						ba.setBrand("");
						ba.setBookingNumber(BigDecimal.ZERO);
						ba.setDescription("Undefinierte Teilearten");
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
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "pdf for Closed BBOF parts Summary"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"pdf for Closed BBOF parts Summary"), exception);
			throw exception;
		}

		return updatedAccountList;
	}
	
	
	/**
	 * This method is used to get DB details
	 * */
	public List<BookingAccount> getKontoDetailList(String schema, String warehouseNumber) throws Exception  {
		
		warehouseNumber = StringUtils.leftPad(warehouseNumber, 2, "0");

		StringBuilder selectQuery = new StringBuilder("select BUK_TEIART, BUK_TMARKE, BUK_KONTO, BUK_BEZEI from ");
		selectQuery.append(schema).append(".PEI_BUKONT t where (t.BUK_LNR = '").append(warehouseNumber).append("' OR t.BUK_LNR = '*') AND ");
		selectQuery.append(" t.BUK_DTGULV = (select max(BUK_DTGULV) from ").append(schema).append(".PEI_BUKONT where VARCHAR_FORMAT(now(),'YYYY-MM-DD') > BUK_DTGULV ");
		selectQuery.append(" AND (BUK_LNR ='").append(warehouseNumber).append("' OR BUK_LNR = '*') and BUK_TMARKE = t.BUK_TMARKE and BUk_TEIART = t.BUk_TEIART) ");

		List<BookingAccount> bookingAccountList = dbServiceRepository.getResultUsingQuery(BookingAccount.class, selectQuery.toString(), true);

		return bookingAccountList;
	}

	
	/**
	 * This method is used to get DB details
	 * */
	public List<InventoryParts> getBBOFPartDetailList(String schema, String dataLibrary, String headerId, String warehouseNo, String transferDate) throws Exception  {
		
		StringBuilder buybackHeaderDetailsQuery = new StringBuilder(" SELECT PARTNRI as TNR, PARTKIND as TA, PARTBRAND as TMA_TMARKE, ");
		buybackHeaderDetailsQuery.append(" SEND_QTY as OLD_STOCK, SEND_DAK as NEW_STOCK FROM ").append(schema).append(".O_BBOFITEM item ");
		buybackHeaderDetailsQuery.append(" where item.BBOF_HEAD_ID =").append(headerId).append(" and item.BBOF_ITEM_STATE = 2 ");
		buybackHeaderDetailsQuery.append(" and VARCHAR_FORMAT(item.TRANSFER_DATE, 'DD-MM-YYYY') ='").append(transferDate).append("' order by BBOF_ITEM_ID");

		List<InventoryParts> inventoryPartList = dbServiceRepository.getResultUsingQuery(InventoryParts.class, buybackHeaderDetailsQuery.toString(), true);

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
			String headerId, String warehouseNumber) {

		log.info("Inside createTableContent_DifferenceSum method of BuybackProcessPDFService");

		int[] columnWidth = {12, 14, 18, 28, 25};
		Integer pageCount = 1;
		Integer count = 1;
		
		List<BuybackHeaderInfo> buyBackHeaderCommonList = new ArrayList<BuybackHeaderInfo>();

		try {
			if(bookingAccountList!=null && !bookingAccountList.isEmpty()) {
				for(BookingAccount bookingAcc : bookingAccountList) {

					if(count % 35 == 1) {
						document.newPage();
						getClosedBBOFHeaderData2(document, schema, headerId, warehouseNumber, pageCount, buyBackHeaderCommonList);
						
						//This check is to call header row for table.
						if(count != 1) {
							createTableHeaderRow_DifferenceSum(document, table);							
						}	
					}

					table.setWidthPercentage(100);
					table.setWidths(columnWidth);
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
					table.addCell(new Phrase(String.valueOf(decimalformat_fixFourdigit.format(bookingAcc.getTotalCount())), font));
					
					if(count % 35 == 0) {
						document.add(table);
						document.newPage();
						pageCount ++;
						table.flushContent();
					}
					count++;
				}
				document.add(table);
				document.add(getLineSeperator());
			}
		}
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "pdf for Closed BBOF parts Summary"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"pdf for Closed BBOF parts Summary"), exception);
			throw exception;
		}
	}

	
	/**
	 * This method is used to Create Header for Open Buy Back receipt PDF
	 * @param document
	 * @param schema
	 * @param headerId
	 * @param pageCount
	 * @throws DocumentException
	 */
	public void getClosedBBOFHeaderData2(Document document, String schema, String headerId, String alphaPlusWarehouseNo, Integer pageCount, 
			List<BuybackHeaderInfo>buyBackHeaderCommonList) throws DocumentException, Exception {

		PdfPTable mainTable = new PdfPTable(1);
		
		PdfPTable table = new PdfPTable(2);
		int[] columnWidth = {14, 86};

		if(pageCount.equals(1)) {
			List<BuybackHeaderInfo> inventoryList = getDBDataForHeaders(schema, headerId, alphaPlusWarehouseNo);
			buyBackHeaderCommonList = inventoryList;
		}

		if(buyBackHeaderCommonList != null && !buyBackHeaderCommonList.isEmpty()) {
			for(BuybackHeaderInfo bbofheaderObj : buyBackHeaderCommonList) {				
				
				mainTable.setWidthPercentage(100);
				mainTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
				mainTable.addCell(getCell(RestInputConstants.RPT_CLOSED_BUYBACK_HEADER, PdfPCell.ALIGN_LEFT));

				table.setWidthPercentage(100);
				table.setWidths(columnWidth);
			
				table.addCell(getCell(RestInputConstants.BUYBACK_ORDERNO +": ", PdfPCell.ALIGN_LEFT));
				table.addCell(getCell(bbofheaderObj.getOrderNumber(), PdfPCell.ALIGN_LEFT));

				table.addCell(getCell(RestInputConstants.BUYBACK_DATE +": ", PdfPCell.ALIGN_LEFT));
				table.addCell(getCell(getCurrentDateTime(), PdfPCell.ALIGN_LEFT));
				
				table.addCell(getCell(RestInputConstants.WAREHOUSE_NAME +": " , PdfPCell.ALIGN_LEFT));
				table.addCell(getCell(bbofheaderObj.getAlphaXWarehousId().toString()+" - "+bbofheaderObj.getAlphaXWarehousName().toString(), PdfPCell.ALIGN_LEFT));
				
				table.addCell(getCell(RestInputConstants.BUYBACK_DESTINATION +": ", PdfPCell.ALIGN_LEFT));
				table.addCell(getCell(bbofheaderObj.getFaceLoctionId().toString()+ " - " +bbofheaderObj.getFaceLocation().toString(), PdfPCell.ALIGN_LEFT));
				
				mainTable.addCell(table);
				
			}
		}
		document.add(mainTable);
		Paragraph paragraph = new Paragraph();
		leaveEmptyLine(paragraph, 1);
		document.add(paragraph);
	}
	
	
	/**
	 * This method is used to get DB details for Header 
	 * */
	public List<BuybackHeaderInfo> getDBDataForHeaders(String schema, String headerId, String alphaPlusWarehouseNo ) throws Exception  {
		
		StringBuilder buybackHeaderQuery = new StringBuilder(" SELECT BBOF_HEAD_ID, (SELECT WAREHOUS_ID FROM ").append(schema).append(".O_WRH WHERE ");
		buybackHeaderQuery.append("AP_WAREHOUS_ID = header.AP_LNR ) AS WAREHOUS_ID , (SELECT NAME FROM ").append(schema).append(".O_WRH WHERE ");
		buybackHeaderQuery.append("AP_WAREHOUS_ID = header.AP_LNR ) AS WAREHOUS_NAME, ORDERNO, FACINGLOC, ");
		buybackHeaderQuery.append("(Select LOCATION from ").append(schema).append(".O_BBOFLOC Where ENUM = header.FACINGLOC) As LOCATION  from ").append(schema);
		buybackHeaderQuery.append(".O_BBOFHEAD header WHERE header.BBOF_HEAD_ID = ").append(headerId).append(" AND header.AP_LNR = ").append(alphaPlusWarehouseNo);		

		List<BuybackHeaderInfo> bbofHeaderList = dbServiceRepository.getResultUsingQuery(BuybackHeaderInfo.class, buybackHeaderQuery.toString(), true);
		
		return bbofHeaderList;
	}
	
	
	/**
	 * This method convert currentDate time to string format
	 * @return date-time
	 */
	private String getCurrentDateTime(){
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		String localDateTime = dtf.format(LocalDate.now()); 
		
		return localDateTime;
	}
	

	public Chunk getLineSeperator() {
		
		CustomDashedLineSeparator separator = new CustomDashedLineSeparator();
		separator.setDash(3);
		separator.setGap(3);
		separator.setLineWidth(0.5f);

		Chunk linebreak = new Chunk(separator);
		
		return linebreak;
	}
	
	
	
}