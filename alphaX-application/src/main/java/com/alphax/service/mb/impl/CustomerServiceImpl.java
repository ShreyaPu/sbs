package com.alphax.service.mb.impl;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
import com.alphax.model.mb.CustomerContactPersons;
import com.alphax.model.mb.CustomerDefaultData;
import com.alphax.model.mb.CustomerDetail;
import com.alphax.model.mb.CustomerInboxStandardLine;
import com.alphax.model.mb.CustomerInboxTemporaryLine;
import com.alphax.model.mb.CustomerLocation;
import com.alphax.model.mb.CustomerUpdateAuthority;
import com.alphax.model.mb.SearchCustomer;
import com.alphax.model.mb.SearchVehicle;
import com.alphax.model.mb.VehicleAuthorityCheck;
import com.alphax.common.exception.AlphaXException;
import com.alphax.common.exception.ExceptionMessages;
import com.alphax.repository.CobolServiceRepository;
import com.alphax.repository.DBServiceRepository;
import com.alphax.repository.StubServiceRepository;
import com.alphax.service.mb.CustomerService;
import com.alphax.vo.mb.AuthorityCheck;
import com.alphax.vo.mb.Customer;
import com.alphax.vo.mb.CustomerContactPersonsDTO;
import com.alphax.vo.mb.CustomerDefaultDataVO;
import com.alphax.vo.mb.CustomerDetailVO;
import com.alphax.vo.mb.CustomerInboxStandardLineDTO;
import com.alphax.vo.mb.CustomerOneCheck;
import com.alphax.vo.mb.DropdownObject;
import com.alphax.vo.mb.GlobalSearch;
import com.alphax.vo.mb.SearchCustomerDTO;
import com.alphax.vo.mb.SearchVehicleDTO;
import com.alphax.common.constants.Program_Commands_Constants;
import com.alphax.common.constants.RestInputConstants;
import com.ibm.as400.access.ProgramParameter;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomerServiceImpl extends BaseService implements CustomerService{

	@Autowired
	CobolServiceRepository cobolServiceRepository;

	@Autowired
	DBServiceRepository dbServiceRepository;

	@Autowired
	StubServiceRepository stubServiceRepository;

	@Autowired
	private MessageService messageService;

	/**
	 *
	 */
	@Override
	public Customer createOrUpdateCustomer(Customer customer) {

		log.info("Inside createCustomer method of CustomerServiceImpl");

		ProgramParameter[] parmList;
		Customer customer_Obj = new Customer();

		String firstName = customer.getVoname()!=null?customer.getVoname():"";
		String lastName = customer.getName()!=null?customer.getName():"";
		String additionalNameInfo = customer.getAdditionalNameInfo()!=null?customer.getAdditionalNameInfo():"";
		String fullName = "";

		//fullName is storing in DB 'Name' column and additionalNameInfo DB Name2 column
		if(customer.isPrivatKunde()){
			fullName = firstName+"*"+lastName; 
			//additionalNameInfo = "";
		}else{
			fullName = lastName;
		}


		String firstScreen = StringUtils.rightPad(customer.getFunction(),RestInputConstants.FUNCTION, " ")
				+ StringUtils.rightPad("",156, " ")
				+ StringUtils.rightPad(customer.getAnredeSchlussel(),RestInputConstants.BANR, " ")
				+ StringUtils.rightPad(customer.getAnrede(),RestInputConstants.ANRE1, " ")
				+ StringUtils.rightPad(fullName,RestInputConstants.NAME1, " ")
				+ StringUtils.rightPad(customer.getSortName(),RestInputConstants.SORTNAME, " ")
				+ StringUtils.rightPad(additionalNameInfo,RestInputConstants.NAME2, " ")
				+ StringUtils.rightPad(customer.getBranchenBez(),RestInputConstants.BRANCHBEZ, " ")
				+ StringUtils.rightPad(customer.getStrasse(),RestInputConstants.STRASSE, " ")
				+ StringUtils.rightPad(customer.getSortStrasse(),RestInputConstants.SORTSTR, " ")
				+ StringUtils.rightPad(customer.getLand(),RestInputConstants.LAND, " ")
				+ StringUtils.rightPad(customer.getPlz(),RestInputConstants.PLZ, " ")
				+ StringUtils.rightPad(customer.getOrt(),RestInputConstants.ORT, " ")
				+ StringUtils.rightPad(customer.getPlzPostf(),RestInputConstants.PPLZ, " ")
				+ StringUtils.leftPad(customer.getPostfach(),RestInputConstants.POSTFACH, "0") //leftPad with 0 value
				+ StringUtils.rightPad(customer.getOrtsteil(),RestInputConstants.ORTSTEIL, " ")
				+ StringUtils.rightPad("",617, " ");
		log.info("FirstScreen :" +firstScreen);
		log.info("FirstScreen Length :" +firstScreen.length());

		String secondScreen  = StringUtils.rightPad(customer.getFunction(),RestInputConstants.FUNCTION, " ")
				+ StringUtils.rightPad("",156, " ")
				+ StringUtils.leftPad(customer.getGeburtstagDate(),RestInputConstants.GEBURTSTAG_DATE, "0")
				+ StringUtils.leftPad(customer.getGeburtstagMonth(),RestInputConstants.GEBURTSTAG_MONTH, "0")
				+ StringUtils.rightPad(customer.getGeburtstagYear(),RestInputConstants.GEBURTSTAG_YEAR, "0")
				+ StringUtils.rightPad(customer.getBranchenschlPart1(),RestInputConstants.BRANCHENSCHL_PART1, " ")
				+ StringUtils.rightPad(customer.getBranchenschlPart2(),RestInputConstants.BRANCHENSCHL_PART2, " ")
				+ StringUtils.rightPad(customer.getBranchenschlPart3(),RestInputConstants.BRANCHENSCHL_PART3, " ")
				+ StringUtils.rightPad(customer.isPrivatKunde()==true?"1":" ",RestInputConstants.PRIVATKUNDE, " ")
				+ StringUtils.rightPad(customer.getAkquisitionskz(),RestInputConstants.AKQUISITIONSKZ, " ")
				+ StringUtils.rightPad(customer.getAbweichendeKdNr(),RestInputConstants.ABWEICHENDE_KDNR, " ")
				+ StringUtils.rightPad(customer.isDagDatentransfer()==true?"1":" ",RestInputConstants.DAG_DATENTRANSFER, " ")
				+ StringUtils.rightPad(customer.getLiefNrBKunde(),RestInputConstants.LIEF_NRB_KUNDE, " ")
				+ StringUtils.rightPad(customer.getAnzahlRechnungsexemplare(),RestInputConstants.ANZAHL_RECHNUNGSEXEMPLARE, " ")
				+ StringUtils.rightPad(customer.getHandelsregister(),RestInputConstants.HANDELSREGISTER, " ")
				+ StringUtils.rightPad(customer.isFahrzeugkunde()==true?"1":" ",RestInputConstants.FAHRZEUGKUNDE, " ")
				+ StringUtils.rightPad(customer.getUstIDNr(),RestInputConstants.UST_ID_NR, " ")
				+ StringUtils.rightPad(customer.isInternerKunde()==true?"1":" ",RestInputConstants.INTERNER_KUNDE, " ")
				+ StringUtils.rightPad(customer.getSteuerNr(),RestInputConstants.STEUER_NR, " ")
				+ StringUtils.rightPad(customer.getMwStKz(),RestInputConstants.MWST_KZ, " ")
				+ StringUtils.rightPad(customer.getWerkstattNr(),RestInputConstants.WERKSTATT_NR, " ")
				+ StringUtils.rightPad(customer.isDruckRabattgruppe()==true?"1":" ",RestInputConstants.DRUCK_RABATTGRUPPE, " ")
				+ StringUtils.rightPad(customer.isDrucAWVerrechnungssatz()==true?"1":" ",RestInputConstants.DRUCK_AW_VERRECHNUNGSSATZ, " ")
				+ StringUtils.rightPad(customer.getKundengruppe(),RestInputConstants.KUNDENGRUPPE, " ")
				+ StringUtils.leftPad(customer.getKreditlimit(),RestInputConstants.KREDITLIMIT, "0") //leftPad with 0 value
				+ StringUtils.rightPad(customer.getBonitat(),RestInputConstants.BONITAT, " ")
				+ StringUtils.rightPad(customer.getZahlungsziel(),RestInputConstants.ZAHLUNGSZIEL, " ")
				+ StringUtils.rightPad(customer.getZuAbschlag(),RestInputConstants.ZU_ABSCHLAG, " ")
				+ StringUtils.rightPad(customer.getSkontoTheke(),RestInputConstants.SKONTO_THEKE, " ")
				+ StringUtils.rightPad(customer.getRabattkz(),RestInputConstants.RABATTKZ, " ")
				+ StringUtils.rightPad("",743, " ");
		log.info("SecondScreen :" +secondScreen);
		log.info("SecondScreen Length :" +secondScreen.length());

		String thirdScreen  = StringUtils.rightPad(customer.getFunction(),RestInputConstants.FUNCTION, " ")
				+ StringUtils.rightPad("",156, " ")
				+ StringUtils.rightPad(customer.getHaupttelefonNummer(),RestInputConstants.HAUPTTELEFONNUMMER, " ")
				+ StringUtils.rightPad(customer.getTelefonNR(),RestInputConstants.TELEFON_NR, " ")
				+ StringUtils.rightPad(customer.getFestnetzTelefonnummer(),RestInputConstants.MOBILFUNKNUMMER, " ")
				+ StringUtils.rightPad(customer.getMobilfunknummer(),RestInputConstants.FESTNETZTELEFONNUMMER, " ")
				+ StringUtils.rightPad(customer.getFaxNummer(),RestInputConstants.FAX_NUMMER, " ")
				+ StringUtils.rightPad(customer.getEmailAddress(),RestInputConstants.EMAIL_ADRESSE, " ")
				+ StringUtils.rightPad(customer.getWebseite(),RestInputConstants.FIRMEN_WEB_SEITE, " ")
				+ StringUtils.rightPad("",560, " ");
		log.info("ThirdScreen :" +thirdScreen);
		log.info("ThirdScreen Length :" +thirdScreen.length());


		// set Herkunft2 dropdown value for cobol
		switch (customer.getHerkunft2().trim()) {
		case "G":
			customer.setHerkunft2("GW-Verkauf");
			break;
		case "S":
			customer.setHerkunft2("Service");
			break;
		case "V":
			customer.setHerkunft2("NW-Verkauf");
			break;
		case "X":
			customer.setHerkunft2("Sonstiges");
			break;
		case "C":
			customer.setHerkunft2("CCS-Portal");
			break;
		default:
			log.info("Default value coming from UI for Herkunft :  {}", customer.getHerkunft2().trim());
			customer.setHerkunft2("");
			break;
		}

		String fourthScreen = StringUtils.rightPad(customer.getFunction(),RestInputConstants.FUNCTION, " ")
				+ StringUtils.rightPad("",156, " ")
				+ StringUtils.rightPad("",RestInputConstants.WAS, " ")
				+ StringUtils.rightPad(customer.isKeineDaimlerDSE1()?"N":"N", RestInputConstants.KEINEDAIMLERDSE, " ")
				+ StringUtils.leftPad(customer.getDabgDate(), RestInputConstants.DATE, "0")
				+ StringUtils.leftPad(customer.getDabgMonth(), RestInputConstants.MONTH, "0")
				+ StringUtils.rightPad(customer.getDabgYear(), RestInputConstants.YEAR, " ")
				+ StringUtils.rightPad(customer.isTelefonYes1()?"N":"N", RestInputConstants.TELEFONYES, " ")
				+ StringUtils.rightPad(customer.isTelefonNo1()?"N":"N", RestInputConstants.TELEFONNO, " ")
				+ StringUtils.rightPad("N", RestInputConstants.OPT_IN_ABGEGEBEN, " ")
				+ StringUtils.rightPad(customer.isFaxYes1()?"N":"N", RestInputConstants.FAXYES, " ")
				+ StringUtils.rightPad(customer.isFaxNo1()?"N":"N", RestInputConstants.FAXNO, " ")
				+ StringUtils.rightPad(customer.isDialogverbot1()?"N":"N", RestInputConstants.DIALOGVERBOT, " ")
				+ StringUtils.rightPad(customer.isSmsYes1()?"N":"N", RestInputConstants.SMSYES, " ")
				+ StringUtils.rightPad(customer.isSmsNo1()?"N":"N", RestInputConstants.SMSNO, " ")
				+ StringUtils.rightPad(customer.isKeineService_CSI_Befragung1()?"N":"N", RestInputConstants.KEINE_SERVICE_CSI_BEFRAGUNG, " ")
				+ StringUtils.rightPad(customer.isEmailYes1()?"N":"N", RestInputConstants.EMAILYES, " ")
				+ StringUtils.rightPad(customer.isEmailNo1()?"N":"N", RestInputConstants.EMAILNO, " ")
				+ StringUtils.rightPad(customer.getBeleg1(), RestInputConstants.BELEG, " ")
				+ StringUtils.rightPad(customer.getHerkunft1(), RestInputConstants.HERKUNFT, " ")
				+ StringUtils.rightPad(customer.isDag_Fzg1()?"0":"0", RestInputConstants.DAG_FZG, " ")
				+ StringUtils.leftPad(customer.getAbgabedatumDD1(), RestInputConstants.DATE, "0")
				+ StringUtils.leftPad(customer.getAbgabedatumMM1(), RestInputConstants.MONTH, "0")
				+ StringUtils.rightPad(customer.getAbgabedatumYY1(), RestInputConstants.YEAR, " ")
				+ StringUtils.rightPad(customer.isAngekreuzt1()?"N":"N", RestInputConstants.ANGEKREUZT, " ")
				+ StringUtils.rightPad(customer.isNichtAngekreuzt1()?"N":"N", RestInputConstants.NICHT_ANGEKREUZT, " ")
				+ StringUtils.leftPad(customer.getAblaufdatumDD1(), RestInputConstants.DATE, "0")
				+ StringUtils.leftPad(customer.getAblaufdatumMM1(), RestInputConstants.MONTH, "0")
				+ StringUtils.rightPad(customer.getAblaufdatumYY1(),RestInputConstants.YEAR, " ")
				+ StringUtils.rightPad(customer.isDialogverbot2()?"J":"N", RestInputConstants.DIALOGVERBOT, " ")
				+ StringUtils.rightPad(customer.isTelefonYes2()?"J":"N", RestInputConstants.TELEFONYES, " ")
				+ StringUtils.rightPad(customer.isTelefonYes2()?"N":"J", RestInputConstants.TELEFONNO, " ")
				+ StringUtils.rightPad(customer.isKeineService_CSI_Befragung2()?"J":"N", RestInputConstants.KEINE_SERVICE_CSI_BEFRAGUNG, " ")
				+ StringUtils.rightPad(customer.isFaxYes2()?"J":"N", RestInputConstants.FAXYES, " ")
				+ StringUtils.rightPad(customer.isFaxYes2()?"N":"J", RestInputConstants.FAXNO, " ")
				+ StringUtils.rightPad(customer.isSmsYes2()?"J":"N", RestInputConstants.SMSYES, " ")
				+ StringUtils.rightPad(customer.isSmsYes2()?"N":"J", RestInputConstants.SMSNO, " ")
				+ StringUtils.rightPad(customer.isEmailYes2()?"J":"N", RestInputConstants.EMAILYES, " ")
				+ StringUtils.rightPad(customer.isEmailYes2()?"N":"J", RestInputConstants.EMAILNO, " ")
				+ StringUtils.rightPad(customer.getBeleg2(), RestInputConstants.BELEG, " ")
				+ StringUtils.rightPad(customer.getHerkunft2(), RestInputConstants.HERKUNFT, " ")
				+ StringUtils.rightPad(customer.isDag_Fzg2()?"1":"0", RestInputConstants.DAG_FZG, " ")
				+ StringUtils.leftPad(customer.getAbgabedatumDD2(), RestInputConstants.DATE, "0")
				+ StringUtils.leftPad(customer.getAbgabedatumMM2(), RestInputConstants.MONTH, "0")
				+ StringUtils.rightPad(customer.getAbgabedatumYY2(), RestInputConstants.YEAR, " ")
				+ StringUtils.rightPad(customer.isAngekreuzt2()?"J":"N", RestInputConstants.ANGEKREUZT, " ")
				+ StringUtils.rightPad(customer.isAngekreuzt2()?"N":"J", RestInputConstants.NICHT_ANGEKREUZT, " ")
				+ StringUtils.leftPad(customer.getAblaufdatumDD2(), RestInputConstants.DATE, "0")
				+ StringUtils.leftPad(customer.getAblaufdatumMM2(), RestInputConstants.MONTH, "0")
				+ StringUtils.rightPad(customer.getAblaufdatumYY2(), RestInputConstants.YEAR, " ")
				+ StringUtils.rightPad(customer.isKeineDaimlerDSE2()?"J":"N", RestInputConstants.KEINEDAIMLERDSE, " ")
				+ StringUtils.rightPad(customer.getDruckerId(), RestInputConstants.DRUCKERID, " ")
				+ StringUtils.rightPad("",730, " ")
				+ StringUtils.rightPad("05",512, " ");

		log.info("fourthScreen :" +fourthScreen);
		log.info("fourthScreen Length :" +fourthScreen.length());

		String fifthScreen  = StringUtils.rightPad(customer.getFunction(),RestInputConstants.FUNCTION, " ")
				+ StringUtils.rightPad("",156, " ")
				+ StringUtils.rightPad(customer.getTemporarInfozeile(), RestInputConstants.TEMPORARINFOZEILE, " ")
				+ StringUtils.rightPad(customer.getPermanentInfozeile1(), RestInputConstants.PERMANNENTINFOZEILE, " ")
				+ StringUtils.rightPad(customer.getPermanentInfozeile2(), RestInputConstants.PERMANNENTINFOZEILE, " ")
				+ StringUtils.rightPad(customer.getPermanentInfozeile3(), RestInputConstants.PERMANNENTINFOZEILE, " ")
				+ StringUtils.rightPad(customer.getPermanentInfozeile4(), RestInputConstants.PERMANNENTINFOZEILE, " ")
				+ StringUtils.rightPad(customer.getPermanentInfozeile5(), RestInputConstants.PERMANNENTINFOZEILE, " ")
				+ StringUtils.rightPad(customer.getPermanentInfozeile6(), RestInputConstants.PERMANNENTINFOZEILE, " ")
				+ StringUtils.rightPad("", RestInputConstants.STRICH, " ")
				+ StringUtils.rightPad(customer.isTemporarInfozeileChb()?"D":" ", RestInputConstants.TEMPORARINFOZEILECHB, " ")
				+ StringUtils.rightPad(customer.isPermanentInfozeileChb1()?"D":" ", RestInputConstants.PERMANNENTINFOZEILECHB, " ")
				+ StringUtils.rightPad(customer.isPermanentInfozeileChb2()?"D":" ", RestInputConstants.PERMANNENTINFOZEILECHB, " ")
				+ StringUtils.rightPad(customer.isPermanentInfozeileChb3()?"D":" ", RestInputConstants.PERMANNENTINFOZEILECHB, " ")
				+ StringUtils.rightPad(customer.isPermanentInfozeileChb4()?"D":" ", RestInputConstants.PERMANNENTINFOZEILECHB, " ")
				+ StringUtils.rightPad(customer.isPermanentInfozeileChb5()?"D":" ", RestInputConstants.PERMANNENTINFOZEILECHB, " ")
				+ StringUtils.rightPad(customer.isPermanentInfozeileChb6()?"D":" ", RestInputConstants.PERMANNENTINFOZEILECHB, " ")
				+ StringUtils.rightPad("",515, " ");

		log.info("fifthScreen :" +fifthScreen);
		log.info("fifthScreen Length :" +fifthScreen.length());

		String sixthScreen  = StringUtils.rightPad(customer.getFunction(),RestInputConstants.FUNCTION, " ")
				+ StringUtils.rightPad("",156, " ")
				+ StringUtils.rightPad(customer.getElektronRechnung(), RestInputConstants.ELEKTRONRECHNUNG, " ")
				+ StringUtils.rightPad(customer.getErEmail(), RestInputConstants.EREMAIL, " ")
				+ StringUtils.rightPad(customer.getDlCode(), RestInputConstants.DLCODE, " ")
				+ StringUtils.rightPad(customer.getVerCode(), RestInputConstants.VERCODE, " ")
				+ StringUtils.rightPad(customer.getBeZeich(), RestInputConstants.BEZEICH, " ")
				+ StringUtils.rightPad(customer.getErDaten(), RestInputConstants.ERDATEN, " ")
				+ StringUtils.rightPad(customer.getErSignatur()?"1":"0", RestInputConstants.ERSIGNATUR, " ")
				+ StringUtils.rightPad(customer.getErAnlagen()?"1":"0", RestInputConstants.ERANLAGEN, " ")
				+ StringUtils.rightPad("",753, " ");

		log.info("sixthScreen :" +sixthScreen);
		log.info("sixthScreen Length :" +sixthScreen.length());

		String inputString = StringUtils.join(firstScreen, secondScreen, thirdScreen, sixthScreen, fourthScreen, fifthScreen);

		log.info("Final inputString Length :"+inputString.length());

		String custID = null;
		String returnCode =null;
		if(customer.getKundenNummer()!=null && !customer.getKundenNummer().trim().isEmpty()){
			log.info("Update InputString :"+inputString);
			custID = StringUtils.rightPad(customer.getKundenNummer().trim(), 8 , " ");
			returnCode = "ANDERUNG";
		}else{
			log.info("Create InputString :"+inputString);
			custID = "        ";
			returnCode = "NEUKUNDE";
		}

		try {
			parmList = new ProgramParameter[6];

			// Create the input parameter  
			parmList[0] = new ProgramParameter(inputString.getBytes(Program_Commands_Constants.IBM_273));
			parmList[1] = new ProgramParameter(custID.getBytes(Program_Commands_Constants.IBM_273));
			parmList[2] = new ProgramParameter(returnCode.getBytes(Program_Commands_Constants.IBM_273));

			parmList[3] = new ProgramParameter(6656);
			parmList[4] = new ProgramParameter(8);
			parmList[5] = new ProgramParameter(8);

			List<String> outputList = cobolServiceRepository.executeProgramWithoutTrim(parmList, 3, Program_Commands_Constants.CREATE_CUSTOMER_PROGRAM);

			if(outputList != null && !outputList.isEmpty()) {
				if(!outputList.get(0).contains("Error")) {
					setCustomerValues(outputList , customer_Obj);
				}
			}

		}catch(AlphaXException ex) {
			throw ex;
		}catch (Exception e) {
			log.info("Error Message:"+e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.CREATE_FAILED_MSG_KEY, "Customer"));
			log.error(messageService.getReadableMessage(ExceptionMessages.CREATE_FAILED_MSG_KEY, "Customer"), exception);
			throw exception;
		}

		return customer_Obj;

	}


	private void setCustomerValues(List<String> outputList , Customer customer) {

		String output1 = outputList.get(0);
		log.info("output length:- {} ", output1.length());

		String additionalNameInfo = output1.substring(223, 253).trim();
		String fullname = output1.substring(178, 208).trim();

		if(output1.substring(1202, 1203).trim().equalsIgnoreCase("1") && fullname.contains("*")){
			String name[] = output1.substring(178, 208).trim().split("\\*");
			customer.setName(name[1]);
			customer.setVoname(name[0]);
		} else {
			customer.setName(fullname);
		}

		customer.setAdditionalNameInfo(additionalNameInfo);
		customer.setAnredeSchlussel(output1.substring(158, 160).trim());
		customer.setAnrede(output1.substring(160, 178).trim());
		//customer.setName(lastName);
		customer.setSortName(output1.substring(208, 223).trim());
		//customer.setVoname(firstName);
		customer.setBranchenBez(output1.substring(253, 283).trim());
		customer.setStrasse(output1.substring(283, 313).trim());
		customer.setSortStrasse(output1.substring(313, 322).trim());
		customer.setLand(output1.substring(322, 324).trim());
		customer.setPlz(output1.substring(324, 330).trim());
		customer.setOrt(output1.substring(330, 355).trim());
		customer.setPlzPostf(output1.substring(355, 361).trim());
		customer.setPostfach(output1.substring(361, 367).trim());
		customer.setOrtsteil(output1.substring(367, 407).trim());
		//First Screen Error code
		customer.setErrorCode(output1.substring(1020, 1024).trim());
		//add all screen error in errorList
		List< String> errorList = new ArrayList<String>();
		errorList.add(output1.substring(515,593).trim()); // First Screen error
		errorList.add(output1.substring(1383,1461).trim());// Second Screen error
		errorList.add(output1.substring(2590,2668).trim());// Third Screen error
		errorList.add(output1.substring(3421,3499).trim());// Sixth Screen error
		errorList.add(output1.substring(4468,4546).trim());// Fourth Screen error
		errorList.add(output1.substring(6141,6219).trim());// Fifth Screen error

		customer.setErrorList(errorList);
		//Customer Number
		customer.setKundenNummer(outputList.get(1));

		customer.setGeburtstagDate(output1.substring(1182, 1184).trim());         
		customer.setGeburtstagMonth(output1.substring(1184, 1186).trim());        
		customer.setGeburtstagYear(output1.substring(1186, 1190).trim());         
		customer.setBranchenschlPart1(output1.substring(1190, 1194).trim());      
		customer.setBranchenschlPart2(output1.substring(1194, 1198).trim());      
		customer.setBranchenschlPart3(output1.substring(1198, 1202).trim());      
		customer.setPrivatKunde(output1.substring(1202, 1203).trim().equalsIgnoreCase("1")?true:false);            
		customer.setAkquisitionskz(output1.substring(1203, 1205).trim());         
		customer.setAbweichendeKdNr(output1.substring(1205, 1213).trim());        
		customer.setDagDatentransfer(output1.substring(1213, 1214).trim().equalsIgnoreCase("1")?true:false);       
		customer.setLiefNrBKunde(output1.substring(1214, 1224).trim());           
		customer.setAnzahlRechnungsexemplare(output1.substring(1224, 1225).trim());
		customer.setHandelsregister(output1.substring(1225, 1245).trim());        
		customer.setFahrzeugkunde(output1.substring(1245, 1246).trim().equalsIgnoreCase("1")?true:false);          
		customer.setUstIDNr(output1.substring(1246, 1261).trim());                
		//customer.setBranchenschlPart3(output1.substring(1261, 1262).trim());      
		customer.setInternerKunde(output1.substring(1261, 1262).trim().equalsIgnoreCase("1")?true:false);          
		customer.setSteuerNr(output1.substring(1262, 1277).trim());               
		customer.setMwStKz(output1.substring(1277, 1278).trim());                 
		customer.setWerkstattNr(output1.substring(1278, 1286).trim());            
		customer.setDruckRabattgruppe(output1.substring(1286, 1287).trim().equalsIgnoreCase("1")?true:false);      
		customer.setDrucAWVerrechnungssatz(output1.substring(1287, 1288).trim().equalsIgnoreCase("1")?true:false); 
		customer.setKundengruppe(output1.substring(1288, 1290).trim());           
		customer.setKreditlimit(output1.substring(1290, 1299).trim());            
		customer.setBonitat(output1.substring(1299, 1300).trim());                
		customer.setZahlungsziel(output1.substring(1300, 1302).trim());           
		customer.setZuAbschlag(output1.substring(1302, 1303).trim());             
		customer.setSkontoTheke(output1.substring(1303, 1304).trim());            
		customer.setRabattkz(output1.substring(1304, 1305).trim());  

		customer.setScreenTwoErrorCode(output1.substring(2044, 2048).trim());

		customer.setHaupttelefonNummer(output1.substring(2206, 2240).trim());
		customer.setTelefonNR(output1.substring(2240, 2254).trim());
		customer.setMobilfunknummer(output1.substring(2254, 2288).trim());
		customer.setFestnetzTelefonnummer(output1.substring(2288, 2322).trim());
		customer.setFaxNummer(output1.substring(2322, 2356).trim());
		customer.setEmailAddress(output1.substring(2356, 2434).trim());
		customer.setWebseite(output1.substring(2434, 2512).trim());
		customer.setScreenThreeErrorCode(output1.substring(3068, 3072).trim());


		customer.setKeineDaimlerDSE2(output1.substring(4275, 4276).trim().equalsIgnoreCase("J")?true:false);
		customer.setDabgDate(output1.substring(4276, 4278).trim());
		customer.setDabgMonth(output1.substring(4278, 4280).trim());
		customer.setDabgYear(output1.substring(4280, 4284).trim());
		customer.setTelefonYes2(output1.substring(4284, 4285).trim().equalsIgnoreCase("J")?true:false);
		customer.setTelefonNo2(output1.substring(4285, 4286).trim().equalsIgnoreCase("N")?false:false);
		customer.setOpt_in_Abgegeben(output1.substring(4286, 4287).trim());
		customer.setFaxYes2(output1.substring(4287, 4288).trim().equalsIgnoreCase("J")?true:false);
		customer.setFaxNo2(output1.substring(4288, 4289).trim().equalsIgnoreCase("J")?false:false);
		customer.setDialogverbot2(output1.substring(4289, 4290).trim().equalsIgnoreCase("J")?true:false);
		customer.setSmsYes2(output1.substring(4290, 4291).trim().equalsIgnoreCase("J")?true:false);
		customer.setSmsNo2(output1.substring(4291, 4292).trim().equalsIgnoreCase("J")?false:false);
		customer.setKeineService_CSI_Befragung2(output1.substring(4292, 4293).trim().equalsIgnoreCase("J")?true:false);
		customer.setEmailYes2(output1.substring(4293, 4294).trim().equalsIgnoreCase("J")?true:false);
		customer.setEmailNo2(output1.substring(4294, 4295).trim().equalsIgnoreCase("J")?false:false);
		customer.setBeleg2(output1.substring(4295, 4307).trim());
		customer.setHerkunft2(output1.substring(4307, 4317).trim());
		customer.setDag_Fzg2(output1.substring(4317, 4318).trim().equalsIgnoreCase("1")?true:false);
		customer.setAbgabedatumDD2(output1.substring(4318, 4320).trim());
		customer.setAbgabedatumMM2(output1.substring(4320, 4322).trim());
		customer.setAbgabedatumYY2(output1.substring(4322, 4326).trim());
		customer.setAngekreuzt2(output1.substring(4326, 4327).trim().equalsIgnoreCase("J")?true:false);
		customer.setNichtAngekreuzt2(output1.substring(4327, 4328).trim().equalsIgnoreCase("N")?false:false);
		customer.setAblaufdatumDD2(output1.substring(4328, 4330).trim());
		customer.setAblaufdatumMM2(output1.substring(4330, 4332).trim());
		customer.setAblaufdatumYY2(output1.substring(4332, 4336).trim());
		//		customer.setDialogverbot2(output1.substring(4336, 4337).trim().equalsIgnoreCase("J")?true:false);
		//		customer.setTelefonYes2(output1.substring(4337, 4338).trim().equalsIgnoreCase("J")?true:false);
		//		customer.setTelefonNo2(output1.substring(4338, 4339).trim().equalsIgnoreCase("N")?false:false);
		//		customer.setKeineService_CSI_Befragung1(output1.substring(4339, 4340).trim().equalsIgnoreCase("J")?true:false);
		//		customer.setFaxYes2(output1.substring(4340, 4341).trim().equalsIgnoreCase("J")?true:false);
		//		customer.setFaxNo2(output1.substring(4341, 4342).trim().equalsIgnoreCase("N")?false:false);
		//		customer.setSmsYes2(output1.substring(4342, 4343).trim().equalsIgnoreCase("J")?true:false);
		//		customer.setSmsNo2(output1.substring(4343, 4344).trim().equalsIgnoreCase("N")?true:false);
		//		customer.setEmailYes2(output1.substring(4344, 4345).trim().equalsIgnoreCase("J")?true:false);
		//		customer.setEmailNo2(output1.substring(4345, 4346).trim().equalsIgnoreCase("N")?true:false);
		//		customer.setBeleg2(output1.substring(4346, 4358).trim());
		//		customer.setHerkunft2(output1.substring(4358, 4368).trim());
		//		customer.setDag_Fzg2(output1.substring(4368, 4369).trim().equalsIgnoreCase("1")?true:false);
		//		customer.setAbgabedatumDD2(output1.substring(4369, 4371).trim());
		//		customer.setAbgabedatumMM2(output1.substring(4371, 4373).trim());
		//		customer.setAbgabedatumYY2(output1.substring(4373, 4377).trim());
		//		customer.setAngekreuzt2(output1.substring(4377, 4378).trim().equalsIgnoreCase("J")?true:false);
		//		customer.setNichtAngekreuzt2(output1.substring(4378, 4379).trim().equalsIgnoreCase("N")?false:false);
		//		customer.setAblaufdatumDD2(output1.substring(4379, 4381).trim());
		//		customer.setAblaufdatumMM2(output1.substring(4381, 4383).trim());
		//		customer.setAblaufdatumYY2(output1.substring(4383, 4387).trim());
		//		customer.setKeineDaimlerDSE2(output1.substring(4387, 4388).trim().equalsIgnoreCase("J")?true:false);
		customer.setDruckerId(output1.substring(4388, 4390).trim());

		customer.setScreenFourErrorCode(output1.substring(5116, 5120).trim());


		customer.setTemporarInfozeile(output1.substring(5790, 5828).trim());
		customer.setPermanentInfozeile1(output1.substring(5828, 5866).trim());
		customer.setPermanentInfozeile2(output1.substring(5866, 5904).trim());
		customer.setPermanentInfozeile3(output1.substring(5904, 5942).trim());
		customer.setPermanentInfozeile4(output1.substring(5942, 5980).trim());
		customer.setPermanentInfozeile5(output1.substring(5980, 6018).trim());
		customer.setPermanentInfozeile6(output1.substring(6018, 6056).trim());
		customer.setTemporarInfozeileChb(output1.substring(6134, 6135).trim().equalsIgnoreCase("D")?true:false);
		customer.setPermanentInfozeileChb1(output1.substring(6135, 6136).trim().equalsIgnoreCase("D")?true:false);
		customer.setPermanentInfozeileChb2(output1.substring(6136, 6137).trim().equalsIgnoreCase("D")?true:false);
		customer.setPermanentInfozeileChb3(output1.substring(6137, 6138).trim().equalsIgnoreCase("D")?true:false);
		customer.setPermanentInfozeileChb4(output1.substring(6138, 6139).trim().equalsIgnoreCase("D")?true:false);
		customer.setPermanentInfozeileChb5(output1.substring(6139, 6140).trim().equalsIgnoreCase("D")?true:false);
		customer.setPermanentInfozeileChb6(output1.substring(6140, 6141).trim().equalsIgnoreCase("D")?true:false);

		customer.setScreenFiveErrorCode(output1.substring(6652, 6656).trim());


		customer.setElektronRechnung(output1.substring(3230, 3231).trim());
		customer.setErEmail(output1.substring(3231, 3281).trim());
		customer.setDlCode(output1.substring(3281, 3284).trim());
		customer.setVerCode(output1.substring(3284, 3290).trim());
		customer.setBeZeich(output1.substring(3290, 3340).trim());
		customer.setErDaten(output1.substring(3340, 3341).trim());
		customer.setErSignatur(output1.substring(3341, 3342).trim().equalsIgnoreCase("1")?true:false);
		customer.setErAnlagen(output1.substring(3342, 3343).trim().equalsIgnoreCase("1")?true:false);

		customer.setScreenSixErrorCode(output1.substring(4092, 4096).trim());

		log.info("Create Customer Responce : "+output1);
		log.info("Customer Number : "+ outputList.get(1));

	}


	@Override
	public GlobalSearch getCustomerSearchList(String dataLibrary, String companyId,String agencyId,
			String searchString, String pageSize, String pageNo) {
		log.info("Inside getCustomerSearchList method of CustomerServiceImpl");
		GlobalSearch globalSearchList = new GlobalSearch();
		List<SearchCustomerDTO> searchCustomersDTOList = new ArrayList<>();
		
		//validate the company Id 
		validateCompany(companyId);

		try {			
			String compID =  StringUtils.stripStart(companyId, "0");
			String agencyID = StringUtils.stripStart(agencyId, "0");
			log.info("Company ID  :  {}  and  Agency ID  :  {}", compID, agencyID);

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

			StringBuilder query = new StringBuilder("SELECT KDNR1, ANRE, NAME, NAME2, PLZ, ORT, STR, UNSEL, ( SELECT COUNT(*) FROM  ");
			query.append(dataLibrary).append(".M").append(compID).append("_KDDATK1 WHERE ( UPPER(NAME) LIKE UPPER('%").append(searchString).append("%') OR UPPER(KDNR1) LIKE UPPER('%").append(searchString).append("%'))"); 
			query.append(" AND NOT LKZ='L' AND KDNR2 = '' AND FNR = '' ) AS ROWNUMER FROM  ");
			query.append(dataLibrary).append(".M").append(compID).append("_KDDATK1 WHERE ( UPPER(NAME) LIKE UPPER('%").append(searchString).append("%') OR UPPER(KDNR1) LIKE UPPER('%").append(searchString).append("%'))");
			query.append(" AND NOT LKZ='L' AND  KDNR2 = '' AND FNR = '' ORDER BY UPPER(NAME) ASC OFFSET ");
			query.append(String.valueOf(nextRows)).append(" ROWS FETCH NEXT ").append(pageSize).append(" ROWS ONLY ");
			//log.info("Search Query :" + query.toString());

			List<SearchCustomer> searchCustomersList = dbServiceRepository.getResultUsingQuery(SearchCustomer.class, query.toString(),true);

			if (searchCustomersList != null && !searchCustomersList.isEmpty()) {
				for (SearchCustomer cust : searchCustomersList) {
					SearchCustomerDTO customerDTO =  new SearchCustomerDTO();

					customerDTO.setKundenNummer(cust.getKundenNummer());
					customerDTO.setAnrede(cust.getAnrede());
					customerDTO.setOrt(cust.getOrt());
					customerDTO.setPlz(cust.getPlz());
					customerDTO.setStrasse(cust.getStrasse());
					//customerDTO.setName2(cust.getName2());
					customerDTO.setName(cust.getName());
					customerDTO.setPrivatKunde(cust.getPrivatKunde().equalsIgnoreCase("1")?true:false);

					if(cust.getPrivatKunde().equalsIgnoreCase("1") && cust.getName().contains("*")){
						String firstListName[] = cust.getName().split("\\*", 2);
						customerDTO.setName(firstListName[1].trim()+","+firstListName[0].trim());
					}

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
	@Cacheable(value = "akquisitionskzCache", sync = true)
	public List<DropdownObject> getAkquisitionskz(String dataLibrary, String companyid, String agencyId) {
		log.info("Inside getAkquisitionskz method of CustomerServiceImpl");
		List<DropdownObject> acquisitionCodeList = new ArrayList<>();

		//validate the company Id 
		validateCompany(companyid);
		
		String compID =  StringUtils.stripStart(companyid, "0");
		String agencyID = StringUtils.stripStart(agencyId, "0");
		log.info("Company ID :  {} And Agency ID : {}", compID, agencyID);	


		try {
			StringBuilder query = new StringBuilder("SELECT KEYFLD, DATAFLD FROM ");
			query.append(dataLibrary).append(".M").append(compID).append("_KOND WHERE KEYFLD LIKE '3040%'");

			Map<String, String> acquisitionCodeMap = dbServiceRepository.getResultUsingCobolQuery(query.toString());

			if(acquisitionCodeMap != null && !acquisitionCodeMap.isEmpty()) {
				for(Map.Entry<String, String> acquisitionCode : acquisitionCodeMap.entrySet()) {

					DropdownObject dropdownObject = new DropdownObject();
					dropdownObject.setKey(acquisitionCode.getKey().substring(acquisitionCode.getKey().length()-2));
					dropdownObject.setValue(acquisitionCode.getValue());

					acquisitionCodeList.add(dropdownObject);
				}
				//sort the List by keys
				Collections.sort(acquisitionCodeList, Comparator.comparing(DropdownObject::getKey));
			}else{
				log.debug("Acquisition Code List is empty");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Akquisitionskz"));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Akquisitionskz"));
				throw exception;
			}
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Akquisitionskz"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Akquisitionskz"), exception);
			throw exception;
		}
		return acquisitionCodeList;
	}


	@Override
	@Cacheable(value = "branchenSchlusselCache", sync = true)
	public List<DropdownObject> getBranchenSchlussel(String dataLibrary, String companyid, String agencyId) {
		log.info("Inside getBranchenSchlussel method of CustomerServiceImpl");
		List<DropdownObject> industrykeyList = new ArrayList<>();

		//validate the company Id 
		validateCompany(companyid);
		
		String compID =  StringUtils.stripStart(companyid, "0");
		String agencyID = StringUtils.stripStart(agencyId, "0");
		log.info("Company ID :  {}  And  Agency ID : {}", compID, agencyID);

		try {
			StringBuilder query = new StringBuilder("SELECT KEYFLD, DATAFLD FROM ");
			query.append(dataLibrary).append(".M").append(compID).append("_KOND WHERE KEYFLD LIKE '3002%'");

			Map<String, String> industrykeyMap = dbServiceRepository.getResultUsingCobolQuery(query.toString());

			if(industrykeyMap != null && !industrykeyMap.isEmpty()) {
				for(Map.Entry<String, String> acquisitionCode : industrykeyMap.entrySet()) {

					DropdownObject dropdownObject = new DropdownObject();
					dropdownObject.setKey(acquisitionCode.getKey().substring(acquisitionCode.getKey().length()-4));
					dropdownObject.setValue(acquisitionCode.getValue());

					industrykeyList.add(dropdownObject);
				}
				//sort the List by keys
				Collections.sort(industrykeyList, Comparator.comparing(DropdownObject::getKey));
			}else{
				log.debug("Industry key List is empty");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Branchenschlussel"));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Branchenschlussel"));
				throw exception;
			}
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Branchenschlussel"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Branchenschlussel"), exception);
			throw exception;
		}
		return industrykeyList;
	}


	@Override
	@Cacheable(value = "anredeSchlusselCache", sync = true)
	public List<DropdownObject> getAnredeSchlussel(String dataLibrary, String companyid, String agencyId) {
		log.info("Inside getAnredeSchlüssel method of CustomerServiceImpl");
		List<DropdownObject> salutationKeyList = new ArrayList<>();

		//validate the company Id 
		validateCompany(companyid);
		
		String compID =  StringUtils.stripStart(companyid, "0");
		String agencyID = StringUtils.stripStart(agencyId, "0");
		log.info("Company ID :  {}  And  Agency ID : {}", compID, agencyID);

		try {
			StringBuilder query = new StringBuilder("SELECT KEYFLD, DATAFLD FROM ");
			query.append(dataLibrary).append(".M").append(compID).append("_KOND WHERE KEYFLD LIKE '3004%'");

			Map<String, String> salutationKeyMap = dbServiceRepository.getResultUsingCobolQuery(query.toString());

			if(salutationKeyMap != null && !salutationKeyMap.isEmpty()) {
				for(Map.Entry<String, String> acquisitionCode : salutationKeyMap.entrySet()) {

					DropdownObject dropdownObject = new DropdownObject();
					dropdownObject.setKey(acquisitionCode.getKey().substring(4,6));
					dropdownObject.setValue(acquisitionCode.getValue());

					salutationKeyList.add(dropdownObject);
				}
				//sort the List by keys
				Collections.sort(salutationKeyList, Comparator.comparing(DropdownObject::getKey));
			}else{
				log.debug("Salutation Key List is empty");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Anredeschlussel"));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Anredeschlussel"));
				throw exception;
			}
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Anredeschlussel"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Anredeschlussel"), exception);
			throw exception;
		}
		return salutationKeyList;
	}


	@Override
	public CustomerDetailVO getCustomerDetailsByID(String dataLibrary, String companyId, String agencyId,int id) {
		log.info("Inside getCustomerDetailsByID method of CustomerServiceImpl");

		//validate the company Id 
		validateCompany(companyId);

		String compID =  StringUtils.stripStart(companyId, "0");

		StringBuilder customer_query_1 = new StringBuilder(" SELECT KDNR1, BANR, ANRE, NAME2, NAME, SNAM, BRAN, STR, SSTR, LAND ,PLZ, ORT, POSTF, BRSL1, BRSL2, BRSL3,");
		customer_query_1.append(" UNSEL, KZAQ, KZDV, LIEFNRKD, ANZR, STNR, FZKD, INTKD, KMWSL, KGRU, KLIM, BONIT, ZAHLZIEL, KZSCHL, WSTN , ZA, KRABKZ ,ERECH, EREMAIL,");
		customer_query_1.append(" ERDATEN, ERSIGNATUR, ERANLAGEN,DSEPOST, BTREUSTOP, DSETELEF, DSEHERKUNF , CSISPERRE, DSEFAX, DSESMS, DSEEMAIL, DSEBELEGNR, KZDAGFZG, DSEERSTDAT, DSEABLDAT, NODAGDSE ");
		customer_query_1.append(" FROM ").append(dataLibrary).append(".M").append(compID).append("_KDDATK1 WHERE KDNR2 = '' AND FNR = ''  AND KDNR1 = ").append("'"+id+"'");

		StringBuilder customer_query_2 = new StringBuilder("SELECT PLZ, ORTSTEIL FROM ").append(dataLibrary).append(".M").append(compID).append("_PLZ WHERE  KDNR = ").append("'"+id+"'");;
		StringBuilder customer_query_3 = new StringBuilder("SELECT KDNRECH, USTIDENT, STEUERNR, DRRG, DRAW,INFTEXT, DRKZ FROM ").append(dataLibrary).append(".F").append(compID).append("_KBRIK0X  WHERE KDNUM = ").append("'"+id+"'");

		StringBuilder customer_query_4 = new StringBuilder("SELECT SA, GEBTAG, HPTELNR, TELNR1, MOBTEL, FAXNUM, WEBSID, EMAILA, KZTRANSAG FROM ").append(dataLibrary).append(".M_ANSPRE WHERE KDNR = ").append("'"+id+"'");
		StringBuilder customer_query_5 = new StringBuilder("SELECT SATZART, INFTEXTP, DRKZ FROM ").append(dataLibrary).append(".F").append(compID).append("_KBRIK1X WHERE KDNUM = ").append("'"+id+"'");

		CustomerDetailVO customerDetailVO = new CustomerDetailVO();

		List<CustomerDetail> customersDtlsList = new ArrayList<>();
		List<CustomerLocation> customersLocationList = new ArrayList<>();
		List<CustomerInboxTemporaryLine> customersTempLineList = new ArrayList<>();
		List<CustomerContactPersons> customerContactPersonsList = new ArrayList<>();
		List<CustomerInboxStandardLine> customerInboxLineList = new ArrayList<>();
		List<CustomerContactPersonsDTO> customerContactPersonsDTOList = new ArrayList<>();
		List<CustomerInboxStandardLineDTO> customerInboxLineDTOList = new ArrayList<>();

		//Old Way to get data
		/*customersDtlsList = dbServiceRepository.getResultUsingQuery(CustomerDetail.class, customer_query_1.toString());
		customersLocationList =dbServiceRepository.getResultUsingQuery(CustomerLocation.class, customer_query_2.toString());
		customersTempLineList = dbServiceRepository.getResultUsingQuery(CustomerInboxTemporaryLine.class,customer_query_3.toString());
		customerContactPersonsList= dbServiceRepository.getResultUsingQuery(CustomerContactPersons.class,customer_query_4.toString());
		customerInboxLineList= dbServiceRepository.getResultUsingQuery(CustomerInboxStandardLine.class,customer_query_5.toString());*/


		try {
			List<Callable<List>> callables = Arrays.asList(
					() -> dbServiceRepository.getResultUsingQuery(CustomerDetail.class, customer_query_1.toString(),true),
					() -> dbServiceRepository.getResultUsingQuery(CustomerLocation.class, customer_query_2.toString(),true),
					() -> dbServiceRepository.getResultUsingQuery(CustomerInboxTemporaryLine.class,customer_query_3.toString(),true),
					() -> dbServiceRepository.getResultUsingQuery(CustomerContactPersons.class,customer_query_4.toString(),true),
					() -> dbServiceRepository.getResultUsingQuery(CustomerInboxStandardLine.class,customer_query_5.toString(),true));

			ExecutorService service = Executors.newCachedThreadPool();
			List<Future<List>> resultList = service.invokeAll(callables);
			service.shutdown();

			for (int i = 0; i < resultList.size(); i++) {
				Future<List> future = resultList.get(i);

				for (Object obj : future.get()) {
					if (obj instanceof CustomerDetail) {
						customersDtlsList = future.get();
					}
					if (obj instanceof CustomerLocation) {
						customersLocationList = future.get();
					}
					if (obj instanceof CustomerInboxTemporaryLine) {
						customersTempLineList = future.get();
					}
					if (obj instanceof CustomerContactPersons) {
						customerContactPersonsList = future.get();
					}
					if (obj instanceof CustomerInboxStandardLine) {
						customerInboxLineList = future.get();
					}
				}
			}

			if (customersDtlsList != null && !customersDtlsList.isEmpty() && customersDtlsList.size() == 1) {


				for (CustomerDetail customer_obj : customersDtlsList) {
					setCustomerDetails(customer_obj , customerDetailVO);
				}
			} else {
				if(customersDtlsList.size() > 1){
					log.info("More then one records found for this Id in KDDATK1 table :" + id);
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.MULTIPLE_RECORD_MSG_KEY));
					log.error(messageService.getReadableMessage(ExceptionMessages.MULTIPLE_RECORD_MSG_KEY));
					throw exception;
				}
				log.info("Customer details not found in KDDATK1 table for this Id :" + id);
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "Customer"));
				log.error(messageService.getReadableMessage(ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "Customer"));
				throw exception;
			}

			if (customersLocationList != null && !customersLocationList.isEmpty() && customersLocationList.size() == 1) {

				for (CustomerLocation customerLocation_obj : customersLocationList) {
					setCustomerLocation(customerLocation_obj , customerDetailVO);
				}
			} else {
				if(customersDtlsList.size() > 1){
					log.info("More then one records found for this Id in M1_PLZ table :" + id);
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.MULTIPLE_RECORD_MSG_KEY));
					log.error(messageService.getReadableMessage(ExceptionMessages.MULTIPLE_RECORD_MSG_KEY));
					throw exception;
				}
			}

			if (customersTempLineList != null && !customersTempLineList.isEmpty() && customersLocationList.size() == 1) {

				for (CustomerInboxTemporaryLine customerTempLine_obj : customersTempLineList) {
					setCustomerTemporaryLine(customerTempLine_obj , customerDetailVO);
				}
			} else {
				if(customersDtlsList.size() > 1){
					log.info("More then one records found for this Id in M1_PLZ table :" + id);
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.MULTIPLE_RECORD_MSG_KEY));
					log.error(messageService.getReadableMessage(ExceptionMessages.MULTIPLE_RECORD_MSG_KEY));
					throw exception;
				}
			}


			if (customerContactPersonsList != null && !customerContactPersonsList.isEmpty()) {

				for (CustomerContactPersons customer_contect_obj : customerContactPersonsList) {
					if(customer_contect_obj.getSa().compareTo(new BigDecimal("0"))==0){
						setCustomerContests(customer_contect_obj, customerDetailVO);
					}
				}
				customerDetailVO.setCustContactPersons(customerContactPersonsDTOList);
			} else{
				customerDetailVO.setCustContactPersons(customerContactPersonsDTOList);
			}

			if (customerInboxLineList != null && !customerInboxLineList.isEmpty()) {

				log.info("Start setCustomerInboxLine");
				for (CustomerInboxStandardLine customer_inboxLine_obj : customerInboxLineList) {
					setCustomerInboxLine(customer_inboxLine_obj, customerDetailVO);
				}
				log.info("End setCustomerInboxLine");
			} else{
				customerDetailVO.setCustomerInboxLine(customerInboxLineDTOList);
			}

		}catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Customer"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Customer"), exception);
			throw exception;
		}
		return customerDetailVO;


		/*ExecutorService service =  Executors.newSingleThreadExecutor();
		Future<CustomerDetailVO> future = service.submit(callableObj);
		CustomerDetailVO customerDetailVO1 = null;
		try {
			customerDetailVO1 = future.get();
		} catch (InterruptedException e) {

			e.printStackTrace();
		} catch (ExecutionException e) {

			e.printStackTrace();
		}
		return  customerDetailVO1;*/
	}


	private void setCustomerLocation(CustomerLocation customerLocation_obj, CustomerDetailVO customerVo) {
		log.debug("Start setCustomerLocation");
		customerVo.setPlzPostf(customerLocation_obj.getPlzPostf());
		customerVo.setOrtsteil(customerLocation_obj.getOrtsteil());
		log.debug("End..");
	}

	private void setCustomerTemporaryLine(CustomerInboxTemporaryLine customerTempLine_obj,
			CustomerDetailVO customerVo) {
		log.debug("Start setCustomerTemporaryLine");
		customerVo.setAbweichendeKdNr(customerTempLine_obj.getAbweichendeKdNr());
		customerVo.setUstIDNr(customerTempLine_obj.getUstIDNr()); 
		customerVo.setSteuerNr(customerTempLine_obj.getSteuerNr());  
		customerVo.setDruckRabattgruppe(customerTempLine_obj.getDruckRabattgruppe() != null && customerTempLine_obj.getDruckRabattgruppe().equalsIgnoreCase("1")?true:false);      
		customerVo.setDrucAWVerrechnungssatz(customerTempLine_obj.getDrucAWVerrechnungssatz() != null && customerTempLine_obj.getDrucAWVerrechnungssatz().equalsIgnoreCase("1")?true:false); 
		customerVo.setTemporarInfozeile(customerTempLine_obj.getTemporarInfozeile());
		customerVo.setTemporarInfozeileChb(customerTempLine_obj.getTemporarInfozeileChb() != null && customerTempLine_obj.getTemporarInfozeileChb().equalsIgnoreCase("D")?true:false);
		log.debug("End..");
	}


	private void setCustomerInboxLine(CustomerInboxStandardLine customer_inboxLine_obj, CustomerDetailVO customerVo) {

		if(customer_inboxLine_obj.getPermanentInfoLineNo().compareTo(new BigDecimal("2"))==0){
			customerVo.setPermanentInfozeile1(customer_inboxLine_obj.getPermanentInfozeile());
			customerVo.setPermanentInfozeileChb1(customer_inboxLine_obj.getPermanentInfozeileChb().equalsIgnoreCase("D")?true:false);
		}
		if(customer_inboxLine_obj.getPermanentInfoLineNo().compareTo(new BigDecimal("3"))==0){
			customerVo.setPermanentInfozeile2(customer_inboxLine_obj.getPermanentInfozeile());
			customerVo.setPermanentInfozeileChb2(customer_inboxLine_obj.getPermanentInfozeileChb().equalsIgnoreCase("D")?true:false);
		}
		if(customer_inboxLine_obj.getPermanentInfoLineNo().compareTo(new BigDecimal("4"))==0){
			customerVo.setPermanentInfozeile3(customer_inboxLine_obj.getPermanentInfozeile());
			customerVo.setPermanentInfozeileChb3(customer_inboxLine_obj.getPermanentInfozeileChb().equalsIgnoreCase("D")?true:false);
		}
		if(customer_inboxLine_obj.getPermanentInfoLineNo().compareTo(new BigDecimal("5"))==0){
			customerVo.setPermanentInfozeile4(customer_inboxLine_obj.getPermanentInfozeile());
			customerVo.setPermanentInfozeileChb4(customer_inboxLine_obj.getPermanentInfozeileChb().equalsIgnoreCase("D")?true:false);
		}
		if(customer_inboxLine_obj.getPermanentInfoLineNo().compareTo(new BigDecimal("6"))==0){
			customerVo.setPermanentInfozeile5(customer_inboxLine_obj.getPermanentInfozeile());
			customerVo.setPermanentInfozeileChb5(customer_inboxLine_obj.getPermanentInfozeileChb().equalsIgnoreCase("D")?true:false);
		}
		if(customer_inboxLine_obj.getPermanentInfoLineNo().compareTo(new BigDecimal("7"))==0){
			customerVo.setPermanentInfozeile6(customer_inboxLine_obj.getPermanentInfozeile());
			customerVo.setPermanentInfozeileChb6(customer_inboxLine_obj.getPermanentInfozeileChb().equalsIgnoreCase("D")?true:false);
		}

	}


	private void setCustomerContests(CustomerContactPersons customer_contect_obj, CustomerDetailVO customerVo) {

		log.debug("Start setCustomerContests");

		String date="";
		String month="";
		String year="";
		if(customer_contect_obj.getGeburtstag() != null && !customer_contect_obj.getGeburtstag().isEmpty() && customer_contect_obj.getGeburtstag().trim().length() ==8){
			date = customer_contect_obj.getGeburtstag().substring(0,2);
			month = customer_contect_obj.getGeburtstag().substring(2,4);
			year = customer_contect_obj.getGeburtstag().substring(4,8);
		}
		customerVo.setGeburtstagDate(date);         
		customerVo.setGeburtstagMonth(month);        
		customerVo.setGeburtstagYear(year);  

		customerVo.setHaupttelefonNummer(customer_contect_obj.getHaupttelefonNummer());
		//customerVo.setTelefonNR(customer_contect_obj.getT);
		customerVo.setMobilfunknummer(customer_contect_obj.getMobilfunknummer());
		customerVo.setFestnetzTelefonnummer(customer_contect_obj.getFestnetzTelefonnummer());
		customerVo.setFaxNummer(customer_contect_obj.getFaxNummer());
		customerVo.setEmailAddress(customer_contect_obj.getEmailAddress());
		customerVo.setWebseite(customer_contect_obj.getWebseite());
		customerVo.setDagDatentransfer(customer_contect_obj.getDagDatentransfer().equalsIgnoreCase("1")?true:false);       
		log.debug("End..");		
	}


	private void setCustomerDetails(CustomerDetail customerDtls , CustomerDetailVO customerVo) {
		log.debug("Start setCustomerDetails");

		String fullName = customerDtls.getName().trim();

		if(customerDtls.getPrivatKunde().equalsIgnoreCase("1") && fullName.contains("*")){
			String name[] = customerDtls.getName().trim().split("\\*");
			customerVo.setName(name[1]);
			customerVo.setVoname( name[0]);
		}else{
			customerVo.setName(fullName);
		}
		customerVo.setAdditionalNameInfo(customerDtls.getAdditionalNameInfo().trim());
		customerVo.setAnredeSchlussel(customerDtls.getAnredeSchlussel());
		customerVo.setAnrede(customerDtls.getAnrede());
		//customerVo.setName(lastName);
		customerVo.setSortName(customerDtls.getSortName());
		//customerVo.setVoname(firstName);
		customerVo.setBranchenBez(customerDtls.getBranchenBez());
		customerVo.setStrasse(customerDtls.getStrasse());
		customerVo.setSortStrasse(customerDtls.getSortStrasse());
		customerVo.setLand(customerDtls.getLand());
		customerVo.setPlz(customerDtls.getPlz());
		customerVo.setOrt(customerDtls.getOrt());
		customerVo.setPostfach(String.valueOf(customerDtls.getPostfach()));

		//customerVo Number
		customerVo.setKundenNummer(customerDtls.getKundenNummer());
		customerVo.setBranchenschlPart1(customerDtls.getBranchenschlPart1());      
		customerVo.setBranchenschlPart2(customerDtls.getBranchenschlPart2());      
		customerVo.setBranchenschlPart3(customerDtls.getBranchenschlPart3());      
		customerVo.setPrivatKunde(customerDtls.getPrivatKunde().equalsIgnoreCase("1")?true:false);            
		customerVo.setAkquisitionskz(customerDtls.getAkquisitionskz());         
		/* setDagDatentransfer Move to CustomerContectPerson Object*/
		//customerVo.setDagDatentransfer(customerDtls.getDagDatentransfer().compareTo(new BigDecimal("1"))==0?true:false);       
		customerVo.setLiefNrBKunde(customerDtls.getLiefNrBKunde());           
		customerVo.setAnzahlRechnungsexemplare(String.valueOf(customerDtls.getAnzahlRechnungsexemplare()));
		customerVo.setHandelsregister(customerDtls.getHandelsregister());        
		customerVo.setFahrzeugkunde(customerDtls.getFahrzeugkunde().equalsIgnoreCase("1")?true:false); 
		//customerVo.setBranchenschlPart3();      
		customerVo.setInternerKunde(customerDtls.getInternerKunde().equalsIgnoreCase("1")?true:false);
		customerVo.setMwStKz(String.valueOf(customerDtls.getMwStKz()));                 
		customerVo.setWerkstattNr(customerDtls.getWerkstattNr()); 
		customerVo.setKundengruppe(String.valueOf(customerDtls.getKundengruppe()));           
		customerVo.setKreditlimit(String.valueOf(customerDtls.getKreditlimit()));            
		customerVo.setBonitat(customerDtls.getBonitat());                
		customerVo.setZahlungsziel(String.valueOf(customerDtls.getZahlungsziel()));           
		customerVo.setZuAbschlag(String.valueOf(customerDtls.getZuAbschlag()));             
		customerVo.setSkontoTheke(customerDtls.getSkontoTheke());            
		customerVo.setRabattkz(String.valueOf(customerDtls.getRabattkz()));  
		customerVo.setDialogverbot2(customerDtls.getDialogverbot().equalsIgnoreCase("J")?true:false);
		customerVo.setTelefonYes2(customerDtls.getTelefon().equalsIgnoreCase("J")?true:false);
		customerVo.setTelefonNo2(customerDtls.getTelefon().equalsIgnoreCase("N")?false:false);
		customerVo.setKeineService_CSI_Befragung2(customerDtls.getKeineService_CSI_Befragung().equalsIgnoreCase("J")?true:false);
		customerVo.setFaxYes2(customerDtls.getFax().equalsIgnoreCase("J")?true:false);
		customerVo.setFaxNo2(customerDtls.getFax().equalsIgnoreCase("N")?false:false);
		customerVo.setSmsYes2(customerDtls.getSms().equalsIgnoreCase("J")?true:false);
		customerVo.setSmsNo2(customerDtls.getSms().equalsIgnoreCase("N")?true:false);
		customerVo.setEmailYes2(customerDtls.getEmail().equalsIgnoreCase("J")?true:false);
		customerVo.setEmailNo2(customerDtls.getEmail().equalsIgnoreCase("N")?true:false);
		customerVo.setBeleg2(customerDtls.getBeleg());
		customerVo.setHerkunft2(customerDtls.getHerkunft2()); // Need to check
		customerVo.setDag_Fzg2(customerDtls.getDag_Fzg().compareTo(new BigDecimal("1"))==0?true:false);

		String date=""; 
		String month="";
		String year="";
		String abgabedatum = String.valueOf(customerDtls.getAbgabedatum());
		log.debug("abgabedatum :" + abgabedatum.length());
		abgabedatum = abgabedatum.length() == 7 ? "0"+abgabedatum : abgabedatum;

		if(abgabedatum.length() == 8 ){
			date = abgabedatum.substring(0,2);
			month = abgabedatum.substring(2,4);
			year = abgabedatum.substring(4,8);
		}

		customerVo.setAbgabedatumDD2(date);
		customerVo.setAbgabedatumMM2(month);
		customerVo.setAbgabedatumYY2(year);


		customerVo.setAngekreuzt2(customerDtls.getAngekreuzt().equalsIgnoreCase("N")?true:false); // Need to check
		customerVo.setNichtAngekreuzt2(customerDtls.getAngekreuzt().equalsIgnoreCase("N")?false:false); // need to check
		String ablaufdatum = String.valueOf(customerDtls.getAblaufdatum());

		ablaufdatum = ablaufdatum.length() == 7 ?"0"+ablaufdatum : ablaufdatum;
		if(ablaufdatum.length()==8){
			date = ablaufdatum.substring(0,2);
			month = ablaufdatum.substring(2,4);
			year = ablaufdatum.substring(4,8);
		}

		customerVo.setAblaufdatumDD2(date);
		customerVo.setAblaufdatumMM2(month);
		customerVo.setAblaufdatumYY2(year);

		customerVo.setKeineDaimlerDSE2(customerDtls.getKeineDaimlerDSE().equalsIgnoreCase("J")?true:false);
		//customerVo.setDruckerId(); need to check

		customerVo.setElektronRechnung(customerDtls.getElektronRechnung());
		customerVo.setErEmail(customerDtls.getErEmail());
		/* Not use at present
		 * customerVo.setDlCode("");
		customerVo.setVerCode("");
		customerVo.setBeZeich("");*/
		customerVo.setErDaten(customerDtls.getErDaten());
		customerVo.setErSignatur(customerDtls.getErSignatur().equalsIgnoreCase("1")?true:false);
		customerVo.setErAnlagen(customerDtls.getErAnlagen().equalsIgnoreCase("1")?true:false);
		log.debug("End..");
	}


/*	@Override
	public CustomerOneCheck getCustomerOne(String dataLibrary, String companyId, String agencyId) {
		log.info("Inside getCustomerOne method of CustomerServiceImpl");
		ProgramParameter[] parmList;
		CustomerOneCheck customerOne_obj = new CustomerOneCheck();

		log.info("companyId from OROLLE : " +companyId);

		if (companyId.trim().length() == 3) {
			companyId = companyId.substring(0, 2);
		} else if (companyId.length() == 1) {
			companyId = "01";
		}

		String inputString = StringUtils.rightPad(companyId, 10, " ") 
				+ StringUtils.rightPad("", 10, " ")
				+ StringUtils.rightPad("000", 3, " ") 
				+ StringUtils.rightPad("000", 3, " ")
				+ StringUtils.rightPad("COCHECK", 7, " ") 
				+ StringUtils.rightPad("", 388, " ");

		log.info("CustomerOne Check inputString length : " + inputString.length());
		log.info("CustomerOne Check inputString value :" + inputString);

		try {
			parmList = new ProgramParameter[1];
			parmList[0] = new ProgramParameter(inputString.getBytes(Program_Commands_Constants.IBM_273),inputString.length());
			List<String> outputList = cobolServiceRepository.executeProgram(parmList, 0,Program_Commands_Constants.CUSTOMER_ONE_CHECK_PROGRAM);

			if (outputList != null && !outputList.isEmpty()) {
				if (!outputList.get(0).contains("Error")) {
					String returnCode = outputList.get(0).substring(346, 351);
					if (returnCode.equalsIgnoreCase("00000")) {
						customerOne_obj.setCustomerOne(true);
					} else if (returnCode.equalsIgnoreCase("38B03")) {
						customerOne_obj.setCustomerOne(false);
					}
				} else {
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.CUSTOMER_ONE_FAILED_MSG_KEY));
					log.error(messageService.getReadableMessage(ExceptionMessages.CUSTOMER_ONE_FAILED_MSG_KEY), exception);
					throw exception;
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.CUSTOMER_ONE_FAILED_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.CUSTOMER_ONE_FAILED_MSG_KEY), exception);
			throw exception;
		}

		return customerOne_obj;
	}
*/
	
	@Override
	public CustomerOneCheck getCustomerOne(String dataLibrary, String companyId, String agencyId) {

		log.info("Inside getCustomerOne method of CustomerServiceImpl");
		ProgramParameter[] parmList;
		CustomerOneCheck customerOne_obj = new CustomerOneCheck();

		log.info("companyId from OROLLE : {}", companyId);

		if (companyId.trim().length() == 3) {
			companyId = companyId.substring(0, 2);
		} else if (companyId.length() == 1) {
			companyId = "01";
		}


		try {
			parmList = new ProgramParameter[4];
			parmList[0] = new ProgramParameter(05);
			parmList[1] = new ProgramParameter(100);
			parmList[2] = new ProgramParameter(10);
			parmList[3] = new ProgramParameter(01);
			
			String progRc   = "00000";
			byte[] progRcB  = progRc.getBytes(Program_Commands_Constants.IBM_273);
			parmList[0] = new ProgramParameter(progRcB, 3);

			String progRcTxt = StringUtils.rightPad("", 100, " ");
			byte[] progRcTxtB   = progRcTxt.getBytes(Program_Commands_Constants.IBM_273);
			parmList[1] = new ProgramParameter(progRcTxtB, 5);

			String progLocationId   = StringUtils.rightPad(companyId, 10, " ");
			byte[] progLocationIdB  = progLocationId.getBytes(Program_Commands_Constants.IBM_273);
			parmList[2]           = new ProgramParameter(progLocationIdB, 3);
			
			String progEditable  = "0";
			byte[] progEditableB  = progEditable.getBytes(Program_Commands_Constants.IBM_273);
			parmList[3]         = new ProgramParameter(progEditableB, 3);
			
			
			List<String> outputList = cobolServiceRepository.executeProgram(parmList, 0, Program_Commands_Constants.NEW_CUSTOMER_ONE_CHECK_PROGRAM);

			if (outputList != null && !outputList.isEmpty()) {
				if (!outputList.get(0).contains("Error")) {
					String returnCode = outputList.get(0);
					
					log.info("returnCode : {}", returnCode);
					
					if (returnCode.equalsIgnoreCase("00000")) {
						customerOne_obj.setCustomerOne(true);
					} else if (returnCode.equalsIgnoreCase("38B03")) {
						customerOne_obj.setCustomerOne(false);
					}
				} else {
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.CUSTOMER_ONE_FAILED_MSG_KEY));
					log.error(messageService.getReadableMessage(ExceptionMessages.CUSTOMER_ONE_FAILED_MSG_KEY), exception);
					throw exception;
				}
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.CUSTOMER_ONE_FAILED_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.CUSTOMER_ONE_FAILED_MSG_KEY), exception);
			throw exception;
		}

		return customerOne_obj;
	}
	
	@Override
	public AuthorityCheck updateEditFlag(String dataLibrary, String companyId,String agencyId, String affects, String flag,
			String id) {
		log.info("Inside updateEditFlag method of CustomerServiceImpl");

		//validate the company Id 
		validateCompany(companyId);

		String compID = StringUtils.stripStart(companyId, "0");
		String agencyID = StringUtils.stripStart(agencyId, "0");
		log.info("Company ID  xx: {} and Agency ID yy: {} ", compID, agencyID);

		AuthorityCheck authorituUpdateCheck = new AuthorityCheck();
		boolean updateCheck = false;

		try{
			if(affects.equalsIgnoreCase("customer")){

				StringBuilder customer_query = new StringBuilder("UPDATE ");
				customer_query.append(dataLibrary).append(".M").append(compID).append("_KDDATK1");

				switch (flag) {
				case "in_use":
					customer_query.append(" SET LKZ = ? WHERE KDNR2 = '' AND FNR = '' AND KDNR1 = ").append("'"+id+"'");
					updateCheck = dbServiceRepository.updateResultUsingQuery(customer_query.toString().replace("?", "'S'"));
					break;
				case "clear":
					customer_query.append(" SET LKZ = ? WHERE KDNR2 = '' AND FNR = '' AND KDNR1 = ").append("'"+id+"'");
					updateCheck = dbServiceRepository.updateResultUsingQuery(customer_query.toString().replace("?", "''"));
					break;
				case "deleted":
					customer_query.append(" SET LKZ = ? WHERE KDNR2 = '' AND FNR = '' AND KDNR1 = ").append("'"+id+"'");
					updateCheck = dbServiceRepository.updateResultUsingQuery(customer_query.toString().replace("?", "'L'"));
					break;
				case "moved":
					customer_query.append(" SET LKZ = ? WHERE KDNR2 = '' AND FNR = '' AND KDNR1 = ").append("'"+id+"'");
					updateCheck = dbServiceRepository.updateResultUsingQuery(customer_query.toString().replace("?", "'G'"));
					break;
				case "enabled":
					customer_query.append(" SET KZST = ? WHERE KDNR2 = '' AND FNR = '' AND KDNR1 = ").append("'"+id+"'");
					updateCheck = dbServiceRepository.updateResultUsingQuery(customer_query.toString().replace("?", "''"));
					break;
				case "disabled":
					customer_query.append(" SET KZST = ? WHERE KDNR2 = '' AND FNR = '' AND KDNR1 = ").append("'"+id+"'");
					updateCheck = dbServiceRepository.updateResultUsingQuery(customer_query.toString().replace("?", "'S'"));
					break;
				default:
					log.info("Flag value not match");
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.CUSTOMER_EDIT_FLAG_INVALID_MSG_KEY));
					log.error(messageService.getReadableMessage(ExceptionMessages.CUSTOMER_EDIT_FLAG_INVALID_MSG_KEY));
					throw exception;
				}
			}else if(affects.equalsIgnoreCase("vehicle")){

				StringBuilder vehicle_query = new StringBuilder("UPDATE ");
				vehicle_query.append(dataLibrary).append(".M").append(compID).append("_KFZF1XX");

				switch (flag) {
				case "in_use":
					vehicle_query.append(" SET LKZ1 = ? WHERE FKENNZ7 = ").append("'"+id+"'");
					updateCheck = dbServiceRepository.updateResultUsingQuery(vehicle_query.toString().replace("?", "'S'"));
					break;
				case "clear":
					vehicle_query.append(" SET LKZ1 = ? WHERE FKENNZ7 = ").append("'"+id+"'");
					updateCheck = dbServiceRepository.updateResultUsingQuery(vehicle_query.toString().replace("?", "''"));
					break;
				case "deleted":
					vehicle_query.append(" SET LKZ1 = ? WHERE FKENNZ7 = ").append("'"+id+"'");
					updateCheck = dbServiceRepository.updateResultUsingQuery(vehicle_query.toString().replace("?", "'L'"));
					break;
				case "moved":
					vehicle_query.append(" SET LKZ1 = ? WHERE FKENNZ7 = ").append("'"+id+"'");
					updateCheck = dbServiceRepository.updateResultUsingQuery(vehicle_query.toString().replace("?", "'G'"));
					break;
				case "enabled":
					vehicle_query.append("  SET KZST = ? WHERE FKENNZ7 = ").append("'"+id+"'");
					updateCheck = dbServiceRepository.updateResultUsingQuery(vehicle_query.toString().replace("?", "''"));
					break;
				case "disabled":
					vehicle_query.append("  SET KZST = ? WHERE FKENNZ7 = ").append("'"+id+"'");
					updateCheck = dbServiceRepository.updateResultUsingQuery(vehicle_query.toString().replace("?", "'S'"));
					break;
				default:
					log.info("Flag value not match");
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.CUSTOMER_EDIT_FLAG_INVALID_MSG_KEY));
					log.error(messageService.getReadableMessage(ExceptionMessages.CUSTOMER_EDIT_FLAG_INVALID_MSG_KEY));
					throw exception;
				}
			}else{
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.CUSTOMER_AFFECT_FLAG_INVALID_MSG_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.CUSTOMER_AFFECT_FLAG_INVALID_MSG_KEY));
				throw exception;
			}
		}catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.CUSTOMER_EDIT_FLAG_FAILED_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.CUSTOMER_EDIT_FLAG_FAILED_MSG_KEY), exception);
			throw exception;
		}

		authorituUpdateCheck.setOpertionInUse(updateCheck);

		return authorituUpdateCheck;
	}


	@Override
	public AuthorityCheck getEditFlagValueById(String dataLibrary, String companyId,String agencyId, String affects ,String id) {

		log.info("Inside getEditFlagValueById method of CustomerServiceImpl");

		//validate the company Id 
		validateCompany(companyId);

		String compID = StringUtils.stripStart(companyId, "0");
		String agencyID = StringUtils.stripStart(agencyId, "0");
		log.info("Company ID  xx: {} and Agency ID yy: {}", compID, agencyID);

		List<?> authorityCheckList = new ArrayList<>();
		AuthorityCheck authorityCheck = new AuthorityCheck();

		try{
			if(affects.equalsIgnoreCase("customer")) {

				StringBuilder customer_query = new StringBuilder("SELECT LKZ, KZST FROM ");
				customer_query.append(dataLibrary).append(".M").append(compID).append("_KDDATK1  WHERE KDNR2 = '' AND FNR = '' AND KDNR1 = ").append("'"+id+"'");
				log.info("Select Query for customer :" + customer_query.toString());

				authorityCheckList = dbServiceRepository.getResultUsingQuery(CustomerUpdateAuthority.class, customer_query.toString(),true);
			}
			else if (affects.equalsIgnoreCase("vehicle")){

				StringBuilder vehicle_query = new StringBuilder("SELECT LKZ1 , KZST FROM ");
				vehicle_query.append(dataLibrary).append(".M").append(compID).append("_KFZF1XX  WHERE FKENNZ7 = ").append("'"+id+"'");
				log.info("Select Query for vehicle :" + vehicle_query.toString());

				authorityCheckList = dbServiceRepository.getResultUsingQuery(VehicleAuthorityCheck.class, vehicle_query.toString(),true);	
			}
			else{
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.CUSTOMER_EDIT_FLAG_FAILED_MSG_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.CUSTOMER_EDIT_FLAG_FAILED_MSG_KEY), exception);
				throw exception;
			}

			if (authorityCheckList != null && !authorityCheckList.isEmpty()) {
				for (Object object : authorityCheckList) {

					if (object instanceof CustomerUpdateAuthority) {
						log.info("instanceof CustomerUpdateAuthority" );
						CustomerUpdateAuthority customerUpdateAuthority = (CustomerUpdateAuthority) object ;

						if (customerUpdateAuthority.getKzLoeschung().equalsIgnoreCase("S")
								|| customerUpdateAuthority.getKzLoeschung().equalsIgnoreCase("L")
								|| customerUpdateAuthority.getKzLoeschung().equalsIgnoreCase("G") 
								|| customerUpdateAuthority.getKzStilllegung().equalsIgnoreCase("S")){
							// || customerUpdateAuthority.getKzStilllegung().equalsIgnoreCase("")){
							authorityCheck.setOpertionInUse(false);
						} else {
							authorityCheck.setOpertionInUse(true);
						}
					} 
					else if (object instanceof VehicleAuthorityCheck) {
						log.info("instanceof VehicleAuthorityCheck" );
						VehicleAuthorityCheck vehicleAuthorityCheck = (VehicleAuthorityCheck) object ;

						if (vehicleAuthorityCheck.getKzLoeschung().equalsIgnoreCase("S")
								|| vehicleAuthorityCheck.getKzLoeschung().equalsIgnoreCase("L")
								|| vehicleAuthorityCheck.getKzLoeschung().equalsIgnoreCase("G") 
								|| vehicleAuthorityCheck.getKzStilllegung().equalsIgnoreCase("S")){
							//  || vehicleAuthorityCheck.getKzStilllegung().equalsIgnoreCase("")){

							authorityCheck.setOpertionInUse(false);
						} 
						else {
							authorityCheck.setOpertionInUse(true);
						}
					}
				}

			} else {
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.AUTHORITY_FLAG_FAIL_MSG_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.AUTHORITY_FLAG_FAIL_MSG_KEY), exception);
				throw exception;
			}
		}catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.AUTHORITY_FLAG_FAIL_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.AUTHORITY_FLAG_FAIL_MSG_KEY), exception);
			throw exception;
		}

		return authorityCheck;
	}


	@Override
	public CustomerDefaultDataVO getCustomerDefaultData(String schema, String companyId, String agencyId,
			String customerGroup) {
		log.info("Inside getCustomerDefaultData method of CustomerServiceImpl");

		//validate the company Id 
		validateCompany(companyId);
		
		String custGroup = "00";
		if(customerGroup!=null && !customerGroup.isEmpty()){
			custGroup = customerGroup;
		}
		log.info("Customer group value is :"+custGroup);
		StringBuilder query = new StringBuilder("SELECT KUN_KLIMIT,KUN_BONIT,KUN_ZAZIEL,KUN_ZUABSL,KUN_SKONTE,KUN_RABFLG ,KUN_FIRMA, KUN_KDGRP ");
		query.append(" FROM ").append(schema).append(".PKF_KUNDEF");
		query.append(" WHERE KUN_FIRMA = ").append("'"+companyId.trim()+"'").append(" AND KUN_KDGRP = ").append("'"+custGroup+"'");

		CustomerDefaultDataVO customerDefaultDataVO = new CustomerDefaultDataVO();
		List<CustomerDefaultData> customerDefaultDataList = new ArrayList<>();

		try {
			customerDefaultDataList = dbServiceRepository.getResultUsingQuery(CustomerDefaultData.class, query.toString(),true);

			if (customerDefaultDataList != null && !customerDefaultDataList.isEmpty()) {

				for (CustomerDefaultData customer_obj : customerDefaultDataList) {
					customerDefaultDataVO.setKundengruppe(new BigDecimal(customer_obj.getKundengruppe()));
					customerDefaultDataVO.setKreditlimit(customer_obj.getKreditlimit());
					customerDefaultDataVO.setBonitat(customer_obj.getBonitat());
					customerDefaultDataVO.setZahlungsziel(customer_obj.getZahlungsziel());
					customerDefaultDataVO.setZuAbschlag(customer_obj.getZuAbschlag());
					customerDefaultDataVO.setSkontoTheke(String.valueOf(customer_obj.getSkontoTheke()));
					customerDefaultDataVO.setRabattkz(customer_obj.getRabattkz());
				}
			} else {
				log.info("Customer default data not found in PKF_KUNDEF table so default data will return");
				customerDefaultDataVO.setKundengruppe(new BigDecimal("0"));
				customerDefaultDataVO.setKreditlimit(new BigDecimal("0"));
				customerDefaultDataVO.setBonitat("B");
				customerDefaultDataVO.setZahlungsziel(new BigDecimal("0"));
				customerDefaultDataVO.setZuAbschlag(new BigDecimal("0"));
				customerDefaultDataVO.setSkontoTheke("0");
				customerDefaultDataVO.setRabattkz(new BigDecimal("0"));
			}

		}catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.DEFAULT_DATA_FAIL_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.DEFAULT_DATA_FAIL_MSG_KEY), exception);
			throw exception;
		}
		return customerDefaultDataVO;
	}

	@Override
	public List<DropdownObject> getMehrwertsteuerList() {
		log.info("Inside getMehrwertsteuerList method of CustomerServiceImpl");

		List<DropdownObject> mehrwertsteuerList = new ArrayList<>();
		mehrwertsteuerList = stubServiceRepository.getDDLValuesFromStub(StubServiceRepository.mehrwertsteuerMap);
		return mehrwertsteuerList;
	}


	@Override
	public List<DropdownObject> getSkontoList() {
		log.info("Inside getSkontoList method of CustomerServiceImpl");

		List<DropdownObject> skontoList = new ArrayList<>();
		skontoList = stubServiceRepository.getDDLValuesFromStub(StubServiceRepository.skontoMap);
		return skontoList;
	}


	@Override
	public List<DropdownObject> getZuAbschlagList() {
		log.info("Inside getZuAbschlagList method of CustomerServiceImpl");

		List<DropdownObject> zuAbschlagList = new ArrayList<>();
		zuAbschlagList = stubServiceRepository.getDDLValuesFromStub(StubServiceRepository.zuAbschlagMap);
		return zuAbschlagList;
	}


	@Override
	public List<DropdownObject> getRabattkzList() {
		log.info("Inside getRabattkzList method of CustomerServiceImpl");

		List<DropdownObject> rabattkzList = new ArrayList<>();
		rabattkzList = stubServiceRepository.getDDLValuesFromStub(StubServiceRepository.rabattkzMap);
		return rabattkzList;
	}


	@Override
	public List<DropdownObject> getElektRechnungList() {
		log.info("Inside getElektRechnungList method of CustomerServiceImpl");

		List<DropdownObject> elektRechnungList = new ArrayList<>();
		elektRechnungList = stubServiceRepository.getDDLValuesFromStub(StubServiceRepository.elektRechnungMap);
		return elektRechnungList;
	}


	@Override
	public List<DropdownObject> getXMLDatenList() {
		log.info("Inside getXMLDatenList method of CustomerServiceImpl");

		List<DropdownObject> xmlDatenList = new ArrayList<>();
		xmlDatenList = stubServiceRepository.getDDLValuesFromStub(StubServiceRepository.xmlDatenMap);
		return xmlDatenList;
	}


	@Override
	public List<DropdownObject> getHerkunftList() {
		log.info("Inside getHerkunftList method of CustomerServiceImpl");

		List<DropdownObject> herkunftList = new ArrayList<>();
		herkunftList = stubServiceRepository.getDDLValuesFromStub(StubServiceRepository.herkunftMap);
		return herkunftList;
	}

	@Override
	public List<DropdownObject> getAnredeCodeList() {
		log.info("Inside getAnredeCodeList method of CustomerServiceImpl");

		List<DropdownObject> anredeCodeList = new ArrayList<>();
		anredeCodeList = stubServiceRepository.getDDLValuesFromStub(StubServiceRepository.anredeMap);
		return anredeCodeList;
	}


	@Override
	@Cacheable(value = "landDetailsListCache", sync = true)
	public List<DropdownObject> getLandList(String schema, String companyId, String agencyId) {

		log.info("Inside getLandList method of CustomerServiceImpl");
		List<DropdownObject> landDetailsList = new ArrayList<>();

		try {
			StringBuilder query = new StringBuilder("SELECT LAN_LKZAPL AS KEYFLD, LAN_BEZDE AS DATAFLD FROM  ");
			query.append(schema).append(".PSS_LANDKZ WHERE LAN_LKZAPL NOT LIKE ' %' ORDER BY LAN_BEZDE ");

			Map<String, String> landDetailsMap = dbServiceRepository.getResultUsingCobolQuery(query.toString());

			if(landDetailsMap != null && !landDetailsMap.isEmpty()) {
				for(Map.Entry<String, String> landDetails : landDetailsMap.entrySet()) {

					DropdownObject dropdownObject = new DropdownObject();
					dropdownObject.setKey(landDetails.getKey());
					dropdownObject.setValue(landDetails.getValue());

					landDetailsList.add(dropdownObject);
				}
			}else{
				log.debug("LAND List is empty");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "LAND"));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "LAND"));
				throw exception;
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "LAND"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "LAND"), exception);
			throw exception;
		}

		return landDetailsList;
	}


	@Override
	public List<DropdownObject> getBonitateList(String schema, String companyId, String agencyId) {
		log.info("Inside getBonitateList method of CustomerServiceImpl");

		//validate the company Id 
		validateCompany(companyId);

		List<DropdownObject> bonitateList = new ArrayList<>();

		try {
			String compID =  StringUtils.stripStart(companyId, "0");
			log.info("Company ID  xx  :" + compID);

			StringBuilder query = new StringBuilder("SELECT BON_BONIT AS KEYFLD, BON_BEZLG AS DATAFLD from  ");
			query.append(schema).append(".PKF_BONIT WHERE BON_FIRMA= ").append(compID).append(" ORDER BY BON_BEZLG");

			Map<String, String> bonitateDetailsMap = dbServiceRepository.getResultUsingCobolQuery(query.toString());

			if(bonitateDetailsMap != null && !bonitateDetailsMap.isEmpty()) {
				for(Map.Entry<String, String> bonitateDetails : bonitateDetailsMap.entrySet()) {

					DropdownObject dropdownObject = new DropdownObject();
					dropdownObject.setKey(bonitateDetails.getKey());
					dropdownObject.setValue(bonitateDetails.getValue());

					bonitateList.add(dropdownObject);
				}
			}else{
				log.info("bonitate List is empty");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Bonitate"));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Bonitate"));
				throw exception;
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.debug("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Bonitate"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Bonitate"), exception);
			throw exception;
		}

		return bonitateList;
	}


	@Override
	@Cacheable(value = "druckerCache", sync = true)
	public List<DropdownObject> getDruckerList(String dataLibrary, String companyId, String agencyId) {
		log.info("Inside getDruckerList method of CustomerServiceImpl");
		List<DropdownObject> druckerList = new ArrayList<>();

		try {
			StringBuilder query = new StringBuilder("SELECT substr(KEYFLD, 11, 2) AS KEYFLD, substr(DATAFLD, 1, 60) AS DATAFLD FROM  ");
			query.append(dataLibrary).append(".REFERENZ where KEYFLD like '0000019911%' order by KEYFLD");

			Map<String, String> druckerDetailsMap = dbServiceRepository.getResultUsingCobolQuery(query.toString());

			if(druckerDetailsMap != null && !druckerDetailsMap.isEmpty()) {

				for(Map.Entry<String, String> druckerDetails : druckerDetailsMap.entrySet()) {

					DropdownObject dropdownObject = new DropdownObject();
					dropdownObject.setKey(druckerDetails.getKey());
					dropdownObject.setValue(druckerDetails.getValue());

					druckerList.add(dropdownObject);
				}
				//sort the List by values
				//Collections.sort(druckerList, Comparator.comparing(DropdownObject::getValue));
			}else{
				log.debug("Drucker List is empty");
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
						ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Drucker"));
				log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_EMPTY_MSG_KEY, "Drucker"));
				throw exception;
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Drucker"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Drucker"), exception);
			throw exception;
		}

		return druckerList;
	}

	@Override
	public GlobalSearch getCustomerVehicles(String dataLibrary, String companyId, String agencyId,
			String customerId) {

		log.info("Inside getCustomerVehicles method of CustomerServiceImpl");
		GlobalSearch globalSearchList = new GlobalSearch();
		List<SearchVehicleDTO> searchVehicleDTOList = new ArrayList<>();
		try {

			//validate the company Id 
			validateCompany(companyId);

			String compID =  StringUtils.stripStart(companyId, "0");
			String agencyID = StringUtils.stripStart(agencyId, "0");
			log.debug("Company ID  xx:  {}  and  Agency ID yy:  {}", compID, agencyID);

			StringBuilder query = new StringBuilder("SELECT FKENNZ7, WHC, BAUM, ENDN, FGP, DTEZU, TYP, KDNR1 FROM ");
			query.append(dataLibrary).append(".M").append(compID).append("_KFZF1XX WHERE VZSL1=7 AND KDNR1 = ").append("'"+customerId+"'");

			List<SearchVehicle> searchVehiclesList = dbServiceRepository.getResultUsingQuery(SearchVehicle.class, query.toString(),true);

			if (searchVehiclesList != null && !searchVehiclesList.isEmpty()) {
				//set all vehicle details
				searchVehicleDTOList = convertVehicleEntityToDTO(searchVehiclesList, searchVehicleDTOList);
				globalSearchList.setSearchDetailsList(searchVehicleDTOList);
			} else {
				globalSearchList.setSearchDetailsList(searchVehicleDTOList);
			}
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.SEARCH_FAILED_MSG_KEY, "Customer's vehicle"));
			log.error(messageService.getReadableMessage(ExceptionMessages.SEARCH_FAILED_MSG_KEY, "Customer's vehicle"), exception);
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
					StringUtils.rightPad(vehicle.getWorldManufacturerCode(), 3, " "),
					StringUtils.rightPad(vehicle.getModelInformation(), 8, " "),
					StringUtils.rightPad(vehicle.getProductionIndicator(), 6, " "),
					StringUtils.rightPad(vehicle.getChasisNumberDigit(), 1, " ")));

			vehicleDTO.setVehicleType(vehicle.getVehicleType());
			vehicleDTO.setLicencePlate(vehicle.getLicencePlate());

			searchVehicleDTOList.add(vehicleDTO);
		}

		return searchVehicleDTOList;
	}

}