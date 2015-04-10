package com.kmutnb.fasttrack;


import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity {

	Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intent = new Intent(this, HomeSpeed.class);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void loginProcess(View view) {
        // Do something in response to button
    	
    	/*EditText editText = (EditText) findViewById(R.id.edit_message);
    	String message = editText.getText().toString();
    	intent.putExtra(EXTRA_MESSAGE, message);*/
    	
    	EditText userText = (EditText) findViewById(R.id.user_txt);
    	EditText passText = (EditText) findViewById(R.id.password_txt);
    	String username =userText.getText().toString();
    	String password = passText.getText().toString();
    	
    	RequestParams params = new RequestParams();
    	params.put("aut_username", username);
    	params.put("aut_password", password);
    	AsyncHttpClient client = new AsyncHttpClient();
    	client.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
    	 client.post("http://122.155.197.49/FastTrackHostApp/userprofile/aut",params ,new JsonHttpResponseHandler(){

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Error Connect to Server ", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub

				Log.w("FastTrack",  response.toString());
                // When the JSON response has status boolean value assigned with true
                try {
					if(!response.get("username").equals("")){
					 	Toast.makeText(getApplicationContext(), "You are successfully logged in!", Toast.LENGTH_LONG).show();
					 // Navigate to Home screen
					 	intent.putExtra("profile_id", response.getString("profile_id"));
					 	intent.putExtra("username", response.getString("username"));
					 	startActivity(intent);
					} 
					// Else display error message
					else{
					 Toast.makeText(getApplicationContext(), "Username or Password is invalid", Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
				}
			}
			


    		 
    	 });
    	
    	

    }
}
