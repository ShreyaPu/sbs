/**
 * 
 */
package com.alphax.controller.mb;

import java.io.ByteArrayInputStream;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alphax.common.exception.ExceptionMessages;
import com.alphax.common.response.AlphaXResponse;
import com.alphax.service.mb.BuybackProcessReportService;
import com.alphax.api.mb.BuybackProcessReportApi;
import com.alphax.common.constants.AlphaXCommonUtils;
import com.alphax.util.DecryptTokenUtils;
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
public class BuybackProcessReportController implements BuybackProcessReportApi {

	@Autowired
	AlphaXCommonUtils commonUtils;
	
	@Autowired
	BuybackProcessReportService buybackReportService;
	
	@Autowired
	DecryptTokenUtils decryptUtil;
	
	
	/**
	 * This method is used to create the PDF for Opened Buy Back Receipt.
	 * @return
	 */
	@Override
	public <T> ResponseEntity downloadPDFOpenBBOFReceipt(
			@NotBlank @RequestParam(value = "headerId", required = true) String headerId,
			@NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "alphaPlusWarehouseNo", required = true) String alphaPlusWarehouseNo,
			@Valid @RequestBody List<BuybackHeaderDetailsDTO> buybackHeaderDetailsDTOList) {

		log.info("BuybackProcessReportController : Start downloadPDFOpenBBOFReceipt");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		String reportName = "OpenBBOF.pdf";

		ByteArrayInputStream resource = buybackReportService.downloadPDFOpenBBOFReceipt(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), headerId, 
				alphaPlusWarehouseNo, buybackHeaderDetailsDTOList, decryptUtil.getLogedInUser());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(resource);

		if(resource != null) {
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
	 * This method is used to create the PDF for closed Buy Back Summary.
	 * @return
	 */
	@Override
	public <T> ResponseEntity downloadPDFClosedBBOF(
			@NotBlank @RequestParam(value = "headerId", required = true) String headerId,
			@NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "alphaPlusWarehouseNo", required = true) String alphaPlusWarehouseNo,
			@NotBlank @RequestParam(value = "transferDate", required = true) String transferDate) {

		log.info("BuybackProcessReportController : Start downloadPDFClosedBBOF");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		String reportName = "ClosedBBOF.pdf";

		ByteArrayInputStream resource = buybackReportService.downloadPDFClosedBBOF(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), headerId, 
				alphaPlusWarehouseNo, transferDate);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(resource);

		if(resource != null) {
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
