package com.alphax.util;

public class RestURIConstants {

	public static final String GET_DEALER_NAME = "/dealer/name/{input}";
	public static final String GET_DEALER_BY_ID = "/dealer/{id}";
	public static final String ADD_NEW_DEALER = "/dealer";
	public static final String UPDATE_EXISTING_DEALER = "/dealer";
	public static final String DELETE_DEALER_BY_ID = "/dealer/{id}";

	public static final String GET_DEALER_ADDRESS = "/dealer/address";

	public static final String MANDANT_DETAILS = "/mandant/details";
	
	public static final String GET_COMPANY = "/company/{id}";
	
	public static final String CUSTOMER = "/customer";
	public static final String CUSTOMER_DEFAULT_DATA = "/customers/defaultData";
	
	public static final String GET_CUSTOMER_DETAILS = "/customer/{id}";
	
	public static final String CUSTOMER_AKQUISITIONSKZ = "/customer/akquisitionskz";
	
	public static final String CUSTOMER_BRANCHENSCHLUSSEL = "/customer/branchenschlussel";
	
	public static final String CUSTOMER_ANREDESCHLUSSEL = "/customer/anredeschlussel";
	
	public static final String CUSTOMER_ONE_CHECK = "/customer/customerOne";
	public static final String USER_PERMISSIONS = "/permissions";
	public static final String USER_INFORMATION = "/user/standardInfo";
	public static final String USER_COMPANY_INFORMATION = "/user/companyInfo";
	
	public static final String AUTHORITY_FLAG = "/authority/flag";
	
	public static final String CUSTOMER_MEHRWERTSTEUER = "/customer/mehrwertsteuer";
	public static final String CUSTOMER_SKONTO = "/customer/skonto";
	public static final String CUSTOMER_ZUABSCHLAG = "/customer/zuAbschlag";
	public static final String CUSTOMER_RABATTKZ = "/customer/rabattkz";
	public static final String CUSTOMER_ELEKRECHNUNG = "/customer/elektRechnung"; 
	public static final String CUSTOMER_XMLDATEN = "/customer/xmlDaten"; 
	public static final String CUSTOMER_HERKUNFT = "/customer/herkunft"; 
	public static final String CUSTOMER_LAND = "/customer/land"; 
	public static final String CUSTOMER_BONITATE = "/customer/bonitate"; 
	public static final String CUSTOMER_DRUCKER = "/customer/drucker"; 
	public static final String CUSTOMER_ANREDE = "/customer/anrede"; 
	public static final String CUSTOMER_VEHICLE = "/customer/{id}/vehicles"; 
	
	public static final String VEHICLES = "/vehicles";
	public static final String VEHICLES_STANDARD_INFO = "/vehicles/specificData";
	
	public static final String VEHICLES_MARKE = "/vehicles/marke";
	public static final String VEHICLES_ZULASSUNGSART = "/vehicles/zulassungsart";
	public static final String VEHICLES_ANTRIEBSART = "/vehicles/antriebsart";
	public static final String VEHICLES_LAUFLEISTUNG = "/vehicles/laufleistung";
	public static final String VEHICLES_VERKAUFER = "/vehicles/verkaufer";
	public static final String VEHICLES_VERKAUFSSPARTE = "/vehicles/verkaufssparte";
	public static final String VEHICLES_AKQUISITIONSPERRE = "/vehicles/akquisitionssperre";
	public static final String VEHICLES_WARTGRUPPE = "/vehicles/wartGruppe";
	public static final String VEHICLES_KUNDENDIENST = "/vehicles/kundendienst-berater";
	public static final String VEHICLES_BETREUENDE = "/vehicles/betreuende";
	public static final String VEHICLES_AKTION = "/vehicles/aktion";
	public static final String VEHICLES_KUNDEN_VEGA = "/vehicles/kundenVEGA";
	public static final String VEHICLES_KUNDEN_NR = "/vehicles/kundenNr";
	
	public static final String PARTS = "/parts";
	public static final String PARTS_OEM = "/parts/manufacturer";
	public static final String GET_PART_DETAILS = "/parts/{id}";
	public static final String GET_PARTS_PREISKZ = "/parts/preisKz";
	public static final String GET_PARTS_ABSCHLAGKZ = "/parts/abschlagKz";
	public static final String GET_PARTS_KATEGORIEN = "/parts/kategorien";
	public static final String GET_LAGERS_PART_DETAILS = "/parts/{id}/lager";
	public static final String ALTERNATIVE_PARTS_FOR_PART_SEARCH = "/parts/alternativeParts";
	public static final String GET_MOVEMENT_DATA_DETAILS = "/parts/{id}/movementdata";
	public static final String GET_LOPA_DETAILS_LIST = "/parts/{id}/lopa";
	public static final String GET_DISCOUNT_GROUP_AND_VALUE_LIST = "/parts/groupValues";
	public static final String GET_MEASURED_VALUE_OF_THE_EXTERNAL_ROLLING_LIST = "/parts/measuredValue";
	public static final String GET_TIRE_CLASSIFICATION_LIST = "/parts/tireClassification";
	public static final String GET_PARTS_DECODE_MC = "/parts/decodeMC";
	public static final String GET_PART_STOCKS_DETAILS = "/parts/{id}/stocksInfo";
	public static final String CONVERT_PARTNO_FORMATS = "/parts/convert/newPartNoFormat";
	public static final String LOCAL_ALTERNATIVES_FOR_TREEVIEW = "/parts/localAlternativeTreeview";
	public static final String GET_BASEDATA_FOR_BA_POPUP = "/parts/baseDataForBA";
	public static final String PARTS_GLOBAL_SEARCH = "/parts/globalSearch";
	public static final String PARTS_LABEL_PRINTING = "/parts/report/csv/labelPrinting";
	public static final String PARTS_GLOBAL_SEARCH_PRICE = "/parts/globalSearch/calculatedPrice";
	public static final String REQUIRED_WAREHOUSE_LIST = "/parts/requiredLager";
	
	public static final String GET_VEHICLE_DETAILS = "/vehicles/{id}";
	public static final String GET_VEHICLE_LEGENDS = "/vehicles/{id}/vehicleLegend";
	public static final String VEHICLE_DUPLICATE_LICENCE = "/vehicles/duplicateLicence";
	public static final String VEHICLE_DUPLICATE_VIN = "/vehicles/duplicateVIN";
	public static final String GET_VEHICLE_DATAMAP = "/vehicles/{id}/vehicleDataMap";
	public static final String VEHICLE_LEGEND_REPAIRCODE = "/vehicles/vehicleLegend/repkz";
	
	public static final String DASHBOARD_WARENEINGANG_COUNTS = "/dashboard/wareneingang";
	public static final String DASHBOARD_DAILYUPDATE_COUNTS = "/dashboard/dailyupdate";
	public static final String ADMIN_CONFIGURATION_SETUP= "/dashboard/setupKeys";
	public static final String CREATE_O_SETUP_TABLE= "/dashboard/createTable";
	public static final String AUTOMATIC_DELIVERYNOTE_PROCESSING= "/dashboard/automaticProcessing";
	
	
	public static final String GET_DELIVERY_NOTE_LIST = "/incominggoods/deliverynote";
	public static final String GET_DELIVERY_NOTE_DETAILS_LIST = "/incominggoods/deliverynotedetails";
	public static final String GET_BSN_DELIVERY_NOTE_LIST = "/incominggoods/bsndeliverynote";
	public static final String GET_BSN_DELIVERY_NOTE_DETAILS_LIST = "/incominggoods/bsndeliverynotedetails";
	public static final String UPDATE_DELIVERY_NOTE_SPARE_PART = "/incominggoods/deliverynote/sparepart";
	public static final String DELIVERY_NOTE_TO_BSN = "/incominggoods/deliverynoteTobsn";
	public static final String DELIVERY_NOTE_TO_ET_STAMM = "/incominggoods/deliverynoteFromBSNToETStamm";
	public static final String GET_DELIVERY_NOTE_LIST_BASED_ON_FILTER = "/incominggoods/filter/deliverynote";
	public static final String GET_BSN_DELIVERY_NOTE_LIST_BASED_ON_FILTER = "/incominggoods/filter/bsndeliverynote";
	public static final String ANNOUNCED_CANCELED_DELIVERY_NOTE = "/incominggoods/deliverynote/announceCancel";
	public static final String GET_FILTER_VALUE_DELIVERY_NOTE = "/incominggoods/filterValues";
	public static final String GET_CONFLICT_DELIVERY_NOTE_LIST = "/incominggoods/conflictdeliverynote";
	public static final String GET_CONFLICT_DELIVERY_NOTE_DETAILS_LIST = "/incominggoods/conflictdeliverynotedetails";
	public static final String CREATE_DELIVERY_NOTE_HISTORY = "/incominggoods/history";
	public static final String GET_CONFLICT_RESOLUTION = "/incominggoods/conflictResolution";
	public static final String GET_SOLVED_CONFLICT_DN_LIST = "/incominggoods/solvedconflictDN";
	public static final String BACKLOG_RESOLUTION = "/incominggoods/backlogResolution";
	public static final String AUDIT_NOT_ASSIGNED_DELIVERY_NOTES = "/incominggoods/invoice/notAssignedDeliveryNotes";
	public static final String AUDIT_ASSIGNED_DELIVERY_NOTES = "/incominggoods/invoice/assignedDeliveryNotes";
	public static final String UPDATE_INVOICE_DELIVERY_NOTE = "/incominggoods/invoice/updateInvoiceDeliveryNoteAsApproved";
	public static final String RETURN_COUNT_OF_NOT_IMPORTED_INCOICE_ITEM = "/incominggoods/invoice/countOfNotImportedInvoiceItem";
	public static final String SET_ASSIGN_DELIVERY_NOTE_FOR_INVOICE = "/incominggoods/invoice/setAssignDeliveryNoteForInvoice";
	public static final String UPDATE_INVOICE_AMOUNT_DELIVERY_NOTE = "/incominggoods/invoice/updateInvoiceAmountForDeliveryNote";
	public static final String GET_WAREHOUSE_LIST_FOR_INVOICE ="/incominggoods/invoice/warehouseList";
	
	public static final String GET_INVOICE_BOOKING_TEXT_LIST = "/incominggoods/invoice/bookingTexts";
	public static final String GET_INVOICE_NUMBER_LIST = "/incominggoods/invoicesNumber";
	public static final String RECORD_DIFFERENCES = "/incominggoods/recordDifferences";
	public static final String CORRECTION_IN_BOOKING = "/incominggoods/bookingCorrections";
	public static final String DIFFERENCES_VALUE = "/incominggoods/differencesValue";
	public static final String CREATE_INVOICE_ENTRY_MANUALLY = "/incominggoods/invoice/createInvoiceManually";
	
	public static final String ADDITIONAL_COSTS = "/incominggoods/additionalCost";
	public static final String IMPORT_INVOICE_DATA_FROM_O_ARPDC = "/incominggoods/importInvoiceData";
	public static final String GET_INVOICE_OVERVIEW = "/incominggoods/invoiceOverview";
	public static final String GET_INVOICE_DETAILS = "/incominggoods/invoiceDetails";
	public static final String GET_BOOKED_DELIVERY_NOTE_DETAILS = "/incominggoods/bookedDeliveryNoteDetails";
	
	public static final String RESET_EXECUTE_ORDER = "/orders/resetExecuteOrder";
	public static final String ORDERS_BASKET = "/orders/orderbasket";
	public static final String ORDERS_BASKET_DETAILS = "/orders/{id}/orderbasketdetails";
	public static final String ORDERS_TEILMARKE = "/orders/teilemarke";
	public static final String GET_ORDERS_AUFTRAGSART = "/orders/auftragsart";
	public static final String GET_ORDERS_ABGEBENDES_LAGER_BA = "/orders/abgebendesLagerBA";
	public static final String GET_ORDERS_EMPFANGENDES_LAGER_BA = "/orders/empfangendesLagerBA";
	public static final String ORDERS_GET_PARTS = "/orders/parts";
	public static final String GET_PART_RELOCATION_DETAILS = "/orders/partRelocation";
	public static final String CHECK_DUPLICATE_ORDER_NUMBER = "/orders/duplicateOrderNumber";
	public static final String ALTERNATIVE_PARTS_DETAILS = "/orders/alternativeParts";
	public static final String GET_ORDERNUMBER = "/orders/orderNumber";
	public static final String ORDERS = "/orders";	
	public static final String GET_ALTERNATIVE_PARTS_AVAILABILITY = "/orders/alternativePartsAvailability";
	public static final String ORDERS_DETAILS = "/orders/{id}/orderdetails";
	public static final String GET_ORDERS_LIST_BASED_ON_FILTER = "/orders/filter";
	public static final String GET_FILTER_VALUE_ORDER = "/orders/filterValues";
	public static final String TRANSFER_ORDERS_TO_ET = "/orders/ordersToETStamm";
	public static final String GET_ORDERS_BASKET_BASED_ON_FILTER = "/orders/orderbasket/filter";
	public static final String CREATE_ORDERS_HISTORY = "/orders/history";
	public static final String GET_ORDERS_HISTORY_BASED_ON_FILTER = "/orders/history/filter";
	public static final String CHECK_ESKDAT_ORDERS = "/orders/checkOrders";
	public static final String GET_WAREHOUSE_LIST_FOR_PART_MOVEMENT = "/orders/warehouseDetails";
	public static final String ORDERS_GET_PARTS_LIST = "/orders/partslist";
	public static final String ORDERS_RECREATE_E36 = "/orders/recreateE36";
	public static final String CHECK_PART_MOVEMENT = "/orders/checkPartmovement";
	public static final String PARTS_DETAILS_FOR_TREEVIEW = "/orders/partsTreeview";
	
	public static final String ADMIN_COMPANY = "/admin/company";
	public static final String GET_ADMIN_COMPANY_DETAILS = "/admin/{id}/company";
	public static final String ADMIN_USERS = "/admin/user";
	public static final String GET_ADMIN_USER_DETAILS = "/admin/{id}/user";
	public static final String ADMIN_ROLES = "/admin/roles";
	public static final String GET_ADMIN_ROLE_DETAILS = "/admin/{id}/role";
	public static final String ADMIN_MODULE = "/admin/module";
	public static final String GET_ADMIN_MODULE_DETAILS = "/admin/{id}/module";
	public static final String ADMIN_AGENCY = "/admin/agency";
	public static final String GET_ADMIN_AGENCY_DETAILS = "/admin/{id}/agency";
	public static final String ADMIN_LOGIN_USERS = "/admin/loginusers";
	public static final String USER_ROLE_AND_AGENCY_MAPPING = "/admin/mapping/userRole";
	public static final String AGENCY_WAREHOUSE_MAPPING = "/admin/mapping/agencyWarehouse";
	public static final String USER_NOT_ASSIGNED_ANY_ROLE_TO_CURRENT_AGENCY = "/admin/mapping/usersNotAssigned";
	public static final String USER_ASSIGNED_SELECTED_ROLE_TO_CURRENT_AGENCY = "/admin/mapping/usersAssigned";
	public static final String WAREHOUSE_NOT_ASSIGNED_TO_CURRENT_AGENCY = "/admin/mapping/warehousesNotAssigned";
	public static final String WAREHOUSE_ASSIGNED_TO_CURRENT_AGENCY = "/admin/mapping/warehousesAssigned";
	public static final String GET_ADMIN_AGENCY_TOKEN_LIST = "/admin/agency/token";
	public static final String GET_ADMIN_AGENCY_TOKEN = "/admin/agency/assignedToken";
    public static final String ALPHAX_COMPANY = "/admin/alphax/company";
    public static final String GET_ADMIN_USER_DEFAULT_SETTING = "/admin/{id}/userSetting";
    public static final String UPDATE_USER_DEFAULT_SETTING = "/admin/userSetting";
    public static final String GET_EMPLOYEE_ROLE_LIST = "/admin/employeeroles";
    
    public static final String AGENCY_NOT_ASSIGNED_TO_CURRENT_WAREHOUSE = "/admin/{id}/warehouse/agencyNotAssigned";
	public static final String AGENCY_ASSIGNED_TO_CURRENT_WAREHOUSE = "/admin/{id}/warehouse/agencyAssigned";
	
	public static final String ADMIN_WAREHOUSE = "/admin/warehouse";
	public static final String GET_ADMIN_WAREHOUSE_DETAILS = "/admin/{id}/warehouse";
	
	public static final String GET_ROLES_SETTINGS = "/admin/{id}/role/settings";
	public static final String GET_AP_WAREHOUSE_LIST = "/admin/apwarehouse";
	public static final String GET_ADMIN_COMPANY_FISCAL_DATE = "/admin/{id}/company/fiscaldate";
	
	public static final String ADMIN_PRINTER = "/admin/printer";
	public static final String ADMIN_PRINTER_TYPE = "/admin/printerType";
	public static final String ADMIN_PRINTER_DETAILS = "/admin/{id}/printer";
	public static final String PRINTER_AND_AGENCY_MAPPING = "/admin/mapping/printerAgency";	
	public static final String PRINTER_NOT_ASSIGNED_TO_CUR_AGENCY = "/admin/{id}/agency/printerNotAssigned";
	public static final String PRINTER_ASSIGNED_TO_CUR_AGENCY = "/admin/{id}/agency/printerAssigned";
	
	public static final String SIMULATE_DELIVERYNOTE = "/simulate/genrateDeliveryNote";
	public static final String SIMULATE_AUTOPROCESS = "/simulate/autoProcess";
	public static final String SIMULATE_CONFLICT_DELIVERYNOTE = "/simulate/genrateConflictdeliveryNote";
	
	public static final String FINALIZATIONS_BA = "/businessCases/finalizations";
	public static final String ACCESSES_BA = "/businessCases/accesses";
	public static final String DEPARTURES_BA = "/businessCases/departuresBA";
	public static final String MASTER_DATA_BA = "/businessCases/masterDataBA";
	public static final String GET_DELIVERY_NOTES = "/businessCases/deliverynote";
	public static final String GET_LAGER_LIST = "/businessCases/lager";
	public static final String GET_COMPANY_LAGER_LIST = "/businessCases/company/lager";
	public static final String GET_LIEFERANT_LIST = "/businessCases/lieferant";
	public static final String ACCESSES_BA_06 = "/businessCases/accessesBA06";
	public static final String DEPARTURES_BA_25 = "/businessCases/departuresBA25";
	public static final String ACCESSES_BA_TAXES = "/businessCases/accesses/taxes";
	public static final String GET_BA_CUSTOMERGROUP = "/businessCases/customergroup";
	public static final String REBOOKIN_GBUNDLES_BA = "/businessCases/rebooking";
	public static final String GET_ACTIVE_BA = "/businessCases/list";
	public static final String GET_DEFAULT_PRINTER = "/businessCases/defaultPrinter";
	public static final String BA_49 = "/businessCases/selectionBA49";
	public static final String FINALIZATIONS_BA_50 = "/businessCases/finalizationsBA50";
	public static final String ACCESSES_BA_17 = "/businessCases/accessesBA17";
	public static final String CLEANUP_PARTS = "/businessCases/partsCleanUp";
	public static final String GET_DETAILS_BA_17 = "/businessCases/accessesBA17";
	public static final String CREATE_PART_IN_MULTIPLE_WAREHOUSE = "/businessCases/createParts";
	public static final String BA_BSN_475 = "/businessCases/bsn475";
	
	
	public static final String GET_SELECTIONS_FROM_STOCK = "/selections/preview/stock";
	public static final String GENERATE_REPORT_FROM_STOCK = "/selections/report/stock";
	public static final String GET_SELECTIONS_BASED_MARKETABILITY = "/selections/preview/marketability";
	public static final String GENERATE_REPORT_BASED_MARKETABILITY = "/selections/report/marketability";
	public static final String GET_SELECTIONS_BASED_INVENTORY = "/selections/preview/inventory";
	public static final String GENERATE_REPORT_BASED_INVENTORY = "/selections/report/inventory";
	public static final String GET_SELECTIONS_BASED_STATISTICS_DAK = "/selections/preview/BCstatistics/dak";
	public static final String GET_SELECTIONS_BASED_STATISTICS_MANUAL_COUNT = "/selections/preview/BCstatistics/manualCount";
	public static final String GET_SELECTIONS_BASED_STATISTICS_MANUAL_CORRECTION = "/selections/preview/BCstatistics/manualCorrection";
	public static final String GET_SELECTIONS_BASED_STATISTICS_REDEMPTION = "/selections/preview/BCstatistics/redemption";
	public static final String GET_SELECTIONS_BASED_STATISTICS_WITHOUT_MOVEMENT = "/selections/preview/BCstatistics/withoutInventoryMovement";
	public static final String GENERATE_REPORT_FROM_MOVEMENT = "/selections/report/movement";
	public static final String GENERATE_REPORT_BASED_BA_STATISTICS = "/selections/report/BCstatistics";
	public static final String GET_SELECTIONS_FROM_MOVEMENT = "/selections/preview/movement";
	public static final String SEARCH_MOVEMENT = "/selections/search/movement";
	
	public static final String GENERATE_BOOKING_RELEVANT_LIST = "/selections/preview/bookingrelevantList";
	public static final String GENERATE_REPORT_BOOKING_RELEVANT_LIST = "/selections/report/bookingrelevantList";
	public static final String GENERATE_PARTS_SUPPLY_LIST = "/selections/preview/partSupply";
	public static final String GENERATE_REPORT_PARTS_SUPPLY_LIST = "/selections/report/partSupply";	
	public static final String GENERATE_INTRA_TRADE_STATS_LIST = "/selections/preview/intraTradeStats";
	public static final String GENERATE_REPORT_INTRA_TRADE_STATS = "/selections/report/intraTradeStats";
	public static final String GENERATE_BOOKING_RELEVANT_STOCK_DIFF_LIST = "/selections/preview/booking/stockDifferences";
	public static final String GENERATE_BOOKING_RELEVANT_ACCOUNT_CHANGE_LIST = "/selections/preview/booking/accountChange";
	public static final String GENERATE_REPORT_BOOKING_RELEVANT_STOCK_DIFF = "/selections/report/booking/stockDifferences";
	public static final String GENERATE_REPORT_BOOKING_RELEVANT_ACCOUNT_CHANGE = "/selections/report/booking/accountChange";
	public static final String GENERATE_REPORT_LABEL_PRINTING = "/selections/report/labelPrinting";
	public static final String GENERATE_REPORT_LABEL_PRINTING_CSV = "/selections/report/csv/labelPrinting";

	public static final String GET_LIEFERANT_LIST_BY_NUMBER_OR_NAME = "/businessCases/search/lieferant";

	public static final String LOGOUT = "/logout";
	public static final String KILL_ME = "/killMe";
	
	public static final String LOGS_INFO = "/logs";
	public static final String DOWNLOAD_LOGS = "/download/logs";
	
	public static final String LOGIN_AGENCY = "/company/loginAgency";

	public static final String ALPHAX_SETUP_LOGIN = "/authenticate/axSetup";

	public static final String PASSWORD_CHANGED = "/changePassword";

	
	public static final String ALPHAX_ADMIN_SETUP = "/axSetup/{adminUser}/admin";
	
	public static final String INVENTORY_LIST = "/inventory/allCreatedInventory";
	public static final String INVENTORY_DETAILS_LIST = "/inventory/inventoryDetails";
	//public static final String INVENTORY_OVERVIEW_LIST = "/inventory/inventoryOverview";
	public static final String INVENTORY_OVERVIEW_COUNT = "/inventory/inventoryOverviewCount";
	public static final String DASHBOARD_INVENTORY_OVERVIEW_COUNT = "/dashboard/inventoryOverviewCount";
	public static final String STARTED_INVENTORY_DISPLAY_LIST = "/inventory/startedInventoryDisplay";
	public static final String STARTED_INVENTORY_DETAILS_LIST = "/inventory/startedInventoryDetails";
	public static final String START_INVENTORY = "/inventory/start";
	public static final String WAREHOUSE_LIST_FOR_INVENTORY = "/inventory/warehouse";
	public static final String SUBMIT_INVENTORY_COUNT = "/inventory/submitInventoryCount";
	public static final String CLOSE_INVENTORY = "/inventory/close";
	public static final String ACTIVATED_COUNT_INV_LIST = "/inventory/activatedCount";
	public static final String ACTIVATED_COUNT_INV_LIST_DETAIL = "/inventory/{warehousId}/activatedCount";
	public static final String CREATED_COUNT_INV_LIST = "/inventory/createdCount";
	public static final String CREATED_COUNT_INV_LIST_DETAILS = "/inventory/{inventoryListId}/createdCount";
	
	public static final String GET_INVENTORY_REMARKABLE_ITEMS = "/inventory/inventoryItems";
	public static final String GET_INVENTORY_REMARKABLE_ITEMS_DETAILS = "/inventory/inventoryItemsDetails";
	public static final String GET_INVENTORY_ACCHIVED_DIFFERENCES = "/inventory/archivedDifferences";
	public static final String GET_INVENTORY_ACCHIVED_DIFFERENCES_IN_DETAILS = "/inventory/archivedDifferencesDetails";
	public static final String GET_LOPA_LIST = "/inventory/lopa";
	public static final String GET_COUNTING_GROUP_LIST = "/inventory/countingGroup";
	public static final String GET_PARTS_BRAND_LIST = "/inventory/partsBrand";
	public static final String GET_SORTING_AND_PRINT_SELECTION_LIST = "/inventory/sortingAndPrint";
	public static final String ACTIVATE_COUNTING_LIST= "/inventory/activateCountingList";
	public static final String GET_INVENTORY_ACCHIVED_COUNTING = "/inventory/archivedCounting";
	public static final String GET_INVENTORY_ACCHIVED_COUNTING_DETAILS = "/inventory/archivedCountingDetails";
	public static final String INVENTORY_STATUS = "/inventory/status";
	public static final String GET_ANSICHT_LIST = "/inventory/ansichtList";
	public static final String GET_DIFFERENTIAL_LIST = "/inventory/{inventoryListId}/differentialList";
	public static final String ADD_COUNTED_PART_MANUELLY = "/inventory/addCountedPartManuelly";
	public static final String DOWNLOAD_PDF_INVENTORY_PARTS = "/inventory/{inventoryListId}/downloadPDF";
	public static final String CREATED_COUNTING_LIST_VALUE = "/inventory/{inventoryListId}/createdCountingValue";
	public static final String GET_PRINTING_PAGEBREAK_LIST = "/inventory/printingPagebreak";
	public static final String UPDATE_COUNTING_LIST_VALUE = "/inventory/createdCountingValue";
	public static final String CREATED_COUNTING_LIST_ITEMS = "/inventory/createdCountingListItems";
	public static final String CREATE_NEW_COUNTING_LIST = "/inventory/createdNewCountingList";
	public static final String ADD_PARTS_IN_EXISTING_COUNTING_LIST = "/inventory/addPartsInExistingCountingList";
	public static final String ADD_DIFFERENTIAL_LIST = "/inventory/differentialList";
	public static final String PROCESS_COUNTINGLIST_AS_FULLY_COUNTED = "/inventory/processCountingList";
	public static final String DELETE_PART_FROM_COUNTING_LIST = "/inventory/{invPartId}/partFromCountingList";
	public static final String CLOSED_COUNT_INV_LIST = "/inventory/closedCountingList";
	public static final String CLOSED_COUNT_INV_LIST_DETAIL = "/inventory/{warehousId}/closedCountingList";
	public static final String DOWNLOAD_PDF_CLOSED_INVENTORY_PARTS = "/inventory/{inventoryListId}/downloadPDFClosedInv";
	public static final String MARK_DIFFERENTIAL_LIST_AS_FULLY_PROCESSED = "/inventory/markDifferentialLisAsfullyProcessed";
	public static final String COPY_INVENTORY_DTLS_INTO_ALPHAPLUS_HISTORY = "/inventory/copyInventoryDtlsIntoAlphaplusHistory";
	public static final String INVENTORY_STATUS_RECORDING_DATA = "/inventory/status/recordingData";
	public static final String DOWNLOAD_PDF_STS3_INVENTORY_PARTS = "/inventory/{inventoryListId}/downloadPDFSTS3";
	public static final String DOWNLOAD_PDF_INTERMEDIATE_DIFF = "/inventory/{inventoryListId}/downloadPDF/intermediateDiff";
	public static final String GET_SUM_DELTA_DIFFERENTIAL = "/inventory/{inventoryListId}/sumOfDeltaDifferntial";
	public static final String ADD_SINGLE_PART_IN_EXISTING_COUNTING_LIST = "/inventory/addSinglePartInExistingCountingList";
	public static final String GET_PART_NEGATION_STOCK_AMOUNT = "/inventory/partNegativeStock";
	
	public static final String DOWNLOAD_PDF_COUNTLESS_REJECTED_LIST = "/inventory/{inventoryListId}/downloadPDF/countlessRejectedList";
	public static final String DOWNLOAD_PDF_COUNTING_LIST_QUANTITY = "/inventory/{inventoryListId}/downloadPDF/countingListWithQuantity";
	public static final String DOWNLOAD_PDF_POSITIONS_WITHOUT_DIFF = "/inventory/{inventoryListId}/downloadPDF/positionsWithoutDiff";
	public static final String DOWNLOAD_PDF_ACCEPTED_DIFFERENT_POS = "/inventory/{inventoryListId}/downloadPDF/acceptedDiffPositions";
	public static final String DOWNLOAD_PDF_DIFFERENCE_SUM_POS = "/inventory/{inventoryListId}/downloadPDF/differenceSumPos";
	public static final String INVENTORY_STATUS_LIST = "/inventory/allInvStatus";
	public static final String GENERATED_COUNTING_LIST_NAME = "/inventory/countingListName";
	public static final String PARTLY_COUNTED_STORAGE_LOCATION = "/inventory/{inventoryListId}/partlyCountedStorageLocation";
	public static final String INVENTORY_LIST_TYPE = "/inventory/listType";
	public static final String WAREHOUSES_FRO_CREATED_COUNT_INV_LIST = "/inventory/createdCountList/warehouses";
	
	public static final String BUYBACK_OFFENE_HEADER_INFO = "/buyback/open/header";
	public static final String BUYBACK_OFFENE_HEADER_ITEM_INFO = "/buyback/open/headerDetails";
	public static final String BUYBACK_CLOSED_HEADER_INFO = "/buyback/closed/header";
	public static final String BUYBACK_CLOSED_HEADER_ITEM_INFO = "/buyback/closed/headerDetails";
	public static final String UPDATE_BUYBACK_FROM_DAVET2ONEAPI = "/buyback/refreshData";
	public static final String BUYBACK_COUNT_OF_DASHBOARD = "/buyback/dashboardCount";
	public static final String BUYBACK_UPDATE_FROM_HEAD_STATUS = "/buyback/{headerId}/openHeadStatus";
	public static final String BUYBACK_OFFENE_BOOKOUT = "/buyback/open/{headerId}/bookout";
	
	public static final String DOWNLOAD_PDF_OPEN_BBOF = "/buyback/downloadPDFOpenBBOF";
	public static final String DOWNLOAD_PDF_CLOSED_BBOF = "/buyback/downloadPDFClosedBBOF";
	
	public static final String ONEAPI_CUSTOMER_DETAILS = "/oneapi/{customerId}/customer";
	public static final String GET_ONEAPI_LIST = "/oneapi/list";
	public static final String CREATE_ONEAPI = "/oneapi";
	public static final String GET_VF_NUMBER = "/oneapi/vfNumbers";
	public static final String DELETE_ONEAPI_CONFIGURATION_DETAILS = "/oneapi/{customerId}/configuration";
	public static final String GET_APIURL = "/oneapi/apiurl";
	public static final String CREATE_EVOBUSAPI = "/oneapi/evobus";
	
	public static final String MSG_TYPE_SUCCESS = "Success";
	public static final String MSG_TYPE_ERROR = "Error";
	public static final String MSG_TYPE_INFO = "Information";
	public static final String MSG_TYPE_WARN = "Warning";
	 
}