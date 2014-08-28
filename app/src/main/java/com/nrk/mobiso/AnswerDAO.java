package com.nrk.mobiso;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AnswerDAO {
	private SQLiteDatabase db;
	private AnswerSQLiteHelper answerHelper;
	private String[] columns = {
		AnswerSQLiteHelper.COLUMN_NAME_ID,
		AnswerSQLiteHelper.COLUMN_NAME_OWNERNAME,
		AnswerSQLiteHelper.COLUMN_NAME_CONTENTS,
		AnswerSQLiteHelper.COLUMN_NAME_SCORE
	};
	public AnswerDAO(Context context){
		answerHelper = new AnswerSQLiteHelper(context);
	}
	
	public void openDb(){
		db = answerHelper.getWritableDatabase();
	}
	
	public void closeDb(){
		if(db != null) {
			db.close();
		}
	}
	
	public boolean recordExists(Answer a) {
		String query = "SELECT " + AnswerSQLiteHelper.COLUMN_NAME_ID
			+ " FROM " + AnswerSQLiteHelper.TABLE_NAME
			+ " WHERE " + AnswerSQLiteHelper.COLUMN_NAME_ID + " =  " + a.id 
			+ " ;";
		Cursor c = db.rawQuery(query, null);
		return c != null && c.getCount() == 1;
	}
	
	public void saveAnswer(Answer a, long qId){
		if(recordExists(a)) {
			return;
		}
		ContentValues values = new ContentValues();
		values.put(AnswerSQLiteHelper.COLUMN_NAME_ID, a.id);
		values.put(AnswerSQLiteHelper.COLUMN_NAME_OWNERNAME, a.ownerName);
		values.put(AnswerSQLiteHelper.COLUMN_NAME_CONTENTS, a.contents);
		values.put(AnswerSQLiteHelper.COLUMN_NAME_SCORE, a.score);
		values.put(AnswerSQLiteHelper.COLUMN_NAME_QID, qId);
		db.insert(AnswerSQLiteHelper.TABLE_NAME, null, values);
	}
	
	public ArrayList<Answer> lookup(long qId){
		ArrayList<Answer> answers = new ArrayList<Answer>();
		String WHERE = AnswerSQLiteHelper.COLUMN_NAME_QID + "=" + qId;
		Cursor c = db.query(
			AnswerSQLiteHelper.TABLE_NAME,
			columns, WHERE, null, null, null, null);
		c.moveToFirst();
		while(!c.isAfterLast()){
			Answer a = new Answer(
				c.getLong(c.getColumnIndex(AnswerSQLiteHelper.COLUMN_NAME_ID)),
				c.getString(c.getColumnIndex(AnswerSQLiteHelper.COLUMN_NAME_CONTENTS)),
				c.getInt(c.getColumnIndex(AnswerSQLiteHelper.COLUMN_NAME_SCORE)),
				c.getString(c.getColumnIndex(AnswerSQLiteHelper.COLUMN_NAME_OWNERNAME))
			);
			answers.add(a);
			c.moveToNext();
		}
		return answers;
	}
}
