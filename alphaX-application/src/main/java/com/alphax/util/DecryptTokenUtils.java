package com.alphax.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alphax.common.constants.AlphaXTokenConstants;
import com.alphax.config.JwtRequestFilter;

/**
 * This Class is used to decrypt the Authentication and Authorization tokens.
 * 
 * @author A106744104
 *
 */
@Component
public class DecryptTokenUtils {

	@Autowired
	private JwtRequestFilter requestFilter;

	
	public DecryptTokenUtils() {
	}	
	
	
	/**
	 * Get the Datalibrary from Token
	 * @return String: DataLibrary
	 */
	public String getDataLibrary() {
		return requestFilter.getClaimsUsingToken().get(AlphaXTokenConstants.DATALIB, String.class);
	}

	/**
	 * Get the Schema from Token
	 * @return String: Schema
	 */
	public String getSchema() {
		return requestFilter.getClaimsUsingToken().get(AlphaXTokenConstants.SCHEMA, String.class);
	}

	/**
	 * Get the WsId from Token
	 * @return String: WsId
	 */
	public String getWsId() {
		return requestFilter.getClaimsUsingToken().get(AlphaXTokenConstants.WSID, String.class);
	}

	/**
	 * Get the Logged-In User from Token
	 * @return String: User-name
	 */
	public String getLogedInUser() {
		return requestFilter.getClaimsUsingToken().get(AlphaXTokenConstants.LOGIN, String.class);
	}

	/**
	 * Get the Logged-In UserId from Token
	 * @return String: UserId
	 */
	public String getUserId() {
		return requestFilter.getClaimsUsingToken().get(AlphaXTokenConstants.USERID, String.class);
	}

	/**
	 * Get the RoleId from Token
	 * @return String: RoleId
	 */
	public String getRoleId() {
		return requestFilter.getClaimsUsingToken().get(AlphaXTokenConstants.ROLEID, String.class);
	}

	/**
	 * Get the RoleName from Token
	 * @return String: RoleName
	 */
	public String getRoleName() {
		return requestFilter.getClaimsUsingToken().get(AlphaXTokenConstants.ROLENAME, String.class);
	}

	/**
	 * Get the Alpha-plus CompanyId from Token
	 * @return String: Alpha-plus CompanyId
	 */
	public String getApCompanyId() {
		return requestFilter.getClaimsUsingToken().get(AlphaXTokenConstants.AP_COMPANY, String.class);
	}

	/**
	 * Get the Alpha-plus AgencyId from Token
	 * @return String: Alpha-plus AgencyId
	 */
	public String getApAgencyId() {
		return requestFilter.getClaimsUsingToken().get(AlphaXTokenConstants.AP_AGENCY, String.class);
	}

	/**
	 * Get the Alpha-X CompanyId from Token
	 * @return String: Alpha-X CompanyId
	 */
	public String getAxCompanyId() {
		return requestFilter.getClaimsUsingToken().get(AlphaXTokenConstants.AX_COMPANYID, String.class);
	}

	/**
	 * Get the Alpha-X AgencyId from Token
	 * @return String: Alpha-X AgencyId
	 */
	public String getAxAgencyId() {
		return requestFilter.getClaimsUsingToken().get(AlphaXTokenConstants.AX_AGENCYID, String.class);
	}

	/**
	 * Get the Allowed Alpha-plus Agencies from Token
	 * @return String: AllowedApAgencies
	 */
	public String getAllowedApAgencies() {
		return requestFilter.getClaimsUsingToken().get(AlphaXTokenConstants.ALLOWED_AGENCY, String.class);
	}

	/**
	 * Get the Allowed Alpha-plus Warehouse from Token
	 * @return String: AllowedApWarehouse
	 */
	public String getAllowedApWarehouse() {
		return requestFilter.getClaimsUsingToken().get(AlphaXTokenConstants.ALLOWED_WAREHOUSE, String.class);
	}
	
	/**
	 * Get the User Name from Token
	 * @return String: Name
	 */
	public String getName() {
		return requestFilter.getClaimsUsingToken().get(AlphaXTokenConstants.USERNAME, String.class);
	}
	
	/**
	 * Get the SavLibrary from Token
	 * @return String: SavLibrary
	 */
	public String getSavLibrary() {		
		return requestFilter.getClaimsUsingToken().get(AlphaXTokenConstants.SAVLIB, String.class);
	}
	
	/**
	 * Get the Podlibrary from Token
	 * @return String: Podlibrary
	 */
	public String getPodlib() {		
		return requestFilter.getClaimsUsingToken().get(AlphaXTokenConstants.PODLIB, String.class);
	}
	
	/**
	 * Get the NoSavLibrary from Token
	 * @return String: NoSavLibrary
	 */
	public String getNosavlib() {		
		return requestFilter.getClaimsUsingToken().get(AlphaXTokenConstants.NOSAVLIB, String.class);
	}
	
	/**
	 * Get the Wsprt from Token
	 * @return String: Wsprt
	 */
	public String getWsprt() {		
		return requestFilter.getClaimsUsingToken().get(AlphaXTokenConstants.WSPRT, String.class);
	}
	
	/**
	 * Get the Sysprt from Token
	 * @return String: Sysprt
	 */
	public String getSysprt() {		
		return requestFilter.getClaimsUsingToken().get(AlphaXTokenConstants.SYSPRT, String.class);
	}
	
	/**
	 * Get the Prtkurz from Token
	 * @return String: Prtkurz
	 */
	public String getPrtkurz() {		
		return requestFilter.getClaimsUsingToken().get(AlphaXTokenConstants.PRTKURZ, String.class);
	}
	
	/**
	 * Get the Alpha-plus Warehouse assigned to Company from Token
	 * @return String: CompanyAllAPwarehouse
	 */
	public String getCompanyApWarehouse() {
		return requestFilter.getClaimsUsingToken().get(AlphaXTokenConstants.COMPANY_ALL_AP_WAREHOUSE, String.class);
	}
	
	/**
	 * Get the et_central_function from Token
	 * @return String: et_central_function
	 */
	public String getETCentralFunctionValue() {
		return requestFilter.getClaimsUsingToken().get(AlphaXTokenConstants.ET_CENTRAL_FUNCTION, String.class);
	}
	

	/**
	 * Get the Mandent from Token
	 * @return String: Mandent
	 */
	public String getMandent() {
		return requestFilter.getClaimsUsingToken().get(AlphaXTokenConstants.MANDANT, String.class);
	}
	
	
	/**
	 * Get the AX_DataCenter_lib from Token
	 * @return String: AX_DataCenter_lib
	 */
	public String getAX_DataCenter_Lib() {
		return requestFilter.getClaimsUsingToken().get(AlphaXTokenConstants.AX_DATACENTER_LIB, String.class);
	}
	
}