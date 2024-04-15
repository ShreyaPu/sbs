package com.alphax.repository;

import java.lang.reflect.Field;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
//import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.alphax.common.rest.message.service.MessageService;
import com.alphax.service.mb.AS400UsersService;
import com.alphax.common.exception.AlphaXException;
import com.alphax.common.exception.ExceptionMessages;
import com.alphax.common.util.DBTable;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCDataSource;

import lombok.extern.slf4j.Slf4j;

@Service("dbServiceRepository")
@Slf4j
public class DBServiceRepository {

	@Autowired
	AS400UsersService as400UsersService;
	
//	@Autowired
//	JdbcTemplate jdbcTemplate;
	
	@Autowired
	private MessageService messageService;

	public Map<String, String> getResultUsingCobolQuery(String query) {
		
		AS400 as400 = as400UsersService.getAs400Object();
		if(as400==null){
			AlphaXException exception = new AlphaXException(ExceptionMessages.AUTHENTICATION_FAILED, AlphaXException.AUTHENTICATION_FAILED);
			log.error(ExceptionMessages.AUTHENTICATION_FAILED, exception);
			throw exception;
		}
		
		AS400JDBCDataSource datasource = new AS400JDBCDataSource(as400);
		Map<String, String> dataMap = new LinkedHashMap<>();
		log.info("Get Results Using Query :" + query);
		try (Connection con = datasource.getConnection();
				PreparedStatement stmt = con.prepareStatement(query);	) {
			
			try ( ResultSet rs = stmt.executeQuery()) {
				
				if (rs != null) {
					while (rs.next()) {
						dataMap.put(rs.getString("KEYFLD").trim(), rs.getString("DATAFLD").trim());
					} 
				}
				else {
					log.info("ResultSet is emply");
				}
			}

		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.DB_CALL_FAIL_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.DB_CALL_FAIL_MSG_KEY), exception);
			throw exception;

		}
		//return the dataMap
		return dataMap;

	}
	
	public boolean updateResultUsingQuery(String query) {

		AS400 as400 = as400UsersService.getAs400Object();
		if (as400 == null) {
			AlphaXException exception = new AlphaXException(ExceptionMessages.AUTHENTICATION_FAILED,
					AlphaXException.AUTHENTICATION_FAILED);
			log.error(ExceptionMessages.AUTHENTICATION_FAILED, exception);
			throw exception;
		}

		AS400JDBCDataSource datasource = new AS400JDBCDataSource(as400);
		boolean updateCheck = false;
		log.info("Update Result Using Query :" + query);
		try (Connection con = datasource.getConnection();
				PreparedStatement stmt = con.prepareStatement(query);) {

			int updateCount = stmt.executeUpdate();
			log.info("Update Count :" + updateCount);
			if (updateCount > 0) {
				updateCheck = true;
			}

		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.DB_CALL_FAIL_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.DB_CALL_FAIL_MSG_KEY), exception);
			throw exception;

		}
		return updateCheck;

	}

	
	/**
	 * This method is used as generic method for get Result from DB
	 * @param <T>
	 * @param as400
	 * @param type
	 * @param query
	 * @return
	 */
	public <T> List<T> getResultUsingQuery(Class<T> type, String query, boolean isTrimRequired) {

		AS400 as400 = as400UsersService.getAs400Object();
		if(as400==null){
			AlphaXException exception = new AlphaXException(ExceptionMessages.AUTHENTICATION_FAILED, AlphaXException.AUTHENTICATION_FAILED);
			log.error(ExceptionMessages.AUTHENTICATION_FAILED, exception);
			throw exception;
		}

		AS400JDBCDataSource datasource = new AS400JDBCDataSource(as400);
		List<T> list = new ArrayList<T>();
		log.info("Get Results Using Query : {}", query);
		try (Connection con = datasource.getConnection();
				PreparedStatement stmt = con.prepareStatement(query);) {
			
			try (ResultSet rs = stmt.executeQuery()) {
				
				if (rs != null) {
					while (rs.next()) {
						T t = type.newInstance();
						loadResultSetIntoObject(rs, t, isTrimRequired);
						list.add(t);
					}
				}
				else {
					log.info("ResultSet is empty");
				}
			}
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.DB_CALL_FAIL_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.DB_CALL_FAIL_MSG_KEY), exception);
			throw exception;
		} 
		return list;
	}
	
	
	public void loadResultSetIntoObject(ResultSet rst, Object object, boolean isTrimRequired) throws Exception
	{
		Class<?> zclass = object.getClass();
		ResultSetMetaData rsMetaData = rst.getMetaData();

		// get the column names; column indexes start from 1
		for (int i = 1; i < rsMetaData.getColumnCount() + 1; i++) {

			String columnName = rsMetaData.getColumnName(i);
			
			for (Field field : zclass.getDeclaredFields()) {
				field.setAccessible(true);

				DBTable column = field.getAnnotation(DBTable.class);
				//System.out.println(column.columnName());
				if(column.required() && columnName.equalsIgnoreCase(column.columnName())) {
					Object value = rst.getObject(column.columnName());
					Class<?> type = field.getType();
					if(value != null){
						if (isPrimitive(type)) {//check primitive type
							Class<?> boxed = boxPrimitiveClass(type);//box if primitive
							value = boxed.cast(value);
						}
						if (type == String.class) {
							if(isTrimRequired){
							value = value.toString().trim();
							}else{
								value = value.toString();	
							}
						}
					}
					field.set(object, value);
					break;
				}
			}
		}
	}
	
	public boolean isPrimitive(Class<?> type) {
	    return (type == int.class || type == long.class || type == double.class || type == float.class
	            || type == boolean.class || type == byte.class || type == char.class || type == short.class);
	}
	
	
	public Class<?> boxPrimitiveClass(Class<?> type) {
	    if (type == int.class) {
	        return Integer.class;
	    } else if (type == long.class) {
	        return Long.class;
	    } else if (type == double.class) {
	        return Double.class;
	    } else if (type == float.class) {
	        return Float.class;
	    } else if (type == boolean.class) {
	        return Boolean.class;
	    } else if (type == byte.class) {
	        return Byte.class;
	    } else if (type == char.class) {
	        return Character.class;
	    } else if (type == short.class) {
	        return Short.class;
	    } else {
	        String string = "class '" + type.getName() + "' is not a primitive";
	        throw new IllegalArgumentException(string);
	    }
	}
	
	public String getCountUsingQuery(String query) {
			
		AS400 as400 = as400UsersService.getAs400Object();
		if(as400==null){
			AlphaXException exception = new AlphaXException(ExceptionMessages.AUTHENTICATION_FAILED, AlphaXException.AUTHENTICATION_FAILED);
			log.error(ExceptionMessages.AUTHENTICATION_FAILED, exception);
			throw exception;
		}

		AS400JDBCDataSource datasource = new AS400JDBCDataSource(as400);
		
			String count = "0";
			log.info("Get Results Using Query :" + query);
			try (Connection con = datasource.getConnection();
					PreparedStatement stmt = con.prepareStatement(query);	) {
				
				try ( ResultSet rs = stmt.executeQuery()) {
					
					if (rs != null) {
						while (rs.next()) {
							count = rs.getString("count");
						} 
					}
					else {
						log.info("ResultSet is emply");
					}
				}
	
			} catch (Exception e) {
				log.info("Error Message:" + e.getMessage());
				AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.DB_CALL_FAIL_MSG_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.DB_CALL_FAIL_MSG_KEY), exception);
				throw exception;
	
			}
			//return the dataMap
			return count;
	
		}
	
	
	public int insertResultUsingQuery(String query) {

		AS400 as400 = as400UsersService.getAs400Object();
		if(as400==null){
			AlphaXException exception = new AlphaXException(ExceptionMessages.AUTHENTICATION_FAILED, AlphaXException.AUTHENTICATION_FAILED);
			log.error(ExceptionMessages.AUTHENTICATION_FAILED, exception);
			throw exception;
		}

		AS400JDBCDataSource datasource = new AS400JDBCDataSource(as400);
		
		int autocreatedId = 0;
		log.info("Insert Result Using Query :" + query);
		/*try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement stmt = con.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS);) {*/
		
		try (Connection con = datasource.getConnection();
				PreparedStatement stmt = con.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS);) {
			
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();

				if (rs != null) {
					while (rs.next()) {
						autocreatedId = rs.getBigDecimal(1).intValue();	
					}
				} else {
					log.info("ResultSet is empty");
				}

		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.DB_CALL_FAIL_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.DB_CALL_FAIL_MSG_KEY), exception);
			throw exception;

		}
		return autocreatedId;

	}
	
	public boolean isTableExist(String schema,String tableName) {

		AS400 as400 = as400UsersService.getAs400Object();
		if(as400==null){
			AlphaXException exception = new AlphaXException(ExceptionMessages.AUTHENTICATION_FAILED, AlphaXException.AUTHENTICATION_FAILED);
			log.error(ExceptionMessages.AUTHENTICATION_FAILED, exception);
			throw exception;
		}

		AS400JDBCDataSource datasource = new AS400JDBCDataSource(as400);
		
		boolean flag = false;
		log.debug("Check table exist or not :" + tableName);
		
		try (Connection con = datasource.getConnection();
				ResultSet rs = con.getMetaData().getTables(null, schema, tableName, null);) {

				if (rs != null && rs.next() && rs.getString("TABLE_NAME").equalsIgnoreCase(tableName)) {
					/*String name = rs.getString("TABLE_NAME");
				    String schema1 = rs.getString("TABLE_SCHEM");
				    System.out.println(name + " on schema " + schema1);*/
					flag = true;
					log.info(tableName +" - Table exist in DB ");
				} else {
					flag = false;
				}

		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.DB_CALL_FAIL_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.DB_CALL_FAIL_MSG_KEY), exception);
			throw exception;

		}
		return flag;

	}
	
	
	public int insertResultUsingQuery(String query,Connection con) {
		
		int autocreatedId = 0;
		log.info("Insert Result Using Query :" + query);
		/*try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement stmt = con.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS);) {*/
		
		try (PreparedStatement stmt = con.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS);) {
			
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();

				if (rs != null) {
					while (rs.next()) {
						autocreatedId = rs.getBigDecimal(1).intValue();	
					}
				} else {
					log.info("ResultSet is empty");
				}

		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.DB_CALL_FAIL_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.DB_CALL_FAIL_MSG_KEY), exception);
			throw exception;

		}
		return autocreatedId;

	}
	
	
	
	public Connection getConnectionObject() {

		AS400 as400 = as400UsersService.getAs400Object();
		if (as400 == null) {
			AlphaXException exception = new AlphaXException(ExceptionMessages.AUTHENTICATION_FAILED,AlphaXException.AUTHENTICATION_FAILED);
			log.error(ExceptionMessages.AUTHENTICATION_FAILED, exception);
			throw exception;
		}

		AS400JDBCDataSource datasource = new AS400JDBCDataSource(as400);
		Connection con = null;
		try {
			con = datasource.getConnection();
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.DB_CALL_FAIL_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.DB_CALL_FAIL_MSG_KEY), exception);
			throw exception;
		}
		return con;

	}
	
	public boolean updateResultUsingQuery(String query,Connection con) {

		boolean updateCheck = false;
		log.info("Update Result Using Query :" + query);
		try (PreparedStatement stmt = con.prepareStatement(query);) {

			int updateCount = stmt.executeUpdate();
			log.info("Update Count :" + updateCount);
			if (updateCount > 0) {
				updateCheck = true;
			}

		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.DB_CALL_FAIL_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.DB_CALL_FAIL_MSG_KEY), exception);
			throw exception;

		}
		return updateCheck;

	}
	
	public boolean deleteResultUsingQuery(String query,Connection con) {

		boolean isDeleted = false;
		log.info("Delete Results Using Query :" + query);
		try (PreparedStatement stmt = con.prepareStatement(query);) {

			int updateCount = stmt.executeUpdate();
			log.info("Delete Count :" + updateCount);
			if (updateCount > 0) {
				isDeleted = true;
			}

		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.DB_CALL_FAIL_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.DB_CALL_FAIL_MSG_KEY), exception);
			throw exception;

		}
		return isDeleted;

	}
	
	public String getCountUsingQuery(String query, Connection con) {
		
			String count = "0";
			log.info("Get Results Using Query :" + query);
			try (PreparedStatement stmt = con.prepareStatement(query);) {
				
				try ( ResultSet rs = stmt.executeQuery()) {
					
					if (rs != null) {
						while (rs.next()) {
							count = rs.getString("count");
						} 
					}
					else {
						log.info("ResultSet is emply");
					}
				}
	
			} catch (Exception e) {
				log.info("Error Message:" + e.getMessage());
				AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.DB_CALL_FAIL_MSG_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.DB_CALL_FAIL_MSG_KEY), exception);
				throw exception;
	
			}
			//return the dataMap
			return count;
	
		}
	
	
	public int[] insertResultUsingBatchQuery(Statement stmt) {
        
        log.info("Insert Result Using Batch :");
        int[] insertedResult = null;
        try {

                       insertedResult = stmt.executeBatch();

        } catch (Exception e) {
                       log.info("Error Message:" + e.getMessage());
                       AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.DB_CALL_FAIL_MSG_KEY));
        log.error(messageService.getReadableMessage(ExceptionMessages.DB_CALL_FAIL_MSG_KEY), exception);
                       throw exception;

        }
        return insertedResult;

	}
	
	public void createTableUsingQuery(String query) {

		AS400 as400 = as400UsersService.getAs400Object();
		if(as400==null){
			AlphaXException exception = new AlphaXException(ExceptionMessages.AUTHENTICATION_FAILED, AlphaXException.AUTHENTICATION_FAILED);
			log.error(ExceptionMessages.AUTHENTICATION_FAILED, exception);
			throw exception;
		}
		AS400JDBCDataSource datasource = new AS400JDBCDataSource(as400);
		
		log.info("Create table Using Query :" + query);
		
		try (Connection con = datasource.getConnection();
				PreparedStatement stmt = con.prepareStatement(query);) {
			int updateCount = stmt.executeUpdate();
			log.info("Table / Alias created successfully ");

		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.DB_CALL_FAIL_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.DB_CALL_FAIL_MSG_KEY), exception);
			throw exception;

		}
	}
	
	
	public boolean excuteProcedureWithoutRS(CallableStatement cstmt, Connection con) {

		boolean updateCheck = false;
		log.info("Executing Stored Procedure...");
		try {
			int updatedFlag = cstmt.executeUpdate();
			if (updatedFlag == 0) {
				updateCheck = true;
			}

		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.DB_CALL_FAIL_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.DB_CALL_FAIL_MSG_KEY), exception);
			throw exception;

		}
		return updateCheck;

	}
	
	public boolean excuteProcedure(String query,Connection con) {

		boolean updateCheck = false;
		log.info("Executing Stored Procedure..."+query);
		try (PreparedStatement stmt = con.prepareStatement(query);) {

			int updatedFlag = stmt.executeUpdate();
			if (updatedFlag == 0) {
				updateCheck = true;
			}
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.DB_CALL_FAIL_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.DB_CALL_FAIL_MSG_KEY), exception);
			throw exception;

		}
		return updateCheck;

	}
	
	
	public int[] deleteResultUsingBatchQuery(Statement stmt) {
        
        log.info("Delete Result Using Batch :");
        int[] insertedResult = null;
        try {

                       insertedResult = stmt.executeBatch();

        } catch (Exception e) {
                       log.info("Error Message:" + e.getMessage());
                       AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.DB_CALL_FAIL_MSG_KEY));
        log.error(messageService.getReadableMessage(ExceptionMessages.DB_CALL_FAIL_MSG_KEY), exception);
                       throw exception;

        }
        return insertedResult;

	}
	

}
