package com.alphax.controller.mb;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
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
import com.alphax.service.mb.VehicleService;
import com.alphax.api.mb.VehiclesApi;
import com.alphax.common.constants.AlphaXCommonUtils;
import com.alphax.util.DecryptTokenUtils;
import com.alphax.common.constants.RestInputConstants;
import com.alphax.util.RestURIConstants;
import com.alphax.vo.mb.CustomerVegaDTO;
import com.alphax.vo.mb.DropdownObject;
import com.alphax.vo.mb.GlobalSearch;
import com.alphax.vo.mb.VehicleDetailsVO;
import com.alphax.vo.mb.VehicleStandardInfoVO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author A106744104
 *
 */
@RestController
@RequestMapping("/api")
@Slf4j
@Validated
public class VehiclesController implements VehiclesApi {
	
	@Autowired
	VehicleService vehicleService;

	@Autowired
	AlphaXCommonUtils commonUtils;
	
	@Autowired
	DecryptTokenUtils decryptUtil;
	
	
	/**
	 * This is VehiclesController method used to get the vehicles details List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getVehiclesSearchList(
			@RequestParam(value = "searchString", required = true) String searchString, 
			@RequestParam(value = "pageSize", required = false) String pageSize, 
			@RequestParam(value = "pageNumber", required = false) String pageNumber) {
		
		log.info("VehiclesController : Start getVehiclesSearchList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		GlobalSearch globalSearch = vehicleService.getVehiclesSearchList(decryptUtil.getDataLibrary(),
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), searchString, pageSize, pageNumber);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(globalSearch);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}

	
	/**
	 * This method is used to get the vehicles details using VIN no.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getVehiclesInfoByVin( 
			@NotBlank @RequestParam(value = "vin", required = true) String vin){			
		
		log.info("VehiclesController : Start getVehiclesDataByVin");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		VehicleStandardInfoVO vehicleStandardInfo = vehicleService.getVehiclesInfoByVin(decryptUtil.getDataLibrary(), decryptUtil.getSchema(), decryptUtil.getApCompanyId(), 
				decryptUtil.getApAgencyId(), vin );

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(vehicleStandardInfo);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used to Create the vehicles details.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> createVehicle( @Valid @RequestBody VehicleDetailsVO vehicleDtl) {

		log.info("VehiclesController : Start createVehicle.");
		AlphaXResponse<Object> response = new AlphaXResponse<>();
		VehicleDetailsVO vehicleDetails = null;
		Locale locale = LocaleContextHolder.getLocale();

		vehicleDetails = vehicleService.createOrUpdateVehicle(vehicleDtl, decryptUtil.getDataLibrary(),
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId() , RestInputConstants.CREATE_INDICATER_FLAG);

		if (vehicleDetails != null) {
			if (vehicleDetails.getReturnCode().equalsIgnoreCase("0011") && vehicleDetails.getPopUpMsg().isEmpty()) {
				response.setResponseMessages(commonUtils.buildAlphaXResponseEntity(response.getResponseMessages(),
						vehicleDetails.getReturnMsg(), "200", RestURIConstants.MSG_TYPE_SUCCESS, locale.getLanguage().toUpperCase()));
			} 
			else if (!vehicleDetails.getReturnCode().equalsIgnoreCase("0011")) {
				response.setResponseMessages(commonUtils.buildAlphaXResponseEntity(response.getResponseMessages(),
						vehicleDetails.getReturnMsg().trim(), "1111", RestURIConstants.MSG_TYPE_ERROR, locale.getLanguage().toUpperCase()));
			} 
			else if (!vehicleDetails.getPopUpMsg().isEmpty()) {
				response.setResponseMessages(commonUtils.buildAlphaXResponseEntity(response.getResponseMessages(),
						vehicleDetails.getPopUpMsg().trim(), "1111", RestURIConstants.MSG_TYPE_ERROR, locale.getLanguage().toUpperCase()));
			}
		} 
		else {
			response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, ExceptionMessages.VEHICLE_CREATE_FAIL_MSG_KEY));
		}
		response.setResultSet(vehicleDetails);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	/**
	 * This is VehiclesController method used to get the Marke (Brand) detail List for DDL
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getMarkeList() {
		
		log.info("VehiclesController : Start getMarkeList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		List<DropdownObject> markeList = vehicleService.getMarkeList(decryptUtil.getSchema(),
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId());
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(markeList);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This is VehiclesController method used to get the Zulassungsart (Registration type) detail List for DDL
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getZulassungsartList() {
		
		log.info("VehiclesController : Start getZulassungsartList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		List<DropdownObject> zulassungsartList = vehicleService.getZulassungsartList(decryptUtil.getDataLibrary(),
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId());
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(zulassungsartList);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This is VehiclesController method used to get the Antriebsart (Engine type) detail List for DDL
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getAntriebsartList() {
		
		log.info("VehiclesController : Start getAntriebsartList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		List<DropdownObject> antriebsartList = vehicleService.getAntriebsartList(decryptUtil.getDataLibrary(),
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId());
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(antriebsartList);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This is VehiclesController method used to get the Laufleistung (Mileage indicator) detail List for DDL
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getLaufleistungList() {
		
		log.info("VehiclesController : Start getLaufleistungList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		List<DropdownObject> LaufleistungList = vehicleService.getLaufleistungList();
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(LaufleistungList);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}

	
	/**
	 * This is VehiclesController method used to get the Verkaufer (Salesman) detail List for DDL
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getVerkauferList() {
		
		log.info("VehiclesController : Start getVerkauferList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		List<DropdownObject> verkauferList = vehicleService.getVerkauferList(decryptUtil.getDataLibrary(),
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId());
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(verkauferList);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}

	
	/**
	 * This is VehiclesController method used to get the Verkaufssparte (sales division) detail List for DDL
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getVerkaufssparteList() {
		
		log.info("VehiclesController : Start getVerkaufssparteList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		List<DropdownObject> verkaufssparteList = vehicleService.getVerkaufssparteList(decryptUtil.getDataLibrary(),
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId());
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(verkaufssparteList);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This is VehiclesController method used to get the Akquisitionssperre (Acquisition lock) detail List for DDL
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getAkquisitionspereList() {
		
		log.info("VehiclesController : Start getAkquisitionspereList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		List<DropdownObject> akquisitionspereList = vehicleService.getAkquisitionspereList(decryptUtil.getDataLibrary(),
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId());
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(akquisitionspereList);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This is VehiclesController method used to get the Wartungs-Gruppe (Maintenance group) detail List for DDL
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getWartGruppeList() {
		
		log.info("VehiclesController : Start getWartGruppeList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		List<DropdownObject> wartGruppeList = vehicleService.getWartGruppeList();
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(wartGruppeList);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This is VehiclesController method used to get the Kundendienst-Berater (Customer Service Advisor) detail List for DDL
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getKundendBeraterList() {
		
		log.info("VehiclesController : Start getKundendBeraterList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		List<DropdownObject> beraterList = vehicleService.getKundendBeraterList(decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId());
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(beraterList);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This is VehiclesController method used to get the Betreuende Werkstatt (Supporting workshop) detail List for DDL
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getBetreuendeWerkList() {
		
		log.info("VehiclesController : Start getBetreuendeWerkList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		List<DropdownObject> betreuendeWerkList = vehicleService.getBetreuendeWerkList(decryptUtil.getSchema(), decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId());
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(betreuendeWerkList);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	@Override
	public ResponseEntity<AlphaXResponse> getVehicleDetailsByID( 
			@PathVariable(value = "id", required = true) String id ) {

		log.info("VehiclesController : Start getVehicleDetailsByID");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		VehicleDetailsVO vehicleDtls= vehicleService.getVehicleDetailsByID(decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), id);
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(vehicleDtls);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This is VehiclesController method used to get the KundenNr VEGA details List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getKundenNr_VEGAList() {
		
		log.info("VehiclesController : Start getKundenNr_VEGAList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<CustomerVegaDTO> customerVegaList = vehicleService.getKundenNr_VEGAList(decryptUtil.getDataLibrary(),
				decryptUtil.getSchema(),decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(customerVegaList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This is VehiclesController method used to get the KundenNr details List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getKundenNrList( 
			@RequestParam(value = "searchString", required = false) String searchString, 
			@RequestParam(value = "pageSize", required = false) String pageSize, 
			@RequestParam(value = "pageNumber", required = false) String pageNumber) {
		
		log.info("VehiclesController : Start getKundenNrList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		GlobalSearch globalSearch = vehicleService.getKundenNrList(decryptUtil.getDataLibrary(),
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), searchString, pageSize, pageNumber);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(globalSearch);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This is VehiclesController method used to get the Aktions List for DDL
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getAktionList() {
		
		log.info("VehiclesController : Start getAktionList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		List<DropdownObject> aktionsList = vehicleService.getAktionList(decryptUtil.getSchema());
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(aktionsList);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}

	
	/**
	 * This is VehiclesController method used to Update the Vehicle details in DB.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> updateVehicle( @Valid @RequestBody VehicleDetailsVO vehicleDtl) {
		
		log.info("VehiclesController : Start updateVehicle.");
		
		AlphaXResponse<Object> response = new AlphaXResponse<>();
		VehicleDetailsVO vehicleDetails = null;
		Locale locale = LocaleContextHolder.getLocale();
		
		vehicleDetails = vehicleService.createOrUpdateVehicle(vehicleDtl, decryptUtil.getDataLibrary(),
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(),RestInputConstants.EDIT_INDICATER_FLAG );
		
		if (vehicleDetails != null) {
			if (vehicleDetails.getReturnCode().equalsIgnoreCase("0020") && vehicleDetails.getPopUpMsg().isEmpty()) {
				response.setResponseMessages(commonUtils.buildAlphaXResponseEntity(response.getResponseMessages(),
						vehicleDetails.getReturnMsg(), "200", RestURIConstants.MSG_TYPE_SUCCESS, locale.getLanguage().toUpperCase()));
			} 
			else if (!vehicleDetails.getReturnCode().equalsIgnoreCase("0020")) {
				response.setResponseMessages(commonUtils.buildAlphaXResponseEntity(response.getResponseMessages(),
						vehicleDetails.getReturnMsg().trim(), "1111", RestURIConstants.MSG_TYPE_ERROR, locale.getLanguage().toUpperCase()));
			} 
			else if (!vehicleDetails.getPopUpMsg().isEmpty()) {
				response.setResponseMessages(commonUtils.buildAlphaXResponseEntity(response.getResponseMessages(),
						vehicleDetails.getPopUpMsg().trim(), "1111", RestURIConstants.MSG_TYPE_ERROR, locale.getLanguage().toUpperCase()));
			}
		}
		else {
			response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, ExceptionMessages.VEHICLE_CREATE_FAIL_MSG_KEY));
		}
		response.setResultSet(vehicleDetails);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	/**
	 * This is VehiclesController method used to Check the Duplicate Liecence plate number in DB.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> checkDuplicateLicencePlate( 
			@NotBlank @RequestParam(value = "licencePlate", required = true) String licencePlate) {
		
		log.info("VehiclesController : Start checkDuplicateLicencePlate");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		Map<String, Boolean> isDuplicateMap = vehicleService.checkDuplicateLicencePlate(licencePlate, decryptUtil.getDataLibrary(),
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId());
		
		if(isDuplicateMap.get("isDuplicate")) {
			alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
					LocaleContextHolder.getLocale(), ExceptionMessages.LICENCE_DUPLICATE_MSG_KEY));
		}
		else {
			alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
					LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		}
		alphaxResponse.setResultSet(isDuplicateMap);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	

	/**
	 * This is VehiclesController method used to Check the Duplicate vehicle identification number in DB.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> checkDuplicateVIN(
			@NotBlank @RequestParam(value = "vin", required = true) String vin)	{
		
		log.info("VehiclesController : Start checkDuplicateVIN");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		Map<String, Boolean> isDuplicateMap = vehicleService.checkDuplicateVIN(vin, decryptUtil.getDataLibrary(),
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId());
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
					LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
	
		alphaxResponse.setResultSet(isDuplicateMap);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
}