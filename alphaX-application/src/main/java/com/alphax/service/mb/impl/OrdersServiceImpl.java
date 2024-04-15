package com.alphax.service.mb.impl;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.alphax.common.rest.message.service.MessageService;
import com.alphax.model.mb.AlternativePartsDetails;
import com.alphax.model.mb.OrderHistory;
import com.alphax.model.mb.OrdersBasket;
import com.alphax.model.mb.OrdersBasketDetails;
import com.alphax.model.mb.OrdersObject;
import com.alphax.model.mb.OrdersObjectDetails;
import com.alphax.model.mb.PartCatalog;
import com.alphax.model.mb.PartRelocation;
import com.alphax.model.mb.SearchParts;
import com.alphax.model.mb.SearchParts2;
import com.alphax.common.exception.AlphaXException;
import com.alphax.common.exception.ExceptionMessages;
import com.alphax.repository.CobolServiceRepository;
import com.alphax.repository.DBServiceRepository;
import com.alphax.repository.StubServiceRepository;
import com.alphax.service.mb.OrdersService;
import com.alphax.util.DecryptTokenUtils;
import com.alphax.vo.mb.AlternativePartsAvailabilityDTO;
import com.alphax.vo.mb.AlternativePartsDTO;
import com.alphax.vo.mb.DropdownObject;
import com.alphax.vo.mb.GlobalSearch;
import com.alphax.vo.mb.KeyValuePairDTO;
import com.alphax.vo.mb.OrderHistoryDTO;
import com.alphax.vo.mb.OrdersBasketDetailsDTO;
import com.alphax.vo.mb.PartRelocationDTO;
import com.alphax.vo.mb.PartRelocationDetailsDTO;
import com.alphax.vo.mb.PartsTreeViewDTO;
import com.alphax.vo.mb.SearchPartsDTO;
import com.alphax.common.constants.Program_Commands_Constants;
import com.alphax.common.constants.RestInputConstants;
import com.ibm.as400.access.ProgramParameter;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrdersServiceImpl extends BaseService implements OrdersService {

	@Autowired
	DBServiceRepository dbServiceRepository;

	@Autowired
	private MessageService messageService;

	@Autowired
	StubServiceRepository stubServiceRepository;

	@Autowired
	CobolServiceRepository cobolServiceRepository;
	
	/*@Value("${alphax.datacenter.lib}")
	private String datacenterLibValue;*/
	
	@Autowired
	DecryptTokenUtils decryptUtil;
	

	/**
	 * This is method is used to get List of Offene Bestellungen (Open Orders Basket) from DB.
	 */
	@Override
	public GlobalSearch getBestellKorbList(String dataLibrary, String companyId, String agencyId, String allowedWarehouses, String pageSize, String pageNumber,
			String sortingBy, String sortingType, List<String> warehouseNos ) {

		log.info("Inside getBestellKorbList method of OrdersServiceImpl");

		List<OrdersBasket> orderBasketList = new ArrayList<>();
		GlobalSearch globalSearchList = new GlobalSearch();

		try {
			if(pageSize==null || pageNumber==null || pageSize.isEmpty() || pageNumber.isEmpty()){
				pageSize = RestInputConstants.DEFAULT_PAGE_SIZE;
				pageNumber = RestInputConstants.DEFAULT_PAGE_NO;
			}

			int totalRecords = Integer.parseInt(pageSize);
			int nextRows = totalRecords * (Integer.parseInt(pageNumber) - 1);

			//validate the page size
			validatePageSize(totalRecords);
			log.info("PageSize  : {}  OFFSET Size (Skip Rows):  {} Page Number  : {} ", pageSize, nextRows, pageNumber);
			
			//validate the Warehouse Ids
			validateWarehouses(allowedWarehouses);
			
			String orderByClause = getOrderByClause(sortingType, sortingBy, "Offene Bestellungen");
			
			if(warehouseNos != null && !warehouseNos.isEmpty()){
				warehouseNos.removeIf(s -> s == null || s.trim().isEmpty());
				allowedWarehouses = StringUtils.join(warehouseNos, ",");
			}
			
			StringBuilder query = new StringBuilder("Select distinct et.ET_LNR, et.BHERST, et.BEST_BEND, et.PSERV_AUFNR, et.ASERV_AUFNR, et.BDAT, et.ASERV_FA, et.ASERV_FIL, et.PSERV_FA, et.PSERV_FIL, ");
//			query.append("(Select NAME1 from ").append(dataLibrary).append(".e_etstamk6 where KEY2 = et.BEST_BEND ) AS BEST_DESC, ");
			query.append("CASE  WHEN  et.PSERV_AUFNR =0  then ");
			query.append("(Select KDNAM FROM ").append(dataLibrary).append(".F_AUFKO2 WHERE FA = et.ASERV_FA and  FIL = et.ASERV_FIL  and AUFNR = et.ASERV_AUFNR and RECHKZ = 1) ");
			query.append(" WHEN et.ASERV_AUFNR =0  then ");
			query.append("(Select KDNAM FROM ").append(dataLibrary).append(".F_PATKO2 WHERE FA = et.PSERV_FA and  FIL = et.PSERV_FIL  and AUFNR = et.PSERV_AUFNR and RECHKZ = 1) ");
			query.append(" END AS KUNDEN, ");

			query.append("(Select count(*) from ").append(dataLibrary).append(".e_eskdat where PSERV_AUFNR = et.PSERV_AUFNR and ASERV_AUFNR = et.ASERV_AUFNR ");
			query.append("and ET_LNR = et.ET_LNR  and BHERST = et.BHERST and BEST_BEND = et.BEST_BEND and ET_STATUS ='') AS positionen, et.ET_HERST, ");
			query.append("(select count(*) from ");
			query.append("(select  distinct ET_LNR, BHERST, BEST_BEND, PSERV_AUFNR, ASERV_AUFNR, BDAT from ").append(dataLibrary).append(".e_eskdat ");
			query.append(" where ET_STATUS ='' and not SERV_STATUS='R' and BEST_BEND='' ");			
			query.append(" and ET_LNR in (").append(allowedWarehouses).append(") )) AS ROWNUMER from ");			
			query.append(dataLibrary).append(".e_eskdat et where ET_STATUS ='' and not SERV_STATUS='R' and BEST_BEND='' ");			
			query.append(" and ET_LNR in (").append(allowedWarehouses).append(") ");
			
			//query.append(")  order by PSERV_AUFNR, ASERV_AUFNR, BHERST OFFSET "); comment out for task - ALPHAX-2297
			query.append(orderByClause).append(" OFFSET ");
			query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");

			List<OrdersBasketDetailsDTO> orderBasketDTOList = new ArrayList<>();
			orderBasketList = dbServiceRepository.getResultUsingQuery(OrdersBasket.class, query.toString(), true);

			//if the list is not empty
			if (orderBasketList != null && !orderBasketList.isEmpty()) {

				orderBasketDTOList = convertOrderBasketEntityToDTO(orderBasketList, orderBasketDTOList);

				globalSearchList.setSearchDetailsList(orderBasketDTOList);
				globalSearchList.setTotalPages(Integer.toString(orderBasketList.get(0).getTotalCount()));
				globalSearchList.setTotalRecordCnt(Integer.toString(orderBasketList.get(0).getTotalCount()));
			}
			else {
				globalSearchList.setSearchDetailsList(orderBasketDTOList);
				globalSearchList.setTotalPages(Integer.toString(0));
				globalSearchList.setTotalRecordCnt(Integer.toString(0));
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "BestellKorb (Order Basket)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "BestellKorb (Order Basket)"), exception);
			throw exception;
		}

		return globalSearchList;
	}


	/**
	 * This method is used to convert OrdersBasket Entity to OrdersBasket DTO Format.
	 * @param orderBasketList
	 * @return ordersBasketDTOList
	 */
	private List<OrdersBasketDetailsDTO> convertOrderBasketEntityToDTO(List<OrdersBasket> orderBasketList, List<OrdersBasketDetailsDTO> ordersBasketDetailsDTOList) {

		for (OrdersBasket orderBasket : orderBasketList) {
			OrdersBasketDetailsDTO ordersBasketDetailDTO = new OrdersBasketDetailsDTO();

			ordersBasketDetailDTO.setWarehouseNumber(orderBasket.getWarehouseNumber());
			if(orderBasket.getManufacturer()!= null && orderBasket.getManufacturer().equalsIgnoreCase(RestInputConstants.DCAG_STRING)) {
				ordersBasketDetailDTO.setManufacturer(RestInputConstants.DAG_STRING);
			}
			else {
				ordersBasketDetailDTO.setManufacturer(orderBasket.getManufacturer());
			}

			if(orderBasket.getAserv_Aufnr()!= null && !orderBasket.getAserv_Aufnr().equals("0")) {
				ordersBasketDetailDTO.setOrderNumber(StringUtils.leftPad(orderBasket.getAserv_Aufnr(), 6, "0"));
				ordersBasketDetailDTO.setIsAserv(true);
			}
			else {
				ordersBasketDetailDTO.setOrderNumber(StringUtils.leftPad(orderBasket.getPserv_Aufnr(), 6, "0"));
				ordersBasketDetailDTO.setIsAserv(false);
			}

			ordersBasketDetailDTO.setOrderPosition(orderBasket.getOrderPosition());
			ordersBasketDetailDTO.setOrderDate(convertDateToString(orderBasket.getOrderDate()));
			ordersBasketDetailDTO.setOrderTime(convertTimeToString(orderBasket.getOrderTime()));
			ordersBasketDetailDTO.setSupplierNo(orderBasket.getSupplierNo());
			ordersBasketDetailDTO.setSupplierName(orderBasket.getSupplierName());
			if(orderBasket.getEtManufacturer()!= null && orderBasket.getEtManufacturer().equalsIgnoreCase(RestInputConstants.DCAG_STRING)) {
				ordersBasketDetailDTO.setEtManufacturer(RestInputConstants.DAG_STRING);
			}
			else {
				ordersBasketDetailDTO.setEtManufacturer(orderBasket.getEtManufacturer());
			}
			ordersBasketDetailDTO.setCustomerName("");
			if(orderBasket.getCustomerName()!=null && !orderBasket.getCustomerName().isEmpty()) {
				ordersBasketDetailDTO.setCustomerName(orderBasket.getCustomerName().replaceAll("\\*", " "));
			}
			
			ordersBasketDetailDTO.setCustomerNameWithAsterisk(orderBasket.getCustomerName());
			ordersBasketDetailDTO.setNewOrderNo("--------");
			ordersBasketDetailDTO.setOrderNote("--------");
			ordersBasketDetailDTO.setOrderStatus(" ");
			
			ordersBasketDetailDTO.setDeliveryNoteNo("");
			ordersBasketDetailDTO.setTotalPartsDelivered("");
			ordersBasketDetailDTO.setPartPosition("");
			ordersBasketDetailDTO.setAserv_FA(String.valueOf(orderBasket.getAserv_FA())); 
			ordersBasketDetailDTO.setAserv_FIL(String.valueOf(orderBasket.getAserv_FIL()));
			ordersBasketDetailDTO.setPserv_FA(String.valueOf(orderBasket.getPserv_FA())); 
			ordersBasketDetailDTO.setPserv_FIL(String.valueOf(orderBasket.getPserv_FIL()));
			
			

			ordersBasketDetailsDTOList.add(ordersBasketDetailDTO);
		}
		//return the order List
		return ordersBasketDetailsDTOList;
	}


	/**
	 * This method is used to convert date string YYYYMMDD  to dd/MM/YYYY
	 * @param entityDate
	 * @return
	 */
	private String convertDateToString(String entityDate) {

		StringBuilder dateBuilder = new StringBuilder();

		if(entityDate!=null && !entityDate.isEmpty() && entityDate.length() == 8) {
			dateBuilder.append(entityDate.substring(6, 8)).append("/");
			dateBuilder.append(entityDate.substring(4, 6)).append("/").append(entityDate.substring(0, 4));
		}
		return dateBuilder.toString();
	}

	/**
	 * This method is used to convert Time string hhmmss  to hh:mm:ss
	 * @param entityTime
	 * @return
	 */
	private String convertTimeToString(String entityTime) {

		StringBuilder timeBuilder = new StringBuilder();

		if(entityTime!=null && !entityTime.isEmpty()) {

			if(entityTime.length() == 6) {
				timeBuilder.append(entityTime.substring(0, 2)).append(":");
				timeBuilder.append(entityTime.substring(2, 4)).append(":").append(entityTime.substring(4, 6));
			}

			if(entityTime.length() == 5) {
				timeBuilder.append("0").append(entityTime.substring(0, 1)).append(":");
				timeBuilder.append(entityTime.substring(1, 3)).append(":").append(entityTime.substring(3, 5));
			}			
		}
		return timeBuilder.toString();
	}


	/**
	 * This is method is used to get Detail List of Offene Bestellungen (Open Order Basket Details) based on order number from DB.
	 */
	@Override
	public List<OrdersBasketDetailsDTO> getBestellKorbDetailsList(String orderNumber, String dataLibrary, String companyId, String agencyId, String manufacturer, 
			String warehouseNo, Boolean isAserv, String supplierNo, String warehouseListFromtoken) {

		log.info("Inside getBestellKorbDetailsList method of OrdersServiceImpl");

		List<OrdersBasketDetailsDTO> orderBasketDetailsDTOList = new ArrayList<>();

		try {
			
			String warehouseListFromCobolforPartMovement = null;
			String finalWarehouseList = null;
			Map<String, String> warehouseDetails = getWarehouseListForPartMovement(dataLibrary, companyId, agencyId, warehouseNo);

			if (warehouseDetails != null && !warehouseDetails.isEmpty()) {
				
				if (warehouseDetails.get("returnCode").equalsIgnoreCase("00000")) {
					warehouseListFromCobolforPartMovement = warehouseDetails.get("warehouseList");
					warehouseListFromCobolforPartMovement = StringUtils.removeEnd(warehouseListFromCobolforPartMovement, ",");
				
				List<String> listFromCobol = Stream.of(warehouseListFromCobolforPartMovement.split(",", -1)).collect(Collectors.toList());
				List<String> listFromtoken = Stream.of(warehouseListFromtoken.split(",", -1)).collect(Collectors.toList());
				
				listFromtoken.retainAll(listFromCobol);
				finalWarehouseList = listFromtoken.stream().map(String::valueOf).collect(Collectors.joining(","));
				
				log.info("Warehouse list from cobol program : "+warehouseListFromCobolforPartMovement);
				log.info("Warehouse list from token :"+warehouseListFromtoken);
				log.info("final warehouse list belongs to the Company :"+finalWarehouseList);
				}
				
			}
			
			manufacturer = manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING : manufacturer;			

			StringBuilder query = new StringBuilder("Select et.BEST_BEND, et.MARKE, et.ET_ETNR, et.ET_BEN, et.SERV_MENGE_B, et.ET_LNR, et.PSRV_POSNR, et.ASRV_POSNR, et.ET_HERST, ");
//			query.append("(Select NAME1 from ").append(dataLibrary).append(".e_etstamk6 where KEY2 = et.BEST_BEND ) AS BEST_DESC, ");
			if(finalWarehouseList != null && !finalWarehouseList.isEmpty()){
				query.append("(Select count(*) from ").append(dataLibrary).append(".e_etstamm where TNR = et.ET_ETNR and  lnr IN (").append(finalWarehouseList).append(")");
				query.append(" and HERST='").append(manufacturer).append("'  and AKTBES > 0 ) AS CNT_TNR, ");
				}
			query.append(" ET_ETUP,ASERV_FA, ASERV_FIL, ASERV_AUFNR, ASERV_ERGNR, KFIRMA, KSERV_KDNUM, KSERV_SA, PSERV_FA, PSERV_FIL, PSERV_AUFNR, PSERV_SAART, PSERV_ERGNR,ASERV_SAART,BHERST, ");
			query.append("(Select count(*) from ");
			query.append(dataLibrary).append(".e_eskdat where ET_STATUS ='' and ET_LNR=").append(warehouseNo).append(" and  BHERST='").append(manufacturer);

			query.append( (isAserv) ? "' and ASERV_AUFNR =" : "' and PSERV_AUFNR =" );

			query.append(orderNumber).append(" and  BEST_BEND='' ) AS ROWNUMER from ");
			query.append(dataLibrary).append(".e_eskdat et where ET_STATUS ='' and ET_LNR=").append(warehouseNo).append(" and  BHERST='").append(manufacturer);

			query.append( (isAserv) ? "' and ASERV_AUFNR =" : "' and PSERV_AUFNR =" );

			query.append(orderNumber).append(" and BEST_BEND='' ORDER BY et.PSRV_POSNR, et.ASRV_POSNR, et.BEST_BEND, et.ET_ETNR");

			List<OrdersBasketDetails> orderBasketDetailList = dbServiceRepository.getResultUsingQuery(OrdersBasketDetails.class, query.toString(), true);

			//if the list is not empty
			if (orderBasketDetailList != null && !orderBasketDetailList.isEmpty()) {

				orderBasketDetailsDTOList = convertOrderBasketDetailsEntityToDTO(orderBasketDetailList, orderBasketDetailsDTOList, manufacturer, orderNumber, 
						isAserv, dataLibrary, warehouseListFromCobolforPartMovement);
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "BestellKorb Detail (Order Basket Detail)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "BestellKorb Detail (Order Basket Detail )"), exception);
			throw exception;
		}

		return orderBasketDetailsDTOList;
	}


	/**
	 * This method is used to convert OrdersBasketDetail Entity to OrdersBasketDetail DTO Format.
	 * @param orderBasketDetailList
	 * @param manufacturer 
	 * @param dataLibrary 
	 * @param warehouseListforPartMovement 
	 * @return ordersBasketDetailDTOList
	 */
	private List<OrdersBasketDetailsDTO> convertOrderBasketDetailsEntityToDTO(List<OrdersBasketDetails> orderBasketDetailList, List<OrdersBasketDetailsDTO> ordersBasketDetailDTOList, 
			String manufacturer, String orderNumber, Boolean isAserv, String dataLibrary, String warehouseListforPartMovement) {

		for (OrdersBasketDetails orderBasketDet : orderBasketDetailList) {
			OrdersBasketDetailsDTO ordersBasketDetailsDTO = new OrdersBasketDetailsDTO();

			ordersBasketDetailsDTO.setSupplierName(orderBasketDet.getSupplierName());
			ordersBasketDetailsDTO.setSupplierNo(orderBasketDet.getSupplierNo());
			ordersBasketDetailsDTO.setPart_Brand(orderBasketDet.getPart_Brand());

			ordersBasketDetailsDTO.setPartNumber(orderBasketDet.getPartNumber());
			ordersBasketDetailsDTO.setPartDescription(orderBasketDet.getPartDescription());

			ordersBasketDetailsDTO.setWarehouseNumber(orderBasketDet.getWarehouseNumber());
			ordersBasketDetailsDTO.setTotalPartsNumber(orderBasketDet.getTotalPartsNumber());
			ordersBasketDetailsDTO.setCount_Relocate(orderBasketDet.getCount_Relocate()!=null?orderBasketDet.getCount_Relocate():0);
			ordersBasketDetailsDTO.setWarehouseList(warehouseListforPartMovement);
			ordersBasketDetailsDTO.setOrderNumber(orderNumber);
			ordersBasketDetailsDTO.setIsAserv(isAserv);
			manufacturer = manufacturer.equalsIgnoreCase(RestInputConstants.DCAG_STRING)? RestInputConstants.DAG_STRING: manufacturer;
			ordersBasketDetailsDTO.setManufacturer(manufacturer);

			if(isAserv) {
				ordersBasketDetailsDTO.setOrderPositionSerialNo(orderBasketDet.getOrderPosition_ASRV().toString());
			}
			else {
				ordersBasketDetailsDTO.setOrderPositionSerialNo(orderBasketDet.getOrderPosition_PSRV().toString());
			}

			if(orderBasketDet.getEtManufacturer()!= null && orderBasketDet.getEtManufacturer().equalsIgnoreCase(RestInputConstants.DCAG_STRING)) {
				ordersBasketDetailsDTO.setEtManufacturer(RestInputConstants.DAG_STRING);
			}
			else {
				ordersBasketDetailsDTO.setEtManufacturer(orderBasketDet.getEtManufacturer());
			}
			ordersBasketDetailsDTO.setDeliveryNoteNo("");
			ordersBasketDetailsDTO.setTotalPartsDelivered("");
			ordersBasketDetailsDTO.setPartPosition("");
			
			//below fields for part replacement
			ordersBasketDetailsDTO.setEt_ETUP(String.valueOf(orderBasketDet.getEt_ETUP())); 
			ordersBasketDetailsDTO.setAserv_FA(String.valueOf(orderBasketDet.getAserv_FA())); 
			ordersBasketDetailsDTO.setAserv_FIL(String.valueOf(orderBasketDet.getAserv_FIL())); 
			ordersBasketDetailsDTO.setAsrv_AUFNR(String.valueOf(orderBasketDet.getAserv_AUFNR())); 
			ordersBasketDetailsDTO.setAserv_SAART(String.valueOf(orderBasketDet.getAserv_SAART()));
			ordersBasketDetailsDTO.setAsrv_ERGNR(String.valueOf(orderBasketDet.getAserv_ERGNR())); 
			ordersBasketDetailsDTO.setKfirma(String.valueOf(orderBasketDet.getKfirma())); 
			ordersBasketDetailsDTO.setKsrv_KDNUM(orderBasketDet.getKserv_KDNUM()); 
			ordersBasketDetailsDTO.setKserv_SA(String.valueOf(orderBasketDet.getKserv_SA())); 
			ordersBasketDetailsDTO.setPserv_FA(String.valueOf(orderBasketDet.getPserv_FA())); 
			ordersBasketDetailsDTO.setPserv_FIL(String.valueOf(orderBasketDet.getPserv_FIL())); 
			ordersBasketDetailsDTO.setPsrv_AUFNR(String.valueOf(orderBasketDet.getPserv_AUFNR())); 
			ordersBasketDetailsDTO.setPsrv_SAART(String.valueOf(orderBasketDet.getPserv_SAART())); 
			ordersBasketDetailsDTO.setPsrv_ERGNR(String.valueOf(orderBasketDet.getPserv_ERGNR())); 
			ordersBasketDetailsDTO.setbManufacturer(String.valueOf(orderBasketDet.getbManufacturer())); 
			ordersBasketDetailsDTO.setOrderPosition_ASRV(String.valueOf(orderBasketDet.getOrderPosition_ASRV()));
			ordersBasketDetailsDTO.setOrderPosition_PSRV(String.valueOf(orderBasketDet.getOrderPosition_PSRV()));
			
			ordersBasketDetailDTOList.add(ordersBasketDetailsDTO);
		} 
		//return the order Detail List
		return ordersBasketDetailDTOList;
	}


	/**
	 * This method is used to get the TeilMarke (Brand) List based on provided OEM.
	 * @param schema
	 * @param manufacturer
	 * @return DropdownObject
	 */
	@Override
	public List<DropdownObject> getTeileMarkeList(String dataLibrary, String schema, String manufacturer) {

		log.info("Inside getTeilMarkeList method of OrdersServiceImpl");
		List<DropdownObject> markeDetailsList = new ArrayList<>();

		try {
			manufacturer = manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING : manufacturer;

			if(null != manufacturer && manufacturer.equalsIgnoreCase("SONS")) {

				DropdownObject dropdownObject = new DropdownObject();
				dropdownObject.setKey("X");
				dropdownObject.setValue("X");
				markeDetailsList.add(dropdownObject);
			}
			else {

				StringBuilder query = new StringBuilder("SELECT TMA_TMARKE AS KEYFLD, TMA_BEZEI AS DATAFLD FROM ");
				query.append(schema).append(".PMH_TMARKE where TMA_HERST='").append(manufacturer).append("' ORDER BY TMA_TMARKE");

				Map<String, String> markeDetailsMap = dbServiceRepository.getResultUsingCobolQuery(query.toString());

				if(markeDetailsMap != null && !markeDetailsMap.isEmpty()) {
					for(Map.Entry<String, String> markeDetails : markeDetailsMap.entrySet()) {

						DropdownObject dropdownObject = new DropdownObject();
						dropdownObject.setKey(markeDetails.getKey());
						dropdownObject.setValue(markeDetails.getValue());

						markeDetailsList.add(dropdownObject);
					}
				}else{
					log.info("Teilemarke List is empty");
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Teilemarke (Brand)"));
					log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Teilemarke (Parts Brand)"));
					throw exception;
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Teilemarke (Brand)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Teilemarke (Parts Brand)"), exception);
			throw exception;
		}

		return markeDetailsList;
	}


	/**
	 * This method is used to get the Auftragsart (Order type) List from Stub
	 * @return List
	 */

	@Override
	public List<DropdownObject> getAuftragsartList() {

		log.info("Inside getAuftragsartList method of OrdersServiceImpl");

		List<DropdownObject> auftragsartList = new ArrayList<>();
		auftragsartList = stubServiceRepository.getDDLValuesFromStub(StubServiceRepository.auftragsartMap);
		return auftragsartList;
	}

	/**
	 * This method is used to get the Abgebendes Lager BA  (Delivering warehouse) List from Stub
	 * @return List
	 */

	@Override
	public List<DropdownObject> getAbgebendesLagerBAList() {

		log.info("Inside getAbgebendesLagerBAList method of OrdersServiceImpl");

		List<DropdownObject> abgebendesLagerBAList = new ArrayList<>();
		abgebendesLagerBAList = stubServiceRepository.getDDLValuesFromStub(StubServiceRepository.abgebendesLagerBAMap);
		return abgebendesLagerBAList;
	}


	/**
	 * This method is used to get the Empfangendes Lager BA  (Receiving camp) List from Stub
	 * @return List
	 */
	@Override
	public List<DropdownObject> getEmpfangendesLagerBAList(String dataLibrary, String companyId, String agencyId, String partNo, 
			String warehouseNo,String manufacturer) {

		log.info("Inside getEmpfangendesLagerBAList method of OrdersServiceImpl");
		
		String count = "0";
		List<DropdownObject> empfangendesLagerBAList = new ArrayList<>();
		List<DropdownObject> empfangendesLagerBAList_stub = stubServiceRepository.getDDLValuesFromStub(StubServiceRepository.empfangendesLagerBAMap);
		
		try {
			manufacturer = manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING : manufacturer;
			StringBuilder query = new StringBuilder(" Select COUNT(TNR) AS COUNT from ");
			query.append(dataLibrary).append(".E_ETSTAMM where ");
			query.append("TNR = ").append("'"+partNo+"'").append(" AND LNR = ").append(warehouseNo).append(" AND HERST = ").append("'"+manufacturer+"'");
			
			count = dbServiceRepository.getCountUsingQuery(query.toString());
			log.info("count -  {}", count);
			
			//if the count is zero
			if (count != null && count.equalsIgnoreCase("0")) {
				
				empfangendesLagerBAList = empfangendesLagerBAList_stub.stream()
                        .filter(i -> (i.getKey().equals("01") || i.getKey().equals("02")))
                        .collect(Collectors.toList());
			}
			else {
				empfangendesLagerBAList = empfangendesLagerBAList_stub.stream()
                        .filter(i -> i.getKey().equals("06"))
                        .collect(Collectors.toList());
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Empfangendes Lager BA"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Empfangendes Lager BA"), exception);
			throw exception;
		}
		
		return empfangendesLagerBAList;
	}


	/**
	 * This method is used for Search the Parts based on provided parts name or part number.
	 */
	@Override
	public GlobalSearch searchPartsList(String dataLibrary, String schema, String companyId, String agencyId, String oem, String searchString, 
			String warehouseNo, String pageSize, String pageNo, String flag) {
		log.info("Inside searchPartsList method of OrdersServiceImpl");

		List<SearchParts> partsList = new ArrayList<>();
		GlobalSearch globalSearchList = new GlobalSearch();

		try {

			validateCompany(companyId);
			/*validateAgency(agencyId);
			
			String agencyIds = decryptUtil.getAllowedApAgencies();
			log.info("User's Agency Ids  xx:  {} ", agencyIds);
			//validate the Agency Ids
			validateAgencys(agencyIds);*/

			if(pageSize==null || pageNo==null || pageSize.isEmpty() || pageNo.isEmpty()){
				pageSize = RestInputConstants.DEFAULT_PAGE_SIZE;
				pageNo = RestInputConstants.DEFAULT_PAGE_NO;
			}

			int totalRecords = Integer.parseInt(pageSize);
			int nextRows = totalRecords * (Integer.parseInt(pageNo) - 1);

			//validate the page size
			validatePageSize(totalRecords);

			log.info("PageSize  : {}  OFFSET Size (Skip Rows):  {} Page Number  : {} ", pageSize, nextRows, pageNo);
			
			if((searchString.startsWith("A") || searchString.startsWith("a")) && searchString.length() >= 11) {
				searchString = searchString.substring(0, 11) + '%' + searchString.substring(11);
			}

			oem = (oem != null && oem.equalsIgnoreCase(RestInputConstants.DAG_STRING))? RestInputConstants.DCAG_STRING : oem;

			StringBuilder query = new StringBuilder("SELECT herst, tnr, lnr, benen, aktbes, filial, tmarke, LOPA, SA, (SELECT NAME FROM  ");  
			query.append(schema).append(".O_WRH WHERE AP_WAREHOUS_ID = lnr) AS LagerName, ");
			query.append(" (SELECT COUNT(*) FROM  ");
			query.append(dataLibrary).append(".E_ETSTAMM WHERE (UPPER(tnr) LIKE UPPER('%").append(searchString).append("%') OR UPPER(benen) LIKE UPPER('%");
			query.append(searchString).append("%')) AND herst='").append(oem).append("' AND lnr=").append(warehouseNo).append("");
			query.append(" ) AS ROWNUMER  FROM ");  
			query.append(dataLibrary).append(".E_ETSTAMM WHERE (UPPER(tnr) LIKE UPPER('%").append(searchString).append("%') OR UPPER(benen) LIKE UPPER('%");
			query.append(searchString).append("%')) AND herst='").append(oem).append("' AND lnr=").append(warehouseNo).append("");
			query.append(" Order by tnr OFFSET ");
			query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");

			List<SearchPartsDTO> vehiclePartsList = new ArrayList<>();
			partsList = dbServiceRepository.getResultUsingQuery(SearchParts.class, query.toString(),true);

			//if the list is not empty
			if (partsList != null && !partsList.isEmpty()) {
				for (SearchParts parts : partsList) {

					SearchPartsDTO partsDTO = new SearchPartsDTO();

					partsDTO.setOem(parts.getOem());
					partsDTO.setPartNumber(parts.getPartNumber());
					partsDTO.setWarehouse(parts.getWarehouse());
					partsDTO.setAgency(parts.getAgency());
					partsDTO.setDescription(parts.getDescription());
					partsDTO.setStock(parts.getStock());
					partsDTO.setOemBrand(parts.getOemBrand());
					partsDTO.setStorageLocation(parts.getStorageLocation());
					partsDTO.setStorageIndikator(String.valueOf(parts.getStorageIndikator()));
					partsDTO.setStorageIndikatorFlag(true);
					partsDTO.setAlphaXWarehouseName(parts.getAlphaXWarehouseName());

					vehiclePartsList.add(partsDTO);
				}
				globalSearchList.setSearchDetailsList(vehiclePartsList);
				globalSearchList.setTotalPages(Integer.toString(partsList.get(0).getTotalCount()));
				globalSearchList.setTotalRecordCnt(Integer.toString(partsList.get(0).getTotalCount()));
			} 
			//this conditions for Teileverlagerung screen part search
			else if(flag != null && flag.equalsIgnoreCase("Teileverlagerung")) {
				
				globalSearchList.setSearchDetailsList(vehiclePartsList);
				globalSearchList.setTotalPages(Integer.toString(0));
				globalSearchList.setTotalRecordCnt(Integer.toString(0));
			}
			else {
				//get the parts details from other DB tables.
				getPartsListOfDCAG_NonDCAG(globalSearchList, schema, oem, searchString, nextRows, pageSize);
			}
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message: {}",  e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.SEARCH_FAILED_MSG_KEY, "Parts"));
			log.error(messageService.getReadableMessage(ExceptionMessages.SEARCH_FAILED_MSG_KEY, "Parts"), exception);
			throw exception;
		}

		return globalSearchList;
	}


	private void getPartsListOfDCAG_NonDCAG(GlobalSearch globalSearchList, String schema, String oem, String searchString, int nextRows, String pageSize){

		List<SearchPartsDTO> vehiclePartsList = new ArrayList<>();
		List<SearchParts2> partsList = new ArrayList<>();
		StringBuilder query;

		if( oem.equalsIgnoreCase(RestInputConstants.DCAG_STRING) ) {
			query = new StringBuilder("SELECT distinct tle_herst AS herst, tle_tnr AS tnr, tle_benena AS benen, tle_tmarke AS tmarke, ( SELECT COUNT(DISTINCT tle_tnr) FROM  ");
			query.append("(select distinct tle_herst, tle_tnr, tle_benena, tle_tmarke from ");
			query.append(schema).append(".etk_tlekat WHERE (UPPER(tle_tnr) LIKE UPPER('%").append(searchString).append("%') OR UPPER(tle_benena) LIKE UPPER('%");
			query.append(searchString).append("%')) )) AS ROWNUMER  FROM ");
			query.append(schema).append(".etk_tlekat etk WHERE (UPPER(tle_tnr) LIKE UPPER('%").append(searchString).append("%') OR UPPER(tle_benena) LIKE UPPER('%");
			query.append(searchString).append("%')) AND ").append(" tle_dtgulv = (SELECT MAX(tle_dtgulv) FROM ").append(schema).append(".etk_tlekat WHERE tle_tnr = etk.tle_tnr  AND  tle_dtgulv <= current date ) Order by etk.tle_tnr ");
			query.append(" OFFSET ").append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");

		}
		else {
			query = new StringBuilder("SELECT distinct tkf_herst AS herst, tkf_tnr AS tnr, tkf_benen AS benen, tkf_tmarke AS tmarke, (SELECT COUNT(DISTINCT tkf_tnr ) FROM  ");
			query.append("(select distinct tkf_herst, tkf_tnr, tkf_benen, tkf_tmarke from ");
			query.append(schema).append(".etk_tkfher WHERE (upper(tkf_tnr) LIKE upper('%").append(searchString).append("%') OR UPPER(tkf_benen) LIKE UPPER('%");
			query.append(searchString).append("%')) AND tkf_herst='").append(oem).append("' )) AS ROWNUMER  FROM ");
			query.append(schema).append(".etk_tkfher etf WHERE (upper(tkf_tnr) LIKE upper('%").append(searchString).append("%') OR UPPER(tkf_benen) LIKE UPPER('%");
			query.append(searchString).append("%')) AND tkf_herst='").append(oem).append("' AND  TKF_DTGULV = (SELECT max(TKF_DTGULV) FROM ").append(schema).append(".etk_tkfher WHERE tkf_tnr = etf.tkf_tnr and TKF_DTGULV <= current date and tkf_herst = etf.tkf_herst ) Order by etf.tkf_tnr");
			query.append(" OFFSET ").append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");

		}

		partsList = dbServiceRepository.getResultUsingQuery(SearchParts2.class, query.toString(),true);

		if (partsList != null && !partsList.isEmpty()) {
			for (SearchParts2 parts : partsList) {

				SearchPartsDTO partsDTO = new SearchPartsDTO();

				partsDTO.setOem(parts.getOem());
				partsDTO.setPartNumber(parts.getPartNumber());
				partsDTO.setWarehouse("-------");
				partsDTO.setAgency("------");
				partsDTO.setDescription(parts.getDescription());
				partsDTO.setStock("-------");
				partsDTO.setOemBrand(parts.getOemBrand());
				
				partsDTO.setStorageIndikator("-----");
				partsDTO.setStorageIndikatorFlag(false);

				vehiclePartsList.add(partsDTO);
			}
			globalSearchList.setSearchDetailsList(vehiclePartsList);
			globalSearchList.setTotalPages(Integer.toString(partsList.get(0).getTotalCount()));
			globalSearchList.setTotalRecordCnt(Integer.toString(partsList.get(0).getTotalCount()));
		} 
		else {
			globalSearchList.setSearchDetailsList(vehiclePartsList);
			globalSearchList.setTotalPages(Integer.toString(0));
			globalSearchList.setTotalRecordCnt(Integer.toString(0));
		}
	}

	/**
	 * This is method is used to get Detail List of Teileverlagerung (Relocation of parts) based on part number ,warehouse number from DB.
	 */
	@Override
	public List<PartRelocationDTO> getPartRelocationDetailsList(String dataLibrary, String schema, String companyId, String agencyId, String partNo, 
			String warehouseNo, String manufacturer, String warehouseListFromCobolforPartMovement, String warehouseListFromtoken) {

		log.info("Inside getPartRelocationDetailsList method of OrdersServiceImpl");

		List<PartRelocationDTO> partRelocationDetailsList =  new ArrayList<>();
		String daten = "";

		try {
			warehouseListFromCobolforPartMovement = StringUtils.removeEnd(warehouseListFromCobolforPartMovement, ",");
			
			List<String> listFromCobol = Stream.of(warehouseListFromCobolforPartMovement.split(",", -1)).collect(Collectors.toList());
			List<String> listFromtoken = Stream.of(warehouseListFromtoken.split(",", -1)).collect(Collectors.toList());
			
			listFromtoken.retainAll(listFromCobol);
			String finalWarehouseList = listFromtoken.stream().map(String::valueOf).collect(Collectors.joining(","));
			
			log.info("Warehouse list from cobol program : "+warehouseListFromCobolforPartMovement);
			log.info("Warehouse list from token :"+warehouseListFromtoken);
			log.info("final warehouse list belongs to the Company :"+finalWarehouseList);
			List<PartRelocation> partRelocationList = null;
			
			
			if(finalWarehouseList != null && !finalWarehouseList.trim().isEmpty()){
			manufacturer = manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING : manufacturer;
			StringBuilder query = new StringBuilder(" SELECT ET.EPR, ET.AKTBES, ET.LNR, ET.VKAVGW, ET.DTLABG, ET4.PNAME ,ET4.PORT, ");
			query.append("( SELECT NAME FROM ").append(schema).append(".O_WRH WHERE AP_WAREHOUS_ID = ET.LNR) AS LagerName FROM ");
			query.append(dataLibrary).append(".E_ETSTAMM ET , ").append(dataLibrary).append(".E_ETSTAMK4 ET4  WHERE ");
			query.append(" ET.TNR = ").append("'"+partNo+"'").append(" AND ET.AKTBES > 0 ").append(" AND ET.LNR IN( ").append(finalWarehouseList).append(") AND ET.HERST = ").append("'"+manufacturer+"'").append(" AND ET4.KEY2 = ET.LNR");

			partRelocationList = dbServiceRepository.getResultUsingQuery(PartRelocation.class, query.toString(), true);
			
			}
			//if the list is not empty
			if (partRelocationList != null && !partRelocationList.isEmpty()) {
				
				StringBuilder queryForBeleg = new StringBuilder(" SELECT SUBSTR(DATEN,2,5)+1 AS DATEN from ").append(dataLibrary).append(".F_FKTREF  where  KEY='00000122850000' ");
				
				List<PartRelocation> datenDataList = dbServiceRepository.getResultUsingQuery(PartRelocation.class, queryForBeleg.toString(), true);
				
				if(datenDataList!=null && !datenDataList.isEmpty()) {
					
					daten = datenDataList.get(0).getDaten();
				}

				partRelocationDetailsList = convertPartRelocationDetailsEntityToDTO(partRelocationList, partRelocationDetailsList, daten, warehouseNo);
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Part Relocation Details (Teileverlagerung)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Part Relocation Details (Teileverlagerung)"), exception);
			throw exception;
		}

		return partRelocationDetailsList;
	}
	
	
	

	private List<PartRelocationDTO> convertPartRelocationDetailsEntityToDTO(List<PartRelocation> partRelocationList,
			List<PartRelocationDTO> partRelocationDetailsList, String daten, String warehouseNo) {

		for(PartRelocation partRelocationDtl : partRelocationList){

			PartRelocationDTO partRelocationDTO = new PartRelocationDTO();
			partRelocationDTO.setWarehouseName(partRelocationDtl.getAlphaXWarehouseName()!=null ? partRelocationDtl.getAlphaXWarehouseName(): "");
			partRelocationDTO.setPrice(partRelocationDtl.getPrice());
			partRelocationDTO.setBestand(partRelocationDtl.getBestand());
			partRelocationDTO.setWarehouseNumber(partRelocationDtl.getWarehouseNumber());
			partRelocationDTO.setWarehouseCity(partRelocationDtl.getWarehouseCity());			
			partRelocationDTO.setAverageSales(String.valueOf(partRelocationDtl.getAverageSales()));
			partRelocationDTO.setLastDisposalDate(convertDateToString(String.valueOf(partRelocationDtl.getLastDisposalDate())));

			//This change is as per task no - ALPHAX-3311/ALPHAX-3337
			StringBuilder str = new StringBuilder(StringUtils.leftPad(daten, 5, " ")).append("V")
					.append(StringUtils.leftPad(partRelocationDtl.getWarehouseNumber(), 2, "0")).append(StringUtils.leftPad(warehouseNo, 2, "0")).append("001");
					
			partRelocationDTO.setNote(str.toString());
			partRelocationDetailsList.add(partRelocationDTO);
		}

		return partRelocationDetailsList;
	}

	/**
	 * This is method is used to check duplicate order number (Bestellnummer) from DB.
	 */
	@Override
	public Map<String, Boolean> checkDuplicateOrderNumber(String dataLibrary, String companyId, String agencyId,
			String manufacturer, String warehouseNo, String orderNo, String supplierNo) {

		log.info("Inside checkDuplicateOrderNumber method of OrdersServiceImpl");

		if(isValidOrderNo(orderNo)){
			log.info("Order number is not valid");
			AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.INVALID_MSG_KEY, "Order Number (Bestellnummer) "));
			log.error(messageService.getReadableMessage(ExceptionMessages.INVALID_MSG_KEY, "Order Number (Bestellnummer) "));
			throw exception;
		}

		String bestArt = orderNo.substring(0,2);
		String bestAp = orderNo.substring(2);
		log.info("bestArt :  {} and bestAp :  {}", bestArt, bestAp);

		Map<String, Boolean> outputMap = new HashMap<>();
		outputMap.put("isDuplicate", false);
		String count = "0";
		try {
			manufacturer = manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING : manufacturer;
			StringBuilder query = new StringBuilder(" SELECT COUNT(*) AS COUNT FROM ");
			query.append(dataLibrary).append(".E_BSNDAT WHERE HERST = ").append("'"+manufacturer+"'").append(" AND LNR = ").append(warehouseNo);
			query.append(" AND BEST_ART = ").append(Integer.parseInt(bestArt)).append(" AND BEST_AP = ").append(Integer.parseInt(bestAp));
			query.append(" AND  BEST_BENDL = ").append(Integer.parseInt(supplierNo));

			count = dbServiceRepository.getCountUsingQuery(query.toString());

			if(Integer.parseInt(count) > 0){
				outputMap.put("isDuplicate", true);	
			}


		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Duplicate Order Number"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Duplicate Order Number"), exception);
			throw exception;
		}

		return outputMap;
	}

	private boolean isValidOrderNo(String orderNo) {

		boolean flag = true;
		if (orderNo != null && orderNo.trim().length() == 6 && !orderNo.substring(0, 2).equalsIgnoreCase("00")
				&& !orderNo.substring(2).equalsIgnoreCase("0000")) {
			try {
				Integer.parseInt(orderNo);
				flag = false;

			} catch (Exception e) {
				flag = true;
			}

		}
		return flag;
	}

	/**
	 * This is method is used to get Alternative Parts details from DB.
	 */
	@Override
	public List<AlternativePartsDTO> getAlternativePartsDetails(String schema, String dataLibrary, String companyId, String agencyId,String partNumber, String manufacturer,
			String warehouseNumber) {

		log.info("Inside getAlternativePartsDetails method of OrdersServiceImpl");
		//AlternativePartsDetailsDTO alternativePartsDetailsDTO = new AlternativePartsDetailsDTO();
		List<AlternativePartsDTO> alternativePartsFinalList = new ArrayList<>();
		List<AlternativePartsDTO> alternativePartsDTOList = null;
		String ACTUAL = "Aktuelles Teil";
		String SUCCESSOR = "Nachfolger";
		String PREDECESSOR = "Vorgänger";
		Integer numberOfTimes = 1;
		Integer sequence = 0;
		try {
		Map<String, String> keyValueDetails = new HashMap<>();
		/*String dataCenterLibrary  = dataLibrary;
			log.info("DataCenter flag :" +datacenterLibValue);
			if(datacenterLibValue.equalsIgnoreCase("1")){
				dataCenterLibrary = "APLUSCOM";
			}*/
		
			String dataCenterLibrary = null;
			ProgramParameter[] parmList = new ProgramParameter[3];

			String returnCode = StringUtils.rightPad("", 5, " ");
			parmList[0] = new ProgramParameter(returnCode.getBytes(Program_Commands_Constants.IBM_273), 5);

			String returnMsg = StringUtils.rightPad("", 100, " ");
			parmList[1] = new ProgramParameter(returnMsg.getBytes(Program_Commands_Constants.IBM_273), 10);

			String location = StringUtils.rightPad("", 10, " ");
			parmList[2] = new ProgramParameter(location.getBytes(Program_Commands_Constants.IBM_273), 10);

			// execute the COBOL program - OFSMFIN.PGM
			List<String> outputList = cobolServiceRepository.executeProgram(parmList, 0,Program_Commands_Constants.GET_DATA_CENTER_LIB_FOR_PREDECESSOR_SUCCESSOR);

			if (outputList != null && !outputList.isEmpty()) {
				if (!outputList.get(0).contains("Error")) {
					dataCenterLibrary = outputList.get(2).trim();
				}
			}
			log.info("DataCenter Library :" + dataCenterLibrary);
			if (dataCenterLibrary == null || dataCenterLibrary.isEmpty()) {
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(
						LocaleContextHolder.getLocale(), ExceptionMessages.INVALID_MSG_KEY, " DataCenter Library"));
				log.error(messageService.getReadableMessage(ExceptionMessages.INVALID_MSG_KEY, " DataCenter Library"),
						exception);
				throw exception;
			}

			StringBuilder queryForText = new StringBuilder(" SELECT CODE,TEXT FROM ").append(dataCenterLibrary).append(".ETHTXTTAB ");

			List<AlternativePartsDetails> alternativePartsTextsList = dbServiceRepository.getResultUsingQuery(AlternativePartsDetails.class, queryForText.toString(), true);

			if (alternativePartsTextsList != null && !alternativePartsTextsList.isEmpty()) {

				for (AlternativePartsDetails partsDetails2 : alternativePartsTextsList) {
					keyValueDetails.put(partsDetails2.getCode(), partsDetails2.getCodeValue());
				}
			}

			alternativePartsDTOList =  new ArrayList<>();
			log.debug("#### getPartDetails() method for actual (Aktuelles Teil ) part ####");
			List<AlternativePartsDTO> actualPartsList =  getPartDetails(partNumber,dataLibrary,ACTUAL,alternativePartsDTOList,keyValueDetails,numberOfTimes, manufacturer, warehouseNumber,schema,companyId,agencyId,dataCenterLibrary);


			alternativePartsDTOList =  new ArrayList<>();
			log.debug("#### getPartDetails() method for successor (Nachfolger) part ####");
			List<AlternativePartsDTO> successorPartsList =  getPartDetails(partNumber,dataLibrary,SUCCESSOR,alternativePartsDTOList,keyValueDetails,numberOfTimes, manufacturer, warehouseNumber,schema,companyId,agencyId,dataCenterLibrary);


			alternativePartsDTOList =  new ArrayList<>();
			log.debug("#### getPartDetails() method for predecessor (Vorgänger) part ####");
			List<AlternativePartsDTO> predecessorPartsList =  getPartDetails(partNumber,dataLibrary,PREDECESSOR,alternativePartsDTOList,keyValueDetails,numberOfTimes, manufacturer, warehouseNumber,schema,companyId,agencyId,dataCenterLibrary);

			//if the list is not empty then retrieve predecessor list in reveres order
			if(predecessorPartsList != null && predecessorPartsList.size() > 0){
				//predecessorPartsList.remove(0);
				Collections.reverse(predecessorPartsList);
				sequence= predecessorPartsList.size();
				for(AlternativePartsDTO predecessorPart :predecessorPartsList ){
					predecessorPart.setType("Vorgänger "+sequence--);
					alternativePartsFinalList.add(predecessorPart);
				}
			}

			//if the list is not empty then set actual part details
			if(actualPartsList != null && !actualPartsList.isEmpty()){

				for(AlternativePartsDTO actualParts : actualPartsList){
					alternativePartsFinalList.add(actualParts);
				}
			}

			//if the list is not empty then set successor list
			if(successorPartsList != null && successorPartsList.size() > 1){
				successorPartsList.remove(0);
				sequence=0;
				for(AlternativePartsDTO successorParts : successorPartsList){
					successorParts.setType("Nachfolger "+ ++sequence);
					alternativePartsFinalList.add(successorParts);
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Alternative Parts details"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Alternative Parts details"), exception);
			throw exception;
		}

		return alternativePartsFinalList ;
	}

	//this method is use to get part details in recursive  way 
	private List<AlternativePartsDTO>  getPartDetails(String partNumber, String dataLibrary,String type,List<AlternativePartsDTO> alternativePartsDTOList, Map<String, String> keyValueDetails, Integer numberOfTimes, String manufacturer,
			String warehouseNumber,String schema,String companyId, String agencyId, String dataCenterLibrary) {
		log.debug("getPartDetails() method call in recursive  way ## Type : " +type +" Number of times :"+numberOfTimes);
		AlternativePartsDTO alternativePartsDTO = new AlternativePartsDTO();
		List<AlternativePartsDetails> alternativePartsDetailsList = null;
		List<AlternativePartsDetails> alternativePartsNameList = null;
		KeyValuePairDTO keyValuePairDTO = null;
		List<KeyValuePairDTO> keyValuePairDTOList = new ArrayList<>();
		boolean partNameRequired = true;

		if(type.equalsIgnoreCase("Aktuelles Teil") ){
			//this query use to get successor and actual part details
			StringBuilder successor_query = new StringBuilder(" SELECT CODEF,CODEB FROM ");
			successor_query.append(dataCenterLibrary).append(".ETHIPS1Z1 WHERE TNR = ").append("'"+partNumber+"'");
			successor_query.append(" AND (CODEF=21 OR CODEF=22 OR CODEF=24) ");

			alternativePartsDetailsList = dbServiceRepository.getResultUsingQuery(AlternativePartsDetails.class,successor_query.toString(), true);
		}else if(type.equalsIgnoreCase("Nachfolger")){
			//this query use to get successor and actual part details
			StringBuilder successor_query = new StringBuilder(" SELECT TNRN,CODEF,CODEB FROM ");
			successor_query.append(dataCenterLibrary).append(".ETHIPS1Z1 WHERE TNR = ").append("'"+partNumber+"'");
			successor_query.append(" AND (CODEF=21 OR CODEF=22 OR CODEF=24) ");

			alternativePartsDetailsList = dbServiceRepository.getResultUsingQuery(AlternativePartsDetails.class,successor_query.toString(), true);
		}else{
			//this query use to get predecessor part details
			StringBuilder predecessor_query = new StringBuilder(" SELECT TNR,CODEF,CODEB  FROM ");
			predecessor_query.append(dataCenterLibrary).append(".ETHIPS1Z1 WHERE TNRN = ").append("'"+partNumber+"'");
			predecessor_query.append(" AND (CODEF=21 OR CODEF=22 OR CODEF=24) ");
			
			partNameRequired = false;
			alternativePartsDetailsList = dbServiceRepository.getResultUsingQuery(AlternativePartsDetails.class,predecessor_query.toString(), true);

			if (alternativePartsDetailsList != null && alternativePartsDetailsList.size() > 0) {
				AlternativePartsDetails partsDetails = alternativePartsDetailsList.get(0);
				partNumber = partsDetails.getPartNumber();
				partNameRequired = true;
			}

		}

		if (partNumber != null && !partNumber.isEmpty() && partNameRequired) {
			// this query use to get part name from E_ETSTAMM
			if (manufacturer != null && warehouseNumber != null) {

				manufacturer = manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING: manufacturer;			
				
				StringBuilder query = new StringBuilder(" SELECT BENEN AS partName, AKTBES FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE ");
				query.append("HERST = ").append("'" + manufacturer + "'");
				query.append(" AND LNR = ").append(warehouseNumber);
				query.append(" AND TNR = ").append("'" + partNumber + "'");
				//query.append(" AND FIRMA = ").append(companyId);//.append(" AND FILIAL = ").append(agencyId);

				alternativePartsNameList = dbServiceRepository.getResultUsingQuery(AlternativePartsDetails.class,
						query.toString(), true);

				if (alternativePartsNameList != null && alternativePartsNameList.size() > 0) {
					log.debug("Get details from E_ETSTAMM");
					AlternativePartsDetails part = alternativePartsNameList.get(0);
					alternativePartsDTO.setPartName(part.getPartName());
					alternativePartsDTO.setCurrentStock(String.valueOf(part.getCurrentStock()));

				}

			}else{
			
			StringBuilder part_name_query = new StringBuilder(" SELECT DISTINCT BENEN AS partName FROM ");
			part_name_query.append(dataLibrary).append(".E_ETSTAMM  WHERE TNR = ").append("'" + partNumber + "'");

			alternativePartsNameList = dbServiceRepository.getResultUsingQuery(AlternativePartsDetails.class,part_name_query.toString(), true);

			if (alternativePartsNameList != null && alternativePartsNameList.size() > 0) {
				log.debug("Get details from E_ETSTAMM");
				AlternativePartsDetails part = alternativePartsNameList.get(0);
				alternativePartsDTO.setPartName(part.getPartName());
			}else{
				StringBuilder query = new StringBuilder(" Select TLE_BENENA FROM ").append(schema).append(".ETK_TLEKAT WHERE ");
				query.append(" TLE_TNR= ").append("'"+partNumber+"'");
				query.append(" AND TLE_DTGULV <= current date order by TLE_DTGULV desc LIMIT 1");
				
				List<PartCatalog> finalizationsPartBAList = dbServiceRepository.getResultUsingQuery(PartCatalog.class, query.toString(), true);
				//if the list is not empty
				if (finalizationsPartBAList != null && !finalizationsPartBAList.isEmpty()) {
					log.debug("Get details from ETK_TLEKAT");
					PartCatalog partCatalog =  finalizationsPartBAList.get(0);
					alternativePartsDTO.setPartName(partCatalog.getPartName());
				}
			}
			
		}
	}


		if (alternativePartsDetailsList != null && alternativePartsDetailsList.size() > 0) {
			AlternativePartsDetails partsDetails = alternativePartsDetailsList.get(0);
			numberOfTimes++;

			if ((partsDetails.getNewPartNumber() == null || partsDetails.getNewPartNumber().isEmpty()) && type.equalsIgnoreCase("Nachfolger")) {
				return alternativePartsDTOList;
			}

			if ((partsDetails.getPartNumber() == null || partsDetails.getPartNumber().isEmpty()) && type.equalsIgnoreCase("Vorgänger")) {
				return alternativePartsDTOList;
			}

			if(partsDetails.getCodeEB() != null && !partsDetails.getCodeEB().isEmpty()){
				keyValuePairDTO = new KeyValuePairDTO();
				keyValuePairDTO.setCode(partsDetails.getCodeEB());
				keyValuePairDTO.setCodeValue(keyValueDetails.get(partsDetails.getCodeEB()));
				keyValuePairDTOList.add(keyValuePairDTO);
			}

			if(partsDetails.getCodeEF() != null && !partsDetails.getCodeEF().isEmpty()){
				keyValuePairDTO = new KeyValuePairDTO();
				keyValuePairDTO.setCode(partsDetails.getCodeEF());
				keyValuePairDTO.setCodeValue(keyValueDetails.get(partsDetails.getCodeEF()));
				keyValuePairDTOList.add(keyValuePairDTO);
			}

			String nextPartNo = "";
			if (type.equalsIgnoreCase("Aktuelles Teil")) {
				alternativePartsDTO.setPartNumber(partNumber);
				alternativePartsDTO.setType("Aktuelles Teil 0");
				nextPartNo = partNumber;
			}

			if (type.equalsIgnoreCase("Nachfolger")) {
				alternativePartsDTO.setPartNumber(partNumber);
				nextPartNo = partsDetails.getNewPartNumber();
			}

			if (type.equalsIgnoreCase("Vorgänger")) {
				alternativePartsDTO.setPartNumber(partsDetails.getPartNumber());
				nextPartNo = partsDetails.getPartNumber();
			}

			alternativePartsDTO.setKeyValueList(keyValuePairDTOList);
			alternativePartsDTOList.add(alternativePartsDTO);

			if (!type.equalsIgnoreCase("Aktuelles Teil")) {
				getPartDetails(nextPartNo, dataLibrary, type, alternativePartsDTOList,keyValueDetails,numberOfTimes, manufacturer, warehouseNumber,schema,companyId,agencyId,dataCenterLibrary);
			}
		}else{
			if (type.equalsIgnoreCase("Aktuelles Teil")) {
				alternativePartsDTO.setPartNumber(partNumber);
				alternativePartsDTO.setType("Aktuelles Teil 0");
				alternativePartsDTOList.add(alternativePartsDTO);
			}

			if (type.equalsIgnoreCase("Nachfolger")) {
				alternativePartsDTO.setPartNumber(partNumber);
				alternativePartsDTOList.add(alternativePartsDTO);
			}
		}

		return alternativePartsDTOList;
	}


	/**
	 * This method is used to delete the Delivery Notes using COBOL program.
	 */
	@Override
	public Map<String, String> addPartsInOrderBasket(OrdersBasketDetailsDTO ordersBasketDetails, String dataLibrary, 
			String companyId, String agencyId, String userName, String wsId) {
		log.info("Inside addPartsInOrderBasket method of OrdersServiceImpl");

		ProgramParameter[] parmList = new ProgramParameter[12];
		Map<String, String> programOutput = new HashMap<String, String>();
		try{

			// Create the input parameters 
			String returnCode = StringUtils.rightPad("", 5, " ");
			parmList[0] = new ProgramParameter(returnCode.getBytes(Program_Commands_Constants.IBM_273), 5);

			String returnMsg  = StringUtils.rightPad("", 100, " ");
			parmList[1] = new ProgramParameter(returnMsg.getBytes(Program_Commands_Constants.IBM_273), 100);

			String inWSId = StringUtils.rightPad(wsId, 2, " ");
			parmList[2] = new ProgramParameter(inWSId.getBytes(Program_Commands_Constants.IBM_273), 2);

			String inUserName = StringUtils.rightPad(userName, 10, " ");
			parmList[3] = new ProgramParameter(inUserName.getBytes(Program_Commands_Constants.IBM_273), 4);

			String inManufacturer = StringUtils.rightPad(ordersBasketDetails.getEtManufacturer(), 4, " ");
			parmList[4] = new ProgramParameter(inManufacturer.getBytes(Program_Commands_Constants.IBM_273), 4);

			String inWarehousNo = StringUtils.leftPad(ordersBasketDetails.getWarehouseNumber(), 2, "0");
			parmList[5] = new ProgramParameter(inWarehousNo.getBytes(Program_Commands_Constants.IBM_273), 4);

			String inOrderNumber = StringUtils.leftPad(ordersBasketDetails.getOrderNumber(), 6, "0");
			parmList[6] = new ProgramParameter(inOrderNumber.getBytes(Program_Commands_Constants.IBM_273), 4);

			String inPartNumber = StringUtils.rightPad(ordersBasketDetails.getPartNumber(), 19, " ");
			parmList[7] = new ProgramParameter(inPartNumber.getBytes(Program_Commands_Constants.IBM_273), 4);

			String partOEM = ordersBasketDetails.getPartOEM().equalsIgnoreCase(RestInputConstants.DCAG_STRING)? RestInputConstants.DAG_STRING : ordersBasketDetails.getPartOEM();

			String inPartOEM = StringUtils.rightPad(partOEM, 4, " ");
			parmList[8] = new ProgramParameter(inPartOEM.getBytes(Program_Commands_Constants.IBM_273), 4);

			String inPartDescription = StringUtils.rightPad(ordersBasketDetails.getPartDescription(), 19, " ");
			parmList[9] = new ProgramParameter(inPartDescription.getBytes(Program_Commands_Constants.IBM_273), 4);

			String inPartBrand = StringUtils.rightPad(ordersBasketDetails.getPart_Brand(), 2, " ");
			parmList[10] = new ProgramParameter(inPartBrand.getBytes(Program_Commands_Constants.IBM_273), 2);

			String inPartQuantity = StringUtils.leftPad(ordersBasketDetails.getTotalPartsNumber(), 7, "0");
			parmList[11] = new ProgramParameter(inPartQuantity.getBytes(Program_Commands_Constants.IBM_273), 4);

			//execute the COBOL program - OBADD100CL.PGM
			List<String> outputList = cobolServiceRepository.executeProgram(parmList, 0, Program_Commands_Constants.ADD_PARTS_IN_ORDER_PROGRAM );

			if(outputList != null && !outputList.isEmpty()) {
				if(!outputList.get(0).contains("Error")) {
					programOutput.put(outputList.get(0).trim(), outputList.get(1).trim());
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.ADD_PARTS_ORDER_BASKET_FAILED));
			log.error(messageService.getReadableMessage(ExceptionMessages.ADD_PARTS_ORDER_BASKET_FAILED), exception);
			throw exception;
		}
		return programOutput;
	}


	/**
	 * This method is used to generate order number using COBOL Program to create an Order.
	 */
	@Override
	public Map<String, String> generateOrderNumber(String dataLibrary, String companyId, String agencyId, String manufacturer, String etManufacturer, 
			String warehouseNo, String orderNo, String supplierNo, String userName, String wsId ) {
		log.info("Inside generateOrderNumber method of OrdersServiceImpl");

		Map<String, String> programOutput = new HashMap<String, String>();
		String progamInputParam = "1";
		
		try{

			ProgramParameter[] parmList = setParametersForPgm("", wsId, userName, progamInputParam, etManufacturer, manufacturer, warehouseNo, 
					 "", supplierNo, Arrays.asList(orderNo), Arrays.asList(""), Arrays.asList("") );

			//execute the COBOL program - OBCRT100CL.PGM
			List<String> outputList = cobolServiceRepository.executeProgram(parmList, 0, Program_Commands_Constants.SEND_CREATE_ORDER_PROGRAM );

			if(outputList != null && !outputList.isEmpty()) {
				if(!outputList.get(0).contains("Error")) {
					String pgmOrderNumber = outputList.get(2).trim();
					String newSupplierNumber = outputList.get(10).trim();

					programOutput.put("returnCode", outputList.get(0).trim());
					programOutput.put("returnMsg", outputList.get(1).trim());

					if(pgmOrderNumber != null && pgmOrderNumber.length() == 6) {
						String newOrderType = pgmOrderNumber.substring(0, 2);
						String newOrderNumber = pgmOrderNumber.substring(2, 6);

						programOutput.put("newOrderType", newOrderType);
						programOutput.put("newOrderNumber", newOrderNumber);
						programOutput.put("newSupplierNumber", newSupplierNumber);
					}
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GENERATE_ORDER_NUMBER_FAILED));
			log.error(messageService.getReadableMessage(ExceptionMessages.GENERATE_ORDER_NUMBER_FAILED), exception);
			throw exception;
		}

		return programOutput;
	}

	private ProgramParameter[] setParametersForPgm(String outOrderNo, String wsId, String userName, String progamParam, String etManufacturer, 
			String manufacturer, String warehouseNo, String orderNote, String supplierNo, List<String> orderNoList, List<String> orderPostionNumberList,
			List<String> customerLastNameList ) throws Exception {

		ProgramParameter[] parmList = new ProgramParameter[(orderNoList.size()*3)+11];

		// Create the input parameters 
		String returnCode = StringUtils.rightPad("", 5, " ");
		parmList[0] = new ProgramParameter(returnCode.getBytes(Program_Commands_Constants.IBM_273), 5);

		String returnMsg  = StringUtils.rightPad("", 100, " ");
		parmList[1] = new ProgramParameter(returnMsg.getBytes(Program_Commands_Constants.IBM_273), 100);

		String outOrderNumber = StringUtils.rightPad(outOrderNo, 6, " ");
		parmList[2] = new ProgramParameter(outOrderNumber.getBytes(Program_Commands_Constants.IBM_273), 6);

		String inWSId = StringUtils.rightPad(wsId, 2, " ");
		parmList[3] = new ProgramParameter(inWSId.getBytes(Program_Commands_Constants.IBM_273), 2);

		String inUserName = StringUtils.rightPad(userName, 10, " ");
		parmList[4] = new ProgramParameter(inUserName.getBytes(Program_Commands_Constants.IBM_273), 4);

		String inProgramParam = StringUtils.rightPad(progamParam, 1, " ");
		parmList[5] = new ProgramParameter(inProgramParam.getBytes(Program_Commands_Constants.IBM_273), 1);

		String inEtManufacturer = StringUtils.rightPad(etManufacturer, 4, " ");
		parmList[6] = new ProgramParameter(inEtManufacturer.getBytes(Program_Commands_Constants.IBM_273), 4);

		String inManufacturer = StringUtils.rightPad(manufacturer, 4, " ");
		parmList[7] = new ProgramParameter(inManufacturer.getBytes(Program_Commands_Constants.IBM_273), 4);

		String inWarehousNo = StringUtils.leftPad(warehouseNo, 2, "0");
		parmList[8] = new ProgramParameter(inWarehousNo.getBytes(Program_Commands_Constants.IBM_273), 4);

		String inOrderNote = StringUtils.rightPad(orderNote, 6, " ");
		parmList[9] = new ProgramParameter(inOrderNote.getBytes(Program_Commands_Constants.IBM_273), 4);

		String inSupplierNumber = StringUtils.leftPad(supplierNo, 5, "0");
		parmList[10] = new ProgramParameter(inSupplierNumber.getBytes(Program_Commands_Constants.IBM_273), 2);
		Integer count = 11;
		
		for(int i=0; i< orderNoList.size(); i++) {
			
			String inOrderNumber = StringUtils.leftPad(orderNoList.get(i), 6, "0");
			parmList[count] = new ProgramParameter(inOrderNumber.getBytes(Program_Commands_Constants.IBM_273), 4);
			count++;
			String inOrderPostionNumber = StringUtils.rightPad(orderPostionNumberList.get(i), 1500, " ");
			parmList[count] = new ProgramParameter(inOrderPostionNumber.getBytes(Program_Commands_Constants.IBM_273), 4);
			count++;
			String inCustomerName = StringUtils.rightPad(StringUtils.left(customerLastNameList.get(i), 6), 6, " ");
			parmList[count] = new ProgramParameter(inCustomerName.getBytes(Program_Commands_Constants.IBM_273), 4);
			count++;
		}
		
		return parmList;
	}


	/**
	 * This method is used to Create/Send the Order using COBOL Program.
	 */
	@Override
	public Map<String, String> createOrders( List<OrdersBasketDetailsDTO> OrderbasketDetailsList, String dataLibrary, String companyId, String agencyId, 
			 String newOrderNo, String supplierNo, String orderNote, String userName, String wsId ) {
		log.info("Inside createOrders method of OrdersServiceImpl");

		Map<String, String> programOutput = new HashMap<String, String>();
		String progamInputParam = "2";
		String etManufacturer = "";
		String manufacturer = "";
		String warehouseNo = "";
//		String supplierNo ="";
		List<String> orderNumbeList = new ArrayList<>();
		List<String> orderPositionNoList = new ArrayList<>();
		List<String> customerNameList = new ArrayList<>();
		try{
			
			if(OrderbasketDetailsList!=null && !OrderbasketDetailsList.isEmpty()) {
				
				for(OrdersBasketDetailsDTO ordersBasketDetDTO : OrderbasketDetailsList) {
					
					etManufacturer = ordersBasketDetDTO.getEtManufacturer();
					manufacturer = ordersBasketDetDTO.getManufacturer();
					warehouseNo = ordersBasketDetDTO.getWarehouseNumber();
//					supplierNo = ordersBasketDetDTO.getSupplierNo();
					
					//add order number and orderPositionNo in List
					orderNumbeList.add(ordersBasketDetDTO.getOrderNumber());
					orderPositionNoList.add(createOrderPositionValue(ordersBasketDetDTO.getOrderBasketDetList()));
					//add customerName in the List
					customerNameList.add(getCustomerLastName(ordersBasketDetDTO.getCustomerNameWithAsterisk()!=null ? 
							ordersBasketDetDTO.getCustomerNameWithAsterisk() : ""));
				}

				ProgramParameter[] parmList = setParametersForPgm(newOrderNo, wsId, userName, progamInputParam, etManufacturer, manufacturer, 
						warehouseNo, orderNote, supplierNo, orderNumbeList, orderPositionNoList, customerNameList);

				//execute the COBOL program - OBCRT100CL.PGM
				List<String> outputList = cobolServiceRepository.executeProgram(parmList, 0, Program_Commands_Constants.SEND_CREATE_ORDER_PROGRAM );

				if(outputList != null && !outputList.isEmpty()) {
					if(!outputList.get(0).contains("Error")) {
						programOutput.put(outputList.get(0).trim(), outputList.get(1).trim());
					}
				}
				
			}
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.CREATE_FAILED_MSG_KEY, "Order (Bestellen)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY, "Order (Bestellen)"), exception);
			throw exception;
		}

		return programOutput;
	}


	/**
	 * This method is used to create OrderPositionString.
	 * @param OrderbasketDetailsList
	 * @return
	 */
	private String createOrderPositionValue(List<OrdersBasketDetailsDTO> OrderbasketDetailsList) {
		StringBuilder orderPositionNo = new StringBuilder("");

		if(OrderbasketDetailsList!=null && !OrderbasketDetailsList.isEmpty()) {

			for(OrdersBasketDetailsDTO ordersBasketDetails: OrderbasketDetailsList) {

				orderPositionNo.append(StringUtils.leftPad(ordersBasketDetails.getOrderPositionSerialNo(), 4, "0")).append("|");
			}
		}
		//return the String value
		return orderPositionNo.toString();
	}
	
	
	/**
	 * This method is used to create Customer Last name.
	 * @param OrderbasketDetailsList
	 * @return
	 */
	private String getCustomerLastName(String customerName) {
		
		String customerLastName = "";
		
		List<String> customerNameList = Arrays.stream(customerName.split("\\*"))
                .collect(Collectors.toList());
		
		if(customerNameList != null && customerNameList.size() > 1) {
			customerLastName = customerNameList.get(1).trim();
		}
		else if(customerNameList != null && customerNameList.size() == 1) {
			customerLastName = customerNameList.get(0).trim();
		}
				
		return customerLastName;
	}
	

	/**
	 * This method is used to check part predecessor and successor.
	 * @param alternativePartsAvailabilityList
	 * @param dataLibrary 
	 */
	@Override
	public List<AlternativePartsAvailabilityDTO> checkAlternativePartsAvailability(
			List<AlternativePartsAvailabilityDTO> alternativePartsAvailabilityList, String dataLibrary,
			String companyId, String agencyId) {
		log.info("Inside checkAlternativePartsAvailability method of OrdersServiceImpl");
		
		List<AlternativePartsAvailabilityDTO> partsAvailabilityList = new ArrayList<>();
		Map<String, Boolean> partNumberMap = new HashMap<>();
		try {
			String dataCenterLibrary = null;
			ProgramParameter[] parmList = new ProgramParameter[3];

			String returnCode = StringUtils.rightPad("", 5, " ");
			parmList[0] = new ProgramParameter(returnCode.getBytes(Program_Commands_Constants.IBM_273), 5);

			String returnMsg = StringUtils.rightPad("", 100, " ");
			parmList[1] = new ProgramParameter(returnMsg.getBytes(Program_Commands_Constants.IBM_273), 10);

			String location = StringUtils.rightPad("", 10, " ");
			parmList[2] = new ProgramParameter(location.getBytes(Program_Commands_Constants.IBM_273), 10);

			// execute the COBOL program - OFSMFIN.PGM
			List<String> outputList = cobolServiceRepository.executeProgram(parmList, 0,Program_Commands_Constants.GET_DATA_CENTER_LIB_FOR_PREDECESSOR_SUCCESSOR);

			if (outputList != null && !outputList.isEmpty()) {
				if (!outputList.get(0).contains("Error")) {
					dataCenterLibrary = outputList.get(2).trim();
				}
			}
			log.info("DataCenter Library :" + dataCenterLibrary);
			if (dataCenterLibrary == null || dataCenterLibrary.isEmpty()) {
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(
						LocaleContextHolder.getLocale(), ExceptionMessages.INVALID_MSG_KEY, " DataCenter Library"));
				log.error(messageService.getReadableMessage(ExceptionMessages.INVALID_MSG_KEY, " DataCenter Library"),
						exception);
				throw exception;
			}

			if (alternativePartsAvailabilityList != null && alternativePartsAvailabilityList.size() > 0) {
				for (AlternativePartsAvailabilityDTO partsAvailability : alternativePartsAvailabilityList) {

					if (partsAvailability.getPartNumber() != null && !partsAvailability.getPartNumber().isEmpty()) {

						if (!partNumberMap.containsKey(partsAvailability.getPartNumber())) {
							boolean historyFlag = isPartPredecessor_Successor(partsAvailability.getPartNumber(),dataCenterLibrary);
							partsAvailability.setPartHistory(historyFlag);
							partNumberMap.put(partsAvailability.getPartNumber(), historyFlag);
						} else {
							partsAvailability.setPartHistory(partNumberMap.get(partsAvailability.getPartNumber()));
						}
					}
					partsAvailabilityList.add(partsAvailability);
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Alternative Parts details"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Alternative Parts Availability details"), exception);
			throw exception;
		}
		return partsAvailabilityList;
	}

	/**
	 * This method is used to check part predecessor and successor.
	 * @param partNumber
	 * @param dataLibrary 
	 */
	private Boolean isPartPredecessor_Successor(String partNumber, String dataLibrary) {

		log.debug("isPartPredecessor_Successor() method of OrdersServiceImpl :" +partNumber);
		List<AlternativePartsDetails> alternativePartsDetailsList = null;
		boolean isPartHistory = false;

		if (partNumber != null && !partNumber.isEmpty()) {
			// this query to check part predecessor
			StringBuilder predecessor_query = new StringBuilder(" SELECT TNR FROM ");
			predecessor_query.append(dataLibrary).append(".ETHIPS1Z1 WHERE TNRN = ").append("'" + partNumber + "'");
			predecessor_query.append(" AND (CODEF=21 OR CODEF=22 OR CODEF=24) AND TNR <>  TNRN ");

			alternativePartsDetailsList = dbServiceRepository.getResultUsingQuery(AlternativePartsDetails.class,
					predecessor_query.toString(), true);

			if (alternativePartsDetailsList != null && alternativePartsDetailsList.size() > 0) {
				AlternativePartsDetails partsDetails = alternativePartsDetailsList.get(0);
				if ((partsDetails.getPartNumber() != null && !partsDetails.getPartNumber().isEmpty())) {
					isPartHistory = true;
					log.debug("is predecessor available :"+isPartHistory);
				}
			}

			if (!isPartHistory) {
				// this query to check part successor
				StringBuilder successor_query = new StringBuilder(" SELECT TNRN FROM ");
				successor_query.append(dataLibrary).append(".ETHIPS1Z1 WHERE TNR = ").append("'" + partNumber + "'");
				successor_query.append(" AND (CODEF=21 OR CODEF=22 OR CODEF=24) AND TNRN <>  TNR ");

				alternativePartsDetailsList = null;
				alternativePartsDetailsList = dbServiceRepository.getResultUsingQuery(AlternativePartsDetails.class,
						successor_query.toString(), true);

				if (alternativePartsDetailsList != null && alternativePartsDetailsList.size() > 0) {

					AlternativePartsDetails partsDetails = alternativePartsDetailsList.get(0);

					if ((partsDetails.getNewPartNumber() != null && !partsDetails.getNewPartNumber().isEmpty())) {
						isPartHistory = true;
						log.debug("is successor available :"+isPartHistory);
					}
				}

			}

		}
		return isPartHistory;
	}


	/**
	 * This is method is used to get List of Ausgelöste Bestellungen (Triggered Orders) from DB.
	 */
	@Override
	public GlobalSearch getOrdersList(String dataLibrary, String companyId, String agencyId, String allowedWarehouses, String pageSize, String pageNumber,
			String sortingBy, String sortingType, List<String> warehouseNos) {

		log.info("Inside getOrdersList method of OrdersServiceImpl");

		List<OrdersObject> ordersList = new ArrayList<>();
		GlobalSearch globalSearchList = new GlobalSearch();

		try {
			if(pageSize==null || pageNumber==null || pageSize.isEmpty() || pageNumber.isEmpty()){
				pageSize = RestInputConstants.DEFAULT_PAGE_SIZE;
				pageNumber = RestInputConstants.DEFAULT_PAGE_NO;
			}

			int totalRecords = Integer.parseInt(pageSize);
			int nextRows = totalRecords * (Integer.parseInt(pageNumber) - 1);

			//validate the page size
			validatePageSize(totalRecords);
			log.info("PageSize  : {}  OFFSET Size (Skip Rows):  {} Page Number  : {} ", pageSize, nextRows, pageNumber);
			
			//validate the Warehouse Ids
			validateWarehouses(allowedWarehouses);
			
			String orderByClause = getOrderByClause(sortingType, sortingBy, "Ausgeloste Bestellungen");
			
			if(warehouseNos != null && !warehouseNos.isEmpty()){
				warehouseNos.removeIf(s -> s == null || s.trim().isEmpty());
				allowedWarehouses = StringUtils.join(warehouseNos, ",");
			}

			StringBuilder query = new StringBuilder("Select distinct (bsn.BEST_ART|| bsn.BEST_AP) as Bestellnummer, bsn.SERV_AUFNRX, bsn.PSERV_AUFNR, ");
			query.append("bsn.LNR, bsn.HERST, ");
//			query.append("(Select NAME1 from ").append(dataLibrary).append(".e_etstamk6 where key2=bsn.BEST_BENDL ) AS Lieferant, ");
			query.append("bsn.BDAT, bsn.BVER as Hinweis, ");
			query.append("(Select count(*) from ").append(dataLibrary).append(".e_bsndat Where SERV_AUFNRX = bsn.SERV_AUFNRX and PSERV_AUFNR = bsn.PSERV_AUFNR ");
			query.append("and LNR = bsn.LNR  and HERST = bsn.HERST and ");
			query.append("(BEST_ART|| BEST_AP) = (bsn.BEST_ART|| bsn.BEST_AP) ) AS positionen, ");
			
			query.append("case when bsn.PSERV_AUFNR = 0  then ");
			query.append("(Select KDNAM from ").append(dataLibrary).append(".F_AUFKO2 Where FA = bsn.SERV_FAX AND FIL = bsn.SERV_FILX AND AUFNR = bsn.SERV_AUFNRX  AND RECHKZ = 1) ");
			query.append("when bsn.SERV_AUFNRX = 0  then ");
			query.append("(Select KDNAM from ").append(dataLibrary).append(".F_PATKO2 WHERE FA = bsn.PSERV_FA  AND  FIL = bsn.PSERV_FIL  AND  AUFNR = bsn.PSERV_AUFNR AND RECHKZ = 1) ");
			query.append("END AS kunden,");
			query.append("(select count(*) from ");
			query.append("(select  distinct (BEST_ART|| BEST_AP), SERV_AUFNRX, PSERV_AUFNR, LNR, HERST, BDAT, BVER from ").append(dataLibrary).append(".e_bsndat ");
			query.append(" where ((serv_aufnrx <>'000000' and serv_aufnrx <>'') OR (PSERV_AUFNR > 0)) ");			
			query.append(" AND LNR in (").append(allowedWarehouses).append(") )) AS ROWNUMER from ");
			query.append(dataLibrary).append(".e_bsndat bsn Where ((bsn.serv_aufnrx <> '000000' and  bsn.serv_aufnrx <>'') OR (bsn.PSERV_AUFNR > 0)) ");			
			query.append(" AND LNR in (").append(allowedWarehouses).append(") ");
			
			//query.append(" order by Bestellnummer, bsn.SERV_AUFNRX, bsn.PSERV_AUFNR OFFSET "); //comment out for task -  ALPHAX-2297
			query.append(orderByClause).append(" OFFSET ");
			query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");

			List<OrdersBasketDetailsDTO> ordersDTOList = new ArrayList<>();
			ordersList = dbServiceRepository.getResultUsingQuery(OrdersObject.class, query.toString(), true);

			//if the list is not empty
			if (ordersList != null && !ordersList.isEmpty()) {
				
				ordersDTOList = convertOrdersEntityToDTO(ordersList, ordersDTOList, dataLibrary);

				globalSearchList.setSearchDetailsList(ordersDTOList);
				globalSearchList.setTotalPages(Integer.toString(ordersList.get(0).getTotalCount()));
				globalSearchList.setTotalRecordCnt(Integer.toString(ordersList.get(0).getTotalCount()));
			}
			else {
				globalSearchList.setSearchDetailsList(ordersDTOList);
				globalSearchList.setTotalPages(Integer.toString(0));
				globalSearchList.setTotalRecordCnt(Integer.toString(0));
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Ausgelöste Bestellungen (Orders)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Ausgelöste Bestellungen (Orders)"), exception);
			throw exception;
		}

		return globalSearchList;
	}
	
	
	/**
	 * This method is used to convert Orders Entity to Orders DTO Format.
	 * @param ordersList
	 * @return ordersDTOList
	 */
	private List<OrdersBasketDetailsDTO> convertOrdersEntityToDTO(List<OrdersObject> ordersList, List<OrdersBasketDetailsDTO> ordersDTOList, String dataLibrary) {

		for (OrdersObject orders : ordersList) {
			OrdersBasketDetailsDTO ordersDTO = new OrdersBasketDetailsDTO();

			ordersDTO.setWarehouseNumber(orders.getWarehouseNumber());
			if(orders.getManufacturer()!= null && orders.getManufacturer().equalsIgnoreCase(RestInputConstants.DCAG_STRING)) {
				ordersDTO.setManufacturer(RestInputConstants.DAG_STRING);
			}
			else {
				ordersDTO.setManufacturer(orders.getManufacturer());
			}
			
			if(orders.getAserv_Aufnr()!= null && !orders.getAserv_Aufnr().equals("000000")) {
				ordersDTO.setOrderNumber(StringUtils.leftPad(orders.getAserv_Aufnr(), 6, "0"));
				ordersDTO.setIsAserv(true);
			}
			else {
				ordersDTO.setOrderNumber(StringUtils.leftPad(orders.getPserv_Aufnr(), 6, "0"));
				ordersDTO.setIsAserv(false);
			}

			ordersDTO.setOrderPosition(orders.getOrderPosition());
			ordersDTO.setOrderDate(convertDateToString(orders.getOrderDate()));
			ordersDTO.setSupplierNo(orders.getSupplierNo());
			ordersDTO.setSupplierName(orders.getSupplierName());
			
			ordersDTO.setCustomerName("");
			if(orders.getCustomerName()!=null && !orders.getCustomerName().isEmpty()) {
				ordersDTO.setCustomerName(orders.getCustomerName().replaceAll("\\*", " "));
			}
			ordersDTO.setNewOrderNo(StringUtils.leftPad(orders.getNewOrderNo(), 6, "0"));
			ordersDTO.setOrderNote(orders.getOrderNote());
			
			ordersDTO.setOrderStatus(getCustomOrderStatus(orders, dataLibrary, ordersDTO));
			
			ordersDTO.setDeliveryNoteNo("");
			ordersDTO.setTotalPartsDelivered("");
			ordersDTO.setPartPosition("");

			ordersDTOList.add(ordersDTO);
		}
		//return the orderDTO List
		return ordersDTOList;
	}
	
	
	/**
	 * This method is used to calculate the status of individual orders using different conditions.
	 * @param orders
	 * @param dataLibrary
	 * @return
	 */
	private String getCustomOrderStatus(OrdersObject orders, String dataLibrary, OrdersBasketDetailsDTO ordersDTO ) {
		String orderStatus = "";
		Integer type1 = 0;
		Integer type2 = 0;
		Integer type2_1 = 0;
		Integer type2_2 = 0;
		Integer type3 = 0;
		Integer type4 = 0;
		try {

			StringBuilder query = new StringBuilder("Select (bsn.BEST_ART|| bsn.BEST_AP) as Bestellnummer, bsn.DISPOPOS, bsn.STATUS, ");
			query.append("bsn.BEMENG, bsn.GLMENG, bsn.OMENG, bsn.ZUMENG, BEST_BENDL from ");
			query.append(dataLibrary).append(".e_bsndat bsn Where bsn.LNR =").append(orders.getWarehouseNumber()).append(" and  bsn.HERST='").append(orders.getManufacturer());
			query.append("' and  (bsn.BEST_ART|| bsn.BEST_AP) = ").append(orders.getNewOrderNo());
			query.append(" ORDER BY bsn.PSRV_POSNR, bsn.SERV_POSNRX");

			List<OrdersObjectDetails> ordersDetailList = dbServiceRepository.getResultUsingQuery(OrdersObjectDetails.class, query.toString(), true);

			//if the list is not empty
			if (ordersDetailList != null && !ordersDetailList.isEmpty()) {

				ordersDTO.setSupplierNo(ordersDetailList.get(0).getSupplierNo());

				for (OrdersObjectDetails orderDet : ordersDetailList) {

					//					TotalPartsNumber()     // BEMENG
					//					TotalPartsDelivered()  // GLMENG
					//					RemainingAmount() 	   // OMENG
					//					DeliveredQuantity()    // ZUMENG

					//					Double goMeng = Double.parseDouble(orderDet.getTotalPartsDelivered()) + Double.parseDouble(orderDet.getRemainingAmount());

					//type - 1
					if(orderDet.getOrderStatus().equalsIgnoreCase("50") && Double.parseDouble(orderDet.getRemainingAmount()) > 0
							&& Double.parseDouble(orderDet.getTotalPartsDelivered()) == 0.0) {

						//this means None of the item are delivered for this part....STATUS 50 and OMENG > 0 and GLMENG = 0
						type1 += 1; 
					}
					//type - 2
					else if((orderDet.getOrderStatus().equalsIgnoreCase("55") || orderDet.getOrderStatus().equalsIgnoreCase("56")) 
							&& Double.parseDouble(orderDet.getRemainingAmount()) > 0 
							&& Double.parseDouble(orderDet.getDeliveredQuantity()) > 0
							&& Double.parseDouble(orderDet.getTotalPartsDelivered()) == 0.0) {

						//Some parts are already delivered but some are still not delivered.... STATUS 55 or 56 and OMENG > 0 and ZUMENG > 0  and GLMENG = 0
						type2 += 1; 
					}
					//type - 2.1
					else if((orderDet.getOrderStatus().equalsIgnoreCase("55") || orderDet.getOrderStatus().equalsIgnoreCase("56")) 
							&& Double.parseDouble(orderDet.getRemainingAmount()) > 0 
							&& Double.parseDouble(orderDet.getDeliveredQuantity()) > 0
							&& Double.parseDouble(orderDet.getTotalPartsDelivered()) > 0) {

						//Some parts are already delivered but some are still not delivered.... STATUS 55 or 56 and OMENG > 0 and ZUMENG > 0  and GLMENG > 0
						type2_1 += 1; 
					}
					//type - 2.2
					else if((orderDet.getOrderStatus().equalsIgnoreCase("55") || orderDet.getOrderStatus().equalsIgnoreCase("56") || orderDet.getOrderStatus().equalsIgnoreCase("50")) 
							&& Double.parseDouble(orderDet.getRemainingAmount()) > 0 
							&& Double.parseDouble(orderDet.getTotalPartsDelivered()) > 0) {

						//Some parts are already delivered but some are still not delivered....  : STATUS 55 or 56 or 50 and OMENG > 0.0 and GLMENG > 0.0 
						type2_2 += 1; 
					}
					//type - 3
					else if(orderDet.getOrderStatus().equalsIgnoreCase("90") 
							&& Double.parseDouble(orderDet.getRemainingAmount()) == 0.0) {

						//this means all items are delivered for this part....   : STATUS 90 and OMENG=0
						type3 += 1; 
					}
					//type - 4
					else if(orderDet.getOrderStatus().equalsIgnoreCase("41")) {

						//this means .e36 file could not be created for this part ....   : STATUS 41
						type4 += 1; 
					}

				}

				if(type1 > 0 && type2.equals(0) && type2_1.equals(0) && type2_2.equals(0) && type3.equals(0) && type4.equals(0)  ) {
					//all items delivered
					orderStatus = "Ausgelöst";
				}
				else if(type2 > 0 || type2_1 > 0 || type2_2 > 0 || (type1 > 0 && type3 > 0) && type4.equals(0) ) {
					//partly delivered
					orderStatus = "Teilweise geliefert";
				}
				else if(type3 > 0 && type1.equals(0) && type2.equals(0) && type2_1.equals(0) && type2_2.equals(0) && type4.equals(0)) {
					//all items delivered
					orderStatus = "Vollständig geliefert";
				}
				else if(type4 > 0 && (type2 >= 0 || type2_1 >= 0 || type2_2 >= 0 || type1 >= 0 || type3 >= 0) ) {
					//.e36 file could not be created
					orderStatus = "Dateierstellung fehlgeschlagen";
				}

			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Ausgelöste Bestellungen Status Detail"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Ausgelöste Bestellungen Status Detail"), exception);
			throw exception;
		}

		return orderStatus;

	}
	
	
	/**
	 * This is method is used to get Detail List of Ausgelöste Bestellungen (Order Details) based on order number from DB.
	 */
	@Override
	public List<OrdersBasketDetailsDTO> getOrderDetailsList(String newOrderNumber, String dataLibrary, String companyId, String agencyId, String manufacturer, 
			String warehouseNo, Boolean isAserv, String supplierNo, String orderNumber) {

		log.info("Inside getOrderDetailsList method of OrdersServiceImpl");

		List<OrdersBasketDetailsDTO> orderBasketDetailsDTOList = new ArrayList<>();

		try {
			manufacturer = manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING: manufacturer;			

			StringBuilder query = new StringBuilder("Select (bsn.BEST_ART|| bsn.BEST_AP) as Bestellnummer, bsn.LNR, bsn.HERST, ");
			query.append("bsn.BEST_BENDL, bsn.MARKE, bsn.ETNR, bsn.BEN, bsn.BEMENG, bsn.PSRV_POSNR, bsn.SERV_POSNRX, bsn.DISPOPOS, bsn.DISPOUP, ");
			query.append("(Select NAME1 from ").append(dataLibrary).append(".e_etstamk6 where KEY2 = bsn.BEST_BENDL ) AS Lieferant, ");
			query.append(" bsn.LIEFNR_GESAMT, bsn.STATUS, bsn.GLMENG, bsn.OMENG, bsn.ZUMENG from ");
			query.append(dataLibrary).append(".e_bsndat bsn Where bsn.LNR =").append(warehouseNo).append(" and  bsn.HERST='").append(manufacturer);
			query.append("' and  (bsn.BEST_ART|| bsn.BEST_AP) = ").append(newOrderNumber);
			
			query.append( (isAserv) ? " and bsn.SERV_AUFNRX =" : " and bsn.PSERV_AUFNR =" );

			query.append(orderNumber).append(" ORDER BY bsn.PSRV_POSNR, bsn.SERV_POSNRX, bsn.BEST_BENDL, bsn.ETNR");

			List<OrdersObjectDetails> ordersDetailList = dbServiceRepository.getResultUsingQuery(OrdersObjectDetails.class, query.toString(), true);

			//if the list is not empty
			if (ordersDetailList != null && !ordersDetailList.isEmpty()) {

				orderBasketDetailsDTOList = convertOrderDetailsEntityToDTO(ordersDetailList, orderBasketDetailsDTOList, orderNumber, isAserv);
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Ausgelöste Bestellungen Detail (Order Details)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Ausgelöste Bestellungen Detail (Order Details)"), exception);
			throw exception;
		}

		return orderBasketDetailsDTOList;
	}


	/**
	 * This method is used to convert Orders Detail Entity to OrdersBasketDetail DTO Format.
	 * @param orderDetailList
	 * @param orderNumber 
	 * @param isAserv 
	 * @return ordersBasketDetailDTOList
	 */
	private List<OrdersBasketDetailsDTO> convertOrderDetailsEntityToDTO(List<OrdersObjectDetails> orderBasketDetailList, List<OrdersBasketDetailsDTO> ordersBasketDetailDTOList, 
			String orderNumber, Boolean isAserv) {

		for (OrdersObjectDetails orderDet : orderBasketDetailList) {
			OrdersBasketDetailsDTO ordersDetailsDTO = new OrdersBasketDetailsDTO();

			ordersDetailsDTO.setSupplierName(orderDet.getSupplierName());
			ordersDetailsDTO.setSupplierNo(orderDet.getSupplierNo());
			ordersDetailsDTO.setPart_Brand(orderDet.getPart_Brand());

			ordersDetailsDTO.setPartNumber(orderDet.getPartNumber());
			ordersDetailsDTO.setPartDescription(orderDet.getPartDescription());

			ordersDetailsDTO.setWarehouseNumber(orderDet.getWarehouseNumber());
			ordersDetailsDTO.setTotalPartsNumber(orderDet.getTotalPartsNumber());
			ordersDetailsDTO.setOrderNumber(orderNumber);
			ordersDetailsDTO.setIsAserv(isAserv);
			ordersDetailsDTO.setNewOrderNo(StringUtils.leftPad(orderDet.getNewOrderNo(), 6, "0"));

			if(isAserv) {
				ordersDetailsDTO.setOrderPositionSerialNo(orderDet.getOrderPosition_ASRV().toString());
			}
			else {
				ordersDetailsDTO.setOrderPositionSerialNo(orderDet.getOrderPosition_PSRV().toString());
			}

			if(orderDet.getManufacturer()!= null && orderDet.getManufacturer().equalsIgnoreCase(RestInputConstants.DCAG_STRING)) {
				ordersDetailsDTO.setManufacturer(RestInputConstants.DAG_STRING);
			}
			else {
				ordersDetailsDTO.setManufacturer(orderDet.getManufacturer());
			}
			
			ordersDetailsDTO.setDeliveryNoteNo(orderDet.getDeliveryNoteNo() !=null ? orderDet.getDeliveryNoteNo() : "");
			ordersDetailsDTO.setTotalPartsDelivered(orderDet.getTotalPartsDelivered() !=null ? orderDet.getTotalPartsDelivered() : "");
			ordersDetailsDTO.setPartPosition(orderDet.getPartPosition());
			ordersDetailsDTO.setPartPositionUP(orderDet.getPartPositionUP());
			
			ordersDetailsDTO.setOrderStatus(orderDet.getOrderStatus());
			ordersDetailsDTO.setRemainingAmount(orderDet.getRemainingAmount());
			ordersDetailsDTO.setDeliveredQuantity(orderDet.getDeliveredQuantity() !=null ? orderDet.getDeliveredQuantity() : "");
			ordersDetailsDTO.setPartStatus("");
			

			if(orderDet.getOrderStatus().equalsIgnoreCase("50") && Double.parseDouble(orderDet.getRemainingAmount()) > 0
					&& Double.parseDouble(orderDet.getTotalPartsDelivered()) == 0.0) {
				
				//this means None of the item are delivered for this part....  // STATUS 50 and OMENG > 0 and GLMENG = 0
				ordersDetailsDTO.setPartStatus("Noch keine Lieferung");
				ordersDetailsDTO.setPartStatusType("Type1");
			}
			else if((orderDet.getOrderStatus().equalsIgnoreCase("55") || orderDet.getOrderStatus().equalsIgnoreCase("56")) 
					&& Double.parseDouble(orderDet.getRemainingAmount()) > 0 
					&& Double.parseDouble(orderDet.getDeliveredQuantity()) > 0
					&& Double.parseDouble(orderDet.getTotalPartsDelivered()) == 0.0) {
				
				//partially delivered .............. // STATUS 55 or 56 and OMENG > 0 and ZUMENG > 0  and GLMENG = 0
				ordersDetailsDTO.setPartStatus("Teilweise geliefert");
				ordersDetailsDTO.setPartStatusType("Type2");
			}
			else if((orderDet.getOrderStatus().equalsIgnoreCase("55") || orderDet.getOrderStatus().equalsIgnoreCase("56")) 
					&& Double.parseDouble(orderDet.getRemainingAmount()) > 0 
					&& Double.parseDouble(orderDet.getDeliveredQuantity()) > 0
					&& Double.parseDouble(orderDet.getTotalPartsDelivered()) > 0) {
				
				//partially delivered .............. // STATUS 55 or 56 and OMENG > 0 and ZUMENG > 0  and GLMENG > 0
				ordersDetailsDTO.setPartStatus("Teilweise geliefert");
				ordersDetailsDTO.setPartStatusType("Type2_1");
			}
			else if((orderDet.getOrderStatus().equalsIgnoreCase("55") || orderDet.getOrderStatus().equalsIgnoreCase("56") || orderDet.getOrderStatus().equalsIgnoreCase("50")) 
					&& Double.parseDouble(orderDet.getRemainingAmount()) > 0 
					&& Double.parseDouble(orderDet.getTotalPartsDelivered()) > 0) {
				
				//partially delivered ............. // STATUS 55 or 56 or 50 and OMENG > 0.0 and GLMENG > 0.0
				ordersDetailsDTO.setPartStatus("Teilweise geliefert");
				ordersDetailsDTO.setPartStatusType("Type2_2");
			}
			else if(orderDet.getOrderStatus().equalsIgnoreCase("90") 
					&& Double.parseDouble(orderDet.getRemainingAmount()) == 0.0) {
				
				//all items delivered ......... // STATUS 90 and OMENG=0
				ordersDetailsDTO.setPartStatus("Lieferung Vollständig");
				ordersDetailsDTO.setPartStatusType("Type3");
			}
			else if(orderDet.getOrderStatus().equalsIgnoreCase("41")) {
				
				//creation of the .e36 file failed ......... // STATUS 41 
				ordersDetailsDTO.setPartStatus("Dateierstellung fehlgeschlagen");
				ordersDetailsDTO.setPartStatusType("Type4");
			}

			ordersBasketDetailDTOList.add(ordersDetailsDTO);
		} 
		//return the order Detail List
		return ordersBasketDetailDTOList;
	}

	
	/**
	 * This is method is used to get List of Ausgelöste Bestellungen (Triggered Orders) Based On Filter from DB.
	 */
	@Override
	public GlobalSearch getOrdersListBasedOnFilter(String dataLibrary, String companyId, String agencyId, String allowedWarehouses, 
			String pageSize, String pageNumber, String searchText, String searchBy, String sortingBy, String sortingType, List<String> warehouseNos) {

		log.info("Inside getOrdersListBasedOnFilter method of OrdersServiceImpl");

		List<OrdersObject> ordersList = new ArrayList<>();
		GlobalSearch globalSearchList = new GlobalSearch();
		StringBuilder query = new StringBuilder();
		try {
			if(pageSize==null || pageNumber==null || pageSize.isEmpty() || pageNumber.isEmpty()){
				pageSize = RestInputConstants.DEFAULT_PAGE_SIZE;
				pageNumber = RestInputConstants.DEFAULT_PAGE_NO;
			}

			int totalRecords = Integer.parseInt(pageSize);
			int nextRows = totalRecords * (Integer.parseInt(pageNumber) - 1);

			//validate the page size
			validatePageSize(totalRecords);
			log.info("PageSize  : {}  OFFSET Size (Skip Rows):  {} Page Number  : {} ", pageSize, nextRows, pageNumber);
			
			//validate the Warehouse Ids
			validateWarehouses(allowedWarehouses);
			
			String orderByClause = getOrderByClause(sortingType, sortingBy, "Ausgeloste Bestellungen");
			
			if(warehouseNos != null && !warehouseNos.isEmpty()){
				warehouseNos.removeIf(s -> s == null || s.trim().isEmpty());
				allowedWarehouses = StringUtils.join(warehouseNos, ",");
			}

			if(searchBy.equalsIgnoreCase("Auftrag")){

				query.append("Select distinct (bsn.BEST_ART|| bsn.BEST_AP) as Bestellnummer, bsn.SERV_AUFNRX, bsn.PSERV_AUFNR, ");
				query.append("bsn.LNR, bsn.HERST, ");
				query.append("bsn.BDAT, bsn.BVER as Hinweis, ");
				query.append("(Select count(*) from ").append(dataLibrary).append(".e_bsndat Where SERV_AUFNRX = bsn.SERV_AUFNRX and PSERV_AUFNR = bsn.PSERV_AUFNR ");
				query.append("and LNR = bsn.LNR  and HERST = bsn.HERST and ");
				query.append("(BEST_ART|| BEST_AP) = (bsn.BEST_ART|| bsn.BEST_AP) ) AS positionen, ");
				
				query.append("case when bsn.PSERV_AUFNR = 0  then ");
				query.append("(Select KDNAM FROM ").append(dataLibrary).append(".F_AUFKO2 Where FA = bsn.SERV_FAX AND FIL = bsn.SERV_FILX AND AUFNR = bsn.SERV_AUFNRX  AND RECHKZ = 1) ");
				query.append("when bsn.SERV_AUFNRX = 0  then ");
				query.append("(Select KDNAM FROM ").append(dataLibrary).append(".F_PATKO2 WHERE FA = bsn.PSERV_FA  AND  FIL = bsn.PSERV_FIL  AND  AUFNR = bsn.PSERV_AUFNR AND RECHKZ = 1) ");
				query.append("END AS kunden,");
				query.append("(select count(*) from ");
				query.append("(select  distinct (BEST_ART|| BEST_AP), SERV_AUFNRX, PSERV_AUFNR, LNR, HERST, BDAT, BVER from ").append(dataLibrary).append(".e_bsndat ");
				query.append(" where serv_aufnrx LIKE '%"+searchText+"%' OR CONCAT('0',CAST(PSERV_AUFNR as CHARACTER(10))) LIKE '%"+searchText+"%' ");
				query.append(" AND LNR in (").append(allowedWarehouses).append(") )) AS ROWNUMER from ");
				query.append(dataLibrary).append(".e_bsndat bsn Where serv_aufnrx LIKE '%"+searchText+"%' OR  CONCAT('0',CAST(PSERV_AUFNR as CHARACTER(10))) LIKE '%"+searchText+"%' ");
				query.append(" AND bsn.LNR in (").append(allowedWarehouses).append(") ");
				query.append(orderByClause).append(" OFFSET ");
				query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");
				
			}
/*			else if(searchBy.equalsIgnoreCase("Lager")){
				
				query.append("Select distinct (bsn.BEST_ART|| bsn.BEST_AP) as Bestellnummer, bsn.SERV_AUFNRX, bsn.PSERV_AUFNR, ");
				query.append("bsn.LNR, bsn.HERST, ");
//				query.append("(Select NAME1 from ").append(dataLibrary).append(".e_etstamk6 where key2=bsn.BEST_BENDL ) AS Lieferant, ");
				query.append("bsn.BDAT, bsn.BVER as Hinweis, ");
				query.append("(Select count(*) from ").append(dataLibrary).append(".e_bsndat Where SERV_AUFNRX = bsn.SERV_AUFNRX and PSERV_AUFNR = bsn.PSERV_AUFNR ");
				query.append("and LNR = bsn.LNR  and HERST = bsn.HERST and ");
				query.append("(BEST_ART|| BEST_AP) = (bsn.BEST_ART|| bsn.BEST_AP) ) AS positionen, ");
				
				query.append("case when bsn.PSERV_AUFNR = 0  then ");
				query.append("(Select SUBSTR(KDADRESSE, 29, 30) from ").append(dataLibrary).append(".F_AUFKO2 Where FA = bsn.SERV_FAX AND FIL = bsn.SERV_FILX AND AUFNR = bsn.SERV_AUFNRX  AND RECHKZ = 1) ");
				query.append("when bsn.SERV_AUFNRX = 0  then ");
				query.append("(Select SUBSTR(KDADRESSE, 29, 30) from ").append(dataLibrary).append(".F_PATKO2 WHERE FA = bsn.PSERV_FA  AND  FIL = bsn.PSERV_FIL  AND  AUFNR = bsn.PSERV_AUFNR AND RECHKZ = 1) ");
				query.append("END AS kunden,");
				query.append("(select count(*) from ");
				query.append("(select  distinct (BEST_ART|| BEST_AP), SERV_AUFNRX, PSERV_AUFNR, LNR, HERST, BDAT, BVER from ").append(dataLibrary).append(".e_bsndat ");
				query.append(" where ((serv_aufnrx <>'000000' and serv_aufnrx <>'') OR (PSERV_AUFNR > 0) ) and LNR = "+searchText+" ");
				query.append(" and LNR in (").append(allowedWarehouses).append(") )) AS ROWNUMER from ");
				query.append(dataLibrary).append(".e_bsndat bsn Where ((bsn.serv_aufnrx <> '000000' and  bsn.serv_aufnrx <>'') OR (bsn.PSERV_AUFNR > 0)) and LNR = "+searchText+" ");
				query.append(" and bsn.LNR in (").append(allowedWarehouses).append(") ");
				query.append(orderByClause).append(" OFFSET ");
				query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");
				
			}*/
			else if(searchBy.equalsIgnoreCase("Kunde")){
				
				if (searchText != null && searchText.trim().length() > 0) {

					int spaceCount = StringUtils.countMatches(searchText.trim(), " ");
					if (spaceCount > 0) {
						searchText = searchText.trim().replaceFirst(" ", "*").trim();
					} else {
						searchText = searchText.trim();
					}
				}
				
				query.append("( Select distinct (bsn.BEST_ART|| bsn.BEST_AP) as Bestellnummer, bsn.SERV_AUFNRX, bsn.PSERV_AUFNR, ");
				query.append("bsn.LNR, bsn.HERST, ");
				query.append("bsn.BDAT, bsn.BVER as Hinweis, auf0.KDNAM as kunden, ");
				query.append("(Select count(*) from ").append(dataLibrary).append(".e_bsndat Where SERV_AUFNRX = bsn.SERV_AUFNRX and PSERV_AUFNR = bsn.PSERV_AUFNR ");
				query.append("and LNR = bsn.LNR  and HERST = bsn.HERST and ");
				query.append("(BEST_ART|| BEST_AP) = (bsn.BEST_ART|| bsn.BEST_AP) ) AS positionen, ");
				query.append("(select count(*) from ");
				query.append("(select  distinct (BEST_ART|| BEST_AP), SERV_AUFNRX, PSERV_AUFNR, LNR, HERST, BDAT, BVER from ").append(dataLibrary).append(".e_bsndat bsn ");
				query.append(" LEFT JOIN ").append(dataLibrary).append(".F_PATKO2 auf0 ON auf0.FA = bsn.PSERV_FA AND auf0.FIL = bsn.PSERV_FIL AND auf0.AUFNR = bsn.PSERV_AUFNR ");
				query.append(" where ((bsn.serv_aufnrx <> '000000' and  bsn.serv_aufnrx <>'') OR (bsn.PSERV_AUFNR > 0)) and auf0.RECHKZ = 1 and UPPER(trim(auf0.KDNAM)) LIKE ").append("UPPER('%"+searchText+"%') ");
				query.append(" and bsn.LNR in (").append(allowedWarehouses).append(") )) AS ROWNUMER from ");
				query.append(dataLibrary).append(".e_bsndat bsn LEFT JOIN ").append(dataLibrary).append(".F_PATKO2 auf0 ON auf0.FA = bsn.PSERV_FA AND auf0.FIL = bsn.PSERV_FIL AND auf0.AUFNR = bsn.PSERV_AUFNR ");
				query.append(" Where ((bsn.serv_aufnrx <> '000000' and  bsn.serv_aufnrx <>'') OR (bsn.PSERV_AUFNR > 0)) and auf0.RECHKZ = 1 and UPPER(trim(auf0.KDNAM)) LIKE ").append("UPPER('%"+searchText+"%') ");
				query.append(" and bsn.LNR in (").append(allowedWarehouses).append(") ");
				query.append(orderByClause).append(" OFFSET ");
				query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY )");
				
				query.append("UNION ");
				
				query.append("( Select distinct (bsn.BEST_ART|| bsn.BEST_AP) as Bestellnummer, bsn.SERV_AUFNRX, bsn.PSERV_AUFNR, ");
				query.append("bsn.LNR, bsn.HERST, ");
				query.append("bsn.BDAT, bsn.BVER as Hinweis, auf0.KDNAM as kunden, ");
				query.append("(Select count(*) from ").append(dataLibrary).append(".e_bsndat Where SERV_AUFNRX = bsn.SERV_AUFNRX and PSERV_AUFNR = bsn.PSERV_AUFNR ");
				query.append("and LNR = bsn.LNR  and HERST = bsn.HERST and ");
				query.append("(BEST_ART|| BEST_AP) = (bsn.BEST_ART|| bsn.BEST_AP) ) AS positionen, ");
			
				query.append("(select count(*) from ");
				query.append("(select  distinct (BEST_ART|| BEST_AP), SERV_AUFNRX, PSERV_AUFNR, LNR, HERST, BDAT, BVER from ").append(dataLibrary).append(".e_bsndat bsn ");
				query.append(" LEFT JOIN ").append(dataLibrary).append(".F_AUFKO2 auf0 ON auf0.FA = bsn.SERV_FAX AND auf0.FIL = bsn.SERV_FILX AND auf0.AUFNR = bsn.SERV_AUFNRX ");
				query.append(" where((bsn.serv_aufnrx <> '000000' and  bsn.serv_aufnrx <>'') OR (bsn.PSERV_AUFNR > 0)) and auf0.RECHKZ = 1 and UPPER(trim(auf0.KDNAM)) LIKE ").append("UPPER('%"+searchText+"%') ");
				query.append(" and bsn.LNR in (").append(allowedWarehouses).append(") )) AS ROWNUMER from ");
				query.append(dataLibrary).append(".e_bsndat bsn LEFT JOIN ").append(dataLibrary).append(".F_AUFKO2 auf0 ON auf0.FA = bsn.SERV_FAX AND auf0.FIL = bsn.SERV_FILX AND auf0.AUFNR = bsn.SERV_AUFNRX ");
				query.append(" Where ((bsn.serv_aufnrx <> '000000' and  bsn.serv_aufnrx <>'') OR (bsn.PSERV_AUFNR > 0)) and auf0.RECHKZ = 1 and UPPER(trim(auf0.KDNAM)) LIKE ").append("UPPER('%"+searchText+"%') ");
				query.append(" and bsn.LNR in (").append(allowedWarehouses).append(") )");
				query.append(orderByClause).append(" OFFSET ");
				query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY");
				
			}else if(searchBy.equalsIgnoreCase("Bestellnummer")){
				
				String order_firstPart = "";
				String order_secondPart = "";
				
				if (searchText != null && searchText.length() == 6) {
					order_firstPart = searchText.substring(0, 2);
					order_secondPart = searchText.substring(2, 6);
				} else {
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.INVALID_MSG_KEY,"Bestellnummer"));
					log.error(messageService.getReadableMessage(ExceptionMessages.INVALID_MSG_KEY,"Bestellnummer"));
					throw exception;
				}
				
				query.append("Select distinct (bsn.BEST_ART|| bsn.BEST_AP) as Bestellnummer, bsn.SERV_AUFNRX, bsn.PSERV_AUFNR, ");
				query.append("bsn.LNR, bsn.HERST, ");
				query.append("bsn.BDAT, bsn.BVER as Hinweis, ");
				query.append("(Select count(*) from ").append(dataLibrary).append(".e_bsndat Where SERV_AUFNRX = bsn.SERV_AUFNRX and PSERV_AUFNR = bsn.PSERV_AUFNR ");
				query.append("and LNR = bsn.LNR  and HERST = bsn.HERST and ");
				query.append("(BEST_ART|| BEST_AP) = (bsn.BEST_ART|| bsn.BEST_AP) ) AS positionen, ");
				
				query.append("case when bsn.PSERV_AUFNR = 0  then ");
				query.append("(Select KDNAM from ").append(dataLibrary).append(".F_AUFKO2 Where FA = bsn.SERV_FAX AND FIL = bsn.SERV_FILX AND AUFNR = bsn.SERV_AUFNRX  AND RECHKZ = 1) ");
				query.append("when bsn.SERV_AUFNRX = 0  then ");
				query.append("(Select KDNAM from ").append(dataLibrary).append(".F_PATKO2 WHERE FA = bsn.PSERV_FA  AND  FIL = bsn.PSERV_FIL  AND  AUFNR = bsn.PSERV_AUFNR AND RECHKZ = 1) ");
				query.append("END AS kunden,");
				query.append("(select count(*) from ");
				query.append("(select  distinct (BEST_ART|| BEST_AP), SERV_AUFNRX, PSERV_AUFNR, LNR, HERST, BDAT, BVER from ").append(dataLibrary).append(".e_bsndat ");
				query.append(" where ((serv_aufnrx <>'000000' and serv_aufnrx <>'') OR (PSERV_AUFNR > 0)) AND (BEST_ART =  "+order_firstPart+"  AND BEST_AP = "+order_secondPart+") ");
				query.append(" AND LNR in (").append(allowedWarehouses).append(") )) AS ROWNUMER from ");
				query.append(dataLibrary).append(".e_bsndat bsn Where ((bsn.serv_aufnrx <> '000000' and  bsn.serv_aufnrx <>'') OR (bsn.PSERV_AUFNR > 0) ) and (BEST_ART = "+order_firstPart+" AND BEST_AP = "+order_secondPart+") ");
				query.append(" AND bsn.LNR in (").append(allowedWarehouses).append(") ");
				query.append(orderByClause).append(" OFFSET ");
				query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");
				
			}else{
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.SEARCH_BY_FILTER_INVALID_MSG_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.SEARCH_BY_FILTER_INVALID_MSG_KEY));
				throw exception;
			}
			
			List<OrdersBasketDetailsDTO> ordersDTOList = new ArrayList<>();
			ordersList = dbServiceRepository.getResultUsingQuery(OrdersObject.class, query.toString(), true);

			//if the list is not empty
			if (ordersList != null && !ordersList.isEmpty()) {

				ordersDTOList = convertOrdersEntityToDTO(ordersList, ordersDTOList, dataLibrary);

				globalSearchList.setSearchDetailsList(ordersDTOList);
				globalSearchList.setTotalPages(Integer.toString(ordersList.get(0).getTotalCount()));
				globalSearchList.setTotalRecordCnt(Integer.toString(ordersList.get(0).getTotalCount()));
			}
			else {
				globalSearchList.setSearchDetailsList(ordersDTOList);
				globalSearchList.setTotalPages(Integer.toString(0));
				globalSearchList.setTotalRecordCnt(Integer.toString(0));
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Ausgelöste Bestellungen (Orders based on filter)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Ausgelöste Bestellungen (Orders based on filter) "), exception);
			throw exception;
		}

		return globalSearchList;
	}
	

	/**
	 * This method is used to get the filter value for Ausgelöste Bestellungen (Triggered Orders)  from Stub
	 * @return List
	 */
	@Override
	public List<DropdownObject> getFilterValuesForOrder() {

		log.info("Inside getFilterValuesForOrder method of OrdersServiceImpl");

		List<DropdownObject> filterValuesForOrder = new ArrayList<>();
		filterValuesForOrder = stubServiceRepository.getDDLValuesFromStub(StubServiceRepository.filterValuesForOrderMap);
		return filterValuesForOrder;
	}
	
	
	/**
	 * This method is used to transfer Ausgelöste Bestellungen Orders to ET Warehouse using COBOL Program.
	 */
	@Override
	public Map<String, String> transferOrderstoET( OrdersBasketDetailsDTO orderbasketDetails, String dataLibrary, 
			String companyId,	String agencyId ) {
		log.info("Inside transferOrderstoET method of OrdersServiceImpl");

		ProgramParameter[] parmList = new ProgramParameter[16];
		Map<String, String> programOutput = new HashMap<String, String>();
		
		try{
			
			companyId = StringUtils.leftPad(companyId, 2, "0");
			agencyId = StringUtils.leftPad(agencyId, 2, "0");
			
			if(!orderbasketDetails.getDeliveryNoteNo().trim().isEmpty() && !orderbasketDetails.getDeliveredQuantity().trim().isEmpty()) {

				// Create the input parameter 
				String funktionName =  "ZUGBSN";
				
				String returnCode = StringUtils.rightPad("", 5, " ");
				parmList[0] = new ProgramParameter(returnCode.getBytes(Program_Commands_Constants.IBM_273), 2);
				
				String returnMsg  = StringUtils.rightPad("", 100, " ");
				parmList[1] = new ProgramParameter(returnMsg.getBytes(Program_Commands_Constants.IBM_273), 4);
				
				parmList[2] = new ProgramParameter(funktionName.getBytes(Program_Commands_Constants.IBM_273), 2);
				
				String companyAndAgency = StringUtils.rightPad(companyId+agencyId, 4, " ");
				parmList[3] = new ProgramParameter(companyAndAgency.getBytes(Program_Commands_Constants.IBM_273),4);
				
				/* ---- This change is for filter and warehouse selection on Incoming goods Screen ---*/
				String inLFS = StringUtils.rightPad("", 12, "%");
				String inLIF = StringUtils.rightPad("", 7, "%");
				parmList[4] = new ProgramParameter(inLFS.getBytes(Program_Commands_Constants.IBM_273), 4);
				
				//This extra parameter add for PIV filter used in Incoming good
				parmList[5] = new ProgramParameter(inLFS.getBytes(Program_Commands_Constants.IBM_273), 4);
				
				//This extra parameter add for Liferant(Supplier) filter used in Incoming good
				parmList[6] = new ProgramParameter(inLIF.getBytes(Program_Commands_Constants.IBM_273), 4);
				
				String inWarehouses = StringUtils.rightPad("", 300, "0");
				parmList[7] = new ProgramParameter(inWarehouses.getBytes(Program_Commands_Constants.IBM_273), 4);
				/* -------------- */
								
				String orderNumber = StringUtils.leftPad(orderbasketDetails.getNewOrderNo(), 5, "0");
				parmList[8] = new ProgramParameter(orderNumber.getBytes(Program_Commands_Constants.IBM_273), 2);
				
				String totalParts = StringUtils.leftPad(orderbasketDetails.getDeliveredQuantity(), 9, "0");
				parmList[9] = new ProgramParameter(totalParts.getBytes(Program_Commands_Constants.IBM_273), 2);
				
				String partnumber = StringUtils.rightPad(orderbasketDetails.getPartNumber(), 19, " ");
				parmList[10] = new ProgramParameter(partnumber.getBytes(Program_Commands_Constants.IBM_273), 2);
				
				String partPositionPARM = StringUtils.join(StringUtils.leftPad(orderbasketDetails.getPartPosition(), 4, "0"), StringUtils.leftPad(orderbasketDetails.getPartPositionUP(), 2, "0"));
				parmList[11] = new ProgramParameter(partPositionPARM.getBytes(Program_Commands_Constants.IBM_273), 2);
				
				String manufacturer = StringUtils.rightPad(orderbasketDetails.getManufacturer(), 4, " ");
				parmList[12] = new ProgramParameter(manufacturer.getBytes(Program_Commands_Constants.IBM_273), 2);

				String warehouseNumber = StringUtils.leftPad(orderbasketDetails.getWarehouseNumber(), 2, "0");
				parmList[13] = new ProgramParameter(warehouseNumber.getBytes(Program_Commands_Constants.IBM_273), 2);

				String supplierNumber = StringUtils.leftPad(orderbasketDetails.getSupplierNo(), 5, "0");
				parmList[14] = new ProgramParameter(supplierNumber.getBytes(Program_Commands_Constants.IBM_273), 2);

				String deliveryNoteNo = StringUtils.rightPad(orderbasketDetails.getDeliveryNoteNo(), 10, " ");
				parmList[15] = new ProgramParameter(deliveryNoteNo.getBytes(Program_Commands_Constants.IBM_273), 2);
			
			}
			else {
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.ORDER_TRANSFER_MANDATORY_MSG_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.ORDER_TRANSFER_MANDATORY_MSG_KEY), exception);
				throw exception;
			}

			List<String> outputList = cobolServiceRepository.executeProgram(parmList, 0, Program_Commands_Constants.DELIVERY_NOTES_FROM_BSN_TO_ETSTAMM_PROGRAM );

			if(outputList != null && !outputList.isEmpty()) {
				if(!outputList.get(0).contains("Error")) {
					programOutput.put(outputList.get(0).trim(), outputList.get(1).trim());
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.ORDER_TRANSFER_ET_FAILED_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.ORDER_TRANSFER_ET_FAILED_MSG_KEY), exception);
			throw exception;
		}
		return programOutput;
	}
	
	
	/**
	 * This is method is used to get List of Offene Bestellungen Based On Filter from DB.
	 */
	@Override
	public GlobalSearch getBestellKorbListBasedOnFilter(String dataLibrary, String companyId, String agencyId, String allowedWarehouses,
			String pageSize, String pageNumber, String searchText, String searchBy, String sortingBy, String sortingType, List<String> warehouseNos) {

		log.info("Inside getBestellKorbListBasedOnFilter method of OrdersServiceImpl");

		List<OrdersBasket> orderBasketList = new ArrayList<>();
		GlobalSearch globalSearchList = new GlobalSearch();
		StringBuilder query = new StringBuilder();
		
		try {
			if(pageSize==null || pageNumber==null || pageSize.isEmpty() || pageNumber.isEmpty()){
				pageSize = RestInputConstants.DEFAULT_PAGE_SIZE;
				pageNumber = RestInputConstants.DEFAULT_PAGE_NO;
			}

			int totalRecords = Integer.parseInt(pageSize);
			int nextRows = totalRecords * (Integer.parseInt(pageNumber) - 1);

			//validate the page size
			validatePageSize(totalRecords);
			log.info("PageSize  : {}  OFFSET Size (Skip Rows):  {} Page Number  : {} ", pageSize, nextRows, pageNumber);
			
			//validate the Warehouse Ids
			validateWarehouses(allowedWarehouses);
			
			String orderByClause = getOrderByClause(sortingType, sortingBy, "Offene Bestellungen");
			
			if(warehouseNos != null && !warehouseNos.isEmpty()){
				warehouseNos.removeIf(s -> s == null || s.trim().isEmpty());
				allowedWarehouses = StringUtils.join(warehouseNos, ",");
			}

			if(searchBy.equalsIgnoreCase("Auftrag")){

				query.append("Select distinct et.ET_LNR, et.BHERST, et.BEST_BEND, et.PSERV_AUFNR, et.ASERV_AUFNR, et.BDAT, ");
//				query.append("(Select NAME1 from ").append(dataLibrary).append(".e_etstamk6 where KEY2 = et.BEST_BEND ) AS BEST_DESC, ");
				query.append("CASE  WHEN  et.PSERV_AUFNR =0  then ");
				query.append("(Select KDNAM FROM ").append(dataLibrary).append(".F_AUFKO2 WHERE FA = et.ASERV_FA and  FIL = et.ASERV_FIL  and AUFNR = et.ASERV_AUFNR and RECHKZ = 1) ");
				query.append(" WHEN et.ASERV_AUFNR =0  then ");
				query.append("(Select KDNAM FROM ").append(dataLibrary).append(".F_PATKO2 WHERE FA = et.PSERV_FA and  FIL = et.PSERV_FIL  and AUFNR = et.PSERV_AUFNR and RECHKZ = 1) ");
				query.append(" END AS KUNDEN, ");

				query.append("(Select count(*) from ").append(dataLibrary).append(".e_eskdat where PSERV_AUFNR = et.PSERV_AUFNR and ASERV_AUFNR = et.ASERV_AUFNR ");
				query.append("and ET_LNR = et.ET_LNR  and BHERST = et.BHERST and BEST_BEND = et.BEST_BEND and ET_STATUS ='') AS positionen, et.ET_HERST, ");
				query.append("(select count(*) from ");
				query.append("(select  distinct ET_LNR, BHERST, BEST_BEND, PSERV_AUFNR, ASERV_AUFNR, BDAT from ").append(dataLibrary).append(".e_eskdat ");
				query.append(" where ET_STATUS ='' AND not SERV_STATUS='R' AND BEST_BEND='' ");				
				query.append(" AND ET_LNR in (").append(allowedWarehouses).append(") ");				
				query.append(" AND ( CONCAT('0',CAST(ASERV_AUFNR as CHARACTER(10))) LIKE '%"+searchText+"%' OR  CONCAT('0',CAST(PSERV_AUFNR as CHARACTER(10))) LIKE '%"+searchText+"%' ))) AS ROWNUMER from ");
				query.append(dataLibrary).append(".e_eskdat et where ET_STATUS ='' AND not SERV_STATUS='R' AND BEST_BEND='' ");								
				query.append(" AND et.ET_LNR in (").append(allowedWarehouses).append(") ");							
				query.append(" AND (CONCAT('0',CAST(ASERV_AUFNR as CHARACTER(10))) LIKE '%"+searchText+"%' OR  CONCAT('0',CAST(PSERV_AUFNR as CHARACTER(10))) LIKE '%"+searchText+"%') ");
				query.append(orderByClause).append(" OFFSET ");
				query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");
				
			}else if(searchBy.equalsIgnoreCase("Kunde")){
				
				if (searchText != null && searchText.trim().length() > 0) {

					int spaceCount = StringUtils.countMatches(searchText.trim(), " ");
					if (spaceCount > 0) {
						searchText = searchText.trim().replaceFirst(" ", "*").trim();
					} else {
						searchText = searchText.trim();
					}
				}
				
				query.append("(Select distinct et.ET_LNR, et.BHERST, et.BEST_BEND, et.PSERV_AUFNR, et.ASERV_AUFNR, et.BDAT, ");
				query.append("auf0.KDNAM AS KUNDEN, ");
				query.append("(Select count(*) from ").append(dataLibrary).append(".e_eskdat where PSERV_AUFNR = et.PSERV_AUFNR and ASERV_AUFNR = et.ASERV_AUFNR and ET_LNR = et.ET_LNR  and BHERST = et.BHERST and BEST_BEND = et.BEST_BEND and ET_STATUS ='') AS positionen, ");
				query.append("et.ET_HERST, (select count(*) from (select  distinct ET_LNR, BHERST, BEST_BEND, PSERV_AUFNR, ASERV_AUFNR, BDAT ");
				query.append("FROM ").append(dataLibrary).append(".e_eskdat et LEFT JOIN ").append(dataLibrary).append(".F_AUFKO2 auf0 ");
				query.append("ON auf0.FA = et.ASERV_FA and  auf0.FIL = et.ASERV_FIL  and auf0.AUFNR = et.ASERV_AUFNR ");
				query.append("where et.ET_STATUS ='' AND not et.SERV_STATUS='R' AND et.BEST_BEND='' AND auf0.RECHKZ = 1 ");				
				query.append(" AND et.ET_LNR in (").append(allowedWarehouses).append(") ");				
				query.append(" AND  UPPER(trim(auf0.KDNAM)) LIKE ").append("UPPER('%"+searchText+"%'))) ").append(" AS ROWNUMER ");
				query.append("FROM ").append(dataLibrary).append(".e_eskdat et LEFT JOIN ").append(dataLibrary).append(".F_AUFKO2 auf0 ON ");
				query.append("auf0.FA = et.ASERV_FA and  auf0.FIL = et.ASERV_FIL  and auf0.AUFNR = et.ASERV_AUFNR where et.ET_STATUS ='' AND not et.SERV_STATUS='R' AND et.BEST_BEND='' ");
				query.append(" AND et.ET_LNR in (").append(allowedWarehouses).append(") ");				
				query.append(" AND auf0.RECHKZ = 1 AND  UPPER(trim(auf0.KDNAM)) LIKE ").append("UPPER('%"+searchText+"%') ");
				query.append(orderByClause).append(" OFFSET ");
				query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY )");
				
				query.append(" UNION ");
				
				query.append("(Select distinct et.ET_LNR, et.BHERST, et.BEST_BEND, et.PSERV_AUFNR, et.ASERV_AUFNR, et.BDAT, ");
				query.append("auf0.KDNAM AS KUNDEN,");
				query.append("(Select count(*) from ").append(dataLibrary).append(".e_eskdat where PSERV_AUFNR = et.PSERV_AUFNR and ASERV_AUFNR = et.ASERV_AUFNR and ET_LNR = et.ET_LNR  and BHERST = et.BHERST and BEST_BEND = et.BEST_BEND and ET_STATUS ='') AS positionen,");
				query.append("et.ET_HERST, (select count(*) from (select  distinct ET_LNR, BHERST, BEST_BEND, PSERV_AUFNR, ASERV_AUFNR, BDAT from ").append(dataLibrary).append(".e_eskdat et");
				query.append(" LEFT JOIN ").append(dataLibrary).append(".F_PATKO2 auf0 ON  auf0.FA = et.PSERV_FA and  auf0.FIL = et.PSERV_FIL  and auf0.AUFNR = et.PSERV_AUFNR  ");
				query.append("where et.ET_STATUS ='' AND not et.SERV_STATUS='R' AND et.BEST_BEND='' ");				
				query.append(" AND et.ET_LNR in (").append(allowedWarehouses).append(") ");
				query.append(" AND auf0.RECHKZ = 1 AND  UPPER(trim(auf0.KDNAM)) LIKE ").append("UPPER('%"+searchText+"%'))) AS ROWNUMER ");
				query.append("from ").append(dataLibrary).append(".e_eskdat et LEFT JOIN ").append(dataLibrary).append(".F_PATKO2 auf0 ON  auf0.FA = et.PSERV_FA and  auf0.FIL = et.PSERV_FIL  and auf0.AUFNR = et.PSERV_AUFNR ");
				query.append("where et.ET_STATUS ='' AND not et.SERV_STATUS='R' AND et.BEST_BEND='' ");				
				query.append(" AND et.ET_LNR in (").append(allowedWarehouses).append(") ");				
				query.append(" AND auf0.RECHKZ = 1 AND  UPPER(trim(auf0.KDNAM)) LIKE ").append("UPPER('%"+searchText+"%') ) ");
				query.append(orderByClause).append(" OFFSET ");
				query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");
				
			}else{
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.SEARCH_BY_FILTER_INVALID_MSG_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.SEARCH_BY_FILTER_INVALID_MSG_KEY));
				throw exception;
			}			

			List<OrdersBasketDetailsDTO> orderBasketDTOList = new ArrayList<>();
			orderBasketList = dbServiceRepository.getResultUsingQuery(OrdersBasket.class, query.toString(), true);

			//if the list is not empty
			if (orderBasketList != null && !orderBasketList.isEmpty()) {

				orderBasketDTOList = convertOrderBasketEntityToDTO(orderBasketList, orderBasketDTOList);

				globalSearchList.setSearchDetailsList(orderBasketDTOList);
				globalSearchList.setTotalPages(Integer.toString(orderBasketList.get(0).getTotalCount()));
				globalSearchList.setTotalRecordCnt(Integer.toString(orderBasketList.get(0).getTotalCount()));
			}
			else {
				globalSearchList.setSearchDetailsList(orderBasketDTOList);
				globalSearchList.setTotalPages(Integer.toString(0));
				globalSearchList.setTotalRecordCnt(Integer.toString(0));
			}
			

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Offene Bestellungen (Open orders based on filter)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Offene Bestellungen (Open orders based on filter)"), exception);
			throw exception;
		}

		return globalSearchList;
	}
	
	/**
	 * This is method is used to update part number for Teilersetzung
	 */
	@Override
	public Map<String, Boolean> replacePartNumber(OrdersBasketDetailsDTO orderbasketDetails,String dataLibrary, String companyId, String agencyId,String wsId,String userName,
			String newPartNumber, String partName) {

		log.info("Inside replacePardNumber method of OrdersServiceImpl");
		
		boolean update_part_flag = false;
		Map<String, Boolean> outputMap = new HashMap<>();
		outputMap.put("isPartReplacement", update_part_flag);
		String etManufacturer = isValidFielsData(orderbasketDetails.getEtManufacturer());
		String bManufacturer = isValidFielsData(orderbasketDetails.getbManufacturer());
		
		etManufacturer = etManufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING: etManufacturer;
		bManufacturer = bManufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING: bManufacturer;
		
		StringBuilder comman_where_clause = new StringBuilder(" WHERE ET_HERST = ").append("'"+etManufacturer+"' ");
		comman_where_clause.append("AND ET_LNR = ").append(isValidFielsData(orderbasketDetails.getWarehouseNumber()));
		comman_where_clause.append(" AND ET_ETNR = ").append("'"+isValidFielsData(orderbasketDetails.getPartNumber())+"'");
		
		comman_where_clause.append(" AND ET_ETUP = ").append(isValidFielsData(orderbasketDetails.getEt_ETUP()));
		comman_where_clause.append(" AND ASERV_FA = ").append(isValidFielsData(orderbasketDetails.getAserv_FA()));
		comman_where_clause.append(" AND ASERV_FIL = ").append(isValidFielsData(orderbasketDetails.getAserv_FIL()));
		comman_where_clause.append(" AND ASERV_AUFNR = ").append(isValidFielsData(orderbasketDetails.getAsrv_AUFNR()));
		comman_where_clause.append(" AND ASERV_SAART = ").append(isValidFielsData(orderbasketDetails.getAserv_SAART()));
		comman_where_clause.append(" AND ASERV_POSNR = ").append(isValidFielsData(orderbasketDetails.getOrderPosition_ASRV()));
		comman_where_clause.append(" AND ASERV_ERGNR = ").append(isValidFielsData(orderbasketDetails.getAsrv_ERGNR()));
		   
		comman_where_clause.append(" AND PSERV_FA = ").append(isValidFielsData(orderbasketDetails.getPserv_FA()));
		comman_where_clause.append(" AND PSERV_FIL = ").append(isValidFielsData(orderbasketDetails.getPserv_FIL()));
		comman_where_clause.append(" AND PSERV_AUFNR = ").append(isValidFielsData(orderbasketDetails.getPsrv_AUFNR()));
		comman_where_clause.append(" AND PSERV_SAART = ").append(isValidFielsData(orderbasketDetails.getPsrv_SAART()));
		comman_where_clause.append(" AND PSERV_POSNR = ").append(isValidFielsData(orderbasketDetails.getOrderPosition_PSRV()));
		comman_where_clause.append(" AND PSERV_ERGNR = ").append(isValidFielsData(orderbasketDetails.getPsrv_ERGNR()));
		
		comman_where_clause.append(" AND BHERST = ").append("'"+bManufacturer+"'");
		
		comman_where_clause.append(" AND NOT EXISTS (SELECT * FROM ").append(dataLibrary).append(".E_BSNDAT B WHERE ");
		comman_where_clause.append("B.HERST LIKE ").append("'%"+etManufacturer+"%'");
		comman_where_clause.append(" AND B.LNR =  ").append(isValidFielsData(orderbasketDetails.getWarehouseNumber()));
		comman_where_clause.append(" AND  A.ASERV_AUFNR = DECIMAL(B.SERV_AUFNRX, 6, 0)");
		comman_where_clause.append(" AND   A.ASERV_POSNR = DECIMAL(B.SERV_POSNRX, 4, 0)");
		comman_where_clause.append(" AND   A.PSERV_AUFNR = B.PSERV_AUFNR ").append("AND   A.PSERV_POSNR = B.PSRV_POSNR)");
		
		try {

			StringBuilder select_query = new StringBuilder("SELECT SUBSTR(BESTKORB, 0, 14) AS BESTKORB FROM ");
			select_query.append(dataLibrary).append(".E_ESKDAT A ");
			select_query.append(comman_where_clause);
			
			List<OrdersBasketDetails> orderBasketDetailList = dbServiceRepository.getResultUsingQuery(OrdersBasketDetails.class, select_query.toString(), true);

			//if the list is not empty
			if (orderBasketDetailList != null && !orderBasketDetailList.isEmpty() && orderBasketDetailList.size() == 1) {
				OrdersBasketDetails orderBasketDet = orderBasketDetailList.get(0);
				String bestkorb = orderBasketDet.getBestkorb().trim();
				log.debug(" Bestkorb Details : "+bestkorb);
			//blocking record by user	
				if(bestkorb == null || bestkorb.isEmpty()){
					String new_bestkorb = "S".concat(wsId).concat(userName);
				StringBuilder update_query_for_block = new StringBuilder("UPDATE ").append(dataLibrary).append(".E_ESKDAT A SET ");
				update_query_for_block.append("BESTKORB = ").append("CONCAT(").append("'"+new_bestkorb+"'").append(", SUBSTR(BESTKORB, 14))");
				update_query_for_block.append(comman_where_clause);
				
				boolean updateFlag = dbServiceRepository.updateResultUsingQuery(update_query_for_block.toString());
				
				if(updateFlag){
					update_part_flag = true;
				}else{
					log.info("Unable to block user details for part replacement :");
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(
							LocaleContextHolder.getLocale(), ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Part replacement"));
					log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Part replacement"));
					throw exception;
				}
				
				}else if(bestkorb.length()==13 && bestkorb.equalsIgnoreCase("S"+wsId+userName)){
					update_part_flag = true;
					
				}else{
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.ORDER_PART_REPLACEMENT_MSG_KEY));
					log.error(messageService.getReadableMessage(ExceptionMessages.ORDER_PART_REPLACEMENT_MSG_KEY));
					throw exception;	
				}
				
			}else{
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.GET_LIST_FAILED_MSG_KEY,"Parts replacement details "));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY,"Parts replacement details"));
				throw exception;
				
			}
			
			if(update_part_flag){
				log.debug("Update Bestkorb Details");
				String new_bestkorb = "S".concat(wsId).concat(userName);
				StringBuilder update_query = new StringBuilder("UPDATE ").append(dataLibrary).append(".E_ESKDAT A SET ");
				update_query.append("BESTKORB = ").append("CONCAT(").append("'"+new_bestkorb+"'").append(", SUBSTR(BESTKORB, 14)),");
				update_query.append("ET_ETNR = ").append("'"+newPartNumber+"',");
				update_query.append("ET_BEN = ").append("'"+StringUtils.truncate(partName, 19)+"'");
				update_query.append(comman_where_clause);
				boolean updateFlag = dbServiceRepository.updateResultUsingQuery(update_query.toString());
				if(!updateFlag){
					update_part_flag = false;
					log.info("Unable to update part details for replacement :");
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(
							LocaleContextHolder.getLocale(), ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Part replacement"));
					log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Part replacement"));
					throw exception;
				}
				
			}
			outputMap.put("isPartReplacement", update_part_flag);

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Alternative Parts details"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Parts replacement details"), exception);
			throw exception;
		}

		return outputMap;
	}
	
	String isValidFielsData(String fieldValue){
		
		if(fieldValue == null || fieldValue.isEmpty()){
			log.info("Field value is null or empty :" + fieldValue);
			AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Alternative Parts details"));	
			throw exception;
		}
		return fieldValue;
	}
	
	/**
	 * This is method is used to create order history for Bestellhistorie 
	 */
	@Override
	public Map<String, Boolean> createOrderHistory(List<OrdersBasketDetailsDTO> OrderbasketDetailsList, String dataLibrary, String schema,
			 String newOrderNo,String userName, String orderNote) {

		log.info("Inside createOrderHistory method of OrdersServiceImpl");
		
		boolean orderHistoryCreated = false;
		Map<String, Boolean> outputMap = new HashMap<>();
		List<OrderHistory> orderHistoryList = new ArrayList<>();
		outputMap.put("isHistoryCreated", orderHistoryCreated);
		
		if(isValidOrderNo(newOrderNo)){
			log.info("Order number is not valid");
			AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.INVALID_MSG_KEY, "Order Number (Bestellnummer) "));
			log.error(messageService.getReadableMessage(ExceptionMessages.INVALID_MSG_KEY, "Order Number (Bestellnummer) "));
			throw exception;
		}

		String bestArt = newOrderNo.substring(0,2);
		String bestAp = newOrderNo.substring(2);
		log.info("bestArt :  {} and bestAp :  {}", bestArt, bestAp);

		
		try {
			
			if (OrderbasketDetailsList != null && !OrderbasketDetailsList.isEmpty()) {

				for (OrdersBasketDetailsDTO ordersBasketDTO : OrderbasketDetailsList) {
					
					String manufacturer = ordersBasketDTO.getEtManufacturer();
					manufacturer = manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING: manufacturer;
					
					StringBuilder query_get_customerId = new StringBuilder(" SELECT KDNRFZ FROM  ").append(dataLibrary).append((ordersBasketDTO.getIsAserv()) ? ".f_aufko2" : ".F_PATKO2");
					query_get_customerId.append(" WHERE aufnr = ").append("'"+ordersBasketDTO.getOrderNumber()+"'");
					
					StringBuilder query_get_vehicleId = new StringBuilder(" SELECT FZNUMMER FROM  ").append(dataLibrary).append(".f_aufko1 WHERE ");
					query_get_vehicleId.append("aufnr = ").append("'"+ordersBasketDTO.getOrderNumber()+"'");
					
					String companyId = null;
					String agencyId = null;
					String orderType = null;
					if(ordersBasketDTO.getIsAserv()){
						companyId = ordersBasketDTO.getAserv_FA();
						agencyId =  ordersBasketDTO.getAserv_FIL();
						
						if(Integer.parseInt(ordersBasketDTO.getOrderNumber()) > 0){
							orderType = "A";
						}else{
							orderType = "S";
						}
						
					}else{
						
						companyId = ordersBasketDTO.getPserv_FA();
						agencyId =  ordersBasketDTO.getPserv_FIL();
						
						if(Integer.parseInt(ordersBasketDTO.getOrderNumber()) > 0){
							orderType = "P";
						}else{
							orderType = "S";
						}
					}
						
					StringBuilder insert_order = new StringBuilder(" INSERT INTO ").append(schema).append(".o_order");
					insert_order.append(" (COMPANY_ID,AGENCY_ID,ORDERNUMBER,ORDERTYPE,CUSTOMERID,VEHICLEID,LOGIN,TIMESTAMP)  values ( ");
					insert_order.append(companyId).append(","+agencyId+",");
					insert_order.append(ordersBasketDTO.getOrderNumber()+",");
					insert_order.append("'"+orderType+"'"+",");
					
					//get customer Id 
					orderHistoryList = dbServiceRepository.getResultUsingQuery(OrderHistory.class, query_get_customerId.toString(), true);
					
					if(orderHistoryList != null && !orderHistoryList.isEmpty()){
						String customerId = orderHistoryList.get(0).getCustomerId();
						insert_order.append(!customerId.isEmpty() ? customerId+"," :"0,");
					}else{
						insert_order.append("0,");
					}
					//get vehicle Id 
					orderHistoryList= null;
					orderHistoryList = dbServiceRepository.getResultUsingQuery(OrderHistory.class, query_get_vehicleId.toString(), true);
					
					if(orderHistoryList != null && !orderHistoryList.isEmpty()){
						String vehicleId = orderHistoryList.get(0).getVehicleId();
						insert_order.append(!vehicleId.isEmpty() ? vehicleId+","  :"0,");
					}else{
						insert_order.append("0,");
					}
					insert_order.append("'"+userName+"'"+",").append(" CURRENT TIMESTAMP )");	
					
					//insert in O_Order
					int orderId = dbServiceRepository.insertResultUsingQuery(insert_order.toString());
					
					StringBuilder insert_purchase_order = new StringBuilder(" INSERT INTO ").append(schema).append(".O_PRORD");
					insert_purchase_order.append(" (ORDERID,PURCHASEORDERNUMBER,OEM,LOGIN,TIMESTAMP)  values ( ");
					insert_purchase_order.append(orderId+",");
					insert_purchase_order.append(newOrderNo+",");
					insert_purchase_order.append("'"+manufacturer+"'"+",");
					insert_purchase_order.append("'"+userName+"'"+",");
					String orderDate = ordersBasketDTO.getOrderDate();
					String orderTime = ordersBasketDTO.getOrderTime();
					if(orderDate != null && orderDate.length()==10){
					orderDate = orderDate.substring(6,10)+"-"+orderDate.substring(3,5)+"-"+orderDate.substring(0,2);
					}else{
					orderDate = "";
					}
					insert_purchase_order.append("'"+orderDate+" "+orderTime+"'"+")");
					//insert in O_PRORD
					int purchaseOrderId = dbServiceRepository.insertResultUsingQuery(insert_purchase_order.toString());
					
					//get new order details
					StringBuilder query_get_order_details = new StringBuilder(" SELECT ET_ETNR,ET_BEN,SERV_MENGE_B,PSRV_POSNR,ASRV_POSNR,ET_LNR,BDAT,BZEIT FROM  ").append(dataLibrary).append(".E_ESKDAT WHERE ");
					query_get_order_details.append("ET_LNR=").append(ordersBasketDTO.getWarehouseNumber()).append(" and  BHERST='").append(manufacturer+"'");
					query_get_order_details.append( (ordersBasketDTO.getIsAserv()) ? "and ASERV_AUFNR =" : " and PSERV_AUFNR =" );
					query_get_order_details.append(ordersBasketDTO.getOrderNumber()).append(" and BEST_BEND='").append(ordersBasketDTO.getSupplierNo());
					query_get_order_details.append("' And BEST_ART=").append("'"+bestArt+"'").append(" And BEST_AP = ").append(bestAp);
					
					List<OrdersBasketDetails> orderBasketDetailList = dbServiceRepository.getResultUsingQuery(OrdersBasketDetails.class, query_get_order_details.toString(), true);

					if (orderBasketDetailList != null && !orderBasketDetailList.isEmpty()) {
						
						for (OrdersBasketDetails ordersBasketDtlDTO : orderBasketDetailList) {
							
							
							StringBuilder query_get_price_details = new StringBuilder(" SELECT EKNPR, EPR, MC  FROM  ").append(dataLibrary).append(".E_ETSTAMM WHERE ");
							query_get_price_details.append("HERST = ").append("'"+manufacturer+"'");
							query_get_price_details.append("AND LNR = ").append(ordersBasketDtlDTO.getWarehouseNumber());
							query_get_price_details.append(" AND TNR = ").append("'"+ordersBasketDtlDTO.getPartNumber()+"'");
							
							orderHistoryList= null;
							orderHistoryList = dbServiceRepository.getResultUsingQuery(OrderHistory.class, query_get_price_details.toString(), true);
							
							String purchasePrice = "0";
							String sellingPrice = "0";
							String priceCode = "";
							
							if(orderHistoryList != null && !orderHistoryList.isEmpty()){
								OrderHistory orderHistory  = new OrderHistory();
								purchasePrice =  orderHistory.getPurchasePrice();
								sellingPrice = orderHistory.getSellingPrice();
								priceCode = orderHistory.getPartCode();
							}
							
							
							StringBuilder insert_purchase_order_element = new StringBuilder(" INSERT INTO ").append(schema).append(".O_PRORDEL");
							insert_purchase_order_element.append(" (PURCHASEORDERID,PURCHASEORDERHINT,PARTNUMBER,PARTNAME,ORDEREDAMOUNT,PURCHASEPRICE,SELLINGPRICE,PURCHASEORDERPOSITION,PARTCODE,WAREHOUS_ID,LOGIN,TIMESTAMP)  values ( ");
							insert_purchase_order_element.append(purchaseOrderId+",");
							insert_purchase_order_element.append("'"+orderNote+"',");
							insert_purchase_order_element.append("'"+ordersBasketDtlDTO.getPartNumber()+"',");
							insert_purchase_order_element.append("'"+ordersBasketDtlDTO.getPartDescription()+"',");
							insert_purchase_order_element.append(ordersBasketDtlDTO.getTotalPartsNumber()+",");
							insert_purchase_order_element.append(purchasePrice!=null ? purchasePrice+"," : "0,");
							insert_purchase_order_element.append(sellingPrice != null ? sellingPrice+"," : "0,");
							if(ordersBasketDTO.getIsAserv()){
								insert_purchase_order_element.append(ordersBasketDtlDTO.getOrderPosition_ASRV().toString()+",");
							}else{
								insert_purchase_order_element.append(ordersBasketDtlDTO.getOrderPosition_PSRV().toString()+",");
							}
							insert_purchase_order_element.append(priceCode != null && !priceCode.isEmpty() ? priceCode+"," : "'',");
							insert_purchase_order_element.append(ordersBasketDtlDTO.getWarehouseNumber()+",");
							insert_purchase_order_element.append("'"+userName+"'"+",");
							
							String date = convertDateToString(ordersBasketDtlDTO.getOrderDate());
							String time = convertTimeToString(ordersBasketDtlDTO.getOrderTime());
							if(date != null && date.length()==10 ){
							date = date.substring(6,10)+"-"+date.substring(3,5)+"-"+date.substring(0,2);
							}else{
								date = "";
							}
							insert_purchase_order_element.append("'"+date+" "+time+"'"+")");
							
							int purchaseOrderElementId = dbServiceRepository.insertResultUsingQuery(insert_purchase_order_element.toString());
							
						}
						
					}
					
				}
				orderHistoryCreated = true;
			}
			outputMap.put("isHistoryCreated", orderHistoryCreated);

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.CREATE_FAILED_MSG_KEY, "Create order history"));
			log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY, "Create order history"), exception);
			throw exception;
		}

		return outputMap;
	}
	
	
	/**
	 * This method is used to delete the Open Orders using COBOL Program.
	 */
	@Override
	public Map<String, String> deleteOpenOrders( List<OrdersBasketDetailsDTO> OrderbasketDetailsList, String dataLibrary, String companyId, String agencyId,
			String userName, String wsId ) {
		log.info("Inside deleteOpenOrders method of OrdersServiceImpl");

		Map<String, String> programOutput = new HashMap<String, String>();
		String etManufacturer = "";
		String manufacturer = "";
		String warehouseNo = "";
		String supplierNo ="";
		List<String> orderNumbeList = new ArrayList<>();
		List<String> orderPositionNoList = new ArrayList<>();
		try{
			
			if(OrderbasketDetailsList!=null && !OrderbasketDetailsList.isEmpty()) {
				
				for(OrdersBasketDetailsDTO ordersBasketDetDTO : OrderbasketDetailsList) {
					
					etManufacturer = ordersBasketDetDTO.getEtManufacturer();
					manufacturer = ordersBasketDetDTO.getManufacturer();
					warehouseNo = ordersBasketDetDTO.getWarehouseNumber();
					supplierNo = ordersBasketDetDTO.getSupplierNo();
					
					//add order number and orderPositionNo in List
					orderNumbeList.add(ordersBasketDetDTO.getOrderNumber());
					orderPositionNoList.add(createOrderPositionValue(ordersBasketDetDTO.getOrderBasketDetList()));
				}

				ProgramParameter[] parmList = setParametersForDeletePgm(wsId, userName, etManufacturer, manufacturer, 
						warehouseNo, supplierNo, orderNumbeList, orderPositionNoList);
				
				//execute the COBOL program - OBDEL100CL.PGM
				List<String> outputList = cobolServiceRepository.executeProgram(parmList, 0, Program_Commands_Constants.DELETE_OPEN_ORDER_PROGRAM );

				if(outputList != null && !outputList.isEmpty()) {
					if(!outputList.get(0).contains("Error")) {
						programOutput.put(outputList.get(0).trim(), outputList.get(1).trim());
					}
				}
				
			}
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.DELETE_ORDERS_FAILED_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.DELETE_ORDERS_FAILED_MSG_KEY), exception);
			throw exception;
		}

		return programOutput;
	}
	
	
	private ProgramParameter[] setParametersForDeletePgm(String wsId, String userName, String etManufacturer, 
			String manufacturer, String warehouseNo, String supplierNo, List<String> orderNoList, List<String> orderPostionNumberList ) throws Exception {

		ProgramParameter[] parmList = new ProgramParameter[(orderNoList.size()*2)+8];

		// Create the input parameters 
		String returnCode = StringUtils.rightPad("", 5, " ");
		parmList[0] = new ProgramParameter(returnCode.getBytes(Program_Commands_Constants.IBM_273), 5);

		String returnMsg  = StringUtils.rightPad("", 100, " ");
		parmList[1] = new ProgramParameter(returnMsg.getBytes(Program_Commands_Constants.IBM_273), 100);

		String inWSId = StringUtils.rightPad(wsId, 2, " ");
		parmList[2] = new ProgramParameter(inWSId.getBytes(Program_Commands_Constants.IBM_273), 2);

		String inUserName = StringUtils.rightPad(userName, 10, " ");
		parmList[3] = new ProgramParameter(inUserName.getBytes(Program_Commands_Constants.IBM_273), 4);

		String inEtManufacturer = StringUtils.rightPad(etManufacturer, 4, " ");
		parmList[4] = new ProgramParameter(inEtManufacturer.getBytes(Program_Commands_Constants.IBM_273), 4);

		String inManufacturer = StringUtils.rightPad(manufacturer, 4, " ");
		parmList[5] = new ProgramParameter(inManufacturer.getBytes(Program_Commands_Constants.IBM_273), 4);

		String inWarehousNo = StringUtils.leftPad(warehouseNo, 2, "0");
		parmList[6] = new ProgramParameter(inWarehousNo.getBytes(Program_Commands_Constants.IBM_273), 4);

		String inSupplierNumber = StringUtils.rightPad(supplierNo, 5, " ");
		parmList[7] = new ProgramParameter(inSupplierNumber.getBytes(Program_Commands_Constants.IBM_273), 2);
		Integer count = 8;
		
		for(int i=0; i< orderNoList.size(); i++) {
			
			String inOrderNumber = StringUtils.leftPad(orderNoList.get(i), 6, "0");
			parmList[count] = new ProgramParameter(inOrderNumber.getBytes(Program_Commands_Constants.IBM_273), 4);
			count++;
			String inOrderPostionNumber = StringUtils.rightPad(orderPostionNumberList.get(i), 1500, " ");
			parmList[count] = new ProgramParameter(inOrderPostionNumber.getBytes(Program_Commands_Constants.IBM_273), 4);
			count++;
		}
		
		return parmList;
	}
	
	/**
	 * This is method is used to get order history from DB 
	 */
	
	@Override
	public GlobalSearch getOrderHistory(String schema, String dataLibrary, String companyId, String agencyId, String pageSize, String pageNumber) {

		log.info("Inside getOrderHistory method of OrdersServiceImpl");

		GlobalSearch globalSearchList = new GlobalSearch();

		try {
			if(pageSize==null || pageNumber==null || pageSize.isEmpty() || pageNumber.isEmpty()){
				pageSize = RestInputConstants.DEFAULT_PAGE_SIZE;
				pageNumber = RestInputConstants.DEFAULT_PAGE_NO;
			}

			int totalRecords = Integer.parseInt(pageSize);
			int nextRows = totalRecords * (Integer.parseInt(pageNumber) - 1);

			//validate the page size
			validatePageSize(totalRecords);
			log.info("PageSize  : {}  OFFSET Size (Skip Rows):  {} Page Number  : {} ", pageSize, nextRows, pageNumber);

			StringBuilder query = new StringBuilder(" SELECT O.ORDERTYPE, O.ORDERNUMBER, O.CUSTOMERID, O.TIMESTAMP, PR.OEM, PR.PURCHASEORDERNUMBER, ");
			query.append(" ( SELECT distinct COUNT(*) FROM ").append(schema).append(".O_PRORDEL POE_SUB, ");
			query.append(schema).append(".O_PRORD PR_SUB where POE_SUB.PURCHASEORDERID = PR_SUB.ID and  PR_SUB.PurchaseOrderNumber = PR.PURCHASEORDERNUMBER ) AS POE_COUNT, ");
			query.append("POE.WAREHOUS_ID, POE.PURCHASEORDERHINT, ");
			query.append("( SELECT COUNT(*) FROM ").append(schema).append(".o_order O , ");
			query.append(schema).append(".O_PRORD PR ,").append(schema).append(".O_PRORDEL POE where O.ID = PR.ORDERID  AND PR.ID = POE.PURCHASEORDERID) AS ROWNUMER  ");
			query.append(" FROM ").append(schema).append(".o_order O , ");
			query.append(schema).append(".O_PRORD PR ,").append(schema).append(".O_PRORDEL POE where O.ID = PR.ORDERID  AND PR.ID = POE.PURCHASEORDERID ORDER BY PR.PURCHASEORDERNUMBER OFFSET ");
			query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");

			List<OrderHistoryDTO> orderHistoryDTOList = new ArrayList<>();
			List<OrderHistory> orderHistoryList = dbServiceRepository.getResultUsingQuery(OrderHistory.class, query.toString(), true);

			//if the list is not empty
			if (orderHistoryList != null && !orderHistoryList.isEmpty()) {

				orderHistoryDTOList = convertOrderHistoryEntityToDTO(orderHistoryList, orderHistoryDTOList);

				globalSearchList.setSearchDetailsList(orderHistoryDTOList);
				globalSearchList.setTotalPages(Integer.toString(orderHistoryList.get(0).getTotalCount()));
				globalSearchList.setTotalRecordCnt(Integer.toString(orderHistoryList.get(0).getTotalCount()));
			}
			else {
				globalSearchList.setSearchDetailsList(orderHistoryDTOList);
				globalSearchList.setTotalPages(Integer.toString(0));
				globalSearchList.setTotalRecordCnt(Integer.toString(0));
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "BestellKorb (Order Basket)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "BestellKorb (Order Basket)"), exception);
			throw exception;
		}

		return globalSearchList;
	}


	private List<OrderHistoryDTO> convertOrderHistoryEntityToDTO(List<OrderHistory> orderHistoryList,
			List<OrderHistoryDTO> orderHistoryDTOList) {
 
		for(OrderHistory orderHistory : orderHistoryList ){
			OrderHistoryDTO orderHistoryDTO = new OrderHistoryDTO();
			orderHistoryDTO.setCustomerIdNo(String.valueOf(orderHistory.getCustomerIdNo()));
			orderHistoryDTO.setManufacturer(orderHistory.getManufacturer());
			
			String yyyymmdd = "";
			String hhmmss = "";
			
			String orderTime = orderHistory.getOrderDate();
			if(orderTime != null && orderTime.length() > 20){
				yyyymmdd = orderTime.substring(0,orderTime.length()-16);
				hhmmss = orderTime.substring(11,orderTime.length()-7);
				orderHistoryDTO.setOrderDate(yyyymmdd);
			}else{
				orderHistoryDTO.setOrderDate("");
			}
			
			orderHistoryDTO.setOrderNumber(String.valueOf(orderHistory.getOrderNumber()));
			orderHistoryDTO.setOrderType(orderHistory.getOrderType());
			orderHistoryDTO.setPurchaseOrderElementCount(orderHistory.getPurchaseOrderElementCount());
			orderHistoryDTO.setPurchaseOrderHint(orderHistory.getPurchaseOrderHint());
			orderHistoryDTO.setPurchaseOrderNumber(orderHistory.getPurchaseOrderNumber());
			orderHistoryDTO.setWarehouseNo(String.valueOf(orderHistory.getWarehouseNo()));
			orderHistoryDTO.setStatus("Need to define");
			
			orderHistoryDTOList.add(orderHistoryDTO);
		}
		return orderHistoryDTOList;
	}
	
	/**
	 * This is method is used to get order history from DB based on filter 
	 */
	
	@Override
	public GlobalSearch getOrderHistoryBasedOnFilter(String schema, String dataLibrary, String companyId, String agencyId, String pageSize, String pageNumber, String searchText, String searchBy) {

		log.info("Inside getOrderHistory method of OrdersServiceImpl");

		GlobalSearch globalSearchList = new GlobalSearch();
		StringBuilder query = new StringBuilder();
		
		try {
			if(pageSize==null || pageNumber==null || pageSize.isEmpty() || pageNumber.isEmpty()){
				pageSize = RestInputConstants.DEFAULT_PAGE_SIZE;
				pageNumber = RestInputConstants.DEFAULT_PAGE_NO;
			}

			int totalRecords = Integer.parseInt(pageSize);
			int nextRows = totalRecords * (Integer.parseInt(pageNumber) - 1);

			//validate the page size
			validatePageSize(totalRecords);
			log.info("PageSize  : {}  OFFSET Size (Skip Rows):  {} Page Number  : {} ", pageSize, nextRows, pageNumber);

			if(searchBy.equalsIgnoreCase("Auftrag")){
				
				query.append(" SELECT O.ORDERTYPE, O.ORDERNUMBER, O.CUSTOMERID, O.TIMESTAMP, PR.OEM, PR.PURCHASEORDERNUMBER, ");
				query.append(" ( SELECT distinct COUNT(*) FROM ").append(schema).append(".O_PRORDEL POE_SUB, ");
				query.append(schema).append(".O_PRORD PR_SUB where POE_SUB.PURCHASEORDERID = PR_SUB.ID and  PR_SUB.PurchaseOrderNumber = PR.PURCHASEORDERNUMBER ) AS POE_COUNT, ");
				query.append("POE.WAREHOUS_ID, POE.PURCHASEORDERHINT, ");
				query.append("( SELECT COUNT(*) FROM ").append(schema).append(".o_order O , ");
				query.append(schema).append(".O_PRORD PR ,").append(schema).append(".O_PRORDEL POE where O.ID = PR.ORDERID  AND PR.ID = POE.PURCHASEORDERID AND O.ORDERNUMBER LIKE '%"+searchText+"%' ) AS ROWNUMER  ");
				query.append(" FROM ").append(schema).append(".o_order O , ");
				query.append(schema).append(".O_PRORD PR ,").append(schema).append(".O_PRORDEL POE where O.ID = PR.ORDERID  AND PR.ID = POE.PURCHASEORDERID AND O.ORDERNUMBER LIKE '%"+searchText+"%' ORDER BY PR.PURCHASEORDERNUMBER OFFSET ");
				query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");
				
			}else if(searchBy.equalsIgnoreCase("Lager")){
				
				query.append(" SELECT O.ORDERTYPE, O.ORDERNUMBER, O.CUSTOMERID, O.TIMESTAMP, PR.OEM, PR.PURCHASEORDERNUMBER, ");
				query.append(" ( SELECT distinct COUNT(*) FROM ").append(schema).append(".O_PRORDEL POE_SUB, ");
				query.append(schema).append(".O_PRORD PR_SUB where POE_SUB.PURCHASEORDERID = PR_SUB.ID and  PR_SUB.PurchaseOrderNumber = PR.PURCHASEORDERNUMBER ) AS POE_COUNT, ");
				query.append("POE.WAREHOUS_ID, POE.PURCHASEORDERHINT, ");
				query.append("( SELECT COUNT(*) FROM ").append(schema).append(".o_order O , ");
				query.append(schema).append(".O_PRORD PR ,").append(schema).append(".O_PRORDEL POE where O.ID = PR.ORDERID  AND PR.ID = POE.PURCHASEORDERID AND POE.WAREHOUS_ID = "+searchText+" ) AS ROWNUMER  ");
				query.append(" FROM ").append(schema).append(".o_order O , ");
				query.append(schema).append(".O_PRORD PR ,").append(schema).append(".O_PRORDEL POE where O.ID = PR.ORDERID  AND PR.ID = POE.PURCHASEORDERID AND POE.WAREHOUS_ID = "+searchText+" ORDER BY PR.PURCHASEORDERNUMBER OFFSET ");
				query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");
				
			}else if(searchBy.equalsIgnoreCase("Bestellnummer")){
				
				query.append(" SELECT O.ORDERTYPE, O.ORDERNUMBER, O.CUSTOMERID, O.TIMESTAMP, PR.OEM, PR.PURCHASEORDERNUMBER, ");
				query.append(" ( SELECT distinct COUNT(*) FROM ").append(schema).append(".O_PRORDEL POE_SUB, ");
				query.append(schema).append(".O_PRORD PR_SUB where POE_SUB.PURCHASEORDERID = PR_SUB.ID and  PR_SUB.PurchaseOrderNumber = PR.PURCHASEORDERNUMBER ) AS POE_COUNT, ");
				query.append("POE.WAREHOUS_ID, POE.PURCHASEORDERHINT, ");
				query.append("( SELECT COUNT(*) FROM ").append(schema).append(".o_order O , ");
				query.append(schema).append(".O_PRORD PR ,").append(schema).append(".O_PRORDEL POE where O.ID = PR.ORDERID  AND PR.ID = POE.PURCHASEORDERID AND PR.PURCHASEORDERNUMBER LIKE '%"+searchText+"%' ) AS ROWNUMER  ");
				query.append(" FROM ").append(schema).append(".o_order O , ");
				query.append(schema).append(".O_PRORD PR ,").append(schema).append(".O_PRORDEL POE where O.ID = PR.ORDERID  AND PR.ID = POE.PURCHASEORDERID AND PR.PURCHASEORDERNUMBER LIKE '%"+searchText+"%' ORDER BY PR.PURCHASEORDERNUMBER OFFSET ");
				query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");
				
			}else if(searchBy.equalsIgnoreCase("Bestellhinweis")){
				
				query.append(" SELECT O.ORDERTYPE, O.ORDERNUMBER, O.CUSTOMERID, O.TIMESTAMP, PR.OEM, PR.PURCHASEORDERNUMBER, ");
				query.append(" ( SELECT distinct COUNT(*) FROM ").append(schema).append(".O_PRORDEL POE_SUB, ");
				query.append(schema).append(".O_PRORD PR_SUB where POE_SUB.PURCHASEORDERID = PR_SUB.ID and  PR_SUB.PurchaseOrderNumber = PR.PURCHASEORDERNUMBER ) AS POE_COUNT, ");
				query.append("POE.WAREHOUS_ID, POE.PURCHASEORDERHINT, ");
				query.append("( SELECT COUNT(*) FROM ").append(schema).append(".o_order O , ");
				query.append(schema).append(".O_PRORD PR ,").append(schema).append(".O_PRORDEL POE where O.ID = PR.ORDERID  AND PR.ID = POE.PURCHASEORDERID AND POE.PURCHASEORDERHINT LIKE '%"+searchText+"%' ) AS ROWNUMER  ");
				query.append(" FROM ").append(schema).append(".o_order O , ");
				query.append(schema).append(".O_PRORD PR ,").append(schema).append(".O_PRORDEL POE where O.ID = PR.ORDERID  AND PR.ID = POE.PURCHASEORDERID AND POE.PURCHASEORDERHINT LIKE '%"+searchText+"%' ORDER BY PR.PURCHASEORDERNUMBER OFFSET ");
				query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");
				
			}else{
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.SEARCH_BY_FILTER_INVALID_MSG_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.SEARCH_BY_FILTER_INVALID_MSG_KEY));
				throw exception;
			}
			
			

			List<OrderHistoryDTO> orderHistoryDTOList = new ArrayList<>();
			List<OrderHistory> orderHistoryList = dbServiceRepository.getResultUsingQuery(OrderHistory.class, query.toString(), true);

			//if the list is not empty
			if (orderHistoryList != null && !orderHistoryList.isEmpty()) {

				orderHistoryDTOList = convertOrderHistoryEntityToDTO(orderHistoryList, orderHistoryDTOList);

				globalSearchList.setSearchDetailsList(orderHistoryDTOList);
				globalSearchList.setTotalPages(Integer.toString(orderHistoryList.get(0).getTotalCount()));
				globalSearchList.setTotalRecordCnt(Integer.toString(orderHistoryList.get(0).getTotalCount()));
			}
			else {
				globalSearchList.setSearchDetailsList(orderHistoryDTOList);
				globalSearchList.setTotalPages(Integer.toString(0));
				globalSearchList.setTotalRecordCnt(Integer.toString(0));
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "BestellKorb (Order Basket)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "BestellKorb (Order Basket)"), exception);
			throw exception;
		}

		return globalSearchList;
	}
	
	
	/**
	 * This method is used to update Part Relocation Details using COBOL Program.
	 */
	@Override
	public Map<String, String> updatePartRelocationDetails(PartRelocationDetailsDTO partRelocationDetails, String dataLibrary, String companyId, 
			String agencyId, String wsId, String relocationType, String logedInUser ) {
		log.info("Inside updatePartRelocationDetails method of OrdersServiceImpl");

		Map<String, String> programOutput = new HashMap<String, String>();
		ProgramParameter[] parmList = null;
		
		try{
			if(partRelocationDetails.getPartRelocationList()!=null && !partRelocationDetails.getPartRelocationList().isEmpty()) {
				
				if(relocationType.equals("TEILVER") ) {
					parmList = setParametersForBARelocationPgm(partRelocationDetails, wsId, relocationType, logedInUser);
				}
				else if(relocationType.equals("BESTVER")){
					parmList = setParametersForRelocationPgm(partRelocationDetails, wsId, relocationType, logedInUser);
				}
				else {
					log.info(" part Relocation type is invalid.");
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Part Relocation (Verlagern)"));
					log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Part Relocation (Verlagern)"), exception);
					throw exception;
				}

				//execute the COBOL program - OVRLG001CL.PGM
				List<String> outputList = cobolServiceRepository.executeProgram(parmList, 0, Program_Commands_Constants.PART_RELOCATION_PROGRAM );

				if(outputList != null && !outputList.isEmpty()) {
					if(!outputList.get(0).contains("Error")) {
						programOutput.put(outputList.get(0).trim(), outputList.get(1).trim());
					}
				}
			}
			else {
				log.info("Part Relocation Warehouse List is empty");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.PART_RELOCATION_LAGER_EMPTY_MSG_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.PART_RELOCATION_LAGER_EMPTY_MSG_KEY));
				throw exception;
			}
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Part Relocation (Verlagern)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Part Relocation (Verlagern)"), exception);
			throw exception;
		}

		return programOutput;
	}
	
	
	private ProgramParameter[] setParametersForRelocationPgm(PartRelocationDetailsDTO partRelocationDetails, String wsId, String verlagerType, String logedInUser ) throws Exception {

		ProgramParameter[] parmList = new ProgramParameter[(partRelocationDetails.getPartRelocationList().size()*8)+19];

		// Create the input parameters 
		String returnCode = StringUtils.rightPad("", 5, " ");
		parmList[0] = new ProgramParameter(returnCode.getBytes(Program_Commands_Constants.IBM_273), 5);

		String returnMsg  = StringUtils.rightPad("", 300, " ");
		parmList[1] = new ProgramParameter(returnMsg.getBytes(Program_Commands_Constants.IBM_273), 100);

		String inWSId = StringUtils.rightPad(wsId, 2, " ");
		parmList[2] = new ProgramParameter(inWSId.getBytes(Program_Commands_Constants.IBM_273), 2);

		String insctaste  = "00";
		parmList[3] = new ProgramParameter(insctaste.getBytes(Program_Commands_Constants.IBM_273), 2);
		
			parmList[4] = new ProgramParameter(verlagerType.getBytes(Program_Commands_Constants.IBM_273), 2);
		
		String etManufacturer = partRelocationDetails.getEtManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING : partRelocationDetails.getEtManufacturer();

		String inETManufacturer = StringUtils.rightPad(etManufacturer, 4, " ");
		parmList[5] = new ProgramParameter(inETManufacturer.getBytes(Program_Commands_Constants.IBM_273), 4);

		String inWarehousNo = StringUtils.leftPad(partRelocationDetails.getWarehouseNumber(), 2, "0");
		parmList[6] = new ProgramParameter(inWarehousNo.getBytes(Program_Commands_Constants.IBM_273), 2);

		String inAsrv_AUFNR = StringUtils.leftPad(partRelocationDetails.getAsrv_AUFNR(), 6, "0");
		parmList[7] = new ProgramParameter(inAsrv_AUFNR.getBytes(Program_Commands_Constants.IBM_273), 2);

		String inPsrv_AUFNR = StringUtils.leftPad(partRelocationDetails.getPsrv_AUFNR(), 6, "0");
		parmList[8] = new ProgramParameter(inPsrv_AUFNR.getBytes(Program_Commands_Constants.IBM_273), 2);

		if(partRelocationDetails.getIsAserv()) {
			String inAsrv_POS = StringUtils.leftPad(partRelocationDetails.getOrderPositionSerialNo(), 4, "0");
			parmList[9] = new ProgramParameter(inAsrv_POS.getBytes(Program_Commands_Constants.IBM_273), 2);

			String inPsrv_POS = StringUtils.leftPad("0000", 4, "0");
			parmList[10] = new ProgramParameter(inPsrv_POS.getBytes(Program_Commands_Constants.IBM_273), 2);
		}
		else {
			String inAsrv_POS = StringUtils.leftPad("0000", 4, "0");
			parmList[9] = new ProgramParameter(inAsrv_POS.getBytes(Program_Commands_Constants.IBM_273), 2);

			String inPsrv_POS = StringUtils.leftPad(partRelocationDetails.getOrderPositionSerialNo(), 4, "0");
			parmList[10] = new ProgramParameter(inPsrv_POS.getBytes(Program_Commands_Constants.IBM_273), 2);
		}

		String inPartNumber = StringUtils.rightPad(partRelocationDetails.getPartNumber(), 19, " ");
		parmList[11] = new ProgramParameter(inPartNumber.getBytes(Program_Commands_Constants.IBM_273), 4);

		String manufacturer = partRelocationDetails.getManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING : partRelocationDetails.getManufacturer();

		String inManufacturer = StringUtils.rightPad(manufacturer, 4, " ");
		parmList[12] = new ProgramParameter(inManufacturer.getBytes(Program_Commands_Constants.IBM_273), 4);

		String inRequiredQuantity = StringUtils.leftPad(partRelocationDetails.getRequiredQuantity(), 5, "0");
		parmList[13] = new ProgramParameter(inRequiredQuantity.getBytes(Program_Commands_Constants.IBM_273), 2);

		String inRelocationBy = StringUtils.rightPad("%%", 2, " ");
		parmList[14] = new ProgramParameter(inRelocationBy.getBytes(Program_Commands_Constants.IBM_273), 4);

		String inmovementTypeBA = StringUtils.leftPad(partRelocationDetails.getMovementType(), 2, "0");
		parmList[15] = new ProgramParameter(inmovementTypeBA.getBytes(Program_Commands_Constants.IBM_273), 2);

		String inFromPrinter = StringUtils.rightPad(partRelocationDetails.getFromPrinter(), 2, " ");
		parmList[16] = new ProgramParameter(inFromPrinter.getBytes(Program_Commands_Constants.IBM_273), 2);
		
		String userName = StringUtils.rightPad(logedInUser, 10, " ");
		parmList[17] = new ProgramParameter(userName.getBytes(Program_Commands_Constants.IBM_273), 10); //USER
		
		String publisher = "alphaX";
		if(partRelocationDetails.getPublisher()!=null && !partRelocationDetails.getPublisher().trim().isEmpty()){
			publisher = partRelocationDetails.getPublisher();
		}
		
		publisher = StringUtils.rightPad(publisher, 30, " ");
		parmList[18] = new ProgramParameter(publisher.getBytes(Program_Commands_Constants.IBM_273), 30); //publisher

		Integer count = 19;

		List<PartRelocationDTO> partRelocationList = partRelocationDetails.getPartRelocationList();

		for(int i=0; i< partRelocationList.size(); i++) {

			String inListWarehousNo = StringUtils.leftPad(partRelocationList.get(i).getWarehouseNumber(), 2, "0");
			parmList[count] = new ProgramParameter(inListWarehousNo.getBytes(Program_Commands_Constants.IBM_273), 4);
			count++;

			String inListPartName = StringUtils.rightPad(partRelocationDetails.getPartName(), 19, " ");
			parmList[count] = new ProgramParameter(inListPartName.getBytes(Program_Commands_Constants.IBM_273), 4);
			count++;

			String inListPrice = StringUtils.leftPad(partRelocationList.get(i).getPrice(), 8, "0");
			parmList[count] = new ProgramParameter(inListPrice.getBytes(Program_Commands_Constants.IBM_273), 4);
			count++;

			String inListBestand = StringUtils.leftPad(partRelocationList.get(i).getBestand(), 9, "0");
			parmList[count] = new ProgramParameter(inListBestand.getBytes(Program_Commands_Constants.IBM_273), 4);
			count++;

			String inListQuantity1 = StringUtils.leftPad(partRelocationList.get(i).getNumberofQuantity(), 5, "0");
			parmList[count] = new ProgramParameter(inListQuantity1.getBytes(Program_Commands_Constants.IBM_273), 4);
			count++;

			String inListMovementBA = StringUtils.leftPad(partRelocationList.get(i).getMovementType(), 2, "0");
			parmList[count] = new ProgramParameter(inListMovementBA.getBytes(Program_Commands_Constants.IBM_273), 4);
			count++;

			String inPrinter = StringUtils.rightPad(partRelocationList.get(i).getPrinter(), 2, " ");
			parmList[count] = new ProgramParameter(inPrinter.getBytes(Program_Commands_Constants.IBM_273), 2);
			count++;

			String inHinweis = StringUtils.rightPad(partRelocationList.get(i).getNote(), 100, " ");
			parmList[count] = new ProgramParameter(inHinweis.getBytes(Program_Commands_Constants.IBM_273), 2);
			count++;
		}

		return parmList;
	}
	
	
	/**
	 * This method is used to check ESKDAT Order using COBOL Program.
	 */
	@Override
	public Map<String, String> checkESKDATOrders(String dataLibrary, String companyId, String agencyId,String userName, String wsId ) {
		log.info("Inside checkESKDATOrders method of OrdersServiceImpl");

		Map<String, String> programOutput = new HashMap<String, String>();
		ProgramParameter[] parmList = new ProgramParameter[14];
		
		try{
		// Create the input parameters 
		String returnCode = StringUtils.rightPad("", 5, " ");
		parmList[0] = new ProgramParameter(returnCode.getBytes(Program_Commands_Constants.IBM_273), 5);

		String returnMsg  = StringUtils.rightPad("", 100, " ");
		parmList[1] = new ProgramParameter(returnMsg.getBytes(Program_Commands_Constants.IBM_273), 100);

		parmList[2] = new ProgramParameter(StringUtils.rightPad("", 6, " ").getBytes(Program_Commands_Constants.IBM_273), 6);

		String inWSId = StringUtils.rightPad(wsId, 2, " ");
		parmList[3] = new ProgramParameter(inWSId.getBytes(Program_Commands_Constants.IBM_273), 2);

		String inUserName = StringUtils.rightPad(userName, 10, " ");
		parmList[4] = new ProgramParameter(inUserName.getBytes(Program_Commands_Constants.IBM_273), 10);

		String inProgramParam = StringUtils.rightPad("0", 1, " ");
		parmList[5] = new ProgramParameter(inProgramParam.getBytes(Program_Commands_Constants.IBM_273), 1);

		parmList[6] = new ProgramParameter(StringUtils.rightPad("", 4, " ").getBytes(Program_Commands_Constants.IBM_273), 4);

		parmList[7] = new ProgramParameter(StringUtils.rightPad("", 4, " ").getBytes(Program_Commands_Constants.IBM_273), 4);

		parmList[8] = new ProgramParameter(StringUtils.rightPad("", 2, " ").getBytes(Program_Commands_Constants.IBM_273), 2);

		parmList[9] = new ProgramParameter(StringUtils.rightPad("", 6, " ").getBytes(Program_Commands_Constants.IBM_273), 6);

		parmList[10] = new ProgramParameter(StringUtils.rightPad("", 5, " ").getBytes(Program_Commands_Constants.IBM_273), 2);
		
		parmList[11] = new ProgramParameter(StringUtils.rightPad("", 6, " ").getBytes(Program_Commands_Constants.IBM_273), 4);

		parmList[12] = new ProgramParameter(StringUtils.rightPad("", 1500, " ").getBytes(Program_Commands_Constants.IBM_273), 4);
		
		parmList[13] = new ProgramParameter(StringUtils.rightPad("", 6, " ").getBytes(Program_Commands_Constants.IBM_273), 4);
		
		//execute the COBOL program - OBCRT100CL.PGM
				List<String> outputList = cobolServiceRepository.executeProgram(parmList, 0, Program_Commands_Constants.SEND_CREATE_ORDER_PROGRAM );

				if(outputList != null && !outputList.isEmpty()) {
					if(!outputList.get(0).contains("Error")) {
						programOutput.put(outputList.get(0).trim(), outputList.get(1).trim());
					}
				}
			
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.CREATE_FAILED_MSG_KEY, "Check ESKDAT orders"));
			log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY, "Check ESKDAT orders"), exception);
			throw exception;
		}

		return programOutput;
	}
	
	/**
	 * This is method used to get warehouse list for part movement from COBOL Program.
	 */
	@Override
	public Map<String, String> getWarehouseListForPartMovement(String dataLibrary, String companyId, String agencyId, String warehouseNo) {
		log.info("Inside getWarehouseListForPartMovement method of OrdersServiceImpl");

		Map<String, String> programOutput = new HashMap<String, String>();
		
		try{

			ProgramParameter[] parmList = new ProgramParameter[4];

			// Create the input parameters 
			String returnCode = StringUtils.rightPad("", 5, " ");
			parmList[0] = new ProgramParameter(returnCode.getBytes(Program_Commands_Constants.IBM_273), 5);

			String returnMsg  = StringUtils.rightPad("", 100, " ");
			parmList[1] = new ProgramParameter(returnMsg.getBytes(Program_Commands_Constants.IBM_273), 100);

			String warehouseNumber = StringUtils.leftPad(warehouseNo, 2, "0");
			parmList[2] = new ProgramParameter(warehouseNumber.getBytes(Program_Commands_Constants.IBM_273), 2);

			String warehouseList = StringUtils.rightPad("", 300, " ");
			parmList[3] = new ProgramParameter(warehouseList.getBytes(Program_Commands_Constants.IBM_273), 300);

			//execute the COBOL program - OBCRT100CL.PGM
			List<String> outputList = cobolServiceRepository.executeProgram(parmList, 0, Program_Commands_Constants.GET_WAREHOUSE_LIST_FOR_PART_MOVEMENT );

			if (outputList != null && !outputList.isEmpty()) {
				if (!outputList.get(0).contains("Error")) {

					programOutput.put("returnCode", outputList.get(0).trim());
					programOutput.put("returnMsg", outputList.get(1).trim());
					programOutput.put("warehouseList", outputList.get(3).trim());
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY,"Warehouses (From OPRLNR01CL program)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY,"Warehouses (From OPRLNR01CL program)"), exception);
			throw exception;
		}
		return programOutput;
	}
	
	
	private String getOrderByClause(String sortingType, String sortingBy, String methodType) {
		
		StringBuilder orderByClause = new StringBuilder();
		String sortType = "ASC";
		String aservAufnr = "ASERV_AUFNR ";
		
		if(sortingType!= null && sortingType.equalsIgnoreCase("Descending")) {
			sortType = "DESC";
		}
		
		if(methodType.equalsIgnoreCase("Ausgeloste Bestellungen")) {
			aservAufnr = "SERV_AUFNRX ";
		}
		
		switch(sortingBy) {
		
		case "Auftrag":
			orderByClause.append("ORDER BY ").append("PSERV_AUFNR ").append(sortType).append(", "+aservAufnr).append(sortType);			
			break;
		case "Datum":
			orderByClause.append("ORDER BY BDAT ").append(sortType);
			break;
		case "Bestellnummer":
			orderByClause.append("ORDER BY Bestellnummer ").append(sortType);
			break;
		default:
			orderByClause.append("ORDER BY BDAT ").append(sortType);
			break;			
		}
			
		return orderByClause.toString();
	}

	
	/**
	 * This method is used for Search the Parts based on provided parts name or part number.
	 */
	@Override
	public GlobalSearch searchPartsListForVerlegun(String dataLibrary, String schema, String companyId, String agencyId, String oem, String searchString, 
			String pageSize, String pageNo, String flag, String allowedWarehouses, String warehouseNo) {
		log.info("Inside searchPartsListForVerlegun method of OrdersServiceImpl");

		List<SearchParts> partsList = new ArrayList<>();
		GlobalSearch globalSearchList = new GlobalSearch();

		try {
			validateCompany(companyId);
			
			//validate the Warehouse Ids
			validateWarehouses(allowedWarehouses);
			
			if(pageSize==null || pageNo==null || pageSize.isEmpty() || pageNo.isEmpty()){
				pageSize = RestInputConstants.DEFAULT_PAGE_SIZE;
				pageNo = RestInputConstants.DEFAULT_PAGE_NO;
			}

			int totalRecords = Integer.parseInt(pageSize);
			int nextRows = totalRecords * (Integer.parseInt(pageNo) - 1);

			//validate the page size
			validatePageSize(totalRecords);

			log.info("PageSize  : {}  OFFSET Size (Skip Rows):  {} Page Number  : {} ", pageSize, nextRows, pageNo);
			
			if((searchString.startsWith("A") || searchString.startsWith("a")) && searchString.length() >= 11) {
				searchString = searchString.substring(0, 11) + '%' + searchString.substring(11);
			}

			oem = (oem != null && oem.equalsIgnoreCase(RestInputConstants.DAG_STRING))? RestInputConstants.DCAG_STRING : oem;
			
			//this condition is added for - ALPHAX-3824 ( Televerlagan screen is called from Edit part with warehouse and part no. and manufraturer )
			if(warehouseNo != null && !warehouseNo.trim().isEmpty()) {
				allowedWarehouses = warehouseNo;
			}

			StringBuilder query = new StringBuilder("SELECT herst, tnr, lnr, benen, aktbes, filial, tmarke, EPR, VKAVGW, DTLABG, (SELECT NAME FROM ");
			query.append(schema).append(".O_WRH WHERE AP_WAREHOUS_ID = lnr) AS LagerName, ");
			query.append("(SELECT COUNT(*) FROM  ");
			query.append(dataLibrary).append(".E_ETSTAMM WHERE (UPPER(tnr) LIKE UPPER('%").append(searchString).append("%') OR UPPER(benen) LIKE UPPER('%");
			query.append(searchString).append("%')) AND herst='").append(oem).append("' AND LNR IN (").append(allowedWarehouses).append(") and aktbes > 0 ");
			query.append(" ) AS ROWNUMER  FROM ");  
			query.append(dataLibrary).append(".E_ETSTAMM WHERE (UPPER(tnr) LIKE UPPER('%").append(searchString).append("%') OR UPPER(benen) LIKE UPPER('%");
			query.append(searchString).append("%')) AND herst='").append(oem).append("' AND LNR IN (").append(allowedWarehouses).append(") and aktbes > 0 ");
			query.append(" Order by tnr OFFSET ");
			query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");

			List<SearchPartsDTO> vehiclePartsList = new ArrayList<>();
			partsList = dbServiceRepository.getResultUsingQuery(SearchParts.class, query.toString(),true);

			//if the list is not empty
			if (partsList != null && !partsList.isEmpty()) {
				for (SearchParts parts : partsList) {

					SearchPartsDTO partsDTO = new SearchPartsDTO();

					partsDTO.setOem(parts.getOem());
					partsDTO.setPartNumber(parts.getPartNumber());
					partsDTO.setWarehouse(StringUtils.leftPad(parts.getWarehouse(), 2, "0"));
					partsDTO.setAlphaXWarehouseName(parts.getAlphaXWarehouseName());
					partsDTO.setAgency(parts.getAgency());
					partsDTO.setDescription(parts.getDescription());
					partsDTO.setStock(parts.getStock());
					partsDTO.setOemBrand(parts.getOemBrand());	
					partsDTO.setPrice(parts.getPrice());
					partsDTO.setAverageSales(String.valueOf(parts.getAverageSales()));
					partsDTO.setLastDisposalDate(convertDateToString(String.valueOf(parts.getLastDisposalDate())));

					vehiclePartsList.add(partsDTO);
				}
				globalSearchList.setSearchDetailsList(vehiclePartsList);
				globalSearchList.setTotalPages(Integer.toString(partsList.get(0).getTotalCount()));
				globalSearchList.setTotalRecordCnt(Integer.toString(partsList.get(0).getTotalCount()));
			} 
			//this conditions for Teileverlagerung screen part search
			else {
				
				globalSearchList.setSearchDetailsList(vehiclePartsList);
				globalSearchList.setTotalPages(Integer.toString(0));
				globalSearchList.setTotalRecordCnt(Integer.toString(0));
			}
			
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message: {}",  e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.SEARCH_FAILED_MSG_KEY, "Parts"));
			log.error(messageService.getReadableMessage(ExceptionMessages.SEARCH_FAILED_MSG_KEY, "Parts"), exception);
			throw exception;
		}

		return globalSearchList;
	}
	
	
	private ProgramParameter[] setParametersForBARelocationPgm(PartRelocationDetailsDTO partRelocationDetails, String wsId, String verlagerType, String logedInUser ) throws Exception {

		ProgramParameter[] parmList = new ProgramParameter[(partRelocationDetails.getPartRelocationList().size()*8)+19];

		// Create the input parameters 
		String returnCode = StringUtils.rightPad("", 5, " ");
		parmList[0] = new ProgramParameter(returnCode.getBytes(Program_Commands_Constants.IBM_273), 5);

		String returnMsg  = StringUtils.rightPad("", 300, " ");
		parmList[1] = new ProgramParameter(returnMsg.getBytes(Program_Commands_Constants.IBM_273), 100);

		String inWSId = StringUtils.rightPad(wsId, 2, " ");
		parmList[2] = new ProgramParameter(inWSId.getBytes(Program_Commands_Constants.IBM_273), 2);

		String insctaste  = "00";
		parmList[3] = new ProgramParameter(insctaste.getBytes(Program_Commands_Constants.IBM_273), 2);
		
		parmList[4] = new ProgramParameter(verlagerType.getBytes(Program_Commands_Constants.IBM_273), 2);
		
		String etManufacturer = partRelocationDetails.getEtManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING : partRelocationDetails.getEtManufacturer();

		String inETManufacturer = StringUtils.rightPad(etManufacturer, 4, " ");
		parmList[5] = new ProgramParameter(inETManufacturer.getBytes(Program_Commands_Constants.IBM_273), 4);

		String inWarehousNo = StringUtils.leftPad(partRelocationDetails.getWarehouseNumber(), 2, "0");
		parmList[6] = new ProgramParameter(inWarehousNo.getBytes(Program_Commands_Constants.IBM_273), 2);

		String inAsrv_AUFNR = StringUtils.leftPad(partRelocationDetails.getAsrv_AUFNR(), 6, "0");
		parmList[7] = new ProgramParameter(inAsrv_AUFNR.getBytes(Program_Commands_Constants.IBM_273), 2);

		String inPsrv_AUFNR = StringUtils.leftPad(partRelocationDetails.getPsrv_AUFNR(), 6, "0");
		parmList[8] = new ProgramParameter(inPsrv_AUFNR.getBytes(Program_Commands_Constants.IBM_273), 2);

		if(partRelocationDetails.getIsAserv()) {
			String inAsrv_POS = StringUtils.leftPad(partRelocationDetails.getOrderPositionSerialNo(), 4, "0");
			parmList[9] = new ProgramParameter(inAsrv_POS.getBytes(Program_Commands_Constants.IBM_273), 2);

			String inPsrv_POS = StringUtils.leftPad("0000", 4, "0");
			parmList[10] = new ProgramParameter(inPsrv_POS.getBytes(Program_Commands_Constants.IBM_273), 2);
		}
		else {
			String inAsrv_POS = StringUtils.leftPad("0000", 4, "0");
			parmList[9] = new ProgramParameter(inAsrv_POS.getBytes(Program_Commands_Constants.IBM_273), 2);

			String inPsrv_POS = StringUtils.leftPad(partRelocationDetails.getOrderPositionSerialNo(), 4, "0");
			parmList[10] = new ProgramParameter(inPsrv_POS.getBytes(Program_Commands_Constants.IBM_273), 2);
		}

		String inPartNumber = StringUtils.rightPad(partRelocationDetails.getPartNumber(), 19, " ");
		parmList[11] = new ProgramParameter(inPartNumber.getBytes(Program_Commands_Constants.IBM_273), 4);

		String manufacturer = partRelocationDetails.getManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING : partRelocationDetails.getManufacturer();

		String inManufacturer = StringUtils.rightPad(manufacturer, 4, " ");
		parmList[12] = new ProgramParameter(inManufacturer.getBytes(Program_Commands_Constants.IBM_273), 4);

		String inRequiredQuantity = StringUtils.leftPad(partRelocationDetails.getRequiredQuantity(), 5, "0");
		parmList[13] = new ProgramParameter(inRequiredQuantity.getBytes(Program_Commands_Constants.IBM_273), 2);

		String inRelocationBy = StringUtils.rightPad("%%", 2, " ");
		parmList[14] = new ProgramParameter(inRelocationBy.getBytes(Program_Commands_Constants.IBM_273), 4);

		String inmovementTypeBA = StringUtils.leftPad(partRelocationDetails.getMovementType(), 2, "0");
		parmList[15] = new ProgramParameter(inmovementTypeBA.getBytes(Program_Commands_Constants.IBM_273), 2);

		String inFromPrinter = StringUtils.rightPad(partRelocationDetails.getFromPrinter(), 2, " ");
		parmList[16] = new ProgramParameter(inFromPrinter.getBytes(Program_Commands_Constants.IBM_273), 2);
		
		String userName = StringUtils.rightPad(logedInUser, 10, " ");
		parmList[17] = new ProgramParameter(userName.getBytes(Program_Commands_Constants.IBM_273), 10); //USER
		
		String publisher = "alphaX";
		if(partRelocationDetails.getPublisher()!=null && !partRelocationDetails.getPublisher().trim().isEmpty()){
			publisher = partRelocationDetails.getPublisher();
		}
		
		publisher = StringUtils.rightPad(publisher, 30, " ");
		parmList[18] = new ProgramParameter(publisher.getBytes(Program_Commands_Constants.IBM_273), 30); //publisher
		Integer count = 19;

		List<PartRelocationDTO> partRelocationList = partRelocationDetails.getPartRelocationList();

		for(int i=0; i< partRelocationList.size(); i++) {

			String inListWarehousNo = StringUtils.leftPad(partRelocationList.get(i).getWarehouseNumber(), 2, "0");
			parmList[count] = new ProgramParameter(inListWarehousNo.getBytes(Program_Commands_Constants.IBM_273), 4);
			count++;

			String inListPartName = StringUtils.rightPad(partRelocationDetails.getPartName(), 19, " ");
			parmList[count] = new ProgramParameter(inListPartName.getBytes(Program_Commands_Constants.IBM_273), 4);
			count++;

			String inListPrice = StringUtils.leftPad(partRelocationList.get(i).getPrice(), 8, "0");
			parmList[count] = new ProgramParameter(inListPrice.getBytes(Program_Commands_Constants.IBM_273), 4);
			count++;

			String inListBestand = StringUtils.leftPad(partRelocationList.get(i).getBestand(), 9, "0");
			parmList[count] = new ProgramParameter(inListBestand.getBytes(Program_Commands_Constants.IBM_273), 4);
			count++;

			String inListQuantity1 = StringUtils.leftPad(partRelocationList.get(i).getNumberofQuantity(), 5, "0");
			parmList[count] = new ProgramParameter(inListQuantity1.getBytes(Program_Commands_Constants.IBM_273), 4);
			count++;

			String inListMovementBA = StringUtils.leftPad(partRelocationList.get(i).getMovementType(), 2, "0");
			parmList[count] = new ProgramParameter(inListMovementBA.getBytes(Program_Commands_Constants.IBM_273), 4);
			count++;

			String inPrinter = StringUtils.rightPad(partRelocationList.get(i).getPrinter(), 2, " ");
			parmList[count] = new ProgramParameter(inPrinter.getBytes(Program_Commands_Constants.IBM_273), 2);
			count++;

			String inHinweis = StringUtils.rightPad(partRelocationList.get(i).getNote(), 100, " ");
			parmList[count] = new ProgramParameter(inHinweis.getBytes(Program_Commands_Constants.IBM_273), 2);
			count++;
		}

		return parmList;
	}
	
	
	/**
	 * This is method used to recreate the .e36 file using COBOL Program.
	 */
	@Override
	public Map<String, String> recreateE36File(String dataLibrary, OrdersBasketDetailsDTO orderBasketDto) {
		log.info("Inside recreateE36File method of OrdersServiceImpl");

		Map<String, String> programOutput = new HashMap<String, String>();
		String inAUFART = "";
		String inBSTNR = "";
		try{

			ProgramParameter[] parmList = new ProgramParameter[9];
			
			String manufacturer = orderBasketDto.getManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING) ? RestInputConstants.DCAG_STRING : orderBasketDto.getManufacturer();
			
			if(orderBasketDto.getNewOrderNo().length() == 6 ) {
				inAUFART = StringUtils.leftPad(orderBasketDto.getNewOrderNo().substring(0, 2), 2, "0");
				inBSTNR =  StringUtils.rightPad(orderBasketDto.getNewOrderNo().substring(2, 6), 4, " ");
			}
			else if(orderBasketDto.getNewOrderNo().length() == 5 ) {
				inAUFART = StringUtils.leftPad(orderBasketDto.getNewOrderNo().substring(0, 1), 2, "0");
				inBSTNR =  StringUtils.rightPad(orderBasketDto.getNewOrderNo().substring(1, 5), 4, " ");
			}

			// Create the input parameters
			String returnCode = StringUtils.rightPad("", 5, " ");
			parmList[0] = new ProgramParameter(returnCode.getBytes(Program_Commands_Constants.IBM_273), 5);

			String returnMsg  = StringUtils.rightPad("", 100, " ");
			parmList[1] = new ProgramParameter(returnMsg.getBytes(Program_Commands_Constants.IBM_273), 100);
			
			String inrcd = StringUtils.rightPad("0", 1, "0");
			parmList[2] = new ProgramParameter(inrcd.getBytes(Program_Commands_Constants.IBM_273), 1);
			
			String invfnr = StringUtils.rightPad("0", 5, "0");
			parmList[3] = new ProgramParameter(invfnr.getBytes(Program_Commands_Constants.IBM_273), 2);
			
			String inManufacturer = StringUtils.rightPad(manufacturer, 4, " ");
			parmList[4] = new ProgramParameter(inManufacturer.getBytes(Program_Commands_Constants.IBM_273), 4);
			
			String warehouse = StringUtils.leftPad(orderBasketDto.getWarehouseNumber(), 2, "0");
			parmList[5] = new ProgramParameter(warehouse.getBytes(Program_Commands_Constants.IBM_273), 2);
			
			parmList[6] = new ProgramParameter(inAUFART.getBytes(Program_Commands_Constants.IBM_273), 2);
			
			parmList[7] = new ProgramParameter(inBSTNR.getBytes(Program_Commands_Constants.IBM_273), 4);
			
			String inBENDL = StringUtils.rightPad(orderBasketDto.getSupplierNo(), 5, " ");
			parmList[8] = new ProgramParameter(inBENDL.getBytes(Program_Commands_Constants.IBM_273), 5);
			

			//execute the COBOL program - OBSN815CL.PGM
			List<String> outputList = cobolServiceRepository.executeProgram(parmList, 0, Program_Commands_Constants.RECREATE_E36_PROGRAM );

			if (outputList != null && !outputList.isEmpty()) {
				if (!outputList.get(0).contains("Error")) {

					programOutput.put("returnCode", outputList.get(0).trim());
					programOutput.put("returnMsg", outputList.get(1).trim());
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.CREATE_FAILED_MSG_KEY,".es36 file "));
			log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY,".es36 file"), exception);
			throw exception;
		}
		return programOutput;
	}
	
	
	/**
	 * This is method used to delete order Detail which have status Ausgelöst (all items delivered) based on order number from DB.
	 */
	@Override
	public Map<String , Boolean> deleteDeliveredOrder( String dataLibrary, List<OrdersBasketDetailsDTO> deliveredOrdersList) {

		log.info("Inside deleteDeliveredOrder method of OrdersServiceImpl");
		
		Map<String, Boolean> deleteOrdersOutput = new HashMap<String, Boolean>();
		deleteOrdersOutput.put("isDeleted", false);

		try(Connection con = dbServiceRepository.getConnectionObject();
				Statement stmt = con.createStatement();){ 
			
			con.setAutoCommit(false);
			
			if(deliveredOrdersList!=null && !deliveredOrdersList.isEmpty()) {
				
				for(OrdersBasketDetailsDTO ordersBasketDetDTO : deliveredOrdersList) {
					
					if(!StringUtils.isBlank(ordersBasketDetDTO.getWarehouseNumber()) && !StringUtils.isBlank(ordersBasketDetDTO.getManufacturer()) &&
							!StringUtils.isBlank(ordersBasketDetDTO.getNewOrderNo()) && !StringUtils.isBlank(ordersBasketDetDTO.getOrderNumber())){
					String manufacturer = ordersBasketDetDTO.getManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING: ordersBasketDetDTO.getManufacturer();			
					
					StringBuilder query = new StringBuilder("DELETE FROM ");
					query.append(dataLibrary).append(".e_bsndat  Where LNR =").append(ordersBasketDetDTO.getWarehouseNumber());
					query.append(" and HERST='").append(manufacturer);
					query.append("' and  (BEST_ART|| BEST_AP) = ").append(ordersBasketDetDTO.getNewOrderNo());
					query.append( (ordersBasketDetDTO.getIsAserv()) ? " and SERV_AUFNRX =" : " and PSERV_AUFNR =" );
					query.append(ordersBasketDetDTO.getOrderNumber());
					log.info("Query : {} ", query.toString() );
					stmt.addBatch(query.toString());
					}else{
						log.info("Order object not valid for delete - BEST_ART|| BEST_AP : {}, SERV_AUFNRX OR PSERV_AUFNR : {} ", ordersBasketDetDTO.getNewOrderNo() , ordersBasketDetDTO.getOrderNumber()  );
						AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
								ExceptionMessages.DELETE_ADMIN_SETTING_FAILED_MSG_KEY, "(Ausgelöst) order"));
						log.error(messageService.getReadableMessage(ExceptionMessages.DELETE_ADMIN_SETTING_FAILED_MSG_KEY, "(Ausgelöst) order"), exception);
						throw exception;
					}
					
				}
			}
			
			int[] records =  dbServiceRepository.deleteResultUsingBatchQuery(stmt);
			
			if(records != null){
			log.info("Delete delivered (Ausgelöst) order - Total rows deleted :"+records.length);
			}
			con.commit();
			con.setAutoCommit(true);
			deleteOrdersOutput.put("isDeleted", true);

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.DELETE_ADMIN_SETTING_FAILED_MSG_KEY, "delivered (Ausgelöst) order"));
			log.error(messageService.getReadableMessage(ExceptionMessages.DELETE_ADMIN_SETTING_FAILED_MSG_KEY, "delivered (Ausgelöst) order"), exception);
			throw exception;
		}

		return deleteOrdersOutput;
	}

	
	
	/**
	 * This is method is used to get List of Offene Bestellungen (Open Order Basket Details) for part movement is possible or not.
	 */
	@Override
	public List<OrdersBasketDetailsDTO> checkPartMovementPossible(String dataLibrary, String companyId, String agencyId, String warehouseListFromtoken,
			List<OrdersBasketDetailsDTO> orderBasketList) {

		log.info("Inside checkPartMovementPossible method of OrdersServiceImpl");

		String manufacturer = "";
		Map<String, String> warehouseMap = new HashMap<>();
		String finalWarehouseList = null;
		
		try {

			if(orderBasketList!=null && !orderBasketList.isEmpty()) {

				for(OrdersBasketDetailsDTO orderBasket: orderBasketList) {

					manufacturer = orderBasket.getManufacturer().equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING 
							: orderBasket.getManufacturer();
					
					//check in map warehouse is already present
					finalWarehouseList = warehouseMap.getOrDefault(orderBasket.getWarehouseNumber(), null);
					
					if(null == finalWarehouseList ) {
						//call cobol pgm to get the final warehouse list
						finalWarehouseList = filteredWarehouseList(dataLibrary, companyId, agencyId, orderBasket.getWarehouseNumber(), warehouseListFromtoken);
						warehouseMap.put(orderBasket.getWarehouseNumber(), finalWarehouseList);
					}

					StringBuilder query = new StringBuilder("Select et.BEST_BEND, et.MARKE, et.ET_ETNR, et.ET_LNR, et.PSRV_POSNR, et.ASRV_POSNR, ");

					if(finalWarehouseList != null && !finalWarehouseList.isEmpty()){
						query.append("(Select count(*) from ").append(dataLibrary).append(".e_etstamm where TNR = et.ET_ETNR and ");
						query.append(" lnr IN (").append(finalWarehouseList).append(")");
						query.append(" and HERST='").append(manufacturer).append("'  and AKTBES > 0 ) AS CNT_TNR, ");
					}
					query.append(" BHERST from ");

					query.append(dataLibrary).append(".e_eskdat et where ET_STATUS ='' and ET_LNR=").append(orderBasket.getWarehouseNumber());
					query.append(" and  BHERST='").append(manufacturer);
					query.append( (orderBasket.getIsAserv()) ? "' and ASERV_AUFNR =" : "' and PSERV_AUFNR =" );

					query.append(orderBasket.getOrderNumber()).append(" and BEST_BEND='' ORDER BY et.PSRV_POSNR, et.ASRV_POSNR, et.BEST_BEND, et.ET_ETNR ");

					List<OrdersBasketDetails> orderBasketDetailList = dbServiceRepository.getResultUsingQuery(OrdersBasketDetails.class, query.toString(), true);

					//if the list is not empty
					if (orderBasketDetailList != null && !orderBasketDetailList.isEmpty()) {

						//check in the list atleast 1 count_relocate > 0
						boolean partMovementFound = orderBasketDetailList.stream().anyMatch(o -> (o.getCount_Relocate() !=null ? o.getCount_Relocate() : 0 ) > 0);
						orderBasket.setCount_Relocate(partMovementFound ? 1 : 0);
					}
					else {
						orderBasket.setCount_Relocate(0);
					}

				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Part movement check "));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Part movement check "), exception);
			throw exception;
		}

		return orderBasketList;
	}
	
	
	private String filteredWarehouseList(String dataLibrary, String companyId, String agencyId, String warehouseNo, String warehouseListFromtoken) {
		
		String warehouseListFromCobolforPartMovement = null;
		String finalWarehouseList = null;
		Map<String, String> warehouseDetails = getWarehouseListForPartMovement(dataLibrary, companyId, agencyId, warehouseNo);

		if (warehouseDetails != null && !warehouseDetails.isEmpty()) {
			
			if (warehouseDetails.get("returnCode").equalsIgnoreCase("00000")) {
				warehouseListFromCobolforPartMovement = warehouseDetails.get("warehouseList");
				warehouseListFromCobolforPartMovement = StringUtils.removeEnd(warehouseListFromCobolforPartMovement, ",");
			
			List<String> listFromCobol = Stream.of(warehouseListFromCobolforPartMovement.split(",", -1)).collect(Collectors.toList());
			List<String> listFromtoken = Stream.of(warehouseListFromtoken.split(",", -1)).collect(Collectors.toList());
			
			listFromtoken.retainAll(listFromCobol);
			finalWarehouseList = listFromtoken.stream().map(String::valueOf).collect(Collectors.joining(","));
			
			log.info("Warehouse list from cobol program : "+warehouseListFromCobolforPartMovement);
			log.info("Warehouse list from token :"+warehouseListFromtoken);
			log.info("final warehouse list belongs to the Company :"+finalWarehouseList);
			}
		}
		
		return finalWarehouseList;
	}
	
	@Override
	public Map<String, Boolean> resetExecuteOrder(OrdersBasketDetailsDTO ordersBasketDetDTO, String dataLibrary,
			String apCompanyId, String apAgencyId, String logedInUser, String wsId) {

		log.info("Inside deleteDeliveredOrder method of OrdersServiceImpl");

		Map<String, Boolean> deleteOrdersOutput = new HashMap<String, Boolean>();
		deleteOrdersOutput.put("isDeleted", false);

		try (Connection con = dbServiceRepository.getConnectionObject(); Statement stmt = con.createStatement();) {

			// con.setAutoCommit(false);

			if (ordersBasketDetDTO != null ) {

				 
					if (!StringUtils.isBlank(ordersBasketDetDTO.getWarehouseNumber())
							&& !StringUtils.isBlank(ordersBasketDetDTO.getManufacturer())
							&& !StringUtils.isBlank(ordersBasketDetDTO.getNewOrderNo())
							&& !StringUtils.isBlank(ordersBasketDetDTO.getOrderNumber())
							&& !StringUtils.isBlank(ordersBasketDetDTO.getSupplierNo())
							&& !StringUtils.isBlank(ordersBasketDetDTO.getPartPosition())
							&& !StringUtils.isBlank(ordersBasketDetDTO.getPartPositionUP())
							&& !StringUtils.isBlank(ordersBasketDetDTO.getPartNumber())) {
						String manufacturer = ordersBasketDetDTO.getManufacturer()
								.equalsIgnoreCase(RestInputConstants.DAG_STRING) ? RestInputConstants.DCAG_STRING
										: ordersBasketDetDTO.getManufacturer();

						StringBuilder query = new StringBuilder("DELETE FROM ");
						query.append(dataLibrary).append(".e_bsndat  Where LNR =").append(ordersBasketDetDTO.getWarehouseNumber());
						query.append(" and HERST='").append(manufacturer);
						query.append("' and  (BEST_ART||BEST_AP) = ").append(ordersBasketDetDTO.getNewOrderNo());
						query.append(" AND  BEST_BENDL = ").append(ordersBasketDetDTO.getSupplierNo());
						query.append(" AND DISPOPOS = ").append(ordersBasketDetDTO.getPartPosition());
						query.append(" AND DISPOUP = ").append(ordersBasketDetDTO.getPartPositionUP());
						query.append(" AND ETNR ='").append(ordersBasketDetDTO.getPartNumber() + "'");
						log.info("Query : {} ", query.toString());
						stmt.addBatch(query.toString());

						StringBuilder query1 = new StringBuilder("DELETE FROM ");
						query1.append(dataLibrary).append(".e_eskdat  Where BLNR ='")
								.append(ordersBasketDetDTO.getWarehouseNumber() +"'");
						query1.append(" and ET_HERST='").append(manufacturer);
						query1.append("' and  (BEST_ART||BEST_AP) = ").append(ordersBasketDetDTO.getNewOrderNo());
						query1.append(" AND  BEST_BEND = ").append(ordersBasketDetDTO.getSupplierNo());
						query1.append(" AND BDISPOS = ").append(ordersBasketDetDTO.getPartPosition());
						query1.append(" AND BDISUP = ").append(ordersBasketDetDTO.getPartPositionUP());
						query1.append(" AND ET_ETNR  ='").append(ordersBasketDetDTO.getPartNumber() + "'");
						log.info("Query : {} ", query1.toString());
						stmt.addBatch(query1.toString());

					} else{
						log.info("Order object not valid for delete - BEST_ART|| BEST_AP : {}, SERV_AUFNRX OR PSERV_AUFNR : {} ", ordersBasketDetDTO.getNewOrderNo() , ordersBasketDetDTO.getOrderNumber()  );
						AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
								ExceptionMessages.DELETE_ADMIN_SETTING_FAILED_MSG_KEY, "(Ausgelöst) order"));
						log.error(messageService.getReadableMessage(ExceptionMessages.DELETE_ADMIN_SETTING_FAILED_MSG_KEY, "(Ausgelöst) order"), exception);
						throw exception;
					}
				
			}
			int[] records = dbServiceRepository.deleteResultUsingBatchQuery(stmt);

			if (records != null) {
				log.info("reset  (offene) order - Total rows deleted :" + records.length);
				con.commit();
				con.setAutoCommit(true);
				deleteOrdersOutput.put("isDeleted", true);

			}
			
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,
					messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.DELETE_ADMIN_SETTING_FAILED_MSG_KEY,
							"delivered (Ausgelöst) order"));
			log.error(messageService.getReadableMessage(ExceptionMessages.DELETE_ADMIN_SETTING_FAILED_MSG_KEY,
					"reset (offene) order"), exception);
			throw exception;
		}

		return deleteOrdersOutput;
	}
	
	/**
	 * This is method is used to get Alternative Parts details for tree view from DB.
	 */
	@Override
	public List<PartsTreeViewDTO> getPartsDetailsInTreeView(String schema, String dataLibrary, String partNumber, String manufacturer,
			String warehouseNumber) {

		log.info("Inside getPartsDetailsInTreeView method of OrdersServiceImpl");
		List<PartsTreeViewDTO> partsTreeViewList = new ArrayList<>();
		
		try {
				
		Map<String, String> keyValueDetails = new HashMap<>();
		
			String dataCenterLibrary = null;
			ProgramParameter[] parmList = new ProgramParameter[3];

			String returnCode = StringUtils.rightPad("", 5, " ");
			parmList[0] = new ProgramParameter(returnCode.getBytes(Program_Commands_Constants.IBM_273), 5);

			String returnMsg = StringUtils.rightPad("", 100, " ");
			parmList[1] = new ProgramParameter(returnMsg.getBytes(Program_Commands_Constants.IBM_273), 10);

			String location = StringUtils.rightPad("", 10, " ");
			parmList[2] = new ProgramParameter(location.getBytes(Program_Commands_Constants.IBM_273), 10);

			// execute the COBOL program - OFSMFIN.PGM
			List<String> outputList = cobolServiceRepository.executeProgram(parmList, 0,Program_Commands_Constants.GET_DATA_CENTER_LIB_FOR_PREDECESSOR_SUCCESSOR);

			if (outputList != null && !outputList.isEmpty()) {
				if (!outputList.get(0).contains("Error")) {
					dataCenterLibrary = outputList.get(2).trim();
				}
			}
			log.info("DataCenter Library :" + dataCenterLibrary);
			if (dataCenterLibrary == null || dataCenterLibrary.isEmpty()) {
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(
						LocaleContextHolder.getLocale(), ExceptionMessages.INVALID_MSG_KEY, " DataCenter Library"));
				log.error(messageService.getReadableMessage(ExceptionMessages.INVALID_MSG_KEY, " DataCenter Library"),
						exception);
				throw exception;
			}

			StringBuilder queryForText = new StringBuilder(" SELECT CODE,TEXT FROM ").append(dataCenterLibrary).append(".ETHTXTTAB ");

			List<AlternativePartsDetails> alternativePartsTextsList = dbServiceRepository.getResultUsingQuery(AlternativePartsDetails.class, queryForText.toString(), true);

			if (alternativePartsTextsList != null && !alternativePartsTextsList.isEmpty()) {

				for (AlternativePartsDetails partsDetails2 : alternativePartsTextsList) {
					keyValueDetails.put(partsDetails2.getCode(), partsDetails2.getCodeValue());
				}
			}

			List<AlternativePartsDetails> successerList = getPartSuccesserDetails(partNumber, dataLibrary, keyValueDetails, manufacturer,
					 warehouseNumber, schema,  dataCenterLibrary);
			
			List<AlternativePartsDetails> predecesserList = getPartPredecessorDetails(partNumber, dataLibrary, keyValueDetails, manufacturer,
					 warehouseNumber, schema,  dataCenterLibrary);
			
			Map<String, String> keyValueMap  = null;
			
			if(successerList!= null && !successerList.isEmpty()) {
			
				log.debug("#### getPartDetails() method for actual (Aktuelles Teil ) part ####");	
				AlternativePartsDetails partsDetails = successerList.get(0);
				 keyValueMap  = new HashMap<String, String>();
				PartsTreeViewDTO actualPart = new PartsTreeViewDTO();
				
				actualPart.setPartNumber(partsDetails.getPartNumber());
				actualPart =  getPartDetails(actualPart, partsDetails.getPartNumber(), manufacturer, warehouseNumber,
						dataLibrary,schema); 
				
				if(partsDetails.getCodeEB() != null && !partsDetails.getCodeEB().isEmpty()){
					keyValueMap.put(partsDetails.getCodeEB(), keyValueDetails.get(partsDetails.getCodeEB()));
				}
				if(partsDetails.getCodeEF() != null && !partsDetails.getCodeEF().isEmpty()){
					keyValueMap.put(partsDetails.getCodeEF(), keyValueDetails.get(partsDetails.getCodeEF()));
				}
				actualPart.setKeyValueMap(keyValueMap);	
				actualPart.setParentId(null);
				partsTreeViewList.add(actualPart);
			
			
			log.debug("#### getPartDetails() method for predecessor (Vorgänger) part ####");
			
			if(predecesserList!= null && !predecesserList.isEmpty()) {
				
				PartsTreeViewDTO predecesserheader = new PartsTreeViewDTO();
				
				predecesserheader.setPartNumber("Vorgänger");
				predecesserheader.setParentId(actualPart.getPartNumber());
				partsTreeViewList.add(predecesserheader);
				
				for(AlternativePartsDetails predecesserDetails: predecesserList) {
					
					PartsTreeViewDTO partPredecesser = new PartsTreeViewDTO();
					keyValueMap  = new HashMap<String, String>();
					
					partPredecesser.setPartNumber(predecesserDetails.getPartNumber());
					partPredecesser =  getPartDetails(partPredecesser, predecesserDetails.getPartNumber(), manufacturer, warehouseNumber,
							dataLibrary,schema); 
					
					if(predecesserDetails.getCodeEB() != null && !predecesserDetails.getCodeEB().isEmpty()){
						keyValueMap.put(predecesserDetails.getCodeEB(), keyValueDetails.get(predecesserDetails.getCodeEB()));
					}
					if(predecesserDetails.getCodeEF() != null && !predecesserDetails.getCodeEF().isEmpty()){
						keyValueMap.put(predecesserDetails.getCodeEF(), keyValueDetails.get(predecesserDetails.getCodeEF()));
					}
					partPredecesser.setKeyValueMap(keyValueMap);
					
					if(predecesserDetails.getNewPartNumber().equalsIgnoreCase(actualPart.getPartNumber())) {
						partPredecesser.setParentId("Vorgänger");
					}else {
						partPredecesser.setParentId(predecesserDetails.getNewPartNumber());	
					}
					
					partsTreeViewList.add(partPredecesser);
				}
			}
			
			log.debug("#### getPartDetails() method for successor (Nachfolger) part ####");
			
			if(successerList!= null && !successerList.isEmpty()) {
				
				PartsTreeViewDTO successerheader = new PartsTreeViewDTO();
				
				successerheader.setPartNumber("Nachfolger");
				successerheader.setParentId(actualPart.getPartNumber());
				partsTreeViewList.add(successerheader);
				
				for(AlternativePartsDetails successerDetails: successerList) {
					
					PartsTreeViewDTO partPredecesser = new PartsTreeViewDTO();
					 keyValueMap  = new HashMap<String, String>();
					
					partPredecesser.setPartNumber(successerDetails.getNewPartNumber());
					partPredecesser =  getPartDetails(partPredecesser, successerDetails.getNewPartNumber(), manufacturer, warehouseNumber,
							dataLibrary,schema); 
					
					if(successerDetails.getCodeEB() != null && !successerDetails.getCodeEB().isEmpty()){
						keyValueMap.put(successerDetails.getCodeEB(), keyValueDetails.get(successerDetails.getCodeEB()));
					}
					if(successerDetails.getCodeEF() != null && !successerDetails.getCodeEF().isEmpty()){
						keyValueMap.put(successerDetails.getCodeEF(), keyValueDetails.get(successerDetails.getCodeEF()));
					}
					
					partPredecesser.setKeyValueMap(keyValueMap);	
					if(successerDetails.getPartNumber().equalsIgnoreCase(actualPart.getPartNumber())) {
						partPredecesser.setParentId("Nachfolger");
					}else {
						partPredecesser.setParentId(successerDetails.getPartNumber());
					}
					partsTreeViewList.add(partPredecesser);
				}
			}
		}
		}catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Parts treeview details"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Parts treeview details"), exception);
			throw exception;
		}

		return partsTreeViewList;
	}

	
	
	private List<AlternativePartsDetails>  getPartPredecessorDetails(String partNumber, String dataLibrary, Map<String, String> keyValueDetails, String manufacturer,
			String warehouseNumber,String schema, String dataCenterLibrary) {
		
		log.info("Inside getPartPredecessorDetails method of OrdersServiceImpl");
		
			//this query use to get predecessor part details
			StringBuilder predecessor_query = new StringBuilder(" WITH X(  ZEILE, TNR, TNRN, CODEF,CODEB , Level) AS (");
			predecessor_query.append(" SELECT  P.ZEILE, P.TNR, P.TNRN, P.CODEF, P.CODEB  , 0 FROM ");
			predecessor_query.append(dataCenterLibrary).append(".ETHIPS1Z1 P WHERE TNRN = ").append("'"+partNumber+"'");
			predecessor_query.append(" AND (CODEF=21 OR CODEF=22 OR CODEF=24) AND TNR <> TNRN ");
			predecessor_query.append(" UNION ALL ");
			predecessor_query.append(" SELECT P.ZEILE, P.TNR, P.TNRN, P.CODEF, P.CODEB ,Level+1 FROM x ,");
			predecessor_query.append(dataCenterLibrary).append(".ETHIPS1Z1 P  WHERE x.TNR=P.TNRN  ) SELECT * FROM X WHERE LEVEL < 6 ");
			
		List<AlternativePartsDetails> alternativePartsDetailsList = dbServiceRepository.getResultUsingQuery(AlternativePartsDetails.class,predecessor_query.toString(), true);
			
		return alternativePartsDetailsList;
	}

	
	private List<AlternativePartsDetails>  getPartSuccesserDetails(String partNumber, String dataLibrary, Map<String, String> keyValueDetails, String manufacturer,
			String warehouseNumber,String schema, String dataCenterLibrary) {
		
		log.info("Inside getPartSuccesserDetails method of OrdersServiceImpl");
		
			//this query use to get predecessor part details
			StringBuilder successer_query = new StringBuilder(" WITH X( TNRN, TNR, CODEF,CODEB , Level) AS (");
			successer_query.append(" SELECT P.TNRN, P.TNR, P.CODEF, P.CODEB  , 0 FROM ");
			successer_query.append(dataCenterLibrary).append(".ETHIPS1Z1 P WHERE TNR = ").append("'"+partNumber+"'");
			successer_query.append(" AND (CODEF=21 OR CODEF=22 OR CODEF=24) AND TNR <> TNRN ");
			successer_query.append(" UNION ALL ");
			successer_query.append(" SELECT  P.TNRN, P.TNR, P.CODEF, P.CODEB ,Level+1 FROM x ,");
			successer_query.append(dataCenterLibrary).append(".ETHIPS1Z1 P  WHERE x.TNRN=P.TNR  ) SELECT * FROM X WHERE LEVEL < 6 ");
			
		List<AlternativePartsDetails> alternativePartsDetailsList = dbServiceRepository.getResultUsingQuery(AlternativePartsDetails.class,successer_query.toString(), true);
		
		return alternativePartsDetailsList;
	}
	
	private PartsTreeViewDTO getPartDetails(PartsTreeViewDTO treeView, String partNumber, 
			String manufacturer, String warehouseNumber, String dataLibrary, String schema) {
		
			// this query use to get part name from E_ETSTAMM

				StringBuilder query = new StringBuilder(" SELECT DISTINCT BENEN AS partName, AKTBES FROM ").append(dataLibrary).append(".E_ETSTAMM WHERE ");
				query.append(" TNR = ").append("'" + partNumber + "'");
				
				if(manufacturer!=null && !manufacturer.isEmpty()) {
					manufacturer = manufacturer.equalsIgnoreCase(RestInputConstants.DAG_STRING)? RestInputConstants.DCAG_STRING: manufacturer;	
					query.append(" AND HERST = ").append("'" + manufacturer + "'");
				}
				if(warehouseNumber!=null && !warehouseNumber.isEmpty()) {
					query.append(" AND LNR = ").append(warehouseNumber);
				}
				
				List<AlternativePartsDetails> alternativePartsNameList = dbServiceRepository.getResultUsingQuery(AlternativePartsDetails.class,
						query.toString(), true);

				if (alternativePartsNameList != null && alternativePartsNameList.size() > 0) {
					log.debug("Get details from E_ETSTAMM");
					AlternativePartsDetails part = alternativePartsNameList.get(0);
					treeView.setPartName(part.getPartName());
					treeView.setCurrentStock(String.valueOf(part.getCurrentStock()));
				}else{
				StringBuilder query_tle = new StringBuilder(" Select TLE_BENENA FROM ").append(schema).append(".ETK_TLEKAT WHERE ");
				query_tle.append(" TLE_TNR= ").append("'"+partNumber+"'");
				query_tle.append(" AND TLE_DTGULV <= current date order by TLE_DTGULV desc LIMIT 1");
				
				List<PartCatalog> finalizationsPartBAList = dbServiceRepository.getResultUsingQuery(PartCatalog.class, query_tle.toString(), true);
				//if the list is not empty
				if (finalizationsPartBAList != null && !finalizationsPartBAList.isEmpty()) {
					log.debug("Get details from ETK_TLEKAT");
					PartCatalog partCatalog =  finalizationsPartBAList.get(0);
					treeView.setPartName(partCatalog.getPartName());
				}else {
					log.debug("Parts details not available in both tables");
				}
			}
		return treeView;
	}

}