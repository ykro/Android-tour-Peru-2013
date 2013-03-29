package com.elementalgeeks.demoapiv1;

import java.util.ArrayList;

import com.elementalgeeks.demoapiv1.models.Person;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.view.Menu;
import android.widget.ArrayAdapter;

public class MainActivity extends ListActivity {
	private static APIHelper api = new APIHelper();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		new APITask().execute();		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	class APITask extends AsyncTask<Void, Void, ArrayList<Person>> {
		private ProgressDialog dialog = new ProgressDialog(MainActivity.this);
		
		@Override
		protected void onPreExecute() {
			dialog.setMessage("Actualizando...");
			dialog.show();
		}  
		
		@Override
		protected ArrayList<Person> doInBackground(Void... params) {
			return api.parseJSONArray(api.readJSONArrayFromURL(APIHelper.URL));			
		}

		@Override
		protected void onPostExecute(ArrayList<Person> result){
			dialog.dismiss();
			ArrayAdapter<Person> adapter = new ArrayAdapter<Person>(MainActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, result);
			setListAdapter(adapter);
		}
	}
}
