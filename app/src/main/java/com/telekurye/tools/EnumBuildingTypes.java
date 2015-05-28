package com.telekurye.tools;

public enum EnumBuildingTypes {

	Arsa("Arsa", 1), //
	Insaat("Ýnþaat", 2), //
	Mesken("Mesken", 3), //
	Kamu("Kamu", 4), //
	Ozel_Isyeri("Özel Ýþyeri", 5), //
	Banka("Banka", 6), //
	Otel("Otel", 7), //
	Ptt("PTT", 8), //
	Sanayi("Sanayi", 9), //
	Bufe("Büfe", 10), //
	Gecici_Yerlesim("Geçici Yerleþim", 11), //
	Seyyar("Seyyar", 12), //
	Site_Girisi("Site Giriþi", 13), //
	Yeralti_Carsisi("Yeraltý Çarþýsý", 14), //
	Bina_Disi_Yapi("Bina Dýþý Yapý", 15), //
	Tahsis("Tahsis", 16), //
	Diger("Diðer", 17);

	private String	name;
	private int		id;

	private EnumBuildingTypes(String name, int id) {
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

}
