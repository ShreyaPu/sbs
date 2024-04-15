package com.alphax.controller.mb;

import java.io.ByteArrayInputStream;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alphax.common.exception.ExceptionMessages;
import com.alphax.common.response.AlphaXResponse;
import com.alphax.service.mb.InventoryReportService;
import com.alphax.api.mb.InventoryReportApi;
import com.alphax.common.constants.AlphaXCommonUtils;
import com.alphax.util.DecryptTokenUtils;

import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/api")
@Slf4j
@Validated
public class InventoryReportController implements InventoryReportApi {

	@Autowired
	AlphaXCommonUtils commonUtils;

	@Autowired
	InventoryReportService inventoryReportService;

	@Autowired
	DecryptTokenUtils decryptUtil;


	/**
	 * This method is used to get created pdf for counting list inventory.. 
	 * @return
	 */
	@Override
	public <T> ResponseEntity downloadPDFInvPartsList(
			@NotBlank @PathVariable(value = "inventoryListId", required = true) String inventoryListId,
			@RequestParam(value = "pageBreakFlag", required = true) boolean pageBreakFlag,
			@RequestParam(value = "pageBreakDDLValue", required = false) String pageBreakDDLValue ) {

		log.info("InventoryController : Start downloadPDFInvPartsList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		String reportName = "ActivatedCountingList.pdf";

		ByteArrayInputStream resource = inventoryReportService.downloadPDFInvPartsList(decryptUtil.getSchema(), inventoryListId, pageBreakFlag, pageBreakDDLValue,
				decryptUtil.getAxCompanyId());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(resource);

		if(resource != null){
			InputStreamResource file = new InputStreamResource(resource);

			// setting HTTP headers
			HttpHeaders headers = new HttpHeaders();				
			headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + reportName);

			// defining the custom Content-Type
			headers.set(HttpHeaders.CONTENT_TYPE, "application/pdf; charset=UTF-8;");

			return  new ResponseEntity<>(file, headers, HttpStatus.OK);
		}
		else{
			return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
		}	
	}


	/**
	 * This method is used to get created pdf for closed counting list inventory.
	 * @return pdf
	 */
	@Override
	public <T> ResponseEntity downloadPDFClosedInvPartsList(
			@NotBlank @PathVariable(value = "inventoryListId", required = true) String inventoryListId ) {

		log.info("InventoryController : Start downloadPDFClosedInvPartsList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		String reportName = "ClosedCountingList.pdf";

		ByteArrayInputStream resource = inventoryReportService.downloadPDFClosedInvPartsList(decryptUtil.getSchema(), inventoryListId, decryptUtil.getAxCompanyId());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(resource);

		if(resource != null){
			InputStreamResource file = new InputStreamResource(resource);

			// setting HTTP headers
			HttpHeaders headers = new HttpHeaders();				
			headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + reportName);

			// defining the custom Content-Type
			headers.set(HttpHeaders.CONTENT_TYPE, "application/pdf; charset=UTF-8;");

			return  new ResponseEntity<>(file, headers, HttpStatus.OK);
		}
		else{
			return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
		}	
	}


	/**
	 * This method is used to get created pdf for counting list inventory having Status 3 ..
	 * @return
	 */
	@Override
	public <T> ResponseEntity downloadPDF_ST3_InvPartsList(
			@NotBlank @Pattern(regexp = "[0-9]+") @PathVariable(value = "inventoryListId", required = true) String inventoryListId,
			@RequestParam(value = "pageBreakFlag", required = true) boolean pageBreakFlag,
			@RequestParam(value = "pageBreakDDLValue", required = false) String pageBreakDDLValue ) {

		log.info("InventoryController : Start downloadPDF_ST3_InvPartsList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		String reportName = "ActivatedCountingListST3.pdf";

		ByteArrayInputStream resource = inventoryReportService.downloadPDF_ST3_InvPartsList(decryptUtil.getSchema(), inventoryListId, pageBreakFlag, 
				pageBreakDDLValue, decryptUtil.getAxCompanyId());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(resource);

		if(resource != null){
			InputStreamResource file = new InputStreamResource(resource);

			// setting HTTP headers
			HttpHeaders headers = new HttpHeaders();				
			headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + reportName);

			// defining the custom Content-Type
			headers.set(HttpHeaders.CONTENT_TYPE, "application/pdf; charset=UTF-8;");

			return  new ResponseEntity<>(file, headers, HttpStatus.OK);
		}
		else{
			return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
		}	
	}


	/**
	 * This method is used to get created pdf to show differences of inventory parts intermediate results
	 * @return
	 */
	@Override
	public <T> ResponseEntity downloadPDF_IntermediateDiff(
			@NotBlank @Pattern(regexp = "[0-9]+") @PathVariable(value = "inventoryListId", required = true) String inventoryListId ) {

		log.info("InventoryController : Start downloadPDF_IntermediateDiff");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		String reportName = "IntermediateDifference.pdf";

		ByteArrayInputStream resource = inventoryReportService.downloadPDF_IntermediateDiff(decryptUtil.getSchema(), inventoryListId, decryptUtil.getAxCompanyId());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(resource);

		if(resource != null){
			InputStreamResource file = new InputStreamResource(resource);

			// setting HTTP headers
			HttpHeaders headers = new HttpHeaders();				
			headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + reportName);

			// defining the custom Content-Type
			headers.set(HttpHeaders.CONTENT_TYPE, "application/pdf; charset=UTF-8;");

			return  new ResponseEntity<>(file, headers, HttpStatus.OK);
		}
		else{
			return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
		}	
	}


	/**
	 * This method is used to get created PDF to show Countless and Rejected Parts List of Inventory.(Ungezählte und abgewiesene Positionen)
	 * @return
	 */
	@Override
	public <T> ResponseEntity downloadPDF_Countless_Rejected_List(
			@NotBlank @Pattern(regexp = "[0-9]+") @PathVariable(value = "inventoryListId", required = true) String inventoryListId ) {

		log.info("InventoryController : Start downloadPDF_Countless_Rejected_List");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		String reportName = "CountlessRejectedList.pdf";

		ByteArrayInputStream resource = inventoryReportService.downloadPDF_Countless_Rejected_List(decryptUtil.getSchema(), inventoryListId, decryptUtil.getAxCompanyId());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(resource);

		if(resource != null){
			InputStreamResource file = new InputStreamResource(resource);

			// setting HTTP headers
			HttpHeaders headers = new HttpHeaders();				
			headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + reportName);

			// defining the custom Content-Type
			headers.set(HttpHeaders.CONTENT_TYPE, "application/pdf; charset=UTF-8;");

			return  new ResponseEntity<>(file, headers, HttpStatus.OK);
		}
		else{
			return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
		}	
	}


	/**
	 * This method is used to get created PDF to show Counting List with Quantities for Inventory.(Zählliste mit Zählmengen)
	 * @return
	 */
	@Override
	public <T> ResponseEntity downloadPDF_CountingListQuantity(
			@NotBlank @Pattern(regexp = "[0-9]+") @PathVariable(value = "inventoryListId", required = true) String inventoryListId ) {

		log.info("InventoryController : Start downloadPDF_CountingListQuantity");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		String reportName = "CountingListWithQuantity.pdf";

		ByteArrayInputStream resource = inventoryReportService.downloadPDF_CountingListQuantity(decryptUtil.getSchema(), inventoryListId, decryptUtil.getAxCompanyId());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(resource);

		if(resource != null){
			InputStreamResource file = new InputStreamResource(resource);

			// setting HTTP headers
			HttpHeaders headers = new HttpHeaders();				
			headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + reportName);

			// defining the custom Content-Type
			headers.set(HttpHeaders.CONTENT_TYPE, "application/pdf; charset=UTF-8;");

			return  new ResponseEntity<>(file, headers, HttpStatus.OK);
		}
		else{
			return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
		}	
	}


	/**
	 * This method is used to get created PDF to show Positions without Difference for Inventory.(Positionen ohne Differenz)
	 * @return
	 */
	@Override
	public <T> ResponseEntity downloadPDF_Without_Differnce(
			@NotBlank @Pattern(regexp = "[0-9]+") @PathVariable(value = "inventoryListId", required = true) String inventoryListId ) {

		log.info("InventoryController : Start downloadPDF_Without_Differnce");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		String reportName = "PostionWithoutDifference.pdf";

		ByteArrayInputStream resource = inventoryReportService.downloadPDF_Without_Differnce(decryptUtil.getSchema(), inventoryListId, decryptUtil.getAxCompanyId());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(resource);

		if(resource != null){
			InputStreamResource file = new InputStreamResource(resource);

			// setting HTTP headers
			HttpHeaders headers = new HttpHeaders();				
			headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + reportName);

			// defining the custom Content-Type
			headers.set(HttpHeaders.CONTENT_TYPE, "application/pdf; charset=UTF-8;");

			return  new ResponseEntity<>(file, headers, HttpStatus.OK);
		}
		else{
			return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
		}	
	}


	/**
	 * This method is used to get created PDF to show Accepted Different Positions for Inventory.(Akzeptierte Differente Positionen)
	 * @return
	 */
	@Override
	public <T> ResponseEntity downloadPDF_Accepted_Diff_Pos(
			@NotBlank @Pattern(regexp = "[0-9]+") @PathVariable(value = "inventoryListId", required = true) String inventoryListId ) {

		log.info("InventoryController : Start downloadPDF_Accepted_Diff_Pos");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		String reportName = "AcceptedDiffPositions.pdf";

		ByteArrayInputStream resource = inventoryReportService.downloadPDF_Accepted_Diff_Pos(decryptUtil.getSchema(), inventoryListId, decryptUtil.getAxCompanyId());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(resource);

		if(resource != null){
			InputStreamResource file = new InputStreamResource(resource);

			// setting HTTP headers
			HttpHeaders headers = new HttpHeaders();				
			headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + reportName);

			// defining the custom Content-Type
			headers.set(HttpHeaders.CONTENT_TYPE, "application/pdf; charset=UTF-8;");

			return  new ResponseEntity<>(file, headers, HttpStatus.OK);
		}
		else{
			return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
		}	
	}


	/**
	 * This method is used to get created PDF to show Difference sum of accepted positions.(Differenzen-Summe von akzeptierten Positionen)
	 * @return
	 */
	@Override
	public <T> ResponseEntity downloadPDF_DifferenceSum_Pos(
			@NotBlank @Pattern(regexp = "[0-9]+") @PathVariable(value = "inventoryListId", required = true) String inventoryListId,
			@NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "warehouseNumber", required = true) String warehouseNumber ) {

		log.info("InventoryController : Start downloadPDF_DifferenceSum_Pos");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		String reportName = "DifferenceSumPosition.pdf";

		ByteArrayInputStream resource = inventoryReportService.downloadPDF_DifferenceSum_Pos(decryptUtil.getSchema(), inventoryListId, warehouseNumber,
				decryptUtil.getAxCompanyId());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(resource);

		if(resource != null){
			InputStreamResource file = new InputStreamResource(resource);

			// setting HTTP headers
			HttpHeaders headers = new HttpHeaders();				
			headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + reportName);

			// defining the custom Content-Type
			headers.set(HttpHeaders.CONTENT_TYPE, "application/pdf; charset=UTF-8;");

			return  new ResponseEntity<>(file, headers, HttpStatus.OK);
		}
		else{
			return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
		}	
	}


}