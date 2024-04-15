package com.alphax.controller.mb;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

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
import com.alphax.service.mb.OrdersService;
import com.alphax.api.mb.OrdersApi;
import com.alphax.common.constants.AlphaXCommonUtils;
import com.alphax.util.DecryptTokenUtils;
import com.alphax.util.RestURIConstants;
import com.alphax.vo.mb.AlternativePartsAvailabilityDTO;
import com.alphax.vo.mb.AlternativePartsDTO;
import com.alphax.vo.mb.DropdownObject;
import com.alphax.vo.mb.GlobalSearch;
import com.alphax.vo.mb.OrdersBasketDetailsDTO;
import com.alphax.vo.mb.PartRelocationDTO;
import com.alphax.vo.mb.PartRelocationDetailsDTO;
import com.alphax.vo.mb.PartsTreeViewDTO;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
@Validated
public class OrdersController implements OrdersApi {

	@Autowired
	OrdersService orderService;

	@Autowired
	AlphaXCommonUtils commonUtils;

	@Autowired
	DecryptTokenUtils decryptUtil;


	/**
	 * This is OrdersController method used to get the Offene Bestellungen (Open Order Basket) List.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getBestellKorbList( 
			@RequestParam(value = "pageSize", required = false) String pageSize,
			@RequestParam(value = "pageNumber", required = false) String pageNumber,
			@NotBlank @RequestParam(value = "sortingBy", required = true) String sortingBy, 
			@NotBlank @RequestParam(value = "sortingType", required = true) String sortingType,
			@NotEmpty @RequestParam(value = "warehouseNos", required = true) List<String> warehouseNos) {

		log.info("OrdersController : Start getBestellKorbList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}

		GlobalSearch orderBasketList = orderService.getBestellKorbList(decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), 
				allowedApWarehouses, pageSize, pageNumber, sortingBy, sortingType, warehouseNos);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(orderBasketList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This is OrdersController method used to get the Offene Bestellungen Details List (Open Order Basket Details) List.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getBestellKorbDetailsList( 
			@NotBlank @PathVariable(value = "id", required = true) String orderNumber,
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer,
			@NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@RequestParam(value = "isAserv", required = true) Boolean isAserv,
			@RequestParam(value = "supplierNo", required = false) String supplierNo) {

		log.info("OrdersController : Start getBestellKorbDetailsList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<OrdersBasketDetailsDTO> orderBasketDetailList = orderService.getBestellKorbDetailsList(orderNumber, decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), 
				decryptUtil.getApAgencyId(), manufacturer, warehouseNo, isAserv, supplierNo, decryptUtil.getCompanyApWarehouse());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(orderBasketDetailList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This is OrdersController method used to get the TeileMarke (Brand) detail List for DDL
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getTeileMarkeList(
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer) {

		log.info("OrdersController : Start getTeilMarkeList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<DropdownObject> teileMarkeList = orderService.getTeileMarkeList(decryptUtil.getDataLibrary(), decryptUtil.getSchema(), manufacturer);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(teileMarkeList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used for get Auftragsart (Order type) List
	 */	
	@Override
	public ResponseEntity<AlphaXResponse> getAuftragsartList() {

		log.info("OrdersController : Start getAuftragsartList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<DropdownObject> auftragsartList = orderService.getAuftragsartList();

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(auftragsartList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used for get Abgebendes Lager BA  (Delivering warehouse) List
	 */	
	@Override
	public ResponseEntity<AlphaXResponse> getAbgebendesLagerBAList() {

		log.info("OrdersController : Start getAbgebendesLagerBAList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<DropdownObject> abgebendesLagerBAList = orderService.getAbgebendesLagerBAList();

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(abgebendesLagerBAList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used for get Empfangendes Lager BA  (Receiving camp) List
	 */	
	@Override
	public ResponseEntity<AlphaXResponse> getEmpfangendesLagerBAList(
			@NotBlank @RequestParam(value = "partNo", required = true) String partNo,
			@NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer ) {

		log.info("OrdersController : Start getEmpfangendesLagerBAList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<DropdownObject> empfangendesLagerBAList = orderService.getEmpfangendesLagerBAList(decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), 
				decryptUtil.getApAgencyId(), partNo, warehouseNo, manufacturer);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(empfangendesLagerBAList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used for Search the Parts
	 */
	@Override
	public ResponseEntity<AlphaXResponse> searchPartsList( 
			@RequestParam(value = "oem", required = false) String oem,
			@RequestParam(value = "searchString", required = true) String searchString,
			@RequestParam(value = "warehouseNo", required = true) String warehouseNo, 
			@RequestParam(value = "pageSize", required = false) String pageSize,			
			@RequestParam(value = "pageNumber", required = false) String pageNumber,
			@RequestParam(value = "flag", required = false) String flag) {

		log.info("OrdersController : Start searchPartsList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		GlobalSearch globalSearch = orderService.searchPartsList(decryptUtil.getDataLibrary(), decryptUtil.getSchema(), decryptUtil.getApCompanyId(), 
				decryptUtil.getApAgencyId(), oem, searchString, warehouseNo, pageSize, pageNumber, flag);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(globalSearch);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This is OrdersController method used to get the Part Relocation Details List (Teileverlagerung) List.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getPartRelocationDetailsList( 
			@NotBlank @RequestParam(value = "partNo", required = true) String partNo,
			@NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer,
			@NotBlank @RequestParam(value = "warehouseList", required = true) String warehouseList) {

		log.info("OrdersController : Start getPartRelocationDetailsList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<PartRelocationDTO> partRelocationDetailList = orderService.getPartRelocationDetailsList(decryptUtil.getDataLibrary(), decryptUtil.getSchema(), 
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), partNo, warehouseNo, manufacturer, warehouseList, decryptUtil.getCompanyApWarehouse());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(partRelocationDetailList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This OrdersController method is used to check duplicate order number (Bestellnummer) from DB.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> checkDuplicateOrderNumber( 
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer,
			@NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@NotBlank @RequestParam(value = "orderNo", required = true) String orderNo,
			@NotBlank @RequestParam(value = "supplierNo", required = true) String supplierNo) {

		log.info("OrdersController : Start checkDuplicateOrderNumber");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String,Boolean> duplicateValueMap = orderService.checkDuplicateOrderNumber(decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), 
				decryptUtil.getApAgencyId(), manufacturer, warehouseNo, orderNo, supplierNo);

		if (duplicateValueMap != null && !duplicateValueMap.isEmpty()) {

			Map.Entry<String,Boolean> entry = duplicateValueMap.entrySet().iterator().next();
			if(entry.getValue()){
				alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
						LocaleContextHolder.getLocale(), ExceptionMessages.DUPLICATE_ORDER_NUMBER_MSG_KEY));	
			}else{
				alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
						LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
			}
		}
		alphaxResponse.setResultSet(duplicateValueMap);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This is OrdersController method used to get the Alternative Parts Details List.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getAlternativePartsDetails( 
			@NotBlank @RequestParam(value = "partNo", required = true) String partNo) {

		log.info("OrdersController : Start getAlternativePartsDetails");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<AlternativePartsDTO> alternativePartsDetailsList = orderService.getAlternativePartsDetails(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), 
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), partNo, null, null);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(alternativePartsDetailsList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This is OrdersController method used to Add the parts to Order Basket.
	 * @param orderbasketDetails
	 * @param dataLibrary
	 * @param companyId
	 * @param agencyId
	 * @param userName
	 * @param wsId
	 * @return
	 */
	@Override
	public ResponseEntity<AlphaXResponse> addPartsInOrderBasket(
			@Valid @RequestBody OrdersBasketDetailsDTO orderbasketDetails){

		log.info("OrdersController : Start addPartsInOrderBasket");
		AlphaXResponse<Object> response = new AlphaXResponse<>();
		Locale locale = LocaleContextHolder.getLocale();

		Map<String, String> addPartsToOrderBasketReceipt = orderService.addPartsInOrderBasket( orderbasketDetails, decryptUtil.getDataLibrary(), 
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), decryptUtil.getLogedInUser(), decryptUtil.getWsId());

		if (addPartsToOrderBasketReceipt != null && !addPartsToOrderBasketReceipt.isEmpty()) {

			Map.Entry<String,String> entry = addPartsToOrderBasketReceipt.entrySet().iterator().next();

			if (entry.getKey().equalsIgnoreCase("00000")) {
				response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, ExceptionMessages.SUCCESS_MESSAGE_KEY));
			}
			else {
				response.setResponseMessages(commonUtils.buildAlphaXResponseEntity(response.getResponseMessages(),
						entry.getValue(), entry.getKey(), RestURIConstants.MSG_TYPE_ERROR, locale.getLanguage().toUpperCase()));
			} 
		}
		else {
			response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, ExceptionMessages.ADD_PARTS_ORDER_BASKET_FAILED));
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	/**
	 * This method is used to generate order number using COBOL Program to create an Order.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> generateOrderNumber( 
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer,
			@NotBlank @RequestParam(value = "etManufacturer", required = true) String etManufacturer,
			@NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@NotBlank @RequestParam(value = "orderNo", required = true) String orderNo,
			@RequestParam(value = "supplierNo", required = false) String supplierNo){

		log.info("OrdersController : Start generateOrderNumber");
		AlphaXResponse<Object> response = new AlphaXResponse<>();
		Locale locale = LocaleContextHolder.getLocale();

		Map<String, String> generatedOrderNo = orderService.generateOrderNumber( decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), 
				manufacturer, etManufacturer, warehouseNo, orderNo, supplierNo, decryptUtil.getLogedInUser(), decryptUtil.getWsId());

		if (generatedOrderNo != null && !generatedOrderNo.isEmpty()) {

			if (generatedOrderNo.get("returnCode").equalsIgnoreCase("00000")) {
				//removed the return code and message from map
				generatedOrderNo.remove("returnCode");
				generatedOrderNo.remove("returnMsg");

				//set the new order number in result set
				response.setResultSet(generatedOrderNo);
				response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, ExceptionMessages.SUCCESS_MESSAGE_KEY));
			}
			else {
				response.setResponseMessages(commonUtils.buildAlphaXResponseEntity(response.getResponseMessages(),
						generatedOrderNo.get("returnMsg"), generatedOrderNo.get("returnCode"), RestURIConstants.MSG_TYPE_ERROR, locale.getLanguage().toUpperCase()));
			} 
		}			
		else {
			response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, ExceptionMessages.GENERATE_ORDER_NUMBER_FAILED));
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	/**
	 * This method is used to Create/Send the Order using COBOL Program.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> createOrders( 
			@Valid @RequestBody List<OrdersBasketDetailsDTO> OrderbasketDetailsList,
			@NotBlank @RequestParam(value = "newOrderNo", required = true) String newOrderNo,
			@NotBlank @RequestParam(value = "supplierNo", required = true) String supplierNo,
			@RequestParam(value = "orderNote", required = true) String orderNote){

		log.info("OrdersController : Start createOrders");
		AlphaXResponse<Object> response = new AlphaXResponse<>();
		Locale locale = LocaleContextHolder.getLocale();

		Map<String, String> createOrderReceipt = orderService.createOrders( OrderbasketDetailsList, decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), 
				decryptUtil.getApAgencyId(), newOrderNo, supplierNo, orderNote, decryptUtil.getLogedInUser(), decryptUtil.getWsId());

		if (createOrderReceipt != null && !createOrderReceipt.isEmpty()) {

			Map.Entry<String,String> entry = createOrderReceipt.entrySet().iterator().next();

			if (entry.getKey().equalsIgnoreCase("00000")) {
				response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, ExceptionMessages.SUCCESS_MESSAGE_KEY));
			}
			else {
				response.setResponseMessages(commonUtils.buildAlphaXResponseEntity(response.getResponseMessages(),
						entry.getValue(), entry.getKey(), RestURIConstants.MSG_TYPE_ERROR, locale.getLanguage().toUpperCase()));
			} 
		}
		else {
			response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, 
					ExceptionMessages.CREATE_FAILED_MSG_KEY, "Order (Bestellen)"));
		}
		return new ResponseEntity<>(response, HttpStatus.OK);		
	}


	/**
	 * This is OrdersController method used to check Parts history is available or not.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> checkAlternativePartsAvailability( 
			@Valid @RequestBody List<AlternativePartsAvailabilityDTO> alternativePartsAvailabilityList){

		log.info("OrdersController : Start checkAlternativePartsAvailability");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<AlternativePartsAvailabilityDTO> alternativePartsHistoryList = orderService.checkAlternativePartsAvailability(alternativePartsAvailabilityList, 
				decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(alternativePartsHistoryList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This is OrdersController method used to get the Ausgelöste Bestellungen (Triggered Orders) List.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getOrdersList( 
			@RequestParam(value = "pageSize", required = false) String pageSize,
			@RequestParam(value = "pageNumber", required = false) String pageNumber,
			@NotBlank @RequestParam(value = "sortingBy", required = true) String sortingBy, 
			@NotBlank @RequestParam(value = "sortingType", required = true) String sortingType,
			@NotEmpty @RequestParam(value = "warehouseNos", required = true) List<String> warehouseNos ) {

		log.info("OrdersController : Start getOrdersList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}

		GlobalSearch orderList = orderService.getOrdersList(decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), 
				allowedApWarehouses, pageSize, pageNumber, sortingBy, sortingType, warehouseNos);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(orderList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This is OrdersController method used to get the Ausgelöste Bestellungen Details List (Order Details List).
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getOrderDetailsList(
			@NotBlank @PathVariable(value = "id", required = true) String newOrderNumber,
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer,
			@NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo,
			@RequestParam(value = "isAserv", required = true) Boolean isAserv,
			@RequestParam(value = "supplierNo", required = false) String supplierNo,
			@NotBlank @RequestParam(value = "orderNumber", required = true) String orderNumber) {

		log.info("OrdersController : Start getOrderDetailsList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<OrdersBasketDetailsDTO> orderDetailsList = orderService.getOrderDetailsList(newOrderNumber, decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), 
				decryptUtil.getApAgencyId(), manufacturer, warehouseNo, isAserv, supplierNo, orderNumber);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(orderDetailsList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This is getOrdersListBasedOnFilter method used to get the Ausgelöste Bestellungen (Triggered Orders) List Based On Filter.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getOrdersListBasedOnFilter( 
			@RequestParam(value = "searchText", required = true) String searchText,
			@RequestParam(value = "searchBy", required = true) String searchBy,
			@RequestParam(value = "pageSize", required = false) String pageSize,
			@RequestParam(value = "pageNumber", required = false) String pageNumber,
			@NotBlank @RequestParam(value = "sortingBy", required = true) String sortingBy, 
			@NotBlank @RequestParam(value = "sortingType", required = true) String sortingType,
			@NotEmpty @RequestParam(value = "warehouseNos", required = true) List<String> warehouseNos ) {

		log.info("OrdersController : Start getOrdersListBasedOnFilter");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}

		GlobalSearch orderList = orderService.getOrdersListBasedOnFilter(decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), 
				allowedApWarehouses, pageSize, pageNumber, searchText, searchBy, sortingBy, sortingType, warehouseNos);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(orderList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used for get filter values for Ausgelöste Bestellungen (Triggered Orders)
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getFilterValuesForOrder() {

		log.info("OrdersController : Start getFilterValuesForOrder");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<DropdownObject> orderFilterList = orderService.getFilterValuesForOrder();

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(orderFilterList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used to transfer the Ausgelöste Bestellungen Orders to ET Warehouse.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> transferOrderstoET(
			@Valid @RequestBody OrdersBasketDetailsDTO orderbasketDetails){

		log.info("OrdersController : Start transferOrderstoET");
		AlphaXResponse<Object> response = new AlphaXResponse<>();
		Locale locale = LocaleContextHolder.getLocale();

		Map<String, String> ordersToETReceipt = orderService.transferOrderstoET( orderbasketDetails, decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), 
				decryptUtil.getApAgencyId() );

		if (ordersToETReceipt != null && !ordersToETReceipt.isEmpty()) {

			Map.Entry<String,String> entry = ordersToETReceipt.entrySet().iterator().next();

			if (entry.getKey().equalsIgnoreCase("00000")) {
				response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, ExceptionMessages.SUCCESS_MESSAGE_KEY));
			}
			else {
				response.setResponseMessages(commonUtils.buildAlphaXResponseEntity(response.getResponseMessages(),
						entry.getValue(), entry.getKey(), RestURIConstants.MSG_TYPE_ERROR, locale.getLanguage().toUpperCase()));
			} 
		}
		else {
			response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, ExceptionMessages.ORDER_TRANSFER_ET_FAILED_MSG_KEY));
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	/**
	 * This is getBestellKorbListBasedOnFilter method used to get the Offene Bestellungen (Open Orders List) Based On Filter.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getBestellKorbListBasedOnFilter( 
			@RequestParam(value = "searchText", required = true) String searchText,
			@RequestParam(value = "searchBy", required = true) String searchBy,
			@RequestParam(value = "pageSize", required = false) String pageSize,
			@RequestParam(value = "pageNumber", required = false) String pageNumber,
			@NotBlank @RequestParam(value = "sortingBy", required = true) String sortingBy, 
			@NotBlank @RequestParam(value = "sortingType", required = true) String sortingType,
			@NotEmpty @RequestParam(value = "warehouseNos", required = true) List<String> warehouseNos) {

		log.info("OrdersController : Start getBestellKorbListBasedOnFilter");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}

		GlobalSearch orderBasketList = orderService.getBestellKorbListBasedOnFilter(decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), 
				allowedApWarehouses, pageSize, pageNumber, searchText, searchBy, sortingBy, sortingType, warehouseNos);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(orderBasketList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * 
	 * This is method is used to update part number for Teilersetzung
	 */	 
	@Override
	public ResponseEntity<AlphaXResponse> replacePartNumber( 
			@Valid @RequestBody OrdersBasketDetailsDTO orderbasketDetails,
		    @NotBlank @RequestParam(value = "partNumber", required = true) String partNumber, 
		    @NotBlank @RequestParam(value = "partName", required = true) String partName){

		log.info("OrdersController : Start replacePartNumber");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean>  bestkorbUpdateFlagMap = orderService.replacePartNumber(orderbasketDetails, decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), 
				decryptUtil.getApAgencyId(), decryptUtil.getWsId(), decryptUtil.getLogedInUser(), partNumber, partName);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(bestkorbUpdateFlagMap);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This method is used to Create Order History.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> createOrderHistory( 
			@Valid @RequestBody List<OrdersBasketDetailsDTO> OrderbasketDetailsList,
			@NotBlank @RequestParam(value = "newOrderNo", required = true) String newOrderNo,
			@RequestParam(value = "orderNote", required = true) String orderNote){

		log.info("OrdersController : Start createOrderHistory");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> createOrderHistory = orderService.createOrderHistory( OrderbasketDetailsList, decryptUtil.getDataLibrary(),decryptUtil.getSchema(), 
				newOrderNo, decryptUtil.getLogedInUser(), orderNote);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(createOrderHistory);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);		
	}


	/**
	 * This method is used to Delete the Open Orders using COBOL Program.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> deleteOpenOrders( 
			@Valid @RequestBody List<OrdersBasketDetailsDTO> OrderbasketDetailsList){

		log.info("OrdersController : Start deleteOpenOrders");
		AlphaXResponse<Object> response = new AlphaXResponse<>();
		Locale locale = LocaleContextHolder.getLocale();

		Map<String, String> deleteOrdersReceipt = orderService.deleteOpenOrders( OrderbasketDetailsList, decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), 
				decryptUtil.getApAgencyId(), decryptUtil.getLogedInUser(), decryptUtil.getWsId() );

		if (deleteOrdersReceipt != null && !deleteOrdersReceipt.isEmpty()) {

			Map.Entry<String,String> entry = deleteOrdersReceipt.entrySet().iterator().next();

			if (entry.getKey().equalsIgnoreCase("00000")) {
				response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, ExceptionMessages.SUCCESS_MESSAGE_KEY));
			}
			else {
				response.setResponseMessages(commonUtils.buildAlphaXResponseEntity(response.getResponseMessages(),
						entry.getValue(), entry.getKey(), RestURIConstants.MSG_TYPE_ERROR, locale.getLanguage().toUpperCase()));
			} 
		}
		else {
			response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, ExceptionMessages.DELETE_ORDERS_FAILED_MSG_KEY));
		}
		//return the response
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	/**
	 * This is OrdersController method used to Get Bestellhistorie (order history list) List.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getOrderHistory( 
			@RequestParam(value = "pageSize", required = false) String pageSize,
			@RequestParam(value = "pageNumber", required = false) String pageNumber) {

		log.info("OrdersController : Start getBestellKorbList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		GlobalSearch orderHistoryList = orderService.getOrderHistory(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), 
				decryptUtil.getApAgencyId(), pageSize, pageNumber);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(orderHistoryList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This is OrdersController method used to Get Bestellhistorie (order history list) List based on filter
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getOrderHistoryBasedOnFilter(
			@RequestParam(value = "searchText", required = true) String searchText,
			@RequestParam(value = "searchBy", required = true) String searchBy,
			@RequestParam(value = "pageSize", required = false) String pageSize,
			@RequestParam(value = "pageNumber", required = false) String pageNumber) {

		log.info("OrdersController : Start getOrderHistoryBasedOnFilter");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		GlobalSearch orderHistoryList = orderService.getOrderHistoryBasedOnFilter(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), 
				decryptUtil.getApAgencyId(), pageSize, pageNumber, searchText, searchBy);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(orderHistoryList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This is OrdersController method used to get the Alternative Parts Details List for part search.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getAlternativePartsForPartSearch( 
			@NotBlank @RequestParam(value = "partNo", required = true) String partNo,
			@NotBlank @RequestParam(value = "manufacturer", required = true) String manufacturer,
			@NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo){

		log.info("OrdersController : Start getAlternativePartsDetails");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<AlternativePartsDTO> alternativePartsDetailsList = orderService.getAlternativePartsDetails(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), 
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), partNo, manufacturer, warehouseNo);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(alternativePartsDetailsList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This is method used to update the part relocation details using COBOL Program.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> updatePartRelocationDetails(
			@Valid @RequestBody PartRelocationDetailsDTO partRelocationDetails,
			@NotBlank @RequestParam(value = "relocationType", required = true) String relocationType){

		log.info("OrdersController : Start updatePartRelocationDetails");
		AlphaXResponse<Object> response = new AlphaXResponse<>();
		Locale locale = LocaleContextHolder.getLocale();

		Map<String, String> partRelocationResponse = orderService.updatePartRelocationDetails( partRelocationDetails, decryptUtil.getDataLibrary(), 
				decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), decryptUtil.getWsId(), relocationType, decryptUtil.getLogedInUser() );

		if (partRelocationResponse != null && !partRelocationResponse.isEmpty()) {

			Map.Entry<String,String> entry = partRelocationResponse.entrySet().iterator().next();

			if (entry.getKey().equalsIgnoreCase("00000")) {
				response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, ExceptionMessages.SUCCESS_MESSAGE_KEY));
			}
			else {
				response.setResponseMessages(commonUtils.buildAlphaXResponseEntity(response.getResponseMessages(),
						entry.getValue(), entry.getKey(), RestURIConstants.MSG_TYPE_ERROR, locale.getLanguage().toUpperCase()));
			} 
		}
		else {
			response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, 
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Verlagern (Part Relocation)"));
		}
		//return the response
		return new ResponseEntity<>(response, HttpStatus.OK);		
	}


	/**
	 * This method is used to check ESKDAT Order.
	 */	
	@Override
	public ResponseEntity<AlphaXResponse> checkESKDATOrders(){

		log.info("OrdersController : Start checkESKDATOrders");
		AlphaXResponse<Object> response = new AlphaXResponse<>();
		Locale locale = LocaleContextHolder.getLocale();

		Map<String, String> checkOrders = orderService.checkESKDATOrders(decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), decryptUtil.getApAgencyId(), 
				decryptUtil.getLogedInUser(), decryptUtil.getWsId());

		if (checkOrders != null && !checkOrders.isEmpty()) {

			response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, ExceptionMessages.SUCCESS_MESSAGE_KEY));
			response.setResultSet(checkOrders);
		}else{
			response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, 
					ExceptionMessages.CHECK_ESKDAT_ORDERS_FAILED_MSG_KEY, "check ESKDAT Orders "));
		}

		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	/**
	 * This is method used to get warehouse list for part movement from COBOL Program.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getWarehouseListForPartMovement(
			@NotBlank @RequestParam(value = "warehouseNo", required = true) String warehouseNo) {

		log.info("OrdersController : Start getWarehouseListForPartMovement");
		AlphaXResponse<Object> response = new AlphaXResponse<>();
		Locale locale = LocaleContextHolder.getLocale();

		Map<String, String> warehouseDetails = orderService.getWarehouseListForPartMovement(decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), 
				decryptUtil.getApAgencyId(), warehouseNo);

		if (warehouseDetails != null && !warehouseDetails.isEmpty()) {

			if (warehouseDetails.get("returnCode").equalsIgnoreCase("00000")) {
				//removed the return code and message from map
				warehouseDetails.remove("returnCode");
				warehouseDetails.remove("returnMsg");

				//set the new order number in result set
				response.setResultSet(warehouseDetails);
				response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, ExceptionMessages.SUCCESS_MESSAGE_KEY));
			}
			else {
				response.setResponseMessages(commonUtils.buildAlphaXResponseEntity(response.getResponseMessages(),
						warehouseDetails.get("returnMsg"), warehouseDetails.get("returnCode"), RestURIConstants.MSG_TYPE_ERROR, locale.getLanguage().toUpperCase()));
			} 
		}			
		else {
			response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, ExceptionMessages.GENERATE_ORDER_NUMBER_FAILED));
		}
		//return the response
		return new ResponseEntity<>(response, HttpStatus.OK);

	}


	/**
	 * This method is used for Search the Parts
	 */
	@Override
	public ResponseEntity<AlphaXResponse> searchPartsListForVerlegun( 
			@RequestParam(value = "oem", required = false) String oem,
			@RequestParam(value = "searchString", required = true) String searchString,
			@RequestParam(value = "pageSize", required = false) String pageSize,			
			@RequestParam(value = "pageNumber", required = false) String pageNumber,
			@RequestParam(value = "flag", required = false) String flag,
			@RequestParam(value = "warehouseNo", required = false) String warehouseNo) {

		log.info("OrdersController : Start searchPartsListForVerlegun");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}

		GlobalSearch globalSearch = orderService.searchPartsListForVerlegun(decryptUtil.getDataLibrary(), decryptUtil.getSchema(), decryptUtil.getApCompanyId(), 
				decryptUtil.getApAgencyId(), oem, searchString, pageSize, pageNumber, flag, allowedApWarehouses, warehouseNo);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(globalSearch);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}


	/**
	 * This is method used to recreate the .e36 file using COBOL Program.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> recreateE36File(
			@Valid @RequestBody OrdersBasketDetailsDTO orderBasketDto) {

		log.info("OrdersController : Start recreateE36File");
		AlphaXResponse<Object> response = new AlphaXResponse<>();
		Locale locale = LocaleContextHolder.getLocale();

		Map<String, String> recreationE36Result = orderService.recreateE36File(decryptUtil.getDataLibrary(), orderBasketDto);

		if (recreationE36Result != null && !recreationE36Result.isEmpty()) {

			if (recreationE36Result.get("returnCode").equalsIgnoreCase("00000")) {
				//removed the return code and message from map
				recreationE36Result.remove("returnCode");
				recreationE36Result.remove("returnMsg");

				response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, ExceptionMessages.SUCCESS_MESSAGE_KEY));
			}
			else {
				response.setResponseMessages(commonUtils.buildAlphaXResponseEntity(response.getResponseMessages(),
						recreationE36Result.get("returnMsg"), recreationE36Result.get("returnCode"), RestURIConstants.MSG_TYPE_ERROR, locale.getLanguage().toUpperCase()));
			} 
		}			
		else {
			response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), locale, ExceptionMessages.CREATE_FAILED_MSG_KEY, ".es36 file "));
		}
		//return the response
		return new ResponseEntity<>(response, HttpStatus.OK);		
	}


	/**
	 * This method is used to delete order Detail which have status Ausgelöst (all items delivered) From DB.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> deleteDeliveredOrder( 
			@Valid @RequestBody List<OrdersBasketDetailsDTO> deliveredOrdersList){

		log.info("OrdersController : Start deleteDeliveredOrder");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> deleteOrdersFlag = orderService.deleteDeliveredOrder(decryptUtil.getDataLibrary(), deliveredOrdersList);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(deleteOrdersFlag);

		//return the response
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This is OrdersController method used to get the Offene Bestellungen List (Open Order Basket) with part movement is possible or not.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> checkPartMovementPossible( 
			@Valid @RequestBody List<OrdersBasketDetailsDTO> orderBasketList) {

		log.info("OrdersController : Start checkPartMovementPossible");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<OrdersBasketDetailsDTO> orderBasketDetailList = orderService.checkPartMovementPossible(decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), 
				decryptUtil.getApAgencyId(), decryptUtil.getCompanyApWarehouse(), orderBasketList);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(orderBasketDetailList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}

	/**
	 * This is resetExecuteOrder method used to reset the Offene Bestellungen List (Open Order Basket) .
	 */
    @Override
	public ResponseEntity<AlphaXResponse> resetExecuteOrder(
			@Valid  OrdersBasketDetailsDTO ordersBasketDetDTO) {
		
    	log.info("OrdersController : Start resetExecuteOrder");
		AlphaXResponse<Object> response = new AlphaXResponse<>();
		Locale locale = LocaleContextHolder.getLocale();

		Map<String, Boolean> resetExecuteOrderList = orderService.resetExecuteOrder( ordersBasketDetDTO, decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), 
				decryptUtil.getApAgencyId(), decryptUtil.getLogedInUser(), decryptUtil.getWsId() );
		
		response.setResponseMessages(commonUtils.buildResponseMessage(response.getResponseMessages(), 
	            LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		response.setResultSet(resetExecuteOrderList); 
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
    
    /**
	 * This is OrdersController method used to get the Alternative Parts Details List for tree view.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getPartsDetailsInTreeView( 
			@NotBlank @RequestParam(value = "partNo", required = true) String partNo,
			@RequestParam(value = "manufacturer", required = false) String manufacturer, 
			@RequestParam(value = "warehouseNo", required = false) String warehouseNo) {

		log.info("OrdersController : Start getPartsDetailsInTreeView");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<PartsTreeViewDTO> alternativePartsDetailsList = orderService.getPartsDetailsInTreeView(decryptUtil.getSchema(), 
				decryptUtil.getDataLibrary(), partNo, manufacturer, warehouseNo);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(alternativePartsDetailsList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
}
