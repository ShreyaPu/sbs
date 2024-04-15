package com.alphax.vo.mb;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about AlphaX Admin setting for Login users")
public class AdminLoginUsersDTO {

	
	private String userFirstName;
	
	private String userLastName;
	
	private String userLoginName;
	
	
	
	/**
	 * @return the userName
	 */
	@ApiModelProperty(value = "User First Name ( %% - DB UBF_NUTZER - FIRSTNAME (BNU_VNAME)).")
	public String getUserFirstName() {
		return userFirstName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}
	
	/**
	 * @return the userName
	 */
	@ApiModelProperty(value = "User Last Name ( %% - DB UBF_NUTZER - LASTNAME (BNU_NNAME)).")
	public String getUserLastName() {
		return userLastName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}
	

	/**
	 * @return the userLoginName
	 */
	@ApiModelProperty(value = "User LoginName ( %% - DB UBF_NUTZER - LOGIN (BNU_NUTZER) ).")
	public String getUserLoginName() {
		return userLoginName;
	}

	/**
	 * @param userLoginName the userLoginName to set
	 */
	public void setUserLoginName(String userLoginName) {
		this.userLoginName = userLoginName;
	}

	
}
