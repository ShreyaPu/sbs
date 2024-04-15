package com.alphax.api.mb;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.alphax.common.response.AlphaXResponse;
import com.alphax.util.RestURIConstants;
import com.alphax.vo.mb.AdminAgencyDTO;
import com.alphax.vo.mb.AdminCompanysDTO;
import com.alphax.vo.mb.AdminLoginUsersDTO;
import com.alphax.vo.mb.AdminModuleDTO;
import com.alphax.vo.mb.AdminRoleSettingsDTO;
import com.alphax.vo.mb.AdminRolesDTO;
import com.alphax.vo.mb.AdminUserSettingDTO;
import com.alphax.vo.mb.AdminUsersDTO;
import com.alphax.vo.mb.AdminWarehouseDTO;
import com.alphax.vo.mb.Admin_AgencyWarehouseMappingDTO;
import com.alphax.vo.mb.Admin_UserRoleMappingDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

/**
 * @author A87740971
 *
 */
@Api(value = "ALPHAX",  tags = {"AdminSettings"} )
@SwaggerDefinition(tags = {
		@Tag(name = "AdminSettings", description = "REST controller that provides all the information about Admin setting.")
})
public interface AdminSettingsApi {

	@ApiOperation(value = "Returns a list of Companys", notes = "Returns a list of Companys based on datalibrary for admin setting", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Company List for admin setting", response = AdminCompanysDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Company list for admin setting due to server Error")
	})
	@GetMapping( value = RestURIConstants.ADMIN_COMPANY, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getCompanyList();
	

	@ApiOperation(value = "Returns a details of Companys", notes = "Returns a details of Companys based on datalibrary for admin setting", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Company details for admin setting", response = AdminCompanysDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Company details for admin setting due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_ADMIN_COMPANY_DETAILS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getCompanyDetails(
			@ApiParam(value = "Admin Company Id", required = true) @PathVariable(value = "id", required = true) String id );
	

	@ApiOperation(value = "Update Companys details", notes = "Update Companys details based on company Id", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Update Company details for admin setting", response = AdminCompanysDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to update Company details for admin setting due to server Error")
	})
	@PutMapping( value = RestURIConstants.ADMIN_COMPANY, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> updateCompanyDetails(
			@ApiParam(value = "Object of Company details for admin setting", required = true) @Valid @RequestBody AdminCompanysDTO adminCompanys_obj);
	

	@ApiOperation(value = "Returns a list of Users", notes = "Returns a list of Users based on datalibrary for admin setting", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Users List for admin setting", response = AdminUsersDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Users list for admin setting due to server Error")
	})
	@GetMapping( value = RestURIConstants.ADMIN_USERS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getUsersList();
	

	@ApiOperation(value = "Returns a details of User", notes = "Returns a details of User based on datalibrary for admin setting", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get User details for admin setting", response = AdminUsersDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get User details for admin setting due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_ADMIN_USER_DETAILS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getUserDetailList(
			@ApiParam(value = "User Id", required = true) @PathVariable(value = "id", required = true) String id );
	
	
	@ApiOperation(value = "Update user details", notes = "Update user details based on user Id", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Update user details for admin setting", response = AdminUsersDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to update user details for admin setting due to server Error")
	})
	@PutMapping( value = RestURIConstants.ADMIN_USERS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> updateUserDetails(
			@ApiParam(value = "Object of user details for admin setting", required = true) @Valid @RequestBody AdminUsersDTO adminUser_obj);

	
	@ApiOperation(value = "Returns a list of Roles", notes = "Returns a list of roles based on datalibrary for admin setting", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get roles List for admin setting", response = AdminRolesDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get roles list for admin setting due to server Error")
	})
	@GetMapping( value = RestURIConstants.ADMIN_ROLES, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getRoleList();
	

	@ApiOperation(value = "Returns a details of role", notes = "Returns a details of role based on datalibrary for admin setting", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get role details for admin setting", response = AdminRolesDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get role details for admin setting due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_ADMIN_ROLE_DETAILS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getRoleDetailList(
			@ApiParam(value = "Role Id", required = true) @PathVariable(value = "id", required = true) String id );
	
	
	@ApiOperation(value = "Update role details", notes = "Update role details based on role Id", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Update role details for admin setting", response = AdminRolesDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to update role details for admin setting due to server Error")
	})
	@PutMapping( value = RestURIConstants.ADMIN_ROLES, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> updateRoleDetails(
			@ApiParam(value = "Object of role details for admin setting", required = true) @Valid @RequestBody AdminRolesDTO adminRole_obj);

	
	@ApiOperation(value = "Returns a list of module", notes = "Returns a list of module based on datalibrary for admin setting", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get module List for admin setting", response = AdminModuleDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get module list for admin setting due to server Error")
	})
	@GetMapping( value = RestURIConstants.ADMIN_MODULE, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getModuleList();
	

	@ApiOperation(value = "Returns a details of module", notes = "Returns a details of module based on datalibrary for admin setting", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get module details for admin setting", response = AdminModuleDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get module details for admin setting due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_ADMIN_MODULE_DETAILS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getModuleDetailList(
			@ApiParam(value = "Module Id", required = true) @PathVariable(value = "id", required = true) String id );
	
	
	@ApiOperation(value = "Update module details", notes = "Update module details based on module Id", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Update module details for admin setting", response = AdminModuleDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to update module details for admin setting due to server Error")
	})
	@PutMapping( value = RestURIConstants.ADMIN_MODULE, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> updateModuleDetails(
			@ApiParam(value = "Object of module details for admin setting", required = true) @Valid @RequestBody AdminModuleDTO adminModule_obj);
	

	@ApiOperation(value = "Returns a list of Agency", notes = "Returns a list of Agency based on datalibrary for admin setting", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Agency List for admin setting", response = AdminAgencyDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Agency list for admin setting due to server Error")
	})
	@GetMapping( value = RestURIConstants.ADMIN_AGENCY, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getAgencyList();
	

	@ApiOperation(value = "delete a details of Companys", notes = "delete a details of Companys based on datalibrary for admin setting", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Delete Company details for admin setting", response = AdminCompanysDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to delete Company details for admin setting due to server Error")
	})
	@DeleteMapping( value = RestURIConstants.GET_ADMIN_COMPANY_DETAILS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> deleteCompanyDetails(
			@ApiParam(value = "Admin Company Id", required = true) @PathVariable(value = "id", required = true) String id );
	

	@ApiOperation(value = "Create New Company details", notes = "Create New Company details", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Create New Company for admin setting", response = AdminCompanysDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Create New Company for admin setting due to server Error")
	})
	@PostMapping( value = RestURIConstants.ADMIN_COMPANY, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> createNewCompany(
			@ApiParam(value = "Object of New Company details for admin setting", required = true) @Valid @RequestBody AdminCompanysDTO adminCompanys_obj);

	
	@ApiOperation(value = "Create New User details", notes = "Create New User details", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Create New User for admin setting", response = AdminUsersDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Create New User for admin setting due to server Error")
	})
	@PostMapping( value = RestURIConstants.ADMIN_USERS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> createNewUser(
			@ApiParam(value = "Object of New User details for admin setting", required = true) @Valid @RequestBody AdminUsersDTO adminUser_obj);
	
	
	@ApiOperation(value = "Create New Role details", notes = "Create New Role details", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Create New Role for admin setting", response = AdminRolesDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Create New Role for admin setting due to server Error")
	})
	@PostMapping( value = RestURIConstants.ADMIN_ROLES, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> createNewRole(
			@ApiParam(value = "Object of New Role details for admin setting", required = true) @Valid @RequestBody AdminRolesDTO adminRole_obj);
	

	@ApiOperation(value = "delete a details of user", notes = "delete a details of user based on datalibrary for admin setting", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Delete user details for admin setting", response = AdminUsersDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to delete user details for admin setting due to server Error")
	})
	@DeleteMapping( value = RestURIConstants.GET_ADMIN_USER_DETAILS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> deleteUserDetails(
			@ApiParam(value = "Admin User Id", required = true) @PathVariable(value = "id", required = true) String id );
	
	
	@ApiOperation(value = "delete a details of role", notes = "delete a details of role based on datalibrary for admin setting", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Delete role details for admin setting", response = AdminRolesDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to delete role details for admin setting due to server Error")
	})
	@DeleteMapping( value = RestURIConstants.GET_ADMIN_ROLE_DETAILS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> deleteRoleDetails(
			@ApiParam(value = "Admin Role Id", required = true) @PathVariable(value = "id", required = true) String id );
	

	@ApiOperation(value = "Returns a list of Login users", notes = "Returns a list of Login users based on datalibrary for admin setting", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Login Users List for admin setting", response = AdminLoginUsersDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Login Users list for admin setting due to server Error")
	})
	@GetMapping( value = RestURIConstants.ADMIN_LOGIN_USERS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getLoginUsersList(
			@ApiParam(value = "User Login Name", required = true) @NotBlank @RequestParam(value = "searchString", required = true) String searchString,
			@ApiParam(value = "Number of records in each page", required = false)  @RequestParam(value = "pageSize", required = false) String pageSize, 
			@ApiParam(value = "Page Number to show", required = false)  @RequestParam(value = "pageNumber", required = false) String pageNumber);
	

	@ApiOperation(value = "Create User Role and Agency Mapping", notes = "Create User Role and Agency Mapping for admin setting", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Create User Role and Agency Mapping for admin setting", response = Admin_UserRoleMappingDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Create User Role and Agency Mapping for admin setting due to server Error")
	})
	@PostMapping( value = RestURIConstants.USER_ROLE_AND_AGENCY_MAPPING, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> updateAgencyRoleAndUserMapping(
			@ApiParam(value = "Object of User, Role and Agency Mapping for admin setting", required = true) @Valid @RequestBody List<Admin_UserRoleMappingDTO> admin_UserRoleMappingDTO,
			@ApiParam(value = "Role Id", required = true) @NotBlank @RequestParam(value = "roleId", required = true) String roleId,
			@ApiParam(value = "Agency Id", required = true) @NotBlank @RequestParam(value = "agencyId", required = true) String agencyId);
	
	
	@ApiOperation(value = "Create Agency Warehouse Mapping", notes = "Create Agency Warehouse Mapping for admin setting", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Create Agency Warehouse Mapping for admin setting", response = Admin_AgencyWarehouseMappingDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Create Agency Warehouse Mapping for admin setting due to server Error")
	})
	@PostMapping( value = RestURIConstants.AGENCY_WAREHOUSE_MAPPING, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> updateAgencyWarehousMapping(
			@ApiParam(value = "Object of Agency Warehouse Mapping for admin setting", required = true) @Valid @RequestBody List<Admin_AgencyWarehouseMappingDTO> agencyWarehouseMappingDTOs,
			@ApiParam(value = "Agency Id", required = true) @NotBlank @RequestParam(value = "agencyId", required = true) String agencyId);

	
	@ApiOperation(value = "Returns a details of Agency using provided AgencyId", notes = "Returns a details of Agency based on AgencyId", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Agency details for admin settings", response = AdminAgencyDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Agency details for admin setting due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_ADMIN_AGENCY_DETAILS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getAgencyDetailsUsingId(
			@ApiParam(value = "Admin Agency Id", required = true) @Pattern(regexp = "[0-9]+") @PathVariable(value = "id", required = true) String id );
	
	
	@ApiOperation(value = "Create new Agency details", notes = "Create new Agency details", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Create new Agency for admin setting", response = AdminAgencyDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Create new Agency for admin setting due to server Error")
	})
	@PostMapping( value = RestURIConstants.ADMIN_AGENCY, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> createNewAgency(
			@ApiParam(value = "Object of new Agency details for admin setting", required = true) @Valid @RequestBody AdminAgencyDTO adminAgency_obj);
	
	
	@ApiOperation(value = "Update Agency details", notes = "Update Agency details based on Agency Id", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Update Agency details for admin setting", response = AdminAgencyDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to update Agency details for admin setting due to server Error")
	})
	@PutMapping( value = RestURIConstants.ADMIN_AGENCY, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> updateAgencyDetails(
			@ApiParam(value = "Object of Agency details for admin setting", required = true) @Valid @RequestBody AdminAgencyDTO adminAgency_obj);
	
	
	@ApiOperation(value = "delete a details of agency", notes = "delete a details of agency for admin setting", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Delete agency details for admin setting", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to delete agency details for admin setting due to server Error")
	})
	@DeleteMapping( value = RestURIConstants.GET_ADMIN_AGENCY_DETAILS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> deleteAgencyDetails(
			@ApiParam(value = "Admin Agency Id", required = true) @PathVariable(value = "id", required = true) String id);
	
	
	@ApiOperation(value = "Returns a list of Warehouse", notes = "Returns a list of Warehouse based on datalibrary for admin setting", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Warehouse List for admin setting", response = AdminWarehouseDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Warehouse list for admin setting due to server Error")
	})
	@GetMapping( value = RestURIConstants.ADMIN_WAREHOUSE, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getWarehouseList();
	
	
	@ApiOperation(value = "Returns a details of Warehouse using provided WarehouseId", notes = "Returns a details of Warehouse based on WarehouseId", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Warehouse details for admin settings", response = AdminWarehouseDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Warehouse details for admin setting due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_ADMIN_WAREHOUSE_DETAILS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getWarehouseDetailsUsingId(
			@ApiParam(value = "Admin Warehouse Id", required = true) @PathVariable(value = "id", required = true) String id );
	
	
	@ApiOperation(value = "Create new Warehouse details", notes = "Create new Warehouse details", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Create new Warehouse for admin setting", response = AdminWarehouseDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Create new Warehouse for admin setting due to server Error")
	})
	@PostMapping( value = RestURIConstants.ADMIN_WAREHOUSE, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> createNewWarehouse(
			@ApiParam(value = "Object of new Warehouse details for admin setting", required = true) @Valid @RequestBody AdminWarehouseDTO adminWarehouse_obj);
	
	
	@ApiOperation(value = "Update Warehouse details", notes = "Update Warehouse details based on Warehouse Id", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Update Warehouse details for admin setting", response = AdminWarehouseDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to update Warehouse details for admin setting due to server Error")
	})
	@PutMapping( value = RestURIConstants.ADMIN_WAREHOUSE, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> updateWarehouseDetails(
			@ApiParam(value = "Object of Warehouse details for admin setting", required = true) @Valid @RequestBody AdminWarehouseDTO adminWarehouse_obj);
	
	
	@ApiOperation(value = "delete a details of Warehouse", notes = "delete a details of Warehouse for admin setting", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Delete Warehouse details for admin setting", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to delete Warehouse details for admin setting due to server Error")
	})
	@DeleteMapping( value = RestURIConstants.GET_ADMIN_WAREHOUSE_DETAILS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> deleteWarehouseDetails(
			@ApiParam(value = "Admin Warehouse Id", required = true) @PathVariable(value = "id", required = true) String id);
	
	
	@ApiOperation(value = "Returns a list of user that are not assigned to any role for current agency", notes = "Returns a list of user that are not assigned to any role for current agency based on datalibrary for admin setting", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get user list that are not assigned to any role for current agency for admin setting", response = AdminUsersDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get user list that are not assigned to any role for current agency for admin setting due to server Error")
	})
	@GetMapping( value = RestURIConstants.USER_NOT_ASSIGNED_ANY_ROLE_TO_CURRENT_AGENCY, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getUsers_NotAssignedToAnyRoleForCurrentAgecy(
			@ApiParam(value = "Agency Id", required = true) @NotBlank @RequestParam(value = "agencyId", required = true) String agencyId,
			@ApiParam(value = "Role Id", required = true) @NotBlank @RequestParam(value = "roleId", required = true) String roleId );
	
	
	@ApiOperation(value = "Returns a list of user that are assigned to selected role for current agency", notes = "Returns a list of user that are assigned to selected role for current agency based on datalibrary for admin setting", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get user list that are assigned to selected role for current agency for admin setting", response = Admin_UserRoleMappingDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get user list that are assigned to selected role for current agency for admin setting due to server Error")
	})
	@GetMapping( value = RestURIConstants.USER_ASSIGNED_SELECTED_ROLE_TO_CURRENT_AGENCY, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getUsers_AssignedToSelectedRoleForCurrentAgecy(
			@ApiParam(value = "Agency Id", required = true) @NotBlank @RequestParam(value = "agencyId", required = true) String agencyId,
			@ApiParam(value = "Role Id", required = true) @NotBlank @RequestParam(value = "roleId", required = true) String roleId );
	
	
	@ApiOperation(value = "Returns a list of warehouses that are not assigned to current agency", notes = "Returns a list of warehouses that are not assigned to current agency based on datalibrary for admin setting", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get warehouses list that are not assigned to current agency for admin setting", response = AdminWarehouseDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get warehouses list that are not assigned to current agency for admin setting due to server Error")
	})
	@GetMapping( value = RestURIConstants.WAREHOUSE_NOT_ASSIGNED_TO_CURRENT_AGENCY, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getWarehouses_NotAssignedToCurrentAgecy(
			@ApiParam(value = "Agency Id", required = true) @NotBlank @RequestParam(value = "agencyId", required = true) String agencyId);
	
	
	@ApiOperation(value = "Returns a list of warehouses that are assigned to current agency", notes = "Returns a list of warehouses that are assigned to current agency based on datalibrary for admin setting", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get warehouses list that are assigned to current agency for admin setting", response = Admin_AgencyWarehouseMappingDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get warehouses list that are assigned to current agency for admin setting due to server Error")
	})
	@GetMapping( value = RestURIConstants.WAREHOUSE_ASSIGNED_TO_CURRENT_AGENCY, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getWarehouses_AssignedToCurrentAgecy(
			@ApiParam(value = "Agency Id", required = true) @NotBlank @RequestParam(value = "agencyId", required = true) String agencyId);
	
	
	@ApiOperation(value = "Returns a list of tokens", notes = "This list is master data, which will be return from Stub, not required DB or COBOL call", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get tokens List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get token list due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_ADMIN_AGENCY_TOKEN_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getTokensList();
	
	
	@ApiOperation(value = "Returns a list of AlphaPlus Companys", notes = "This list is AlphaPlus Companys, which will be return from DB", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get AlphaPlus Company List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get AlphaPlus Company list due to server Error")
	})
	@GetMapping( value = RestURIConstants.ALPHAX_COMPANY, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getAlphaPlusCompanys();
	
	
	@ApiOperation(value = "Returns a default setting of User", notes = "Returns a default setting of User based on datalibrary for admin setting", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get User default setting", response = AdminUserSettingDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get User default setting due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_ADMIN_USER_DEFAULT_SETTING, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getUserDefaultSetting(
			@ApiParam(value = "User Id", required = true) @PathVariable(value = "id", required = true) String id );
	
	
	@ApiOperation(value = "Update user default setting", notes = "Update user default setting based on user Id", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Update user default setting", response = AdminUserSettingDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to update user default setting due to server Error")
	})
	@PutMapping( value = RestURIConstants.UPDATE_USER_DEFAULT_SETTING, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> updateUserDefaultSetting(
			@ApiParam(value = "Object of user details for admin setting", required = true) @Valid @RequestBody AdminUserSettingDTO adminUserSetting_obj);

	
	@ApiOperation(value = "Returns a token value for Agency", notes = "Returns a token value for Agency", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get token value", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get token value due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_ADMIN_AGENCY_TOKEN, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getTokenBasedonAgency(
			@ApiParam(value = "Agency Id", required = true) @NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "agencyId", required = true) String agencyId);
	
	
	@ApiOperation(value = "Create or Update a token value for Agency in DB", notes = "Create or Update a token value for Agency in DB", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Created or Updated a token value", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to create/update token value due to server Error")
	})
	@PutMapping( value = RestURIConstants.GET_ADMIN_AGENCY_TOKEN, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> createUpdateTokenBasedonAgency(
			@ApiParam(value = "Agency Id", required = true) @NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "agencyId", required = true) String agencyId,
			@ApiParam(value = "Token value", required = true) @NotBlank @RequestParam(value = "tokenValue", required = true) String tokenValue);
	
	
	@ApiOperation(value = "Returns a list of Agency that are not assigned to current Warehouse", notes = "Returns a list of Agency that are not assigned to current Warehouse based on datalibrary for admin setting", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Agency list that are not assigned to current Warehouses for admin setting", response = AdminWarehouseDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Agency list that are not assigned to current Warehouses for admin setting due to server Error")
	})
	@GetMapping( value = RestURIConstants.AGENCY_NOT_ASSIGNED_TO_CURRENT_WAREHOUSE, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getAgencies_NotAssignedToCrntWarehouse(
			@ApiParam(value = "Warehouse Id", required = true) @NotBlank @Pattern(regexp = "[0-9]+") @PathVariable(value = "id", required = true) String warehouseId);
	
	
	@ApiOperation(value = "Returns a list of Agency that are assigned to current Warehouse", notes = "Returns a list of Agency that are assigned to current Warehouse based on datalibrary for admin setting", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Agency list that are assigned to current Warehouse for admin setting", response = Admin_AgencyWarehouseMappingDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Agency list that are assigned to current Warehouse for admin setting due to server Error")
	})
	@GetMapping( value = RestURIConstants.AGENCY_ASSIGNED_TO_CURRENT_WAREHOUSE, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getAgencies_AssignedToCrntWarehouse(
			@ApiParam(value = "Warehouse Id", required = true) @NotBlank @Pattern(regexp = "[0-9]+") @PathVariable(value = "id", required = true) String warehouseId);
	
	
	@ApiOperation(value = "Create/Update multiple Agencies mapping with Warehouse", notes = "Create/Update multiple Agencies mapping with Warehouse", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Create/Update multiple Agencies mapping with Warehouse", response = Admin_AgencyWarehouseMappingDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Create/Update Agencies Warehouse Mapping due to server Error")
	})
	@PostMapping( value = RestURIConstants.AGENCY_ASSIGNED_TO_CURRENT_WAREHOUSE, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> createAgenciesMappingWithWRH(
			@ApiParam(value = "Object of Agency Warehouse Mapping for admin setting", required = true) @Valid @RequestBody List<AdminAgencyDTO> agencyDTOList,
			@ApiParam(value = "Warehouse Id", required = true) @NotBlank @Pattern(regexp = "[0-9]+") @PathVariable(value = "id", required = true) String warehouseId);
	
	
	@ApiOperation(value = "Returns a Search Settings for the selected Roles.", notes = "Returns a Search Settings for the selected Roles.", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Search Settings for Role", response = Admin_AgencyWarehouseMappingDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Search Settings due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_ROLES_SETTINGS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getRoleSettingForModules(
			@ApiParam(value = "Role Id", required = true) @NotBlank @Pattern(regexp = "[0-9]+") @PathVariable(value = "id", required = true) String roleId,
			@ApiParam(value = "Module Name", required = true) @NotBlank @RequestParam(value = "moduleName", required = true) String moduleName);
	
	
	@ApiOperation(value = "Create/Update Search Settings for the selected Roles in O_SETUP", notes = "Create/Update Search Settings for the selected Roles in O_SETUP", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Create/Update Search Settings mapping with Roles", response = Admin_AgencyWarehouseMappingDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to Create/Update Search Settings mapping with Roles due to server Error")
	})
	@PutMapping( value = RestURIConstants.GET_ROLES_SETTINGS, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> updateRoleSettingsForModules(
			@ApiParam(value = "Object of Agency Warehouse Mapping for admin setting", required = true) @Valid @RequestBody List<AdminRoleSettingsDTO> settingsDTOList,
			@ApiParam(value = "Role Id", required = true) @NotBlank @Pattern(regexp = "[0-9]+") @PathVariable(value = "id", required = true) String roleId);
	
	
	@ApiOperation(value = "Returns a list of alpha-plus Lager (Warehouse List) based on AX warehouse", notes = "Returns a list of alpha-plus Lager (Warehouse List) based on AX warehouse", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get alpha-plus Lager (Warehouse) List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get alpha-plus Lager list due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_AP_WAREHOUSE_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getAlphaPlusWarehouseList(
			@ApiParam(value = "Warehouse Id", required = true) @Pattern(regexp = "[0-9]+") @RequestParam(value = "warehouseId", required = false) String warehouseId );
	
	
	@ApiOperation(value = "Returns a list of Employees and Role based on Selected Agency", notes = "Returns a list of Employees and Role based on Selected Agency", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Employee Role List", response = AlphaXResponse.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get Employee Role list due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_EMPLOYEE_ROLE_LIST, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getEmployeeRoleList(
			@ApiParam(value = "Agency Id", required = true) @NotBlank @Pattern(regexp = "[0-9]+") @RequestParam(value = "agencyId", required = true) String agencyId );
	
	
	@ApiOperation(value = "Returns a details of fiscal year end date", notes = "Returns a details of fiscal year end date", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get Company's fiscal year end date details for admin setting", response = AdminCompanysDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to get fiscal year end date details for admin setting due to server Error")
	})
	@GetMapping( value = RestURIConstants.GET_ADMIN_COMPANY_FISCAL_DATE, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> getCompanyFiscalDateDetails(
			@ApiParam(value = "Admin Company Id", required = true) @PathVariable(value = "id", required = true) String id );
	
	
	@ApiOperation(value = "Add/Update fiscal year end date in DB", notes = "Add/Update fiscal year end date in DB", response = AlphaXResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Add/Update Company's fiscal year end date details for admin setting", response = AdminCompanysDTO.class),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 500, message = "unable to save fiscal year end date details for admin setting due to server Error")
	})
	@PutMapping( value = RestURIConstants.GET_ADMIN_COMPANY_FISCAL_DATE, produces = {"application/JSON"} )
	public ResponseEntity<AlphaXResponse> createUpdateFiscalDateDetails(
			@ApiParam(value = "Admin Company Id", required = true) @PathVariable(value = "id", required = true) String id,
			@ApiParam(value = "Fiscal date", required = true) @NotBlank @RequestParam(value = "fiscalDate", required = true) String fiscalDate );
	
	
}