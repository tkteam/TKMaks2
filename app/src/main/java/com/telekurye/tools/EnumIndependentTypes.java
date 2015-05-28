package com.telekurye.tools;

public enum EnumIndependentTypes {

	Mesken("Mesken", 1), //
	Huzurevi("Huzurevi", 2), //
	Ogrenci_Yurdu("Öðrenci Yurdu", 3), //
	Misafirhane("Misafirhane", 4), //
	Cocuk_Yurdu("Çocuk Yurdu", 5), //
	Otel("Otel", 6), //
	Banka_Subesi("Banka Þubesi", 7), //
	Ptt("PTT", 8), //
	Belediye("Belediye", 9), //
	Valilik_Kaymakamlik("Valilik/Kaymakamlýk", 10), //
	Muhtarlik("Muhtarlýk", 11), //
	Noter("Noter", 12), //
	Sanayi("Sanayi", 13), //
	Okul_Universite_Arastirma("Okul, Üniversite, Araþtýrma", 14), //
	Hastane_ve_Bakim_Kuruluslari("Hastane ve Bakým Kuruluþlarý", 15), //
	Eczane("Eczane", 16), //
	Ibadet_veya_Dini_Faaliyetler("Ýbadet veya Dini Faaliyetler", 17), //
	Polis("Polis", 18), //
	Silahli_Kuvvetler("Silahlý Kuvvetler", 19), //
	Cezaevi_Tutukevi("Cezaevi/Tutukevi", 20), //
	Itfaiye("Ýtfaiye", 21), //
	Kapici_Dairesi("Kapýcý Dairesi", 22);

	private String	name;
	private int		id;

	private EnumIndependentTypes(String name, int id) {
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
