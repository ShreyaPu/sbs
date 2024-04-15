package com.alphax.controller.mb;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alphax.common.exception.ExceptionMessages;
import com.alphax.common.response.AlphaXResponse;
import com.alphax.service.mb.ReportSelectionsService;
import com.alphax.api.mb.ReportSelectionsApi;
import com.alphax.common.constants.AlphaXCommonUtils;
import com.alphax.util.DecryptTokenUtils;
import com.alphax.vo.mb.GlobalSearch;
import com.alphax.vo.mb.MovementSearchDTO;
import com.alphax.vo.mb.ReportForBookingAccountChangesDTO;
import com.alphax.vo.mb.ReportForBookingStockDifferencesDTO;
import com.alphax.vo.mb.ReportSelectionsForBusinessCaseDTO;
import com.alphax.vo.mb.ReportSelectionsForInventoryDTO;
import com.alphax.common.constants.RestInputConstants;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
@Validated
public class ReportSelectionsController implements ReportSelectionsApi {
	
	@Autowired
	AlphaXCommonUtils commonUtils;
	
	@Autowired
	ReportSelectionsService reportSelectionsService;
	
	@Autowired
	DecryptTokenUtils decryptUtil;

	
	/**
	 * This method is used for get (Selektion aus Bestand) Selections From Stock List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getSelectionsFromStock(
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@NotEmpty @RequestParam(value = "warehouseNos", required = true) List<String> warehouseNos,
			@RequestParam(value = "partExpiryDate", required = false) boolean partExpiryDate,
			@RequestParam(value = "withHoldings", required = false) boolean withHoldings,
			@RequestParam(value = "withoutInventory", required = false) boolean withoutInventory,
			@RequestParam(value = "negativeInventory", required = false) boolean negativeInventory,
			@RequestParam(value = "relevantToTheft", required = false) boolean relevantToTheft,
			@RequestParam(value = "oldPart", required = false) boolean oldPart,
			@RequestParam(value = "recordType1", required = false) boolean recordType1,
			@RequestParam(value = "recordType2", required = false) boolean recordType2,
			@RequestParam(value = "hazardousMaterialPart", required = false) boolean hazardousMaterialPart,
			@RequestParam(value = "multipleStorageLocations", required = false) boolean multipleStorageLocations,
			@RequestParam(value = "pageSize", required = false) String pageSize, 
			@RequestParam(value = "pageNumber", required = false) String pageNumber) {
		
		log.info("ReportSelectionsController : Start getSelectionsFromStock");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}
		
		GlobalSearch reportSelections = reportSelectionsService.getSelectionsFromStock(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(),
				allowedApWarehouses, manufacturer, warehouseNos, partExpiryDate, withHoldings, withoutInventory, negativeInventory, relevantToTheft, 
				oldPart, recordType1, recordType2, hazardousMaterialPart, multipleStorageLocations, pageSize, pageNumber );
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(reportSelections);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used generate a report from Stock (Selektion aus Bestand - CSV ) 
	 */
	@Override
	public <T> ResponseEntity generateReportFromStock(
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@NotEmpty @RequestParam(value = "warehouseNos", required = true) List<String> warehouseNos,
			@RequestParam(value = "partExpiryDate", required = false) boolean partExpiryDate,
			@RequestParam(value = "withHoldings", required = false) boolean withHoldings,
			@RequestParam(value = "withoutInventory", required = false) boolean withoutInventory,
			@RequestParam(value = "negativeInventory", required = false) boolean negativeInventory,
			@RequestParam(value = "relevantToTheft", required = false) boolean relevantToTheft,
			@RequestParam(value = "oldPart", required = false) boolean oldPart,
			@RequestParam(value = "recordType1", required = false) boolean recordType1,
			@RequestParam(value = "recordType2", required = false) boolean recordType2,
			@RequestParam(value = "hazardousMaterialPart", required = false) boolean hazardousMaterialPart,
			@RequestParam(value = "multipleStorageLocations", required = false) boolean multipleStorageLocations){

		log.info("ReportSelectionsController : Start generateReportFromStock");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}
		
		ByteArrayInputStream resource = reportSelectionsService.generateReportFromStock(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), decryptUtil.getAxCompanyId(), 
				allowedApWarehouses, manufacturer, warehouseNos, partExpiryDate, withHoldings, withoutInventory, negativeInventory, 
				relevantToTheft, oldPart, recordType1, recordType2, hazardousMaterialPart , multipleStorageLocations);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(resource);

		if(resource != null){
			InputStreamResource file = new InputStreamResource(resource);
			
			// setting HTTP headers
			HttpHeaders headers = new HttpHeaders();
			
			headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + RestInputConstants.RPT_SELECTION_STOCK);
			// defining the custom Content-Type
			headers.set(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8;");
			
			return  new ResponseEntity<>(file, headers, HttpStatus.OK);
		}
		else{
			return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
		}
	}
	
	
	/**
	 * This method is used for get List of (Selektion nach Gängigkeit) Selections based on  marketability.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getSelectionsBasedMarketability(
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@NotEmpty @RequestParam(value = "warehouseNos", required = true) List<String> warehouseNos,
			@NotBlank @RequestParam(value = "noOfCommonParts", required = true) String noOfCommonParts,
			@NotBlank @RequestParam(value = "periods", required = true) String periods,
			@NotBlank @RequestParam(value = "sortOrder", required = true) String sortOrder,
			@NotBlank @RequestParam(value = "moventType", required = true) String moventType,
			@RequestParam(value = "pageSize", required = false) String pageSize, 
			@RequestParam(value = "pageNumber", required = false) String pageNumber) {
		
		log.info("ReportSelectionsController : Start getSelectionsBasedMarketability");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}
		
		GlobalSearch reportSelections = reportSelectionsService.getSelectionsBasedMarketability(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), 
				decryptUtil.getApCompanyId(), allowedApWarehouses, manufacturer, warehouseNos, noOfCommonParts, periods, sortOrder, 
				moventType, pageSize, pageNumber );
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(reportSelections);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used generate a report based on marketability  (Selektion aus Gängigkeit - CSV)
	 */
	@Override
	public <T> ResponseEntity generateReportBasedOnMarketability(
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@NotEmpty @RequestParam(value = "warehouseNos", required = true) List<String> warehouseNos,
			@NotBlank @RequestParam(value = "noOfCommonParts", required = true) String noOfCommonParts,
			@NotBlank @RequestParam(value = "periods", required = true) String periods,
			@NotBlank @RequestParam(value = "sortOrder", required = true) String sortOrder,
			@NotBlank @RequestParam(value = "moventType", required = true) String moventType){
		
		log.info("ReportSelectionsController : Start generateReportFromStock");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}
		
		ByteArrayInputStream resource = reportSelectionsService.generateReportBasedOnMarketability(decryptUtil.getSchema(), decryptUtil.getDataLibrary(),
				decryptUtil.getAxCompanyId(), allowedApWarehouses, manufacturer, warehouseNos, noOfCommonParts, periods, sortOrder, moventType );
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(resource);
		
		if(resource != null){
			InputStreamResource file = new InputStreamResource(resource);

			// setting HTTP headers
			HttpHeaders headers = new HttpHeaders();
			headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + RestInputConstants.RPT_SELECTION_MKTABTY);
			
			// defining the custom Content-Type
			headers.set(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8;");
			
			return  new ResponseEntity<>(file, headers, HttpStatus.OK);
		}else{		
			return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
		}		
		}
		
	
	/**
	 * This method is used for get Selections based on Inventory Structure (Bestandstruktur) List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getSelectionsForInventory(
			@NotBlank @RequestParam(value = "selectedType", required = true) String selectedType,
			@NotEmpty @RequestParam(value = "warehouseNos", required = true) List<String> warehouseNos,
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@NotBlank @RequestParam(value = "partBrand", required = true) String partBrand){
		
		log.info("ReportSelectionsController : Start getSelectionsFromStock");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}
		
		List<ReportSelectionsForInventoryDTO> reportSelections = reportSelectionsService.getSelectionsForInventory(decryptUtil.getSchema(), decryptUtil.getDataLibrary(),
				decryptUtil.getApCompanyId(), allowedApWarehouses, warehouseNos, manufacturer, partBrand, selectedType);
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(reportSelections);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used generate a report from Inventory Structure ( Bestandstruktur - CSV )
	 * 
	 */
	@Override
	public <T> ResponseEntity generateReportForInventoryStructure(
			@Valid @RequestBody List<ReportSelectionsForInventoryDTO> selectedInventoryList,
			@NotBlank @RequestParam(value = "selectedType", required = true) String selectedType,
			@NotEmpty @RequestParam(value = "warehouseNos", required = true) List<String> warehouseNos,
			@RequestParam(value = "manufacturer", required = false) String manufacturer, 			
			@RequestParam(value = "partBrand", required = false) String partBrand) {
		
		log.info("ReportSelectionsController : Start generateReportForInventoryStructure");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}
		
		ByteArrayInputStream resource = reportSelectionsService.generateReportForInventoryStructure(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), 
				decryptUtil.getAxCompanyId(), allowedApWarehouses, selectedInventoryList, selectedType, warehouseNos, manufacturer, partBrand);
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(resource);
		
		if(resource != null){
			InputStreamResource file = new InputStreamResource(resource);

			// setting HTTP headers
			HttpHeaders headers = new HttpHeaders();
			headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + RestInputConstants.RPT_SELECTION_INVSTR);
			
			// defining the custom Content-Type
			headers.set(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8;");
			
			return  new ResponseEntity<>(file, headers, HttpStatus.OK);
		}else{		
			return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
		}		
	}
	
	
	/**
	 * This method is used for get Selections based on Business Case Statistics for DAK (Geschäftsfallstatistiken / Änderung DAK)  List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getBusinessCaseStatistics_DAK(
			@NotBlank @RequestParam(value = "fromDate", required = true) String fromDate,
			@NotBlank @RequestParam(value = "toDate", required = true) String toDate){
		
		log.info("ReportSelectionsController : Start getBusinessCaseStatistics_DAK");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}
		
		List<ReportSelectionsForBusinessCaseDTO> reportSelections = reportSelectionsService.getBusinessCaseStatistics_DAK(decryptUtil.getSchema(), 
				decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), allowedApWarehouses, fromDate, toDate );
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(reportSelections);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for get Selections based on Business Case Statistics for ManualCount (Geschäftsfallstatistiken / Manuelle Zählung)  List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getBusinessCaseStatistics_ManualCount(
			@NotBlank @RequestParam(value = "fromDate", required = true) String fromDate,
			@NotBlank @RequestParam(value = "toDate", required = true) String toDate){
		
		log.info("ReportSelectionsController : Start getBusinessCaseStatistics_ManualCount");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}
		
		List<ReportSelectionsForBusinessCaseDTO> reportSelections = reportSelectionsService.getBusinessCaseStatistics_ManualCount(decryptUtil.getSchema(), 
				decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), allowedApWarehouses, fromDate, toDate );
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(reportSelections);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for get Selections based on Business Case Statistics for ManualCorrection (Geschäftsfallstatistiken / Manuelle Korrektur)  List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getBusinessCaseStatistics_ManualCorrection(
			@NotBlank @RequestParam(value = "fromDate", required = true) String fromDate,
			@NotBlank @RequestParam(value = "toDate", required = true) String toDate){
		
		log.info("ReportSelectionsController : Start getBusinessCaseStatistics_ManualCorrection");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}
		
		List<ReportSelectionsForBusinessCaseDTO> reportSelections = reportSelectionsService.getBusinessCaseStatistics_ManualCorrection(decryptUtil.getSchema(), 
				decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), allowedApWarehouses, fromDate, toDate );
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(reportSelections);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for get Selections based on Business Case Statistics for Redemptions (Geschäftsfallstatistiken / Rücknahmen)  List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getBusinessCaseStatistics_Redemptions(
			@NotBlank @RequestParam(value = "fromDate", required = true) String fromDate,
			@NotBlank @RequestParam(value = "toDate", required = true) String toDate){
		
		log.info("ReportSelectionsController : Start getBusinessCaseStatistics_Redemptions");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}
		
		List<ReportSelectionsForBusinessCaseDTO> reportSelections = reportSelectionsService.getBusinessCaseStatistics_Redemptions(decryptUtil.getSchema(), 
				decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), allowedApWarehouses, fromDate, toDate );
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(reportSelections);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for get Selections based on Business Case Statistics for WithoutInventoryMovement (Geschäftsfallstatistiken / Gutschrift ohne Bestandsbewegung)  List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getBusinessCaseStatistics_WithoutInventoryMovement(
			@NotBlank @RequestParam(value = "fromDate", required = true) String fromDate,
			@NotBlank @RequestParam(value = "toDate", required = true) String toDate){
		
		log.info("ReportSelectionsController : Start getBusinessCaseStatistics_WithoutInventoryMovement");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}
		
		List<ReportSelectionsForBusinessCaseDTO> reportSelections = reportSelectionsService.getBusinessCaseStatistics_WithoutInventoryMovement(decryptUtil.getSchema(), 
				decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), allowedApWarehouses, fromDate, toDate );
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(reportSelections);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used generate a report based on BA Statistics (Geschäftsfallstatistik - CSV)
	 * 
	 */
	@Override
	public <T> ResponseEntity generateReportBasedOnBAStatistics(
			@NotBlank @RequestParam(value = "fromDate", required = true) String fromDate,
			@NotBlank @RequestParam(value = "toDate", required = true) String toDate){
		
		log.info("ReportSelectionsController : Start generateReportBasedOnBAStatistics");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}
		
		InputStream resource = reportSelectionsService.generateReportBasedOnBAStatistics(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), 
				decryptUtil.getApCompanyId(), allowedApWarehouses, fromDate, toDate );
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(resource);
		
		if(resource != null){
			InputStreamResource file = new InputStreamResource(resource);

			// setting HTTP headers
			HttpHeaders headers = new HttpHeaders();
			headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + RestInputConstants.RPT_SELECTION_BASTATS);
			
			// defining the custom Content-Type
			headers.set(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8;");
			
			return  new ResponseEntity<>(file, headers, HttpStatus.OK);
		}else{		
			return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
		}		
		}
		
	
	/**
	 * This method is used for get (Selektion aus Bewegung) Selections From Movement List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getSelectionsFromMovement(
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@NotEmpty @RequestParam(value = "warehouseNos", required = true) List<String> warehouseNos,
			@NotBlank @RequestParam(value = "fromDate", required = true) String fromDate,
			@NotBlank @RequestParam(value = "toDate", required = true) String toDate,
			@RequestParam(value = "bas", required = false) String bas,
			@RequestParam(value = "invtCustPos", required = false) boolean invtCustPos,
			@RequestParam(value = "custInvtPos", required = false) boolean custInvtPos,
			@RequestParam(value = "dak", required = false) boolean dak,
			@RequestParam(value = "chngOfmktgCode", required = false) boolean chngOfmktgCode,
			@RequestParam(value = "partTypeOrSupplier", required = false) boolean partTypeOrSupplier,
			@RequestParam(value = "documentNo", required = false) String documentNo,
			@RequestParam(value = "customerNo", required = false) String customerNo,
			@RequestParam(value = "supplierNo", required = false) String supplierNo,
			@RequestParam(value = "partNo", required = false) String partNo,
			@RequestParam(value = "furMktgCode", required = false) String furMktgCode,
			@RequestParam(value = "brand", required = false) String brand,
			@RequestParam(value = "pageSize", required = false) String pageSize, 
			@RequestParam(value = "pageNumber", required = false) String pageNumber){
		
		log.info("ReportSelectionsController : Start getSelectionsFromMovement");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}
		
		GlobalSearch reportSelections = reportSelectionsService.getSelectionsFromMovement(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(),
				allowedApWarehouses, manufacturer, warehouseNos, fromDate, toDate, bas, invtCustPos, custInvtPos, dak, chngOfmktgCode, 
				partTypeOrSupplier, documentNo,	customerNo, supplierNo, partNo, furMktgCode, brand, pageSize, pageNumber);
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(reportSelections);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	/**
	 * This method is used generate a report from movent (Selektion aus Bewegung - CSV )
	 * 
	 */
	@Override
	public <T> ResponseEntity generateReportFromMovement(
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@NotEmpty @RequestParam(value = "warehouseNos", required = true) List<String> warehouseNos,
			@NotBlank @RequestParam(value = "fromDate", required = true) String fromDate,
			@NotBlank @RequestParam(value = "toDate", required = true) String toDate,
			@RequestParam(value = "bas", required = false) String bas,
			@RequestParam(value = "invtCustPos", required = false) boolean invtCustPos,
			@RequestParam(value = "custInvtPos", required = false) boolean custInvtPos,
			@RequestParam(value = "dak", required = false) boolean dak,
			@RequestParam(value = "chngOfmktgCode", required = false) boolean chngOfmktgCode,
			@RequestParam(value = "partTypeOrSupplier", required = false) boolean partTypeOrSupplier,
			@RequestParam(value = "documentNo", required = false) String documentNo,
			@RequestParam(value = "customerNo", required = false) String customerNo,
			@RequestParam(value = "supplierNo", required = false) String supplierNo,
			@RequestParam(value = "partNo", required = false) String partNo,
			@RequestParam(value = "furMktgCode", required = false) String furMktgCode,
			@RequestParam(value = "brand", required = false) String brand){

		log.info("ReportSelectionsController : Start generateReportFromMovement");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}
		
		ByteArrayInputStream resource = reportSelectionsService.generateReportFromMovement(decryptUtil.getSchema(),decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), 
				allowedApWarehouses, manufacturer, warehouseNos, fromDate, toDate, bas,invtCustPos,custInvtPos, dak, chngOfmktgCode, 
				partTypeOrSupplier, documentNo,	customerNo, supplierNo,partNo, furMktgCode, brand);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(resource);

		if(resource != null){
			InputStreamResource file = new InputStreamResource(resource);
			
			// setting HTTP headers
			HttpHeaders headers = new HttpHeaders();
			
			headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + RestInputConstants.RPT_SELECTION_MVEMNT);
			// defining the custom Content-Type
			headers.set(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8;");
			
			return  new ResponseEntity<>(file, headers, HttpStatus.OK);
		}
		else{
			return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
		}
	}
	
	
	/**
	 * This method is used to get the Booking related List (Buchungsrelevante Listen)
	 */
	@Override
	public ResponseEntity<AlphaXResponse> generateBookingReletedList(
			@NotEmpty @RequestParam(value = "warehouseNos", required = true) List<String> warehouseNos,
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@NotBlank @RequestParam(value = "fromDate", required = true) String fromDate,
			@NotBlank @RequestParam(value = "toDate", required = true) String toDate,
			@RequestParam(value = "includeArchivedData", required = false) boolean includeArchivedData){
		
		log.info("ReportSelectionsController : Start generateBookingReletedList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}
		
		List<ReportSelectionsForBusinessCaseDTO> reportSelections = reportSelectionsService.generateBookingReletedList(decryptUtil.getSchema(),  
				decryptUtil.getDataLibrary(), allowedApWarehouses, warehouseNos, manufacturer, fromDate, toDate, includeArchivedData);
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(reportSelections);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used generate a report from Booking related List (Buchungsrelevante Listen - CSV)
	 * 
	 */
	@Override
	public <T> ResponseEntity generateReportOnBookingReletedList(
			@NotEmpty @RequestParam(value = "warehouseNos", required = true) List<String> warehouseNos,
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@NotBlank @RequestParam(value = "fromDate", required = true) String fromDate,
			@NotBlank @RequestParam(value = "toDate", required = true) String toDate,
			@RequestParam(value = "includeArchivedData", required = false) boolean includeArchivedData) {
		
		log.info("ReportSelectionsController : Start generateReportOnBookingReletedList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}
		
		ByteArrayInputStream resource = reportSelectionsService.generateReportOnBookingReletedList(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), 
				allowedApWarehouses, warehouseNos, manufacturer, fromDate, toDate, includeArchivedData);
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(resource);
		
		if(resource != null){
			InputStreamResource file = new InputStreamResource(resource);

			// setting HTTP headers
			HttpHeaders headers = new HttpHeaders();
			headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + RestInputConstants.RPT_SELECTION_BOOKING);
			
			// defining the custom Content-Type
			headers.set(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8;");
			
			return  new ResponseEntity<>(file, headers, HttpStatus.OK);
		}else{		
			return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
		}		
	}
	
	
	/**
	 * This method is used Get a List for Search columns (Selektion aus Bewegung)
	 */
	@Override
	public ResponseEntity<AlphaXResponse> searchMovementList(
			@NotBlank @RequestParam(value = "searchType", required = true) String searchType,
			@NotBlank @RequestParam(value = "searchValue", required = true) String searchValue,
			@RequestParam(value = "archData", required = false) boolean archData) {
		
		log.info("ReportSelectionsController : Start searchMovementList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<MovementSearchDTO> reportSelections = reportSelectionsService.searchMovementList(decryptUtil.getDataLibrary(), searchType, searchValue, archData );
				
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(reportSelections);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used to get the Parts Supply related List (Teileanbietung)
	 */
	@Override
	public ResponseEntity<AlphaXResponse> generatePartSupplyRelatedList(
			@RequestParam(value = "pageSize", required = false) String pageSize,
			@RequestParam(value = "pageNumber", required = false) String pageNumber) {
		
		log.info("ReportSelectionsController : Start generatePartSupplyRelatedList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}
		
		GlobalSearch reportSelections = reportSelectionsService.generatePartSupplyRelatedList(decryptUtil.getSchema(),  
				decryptUtil.getDataLibrary(), allowedApWarehouses, decryptUtil.getApCompanyId(), pageSize, pageNumber);
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(reportSelections);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used generate a report for Parts Supply related List (Teileanbietung - CSV)
	 * 
	 */
	@Override
	public <T> ResponseEntity generateReportForPartSupply() {
		
		log.info("ReportSelectionsController : Start generateReportForPartSupply");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}
		
		ByteArrayInputStream resource = reportSelectionsService.generateReportForPartSupply(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), 
				allowedApWarehouses, decryptUtil.getApCompanyId());
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(resource);
		
		if(resource != null){
			InputStreamResource file = new InputStreamResource(resource);

			// setting HTTP headers
			HttpHeaders headers = new HttpHeaders();
			headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + RestInputConstants.RPT_SELECTION_PRTSUPLY);
			
			// defining the custom Content-Type
			headers.set(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8;");
			
			return  new ResponseEntity<>(file, headers, HttpStatus.OK);
		}else{		
			return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
		}		
	}
	
	
	/**
	 * This method is used to get the Intra Trade Statistics List (Intrahandelsstatistik)
	 */
	@Override
	public ResponseEntity<AlphaXResponse> generateIntraTradeStatisticsList(
			@NotBlank @RequestParam(value = "fromDate", required = true) String fromDate,
			@NotBlank @RequestParam(value = "toDate", required = true) String toDate,
			@RequestParam(value = "customerNo", required = false) String customerNo,
			@RequestParam(value = "customerGroup", required = false) String customerGroup,
			@RequestParam(value = "pageSize", required = false) String pageSize,
			@RequestParam(value = "pageNumber", required = false) String pageNumber) {
		
		log.info("ReportSelectionsController : Start generateIntraTradeStatisticsList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		GlobalSearch reportSelections = reportSelectionsService.generateIntraTradeStatisticsList(decryptUtil.getSchema(),  
				decryptUtil.getDataLibrary(), fromDate, toDate, customerNo, customerGroup, pageSize, pageNumber);
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(reportSelections);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used generate a report for Intra Trade Statistics (Intrahandelsstatistik - CSV)
	 * 
	 */
	@Override
	public <T> ResponseEntity generateReportIntraTradeStatistics(
			@NotBlank @RequestParam(value = "fromDate", required = true) String fromDate,
			@NotBlank @RequestParam(value = "toDate", required = true) String toDate,
			@RequestParam(value = "customerNo", required = false) String customerNo,
			@RequestParam(value = "customerGroup", required = false) String customerGroup) {
		
		log.info("ReportSelectionsController : Start generateReportIntraTradeStatistics");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		ByteArrayInputStream resource = reportSelectionsService.generateReportIntraTradeStatistics(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), 
				fromDate, toDate, customerNo, customerGroup);
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(resource);
		
		if(resource != null){
			InputStreamResource file = new InputStreamResource(resource);

			// setting HTTP headers
			HttpHeaders headers = new HttpHeaders();
			headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + RestInputConstants.RPT_SELECTION_INTRATRDE);
			
			// defining the custom Content-Type
			headers.set(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8;");
			
			return  new ResponseEntity<>(file, headers, HttpStatus.OK);
		}else{		
			return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
		}		
	}
	
	
	/**
	 * This method is used to get the Booking relevant - StockDiff List (Buchungsrelevante Listen)
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getBookingRelevantStockDiffList(
			@NotEmpty @RequestParam(value = "warehouseNos", required = true) List<String> warehouseNos,
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@NotBlank @RequestParam(value = "fromDate", required = true) String fromDate,
			@NotBlank @RequestParam(value = "toDate", required = true) String toDate) {
		
		log.info("ReportSelectionsController : Start getBookingRelevantStockDiffList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}
		
		List<ReportForBookingStockDifferencesDTO> reportSelections = reportSelectionsService.getBookingRelevantStockDiffList(decryptUtil.getSchema(),  
				decryptUtil.getDataLibrary(), allowedApWarehouses, warehouseNos, manufacturer, fromDate, toDate);
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(reportSelections);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used to get the Booking relevant - AccountChange List (Kontoänderungen Listen)
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getBookingRelevantAccountChangeList(
			@NotEmpty @RequestParam(value = "warehouseNos", required = true) List<String> warehouseNos,
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@NotBlank @RequestParam(value = "fromDate", required = true) String fromDate,
			@NotBlank @RequestParam(value = "toDate", required = true) String toDate) {
		
		log.info("ReportSelectionsController : Start getBookingRelevantAccountChangeList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}
		
		List<ReportForBookingAccountChangesDTO> reportSelections = reportSelectionsService.getBookingRelevantAccountChangeList(decryptUtil.getSchema(),  
				decryptUtil.getDataLibrary(), allowedApWarehouses, warehouseNos, manufacturer, fromDate, toDate);
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(reportSelections);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used generate a report from Booking relevant - StockDiff List (Buchungsrelevante Listen - CSV)
	 * 
	 */
	@Override
	public <T> ResponseEntity generateReportForBookingRelevant_StockDifferences(
			@NotEmpty @RequestParam(value = "warehouseNos", required = true) List<String> warehouseNos,
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@NotBlank @RequestParam(value = "fromDate", required = true) String fromDate,
			@NotBlank @RequestParam(value = "toDate", required = true) String toDate) {
		
		log.info("ReportSelectionsController : Start generateReportForBookingRelevant_StockDifferences");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}
		
		ByteArrayInputStream resource = reportSelectionsService.generateReportForBookingRelevant_StockDifferences(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), 
				allowedApWarehouses, warehouseNos, manufacturer, fromDate, toDate);
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(resource);
		
		if(resource != null){
			InputStreamResource file = new InputStreamResource(resource);

			// setting HTTP headers
			HttpHeaders headers = new HttpHeaders();
			headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + RestInputConstants.RPT_SELECTION_STOCKDIFF);
			
			// defining the custom Content-Type
			headers.set(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8;");
			
			return  new ResponseEntity<>(file, headers, HttpStatus.OK);
		}
		else {		
			return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
		}		
	}
	
	
	/**
	 * This method is used generate a report from Booking relevant - AccountChange List (Kontoänderungen Listen -CSV)
	 * 
	 */
	@Override
	public <T> ResponseEntity generateReportForBookingRelevant_AccountChange(
			@NotEmpty @RequestParam(value = "warehouseNos", required = true) List<String> warehouseNos,
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@NotBlank @RequestParam(value = "fromDate", required = true) String fromDate,
			@NotBlank @RequestParam(value = "toDate", required = true) String toDate) {
		
		log.info("ReportSelectionsController : Start generateReportForBookingRelevant_AccountChange");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		String reportName = "SelectionsFromBookingAccountChangeList.csv";

		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}
		
		ByteArrayInputStream resource = reportSelectionsService.generateReportForBookingRelevant_AccountChange(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), 
				allowedApWarehouses, warehouseNos, manufacturer, fromDate, toDate);
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(resource);
		
		if(resource != null){
			InputStreamResource file = new InputStreamResource(resource);

			// setting HTTP headers
			HttpHeaders headers = new HttpHeaders();
			headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + reportName);
			
			// defining the custom Content-Type
			headers.set(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8;");
			
			return  new ResponseEntity<>(file, headers, HttpStatus.OK);
		}
		else{		
			return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
		}		
	}
	

	/**
	 * This method is used to get the list of Parts Labels for Printing.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getPartsLabelforPrinting(
			@NotBlank @RequestParam(value = "pageSize", required = true) String pageSize, 
			@NotBlank @RequestParam(value = "pageNumber", required = true) String pageNumber,
			@NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer,
			@RequestParam(value = "partNumberRangeFlag", required = true) boolean partNumberRangeFlag, 
			@RequestParam(value = "allParts", required = true) boolean allParts,
			@RequestParam(value = "allStorageLocations", required = true) boolean allStorageLocations,
			@RequestParam(value = "fromPartNumber", required = false) String fromPartNumber,
			@RequestParam(value = "toPartNumber", required = false) String toPartNumber,
			@RequestParam(value = "fromStorageLocation", required = false) String fromStorageLocation,
			@RequestParam(value = "toStorageLocation", required = false) String toStorageLocation) {
		
		log.info("ReportSelectionsController : Start getPartsLabelforPrinting");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		GlobalSearch labelPrintingList = reportSelectionsService.getPartsLabelforPrinting(decryptUtil.getDataLibrary(), pageSize, pageNumber, 
				warehouseNo, manufacturer, partNumberRangeFlag, allParts, allStorageLocations, fromPartNumber, toPartNumber, fromStorageLocation, toStorageLocation,
				decryptUtil.getApCompanyId());
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(labelPrintingList);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used to get the list of Parts Labels for CSV file.
	 */
	@Override
	public <T> ResponseEntity getPartsLabelforPrintingCSV(
			@NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer,
			@RequestParam(value = "partNumberRangeFlag", required = true) boolean partNumberRangeFlag, 
			@RequestParam(value = "allParts", required = true) boolean allParts,
			@RequestParam(value = "allStorageLocations", required = true) boolean allStorageLocations,
			@RequestParam(value = "fromPartNumber", required = false) String fromPartNumber,
			@RequestParam(value = "toPartNumber", required = false) String toPartNumber,
			@RequestParam(value = "fromStorageLocation", required = false) String fromStorageLocation,
			@RequestParam(value = "toStorageLocation", required = false) String toStorageLocation) {
		
		log.info("ReportSelectionsController : Start getPartsLabelforPrintingCSV");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		String reportName = "PartLabels.csv";
		
		ByteArrayInputStream resource = reportSelectionsService.getPartsLabelforPrintingCSV(decryptUtil.getDataLibrary(), decryptUtil.getSchema(), warehouseNo, manufacturer, 
				partNumberRangeFlag, allParts, allStorageLocations, fromPartNumber, toPartNumber, fromStorageLocation, toStorageLocation);
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(resource);
		
		if(resource != null){
			InputStreamResource file = new InputStreamResource(resource);

			// setting HTTP headers
			HttpHeaders headers = new HttpHeaders();
			headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + reportName);
			
			// defining the custom Content-Type
			headers.set(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8;");
			
			return  new ResponseEntity<>(file, headers, HttpStatus.OK);
		}
		else{		
			return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
		}
		
	}
	
	
}