package com.bravo.bravoclient.persistence;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper{
	private Logger logger = Logger.getLogger(SQLiteHelper.class.getName());
	
	private static final String DATABASE_NAME = "BRAVO_DB";
	private static final int DATABASE_VERSION = 1;
	public static final String TABLE_NAME = "CARD";
	public static final String COLUMN_ID = "COLUMN_ID";
	public static final String COLUMN_CARD_ID = "CARD_ID";
	public static final String COLUMN_LOYALTY_POINT = "LOYALTY_POINT";
	public static final String COLUMN_MERCHANT_ACCOUNT_NUMBER = "MERCHANT_ACC_NO";
	public static final String COLUMN_BALANCE = "BALANCE";
	
	private static final String CREATE_DB = "CREATE TABLE {0} ({1} INTEGER NOT NULL, {2} TEXT PRIMARY KEY, " +
			"{3} REAL NOT NULL, {4} TEXT NOT NULL, {5} REAL NOT NULL, UNIQUE ({1}, {2}));";
	
	public SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(MessageFormat.format(CREATE_DB, 
				new Object[] {TABLE_NAME, COLUMN_ID, COLUMN_CARD_ID, COLUMN_LOYALTY_POINT, COLUMN_MERCHANT_ACCOUNT_NUMBER, COLUMN_BALANCE}));
		logger.log(Level.INFO, "DB is created");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		logger.log(Level.INFO, "DB is updated from " + oldVersion + " to " + newVersion);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		
	}
}
