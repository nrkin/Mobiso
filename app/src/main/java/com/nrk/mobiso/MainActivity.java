package com.nrk.mobiso;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
	//handler
	private void handleButtonClick(){
		//launch the activity with search term set.
        EditText searchTextBox = (EditText)findViewById(R.id.searchInput);
		String searchText = searchTextBox.getText().toString();
		if(searchText == null || searchText.length() == 0){
			return;
		}
		Intent i = new Intent(this, SearchActivity.class);
		i.putExtra("SEARCH_TEXT", searchText);
		startActivity(i);
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //createHomeLayout();
        //setContentView(homeLayout);
        setContentView(R.layout.activity_main);
        Button b = (Button)findViewById(R.id.searchButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick();
            }
        });
    }
}
