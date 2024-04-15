package com.alphax.controller.mb;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alphax.common.exception.ExceptionMessages;
import com.alphax.common.response.AlphaXResponse;
import com.alphax.service.mb.PartsService;
import com.alphax.api.mb.PartsApi;
import com.alphax.common.constants.AlphaXCommonUtils;
import com.alphax.util.DecryptTokenUtils;
import com.alphax.util.RestURIConstants;
import com.alphax.vo.mb.BAWrapperDTO;
import com.alphax.vo.mb.DropdownObject;
import com.alphax.vo.mb.GlobalSearch;
import com.alphax.vo.mb.LagerDetailsDTO;
import com.alphax.vo.mb.LagerPartDetailsDTO;
import com.alphax.vo.mb.PartDetailsDTO;
import com.alphax.vo.mb.PartPrintingLabelDTO;
import com.alphax.vo.mb.PartsTreeViewDTO;
import com.alphax.vo.mb.SearchPartsDTO;
import com.alphax.vo.mb.StorageLocationDTO;
import com.alphax.vo.mb.partBADetailsDTO;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
@Validated
public class PartsController implements PartsApi {

	@Autowired
	PartsService partsService;

	@Autowired
	AlphaXCommonUtils commonUtils;

	@Autowired
	DecryptTokenUtils decryptUtil;


	/**
	 * This method is used for get Parts List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getPartsList( 
			@NotBlank @RequestParam(value = "oem", required = true) String oem,
			@RequestParam(value = "warehouseId", required = false) String warehouseId,
			@RequestParam(value = "searchString", required = true) String searchString,
			@RequestParam(value = "pageSize", required = false) String pageSize, 
			@RequestParam(value = "pageNumber", required = false) String pageNumber) {

		log.info("PartsController : Start getPartsList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		GlobalSearch globalSearch = partsService.getPartsList(decryptUtil.getDataLibrary(), decryptUtil.getSchema(), decryptUtil.getApCompanyId(), 
				decryptUtil.getAllowedApAgencies(), oem, searchString, pageSize, pageNumber, warehouseId);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(globalSearch);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used for get Original Equipment Manufacturer (OEM) List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getManufacturerList() {

		log.info("PartsController : Start getManufacturerList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<DropdownObject> manufacturerList = partsService.getManufacturerList(decryptUtil.getDataLibrary(), decryptUtil.getSchema());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(manufacturerList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used for get Preis Kennzeichen (Price indicator) List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getPreisKzList() {

		log.info("PartsController : Start getPreisKzList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<DropdownObject> preisKzList = partsService.getPreisKzList();

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(preisKzList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used for get Abschlag Kennzeichen (Discount mark) List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getAbschlagKzList() {

		log.info("PartsController : Start getAbschlagKzList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<DropdownObject> abschlagKzList = partsService.getAbschlagKzList();

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(abschlagKzList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used for get Kategorien (Categories) List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getCategoryList(
			@RequestParam(value = "kategorie", required = true) String kategorie) {

		log.info("PartsController : Start getCategoryList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<DropdownObject> categoriesList = partsService.getCategoryList(decryptUtil.getDataLibrary(), kategorie);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(categoriesList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used to get the part details based on provided part number.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getNewPartDetailsByPartNumber( 
			@NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "companyId", required = true) String companyId, 
			@NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "agencyId", required = true) String agencyId, 
			@NotBlank @RequestParam(value = "warehouseId", required = true) String warehouseId,
			@NotBlank @RequestParam(value = "oem", required = true) String oem,
			@PathVariable(value = "id", required = true) String id ) {

		log.info("PartsController : Start getNewPartDetailsByPartNumber");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		PartDetailsDTO partDetails = partsService.getNewPartDetailsByPartNumber(decryptUtil.getDataLibrary(), decryptUtil.getSchema(), companyId, agencyId, 
				warehouseId, oem, id);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(partDetails);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used to get the Lager (Warehouse) details based on provided Part number.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getLagerDetailsBasedOnPart(@PathVariable(value = "id", required = true) String id,
			@NotBlank @RequestParam(value = "warehouseId", required = true) String warehouseId,
			@NotBlank @RequestParam(value = "oem", required = true) String oem) {

		log.info("PartsController : Start getLagerDetailsBasedOnPart");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}

		List<LagerPartDetailsDTO> lagerDetailsList = partsService.getLagerDetailsBasedOnPart(id, decryptUtil.getDataLibrary(), oem, warehouseId, allowedApWarehouses, 
				decryptUtil.getSchema());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(lagerDetailsList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);

	}


	/**
	 * This method is used to get the Movement data details (Bewegungs / Ereignisdaten) based on provided part number.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getMovementDataUsingPartNumber( @PathVariable(value = "id", required = true) String id,
			@NotBlank @RequestParam(value = "warehouseId", required = true) String warehouseId,
			@NotBlank @RequestParam(value = "oem", required = true) String oem,
			@NotBlank @RequestParam(value = "flag", required = true) String flag,
			@RequestParam(value = "pageSize", required = false) String pageSize, 
			@RequestParam(value = "pageNumber", required = false) String pageNumber ) {

		log.info("PartsController : Start getNewPartDetailsByPartNumber");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		GlobalSearch globalSearch = partsService.getMovementDataUsingPartNumber(id, decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), 
				decryptUtil.getApAgencyId(), warehouseId, oem, flag, pageSize, pageNumber);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(globalSearch);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used to get the LOPA (Storage Location) details based on provided Part number.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getStorageLocationList(
			@PathVariable(value = "id", required = true) String id,
			@NotBlank @RequestParam(value = "warehouseId", required = true) String warehouseId,
			@NotBlank @RequestParam(value = "oem", required = true) String oem) {

		log.info("PartsController : Start getStorageLocationList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<StorageLocationDTO> lopaList = partsService.getStorageLocationList(id, decryptUtil.getDataLibrary(), warehouseId, oem);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(lopaList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);

	}


	/**
	 * This method is used to Create / Update the Parts using BA4X.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> createUpdateParts( 
			@RequestBody BAWrapperDTO baWrapperDTO) {

		log.info("PartsController : Start createUpdateParts");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		Locale locale = LocaleContextHolder.getLocale();

		Map<String, Map<String,String>> baResponse = partsService.createUpdateParts(baWrapperDTO, decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), 
				decryptUtil.getApAgencyId(), decryptUtil.getSchema());

		if(baResponse != null && baResponse.size() > 0) {

			for(Map.Entry<String, Map<String,String>> entryMap : baResponse.entrySet()) {

				Map<String, String> responseMsgMap = entryMap.getValue();

				if(responseMsgMap !=null && responseMsgMap.size() > 0) {
					if (responseMsgMap.get("returnCode").equalsIgnoreCase("00000")) {
						alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), locale, 
								ExceptionMessages.SUCCESS_MESSAGE_KEY));
					}
					else {
						alphaxResponse.setResponseMessages(commonUtils.buildAlphaXResponseEntity(alphaxResponse.getResponseMessages(),
								responseMsgMap.get("returnMsg"), responseMsgMap.get("returnCode"), RestURIConstants.MSG_TYPE_ERROR, locale.getLanguage().toUpperCase()));
					} 
				}
				else {
					alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), locale, 
							ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Parts"));
				}
			}
		}
		else {
			alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), locale, ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Parts"));
		}

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used to get the Discount group and value  (Rabattsatz , Rabattgruppe) list from DB table E_RAB.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getDiscountGroupAndValue(
			 @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer,
			 @NotBlank @RequestParam(value = "marke", required = true) String marke) {

		log.info("PartsController : Start getDiscountGroupAndValue");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<DropdownObject> discountAndValueList = partsService.getDiscountGroupAndValue(decryptUtil.getDataLibrary(), manufacturer, marke );

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(discountAndValueList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);

	}


	/**
	 * This method is used to get the Messwert des externen Rollgeräuschs  (Measured value) list from DB table e_etlabel
	 * @return List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getMeasuredValueList(){

		log.info("PartsController : Start getMeasuredValueList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<DropdownObject> measuredValueList = partsService.getMeasuredValueList(decryptUtil.getDataLibrary());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(measuredValueList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);		
	}


	/**
	 * This method is used to get the Reifenklassifizierung  (Tire classification) list from DB table e_etlabel
	 * @return List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getTireClassificationList(){

		log.info("PartsController : Start getTireClassificationList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<DropdownObject> tireClassificationList = partsService.getTireClassificationList(decryptUtil.getDataLibrary());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(tireClassificationList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);		
	}


	/**
	 * This API is used to get Marketing Code return values (OMCGET pgm).
	 * @return Map
	 */
	@Override
	public ResponseEntity<AlphaXResponse> executeOMCGETCLPgm(
			@NotBlank @RequestParam(value = "warehouseId", required = true) String warehouseId,
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer,
			@NotBlank @RequestParam(value = "marketingCode", required = true) String marketingCode,
			@RequestParam(value = "partNumber", required = false) String partNumber,
			@RequestParam(value = "discountGroup", required = false) String discountGroup) {

		log.info("PartsController : Start executeOMCGETCLPgm");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		Locale locale = LocaleContextHolder.getLocale();

		Map<String, String> mcReturnValuesResponse = partsService.executeOMCGETCLPgm(decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), 
				decryptUtil.getApAgencyId(), warehouseId, manufacturer, marketingCode, partNumber, discountGroup);

		if (mcReturnValuesResponse != null && !mcReturnValuesResponse.isEmpty()) {

			if (mcReturnValuesResponse.get("returnCode").equalsIgnoreCase("00000")) {

				alphaxResponse.setResultSet(mcReturnValuesResponse);

				alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), locale, ExceptionMessages.SUCCESS_MESSAGE_KEY));
			}
			else {
				alphaxResponse.setResponseMessages(commonUtils.buildAlphaXResponseEntity(alphaxResponse.getResponseMessages(),
						mcReturnValuesResponse.get("returnMsg"), mcReturnValuesResponse.get("returnCode"), RestURIConstants.MSG_TYPE_ERROR, locale.getLanguage().toUpperCase()));
			} 
		}
		else {
			alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), locale, 
					ExceptionMessages.PROGRAM_FAILED_MSG_KEY, "OMCGETCL"));
		}
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used to create/update/delete the LOPA (Storage Location) details based on provided Part number.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> createUpdateStorageLocation(
			@Valid @RequestBody List<StorageLocationDTO> storageLocationDTOList,
			@PathVariable(value = "id", required = true) String id,
			@NotBlank @RequestParam(value = "warehouseId", required = true) String warehouseId,
			@NotBlank @RequestParam(value = "oem", required = true) String oem) {

		log.info("PartsController : Start createUpdateStorageLocation");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> updatedMapping = partsService.createUpdateStorageLocation(storageLocationDTOList, decryptUtil.getDataLibrary(), id, warehouseId, oem);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(updatedMapping);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);

	}


	/**
	 * This method is used to get the Stock information details based on provided Part number.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getStockInformationOfPart(
			@PathVariable(value = "id", required = true) String id,
			@NotBlank @RequestParam(value = "oem", required = true) String oem,
			@RequestParam(value = "pageSize", required = false) String pageSize,
			@RequestParam(value = "pageNumber", required = false) String pageNumber,
			@NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo) {

		log.info("PartsController : Start getStockInformationOfPart");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		GlobalSearch stockInformationList = partsService.getStockInformationOfPart(decryptUtil.getDataLibrary(), decryptUtil.getSchema(), id, oem, 
				pageSize, pageNumber, warehouseNo, decryptUtil.getAllowedApWarehouse());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(stockInformationList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);		
	}


	/**
	 * This method is used to convert the part number from one format into another using Logic of SYSBRA PGM.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> convertPartNoFormats(
			@NotBlank @RequestParam(value = "partNo", required = true) String partNo,
			@NotBlank @RequestParam(value = "vonValue", required = true) String vonValue,
			@NotBlank @RequestParam(value = "nachValue", required = true) String nachValue) {

		log.info("PartsController : Start convertPartNoFormats");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, String> formattedPartNo = partsService.convertPartNoFormats(partNo, vonValue, nachValue);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(formattedPartNo);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);

	}

	/**
	 * This method used to get the Local Alternative Details List for tree view.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getLocalAlternativesDetailsInTreeView( 
			@NotBlank @RequestParam(value = "partNo", required = true) String partNo,
			@RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@RequestParam(value = "warehouseNo", required = true) String warehouseNo) {

		log.info("PartsController : Start getLocalAlternativesDetailsInTreeView");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<PartsTreeViewDTO> alternativePartsDetailsList = partsService.getLocalAlternativesDetailsInTreeView(decryptUtil.getSchema(), 
				decryptUtil.getDataLibrary(), partNo, manufacturer, warehouseNo);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(alternativePartsDetailsList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}

	
	/**
	 * This method used to get base data for BA popup.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getBaseDataforBAPopup(
			@NotBlank @RequestParam(value = "partNo",required = true) String partNo,
	        @NotBlank @RequestParam(value = "buisnessCase",required = true)String buisnessCase,
	        @NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
	        @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer){
		
		log.info("PartsController : Start changesInBaseData");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		List<partBADetailsDTO> partBADetailList = partsService.getBaseDataforBAPopup( decryptUtil.getDataLibrary(), partNo, buisnessCase, warehouseNo,manufacturer);
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(partBADetailList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);

	}	
	
	/**
	 * This method is used for get Parts List (Global Parts Search)
	 */
	@Override
	public ResponseEntity<AlphaXResponse> globalPartsSearch( 
			@NotBlank @RequestParam(value = "oem", required = true) String oem,
			@RequestParam(value = "warehouseId", required = false) String warehouseId,
			@RequestParam(value = "searchString", required = true) String searchString,
			@RequestParam(value = "pageSize", required = false) String pageSize, 
			@RequestParam(value = "pageNumber", required = false) String pageNumber) {

		log.info("PartsController : Start getPartsList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		GlobalSearch globalSearch = partsService.globalPartsSearch(decryptUtil.getDataLibrary(), decryptUtil.getSchema(), decryptUtil.getApCompanyId(), 
				decryptUtil.getAllowedApAgencies(), oem, searchString, pageSize, pageNumber, warehouseId);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(globalSearch);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}

	
	/**
	 * This method is used to get the list of Parts Labels for CSV file.
	 */
	@Override
	public <T> ResponseEntity getPartsLabelForCSV (
			@Valid @RequestBody PartPrintingLabelDTO partlabelDTO,
			@RequestParam(value = "allStorageLocations", required = true) boolean allStorageLocations) {
		
		log.info("PartsController : Start getPartsLabelForCSV");
		
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		String reportName = "PartLabel.csv";
		
		ByteArrayInputStream resource = partsService.getPartsLabelForCSV(decryptUtil.getDataLibrary(), 
				partlabelDTO, allStorageLocations);
		
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
	 * This method is used for get Parts Price List (Global Parts Search)
	 */
	@Override
	public ResponseEntity<AlphaXResponse> globalSearchPartsPrice( 
			@Valid @RequestBody List<SearchPartsDTO> partsList ) {

		log.info("PartsController : Start globalSearchPartsPrice");
		
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<SearchPartsDTO> updatedPartsList = partsService.globalSearchPartsPrice(decryptUtil.getDataLibrary(), partsList);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(updatedPartsList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used to get required warehouse detail list for part creation 
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getRequiredWarehouseList( 
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer,
			@NotBlank @RequestParam(value = "partNumber", required = true) String partNumber) {		

		log.info("PartsController : Start getRequiredWarehouseList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<LagerDetailsDTO> lagerList = partsService.getRequiredWarehouseList(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), 
				 manufacturer, partNumber, decryptUtil.getAllowedApWarehouse() );

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(lagerList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}

}