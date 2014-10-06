package com.young.account;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final int VERSION = 1;
	private static final String TAG = "DatabaseHelper";
	
	// for creating or opening database
	public DatabaseHelper(Context context, String name) {
		this(context, name, null, VERSION);
	}
	
	// for upgrading database
	public DatabaseHelper(Context context, String name, int version) {
		this(context, name, null, version);
	}
	
	public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		Log.i(TAG, "constructor");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(TAG, "onCreate");
		db.execSQL("create table accounts(id integer primary key autoincrement, " +
										 "price float, " + 
										 "date varchar(10), " +
										 "time varchar(10), " +
										 "type varchar(10), " +
										 "method varchar(20), " +
										 "remark varchar(50))");
	}

	@Override 
	public void onOpen(SQLiteDatabase db) {
		Log.i(TAG, "onOpen");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(TAG, "onUpgrade");
		
	}

}
