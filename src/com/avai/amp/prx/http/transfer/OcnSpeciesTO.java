package com.avai.amp.prx.http.transfer;

public class OcnSpeciesTO {
	private String speciesId;
	private String name;

	public OcnSpeciesTO(String speciesId, String name) {
		this.speciesId = speciesId;
		this.name = name;
	}

	public String getSpeciesId() {
		return speciesId;
	}

	public String getName() {
		return name;
	}

}
