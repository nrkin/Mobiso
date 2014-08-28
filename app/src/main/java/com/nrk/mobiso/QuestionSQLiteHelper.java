package com.nrk.mobiso;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class QuestionSQLiteHelper extends SQLiteOpenHelper{
	public static final String DATABASE_NAME = "mobiso_questions.db";
	public static final String TABLE_NAME = "questions";
	public static final String COLUMN_NAME_ID = "qId";
	public static final String COLUMN_NAME_OWNERNAME = "ownerName";
	public static final String COLUMN_NAME_TITLE = "title";
	public static final String COLUMN_NAME_CONTENTS = "contents";
	public static final String COLUMN_NAME_SCORE = "score";
	public static final String COLUMN_NAME_SEARCH_QUERY = "search_query";
	
	private String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
			"(" + COLUMN_NAME_ID + " INTEGER NOT NULL" +
			", " + COLUMN_NAME_OWNERNAME + " TEXT NOT NULL" +
			", " + COLUMN_NAME_TITLE + " TEXT NOT NULL" +
			", " + COLUMN_NAME_CONTENTS + " TEXT NOT NULL" +
			", " + COLUMN_NAME_SCORE + " INTEGER" +
			", " + COLUMN_NAME_SEARCH_QUERY + " TEXT NOT NULL" +
			", " + "PRIMARY KEY (" + COLUMN_NAME_ID + "," + COLUMN_NAME_SEARCH_QUERY + ")" +
			");";

	public QuestionSQLiteHelper(Context context) {
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
