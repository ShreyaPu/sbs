package com.alphax.controller.mb;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alphax.common.exception.ExceptionMessages;
import com.alphax.common.response.AlphaXResponse;
import com.alphax.service.mb.InventoryEvalutionService;
import com.alphax.api.mb.InventoryEvalutionApi;
import com.alphax.common.constants.AlphaXCommonUtils;
import com.alphax.util.DecryptTokenUtils;
import com.alphax.vo.mb.GlobalSearch;
import com.alphax.vo.mb.InventoryArchivedCountingDTO;
import com.alphax.vo.mb.InventoryArchivedDifferencesDTO;
import com.alphax.vo.mb.InventoryEvalutionDTO;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
@Validated
public class InventoryEvalutionController implements InventoryEvalutionApi {

	@Autowired
	AlphaXCommonUtils commonUtils;
	
	@Autowired
	InventoryEvalutionService inventoryEvalutionService;
	
	@Autowired
	DecryptTokenUtils decryptUtil;
	
	
	/**
	 * This method is used to get the Inventory Conspicuous Items (Bestandsauffällige Positionen) from Savlib.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getInventoryRemarkableItems(
			@NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@NotBlank @RequestParam(value = "fromDate", required = true) String fromDate,
			@NotBlank @RequestParam(value = "toDate", required = true) String toDate,
			@RequestParam(value = "pageSize", required = false) String pageSize, 
			@RequestParam(value = "pageNumber", required = false) String pageNumber){
		
		log.info("InventoryEvalutionController : Start getInventoryRemarkableItems");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		GlobalSearch inventoryItemsList = inventoryEvalutionService.getInventoryRemarkableItems(decryptUtil.getDataLibrary(), decryptUtil.getSavLibrary(), warehouseNo, 
				fromDate, toDate, pageSize, pageNumber);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(inventoryItemsList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used to get the Inventory Conspicuous Items Details (Bestandsauffällige Positionen Details) Using Part Number.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getInventoryRemarkableItemsDetails(
			@NotBlank @RequestParam(value = "partNo", required = true) String partNo,
			@NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo){
		
		log.info("InventoryEvalutionController : Start getInventoryRemarkableItemsDetails");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<InventoryEvalutionDTO> inventoryItemsDetailList = inventoryEvalutionService.getInventoryRemarkableItemsDetails(decryptUtil.getDataLibrary(), 
				decryptUtil.getSavLibrary(), partNo, warehouseNo);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(inventoryItemsDetailList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used to get the Inventory Print - Archived lists of differences (Archivierte Differenzenlisten) from Savlib
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getInventoryArchivedDifferencesList(
			@NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@NotBlank @RequestParam(value = "fromDate", required = true) String fromDate,
			@NotBlank @RequestParam(value = "toDate", required = true) String toDate){
		
		log.info("InventoryEvalutionController : Start getInventoryArchivedDifferencesList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<InventoryArchivedDifferencesDTO> inventoryArchivedList = inventoryEvalutionService.getInventoryArchivedDifferencesList(decryptUtil.getDataLibrary(), 
				decryptUtil.getSavLibrary(), warehouseNo, fromDate, toDate);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(inventoryArchivedList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used to get the Inventory Print - Archived lists of differences in Details (Archivierte Differenzenlisten) from Savlib
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getInventoryArchivedDifferencesInDetails(
			@NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@NotBlank @RequestParam(value = "date", required = true) String date,
			@NotBlank @RequestParam(value = "listId", required = true) String listId,
			@NotBlank @RequestParam(value = "detailsType", required = true) String detailsType){
		
		log.info("InventoryEvalutionController : Start getInventoryArchivedDifferencesInDetails");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<InventoryArchivedDifferencesDTO> inventoryArchivedList = inventoryEvalutionService.getInventoryArchivedDifferencesInDetails(decryptUtil.getDataLibrary(), 
				decryptUtil.getSavLibrary(), warehouseNo, date, listId, detailsType);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(inventoryArchivedList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used to get the Inventory Archived Counting List (Archivierte Zähllisten) from Savlib.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getArchivedCountingList(
			@NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@NotBlank @RequestParam(value = "fromDate", required = true) String fromDate,
			@NotBlank @RequestParam(value = "toDate", required = true) String toDate){
		
		log.info("InventoryEvalutionController : Start getArchivedCountingList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<InventoryArchivedCountingDTO> inventoryItemsList = inventoryEvalutionService.getArchivedCountingList(decryptUtil.getDataLibrary(), 
				decryptUtil.getSavLibrary(), warehouseNo, fromDate, toDate);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(inventoryItemsList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used to get the Inventory Archived Counting Detail List (Archivierte Zähllisten) from Savlib.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getArchivedCountingDetailList(
			@NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@NotBlank @RequestParam(value = "inventoryDate", required = true) String inventoryDate,
			@NotBlank @RequestParam(value = "inventoryTime", required = true) String inventoryTime,
			@NotBlank @RequestParam(value = "detailsType", required = true) String detailsType){
		
		log.info("InventoryEvalutionController : Start getArchivedCountingDetailList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<InventoryArchivedCountingDTO> inventoryItemsList = inventoryEvalutionService.getArchivedCountingDetailList(decryptUtil.getDataLibrary(), 
				decryptUtil.getSavLibrary(), warehouseNo, inventoryDate, inventoryTime, detailsType);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(inventoryItemsList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
}
