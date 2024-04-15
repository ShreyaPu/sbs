package com.alphax.service.mb.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.alphax.common.constants.AlphaXCommonUtils;
import com.alphax.common.constants.Program_Commands_Constants;
import com.alphax.common.constants.RestInputConstants;
import com.alphax.common.exception.AlphaXException;
import com.alphax.common.exception.ExceptionMessages;
import com.alphax.common.rest.message.service.MessageService;
import com.alphax.model.mb.AdminWarehouse;
import com.alphax.model.mb.MovementData;
import com.alphax.model.mb.PartBADetails;
import com.alphax.model.mb.PartDetails;
import com.alphax.model.mb.PartGlobalSearch;
import com.alphax.model.mb.PartLabel;
import com.alphax.model.mb.StorageLocation;
import com.alphax.repository.CobolServiceRepository;
import com.alphax.repository.DBServiceRepository;
import com.alphax.repository.StubServiceRepository;
import com.alphax.service.mb.BusinessCases;
import com.alphax.service.mb.PartsService;
import com.alphax.util.DecryptTokenUtils;
import com.alphax.vo.mb.BAWrapperDTO;
import com.alphax.vo.mb.DropdownObject;
import com.alphax.vo.mb.GlobalSearch;
import com.alphax.vo.mb.LagerDetailsDTO;
import com.alphax.vo.mb.LagerPartDetailsDTO;
import com.alphax.vo.mb.MasterDataBA_DTO;
import com.alphax.vo.mb.MonthlyDecimalValue;
import com.alphax.vo.mb.MonthlyIntValue;
import com.alphax.vo.mb.MovementDataDTO;
import com.alphax.vo.mb.PartDetailsDTO;
import com.alphax.vo.mb.PartLabelDTO;
import com.alphax.vo.mb.PartPrintingLabelDTO;
import com.alphax.vo.mb.PartsTreeViewDTO;
import com.alphax.vo.mb.SearchPartsDTO;
import com.alphax.vo.mb.StorageLocationDTO;
import com.alphax.vo.mb.partBADetailsDTO;
import com.ibm.as400.access.ProgramParameter;

import lombok.extern.slf4j.Slf4j;

/**
 * @author A106744104
 *
 */
@Service
@Slf4j
public class PartsServiceImpl extends BaseService implements PartsService {

	@Autowired
	DBServiceRepository dbServiceRepository;

	@Autowired
	private MessageService messageService;

	@Autowired
	StubServiceRepository stubServiceRepository;
	
	@Autowired
	CobolServiceRepository cobolServiceRepository;
	
	@Autowired
	BusinessCases businessCasesImpl;
	
	@Autowired
	DecryptTokenUtils decryptUtil;
	
	@Autowired
	AlphaXCommonUtils commonUtils;
	
	DecimalFormat decimalformat_twodigit = new DecimalFormat("##.##", new DecimalFormatSymbols(Locale.ENGLISH));
	DecimalFormat decimalformat_fourdigit = new DecimalFormat("##.####", new DecimalFormatSymbols(Locale.ENGLISH));
	DecimalFormat decimalformat_fixtwodigit = new DecimalFormat("#0.00", new DecimalFormatSymbols(Locale.ENGLISH));
	DecimalFormat decimalformat_fixFourdigit = new DecimalFormat("#0.0000", new DecimalFormatSymbols(Locale.ENGLISH));

	@Override
	public GlobalSearch getPartsList(String dataLibrary, String schema, String companyId,String agencyId, String oem, String searchString, 
			String pageSize, String pageNo, String warehouseId) {
		log.info("Inside getPartsList method of PartsServiceImpl");

		List<PartGlobalSearch> partsList = new ArrayList<>();
		GlobalSearch globalSearchList = new GlobalSearch();
		
		try {

			validateCompany(companyId);
			validateAgency(agencyId);
			
			String warehouseIds = decryptUtil.getAllowedApWarehouse();
			
			if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
				warehouseIds = decryptUtil.getCompanyApWarehouse();
			}
			
			if(warehouseId!=null && !warehouseId.trim().isEmpty()){
				warehouseIds = warehouseId;
			}
			log.info("User's warehouse Ids ID xx:  {} ", warehouseIds);
			//validate the Warehouse 
			validateWarehouses(warehouseIds);

			if(pageSize==null || pageNo==null || pageSize.isEmpty() || pageNo.isEmpty()){
				pageSize = RestInputConstants.DEFAULT_PAGE_SIZE;
				pageNo = RestInputConstants.DEFAULT_PAGE_NO;
			}

			int totalRecords = Integer.parseInt(pageSize);
			int nextRows = totalRecords * (Integer.parseInt(pageNo) - 1);

			//validate the page size
			validatePageSize(totalRecords);
			
			log.info("PageSize  : {}  OFFSET Size (Skip Rows):  {} Page Number  : {} ", pageSize, nextRows, pageNo);
			
			if((searchString.startsWith("A") || searchString.startsWith("a")) && searchString.length() >= 11) {
				searchString = searchString.substring(0, 11) + '%' + searchString.substring(11);
			}

			//For ALPHAX-3326 
			List<String> splitSearchString = Arrays.stream(searchString.split("\\s+")).collect(Collectors.toList());
			
			oem = (oem != null && oem.equalsIgnoreCase(RestInputConstants.DAG_STRING))? RestInputConstants.DCAG_STRING : oem;
			
			StringBuilder etstamm_Columns = new StringBuilder("SELECT herst as OEM,tnr as TNR,lnr as LNR, benen as NAME, INT(SA) as SA, ");
			etstamm_Columns.append("INT(aktbes) as STOCK, tmarke as BRAND, cast(EPR as REAL) as PRICE, cast(RG as varchar(2)) AS RGROUP, ");		
			etstamm_Columns.append(" MC AS MC,PRKZ AS PRKZ,cast(MWST as varchar(2)) AS TAX, (SELECT CAST((CAST(substr(DATAFLD, 1 , 7) AS REAL)/100) as DEC(7,2)) from ");
			etstamm_Columns.append(dataLibrary).append(".REFERENZ WHERE keyfld like '%2219%') as surcharge_price_limit,");
			etstamm_Columns.append("cast(GEWICH as REAL) AS GEWICH,LABEL AS LABEL,case substr(TVOLN, 1, 5)  when '     ' then 0 else int(substr(TVOLN, 1, 5)) END AS LAENGE, ");
			etstamm_Columns.append("case substr(TVOLN, 6, 5)when '     ' then 0 else int(substr(TVOLN, 6, 5)) END AS BREITE,");
			etstamm_Columns.append("case substr(TVOLN, 11, 5)when '     ' then 0 else int(substr(TVOLN, 11, 5)) END AS HOEHE, int(VERP1) AS VERP1, int(VERP2) AS VERP2,");
			etstamm_Columns.append("( SELECT RAB_TBL from ").append(dataLibrary).append(".F_KUNRAB where KDNR = '99999900' and FZART = 'ET' AND MARKE = et.tmarke ");
			etstamm_Columns.append(" AND FIRMA = ").append(companyId).append(" ) AS RAB_TBL    FROM ");
			
			StringBuilder tkfher_Columns = new StringBuilder("SELECT tkf_herst AS OEM, tkf_tnr AS TNR, -1 as LNR,tkf_benen AS NAME, int(-1) as SA, ");
			tkfher_Columns.append("int(0) as STOCK,tkf_tmarke AS BRAND,cast(tkf_BLP as REAL) AS PRICE,cast(tkf_RG as varchar(2)) AS RGROUP, ");		
			tkfher_Columns.append(" tkf_MC AS MC,tkf_PRKZ AS PRKZ,cast(tkf_TAXCDE as varchar(2))as TAX,( SELECT CAST((CAST(substr(DATAFLD, 1 , 7) AS REAL)/100) as DEC(7,2)) from ");
			tkfher_Columns.append(dataLibrary).append(".REFERENZ WHERE keyfld like '%2219%') as surcharge_price_limit,");
			tkfher_Columns.append("CAST(tkf_GEWICH as REAL) AS GEWICH, '' AS LABEL, int(tkf_LAENGE) AS LAENGE, ");
			tkfher_Columns.append("int(tkf_BREITE) AS BREITE, int(tkf_HOEHE) AS HOEHE, int(tkf_VERP1) AS VERP1,");
			tkfher_Columns.append("int(tkf_VERP2) AS VERP2, ");
			tkfher_Columns.append("( SELECT RAB_TBL from ").append(dataLibrary).append(".F_KUNRAB where KDNR = '99999900' and FZART = 'ET' AND MARKE = tkf.tkf_tmarke ");
			tkfher_Columns.append(" AND FIRMA = ").append(companyId).append(" ) AS RAB_TBL   FROM ");
			
			StringBuilder tlekat_Columns = new StringBuilder("SELECT tle_herst AS OEM, tle_tnr AS TNR, -1 as LNR,tle_benena AS NAME,int(-1) as SA, int(0) as STOCK,tle_tmarke AS BRAND, ");
			tlekat_Columns.append("cast(TLE_BLP as REAL) AS PRICE, cast(TLE_RG as varchar(2)) AS RGROUP,TLE_MC ");		
			tlekat_Columns.append(" AS MC,TLE_PRKZ AS PRKZ,CAST(TLE_TAXCDE as varchar(2)) AS TAX,(SELECT CAST((CAST(substr(DATAFLD, 1 , 7) AS REAL)/100) as DEC(7,2)) from ");
			tlekat_Columns.append(dataLibrary).append(".REFERENZ WHERE keyfld like '%2219%') as surcharge_price_limit,  CAST(TLE_GEWICH as REAL) AS GEWICH,");
			tlekat_Columns.append(" TLE_LABEL AS LABEL, int(TLE_LAENGE) AS LAENGE, int(TLE_BREITE) AS BREITE, int(TLE_HOEHE) AS HOEHE, int(TLE_VERP1) AS ");
			tlekat_Columns.append("VERP1, int(TLE_VERP2) AS VERP2,");
			tlekat_Columns.append("( SELECT RAB_TBL from ").append(dataLibrary).append(".F_KUNRAB where KDNR = '99999900' and FZART = 'ET' AND MARKE = etk.tle_tmarke ");
			tlekat_Columns.append(" AND FIRMA = ").append(companyId).append(" ) AS RAB_TBL FROM ");
			
			
			StringBuilder etstamm_where_clouse = new StringBuilder(dataLibrary).append(".E_ETSTAMM et WHERE ").append(createWhereClouse("tnr","benen",splitSearchString));
			etstamm_where_clouse.append(" herst='").append(oem).append("' ");
			etstamm_where_clouse.append(" AND LNR IN (").append(warehouseIds).append(") ");
			
			StringBuilder tkfher_where_clouse = new StringBuilder(schema).append(".etk_tkfher tkf WHERE ");
			tkfher_where_clouse.append(createWhereClouse("tkf_tnr","tkf_benen",splitSearchString));
			tkfher_where_clouse.append(" tkf_herst='").append(oem);
			tkfher_where_clouse.append("' AND tkf_dtgulv = (SELECT MAX(tkf_dtgulv) FROM ").append(schema);
			tkfher_where_clouse.append(".etk_tkfher WHERE tkf_tnr = tkf.tkf_tnr  AND  tkf_dtgulv <= current date ) and ");
			tkfher_where_clouse.append(" tkf_herst = tkf.tkf_herst AND tkf_tnr not in (SELECT TNR FROM ").append(dataLibrary);
			tkfher_where_clouse.append(".E_ETSTAMM WHERE ").append(createWhereClouse("tnr","benen",splitSearchString));
			tkfher_where_clouse.append(" herst='").append(oem).append("' ");
			tkfher_where_clouse.append(" AND LNR NOT IN (").append(warehouseIds).append(")) ");
			
			StringBuilder tlekat_where_clouse = new StringBuilder(schema).append(".etk_tlekat etk WHERE ");
			tlekat_where_clouse.append( createWhereClouse("tle_tnr","tle_benena",splitSearchString));
			tlekat_where_clouse.append(" tle_dtgulv = (SELECT MAX(tle_dtgulv) FROM ");
			tlekat_where_clouse.append(schema).append(".etk_tlekat WHERE tle_tnr = etk.tle_tnr  AND  tle_dtgulv <= current date ) ");
			tlekat_where_clouse.append(" AND tle_tnr not in (SELECT TNR FROM ").append(dataLibrary);
			tlekat_where_clouse.append(".E_ETSTAMM WHERE ").append(createWhereClouse("tnr","benen",splitSearchString));
			tlekat_where_clouse.append(" herst='").append(oem).append("' ");
			tlekat_where_clouse.append(" AND LNR NOT IN (").append(warehouseIds).append(")) ");

			String totalCount = "0";
			if( oem.equalsIgnoreCase("DCAG") ) {
			
				StringBuilder queryCountForDCAG = new StringBuilder("SELECT COUNT(tnr) AS count FROM ( ").append(etstamm_Columns);
				queryCountForDCAG.append(etstamm_where_clouse).append("UNION ").append(tlekat_Columns).append(tlekat_where_clouse).append(")");
				totalCount = dbServiceRepository.getCountUsingQuery(queryCountForDCAG.toString());
				
				//get actual results
				StringBuilder queryForDCAG = new StringBuilder(etstamm_Columns).append(etstamm_where_clouse);
				queryForDCAG.append(" UNION ");
				queryForDCAG.append(tlekat_Columns).append(tlekat_where_clouse);
				queryForDCAG.append(" ORDER BY TNR OFFSET ");
				queryForDCAG.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY"); 
				
				/* StringBuilder etstamm_where_clouse1 = new StringBuilder(schema).append(".DCAG_PART_VIEW1 WHERE ").append(createWhereClouse("tnr","NAME",splitSearchString));
				etstamm_where_clouse1.append(" OEM ='").append(oem).append("' ");
				etstamm_where_clouse1.append(" AND ( LNR IN (").append(warehouseIds).append(") OR LNR = -1 )  ");
				
				StringBuilder etstamm_Columns1 = new StringBuilder("SELECT OEM,TNR,LNR, NAME, INT(SA) as SA, ");
				etstamm_Columns1.append(" STOCK, BRAND,  PRICE,  RGROUP, ");		
				etstamm_Columns1.append(" MC, PRKZ, TAX, surcharge_price_limit,");
				etstamm_Columns1.append("cast(GEWICH as REAL) AS GEWICH,LABEL AS LABEL, LAENGE, ");
				etstamm_Columns1.append("BREITE,");
				etstamm_Columns1.append(" HOEHE, int(VERP1) AS VERP1, int(VERP2) AS VERP2, ");
				etstamm_Columns1.append("( SELECT COUNT(*)  FROM ").append(etstamm_where_clouse1).append(") AS count FROM "); 
				
				
				StringBuilder queryForDCAG = new StringBuilder(etstamm_Columns1).append(etstamm_where_clouse1);*/
				
				
				
				
				partsList = dbServiceRepository.getResultUsingQuery(PartGlobalSearch.class, queryForDCAG.toString(),true);
			}else{
				
				StringBuilder queryCountForNonDCAG = new StringBuilder("SELECT COUNT(tnr) AS count FROM ( ").append(etstamm_Columns);
				queryCountForNonDCAG.append(etstamm_where_clouse).append("UNION ").append(tkfher_Columns).append(tkfher_where_clouse).append(")");
				totalCount = dbServiceRepository.getCountUsingQuery(queryCountForNonDCAG.toString());
				
				//get actual results
				StringBuilder queryForNonDCAG = new StringBuilder(etstamm_Columns).append(etstamm_where_clouse);
				queryForNonDCAG.append(" UNION ");
				queryForNonDCAG.append(tkfher_Columns).append(tkfher_where_clouse);
				queryForNonDCAG.append(" ORDER BY TNR OFFSET ");
				queryForNonDCAG.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY");
				
				partsList = dbServiceRepository.getResultUsingQuery(PartGlobalSearch.class, queryForNonDCAG.toString(),true);
			}
			
			List<SearchPartsDTO> vehiclePartsList = new ArrayList<>();
			//if the list is not empty
			if (partsList != null && !partsList.isEmpty()) {
				for (PartGlobalSearch parts : partsList) {

					SearchPartsDTO partsDTO = new SearchPartsDTO();

					partsDTO.setOem(parts.getOem().equalsIgnoreCase(RestInputConstants.DCAG_STRING)? RestInputConstants.DAG_STRING: parts.getOem());
					partsDTO.setPartNumber(parts.getPartNumber());
					//partsDTO.setAgency(parts.getAgency()); //this field not in query
					partsDTO.setDescription(parts.getDescription());
					partsDTO.setOemBrand(parts.getOemBrand());
					
					if(parts.getWarehouse()!= null && !parts.getWarehouse().equalsIgnoreCase("-1")){
						
					partsDTO.setWarehouse(parts.getWarehouse());
					partsDTO.setStock(parts.getStock()!=null?parts.getStock():"-------");
					calculatePriceWithSurcharge(dataLibrary, parts.getPrice(), parts.getDiscountGroup(), 
							parts.getOemBrand() , parts.getSurchargePriceLimit(), partsDTO ,parts.getRabTbl(), companyId  );
						
					}
					else {
						partsDTO.setWarehouse("-------");
						partsDTO.setStock("-------");
						partsDTO.setAgency("------");
					}
					
					if(parts.getStorageIndikator()!= null && !parts.getStorageIndikator().equalsIgnoreCase("-1")){
						partsDTO.setStorageIndikator(parts.getStorageIndikator());
						partsDTO.setStorageIndikatorFlag(true);
					}else{
						partsDTO.setStorageIndikator("-----");
						partsDTO.setStorageIndikatorFlag(false);
					}
					
					partsDTO.setListPrice(String.valueOf(decimalformat_fixtwodigit.format(Double.parseDouble(parts.getPrice()))));
					partsDTO.setShoppingDiscountGroup(parts.getShoppingDiscountGroup());
					partsDTO.setMarketingCode(parts.getMarketingCode());
					partsDTO.setPriceIndicator(parts.getPriceIndicator());
					partsDTO.setVatRegistrationNumber(parts.getVatRegistrationNumber());
					partsDTO.setAssortmentClass(parts.getAssortmentClass());
					
					partsDTO.setWeight(String.valueOf(parts.getWeight()));
					//partsDTO.setDeposit(String.valueOf(parts.getDeposit())); // this field not in query
					partsDTO.setPartLabel(parts.getPartLabel());
					partsDTO.setPackagingUnit1(String.valueOf(parts.getPackagingUnit1()));
					partsDTO.setPackagingUnit2(String.valueOf(parts.getPackagingUnit2()));
					
					String length = StringUtils.leftPad(String.valueOf(parts.getLength()), 5, "0");
					String width = StringUtils.leftPad(String.valueOf(parts.getWidth()), 5, "0");
					String height = StringUtils.leftPad(String.valueOf(parts.getHeight()),5, "0");
					partsDTO.setLengthWidthHeight(StringUtils.join(length, width, height));
					

					vehiclePartsList.add(partsDTO);
				}
				globalSearchList.setSearchDetailsList(vehiclePartsList);
				globalSearchList.setTotalPages(totalCount);
				globalSearchList.setTotalRecordCnt(totalCount);
			} 
			else {
				globalSearchList.setSearchDetailsList(vehiclePartsList);
				globalSearchList.setTotalPages(Integer.toString(0));
				globalSearchList.setTotalRecordCnt(Integer.toString(0));
			}
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message: {}",  e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.SEARCH_FAILED_MSG_KEY, "Parts"));
			log.error(messageService.getReadableMessage(ExceptionMessages.SEARCH_FAILED_MSG_KEY, "Parts"), exception);
			throw exception;
		}

		return globalSearchList;
	}

	private String createWhereClouse(String tnr, String tnrName, List<String> splitSearchString) {

		StringBuilder whereClouse = new StringBuilder("(");
		
		for(String searchString: splitSearchString) {
			whereClouse.append("( UPPER(").append(tnr).append("||").append(tnrName).append(") LIKE UPPER('%").append(searchString).append("%')) AND");
		}
		int last = whereClouse.length() - 3;
		 whereClouse.replace(last, last+3, "");
		 whereClouse.append(") AND ");
 
		 return whereClouse.toString();
	}

	@Override
	@Cacheable(value = "manufacturerListCache", sync = true)
	public List<DropdownObject> getManufacturerList(String dataLibrary, String schema) {

		log.info("Inside getManufacturerList method of PartsServiceImpl");
		List<DropdownObject> manufacturerList = new ArrayList<>();

		try {
			StringBuilder query = new StringBuilder("SELECT her_herst AS KEYFLD, her_hbezku AS DATAFLD, her_bezei  FROM  ");
			query.append(schema).append(".pmh_herst ORDER BY her_bezei ");

			Map<String, String> manufacturerMap = dbServiceRepository.getResultUsingCobolQuery(query.toString());

			if(manufacturerMap != null && !manufacturerMap.isEmpty()) {
				for(Map.Entry<String, String> manufacturerDetails : manufacturerMap.entrySet()) {

					DropdownObject dropdownObject = new DropdownObject();
					dropdownObject.setKey(manufacturerDetails.getKey());
					dropdownObject.setValue(manufacturerDetails.getValue());

					manufacturerList.add(dropdownObject);
				}
			}else{
				log.debug("manufacturer List is empty");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(), 
						ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Manufacturer"));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Manufacturer"), exception);
				throw exception;
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Manufacturer"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Manufacturer"), exception);
			throw exception;
		}

		return manufacturerList;
	}


	private void convertPartEntityToDTO(PartDetails part_obj, PartDetailsDTO partDetailsDTO) {
		log.info("Start convertPartEntityToDTO");
	
		partDetailsDTO.setOem(part_obj.getOem().equalsIgnoreCase(RestInputConstants.DCAG_STRING)? RestInputConstants.DAG_STRING: part_obj.getOem());
		partDetailsDTO.setPartNumber(part_obj.getPartNumber()) ;
		partDetailsDTO.setWarehouse(String.valueOf(part_obj.getWarehouse())) ;
		partDetailsDTO.setStorageIndikator(String.valueOf(part_obj.getStorageIndikator())) ;
		partDetailsDTO.setActivityType(String.valueOf(part_obj.getActivityType()));
		partDetailsDTO.setName(part_obj.getName());
		partDetailsDTO.setStorageLocation(String.valueOf(part_obj.getStorageLocation()));
		partDetailsDTO.setDeliverIndicator(String.valueOf(part_obj.getDeliverIndicator()));
		partDetailsDTO.setAverageNetPrice(String.valueOf(decimalformat_fixFourdigit.format(part_obj.getAverageNetPrice()))) ;
		partDetailsDTO.setNetPrice(String.valueOf(decimalformat_twodigit.format(part_obj.getNetPrice()))) ;
		partDetailsDTO.setDeposit(String.valueOf(decimalformat_twodigit.format(part_obj.getDeposit()))) ;
		partDetailsDTO.setValueAddedTax(String.valueOf(part_obj.getValueAddedTax()));
		
		partDetailsDTO.setReturnValue(decimalformat_fixtwodigit.format(part_obj.getReturnValue()));
		
		partDetailsDTO.setLastPurchasePrice(decimalformat_twodigit.format(part_obj.getLastPurchasePrice()));
		partDetailsDTO.setDisposalCostEuro(String.valueOf(decimalformat_twodigit.format(part_obj.getDisposalCostEuro()))) ;
		partDetailsDTO.setFuturePurchasePrice(decimalformat_twodigit.format(part_obj.getFuturePurchasePrice()));
		partDetailsDTO.setOldPurchasePrice(decimalformat_twodigit.format(part_obj.getOldPurchasePrice()));
		partDetailsDTO.setPreviousPurchasePrice(decimalformat_fixtwodigit.format(part_obj.getPreviousPurchasePrice()));
		partDetailsDTO.setDisposalCost(String.valueOf(decimalformat_twodigit.format(part_obj.getDisposalCost())));
		partDetailsDTO.setDiscountGroup(part_obj.getDiscountGroup());
		partDetailsDTO.setOemBrand(part_obj.getOemBrand());
		partDetailsDTO.setCommonPartWithUnimog(part_obj.getCommonPartWithUnimog());
		partDetailsDTO.setMinimumStock(String.valueOf(decimalformat_twodigit.format(part_obj.getMinimumStock())));
		partDetailsDTO.setMaximumStock(String.valueOf(decimalformat_twodigit.format(part_obj.getMaximumStock())));
		partDetailsDTO.setSaftyStock(String.valueOf(decimalformat_twodigit.format(part_obj.getSaftyStock())));
		partDetailsDTO.setCurrentInventoryCountedStock(String.valueOf(decimalformat_twodigit.format(part_obj.getCurrentInventoryCountedStock())));
		partDetailsDTO.setCurrentStock(String.valueOf(decimalformat_fixtwodigit.format(part_obj.getCurrentStock())));
		partDetailsDTO.setPreviousInventoryCountedStock(String.valueOf(decimalformat_twodigit.format(part_obj.getPreviousInventoryCountedStock())));
		partDetailsDTO.setSalesStockCurrentMonth(String.valueOf(decimalformat_fixtwodigit.format(part_obj.getSalesStockCurrentMonth())));

		List<Object> currentYearMonthlySalesStockList = new ArrayList<>();
		currentYearMonthlySalesStockList.add(part_obj.getCurrentYearMonthlySalesStock01());
		currentYearMonthlySalesStockList.add(part_obj.getCurrentYearMonthlySalesStock02());
		currentYearMonthlySalesStockList.add(part_obj.getCurrentYearMonthlySalesStock03());
		currentYearMonthlySalesStockList.add(part_obj.getCurrentYearMonthlySalesStock04());
		currentYearMonthlySalesStockList.add(part_obj.getCurrentYearMonthlySalesStock05());
		currentYearMonthlySalesStockList.add(part_obj.getCurrentYearMonthlySalesStock06());
		currentYearMonthlySalesStockList.add(part_obj.getCurrentYearMonthlySalesStock07());
		currentYearMonthlySalesStockList.add(part_obj.getCurrentYearMonthlySalesStock08());
		currentYearMonthlySalesStockList.add(part_obj.getCurrentYearMonthlySalesStock09());
		currentYearMonthlySalesStockList.add(part_obj.getCurrentYearMonthlySalesStock10());
		currentYearMonthlySalesStockList.add(part_obj.getCurrentYearMonthlySalesStock11());
		currentYearMonthlySalesStockList.add(part_obj.getCurrentYearMonthlySalesStock12());
		List<MonthlyDecimalValue> MonthlyDecimalValues1 =  getMonthlyDecimalValuePairs(currentYearMonthlySalesStockList);
		partDetailsDTO.setCurrentYearMonthlySalesStock(MonthlyDecimalValues1);

		partDetailsDTO.setSalesStockAverageCurrentYear(String.valueOf(decimalformat_twodigit.format(part_obj.getSalesStockAverageCurrentYear()))) ;

		List<Object> lastYearMonthlySalesStockList = new ArrayList<>();
		lastYearMonthlySalesStockList.add(part_obj.getLastYearMonthlySalesStock01());
		lastYearMonthlySalesStockList.add(part_obj.getLastYearMonthlySalesStock02());
		lastYearMonthlySalesStockList.add(part_obj.getLastYearMonthlySalesStock03());
		lastYearMonthlySalesStockList.add(part_obj.getLastYearMonthlySalesStock04());
		lastYearMonthlySalesStockList.add(part_obj.getLastYearMonthlySalesStock05());
		lastYearMonthlySalesStockList.add(part_obj.getLastYearMonthlySalesStock06());
		lastYearMonthlySalesStockList.add(part_obj.getLastYearMonthlySalesStock07());
		lastYearMonthlySalesStockList.add(part_obj.getLastYearMonthlySalesStock08());
		lastYearMonthlySalesStockList.add(part_obj.getLastYearMonthlySalesStock09());
		lastYearMonthlySalesStockList.add(part_obj.getLastYearMonthlySalesStock10());
		lastYearMonthlySalesStockList.add(part_obj.getLastYearMonthlySalesStock11());
		lastYearMonthlySalesStockList.add(part_obj.getLastYearMonthlySalesStock12());
		List<MonthlyDecimalValue> MonthlyDecimalValues2 =  getMonthlyDecimalValuePairs(lastYearMonthlySalesStockList);
		partDetailsDTO.setLastYearMonthlySalesStock(MonthlyDecimalValues2);

		partDetailsDTO.setSalesStockLastYear(String.valueOf(decimalformat_twodigit.format(part_obj.getSalesStockLastYear())));
		partDetailsDTO.setCurrentYearSalesStock(String.valueOf(decimalformat_twodigit.format(part_obj.getCurrentYearSalesStock())));
		partDetailsDTO.setCurrentMonthReceiptStock(String.valueOf(decimalformat_fixtwodigit.format(part_obj.getCurrentMonthReceiptStock())));

		List<Object> monthlyReceiptsStockList= new ArrayList<>();
		monthlyReceiptsStockList.add(part_obj.getMonthlyReceiptsStock01());
		monthlyReceiptsStockList.add(part_obj.getMonthlyReceiptsStock02());
		monthlyReceiptsStockList.add(part_obj.getMonthlyReceiptsStock03());
		monthlyReceiptsStockList.add(part_obj.getMonthlyReceiptsStock04());
		monthlyReceiptsStockList.add(part_obj.getMonthlyReceiptsStock05());
		monthlyReceiptsStockList.add(part_obj.getMonthlyReceiptsStock06());
		monthlyReceiptsStockList.add(part_obj.getMonthlyReceiptsStock07());
		monthlyReceiptsStockList.add(part_obj.getMonthlyReceiptsStock08());
		monthlyReceiptsStockList.add(part_obj.getMonthlyReceiptsStock09());
		monthlyReceiptsStockList.add(part_obj.getMonthlyReceiptsStock10());
		monthlyReceiptsStockList.add(part_obj.getMonthlyReceiptsStock11());
		monthlyReceiptsStockList.add(part_obj.getMonthlyReceiptsStock12());
		List<MonthlyDecimalValue> MonthlyDecimalValues3 =  getMonthlyDecimalValuePairs(monthlyReceiptsStockList);
		partDetailsDTO.setMonthlyReceiptsStock(MonthlyDecimalValues3);

		partDetailsDTO.setCurrentYearAverageReceiptStock(String.valueOf(decimalformat_twodigit.format(part_obj.getCurrentYearAverageReceiptStock())));
		partDetailsDTO.setLastYearReceiptStock(String.valueOf(decimalformat_twodigit.format(part_obj.getLastYearReceiptStock())));
		partDetailsDTO.setCurrentYearReceiptStock(String.valueOf(decimalformat_twodigit.format(part_obj.getCurrentYearReceiptStock())));
		partDetailsDTO.setCurrentMonthSales(String.valueOf(decimalformat_twodigit.format(part_obj.getCurrentMonthSales())));

		List<Object> currentYearMonthlySalesList = new ArrayList<>();
		currentYearMonthlySalesList.add(part_obj.getCurrentYearMonthlySales01());
		currentYearMonthlySalesList.add(part_obj.getCurrentYearMonthlySales02());
		currentYearMonthlySalesList.add(part_obj.getCurrentYearMonthlySales03());
		currentYearMonthlySalesList.add(part_obj.getCurrentYearMonthlySales04());
		currentYearMonthlySalesList.add(part_obj.getCurrentYearMonthlySales05());
		currentYearMonthlySalesList.add(part_obj.getCurrentYearMonthlySales06());
		currentYearMonthlySalesList.add(part_obj.getCurrentYearMonthlySales07());
		currentYearMonthlySalesList.add(part_obj.getCurrentYearMonthlySales08());
		currentYearMonthlySalesList.add(part_obj.getCurrentYearMonthlySales09());
		currentYearMonthlySalesList.add(part_obj.getCurrentYearMonthlySales10());
		currentYearMonthlySalesList.add(part_obj.getCurrentYearMonthlySales11());
		currentYearMonthlySalesList.add(part_obj.getCurrentYearMonthlySales12());
		List<MonthlyDecimalValue> MonthlyDecimalValues4 =  getMonthlyDecimalValuePairs(currentYearMonthlySalesList);
		partDetailsDTO.setCurrentYearMonthlySales(MonthlyDecimalValues4) ;

		partDetailsDTO.setAverageSales(String.valueOf(decimalformat_fourdigit.format(part_obj.getAverageSales())));

		List<Object> lastYearMonthlySalesList = new ArrayList<>();
		lastYearMonthlySalesList.add(part_obj.getLastYearMonthlySales01());
		lastYearMonthlySalesList.add(part_obj.getLastYearMonthlySales02());
		lastYearMonthlySalesList.add(part_obj.getLastYearMonthlySales03());
		lastYearMonthlySalesList.add(part_obj.getLastYearMonthlySales04());
		lastYearMonthlySalesList.add(part_obj.getLastYearMonthlySales05());
		lastYearMonthlySalesList.add(part_obj.getLastYearMonthlySales06());
		lastYearMonthlySalesList.add(part_obj.getLastYearMonthlySales07());
		lastYearMonthlySalesList.add(part_obj.getLastYearMonthlySales08());
		lastYearMonthlySalesList.add(part_obj.getLastYearMonthlySales09());
		lastYearMonthlySalesList.add(part_obj.getLastYearMonthlySales10());
		lastYearMonthlySalesList.add(part_obj.getLastYearMonthlySales11());
		lastYearMonthlySalesList.add(part_obj.getLastYearMonthlySales12());
		List<MonthlyDecimalValue> MonthlyDecimalValues5 =  getMonthlyDecimalValuePairs(lastYearMonthlySalesList);
		partDetailsDTO.setLastYearMonthlySales(MonthlyDecimalValues5);

		partDetailsDTO.setLastYearSales(String.valueOf(decimalformat_fourdigit.format(part_obj.getLastYearSales())));
		partDetailsDTO.setCurrentYearSales(String.valueOf(decimalformat_fourdigit.format(part_obj.getCurrentYearSales())));
		partDetailsDTO.setCurrentMonthReceiptInEuro(String.valueOf(decimalformat_fourdigit.format(part_obj.getCurrentMonthReceiptInEuro())));

		List<Object> monthlyReceiptsInEuroList = new ArrayList<>();
		monthlyReceiptsInEuroList.add(part_obj.getMonthlyReceiptsInEuro01());
		monthlyReceiptsInEuroList.add(part_obj.getMonthlyReceiptsInEuro02());
		monthlyReceiptsInEuroList.add(part_obj.getMonthlyReceiptsInEuro03());
		monthlyReceiptsInEuroList.add(part_obj.getMonthlyReceiptsInEuro04());
		monthlyReceiptsInEuroList.add(part_obj.getMonthlyReceiptsInEuro05());
		monthlyReceiptsInEuroList.add(part_obj.getMonthlyReceiptsInEuro06());
		monthlyReceiptsInEuroList.add(part_obj.getMonthlyReceiptsInEuro07());
		monthlyReceiptsInEuroList.add(part_obj.getMonthlyReceiptsInEuro08());
		monthlyReceiptsInEuroList.add(part_obj.getMonthlyReceiptsInEuro09());
		monthlyReceiptsInEuroList.add(part_obj.getMonthlyReceiptsInEuro10());
		monthlyReceiptsInEuroList.add(part_obj.getMonthlyReceiptsInEuro11());
		monthlyReceiptsInEuroList.add(part_obj.getMonthlyReceiptsInEuro12());
		List<MonthlyDecimalValue> MonthlyDecimalValues6 =  getMonthlyDecimalValuePairs(monthlyReceiptsInEuroList);
		partDetailsDTO.setMonthlyReceiptsInEuro(MonthlyDecimalValues6) ;

		partDetailsDTO.setAverageReceiptInEuro(String.valueOf(decimalformat_fourdigit.format(part_obj.getAverageReceiptInEuro())));
		partDetailsDTO.setLastYearReceiptInEuro(String.valueOf(decimalformat_fourdigit.format(part_obj.getLastYearReceiptInEuro())));
		partDetailsDTO.setCurrentYearReceiptInEuro(String.valueOf(decimalformat_fourdigit.format(part_obj.getCurrentYearReceiptInEuro())));
		partDetailsDTO.setPendingOrders(String.valueOf(decimalformat_fixtwodigit.format(part_obj.getPendingOrders())));
		partDetailsDTO.setMinimumOrderQuantity(String.valueOf(decimalformat_twodigit.format(part_obj.getMinimumOrderQuantity())));
		partDetailsDTO.setCustomerBacklog(String.valueOf(decimalformat_twodigit.format(part_obj.getCustomerBacklog())));
		partDetailsDTO.setDeliveryBacklog(String.valueOf(decimalformat_twodigit.format(part_obj.getDeliveryBacklog())));
		partDetailsDTO.setSalesAmount(String.valueOf(decimalformat_twodigit.format(part_obj.getSalesAmount())));
		partDetailsDTO.setRequestedQuantity(String.valueOf(decimalformat_twodigit.format(part_obj.getRequestedQuantity())));
		partDetailsDTO.setBookingAmount(String.valueOf(part_obj.getBookingAmount()));
		partDetailsDTO.setRequestedAmount(String.valueOf(part_obj.getRequestedAmount()));

		List<Object> currentYearMonthlyRequestedAmtList = new ArrayList<>();
		currentYearMonthlyRequestedAmtList.add(part_obj.getCurrentYearMonthlyRequestedAmounts01());
		currentYearMonthlyRequestedAmtList.add(part_obj.getCurrentYearMonthlyRequestedAmounts02());
		currentYearMonthlyRequestedAmtList.add(part_obj.getCurrentYearMonthlyRequestedAmounts03());
		currentYearMonthlyRequestedAmtList.add(part_obj.getCurrentYearMonthlyRequestedAmounts04());
		currentYearMonthlyRequestedAmtList.add(part_obj.getCurrentYearMonthlyRequestedAmounts05());
		currentYearMonthlyRequestedAmtList.add(part_obj.getCurrentYearMonthlyRequestedAmounts06());
		currentYearMonthlyRequestedAmtList.add(part_obj.getCurrentYearMonthlyRequestedAmounts07());
		currentYearMonthlyRequestedAmtList.add(part_obj.getCurrentYearMonthlyRequestedAmounts08());
		currentYearMonthlyRequestedAmtList.add(part_obj.getCurrentYearMonthlyRequestedAmounts09());
		currentYearMonthlyRequestedAmtList.add(part_obj.getCurrentYearMonthlyRequestedAmounts10());
		currentYearMonthlyRequestedAmtList.add(part_obj.getCurrentYearMonthlyRequestedAmounts11());
		currentYearMonthlyRequestedAmtList.add(part_obj.getCurrentYearMonthlyRequestedAmounts12());
		List<MonthlyIntValue> monthlyIntValuelist1 =  getMonthlyIntValuePairs(currentYearMonthlyRequestedAmtList);
		partDetailsDTO.setCurrentYearMonthlyRequestedAmounts(monthlyIntValuelist1) ;

		List<Object> lastYearMonthlyRequestedAmountsList = new ArrayList<>();
		lastYearMonthlyRequestedAmountsList.add(part_obj.getLastYearMonthlyRequestedAmounts01());
		lastYearMonthlyRequestedAmountsList.add(part_obj.getLastYearMonthlyRequestedAmounts02());
		lastYearMonthlyRequestedAmountsList.add(part_obj.getLastYearMonthlyRequestedAmounts03());
		lastYearMonthlyRequestedAmountsList.add(part_obj.getLastYearMonthlyRequestedAmounts04());
		lastYearMonthlyRequestedAmountsList.add(part_obj.getLastYearMonthlyRequestedAmounts05());
		lastYearMonthlyRequestedAmountsList.add(part_obj.getLastYearMonthlyRequestedAmounts06());
		lastYearMonthlyRequestedAmountsList.add(part_obj.getLastYearMonthlyRequestedAmounts07());
		lastYearMonthlyRequestedAmountsList.add(part_obj.getLastYearMonthlyRequestedAmounts08());
		lastYearMonthlyRequestedAmountsList.add(part_obj.getLastYearMonthlyRequestedAmounts09());
		lastYearMonthlyRequestedAmountsList.add(part_obj.getLastYearMonthlyRequestedAmounts10());
		lastYearMonthlyRequestedAmountsList.add(part_obj.getLastYearMonthlyRequestedAmounts11());
		lastYearMonthlyRequestedAmountsList.add(part_obj.getLastYearMonthlyRequestedAmounts12());
		List<MonthlyIntValue> monthlyIntValuelist2 =  getMonthlyIntValuePairs(lastYearMonthlyRequestedAmountsList);
		partDetailsDTO.setLastYearMonthlyRequestedAmounts(monthlyIntValuelist2);

		partDetailsDTO.setServiceProposal(String.valueOf(decimalformat_twodigit.format(part_obj.getServiceProposal())));
		partDetailsDTO.setInventoryAmount(String.valueOf(decimalformat_fixtwodigit.format(part_obj.getInventoryAmount())));
		partDetailsDTO.setOrderAnnotation(part_obj.getOrderAnnotation());
		partDetailsDTO.setCalculationLock(part_obj.getCalculationLock());
		partDetailsDTO.setCompartsMark(part_obj.getCompartsMark());
		partDetailsDTO.setPriceMark(part_obj.getPriceMark());
		partDetailsDTO.setDeletionMark(part_obj.getDeletionMark());
		partDetailsDTO.setDispoMark(part_obj.getDispoMark());
		partDetailsDTO.setInventorygMark(part_obj.getInventorygMark());
		partDetailsDTO.setBarcodeNumber(String.valueOf(part_obj.getBarcodeNumber()));
		partDetailsDTO.setSurchargeDeduction(String.valueOf(decimalformat_twodigit.format(part_obj.getSurchargeDeduction())));
		partDetailsDTO.setSurchargeDeductionMark(String.valueOf(part_obj.getSurchargeDeductionMark()));
		partDetailsDTO.setCampaignMark(part_obj.getCampaignMark());
		partDetailsDTO.setMode(part_obj.getMode());
		partDetailsDTO.setLastDisposalDate(getDDMMYYYYFormat(part_obj.getLastDisposalDate()));
		partDetailsDTO.setLastReceiptDate(getDDMMYYYYFormat(part_obj.getLastReceiptDate()));
		partDetailsDTO.setCreationDate(getDDMMYYYYFormat(part_obj.getCreationDate()));
		partDetailsDTO.setLastOrderDate(getDDMMYYYYFormat(part_obj.getLastOrderDate()));
		partDetailsDTO.setExpirationDate(getDDMMYYYYFormat(part_obj.getExpirationDate()));
		partDetailsDTO.setFuturePriceFromDate(getDDMMYYYYFormat(part_obj.getFuturePriceFromDate()));
		partDetailsDTO.setLastMovementDate(getDDMMYYYYFormat(part_obj.getLastMovementDate()));
		partDetailsDTO.setInventoryDate(getDDMMYYYYFormat(part_obj.getInventoryDate()));
		partDetailsDTO.setLastDispositionDate(getDDMMYYYYFormat(part_obj.getLastDispositionDate()));
		partDetailsDTO.setMarketingCode(part_obj.getMarketingCode());
		partDetailsDTO.setSortingFormatPartNumber(part_obj.getSortingFormatPartNumber());
		partDetailsDTO.setPrintingFormatPartNumber(part_obj.getPrintingFormatPartNumber());
		partDetailsDTO.setPredecessorPartNumber(part_obj.getPredecessorPartNumber());
		partDetailsDTO.setSuccessorPartNumber(part_obj.getSuccessorPartNumber());
		partDetailsDTO.setPartNumberMarketplace(part_obj.getPartNumberMarketplace());
		partDetailsDTO.setMarketplaceStockNumber(part_obj.getMarketplaceStockNumber());
		partDetailsDTO.setOemPartNumber(part_obj.getOemPartNumber());
		partDetailsDTO.setcCodeHint(part_obj.getcCodeHint());
		partDetailsDTO.setPackagingUnit1(String.valueOf(part_obj.getPackagingUnit1()));
		partDetailsDTO.setPackagingUnit2(String.valueOf(part_obj.getPackagingUnit2()));
		partDetailsDTO.setDeletionMark2(part_obj.getDeletionMark2());
		partDetailsDTO.setPartNumberIdentificationMark(part_obj.getPartNumberIdentificationMark());
		partDetailsDTO.setTheftMark(part_obj.getTheftMark());
		partDetailsDTO.setHazardousMaterialMark(part_obj.getHazardousMaterialMark());
		partDetailsDTO.setExchangeMark(part_obj.getExchangeMark());
		partDetailsDTO.setWeight(String.valueOf(decimalformat_fixtwodigit.format(part_obj.getWeight())));
		partDetailsDTO.setOldVolume(part_obj.getOldVolume());
		partDetailsDTO.setDiscountLockMark(part_obj.getDiscountLockMark());
		partDetailsDTO.setSubmissionMark(part_obj.getSubmissionMark());
		partDetailsDTO.setTradeGoodsNumber(part_obj.getTradeGoodsNumber());
		partDetailsDTO.setQuantityUnit(part_obj.getQuantityUnit());
		partDetailsDTO.setInventoryGroup(part_obj.getInventoryGroup());
		partDetailsDTO.setStockageMark(part_obj.getStockageMark());
		partDetailsDTO.setReturnPermittedMark(part_obj.getReturnPermittedMark());
		partDetailsDTO.setPreferenceMark(part_obj.getPreferenceMark());
		partDetailsDTO.setCompany(String.valueOf(part_obj.getCompany()));
		partDetailsDTO.setBranch(String.valueOf(part_obj.getBranch()));
		partDetailsDTO.setSalesLockMark(part_obj.getSalesLockMark());
		partDetailsDTO.setMainSupplier(part_obj.getMainSupplier());

		String length = "";
		String width = "";
		String height = "";
		String volume = "";
		
		Integer intLength = 0;
		Integer intWidth = 0;
		Integer intHeight = 0;
		
		String lengthWidthHight = part_obj.getLengthWidthHeigth();
		
		if(lengthWidthHight != null && !lengthWidthHight.trim().isEmpty()){
			length = StringUtils.stripStart(lengthWidthHight.substring(0,5), "0");
			width = StringUtils.stripStart(lengthWidthHight.substring(5,10), "0");
			height = StringUtils.stripStart(lengthWidthHight.substring(10,15), "0");
			
			if(length!= null && !length.trim().isEmpty()) {
				intLength = Integer.valueOf(length);
			}
			if(width!= null && !width.trim().isEmpty()) {
				intWidth = Integer.valueOf(width);
			}
			if(height!= null && !height.trim().isEmpty()) {
				intHeight = Integer.valueOf(height);
			}
			
			volume = String.valueOf(intLength * intWidth * intHeight);
		}
		partDetailsDTO.setLength(length) ;
		partDetailsDTO.setWidth(width) ;
		partDetailsDTO.setHeight(height);
		
		partDetailsDTO.setVolume(volume);

		partDetailsDTO.setInternalAllocationIndicator(part_obj.getInternalAllocationIndicator()) ;
		partDetailsDTO.setInventoryAtYearStart(String.valueOf(decimalformat_twodigit.format(part_obj.getInventoryAtYearStart())));
		
		partDetailsDTO.setDiscountGroupPercentageValue(String.valueOf(decimalformat_twodigit.format(part_obj.getDiscountGroupPercentageValue()!=null ? part_obj.getDiscountGroupPercentageValue() : 0 )));
		
		Double purchasePrice = part_obj.getPurchasePrice() != null ? part_obj.getPurchasePrice().doubleValue() : 0.00;
		partDetailsDTO.setPurchasePrice(decimalformat_fixtwodigit.format(purchasePrice));
		
		
		partDetailsDTO.setStorageValue(part_obj.getStorageValue() !=null ? part_obj.getStorageValue() : "0");
		partDetailsDTO.setImcoNumber((part_obj.getImcoNumber() !=null && !part_obj.getImcoNumber().trim().isEmpty()) ? part_obj.getImcoNumber() : "0");
		partDetailsDTO.setWarehouseName(part_obj.getLagerName());
		
		String partsIndikator = String.valueOf(part_obj.getPartsIndikator());
		String partsIndikatorValue = StubServiceRepository.teileartMap.get(partsIndikator);
		partDetailsDTO.setPartsIndikator(partsIndikator +" "+ partsIndikatorValue);
		

	}


	private List<MonthlyDecimalValue> getMonthlyDecimalValuePairs(List<Object> monthlyValuesList) {

		MonthlyDecimalValue monthlyDecimalValue = null;
		List<MonthlyDecimalValue> monthlyDecimalKeyValueList = new ArrayList<>();
		Integer month = 1;
		for (Object obj : monthlyValuesList) {
			monthlyDecimalValue = new MonthlyDecimalValue();

			monthlyDecimalValue.setMonth(month);
			monthlyDecimalValue.setValue((BigDecimal) obj);
			monthlyDecimalKeyValueList.add(monthlyDecimalValue);

			month++;

		}

		return monthlyDecimalKeyValueList;

	}

	private List<MonthlyIntValue> getMonthlyIntValuePairs(List<Object> monthlyValuesList) {

		MonthlyIntValue monthlyIntValue = null;
		List<MonthlyIntValue> monthlyIntegerKeyValueList = new ArrayList<>();
		Integer month = 1;
		for (Object obj : monthlyValuesList) {
			monthlyIntValue = new MonthlyIntValue();
			//System.out.println(obj);
			monthlyIntValue.setMonth(month);
			monthlyIntValue.setValue((BigDecimal) obj);
			monthlyIntegerKeyValueList.add(monthlyIntValue);

			month++;
		}
		return monthlyIntegerKeyValueList;

	}

	private String getDDMMYYYYFormat(BigDecimal dateValue) {

		String day = "";
		String month = "";
		String year = "";
		String formatedDate = "";

		if (dateValue != null) {
			String dateFormat = String.valueOf(dateValue);
			if (dateFormat.length() == 8) {
				day = dateFormat.substring(6,8);
				month = dateFormat.substring(4, 6);
				year = dateFormat.substring(0, 4);
				formatedDate = day + "/" + month + "/" + year;
			}
		}
		return formatedDate;
	}


	/**
	 * This method is used to get the Preis Kennzeichen (Price indicator) list from Stub
	 * @return List
	 */
	@Override
	public List<DropdownObject> getPreisKzList() {
		log.info("Inside getPreisKzList method of PartsServiceImpl");

		List<DropdownObject> mehrwertsteuerList = new ArrayList<>();
		mehrwertsteuerList = stubServiceRepository.getDDLValuesFromStub(StubServiceRepository.preisKzMap);
		return mehrwertsteuerList;
	}


	/**
	 * This method is used to get the Abschlag Kennzeichen (Discount mark) list from Stub
	 * @return List
	 */
	@Override
	public List<DropdownObject> getAbschlagKzList() {
		log.info("Inside getAbschlagKzList method of PartsServiceImpl");

		List<DropdownObject> mehrwertsteuerList = new ArrayList<>();
		mehrwertsteuerList = stubServiceRepository.getDDLValuesFromStub(StubServiceRepository.abschlagKzMap);
		return mehrwertsteuerList;
	}


	/**
	 * This method is used to get the Kategorie (Categories) list from DB table e_etlabel
	 * @return List
	 */
	@Override
	public List<DropdownObject> getCategoryList(String dataLibrary, String kategorie) {

		log.info("Inside getCategoryList method of PartsServiceImpl");
		List<DropdownObject> categoriesList = new ArrayList<>();

		try {
			StringBuilder query = new StringBuilder("SELECT WERT AS KEYFLD, TEXT AS DATAFLD FROM  ");
			query.append(dataLibrary).append(".e_etlabel WHERE LABEL=1 AND KAT=").append(kategorie).append(" AND WERT>0 ORDER BY WERT ");

			Map<String, String> categoriesMap = dbServiceRepository.getResultUsingCobolQuery(query.toString());

			if(categoriesMap != null && !categoriesMap.isEmpty()) {

				DropdownObject dropdownObject1 = new DropdownObject();
				dropdownObject1.setKey("00");
				dropdownObject1.setValue("Keine");
				categoriesList.add(dropdownObject1);

				for(Map.Entry<String, String> categoriesDetails : categoriesMap.entrySet()) {

					DropdownObject dropdownObject = new DropdownObject();
					dropdownObject.setKey(StringUtils.leftPad(categoriesDetails.getKey(), 2, "0"));
					dropdownObject.setValue(categoriesDetails.getValue());

					categoriesList.add(dropdownObject);
				}
			}else{
				log.debug("categories List is empty");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(), 
						ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "categories"));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "categories"), exception);
				throw exception;
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "categories"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "categories"), exception);
			throw exception;
		}

		return categoriesList;
	}
	
	
	@Override
	public PartDetailsDTO getNewPartDetailsByPartNumber(String dataLibrary, String schema, String companyId, String agencyId,
			String warehouseId, String oem, String partId) {
		log.info("Inside getNewPartDetailsByPartNumber method of PartsServiceImpl");

		//validate the company Id 
		validateCompany(companyId);

		//validate the agency Id
		validateAgency(agencyId);

		if(Integer.parseInt(warehouseId) < 0){
			log.info("Warehouse is not valid");
			AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.VEHICLE_WAREHOUSE_NUMBER_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.VEHICLE_WAREHOUSE_NUMBER_MSG_KEY));
			throw exception;
		}

		oem = oem.equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING: oem;

		StringBuilder part_query_1 = new StringBuilder(" SELECT HERST,TNR,LNR,SA,TA,LEIART,BENEN,LOPA,LIWERK,EPR,DAK,EKNPR,PFAND,TRUWER,LEKPR,ENTSOW,ZPREIS,APREIS,NPREIS,ENTSOP,RG,");
		part_query_1.append("TMARKE,MWST,SKL,MINBES,MAXBES,SIBES,IVZME,AKTBES,LIVBES,VKLFMS,VKLJ1S,VKLJ2S,VKLJ3S,VKLJ4S,VKLJ5S,VKLJ6S,VKLJ7S,VKLJ8S,VKLJ9S,VKLJAS,VKLJBS,VKLJCS,VKAVGS,VKVJ1S,VKVJ2S,");
		part_query_1.append("VKVJ3S,VKVJ4S,VKVJ5S,VKVJ6S,VKVJ7S,VKVJ8S,VKVJ9S,VKVJAS,VKVJBS,VKVJCS,VKSVJS,VKLFJS,ZULFMS,ZULM1S,ZULM2S,ZULM3S,ZULM4S,ZULM5S,ZULM6S,ZULM7S,ZULM8S,ZULM9S,");
		part_query_1.append("ZULMAS,ZULMBS,ZULMCS,ZUAVGS,ZUSVJS,ZULFJS,VKLFMW,VKLJ1W,VKLJ2W,VKLJ3W,VKLJ4W,VKLJ5W,VKLJ6W,VKLJ7W,VKLJ8W,VKLJ9W,VKLJAW,VKLJBW,VKLJCW,VKAVGW,VKVJ1W,VKVJ2W,VKVJ3W,VKVJ4W,VKVJ5W,VKVJ6W,VKVJ7W,VKVJ8W,VKVJ9W,");
		part_query_1.append("VKVJAW,VKVJBW,VKVJCW,VKSVJW,VKLFJW,ZULFMW,ZULM1W,ZULM2W,ZULM3W,ZULM4W,ZULM5W,ZULM6W,ZULM7W,ZULM8W,ZULM9W,ZULMAW,ZULMBW,ZULMCW,ZUAVGW,ZUSVJW,ZULFJW,BESAUS,MIBESM,KDRUE,LIFRUE,ANZVK,NFMGE,ZUMGE,");
		part_query_1.append("ANZNFS,NFLJ01,NFLJ02,NFLJ03,NFLJ04,NFLJ05,NFLJ06,NFLJ07,NFLJ08,NFLJ09,NFLJ10,NFLJ11,NFLJ12,NFVJ01,NFVJ02,NFVJ03,NFVJ04,NFVJ05,NFVJ06,NFVJ07,NFVJ08,NFVJ09,NFVJ10,NFVJ11,NFVJ12,TVORMG,VORHS,BESVER,KSPKZ,NC,PRKZ,LKZ,DISPO,IVKZ,BARCDE,");
		part_query_1.append("ABSCH,KDABS,WINTER,MODUS,DTLABG,DTLZUG,DTANLA,DTLBES,DTVERF,GPRDAT,DTLBEW,DTINV,DATLDI,MC,TNRS,TNRD,TNRV,TNRN,TNRM,TNRMLN,TNRH,TCODE,VERP1,VERP2,LKZ2,TIDENT,DRTKZ,GFKZ,TAUKZ,GEWICH,");
		part_query_1.append("TVOL,TRSPER,KZEINS,THWSNR,ME,ZAEGRU,KZBEVO,KZRUCK,PRAFKZ,FIRMA,FILIAL,SPKZV,LIEFNR,TVOLN,INTVKZ,IVBGJS, LABEL, ");
		part_query_1.append("( SELECT NAME FROM ").append(schema).append(".O_WRH WHERE AP_WAREHOUS_ID = LNR) AS LagerName, ");
		part_query_1.append("(Select TLE_LAZ from ").append(schema).append(".ETK_TLEKAT where TLE_TNR='").append(partId).append("' and TLE_DTGULV <= current date order by TLE_DTGULV desc LIMIT 1) AS TLE_LAZ, ");
		part_query_1.append("(Select TLE_IMCO from ").append(schema).append(".ETK_TLEKAT where TLE_TNR='").append(partId).append("' and TLE_DTGULV <= current date order by TLE_DTGULV desc LIMIT 1) AS TLE_IMCO, ");
		part_query_1.append("(select substr(DATAFLD, 1, 2) as MWST_SATZ from ");
		part_query_1.append(dataLibrary).append(".REFERENZ where substr(KEYFLD, 7, 4) = '9907' and substr(KEYFLD, 11, 2) = '01') AS MWST_SATZ, ");
		part_query_1.append("(select SATZ from ").append(dataLibrary).append(".E_RAB where HERST = HERST and Gruppe = RG and MARKE = TMARKE and LKZ='' order by GDAT desc fetch first row only) AS SATZ, ");
		part_query_1.append("(select CONTAINER_SIZE_FROM  from ").append(schema).append(".O_PARTTRAN where PARTNUMBER_FROM= '").append(partId).append("'  AND  OEM= '").append(oem).append("' AND ");
		part_query_1.append(" WAREHOUSE = ").append(warehouseId).append(") AS containerSizeFrom ");
		part_query_1.append(" FROM ").append(dataLibrary).append(".E_ETSTAMM ");
		part_query_1.append("WHERE TNR = ").append("'"+partId+"'").append(" AND LNR = ").append(warehouseId);
		//part_query_1.append(" AND FIRMA = ").append(companyId);//.append(" AND FILIAL = ").append(agencyId);
		part_query_1.append(" AND HERST = ").append("'"+oem+"'");

		PartDetailsDTO partDetailsDTO = new PartDetailsDTO();
		List<PartDetails> partDetailsList = new ArrayList<>();

		String partLabel = "";
		PartLabelDTO partLabelDTO = null;
		
		try {
			partDetailsList = dbServiceRepository.getResultUsingQuery(PartDetails.class, part_query_1.toString(),true);
			if (partDetailsList != null && !partDetailsList.isEmpty() && partDetailsList.size() == 1) {

				PartDetails part_obj = partDetailsList.get(0);
				partLabel = part_obj.getPartLabel();
				log.info("partLabel : {}   partLabel Length : {} ", partLabel, partLabel.length());
				
				convertPartEntityToDTO(part_obj, partDetailsDTO);
				calculateValueAddedTax(partDetailsDTO, dataLibrary);
				
				calculatePriceWithSurcharge(partDetailsDTO, dataLibrary, companyId); // Verkaufspreis ohne MwSt.
				calculatePriceWithTax(part_obj, partDetailsDTO);   // Verkaufspreis mit MwSt.
				calculatePurchasePriceWithVAT(partDetailsDTO);     // Listenpreis mit MwSt.
				//  purchasePrice  is                              // Listenpreis ohne MwSt.
				
				/* 
				* Map<String, String> priceIndicatorMap = executeOMCGETCLPgm(dataLibrary, companyId, agencyId, partDetailsDTO.getWarehouse(), partDetailsDTO.getOem(),
				*		partDetailsDTO.getMarketingCode(), partDetailsDTO.getPartNumber(), partDetailsDTO.getDiscountGroup() );
				*/		
				
				//Comment out COBOL program and now use Java Implementation for BSN477     ALPHAX-4732
				Map<String, String> priceIndicatorMap = businessCasesImpl.newJavaImplementation_BSN477(dataLibrary, schema, partDetailsDTO.getOem(),
						partDetailsDTO.getMarketingCode(),partDetailsDTO.getDiscountGroup(),partDetailsDTO.getOemBrand(), partDetailsDTO.getPartNumber());
				
				if(priceIndicatorMap != null && !priceIndicatorMap.isEmpty()) {				
					partDetailsDTO.setPriceUpdateByMC(priceIndicatorMap.get("priceIndicator"));
				}
				else {
					partDetailsDTO.setPriceUpdateByMC("");
				}

				if(partLabel != null && partLabel.length() == 22){
					
					partLabelDTO = findPartLabelInfo(partLabel, dataLibrary);
					
				}else{
					log.info("Part label information not available");	
				}
				partDetailsDTO.setPartLableInfo(partLabelDTO);
				partDetailsDTO.setContainerSizeFrom(part_obj.getContainerSizeFrom()!=null ? part_obj.getContainerSizeFrom() : "");

			} else {
				if(partDetailsList.size() > 1){
					log.info("More then one records found for this Id in E_ETSTAMM table :" + partId);
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.MULTIPLE_RECORD_MSG_KEY));
					log.error(messageService.getReadableMessage(ExceptionMessages.MULTIPLE_RECORD_MSG_KEY));
					throw exception;
				}
				log.info("Part details not found in E_ETSTAMM table for this Id :" + partId);
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "Part"));
				log.error(messageService.getReadableMessage(ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "Parts"));
				throw exception;
			}

		}catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Parts"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Parts"), exception);
			throw exception;
		}
		return partDetailsDTO;
	}
	
	
	/**
	 * This is method is used to get Lager Detail List based on provided part number from DB.
	 */
	@Override
	public List<LagerPartDetailsDTO> getLagerDetailsBasedOnPart( String partNumber, String dataLibrary, String oem, String warehouseId, String allowedWarehouses, String schema ) {

		log.info("Inside getLagerDetailsBasedOnPart method of PartsServiceImpl");

		List<LagerPartDetailsDTO> lagerPartDetailsDTOList = new ArrayList<>();
		
		//validate the Warehouse Ids
		validateWarehouses(allowedWarehouses);

		try {
			
			oem = oem.equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING: oem;
			
			StringBuilder query = new StringBuilder("Select  et.DTLABG, et.AKTBES, et.SA, et.LNR, (SELECT NAME FROM ");
			query.append(schema).append(".O_WRH WHERE AP_WAREHOUS_ID = et.LNR) AS LagerName from ");
			query.append(dataLibrary).append(".E_ETSTAMM et where et.TNR ='").append(partNumber).append("' and et.HERST ='");
			query.append(oem).append("' AND et.LNR <> ").append(warehouseId).append(" AND et.LNR in (").append(allowedWarehouses).append(")");
			query.append(" order by et.LNR ");

			List<PartDetails> lagersDetailList = dbServiceRepository.getResultUsingQuery(PartDetails.class, query.toString(), true);

			//if the list is not empty
			if (lagersDetailList != null && !lagersDetailList.isEmpty()) {
				
				for (PartDetails partDetail : lagersDetailList) {
					LagerPartDetailsDTO lagerpartDetailsDTO = new LagerPartDetailsDTO();
					
					lagerpartDetailsDTO.setWarehouseNo(String.valueOf(partDetail.getWarehouse()));
					lagerpartDetailsDTO.setWarehouseName(partDetail.getLagerName()==null?"":partDetail.getLagerName());
					lagerpartDetailsDTO.setLastDisposalDate(getDDMMYYYYFormat(partDetail.getLastDisposalDate()));
					lagerpartDetailsDTO.setCurrentStock(String.valueOf(partDetail.getCurrentStock()));
					lagerpartDetailsDTO.setStorageIndikator(String.valueOf(partDetail.getStorageIndikator()));
					
					lagerPartDetailsDTOList.add(lagerpartDetailsDTO);
					
				}
			}
			else {
				log.debug("Lagers Parts List is empty");
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Lager Details"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Lager Details"), exception);
			throw exception;
		}

		return lagerPartDetailsDTOList;
	}
	
	
	private void calculateValueAddedTax(PartDetailsDTO partDetailsDTO, String dataLibrary) {
		
		log.info("Inside calculateValueAddedTax method of PartsServiceImpl");

		try {
			String mwstValue = StringUtils.leftPad(partDetailsDTO.getValueAddedTax(), 2, "0");
			
			StringBuilder query = new StringBuilder("Select left(Datafld, 4) AS TAX from ");
			query.append(dataLibrary).append(".REFERENZ where keyfld = '0000019907").append(mwstValue+"'");

			List<PartDetails> taxDetails = dbServiceRepository.getResultUsingQuery(PartDetails.class, query.toString(), true);
			
			//if the list is not empty
			if(taxDetails != null && !taxDetails.isEmpty()) {
				
				if(taxDetails.get(0).getTaxValue()!=null ) {
					
					Double taxValue = Double.parseDouble(taxDetails.get(0).getTaxValue()) / 100;
					partDetailsDTO.setValueAddedTax(taxValue.toString());
					
				}else {
					partDetailsDTO.setValueAddedTax("");
				}
			}
			
		}
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Tax Details"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Tax Details"), exception);
			throw exception;
		}
	}
	
	
	private void calculatePriceWithTax(PartDetails part_obj, PartDetailsDTO partDetailsDTO){
		
		log.info("Inside calculatePriceWithTax method of PartsServiceImpl");
		
		if(partDetailsDTO.getPurchasePriceWithSurcharge() != null && !partDetailsDTO.getPurchasePriceWithSurcharge().trim().isEmpty() 
				&& !partDetailsDTO.getPurchasePriceWithSurcharge().equals("-")) {

			Double purchasePricewithSurcharge = Double.parseDouble(partDetailsDTO.getPurchasePriceWithSurcharge());

			if(partDetailsDTO.getValueAddedTax()!= null && !partDetailsDTO.getValueAddedTax().trim().isEmpty()) {

				Double percentageValue = Double.parseDouble(partDetailsDTO.getValueAddedTax())/100;
				Double finalSalePrice = (purchasePricewithSurcharge + (percentageValue * purchasePricewithSurcharge));

				partDetailsDTO.setSalesPriceWithVAT(decimalformat_twodigit.format(finalSalePrice));
			}
			else {
				partDetailsDTO.setSalesPriceWithVAT( decimalformat_twodigit.format(purchasePricewithSurcharge));
			}
		}
		else {
			partDetailsDTO.setSalesPriceWithVAT("-");
		}		
	}
	
	
	private void calculatePriceWithSurcharge(PartDetailsDTO partDetailsDTO, String dataLibrary, String companyId) {

		log.info("Inside calculatePriceWithSurcharge method of PartsServiceImpl");
		Integer surchargePosition = 0;
		Integer surchargePercentage = 0;
		Double finalPriceWithsurchange = 0.0;

		try {
			StringBuilder priceLimitQuery = new StringBuilder("SELECT CAST((CAST(substr(DATAFLD, 1, 7) AS REAL)/100) as DEC(7,2)) AS price_limit from ");
			priceLimitQuery.append(dataLibrary).append(".REFERENZ where keyfld like '%2219%' ");

			List<PartDetails> priceLimit = dbServiceRepository.getResultUsingQuery(PartDetails.class, priceLimitQuery.toString(), true);

			Double purchasePrice = Double.parseDouble(partDetailsDTO.getPurchasePrice());

			//if the list is not empty
			if(priceLimit != null && !priceLimit.isEmpty()) {

				if(priceLimit.get(0).getPriceLimit()!=null ) {

					Double priceLimitValue = Double.parseDouble(priceLimit.get(0).getPriceLimit());

					Integer discountGroup = Integer.parseInt(
							(partDetailsDTO.getDiscountGroup()!=null) && !(partDetailsDTO.getDiscountGroup().trim().isEmpty()) ? partDetailsDTO.getDiscountGroup() : "0");
					
					if(purchasePrice > priceLimitValue) {
						if(discountGroup == 0){
							surchargePosition = ((100 - 1) * 9) + 7;
						}else {
							surchargePosition = ((discountGroup - 1) * 9) + 7;
						}						
					}
					else if(purchasePrice <= priceLimitValue) {
						if(discountGroup == 0){
							surchargePosition = ((100 - 1) * 9) + 4;
						}
						else {
							surchargePosition = ((discountGroup - 1) * 9) + 4;
						}
					}

					StringBuilder surchargeQuery = new StringBuilder("SELECT substr(RAB_TBL, ").append(surchargePosition).append(", 3) as surcharge_percentage from ");
					surchargeQuery.append(dataLibrary).append(".F_KUNRAB where KDNR = '99999900' and FZART = 'ET' and Marke = '").append(partDetailsDTO.getOemBrand()).append("' ");
					surchargeQuery.append(" AND FIRMA = ").append(companyId);

					List<PartDetails> surchargePercentageList = dbServiceRepository.getResultUsingQuery(PartDetails.class, surchargeQuery.toString(), true);

					//if the list is not empty
					if(surchargePercentageList != null && !surchargePercentageList.isEmpty()) {

						if(surchargePercentageList.get(0).getSurchargePercentage() != null && !surchargePercentageList.get(0).getSurchargePercentage().trim().isEmpty() ) {
							surchargePercentage = Integer.parseInt(surchargePercentageList.get(0).getSurchargePercentage());

							finalPriceWithsurchange = purchasePrice + ( purchasePrice * surchargePercentage / 1000 );
							
							partDetailsDTO.setPurchasePriceWithSurcharge(decimalformat_twodigit.format(finalPriceWithsurchange));
						}
						else {
							partDetailsDTO.setPurchasePriceWithSurcharge(decimalformat_twodigit.format(purchasePrice));
						}
					}
					else {
						partDetailsDTO.setPurchasePriceWithSurcharge(decimalformat_twodigit.format(purchasePrice));
					}									
				}
			}
			else {
				partDetailsDTO.setPurchasePriceWithSurcharge(decimalformat_twodigit.format(purchasePrice));
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
	}
	
	
	private void calculatePurchasePriceWithVAT( PartDetailsDTO partDetailsDTO ) {
		
		log.info("Inside calculatePurchasePriceWithVAT method of PartsServiceImpl");
		
		if(partDetailsDTO.getPurchasePrice() != null && !partDetailsDTO.getPurchasePrice().trim().isEmpty() 
				&& !partDetailsDTO.getPurchasePrice().equals("-")) {

			Double purchasePrice = Double.parseDouble(partDetailsDTO.getPurchasePrice());

			if(partDetailsDTO.getValueAddedTax()!= null && !partDetailsDTO.getValueAddedTax().trim().isEmpty()) {

				Double percentageValue = Double.parseDouble(partDetailsDTO.getValueAddedTax())/100;
				Double finalSalePrice = ((percentageValue * purchasePrice) + purchasePrice);

				partDetailsDTO.setPurchasePriceWithVAT(decimalformat_twodigit.format(finalSalePrice));
			}
			else {
				partDetailsDTO.setPurchasePriceWithVAT( decimalformat_twodigit.format(purchasePrice));
			}
		}
		else {
			partDetailsDTO.setPurchasePriceWithVAT("-");
		}		
	}
	
	
	/**
	 * This method is used to execute OMCGETCL COBOL program.
	 */
	@Override
	public Map<String, String> executeOMCGETCLPgm(String dataLibrary, String companyId, String agencyId, String warehouseId, String manufacturer, String marketingCode,
			String partNumber, String discountGroup) {
		log.info("Inside executeOMCGETCL method of PartsServiceImpl");

		ProgramParameter[] parmList = new ProgramParameter[13];
		Map<String, String> mcReturnValues =  new HashMap<>();

		companyId =  StringUtils.leftPad(companyId, 2, "0");
		agencyId = StringUtils.leftPad(agencyId, 2, "0");
		String warehouse = StringUtils.leftPad(warehouseId, 2, "0");
		String oem = manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING: manufacturer;
		partNumber = partNumber != null ? partNumber : "";
		discountGroup = discountGroup != null ? discountGroup:"";

		try{

			// Create the input parameters 
			String returnCode = StringUtils.rightPad("", 5, " ");
			parmList[0] = new ProgramParameter(returnCode.getBytes(Program_Commands_Constants.IBM_273), 5);

			String returnMsg  = StringUtils.rightPad("", 100, " ");
			parmList[1] = new ProgramParameter(returnMsg.getBytes(Program_Commands_Constants.IBM_273), 100);

			//LOCID
			String inLocationID = StringUtils.rightPad(companyId+agencyId+warehouse, 10, " ");
			parmList[2] = new ProgramParameter(inLocationID.getBytes(Program_Commands_Constants.IBM_273), 2);

			//HERST
			String inManufacturer = StringUtils.rightPad(oem, 4, " ");
			parmList[3] = new ProgramParameter(inManufacturer.getBytes(Program_Commands_Constants.IBM_273), 2);

			//MCODE
			String inMarketingCode = StringUtils.rightPad(marketingCode, 6, " ");
			parmList[4] = new ProgramParameter(inMarketingCode.getBytes(Program_Commands_Constants.IBM_273), 2);

			//TNR
			String inPartNumber = StringUtils.rightPad(partNumber, 19, " ");
			parmList[5] = new ProgramParameter(inPartNumber.getBytes(Program_Commands_Constants.IBM_273), 2);

			//RG
			String inDiscountGroup = StringUtils.rightPad(discountGroup, 5, " ");
			parmList[6] = new ProgramParameter(inDiscountGroup.getBytes(Program_Commands_Constants.IBM_273), 2);

			//TMARKE
			String outPartBrand = StringUtils.rightPad("", 2, " ");
			parmList[7] = new ProgramParameter(outPartBrand.getBytes(Program_Commands_Constants.IBM_273), 2);

			//LW
			String outDeliveryPlant = StringUtils.rightPad("", 2, " ");
			parmList[8] = new ProgramParameter(outDeliveryPlant.getBytes(Program_Commands_Constants.IBM_273), 2);

			//TA
			String outPartIndicator = StringUtils.rightPad("", 2, " ");
			parmList[9] = new ProgramParameter(outPartIndicator.getBytes(Program_Commands_Constants.IBM_273), 2);

			//LEIART
			String outActivityType = StringUtils.rightPad("", 2, " ");
			parmList[10] = new ProgramParameter(outActivityType.getBytes(Program_Commands_Constants.IBM_273), 2);

			//PRKZ
			String outPriceIndicator = StringUtils.rightPad("", 1, " ");
			parmList[11] = new ProgramParameter(outPriceIndicator.getBytes(Program_Commands_Constants.IBM_273), 2);

			//BUKONT
			String outGoodsAssigmntAcc = StringUtils.rightPad("", 8, " ");
			parmList[12] = new ProgramParameter(outGoodsAssigmntAcc.getBytes(Program_Commands_Constants.IBM_273), 2);

			//execute the COBOL program - OMCGETCL.PGM
			List<String> outputList = cobolServiceRepository.executeProgram(parmList, 0, Program_Commands_Constants.OMCGET_PROGRAM );

			if(outputList != null && !outputList.isEmpty()) {
				if (outputList.get(0).trim().equalsIgnoreCase("00000")) {
					
					mcReturnValues.put("returnCode", outputList.get(0).trim());
					mcReturnValues.put("returnMsg", outputList.get(1).trim());
					mcReturnValues.put("partBrand", outputList.get(7).trim());
					mcReturnValues.put("deliveryPlant", outputList.get(8).trim());
					mcReturnValues.put("partIndicator", outputList.get(9).trim());
					mcReturnValues.put("activityType", outputList.get(10).trim());
					mcReturnValues.put("priceIndicator", outputList.get(11).trim());
					mcReturnValues.put("goodsAssignAccount", outputList.get(12).trim());

				}
				else {
					mcReturnValues.put("returnCode", outputList.get(0).trim());
					mcReturnValues.put("returnMsg", outputList.get(1).trim());
					mcReturnValues.put("partBrand", "");
					mcReturnValues.put("deliveryPlant", "");
					mcReturnValues.put("partIndicator", "");
					mcReturnValues.put("activityType", "");
					mcReturnValues.put("priceIndicator", "");
					mcReturnValues.put("goodsAssignAccount", "");
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.PROGRAM_FAILED_MSG_KEY, "OMCGETCL"));
			log.error(messageService.getReadableMessage(ExceptionMessages.PROGRAM_FAILED_MSG_KEY, "OMCGETCL"), exception);
			throw exception;
		}

		//return map values
		return mcReturnValues;
	}
	
	
	private PartLabelDTO findPartLabelInfo(String partLabel, String dataLibrary){
		
		List<PartLabel> partLabelList = new ArrayList<>();
		PartLabelDTO partLabelDTO = new PartLabelDTO();

		String wart = partLabel.substring(1,2);
		String kat = partLabel.substring(2,3);
		String label = partLabel.substring(3,4);
		String wart1 = partLabel.substring(4,6);
		String wart2 = partLabel.substring(6,8);
		String wart3 = partLabel.substring(8,10);
		String wart4 = partLabel.substring(10,12);
		String wart5 = partLabel.substring(12,14);
		String wart6 = partLabel.substring(14,16);
		String wart7 = partLabel.substring(16,18);
//		String wart8 = partLabel.substring(18,20);
//		String wart9 = partLabel.substring(20,22);

		StringBuilder label_query = new StringBuilder(" SELECT LABEL,KAT,WERT,TEXT FROM ");
		label_query.append(dataLibrary).append(".E_ETLABEL WHERE ");

		StringBuilder label_name_query = new StringBuilder().append(label_query).append(" LABEL = ").append(label).append(" AND KAT= ").append(kat).append(" AND WERT = ").append(wart);

		//StringBuilder heading1_query = label_query.append(" LABEL = ").append(label).append(" KAT= ").append("1").append(" WERT = ").append(wart);
		StringBuilder heading1_value_query = new StringBuilder().append(label_query).append(" LABEL = ").append(label).append(" AND KAT= ").append("1").append(" AND WERT IN( ").append(wart+",").append(wart1+")");

		//StringBuilder heading2_query = label_query.append(" LABEL = ").append(label).append(" KAT= ").append("2").append(" WERT = ").append(wart);
		StringBuilder heading2_value_query = new StringBuilder().append(label_query).append(" LABEL = ").append(label).append(" AND KAT= ").append("2").append(" AND WERT IN( ").append(wart+",").append(wart2+")");

		//StringBuilder heading3_query = label_query.append(" LABEL = ").append(label).append(" KAT= ").append("3").append(" WERT = ").append(wart);
		StringBuilder heading3_value_query = new StringBuilder().append(label_query).append(" LABEL = ").append(label).append(" AND KAT= ").append("3").append(" AND WERT IN( ").append(wart+",").append(wart3+")");

		//StringBuilder heading4_query = label_query.append(" LABEL = ").append(label).append(" KAT= ").append("4").append(" WERT = ").append(wart);
		StringBuilder heading4_value_query = new StringBuilder().append(label_query).append(" LABEL = ").append(label).append(" AND KAT= ").append("4").append(" AND WERT IN( ").append(wart+",").append(wart4+")");

		//StringBuilder heading5_query = label_query.append(" LABEL = ").append(label).append(" KAT= ").append("5").append(" WERT = ").append(wart);
		StringBuilder heading5_value_query = new StringBuilder().append(label_query).append(" LABEL = ").append(label).append(" AND KAT= ").append("5").append(" AND WERT IN( ").append(wart+",").append(wart5+")");

		StringBuilder heading6_value_query = new StringBuilder().append(label_query).append(" LABEL = ").append(label).append(" AND KAT= ").append("6").append(" AND WERT IN( ").append(wart+",").append(wart6+")");

		StringBuilder heading7_value_query = new StringBuilder().append(label_query).append(" LABEL = ").append(label).append(" AND KAT= ").append("7").append(" AND WERT IN( ").append(wart+",").append(wart7+")");

//		StringBuilder heading8_value_query = new StringBuilder().append(label_query).append(" LABEL = ").append(label).append(" AND KAT= ").append("8").append(" AND WERT IN( ").append(wart+",").append(wart8+")");

//		StringBuilder heading9_value_query = new StringBuilder().append(label_query).append(" LABEL = ").append(label).append(" AND KAT= ").append("9").append(" AND WERT IN( ").append(wart+",").append(wart9+")");

		partLabelDTO.setLabel("0"+label);
		partLabelList = dbServiceRepository.getResultUsingQuery(PartLabel.class, label_name_query.toString(),true);
		if (partLabelList != null && !partLabelList.isEmpty() && partLabelList.size() == 1) {

			for (PartLabel part_obj : partLabelList) {
				partLabelDTO.setName(part_obj.getLabelHeadingValue());
				partLabelList = null;
			}
		}else{
			log.info("More then one label name found");	
		}

		if(!wart1.equalsIgnoreCase("00")){
			partLabelList = dbServiceRepository.getResultUsingQuery(PartLabel.class, heading1_value_query.toString(),true);	
		}
		if (partLabelList != null && !partLabelList.isEmpty()) {

			for (PartLabel part_obj : partLabelList) {
				if(part_obj.getWert().equalsIgnoreCase(wart)){
					partLabelDTO.setHeadline1(part_obj.getLabelHeadingValue());	
				}else{
					partLabelDTO.setHeadlineValue1(part_obj.getLabelHeadingValue());	
				}
				partLabelList = null;
			}
		}

		if(!wart2.equalsIgnoreCase("00")){
			partLabelList = dbServiceRepository.getResultUsingQuery(PartLabel.class, heading2_value_query.toString(),true);
		}
		if (partLabelList != null && !partLabelList.isEmpty()) {

			for (PartLabel part_obj : partLabelList) {
				if(part_obj.getWert().equalsIgnoreCase(wart)){
					partLabelDTO.setHeadline2(part_obj.getLabelHeadingValue());	
				}else{
					partLabelDTO.setHeadlineValue2(part_obj.getLabelHeadingValue());	
				}
				partLabelList = null;
			}
		}

		if(!wart3.equalsIgnoreCase("00")){
			partLabelList = dbServiceRepository.getResultUsingQuery(PartLabel.class, heading3_value_query.toString(),true);
		}
		if (partLabelList != null && !partLabelList.isEmpty()) {

			for (PartLabel part_obj : partLabelList) {
				if(part_obj.getWert().equalsIgnoreCase(wart)){
					partLabelDTO.setHeadline3(part_obj.getLabelHeadingValue());	
				}else{
					partLabelDTO.setHeadlineValue3(part_obj.getLabelHeadingValue());	
				}
				partLabelList = null;
			}
		}

		if(!wart4.equalsIgnoreCase("00")){
			partLabelList = dbServiceRepository.getResultUsingQuery(PartLabel.class, heading4_value_query.toString(),true);
		}
		if (partLabelList != null && !partLabelList.isEmpty()) {

			for (PartLabel part_obj : partLabelList) {
				if(part_obj.getWert().equalsIgnoreCase(wart)){
					partLabelDTO.setHeadline4(part_obj.getLabelHeadingValue());	
				}else{
					partLabelDTO.setHeadlineValue4(part_obj.getLabelHeadingValue());	
				}
				partLabelList = null;
			}
		}

		if(!wart5.equalsIgnoreCase("00")){
			partLabelList = dbServiceRepository.getResultUsingQuery(PartLabel.class, heading5_value_query.toString(),true);
		}
		if (partLabelList != null && !partLabelList.isEmpty()) {

			for (PartLabel part_obj : partLabelList) {
				if(part_obj.getWert().equalsIgnoreCase(wart)){
					partLabelDTO.setHeadline5(part_obj.getLabelHeadingValue());	
				}else{
					partLabelDTO.setHeadlineValue5(part_obj.getLabelHeadingValue());	
				}
				partLabelList = null;
			}
		}

//		if(!wart6.equalsIgnoreCase("00")){
			partLabelList = dbServiceRepository.getResultUsingQuery(PartLabel.class, heading6_value_query.toString(),true);
//		}
		if (partLabelList != null && !partLabelList.isEmpty()) {

			for (PartLabel part_obj : partLabelList) {
				if(part_obj.getWert().equalsIgnoreCase(wart)){
					partLabelDTO.setHeadline6(part_obj.getLabelHeadingValue());	
				}else{
					partLabelDTO.setHeadlineValue6(part_obj.getLabelHeadingValue());	
				}
				partLabelList = null;
			}
		}

//		if(!wart7.equalsIgnoreCase("00")){
			partLabelList = dbServiceRepository.getResultUsingQuery(PartLabel.class, heading7_value_query.toString(),true);
//		}
		if (partLabelList != null && !partLabelList.isEmpty()) {

			for (PartLabel part_obj : partLabelList) {
				if(part_obj.getWert().equalsIgnoreCase(wart)){
					partLabelDTO.setHeadline7(part_obj.getLabelHeadingValue());	
				}else{
					partLabelDTO.setHeadlineValue7(part_obj.getLabelHeadingValue());	
				}
				partLabelList = null;
			}
		}

/*		if(!wart8.equalsIgnoreCase("00")){
			partLabelList = dbServiceRepository.getResultUsingQuery(PartLabel.class, heading8_value_query.toString(),true);
		}
		if (partLabelList != null && !partLabelList.isEmpty()) {

			for (PartLabel part_obj : partLabelList) {
				if(part_obj.getWert().equalsIgnoreCase(wart)){
					partLabelDTO.setHeadline8(part_obj.getLabelHeadingValue());	
				}else{
					partLabelDTO.setHeadlineValue8(part_obj.getLabelHeadingValue());	
				}
				partLabelList = null;
			}
		}

		if(!wart9.equalsIgnoreCase("00")){
			partLabelList = dbServiceRepository.getResultUsingQuery(PartLabel.class, heading9_value_query.toString(),true);
		}
		if (partLabelList != null && !partLabelList.isEmpty()) {

			for (PartLabel part_obj : partLabelList) {
				if(part_obj.getWert().equalsIgnoreCase(wart)){
					partLabelDTO.setHeadline9(part_obj.getLabelHeadingValue());	
				}else{
					partLabelDTO.setHeadlineValue9(part_obj.getLabelHeadingValue());	
				}
				partLabelList = null;
			}
		}
*/		
		return partLabelDTO;
		
	}
	
	
	/**
	 * This is method is used to get Movement Detail List based on provided part number from DB.
	 */
	@Override
	public GlobalSearch getMovementDataUsingPartNumber(String partId, String dataLibrary, String companyId, 
			String agencyId, String warehouseId, String oem, String flag, String pageSize, String pageNumber) {

		log.info("Inside getMovementDataUsingPartNumber method of PartsServiceImpl");

		GlobalSearch globalSearchList = new GlobalSearch();
		StringBuilder query = new StringBuilder();

		try {

			//validate the company Id 
			validateCompany(companyId);

			//validate the agency Id
			validateAgency(agencyId);

			String compID =  StringUtils.stripStart(companyId, "0");
			String agencyID = StringUtils.stripStart(agencyId, "0");
			log.info("Company ID  xx:  {}  and  Agency ID yy:  {}", compID, agencyID);

			if(pageSize==null || pageNumber==null || pageSize.isEmpty() || pageNumber.isEmpty()){
				pageSize = RestInputConstants.DEFAULT_PAGE_SIZE;
				pageNumber = RestInputConstants.DEFAULT_PAGE_NO;
			}

			int totalRecords = Integer.parseInt(pageSize);
			int nextRows = totalRecords * (Integer.parseInt(pageNumber) - 1);

			//validate the page size
			validatePageSize(totalRecords);

			log.info("PageSize  : {}  OFFSET Size (Skip Rows):  {} Page Number  : {} ", pageSize, nextRows, pageNumber);

			oem = oem.equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING: oem;

			if(flag.equalsIgnoreCase("all")){

				query = queryForAllBAMovement(partId, dataLibrary, compID, agencyID, warehouseId, oem, pageSize, nextRows);
			}
			else if(flag.equalsIgnoreCase("sale")) {

				query.append("Select HERST, VFNR, BART, BELNR AS AUFTRAGSNUMMER, BSN_LC, JJMMTT, HHMMSS, t3 as BHW, CAST(SUBSTR(T1, 3, 6) AS REAL)/10 as MENGE, substr(T4,11,6) AS KUNDENNUMMER, ");
				query.append("(select NAME from ").append(dataLibrary).append(".M1_KDDATK1 where KDNR1= substr(T4,11,6) fetch first row only ) as KUNDE, ");
				query.append("(select NAME1 from ").append(dataLibrary).append(".E_ETSTAMK6 where KEY2= BSN_LC ) AS LIEFERANTENNAME, ");
				query.append("(Select COUNT(*) from ").append(dataLibrary).append(".E_CPSDAT where (BART=20 or BART=22) "); //and FIRMA= ").append(compID);
				query.append(" and VFNR=").append(warehouseId).append(" and HERST='").append(oem).append("' and ");
				query.append(" TRIM(KB)=TRIM(substr('").append(partId).append("',1,1)) and TRIM(ETNR)=TRIM(substr('").append(partId).append("', 2, 10)) ");
				query.append(" and TRIM(ES1)=TRIM(substr('").append(partId).append("', 14,2)) and TRIM(ES2)=TRIM(substr('").append(partId).append("', 16,4)) ");
				query.append(" ) AS ROWNUMER  from ");
				query.append(dataLibrary).append(".E_CPSDAT where (BART=20 or BART=22) "); //and FIRMA= ").append(compID);
				query.append(" and VFNR=").append(warehouseId).append(" and HERST='").append(oem);
				query.append("' and TRIM(KB)=TRIM(substr('").append(partId).append("',1,1)) and TRIM(ETNR)=TRIM(substr('").append(partId).append("', 2, 10)) ");
				query.append(" and TRIM(ES1)=TRIM(substr('").append(partId).append("', 14,2)) and TRIM(ES2)=TRIM(substr('").append(partId).append("', 16,4)) ");
				query.append(" order by JJMMTT desc, HHMMSS desc OFFSET ");
				query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");
			}
			else if(flag.equalsIgnoreCase("purchase")) {

				query.append("Select HERST, VFNR, BART, BELNR AS AUFTRAGSNUMMER, BSN_LC, JJMMTT, HHMMSS, t3 as BHW, CAST(SUBSTR(T1, 3, 6) AS REAL)/10 as MENGE, ");
				query.append("(select NAME from ").append(dataLibrary).append(".M1_KDDATK1 where KDNR1= substr(T4,11,6) fetch first row only ) as KUNDE, ");
				query.append("(select NAME1 from ").append(dataLibrary).append(".E_ETSTAMK6 where KEY2= BSN_LC ) AS LIEFERANTENNAME, ");
				query.append("(Select COUNT(*) from ").append(dataLibrary).append(".E_CPSDAT where (BART=1 or BART=2) "); //and FIRMA= ").append(compID);
				query.append(" and VFNR=").append(warehouseId).append(" and HERST='").append(oem).append("' and ");
				query.append(" TRIM(KB)=TRIM(substr('").append(partId).append("',1,1)) and TRIM(ETNR)=TRIM(substr('").append(partId).append("', 2, 10)) ");
				query.append(" and TRIM(ES1)=TRIM(substr('").append(partId).append("', 14,2)) and TRIM(ES2)=TRIM(substr('").append(partId).append("', 16,4)) ");
				query.append(" ) AS ROWNUMER  from ");
				query.append(dataLibrary).append(".E_CPSDAT where (BART=1 or BART=2) "); //and FIRMA= ").append(compID);
				query.append(" and VFNR=").append(warehouseId).append(" and HERST='").append(oem);
				query.append("' and TRIM(KB)=TRIM(substr('").append(partId).append("',1,1)) and TRIM(ETNR)=TRIM(substr('").append(partId).append("', 2, 10)) ");
				query.append(" and TRIM(ES1)=TRIM(substr('").append(partId).append("', 14,2)) and TRIM(ES2)=TRIM(substr('").append(partId).append("', 16,4)) ");
				query.append(" order by JJMMTT desc, HHMMSS desc OFFSET ");
				query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");
			}
			else {
				log.info("Flag value is not valid");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.INVALID_FLAG_VALUE_MSG_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.INVALID_FLAG_VALUE_MSG_KEY));
				throw exception;
			}

			List<MovementData> movementDataList = dbServiceRepository.getResultUsingQuery(MovementData.class, query.toString(), true);

			List<MovementDataDTO> movementDataDTOList = new ArrayList<>();

			//if the list is not empty
			if (movementDataList != null && !movementDataList.isEmpty()) {

				for (MovementData movementData : movementDataList) {

					MovementDataDTO movementDataDTO = new MovementDataDTO();

					movementDataDTO.setManufacturer(movementData.getManufacturer().equalsIgnoreCase(RestInputConstants.DCAG_STRING)? RestInputConstants.DAG_STRING: movementData.getManufacturer());
					movementDataDTO.setWarehouse(String.valueOf(movementData.getWarehouse()));
					movementDataDTO.setBusinesscase(String.valueOf(movementData.getBusinesscase()));
					movementDataDTO.setDocumentNumber(movementData.getDocumentNumber());
					movementDataDTO.setSupplier(movementData.getSupplier());
					movementDataDTO.setSupplierName(movementData.getSupplierName());
					movementDataDTO.setDate(getSixDigitDateFormat(movementData.getDate()));
					movementDataDTO.setTime(convertTimeToString(String.valueOf(movementData.getTime())));
					movementDataDTO.setOrderNumber(movementData.getOrderNumber());
					movementDataDTO.setBhw(movementData.getBhw());
					movementDataDTO.setQuantity(String.valueOf(movementData.getQuantity()));
					movementDataDTO.setCustomerNumber(movementData.getCustomerNumber());
					movementDataDTO.setCustomerName(movementData.getCustomerName());

					movementDataDTO.setMa_Changed(String.valueOf(movementData.getMa_Changed()));					
					movementDataDTO.setMa_Old(movementData.getMa_Old());					
					movementDataDTO.setMa_New(movementData.getMa_New());

					movementDataDTO.setRg_Changed(String.valueOf(movementData.getRg_Changed()));					
					movementDataDTO.setRg_Old(movementData.getRg_Old());					
					movementDataDTO.setRg_New(movementData.getRg_New());

					movementDataDTO.setTa_Changed(String.valueOf(movementData.getTa_Changed()));					
					movementDataDTO.setTa_Old(movementData.getTa_Old());					
					movementDataDTO.setTa_New(movementData.getTa_New());

					movementDataDTO.setLa_Changed(String.valueOf(movementData.getLa_Changed()));					
					movementDataDTO.setLa_Old(String.valueOf(movementData.getLa_Old()));					
					movementDataDTO.setLa_New(String.valueOf(movementData.getLa_New()));

					movementDataDTO.setMc_Changed(String.valueOf(movementData.getMc_Changed()));					
					movementDataDTO.setMc_Old(movementData.getMc_Old());					
					movementDataDTO.setMc_New(movementData.getMc_New());

					movementDataDTO.setKto_Changed(String.valueOf(movementData.getKto_Changed()));					
					movementDataDTO.setKto_Old(String.valueOf(movementData.getKto_Old()));					
					movementDataDTO.setKto_New(String.valueOf(movementData.getKto_New()));

					movementDataDTO.setEpr_Changed(String.valueOf(movementData.getEpr_Changed()));					
					movementDataDTO.setEpr_Old(String.valueOf(movementData.getEpr_Old()));					
					movementDataDTO.setEpr_New(String.valueOf(movementData.getEpr_New()));

					movementDataDTO.setNpr_Changed(String.valueOf(movementData.getNpr_Changed()));					
					movementDataDTO.setNpr_Old(movementData.getNpr_Old());					
					movementDataDTO.setNpr_New(movementData.getNpr_New());

					movementDataDTO.setEknpr_Changed(String.valueOf(movementData.getEknpr_Changed()));						
					movementDataDTO.setEknpr_Old(String.valueOf(movementData.getEknpr_Old()));						
					movementDataDTO.setEknpr_New(String.valueOf(movementData.getEknpr_New()));

					movementDataDTO.setDak_Changed(String.valueOf(movementData.getDak_Changed()));						
					movementDataDTO.setDak_Old(movementData.getDak_Old());						
					movementDataDTO.setDak_New(movementData.getDak_New());					

					movementDataDTOList.add(movementDataDTO);

				}
				globalSearchList.setSearchDetailsList(movementDataDTOList);
				globalSearchList.setTotalPages(Integer.toString(movementDataList.get(0).getTotalCount()));
				globalSearchList.setTotalRecordCnt(Integer.toString(movementDataList.get(0).getTotalCount()));
			}
			else {
				globalSearchList.setSearchDetailsList(movementDataDTOList);
				globalSearchList.setTotalPages(Integer.toString(0));
				globalSearchList.setTotalRecordCnt(Integer.toString(0));
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "MovementData (Ereignisdaten)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "MovementData (Ereignisdaten)"), exception);
			throw exception;
		}

		return globalSearchList;
	}
	
	
	private StringBuilder queryForAllBAMovement(String partId, String dataLibrary, String compID, String agencyID, String warehouseId, 
			String oem, String pageSize, int pageNumber) {

		StringBuilder query = new StringBuilder("Select HERST, VFNR, BART, JJMMTT, HHMMSS, ");
		query.append("CASE  WHEN (BART=17 OR (BART>=40 AND BART <=50)) THEN 0  ELSE  CAST(SUBSTR(T1, 3, 6) AS REAL)/10  END  AS MENGE, ");
		query.append("CASE  WHEN (BART=20 OR BART=22) THEN BELNR  ELSE  '-----'  END AS AUFTRAGSNUMMER, ");
		query.append("CASE  WHEN (BART=1 OR BART=2 OR BART=6 OR BART=67 OR BART=5 OR BART=40 OR BART=25 )  THEN BELNR  ELSE  '-----'  END AS BELEGNUMMER, ");
		query.append("CASE  WHEN (BART=5 OR BART=20 OR BART=22) THEN  (select NAME from ");
		query.append(dataLibrary).append(".M1_KDDATK1 where KDNR1=substr(T4,11,6) fetch first row only )  ELSE  '-----'  END AS KUNDE, ");
		query.append("CASE  WHEN (BART=1 OR BART=2)  THEN  Substr(T3,7,12)  ELSE  '-----'  END AS BHW, ");

		query.append("CASE  WHEN (BART >= 40 AND BART <50)  THEN ");
		query.append("CASE  WHEN  MARKEV=MARKEN  THEN  0  ELSE  1 END ");
		query.append("ELSE  0  END AS  MA_CHANGED, MARKEV AS MA_OLD, MARKEN AS MA_NEW, ");

		query.append("CASE  WHEN (BART >= 40 AND BART <50)  THEN ");
		query.append("CASE  WHEN  RABGRV=RABGRN  THEN  0  ELSE  1  END ");
		query.append("ELSE  0  END AS  RG_CHANGED, RABGRV AS RG_OLD, RABGRN AS RG_NEW, ");

		query.append("CASE  WHEN (BART >= 40 AND BART <50)  THEN ");		
		query.append("CASE  WHEN  TA_EWV=TA_EWN  THEN  0  ELSE  1  END ");
		query.append("ELSE  0  END AS  TA_CHANGED, TA_EWV AS TA_OLD, TA_EWN AS TA_NEW, ");

		query.append("CASE  WHEN (BART >= 40 AND BART <50)  THEN ");
		query.append("CASE  WHEN  LEIARTV=LEIARTN  THEN  0  ELSE  1  END ");
		query.append("ELSE  0  END AS  LA_CHANGED, LEIARTV AS LA_OLD, LEIARTN AS LA_NEW, ");

		query.append("CASE  WHEN (BART >= 40 AND BART <50)  THEN ");
		query.append("CASE  WHEN  (MCODEV=MCODE OR MCODEV='')  THEN  0  ELSE  1  END ");
		query.append("ELSE  0  END AS  MC_CHANGED, MCODEV AS MC_OLD, MCODE AS MC_NEW, ");

		query.append("CASE  WHEN (BART >= 40 AND BART <50)  THEN ");
		query.append("CASE  WHEN  BUK_KONTOV=BUK_KONTON  THEN  0  ELSE  1  END ");
		query.append("ELSE  0  END AS  KTO_CHANGED, BUK_KONTOV AS KTO_OLD, BUK_KONTON AS KTO_NEW, ");

		query.append("CASE  WHEN (BART >= 40 AND BART <50)  THEN ");
		query.append("CASE  WHEN  EPRV=EPRN  THEN  0  ELSE  1  END ");
		query.append("ELSE  0  END AS  EPR_CHANGED, EPRV AS EPR_OLD, EPRN AS EPR_NEW, ");

		query.append("CASE  WHEN (BART >= 40 AND BART <50)  THEN ");
		query.append("CASE  WHEN  NT_EWV=NT_EWN  THEN  0  ELSE  1  END ");
		query.append("ELSE  0  END AS  NPR_CHANGED, NT_EWV AS NPR_OLD, NT_EWN AS NPR_NEW, ");

		query.append("CASE  WHEN (BART >= 40 AND BART <50)  THEN ");
		query.append("CASE  WHEN  EKNPRV=EKNPR  THEN  0  ELSE  1  END ");
		query.append("ELSE  0  END AS  EKNPR_CHANGED,EKNPRV AS EKNPR_OLD,EKNPR AS EKNPR_NEW, ");

		query.append("CASE  WHEN (BART >= 40 AND BART <50)  THEN ");
		query.append("CASE  WHEN  DAKEWV=DAKEWN  THEN  0  ELSE  1  END ");
		query.append("ELSE  0  END AS  DAK_CHANGED, DAKEWV AS DAK_OLD, DAKEWN AS DAK_NEW, ");	

		query.append("(Select COUNT(*) from ").append(dataLibrary).append(".E_CPSDAT where (NOT BART=7 AND NOT BART=43) "); //and FIRMA= ").append(compID);
		query.append(" and VFNR=").append(warehouseId).append(" and HERST='").append(oem).append("' and ");
		query.append(" TRIM(KB)=TRIM(substr('").append(partId).append("',1,1)) and TRIM(ETNR)=TRIM(substr('").append(partId).append("', 2, 10)) ");
		query.append(" and TRIM(ES1)=TRIM(substr('").append(partId).append("', 14,2)) and TRIM(ES2)=TRIM(substr('").append(partId).append("', 16, 4)) ");
		query.append(" ) AS ROWNUMER  from ");
		query.append(dataLibrary).append(".E_CPSDAT where (NOT BART=7 AND NOT BART=43) "); //and FIRMA= ").append(compID);
		query.append(" and VFNR=").append(warehouseId).append(" and HERST='").append(oem);
		query.append("' and TRIM(KB)=TRIM(substr('").append(partId).append("',1,1)) and TRIM(ETNR)=TRIM(substr('").append(partId).append("', 2, 10)) ");
		query.append(" and TRIM(ES1)=TRIM(substr('").append(partId).append("', 14,2)) and TRIM(ES2)=TRIM(substr('").append(partId).append("', 16,4)) ");
		query.append(" order by JJMMTT desc, HHMMSS desc OFFSET ");
		query.append(String.valueOf(pageNumber)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY "); 

		return query;
	}
	
	
	private String getSixDigitDateFormat(BigDecimal dateValue) {

		StringBuilder dateBuilder = new StringBuilder();

		if (dateValue != null) {
			String dateFormat = String.valueOf(dateValue);
			if (dateFormat.length() == 6) {

				if (Integer.parseInt(dateFormat.substring(0, 2)) > 50) {
					dateBuilder.append(dateFormat.substring(4,6)).append("/").append(dateFormat.substring(2, 4));
					dateBuilder.append("/19").append(dateFormat.substring(0, 2));
				} else {
					dateBuilder.append(dateFormat.substring(4,6)).append("/").append(dateFormat.substring(2, 4));
					dateBuilder.append("/20").append(dateFormat.substring(0, 2));
				}
			}
		}
		return dateBuilder.toString();
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
	 * This method is used to get the LOPA (Storage Location) details based on provided Part number.
	 */
	@Override
	public List<StorageLocationDTO> getStorageLocationList( String partNumber, String dataLibrary, String warehouseId, String oem) {

		log.info("Inside getStorageLocationList method of PartsServiceImpl");

		List<StorageLocationDTO> storageLocationDTOList = new ArrayList<>();

		try {
			
			oem = oem.equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING: oem;
			
			StringBuilder query = new StringBuilder("Select LOPA, PRIO from ");
			query.append(dataLibrary).append(".E_LAGO where TNR='").append(partNumber).append("' and HERST='").append(oem).append("' and LNR=").append(warehouseId);
			query.append("  order by PRIO ASC ");

			List<StorageLocation> lopaDetailList = dbServiceRepository.getResultUsingQuery(StorageLocation.class, query.toString(), true);

			//if the list is not empty
			if (lopaDetailList != null && !lopaDetailList.isEmpty()) {
				
				for (StorageLocation storageLocation : lopaDetailList) {
					StorageLocationDTO locationDTO = new StorageLocationDTO();
					
					locationDTO.setStorageLocation(storageLocation.getStorageLocation());
					locationDTO.setPriority(String.valueOf(storageLocation.getPriority()));
					
					storageLocationDTOList.add(locationDTO);
				}
			}
			else {
				log.debug("LOPA List is empty");
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

		return storageLocationDTOList;
	}
	
	
	/**
	 * This method is used to Create / Update the Parts using BA4X.
	 */
	@Override
	public Map<String, Map<String,String>> createUpdateParts(BAWrapperDTO baWrapperDTO, String dataLibrary, String companyId, String agencyId, String schema) {

		log.info("Inside createUpdateParts method of PartsServiceImpl");

		Map<String, Map<String,String>> response = new HashMap<String, Map<String,String>>(); 

		try {

			if(baWrapperDTO.getBa40Object() != null && baWrapperDTO.getBa40Object().getIsBAChange()) {
				baWrapperDTO.getBa40Object().setBusinessCases("40");

				//encode the label value using heading values
				baWrapperDTO.getBa40Object().setLabelEU(retriveLabelString(baWrapperDTO.getBa40Object(), dataLibrary));

				Map<String, String> ba40Response = businessCasesImpl.updateMasterDataBA(baWrapperDTO.getBa40Object(), dataLibrary, companyId, agencyId);

				//check BA40 pgm is successful.
				if(ba40Response.get("returnCode").equalsIgnoreCase("00000")) {
					//check isLOPAChange flag is true.
					if(baWrapperDTO.getBa40Object().getIsLOPAChange()) {

						StorageLocationDTO storageLocationDTO = new StorageLocationDTO();
						storageLocationDTO.setPriority("1");
						storageLocationDTO.setStorageLocation(baWrapperDTO.getBa40Object().getStorageLocation());

						String manufacturer = baWrapperDTO.getBa40Object().getManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING)? 
								RestInputConstants.DCAG_STRING : baWrapperDTO.getBa40Object().getManufacturer();

						//update the E_LAGO table
						updateStorageLocations(storageLocationDTO, dataLibrary, baWrapperDTO.getBa40Object().getPartNumber(), 
								baWrapperDTO.getBa40Object().getWarehouseNumber(), manufacturer);
					}
				}
				
				if(baWrapperDTO.getBa40Object().getContainerSizeFrom() != null && !baWrapperDTO.getBa40Object().getContainerSizeFrom().isEmpty()) {
					
					log.info("createUpdate container size value");
					
					 String manufacturer = baWrapperDTO.getBa40Object().getManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING : baWrapperDTO.getBa40Object().getManufacturer();
					
					  StringBuilder query = new StringBuilder("MERGE INTO ").append(schema).append(".O_PARTTRAN AS A ");
					  query.append("USING (VALUES ( '").append(baWrapperDTO.getBa40Object().getPartNumber()).append("',").append(baWrapperDTO.getBa40Object().getContainerSizeFrom());
					  query.append(", ").append(baWrapperDTO.getBa40Object().getWarehouseNumber()).append(", '").append(manufacturer).append("' ,0,0,0 )) AS A_TMP "); 
					  query.append("(PARTNUMBER_FROM, CONTAINER_SIZE_FROM, WAREHOUSE, OEM, CONTAINER_SIZE_TO, ACTIVATED, DISCOUNT ) "); 
					  query.append("ON A.PARTNUMBER_FROM = A_TMP.PARTNUMBER_FROM AND A.WAREHOUSE= A_TMP.WAREHOUSE AND A.OEM = A_TMP.OEM ");
					  
					  query.append(" WHEN MATCHED THEN ");
					  query.append("UPDATE SET CONTAINER_SIZE_FROM = ").append(baWrapperDTO.getBa40Object().getContainerSizeFrom());
					  
					  query.append(" WHEN NOT MATCHED THEN "); 
					  query.append("INSERT (PARTNUMBER_FROM, CONTAINER_SIZE_FROM, WAREHOUSE, OEM, CONTAINER_SIZE_TO, ACTIVATED, DISCOUNT) "); 
					  query.append("VALUES ( '").append(baWrapperDTO.getBa40Object().getPartNumber()).append("',").append(baWrapperDTO.getBa40Object().getContainerSizeFrom());
					  query.append(", ").append(baWrapperDTO.getBa40Object().getWarehouseNumber()).append(", '").append(manufacturer).append("', 0,0,0 ) ");
					  
					  query.append(" ELSE IGNORE");
					  
					  dbServiceRepository.insertResultUsingQuery(query.toString());
					 
				}
				
				response.put("ba40Response", ba40Response);
			}
			if(baWrapperDTO.getBa44Object() != null && baWrapperDTO.getBa44Object().getIsBAChange()) {
				baWrapperDTO.getBa44Object().setBusinessCases("44");

				Map<String, String> ba44Response = businessCasesImpl.updateMasterDataBA(baWrapperDTO.getBa44Object(), dataLibrary, companyId, agencyId);
				response.put("ba44Response", ba44Response);
			}
			if(baWrapperDTO.getBa45Object() != null && baWrapperDTO.getBa45Object().getIsBAChange()) {
				baWrapperDTO.getBa45Object().setBusinessCases("45");

				Map<String, String> ba45Response = businessCasesImpl.updateMasterDataBA(baWrapperDTO.getBa45Object(), dataLibrary, companyId, agencyId);
				response.put("ba45Response", ba45Response);
			}
			if(baWrapperDTO.getBa49Object() != null && baWrapperDTO.getBa49Object().getIsBAChange()) {
				baWrapperDTO.getBa49Object().setBusinessCases("49");

				Map<String, String> ba49Response = businessCasesImpl.updateMasterDataBA(baWrapperDTO.getBa49Object(), dataLibrary, companyId, agencyId);
				response.put("ba49Response", ba49Response);
			}
			if(baWrapperDTO.getBa09Object() != null && baWrapperDTO.getBa09Object().getIsBAChange()) {
				baWrapperDTO.getBa09Object().setBusinessCases("09");

				Map<String, String> ba09Response = businessCasesImpl.updateDeparturesBA(baWrapperDTO.getBa09Object(), dataLibrary, companyId, agencyId);

				response.put("ba09Response", ba09Response);
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Parts"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Parts"), exception);
			throw exception;
		}
		return response;

	}
	
	/**
	 * This method is used to retrieve the label from each individual headings.
	 * @param ba40Object
	 * @param dataLibrary
	 * @return
	 */
	private String retriveLabelString(MasterDataBA_DTO ba40Object, String dataLibrary) {
		
		List<PartLabel> partLabelList = new ArrayList<>();
		String heading1 = "";
		String heading2 = "";
		String heading3 = "";
		String heading4 = "";
		String heading5 = "";
		String heading6 = "";
		String heading7 = "";
		String label1 = "0001";
		String label2 = "0000";
		StringBuilder strLabel = new StringBuilder("");
		
		StringBuilder label_query = new StringBuilder(" Select LABEL, WERT, TEXT from ");
		label_query.append(dataLibrary).append(".E_ETLABEL WHERE ");
		
		if(ba40Object.getLabelHeadline1()!=null && !ba40Object.getLabelHeadline1().trim().isEmpty()) {
			
			StringBuilder heading1_value_query = new StringBuilder().append(label_query).append("LABEL= 1 AND KAT= 1 AND TEXT= ").append("'"+ba40Object.getLabelHeadline1()+"'");
			partLabelList = dbServiceRepository.getResultUsingQuery(PartLabel.class, heading1_value_query.toString(),true);	
			
			if (partLabelList != null && !partLabelList.isEmpty()) {
				
				heading1 = StringUtils.leftPad(partLabelList.get(0).getWert(), 2, "0");
			}

		}
		if(ba40Object.getLabelHeadline2()!=null && !ba40Object.getLabelHeadline2().trim().isEmpty()) {
			
			StringBuilder heading2_value_query = new StringBuilder().append(label_query).append("LABEL= 1 AND KAT= 2 AND TEXT= ").append("'"+ba40Object.getLabelHeadline2()+"'");
			partLabelList = dbServiceRepository.getResultUsingQuery(PartLabel.class, heading2_value_query.toString(),true);
			
			if (partLabelList != null && !partLabelList.isEmpty()) {
				
				heading2 = StringUtils.leftPad(partLabelList.get(0).getWert(), 2, "0");
			}
		}
		if(ba40Object.getLabelHeadline3()!=null && !ba40Object.getLabelHeadline3().trim().isEmpty()) {
			
			StringBuilder heading3_value_query = new StringBuilder().append(label_query).append("LABEL= 1 AND KAT= 3 AND TEXT= ").append("'"+ba40Object.getLabelHeadline3()+"'");
			partLabelList = dbServiceRepository.getResultUsingQuery(PartLabel.class, heading3_value_query.toString(),true);
			
			if (partLabelList != null && !partLabelList.isEmpty()) {
				
				heading3 = StringUtils.leftPad(partLabelList.get(0).getWert(), 2, "0");
			}
		}
		if(ba40Object.getLabelHeadline4()!=null && !ba40Object.getLabelHeadline4().trim().isEmpty()) {
			
			StringBuilder heading4_value_query = new StringBuilder().append(label_query).append("LABEL= 1 AND KAT= 4 AND TEXT= ").append("'"+ba40Object.getLabelHeadline4()+"'");
			partLabelList = dbServiceRepository.getResultUsingQuery(PartLabel.class, heading4_value_query.toString(),true);
			
			if (partLabelList != null && !partLabelList.isEmpty()) {
				
				heading4 = StringUtils.leftPad(partLabelList.get(0).getWert(), 2, "0");
			}
			
		}
		if(ba40Object.getLabelHeadline5()!=null && !ba40Object.getLabelHeadline5().trim().isEmpty()) {
			
			StringBuilder heading5_value_query = new StringBuilder().append(label_query).append("LABEL= 1 AND KAT= 5 AND TEXT= ").append("'"+ba40Object.getLabelHeadline5()+"'");
			partLabelList = dbServiceRepository.getResultUsingQuery(PartLabel.class, heading5_value_query.toString(),true);
			
			if (partLabelList != null && !partLabelList.isEmpty()) {
				
				heading5 = StringUtils.leftPad(partLabelList.get(0).getWert(), 2, "0");
			}
		}
		if(ba40Object.getLabelHeadline6()!=null && !ba40Object.getLabelHeadline6().trim().isEmpty()) {
			
			StringBuilder heading6_value_query = new StringBuilder().append(label_query).append("LABEL= 1 AND KAT= 6 AND TEXT= ").append("'"+ba40Object.getLabelHeadline6()+"'");
			partLabelList = dbServiceRepository.getResultUsingQuery(PartLabel.class, heading6_value_query.toString(),true);
			
			if (partLabelList != null && !partLabelList.isEmpty()) {
				
				heading6 = StringUtils.leftPad(partLabelList.get(0).getWert(), 2, "0");
			}
		}		
		if(ba40Object.getLabelHeadline7()!=null && !ba40Object.getLabelHeadline7().trim().isEmpty()) {
			
			StringBuilder heading7_value_query = new StringBuilder().append(label_query).append("LABEL= 1 AND KAT= 7 AND TEXT= ").append("'"+ba40Object.getLabelHeadline7()+"'");
			partLabelList = dbServiceRepository.getResultUsingQuery(PartLabel.class, heading7_value_query.toString(),true);
			
			if (partLabelList != null && !partLabelList.isEmpty()) {
				
				heading7 = StringUtils.leftPad(partLabelList.get(0).getWert(), 2, "0");
			}
		}
		
		if(!heading1.isEmpty() && !heading2.isEmpty() && !heading3.isEmpty() && !heading4.isEmpty() && !heading5.isEmpty() && !heading6.isEmpty() && !heading7.isEmpty()) {
		
			strLabel.append(label1).append(heading1).append(heading2).append(heading3).append(heading4).append(heading5).append(heading6).append(heading7).append(label2);
		}
		
		return strLabel.toString();
	}

	
	/**
	 * This method is used to get the Discount group and value  (Rabattsatz , Rabattgruppe) list from DB table E_RAB
	 * @return List
	 */
	@Override
	public List<DropdownObject> getDiscountGroupAndValue(String dataLibrary, String manufacturer, String marke) {

		log.info("Inside getDiscountGroupAndValue method of PartsServiceImpl");
		List<DropdownObject> discountGroupAndValueDetails = new ArrayList<>();

		try {
			manufacturer = manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING: manufacturer;
			
			StringBuilder query = new StringBuilder("SELECT B.GRUPPE, B.SATZ FROM ( SELECT MAX(GDAT) AS maxGDAT, GRUPPE FROM  ");
			query.append(dataLibrary).append(".E_RAB WHERE HERST = ").append("'"+manufacturer+"' AND MARKE = ").append("'"+marke+"' ");
			query.append("AND  LKZ <> 'L' GROUP BY GRUPPE ) AS A INNER JOIN ").append(dataLibrary).append(".E_RAB B ");
			query.append("ON ( A.maxGDAT = B.GDAT AND A.GRUPPE = B.GRUPPE) WHERE  B.HERST = ").append("'"+manufacturer+"' AND B.MARKE = ").append("'"+marke+"' AND B.LKZ <> 'L' ORDER BY B.GRUPPE ");

			List<PartDetails> discountGroupAndValueList = dbServiceRepository.getResultUsingQuery(PartDetails.class, query.toString(), true);

			if(discountGroupAndValueList != null && !discountGroupAndValueList.isEmpty()) {

				for(PartDetails groupAndValueDetails :discountGroupAndValueList ) {

					DropdownObject dropdownObject = new DropdownObject();
					dropdownObject.setKey(groupAndValueDetails.getPartDiscountGroup());
					dropdownObject.setValue(String.valueOf(groupAndValueDetails.getDiscountGroupPercentageValue()));

					discountGroupAndValueDetails.add(dropdownObject);
				}
			}else{
				log.debug("Discount Group And Value List is empty");
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Discount Group And Value List is empty"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Discount Group And Value List is empty"), exception);
			throw exception;
		}

		return discountGroupAndValueDetails;
	}
	
	
	/**
	 * This method is used to get the Messwert des externen Rollgeräuschs  (Measured value) list from DB table e_etlabel
	 * @return List
	 */
	@Override
	public List<DropdownObject> getMeasuredValueList(String dataLibrary) {

		log.info("Inside getMeasuredValueList method of PartsServiceImpl");
		List<DropdownObject> measuredValueObjList = new ArrayList<>();

		try {
			StringBuilder query = new StringBuilder("SELECT WERT, TEXT FROM  ");
			query.append(dataLibrary).append(".e_etlabel WHERE LABEL=1 AND KAT= 4 AND WERT > 0 ORDER BY WERT ");

			List<PartLabel> measuredValueList = dbServiceRepository.getResultUsingQuery(PartLabel.class, query.toString(), true);

			if(measuredValueList != null && !measuredValueList.isEmpty()) {

				for(PartLabel partLabel:measuredValueList) {

					DropdownObject dropdownObject = new DropdownObject();
					dropdownObject.setKey(partLabel.getWert());
					dropdownObject.setValue(partLabel.getLabelHeadingValue());

					measuredValueObjList.add(dropdownObject);
				}
			}else{
				log.debug("Measured value of the external rolling noise List is empty");
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Measured value of the external rolling noise"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Measured value of the external rolling noise"), exception);
			throw exception;
		}

		return measuredValueObjList;
	}
	
	
	/**
	 * This method is used to get the Reifenklassifizierung  (Tire classification) list from DB table e_etlabel
	 * @return List
	 */
	@Override
	public List<DropdownObject> getTireClassificationList(String dataLibrary) {

		log.info("Inside getTireClassificationList method of PartsServiceImpl");
		List<DropdownObject> tireClassificationObjList = new ArrayList<>();

		try {
			StringBuilder query = new StringBuilder("SELECT WERT, TEXT FROM  ");
			query.append(dataLibrary).append(".e_etlabel WHERE LABEL=1 AND KAT= 5 AND WERT > 0 ORDER BY WERT ");

			List<PartLabel> tireClassificationList = dbServiceRepository.getResultUsingQuery(PartLabel.class, query.toString(), true);

			if(tireClassificationList != null && !tireClassificationList.isEmpty()) {

				for(PartLabel partLabel:tireClassificationList) {

					DropdownObject dropdownObject = new DropdownObject();
					dropdownObject.setKey(partLabel.getWert());
					dropdownObject.setValue(partLabel.getLabelHeadingValue());

					tireClassificationObjList.add(dropdownObject);
				}
			}else{
				log.debug("Tire classification List is empty");
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Tire classification"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Tire classification"), exception);
			throw exception;
		}

		return tireClassificationObjList;
	}
	
	
	/**
	 * This method is used to create/Update/delete LOPA (Storage Location) details based on provided Part number.
	 */
	@Override
	public  Map<String, Boolean> createUpdateStorageLocation( List<StorageLocationDTO> storageLocationDTOList, String dataLibrary, String partNumber, 
			String warehouseId, String oem) {

		log.info("Inside createUpdateStorageLocation method of PartsServiceImpl");
		Integer counter = 0;
		Integer deleteCounter = 0;
		Map<String, Boolean> methodOutput = new HashMap<String, Boolean>();
		methodOutput.put("isUpdated", false);
		try {

			oem = oem.equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING: oem;

			StringBuilder query = new StringBuilder("Select LOPA, PRIO from ");
			query.append(dataLibrary).append(".E_LAGO where TNR='").append(partNumber).append("' and HERST='").append(oem).append("' and LNR=").append(warehouseId);
			query.append("  order by PRIO ASC ");

			List<StorageLocation> lopaDetailList = dbServiceRepository.getResultUsingQuery(StorageLocation.class, query.toString(), true);
			
			//if the list is not empty
			if (null == lopaDetailList || lopaDetailList.isEmpty()) {

				if(storageLocationDTOList !=null && !storageLocationDTOList.isEmpty()) {
					for(StorageLocationDTO storageLocationDTO :storageLocationDTOList) {
						//Directly insert code...
						insertStorageLocations(storageLocationDTO, dataLibrary, partNumber, warehouseId, oem);
					}
				}
			}
			else {
				if(storageLocationDTOList != null && !storageLocationDTOList.isEmpty()) {
					for(StorageLocationDTO storageLocationDTO :storageLocationDTOList) {
						// insert/update/delete code...
						counter = 0;
						for (StorageLocation storageLocation : lopaDetailList) {

							if(storageLocationDTO.getPriority().equals(String.valueOf(storageLocation.getPriority())) ){
								if(!storageLocationDTO.getStorageLocation().equals(storageLocation.getStorageLocation())) {
									// update
									updateStorageLocations(storageLocationDTO, dataLibrary, partNumber, warehouseId, oem);
									counter++;
									break;
								}
								else {
									counter++;
									break;
								}
							}
						}
						if(counter==0) {
							insertStorageLocations(storageLocationDTO, dataLibrary, partNumber, warehouseId, oem);
						}
					}
				}

				for (StorageLocation storageLocation : lopaDetailList) {
					deleteCounter = 0;
					for(StorageLocationDTO storageLocationDTO :storageLocationDTOList) {
						if(storageLocationDTO.getPriority().equals(String.valueOf(storageLocation.getPriority())) ) {
							deleteCounter++;
							break;
						}
					}
					if(deleteCounter == 0 && !(String.valueOf(storageLocation.getPriority()).equals("1"))) {
						//delete the values from DB
						deleteStorageLocations(storageLocation, dataLibrary, partNumber, warehouseId, oem);
					}
				}
			}
			methodOutput.put("isUpdated", true);
		}
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Storage Location (LOPA)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Storage Location (LOPA)"), exception);
			throw exception;
		}

		return methodOutput;
	}
	
	
	private void updateStorageLocations(StorageLocationDTO storageLocationDTO, String dataLibrary, String partnumber, String warehouseId, String oem) {

		StringBuilder query = new StringBuilder("Update ").append(dataLibrary).append(".E_LAGO");
		query.append(" SET LOPA='").append(storageLocationDTO.getStorageLocation()).append("' where PRIO=").append(storageLocationDTO.getPriority());
		query.append(" and TNR='").append(partnumber).append("' and HERST='").append(oem);
		query.append("' and LNR=").append(warehouseId);	

		try {
			boolean updateFlag = dbServiceRepository.updateResultUsingQuery(query.toString());

			if (!updateFlag) {
				log.info("Unable to Update this LOPA details :{} partno : {}", storageLocationDTO.getPriority(), partnumber);
			}
		}
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Storage Location (LOPA)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Storage Location (LOPA)"), exception);
			throw exception;
		}
	}
	
	
	private void insertStorageLocations(StorageLocationDTO storageLocationDTO, String dataLibrary, String partnumber, String warehouseId, String oem) {
		
		StringBuilder query = new StringBuilder("Insert into ").append(dataLibrary).append(".E_LAGO");
		query.append(" (HERST, TNR, LNR, PRIO, LOPA)  values ");
		query.append(" ('").append(oem+"', '").append(partnumber+"', ").append(warehouseId+",").append(storageLocationDTO.getPriority()+", '");
		query.append(storageLocationDTO.getStorageLocation()+"') ");
		
		dbServiceRepository.insertResultUsingQuery(query.toString());

	}
	
	
	private void deleteStorageLocations(StorageLocation storageLocation, String dataLibrary, String partNumber, String warehouseId, String oem) {
				
		StringBuilder query = new StringBuilder("DELETE FROM ").append(dataLibrary).append(".E_LAGO");
		query.append(" WHERE PRIO = ").append(storageLocation.getPriority()).append(" and TNR='").append(partNumber).append("' and HERST='").append(oem);
		query.append("' and LNR=").append(warehouseId);
		
		Connection con = dbServiceRepository.getConnectionObject();		
		boolean updateFlag = dbServiceRepository.deleteResultUsingQuery(query.toString(), con);
		
		if (!updateFlag) {
			log.info("Unable to delete this LOPA details :{} partno : {}", storageLocation.getPriority(), partNumber);
			
		}
		
	}
		
	
	/**
	 * This is method is used to get Stock information List based on provided part number from DB.
	 */
	@Override
	public GlobalSearch getStockInformationOfPart( String dataLibrary, String schema, String partNumber, String oem, String pageSize, String pageNumber,
			String warehouseNo, String allowedWarehouses) {

		log.info("Inside getStockInformationOfPart method of PartsServiceImpl");
		
		GlobalSearch globalSearchList = new GlobalSearch();
		List<Integer> updatedAllowedWarehouseList = new ArrayList<Integer>();
		try {	
			
			if(Integer.parseInt(warehouseNo) < 0){
				log.info("Warehouse is not valid");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.VEHICLE_WAREHOUSE_NUMBER_MSG_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.VEHICLE_WAREHOUSE_NUMBER_MSG_KEY));
				throw exception;
			}
			
			if(pageSize==null || pageNumber==null || pageSize.isEmpty() || pageNumber.isEmpty()){
				pageSize = RestInputConstants.DEFAULT_PAGE_SIZE;
				pageNumber = RestInputConstants.DEFAULT_PAGE_NO;
			}

			int totalRecords = Integer.parseInt(pageSize);
			int nextRows = totalRecords * (Integer.parseInt(pageNumber) - 1);

			//validate the page size
			validatePageSize(totalRecords);
			log.info("PageSize  : {}  OFFSET Size (Skip Rows):  {} Page Number  : {} ", pageSize, nextRows, pageNumber);
			
			if(allowedWarehouses != null && !allowedWarehouses.trim().isEmpty()) {
				//Company allowed warehouse list
				updatedAllowedWarehouseList = Stream.of(allowedWarehouses.split(","))
						.map(String::trim).map(Integer::parseInt).collect(Collectors.toList());
			}
			boolean isWarehousPresent = false;
			
			oem = oem.equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING: oem;
			
			StringBuilder query = new StringBuilder("Select distinct et.LNR, et.SA, et.DTLABG, et.AKTBES, et.DAK, (SELECT NAME FROM ");
			query.append(schema).append(".O_WRH WHERE AP_WAREHOUS_ID = et.LNR) AS LagerName, ");
			query.append(" (SELECT COUNT(*) FROM (Select distinct LNR, SA, DTLABG, AKTBES, DAK from ");
			query.append(dataLibrary).append(".E_ETSTAMM where TNR = '").append(partNumber).append("' AND LNR <> ").append(warehouseNo).append(" and HERST ='");
			query.append(oem).append("' )) AS ROWNUMER from ");
			query.append(dataLibrary).append(".E_ETSTAMM et where et.TNR = '").append(partNumber).append("' AND LNR <> ").append(warehouseNo).append(" and et.HERST ='").append(oem);
			query.append("' order by et.LNR OFFSET ");
			query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");

			List<PartDetails> stocksDetailList = dbServiceRepository.getResultUsingQuery(PartDetails.class, query.toString(), true);
			
			List<LagerPartDetailsDTO> stocksInfoDetailDTOList = new ArrayList<>();

			//if the list is not empty
			if (stocksDetailList != null && !stocksDetailList.isEmpty()) {
				
				for (PartDetails partDetail : stocksDetailList) {
					LagerPartDetailsDTO lagerpartDetailsDTO = new LagerPartDetailsDTO();
					
					lagerpartDetailsDTO.setWarehouseNo(String.valueOf(partDetail.getWarehouse()));
					lagerpartDetailsDTO.setWarehouseName(partDetail.getLagerName()==null?"":partDetail.getLagerName());
					lagerpartDetailsDTO.setLastDisposalDate(getDDMMYYYYFormat(partDetail.getLastDisposalDate()));
					lagerpartDetailsDTO.setCurrentStock(String.valueOf(partDetail.getCurrentStock()));
					lagerpartDetailsDTO.setStorageIndikator(String.valueOf(partDetail.getStorageIndikator()));
					lagerpartDetailsDTO.setAverageNetPrice(String.valueOf(partDetail.getAverageNetPrice()));
					lagerpartDetailsDTO.setMovementAllowed(false);
					
					isWarehousPresent = updatedAllowedWarehouseList.stream().anyMatch(x -> x.equals(Integer.parseInt(String.valueOf(partDetail.getWarehouse()))));
					
					if(isWarehousPresent && partDetail.getCurrentStock() != null && partDetail.getCurrentStock().compareTo(BigDecimal.ZERO) > 0) {
						lagerpartDetailsDTO.setMovementAllowed(true);
					}
					
					stocksInfoDetailDTOList.add(lagerpartDetailsDTO);
					
				}
				globalSearchList.setSearchDetailsList(stocksInfoDetailDTOList);
				globalSearchList.setTotalPages(Integer.toString(stocksDetailList.get(0).getTotalCount()));
				globalSearchList.setTotalRecordCnt(Integer.toString(stocksDetailList.get(0).getTotalCount()));
			}
			else {
				log.debug("Stock information List is empty");
				globalSearchList.setSearchDetailsList(stocksInfoDetailDTOList);
				globalSearchList.setTotalPages(Integer.toString(0));
				globalSearchList.setTotalRecordCnt(Integer.toString(0));
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Stock information"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Stock information"), exception);
			throw exception;
		}

		return globalSearchList;
	}
	
	
	/**
	 * This method is used to convert the part number from one format into another using Logic of SYSBRA PGM.
	 */
	@Override
	public Map<String, String> convertPartNoFormats(String partNumber, String vonValue, String nachValue) {

		log.info("Inside convertPartNoFormats method of PartsServiceImpl");

		Map<String, String> formatedPartNo = new HashMap<>();		
		formatedPartNo.put(RestInputConstants.FORMATED_PARTNO, partNumber);

		String kbValue = partNumber.substring(0, 1);
		String partNo = partNumber.substring(1, partNumber.length());
		partNo = StringUtils.rightPad(partNo, 24, " ");		
		String formattedPartNo = kbValue;

		try {

			//1
			if(vonValue.equalsIgnoreCase(RestInputConstants.VALUE_E)) {

				if(nachValue.equalsIgnoreCase(RestInputConstants.VALUE_S)) {

					if(kbValue.equalsIgnoreCase(RestInputConstants.KB_VALUE_A) || kbValue.equalsIgnoreCase(RestInputConstants.KB_VALUE_W) 
							|| kbValue.equalsIgnoreCase(RestInputConstants.KB_VALUE_H)) {

						if(kbValue.equalsIgnoreCase(RestInputConstants.KB_VALUE_A) || kbValue.equalsIgnoreCase(RestInputConstants.KB_VALUE_W)) {
							if(partNumber.length() == 11) {
								partNo = "  "+partNo;
								partNo = StringUtils.rightPad(partNo, 24, " ");
							}
						}

						formattedPartNo += swapChars(partNo, RestInputConstants.ORIGINAL_POS, RestInputConstants.NEW_POS);
						formatedPartNo.put(RestInputConstants.FORMATED_PARTNO, formattedPartNo);					
					}
				}
				else if(nachValue.equalsIgnoreCase(RestInputConstants.VALUE_D)){
					switch(kbValue) {

					case RestInputConstants.KB_VALUE_A:
					case RestInputConstants.KB_VALUE_W:
					case RestInputConstants.KB_VALUE_H:
						if(kbValue.equalsIgnoreCase(RestInputConstants.KB_VALUE_A) || kbValue.equalsIgnoreCase(RestInputConstants.KB_VALUE_W)) {
							if(partNumber.length() == 11) {
								partNo = "  "+partNo;
								partNo = StringUtils.rightPad(partNo, 24, " ");
							}
						}
						formattedPartNo += addSpaces(partNo, RestInputConstants.SPACES_AWH);
						formatedPartNo.put(RestInputConstants.FORMATED_PARTNO, StringUtils.rightPad(formattedPartNo, 24, " "));
						break;
					case RestInputConstants.KB_VALUE_N:
						formattedPartNo += addSpaces(partNo, RestInputConstants.SPACES_N);
						formatedPartNo.put(RestInputConstants.FORMATED_PARTNO, StringUtils.rightPad(formattedPartNo, 24, " "));
						break;
					case RestInputConstants.KB_VALUE_X:
						formattedPartNo += addSpaces(partNo, RestInputConstants.SPACES_X);
						formatedPartNo.put(RestInputConstants.FORMATED_PARTNO, StringUtils.rightPad(formattedPartNo, 24, " "));
						break;
					case RestInputConstants.KB_VALUE_C:
						formattedPartNo += addSpaces(partNo, RestInputConstants.SPACES_C);
						formatedPartNo.put(RestInputConstants.FORMATED_PARTNO, StringUtils.rightPad(formattedPartNo, 24, " "));
						break;
					case RestInputConstants.KB_VALUE_M:
						formattedPartNo += addSpaces(partNo, RestInputConstants.SPACES_M);
						formatedPartNo.put(RestInputConstants.FORMATED_PARTNO, StringUtils.rightPad(formattedPartNo, 24, " "));
						break;
					case RestInputConstants.KB_VALUE_D:
						formattedPartNo += addSpaces(partNo, RestInputConstants.SPACES_D);
						formatedPartNo.put(RestInputConstants.FORMATED_PARTNO, StringUtils.rightPad(formattedPartNo, 24, " "));
						break;
					case RestInputConstants.KB_VALUE_B:
						if(partNo.charAt(0) =='4') {
							formattedPartNo += addSpaces(partNo, RestInputConstants.SPACES_B1);
						}
						else {
							formattedPartNo += addSpaces(partNo, RestInputConstants.SPACES_B2);
						}
						formatedPartNo.put(RestInputConstants.FORMATED_PARTNO, StringUtils.rightPad(formattedPartNo, 24, " "));
						break;
					default:
						formattedPartNo = partNumber;
						formatedPartNo.put(RestInputConstants.FORMATED_PARTNO, formattedPartNo);
						break;				
					}
				}			
			}

			//2
			if(vonValue.equalsIgnoreCase(RestInputConstants.VALUE_S)) {

				if(nachValue.equalsIgnoreCase(RestInputConstants.VALUE_D)) {

					switch(kbValue) {

					case RestInputConstants.KB_VALUE_A:
					case RestInputConstants.KB_VALUE_W:
					case RestInputConstants.KB_VALUE_H:					
						String newPartNo = swapChars(partNo, RestInputConstants.ORIGINAL_POS_SDAWH, RestInputConstants.NEW_POS_SDAWH); // rearrange first

						formattedPartNo += addSpacesAtSpecificPos(newPartNo, RestInputConstants.SPACES_AWH);
						formatedPartNo.put(RestInputConstants.FORMATED_PARTNO, StringUtils.rightPad(formattedPartNo, 24, " "));
						break;
					case RestInputConstants.KB_VALUE_N:
						formattedPartNo += addSpaces(partNo, RestInputConstants.SPACES_N);
						formatedPartNo.put(RestInputConstants.FORMATED_PARTNO, StringUtils.rightPad(formattedPartNo, 24, " "));
						break;
					case RestInputConstants.KB_VALUE_X:
						formattedPartNo += addSpaces(partNo, RestInputConstants.SPACES_X);
						formatedPartNo.put(RestInputConstants.FORMATED_PARTNO, StringUtils.rightPad(formattedPartNo, 24, " "));
						break;
					case RestInputConstants.KB_VALUE_C:
						formattedPartNo += addSpaces(partNo, RestInputConstants.SPACES_C);
						formatedPartNo.put(RestInputConstants.FORMATED_PARTNO, StringUtils.rightPad(formattedPartNo, 24, " "));
						break;
					case RestInputConstants.KB_VALUE_M:
						formattedPartNo += addSpaces(partNo, RestInputConstants.SPACES_M);
						formatedPartNo.put(RestInputConstants.FORMATED_PARTNO, StringUtils.rightPad(formattedPartNo, 24, " "));
						break;
					case RestInputConstants.KB_VALUE_D:
						formattedPartNo += addSpaces(partNo, RestInputConstants.SPACES_D);
						formatedPartNo.put(RestInputConstants.FORMATED_PARTNO, StringUtils.rightPad(formattedPartNo, 24, " "));
						break;
					case RestInputConstants.KB_VALUE_B:
						if(partNo.charAt(0) =='4') {
							formattedPartNo += addSpaces(partNo, RestInputConstants.SPACES_B1);
						}
						else {
							formattedPartNo += addSpaces(partNo, RestInputConstants.SPACES_B2);
						}
						formatedPartNo.put(RestInputConstants.FORMATED_PARTNO, StringUtils.rightPad(formattedPartNo, 24, " "));
						break;
					default:
						formattedPartNo = partNumber; 
						formatedPartNo.put(RestInputConstants.FORMATED_PARTNO, formattedPartNo);
						break;
					}
				}
				else if(nachValue.equalsIgnoreCase(RestInputConstants.VALUE_E)){

					if(kbValue.equalsIgnoreCase(RestInputConstants.KB_VALUE_A) || kbValue.equalsIgnoreCase(RestInputConstants.KB_VALUE_W) 
							|| kbValue.equalsIgnoreCase(RestInputConstants.KB_VALUE_H)) {

						String newPartNo = swapChars(partNo, RestInputConstants.ORIGINAL_POS, RestInputConstants.NEW_POS_SDAWH);
						
						if(kbValue.equalsIgnoreCase(RestInputConstants.KB_VALUE_A) || kbValue.equalsIgnoreCase(RestInputConstants.KB_VALUE_W)) {
							if(newPartNo.charAt(0)==' ' && newPartNo.charAt(1)==' ') {
								newPartNo = newPartNo.substring(2, newPartNo.length());
								
							}
						}
						formattedPartNo += StringUtils.rightPad(newPartNo, 23, " ");
						formatedPartNo.put(RestInputConstants.FORMATED_PARTNO, formattedPartNo);
					}
				}
			}

			//3
			if(vonValue.equalsIgnoreCase(RestInputConstants.VALUE_D)) {

				if(nachValue.equalsIgnoreCase(RestInputConstants.VALUE_S)) {
					switch(kbValue) {

					case RestInputConstants.KB_VALUE_A:
					case RestInputConstants.KB_VALUE_W:
					case RestInputConstants.KB_VALUE_H:
						String newPartNo = swapChars(partNo, RestInputConstants.ORIGINAL_POS_DSAWH, RestInputConstants.NEW_POS_DSAWH);

						formattedPartNo += addSpacesAtSpecificPos(newPartNo, RestInputConstants.SPACES_DSAWH);
						formatedPartNo.put(RestInputConstants.FORMATED_PARTNO, StringUtils.rightPad(formattedPartNo, 24, " "));					
						break;
					case RestInputConstants.KB_VALUE_N:
						formattedPartNo += removeSpaces(partNo, RestInputConstants.SPACES_N);
						formatedPartNo.put(RestInputConstants.FORMATED_PARTNO, formattedPartNo);
						break;

					case RestInputConstants.KB_VALUE_X:					
						formattedPartNo += removeSpaces(partNo, RestInputConstants.SPACES_X);
						formatedPartNo.put(RestInputConstants.FORMATED_PARTNO, formattedPartNo);
						break;
					case RestInputConstants.KB_VALUE_C:
						formattedPartNo += removeSpaces(partNo, RestInputConstants.SPACES_C);
						formatedPartNo.put(RestInputConstants.FORMATED_PARTNO, formattedPartNo);
						break;
					case RestInputConstants.KB_VALUE_M:
						formattedPartNo += removeSpaces(partNo, RestInputConstants.SPACES_M);
						formatedPartNo.put(RestInputConstants.FORMATED_PARTNO, formattedPartNo);
						break;
					case RestInputConstants.KB_VALUE_D:
						formattedPartNo += removeSpaces(partNo, RestInputConstants.SPACES_D);
						formatedPartNo.put(RestInputConstants.FORMATED_PARTNO, formattedPartNo);
						break;
					case RestInputConstants.KB_VALUE_B:
						if(partNo.charAt(0) =='4') {
							formattedPartNo += removeSpaces(partNo, RestInputConstants.SPACES_B1);
						}
						else {
							formattedPartNo += removeSpaces(partNo, RestInputConstants.SPACES_B2);
						}
						formatedPartNo.put(RestInputConstants.FORMATED_PARTNO, formattedPartNo);
						break;
					default:
						formattedPartNo = partNumber; 
						formatedPartNo.put(RestInputConstants.FORMATED_PARTNO, formattedPartNo);
						break;
					}
				}
				else if(nachValue.equalsIgnoreCase(RestInputConstants.VALUE_E)){
					switch(kbValue) {

					case RestInputConstants.KB_VALUE_A:
					case RestInputConstants.KB_VALUE_W:
					case RestInputConstants.KB_VALUE_H:
						String newPartNo = removeSpaces(partNo, RestInputConstants.SPACES_AWH);
						
						if(kbValue.equalsIgnoreCase(RestInputConstants.KB_VALUE_A) || kbValue.equalsIgnoreCase(RestInputConstants.KB_VALUE_W)) {
							if(newPartNo.charAt(0)==' ' && newPartNo.charAt(1)==' ') {
								newPartNo = newPartNo.substring(2, newPartNo.length());
							}
						}
						formattedPartNo += StringUtils.rightPad(newPartNo, 23, " ");
						formatedPartNo.put(RestInputConstants.FORMATED_PARTNO, formattedPartNo);
						break;
					case RestInputConstants.KB_VALUE_N:
						formattedPartNo += removeSpaces(partNo, RestInputConstants.SPACES_N);
						formatedPartNo.put(RestInputConstants.FORMATED_PARTNO, formattedPartNo);
						break;
					case RestInputConstants.KB_VALUE_X:
						formattedPartNo += removeSpaces(partNo, RestInputConstants.SPACES_X);
						formatedPartNo.put(RestInputConstants.FORMATED_PARTNO, formattedPartNo);
						break;
					case RestInputConstants.KB_VALUE_C:
						formattedPartNo += removeSpaces(partNo, RestInputConstants.SPACES_C);
						formatedPartNo.put(RestInputConstants.FORMATED_PARTNO, formattedPartNo);
						break;
					case RestInputConstants.KB_VALUE_M:
						formattedPartNo += removeSpaces(partNo, RestInputConstants.SPACES_M);
						formatedPartNo.put(RestInputConstants.FORMATED_PARTNO, formattedPartNo);
						break;
					case RestInputConstants.KB_VALUE_D:
						formattedPartNo += removeSpaces(partNo, RestInputConstants.SPACES_D);
						formatedPartNo.put(RestInputConstants.FORMATED_PARTNO, formattedPartNo);
						break;
					case RestInputConstants.KB_VALUE_B:
						if(partNo.charAt(0) =='4') {
							formattedPartNo += removeSpaces(partNo, RestInputConstants.SPACES_B1);
							formatedPartNo.put(RestInputConstants.FORMATED_PARTNO, formattedPartNo);
						}
						else {
							formattedPartNo += removeSpaces(partNo, RestInputConstants.SPACES_B2);
							formatedPartNo.put(RestInputConstants.FORMATED_PARTNO, formattedPartNo);
						}
						break;
					default:
						formattedPartNo = partNumber;
						formatedPartNo.put(RestInputConstants.FORMATED_PARTNO, formattedPartNo);
						break;
					}
				}
			}
		}
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.CREATE_FAILED_MSG_KEY, "New formatted Part number"));
			log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY, "New formatted Part number"), exception);
			throw exception;
		}

		return formatedPartNo;
	}
	
	
	static String swapChars(String str, int[] ipos, int[] jpos)
    {
		StringBuilder sb = new StringBuilder(str);
		for(int count=0; count<ipos.length; count++) {
			
			sb.setCharAt(ipos[count], str.charAt(jpos[count]));
//	        sb.setCharAt(jpos[count], str.charAt(ipos[count]));
		}
        return sb.toString();
    }
	 
	
	private String addSpaces(String partNo, int[] spaces) {
        StringBuilder sb = new StringBuilder();
        int k=0;
        int count=0;
        for(int i=0;i<spaces.length;i++){
        	
            sb.append(partNo.substring(k, spaces[i]-count));
            k=spaces[i]-count;
            sb.append(" ");
            count +=1;
        }
        sb.append(partNo.substring(k, partNo.length()));
        
        return StringUtils.rightPad(sb.toString(), 24, " ");
    }
	
	
	private String addSpacesAtSpecificPos(String partNo, int[] spaces) {
        
		StringBuilder sb = new StringBuilder();
        int k=0;
        for(int i=0;i<spaces.length;i++){
            sb.append(partNo.substring(k,spaces[i]));
            k=spaces[i]+1;                             
            sb.append(" ");
        }
        sb.append(partNo.substring(k,partNo.length()));
        return sb.toString();
    }
	
	
	private String removeSpaces(String partNo, int[] spaces) {

		StringBuilder sb = new StringBuilder(partNo);
		
		for(int i : spaces) {
			sb.setCharAt(i, '#');
		}
		
		return  sb.toString().replaceAll("#", "");
	}
	
	
	private void calculatePriceWithSurcharge(String dataLibrary, String partPrice, String discount_Group, 
			String partBrand , String priceLimit, SearchPartsDTO partsDTO , String rabTblValues, String companyId ) {

		log.info("Inside calculatePriceWithSurcharge method of PartsServiceImpl");
		Integer surchargePosition = 0;
		Integer surchargePercentage = 0;
		Double finalPriceWithsurchange = 0.0;

		try {
			
			Double purchasePrice = Double.parseDouble(partPrice);
			
			if(rabTblValues!=null && !rabTblValues.isEmpty() && priceLimit != null && !priceLimit.isEmpty()) {
			Double priceLimitValue = Double.parseDouble(priceLimit);

			Integer discountGroup = Integer.parseInt((discount_Group!=null && !discount_Group.trim().isEmpty()) ? discount_Group : "0");
					
					if(purchasePrice > priceLimitValue) {
						if(discountGroup == 0){
							surchargePosition = ((100 - 1) * 9) + 7;
						}else {
							surchargePosition = ((discountGroup - 1) * 9) + 7;
						}						
					}
					else if(purchasePrice <= priceLimitValue) {
						if(discountGroup == 0){
							surchargePosition = ((100 - 1) * 9) + 4;
						}
						else {
							surchargePosition = ((discountGroup - 1) * 9) + 4;
						}
					}

					//StringBuilder surchargeQuery = new StringBuilder("SELECT substr(RAB_TBL, ").append(surchargePosition).append(", 3) as surcharge_percentage from ");
					//surchargeQuery.append(dataLibrary).append(".F_KUNRAB where KDNR = '99999900' and FZART = 'ET' and Marke = '").append(partBrand).append("' ");

					//List<PartDetails> surchargePercentageList = dbServiceRepository.getResultUsingQuery(PartDetails.class, surchargeQuery.toString(), true);

					//if the list is not empty
					if(rabTblValues.length() >= surchargePosition+3) {
						
							surchargePercentage = Integer.parseInt(rabTblValues.substring(surchargePosition, surchargePosition+3));
							finalPriceWithsurchange = purchasePrice + ( purchasePrice * surchargePercentage / 1000 );
							partsDTO.setPartPriceWithSurcharge(decimalformat_twodigit.format(finalPriceWithsurchange));
					}
					else {
						partsDTO.setPartPriceWithSurcharge(decimalformat_twodigit.format(purchasePrice));
					}									
				}
			else {
				partsDTO.setPartPriceWithSurcharge(decimalformat_twodigit.format(purchasePrice));
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
	}
	
	/**
	 * This is method is used to get Alternative Parts details for tree view from DB.
	 */
	@Override
	public List<PartsTreeViewDTO> getLocalAlternativesDetailsInTreeView(String schema, String dataLibrary, String partNumber, String manufacturer,
			String warehouseNumber) {

		log.info("Inside getLocalAlternativesDetailsInTreeView method of PartsServiceImpl");
		List<PartsTreeViewDTO> partsTreeViewList = new ArrayList<>();
		
		try {
			manufacturer = manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING: manufacturer;		
		
			List<PartDetails> successerList = getPartSuccesserDetails(partNumber, dataLibrary, manufacturer,
					 warehouseNumber, schema);
			
			List<PartDetails> predecesserList = getPartPredecessorDetails(partNumber, dataLibrary, manufacturer,
					 warehouseNumber, schema);
			
			if(successerList!= null && !successerList.isEmpty()) {
				log.debug("#### set actual (Aktuelles Teil ) part details ####");	
				PartDetails partsDetails = successerList.get(0);

				PartsTreeViewDTO actualPart = new PartsTreeViewDTO();
				actualPart.setPartNumber(partsDetails.getPartNumber());
				actualPart.setPartName(partsDetails.getName());
				actualPart.setCurrentStock(String.valueOf(partsDetails.getCurrentStock()));
				actualPart.setParentId(null);
				partsTreeViewList.add(actualPart);
			
			log.debug("#### gset predecessor (Vorgänger) part details ####");
			
			if(predecesserList!= null && !predecesserList.isEmpty()) {
				
				PartsTreeViewDTO predecesserheader = new PartsTreeViewDTO();
				predecesserheader.setPartNumber("Vorgänger");
				predecesserheader.setParentId(actualPart.getPartNumber());
				partsTreeViewList.add(predecesserheader);
				
				for(PartDetails predecesserDetails: predecesserList) {
					
					if(predecesserDetails.getPredecessorPartNumber()!= null && !predecesserDetails.getPredecessorPartNumber().isEmpty() ) {
					PartsTreeViewDTO partPredecesser = new PartsTreeViewDTO();
					partPredecesser.setPartNumber(predecesserDetails.getPredecessorPartNumber());
					partPredecesser.setPartName(predecesserDetails.getName());
					partPredecesser.setCurrentStock(String.valueOf(predecesserDetails.getCurrentStock()));
					
					if(predecesserDetails.getPartNumber().equalsIgnoreCase(actualPart.getPartNumber())) {
						partPredecesser.setParentId("Vorgänger");
					}else {
						partPredecesser.setParentId(predecesserDetails.getPartNumber());	
					}
					
					partsTreeViewList.add(partPredecesser);
					}
				}
			}
			
			log.debug("#### set successor (Nachfolger) part details ####");
			
			if(successerList!= null && !successerList.isEmpty()) {
				
				PartsTreeViewDTO successerheader = new PartsTreeViewDTO();
				
				successerheader.setPartNumber("Nachfolger");
				successerheader.setParentId(actualPart.getPartNumber());
				partsTreeViewList.add(successerheader);
				
				for(PartDetails successerDetails: successerList) {
					
					if(successerDetails.getSuccessorPartNumber()!= null && !successerDetails.getSuccessorPartNumber().isEmpty() ) {
					PartsTreeViewDTO partSuccesser = new PartsTreeViewDTO();
					partSuccesser.setPartNumber(successerDetails.getSuccessorPartNumber());
					partSuccesser.setPartName(successerDetails.getName());
					partSuccesser.setCurrentStock(String.valueOf(successerDetails.getCurrentStock()));
					
					if(successerDetails.getPartNumber().equalsIgnoreCase(actualPart.getPartNumber())) {
						partSuccesser.setParentId("Nachfolger");
					}else {
						partSuccesser.setParentId(successerDetails.getPartNumber());
					}
					partsTreeViewList.add(partSuccesser);
				  }
				}
			
			}
		}
		}catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Local alternatives treeview details"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Local alternatives treeview details"), exception);
			throw exception;
		}

		return partsTreeViewList;
	}

	
	
	private List<PartDetails>  getPartPredecessorDetails(String partNumber, String dataLibrary, String manufacturer,
			String warehouseNumber,String schema) {
		
		log.info("Inside getPartPredecessorDetails method of OrdersServiceImpl");
		
			//this query use to get predecessor part details
			StringBuilder predecessor_query = new StringBuilder(" WITH X(  TNR, TNRV, LNR, HERST, AKTBES, BENEN , Level) AS ( ");
			predecessor_query.append(" SELECT  P.TNR, P.TNRV, P.LNR, P.HERST, P.AKTBES, P.BENEN , 0  FROM ");
			predecessor_query.append(dataLibrary).append(".E_ETSTAMM P WHERE TNR = ").append("'"+partNumber+"'");
			predecessor_query.append(" AND LNR = ").append(warehouseNumber).append(" AND HERST = '").append(manufacturer).append("' AND TNR <> TNRV ");
			predecessor_query.append(" UNION ALL ");
			predecessor_query.append(" SELECT  P.TNR, P.TNRV,  P.LNR, P.HERST, P.AKTBES, P.BENEN, Level+1  FROM x ,");
			predecessor_query.append(dataLibrary).append(".E_ETSTAMM  P   WHERE x.TNRV=P.TNR AND P.LNR = ").append(warehouseNumber).append(" AND P.HERST = '").append(manufacturer).append("' ");
			predecessor_query.append(" ) SELECT * FROM X WHERE X.LNR = ").append(warehouseNumber).append(" AND X.LEVEL <  5 ");
			
		List<PartDetails> alternativePartsDetailsList = dbServiceRepository.getResultUsingQuery(PartDetails.class,predecessor_query.toString(), true);
			
		return alternativePartsDetailsList;
	}

	
	private List<PartDetails>  getPartSuccesserDetails(String partNumber, String dataLibrary, String manufacturer,
			String warehouseNumber,String schema) {
		
		log.info("Inside getPartSuccesserDetails method of OrdersServiceImpl");
		
			//this query use to get predecessor part details
			StringBuilder successer_query = new StringBuilder(" WITH X(  TNR, TNRN, LNR, HERST, AKTBES, BENEN ,   Level) AS  (");
			successer_query.append(" SELECT  P.TNR, P.TNRN, P.LNR, P.HERST, P.AKTBES, P.BENEN , 0 FROM ");
			successer_query.append(dataLibrary).append(".E_ETSTAMM P  WHERE TNR = ").append("'"+partNumber+"'");
			successer_query.append(" AND  LNR = ").append(warehouseNumber).append(" AND HERST = '").append(manufacturer).append("' AND TNR <> TNRN  ");
			successer_query.append(" UNION ALL ");
			successer_query.append(" SELECT  P.TNR, P.TNRN,  P.LNR, P.HERST, P.AKTBES, P.BENEN, Level+1 FROM x ,");
			successer_query.append(dataLibrary).append(".E_ETSTAMM P  WHERE x.TNRN=P.TNR AND P.LNR = ").append(warehouseNumber).append(" AND P.HERST = '").append(manufacturer).append("' ");
			successer_query.append(" ) SELECT * FROM X WHERE X.LNR = ").append(warehouseNumber).append(" AND X.LEVEL <  5");
			
		List<PartDetails> alternativePartsDetailsList = dbServiceRepository.getResultUsingQuery(PartDetails.class,successer_query.toString(), true);
		
		return alternativePartsDetailsList;
	}

	/**
	 *  This method used to get base data for BA popup from DB
	 */
	@Override
	public List<partBADetailsDTO> getBaseDataforBAPopup(String dataLibrary,String partNo, String buisnessCase,String warehouseId, String manufacturer) {
		// TODO Auto-generated method stub
		log.info("Inside changesInBaseData method of PartsServiceImpl");
		List<partBADetailsDTO> partBADetailsDTOList = new ArrayList<>();
		try {
			manufacturer = manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING: manufacturer;
			
			StringBuilder query = new StringBuilder("select NW1_EW ,NW2_EW ,DAKEWV,DAKEWN,TNRVV ,TNRVN ,TNRNV ,TNRNN ,NPREIV ,NPREIN ,RABGRV ,RABGRN ,MARKEV ,MARKEN ,DRTKZV, DRTKZN ,ZPREIV ,ZPREIN , DTZPRV, DTZPRN ,TRUWEV, TRUWEN ,MCODEV, MCODE, MWSTV ,MWSTN ,TA_EWV , TA_EWN from ");
			query.append(dataLibrary).append(".E_CPSDAT where UPPER(KB||ETNR||ES1||ES2) = UPPER('").append(partNo).append("') ");
			query.append(" AND HERST = '").append(manufacturer).append("'");
			query.append(" AND VFNR= ").append(warehouseId);
			query.append(" AND BART ='").append(buisnessCase).append("'");

			List<PartBADetails> partBADetailList = dbServiceRepository.getResultUsingQuery(PartBADetails.class,
					query.toString(), true);
			partBADetailsDTO partDetailsDTO = null;
			if (partBADetailList != null && !partBADetailList.isEmpty()) {

				for (PartBADetails partDetail : partBADetailList) {

					if (buisnessCase.equalsIgnoreCase("49")) {
						partDetailsDTO = new partBADetailsDTO();

						partDetailsDTO.setValue(RestInputConstants.SATZART);
						partDetailsDTO.setPredecessor(partDetail.getSatzart_predecessor());
						partDetailsDTO.setSuccessor(partDetail.getSatzart_successor());

						partBADetailsDTOList.add(partDetailsDTO);

						partDetailsDTO = new partBADetailsDTO();

						partDetailsDTO.setValue(RestInputConstants.DAK);
						partDetailsDTO.setPredecessor(partDetail.getDak_predecessor());
						partDetailsDTO.setSuccessor(partDetail.getDak_successor());

						partBADetailsDTOList.add(partDetailsDTO);

					} else if (buisnessCase.equalsIgnoreCase("45")) {
						partDetailsDTO = new partBADetailsDTO();

						partDetailsDTO.setValue(RestInputConstants.VORGANGER);
						partDetailsDTO.setPredecessor(partDetail.getPredecessor_Alt());
						partDetailsDTO.setSuccessor(partDetail.getPredecessor_Neu());

						partBADetailsDTOList.add(partDetailsDTO);

						partDetailsDTO = new partBADetailsDTO();

						partDetailsDTO.setValue(RestInputConstants.NACHFOLGER);
						partDetailsDTO.setPredecessor(partDetail.getSuccessor_Alt());
						partDetailsDTO.setSuccessor(partDetail.getSuccessor_Neu());

						partBADetailsDTOList.add(partDetailsDTO);

					} else if (buisnessCase.equalsIgnoreCase("44")) {
						partDetailsDTO = new partBADetailsDTO();

						partDetailsDTO.setValue(RestInputConstants.FESTHINTERLEGTER_EINKAUFSPREIS);
						partDetailsDTO.setPredecessor(partDetail.getNetPrice_predecessor());
						partDetailsDTO.setSuccessor(partDetail.getNetPrice_successor());

						partBADetailsDTOList.add(partDetailsDTO);

						partDetailsDTO = new partBADetailsDTO();

						partDetailsDTO.setValue(RestInputConstants.RABATTGRUPPE);
						partDetailsDTO.setPredecessor(partDetail.getDiscount_pressor());
						partDetailsDTO.setSuccessor(partDetail.getDiscount_successor());

						partBADetailsDTOList.add(partDetailsDTO);

						partDetailsDTO = new partBADetailsDTO();

						partDetailsDTO.setValue(RestInputConstants.TEILEMARKE);
						partDetailsDTO.setPredecessor(partDetail.getPartBrand_pressor());
						partDetailsDTO.setSuccessor(partDetail.getPartBrand_successor());

						partBADetailsDTOList.add(partDetailsDTO);
					} else {
						partDetailsDTO = new partBADetailsDTO();

						partDetailsDTO.setValue(RestInputConstants.DIEBSTAHL_KENNZEICHEN);
						partDetailsDTO.setPredecessor(partDetail.getLicenseplate_Pressor());
						partDetailsDTO.setSuccessor(partDetail.getLicenseplate_Successor());

						partBADetailsDTOList.add(partDetailsDTO);

						partDetailsDTO = new partBADetailsDTO();

						partDetailsDTO.setValue(RestInputConstants.NEUER_LISTENPREIS);
						partDetailsDTO.setPredecessor(partDetail.getNewlistprice_Pressor());
						partDetailsDTO.setSuccessor(partDetail.getNewlistprice_SuccessorFrom());

						partBADetailsDTOList.add(partDetailsDTO);

						partDetailsDTO = new partBADetailsDTO();

						partDetailsDTO.setValue(RestInputConstants.NEUER_LISTENPREIS_AB);
						partDetailsDTO.setPredecessor(partDetail.getNewlistprice_PressorFrom());
						partDetailsDTO.setSuccessor(partDetail.getNewlistprice_SuccessorFrom());

						partBADetailsDTOList.add(partDetailsDTO);

						partDetailsDTO = new partBADetailsDTO();

						partDetailsDTO.setValue(RestInputConstants.RUCKGABEWERT);
						partDetailsDTO.setPredecessor(partDetail.getReturnvalue_Pressor());
						partDetailsDTO.setSuccessor(partDetail.getReturnvalue_Successor());

						partBADetailsDTOList.add(partDetailsDTO);

						partDetailsDTO = new partBADetailsDTO();

						partDetailsDTO.setValue(RestInputConstants.MARKETINGCODE);
						partDetailsDTO.setPredecessor(partDetail.getMarketingCode_predecessor());
						partDetailsDTO.setSuccessor(partDetail.getMarketingCode_successor());

						partBADetailsDTOList.add(partDetailsDTO);

						partDetailsDTO = new partBADetailsDTO();

						partDetailsDTO.setValue(RestInputConstants.MWST);
						partDetailsDTO.setPredecessor(partDetail.getVat_pressor());
						partDetailsDTO.setSuccessor(partDetail.getVat_successor());

						partBADetailsDTOList.add(partDetailsDTO);

						partDetailsDTO = new partBADetailsDTO();

						partDetailsDTO.setValue(RestInputConstants.RABATTGRUPPE);
						partDetailsDTO.setPredecessor(partDetail.getDiscount_pressor());
						partDetailsDTO.setSuccessor(partDetail.getDiscount_successor());

						partBADetailsDTOList.add(partDetailsDTO);

						partDetailsDTO = new partBADetailsDTO();

						partDetailsDTO.setValue(RestInputConstants.TEILEMARKE);
						partDetailsDTO.setPredecessor(partDetail.getPartBrand_pressor());
						partDetailsDTO.setSuccessor(partDetail.getPartBrand_successor());

						partBADetailsDTOList.add(partDetailsDTO);

						partDetailsDTO = new partBADetailsDTO();

						partDetailsDTO.setValue(RestInputConstants.TEILEART);
						partDetailsDTO.setPredecessor(partDetail.getTeileart_predecessor());
						partDetailsDTO.setSuccessor(partDetail.getTeileart_successor());

						partBADetailsDTOList.add(partDetailsDTO);

					}
				}
			} else {
				log.debug("  BA popUp List is empty");
			}
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(
					LocaleContextHolder.getLocale(), ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Base data for BA  "));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Base data for BA"),
					exception);
			throw exception;
		}

		return partBADetailsDTOList;

	}
	
	
	@Override
	public GlobalSearch globalPartsSearch(String dataLibrary, String schema, String companyId,String agencyId, String oem, String searchString, 
			String pageSize, String pageNo, String warehouseId) {
		log.info("Inside glogalPartsSearch method of PartsServiceImpl");

		List<PartGlobalSearch> partsList = new ArrayList<>();
		GlobalSearch globalSearchList = new GlobalSearch();
		
		try {

			validateCompany(companyId);
			validateAgency(agencyId);
			
			String warehouseIds = decryptUtil.getAllowedApWarehouse();
			
			if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
				warehouseIds = decryptUtil.getCompanyApWarehouse();
			}
			
			if(warehouseId!=null && !warehouseId.trim().isEmpty()){
				warehouseIds = warehouseId;
			}
			log.info("User's warehouse Ids ID xx:  {} ", warehouseIds);
			//validate the Warehouse 
			validateWarehouses(warehouseIds);

			if(pageSize==null || pageNo==null || pageSize.isEmpty() || pageNo.isEmpty()){
				pageSize = RestInputConstants.DEFAULT_PAGE_SIZE;
				pageNo = RestInputConstants.DEFAULT_PAGE_NO;
			}

			int totalRecords = Integer.parseInt(pageSize);
			int nextRows = totalRecords * (Integer.parseInt(pageNo) - 1);

			//validate the page size
			validatePageSize(totalRecords);
			
			log.info("PageSize  : {}  OFFSET Size (Skip Rows):  {} Page Number  : {} ", pageSize, nextRows, pageNo);
			
			if((searchString.startsWith("A") || searchString.startsWith("a")) && searchString.length() >= 11) {
				searchString = searchString.substring(0, 11) + '%' + searchString.substring(11);
			}

			//For ALPHAX-3326 
			List<String> splitSearchString = Arrays.stream(searchString.split("\\s+")).collect(Collectors.toList());
			
			oem = (oem != null && oem.equalsIgnoreCase(RestInputConstants.DAG_STRING))? RestInputConstants.DCAG_STRING : oem;
			//Faster without price
			/*StringBuilder etstamm_Columns = new StringBuilder("SELECT herst as OEM,tnr as TNR,lnr as LNR, benen as NAME, INT(SA) as SA, ");
			etstamm_Columns.append("INT(aktbes) as STOCK, tmarke as BRAND, cast(EPR as REAL) as PRICE, cast(RG as varchar(2)) AS RGROUP, ");		
			etstamm_Columns.append(" MC AS MC,PRKZ AS PRKZ,cast(MWST as varchar(2)) AS TAX  ");
			etstamm_Columns.append(", cast(GEWICH as REAL) AS GEWICH,LABEL AS LABEL,case substr(TVOLN, 1, 5)  when '     ' then 0 else int(substr(TVOLN, 1, 5)) END AS LAENGE, ");
			etstamm_Columns.append("case substr(TVOLN, 6, 5)when '     ' then 0 else int(substr(TVOLN, 6, 5)) END AS BREITE,");
			etstamm_Columns.append("case substr(TVOLN, 11, 5)when '     ' then 0 else int(substr(TVOLN, 11, 5)) END AS HOEHE, int(VERP1) AS VERP1, int(VERP2) AS VERP2 FROM ");
			
			StringBuilder tkfher_Columns = new StringBuilder("SELECT tkf_herst AS OEM, tkf_tnr AS TNR, -1 as LNR,tkf_benen AS NAME, int(-1) as SA, ");
			tkfher_Columns.append("int(0) as STOCK,tkf_tmarke AS BRAND,cast(tkf_BLP as REAL) AS PRICE,cast(tkf_RG as varchar(2)) AS RGROUP, ");		
			tkfher_Columns.append(" tkf_MC AS MC,tkf_PRKZ AS PRKZ,cast(tkf_TAXCDE as varchar(2))as TAX  ");
			tkfher_Columns.append(", CAST(tkf_GEWICH as REAL) AS GEWICH, '' AS LABEL, int(tkf_LAENGE) AS LAENGE, ");
			tkfher_Columns.append("int(tkf_BREITE) AS BREITE, int(tkf_HOEHE) AS HOEHE, int(tkf_VERP1) AS VERP1, ");
			tkfher_Columns.append("int(tkf_VERP2) AS VERP2 FROM ");
			
			StringBuilder tlekat_Columns = new StringBuilder("SELECT tle_herst AS OEM, tle_tnr AS TNR, -1 as LNR,tle_benena AS NAME,int(-1) as SA, int(0) as STOCK,tle_tmarke AS BRAND, ");
			tlekat_Columns.append("cast(TLE_BLP as REAL) AS PRICE, cast(TLE_RG as varchar(2)) AS RGROUP,TLE_MC ");		
			tlekat_Columns.append(" AS MC,TLE_PRKZ AS PRKZ,CAST(TLE_TAXCDE as varchar(2)) AS TAX  ");
			tlekat_Columns.append(" , CAST(TLE_GEWICH as REAL) AS GEWICH,");
			tlekat_Columns.append(" TLE_LABEL AS LABEL, int(TLE_LAENGE) AS LAENGE, int(TLE_BREITE) AS BREITE, int(TLE_HOEHE) AS HOEHE, int(TLE_VERP1) AS ");
			tlekat_Columns.append("VERP1, int(TLE_VERP2) AS VERP2 FROM ");*/
			
			//Faster with price
			
			StringBuilder etstamm_Columns = new StringBuilder("SELECT herst as OEM,tnr as TNR,lnr as LNR, benen as NAME, INT(SA) as SA, ");
			etstamm_Columns.append("INT(aktbes) as STOCK, tmarke as BRAND, cast(EPR as REAL) as PRICE, cast(RG as varchar(2)) AS RGROUP, ");		
			etstamm_Columns.append(" MC AS MC,PRKZ AS PRKZ,cast(MWST as varchar(2)) AS TAX  ");
			etstamm_Columns.append(", cast(GEWICH as REAL) AS GEWICH,LABEL AS LABEL,case substr(TVOLN, 1, 5)  when '     ' then 0 else int(substr(TVOLN, 1, 5)) END AS LAENGE, ");
			etstamm_Columns.append("case substr(TVOLN, 6, 5)when '     ' then 0 else int(substr(TVOLN, 6, 5)) END AS BREITE,");
			etstamm_Columns.append("case substr(TVOLN, 11, 5)when '     ' then 0 else int(substr(TVOLN, 11, 5)) END AS HOEHE, int(VERP1) AS VERP1, int(VERP2) AS VERP2, ");
			etstamm_Columns.append(" ( SELECT RAB_TBL from ").append(dataLibrary).append(".F_KUNRAB where KDNR = '99999900' and FZART = 'ET' AND MARKE = et.tmarke ");
			etstamm_Columns.append(" AND FIRMA = ").append(companyId).append(" ) AS RAB_TBL  FROM ");
			
			StringBuilder tkfher_Columns = new StringBuilder("SELECT tkf_herst AS OEM, tkf_tnr AS TNR, -1 as LNR,tkf_benen AS NAME, int(-1) as SA, ");
			tkfher_Columns.append("int(0) as STOCK,tkf_tmarke AS BRAND,cast(tkf_BLP as REAL) AS PRICE,cast(tkf_RG as varchar(2)) AS RGROUP, ");		
			tkfher_Columns.append(" tkf_MC AS MC,tkf_PRKZ AS PRKZ,cast(tkf_TAXCDE as varchar(2))as TAX  ");
			tkfher_Columns.append(", CAST(tkf_GEWICH as REAL) AS GEWICH, '' AS LABEL, int(tkf_LAENGE) AS LAENGE, ");
			tkfher_Columns.append("int(tkf_BREITE) AS BREITE, int(tkf_HOEHE) AS HOEHE, int(tkf_VERP1) AS VERP1, ");
			tkfher_Columns.append("int(tkf_VERP2) AS VERP2, ");
			tkfher_Columns.append("( SELECT RAB_TBL from ").append(dataLibrary).append(".F_KUNRAB where KDNR = '99999900' and FZART = 'ET' AND MARKE = tkf.tkf_tmarke ");
			tkfher_Columns.append(" AND FIRMA = ").append(companyId).append(" ) AS RAB_TBL FROM ");
			
			StringBuilder tlekat_Columns = new StringBuilder("SELECT tle_herst AS OEM, tle_tnr AS TNR, -1 as LNR,tle_benena AS NAME,int(-1) as SA, int(0) as STOCK,tle_tmarke AS BRAND, ");
			tlekat_Columns.append("cast(TLE_BLP as REAL) AS PRICE, cast(TLE_RG as varchar(2)) AS RGROUP,TLE_MC ");		
			tlekat_Columns.append(" AS MC,TLE_PRKZ AS PRKZ,CAST(TLE_TAXCDE as varchar(2)) AS TAX  ");
			tlekat_Columns.append(" , CAST(TLE_GEWICH as REAL) AS GEWICH,");
			tlekat_Columns.append(" TLE_LABEL AS LABEL, int(TLE_LAENGE) AS LAENGE, int(TLE_BREITE) AS BREITE, int(TLE_HOEHE) AS HOEHE, int(TLE_VERP1) AS ");
			tlekat_Columns.append("VERP1, int(TLE_VERP2) AS VERP2, ");
			tlekat_Columns.append("( SELECT RAB_TBL from ").append(dataLibrary).append(".F_KUNRAB where KDNR = '99999900' and FZART = 'ET' AND MARKE = etk.tle_tmarke ");
			tlekat_Columns.append(" AND FIRMA = ").append(companyId).append(" ) AS RAB_TBL FROM ");
			
			
			StringBuilder etstamm_where_clouse = new StringBuilder(dataLibrary).append(".E_ETSTAMM et WHERE ").append(createWhereClouse("tnr","benen",splitSearchString));
			etstamm_where_clouse.append(" herst='").append(oem).append("' ");
			etstamm_where_clouse.append(" AND LNR IN (").append(warehouseIds).append(") ");
			
			StringBuilder tkfher_where_clouse = new StringBuilder(schema).append(".etk_tkfher tkf WHERE ");
			tkfher_where_clouse.append(createWhereClouse("tkf_tnr","tkf_benen",splitSearchString));
			tkfher_where_clouse.append(" tkf_herst='").append(oem);
			tkfher_where_clouse.append("' AND tkf_dtgulv  <= current date ");
			
			StringBuilder tlekat_where_clouse = new StringBuilder(schema).append(".etk_tlekat etk WHERE ");
			tlekat_where_clouse.append( createWhereClouse("tle_tnr","tle_benena",splitSearchString));
			tlekat_where_clouse.append(" tle_dtgulv <= current date AND tle_herst =  '").append(oem).append("' ");

			String totalCount = "0";
			if( oem.equalsIgnoreCase("DCAG") ) {
			
				//get actual results
				StringBuilder queryForDCAG = new StringBuilder(etstamm_Columns).append(etstamm_where_clouse);
				queryForDCAG.append(" UNION ");
				queryForDCAG.append(tlekat_Columns).append(tlekat_where_clouse);
				queryForDCAG.append(" ORDER BY TNR OFFSET ");
				queryForDCAG.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY"); 
				
				partsList = dbServiceRepository.getResultUsingQuery(PartGlobalSearch.class, queryForDCAG.toString(),true);
			}else{
				
				//get actual results
				StringBuilder queryForNonDCAG = new StringBuilder(etstamm_Columns).append(etstamm_where_clouse);
				queryForNonDCAG.append(" UNION ");
				queryForNonDCAG.append(tkfher_Columns).append(tkfher_where_clouse);
				queryForNonDCAG.append(" ORDER BY TNR OFFSET ");
				queryForNonDCAG.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY");
				
				partsList = dbServiceRepository.getResultUsingQuery(PartGlobalSearch.class, queryForNonDCAG.toString(),true);
			}
			
			List<SearchPartsDTO> vehiclePartsList = new ArrayList<>();
			//if the list is not empty
			if (partsList != null && !partsList.isEmpty()) {
				for (PartGlobalSearch parts : partsList) {

					SearchPartsDTO partsDTO = new SearchPartsDTO();

					partsDTO.setOem(parts.getOem().equalsIgnoreCase(RestInputConstants.DCAG_STRING)? RestInputConstants.DAG_STRING: parts.getOem());
					partsDTO.setPartNumber(parts.getPartNumber());
					//partsDTO.setAgency(parts.getAgency()); //this field not in query
					partsDTO.setDescription(parts.getDescription());
					partsDTO.setOemBrand(parts.getOemBrand());
					
					if(parts.getWarehouse()!= null && !parts.getWarehouse().equalsIgnoreCase("-1")){
						
					partsDTO.setWarehouse(parts.getWarehouse());
					partsDTO.setStock(parts.getStock()!=null?parts.getStock():"-------");
					}
					else {
						partsDTO.setWarehouse("-------");
						partsDTO.setStock("-------");
						partsDTO.setAgency("------");
					}
					
					if(parts.getStorageIndikator()!= null && !parts.getStorageIndikator().equalsIgnoreCase("-1")){
						partsDTO.setStorageIndikator(parts.getStorageIndikator());
						partsDTO.setStorageIndikatorFlag(true);
					}else{
						partsDTO.setStorageIndikator("-----");
						partsDTO.setStorageIndikatorFlag(false);
					}
					
					partsDTO.setListPrice(String.valueOf(decimalformat_fixtwodigit.format(Double.parseDouble(parts.getPrice()))));
					partsDTO.setShoppingDiscountGroup(parts.getShoppingDiscountGroup());
					partsDTO.setMarketingCode(parts.getMarketingCode());
					partsDTO.setPriceIndicator(parts.getPriceIndicator());
					partsDTO.setVatRegistrationNumber(parts.getVatRegistrationNumber());
					partsDTO.setAssortmentClass(parts.getAssortmentClass());
					
					partsDTO.setWeight(String.valueOf(parts.getWeight()));
					//partsDTO.setDeposit(String.valueOf(parts.getDeposit())); // this field not in query
					partsDTO.setPartLabel(parts.getPartLabel());
					partsDTO.setPackagingUnit1(String.valueOf(parts.getPackagingUnit1()));
					partsDTO.setPackagingUnit2(String.valueOf(parts.getPackagingUnit2()));
					
					String length = StringUtils.leftPad(String.valueOf(parts.getLength()), 5, "0");
					String width = StringUtils.leftPad(String.valueOf(parts.getWidth()), 5, "0");
					String height = StringUtils.leftPad(String.valueOf(parts.getHeight()),5, "0");
					partsDTO.setLengthWidthHeight(StringUtils.join(length, width, height));
					partsDTO.setRabTbl(parts.getRabTbl());
					partsDTO.setDiscountGroup(parts.getDiscountGroup());

					vehiclePartsList.add(partsDTO);
				}
				globalSearchList.setSearchDetailsList(vehiclePartsList);
				globalSearchList.setTotalPages(totalCount);
				globalSearchList.setTotalRecordCnt(totalCount);
			} 
			else {
				globalSearchList.setSearchDetailsList(vehiclePartsList);
				globalSearchList.setTotalPages(Integer.toString(0));
				globalSearchList.setTotalRecordCnt(Integer.toString(0));
			}
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message: {}",  e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.SEARCH_FAILED_MSG_KEY, "Parts"));
			log.error(messageService.getReadableMessage(ExceptionMessages.SEARCH_FAILED_MSG_KEY, "Parts"), exception);
			throw exception;
		}

		return globalSearchList;
	}
	
	
	/**
	 * This is method is used to get List of parts Label for CSV creation.
	 */
	@Override
	public ByteArrayInputStream getPartsLabelForCSV (String dataLibrary, PartPrintingLabelDTO partlabelDTO, 
			boolean allStorageLocations) {

		log.info("Inside getPartsLabelForCSV method of PartsServiceImpl");

		ByteArrayInputStream  byteArrayOutputStream = null;

		final String[] headers = { "PrintingFormatPartNumber", "Name", "Warehouse", "StorageLocation", "data"};

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
			validateWarehouses(partlabelDTO.getWarehouse());
			
			List<String> data = Arrays.asList(
					partlabelDTO.getPrintingFormatPartNumber()!=null ? partlabelDTO.getPrintingFormatPartNumber(): "", // TNRD									
					partlabelDTO.getName(),	 //BENEN
					StringUtils.leftPad(String.valueOf(partlabelDTO.getWarehouse()), 2, "0"), //LNR
					partlabelDTO.getStorageLocation() != null ? partlabelDTO.getStorageLocation() : "", //LOPA
					createJsonString(partlabelDTO, partlabelDTO.getStorageLocation())
			);
			csvPrinter.printRecord(data);

			if(allStorageLocations) {

				//if the Storage location list is not empty.
				if (partlabelDTO.getStorageLocationList() != null && !partlabelDTO.getStorageLocationList().isEmpty()) {

					for(StorageLocationDTO storageLoc : partlabelDTO.getStorageLocationList()) {

						data = Arrays.asList(
								partlabelDTO.getPrintingFormatPartNumber()!=null ? partlabelDTO.getPrintingFormatPartNumber(): "", // TNRD									
								partlabelDTO.getName(),	 //BENEN
								StringUtils.leftPad(String.valueOf(partlabelDTO.getWarehouse()), 2, "0"), //LNR
								storageLoc.getStorageLocation() != null ? storageLoc.getStorageLocation() : "", //LOPA
								createJsonString(partlabelDTO, storageLoc.getStorageLocation())
						);
						
						csvPrinter.printRecord(data);
					}				
				}
			}		
			
			// writing the underlying stream
			csvPrinter.flush();
			byteArrayOutputStream = new ByteArrayInputStream(out.toByteArray());

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
	
	
	private String createJsonString(PartPrintingLabelDTO partLabels, String storageLoc) {

		Map<String, String> json_item = new LinkedHashMap<>();

		json_item.put(RestInputConstants.DATA_PRINTED_PARTNO, partLabels.getPrintingFormatPartNumber()!=null ? 
				partLabels.getPrintingFormatPartNumber(): "");
		json_item.put(RestInputConstants.DATA_PARTNAME, partLabels.getName()) ;
		json_item.put(RestInputConstants.DATA_STORAGE_LOC, storageLoc !=null ? storageLoc : "");
		json_item.put(RestInputConstants.DATA_OEM, partLabels.getOem().equalsIgnoreCase(RestInputConstants.DCAG_STRING)? 
				RestInputConstants.DAG_STRING : partLabels.getOem());

		String jsonStr = json_item.entrySet().stream()
				.map(x -> "\"" + x.getKey() + "\":\"" + x.getValue() + "\"")
				.collect(Collectors.joining(",", "{", "}"));

		return jsonStr;		
	}
	
	
	/**
	 * This is method is used to get parts List with Prices information.
	 */
	@Override
	public List<SearchPartsDTO> globalSearchPartsPrice (String dataLibrary, List<SearchPartsDTO> partsList) {

		log.info("Inside globalSearchPartsPrice method of PartsServiceImpl");

		List<SearchPartsDTO> updatedPartsList = new ArrayList<>();
		String surchargePriceLimit = "";

		try {

			//if the list is not empty 
			if (partsList != null && !partsList.isEmpty()) {

				StringBuilder surchargePriceQuery = new StringBuilder("SELECT CAST((CAST(substr(DATAFLD, 1, 7) AS REAL)/100) as DEC(7, 2)) AS surcharge_price_limit from ");
				surchargePriceQuery.append(dataLibrary).append(".REFERENZ where keyfld like '%2219%' ");

				List<PartGlobalSearch> surchargePriceList = dbServiceRepository.getResultUsingQuery(PartGlobalSearch.class, surchargePriceQuery.toString(), true);

				if(surchargePriceList != null && !surchargePriceList.isEmpty()) {
					surchargePriceLimit = surchargePriceList.get(0).getSurchargePriceLimit() != null ? surchargePriceList.get(0).getSurchargePriceLimit(): "";
				}

				for (SearchPartsDTO parts : partsList) {
					
					SearchPartsDTO updatedParts = parts;

					if(parts.getWarehouse() != null && !parts.getWarehouse().equalsIgnoreCase("-------")) {

						updatedParts.setPartPriceWithSurcharge( calculatePriceWithSurcharge(parts.getListPrice(), parts.getDiscountGroup(), parts.getOemBrand(),
								surchargePriceLimit, parts.getRabTbl()) );
					}
					updatedPartsList.add(updatedParts);
				}
			}
		}
		catch (AlphaXException ex) 
		{ throw ex; } 
		catch (Exception e) {
			log.info("Error Message: {}", e.getMessage()); AlphaXException exception =
					new AlphaXException(e,
							messageService.createApiMessage(LocaleContextHolder.getLocale(),
									ExceptionMessages.SEARCH_FAILED_MSG_KEY, "Parts"));
			log.error(messageService.getReadableMessage(ExceptionMessages.
					SEARCH_FAILED_MSG_KEY, "Parts"), exception); throw exception; }


		return updatedPartsList;
	}
	
	
	/**
	 * This is method is used to Calculate the parts Price with surcharge information.
	 */
	private String calculatePriceWithSurcharge (String partPrice, String discount_Group, String partBrand, String priceLimit, String rabTblValues ) {

		log.info("Inside calculatePriceWithSurcharge method of PartsServiceImpl");
		Integer surchargePosition = 0;
		Integer newSurchargePosition = 0;
		Integer surchargePercentage = 0;
		Double finalPriceWithsurchange = 0.0;

		try {
			Double purchasePrice = Double.parseDouble(partPrice);

			if(rabTblValues != null && !rabTblValues.isEmpty() && !priceLimit.isEmpty()) {

				Double priceLimitValue = Double.parseDouble(priceLimit);

				Integer discountGroup = Integer.parseInt((discount_Group != null && !discount_Group.trim().isEmpty()) ? discount_Group : "0");

				if(purchasePrice > priceLimitValue) {
					if(discountGroup == 0){
						surchargePosition = 898;   // Logic: ((100 - 1) * 9) + 7;
					}else {
						surchargePosition = ((discountGroup - 1) * 9) + 7;
					}						
				}
				else if(purchasePrice <= priceLimitValue) {
					if(discountGroup == 0){
						surchargePosition = 895;   //Logic: ((100 - 1) * 9) + 4;
					}
					else {
						surchargePosition = ((discountGroup - 1) * 9) + 4;
					}
				}
				
				newSurchargePosition = surchargePosition + 2;
				
				if(rabTblValues.length() >= newSurchargePosition) {

					surchargePercentage = Integer.parseInt(rabTblValues.substring(surchargePosition-1, newSurchargePosition));
					finalPriceWithsurchange = purchasePrice + ( purchasePrice * surchargePercentage / 1000 );
					return decimalformat_twodigit.format(finalPriceWithsurchange);
				}
				else {
					return decimalformat_twodigit.format(purchasePrice);
				}						
			}
			else {
				return decimalformat_twodigit.format(purchasePrice);
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
	}
	
	/**
	 * This method is used to get required warehouse detail list for part creation 
	 */
	@Override
	public List<LagerDetailsDTO>  getRequiredWarehouseList(String schema, String dataLibrary,String manufacturer,String partNumber, String allowedWarehouses) {
		log.info("Inside getRequiredWarehouseList method of PartsServiceImpl");
		List<LagerDetailsDTO> lagerList = new ArrayList<LagerDetailsDTO>();
		try {
			//validate the Warehouse Ids
			validateWarehouses(allowedWarehouses);
			
			manufacturer = manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING) ? RestInputConstants.DCAG_STRING : manufacturer;
			
			String finalWarehouseIds = "";
			StringBuilder query = new StringBuilder(" SELECT  LPAD(LNR , 2, 0) as AP_LNR  FROM ").append(dataLibrary).append(".E_ETSTAMM et WHERE ");
			query.append("HERST = ").append("'"+manufacturer+"'");
			query.append(" AND TNR = ").append("'"+partNumber+"'");

			List<PartDetails> etstammLNRForPart = dbServiceRepository.getResultUsingQuery(PartDetails.class, query.toString(), true);
			
			if(etstammLNRForPart!= null && !etstammLNRForPart.isEmpty()) {
			List<String> etstammWarehouse =  etstammLNRForPart.stream().map(PartDetails::getAlphaPlusWarehouseId).collect(Collectors.toList());
			List<String> listFromtoken = Stream.of(allowedWarehouses.split(",", -1)).collect(Collectors.toList());
			listFromtoken.removeAll(etstammWarehouse);
			finalWarehouseIds = listFromtoken.stream().map(String::valueOf).collect(Collectors.joining(","));
			log.info("ETSTAMM Warehouses for Part: {} "+etstammWarehouse.stream().map(String::valueOf).collect(Collectors.joining(",")));
			}else {
				finalWarehouseIds = allowedWarehouses;
			}
			log.info("allowed Warehouses: {} "+allowedWarehouses);
			log.info("Final Warehouse List for part creation: {} "+finalWarehouseIds);
			
			if(finalWarehouseIds.isEmpty()) {
			
				log.info("Part position already exists in all warehouse. BA17 new installation implausible.");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.BUSINESS_CASES_17_01FAILED_MSG_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.BUSINESS_CASES_17_01FAILED_MSG_KEY));
				throw exception;
			}
			
			//Warehouse details list for drop down
			
			StringBuilder query_lager = new StringBuilder("select WAREHOUS_ID, AP_WAREHOUS_ID, NAME, CITY, POST_CODE, STREETANDNUM, ISACTIVE, VF_NUMBER from ");
			query_lager.append(schema).append(".O_WRH").append(" where ISACTIVE = 1 and AP_WAREHOUS_ID  IN ( ").append(finalWarehouseIds).append(" ) order by AP_WAREHOUS_ID ");

			List<AdminWarehouse> lagerDetailsList = dbServiceRepository.getResultUsingQuery(AdminWarehouse.class, query_lager.toString(), true);

			if(lagerDetailsList != null && !lagerDetailsList.isEmpty()) {
				
				for(AdminWarehouse lagerDetails : lagerDetailsList) {
						
						LagerDetailsDTO lagerDetailsDto = new LagerDetailsDTO();
						
						lagerDetailsDto.setWarehouseNo(StringUtils.leftPad(lagerDetails.getAlphaPlusWarehouseId().toString(),2,"0"));
						lagerDetailsDto.setWarehouseName(lagerDetails.getWarehouseName());
						lagerDetailsDto.setAddress(new StringBuilder(lagerDetails.getStreetNumber()).append(", ")
								.append(lagerDetails.getPostalCode()).append(" ").append(lagerDetails.getCity()).toString());
						lagerDetailsDto.setVfNumber(String.valueOf(lagerDetails.getVfNumber()));
						
						String warehouseName = lagerDetails.getWarehouseName()!=null?lagerDetails.getWarehouseName():"";
						lagerDetailsDto.setWarehouseIdWithName(lagerDetailsDto.getWarehouseNo()+" - "+warehouseName);
						
						lagerList.add(lagerDetailsDto);
					}
			}
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Warehouse List"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, " Warehouse List"), exception);
			throw exception;
		}
		return lagerList;
	}
	
}				
			