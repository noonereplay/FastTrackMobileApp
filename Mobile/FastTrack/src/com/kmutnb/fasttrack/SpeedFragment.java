package com.kmutnb.fasttrack;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass. Use the {@link SpeedFragment#newInstance}
 * factory method to create an instance of this fragment.
 * 
 */
public class SpeedFragment extends Fragment  implements OnClickListener{
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	private TextView user_speed_text,notify_text;
	private Button speed_track_btn;
	private ImageView notify_status_img;
	protected LocationManager locationManager;
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 10000; // in Milliseconds
    SpeedGPSListener speed_listener ;
    private static String TRACK_SPEED;
    private static String TRACK_SPEED_IDLE = "IDLE";
    private static String TRACK_SPEED_START = "START";
    private static String TRACK_SPEED_STOP = "STOP";
	private static int IMAGE_RESOURCE = 0;
	private static View speed_view;
	private static MediaPlayer mediaPlayer;
	private static String username;
	private static String profile_id;
	private static boolean path_reset = true;
    // flag for GPS status
	private static boolean isGPSEnabled = false;
 
    // flag for network status
    boolean isNetworkEnabled = false;
 
    // flag for GPS status
    boolean canGetLocation = false;
    private Context mContext;
    
	
	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment SpeedFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static SpeedFragment newInstance(String param1, String param2) {
		SpeedFragment fragment = new SpeedFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		
		return fragment;
	}

	public SpeedFragment() {
		// Required empty public constructor
		super();
		TRACK_SPEED = TRACK_SPEED_IDLE;

	}
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
			username = getArguments().getString("username");
			profile_id = getArguments().getString("profile_id");
		}
		speed_listener = new SpeedGPSListener();
		locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		mContext = getActivity().getApplicationContext();

        // getting GPS status
        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if(!isGPSEnabled){
        	
        }
	    
	}
	
	public boolean checkSystem(){
        // getting GPS status
        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        
		return isGPSEnabled ;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		if(speed_view == null){
			speed_view = inflater.inflate(R.layout.fragment_speed, container, false);
			user_speed_text = (TextView)speed_view.findViewById(R.id.speed_track_text);
			notify_text = (TextView)speed_view.findViewById(R.id.notify_text);
			notify_status_img = (ImageView)speed_view.findViewById(R.id.notify_status_img);
			speed_track_btn = (Button)speed_view.findViewById(R.id.speed_track_btn);
			speed_track_btn.setOnClickListener(this);
			mediaPlayer = MediaPlayer.create(speed_view.getContext(),  R.raw.siren);
		}else{
			(container).removeView(speed_view);
		}
		

		
		return speed_view;
	}
	
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		/*if(TRACK_SPEED.equals(TRACK_SPEED_START)){
			speedStart();
		}else if(TRACK_SPEED.equals(TRACK_SPEED_STOP)){
			speedStop();
		}
		else{
			speedStop();

		}*/

	}
	
	@Override
	public void onClick(View v) {
	
		//Toast.makeText(v.getContext(), "Device isn't support location", Toast.LENGTH_SHORT).show();
		
		if(v.getId() == R.id.speed_track_btn){
			
			if(TRACK_SPEED.equals(TRACK_SPEED_IDLE)){
				speedStart();
				
			}else if(TRACK_SPEED.equals(TRACK_SPEED_START)){
				speedStop();

			}else if(TRACK_SPEED.equals(TRACK_SPEED_STOP)){
				speedStart();
			}
			else{
				speedStop();
				IMAGE_RESOURCE = R.drawable.car;
			}
			notify_text.setText(TRACK_SPEED+"...");
			Toast.makeText(v.getContext(), "Speed "+TRACK_SPEED, Toast.LENGTH_SHORT).show();
			notify_status_img.setImageResource(IMAGE_RESOURCE);
			//speed_track_btn.setText(TRACK_SPEED);
		}
	}
	
	public void speedStart(){
		
		if(checkSystem()){

			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES, 
	                MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, speed_listener);
			//Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);		
			TRACK_SPEED = TRACK_SPEED_START;
			speed_track_btn.setText(TRACK_SPEED_STOP);
			IMAGE_RESOURCE = R.drawable.gps;
			path_reset = true;
		}else{
			speed_listener.showSettingsAlert();
			//speedStop();
		}

		
	}
	
	public void speedStop(){
		if(locationManager!=null){
			locationManager.removeUpdates(speed_listener);
			TRACK_SPEED = TRACK_SPEED_STOP;
			speed_track_btn.setText(TRACK_SPEED_START);
			IMAGE_RESOURCE =R.drawable.stop;
		}
	}


	private class SpeedGPSListener implements LocationListener {


		
		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			String latitude = String.format("%.7f", location.getLatitude());
	        String longitude = String.format("%.7f", location.getLongitude());

	        if(TRACK_SPEED.equals(TRACK_SPEED_START) && location.hasSpeed()){
	        	float speed = location.getSpeed()*3.60f;
	        		String speed_str = String.format("%.4f",speed);
	        		user_speed_text.setText(speed_str+" km/h");
	        
	        		PostLocation(longitude,latitude,speed_str);
	        		
	        		if(speed >= 40.0f){
	        			notify_status_img.setImageResource(R.drawable.caution);
	        			if (!mediaPlayer.isPlaying()) {
	        				mediaPlayer.start();
						}
	        		}else{
	        			notify_status_img.setImageResource(IMAGE_RESOURCE);
	        		}
	        		
	        		if (path_reset == true) {
	        			path_reset = false;
					}
	        
	        }
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		public void PostLocation(String longt , String lat, String speed){
			
			String reset = "no";
			if(path_reset == true){
				reset = "yes";
			}
			Date date = new Date();
			String chg_date =  new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(date);
			JSONObject param = new JSONObject();
			Toast.makeText(getActivity().getApplicationContext(), "latitude:"+lat+" longitude:"+longt
	        		+" speed:"+speed, Toast.LENGTH_SHORT).show();
			try {
				param.put("profile_id", profile_id);
				param.put("speed", speed);
				param.put("longtitude", longt);
				param.put("latitude", lat);
				param.put("chg_date", chg_date);
				param.put("reset", reset);
				StringEntity entity = new StringEntity(param.toString());
				AsyncHttpClient client = new AsyncHttpClient();
		    	client.addHeader("Content-Type", "application/json;charset=UTF-8");
		    	client.post(getActivity().getBaseContext(),"http://122.155.197.49/FastTrackHostApp/history/add", entity,
		    			RequestParams.APPLICATION_JSON, new JsonHttpResponseHandler(){

		    		@Override
		    				public void onSuccess(int statusCode,
		    						Header[] headers, String responseString) {
		    					// TODO Auto-generated method stub
		    			Log.w("FastTrack", responseString.toString());
		    				}
		    		@Override
		    				public void onFailure(int statusCode,
		    						Header[] headers, String responseString,
		    						Throwable throwable) {
		    					// TODO Auto-generated method stub
		    					Log.w("FastTrack", responseString);
		    				}
		    	});

		    	
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

		}

	    
	    public void showSettingsAlert(){
	        Object mContext;
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(SpeedFragment.this.getActivity());
	      
	        // Setting Dialog Title
	        alertDialog.setTitle("GPS is settings");
	  
	        // Setting Dialog Message
	        alertDialog.setMessage("GPS is not enabled or Connection Network Has Problem. Please go to settings menu?");
	  
	        // On pressing Settings button
	        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog,int which) {
	                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	                SpeedFragment.this.getActivity().startActivity(intent);
	            }
	        });
	  
	        
	        // on pressing cancel button
	        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            dialog.cancel();
	            }
	        });
	  		
	        // Showing Alert Message
	        alertDialog.show();
	    }
		
	}
	

}
