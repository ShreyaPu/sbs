package com.alphax.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alphax.vo.mb.Customer;
import com.alphax.vo.mb.DropdownObject;

import lombok.extern.slf4j.Slf4j;

@Service("stubServiceRepository")
@Slf4j
public class StubServiceRepository {
	
	public static Map<String, String> mehrwertsteuerMap;
	public static Map<String, String> skontoMap;
	public static Map<String, String> zuAbschlagMap;
	public static Map<String, String> rabattkzMap;
	public static Map<String, String> elektRechnungMap;
	public static Map<String, String> xmlDatenMap;
	public static Map<String, String> herkunftMap;
	public static Map<String, String> anredeMap;
	public static Map<String, String> LaufleistungMap;
	public static Map<String, String> wartGruppeMap;
	public static Map<String, String> preisKzMap;
	public static Map<String, String> abschlagKzMap;
	public static Map<String, String> auftragsartMap;
	public static Map<String, String> abgebendesLagerBAMap;
	public static Map<String, String> empfangendesLagerBAMap;
	public static Map<String, String> filterValuesForOrderMap;
	public static Map<String, String> filterValuesForDeliveryNoteMap;
	public static Map<String, String> teileartMap;
	public static Map<String, String> tokenValuesMap;
	public static Map<String, String> customerGroupBA2022Map;
	public static Map<String, String> customerGroupBA25Map;
	public static Map<String, String> alphaxCompanys;
	public static Map<String, String> displayForStartedInventory;
	public static Map<String, String> sortingAndPrintSelection;
	public static Map<String, String> ansichtMap;
	public static Map<String, String> printingPBMap;
	
	static {
		mehrwertsteuerMap = new LinkedHashMap<>();
		mehrwertsteuerMap.put("0", "Int. Exp");
		mehrwertsteuerMap.put("1", "Inland");
		mehrwertsteuerMap.put("2", "EU");
		Collections.unmodifiableMap(mehrwertsteuerMap);
		
		skontoMap = new LinkedHashMap<>();
		skontoMap.put("0", "ohne");
		skontoMap.put("1", "1.%Satz");
		skontoMap.put("2", "2.%Satz");
		skontoMap.put("3", "3.%Satz");
		Collections.unmodifiableMap(skontoMap);
		
		zuAbschlagMap = new LinkedHashMap<>();
		zuAbschlagMap.put("0", "ohne Zu\\Abschlag");
		zuAbschlagMap.put("1", "mit Zusclag");
		zuAbschlagMap.put("2", "mit Abschlag");
		zuAbschlagMap.put("3", "mit Zu\\Abschlag");
		Collections.unmodifiableMap(zuAbschlagMap);
		
		rabattkzMap = new LinkedHashMap<>();
		rabattkzMap.put("0", "Kunde ohne Rabatt");
		rabattkzMap.put("1", "Kunde mit Rabatt");
		Collections.unmodifiableMap(rabattkzMap);
		
		elektRechnungMap = new LinkedHashMap<>();
		elektRechnungMap.put("0", "Keine elektr. Zustellung");
		elektRechnungMap.put("1", "Zustellung per E-mail");
		elektRechnungMap.put("2", "Zustellung Dienstleister");
		elektRechnungMap.put("3", "Kunde hat abgelehnt");
		Collections.unmodifiableMap(elektRechnungMap);
		
		xmlDatenMap = new LinkedHashMap<>();
		xmlDatenMap.put("0", "Keine XML-Datei");
		xmlDatenMap.put("1", "XML-Datei Format 1");
		Collections.unmodifiableMap(xmlDatenMap);
		
		herkunftMap = new LinkedHashMap<>();
		herkunftMap.put(" ", "         ");
		herkunftMap.put("G", "Gw-Verkauf");
		herkunftMap.put("S", "Service");
		herkunftMap.put("V", "New-Verkauf");
		herkunftMap.put("X", "Sonstiges");
		herkunftMap.put("C", "CCS-Portal");
		Collections.unmodifiableMap(herkunftMap);
		
		anredeMap = new LinkedHashMap<>();
		anredeMap.put("Herr", "Herr");
		anredeMap.put("Herrn", "Herrn");
		anredeMap.put("Frau", "Frau");
		Collections.unmodifiableMap(anredeMap);
		
		LaufleistungMap = new LinkedHashMap<>();
		LaufleistungMap.put("K", "Kilometer");
		LaufleistungMap.put("M", "Meilen");
		LaufleistungMap.put("H", "Hours");
		Collections.unmodifiableMap(LaufleistungMap);
		
		wartGruppeMap = new LinkedHashMap<>();
		wartGruppeMap.put("1", "Erschwerter Betrieb");
		wartGruppeMap.put("2", "Nahverkehr");
		wartGruppeMap.put("3", "Fernverkehr");
		Collections.unmodifiableMap(wartGruppeMap);
		
		preisKzMap = new LinkedHashMap<>();
		preisKzMap.put("", " ");
		preisKzMap.put("K", "K");
		preisKzMap.put("L", "L");
		Collections.unmodifiableMap(preisKzMap);
		
		abschlagKzMap = new LinkedHashMap<>();
		abschlagKzMap.put("", " ");
		abschlagKzMap.put("0", "0");
		abschlagKzMap.put("1", "1");
		Collections.unmodifiableMap(abschlagKzMap);
		
		auftragsartMap = new LinkedHashMap<>();
		auftragsartMap.put("01", "VOR");
		auftragsartMap.put("02", "EIL");
		auftragsartMap.put("03", "Lagerergänzung");
		auftragsartMap.put("04", "Sonstige");
		auftragsartMap.put("05", "Sonstige");
		auftragsartMap.put("06", "Sonstige");
		auftragsartMap.put("07", "Sonstige");
		auftragsartMap.put("08", "Sonstige");
		auftragsartMap.put("09", "Sonstige");
		
		abgebendesLagerBAMap = new LinkedHashMap<>();
		abgebendesLagerBAMap.put("25","Bestandsminderung als Korrektur eines Lieferscheins");
		
		empfangendesLagerBAMap = new LinkedHashMap<>();
		empfangendesLagerBAMap.put("01","Zugang als Lagerposition");
		empfangendesLagerBAMap.put("02","Zugang als Kundenposition");
		empfangendesLagerBAMap.put("06","Bestandsverlagerung");
		
		filterValuesForOrderMap = new LinkedHashMap<>();
		filterValuesForOrderMap.put("01","Auftrag");
		filterValuesForOrderMap.put("02","Lager");
		filterValuesForOrderMap.put("03","Kunde");
		filterValuesForOrderMap.put("04","Bestellnummer");
		
		filterValuesForDeliveryNoteMap = new LinkedHashMap<>();
		filterValuesForDeliveryNoteMap.put("01","PIV");
		filterValuesForDeliveryNoteMap.put("02","Lieferscheinnummer");
		filterValuesForDeliveryNoteMap.put("03","Lieferant");
		
		teileartMap = new LinkedHashMap<>();
		teileartMap.put("1", "Originalteile");
		teileartMap.put("2", "Aggregate");
		teileartMap.put("3", "Zubehör und Kollektion");
		teileartMap.put("4", "Reifen");
		teileartMap.put("5", "Schmierstoffe");
		Collections.unmodifiableMap(teileartMap);
		
		tokenValuesMap = new LinkedHashMap<>();
		tokenValuesMap.put("1", "1");
		tokenValuesMap.put("2", "2");
		tokenValuesMap.put("3", "3");
		tokenValuesMap.put("4", "4");
		tokenValuesMap.put("5", "5");
		tokenValuesMap.put("6", "6");
		tokenValuesMap.put("7", "7");
		tokenValuesMap.put("8", "8");
		tokenValuesMap.put("9", "9");
		tokenValuesMap.put("10", "10");
		tokenValuesMap.put("11", "11");
		tokenValuesMap.put("12", "12");
		tokenValuesMap.put("13", "13");
		tokenValuesMap.put("14", "14");
		tokenValuesMap.put("15", "15");
		Collections.unmodifiableMap(tokenValuesMap);
		
		customerGroupBA2022Map = new LinkedHashMap<>();
		customerGroupBA2022Map.put("1", "Werkstatt");
		customerGroupBA2022Map.put("2", "Theke");
		customerGroupBA2022Map.put("3", "Zweigebtr");
		customerGroupBA2022Map.put("4", "Garantie");
		customerGroupBA2022Map.put("5", "Sonstige");
		Collections.unmodifiableMap(customerGroupBA2022Map);
		
		customerGroupBA25Map = new LinkedHashMap<>();
		customerGroupBA25Map.put("6", "Korr.Lief");
		customerGroupBA25Map.put("7", "Rücklief");
		customerGroupBA25Map.put("8", "Rückführ");
		customerGroupBA25Map.put("9", "Verschrot");
		customerGroupBA25Map.put("Ü", "Überlieg");
		Collections.unmodifiableMap(customerGroupBA25Map);
		
		alphaxCompanys = new LinkedHashMap<>();
		alphaxCompanys.put("01", "01");
		alphaxCompanys.put("02", "02");
		Collections.unmodifiableMap(alphaxCompanys);
		
		/*displayForStartedInventory = new LinkedHashMap<>();
		displayForStartedInventory.put("Alle", "Alle");
		displayForStartedInventory.put("Nicht erfasste", "Nicht erfasste");
		displayForStartedInventory.put("Bereits erfasste", "Bereits erfasste");
		displayForStartedInventory.put("Differente Positionen", "Differente Positionen");
		Collections.unmodifiableMap(displayForStartedInventory);*/
		
		displayForStartedInventory = new LinkedHashMap<>();
		displayForStartedInventory.put("0", "Alle Positionen");
		displayForStartedInventory.put("1", "Ungezählt");
		Collections.unmodifiableMap(displayForStartedInventory);
		
		sortingAndPrintSelection = new LinkedHashMap<>();
		sortingAndPrintSelection.put("0", "Nach Teilenummer sortieren");
		sortingAndPrintSelection.put("1", "Nach Lagerorten sortieren");
		Collections.unmodifiableMap(sortingAndPrintSelection);
		
		ansichtMap = new LinkedHashMap<>();
		ansichtMap.put("0", "Alle Positionen");
		ansichtMap.put("1", "Nur ohne Differenz");
		ansichtMap.put("2", "Nur mit Differenz");
		Collections.unmodifiableMap(ansichtMap);
				
		printingPBMap = new LinkedHashMap<>();
		printingPBMap.put("1", "1. Stelle");
		printingPBMap.put("2", "2. Stelle");
		printingPBMap.put("3", "3-4. Stelle");
		printingPBMap.put("4", "5. Stelle");
		printingPBMap.put("5", "6-7. Stelle");
		Collections.unmodifiableMap(printingPBMap);
	}
    		
	
	
	public Customer getCustomerDetailsByID(int id) {
		log.info("StubServiceRepository : Start getCustomerDetailsByID");
		
		String output1 = "00                                                "
        		+ "                                                             "
        		+ "                                               01Mr          "
        		+ "      NewCust20154                                            "
        		+ "                                                 PUNE         "
        		+ "                 PUNE     DE123   789                            000000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 "
                + "00                                                "
                + "                                                  "
                + "                                                  "
                + "        01011995010001880189            2222222222"
                + "1KEWAL PATADE PUNE    444444444444444 555555555555"
                + "555166666666  78000002500B00301                   "
                + "                                                  "
                + "                                                  "
                + "                                                  "
                + "                                                  "
                + "                                                  "
                + "                                                  "
                + "                                                  "
                + "                                                  "
                + "                                                  "
                + "                                                  "
                + "                                                  "
                + "                                                  "
                + "                                                  "
                + "                                                  "
                + "                        "
                + "00                                                "
                + "                                                  "
                + "                                                  "
                + "        +917030678484                             "
                + "      +918588041081                     +917030678"
                + "484                     +918588041081 FAX         "
                + "        kewal.patade@gmail.com                    "
                + "                                    www.t-systems."
                + "co.in                                             "
                + "                                                  "
                + "                                                  "
                + "                                                  "
                + "                                                  "
                + "                                                  "
                + "                                                  "
                + "                                                  "
                + "                                                  "
                + "                                                  "
                + "                                                  "
                + "                                                  "
                + "                        ";
		Customer customer = new Customer();
		customer.setAnredeSchlussel(output1.substring(158, 160).trim());
		customer.setAnrede(output1.substring(160, 178).trim());
		customer.setName(output1.substring(178, 208).trim());
		customer.setSortName(output1.substring(208, 223).trim());
		customer.setVoname(output1.substring(223, 253).trim());
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
		
		customer.setErrorList(errorList);
		//Customer Number
		customer.setKundenNummer(String.valueOf(id));
		
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
		
		return customer;
	}
	
	
	public List<DropdownObject> getDDLValuesFromStub(Map<String, String> mapCollection) {
		
		List<DropdownObject> dropDownList = new ArrayList<>();
		for(Map.Entry<String, String> mapEntry : mapCollection.entrySet()) {

			DropdownObject dropdownObject = new DropdownObject();
			dropdownObject.setKey(mapEntry.getKey());
			dropdownObject.setValue(mapEntry.getValue());

			dropDownList.add(dropdownObject);
		}
		return dropDownList;
	}
	
}