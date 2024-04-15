package com.alphax.service.mb;

import java.util.List;
import java.util.Map;
import com.alphax.vo.mb.AdminUsersDTO;
import com.alphax.vo.mb.PasswordChangedDTO;
import com.alphax.vo.mb.UserCompanyInfoDTO;
import com.alphax.vo.mb.UserPermissionsVO;
import com.alphax.vo.mb.UserStandardInfoDTO;

public interface JwtAuthenticationService {
	
	public List<String> authenticate(String username, String password, String computerName);

	public UserPermissionsVO getUserPermissions(String dataLibrary, String companyId, String agencyId);

	public UserStandardInfoDTO getUserStandardInfo(String schema, String dataLibrary,String companyId, String agencyId,String loginName);
	
	public boolean logoutApplication(String computerName);
	
	public UserCompanyInfoDTO getUserCompanyDetails(String schema, String dataLibrary, String loginName);
	
	public AdminUsersDTO checkUserLogin(String schema, String dataLibrary,String loginName);
	public void checkUserRoleAndAgency(String schema, String dataLibrary,String userId);
	
	public List<String> alphaXSetupLogin(String userLogin, String password , String computerName);
	
	public Map<String, Boolean> changePassword(PasswordChangedDTO passwordChangedDTO, String computerName);
	
	public boolean killMe(String userName, String password );
}