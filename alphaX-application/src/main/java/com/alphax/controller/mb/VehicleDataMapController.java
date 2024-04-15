/**
 * 
 */
package com.alphax.controller.mb;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alphax.common.exception.ExceptionMessages;
import com.alphax.common.response.AlphaXResponse;
import com.alphax.service.mb.VehicleDataMapService;
import com.alphax.api.mb.VehicleDataMapApi;
import com.alphax.common.constants.AlphaXCommonUtils;
import com.alphax.util.DecryptTokenUtils;
import com.alphax.vo.mb.VehicleDataMapDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author A106744104
 *
 */
@RestController
@RequestMapping("/api")
@Slf4j
@Validated
public class VehicleDataMapController implements VehicleDataMapApi {
	
	@Autowired
	VehicleDataMapService vehicleDataMapService;

	@Autowired
	AlphaXCommonUtils commonUtils;
	
	@Autowired
	DecryptTokenUtils decryptUtil;
	
	
	/**
	 * This is VehicleLegendController method used to get the Vehicle Data map details.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getVehicleDataMap( @PathVariable(value = "id", required = true) String vehicleId)	{

		log.info("VehicleLegendController : Start getVehicleDataMap");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		VehicleDataMapDTO vehicleDataMapDTO  = vehicleDataMapService.getVehicleDataMap( vehicleId, decryptUtil.getDataLibrary());
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(vehicleDataMapDTO);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
}