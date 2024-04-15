package com.alphax.service.mb;

import java.io.ByteArrayInputStream;
import java.util.List;

import com.alphax.vo.mb.BuybackHeaderDetailsDTO;

/**
 * @author A87740971
 *
 */

public interface BuybackProcessReportService {
	
	public ByteArrayInputStream downloadPDFOpenBBOFReceipt(String schema, String dataLibrary, String headerId, String alphaPlusWarehouseNo, 
			List<BuybackHeaderDetailsDTO> buybackHeaderDetailsDTOList, String loginUserName);

	public ByteArrayInputStream downloadPDFClosedBBOF(String schema, String dataLibrary, String headerId, String alphaPlusWarehouseNo, String transferDate);
	
}
