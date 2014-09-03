package com.nrk.mobiso;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
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

public class AnswerActivity extends Activity {
	private enum ResultStatus {
		PROGRESS,
		SUCCESS,
		ERROR,
		EMPTY
	};

	private AnswersAdapter adapter;
	private Question currentQuestion;
	private String URL = "http://api.stackexchange.com/2.2/questions/";
	private String URLOptions = "/answers?order=desc&site=stackoverflow&filter=withbody";
	private ArrayList<Answer> parsedResult = new ArrayList<Answer>();
	private final static String TAG = "mobiso-anwers-zzz";
	private AnswerDAO answerDAO;
	private SQLiteDatabase qDB;

	private Answer getAnswer(JSONObject o) throws JSONException{
		return new Answer(
			//todo
			o.getLong("answer_id"),
			o.getString("body"),
			o.getInt("score"),
			o.getJSONObject("owner").getString("display_name")
		);
	}
	
	private void updateView(ResultStatus status){
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.answerActivityProgress);
        TextView statusText = (TextView)findViewById(R.id.answerStatusText);
        ListView answersList = (ListView)findViewById(R.id.answersList);
		switch(status){
			case PROGRESS:
				progressBar.setVisibility(View.VISIBLE);
				answersList.setVisibility(View.GONE);
				statusText.setText("Loading");
				statusText.setVisibility(View.GONE);
				break;
			case EMPTY:
				progressBar.setVisibility(View.GONE);
				answersList.setVisibility(View.GONE);
				statusText.setText("Answer for this question is not stored");
				statusText.setVisibility(View.VISIBLE);
				break;
			case ERROR:
				progressBar.setVisibility(View.GONE);
				answersList.setVisibility(View.GONE);
				statusText.setText("Some error while showing results");
				statusText.setVisibility(View.VISIBLE);
				break;
			case SUCCESS:
				progressBar.setVisibility(View.GONE);
				answersList.setVisibility(View.VISIBLE);
				statusText.setText("Done !");
				statusText.setVisibility(View.GONE);
				break;
			default:
				break;
		}
	}
	private void makeRequest(){
		RequestQueue queue = Volley.newRequestQueue(this);

		String requestURL = URL + currentQuestion.qId + URLOptions;
		
		JsonObjectRequest searchRequest = new JsonObjectRequest(
				Request.Method.GET,
				requestURL,
				null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response){
						try {
							JSONObject o;
							JSONArray items = response.getJSONArray("items");
							if(items != null && items.length() > 0) {
								answerDAO.openDb();
							}
							parsedResult.clear();
							for(int i = 0; i < items.length(); i ++){
								o = items.getJSONObject(i);
								Answer a = getAnswer(o);
								parsedResult.add(a);
								answerDAO.saveAnswer(a, currentQuestion.qId);
							}
							if(parsedResult.isEmpty()){
								updateView(ResultStatus.EMPTY);
							} else {
								updateView(ResultStatus.SUCCESS);
								answerDAO.closeDb();
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

	private void setupAnswersList() {
        ListView answersList = (ListView)findViewById(R.id.answersList);
        TextView questionView = new TextView(this);
        questionView.setText(Html.fromHtml(currentQuestion.contents));
        answersList.addHeaderView(questionView);
		if(adapter == null) {
			adapter = 
				new AnswersAdapter(this, R.layout.search_answer, parsedResult);
			answersList.setAdapter(adapter);
		}
	}
	
	private void lookupOffline(){
		answerDAO.openDb();
		updateView(ResultStatus.PROGRESS);
		parsedResult = null;
		parsedResult = answerDAO.lookup(currentQuestion.qId);
		adapter.clear();
		adapter.addAll(parsedResult);
		//parsedResult.clear();
		if(parsedResult.isEmpty()){
			updateView(ResultStatus.EMPTY);
		} else {
			updateView(ResultStatus.SUCCESS);
		}
		answerDAO.closeDb();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		currentQuestion = 
			(Question)getIntent().getExtras().getParcelable("CURRENT_QUESTION");
        setContentView(R.layout.activity_answers);
		answerDAO = new AnswerDAO(this);
        setupAnswersList();
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
		answerDAO.closeDb();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub	
		super.onPause();
		answerDAO.closeDb();
	}
}
