package com.alphax.vo.mb;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "This Object about Part BA Details")
public class partBADetailsDTO {
	
    private String value;
	
	private String predecessor;
	
	private String successor;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getPredecessor() {
		return predecessor;
	}

	public void setPredecessor(String predecessor) {
		this.predecessor = predecessor;
	}

	public String getSuccessor() {
		return successor;
	}

	public void setSuccessor(String successor) {
		this.successor = successor;
	}
	
	

}
