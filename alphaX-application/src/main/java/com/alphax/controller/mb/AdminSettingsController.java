package com.alphax.controller.mb;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alphax.common.exception.ExceptionMessages;
import com.alphax.common.response.AlphaXResponse;
import com.alphax.service.mb.AdminSettings;
import com.alphax.api.mb.AdminSettingsApi;
import com.alphax.common.constants.AlphaXCommonUtils;
import com.alphax.util.DecryptTokenUtils;
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

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
@Validated
public class AdminSettingsController implements AdminSettingsApi {

	@Autowired
	AlphaXCommonUtils commonUtils;
	
	@Autowired
	AdminSettings adminSetting;
	
	@Autowired
	DecryptTokenUtils decryptUtil;
	
	
	/**
	 * This method is used for get (FIRMA) Company List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getCompanyList(){
		
		log.info("AdminSettingsController : Start getCompanyList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<AdminCompanysDTO> companyList = adminSetting.getCompanyList(decryptUtil.getSchema(), decryptUtil.getDataLibrary());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(companyList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for get (FIRMA) Company Details
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getCompanyDetails(@PathVariable(value = "id", required = true) String id ) {
		
		log.info("AdminSettingsController : Start getCompanyDetails");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		AdminCompanysDTO companyList = adminSetting.getCompanyDetails(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), id);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(companyList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used update company
	 */
	@Override
	public ResponseEntity<AlphaXResponse> updateCompanyDetails(
			@Valid @RequestBody AdminCompanysDTO adminCompanys_obj) {
		
		log.info("AdminSettingsController : Start updateCompanyDetails");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> updatedCompany = adminSetting.updateCompanyDetails(adminCompanys_obj, decryptUtil.getSchema(), 
				decryptUtil.getLogedInUser(), decryptUtil.getDataLibrary());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(updatedCompany);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for User List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getUsersList(){
		
		log.info("AdminSettingsController : Start getUsersList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<DropdownObject> usersList = adminSetting.getUserList(decryptUtil.getSchema(), decryptUtil.getDataLibrary());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(usersList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for get user Details
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getUserDetailList(@PathVariable(value = "id", required = true) String id ) {
		
		log.info("AdminSettingsController : Start getUserDetailList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		AdminUsersDTO userDetails = adminSetting.getUserDetailList(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), id);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(userDetails);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for update user 
	 */
	@Override
	public ResponseEntity<AlphaXResponse> updateUserDetails(
			@Valid @RequestBody AdminUsersDTO adminUser_obj) {
		
		log.info("AdminSettingsController : Start updateUserDetails");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> updatedUser = adminSetting.updateUserDetails(adminUser_obj, decryptUtil.getSchema(), decryptUtil.getLogedInUser(), 
				decryptUtil.getDataLibrary());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(updatedUser);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for role List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getRoleList(){
		
		log.info("AdminSettingsController : Start getRoleList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<DropdownObject> rolesList = adminSetting.getRoleList(decryptUtil.getSchema(), decryptUtil.getDataLibrary());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(rolesList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for get role Details
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getRoleDetailList(@PathVariable(value = "id", required = true) String id ) {
		
		log.info("AdminSettingsController : Start getRoleDetailList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		AdminRolesDTO roleDetails = adminSetting.getRoleDetailList(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), id);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(roleDetails);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	/**
	 * This method is used for update role 
	 */
	@Override
	public ResponseEntity<AlphaXResponse> updateRoleDetails(
			@Valid @RequestBody AdminRolesDTO adminRole_obj) {
		
		log.info("AdminSettingsController : Start updateRoleDetails");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> updatedRole = adminSetting.updateRoleDetails(adminRole_obj, decryptUtil.getSchema(), decryptUtil.getLogedInUser(), 
				decryptUtil.getDataLibrary());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(updatedRole);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for module List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getModuleList(){
		
		log.info("AdminSettingsController : Start getModuleList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<DropdownObject> moduleList = adminSetting.getModuleList(decryptUtil.getSchema(), decryptUtil.getDataLibrary());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(moduleList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for get Module Details
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getModuleDetailList(@PathVariable(value = "id", required = true) String id ) {
		
		log.info("AdminSettingsController : Start getModuleDetailList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		AdminModuleDTO moduleDetails = adminSetting.getModuleDetailList(decryptUtil.getSchema(),decryptUtil.getDataLibrary(), id);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(moduleDetails);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for update module 
	 */	
	@Override
	public ResponseEntity<AlphaXResponse> updateModuleDetails(
			@Valid @RequestBody AdminModuleDTO adminModule_obj) {
		
		log.info("AdminSettingsController : Start updateRoleDetails");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		AdminModuleDTO updatedModule = adminSetting.updateModuleDetails(adminModule_obj, decryptUtil.getSchema(), decryptUtil.getLogedInUser(), 
				decryptUtil.getDataLibrary());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(updatedModule);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for get agency List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getAgencyList(){
		
		log.info("AdminSettingsController : Start getAgencyList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<DropdownObject> agencyList = adminSetting.getAgencyList(decryptUtil.getSchema(), decryptUtil.getDataLibrary());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(agencyList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for delete (FIRMA) Company Details
	 */
	@Override
	public ResponseEntity<AlphaXResponse> deleteCompanyDetails(@PathVariable(value = "id", required = true) String id ) {
		
		log.info("deleteCompanyDetails : Start getCompanyDetails");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> company_obj = adminSetting.deleteCompanyDetails(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), id, decryptUtil.getLogedInUser());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(company_obj);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for create (FIRMA) Company Details
	 */
	@Override
	public ResponseEntity<AlphaXResponse> createNewCompany(
			@Valid @RequestBody AdminCompanysDTO adminCompanys_obj) {
		
		log.info("AdminSettingsController : Start createNewCompany");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		AdminCompanysDTO createCompany = adminSetting.createNewCompany(adminCompanys_obj, decryptUtil.getSchema(), decryptUtil.getDataLibrary(), 
				decryptUtil.getLogedInUser());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(createCompany);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for create User Details
	 */
	@Override
	public ResponseEntity<AlphaXResponse> createNewUser(
			@Valid @RequestBody AdminUsersDTO adminUser_obj) {
		
		log.info("AdminSettingsController : Start createNewUser");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		AdminUsersDTO createUser = adminSetting.createNewUser(adminUser_obj, decryptUtil.getSchema(), decryptUtil.getLogedInUser(), decryptUtil.getDataLibrary());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(createUser);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for create Role Details
	 */
	@Override
	public ResponseEntity<AlphaXResponse> createNewRole(
			@Valid @RequestBody AdminRolesDTO adminRole_obj) {
		
		log.info("AdminSettingsController : Start createNewRole");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		AdminRolesDTO createRole = adminSetting.createNewRole(adminRole_obj, decryptUtil.getSchema(), decryptUtil.getLogedInUser(), decryptUtil.getDataLibrary());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(createRole);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for delete user Details
	 */
	@Override
	public ResponseEntity<AlphaXResponse> deleteUserDetails(@PathVariable(value = "id", required = true) String id ) {
		
		log.info("deleteUserDetails : Start getCompanyDetails");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> user_obj = adminSetting.deleteUserDetails(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), id, decryptUtil.getLogedInUser());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(user_obj);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for delete Role Details
	 */
	@Override
	public ResponseEntity<AlphaXResponse> deleteRoleDetails(@PathVariable(value = "id", required = true) String id ) {
		
		log.info("deleteRoleDetails : Start getCompanyDetails");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> role_Obj = adminSetting.deleteRoleDetails(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), id, decryptUtil.getLogedInUser());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(role_Obj);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	

	/**
	 * This method is used to get Login User List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getLoginUsersList(
			@NotBlank @RequestParam(value = "searchString", required = true) String searchString,
			@RequestParam(value = "pageSize", required = false) String pageSize, 
			@RequestParam(value = "pageNumber", required = false) String pageNumber){
		
		log.info("AdminSettingsController : Start getLoginUsersList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		GlobalSearch loginUsersList = adminSetting.getLoginUserList(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), searchString, pageSize, pageNumber);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(loginUsersList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for update user role and agency mapping
	 */	
	@Override
	public ResponseEntity<AlphaXResponse> updateAgencyRoleAndUserMapping(
			@Valid @RequestBody List<Admin_UserRoleMappingDTO> admin_UserRoleMappingDTO,
			@NotBlank @RequestParam(value = "roleId", required = true) String roleId,
			@NotBlank @RequestParam(value = "agencyId", required = true) String agencyId){	 
		
		log.info("AdminSettingsController : Start updateAgencyRoleAndUserMapping");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> updatedMapping = adminSetting.updateAgencyRoleAndUserMapping(admin_UserRoleMappingDTO, decryptUtil.getSchema(), decryptUtil.getDataLibrary(), 
				agencyId, roleId, decryptUtil.getLogedInUser());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(updatedMapping);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for update Agency Warehouse mapping
	 */
	@Override
	public ResponseEntity<AlphaXResponse> updateAgencyWarehousMapping(
			@Valid @RequestBody List<Admin_AgencyWarehouseMappingDTO> agencyWarehouseMappingDTOs,
			@NotBlank @RequestParam(value = "agencyId", required = true) String agencyId){
		
		log.info("AdminSettingsController : Start updateAgencyWarehousMapping");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> updatedMapping = adminSetting.updateAgencyWarehousMapping(agencyWarehouseMappingDTOs, decryptUtil.getSchema(), decryptUtil.getDataLibrary(), 
				agencyId, decryptUtil.getLogedInUser());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(updatedMapping);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for get (Filiale) Agency Details
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getAgencyDetailsUsingId(@Pattern(regexp = "[0-9]+") @PathVariable(value = "id", required = true) String agencyId ) {
		
		log.info("AdminSettingsController : Start getAgencyDetailsUsingId");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		AdminAgencyDTO agencyDetails = adminSetting.getAgencyDetailsUsingId(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), agencyId);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(agencyDetails);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for Create new (Filiale) Agency in DB.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> createNewAgency(
			@Valid @RequestBody AdminAgencyDTO adminAgency_obj) {
		
		log.info("AdminSettingsController : Start createNewAgency");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		AdminAgencyDTO createAgency = adminSetting.createNewAgency(adminAgency_obj, decryptUtil.getSchema(), decryptUtil.getDataLibrary(), decryptUtil.getLogedInUser());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(createAgency);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for update Agency in DB
	 */
	@Override
	public ResponseEntity<AlphaXResponse> updateAgencyDetails(
			@Valid @RequestBody AdminAgencyDTO adminAgency_obj) {
		
		log.info("AdminSettingsController : Start updateAgencyDetails");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> updatedAgency = adminSetting.updateAgencyDetails(adminAgency_obj, decryptUtil.getSchema(), decryptUtil.getDataLibrary(), 
				decryptUtil.getLogedInUser());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(updatedAgency);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for delete Agency Details.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> deleteAgencyDetails(
			@PathVariable(value = "id", required = true) String id) {
		
		log.info("AdminSettingsController : Start deleteAgencyDetails");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> agency_Obj = adminSetting.deleteAgencyDetails(id, decryptUtil.getSchema(), decryptUtil.getDataLibrary(), decryptUtil.getLogedInUser());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(agency_Obj);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for get Warehouse List
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getWarehouseList(){
		
		log.info("AdminSettingsController : Start getWarehouseList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<DropdownObject> warehouseList = adminSetting.getWarehouseList(decryptUtil.getSchema(), decryptUtil.getDataLibrary());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(warehouseList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for get (Lager) Warehouse Details
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getWarehouseDetailsUsingId(@PathVariable(value = "id", required = true) String warehouseId ) {
		
		log.info("AdminSettingsController : Start getWarehouseDetailsUsingId");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		AdminWarehouseDTO warehouseDetails = adminSetting.getWarehouseDetailsUsingId(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), warehouseId);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(warehouseDetails);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for Create new (Lager) Warehouse in DB.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> createNewWarehouse(
			@Valid @RequestBody AdminWarehouseDTO adminWarehouse_obj) {
		
		log.info("AdminSettingsController : Start createNewWarehouse");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		AdminWarehouseDTO createWarehouse = adminSetting.createNewWarehouse(adminWarehouse_obj, decryptUtil.getSchema(), decryptUtil.getDataLibrary(),
				decryptUtil.getLogedInUser());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(createWarehouse);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for update Warehouse in DB
	 */
	@Override
	public ResponseEntity<AlphaXResponse> updateWarehouseDetails(
			@Valid @RequestBody AdminWarehouseDTO adminWarehouse_obj) {
		
		log.info("AdminSettingsController : Start updateWarehouseDetails");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> updatedWarehouse = adminSetting.updateWarehouseDetails(adminWarehouse_obj, decryptUtil.getSchema(), decryptUtil.getDataLibrary(), 
				decryptUtil.getLogedInUser());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(updatedWarehouse);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for delete Warehouse Details.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> deleteWarehouseDetails(
			@PathVariable(value = "id", required = true) String id) {
		
		log.info("AdminSettingsController : Start deleteWarehouseDetails");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> warehouse_Obj = adminSetting.deleteWarehouseDetails(id, decryptUtil.getSchema(), decryptUtil.getDataLibrary(), decryptUtil.getLogedInUser());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(warehouse_Obj);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used to get users list that are not assigned to any role for current agency.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getUsers_NotAssignedToAnyRoleForCurrentAgecy(
			@NotBlank @RequestParam(value = "agencyId", required = true) String agencyId,
			@NotBlank @RequestParam(value = "roleId", required = true) String roleId ) {
		
		log.info("AdminSettingsController : Start getUsers_NotAssignedToAnyRoleForCurrentAgecy");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<AdminUsersDTO> assignedUserList = adminSetting.getUsers_NotAssignedToAnyRoleForCurrentAgecy(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), 
				agencyId, roleId);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(assignedUserList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used to get users list that are assigned to selected role for current agency.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getUsers_AssignedToSelectedRoleForCurrentAgecy(
			@NotBlank @RequestParam(value = "agencyId", required = true) String agencyId,
			@NotBlank @RequestParam(value = "roleId", required = true) String roleId ){
		
		log.info("AdminSettingsController : Start getUsers_NotAssignedToAnyRoleForCurrentAgecy");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<Admin_UserRoleMappingDTO> notAssignedUserList = adminSetting.getUsers_AssignedToSelectedRoleForCurrentAgecy(decryptUtil.getSchema(), 
				decryptUtil.getDataLibrary(), agencyId, roleId);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(notAssignedUserList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used to get warehouse list that are not assigned to current agency.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getWarehouses_NotAssignedToCurrentAgecy(@NotBlank @RequestParam(value = "agencyId", required = true) String agencyId) {
		
		log.info("AdminSettingsController : Start getWarehouses_NotAssignedToCurrentAgecy");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<AdminWarehouseDTO> notAssignedWarehousesList = adminSetting.getWarehouses_NotAssignedToCurrentAgecy(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), 
				agencyId);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(notAssignedWarehousesList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used to get warehouse list that are assigned to current agency.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getWarehouses_AssignedToCurrentAgecy(@NotBlank @RequestParam(value = "agencyId", required = true) String agencyId) {
		
		log.info("AdminSettingsController : Start getWarehouses_AssignedToCurrentAgecy");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<Admin_AgencyWarehouseMappingDTO> assignedWarehousesList = adminSetting.getWarehouses_AssignedToCurrentAgecy(decryptUtil.getSchema(), 
				decryptUtil.getDataLibrary(), agencyId);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(assignedWarehousesList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for get tokens List.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getTokensList() {
		
		log.info("AdminSettingsController : Start getTokensList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		List<DropdownObject> tokenList = adminSetting.getTokensList();
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(tokenList);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for get alphax companys List.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getAlphaPlusCompanys() {
		
		log.info("AdminSettingsController : Start getAlphaPlusCompanys");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		List<DropdownObject> alphaPlusCompany = adminSetting.getAlphaPlusCompanys(decryptUtil.getDataLibrary());
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(alphaPlusCompany);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for get user setting
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getUserDefaultSetting(@PathVariable(value = "id", required = true) String id ) {
		
		log.info("AdminSettingsController : Start getUserDefaultSetting");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		AdminUserSettingDTO userSetting = adminSetting.getUserDefaultSetting(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), id);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(userSetting);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for update user setting
	 */
	@Override
	public ResponseEntity<AlphaXResponse> updateUserDefaultSetting(
			@Valid @RequestBody AdminUserSettingDTO adminUserSetting_obj) {
		
		log.info("AdminSettingsController : Start updateUserDetails");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> updatedUserSetting = adminSetting.updateUserDefaultSetting(adminUserSetting_obj, decryptUtil.getSchema(), decryptUtil.getLogedInUser(), 
				decryptUtil.getDataLibrary());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(updatedUserSetting);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used to get token value for current selected Agency.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getTokenBasedonAgency(
			@NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "agencyId", required = true) String agencyId) {
		
		log.info("AdminSettingsController : Start getTokenBasedonAgency");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, String> assignedTokenValue = adminSetting.getTokenBasedonAgency(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), agencyId);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(assignedTokenValue);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for Create new token entry in O_SETUP table.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> createUpdateTokenBasedonAgency(
			@NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "agencyId", required = true) String agencyId,
			@NotBlank @RequestParam(value = "tokenValue", required = true) String tokenValue) {
		
		log.info("AdminSettingsController : Start createUpdateTokenBasedonAgency");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> create_UpdateToken  = adminSetting.createUpdateTokenBasedonAgency(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), agencyId, 
				tokenValue, decryptUtil.getLogedInUser());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(create_UpdateToken);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used to get Agency list that are not assigned to current warehouse.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getAgencies_NotAssignedToCrntWarehouse(
			@NotBlank @Pattern(regexp = "[0-9]+") @PathVariable(value = "id", required = true) String warehouseId) {
		
		log.info("AdminSettingsController : Start getAgencies_NotAssignedToCrntWarehouse");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<AdminAgencyDTO> notAssigneAgencyList = adminSetting.getAgencies_NotAssignedToCrntWarehouse(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), warehouseId);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(notAssigneAgencyList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used to get Agency list that are assigned to current warehouse.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getAgencies_AssignedToCrntWarehouse(
			@NotBlank @Pattern(regexp = "[0-9]+") @PathVariable(value = "id", required = true) String warehouseId) {
		
		log.info("AdminSettingsController : Start getAgencies_AssignedToCrntWarehouse");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<AdminAgencyDTO> assignedAgencyList = adminSetting.getAgencies_AssignedToCrntWarehouse(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), warehouseId);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(assignedAgencyList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for create / update multiple Agencies mapping with Warehouse.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> createAgenciesMappingWithWRH(
			@Valid @RequestBody List<AdminAgencyDTO> agencyDTOList,
			@NotBlank @Pattern(regexp = "[0-9]+") @PathVariable(value = "id", required = true) String warehouseId ){	 
		
		log.info("AdminSettingsController : Start createAgenciesMappingWithWRH");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> updatedMapping = adminSetting.createAgenciesMappingWithWRH(agencyDTOList, decryptUtil.getSchema(), decryptUtil.getDataLibrary(), 
				warehouseId, decryptUtil.getLogedInUser());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(updatedMapping);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used to get Settings for the selected Roles.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getRoleSettingForModules(
			@NotBlank @Pattern(regexp = "[0-9]+") @PathVariable(value = "id", required = true) String roleId,
			@NotBlank @RequestParam(value = "moduleName", required = true) String moduleName) {
		
		log.info("AdminSettingsController : Start getRoleSettingForSearch");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		List<AdminRoleSettingsDTO> rolesSettingsList = adminSetting.getRoleSettingForModules(decryptUtil.getSchema(), decryptUtil.getDataLibrary(), roleId, moduleName);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(rolesSettingsList);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for create / update Settings for the selected Roles in O_SETUP Table.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> updateRoleSettingsForModules(
			@Valid @RequestBody List<AdminRoleSettingsDTO> settingsDTOList,
			@NotBlank @Pattern(regexp = "[0-9]+") @PathVariable(value = "id", required = true) String roleId){	 
		
		log.info("AdminSettingsController : Start createRoleSettingForSearch");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		Map<String, Boolean> updatedMapping = adminSetting.updateRoleSettingsForModules(settingsDTOList, decryptUtil.getSchema(), decryptUtil.getDataLibrary(), 
				roleId, decryptUtil.getLogedInUser());

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(updatedMapping);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This is AdminSettingsController method used to get the Alpha-Plus Lager (Warehouse) details List for DDL
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getAlphaPlusWarehouseList(
			@Pattern(regexp = "[0-9]+") @RequestParam(value = "warehouseId", required = false) String warehouseId ) {
		
		log.info("AdminSettingsController : Start getAlphaPlusWarehouseList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		List<LagerDetailsDTO> lagerList = adminSetting.getAlphaPlusWarehouseList(decryptUtil.getSchema(),decryptUtil.getDataLibrary(), decryptUtil.getApCompanyId(), 
				decryptUtil.getApAgencyId(), warehouseId);
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(lagerList);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This is AdminSettingsController method used to get the Employee Role list based on selected Agency
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getEmployeeRoleList(
			@NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "agencyId", required = true) String agencyId ) {
		
		log.info("AdminSettingsController : Start getEmployeeRoleList");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();
		
		List<EmployeeRoleDTO> employeeRoleList = adminSetting.getEmployeeRoleList(decryptUtil.getSchema(), agencyId);
		
		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(), 
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(employeeRoleList);
		
		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for get Company's fiscal year end date details
	 */
	@Override
	public ResponseEntity<AlphaXResponse> getCompanyFiscalDateDetails(@PathVariable(value = "id", required = true) String id ) {
		
		log.info("AdminSettingsController : Start getCompanyFiscalDateDetails");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}
		Map<String, String> companyFiscalDate = adminSetting.getCompanyFiscalDateDetails(decryptUtil.getDataLibrary(), id, allowedApWarehouses);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(companyFiscalDate);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used for add/update fiscal year end date in DB tables E_INVPARIP and E_INVPARUP.
	 */
	@Override
	public ResponseEntity<AlphaXResponse> createUpdateFiscalDateDetails(
			@PathVariable(value = "id", required = true) String companyId,
			@NotBlank @RequestParam(value = "fiscalDate", required = true) String fiscalDate) {
		
		log.info("AdminSettingsController : Start createUpdateFiscalDateDetails");
		AlphaXResponse<Object> alphaxResponse = new AlphaXResponse<>();

		String allowedApWarehouses = decryptUtil.getAllowedApWarehouse();
		if(decryptUtil.getETCentralFunctionValue().equalsIgnoreCase("1")){
			allowedApWarehouses = decryptUtil.getCompanyApWarehouse();
		}
		
		Map<String, Boolean> updatedfiscalDate = adminSetting.createUpdateFiscalDateDetails(decryptUtil.getDataLibrary(), allowedApWarehouses, 
				companyId, fiscalDate);

		alphaxResponse.setResponseMessages(commonUtils.buildResponseMessage(alphaxResponse.getResponseMessages(),
				LocaleContextHolder.getLocale(), ExceptionMessages.SUCCESS_MESSAGE_KEY));
		alphaxResponse.setResultSet(updatedfiscalDate);

		return new ResponseEntity<>(alphaxResponse, HttpStatus.OK);
	}
	
}