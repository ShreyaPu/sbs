package com.alphax.service.mb.impl;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.alphax.common.rest.message.service.MessageService;
import com.alphax.model.mb.AlphaXConfigurationKeysDetails;
import com.alphax.common.exception.AlphaXException;
import com.alphax.common.exception.ExceptionMessages;
import com.alphax.repository.CobolServiceRepository;
import com.alphax.repository.DBServiceRepository;
import com.alphax.service.mb.DashboardService;
import com.alphax.vo.mb.AlphaXConfigurationKeysDetailsDTO;
import com.alphax.vo.mb.AlphaXConfigurationSetupDTO;
import com.alphax.common.constants.Program_Commands_Constants;
import com.alphax.common.constants.RestInputConstants;
import com.ibm.as400.access.ProgramParameter;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DashboardServiceImpl extends BaseService implements DashboardService {

	@Autowired
	DBServiceRepository dbServiceRepository;

	@Autowired
	private MessageService messageService;

	@Autowired
	CobolServiceRepository cobolServiceRepository;
	


	@Override
	public Map<String, String> getGoodsReceipt(String dataLibrary, String schema, String companyId, String agencyId, String allowedWarehouses) {

		log.info("Inside getGoodsReceipt method of DashboardServiceImpl");

		//validate the company Id 
		validateCompany(companyId);

		//validate the agency Id
		validateAgency(agencyId);

		//validate the Warehouse Ids
		validateWarehouses(allowedWarehouses);

		String count = "0";
		Map<String,String> resultCounts = new HashMap<String, String>();

		try{
			//Lieferscheine Eingangspool
			StringBuilder query_neue_lieferscheine = new StringBuilder(" SELECT COUNT(DISTINCT LFSNR) AS COUNT FROM ");
			query_neue_lieferscheine.append(dataLibrary).append(".E_LFSALL WHERE SA=53 AND BSN450 <> 'VVV' AND BSN450 <> 'LLL' ");
			query_neue_lieferscheine.append(" AND LNR in (").append(allowedWarehouses).append(")");

			//Positionen mit Terminen
			StringBuilder query_ankundigungen = new StringBuilder(" SELECT COUNT(DISTINCT LFSNR) AS COUNT FROM ");
			query_ankundigungen.append(dataLibrary).append(".E_LFSALL WHERE SA=54 and BSN450 <> 'LLL' ");
			query_ankundigungen.append(" and LNR in (").append(allowedWarehouses).append(")");

			//Positionen mit Terminen
			StringBuilder query_abkundigungen = new StringBuilder(" SELECT COUNT(DISTINCT LFSNR) AS COUNT FROM ");
			query_abkundigungen.append(dataLibrary).append(".E_LFSALL WHERE SA=56 and BSN450 <> 'LLL' ");
			query_abkundigungen.append(" and LNR in (").append(allowedWarehouses).append(")");

			//Lieferscheine Wareneingang
			StringBuilder query_lieferscheine_in_bsn = new StringBuilder(" SELECT COUNT(*)  AS COUNT FROM ( SELECT DISTINCT HERST, LNR, BEST_BENDL, LIEFNR_GESAMT, BDAT FROM ");
			query_lieferscheine_in_bsn.append(dataLibrary).append(".E_BSNDAT WHERE STATUS <= 56 AND LIEFNR_GESAMT <>'' AND ET_FEHLER IN ( 'X' , ' ', '*') ");
			query_lieferscheine_in_bsn.append(" AND LNR in (").append(allowedWarehouses).append(") AND ZUMENG <> 0 ");
			query_lieferscheine_in_bsn.append(" AND NOT EXISTS (SELECT * FROM ").append(schema).append(".O_BSNCONF B WHERE B.HERST = HERST and B.LNR = LNR and B.BEST_BENDL = BEST_BENDL and B.LFSNR = LIEFNR_GESAMT) Group BY HERST, LNR, BEST_BENDL, LFSNR, BDAT )");

			//Offene Bestellungen
			StringBuilder query_offene_Bestellungen = new StringBuilder(" (select count(*) AS COUNT from ");
			query_offene_Bestellungen.append("(select  distinct ET_LNR, BHERST, BEST_BEND, PSERV_AUFNR, ASERV_AUFNR, BDAT from ").append(dataLibrary).append(".e_eskdat ");
			query_offene_Bestellungen.append(" where ET_STATUS ='' and not SERV_STATUS='R' and BEST_BEND='' ");
			query_offene_Bestellungen.append(" and ET_LNR in (").append(allowedWarehouses).append(") ))");

			//Positionen mit offenen Konflikten 
			StringBuilder query_lieferscheine_mit_konflikten  = new StringBuilder(" (select count(*) AS COUNT from ");
			query_lieferscheine_mit_konflikten.append(" (select distinct HERST, LNR, BEST_BENDL, LIEFNR_GESAMT, BDAT from ").append(dataLibrary).append(".E_BSNDAT ");
			query_lieferscheine_mit_konflikten.append(" Where STATUS  > 54 AND   STATUS  < 90 AND LIEFNR_GESAMT <>' ' AND  ET_FEHLER = 'X' ");
			query_lieferscheine_mit_konflikten.append(" AND LNR in (").append(allowedWarehouses).append(") ");
			query_lieferscheine_mit_konflikten.append(" AND EXISTS (SELECT *  FROM ").append(schema).append(".O_BSNCONF B WHERE B.HERST = HERST and B.LNR = LNR and B.BEST_BENDL = BEST_BENDL and B.LFSNR = LIEFNR_GESAMT) Group BY HERST, LNR, BEST_BENDL, LFSNR, BDAT)) ");

			//Ausgelöste Bestellungen
			StringBuilder query_ausgelöste_bestellungen  = new StringBuilder(" (select count(*) AS COUNT from ");
			query_ausgelöste_bestellungen.append("(select  distinct (BEST_ART|| BEST_AP), SERV_AUFNRX, PSERV_AUFNR, LNR, HERST, BDAT, BVER, STATUS from ").append(dataLibrary).append(".e_bsndat ");
			query_ausgelöste_bestellungen.append(" where ((serv_aufnrx <>'000000' and serv_aufnrx <>'') OR (PSERV_AUFNR > 0)) ");
			query_ausgelöste_bestellungen.append(" AND LNR in (").append(allowedWarehouses).append(") ))");

			count = dbServiceRepository.getCountUsingQuery(query_neue_lieferscheine.toString());
			resultCounts.put(RestInputConstants.NEW_LIEFERSCHEINE, count);

			count = dbServiceRepository.getCountUsingQuery(query_ankundigungen.toString());
			resultCounts.put(RestInputConstants.ANKUNDIGUNGEN, count);

			count = dbServiceRepository.getCountUsingQuery(query_abkundigungen.toString());
			resultCounts.put(RestInputConstants.ABKUNDIGUNGEN, count);

			count = dbServiceRepository.getCountUsingQuery(query_lieferscheine_in_bsn.toString());
			resultCounts.put(RestInputConstants.LIEF_IN_BSN, count);

			count = dbServiceRepository.getCountUsingQuery(query_offene_Bestellungen.toString());
			resultCounts.put(RestInputConstants.OFFENE_BESTELLUNGEN, count);

			count = dbServiceRepository.getCountUsingQuery(query_lieferscheine_mit_konflikten.toString());
			resultCounts.put(RestInputConstants.LIEF_MIT_KONFLIKTEN, count);

			count = dbServiceRepository.getCountUsingQuery(query_ausgelöste_bestellungen.toString());
			resultCounts.put(RestInputConstants.AUSGELOSTE_BESTELLUNGEN, count);

		}catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Wareneingang (Goods Receipt)"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Wareneingang (Goods Receipt)"), exception);
			throw exception;
		}
		
		return resultCounts;

	}


	@Override
	public Map<String, String> getDailyUpdateCount(String dataLibrary, String allowedWarehouses) {

		log.info("Inside getDailyUpdateCount method of DashboardServiceImpl");

		//validate the Warehouse Ids
		validateWarehouses(allowedWarehouses);

		String count = "0";
		Map<String,String> resultCounts = new HashMap<String, String>();
		try{

			LocalDate ldate = LocalDate.now();
			DateTimeFormatter dateformatter = DateTimeFormatter.ofPattern("YYMMdd");
			log.info("Todays date: "+ dateformatter.format(ldate)); 

			String todaysdate = dateformatter.format(ldate); 

			//Anderungen DAK
			StringBuilder query_anderungen_dak = new StringBuilder(" SELECT COUNT(*) AS COUNT FROM ");
			query_anderungen_dak.append(dataLibrary).append(".E_CPSDAT WHERE DAKEWV <> DAKEWN ");
			query_anderungen_dak.append(" AND VFNR in (").append(allowedWarehouses).append(")");
			query_anderungen_dak.append(" AND JJMMTT = ").append(todaysdate);

			//Manuelle Zahlung
			StringBuilder query_manuelle_zahlung = new StringBuilder(" SELECT COUNT(*) AS COUNT FROM ");
			query_manuelle_zahlung.append(dataLibrary).append(".E_CPSDAT WHERE BART = 67 ");
			query_manuelle_zahlung.append(" and VFNR in (").append(allowedWarehouses).append(")");
			query_manuelle_zahlung.append(" AND JJMMTT = ").append(todaysdate);

			//Manuelle Korrekturen
			StringBuilder query_manuelle_korrekturen = new StringBuilder(" SELECT COUNT(*) AS COUNT FROM ");
			query_manuelle_korrekturen.append(dataLibrary).append(".E_CPSDAT WHERE (BART = 6 OR BART = 25) ");
			query_manuelle_korrekturen.append(" and VFNR in (").append(allowedWarehouses).append(")");
			query_manuelle_korrekturen.append(" AND JJMMTT = ").append(todaysdate);

			//Rucknahmen
			StringBuilder query_rucknahmen = new StringBuilder(" SELECT COUNT(*) AS COUNT FROM ");
			query_rucknahmen.append(dataLibrary).append(".E_CPSDAT WHERE BART = 5 ");
			query_rucknahmen.append(" AND VFNR in (").append(allowedWarehouses).append(") ");
			query_rucknahmen.append(" AND JJMMTT = ").append(todaysdate);

			//Verkauf/Gutschrift ohne Bestandsbewegung 
			StringBuilder query_verkauf_gutschrift = new StringBuilder(" SELECT COUNT(*) AS COUNT FROM ");
			query_verkauf_gutschrift.append(dataLibrary).append(".E_CPSDAT WHERE (BART = 98 OR BART = 99) ");
			query_verkauf_gutschrift.append(" AND VFNR in (").append(allowedWarehouses).append(") ");
			query_verkauf_gutschrift.append(" AND JJMMTT = ").append(todaysdate);

			count = dbServiceRepository.getCountUsingQuery(query_anderungen_dak.toString());
			resultCounts.put(RestInputConstants.ANDERUNGEN_DAK, count);

			count = dbServiceRepository.getCountUsingQuery(query_manuelle_zahlung.toString());
			resultCounts.put(RestInputConstants.MANUELLE_ZAHLUNG, count);

			count = dbServiceRepository.getCountUsingQuery(query_manuelle_korrekturen.toString());
			resultCounts.put(RestInputConstants.MANUELLE_KORREKTUREN, count);

			count = dbServiceRepository.getCountUsingQuery(query_rucknahmen.toString());
			resultCounts.put(RestInputConstants.RUCKNAHMEN, count);

			count = dbServiceRepository.getCountUsingQuery(query_verkauf_gutschrift.toString());
			resultCounts.put(RestInputConstants.VERKAUF_GUTSCHRIFT, count);

		}catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, " Daily Update Count"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Daily Update Count"), exception);
			throw exception;
		}
		
		return resultCounts;

	}


	@Override
	public List<AlphaXConfigurationSetupDTO> getAlphaXConfigurationDetails(String schema,String dataLibrary, 
			String companyId, String agencyId) {

		log.info("Inside getAlphaXConfigurationDetails method of DashboardServiceImpl");

		//validate the company Id 
		validateCompany(companyId);
		List<AlphaXConfigurationSetupDTO> configurationSetupList = new ArrayList<AlphaXConfigurationSetupDTO>();
		List<AlphaXConfigurationKeysDetailsDTO> configurationKeysDetailsList = null;
		AlphaXConfigurationKeysDetailsDTO configurationKeysDetails =null;
		AlphaXConfigurationSetupDTO configurationSetup = null;

		String companyNo;
		String agencyNo;
		String agencyName;

		try{
			StringBuilder query_1 = new StringBuilder("SELECT KEYFLD, DATAFLD FROM ");
			query_1.append(dataLibrary).append(".REFERENZ WHERE KEYFLD LIKE '0000019903").append(companyId).append("%'");

			StringBuilder query_2 = new StringBuilder("SELECT COMPANY_Id, AGENCY_Id, KEY, VALUE, KEY_TYPE, FIELD_TYPE, UPDATED_BY ,TIMESTAMP FROM ");
			query_2.append(schema).append(".O_SETUP WHERE COMPANY_Id = ").append(companyId);

			Map<String, String> agencyListMap = dbServiceRepository.getResultUsingCobolQuery(query_1.toString());
			List<AlphaXConfigurationKeysDetails> configurationDetailsList = dbServiceRepository.getResultUsingQuery(AlphaXConfigurationKeysDetails.class, query_2.toString(),true);


			if(agencyListMap != null && !agencyListMap.isEmpty()) {

				for(Map.Entry<String, String> companyValues : agencyListMap.entrySet()) {

					String keyFild = companyValues.getKey().trim();
					String companyAndAgency = keyFild.substring(keyFild.trim().length() - 4);
					agencyName = companyValues.getValue().substring(6, 36).trim();
					companyNo = StringUtils.stripStart(companyAndAgency.substring(0,2),"0");
					agencyNo = StringUtils.stripStart(companyAndAgency.substring(2,4),"0");

					configurationKeysDetailsList = new ArrayList<AlphaXConfigurationKeysDetailsDTO>();

					if (configurationDetailsList != null && !configurationDetailsList.isEmpty()) {

						for (AlphaXConfigurationKeysDetails keysDetails : configurationDetailsList) {

							String agencyNumber = String.valueOf(keysDetails.getAgencyId());
							String yyyymmdd = "";
							String hhmmss = "";

							if (agencyNumber.equalsIgnoreCase(agencyNo)) {
								configurationKeysDetails = new AlphaXConfigurationKeysDetailsDTO();
								configurationKeysDetails.setKey(keysDetails.getKey());
								configurationKeysDetails.setValue(keysDetails.getValue());
								configurationKeysDetails.setUpdateby(keysDetails.getUpdateby());
								String updateTimestamp = keysDetails.getUpdatedTime();
								if(updateTimestamp != null && updateTimestamp.length() > 20){
									yyyymmdd = updateTimestamp.substring(0,updateTimestamp.length()-16);
									hhmmss = updateTimestamp.substring(11,updateTimestamp.length()-7);
									configurationKeysDetails.setUpdatedTime(yyyymmdd +" / "+ hhmmss );
								}else{
									configurationKeysDetails.setUpdatedTime("");
								}
								configurationKeysDetails.setUpdated(false);

								configurationKeysDetailsList.add(configurationKeysDetails);
							}
						}
					}
					configurationSetup = new AlphaXConfigurationSetupDTO();
					configurationSetup.setAgencyId(agencyNo);
					configurationSetup.setCompanyId(companyNo);
					configurationSetup.setAgencyName(agencyName);
					configurationSetup.setSetupKeysList(configurationKeysDetailsList);
					configurationSetupList.add(configurationSetup);
				}
			}
		}
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "AlphaX Configuration Details"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "AlphaX Configuration Details"), exception);
			throw exception;
		}

		return configurationSetupList;
	}

	@Override
	public List<AlphaXConfigurationSetupDTO> updateAlphaXConfigurationDetails(List<AlphaXConfigurationSetupDTO> alphaXConfigurationSetupList,
			String schema, String dataLibrary) {

		log.info("Inside getAlphaXConfigurationDetails method of DashboardServiceImpl");

		List<AlphaXConfigurationSetupDTO> configurationSetupList = new ArrayList<AlphaXConfigurationSetupDTO>();
		List<AlphaXConfigurationKeysDetailsDTO> configurationKeysDetailsList = null;

		try{
			if (alphaXConfigurationSetupList != null && !alphaXConfigurationSetupList.isEmpty()) {

				for (AlphaXConfigurationSetupDTO updatedSetupDetails : alphaXConfigurationSetupList) {

					configurationKeysDetailsList = new ArrayList<AlphaXConfigurationKeysDetailsDTO>();
					for (AlphaXConfigurationKeysDetailsDTO keysDetails : updatedSetupDetails.getSetupKeysList()) {
						boolean updateFlag = false;
						if (keysDetails.isUpdated()) {

							StringBuilder query = new StringBuilder(" UPDATE ").append(schema).append(".O_SETUP SET ");
							query.append(" VALUE = '").append(keysDetails.getValue()+"'").append(", UPDATED_BY = ").append("'" + keysDetails.getUpdateby() + "'").append(", TIMESTAMP = CURRENT TIMESTAMP ");
							query.append(" WHERE COMPANY_Id = ").append(updatedSetupDetails.getCompanyId()).append(" AND AGENCY_Id = ").append(updatedSetupDetails.getAgencyId()).append(" AND KEY = ").append("'" + keysDetails.getKey() + "'");

							updateFlag = dbServiceRepository.updateResultUsingQuery(query.toString());
						}
						if (updateFlag) {
							keysDetails.setUpdated(false);
						}

						StringBuilder query_2 = new StringBuilder("SELECT TIMESTAMP FROM ").append(schema).append(".O_SETUP WHERE ");
						query_2.append("COMPANY_ID = ").append(updatedSetupDetails.getCompanyId()).append(" AND AGENCY_ID = ").append(updatedSetupDetails.getAgencyId()).append(" AND KEY = ").append("'"+keysDetails.getKey()+"'");

						List<AlphaXConfigurationKeysDetails> configurationDetailsList = dbServiceRepository.getResultUsingQuery(AlphaXConfigurationKeysDetails.class, query_2.toString(),true);

						if (configurationDetailsList != null && !configurationDetailsList.isEmpty()) {

							for (AlphaXConfigurationKeysDetails details : configurationDetailsList) {

								String yyyymmdd = "";
								String hhmmss = "";
								String updateTimestamp = details.getUpdatedTime() ;

								if( updateTimestamp != null && updateTimestamp.length() > 20){
									yyyymmdd = updateTimestamp.substring(0,updateTimestamp.length()-16);
									hhmmss = updateTimestamp.substring(11,updateTimestamp.length()-7);
									updateTimestamp = yyyymmdd +" / "+ hhmmss ;
								}else{
									updateTimestamp = "";
								}
								keysDetails.setUpdatedTime(updateTimestamp);
							}
						}		

						configurationKeysDetailsList.add(keysDetails);
					}
					updatedSetupDetails.setSetupKeysList(configurationKeysDetailsList);
					updatedSetupDetails.getCompanyId();
					configurationSetupList.add(updatedSetupDetails);
				}
			}
		}
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "AlphaX Configuration Details"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "AlphaX Configuration Details"), exception);
			throw exception;
		}
		return configurationSetupList;
	}
	


	@Override
	public Map<String, String> create_OSetupTable(String dataLibrary, String companyId, String agencyId) {

		log.info("Inside create_OSetupTable method of DashboardServiceImpl");

		ProgramParameter[] parmList;
		Map<String, String> programOutput = new HashMap<String, String>();
		try{
			parmList = new ProgramParameter[3];

			// Create the input parameter 
			String returnCode = StringUtils.rightPad("", 5, " ");
			String returnMsg  = StringUtils.rightPad("", 100, " ");
			String funktion =  "OSETUP";

			parmList[0] = new ProgramParameter(returnCode.getBytes(Program_Commands_Constants.IBM_273),3);
			parmList[1] = new ProgramParameter(returnMsg.getBytes(Program_Commands_Constants.IBM_273),5);
			parmList[2] = new ProgramParameter(funktion.getBytes(Program_Commands_Constants.IBM_273),5);

			List<String> outputList = cobolServiceRepository.executeProgram(parmList, 0, Program_Commands_Constants.CREATE_O_SETUP_TABLE_PROGRAM );

			if(outputList != null && !outputList.isEmpty()) {
				if(!outputList.get(0).contains("Error")) {
					programOutput.put(outputList.get(0).trim(), outputList.get(1).trim());
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.CREATE_FAILED_MSG_KEY , "O_Setup Table"));
			log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY , "O_Setup Table"), exception);
			throw exception;
		}
		return programOutput;
	}


	/**
	 * This method is used to get the Automatic delivery note processing details (Automatische Lieferscheinverarbeitung) from DB table O_SETUP
	 * @return List
	 */
	@Override
	public List<AlphaXConfigurationKeysDetailsDTO> getAutomaticDeliveryNoteProcessingDetails(String schema,String dataLibrary, 
			String warehouseId,String companyId) {

		log.info("Inside getAutomaticDeliveryNoteProcessingDetails method of DashboardServiceImpl");

		List<AlphaXConfigurationKeysDetailsDTO> configurationKeysDetailsList = new ArrayList<>();
		AlphaXConfigurationKeysDetailsDTO configurationKeysDetails =null;

		try{

			StringBuilder query = new StringBuilder("SELECT KEY, VALUE, TIMESTAMP, UPDATED_BY FROM ");
			query.append(schema).append(".O_SETUP WHERE  WAREHOUS_Id = ").append(warehouseId);

			List<AlphaXConfigurationKeysDetails> configurationDetailsList = dbServiceRepository.getResultUsingQuery(AlphaXConfigurationKeysDetails.class, query.toString(),true);

			if (configurationDetailsList != null && !configurationDetailsList.isEmpty()) {

				for (AlphaXConfigurationKeysDetails keysDetails : configurationDetailsList) {

					String yyyymmdd = "";
					String hhmmss = "";

					configurationKeysDetails = new AlphaXConfigurationKeysDetailsDTO();
					configurationKeysDetails.setKey(keysDetails.getKey());
					configurationKeysDetails.setValue(keysDetails.getValue());
					configurationKeysDetails.setUpdateby(keysDetails.getUpdateby());
					String updateTimestamp = keysDetails.getUpdatedTime();
					if (updateTimestamp != null && updateTimestamp.length() > 20) {
						yyyymmdd = updateTimestamp.substring(0, updateTimestamp.length() - 16);
						hhmmss = updateTimestamp.substring(11, updateTimestamp.length() - 7);
						configurationKeysDetails.setUpdatedTime(yyyymmdd + " / " + hhmmss);
					} else {
						configurationKeysDetails.setUpdatedTime("");
					}
					configurationKeysDetails.setUpdated(false);
					configurationKeysDetails.setWarehouseId(warehouseId);
					//configurationKeysDetails.setUpdateby(keysDetails.getUpdateby());
					configurationKeysDetailsList.add(configurationKeysDetails);
				}
			}

		}catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Automatic Deliverynote Processing Details"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Automatic Deliverynote Processing Details"), exception);
			throw exception;
		}

		return configurationKeysDetailsList;
	}


	/**
	 * This method is used to update  the Automatic delivery note processing details (Automatische Lieferscheinverarbeitung) into DB table O_SETUP
	 * @return List
	 */
	@Override
	public List<AlphaXConfigurationKeysDetailsDTO> updateAutomaticDeliveryNoteProcessingDetails(List<AlphaXConfigurationKeysDetailsDTO> alphaXConfigurationSetupList,
			String schema, String dataLibrary,String companyId) {

		log.info("Inside updateAutomaticDeliveryNoteProcessingDetails method of DashboardServiceImpl");

		List<AlphaXConfigurationKeysDetailsDTO> configurationKeysDetailsList = new ArrayList<>();

		try{
			if (alphaXConfigurationSetupList != null && !alphaXConfigurationSetupList.isEmpty()) {

				for (AlphaXConfigurationKeysDetailsDTO updatedDetails : alphaXConfigurationSetupList) {

					boolean updateFlag = false;
					if (updatedDetails.isUpdated()) {

						StringBuilder query = new StringBuilder(" UPDATE ").append(schema).append(".O_SETUP SET ");
						query.append(" VALUE = '").append(updatedDetails.getValue()+"'").append(", UPDATED_BY = ").append("'" + updatedDetails.getUpdateby() + "'").append(", TIMESTAMP = CURRENT TIMESTAMP ");
						query.append(" WHERE COMPANY_ID=0 AND AGENCY_ID=0 AND ROLE_ID=0 AND PRINTER_ID=0 AND USER_ID = 0 ").append(" AND KEY = ").append("'" + updatedDetails.getKey() + "'");
						query.append(" AND WAREHOUS_Id = ").append(updatedDetails.getWarehouseId());

						updateFlag = dbServiceRepository.updateResultUsingQuery(query.toString());
					}
					if (updateFlag) {
						updatedDetails.setUpdated(false);
					}

					configurationKeysDetailsList.add(updatedDetails);
				}
			}
		}
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Automatic Deliverynote Processing"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Automatic Deliverynote Processing"), exception);
			throw exception;
		}
		return configurationKeysDetailsList;
	}


	/**
	 * Inventur Übersicht - (Inventory Overview Counts)
	 */
	@Override
	public Map<String, String> getInventoryOverviewCounts(String dataLibrary, String schema, String allowedApWarehouse) {

		log.info("Inside getInventoryOverviewCounts method of DashboardServiceImpl");

		//validate the Warehouse Ids
		validateWarehouses(allowedApWarehouse);
		String count = "0";
		Map<String,String> resultCounts = new HashMap<String, String>();

		try {

			//Anzahl gezählte Teile - (Number of parts counted)
			StringBuilder query_Anzahl_gezahlte_Teile  = new StringBuilder(" select count(*) AS COUNT from ").append(dataLibrary);
			query_Anzahl_gezahlte_Teile.append(".E_ETSTAMM where IVKZ != '' AND LNR IN (").append(allowedApWarehouse).append(") AND (SA = 1 OR (SA = 2 AND AKTBES > 0))");

			//Anzahl ungezählte Teile - (Number of uncounted parts)
			StringBuilder query_Anzahl_ungezahlte_Teile = new StringBuilder("select count(*) AS COUNT from ").append(dataLibrary);
			query_Anzahl_ungezahlte_Teile.append(".E_ETSTAMM where IVKZ = '' AND LNR IN (").append(allowedApWarehouse).append(") AND (SA = 1 OR (SA = 2 AND AKTBES > 0)) ");

			count = dbServiceRepository.getCountUsingQuery(query_Anzahl_gezahlte_Teile.toString());
			resultCounts.put(RestInputConstants.ANZAHL_GEZAHLTE, count);

			count = dbServiceRepository.getCountUsingQuery(query_Anzahl_ungezahlte_Teile.toString());
			resultCounts.put(RestInputConstants.ANZAHL_UNGEZAHLTE, count);

		} 
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Inventory overview counts"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Inventory overview counts"), exception);
			throw exception;
		}

		return resultCounts;
	}


}
