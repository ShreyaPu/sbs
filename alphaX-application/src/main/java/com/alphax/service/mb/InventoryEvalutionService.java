package com.alphax.service.mb;

import java.util.List;

import com.alphax.vo.mb.GlobalSearch;
import com.alphax.vo.mb.InventoryArchivedCountingDTO;
import com.alphax.vo.mb.InventoryArchivedDifferencesDTO;
import com.alphax.vo.mb.InventoryEvalutionDTO;

/**
 * @author A106744104
 *
 */

public interface InventoryEvalutionService {

	public GlobalSearch getInventoryRemarkableItems(String dataLibrary, String savLibrary, String warehouseNo, String fromDate, String toDate, 
			String pageSize, String pageNumber);
	
	public List<InventoryEvalutionDTO> getInventoryRemarkableItemsDetails(String dataLibrary, String savLibrary, String partNo, String warehouseNo);
	
	public List<InventoryArchivedDifferencesDTO> getInventoryArchivedDifferencesList(String dataLibrary, String savLibrary, String warehouseNo, 
			String fromDate, String toDate);
	
	public List<InventoryArchivedDifferencesDTO> getInventoryArchivedDifferencesInDetails(String dataLibrary, String savLibrary, String warehouseNo,
			String date, String listId, String detailsType);

	public List<InventoryArchivedCountingDTO> getArchivedCountingList(String dataLibrary, String savLibrary, String warehouseNo, String fromDate, String toDate);
	
	public List<InventoryArchivedCountingDTO> getArchivedCountingDetailList(String dataLibrary, String savLibrary, String warehouseNo, String inventoryDate, String inventoryTime, String detailsType);
}