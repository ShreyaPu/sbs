package com.alphax.service.mb.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.alphax.common.rest.message.service.MessageService;
import com.alphax.model.mb.SearchCustomer_CB;
import com.alphax.model.mb.SearchCustomer_VEGA1;
import com.alphax.model.mb.SearchCustomer_VEGA2;
import com.alphax.model.mb.SearchVehicle;
import com.alphax.model.mb.VehicleBriefkastenKFZ222X;
import com.alphax.model.mb.VehicleKFZ221X;
import com.alphax.model.mb.VehicleKFZF1XX;
import com.alphax.model.mb.VehicleMarke;
import com.alphax.common.exception.AlphaXException;
import com.alphax.common.exception.ExceptionMessages;
import com.alphax.repository.CobolServiceRepository;
import com.alphax.repository.DBServiceRepository;
import com.alphax.repository.StubServiceRepository;
import com.alphax.service.mb.VehicleService;
import com.alphax.vo.mb.CustomerVegaDTO;
import com.alphax.vo.mb.DropdownObject;
import com.alphax.vo.mb.GlobalSearch;
import com.alphax.vo.mb.SearchCustomerDTO;
import com.alphax.vo.mb.SearchVehicleDTO;
import com.alphax.vo.mb.VehicleDetailsVO;
import com.alphax.vo.mb.VehicleStandardInfoVO;
import com.alphax.common.constants.Program_Commands_Constants;
import com.alphax.common.constants.RestInputConstants;
import com.ibm.as400.access.ProgramParameter;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VehicleServiceImpl extends BaseService implements VehicleService {

	@Autowired
	DBServiceRepository dbServiceRepository;

	@Autowired
	StubServiceRepository stubServiceRepository;

	@Autowired
	CobolServiceRepository cobolServiceRepository;

	@Autowired
	private MessageService messageService;

	//Create formatter
	DateTimeFormatter FOMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

	/**
	 * This is method is used to get vehicle data from DB.
	 */
	@Override
	public GlobalSearch getVehiclesSearchList(String dataLibrary, String companyId, String agencyId,
			String searchString, String pageSize, String pageNo) {

		log.info("Inside getVehiclesSearchList method of VehicleServiceImpl");
		GlobalSearch globalSearchList = new GlobalSearch();
		List<SearchVehicleDTO> searchVehicleDTOList = new ArrayList<>();
		try {

			//validate the company Id 
			validateCompany(companyId);

			String compID =  StringUtils.stripStart(companyId, "0");
			String agencyID = StringUtils.stripStart(agencyId, "0");
			log.info("Company ID  xx:  {}  and  Agency ID yy:  {}", compID, agencyID);

			if(pageSize==null || pageNo==null || pageSize.isEmpty() || pageNo.isEmpty()){
				pageSize = RestInputConstants.DEFAULT_PAGE_SIZE;
				pageNo = RestInputConstants.DEFAULT_PAGE_NO;
			}

			int totalRecords = Integer.parseInt(pageSize);
			int nextRows = totalRecords * (Integer.parseInt(pageNo) - 1);

			//validate the page size
			validatePageSize(totalRecords);
			
			log.info("PageSize :" + pageSize + " OFFSET Size (Skip Rows) : " + nextRows + " Page Number  :"+pageNo);

			StringBuilder query = new StringBuilder("SELECT kfzf1.FKENNZ7, kfzf1.FWHC, kfzf1.BAUM, kfzf1.ENDN, kfzf1.FGP, kfzf1.DTEZU, kfzf1.TYP, kfzf1.KDNR1, kdd.NAME, kfzf1.FZNUMMER, (SELECT COUNT(*) FROM  ");
			query.append(dataLibrary).append(".M").append(compID).append("_KFZF1XX WHERE ( UPPER(FKENNZ7) LIKE UPPER('%").append(searchString).append("%') OR UPPER(FZNUMMER) LIKE UPPER('%").append(searchString).append("%'))");
			query.append("AND VZSL1=7 ) AS ROWNUMER FROM  ");
			query.append(dataLibrary).append(".M").append(compID).append("_KFZF1XX kfzf1,").append(dataLibrary).append(".M").append(compID).append("_KDDATK1 kdd ");
			query.append(" WHERE ( UPPER(FKENNZ7) LIKE UPPER('%").append(searchString).append("%') OR UPPER(FZNUMMER) LIKE UPPER('%").append(searchString).append("%'))");
			query.append(" AND VZSL1=7 AND kdd.KDNR2='' AND kdd.FNR='' AND kfzf1.KDNR1 = kdd.KDNR1 ORDER BY UPPER(FKENNZ7)  ASC OFFSET ");
			query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");
			//log.info("Search Query :" + query.toString());

			List<SearchVehicle> searchVehiclesList = dbServiceRepository.getResultUsingQuery(SearchVehicle.class, query.toString(),true);

			if (searchVehiclesList != null && !searchVehiclesList.isEmpty()) {
				//set all vehicle details
				searchVehicleDTOList = convertVehicleEntityToDTO(searchVehiclesList, searchVehicleDTOList);

				globalSearchList.setSearchDetailsList(searchVehicleDTOList);
				globalSearchList.setTotalPages(Integer.toString(searchVehiclesList.get(0).getTotalCount()));
				globalSearchList.setTotalRecordCnt(Integer.toString(searchVehiclesList.get(0).getTotalCount()));
			} else {
				globalSearchList.setSearchDetailsList(searchVehicleDTOList);
				globalSearchList.setTotalPages(Integer.toString(0));
				globalSearchList.setTotalRecordCnt(Integer.toString(0));
			}
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.SEARCH_FAILED_MSG_KEY, "Vehicle"));
			log.error(messageService.getReadableMessage(ExceptionMessages.SEARCH_FAILED_MSG_KEY, "Vehicle"), exception);
			throw exception;
		}

		return globalSearchList;
	}


	/**
	 * This method is used to set Entity data to DTO
	 * @param searchVehiclesList
	 * @param searchVehicleDTOList
	 * @return
	 */
	private List<SearchVehicleDTO> convertVehicleEntityToDTO(List<SearchVehicle> searchVehiclesList, List<SearchVehicleDTO> searchVehicleDTOList) {

		for (SearchVehicle vehicle : searchVehiclesList) {
			SearchVehicleDTO vehicleDTO = new SearchVehicleDTO();

			//logic for VIN Creation
			vehicleDTO.setVin(StringUtils.join(
					StringUtils.rightPad(vehicle.getManufacturerCode(), 3, " "),
					StringUtils.rightPad(vehicle.getModelInformation(), 8, " "),
					StringUtils.rightPad(vehicle.getProductionIndicator(), 6, " "),
					StringUtils.rightPad(vehicle.getChasisNumberDigit(), 1, " ")));

			//logic to remove '*' from customer Name  */
			vehicleDTO.setName(vehicle.getName().replace("*", " "));

			/* Start - Logic for First Registration Date creation  */
			StringBuilder registrationDate =  new StringBuilder();
			if(vehicle.getFirstRegistration_Date().startsWith("2") && vehicle.getFirstRegistration_Date().length() == 7) {

				registrationDate.append(vehicle.getFirstRegistration_Date().substring(1, 3)).append("-");
				registrationDate.append(vehicle.getFirstRegistration_Date().substring(3, 5)).append("-");
				registrationDate.append("20").append(vehicle.getFirstRegistration_Date().substring(5, 7));
			}
			else if(vehicle.getFirstRegistration_Date().length() == 6) {

				registrationDate.append(vehicle.getFirstRegistration_Date().substring(0, 2)).append("-");
				registrationDate.append(vehicle.getFirstRegistration_Date().substring(2, 4)).append("-");
				registrationDate.append("19").append(vehicle.getFirstRegistration_Date().substring(4, 6));
			}
			else if(vehicle.getFirstRegistration_Date().length() == 5) {

				registrationDate.append(vehicle.getFirstRegistration_Date().substring(0,1)).append("-");
				registrationDate.append(vehicle.getFirstRegistration_Date().substring(1, 3)).append("-");
				registrationDate.append("19").append(vehicle.getFirstRegistration_Date().substring(3, 5));
			}
			vehicleDTO.setFirstRegistration_Date(registrationDate.toString());
			/* End - Logic for First Registration Date creation  */

			vehicleDTO.setCustomerNumber(vehicle.getCustomerNumber());
			vehicleDTO.setVehicleType(vehicle.getVehicleType());
			vehicleDTO.setLicencePlate(vehicle.getLicencePlate());

			searchVehicleDTOList.add(vehicleDTO);
		}

		return searchVehicleDTOList;
	}


	@Override
	public VehicleStandardInfoVO getVehiclesInfoByVin(String dataLibrary, String schema, String companyId, String agencyId, String vin) {

		log.info("Inside getVehiclesInfoByVin method of VehicleServiceImpl");
		VehicleStandardInfoVO vehicleStandardInfo = new VehicleStandardInfoVO();

		//validate the company Id 
		validateCompany(companyId);

		if(vin.length() != 9){
			log.info("VIN is not valid");
			AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.VIN_INVALID_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.VIN_INVALID_MSG_KEY));
			throw exception;
		}

		String worldManufacturerCode  = vin.substring(0,3);  //worldManufacturerCode = WHC 
		String vin_model = vin.substring(3,9);  // model = Baum - Baumuster 


		String compID =  StringUtils.stripStart(companyId, "0");
		String agencyID = StringUtils.stripStart(agencyId, "0");
		log.info("Company ID  xx:  {}  and  Agency ID yy:  {}", compID, agencyID);


		StringBuilder query_1 = new StringBuilder("SELECT KEYFLD, DATAFLD FROM ");
		query_1.append(dataLibrary).append(".M").append(compID).append("_KOND WHERE KEYFLD LIKE '3034").append(vin_model).append("01%'");

		StringBuilder query_2 = new StringBuilder(" SELECT WFM_WHC, WFM_FINBER, WFM_MARKE FROM ");
		query_2.append(schema).append(".PMH_WHCMA  WHERE   WFM_WHC  = ").append("'"+worldManufacturerCode+"'").append(" ORDER BY WFM_FINBER DESC ");

		List<VehicleMarke> vehicleMarkeList = new ArrayList<>();

		try{
			Map<String, String> vehiclesInfoMap = dbServiceRepository.getResultUsingCobolQuery(query_1.toString());
			vehicleMarkeList = dbServiceRepository.getResultUsingQuery(VehicleMarke.class ,query_2.toString(),true );

			if(vehiclesInfoMap != null && !vehiclesInfoMap.isEmpty() && vehiclesInfoMap.size()==1) {

				for(Map.Entry<String, String> vehicleInfo : vehiclesInfoMap.entrySet()) {

					vehicleStandardInfo.setVehicleType(vehicleInfo.getValue().substring(0,18).trim());
					vehicleStandardInfo.setTypeGroup(vehicleInfo.getValue().substring(18,20).trim());
					vehicleStandardInfo.setTypeCode(vehicleInfo.getValue().substring(20,22).trim());
					vehicleStandardInfo.setEnginePowerKw(StringUtils.stripStart(vehicleInfo.getValue().substring(22,25).trim(),"0"));
					vehicleStandardInfo.setCubicCapacity(StringUtils.stripStart(vehicleInfo.getValue().substring(25,30).trim(),"0"));
					vehicleStandardInfo.setDriveType(vehicleInfo.getValue().substring(41).trim());

				}
			}
			else {
				log.info("Vehicles Information not valid for this VIN");

				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.VEHICLE_DATA_NOT_FOUND_MSG_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.VEHICLE_DATA_NOT_FOUND_MSG_KEY));
				throw exception;
			}

			if (vehicleMarkeList != null && !vehicleMarkeList.isEmpty() && vehicleMarkeList.size() > 0) {

				List<String> specialWHCList = new ArrayList<>(); 
				specialWHCList.add("WDB");
				specialWHCList.add("WDD");
				specialWHCList.add("WDC");
				specialWHCList.add("4JG");

				for (VehicleMarke vehicleMarke_obj : vehicleMarkeList) {
					if (specialWHCList.contains(worldManufacturerCode) && 
							( vehicleMarke_obj.getFinArea().trim().equalsIgnoreCase(vin_model.substring(0,3))
									|| vehicleMarke_obj.getFinArea().trim().equalsIgnoreCase(vin_model.substring(0,4))
									|| vehicleMarke_obj.getFinArea().trim().equalsIgnoreCase(vin_model.substring(0,5))
									|| vehicleMarke_obj.getFinArea().trim().equalsIgnoreCase(vin_model.substring(0,6)))) {

						vehicleStandardInfo.setMarke(vehicleMarke_obj.getMarke());
						log.info("WFM_WHC :"+vehicleMarke_obj.getWorldManufacturerCode() +" WFM_FINBER :" +vehicleMarke_obj.getFinArea() +" WFM_MARKE :"+vehicleMarke_obj.getMarke());
						break;
					}else if(vehicleMarke_obj.getFinArea().trim().equalsIgnoreCase("")){
						log.info("WFM_FINBER is blank");
						vehicleStandardInfo.setMarke(vehicleMarke_obj.getMarke()); 
					}
				}
			} else {
				log.info("Marke Information not valid for this WHC :" + worldManufacturerCode);

				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.VEHICLE_MARKE_NOT_FOUND_MSG_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.VEHICLE_MARKE_NOT_FOUND_MSG_KEY));
				throw exception;
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());

			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.VEHICLE_DEFAULT_INFO_FAILED_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.VEHICLE_DEFAULT_INFO_FAILED_MSG_KEY), exception);
			throw exception;
		}
		return vehicleStandardInfo;
	}


	@Override
	public VehicleDetailsVO createOrUpdateVehicle(VehicleDetailsVO vehicleDtl, String dataLibrary, String companyId,
			String agencyId , String indicaterFlag) {
		log.info("Inside createVehicle method of VehicleServiceImpl");

		//validate the company Id 
		validateCompany(companyId);

		String compID =  StringUtils.stripStart(companyId, "0");
		String agencyID = StringUtils.stripStart(agencyId, "0");
		log.info("Company ID  xx:  {}  and  Agency ID yy:  {}", compID, agencyID);
		ProgramParameter[] parmList;
		VehicleDetailsVO vehicle_Obj =null;

		try{

			String inputString = createInputString(vehicleDtl , indicaterFlag);

			parmList = new ProgramParameter[2];

			// Create the input parameter  
			parmList[0] = new ProgramParameter(inputString.getBytes(Program_Commands_Constants.IBM_273));

			parmList[1] = new ProgramParameter(2634);

			List<String> outputList = cobolServiceRepository.executeProgramWithoutTrim(parmList, 1, Program_Commands_Constants.CREATE_VEHICLE_PROGRAM);

			if(outputList != null && !outputList.isEmpty()) {
				if(!outputList.get(0).contains("Error")) {
					vehicle_Obj = new VehicleDetailsVO();;
					setVehicleValues(outputList , vehicle_Obj);
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.CREATE_FAILED_MSG_KEY, "Vehicle"));
			log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY, "Vehicle"), exception);
			throw exception;
		}
		return vehicle_Obj;
	}


	private void setVehicleValues(List<String> outputList, VehicleDetailsVO vehicle) {
		String output = outputList.get(0);
		log.info("output Value:- {} ", output);
		log.info("output length:- {} ", output.length());

		//vehicle = 
		vehicle.setAmtlichesKennzeichen(output.substring(1,12).trim());
		vehicle.setKundenNummer(output.substring(12,20).trim());

		vehicle.setMarke(output.substring(220,223).trim());
		vehicle.setFzIdentnr1(output.substring(223,226).trim());
		vehicle.setFzIdentnr2(output.substring(226,232).trim());
		vehicle.setFzIdentnr3(output.substring(232,234).trim());
		vehicle.setFzIdentnr4(output.substring(234,240).trim());
		vehicle.setFzIdentnr5(output.substring(240,241).trim());
		// add extra space 
		vehicle.setMotorNummer(output.substring(242,256).trim()); 
		vehicle.setFamilienGruppe(output.substring(256,258).trim()); 
		vehicle.setTypenkennzahl(output.substring(258,260).trim()); 
		vehicle.setTypNummer(output.substring(260,263).trim()); 
		vehicle.setTypVariante(output.substring(263,265).trim()); 
		vehicle.setMotorstarkeKW(output.substring(265,268).trim()); 
		vehicle.setHerstellerCode(output.substring(268,272).trim()); 
		vehicle.setTypCode(output.substring(272,280).trim()); 
		vehicle.setHubraumZul_ges_gew(output.substring(280,285).trim()); 
		vehicle.setFahrzeugTyp(output.substring(285,303).trim()) ;
		vehicle.setFahrzeugTypgruppe(output.substring(303,305).trim()); 
		vehicle.setFahrzeugArt(output.substring(305,307).trim());

		if(!output.substring(307,309).trim().equalsIgnoreCase("00") && !output.substring(309,311).trim().equalsIgnoreCase("00") && !output.substring(311,315).trim().equalsIgnoreCase("0000")){
			vehicle.setLetzteZulassung(output.substring(307,309).trim()+"/"+output.substring(309,311).trim()+"/"+output.substring(311,315).trim()); 
		}else{
			vehicle.setLetzteZulassung("");
		}
		if(!output.substring(315,317).trim().equalsIgnoreCase("00") && !output.substring(317,319).trim().equalsIgnoreCase("00") && !output.substring(319,323).trim().equalsIgnoreCase("0000")){
			vehicle.setErsteZulassung(output.substring(315,317).trim()+"/"+output.substring(317,319).trim()+"/"+output.substring(319,323).trim()); 
		}else{
			vehicle.setErsteZulassung("");
		}
		vehicle.setZulassungsart(output.substring(323,324).trim()); 
		vehicle.setEigenverkauf(output.substring(324,325).trim().equalsIgnoreCase("Y")?true:false);
		vehicle.setNutzlastAnz_platze(output.substring(325,330).trim()); 
		vehicle.setAntriebsart(output.substring(330,331).trim()); 
		vehicle.setAnzahlVorbesitzer(output.substring(331,333).trim()); 
		vehicle.setLaufleistung(output.substring(333,340).trim()); 
		vehicle.setLaufleistungKennzeichen(output.substring(340,341).trim());
		vehicle.setGetriebeNummer(output.substring(341,361).trim());
		vehicle.setAchsNummer(output.substring(361,381).trim()); 
		vehicle.setLaufleistAustauschmotor(output.substring(381,388).trim());

		//Second Screen
		vehicle.setFuhrparkNr(output.substring(588,598).trim()); 
		vehicle.setVerkaufer(output.substring(598,604).trim()); 
		vehicle.setAufragsNummer(output.substring(604,614).trim()); 
		vehicle.setWartungsGruppe(output.substring(614,615).trim()); 
		vehicle.setWartungsVertrag1(output.substring(615,616).trim());
		vehicle.setWartungsVertrag2(output.substring(616,617).trim());
		vehicle.setWartungsVertrag3(output.substring(617,618).trim());
		vehicle.setWartungsVertrag4(output.substring(618,619).trim());
		vehicle.setWartungsVertrag5(output.substring(619,620).trim());
		vehicle.setWartungsVertrag6(output.substring(620,621).trim()); 

		if(!output.substring(621,623).trim().equalsIgnoreCase("00") && !output.substring(623,625).trim().equalsIgnoreCase("00") && !output.substring(625,627).trim().equalsIgnoreCase("00")){
			vehicle.setBestellDatum(output.substring(621,623).trim()+"/"+output.substring(623,625).trim()+"/20"+output.substring(625,627).trim()); 
		}else{
			vehicle.setBestellDatum("");
		}
		if(!output.substring(627,629).trim().equalsIgnoreCase("00") && !output.substring(629,631).trim().equalsIgnoreCase("00") && !output.substring(631,633).trim().equalsIgnoreCase("00")){
			vehicle.setLieferUbernahmedatum(output.substring(627,629).trim()+"/"+output.substring(629,631).trim()+"/20"+output.substring(631,633).trim()); 
		}else{
			vehicle.setLieferUbernahmedatum("");
		}
		vehicle.setKundendienstBerater(output.substring(633,635).trim());
		if(!output.substring(635,637).trim().equalsIgnoreCase("00") && !output.substring(637,639).trim().equalsIgnoreCase("00") && !output.substring(639,643).trim().equalsIgnoreCase("0000")){
			vehicle.setBaujahr(output.substring(635,637).trim()+"/"+output.substring(637,639).trim()+"/"+output.substring(639,643).trim());
		}else{
			vehicle.setBaujahr("");	
		}
		vehicle.setUnfall(output.substring(643,644).trim().equalsIgnoreCase("Y")?true:false); 
		vehicle.setVergleichsklasse(output.substring(644,646).trim()); 
		vehicle.setBetreuendeWerkstatt(output.substring(646,654).trim()); 
		vehicle.setVerkaufssparte(output.substring(654,656).trim()); 
		vehicle.setAkquisitionsperre(output.substring(656,658).trim()); 
		vehicle.setAuftragsZusatzText(output.substring(658,688).trim()); 
		vehicle.setInfoText(output.substring(688,708).trim()); 

		//Third Screen
		if(!output.substring(910,912).trim().equalsIgnoreCase("00") && !output.substring(912,914).trim().equalsIgnoreCase("00")){
			vehicle.setHauptUntersuchung1("01/"+output.substring(910,912).trim()+"/20"+output.substring(912,914).trim()); 
		}else{
			vehicle.setHauptUntersuchung1(""); 	
		}
		//vehicle.setHauptUntersuchung2(output.substring(909,911)); 
		vehicle.setHuKennzeichen(output.substring(914,916).trim());

		if(!output.substring(918,920).trim().equalsIgnoreCase("00") && !output.substring(920,922).trim().equalsIgnoreCase("00")){
			vehicle.setSicherheitsprufung1("01/"+output.substring(918,920).trim()+"/20"+output.substring(920,922).trim()); 
		}else{
			vehicle.setSicherheitsprufung1("");
		}
		//vehicle.setSicherheitsprufung2(output.substring()); 
		if(!output.substring(924,926).trim().equalsIgnoreCase("00") && !output.substring(926,928).trim().equalsIgnoreCase("00")){
			vehicle.setUnfallverhutungsvorschrift1("01/"+output.substring(924,926).trim()+"/20"+output.substring(926,928).trim()); 
		}else{
			vehicle.setUnfallverhutungsvorschrift1("");
		}
		//vehicle.setUnfallverhutungsvorschrift2(output.substring()); 
		if(!output.substring(928,930).trim().equalsIgnoreCase("00") && !output.substring(930,932).trim().equalsIgnoreCase("00") && !output.substring(932,934).trim().equalsIgnoreCase("00")){
			vehicle.setTachoUntersuchung(output.substring(928,930).trim()+"/"+output.substring(930,932).trim()+"/20"+output.substring(932,934).trim()); 
		}else{
			vehicle.setTachoUntersuchung("");	
		}
		vehicle.setAnzahlWerkstattbesuche(output.substring(934,937).trim()); 
		if(!output.substring(937,939).trim().equalsIgnoreCase("00") && !output.substring(939,941).trim().equalsIgnoreCase("00") && !output.substring(941,943).trim().equalsIgnoreCase("00")){
			vehicle.setLetzterWerkstattbesuch(output.substring(937,939).trim()+"/"+output.substring(939,941).trim()+"/20"+output.substring(941,943).trim()); 
		}else{
			vehicle.setLetzterWerkstattbesuch("");	
		}
		vehicle.setLaufleistun(output.substring(943,950).trim()); 
		if(!output.substring(950,952).trim().equalsIgnoreCase("00") && !output.substring(952,954).trim().equalsIgnoreCase("00") && !output.substring(954,956).trim().equalsIgnoreCase("00")){
			vehicle.setLetzteWartung(output.substring(950,952).trim()+"/"+output.substring(952,954).trim()+"/20"+output.substring(954,956).trim()); 
		}else{
			vehicle.setLetzteWartung("");
		}
		vehicle.setLaufleistung4thScreen(output.substring(956,963).trim());
		if(!output.substring(963,965).trim().equalsIgnoreCase("00") && !output.substring(965,967).trim().equalsIgnoreCase("00")){
			vehicle.setKuhlmittelwechsel1("01/"+output.substring(963,965).trim()+"/20"+output.substring(965,967).trim());
		}else{
			vehicle.setKuhlmittelwechsel1("");
		}
		//vehicle.setKuhlmittelwechsel2(); 
		if(!output.substring(967,969).trim().equalsIgnoreCase("00") && !output.substring(969,971).trim().equalsIgnoreCase("00")){
			vehicle.setBremsflussigkeitswechsel1("01/"+output.substring(967,969).trim()+"/20"+output.substring(969,971).trim());
		}else{
			vehicle.setBremsflussigkeitswechsel1("");	
		}
		//vehicle.setBremsflussigkeitswechsel2(); 
		vehicle.setAssyst(output.substring(971,972).trim().equalsIgnoreCase("J")?true:false); 
		vehicle.setArt_l_su(output.substring(972,974).trim()); 
		vehicle.setSu_Ba_km(output.substring(974,981).trim()); 
		if(!output.substring(981,983).trim().equalsIgnoreCase("00") && !output.substring(983,985).trim().equalsIgnoreCase("00") && !output.substring(985,989).trim().equalsIgnoreCase("0000")){
			vehicle.setSu_b_ab(output.substring(981,983).trim()+"/"+output.substring(983,985).trim()+"/"+output.substring(985,989).trim()); 
		}else{
			vehicle.setSu_b_ab("");	
		}

		//Fifth Scren
		vehicle.setKundenNumberVega(output.substring(1189,1197).trim()); 
		vehicle.setVegaName(output.substring(1197,1222).trim()); 
		vehicle.setKundenNumberBelegdruck(output.substring(1222,1230).trim()); 
		vehicle.setBelegdruck_Name(output.substring(1230,1255).trim());
		vehicle.setServicevertragVertragsNumber(output.substring(1255,1275).trim());

		if(!output.substring(1275,1277).trim().equalsIgnoreCase("00") && !output.substring(1277,1279).trim().equalsIgnoreCase("00") && !output.substring(1279,1283).trim().equalsIgnoreCase("0000")){
			vehicle.setLaufzeit1(output.substring(1275,1277).trim()+"/"+output.substring(1277,1279).trim()+"/"+output.substring(1279,1283).trim());
		}else{
			vehicle.setLaufzeit1("");	
		}
		if(!output.substring(1283,1285).trim().equalsIgnoreCase("00") && !output.substring(1285,1287).trim().equalsIgnoreCase("00") && !output.substring(1287,1291).trim().equalsIgnoreCase("0000")){
			vehicle.setLaufzeit2(output.substring(1283,1285).trim()+"/"+output.substring(1285,1287).trim()+"/"+output.substring(1287,1291).trim());
		}else{
			vehicle.setLaufzeit2("");	
		}
		vehicle.setServicevertrag_kmBegrenzung(output.substring(1291,1298).trim());
		vehicle.setVersichererko_KundenNumber(output.substring(1298,1306).trim());
		vehicle.setVersichererko_Name(output.substring(1306,1331).trim()); 
		vehicle.setVersichererko_VertragsNumber(output.substring(1331,1351).trim()); 

		if(!output.substring(1351,1353).trim().equalsIgnoreCase("00") && !output.substring(1353,1355).trim().equalsIgnoreCase("00") && !output.substring(1355,1359).trim().equalsIgnoreCase("0000")){
			vehicle.setVersichererko_laufzeit1(output.substring(1351,1353).trim()+"/"+output.substring(1353,1355).trim()+"/"+output.substring(1355,1359).trim());
		}else{
			vehicle.setVersichererko_laufzeit1("");	
		}
		if(!output.substring(1359,1361).trim().equalsIgnoreCase("00") && !output.substring(1361,1363).trim().equalsIgnoreCase("00") && !output.substring(1363,1367).trim().equalsIgnoreCase("0000")){
			vehicle.setVersichererko_laufzeit2(output.substring(1359,1361).trim()+"/"+output.substring(1361,1363).trim()+"/"+output.substring(1363,1367).trim());
		}else{
			vehicle.setVersichererko_laufzeit2("");	
		}

		vehicle.setVersichererko_kmBegrenzung(output.substring(1367,1374).trim()); 
		vehicle.setWeitererVertrag_KundenNumber(output.substring(1374,1382).trim()); 
		vehicle.setWeitererVertrag_Name(output.substring(1382,1407).trim()); 
		vehicle.setWeitererVertrag_VertragsNumber(output.substring(1407,1427).trim()); 

		if(!output.substring(1427,1429).trim().equalsIgnoreCase("00") && !output.substring(1429,1431).trim().equalsIgnoreCase("00") && !output.substring(1431,1435).trim().equalsIgnoreCase("0000")){
			vehicle.setWeitererVertrag_laufzeit1(output.substring(1427,1429).trim()+"/"+output.substring(1429,1431).trim()+"/"+output.substring(1431,1435).trim());
		}else{
			vehicle.setWeitererVertrag_laufzeit1("");	
		}
		if(!output.substring(1435,1437).trim().equalsIgnoreCase("00") && !output.substring(1437,1439).trim().equalsIgnoreCase("00") && !output.substring(1439,1443).trim().equalsIgnoreCase("0000")){
			vehicle.setWeitererVertrag_laufzeit2(output.substring(1435,1437).trim()+"/"+output.substring(1437,1439).trim()+"/"+output.substring(1439,1443).trim());
		}else{
			vehicle.setWeitererVertrag_laufzeit2("");	
		}
		vehicle.setWeitererVertrag_kmBegrenzung(output.substring(1443,1450).trim()); 

		//6th Screen
		vehicle.setFinanzierunq_KundenNumber(output.substring(1650,1658).trim()); 
		vehicle.setFinanzierunq_Name(output.substring(1658,1683).trim()); 
		vehicle.setFinanzierunq_VertragsNumber(output.substring(1683,1703).trim()); 
		if(!output.substring(1703,1705).trim().equalsIgnoreCase("00") && !output.substring(1705,1707).trim().equalsIgnoreCase("00") && !output.substring(1707,1711).trim().equalsIgnoreCase("0000")){
			vehicle.setFinanzierunq_laufzeit1(output.substring(1703,1705).trim()+"/"+output.substring(1705,1707).trim()+"/"+output.substring(1707,1711).trim());
		}else{
			vehicle.setFinanzierunq_laufzeit1("");	
		}
		if(!output.substring(1711,1713).trim().equalsIgnoreCase("00") && !output.substring(1713,1715).trim().equalsIgnoreCase("00") && !output.substring(1715,1719).trim().equalsIgnoreCase("0000")){
			vehicle.setFinanzierunq_laufzeit2(output.substring(1711,1713).trim()+"/"+output.substring(1713,1715).trim()+"/"+output.substring(1715,1719).trim()); 
		}else{
			vehicle.setFinanzierunq_laufzeit2("");	
		}

		vehicle.setFinanzierunq_kmBegrenzung(output.substring(1719,1726).trim()); 
		vehicle.setFinanzierungsart1(output.substring(1726,1727).trim()); 
		vehicle.setFinanzierungsart2(output.substring(1727,1757).trim()); 
		vehicle.setFinanzGesellschaft1(output.substring(1757,1759).trim()); 
		vehicle.setFinanzGesellschaft2(output.substring(1759,1789).trim()); 
		vehicle.setAktion1(output.substring(1789,1793).trim()); 
		vehicle.setAktion2(output.substring(1793,1818).trim()); 
		vehicle.setKartenNumber(output.substring(1818,1838).trim());

		if(!output.substring(1838,1840).trim().equalsIgnoreCase("00") && !output.substring(1840,1842).trim().equalsIgnoreCase("00") && !output.substring(1842,1846).trim().equalsIgnoreCase("0000")){
			vehicle.setVorteilsaktion_laufzeit1(output.substring(1838,1840).trim()+"/"+output.substring(1840,1842).trim()+"/"+output.substring(1842,1846).trim());
		}else{
			vehicle.setVorteilsaktion_laufzeit1("");	
		}
		if(!output.substring(1846,1848).trim().equalsIgnoreCase("00") && !output.substring(1848,1850).trim().equalsIgnoreCase("00") && !output.substring(1850,1854).trim().equalsIgnoreCase("0000")){
			vehicle.setVorteilsaktion_laufzeit2(output.substring(1846,1848).trim()+"/"+output.substring(1848,1850).trim()+"/"+output.substring(1850,1854).trim());
		}else{
			vehicle.setVorteilsaktion_laufzeit2("");	
		}
		vehicle.setVorteilsaktion_kmBegrenzung(output.substring(1854,1861).trim()); 

		//set seventh screen data

		vehicle.setBriefkastentext(output.substring(2061,2099).trim());
		vehicle.setDruckkennzeichenChb(output.substring(2099, 2100).trim().equalsIgnoreCase("D")?true:false);
		vehicle.setBriefkastentext1(output.substring(2100,2138).trim());
		vehicle.setDruckkennzeichenChb1(output.substring(2138, 2139).trim().equalsIgnoreCase("D")?true:false);
		vehicle.setBriefkastentext2(output.substring(2139,2177).trim());
		vehicle.setDruckkennzeichenChb2(output.substring(2177, 2178).trim().equalsIgnoreCase("D")?true:false);
		vehicle.setBriefkastentext3(output.substring(2178,2216).trim());
		vehicle.setDruckkennzeichenChb3(output.substring(2216, 2217).trim().equalsIgnoreCase("D")?true:false);
		vehicle.setBriefkastentext4(output.substring(2217,2255).trim());
		vehicle.setDruckkennzeichenChb4(output.substring(2255, 2256).trim().equalsIgnoreCase("D")?true:false);
		vehicle.setBriefkastentext5(output.substring(2256,2294).trim());
		vehicle.setDruckkennzeichenChb5(output.substring(2294, 2295).trim().equalsIgnoreCase("D")?true:false);
		vehicle.setBriefkastentext6(output.substring(2295,2333).trim());
		vehicle.setDruckkennzeichenChb6(output.substring(2333, 2334).trim().equalsIgnoreCase("D")?true:false);

		vehicle.setReturnCode(output.substring(2334,2338).trim());
		vehicle.setReturnMsg(output.substring(2338,2538).trim());
		vehicle.setPopUpMsg(output.substring(2538,2634).trim());


		/*List< String> errorList = new ArrayList<String>();
		errorList.add(output.substring(20,220).trim());
		errorList.add(output.substring(387,587).trim());
		errorList.add(output.substring(707,907).trim());
		errorList.add(output.substring(984,1184).trim());
		errorList.add(output.substring(1445,1645).trim());
		errorList.add(output.substring(1856,2056).trim());
		vehicle.setErrorList(errorList);

		vehicle.setReturnCode(output.substring(2061,2065).trim());
		vehicle.setReturnMsg(output.substring(2065,2265).trim());
		vehicle.setPopUpMsg(output.substring(2265,2361).trim());*/

	}


	private String createInputString(VehicleDetailsVO vehicleDtl, String indicaterFlag) {

		String indicater = StringUtils.rightPad(indicaterFlag,RestInputConstants.INDICATER, " ");

		String firstScreen = StringUtils.rightPad(vehicleDtl.getAmtlichesKennzeichen() ,RestInputConstants.AMTLICHESKENNZEICHEN  ," ")
				+ StringUtils.rightPad(vehicleDtl.getKundenNummer() ,RestInputConstants.KUNDENNUMMER ," ")
				+ StringUtils.rightPad("",200, " ");

		log.info("FirstScreen :" +firstScreen);
		log.info("FirstScreen Length :" +firstScreen.length());


		String lastAdmission_day = "";
		String lastAdmission_month= "";
		String lastAdmission_year = "";
		String firstRegistration_day = "";
		String firstRegistration_month = "";
		String firstRegistration_year = "";

		String lastAdmission = vehicleDtl.getLetzteZulassung();
		if(lastAdmission != null && !lastAdmission.isEmpty() && checkDateFormat(lastAdmission,"lastAdmission")){
			String values[] = lastAdmission.split("/");
			lastAdmission_day= StringUtils.leftPad(values[0],2,"0");
			lastAdmission_month = StringUtils.leftPad(values[1],2,"0");
			lastAdmission_year = values[2];
		}

		String firstRegistration = vehicleDtl.getErsteZulassung();
		if(firstRegistration != null && !firstRegistration.isEmpty() && checkDateFormat(firstRegistration,"firstRegistration")){
			String values[] = firstRegistration.split("/");
			firstRegistration_day= StringUtils.leftPad(values[0],2,"0");
			firstRegistration_month = StringUtils.leftPad(values[1],2,"0");
			firstRegistration_year = values[2];
		}

		String secondScreen  = StringUtils.rightPad(vehicleDtl.getMarke() ,RestInputConstants.MARKE ," ")
				+ StringUtils.rightPad(vehicleDtl.getFzIdentnr1() ,RestInputConstants.FZIDENTNR1 ," ")
				+ StringUtils.rightPad(vehicleDtl.getFzIdentnr2() ,RestInputConstants.FZIDENTNR2 ," ")
				+ StringUtils.rightPad(vehicleDtl.getFzIdentnr3() ,RestInputConstants.FZIDENTNR3 ," ")
				+ StringUtils.rightPad(vehicleDtl.getFzIdentnr4() ,RestInputConstants.FZIDENTNR4 ," ")
				+ StringUtils.rightPad(vehicleDtl.getFzIdentnr5() ,RestInputConstants.FZIDENTNR5 ," ")
				+ StringUtils.rightPad("" ,1 ," ") // Add extra value in input string
				+ StringUtils.rightPad(vehicleDtl.getMotorNummer() ,RestInputConstants.MOTORNUMMER ," ")
				+ StringUtils.rightPad(vehicleDtl.getFamilienGruppe() ,RestInputConstants.FAMILIENGRUPPE ," ")
				+ StringUtils.rightPad(vehicleDtl.getTypenkennzahl() ,RestInputConstants.TYPENKENNZAHL ," ")
				+ StringUtils.rightPad(vehicleDtl.getTypNummer() ,RestInputConstants.TYPNUMMER ," ")
				+ StringUtils.rightPad(vehicleDtl.getTypVariante() ,RestInputConstants.TYPVARIANTE ," ")
				+ StringUtils.leftPad(vehicleDtl.getMotorstarkeKW() ,RestInputConstants.MOTORSTARKEKW ," ") //leftPad
				+ StringUtils.rightPad(vehicleDtl.getHerstellerCode() ,RestInputConstants.HERSTELLERCODE ," ")
				+ StringUtils.rightPad(vehicleDtl.getTypCode() ,RestInputConstants.TYPCODE ," ")
				+ StringUtils.leftPad(vehicleDtl.getHubraumZul_ges_gew() ,RestInputConstants.HUBRAUMZUL_GES_GEW ," ")//leftPad
				+ StringUtils.rightPad(vehicleDtl.getFahrzeugTyp() ,RestInputConstants.FAHRZEUGTYP ," ")
				+ StringUtils.rightPad(vehicleDtl.getFahrzeugTypgruppe() ,RestInputConstants.FAHRZEUGTYPGRUPPE ," ")
				+ StringUtils.rightPad(vehicleDtl.getFahrzeugArt() ,RestInputConstants.FAHRZEUGART ," ")
				+ StringUtils.rightPad(lastAdmission_day,RestInputConstants.LETZTEZULASSUNG_DD ,"0")
				+ StringUtils.rightPad(lastAdmission_month,RestInputConstants.LETZTEZULASSUNG_MM ,"0")
				+ StringUtils.rightPad(lastAdmission_year,RestInputConstants.LETZTEZULASSUNG_YYYY ,"0")
				+ StringUtils.rightPad(firstRegistration_day,RestInputConstants.ERSTEZULASSUNG_DD ,"0")
				+ StringUtils.rightPad(firstRegistration_month ,RestInputConstants.ERSTEZULASSUNG_MM ,"0")
				+ StringUtils.rightPad(firstRegistration_year ,RestInputConstants.ERSTEZULASSUNG_YYYY ,"0")
				+ StringUtils.rightPad(vehicleDtl.getZulassungsart() ,RestInputConstants.ZULASSUNGSART ,"0")
				+ StringUtils.rightPad(vehicleDtl.isEigenverkauf()==true?"Y":"" ,RestInputConstants.EIGENVERKAUF ," ")
				+ StringUtils.leftPad(vehicleDtl.getNutzlastAnz_platze() ,RestInputConstants.NUTZLASTANZ_PLATZE ," ")//leftPad
				+ StringUtils.rightPad(vehicleDtl.getAntriebsart() ,RestInputConstants.ANTRIEBSART ," ")
				+ StringUtils.leftPad(vehicleDtl.getAnzahlVorbesitzer() ,RestInputConstants.ANZAHLVORBESITZER ," ")//leftPad
				+ StringUtils.leftPad(vehicleDtl.getLaufleistung() ,RestInputConstants.LAUFLEISTUNG ," ")//leftPad
				+ StringUtils.rightPad(vehicleDtl.getLaufleistungKennzeichen() ,RestInputConstants.LAUFLEISTUNGKENNZEICHEN ," ")
				+ StringUtils.rightPad(vehicleDtl.getGetriebeNummer() ,RestInputConstants.GETRIEBENUMMER ," ")
				+ StringUtils.rightPad(vehicleDtl.getAchsNummer() ,RestInputConstants.ACHSNUMMER ," ")
				+ StringUtils.leftPad(vehicleDtl.getLaufleistAustauschmotor() ,RestInputConstants.LAUFLEISTAUSTAUSCHMOTOR ," ")
				+ StringUtils.rightPad("",200, " ");

		log.info("SecondScreen :" +secondScreen);
		log.info("SecondScreen Length :" +secondScreen.length());

		String orderDate_day= "";
		String orderDate_month = "";
		String orderDate_year = "";
		String deliveryDate_day= "";
		String deliveryDate_month = "";
		String deliveryDate_year = "";
		String constructionYear_day= "";
		String constructionYear_month = "";
		String constructionYear_year = "";

		String orderDate = vehicleDtl.getBestellDatum();
		if(orderDate != null && !orderDate.isEmpty() && checkDateFormat(orderDate,"orderDate")){
			String values[] = orderDate.split("/");
			orderDate_day= StringUtils.leftPad(values[0],2,"0");
			orderDate_month = StringUtils.leftPad(values[1],2,"0");
			orderDate_year = values[2].substring(2);
		}
		String deliveryDate = vehicleDtl.getLieferUbernahmedatum();
		if(deliveryDate != null && !deliveryDate.isEmpty() && checkDateFormat(deliveryDate,"deliveryDate")){
			String values[] = deliveryDate.split("/");
			deliveryDate_day= StringUtils.leftPad(values[0],2,"0");
			deliveryDate_month = StringUtils.leftPad(values[1],2,"0");
			deliveryDate_year = values[2].substring(2);
		}
		String constructionYear = vehicleDtl.getBaujahr();
		if(constructionYear != null && !constructionYear.isEmpty() && checkDateFormat(constructionYear,"constructionYear")){
			String values[] = constructionYear.split("/");
			constructionYear_day= StringUtils.leftPad(values[0],2,"0");
			constructionYear_month = StringUtils.leftPad(values[1],2,"0");
			constructionYear_year = values[2];
		}

		String thirdScreen  = StringUtils.rightPad(vehicleDtl.getFuhrparkNr() ,RestInputConstants.FUHRPARKNR ," ")
				+ StringUtils.rightPad(vehicleDtl.getVerkaufer() ,RestInputConstants.VERKAUFER ," ")
				+ StringUtils.rightPad(vehicleDtl.getAufragsNummer() ,RestInputConstants.AUFRAGSNUMMER ," ")
				+ StringUtils.rightPad(vehicleDtl.getWartungsGruppe() ,RestInputConstants.WARTUNGSGRUPPE ," ")
				+ StringUtils.rightPad(vehicleDtl.getWartungsVertrag1(),RestInputConstants.WARTUNGSVERTRAG1 ," ")
				+ StringUtils.rightPad(vehicleDtl.getWartungsVertrag2(),RestInputConstants.WARTUNGSVERTRAG2 ," ")
				+ StringUtils.rightPad(vehicleDtl.getWartungsVertrag3() ,RestInputConstants.WARTUNGSVERTRAG3 ," ")
				+ StringUtils.rightPad(vehicleDtl.getWartungsVertrag4(),RestInputConstants.WARTUNGSVERTRAG4 ," ")
				+ StringUtils.rightPad(vehicleDtl.getWartungsVertrag5(),RestInputConstants.WARTUNGSVERTRAG5 ," ")
				+ StringUtils.rightPad(vehicleDtl.getWartungsVertrag6(),RestInputConstants.WARTUNGSVERTRAG6 ," ")
				+ StringUtils.rightPad(orderDate_day ,RestInputConstants.BESTELLDATUM_DD ,"0")
				+ StringUtils.rightPad(orderDate_month ,RestInputConstants.BESTELLDATUM_MM ,"0")
				+ StringUtils.rightPad(orderDate_year,RestInputConstants.BESTELLDATUM_YY ,"0")
				+ StringUtils.rightPad(deliveryDate_day,RestInputConstants.LIEFERUBERNAHMEDATUM_DD ,"0")
				+ StringUtils.rightPad(deliveryDate_month,RestInputConstants.LIEFERUBERNAHMEDATUM_MM ,"0")
				+ StringUtils.rightPad(deliveryDate_year,RestInputConstants.LIEFERUBERNAHMEDATUM_YY ,"0")
				+ StringUtils.rightPad(vehicleDtl.getKundendienstBerater() ,RestInputConstants.KUNDENDIENSTBERATER ," ")
				+ StringUtils.rightPad(constructionYear_day,RestInputConstants.BAUJAHR_DD ,"0")
				+ StringUtils.rightPad(constructionYear_month,RestInputConstants.BAUJAHR_MM ,"0")
				+ StringUtils.rightPad(constructionYear_year,RestInputConstants.BAUJAHR_YYYY ,"0")
				+ StringUtils.rightPad(vehicleDtl.isUnfall()==true?"Y":"" ,RestInputConstants.UNFALL ," ")
				+ StringUtils.leftPad(vehicleDtl.getVergleichsklasse() ,RestInputConstants.VERGLEICHSKLASSE ," ") //leftPad
				+ StringUtils.rightPad(vehicleDtl.getBetreuendeWerkstatt() ,RestInputConstants.BETREUENDEWERKSTATT ," ")
				+ StringUtils.rightPad(vehicleDtl.getVerkaufssparte() ,RestInputConstants.VERKAUFSSPARTE ," ")
				+ StringUtils.rightPad(vehicleDtl.getAkquisitionsperre() ,RestInputConstants.AKQUISITIONSPERRE ," ")
				+ StringUtils.rightPad(vehicleDtl.getAuftragsZusatzText() ,RestInputConstants.AUFTRAGSZUSATZTEXT ," ")
				+ StringUtils.rightPad(vehicleDtl.getInfoText() ,RestInputConstants.INFOTEXT ," ")
				+ StringUtils.rightPad("",200, " ");

		log.info("ThirdScreen :" +thirdScreen);
		log.info("ThirdScreen Length :" +thirdScreen.length());

		String speedometerInspection_day = "";
		String speedometerInspection_month = "";
		String speedometerInspection_year = "";
		String lastVisit_day = "";
		String lastVisit_month = "";
		String lastVisit_year = "";
		String lastMaintenance_day = "";
		String lastMaintenance_month = "";
		String lastMaintenance_year = "";
		String su_b_ab_day = "";
		String su_b_ab_month = "";
		String su_b_ab_year = "";

		String speedometerInspection = vehicleDtl.getTachoUntersuchung();
		if(speedometerInspection != null && !speedometerInspection.isEmpty() && checkDateFormat(speedometerInspection,"speedometerInspection")){
			String values[] = speedometerInspection.split("/");
			speedometerInspection_day= StringUtils.leftPad(values[0],2,"0");
			speedometerInspection_month = StringUtils.leftPad(values[1],2,"0");
			speedometerInspection_year = values[2].substring(2);
		}
		String lastVisit = vehicleDtl.getLetzterWerkstattbesuch();
		if(lastVisit != null && !lastVisit.isEmpty() && checkDateFormat(lastVisit,"lastVisit")){
			String values[] = lastVisit.split("/");
			lastVisit_day= StringUtils.leftPad(values[0],2,"0");
			lastVisit_month = StringUtils.leftPad(values[1],2,"0");
			lastVisit_year = values[2].substring(2);
		}
		String lastMaintenance = vehicleDtl.getLetzteWartung();
		if(lastMaintenance != null && !lastMaintenance.isEmpty() && checkDateFormat(lastMaintenance,"lastMaintenance")){
			String values[] = lastMaintenance.split("/");
			lastMaintenance_day= StringUtils.leftPad(values[0],2,"0");
			lastMaintenance_month = StringUtils.leftPad(values[1],2,"0");
			lastMaintenance_year = values[2].substring(2);
		}
		String su_b_ab = vehicleDtl.getSu_b_ab();
		if(su_b_ab != null && !su_b_ab.isEmpty() && checkDateFormat(su_b_ab,"su_b_ab")){
			String values[] = su_b_ab.split("/");
			su_b_ab_day= StringUtils.leftPad(values[0],2,"0");
			su_b_ab_month = StringUtils.leftPad(values[1],2,"0");
			su_b_ab_year = values[2];
		}

		String hauptUntersuchung_mm = "";
		String hauptUntersuchung_yy = "";
		String hauptUntersuchung = vehicleDtl.getHauptUntersuchung1();
		if(hauptUntersuchung != null && !hauptUntersuchung.isEmpty() && checkDateFormat(hauptUntersuchung,"hauptUntersuchung")){
			String values[] = lastMaintenance.split("/");
			//lastMaintenance_day= StringUtils.leftPad(values[0],2,"0");
			hauptUntersuchung_mm = StringUtils.leftPad(values[1],2,"0");
			hauptUntersuchung_yy = values[2].substring(2);
		}

		String sicherheitsprufung_mm = "";
		String sicherheitsprufung_yy = "";
		String sicherheitsprufung = vehicleDtl.getSicherheitsprufung1();
		if(sicherheitsprufung != null && !sicherheitsprufung.isEmpty() && checkDateFormat(sicherheitsprufung,"sicherheitsprufung")){
			String values[] = lastMaintenance.split("/");
			//lastMaintenance_day= StringUtils.leftPad(values[0],2,"0");
			sicherheitsprufung_mm = StringUtils.leftPad(values[1],2,"0");
			sicherheitsprufung_yy = values[2].substring(2);
		}

		String unfallverhutungsvorschrift_mm = "";
		String unfallverhutungsvorschrift_yy = "";
		String unfallverhutungsvorschrift = vehicleDtl.getUnfallverhutungsvorschrift1();
		if(unfallverhutungsvorschrift != null && !unfallverhutungsvorschrift.isEmpty() && checkDateFormat(unfallverhutungsvorschrift,"unfallverhutungsvorschrift")){
			String values[] = lastMaintenance.split("/");
			//lastMaintenance_day= StringUtils.leftPad(values[0],2,"0");
			unfallverhutungsvorschrift_mm = StringUtils.leftPad(values[1],2,"0");
			unfallverhutungsvorschrift_yy = values[2].substring(2);
		}

		String kuhlmittelwechsel_mm = "";
		String kuhlmittelwechsel_yy = "";
		String kuhlmittelwechsel = vehicleDtl.getKuhlmittelwechsel1();
		if(kuhlmittelwechsel != null && !kuhlmittelwechsel.isEmpty() && checkDateFormat(kuhlmittelwechsel,"kuhlmittelwechsel")){
			String values[] = lastMaintenance.split("/");
			//lastMaintenance_day= StringUtils.leftPad(values[0],2,"0");
			kuhlmittelwechsel_mm = StringUtils.leftPad(values[1],2,"0");
			kuhlmittelwechsel_yy = values[2].substring(2);
		}

		String bremsflussigkeitswechsel_mm = "";
		String bremsflussigkeitswechsel_yy = "";
		String bremsflussigkeitswechsel = vehicleDtl.getBremsflussigkeitswechsel1();
		if(bremsflussigkeitswechsel != null && !bremsflussigkeitswechsel.isEmpty() && checkDateFormat(bremsflussigkeitswechsel,"bremsflussigkeitswechsel")){
			String values[] = lastMaintenance.split("/");
			//lastMaintenance_day= StringUtils.leftPad(values[0],2,"0");
			bremsflussigkeitswechsel_mm = StringUtils.leftPad(values[1],2,"0");
			bremsflussigkeitswechsel_yy = values[2].substring(2);
		}

		String fourthScreen =  StringUtils.rightPad("00" ,2 ," ") // Add extra value in input string
				+ StringUtils.rightPad(hauptUntersuchung_mm ,RestInputConstants.hauptUntersuchung1 ,"0")
				+ StringUtils.rightPad(hauptUntersuchung_yy ,RestInputConstants.hauptUntersuchung2 ,"0")
				+ StringUtils.rightPad(vehicleDtl.getHuKennzeichen() ,RestInputConstants.huKennzeichen ," ")
				+  StringUtils.rightPad("00" ,2 ," ") // Add extra value in input string
				+ StringUtils.rightPad(sicherheitsprufung_mm ,RestInputConstants.sicherheitsprufung1 ,"0")
				+ StringUtils.rightPad(sicherheitsprufung_yy ,RestInputConstants.sicherheitsprufung2 ,"0")
				+ StringUtils.rightPad("00" ,RestInputConstants.unfallverhutungsvorschrift1 ," ")
				+ StringUtils.rightPad(unfallverhutungsvorschrift_mm ,RestInputConstants.unfallverhutungsvorschrift1 ,"0")
				+ StringUtils.rightPad(unfallverhutungsvorschrift_yy ,RestInputConstants.unfallverhutungsvorschrift2 ,"0")
				+ StringUtils.rightPad(speedometerInspection_day,RestInputConstants.tachoUntersuchung_dd ,"0")
				+ StringUtils.rightPad(speedometerInspection_month,RestInputConstants.tachoUntersuchung_mm ,"0")
				+ StringUtils.rightPad(speedometerInspection_year,RestInputConstants.tachoUntersuchung_yy ,"0")
				+ StringUtils.leftPad(vehicleDtl.getAnzahlWerkstattbesuche() ,RestInputConstants.AnzahlWerkstattbesuche ," ")
				+ StringUtils.rightPad(lastVisit_day,RestInputConstants.letzterWerkstattbesuch_dd ,"0")
				+ StringUtils.rightPad(lastVisit_month,RestInputConstants.letzterWerkstattbesuch_mm ,"0")
				+ StringUtils.rightPad(lastVisit_year,RestInputConstants.letzterWerkstattbesuch_yy ,"0")
				+ StringUtils.leftPad(vehicleDtl.getLaufleistun() ,RestInputConstants.laufleistun ," ")
				+ StringUtils.rightPad(lastMaintenance_day,RestInputConstants.letzteWartung_dd ,"0")
				+ StringUtils.rightPad(lastMaintenance_month,RestInputConstants.letzteWartung_mm ,"0")
				+ StringUtils.rightPad(lastMaintenance_year,RestInputConstants.letzteWartung_yy ,"0")
				+ StringUtils.leftPad(vehicleDtl.getLaufleistung4thScreen() ,RestInputConstants.laufleistung4thScreen ," ")
				+ StringUtils.rightPad(kuhlmittelwechsel_mm ,RestInputConstants.kuhlmittelwechsel1 ,"0")
				+ StringUtils.rightPad(kuhlmittelwechsel_yy ,RestInputConstants.kuhlmittelwechsel2 ,"0")
				+ StringUtils.rightPad(bremsflussigkeitswechsel_mm ,RestInputConstants.bremsflussigkeitswechsel1 ,"0")
				+ StringUtils.rightPad(bremsflussigkeitswechsel_yy ,RestInputConstants.bremsflussigkeitswechsel2 ,"0")
				+ StringUtils.rightPad(vehicleDtl.isAssyst() == true ? "J":"N" ,RestInputConstants.assyst ," ")
				+ StringUtils.rightPad(vehicleDtl.getArt_l_su() ,RestInputConstants.art_l_su ," ")
				+ StringUtils.leftPad(vehicleDtl.getSu_Ba_km() ,RestInputConstants.su_Ba_km ," ")
				+ StringUtils.rightPad(su_b_ab_day,RestInputConstants.su_b_ab_dd ,"0")
				+ StringUtils.rightPad(su_b_ab_month,RestInputConstants.su_b_ab_mm ,"0")
				+ StringUtils.rightPad(su_b_ab_year,RestInputConstants.su_b_ab_yyyy ,"0")
				+ StringUtils.rightPad("",200, " ");

		log.info("fourthScreen :" +fourthScreen);
		log.info("fourthScreen Length :" +fourthScreen.length());

		String runningTime1_day = "";
		String runningTime1_month = "";
		String runningTime1_year = "";
		String runningTime2_day = "";
		String runningTime2_month = "";
		String runningTime2_year = "";
		String insurance_runningTime1_day = "";
		String insurance_runningTime1_month = "";
		String insurance_runningTime1_year = "";
		String insurance_runningTime2_day = "";
		String insurance_runningTime2_month = "";
		String insurance_runningTime2_year = "";
		String additionalContract1_day = "";
		String additionalContract1_month = "";
		String additionalContract1_year = "";
		String additionalContract2_day = "";
		String additionalContract2_month = "";
		String additionalContract2_year = "";

		String runningTime1 = vehicleDtl.getLaufzeit1();
		if(runningTime1 != null && !runningTime1.isEmpty() && checkDateFormat(runningTime1,"runningTime1")){
			String values[] = runningTime1.split("/");
			runningTime1_day= StringUtils.leftPad(values[0],2,"0");
			runningTime1_month = StringUtils.leftPad(values[1],2,"0");
			runningTime1_year = values[2];
		}
		String runningTime2 = vehicleDtl.getLaufzeit2();
		if(runningTime2 != null && !runningTime2.isEmpty() && checkDateFormat(runningTime2,"runningTime2")){
			String values[] = runningTime2.split("/");
			runningTime2_day= StringUtils.leftPad(values[0],2,"0");
			runningTime2_month = StringUtils.leftPad(values[1],2,"0");
			runningTime2_year = values[2];
		}

		String insurance_runningTime1 = vehicleDtl.getVersichererko_laufzeit1();
		if(insurance_runningTime1 != null && !insurance_runningTime1.isEmpty() && checkDateFormat(insurance_runningTime1,"insurance_runningTime1")){
			String values[] = insurance_runningTime1.split("/");
			insurance_runningTime1_day= StringUtils.leftPad(values[0],2,"0");
			insurance_runningTime1_month = StringUtils.leftPad(values[1],2,"0");
			insurance_runningTime1_year = values[2];
		}

		String insurance_runningTime2 = vehicleDtl.getVersichererko_laufzeit2();
		if(insurance_runningTime2 != null && !insurance_runningTime2.isEmpty() && checkDateFormat(insurance_runningTime2,"insurance_runningTime2")){
			String values[] = insurance_runningTime2.split("/");
			insurance_runningTime2_day= StringUtils.leftPad(values[0],2,"0");
			insurance_runningTime2_month = StringUtils.leftPad(values[1],2,"0");
			insurance_runningTime2_year = values[2];
		}

		String additionalContract1  = vehicleDtl.getWeitererVertrag_laufzeit1();
		if(additionalContract1 != null && !additionalContract1.isEmpty() && checkDateFormat(additionalContract1,"additionalContract1")){
			String values[] = additionalContract1.split("/");
			additionalContract1_day= StringUtils.leftPad(values[0],2,"0");
			additionalContract1_month = StringUtils.leftPad(values[1],2,"0");
			additionalContract1_year = values[2];
		}

		String additionalContract2  = vehicleDtl.getWeitererVertrag_laufzeit2();
		if(additionalContract2 != null && !additionalContract2.isEmpty() && checkDateFormat(additionalContract2,"additionalContract2")){
			String values[] = additionalContract2.split("/");
			additionalContract2_day= StringUtils.leftPad(values[0],2,"0");
			additionalContract2_month = StringUtils.leftPad(values[1],2,"0");
			additionalContract2_year = values[2];
		}

		String fifthScreen  = StringUtils.rightPad(vehicleDtl.getKundenNumberVega() ,RestInputConstants.KundenNumberVega ," ")
				+ StringUtils.rightPad(vehicleDtl.getVegaName() ,RestInputConstants.VEGANAME ," ")
				+ StringUtils.rightPad(vehicleDtl.getKundenNumberBelegdruck() ,RestInputConstants.kundenNumberBelegdruck ," ")
				+ StringUtils.rightPad(vehicleDtl.getBelegdruck_Name() ,RestInputConstants.BELEGDRUCK_NAME ," ")
				+ StringUtils.rightPad(vehicleDtl.getServicevertragVertragsNumber() ,RestInputConstants.servicevertragVertragsNumber ," ")
				+ StringUtils.rightPad(runningTime1_day,RestInputConstants.laufzeit1_dd ,"0")
				+ StringUtils.rightPad(runningTime1_month,RestInputConstants.laufzeit1_mm ,"0")
				+ StringUtils.rightPad(runningTime1_year,RestInputConstants.laufzeit1_yyyy ,"0")
				+ StringUtils.rightPad(runningTime2_day,RestInputConstants.laufzeit2_dd ,"0")
				+ StringUtils.rightPad(runningTime2_month,RestInputConstants.laufzeit2_mm ,"0")
				+ StringUtils.rightPad(runningTime2_year,RestInputConstants.laufzeit2_yyyy ,"0")
				+ StringUtils.leftPad(vehicleDtl.getServicevertrag_kmBegrenzung() ,RestInputConstants.servicevertrag_kmBegrenzung ," ")
				+ StringUtils.rightPad(vehicleDtl.getVersichererko_KundenNumber() ,RestInputConstants.versichererko_KundenNumber ," ")
				+ StringUtils.rightPad(vehicleDtl.getVersichererko_Name() ,RestInputConstants.versichererko_Name ," ")
				+ StringUtils.rightPad(vehicleDtl.getVersichererko_VertragsNumber() ,RestInputConstants.versichererko_VertragsNumber ," ")
				+ StringUtils.rightPad(insurance_runningTime1_day,RestInputConstants.versichererko_laufzeit1_dd ,"0")
				+ StringUtils.rightPad(insurance_runningTime1_month,RestInputConstants.versichererko_laufzeit1_mm ,"0")
				+ StringUtils.rightPad(insurance_runningTime1_year,RestInputConstants.versichererko_laufzeit1_yyyy ,"0")
				+ StringUtils.rightPad(insurance_runningTime2_day,RestInputConstants.versichererko_laufzeit2_dd ,"0")
				+ StringUtils.rightPad(insurance_runningTime2_month,RestInputConstants.versichererko_laufzeit2_mm ,"0")
				+ StringUtils.rightPad(insurance_runningTime2_year,RestInputConstants.versichererko_laufzeit2_yyyy ,"0")
				+ StringUtils.leftPad(vehicleDtl.getVersichererko_kmBegrenzung() ,RestInputConstants.versichererko_kmBegrenzung ," ")
				+ StringUtils.rightPad(vehicleDtl.getWeitererVertrag_KundenNumber() ,RestInputConstants.weitererVertrag_KundenNumber ," ")
				+ StringUtils.rightPad(vehicleDtl.getWeitererVertrag_Name() ,RestInputConstants.weitererVertrag_Name ," ")
				+ StringUtils.rightPad(vehicleDtl.getWeitererVertrag_VertragsNumber() ,RestInputConstants.weitererVertrag_VertragsNumber ," ")
				+ StringUtils.rightPad(additionalContract1_day,RestInputConstants.weitererVertrag_laufzeit1_dd ,"0")
				+ StringUtils.rightPad(additionalContract1_month,RestInputConstants.weitererVertrag_laufzeit1_mm ,"0")
				+ StringUtils.rightPad(additionalContract1_year,RestInputConstants.weitererVertrag_laufzeit1_yyyy ,"0")
				+ StringUtils.rightPad(additionalContract2_day,RestInputConstants.weitererVertrag_laufzeit2_dd ,"0")
				+ StringUtils.rightPad(additionalContract2_month,RestInputConstants.weitererVertrag_laufzeit2_mm ,"0")
				+ StringUtils.rightPad(additionalContract2_year,RestInputConstants.weitererVertrag_laufzeit2_yyyy ,"0")
				+ StringUtils.leftPad(vehicleDtl.getWeitererVertrag_kmBegrenzung() ,RestInputConstants.weitererVertrag_kmBegrenzung ," ")
				+ StringUtils.rightPad("",200, " ");

		log.info("fifthScreen :" +fifthScreen);
		log.info("fifthScreen Length :" +fifthScreen.length());

		String financing_runningTime1_day = "";
		String financing_runningTime1_month = "";
		String financing_runningTime1_year = "";
		String financing_runningTime2_day = "";
		String financing_runningTime2_month = "";
		String financing_runningTime2_year = "";
		String specialOffer_runningTime1_day="";
		String specialOffer_runningTime1_month="";
		String specialOffer_runningTime1_year="";
		String specialOffer_runningTime2_day="";
		String specialOffer_runningTime2_month="";
		String specialOffer_runningTime2_year="";

		String financing_runningTime1  = vehicleDtl.getFinanzierunq_laufzeit1();
		if(financing_runningTime1 != null && !financing_runningTime1.isEmpty() && checkDateFormat(financing_runningTime1,"financing_runningTime1")){
			String values[] = financing_runningTime1.split("/");
			financing_runningTime1_day= StringUtils.leftPad(values[0],2,"0");
			financing_runningTime1_month = StringUtils.leftPad(values[1],2,"0");
			financing_runningTime1_year = values[2];
		}

		String financing_runningTime2  = vehicleDtl.getFinanzierunq_laufzeit2();
		if(financing_runningTime2 != null && !financing_runningTime2.isEmpty() && checkDateFormat(financing_runningTime2,"financing_runningTime2")){
			String values[] = financing_runningTime2.split("/");
			financing_runningTime2_day= StringUtils.leftPad(values[0],2,"0");
			financing_runningTime2_month = StringUtils.leftPad(values[1],2,"0");
			financing_runningTime2_year = values[2];
		}

		String specialOffer_runningTime1  = vehicleDtl.getVorteilsaktion_laufzeit1();
		if(specialOffer_runningTime1 != null && !specialOffer_runningTime1.isEmpty() && checkDateFormat(specialOffer_runningTime1,"specialOffer_runningTime1")){
			String values[] = specialOffer_runningTime1.split("/");
			specialOffer_runningTime1_day= StringUtils.leftPad(values[0],2,"0");
			specialOffer_runningTime1_month = StringUtils.leftPad(values[1],2,"0");
			specialOffer_runningTime1_year = values[2];
		}

		String specialOffer_runningTime2  = vehicleDtl.getVorteilsaktion_laufzeit2();
		if(specialOffer_runningTime2 != null && !specialOffer_runningTime2.isEmpty() && checkDateFormat(specialOffer_runningTime2,"specialOffer_runningTime2")){
			String values[] = specialOffer_runningTime2.split("/");
			specialOffer_runningTime2_day= StringUtils.leftPad(values[0],2,"0");
			specialOffer_runningTime2_month = StringUtils.leftPad(values[1],2,"0");
			specialOffer_runningTime2_year = values[2];
		}

		String sixthScreen  = StringUtils.rightPad(vehicleDtl.getFinanzierunq_KundenNumber() ,RestInputConstants.FINANZIERUNQ_KUNDENNUMBER ," ")
				+ StringUtils.rightPad(vehicleDtl.getFinanzierunq_Name() ,RestInputConstants.FINANZIERUNQ_NAME ," ")
				+ StringUtils.rightPad(vehicleDtl.getFinanzierunq_VertragsNumber() ,RestInputConstants.FINANZIERUNQ_VERTRAGSNUMBER ," ")
				+ StringUtils.rightPad(financing_runningTime1_day,RestInputConstants.FINANZIERUNQ_LAUFZEIT1_DD ,"0")
				+ StringUtils.rightPad(financing_runningTime1_month,RestInputConstants.FINANZIERUNQ_LAUFZEIT1_MM ,"0")
				+ StringUtils.rightPad(financing_runningTime1_year,RestInputConstants.FINANZIERUNQ_LAUFZEIT1_YYYY ,"0")
				+ StringUtils.rightPad(financing_runningTime2_day,RestInputConstants.FINANZIERUNQ_LAUFZEIT2_DD ,"0")
				+ StringUtils.rightPad(financing_runningTime2_month,RestInputConstants.FINANZIERUNQ_LAUFZEIT2_MM ,"0")
				+ StringUtils.rightPad(financing_runningTime2_year,RestInputConstants.FINANZIERUNQ_LAUFZEIT2_YYYY ,"0")
				+ StringUtils.leftPad(vehicleDtl.getFinanzierunq_kmBegrenzung() ,RestInputConstants.FINANZIERUNQ_KMBEGRENZUNG ," ")
				+ StringUtils.rightPad(vehicleDtl.getFinanzierungsart1() ,RestInputConstants.FINANZIERUNGSART1 ," ")
				+ StringUtils.rightPad(vehicleDtl.getFinanzierungsart2() ,RestInputConstants.FINANZIERUNGSART2 ," ")
				+ StringUtils.rightPad(vehicleDtl.getFinanzGesellschaft1() ,RestInputConstants.FINANZGESELLSCHAFT1 ," ")
				+ StringUtils.rightPad(vehicleDtl.getFinanzGesellschaft2() ,RestInputConstants.FINANZGESELLSCHAFT2 ," ")
				+ StringUtils.rightPad(vehicleDtl.getAktion1() ,RestInputConstants.AKTION1 ," ")
				+ StringUtils.rightPad(vehicleDtl.getAktion2() ,RestInputConstants.AKTION2 ," ")
				+ StringUtils.rightPad(vehicleDtl.getKartenNumber() ,RestInputConstants.KARTENNUMBER ," ")
				+ StringUtils.rightPad(specialOffer_runningTime1_day,RestInputConstants.VORTEILSAKTION_LAUFZEIT1 ,"0")
				+ StringUtils.rightPad(specialOffer_runningTime1_month,RestInputConstants.VORTEILSAKTION_LAUFZEIT1_MM ,"0")
				+ StringUtils.rightPad(specialOffer_runningTime1_year,RestInputConstants.VORTEILSAKTION_LAUFZEIT1_YYYY ,"0")
				+ StringUtils.rightPad(specialOffer_runningTime2_day,RestInputConstants.VORTEILSAKTION_LAUFZEIT2 ,"0")
				+ StringUtils.rightPad(specialOffer_runningTime2_month,RestInputConstants.VORTEILSAKTION_LAUFZEIT2_MM ,"0")
				+ StringUtils.rightPad(specialOffer_runningTime2_year,RestInputConstants.VORTEILSAKTION_LAUFZEIT2_YYYY ,"0")
				+ StringUtils.leftPad(vehicleDtl.getVorteilsaktion_kmBegrenzung() ,RestInputConstants.VORTEILSAKTION_KMBEGRENZUNG ," ")
				+ StringUtils.rightPad("",200, " ");

		log.info("sixthScreen :" +sixthScreen);
		log.info("sixthScreen Length :" +sixthScreen.length());

		String seventhScreen  = StringUtils.rightPad(vehicleDtl.getBriefkastentext() ,RestInputConstants.BRIEFKASTENTEXT ," ")
				+ StringUtils.rightPad(vehicleDtl.isDruckkennzeichenChb()==true?"D":" " ,RestInputConstants.DRUCKKENNZEICHENCHB ," ")
				+ StringUtils.rightPad(vehicleDtl.getBriefkastentext1() ,RestInputConstants.BRIEFKASTENTEXT1 ," ")
				+ StringUtils.rightPad(vehicleDtl.isDruckkennzeichenChb1()==true?"D":" " ,RestInputConstants.DRUCKKENNZEICHENCHB1 ," ")
				+ StringUtils.rightPad(vehicleDtl.getBriefkastentext2() ,RestInputConstants.BRIEFKASTENTEXT2 ," ")
				+ StringUtils.rightPad(vehicleDtl.isDruckkennzeichenChb2()==true?"D":" " ,RestInputConstants.DRUCKKENNZEICHENCHB2 ," ")
				+ StringUtils.rightPad(vehicleDtl.getBriefkastentext3() ,RestInputConstants.BRIEFKASTENTEXT3 ," ")
				+ StringUtils.rightPad(vehicleDtl.isDruckkennzeichenChb3()==true?"D":" " ,RestInputConstants.DRUCKKENNZEICHENCHB3 ," ")
				+ StringUtils.rightPad(vehicleDtl.getBriefkastentext4() ,RestInputConstants.BRIEFKASTENTEXT4 ," ")
				+ StringUtils.rightPad(vehicleDtl.isDruckkennzeichenChb4()==true?"D":" " ,RestInputConstants.DRUCKKENNZEICHENCHB4 ," ")
				+ StringUtils.rightPad(vehicleDtl.getBriefkastentext5() ,RestInputConstants.BRIEFKASTENTEXT5 ," ")
				+ StringUtils.rightPad(vehicleDtl.isDruckkennzeichenChb5()==true?"D":" " ,RestInputConstants.DRUCKKENNZEICHENCHB5 ," ")
				+ StringUtils.rightPad(vehicleDtl.getBriefkastentext6() ,RestInputConstants.BRIEFKASTENTEXT6 ," ")
				+ StringUtils.rightPad(vehicleDtl.isDruckkennzeichenChb6()==true?"D":" " ,RestInputConstants.DRUCKKENNZEICHENCHB6 ," ");

		log.info("seventhScreen :" +seventhScreen);
		log.info("seventhScreen Length :" +seventhScreen.length());	


		String returnCodeAndMsg = StringUtils.rightPad("",4, " ")
				+ StringUtils.rightPad("",200, " ")
				+ StringUtils.rightPad("",96, " ");

		log.info("returnCodeAndMsg :" +returnCodeAndMsg);
		log.info("returnCodeAndMsg Length :" +returnCodeAndMsg.length());

		String inputString = StringUtils.join(indicater, firstScreen, secondScreen, thirdScreen, fourthScreen, fifthScreen, sixthScreen, seventhScreen, returnCodeAndMsg);

		log.info("InputString :"+inputString);
		log.info("InputString Length :"+inputString.length());


		return inputString;
	}

	boolean checkDateFormat(String date , String field) {
		SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
		sdfrmt.setLenient(false);

		try {
			Date javaDate = sdfrmt.parse(date);
		} catch (Exception e) {
			log.info(date + " is Invalid Date format for " +field);
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.CREATE_FAILED_MSG_KEY, "Vehicle"));
			log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY, "Vehicle"), exception);
			throw exception;
			//return false;
		}
		/* Return true if date format is valid */
		return true;

	}


	@Override
	@Cacheable(value = "markeListCache", sync = true)
	public List<DropdownObject> getMarkeList(String schema, String companyId, String agencyId) {

		log.info("Inside getMarkeList method of VehicleServiceImpl");
		List<DropdownObject> markeDetailsList = new ArrayList<>();

		try {
			StringBuilder query = new StringBuilder("SELECT FMA_MARKE AS KEYFLD, FMA_BEZEI AS DATAFLD FROM ");
			query.append(schema).append(".PMH_FMARKE ORDER BY FMA_MARKE");

			Map<String, String> markeDetailsMap = dbServiceRepository.getResultUsingCobolQuery(query.toString());

			if(markeDetailsMap != null && !markeDetailsMap.isEmpty()) {
				for(Map.Entry<String, String> markeDetails : markeDetailsMap.entrySet()) {

					DropdownObject dropdownObject = new DropdownObject();
					dropdownObject.setKey(markeDetails.getKey());
					dropdownObject.setValue(markeDetails.getValue());

					markeDetailsList.add(dropdownObject);
				}
			}else{
				log.info("marke List is empty");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Marke (Brand)"));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Marke (Brand)"));
				throw exception;
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Marke (Brand)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Marke (Brand)"), exception);
			throw exception;
		}

		return markeDetailsList;
	}


	@Override
	public List<DropdownObject> getZulassungsartList(String dataLibrary, String companyId, String agencyId) {

		log.info("Inside getZulassungsartList method of VehicleServiceImpl");

		//validate the company Id 
		validateCompany(companyId);

		String compID =  StringUtils.stripStart(companyId, "0");
		List<DropdownObject> zulassungsartList = new ArrayList<>();

		try {
			StringBuilder query = new StringBuilder("SELECT SUBSTR(KEYFLD, 5, 1) AS KEYFLD, SUBSTR(DATAFLD, 1, 40) AS DATAFLD FROM ");
			query.append(dataLibrary).append(".M").append(compID).append("_KOND WHERE KEYFLD LIKE '3036%' ORDER BY KEYFLD");

			Map<String, String> zulassungsartMap = dbServiceRepository.getResultUsingCobolQuery(query.toString());

			if(zulassungsartMap != null && !zulassungsartMap.isEmpty()) {
				for(Map.Entry<String, String> zulassungsartDetails : zulassungsartMap.entrySet()) {

					DropdownObject dropdownObject = new DropdownObject();
					dropdownObject.setKey(zulassungsartDetails.getKey());
					dropdownObject.setValue(zulassungsartDetails.getValue());

					zulassungsartList.add(dropdownObject);
				}
			}else{
				log.info("zulassungsart List is empty");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Zulassungsart (Registration type)"));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Zulassungsart (Registration type)"));
				throw exception;
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Zulassungsart (Registration type)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Zulassungsart (Registration type)"), exception);
			throw exception;
		}

		return zulassungsartList;
	}


	@Override
	public List<DropdownObject> getAntriebsartList(String dataLibrary, String companyId, String agencyId) {

		log.info("Inside getAntriebsartList method of VehicleServiceImpl");

		//validate the company Id 
		validateCompany(companyId);

		String compID =  StringUtils.stripStart(companyId, "0");
		List<DropdownObject> antriebsartList = new ArrayList<>();

		try {
			StringBuilder query = new StringBuilder("SELECT SUBSTR(KEYFLD, 5, 1) AS KEYFLD, SUBSTR(DATAFLD, 1, 30) AS DATAFLD FROM ");
			query.append(dataLibrary).append(".M").append(compID).append("_KOND WHERE KEYFLD LIKE '3038%' AND SUBSTR(KEYFLD, 6, 1) LIKE '1%' ORDER BY KEYFLD");

			Map<String, String> antriebsartMap = dbServiceRepository.getResultUsingCobolQuery(query.toString());

			if(antriebsartMap != null && !antriebsartMap.isEmpty()) {
				for(Map.Entry<String, String> antriebsartDetails : antriebsartMap.entrySet()) {

					DropdownObject dropdownObject = new DropdownObject();
					dropdownObject.setKey(antriebsartDetails.getKey());
					dropdownObject.setValue(antriebsartDetails.getValue());

					antriebsartList.add(dropdownObject);
				}
			}else{
				log.info("AntriebsartList List is empty");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Antriebsart (Engine type)"));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Antriebsart (Engine type)"));
				throw exception;
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Antriebsart (Engine type)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Antriebsart (Engine type)"), exception);
			throw exception;
		}

		return antriebsartList;
	}


	@Override
	public List<DropdownObject> getLaufleistungList() {
		log.info("Inside getLaufleistungList method of VehicleServiceImpl");

		List<DropdownObject> herkunftList = new ArrayList<>();
		herkunftList = stubServiceRepository.getDDLValuesFromStub(StubServiceRepository.LaufleistungMap);
		return herkunftList;
	}


	@Override
	public List<DropdownObject> getVerkauferList(String dataLibrary, String companyId, String agencyId) {

		log.info("Inside getVerkauferList method of VehicleServiceImpl");

		//validate the company Id 
		validateCompany(companyId);

		String compID =  StringUtils.stripStart(companyId, "0");
		List<DropdownObject> verkauferList = new ArrayList<>();

		try {
			StringBuilder query = new StringBuilder("SELECT SUBSTR(KEYFLD, 5, 6) AS KEYFLD, SUBSTR(DATAFLD, 5, 30) AS DATAFLD FROM ");
			query.append(dataLibrary).append(".M").append(compID).append("_KOND WHERE KEYFLD LIKE '3021%' AND SUBSTR(DATAFLD, 44, 1)  <> 'L' ORDER BY SUBSTR(DATAFLD, 5, 30)");

			Map<String, String> verkauferMap = dbServiceRepository.getResultUsingCobolQuery(query.toString());

			if(verkauferMap != null && !verkauferMap.isEmpty()) {
				for(Map.Entry<String, String> verkauferDetails : verkauferMap.entrySet()) {

					DropdownObject dropdownObject = new DropdownObject();
					dropdownObject.setKey(verkauferDetails.getKey());
					dropdownObject.setValue(verkauferDetails.getValue());

					verkauferList.add(dropdownObject);
				}
			}else{
				log.info("Verkaufer (SalesMan) List is empty");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Verkäufer (salesMan)"));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Verkäufer (salesMan)"));
				throw exception;
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Verkäufer (salesMan)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Verkäufer (salesMan)"), exception);
			throw exception;
		}

		return verkauferList;
	}


	@Override
	public List<DropdownObject> getVerkaufssparteList(String dataLibrary, String companyId, String agencyId) {

		log.info("Inside getVerkaufssparteList method of VehicleServiceImpl");

		//validate the company Id 
		validateCompany(companyId);

		String compID =  StringUtils.stripStart(companyId, "0");
		List<DropdownObject> verkaufssparteList = new ArrayList<>();

		try {
			StringBuilder query = new StringBuilder("SELECT SUBSTR(KEYFLD, 5, 2) AS KEYFLD, SUBSTR(DATAFLD, 1, 2) AS DATAFLD FROM ");
			query.append(dataLibrary).append(".M").append(compID).append("_KOND WHERE KEYFLD LIKE '3006%' AND SUBSTR(KEYFLD, 7, 2) = '01' ORDER BY KEYFLD");

			Map<String, String> verkaufssparteMap = dbServiceRepository.getResultUsingCobolQuery(query.toString());

			if(verkaufssparteMap != null && !verkaufssparteMap.isEmpty()) {
				for(Map.Entry<String, String> verkaufssparteDetails : verkaufssparteMap.entrySet()) {

					DropdownObject dropdownObject = new DropdownObject();
					dropdownObject.setKey(verkaufssparteDetails.getKey());
					dropdownObject.setValue(verkaufssparteDetails.getValue());

					verkaufssparteList.add(dropdownObject);
				}
			}else{
				log.info("Verkaufssparte (sales division) List is empty");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Verkaufssparte (sales division)"));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Verkaufssparte (sales division)"));
				throw exception;
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Verkaufssparte (sales division)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Verkaufssparte (sales division)"), exception);
			throw exception;
		}

		return verkaufssparteList;
	}


	@Override
	public List<DropdownObject> getAkquisitionspereList(String dataLibrary, String companyId, String agencyId) {

		log.info("Inside getAkquisitionspereList method of VehicleServiceImpl");

		//validate the company Id 
		validateCompany(companyId);

		String compID =  StringUtils.stripStart(companyId, "0");
		List<DropdownObject> akquisitionList = new ArrayList<>();

		try {
			StringBuilder query = new StringBuilder("SELECT SUBSTR(KEYFLD, 5, 2) AS KEYFLD, SUBSTR(DATAFLD, 1, 30) AS DATAFLD FROM ");
			query.append(dataLibrary).append(".M").append(compID).append("_KOND WHERE KEYFLD LIKE '3040%'  ORDER BY KEYFLD");

			Map<String, String> akquisitionMap = dbServiceRepository.getResultUsingCobolQuery(query.toString());

			if(akquisitionMap != null && !akquisitionMap.isEmpty()) {
				for(Map.Entry<String, String> akquisitionDetails : akquisitionMap.entrySet()) {

					DropdownObject dropdownObject = new DropdownObject();
					dropdownObject.setKey(akquisitionDetails.getKey());
					dropdownObject.setValue(akquisitionDetails.getValue());

					akquisitionList.add(dropdownObject);
				}
			}else{
				log.info("Akquisitionssperre (Acquisition lock) List is empty");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Akquisitionssperre (Acquisition lock)"));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Akquisitionssperre (Acquisition lock)"));
				throw exception;
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Akquisitionssperre (Acquisition lock)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Akquisitionssperre (Acquisition lock)"), exception);
			throw exception;
		}

		return akquisitionList;
	}


	@Override
	public List<DropdownObject> getWartGruppeList() {

		log.info("Inside getWartGruppeList method of VehicleServiceImpl");

		List<DropdownObject> wartGruppeList = new ArrayList<>();
		wartGruppeList = stubServiceRepository.getDDLValuesFromStub(StubServiceRepository.wartGruppeMap);
		return wartGruppeList;
	}


	@Override
	public List<DropdownObject> getKundendBeraterList(String dataLibrary, String companyId, String agencyId) {

		log.info("Inside getKundendBeraterList method of VehicleServiceImpl");
		List<DropdownObject> beraterList = new ArrayList<>();

		try {
			StringBuilder query = new StringBuilder("SELECT SUBSTR(KEYFLD, 11, 2) AS KEYFLD, SUBSTR(DATAFLD, 1, 30) AS DATAFLD FROM ");
			query.append(dataLibrary).append(".REFERENZ WHERE KEYFLD LIKE '0000019905%' ORDER BY DATAFLD");

			Map<String, String> beraterMap = dbServiceRepository.getResultUsingCobolQuery(query.toString());

			if(beraterMap != null && !beraterMap.isEmpty()) {
				for(Map.Entry<String, String> beraterDetails : beraterMap.entrySet()) {

					DropdownObject dropdownObject = new DropdownObject();
					dropdownObject.setKey(beraterDetails.getKey());
					dropdownObject.setValue(beraterDetails.getValue());

					beraterList.add(dropdownObject);
				}
			}else{
				log.info("Kundendienst-Berater (Customer Service Advisor) List is empty");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Kundendienst-Berater (Customer Service Advisor)"));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Kundendienst-Berater (Customer Service Advisor)"));
				throw exception;
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Kundendienst-Berater (Customer Service Advisor)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Kundendienst-Berater (Customer Service Advisor)"), exception);
			throw exception;
		}

		return beraterList;
	}


	@Override
	public List<DropdownObject> getBetreuendeWerkList(String schema, String companyId, String agencyId) {

		log.info("Inside getBetreuendeWerkList method of VehicleServiceImpl");

		//validate the company Id 
		validateCompany(companyId);

		List<DropdownObject> betreuendeWerkList = new ArrayList<>();

		try {
			StringBuilder query = new StringBuilder("SELECT FIL_FIRMA, FIL_FILIA AS KEYFLD, FIL_BETWST AS DATAFLD FROM ");
			query.append(schema).append(".PFF_FILIAL WHERE FIL_FIRMA='").append(companyId).append("' ORDER BY FIL_FILIA");

			Map<String, String> betreuendeWerkMap = dbServiceRepository.getResultUsingCobolQuery(query.toString());

			if(betreuendeWerkMap != null && !betreuendeWerkMap.isEmpty()) {
				for(Map.Entry<String, String> betreuendeWerkDetails : betreuendeWerkMap.entrySet()) {

					DropdownObject dropdownObject = new DropdownObject();
					dropdownObject.setKey(betreuendeWerkDetails.getKey());
					dropdownObject.setValue(betreuendeWerkDetails.getValue());

					betreuendeWerkList.add(dropdownObject);
				}
			}else{
				log.info("Betreuende Werkstatt (Supporting workshop) List is empty");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Betreuende Werkstatt (Supporting workshop)"));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Betreuende Werkstatt (Supporting workshop)"));
				throw exception;
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Betreuende Werkstatt (Supporting workshop)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Betreuende Werkstatt (Supporting workshop)"), exception);
			throw exception;
		}

		return betreuendeWerkList;
	}


	@Override
	public VehicleDetailsVO getVehicleDetailsByID(String dataLibrary, String companyId, String agencyId,String id) {
		log.info("Inside getVehicleDetailsByID method of VehicleServiceImpl");

		//validate the company Id 
		validateCompany(companyId);

		String compID =  StringUtils.stripStart(companyId, "0");

		StringBuilder vehicle_query_1 = new StringBuilder(" SELECT FKENNZ7,KDNR1,WHC,BAUM,ENDN,FGP,MOTN,BUCH1,SPAL1,TYPNR,TYPVA,KW,HUB,TYP,TYPGR,FART,");
		vehicle_query_1.append(" DTLIP,DTEZU,ZUART,KZEV,PLATZ,ANTA,ANZVO,GKM,KZLF,VERK,AUFN,WAGR,WAVT_TBL,DTBES,");
		vehicle_query_1.append(" KDBE,WSTN,TEXT,DTTUV,KZTUV,ANZWE,DTLWE,KMLWE,DTLWA,KMLWA ,UNFAL, KZAQ  ");
		vehicle_query_1.append(" FROM ").append(dataLibrary).append(".M").append(compID).append("_KFZF1XX WHERE VZSL1=7 AND FKENNZ7 = ").append("'"+id+"'");

		StringBuilder vehicle_query_2 = new StringBuilder("SELECT HMARKE,HERCOD,TYPCOD,FGNR,FANR,ATMKM,FFUHRP,BAUJAHR,VKL,VKSPA,FAUFZU,FBRE_MJ, ");
		vehicle_query_2.append(" UVV_MJ,FTAO,FAKL_MJ,FABF_MJ,ASSYST,ARTLWD,WDABKM,WDAB,SERKDNR,SERKDNR2,SERVNR, ");
		vehicle_query_2.append(" SERDTB,SERDTE,SERKM,VSKOKDNR,VSKOVNR,VSKODTBEG,VSKODTEND,VSKOKM,WTRVKDNR,WTRVVNR, ");
		vehicle_query_2.append(" WTRVDTBEG,WTRVDTEND,WTRVKM,FINKDNR,FINVNR,FINDTBEG,FINDTEND,FINKM,FINART,FINGES, ");
		vehicle_query_2.append(" VORAAKTION,VORAVNR,VORADTBEG,VORADTEND,VORAKM,FZWI_TT,FZWI_MJ ");
		vehicle_query_2.append(" FROM ").append(dataLibrary).append(".M").append(compID).append("_KFZ221X WHERE VZSL=7 AND FKENNZ7 = ").append("'"+id+"'");;

		StringBuilder vehicle_query_3 = new StringBuilder(" SELECT  KDNR1 AS KEYFLD , NAME AS DATAFLD "); 
		vehicle_query_3.append(" FROM ").append(dataLibrary).append(".M").append(compID).append("_KDDATK1 WHERE FNR = '' AND KDNR2 = '' AND KDNR1 IN  ( ");

		StringBuilder vehicle_query_4 = new StringBuilder(" SELECT FTEXT_T, FTEXT_1, FTEXT_2, FTEXT_3, FTEXT_4, FTEXT_5, FTEXT_6, DRUCKBK ");
		vehicle_query_4.append(" FROM ").append(dataLibrary).append(".M").append(compID).append("_KFZ222X WHERE VZSL_1 = 7 AND FKENNZ7_1 = ").append("'"+id+"'");;

		VehicleDetailsVO vehicleDetailVO = new VehicleDetailsVO();

		List<VehicleKFZF1XX> vehicleDtlsKFZF1XXList = new ArrayList<>();
		List<VehicleKFZ221X> vehicleDtlsKFZ221XList = new ArrayList<>();
		List<VehicleBriefkastenKFZ222X> vehicleDtlsKFZ222XList = new ArrayList<>();

		try {
			List<Callable<List>> callables = Arrays.asList(
					() -> dbServiceRepository.getResultUsingQuery(VehicleKFZF1XX.class, vehicle_query_1.toString(),true),
					() -> dbServiceRepository.getResultUsingQuery(VehicleKFZ221X.class, vehicle_query_2.toString(),true),
					() -> dbServiceRepository.getResultUsingQuery(VehicleBriefkastenKFZ222X.class, vehicle_query_4.toString(),false));

			ExecutorService service = Executors.newCachedThreadPool();
			List<Future<List>> resultList = service.invokeAll(callables);
			service.shutdown();

			for (int i = 0; i < resultList.size(); i++) {
				Future<List> future = resultList.get(i);

				for (Object obj : future.get()) {
					if (obj instanceof VehicleKFZF1XX) {
						vehicleDtlsKFZF1XXList = future.get();
					}
					if (obj instanceof VehicleKFZ221X) {
						vehicleDtlsKFZ221XList = future.get();
					}
					if (obj instanceof VehicleBriefkastenKFZ222X) {
						vehicleDtlsKFZ222XList = future.get();
					}
				}
			}

			if (vehicleDtlsKFZF1XXList != null && !vehicleDtlsKFZF1XXList.isEmpty() && vehicleDtlsKFZF1XXList.size() == 1) {


				for (VehicleKFZF1XX vehicle_obj : vehicleDtlsKFZF1XXList) {
					setVehicleDetailsKFZF1XX(vehicle_obj , vehicleDetailVO);
				}
			} else {
				if(vehicleDtlsKFZF1XXList.size() > 1){
					log.info("More then one records found for this Id in KFZF1XX table :" + id);
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.MULTIPLE_RECORD_MSG_KEY));
					log.error(messageService.getReadableMessage(ExceptionMessages.MULTIPLE_RECORD_MSG_KEY));
					throw exception;
				}
				log.info("Vehicle details not found in KFZF1XX table for this Id :" + id);
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "Vehicle"));
				log.error(messageService.getReadableMessage(ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "Vehicle"));
				throw exception;
			}

			if (vehicleDtlsKFZ221XList != null && !vehicleDtlsKFZ221XList.isEmpty() && vehicleDtlsKFZ221XList.size() == 1) {

				StringBuilder customerIds = new StringBuilder("");
				Map<String, String> userNames = null;

				for (VehicleKFZ221X vehicle_obj : vehicleDtlsKFZ221XList) {

					if(vehicle_obj.getKundenNumberVega()!=null && !vehicle_obj.getKundenNumberVega().trim().isEmpty()){
						customerIds.append("'"+vehicle_obj.getKundenNumberVega()+"',");
					}
					if(vehicle_obj.getKundenNumberBelegdruck()!=null && !vehicle_obj.getKundenNumberBelegdruck().trim().isEmpty()){
						customerIds.append("'"+vehicle_obj.getKundenNumberBelegdruck()+"',");
					}
					if(vehicle_obj.getVersichererko_KundenNumber()!=null && !vehicle_obj.getVersichererko_KundenNumber().trim().isEmpty()){
						customerIds.append("'"+vehicle_obj.getVersichererko_KundenNumber()+"',");
					}
					if(vehicle_obj.getWeitererVertrag_KundenNumber()!=null && !vehicle_obj.getWeitererVertrag_KundenNumber().trim().isEmpty()){
						customerIds.append("'"+vehicle_obj.getWeitererVertrag_KundenNumber()+"',");
					}
					if(vehicle_obj.getFinanzierunq_KundenNumber()!=null && !vehicle_obj.getFinanzierunq_KundenNumber().trim().isEmpty()){
						customerIds.append("'"+vehicle_obj.getFinanzierunq_KundenNumber()+"',");
					}
					if(!customerIds.toString().isEmpty()){
						customerIds = customerIds.deleteCharAt(customerIds.toString().length()-1); 
						vehicle_query_3.append(customerIds).append(")");
						userNames = dbServiceRepository.getResultUsingCobolQuery(vehicle_query_3.toString());
					}

					setCustomerKFZ221X(vehicle_obj , vehicleDetailVO ,userNames);
				}
			} else {
				if(vehicleDtlsKFZ221XList.size() > 1){
					log.info("More then one records found for this Id in KFZ221X table :" + id);
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.MULTIPLE_RECORD_MSG_KEY));
					log.error(messageService.getReadableMessage(ExceptionMessages.MULTIPLE_RECORD_MSG_KEY), exception);
					throw exception;
				}
			}

			if (vehicleDtlsKFZ222XList != null && !vehicleDtlsKFZ222XList.isEmpty() && vehicleDtlsKFZ222XList.size() == 1) {

				for (VehicleBriefkastenKFZ222X vehicle_obj : vehicleDtlsKFZ222XList) {

					setCustomerKFZ222X(vehicle_obj, vehicleDetailVO);
				}
			} else {
				if (vehicleDtlsKFZ222XList.size() > 1) {
					log.info("More then one records found for this Id in KFZ222X table :" + id);
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.MULTIPLE_RECORD_MSG_KEY));
					log.error(messageService.getReadableMessage(ExceptionMessages.MULTIPLE_RECORD_MSG_KEY));
					throw exception;
				}
			}

		}catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Vehicle"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Vehicle"), exception);
			throw exception;
		}
		return vehicleDetailVO;

	}

	private void setVehicleDetailsKFZF1XX(VehicleKFZF1XX vehicle_obj, VehicleDetailsVO vehicleDetailVO) {
		log.info("Start setVehicleDetailsKFZF1XX");

		vehicleDetailVO.setAmtlichesKennzeichen( vehicle_obj.getAmtlichesKennzeichen() );
		vehicleDetailVO.setKundenNummer( vehicle_obj.getKundenNummer() );

		vehicleDetailVO.setFzIdentnr1( vehicle_obj.getFzIdentnr1() );
		String fzIdentnr2 = "";
		String fzIdentnr3 = "";
		if(vehicle_obj.getFzIdentnr2().trim().length() >= 6){
			fzIdentnr2 = vehicle_obj.getFzIdentnr2().trim().substring(0,6);
		}
		if(vehicle_obj.getFzIdentnr2().trim().length() >= 8){
			fzIdentnr3 = vehicle_obj.getFzIdentnr2().trim().substring(6,8);
		}

		vehicleDetailVO.setFzIdentnr2(fzIdentnr2);
		vehicleDetailVO.setFzIdentnr3(fzIdentnr3);
		vehicleDetailVO.setFzIdentnr4(vehicle_obj.getFzIdentnr4());
		vehicleDetailVO.setFzIdentnr5(vehicle_obj.getFzIdentnr5());
		vehicleDetailVO.setMotorNummer(vehicle_obj.getMotorNummer());
		vehicleDetailVO.setFamilienGruppe(vehicle_obj.getFamilienGruppe());
		vehicleDetailVO.setTypenkennzahl(vehicle_obj.getTypenkennzahl());
		vehicleDetailVO.setTypNummer(vehicle_obj.getTypNummer());
		vehicleDetailVO.setTypVariante(vehicle_obj.getTypVariante());
		vehicleDetailVO.setMotorstarkeKW(String.valueOf(vehicle_obj.getMotorstarkeKW()));
		vehicleDetailVO.setHubraumZul_ges_gew(String.valueOf(vehicle_obj.getHubraumZul_ges_gew()));
		vehicleDetailVO.setFahrzeugTyp(vehicle_obj.getFahrzeugTyp());
		vehicleDetailVO.setFahrzeugTypgruppe(vehicle_obj.getFahrzeugTypgruppe());
		vehicleDetailVO.setFahrzeugArt(vehicle_obj.getFahrzeugArt());

		/*For LetzteZulassung - If the Year is greater than 50 then 19 century will be shown 
		 * and if the year is less than 50 then 20 century will be shown.*/
		vehicleDetailVO.setLetzteZulassung(getCobolDateFormat(vehicle_obj.getLetzteZulassung()));

		vehicleDetailVO.setErsteZulassung(getDDMMYYFormat(vehicle_obj.getErsteZulassung()));
		vehicleDetailVO.setZulassungsart(vehicle_obj.getZulassungsart());
		vehicleDetailVO.setEigenverkauf(vehicle_obj.getEigenverkauf().equalsIgnoreCase("Y")?true:false);
		vehicleDetailVO.setNutzlastAnz_platze(String.valueOf(vehicle_obj.getNutzlastAnz_platze()));
		vehicleDetailVO.setAntriebsart(vehicle_obj.getAntriebsart());
		vehicleDetailVO.setAnzahlVorbesitzer(String.valueOf(vehicle_obj.getAnzahlVorbesitzer()));
		vehicleDetailVO.setLaufleistungKennzeichen(vehicle_obj.getLaufleistung());
		vehicleDetailVO.setLaufleistung(String.valueOf(vehicle_obj.getLaufleistungKennzeichen()));
		vehicleDetailVO.setVerkaufer(vehicle_obj.getVerkaufer());
		vehicleDetailVO.setAufragsNummer(vehicle_obj.getAufragsNummer());
		vehicleDetailVO.setWartungsGruppe(vehicle_obj.getWartungsGruppe());

		String wartungsVertrag = String.valueOf(vehicle_obj.getWartungsVertrag()).trim();

		vehicleDetailVO
		.setWartungsVertrag1(wartungsVertrag.length() > 0 ? String.valueOf(wartungsVertrag.charAt(0)) : "");
		vehicleDetailVO
		.setWartungsVertrag2(wartungsVertrag.length() > 1 ? String.valueOf(wartungsVertrag.charAt(1)) : "");
		vehicleDetailVO
		.setWartungsVertrag3(wartungsVertrag.length() > 2 ? String.valueOf(wartungsVertrag.charAt(2)) : "");
		vehicleDetailVO
		.setWartungsVertrag4(wartungsVertrag.length() > 3 ? String.valueOf(wartungsVertrag.charAt(3)) : "");
		vehicleDetailVO
		.setWartungsVertrag5(wartungsVertrag.length() > 4 ? String.valueOf(wartungsVertrag.charAt(4)) : "");
		vehicleDetailVO
		.setWartungsVertrag6(wartungsVertrag.length() > 5 ? String.valueOf(wartungsVertrag.charAt(5)) : "");
		//Date format DDMMYY
		vehicleDetailVO.setBestellDatum(getDDMMYYFormat(vehicle_obj.getBestellDatum()));
		//Date format DDMMYY
		//vehicleDetailVO.setLieferUbernahmedatum(getDDMMYYFormat(vehicle_obj.getLieferUbernahmedatum()));

		vehicleDetailVO.setKundendienstBerater( vehicle_obj.getKundendienstBerater() );
		vehicleDetailVO.setUnfall( vehicle_obj.getUnfall().equalsIgnoreCase("Y")?true:false );
		vehicleDetailVO.setBetreuendeWerkstatt( vehicle_obj.getBetreuendeWerkstatt() );
		vehicleDetailVO.setInfoText(vehicle_obj.getInfoText() );
		//Date format MMYY
		vehicleDetailVO.setHauptUntersuchung1(getMMYYFormat(vehicle_obj.getHauptUntersuchung1()));

		vehicleDetailVO.setHuKennzeichen( vehicle_obj.getHuKennzeichen() );
		vehicleDetailVO.setAnzahlWerkstattbesuche(String.valueOf(vehicle_obj.getAnzahlWerkstattbesuche()));
		//Date format DDMMYY
		vehicleDetailVO.setLetzterWerkstattbesuch(getDDMMYYFormat(vehicle_obj.getLetzterWerkstattbesuch()));
		vehicleDetailVO.setLaufleistun(String.valueOf(vehicle_obj.getLaufleistun()));
		//Date format DDMMYY
		vehicleDetailVO.setLetzteWartung(getDDMMYYFormat(vehicle_obj.getLetzteWartung()));
		vehicleDetailVO.setLaufleistung4thScreen(String.valueOf(vehicle_obj.getLaufleistung4thScreen()));
		vehicleDetailVO.setAkquisitionsperre(vehicle_obj.getAkquisitionsperre());

	}


	private void setCustomerKFZ221X(VehicleKFZ221X vehicle_obj, VehicleDetailsVO vehicleDetailVO, Map<String, String> userNames) {
		log.info("Start setCustomerKFZ221X");

		vehicleDetailVO.setMarke(vehicle_obj.getMarke());
		vehicleDetailVO.setHerstellerCode(vehicle_obj.getHerstellerCode());
		vehicleDetailVO.setTypCode(vehicle_obj.getTypCode());
		vehicleDetailVO.setGetriebeNummer(vehicle_obj.getGetriebeNummer());
		vehicleDetailVO.setAchsNummer(vehicle_obj.getAchsNummer());
		vehicleDetailVO.setLaufleistAustauschmotor(String.valueOf(vehicle_obj.getLaufleistAustauschmotor()));
		vehicleDetailVO.setFuhrparkNr(vehicle_obj.getFuhrparkNr());

		String day = "";
		String month = "";
		String year = "";
		vehicleDetailVO.setLieferUbernahmedatum("");

		if (vehicle_obj.getLieferUbernahmedatum_dd() != null && vehicle_obj.getLieferUbernahmedatum_mmyy() != null) {
			
			String dateFormat_dd = StringUtils.leftPad(String.valueOf(vehicle_obj.getLieferUbernahmedatum_dd()), 2,"0");
			String dateFormat_mmyy = String.valueOf(vehicle_obj.getLieferUbernahmedatum_mmyy());
			dateFormat_mmyy = dateFormat_mmyy.length() == 3 ? "0" + dateFormat_mmyy : dateFormat_mmyy;
			
			if (dateFormat_mmyy.length() == 4 && dateFormat_dd.length() == 2 && !dateFormat_dd.equalsIgnoreCase("00")) {
				day = dateFormat_dd;
				month = dateFormat_mmyy.substring(0, 2);
				year = dateFormat_mmyy.substring(2, 4);
				vehicleDetailVO.setLieferUbernahmedatum(day + "/" + month + "/20" + year);
			}
		}

		vehicleDetailVO.setBaujahr(getDDMMYYYYFormat(vehicle_obj.getBaujahr()));
		vehicleDetailVO.setVergleichsklasse(String.valueOf(vehicle_obj.getVergleichsklasse()));
		vehicleDetailVO.setVerkaufssparte(vehicle_obj.getVerkaufssparte());
		vehicleDetailVO.setAuftragsZusatzText(vehicle_obj.getAuftragsZusatzText());
		//Date format MMYY
		vehicleDetailVO.setSicherheitsprufung1(getMMYYFormat(vehicle_obj.getSicherheitsprufung1()));
		//Date format MMYY
		vehicleDetailVO.setUnfallverhutungsvorschrift1(getMMYYYYFormat(vehicle_obj.getUnfallverhutungsvorschrift1()));
		//Date format DDMMYY
		vehicleDetailVO.setTachoUntersuchung(getDDMMYYFormat(vehicle_obj.getTachoUntersuchung()));
		//Date format MMYY
		vehicleDetailVO.setKuhlmittelwechsel1(getMMYYFormat(vehicle_obj.getKuhlmittelwechsel1()));
		//Date format MMYY
		vehicleDetailVO.setBremsflussigkeitswechsel1(getMMYYFormat(vehicle_obj.getBremsflussigkeitswechsel1()));

		vehicleDetailVO.setAssyst(vehicle_obj.getAssyst().equalsIgnoreCase("J")?true:false);
		vehicleDetailVO.setArt_l_su(vehicle_obj.getArt_l_su());
		//Date format DDMMYYYY
		vehicleDetailVO.setSu_Ba_km(getDDMMYYYYFormat(vehicle_obj.getSu_Ba_km()));
		//Date format DDMMYY
		vehicleDetailVO.setSu_b_ab(getDDMMYYFormat(vehicle_obj.getSu_b_ab()));

		vehicleDetailVO.setKundenNumberVega(vehicle_obj.getKundenNumberVega());
		vehicleDetailVO.setKundenNumberBelegdruck(vehicle_obj.getKundenNumberBelegdruck());
		vehicleDetailVO.setServicevertragVertragsNumber(vehicle_obj.getServicevertragVertragsNumber());
		//Date format DDMMYYYY
		vehicleDetailVO.setLaufzeit1(getDDMMYYYYFormat(vehicle_obj.getLaufzeit1()));
		//Date format DDMMYYYY
		vehicleDetailVO.setLaufzeit2(getDDMMYYYYFormat(vehicle_obj.getLaufzeit2()));

		vehicleDetailVO.setServicevertrag_kmBegrenzung(String.valueOf(vehicle_obj.getServicevertrag_kmBegrenzung()));
		vehicleDetailVO.setVersichererko_KundenNumber(vehicle_obj.getVersichererko_KundenNumber());
		vehicleDetailVO.setVersichererko_VertragsNumber(vehicle_obj.getVersichererko_VertragsNumber());
		//Date format DDMMYYYY
		vehicleDetailVO.setVersichererko_laufzeit1(getDDMMYYYYFormat(vehicle_obj.getVersichererko_laufzeit1()));
		//Date format DDMMYYYY
		vehicleDetailVO.setVersichererko_laufzeit2(getDDMMYYYYFormat(vehicle_obj.getVersichererko_laufzeit2()));

		vehicleDetailVO.setVersichererko_kmBegrenzung(String.valueOf(vehicle_obj.getVersichererko_kmBegrenzung()));
		vehicleDetailVO.setWeitererVertrag_KundenNumber(vehicle_obj.getWeitererVertrag_KundenNumber());
		vehicleDetailVO.setWeitererVertrag_VertragsNumber(vehicle_obj.getWeitererVertrag_VertragsNumber());
		//Date format DDMMYYYY
		vehicleDetailVO.setWeitererVertrag_laufzeit1(getDDMMYYYYFormat(vehicle_obj.getWeitererVertrag_laufzeit1()));
		//Date format DDMMYYYY
		vehicleDetailVO.setWeitererVertrag_laufzeit2(getDDMMYYYYFormat(vehicle_obj.getWeitererVertrag_laufzeit2()));

		vehicleDetailVO.setWeitererVertrag_kmBegrenzung(String.valueOf(vehicle_obj.getWeitererVertrag_kmBegrenzung()));
		vehicleDetailVO.setFinanzierunq_KundenNumber(vehicle_obj.getFinanzierunq_KundenNumber());
		vehicleDetailVO.setFinanzierunq_VertragsNumber(vehicle_obj.getFinanzierunq_VertragsNumber());
		//Date format DDMMYYYY
		vehicleDetailVO.setFinanzierunq_laufzeit1(getDDMMYYYYFormat(vehicle_obj.getFinanzierunq_laufzeit1()));
		//Date format DDMMYYYY
		vehicleDetailVO.setFinanzierunq_laufzeit2(getDDMMYYYYFormat(vehicle_obj.getFinanzierunq_laufzeit2()));

		vehicleDetailVO.setFinanzierunq_kmBegrenzung(String.valueOf(vehicle_obj.getFinanzierunq_kmBegrenzung()));
		vehicleDetailVO.setFinanzierungsart1(vehicle_obj.getFinanzierungsart1());
		vehicleDetailVO.setFinanzGesellschaft1(vehicle_obj.getFinanzGesellschaft1());
		vehicleDetailVO.setAktion1(vehicle_obj.getAktion1());
		vehicleDetailVO.setKartenNumber(vehicle_obj.getKartenNumber());
		//Date format DDMMYYYY
		vehicleDetailVO.setVorteilsaktion_laufzeit1(getDDMMYYYYFormat(vehicle_obj.getVorteilsaktion_laufzeit1()));
		//Date format DDMMYYYY
		vehicleDetailVO.setVorteilsaktion_laufzeit2(getDDMMYYYYFormat(vehicle_obj.getVorteilsaktion_laufzeit2()));
		vehicleDetailVO.setVorteilsaktion_kmBegrenzung(String.valueOf(vehicle_obj.getVorteilsaktion_kmBegrenzung()));


		if (userNames != null) {
			String vegaName = userNames.get(vehicle_obj.getKundenNumberVega()) != null ? userNames.get(vehicle_obj.getKundenNumberVega()).trim() : "";
			String belegdruckName =userNames.get(vehicle_obj.getKundenNumberBelegdruck()) != null ? userNames.get(vehicle_obj.getKundenNumberBelegdruck()).trim() : "";
			String versichererkoName = userNames.get(vehicle_obj.getVersichererko_KundenNumber()) != null ? userNames.get(vehicle_obj.getVersichererko_KundenNumber()).trim() : "";
			String weitererVertragName = userNames.get(vehicle_obj.getWeitererVertrag_KundenNumber()) != null ? userNames.get(vehicle_obj.getWeitererVertrag_KundenNumber()).trim() : "";
			String finanzierunqName = userNames.get(vehicle_obj.getFinanzierunq_KundenNumber()) != null ? userNames.get(vehicle_obj.getFinanzierunq_KundenNumber()).trim() : "";

			vehicleDetailVO.setVegaName(vegaName);
			if(vegaName.length() > 25){
				vehicleDetailVO.setVegaName(vegaName.substring(0,25));
			}
			vehicleDetailVO.setBelegdruck_Name(belegdruckName);
			if(belegdruckName.length() > 25){
				vehicleDetailVO.setVegaName(belegdruckName.substring(0,25));
			}
			vehicleDetailVO.setVersichererko_Name(versichererkoName);
			if(versichererkoName.length() > 25){
				vehicleDetailVO.setVegaName(versichererkoName.substring(0,25));
			}
			vehicleDetailVO.setWeitererVertrag_Name(weitererVertragName);
			if(weitererVertragName.length() > 25){
				vehicleDetailVO.setVegaName(weitererVertragName.substring(0,25));
			}
			vehicleDetailVO.setFinanzierunq_Name(finanzierunqName);
			if(finanzierunqName.length() > 25){
				vehicleDetailVO.setVegaName(finanzierunqName.substring(0,25));
			}
		}

	}


	private void setCustomerKFZ222X(VehicleBriefkastenKFZ222X vehicle_obj, VehicleDetailsVO vehicleDetailVO) {

		log.info("Start setCustomerKFZ222X");
		String druckkennzeichen = vehicle_obj.getDruckkennzeichenChb();
		vehicleDetailVO.setBriefkastentext(vehicle_obj.getBriefkastentext().trim());
		vehicleDetailVO.setBriefkastentext1(vehicle_obj.getBriefkastentext1().trim());
		vehicleDetailVO.setBriefkastentext2(vehicle_obj.getBriefkastentext2().trim());
		vehicleDetailVO.setBriefkastentext3(vehicle_obj.getBriefkastentext3().trim());
		vehicleDetailVO.setBriefkastentext4(vehicle_obj.getBriefkastentext4().trim());
		vehicleDetailVO.setBriefkastentext5(vehicle_obj.getBriefkastentext5().trim());
		vehicleDetailVO.setBriefkastentext6(vehicle_obj.getBriefkastentext6().trim());

		vehicleDetailVO.setDruckkennzeichenChb(druckkennzeichen.length() >  0 && druckkennzeichen.charAt(0)=='D'? true:false);
		vehicleDetailVO.setDruckkennzeichenChb1(druckkennzeichen.length() > 1 && druckkennzeichen.charAt(1)=='D'? true:false);
		vehicleDetailVO.setDruckkennzeichenChb2(druckkennzeichen.length() > 2 && druckkennzeichen.charAt(2)=='D'? true:false);
		vehicleDetailVO.setDruckkennzeichenChb3(druckkennzeichen.length() > 3 && druckkennzeichen.charAt(3)=='D'? true:false);
		vehicleDetailVO.setDruckkennzeichenChb4(druckkennzeichen.length() > 4 && druckkennzeichen.charAt(4)=='D'? true:false);
		vehicleDetailVO.setDruckkennzeichenChb5(druckkennzeichen.length() > 5 && druckkennzeichen.charAt(5)=='D'? true:false);
		vehicleDetailVO.setDruckkennzeichenChb6(druckkennzeichen.length() > 6 && druckkennzeichen.charAt(6)=='D'? true:false);
	}

	private String getCobolDateFormat(BigDecimal letzteZulassung) {

		String day = "";
		String month = "";
		String year = "";
		String formatedDate = "";

		if (letzteZulassung != null) {
			String dateFormat = String.valueOf(letzteZulassung);

			dateFormat = dateFormat.length() == 5 ? "0" + dateFormat : dateFormat;

			if (dateFormat.length() == 6) {
				day = dateFormat.substring(0, 2);
				month = dateFormat.substring(2, 4);
				year = dateFormat.substring(4, 6);

				if (Integer.parseInt(year) > 50) {
					formatedDate = day + "/" + month + "/19" + year;
				} else {
					formatedDate = day + "/" + month + "/20" + year;
				}
			}
		}

		return formatedDate;
	}


	private String getDDMMYYFormat(BigDecimal dateValue) {

		String day = "";
		String month = "";
		String year = "";
		String formatedDate = "";

		if (dateValue != null) {
			String dateFormat = String.valueOf(dateValue);
			if (dateFormat.length() == 7) {

				day = dateFormat.substring(1, 3);
				month = dateFormat.substring(3, 5);
				year = dateFormat.substring(5, 7);
				formatedDate = day + "/" + month + "/20" + year;
			} else {
				dateFormat = dateFormat.length() == 5 ? "0" + dateFormat : dateFormat;
				if (dateFormat.length() == 6) {
					day = dateFormat.substring(0, 2);
					month = dateFormat.substring(2, 4);
					year = dateFormat.substring(4, 6);
					formatedDate = day + "/" + month + "/20" + year;
				}
			}
		}
		return formatedDate;
	}

	private String getMMYYFormat(BigDecimal dateValue) {

		String day = "";
		String month = "";
		String year = "";
		String formatedDate = "";

		if (dateValue != null) {
			String dateFormat = String.valueOf(dateValue);
			dateFormat = dateFormat.length() == 3 ? "0" + dateFormat : dateFormat;
			if (dateFormat.length() == 4) {
				day = "01";
				month = dateFormat.substring(0, 2);
				year = dateFormat.substring(2, 4);
				formatedDate = day + "/" + month + "/20" + year;
			}
		}
		return formatedDate;
	}

	private String getDDMMYYYYFormat(BigDecimal dateValue) {

		String day = "";
		String month = "";
		String year = "";
		String formatedDate = "";

		if (dateValue != null) {
			String dateFormat = String.valueOf(dateValue);
			dateFormat = dateFormat.length() == 7 ? "0" + dateFormat : dateFormat;
			if (dateFormat.length() == 8) {
				day = dateFormat.substring(0, 2);
				month = dateFormat.substring(2, 4);
				year = dateFormat.substring(4, 8);
				formatedDate = day + "/" + month + "/" + year;
			}
		}
		return formatedDate;
	}

	private String getMMYYYYFormat(BigDecimal dateValue) {

		String day = "";
		String month = "";
		String year = "";
		String formatedDate = "";

		if (dateValue != null) {
			String dateFormat = String.valueOf(dateValue);
			dateFormat = dateFormat.length() == 5 ? "0" + dateFormat : dateFormat;
			if (dateFormat.length() == 6) {
				day = "01";
				month = dateFormat.substring(0, 2);
				year = dateFormat.substring(2, 6);
				formatedDate = day + "/" + month + "/" + year;
			}
		}
		return formatedDate;
	}

	/**
	 * This is method is used to get KundenNr_VEGA List data from DB.
	 */
	@Override
	public List<CustomerVegaDTO> getKundenNr_VEGAList(String dataLibrary, String schema, String companyId, String agencyId) {

		log.info("Inside getKundenNr_VEGAList method of VehicleServiceImpl");

		//List to add all result in one collection
		List<CustomerVegaDTO> searchCustomerVegaList = new ArrayList<>();

		try {

			//validate the company Id 
			validateCompany(companyId);

			String compID =  StringUtils.stripStart(companyId, "0");
			String agencyID = StringUtils.stripStart(agencyId, "0");
			log.info("Company ID  xx:  {}  and  Agency ID yy:  {}", compID, agencyID);

			StringBuilder query1 = new StringBuilder("SELECT mk.kdnr1 AS KDNR1, mk.name AS NAME FROM ").append(dataLibrary);
			query1.append(".M").append(compID).append("_KDDATK1 mk, ").append(schema).append(".PSC_SERVCW ps ");
			query1.append(" WHERE mk.kdnr1=ps.SVC_KDNRCW AND ps.SVC_FIRMA='").append(companyId).append("'");

			log.info("Search Query1 :" + query1.toString());

			List<SearchCustomer_VEGA1> searchKundenVEGAList1 = dbServiceRepository.getResultUsingQuery(SearchCustomer_VEGA1.class, query1.toString(),true);

			StringBuilder query2 = new StringBuilder("SELECT SER_KDNR AS KDNR1, SER_BEZEI AS NAME, VARCHAR_FORMAT(SER_DTGULV,'YYYY-MM-DD') AS SER_DTGULV, VARCHAR_FORMAT(SER_DTGULB,'YYYY-MM-DD') AS SER_DTGULB FROM ");
			query2.append(schema).append(".PSC_SERVER WHERE SER_FIRMA='").append(companyId).append("'");

			log.info("Search Query2 :" + query2.toString());

			List<SearchCustomer_VEGA2> searchKundenVEGAList2 = dbServiceRepository.getResultUsingQuery(SearchCustomer_VEGA2.class, query2.toString(),true);

			if (searchKundenVEGAList1 != null && !searchKundenVEGAList1.isEmpty()) {
				for (SearchCustomer_VEGA1 vega1 : searchKundenVEGAList1) {

					CustomerVegaDTO vegaDTO = new CustomerVegaDTO();
					vegaDTO.setKundenNummer(vega1.getKundenNummer());
					vegaDTO.setName(vega1.getName());
					vegaDTO.setCw_sv("*");
					vegaDTO.setValidityFrom("");
					vegaDTO.setValidityTo("");
					searchCustomerVegaList.add(vegaDTO);
				}
			}

			if (searchKundenVEGAList2 != null && !searchKundenVEGAList2.isEmpty()) {
				for (SearchCustomer_VEGA2 vega2 : searchKundenVEGAList2) {

					CustomerVegaDTO vegaDTO = new CustomerVegaDTO();
					vegaDTO.setKundenNummer(vega2.getKundenNummer());
					vegaDTO.setName(vega2.getName());
					vegaDTO.setCw_sv("");
					vegaDTO.setValidityFrom("");
					if(null != vega2.getValidity_From()) {
						vegaDTO.setValidityFrom(LocalDate.parse(vega2.getValidity_From()).format(FOMATTER));
					}
					vegaDTO.setValidityTo("");
					if(null != vega2.getValidity_To()) {
						vegaDTO.setValidityTo(LocalDate.parse(vega2.getValidity_To()).format(FOMATTER));
					}
					searchCustomerVegaList.add(vegaDTO);

				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.SEARCH_FAILED_MSG_KEY, "Customer VEGA"));
			log.error(messageService.getReadableMessage(ExceptionMessages.SEARCH_FAILED_MSG_KEY, "Customer VEGA"), exception);
			throw exception;
		}

		return searchCustomerVegaList;
	}


	/**
	 * This is method is used to get KundenNr List data from DB.
	 */
	@Override
	public GlobalSearch getKundenNrList(String dataLibrary, String companyId,String agencyId,
			String searchString, String pageSize, String pageNo) {
		log.info("Inside getKundenNrList method of VehicleServiceImpl");

		GlobalSearch globalSearchList = new GlobalSearch();
		List<SearchCustomerDTO> searchCustomersDTOList = new ArrayList<>();
		StringBuilder query;

		//validate the company Id 
		validateCompany(companyId);

		try {
			String compID =  StringUtils.stripStart(companyId, "0");
			String agencyID = StringUtils.stripStart(agencyId, "0");
			log.info("Company ID  xx:  {}  and  Agency ID yy:  {}", compID, agencyID);

			if(pageSize==null || pageNo==null || pageSize.isEmpty() || pageNo.isEmpty()){
				pageSize = RestInputConstants.DEFAULT_PAGE_SIZE;
				pageNo = RestInputConstants.DEFAULT_PAGE_NO;
			}

			int totalRecords = Integer.parseInt(pageSize);
			int totalPages = Integer.parseInt(pageNo);
			int nextRows = totalRecords * (totalPages - 1);

			//validate the page size
			validatePageSize(totalRecords);

			log.info("PageSize :" + pageSize + " OFFSET Size (Skip Rows) : " + nextRows + " Page Number  :"+pageNo);

			if(null != searchString && searchString.trim().length() > 0) {

				query = new StringBuilder("SELECT KDNR1, NAME, PLZ, ORT, STR, KZST, ( SELECT COUNT(*) FROM  ");
				query.append(dataLibrary).append(".M").append(compID).append("_KDDATK1 WHERE UPPER(NAME) LIKE UPPER('%").append(searchString).append("%') AND  KDNR2 = '' AND FNR = '' ) AS ROWNUMER FROM  ");
				query.append(dataLibrary).append(".M").append(compID).append("_KDDATK1 WHERE UPPER(NAME) LIKE UPPER('%").append(searchString).append("%')");
				query.append(" AND  KDNR2 = '' AND FNR = '' ORDER BY UPPER(NAME) ASC OFFSET ");
				query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");
				log.info("Search Query with search String :" + query.toString());

			}
			else {
				query = new StringBuilder("SELECT KDNR1, NAME, PLZ, ORT, STR, KZST, 999 AS ROWNUMER FROM ");
				query.append(dataLibrary).append(".M").append(compID).append("_KDDATK1 WHERE KDNR2 = '' AND FNR = '' ORDER BY UPPER(NAME) ASC OFFSET ");
				query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");
				log.info("Search Query without search String:" + query.toString());
			}

			List<SearchCustomer_CB> searchCustomersList = dbServiceRepository.getResultUsingQuery(SearchCustomer_CB.class, query.toString(),true);

			if (searchCustomersList != null && !searchCustomersList.isEmpty()) {

				for(SearchCustomer_CB customer_CB: searchCustomersList) {

					SearchCustomerDTO customerDTO = new SearchCustomerDTO();
					customerDTO.setKundenNummer(customer_CB.getKundenNummer());
					customerDTO.setName(customer_CB.getName());
					customerDTO.setOrt(customer_CB.getOrt());
					customerDTO.setPlz(customer_CB.getPlz());
					customerDTO.setStrasse(customer_CB.getStrasse());
					customerDTO.setKzst(customer_CB.getKzst());

					searchCustomersDTOList.add(customerDTO);
				}

				globalSearchList.setSearchDetailsList(searchCustomersDTOList);
				globalSearchList.setTotalPages(Integer.toString(searchCustomersList.get(0).getTotalCount()));
				globalSearchList.setTotalRecordCnt(Integer.toString(searchCustomersList.get(0).getTotalCount()));
			} else {
				globalSearchList.setSearchDetailsList(searchCustomersDTOList);
				globalSearchList.setTotalPages(Integer.toString(0));
				globalSearchList.setTotalRecordCnt(Integer.toString(0));
			}
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.SEARCH_FAILED_MSG_KEY, "Customer"));
			log.error(messageService.getReadableMessage(ExceptionMessages.SEARCH_FAILED_MSG_KEY, "Customer"), exception);
			throw exception;
		}

		return globalSearchList;
	}


	@Override
	public List<DropdownObject> getAktionList(String schema) {

		log.info("Inside getAktionList method of VehicleServiceImpl");
		List<DropdownObject> aktionsList = new ArrayList<>();

		try {
			StringBuilder query = new StringBuilder("SELECT VOR_TXTCDE AS KEYFLD, VOR_TEXT AS DATAFLD FROM ");
			query.append(schema).append(".PSC_VAART ORDER BY VOR_TXTCDE");

			Map<String, String> aktionsMap = dbServiceRepository.getResultUsingCobolQuery(query.toString());

			if(aktionsMap != null && !aktionsMap.isEmpty()) {
				for(Map.Entry<String, String> aktionsDetails : aktionsMap.entrySet()) {

					DropdownObject dropdownObject = new DropdownObject();
					dropdownObject.setKey(aktionsDetails.getKey());
					dropdownObject.setValue(aktionsDetails.getValue());

					aktionsList.add(dropdownObject);
				}
			}else{
				log.info("Aktion List is empty");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Aktion"));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Aktion"));
				throw exception;
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Aktion"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Aktion"), exception);
			throw exception;
		}

		return aktionsList;
	}


	@Override
	public Map<String, Boolean> checkDuplicateLicencePlate(String licencePlate, String dataLibrary, String companyId, String agencyId) {

		log.info("Inside checkDuplicateLicencePlate method of VehicleServiceImpl");

		//validate the company Id 
		validateCompany(companyId);

		Map<String, Boolean> outputMap = new HashMap<>();
		outputMap.put("isDuplicate", false);
		try {
			String compID =  StringUtils.stripStart(companyId, "0");

			StringBuilder query = new StringBuilder( "SELECT FKENNZ7 FROM ");
			query.append(dataLibrary).append(".M").append(compID).append("_KFZF1XX WHERE VZSL1=7 AND FKENNZ7 = '").append(licencePlate+"'");

			List<VehicleKFZF1XX> vehicleLicenceNoList = dbServiceRepository.getResultUsingQuery(VehicleKFZF1XX.class, query.toString(),true);

			if(vehicleLicenceNoList!=null && !vehicleLicenceNoList.isEmpty()) {

				outputMap.put("isDuplicate", true);
				return outputMap;
			}

		}
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Vehicle"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Vehicle"), exception);
			throw exception;
		}

		return outputMap;
	}

	@Override
	public Map<String, Boolean> checkDuplicateVIN(String vin, String dataLibrary, String companyId, String agencyId) {

		log.info("Inside checkDuplicateVIN method of VehicleServiceImpl");

		//validate the company Id 
		validateCompany(companyId);

		if(vin == null || vin.trim().length() != 18){
			log.info("Vehicle identification number is not valid");
			AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.VIN_LENGTH_INVALID_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.VIN_LENGTH_INVALID_MSG_KEY));
			throw exception;
		}

		Map<String, Boolean> outputMap = new HashMap<>();
		outputMap.put("isDuplicate", false);
		outputMap.put("isVINChecksum", false);

		boolean isChecksumFlag = checkVINCheckSum(vin);
		if (isChecksumFlag) {
			log.info("Vehicle identification number checksum (VIN) checksum is valid");
			outputMap.put("isVINChecksum", true);
		} else {
			log.info("Vehicle identification number(VIN) checksum is not valid : "+vin);
			//AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.VIN_CHECKSUM_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.VIN_CHECKSUM_MSG_KEY));
			//throw exception;
		}

		String whc = vin.substring(0,3);
		String baum = vin.substring(3,11);
		String endn = vin.substring(11,17);
		String fgp = vin.substring(17,18);

		try {
			String compID =  StringUtils.stripStart(companyId, "0");

			StringBuilder query = new StringBuilder( "SELECT FKENNZ7 FROM ");
			query.append(dataLibrary).append(".M").append(compID).append("_KFZF1XX WHERE VZSL1=7 AND ");
			query.append(" WHC = ").append("'"+whc+"'").append(" AND BAUM = ").append("'"+baum+"'");
			query.append(" AND ENDN = ").append("'"+endn+"'").append(" AND FGP = ").append("'"+fgp+"'");

			List<VehicleKFZF1XX> vehicleLicenceNoList = dbServiceRepository.getResultUsingQuery(VehicleKFZF1XX.class, query.toString(),true);

			if(vehicleLicenceNoList!=null && !vehicleLicenceNoList.isEmpty()) {

				outputMap.put("isDuplicate", true);
				return outputMap;
			}

		}
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Vehicle"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Vehicle"), exception);
			throw exception;
		}

		return outputMap;
	}


	private boolean checkVINCheckSum(String vin) {
		log.info("Start : checkVINCheckSum() ");
		boolean isValid = false;

		Map<String, Integer> keyValueMap = new HashMap<>();
		Map<Integer, Integer> positionWeightsMap = new HashMap<>();

		// values for VIN Decoding
		keyValueMap.put("A", 1);  keyValueMap.put("J", 1);   keyValueMap.put("S", 2);
		keyValueMap.put("B", 2);  keyValueMap.put("K", 2);   keyValueMap.put("T", 3);
		keyValueMap.put("C", 3);  keyValueMap.put("L", 3);   keyValueMap.put("U", 4);
		keyValueMap.put("D", 4);  keyValueMap.put("M", 4);	 keyValueMap.put("V", 5);
		keyValueMap.put("E", 5);  keyValueMap.put("N", 5);	 keyValueMap.put("W", 6);	
		keyValueMap.put("F", 6);  keyValueMap.put("O", 0);   keyValueMap.put("X", 7);
		keyValueMap.put("G", 7);  keyValueMap.put("P", 7);   keyValueMap.put("Y", 8);
		keyValueMap.put("H", 8);  keyValueMap.put("Q", 8);   keyValueMap.put("Z", 9);
		keyValueMap.put("I", 9);  keyValueMap.put("R", 9);   

		//Weights used in calculation

		positionWeightsMap.put(1, 2);   positionWeightsMap.put(10, 2);
		positionWeightsMap.put(2, 3);   positionWeightsMap.put(11, 3);
		positionWeightsMap.put(3, 4);   positionWeightsMap.put(12, 4);
		positionWeightsMap.put(4, 5);	positionWeightsMap.put(13, 5);
		positionWeightsMap.put(5, 6);	positionWeightsMap.put(14, 6);
		positionWeightsMap.put(6, 7);	positionWeightsMap.put(15, 7);
		positionWeightsMap.put(7, 8);	positionWeightsMap.put(16, 8);
		positionWeightsMap.put(8, 9);	positionWeightsMap.put(17, 9);
		positionWeightsMap.put(9, 10);

		vin = vin.toUpperCase();
		int modValue = 11;
		int sum = 0;
		log.debug("VIN Number :"+vin);

		for (int i = 0; i < 17; i++) {
			char vinChar = vin.charAt(i);
			int value=0;

			Boolean isAlphabetic = Character.isAlphabetic(vinChar);
			if(isAlphabetic){
				value = keyValueMap.get(String.valueOf(vinChar));
			}else{
				value =  Character.getNumericValue(vinChar); 
			}

			log.debug("This Char " + vinChar +" Value is: " +value);

			int weight = positionWeightsMap.get(17-i);

			log.debug("Position " + (17-i) +" weight is: " +weight);

			sum = sum + (value * weight);

			log.debug("sum + (value * weight) :" +sum);	
		}
		log.debug("Final sum :" +sum);
		sum = sum % modValue;

		log.debug("Reminder  :" +sum);

		char checkDigit = vin.charAt(17);
		log.debug("checkDigit :"+checkDigit);

		if((checkDigit == 'X' && sum == 10 ) || Character.getNumericValue(checkDigit)  == sum){
			isValid = true;
		}
		return isValid;
	}

}