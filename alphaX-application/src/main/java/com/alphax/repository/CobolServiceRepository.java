package com.alphax.repository;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.alphax.common.rest.message.service.MessageService;
import com.alphax.common.exception.AlphaXException;
import com.alphax.common.exception.ExceptionMessages;
import com.alphax.service.mb.AS400UsersService;
import com.alphax.service.mb.impl.EnvironmentSetupService;
import com.alphax.common.constants.Program_Commands_Constants;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Exception;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ErrorCompletingRequestException;
import com.ibm.as400.access.ObjectDoesNotExistException;
import com.ibm.as400.access.ProgramCall;
import com.ibm.as400.access.ProgramParameter;

import lombok.extern.slf4j.Slf4j;

@Service("cobolServiceRepository")
@Slf4j
public class CobolServiceRepository {

	@Autowired
	AS400UsersService as400UsersService;

	@Autowired
	private MessageService messageService;

	@Autowired
	EnvironmentSetupService environmentSetup;

	/**
	 * This method is used to execute Cobol Command - and add respective error message
	 * @param cmd
	 * @param commandName
	 * @throws AS400Exception
	 */
	public CommandCall getCommandCallObject(){

		AS400 as400 = as400UsersService.getAs400Object();

		if(as400==null){

			AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.AUTH_FAILED_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.AUTH_FAILED_MSG_KEY), exception);
			throw exception;
		}

		CommandCall cmd = new CommandCall(as400);

		return cmd;
	}


	public void executeCommand(CommandCall cmd, String commandName) {
		log.info("Executing command- {}", commandName);
		ExecutorService executor = Executors.newCachedThreadPool();
		Future<Boolean> futureTask = executor.submit(() -> executeCobolCommand(cmd, commandName) );

		try {
			if(futureTask.get(15, TimeUnit.SECONDS)) {
				log.info("Command - {} executed successfully...", commandName);
			}
			else {
				log.info("Command - {} Call Not successfull...", commandName);
				AS400Message[] messagelist = cmd.getMessageList();
				for (int count = 0; count < messagelist.length; count++) {
					// Show each message.
					log.info("System message: {}", messagelist[count].getText());
				}
			}
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			log.info("Error Message:"+e.getMessage());
			futureTask.cancel(true);
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.COBOL_TIMEOUT_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.COBOL_TIMEOUT_MSG_KEY), exception);
			throw exception;
		}
		finally {
			// shut down the executor manually
			executor.shutdownNow();
		}

	}


	/**
	 * This method is used to execute Cobol Command
	 * @param cmd
	 * @param commandName
	 * @return
	 */
	private boolean executeCobolCommand(CommandCall cmd, String commandName) {
		boolean result = false;
		try {
			result = cmd.run(commandName);
		} catch (AS400SecurityException | ErrorCompletingRequestException | IOException | InterruptedException
				| PropertyVetoException e) {
			log.info("Error Message:"+e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.COBOL_COMMAND_FAIL_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.COBOL_COMMAND_FAIL_MSG_KEY), exception);
			throw exception;
		}

		return result;
	}


	/**
	 * This method is used to execute Cobol Program.
	 * @param as400
	 * @param parmList
	 * @param OutputStartLocation
	 * @param programName
	 * @return
	 * @throws AS400Exception
	 * @throws PropertyVetoException 
	 * @throws ObjectDoesNotExistException 
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws ErrorCompletingRequestException 
	 * @throws AS400SecurityException 
	 */
	public List<String> executeProgram(ProgramParameter[] parmList, int	OutputStartLocation, String programName) throws Exception {

		log.info("Attempting to run the IBM iSeries program...{}", programName);
		List<String> outputList = new ArrayList<>();
		Future<Boolean>	futureTask = null;

		AS400 as400 = as400UsersService.getAs400Object(); 
		if(as400 == null){
			AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.AUTH_FAILED_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.AUTH_FAILED_MSG_KEY), exception);
			throw exception;
		}

		ProgramCall programCall = new ProgramCall(as400);
		programCall.setProgram(programName, parmList); 

		String jobId = programCall.getServerJob().getNumber();
		log.info("JOB number :- {}", jobId);
		
		checkConnectionWithCOBOL(as400, jobId);
		
		ExecutorService executor = Executors.newCachedThreadPool(); 
		try {
			
			futureTask = executor.submit(() -> executeCobolProgram(programCall) ); 

			if(futureTask.get(60, TimeUnit.SECONDS)) {
				log.info("Program - {} Call successfull...", programName);

				for(int i=OutputStartLocation; i<parmList.length;i++) {

					String output = new String(parmList[i].getOutputData(),	Program_Commands_Constants.IBM_273).trim();
					//log.info("Output# is: " +	output); 
					outputList.add(output); 
				} 
			} 
			else {
				log.info("Program - {} Call Not successfull...", programName); 
				AS400Message[] messageList = programCall.getMessageList(); 
				for (AS400Message message :	messageList) 
				{ 
					log.info("Error Message for messageId: {} - {}",message.getID(), message.getText()); 
					outputList.add("Error "+message.getID()	+ " - " + message.getText()); 
				}
			} 
		}
		catch (TimeoutException e) {
			log.info("Error Message:"+e.getMessage()); 
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.COBOL_TIMEOUT_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.COBOL_TIMEOUT_MSG_KEY));
			
			futureTask.cancel(true);
			as400.disconnectAllServices();

			//end the JOB
			executeCommand(getCommandCallObject(), "ENDJOB Job(" +jobId + "/QUSER/QZRCSRVS) OPTION(*IMMED)");

			environmentSetup.setUserEnvironment(as400.getUserId());
			//environmentSetup.getUserRoleAndCompany();

			throw exception;
		} 
		finally { 
			// shut down the executor manually 
			executor.shutdownNow(); 
		}
		log.info("IBM iSeries program end....{}", programName); 
		return outputList; 
	}
	
	
	/**
	 * This method is used to test the connection with COBOL.
	 * 
	 * @param as400
	 * @param jobId
	 * @throws Exception 
	 */
	private void checkConnectionWithCOBOL(AS400 as400, String jobId ) throws Exception {

		if(!as400.isConnected(AS400.COMMAND)) {
			log.info("COBOL System is disconnected... so setting up new environment...");
			as400.disconnectAllServices();

			//end the JOB
			executeCommand(getCommandCallObject(), "ENDJOB Job(" +jobId + "/QUSER/QZRCSRVS) OPTION(*IMMED)");

			environmentSetup.setUserEnvironment(as400.getUserId());
			//environmentSetup.getUserRoleAndCompany();
		}		
	}


	/**
	 * This method is used to execute Cobol Program.
	 * @param programCall
	 * @return
	 */

	private Boolean executeCobolProgram(ProgramCall programCall){ 
		boolean result  = false; 
		try { 
			result = programCall.run();
		} 
		catch (AS400SecurityException | ErrorCompletingRequestException |
				IOException | ObjectDoesNotExistException e) {
			log.info("Error Message:"+e.getMessage()); 
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.COBOL_PROGRAM_FAIL_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.COBOL_PROGRAM_FAIL_MSG_KEY), exception);

			throw  exception; 
		} 
		catch(InterruptedException ex) 
		{ 
			log.error(ex.toString()); 
		}
		return result; 
	}

	
	public boolean executeCommandForFile(String commandName) {
		log.info("Executing command- {}", commandName);
		CommandCall cmd = getCommandCallObject();
		ExecutorService executor = Executors.newCachedThreadPool();
		Future<Boolean> futureTask = executor.submit(() -> executeCobolCommand(cmd, commandName) );
        boolean fileExistCheck = false;
		try {
			if(futureTask.get(15, TimeUnit.SECONDS)) {
				log.info("Command - {} executed successfully...", commandName);
				fileExistCheck = true;
			}
			else {
				log.info("Command - {} Call Not successfull...", commandName);
				AS400Message[] messagelist = cmd.getMessageList();
				for (int count = 0; count < messagelist.length; count++) {
					// Show each message.
					log.info("System message: {}", messagelist[count].getText());
				}
			}
			return fileExistCheck;
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			log.info("Error Message:"+e.getMessage());
			futureTask.cancel(true);
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.COBOL_TIMEOUT_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.COBOL_TIMEOUT_MSG_KEY), exception);
			throw exception;
		}
		finally {
			// shut down the executor manually
			executor.shutdownNow();
		}

	}
	
	
	/**
	 * This method is used to execute Cobol Program.
	 * @param as400
	 * @param parmList
	 * @param OutputStartLocation
	 * @param programName
	 * @return
	 * @throws AS400Exception
	 * @throws PropertyVetoException 
	 * @throws ObjectDoesNotExistException 
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws ErrorCompletingRequestException 
	 * @throws AS400SecurityException 
	 */
	public List<String> executeProgramWithoutTrim(ProgramParameter[] parmList, int	OutputStartLocation, String programName) throws Exception {

		log.info("Attempting to run the IBM iSeries program...{}", programName);
		List<String> outputList = new ArrayList<>();
		Future<Boolean>	futureTask = null;

		AS400 as400 = as400UsersService.getAs400Object(); 
		if(as400 == null){
			AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.AUTH_FAILED_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.AUTH_FAILED_MSG_KEY), exception);
			throw exception;
		}

		ProgramCall programCall = new ProgramCall(as400);
		programCall.setProgram(programName, parmList); 

		String jobId = programCall.getServerJob().getNumber();
		log.info("JOB number :- {}", jobId);
		
		checkConnectionWithCOBOL(as400, jobId);
		
		ExecutorService executor = Executors.newCachedThreadPool(); 
		try {
			
			futureTask = executor.submit(() -> executeCobolProgram(programCall) ); 

			if(futureTask.get(60, TimeUnit.SECONDS)) {
				log.info("Program - {} Call successfull...", programName);

				for(int i=OutputStartLocation; i<parmList.length;i++) {

					String output = new String(parmList[i].getOutputData(),	Program_Commands_Constants.IBM_273);
					//log.info("Output# is: " +	output); 
					outputList.add(output); 
				} 
			} 
			else {
				log.info("Program - {} Call Not successfull...", programName); 
				AS400Message[] messageList = programCall.getMessageList(); 
				for (AS400Message message :	messageList) 
				{ 
					log.info("Error Message for messageId: {} - {}",message.getID(), message.getText()); 
					outputList.add("Error "+message.getID()	+ " - " + message.getText()); 
				}
			} 
		}
		catch (TimeoutException e) {
			log.info("Error Message:"+e.getMessage()); 
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.COBOL_TIMEOUT_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.COBOL_TIMEOUT_MSG_KEY));
			
			futureTask.cancel(true);
			as400.disconnectAllServices();

			//end the JOB
			executeCommand(getCommandCallObject(), "ENDJOB Job(" +jobId + "/QUSER/QZRCSRVS) OPTION(*IMMED)");

			environmentSetup.setUserEnvironment(as400.getUserId());
			//environmentSetup.getUserRoleAndCompany();

			throw exception;
		} 
		finally { 
			// shut down the executor manually 
			executor.shutdownNow(); 
		}
		log.info("IBM iSeries program end....{}", programName); 
		return outputList; 
	}
	

	/**
	 * This method is used to execute Cobol Program.
	 * @param as400
	 * @param parmList
	 * @param OutputStartLocation
	 * @param programName
	 * @return
	 * @throws AS400Exception
	 * @throws PropertyVetoException 
	 * @throws ObjectDoesNotExistException 
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws ErrorCompletingRequestException 
	 * @throws AS400SecurityException 
	 */
	/*	public List<String> executeProgram(ProgramParameter[] parmList, int OutputStartLocation, String programName) throws PropertyVetoException, AS400SecurityException, ErrorCompletingRequestException, IOException, ObjectDoesNotExistException  {

		log.info("Attempting to run the IBM iSeries program...{}", programName);
		List<String> outputList = new ArrayList<>();

		AS400 as400 = as400UsersService.getAs400Object();

		if(as400==null){
			AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.AUTH_FAILED_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.AUTH_FAILED_MSG_KEY), exception);
			throw exception;
		}
		ProgramCall programCall = new ProgramCall(as400);
		programCall.setProgram(programName, parmList);
		programCall.setTimeOut(15);

		try {

			log.info("JOB Name :-"+ programCall.getServerJob().getName());
			log.info("JOB number :- "+ programCall.getServerJob().getNumber());

			if(programCall.run()) {
				log.info("Program - {} Call successfull...", programName);

				for(int i=OutputStartLocation; i<parmList.length;i++) {
					String output = new String(parmList[i].getOutputData(), Program_Commands_Constants.IBM_273);
					//log.info("Output# is: " + output);
					outputList.add(output);
				}
			}
			else {
				log.info("Program - {} Call Not successfull...", programName);
				AS400Message[] messageList = programCall.getMessageList();
				for (AS400Message message : messageList) {
					log.info("Error Message for messageId: {} - {}", message.getID(), message.getText());
					outputList.add("Error "+message.getID() + " - " + message.getText());
				}

			}
		} catch (InterruptedException | ConnectionDroppedException  e) {
			log.info("Error Message:"+e.getMessage());
			as400.disconnectAllServices();
			log.info("JOBS :- "+Arrays.toString(as400.getJobs(AS400.COMMAND)));
//			as400.resetAllServices();
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.COBOL_TIMEOUT_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.COBOL_TIMEOUT_MSG_KEY), exception);
			throw exception;
		} 

		log.info("IBM iSeries program end....{}", programName);
		return outputList;
	}
	 */


}