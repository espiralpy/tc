package com.avai.amp.prx.http.transfer;

import java.util.Date;

import com.avai.amp.prx.lib.LibraryApplication;

public class OcnRegisterTO {
	private String emailAddress;
	private String dateOfBirth;
	private String password;
	private String screenName;
	private String appId;

	public OcnRegisterTO(String screenName, String emailAddress, String password, Date birthday, String appId) {
		super();
		this.screenName = screenName;
		this.emailAddress = emailAddress;
		this.password = password;
		dateOfBirth = LibraryApplication.toISOString(birthday);
		this.appId = appId;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	/**
	 * @return the dateOfBirth
	 */
	public String getDateOfBirth() {
		return dateOfBirth;
	}

	/**
	 * @param dateOfBirth the dateOfBirth to set
	 */
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	/**
	 * @return the appId
	 */
	public String getAppId() {
		return appId;
	}

	/**
	 * @param appId the appId to set
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}

}
