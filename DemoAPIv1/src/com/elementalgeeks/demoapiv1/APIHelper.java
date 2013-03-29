package com.elementalgeeks.demoapiv1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.elementalgeeks.demoapiv1.models.Person;

import android.util.Log;

public class APIHelper {
	public final static String URL = "http://demo-backend-ykro.herokuapp.com/people.json";
	
	public JSONArray readJSONArrayFromURL(String url) {
		JSONArray result = null;
		
		try {
			HttpClient http_client = new DefaultHttpClient();
			HttpGet http_get = new HttpGet(url);
			HttpResponse response = http_client.execute(http_get);
			HttpEntity entity = response.getEntity();
			
			InputStream input_stream = entity.getContent();
			BufferedReader buffered_reader = new BufferedReader(new InputStreamReader(input_stream,"iso-8859-1"),8);
			
	        StringBuilder string_builder = new StringBuilder();	        
	        String line = null;
	        while ((line = buffered_reader.readLine()) != null) {
	        	string_builder.append(line);	        	
	        }	        
	        input_stream.close();	        
	        result = new JSONArray(string_builder.toString());			
		} catch (JSONException e){			
			Log.e("DemoAPI", "Error parsing data " + e.toString());		
		} catch (IOException e) {
			Log.e("DemoAPI","IO Connection error" + e.toString());
		} catch (Exception e) {			
			Log.e("DemoAPI","Error" + e.toString());			
		}
		
		return result;
	}
	
	public ArrayList<Person> parseJSONArray(JSONArray data) {
		ArrayList<Person> result = new ArrayList<Person>();
	
		if(data != null && data.length() > 0) {
			JSONArray names;
			String current_name;
			Person current_person;
			JSONObject current_object;				
							
			for (int i = 0;i < data.length();i++) {
				try {
					current_object = data.getJSONObject(i);
					names = current_object.names();
					current_person = new Person();
					for (int object_index = 0;object_index < current_object.length();object_index++) {
						current_name = names.getString(object_index);
						if (current_name.equals("name")) {
							current_person.setName(current_object.getString(current_name));
						} else if (current_name.equals("nickname")) {
							current_person.setNickname(current_object.getString(current_name));
						} else if (current_name.equals("email")) {
							current_person.setEmail(current_object.getString(current_name));
						} else if (current_name.equals("address")) {
							current_person.setAddress(current_object.getString(current_name));
						} else if (current_name.equals("phone")) {
							current_person.setPhone(current_object.getString(current_name));							
						}
					}
					result.add(current_person);
				} catch (JSONException e) {
					Log.e("DemoAPI","JSON error" + e.toString());
				}
			}
		}
		return result;
	}
}
