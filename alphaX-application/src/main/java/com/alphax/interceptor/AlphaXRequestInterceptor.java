package com.alphax.interceptor;


import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import com.alphax.common.rest.message.service.MessageService;
import com.alphax.common.exception.AlphaXException;
import com.alphax.common.exception.ExceptionMessages;
import com.alphax.repository.DBServiceRepository;
import com.alphax.util.DecryptTokenUtils;

import lombok.extern.slf4j.Slf4j;


@Aspect
@Component
@Slf4j
public class AlphaXRequestInterceptor { 

	@Autowired
	private MessageService messageService;
	
	@Autowired
	DBServiceRepository dbServiceRepository;
	
	@Autowired
	DecryptTokenUtils decryptUtil;
	
	@Value("${daily.closing.process}")
	private String checkDailyClosingProcess;	
	

@Before(value = "execution(* com.alphax.service.mb.BusinessCases.*(..)) || "
		+ "execution(* com.alphax.service.mb.AdminSettings.*(..)) || "
		+ "execution(* com.alphax.service.mb.CompanyService.*(..)) || "
		+ "execution(* com.alphax.service.mb.DashboardService.*(..)) || "
		+ "execution(* com.alphax.service.mb.IncomingGoodsService.*(..)) || "
		+ "execution(* com.alphax.service.mb.OrdersService.*(..)) || "
		+ "execution(* com.alphax.service.mb.PartsService.*(..)) || "
		+ "execution(* com.alphax.service.mb.SimulationProcessService.*(..)) || "
		+ "execution(* com.alphax.service.mb.VehicleDataMapService.*(..)) || "
		+ "execution(* com.alphax.service.mb.VehicleLegendService.*(..)) || "
		+ "execution(* com.alphax.service.mb.VehicleService.*(..)) || "
		+ "execution(* com.alphax.service.mb.CustomerService.*(..)) || "
		+ "execution(* com.alphax.service.mb.JwtAuthenticationService.getUserPermissions(..)) || "
		+ "execution(* com.alphax.service.mb.JwtAuthenticationService.getUserStandardInfo(..)) ||"
		+ "execution(* com.alphax.service.mb.ReportSelectionsService.*(..)) || "
		+ "execution(* com.alphax.service.mb.InventoryEvalutionService.*(..)) || "
		+ "execution(* com.alphax.service.mb.InventoryService.*(..)) ||  "
		+ "execution(* com.alphax.service.mb.AdminOneapiService.*(..))  ")
	public void beforeAdvice(JoinPoint joinPoint) throws Exception {
		log.debug("Executing Before Advice ");
		try {
			String  ip = InetAddress.getLocalHost().getHostAddress();
			log.debug("IP Address : "+ip);

			if(checkDailyClosingProcess != null && checkDailyClosingProcess.equalsIgnoreCase("true")) {
			String dataLibrary = "";
			 MethodSignature method = (MethodSignature)joinPoint.getSignature();
		     String[] parameters = method.getParameterNames();
		     if(parameters != null && parameters.length > 0){
		     for (int t = 0; t< parameters.length; t++) {
		            if(parameters[t].equalsIgnoreCase("dataLibrary")) {
		                Object[] obj = joinPoint.getArgs();
		                dataLibrary = String.valueOf(obj[t]);
		                log.debug("dataLibrary {} " +dataLibrary );
		            }
		        }
		     }
		     
		     boolean isTableExist = false;
			if (!dataLibrary.isEmpty()) {
				isTableExist = dbServiceRepository.isTableExist(dataLibrary,"E_CPSEND");
				if(isTableExist){
					log.info("E_CPSEND table exist inside DB /daily closing process is running");
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(), 
							ExceptionMessages.CHECK_DAILY_CLOSING_PROCESS_MSG_KEY));
					log.error(messageService.getReadableMessage(ExceptionMessages.CHECK_DAILY_CLOSING_PROCESS_MSG_KEY), exception);
					
					throw exception;
				}
				isTableExist = dbServiceRepository.isTableExist("QUSRSYS","XAINSTALL");
				if(isTableExist){
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(), 
							ExceptionMessages.CHECK_INSTALLATIO_UPDATE_PROCESS_MSG_KEY));
					log.error(messageService.getReadableMessage(ExceptionMessages.CHECK_INSTALLATIO_UPDATE_PROCESS_MSG_KEY), exception);
					throw exception;
				}
				
				isTableExist = dbServiceRepository.isTableExist(dataLibrary,"XAJAHWE");
				if(isTableExist){
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(), 
							ExceptionMessages.CHECK_END_OF_YEAR_PROCESS_MSG_KEY));
					log.error(messageService.getReadableMessage(ExceptionMessages.CHECK_END_OF_YEAR_PROCESS_MSG_KEY), exception);
					throw exception;
				}
				
				isTableExist = dbServiceRepository.isTableExist(decryptUtil.getDataLibrary(),"NOAP4USR");
				if(isTableExist){
					int checkCount = checkSecureUser(decryptUtil.getSchema(),decryptUtil.getLogedInUser());
					if(checkCount==0){
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(), 
							ExceptionMessages.CHECK_MAINTENANCE_WORK_PROCESS_MSG_KEY));
					log.error(messageService.getReadableMessage(ExceptionMessages.CHECK_MAINTENANCE_WORK_PROCESS_MSG_KEY), exception);
					throw exception;
					}
				}
				
			}
		}
		} catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.SEARCH_FAILED_MSG_KEY, "Closing/Installation Process"));
			log.error(messageService.getReadableMessage(ExceptionMessages.SEARCH_FAILED_MSG_KEY, "Closing/Installation Process"), exception);
			throw exception;
		}

	}

	private int checkSecureUser(String schema, String loginUser) {

		StringBuilder query = new StringBuilder("SELECT COUNT(*) as count FROM ").append(schema);
		query.append(".O_SECURE WHERE SECURE_USER=HASH('").append(loginUser).append("',2)");

		return Integer.parseInt(dbServiceRepository.getCountUsingQuery(query.toString()));
	}

	public int getCurrentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date now = new Date();
		String strDate = sdf.format(now);
		int time = Integer.parseInt(strDate.substring(11, 13));
		log.info("Current Time in Hrs:" + time);
		return time;
	}

}
