package com.canerce.butcetakip;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TableRow;

public class GelirGiderEkle extends Activity {
	public TableRow rowAciklama;
	TableRow rowKaynakGelir;
	TableRow rowKaynakGider;
	Spinner cmbGelir;
	Spinner cmbGider;
	RadioButton rdGelir;
	RadioButton rdGider;
	ArrayAdapter<CharSequence> adapterGelir;
	ArrayAdapter<CharSequence> adapterGider;
	EditText txtTutar;
	EditText txtAciklama;

	int ID = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gelir_gider_ekle);
		
		Intent intend = getIntent();
		ID = intend.getIntExtra("ID", 0);
		/************************************************************/
		rowAciklama = (TableRow) findViewById(R.id.rowAciklama);
		rowKaynakGelir = (TableRow) findViewById(R.id.rowKaynakGelir);
		rowKaynakGider = (TableRow) findViewById(R.id.rowKaynakGider);
		rdGelir = (RadioButton) findViewById(R.id.rdGelir);
		rdGider = (RadioButton) findViewById(R.id.rdGider);
		cmbGelir = (Spinner) findViewById(R.id.cmbGelir);
		cmbGider = (Spinner) findViewById(R.id.cmbGider);
		txtTutar = (EditText) findViewById(R.id.txtTutar);
		txtAciklama = (EditText) findViewById(R.id.txtAciklama);
		/************************************************************/
		adapterGelir = ArrayAdapter.createFromResource(this, R.array.gelirler,android.R.layout.simple_spinner_item);
		adapterGelir.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		cmbGelir.setAdapter(adapterGelir);
		cmbGelir.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				if (pos == 5)
					rowAciklama.setVisibility(View.VISIBLE);
				else
					rowAciklama.setVisibility(View.GONE);
			}
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		/************************************************************/
		adapterGider = ArrayAdapter.createFromResource(this, R.array.giderler,android.R.layout.simple_spinner_item);
		adapterGider.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		cmbGider.setAdapter(adapterGider);
		cmbGider.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				if (pos == 5)
					rowAciklama.setVisibility(View.VISIBLE);
				else
					rowAciklama.setVisibility(View.GONE);
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		/************************************************************/
		if (ID > 0) {
			getIslem();
			Button btnSil = (Button) findViewById(R.id.btnSil);
			btnSil.setVisibility(View.VISIBLE);
		}
	}

	public void onClickRadioGelGit(View view) {
		boolean checked = ((RadioButton) view).isChecked();

		switch (view.getId()) {
		case R.id.rdGelir:
			if (checked) {
				rowKaynakGider.setVisibility(View.GONE);
				rowAciklama.setVisibility(View.GONE);
				rowKaynakGelir.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.rdGider:
			if (checked) {
				rowKaynakGider.setVisibility(View.VISIBLE);
				rowAciklama.setVisibility(View.GONE);
				rowKaynakGelir.setVisibility(View.GONE);
			}
			break;
		}
	}

	public void Geri(View v) {
		setResult(RESULT_CANCELED, new Intent());
		finish();
	}

	public void KayitKaydet(View v) {

		String Aciklama;
		int Tur;
		if (rdGelir.isChecked()) {
			Tur = 1;
			if (cmbGelir.getSelectedItemPosition() < 5)
				Aciklama = cmbGelir.getSelectedItem().toString();
			else
				Aciklama = txtAciklama.getText().toString().trim();
		} else {
			Tur = 0;
			if (cmbGider.getSelectedItemPosition() < 5)
				Aciklama = cmbGider.getSelectedItem().toString();
			else
				Aciklama = txtAciklama.getText().toString().trim();
		}
		ButceSQLiteHelper helper = new ButceSQLiteHelper(this);

		if (ID > 0) {
			helper.updateHarcama(ID,
					Float.parseFloat(txtTutar.getText().toString().trim()),
					Tur, Aciklama);
		} else {
			ID = (int) helper.addHarcama(
					Float.parseFloat(txtTutar.getText().toString().trim()),
					Tur, Aciklama);
		}

		Intent intend = new Intent();
		intend.putExtra("ID", ID);
		setResult(RESULT_OK, intend);

		finish();
	}

	public void KayitSil(View v) {
		ButceSQLiteHelper helper = new ButceSQLiteHelper(this);
		helper.deleteHarcama(ID);
		Intent intend = new Intent();
		intend.putExtra("ID", ID);
		setResult(RESULT_OK, intend);
		finish();
	}
	public void getIslem() {
		ButceSQLiteHelper helper = new ButceSQLiteHelper(this);
		ButceClass islem = helper.getIslem(ID);

		txtTutar.setText(islem.getMiktar().toString());

		if (islem.getTur() == 1) { // gelir
			rdGelir.setChecked(true);
			rowKaynakGider.setVisibility(View.GONE);
			rowKaynakGelir.setVisibility(View.VISIBLE);
			if (adapterGelir.getPosition(islem.getAciklama()) > -1) {
				cmbGelir.setSelection(adapterGelir.getPosition(islem.getAciklama()));
				rowAciklama.setVisibility(View.GONE);
			} else {
				cmbGelir.setSelection(5);
				txtAciklama.setText(islem.getAciklama());
				rowAciklama.setVisibility(View.VISIBLE);
			}
		} else {
			rdGider.setChecked(true);
			rowKaynakGider.setVisibility(View.VISIBLE);
			rowKaynakGelir.setVisibility(View.GONE);
			if (adapterGider.getPosition(islem.getAciklama()) > -1) {
				cmbGider.setSelection(adapterGider.getPosition(islem.getAciklama()));
				rowAciklama.setVisibility(View.GONE);
			} else {
				cmbGider.setSelection(5);
				txtAciklama.setText(islem.getAciklama());
				rowAciklama.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gelir_gider_ekle, menu);
		return true;
	}
}
