package com.nrk.mobiso;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class QuestionDAO {
	private SQLiteDatabase db;
	private QuestionSQLiteHelper questionHelper;
	private String[] columns = {
		QuestionSQLiteHelper.COLUMN_NAME_ID,
		QuestionSQLiteHelper.COLUMN_NAME_OWNERNAME,
		QuestionSQLiteHelper.COLUMN_NAME_CONTENTS,
		QuestionSQLiteHelper.COLUMN_NAME_TITLE,
		QuestionSQLiteHelper.COLUMN_NAME_SCORE,
		QuestionSQLiteHelper.COLUMN_NAME_SEARCH_QUERY
	};
	public QuestionDAO(Context context){
		questionHelper = new QuestionSQLiteHelper(context);
	}
	
	public void openDb(){
		db = questionHelper.getWritableDatabase();
	}
	
	public void closeDb(){
		if(db != null) {
			db.close();
		}
	}
	
	public boolean recordExists(Question q, String searchQuery) {
		String query = "SELECT " + QuestionSQLiteHelper.COLUMN_NAME_ID
			+ " FROM " + QuestionSQLiteHelper.TABLE_NAME
			+ " WHERE " + QuestionSQLiteHelper.COLUMN_NAME_ID + " =  " + q.qId 
			+ " AND " + QuestionSQLiteHelper.COLUMN_NAME_SEARCH_QUERY + " = ? ;";
		String[] args = {searchQuery};
		Cursor c = db.rawQuery(query, args);
		return c != null && c.getCount() == 1;
	}
	
	public void saveQuestion(Question q, String searchQuery){
		if(recordExists(q, searchQuery)) {
			return;
		}
		ContentValues values = new ContentValues();
		values.put(QuestionSQLiteHelper.COLUMN_NAME_ID, q.qId);
		values.put(QuestionSQLiteHelper.COLUMN_NAME_OWNERNAME, q.ownerName);
		values.put(QuestionSQLiteHelper.COLUMN_NAME_CONTENTS, q.contents);
		values.put(QuestionSQLiteHelper.COLUMN_NAME_TITLE, q.title);
		values.put(QuestionSQLiteHelper.COLUMN_NAME_SCORE, q.score);
		values.put(QuestionSQLiteHelper.COLUMN_NAME_SEARCH_QUERY, searchQuery);
		db.insert(QuestionSQLiteHelper.TABLE_NAME, null, values);
	}
	
	public ArrayList<Question> lookup(String searchQuery){
		ArrayList<Question> questions = new ArrayList<Question>();
		String WHERE = QuestionSQLiteHelper.COLUMN_NAME_SEARCH_QUERY + "=?";
		String[] WHERE_ARGS = {searchQuery};
		Cursor c = db.query(
			QuestionSQLiteHelper.TABLE_NAME,
			columns, WHERE, WHERE_ARGS, null, null, null);
		c.moveToFirst();
		while(!c.isAfterLast()){
			Question q = new Question(
				c.getLong(c.getColumnIndex(QuestionSQLiteHelper.COLUMN_NAME_ID)),
				c.getString(c.getColumnIndex(QuestionSQLiteHelper.COLUMN_NAME_TITLE)),
				c.getInt(c.getColumnIndex(QuestionSQLiteHelper.COLUMN_NAME_SCORE)),
				c.getString(c.getColumnIndex(QuestionSQLiteHelper.COLUMN_NAME_OWNERNAME)),
				c.getString(c.getColumnIndex(QuestionSQLiteHelper.COLUMN_NAME_CONTENTS))
			);
			questions.add(q);
			c.moveToNext();
		}
		return questions;
	}
}
