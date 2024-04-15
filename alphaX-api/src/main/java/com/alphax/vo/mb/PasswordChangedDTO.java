package com.alphax.vo.mb;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All fields about to changed password")
public class PasswordChangedDTO {
	
	@ApiModelProperty(value = "User login Id")
	@NotBlank
	private String loginUserId;
	
	@ApiModelProperty(value = "User old password")
	@NotBlank
	private String oldPassword;
	
	@ApiModelProperty(value = "User new password")
	@NotBlank
	private String newPassword;
	
	@ApiModelProperty(value = "confirm Password")
	@NotBlank
	private String confirmPassword;

	public String getLoginUserId() {
		return loginUserId;
	}

	public void setLoginUserId(String loginUserId) {
		this.loginUserId = loginUserId;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	
	
	

}
