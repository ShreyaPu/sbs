package com.alphax.service.mb;

import java.io.ByteArrayInputStream;


/**
 * @author A106744104
 *
 */
public interface InventoryReportService {

	public ByteArrayInputStream downloadPDFInvPartsList(String schema, String inventoryListId, boolean pageBreakFlag, String pageBreakDDLValue, String axCompanyId);
	
	public ByteArrayInputStream downloadPDFClosedInvPartsList( String schema, String inventoryListId, String axCompanyId );
	
	public ByteArrayInputStream downloadPDF_ST3_InvPartsList(String schema, String inventoryListId, boolean pageBreakFlag, String pageBreakDDLValue, String axCompanyId);
	
	public ByteArrayInputStream downloadPDF_IntermediateDiff( String schema, String inventoryListId, String axCompanyId );
	
	public ByteArrayInputStream downloadPDF_Countless_Rejected_List( String schema, String inventoryListId, String axCompanyId );
	
	public ByteArrayInputStream downloadPDF_CountingListQuantity( String schema, String inventoryListId, String axCompanyId );
	
	public ByteArrayInputStream downloadPDF_Without_Differnce( String schema, String inventoryListId, String axCompanyId );
	
	public ByteArrayInputStream downloadPDF_Accepted_Diff_Pos( String schema, String inventoryListId, String axCompanyId );
	
	public ByteArrayInputStream downloadPDF_DifferenceSum_Pos( String schema, String inventoryListId, String warehouseNumber, String axCompanyId );
	
}