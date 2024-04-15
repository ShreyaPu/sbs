/**
 * 
 */
package com.alphax.api.mb;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.alphax.common.response.AlphaXResponse;
import com.alphax.util.RestURIConstants;
import com.alphax.vo.mb.BuybackHeaderDTO;
import com.alphax.vo.mb.BuybackHeaderDetailsDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

/**
 * @author A87740971
 *
 */

@Api(value = "ALPHAX",  tags = {"Buyback"} )
@SwaggerDefinition(tags = {
		@Tag(name = "Buyback", description = "REST controller that provides all the information about buyback process.")
})
public interface BuybackProcessApi {

	@ApiOperation(value = "Return a list of BBOF Offene(Open) header information", notes = "Return a list of BBOF Offene(Open) header information from DB", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get BBOF Offene header information", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get BBOF Offene header information due to server Error")
	})
	@GetMapping( value = RestURIConstants.BUYBACK_OFFENE_HEADER_INFO, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getBuybackOpenHeaderInfo(
			@ApiParam(value = "List of Warehouse numbers", required = false) @RequestParam(value = "warehouseNos", required = false) List<String> warehouseNos);
	
	
	@ApiOperation(value = "Return a list of BBOF Offene(Open) header's item detail", notes = "Return a list of BBOF Offene(Open) header's item detail from DB based on header Id", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get BBOF Offene header's item detail", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get BBOF Offene header's item detail due to server Error")
	})
	@GetMapping( value = RestURIConstants.BUYBACK_OFFENE_HEADER_ITEM_INFO, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getBuybackOpenHeaderItemDetails(
			@ApiParam(value = "Buyback header Id", required = true) @NotBlank @RequestParam(value = "headerId", required = true) String headerId,
			@ApiParam(value = "AlphaPlus Warehouse Number", required = true) @NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "alphaPlusWarehouseNo", required = true) String alphaPlusWarehouseNo);
	
	
	@ApiOperation(value = "Return a list of BBOF Ausgebuchte/Geschlossene (closed) header information", notes = "Return a list of BBOF Ausgebuchte/Geschlossene (closed) header information from DB", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get BBOF closed header information", response = BuybackHeaderDTO.class, responseContainer = "List"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get BBOF closed header information due to server Error")
	})
	@GetMapping( value = RestURIConstants.BUYBACK_CLOSED_HEADER_INFO, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getBuybackClosedHeaderInfo(
			@ApiParam(value = "List of Warehouse numbers", required = false) @RequestParam(value = "warehouseNos", required = false) List<String> warehouseNos);
	
	
	@ApiOperation(value = "Return a list of BBOF Ausgebuchte/Geschlossene (closed) header's item detail", notes = "Return a list of BBOF Ausgebuchte/Geschlossene (closed) header's item detail from DB based on header Id", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get BBOF closed header's items detail", response = BuybackHeaderDetailsDTO.class, responseContainer = "List"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get BBOF closed header's item detail due to server Error")
	})
	@GetMapping( value = RestURIConstants.BUYBACK_CLOSED_HEADER_ITEM_INFO, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getBuybackClosedHeaderItemDetails(
			@ApiParam(value = "Buyback header Id", required = true) @NotBlank @RequestParam(value = "headerId", required = true) String headerId,
			@ApiParam(value = "AlphaPlus Warehouse Number", required = true) @NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "alphaPlusWarehouseNo", required = true) String alphaPlusWarehouseNo,
			@ApiParam(value = "Transfer Date", required = true) @NotBlank @RequestParam(value = "transferDate", required = true) String transferDate);
	
	
	@ApiOperation(value = "saves a list of BBOF Offene(Open) header's item details", notes = "Saves BBOF Offene(Open) header's item details", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Save BBOF Offene header's item detail", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to save BBOF Offene header's item detail due to server Error")
	})
	@PostMapping( value = RestURIConstants.BUYBACK_OFFENE_HEADER_ITEM_INFO, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> saveBuybackOpenItemDetails(
			@ApiParam(value = "Object of Buy Back Header details", required = true) @Valid @RequestBody List<BuybackHeaderDetailsDTO> buybackHeaderDetailsDTOList,
			@ApiParam(value = "Buyback header Id", required = true) @NotBlank @RequestParam(value = "headerId", required = true) String headerId);
	
	
	@ApiOperation(value = "Update BBOF data from Davet2oneApi(ARBBOF)", notes = "Update BBOF data from Davet2oneApi(ARBBOF)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Update BBOF data from Davet2oneApi(ARBBOF)", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Update BBOF data from Davet2oneApi(ARBBOF) due to server Error")
	})
	@PutMapping( value = RestURIConstants.UPDATE_BUYBACK_FROM_DAVET2ONEAPI, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> updateBuybackDataFromARBBOF();
	
	
	@ApiOperation(value = "Return a count of BBOF dashboard( BBOFHEAD & o_BBOFITEM)", notes = "Return a count of BBOF dashboard( BBOFHEAD & o_BBOFITEM) from DB", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Return a count of BBOF dashboard( BBOFHEAD & o_BBOFITEM)", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get a count of BBOF dashboard( BBOFHEAD & o_BBOFITEM) due to server Error")
	})
	@GetMapping( value = RestURIConstants.BUYBACK_COUNT_OF_DASHBOARD, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getBuybackDashboardCount();
	
	
	@ApiOperation(value = "Update the buyback head Status in DB", notes = "Update the buyback head Status in DB", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Update the buyback head Status in DB", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Update the buyback head Status due to server Error")
	})
	@PutMapping( value = RestURIConstants.BUYBACK_UPDATE_FROM_HEAD_STATUS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> updateBuybackHeadStatus(
			@ApiParam(value = "BBOF Head Id", required = true) @NotBlank @PathVariable(value = "headerId", required = true) String headerId);
	
	
	@ApiOperation(value = "Book out (Ausbuchen) the BBOF Offene(Open) header's item details", notes = "Book out (Ausbuchen) the BBOF Offene(Open) header's item details", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Book out BBOF Offene header's item detail", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Book out BBOF Offene header's item detail due to server Error")
	})
	@PutMapping( value = RestURIConstants.BUYBACK_OFFENE_BOOKOUT, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> bookOutBuybackOpenItemDetails(
			@ApiParam(value = "Object of Buy Back Header details", required = true) @Valid @RequestBody List<BuybackHeaderDetailsDTO> buybackHeaderDetailsDTOList,
			@ApiParam(value = "Buyback header Id", required = true) @NotBlank @PathVariable(value = "headerId", required = true) String headerId,
			@ApiParam(value = "Buyback MB order Number", required = true) @NotBlank @RequestParam(value = "orderNumber", required = true) String orderNumber,
			@ApiParam(value = "AlphaPlus Warehouse Number", required = true) @NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "alphaPlusWarehouseNo", required = true) String alphaPlusWarehouseNo);
			
}
