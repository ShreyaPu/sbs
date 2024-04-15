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
import com.alphax.vo.mb.BAWrapperDTO;
import com.alphax.vo.mb.LagerDetailsDTO;
import com.alphax.vo.mb.PartDetailsDTO;
import com.alphax.vo.mb.PartPrintingLabelDTO;
import com.alphax.vo.mb.PartsTreeViewDTO;
import com.alphax.vo.mb.SearchPartsDTO;
import com.alphax.vo.mb.StorageLocationDTO;

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
@Api(value = "ALPHAX",  tags = {"Parts"} )
@SwaggerDefinition(tags = {
		@Tag(name = "Parts", description = "REST controller that provides information regarding the Vehicle Parts.")
})
public interface PartsApi {
	
	
	@ApiOperation(value = "Returns a list of parts.", notes = "Requesting a list of parts that fit to the given part number or part number fragments.", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Parts List", response = SearchPartsDTO.class, responseContainer = "List"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Parts list due to server Error")
	})
	@GetMapping( value = RestURIConstants.PARTS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getPartsList(
			@ApiParam(value = "Manufacturer (Hersteller - OEM).", required = true) @NotBlank @RequestParam(value = "oem", required = true) String oem,
			@ApiParam(value = "Warehouse number", required = false) @RequestParam(value = "warehouseId", required = false) String warehouseId,
			@ApiParam(value = "Search string to get a list of parts. This needs to be at least a fragment of the part number or of the part name.", required = true)  @RequestParam(value = "searchString", required = true) String searchString,
			@ApiParam(value = "Number of records in each page", required = false)  @RequestParam(value = "pageSize", required = false) String pageSize, 
			@ApiParam(value = "Page Number to show", required = false)  @RequestParam(value = "pageNumber", required = false) String pageNumber );
	
	
	@ApiOperation(value = "Returns a list of Original Equipment Manufacturer  (OME List)", notes = "Returns a list of Original Equipment Manufacturer  (OME List)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get OME List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get OME list due to server Error")
	})
	@GetMapping( value = RestURIConstants.PARTS_OEM, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getManufacturerList();
	
	
	@ApiOperation(value = "Returns a list of Preis Kennzeichen (Price indicator)", notes = "This list is master data, which will be return from Stub, not required DB or COBOL call", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Preis Kennzeichen List", response = PartDetailsDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Preis Kennzeichen list due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_PARTS_PREISKZ, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getPreisKzList();
	
	
	@ApiOperation(value = "Returns a list of Abschlag Kennzeichen (Discount mark)", notes = "This list is master data, which will be return from Stub, not required DB or COBOL call", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Abschlag Kennzeichen List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Abschlag Kennzeichen list due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_PARTS_ABSCHLAGKZ, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getAbschlagKzList();
	
	
	@ApiOperation(value = "Returns a list of kategorien (Categories List)", notes = "Returns a list of kategorien (Categories List)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get kategorien List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get kategorien list due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_PARTS_KATEGORIEN, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getCategoryList(
			@ApiParam(value = "The kategorie number. It's value range is between 1 and 5", required = true)  @RequestParam(value = "kategorie", required = true) String kategorie);
	
	
	@ApiOperation(value = "Returns the details information for a particular part shown in the alphaX UI.", notes = "Returns specific part details based on part number", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get part details", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get part details due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_PART_DETAILS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getNewPartDetailsByPartNumber(
			@ApiParam(value = "The company identifier that need to be used to specify the table name(s). It's value range is between 1 and 9", required = true)  @NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "companyId", required = true) String companyId,
			@ApiParam(value = "The agency (subsidiary) identifier that might need to be used to address some special table(s). It's value range is between 01 and 99", required = true) @NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "agencyId", required = true) String agencyId,
			@ApiParam(value = "Warehouse number", required = true) @NotBlank @RequestParam(value = "warehouseId", required = true) String warehouseId,
			@ApiParam(value = "Manufacturer (Hersteller - OEM)", required = true) @NotBlank @RequestParam(value = "oem", required = true) String oem,
			@ApiParam(value = "Part number", required = true) @PathVariable(value = "id", required = true) String id );

	
	@ApiOperation(value = "Returns a list of Lager details related to part", notes = "Returns a list of Lager details related to part", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Lager details based on part", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Lager details list due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_LAGERS_PART_DETAILS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getLagerDetailsBasedOnPart(
			@ApiParam(value = "Part number", required = true) @PathVariable(value = "id", required = true) String id,
			@ApiParam(value = "Warehouse number", required = true) @NotBlank @RequestParam(value = "warehouseId", required = true) String warehouseId,
			@ApiParam(value = "Manufacturer (Hersteller - OEM)", required = true) @NotBlank @RequestParam(value = "oem", required = true) String oem);
	
	
	@ApiOperation(value = "Returns a list of Movement data (Bewegungs / Ereignisdaten) information for part.", notes = "Returns a list of Movement data (Bewegungs / Ereignisdaten) information for part", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Movement data details", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Movement data details due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_MOVEMENT_DATA_DETAILS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getMovementDataUsingPartNumber(
			@ApiParam(value = "Part number", required = true) @PathVariable(value = "id", required = true) String id,
			@ApiParam(value = "Warehouse number", required = true) @NotBlank @RequestParam(value = "warehouseId", required = true) String warehouseId,
			@ApiParam(value = "Manufacturer (Hersteller - OEM)", required = true) @NotBlank @RequestParam(value = "oem", required = true) String oem,
			@ApiParam(value = "flag for movement data", required = true) @NotBlank @RequestParam(value = "flag", required = true) String flag,
			@ApiParam(value = "Number of records in each page", required = false)  @RequestParam(value = "pageSize", required = false) String pageSize, 
			@ApiParam(value = "Page Number to show", required = false)  @RequestParam(value = "pageNumber", required = false) String pageNumber);

	
	@ApiOperation(value = "Returns a list of available LOPA for provided Part number", notes = "Returns a list of available LOPA for provided Part number", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get LOPA List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get LOPA list due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_LOPA_DETAILS_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getStorageLocationList(
			@ApiParam(value = "Part number", required = true) @PathVariable(value = "id", required = true) String id,
			@ApiParam(value = "Warehouse number", required = true) @NotBlank @RequestParam(value = "warehouseId", required = true) String warehouseId,
			@ApiParam(value = "Manufacturer Name(Hersteller)", required = true) @NotBlank @RequestParam(value = "oem", required = true) String oem );
	
	
	@ApiOperation(value = "Creates / Updates the parts.", notes = "Creates / Updates the parts.", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "creates / Update Parts successful", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to update Parts due to server Error")
	})
	@PutMapping( value = RestURIConstants.PARTS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> createUpdateParts(
			@ApiParam(value = "Parts Wrapper Object", required = true) @RequestBody BAWrapperDTO baWrapperDTO);
	
	
	@ApiOperation(value = "Returns a list of available discount group and value (Rabattsatz , Rabattgruppe)", notes = "Returns a list of available discount group and value (Rabattsatz , Rabattgruppe)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get discount group and value List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get discount group and value list due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_DISCOUNT_GROUP_AND_VALUE_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getDiscountGroupAndValue(
			@ApiParam(value = "Manufacturer Name(Hersteller)", required = true) @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer,
			@ApiParam(value = "Brand (Marke)", required = true) @NotBlank @RequestParam(value = "marke", required = true) String marke);
	
	
	@ApiOperation(value = "Returns a list of Measured value (Messwert des externen Rollgeräuschs)", notes = "Returns a list of Measured value (Messwert des externen Rollgeräuschs)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Measured value (Messwert des externen Rollgeräuschs) List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Measured value (Messwert des externen Rollgeräuschs) list due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_MEASURED_VALUE_OF_THE_EXTERNAL_ROLLING_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getMeasuredValueList();
	
	
	@ApiOperation(value = "Returns a list of Tire Classification (Reifenklassifizierung)", notes = "Returns a list of Tire Classification (Reifenklassifizierung)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Tire Classification (Reifenklassifizierung) List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Tire Classification (Reifenklassifizierung) list due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_TIRE_CLASSIFICATION_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getTireClassificationList();
	
	
	@ApiOperation(value = "This API is used to get Marketing Code return values (OMCGET).", notes = "This API is used to get Marketing Code return values (OMCGET).", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "get MC return values successful", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get MC return values due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_PARTS_DECODE_MC, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> executeOMCGETCLPgm(
			@ApiParam(value = "Warehouse number", required = true) @NotBlank @RequestParam(value = "warehouseId", required = true) String warehouseId,
			@ApiParam(value = "Manufacturer name", required = true) @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer,
			@ApiParam(value = "Marketing Code", required = true) @NotBlank @RequestParam(value = "marketingCode", required = true) String marketingCode,
			@ApiParam(value = "Part number", required = false) @RequestParam(value = "partNumber", required = false) String partNumber,
			@ApiParam(value = "Discount Group (Rabattgruppe)", required = false) @RequestParam(value = "discountGroup", required = false) String discountGroup);
	
	
	@ApiOperation(value = "create / update the LOPA for provided Part number", notes = "create / update the LOPA for provided Part number", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "create/updated LOPA List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to create/update LOPA value due to server Error")
	})
	@PutMapping( value = RestURIConstants.GET_LOPA_DETAILS_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> createUpdateStorageLocation(
			@ApiParam(value = "Storage Location (LOPA) Object List", required = true) @Valid @RequestBody List<StorageLocationDTO> storageLocationDTOList,
			@ApiParam(value = "Part number", required = true) @PathVariable(value = "id", required = true) String id,
			@ApiParam(value = "Warehouse number", required = true) @NotBlank @RequestParam(value = "warehouseId", required = true) String warehouseId,
			@ApiParam(value = "Manufacturer Name(Hersteller)", required = true) @NotBlank @RequestParam(value = "oem", required = true) String oem );
	
	
	@ApiOperation(value = "Returns the Stock information for a particular part shown in the UI.", notes = "Returns Stock information based on part number", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Stock details", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Stock details due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_PART_STOCKS_DETAILS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getStockInformationOfPart(
			@ApiParam(value = "Part number", required = true) @PathVariable(value = "id", required = true) String id, 
			@ApiParam(value = "Manufacturer (Hersteller - OEM)", required = true) @NotBlank @RequestParam(value = "oem", required = true) String oem, 
			@ApiParam(value = "Number of records in each page", required = false)  @RequestParam(value = "pageSize", required = false) String pageSize, 
			@ApiParam(value = "Page Number to show", required = false)  @RequestParam(value = "pageNumber", required = false) String pageNumber,
			@ApiParam(value = "Warehouse number", required = true) @NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo);
	
	
	@ApiOperation(value = "convert part number from one format into another using SYSBRA", notes = "convert part number from one format into another using SYSBRA", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "new formatted partNo", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to convert PartNo in different format due to server Error")
	})
	@GetMapping( value = RestURIConstants.CONVERT_PARTNO_FORMATS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> convertPartNoFormats(
			@ApiParam(value = "Part number", required = true) @NotBlank @RequestParam(value = "partNo", required = true) String partNo,
			@ApiParam(value = "Part VON value", required = true) @NotBlank @RequestParam(value = "vonValue", required = true) String vonValue,
			@ApiParam(value = "Part Nach Value", required = true) @NotBlank @RequestParam(value = "nachValue", required = true) String nachValue);
	
	
	@ApiOperation(value = "Returns a list of all local alternative details for treeview ", notes = "Returns a list of all local alternative details for treeview", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get local alternative details list for treeview", response = PartsTreeViewDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get local alternative details list for treeview due to server Error")
	})
	@GetMapping( value = RestURIConstants.LOCAL_ALTERNATIVES_FOR_TREEVIEW, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getLocalAlternativesDetailsInTreeView( 
			@ApiParam(value = "Part Number", required = true)  @NotBlank @RequestParam(value = "partNo", required = true) String partNo,
			@ApiParam(value = "Manufacturer name", required = true)  @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@ApiParam(value = "Warehouse number", required = true)   @RequestParam(value = "warehouseNo", required = true) String warehouseNo);
	
	@ApiOperation(value = "get base data for BA popup", notes = "get base data for BA popup", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "get base data for BA popup", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get base data for BA popup due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_BASEDATA_FOR_BA_POPUP, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getBaseDataforBAPopup(
	      	@ApiParam(value = "Part number", required = true) @NotBlank @RequestParam(value = "partNo", required = true) String partNo,
			@ApiParam(value = "buisnessCase ", required = true) @NotBlank @RequestParam(value = "buisnessCase", required = true) String buisnessCase,
			@ApiParam(value = "Warehouse number", required = true) @NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@ApiParam(value = "Manufacturer", required = true) @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer);
	
	
	@ApiOperation(value = "Returns a list of parts.", notes = "Requesting a list of parts that fit to the given part number or part number fragments.", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Parts List", response = SearchPartsDTO.class, responseContainer = "List"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Parts list due to server Error")
	})
	@GetMapping( value = RestURIConstants.PARTS_GLOBAL_SEARCH, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> globalPartsSearch(
			@ApiParam(value = "Manufacturer (Hersteller - OEM).", required = true) @NotBlank @RequestParam(value = "oem", required = true) String oem,
			@ApiParam(value = "Warehouse number", required = false) @RequestParam(value = "warehouseId", required = false) String warehouseId,
			@ApiParam(value = "Search string to get a list of parts. This needs to be at least a fragment of the part number or of the part name.", required = true)  @RequestParam(value = "searchString", required = true) String searchString,
			@ApiParam(value = "Number of records in each page", required = false)  @RequestParam(value = "pageSize", required = false) String pageSize, 
			@ApiParam(value = "Page Number to show", required = false)  @RequestParam(value = "pageNumber", required = false) String pageNumber );
	
	
	@ApiOperation(value = "download the CSV for Labels of provided Part number.", notes = "download the CSV for Labels of provided Part number.", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "download CSV for parts Labels", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to download CSV value due to server Error")
	})
	@PutMapping( value = RestURIConstants.PARTS_LABEL_PRINTING, produces = {"application/JSON"} )
	public <T> ResponseEntity<T> getPartsLabelForCSV(
			@ApiParam(value = "Storage Location (LOPA) Object List", required = true) @Valid @RequestBody PartPrintingLabelDTO partlabelDTO,
			@ApiParam(value = "Is all Storage Locations selected", required = true) @RequestParam(value = "allStorageLocations", required = true) boolean allStorageLocations);
	
	
	@ApiOperation(value = "Returns a list of parts price.", notes = "Requesting a list of parts price that fit to the given part number or part number fragments.", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Parts price List", response = SearchPartsDTO.class, responseContainer = "List"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Parts Price list due to server Error")
	})
	@PostMapping( value = RestURIConstants.PARTS_GLOBAL_SEARCH_PRICE, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> globalSearchPartsPrice(
			@ApiParam(value = "Searched Parts Object List", required = true) @Valid @RequestBody List<SearchPartsDTO> searchedPartsList );
	
	@ApiOperation(value = "Returns required warehouse list for part creation", notes = "Returns required warehouse list for part creation", response = LagerDetailsDTO.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get required warehouse detail list for part creation", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get required warehouse detail list for part creation due to server Error")
	})
	@GetMapping( value = RestURIConstants.REQUIRED_WAREHOUSE_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getRequiredWarehouseList(
			@ApiParam(value = "Manufacturer name", required = true)  @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@ApiParam(value = "Part number", required = true) @NotBlank @RequestParam(value = "partNumber", required = true) String partNumber);

	
}