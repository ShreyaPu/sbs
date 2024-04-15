package com.alphax.api.mb;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.alphax.common.response.AlphaXResponse;
import com.alphax.util.RestURIConstants;
import com.alphax.vo.mb.AdminPrinterDTO;
import com.alphax.vo.mb.AlphaXConfigurationSetupDTO;

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
@Api(value = "ALPHAX",  tags = {"AdminPrinterSetting"} )
@SwaggerDefinition(tags = {
		@Tag(name = "AdminPrinterSetting", description = "REST controller that provides all the information about Admin Printer settings.")
})
public interface AdminPrinterSettingApi {
	

	@ApiOperation(value = "Create New Printer details", notes = "Create New Printer details", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Create new printer for admin setting", response = AdminPrinterDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Create New printer for admin setting due to server Error")
	})
	@PostMapping( value = RestURIConstants.ADMIN_PRINTER, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> createNewPrinter(
			@ApiParam(value = "Object of new Printer details for admin setting", required = true) @Valid @RequestBody AdminPrinterDTO adminPrinter_obj);
	
	
	@ApiOperation(value = "Returns a list of printer", notes = "Return the list of admin printer", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get printer List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get printer list due to server Error")
	})
	@GetMapping( value = RestURIConstants.ADMIN_PRINTER, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getPrinterList();
	
	
	@ApiOperation(value = "delete a details of printer", notes = "delete a details of printer based on printerId for admin setting", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Delete printer details for admin setting", response = AdminPrinterDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to delete printer details for admin setting due to server Error")
	})
	@DeleteMapping( value = RestURIConstants.ADMIN_PRINTER_DETAILS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> deletePrinterDetails(
			@ApiParam(value = "Printer Id", required = true) @PathVariable(value = "id", required = true) String printerId );
	
	
	@ApiOperation(value = "Create printer and Agency Mapping", notes = "Create printer and Agency Mapping for admin setting", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Create printer and Agency Mapping for admin setting", response = AdminPrinterDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Create printer and Agency Mapping for admin setting due to server Error")
	})
	@PostMapping( value = RestURIConstants.PRINTER_AND_AGENCY_MAPPING, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> updateAgencyPrinterMapping(
			@ApiParam(value = "Object of User, Role and Agency Mapping for admin setting", required = true) @Valid @RequestBody List<AdminPrinterDTO> admin_PrinterMappingDTO,
			@ApiParam(value = "Agency Id", required = true) @NotBlank @RequestParam(value = "agencyId", required = true) String agencyId);
	
	
	@ApiOperation(value = "Returns a list of Printers that are not assigned to current Agency", notes = "Returns a list of Printers that are not assigned to current Agency for admin setting", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Printers list that are not assigned to current Agency for admin setting", response = AdminPrinterDTO.class, responseContainer = "List"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Printers list that are not assigned to current Agency for admin setting due to server Error")
	})
	@GetMapping( value = RestURIConstants.PRINTER_NOT_ASSIGNED_TO_CUR_AGENCY, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getPrinters_NotAssignedToCrntAgency(
			@ApiParam(value = "Agency Id", required = true) @NotBlank @Pattern(regexp = "[0-9]+") @PathVariable(value = "id", required = true) String agencyId);
	
	
	@ApiOperation(value = "Returns a list of Printers that are assigned to current Agency", notes = "Returns a list of Printers that are assigned to current Agency based on datalibrary for admin setting", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Printers list that are assigned to current Agency for admin setting", response = AdminPrinterDTO.class, responseContainer = "List"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Printers list that are assigned to current Agency for admin setting due to server Error")
	})
	@GetMapping( value = RestURIConstants.PRINTER_ASSIGNED_TO_CUR_AGENCY, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getPrinters_AssignedToCrntAgency(
			@ApiParam(value = "Agency Id", required = true) @NotBlank @Pattern(regexp = "[0-9]+") @PathVariable(value = "id", required = true) String agencyId);
	
	@ApiOperation(value = "Returns a details of selected printer", notes = "Returns a details of selected printer", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get printer Details", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get details of selected printer due to server Error")
	})
	@GetMapping( value = RestURIConstants.ADMIN_PRINTER_DETAILS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getPrinterDetails(
			@ApiParam(value = "Printer Id", required = true) @PathVariable(value = "id", required = true) String printerId );
			

	@ApiOperation(value = "Update the Admin Printer details", notes = "Update the Admin Printer details ", response = AlphaXResponse.class)
	@ApiResponses(value = { 
		@ApiResponse(code = 200, message = "Get Updated printer Details", response = AlphaXConfigurationSetupDTO.class),
		@ApiResponse(code = 400, message = "Bad request"),
		@ApiResponse(code = 401, message = "Unauthorized"),
		@ApiResponse(code = 500, message = "unable to Update Admin Printer Object Details   due to server Error")
})
	@PutMapping( value = RestURIConstants.ADMIN_PRINTER, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse>  updateAdminPrinterDetails(
		@ApiParam(value = "Admin Printer Details Object", required = true) @Valid @RequestBody AdminPrinterDTO updatedAdminPrinterDetailsList);

	@ApiOperation(value = "Returns a list of printer Type List", notes = "Return the list of admin printer Type", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get printer Type List ", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get printer list type  due to server Error")
	})
	@GetMapping( value = RestURIConstants.ADMIN_PRINTER_TYPE, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getPrinterTypeList();
	
	
}