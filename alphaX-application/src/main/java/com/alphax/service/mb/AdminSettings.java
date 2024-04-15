package com.alphax.service.mb;

import java.util.List;
import java.util.Map;

import com.alphax.vo.mb.AdminAgencyDTO;
import com.alphax.vo.mb.AdminCompanysDTO;
import com.alphax.vo.mb.AdminModuleDTO;
import com.alphax.vo.mb.AdminRoleSettingsDTO;
import com.alphax.vo.mb.AdminRolesDTO;
import com.alphax.vo.mb.AdminUserSettingDTO;
import com.alphax.vo.mb.AdminUsersDTO;
import com.alphax.vo.mb.AdminWarehouseDTO;
import com.alphax.vo.mb.Admin_AgencyWarehouseMappingDTO;
import com.alphax.vo.mb.Admin_UserRoleMappingDTO;
import com.alphax.vo.mb.DropdownObject;
import com.alphax.vo.mb.EmployeeRoleDTO;
import com.alphax.vo.mb.GlobalSearch;
import com.alphax.vo.mb.LagerDetailsDTO;

public interface AdminSettings {

	public List<AdminCompanysDTO> getCompanyList(String schema, String dataLibrary);

	public AdminCompanysDTO getCompanyDetails(String schema, String dataLibrary, String companyId);

	public Map<String, Boolean> updateCompanyDetails(AdminCompanysDTO adminCompanys_obj, String schema, String loginName, String dataLibrary);
	
	public List<DropdownObject> getUserList(String schema, String dataLibrary);
	
	public AdminUsersDTO getUserDetailList(String schema, String dataLibrary,String userId);
	
	public Map<String, Boolean> updateUserDetails(AdminUsersDTO adminUser_obj, String schema, String loginName, String dataLibrary);
	
	public List<DropdownObject> getRoleList(String schema, String dataLibrary);
	
	public AdminRolesDTO getRoleDetailList(String schema, String dataLibrary,String roleId);
	
	public Map<String, Boolean> updateRoleDetails(AdminRolesDTO adminRole_obj, String schema, String loginName, String dataLibrary);
	
	public List<DropdownObject> getModuleList(String schema, String dataLibrary);
	
	public AdminModuleDTO getModuleDetailList(String schema, String dataLibrary,String moduleId);
	
	public AdminModuleDTO updateModuleDetails(AdminModuleDTO adminModule_obj, String schema, String loginName, String dataLibrary);
	
	public List<DropdownObject> getAgencyList(String schema, String dataLibrary);
	
	public Map<String, Boolean> deleteCompanyDetails(String schema, String dataLibrary,String companyId, String userLoginName);
	public AdminCompanysDTO createNewCompany(AdminCompanysDTO adminCompanys_obj,String schema, String dataLibrary,String loginUserName);
	
	public AdminUsersDTO createNewUser(AdminUsersDTO adminUser_obj, String schema, String loginUserName, String dataLibrary);
	public AdminRolesDTO createNewRole(AdminRolesDTO adminRole_obj, String schema, String loginUserName, String dataLibrary);
	
	public Map<String, Boolean> deleteRoleDetails(String schema, String dataLibrary,String roleId, String userLoginName);
	public Map<String, Boolean> deleteUserDetails(String schema, String dataLibrary,String userId, String userLoginName);
	public GlobalSearch getLoginUserList(String schema, String dataLibrary,String searchText, String pageSize, String pageNumber);
	public Map<String, Boolean> updateAgencyRoleAndUserMapping(List<Admin_UserRoleMappingDTO> mapping_DTO, String schema, String dataLibrary,String agencyId,String roleId,String userLoginName);
	public Map<String, Boolean> updateAgencyWarehousMapping(List<Admin_AgencyWarehouseMappingDTO> mapping_DTO, String schema, String dataLibrary,String agencyId,String userLoginName);
	
	public AdminAgencyDTO getAgencyDetailsUsingId(String schema, String dataLibrary, String agencyId);
	public AdminAgencyDTO createNewAgency(AdminAgencyDTO adminAgency_obj, String schema, String dataLibrary, String loginUserName);
	public Map<String, Boolean> updateAgencyDetails(AdminAgencyDTO adminAgency_obj, String schema, String dataLibrary, String loginName);
	public Map<String, Boolean> deleteAgencyDetails(String agencyId, String schema, String dataLibrary, String loginName);
	
	public List<DropdownObject> getWarehouseList(String schema, String dataLibrary);
	public AdminWarehouseDTO getWarehouseDetailsUsingId(String schema, String dataLibrary, String warehouseId);
	public AdminWarehouseDTO createNewWarehouse(AdminWarehouseDTO adminWarehouse_obj, String schema, String dataLibrary, String loginUserName);
	public Map<String, Boolean> updateWarehouseDetails(AdminWarehouseDTO adminWarehouse_obj, String schema, String dataLibrary, String loginName);
	public Map<String, Boolean> deleteWarehouseDetails(String warehouseId, String schema, String dataLibrary, String loginName);
	public List<AdminUsersDTO> getUsers_NotAssignedToAnyRoleForCurrentAgecy(String schema, String dataLibrary,String agencyId, String roleId);
	public List<Admin_UserRoleMappingDTO> getUsers_AssignedToSelectedRoleForCurrentAgecy(String schema, String dataLibrary,String agencyId,String roleId);
	public List<AdminWarehouseDTO> getWarehouses_NotAssignedToCurrentAgecy(String schema, String dataLibrary,String agencyId);
	public List<Admin_AgencyWarehouseMappingDTO> getWarehouses_AssignedToCurrentAgecy(String schema, String dataLibrary,String agencyId);
	
	public List<DropdownObject> getTokensList();
	
	public List<DropdownObject> getAlphaPlusCompanys(String dataLibrary);
	
	public AdminUserSettingDTO getUserDefaultSetting(String schema, String dataLibrary,String userId);
	public Map<String, Boolean> updateUserDefaultSetting(AdminUserSettingDTO adminUserSetting_obj, String schema, String loginName, String dataLibrary);
	
	public Map<String, String> getTokenBasedonAgency(String schema, String dataLibrary,String agencyId);
	public Map<String, Boolean> createUpdateTokenBasedonAgency(String schema, String dataLibrary, String agencyId, String tokenValue, String loginName);
	
	public List<AdminAgencyDTO> getAgencies_NotAssignedToCrntWarehouse(String schema, String dataLibrary, String warehouseId);
	public List<AdminAgencyDTO> getAgencies_AssignedToCrntWarehouse(String schema, String dataLibrary, String warehouseId);
	public Map<String, Boolean> createAgenciesMappingWithWRH(List<AdminAgencyDTO> agencyDTOList, String schema, String dataLibrary, String warehouseId, String loginName);
	
	public List<AdminRoleSettingsDTO> getRoleSettingForModules(String schema, String dataLibrary, String roleId, String moduleName);
	public Map<String, Boolean> updateRoleSettingsForModules(List<AdminRoleSettingsDTO> settingsDTOList, String schema, String dataLibrary, String roleId, String loginName);
	
	public List<LagerDetailsDTO> getAlphaPlusWarehouseList(String schema, String dataLibrary, String companyId, String agencyId, String warehouseId );
	
	public List<EmployeeRoleDTO> getEmployeeRoleList(String schema, String agencyId );
	
	public Map<String, String> getCompanyFiscalDateDetails(String dataLibrary, String companyId, String allowedWarehouses);
	
	public Map<String, Boolean> createUpdateFiscalDateDetails(String dataLibrary, String allowedWarehouses, String companyId, String fiscalDate);
	
}
