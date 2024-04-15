/**
 * 
 */
package com.alphax.common.exception;

/**
 * @author A106744104
 *
 */
public class ExceptionMessages {

	public static final String INVALID_XML = "XML is not valid ";

	public static final String AUTHENTICATION_FAILED = "Authentication failed";

	public static final String RETURN_MESSAGE_FROM_OROLLE = "Message from OROLLE";

	public static final String SUCCESS_MESSAGE_KEY = "com.alphax.api.success";
	public static final String LOGIN_SUCCESS_MSG_KEY = "com.alphax.api.login.success";
	public static final String LOGOUT_SUCCESS_MSG_KEY = "com.alphax.api.logout.success";
	public static final String LOGIN_WARNING_MSG_KEY = "com.alphax.api.login.warn";

	public static final String AUTH_FAILED_MSG_KEY = "com.alphax.api.authentication.fail";
	public static final String COBOL_TIMEOUT_MSG_KEY = "com.alphax.api.cobol.timeout.error";
	public static final String COBOL_COMMAND_FAIL_MSG_KEY = "com.alphax.api.cobol.command.fail";
	public static final String COBOL_PROGRAM_FAIL_MSG_KEY = "com.alphax.api.cobol.program.fail";
	public static final String DB_CALL_FAIL_MSG_KEY = "com.alphax.api.db.operation.fail";

	public static final String SEARCH_FAILED_MSG_KEY = "com.alphax.api.controller.search.fail";
	public static final String GET_LIST_EMPTY_MSG_KEY = "com.alphax.controller.list.empty";
	public static final String GET_LIST_FAILED_MSG_KEY = "com.alphax.controller.list.fail";
	public static final String CREATE_FAILED_MSG_KEY = "com.alphax.controller.create.fail";
	public static final String GET_DETAILS_FAILED_MSG_KEY = "com.alphax.controller.get.details.fail";
	public static final String UPDATE_FAILED_MSG_KEY = "com.alphax.controller.update.fail";

	public static final String LOGIN_FAILED_MSG_KEY = "com.alphax.api.controller.env.login.fail";
	public static final String SCHEMA_NOT_FOUND_MSG_KEY = "com.alphax.api.controller.env.schema.invalid";
	public static final String CREDENTIALS_INVALID_MSG_KEY = "com.alphax.api.controller.credential.invalid";
	public static final String DATA_LIBRARY_INVALID_MSG_KEY = "com.alphax.api.controller.credential.invalid";
	public static final String OROLLE_INVALID_MSG_KEY = "com.alphax.api.controller.orolle.invalid";
	public static final String ENV_SETUP_MSG_KEY = "com.alphax.api.controller.env.fail";
	public static final String PERMISSION_FAILED_MSG_KEY = "com.alphax.api.controller.permission.fail";

	public static final String CUSTOMER_ONE_FAILED_MSG_KEY = "com.alphax.api.controller.customer.one.fail";
	public static final String CUSTOMER_EDIT_FLAG_FAILED_MSG_KEY = "com.alphax.api.controller.customer.flag.fail";
	public static final String CUSTOMER_EDIT_FLAG_INVALID_MSG_KEY = "com.alphax.api.controller.customer.edit.flag.invalid";
	public static final String CUSTOMER_AFFECT_FLAG_INVALID_MSG_KEY = "com.alphax.api.controller.customer.affect.flag.invalid";

	public static final String AUTHORITY_FLAG_FAIL_MSG_KEY = "com.alphax.api.controller.authority.flag.fail";
	public static final String DEFAULT_DATA_FAIL_MSG_KEY = "com.alphax.api.controller.customer.default.fail";
	public static final String VEHICLE_DATA_NOT_FOUND_MSG_KEY = "com.alphax.api.controller.vehicle.default.not.found";
	public static final String VEHICLE_MARKE_NOT_FOUND_MSG_KEY = "com.alphax.api.controller.vehicle.marke.not.found";
	public static final String VEHICLE_DEFAULT_INFO_FAILED_MSG_KEY = "Get vehicles standard information call failed";

	public static final String MULTIPLE_RECORD_MSG_KEY = "com.alphax.api.controller.multiple.record.found";
	public static final String RECORD_NOT_FOUND_MSG_KEY = "com.alphax.api.controller.record.not.found";

	public static final String CUSTOMER_CREATED_MSG_KEY = "com.alphax.api.controller.customer.create.success";
	public static final String CUSTOMER_CREATE_FAIL_MSG_KEY = "com.alphax.api.controller.customer.create.fail";
	public static final String CUSTOMER_UPDATE_MSG_KEY = "com.alphax.api.controller.customer.update.success";
	public static final String CUSTOMER_UPDATE_FAIL_MSG_KEY = "com.alphax.api.controller.customer.update.fail";

	public static final String VEHICLE_CREATE_FAIL_MSG_KEY = "com.alphax.api.controller.vehicle.create.fail";
	public static final String LICENCE_DUPLICATE_MSG_KEY = "com.alphax.api.controller.licence.duplicate.error";
	public static final String VEHICLE_WAREHOUSE_NUMBER_MSG_KEY = "com.alphax.api.controller.vehicle.warehouseId.invalid";

	public static final String VIN_LENGTH_INVALID_MSG_KEY = "com.alphax.api.controller.vin.length.invalid";
	public static final String VIN_DUPLICATE_MSG_KEY = "com.alphax.api.controller.vin.duplicate.error";
	public static final String VIN_CHECKSUM_MSG_KEY = "com.alphax.api.controller.vin.checksum.error";

	public static final String COMPANYID_INVALID_MSG_KEY = "com.alphax.api.controller.companyId.invalid";
	public static final String AGENCYID_INVALID_MSG_KEY = "com.alphax.api.controller.agencyId.invalid";
	public static final String PGSIZE_INVALID_MSG_KEY = "com.alphax.api.controller.search.pgsize.invalid";
	public static final String VIN_INVALID_MSG_KEY = "com.alphax.api.controller.vin.invalid";
	public static final String CLIENT_IP_INVALID_MSG_KEY = "com.alphax.api.client.ip.invalid";
	public static final String WAREHOUSE_INVALID_MSG_KEY = "com.alphax.api.controller.warehouses.invalid";
	public static final String ALLOWED_AGENCYID_INVALID_MSG_KEY = "com.alphax.api.controller.allowed.agencys.invalid";

	public static final String TOKEN_EXPIRED_MSG_KEY = "com.alphax.api.token.expired";
	public static final String TOKEN_INVALID_MSG_KEY = "com.alphax.api.token.invalid";
	public static final String PARAMETERS_INVALID_MSG_KEY = "com.alphax.api.parameter.invalid";
	public static final String CONSTRAINT_VIOLATION_MSG_KEY = "com.alphax.api.constraint.violation";
	public static final String INVALID_FLAG_VALUE_MSG_KEY = "com.alphax.api.controller.deliveryNote.flag.invalid";
	public static final String PASSWORD_EXPIRED_MSG_KEY = "com.alphax.api.password.expired";

	public static final String DELIVERYNOTE_TO_BSN_FAILED_MSG_KEY = "com.alphax.api.controller.deliveryNoteTo.bsn.failed";
	public static final String DELIVERYNOTE_FROM_BSN_TO_ETSTAMM_FAILED_MSG_KEY = "com.alphax.api.controller.deliveryNoteFrom.bsn.to.etStamm.failed";
	public static final String DELETE_DELIVERYNOTE_FAILED_MSG_KEY = "com.alphax.api.controller.deliveryNote.delete.failed";
	public static final String DELETE_ANN_CANCELED_DLIVRYNTE_FAILED_MSG_KEY = "com.alphax.api.controller.announcecancel.deliveryNote.delete.failed";

	public static final String SEARCH_BY_FILTER_INVALID_MSG_KEY = "com.alphax.api.controller.filter.searchBy.name.invalid";
	public static final String INVALID_MSG_KEY = "com.alphax.api.controller.invalid.value";
	public static final String DUPLICATE_ORDER_NUMBER_MSG_KEY = "com.alphax.api.controller.duplicate.order.number";

	public static final String ADD_PARTS_ORDER_BASKET_FAILED = "com.alphax.api.controller.order.part.failed";
	public static final String GENERATE_ORDER_NUMBER_FAILED = "com.alphax.api.controller.create.ordernumber.failed";
	public static final String INVALID_DATE_FORMAT_MSG_KEY = "com.alphax.controller.invalid.date.format";

	public static final String SIMULATION_PROCESS_SUCCESS_MSG_KEY = "com.alphax.controller.simulation.process.success";
	public static final String SIMULATION_PROCESS_FAILED_MSG_KEY = "com.alphax.controller.simulation.process.failed";
	public static final String AUTO_PROCESS_FAILED_MSG_KEY = "com.alphax.controller.auto.process.failed";
	public static final String SIMULATION_CONFLICT_PROCESS_FAILED_MSG_KEY = "com.alphax.controller.simulation.conflict.process.failed";

	public static final String ORDER_TRANSFER_MANDATORY_MSG_KEY = "com.alphax.controller.order.transferET.mandatory.failed";
	public static final String ORDER_TRANSFER_ET_FAILED_MSG_KEY = "com.alphax.api.controller.order.transferto.etStamm.failed";
	public static final String ORDER_PART_REPLACEMENT_MSG_KEY = "com.alphax.api.controller.order.part.replacement.faild";

	public static final String BSN_TO_ETSTAMM_SUCCESS_MSG_KEY = "com.alphax.controller.deliveryNoteFrom.bsn.to.etStamm.success";

	public static final String BUSINESS_CASES_50_FAILED_MSG_KEY = "com.alphax.controller.business.cases.50.failed";
	public static final String BUSINESS_CASES_67_FAILED_MSG_KEY = "com.alphax.controller.business.cases.67.failed";
	public static final String BUSINESS_CASES_01_FAILED_MSG_KEY = "com.alphax.controller.business.cases.01.failed";
	public static final String BUSINESS_CASES_05_FAILED_MSG_KEY = "com.alphax.controller.business.cases.05.failed";
	public static final String BUSINESS_CASES_17_01FAILED_MSG_KEY = "com.alphax.controller.business.cases.17_01.failed";
	public static final String BUSINESS_CASES_17_02FAILED_MSG_KEY = "com.alphax.controller.business.cases.17_02.failed";

	public static final String DELETE_ORDERS_FAILED_MSG_KEY = "com.alphax.api.controller.orders.delete.failed";

	public static final String BA09_VALIDATION_FAILED_MSG_KEY = "com.alphax.controller.business.cases.09.failed";
	public static final String BSN477CL_PROGRAM_FAILED_MSG_KEY = "com.alphax.controller.business.cases.program.bsn477.failed";
	public static final String PROGRAM_FAILED_MSG_KEY = "com.alphax.controller.part.cobol.program.fail";

	public static final String PART_RELOCATION_LAGER_EMPTY_MSG_KEY = "com.alphax.controller.part.relocation.fail";
	public static final String PART_RELOCATION_MOVEMET_TYPE_MSG_KEY = "com.alphax.controller.order.part.movement.error";

	public static final String DELETE_ADMIN_SETTING_FAILED_MSG_KEY = "com.alphax.api.controller.admin.setting.delete.failed";

	public static final String CHECK_ESKDAT_ORDERS_FAILED_MSG_KEY = "com.alphax.controller.check.orders.fail";

	public static final String CHECK_DAILY_CLOSING_PROCESS_MSG_KEY = "com.alphax.check.daily.closing.process";
	public static final String CHECK_INSTALLATIO_UPDATE_PROCESS_MSG_KEY = "com.alphax.check.installation.update.process";
	public static final String CHECK_END_OF_YEAR_PROCESS_MSG_KEY = "com.alphax.check.end.of.year.process";
	public static final String CHECK_MAINTENANCE_WORK_PROCESS_MSG_KEY = "com.alphax.check.maintenance.work.process";

	public static final String LOGOUT_FAILED_MSG_KEY = "com.alphax.api.logout.fail";

	public static final String INVALID_USER_LOGIN_MSG_KEY = "com.alphax.controller.invalid.user.login";
	public static final String INVALID_USER_ROLE_AGENCY_MSG_KEY = "com.alphax.controller.invalid.user.roleAndAgency";

	public static final String UNIQUE_CONSTRAINT_FAILED_MSG_KEY = "com.alphax.api.controller.unique.constraint.fail";
	public static final String PART_ALREADY_EXISTS_KEY = "com.alphax.api.controller.add.part.fail";

	public static final String ALPHAX_SETUP_FAILED_MSG_KEY = "com.alphax.api.controller.alphax.setup.fail";

	public static final String GET_SELECTION_DETAILS_FAILED_MSG_KEY = "com.alphax.controller.get.selection.details.fail";

	public static final String INVENTORY_ARCHIVED_FILE_CHECK_FAILED_MSG_KEY = "com.alphax.controller.inventory.archived.file.check.fail";
	public static final String INVENTORY_ARCHIVED_DATA_CHECK_FAILED_MSG_KEY = "com.alphax.controller.inventory.archived.data.check.fail";

	public static final String INVENTORY_PDF_DOWNLOAD_FAILED_MSG_KEY = "com.alphax.controller.inventory.pdf.download.fail";

	public static final String PARTS_LIST_ALREADY_EXISTS_MSG_KEY = "com.alphax.api.controller.add.parts.list.fail";
	public static final String ADD_COUNTED_PART_STORAGE_LOCATION_FAILED_MSG_KEY = "com.alphax.api.controller.storage.location.fail";
	public static final String ADD_COUNTED_PART_MASTER_DATA_FAILED_MSG_KEY = "com.alphax.api.controller.part.master.data.fail";
	public static final String ADD_COUNTED_PART_STORAGE_MASTER_DATA_FAILED_MSG_KEY = "com.alphax.api.controller.storage.master.data.fail";

	public static final String COUNTING_LIST_STATUS_FAILED_MSG_KEY = "com.alphax.api.controller.inventory.counting.list.status.fail";

	public static final String BUSINESS_CASE_49_CONVERSION_FAILED_MSG_KEY = "com.alphax.api.controller.BA49.conversion.fail";

	public static final String REPORT_EMPTY_MSG_KEY = "com.alphax.api.controller.report.record.not.found";

	public static final String BUSINESS_CASES_25_FAILED_MSG_KEY_1 = "com.alphax.controller.ba.25.delivery.note.done.failed";
	public static final String BUSINESS_CASES_25_FAILED_MSG_KEY_2 = "com.alphax.controller.ba.25.delivery.note.closed.failed";
	public static final String BUSINESS_CASES_25_FAILED_MSG_KEY_3 = "com.alphax.controller.ba.25.delivery.note.not.available.failed";
	public static final String BUSINESS_CASES_25_FAILED_MSG_KEY_4 = "com.alphax.controller.ba.25.delivery.note.create.auto.failed";
	public static final String BUSINESS_CASES_25_PRICE_CHECK_FAILED_MSG_KEY = "com.alphax.controller.ba.25.price.check.failed";
	public static final String BUSINESS_CASES_25_FAILED_MSG_KEY_5 = "com.alphax.controller.ba.25.Marke.check.failed";
	
	public static final String LOGFILE_INVALID_ACCESS = "com.alphax.api.logfile.invalid.access";
	public static final String PARTS_NOT_EXISTS_MSG_KEY = "com.alphax.api.controller.parts.check.fail";

}