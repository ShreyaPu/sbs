/**
 * 
 */
package com.alphax.service.mb.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.alphax.common.rest.message.service.MessageService;
import com.alphax.model.mb.AdminUsers;
import com.alphax.common.exception.AlphaXException;
import com.alphax.common.exception.ExceptionMessages;
import com.alphax.repository.DBServiceRepository;
import com.alphax.repository.StubServiceRepository;
import com.alphax.service.mb.AlphaXSetupService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author A98157120
 *
 */

@Service
@Slf4j
public class AlphaXSetupImpl extends BaseService implements AlphaXSetupService {
	
	@Autowired
	DBServiceRepository dbServiceRepository;

	@Autowired
	private MessageService messageService;
	
	@Autowired
	StubServiceRepository stubServiceRepository;
	
	/**
	 * This method is used to Set up the Initial Admin User for AlphaX
	 * @return Object
	 */

	@Override
	public Map<String, Boolean> alphaXAdminUserSetup(String schema, String dataLibrary, String adminUser, String loginUserName) {

		log.info("Inside alphaXAdminUSerSetup method of AlphaXSetupImpl");
		Map<String, Boolean> programOutput = new HashMap<String, Boolean>();
		
		try(Connection con = dbServiceRepository.getConnectionObject();
				Statement stmt = con.createStatement();){
		
			log.info("Start :Check admin user");
			boolean adminSetupFalg = checkAdminUserId(schema,dataLibrary,loginUserName); 
			log.info("End..");
			
			con.setAutoCommit(false);
			
			if(!adminSetupFalg){
				
			// Start Module and Module section setup
			alphaXModuleSetup(schema, loginUserName, stmt);
			
			log.info("Create Entries in O_COMPANY Table");
			
			StringBuilder query47 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_COMPANY");
			query47.append(" (AP_COMPANY_ID, NAME, CITY, POST_CODE, STREETANDNUM, ISACTIVE, ISDELETED, CREATED_TS, CREATED_BY)  values ( ");
			query47.append(" '99', 'Initial Firma', 'Dummy', 12345, 'Dummy', 1, 0, ");
			query47.append("CURRENT TIMESTAMP ,");
			query47.append("'"+loginUserName+"') ");
			
			log.info("Create Entries in O_AGENCY Table");
			
			StringBuilder query48 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_AGENCY");
			query48.append(" (AP_AGENCY_ID, COMPANY_ID, NAME, CITY, POST_CODE, STREETANDNUM, ISACTIVE, ISDELETED, CREATED_TS, CREATED_BY)   values ( ");
			query48.append(" '99', 1, 'Initial Filiale', 'Dummy', 12345, 'Dummy', 1, 0, ");
			query48.append("CURRENT TIMESTAMP ,");
			query48.append("'"+loginUserName+"') ");
			
			log.info("Create Entries in O_WRH Table");
			
			StringBuilder query49 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_WRH");
			query49.append(" (AP_WAREHOUS_ID, NAME, CITY, POST_CODE, STREETANDNUM, VF_NUMBER, ISACTIVE, ISDELETED, CREATED_TS, CREATED_BY)  values ( ");
			query49.append(" '99', 'Initial Lager', 'Dummy', 12345, 'Dummy', 1, 1, 0, ");
			query49.append("CURRENT TIMESTAMP ,");
			query49.append("'"+loginUserName+"') ");
			
			log.info("Create Entries in O_USER Table");
			
			StringBuilder query50 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_USER");
			query50.append(" (FIRSTNAME, LASTNAME, LOGIN, EMAIL, ISACTIVE, ISDELETED, CREATED_TS, CREATED_BY)  values ( ");
			query50.append(" 'Admin', 'Admin', '"+adminUser+"', 'xxxx', 1, 0, ");
			query50.append("CURRENT TIMESTAMP ,");
			query50.append("'"+loginUserName+"') ");
			
			log.info("Create Entries in O_ROLE Table");
			
			StringBuilder query51 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_ROLE");
			query51.append(" (NAME, DESCRIPTION, ISACTIVE, ISDELETED, CREATED_TS, CREATED_BY)  values ( ");
			query51.append(" 'Admin', 'Administrator', 1, 0, ");
			query51.append("CURRENT TIMESTAMP ,");
			query51.append("'"+loginUserName+"') ");
			
			log.info("Create Entries in O_USERRL Table");
			
			StringBuilder query52 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_USERRL");
			query52.append(" (USER_ID, ROLE_ID, AGENCY_ID, ISACTIVE, ISDEFAULT, CREATED_TS, CREATED_BY)  values ( ");
			query52.append(" 1, 1, 1, 1, 1, ");
			query52.append("CURRENT TIMESTAMP ,");
			query52.append("'"+loginUserName+"') ");
			
			log.info("Create Entries in O_AGNWRH Table");
			
			StringBuilder query53 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_AGNWRH");
			query53.append(" (AGENCY_ID, WAREHOUS_ID, ISACTIVE, CREATED_TS, CREATED_BY)  values ( ");
			query53.append(" 1, 1, 1, ");
			query53.append("CURRENT TIMESTAMP ,");
			query53.append("'"+loginUserName+"') ");
			
			log.info("Create Entries in O_SETUP Table");
			
			StringBuilder query54 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query54.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query54.append(" (0, 0, 1, 0, 0, 0, 'BSN_AUTO_MOVE_EDN', 0, ");
			query54.append("'"+loginUserName+"' ,");
			query54.append("CURRENT TIMESTAMP )");
			
			StringBuilder query55 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query55.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query55.append(" (0, 0, 1, 0, 0, 0, 'LFS_AUTO_MOVE_BSN_ET', 0, ");
			query55.append("'"+loginUserName+"' ,");
			query55.append("CURRENT TIMESTAMP )");
			
			StringBuilder query56 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query56.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query56.append(" (0, 0, 1, 0, 0, 0, 'LFS_AUTO_PRINT_EDN', 0, ");
			query56.append("'"+loginUserName+"' ,");
			query56.append("CURRENT TIMESTAMP )");
			
			StringBuilder query57 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query57.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query57.append(" (0, 0, 1, 0, 0, 0, 'CONF_AUTO_PRICE', 0, ");
			query57.append("'"+loginUserName+"' ,");
			query57.append("CURRENT TIMESTAMP )");
			
			StringBuilder query58 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query58.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query58.append(" (0, 0, 1, 0, 0, 0, 'CONF_AUTO_PRICE_VALUE', 0, ");
			query58.append("'"+loginUserName+"' ,");
			query58.append("CURRENT TIMESTAMP )");
			
			StringBuilder query59 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query59.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query59.append(" (0, 0, 1, 0, 0, 0, 'CONF_AUTO_MARKETINGCODE', 0, ");
			query59.append("'"+loginUserName+"' ,");
			query59.append("CURRENT TIMESTAMP )");
			
			StringBuilder query60 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query60.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query60.append(" (0, 0, 1, 0, 0, 0, 'CONF_AUTO_MARKETINGCODE_VALUE', 0, ");
			query60.append("'"+loginUserName+"' ,");
			query60.append("CURRENT TIMESTAMP )");
			
			StringBuilder query61 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query61.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query61.append(" (0, 0, 1, 0, 0, 0, 'CONF_AUTO_NAME', 0, ");
			query61.append("'"+loginUserName+"' ,");
			query61.append("CURRENT TIMESTAMP )");
			
			StringBuilder query62 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query62.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query62.append(" (0, 0, 1, 0, 0, 0, 'CONF_AUTO_NAME_VALUE', 0, ");
			query62.append("'"+loginUserName+"' ,");
			query62.append("CURRENT TIMESTAMP )");
			
			StringBuilder query63 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query63.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query63.append(" (0, 0, 1, 0, 0, 0, 'CONF_AUTO_DISCOUNT', 0, ");
			query63.append("'"+loginUserName+"' ,");
			query63.append("CURRENT TIMESTAMP )");
			
			StringBuilder query64 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query64.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query64.append(" (0, 0, 1, 0, 0, 0, 'CONF_AUTO_DISCOUNT_VALUE', 0, ");
			query64.append("'"+loginUserName+"' ,");
			query64.append("CURRENT TIMESTAMP )");
			
			StringBuilder query65 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query65.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query65.append(" (0, 0, 1, 0, 0, 0, 'CONF_AUTO_LAGERORT', 0, ");
			query65.append("'"+loginUserName+"' ,");
			query65.append("CURRENT TIMESTAMP )");
			
			StringBuilder query66 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query66.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query66.append(" (0, 0, 1, 0, 0, 0, 'CONF_AUTO_LAGERORT_VALUE', 0, ");
			query66.append("'"+loginUserName+"' ,");
			query66.append("CURRENT TIMESTAMP )");
			
			StringBuilder query67 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query67.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query67.append(" (0, 0, 1, 0, 0, 0, 'CONF_AUTO_MARKE', 0, ");
			query67.append("'"+loginUserName+"' ,");
			query67.append("CURRENT TIMESTAMP )");
			
			StringBuilder query68 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query68.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query68.append(" (0, 0, 1, 0, 0, 0, 'CONF_AUTO_MARKE_VALUE', 0, ");
			query68.append("'"+loginUserName+"' ,");
			query68.append("CURRENT TIMESTAMP )");
			
			StringBuilder query69 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query69.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query69.append(" (0, 0, 0, 1, 0, 0, 'SUCHE_AUFTRAGE', 0, ");
			query69.append("'"+loginUserName+"' ,");
			query69.append("CURRENT TIMESTAMP )");
			
			StringBuilder query70 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query70.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query70.append(" (0, 0, 0, 1, 0, 0, 'SUCHE_KUNDEN', 0, ");
			query70.append("'"+loginUserName+"' ,");
			query70.append("CURRENT TIMESTAMP )");
			
			StringBuilder query71 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query71.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query71.append(" (0, 0, 0, 1, 0, 0, 'SUCHE_FAHRZEUG', 0, ");
			query71.append("'"+loginUserName+"' ,");
			query71.append("CURRENT TIMESTAMP )");
			
			StringBuilder query72 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query72.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query72.append(" (0, 0, 0, 1, 0, 0, 'SUCHE_TEILE', 0, ");
			query72.append("'"+loginUserName+"' ,");
			query72.append("CURRENT TIMESTAMP )");
			
			StringBuilder query73 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query73.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query73.append(" (0, 0, 0, 1, 0, 0, 'WE_EINGANG', 0, ");
			query73.append("'"+loginUserName+"' ,");
			query73.append("CURRENT TIMESTAMP )");
			
			StringBuilder query74 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query74.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query74.append(" (0, 0, 0, 1, 0, 0, 'WE_WARENEINGANG', 0, ");
			query74.append("'"+loginUserName+"' ,");
			query74.append("CURRENT TIMESTAMP )");
			
			StringBuilder query75 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query75.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query75.append(" (0, 0, 0, 1, 0, 0, 'WE_TERMINE', 0, ");
			query75.append("'"+loginUserName+"' ,");
			query75.append("CURRENT TIMESTAMP )");
			
			StringBuilder query76 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query76.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query76.append(" (0, 0, 0, 1, 0, 0, 'WE_OFFENKONF', 0, ");
			query76.append("'"+loginUserName+"' ,");
			query76.append("CURRENT TIMESTAMP )");
			
			StringBuilder query77 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query77.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query77.append(" (0, 0, 0, 1, 0, 0, 'WE_BEHOKONF', 0, ");
			query77.append("'"+loginUserName+"' ,");
			query77.append("CURRENT TIMESTAMP )");
			
			StringBuilder query78 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query78.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query78.append(" (0, 0, 0, 1, 0, 0, 'BW_OFFENE', 0, ");
			query78.append("'"+loginUserName+"' ,");
			query78.append("CURRENT TIMESTAMP )");
			
			StringBuilder query79 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query79.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query79.append(" (0, 0, 0, 1, 0, 0, 'BW_AUSGELOSTE', 0, ");
			query79.append("'"+loginUserName+"' ,");
			query79.append("CURRENT TIMESTAMP )");
			
			StringBuilder query80 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query80.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query80.append(" (0, 0, 0, 1, 0, 0, 'BW_HISTORIE', 0, ");
			query80.append("'"+loginUserName+"' ,");
			query80.append("CURRENT TIMESTAMP )");
			
			StringBuilder query81 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query81.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query81.append(" (0, 0, 0, 1, 0, 0, 'TW_TEILEANLEGEN', 0, ");
			query81.append("'"+loginUserName+"' ,");
			query81.append("CURRENT TIMESTAMP )");
			
			StringBuilder query82 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query82.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query82.append(" (0, 0, 0, 1, 0, 0, 'TW_TEILEUBERSICHT', 0, ");
			query82.append("'"+loginUserName+"' ,");
			query82.append("CURRENT TIMESTAMP )");
			
			StringBuilder query83 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query83.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query83.append(" (0, 0, 0, 1, 0, 0, 'TW_ALTERNATIVEN', 0, ");
			query83.append("'"+loginUserName+"' ,");
			query83.append("CURRENT TIMESTAMP )");
			
			StringBuilder query84 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query84.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query84.append(" (0, 0, 0, 1, 0, 0, 'TW_PREISPFLEGE', 0, ");
			query84.append("'"+loginUserName+"' ,");
			query84.append("CURRENT TIMESTAMP )");
			
			StringBuilder query85 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query85.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query85.append(" (0, 0, 0, 1, 0, 0, 'TW_SONSTIGE', 0, ");
			query85.append("'"+loginUserName+"' ,");
			query85.append("CURRENT TIMESTAMP )");
			
			StringBuilder query86 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query86.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query86.append(" (0, 0, 0, 1, 0, 0, 'TW_BEWEGUNG', 0, ");
			query86.append("'"+loginUserName+"' ,");
			query86.append("CURRENT TIMESTAMP )");
			
			StringBuilder query87 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query87.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query87.append(" (0, 0, 0, 1, 0, 0, 'TW_REIFENLABEL', 0, ");
			query87.append("'"+loginUserName+"' ,");
			query87.append("CURRENT TIMESTAMP )");
			
			StringBuilder query88 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query88.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query88.append(" (0, 0, 0, 1, 0, 0, 'TW_VERKAUFE', 0, ");
			query88.append("'"+loginUserName+"' ,");
			query88.append("CURRENT TIMESTAMP )");
			
			StringBuilder query89 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query89.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query89.append(" (0, 0, 0, 1, 0, 0, 'TW_EINKAUF', 0, ");
			query89.append("'"+loginUserName+"' ,");
			query89.append("CURRENT TIMESTAMP )");
			
			StringBuilder query90 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query90.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query90.append(" (0, 0, 0, 1, 0, 0, 'MVB_ZUGANG', 0, ");
			query90.append("'"+loginUserName+"' ,");
			query90.append("CURRENT TIMESTAMP )");
			
			StringBuilder query91 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query91.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query91.append(" (0, 0, 0, 1, 0, 0, 'MVB_ERHOHUNG', 0, ");
			query91.append("'"+loginUserName+"' ,");
			query91.append("CURRENT TIMESTAMP )");
			
			StringBuilder query92 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query92.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query92.append(" (0, 0, 0, 1, 0, 0, 'MVB_VERMINDERUNG', 0, ");
			query92.append("'"+loginUserName+"' ,");
			query92.append("CURRENT TIMESTAMP )");
			
			StringBuilder query93 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query93.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query93.append(" (0, 0, 0, 1, 0, 0, 'MVB_LOSCHUNG_TEIL', 0, ");
			query93.append("'"+loginUserName+"' ,");
			query93.append("CURRENT TIMESTAMP )");
			
			StringBuilder query94 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query94.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query94.append(" (0, 0, 0, 1, 0, 0, 'MVB_MANUELLE_ZAHLUNG', 0, ");
			query94.append("'"+loginUserName+"' ,");
			query94.append("CURRENT TIMESTAMP )");
			
			StringBuilder query95 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query95.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query95.append(" (0, 0, 0, 1, 0, 0, 'MVB_UMBUCHEN', 0, ");
			query95.append("'"+loginUserName+"' ,");
			query95.append("CURRENT TIMESTAMP )");
			
			StringBuilder query96 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query96.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query96.append(" (0, 1, 0, 0, 0, 0, 'GET_TOKEN', 3, ");
			query96.append("'"+loginUserName+"' ,");
			query96.append("CURRENT TIMESTAMP )");
			
			StringBuilder query97 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query97.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query97.append(" (0, 0, 0, 0, 0, 1, 'DEFAULT_AGENCY', 1, ");
			query97.append("'"+loginUserName+"' ,");
			query97.append("CURRENT TIMESTAMP )");
			
			StringBuilder query98 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query98.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query98.append(" (0, 0, 0, 0, 0, 1, 'DEFAULT_COMPANY', 1, ");
			query98.append("'"+loginUserName+"' ,");
			query98.append("CURRENT TIMESTAMP )");
			
			StringBuilder query99 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query99.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query99.append(" (0, 0, 0, 0, 0, 1, 'DEFAULT_WAREHOUSE', 1, ");
			query99.append("'"+loginUserName+"' ,");
			query99.append("CURRENT TIMESTAMP )");
			
			log.info("Create Entries for MVB_VERLAGERUNG");
			
			StringBuilder query100 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_SETUP");
			query100.append(" (COMPANY_ID, AGENCY_ID, WAREHOUS_ID, ROLE_ID, PRINTER_ID, USER_ID, KEY, VALUE, UPDATED_BY, TIMESTAMP)  values  ");
			query100.append(" (0, 0, 0, 1, 0, 0, 'MVB_VERLAGERUNG', 0, ");
			query100.append("'"+loginUserName+"' ,");
			query100.append("CURRENT TIMESTAMP )");			
			
			log.info("SQL Builder End");
			
			stmt.addBatch(query47.toString());
			stmt.addBatch(query48.toString());
			stmt.addBatch(query49.toString());
			stmt.addBatch(query50.toString());
			stmt.addBatch(query51.toString());
			stmt.addBatch(query52.toString());
			stmt.addBatch(query53.toString());
			stmt.addBatch(query54.toString());
			stmt.addBatch(query55.toString());
			stmt.addBatch(query56.toString());
			stmt.addBatch(query57.toString());
			stmt.addBatch(query58.toString());
			stmt.addBatch(query59.toString());
			stmt.addBatch(query60.toString());
			stmt.addBatch(query61.toString());
			stmt.addBatch(query62.toString());
			stmt.addBatch(query63.toString());
			stmt.addBatch(query64.toString());
			stmt.addBatch(query65.toString());
			stmt.addBatch(query66.toString());
			stmt.addBatch(query67.toString());
			stmt.addBatch(query68.toString());
			stmt.addBatch(query69.toString());
			stmt.addBatch(query70.toString());
			stmt.addBatch(query71.toString());
			stmt.addBatch(query72.toString());
			stmt.addBatch(query73.toString());
			stmt.addBatch(query74.toString());
			stmt.addBatch(query75.toString());
			stmt.addBatch(query76.toString());
			stmt.addBatch(query77.toString());
			stmt.addBatch(query78.toString());
			stmt.addBatch(query79.toString());
			stmt.addBatch(query80.toString());
			stmt.addBatch(query81.toString());
			stmt.addBatch(query82.toString());
			stmt.addBatch(query83.toString());
			stmt.addBatch(query84.toString());
			stmt.addBatch(query85.toString());
			stmt.addBatch(query86.toString());
			stmt.addBatch(query87.toString());
			stmt.addBatch(query88.toString());
			stmt.addBatch(query89.toString());
			stmt.addBatch(query90.toString());
			stmt.addBatch(query91.toString());
			stmt.addBatch(query92.toString());
			stmt.addBatch(query93.toString());
			stmt.addBatch(query94.toString());
			stmt.addBatch(query95.toString());
			stmt.addBatch(query96.toString());
			stmt.addBatch(query97.toString());
			stmt.addBatch(query98.toString());
			stmt.addBatch(query99.toString());
			stmt.addBatch(query100.toString());
				
			
			} else {
				
				StringBuilder deleteModules = new StringBuilder(" DELETE FROM ").append(schema).append(".O_MODULE");
				StringBuilder deleteModuleSections = new StringBuilder(" DELETE FROM ").append(schema).append(".O_MODSEC");

				boolean modsecUpdateFlag = dbServiceRepository.deleteResultUsingQuery(deleteModuleSections.toString(),con);
				if(modsecUpdateFlag){
				log.info("O_MODSEC deleted successfully");
				}
				
				boolean moduleUpdateFlag = dbServiceRepository.deleteResultUsingQuery(deleteModules.toString(),con);
				if(moduleUpdateFlag){
				log.info("O_MODULE deleted successfully");
				}
				
				alphaXModuleSetup(schema, loginUserName, stmt);
			}
			
			log.info("AddBatch Completed - The Admin records Insertion will Start");
			
			int[] records =  dbServiceRepository.insertResultUsingBatchQuery(stmt);
			
			if(records != null){
			log.info("Admin user created - Total rows inserted :"+records.length);
			}
			con.commit();
			con.setAutoCommit(true);
			programOutput.put("Die intitiale Einrichtung eines Admin-Nutzers für alphaX ist erfolgreich.", true);
														
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.ENV_SETUP_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.ENV_SETUP_MSG_KEY), exception);
			throw exception;
		}
		
		return programOutput;
	}
	
	
	public boolean checkAdminUserId(String schema, String dataLibrary,String loginName) {

		log.info("Inside checkAdminUserId method of JwtAuthenticationServiceImpl");

		boolean isAdminUserExist = false;
		
		try {
			StringBuilder query = new StringBuilder("SELECT CREATED_BY,CREATED_TS  FROM ").append(schema).append(".O_USER ");
			query.append(" WHERE ISACTIVE = 1 AND USER_ID = 1 ");
			
			List<AdminUsers> adminUserList = dbServiceRepository.getResultUsingQuery(AdminUsers.class, query.toString(), true);

			if (adminUserList != null && !adminUserList.isEmpty() && adminUserList.size() == 1) {
				isAdminUserExist = true;
			}else{
				isAdminUserExist = false;
			} 
			
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.INVALID_USER_LOGIN_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.INVALID_USER_LOGIN_MSG_KEY), exception);
			throw exception;
		}
		
		return isAdminUserExist;
	}
	

	private void alphaXModuleSetup(String schema, String loginUserName, Statement stmt) throws SQLException{
		
		log.info("Inside alphaXModuleSetup method of AlphaXSetupImpl");
		
		log.info("Create Entries in O_MODULE Table");
		
		StringBuilder query1 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODULE");
		query1.append(" (MODULE_ID, MODULE_NAME, DESCRIPTION, ISACTIVE, CREATED_TS, CREATED_BY ) values ( ");
		query1.append(" 10, 'Administration', 'Administration', 1 ,");
		query1.append("CURRENT TIMESTAMP ,");
		query1.append("'"+loginUserName+"') ");
		
		StringBuilder query2 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODULE");
		query2.append(" (MODULE_ID, MODULE_NAME, DESCRIPTION, ISACTIVE, CREATED_TS, CREATED_BY ) values ( ");
		query2.append(" 20, 'Suche', 'Suchleiste', 1 , ");
		query2.append("CURRENT TIMESTAMP ,");
		query2.append("'"+loginUserName+"') ");
		
		StringBuilder query3 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODULE");
		query3.append(" (MODULE_ID, MODULE_NAME, DESCRIPTION, ISACTIVE, CREATED_TS, CREATED_BY ) values ( ");
		query3.append(" 30, 'Menu Administration', 'Administration', 1 ,");
		query3.append("CURRENT TIMESTAMP ,");
		query3.append("'"+loginUserName+"') ");
		
		StringBuilder query4 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODULE");
		query4.append(" (MODULE_ID, MODULE_NAME, DESCRIPTION, ISACTIVE, CREATED_TS, CREATED_BY ) values ( ");
		query4.append(" 40, 'Menu Lagerwirtschaft', 'Lagerwirtschaft', 1 ,");
		query4.append("CURRENT TIMESTAMP ,");
		query4.append("'"+loginUserName+"') ");
		
		StringBuilder query5 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODULE");
		query5.append(" (MODULE_ID, MODULE_NAME, DESCRIPTION, ISACTIVE, CREATED_TS, CREATED_BY ) values ( ");
		query5.append(" 50, 'Menu Stammdaten', 'Stammdaten', 1 ,");
		query5.append("CURRENT TIMESTAMP ,");
		query5.append("'"+loginUserName+"') ");
		
		StringBuilder query6 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODULE");
		query6.append(" (MODULE_ID, MODULE_NAME, DESCRIPTION, ISACTIVE, CREATED_TS, CREATED_BY ) values ( ");
		query6.append(" 60, 'Bar', 'Top Bar', 1 ,");
		query6.append("CURRENT TIMESTAMP ,");
		query6.append("'"+loginUserName+"') ");
		
		StringBuilder query7 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODULE");
		query7.append(" (MODULE_ID, MODULE_NAME, DESCRIPTION, ISACTIVE, CREATED_TS, CREATED_BY ) values ( ");
		query7.append(" 70, 'Wareneingang', 'Wareneingang', 1 ,");
		query7.append("CURRENT TIMESTAMP ,");
		query7.append("'"+loginUserName+"') ");
		
		StringBuilder query8 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODULE");
		query8.append(" (MODULE_ID, MODULE_NAME, DESCRIPTION, ISACTIVE, CREATED_TS, CREATED_BY ) values ( ");
		query8.append(" 80, 'Bestellwesen', 'Bestellwesen', 1 ,");
		query8.append("CURRENT TIMESTAMP ,");
		query8.append("'"+loginUserName+"') ");
		
		StringBuilder query9 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODULE");
		query9.append(" (MODULE_ID, MODULE_NAME, DESCRIPTION, ISACTIVE, CREATED_TS, CREATED_BY ) values ( ");
		query9.append(" 90, 'Teilewesen', 'Ersatzteilstamm', 1 ,");
		query9.append("CURRENT TIMESTAMP ,");
		query9.append("'"+loginUserName+"') ");
		
		StringBuilder query10 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODULE");
		query10.append(" (MODULE_ID, MODULE_NAME, DESCRIPTION, ISACTIVE, CREATED_TS, CREATED_BY ) values ( ");
		query10.append(" 100, 'Manuelle Vorgange buchen', 'Manuelle Vorgänge', 1 ,");
		query10.append("CURRENT TIMESTAMP ,");
		query10.append("'"+loginUserName+"') ");
		
		StringBuilder query11 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODULE");
		query11.append(" (MODULE_ID, MODULE_NAME, DESCRIPTION, ISACTIVE, CREATED_TS, CREATED_BY ) values ( ");
		query11.append(" 110, 'Dashboard', 'Dashboard', 1 ,");
		query11.append("CURRENT TIMESTAMP ,");
		query11.append("'"+loginUserName+"') ");
		
		StringBuilder query12 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODULE");
		query12.append(" (MODULE_ID, MODULE_NAME, DESCRIPTION, ISACTIVE, CREATED_TS, CREATED_BY ) values ( ");
		query12.append(" 120, 'Selektionen', 'Reports aller art', 1 ,");
		query12.append("CURRENT TIMESTAMP ,");
		query12.append("'"+loginUserName+"') ");
		
		log.info("Create Entries in O_MODSEC Table");
		
		StringBuilder query13 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query13.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query13.append(" 210, 'Aufträge', 20, 1, 'SUCHE_AUFTRAGE',");
		query13.append("CURRENT TIMESTAMP ,");
		query13.append("'"+loginUserName+"') ");
		
		StringBuilder query14 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query14.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query14.append(" 220, 'Kunden', 20, 1, 'SUCHE_KUNDEN',");
		query14.append("CURRENT TIMESTAMP ,");
		query14.append("'"+loginUserName+"') ");
		
		StringBuilder query15 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query15.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query15.append(" 230, 'Farhzeug', 20, 1, 'SUCHE_FAHRZEUG',");
		query15.append("CURRENT TIMESTAMP ,");
		query15.append("'"+loginUserName+"') ");
		
		StringBuilder query16 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query16.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query16.append(" 240, 'Teile', 20, 1, 'SUCHE_TEILE',");
		query16.append("CURRENT TIMESTAMP ,");
		query16.append("'"+loginUserName+"') ");
		
		StringBuilder query17 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query17.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query17.append(" 700, 'Lieferscheine Eingangspool', 70, 1, 'WE_EINGANG',");
		query17.append("CURRENT TIMESTAMP ,");
		query17.append("'"+loginUserName+"') ");
		
		StringBuilder query18 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query18.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query18.append(" 710, 'Lieferscheine Wareneingang', 70, 1, 'WE_WARENEINGANG',");
		query18.append("CURRENT TIMESTAMP ,");
		query18.append("'"+loginUserName+"') ");
		
		StringBuilder query19 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query19.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query19.append(" 720, 'Lieferscheine Termine', 70, 1, 'WE_TERMINE',");
		query19.append("CURRENT TIMESTAMP ,");
		query19.append("'"+loginUserName+"') ");
		
		StringBuilder query20 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query20.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query20.append(" 730, 'Lieferscheine offene Konflikte', 70, 1, 'WE_OFFENKONF',");
		query20.append("CURRENT TIMESTAMP ,");
		query20.append("'"+loginUserName+"') ");
		
		StringBuilder query21 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query21.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query21.append(" 740, 'Lieferscheine behobene Konflikte', 70, 1, 'WE_BEHOKONF',");
		query21.append("CURRENT TIMESTAMP ,");
		query21.append("'"+loginUserName+"') ");
		
		StringBuilder query22 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query22.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query22.append(" 800, 'Offene Bestellungen', 80, 1, 'BW_OFFENE',");
		query22.append("CURRENT TIMESTAMP ,");
		query22.append("'"+loginUserName+"') ");
		
		StringBuilder query23 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query23.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query23.append(" 810, 'Ausgelöste Bestellungen', 80, 1, 'BW_AUSGELOSTE',");
		query23.append("CURRENT TIMESTAMP ,");
		query23.append("'"+loginUserName+"') ");
		
		StringBuilder query24 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query24.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query24.append(" 820, 'Bestellhistorie', 80, 0, 'BW_HISTORIE',");
		query24.append("CURRENT TIMESTAMP ,");
		query24.append("'"+loginUserName+"') ");
		
		StringBuilder query25 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query25.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query25.append(" 900, 'Teileübersicht', 90, 1, 'TW_TEILEUBERSICHT',");
		query25.append("CURRENT TIMESTAMP ,");
		query25.append("'"+loginUserName+"') ");
		
		StringBuilder query26 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query26.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query26.append(" 905, 'Teileanlegen', 90, 1, 'TW_TEILEANLEGEN',");
		query26.append("CURRENT TIMESTAMP ,");
		query26.append("'"+loginUserName+"') ");
		
		StringBuilder query27 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query27.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query27.append(" 910, 'Alternativen', 90, 1, 'TW_ALTERNATIVEN',");
		query27.append("CURRENT TIMESTAMP ,");
		query27.append("'"+loginUserName+"') ");
		
		StringBuilder query28 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query28.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query28.append(" 920, 'Preispflege', 90, 1, 'TW_PREISPFLEGE',");
		query28.append("CURRENT TIMESTAMP ,");
		query28.append("'"+loginUserName+"') ");
		
		StringBuilder query29 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query29.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query29.append(" 930, 'Sonstige Teileparameter', 90, 1, 'TW_SONSTIGE',");
		query29.append("CURRENT TIMESTAMP ,");
		query29.append("'"+loginUserName+"') ");
		
		StringBuilder query30 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query30.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query30.append(" 940, 'Bewegungsdaten / Ereignisdaten', 90, 1, 'TW_BEWEGUNG',");
		query30.append("CURRENT TIMESTAMP ,");
		query30.append("'"+loginUserName+"') ");
		
		StringBuilder query31 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query31.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query31.append(" 950, 'Reifenlabel', 90, 1, 'TW_REIFENLABEL',");
		query31.append("CURRENT TIMESTAMP ,");
		query31.append("'"+loginUserName+"') ");
		
		StringBuilder query32 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query32.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query32.append(" 960, 'Verkäufe', 90, 1, 'TW_VERKAUFE',");
		query32.append("CURRENT TIMESTAMP ,");
		query32.append("'"+loginUserName+"') ");
		
		StringBuilder query33 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query33.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query33.append(" 970, 'Einkauf', 90, 1, 'TW_EINKAUF',");
		query33.append("CURRENT TIMESTAMP ,");
		query33.append("'"+loginUserName+"') ");
		
		StringBuilder query331 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query331.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query331.append(" 980, 'Etikett', 90, 1, 'TW_ETIKETT',");
		query331.append("CURRENT TIMESTAMP ,");
		query331.append("'"+loginUserName+"') ");
		
		StringBuilder query332 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query332.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query332.append(" 990, 'Teile Loeschen', 90, 1, 'TW_LOESCHEN',");
		query332.append("CURRENT TIMESTAMP ,");
		query332.append("'"+loginUserName+"') ");
		
		StringBuilder query333 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query333.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query333.append(" 995, 'Weitere Verfügbarkeit', 90, 1, 'TW_WEITERE_VERFUEGBAR',");
		query333.append("CURRENT TIMESTAMP ,");
		query333.append("'"+loginUserName+"') ");
		
		StringBuilder query34 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query34.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query34.append(" 1000, 'Zugänge (BA01 / BA02)', 100, 1, 'MVB_ZUGANG',");
		query34.append("CURRENT TIMESTAMP ,");
		query34.append("'"+loginUserName+"') ");
		
		StringBuilder query35 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query35.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query35.append(" 1010, 'Bestandserhöhung (BA 06)', 100, 1, 'MVB_ERHOHUNG',");
		query35.append("CURRENT TIMESTAMP ,");
		query35.append("'"+loginUserName+"') ");
		
		StringBuilder query36 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query36.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query36.append(" 1035, 'Bestandsverminderung (BA 25)', 100, 1, 'MVB_VERMINDERUNG',");
		query36.append("CURRENT TIMESTAMP ,");
		query36.append("'"+loginUserName+"') ");
		
		StringBuilder query37 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query37.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query37.append(" 1060, 'Löschung Teil (BA 50)', 100, 1, 'MVB_LOSCHUNG_TEIL',");
		query37.append("CURRENT TIMESTAMP ,");
		query37.append("'"+loginUserName+"') ");
		
		StringBuilder query38 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query38.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query38.append(" 1065, 'Manuelle Zählung (BA 67)', 100, 1, 'MVB_MANUELLE_ZAHLUNG',");
		query38.append("CURRENT TIMESTAMP ,");
		query38.append("'"+loginUserName+"') ");
		
		StringBuilder query39 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query39.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query39.append(" 1070, 'Umbuchen von Gebinden in Stück (BA25 & BA06)', 100, 1, 'MVB_UMBUCHEN',");
		query39.append("CURRENT TIMESTAMP ,");
		query39.append("'"+loginUserName+"') ");
		
		StringBuilder query40 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query40.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ");
		query40.append(" (1080, 'Teileverlagerung', 100, 1, 'MVB_VERLAGERUNG',");
		query40.append("CURRENT TIMESTAMP ,");
		query40.append("'"+loginUserName+"') ");			
		
		StringBuilder query41 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query41.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query41.append(" 1200, 'Selektion aus Bestand', 120, 1, 'SEL_BESTAND',");
		query41.append("CURRENT TIMESTAMP ,");
		query41.append("'"+loginUserName+"') ");
		
		StringBuilder query42 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query42.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query42.append(" 1210, 'Selektion aus Bewegung', 120, 1, 'SEL_BEWEGUNG',");
		query42.append("CURRENT TIMESTAMP ,");
		query42.append("'"+loginUserName+"') ");
		
		StringBuilder query43 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query43.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query43.append(" 1220, 'Selektion nach Gängigkeit', 120, 1, 'SEL_GAENGIGKEIT',");
		query43.append("CURRENT TIMESTAMP ,");
		query43.append("'"+loginUserName+"') ");
		
		StringBuilder query44 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query44.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query44.append(" 1230, 'Geschäftsfallstatistik', 120, 1, 'SEL_GESCHAEFTSFALL',");
		query44.append("CURRENT TIMESTAMP ,");
		query44.append("'"+loginUserName+"') ");
		
		StringBuilder query45 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query45.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query45.append(" 1240, 'Buchungsrelevante Listen', 120, 1, 'SEL_BUCHUNGEN',");
		query45.append("CURRENT TIMESTAMP ,");
		query45.append("'"+loginUserName+"') ");
		
		StringBuilder query46 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query46.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query46.append(" 1250, 'Teileanbietung', 120, 1, 'SEL_TEILEANBIETUNG',");
		query46.append("CURRENT TIMESTAMP ,");
		query46.append("'"+loginUserName+"') ");
		
		StringBuilder query47 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query47.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query47.append(" 1260, 'Bestandstruktur', 120, 1, 'SEL_BESTANDSTRUKTUR',");
		query47.append("CURRENT TIMESTAMP ,");
		query47.append("'"+loginUserName+"') ");
		
		StringBuilder query48 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query48.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query48.append(" 1270, 'Intrahandelsstatistik', 120, 1, 'SEL_INTRAHANDELSSTATISTIK',");
		query48.append("CURRENT TIMESTAMP ,");
		query48.append("'"+loginUserName+"') ");
		
		
		log.info("Create Entries in O_MODULE Table - These have been introduced in Release 2.2");
		
		StringBuilder query49 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODULE");
		query49.append(" (MODULE_ID, MODULE_NAME, DESCRIPTION, ISACTIVE, CREATED_TS, CREATED_BY ) values ( ");
		query49.append(" 130, 'Inventur', 'Inventur', 1 ,");
		query49.append("CURRENT TIMESTAMP ,");
		query49.append("'"+loginUserName+"') ");
		
		log.info("Create Entries in O_MODSEC Table - These have been introduced in Release 2.2");
		
		StringBuilder query50 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query50.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query50.append(" 1300, 'Zähllisten aufbauen', 130, 1, 'INV_ZAEHLLISTEN_AUFBAUEN',");
		query50.append("CURRENT TIMESTAMP ,");
		query50.append("'"+loginUserName+"') ");
		
		StringBuilder query51 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query51.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query51.append(" 1310, 'Inventur Starten', 130, 1, 'INV_INVENTUR_STARTEN',");
		query51.append("CURRENT TIMESTAMP ,");
		query51.append("'"+loginUserName+"') ");
		
		StringBuilder query52 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query52.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query52.append(" 1320, 'Zähllisten erfassen', 130, 1, 'INV_ZAEHLLISTEN_ERFASSEN',");
		query52.append("CURRENT TIMESTAMP ,");
		query52.append("'"+loginUserName+"') ");
		
		StringBuilder query53 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query53.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query53.append(" 1330, 'Inventur Ergebnis', 130, 1, 'INV_INVENTUR_ERGEBNIS',");
		query53.append("CURRENT TIMESTAMP ,");
		query53.append("'"+loginUserName+"') ");
		
		StringBuilder query54 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query54.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query54.append(" 1340, 'Inventur abschließen', 130, 1, 'INV_INVENTUR_ABSCHLIESSEN',");
		query54.append("CURRENT TIMESTAMP ,");
		query54.append("'"+loginUserName+"') ");
		
		StringBuilder query55 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query55.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query55.append(" 1350, 'Druckfunktionen', 130, 1, 'INV_DRUCKFUNKTIONEN',");
		query55.append("CURRENT TIMESTAMP ,");
		query55.append("'"+loginUserName+"') "); 
				
		StringBuilder query56 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query56.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query56.append(" 1360, 'Inventur Übersicht', 130, 1, 'INV_UBERSICHT',");
		query56.append("CURRENT TIMESTAMP ,");
		query56.append("'"+loginUserName+"') ");
		
		StringBuilder query57 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query57.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query57.append(" 750, 'Mengen- und Preiskorrektur', 70, 1, 'WE_MENGENUNDPREISKORR',");
		query57.append("CURRENT TIMESTAMP ,");
		query57.append("'"+loginUserName+"') ");
		
		StringBuilder query58 = new StringBuilder(" INSERT INTO ").append(schema).append(".O_MODSEC");
		query58.append(" (MODSEC_ID, SECTION_NAME, MODULE_ID, ISACTIVE, KEY, CREATED_TS, CREATED_BY) values ( ");
		query58.append(" 760, 'Rechnungsprüfung', 70, 1, 'WE_RECHNUNGSPRUEFUNG',");
		query58.append("CURRENT TIMESTAMP ,");
		query58.append("'"+loginUserName+"') ");
				
		stmt.addBatch(query1.toString());
		stmt.addBatch(query2.toString());
		stmt.addBatch(query3.toString());
		stmt.addBatch(query4.toString());
		stmt.addBatch(query5.toString());
		stmt.addBatch(query6.toString());
		stmt.addBatch(query7.toString());
		stmt.addBatch(query8.toString());
		stmt.addBatch(query9.toString());
		stmt.addBatch(query10.toString());
		stmt.addBatch(query11.toString());
		stmt.addBatch(query12.toString());
		stmt.addBatch(query13.toString());
		stmt.addBatch(query14.toString());
		stmt.addBatch(query15.toString());
		stmt.addBatch(query16.toString());
		stmt.addBatch(query17.toString());
		stmt.addBatch(query18.toString());
		stmt.addBatch(query19.toString());
		stmt.addBatch(query20.toString());
		stmt.addBatch(query21.toString());
		stmt.addBatch(query22.toString());
		stmt.addBatch(query23.toString());
		stmt.addBatch(query24.toString());
		stmt.addBatch(query25.toString());
		stmt.addBatch(query26.toString());
		stmt.addBatch(query27.toString());
		stmt.addBatch(query28.toString());
		stmt.addBatch(query29.toString());
		stmt.addBatch(query30.toString());
		stmt.addBatch(query31.toString());
		stmt.addBatch(query32.toString());
		stmt.addBatch(query33.toString());
		stmt.addBatch(query331.toString());
		stmt.addBatch(query332.toString());
		stmt.addBatch(query333.toString());
		stmt.addBatch(query34.toString());
		stmt.addBatch(query35.toString());
		stmt.addBatch(query36.toString());
		stmt.addBatch(query37.toString());
		stmt.addBatch(query38.toString());
		stmt.addBatch(query39.toString());
		stmt.addBatch(query40.toString());
		stmt.addBatch(query41.toString());
		stmt.addBatch(query42.toString());
		stmt.addBatch(query43.toString());
		stmt.addBatch(query44.toString());
		stmt.addBatch(query45.toString());
		stmt.addBatch(query46.toString());
		stmt.addBatch(query47.toString());
		stmt.addBatch(query48.toString());
		stmt.addBatch(query49.toString());
		stmt.addBatch(query50.toString());
		stmt.addBatch(query51.toString());
		stmt.addBatch(query52.toString());
		stmt.addBatch(query53.toString());
		stmt.addBatch(query54.toString());
		stmt.addBatch(query55.toString());
		stmt.addBatch(query56.toString());
		stmt.addBatch(query57.toString());
		stmt.addBatch(query58.toString());
		
	}
}
