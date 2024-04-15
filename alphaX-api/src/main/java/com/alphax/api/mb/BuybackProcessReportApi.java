/**
 * 
 */
package com.alphax.api.mb;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.alphax.common.response.AlphaXResponse;
import com.alphax.util.RestURIConstants;
import com.alphax.vo.mb.BuybackHeaderDetailsDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

/**
 * @author A106744104
 *
 */

@Api(value = "ALPHAX",  tags = {"BuybackReport"} )
@SwaggerDefinition(tags = {
		@Tag(name = "BuybackReport", description = "REST controller that provides all the information about buyback process Reports.")
})
public interface BuybackProcessReportApi {
	
	
	@ApiOperation(value = "Download PDF file for Opened BuyBack receipt", notes = "Download PDF file for Opened BuyBack receipt", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Downloaded PDF file", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Download PDF file due to server Error")
	})
	@PutMapping( value = RestURIConstants.DOWNLOAD_PDF_OPEN_BBOF, produces = MediaType.APPLICATION_PDF_VALUE )
	public <T> ResponseEntity<T> downloadPDFOpenBBOFReceipt(
			@ApiParam(value = "Buyback header Id", required = true) @NotBlank @RequestParam(value = "headerId", required = true) String headerId,
			@ApiParam(value = "AlphaPlus Warehouse Number", required = true) @NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "alphaPlusWarehouseNo", required = true) String alphaPlusWarehouseNo,
			@ApiParam(value = "Object of Buy Back Header details", required = false) @Valid @RequestBody List<BuybackHeaderDetailsDTO> buybackHeaderDetailsDTOList);
	

	@ApiOperation(value = "Download PDF file for Closed BuyBack Summary", notes = "Download PDF file for Closed BuyBack Summary", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Downloaded PDF file", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Download PDF file due to server Error")
	})
	@GetMapping( value = RestURIConstants.DOWNLOAD_PDF_CLOSED_BBOF, produces = MediaType.APPLICATION_PDF_VALUE )
	public <T> ResponseEntity<T> downloadPDFClosedBBOF(
			@ApiParam(value = "Buyback header Id", required = true) @NotBlank @RequestParam(value = "headerId", required = true) String headerId,
			@ApiParam(value = "AlphaPlus Warehouse Number", required = true) @NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "alphaPlusWarehouseNo", required = true) String alphaPlusWarehouseNo,
			@ApiParam(value = "Transfer Date", required = true) @NotBlank @RequestParam(value = "transferDate", required = true) String transferDate);
			
}
