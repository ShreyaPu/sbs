package com.alphax.service.mb;

import java.util.List;

import com.alphax.vo.mb.Companys;
import com.alphax.vo.mb.LoginAgencysDTO;

/**
 * @author A106744104
 *
 */

public interface CompanyService {
	
	public List<Companys> getAlphaPlusAgencyList(String datalibrary , String company, String agencyId);
	
	public List<LoginAgencysDTO> getAlphaXAgencyList(String datalibrary, String schema,String userLogin);
	
	public String createAuthorizationToken(LoginAgencysDTO agency_obj, String dataLibrary, String schema, String userId, String logedInUser, String name, String wsId,
			String savLib, String podLib, String noSavLib, String wsprt, String sysprt, String prtkurz, String mandent);
	
}
