package com.nrk.mobiso;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class SearchActivity extends Activity {
	private enum ResultStatus {
		PROGRESS,
		SUCCESS,
		ERROR,
		EMPTY
	};
	private LinearLayout searchLayout;
	private SearchResultAdapter adapter;
	private ListView searchResultList;
	private ProgressBar progressBar;
	private TextView statusText;
	private String currentSearchTerm;
	private String URL = "http://api.stackexchange.com/2.2/search/advanced?order=desc&sort=relevance&site=stackoverflow";
	private int RESULT_LIMIT = 30;
	private String result = "";
	private ArrayList<Question> parsedResult = new ArrayList<Question>();
	private final static String TAG = "mobiso-zzz";
	private QuestionDAO questionDAO;
	private SQLiteDatabase qDB;

	private Question getQuestion(JSONObject o) throws JSONException{
		return new Question(
			o.getLong("question_id"),
			o.getString("title"),
			o.getInt("score"),
			o.getJSONObject("owner").getString("display_name"),
			o.getString("body")
		);
	}
	
	private void updateView(ResultStatus status){
		switch(status){
			case PROGRESS:
				progressBar.setVisibility(View.VISIBLE);
				searchResultList.setVisibility(View.GONE);
				statusText.setText("Loading");
				statusText.setVisibility(View.GONE);
				break;
			case EMPTY:
				progressBar.setVisibility(View.GONE);
				searchResultList.setVisibility(View.GONE);
				statusText.setText("No results for " + currentSearchTerm);
				statusText.setVisibility(View.VISIBLE);
				break;
			case ERROR:
				progressBar.setVisibility(View.GONE);
				searchResultList.setVisibility(View.GONE);
				statusText.setText("Some error while loading results");
				statusText.setVisibility(View.VISIBLE);
				break;
			case SUCCESS:
				progressBar.setVisibility(View.GONE);
				searchResultList.setVisibility(View.VISIBLE);
				statusText.setText("Done !");
				statusText.setVisibility(View.GONE);
				break;
			default:
				break;
		}
	}
	private void makeRequest(){
		RequestQueue queue = Volley.newRequestQueue(this);

		URL = "http://api.stackexchange.com/2.2/search/advanced?" +
				"order=desc&sort=relevance&site=stackoverflow" +
				"&q=" + currentSearchTerm + "&pagesize=" + RESULT_LIMIT +
				"&filter=withbody";
		
		JsonObjectRequest searchRequest = new JsonObjectRequest(
				Request.Method.GET,
				URL,
				null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response){
						try {
							JSONObject o;
							JSONArray items = response.getJSONArray("items");
							parsedResult.clear();
							if(items != null && items.length() > 0){
								questionDAO.openDb();
							}
							for(int i = 0; i < items.length(); i ++){
								o = items.getJSONObject(i);
								Question q = getQuestion(o);
								parsedResult.add(q);
								questionDAO.saveQuestion(q, currentSearchTerm);
							}
							if(parsedResult.isEmpty()){
								updateView(ResultStatus.EMPTY);
							} else {
								questionDAO.closeDb();
								updateView(ResultStatus.SUCCESS);
							}
						} catch (JSONException e) {
							e.printStackTrace();
							parsedResult.clear();
							updateView(ResultStatus.ERROR);
						}
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						updateView(ResultStatus.ERROR);
					}
				}
		);
		queue.add(searchRequest);
		updateView(ResultStatus.PROGRESS);
	}

	private void createLoading(){
		progressBar = new ProgressBar(this);
		progressBar.setLayoutParams(new LayoutParams(
			LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT
		));
		progressBar.setIndeterminate(true);
		searchLayout.addView(progressBar);
	}
	
	private void createEmpty(){
		statusText = new TextView(this);
		statusText.setLayoutParams(new LayoutParams(
			LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
		));
		statusText.setGravity(Gravity.CENTER);
		statusText.setText("");
		searchLayout.addView(statusText);
	}
	
	private void createListView() {
		if(searchResultList == null) {
			searchResultList = new ListView(this);
			adapter = 
				new SearchResultAdapter(this, R.layout.search_result_row, parsedResult);
			searchResultList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Question q;
					Intent i;
					SearchResultAdapter adapter = (SearchResultAdapter)parent.getAdapter(); 
					if(adapter != null && !adapter.isEmpty()){
						q = adapter.getItem(position);
						i = new Intent(SearchActivity.this, AnswerActivity.class);
						i.putExtra("CURRENT_QUESTION", q);
						startActivity(i);
					}
				}
			});
			searchResultList.setAdapter(adapter);
		}
	}
	private void createSearchLayout(){
		if(searchLayout == null){
			searchLayout = new LinearLayout(this);
			createLoading();
			createEmpty();
			createListView();
			searchLayout.addView(searchResultList);
		}
	}
	
	private void lookupOffline(){
		questionDAO.openDb();
		updateView(ResultStatus.PROGRESS);
		parsedResult = null;
		parsedResult = questionDAO.lookup(currentSearchTerm);
		adapter.clear();
		adapter.addAll(parsedResult);
		//parsedResult.clear();
		if(parsedResult.isEmpty()){
			updateView(ResultStatus.EMPTY);
		} else {
			updateView(ResultStatus.SUCCESS);
		}
		questionDAO.closeDb();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		questionDAO = new QuestionDAO(this);
		
		currentSearchTerm = getIntent().getStringExtra("SEARCH_TEXT");
		createSearchLayout();
		setContentView(searchLayout);
		if(NetworkStatus.isConnected(this)){
			makeRequest();
		} else {
			lookupOffline();
		}
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		questionDAO.closeDb();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		questionDAO.closeDb();
	}
}
