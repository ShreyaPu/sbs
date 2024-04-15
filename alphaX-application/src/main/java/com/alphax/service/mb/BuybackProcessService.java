package com.alphax.service.mb;

import java.util.List;
import java.util.Map;

import com.alphax.vo.mb.BuybackHeaderDTO;
import com.alphax.vo.mb.BuybackHeaderDetailsDTO;

/**
 * @author A87740971
 *
 */

public interface BuybackProcessService {

	public List<BuybackHeaderDTO> getBuybackOpenHeaderInfo(String schema, String allowedWarehouses, List<String> warehouseNos);

	public List<BuybackHeaderDetailsDTO> getBuybackOpenHeaderItemDetails(String warehouseNo, String headerId, String schema, String dataLibrary);
	
	public List<BuybackHeaderDTO> getBuybackClosedHeaderInfo(String schema, String allowedWarehouses, List<String> warehouseNos);
	
	public List<BuybackHeaderDetailsDTO> getBuybackClosedHeaderItemDetails(String alphaPlusWarehouseNo, String headerId, String schema, String dataLibrary, String transferDate);
	
	public Map<String, Boolean> saveBuybackOpenItemDetails(List<BuybackHeaderDetailsDTO> buybackHeaderDetailsDTOList, String headerId, String schema, String loginUserName);

	public Map<String, Boolean> updateBuybackDataFromARBBOF(String schema, String dataLibrary);

	public Map<String, String> getBuybackDashboardCount(String dataLibrary, String schema);

	public Map<String, Boolean> updateBuybackHeadStatus(String schema, String headerId);
	
	public Map<String, Boolean> bookOutBuybackOpenItemDetails(List<BuybackHeaderDetailsDTO> buybackHeaderDetailsDTOList, String headerId, String orderNumber, String schema, 
			String dataLibrary, String loginUserName, String alphaPlusWarehouseNo, String companyId, String agencyId);
	
}
