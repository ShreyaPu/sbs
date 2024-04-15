/**
 * 
 */
package com.alphax.common.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * @author A106744104
 *
 */
@JsonView
public class AlphaXResponse<T> {

	private T resultSet;
	List<ResponseMessage> responseMessages;

	/**
	 * @return the resultSet
	 */
	public T getResultSet() {
		return resultSet;
	}

	/**
	 * @param resultSet the resultSet to set
	 */
	public void setResultSet(T resultSet) {
		this.resultSet = resultSet;
	}

	/**
	 * @return the responseMessages
	 */
	public List<ResponseMessage> getResponseMessages() {
		return responseMessages;
	}

	/**
	 * @param responseMessages the responseMessages to set
	 */
	public void setResponseMessages(List<ResponseMessage> responseMessages) {
		this.responseMessages = responseMessages;
	}
	
}