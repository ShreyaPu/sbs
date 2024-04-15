package com.alphax.service.mb;

import java.util.List;
import java.util.Map;

import com.alphax.vo.mb.AlphaXConfigurationKeysDetailsDTO;
import com.alphax.vo.mb.AlphaXConfigurationSetupDTO;

public interface DashboardService {

	Map<String, String> getGoodsReceipt(String dataLibrary, String schema, String companyId, String agencyId, String allowedWarehouses);
	
	Map<String, String> getDailyUpdateCount(String dataLibrary, String allowedWarehouses);

	List<AlphaXConfigurationSetupDTO> getAlphaXConfigurationDetails(String schema, String dataLibrary, String companyId, String agencyId);

	List<AlphaXConfigurationSetupDTO>  updateAlphaXConfigurationDetails(List<AlphaXConfigurationSetupDTO> alphaXConfigurationSetupList,
			String schema, String dataLibrary);

	Map<String, String> create_OSetupTable(String dataLibrary, String companyId, String agencyId);
	
	public List<AlphaXConfigurationKeysDetailsDTO> getAutomaticDeliveryNoteProcessingDetails(String schema, String dataLibrary, 
			String warehouseId, String companyId);
	
	public List<AlphaXConfigurationKeysDetailsDTO> updateAutomaticDeliveryNoteProcessingDetails(List<AlphaXConfigurationKeysDetailsDTO> alphaXConfigurationSetupList,
			String schema, String dataLibrary, String companyId);
	
	public Map<String, String> getInventoryOverviewCounts(String dataLibrary, String schema, String allowedApWarehouse);

}