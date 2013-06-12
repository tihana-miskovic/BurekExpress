package com.hr.aproj;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonGetCoordinates {
	
	//dohvaca latitude i longitude vrijednosti pekara iz odgovora od baze
	public static List<String> GetCoordinates(String json_string) {
		List<JSONObject> podaciPekara = new ArrayList<JSONObject>();
		List<String> listaKoordinata = new ArrayList<String>();
		try {
			JSONArray json_array1 = new JSONArray(json_string);
			for (int i = 0; i < json_array1.length(); ++i) {
				podaciPekara.add(new JSONObject(json_array1.getString(i)));
			}
			
			for (JSONObject json_obj : podaciPekara) {
				listaKoordinata.add(json_obj.getString("nazPekara"));
				listaKoordinata.add(json_obj.getString("latitude"));
				listaKoordinata.add(json_obj.getString("longitude"));
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println("" + listaKoordinata);
		SearchData.listaKoordinata = listaKoordinata;
		return listaKoordinata;
	}


}
