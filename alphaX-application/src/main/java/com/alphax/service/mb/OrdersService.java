package com.alphax.service.mb;

import java.util.List;
import java.util.Map;

import com.alphax.vo.mb.AlternativePartsAvailabilityDTO;
import com.alphax.vo.mb.AlternativePartsDTO;
import com.alphax.vo.mb.DropdownObject;
import com.alphax.vo.mb.GlobalSearch;
import com.alphax.vo.mb.OrdersBasketDetailsDTO;
import com.alphax.vo.mb.PartRelocationDTO;
import com.alphax.vo.mb.PartRelocationDetailsDTO;
import com.alphax.vo.mb.PartsTreeViewDTO;

/**
 * @author A106744104
 *
 */

public interface OrdersService {

	public GlobalSearch getBestellKorbList(String dataLibrary, String companyId, String agencyId,
			String allowedWarehouses, String pageSize, String pageNumber, String sortingBy, String sortingType,
			List<String> warehouseNos);

	public List<OrdersBasketDetailsDTO> getBestellKorbDetailsList(String orderNumber, String dataLibrary,
			String companyId, String agencyId, String manufacturer, String warehouseNo, Boolean isAserv,
			String supplierNo, String warehouseListFromToken);

	public List<DropdownObject> getTeileMarkeList(String dataLibrary, String schema, String manufacturer);

	public List<DropdownObject> getAuftragsartList();

	public List<DropdownObject> getAbgebendesLagerBAList();

	public List<DropdownObject> getEmpfangendesLagerBAList(String dataLibrary, String companyId, String agencyId,
			String partNo, String warehouseNo, String manufacturer);

	public GlobalSearch searchPartsList(String dataLibrary, String schema, String companyId, String agencyId,
			String oem, String searchString, String warehouseNo, String pageSize, String pageNo, String flag);

	public List<PartRelocationDTO> getPartRelocationDetailsList(String dataLibrary, String schema, String companyId,
			String agencyId, String partNo, String warehouseNo, String manufacturer,
			String warehouseListFromCobolforPartMovement, String warehouseListFromToken);

	public Map<String, Boolean> checkDuplicateOrderNumber(String dataLibrary, String companyId, String agencyId,
			String manufacturer, String warehouseNo, String orderNo, String supplierNo);

	public List<AlternativePartsDTO> getAlternativePartsDetails(String schema, String dataLibrary, String companyId,
			String agencyId, String partNumber, String manufacturer, String warehouseNumber);

	public Map<String, String> addPartsInOrderBasket(OrdersBasketDetailsDTO orderBasketDetails, String dataLibrary,
			String companyId, String agencyId, String userName, String wsId);

	public Map<String, String> generateOrderNumber(String dataLibrary, String companyId, String agencyId,
			String manufacturer, String etManufacturer, String warehouseNo, String orderNo, String supplierNo,
			String userName, String wsId);

	public Map<String, String> createOrders(List<OrdersBasketDetailsDTO> OrderbasketDetailsList, String dataLibrary,
			String companyId, String agencyId, String newOrderNo, String supplierNo, String orderNote, String userName,
			String wsId);

	public List<AlternativePartsAvailabilityDTO> checkAlternativePartsAvailability(
			List<AlternativePartsAvailabilityDTO> alternativePartsAvailabilityList, String dataLibrary,
			String companyId, String agencyId);

	public GlobalSearch getOrdersList(String dataLibrary, String companyId, String agencyId, String allowedWarehouses,
			String pageSize, String pageNumber, String sortingBy, String sortingType, List<String> warehouseNos);

	public List<OrdersBasketDetailsDTO> getOrderDetailsList(String newOrderNumber, String dataLibrary, String companyId,
			String agencyId, String manufacturer, String warehouseNo, Boolean isAserv, String supplierNo,
			String orderNumber);

	public GlobalSearch getOrdersListBasedOnFilter(String dataLibrary, String companyId, String agencyId,
			String allowedWarehouses, String pageSize, String pageNumber, String searchText, String searchBy,
			String sortingBy, String sortingType, List<String> warehouseNos);

	public List<DropdownObject> getFilterValuesForOrder();

	public Map<String, String> transferOrderstoET(OrdersBasketDetailsDTO orderbasketDetails, String dataLibrary,
			String companyId, String agencyId);

	public GlobalSearch getBestellKorbListBasedOnFilter(String dataLibrary, String companyId, String agencyId,
			String allowedWarehouses, String pageSize, String pageNumber, String searchText, String searchBy,
			String sortingBy, String sortingType, List<String> warehouseNos);

	public Map<String, Boolean> replacePartNumber(OrdersBasketDetailsDTO orderbasketDetails, String dataLibrary,
			String companyId, String agencyId, String wsId, String userName, String partNumber, String partName);

	public Map<String, Boolean> createOrderHistory(List<OrdersBasketDetailsDTO> OrderbasketDetailsList,
			String dataLibrary, String schema, String newOrderNo, String userName, String orderNote);

	public Map<String, String> deleteOpenOrders(List<OrdersBasketDetailsDTO> OrderbasketDetailsList, String dataLibrary,
			String companyId, String agencyId, String userName, String wsId);

	public GlobalSearch getOrderHistory(String schema, String dataLibrary, String companyId, String agencyId,
			String pageSize, String pageNumber);

	public GlobalSearch getOrderHistoryBasedOnFilter(String schema, String dataLibrary, String companyId,
			String agencyId, String pageSize, String pageNumber, String searchText, String searchBy);

	public Map<String, String> updatePartRelocationDetails(PartRelocationDetailsDTO partRelocationDetails,
			String dataLibrary, String companyId, String agencyId, String wsId, String relocationType,
			String logedInUser);

	public Map<String, String> checkESKDATOrders(String dataLibrary, String companyId, String agencyId, String userName,
			String wsId);

	public Map<String, String> getWarehouseListForPartMovement(String dataLibrary, String companyId, String agencyId,
			String warehouseNo);

	public GlobalSearch searchPartsListForVerlegun(String dataLibrary, String schema, String companyId, String agencyId,
			String oem, String searchString, String pageSize, String pageNo, String flag, String allowedWarehouses, String warehouseNo);

	public Map<String, String> recreateE36File(String dataLibrary, OrdersBasketDetailsDTO orderBasketDto);

	public Map<String, Boolean> deleteDeliveredOrder(String dataLibrary,
			List<OrdersBasketDetailsDTO> deliveredOrdersList);

	public List<OrdersBasketDetailsDTO> checkPartMovementPossible(String dataLibrary, String companyId, String agencyId,
			String warehouseListFromToken, List<OrdersBasketDetailsDTO> orderBasketList);

	public Map<String, Boolean> resetExecuteOrder(OrdersBasketDetailsDTO ordersBasketDetDTO,String dataLibrary, String apCompanyId, String apAgencyId, String logedInUser, String wsId);
	
	public List<PartsTreeViewDTO> getPartsDetailsInTreeView(String schema, String dataLibrary,String partNumber, String manufacturer,
			String warehouseNumber);
	

}
