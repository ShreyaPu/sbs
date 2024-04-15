package com.alphax.service.mb;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

import com.alphax.vo.mb.BAWrapperDTO;
import com.alphax.vo.mb.DropdownObject;
import com.alphax.vo.mb.GlobalSearch;
import com.alphax.vo.mb.LagerDetailsDTO;
import com.alphax.vo.mb.LagerPartDetailsDTO;
import com.alphax.vo.mb.PartDetailsDTO;
import com.alphax.vo.mb.PartPrintingLabelDTO;
import com.alphax.vo.mb.PartsTreeViewDTO;
import com.alphax.vo.mb.SearchPartsDTO;
import com.alphax.vo.mb.StorageLocationDTO;
import com.alphax.vo.mb.partBADetailsDTO;

/**
 * @author A106744104
 *
 */

public interface PartsService {
	
	public GlobalSearch getPartsList(String dataLibrary, String schema, String companyId, String agencyId, String oem, String searchString, 
			String pageSize, String pageNumber, String warehouseId);
	
	public List<DropdownObject> getManufacturerList(String dataLibrary, String schema);

	public List<DropdownObject> getPreisKzList();
	
	public List<DropdownObject> getAbschlagKzList();
	
	public List<DropdownObject> getCategoryList(String dataLibrary, String kategorie);
	
	public PartDetailsDTO getNewPartDetailsByPartNumber(String dataLibrary, String schema, String companyId, String agencyId,
			String warehouseId, String oem, String partId);
	
	
	public List<LagerPartDetailsDTO> getLagerDetailsBasedOnPart( String id, String dataLibrary, String oem, String warehouseId, String allowedWarehouses, String schema);
	
	public GlobalSearch getMovementDataUsingPartNumber(String partId, String dataLibrary, String companyId, String agencyId, String warehouseId, String oem, 
			String flag, String pageSize, String pageNumber);
	
	public List<StorageLocationDTO> getStorageLocationList( String id, String dataLibrary, String warehouseId, String oem);
	
	public Map<String, Map<String,String>> createUpdateParts(BAWrapperDTO baWrapperDTO, String dataLibrary, String companyId, String agencyId, String schema);
	
	public List<DropdownObject> getDiscountGroupAndValue(String dataLibrary, String manufacturer, String marke);
	
	public List<DropdownObject> getMeasuredValueList(String dataLibrary);
	
	public List<DropdownObject> getTireClassificationList(String dataLibrary);
	
	public Map<String, String> executeOMCGETCLPgm(String dataLibrary, String companyId, String agencyId, String warehouseId, String manufacturer, String marketingCode,
			String partNumber, String discountGroup);	
	
	public Map<String, Boolean> createUpdateStorageLocation(List<StorageLocationDTO> storageLocationDTOList, String dataLibrary, String id, String warehouseId, String oem);
	
	public GlobalSearch getStockInformationOfPart( String dataLibrary, String schema, String id, String oem, String pageSize, String pageNumber, String warehouseNo, 
			String allowedWarehouses );
	
	public Map<String, String> convertPartNoFormats(String partNumber, String vonValue, String nachValue);
	
	public List<PartsTreeViewDTO> getLocalAlternativesDetailsInTreeView(String schema, String dataLibrary, String partNumber, String manufacturer,
			String warehouseNumber);

	public List<partBADetailsDTO> getBaseDataforBAPopup(String dataLibrary,  String partNo,
			 String buisnessCase,  String warehouseNo,  String manufacturer);
	
	public GlobalSearch globalPartsSearch(String dataLibrary, String schema, String companyId,String agencyId, String oem, String searchString, 
			String pageSize, String pageNo, String warehouseId);
	
	public ByteArrayInputStream getPartsLabelForCSV(String dataLibrary, PartPrintingLabelDTO partlabelDTO, boolean allStorageLocations);
	
	public List<SearchPartsDTO> globalSearchPartsPrice(String dataLibrary, List<SearchPartsDTO> partsList);
	
	public List<LagerDetailsDTO>  getRequiredWarehouseList(String schema, String dataLibrary,String manufacturer,String partNumber, String allowedWarehouses);
	
}
