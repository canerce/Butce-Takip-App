package com.canerce.butcetakip;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ButceListe extends Activity {
	private ListView lvButce;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.butce_liste);
		
		lvButce = (ListView) findViewById(R.id.lvButce);
		registerForContextMenu(lvButce);
		lvButce.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adp, View v, int position, long id) {
				ButceClass item = (ButceClass) lvButce.getItemAtPosition(position);
				Intent intent = new Intent(ButceListe.this, GelirGiderEkle.class);
				intent.putExtra("ID", item.getId());

				startActivityForResult(intent, 1);
			}
		});

		LoadButceList();
	}
	
	public void LoadButceList() {
		ButceSQLiteHelper mHelper = new ButceSQLiteHelper(getApplicationContext());
		ArrayList<ButceClass> butceList = mHelper.getAllListe();
		ButceAdapter adapter = new ButceAdapter(this, butceList);
		lvButce.setAdapter(adapter);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {

		super.onCreateContextMenu(menu, v, menuInfo);

		
		if (v.getId() == R.id.lvButce) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			ButceClass item = (ButceClass) lvButce.getItemAtPosition(info.position);
			menu.setHeaderTitle(item.getAciklama());
			menu.add(Menu.NONE, 1, 1, getString(R.string.duzenle));
			menu.add(Menu.NONE, 2, 2, getString(R.string.sil));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.butce_liste, menu);
		return true;
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {

		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		
		ButceClass islem = (ButceClass) lvButce.getItemAtPosition(info.position);
		
		if (item.getItemId() == 1) {
			
			Intent intend = new Intent(this, GelirGiderEkle.class);
			intend.putExtra("ID", islem.getId());
			startActivityForResult(intend , 1);
		}
		else {
			ButceSQLiteHelper helper = new ButceSQLiteHelper(this);
			helper.deleteHarcama(islem.getId());
		}
		
		LoadButceList();
		
		return super.onContextItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				Toast.makeText(this, getString(R.string.record_ok), Toast.LENGTH_LONG).show();
				LoadButceList();
			} else {
				Toast.makeText(this, getString(R.string.user_cancelled), Toast.LENGTH_LONG).show();
			}

		}

	}

}
