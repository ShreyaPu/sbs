/**
 * 
 */
package com.alphax.service.mb.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.alphax.common.rest.message.service.MessageService;
import com.alphax.model.mb.BuybackHeaderInfo;
import com.alphax.model.mb.BuybackItemInfo;
import com.alphax.common.exception.AlphaXException;
import com.alphax.common.exception.ExceptionMessages;
import com.alphax.repository.DBServiceRepository;
import com.alphax.service.mb.BusinessCases;
import com.alphax.service.mb.BuybackProcessService;
import com.alphax.vo.mb.BusinessCases25DTO;
import com.alphax.vo.mb.BuybackHeaderDTO;
import com.alphax.vo.mb.BuybackHeaderDetailsDTO;
import com.alphax.vo.mb.DeparturesBA_DTO;
import com.alphax.common.constants.RestInputConstants;

import lombok.extern.slf4j.Slf4j;

/**
 * @author A87740971
 *
 */

@Service
@Slf4j
public class BuybackProcessServiceImpl extends BaseService implements BuybackProcessService  {

	@Autowired
	DBServiceRepository dbServiceRepository;

	@Autowired
	private MessageService messageService;
	
	@Autowired
	BusinessCases businessCases;


	/**
	 * This method is used to Return a list of BBOF Offene(Open) header information from DB
	 * @return Object
	 */	
	@Override
	public List<BuybackHeaderDTO> getBuybackOpenHeaderInfo(String schema, String allowedWarehouses, List<String> warehouseNos) {
		log.info("Inside getBuybackOpenHeaderInfo method of BuybackProcessServiceImpl");
		List<BuybackHeaderDTO> buybackHeaderList = new ArrayList<BuybackHeaderDTO>();
		try {

			//validate the Warehouse Ids
			validateWarehouses(allowedWarehouses);
			
			if(warehouseNos != null && !warehouseNos.isEmpty()){
				 warehouseNos.removeIf(s -> s == null || "".equals(s.trim()));
				allowedWarehouses = StringUtils.join(warehouseNos, ",");
			}
			
			StringBuilder buybackHeaderQuery = new StringBuilder(" SELECT BBOF_HEAD_ID, (SELECT WAREHOUS_ID FROM ").append(schema).append(".O_WRH WHERE ");
			buybackHeaderQuery.append("AP_WAREHOUS_ID = header.AP_LNR ) AS WAREHOUS_ID , (SELECT NAME FROM ").append(schema).append(".O_WRH WHERE ");
			buybackHeaderQuery.append("AP_WAREHOUS_ID = header.AP_LNR ) AS WAREHOUS_NAME , ORDERNO , VARCHAR_FORMAT(DELIVERYDATE, 'DD-MM-YYYY') AS DELIVERYDATE, ");
			buybackHeaderQuery.append("ORDERTYPE, BBOF_HEADER_STATE, AP_LNR FROM ").append(schema);
			buybackHeaderQuery.append(".O_BBOFHEAD header WHERE BBOF_HEADER_STATE IN (0,1) AND AP_LNR IN(").append(allowedWarehouses).append(") order by AP_LNR");

			List<BuybackHeaderInfo> headerInfoList = dbServiceRepository.getResultUsingQuery(BuybackHeaderInfo.class, buybackHeaderQuery.toString(), true);

			if(headerInfoList!=null && !headerInfoList.isEmpty()) {

				for(BuybackHeaderInfo headerInfoFromDB: headerInfoList) {
					BuybackHeaderDTO headerDTO = new BuybackHeaderDTO();

					headerDTO.setAlphaXWarehousId(String.valueOf(headerInfoFromDB.getAlphaXWarehousId()));
					headerDTO.setAlphaXWarehousName(headerInfoFromDB.getAlphaXWarehousName());
					headerDTO.setOrderNumber(headerInfoFromDB.getOrderNumber());
					headerDTO.setDeliveryDate(headerInfoFromDB.getDeliveryDate());
					headerDTO.setOrderType(headerInfoFromDB.getOrderType());
					headerDTO.setHeaderStatus(String.valueOf(headerInfoFromDB.getHeaderStatus()));
					headerDTO.setAlphaPlusWarehouseId(String.valueOf(headerInfoFromDB.getAlphaPlusWarehouseId()));
					headerDTO.setHeaderId(String.valueOf(headerInfoFromDB.getHeaderId()));

					buybackHeaderList.add(headerDTO);
				}
			}
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Buyback open Header Info"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY,
					"Buyback open Header Info"), exception);

			throw exception;
		}

		return buybackHeaderList;
	}


	/**
	 * This method is used to Return BBOF Offene(Open) header's item detail from DB
	 * @return Object
	 */	 
	@Override
	public List<BuybackHeaderDetailsDTO> getBuybackOpenHeaderItemDetails(String warehouseNo, String headerId, String schema, String dataLibrary) {
		log.info("Inside getBuybackOpenHeaderItemDetails method of BuybackProcessServiceImpl");
		List<BuybackHeaderDetailsDTO> buybackHeaderDetailsList = new ArrayList<BuybackHeaderDetailsDTO>();
		try {

			StringBuilder buybackHeaderDetailsQuery = new StringBuilder(" SELECT PARTNRI, PARTNRS, PARTNRE, (SELECT BENEN FROM ").append(dataLibrary);
			buybackHeaderDetailsQuery.append(".E_ETSTAMM WHERE TNR = item.PARTNRE  AND LNR =").append(warehouseNo).append(" AND HERST='DCAG' ) , ");
			buybackHeaderDetailsQuery.append("custRefPos,  concat (concat  (QTY, ' ' ),  (SELECT  UNIT FROM ").append(schema);
			buybackHeaderDetailsQuery.append(".O_BBOFUNIT WHERE  ENUM=item.QTYUNIT ) )  As RequestedQuantity, (SELECT  AKTBES  FROM ").append(dataLibrary);
			buybackHeaderDetailsQuery.append(".E_ETSTAMM WHERE TNR=item.PARTNRE AND LNR =").append(warehouseNo);
			buybackHeaderDetailsQuery.append(" AND HERST='DCAG' ), BBOF_ITEM_STATE, SEND_QTY, QTY, BBOF_ITEM_ID FROM ").append(schema).append(".O_BBOFITEM ").append("item ");
			buybackHeaderDetailsQuery.append(" where BBOF_HEAD_ID =").append(headerId).append(" and BBOF_ITEM_STATE IN (0,1) order by BBOF_ITEM_ID");

			List<BuybackItemInfo> itemDetails = dbServiceRepository.getResultUsingQuery(BuybackItemInfo.class, buybackHeaderDetailsQuery.toString(), true);

			if(itemDetails!=null && !itemDetails.isEmpty()) {

				for(BuybackItemInfo itemInfoFromDB: itemDetails) {
					BuybackHeaderDetailsDTO itemrDTO = new BuybackHeaderDetailsDTO();

					itemrDTO.setItemId(String.valueOf(itemInfoFromDB.getItemId())); //BBOF_ITEM_ID
					itemrDTO.setPartNumber(itemInfoFromDB.getPartNumber()); //PARTNRI
					itemrDTO.setSortedPartNumber(itemInfoFromDB.getSortedPartNumber()); //PARTNRS
					itemrDTO.setCapturePartNumber(itemInfoFromDB.getCapturePartNumber()); //PARTNRE
					itemrDTO.setPartName(itemInfoFromDB.getPartName() !=null ? itemInfoFromDB.getPartName() : ""); //BENEN
					itemrDTO.setCustResPos(itemInfoFromDB.getCustResPos()); // custRefPos
					itemrDTO.setRequestedQuantity(itemInfoFromDB.getRequestedQuantity());  //QTY | UNIT
					itemrDTO.setStock(String.valueOf(itemInfoFromDB.getStock()!=null ? itemInfoFromDB.getStock() : "0")); // AKTBES
					itemrDTO.setItemStatus(String.valueOf(itemInfoFromDB.getItemStatus())); //BBOF_ITEM_STATE
					itemrDTO.setSendQuantity(String.valueOf(itemInfoFromDB.getSendQuantity())); //SEND_QTY
					itemrDTO.setQuantity(String.valueOf(itemInfoFromDB.getQuantity())); //QTY
					//need to add condition 

					buybackHeaderDetailsList.add(itemrDTO);
				}
			}
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Buyback open Header Info"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY,
					"Buyback open Header Info"), exception);

			throw exception;
		}

		return buybackHeaderDetailsList;
	}


	/**
	 * This method is used to Return a list of BBOF Ausgebuchte/Geschlossene (closed) header information from DB
	 * @return List
	 */	
	@Override
	public List<BuybackHeaderDTO> getBuybackClosedHeaderInfo(String schema, String allowedWarehouses, List<String> warehouseNos) {

		log.info("Inside getBuybackClosedHeaderInfo method of BuybackProcessServiceImpl");

		List<BuybackHeaderDTO> buybackHeaderList = new ArrayList<BuybackHeaderDTO>();

		try {
			
			//validate the Warehouse Ids
			validateWarehouses(allowedWarehouses);
			
			if(warehouseNos != null && !warehouseNos.isEmpty()){
				 warehouseNos.removeIf(s -> s == null || "".equals(s.trim()));
				allowedWarehouses = StringUtils.join(warehouseNos, ",");
			}

			StringBuilder buybackHeaderQuery = new StringBuilder("SELECT distinct header.BBOF_HEAD_ID, VARCHAR_FORMAT(item.TRANSFER_DATE, 'DD-MM-YYYY') AS TRANSFER_DATE, ");
			buybackHeaderQuery.append(" header.ORDERNO, header.DELIVERYDATE, header.ORDERTYPE, header.BBOF_HEADER_STATE, header.AP_LNR, ");
			buybackHeaderQuery.append("(SELECT WAREHOUS_ID FROM ").append(schema).append(".O_WRH WHERE AP_WAREHOUS_ID = header.AP_LNR ) AS WAREHOUS_ID,");
			buybackHeaderQuery.append("(SELECT NAME FROM ").append(schema).append(".O_WRH WHERE AP_WAREHOUS_ID = header.AP_LNR ) AS WAREHOUS_NAME FROM ");
			buybackHeaderQuery.append(schema).append(".O_BBOFHEAD header left join ").append(schema).append(".O_BBOFITEM item  on header.BBOF_HEAD_ID = item.BBOF_HEAD_ID ");
			buybackHeaderQuery.append(" Where  header.AP_LNR IN(").append(allowedWarehouses).append(") AND header.BBOF_HEADER_STATE = 2 ");
			buybackHeaderQuery.append(" OR (header.BBOF_HEADER_STATE = 1 and item.BBOF_ITEM_STATE = 2 ) order by header.AP_LNR ");

			List<BuybackHeaderInfo> headerInfoList = dbServiceRepository.getResultUsingQuery(BuybackHeaderInfo.class, buybackHeaderQuery.toString(), true);

			if(headerInfoList!=null && !headerInfoList.isEmpty()) {

				for(BuybackHeaderInfo headerInfoFromDB: headerInfoList) {
					BuybackHeaderDTO headerDTO = new BuybackHeaderDTO();

					headerDTO.setAlphaXWarehousId(String.valueOf(headerInfoFromDB.getAlphaXWarehousId()));
					headerDTO.setAlphaXWarehousName(headerInfoFromDB.getAlphaXWarehousName());
					headerDTO.setOrderNumber(headerInfoFromDB.getOrderNumber());
					headerDTO.setDeliveryDate(headerInfoFromDB.getDeliveryDate());
					headerDTO.setOrderType(headerInfoFromDB.getOrderType());
					headerDTO.setHeaderStatus(String.valueOf(headerInfoFromDB.getHeaderStatus()));
					headerDTO.setAlphaPlusWarehouseId(String.valueOf(headerInfoFromDB.getAlphaPlusWarehouseId()));
					headerDTO.setHeaderId(String.valueOf(headerInfoFromDB.getHeaderId()));
					headerDTO.setTransferDate(headerInfoFromDB.getTransferDate());
					buybackHeaderList.add(headerDTO);
				}
			}
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Buyback closed Header Info"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY,
					"Buyback closed Header Info"), exception);

			throw exception;
		}

		return buybackHeaderList;
	}


	/**
	 * This method is used to Return BBOF Ausgebuchte/Geschlossene (closed) header's item detail from DB
	 * @return List
	 */	 
	@Override
	public List<BuybackHeaderDetailsDTO> getBuybackClosedHeaderItemDetails(String alphaPlusWarehouseNo, String headerId, String schema, String dataLibrary, 
			String transferDate) {

		log.info("Inside getBuybackClosedHeaderItemDetails method of BuybackProcessServiceImpl");

		List<BuybackHeaderDetailsDTO> buybackHeaderDetailsList = new ArrayList<BuybackHeaderDetailsDTO>();		
		try {

			StringBuilder buybackHeaderDetailsQuery = new StringBuilder(" SELECT PARTNRI, PARTNRS, PARTNRE, (SELECT BENEN FROM ").append(dataLibrary);
			buybackHeaderDetailsQuery.append(".E_ETSTAMM WHERE TNR = item.PARTNRI  AND LNR =").append(alphaPlusWarehouseNo).append(") , ");
			buybackHeaderDetailsQuery.append("custRefPos,  concat (concat  (QTY, ' ' ),  (SELECT  UNIT FROM ").append(schema);
			buybackHeaderDetailsQuery.append(".O_BBOFUNIT WHERE  ENUM=item.QTYUNIT ) )  As RequestedQuantity, ");
			buybackHeaderDetailsQuery.append(" BBOF_ITEM_STATE, SEND_QTY, QTY, BBOF_ITEM_ID from ").append(schema).append(".O_BBOFITEM item ");
			buybackHeaderDetailsQuery.append(" where item.BBOF_HEAD_ID =").append(headerId).append(" and item.BBOF_ITEM_STATE = 2 ");
			buybackHeaderDetailsQuery.append(" and VARCHAR_FORMAT(item.TRANSFER_DATE, 'DD-MM-YYYY') ='").append(transferDate).append("' order by BBOF_ITEM_ID");

			List<BuybackItemInfo> itemDetails = dbServiceRepository.getResultUsingQuery(BuybackItemInfo.class, buybackHeaderDetailsQuery.toString(), true);

			if(itemDetails!=null && !itemDetails.isEmpty()) {

				for(BuybackItemInfo itemInfoFromDB: itemDetails) {
					BuybackHeaderDetailsDTO itemrDTO = new BuybackHeaderDetailsDTO();

					itemrDTO.setItemId(String.valueOf(itemInfoFromDB.getItemId())); //BBOF_ITEM_ID
					itemrDTO.setPartNumber(itemInfoFromDB.getPartNumber()); //PARTNRI
					itemrDTO.setSortedPartNumber(itemInfoFromDB.getSortedPartNumber()); //PARTNRS
					itemrDTO.setCapturePartNumber(itemInfoFromDB.getCapturePartNumber()); //PARTNRE
					itemrDTO.setPartName(itemInfoFromDB.getPartName() != null ? itemInfoFromDB.getPartName() : ""); //BENEN
					itemrDTO.setCustResPos(itemInfoFromDB.getCustResPos()); // custRefPos
					itemrDTO.setRequestedQuantity(itemInfoFromDB.getRequestedQuantity());  //QTY | UNIT
					itemrDTO.setItemStatus(String.valueOf(itemInfoFromDB.getItemStatus())); //BBOF_ITEM_STATE
					itemrDTO.setSendQuantity(String.valueOf(itemInfoFromDB.getSendQuantity())); //SEND_QTY
					itemrDTO.setQuantity(String.valueOf(itemInfoFromDB.getQuantity())); //QTY											

					buybackHeaderDetailsList.add(itemrDTO);
				}
			}
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Buyback closed Header details"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY,
					"Buyback closed Header details"), exception);

			throw exception;
		}

		return buybackHeaderDetailsList;
	}


	/**
	 * This method is used to Save the BBOF Offene (Open) header's item details in DB.
	 * @return Map
	 */
	@Override
	public Map<String, Boolean> saveBuybackOpenItemDetails(List<BuybackHeaderDetailsDTO> buybackHeaderDetailsDTOList, String headerId, 
			String schema, String loginUserName) {

		log.info("Inside saveBuybackOpenItemDetails method of BuybackProcessServiceImpl");

		Connection con = null;
		Map<String, Boolean> programOutput = new HashMap<String, Boolean>();
		programOutput.put("isUpdated", false);
		try {

			if(buybackHeaderDetailsDTOList != null && !buybackHeaderDetailsDTOList.isEmpty()) {

				StringBuilder updateHeaderQuery = new StringBuilder("Update ").append(schema).append(".O_BBOFHEAD ");
				updateHeaderQuery.append(" SET BBOF_HEADER_STATE = 1, CHANGED_TS = now(), CHANGED_BY =").append("'"+loginUserName+"'");
				updateHeaderQuery.append(" where BBOF_HEAD_ID = ").append(headerId);

				con = dbServiceRepository.getConnectionObject();
				con.setAutoCommit(false);

				boolean isUpdated = dbServiceRepository.updateResultUsingQuery(updateHeaderQuery.toString(), con);

				if (isUpdated) {

					updateInTable_BBOFITEM(schema, headerId, loginUserName, buybackHeaderDetailsDTOList, con);

					programOutput.put("isUpdated", true);
					con.commit();
					con.setAutoCommit(true);
				}
				//if not updated successfully.
				else {

					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
							ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Buy back Header"));
					log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Buy back Header"), exception);
					throw exception;					
				}

			}
		} catch (AlphaXException ex) {
			transactionRollback(con, ex);
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Buy back Header"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Buy back Header"), exception);

			transactionRollback(con, exception);
			throw exception;
		}
		finally {
			connectionClose(con);
		}

		return programOutput;
	}


	/**
	 * This method is used to Update the Item details in O_BBOFITEM table.
	 */
	private boolean updateInTable_BBOFITEM(String schema, String headerId, String loginUserName, List<BuybackHeaderDetailsDTO> buybackHeaderDetailsDTOList, 
			Connection con) {

		log.info("Inside updateInTable_BBOFITEM method of BuybackProcessServiceImpl");

		boolean updateFlag = false;

		try (Statement stmt = con.createStatement()) {

			for(BuybackHeaderDetailsDTO headerDetailsDTO: buybackHeaderDetailsDTOList) {

				StringBuilder updateHeaderDetailsQuery = new StringBuilder("Update ").append(schema).append(".O_BBOFITEM ");
				updateHeaderDetailsQuery.append(" SET SEND_QTY = ").append(headerDetailsDTO.getSendQuantity());
				updateHeaderDetailsQuery.append(", BBOF_ITEM_STATE = 1, CHANGED_TS = now(), CHANGED_BY =").append("'"+loginUserName+"'");
				updateHeaderDetailsQuery.append(" where BBOF_HEAD_ID = ").append(headerId).append(" and BBOF_ITEM_ID = ").append(headerDetailsDTO.getItemId());

				stmt.addBatch(updateHeaderDetailsQuery.toString());
			}

			int[] records =  dbServiceRepository.insertResultUsingBatchQuery(stmt);
			if(records != null) {
				updateFlag = true;
				log.info("Total rows inserted : {} ", records.length);
			}
		}
		catch (AlphaXException ex) {
			transactionRollback(con, ex);
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Buy back Header item details"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY,
					"Buy back Header item details"), exception);

			transactionRollback(con, exception);
			throw exception;
		}

		return updateFlag;
	}


	private void transactionRollback (Connection con, AlphaXException ex) throws AlphaXException{

		try {
			if(con!=null){
				con.rollback();
			}
		} catch (SQLException e1) {
			throw ex;
		}
	}


	private void connectionClose(Connection con) {
		try {
			if(con!=null){
				con.close();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}


	/**
	 * This method is used to update BBOF data from Davet2oneapi(ARBBOF) from DB
	 * @return Object
	 */
	@Override
	public Map<String, Boolean> updateBuybackDataFromARBBOF(String schema, String dataLibrary) {
		log.info("Inside updateBuybackDataFromARBBOF method of BuybackProcessServiceImpl");
		Map<String, Boolean> programOutput = new HashMap<String, Boolean>();
		programOutput.put("isUpdated", false);
		try(Connection con = dbServiceRepository.getConnectionObject();){
		
			StringBuilder query = new StringBuilder("BEGIN DECLARE divisor INT; SET divisor=100; ");
			query.append("FOR v CURSOR FOR ");
			query.append("select distinct guid as mguid1,");
			query.append("orderTypeBB as morderType,");
			query.append("dimsCodeBB as mdimsCode,");
			query.append("	orderNoBB as morderNo,");
			query.append("	orderDateBB as morderDate, ");
			query.append("	customerNoBB as mcustomerNo, ");
			query.append("	custVendorNoBB as mcustVendorNo , ");
			query.append("	custRefNoBB as mcustRefNo,");
			query.append("	currencyBB as mcurrency , ");
			query.append("	totalNetValueBB as mtotalNetValue, ");
			query.append(" facingLocBB as mfacingLoc,");
			query.append("deliveryDateBB as mdeliveryDate ");
			query.append("from ");
			query.append("(SELECT guid, jt.* from ").append(schema).append(".o_arbbof , JSON_TABLE(payload, 'lax $' ");
			query.append("	COLUMNS(	");
			query.append("	orderTypeBB varchar(6) path 'lax $.orderHeader.orderType', ");
			query.append("	dimsCodeBB varchar(4) path 'lax $.orderHeader.dimsCode', ");
			query.append("	orderNoBB varchar(15) path 'lax $.orderHeader.orderNo', ");
			query.append(" orderDateBB varchar(8) path 'lax $.orderHeader.orderDate', ");
			query.append("	customerNoBB varchar(8) path 'lax $.orderHeader.customerNo', ");
			query.append("	custVendorNoBB varchar(15) path 'lax $.orderHeader.custVendorNo',");
			query.append("	custRefNoBB varchar(15) path 'lax $.orderHeader.custRefNo',");
			query.append("	currencyBB varchar(5) path 'lax $.orderHeader.currency', ");
			query.append("	totalNetValueBB varchar(20) path 'lax $.orderHeader.totalNetValue',");
			query.append("	facingLocBB varchar(10) path 'lax $.orderHeader.facingLoc',");
			query.append("	deliveryDateBB varchar(8) path 'lax $.orderHeader.deliveryDate'	");
			query.append (") ");
			query.append (")  AS jt  where state=9 and guid not in (select guid from ").append(schema).append(".o_bbofhead)	");	
			query.append("	)"); 
			query.append("	DO ");
					
			query.append("INSERT INTO ").append(schema).append(".o_BBOFHEAD  ");
			query.append( "( ");
			query.append(" orderType, ");
			query.append("dimsCode,");
			query.append("orderNo,");
			query.append("orderDate, ");
			query.append("customerNo, ");
			query.append("custVendorNo,");
			query.append("custRefNo,");
			query.append("currency,");
			query.append("totalNetValue,");
			query.append("facingLoc,");
			query.append("deliveryDate,");
			query.append("CREATED_TS,");
			query.append("BBOF_HEADER_STATE,");
			query.append("GUID,");
			query.append("AP_LNR");
			query.append( ") ");
			query.append("VALUES ");
			query.append("( ");
			query.append("morderType,mdimsCode,morderNo,cast(substr(morderDate, 1, 4)||'-'|| substr(morderDate, 5, 2)|| '-' ||  substr(morderDate, 7, 2 ) as date ), mcustomerNo, mcustVendorNo,mcustRefNo,mcurrency ,cast(trim(replace (mtotalNetValue, ',', '')) as real)/divisor, cast (mfacingLoc as integer),cast(substr(mdeliveryDate, 1, 4)||'-'|| substr(mdeliveryDate, 5, 2)|| '-' ||  substr(mdeliveryDate, 7, 2 ) as date ),now(),0,mguid1,(select LKD_LNR from ").append(schema).append(".PMH_LKDNR WHERE LKD_LKDNR=right(mcustomerNo, 6))); ");
			query.append("END FOR; ");
			query.append("	FOR v CURSOR FOR ");
			query.append("select 	guid as mguid2,posNoBB as mposNo,partNrIBB as mpartNrI ,partNrSBB as mpartNrS,qtyBB as mqty ,qtyUnitBB as mqtyUnit ,netValueBB as mnetValue ,netValuePosBB as mnetValuePos ,rmaBB as mrma ,custRefPosBB as mcustRefPos ");
			query.append("from ");
			query.append("(SELECT  guid, jt.* from  ").append(schema).append(".o_arbbof , JSON_TABLE(payload, 'lax $' ");
			query.append("COLUMNS( ");
			query.append("NESTED PATH 'lax $.orderItem[*]' ");
			query.append("	COLUMNS ( ");
			query.append("nested_ordinality FOR ORDINALITY,posNoBB varchar(8) path 'lax $.posNo',	partNrIBB varchar(50) path 'lax $.partNrI',partNrSBB varchar(50) path 'lax $.partNrS',qtyBB varchar(10) path 'lax $.qty',qtyUnitBB integer path 'lax $.qtyUnit',netValueBB varchar(10) path 'lax $.netValue',netValuePosBB varchar(10) path 'lax $.netValuePos',rmaBB varchar(20) path 'lax $.rma',custRefPosBB varchar(15) path 'lax $.custRefPos' ");
			query.append(")	)");
			query.append(") "	);
			query.append(" AS jt  where state=9 and guid not in (select guid from ").append(schema).append(".o_bbofitem) ");
			query.append(") "); 
			query.append("	DO ");
			query.append("INSERT INTO ").append(schema).append(".o_BBOFITEM  ");
			query.append("( ");
			query.append("guid,BBOF_HEAD_ID,posNo,partNrI,partNrS,partNrE, qty,qtyUnit,netValue,netValuePos,rma,custRefPos,BBOF_ITEM_STATE,created_TS, PARTBRAND,PARTKIND,PARTNAME ");
			query.append(")	");
			query.append("	VALUES ");
			query.append("	(	");
			query.append("mguid2,(select bbof_head_id from ").append(schema).append(".o_bbofhead where guid=mguid2),cast (mposNo as integer),mpartNrI ,mpartNrS ,(values (").append(schema).append(".SYBRAS_TRANSFORMATION(mpartNrS,'S','E'))),cast(trim(replace (mqty, ',', '')) as real)/divisor,cast (mqtyUnit as integer),cast(trim(replace (mnetValue, ',', '')) as real)/divisor,cast(trim(replace (mnetValuePos, ',', '')) as real)/divisor,mrma,mcustRefPos,0,now(), ");
			query.append("	(select TMARKE from ").append(dataLibrary).append(".E_ETSTAMM WHERE LNR=(SELECT AP_LNR from ").append(schema).append(" .O_BBOFHEAD WHERE guid = mguid2) AND HERST='DCAG' AND TNRS=mpartNrS), ");
			query.append("		(select TA from ").append(dataLibrary).append(".E_ETSTAMM WHERE LNR=(SELECT AP_LNR from ").append(schema).append(".O_BBOFHEAD WHERE guid = mguid2) AND HERST='DCAG' AND TNRS=mpartNrS), ");
			query.append("	(SELECT BENEN from ").append(dataLibrary).append(".E_ETSTAMM WHERE LNR=(SELECT AP_LNR from ").append(schema).append(".O_BBOFHEAD WHERE guid = mguid2) AND HERST='DCAG' AND TNRS=mpartNrS) ");
			query.append(");");
			query.append("	END FOR;");			
			query.append("END ");
		
		boolean executionFlag = dbServiceRepository.excuteProcedure(query.toString(), con);
		
		
		if(executionFlag) {
			programOutput.put("isUpdated", true);
			con.setAutoCommit(true);
			con.commit();
		}
		
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "buyback data from Davet2API(ARBBOF) "));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "buyback data from Davet2API(ARBBOF)"), exception);
			throw exception;
		}
		return programOutput;
	}

	/**
	 * This method is used to return the count of Dashboard(BBOFHEAD & o_BBOFITEM) from DB.
	 * @return Object
	 */
	@Override
	public Map<String, String> getBuybackDashboardCount(String dataLibrary, String schema) {
		
		log.info("Inside getBuybackDashboardCount method of BuybackProcessServiceImpl");
		String count = "0";
		Map<String,String> resultCounts = new HashMap<String, String>();
		
		try {

			// FOR (BBOFHEAD TABLE)
			StringBuilder query_openBBOFList = new StringBuilder(" SELECT COUNT (*) AS COUNT FROM ").append(schema).append(".O_BBOFHEAD ");
			query_openBBOFList.append("WHERE BBOF_HEADER_STATE IN (0,1) ");
			
			StringBuilder query_newBBOFList = new StringBuilder(" SELECT count ( * ) AS COUNT from ").append(schema).append(".O_ARBBOF where ");
					query_newBBOFList.append( "GUID NOT IN (SELECT GUID from ").append(schema).append(" .O_BBOFHEAD ) ");
					query_newBBOFList.append("AND STATE=9");

			count = dbServiceRepository.getCountUsingQuery(query_openBBOFList.toString());
			resultCounts.put(RestInputConstants.QUERY_OPEN_BBOF_lIST, count);

			count = dbServiceRepository.getCountUsingQuery(query_newBBOFList.toString());
			resultCounts.put(RestInputConstants.QUERY_NEW_BBOF_LIST, count);

		} 
		catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Dashboard BBOF(BBOFHEAD & o_BBOFITEM) counts "));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_LIST_FAILED_MSG_KEY, "Dashboard BBOF (BBOFHEAD & o_BBOFITEM) counts"), exception);
			throw exception;
		}

		return resultCounts;
	}

	/**
	 * This method is used to Update the buyback head Status(BBOFHEAD) from DB.
	 * @return Object
	 */
    @Override
	public Map<String, Boolean> updateBuybackHeadStatus(String schema, String headerId) {
		
		log.info("Inside updateBuybackHeadStatus method of BuybackProcessServiceImpl");
		Map<String, Boolean> queryOutput = new HashMap<String, Boolean>();
		queryOutput.put("isUpdated", false);
		
		try {
			
			StringBuilder updateHeaderQuery = new StringBuilder("Update ").append(schema).append(".O_BBOFHEAD ");
			updateHeaderQuery.append(" SET BBOF_HEADER_STATE = 3");
			updateHeaderQuery.append(" where BBOF_HEAD_ID = ").append(headerId);
			
			boolean updateFlag = dbServiceRepository.updateResultUsingQuery(updateHeaderQuery.toString());
			
			if (updateFlag) {
				queryOutput.put("isUpdated", true);
			}
			else {
				log.info("Unable to update BBOF header Status Id in O_BBOFHEAD for this Id : {}", headerId);
				AlphaXException exception = new AlphaXException(messageService.createApiMessage(
						LocaleContextHolder.getLocale(), ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "BBOF header id"));
				log.error(messageService.getReadableMessage(ExceptionMessages.RECORD_NOT_FOUND_MSG_KEY, "BBOF header id"));
				throw exception;
			}

		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "BBOF header Status"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "BBOF header Status"), exception);
			throw exception;
		}

		return queryOutput;
	}
		
	
    /**
	 * This method is used to book out the BBOF Offene (Open) header's item details in DB.
	 * @return Map
	 */
	@Override
	public Map<String, Boolean> bookOutBuybackOpenItemDetails(List<BuybackHeaderDetailsDTO> buybackHeaderDetailsDTOList, String headerId, String orderNumber, 
			String schema, String dataLibrary, String loginUserName, String alphaPlusWarehouseNo, String companyId, String agencyId) {

		log.info("Inside bookOutBuybackOpenItemDetails method of BuybackProcessServiceImpl");

		Connection con = null;
		Map<String, Boolean> programOutput = new HashMap<String, Boolean>();
		programOutput.put("isUpdated", false);
		try {
			if(buybackHeaderDetailsDTOList != null && !buybackHeaderDetailsDTOList.isEmpty()) {

				//call the save method of Buy-back OpenItem Details
				Map<String, Boolean> saveOutputFlag = saveBuybackOpenItemDetails(buybackHeaderDetailsDTOList, headerId, schema, loginUserName);

				if(saveOutputFlag != null && saveOutputFlag.entrySet().stream().findFirst().get().getValue()) {

					StringBuilder buybackHeaderDetailsQuery = new StringBuilder(" Select PARTNRI, BBOF_ITEM_STATE, SEND_QTY, QTY, BBOF_ITEM_ID, now() as TRANSFER_DATE from ");
					buybackHeaderDetailsQuery.append(schema).append(".O_BBOFITEM  Where BBOF_HEAD_ID =");
					buybackHeaderDetailsQuery.append(headerId).append(" and BBOF_ITEM_STATE = 1 order by BBOF_ITEM_ID ");

					List<BuybackItemInfo> itemDetails = dbServiceRepository.getResultUsingQuery(BuybackItemInfo.class, buybackHeaderDetailsQuery.toString(), true);

					if(itemDetails != null && !itemDetails.isEmpty()) {

						//get current the date and Time 
						String currentDateTime = itemDetails.get(0).getTransferDate();
						con = dbServiceRepository.getConnectionObject();

						for(BuybackItemInfo itemInfo: itemDetails) {							

							//get the parts details using E_ETSTAMM table.
							DeparturesBA_DTO departureDto = businessCases.getDeparturesBA25Details(dataLibrary, companyId, agencyId, RestInputConstants.DCAG_STRING, 
									alphaPlusWarehouseNo, itemInfo.getPartNumber(), RestInputConstants.SUPPLIER_NO, orderNumber, RestInputConstants.PROCESS_CODE, 
									RestInputConstants.BA_25 );

							if(departureDto != null) {

								//update the SEND_DAK, PARTBRAND and PARTKIND in the O_BBOFITEM using E_ETSTAMM
								StringBuilder updateHeaderItemQuery = new StringBuilder("Update ").append(schema).append(".O_BBOFITEM ");
								updateHeaderItemQuery.append(" SET SEND_DAK = ").append(departureDto.getAverageNetPrice());
								updateHeaderItemQuery.append(", PARTBRAND = ").append("'"+departureDto.getOemBrand()+"'");
								updateHeaderItemQuery.append(", PARTKIND = ").append("'"+departureDto.getPartsIndikator()+"'");
								updateHeaderItemQuery.append(" where BBOF_HEAD_ID = ").append(headerId).append(" and BBOF_ITEM_ID = ").append(itemInfo.getItemId());

								boolean updateFlag = dbServiceRepository.updateResultUsingQuery(updateHeaderItemQuery.toString(), con);

								if (!updateFlag) {
									log.info("Unable to update O_BBOFITEM details for Id : {}", itemInfo.getItemId());
									AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
											ExceptionMessages.UPDATE_FAILED_MSG_KEY, "O_BBOFITEM"));
									log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "O_BBOFITEM"), exception);
									throw exception;
								}
							}

							Double sendQuantity = itemInfo.getSendQuantity()!=null ? itemInfo.getSendQuantity().doubleValue(): 0.00;
							Double currentStock = departureDto.getCurrentStock()!=null ? Double.parseDouble(departureDto.getCurrentStock()): 0.00;

							//check the condition to call BA25
							if( itemInfo.getItemStatus() == 1 && sendQuantity > 0 &&  currentStock >= sendQuantity ) {

								//set the required parameters in BusinessCases25DTO obj
								BusinessCases25DTO ba25_obj = setParamForBA25(departureDto, itemInfo, alphaPlusWarehouseNo, orderNumber);

								//call BA25
								Map<String, Boolean> ba25Ouput = businessCases.newJavaImplementation_BA25( ba25_obj, dataLibrary, schema, companyId, agencyId, loginUserName,null );
								
								//if BA25 was successful
								if(ba25Ouput != null && ba25Ouput.entrySet().stream().findFirst().get().getValue()) {

									//update the BBOF_ITEM_STATE, TRANSFER_DATE in the O_BBOFITEM using E_ETSTAMM
									StringBuilder updateItemQuery = new StringBuilder("Update ").append(schema).append(".O_BBOFITEM ");
									updateItemQuery.append(" SET BBOF_ITEM_STATE = 2, TRANSFER_DATE=").append("'"+currentDateTime+"'");
									updateItemQuery.append(" where BBOF_HEAD_ID = ").append(headerId).append(" and BBOF_ITEM_ID = ").append(itemInfo.getItemId());

									dbServiceRepository.updateResultUsingQuery(updateItemQuery.toString(), con);
								}
							}
						}

						//get the count for the O_BBOFITEM
						StringBuilder checkItemStatusEntry = new StringBuilder("SELECT COUNT(*) AS COUNT FROM ").append(schema).append(".O_BBOFITEM ");
						checkItemStatusEntry.append(" where BBOF_HEAD_ID = ").append(headerId).append(" and BBOF_ITEM_STATE NOT IN (2,3) ");

						String count = dbServiceRepository.getCountUsingQuery(checkItemStatusEntry.toString());

						if(Integer.parseInt(count) == 0){

							//update the BBOF_ITEM_STATE, TRANSFER_DATE in the O_BBOFITEM using E_ETSTAMM
							StringBuilder updateHeaderQuery = new StringBuilder("Update ").append(schema).append(".O_BBOFHEAD ");
							updateHeaderQuery.append(" SET BBOF_HEADER_STATE = 2, CHANGED_TS = now(), CHANGED_BY =").append("'"+loginUserName+"'");
							updateHeaderQuery.append(" where BBOF_HEAD_ID = ").append(headerId);

							dbServiceRepository.updateResultUsingQuery(updateHeaderQuery.toString(), con);
						}
						
						con.commit();
						con.setAutoCommit(true);
						programOutput.put("isUpdated", true);
					}					
				}
			}
		} catch (AlphaXException ex) {
			transactionRollback(con, ex);
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e,messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Bookout for Buy back Header"));
			log.error(messageService.getReadableMessage(ExceptionMessages.UPDATE_FAILED_MSG_KEY, "Bookout for Buy back Header"), exception);

			transactionRollback(con, exception);
			throw exception;
		}
		finally {
			connectionClose(con);
		}

		return programOutput;
	}
	
	
	/**
	 * This method is used to set the required parameter for BA25
	 * @param departureDto
	 * @param itemInfo
	 * @param alphaPlusWarehouseNo
	 * @param orderNumber
	 * @return
	 */
	private BusinessCases25DTO setParamForBA25(DeparturesBA_DTO departureDto, BuybackItemInfo itemInfo, String alphaPlusWarehouseNo, String orderNumber) {
		
		BusinessCases25DTO ba25_obj = new BusinessCases25DTO();

		ba25_obj.setManufacturer(RestInputConstants.DCAG_STRING);
		ba25_obj.setWarehouseNumber(alphaPlusWarehouseNo);
		ba25_obj.setSupplierNumber(RestInputConstants.SUPPLIER_NO);
		ba25_obj.setBusinessCases(RestInputConstants.BA_25);
		ba25_obj.setCustomerGroup(RestInputConstants.PROCESS_CODE);
		ba25_obj.setCurrentStock(departureDto.getCurrentStock()); //AKTBES
		ba25_obj.setPartNumber(itemInfo.getPartNumber()); //TNR							
		ba25_obj.setListPrice(departureDto.getListPrice()); ////EPR mwst
		ba25_obj.setInputListPrice(departureDto.getListPrice());
		ba25_obj.setAverageNetPrice(departureDto.getAverageNetPrice()); //DAK
		ba25_obj.setDiscountGroup(departureDto.getDiscountGroup()); // RG
		ba25_obj.setOemBrand(departureDto.getOemBrand());
		ba25_obj.setMarketingCode(departureDto.getMarketingCode());
		ba25_obj.setLastPurchasePrice(departureDto.getLastPurchasePrice()); //LEKPR
		ba25_obj.setPartsIndikator(departureDto.getPartsIndikator()); //TA
		ba25_obj.setDeliverIndicator(departureDto.getDeliverIndicator()); //LIWERK
		ba25_obj.setNetPrice(departureDto.getNetPrice());  //EKNPR
		ba25_obj.setNetShoppingPrice("0.00");
		ba25_obj.setPreviousPurchasePrice(departureDto.getPreviousPurchasePrice());  //NPREIS
		ba25_obj.setSortingFormatPartNumber(departureDto.getSortingFormatPartNumber());  //TNRS
		ba25_obj.setPrintingFormatPartNumber(departureDto.getPrintingFormatPartNumber()); //TNRD				
		ba25_obj.setCompany(departureDto.getCompany()); //FIRMA
		ba25_obj.setAgency(departureDto.getAgency());  //FILALE
		ba25_obj.setDeliveryNoteNo(orderNumber);  // Belegnr
		ba25_obj.setBookingAmount(String.valueOf(itemInfo.getSendQuantity())); //Menge
		ba25_obj.setPendingOrders(departureDto.getPendingOrders()); //BESAUS
		ba25_obj.setSalesOrderPosition(""); //POSNR
		
		return ba25_obj;
	}

	
}