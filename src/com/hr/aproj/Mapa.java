package com.hr.aproj;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;


public class Mapa extends MapActivity {
	
	String localhost = "10.0.2.2";
	
	private MapView mapview; 
	MapController mc;
    GeoPoint p; 
    private MapController mapControll;
    private GeoPoint geoPoint=null;
    private MyItemizedOverlay userPicOverlay;
    private MyItemizedOverlay nearPicOverlay,dubravicaPicOverlay,klaraPicOverlay,panpekPicOverlay;
    private Drawable userPic,bakeryPic,bakeryPicDubravica, bakeryPicKlara,bakeryPicPanPek;
    private OverlayItem nearbakery[] = new OverlayItem[70];
    public static Context context;
    List<String> lokacPekare = new ArrayList<String>();
	List<Overlay> mapOverlays;
    
    
	//Klasa za prikaz lokacija pekara
    public class MyItemizedOverlay extends ItemizedOverlay<OverlayItem> {

        private ArrayList<OverlayItem> myOverlays = new ArrayList<OverlayItem>();


        public MyItemizedOverlay(Drawable defaultMarker) {
            super(boundCenterBottom(defaultMarker));
            populate();
        }

        public void addOverlay(OverlayItem overlay){
            myOverlays.add(overlay);
            populate();
        }

        @Override
        protected OverlayItem createItem(int i) {
            return myOverlays.get(i);
        }

        public void removeItem(int i){
            myOverlays.remove(i);
            populate();
        }

        @Override
        public int size() {
            return myOverlays.size();
        }


        public void addOverlayItem(OverlayItem overlayItem) {
            myOverlays.add(overlayItem);
            populate();
        }


        public void addOverlayItem(int lat, int lon, String title) {
            try {
                GeoPoint point = new GeoPoint(lat, lon);
                OverlayItem overlayItem = new OverlayItem(point, title, null);
                addOverlayItem(overlayItem);    
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected boolean onTap(int index) {
            String title = myOverlays.get(index).getTitle();
            Toast.makeText(Mapa.context, title, Toast.LENGTH_LONG).show();
            return super.onTap(index);
        }
    }

    
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_gmaps);
             
        findGeopoints();	
        showMap();
		
	}
	
	//postavljanje pinova na mapu
	//postavlja se lokacija korisnika i lokacije pekara koje odgovaraju rezultatima pretrazivanja
	public void showMap() {

        try {
            geoPoint = new GeoPoint(SearchData.ulatitude,SearchData.ulongitude);   
            mapview = (MapView)findViewById(R.id.mapView);
            mapControll= mapview.getController();
            mapview.setBuiltInZoomControls(true);
            mapview.setStreetView(true);
            mapControll.setZoom(15);
            mapControll.animateTo(geoPoint);

            //Prikaz moje pozicije
            userPic = this.getResources().getDrawable(R.drawable.pin2);
            userPicOverlay = new MyItemizedOverlay(userPic);
            OverlayItem overlayItem = new OverlayItem(geoPoint, "I'm Here!!!", null);
            
            userPicOverlay.addOverlay(overlayItem);
            mapview.getOverlays().add(userPicOverlay);

            
            //prikaz ostalih lokacija, tj pekara
            bakeryPic = this.getResources().getDrawable(R.drawable.pinn);
            nearPicOverlay = new MyItemizedOverlay(bakeryPic);
            
            bakeryPicDubravica = this.getResources().getDrawable(R.drawable.dubravica);
            dubravicaPicOverlay = new MyItemizedOverlay(bakeryPicDubravica);
            
            bakeryPicKlara = this.getResources().getDrawable(R.drawable.klara);
            klaraPicOverlay = new MyItemizedOverlay(bakeryPicKlara);
            
            bakeryPicPanPek = this.getResources().getDrawable(R.drawable.panpek);
            panpekPicOverlay = new MyItemizedOverlay(bakeryPicPanPek);
            
            
            int i = 0;
            for (int y = 1; y < lokacPekare.size(); y += 3) {
				Integer lat = Integer.parseInt(lokacPekare.get(y));
				Integer lon = Integer.parseInt(lokacPekare.get(y+1));
				
				String name = lokacPekare.get(y-1);
				
                nearbakery[i] = new OverlayItem(new GeoPoint(lat, lon),name, null);
				System.out.println(""+ name + " " + lat+" "+lon);
				if (name.equals("Dubravica") ){ 
						dubravicaPicOverlay.addOverlay(nearbakery[i]);
				}
				if (name.equals("Klara") ){ 
					klaraPicOverlay.addOverlay(nearbakery[i]);
			}
				if (name.equals("Pan-pek") ){ 
					panpekPicOverlay.addOverlay(nearbakery[i]);
			}
                i++;
            }
            
            mapview.getOverlays().add(panpekPicOverlay);
            mapview.getOverlays().add(klaraPicOverlay);
            mapview.getOverlays().add(dubravicaPicOverlay);
            mapview.getOverlays().add(nearPicOverlay);
            
            mapview.postInvalidate();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	
	//slanje url get upita (pozivanje php skripte)
	//salju se parametri - mjesto, vrijeme, dan u tjednu i ime odabrane pekare
	public void findGeopoints () {
		
		String upitPonuda = "http://" + localhost + "/android/popisPekara.php?";
    	String akcija;
                
		if (SearchData.getPekara().equals("n")
				|| SearchData.getVrijeme().equals("n")){
			if (SearchData.dan.equals("Monday"))
				akcija = "Apopis3";
			else if (SearchData.dan.equals("Sunday"))
				akcija = "Apopis2";
			else
				akcija = "Apopis1";
		}
		else {
			if (SearchData.dan.equals("Monday"))
				akcija = "Bpopis3";
			else if (SearchData.dan.equals("Sunday"))
				akcija = "Bpopis2";
			else
				akcija = "Bpopis1";
		}

		String mjesto = Uri.encode(SearchData.mjesto);
		String urlUpit = upitPonuda + "akcija=" + akcija + "&mjesto=" + mjesto
				+ "&pekara=" + SearchData.getPekara() + "&vrijeme="
				+ SearchData.getVrijeme();
		System.out.println("UPIT    "+urlUpit);

		String odgovor = (String) Json.GetJSONResponse(urlUpit);
		lokacPekare = JsonGetCoordinates.GetCoordinates(odgovor);

	}
	
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}
