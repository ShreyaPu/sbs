/**
 * 
 */
package com.alphax.service.mb;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import com.alphax.vo.mb.GlobalSearch;
import com.alphax.vo.mb.MovementSearchDTO;
import com.alphax.vo.mb.ReportForBookingAccountChangesDTO;
import com.alphax.vo.mb.ReportForBookingStockDifferencesDTO;
import com.alphax.vo.mb.ReportSelectionsForBusinessCaseDTO;
import com.alphax.vo.mb.ReportSelectionsForInventoryDTO;

/**
 * @author A87740971
 *
 */
public interface ReportSelectionsService {

	public GlobalSearch getSelectionsFromStock(String schema, String dataLibrary, String companyId, String allowedApWarehouse, String manufacturer, List<String> warehouseNos, 
			boolean partExpiryDate, boolean withHoldings,  boolean withoutInventory, boolean negativeInventory, boolean relevantToTheft, boolean oldPart, 
			boolean recordType1, boolean recordType2, boolean hazardousMaterialPart, boolean multipleStorageLocations, String pageSize, String pageNumber);
	
	public ByteArrayInputStream generateReportFromStock(String schema, String dataLibrary, String companyId,String allowedApWarehouse, String manufacturer, 
			List<String> warehouseNos, boolean partExpiryDate, boolean withHoldings,  boolean withoutInventory, boolean negativeInventory, 
			boolean relevantToTheft, boolean oldPart, boolean recordType1, boolean recordType2, boolean hazardousMaterialPart, boolean multipleStorageLocations);
	
	public GlobalSearch getSelectionsBasedMarketability(String schema, String dataLibrary, String companyId, String allowedApWarehouse, String manufacturer, List<String> warehouseNos,  
			String noOfCommonParts, String periods, String sortOrder, String moventType, String pageSize, String pageNumber);
	
	public ByteArrayInputStream generateReportBasedOnMarketability(String schema, String dataLibrary, String companyId, String allowedWarehouses, String manufacturer,
			List<String> warehouseNos, String noOfCommonParts, String periods, String sortOrder, String moventType );
	
	public List<ReportSelectionsForInventoryDTO> getSelectionsForInventory(String schema, String dataLibrary, String companyId, String allowedWarehouses, List<String> warehouseNos,
			String manufacturer, String partBrand, String selectedType);
	
	public ByteArrayInputStream generateReportForInventoryStructure(String schema, String dataLibrary, String companyId, String allowedWarehouses, 
			List<ReportSelectionsForInventoryDTO> selectedInventoryList, String selectedType, List<String> warehouseNos, String manufacturer, String partBrand );
	
	public List<ReportSelectionsForBusinessCaseDTO> getBusinessCaseStatistics_DAK(String schema, String dataLibrary, String companyId, String allowedWarehouses,
			String fromDate, String toDate);
	
	public List<ReportSelectionsForBusinessCaseDTO> getBusinessCaseStatistics_ManualCount(String schema, String dataLibrary, String companyId, String allowedWarehouses,
			String fromDate, String toDate);
	
	public List<ReportSelectionsForBusinessCaseDTO> getBusinessCaseStatistics_ManualCorrection(String schema, String dataLibrary, String companyId, String allowedWarehouses,
			String fromDate, String toDate);
	
	public List<ReportSelectionsForBusinessCaseDTO> getBusinessCaseStatistics_Redemptions(String schema, String dataLibrary, String companyId, String allowedWarehouses,
			String fromDate, String toDate);
	
	public List<ReportSelectionsForBusinessCaseDTO> getBusinessCaseStatistics_WithoutInventoryMovement(String schema, String dataLibrary, String companyId, String allowedWarehouses,
			String fromDate, String toDate);
	
	public InputStream generateReportBasedOnBAStatistics(String schema, String dataLibrary, String companyId, String allowedWarehouses, String fromDate, String toDate );
	
	public GlobalSearch getSelectionsFromMovement(String schema, String dataLibrary, String companyId, String allowedWarehouses, 
			String manufacturer, List<String> warehouseNos, String fromDate, String toDate, String businessCase, boolean invtCustPos, boolean custInvtPos,
			boolean dak, boolean chngOfmktgCode, boolean partTypeOrSupplier, String documentNo, String customerNo,
			String supplierNo, String partNo, String furMktgCode, String brand,
			String pageSize, String pageNumber);
	
	public ByteArrayInputStream generateReportFromMovement(String schema, String dataLibrary, String companyId, String allowedWarehouses, 
			String manufacturer, List<String> warehouseNos, String fromDate, String toDate, String businessCase, boolean invtCustPos, boolean custInvtPos,
			boolean dak, boolean chngOfmktgCode, boolean partTypeOrSupplier, String documentNo, String customerNo,
			String supplierNo, String partNo, String furMktgCode, String brand);
	
	
	public List<ReportSelectionsForBusinessCaseDTO> generateBookingReletedList(String schema, String dataLibrary, String allowedWarehouses,
			List<String> warehouseNos, String manufacturer, String fromDate, String toDate, boolean includeArchivedData);
	
	public ByteArrayInputStream generateReportOnBookingReletedList(String schema, String dataLibrary, String allowedWarehouses, 
			List<String> warehouseNos, String manufacturer, String fromDate, String toDate, boolean includeArchivedData );	
	
	public List<MovementSearchDTO> searchMovementList(String dataLibrary, String searchType, String searchValue, boolean archData );	
	
	public GlobalSearch generatePartSupplyRelatedList(String schema, String dataLibrary, String allowedWarehouses, String companyId, String pageSize,
			String pageNumber );
	
	public ByteArrayInputStream generateReportForPartSupply(String schema, String dataLibrary, String allowedWarehouses, String companyId );	
	
	public GlobalSearch generateIntraTradeStatisticsList(String schema, String dataLibrary, String fromDate, String toDate, String customerNo, String customerGroup, 
			String pageSize, String pageNumber );
	
	public ByteArrayInputStream generateReportIntraTradeStatistics(String schema, String dataLibrary, String fromDate, String toDate, String customerNo, String customerGroup );
	
	public List<ReportForBookingStockDifferencesDTO> getBookingRelevantStockDiffList(String schema, String dataLibrary, String allowedWarehouses, List<String> warehouseNos, String manufacturer, String fromDate, String toDate);
	
	public List<ReportForBookingAccountChangesDTO> getBookingRelevantAccountChangeList(String schema, String dataLibrary, String allowedWarehouses, List<String> warehouseNos, String manufacturer, String fromDate, String toDate);
	
	public ByteArrayInputStream generateReportForBookingRelevant_StockDifferences(String schema, String dataLibrary, String allowedWarehouses, 
			List<String> warehouseNos, String manufacturer, String fromDate, String toDate);

	public ByteArrayInputStream generateReportForBookingRelevant_AccountChange(String schema, String dataLibrary, String allowedWarehouses, 
			List<String> warehouseNos, String manufacturer, String fromDate, String toDate);	
	
	public GlobalSearch getPartsLabelforPrinting(String dataLibrary, String pageSize, String pageNumber,  String warehouseNo, String manufacturer, 
			boolean partNumberRangeFlag, boolean allParts, boolean allStorageLocations, String fromPartNumber, String toPartNumber, 
			String fromStorageLocation, String toStorageLocation, String companyId);	
	
	public ByteArrayInputStream getPartsLabelforPrintingCSV(String dataLibrary, String schema, String warehouseNo, String manufacturer, boolean partNumberRangeFlag, 
			boolean allParts, boolean allStorageLocations, String fromPartNumber, String toPartNumber, String fromStorageLocation, String toStorageLocation);
	
}
