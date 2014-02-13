package com.canerce.butcetakip;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableLayout;
import android.widget.TextView;

public class ButceAdapter extends ArrayAdapter<ButceClass> {

	private ArrayList<ButceClass> items;
	private Context c;

	public ButceAdapter(Context context,   ArrayList<ButceClass> items) {
		super(context, R.layout.butce_liste_satir, items);
		this.items = items;
		this.c = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) 
					c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.butce_liste_satir, null);
		}
		ButceClass o = items.get(position);
		if (o != null) {
			TextView tbAciklama = (TextView) v.findViewById(R.id.tbAciklama);
			TextView tbTutar = (TextView) v.findViewById(R.id.tbTutar);
			TextView tbTarih = (TextView) v.findViewById(R.id.tbTarih);
			tbTutar.setText(Float.toString( o.getMiktar()));
			tbAciklama.setText(o.getAciklama());
			tbTarih.setText(new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(o.getTarih()));
			
			if (o.getTur()==0) {
				TableLayout butce_satir = (TableLayout) v.findViewById(R.id.butce_satir);
				butce_satir.setBackgroundColor(Color.RED);
			}
			
		}
		return v;
	}

}
