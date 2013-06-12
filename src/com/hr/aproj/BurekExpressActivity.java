package com.hr.aproj;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class BurekExpressActivity extends Activity implements OnClickListener {

	Button bPekara, bDate, bTime, bSearch;
	TextView tvPekara, tvDate, tvTime, tvLocation;
	Dialog dialog;
	MyPDialog myDialog;
	LocationManager lm;
	Context con = this;

	int mYear, mMonth, mDay, mHour, mMinute;
	static final int DATE_DIALOG_ID = 0, TIME_DIALOG_ID = 1,
			PEKARA_DIALOG_ID = 2;
	String name, towers, city = "";
	double lat = 0;
	double longi = 0;
	String userAddress = null;
	int i = 0, j = 0;

	//metoda postavlja trenutne vrijednosti u datum i vrijeme 
	//pri vracanju s mape na glavni izbornik
	private void resetSearchData() {
		SearchData.mjesto = city;
		setTimeDate();
		String monthString, dayString;
		int a = mMonth + 1;
		if (a < 10)
			monthString = "0" + a;
		else
			monthString = "" + a;

		if (mDay < 10)
			dayString = "0" + mDay;
		else
			dayString = "" + mDay;

		SearchData.datum = "" + mYear + "-" + monthString + "-" + dayString;
		String vrijeme = "" + pad(mHour) + ":" + pad(mMinute);
		SearchData.setVrijeme(vrijeme);
		
		tvTime.setText("");
		tvDate.setText(new StringBuilder().append(mDay).append(".")
				.append(mMonth + 1).append(".").append(mYear).append("."));
		tvPekara.setText("Sve pekare");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainscreen);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
				
		setVariables();
		resetSearchData();
		setTimeDate();
		
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				  
		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0,
				networkLocationListener);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100000, 0,
				gpsLocationListener);
	}

	//dohvaca adresu korisnika preko koordinata dobivenih GPS-om ili NetworkProvidera
	void getAddress() {
		Geocoder gcd = new Geocoder(con, Locale.getDefault());
		List<Address> addresses = null;
		try {
			addresses = gcd.getFromLocation(lat, longi, 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (addresses.size() > 0) {
			userAddress = addresses.get(0).getAddressLine(0).toString();
			city = addresses.get(0).getLocality();

			userAddress = userAddress + " " + city;
			tvLocation.setText(userAddress);

		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		lm.removeUpdates(networkLocationListener);
		lm.removeUpdates(gpsLocationListener);
	}

	@Override
	protected void onResume() {
		super.onResume();
		resetSearchData();
	}

	@Override
	protected void onStop() {
		super.onStop();
		lm.removeUpdates(networkLocationListener);
		lm.removeUpdates(gpsLocationListener);
	}

	// metoda za date i time dijaloge, stavlja trenutno vrijeme u dijaloge
	private void setTimeDate() {
		// get vrijeme i datum
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);

		SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
		Date d = new Date(mYear, mMonth, mDay);
		SearchData.dan = sdf.format(d);		
	}

	//postavljanje vremena koje je korisnik odabrao u dialogu
	void updateDisplayTime() {
		final Calendar c = Calendar.getInstance();
		int tYear = c.get(Calendar.YEAR);
		int tMonth = c.get(Calendar.MONTH);
		int tDay = c.get(Calendar.DAY_OF_MONTH);
		int tHour = c.get(Calendar.HOUR_OF_DAY);
		int tMinute = c.get(Calendar.MINUTE);

		if ((mHour < tHour && mYear == tYear && mMonth == tMonth && mDay == tDay)
				|| (mHour == tHour && mMinute < tMinute && mYear == tYear
						&& mMonth == tMonth && mDay == tDay)) {
			tvTime.setText("Krivo vrijeme");
			bSearch.setVisibility(View.INVISIBLE);

		} else if ((mYear < tYear) || (mYear == tYear && mMonth < tMonth)
				|| (mYear == tYear && mMonth == tMonth && mDay < tDay)) {
			tvTime.setText(new StringBuilder().append(pad(mHour)).append(":")
					.append(pad(mMinute)));
			bSearch.setVisibility(View.INVISIBLE);
		} else {
			tvTime.setText(new StringBuilder().append(pad(mHour)).append(":")
					.append(pad(mMinute)));
			bSearch.setVisibility(View.VISIBLE);
		}
	}

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);

	}

	//postavljanje datuma koji je korisnik odabrao u dialogu i odredivanje koji je dan u tjednu
	//zbog radnog vremena (je li radni dan, sub ili ned)
	void updateDisplayDate() {
		final Calendar c = Calendar.getInstance();
		int tYear = c.get(Calendar.YEAR);
		int tMonth = c.get(Calendar.MONTH);
		int tDay = c.get(Calendar.DAY_OF_MONTH);
		int tHour = c.get(Calendar.HOUR_OF_DAY);
		int tMinute = c.get(Calendar.MINUTE);
		
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
		Date d = new Date(mYear, mMonth, mDay);
		SearchData.dan = sdf.format(d);		
		
		//datum<danasnjeg
		if ((mYear < tYear) || (mYear == tYear && mMonth < tMonth)
				|| (mYear == tYear && mMonth == tMonth && mDay < tDay)) {
			if (!tvTime.equals("")) {
				tvTime.setText(new StringBuilder().append(pad(mHour))
						.append(":").append(pad(mMinute)));
				SearchData.setVrijeme(tvTime.getText().toString());
			}
			tvDate.setText("Krivi datum");
			bSearch.setVisibility(View.INVISIBLE);
			
		//datum dobar, vrijeme vec proslo
		} else if ((mYear == tYear && mMonth == tMonth && mDay == tDay && mHour < tHour)
				|| (mYear == tYear && mMonth == tMonth && mDay == tDay
						&& mHour == tHour && mMinute < tMinute)) {
			tvTime.setText("Krivo vrijeme");
			bSearch.setVisibility(View.INVISIBLE);
			tvDate.setText(new StringBuilder().append(mDay).append(".")
					.append(mMonth + 1).append(".").append(mYear).append("."));

			if (mMonth <= 8) {
				SearchData.setDatum((new StringBuilder().append(mYear)
						.append("-").append("0").append(mMonth + 1).append("-")
						.append(mDay)).toString());
			} else {
				SearchData.setDatum((new StringBuilder().append(mYear)
						.append("-").append(mMonth + 1).append("-")
						.append(mDay)).toString());
			}

		} else {
			if (!tvTime.getText().toString().equals("")) {
				tvTime.setText(new StringBuilder().append(pad(mHour))
						.append(":").append(pad(mMinute)));
				SearchData.setVrijeme(tvTime.getText().toString());
			}

			tvDate.setText(new StringBuilder().append(mDay).append(".")
					.append(mMonth + 1).append(".").append(mYear).append("."));

			if (mMonth <= 8) {
				SearchData.setDatum((new StringBuilder().append(mYear)
						.append("-").append("0").append(mMonth + 1).append("-")
						.append(mDay)).toString());
			} else {
				SearchData.setDatum((new StringBuilder().append(mYear)
						.append("-").append(mMonth + 1).append("-")
						.append(mDay)).toString());
			}

			bSearch.setVisibility(View.VISIBLE);
		}
	}

	//TimePickerDialogListener
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			if (minute == 0) {
				mMinute = 00;
				mHour = hourOfDay;
			} else {
				mMinute = 00;
				mHour = hourOfDay + 1;
			}
			tvTime.setVisibility(View.VISIBLE);
			updateDisplayTime();
			SearchData.setVrijeme(tvTime.getText().toString());
		}
	};

	//DatePickerDialogListener
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplayDate();
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute,
					true);
		}
		return null;
	}

	//inicijalizacija varijabli
	private void setVariables() {

		bPekara = (Button) findViewById(R.id.bPekara);
		bDate = (Button) findViewById(R.id.bDate);
		bTime = (Button) findViewById(R.id.bTime);
		tvPekara = (TextView) findViewById(R.id.tvPekara);
		tvDate = (TextView) findViewById(R.id.tvDate);
		tvTime = (TextView) findViewById(R.id.tvTime);
		bSearch = (Button) findViewById(R.id.bSearch);
		tvLocation = (TextView) findViewById(R.id.tvLocation);

		bSearch.setOnClickListener(this);

		myDialog = new MyPDialog(this, "", new OnReadyListener());

		bDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});

		bTime.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(TIME_DIALOG_ID);
			}
		});

		bPekara.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				myDialog.show();
			}
		});

	}

	// onClick za Search, prebacivanje na Mapu
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bSearch:
			Intent openPrikazPonude = new Intent(
					"android.intent.action.MAPA");
			startActivity(openPrikazPonude);
			break;
		}

	}


	private class OnReadyListener implements MyPDialog.ReadyListener {
		public void ready(String name) {
			tvPekara.setText(name);
			bSearch.setVisibility(View.VISIBLE);
		}
	}

	//GPS LocationListener
	final LocationListener gpsLocationListener = new LocationListener() {

		public void onLocationChanged(Location location) {
			lm.removeUpdates(gpsLocationListener);
			lat = location.getLatitude();
			longi = location.getLongitude();
			
			SearchData.setulat((int) (lat * 1E6));
			SearchData.setulongi((int) (longi * 1E6));
			
			getAddress();
			if (i == 0) {
				SearchData.setMjesto(city);
				i++;
			}
		}

		public void onProviderDisabled(String provider) {
			Toast.makeText(getApplicationContext(), "Gps Disabled",
					Toast.LENGTH_SHORT).show();
		}

		public void onProviderEnabled(String provider) {
			Toast.makeText(getApplicationContext(), "Gps Enabled",
					Toast.LENGTH_SHORT).show();
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
		}

	};

	//Network LocationListener
	private final LocationListener networkLocationListener = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		@Override
		public void onProviderEnabled(String provider) {
			Toast.makeText(getApplicationContext(), "Network Provider Enabled",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onProviderDisabled(String provider) {
			Toast.makeText(getApplicationContext(),
					"Network Provider Disabled", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onLocationChanged(Location location) {
			lat = location.getLatitude();
			longi = location.getLongitude();
			
			SearchData.setulat((int) (lat * 1E6));
			SearchData.setulongi((int) (longi * 1E6));
			
			getAddress();
			if (i == 0) {
				SearchData.setMjesto(city);
				i++;
				
			}

		}
	};

}