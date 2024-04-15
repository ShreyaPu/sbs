package com.alphax.service.mb;

import java.util.List;
import java.util.Map;

import com.alphax.vo.mb.CustomerVegaDTO;
import com.alphax.vo.mb.DropdownObject;
import com.alphax.vo.mb.GlobalSearch;
import com.alphax.vo.mb.VehicleDetailsVO;
import com.alphax.vo.mb.VehicleStandardInfoVO;

public interface VehicleService  {

	
	public GlobalSearch getVehiclesSearchList(String dataLibrary, String companyId, String agencyId,
			String searchString, String pageSize, String pageNo);

	public VehicleStandardInfoVO getVehiclesInfoByVin(String dataLibrary, String schema, String companyId, 
			String agencyId, String vin);

	public VehicleDetailsVO createOrUpdateVehicle(VehicleDetailsVO vehicleDtl, String dataLibrary, String companyId,
			String agencyId, String indicaterFlag);
	
	public List<DropdownObject> getMarkeList(String schema, String companyId, String agencyId);
	
	public List<DropdownObject> getZulassungsartList(String dataLibrary, String companyId, String agencyId);
	
	public List<DropdownObject> getAntriebsartList(String dataLibrary, String companyId, String agencyId);
	
	public List<DropdownObject> getLaufleistungList();
	
	public List<DropdownObject> getVerkauferList(String dataLibrary, String companyId, String agencyId);
	
	public List<DropdownObject> getVerkaufssparteList(String dataLibrary, String companyId, String agencyId);
	
	public List<DropdownObject> getAkquisitionspereList(String dataLibrary, String companyId, String agencyId);
	
	public List<DropdownObject> getWartGruppeList();
	
	public List<DropdownObject> getKundendBeraterList(String dataLibrary, String companyId, String agencyId);
	
	public List<DropdownObject> getBetreuendeWerkList(String schema, String companyId, String agencyId);

	public VehicleDetailsVO getVehicleDetailsByID(String dataLibrary, String companyId, String agencyId, String id);
	
	public List<CustomerVegaDTO> getKundenNr_VEGAList(String dataLibrary, String schema, String ompanyId, String agencyId);
	
	public GlobalSearch getKundenNrList(String dataLibrary, String ompanyId, String agencyId,
			String searchString, String pageSize, String pageNo);
	
	public List<DropdownObject> getAktionList(String schema);
	
	public Map<String, Boolean> checkDuplicateLicencePlate(String licencePlate, String dataLibrary, String companyId, String agencyId);
	
	public Map<String, Boolean> checkDuplicateVIN(String vin, String dataLibrary, String companyId, String agencyId);
	
}
