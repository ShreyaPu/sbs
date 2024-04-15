package com.alphax.service.mb;

import java.util.List;
import java.util.Map;

import com.alphax.vo.mb.BSNDeliveryNotesDTO;
import com.alphax.vo.mb.BSNDeliveryNotesDetailDTO;
import com.alphax.vo.mb.ConflictResolutionDTO;
import com.alphax.vo.mb.DeliveryNoteSparePartDTO;
import com.alphax.vo.mb.DeliveryNotesDTO;
import com.alphax.vo.mb.DeliveryNotesDetailDTO;
import com.alphax.vo.mb.DropdownObject;
import com.alphax.vo.mb.GlobalSearch;

public interface IncomingGoodsService  {

	public GlobalSearch getDeliveryNoteList(String dataLibrary, String companyId, String agencyId, Integer flag, String allowedWarehouses, String pageSize, String pageNumber,String sortingBy, String sortingType, List<String> warehouseNos);
	
	public List<DeliveryNotesDetailDTO> getDeliveryNoteDetailsList(String deliveryNoteNo, String dataLibrary, String companyId, String agencyId, Integer flag, String orderNumber, String orderReference);
	
	public GlobalSearch getBSN_DeliveryNoteList(String schema,String dataLibrary, String companyId, String agencyId, String allowedWarehouses, String pageSize, String pageNumber,String sortingBy, String sortingType, List<String> warehouseNos);
	
	public List<BSNDeliveryNotesDetailDTO> getBSN_DeliveryNoteDetailsList(String deliveryNoteNo, String dataLibrary, String companyId, String agencyId, 
			String supplierNumber, String manufacturer, String warehouseNumber,String date);
	
	public DeliveryNoteSparePartDTO updateDeliveryNoteSparePartNumber(DeliveryNoteSparePartDTO partDeliveryNoteDtl, String dataLibrary, String companyId,String agencyId);
	
	public Map<String, String> takeOverdeliveryNotetoBSN(List<DeliveryNotesDTO> deliveryNoteList, String dataLibrary, boolean isBSNAll, String companyId, String agencyId, String filterText, String filterBy, List<String> warehouseNos);

	public Map<String, String> takeOverdeliveryNoteFromBSNToETStamm(List<BSNDeliveryNotesDTO> bsnDeliveryNoteList, boolean isEtAll,String dataLibrary, String companyId, String agencyId, String filterText, String filterBy, List<String> warehouseNos);
	
	public GlobalSearch getDeliveryNoteListBasedOnFilter(String dataLibrary, String companyId, String agencyId, String allowedWarehouses, String pageSize, String pageNumber , String searchText, String searchBy, String sortingBy, String sortingType, List<String> warehouseNos);
	
	public GlobalSearch getBSN_DeliveryNoteListBasedOnFilter(String schema,String dataLibrary, String companyId, String agencyId, String allowedWarehouses, String pageSize, String pageNumber , String searchText, String searchBy, String sortingBy, String sortingType, List<String> warehouseNos);
	
	public Map<String, String> deleteDeliveryNotes(List<DeliveryNotesDTO> deliveryNoteList, String dataLibrary, String companyId, String agencyId);
	
	public Map<String, String> deleteAnnouncedAndCancelledNotes(List<DeliveryNotesDTO> deliveryNoteList, String dataLibrary, String companyId, String agencyId);

	public List<DropdownObject> getFilterValuesForDeliveryNote();
	
	public GlobalSearch getConflict_DeliveryNoteList(String schema, String dataLibrary, String companyId, String agencyId, String allowedWarehouses, String pageSize, String pageNumber, String sortingBy, String sortingType, List<String> warehouseNos );
	
	public List<BSNDeliveryNotesDetailDTO> getConflict_DeliveryNoteDetailsList( String deliveryNoteNo, String dataLibrary, String schema, String companyId, String agencyId, 
			String supplierNumber, String manufacturer, String warehouseNumber,String date );
	
	public Map<String, Boolean> createDeliveryNoteHistory(String dataLibrary, String schema,String userName, String wsid);

	public List<ConflictResolutionDTO> getConflictResolution(String dataLibrary, String schema, String companyId, String agencyId, 
			String supplierNumber, String manufacturer, String warehouseNumber, String deliveryNoteNo, String partNo, 
			String orderNumber, String dispopos, String dispoup, String conflict_Codes);
	
	public Map<String, String> updateConflictResolution(List<ConflictResolutionDTO> conflictResolutionList, String dataLibrary, String schema, String companyId, String agencyId, String userName, String wsid);
	
	public GlobalSearch getSolvedConflicts_DeliveryNoteList(String dataLibrary, String schema, String companyId, String agencyId, String allowedWarehouses, String pageSize, String pageNumber, String sortingBy, String sortingType, List<String> warehouseNos );
	
	public Map<String, Boolean> deleteBSNDeliveryNotes( String dataLibrary, List<BSNDeliveryNotesDTO> bsnDeliveryNoteList);
	
	public Map<String, String> backlogResolution(String dataLibrary, String backlogType, String agencyId, String companyId);

	
			 
}
