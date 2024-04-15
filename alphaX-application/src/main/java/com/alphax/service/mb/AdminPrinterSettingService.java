package com.alphax.service.mb;

import java.util.List;
import java.util.Map;

import com.alphax.vo.mb.AdminPrinterDTO;
import com.alphax.vo.mb.DropdownObject;

public interface AdminPrinterSettingService {	
	
	public AdminPrinterDTO createNewPrinter(AdminPrinterDTO adminPrinter_obj, String schema, String loginUserName);

	public List<DropdownObject> getPrinterList(String schema, String dataLibrary);
	
	public Map<String, Boolean> deletePrinterDetails(String schema, String printerId, String loginUserName);
	
	public Map<String, Boolean> updateAgencyPrinterMapping(List<AdminPrinterDTO> mapping_DTO, String schema, String agencyId, String loginUserName);
	
	public List<AdminPrinterDTO> getPrinters_NotAssignedToCrntAgency(String schema, String agencyId);
	
	public List<AdminPrinterDTO> getPrinters_AssignedToCrntAgency(String schema, String agencyId);
	
	public AdminPrinterDTO getPrinterDetails(String schema, String dataLibrary, String printerId);

	public  Map<String, Boolean> updateAdminPrinterDetails(AdminPrinterDTO updatedAdminPrinterDetails, String schema,
			String dataLibrary, String loginUserName);

	public List<DropdownObject> getPrinterTypeList(String schema, String dataLibrary);
			
	
}
