package com.avai.amp.prx.http.transfer;

public class OcnFollowerTO {
	private String userId;
	private String screenName;
	private String fullName;
	private String profilePicUrl;

	public String getUserId() {
		return userId;
	}

	public String getScreenName() {
		return screenName;
	}

	public String getFullName() {
		return fullName;
	}

	public String getProfilePicUrl() {
		return profilePicUrl;
	}

	public OcnFollowerTO(String userId, String screenName, String fullName, String profilePicUrl) {
		this.userId = userId;
		this.screenName = screenName;
		this.fullName = fullName;
		this.profilePicUrl = profilePicUrl;
	}

}
