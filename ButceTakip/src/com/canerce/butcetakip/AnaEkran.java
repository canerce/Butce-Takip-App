package com.canerce.butcetakip;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AnaEkran extends Activity {

	TextView txtDolar;
	TextView txtEuro;
	TextView txtGelirTL;
	TextView txtGiderTL;
	TextView txtNakitTL;

	String forexUSD = "";
	String forexEUR = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ana_ekran);

		// set Date
		TextView txtTarih = (TextView) findViewById(R.id.txtTarih);
		txtTarih.setText(new SimpleDateFormat("dd MMMM yyyy", Locale
				.getDefault()).format(new Date()));
		txtGelirTL = (TextView) findViewById(R.id.txtGelirTL);
		txtGiderTL = (TextView) findViewById(R.id.txtGiderTL);
		txtNakitTL = (TextView) findViewById(R.id.txtNakitTL);
		guncelleMiktar();
		XMLTask task = new XMLTask();
		task.execute("");
	}

	// Güncelle butonu
	public void guncelleDoviz(View v) {
		guncelleMiktar();
		// Döviz getir
		XMLTask task = new XMLTask();
		task.execute("");
	}

	// Ekle Butonu
	public void openEkle(View v) {
		startActivityForResult(new Intent(this, GelirGiderEkle.class), 1);
	}

	// Listele Butonu
	public void openListe(View v) {
		startActivity(new Intent(this, ButceListe.class));
	}

	// Ayarlar Butonu
	public void openAyarlar(View v) {
		startActivityForResult(new Intent(this, Ayarlar.class), 2);
	}

	class XMLTask extends AsyncTask<String, Integer, String> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(AnaEkran.this);
			pd.setTitle(getText(R.string.updatetitle));
			pd.setMessage(getText(R.string.updatemesage));
			pd.show();

			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			try {

				URL url = new URL("http://www.tcmb.gov.tr/kurlar/today.xml");
				XmlPullParserFactory factory = XmlPullParserFactory
						.newInstance();
				XmlPullParser parser = factory.newPullParser();
				parser.setInput(url.openConnection().getInputStream(), "UTF-8");
				int eventType = parser.getEventType();
				String code = "";
				while (eventType != XmlPullParser.END_DOCUMENT) {
					if (eventType == XmlPullParser.START_TAG) {
						if (parser.getName().equals("Currency")) {
							code = parser.getAttributeValue(null,
									"CurrencyCode");
						} else if (parser.getName().equals("ForexBuying")
								&& parser.next() == XmlPullParser.TEXT) {
							if (code.equals("USD"))
								forexUSD = parser.getText();
							else if (code.equals("EUR"))
								forexEUR = parser.getText();
						}
					}
					eventType = parser.next();
				}
				eventType = 0;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (pd.isShowing())
				pd.dismiss();

			txtDolar = (TextView) findViewById(R.id.txtDolar);
			txtEuro = (TextView) findViewById(R.id.txtEuro);
			txtDolar.setText(forexUSD);
			txtEuro.setText(forexEUR);

			guncelleTablo();
			super.onPostExecute(result);
		}

	}

	// Dövize göre gelir-gider
	public void guncelleTablo() {
		TextView txtGelirUSD = (TextView) findViewById(R.id.txtGelirUSD);
		TextView txtGelirEURO = (TextView) findViewById(R.id.txtGelirEURO);

		TextView txtGiderUSD = (TextView) findViewById(R.id.txtGiderUSD);
		TextView txtGiderEURO = (TextView) findViewById(R.id.txtGiderEURO);

		TextView txtNakitUSD = (TextView) findViewById(R.id.txtNakitUSD);
		TextView txtNakitEURO = (TextView) findViewById(R.id.txtNakitEURO);

		Float temp;
		temp = Float.parseFloat(txtGelirTL.getText().toString())
				/ Float.parseFloat(txtDolar.getText().toString());
		txtGelirUSD.setText(new DecimalFormat("##.##").format(temp));
		temp = Float.parseFloat(txtGelirTL.getText().toString())
				/ Float.parseFloat(txtEuro.getText().toString());
		txtGelirEURO.setText(new DecimalFormat("##.##").format(temp));
		temp = Float.parseFloat(txtGiderTL.getText().toString())
				/ Float.parseFloat(txtDolar.getText().toString());
		txtGiderUSD.setText(new DecimalFormat("##.##").format(temp));
		temp = Float.parseFloat(txtGiderTL.getText().toString())
				/ Float.parseFloat(txtEuro.getText().toString());
		txtGiderEURO.setText(new DecimalFormat("##.##").format(temp));
		temp = Float.parseFloat(txtNakitTL.getText().toString())
				/ Float.parseFloat(txtDolar.getText().toString());
		txtNakitUSD.setText(new DecimalFormat("##.##").format(temp));
		temp = Float.parseFloat(txtNakitTL.getText().toString())
				/ Float.parseFloat(txtEuro.getText().toString());
		txtNakitEURO.setText(new DecimalFormat("##.##").format(temp));
	}

	// Toplam gelir-gider
	public void guncelleMiktar() {

		try {
			ButceSQLiteHelper helper = new ButceSQLiteHelper(this);
			String[] miktarlar = helper.getMiktarlar();
			if (miktarlar[0] != null)
				txtGelirTL.setText(miktarlar[0]);
			if (miktarlar[1] != null)
				txtGiderTL.setText(miktarlar[1]);

			int gel = 0, git = 0;
			gel = Integer.parseInt(txtGelirTL.getText().toString());
			git = Integer.parseInt(txtGiderTL.getText().toString());
			int temp = gel - git;
			txtNakitTL.setText(new DecimalFormat("##.##").format(temp));

		} catch (Exception e) {
			// TODO: handle exception
			Log.e("Anasayfa - Guncelleme", e.toString());
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				Toast.makeText(this, getString(R.string.record_ok),
						Toast.LENGTH_LONG).show();
				guncelleMiktar();
				guncelleTablo();
			} else {
				Toast.makeText(this, getString(R.string.user_cancelled),
						Toast.LENGTH_LONG).show();
			}
		}
		if (requestCode == 2) {
			dilguncel();

	        Intent refresh = new Intent(this, AnaEkran.class);
	        startActivity(refresh);
			
		}

	}
	public void dilguncel() {
		Configuration config = new Configuration();
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		if (sharedPrefs.getString("pref_dil", "TR") == "TR") {
			Locale locale = new Locale("tr");
			Locale.setDefault(locale);

			config.locale = locale;
		} else if (sharedPrefs.getString("pref_dil", "TR") == "EN") {
			Locale locale = new Locale("en");
			Locale.setDefault(locale);

			config.locale = locale;
		}
        Resources res = getBaseContext().getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        res.updateConfiguration(config, dm);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ana_ekran, menu);
		return true;
	}

}
