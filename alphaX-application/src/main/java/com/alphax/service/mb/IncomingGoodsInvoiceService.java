package com.alphax.service.mb;

import java.util.List;
import java.util.Map;

import com.alphax.vo.mb.ActivatedCountingListDTO;
import com.alphax.vo.mb.AddCostsDetailsDTO;
import com.alphax.vo.mb.AuditAssignDeliveryNotesDTO;
import com.alphax.vo.mb.AuditInvoiceDTO;
import com.alphax.vo.mb.BookedDeliveryNoteDetailsDTO;
import com.alphax.vo.mb.DeliveryNotes_BA_DTO;
import com.alphax.vo.mb.GlobalSearch;
import com.alphax.vo.mb.InvoiceDeliveryNoteDTO;
import com.alphax.vo.mb.InvoiceDifferncesDTO;
import com.alphax.vo.mb.InvoiceOverviewDTO;
import com.alphax.vo.mb.RecordDifferences_DTO;

public interface IncomingGoodsInvoiceService  {

	public GlobalSearch getBookingTextList(String schema, String searchString);
	
	public List<InvoiceDifferncesDTO> getInvoiceNumberList(String dataLibrary,String schema,
			String warehouseNo, String manufacturer, String supplierNo, String searchString);
	
	public List<RecordDifferences_DTO> getRecordDifferencesList(String dataLibrary,String schema, String deliveryNoteNo, 
			String manufacturer, String warehouseNo, String supplierNo);
	
	public List<RecordDifferences_DTO> saveRecordDifferences(List<RecordDifferences_DTO> recordDiffList, String dataLibrary,String schema);
	
	public Map<String, String> correctionsInBookingForDeliveryNotes(BookedDeliveryNoteDetailsDTO detailsDTO,
			String dataLibrary,String schema, String companyId, String agencyId, String loginUserName);
	
	public String getCalculatedDifferanceValue(String dataLibrary,String schema, String invoiceNumber);
	
	public List<AddCostsDetailsDTO> getAdditionalCostList(String schema, String invoiceNumber);
	
	public Map<String, Boolean> saveAdditionalCostDetails(List<AddCostsDetailsDTO> additionalCostDtoList, String invoiceNumber, String schema, String loginUserName);

	public Map<String, Boolean> createInvoiceManually( AuditInvoiceDTO auditInvoiceObj, String dataLibrary, String schema, String logedInUser); 
	
	public Map<String, Boolean> importInvoiceDataFromO_ARPDC(String schema, String dataLibrary, String loginUserName);
	
	public List<InvoiceOverviewDTO> getOverviewOfInvoiceList(String dataLibrary, String schema, String warehouseNo);
	
	public List<AuditAssignDeliveryNotesDTO> getAudit_NotAssignedDeliveryNotes(String schema, String dataLibrary,
			 String manufacturer,  String warehouse);
	
	public List<AuditAssignDeliveryNotesDTO> getAudit_AssignedDeliveryNotes(String schema, String dataLibrary,
			 String manufacturer,  String invoiceNumber);
	
	public List<InvoiceOverviewDTO> getInvoiceDetailList(String dataLibrary, String schema, String invoiceNo, String warehouseNo, String manufacturer );
	
	public List<BookedDeliveryNoteDetailsDTO> getBookedDeliveryNoteDetails(String dataLibrary,String schema, String deliveryNoteNo,
			String warehouseNo);
	
	public Map<String, Boolean> updateInvoiceDeliveryNoteAsApproved( DeliveryNotes_BA_DTO deliveryNotes_BA_DTO,
			String dataLibrary,String loginUserName ,String schema);

	public Map<String, String> getCountOfNotImportedInvoiceItems(String dataLibrary, String schema);
	
	public Map<String, Boolean> setAssignDeliveryNoteForInvoice(List<InvoiceDeliveryNoteDTO> auditInvoiceObj ,String schema, String dataLibrary,
			 String loginUserName);

	
	public Map<String, Boolean> updateInvoiceAmountForDeliveryNote( BookedDeliveryNoteDetailsDTO bookedDeliveryNoteDetailsDTO, String dataLibrary, String schema);
			
	public List<ActivatedCountingListDTO> getWarehouseListForInvoice(String dataLibrary, String schema,
			String allowedApWarehouses);

}