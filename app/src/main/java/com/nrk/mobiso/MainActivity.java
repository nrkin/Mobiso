package com.nrk.mobiso;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
	private LinearLayout homeLayout;
	private EditText searchTextBox;
	private Button searchButton;
	
	//handler
	private void handleButtonClick(){
		//launch the activity with search term set.
		String searchText = searchTextBox.getText().toString();
		if(searchText == null || searchText.length() == 0){
			return;
		}
		Intent i = new Intent(this, SearchActivity.class);
		i.putExtra("SEARCH_TEXT", searchText);
		startActivity(i);
	}
	private void createHomeLayout(){
		if(homeLayout == null) {
			homeLayout = new LinearLayout(this);
			homeLayout.setOrientation(1);
			searchTextBox = new EditText(this);
			searchTextBox.setHint(R.string.searchText_hint);
			searchTextBox.setLines(2);
			searchButton = new Button(this);
			searchButton.setText(R.string.searchButton_text);
			homeLayout.addView(searchTextBox, 0);
			homeLayout.addView(searchButton, 1);
			searchButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					handleButtonClick();
				}
			});
		}
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createHomeLayout();
        setContentView(homeLayout);
    }
}
