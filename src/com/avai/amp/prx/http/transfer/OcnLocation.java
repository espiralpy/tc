package com.avai.amp.prx.http.transfer;

public class OcnLocation {
	private float latitude;
	private float longitude;

	public OcnLocation(float lat, float lon) {
		this.latitude = lat;
		this.longitude = lon;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLocation(float latitude, float longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public float getLongitude() {
		return longitude;
	}
}
