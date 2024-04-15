package com.alphax.service.mb;

import java.util.List;
import java.util.Map;

import com.alphax.vo.mb.AccessesBA_DTO;
import com.alphax.vo.mb.BusinessCases25DTO;
import com.alphax.vo.mb.BusinessCases49DTO;
import com.alphax.vo.mb.BusinessCasesDTO;
import com.alphax.vo.mb.DeliveryNotes_BA_DTO;
import com.alphax.vo.mb.DeparturesBA_DTO;
import com.alphax.vo.mb.DropdownObject;
import com.alphax.vo.mb.FinalizationsBA_DTO;
import com.alphax.vo.mb.GlobalSearch;
import com.alphax.vo.mb.LagerDetailsDTO;
import com.alphax.vo.mb.MasterDataBA_DTO;
import com.alphax.vo.mb.RebookingBundlesBA_DTO;

public interface BusinessCases {
	
	public Map<String, String> updateFinalizationsBA(FinalizationsBA_DTO finalizationsBA, String dataLibrary, String schema, String companyId, String agencyId );

	public Map<String, String> updateAccessesBA(AccessesBA_DTO accessesBA, String dataLibrary, String companyId, String agencyId );
	
	public Map<String, String> updateDeparturesBA(DeparturesBA_DTO  departuresBA_Obj, String dataLibrary, String companyId, String agencyId );
	
	public Map<String, String> updateMasterDataBA(MasterDataBA_DTO  masterDataBA_Obj, String dataLibrary, String companyId, String agencyId );
	
	public FinalizationsBA_DTO getFinalizationsBADetails(String dataLibrary, String companyId, String agencyId,
			String manufacturer,String warehouseNumber,String partNumber, String businessCase  );

	public AccessesBA_DTO getAccessesBADetails(String schema, String dataLibrary, String companyId, String agencyId,
			String manufacturer,String warehouseNumber,String partNumber,String businessCase);
	
	public GlobalSearch getDeliveryNoteList(String dataLibrary, String companyId, String agencyId,
			String manufacturer,String warehouseNumber,String supplierNumber, String searchString, String pageSize, String pageNumber);
	
	public List<LagerDetailsDTO> getLagerList(String dataLibrary, String companyId, String agencyId, String allowedWarehouses, String schema );
	
	public GlobalSearch getSupplierList(String dataLibrary, String companyId, String agencyId);
	
	public AccessesBA_DTO getAccessesBA06Details(String schema, String dataLibrary, String companyId, String agencyId,
			String manufacturer,String warehouseNumber,String partNumber,String businessCase,String supplierNo, String deliveryNoteNo);
	
	public DeparturesBA_DTO getDeparturesBADetails(String dataLibrary, String companyId, String agencyId,
			String manufacturer, String warehouseNumber, String partNumber, String businessCase);
	
	public MasterDataBA_DTO getMasterDataBADetails(String dataLibrary, String schema, String companyId, String agencyId,
			String manufacturer, String warehouseNumber, String partNumber, String businessCase);
	
	public DeparturesBA_DTO getDeparturesBA25Details(String dataLibrary, String companyId, String agencyId,
			String manufacturer, String warehouseNumber, String partNumber, String supplierNo, String deliveryNoteNo, String processCode, String businessCase);
	public List<DropdownObject> getVATRegistrationNumber(String schema, String dataLibrary);
	
	public List<DropdownObject> getCustomerGroupList(String businessCase);
	
	public Map<String, String> updateRebookingBundlesBA(RebookingBundlesBA_DTO rebookingBundlesBA,String dataLibrary, String companyId, String agencyId );

	public DeparturesBA_DTO getRebookingBundleDetails(String dataLibrary, String companyId, String agencyId,String manufacturer, String warehouseNumber, String partNumber);

	public GlobalSearch getSupplierListUsingSearchString(String dataLibrary, String companyId, String agencyId,String searchString);
	
	public List<BusinessCasesDTO> getActiveBA(String schema);
	
	public Map<String, String> getDefaultPrinter(String dataLibrary, String warehouseNumber, Integer flag);
	
	public List<LagerDetailsDTO> getComapnyBasedLagerList(String dataLibrary, String schema, String axCompanyId, String companyApWarehouses );
	
	public Map<String, Boolean> updateDeliveryNoteStatus(DeliveryNotes_BA_DTO  ba_dto, String dataLibrary);
	
	public Map<String, String> newJavaImplementation_BA49(BusinessCases49DTO obj_ba49, String dataLibrary, 
			String schema, String companyId, String agencyId, String loginUserName);
	
	public Map<String, Boolean> newJavaImplementation_BA25(BusinessCases25DTO  ba25_obj, String dataLibrary, String schema,  
			String companyId, String agencyId, String loginUserName, Map<String, String> output_bsn475 );
	
	public Map<String, String> newJavaImplementation_BA06(BusinessCases25DTO  ba25_obj, String dataLibrary, String schema,  
			String companyId, String agencyId, String loginUserName, Map<String, String> output_bsn475 );
	
	public Map<String, Boolean> newJavaImplementation_BA50(FinalizationsBA_DTO  ba50_obj, String dataLibrary, String schema,  
			String companyId, String agencyId, String loginUserName );
	
	public Map<String, Boolean> newJavaImplementation_BA17(AccessesBA_DTO accessesBA, String dataLibrary,String schema,
			String companyId, String agencyId, String loginUserName );
	
	public Map<String, Boolean> partsCleanupUsingBA50(String warehouseNumber, String toDate, String dataLibrary, String schema,  
			String companyId, String agencyId, String loginUserName );
	
	public AccessesBA_DTO getDetailsFor_BA17(String schema, String dataLibrary,String manufacturer,String partNumber, String allowedWarehouses);
	
	public Map<String, Boolean> createPartInMultipleWarehouses(String dataLibrary, String schema, 
			String partNumber, String manufacturer, String warehouseNumbers, String vatRegistrationNumber, String companyId, String agencyId, String loginUserName);
	
	public Map<String, String> newJavaImplementation_BSN475( String partBrand,String dataLibrary, String schema, String manufacturer, 
			String marketingCode, String discountGroup, String partNumber);
	
	public Map<String, String> newJavaImplementation_BSN477(String dataLibrary, String schema, String manufacturer, 
			String marketingCode, String discountGroup,String partBrand, String partNumber);
}
