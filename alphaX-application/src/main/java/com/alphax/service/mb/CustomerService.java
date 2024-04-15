package com.alphax.service.mb;

import java.util.List;

import com.alphax.vo.mb.AuthorityCheck;
import com.alphax.vo.mb.Customer;
import com.alphax.vo.mb.CustomerDefaultDataVO;
import com.alphax.vo.mb.CustomerDetailVO;
import com.alphax.vo.mb.CustomerOneCheck;
import com.alphax.vo.mb.DropdownObject;
import com.alphax.vo.mb.GlobalSearch;

public interface CustomerService  {

	public Customer createOrUpdateCustomer(Customer customer);
	
	public GlobalSearch getCustomerSearchList(String dataLibrary, String companyId,
			String agencyId , String searchString, String pageSize, String pageNo);

	public List<DropdownObject> getAkquisitionskz(String dataLibrary, String companyid , String agencyId);

	public List<DropdownObject> getBranchenSchlussel(String dataLibrary, String companyid, String agencyId);

	public List<DropdownObject> getAnredeSchlussel(String dataLibrary, String companyid, String agencyId);

	public CustomerDetailVO getCustomerDetailsByID(String dataLibrary, String companyId, String agencyId, int id);

	public CustomerOneCheck getCustomerOne(String dataLibrary, String companyId, String agencyId);
	
	public AuthorityCheck updateEditFlag(String dataLibrary, String companyId, String agencyId , String affects, String flag,
			String id);

	public AuthorityCheck getEditFlagValueById(String dataLibrary, String companyId, String agencyId , String id, String affects);

	public CustomerDefaultDataVO getCustomerDefaultData(String schema, String companyId, String agencyId,
			String customerGroup);
	
	public List<DropdownObject> getMehrwertsteuerList();
	public List<DropdownObject> getSkontoList();
	public List<DropdownObject> getZuAbschlagList();
	public List<DropdownObject> getRabattkzList();
	public List<DropdownObject> getElektRechnungList();
	public List<DropdownObject> getXMLDatenList();
	public List<DropdownObject> getHerkunftList();
	public List<DropdownObject> getAnredeCodeList();
	
	public List<DropdownObject> getLandList(String schema, String companyId, String agencyId);
	public List<DropdownObject> getBonitateList(String schema, String companyId, String agencyId);
	public List<DropdownObject> getDruckerList(String dataLibrary, String companyId, String agencyId);
	
	public GlobalSearch getCustomerVehicles(String dataLibrary, String companyId, String agencyId,
			String customerId);
}
