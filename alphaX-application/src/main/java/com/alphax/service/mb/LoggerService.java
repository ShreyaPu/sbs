package com.alphax.service.mb;

import java.io.ByteArrayInputStream;

public interface LoggerService  {

	public String getLogsInformation(String dataCenterLibValue);
	
	public ByteArrayInputStream downloadLogFile(String dataCenterLibValue);
	
}
