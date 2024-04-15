package com.alphax.service.mb.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.alphax.service.mb.AS400UsersService;
import com.ibm.as400.access.AS400;

@Service
public class AS400UsersServiceImpl implements AS400UsersService {
	
	@Autowired
	CacheManager cacheManager;
	
	Map<String , Object> as400users = new HashMap<>();
	
	private AS400 as400Object;
	
	@Override
	@Cacheable("as400Cache")
	public AS400 getAs400users(String userAndClientIP) {
		return (AS400) as400users.get(userAndClientIP);
	}

	@Override
	public void setAs400users(String userAndClientIP , AS400 as400) {
		
		as400users.put(userAndClientIP, as400);
	}

	@Override
	public AS400 getAs400Object() {
		return this.as400Object;
	}

	@Override
	public void setAs400Object(AS400 as400Object) {
		this.as400Object = as400Object;
		
	}

	@Override
	public boolean isUserAvailable(String userAndClientIP) {
		
		return as400users.containsKey(userAndClientIP);
	}

	@Override
	public void clearCache() {
		this.as400users.clear();
		this.as400Object=null;
		cacheManager.getCache("as400Cache").clear();
		
	}

	@Override
	public Set<?> getAll() {
		return as400users.entrySet();
		
	}

	@Override
	public Map getAs400Users() {
		return as400users;
	}

}
