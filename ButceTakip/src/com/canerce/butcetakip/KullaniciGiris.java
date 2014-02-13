package com.canerce.butcetakip;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class KullaniciGiris extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kullanici_giris);
	}
	public void Login(View v) {
		
		
		EditText etUser = (EditText) findViewById(R.id.etUser);
		EditText etPass = (EditText) findViewById(R.id.etPass);

		if (etUser.getText().toString().equals("1") && etPass.getText().toString().equals("1")) {
			startActivity(new Intent(this, AnaEkran.class));
		} else
			Toast.makeText(this, getText(R.string.logout), Toast.LENGTH_SHORT).show();
		

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.kullanici_giris, menu);
		return true;
	}

}
