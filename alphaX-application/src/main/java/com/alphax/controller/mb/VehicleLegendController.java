/**
 * 
 */
package com.alphax.controller.mb;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alphax.common.exception.ExceptionMessages;
import com.alphax.common.response.AlphaXResponse;
import com.alphax.service.mb.VehicleLegendService;
import com.alphax.api.mb.VehicleLegendApi;
import com.alphax.common.constants.AlphaXCommonUtils;
import com.alphax.util.DecryptTokenUtils;
import com.alphax.vo.mb.DropdownObject;
import com.alphax.vo.mb.GlobalSearch;

import lombok.extern.slf4j.Slf4j;

/**
 * @author A106744104
 *
 */
@RestController
@RequestMapping("/api")
@Slf4j
@Validated
public class VehicleLegendController implements VehicleLegendApi {
	
	@Autowired
	VehicleLegendService vehicleLegendService;

	@Autowired
	AlphaXCommonUtils commonUtils;
	
	@Autowired
	DecryptTokenUtils decryptUtil;
	
	
	/**
	 * This is VehicleLegendController method used to get the Vehicle Legend details.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getVehicleLegend( @PathVariable(value = "id", required = true) String vehicleId,
			@RequestParam(value = "pageSize", required = false) String pageSize, 
			@RequestParam(value = "pageNumber", required = false) String pageNumber) {

		log.info("VehicleLegendController : Start getVehicleLegend");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		GlobalSearch globalSearch  = vehicleLegendService.getVehicleLegend( vehicleId, decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), 
				pageSize, pageNumber );
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(globalSearch);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This is VehicleLegendController method used to get the Reparaturkennzeichen (Repair Codes) detail List for DDL
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getRepairCodeList() 
	{
		
		log.info("VehicleLegendController : Start getRepairCodeList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		List<DropdownObject> repairCodeList = vehicleLegendService.getRepairCodeList(decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId());
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(repairCodeList);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
}