package com.nrk.mobiso;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.text.Html;
import android.util.Log;

public class AnswersAdapter extends ArrayAdapter<Answer>{
	final static private String TAG = "SearchResultAdapter";
	Context context;
	int layoutResourceId;
	ArrayList<Answer> data = null;

	public AnswersAdapter(Context context, int resource, ArrayList<Answer> objects) {
		super(context, resource, objects);
		this.context = context;
		this.layoutResourceId = resource;
		this.data = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			Log.i(TAG, "layoutResourceId " + (layoutResourceId == R.layout.search_result_row));
			convertView = inflater.inflate(layoutResourceId, parent, false); 
		}
		Answer ans = data.get(position);
		View textView = convertView.findViewById(R.id.answer_body);
		((TextView)textView).setText(Html.fromHtml(ans.contents));
		textView = convertView.findViewById(R.id.answer_score);
		((TextView)textView).setText("Score : " + ans.score);
		return convertView;
	}
}
