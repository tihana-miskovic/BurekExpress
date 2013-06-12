package com.hr.aproj;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MyPDialog extends Dialog implements OnCheckedChangeListener {

	String pekaraName;
	String pekaraSend="n";
	ReadyListener readyListener;
	RadioGroup rgPekara;

	public MyPDialog(Context context, String name,
			ReadyListener readyListener) {
		super(context);
		this.pekaraName = name;
		this.readyListener = readyListener;
	}

	public interface ReadyListener {
		public void ready(String name);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pekaradialog);
		setTitle("Odaberite pekaru");
		Button bSave = (Button) findViewById(R.id.bSave);
		RadioGroup selectionList = (RadioGroup) findViewById(R.id.rgAnswers);
		selectionList.setOnCheckedChangeListener(this);
		bSave.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				readyListener.ready(pekaraName);
				MyPDialog.this.dismiss();
				SearchData.setPekara(pekaraSend);
			}
		});
	}

	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		switch (arg1) {
		case R.id.rbMN:
			pekaraName = "Pan-Pek";
			pekaraSend = "Pan-Pek";
			break;
		case R.id.rbT:
			pekaraName = "Dubravica";
			pekaraSend = "Dubravica";
			break;
		case R.id.rbPB:
			pekaraName = "Klara";
			pekaraSend = "Klara";
			break;
		case R.id.rbAll:
			pekaraName = "Sve pekare";
			pekaraSend = "n";

		}
	}
}
