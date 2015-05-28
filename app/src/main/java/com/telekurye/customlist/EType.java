package com.telekurye.customlist;

public enum EType {

	City(4, "City"), County(5, "County"), District(8, "District");

	private int		id;
	private String	name;

	private EType(int id, String name) {

		this.id = id;
		this.name = name;

	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
