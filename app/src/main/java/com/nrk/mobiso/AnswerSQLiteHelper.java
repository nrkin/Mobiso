package com.nrk.mobiso;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AnswerSQLiteHelper  extends SQLiteOpenHelper{
	public static final String DATABASE_NAME = "mobiso_answers.db";
	public static final String TABLE_NAME = "answers";
	public static final String COLUMN_NAME_ID = "id";
	public static final String COLUMN_NAME_OWNERNAME = "ownerName";
	public static final String COLUMN_NAME_CONTENTS = "contents";
	public static final String COLUMN_NAME_SCORE = "score";
	public static final String COLUMN_NAME_QID = "qId";
	
	private String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
			"(" + COLUMN_NAME_ID + " INTEGER NOT NULL PRIMARY KEY " +
			", " + COLUMN_NAME_OWNERNAME + " TEXT NOT NULL" +
			", " + COLUMN_NAME_CONTENTS + " TEXT NOT NULL" +
			", " + COLUMN_NAME_SCORE + " INTEGER" +
			", " + COLUMN_NAME_QID + " INTEGER NOT NULL" +
			");";

	public AnswerSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, 2);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
}
