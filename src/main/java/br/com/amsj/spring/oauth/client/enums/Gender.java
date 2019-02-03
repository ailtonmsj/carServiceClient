package br.com.amsj.spring.oauth.client.enums;

public enum Gender {
	
	MALE("Male", "M"),
	FEMALE("Female", "F");
	
	private Gender(String description, String acronym) {
		this.description = description;
		this.acronym = acronym;
	}
	
	private final String description;
	private final String acronym;
	
	public String getDescription() {
		return description;
	}
	public String getAcronym() {
		return acronym;
	}
}
