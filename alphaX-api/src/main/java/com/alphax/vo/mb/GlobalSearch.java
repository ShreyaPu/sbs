package com.alphax.vo.mb;

import java.util.List;

public class GlobalSearch {
	
	private List<?> searchDetailsList = null;
	private String totalPages;
	private String totalRecordCnt;
	
	
	public String getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(String totalPages) {
		this.totalPages = totalPages;
	}
	public String getTotalRecordCnt() {
		return totalRecordCnt;
	}
	public void setTotalRecordCnt(String totalRecordCnt) {
		this.totalRecordCnt = totalRecordCnt;
	}
	
	/**
	 * @return the searchDetailsList
	 */
	public List<?> getSearchDetailsList() {
		return searchDetailsList;
	}
	/**
	 * @param searchDetailsList the searchDetailsList to set
	 */
	public void setSearchDetailsList(List<?> searchDetailsList) {
		this.searchDetailsList = searchDetailsList;
	}
	
	
	

}
