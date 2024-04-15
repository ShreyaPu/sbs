/**
 * 
 */
package com.alphax.service.mb;

import java.util.Map;

/**
 * @author A98157120
 *
 */
public interface AlphaXSetupService {
	
	public Map<String, Boolean> alphaXAdminUserSetup(String schema, String dataLibrary, String adminUser, String loginUserName);

}
