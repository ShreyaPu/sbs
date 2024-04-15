/**
 * 
 */
package com.alphax.api.mb;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

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
import com.alphax.vo.mb.AlternativePartsAvailabilityDTO;
import com.alphax.vo.mb.AlternativePartsDTO;
import com.alphax.vo.mb.OrderHistoryDTO;
import com.alphax.vo.mb.OrdersBasketDetailsDTO;
import com.alphax.vo.mb.PartRelocationDTO;
import com.alphax.vo.mb.PartRelocationDetailsDTO;
import com.alphax.vo.mb.PartsTreeViewDTO;
import com.alphax.vo.mb.SearchPartsDTO;

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
@Api(value = "ALPHAX",  tags = {"Orders"} )
@SwaggerDefinition(tags = {
		@Tag(name = "Orders", description = "REST controller that provides information regarding the Orders Bestellkorb.")
})
public interface OrdersApi {
	
	
	@ApiOperation(value = "Returns a list of Offene Bestellungen (Open Orders Basket List)", notes = "Returns a list of Offene Bestellungen (Open Orders Basket List)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Offene Bestellungen (Open Orders Basket) List", response = OrdersBasketDetailsDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Bestellkorb list due to server Error")
	})
	@GetMapping( value = RestURIConstants.ORDERS_BASKET, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getBestellKorbList(
			@ApiParam(value = "Number of records in each page", required = false)  @RequestParam(value = "pageSize", required = false) String pageSize, 
			@ApiParam(value = "Page Number to show", required = false)  @RequestParam(value = "pageNumber", required = false) String pageNumber,
			@ApiParam(value = "sorting by column name", required = true) @NotBlank @RequestParam(value = "sortingBy", required = true) String sortingBy, 
			@ApiParam(value = "sorting type that is ASC or DESC", required = true) @NotBlank @RequestParam(value = "sortingType", required = true) String sortingType,
			@ApiParam(value = "List of Warehouse numbers", required = true) @NotEmpty @RequestParam(value = "warehouseNos", required = true) List<String> warehouseNos);
	
	
	@ApiOperation(value = "Returns a list of Offene Bestellungen Details (Open Order Basket Details List)", notes = "Returns a list of Offene Bestellungen (Open Order Basket Details List)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Offene Bestellungen Details (Open Order Basket Details) List", response = OrdersBasketDetailsDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Bestellkorb Details list due to server Error")
	})
	@GetMapping( value = RestURIConstants.ORDERS_BASKET_DETAILS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getBestellKorbDetailsList(
			@ApiParam(value = "order Number", required = true) @NotBlank @PathVariable(value = "id", required = true) String orderNumber,
			@ApiParam(value = "Manufacturer name", required = true)  @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@ApiParam(value = "Warehouse number", required = true) @NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@ApiParam(value = "isAserv is need to check selected value is ASERV or PSERV. If ASERV then it is true else for PSERV it is false", required = true) @RequestParam(value = "isAserv", required = true) Boolean isAserv,
			@ApiParam(value = "supplier Number (Lieferant)", required = false) @RequestParam(value = "supplierNo", required = false) String supplierNo);
	
	
	@ApiOperation(value = "Returns a list of Teilemarke (Parts brand List)", notes = "Returns a list of Teilemarke (Parts brand List)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Teilemarke List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Teilemarke list due to server Error")
	})
	@GetMapping( value = RestURIConstants.ORDERS_TEILMARKE, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getTeileMarkeList(
			@ApiParam(value = "Original Equipment Manufacturer (OEM) value", required = true) @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer );
	

	@ApiOperation(value = "Returns a list of Auftragsart (Order type)", notes = "This list is master data, which will be return from Stub, not required DB or COBOL call", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Auftragsart List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Auftragsart list due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_ORDERS_AUFTRAGSART, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getAuftragsartList();
	
	
	@ApiOperation(value = "Returns a list of Abgebendes Lager BA  (Delivering warehouse)", notes = "This list is master data, which will be return from Stub, not required DB or COBOL call", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Abgebendes Lager BA List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Abgebendes Lager BA list due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_ORDERS_ABGEBENDES_LAGER_BA, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getAbgebendesLagerBAList();
	
	
	@ApiOperation(value = "Returns a list of Empfangendes Lager BA  (Receiving camp)", notes = "This list is master data, which will be return from Stub, not required DB or COBOL call", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Empfangendes Lager BA List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Empfangendes Lager BA list due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_ORDERS_EMPFANGENDES_LAGER_BA, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getEmpfangendesLagerBAList(
			@ApiParam(value = "Part Number", required = true)  @NotBlank @RequestParam(value = "partNo", required = true) String partNo, 
			@ApiParam(value = "Warehouse number", required = true) @NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@ApiParam(value = "Manufacturer name", required = true)  @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer);
	
	
	@ApiOperation(value = "Returns a list of parts.", notes = "Requesting a list of parts that fit to the given part number or part number fragments.", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Parts List", response = SearchPartsDTO.class, responseContainer = "List"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Parts list due to server Error")
	})
	@GetMapping( value = RestURIConstants.ORDERS_GET_PARTS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> searchPartsList(
			@ApiParam(value = "Manufacturer (Hersteller - OEM).", required = false)  @RequestParam(value = "oem", required = false) String oem,
			@ApiParam(value = "Search string to get a list of parts. This needs to be at least a fragment of the part number or of the part name.", required = true)  @RequestParam(value = "searchString", required = true) String searchString,
			@ApiParam(value = "Warehouse Number for part.", required = true)  @RequestParam(value = "warehouseNo", required = true) String warehouseNo, 
			@ApiParam(value = "Number of records in each page", required = false)  @RequestParam(value = "pageSize", required = false) String pageSize, 
			@ApiParam(value = "Page Number to show", required = false)  @RequestParam(value = "pageNumber", required = false) String pageNumber,
			@ApiParam(value = "Teileverlagerung flag for only warehouse parts", required = false)  @RequestParam(value = "flag", required = false) String flag);
	
	
	@ApiOperation(value = "Returns a list of Teileverlagerung (Part Relocation Details List)", notes = "Returns a list of Teileverlagerung (Part Relocation Details List)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Teileverlagerung Details (Part Relocation Details) List", response = PartRelocationDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Teileverlagerung Details list due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_PART_RELOCATION_DETAILS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getPartRelocationDetailsList( 
			@ApiParam(value = "Part Number", required = true)  @NotBlank @RequestParam(value = "partNo", required = true) String partNo,
			@ApiParam(value = "Warehouse number", required = true) @NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@ApiParam(value = "Manufacturer name", required = true)  @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer,
			@ApiParam(value = "Warehouse list for part Relocation", required = true) @NotBlank @RequestParam(value = "warehouseList", required = true) String warehouseList);
	
	
	@ApiOperation(value = "Check duplicate order number", notes = "Check duplicate order number", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get flag of duplicate order number", response = PartRelocationDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get duplicate order number flag due to server Error")
	})
	@GetMapping( value = RestURIConstants.CHECK_DUPLICATE_ORDER_NUMBER, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> checkDuplicateOrderNumber( 
			@ApiParam(value = "Manufacturer name", required = true)  @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@ApiParam(value = "Warehouse number", required = true) @NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@ApiParam(value = "Order Number", required = true)  @NotBlank @RequestParam(value = "orderNo", required = true) String orderNo,
			@ApiParam(value = "supplier number", required = true) @NotBlank @RequestParam(value = "supplierNo", required = true) String supplierNo);
	
	
	@ApiOperation(value = "Returns a list of all alternative Parts details list ", notes = "Returns a list of all alternative Parts details list", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get alternative Parts details list", response = AlternativePartsDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get alternative Parts details list due to server Error")
	})
	@GetMapping( value = RestURIConstants.ALTERNATIVE_PARTS_DETAILS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getAlternativePartsDetails( 
			@ApiParam(value = "Part Number", required = true)  @NotBlank @RequestParam(value = "partNo", required = true) String partNo);
	
	
	@ApiOperation(value = "Add Parts to Order Basket", notes = "Add Parts to Order Basket", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Added Parts to Order Basket", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request" ),
			@ApiResponse(code = 401, message = "Unauthorized" ),
			@ApiResponse(code = 500, message = "unable to Add Parts to Order Basket due to server Error") 
	})		
	@PostMapping(value = RestURIConstants.ORDERS_GET_PARTS)
	public ResponseEntity<AlphaXResponse> addPartsInOrderBasket(
					@ApiParam(value = "Order Basket details Object", required = true) @Valid @RequestBody OrdersBasketDetailsDTO OrderbasketDetails);
	
	
	@ApiOperation(value = "Returns an order number (Bestellnummer) using COBOL Program", notes = "Returns an order number (Bestellnummer) using COBOL Program", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Order number (Bestellnummer)", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to generate order number due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_ORDERNUMBER, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> generateOrderNumber( 
			@ApiParam(value = "manufacturer Name", required = true)  @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer,
			@ApiParam(value = "ET_manufacturer Name", required = true)  @NotBlank @RequestParam(value = "etManufacturer", required = true) String etManufacturer,
			@ApiParam(value = "Warehouse number", required = true) @NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@ApiParam(value = "Order Number", required = true)  @NotBlank @RequestParam(value = "orderNo", required = true) String orderNo,
			@ApiParam(value = "Supplier Number (Lieferant)", required = false)  @RequestParam(value = "supplierNo", required = false) String supplierNo);
	
	
	@ApiOperation(value = "Create / Send the Orders", notes = "Create / Send the Orders", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Create / Send the Order", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Create / Send the Order due to server Error")
	})
	@PostMapping( value = RestURIConstants.ORDERS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> createOrders( 
			@ApiParam(value = "Order Basket details Object List", required = true) @Valid @RequestBody List<OrdersBasketDetailsDTO> OrderbasketDetailsList,
			@ApiParam(value = "new Order Number (Bestellnummer)", required = true)  @NotBlank @RequestParam(value = "newOrderNo", required = true) String newOrderNo,
			@ApiParam(value = "Supplier Number (Lieferant)", required = true) @NotBlank @RequestParam(value = "supplierNo", required = true) String supplierNo,
			@ApiParam(value = "Order Note (Bestellhinweis)", required = true)  @RequestParam(value = "orderNote", required = true) String orderNote);
	
	
	@ApiOperation(value = "Returns a list to check alternative parts availability ", notes = "Returns a list with flag value if atleast one predecessor or successor is available ", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get alternative Parts details availability", response = AlternativePartsAvailabilityDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get alternative Parts details availability due to server Error")
	})
	@PostMapping( value = RestURIConstants.GET_ALTERNATIVE_PARTS_AVAILABILITY, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> checkAlternativePartsAvailability(
			@ApiParam(value = "Alternative parts details availability object list", required = true) @Valid @RequestBody List<AlternativePartsAvailabilityDTO> alternativePartsAvailabilityList);
	
	
	@ApiOperation(value = "Returns a list of Ausgelöste Bestellungen (Triggered Orders List)", notes = "Returns a list of Ausgelöste Bestellungen (Orders List)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Ausgelöste Bestellungen (Triggered Orders) List", response = OrdersBasketDetailsDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Ausgelöste Bestellungen list due to server Error")
	})
	@GetMapping( value = RestURIConstants.ORDERS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getOrdersList(
			@ApiParam(value = "Number of records in each page", required = false)  @RequestParam(value = "pageSize", required = false) String pageSize, 
			@ApiParam(value = "Page Number to show", required = false)  @RequestParam(value = "pageNumber", required = false) String pageNumber,
			@ApiParam(value = "sorting by column name", required = true) @NotBlank @RequestParam(value = "sortingBy", required = true) String sortingBy, 
			@ApiParam(value = "sorting type that is ASC or DESC", required = true) @NotBlank @RequestParam(value = "sortingType", required = true) String sortingType,
			@ApiParam(value = "List of Warehouse numbers", required = true) @NotEmpty @RequestParam(value = "warehouseNos", required = true) List<String> warehouseNos );
	
	
	@ApiOperation(value = "Returns a list of Ausgelöste Bestellungen Details (Order Details List)", notes = "Returns a list of Ausgelöste Bestellungen Details (Order Details List)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Ausgelöste Bestellungen Details (Order Details) List", response = OrdersBasketDetailsDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Order Details list due to server Error")
	})
	@GetMapping( value = RestURIConstants.ORDERS_DETAILS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getOrderDetailsList(
			@ApiParam(value = "Bestellnummer", required = true) @NotBlank @PathVariable(value = "id", required = true) String newOrderNumber,
			@ApiParam(value = "Manufacturer name", required = true)  @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@ApiParam(value = "Warehouse number", required = true) @NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@ApiParam(value = "isAserv is need to check selected value is ASERV or PSERV. If ASERV then it is true else for PSERV it is false", required = true) @RequestParam(value = "isAserv", required = true) Boolean isAserv,
			@ApiParam(value = "supplier Number (Lieferant)", required = false) @RequestParam(value = "supplierNo", required = false) String supplierNo,
			@ApiParam(value = "order Number", required = true) @NotBlank @RequestParam(value = "orderNumber", required = true) String orderNumber);
	
	
	@ApiOperation(value = "Returns a list of Ausgelöste Bestellungen (Triggered Orders List based on filter)", notes = "Returns a list of Ausgelöste Bestellungen (Orders List based on filter)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Ausgelöste Bestellungen (Triggered Orders) List based on filter", response = OrdersBasketDetailsDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Ausgelöste Bestellungen list based on filter due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_ORDERS_LIST_BASED_ON_FILTER, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getOrdersListBasedOnFilter(
			@ApiParam(value = "searchText that need to use for filter value", required = true)  @RequestParam(value = "searchText", required = true) String searchText, 
			@ApiParam(value = "searchBy that need to use for filter criteria", required = true)  @RequestParam(value = "searchBy", required = true) String searchBy, 
			@ApiParam(value = "Number of records in each page", required = false)  @RequestParam(value = "pageSize", required = false) String pageSize, 
			@ApiParam(value = "Page Number to show", required = false)  @RequestParam(value = "pageNumber", required = false) String pageNumber,
			@ApiParam(value = "sorting by column name", required = true) @NotBlank @RequestParam(value = "sortingBy", required = true) String sortingBy, 
			@ApiParam(value = "sorting type that is ASC or DESC", required = true) @NotBlank @RequestParam(value = "sortingType", required = true) String sortingType,
			@ApiParam(value = "List of Warehouse numbers", required = true) @NotEmpty @RequestParam(value = "warehouseNos", required = true) List<String> warehouseNos);
	
	@ApiOperation(value = "Returns a list of filter values for Ausgelöste Bestellungen (Triggered Orders) ", notes = "This list is master data, which will be return from Stub, not required DB or COBOL call", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get filter values for order", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get filter values for order due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_FILTER_VALUE_ORDER, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getFilterValuesForOrder();
	
	
	@ApiOperation(value = "transfer the Orders to ET warehouse", notes = "transfer the Orders to ET warehouse", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "transfer the Orders to ET", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to transfer the Orders to ET due to server Error")
	})
	@PostMapping( value = RestURIConstants.TRANSFER_ORDERS_TO_ET, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> transferOrderstoET( 
			@ApiParam(value = "Order Basket details Object", required = true) @Valid @RequestBody OrdersBasketDetailsDTO orderbasketDetails);

	
	@ApiOperation(value = "Returns a list of Offene Bestellungen (Open Orders List based on filter)", notes = "Returns a list of Offene Bestellungen (Open Orders List based on filter)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Offene Bestellungen list (Open Orders List based on filter)", response = OrdersBasketDetailsDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Offene Bestellungen list based on filter due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_ORDERS_BASKET_BASED_ON_FILTER, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getBestellKorbListBasedOnFilter(
			@ApiParam(value = "searchText that need to use for filter value", required = true)  @RequestParam(value = "searchText", required = true) String searchText, 
			@ApiParam(value = "searchBy that need to use for filter criteria", required = true)  @RequestParam(value = "searchBy", required = true) String searchBy, 
			@ApiParam(value = "Number of records in each page", required = false)  @RequestParam(value = "pageSize", required = false) String pageSize, 
			@ApiParam(value = "Page Number to show", required = false)  @RequestParam(value = "pageNumber", required = false) String pageNumber,
			@ApiParam(value = "sorting by column name", required = true) @NotBlank @RequestParam(value = "sortingBy", required = true) String sortingBy, 
			@ApiParam(value = "sorting type that is ASC or DESC", required = true) @NotBlank @RequestParam(value = "sortingType", required = true) String sortingType,
			@ApiParam(value = "List of Warehouse numbers", required = true) @NotEmpty @RequestParam(value = "warehouseNos", required = true) List<String> warehouseNos);
	
	
	@ApiOperation(value = "Update part number for part replacement (Teilersetzung)", notes = "Update part number for part replacement (Teilersetzung)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Update part number for part replacement", response = OrdersBasketDetailsDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Update part number due to server Error")
	})
	@PutMapping( value = RestURIConstants.ALTERNATIVE_PARTS_DETAILS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> replacePartNumber(
			@ApiParam(value = "Order Basket details Object", required = true) @Valid @RequestBody OrdersBasketDetailsDTO OrderbasketDetails,
			@ApiParam(value = "Part Number", required = true)  @NotBlank @RequestParam(value = "partNumber", required = true) String partNumber, 
			@ApiParam(value = "Part Name", required = true) @NotBlank @RequestParam(value = "partName", required = true) String partName);
	
	
	@ApiOperation(value = "Create order history", notes = "Create order history", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Create order history", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to create order history due to server Error")
	})
	@PostMapping( value = RestURIConstants.CREATE_ORDERS_HISTORY, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> createOrderHistory( 
			@ApiParam(value = "Order Basket Object List", required = true) @Valid @RequestBody List<OrdersBasketDetailsDTO> OrderbasketDetailsList,
			@ApiParam(value = "new Order Number (Bestellnummer)", required = true)  @NotBlank @RequestParam(value = "newOrderNo", required = true) String newOrderNo,
			@ApiParam(value = "Order Note (Bestellhinweis)", required = true)  @RequestParam(value = "orderNote", required = true) String orderNote);
	
	
	@ApiOperation(value = "Delete selected Open Orders detail from list", notes = "Delete selected Open Orders detail from list", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Delete selected Orders from list", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request" ),
			@ApiResponse(code = 401, message = "Unauthorized" ),
			@ApiResponse(code = 500, message = "unable to delete Orders list due to server Error")
	})		
	@DeleteMapping(value = RestURIConstants.ORDERS_BASKET)
	public ResponseEntity<AlphaXResponse> deleteOpenOrders(
					@ApiParam(value = "Order Basket details Object List", required = true) @Valid @RequestBody List<OrdersBasketDetailsDTO> OrderbasketDetailsList);
	
	
	@ApiOperation(value = "Reset Executed open order from list", notes = "Reset Executed open order from list", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Reset Executed open order from list", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request" ),
			@ApiResponse(code = 401, message = "Unauthorized" ),
			@ApiResponse(code = 500, message = "unable to Reset Executed open order from list")
	})
			
	@DeleteMapping(value = RestURIConstants.RESET_EXECUTE_ORDER)
	public ResponseEntity<AlphaXResponse> resetExecuteOrder(
					@ApiParam(value = "Order Basket details Object List", required = true) @Valid @RequestBody OrdersBasketDetailsDTO OrderbasketDetailsList);
	
			
	
	@ApiOperation(value = "Returns a list of Bestellhistorie (order history list)", notes = "Returns a list of Bestellhistorie (order history list)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Bestellhistorie (order history list) List", response = OrderHistoryDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Bestellhistorie (order history list) due to server Error")
	})
	@GetMapping( value = RestURIConstants.CREATE_ORDERS_HISTORY, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getOrderHistory(
			@ApiParam(value = "Number of records in each page", required = false)  @RequestParam(value = "pageSize", required = false) String pageSize, 
			@ApiParam(value = "Page Number to show", required = false)  @RequestParam(value = "pageNumber", required = false) String pageNumber );
	
	
	@ApiOperation(value = "Returns a list of Bestellhistorie (order history list based on filter))", notes = "Returns a list of Bestellhistorie (order history list based on filter))", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Bestellhistorie List based on filter", response = OrderHistoryDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Bestellhistorie List based on filter due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_ORDERS_HISTORY_BASED_ON_FILTER, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getOrderHistoryBasedOnFilter(
			@ApiParam(value = "searchText that need to use for filter value", required = true)  @RequestParam(value = "searchText", required = true) String searchText, 
			@ApiParam(value = "searchBy that need to use for filter criteria", required = true)  @RequestParam(value = "searchBy", required = true) String searchBy, 
			@ApiParam(value = "Number of records in each page", required = false)  @RequestParam(value = "pageSize", required = false) String pageSize, 
			@ApiParam(value = "Page Number to show", required = false)  @RequestParam(value = "pageNumber", required = false) String pageNumber );
	

	@ApiOperation(value = "Returns a list of all alternative Parts details list for part search ", notes = "Returns a list of all alternative Parts details list for part search", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get alternative Parts details list for part search", response = AlternativePartsDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get alternative Parts details list due to server Error")
	})
	@GetMapping( value = RestURIConstants.ALTERNATIVE_PARTS_FOR_PART_SEARCH, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getAlternativePartsForPartSearch( 
			@ApiParam(value = "Part Number", required = true)  @NotBlank @RequestParam(value = "partNo", required = true) String partNo,
			@ApiParam(value = "Manufacturer name", required = true)  @NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer, 
			@ApiParam(value = "Warehouse number", required = true) @NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo);
	
	
	@ApiOperation(value = "Update the Verlagern (Part Relocation Details)", notes = "Update the Verlagern (Part Relocation Details)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Update the Verlagern (Part Relocation Details)", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Update the Verlagern Details due to server Error")
	})
	@PutMapping( value = RestURIConstants.GET_PART_RELOCATION_DETAILS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> updatePartRelocationDetails( 
			@ApiParam(value = "Part Relocation details Object", required = true) @Valid @RequestBody PartRelocationDetailsDTO partRelocationDetails,
			@ApiParam(value = "relocation Type", required = true) @NotBlank @RequestParam(value = "relocationType", required = true) String relocationType);
	

	@ApiOperation(value = "Check ESKDAT Orders", notes = "Check ESKDAT Orders by COBOL", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Check ESKDAT Orders", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Check ESKDAT Orders due to server Error")
	})
	@GetMapping( value = RestURIConstants.CHECK_ESKDAT_ORDERS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> checkESKDATOrders();
	
	
	@ApiOperation(value = "Return warehouse list", notes = "Return warehouse list by COBOL", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get warehouse list", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Get warehouse list due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_WAREHOUSE_LIST_FOR_PART_MOVEMENT, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getWarehouseListForPartMovement( 
			@ApiParam(value = "Warehouse Number", required = true) @NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo);
	
	
	@ApiOperation(value = "Returns a list of parts for Teileverlagerung.", notes = "Requesting a list of parts that fit to the given part number or part number fragments.", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Parts List", response = SearchPartsDTO.class, responseContainer = "List"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Parts list due to server Error")
	})
	@GetMapping( value = RestURIConstants.ORDERS_GET_PARTS_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> searchPartsListForVerlegun(
			@ApiParam(value = "Manufacturer (Hersteller - OEM).", required = false)  @RequestParam(value = "oem", required = false) String oem,
			@ApiParam(value = "Search string to get a list of parts. This needs to be at least a fragment of the part number or of the part name.", required = true)  @RequestParam(value = "searchString", required = true) String searchString,
			@ApiParam(value = "Number of records in each page", required = false)  @RequestParam(value = "pageSize", required = false) String pageSize, 
			@ApiParam(value = "Page Number to show", required = false)  @RequestParam(value = "pageNumber", required = false) String pageNumber,
			@ApiParam(value = "Teileverlagerung flag for only warehouse parts", required = false)  @RequestParam(value = "flag", required = false) String flag,
			@ApiParam(value = "Warehouse number", required = false) @RequestParam(value = "warehouseNo", required = false) String warehouseNo);
	
	
	@ApiOperation(value = "Recreate .e36 file for the rows where file .e36 creation has failed in section ausgelöste Bestellungen", notes = "Recreate the .e36 file for the rows where file .e36-creation has failed in section ausgelöste Bestellungen", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Recreate the .e36 file", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to recreate the .e36 file due to server Error")
	})
	@PutMapping( value = RestURIConstants.ORDERS_RECREATE_E36, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> recreateE36File( 
			@ApiParam(value = "Order details Object", required = true) @Valid @RequestBody OrdersBasketDetailsDTO orderBasketDto);
	
	
	@ApiOperation(value = "Delete selected delivered (Ausgelöst) order from list", notes = "Delete selected delivered (Ausgelöst) order from list", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Delete selected delivered (Ausgelöst) order from list", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request" ),
			@ApiResponse(code = 401, message = "Unauthorized" ),
			@ApiResponse(code = 500, message = "unable to delete delivered (Ausgelöst) order due to server Error")
	})		
	@DeleteMapping(value = RestURIConstants.ORDERS)
	public ResponseEntity<AlphaXResponse> deleteDeliveredOrder(
					@ApiParam(value = "Triggered Orders List", required = true) @Valid @RequestBody List<OrdersBasketDetailsDTO> deliveredOrdersList);
		
	
	@ApiOperation(value = "Returns a list of orders with part movement possible or not for orders", notes = "Returns a list of orders with part movement possible or not for orders)", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "order part movement possibility List", response = OrdersBasketDetailsDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get orders Details list for part movement due to server Error")
	})
	@PostMapping( value = RestURIConstants.CHECK_PART_MOVEMENT, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> checkPartMovementPossible(
			@ApiParam(value = "order basket List", required = true) @Valid @RequestBody List<OrdersBasketDetailsDTO> orderBasketList);
	
	@ApiOperation(value = "Returns a list of all alternative Parts details for treeview ", notes = "Returns a list of all alternative Parts details for treeview", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get alternative Parts details list for treeview", response = PartsTreeViewDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get alternative Parts details list for treeview due to server Error")
	})
	@GetMapping( value = RestURIConstants.PARTS_DETAILS_FOR_TREEVIEW, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getPartsDetailsInTreeView( 
			@ApiParam(value = "Part Number", required = true)  @NotBlank @RequestParam(value = "partNo", required = true) String partNo,
			@ApiParam(value = "Manufacturer name", required = false)  @RequestParam(value = "manufacturer", required = false) String manufacturer, 
			@ApiParam(value = "Warehouse number", required = false)   @RequestParam(value = "warehouseNo", required = false) String warehouseNo);
}
