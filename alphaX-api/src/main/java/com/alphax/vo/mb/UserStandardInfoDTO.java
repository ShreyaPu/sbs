package com.alphax.vo.mb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "This Object about extended user parameters and informations")
public class UserStandardInfoDTO {
	
	@ApiModelProperty(value = "Benutzer-Rolle - DB UBF_NUTZRO - NRO_ROLLE")
	String userRole;
	
	@ApiModelProperty(value = "Vorname des Benutzers - DB UBF_NUTZER - BNU_VNAME")
	String userFirstName;
	
	@ApiModelProperty(value = "Benutzer Nachname - DB UBF_NUTZER - BNU_NNAME")
	String userLastName;
	
	@ApiModelProperty(value = "Benutzerteam - DB UBF_NUTZTE - NTE_TEAM")
	String userTeam;

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getUserFirstName() {
		return userFirstName;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	public String getUserLastName() {
		return userLastName;
	}

	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}

	public String getUserTeam() {
		return userTeam;
	}

	public void setUserTeam(String userTeam) {
		this.userTeam = userTeam;
	}
	
	

}
