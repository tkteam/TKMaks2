package com.telekurye.tools;

public enum EnumBuildingTypes {

	Arsa("Arsa", 1), //
	Insaat("�n�aat", 2), //
	Mesken("Mesken", 3), //
	Kamu("Kamu", 4), //
	Ozel_Isyeri("�zel ��yeri", 5), //
	Banka("Banka", 6), //
	Otel("Otel", 7), //
	Ptt("PTT", 8), //
	Sanayi("Sanayi", 9), //
	Bufe("B�fe", 10), //
	Gecici_Yerlesim("Ge�ici Yerle�im", 11), //
	Seyyar("Seyyar", 12), //
	Site_Girisi("Site Giri�i", 13), //
	Yeralti_Carsisi("Yeralt� �ar��s�", 14), //
	Bina_Disi_Yapi("Bina D��� Yap�", 15), //
	Tahsis("Tahsis", 16), //
	Diger("Di�er", 17);

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
