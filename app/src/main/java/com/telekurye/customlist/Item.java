package com.telekurye.customlist;

public interface Item {
	public boolean isSectionItem();
}

class Items implements Item, Comparable<Items> {

	private int id;
	private String name;
	private int typeId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public boolean isSectionItem() {
		return false;
	}

	@Override
	public int compareTo(Items another) {
		return this.getName().compareTo(another.getName());
	}

}

class ItemsSections implements Item {

	private char sectionLetter;

	public char getSectionLetter() {
		return sectionLetter;
	}

	public void setSectionLetter(char sectionLetter) {
		this.sectionLetter = sectionLetter;
	}

	public boolean isSectionItem() {
		return true;
	}

}