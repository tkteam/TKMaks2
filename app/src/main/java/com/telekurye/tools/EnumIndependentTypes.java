package com.telekurye.tools;

public enum EnumIndependentTypes {

	Mesken("Mesken", 1), //
	Huzurevi("Huzurevi", 2), //
	Ogrenci_Yurdu("��renci Yurdu", 3), //
	Misafirhane("Misafirhane", 4), //
	Cocuk_Yurdu("�ocuk Yurdu", 5), //
	Otel("Otel", 6), //
	Banka_Subesi("Banka �ubesi", 7), //
	Ptt("PTT", 8), //
	Belediye("Belediye", 9), //
	Valilik_Kaymakamlik("Valilik/Kaymakaml�k", 10), //
	Muhtarlik("Muhtarl�k", 11), //
	Noter("Noter", 12), //
	Sanayi("Sanayi", 13), //
	Okul_Universite_Arastirma("Okul, �niversite, Ara�t�rma", 14), //
	Hastane_ve_Bakim_Kuruluslari("Hastane ve Bak�m Kurulu�lar�", 15), //
	Eczane("Eczane", 16), //
	Ibadet_veya_Dini_Faaliyetler("�badet veya Dini Faaliyetler", 17), //
	Polis("Polis", 18), //
	Silahli_Kuvvetler("Silahl� Kuvvetler", 19), //
	Cezaevi_Tutukevi("Cezaevi/Tutukevi", 20), //
	Itfaiye("�tfaiye", 21), //
	Kapici_Dairesi("Kap�c� Dairesi", 22);

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
