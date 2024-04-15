package com.alphax.service.mb;

import java.util.Map;
import java.util.Set;

import com.ibm.as400.access.AS400;

public interface AS400UsersService {
	
	public AS400 getAs400users(String userAndClientIP);

	public void setAs400users(String userAndClientIP , AS400 as400);

	public AS400 getAs400Object();

	public void setAs400Object(AS400 as400Object);
	
	public boolean isUserAvailable(String userAndClientIP);
	
	public void clearCache();
	
	public Set<?> getAll();
	
	public Map<?, ?> getAs400Users();

}
