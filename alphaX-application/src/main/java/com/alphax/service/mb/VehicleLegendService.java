package com.alphax.service.mb;

import java.util.List;

import com.alphax.vo.mb.DropdownObject;
import com.alphax.vo.mb.GlobalSearch;

public interface VehicleLegendService  {

	public GlobalSearch getVehicleLegend(String vehicleId, String dataLibrary, String companyId, String agencyId, String pageSize, String pageNo);
	
	public List<DropdownObject> getRepairCodeList(String dataLibrary, String companyId, String agencyId);
	
}
