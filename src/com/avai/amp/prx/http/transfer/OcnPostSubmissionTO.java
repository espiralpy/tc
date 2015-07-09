package com.avai.amp.prx.http.transfer;

import java.util.Date;

import com.avai.amp.prx.lib.LibraryApplication;

import android.text.format.DateFormat;

public class OcnPostSubmissionTO {
	private String Type;
	private String date;
	private String company;
	private String state;
	private String county;
	private String notes;
	private OcnLocation location;
	private String[] photos;

	// the optional fields
	private String species;
	private int size;
	private String weapon;
	private String bait;

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public Date getDate() {
		return LibraryApplication.toISODate(date);
	}

	public void setDate(Date date) {
		this.date = LibraryApplication.toISOString(date);
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public OcnLocation getLocation() {
		return location;
	}

	public void setLocation(OcnLocation Location) {
		this.location = Location;
	}

	public String[] getPhotos() {
		return photos;
	}

	public void setPhotos(String[] photos) {
		this.photos = photos;
	}

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getWeapon() {
		return weapon;
	}

	public void setWeapon(String weapon) {
		this.weapon = weapon;
	}

	public String getBait() {
		return bait;
	}

	public void setBait(String bait) {
		this.bait = bait;
	}
}
