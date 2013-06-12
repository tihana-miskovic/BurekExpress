package com.hr.aproj;

import java.util.ArrayList;
import java.util.List;

public class SearchData {

	public static String dan = "Monday";
	public static String pekara = "n";
	public static String mjesto = "";
	public static String vrijeme = "n";
	public static String datum = "";
	public static String tipObjekta = "n";
	public static int ulatitude = 45801121;
	public static int ulongitude = 15970841;
	
	//postavke za emulator
	//45814912,15978514 Centar
	
	public static List<String> listaKoordinata = new ArrayList<String>();


	public static String getPekara() {
		return pekara;
	}

	public static void setPekara(String pekara) {
		SearchData.pekara = pekara;
	}

	public static String getMjesto() {
		return mjesto;
	}

	public static void setMjesto(String mjesto) {
		SearchData.mjesto = mjesto;
	}

	public static String getVrijeme() {
		return vrijeme;
	}

	public static void setVrijeme(String vrijeme) {

		SearchData.vrijeme = vrijeme;
		SearchData.vrijeme += ":00";
	}

	public static String getDatum() {
		return datum;
	}

	public static void setDatum(String datum) {
		SearchData.datum = datum;
	}

	
	public static String getDan() {
		return dan;
	}

	public static void setDan(String dan) {
		SearchData.dan = dan;
	}
	
	public static Integer getulat() {
		return ulatitude;
	}

	public static void setulat(Integer lat) {
		SearchData.ulatitude = lat;
	}
	
	public static Integer getulongi() {
		return ulatitude;
	}

	public static void setulongi(Integer longi) {
		SearchData.ulongitude = longi;
	}
	
	public static void setListCoordinates(List<String> list) {
		SearchData.listaKoordinata = list;
	}
	
	public static List<String> getListCoordinates() {
		return listaKoordinata;
	}

	
	
	
}
