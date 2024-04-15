package com.alphax.vo.mb;


import io.swagger.annotations.ApiModel;

@ApiModel(description = "All fields about AuthorityCheck")
public class AuthorityCheck {
	
	private boolean opertionInUse;
	

	public boolean isOpertionInUse() {
		return opertionInUse;
	}

	public void setOpertionInUse(boolean opertionInUse) {
		this.opertionInUse = opertionInUse;
	}

}
