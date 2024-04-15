package com.alphax.service.mb.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.alphax.common.rest.message.service.MessageService;
import com.alphax.model.mb.VehicleLegend;
import com.alphax.common.exception.AlphaXException;
import com.alphax.common.exception.ExceptionMessages;
import com.alphax.repository.DBServiceRepository;
import com.alphax.service.mb.VehicleLegendService;
import com.alphax.vo.mb.DropdownObject;
import com.alphax.vo.mb.GlobalSearch;
import com.alphax.vo.mb.VehicleLegendDTO;
import com.alphax.vo.mb.VehicleLegendRepairCode;
import com.alphax.common.constants.RestInputConstants;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VehicleLegendServiceImpl extends BaseService implements VehicleLegendService {

	@Autowired
	DBServiceRepository dbServiceRepository;

	@Autowired
	private MessageService messageService;

	public static final String REPAIR_CODE_NAME = "Reparaturkennzeichen ";

	/**
	 * This is method is used to get List of Vehicle Legend Detail data from DB.
	 */
	@Override
	public GlobalSearch getVehicleLegend( String vehicleId, String dataLibrary, String companyId, String agencyId, String pageSize, String pageNo ) {

		log.info("Inside getVehicleLegend method of VehicleLegendServiceImpl");

		//validate the company Id 
		validateCompany(companyId);

		if(vehicleId == null || vehicleId.trim().length() != 18){
			log.info("Vehicle identification number is not valid");
			AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.VIN_LENGTH_INVALID_MSG_KEY));
			throw exception;
		}

		//List to add all result in one collection
		List<VehicleLegendDTO> vehicleLegendDTOList = new ArrayList<>();
		GlobalSearch globalSearchList = new GlobalSearch();

		try {
			String compID =  StringUtils.stripStart(companyId, "0");

			String baum = vehicleId.substring(3, 11);
			String endn = vehicleId.substring(11, 17);

			if(pageSize==null || pageNo==null || pageSize.isEmpty() || pageNo.isEmpty()){
				pageSize = RestInputConstants.DEFAULT_PAGE_SIZE;
				pageNo = RestInputConstants.DEFAULT_PAGE_NO;
			}

			int totalRecords = Integer.parseInt(pageSize);
			int nextRows = totalRecords * (Integer.parseInt(pageNo) - 1);

			if(totalRecords > 100){
				log.info("pageSize is not valid");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.PGSIZE_INVALID_MSG_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.PGSIZE_INVALID_MSG_KEY));
				throw exception;
			}
			log.info("PageSize :" + pageSize + " OFFSET Size (Skip Rows) : " + nextRows + " Page Number  :"+pageNo);

			StringBuilder vehicleLegend_Query = new StringBuilder(" SELECT FA, FIL, AUFNR, AUFDAT, KM, REPAN, RENR, REBET, ");
			vehicleLegend_Query.append(" REPKZ01, REPKZ02, REPKZ03, REPKZ04, REPKZ05, REPKZ06, REPKZ07, REPKZ08, REPKZ09, REPKZ10, REPKZ11, REPKZ12, (SELECT COUNT(*) FROM ");
			vehicleLegend_Query.append(dataLibrary).append(".M").append(compID).append("_KFZL WHERE BAUM = '").append(baum);
			vehicleLegend_Query.append("' AND ENDN = '").append(endn).append("') AS ROWNUMER FROM ");
			vehicleLegend_Query.append(dataLibrary).append(".M").append(compID).append("_KFZL WHERE BAUM = '").append(baum);
			vehicleLegend_Query.append("' AND ENDN = '").append(endn).append("' OFFSET ");
			vehicleLegend_Query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");

			List<VehicleLegend> vehicleLegendList = dbServiceRepository.getResultUsingQuery(VehicleLegend.class, vehicleLegend_Query.toString(), true);

			if (vehicleLegendList != null && !vehicleLegendList.isEmpty()) {

				for (VehicleLegend legend : vehicleLegendList) {

					VehicleLegendDTO legendDTO = new VehicleLegendDTO();
					legendDTO.setCompanyId(legend.getCompanyId().intValue());
					legendDTO.setAgencyId(legend.getAgencyId().intValue());
					legendDTO.setAssignmentNumber(legend.getAssignmentNumber().toString());
					legendDTO.setCustomerConsultant(legend.getCustomerConsultant());
					legendDTO.setInvoiceNumber(legend.getInvoiceNumber().toString());
					legendDTO.setMileage(legend.getMileage().toString());
					legendDTO.setRevenue(legend.getRevenue().toString());
					legendDTO.setAssignmentDate("");

					if(legend.getAssignmentDate() != null && legend.getAssignmentDate().compareTo(BigDecimal.ZERO) != 0) {
						String dateValue = String.valueOf(legend.getAssignmentDate());

						legendDTO.setAssignmentDate(convertAssignmentDateToDDMMYYYY(dateValue));
					}

					legendDTO.setRepairCodeList(getRepairCodeList(legend));
					vehicleLegendDTOList.add(legendDTO);
				}
				globalSearchList.setSearchDetailsList(vehicleLegendDTOList);
				globalSearchList.setTotalPages(Integer.toString(vehicleLegendList.get(0).getTotalCount()));
				globalSearchList.setTotalRecordCnt(Integer.toString(vehicleLegendList.get(0).getTotalCount()));
			}
			else {
				globalSearchList.setSearchDetailsList(vehicleLegendDTOList);
				globalSearchList.setTotalPages(Integer.toString(0));
				globalSearchList.setTotalRecordCnt(Integer.toString(0));
			}
		}catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Vehicle Legend"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Vehicle Legend"), exception);
			throw exception;
		}

		return globalSearchList;
	}


	/**
	 * This method is used to convert date string ddMMYY to dd.MM.YYYY / dMMYY to dd.MM.YYYY
	 * @param entityDate
	 * @return
	 */
	private String convertAssignmentDateToDDMMYYYY(String assignmentDate) {

		StringBuilder dateBuilder = new StringBuilder();
		Integer yearValue;

		if(assignmentDate.length() == 5) {
			yearValue = Integer.parseInt(assignmentDate.substring(3, 5));

			dateBuilder.append("0").append(assignmentDate.substring(0, 1)).append(".");
			dateBuilder.append(assignmentDate.substring(1, 3)).append(".").append((yearValue > 50)?"19":"20").append(assignmentDate.substring(3, 5));
		}
		if(assignmentDate.length() == 6) {
			yearValue = Integer.parseInt(assignmentDate.substring(4, 6));

			dateBuilder.append(assignmentDate.substring(0, 2)).append(".");
			dateBuilder.append(assignmentDate.substring(2, 4)).append(".").append((yearValue > 50)?"19":"20").append(assignmentDate.substring(4, 6));
		}

		return dateBuilder.toString();
	}


	private List<VehicleLegendRepairCode> getRepairCodeList(VehicleLegend legend){

		List<VehicleLegendRepairCode> repairCodeList = new ArrayList<>();

		//iterate loop to get 12 repair code  values
		for(int count=1; count <= 12; count++) {

			VehicleLegendRepairCode repairCode = new VehicleLegendRepairCode();
			repairCode.setName(REPAIR_CODE_NAME +count);
			repairCode.setValue(repairCodeValue(count, legend));
			repairCodeList.add(repairCode);
		}
		//return the repairCode List
		return repairCodeList;
	}


	private String repairCodeValue(int number, VehicleLegend legend) {

		String repairCodeValue;

		switch (number) {
		case 1 :
			repairCodeValue = legend.getRepairCode01();
			break;
		case 2 :
			repairCodeValue = legend.getRepairCode02();
			break;
		case 3 :
			repairCodeValue = legend.getRepairCode03();
			break;
		case 4 :
			repairCodeValue = legend.getRepairCode04();
			break;
		case 5 :
			repairCodeValue = legend.getRepairCode05();
			break;
		case 6 :
			repairCodeValue = legend.getRepairCode06();
			break;
		case 7 :
			repairCodeValue = legend.getRepairCode07();
			break;
		case 8 :
			repairCodeValue = legend.getRepairCode08();
			break;
		case 9 :
			repairCodeValue = legend.getRepairCode09();
			break;
		case 10 :
			repairCodeValue = legend.getRepairCode10();
			break;
		case 11 :
			repairCodeValue = legend.getRepairCode11();
			break;
		case 12 :
			repairCodeValue = legend.getRepairCode12();
			break;
		default :
			repairCodeValue = "";
			break;
		}

		//return the repair Code Value
		return repairCodeValue;
	}


	/**
	 * This is method is used to get List of Reparaturkennzeichen (Repair Codes) from DB.
	 */
	@Override
	public List<DropdownObject> getRepairCodeList(String dataLibrary, String companyId, String agencyId) {

		log.info("Inside getRepairCodeList method of VehicleLegendServiceImpl");

		//validate the company Id 
		validateCompany(companyId);

		String compID =  StringUtils.stripStart(companyId, "0");
		List<DropdownObject> repairCodeList = new ArrayList<>();

		try {

			StringBuilder query = new StringBuilder("SELECT SUBSTR(keyfld,5,2) AS KEYFLD, LEFT(datafld,17) AS DATAFLD FROM ");
			query.append(dataLibrary).append(".M").append(compID).append("_KOND WHERE KEYFLD LIKE '3045%' ORDER BY KEYFLD");

			Map<String, String> repairCodeMap = dbServiceRepository.getResultUsingCobolQuery(query.toString());

			if(repairCodeMap != null && !repairCodeMap.isEmpty()) {
				for(Map.Entry<String, String> repairCodeDetails : repairCodeMap.entrySet()) {

					DropdownObject dropdownObject = new DropdownObject();
					dropdownObject.setKey(repairCodeDetails.getKey());
					dropdownObject.setValue(repairCodeDetails.getValue());

					repairCodeList.add(dropdownObject);
				}
			}else{
				log.info("Repair Code List is empty");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Reparaturkennzeichen (Repair Codes)"));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Reparaturkennzeichen (Repair Codes)"));
				throw exception;
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Reparaturkennzeichen (Repair Codes)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Reparaturkennzeichen (Repair Codes)"), exception);
			throw exception;
		}

		return repairCodeList;
	}


}