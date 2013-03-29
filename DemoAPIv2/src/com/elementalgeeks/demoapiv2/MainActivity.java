package com.elementalgeeks.demoapiv2;

import java.io.IOException;
import java.util.ArrayList;

import us.monoid.json.JSONException;
import us.monoid.web.Resty;

import com.elementalgeeks.demoapiv2.models.Person;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;

public class MainActivity extends ListActivity {
	public final static String URL = "http://demo-backend-ykro.herokuapp.com/people.json";	

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
			ArrayList<Person> result = new ArrayList<Person>(); 
			Resty client = new Resty();
			ObjectMapper mapper = new ObjectMapper();
			 
			try {
				result = mapper.readValue(client.json(URL).array().toString(), new TypeReference<ArrayList<Person>>(){});
			} catch (JsonParseException e) {
				Log.e("DemoAPI", "Error parsing data " + e.toString());
			} catch (JsonMappingException e) {
				Log.e("DemoAPI", "Error mapping data " + e.toString());
			} catch (IOException e) {
				Log.e("DemoAPI","IO Connection error" + e.toString());			
			} catch (JSONException e) {
				Log.e("DemoAPI", "Error parsing data " + e.toString());
			} catch (Exception e) {			
				Log.e("DemoAPI","Error" + e.toString());				
			}
			
			return result;
		}

		@Override
		protected void onPostExecute(ArrayList<Person> result){
			dialog.dismiss();
			ArrayAdapter<Person> adapter = new ArrayAdapter<Person>(MainActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, result);
			setListAdapter(adapter);
		}
	}
}
