package com.alphax.service.mb;

import com.alphax.vo.mb.VehicleDataMapDTO;

public interface VehicleDataMapService  {

	public VehicleDataMapDTO getVehicleDataMap(String vehicleId, String dataLibrary);
	
}
