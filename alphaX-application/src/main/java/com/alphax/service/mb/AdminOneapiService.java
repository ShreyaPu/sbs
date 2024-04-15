package com.alphax.service.mb;

import java.util.List;
import java.util.Map;

import com.alphax.vo.mb.AdminApiurlDTO;
import com.alphax.vo.mb.AdminOneapiDTO;
import com.alphax.vo.mb.DropdownObject;
import com.alphax.vo.mb.GlobalSearch;

public interface AdminOneapiService {
	
	public AdminOneapiDTO getOneapiCustomerDetails(String schema, String dataLibrary, String customerId, String apiKey);
	
	public GlobalSearch getOneapiList(String schema, String dataLibrary, String pageSize, String pageNumber, Boolean isEVOBus);
	
	public AdminOneapiDTO createOneapi(AdminOneapiDTO adminOneapi_obj, String schema, String dataLibrary, String loginUserName, String mandent);

	public List<DropdownObject> getVFNumber(String schema, String dataLibrary, Boolean isEVOBus);
	
	public Map<String, Boolean> deleteOneapiCustomerDetails(String schema, String dataLibrary, String customerNo);
	
	public Map<String, Boolean> deleteOneapiConfigurationDetails(String schema, String dataLibrary, String customerNo, String apiKey);

	public List<AdminApiurlDTO> getApiurlList(String schema, String dataLibrary, Boolean isEVOBus);
	
	public AdminOneapiDTO updateOneapi(AdminOneapiDTO adminOneapi_obj, String schema, String dataLibrary, String loginUserName);
	
	public AdminOneapiDTO createEVOBusApi(AdminOneapiDTO adminOneapi_obj, String schema, String dataLibrary, String loginUserName, String mandent);
}
