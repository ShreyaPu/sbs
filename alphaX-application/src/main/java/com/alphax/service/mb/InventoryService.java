package com.alphax.service.mb;

import java.util.List;
import java.util.Map;

import com.alphax.vo.mb.ActivatedCountingListDTO;
import com.alphax.vo.mb.AddCountedPartDTO;
import com.alphax.vo.mb.CountingGroupDTO;
import com.alphax.vo.mb.CreateNewCountingListDTO;
import com.alphax.vo.mb.CreatedCountingDetailListDTO;
import com.alphax.vo.mb.CreatedCountingListDTO;
import com.alphax.vo.mb.CreatedCountingListValues;
import com.alphax.vo.mb.DropdownObject;
import com.alphax.vo.mb.GlobalSearch;
import com.alphax.vo.mb.InventoryDTO;
import com.alphax.vo.mb.InventoryDiferentialListDTO;
import com.alphax.vo.mb.PartsBrandDTO;

/**
 * @author A87740971
 *
 */

public interface InventoryService {

	public Map<String, String> getInventoryOverviewCounts(String dataLibrary, String schema, String warehouseNo, String allowedApWarehouse, String manufacturer);

	public List<DropdownObject> getDisplayListForStartedInventory();
	
	public Map<String, String> startInventoryForExistingCountingList(String warehouseNo);
	
	public List<DropdownObject> getWarehouseListWithStatus(String dataLibrary, String schema, String allowedApWarehouse);
	
	public Map<String, String> submitInventoryCount(String warehouseNo, String manufacturer, String partNo, String pageNo, String countValue, String buffer);
	
	public Map<String, String> closeInventory(String warehouseNo);
	
	public List<ActivatedCountingListDTO> getActivatedCountingList(String dataLibrary, String schema, String allowedWarehouses);
	
	public GlobalSearch getActivatedCountingDetailList(String dataLibrary, String schema, String warehouseNo, 
			String pageSize, String pageNumber, String sortingBy, String sortingType, List<String> invStatusIds);
	
	public List<CreatedCountingListValues> getCreatedCountingList(String dataLibrary, String schema, String warehouseNo, List<String> invTypeIds);
	
	public GlobalSearch getCreatedCountingDetailList(String dataLibrary, String schema, String inventoryListId, String orderBy, String pageSize, String pageNumber);
	
	public GlobalSearch getStorageLocationList(String dataLibrary, String schema, String manufacturer, String pageSize, String pageNumber, String searchString);
	
	public List<CountingGroupDTO> getCountingGroupList(String dataLibrary, String schema, String manufacturer, String warehouseNo);
	
	public List<PartsBrandDTO> getPartsBrandList(String dataLibrary, String schema, String manufacturer);
	
	public List<DropdownObject> getSortingAndPrintSelectionList();
	
	public Map<String, Boolean> activateCountingList(String dataLibrary, String schema, String inventoryListId);
	
	public Map<String, Boolean> genericUpdateInventoryStatus(String schema, String inventoryListId, String statusId);
	
	public List<DropdownObject> getAnsichtList();
	
	public GlobalSearch getDifferentialList(String dataLibrary, String schema, String inventoryListId, String opinionValue, String pageSize, 
			String pageNumber, String sortingBy, String sortingType);
	
	public AddCountedPartDTO addCountedPartManuelly(AddCountedPartDTO part_obj, String schema, String dataLibrary);

	public List<InventoryDTO> getCountingListValueFromActiveCounting(String dataLibrary, String schema, String inventoryListId);
	
	public List<DropdownObject> getPrintingPageBreakList();
	
	public Map<String, Boolean> updateCountingListValueFromActiveCounting(String dataLibrary, String schema, List<CreatedCountingDetailListDTO> updatedcountingList);
	
	public GlobalSearch getCountingListItemsFromActiveCounting(String dataLibrary, String schema, String inventoryListId, 
			String pageSize, String pageNumber, String displayType, String searchCount );
	
	public Map<String, Boolean> createNewCountingList(CreateNewCountingListDTO countingList_DTO, String schema, String dataLibrary, String loginUser, String selectionType);
	
	public Map<String, Boolean> addPartInExistingCountingList(CreateNewCountingListDTO countingList_DTO, String schema, String dataLibrary, String loginUser,
			String selectionType, String invListId);
	
	public Map<String, Boolean> saveDifferentialList(String schema, List<InventoryDiferentialListDTO> differntialDTOList, 
			String inventoryListId, boolean selectAllFlag, String opinionValue);
	
	public Map<String, Boolean> processCountingListAsFullyCounted( String dataLibrary, String schema, String loggedInUser, String inventoryListId );
	
	public Map<String, Boolean> deletePartsFromCountingList(String dataLibrary, String schema, String partNo, String manufacturer,String numberOfPlace, String invPartId);
	
	public List<ActivatedCountingListDTO> getClosedCountingList(String dataLibrary, String schema, String allowedWarehouses);
	
	public GlobalSearch getClosedCountingDetailList(String dataLibrary, String schema, String warehouseNo, String pageSize, String pageNumber);
	
	public Map<String, Boolean> markDifferentialListAsFullyProcessed(String dataLibrary, String schema, String loggedInUser, String companyId, String agencyId, 
			String warehouseNo, String inventoryListId, String savLibrary);
	
	public Map<String, Boolean> copyInventoryDtlsIntoAlphaplusHistory(String dataLibrary, String savLibrary, String schema, String invListId, String warehouseNo);
	
	public Map<String, Boolean> updateInventoryStatus(String schema, String inventoryListId);
	
	public InventoryDiferentialListDTO getSumOfDifferntialDelta(String dataLibrary, String schema, String inventoryListId);
	
	public Map<String, Boolean> addSinglePartInExistingCountingList(CreateNewCountingListDTO countingList_DTO, String schema, String dataLibrary, String loginUser,
			 String invListId);
	
	public Map<String, Boolean> checkPartsWithNegativeStock(String dataLibrary, String schema, String warehouseId);

	public List<DropdownObject> getInventoryListStatus(String dataLibrary, String schema);
	
	public String generateCountingListName(String dataLibrary,String schema);
	
	public Boolean checkPartlyCountedStorageLocationOfPart(String dataLibrary,String schema, String inventoryListId);
	
	public List<DropdownObject> getInventoryListType(String dataLibrary,String schema);
	
	public List<CreatedCountingListDTO> getWarehouseListForCreatedCountingList(String dataLibrary, String schema, String allowedWarehouses);
	
}