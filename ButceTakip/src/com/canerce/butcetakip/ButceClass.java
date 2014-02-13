package com.canerce.butcetakip;

import java.util.Date;

public class ButceClass {

	int id;
	Float miktar;
	String aciklama;
	int tur;
	Date tarih;

	public Date getTarih() {
		return tarih;
	}

	public void setTarih(Date tarih) {
		this.tarih = tarih;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Float getMiktar() {
		return miktar;
	}

	public void setMiktar(Float miktar) {
		this.miktar = miktar;
	}

	public String getAciklama() {
		return aciklama;
	}

	public void setAciklama(String aciklama) {
		this.aciklama = aciklama;
	}

	public int getTur() {
		return tur;
	}

	public void setTur(int tur) {
		this.tur = tur;
	}

}
