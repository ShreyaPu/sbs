/**
 * 
 */
package com.alphax.controller.mb;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

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
import com.alphax.service.mb.BuybackProcessService;
import com.alphax.api.mb.BuybackProcessApi;
import com.alphax.common.constants.AlphaXCommonUtils;
import com.alphax.util.DecryptTokenUtils;
import com.alphax.vo.mb.BuybackHeaderDTO;
import com.alphax.vo.mb.BuybackHeaderDetailsDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author A87740971
 *
 */
@RestController
@RequestMapping("/api")
@Slf4j
@Validated
public class BuybackProcessController implements BuybackProcessApi{

	@Autowired
	AlphaXCommonUtils commonUtils;

	@Autowired
	BuybackProcessService buybackProcessService;

	@Autowired
	DecryptTokenUtils decryptUtil;


	/**
	 * This method is used to return list of BBOF Offene (Open) header information.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getBuybackOpenHeaderInfo(
			@RequestParam(value = "warehouseNos", required = false) List<String> warehouseNos) {

		log.info("BuybackProcessController : Start getBuybackOpenHeaderInfo");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<BuybackHeaderDTO> buybackHeaderDTO  = buybackProcessService.getBuybackOpenHeaderInfo(decryptUtil.getSchema(),decryptUtil.getAllowedApWarehouse(), warehouseNos);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(buybackHeaderDTO);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used to return list of BBOF Offene (Open) header's item detail.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getBuybackOpenHeaderItemDetails(
			@NotBlank @RequestParam(value = "headerId", required = true) String headerId,
			@NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "alphaPlusWarehouseNo", required = true) String alphaPlusWarehouseNo) {

		log.info("BuybackProcessController : Start getBuybackOpenHeaderItemDetails");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<BuybackHeaderDetailsDTO> buybackHeaderDetailsDTO  = buybackProcessService.getBuybackOpenHeaderItemDetails(alphaPlusWarehouseNo, headerId,
				decryptUtil.getSchema(), decryptUtil.getDataLibrary());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(buybackHeaderDetailsDTO);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used to return list of BBOF Ausgebuchte/Geschlossene (closed) header information.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getBuybackClosedHeaderInfo(
			@RequestParam(value = "warehouseNos", required = false) List<String> warehouseNos) {

		log.info("BuybackProcessController : Start getBuybackClosedHeaderInfo");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<BuybackHeaderDTO> buybackHeaderDTO  = buybackProcessService.getBuybackClosedHeaderInfo(decryptUtil.getSchema(), 
				decryptUtil.getAllowedApWarehouse(), warehouseNos);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(buybackHeaderDTO);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used to return list of Ausgebuchte/Geschlossene (closed) header's item detail.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getBuybackClosedHeaderItemDetails(
			@NotBlank @RequestParam(value = "headerId", required = true) String headerId,
			@NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "alphaPlusWarehouseNo", required = true) String alphaPlusWarehouseNo,
			@NotBlank @RequestParam(value = "transferDate", required = true) String transferDate) {

		log.info("BuybackProcessController : Start getBuybackClosedHeaderItemDetails");

		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<BuybackHeaderDetailsDTO> buybackHeaderDetailsDTO  = buybackProcessService.getBuybackClosedHeaderItemDetails(alphaPlusWarehouseNo, headerId,
				decryptUtil.getSchema(), decryptUtil.getDataLibrary(), transferDate);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(buybackHeaderDetailsDTO);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used to Save the BBOF Offene (Open) header's item details.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> saveBuybackOpenItemDetails( @Valid @RequestBody List<BuybackHeaderDetailsDTO> buybackHeaderDetailsDTOList,
			@NotBlank @RequestParam(value = "headerId", required = true) String headerId) {

		log.info("BuybackProcessController : Start saveBuybackOpenItemDetails");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> buyBackItems_obj = buybackProcessService.saveBuybackOpenItemDetails(buybackHeaderDetailsDTOList, headerId, decryptUtil.getSchema(), 
				decryptUtil.getLogedInUser());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(buyBackItems_obj);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used to update BBOF data from Davet2oneapi(ARBBOF).
	 */
	@Override
	public ResponseEntity<AlphaXResponse> updateBuybackDataFromARBBOF() {
		log.info("BuybackProcessController : Start updateBuybackDataFromARBBOF");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> buybackDataFromDavet2ApiDTO = buybackProcessService.updateBuybackDataFromARBBOF(decryptUtil.getSchema(), decryptUtil.getDataLibrary());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(buybackDataFromDavet2ApiDTO);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used to return the count of Dashboard(BBOFHEAD & o_BBOFITEM).
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getBuybackDashboardCount() {

		log.info("BuybackProcessController : Start getBuybackDashboardCount");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		Map<String,String> counts = buybackProcessService.getBuybackDashboardCount(decryptUtil.getDataLibrary(), decryptUtil.getSchema()); 

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(counts);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);

	}


	/**
	 * This method is used to Update the buyback head Status(BBOFHEAD).
	 */
	@Override
	public ResponseEntity<AlphaXResponse> updateBuybackHeadStatus(	
			@NotBlank  @PathVariable(value = "headerId" , required = true) String headerId) {
		log.info("BuybackProcessController : Start updateBuybackHeadStatus");
		Map<String, Boolean> updatedHeadStatus = buybackProcessService.updateBuybackHeadStatus(decryptUtil.getSchema(), headerId);

		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(updatedHeadStatus);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);		
	}


	/**
	 * This method is used to Book out the BBOF Offene (Open) header's item details.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> bookOutBuybackOpenItemDetails( 
			@Valid @RequestBody List<BuybackHeaderDetailsDTO> buybackHeaderDetailsDTOList,
			@NotBlank @PathVariable(value = "headerId", required = true) String headerId,
			@NotBlank @RequestParam(value = "orderNumber", required = true) String orderNumber,
			@NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "alphaPlusWarehouseNo", required = true) String alphaPlusWarehouseNo) {

		log.info("BuybackProcessController : Start bookOutBuybackOpenItemDetails");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> buyBackItems_obj = buybackProcessService.bookOutBuybackOpenItemDetails(buybackHeaderDetailsDTOList, headerId, orderNumber, decryptUtil.getSchema(), 
				decryptUtil.getDataLibrary(), decryptUtil.getLogedInUser(), alphaPlusWarehouseNo, decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(buyBackItems_obj);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}	


}