package com.alphax.service.mb.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.alphax.service.mb.AS400UsersService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JobScheduler {
	
	@Autowired
	AS400UsersService as400UsersService;
	
	@Scheduled(cron = "0 0/30 23 * * *")
	   public void objectRemoveSch() {
		
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	      Date now = new Date();
	      String strDate = sdf.format(now);
	      log.info("AS400 objects cleanup process start at :: " + strDate);
	      as400UsersService.clearCache();
	      as400UsersService.setAs400Object(null);
	      log.info("AS400 objects cleanup process end at :: " + strDate);
	      
	}
}


