package com.alphax.service.mb;

import java.util.Map;

/**
 * @author A106744104
 *
 */
public interface SimulationProcessService {
	
	public Map<String, String> generateDeliveryNotes( String dataLibrary, String companyId, String agencyId );
	
	public Map<String, String> autoProcessDiveryNotes( String dataLibrary, String companyId, String agencyId );
	
	public Map<String, String> generateConflictDeliveryNotes( String dataLibrary, String companyId, String agencyId );
	
}