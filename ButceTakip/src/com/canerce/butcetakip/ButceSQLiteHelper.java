package com.canerce.butcetakip;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ButceSQLiteHelper extends SQLiteOpenHelper {
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "caner.db";
	public static final String TBL_BUTCE = "butce";

	private static final String SQL_CREATE_TABLES = "create table butce(" 
			+ "id integer primary key autoincrement," 
			+ "miktar real  null,"
			+ "tur integer  null,"
			+ "aciklama text  null," 
			+ "tarih DATETIME DEFAULT CURRENT_TIMESTAMP);";
	
	
	public ButceSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_TABLES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// db.execSQL("DROP TABLE IF EXISTS " + TBL_BUTCE);
		onCreate(db);
	}
	public long addHarcama(float miktar, int tur, String aciklama) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("miktar", miktar);
		values.put("tur", tur);
		values.put("aciklama", aciklama);
		long id= 0;
		id = db.insert(TBL_BUTCE, null, values);
		db.close();
		return id;
	}
	
	public int updateHarcama(int id, float miktar, int tur, String aciklama) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("miktar", miktar);
		values.put("tur", tur);
		values.put("aciklama", aciklama);
		int updateCount = db.update(TBL_BUTCE, values, "id= ?", new String[] { String.valueOf(id) });
		db.close();
		return updateCount;
	}
	
	public int deleteHarcama(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		int deleteCount = db.delete(TBL_BUTCE, "id= ?", new String[] { String.valueOf(id) });
		db.close();
		return deleteCount;
	}
	public String[] getMiktarlar() {
		String[] miktarlar = new String[2];
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("select sum(miktar) from butce Where tur=1", null); // gelir
		if (cursor.moveToFirst()) {
			do {
				miktarlar[0] = cursor.getString(0);
			} while (cursor.moveToNext());
		}
		 cursor = db.rawQuery("select sum(miktar) from butce Where tur=0", null); // gider
		if (cursor.moveToFirst()) {
			do {
				miktarlar[1] = cursor.getString(0);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return miktarlar;
	}


	public ArrayList<ButceClass> getAllListe() {
		
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<ButceClass> butceList = new ArrayList<ButceClass>();
		Cursor cursor = db.rawQuery("Select * from butce", null);

		if (cursor.moveToFirst()) {
			do {
				ButceClass islem = new ButceClass();
				islem.setId(cursor.getInt(0));
				islem.setMiktar(cursor.getFloat(1));
				islem.setTur(cursor.getInt(2));
				islem.setAciklama(cursor.getString(3));
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
				Date ds = null;
				try {
					ds = sdf.parse(cursor.getString(4));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				islem.setTarih(ds);

				butceList.add(islem);
			} while (cursor.moveToNext());
		}
		cursor.close();

		db.close();
		return butceList;
	}
	
	public ButceClass getIslem(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		ButceClass islem = new ButceClass();

		Cursor cursor = db.rawQuery("Select * from butce where id=?", new String[] { Integer.toString(id) });

		if (cursor.moveToFirst()) {
			do {
				islem.setId(cursor.getInt(0));
				islem.setMiktar(cursor.getFloat(1));
				islem.setTur(cursor.getInt(2));
				islem.setAciklama(cursor.getString(3));

			} while (cursor.moveToNext());
		}
		cursor.close();

		db.close();
		return islem;
	}
	
}