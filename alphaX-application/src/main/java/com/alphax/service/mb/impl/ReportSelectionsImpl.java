package com.alphax.service.mb.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.SequenceInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.alphax.common.rest.message.Message;
import com.alphax.common.rest.message.service.MessageService;
import com.alphax.model.mb.PartDetails;
import com.alphax.model.mb.ReportForBookingAccountChangesList;
import com.alphax.model.mb.ReportForBookingStockDifferencesList;
import com.alphax.model.mb.ReportSelectionBookingList;
import com.alphax.model.mb.ReportSelectionIntraTradeList;
import com.alphax.model.mb.ReportSelections;
import com.alphax.common.exception.AlphaXException;
import com.alphax.common.exception.ExceptionMessages;
import com.alphax.repository.DBServiceRepository;
import com.alphax.repository.StubServiceRepository;
import com.alphax.service.mb.ReportSelectionsService;
import com.alphax.vo.mb.GlobalSearch;
import com.alphax.vo.mb.MovementSearchDTO;
import com.alphax.vo.mb.PartPrintingLabelDTO;
import com.alphax.vo.mb.ReportForBookingAccountChangesDTO;
import com.alphax.vo.mb.ReportForBookingStockDifferencesDTO;
import com.alphax.vo.mb.ReportSelectionIntraTradeDTO;
import com.alphax.vo.mb.ReportSelectionsDTO;
import com.alphax.vo.mb.ReportSelectionsForBusinessCaseDTO;
import com.alphax.vo.mb.ReportSelectionsForInventoryDTO;
import com.alphax.vo.mb.ReportSelectionsForMovementDTO;
import com.alphax.common.constants.RestInputConstants;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReportSelectionsImpl extends BaseService implements ReportSelectionsService {

	@Autowired
	DBServiceRepository dbServiceRepository;

	@Autowired
	private MessageService messageService;

	@Autowired
	StubServiceRepository stubServiceRepository;
	
	DecimalFormat decimalformat_Onedigit = new DecimalFormat("#0.0", new DecimalFormatSymbols(Locale.GERMAN));
	DecimalFormat decimalformat_Twodigit = new DecimalFormat("#0.00", new DecimalFormatSymbols(Locale.GERMAN));
	DecimalFormat decimalformat_Fourdigit = new DecimalFormat("#0.0000", new DecimalFormatSymbols(Locale.GERMAN));

	DecimalFormat decimalformat_fixtwodigit = new DecimalFormat("#0.00", new DecimalFormatSymbols(Locale.ENGLISH));

	/**
	 * This method is used for get (Selektion aus Bestand) Selections From Stock List
	 */

	@Override
	public GlobalSearch getSelectionsFromStock(String schema, String dataLibrary, String companyId, String allowedWarehouses, String manufacturer, 
			List<String> warehouseNos, boolean partExpiryDate, boolean withHoldings,  boolean withoutInventory, boolean negativeInventory, boolean relevantToTheft, 
			boolean oldPart, boolean recordType1, boolean recordType2, boolean hazardousMaterialPart, boolean multipleStorageLocations, String pageSize, String pageNumber) {

		log.info("Inside getSelectionsFromStock method of ReportSelectionsImpl");

		List<ReportSelectionsDTO> reportSelectionsList = new ArrayList<>();
		GlobalSearch globalSearchList = new GlobalSearch();

		try {
			validateCompany(companyId);

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

			//create where clause based on input value
			String whereClause = createWhereClause(schema, dataLibrary, allowedWarehouses, manufacturer, warehouseNos, partExpiryDate, withHoldings, 
					withoutInventory, negativeInventory, relevantToTheft, oldPart, recordType1, recordType2, hazardousMaterialPart, multipleStorageLocations);

			StringBuilder query = new StringBuilder("SELECT LNR as AP_Lager, (select NAME FROM ").append(schema).append(".O_WRH ");
			query.append("WHERE AP_Warehous_ID = LNR) as AX_Lager, HERST, TNR, BENEN, SA, AKTBES, DRTKZ , GFKZ, ");
			query.append("(select COUNT (*) FROM ").append(schema).append(".etk_tlekat WHERE");
			query.append(" TLE_LAZ > 0 and tle_tnr = TNR  ").append(" ) AS PARTEXPDATE  ");
			query.append(", (SELECT COUNT(*) FROM ").append(dataLibrary).append(".e_etstamm WHERE ");
			query.append(whereClause).append(" ) AS ROWNUMER FROM ");
			query.append(dataLibrary).append(".e_etstamm WHERE ");
			query.append(whereClause).append(" order by TNR OFFSET ");
			query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");


			List<ReportSelections> reportSelectionList = dbServiceRepository.getResultUsingQuery(ReportSelections.class, query.toString(), true);

			if (reportSelectionList != null && !reportSelectionList.isEmpty()) {

				for(ReportSelections reportSelection : reportSelectionList){
					ReportSelectionsDTO reportSelectionsDto = new ReportSelectionsDTO();

					reportSelectionsDto.setAlphaPlusWarehouseId(StringUtils.leftPad(String.valueOf(reportSelection.getAlphaPlusWarehouseId()),2,"0"));
					reportSelectionsDto.setAlphaxWarehouseName(reportSelection.getAlphaxWarehouseName());
					reportSelectionsDto.setStorageIndikator(String.valueOf(reportSelection.getStorageIndikator()));
					manufacturer = reportSelection.getManufacturer();
					reportSelectionsDto.setManufacturer(manufacturer!=null && manufacturer.equalsIgnoreCase(RestInputConstants.DCAG_STRING)? RestInputConstants.DAG_STRING : manufacturer);
					reportSelectionsDto.setPartNumber(reportSelection.getPartNumber());
					reportSelectionsDto.setName(reportSelection.getName());
					reportSelectionsDto.setCurrentStock(String.valueOf(reportSelection.getCurrentStock()));
					if(reportSelection.getPartExpDate() != null && reportSelection.getPartExpDate().equals("0") ) {
						reportSelectionsDto.setPartExpDate(false);
					}else
					{
						reportSelectionsDto.setPartExpDate(true);
					}

					reportSelectionsList.add(reportSelectionsDto);
				}

				globalSearchList.setSearchDetailsList(reportSelectionsList);
				globalSearchList.setTotalPages(Integer.toString(reportSelectionList.get(0).getTotalCount()));
				globalSearchList.setTotalRecordCnt(Integer.toString(reportSelectionList.get(0).getTotalCount()));

			}else{
				globalSearchList.setSearchDetailsList(reportSelectionsList);
				globalSearchList.setTotalPages(Integer.toString(0));
				globalSearchList.setTotalRecordCnt(Integer.toString(0));
			} 

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Selections from stock list"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Selections from stock list"), exception);
			throw exception;
		}

		return globalSearchList;
	}


	/**
	 * This method is used for create report from Stock  (Selektion aus Bestand - CSV) 
	 */	
	@Override
	public ByteArrayInputStream generateReportFromStock(String schema, String dataLibrary, String alphaXCompanyId, String allowedWarehouses, String manufacturer, 
			List<String> warehouseNos, boolean partExpiryDate, boolean withHoldings, boolean withoutInventory, boolean negativeInventory, boolean relevantToTheft, 
			boolean oldPart, boolean recordType1, boolean recordType2, boolean hazardousMaterialPart, boolean multipleStorageLocations) {

		log.info("Inside createReportFromStock method of ReportSelectionsImpl");

		ByteArrayInputStream  byteArrayOutputStream = null;

		final String[] headers = {"AP-Lager Nr.", "AX-Lager", "Hersteller", "Teilemarke", 
				"Teilenummer", "ES1", "ES2", "Bezeichnung", "Satzart", "aktueller Bestand", "Bestellausstand", "Lagerort", "Marketingcode", "Teileart", 
				"Leistungsart", "Rabattgruppe", "Listenpreis", "Mehrwertsteuer", "DAK", "Letzter Einkaufspreis", "fest hinterlegter Einkaufspreis", 
				"Rückgabewert", "neuer Listenpreis", "Neuer Listenpreis gültig ab", "Datum letzter Abgang", "Datum letzter Zugang", "Datum letzte Bewegung", 
				"Inventurzählmenge", "Umsatz ME Vorjahr", "Umsatz ME lfd. Jahr", "Preisupdate gesperrt", "Teilenummer Vorgänger", "Teilenummer Nachfolger", 
				"diebstahlrelevantes Kennzeichen", "REACH-Kennzeichen", "Zählgruppe"};

		//final CSVFormat format = CSVFormat.DEFAULT.withHeader(headers).withQuoteMode(QuoteMode.MINIMAL);

		final CSVFormat format = CSVFormat.RFC4180.withIgnoreSurroundingSpaces().
				withIgnoreEmptyLines().
				withTrim().
				withDelimiter(';').
				withQuote('"').
				//withRecordSeparator("\r\n").
				withQuoteMode(QuoteMode.ALL).
				withHeader(headers);

		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
				CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(out, "ISO-8859-1"), format);) {

			//validateCompany(companyId);

			//validate the Warehouse Ids
			validateWarehouses(allowedWarehouses);

			//create where clause based on input value
			String whereClause = createWhereClause(schema, dataLibrary, allowedWarehouses, manufacturer, warehouseNos, partExpiryDate,
					withHoldings, withoutInventory, negativeInventory, relevantToTheft, oldPart, recordType1, recordType2, hazardousMaterialPart, multipleStorageLocations);

			StringBuilder query = new StringBuilder("SELECT FIRMA as AP_Firma,").append("( SELECT NAME FROM ").append(schema).append(".o_company "); 
			query.append("WHERE CAST(AP_COMPANY_ID AS INT) = FIRMA ) as AX_Firma, FILIAL as AP_Filiale,").append("( SELECT NAME FROM ").append(schema).append(".o_agency "); 
			query.append("where CAST(AP_AGENCY_ID AS INT) =  FILIAL AND COMPANY_ID = ").append(alphaXCompanyId).append(" FETCH NEXT 1 ROWS ONLY ) as AX_Filiale, LNR as AP_Lager, (select NAME FROM ").append(schema).append(".O_WRH ");
			query.append("WHERE AP_Warehous_ID = LNR) as AX_Lager, HERST, TMARKE, TNR, BENEN, SA, AKTBES, BESAUS, LOPA,MC, TA , LEIART, RG, EPR , ");
			query.append(" (select substr(DATAFLD, 1, 2) from ").append(dataLibrary).append(".REFERENZ Where substr(KEYFLD, 7, 4) = '9907' and substr(KEYFLD, 11, 2) = MWST) as Mehrwertsteuer, ");
			query.append("DAK, LEKPR, NPREIS, TRUWER, ZPREIS, GPRDAT, DTLABG , DTLZUG , DTLBEW, IVZME , VKSVJS , VKLFJS ,  PRKZ , TNRV , ");
			query.append(" TNRN , DRTKZ , GFKZ , ZAEGRU FROM ");
			query.append(dataLibrary).append(".e_etstamm WHERE ");
			query.append(whereClause).append(" order by TNR  ");

			List<ReportSelections> reportSelectionList = dbServiceRepository.getResultUsingQuery(ReportSelections.class, query.toString(), true);

			if (reportSelectionList != null && !reportSelectionList.isEmpty()) {

				for(ReportSelections reportSelection : reportSelectionList){

					String partNumber = "";
					String es1 = "";
					String es2 = "";

					if(reportSelection.getManufacturer().equalsIgnoreCase("DCAG") && reportSelection.getPartNumber() != null){
						String fullPartNumber  = StringUtils.rightPad(reportSelection.getPartNumber(), 20, " ");
						partNumber = StringUtils.substring(fullPartNumber,0, 13);
						es1 = StringUtils.substring(fullPartNumber,13, 15);
						es2 = StringUtils.substring(fullPartNumber,15, 19);
					}else{
						partNumber = reportSelection.getPartNumber();

					}

					List<String> data = Arrays.asList(
							//StringUtils.leftPad(String.valueOf(reportSelection.getAlphaPlusCompanyId()),2,"0"),
							//reportSelection.getAlphaxCompanyName(),					
							//StringUtils.leftPad(String.valueOf(reportSelection.getAlphaPlusAgencyId()),2,"0"),
							//reportSelection.getAlphaxAgencyName(),
							StringUtils.leftPad(String.valueOf(reportSelection.getAlphaPlusWarehouseId()),2,"0"),
							reportSelection.getAlphaxWarehouseName(),
							reportSelection.getManufacturer()!=null && reportSelection.getManufacturer().equalsIgnoreCase(RestInputConstants.DCAG_STRING)? RestInputConstants.DAG_STRING :reportSelection.getManufacturer(),
									reportSelection.getPartBrand(),
									partNumber,
									es1,
									es2,
									reportSelection.getName(),
									replaceDotToComma(String.valueOf(reportSelection.getStorageIndikator())),
									replaceDotToComma(String.valueOf(reportSelection.getCurrentStock())),
									replaceDotToComma(String.valueOf(reportSelection.getPendingOrders())),
									reportSelection.getStorageLocation(),
									reportSelection.getMarketingCode(),
									replaceDotToComma(String.valueOf(reportSelection.getPartsIndikator())),
									replaceDotToComma(String.valueOf(reportSelection.getActivityType())),
									reportSelection.getDiscountGroup(),
									replaceDotToComma(String.valueOf(reportSelection.getPurchasePrice())),
									reportSelection.getValueAddedTax(),
									replaceDotToComma(String.valueOf(reportSelection.getAverageNetPrice())),
									replaceDotToComma(String.valueOf(reportSelection.getLastPurchasePrice())),
									replaceDotToComma(String.valueOf(reportSelection.getPreviousPurchasePrice())),
									replaceDotToComma(String.valueOf(reportSelection.getReturnValue())),
									replaceDotToComma(String.valueOf(reportSelection.getFuturePurchasePrice())),
									convertDateToString(String.valueOf(reportSelection.getFuturePriceFromDate())),
									convertDateToString(String.valueOf(reportSelection.getLastDisposalDate())),
									convertDateToString(String.valueOf(reportSelection.getLastReceiptDate())),
									convertDateToString(String.valueOf(reportSelection.getLastMovementDate())),
									replaceDotToComma(String.valueOf(reportSelection.getCurrentInventoryCount())),
									replaceDotToComma(String.valueOf(reportSelection.getSalesStockLastYear())),
									replaceDotToComma(String.valueOf(reportSelection.getCurrentYearSalesStock())),
									reportSelection.getPriceMark(),
									reportSelection.getPredecessorPartNumber(),
									reportSelection.getSuccessorPartNumber(),
									reportSelection.getTheftRelevantLicensePlate(),
									reportSelection.getReachLabel(),
									reportSelection.getInventoryGroup()
							);

					csvPrinter.printRecord(data);
				}

				// writing the underlying stream
				csvPrinter.flush();

				byteArrayOutputStream = new ByteArrayInputStream(out.toByteArray());
			}			

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Selections from stock list"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Selections from stock list"), exception);
			throw exception;
		}

		return byteArrayOutputStream;
	}


	private String createWhereClause(String schema, String dataLibrary, String allowedWarehouses, String manufacturer, List<String> warehouseNos, 
			boolean partExpiryDate, boolean withHoldings, boolean withoutInventory,	boolean negativeInventory, boolean relevantToTheft, 
			boolean oldPart, boolean recordType1, boolean recordType2, boolean hazardousMaterialPart, boolean multipleStorageLocations) {

		StringBuilder query = new StringBuilder("");
		
		String warehouseNo = StringUtils.join(warehouseNos, ",");

		query.append(" LNR IN( ").append(warehouseNo).append(" )");
		
		if(!manufacturer.equalsIgnoreCase("Alle")){
			manufacturer = (manufacturer != null && manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING))? RestInputConstants.DCAG_STRING : manufacturer;
			query.append(" AND HERST = '").append(manufacturer+"'");	
		}

		if(partExpiryDate){
			query.append(" AND (SELECT count(*) FROM ").append(schema).append(".etk_tlekat WHERE TLE_LAZ > 0 and tle_tnr = TNR ) > 0 ");
		}
		if(withHoldings){
			query.append(" AND aktbes > 0");
		}
		if(withoutInventory){
			query.append(" AND aktbes = 0 ");
		}
		if(negativeInventory){
			query.append(" AND aktbes < 0");
		}
		if(relevantToTheft){
			query.append(" AND DRTKZ <> '' ");
		}
		if(oldPart){
			//query.append(" AND substr(TNR, 14, 2) <> '' "); // ALPHAX-2812
			query.append(" AND substr(TNR, 14, 1)='7' ");
		}

		//Check SA1 and SA2 both are checked
		if(recordType1 && recordType2){
			query.append(" AND SA in ( 1, 2 )");
		}
		else {
			if(recordType1){
				query.append(" AND SA = 1");
			}
			if(recordType2){
				query.append(" AND SA = 2");
			}
		}
		
		if(hazardousMaterialPart){
			query.append(" AND (SELECT count(*) FROM ").append(schema).append(".etk_tlekat WHERE TLE_IMCO != '' and tle_tnr = TNR ) > 0 ");
		}
		
		if(multipleStorageLocations){
			query.append(" AND ( SA=1 OR (SA=2 AND AKTBES>0)) AND (select count(lopa) from ").append(dataLibrary).append(".e_lago where tnr= tnr and lnr=LNR) > 1 ");
		}

		return query.toString();
	}


	/**
	 * This method is used for get List (Selektion nach Gängigkeit) Selections based on marketability 
	 */
	@Override
	public GlobalSearch getSelectionsBasedMarketability(String schema, String dataLibrary, String companyId, String allowedWarehouses, String manufacturer, 
			List<String> warehouseNos, String noOfCommonParts, String periods, String sortOrder, String moventType, String pageSize, String pageNumber) {

		log.info("Inside getSelectionsBasedMarketability method of ReportSelectionsImpl");

		List<ReportSelectionsDTO> reportSelectionsList = new ArrayList<>();
		GlobalSearch globalSearchList = new GlobalSearch();

		try {
			validateCompany(companyId);

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

			//create where clause based on input value
			List<String> whereClause = createWhereClause(schema, dataLibrary, allowedWarehouses, manufacturer, warehouseNos, 
					noOfCommonParts, periods, sortOrder, moventType );

			StringBuilder query = new StringBuilder("SELECT LNR as AP_Lager, (select NAME FROM ").append(schema).append(".O_WRH ");
			query.append("WHERE AP_Warehous_ID = LNR) as AX_Lager, HERST, TNR, BENEN, SA, AKTBES, EPR, ").append(whereClause.get(1)).append(" as Durchschnitt, ");
			query.append("(SELECT COUNT(*) FROM ").append(dataLibrary).append(".e_etstamm WHERE ");
			query.append( StringUtils.substringBefore(whereClause.get(0), "ORDER BY") ).append(" ) AS ROWNUMER FROM ");
			query.append(dataLibrary).append(".e_etstamm WHERE ");
			query.append(whereClause.get(0)).append(" OFFSET ");
			query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");


			List<ReportSelections> reportSelectionList = dbServiceRepository.getResultUsingQuery(ReportSelections.class, query.toString(), true);

			if (reportSelectionList != null && !reportSelectionList.isEmpty()) {

				for(ReportSelections reportSelection : reportSelectionList){
					ReportSelectionsDTO reportSelectionsDto = new ReportSelectionsDTO();

					reportSelectionsDto.setAlphaPlusWarehouseId(StringUtils.leftPad(String.valueOf(reportSelection.getAlphaPlusWarehouseId()),2,"0"));
					reportSelectionsDto.setAlphaxWarehouseName(reportSelection.getAlphaxWarehouseName());
					reportSelectionsDto.setStorageIndikator(String.valueOf(reportSelection.getStorageIndikator()));
					manufacturer = reportSelection.getManufacturer();
					reportSelectionsDto.setManufacturer(manufacturer!=null && manufacturer.equalsIgnoreCase(RestInputConstants.DCAG_STRING)? RestInputConstants.DAG_STRING : manufacturer);
					reportSelectionsDto.setPartNumber(reportSelection.getPartNumber());
					reportSelectionsDto.setName(reportSelection.getName());
					reportSelectionsDto.setCurrentStock(String.valueOf(reportSelection.getCurrentStock()));
					reportSelectionsDto.setPurchasePrice(String.valueOf(reportSelection.getPurchasePrice()));
					reportSelectionsDto.setAverage(String.valueOf(reportSelection.getAverage()));

					reportSelectionsList.add(reportSelectionsDto);
				}

				globalSearchList.setSearchDetailsList(reportSelectionsList);
				globalSearchList.setTotalPages(Integer.toString(reportSelectionList.get(0).getTotalCount()));
				globalSearchList.setTotalRecordCnt(Integer.toString(reportSelectionList.get(0).getTotalCount()));

			}else{
				globalSearchList.setSearchDetailsList(reportSelectionsList);
				globalSearchList.setTotalPages(Integer.toString(0));
				globalSearchList.setTotalRecordCnt(Integer.toString(0));
			} 

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Selections based on marketability list"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Selections based on marketability list"), exception);
			throw exception;
		}

		return globalSearchList;
	}


	/**
	 * This method is used for create report based on marketability  (based on marketability - CSV) 
	 */

	@Override
	public ByteArrayInputStream generateReportBasedOnMarketability(String schema, String dataLibrary, String alphaXCompanyId, String allowedWarehouses, String manufacturer,
			List<String> warehouseNos, String noOfCommonParts, String periods, String sortOrder, String moventType ) {

		log.info("Inside generateReportBasedOnMarketability method of ReportSelectionsImpl");

		ByteArrayInputStream  byteArrayOutputStream = null;

		final String[] headers = { "AP-Firma Nr.",	"AX-Firma",	"AP-Filiale Nr.", "AX-Filiale",	"AP-Lager Nr.",	"AX-Lager",	"Hersteller", "Teilemarke", 
				"Teilenummer", "ES1", "ES2", "Bezeichnung", "Satzart", "aktueller Bestand", "Bestellausstand", "Lagerort", "Marketingcode", "Teileart", 
				"Leistungsart", "Rabattgruppe", "Listenpreis", "Mehrwertsteuer", "DAK", "Letzter Einkaufspreis", "fest hinterlegter Einkaufspreis", 
				"Rückgabewert", "neuer Listenpreis", "Neuer Listenpreis gültig ab", "Datum letzter Abgang", "Datum letzter Zugang", "Datum letzte Bewegung", 
				"Inventurzählmenge", "Umsatz ME Vorjahr", "Umsatz ME lfd. Jahr", "Preisupdate gesperrt", "Teilenummer Vorgänger", "Teilenummer Nachfolger", 
				"diebstahlrelevantes Kennzeichen", "REACH-Kennzeichen", "Zählgruppe" };

		//final CSVFormat format = CSVFormat.DEFAULT.withHeader(headers).withQuoteMode(QuoteMode.MINIMAL);

		final CSVFormat format = CSVFormat.RFC4180.withIgnoreSurroundingSpaces().
				withIgnoreEmptyLines().
				withTrim().
				withDelimiter(';').
				withQuote('"').
				//withRecordSeparator("\r\n").
				withQuoteMode(QuoteMode.ALL).
				withHeader(headers);

		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
				CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(out, "ISO-8859-1"), format);) {

			//validateCompany(companyId);

			//validate the Warehouse Ids
			validateWarehouses(allowedWarehouses);

			//create where clause based on input value
			List<String> whereClause = createWhereClause(schema, dataLibrary, allowedWarehouses, manufacturer, warehouseNos, 
					noOfCommonParts, periods, sortOrder, moventType );

			StringBuilder query = new StringBuilder("SELECT FIRMA as AP_Firma,").append("( SELECT NAME FROM ").append(schema).append(".o_company "); 
			query.append("WHERE CAST(AP_COMPANY_ID AS INT) = FIRMA ) as AX_Firma, FILIAL as AP_Filiale,").append("( SELECT NAME FROM ").append(schema).append(".o_agency "); 
			query.append("where CAST(AP_AGENCY_ID AS INT) =  FILIAL AND COMPANY_ID = ").append(alphaXCompanyId).append(" FETCH NEXT 1 ROWS ONLY ) as AX_Filiale, LNR as AP_Lager, (select NAME FROM ").append(schema).append(".O_WRH ");
			query.append("WHERE AP_Warehous_ID = LNR) as AX_Lager, HERST, TMARKE, TNR, BENEN, SA, AKTBES, BESAUS, LOPA,MC, TA , LEIART, RG, EPR , ");
			query.append(" (select substr(DATAFLD, 1, 2) from ").append(dataLibrary).append(".REFERENZ Where substr(KEYFLD, 7, 4) = '9907' and substr(KEYFLD, 11, 2) = MWST) as Mehrwertsteuer, ");
			query.append("DAK, LEKPR, NPREIS, TRUWER, ZPREIS, GPRDAT, DTLABG , DTLZUG , DTLBEW, IVZME , VKSVJS , VKLFJS ,  PRKZ , TNRV , ");
			query.append(" TNRN , DRTKZ , GFKZ , ZAEGRU FROM ");
			query.append(dataLibrary).append(".e_etstamm WHERE ");
			query.append(whereClause.get(0));

			List<ReportSelections> reportSelectionList = dbServiceRepository.getResultUsingQuery(ReportSelections.class, query.toString(), true);

			if (reportSelectionList != null && !reportSelectionList.isEmpty()) {

				for(ReportSelections reportSelection : reportSelectionList){

					String partNumber = "";
					String es1 = "";
					String es2 = "";

					if(reportSelection.getManufacturer().equalsIgnoreCase("DCAG") && reportSelection.getPartNumber() != null){
						String fullPartNumber = StringUtils.rightPad(reportSelection.getPartNumber(), 20, " ");
						partNumber = StringUtils.substring(fullPartNumber,0, 11);
						es1 = StringUtils.substring(fullPartNumber,13, 15);
						es2 = StringUtils.substring(fullPartNumber,15, 19);
					}
					else{
						partNumber = reportSelection.getPartNumber();
					}

					List<String> data = Arrays.asList(
							StringUtils.leftPad(String.valueOf(reportSelection.getAlphaPlusCompanyId()),2,"0"),
							reportSelection.getAlphaxCompanyName(),					
							StringUtils.leftPad(String.valueOf(reportSelection.getAlphaPlusAgencyId()),2,"0"),
							reportSelection.getAlphaxAgencyName(),
							StringUtils.leftPad(String.valueOf(reportSelection.getAlphaPlusWarehouseId()),2,"0"),
							reportSelection.getAlphaxWarehouseName(),
							reportSelection.getManufacturer()!=null && reportSelection.getManufacturer().equalsIgnoreCase(RestInputConstants.DCAG_STRING)? RestInputConstants.DAG_STRING :reportSelection.getManufacturer(),
									reportSelection.getPartBrand(),
									partNumber,
									es1,
									es2,
									reportSelection.getName(),
									replaceDotToComma(String.valueOf(reportSelection.getStorageIndikator())),
									replaceDotToComma(String.valueOf(reportSelection.getCurrentStock())),
									replaceDotToComma(String.valueOf(reportSelection.getPendingOrders())),
									reportSelection.getStorageLocation(),
									reportSelection.getMarketingCode(),
									replaceDotToComma(String.valueOf(reportSelection.getPartsIndikator())),
									replaceDotToComma(String.valueOf(reportSelection.getActivityType())),
									reportSelection.getDiscountGroup(),
									replaceDotToComma(String.valueOf(reportSelection.getPurchasePrice())),
									reportSelection.getValueAddedTax(),
									replaceDotToComma(String.valueOf(reportSelection.getAverageNetPrice())),
									replaceDotToComma(String.valueOf(reportSelection.getLastPurchasePrice())),
									replaceDotToComma(String.valueOf(reportSelection.getPreviousPurchasePrice())),
									replaceDotToComma(String.valueOf(reportSelection.getReturnValue())),
									replaceDotToComma(String.valueOf(reportSelection.getFuturePurchasePrice())),
									convertDateToString(String.valueOf(reportSelection.getFuturePriceFromDate())),
									convertDateToString(String.valueOf(reportSelection.getLastDisposalDate())),
									convertDateToString(String.valueOf(reportSelection.getLastReceiptDate())),
									convertDateToString(String.valueOf(reportSelection.getLastMovementDate())),
									replaceDotToComma(String.valueOf(reportSelection.getCurrentInventoryCount())),
									replaceDotToComma(String.valueOf(reportSelection.getSalesStockLastYear())),
									replaceDotToComma(String.valueOf(reportSelection.getCurrentYearSalesStock())),
									reportSelection.getPriceMark(),
									reportSelection.getPredecessorPartNumber(),
									reportSelection.getSuccessorPartNumber(),
									reportSelection.getTheftRelevantLicensePlate(),
									reportSelection.getReachLabel(),
									reportSelection.getInventoryGroup()
							);

					csvPrinter.printRecord(data);
				}

				// writing the underlying stream
				csvPrinter.flush();

				byteArrayOutputStream = new ByteArrayInputStream(out.toByteArray());
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Selections report based on marketability list"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Selections report based on marketability list"), exception);
			throw exception;
		}

		return byteArrayOutputStream;
	}


	private List<String> createWhereClause(String schema, String dataLibrary, String allowedWarehouses, String manufacturer,
			List<String> warehouseNos, String noOfCommonParts, String periods, String sortOrder, String moventType) {

		List<String> queryList = new ArrayList<String>();
		StringBuilder query = new StringBuilder("");
		String queryOrder = "";
		LocalDate today = LocalDate.now();
		
		String warehouseNo = StringUtils.join(warehouseNos, ",");

		log.info("Current Month : {} ", today.getMonthValue());
		String actualMonth = String.valueOf(today.getMonthValue());

		query.append("  LNR IN( ").append(warehouseNo).append(" )");	
		
		if(!manufacturer.equalsIgnoreCase("Alle")){
			manufacturer = (manufacturer != null && manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING))? RestInputConstants.DCAG_STRING : manufacturer;
			query.append(" AND HERST = '").append(manufacturer+"'");
		}
		

		//if condition :- Pieces (Gängigkeit nach Stück)
		if(moventType.equalsIgnoreCase("1")){

			// laufender Monat
			if(periods.equalsIgnoreCase("1")){
				query.append(" AND VKLFMS >= ").append(noOfCommonParts);
				queryOrder = "VKLFMS";
			}
			// laufendes Jahr
			else if(periods.equalsIgnoreCase("2")){
				query.append(" AND (VKLJ1S+VKLJ2S+VKLJ3S+VKLJ4S+VKLJ5S+VKLJ6S+VKLJ7S+VKLJ8S+VKLJ9S+VKLJAS+VKLJBS+VKLJCS)  >= ").append(noOfCommonParts);
				queryOrder = "(VKLJ1S+VKLJ2S+VKLJ3S+VKLJ4S+VKLJ5S+VKLJ6S+VKLJ7S+VKLJ8S+VKLJ9S+VKLJAS+VKLJBS+VKLJCS)";
			}
			// Durchschnitt der letzten 3 Monate
			else if(periods.equalsIgnoreCase("3")){					

				if(actualMonth.equalsIgnoreCase("1")){
					query.append(" AND  (VKVJAS+ VKVJBS +VKVJCS)/3 >= ").append(noOfCommonParts);	
					queryOrder = "(VKVJAS+ VKVJBS +VKVJCS)/3";
				}
				else if(actualMonth.equalsIgnoreCase("2")){
					query.append(" AND  (VKVJBS+ VKVJCS +VKLJ1S)/3 >= ").append(noOfCommonParts);
					queryOrder = "(VKVJBS+ VKVJCS +VKLJ1S)/3";
				}
				else if(actualMonth.equalsIgnoreCase("3")){
					query.append(" AND  (VKVJCS+ VKLJ1S +VKLJ2S)/3  >=  ").append(noOfCommonParts);
					queryOrder = "(VKVJCS+ VKLJ1S +VKLJ2S)/3";
				}
				// if month >= 4 and month <=10 
				else if(Integer.parseInt(actualMonth) >= 4 && Integer.parseInt(actualMonth) <= 10 ){
					int i = Integer.parseInt(actualMonth);
					StringBuilder orderByClause = new StringBuilder("");
					query.append(" AND  (VKLJ").append(i-3).append("S+VKLJ").append(i-2).append("S+VKLJ").append(i-1).append("S)/3 >=  ").append(noOfCommonParts);
					queryOrder = orderByClause.append("(VKLJ").append(i-3).append("S+VKLJ").append(i-2).append("S+VKLJ").append(i-1).append("S)/ 3").toString();
				}
				else if(actualMonth.equalsIgnoreCase("11")){
					query.append(" AND  (VKLJ8S+VKLJ9S+VKLJAS)/3 >=  ").append(noOfCommonParts);	
					queryOrder = "(VKLJ8S+VKLJ9S+VKLJAS)/3";
				}
				else if(actualMonth.equalsIgnoreCase("12")){
					query.append(" AND  (VKLJ9S+VKLJAS+VKLJBS)/3  >=  ").append(noOfCommonParts);
					queryOrder = "(VKLJ9S+VKLJAS+VKLJBS)/3";
				}
			}

			if(sortOrder.equalsIgnoreCase("1")){
				query.append(" ORDER BY ").append(queryOrder).append(" desc, tnr asc, lopa asc ");	
			}else if(sortOrder.equalsIgnoreCase("2")){
				query.append(" ORDER BY lopa asc, tnr asc,").append(queryOrder).append(" desc  ");	
			}else if(sortOrder.equalsIgnoreCase("3")){
				query.append(" ORDER BY ").append(queryOrder).append(" desc, lopa asc, tnr asc  ");	
			}

		}
		//if condition :- Money (Gängigkeit nach Wert)
		else if(moventType.equalsIgnoreCase("2")){

			if(periods.equalsIgnoreCase("1")){
				query.append(" AND VKLFMW >= ").append(noOfCommonParts);	
				queryOrder = "VKLFMW";
			}
			else if(periods.equalsIgnoreCase("2")){
				query.append(" AND VKLFJW  >= ").append(noOfCommonParts);	
				queryOrder = "VKLFJW";
			}
			else if(periods.equalsIgnoreCase("3")){					

				if(actualMonth.equalsIgnoreCase("1")){
					query.append(" AND  (VKVJAW+ VKVJBW +VKVJCW)/3  >=  ").append(noOfCommonParts);	
					queryOrder = "(VKVJAW+ VKVJBW +VKVJCW)/3";
				}
				else if(actualMonth.equalsIgnoreCase("2")){
					query.append(" AND  (VKVJBW+ VKVJCW +VKLJ1W)/3  >=  ").append(noOfCommonParts);
					queryOrder = "(VKVJBW+ VKVJCW +VKLJ1W)/3 ";
				}
				else if(actualMonth.equalsIgnoreCase("3")){
					query.append(" AND  (VKVJCW+ VKLJ1W +VKLJ2W)/3   >=  ").append(noOfCommonParts);
					queryOrder = "(VKVJCW+ VKLJ1W +VKLJ2W)/3";
				}
				else if(Integer.parseInt(actualMonth) >= 4 && Integer.parseInt(actualMonth) <= 10 ){
					int i = Integer.parseInt(actualMonth);
					StringBuilder orderByClause = new StringBuilder("");
					query.append(" AND  (VKLJ").append(i-3).append("W+VKLJ").append(i-2).append("W+VKLJ").append(i-1).append("W)/3 >=  ").append(noOfCommonParts);
					queryOrder = orderByClause.append("(VKLJ").append(i-3).append("W+VKLJ").append(i-2).append("W+VKLJ").append(i-1).append("W)/ 3").toString();
				}
				else if(actualMonth.equalsIgnoreCase("11")){
					query.append(" AND (VKLJ8W+ VKLJ9W+ VKLJAW)/3  >=  ").append(noOfCommonParts);
					queryOrder = "(VKLJ8W+ VKLJ9W+ VKLJAW)/3 ";
				}
				else if(actualMonth.equalsIgnoreCase("12")){
					query.append(" AND  (VKLJ9W+ VKLJAW+ VKLJBW)/3   >=  ").append(noOfCommonParts);	
					queryOrder = "(VKLJ9W+ VKLJAW+ VKLJBW)/3";
				}

			}

			if(sortOrder.equalsIgnoreCase("1")){
				query.append(" ORDER BY ").append(queryOrder).append(" desc, tnr asc, lopa asc ");	
			}else if(sortOrder.equalsIgnoreCase("2")){
				query.append(" ORDER BY lopa asc, tnr asc,").append(queryOrder).append(" desc  ");	
			}else if(sortOrder.equalsIgnoreCase("3")){
				query.append(" ORDER BY ").append(queryOrder).append(" desc, lopa asc, tnr asc  ");	
			}
		}

		//add data to list
		queryList.add(query.toString());
		queryList.add(queryOrder);

		return queryList;	
	}


	/**
	 * This method is used for get Selections based on Inventory Structure (Bestandstruktur) List
	 */
	@Override
	public List<ReportSelectionsForInventoryDTO> getSelectionsForInventory(String schema, String dataLibrary, String companyId, String allowedWarehouses, 
			List<String> warehouseNos, String manufacturer, String partBrand, String selectedType) {

		log.info("Inside getSelectionsForInventory method of ReportSelectionsImpl");

		List<ReportSelectionsForInventoryDTO> reportSelectionsList = new ArrayList<>();
		String whereClause = "";
		try {
			validateCompany(companyId);

			//validate the Warehouse Ids
			validateWarehouses(allowedWarehouses);

			//create where clause based on input value
			//String whereClause = createWhereClause(schema, dataLibrary, allowedWarehouses, manufacturer, warehouseNo, partBrand);

			switch(selectedType) {

			case "1":    //Part Price range  ( Preisstruktur )
				whereClause = createWhereClause(schema, dataLibrary, allowedWarehouses, warehouseNos, null, null);
				reportSelectionsList = getSelectionsBasedOnPartsPriceRange(schema, dataLibrary,whereClause );	
				break;
			case "2":    //Record Type ( Satzarten )
				whereClause = createWhereClause(schema, dataLibrary, allowedWarehouses, warehouseNos, null, null);
				reportSelectionsList = getSelectionsBasedOnRecordType(dataLibrary,whereClause);
				break;
			case "3":    //Part Type   ( Teilearten )
				whereClause = createWhereClause(schema, dataLibrary, allowedWarehouses, warehouseNos, null, null);
				reportSelectionsList = getSelectionsBasedOnPartType(schema, dataLibrary,whereClause );	
				break;
			case "4":    //Part Brand   ( Marken )
				whereClause = createWhereClause(schema, dataLibrary, allowedWarehouses, warehouseNos, manufacturer, null);
				reportSelectionsList = getSelectionsBasedOnPartBrand(dataLibrary,whereClause );	
				break;
			case "5":    //Discount Group  ( Rabattgruppen )
				whereClause = createWhereClause(schema, dataLibrary, allowedWarehouses, warehouseNos, manufacturer, partBrand);
				reportSelectionsList = getSelectionsBasedOnDiscountGroup(dataLibrary,whereClause );
				break;
			case "6":    //Category  ( Inventurkennzeichen )
				whereClause = createWhereClause(schema, dataLibrary, allowedWarehouses, warehouseNos, null, null);
				reportSelectionsList = getSelectionsBasedOnCategory(dataLibrary,whereClause);
				break;
			default:
				log.info("Input type is not valid");
				break;			
			}


		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Inventory Structure (Bestandstruktur) "));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Inventory Structure (Bestandstruktur)"), exception);
			throw exception;
		}

		return reportSelectionsList;
	}


	private List<ReportSelectionsForInventoryDTO> getSelectionsBasedOnPartType(String schema, String dataLibrary, String whereClause) {

		log.info("Start getSelectionsBasedOnpartType method");

		List<ReportSelectionsForInventoryDTO> selectionsList = new ArrayList<>();

		StringBuilder query1 = new StringBuilder("SELECT COUNT(*) AS COUNT FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE ").append(whereClause);

		StringBuilder query2 = new StringBuilder("SELECT SUM(AKTBES*DAK) AS COUNT FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE ").append(whereClause);

		StringBuilder partTypes = new StringBuilder("SELECT PARTKIND_ID, KINDNAME FROM  ").append(schema).append(".O_PARTKIND WHERE ISACTIVE = 1");

		String result1 = dbServiceRepository.getCountUsingQuery(query1.toString());
		String result2 = dbServiceRepository.getCountUsingQuery(query2.toString());
		List<ReportSelections> reportSelectionList = dbServiceRepository.getResultUsingQuery(ReportSelections.class, partTypes.toString(), true);

		if (reportSelectionList != null && !reportSelectionList.isEmpty()) {

			for(ReportSelections reportSelection : reportSelectionList){

				String partKindId = String.valueOf(reportSelection.getPartKindId());
				String kindName = reportSelection.getPartType();

				StringBuilder query3 = new StringBuilder("SELECT COUNT(*) AS COUNT FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE ");
				query3.append("TA = ").append(partKindId).append(" AND ").append(whereClause);

				StringBuilder query4 = new StringBuilder("SELECT SUM(AKTBES*DAK) AS COUNT FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE ");
				query4.append("TA = ").append(partKindId).append(" AND ").append(whereClause);

				String result3 = dbServiceRepository.getCountUsingQuery(query3.toString());
				String result4 = dbServiceRepository.getCountUsingQuery(query4.toString());

				selectionsList.add(getResultsForInventory(partKindId+" - "+kindName, result1, result2, result3, result4));

			}
		}

		return selectionsList;
	}


	private List<ReportSelectionsForInventoryDTO> getSelectionsBasedOnPartsPriceRange(String schema, String dataLibrary,
			String whereClause) {
		log.info("Start getSelectionsBasedOnPartsPriceRange method");

		List<ReportSelectionsForInventoryDTO> selectionsList = new ArrayList<>();

		StringBuilder query1 = new StringBuilder("SELECT COUNT(*) AS COUNT FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE ").append(whereClause);

		StringBuilder query2 = new StringBuilder("SELECT SUM(AKTBES*DAK) AS COUNT FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE ").append(whereClause);

		StringBuilder partsPriceRange = new StringBuilder("SELECT FROM_CUR, TO_CUR FROM ").append(schema).append(".O_CURRANGE WHERE ISACTIVE = 1 AND DATASET = 1 ");

		String result1 = dbServiceRepository.getCountUsingQuery(query1.toString());
		String result2 = dbServiceRepository.getCountUsingQuery(query2.toString());
		List<ReportSelections> reportSelectionList = dbServiceRepository.getResultUsingQuery(ReportSelections.class, partsPriceRange.toString(), true);

		if (reportSelectionList != null && !reportSelectionList.isEmpty()) {

			for(ReportSelections reportSelection : reportSelectionList){

				String fromCur = String.valueOf(reportSelection.getFromCurrency());
				String toCur = String.valueOf(reportSelection.getToCurrency());

				StringBuilder query3 = new StringBuilder("SELECT COUNT(*) AS COUNT FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE ");
				query3.append("EPR >= ").append(fromCur).append(" AND EPR < ").append(toCur).append(" AND ").append(whereClause);

				StringBuilder query4 = new StringBuilder("SELECT SUM(AKTBES*DAK) AS COUNT FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE ");
				query4.append("EPR >= ").append(fromCur).append(" AND EPR < ").append(toCur).append(" AND ").append(whereClause);

				String result3 = dbServiceRepository.getCountUsingQuery(query3.toString());
				String result4 = dbServiceRepository.getCountUsingQuery(query4.toString());

				ReportSelectionsForInventoryDTO reportSelectionsForInventoryDTO = getResultsForInventory(toCur, result1, result2, result3, result4);
				reportSelectionsForInventoryDTO.setFromCurValue(fromCur);
				selectionsList.add(reportSelectionsForInventoryDTO);

			}
		}

		return selectionsList;
	}


	private List<ReportSelectionsForInventoryDTO> getSelectionsBasedOnPartBrand(String dataLibrary,
			String whereClause) {

		log.info("Start getSelectionsBasedOnPartBrand method");

		List<ReportSelectionsForInventoryDTO> selectionsList = new ArrayList<>();

		StringBuilder query1 = new StringBuilder("SELECT COUNT(*) AS COUNT FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE ").append(whereClause);

		StringBuilder query2 = new StringBuilder("SELECT SUM(AKTBES*DAK) AS COUNT FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE ").append(whereClause);

		StringBuilder partBrands = new StringBuilder("SELECT DISTINCT TMARKE FROM  ").append(dataLibrary).append(".E_ETSTAMM WHERE ").append(whereClause);

		String result1 = dbServiceRepository.getCountUsingQuery(query1.toString());
		String result2 = dbServiceRepository.getCountUsingQuery(query2.toString());
		List<ReportSelections> reportSelectionList = dbServiceRepository.getResultUsingQuery(ReportSelections.class, partBrands.toString(), true);

		if (reportSelectionList != null && !reportSelectionList.isEmpty()) {

			for(ReportSelections reportSelection : reportSelectionList){

				String partBrand = reportSelection.getPartBrand();

				StringBuilder query3 = new StringBuilder("SELECT COUNT(*) AS COUNT FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE ");
				query3.append("TMARKE = '").append(partBrand).append("' AND ").append(whereClause);

				StringBuilder query4 = new StringBuilder("SELECT SUM(AKTBES*DAK) AS COUNT FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE ");
				query4.append("TMARKE = '").append(partBrand).append("' AND ").append(whereClause);

				String result3 = dbServiceRepository.getCountUsingQuery(query3.toString());
				String result4 = dbServiceRepository.getCountUsingQuery(query4.toString());

				selectionsList.add(getResultsForInventory(partBrand, result1, result2, result3, result4));

			}
		}

		return selectionsList;
	}


	/**
	 * This method is used for get Selections based on Discount Group (Rabattgruppen) List
	 * @param partBrand 
	 * @param manufacturer 
	 */

	private List<ReportSelectionsForInventoryDTO> getSelectionsBasedOnDiscountGroup(String dataLibrary,String whereClause) {

		log.info("Start getSelectionsBasedOnDiscountGroup method");

		List<ReportSelectionsForInventoryDTO> selectionsList = new ArrayList<>();

		StringBuilder query1 = new StringBuilder("SELECT COUNT(*) AS COUNT FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE ").append(whereClause);

		StringBuilder query2 = new StringBuilder("SELECT SUM(AKTBES*DAK) AS COUNT FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE ").append(whereClause);

		StringBuilder distinctRG = new StringBuilder("SELECT DISTINCT RG FROM  ").append(dataLibrary).append(".E_ETSTAMM WHERE ").append(whereClause).append(" order by RG");

		String result1 = dbServiceRepository.getCountUsingQuery(query1.toString());
		String result2 = dbServiceRepository.getCountUsingQuery(query2.toString());
		List<ReportSelections> reportSelectionList = dbServiceRepository.getResultUsingQuery(ReportSelections.class, distinctRG.toString(), true);

		if (reportSelectionList != null && !reportSelectionList.isEmpty()) {

			for(ReportSelections reportSelection : reportSelectionList){

				String discountGroup = reportSelection.getDiscountGroup();

				StringBuilder query3 = new StringBuilder("SELECT COUNT(*) AS COUNT FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE ");
				query3.append("RG = '").append(discountGroup).append("' AND ").append(whereClause);

				StringBuilder query4 = new StringBuilder("SELECT SUM(AKTBES*DAK) AS COUNT FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE ");
				query4.append("RG = '").append(discountGroup).append("' AND ").append(whereClause);

				String result3 = dbServiceRepository.getCountUsingQuery(query3.toString());
				String result4 = dbServiceRepository.getCountUsingQuery(query4.toString());

				selectionsList.add(getResultsForInventory(discountGroup, result1, result2, result3, result4));

			}
		}

		return selectionsList;
	}


	/**
	 * This method is used for get Selections based Category (Inventurkennzeichen) List
	 */

	private List<ReportSelectionsForInventoryDTO> getSelectionsBasedOnCategory(String dataLibrary, String whereClause) {

		log.info("Start getSelectionsBasedOnCategory method");

		List<ReportSelectionsForInventoryDTO> selectionsList = new ArrayList<>();

		StringBuilder query1 = new StringBuilder("SELECT COUNT(*) AS COUNT FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE ").append(whereClause);

		StringBuilder query2 = new StringBuilder("SELECT SUM(AKTBES*DAK) AS COUNT FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE ").append(whereClause);

		StringBuilder query3 = new StringBuilder("SELECT COUNT(*) AS COUNT FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE SA=1 AND IVKZ <> '1' AND ").append(whereClause);

		StringBuilder query4 = new StringBuilder("SELECT SUM(AKTBES*DAK) AS COUNT FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE SA=1 AND IVKZ <> '1' AND ").append(whereClause);

		StringBuilder query5 = new StringBuilder("SELECT COUNT(*) AS COUNT FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE SA=1 AND IVKZ='1' AND ").append(whereClause);

		StringBuilder query6 = new StringBuilder("SELECT SUM(AKTBES*DAK) AS COUNT FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE SA=1 AND IVKZ='1' AND ").append(whereClause);

		StringBuilder query7 = new StringBuilder("SELECT COUNT(*) AS COUNT FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE SA=2 AND IVKZ <> '1' AND ").append(whereClause);

		StringBuilder query8 = new StringBuilder("SELECT SUM(AKTBES*DAK) AS COUNT FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE SA=2 AND IVKZ <> '1' AND ").append(whereClause);

		StringBuilder query9 = new StringBuilder("SELECT COUNT(*) AS COUNT FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE SA=2 AND IVKZ= '1' AND ").append(whereClause);

		StringBuilder query10 = new StringBuilder("SELECT SUM(AKTBES*DAK) AS COUNT FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE SA=2 AND IVKZ= '1' AND ").append(whereClause);


		String result1 = dbServiceRepository.getCountUsingQuery(query1.toString());
		String result2 = dbServiceRepository.getCountUsingQuery(query2.toString());
		String result3 = dbServiceRepository.getCountUsingQuery(query3.toString());
		String result4 = dbServiceRepository.getCountUsingQuery(query4.toString());
		String result5 = dbServiceRepository.getCountUsingQuery(query5.toString());
		String result6 = dbServiceRepository.getCountUsingQuery(query6.toString());
		String result7 = dbServiceRepository.getCountUsingQuery(query7.toString());
		String result8 = dbServiceRepository.getCountUsingQuery(query8.toString());
		String result9 = dbServiceRepository.getCountUsingQuery(query9.toString());
		String result10 = dbServiceRepository.getCountUsingQuery(query10.toString());

		log.info("For Bestandsposition ungezählt ");
		selectionsList.add(getResultsForInventory("Bestandsposition ungezählt", result1, result2, result3, result4));
		log.info("For Bestandsposition gezählt ");
		selectionsList.add(getResultsForInventory("Bestandsposition gezählt", result1, result2, result5, result6));
		log.info("For Kundenposition ungezählt ");
		selectionsList.add(getResultsForInventory("Kundenposition ungezählt", result1, result2, result7, result8));
		log.info("For Kundenposition gezählt ");
		selectionsList.add(getResultsForInventory("Kundenposition gezählt", result1, result2, result9, result10));

		return selectionsList;


	}


	/**
	 * This method is used for get Selections based record type (Satzart) List
	 */

	private List<ReportSelectionsForInventoryDTO> getSelectionsBasedOnRecordType(String dataLibrary, String whereClause) {

		log.info("Start getSelectionsBasedOnRecordType method");

		List<ReportSelectionsForInventoryDTO> selectionsList = new ArrayList<>();

		StringBuilder query1 = new StringBuilder("SELECT COUNT(*) AS COUNT FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE ").append(whereClause);

		StringBuilder query2 = new StringBuilder("SELECT SUM(AKTBES*DAK) AS COUNT FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE ").append(whereClause);

		StringBuilder query3 = new StringBuilder("SELECT COUNT(*) AS COUNT FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE SA = 1 AND ").append(whereClause);

		StringBuilder query4 = new StringBuilder("SELECT SUM(AKTBES*DAK) AS COUNT FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE SA = 1 AND ").append(whereClause);

		StringBuilder query5 = new StringBuilder("SELECT COUNT(*) AS COUNT FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE SA = 2 AND ").append(whereClause);

		StringBuilder query6 = new StringBuilder("SELECT SUM(AKTBES*DAK) AS COUNT FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE SA = 2 AND ").append(whereClause);

		String result1 = dbServiceRepository.getCountUsingQuery(query1.toString());
		String result2 = dbServiceRepository.getCountUsingQuery(query2.toString());
		String result3 = dbServiceRepository.getCountUsingQuery(query3.toString());
		String result4 = dbServiceRepository.getCountUsingQuery(query4.toString());
		String result5 = dbServiceRepository.getCountUsingQuery(query5.toString());
		String result6 = dbServiceRepository.getCountUsingQuery(query6.toString());

		log.info("For SA- 01 - Bestandspositionen");
		selectionsList.add(getResultsForInventory("01 - Bestandspositionen", result1, result2, result3, result4));
		log.info("For SA- 02 - Kundenpositionen");
		selectionsList.add(getResultsForInventory("02 - Kundenpositionen", result1, result2, result5, result6));

		return selectionsList;

	}

	private String createWhereClause(String schema, String dataLibrary, String allowedWarehouses, List<String> warehouseNos, 
			String manufacturer, String partBrand) {

		StringBuilder query = new StringBuilder("");
		String warehouseNo = StringUtils.join(warehouseNos, ",");

		query.append(" LNR IN( ").append(warehouseNo).append(" )");

		if(manufacturer != null && !manufacturer.isEmpty()){
			manufacturer = (manufacturer != null && manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING))? RestInputConstants.DCAG_STRING : manufacturer;
			query.append(" AND HERST = '").append(manufacturer+"'");	
		}

		if(partBrand != null && !partBrand.isEmpty()){
			query.append(" AND TMARKE = '").append(partBrand+"'");	
		}

		return query.toString();	
	}


	/**
	 * This method is used Inventory Structure calculation
	 */
	private ReportSelectionsForInventoryDTO getResultsForInventory(String selectedTypeValue, String result1, String result2, String result3, String result4) {

		ReportSelectionsForInventoryDTO selectionsForInventoryDTO = new ReportSelectionsForInventoryDTO();
		String inventoryValueInPct = "0.0";
		String positionsInPct = "0.0";
		String inventoryValueInEur = "0.00";
		String numberOfPositions = "0.0";
		BigDecimal multiplyBy = new BigDecimal(100);

		if(result1 != null  && !result1.equalsIgnoreCase("0") && result3!= null  ){

			BigDecimal bigDecimalResult3 = new BigDecimal(result3);
			BigDecimal bigDecimalResult1 = new BigDecimal(result1);
			numberOfPositions = String.valueOf(bigDecimalResult3);
			log.debug("Result3 :"+bigDecimalResult3 +" Result1 :"+bigDecimalResult1);//need to renove this

			positionsInPct = String.valueOf((bigDecimalResult3.multiply(multiplyBy)).divide(bigDecimalResult1,2,RoundingMode.DOWN));
			log.debug("positionsInPct :"+positionsInPct);//need to renove this
		}

		if(result2 != null && !result2.equalsIgnoreCase("0") && result4!= null  ){
			BigDecimal bigDecimalResult4 = new BigDecimal(result4);
			BigDecimal bigDecimalResult2 = new BigDecimal(result2);
			inventoryValueInEur = String.valueOf(bigDecimalResult4.setScale(2,RoundingMode.DOWN));
			log.debug("Result4 :"+bigDecimalResult4 +" Result2 :"+bigDecimalResult2);//need to renove this

			inventoryValueInPct = String.valueOf((bigDecimalResult4.multiply(multiplyBy)).divide(bigDecimalResult2,2,RoundingMode.DOWN)); 
			log.debug("inventoryValueInPct :"+inventoryValueInPct); //need to renove this
		}

		selectionsForInventoryDTO.setLeadingColumnValue(selectedTypeValue);
		selectionsForInventoryDTO.setInventoryValueInEur(inventoryValueInEur);
		selectionsForInventoryDTO.setInventoryValueInPct(inventoryValueInPct);
		selectionsForInventoryDTO.setNumberOfPositions(numberOfPositions);
		selectionsForInventoryDTO.setPositionsInPct(positionsInPct);

		return selectionsForInventoryDTO;

	}


	String replaceDotToComma(String value){

		if(value != null && !value.isEmpty()){
			value = value.replace(".", ",")	;
		}

		return value;		
	}


	/**
	 * This method is used for create report from Inventory Structure ( Bestandstruktur - CSV )
	 */	
	@Override
	public ByteArrayInputStream generateReportForInventoryStructure(String schema, String dataLibrary, String alphaXcompanyId, String allowedWarehouses, 
			List<ReportSelectionsForInventoryDTO> selectedInventoryList, String selectedType, List<String> warehouseNos, String manufacturer, String partBrand ) {

		log.info("Inside generateReportForInventoryStructure method of ReportSelectionsImpl");

		ByteArrayInputStream  byteArrayOutputStream = null;
		List<ReportSelections> reportSelectionList = new ArrayList<>();

		final String[] headers = {"AP-Firma Nr.",	"AX-Firma",	"AP-Filiale Nr.", "AX-Filiale",	"AP-Lager Nr.",	"AX-Lager",	"Hersteller", "Teilemarke", 
				"Teilenummer", "ES1", "ES2", "Bezeichnung", "Satzart", "aktueller Bestand", "Bestellausstand", "Lagerort", "Marketingcode", "Teileart", 
				"Leistungsart", "Rabattgruppe", "Listenpreis", "Mehrwertsteuer", "DAK", "Letzter Einkaufspreis", "fest hinterlegter Einkaufspreis", 
				"Rückgabewert", "neuer Listenpreis", "Neuer Listenpreis gültig ab", "Datum letzter Abgang", "Datum letzter Zugang", "Datum letzte Bewegung", 
				"Inventurzählmenge", "Umsatz ME Vorjahr", "Umsatz ME lfd. Jahr", "Preisupdate gesperrt", "Teilenummer Vorgänger", "Teilenummer Nachfolger", 
				"diebstahlrelevantes Kennzeichen", "REACH-Kennzeichen", "Zählgruppe"};

		final CSVFormat format = CSVFormat.RFC4180.withIgnoreSurroundingSpaces().
				withIgnoreEmptyLines().
				withTrim().
				withDelimiter(';').
				withQuote('"').
				withQuoteMode(QuoteMode.ALL).
				withHeader(headers);

		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
				CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(out, "ISO-8859-1"), format);) {

			//validate the Company Id
			//validateCompany(companyId);

			//validate the Warehouse Ids
			validateWarehouses(allowedWarehouses);

			manufacturer = (manufacturer != null && manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING))? RestInputConstants.DCAG_STRING : manufacturer;

			switch(selectedType) {

			case "1":    //Part Price range  ( Preisstruktur )
				reportSelectionList = createWhereClause_PriceRange(selectedInventoryList, schema, dataLibrary, allowedWarehouses, alphaXcompanyId, warehouseNos);
				break;
			case "2":    //Record Type ( Satzarten )
				reportSelectionList = createWhereClause_RcrdType(selectedInventoryList, schema, dataLibrary, allowedWarehouses, alphaXcompanyId, warehouseNos);
				break;
			case "3":    //Part Type   ( Teilearten )
				reportSelectionList = createWhereClause_PartType(selectedInventoryList, schema, dataLibrary, allowedWarehouses, alphaXcompanyId, warehouseNos);
				break;
			case "4":    //Part Brand   ( Marken )
				reportSelectionList = createWhereClause_Brand(selectedInventoryList, schema, dataLibrary, allowedWarehouses, alphaXcompanyId, warehouseNos, manufacturer);
				break;
			case "5":    //Discount Group  ( Rabattgruppen )
				reportSelectionList = createWhereClause_DiscountGr(selectedInventoryList, schema, dataLibrary, allowedWarehouses, alphaXcompanyId, warehouseNos, manufacturer, partBrand);
				break;
			case "6":    //Category  ( Inventurkennzeichen )
				reportSelectionList = createWhereClause_Category(selectedInventoryList, schema, dataLibrary, allowedWarehouses, alphaXcompanyId, warehouseNos);
				break;
			default:
				log.info("Input type is not valid");
				break;			
			}

			if (reportSelectionList != null && !reportSelectionList.isEmpty()) {

				for(ReportSelections reportSelection : reportSelectionList){

					String partNumber = "";
					String es1 = "";
					String es2 = "";

					if(reportSelection.getManufacturer().equalsIgnoreCase("DCAG") && reportSelection.getPartNumber() != null){
						String fullPartNumber  = StringUtils.rightPad(reportSelection.getPartNumber(), 20, " ");
						partNumber = StringUtils.substring(fullPartNumber,0, 11);
						es1 = StringUtils.substring(fullPartNumber,13, 15);
						es2 = StringUtils.substring(fullPartNumber,15, 19);
					}else{

						partNumber = reportSelection.getPartNumber();
					}

					List<String> data = Arrays.asList(
							StringUtils.leftPad(String.valueOf(reportSelection.getAlphaPlusCompanyId()),2,"0"),
							reportSelection.getAlphaxCompanyName(),
							StringUtils.leftPad(String.valueOf(reportSelection.getAlphaPlusAgencyId()),2,"0"),
							reportSelection.getAlphaxAgencyName(),
							StringUtils.leftPad(String.valueOf(reportSelection.getAlphaPlusWarehouseId()),2,"0"),
							reportSelection.getAlphaxWarehouseName(),
							reportSelection.getManufacturer()!=null && reportSelection.getManufacturer().equalsIgnoreCase(RestInputConstants.DCAG_STRING)? RestInputConstants.DAG_STRING :reportSelection.getManufacturer(),
									reportSelection.getPartBrand(),
									partNumber,
									es1,
									es2,
									reportSelection.getName(),
									replaceDotToComma(String.valueOf(reportSelection.getStorageIndikator())),
									replaceDotToComma(String.valueOf(reportSelection.getCurrentStock())),
									replaceDotToComma(String.valueOf(reportSelection.getPendingOrders())),
									reportSelection.getStorageLocation(),
									reportSelection.getMarketingCode(),
									replaceDotToComma(String.valueOf(reportSelection.getPartsIndikator())),
									replaceDotToComma(String.valueOf(reportSelection.getActivityType())),
									reportSelection.getDiscountGroup(),
									replaceDotToComma(String.valueOf(reportSelection.getPurchasePrice())),
									String.valueOf(reportSelection.getValueAddedTax()),
									replaceDotToComma(String.valueOf(reportSelection.getAverageNetPrice())),
									replaceDotToComma(String.valueOf(reportSelection.getLastPurchasePrice())),
									replaceDotToComma(String.valueOf(reportSelection.getPreviousPurchasePrice())),
									replaceDotToComma(String.valueOf(reportSelection.getReturnValue())),
									replaceDotToComma(String.valueOf(reportSelection.getFuturePurchasePrice())),
									convertDateToString(String.valueOf(reportSelection.getFuturePriceFromDate())),
									convertDateToString(String.valueOf(reportSelection.getLastDisposalDate())),
									convertDateToString(String.valueOf(reportSelection.getLastReceiptDate())),
									convertDateToString(String.valueOf(reportSelection.getLastMovementDate())),
									replaceDotToComma(String.valueOf(reportSelection.getCurrentInventoryCount())),
									replaceDotToComma(String.valueOf(reportSelection.getSalesStockLastYear())),
									replaceDotToComma(String.valueOf(reportSelection.getCurrentYearSalesStock())),
									reportSelection.getPriceMark(),
									reportSelection.getPredecessorPartNumber(),
									reportSelection.getSuccessorPartNumber(),
									reportSelection.getTheftRelevantLicensePlate(),
									reportSelection.getReachLabel(),
									reportSelection.getInventoryGroup()
							);

					csvPrinter.printRecord(data);
				}

				// writing the underlying stream
				csvPrinter.flush();

				byteArrayOutputStream = new ByteArrayInputStream(out.toByteArray());
			}			

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Selections report based on Inventory Structure (Bestandstruktur)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Selections report based on Inventory Structure (Bestandstruktur)"), exception);
			throw exception;
		}

		return byteArrayOutputStream;
	}



	private List<ReportSelections> generateQueryforReport(String schema, String dataLibrary, String allowedWarehouses, String alphaXCompanyId, 
			List<String> warehouseNos, String whereClause){

		StringBuilder whereQuery = new StringBuilder("");
		
		String warehouseNo = StringUtils.join(warehouseNos, ",");

		whereQuery.append(" LNR IN( ").append(warehouseNo).append(" )");
		

		StringBuilder query = new StringBuilder("SELECT FIRMA as AP_Firma,").append("( SELECT NAME FROM ").append(schema).append(".o_company "); 
		query.append("WHERE CAST(AP_COMPANY_ID AS INT) = FIRMA ) as AX_Firma, FILIAL as AP_Filiale,").append("( SELECT NAME FROM ").append(schema).append(".o_agency "); 
		query.append("where CAST(AP_AGENCY_ID AS INT) =  FILIAL AND COMPANY_ID = ").append(alphaXCompanyId).append(" FETCH NEXT 1 ROWS ONLY ) as AX_Filiale, LNR as AP_Lager, (select NAME FROM ").append(schema).append(".O_WRH ");
		query.append("WHERE AP_Warehous_ID = LNR) as AX_Lager, HERST, TMARKE, TNR, BENEN, SA, AKTBES, BESAUS, LOPA,MC, TA , LEIART, RG, EPR , ");
		query.append(" (select substr(DATAFLD, 1, 2) from ").append(dataLibrary).append(".REFERENZ Where substr(KEYFLD, 7, 4) = '9907' and substr(KEYFLD, 11, 2) = MWST) as Mehrwertsteuer, ");
		query.append("DAK, LEKPR, NPREIS, TRUWER, ZPREIS, GPRDAT, DTLABG, DTLZUG, DTLBEW, IVZME, VKSVJS, VKLFJS,  PRKZ, TNRV, ");
		query.append(" TNRN , DRTKZ , GFKZ , ZAEGRU FROM ");
		query.append(dataLibrary).append(".e_etstamm WHERE ").append(whereQuery).append(" and ");
		query.append(whereClause).append(" order by TNR  ");

		List<ReportSelections> reportSelectionList = dbServiceRepository.getResultUsingQuery(ReportSelections.class, query.toString(), true);

		return reportSelectionList;

	}


	/**
	 * This method is used for get Selections based Price Range (Preisstruktur) List
	 */
	private List<ReportSelections> createWhereClause_PriceRange(List<ReportSelectionsForInventoryDTO> selectedInventoryList, String schema, String dataLibrary, 
			String allowedWarehouses, String companyId, List<String> warehouseNos) {

		List<ReportSelections> reportList = new ArrayList<>();

		for(ReportSelectionsForInventoryDTO inventoryDTO : selectedInventoryList) {

			StringBuilder whereQuery = new StringBuilder("EPR >= ").append(inventoryDTO.getFromCurValue()).append(" and EPR < ").append(inventoryDTO.getLeadingColumnValue());

			reportList = generateQueryforReport(schema, dataLibrary, allowedWarehouses, companyId, warehouseNos, whereQuery.toString());
		}

		return reportList;		
	}


	/**
	 * This method is used for get Selections based Record Type (Satzart) List
	 */
	private List<ReportSelections>  createWhereClause_RcrdType(List<ReportSelectionsForInventoryDTO> selectedInventoryList, String schema, String dataLibrary, 
			String allowedWarehouses, String companyId, List<String> warehouseNos) {

		StringJoiner joiner = new StringJoiner(",");
		List<String> recordTypeList = new ArrayList<>();
		List<ReportSelections> reportList = new ArrayList<>();

		for(ReportSelectionsForInventoryDTO inventoryDTO : selectedInventoryList) {

			recordTypeList.add(inventoryDTO.getLeadingColumnValue().substring(0, inventoryDTO.getLeadingColumnValue().indexOf("-")).trim());
		}
		recordTypeList.forEach(item -> joiner.add(item));

		StringBuilder whereQuery = new StringBuilder(" SA in (").append(joiner).append(") ");

		reportList = generateQueryforReport(schema, dataLibrary, allowedWarehouses, companyId, warehouseNos, whereQuery.toString());

		return reportList;
	}


	/**
	 * This method is used for get Selections based Part Type (Teileart) List
	 */
	private List<ReportSelections>  createWhereClause_PartType(List<ReportSelectionsForInventoryDTO> selectedInventoryList, String schema, String dataLibrary, 
			String allowedWarehouses, String companyId, List<String> warehouseNos) {

		StringJoiner joiner = new StringJoiner(",");
		List<String> recordTypeList = new ArrayList<>();
		List<ReportSelections> reportList = new ArrayList<>();

		for(ReportSelectionsForInventoryDTO inventoryDTO : selectedInventoryList) {

			recordTypeList.add(inventoryDTO.getLeadingColumnValue().substring(0, inventoryDTO.getLeadingColumnValue().indexOf("-")).trim());
		}
		recordTypeList.forEach(item -> joiner.add(item));

		StringBuilder whereQuery = new StringBuilder(" TA in (").append(joiner).append(") ");

		reportList = generateQueryforReport(schema, dataLibrary, allowedWarehouses, companyId, warehouseNos, whereQuery.toString());

		return reportList;
	}


	/**
	 * This method is used for get Selections based Part Brand (Marken) List
	 */
	private List<ReportSelections>  createWhereClause_Brand(List<ReportSelectionsForInventoryDTO> selectedInventoryList, String schema, String dataLibrary, 
			String allowedWarehouses, String companyId, List<String> warehouseNos, String manufacturer) {

		StringJoiner joiner = new StringJoiner(",");
		List<ReportSelections> reportList = new ArrayList<>();

		if(manufacturer!=null && !manufacturer.trim().isEmpty()) {

			selectedInventoryList.forEach(item -> joiner.add("'"+item.getLeadingColumnValue()+"'"));

			StringBuilder whereQuery = new StringBuilder(" HERST='").append(manufacturer).append("'");
			whereQuery.append(" and TMARKE in (").append(joiner).append(") ");	

			reportList = generateQueryforReport(schema, dataLibrary, allowedWarehouses, companyId, warehouseNos, whereQuery.toString());
		}
		else {
			log.info(" manufacturer is required.");
			AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.PARAMETERS_INVALID_MSG_KEY, "", "manufacturer"));
			log.error(messageService.getReadableMessage(ExceptionMessages.PARAMETERS_INVALID_MSG_KEY, "", "manufacturer"));
			throw exception;
		}

		return reportList;
	}


	/**
	 * This method is used for get Selections based Discount Group (Rabattgruppen) List
	 */
	private List<ReportSelections>  createWhereClause_DiscountGr(List<ReportSelectionsForInventoryDTO> selectedInventoryList, String schema, String dataLibrary, 
			String allowedWarehouses, String companyId, List<String> warehouseNos, String manufacturer, String partBrand) {

		StringJoiner joiner = new StringJoiner(",");
		List<ReportSelections> reportList = new ArrayList<>();

		if(manufacturer!=null && !manufacturer.trim().isEmpty() && partBrand!=null && !partBrand.trim().isEmpty()) {

			selectedInventoryList.forEach(item -> joiner.add("'"+item.getLeadingColumnValue()+"'"));

			StringBuilder whereQuery = new StringBuilder(" HERST='").append(manufacturer).append("'");
			whereQuery.append(" and TMARKE='").append(partBrand).append("'");
			whereQuery.append(" and RG in (").append(joiner).append(") ");

			reportList = generateQueryforReport(schema, dataLibrary, allowedWarehouses, companyId, warehouseNos, whereQuery.toString());
		}
		else {
			log.info(" manufacturer and partBrand is required.");
			AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.PARAMETERS_INVALID_MSG_KEY, "", "manufacturer, partBrand"));
			log.error(messageService.getReadableMessage(ExceptionMessages.PARAMETERS_INVALID_MSG_KEY, "", "manufacturer, partBrand"));
			throw exception;
		}

		return reportList;
	}


	/**
	 * This method is used for get Selections based Category (Inventurkennzeichen) List
	 */
	private List<ReportSelections> createWhereClause_Category(List<ReportSelectionsForInventoryDTO> selectedInventoryList, String schema, String dataLibrary, 
			String allowedWarehouses, String companyId, List<String> warehouseNos) {

		List<ReportSelections> reportList = new ArrayList<>();

		for(ReportSelectionsForInventoryDTO inventoryDTO : selectedInventoryList) {

			if(inventoryDTO.getLeadingColumnValue().equals(RestInputConstants.CATEGORY_INVENTORY_POS1)) {
				StringBuilder whereQuery = new StringBuilder(" SA = 1 and IVKZ <> '1' ");
				reportList.addAll(generateQueryforReport(schema, dataLibrary, allowedWarehouses, companyId, warehouseNos, whereQuery.toString()));				
			}
			else if(inventoryDTO.getLeadingColumnValue().equals(RestInputConstants.CATEGORY_INVENTORY_POS2)) {
				StringBuilder whereQuery = new StringBuilder(" SA = 1 and IVKZ = '1' ");
				reportList.addAll(generateQueryforReport(schema, dataLibrary, allowedWarehouses, companyId, warehouseNos, whereQuery.toString()));				
			}			
			else if(inventoryDTO.getLeadingColumnValue().equals(RestInputConstants.CATEGORY_CUSTOMER_POS1)) {
				StringBuilder whereQuery = new StringBuilder(" SA = 2 and IVKZ <> '1' ");
				reportList.addAll(generateQueryforReport(schema, dataLibrary, allowedWarehouses, companyId, warehouseNos, whereQuery.toString()));				
			}
			else if(inventoryDTO.getLeadingColumnValue().equals(RestInputConstants.CATEGORY_CUSTOMER_POS2)) {
				StringBuilder whereQuery = new StringBuilder(" SA = 2 and IVKZ = '1' ");
				reportList.addAll( generateQueryforReport(schema, dataLibrary, allowedWarehouses, companyId, warehouseNos, whereQuery.toString()));				
			}
		}

		return reportList;	
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
	 * This method is used to get Selections based on Business Case Statistics DAK (Geschäftsfallstatistiken) List
	 */

	@Override
	public List<ReportSelectionsForBusinessCaseDTO> getBusinessCaseStatistics_DAK(String schema, String dataLibrary, String companyId, String allowedWarehouses,
			String fromDate, String toDate) {

		log.info("Inside getBusinessCaseStatistics_DAK method of ReportSelectionsImpl");
		List<ReportSelectionsForBusinessCaseDTO> reportSelectionsList = null;
		try {

			//validate the Warehouse Ids
			validateWarehouses(allowedWarehouses);

			//create where clause based on input value
			String whereClause = createWhereClause(allowedWarehouses, fromDate, toDate);

			reportSelectionsList = getChangeDAK(schema,dataLibrary, whereClause);

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Business Case Statistics DAK list"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Selections based on Business Case Statistics DAK list"), exception);
			throw exception;
		}

		return reportSelectionsList;
	}

	/**
	 * This method is used to get Selections based on Business Case Statistics ManualCount (Geschäftsfallstatistiken) List
	 */

	@Override
	public List<ReportSelectionsForBusinessCaseDTO> getBusinessCaseStatistics_ManualCount(String schema, String dataLibrary, String companyId, String allowedWarehouses,
			String fromDate, String toDate) {

		log.info("Inside getBusinessCaseStatistics_ManualCount method of ReportSelectionsImpl");

		List<ReportSelectionsForBusinessCaseDTO> reportSelectionsList = null;

		try {

			//validate the Warehouse Ids
			validateWarehouses(allowedWarehouses);

			//create where clause based on input value
			String whereClause = createWhereClause(allowedWarehouses, fromDate, toDate);

			reportSelectionsList = getManualCount(schema,dataLibrary,whereClause);

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Business Case Statistics ManualCount list"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Business Case Statistics ManualCount list"), exception);
			throw exception;
		}

		return reportSelectionsList;
	}

	/**
	 * This method is used to get Selections based on Business Case Statistics ManualCorrection (Geschäftsfallstatistiken) List
	 */

	@Override
	public List<ReportSelectionsForBusinessCaseDTO> getBusinessCaseStatistics_ManualCorrection(String schema, String dataLibrary, String companyId, String allowedWarehouses,
			String fromDate, String toDate) {

		log.info("Inside getBusinessCaseStatistics_ManualCorrection method of ReportSelectionsImpl");

		List<ReportSelectionsForBusinessCaseDTO> reportSelectionsList = null;

		try {

			//validate the Warehouse Ids
			validateWarehouses(allowedWarehouses);

			//create where clause based on input value
			String whereClause = createWhereClause(allowedWarehouses, fromDate, toDate);

			reportSelectionsList = getManualCorrection(schema,dataLibrary, whereClause);

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Business Case Statistics ManualCorrection list"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Business Case Statistics ManualCorrection list"), exception);
			throw exception;
		}

		return reportSelectionsList;
	}

	/**
	 * This method is used to get Selections based on Business Case Statistics Redemptions (Geschäftsfallstatistiken) List
	 */

	@Override
	public List<ReportSelectionsForBusinessCaseDTO> getBusinessCaseStatistics_Redemptions(String schema, String dataLibrary, String companyId, String allowedWarehouses,
			String fromDate, String toDate) {

		log.info("Inside getBusinessCaseStatistics_Redemptions method of ReportSelectionsImpl");

		List<ReportSelectionsForBusinessCaseDTO> reportSelectionsList = null;

		try {

			//validate the Warehouse Ids
			validateWarehouses(allowedWarehouses);

			//create where clause based on input value
			String whereClause = createWhereClause(allowedWarehouses, fromDate, toDate);

			reportSelectionsList = getRedemptions(schema,dataLibrary, whereClause);

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Business Case Statistics Redemptions list"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Business Case Statistics Redemptions list"), exception);
			throw exception;
		}

		return reportSelectionsList;
	}

	/**
	 * This method is used to get Selections based on Business Case Statistics Redemptions WithoutInventoryMovement (Geschäftsfallstatistiken) List
	 */

	@Override
	public List<ReportSelectionsForBusinessCaseDTO> getBusinessCaseStatistics_WithoutInventoryMovement(String schema, String dataLibrary, String companyId, String allowedWarehouses,
			String fromDate, String toDate) {

		log.info("Inside getBusinessCaseStatistics_WithoutInventoryMovement method of ReportSelectionsImpl");

		List<ReportSelectionsForBusinessCaseDTO> reportSelectionsList = null;

		try {

			//validate the Warehouse Ids
			validateWarehouses(allowedWarehouses);

			//create where clause based on input value
			String whereClause = createWhereClause(allowedWarehouses, fromDate, toDate);

			reportSelectionsList = getCreditWithoutInventoryMovement(schema,dataLibrary, whereClause);


		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Business Case Statistics WithoutInventoryMovement list"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Business Case Statistics WithoutInventoryMovement list"), exception);
			throw exception;
		}

		return reportSelectionsList;
	}


	private List<ReportSelectionsForBusinessCaseDTO> getCreditWithoutInventoryMovement(String schema,
			String dataLibrary, String whereClause) {

		StringBuilder query = new StringBuilder("SELECT VFNR  ||' / '|| (SELECT NAME FROM ").append(schema);
		query.append(".O_WRH WHERE AP_WAREHOUS_ID=VFNR) AS LAGER,");
		query.append("SUBSTR(JJMMTT, 5 , 2)||'.'||SUBSTR(JJMMTT, 3 , 2)||'.20'||SUBSTR(JJMMTT, 1 , 2) AS DATE, ");
		query.append("KB||ETNR||ES1||ES2 AS TEILENUMMER, BELNR AS BELEGNUMMER, HERK AS HERKUNFT, ");
		query.append("CAST(SUBSTR(T1, 3 , 6) AS REAL)/10 AS BUCHUNGSMENGE, HERST  ");
		query.append("FROM ").append(dataLibrary).append(".E_CPSDAT WHERE (BART=98 OR BART=99)   AND ").append(whereClause);

		List<ReportSelections> reportSelectionList = dbServiceRepository.getResultUsingQuery(ReportSelections.class, query.toString(), true);

		List<ReportSelectionsForBusinessCaseDTO> reportSelectionsList = setValueForBusinessCaseStatistics(reportSelectionList);

		return reportSelectionsList;
	}


	private List<ReportSelectionsForBusinessCaseDTO> getRedemptions(String schema, String dataLibrary,
			String whereClause) {

		StringBuilder query = new StringBuilder("SELECT VFNR  ||' / '|| (SELECT NAME FROM ").append(schema);
		query.append(".O_WRH WHERE AP_WAREHOUS_ID=VFNR) AS LAGER,");
		query.append("SUBSTR(JJMMTT, 5 , 2)||'.'||SUBSTR(JJMMTT, 3 , 2)||'.20'||SUBSTR(JJMMTT, 1 , 2) AS DATE, ");
		query.append("KB||ETNR||ES1||ES2 AS TEILENUMMER, BELNR AS BELEGNUMMER, ");
		query.append("(CAST(EB_VZ|| EBEST AS REAL) / 10) - (CAST(SUBSTR(T1, 3 , 6) AS REAL)/10) AS BESTAND_ALT, CAST(EB_VZ|| EBEST AS REAL) / 10 AS BESTAND_NEU,");
		query.append("CAST(SUBSTR(T1, 3 , 6) AS REAL)/10 AS BUCHUNGSMENGE, HERST   ");
		query.append("FROM ").append(dataLibrary).append(".E_CPSDAT WHERE BART=5  AND ").append(whereClause);

		List<ReportSelections> reportSelectionList = dbServiceRepository.getResultUsingQuery(ReportSelections.class, query.toString(), true);

		List<ReportSelectionsForBusinessCaseDTO> reportSelectionsList = setValueForBusinessCaseStatistics(reportSelectionList);

		return reportSelectionsList;
	}


	private List<ReportSelectionsForBusinessCaseDTO> getManualCorrection(String schema, String dataLibrary,
			String whereClause) {

		StringBuilder query = new StringBuilder("SELECT VFNR  ||' / '|| (SELECT NAME FROM ").append(schema);
		query.append(".O_WRH WHERE AP_WAREHOUS_ID=VFNR) AS LAGER,");
		query.append("SUBSTR(JJMMTT, 5 , 2)||'.'||SUBSTR(JJMMTT, 3 , 2)||'.20'||SUBSTR(JJMMTT, 1 , 2) AS DATE, ");
		query.append("KB||ETNR||ES1||ES2 AS TEILENUMMER, BELNR AS BELEGNUMMER, ");
		query.append("(CAST(EB_VZ|| EBEST AS REAL) / 10) - (CAST(SUBSTR(T1, 3 , 6) AS REAL)/10) AS BESTAND_ALT, CAST(EB_VZ|| EBEST AS REAL) / 10 AS BESTAND_NEU,");
		query.append("CAST(SUBSTR(T1, 3 , 6) AS REAL)/10 AS BUCHUNGSMENGE, HERST  ");
		query.append("FROM ").append(dataLibrary).append(".E_CPSDAT WHERE (BART=6 OR BART=25)  AND ").append(whereClause);

		List<ReportSelections> reportSelectionList = dbServiceRepository.getResultUsingQuery(ReportSelections.class, query.toString(), true);

		List<ReportSelectionsForBusinessCaseDTO> reportSelectionsList = setValueForBusinessCaseStatistics(reportSelectionList);

		return reportSelectionsList;
	}


	private List<ReportSelectionsForBusinessCaseDTO> getManualCount(String schema, String dataLibrary,
			String whereClause) {

		StringBuilder query = new StringBuilder("SELECT VFNR  ||' / '|| (SELECT NAME FROM ").append(schema);
		query.append(".O_WRH WHERE AP_WAREHOUS_ID=VFNR) AS LAGER,");
		query.append("SUBSTR(JJMMTT, 5 , 2)||'.'||SUBSTR(JJMMTT, 3 , 2)||'.20'||SUBSTR(JJMMTT, 1 , 2) AS DATE, ");
		query.append("KB||ETNR||ES1||ES2 AS TEILENUMMER, BELNR AS BELEGNUMMER, ");
		query.append("CAST(EB_VZ|| EBEST AS REAL) / 10 AS BESTAND_ALT, CAST(SUBSTR(T1, 3 , 6) AS REAL)/10 AS BESTAND_NEU,");
		query.append("(CAST(SUBSTR(T1, 3 , 6) AS REAL)/10) - (CAST(EB_VZ|| EBEST AS REAL) / 10)  AS Zahldifferenz, HERST ");
		query.append("FROM ").append(dataLibrary).append(".E_CPSDAT WHERE BART=67 AND ").append(whereClause);

		List<ReportSelections> reportSelectionList = dbServiceRepository.getResultUsingQuery(ReportSelections.class, query.toString(), true);

		List<ReportSelectionsForBusinessCaseDTO> reportSelectionsList = setValueForBusinessCaseStatistics(reportSelectionList);

		return reportSelectionsList;
	}


	private List<ReportSelectionsForBusinessCaseDTO> getChangeDAK(String schema, String dataLibrary, String whereClause) {

		StringBuilder query = new StringBuilder("SELECT VFNR  ||' / '|| (SELECT NAME FROM ").append(schema);
		query.append(".O_WRH WHERE AP_WAREHOUS_ID=VFNR) AS LAGER,");
		query.append("SUBSTR(JJMMTT, 5 , 2)||'.'||SUBSTR(JJMMTT, 3 , 2)||'.20'||SUBSTR(JJMMTT, 1 , 2) AS DATE, ");
		query.append("KB||ETNR||ES1||ES2 AS TEILENUMMER, BELNR AS BELEGNUMMER, ");
		query.append("CAST(DAKEWV AS REAL)/10000  AS DAK_ALT, CAST(DAKEWN AS REAL)/10000 AS DAK_NEU, HERK AS HERKUNFT, HERST ");
		query.append(" FROM ").append(dataLibrary).append(".E_CPSDAT WHERE DAKEWV <> DAKEWN AND ").append(whereClause);

		List<ReportSelections> reportSelectionList = dbServiceRepository.getResultUsingQuery(ReportSelections.class, query.toString(), true);

		List<ReportSelectionsForBusinessCaseDTO> reportSelectionsList = setValueForBusinessCaseStatistics(reportSelectionList);

		return reportSelectionsList;

	}


	private List<ReportSelectionsForBusinessCaseDTO> setValueForBusinessCaseStatistics(
			List<ReportSelections> reportSelectionList) {

		List<ReportSelectionsForBusinessCaseDTO> reportSelectionsList = new ArrayList<>();

		if (reportSelectionList != null && !reportSelectionList.isEmpty()) {

			for(ReportSelections reportSelection : reportSelectionList){
				ReportSelectionsForBusinessCaseDTO reportSelectionsDto = new ReportSelectionsForBusinessCaseDTO();

				reportSelectionsDto.setWarehouse(reportSelection.getBusinessCaseWarehouse());
				reportSelectionsDto.setDate(reportSelection.getBusinessCaseDate());
				reportSelectionsDto.setPartNumber(reportSelection.getBusinessCasePartNo());
				reportSelectionsDto.setDocumentNumber(reportSelection.getBusinessCaseDocumentNo());
				reportSelectionsDto.setDakAlt(reportSelection.getDakAlt());
				reportSelectionsDto.setDakNeu(reportSelection.getDakNeu());
				reportSelectionsDto.setBestandAlt(reportSelection.getBestandAlt());
				reportSelectionsDto.setBestandNeu(reportSelection.getBestandNeu());
				reportSelectionsDto.setOrigin(reportSelection.getBusinessCaseOrigin());
				reportSelectionsDto.setCountingDifference(reportSelection.getCountingDifference());
				reportSelectionsDto.setBookingAmount(reportSelection.getBookingAmount());
				reportSelectionsDto.setManufacturer(reportSelection.getManufacturer());

				reportSelectionsList.add(reportSelectionsDto);
			}
		}
		return reportSelectionsList;
	}


	private String createWhereClause(String allowedWarehouses, String fromDate, String toDate) {

		StringBuilder whereQuery = new StringBuilder("");

		whereQuery.append(" VFNR IN( ").append(allowedWarehouses+") ");

		if(fromDate!= null && !fromDate.isEmpty() && toDate!= null && !toDate.isEmpty()
				&& fromDate.length() == 10 && toDate.length() == 10 ){

			String startDate = fromDate.substring(8, 10)+fromDate.substring(3, 5)+fromDate.substring(0, 2);
			String tillDate =  toDate.substring(8, 10)+toDate.substring(3, 5)+toDate.substring(0, 2);

			whereQuery.append("AND JJMMTT BETWEEN  ").append(startDate).append(" AND ").append(tillDate);

		}else {
			log.info(" fromDate and toDate is required.");
			AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.PARAMETERS_INVALID_MSG_KEY, "", "fromDate, toDate"));
			log.error(messageService.getReadableMessage(ExceptionMessages.PARAMETERS_INVALID_MSG_KEY, "", "fromDate, toDate"));
			throw exception;
		}

		return whereQuery.toString();
	}


	/**
	 * This method is used to create report Based on BA Statistics (Geschäftsfallstatistik - CSV)
	 */	
	@Override
	public InputStream generateReportBasedOnBAStatistics(String schema, String dataLibrary, String companyId, String allowedWarehouses, 
			String fromDate, String toDate) {

		log.info("Inside generateReportBasedOnBAStatistics method of ReportSelectionsImpl");

		InputStream inputStream = null;

		final CSVFormat format = CSVFormat.RFC4180.withIgnoreSurroundingSpaces().
				withIgnoreEmptyLines().
				withTrim().
				withDelimiter(';').
				withQuote('"').
				withQuoteMode(QuoteMode.ALL);				

		try  {	

			List<ByteArrayInputStream> streams = Arrays.asList(
					generateData_DAK(getBusinessCaseStatistics_DAK(schema, dataLibrary, companyId, allowedWarehouses, fromDate, toDate), format, fromDate, toDate),
					generateData_ManualCount(getBusinessCaseStatistics_ManualCount(schema, dataLibrary, companyId, allowedWarehouses, fromDate, toDate), format),
					generateData_Correction_Redemptions(getBusinessCaseStatistics_ManualCorrection(schema, dataLibrary, companyId, allowedWarehouses, fromDate, toDate), format, "ManualCorrection"),
					generateData_Correction_Redemptions(getBusinessCaseStatistics_Redemptions(schema, dataLibrary, companyId, allowedWarehouses, fromDate, toDate), format, "Redemptions"),
					generateData_InventoryMovement(getBusinessCaseStatistics_WithoutInventoryMovement(schema, dataLibrary, companyId, allowedWarehouses, fromDate, toDate), format)
					);

			inputStream = new SequenceInputStream(Collections.enumeration(streams));

		}
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Selections report based on BA Statistics (Geschäftsfallstatistik)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Selections report based on BA Statistics (Geschäftsfallstatistik)"), exception);
			throw exception;
		}

		return inputStream;
	}


	private ByteArrayInputStream generateData_DAK(List<ReportSelectionsForBusinessCaseDTO> reportSelectionList, CSVFormat format, String fromDate, String toDate){

		ByteArrayInputStream  byteArrayInputStream = null;
		final String[] headers_table1  = {"Lager",	"Datum",  "Teilenummer", "Belegnummer",  "DAK alt",	"DAK neu",	"Herkunft"};

		format = format.withCommentMarker('#').withHeaderComments("Geschäftsfallstatistiken;"+fromDate+";"+toDate+"\n\n Änderung DAK");
		format = format.withHeader(headers_table1);

		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
				CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(out, "ISO-8859-1"), format);) {			

			if (reportSelectionList != null && !reportSelectionList.isEmpty()) {

				for(ReportSelectionsForBusinessCaseDTO reportSelection : reportSelectionList){

					List<String> data = Arrays.asList(
							reportSelection.getWarehouse(),
							reportSelection.getDate(),
							reportSelection.getPartNumber(),
							reportSelection.getDocumentNumber(),
							replaceDotToComma(reportSelection.getDakAlt()),
							replaceDotToComma(reportSelection.getDakNeu()),
							reportSelection.getOrigin()							
							);

					csvPrinter.printRecord(data);					
				}
				csvPrinter.println();
				csvPrinter.println();

				// writing the underlying stream
				csvPrinter.flush();

				byteArrayInputStream = new ByteArrayInputStream(out.toByteArray());
			}else{
				csvPrinter.println();
				csvPrinter.println();
				// writing the underlying stream
				csvPrinter.flush();
				byteArrayInputStream = new ByteArrayInputStream(out.toByteArray());
			}			

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "report Geschäftsfallstatistik - Änderung DAK"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"report Geschäftsfallstatistik - Änderung DAK"), exception);
			throw exception;
		}

		return byteArrayInputStream;
	}


	private ByteArrayInputStream generateData_ManualCount(List<ReportSelectionsForBusinessCaseDTO> reportSelectionList, CSVFormat format){

		ByteArrayInputStream  byteArrayInputStream = null;
		final String[] headers_table2  = {"Lager",	"Datum",  "Teilenummer", "Belegnummer",  "Bestand alt",	"Bestand neu",	"Zähldifferenz"};

		format = format.withCommentMarker('#').withHeaderComments("Manuelle Zählung");
		format = format.withHeader(headers_table2);

		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
				CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(out, "ISO-8859-1"), format);) {	

			if (reportSelectionList != null && !reportSelectionList.isEmpty()) {

				for(ReportSelectionsForBusinessCaseDTO reportSelection : reportSelectionList){

					List<String> data = Arrays.asList(
							reportSelection.getWarehouse(),
							reportSelection.getDate(),
							reportSelection.getPartNumber(),
							reportSelection.getDocumentNumber(),
							replaceDotToComma(reportSelection.getBestandAlt()),
							replaceDotToComma(reportSelection.getBestandNeu()),
							replaceDotToComma(reportSelection.getCountingDifference())							
							);

					csvPrinter.printRecord(data);					
				}
				csvPrinter.println();
				csvPrinter.println();

				// writing the underlying stream
				csvPrinter.flush();

				byteArrayInputStream = new ByteArrayInputStream(out.toByteArray());
			}else{
				csvPrinter.println();
				csvPrinter.println();
				// writing the underlying stream
				csvPrinter.flush();
				byteArrayInputStream = new ByteArrayInputStream(out.toByteArray());
			}			

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "report Geschäftsfallstatistik - Manuelle Zählung"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"report Geschäftsfallstatistik - Manuelle Zählung"), exception);
			throw exception;
		}

		return byteArrayInputStream;
	}


	private ByteArrayInputStream generateData_Correction_Redemptions(List<ReportSelectionsForBusinessCaseDTO> reportSelectionList, CSVFormat format, String reportName){

		ByteArrayInputStream  byteArrayInputStream = null;
		final String[] headers_table34 = {"Lager",	"Datum",  "Teilenummer", "Belegnummer",  "Bestand alt",	"Bestand neu",	"Buchungsmenge"};

		if(reportName.equals("ManualCorrection")) {
			format = format.withCommentMarker('#').withHeaderComments("Manuelle Korrektur");
		}
		else {
			format = format.withCommentMarker('#').withHeaderComments("Rücknahmen");
		}

		format = format.withHeader(headers_table34);

		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
				CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(out, "ISO-8859-1"), format);) {	

			if (reportSelectionList != null && !reportSelectionList.isEmpty()) {

				for(ReportSelectionsForBusinessCaseDTO reportSelection : reportSelectionList){

					List<String> data = Arrays.asList(
							reportSelection.getWarehouse(),
							reportSelection.getDate(),
							reportSelection.getPartNumber(),
							reportSelection.getDocumentNumber(),
							replaceDotToComma(reportSelection.getBestandAlt()),
							replaceDotToComma(reportSelection.getBestandNeu()),
							replaceDotToComma(reportSelection.getBookingAmount())							
							);

					csvPrinter.printRecord(data);					
				}
				csvPrinter.println();
				// writing the underlying stream
				csvPrinter.flush();

				byteArrayInputStream = new ByteArrayInputStream(out.toByteArray());
			}else{
				csvPrinter.println();
				csvPrinter.println();
				// writing the underlying stream
				csvPrinter.flush();
				byteArrayInputStream = new ByteArrayInputStream(out.toByteArray());
			}			

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "report Geschäftsfallstatistik - Manuelle Korrektur / Rücknahmen"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"report Geschäftsfallstatistik - Manuelle Korrektur / Rücknahmen"), exception);
			throw exception;
		}

		return byteArrayInputStream;
	}


	private ByteArrayInputStream generateData_InventoryMovement(List<ReportSelectionsForBusinessCaseDTO> reportSelectionList, CSVFormat format){

		ByteArrayInputStream  byteArrayInputStream = null;
		final String[] headers_table5  = {"Lager",	"Datum",  "Teilenummer", "Belegnummer",  "Herkunft",	"Buchungsmenge"};

		format = format.withCommentMarker('#').withHeaderComments("Verkauf / Gutschrift ohne Bestandsbewegung");
		format = format.withHeader(headers_table5);

		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
				CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(out, "ISO-8859-1"), format);) {	

			if (reportSelectionList != null && !reportSelectionList.isEmpty()) {

				for(ReportSelectionsForBusinessCaseDTO reportSelection : reportSelectionList){

					List<String> data = Arrays.asList(
							reportSelection.getWarehouse(),
							reportSelection.getDate(),
							reportSelection.getPartNumber(),
							reportSelection.getDocumentNumber(),
							reportSelection.getOrigin(),
							replaceDotToComma(reportSelection.getBookingAmount())							
							);

					csvPrinter.printRecord(data);					
				}				
				csvPrinter.println();
				csvPrinter.println();

				// writing the underlying stream
				csvPrinter.flush();

				byteArrayInputStream = new ByteArrayInputStream(out.toByteArray());
			}else{
				csvPrinter.println();
				csvPrinter.println();
				// writing the underlying stream
				csvPrinter.flush();
				byteArrayInputStream = new ByteArrayInputStream(out.toByteArray());
			}			

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "report Geschäftsfallstatistik - Verkauf ohne Bestandsbewegung"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"report Geschäftsfallstatistik - Verkauf ohne Bestandsbewegung"), exception);
			throw exception;
		}

		return byteArrayInputStream;
	}
	
	
	/**
	 * This method is used for get (Selektion aus Bestand) Selections From Stock List
	 */
	@Override
	public GlobalSearch getSelectionsFromMovement(String schema, String dataLibrary, String companyId, String allowedWarehouses, 
			String manufacturer, List<String> warehouseNos, String fromDate, String toDate, String businessCase, boolean invtCustPos, boolean custInvtPos,
			boolean dak, boolean chngOfmktgCode, boolean partTypeOrSupplier, String documentNo, String customerNo,
			String supplierNo, String partNo, String furMktgCode, String brand,
			String pageSize, String pageNumber) {

		log.info("Inside getSelectionsFromMovement method of ReportSelectionsImpl");

		List<ReportSelectionsForMovementDTO> reportSelectionsList = new ArrayList<>();
		GlobalSearch globalSearchList = new GlobalSearch();
		List<ReportSelections> reportSelectionList = null;
		String totalCount = "0";
		boolean isReport = false;

		try {
			validateCompany(companyId);

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

			//create where clause based on input value
			String whereClause = createWhereClause(schema, dataLibrary, allowedWarehouses, manufacturer, warehouseNos,
					businessCase, fromDate, toDate, invtCustPos, custInvtPos, dak, chngOfmktgCode, partTypeOrSupplier, 
					documentNo, customerNo,supplierNo, partNo, furMktgCode, brand, isReport, "");

			StringBuilder query = new StringBuilder("SELECT HERST, VFNR as AP_Lager,(SELECT NAME FROM ").append(schema).append(".O_WRH ");
			query.append("WHERE AP_WAREHOUS_ID=VFNR) as AX_Lager, bart, (SELECT DESCRIPTION FROM ").append(schema).append(".O_BA ");
			query.append("WHERE BA=BART) as VORGANGSART_TEXT ,");
			//query.append("SUBSTR(JJMMTT, 5 , 2)||'.'||SUBSTR(JJMMTT, 3 , 2)||'.20'||SUBSTR(JJMMTT, 1 , 2) AS DATE, ");
			query.append("'20'||SUBSTR(JJMMTT, 1 , 2)||'.'||SUBSTR(JJMMTT, 3 , 2)||'.'||SUBSTR(JJMMTT, 5 , 2) AS DATE, ");
			query.append("KB||ETNR||ES1||ES2 AS TEILENUMMER, BELNR AS BELEGNUMMER, ");
			query.append("MARKEN , POSNR, HHMMSS, ws as WS_ID, WSUSER, KDNR, MCODE , BSN_LC , T1, T2, T3, ");
			query.append("( CASE WHEN (bart=40 OR bart=43 OR bart=44 OR bart=45 OR bart=49 OR bart=50) THEN 0 ELSE  CAST(SUBSTR(T1, 3, 6) AS REAL)/10 END ) AS BUCHUNGSMENGE ");
			
			

/*			if(!archData){
				query.append(",(SELECT COUNT(*) AS ROWNUMER FROM ").append(dataLibrary).append(".E_CPSDAT WHERE ").append(whereClause);
				query.append(") FROM ").append(dataLibrary).append(".E_CPSDAT WHERE ").append(whereClause).append(" ORDER BY DATE DESC, HHMMSS DESC  OFFSET ");
				query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");
				reportSelectionList = dbServiceRepository.getResultUsingQuery(ReportSelections.class, query.toString(), true);
				
				if (reportSelectionList != null && !reportSelectionList.isEmpty()) {
					
					totalCount = Integer.toString(reportSelectionList.get(0).getTotalCount());
				}
*/				

//			}else{
				//get total count from both table 
				StringBuilder queryCount = new StringBuilder("SELECT COUNT(*) AS count FROM ( ").append(query).append(" FROM ");
				queryCount.append(dataLibrary).append(".E_CPSDAT WHERE ").append(whereClause).append("UNION ").append(query).append("  FROM ");
				queryCount.append(dataLibrary).append(".E_HISTO WHERE ").append(whereClause).append(")");
				totalCount = dbServiceRepository.getCountUsingQuery(queryCount.toString());
				
				//get actual results
				StringBuilder queryForHisto = new StringBuilder(query);
				queryForHisto.append(" FROM ").append(dataLibrary).append(".E_HISTO WHERE ").append(whereClause);
				queryForHisto.append(" UNION ");
				queryForHisto.append(query).append("FROM ").append(dataLibrary).append(".E_CPSDAT WHERE ").append(whereClause);
				queryForHisto.append(" ORDER BY DATE DESC, HHMMSS DESC OFFSET ");
				queryForHisto.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY");
				
				reportSelectionList = dbServiceRepository.getResultUsingQuery(ReportSelections.class, queryForHisto.toString(), true);

//			}

			 
			if (reportSelectionList != null && !reportSelectionList.isEmpty()) {

				for(ReportSelections reportSelection : reportSelectionList){
					ReportSelectionsForMovementDTO reportSelectionsDto = new ReportSelectionsForMovementDTO();

					reportSelectionsDto.setWarehouse(StringUtils.leftPad(String.valueOf(reportSelection.getAlphaPlusWarehouseId()),2,"0")+"-"+reportSelection.getAlphaxWarehouseName());
					manufacturer = reportSelection.getManufacturer();
					reportSelectionsDto.setManufacturer(manufacturer!=null && manufacturer.equalsIgnoreCase(RestInputConstants.DCAG_STRING)? RestInputConstants.DAG_STRING : manufacturer);
					reportSelectionsDto.setMovementType(reportSelection.getMovementTypeName());
					reportSelectionsDto.setDate(convertDateToDDMMYYYY(reportSelection.getBusinessCaseDate()));
					reportSelectionsDto.setPartNumber(reportSelection.getBusinessCasePartNo());
					if(reportSelection.getBusinessCaseDocumentNo()==null || reportSelection.getBusinessCaseDocumentNo().trim().equalsIgnoreCase("0") ||
							reportSelection.getBusinessCaseDocumentNo().trim().isEmpty() ){
						reportSelectionsDto.setDocumentNumber("--");
					}else{
						reportSelectionsDto.setDocumentNumber(reportSelection.getBusinessCaseDocumentNo());
					}
					
					reportSelectionsDto.setBrand(reportSelection.getBrand());
					reportSelectionsDto.setPositionNo(reportSelection.getPositionNo());
					reportSelectionsDto.setTime(convertTimeToString(reportSelection.getTime()));
					reportSelectionsDto.setWsId(reportSelection.getWS());
					reportSelectionsDto.setUsers(reportSelection.getUsers());
					reportSelectionsDto.setCustomerNo(reportSelection.getCustomerNo());
					reportSelectionsDto.setMarketingCode(reportSelection.getMktgCodeForMovement());
					reportSelectionsDto.setSupplierNo(reportSelection.getSupplierNo());
					reportSelectionsDto.setCustomerNo(reportSelection.getCustomerNo());
					
					if(reportSelection.getBookingAmount() != null && reportSelection.getBookingAmount().trim().length() > 0) {
						reportSelectionsDto.setMenge(reportSelection.getBookingAmount());
					}else {
						reportSelectionsDto.setMenge("---");
					}

					reportSelectionsList.add(reportSelectionsDto);
				}

				globalSearchList.setSearchDetailsList(reportSelectionsList);
				globalSearchList.setTotalPages(totalCount);
				globalSearchList.setTotalRecordCnt(totalCount);

			}else{
				globalSearchList.setSearchDetailsList(reportSelectionsList);
				globalSearchList.setTotalPages(Integer.toString(0));
				globalSearchList.setTotalRecordCnt(Integer.toString(0));
			} 

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_SELECTION_DETAILS_FAILED_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_SELECTION_DETAILS_FAILED_MSG_KEY), exception);
			throw exception;
		}

		return globalSearchList;
	}

	private String createWhereClause(String schema, String dataLibrary, String allowedWarehouses, String manufacturer,
			List<String> warehouseNos, String businessCase, String fromDate, String toDate, boolean invtCustPos, boolean custInvtPos,
			boolean dak, boolean chngOfmktgCode, boolean partTypeOrSupplier, String documentNo, String customerNo,
			String supplierNo, String partNo, String furMktgCode, String brand, boolean isReport, String tableAlias) {

		StringBuilder query = new StringBuilder("");
		
		String warehouseNo = StringUtils.join(warehouseNos, ",");
		
		query.append(" ").append(tableAlias).append("VFNR IN( ").append(warehouseNo).append(" )");	
		
		if(!manufacturer.equalsIgnoreCase("Alle")){
			manufacturer = (manufacturer != null && manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING))? RestInputConstants.DCAG_STRING : manufacturer;
			query.append(" AND ").append(tableAlias).append("HERST = '").append(manufacturer+"'");
		}
		

		if (businessCase != null && !businessCase.isEmpty()) {

			String bas = "";
			for (String ba : businessCase.split(",")) {
				bas = bas + "'" + ba + "',";
			}
			bas = StringUtils.chop(bas);
			
			query.append(" AND ").append(tableAlias).append("BART IN (").append(bas).append(")");			
		}
		else if(invtCustPos || custInvtPos || dak || chngOfmktgCode || partTypeOrSupplier  ){
			query.append(" AND (");
			boolean isFirstValue = false;
			if(invtCustPos){
				query.append(" (BART=49 AND SUBSTR(T1, 3 ,4)= '0051') ");
				isFirstValue = true;
			}
			
			if(custInvtPos){
				if(isFirstValue){
					query.append(" OR ");	
				}
				query.append(" (BART=49 AND SUBSTR(T1, 3 ,4)= '0052') ");
				isFirstValue = true;
			}
			
			if(dak){
				if(isFirstValue){
					query.append(" OR ");	
				}
				query.append("  DAKEWV <> DAKEWN");
				isFirstValue = true;
			}
			
			if(chngOfmktgCode){
				
				if(isFirstValue){
					query.append(" OR ");	
				}
				
				query.append(" MCODE <> MCODEV");
				isFirstValue = true;
			}
			
			if(partTypeOrSupplier){
				if(isFirstValue){
					query.append(" OR ");	
				}
				
				query.append(" (TA_EWV <> TA_EWN OR LIENRV <> LIENRN)");
				isFirstValue = true;
			}
			query.append(" ) ");
		}else{
			log.info("Atleast one selection required between Bewegungsart or Änderungen ");
		}
		
		if(documentNo != null && !documentNo.isEmpty()){
			query.append(" AND ").append(tableAlias).append("BELNR='").append(documentNo).append("'");
		}
		if(customerNo != null && !customerNo.isEmpty()){
			query.append(" AND ").append(tableAlias).append("KDNR='").append(customerNo).append("'");
		}
		if(supplierNo != null && !supplierNo.isEmpty()){
			query.append(" AND ").append(tableAlias).append("BSN_LC='").append(supplierNo).append("'");
		}
		if(partNo != null && !partNo.isEmpty()){
			query.append(" AND KB||ETNR|| ES1 ||ES2='").append(partNo).append("'");
		}
		if(furMktgCode != null && !furMktgCode.isEmpty()){
			query.append(" AND ").append(tableAlias).append("MCODE='").append(furMktgCode).append("'");
		}
		if(brand != null && !brand.isEmpty()){
			query.append(" AND ").append(tableAlias).append("MARKEN='").append(brand).append("'");
		}
		
		if(fromDate!= null && !fromDate.isEmpty() && toDate!= null && !toDate.isEmpty()
				&& fromDate.length() == 10 && toDate.length() == 10 ){

			String startDate = fromDate.substring(8, 10)+fromDate.substring(3, 5)+fromDate.substring(0, 2);
			String tillDate =  toDate.substring(8, 10)+toDate.substring(3, 5)+toDate.substring(0, 2);

			query.append(" AND (JJMMTT BETWEEN  '").append(startDate).append("' AND '").append(tillDate).append("' ) ");

		}else {
			log.info(" fromDate and toDate is required.");
			AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.PARAMETERS_INVALID_MSG_KEY, "", "fromDate, toDate"));
			log.error(messageService.getReadableMessage(ExceptionMessages.PARAMETERS_INVALID_MSG_KEY, "", "fromDate, toDate"));
			throw exception;
		}
		
		if(isReport) {
			query.append(" AND ").append(tableAlias).append("vfnr = et.LNR AND ").append(tableAlias).append("HERST = et.HERST AND  KB||ETNR||ES1||ES2 = et.TNR ");
		}

		return query.toString();
		
	}
	
	
	/**
	 * This method is used to generate (Selektion aus Bewegung ) CSV report From movement List
	 */
	@Override
	public ByteArrayInputStream generateReportFromMovement(String schema, String dataLibrary, String companyId, String allowedWarehouses, 
			String manufacturer, List<String> warehouseNos, String fromDate, String toDate, String businessCase, boolean invtCustPos, boolean custInvtPos,
			boolean dak, boolean chngOfmktgCode, boolean partTypeOrSupplier, String documentNo, String customerNo,
			String supplierNo, String partNo, String furMktgCode, String brand) {

		log.info("Inside generateReportFromMovement method of ReportSelectionsImpl");

		ByteArrayInputStream  byteArrayOutputStream = null;
		List<ReportSelections> reportSelectionList = null;
		boolean isReport = true;

		final String[] headers = {"Hersteller", "Lager", "AX-Lager", "BA", "Datum", "Uhrzeit", "Bildschirm", "User", "Abnehmergruppe/Vorgangscode",
				"Teilenummer", "Benennung aktuell", "Belegnummer", "Position", "Kundennummer", "Marke alt", "Marke neu", 
				"Marketingcode alt", "Marketingcode neu", "Buchungsmenge", "Menge vor BA67", "Menge nach BA 1, 2, 6, 25",
				"Listenpreis alt", "Listenpreis neu", "TA alt", "TA neu", "Leistungsart alt", "Leistungsart neu",
				"Rabattgruppe alt", "Rabattgruppe neu", "Konto alt", "Konto neu", "Fester EK alt", "Fester EK neu",
				"DAK alt", "DAK neu", "TNR Vorgänger alt", "TNR Vorgänger neu", "TNR Nachfolger alt", "TNR Nachfolger neu", 
				"Reifenlabel alt", "Reifenlabel neu", "Satzart alt", "Satzart neu", "Stamm Satzart", "Stamm MC",
				"Stamm Marke", "Stamm Teileart", "Stamm Leistungsart", "Stamm Bestand", "Stamm EPR", "Stamm DAK"
				 };


		final CSVFormat format = CSVFormat.RFC4180.withIgnoreSurroundingSpaces().
				withIgnoreEmptyLines().
				withTrim().
				withDelimiter(';').
				withQuote('"').
				//withRecordSeparator("\r\n").
				withQuoteMode(QuoteMode.ALL).
				withHeader(headers);
		//String fileName = generateFileName("loginName","Movement.csv");   //For File copy on server
		
		 
		try (//FileOutputStream fos =  new FileOutputStream(new File(fileName));   //For File copy on server
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(out, "ISO-8859-1"), format);) {

			//validate the Warehouse Ids
			validateWarehouses(allowedWarehouses);

			StringBuilder query_HISTO = new StringBuilder("SELECT h.HERST, h.VFNR as AP_Lager, (SELECT NAME FROM ").append(schema).append(".O_WRH ");
			query_HISTO.append(" WHERE AP_WAREHOUS_ID=VFNR) as AX_Lager, h.bart, (SELECT DESCRIPTION FROM ").append(schema).append(".O_BA ");
			query_HISTO.append("WHERE BA=BART) as VORGANGSART_TEXT ,");			
			query_HISTO.append("'20'||SUBSTR(JJMMTT, 1 , 2)||'.'||SUBSTR(JJMMTT, 3 , 2)||'.'||SUBSTR(JJMMTT, 5 , 2) AS DATE, ");
			query_HISTO.append("substr(right('00000' || h.HHMMSS,6),1,2) ||':' || substr(right('00000' || h.HHMMSS,6),3,2) ||':' || substr(right('00000' || h.HHMMSS,6),5,2) AS HHMMSS,");
			query_HISTO.append("KB||ETNR||ES1||ES2 AS TEILENUMMER, h.BELNR AS BELEGNUMMER, ");
			query_HISTO.append("h.MARKEN, h.POSNR, h.ws as WS_ID, h.WSUSER, h.KDNR, h.MCODE, h.BSN_LC, h.T1, h.T2, h.T3, ");
			query_HISTO.append(" (CASE WHEN (h.bart=40 OR h.bart=43 OR h.bart=44 OR h.bart=45 OR h.bart=49 OR h.bart=50) THEN 0 ELSE  CAST(SUBSTR(h.T1, 3, 6) AS REAL)/10 END ) AS BUCHUNGSMENGE, ");
			query_HISTO.append(" (CASE WHEN (h.bart=67) THEN CAST(h.EBEST AS REAL)/10 ELSE  0  END ) AS MengeVorBA67, ");
			query_HISTO.append(" (CASE WHEN (h.bart=1 OR h.bart=2 OR h.bart=6 OR h.bart=25) THEN CAST(h.EBEST AS REAL)/10 ELSE 0 END ) AS MengeNachBA_1_2_6_25, ");
			query_HISTO.append(" (CASE WHEN (h.bart=40) THEN CAST(EPREW AS REAL)/100  ELSE  0 END ) AS Listenpreis_Alt, ");
			query_HISTO.append(" (CASE WHEN (h.bart=40) THEN CAST(SUBSTR(h.T1, 9, 7) AS REAL)/100  ELSE  0 END ) AS Listenpreis_Neu, ");
			query_HISTO.append("h.TA_EWV, h.TA_EWN, h.LEIARTV, h.LEIARTN, h.MCODEV, h.BUK_KONTOV, h.BUK_KONTON, h.EPRV, h.EPRN, h.NPREIV, h.NPREIN, ");
			query_HISTO.append("CAST(h.DAKEWN AS REAL) / 10000 AS DAKEWN, CAST(h.DAKEWV AS REAL) / 10000 AS DAKEWV, h.NT_EWV, h.NT_EWN, h.VCODE, h.TNRVV, h.TNRVN, h.TNRNV, h.TNRNN, ");
			query_HISTO.append("h.LIENRV, h.LIENRN, h.LABELV, h.LABELN, h.NW1_EW, h.NW2_EW, h.RABGRV, h.RABGRN, ");
			query_HISTO.append("et.BENEN, et.SA, et.MC, et.TMARKE, et.TA, et.LEIART, et.AKTBES, et.EPR, et.DAK ");
			
			
			StringBuilder query_CPSDAT = new StringBuilder("SELECT cps.HERST, cps.VFNR as AP_Lager, (SELECT NAME FROM ").append(schema).append(".O_WRH ");
			query_CPSDAT.append("WHERE AP_WAREHOUS_ID=VFNR) as AX_Lager, cps.bart, (SELECT DESCRIPTION FROM ").append(schema).append(".O_BA ");
			query_CPSDAT.append("WHERE BA=BART) as VORGANGSART_TEXT ,");			
			query_CPSDAT.append("'20'||SUBSTR(JJMMTT, 1 , 2)||'.'||SUBSTR(JJMMTT, 3 , 2)||'.'||SUBSTR(JJMMTT, 5 , 2) AS DATE, ");
			query_CPSDAT.append("substr(right('00000' || CPS.HHMMSS,6),1,2) ||':' || substr(right('00000' || cps.HHMMSS,6),3,2) ||':' || substr(right('00000' || cps.HHMMSS,6),5,2) AS HHMMSS, ");
			query_CPSDAT.append("KB||ETNR||ES1||ES2 AS TEILENUMMER, cps.BELNR AS BELEGNUMMER, ");
			query_CPSDAT.append("cps.MARKEN, cps.POSNR, cps.ws as WS_ID, cps.WSUSER, cps.KDNR, cps.MCODE, cps.BSN_LC, cps.T1, cps.T2, cps.T3,");
			query_CPSDAT.append(" (CASE WHEN (cps.bart=40 OR cps.bart=43 OR cps.bart=44 OR cps.bart=45 OR cps.bart=49 OR cps.bart=50) THEN 0 ELSE  CAST(SUBSTR(cps.T1, 3, 6) AS REAL)/10 END ) AS BUCHUNGSMENGE, ");
			query_CPSDAT.append(" (CASE WHEN (cps.bart=67) THEN CAST(cps.EBEST AS REAL)/10 ELSE 0 END ) AS MengeVorBA67, ");
			query_CPSDAT.append(" (CASE WHEN (cps.bart=1 OR cps.bart=2 OR cps.bart=6 OR cps.bart=25) THEN  CAST(cps.EBEST AS REAL)/10 ELSE	0 END ) AS MengeNachBA_1_2_6_25, ");
			query_CPSDAT.append(" (CASE WHEN (cps.bart=40) THEN CAST(EPREW AS REAL)/100 ELSE  0 END ) AS Listenpreis_Alt, ");
			query_CPSDAT.append(" (CASE WHEN (cps.bart=40) THEN CAST(SUBSTR(cps.T1, 9, 7) AS REAL)/100  ELSE  0 END ) AS Listenpreis_Neu, "); 
			query_CPSDAT.append("cps.TA_EWV, cps.TA_EWN, cps.LEIARTV, cps.LEIARTN, cps.MCODEV, cps.BUK_KONTOV, cps.BUK_KONTON, cps.EPRV, cps.EPRN, cps.NPREIV, cps.NPREIN, ");
			query_CPSDAT.append("CAST(cps.DAKEWN as REAL) / 10000 AS DAKEWN, CAST(cps.DAKEWV as REAL) / 10000 AS DAKEWV, cps.NT_EWV, cps.NT_EWN, cps.VCODE, cps.TNRVV, cps.TNRVN, cps.TNRNV, cps.TNRNN, ");
			query_CPSDAT.append("cps.LIENRV, cps.LIENRN, cps.LABELV, cps.LABELN, cps.NW1_EW, cps.NW2_EW, cps.RABGRV, cps.RABGRN, ");
			query_CPSDAT.append("et.BENEN, et.SA, et.MC, et.TMARKE, et.TA, et.LEIART, et.AKTBES, et.EPR, et.DAK ");

			//create where clause based on input value
			String whereClause_histo = createWhereClause(schema, dataLibrary, allowedWarehouses, manufacturer, warehouseNos,
					businessCase, fromDate, toDate, invtCustPos, custInvtPos, dak, chngOfmktgCode, partTypeOrSupplier, 
					documentNo, customerNo,supplierNo, partNo, furMktgCode, brand, isReport, "h.");
			
			//create where clause based on input value
			String whereClause_cps = createWhereClause(schema, dataLibrary, allowedWarehouses, manufacturer, warehouseNos,
					businessCase, fromDate, toDate, invtCustPos, custInvtPos, dak, chngOfmktgCode, partTypeOrSupplier, 
					documentNo, customerNo,supplierNo, partNo, furMktgCode, brand, isReport, "cps.");
			
			//get actual results
			StringBuilder queryForHisto = new StringBuilder(query_HISTO);
			queryForHisto.append(" FROM ").append(dataLibrary).append(".E_HISTO h, ").append(dataLibrary).append(".E_ETSTAMM et ").append(" WHERE ").append(whereClause_histo);
			queryForHisto.append(" UNION ");
			queryForHisto.append(query_CPSDAT).append("FROM ").append(dataLibrary).append(".E_CPSDAT cps, ").append(dataLibrary).append(".E_ETSTAMM et ").append(" WHERE ").append(whereClause_cps);
			queryForHisto.append(" ORDER BY DATE DESC, HHMMSS DESC ");
				
			reportSelectionList = dbServiceRepository.getResultUsingQuery(ReportSelections.class, queryForHisto.toString(), true);


			if (reportSelectionList != null && !reportSelectionList.isEmpty()) {

				for(ReportSelections reportSelection : reportSelectionList){
					
					String documentNumber = "--";
					if(reportSelection.getBusinessCaseDocumentNo()!=null && !reportSelection.getBusinessCaseDocumentNo().trim().equalsIgnoreCase("0") &&
							!reportSelection.getBusinessCaseDocumentNo().trim().isEmpty() ){
						documentNumber = reportSelection.getBusinessCaseDocumentNo();
					}
					String menge = "0.00";
					
					if(reportSelection.getBookingAmount() != null && reportSelection.getBookingAmount().trim().length() > 0) {
						menge = reportSelection.getBookingAmount();
					}
					
					List<String> data = Arrays.asList(
							reportSelection.getManufacturer().equalsIgnoreCase(RestInputConstants.DCAG_STRING) ? RestInputConstants.DAG_STRING : reportSelection.getManufacturer(),
							StringUtils.leftPad(String.valueOf(reportSelection.getAlphaPlusWarehouseId()),2,"0"),
							reportSelection.getAlphaxWarehouseName(),
							reportSelection.getMovementType() +"-"+reportSelection.getMovementTypeName(),
							convertDateToDDMMYYYY(reportSelection.getBusinessCaseDate()),
							reportSelection.getTime(),							
							reportSelection.getWS(),
							reportSelection.getUsers(),
							reportSelection.getProcessCode(),
							reportSelection.getBusinessCasePartNo(),
							reportSelection.getName(),
							documentNumber,
							reportSelection.getPositionNo(),
							reportSelection.getCustomerNo(),
							
							reportSelection.getBrandAlt(),
							reportSelection.getBrand(),
							reportSelection.getMarketingCode_Alt(),
							reportSelection.getMktgCodeForMovement(),
							
							decimalformat_Onedigit.format(Double.parseDouble(menge)),
							decimalformat_Twodigit.format(reportSelection.getMengeVorBA67() !=null ? reportSelection.getMengeVorBA67() : 0.00),
							decimalformat_Twodigit.format(reportSelection.getMengeNachBA() !=null ? reportSelection.getMengeNachBA() : 0.00),
							decimalformat_Twodigit.format(reportSelection.getListenpreisAlt() !=null ? reportSelection.getListenpreisAlt() : 0.00),
							decimalformat_Twodigit.format(reportSelection.getListenpreisNeu() !=null ? reportSelection.getListenpreisNeu() : 0.00),
							
							reportSelection.getTa_Alt(),
							reportSelection.getTa_Neu(), 
							reportSelection.getLa_Alt()!=null ? String.valueOf(reportSelection.getLa_Alt()) :"0.00",
							reportSelection.getLa_Neu()!=null ? String.valueOf(reportSelection.getLa_Neu()) :"0.00",
							reportSelection.getRb_Alt(),
							reportSelection.getRb_New(),
							reportSelection.getKonto_Alt()!=null ? String.valueOf(reportSelection.getKonto_Alt()) : "0.00",
							reportSelection.getKonto_Neu()!=null ? String.valueOf(reportSelection.getKonto_Neu()) : "0.00",
							
							decimalformat_Twodigit.format(reportSelection.getNetPrice_Alt()!=null ? reportSelection.getNetPrice_Alt().doubleValue() : 0.00),
							decimalformat_Twodigit.format(reportSelection.getNetPrice_Neu()!=null ? reportSelection.getNetPrice_Neu().doubleValue() : 0.00),
							decimalformat_Fourdigit.format((reportSelection.getDak_Alt() !=null && !reportSelection.getDak_Alt().isEmpty()) ? Double.parseDouble(reportSelection.getDak_Alt()) : 0.00),
							decimalformat_Fourdigit.format((reportSelection.getDak_Neu()!=null && !reportSelection.getDak_Neu().isEmpty()) ? Double.parseDouble(reportSelection.getDak_Neu()) : 0.00),
							
							reportSelection.getPredecessor_Alt(),
							reportSelection.getPredecessor_Neu(),
							reportSelection.getSuccessor_Alt(),
							reportSelection.getSuccessor_Neu(),							
							reportSelection.getLabel_Alt(),
							reportSelection.getLabel_Neu(),
							reportSelection.getSa_Alt(),
							reportSelection.getSa_Neu(),
							reportSelection.getStorageIndikator() !=null ? String.valueOf(reportSelection.getStorageIndikator()) :"0",
							reportSelection.getMarketingCode(),
							reportSelection.getPartBrand(),
							reportSelection.getPartsIndikator() !=null ? String.valueOf(reportSelection.getPartsIndikator()) :"0.00",
							reportSelection.getActivityType() !=null ? String.valueOf(reportSelection.getActivityType()) :"0.00",
							decimalformat_Twodigit.format(reportSelection.getCurrentStock() !=null ? reportSelection.getCurrentStock().doubleValue() : 0.00),						
							decimalformat_Twodigit.format(reportSelection.getPurchasePrice() !=null ? reportSelection.getPurchasePrice().doubleValue() : 0.00),
							decimalformat_Fourdigit.format(reportSelection.getAverageNetPrice() !=null ? reportSelection.getAverageNetPrice().doubleValue() : 0.00)							
							
						);

					csvPrinter.printRecord(data);
				}

				// writing the underlying stream
				csvPrinter.flush();
					//out.writeTo(fos);  //For File copy on server
				byteArrayOutputStream = new ByteArrayInputStream(out.toByteArray());
			}			

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_SELECTION_DETAILS_FAILED_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_SELECTION_DETAILS_FAILED_MSG_KEY), exception);
			throw exception;
		}

		return byteArrayOutputStream;
	}
	
	
	/**
	 * This method is used to get the Booking related List (Buchungsrelevante Listen)
	 */
	@Override
	public List<ReportSelectionsForBusinessCaseDTO> generateBookingReletedList(String schema, String dataLibrary, String allowedWarehouses,
			List<String> warehouseNos, String manufacturer, String fromDate, String toDate, boolean includeArchivedData) {

		log.info("Inside generateBookingReletedList method of ReportSelectionsImpl");

		List<ReportSelectionsForBusinessCaseDTO> reportSelectionsList = null;

		try {

			//validate the Warehouse Ids
			validateWarehouses(allowedWarehouses);

			//create where clause based on input value
			List<ReportSelectionBookingList> reportSelectionsBookingList = generateQueryforBookingList(schema, dataLibrary, allowedWarehouses, 
					warehouseNos, manufacturer, fromDate, toDate, includeArchivedData);
			
			reportSelectionsList = setValueForBookingRelatedList(reportSelectionsBookingList);

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Booking related list (Buchungsrelevante Listen)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Booking related list (Buchungsrelevante Listen)"), exception);
			throw exception;
		}

		return reportSelectionsList;
	}
	
	
	private List<ReportSelectionsForBusinessCaseDTO> setValueForBookingRelatedList(List<ReportSelectionBookingList> reportSelectionBookingList) {

		List<ReportSelectionsForBusinessCaseDTO> reportSelectionsList = new ArrayList<>();
		String century = "20";
		
		if (reportSelectionBookingList != null && !reportSelectionBookingList.isEmpty()) {

			for(ReportSelectionBookingList reportSelection : reportSelectionBookingList){
				ReportSelectionsForBusinessCaseDTO reportSelectionsDto = new ReportSelectionsForBusinessCaseDTO();

				reportSelectionsDto.setWarehouse(reportSelection.getAlphaPlusWarehouseId()+ " - " +reportSelection.getAlphaxWarehouseName());
				reportSelectionsDto.setDate(convertDateToString(century+reportSelection.getBookingDate()));
				reportSelectionsDto.setPartNumber(reportSelection.getPartNumber());
				reportSelectionsDto.setDocumentNumber(reportSelection.getBusinessCaseDocumentNo());
				reportSelectionsDto.setBestandAlt(reportSelection.getBestandAlt());
				reportSelectionsDto.setBestandNeu(reportSelection.getBestandNeu());
				reportSelectionsDto.setManufacturer(reportSelection.getManufacturer()!=null && reportSelection.getManufacturer().equalsIgnoreCase(RestInputConstants.DCAG_STRING)? RestInputConstants.DAG_STRING : reportSelection.getManufacturer());

				
				//Extra fields for detail view
				reportSelectionsDto.setPartName(reportSelection.getPartName()); //Bezeichnung
				reportSelectionsDto.setPartType(reportSelection.getPartType()); //Teileart
				reportSelectionsDto.setActivityType(reportSelection.getActivityType()); //Leistungsart
				reportSelectionsDto.setDiscountGroup(reportSelection.getDiscountGroup());  //Rabattgruppe
				reportSelectionsDto.setDiscountGroupPercentageValue(String.valueOf(reportSelection.getDiscountGroupPercentageValue()));  //Rabattsatz
				reportSelectionsDto.setAverageNetPrice(String.valueOf(reportSelection.getAverageNetPrice()));  //DAK
				reportSelectionsDto.setBookingAccount(reportSelection.getBookingAccount());  //Buchungskonto
				reportSelectionsDto.setDifference_EURO(String.valueOf(reportSelection.getDifference_EURO()));  //Differenz_EUR	
				
				reportSelectionsList.add(reportSelectionsDto);
			}
		}
		return reportSelectionsList;
	}
	
	
	private List<ReportSelectionBookingList> generateQueryforBookingList(String schema, String dataLibrary, String allowedWarehouses, List<String> warehouseNos, 
			String manufacturer, String fromDate, String toDate, boolean includeArchivedData){

		StringBuilder whereQuery = new StringBuilder("");
		
		String warehouseNo = StringUtils.join(warehouseNos, ",");
		
		if(!manufacturer.equalsIgnoreCase("Alle")){
			manufacturer = (manufacturer != null && manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING))? RestInputConstants.DCAG_STRING : manufacturer;
			whereQuery.append(" and HERST = '").append(manufacturer+"'");	
		}

		whereQuery.append(" and VFNR IN( ").append(warehouseNo).append(" )");
				
		if(fromDate!= null && !fromDate.isEmpty() && toDate!= null && !toDate.isEmpty()
				&& fromDate.length() == 10 && toDate.length() == 10 ){

			String startDate = fromDate.substring(8, 10)+fromDate.substring(3, 5)+fromDate.substring(0, 2);
			String tillDate =  toDate.substring(8, 10)+toDate.substring(3, 5)+toDate.substring(0, 2);

			whereQuery.append(" and (JJMMTT BETWEEN  '").append(startDate).append("' AND '").append(tillDate).append("' ) ");

		}else {
			log.info(" fromDate and toDate is required.");
			AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.PARAMETERS_INVALID_MSG_KEY, "", "fromDate, toDate"));
			log.error(messageService.getReadableMessage(ExceptionMessages.PARAMETERS_INVALID_MSG_KEY, "", "fromDate, toDate"));
			throw exception;
		}
	
		StringBuilder query = new StringBuilder("Select HERST, VFNR as AP_Lager, ");
		query.append(" (select NAME from ").append(schema).append(".O_WRH where AP_WAREHOUS_ID=VFNR) as aX_Lager, KB||ETNR||ES1||ES2 as Teilenummer, "); 
		query.append(" (select BENEN from ").append(dataLibrary).append(".e_etstamm where tnrs=ETNR_S and herst=HERST and lnr=VFNR FETCH FIRST 1 ROWS ONLY) as Bezeichnung, ");
		query.append(" BELNR as Belegnummer, JJMMTT as Datum, HHMMSS as Uhrzeit, ");
		query.append(" cast(EB_VZ||EBEST as real)/10 as Bestand_alt, ");
		query.append(" cast(substr(T1, 3 , 6) as real)/10 as Bestand_neu, (cast(substr(T1, 3 , 6) as real)/10)-(cast(EBEST as real)/10) as Differenz, ");
		query.append(" TA_EWN  || ' - ' ||  (select kindname from ").append(schema).append(".O_PARTKIND where partkind_id=TA_EWN) as Teileart, ");
		query.append(" MARKEN as Marke, LEIARTN as Leistungsart, RABGRN as Rabattgruppe, cast(PROZ as real)/100 as Rabattsatz, cast(DAKEWN as real)/10000 as DAK, ");
		query.append(" round(((cast(substr(T1, 3 , 6) as real)/10)-(cast(EB_VZ||EBEST as real)/10))*(cast(DAKEWN as real)/10000), 4) as Differenz_EUR, ");
		query.append(" BUK_KONTON as Buchungskonto, WS as Workstation_ID, WSUSER as alphaX_User"); 
		query.append(" from ").append(dataLibrary).append(".e_cpsdat where bart=67 ").append(whereQuery);

		if(includeArchivedData) {
			query.append(" UNION ");

			query.append("Select HERST, VFNR as AP_Lager, ");
			query.append(" (select NAME from ").append(schema).append(".O_WRH where AP_WAREHOUS_ID=VFNR) as aX_Lager, KB||ETNR||ES1||ES2 as Teilenummer, ");
			query.append(" (select BENEN from ").append(dataLibrary).append(".e_etstamm where tnrs=ETNR_S and herst=HERST and lnr=VFNR FETCH FIRST 1 ROWS ONLY) as Bezeichnung, ");
			query.append(" BELNR as Belegnummer, JJMMTT as Datum, HHMMSS as Uhrzeit, ");
			query.append(" cast(EB_VZ||EBEST as real)/10 as Bestand_alt, ");
			query.append(" cast(substr(T1, 3 , 6) as real)/10 as Bestand_neu, (cast(substr(T1, 3 , 6) as real)/10)-(cast(EBEST as real)/10) as Differenz, ");
			query.append(" TA_EWN  || ' - ' ||  (select kindname from ").append(schema).append(".O_PARTKIND where partkind_id=TA_EWN) as Teileart, ");
			query.append(" MARKEN as Marke, LEIARTN as Leistungsart, RABGRN as Rabattgruppe, cast(PROZ as real)/100 as Rabattsatz, cast(DAKEWN as real)/10000 as DAK, ");
			query.append(" round(((cast(substr(T1, 3 , 6) as real)/10)-(cast(EB_VZ||EBEST as real)/10))*(cast(DAKEWN as real)/10000), 4) as Differenz_EUR, ");
			query.append(" BUK_KONTON as Buchungskonto, WS as Workstation_ID, WSUSER as alphaX_User ");
			query.append(" from ").append(dataLibrary).append(".e_histo where bart=67 ").append(whereQuery);
		
		}
		query.append(" Order by Datum desc, Uhrzeit desc ");
		
		List<ReportSelectionBookingList> reportSelectionList = dbServiceRepository.getResultUsingQuery(ReportSelectionBookingList.class, query.toString(), true);

		return reportSelectionList;

	}
	
	
	/**
	 * This method is used for create report based on Booking related List (Buchungsrelevante Listen) 
	 */
	@Override
	public ByteArrayInputStream generateReportOnBookingReletedList(String schema, String dataLibrary, String allowedWarehouses, 
			List<String> warehouseNos, String manufacturer, String fromDate, String toDate, boolean includeArchivedData ) {

		log.info("Inside generateReportOnBookingReletedList method of ReportSelectionsImpl");
		
		ByteArrayInputStream  byteArrayOutputStream = null;
		String century = "20";

		final String[] headers = { "AP-Lager Nr.",	"AX-Lager",	"Hersteller", "Teilemarke", 
				"Teilenummer", "Bezeichnung", "Belegnummer", "Datum", "Uhrzeit", "Bestand alt", "Bestand neu", "Differenz", "Teileart",
				"Leistungsart", "Rabattgruppe", "Rabattsatz", "DAK", "Differenz EUR", "Buchungskonto", "Workstation ID", "alphaX User" };

		final CSVFormat format = CSVFormat.RFC4180.withIgnoreSurroundingSpaces().
				withIgnoreEmptyLines().
				withTrim().
				withDelimiter(';').
				withQuote('"').
				//withRecordSeparator("\r\n").
				withQuoteMode(QuoteMode.ALL).
				withHeader(headers);

		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
				CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(out, "ISO-8859-1"), format);) {

			List<ReportSelectionBookingList> reportSelectionList = generateQueryforBookingList(schema, dataLibrary, allowedWarehouses, warehouseNos, manufacturer, 
					fromDate, toDate, includeArchivedData);

			if (reportSelectionList != null && !reportSelectionList.isEmpty()) {

				for(ReportSelectionBookingList reportSelection : reportSelectionList){

					List<String> data = Arrays.asList(										
							StringUtils.leftPad(String.valueOf(reportSelection.getAlphaPlusWarehouseId()),2,"0"),
							reportSelection.getAlphaxWarehouseName(),
							reportSelection.getManufacturer()!=null && reportSelection.getManufacturer().equalsIgnoreCase(RestInputConstants.DCAG_STRING)? RestInputConstants.DAG_STRING :reportSelection.getManufacturer(),
									reportSelection.getPartBrand(),
									reportSelection.getPartNumber(),									
									reportSelection.getPartName(),	
									reportSelection.getBusinessCaseDocumentNo(),
									convertDateToString(century+reportSelection.getBookingDate()),
									convertTimeToString(reportSelection.getBookingTime()),
									replaceDotToComma(reportSelection.getBestandAlt()),
									replaceDotToComma(reportSelection.getBestandNeu()),
									replaceDotToComma(String.valueOf(reportSelection.getDifference())),
									reportSelection.getPartType(),
									reportSelection.getActivityType(),
									reportSelection.getDiscountGroup(),
									replaceDotToComma(String.valueOf(reportSelection.getDiscountGroupPercentageValue())),
									replaceDotToComma(String.valueOf(reportSelection.getAverageNetPrice())),
									replaceDotToComma(String.valueOf(reportSelection.getDifference_EURO())),
									reportSelection.getBookingAccount(),
									reportSelection.getWorkstationID(),
									reportSelection.getAlphaXUser()									
							);

					csvPrinter.printRecord(data);
				}

				// writing the underlying stream
				csvPrinter.flush();

				byteArrayOutputStream = new ByteArrayInputStream(out.toByteArray());
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Selections report based on Booking related List"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Selections report based on Booking related List"), exception);
			throw exception;
		}

		return byteArrayOutputStream;
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
	
	
	/**
	 * This method is used Get a List for Search columns (Selektion aus Bewegung)
	 */
	@Override
	public List<MovementSearchDTO> searchMovementList(String dataLibrary, String searchType, String searchValue, boolean archData ) {
		
		log.info("Inside searchMovementList method of ReportSelectionsImpl ");
		
		StringBuilder query = new StringBuilder();
		
		List<MovementSearchDTO> searchMovementList = new ArrayList<>();

		switch(searchType) {
		case "BELEGNUMMER":
			query.append("Select distinct(BELNR) as result from ");
			query.append(dataLibrary).append(".E_CPSDAT where BELNR like '%").append(searchValue).append("%' "); 

			if(archData) {
				query.append(" UNION ");
				query.append("Select distinct(BELNR) as result from ");
				query.append(dataLibrary).append(".E_HISTO where BELNR like '%").append(searchValue).append("%' "); 
			}
			query.append(" Order by result ");
		break;
		
		case "KUNDENNUMMER":
			query.append("Select distinct(KDNR) as result from ");
			query.append(dataLibrary).append(".E_CPSDAT where KDNR like '%").append(searchValue).append("%' "); 

			if(archData) {
				query.append(" UNION ");
				query.append("Select distinct(KDNR) as result from ");
				query.append(dataLibrary).append(".E_HISTO where KDNR like '%").append(searchValue).append("%' "); 
			}
			query.append(" Order by result ");
		break;
		
		case "LIEFERANTENNUMMER":
			query.append("Select distinct(BSN_LC) as result from ");
			query.append(dataLibrary).append(".E_CPSDAT where BSN_LC like '%").append(searchValue).append("%' "); 

			if(archData) {
				query.append(" UNION ");
				query.append("Select distinct(BSN_LC) as result from ");
				query.append(dataLibrary).append(".E_HISTO where BSN_LC like '%").append(searchValue).append("%' "); 
			}
			query.append(" Order by result ");
		break;
		
		case "TEILENUMMER":
			if((searchValue.startsWith("A") || searchValue.startsWith("a")) && searchValue.length() >= 11) {
				searchValue = searchValue.substring(0, 11) + '%' + searchValue.substring(11);
			}
			query.append("Select distinct(KB||ETNR||ES1||ES2) as result from ");
			query.append(dataLibrary).append(".E_CPSDAT where UPPER(KB||ETNR||ES1||ES2) like UPPER('%").append(searchValue).append("%') "); 

			if(archData) {
				query.append(" UNION ");
				query.append("Select distinct(KB||ETNR||ES1||ES2) as result from ");
				query.append(dataLibrary).append(".E_HISTO where UPPER(KB||ETNR||ES1||ES2) like UPPER('%").append(searchValue).append("%') "); 
			}
			query.append(" Order by result ");
		break;
		
		case "MARKETINGCODE":
			query.append("Select distinct(MCODE) as result from ");
			query.append(dataLibrary).append(".E_CPSDAT where MCODE like '%").append(searchValue).append("%' "); 

			if(archData) {
				query.append(" UNION ");
				query.append("Select distinct(MCODE) as result from ");
				query.append(dataLibrary).append(".E_HISTO where MCODE like '%").append(searchValue).append("%' "); 
			}
			query.append(" Order by result ");
		break;
		
		case "MARKE":
			query.append("Select distinct(MARKEN) as result from ");
			query.append(dataLibrary).append(".E_CPSDAT where MARKEN like '%").append(searchValue).append("%' "); 

			if(archData) {
				query.append(" UNION ");
				query.append("Select distinct(MARKEN) as result from ");
				query.append(dataLibrary).append(".E_HISTO where MARKEN like '%").append(searchValue).append("%' "); 
			}
			query.append(" Order by result ");
		break;
		
		default: 
			log.info("Incorrect Search Type passed from Client");
			Message message = messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.SEARCH_FAILED_MSG_KEY, "Weitere Einschränkungen");
			AlphaXException exception = new AlphaXException(message);
			log.error(messageService.getReadableMessage(ExceptionMessages.SEARCH_FAILED_MSG_KEY, "Weitere Einschränkungen"), exception);
			throw exception;
		}
		
		try {
			
			List<ReportSelections> searchList = dbServiceRepository.getResultUsingQuery(ReportSelections.class, query.toString(), true);
			
			if(searchList != null && !searchList.isEmpty()) {
				
				for(ReportSelections searchObj:searchList){
					MovementSearchDTO mSearchDTO = new MovementSearchDTO();
					mSearchDTO.setSearchResult(searchObj.getSearchResult());
					searchMovementList.add(mSearchDTO);
				}
			}else{
				log.debug("Movement Search List is empty");
			}
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Bewegung"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Bewegung"), exception);
			throw exception;
		}
		
		return searchMovementList;
	}	
	
	  //For File copy on server 
	/*private String generateFileName(String loginName, String fileName){
		
		try {
			FileUtils.cleanDirectory(new File("C:/AlphaX/AlphaXBrokerService/CSV Files"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd HHmmss");  
	     Date date = new Date();
		return "C:/AlphaX/AlphaXBrokerService/CSV Files/"+formatter.format(date)+"-AlphaX-"+fileName;
	}*/
	
	
	
	/**
	 * This method is used to get Part Supply related List (Teileanbietung)
	 */
	@Override
	public GlobalSearch generatePartSupplyRelatedList(String schema, String dataLibrary, String allowedWarehouses, String companyId, String pageSize,
			String pageNumber) {

		log.info("Inside generatePartSupplyRelatedList method of ReportSelectionsImpl");

		List<ReportSelectionsDTO> reportSelectionsList = new ArrayList<>();
		GlobalSearch globalSearchList = new GlobalSearch();

		try {
			//validate the company Id
			validateCompany(companyId);

			//validate the Warehouse Ids
			validateWarehouses(allowedWarehouses);

			if(pageSize==null || pageNumber==null || pageSize.isEmpty() || pageNumber.isEmpty()){
				pageSize = RestInputConstants.DEFAULT_PAGE_SIZE;
				pageNumber = RestInputConstants.DEFAULT_PAGE_NO;
			}

			int totalRecords = Integer.parseInt(pageSize);
			int nextRows = totalRecords * (Integer.parseInt(pageNumber) - 1);

			//validate the page size
			validatePageSize(totalRecords);
			log.info("PageSize  : {}  OFFSET Size (Skip Rows):  {} Page Number  : {} ", pageSize, nextRows, pageNumber);

			//create where clause based on input value
			List<ReportSelections> reportSelectionPartSupplyList = generateQueryforPartSupplyList(schema, dataLibrary, allowedWarehouses, companyId, nextRows, pageSize );

			if (reportSelectionPartSupplyList != null && !reportSelectionPartSupplyList.isEmpty()) {

				reportSelectionsList = setValueForPartSupplyRelatedList(reportSelectionPartSupplyList);

				globalSearchList.setSearchDetailsList(reportSelectionsList);
				globalSearchList.setTotalPages(Integer.toString(reportSelectionPartSupplyList.get(0).getTotalCount()));
				globalSearchList.setTotalRecordCnt(Integer.toString(reportSelectionPartSupplyList.get(0).getTotalCount()));

			}
			else{
				globalSearchList.setSearchDetailsList(reportSelectionsList);
				globalSearchList.setTotalPages(Integer.toString(0));
				globalSearchList.setTotalRecordCnt(Integer.toString(0));
			} 

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Part Supply related list (Teileanbietung)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Part Supply related list (Teileanbietung)"), exception);
			throw exception;
		}

		return globalSearchList;
	}
	
	
	private List<ReportSelections> generateQueryforPartSupplyList(String schema, String dataLibrary, String allowedWarehouses, String companyId, 
			int nextRows, String pageSize) {

		StringBuilder query = new StringBuilder("Select LNR as AP_Lager, TNR, SA, EPR, BENEN, AKTBES, HERST, ");
		query.append(" (select NAME from ").append(schema).append(".O_WRH where AP_WAREHOUS_ID=lnr) as AX_Lager ");
		
		if(pageSize != null) {
			query.append(", (Select count(*) from ").append(dataLibrary).append(".e_etstamm where TNR in ");
			query.append(" (select distinct etnr from ").append(dataLibrary).append(".e_bsndat where REC_DAT >= VARCHAR_FORMAT(CURRENT TIMESTAMP - 28 DAY, 'YYYYMMDD') ) ");
			query.append(" AND EPR >= 1 AND HERST='DCAG' AND not substr(tnr, 14, 2)='26' AND not substr(tnr, 14, 2)='27' AND DRTKZ='' ");
			query.append(" AND NOT (GFKZ='REAC' AND epr < 200) AND aktbes > 0 AND LNR IN ( ").append(allowedWarehouses).append(") ");
			query.append(") as ROWNUMER ");
		}

		query.append(" from ").append(dataLibrary).append(".e_etstamm where TNR in ");		
		query.append(" (select distinct etnr from ").append(dataLibrary).append(".e_bsndat where REC_DAT >= VARCHAR_FORMAT(CURRENT TIMESTAMP - 28 DAY, 'YYYYMMDD') ) "); 
		query.append(" AND EPR >= 1 AND HERST='DCAG' AND not substr(tnr, 14, 2)='26' AND not substr(tnr, 14, 2)='27' AND DRTKZ='' ");
		query.append(" AND NOT (GFKZ='REAC' AND epr < 200) AND aktbes > 0 AND LNR IN ( ").append(allowedWarehouses).append(") ");
		query.append(" order by TNR ");
		
		if(pageSize != null) {
			query.append(" OFFSET ").append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");
		}

		List<ReportSelections> reportSelectionList = dbServiceRepository.getResultUsingQuery(ReportSelections.class, query.toString(), true);

		return reportSelectionList;
	}
	
	
	private List<ReportSelectionsDTO> setValueForPartSupplyRelatedList(List<ReportSelections> reportSelectionPartSupplyList) {

		List<ReportSelectionsDTO> reportSelectionsList = new ArrayList<>();

		for(ReportSelections reportSelection : reportSelectionPartSupplyList){
			ReportSelectionsDTO reportSelectionsDto = new ReportSelectionsDTO();

			reportSelectionsDto.setAlphaPlusWarehouseId(StringUtils.leftPad(String.valueOf(reportSelection.getAlphaPlusWarehouseId()),2,"0"));
			reportSelectionsDto.setAlphaxWarehouseName(reportSelection.getAlphaxWarehouseName());
			reportSelectionsDto.setStorageIndikator(String.valueOf(reportSelection.getStorageIndikator()));
			reportSelectionsDto.setPartNumber(reportSelection.getPartNumber());
			reportSelectionsDto.setName(reportSelection.getName());
			reportSelectionsDto.setCurrentStock(String.valueOf(reportSelection.getCurrentStock()));
			reportSelectionsDto.setPurchasePrice(String.valueOf(reportSelection.getPurchasePrice()));
			reportSelectionsDto.setManufacturer(reportSelection.getManufacturer()!=null && reportSelection.getManufacturer().equalsIgnoreCase(RestInputConstants.DCAG_STRING)? RestInputConstants.DAG_STRING : reportSelection.getManufacturer());
			
			reportSelectionsList.add(reportSelectionsDto);
		}
		return reportSelectionsList;
	}
	
	
	/**
	 * This method is used for create report for Parts Supply related List (Teileanbietung - CSV) 
	 */
	@Override
	public ByteArrayInputStream generateReportForPartSupply(String schema, String dataLibrary, String allowedWarehouses, String companyId ) {

		log.info("Inside generateReportForPartSupply method of ReportSelectionsImpl");

		ByteArrayInputStream  byteArrayOutputStream = null;

		final String[] headers = { "AP-Lager Nr.", "AX-Lager", "Teilenummer", "Bezeichnung", "Satzart", "Einkaufspreis", "Bestand" };

		final CSVFormat format = CSVFormat.RFC4180.withIgnoreSurroundingSpaces().
				withIgnoreEmptyLines().
				withTrim().
				withDelimiter(';').
				withQuote('"').
				//withRecordSeparator("\r\n").
				withQuoteMode(QuoteMode.ALL).
				withHeader(headers);

		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
				CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(out, "ISO-8859-1"), format);) {

			List<ReportSelections> reportSelectionPartSupplyList = generateQueryforPartSupplyList(schema, dataLibrary, allowedWarehouses, companyId, 0, null );

			if (reportSelectionPartSupplyList != null && !reportSelectionPartSupplyList.isEmpty()) {

				for(ReportSelections reportSelection : reportSelectionPartSupplyList){

					List<String> data = Arrays.asList(										
							StringUtils.leftPad(String.valueOf(reportSelection.getAlphaPlusWarehouseId()),2,"0"),
							reportSelection.getAlphaxWarehouseName(),
							reportSelection.getPartNumber(),									
							reportSelection.getName(),	
							replaceDotToComma(String.valueOf(reportSelection.getStorageIndikator())),			
							replaceDotToComma(String.valueOf(reportSelection.getPurchasePrice())),
							replaceDotToComma(String.valueOf(reportSelection.getCurrentStock()))
							);

					csvPrinter.printRecord(data);
				}

				// writing the underlying stream
				csvPrinter.flush();

				byteArrayOutputStream = new ByteArrayInputStream(out.toByteArray());
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Selections report for Parts Supply (Teileanbietung)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Selections report for Parts Supply (Teileanbietung)"), exception);
			throw exception;
		}

		return byteArrayOutputStream;
	}
	
	
	/**
	 * This method is used to convert date string YYYYMMDD  to dd.MM.YYYY
	 * @param entityDate
	 * @return
	 */
	private String convertDateToDDMMYYYY(String entityDate) {

		StringBuilder dateBuilder = new StringBuilder();

		if(entityDate!=null && !entityDate.isEmpty() && entityDate.length() == 10) {
			dateBuilder.append(entityDate.substring(8, 10)).append(".");
			dateBuilder.append(entityDate.substring(5,7)).append(".").append(entityDate.substring(0, 4));
		}
		return dateBuilder.toString();
	}
	
	
	/**
	 * This method is used to get Intra Trade Statistics List (Intrahandelsstatistik)
	 */
	@Override
	public GlobalSearch generateIntraTradeStatisticsList(String schema, String dataLibrary, String fromDate, String toDate, String customerNo, 
			String customerGroup, String pageSize, String pageNumber) {

		log.info("Inside generateIntraTradeStatisticsList method of ReportSelectionsImpl");

		List<ReportSelectionIntraTradeDTO> reportSelectionsList = new ArrayList<>();
		GlobalSearch globalSearchList = new GlobalSearch();
		String startDate = "";
		String tillDate = "";

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
			
			startDate = formatFromToDateforQuery(fromDate);
			tillDate = formatFromToDateforQuery(toDate);

			//create where clause based on input value
			List<ReportSelectionIntraTradeList> reportSelectionIntraTradeList = generateQueryforIntraTradeList(schema, dataLibrary, startDate, tillDate, customerNo, 
					customerGroup, nextRows, pageSize );			
			
			String totalCount = totalCountFromQuery(dataLibrary, startDate, tillDate, customerNo, customerGroup);
			
			if (reportSelectionIntraTradeList != null && !reportSelectionIntraTradeList.isEmpty()) {

				reportSelectionsList = setValueForIntraTradeStatsList(reportSelectionIntraTradeList);

				globalSearchList.setSearchDetailsList(reportSelectionsList);
				globalSearchList.setTotalPages(totalCount);
				globalSearchList.setTotalRecordCnt(totalCount);
			}
			else{
				globalSearchList.setSearchDetailsList(reportSelectionsList);
				globalSearchList.setTotalPages(Integer.toString(0));
				globalSearchList.setTotalRecordCnt(Integer.toString(0));
			} 

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "IntraTrade Statistics list (Intrahandelsstatistik)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"IntraTrade Statistics list (Intrahandelsstatistik)"), exception);
			throw exception;
		}

		return globalSearchList;
	}
	
	
	private String formatFromToDateforQuery(String entityDate) {
		
		String formattedDate = "";
		
		if(entityDate!= null && !entityDate.isEmpty() && entityDate.length() == 10 ){

			formattedDate = entityDate.substring(8, 10)+entityDate.substring(3, 5)+entityDate.substring(0, 2);
		}
		else {
			log.info(" fromDate and toDate is required.");
			AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.PARAMETERS_INVALID_MSG_KEY, "", "fromDate, toDate"));
			log.error(messageService.getReadableMessage(ExceptionMessages.PARAMETERS_INVALID_MSG_KEY, "", "fromDate, toDate"));
			throw exception;
		}
		
		return formattedDate;
	}
	
	
	private List<ReportSelectionIntraTradeList> generateQueryforIntraTradeList(String schema, String dataLibrary, String startDate, String tillDate, String customerNo, 
			String customerGroup, int nextRows, String pageSize) {
		
		if(null == customerNo) {
			customerNo = "";
		}
		if(null == customerGroup) {
			customerGroup = "";
		}
		 
		StringBuilder query = new StringBuilder("Select BELNR as Belegnummer, BART as BA_Nummer, ETNR as Teilenummer, ES1, ES2, JJMMTT as Datum, ");
		query.append("TRIM(BOTH FROM T4) as Kunde, ");
		query.append("(select DISTINCT TLE_BENENA from ").append(schema).append(".ETK_TLEKAT where TLE_TNR like '%' || TRIM(BOTH FROM ETNR) ||'%' LIMIT 1) as Bezeichnung, ");
		query.append("(select DISTINCT TLE_ZOLLNR from ").append(schema).append(".ETK_TLEKAT where TLE_TNR like '%' || TRIM(BOTH FROM ETNR) ||'%' LIMIT 1) as Zollnummer, ");
		query.append("(select DISTINCT FAKBET from ").append(dataLibrary).append(".F_AUFPOSET where AUFNR like '%' || substr(BELNR, 3 , 4) ");
		query.append(" || '%' and TNR=ETNR and THEKEKZ='T') as Fakturierungsbetrag, ");
		query.append("(select count(*) from ").append(dataLibrary).append(".F_AUFPOSET where AUFNR like '%' || substr(BELNR, 3 , 4) || '%' ");
		query.append(" and TNR=ETNR and THEKEKZ='T') as Anzahl, ");		
		query.append(" round(cast((select count(*) from ").append(dataLibrary).append(".F_AUFPOSET where AUFNR like '%' || substr(BELNR, 3 , 4) || '%' ");
		query.append(" and TNR=ETNR and THEKEKZ='T') as real) ");
		query.append(" * cast((select DISTINCT FAKBET from ").append(dataLibrary).append(".F_AUFPOSET where AUFNR like '%' || substr(BELNR, 3 , 4) || '%' ");
		query.append(" and TNR=ETNR and THEKEKZ='T') as real), 2) as Summe ");
		query.append(" from ").append(dataLibrary).append(".E_CPSDAT where T4 like '%").append(customerNo).append("% %").append(customerGroup);
		query.append("%' AND BART=20 AND (JJMMTT BETWEEN '").append(startDate).append("' AND '").append(tillDate).append("' ) ");
		
		query.append(" UNION ");
		
		query.append("Select BELNR as Belegnummer, BART as BA_Nummer, ETNR as Teilenummer, ES1, ES2, JJMMTT as Datum, ");
		query.append("TRIM(BOTH FROM T4) as Kunde, ");
		query.append("(select DISTINCT TLE_BENENA from ").append(schema).append(".ETK_TLEKAT where TLE_TNR like '%' || TRIM(BOTH FROM ETNR) ||'%' LIMIT 1) as Bezeichnung, ");
		query.append("(select DISTINCT TLE_ZOLLNR from ").append(schema).append(".ETK_TLEKAT where TLE_TNR like '%' || TRIM(BOTH FROM ETNR) ||'%' LIMIT 1) as Zollnummer, ");
		query.append("(select DISTINCT FAKBET from ").append(dataLibrary).append(".F_ARCPOSET where AUFNR like '%' || substr(BELNR, 3 , 4) ");
		query.append(" || '%' and TNR=ETNR and THEKEKZ='T') as Fakturierungsbetrag, ");
		query.append("(select count(*) from ").append(dataLibrary).append(".F_ARCPOSET where AUFNR like '%' || substr(BELNR, 3 , 4) || '%' ");
		query.append(" and TNR=ETNR and THEKEKZ='T') as Anzahl, ");		
		query.append(" round(cast((select count(*) from ").append(dataLibrary).append(".F_ARCPOSET where AUFNR like '%' || substr(BELNR, 3 , 4) || '%' ");
		query.append(" and TNR=ETNR and THEKEKZ='T') as real) ");
		query.append(" * cast((select DISTINCT FAKBET from ").append(dataLibrary).append(".F_ARCPOSET where AUFNR like '%' || substr(BELNR, 3 , 4) || '%' ");
		query.append(" and TNR=ETNR and THEKEKZ='T') as real), 2) as Summe ");
		query.append(" from ").append(dataLibrary).append(".E_HISTO where T4 like '%").append(customerNo).append("% %").append(customerGroup); 
		query.append("%' AND BART=20 AND (JJMMTT BETWEEN '").append(startDate).append("' AND '").append(tillDate).append("' ) ");

		if(pageSize != null) {
			query.append(" OFFSET ").append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");
		}

		List<ReportSelectionIntraTradeList> reportSelectionList = dbServiceRepository.getResultUsingQuery(ReportSelectionIntraTradeList.class, query.toString(), true);
		
		return reportSelectionList;
	}
	
	
	private String totalCountFromQuery(String dataLibrary, String startDate, String tillDate, String customerNo, String customerGroup) {
		
		String totalCount = "0";
		
		if(null == customerNo) {
			customerNo = "";
		}
		if(null == customerGroup) {
			customerGroup = "";
		}
		
		//get total count from both table 
		StringBuilder queryCount = new StringBuilder("SELECT COUNT(*) AS count FROM ( ");		 
		queryCount.append("Select BELNR, BART, ETNR, ES1, ES2, JJMMTT, TRIM(BOTH FROM T4) as Kunde from ").append(dataLibrary).append(".E_CPSDAT where T4 like '%").append(customerNo).append("% %").append(customerGroup);
		queryCount.append("%' AND BART=20 AND (JJMMTT BETWEEN '").append(startDate).append("' AND '").append(tillDate).append("' ) ");
		queryCount.append(" UNION ");		
		queryCount.append("Select BELNR, BART, ETNR, ES1, ES2, JJMMTT, TRIM(BOTH FROM T4) as Kunde from ").append(dataLibrary).append(".E_HISTO where T4 like '%").append(customerNo).append("% %").append(customerGroup); 
		queryCount.append("%' AND BART=20 AND (JJMMTT BETWEEN '").append(startDate).append("' AND '").append(tillDate).append("' ) ) ");
		
		totalCount = dbServiceRepository.getCountUsingQuery(queryCount.toString());
		
		return totalCount;		
	}
	
		
	private List<ReportSelectionIntraTradeDTO> setValueForIntraTradeStatsList(List<ReportSelectionIntraTradeList> reportSelectionIntraTradeList) {

		List<ReportSelectionIntraTradeDTO> reportSelectionsList = new ArrayList<>();

		for(ReportSelectionIntraTradeList reportSelection : reportSelectionIntraTradeList){
			ReportSelectionIntraTradeDTO reportSelectionsDto = new ReportSelectionIntraTradeDTO();

			reportSelectionsDto.setCustomerNumber(reportSelection.getCustomerNumber());			
			reportSelectionsDto.setPartNumber(reportSelection.getPartNumber());
			reportSelectionsDto.setPartName(reportSelection.getPartName());
			reportSelectionsDto.setInvoicingAmount(reportSelection.getInvoicingAmount()!= null ? reportSelection.getInvoicingAmount() : "0.00");
			reportSelectionsDto.setCustomsNumber(reportSelection.getCustomsNumber()!= null ? reportSelection.getCustomsNumber() : "");
			
			reportSelectionsDto.setDocumentNumber(reportSelection.getDocumentNumber());
			reportSelectionsDto.setBaNumber(String.valueOf(reportSelection.getBaNumber()));
			reportSelectionsDto.setNumberofPositions(String.valueOf(reportSelection.getNumberofPositions()));
			reportSelectionsDto.setEs1(reportSelection.getEs1()!= null ? reportSelection.getEs1() : "");
			reportSelectionsDto.setEs2(reportSelection.getEs2()!= null ? reportSelection.getEs2() : "");
			reportSelectionsDto.setStatsDate(reportSelection.getStatsDate());
			reportSelectionsDto.setTotal(reportSelection.getTotal()!=null ? reportSelection.getTotal() : "0.00");
			
			reportSelectionsList.add(reportSelectionsDto);
		}
		return reportSelectionsList;
	}
	
	
	/**
	 * This method is used for create report for Intra Trade Statistics (Intrahandelsstatistik - CSV) 
	 */
	@Override
	public ByteArrayInputStream generateReportIntraTradeStatistics(String schema, String dataLibrary, String fromDate, String toDate, 
			String customerNo, String customerGroup ) {

		log.info("Inside generateReportIntraTradeStatistics method of ReportSelectionsImpl");

		ByteArrayInputStream  byteArrayOutputStream = null;		
		String startDate = "";
		String tillDate = "";

		final String[] headers = { "Belegnummer", "Kunde", "TeileNummer", "Bezeichnung", "Fakturierungsbetrag", "Zollnummer" };

		final CSVFormat format = CSVFormat.RFC4180.withIgnoreSurroundingSpaces().
				withIgnoreEmptyLines().
				withTrim().
				withDelimiter(';').
				withQuote('"').
				//withRecordSeparator("\r\n").
				withQuoteMode(QuoteMode.ALL).
				withHeader(headers);

		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
				CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(out, "ISO-8859-1"), format);) {
			
			startDate = formatFromToDateforQuery(fromDate);
			tillDate = formatFromToDateforQuery(toDate);
			
			List<ReportSelectionIntraTradeList> reportSelectionIntraTradeList = generateQueryforIntraTradeList(schema, dataLibrary, startDate, tillDate, customerNo, 
					customerGroup, 0, null );	

			if (reportSelectionIntraTradeList != null && !reportSelectionIntraTradeList.isEmpty()) {

				for(ReportSelectionIntraTradeList reportSelection : reportSelectionIntraTradeList){

					List<String> data = Arrays.asList(										
							reportSelection.getDocumentNumber(),
							reportSelection.getCustomerNumber(),
							reportSelection.getPartNumber(),									
							reportSelection.getPartName(),
							replaceDotToComma(reportSelection.getInvoicingAmount()!= null ? reportSelection.getInvoicingAmount() : "0.00"),
							reportSelection.getCustomsNumber()
					);

					csvPrinter.printRecord(data);
				}

				// writing the underlying stream
				csvPrinter.flush();

				byteArrayOutputStream = new ByteArrayInputStream(out.toByteArray());
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Selections report for IntraTrade Statistics (Intrahandelsstatistik)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Selections report for IntraTrade Statistics (Intrahandelsstatistik)"), exception);
			throw exception;
		}

		return byteArrayOutputStream;
	}
	
	
	/**
	 * This method is used to get the Booking-Relevant - Stock Differance Lists (Buchungsrelevante Listen)
	 */
	@Override
	public List<ReportForBookingStockDifferencesDTO> getBookingRelevantStockDiffList(String schema, String dataLibrary, String allowedWarehouses,
			List<String> warehouseNos, String manufacturer, String fromDate, String toDate) {

		log.info("Inside getBookingRelevantStockDiffList method of ReportSelectionsImpl");

		List<ReportForBookingStockDifferencesDTO> reportSelectionsForStockDifferanceList = null;
		try {

			//validate the Warehouse Ids
			validateWarehouses(allowedWarehouses);
				//stock differences = Bestandsunterschiede
				List<ReportForBookingStockDifferencesList> reportSelectionsStockDifferanceList = generateQueryforStockDifferencesList(schema, dataLibrary, allowedWarehouses, 
					warehouseNos, manufacturer, fromDate, toDate);
			 
				 reportSelectionsForStockDifferanceList = setValueForStockDifferanceList(reportSelectionsStockDifferanceList);

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Booking relevant - Stock Differance list (Buchungsrelevante Listen)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Booking relevant - Stock Differance list (Buchungsrelevante Listen)"), exception);
			throw exception;
		}
		
		return reportSelectionsForStockDifferanceList;

	}
	
	
	/**
	 * This method is used to get the Booking-Relevant - Account Changes Lists (Buchungsrelevante Listen)
	 */
	@Override
	public List<ReportForBookingAccountChangesDTO> getBookingRelevantAccountChangeList(String schema, String dataLibrary, String allowedWarehouses,
			List<String> warehouseNos, String manufacturer, String fromDate, String toDate) {

		log.info("Inside getBookingRelevantAccountChangeList method of ReportSelectionsImpl");

		List<ReportForBookingAccountChangesDTO> reportSelectionsForAccountChangeList = null;
		try {

			//validate the Warehouse Ids
			validateWarehouses(allowedWarehouses);

			//Account Changes = Kontoanderungen
			List<ReportForBookingAccountChangesList> reportSelectionsAccountChangeList = generateQueryforAccountChangesList(schema, dataLibrary, allowedWarehouses, 
						warehouseNos, manufacturer, fromDate, toDate);
			
			reportSelectionsForAccountChangeList = setValueForAccountChangeList(reportSelectionsAccountChangeList);

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Booking related - Account Changes list (Buchungsrelevante Listen)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Booking related - Account Changes list (Buchungsrelevante Listen)"), exception);
			throw exception;
		}
		
		return reportSelectionsForAccountChangeList;

	}
	
	

	private List<ReportForBookingStockDifferencesList> generateQueryforStockDifferencesList(String schema, String dataLibrary, String allowedWarehouses, List<String> warehouseNos, 
			String manufacturer, String fromDate, String toDate){

		StringBuilder whereQuery = new StringBuilder("");
		
		if(!manufacturer.equalsIgnoreCase("Alle")){
			manufacturer = (manufacturer != null && manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING))? RestInputConstants.DCAG_STRING : manufacturer;
			whereQuery.append(" and HERST = '").append(manufacturer+"'");	
		}

		String warehouseNo = StringUtils.join(warehouseNos, ",");
		whereQuery.append(" and VFNR IN( ").append(warehouseNo).append(" )");
		
		if(fromDate!= null && !fromDate.isEmpty() && toDate!= null && !toDate.isEmpty()
				&& fromDate.length() == 10 && toDate.length() == 10 ){

			String startDate = fromDate.substring(8, 10)+fromDate.substring(3, 5)+fromDate.substring(0, 2);
			String tillDate =  toDate.substring(8, 10)+toDate.substring(3, 5)+toDate.substring(0, 2);

			whereQuery.append(" and (JJMMTT BETWEEN  '").append(startDate).append("' AND '").append(tillDate).append("' ) ");

		}else {
			log.info(" fromDate and toDate is required.");
			AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.PARAMETERS_INVALID_MSG_KEY, "", "fromDate, toDate"));
			log.error(messageService.getReadableMessage(ExceptionMessages.PARAMETERS_INVALID_MSG_KEY, "", "fromDate, toDate"));
			throw exception;
		}
	
		StringBuilder query = new StringBuilder("Select bart AS BA, HERST, VFNR as AP_Lager,herk as Herkunft, JJMMTT as Datum, BELNR as Belegnummer, ");
		query.append(" (select NAME from ").append(schema).append(".O_WRH where AP_WAREHOUS_ID=VFNR) as aX_Lager, KB||ETNR||ES1||ES2 as Teilenummer, "); 
		query.append(" (select BENEN from ").append(dataLibrary).append(".e_etstamm where lnr=vfnr AND trim(TNR)=trim(KB||ETNR||ES1||ES2) fetch first row only ) as Bezeichnung, ");
		query.append(" TA_EWV AS Teileart, mcode AS MarketingCode, markeV AS Marke,");
		query.append(" cast(ebest as real)/10 AS BestandVorher, ");
		query.append(" cast(substr(T1, 3 , 6) as real)/10 as BestandNachher,");
		query.append(" (cast(substr(T1, 3 , 6) as real)/10) - (CAST(EB_VZ|| EBEST as real) / 10) AS Zahldifferenz, ");
		query.append(" cast(DAKEWN as real)/10000 AS DAK, ");
		query.append(" ((cast(substr(T1, 3 , 6) as real)/10) - (CAST(EB_VZ|| EBEST as real) / 10)) * (cast(DAKEWV as real) /10000) AS SummeInEUR, ");
		query.append(" BUK_KONTOV As Buchungskonto, (select BUK_BEZEI from ").append(schema).append(".pei_bukont where BUK_KONTO=BUK_KONTOV AND ");
		query.append(" BUK_TMARKE=markeV AND  BUK_LNR=VFNR AND BUK_TEIART=TA_EWV ) AS Kontobezeichnung ");  
		query.append(" from ").append(dataLibrary).append(".E_HISTO where bart=67 ").append(whereQuery);

		
			query.append(" UNION ");

		query.append("Select bart AS BA, HERST, VFNR as AP_Lager,herk as Herkunft, JJMMTT as Datum, BELNR as Belegnummer, ");
		query.append(" (select NAME from ").append(schema).append(".O_WRH where AP_WAREHOUS_ID=VFNR) as aX_Lager, KB||ETNR||ES1||ES2 as Teilenummer, "); 
		query.append(" (select BENEN from ").append(dataLibrary).append(".e_etstamm where lnr=vfnr AND trim(TNR)=trim(KB||ETNR||ES1||ES2) fetch first row only ) as Bezeichnung, ");
		query.append(" TA_EWV AS Teileart, mcode AS MarketingCode, markeV AS Marke,");
		query.append(" cast(ebest as real)/10 AS BestandVorher, ");
		query.append(" cast(substr(T1, 3 , 6) as real)/10 as BestandNachher,");
		query.append(" (cast(substr(T1, 3 , 6) as real)/10) - (CAST(EB_VZ|| EBEST as real) / 10) AS Zahldifferenz, ");
		query.append(" cast(DAKEWN as real)/10000 AS DAK, ");
		query.append(" ((cast(substr(T1, 3 , 6) as real)/10) - (CAST(EB_VZ|| EBEST as real) / 10)) * (cast(DAKEWV as real) /10000) AS SummeInEUR, ");
		query.append(" BUK_KONTOV As Buchungskonto, (select BUK_BEZEI from ").append(schema).append(".pei_bukont where BUK_KONTO=BUK_KONTOV AND ");
		query.append(" BUK_TMARKE=markeV AND BUK_LNR=VFNR AND BUK_TEIART=TA_EWV ) AS Kontobezeichnung "); 
		query.append(" from ").append(dataLibrary).append(".E_CPSDAT where bart=67 ").append(whereQuery);
		
		
		query.append(" Order by Datum asc ");
		
		List<ReportForBookingStockDifferencesList> reportSelectionList = dbServiceRepository.getResultUsingQuery(ReportForBookingStockDifferencesList.class, query.toString(), true);

		return reportSelectionList;

	}
	
	
	private List<ReportForBookingAccountChangesList> generateQueryforAccountChangesList(String schema, String dataLibrary, String allowedWarehouses, List<String> warehouseNos, 
			String manufacturer, String fromDate, String toDate){

		StringBuilder whereQuery = new StringBuilder("");
		
		if(!manufacturer.equalsIgnoreCase("Alle")){
			manufacturer = (manufacturer != null && manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING))? RestInputConstants.DCAG_STRING : manufacturer;
			whereQuery.append(" and HERST = '").append(manufacturer+"'");	
		}

		String warehouseNo = StringUtils.join(warehouseNos, ",");
		whereQuery.append(" and VFNR IN( ").append(warehouseNo).append(" )");
		
		if(fromDate!= null && !fromDate.isEmpty() && toDate!= null && !toDate.isEmpty()
				&& fromDate.length() == 10 && toDate.length() == 10 ){

			String startDate = fromDate.substring(8, 10)+fromDate.substring(3, 5)+fromDate.substring(0, 2);
			String tillDate =  toDate.substring(8, 10)+toDate.substring(3, 5)+toDate.substring(0, 2);

			whereQuery.append(" and (JJMMTT BETWEEN  '").append(startDate).append("' AND '").append(tillDate).append("' ) ");

		}else {
			log.info(" fromDate and toDate is required.");
			AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.PARAMETERS_INVALID_MSG_KEY, "", "fromDate, toDate"));
			log.error(messageService.getReadableMessage(ExceptionMessages.PARAMETERS_INVALID_MSG_KEY, "", "fromDate, toDate"));
			throw exception;
		}
	
		StringBuilder query = new StringBuilder("Select bart AS BA, HERST, VFNR as AP_Lager,herk as Herkunft, JJMMTT as Datum, BELNR as Belegnummer, ");
		query.append(" (select NAME from ").append(schema).append(".O_WRH where AP_WAREHOUS_ID=VFNR) as aX_Lager, KB||ETNR||ES1||ES2 as Teilenummer, "); 
		query.append(" (select BENEN from ").append(dataLibrary).append(".e_etstamm where lnr=vfnr AND trim(TNR)=trim(KB||ETNR||ES1||ES2) fetch first row only ) as Bezeichnung, ");
		query.append(" TA_EWV As TeilartVorher, mcode as MarketingCodeVorher, markeV as MarkeVorher, BUK_KONTOV As BuchungskontoVorher,  ");
		query.append(" (select BUK_BEZEI from ").append(schema).append(".pei_bukont where BUK_KONTO=BUK_KONTOV AND BUK_TMARKE= markeV AND ");
		query.append("BUK_LNR=VFNR AND BUK_TEIART=TA_EWV ) As KontobezeichnungVorher, ");
		query.append(" TA_EWN As TeileartNachher, mcodev as MarketingCodeNachher, marken as MarkeNachher, BUK_KONTON As BuchungskontoNachher, ");
		query.append(" (select BUK_BEZEI from ").append(schema).append(".pei_bukont where BUK_KONTO=BUK_KONTON AND BUK_TMARKE= markeN AND ");
		query.append(" BUK_LNR=VFNR AND BUK_TEIART=TA_EWN) As KontobezeichnungNachher, ");
		query.append(" cast(ebest as real)/10 as Bestand, ");
		query.append(" cast(DAKEWN as real)/10000 as DAK,  ");
		query.append(" (cast(ebest as real)/10 * cast(DAKEWN as real)/10000) as SummeInEUR "); 
		query.append(" from ").append(dataLibrary).append(".E_HISTO where BUK_KONTOV != BUK_KONTON AND TA_EWN != TA_EWV  ").append(whereQuery);

		
			query.append(" UNION ");

		query.append("Select bart AS BA, HERST, VFNR as AP_Lager,herk as Herkunft, JJMMTT as Datum, BELNR as Belegnummer, ");
		query.append(" (select NAME from ").append(schema).append(".O_WRH where AP_WAREHOUS_ID=VFNR) as aX_Lager, KB||ETNR||ES1||ES2 as Teilenummer, "); 
		query.append(" (select BENEN from ").append(dataLibrary).append(".e_etstamm where lnr=vfnr AND trim(TNR)=trim(KB||ETNR||ES1||ES2) fetch first row only ) as Bezeichnung, ");
		query.append(" TA_EWV As TeilartVorher, mcode as MarketingCodeVorher, markeV as MarkeVorher, BUK_KONTOV As BuchungskontoVorher,  ");
		query.append(" (select BUK_BEZEI from ").append(schema).append(".pei_bukont where BUK_KONTO=BUK_KONTOV AND BUK_TMARKE= markeV AND ");
		query.append(" BUK_LNR=VFNR AND  BUK_TEIART=TA_EWV ) As KontobezeichnungVorher, ");
		query.append(" TA_EWN As TeileartNachher, mcodev as MarketingCodeNachher, marken as MarkeNachher, BUK_KONTON As BuchungskontoNachher, ");
		query.append(" (select BUK_BEZEI from ").append(schema).append(".pei_bukont where BUK_KONTO=BUK_KONTON AND BUK_TMARKE= markeN AND ");
		query.append(" BUK_LNR=VFNR AND BUK_TEIART=TA_EWN ) As KontobezeichnungNachher, ");
		query.append(" cast(ebest as real)/10 as Bestand, ");
		query.append(" cast(DAKEWN as real)/10000 as DAK,  ");
		query.append(" (cast(ebest as real)/10 * cast(DAKEWN as real)/10000) as SummeInEUR "); 
		query.append(" from ").append(dataLibrary).append(".E_CPSDAT where BUK_KONTOV != BUK_KONTON AND TA_EWN != TA_EWV  ").append(whereQuery);
		
		query.append(" Order by Datum asc ");
		
		List<ReportForBookingAccountChangesList> reportSelectionList = dbServiceRepository.getResultUsingQuery(ReportForBookingAccountChangesList.class, query.toString(), true);

		return reportSelectionList;

	}
	
	private List<ReportForBookingAccountChangesDTO> setValueForAccountChangeList(List<ReportForBookingAccountChangesList> reportSelectionsAccountChangeList) {
		List<ReportForBookingAccountChangesDTO> reportSelectionsList = new ArrayList<>();
		String century = "20";
		
		if (reportSelectionsAccountChangeList != null && !reportSelectionsAccountChangeList.isEmpty()) {

			for(ReportForBookingAccountChangesList reportSelection : reportSelectionsAccountChangeList){
				ReportForBookingAccountChangesDTO reportSelectionsDto = new ReportForBookingAccountChangesDTO();
				
				reportSelectionsDto.setWarehouseNo(String.valueOf(reportSelection.getAlphaPlusWarehouseId())); //AP-Lager
				reportSelectionsDto.setWarehouseName(reportSelection.getAlphaxWarehouseName()); //AX-Lager Name
				reportSelectionsDto.setPartNumber(reportSelection.getPartNumber()); //Teilenummer
				reportSelectionsDto.setPartName(reportSelection.getPartName()); //Bezeichnung
				reportSelectionsDto.setDocumentNumber(reportSelection.getDocumentNo()); //Belegnummer
				reportSelectionsDto.setDate(convertDateToString(century+reportSelection.getBookingDate())); //JJMMTT
				
				reportSelectionsDto.setBeforePostingAccount(reportSelection.getBeforePostingAccount()); //Buchungskonto vorher
				reportSelectionsDto.setAfterPostingAccount(reportSelection.getAfterPostingAccount()); //Buchungskonto nachher
				reportSelectionsDto.setSummeInEUR(reportSelection.getSummeInEUR());
				
				//Extra fields for detail view
				reportSelectionsDto.setOrigin(getOriginMapping(reportSelection.getOrigin())); //Herkunft
				reportSelectionsDto.setBeforePartType(reportSelection.getBeforePartType()); //Teileart vorher
				reportSelectionsDto.setBeforeMarketingCode(reportSelection.getBeforeMarketingCode()); //Marketing Code vorher
				reportSelectionsDto.setBeforePartBrand(reportSelection.getBeforePartBrand());  //Marke vorher
				reportSelectionsDto.setBeforeAccountDesignation(reportSelection.getBeforeAccountDesignation());// Kontobezeichnung vorher
				
				reportSelectionsDto.setAfterPartType(reportSelection.getAfterPartType()); //Teileart nachher
				reportSelectionsDto.setAfterMarketingCode(reportSelection.getAfterMarketingCode());  //Marketing Code nachher
				reportSelectionsDto.setAfterPartBrand(reportSelection.getAfterPartBrand());  //Marke nachher
				reportSelectionsDto.setAfterAccountDesignation(reportSelection.getAfterAccountDesignation()); //Kontobezeichnung nachher
				
				reportSelectionsDto.setBestand(reportSelection.getBestand());
				reportSelectionsDto.setAverageNetPrice(String.valueOf(reportSelection.getAverageNetPrice())); //DAK
				
				reportSelectionsDto.setManufacturer(reportSelection.getManufacturer());
				
				reportSelectionsList.add(reportSelectionsDto);
			}
		}
		return reportSelectionsList;
	}


	private List<ReportForBookingStockDifferencesDTO> setValueForStockDifferanceList(List<ReportForBookingStockDifferencesList> reportSelectionsStockDifferanceList) {
		
		List<ReportForBookingStockDifferencesDTO> reportSelectionsList = new ArrayList<>();
		String century = "20";
		
		if (reportSelectionsStockDifferanceList != null && !reportSelectionsStockDifferanceList.isEmpty()) {

			for(ReportForBookingStockDifferencesList reportSelection : reportSelectionsStockDifferanceList){
				ReportForBookingStockDifferencesDTO reportSelectionsDto = new ReportForBookingStockDifferencesDTO();

				
				reportSelectionsDto.setWarehouseNo(String.valueOf(reportSelection.getAlphaPlusWarehouseId())); //AP-Lager
				reportSelectionsDto.setWarehouseName(reportSelection.getAlphaxWarehouseName()); //AX-Lager Name
				reportSelectionsDto.setPartNumber(reportSelection.getPartNumber()); //Teilenummer
				reportSelectionsDto.setPartName(reportSelection.getPartName()); //Bezeichnung
				reportSelectionsDto.setDocumentNumber(reportSelection.getDocumentNo()); //Belegnummer
				reportSelectionsDto.setDate(convertDateToString(century+reportSelection.getBookingDate())); //JJMMTT
				reportSelectionsDto.setBeforeStock(reportSelection.getBeforeStock()); //Bestand vorher
				reportSelectionsDto.setAfterStock(reportSelection.getAfterStock()); //Bestand nachher
				reportSelectionsDto.setSummeInEUR(String.valueOf(reportSelection.getSummeInEUR()));
				
				//Extra fields for detail view
				reportSelectionsDto.setOrigin(getOriginMapping(reportSelection.getOrigin())); //Herkunft
				reportSelectionsDto.setPartType(reportSelection.getPartType()); //Teileart
				reportSelectionsDto.setMarketingCode(reportSelection.getMarketingCode()); 
				reportSelectionsDto.setPartBrand(reportSelection.getPartBrand());  //MarkeV
				
				reportSelectionsDto.setPostingAccount(reportSelection.getPostingAccount()); //Buchungskonto
				reportSelectionsDto.setAccountDesignation(reportSelection.getAccountDesignation()); //Kontobezeichnung
				reportSelectionsDto.setCountDifference(reportSelection.getCountDifference()); //Zähldifferenz
				reportSelectionsDto.setAverageNetPrice(String.valueOf(reportSelection.getAverageNetPrice()));
				reportSelectionsDto.setManufacturer(reportSelection.getManufacturer());
				
				
				reportSelectionsList.add(reportSelectionsDto);
			}
		}
		return reportSelectionsList;
	}
	
	/**
	 * This method is used for create report based on Booking relevant Stock Differences List (Buchungsrelevante Listen) 
	 */
	@Override
	public ByteArrayInputStream generateReportForBookingRelevant_StockDifferences(String schema, String dataLibrary, String allowedWarehouses, 
			List<String> warehouseNos, String manufacturer, String fromDate, String toDate) {

		log.info("Inside generateReportForBookingRelevant_StockDifferences method of ReportSelectionsImpl");
		
		ByteArrayInputStream  byteArrayOutputStream = null;
		String century = "20";

		final String[] headers = { "BA", "AP-Lager Nr.",	"AX-Lager", "Teilenummer",	"Bezeichnung", "Belegnummer", "Datum", 
				 "Bestand vorher", "Bestand nachher", "Summe in EUR", "Herkunft", "Teileart", "Marketing Code", "Marke", "Buchungskonto",
				"Kontobezeichnung", "Zähldifferenz","DAK"};

		final CSVFormat format = CSVFormat.RFC4180.withIgnoreSurroundingSpaces().
				withIgnoreEmptyLines().
				withTrim().
				withDelimiter(';').
				withQuote('"').
				//withRecordSeparator("\r\n").
				withQuoteMode(QuoteMode.ALL).
				withHeader(headers);

		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
				CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(out, "ISO-8859-1"), format);) {

			List<ReportForBookingStockDifferencesList> reportSelectionsStockDifferanceList = generateQueryforStockDifferencesList(schema, dataLibrary, allowedWarehouses, 
					warehouseNos, manufacturer, fromDate, toDate);

			if (reportSelectionsStockDifferanceList != null && !reportSelectionsStockDifferanceList.isEmpty()) {

				for(ReportForBookingStockDifferencesList reportSelection : reportSelectionsStockDifferanceList){

					List<String> data = Arrays.asList(	
							reportSelection.getBusinessCase(),
							StringUtils.leftPad(String.valueOf(reportSelection.getAlphaPlusWarehouseId()),2,"0"),
							reportSelection.getAlphaxWarehouseName(),
									reportSelection.getPartNumber(),									
									reportSelection.getPartName(),	
									reportSelection.getDocumentNo(),
									convertDateToString(century+reportSelection.getBookingDate()),
									replaceDotToComma(reportSelection.getBeforeStock()),
									replaceDotToComma(reportSelection.getAfterStock()),
									replaceDotToComma(String.valueOf(reportSelection.getSummeInEUR())),
									getOriginMapping(reportSelection.getOrigin()),
									reportSelection.getPartType(),
									reportSelection.getMarketingCode(),
									reportSelection.getPartBrand(),
									reportSelection.getPostingAccount(),
									reportSelection.getAccountDesignation(),
									replaceDotToComma(reportSelection.getCountDifference()),
									replaceDotToComma(String.valueOf(reportSelection.getAverageNetPrice()))
									
							);

					csvPrinter.printRecord(data);
				}

				// writing the underlying stream
				csvPrinter.flush();

				byteArrayOutputStream = new ByteArrayInputStream(out.toByteArray());
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Report based on Booking relevant - Stock Differences"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Report based on Booking relevant - Stock Differences"), exception);
			throw exception;
		}

		return byteArrayOutputStream;
	}

	
	/**
	 * This method is used for create report based on Booking relevant Account Change List (Kontoänderungen Listen) 
	 */
	@Override
	public ByteArrayInputStream generateReportForBookingRelevant_AccountChange(String schema, String dataLibrary, String allowedWarehouses, 
			List<String> warehouseNos, String manufacturer, String fromDate, String toDate) {

		log.info("Inside generateReportForBookingRelevant_AccountChange method of ReportSelectionsImpl");
		
		ByteArrayInputStream  byteArrayOutputStream = null;
		String century = "20";

		final String[] headers = { "BA", "AP-Lager Nr.",	"AX-Lager", "Teilenummer",	"Bezeichnung", "Belegnummer", "Datum", 
				 "Buchungskonto vorher", "Buchungskonto nachher", "Summe in EUR", "Herkunft", "Teilart vorher","Marketing Code vorher","Marke vorher","Kontobezeichnung vorher",
				 "Teileart nachher", "Marketing Code nachher", "Marke nachher", "Kontobezeichnung nachher", "Bestand", "DAK"};

		final CSVFormat format = CSVFormat.RFC4180.withIgnoreSurroundingSpaces().
				withIgnoreEmptyLines().
				withTrim().
				withDelimiter(';').
				withQuote('"').
				//withRecordSeparator("\r\n").
				withQuoteMode(QuoteMode.ALL).
				withHeader(headers);

		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
				CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(out, "ISO-8859-1"), format);) {

			List<ReportForBookingAccountChangesList> reportSelectionsAccountChange = generateQueryforAccountChangesList(schema, dataLibrary, allowedWarehouses, 
					warehouseNos, manufacturer, fromDate, toDate);

			if (reportSelectionsAccountChange != null && !reportSelectionsAccountChange.isEmpty()) {

				for(ReportForBookingAccountChangesList reportSelection : reportSelectionsAccountChange){

					List<String> data = Arrays.asList(	
							reportSelection.getBusinessCase(),
							StringUtils.leftPad(String.valueOf(reportSelection.getAlphaPlusWarehouseId()),2,"0"),
							reportSelection.getAlphaxWarehouseName(),
									reportSelection.getPartNumber(),									
									reportSelection.getPartName(),	
									reportSelection.getDocumentNo(),
									convertDateToString(century+reportSelection.getBookingDate()),
									reportSelection.getBeforePostingAccount(), //Buchungskonto vorher
									reportSelection.getAfterPostingAccount(), //Buchungskonto nachher
									replaceDotToComma(reportSelection.getSummeInEUR()),
									
									getOriginMapping(reportSelection.getOrigin()),
									reportSelection.getBeforePartType(),
									reportSelection.getBeforeMarketingCode(),
									reportSelection.getBeforePartBrand(),
									reportSelection.getBeforeAccountDesignation(),
									
									reportSelection.getAfterPartType(),
									reportSelection.getAfterMarketingCode(),
									reportSelection.getAfterPartBrand(),
									reportSelection.getAfterAccountDesignation(),
									replaceDotToComma(reportSelection.getBestand()),
									replaceDotToComma(String.valueOf(reportSelection.getAverageNetPrice()))
									
							);

					csvPrinter.printRecord(data);
				}

				// writing the underlying stream
				csvPrinter.flush();

				byteArrayOutputStream = new ByteArrayInputStream(out.toByteArray());
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Report based on Booking relevant - Account Change"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY,
					"Report based on Booking relevant - Account Change"), exception);
			throw exception;
		}

		return byteArrayOutputStream;
	}
	
	public String getOriginMapping(String origin){
		
		if(origin!=null && !origin.isEmpty()){
			origin = origin.trim();
		}else{
			origin = "";
		}
		
		String mappingValue;
		switch (origin) {
		case "BSN5BA":
			mappingValue = "Manueller Vorgang";
			break;
		case "ETP00101":
			mappingValue = "Verwaltung Info Stammdaten";
			break;
		case "ETP00109":
			mappingValue = "Verwaltung Info Stammdaten";
			break;
		case "CPS401":
			mappingValue = "Erfasste Inventurmengen";
			break;
		default:
			mappingValue = "Unbekannt";
			break;
		}
		
		return mappingValue;
	}

	
	/**
	 * This is method is used to get List of parts Label for Printing.
	 */
	@Override
	public GlobalSearch getPartsLabelforPrinting(String dataLibrary, String pageSize, String pageNumber, String warehouseNo, String manufacturer, 
			boolean partNumberRangeFlag, boolean allParts, boolean allStorageLocations, String fromPartNumber, String toPartNumber, 
			String fromStorageLocation, String toStorageLocation, String companyId) {

		log.info("Inside getPartsLabelforPrinting method of ReportSelectionsImpl");

		List<PartPrintingLabelDTO> partPrintingLabelsDTOList = new ArrayList<>();
		GlobalSearch globalSearchList = new GlobalSearch();
		Map<String, Integer> surchargeValueMap =  new HashMap<>();
		Double purchasePrice = null;
		Double purchasePriceWithSurcharge = null;

		try {
			if(pageSize==null || pageNumber==null || pageSize.isEmpty() || pageNumber.isEmpty()){
				pageSize = "200";
				pageNumber= "1";
			}

			int totalRecords = Integer.parseInt(pageSize);
			int nextRows = totalRecords * (Integer.parseInt(pageNumber) - 1);

			log.info("PageSize  : {}  OFFSET Size (Skip Rows):  {} Page Number  : {} ", pageSize, nextRows, pageNumber);
			
			//validate the Warehouse Id
			validateWarehouses(warehouseNo);
			
			StringBuilder whereClause  = createWhereClause(dataLibrary, warehouseNo, manufacturer, partNumberRangeFlag, allParts, allStorageLocations, 
					fromPartNumber, toPartNumber, fromStorageLocation, toStorageLocation );
			
			manufacturer = manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING: manufacturer;
			StringBuilder whereHerstAndLNR = new StringBuilder("Herst='").append(manufacturer).append("' AND LNR = ").append(warehouseNo);
			
			StringBuilder query = new StringBuilder("Select htnr AS TNR, ort AS LOPA, ");
			query.append("(select BENEN from ").append(dataLibrary).append(".e_etstamm where tnr=htnr AND ").append(whereHerstAndLNR).append(") , ");
			query.append("(select TMARKE from ").append(dataLibrary).append(".e_etstamm where tnr=htnr AND ").append(whereHerstAndLNR).append(") , ");
			query.append("(select TNRD from ").append(dataLibrary).append(".e_etstamm where tnr=htnr AND ").append(whereHerstAndLNR).append(") , ");
			query.append("(select EPR from ").append(dataLibrary).append(".e_etstamm where tnr=htnr AND ").append(whereHerstAndLNR).append(") , ");
			query.append("(select RG from ").append(dataLibrary).append(".e_etstamm where tnr=htnr AND ").append(whereHerstAndLNR).append("),  ");
			query.append("(select COUNT(*) from ").append(" ( Select TNR AS htnr, LOPA as ort from ").append(dataLibrary).append(".e_lago where PRIO>1 AND ").append(whereClause);
			query.append(" AND TNR in ( select TNR from ").append(dataLibrary).append(".e_etstamm where ").append(whereHerstAndLNR).append(")");
			query.append(" UNION ");
			query.append(" Select TNR AS htnr, LOPA as ort from ").append(dataLibrary).append(".e_etstamm where ").append(whereClause).append(")) As ROWNUMER ");
			query.append(" from ");
			query.append(" ( Select TNR AS htnr, LOPA as ort from ").append(dataLibrary).append(".e_lago where PRIO>1 AND ").append(whereClause);
			query.append(" AND TNR in ( select TNR from ").append(dataLibrary).append(".e_etstamm where ").append(whereHerstAndLNR).append(")");
			query.append(" UNION ");
			query.append(" Select TNR AS htnr, LOPA as ort from ").append(dataLibrary).append(".e_etstamm where ").append(whereClause).append(") ");
			query.append("order by htnr ASC  OFFSET ").append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");

			List<PartDetails> partDetailsList = new ArrayList<>();
			partDetailsList = dbServiceRepository.getResultUsingQuery(PartDetails.class, query.toString(), true);

			//if the list is not empty
			if (partDetailsList != null && !partDetailsList.isEmpty()) {
				
				List<PartDetails> priceLimit = getPricelimitFromDB(dataLibrary);
				
				for(PartDetails parts : partDetailsList) {
					
					purchasePrice = parts.getPurchasePrice() != null ? parts.getPurchasePrice().setScale(2, RoundingMode.HALF_UP).doubleValue() : 0.00;
					
					purchasePriceWithSurcharge = calculatePriceWithSurcharge(dataLibrary, purchasePrice, parts.getDiscountGroup(), parts.getOemBrand(), priceLimit, surchargeValueMap, companyId);
					
					PartPrintingLabelDTO labelDTO = new PartPrintingLabelDTO();

					labelDTO.setPrintingFormatPartNumber(parts.getPrintingFormatPartNumber());
					labelDTO.setName(parts.getName());					
					labelDTO.setPurchasePriceWithSurcharge(decimalformat_fixtwodigit.format(purchasePriceWithSurcharge));
					labelDTO.setStorageLocation(parts.getStorageLocation()!=null ? parts.getStorageLocation() : "");
					
					labelDTO.setPartNumber(parts.getPartNumber());
					labelDTO.setOem(manufacturer.equalsIgnoreCase(RestInputConstants.DCAG_STRING)? RestInputConstants.DAG_STRING: manufacturer);
					labelDTO.setWarehouse(warehouseNo);
					
					partPrintingLabelsDTOList.add(labelDTO);					
				}

				globalSearchList.setSearchDetailsList(partPrintingLabelsDTOList);
				globalSearchList.setTotalPages(Integer.toString(partDetailsList.get(0).getTotalCount()));
				globalSearchList.setTotalRecordCnt(Integer.toString(partDetailsList.get(0).getTotalCount()));
			}
			else {
				globalSearchList.setSearchDetailsList(partPrintingLabelsDTOList);
				globalSearchList.setTotalPages(Integer.toString(0));
				globalSearchList.setTotalRecordCnt(Integer.toString(0));
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Printing Part labels"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Printing Part labels"), exception);
			throw exception;
		}

		return globalSearchList;
	}
			
			
	private StringBuilder createWhereClause(String dataLibrary, String warehouseNo, String manufacturer, 
			boolean partNumberRangeFlag, boolean allParts, boolean allStorageLocations, String fromPartNumber, String toPartNumber, 
			String fromStorageLocation, String toStorageLocation) {

		manufacturer = manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING: manufacturer;
		StringBuilder query = new StringBuilder(" HERST= '").append(manufacturer).append("' AND LNR = ").append(warehouseNo);


		if(partNumberRangeFlag){
			if(!allParts){
				if(isNullOrEmpty(fromPartNumber) && isNullOrEmpty(toPartNumber)){
					query.append(" AND (tnr between '").append(fromPartNumber).append("' AND '").append(toPartNumber).append("' ) ");
				}
				else if(isNullOrEmpty(fromPartNumber)){
					query.append(" AND (tnr between '").append(fromPartNumber).append("' AND '").append(fromPartNumber).append("' ) ");
				}
				else if(isNullOrEmpty(toPartNumber)){
					query.append(" AND (tnr between '").append(toPartNumber).append("' AND '").append(toPartNumber).append("' ) ");
				}
				else{
					log.info("Both part selection is empty");
				}
			}
		}
		else {
			if(!allStorageLocations){
				if(isNullOrEmpty(fromStorageLocation) && isNullOrEmpty(toStorageLocation)){

					query.append(" AND (LOPA between '").append(fromStorageLocation).append("' AND '").append(toStorageLocation).append("' ) ");
				}else if(isNullOrEmpty(fromStorageLocation)){

					query.append(" AND (LOPA between '").append(fromStorageLocation).append("' AND '").append(fromStorageLocation).append("' ) ");
				}else if(isNullOrEmpty(toStorageLocation)){

					query.append(" AND (LOPA between '").append(toStorageLocation).append("' AND '").append(toStorageLocation).append("' ) ");
				}else{
					log.info("Both LOPA selection is empty");
				}
			}
		}

		return query;
	}
	
	
	boolean isNullOrEmpty(String value) {

		boolean flag = false;
		if (value != null && !value.trim().isEmpty()) {
			flag = true;
		}
		return flag;
	}
	
	
	private Double calculatePriceWithSurcharge(String dataLibrary, Double purchasePrice, String discountGroup, String oemBrand, 
			List<PartDetails> priceLimit, Map<String, Integer> surchargeValueMap, String companyId) {

		log.info("Inside calculatePriceWithSurcharge method of ReportSelectionsImpl");
		Integer surchargePosition = 0;
		Integer surchargePercentage = 0;
		
		Double finalPriceWithsurchange = purchasePrice;

		try {
			//if the list is not empty
			if(priceLimit != null && !priceLimit.isEmpty()) {

				if(priceLimit.get(0).getPriceLimit() != null ) {

					Double priceLimitValue = Double.parseDouble(priceLimit.get(0).getPriceLimit());

					Integer newDiscountGroup = Integer.parseInt((discountGroup != null) && !(discountGroup.trim().isEmpty()) ? discountGroup : "0");

					if(purchasePrice > priceLimitValue) {
						if(newDiscountGroup == 0){
							surchargePosition = ((100 - 1) * 9) + 7;
						}else {
							surchargePosition = ((newDiscountGroup - 1) * 9) + 7;
						}						
					}
					else if(purchasePrice <= priceLimitValue) {
						if(newDiscountGroup == 0){
							surchargePosition = ((100 - 1) * 9) + 4;
						}
						else {
							surchargePosition = ((newDiscountGroup - 1) * 9) + 4;
						}
					}
					//check the Map contains the value of Surcharge_percentage
					if(surchargeValueMap != null && surchargeValueMap.containsKey(oemBrand)) {

						surchargePercentage = surchargeValueMap.get(oemBrand);
						if(surchargePercentage != null) {
							finalPriceWithsurchange = purchasePrice + ( purchasePrice * surchargePercentage / 1000 );
						}
					}
					//if not present then execute query
					else {
						StringBuilder surchargeQuery = new StringBuilder("SELECT substr(RAB_TBL, ").append(surchargePosition).append(", 3) as surcharge_percentage from ");
						surchargeQuery.append(dataLibrary).append(".F_KUNRAB where KDNR = '99999900' and FZART = 'ET' and Marke = '").append(oemBrand).append("' ");
						surchargeQuery.append(" AND FIRMA = ").append(companyId);

						List<PartDetails> surchargePercentageList = dbServiceRepository.getResultUsingQuery(PartDetails.class, surchargeQuery.toString(), true);

						//if the list is not empty
						if(surchargePercentageList != null && !surchargePercentageList.isEmpty()) {

							if(surchargePercentageList.get(0).getSurchargePercentage() != null && 
									!surchargePercentageList.get(0).getSurchargePercentage().trim().isEmpty() ) {
								surchargePercentage = Integer.parseInt(surchargePercentageList.get(0).getSurchargePercentage());

								finalPriceWithsurchange = purchasePrice + ( purchasePrice * surchargePercentage / 1000 );	
								surchargeValueMap.put(oemBrand, surchargePercentage);
							}							
						}
						//if there is no data add null to specific brand
						else {
							surchargeValueMap.put(oemBrand, null);
						}
					}
				}
			}
		}
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Surcharge Details"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Surcharge Details"), exception);
			throw exception;
		}
		
		return finalPriceWithsurchange;
	}
	
	
	private List<PartDetails> getPricelimitFromDB(String dataLibrary) {
		
		log.info("Inside getPricelimitFromDB method of ReportSelectionsImpl");
		List<PartDetails> priceLimit = null;
		
		try {
			StringBuilder priceLimitQuery = new StringBuilder("SELECT CAST((CAST(substr(DATAFLD, 1, 7) AS REAL)/100) as DEC(7,2)) AS price_limit from ");
			priceLimitQuery.append(dataLibrary).append(".REFERENZ where keyfld like '%2219%' ");

			priceLimit = dbServiceRepository.getResultUsingQuery(PartDetails.class, priceLimitQuery.toString(), true);
		}
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "REFERENZ Details"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "REFERENZ Details"), exception);
			throw exception;
		}
		
		return priceLimit;
	}
	
	
	
	/**
	 * This is method is used to get List of parts Label for Printing CSV.
	 */
	@Override
	public ByteArrayInputStream getPartsLabelforPrintingCSV(String dataLibrary, String schema, String warehouseNo, String manufacturer, 
			boolean partNumberRangeFlag, boolean allParts, boolean allStorageLocations, String fromPartNumber, String toPartNumber, 
			String fromStorageLocation, String toStorageLocation) {

		log.info("Inside getPartsLabelforPrintingCSV method of ReportSelectionsImpl");

		ByteArrayInputStream  byteArrayOutputStream = null;
		String tmv = "";
		Double gskDouble = 0.0;

		final String[] headers = { "PrintingFormatPartNumber", "Name", "Warehouse", "StorageLocation","storageIndikator","currentStock", "data", "TMV", "GSK", "REACH", "DRTKZ"};

		final CSVFormat format = CSVFormat.RFC4180.withIgnoreSurroundingSpaces().
				withIgnoreEmptyLines().
				withTrim().
				withDelimiter(';').
				withQuote('"').
				//withRecordSeparator("\r\n").
				withQuoteMode(QuoteMode.ALL).
				withHeader(headers);

		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
				CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(out, "ISO-8859-1"), format);) {

			//validate the Warehouse Id
			validateWarehouses(warehouseNo);

			StringBuilder whereClause  = createWhereClause(dataLibrary, warehouseNo, manufacturer, partNumberRangeFlag, allParts, allStorageLocations, 
					fromPartNumber, toPartNumber, fromStorageLocation, toStorageLocation );
			
			manufacturer = manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING: manufacturer;
			StringBuilder whereHerstAndLNR = new StringBuilder("Herst='").append(manufacturer).append("' AND LNR = ").append(warehouseNo);
			
			StringBuilder query = new StringBuilder("Select htnr AS TNR, ort AS LOPA, ");
			//query.append("(select TNRS from ").append(dataLibrary).append(".e_etstamm where tnr=htnr AND ").append(whereHerstAndLNR).append(") , ");
			query.append("(select BENEN from ").append(dataLibrary).append(".e_etstamm where tnr=htnr AND ").append(whereHerstAndLNR).append(") , ");
			query.append("(select EPR from ").append(dataLibrary).append(".e_etstamm where tnr=htnr AND ").append(whereHerstAndLNR).append(") , ");
			query.append("(select SA from ").append(dataLibrary).append(".e_etstamm where tnr=htnr AND ").append(whereHerstAndLNR).append(") , ");
			query.append("(select AKTBES from ").append(dataLibrary).append(".e_etstamm where tnr=htnr AND ").append(whereHerstAndLNR).append("), ");
			query.append("(select TNRD from ").append(dataLibrary).append(".e_etstamm where tnr=htnr AND ").append(whereHerstAndLNR).append("),  ");
			//query.append("(select RG from ").append(dataLibrary).append(".e_etstamm where tnr=htnr AND ").append(whereHerstAndLNR).append(") , ");
			//query.append("(select TMARKE from ").append(dataLibrary).append(".e_etstamm where tnr=htnr AND ").append(whereHerstAndLNR).append(") , ");
			query.append("(Select TLE_LAZ from ").append(schema).append(".ETK_TLEKAT where TLE_TNR = htnr and TLE_DTGULV <= current date order by TLE_DTGULV desc LIMIT 1) AS TLE_LAZ, ");
			query.append("(Select TLE_IMCO from ").append(schema).append(".ETK_TLEKAT where TLE_TNR = htnr and TLE_DTGULV <= current date order by TLE_DTGULV desc LIMIT 1) AS TLE_IMCO, ");
			query.append("(select GFKZ from ").append(dataLibrary).append(".e_etstamm where tnr=htnr AND ").append(whereHerstAndLNR).append("), ");
			query.append("(select DRTKZ from ").append(dataLibrary).append(".e_etstamm where tnr=htnr AND ").append(whereHerstAndLNR).append(")  ");
			query.append(" from ");
			query.append(" ( Select TNR AS htnr, LOPA as ort from ").append(dataLibrary).append(".e_lago where PRIO>1 AND ").append(whereClause);
			query.append(" AND TNR in ( select TNR from ").append(dataLibrary).append(".e_etstamm where ").append(whereHerstAndLNR).append(")");
			query.append(" UNION ");
			query.append(" Select TNR AS htnr, LOPA as ort from ").append(dataLibrary).append(".e_etstamm where ").append(whereClause).append(") ");
			query.append("order by htnr ASC  ");

			List<PartDetails> partDetailsList = dbServiceRepository.getResultUsingQuery(PartDetails.class, query.toString(), true);

			//if the list is not empty
			if (partDetailsList != null && !partDetailsList.isEmpty()) {

				for(PartDetails parts : partDetailsList) {
					
					tmv = (parts.getStorageValue()!=null && !parts.getStorageValue().isEmpty()) ? parts.getStorageValue() : "0";  //TMV
					gskDouble = (parts.getImcoNumber()!=null && !parts.getImcoNumber().isEmpty()) ? Double.parseDouble(parts.getImcoNumber()) : 0.0;  //GSK
					
					parts.setOem(manufacturer);
					List<String> data = Arrays.asList(
								parts.getPrintingFormatPartNumber()!=null ? parts.getPrintingFormatPartNumber(): "", // TNRD									
								parts.getName(),	 //BENEN
								StringUtils.leftPad(warehouseNo, 2, "0"), //LNR
								parts.getStorageLocation()!=null ? parts.getStorageLocation() : "", //LOPA
								parts.getStorageIndikator()!=null ? String.valueOf(parts.getStorageIndikator()) :"", //SA	
								parts.getCurrentStock()!=null ? String.valueOf(parts.getCurrentStock()) :"0.00", //AKTBES	
								createJsonString(parts),
								(Integer.parseInt(tmv) > 0) ? "X" : "", //TMV
								(gskDouble > 0) ? gskDouble.toString().substring(0, gskDouble.toString().indexOf('.')) : "", //GSK
								(parts.getHazardousMaterialMark()!=null && parts.getHazardousMaterialMark().equalsIgnoreCase("REAC")) ? "X" : "", //REACH
								(parts.getTheftMark()!=null && !parts.getTheftMark().isEmpty())  ? "X" : ""  //DRTKZ
						);

					csvPrinter.printRecord(data);
				}
				// writing the underlying stream
				csvPrinter.flush();

				byteArrayOutputStream = new ByteArrayInputStream(out.toByteArray());
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "CSV Part labels"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "CSV Part labels"), exception);
			throw exception;
		}

		return byteArrayOutputStream;
	}
	
	
	private String createJsonString(PartDetails parts) {
		
		Map<String, String> json_item = new LinkedHashMap<>();
		
		json_item.put(RestInputConstants.DATA_PRINTED_PARTNO, parts.getPrintingFormatPartNumber()!=null ? parts.getPrintingFormatPartNumber(): "");
		json_item.put(RestInputConstants.DATA_PARTNAME, parts.getName()) ;
		json_item.put(RestInputConstants.DATA_STORAGE_LOC, parts.getStorageLocation()!=null ? parts.getStorageLocation() : "");
		json_item.put(RestInputConstants.DATA_OEM, parts.getOem().equalsIgnoreCase(RestInputConstants.DCAG_STRING)? RestInputConstants.DAG_STRING : parts.getOem());
		
		String jsonStr = json_item.entrySet().stream()
	            .map(x -> "\"" + x.getKey() + "\":\"" + x.getValue() + "\"")
	            .collect(Collectors.joining(",", "{", "}"));
		
	    return jsonStr;		
	}
	
}